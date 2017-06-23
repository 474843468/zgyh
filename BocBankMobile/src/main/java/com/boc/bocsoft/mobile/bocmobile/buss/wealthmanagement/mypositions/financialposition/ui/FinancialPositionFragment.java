package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.emptyview.CommonEmptyView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.measureview.MeasureListView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.wealthmanagemenadviertisementview.WealthManagemenAdviertisementView;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.ui.AccountMainFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.adapter.FinancialPositionListAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadaccountquery.PsnXpadAccountQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadproductbalancequery.PsnXpadProductBalanceQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.presenter.FinancialPositionContract;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.presenter.FinancialPositionPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.util.FinancialPositionCodeModeUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadproductdetailquery.PsnXpadProductDetailQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.ui.PurchaseFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;

import java.util.ArrayList;
import java.util.List;

/**
 * 中银理财-我的持仓-持仓列表
 * Created by yx on 2016/9/16.
 */
public class FinancialPositionFragment extends MvpBussFragment<FinancialPositionPresenter> implements View.OnClickListener, FinancialPositionContract.FinancialPositionView {

    // ====================view定义=================start=========
    /**
     * 页面根视图
     */
    private View mRootView;
    //--------------------------------有数据时显示的区域-------
    /**
     * 内容显示区域布局
     */
    private ScrollView sl_content_view;
    //  ==============  <!--现金管理类产品-->=====
    /**
     * 现金管理类产品布局
     */
    private LinearLayout ll_content_view1;
    /**
     * 现金管理类产品ListView
     */
    private MeasureListView listview_cash_management_products;
    //  ==============  <!--净值开放类产品-->=====
    /**
     * 净值开放类产品布局
     */
    private LinearLayout ll_content_view2;
    /**
     * 净值开放类产品ListView
     */
    private MeasureListView listview_net_value_open_products;
    //  ==============  <!--固定期限类产品-->=====
    /**
     * 固定期限类产品布局
     */
    private LinearLayout ll_content_view3;
    /**
     * 固定期限类产品ListView
     */
    private MeasureListView listview_fixed_term_product;
    //--------------------------------无数据时显示的区域-------
    /**
     * 无数据内容显示区域布局
     */
    private RelativeLayout rl_content_view_nodata;
    /**
     * 空界面显示图片和文案，文案可以点击跳转
     */
    private CommonEmptyView view_no_data;
    /**
     * 空界面显示广告界面
     */
    private WealthManagemenAdviertisementView view_no_data_adviertisement;


    // ====================view定义=================end===========

    // ===================接口code===============start=============
    /**
     * PsnInvestmentManageIsOpen 判断是否开通投资服务
     */
    private final static int RESULT_CODE_PSNINVESTMENTMANAGEISOPEN = 0xff01;
    /**
     * I42-4.37 037查询客户理财账户信息 PsnXpadAccountQuery
     */
    private final static int RESULT_CODE_PSNXPADACCOUNTQUERY = 0xff02;
    /**
     * I42-4.36 036查询客户持仓信息 PsnXpadProductBalanceQuery
     */
    private final static int RESULT_CODE_PSNXPADPRODUCTBALANCEQUERY = 0xff03;
    // ===================接口code定义=================end===========

    // ===================变量义=================start===========


//    /**
//     * 请求接口
//     */
//    private FinancialPositionPresenter mFinancialPositionPresenter;

