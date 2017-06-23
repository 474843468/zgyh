package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullableListView;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.adapter.InvestTreatyAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.model.InvestTreatyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.presenter.InvestTreatyContract;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.presenter.InvestTreatyPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * 投资协议管理界面
 * Created by guokai on 2016/9/7.
 */
public class InvestTreatyFragment extends MvpBussFragment<InvestTreatyPresenter> implements InvestTreatyContract.InvestTreatyView, View.OnClickListener {

    /**
     * 有效协议
     */
    private static final String AGRSTATE_VALID = "1";
    /**
     * 无效协议
     */
    private static final String AGRSTATE_INVALID = "2";
    //结果页
    public static final String WEALTHPRODUCTFRAGMENT = "WealthProductFragment";
    private String resquestCode = "";

    private RadioButton btnValid;
    private RadioButton btnInvalid;
    private LinearLayout llListView;
    //详情的ListView
    PullableListView paymentRecordListView;
    //详情的上拉刷新
    PullToRefreshLayout paymentRecordRefresh;
    private InvestTreatyAdapter adapter;
    private ImageView btnBack;
    private LinearLayout tvEmpty;
    private RadioGroup radioGroup;
    private InvestTreatyModel model;

    //当前加载页码
    private int pageCurrentIndex;
    //每页大小
    private static int pageSize;
    private String _refresh = "false";
    private String state = AGRSTATE_VALID;
    private List<InvestTreatyModel.CapacityQueryBean> adapterList;

    private boolean isRefresh = false;

    public InvestTreatyFragment() {
    }

    @SuppressLint("ValidFragment")
    public InvestTreatyFragment(String resquestCode) {
        this.resquestCode = resquestCode;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.invest_treaty_view, null);
    }

    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    protected void titleLeftIconClick() {
        if (resquestCode.equals(WEALTHPRODUCTFRAGMENT)) {
            goHomePage();
        } else {
            super.titleLeftIconClick();
        }
    }

    @Override
    public boolean onBack() {
        if (resquestCode.equals(WEALTHPRODUCTFRAGMENT)) {
            goHomePage();
            return false;
        } else {
            return super.onBack();
        }
    }

    private void goHomePage() {
        if (findFragment(WealthProductFragment.class) == null)
            mActivity.finish();
        else
            popToAndReInit(WealthProductFragment.class);
    }

    @Override
    public void reInit() {
        initData();
    }

    @Override
    public void initView() {
        radioGroup = (RadioGroup) mContentView.findViewById(R.id.radio_group);
        btnValid = (RadioButton) mContentView.findViewById(R.id.btn_valid);
        btnInvalid = (RadioButton) mContentView.findViewById(R.id.btn_invalid);
        paymentRecordListView = (PullableListView) mContentView.findViewById(R.id.lv_transfer_query);
        paymentRecordRefresh = (PullToRefreshLayout) mContentView.findViewById(R.id.refresh_transfer_query);
        llListView = (LinearLayout) mContentView.findViewById(R.id.ll_listview);
        tvEmpty = (LinearLayout) mContentView.findViewById(R.id.noResult);
        btnBack = (ImageView) mContentView.findViewById(R.id.btn_back);
        adapter = new InvestTreatyAdapter(mContext);
        paymentRecordListView.setAdapter(adapter);
    }

    @Override
    public void initData() {
        pageSize = ApplicationConst.PAGE_SIZE;
        pageCurrentIndex = 0;
//        boolean[] openWealth = WealthProductFragment.getInstance().isOpenWealth();
//        if (openWealth[0] == false || openWealth[1] == false){
//            InvesttreatyFragment fragment = new InvesttreatyFragment();
//            Bundle bundle = new Bundle();
//            bundle.putBooleanArray(InvesttreatyFragment.INPUT_DATA,new boolean[]{true,true,false});
//            fragment.setArguments(bundle);
//            start(fragment);
//            return;
//        }

        model = new InvestTreatyModel();
        adapterList = new ArrayList<>();
        showLoadingDialog(false);
        /**
         * 客户智能协议查询
         */
        getPresenter().psnXpadCapacityQuery(buildInvestTreatyModel());
    }

    @Override
    public void setListener() {
        btnValid.setOnClickListener(this);
        btnInvalid.setOnClickListener(this);
        //listView的item点击事件
        paymentRecordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InvestTreatyModel.CapacityQueryBean item1 = (InvestTreatyModel.CapacityQueryBean) adapter.getItem(position);
                if (radioGroup.getCheckedRadioButtonId() == R.id.btn_valid) {
                    start(new InvestTreatyInfoFragment(item1, true));
                } else {
                    start(new InvestTreatyInfoFragment(item1, false));
                }
            }
        });

        //上拉加载
        paymentRecordRefresh.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                isRefresh = true;
                if (adapterList != null) {
                    if (adapterList.size() < model.getRecordNumber()) {
                        queryPaymentRecordList();
                    } else {
                        paymentRecordRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                    }
                } else {
                    adapterList.clear();
                    pageCurrentIndex = 0;
                    _refresh = "true";
                    queryPaymentRecordList();
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });

    }

    /**
     * 调用接口，查询协议列表
     */
    private void queryPaymentRecordList() {
        if (pageCurrentIndex == 0) {
            //开启加载对话框
            showLoadingDialog(false);
        }
        startPresenter();
        /**
         * 客户智能协议查询
         */
        getPresenter().psnXpadCapacityQuery(buildInvestTreatyModel());
    }

    /**
     * 封装上送参数
     *
     * @return
     */
    private InvestTreatyModel buildInvestTreatyModel() {
        model.setAgrType("0");
        model.setAgrState(state);
        model.setPageSize(pageSize + "");
        model.setCurrentIndex(pageCurrentIndex + "");
        model.set_refresh(_refresh);
        return model;
    }

    /**
     * 协议列表回调
     */
    @Override
    public void psnXpadCapacityQuery(InvestTreatyModel treatyModel) {
        closeProgressDialog();
        this.model = treatyModel;
        List<InvestTreatyModel.CapacityQueryBean> list = model.getList();
        adapterList.addAll(list);
        if (isRefresh) {
            paymentRecordRefresh.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
        }
        if (PublicUtils.isEmpty(adapterList)) {
            llListView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            llListView.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
            adapter.setDatas(adapterList);
            pageCurrentIndex += pageSize;//此处的索引是按记录的索引来走
            _refresh = "false";
        }
    }

    /**
     * 协议列表回调 失败
     */
    @Override
    public void psnXpadCapacityQueryFailed() {
        llListView.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public InvestTreatyModel getModel() {
        return model;
    }

    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    /**
     * 有效和无效协议的点击
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_valid) {
            state = AGRSTATE_VALID;
            adapter.setState(true);
            initData();
        } else if (id == R.id.btn_invalid) {
            state = AGRSTATE_INVALID;
            adapter.setState(false);
            initData();
        }
    }

    @Override
    protected InvestTreatyPresenter initPresenter() {
        return new InvestTreatyPresenter(this);
    }

}
