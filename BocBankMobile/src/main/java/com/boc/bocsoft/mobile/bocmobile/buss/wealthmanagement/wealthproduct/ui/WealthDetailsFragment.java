package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.pdf.PDFFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.buyprocedure.BuyProcedureWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ErrorDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.shareinfoview.ShareUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.trendview.BocTrendView;
import com.boc.bocsoft.mobile.bocmobile.buss.common.LoginCallback;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.model.WealthAccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.ui.FinancialPsnXpadExpectYieldQueryFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.ui.FinancialTypeProgressQueryFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.ui.InvestTreatyFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.ui.OpenStatusI;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.ui.PortfolioPurchaseFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.InvestTreatyBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolIntelligentDetailsBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.PsnXpadSignInitBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui.FixedtimeFixedAmountInvestFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui.ProtocolBalanceInvestFragment1;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui.ProtocolPeriodContinueFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui.ProtocolSelectFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui.ProtocolSmartFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.ui.PurchaseFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.DetailsRequestBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthDetailsBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthHistoryBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.presenter.WealthPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.data.WealthBundleData;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.data.WealthViewData;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.utils.ResultConvertUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.utils.WealthPublicUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.widget.ClickDownShowView;
import com.boc.bocsoft.mobile.common.utils.ButtonClickLock;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.tencent.mm.sdk.openapi.SendMessageToWX;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * 理财产品明细
 * Created by liuweidong on 2016/9/12.
 */
public class WealthDetailsFragment extends MvpBussFragment<WealthPresenter> implements WealthContract.DetailsView, View.OnClickListener {
    private static final String TAG = "WealthDetailsFragment";

    public static final String DETAILS_INFO = "details_info";// 明细页数据（列表页使用）
    public static final String OTHER = "other";// true：其他页面跳转到明细 false：列表页
    public static final String DETAILS_REQUEST = "details_request";// 明细需要请求数据（其它页面跳转）
    public static final String DETAIL_INFO_BY_RESULT = "detail_info_by_result";// 购买结果页猜你喜欢跳转（列表）
    public static final String FILE_NAME = "collect";// 存储关注的数据
    public static final String PROD_CODE = "prodcode";// 产品代码
    /*对应模块标识*/
    public static final String BUY = "000";// 购买
    public static final String BUYGROUP = "001";// 组合购买
    public static final String PROTOCOL = "002";// 投资协议申请
    public static final String PROFIT = "003";// 收益试算
    private View rootView;
    private RelativeLayout rlRootView;
    private ScrollView scrollView;
    private TextView txtTitle;// 标题
    private ImageView imgRiskLevel;// 风险级别
    private ImageView imgRiskType;// 风险类型
    private CheckBox checkBox;// 收藏
    private TextView txtType;// 产品类型
    private TextView txtTypeValue;
    private LinearLayout llRight;
    private TextView txtNameL;
    private TextView txtValueL;
    private LinearLayout llParentC;
    private TextView txtNameC;
    private TextView txtValueC;
    private TextView txtNameR;
    private TextView txtValueR;
    private LinearLayout llParentProgress;
    private BuyProcedureWidget buyProcedureWidget;
    private BocTrendView bocTrendView;// 历史净值
    private LinearLayout llParentGroup;
    private TextView txtBuyGroup;// 组合购买
    private TextView txtRequest;// 投资协议申请
    private ClickDownShowView clickView1;// 产品属性
    private ClickDownShowView clickView2;// 购买属性
    private ClickDownShowView clickView3;// 赎回属性
    private TextView txtSuggest, txtHelp;// 说明书|帮助
    private LinearLayout llBottom;
    private ViewGroup ll_history_price;//历史净值布局
    private RadioGroup rg_time_zone;//选择时间区间
    private LinearLayout llProfit;// 收益试算
    private LinearLayout llParentBuy;
    private TextView txtBottom;
    private TextView txtBuyMessage;

    private static WealthDetailsFragment instance;
    private WealthDetailsBean detailsBean;// 明细接口响应数据
    private DetailsRequestBean requestBean;// 明细页传递的数据
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ProtocolModel mProtocolModel;// 投资协议申请数据
    private String clickOprLock = "click_more";// 防重锁
    private ProtocolModel viewModel;

