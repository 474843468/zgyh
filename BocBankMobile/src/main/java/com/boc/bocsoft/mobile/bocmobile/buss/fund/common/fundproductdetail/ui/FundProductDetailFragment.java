package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnQueryInvtBindingInfo.PsnQueryInvtBindingInfoParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAttentionAdd.PsnFundAttentionAddResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAttentionCancel.PsnFundAttentionCancelResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAttentionQueryList.PsnFundAttentionQueryListResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundDetailQueryOutlay.PsnFundDetailQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundDetailQueryOutlay.PsnFundDetailQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundRiskEvaluationQuery.PsnFundRiskEvaluationQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvestmentManageIsOpen.PsnInvestmentManageIsOpenParams;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailRow;
import com.boc.bocsoft.mobile.bocmobile.base.widget.userwidget.echarsview.ECharsType;
import com.boc.bocsoft.mobile.bocmobile.base.widget.userwidget.echarsview.IECharsData;
import com.boc.bocsoft.mobile.bocmobile.base.widget.userwidget.sfkline.SFKLineView;
import com.boc.bocsoft.mobile.bocmobile.buss.common.LoginCallback;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.BIFundDetailParamsViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.BIFundDetailResultViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.FundNewsListViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.FundNoticeViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.JzTendencyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.KLineParams;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.RankTendencyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.WFSSFundBasicDetailParamsViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.WFSSFundBasicDetailResultViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.YieldOfWanFenTendencyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.YieldOfWeekTendencyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.YieldRateTendencyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.presenter.FundProductDetailPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.model.InvstBindingInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.ui.FundH5Fragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.ui.FundUserContract;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.ui.InvestmanageFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.purchase.ui.FundPurchaseFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.redeem.ui.FundRedeemFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import java.io.Serializable;


/**
 * 理财产品明细
 * Created by liuzc on 2016/11/25.
 */