    /**
     * 会话id
     */
    private String mConversationId = "";
    /**
     * 4.36 036查询客户持仓信息PsnXpadProductBalanceQuery 响应model
     */
    private List<PsnXpadProductBalanceQueryResModel> mPsnXpadProductBalanceQueryResModel;
    /**
     * I42-4.40 040产品详情查询 PsnXpadProductDetailQuery-响应model
     */
    private PsnXpadProductDetailQueryResModel mPsnXpadProductDetailQueryResModel = null;
    /***
     * 现金管理类产品集合数据
     */
    private List<PsnXpadProductBalanceQueryResModel> cashManagementProductsList = new ArrayList<PsnXpadProductBalanceQueryResModel>();
    /**
     * 现金管理类产品-日积月累-集合数据
     */
    private List<PsnXpadProductBalanceQueryResModel> cashManagementProductsRJYLList = new ArrayList<PsnXpadProductBalanceQueryResModel>();
    /**
     * 现金管理类产品-收益累进-集合数据
     */
    private List<PsnXpadProductBalanceQueryResModel> cashManagementProductsSYLJList = new ArrayList<PsnXpadProductBalanceQueryResModel>();
    /***
     * 净值开放类产品集合数据
     */
    private List<PsnXpadProductBalanceQueryResModel> netValueOpenProductsList = new ArrayList<PsnXpadProductBalanceQueryResModel>();
    /***
     * 固定期限类产品集合数据
     */
    private List<PsnXpadProductBalanceQueryResModel> fixedTermProductList = new ArrayList<PsnXpadProductBalanceQueryResModel>();
    /***
     * 现金管理类产品Adapter
     */
    private FinancialPositionListAdapter mCashManagementProductsAdapter;
    /***
     * 净值开放类产品Adapter
     */
    private FinancialPositionListAdapter mNetValueOpenProductsAdapter;
    /***
     * 固定期限类产品Adapter
     */
    private FinancialPositionListAdapter mFixedTermProductAdapter;
    //------------跳转详情界面------
    //净值型详情
    private FinancialTypeNetValueDetaileFragment mTypeNetValueDetaileFragment = null;
    //------====日积月累和收益累计===---
    //日积月累型详情
    private FinancialTypeFixedTermDetailFragment mFinancialTypeFixedTermDetailFragment = null;
    //收益累计型详情
    private FinancialTypeEarnBuildDetailFragment mFinancialTypeEarnBuildDetailFragment = null;
    //业绩基准型详情
    private FinancialTypeOutStandDetaileFragment mFinancialTypeOutStandDetaileFragment = null;
    //非业绩基准型详情
    private FinancialTypeFixedOutPerformanceFragment mFinancialTypeFixedOutPerformanceFragment = null;

    /**
     * 详情跳转类型{日积月累详情，收益累计详情，净值型详情，业绩基准详情,非业绩基准详情，产品购买}
     */
    private enum FinancialDetailType {
        DETAIL_TYPE_ONE, DETAIL_TYPE_TWO, DETAIL_TYPE_THREE, DETAIL_TYPE_FOUR, DETAIL_TYPE_FIVE, DETAIL_TYPE_SIX
    }

    /**
     * 详情跳转类型-临时变量
     */
    private FinancialDetailType mFinancialDetailType = FinancialDetailType.DETAIL_TYPE_ONE;
    //item的model数据 -单条客户持仓信息
    private PsnXpadProductBalanceQueryResModel mPsnXpadProductBalanceQueryResItemModel = null;