    public static WealthDetailsFragment newInstance(WealthListBean wealthListBean) {
        WealthDetailsFragment fragment = new WealthDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DETAIL_INFO_BY_RESULT, wealthListBean);// 理财列表
        bundle.putBoolean(OTHER, true);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.boc_fragment_wealth_product_details, null);
        return rootView;
    }

    @Override
    public void initView() {
        rlRootView = (RelativeLayout) rootView.findViewById(R.id.root_view);
        scrollView = (ScrollView) rootView.findViewById(R.id.scrollview);
        txtTitle = (TextView) rootView.findViewById(R.id.txt_title);
        imgRiskLevel = (ImageView) rootView.findViewById(R.id.img_risk_level);
        imgRiskType = (ImageView) rootView.findViewById(R.id.img_risk_type);
        checkBox = (CheckBox) rootView.findViewById(R.id.checkbox);
        txtType = (TextView) rootView.findViewById(R.id.txt_type);
        txtTypeValue = (TextView) rootView.findViewById(R.id.txt_type_value);
        llRight = (LinearLayout) rootView.findViewById(R.id.ll_right);
        txtNameL = (TextView) rootView.findViewById(R.id.txt_name_left);
        txtValueL = (TextView) rootView.findViewById(R.id.txt_value_left);
        llParentC = (LinearLayout) rootView.findViewById(R.id.ll_parent_c);
        txtNameC = (TextView) rootView.findViewById(R.id.txt_name_center);
        txtValueC = (TextView) rootView.findViewById(R.id.txt_value_center);
        txtNameR = (TextView) rootView.findViewById(R.id.txt_name_right);
        txtValueR = (TextView) rootView.findViewById(R.id.txt_value_right);
        llParentProgress = (LinearLayout) rootView.findViewById(R.id.ll_parent_progress);
        buyProcedureWidget = (BuyProcedureWidget) rootView.findViewById(R.id.product_progress);
        bocTrendView = (BocTrendView) rootView.findViewById(R.id.history_price);
        llParentGroup = (LinearLayout) rootView.findViewById(R.id.ll_parent_buy_group);
        txtBuyGroup = (TextView) rootView.findViewById(R.id.txt_buy_group);
        txtRequest = (TextView) rootView.findViewById(R.id.txt_request);
        clickView1 = (ClickDownShowView) rootView.findViewById(R.id.click_view_1);
        clickView2 = (ClickDownShowView) rootView.findViewById(R.id.click_view_2);
        clickView3 = (ClickDownShowView) rootView.findViewById(R.id.click_view_3);
        txtSuggest = (TextView) rootView.findViewById(R.id.txt_suggest);
        txtHelp = (TextView) rootView.findViewById(R.id.txt_help);
        llBottom = (LinearLayout) rootView.findViewById(R.id.ll_bottom);
        llProfit = (LinearLayout) rootView.findViewById(R.id.ll_profit);
        llParentBuy = (LinearLayout) rootView.findViewById(R.id.ll_parent_buy);
        txtBottom = (TextView) rootView.findViewById(R.id.txt_bottom);
        txtBuyMessage = (TextView) rootView.findViewById(R.id.txt_buy_message);
        ll_history_price = (ViewGroup) rootView.findViewById(R.id.ll_history_price);
        rg_time_zone = (RadioGroup) rootView.findViewById(R.id.rg_time_zone);
    }

    @Override
    public void initData() {
        instance = this;
        preferences = mContext.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
        editor = preferences.edit();
        setBocTrendViewAttribute();

        setTitleRightView();// 设置标题右边view
        if (getArguments().getBoolean(OTHER, false)) {// 其他页面跳转明细
            scrollView.setVisibility(View.GONE);
            llBottom.setVisibility(View.GONE);
            llParentGroup.setVisibility(View.GONE);// 隐藏组合购买与投资协议申请
            llProfit.setVisibility(View.GONE);// 隐藏收益试算
            if (getArguments().getParcelable(DETAILS_REQUEST) != null) {// 其他页面跳转
                /*没有理财账户*/
                requestBean = getArguments().getParcelable(DETAILS_REQUEST);
                requestBean.setIsBuy(WealthConst.YES_1);// 设置可购买
            }
            if (getArguments().getSerializable(DETAIL_INFO_BY_RESULT) != null) {// 购买猜你喜欢跳转
                getBuyLikeBundleData();
            }
            showLoadingDialog(false);
            if (ApplicationContext.getInstance().isLogin()) {
                getPresenter().queryOpenStatus(new OpenStatusI() {
                    @Override
                    public void openSuccess() {
                        boolean[] openStatus = WealthProductFragment.getInstance().isOpenWealth();
                        if (openStatus[0] && openStatus[2]) {
                            requestBean.setIbknum(WealthProductFragment.getInstance().getViewModel().getAccountList().get(0).getIbkNumber());
                            getPresenter().queryProductDetailY(requestBean);
                        } else {
                            getPresenter().queryProductDetailN(requestBean);
                        }
                    }

                    @Override
                    public void openFail(ErrorDialog dialog) {
                        getPresenter().queryProductDetailN(requestBean);
                    }
                });
            } else {
                getPresenter().queryProductDetailN(requestBean);
            }
        } else {// 列表页面跳转
            detailsBean = getArguments().getParcelable(DETAILS_INFO);
            requestBean = getArguments().getParcelable(DETAILS_REQUEST);
            isShowView();
            setProductData();
            /*查询历史净值*/
            if (WealthConst.PRODUCT_TYPE_2.equals(detailsBean.getProductType())) {
                if (detailsBean.isLoginBeforeI()) {// 登录前
//                    getPresenter().queryNetHistoryN(detailsBean.getProdCode(), "0");
                } else {// 登录后
                    showLoadingDialog(false);
                    getPresenter().queryNetHistoryY(detailsBean.getProdCode(), "0");
                }
            }
        }
    }

    @Override
    public void setListener() {
        txtBuyGroup.setOnClickListener(this);
        txtRequest.setOnClickListener(this);
        txtSuggest.setOnClickListener(this);
        txtHelp.setOnClickListener(this);
        llParentBuy.setOnClickListener(this);// 购买
        llProfit.setOnClickListener(this);// 收益试算
        txtTypeValue.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {// 收藏
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putString(PROD_CODE + detailsBean.getProdCode(), detailsBean.getProdCode()).commit();
                } else {
                    editor.remove(PROD_CODE + detailsBean.getProdCode()).commit();
                }
            }
        });
        rg_time_zone.setOnCheckedChangeListener(timeZoneListener);
    }

    @Override
    protected String getTitleValue() {
        return "理财详情";
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected WealthPresenter initPresenter() {
        return new WealthPresenter(this, true);
    }

    /**
     * 设置折线图属性
     */
    private void setBocTrendViewAttribute() {
        bocTrendView.getParamBuilder().touchIndicatorEnable(true);// 设置折线图是否可触摸
        bocTrendView.getParamBuilder().dateFormat(DateFormatters.DATE_FORMAT_V2_1);
        bocTrendView.getParamBuilder().bottomDateFormat("yyyy\nMM/dd");
        bocTrendView.getParamBuilder().selectDateFormat(DateFormatters.DATE_FORMAT_V2_1);
        bocTrendView.getParamBuilder().selectPriceTips("单位净值：");
        bocTrendView.getParamBuilder().selectDateTips("净值日期：");
        bocTrendView.getParamBuilder().leftPriceScale(4);
    }

    /**
     * 当前实例
     *
     * @return
     */
    public static WealthDetailsFragment getInstance() {
        if (instance == null)
            instance = new WealthDetailsFragment();
        return instance;
    }

    /**
     * 选择时间区间监听
     */
    private RadioGroup.OnCheckedChangeListener timeZoneListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            showLoadingDialog();
            if (checkedId == R.id.rb_three_month) {// 近三月
                if (detailsBean.isLoginBeforeI()) {// 查询历史净值
                    getPresenter().queryNetHistoryN(detailsBean.getProdCode(), "0");// 登录前
                } else {
                    getPresenter().queryNetHistoryY(detailsBean.getProdCode(), "0");// 登录后
                }
            } else if (checkedId == R.id.rb_one_year) {// 近一年
                if (detailsBean.isLoginBeforeI()) {// 查询历史净值
                    getPresenter().queryNetHistoryN(detailsBean.getProdCode(), "1");// 登录前
                } else {
                    getPresenter().queryNetHistoryY(detailsBean.getProdCode(), "1");// 登录后
                }
            }
        }
    };

    /**
     * 获取购买猜你喜欢的数据
     */
    private void getBuyLikeBundleData() {
        /*没有理财账户*/
        llParentGroup.setVisibility(View.VISIBLE);
        llProfit.setVisibility(View.VISIBLE);
        /*理财列表数据*/
        WealthListBean wealthListBean = (WealthListBean) getArguments().getSerializable(DETAIL_INFO_BY_RESULT);
        requestBean = new DetailsRequestBean();
        requestBean.setIsBuy(wealthListBean.getIsBuy());// 是否购买
        requestBean.setGroupBuy(wealthListBean.getImpawnPermit());// 是否组合购买
        requestBean.setIsAgreement(wealthListBean.getIsAgreement());// 是否投资协议申请
        requestBean.setIsProfitTest(wealthListBean.getIsProfiTest());// 是否收益试算
        requestBean.setProdCode(wealthListBean.getProdCode());// 产品代码
        requestBean.setProdKind(wealthListBean.getProductKind());// 产品性质
    }

    /**
     * 设置标题右边View
     */
    private void setTitleRightView() {
        LinearLayout linearLayout = mTitleBarView.getRightContainer();
        linearLayout.removeAllViews();
        View view = View.inflate(mContext, R.layout.boc_fragment_title_right, linearLayout);
        ImageView imgL = (ImageView) view.findViewById(R.id.img_left);
        ImageView imgR = (ImageView) view.findViewById(R.id.img_right);
        imgL.setImageResource(R.drawable.icon_share_black);
        imgR.setImageResource(R.drawable.icon_qrcode_black);
        imgL.setOnClickListener(new View.OnClickListener() {// 分享
            @Override
            public void onClick(View v) {
                if (detailsBean == null) {
                    return;
                }
                if (!ButtonClickLock.isCanClick(clickOprLock)) {// 防止暴力点击
                    return;
                }

                String url = WealthConst.getShareProductUrl(detailsBean.getProdCode(), detailsBean.getProductKind());
                String title = detailsBean.getProdName();
                String type = "";
                String[] values = new String[3];
                String date = "";
                date = ResultConvertUtils.convertDate(detailsBean.getProductKind(), detailsBean.getProdTimeLimit(), detailsBean.getIsLockPeriod(), detailsBean.getProductTermType());

                if (WealthConst.PRODUCT_TYPE_2.equals(detailsBean.getProductType())) {// 净值
                    type = "1";
                    values[0] = MoneyUtils.getRoundNumber(detailsBean.getPrice(), 4, BigDecimal.ROUND_HALF_UP);
                    values[1] = date;
                    values[2] = detailsBean.getSubAmount();
                } else {// 非净值
                    type = "0";
                    values[0] = ResultConvertUtils.convertRate(detailsBean.getYearlyRR(), detailsBean.getRateDetail());
                    values[1] = date;
                    values[2] = detailsBean.getSubAmount();
                }
                String content = WealthPublicUtils.buildShareStr(type, values, detailsBean.getCurCode());
                SendMessageToWX.Req req = ShareUtils.shareWebPage(0, url, title, content);
                if (getApi() != null)
                    getApi().sendReq(req);//跳转到朋友圈或会话列表
            }
        });
        imgR.setOnClickListener(new View.OnClickListener() {// 二维码
            @Override
            public void onClick(View v) {
                if (detailsBean == null) {
                    return;
                }
                if (!ButtonClickLock.isCanClick(clickOprLock)) {// 防止暴力点击
                    return;
                }
                WealthQrCodeFragment fragment = new WealthQrCodeFragment();
                Bundle bundle = new Bundle();
                bundle.putString(WealthQrCodeFragment.WEALTH_QRCODE_KEY_CODE, detailsBean.getProdCode());
                bundle.putString(WealthQrCodeFragment.WEALTH_QRCODE_KEY_KIND, detailsBean.getProductKind());
                fragment.setArguments(bundle);
                start(fragment);
            }
        });
    }

    /**
     * 判断页面布局的显示
     */
    private void isShowView() {
        /*组合购买、投资协议申请、购买是否显示*/
        if (WealthConst.NO_0.equals(requestBean.getIsBuy())) {// 购买（否）
            if (WealthConst.YES_1.equals(requestBean.getIsAgreement())) {// 协议申请（是）
                llBottom.setVisibility(View.VISIBLE);
                llParentGroup.setVisibility(View.GONE);
                txtBuyMessage.setVisibility(View.GONE);
                txtBottom.setText(getString(R.string.boc_wealth_protocol));
            } else {
                llBottom.setVisibility(View.GONE);
                llParentGroup.setVisibility(View.GONE);
            }
        } else if (WealthConst.YES_1.equals(requestBean.getIsBuy())) {// 购买（是）
            llBottom.setVisibility(View.VISIBLE);
            txtBuyMessage.setVisibility(View.VISIBLE);
            txtBottom.setText(getString(R.string.boc_wealth_buy));
            txtBuyMessage.setText(getString(R.string.boc_wealth_buy_message, MoneyUtils.getLoanAmountShownRMB1(detailsBean.getSubAmount(), detailsBean.getCurCode())));
            /*是否可组合购买与协议申请*/
            if (WealthConst.NO_1.equals(requestBean.getGroupBuy()) &&
                    WealthConst.NO_0.equals(requestBean.getIsAgreement())) {// 组合购买（否）协议申请（否）
                llParentGroup.setVisibility(View.GONE);
            } else if (WealthConst.YES_0.equals(requestBean.getGroupBuy()) &&
                    WealthConst.NO_0.equals(requestBean.getIsAgreement())) {// 组合购买（是）协议申请（否）
                txtRequest.setVisibility(View.GONE);
            } else if (WealthConst.NO_1.equals(requestBean.getGroupBuy()) &&
                    WealthConst.YES_1.equals(requestBean.getIsAgreement())) {// 组合购买（否）协议申请（是）
                txtBuyGroup.setVisibility(View.GONE);
            }
        }
        if (WealthConst.NO_0.equals(requestBean.getIsProfitTest())) {// 是否可收益试算
            llProfit.setVisibility(View.GONE);
        }
    }

    /**
     * 设置产品进度
     */
    private void setProductPeriod() {
        if ("0".equals(detailsBean.getProductKind())) {
            if (!WealthConst.IS_LOCK_PERIOD_0.equals(detailsBean.getIsLockPeriod())) {// 业绩基准去掉进度条
                llParentProgress.setVisibility(View.GONE);
                return;
            }
        }
        String[] textStr = new String[3];
        String[] dateStr = new String[3];
        textStr[0] = getString(R.string.boc_wealth_buy_date);
        dateStr[0] = detailsBean.getSellingStartingDate();
        if (WealthConst.PRODUCT_TYPE_3.equals(detailsBean.getProductType())) {// 固定期限产品
            textStr[1] = getString(R.string.boc_wealth_interest_date);
            dateStr[1] = detailsBean.getProdBegin();
        } else {
            textStr[1] = getString(R.string.boc_wealth_interest);
            dateStr[1] = getString(R.string.boc_wealth_interest_rule);
            buyProcedureWidget.setMiddCanClick(true);
            buyProcedureWidget.setMiddHint(ResultConvertUtils.convertCouponpayFreq(detailsBean.getCouponpayFreq(), detailsBean.getInterestDate()));
        }
        if (WealthConst.TERM_TYPE_0.equals(detailsBean.getProductTermType())) {// 有限期封闭式
            textStr[2] = getString(R.string.boc_wealth_buy_end);
            dateStr[2] = detailsBean.getProdEnd();
        } else if (WealthConst.TERM_TYPE_1.equals(detailsBean.getProductTermType())) {// 有限半开放式
            textStr[2] = getString(R.string.boc_wealth_redeem_1);
            dateStr[2] = detailsBean.getProdEnd();
        } else {
            textStr[2] = getString(R.string.boc_wealth_redeem);
            dateStr[2] = getString(R.string.boc_wealth_redeem_rule);
            buyProcedureWidget.setRightCanClick(true);
            buyProcedureWidget.setRightHint(ResultConvertUtils.convertRedeemType(detailsBean));
        }
        buyProcedureWidget.setText(textStr);
        buyProcedureWidget.setDate(dateStr);
        buyProcedureWidget.setStatus(BuyProcedureWidget.CompleteStatus.PAY);
        buyProcedureWidget.requestLayout();
    }

    /**
     * 设置产品数据
     */
    private void setProductData() {
        /*设置产品名称*/
        String currency = "";
        if (!ApplicationConst.CURRENCY_CNY.equals(detailsBean.getCurCode())) {
            currency = "[" + PublicCodeUtils.getCurrency(mContext, detailsBean.getCurCode()) + "]";
        }
        txtTitle.setText(currency + detailsBean.getProdName() + "（" + detailsBean.getProdCode() + "）");
        imgRiskLevel.setBackgroundResource(ResultConvertUtils.getRiskLevelRes(detailsBean.getProdRisklvl()));// 风险级别
        imgRiskType.setBackgroundResource(ResultConvertUtils.getRiskTypeRes(detailsBean.getProdRiskType()));// 风险类型
        /*收藏*/
        String proCode = preferences.getString(WealthDetailsFragment.PROD_CODE + detailsBean.getProdCode(), "default");
        if (proCode.equals(detailsBean.getProdCode())) {
            checkBox.setChecked(true);
        }
        setProductPeriod();// 产品进度
        if (WealthConst.PRODUCT_TYPE_2.equals(detailsBean.getProductType())) {// 净值
            String[][] priceStr = getPriceData();
            txtType.setText(priceStr[0][0]);
            txtTypeValue.setText(priceStr[0][1]);
            txtNameL.setText(priceStr[1][0]);
            txtValueL.setText(priceStr[1][1]);
            txtNameC.setText(priceStr[2][0]);
            txtValueC.setText(priceStr[2][1]);
            txtNameR.setText(priceStr[3][0]);
            txtValueR.setText(priceStr[3][1]);
            clickView1.setData(WealthViewData.productAttrNameY(), WealthViewData.productAttrValueY(detailsBean, mContext));
            clickView2.setData(WealthViewData.buyAttrNameY(), WealthViewData.buyAttrValueY(detailsBean, mContext));
            clickView3.setData(WealthViewData.redeemAttrNameY(detailsBean.getSellType()), WealthViewData.redeemAttrValueY(detailsBean));
        } else {// 非净值
            String[][] priceNStr = getPriceNData();
            txtType.setText(priceNStr[0][0]);
            txtTypeValue.setText(priceNStr[0][1]);
            txtNameL.setText(priceNStr[1][0]);
            txtValueL.setText(priceNStr[1][1]);
            txtNameC.setText(priceNStr[2][0]);
            txtValueC.setText(priceNStr[2][1]);
            txtNameR.setText(priceNStr[3][0]);
            txtValueR.setText(priceNStr[3][1]);
            clickView1.setData(WealthViewData.productAttrNameN(), WealthViewData.productAttrValueN(detailsBean, mContext));
            clickView2.setData(WealthViewData.buyAttrNameN(), WealthViewData.buyAttrValueN(detailsBean, mContext));
            clickView3.setData(WealthViewData.redeemAttrNameN(detailsBean.getSellType()), WealthViewData.redeemAttrValueN(detailsBean));
        }
    }

    /**
     * 获取产品数据（净值）
     *
     * @return
     */
    private String[][] getPriceData() {
        String[][] datas = new String[4][2];
        datas[0][0] = getString(R.string.boc_wealth_details_price);// 单位净值
        datas[0][1] = MoneyUtils.getRoundNumber(detailsBean.getPrice(), 4, BigDecimal.ROUND_HALF_UP);// 单位净值格式化
        datas[1][0] = getString(R.string.boc_wealth_details_price_date);// 净值日期
        datas[1][1] = detailsBean.getPriceDate();
        if (detailsBean.isLoginBeforeI()) {// 登录前
            llParentC.setVisibility(View.GONE);
            datas[3][0] = getString(R.string.boc_wealth_details_begin_money);// 起购金额
            datas[3][1] = MoneyUtils.getLoanAmountShownRMB1(detailsBean.getSubAmount(), detailsBean.getCurCode());
        } else {// 登录后
            datas[2][0] = getString(R.string.boc_wealth_details_begin_money);// 起购金额
            datas[2][1] = MoneyUtils.getLoanAmountShownRMB1(detailsBean.getSubAmount(), detailsBean.getCurCode());
            datas[3][0] = getString(R.string.boc_wealth_details_surplus_money);// 剩余额度
            BigDecimal surplus = new BigDecimal(detailsBean.getAvailamt());// 剩余额度
            if (surplus.compareTo(BigDecimal.valueOf(1000000000)) == 1) {
                datas[3][1] = mContext.getString(R.string.boc_wealth_money_ample);// 额度充足
            } else {
                datas[3][1] = MoneyUtils.formatMoney(detailsBean.getAvailamt());
            }
        }
        return datas;
    }

    /**
     * 获取产品数据（非净值）
     *
     * @return
     */
    private String[][] getPriceNData() {
        String[][] datas = new String[4][2];
        if (!WealthConst.IS_LOCK_PERIOD_0.equals(detailsBean.getIsLockPeriod())) {
            datas[0][0] = "业绩基准";
        } else {
            datas[0][0] = getString(R.string.boc_wealth_details_rate);// 预期年化收益率
        }
        datas[0][1] = ResultConvertUtils.convertRate(detailsBean.getYearlyRR(), detailsBean.getRateDetail());
        datas[1][0] = getString(R.string.boc_wealth_details_period);// 产品期限
        if (detailsBean.isLoginBeforeI()) {// 登录前
            llParentC.setVisibility(View.GONE);
            datas[1][1] = ResultConvertUtils.convertDate(detailsBean.getProductKind(), detailsBean.getProdTimeLimit(), detailsBean.getIsLockPeriod(), detailsBean.getProductTermType());
            datas[3][0] = getString(R.string.boc_wealth_details_begin_money);// 起购金额
            datas[3][1] = MoneyUtils.getLoanAmountShownRMB1(detailsBean.getSubAmount(), detailsBean.getCurCode());
        } else {// 登录后
            datas[1][1] = ResultConvertUtils.convertDate(detailsBean.getProductKind(), detailsBean.getProdTimeLimit(), detailsBean.getIsLockPeriod(), detailsBean.getProductTermType());
            datas[2][0] = getString(R.string.boc_wealth_details_begin_money);// 起购金额
            datas[2][1] = MoneyUtils.getLoanAmountShownRMB1(detailsBean.getSubAmount(), detailsBean.getCurCode());
            datas[3][0] = getString(R.string.boc_wealth_details_surplus_money);// 剩余额度
            BigDecimal surplus = new BigDecimal(detailsBean.getAvailamt());// 剩余额度
            if (surplus.compareTo(BigDecimal.valueOf(1000000000)) == 1) {
                datas[3][1] = mContext.getString(R.string.boc_wealth_money_ample);// 额度充足
            } else {
                datas[3][1] = MoneyUtils.formatMoney(detailsBean.getAvailamt());
            }
        }
        return datas;
    }

    /**
     * 查询投资协议接口
     */
    public void queryInvestTreaty() {
        String accountID = "";
        if (requestBean.getAccountBean() == null) {
            accountID = WealthProductFragment.getInstance().getViewModel().getAccountList().get(0).getAccountId();
            requestBean.setAccountBean(WealthProductFragment.getInstance().getViewModel().getAccountList().get(0));
        } else {
            accountID = requestBean.getAccountBean().getAccountId();
        }
        getPresenter().queryInvestTreaty(accountID, detailsBean.getProdCode());// 查询投资协议
    }

    /**
     * 投资协议申请的跳转
     */
    private void startProtocolFragment() {
        if (ApplicationContext.getInstance().isLogin()) {// 登录后
            startProtocolFragmentStep();
        } else {// 未登录
            ModuleActivityDispatcher.startToLogin(getActivity(), new LoginCallback() {
                @Override
                public void success() {
                    WealthProductFragment.getInstance().setRequestStatus(false);
                    startProtocolFragmentStep();
                }
            });
        }
    }

    /**
     * 投资协议申请
     */
    private void startProtocolFragmentStep() {
        final boolean[] needs = WealthViewData.needsStatus(PROTOCOL);
        if (WealthProductFragment.getInstance().getRequestStatus()) {// 接口调用成功
            if (WealthPublicUtils.isOpenAll(needs, WealthProductFragment.getInstance().isOpenWealth())) {
                // 所有理财状态开通查询投资协议接口
                String accountID = "";
                if (requestBean.getAccountBean() == null) {
                    accountID = WealthProductFragment.getInstance().getViewModel().getAccountList().get(0).getAccountId();
                } else {
                    accountID = requestBean.getAccountBean().getAccountId();
                }
                getPresenter().queryInvestTreaty(accountID, detailsBean.getProdCode());// 查询投资协议
            } else {
                ProtocolSelectFragment toFragment = new ProtocolSelectFragment();
                InvestTreatyFragment fragment = new InvestTreatyFragment();
                fragment.setDefaultInvestFragment(needs, toFragment);
                start(fragment);
            }
        } else {// 接口调用失败
            WealthProductFragment.getInstance().setCall(new OpenStatusI() {
                @Override
                public void openSuccess() {
                    closeProgressDialog();
                    if (WealthPublicUtils.isOpenAll(needs, WealthProductFragment.getInstance().isOpenWealth())) {
                        // 所有理财状态开通——查询投资协议接口
                        String accountID = "";
                        if (requestBean.getAccountBean() == null) {
                            accountID = WealthProductFragment.getInstance().getViewModel().getAccountList().get(0).getAccountId();
                        } else {
                            accountID = requestBean.getAccountBean().getAccountId();
                        }
                        getPresenter().queryInvestTreaty(accountID, detailsBean.getProdCode());// 查询投资协议
                    } else {
                        ProtocolSelectFragment toFragment = new ProtocolSelectFragment();
                        InvestTreatyFragment fragment = new InvestTreatyFragment();
                        fragment.setDefaultInvestFragment(needs, toFragment);
                        start(fragment);
                    }
                }

                @Override
                public void openFail(ErrorDialog dialog) {
                    closeProgressDialog();
                }
            });
            showLoadingDialog(false);
            WealthProductFragment.getInstance().requestOpenStatus();// 开通理财状态接口
        }
    }


    /**
     * 购买的跳转
     */
    private void startBuyFragment() {
        if (ApplicationContext.getInstance().isLogin()) {// 登录后
            startBuyFragmentStep();
        } else {// 未登录
            ModuleActivityDispatcher.startToLogin(getActivity(), new LoginCallback() {
                @Override
                public void success() {
                    WealthProductFragment.getInstance().setRequestStatus(false);
                    startBuyFragmentStep();
                }
            });
        }
    }

    /**
     * 购买
     */
    private void startBuyFragmentStep() {
        final boolean[] needs = WealthViewData.needsStatus(BUY);
        if (WealthProductFragment.getInstance().getRequestStatus()) {// 接口调用成功
            if (WealthPublicUtils.isOpenAll(needs, WealthProductFragment.getInstance().isOpenWealth())) {// 都开通
                WealthViewModel viewModel = WealthProductFragment.getInstance().getViewModel();
                WealthAccountBean accountBean = null;
                if (requestBean.getAccountBean() == null) {
                    accountBean = viewModel.getAccountList().get(0);
                } else {
                    accountBean = requestBean.getAccountBean();
                }
                PurchaseFragment fragment = PurchaseFragment.newInstance(WealthBundleData.buildBuyData(detailsBean,
                        accountBean), (ArrayList<WealthAccountBean>) viewModel.getAccountList());
                start(fragment);
            } else {// 存在未开通
                PurchaseFragment toFragment = new PurchaseFragment();
                InvestTreatyFragment fragment = new InvestTreatyFragment(detailsBean, requestBean);
                fragment.setDefaultInvestFragment(needs, toFragment);
                start(fragment);
            }
        } else {// 接口调用失败
            WealthProductFragment.getInstance().setCall(new OpenStatusI() {
                @Override
                public void openSuccess() {
                    closeProgressDialog();
                    if (WealthPublicUtils.isOpenAll(needs, WealthProductFragment.getInstance().isOpenWealth())) {// 都开通
                        WealthViewModel viewModel = WealthProductFragment.getInstance().getViewModel();
                        WealthAccountBean accountBean = null;
                        if (requestBean.getAccountBean() == null) {
                            accountBean = viewModel.getAccountList().get(0);
                        } else {
                            accountBean = requestBean.getAccountBean();
                        }
                        PurchaseFragment fragment = PurchaseFragment.newInstance(WealthBundleData.buildBuyData(detailsBean,
                                accountBean), (ArrayList<WealthAccountBean>) viewModel.getAccountList());
                        start(fragment);
                    } else {// 存在未开通
                        PurchaseFragment toFragment = new PurchaseFragment();
                        InvestTreatyFragment fragment = new InvestTreatyFragment(detailsBean, requestBean);
                        fragment.setDefaultInvestFragment(needs, toFragment);
                        start(fragment);
                    }
                }

                @Override
                public void openFail(ErrorDialog dialog) {
                    closeProgressDialog();
                }
            });
            showLoadingDialog(false);
            WealthProductFragment.getInstance().requestOpenStatus();// 开通理财状态接口
        }
    }

    /**
     * 组合购买的跳转
     */
    private void startBuyGroupFragment() {
        final boolean[] needs = WealthViewData.needsStatus(BUYGROUP);
        final PortfolioPurchaseFragment fragment = new PortfolioPurchaseFragment();
//        fragment.setArguments(bundle);
        if (ApplicationContext.getInstance().isLogin()) {// 已登录
            judgeToFragment(needs, fragment);
        } else {// 未登录
            ModuleActivityDispatcher.startToLogin(getActivity(), new LoginCallback() {
                @Override
                public void success() {
                    WealthProductFragment.getInstance().setRequestStatus(false);
                    judgeToFragment(needs, fragment);
                }
            });
        }
    }


    /**
     * 判断跳转到哪个页面
     *
     * @param needs
     */
    public void judgeToFragment(final boolean[] needs, final BussFragment toFragment) {
        final Bundle bundle = new Bundle();
        if (WealthProductFragment.getInstance().getRequestStatus()) {// 接口调用成功
            if (requestBean.getAccountBean() == null) {
                requestBean.setAccountBean(WealthProductFragment.getInstance().getViewModel().getAccountList().get(0));
            }
            bundle.putParcelable(PortfolioPurchaseFragment.INPUT_MODEL, WealthBundleData.buildGroupBuyData(detailsBean, requestBean));
            toFragment.setArguments(bundle);
            startToFragment(needs, toFragment);
        } else {
            WealthProductFragment.getInstance().setCall(new OpenStatusI() {
                @Override
                public void openSuccess() {
                    closeProgressDialog();
                    if (requestBean.getAccountBean() == null) {
                        requestBean.setAccountBean(WealthProductFragment.getInstance().getViewModel().getAccountList().get(0));
                    }
                    bundle.putParcelable(PortfolioPurchaseFragment.INPUT_MODEL, WealthBundleData.buildGroupBuyData(detailsBean, requestBean));
                    toFragment.setArguments(bundle);
                    startToFragment(needs, toFragment);
                }

                @Override
                public void openFail(ErrorDialog dialog) {
                    closeProgressDialog();
                }
            });
            showLoadingDialog(false);
            WealthProductFragment.getInstance().requestOpenStatus();
        }
    }

    /**
     * 开启哪一个fragment
     *
     * @param needs
     * @param toFragment
     */
    public void startToFragment(boolean[] needs, BussFragment toFragment) {
        if (WealthPublicUtils.isOpenAll(needs, WealthProductFragment.getInstance().isOpenWealth())) {// 开通所有
            start(toFragment);
        } else {
            InvestTreatyFragment fragment = new InvestTreatyFragment();
            fragment.setDefaultInvestFragment(needs, toFragment);
            start(fragment);
        }
    }

    /**
     * 收益试算的跳转
     */
    private void startProfitFragment() {
        if (ApplicationContext.isLogin()) {// 登录
//            WealthProductFragment.getInstance().judgeToFragment(needs, fragment);
            showLoadingDialog();
            getPresenter().isOpenInvestmentManage();
        } else {// 未登录
            ModuleActivityDispatcher.startToLogin(mActivity, new LoginCallback() {
                @Override
                public void success() {
//                    WealthProductFragment.getInstance().setRequestStatus(false);
//                    WealthProductFragment.getInstance().judgeToFragment(needs, fragment);
                    showLoadingDialog();
                    getPresenter().isOpenInvestmentManage();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.txt_buy_group) {// 组合购买
            startBuyGroupFragment();
        } else if (i == R.id.txt_request) {// 投资协议申请
            startProtocolFragment();
        } else if (i == R.id.txt_suggest) {// 产品说明书
            String url = WealthConst.URL_INSTRUCTION + detailsBean.getProdCode();
            start(PDFFragment.newInstance(Uri.parse(url)));
        } else if (i == R.id.txt_help) {// 帮助
            // TODO: 2016/11/20 帮助的点击
        } else if (i == R.id.ll_parent_buy) {// 购买|投资协议申请
            if (mContext.getString(R.string.boc_wealth_buy).equals(txtBottom.getText().toString())) {// 购买
                startBuyFragment();
            } else if (mContext.getString(R.string.boc_wealth_protocol).equals(txtBottom.getText().toString())) {// 投资协议申请
                startProtocolFragment();
            }
        } else if (i == R.id.ll_profit) {// 收益试算
            startProfitFragment();
        } else if (i == R.id.txt_type_value) {// 预期年化收益率
            if ("0".equals(detailsBean.getProductKind())) {
                if (!"0".equals(detailsBean.getIsLockPeriod())) {// 业绩基准产品
                    Log.i(TAG, "-------------业绩基准年化收益率详情");
                    FinancialPsnXpadExpectYieldQueryFragment fragment = new FinancialPsnXpadExpectYieldQueryFragment();
                    fragment.setYieldQueryDeta(detailsBean.getProdCode(), !(detailsBean.isLoginBeforeI()));
                    start(fragment);
                }
            }
            if (WealthConst.YES_1.equals(detailsBean.getProgressionflag())) {// 收益累进产品
                Log.i(TAG, "-------------收益累进年化收益率详情");
                String accountKey = "";
                if (!detailsBean.isLoginBeforeI()) {
                    WealthAccountBean accountBean = null;
                    if (requestBean.getAccountBean() == null) {
                        accountBean = WealthProductFragment.getInstance().getViewModel().getAccountList().get(0);
                        requestBean.setAccountBean(accountBean);
                    } else {
                        accountBean = requestBean.getAccountBean();
                    }
                    accountKey = accountBean.getAccountKey();
                }

                FinancialTypeProgressQueryFragment fragment = new FinancialTypeProgressQueryFragment();
                fragment.setReferDetail(detailsBean.getProdName(), detailsBean.getProdCode(), accountKey, !(detailsBean.isLoginBeforeI()));
                start(fragment);
            }
        }
    }

    @Override
    public void isOpenInvestmentManageFail() {
        closeProgressDialog();
    }

    @Override
    public void isOpenInvestmentManageSuccess(boolean result) {
        closeProgressDialog();
        boolean[] needs = WealthViewData.needsStatus(PROFIT);
        WealthProfitCalcFragment toFragment = new WealthProfitCalcFragment(detailsBean);
        if (result) {
            start(toFragment);
        } else {
            InvestTreatyFragment fragment = new InvestTreatyFragment();
            fragment.setDefaultInvestFragment(needs, toFragment);
            start(fragment);
        }
    }

    /**
     * 查询历史净值失败（登录后）
     */
    @Override
    public void queryNetHistoryFailY() {
        closeProgressDialog();
    }

    /**
     * 查询历史净值成功（登录后）
     */
    @Override
    public void queryNetHistorySuccessY(WealthHistoryBean wealthHistoryBean) {
        closeProgressDialog();
        ll_history_price.setVisibility(View.VISIBLE);
        List<BocTrendView.TrendVo> transedList = transTrendVos(wealthHistoryBean);
        bocTrendView.update(transedList);
    }

    /**
     * 转换为折线图数据
     *
     * @param wealthHistoryBean
     * @return
     */
    @NonNull
    private List<BocTrendView.TrendVo> transTrendVos(WealthHistoryBean wealthHistoryBean) {
        List<WealthHistoryBean.Item> list = wealthHistoryBean.getList();
        List<BocTrendView.TrendVo> transedList = new ArrayList<>();
        for (WealthHistoryBean.Item item : list) {
            BocTrendView.TrendVo trendVo = new BocTrendView.TrendVo();
            trendVo.setDate(item.getValueDate().format(DateFormatters.dateFormatter1));
            trendVo.setPrice(item.getNetValue());
            transedList.add(trendVo);
        }
        return transedList;
    }

    /**
     * 查询历史净值失败（登录前）
     */
    @Override
    public void queryNetHistoryFailN() {
//        closeProgressDialog();
    }

    /**
     * 查询历史净值成功（登录前）
     */
    @Override
    public void queryNetHistorySuccessN(WealthHistoryBean wealthHistoryBean) {
//        closeProgressDialog();
//        List<BocTrendView.TrendVo> transedList = transTrendVos(wealthHistoryBean);
//        bocTrendView.update(transedList);
//        bocTrendView.setVisibility(View.VISIBLE);
//        ll_history_price.setVisibility(View.VISIBLE);
    }

    /**
     * 明细请求失败（登录前）
     */
    @Override
    public void queryProductDetailNFail() {
        closeProgressDialog();
        rlRootView.removeAllViews();
    }

    /**
     * 明细请求成功（登录前）
     *
     * @param detailsBean
     */
    @Override
    public void queryProductDetailNSuccess(WealthDetailsBean detailsBean) {
        this.detailsBean = detailsBean;
        scrollView.setVisibility(View.VISIBLE);
        llBottom.setVisibility(View.VISIBLE);
//        if (WealthConst.PRODUCT_TYPE_2.equals(detailsBean.getProductType())) {// 净值
//            getPresenter().queryNetHistoryN(detailsBean.getProdCode(), "0");// 查询历史净值 默认查三个月
//        } else {
        closeProgressDialog();
//        }
        isShowView();
        setProductData();
    }

    /**
     * 明细请求失败（登录后）
     */
    @Override
    public void queryProductDetailYFail() {
        closeProgressDialog();
        rlRootView.removeAllViews();
    }

    /**
     * 明细请求成功（登录后）
     *
     * @param detailsBean
     */
    @Override
    public void queryProductDetailYSuccess(WealthDetailsBean detailsBean) {
        this.detailsBean = detailsBean;
        scrollView.setVisibility(View.VISIBLE);
        llBottom.setVisibility(View.VISIBLE);
        isShowView();
        setProductData();
        if (WealthConst.PRODUCT_TYPE_2.equals(detailsBean.getProductType())) {// 净值
            getPresenter().queryNetHistoryY(detailsBean.getProdCode(), "0");// 查询历史净值 默认查三个月
        } else {
            closeProgressDialog();
        }
    }


    /**
     * 查询产品投资协议成功
     */
    @Override
    public void queryInvestTreatySuccess(ProtocolModel viewModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ProtocolSelectFragment.PROTOCOL_PURCHASE, WealthBundleData.buildProtocolData(viewModel, detailsBean, requestBean));
        this.viewModel = viewModel;
        if (Integer.valueOf(viewModel.getRecordNumber()) > 1) {
            ProtocolSelectFragment fragment = new ProtocolSelectFragment();
            fragment.setArguments(bundle);
            start(fragment);
        } else if (Integer.valueOf(viewModel.getRecordNumber()) == 1) {
            InvestTreatyBean item = viewModel.getProtocolList().get(0);
            if ("1".equals(item.getAgrType())) {// 智能投资
                getPresenter().queryTreatyDetail(viewModel.getAccountList().get(0).getAccountId(), viewModel.getProtocolList().get(0).getAgrCode());
            } else if ("2".equals(item.getAgrType())) {// 定时定额投资
//                ProtocolFixPurchaseFragment fragment = new ProtocolFixPurchaseFragment();
//                fragment.setArguments(bundle);
//                start(fragment);
                FixedtimeFixedAmountInvestFragment fragment = new FixedtimeFixedAmountInvestFragment();
                fragment.setArguments(bundle);
                start(fragment);
            } else if ("3".equals(item.getAgrType())) {// 周期滚续投资
                if ("7".equals(item.getInstType())) {// 周期滚续协议
                    mProtocolModel = viewModel;
                    getPresenter().psnXpadSignInit(detailsBean, requestBean);
//                    ProtocolPeriodContinueFragment fragment = new ProtocolPeriodContinueFragment();
//                    fragment.setArguments(bundle);
//                    start(fragment);
                } else if ("8".equals(item.getInstType())) {// 业绩基准周期滚续（购买）
                    PurchaseFragment fragment = PurchaseFragment.newInstance(WealthBundleData.buildBuyData(detailsBean, requestBean.getAccountBean()), (ArrayList<WealthAccountBean>) WealthProductFragment.getInstance().getViewModel().getAccountList());
                    start(fragment);
                }
            } else if ("4".equals(item.getAgrType())) {// 余额理财投资
                ProtocolBalanceInvestFragment1 fragment = new ProtocolBalanceInvestFragment1();
                fragment.setArguments(bundle);
                start(fragment);
            }
        } else {
        }
    }

    /**
     * 周期性产品续约协议签约/签约初始化查询成功
     */
    @Override
    public void psnXpadSignInitSuccess(PsnXpadSignInitBean initBean) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ProtocolPeriodContinueFragment.SIGNINIT, initBean);
        bundle.putParcelable(ProtocolSelectFragment.PROTOCOL_PURCHASE, WealthBundleData.buildProtocolData(mProtocolModel, detailsBean, requestBean));
        ProtocolPeriodContinueFragment fragment = new ProtocolPeriodContinueFragment();
        fragment.setArguments(bundle);
        start(fragment);
    }

    @Override
    public void queryTreatyDetailSuccess(ProtocolIntelligentDetailsBean details) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ProtocolSmartFragment.DETAILS, details);
        bundle.putParcelable(ProtocolSmartFragment.MODEL, viewModel);
        ProtocolSmartFragment fragment = new ProtocolSmartFragment();
        fragment.setArguments(bundle);
        start(fragment);
    }

    @Override
    protected void titleLeftIconClick() {
        if (getArguments().getBoolean(OTHER, false)) {
            super.titleLeftIconClick();
        } else {
            popToAndReInit(WealthProductFragment.class);
        }
    }

    @Override
    public boolean onBack() {
        if (!getArguments().getBoolean(OTHER, false)) {
            popToAndReInit(WealthProductFragment.class);
            return false;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButtonClickLock.removeLock(clickOprLock);
    }
}
