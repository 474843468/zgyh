package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.more.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnQueryInvtBindingInfo.PsnQueryInvtBindingInfoParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundRiskEvaluationQuery.PsnFundRiskEvaluationQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundRiskEvaluationQuery.PsnFundRiskEvaluationQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvestmentManageIsOpen.PsnInvestmentManageIsOpenParams;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.common.LoginCallback;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui.AccountManagementFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui.FundFloatProfileLosslistFragemt;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.ui.StatementFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.ui.FundPositionFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.model.InvstBindingInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.presenter.FundUserPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.ui.FundH5Fragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.ui.FundUserContract;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.ui.InvestmanageFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.riskassessment.ui.FundRiskEvaluationQuestionFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.ui.TradeManagementFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * Created by liuzc on 2016/11/18.
 * 基金产品，更多页面
 */
public class FundHomeMoreOptionFragment extends MvpBussFragment<FundUserContract.Presenter>
        implements FundUserContract.View, View.OnClickListener {
    private final int CHECK_CONDITION_LOGIN = 0;//是否登陆条件检查
    private final int CHECK_CONDITION_OPEN_INVESTMENT = 1;//开通投资服务条件检查
    private final int CHECK_CONDITION_BINDING_INFO = 2;//绑定资金账号条件检查
    private final int CHECK_CONDITION_RISK_EVALUATION = 3;//进行风险测评条件检查
    private final String KEY_ACCOUNT_CURRENT_TAB_INDEX = "currentTabIndex";//当前Tab位置

    protected LinearLayout llyFundPosition; //持仓
    protected LinearLayout llyFloatingCalc; //浮动盈亏试算
    protected LinearLayout llyFundstatement;//基金对账单

    protected LinearLayout llyValidInvest; //有效定投基金
    protected LinearLayout llyInvalidInvest; //失效定投基金

    protected LinearLayout llyFundTransAccount; //基金交易账户
    protected LinearLayout llyFundTaAccount; //基金TA账户
    protected LinearLayout llyRightsNotice; //投资人权益须知

    protected LinearLayout llyTransitTrade; //在途交易
    protected LinearLayout llyHistoryTrade; //历史交易

    protected LinearLayout llyFundRec; //基金推荐

    protected LinearLayout llyHelp;  //帮助

    protected LinearLayout llyRisk;  //风险评估

    private View rootView;

    private int investmentOpenState = DataUtils.INVESTMENT_OPEN_STATE_UNCHECK; //投资服务开通状态
    private InvstBindingInfoViewModel mBindingInfoModel = null; //基金账户信息

    private int currentActionID = 0; //记录当前操作的ID（所点击的view的ID）

    private int riskEvaluationState = 0; //风险评估状态： 0：未查看； 1：未进行； 2：已进行

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_fund_home_moreopt, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        llyFundPosition = (LinearLayout) rootView.findViewById(R.id.llyFundPosition);
        llyFloatingCalc = (LinearLayout) rootView.findViewById(R.id.llyFloatingCalc);
        llyFundstatement = (LinearLayout) rootView.findViewById(R.id.llyFundstatement);
        llyValidInvest = (LinearLayout) rootView.findViewById(R.id.llyValidInvest);
        llyInvalidInvest = (LinearLayout) rootView.findViewById(R.id.llyInvalidInvest);
        llyFundTransAccount = (LinearLayout) rootView.findViewById(R.id.llyFundTransAccount);
        llyFundTaAccount = (LinearLayout) rootView.findViewById(R.id.llyFundTaAccount);
        llyRightsNotice = (LinearLayout) rootView.findViewById(R.id.llyRightsNotice);
        llyTransitTrade = (LinearLayout) rootView.findViewById(R.id.llyTransitTrade);
        llyHistoryTrade = (LinearLayout) rootView.findViewById(R.id.llyHistoryTrade);
        llyFundRec = (LinearLayout) rootView.findViewById(R.id.llyFundRec);
        llyHelp = (LinearLayout) rootView.findViewById(R.id.llyHelp);
        llyRisk = (LinearLayout) rootView.findViewById(R.id.llyrisk);

        llyFundPosition.setOnClickListener(this);
        llyFloatingCalc.setOnClickListener(this);
        llyFundstatement.setOnClickListener(this);
        llyValidInvest.setOnClickListener(this);
        llyInvalidInvest.setOnClickListener(this);
        llyFundTransAccount.setOnClickListener(this);
        llyFundTaAccount.setOnClickListener(this);
        llyRightsNotice.setOnClickListener(this);
        llyTransitTrade.setOnClickListener(this);
        llyHistoryTrade.setOnClickListener(this);
        llyFundRec.setOnClickListener(this);
        llyHelp.setOnClickListener(this);
        llyRisk.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        investmentOpenState = getArguments().getInt(DataUtils.KEY_INVESTMENT_OPEN_STATE);
        mBindingInfoModel = (InvstBindingInfoViewModel) getArguments().getSerializable(DataUtils.KEY_BINDING_INFO);
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_loan_mored);
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
    public void onClick(View view) {
        currentActionID = view.getId();

        if(shouldCheckCondition(CHECK_CONDITION_LOGIN, currentActionID)){ //登陆条件检查
            if(!ApplicationContext.getInstance().getUser().isLogin()){
                gotoLogin();
                return;
            }
        }

        if(shouldCheckCondition(CHECK_CONDITION_OPEN_INVESTMENT, currentActionID)){ //开通投资服务检查
            if(investmentOpenState == DataUtils.INVESTMENT_OPEN_STATE_UNCHECK){
                //没有检查过是否开通投资服务，则发送请求，开始检查
                showLoadingDialog();
                PsnInvestmentManageIsOpenParams params = new PsnInvestmentManageIsOpenParams();
                getPresenter().queryInvestmentManageIsOpen(params);
                return;
            }
            else if(investmentOpenState == DataUtils.INVESTMENT_OPEN_STATE_NOTOPEN){
                //尚未开通投资服务，则跳转到H5页面，去开通
                gotoOpenInvestment();
                return;
            }
        }

        doAfterInvestmentIsOpen();
    }

    /**
     * 是否应当对某一操作进行某项条件检查（比如，对持仓进行登陆条件的检查）
     * 1、帮助、权益须知无需做任何检查
     * 2、其他功能，需要进行登陆、开通投资服务、绑定资金账号操作
     * 3、交易账户、TA账户原定需进行风险测评，目前已经去掉
     * @param condition 条件码：登陆、开通投资服务、绑定资金账号、进行风险测评
     * @param actionCode 操作码：持仓、浮动盈亏试算等
     * @return
     */
    private boolean shouldCheckCondition(int condition, int actionCode){
        if(actionCode == R.id.llyFundPosition || actionCode == R.id.llyFloatingCalc ||
                actionCode == R.id.llyFundstatement || actionCode == R.id.llyValidInvest ||
                actionCode == R.id.llyInvalidInvest || actionCode == R.id.llyTransitTrade ||
                actionCode == R.id.llyHistoryTrade || actionCode == R.id.llyFundRec ||
                actionCode == R.id.llyFundTransAccount || actionCode == R.id.llyFundTaAccount){
            if(condition == CHECK_CONDITION_LOGIN || condition == CHECK_CONDITION_OPEN_INVESTMENT
                    || condition == CHECK_CONDITION_BINDING_INFO){
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * 已开通投资服务之后的操作
     */
    private void doAfterInvestmentIsOpen(){
        if(shouldCheckCondition(CHECK_CONDITION_BINDING_INFO, currentActionID)){ //绑定资金账户条件检查
            //没有请求过，则先请求
            if(mBindingInfoModel == null){
                showLoadingDialog();

                PsnQueryInvtBindingInfoParams params = new PsnQueryInvtBindingInfoParams();
                params.setInvtType("12");
                getPresenter().queryInvtBindingInfo(params);
                return;
            }
            else{
                if(StringUtils.isEmptyOrNull(mBindingInfoModel.getAccount())){
                    gotoRegisterBindingInfo();
                    return;
                }
            }
        }

        doAfterBindingInfo();
    }

    /**
     * 已绑定交易账户之后的操作
     */
    private void doAfterBindingInfo(){
        if(shouldCheckCondition(CHECK_CONDITION_RISK_EVALUATION, currentActionID)){ //风险测评条件检查
            if(riskEvaluationState == 0){
                showLoadingDialog();
                PsnFundRiskEvaluationQueryParams params = new PsnFundRiskEvaluationQueryParams();
                getPresenter().queryFundRiskEvaluation(params);
                return;
            }
            else if(riskEvaluationState == 1){
                gotoRiskEvaluation();
                return;
            }
        }

        doHandleAction();
    }

    /**
     * 前置条件（登陆、开通投资服务等）检查通过后，执行具体的操作
     */
    private void doHandleAction(){
        if (currentActionID == R.id.llyFundPosition) { //持仓
            start(new FundPositionFragment());
        }
        else if (currentActionID == R.id.llyFloatingCalc) {//浮动盈亏试算
            start(new FundFloatProfileLosslistFragemt());
        }
        else if (currentActionID == R.id.llyFundstatement) {//基金对账单
            start(new StatementFragment());
            Toast.makeText(getContext(), "undstatement", Toast.LENGTH_LONG).show();
        }
        else if (currentActionID == R.id.llyValidInvest) {//有效定投基金
            Bundle bundle = new Bundle();
            bundle.putString(DataUtils.FUND_CODE_KEY,"Y");
            InvestmanageFragment fragment = new InvestmanageFragment();
            fragment.setArguments(bundle);
            start(fragment);
            Toast.makeText(getContext(), "ValidInvest", Toast.LENGTH_LONG).show();
        }
        else if (currentActionID == R.id.llyInvalidInvest) {//失效定投基金
            Bundle bundle = new Bundle();
            bundle.putString(DataUtils.FUND_CODE_KEY, "N");
            InvestmanageFragment fragment = new InvestmanageFragment();
            fragment.setArguments(bundle);
            start(fragment);
            Toast.makeText(getContext(), "InvalidInvest", Toast.LENGTH_LONG).show();
        }
        else if (currentActionID == R.id.llyFundTransAccount) {//基金交易账户
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_ACCOUNT_CURRENT_TAB_INDEX,0);
            bundle.putSerializable(DataUtils.KEY_BINDING_INFO, mBindingInfoModel);
            AccountManagementFragment fragment= new AccountManagementFragment();
            fragment.setArguments(bundle);
            start(fragment);
//            start(new AccountManagementFragment());
        }
        else if (currentActionID == R.id.llyFundTaAccount) {//基金TA账户
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_ACCOUNT_CURRENT_TAB_INDEX,1);
            bundle.putSerializable(DataUtils.KEY_BINDING_INFO, mBindingInfoModel);
            AccountManagementFragment fragment= new AccountManagementFragment();
            fragment.setArguments(bundle);
            start(fragment);
//            start(new FundPurchaseFragment());
        }
        else if (currentActionID == R.id.llyRightsNotice) {//投资人权益须知
            Toast.makeText(getContext(), "RightsNotice", Toast.LENGTH_LONG).show();
        }
        else if (currentActionID == R.id.llyTransitTrade) {//在途交易
            Bundle bundle = new Bundle();
            bundle.putInt("currentIndex",1);
            TradeManagementFragment fragment= new TradeManagementFragment();
            fragment.setArguments(bundle);
            start(fragment);
            // Toast.makeText(getContext(), "TransitTrade", Toast.LENGTH_LONG).show();
        }
        else if (currentActionID == R.id.llyHistoryTrade) {//历史交易
            Bundle bundle = new Bundle();
            bundle.putInt("currentIndex",0);
            TradeManagementFragment fragment= new TradeManagementFragment();
            fragment.setArguments(bundle);
            start(fragment);
        }
        else if (currentActionID == R.id.llyFundRec) {//基金推荐
            Toast.makeText(getContext(), "FundRec", Toast.LENGTH_LONG).show();
        }
        else if (currentActionID == R.id.llyHelp) {//帮助
            Toast.makeText(getContext(), "Help", Toast.LENGTH_LONG).show();
        }else if (currentActionID == R.id.llyrisk){
            FundRiskEvaluationQuestionFragment fragment = new FundRiskEvaluationQuestionFragment(this.getClass());
            start(fragment);
        }
    }

    /**
     * 进入登陆页
     */
    private void gotoLogin(){
        ModuleActivityDispatcher.startToLogin(mActivity, new LoginCallback(){
            @Override
            public void success() {
                //登陆成功后，暂时无操作
            }
        });
    }

    /**
     * 进入打开投资交易页
     */
    private void gotoOpenInvestment(){
        start(new FundH5Fragment());
    }

    /**
     * 进入绑定交易账户页
     */
    private void gotoRegisterBindingInfo(){
        start(new FundH5Fragment());
    }

    /**
     * 进入风险测评页
     */
    private void gotoRiskEvaluation(){
        start(new FundH5Fragment());
    }

    @Override
    protected FundUserContract.Presenter initPresenter() {
        return new FundUserPresenter(this);
    }

    @Override
    public void queryInvestmentManageIsOpenSuccess(Boolean result) {
        closeProgressDialog();
        if(result){
            investmentOpenState = DataUtils.INVESTMENT_OPEN_STATE_OPEN;
            doAfterInvestmentIsOpen();
        }
        else{
            investmentOpenState = DataUtils.INVESTMENT_OPEN_STATE_NOTOPEN;
            gotoOpenInvestment();
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
            doAfterBindingInfo();
        }
        else{
            gotoRegisterBindingInfo();
        }
    }

    @Override
    public void queryInvtBindingInfoFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void queryFundRiskEvaluationSuccess(PsnFundRiskEvaluationQueryResult result) {
        closeProgressDialog();
        if(result != null && result.isEvaluated()){
            riskEvaluationState = 2;
            doHandleAction();
        }
        else{
            riskEvaluationState = 1;
            gotoRiskEvaluation();
        }
    }

    @Override
    public void queryFundRiskEvaluationFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void setPresenter(FundUserContract.Presenter presenter) {

    }
}
