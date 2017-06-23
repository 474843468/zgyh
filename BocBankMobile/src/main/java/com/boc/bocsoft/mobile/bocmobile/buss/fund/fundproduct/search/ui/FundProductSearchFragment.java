package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.search.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.SpUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.common.eventbus.BocEventBus;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.ui.FundProductDetailFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.model.InvstBindingInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.search.presenter.FundProductSearchPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.system.common.data.InvestDataCenter;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.searchallpro.SearchAllProParams;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.searchallpro.SearchAllProResult;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.adapter.ProductSearchAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.system.main.eventbus.event.GoInvestEvent;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.cr.bus.product.model.CRgetProductList.CRgetProductListResult;
import com.boc.bocsoft.mobile.framework.utils.ViewFinder;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基金产品搜索页面
 * Created by liuzc on 2016/12/31.
 */
public class FundProductSearchFragment extends MvpBussFragment<FundProductSearchContract.Presenter>
        implements View.OnClickListener, FundProductSearchContract.View {
    private View mRoot;
    private ViewFinder finder;
    private ImageView leftIconIv;
    private GridView gv_hot_product;
    private ListView lv_record_history;
    private ExpandableListView elv_product_result;
    private ViewGroup cus_default_view;
    private ViewGroup cus_qry_result_view;
    private ViewGroup cus_qry_empty_view;
    private Button btn_qry_fund;
    private TextView tv_look_for_hot_product;
    private EditText et_search_fund;
    private ViewGroup ll_hisRed_title;
    private View tv_hot_product_title;

    private InvstBindingInfoViewModel mBindingInfoModel = null; //基金账户信息

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRoot = mInflater.inflate(R.layout.boc_fragment_product_search, null);
        finder = new ViewFinder(mRoot);
        return mRoot;
    }

    @Override
    public void initView() {
        leftIconIv = finder.find(R.id.leftIconIv);
        gv_hot_product = finder.find(R.id.gv_hot_product);
        lv_record_history = finder.find(R.id.lv_record_history);
        elv_product_result = finder.find(R.id.elv_product_result);
        cus_default_view = finder.find(R.id.cus_default_view);
        cus_qry_result_view = finder.find(R.id.cus_qry_result_view);
        cus_qry_empty_view = finder.find(R.id.cus_qry_empty_view);
        btn_qry_fund = finder.find(R.id.btn_qry_fund);
        tv_look_for_hot_product = finder.find(R.id.tv_look_for_hot_product);
        tv_look_for_hot_product.setVisibility(View.GONE);
        et_search_fund = finder.find(R.id.et_search_fund);
        et_search_fund.setHint(getString(R.string.boc_fund_search_hint));
        ll_hisRed_title = finder.find(R.id.ll_hisRed_title);
        tv_hot_product_title = finder.find(R.id.tv_hot_product_title);
    }


    List<String> productTypeList;

    @Override
    public void initData() {
        super.initData();

        mBindingInfoModel = (InvstBindingInfoViewModel) getArguments().getSerializable(DataUtils.KEY_BINDING_INFO);

        productTypeList = new ArrayList<>();
        expandAdapter = new ProductSearchAdapter(mContext);
        // 判断是已经有值
        if (PublicUtils.isEmpty(InvestDataCenter.getInstance().getInvestProducts())) {
            getHotProduct();
        } else {
            setHotProductData(InvestDataCenter.getInstance().getInvestProducts());
        }
        updateHisRecView();
    }

    private AdapterView.OnItemClickListener gvItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            CRgetProductListResult.ProductBean productBean = hotProductList.get(position);
            jumpHotProduct(productBean);
        }
    };

    @Override
    public void setListener() {
        super.setListener();
        leftIconIv.setOnClickListener(this);
        tv_look_for_hot_product.setOnClickListener(this);
        btn_qry_fund.setOnClickListener(this);
        gv_hot_product.setOnItemClickListener(gvItemListener);
        lv_record_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String productName = ((TextView) view.findViewById(R.id.tv_product_name)).getText().toString();
                et_search_fund.setText(productName);
                qryProduct();
            }
        });
        elv_product_result.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                List<SearchAllProResult.Fund> fundList = productResult.getFundList();
                SearchAllProResult.Fund fund = fundList.get(childPosition);

                gotoFundDetail(fund.getFundId());
                return false;
            }
        });
    }

    private String lcProId = "";//理财产品id
    private String lcProKind = "";//理财产品性质

    /**
     * 进入基金产品详情页
     * @param fundID
     */
    private void gotoFundDetail(String fundID){
        FundProductDetailFragment fragment = new FundProductDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DataUtils.KEY_FUND_ID, fundID);
        fragment.setArguments(bundle);
        start(fragment);
    }

    /**
     * 跳转热门产品
     */
    private void jumpHotProduct(CRgetProductListResult.ProductBean productBean) {
        gotoFundDetail(productBean.getProductCode());
    }

    /**
     * 处理搜索结果
     *
     * @param resultList
     */
    private void handSearchResult(SearchAllProResult resultList) {
        // 是否有数据
        boolean resultEmpty = resultList.getForexList().size() +
                resultList.getFundList().size() +
                resultList.getLcList().size() == 0 ? true : false;
        saveHisRecord();
        if (resultEmpty) {
            cus_qry_result_view.setVisibility(View.GONE);
            cus_default_view.setVisibility(View.GONE);
            cus_qry_empty_view.setVisibility(View.VISIBLE);
        } else {
            cus_qry_result_view.setVisibility(View.VISIBLE);
            cus_default_view.setVisibility(View.GONE);
            cus_qry_empty_view.setVisibility(View.GONE);
            updateHisRecView();
            setProductSearchData(resultList);
        }
    }

    /**
     * 保存历史查询记录
     */
    private void saveHisRecord() {
        String savedProductHis = SpUtils.getSpString(mContext, ApplicationConst.FUND_PRODUCT_QUERY_HIS, "");
        try {
            JSONArray jsArray;
            if (savedProductHis.length() == 0) {
                jsArray = new JSONArray();
            } else {
                jsArray = new JSONArray(savedProductHis);
            }
            boolean isContain = false;
            for (int i = 0; i < jsArray.length(); i++) {
                String object = (String) jsArray.get(i);
                if (object.equals(et_search_fund.getText().toString())) {
                    isContain = true;
                    break;
                } else {
                    isContain = false;
                }
            }
            if (!isContain) {
                jsArray.put(et_search_fund.getText().toString());
                SpUtils.saveString(mContext, ApplicationConst.FUND_PRODUCT_QUERY_HIS, jsArray.toString());
            }
        } catch (Exception e) {

        }
    }

    private LinkedHashMap<String, List<String>> qryResult;//存储全量数据
    private LinkedHashMap<String, List<String>> qryResultCopy;// 存储展示数据
    private List<String> productQrykeys;
    private ProductSearchAdapter expandAdapter;
    private List<Boolean> isExpandList;

    /**
     * 设置产品查询数据
     */
    private void setProductSearchData(SearchAllProResult resultList) {
        qryResult = translateProductResult(resultList);
        // 为了显示折叠时也要展示数据expandListView始终都是展开状态，只是显示折叠和展开时数据不同。
        // isCollapseList记录显示状态是展开还是折叠
        isExpandList = new ArrayList<>();
        for (int i = 0; i < qryResult.size(); i++) {
            isExpandList.add(true);//默认展开
        }
        // 分类的key
        Iterator ite = qryResult.keySet().iterator();
        productQrykeys = new ArrayList<>();
        while (ite.hasNext()) {
            productQrykeys.add((String) ite.next());
        }
        // 数据copy用于展示折叠时的数据
        qryResultCopy = copyProData(qryResult, productQrykeys);

        expandAdapter.updateDate(qryResult, qryResult, isExpandList, productQrykeys);
        elv_product_result.setOnGroupCollapseListener(collapseListener);
        elv_product_result.setOnGroupExpandListener(expandListener);
        elv_product_result.setAdapter(expandAdapter);
        // 初始折叠用collapseGroup，展开用expandGroup；目前默认折叠
        for (int i = 0; i < qryResult.size(); i++) {
            elv_product_result.collapseGroup(i);
        }
    }

    /**
     * 展开监听
     */
    private ExpandableListView.OnGroupExpandListener expandListener =
            new ExpandableListView.OnGroupExpandListener() {

                @Override
                public void onGroupExpand(int groupPosition) {
                    // 展开时对qryResultCopy数据进行设置
                    String key = productQrykeys.get(groupPosition);
                    List<String> result = qryResultCopy.get(key);
                    result.clear();
                    result.addAll(qryResult.get(key));
                }
            };
    /**
     * 收缩监听
     */
    private ExpandableListView.OnGroupCollapseListener collapseListener =
            new ExpandableListView.OnGroupCollapseListener() {
                @Override
                public void onGroupCollapse(int groupPosition) {
                    //为了折叠时显示数据都处理成展开，只是数据不同。自己记录显示状态是展开还是折叠
                    boolean isCollapse = isExpandList.get(groupPosition);
                    //展开显示所有数据，折叠如果数据小于等于3条都显示大于三条只显示三条
                    if (isCollapse) {
                        String key = productQrykeys.get(groupPosition);
                        List<String> result = qryResultCopy.get(key);
                        // 折叠时保留三条或者更少数据
                        if (result.size() > 3) {
                            for (int i = result.size() - 1; i > 2; i--) {
                                result.remove(i);
                            }
                        }
                        expandAdapter.updateDate(qryResultCopy, qryResult, isExpandList, productQrykeys);
                    } else {
                        expandAdapter.updateDate(qryResult, qryResult, isExpandList, productQrykeys);
                    }
                    // 做展开处理
                    elv_product_result.expandGroup(groupPosition, true);
                    //手动记录状态
                    isExpandList.remove(groupPosition);
                    isExpandList.add(groupPosition, !isCollapse);
                }

            };

    /**
     * 复制产品查询数据
     */
    private LinkedHashMap<String, List<String>> copyProData(
            LinkedHashMap<String, List<String>> result, List<String> keys) {
        LinkedHashMap<String, List<String>> resultCopy = new LinkedHashMap<>();
        for (int i = 0; i < result.size(); i++) {
            List<String> valueList = new ArrayList<>();
            valueList.addAll(result.get(keys.get(i)));
            resultCopy.put(keys.get(i), valueList);
        }
        return resultCopy;
    }

    private SearchAllProResult productResult;

    /**
     * 对产品查询结果进行转换
     *
     * @param resultList
     * @return
     */
    private LinkedHashMap translateProductResult(SearchAllProResult resultList) {
        productResult = resultList;
        LinkedHashMap<String, List<String>> products = new LinkedHashMap<>();
        List<SearchAllProResult.Lc> lcList = resultList.getLcList();//	理财列表
        List<String> lcs = new ArrayList<>();
        for (SearchAllProResult.Lc lc :
                lcList) {
            lcs.add(lc.getProName());
        }
        if (lcList.size() > 0) {// 没有数据不显示
            productTypeList.add("理财");
            products.put("理财", lcs);
        }
        List<SearchAllProResult.Fund> fundList = resultList.getFundList();//基金列表
        List<String> funds = new ArrayList<>();
        for (SearchAllProResult.Fund fund :
                fundList) {
            funds.add(fund.getFundName());
        }
        if (fundList.size() > 0) {// 没有数据不显示
            productTypeList.add("基金");
            products.put("基金", funds);
        }

        return products;
    }


    @Override
    public void searchProductSucc(SearchAllProResult resultList) {
        closeProgressDialog();
        handSearchResult(resultList);
    }

    @Override
    public void searchProductFail(BiiResultErrorException biiResultErrorException) {

    }

    List<CRgetProductListResult.ProductBean> hotProductList;//热门产品结果

    @Override
    public void getProductListSucc(CRgetProductListResult result) {
        closeProgressDialog();
        setHotProductData(result.getArrayList());
        //保存数据，下次不再重新请求
        InvestDataCenter.getInstance().updateInvestProducts(result.getArrayList());
    }

    @Override
    public void getProductListFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        setHotProductData(null);
    }

    private void setHotProductData(List<CRgetProductListResult.ProductBean> hotProList) {
        List<Map<String, String>> list = new ArrayList<>();
        HashMap<String, String> map = null;
        hotProductList = hotProList;
        // 热门产品无结果标题不显示
        if (hotProductList.size() == 0) {
            tv_hot_product_title.setVisibility(View.GONE);
        } else {
            tv_hot_product_title.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < hotProductList.size(); i++) {
            map = new HashMap<>();
            map.put("productName", hotProductList.get(i).getName());
            list.add(map);
        }
        String[] form = new String[]{"productName"};
        int[] to = new int[]{R.id.tv_product_name};
        gv_hot_product.setAdapter(new SimpleAdapter(mContext, list,
                R.layout.boc_item_hot_product, form, to));
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    private String[] from;

    /**
     * 更新历史记录界面（是否添加清空历史记录）
     */
    private void updateHisRecView() {
        String savedProductHis = SpUtils.getSpString(mContext, ApplicationConst.FUND_PRODUCT_QUERY_HIS, "");
        try {
            JSONArray jsArray;
            if (savedProductHis.length() == 0) {
                jsArray = new JSONArray();
                ll_hisRed_title.setVisibility(View.GONE);
            } else {
                jsArray = new JSONArray(savedProductHis);
                ll_hisRed_title.setVisibility(View.VISIBLE);
                addCleanHisRecView();
            }
            updateHisRecData(jsArray);
        } catch (Exception e) {

        }
    }

    /**
     * 添加历史记录最下方的清空历史记录
     */
    private void addCleanHisRecView() {
        final LinearLayout footView = (LinearLayout) View.inflate(getActivity(), R.layout.boc_item_hot_product, null);
        footView.setBackgroundResource(0);
        TextView text = (TextView) footView.findViewById(R.id.tv_product_name);
        text.setBackgroundResource(R.color.boc_common_cell_color);
        text.setText(getString(R.string.boc_clear_history));
        text.setTextColor(mContext.getResources().getColor(R.color.boc_common_view_line));
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtils.saveString(mContext, ApplicationConst.FUND_PRODUCT_QUERY_HIS, "");
                updateHisRecView();
                lv_record_history.removeFooterView(footView);
            }
        });
        lv_record_history.addFooterView(footView);
    }

    /**
     * 更新历史记录数据
     *
     * @param jsArray
     * @throws Exception
     */
    private void updateHisRecData(JSONArray jsArray) throws Exception {
        List<Map<String, String>> list = new ArrayList<>();
        HashMap<String, String> map = null;
        // 最新搜索结果在最上面,最多五条
        for (int i = jsArray.length(); i > (jsArray.length() > 5 ? jsArray.length() - 5 : 0); i--) {
            map = new HashMap<>();
            map.put(ApplicationConst.FUND_QUERY_HIS_NAME, jsArray.getString(i - 1));
            list.add(map);
        }
        from = new String[]{ApplicationConst.FUND_QUERY_HIS_NAME};
        int[] to = new int[]{R.id.tv_product_name};
        lv_record_history.setAdapter(new SimpleAdapter(mContext, list,
                R.layout.boc_item_record_history, from, to));
    }

    /**
     * 获取热门产品
     */
    private void getHotProduct() {
        showLoadingDialog();
        getPresenter().cRgetProductList();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.leftIconIv) {
            titleLeftIconClick();
        } else if (v.getId() == R.id.btn_qry_fund) {
            qryProduct();
        } else if (v.getId() == R.id.tv_look_for_hot_product) {
            pop();
            BocEventBus.getInstance().post(new GoInvestEvent());
        }
    }

    /**
     * 搜索产品
     */
    private void qryProduct() {
        String qryContent = et_search_fund.getText().toString().trim();
        if (StringUtils.isEmpty(qryContent)) {
            showErrorDialog("搜索条件不能为空");
            return;
        }
        showLoadingDialog();//需要在请求接口前调用，此代码会startPresenter
        SearchAllProParams params = new SearchAllProParams();
        params.setKey(qryContent);


        getPresenter().searchAllPro(params, isLoginAndBinding());
    }

    /**
     * 是否已经登陆并且绑定了账户
     * @return
     */
    private boolean isLoginAndBinding() {
        return (ApplicationContext.getInstance().isLogin() && mBindingInfoModel != null
                && !StringUtils.isEmptyOrNull(mBindingInfoModel.getAccount()));
    }

    @Nullable
    @Override
    public View getView() {
        return mRoot;
    }

    @Override
    public void onStop() {
        super.onStop();
        hideSoftInput();
    }

    @Override
    public void setPresenter(FundProductSearchContract.Presenter presenter) {

    }

    @Override
    protected FundProductSearchContract.Presenter initPresenter() {
        return new FundProductSearchPresenter(this);
    }
}
