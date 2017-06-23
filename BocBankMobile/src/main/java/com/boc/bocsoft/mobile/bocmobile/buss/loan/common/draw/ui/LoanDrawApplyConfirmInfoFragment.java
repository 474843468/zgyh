package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.draw.ui;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.draw.model.LoanDrawApplySubmitReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.draw.model.LoanDrawApplyVerifyReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.draw.model.LoanDrawApplyVerifyRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.draw.presenter.LoanDrawApplayPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.model.LOANCycleLoanEApplySubmitReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.LinkedHashMap;

/**
 * 贷款管理-非中银E贷-提款确认信息页面fragment
 * Created by liuzc on 2016/8/24.
 */
public class LoanDrawApplyConfirmInfoFragment extends BussFragment implements LoanDrawApplyContract.DrawConfirmView {
    private static final String TAG = "LoanDrawApplyConfirm";

    //确认页面
    protected ConfirmInfoView confirmInfoView;
    //页面名称
    private static String PAGENAME;
    private LoanDrawApplyResultsFragment resultsFragment;
    private boolean bAvailable;

    private SecurityVerity securityVerity;
    private LoanDrawApplayPresenter drawPresenter;
    private String conversationId;
    private String _combinId, _combinName;
    private LoanDrawApplyVerifyReq applyVerifyReq;
    private LoanDrawApplySubmitReq req;

    private String random = null; //随机数

    private String repayCardNumber = null; //还款账户卡号
    private String accountID = null;

