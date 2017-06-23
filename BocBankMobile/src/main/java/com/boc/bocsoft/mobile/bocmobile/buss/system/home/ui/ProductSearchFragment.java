package com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui;

import android.content.Intent;
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

import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryResult;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussActivity;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.SpUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.common.eventbus.BocEventBus;
import com.boc.bocsoft.mobile.bocmobile.buss.common.lianlong.ModuleDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.system.common.data.InvestDataCenter;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.searchallpro.SearchAllProParams;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.searchallpro.SearchAllProResult;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.presenter.ProductSearchPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.adapter.ProductSearchAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.system.main.eventbus.event.GoInvestEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.DetailsRequestBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthDetailsFragment;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.cr.bus.product.model.CRgetProductList.CRgetProductListResult;
import com.boc.bocsoft.mobile.framework.utils.ViewFinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

/**
 * 产品搜索页面
 * Created by gwluo on 2016/9/6.
 */
public class ProductSearchFragment extends MvpBussFragment<ProductSearchContract.Presenter> implements View.OnClickListener, ProductSearchContract.View {
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
        et_search_fund = finder.find(R.id.et_search_fund);
        ll_hisRed_title = finder.find(R.id.ll_hisRed_title);
        tv_hot_product_title = finder.find(R.id.tv_hot_product_title);
    }


    List<String> productTypeList;

    @Override
    public void initData() {
        super.initData();
        productTypeList = new ArrayList<>();
        expandAdapter = new ProductSearchAdapter(mContext);
        // 判断是已经有值
        if (PublicUtils.isEmpty(InvestDataCenter.getInstance().getInvestProducts())) {
            getHotProduct();
        } else {
            setHotProductData(setDefaultHotProduct(InvestDataCenter.getInstance().getInvestProducts()));
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
                String productType = productTypeList.get(groupPosition);
                if (productType.equals("基金")) {//基金
                    List<SearchAllProResult.Fund> fundList = productResult.getFundList();
                    SearchAllProResult.Fund fund = fundList.get(childPosition);
                    ModuleDispatcher.gotoFincMudule(getActivity(), fund.getFundBakCode());
                } else if (productType.equals("理财")) {//理财
                    List<SearchAllProResult.Lc> lcList = productResult.getLcList();
                    SearchAllProResult.Lc lc = lcList.get(childPosition);
                    lcProId = lc.getProId();

                    lcProKind = lc.getKind();
                    if (ApplicationContext.getInstance().isLogin()) {
                        showLoadingDialog();
                        getPresenter().queryFinanceAccountInfo();
                    } else {
                        jumpToLc("");
                    }
                }
                return false;
            }
        });
    }

    private String lcProId = "";//理财产品id
    private String lcProKind = "";//理财产品性质

    /**
     * 跳转热门产品
     */
    private void jumpHotProduct(CRgetProductListResult.ProductBean productBean) {
        if ("0".equals(productBean.getType())) {// 基金
            ModuleDispatcher.gotoFincMudule(getActivity(), productBean.getProductCode());
        } else if ("1".equals(productBean.getType())) {//理财
            lcProId = productBean.getProductCode();
            lcProKind = productBean.getProductNature();
            if (ApplicationContext.getInstance().isLogin()) {
                showLoadingDialog();
                getPresenter().queryFinanceAccountInfo();
            } else {
                jumpToLc("");
            }
        }
    }

    /**
     * 跳转到理财
     *
     * @param ibKnum 省行联行号
     */
    private void jumpToLc(String ibKnum) {
        DetailsRequestBean bean = new DetailsRequestBean();
        bean.setProdCode(lcProId);
        bean.setProdKind(lcProKind);
        bean.setIbknum(ibKnum);
        Bundle bundle = new Bundle();
        bundle.putBoolean(WealthDetailsFragment.OTHER, true);
        bundle.putParcelable(WealthDetailsFragment.DETAILS_REQUEST, bean);

        Intent intent = new Intent();
        intent.setClass(getContext(), BussActivity.class);
        intent.putExtra(BussActivity.MODULE_CLASS, WealthDetailsFragment.class);
        intent.putExtras(bundle);
        startActivity(intent);
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
        String savedProductHis = SpUtils.getSpString(mContext, ApplicationConst.PRODUCT_QUERY_HIS, "");
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
                SpUtils.saveString(mContext, ApplicationConst.PRODUCT_QUERY_HIS, jsArray.toString());
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
    public ProductSearchContract.Presenter initPresenter() {
        return new ProductSearchPresenter(this);

    }

    @Override
    public void searchProductSucc(SearchAllProResult resultList) {
        closeProgressDialog();
        handSearchResult(resultList);
    }

    @Override
    public void searchProductFail() {
        closeProgressDialog();
    }

    List<CRgetProductListResult.ProductBean> hotProductList;//热门产品结果

    @Override
    public void getProductListFail() {
        closeProgressDialog();
        setHotProductData(setDefaultHotProduct(null));
    }

    @Override
    public void getProductListSucc(CRgetProductListResult result) {
        closeProgressDialog();
        setHotProductData(setDefaultHotProduct(result.getArrayList()));
        //保存数据，下次不再重新请求
        InvestDataCenter.getInstance().updateInvestProducts(result.getArrayList());
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

    /**
     * 设置两个默认的热门产品，最少返回两个中银日积月累-日计划 和 收益累进
     */
    private List<CRgetProductListResult.ProductBean>
    setDefaultHotProduct(List<CRgetProductListResult.ProductBean> list) {
        List<CRgetProductListResult.ProductBean> returnList = new ArrayList<>();
        String daydayUpCode = "AMRJYL01";//中银日积月累-日计划 产品码
        boolean containItem = isContainItem(list, daydayUpCode);
        if (!containItem) {
            //中银日积月累-日计划
            CRgetProductListResult.ProductBean daydayUp = new CRgetProductListResult.ProductBean();
            daydayUp.setProductCode(daydayUpCode);
            daydayUp.setType("1");
            daydayUp.setName("中银日积月累-日计划");
            daydayUp.setProductNature("0");
            returnList.add(daydayUp);
        }
        String moneyUpCode = "AMSYLJ01";//收益累进 产品码
        containItem = isContainItem(list, moneyUpCode);
        if (!containItem) {
            //收益累进
            CRgetProductListResult.ProductBean moneyUp = new CRgetProductListResult.ProductBean();
            moneyUp.setProductCode(moneyUpCode);
            moneyUp.setType("1");
            moneyUp.setName("收益累进");
            moneyUp.setProductNature("0");
            returnList.add(moneyUp);
        }
        if (!PublicUtils.isEmpty(list)) {
            returnList.addAll(list);
        }
        return returnList;
    }

    private boolean isContainItem(List<CRgetProductListResult.ProductBean> list, String itemCode) {
        if (list == null || list.size() == 0) {
            return false;
        }
        for (CRgetProductListResult.ProductBean bean : list) {
            if (bean.getProductCode().equals(itemCode)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void queryFinanceAccountInfoFail() {
        closeProgressDialog();
    }

    @Override
    public void queryFinanceAccountInfoSuccess(PsnXpadAccountQueryResult result) {
        closeProgressDialog();
        if (result.getList().size() == 0) {
            jumpToLc("");
        } else {
            jumpToLc(result.getList().get(0).getIbkNumber());
        }
    }

    @Override
    public void setPresenter(ProductSearchContract.Presenter presenter) {

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
        String savedProductHis = SpUtils.getSpString(mContext, ApplicationConst.PRODUCT_QUERY_HIS, "");
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
                SpUtils.saveString(mContext, ApplicationConst.PRODUCT_QUERY_HIS, "");
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
            map.put(ApplicationConst.QUERY_HIS_NAME, jsArray.getString(i - 1));
            list.add(map);
        }
        from = new String[]{ApplicationConst.QUERY_HIS_NAME};
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
        getPresenter().searchAllPro(params);
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
}