    //头部 右侧 账户设置按钮 点击跳转类
    private AccountMainFragment mAccountMainFragment = null;
    //理财推荐日积月累，购买界面
    private PurchaseFragment mPurchaseFragment = null;
    //I42-4.37 037 查询客户理财账户信息
    private PsnXpadAccountQueryResModel mPsnXpadAccountQueryResModel = null;
    //I42-4.37 037 查询客户理财账户信息 -单条记录
    private PsnXpadAccountQueryResModel.XPadAccountEntity mItemXPadAccountEntity = null;
    //是否点击左上角返回按钮 返回到理财首页
    private boolean isBackToHome = false;
    // ====================变量定义=================end===========

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_financial_postiton_main, null);
        return mRootView;
    }

    /**
     * 标题栏左侧图标点击事件
     */
    @Override
    protected void titleLeftIconClick() {
        if (isBackToHome) {
            WealthProductFragment mWealthProductFragment = findFragment(WealthProductFragment.class);
            if(mWealthProductFragment!=null){
                popToAndReInit(WealthProductFragment.class);
            }else{
//                mWealthProductFragment = new WealthProductFragment();
//                startWithPop(mWealthProductFragment);
                mActivity.finish();
            }

        } else {
            super.titleLeftIconClick();
        }

    }

    /**
     * 标题栏右侧图标点击事件
     */
    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
    }

    /**
     * 是否显示右侧标题按钮
     */
    @Override
    protected boolean isDisplayRightIcon() {
        return true;
    }

    /**
     * 是否显示红头
     *
     * @return
     */
    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    /**
     * 设置标题
     */
    @Override
    protected String getTitleValue() {
        return getContext().getString(R.string.boc_position_my_position);
    }

    /**
     * 头部右侧标题设置
     */
    @Override
    protected View getTitleBarView() {
        TitleBarView titleBarView = (TitleBarView) super.getTitleBarView();
        //账户设置 点击事件
        titleBarView.setRightButton(getResources().getString(R.string.boc_position_account_set), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAccountMainFragment == null) {//账户设置
                    mAccountMainFragment = new AccountMainFragment(FinancialPositionFragment.class);
                }
                start(mAccountMainFragment);
            }
        });
        return titleBarView;
    }

    /**
     * 初始化view 之前
     */
    @Override
    public void beforeInitView() {
    }


    /**
     * 初始化view
     */
    @Override
    public void initView() {
        sl_content_view = (ScrollView) mRootView.findViewById(R.id.sl_content_view);
        ll_content_view1 = (LinearLayout) mRootView.findViewById(R.id.ll_content_view1);
        listview_cash_management_products = (MeasureListView) mRootView.findViewById(R.id.listview_cash_management_products);
        ll_content_view2 = (LinearLayout) mRootView.findViewById(R.id.ll_content_view2);
        listview_net_value_open_products = (MeasureListView) mRootView.findViewById(R.id.listview_net_value_open_products);
        ll_content_view3 = (LinearLayout) mRootView.findViewById(R.id.ll_content_view3);
        listview_fixed_term_product = (MeasureListView) mRootView.findViewById(R.id.listview_fixed_term_product);
        rl_content_view_nodata = (RelativeLayout) mRootView.findViewById(R.id.rl_content_view_nodata);
        view_no_data = (CommonEmptyView) mRootView.findViewById(R.id.view_no_data);
        view_no_data_adviertisement = (WealthManagemenAdviertisementView) mRootView.findViewById(R.id.view_no_data_adviertisement);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
//        mFinancialPositionPresenter = new FinancialPositionPresenter(this);
        mCashManagementProductsAdapter = new FinancialPositionListAdapter(getActivity());
        mNetValueOpenProductsAdapter = new FinancialPositionListAdapter(getActivity());
        mFixedTermProductAdapter = new FinancialPositionListAdapter(getActivity());
        view_no_data.setEmptyTips(R.drawable.no_result, 1, "您当前暂无持仓，", "点我去看看吧", "！");
        showLoadingDialog(false);
        getPresenter().getPSNCreatConversation();


    }

    /**
     * 初始化监听
     */
    @Override
    public void setListener() {
        view_no_data.setTextOnclickListener(new CommonEmptyView.TextOnclickListener() {//点我去看看点击事件
            @Override
            public void textOnclickListener() {
                popToAndReInit(WealthProductFragment.class);
            }
        });
        view_no_data_adviertisement.setTextOnclickListener(new WealthManagemenAdviertisementView.AdviertisementViewOnclickListener() {//日积月累推荐按钮
            @Override
            public void adviertisementViewOnclickListener() {
                showLoadingDialog(false);
                mFinancialDetailType = FinancialDetailType.DETAIL_TYPE_SIX;
                getPresenter().getPsnXpadProductDetailQuery("AMRJYL01", "", "0");//跳转产品购买界面请求接口
            }
        });
        //日积月累和收益累计 列表点击
        // {日积月累单独详情界面，收益累计也是单独详情界面【固定期限的非业绩基准也用收益累计的详情界面】】）
        listview_cash_management_products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.d("yx---现金管理-->" + cashManagementProductsList.get(position).getProgressionflag());
                //  是否收益累计产品	String	0：否 ,1：是
                if ("0".equalsIgnoreCase(cashManagementProductsList.get(position).getProgressionflag())) {
                    mFinancialDetailType = FinancialDetailType.DETAIL_TYPE_ONE;
                    //日积月累
                    mPsnXpadProductBalanceQueryResItemModel = cashManagementProductsList.get(position);
                } else if ("1".equalsIgnoreCase(cashManagementProductsList.get(position).getProgressionflag())) {
                    mFinancialDetailType = FinancialDetailType.DETAIL_TYPE_TWO;
                    //收益累计
                    mPsnXpadProductBalanceQueryResItemModel = cashManagementProductsList.get(position);
                }
                //获取37接口匹配数据，为详情页继续购买做准备
                mItemXPadAccountEntity = FinancialPositionCodeModeUtil.buildXPadAccountEntity(cashManagementProductsList.get(position), mPsnXpadAccountQueryResModel);
                getPsnXpadProductDetailQuery(mPsnXpadProductBalanceQueryResItemModel);
            }
        });
        //净值列表点击
        listview_net_value_open_products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.d("yx---净值-->" + netValueOpenProductsList.get(position).getIssueType());
                mFinancialDetailType = FinancialDetailType.DETAIL_TYPE_THREE;
                mPsnXpadProductBalanceQueryResItemModel = netValueOpenProductsList.get(position);
                //获取37接口匹配数据，为详情页继续购买做准备
                mItemXPadAccountEntity = FinancialPositionCodeModeUtil.buildXPadAccountEntity(netValueOpenProductsList.get(position), mPsnXpadAccountQueryResModel);
                //净值
                getPsnXpadProductDetailQuery(mPsnXpadProductBalanceQueryResItemModel);

            }
        });

        //业绩基准列表点击{包括非业绩基准，点击跳转到 收益累计界面}
        listview_fixed_term_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.d("yx---固定期限-->" + fixedTermProductList.get(position).getStandardPro());
                //0：非业绩基准产品
                //1：业绩基准-锁定期转低收益
                // 2：业绩基准-锁定期后入账
                // 3：业绩基准-锁定期周期滚续
                // 业绩基准产品允许查看份额明细
                if ("0".equalsIgnoreCase(fixedTermProductList.get(position).getStandardPro())) {
                    mFinancialDetailType = FinancialDetailType.DETAIL_TYPE_FIVE;
                    mPsnXpadProductBalanceQueryResItemModel = fixedTermProductList.get(position);
                    //非业绩基准
                } else {
                    mFinancialDetailType = FinancialDetailType.DETAIL_TYPE_FOUR;
                    mPsnXpadProductBalanceQueryResItemModel = fixedTermProductList.get(position);
                    //业绩基准
                }
                //获取37接口匹配数据，为详情页继续购买做准备
                mItemXPadAccountEntity = FinancialPositionCodeModeUtil.buildXPadAccountEntity(fixedTermProductList.get(position), mPsnXpadAccountQueryResModel);
                getPsnXpadProductDetailQuery(mPsnXpadProductBalanceQueryResItemModel);

            }
        });
    }

    /**
     * 监听view处理方法
     */
    @Override
    public void onClick(View v) {

    }

    /**
     * 重新进入界面调用次方法
     */
    @Override
    public void reInit() {
        super.reInit();
//        if (sl_content_view != null) {
//            sl_content_view.scrollTo(10, 10);
//        }
//        showLoadingDialog(false);
//        getPresenter().getPSNCreatConversation();
        initData();
    }

    @Override
    protected FinancialPositionPresenter initPresenter() {
        return new FinancialPositionPresenter(this);
    }

    /**
     * @param conversationId
     */
    @Override
    public void obtainConversationSuccess(String conversationId) {
        mConversationId = conversationId;
        getPresenter().getPsnXpadProductBalanceQuery();
    }

    @Override
    public void obtainConversationFail() {
        closeProgressDialog();
    }

    /**
     * I42-4.36 036查询客户持仓信息 PsnXpadProductBalanceQuery -成功回调
     */
    @Override
    public void obtainPsnXpadProductBalanceSuccess(List<PsnXpadProductBalanceQueryResModel> mViewModel) {
        handlePsnXpadProductBalanceQuery(mViewModel);
        getPresenter().getPsnXpadAccountQuery("1", "1");
    }

    /**
     * I42-4.36 036查询客户持仓信息PsnXpadProductBalanceQuery-失败回调
     */
    @Override
    public void obtainPsnXpadProductBalanceFail() {
        closeProgressDialog();
    }


    /**
     * I42-4.40 040产品详情查询 PsnXpadProductDetailQuery，成功调用
     */
    @Override
    public void obtainPsnXpadProductDetailQuerySuccess(PsnXpadProductDetailQueryResModel mPsnXpadProductDetailQueryResModel) {
        closeProgressDialog();
        handlePsnXpadProductDetailQueryResult(mPsnXpadProductDetailQueryResModel);
    }

    /**
     * I42-4.40 040产品详情查询PsnXpadProductDetailQuery，失败调用
     */
    @Override
    public void obtainPsnXpadProductDetailQueryFail() {
        closeProgressDialog();
    }


    /**
     * I42-4.37 037查询客户理财账户信息 PsnXpadAccountQuery
     *
     * @param mPsnXpadAccountQueryResModel
     */
    @Override
    public void obtainPsnXpadAccountQuerySuccess(PsnXpadAccountQueryResModel mPsnXpadAccountQueryResModel) {
        closeProgressDialog();
        this.mPsnXpadAccountQueryResModel = mPsnXpadAccountQueryResModel;
    }

    /**
     * I42-4.37 037查询客户理财账户信息 PsnXpadAccountQuery
     */
    @Override
    public void obtainPsnXpadAccountQueryFail() {
        closeProgressDialog();
    }

    @Override
    public void setPresenter(BasePresenter presenter) {

    }
    //=================================接口回调处理段落================start==================

    /**
     * I42-4.36 036查询客户持仓信息 PsnXpadProductBalanceQuery -响应处理
     *
     * @param mViewModel
     */
    private void handlePsnXpadProductBalanceQuery(List<PsnXpadProductBalanceQueryResModel> mViewModel) {
        this.mPsnXpadProductBalanceQueryResModel = mViewModel;
        if (PublicUtils.isEmpty(mPsnXpadProductBalanceQueryResModel)) {
            sl_content_view.setVisibility(View.GONE);
            rl_content_view_nodata.setVisibility(View.VISIBLE);
        } else {
            sl_content_view.setVisibility(View.VISIBLE);
            rl_content_view_nodata.setVisibility(View.GONE);
            if (!PublicUtils.isEmpty(cashManagementProductsList)) {//1：现金管理类产品
                cashManagementProductsList.clear();
                if(!PublicUtils.isEmpty(cashManagementProductsRJYLList)){
                    cashManagementProductsRJYLList.clear();
                }
                if(!PublicUtils.isEmpty(cashManagementProductsSYLJList)){
                    cashManagementProductsSYLJList.clear();
                }
            }
            if (!PublicUtils.isEmpty(netValueOpenProductsList)) { //2：净值开放类产品
                netValueOpenProductsList.clear();
            }
            if (!PublicUtils.isEmpty(fixedTermProductList)) {//3：固定期限产品
                fixedTermProductList.clear();
            }
            //三大类型进行区分
            for (int i = 0; i < mPsnXpadProductBalanceQueryResModel.size(); i++) {
                if ("1".equalsIgnoreCase(mPsnXpadProductBalanceQueryResModel.get(i).getIssueType())) {//1：现金管理类产品
                    //progressionflag 是否收益累计产品	0：否;1：是
                    if ("0".equalsIgnoreCase(mPsnXpadProductBalanceQueryResModel.get(i).getProgressionflag())) {
                        cashManagementProductsRJYLList.add(mPsnXpadProductBalanceQueryResModel.get(i));
                    } else if ("1".equalsIgnoreCase(mPsnXpadProductBalanceQueryResModel.get(i).getProgressionflag())) {
                        cashManagementProductsSYLJList.add(mPsnXpadProductBalanceQueryResModel.get(i));
                    }
                } else if ("2".equalsIgnoreCase(mPsnXpadProductBalanceQueryResModel.get(i).getIssueType())) { //2：净值开放类产品
                    netValueOpenProductsList.add(mPsnXpadProductBalanceQueryResModel.get(i));
                } else if ("3".equalsIgnoreCase(mPsnXpadProductBalanceQueryResModel.get(i).getIssueType())) {//3：固定期限产品
                    fixedTermProductList.add(mPsnXpadProductBalanceQueryResModel.get(i));
                }
            }
            if (!PublicUtils.isEmpty(cashManagementProductsList)) {
                cashManagementProductsList.clear();
            }
            cashManagementProductsList.addAll(cashManagementProductsRJYLList);
            cashManagementProductsList.addAll(cashManagementProductsSYLJList);
            if (!PublicUtils.isEmpty(cashManagementProductsList)) {
                listview_cash_management_products.setAdapter(mCashManagementProductsAdapter);
                mCashManagementProductsAdapter.setDatas(cashManagementProductsList);
                ll_content_view1.setVisibility(View.VISIBLE);
            } else {
                ll_content_view1.setVisibility(View.GONE);
            }
            if (!PublicUtils.isEmpty(netValueOpenProductsList)) {
                listview_net_value_open_products.setAdapter(mNetValueOpenProductsAdapter);
                mNetValueOpenProductsAdapter.setDatas(netValueOpenProductsList);
                ll_content_view2.setVisibility(View.VISIBLE);

            } else {
                ll_content_view2.setVisibility(View.GONE);
            }
            if (!PublicUtils.isEmpty(fixedTermProductList)) {
                listview_fixed_term_product.setAdapter(mFixedTermProductAdapter);
                mFixedTermProductAdapter.setDatas(fixedTermProductList);
                ll_content_view3.setVisibility(View.VISIBLE);
            } else {
                ll_content_view3.setVisibility(View.GONE);
            }

        }
    }

    /**
     * I42-4.40 040产品详情查询 PsnXpadProductDetailQuery
     *
     * @param mResModel
     */
    private void handlePsnXpadProductDetailQueryResult(PsnXpadProductDetailQueryResModel mResModel) {
        closeProgressDialog();
        if (mResModel != null) {
            this.mPsnXpadProductDetailQueryResModel = mResModel;
            if (mFinancialDetailType == FinancialDetailType.DETAIL_TYPE_ONE) {
                //日积月累
                LogUtils.d("yx------------日积月累-->");
                mFinancialTypeFixedTermDetailFragment = new FinancialTypeFixedTermDetailFragment();
                mFinancialTypeFixedTermDetailFragment.setFixedTermDetailDeta(mPsnXpadProductBalanceQueryResItemModel, mResModel, mConversationId, mItemXPadAccountEntity);
                start(mFinancialTypeFixedTermDetailFragment);
            } else if (mFinancialDetailType == FinancialDetailType.DETAIL_TYPE_TWO) {
                LogUtils.d("yx------------收益累计-->");
                //收益累计
                mFinancialTypeEarnBuildDetailFragment = new FinancialTypeEarnBuildDetailFragment();
                mFinancialTypeEarnBuildDetailFragment.setFixedEarnbuildDeta(mConversationId, mPsnXpadProductBalanceQueryResItemModel, mResModel, mItemXPadAccountEntity);
                start(mFinancialTypeEarnBuildDetailFragment);
            } else if (mFinancialDetailType == mFinancialDetailType.DETAIL_TYPE_THREE) {
                //净值型详情
                LogUtils.d("yx------------净值型详情-->");
//                if (mTypeNetValueDetaileFragment == null) {
                mTypeNetValueDetaileFragment = new FinancialTypeNetValueDetaileFragment();
//                }
                mTypeNetValueDetaileFragment.setNetValueDetaileDeta(mPsnXpadProductBalanceQueryResItemModel, mResModel, mConversationId, mItemXPadAccountEntity);
                start(mTypeNetValueDetaileFragment);
            } else if (mFinancialDetailType == FinancialDetailType.DETAIL_TYPE_FOUR) {
                LogUtils.d("yx------------业绩基准-->");
                //业绩基准
//                if (mFinancialTypeOutStandDetaileFragment== null) {
                mFinancialTypeOutStandDetaileFragment = new FinancialTypeOutStandDetaileFragment();
//                }
                mFinancialTypeOutStandDetaileFragment.setOutStandDetailDeta(mPsnXpadProductBalanceQueryResItemModel, mResModel, mConversationId, mItemXPadAccountEntity,mPsnXpadAccountQueryResModel);
                start(mFinancialTypeOutStandDetaileFragment);
            } else if (mFinancialDetailType == FinancialDetailType.DETAIL_TYPE_FIVE) {
                LogUtils.d("yx------------非业绩基准--->");
                //非业绩基准
//                if (mFinancialTypeFixedOutPerformanceFragment == null) {
                mFinancialTypeFixedOutPerformanceFragment = new FinancialTypeFixedOutPerformanceFragment();
//                }
                mFinancialTypeFixedOutPerformanceFragment.setFixedTermDetailDeta(mPsnXpadProductBalanceQueryResItemModel, mResModel, mConversationId, mItemXPadAccountEntity);
                start(mFinancialTypeFixedOutPerformanceFragment);
            } else if (mFinancialDetailType == FinancialDetailType.DETAIL_TYPE_SIX) {
                LogUtils.d("yx------------产品购买-->");
                //产品购买
                if (mPurchaseFragment == null) {
                    mPurchaseFragment = PurchaseFragment.newInstance(FinancialPositionCodeModeUtil.buildPurchaseInputModeParams(mPsnXpadProductDetailQueryResModel, null), ModelUtil.generateWealthBeans(mPsnXpadAccountQueryResModel.getList()));
                }
                start(mPurchaseFragment);
            }
        }

    }

    //=================================接口回调处理段落================end==================

    //=================================自定义方法段落================start==================

    /**
     * 请求4.40 040产品详情查询PsnXpadProductDetailQuery
     *
     * @param mItemModel 产品详情
     */
    private void getPsnXpadProductDetailQuery(PsnXpadProductBalanceQueryResModel mItemModel) {
        showLoadingDialog();
        getPresenter().getPsnXpadProductDetailQuery(mItemModel.getProdCode() + "", "", mItemModel.getProductKind() + "");

    }

    /**
     * 设置点击左上角返回按钮是否返回到理财首页
     *
     * @param isBackToHome
     */
    public void setBackToHome(boolean isBackToHome) {
        this.isBackToHome = isBackToHome;
    }
    //=================================自定义方法段落================end==================
    //=================================自定义公共方法段落================start==================

    /**
     * String 转换成 int 类型
     *
     * @param number
     * @return
     */
    private int strToInt(String number) {
        if (!"".equals(number) && number != null) {
            return Integer.valueOf(number);
        }
        return 0;
    }

    /**
     * 通过资源id 查找 资源文件内容
     *
     * @param mResId 资源id
     * @return
     */
    private String getStr(int mResId) {
        return getContext().getString(mResId);
    }


    //=================================自定义公共方法段落================end==================
}