    private String repayPeriodCode = null; //还款周期

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        confirmInfoView = new ConfirmInfoView(mContext);
        return confirmInfoView;
    }

    @Override
    protected void titleLeftIconClick() {
        super.titleLeftIconClick();
    }

    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_finance_account_transfer_confirm_title);
    }

    public void setApplyVerifyReq(LoanDrawApplyVerifyReq req) {
        this.applyVerifyReq = req;
    }

    @Override
    public void beforeInitView() {

    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }
    public void set_combinName(String value) {
        this._combinName = value;
    }
    public void setRepayCardNumber(String value){
        this.repayCardNumber = value;
    }
    public void setRepayPeriod(String value){
        this.repayPeriodCode = value;
    }
    public void setAccountID(String value){
        this.accountID = value;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
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
    public void initView() {
        drawPresenter = new LoanDrawApplayPresenter(this);
        securityVerity = SecurityVerity.getInstance(getActivity());

        resultsFragment
                = new LoanDrawApplyResultsFragment();
    }

    @Override
    public void initData() {
        drawPresenter.setConversationId(conversationId);
        securityVerity.setConversationId(conversationId);

        String  currencyCode = applyVerifyReq.getCurrencyCode();
        confirmInfoView.setHeadValue(getString(R.string.boc_pledge_info_confirm_head,
                com.boc.bocsoft.mobile.bocmobile.buss.loan.utils.DataUtils.getCurrencyDescByLetter(mContext, currencyCode)),
                MoneyUtils.transMoneyFormat(applyVerifyReq.getAmount().toString(), currencyCode));



        LinkedHashMap<String, String> datas = new LinkedHashMap<String, String>();
        datas.put(getResources().getString(R.string.boc_repaydetails_interest),
                getResources().getString(R.string.boc_loan_n_month, applyVerifyReq.getLoanPeriod())
                +"/"+ String.format("%s%s", MoneyUtils.transRateFormat(applyVerifyReq.getLoanRate()), "%"));

        String repayTypeDesc = com.boc.bocsoft.mobile.bocmobile.buss.loan.utils.DataUtils.getRepayTypeDesc(mContext,
                applyVerifyReq.getPayType(), repayPeriodCode);
        if(!StringUtils.isEmptyOrNull(repayTypeDesc)){
            datas.put(getResources().getString(R.string.boc_details_repaytype), repayTypeDesc);
        }

        String repayPeriod =  DataUtils.getLoanRepayPeriodDesc(mContext, applyVerifyReq.getPayCycle());
        if(!StringUtils.isEmptyOrNull(repayPeriod)){
            datas.put(getString(R.string.boc_facility_loan_period_unit), repayPeriod);
        }

        if(!StringUtils.isEmptyOrNull(applyVerifyReq.getLoanCycleToActNum()) &&  !applyVerifyReq.getLoanCycleToActNum().equals("0")){
            datas.put(getResources().getString(R.string.boc_eloan_draw_receiptAccount),
                    NumberUtils.formatCardNumberStrong(applyVerifyReq.getLoanCycleToActNum()));
        }

        if(!StringUtils.isEmptyOrNull(repayCardNumber) &&  !repayCardNumber.equals("0")){
            datas.put(getResources().getString(R.string.boc_eloan_prepay_repaymentAccount),
                    NumberUtils.formatCardNumberStrong(repayCardNumber));
        }

        if(!StringUtils.isEmptyOrNull(applyVerifyReq.getLoanCycleMatDate()) && !applyVerifyReq.getLoanCycleMatDate().equals("0")){
            datas.put(getString(R.string.boc_details_loanDate), applyVerifyReq.getLoanCycleMatDate());
        }

        confirmInfoView.addData(datas);
        confirmInfoView.updateSecurity(_combinName);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"onResume------------------>onResume");
    }

    @Override
    public void setListener() {
        //安全组件点击监听
        securityVerity.setSecurityVerifyListener(new SecurityVerity.VerifyCodeResultListener() {
            @Override
            public void onSecurityTypeSelected(CombinListBean bean) {
                _combinId = bean.getId();
                confirmInfoView.updateSecurity(bean.getName());
            }

            @Override
            public void onEncryptDataReturned(String factorId, String[] randomNums, String[] encryptPasswords) {
                //提交接口调用
                buildSubmitReq(factorId, randomNums, encryptPasswords);
            }

            @Override
            public void onSignedReturn(String signRetData) {
                submitForVoiKey(signRetData);
            }
        });

        confirmInfoView.setListener(new ConfirmInfoView.OnClickListener(){
            @Override
            public void onClickConfirm() {
                applyVerifyReq.set_combinId(_combinId);
                drawPresenter.setVerifyReq(applyVerifyReq);
                //点击下一步按钮，跳转到提款确认页
                drawPresenter.drawApplyVerify();
                showLoadingDialog(false);
            }

            @Override
            public void onClickChange() {
                securityVerity.selectSecurityType();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        drawPresenter.unsubscribe();
    }


    @Override
    public void drawApplyVerifySuccess(LoanDrawApplyVerifyRes result) {
        Log.i(TAG, "提款预交易成功！");
        closeProgressDialog();
        bAvailable = securityVerity.confirmFactor(result.getFactorList());
        if (bAvailable) {
            //音频key加密
            EShieldVerify.getInstance(getActivity()).setmPlainData(result.get_plainData());
            //获取随机数
            drawPresenter.getRandom();
            showLoadingDialog(false);
        } else {
            //TODO 获取确定的安全因子失败
        }

    }

    public void setbAvailable(boolean bAvailable) {
        this.bAvailable = bAvailable;
    }

    @Override
    public void drawApplyVerifyFail(ErrorException e) {
        Log.i(TAG, "提款预交易失败！");
        closeProgressDialog();
    }

    @Override
    public void obtainRandomSuccess(String random) {
        Log.i(TAG, "获取随机数成功！");
        this.random = random;
        closeProgressDialog();
        //弹出选择的安全认证工具
        securityVerity.showSecurityDialog(random);


    }

    @Override
    public void obtainRandomFail(ErrorException e) {
        Log.i(TAG, "获取随机数失败！");
    }

    @Override
    public void drawApplySubmitSuccess() {
        Log.i(TAG, "提款交易成功！");

        //检查卡内余额，再进入结果页
        drawPresenter.checkAccountDetail(accountID);
    }


    /**
     * 进入结果页
     * @param remainAmount 卡内余额
     */
    private void gotoResultPage(String remainAmount){
        resultsFragment.setStatus(OperationResultHead.Status.SUCCESS);
        Bundle bundle=new Bundle();
        bundle.putSerializable(EloanConst.ELON_DRAW_COMMIT,req);
        resultsFragment.setArguments(bundle);

        if(!StringUtils.isEmptyOrNull(remainAmount)){
            resultsFragment.setRemainAmout(remainAmount);
        }
        resultsFragment.setRepayCardNumber(repayCardNumber);
        start(resultsFragment);
    }

    @Override
    public void drawApplySubmitFail(ErrorException e) {
        Log.i(TAG, "提款交易失败！");
        closeProgressDialog();
    }

    @Override
    public void checkAccountDetailSuccess(PsnAccountQueryAccountDetailResult result) {
        String remainAmount = null;//卡内余额
        if(result != null && result.getAccountDetaiList() != null && result.getAccountDetaiList().size() > 0){
            for(int i = 0; i < result.getAccountDetaiList().size(); i ++){
                PsnAccountQueryAccountDetailResult.AccountDetaiListBean bean = result.getAccountDetaiList().get(i);

                if(isSameCurrency(bean.getCurrencyCode(), req.getCurrencyCode())){
                    remainAmount = bean.getAvailableBalance().toString();
                }
            }
        }
        closeProgressDialog();
        gotoResultPage(remainAmount);
    }

    /**
     * 比较两个币种是否相同， 比重可能是001、也可能是CNY这种形式
     * @param currency1
     * @param currency2
     * @return
     */
    private boolean isSameCurrency(String currency1, String currency2){
        if(StringUtils.isEmptyOrNull(currency1) || StringUtils.isEmptyOrNull(currency2)){
            return false;
        }

        String curDesc1 = DataUtils.getCurrencyDescByLetter(mContext, currency1);
        String curDesc2 = DataUtils.getCurrencyDescByLetter(mContext, currency2);

        if(!StringUtils.isEmptyOrNull(curDesc1) && !StringUtils.isEmptyOrNull(curDesc2) &&
                curDesc1.equals(curDesc2)){
            return true;
        }

        return false;
    }

    @Override
    public void checkAccountDetailFail(ErrorException e) {
        closeProgressDialog();
        gotoResultPage(null);
    }

    @Override
    public void setPresenter(LoanDrawApplyContract.Presenter presenter) {

    }

    private void buildSubmitReq(String factorId, String[] randomNums, String[] encryptPasswords){
//        LOANDrawApplySubmitReq req = new LOANDrawApplySubmitReq();
        req = new LoanDrawApplySubmitReq();

        if ("8".equals(factorId)) {
            //动态口令
            req.setOtp(encryptPasswords[0]);
            req.setOtp_RC(randomNums[0]);
            req.setFactorId(factorId);
        } else if ("32".equals(factorId)) {
            //短信验证码
            req.setSmc(encryptPasswords[0]);
            req.setSmc_RC(randomNums[0]);
            req.setFactorId(factorId);
        } else if ("40".equals(factorId)) {
            //动态口令+短信验证码
            req.setOtp(encryptPasswords[0]);
            req.setOtp_RC(randomNums[0]);
            req.setSmc(encryptPasswords[1]);
            req.setSmc_RC(randomNums[1]);
            req.setFactorId(factorId);
        } else if ("96".equals(factorId)) {
            //短信验证码
            req.setSmc(encryptPasswords[0]);
            req.setSmc_RC(randomNums[0]);
            req.setFactorId(factorId);
            DeviceInfoModel infoModel =  DeviceInfoUtils.getDeviceInfo(getContext(),random, ApplicationContext.getInstance().getUser().getOperatorId());

            req.setDeviceInfo(infoModel.getDeviceInfo());
            req.setDeviceInfo_RC(infoModel.getDeviceInfo_RC());
            req.setDevicePrint(DeviceInfoUtils.getDevicePrint(getActivity()));
        }else if ("4".equals(factorId)) {

        }
        doBuildParamsAndSubmit();
    }

    /**
     * 音频key请求
     * @param signRetData
     */
    private void submitForVoiKey(String signRetData){
        if(req == null){
            req = new LoanDrawApplySubmitReq();
        }
        req.set_signedData(signRetData);
        req.setFactorId("4");
        doBuildParamsAndSubmit();
    }

    /**
     * 构造请求参数公共部分，再发送请求
     */
    private void doBuildParamsAndSubmit(){
        if(req == null){
            return;
        }

        showLoadingDialog(false);

        req.setActiv(SecurityVerity.getInstance(getActivity()).getCfcaVersion());
        req.setState(SecurityVerity.SECURITY_VERIFY_STATE);

        req.setLoanActNum(applyVerifyReq.getLoanActNum());
        req.setLoanType(applyVerifyReq.getLoanType());
        req.setAvailableAmount(MoneyUtils.transMoneyFormat(applyVerifyReq.getAvailableAmount(),
                applyVerifyReq.getCurrencyCode()));
        req.setCurrencyCode(applyVerifyReq.getCurrencyCode());
        req.setAmount(MoneyUtils.transMoneyFormat(applyVerifyReq.getAmount(),
                applyVerifyReq.getCurrencyCode()));
        req.setLoanCycleToActNum(applyVerifyReq.getLoanCycleToActNum());
        req.setToAccountId(applyVerifyReq.getToAccountId());
        req.setLoanCycleDrawdownDate(applyVerifyReq.getLoanCycleDrawdownDate());
        req.setLoanCycleMatDate(applyVerifyReq.getLoanCycleMatDate());
        req.setLoanPeriod(applyVerifyReq.getLoanPeriod());
        req.setPayType(applyVerifyReq.getPayType());
        req.setLoanRate(applyVerifyReq.getLoanRate());
        req.setPayAccount(applyVerifyReq.getPayAccount());
        req.setPayCycle(applyVerifyReq.getPayCycle());

        drawPresenter.setSubmitReq(req);
        drawPresenter.drawApplySubmit();
    }

}