public class FundProductDetailFragment extends MvpBussFragment<FundProductDetailContract.Presenter>
        implements FundProductDetailContract.View, android.view.View.OnClickListener {
    private final int ACTION_FIXED_INVEST = 0; //操作：定投
    private final int ACTION_REDEEM = 1; //操作：赎回
    private final int ACTION_PURCHASE = 2; //操作：购买

    protected TextView tvProductName;  //基金名称
    protected TextView tvProductCode; //基金Code
    protected ImageView imvRiskLevel; //风险级别
    protected TextView tvFundType; //基金类型
    protected ImageView imvAttention; //关注

    /**
     * 日涨跌幅、最新净值、起购金额
     */
    protected TextView tvCurrentPercent;
    protected TextView tvNewJz;
    protected TextView tvBuyStart;
    protected TextView tvCurPercentValue;
    protected TextView tvNewJzValue;
    protected TextView tvJzTime;
    protected TextView tvBuyStartValue;

    /**
     * 净值走势、累计收益率、排名变化
     */
    protected TextView tvJz;
    protected ImageView imvJzBotLine;
    protected LinearLayout llyJz;
    protected TextView tvYieldRate;
    protected ImageView imvYieldRateBotLine;
    protected LinearLayout llyYieldRate;
    protected TextView tvRank;
    protected ImageView imvRankBotLine;
    protected LinearLayout llyRank;
    protected LinearLayout llyLineTitle;

    /**
     * 月涨跌幅、查看历史净值
     */
    protected TextView tvChangeOfMonth;
    protected TextView tvChangeOfMonthValue;
    protected TextView tvLookHistValue;

    /**
     * 图表区域
     */
    protected LinearLayout llyLines;
    protected SFKLineView KLineView;//四方K线图
    protected RadioButton rbOneMonth;
    protected RadioButton rbThreeMonth;
    protected RadioButton rbSixMonth;
    protected RadioButton rbOneYear;
    protected RadioGroup rgpLayout;

    /**
     * 持仓信息：参考市值、持有份额、浮动盈亏
     */
    protected LinearLayout llyFundPositionInfo;
    protected LinearLayout llyProductProperties;
    protected TextView tvCurrentCapitals;
    protected TextView tvHoldAccount;
    protected TextView tvFloatProfit;

    /**
     * 购买属性、赎回属性
     */
    protected LinearLayout llyPurchaseTitle; //购买属性
    protected LinearLayout llyRedeemTitle; //赎回属性
    protected LinearLayout llyPurchaseAndRedeemBack;
    protected TextView tvPurchaseTitle;
    protected ImageView imvPurchaseBotLine;
    protected TextView tvRedeem;
    protected ImageView imvRedeemBotLine;
    protected LinearLayout llyPurchaseAndRedeem;
    protected LinearLayout llyPurchaseContent;
    protected LinearLayout llyRedeemContent;

    /**
     * 公告、新闻区域
     */
    protected LinearLayout llyNoticeAndNewsBack;
    protected LinearLayout llyNoticesTitle;
    protected LinearLayout llyNewsTitle;
    protected TextView tvNews;
    protected ImageView imvNewsLine;
    protected TextView tvNotices;
    protected ImageView imvNoticesLine;
    protected LinearLayout llyNewsAndNotices;
    protected LinearLayout llyNewsContent;

    /**
     * 申购费率
     */
    protected TextView tvFeeRatioTitle;
    protected TextView tvFeeRatioValueOld;
    protected TextView tvFeeRadioValueNew;
    protected LinearLayout llyFeeRatio;

    protected LinearLayout llyAction;
    protected Button btnFixedInvest; //定投
    protected Button btnRedeem; //赎回
    protected Button btnPurchase; //购买

    private android.view.View rootView = null;

    private String fundId = null; //基金ID

    private WFSSFundBasicDetailResultViewModel mWFSSFundDetail = null; //WFSS详情数据
    private BIFundDetailResultViewModel mBIFundDetailLogin = null; //登陆后从BI获取的详情数据
    private PsnFundDetailQueryOutlayResult mBIFundDetailNLog = null; //登陆前从BI获取的详情数据

    private KLineParams kLineParams; //图表的参数

    private IECharsData kLineData = null; //图表的返回数据

    private FundNoticeViewModel mFundNoticesModel = null;  //公告model
    private FundNewsListViewModel mFundNewsListModel = null; //新闻model

    private int investmentOpenState = DataUtils.INVESTMENT_OPEN_STATE_UNCHECK; //投资服务开通状态
    private InvstBindingInfoViewModel mBindingInfoModel = null; //基金账户信息

    /**
     * 点击定投、购买、赎回后，依次校验以下内容：
     * 1、如果用户未登陆，则先让用户登陆；
     * 2、登陆成功后会检查是否开通投资服务，若未开通，则进入开通页面；
     * 3、开通投资服务成功后，检查用户是否绑定交易账户，如未绑定，则进入开户页面
     * 4、以上都检查通过后，进入相应的操作页面
     */
    private int curAction = ACTION_FIXED_INVEST; //记录当前的操作

    /**
     * 请求公告或者新闻的模式，
     * 0：页面初始化时请求，先请求公告，成功后请求净值走势；如果失败，则请求新闻，之后再请求净值走势；
     * 1：点击后请求，请求成功失败后不再请求其他接口
     */
    private int requestNoticesOrNewsMode = 0;

    @Override
    protected android.view.View onCreateView(LayoutInflater mInflater) {
        rootView = android.view.View.inflate(mContext, R.layout.boc_fragment_fund_product_detail, null);
        return rootView;
    }

    @Override
    public void initView() {
        tvProductName = (TextView) rootView.findViewById(R.id.tvProductName);
        tvProductCode = (TextView) rootView.findViewById(R.id.tvProductCode);
        imvRiskLevel = (ImageView) rootView.findViewById(R.id.imvRiskLevel);
        tvFundType = (TextView) rootView.findViewById(R.id.tvFundType);
        imvAttention = (ImageView) rootView.findViewById(R.id.imvAttention);
        tvCurrentPercent = (TextView) rootView.findViewById(R.id.tvCurrentPercent);
        tvNewJz = (TextView) rootView.findViewById(R.id.tvNewJz);
        tvBuyStart = (TextView) rootView.findViewById(R.id.tvBuyStart);
        tvCurPercentValue = (TextView) rootView.findViewById(R.id.tvCurPercentValue);
        tvNewJzValue = (TextView) rootView.findViewById(R.id.tvNewJzValue);
        tvJzTime = (TextView) rootView.findViewById(R.id.tvJzTime);
        tvBuyStartValue = (TextView) rootView.findViewById(R.id.tvBuyStartValue);
        tvJz = (TextView) rootView.findViewById(R.id.tvJz);
        imvJzBotLine = (ImageView) rootView.findViewById(R.id.imvJzBotLine);
        llyJz = (LinearLayout) rootView.findViewById(R.id.llyJz);
        tvYieldRate = (TextView) rootView.findViewById(R.id.tvYieldRate);
        imvYieldRateBotLine = (ImageView) rootView.findViewById(R.id.imvYieldRateBotLine);
        llyYieldRate = (LinearLayout) rootView.findViewById(R.id.llyYieldRate);
        tvRank = (TextView) rootView.findViewById(R.id.tvRank);
        imvRankBotLine = (ImageView) rootView.findViewById(R.id.imvRankBotLine);
        llyRank = (LinearLayout) rootView.findViewById(R.id.llyRank);
        llyLineTitle = (LinearLayout) rootView.findViewById(R.id.llyLineTitle);
        tvChangeOfMonth = (TextView) rootView.findViewById(R.id.tvChangeOfMonth);
        tvChangeOfMonthValue = (TextView) rootView.findViewById(R.id.tvChangeOfMonthValue);
        tvLookHistValue = (TextView) rootView.findViewById(R.id.tvLookHistValue);
        llyLines = (LinearLayout) rootView.findViewById(R.id.llyLines);
        KLineView = (SFKLineView) rootView.findViewById(R.id.KLine_view);
        rbOneMonth = (RadioButton) rootView.findViewById(R.id.rbOneMonth);
        rbThreeMonth = (RadioButton) rootView.findViewById(R.id.rbThreeMonth);
        rbSixMonth = (RadioButton) rootView.findViewById(R.id.rbSixMonth);
        rbOneYear = (RadioButton) rootView.findViewById(R.id.rbOneYear);
        rgpLayout = (RadioGroup) rootView.findViewById(R.id.rgpLayout);
        tvCurrentCapitals = (TextView) rootView.findViewById(R.id.tvCurrentCapitals);
        tvHoldAccount = (TextView) rootView.findViewById(R.id.tvHoldAccount);
        tvFloatProfit = (TextView) rootView.findViewById(R.id.tvFloatProfit);
        llyFundPositionInfo = (LinearLayout) rootView.findViewById(R.id.llyFundPositionInfo);
        llyProductProperties = (LinearLayout) rootView.findViewById(R.id.llyProductProperties);
        tvPurchaseTitle = (TextView) rootView.findViewById(R.id.tvPurchaseTitle);
        imvPurchaseBotLine = (ImageView) rootView.findViewById(R.id.imvPurchaseBotLine);
        tvRedeem = (TextView) rootView.findViewById(R.id.tvRedeem);
        imvRedeemBotLine = (ImageView) rootView.findViewById(R.id.imvRedeemBotLine);
        llyPurchaseAndRedeem = (LinearLayout) rootView.findViewById(R.id.llyPurchaseAndRedeem);
        llyPurchaseContent = (LinearLayout) rootView.findViewById(R.id.llyPurchaseContent);
        llyRedeemContent = (LinearLayout) rootView.findViewById(R.id.llyRedeemContent);
        tvNews = (TextView) rootView.findViewById(R.id.tvNews);
        imvNewsLine = (ImageView) rootView.findViewById(R.id.imvNewsLine);
        tvNotices = (TextView) rootView.findViewById(R.id.tvNotices);
        imvNoticesLine = (ImageView) rootView.findViewById(R.id.imvNoticesLine);
        llyNewsAndNotices = (LinearLayout) rootView.findViewById(R.id.llyNewsAndNotices);
        llyNewsContent = (LinearLayout) rootView.findViewById(R.id.llyNewsContent);
        tvFeeRatioTitle = (TextView) rootView.findViewById(R.id.tvFeeRatioTitle);
        tvFeeRatioValueOld = (TextView) rootView.findViewById(R.id.tvFeeRatioValueOld);
        tvFeeRadioValueNew = (TextView) rootView.findViewById(R.id.tvFeeRadioValueNew);
        llyFeeRatio = (LinearLayout) rootView.findViewById(R.id.llyFeeRatio);
        btnFixedInvest = (Button) rootView.findViewById(R.id.btnFixedInvest);
        btnRedeem = (Button) rootView.findViewById(R.id.btnRedeem);
        btnPurchase = (Button) rootView.findViewById(R.id.btnPurchase);
        llyAction = (LinearLayout) rootView.findViewById(R.id.llyAction);

        KLineView.setTitleContentVisibility(android.view.View.GONE);
        KLineView.setShowType(2);
        llyPurchaseTitle = (LinearLayout) rootView.findViewById(R.id.llyPurchaseTitle);
        llyPurchaseTitle.setOnClickListener(FundProductDetailFragment.this);
        llyRedeemTitle = (LinearLayout) rootView.findViewById(R.id.llyRedeemTitle);
        llyRedeemTitle.setOnClickListener(FundProductDetailFragment.this);
        llyPurchaseAndRedeemBack = (LinearLayout) rootView.findViewById(R.id.llyPurchaseAndRedeemBack);
        llyNoticeAndNewsBack = (LinearLayout) rootView.findViewById(R.id.llyNoticeAndNewsBack);
        llyNoticesTitle = (LinearLayout) rootView.findViewById(R.id.llyNoticesTitle);
        llyNoticesTitle.setOnClickListener(FundProductDetailFragment.this);
        llyNewsTitle = (LinearLayout) rootView.findViewById(R.id.llyNewsTitle);
        llyNewsTitle.setOnClickListener(FundProductDetailFragment.this);
    }

    @Override
    public void initData() {
        //基金ID
        fundId = getArguments().getString(DataUtils.KEY_FUND_ID);
        //开通投资服务状态
        investmentOpenState = getArguments().getInt(DataUtils.KEY_INVESTMENT_OPEN_STATE);
        //基金绑定的账户信息
        mBindingInfoModel = (InvstBindingInfoViewModel) getArguments().getSerializable(DataUtils.KEY_BINDING_INFO);

        kLineParams = new KLineParams();

        showLoadingDialog();

        if (isLoginAndBinding()) {
            //已经登陆且绑定，先请求登陆后BI的详情，再请求四方的详情数据
            BIFundDetailParamsViewModel model = new BIFundDetailParamsViewModel();
            model.setFundCode(fundId);
            getPresenter().getBiFundDetailLogin(model);
        } else {
            //未绑定账户信息，先请求登陆前BI的详情，再请求四方的详情数据
            PsnFundDetailQueryOutlayParams params = new PsnFundDetailQueryOutlayParams();
            params.setFundCode(fundId);
            getPresenter().queryBiFundDetailNLog(params);
        }
    }

    /**
     * 是否已经登陆并且绑定了账户
     * @return
     */
    private boolean isLoginAndBinding() {
        return (ApplicationContext.getInstance().isLogin() && mBindingInfoModel != null
                && !StringUtils.isEmptyOrNull(mBindingInfoModel.getAccount()));
    }

    /**
     * 更新页面数据
     */
    private void updateViews() {
        boolean bLoginAndBinding = isLoginAndBinding(); //是否登陆且绑定账号
        if (bLoginAndBinding && (mWFSSFundDetail == null || mBIFundDetailLogin == null)) {
            return;
        }
        else if(mWFSSFundDetail == null || mBIFundDetailNLog == null){
            return;
        }

        tvProductName.setText(mWFSSFundDetail.getFundName());
        tvProductCode.setText(String.format("(%s)", mWFSSFundDetail.getFundBakCode()));

        if(bLoginAndBinding){
            if(!StringUtils.isEmptyOrNull(mBIFundDetailLogin.getFundIncomeRatio())){
                tvCurPercentValue.setText(String.format("%s%s", mBIFundDetailLogin.getFundIncomeRatio(), "%"));
            }

            //// TODO: 2016/12/29  区分货币型、非货币型
            if(!StringUtils.isEmptyOrNull(mBIFundDetailLogin.getNetPrice())){
                tvCurPercentValue.setText(mBIFundDetailLogin.getNetPrice());
            }
        }
        else{
            if(mBIFundDetailNLog.getFundIncomeRatio() != null){
                tvCurPercentValue.setText(String.format("%s%s",
                        mBIFundDetailNLog.getFundIncomeRatio().toString(), "%"));
            }

            //// TODO: 2016/12/29  区分货币型、非货币型
            if(mBIFundDetailNLog.getNetPrice() != null){
                tvCurPercentValue.setText(mBIFundDetailNLog.getNetPrice().toString());
            }

            if(mBIFundDetailNLog.getApplyLowLimit() != null){
                tvCurPercentValue.setText(mBIFundDetailNLog.getApplyLowLimit().toString());
            }
        }

        String riskLevel = mWFSSFundDetail.getLevelOfRisk();
        if (riskLevel != null) {
            if (riskLevel.equals("1")) {
                imvRiskLevel.setBackgroundResource(R.drawable.icon_risk_l);
            } else if (riskLevel.equals("2")) {
                imvRiskLevel.setBackgroundResource(R.drawable.icon_risk_m_l);
            } else if (riskLevel.equals("3")) {
                imvRiskLevel.setBackgroundResource(R.drawable.icon_risk_m);
            } else if (riskLevel.equals("4")) {
                imvRiskLevel.setBackgroundResource(R.drawable.icon_risk_m_h);
            } else if (riskLevel.equals("5")) {
                imvRiskLevel.setBackgroundResource(R.drawable.icon_risk_m_h);
            }
        }

        //// TODO: 2016/11/26  产品种类
        if (!StringUtils.isEmptyOrNull(mWFSSFundDetail.getProductType())) {
            tvFundType.setText(mWFSSFundDetail.getProductType());
        }

        updateProductProperties();
        updatePurchaseProp();

        if(bLoginAndBinding){
            llyFundPositionInfo.setVisibility(View.VISIBLE);
        }
        else{
            llyFundPositionInfo.setVisibility(View.GONE);
        }
    }

    /**
     * 更新产品属性
     */
    private void updateProductProperties() {
        llyProductProperties.removeAllViews();

        addDetailRow(llyProductProperties, "种类", "开放式基金产品");
        addDetailRow(llyProductProperties, "基金公司", "中银基金管理有限公司");
        addDetailRow(llyProductProperties, "基金公司", "中银基金管理有限公司1");
        addDetailRow(llyProductProperties, "基金公司", "中银基金管理有限公司2");
        addDetailRow(llyProductProperties, "基金公司", "中银基金管理有限公司3");
        addDetailRow(llyProductProperties, "基金公司", "中银基金管理有限公司4");
        addDetailRow(llyProductProperties, "基金公司", "中银基金管理有限公司5");
    }

    /**
     * 向布局中添加一条详情记录
     * @param llyParent
     * @param title
     * @param value
     */
    private void addDetailRow(LinearLayout llyParent, String title, String value){
        DetailRow detailRow = new DetailRow(getContext());
        detailRow.updateData(title, value);

        int padding13px = getResources().getDimensionPixelOffset(R.dimen.boc_space_between_13px);
        detailRow.setPadding(0, padding13px, 0, padding13px);
        llyParent.addView(detailRow);
    }


    /**
     * 显示购买或者赎回
     *
     * @param pageIndex 1： 购买， 2：赎回
     */
    private void showPurchaseOrRedeem(int pageIndex) {
        //购买赎回标题头灰显
        tvPurchaseTitle.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
        imvPurchaseBotLine.setVisibility(android.view.View.INVISIBLE);
        tvRedeem.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
        imvRedeemBotLine.setVisibility(android.view.View.INVISIBLE);

        if (pageIndex == 1) {
            tvPurchaseTitle.setTextColor(getResources().getColor(R.color.boc_text_color_red));
            imvPurchaseBotLine.setVisibility(android.view.View.VISIBLE);
            updatePurchaseProp();
        } else {
            tvRedeem.setTextColor(getResources().getColor(R.color.boc_text_color_red));
            imvRedeemBotLine.setVisibility(android.view.View.VISIBLE);
            updateRedeemProp();
        }
    }

    /**
     * 更新购买属性
     */
    private void updatePurchaseProp() {
        llyPurchaseContent.setVisibility(android.view.View.VISIBLE);
        llyRedeemContent.setVisibility(android.view.View.GONE);

        llyPurchaseContent.removeAllViews();

        addDetailRow(llyPurchaseContent, "首次认购下限", "100,000.00");
        addDetailRow(llyPurchaseContent, "追加认购下限", "100,000.00");
        addDetailRow(llyPurchaseContent, "追加认购下限", "100,000.00");
        addDetailRow(llyPurchaseContent, "追加认购下限", "100,000.00");
        addDetailRow(llyPurchaseContent, "追加认购下限", "100,000.00");
    }

    /**
     * 更新赎回属性
     */
    private void updateRedeemProp() {
        llyPurchaseContent.setVisibility(android.view.View.GONE);
        llyRedeemContent.setVisibility(android.view.View.VISIBLE);
        llyRedeemContent.removeAllViews();

        addDetailRow(llyRedeemContent, "最近可赎回日期", "2016/05/16");
        addDetailRow(llyRedeemContent, "赎回下限", "10份");
        addDetailRow(llyRedeemContent, "最低持有份额", "23份");
        addDetailRow(llyRedeemContent, "单日赎回上限", "1,000.00份");
    }

    /**
     * 显示新闻或者公告
     * @param pageIndex 1：公告， 2：新闻
     */
    private void showNoticesOrNews(int pageIndex){
        tvNotices.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
        imvNoticesLine.setVisibility(android.view.View.INVISIBLE);
        tvNews.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
        imvNewsLine.setVisibility(android.view.View.INVISIBLE);

        if(pageIndex == 1){
            tvNotices.setTextColor(getResources().getColor(R.color.boc_text_color_red));
            imvNoticesLine.setVisibility(android.view.View.VISIBLE);
        }
        else{
            tvNews.setTextColor(getResources().getColor(R.color.boc_text_color_red));
            imvNewsLine.setVisibility(android.view.View.VISIBLE);
        }
    }

    /**
     * 更新公告内容
     */
    private void updateNotices(FundNoticeViewModel model) {
        llyNewsContent.removeAllViews();

        if(model == null || model.getItems() == null || model.getItems().size() < 1){
            return;
        }

        for(int i = 0; i < model.getItems().size(); i ++){
            String curNotice = model.getItems().get(i).getStrcaption();
            addOneNoticeOrNews(llyNewsContent, curNotice);
            if(i >= 5){
                addOneNoticeOrNews(llyNewsContent, getString(R.string.boc_finance_account_transfer_detail_more));
                break;
            }
        }
    }

    /**
     * 更新新闻内容
     */
    private void updateNews(FundNewsListViewModel model){
        llyNewsContent.removeAllViews();

        if(model == null || model.getItems() == null || model.getItems().size() < 1){
            return;
        }
        for(int i = 0; i < model.getItems().size(); i ++){
            String curNotice = model.getItems().get(i).getTitle();
            addOneNoticeOrNews(llyNewsContent, curNotice);
            if(i >= 5){
                addOneNoticeOrNews(llyNewsContent, getString(R.string.boc_finance_account_transfer_detail_more));
                break;
            }
        }
    }

    /**
     * 添加一条新闻或者公告内容
     *
     * @param parentLayout 父布局
     * @param content      内容
     */
    private void addOneNoticeOrNews(LinearLayout parentLayout, String content) {
        android.view.View tempView = android.view.View.inflate(mContext, R.layout.boc_view_fund_notice_line, null);
        TextView tvContent = (TextView) tempView.findViewById(R.id.tv_content);
        tvContent.setText(content);

        parentLayout.addView(tempView);
    }


    @Override
    public void setListener() {
        llyJz.setOnClickListener(this);
        llyYieldRate.setOnClickListener(this);
        llyRank.setOnClickListener(this);

        tvLookHistValue.setOnClickListener(this);

        rbOneMonth.setOnClickListener(this);
        rbThreeMonth.setOnClickListener(this);
        rbSixMonth.setOnClickListener(this);
        rbOneYear.setOnClickListener(this);

        btnFixedInvest.setOnClickListener(this);
        btnRedeem.setOnClickListener(this);
        btnPurchase.setOnClickListener(this);
    }


    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_transfer_details_title);
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
    protected FundProductDetailContract.Presenter initPresenter() {
        return new FundProductDetailPresenter(this);
    }

    @Override
    public void onClick(android.view.View view) {
        int viewID = view.getId();
        if (viewID == R.id.llyJz) { //净值走势
            kLineParams.setLineType(KLineParams.LineType.jzTendency);
            updateGraphTitleAndContent();
        } else if (viewID == R.id.llyYieldRate) { //基金累计收益率
            kLineParams.setLineType(KLineParams.LineType.yieldRateTendency);
            updateGraphTitleAndContent();
        } else if (viewID == R.id.llyRank) { //排名变化
            kLineParams.setLineType(KLineParams.LineType.rankTendency);
            updateGraphTitleAndContent();
        } else if(viewID == R.id.tvLookHistValue){ //查看历史净值
            Bundle bundle = new Bundle();
            bundle.putSerializable(DataUtils.KEY_FUND_HISTORY_DATA, (Serializable) kLineData);
            FundDetailHisRecordFragment fragment = new FundDetailHisRecordFragment();
            fragment.setArguments(bundle);
            start(fragment);
        } else if (viewID == R.id.rbOneMonth) { //近一月
            kLineParams.setCycleFlag(KLineParams.CycleFlag.cycle_OneMonth);
            updateGraphTitleAndContent();
        } else if (viewID == R.id.rbThreeMonth) { //近三月
            kLineParams.setCycleFlag(KLineParams.CycleFlag.cycle_ThreeMonth);
            updateGraphTitleAndContent();
        } else if (viewID == R.id.rbSixMonth) { //近六月
            kLineParams.setCycleFlag(KLineParams.CycleFlag.cycle_SixMonth);
            updateGraphTitleAndContent();
        } else if (viewID == R.id.rbOneYear) { //近一年
            kLineParams.setCycleFlag(KLineParams.CycleFlag.cycle_OneYear);
            updateGraphTitleAndContent();
        } else if (viewID == R.id.llyPurchaseTitle) {
            showPurchaseOrRedeem(1);
        } else if (viewID == R.id.llyRedeemTitle) {
            showPurchaseOrRedeem(2);
        } else if (viewID == R.id.llyNoticesTitle) {
            requestNoticesOrNewsMode = 1;
            showNoticesOrNews(1);
            if(mFundNoticesModel != null){
                updateNotices(mFundNoticesModel);
            }
            else{
                doRequestFundNotices();
            }
        } else if (viewID == R.id.llyNewsTitle) {
            requestNoticesOrNewsMode = 1;
            showNoticesOrNews(2);
            if(mFundNewsListModel != null){
                updateNews(mFundNewsListModel);
            }
            else{
                doRequestFundNewslist();
            }
        } else if(viewID == R.id.btnFixedInvest){ //定投
            curAction = ACTION_FIXED_INVEST;
            checkLogin();
        } else if(viewID == R.id.btnRedeem){ //赎回
            curAction = ACTION_REDEEM;
            checkLogin();
        } else if(viewID == R.id.btnPurchase){ //购买
            curAction = ACTION_PURCHASE;
            checkLogin();
        }
    }

    /**
     * 检查是否登陆
     */
    private void checkLogin(){
        //需登陆
        if(!ApplicationContext.getInstance().getUser().isLogin()){
            ModuleActivityDispatcher.startToLogin(mActivity, new LoginCallback() {
                @Override
                public void success() {
                    checkInvestmentOpen();
                }
            });
        }
        else{
            checkInvestmentOpen();
        }
    }

    /**
     * 检查是否开通投资服务
     */
    private void checkInvestmentOpen(){
        if(investmentOpenState == DataUtils.INVESTMENT_OPEN_STATE_UNCHECK){
            showLoadingDialog();
            PsnInvestmentManageIsOpenParams params = new PsnInvestmentManageIsOpenParams();
            getPresenter().queryInvestmentManageIsOpen(params);
            return;
        }
        else if(investmentOpenState == DataUtils.INVESTMENT_OPEN_STATE_NOTOPEN){
            gotoH5Fragment();
            return;
        }

        checkBindingInfo();
    }

    /**
     * 检查是否绑定资金账户
     */
    private void checkBindingInfo(){
        if(mBindingInfoModel == null){
            showLoadingDialog();
            PsnQueryInvtBindingInfoParams params = new PsnQueryInvtBindingInfoParams();
            params.setInvtType("12");
            getPresenter().queryInvtBindingInfo(params);
            return;
        }
        else if(StringUtils.isEmptyOrNull(mBindingInfoModel.getAccount())){
            gotoH5Fragment();
            return;
        }

        doHandleAction();
    }

    /**
     * 前置条件（登陆、开通投资服务等）检查通过后，执行具体的操作
     */
    private void doHandleAction(){
        if(curAction == ACTION_FIXED_INVEST){ //定投
            start(new InvestmanageFragment());
        }
        else if(curAction == ACTION_REDEEM){ //赎回
            start(new FundRedeemFragment());
        }
        else if(curAction == ACTION_PURCHASE){//购买
            start(new FundPurchaseFragment());
        }
    }

    /**
     * 进入H5开户页面
     */
    private void gotoH5Fragment(){
        start(new FundH5Fragment());
    }

    /**
     * 请求基金公告
     */
    private void doRequestFundNotices(){
        showLoadingDialog();

        FundNoticeViewModel model = new FundNoticeViewModel();
        model.setPageNo("1");
        model.setPageSize(String.valueOf(ApplicationConst.FUND_PAGE_SIZE));
        model.setFundId(fundId);
        getPresenter().queryFundNotices(model);
    }

    /**
     * 从WFSS查询基金的详情信息
     */
    private void queryWFSSFundDetail(){
        showLoadingDialog();
        WFSSFundBasicDetailParamsViewModel model = new WFSSFundBasicDetailParamsViewModel();
        model.setFundId(fundId);
        getPresenter().queryWFSSFundDetail(model);
    }

    /**
     * 请求基金新闻列表
     */
    private void doRequestFundNewslist(){
        showLoadingDialog();

        FundNewsListViewModel model = new FundNewsListViewModel();
        model.setPageNo("1");
        model.setPageSize(String.valueOf(ApplicationConst.FUND_PAGE_SIZE));
        if(mWFSSFundDetail != null){
            model.setFundBakCode(mWFSSFundDetail.getFundBakCode());
        }
        getPresenter().queryFundNewsList(model);
    }

    /**
     * 更新图表的标题和内容
     */
    private void updateGraphTitleAndContent(){
        tvChangeOfMonth.setVisibility(View.GONE);
        tvLookHistValue.setVisibility(View.GONE);
        KLineView.setVisibility(android.view.View.INVISIBLE);
        updateKLineTypeTitleViews(kLineParams.getLineType());
        queryGraphData();
    }

    /**
     * 更新图表类型标题view
     * @param lineType 图表类型
     */
    private void updateKLineTypeTitleViews(KLineParams.LineType lineType) {
        TextView tvJz = (TextView) llyJz.findViewById(R.id.tvJz);
        ImageView imvJzBotLine = (ImageView) llyJz.findViewById(R.id.imvJzBotLine);
        tvJz.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
        imvJzBotLine.setBackgroundColor(getResources().getColor(R.color.white));

        TextView tvYieldRate = (TextView) llyYieldRate.findViewById(R.id.tvYieldRate);
        ImageView imvYieldRateBotLine = (ImageView) llyYieldRate.findViewById(R.id.imvYieldRateBotLine);
        tvYieldRate.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
        imvYieldRateBotLine.setBackgroundColor(getResources().getColor(R.color.white));

        TextView tvRank = (TextView) llyRank.findViewById(R.id.tvRank);
        ImageView imvRankBotLine = (ImageView) llyRank.findViewById(R.id.imvRankBotLine);
        tvRank.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
        imvRankBotLine.setBackgroundColor(getResources().getColor(R.color.white));

        if (lineType == KLineParams.LineType.jzTendency) {
            tvJz.setTextColor(getResources().getColor(R.color.boc_text_color_red));
            imvJzBotLine.setBackgroundColor(getResources().getColor(R.color.boc_text_color_red));
        } else if (lineType == KLineParams.LineType.yieldRateTendency) {
            tvYieldRate.setTextColor(getResources().getColor(R.color.boc_text_color_red));
            imvYieldRateBotLine.setBackgroundColor(getResources().getColor(R.color.boc_text_color_red));
        } else if (lineType == KLineParams.LineType.rankTendency) {
            tvRank.setTextColor(getResources().getColor(R.color.boc_text_color_red));
            imvRankBotLine.setBackgroundColor(getResources().getColor(R.color.boc_text_color_red));
        }
    }

    /**
     * 请求图表数据
     */
    private void queryGraphData() {
        showLoadingDialog();

        KLineParams.LineType lineType = kLineParams.getLineType();

        if (lineType == KLineParams.LineType.jzTendency) {
            JzTendencyViewModel model = new JzTendencyViewModel();
            model.setFundId(fundId);
            model.setFundCycle(kLineParams.getCycleFlagParamCode());
            getPresenter().queryJzTendency(model);
        } else if (lineType == KLineParams.LineType.yieldRateTendency) {
            YieldRateTendencyViewModel model = new YieldRateTendencyViewModel();
            model.setFundId(fundId);
            model.setFundCycle(kLineParams.getCycleFlagParamCode());
            getPresenter().queryYieldRateTendency(model);
        } else if (lineType == KLineParams.LineType.rankTendency) {
            RankTendencyViewModel model = new RankTendencyViewModel();
            model.setFundId(fundId);
            model.setFundCycle(kLineParams.getCycleFlagParamCode());
            getPresenter().queryRrankTendency(model);
        }
    }

    /**
     *  请求图表数据成功之后,更新图表view
     */
    private void updateGraphViews(){
        KLineParams.LineType lineType = kLineParams.getLineType();

        if (lineType == KLineParams.LineType.jzTendency) {
            KLineView.setECharsViewData(ECharsType.QuShi, kLineData);  //设置趋势图等折线图数据
            KLineView.setVisibility(android.view.View.VISIBLE);
            //累计收益率
        } else if (lineType == KLineParams.LineType.yieldRateTendency) {

            //累计收益view层数据
            if(kLineData instanceof YieldRateTendencyViewModel){
                YieldRateTendencyViewModel curData = (YieldRateTendencyViewModel)kLineData;
                //理财型基金 且登陆
                if(mBIFundDetailLogin != null && !StringUtils.isEmptyOrNull(mBIFundDetailLogin.getFntype())
                        && mBIFundDetailLogin.getFntype().equals("01")){
                    //设置趋势图等折线图数据
                    KLineView.setECharsViewData(ECharsType.MutilLine, curData.getLine2(),
                            curData.getLine32(), curData.getLine4());
                }
                else{
                    //非理财型
                    KLineView.setECharsViewData(ECharsType.MutilLine, curData.getLine1(),
                            curData.getLine3(), curData.getLine2());
                }
            }

            KLineView.setVisibility(android.view.View.VISIBLE);
            //排名变化
        } else if (lineType == KLineParams.LineType.rankTendency) {
            KLineView.setECharsViewData(ECharsType.ZheXianDian, kLineData);
            KLineView.setVisibility(android.view.View.VISIBLE);
            //七日年化收益率
        } else if (lineType == KLineParams.LineType.yieldOfWeekTendency) {

        } else if (lineType == KLineParams.LineType.yieldOfWanfenTendency) {

        }

        if(kLineData != null && kLineData.getXList() != null && kLineData.getXList().size() > 0){
            tvChangeOfMonth.setVisibility(View.VISIBLE);
            tvLookHistValue.setVisibility(View.VISIBLE);
            tvChangeOfMonth.setText(String.format("%s%s%s", kLineParams.getCycleFlagParamDesc(getContext()),
                    getString(R.string.boc_long_short_forex_change), "(%):"));
            tvLookHistValue.setText(String.format("%s%s", getString(R.string.boc_fund_search),
                    kLineParams.getLineTypeRecordTitle(getContext())));
        }
    }

    @Override
    public void queryWFSSFundDetailSuccess(WFSSFundBasicDetailResultViewModel result) {
        mWFSSFundDetail = result;
        updateViews();

        doRequestFundNotices();
    }

    @Override
    public void queryWFSSFundDetailFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();

        doRequestFundNotices();
        //// TODO: 2016/12/29
    }

    //登录前从BI获取基金详情数据成功
    @Override
    public void queryBiFundDetailNLogSuccess(PsnFundDetailQueryOutlayResult result) {
        Log.e("qq", ".........................queryBiFundDetailNLogSuccess");
        mBIFundDetailNLog = result;
        queryWFSSFundDetail();
    }

    //登录前从BI获取基金详情数据失败
    @Override
    public void queryBiFundDetailNLogFail(BiiResultErrorException biiResultErrorException) {
        Log.e("qq", ".........................queryBiFundDetailNLogFail");

        queryWFSSFundDetail();
    }

    //登录后从BI获取基金详情数据成功
    @Override
    public void getBiFundDetailLoginSuccess(BIFundDetailResultViewModel result) {
        mBIFundDetailLogin = result;
        queryWFSSFundDetail();
    }

    //登录后从BI获取基金详情数据失败
    @Override
    public void getBiFundDetailLoginFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();

    }

    //请求图表数据成功
    @Override
    public void queryJzTendencySuccss(JzTendencyViewModel result) {
        closeProgressDialog();

        kLineData = result;
        updateGraphViews();
    }
    //请求图表数据失败
    @Override
    public void queryJzTendencyFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void queryYieldRateTendencySuccess(YieldRateTendencyViewModel result) {
        closeProgressDialog();
        kLineData = result;
        updateGraphViews();
    }

    @Override
    public void queryYieldRateTendencyFail(BiiResultErrorException biiResultErrorException) {
        Log.e("qq", "...........................queryYieldRateTendencyFail: ");
        closeProgressDialog();
    }

    @Override
    public void queryRrankTendencySuccess(RankTendencyViewModel result) {
        Log.e("qq", "...........................queryJzTendencySuccss: " + result.getItems().size());
        closeProgressDialog();
        kLineData = result;
        updateGraphViews();
    }

    @Override
    public void queryRrankTendencyFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void queryYieldOfWeekTendencySuccess(YieldOfWeekTendencyViewModel result) {

    }

    @Override
    public void queryYieldOfWeekTendencyFail(BiiResultErrorException biiResultErrorException) {

    }

    @Override
    public void queryYieldOfWanFenTendencySuccess(YieldOfWanFenTendencyViewModel result) {

    }

    @Override
    public void queryYieldOfWanFenTendencyFail(BiiResultErrorException biiResultErrorException) {

    }

    @Override
    public void queryFundNoticesSuccess(FundNoticeViewModel result) {
        closeProgressDialog();
        mFundNoticesModel = result;
        updateNotices(result);

        if(requestNoticesOrNewsMode == 0){
            queryGraphData();
        }
    }

    @Override
    public void queryFundNoticesFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        updateNotices(null);

        if(requestNoticesOrNewsMode == 0){
            showLoadingDialog();
            showNoticesOrNews(2);
            doRequestFundNewslist();
        }
    }

    @Override
    public void queryNewsListSuccess(FundNewsListViewModel result) {
        closeProgressDialog();
        mFundNewsListModel = result;
        updateNews(result);

        if(requestNoticesOrNewsMode == 0){
            queryGraphData();
        }
    }

    @Override
    public void queryNewsListFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        updateNews(null);

        if(requestNoticesOrNewsMode == 0){
            queryGraphData();
        }
    }

    @Override
    public void queryFundAttentionListSuccess(PsnFundAttentionQueryListResult result) {

    }

    @Override
    public void queryFundAttentionListFail(BiiResultErrorException biiResultErrorException) {

    }

    @Override
    public void addFundAttentionSuccess(PsnFundAttentionAddResult result) {

    }

    @Override
    public void addFundAttentionFail(BiiResultErrorException biiResultErrorException) {

    }

    @Override
    public void cancelFundAttentionSuccess(PsnFundAttentionCancelResult result) {

    }

    @Override
    public void cancelFundAttentionFail(BiiResultErrorException biiResultErrorException) {

    }


    @Override
    public void queryInvestmentManageIsOpenSuccess(Boolean result) {
        closeProgressDialog();
        if(result){
            investmentOpenState = DataUtils.INVESTMENT_OPEN_STATE_OPEN;
            checkBindingInfo();
        }
        else{
            investmentOpenState = DataUtils.INVESTMENT_OPEN_STATE_NOTOPEN;
            gotoH5Fragment();
        }
    }

    @Override
    public void queryInvestmentManageIsOpenFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void queryInvtBindingInfoSuccess(InvstBindingInfoViewModel result) {
        closeProgressDialog();
        mBindingInfoModel = result;
        if(mBindingInfoModel != null && !StringUtils.isEmptyOrNull(mBindingInfoModel.getAccount())){
            doHandleAction();
        }
        else{
            gotoH5Fragment();
        }
    }

    @Override
    public void queryInvtBindingInfoFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void queryFundRiskEvaluationSuccess(PsnFundRiskEvaluationQueryResult result) {

    }

    @Override
    public void queryFundRiskEvaluationFail(BiiResultErrorException biiResultErrorException) {

    }

    @Override
    public void setPresenter(FundUserContract.Presenter presenter) {

    }
}
