package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.draw.presenter;



import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQueryAllChinaBankAccount.PsnCommonQueryAllChinaBankAccountParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQueryAllChinaBankAccount.PsnCommonQueryAllChinaBankAccountResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanApplySubmit.PsnLOANCycleLoanApplySubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanApplySubmit.PsnLOANCycleLoanApplySubmitResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanApplyVerify.PsnLOANCycleLoanApplyVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanApplyVerify.PsnLOANCycleLoanApplyVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanMinAmountQuery.PsnLOANCycleLoanMinAmountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayeeAcountCheck.PsnLOANPayeeAcountCheckParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayeeAcountCheck.PsnLOANPayeeAcountCheckResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.draw.model.LoanDrawApplySubmitReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.draw.model.LoanDrawApplyVerifyReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.draw.model.LoanDrawApplyVerifyRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.draw.ui.LoanDrawApplyContract;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepayAccountDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.CollectionAccountCheckRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.QueryAllChinaBankAccountRes;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;


/**
 * BII通信逻辑处理
 * Created by liuzc on 2016/8/24.
 */
public class LoanDrawApplayPresenter extends RxPresenter implements LoanDrawApplyContract.Presenter{
    
    /**
     * 贷款申请service
     */
    private PsnLoanService psnLoanService ;
    private GlobalService globalService;
    private LoanDrawApplyContract.DrawView drawView;
    private LoanDrawApplyContract.DrawConfirmView drawConfirmView;
    /**
     * 申请贷款预交易前会话
     */
    private String conversationId;
    private String tokenId;

    //接口请求参数model
    private LoanDrawApplyVerifyReq verifyReq;
    private LoanDrawApplySubmitReq submitReq;

    /**
     * 账户类型
     * 获取中行所有帐户列表，请求参数
     */
    private List<String> accountType;

    public LoanDrawApplayPresenter(LoanDrawApplyContract.DrawView drawView) {
        this.drawView = drawView;
        psnLoanService = new PsnLoanService();
        globalService = new GlobalService();
        drawView.setPresenter(this);
    }

    public LoanDrawApplayPresenter(LoanDrawApplyContract.DrawConfirmView drawConfirmView) {
        this.drawConfirmView = drawConfirmView;
        psnLoanService = new PsnLoanService();
        globalService = new GlobalService();
        drawConfirmView.setPresenter(this);

    }

    public void setVerifyReq(LoanDrawApplyVerifyReq verifyReq) {
        this.verifyReq = verifyReq;
    }

    public void setSubmitReq(LoanDrawApplySubmitReq submitReq) {
        this.submitReq = submitReq;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setAccountType(List<String> accountType) {
        this.accountType = accountType;
    }

    /**
     * 创建页面公共会话
     */
    @Override
    public void creatConversation() {
        //请求会话ID
        PSNCreatConversationParams conversationPreParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(conversationPreParams)
                .compose(this.<String>bindToLifecycle())
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<String>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(String s) {
                        conversationId = s;
                        drawView.obtainConversationSuccess(s);
                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        drawView.obtainConversationFail(errorException);
                    }
                });
    }

    @Override
    public void getSecurityFactor() {
        //获取安全因子
        PsnGetSecurityFactorParams params = new PsnGetSecurityFactorParams();
        params.setConversationId(conversationId);
        params.setServiceId("PB094");
        globalService.psnGetSecurityFactor(params)
                .compose(this.<PsnGetSecurityFactorResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnGetSecurityFactorResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnGetSecurityFactorResult>() {


                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnGetSecurityFactorResult result) {
                        SecurityFactorModel securityFactorModel
                                = new SecurityFactorModel(result);
                        drawView.obtainSecurityFactorSuccess(securityFactorModel);
                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        drawView.obtainSecurityFactorFail(errorException);
                    }
                });
    }

    @Override
    public void getRandom() {
        //获取随机数
        PSNGetRandomParams params = new PSNGetRandomParams();
        params.setConversationId(conversationId);
        globalService.psnGetRandom(params)
                .compose(this.<String>bindToLifecycle())
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<String>() {


                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(String result) {
                        drawConfirmView.obtainRandomSuccess(result);
                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        drawConfirmView.obtainRandomFail(errorException);
                    }
                });
    }

    //PsnLOANCycleLoanApplyVerify用款预交易
    @Override
    public void drawApplyVerify() {
        PsnLOANCycleLoanApplyVerifyParams psnLOANCycleLoanApplyVerifyParams
                = buildDrawVerify(verifyReq);
        psnLoanService.psnLOANCycleLoanApplyVerify(psnLOANCycleLoanApplyVerifyParams)
                .compose(this.<PsnLOANCycleLoanApplyVerifyResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnLOANCycleLoanApplyVerifyResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnLOANCycleLoanApplyVerifyResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnLOANCycleLoanApplyVerifyResult result) {
                        LoanDrawApplyVerifyRes res = new LoanDrawApplyVerifyRes();
                        res.set_certDN(result.get_certDN());
                        res.set_plainData(result.get_plainData());
                        res.setFactorList(result.getFactorList());
                        res.setSmcTrigerInterval(result.getSmcTrigerInterval());
                        if(drawView!=null){
                            //中银E贷用款预交易成功调用
                            drawView.drawApplyVerifySuccess(res);
                        }
                        if(drawConfirmView!=null){
                            drawConfirmView.drawApplyVerifySuccess(res);
                        }


                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        if(drawView!=null){
                            //中银E贷用款预交易失败调用
                            drawView.drawApplyVerifyFail(errorException);
                        }
                        if(drawConfirmView!=null){
                            drawConfirmView.drawApplyVerifyFail(errorException);
                        }


                    }
                });
    }

    //PsnLOANCycleLoanApplySubmit 用款提交交易
    @Override
    public void drawApplySubmit() {
        //请求token
        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
        psnGetTokenIdParams.setConversationId(conversationId);
        globalService.psnGetTokenId(psnGetTokenIdParams)
                //将任务绑定到this上，调用将任务绑定到this.onDestroy即可取消任务
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnLOANCycleLoanApplySubmitResult>>() {
                    @Override
                    public Observable<PsnLOANCycleLoanApplySubmitResult> call(String token) {
                        tokenId = token;

                        PsnLOANCycleLoanApplySubmitParams psnLOANCycleLoanApplySubmitParams
                                = buildDrawSubmit(submitReq);
                        psnLOANCycleLoanApplySubmitParams.setToken(token);
                        return psnLoanService.psnLOANCycleLoanApplySubmit(psnLOANCycleLoanApplySubmitParams);
                    }
                })
                .compose(SchedulersCompat.<PsnLOANCycleLoanApplySubmitResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnLOANCycleLoanApplySubmitResult>() {


                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnLOANCycleLoanApplySubmitResult result) {

                        //用款提交交易成功调用
                        drawConfirmView.drawApplySubmitSuccess();

                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        //用款提交交易失败调用
                        drawConfirmView.drawApplySubmitFail(errorException);

                    }
                });

    }

    //构造用款预交易请求参数
    private PsnLOANCycleLoanApplyVerifyParams buildDrawVerify(LoanDrawApplyVerifyReq req){
        PsnLOANCycleLoanApplyVerifyParams params = new PsnLOANCycleLoanApplyVerifyParams();
        params.setConversationId(req.getConversationId());
        params.set_combinId(req.get_combinId());
        params.setLoanType(req.getLoanType());
        params.setLoanActNum(req.getLoanActNum());
        params.setAvailableAmount(req.getAvailableAmount());
        params.setCurrencyCode(req.getCurrencyCode());
        params.setAmount(req.getAmount());
        params.setLoanPeriod(req.getLoanPeriod());
        params.setLoanRate(req.getLoanRate());
        params.setLoanCycleDrawdownDate(req.getLoanCycleDrawdownDate());
        params.setLoanCycleMatDate(req.getLoanCycleMatDate());
        params.setToAccountId(req.getToAccountId());
        params.setLoanCycleToActNum(req.getLoanCycleToActNum());
        params.setPayAccount(req.getPayAccount());
        params.setPayType(req.getPayType());
        params.setPayCycle(req.getPayCycle());
        return params;
    }
    //构造中银E贷用款提交交易请求参数
    private PsnLOANCycleLoanApplySubmitParams buildDrawSubmit(LoanDrawApplySubmitReq req){
        PsnLOANCycleLoanApplySubmitParams params = new PsnLOANCycleLoanApplySubmitParams();

        params.setConversationId(conversationId);
        params.setSmc(req.getSmc());
        params.setOtp(req.getOtp());
        params.setLoanType(req.getLoanType());
        params.set_signedData(req.get_signedData());
        params.setLoanActNum(req.getLoanActNum());
        params.setAvailableAmount(req.getAvailableAmount());
        params.setCurrencyCode(req.getCurrencyCode());
        params.setAmount(req.getAmount());
        params.setLoanPeriod(req.getLoanPeriod());
        params.setLoanRate(req.getLoanRate());
        params.setLoanCycleDrawdownDate(req.getLoanCycleDrawdownDate());
        params.setLoanCycleMatDate(req.getLoanCycleMatDate());
        params.setToAccountId(req.getToAccountId());
        params.setLoanCycleToActNum(req.getLoanCycleToActNum());
        params.setPayAccount(req.getPayAccount());
        params.setPayType(req.getPayType());
        params.setPayCycle(req.getPayCycle());
        params.setToken(req.getToken());

        params.setDeviceInfo(req.getDeviceInfo());
        params.setDeviceInfo_RC(req.getDeviceInfo_RC());
        params.setDevicePrint(req.getDevicePrint());
        params.setSmc_RC(req.getSmc_RC());
        params.setOtp_RC(req.getOtp_RC());
        params.setActiv(req.getActiv());
        params.setState(req.getState());
        params.setFactorId(req.getFactorId());


        return params;
    }
    /**
     * 获取中行所有帐户列表
     */
    @Override
    public void queryAllChinaBankAccount() {
        PsnCommonQueryAllChinaBankAccountParams psnCommonQueryAllChinaBankAccountParams
                = new PsnCommonQueryAllChinaBankAccountParams();
        psnCommonQueryAllChinaBankAccountParams.setAccountType(accountType);
        globalService.psnCommonQueryAllChinaBankAccount(psnCommonQueryAllChinaBankAccountParams)
                .compose(this.<List<PsnCommonQueryAllChinaBankAccountResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnCommonQueryAllChinaBankAccountResult> >applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnCommonQueryAllChinaBankAccountResult> >() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(List<PsnCommonQueryAllChinaBankAccountResult> result) {
                        //获取中行所有帐户列表，成功调用
                        drawView.obtainAllChinaBankAccountSuccess(queryAllChinaBankAccountRes(result));
                    }
                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorCode(e.getErrorMessage());
                        errorException.setErrorCode(e.getErrorType());
                        //获取中行所有帐户列表，失败调用
                        drawView.obtainAllChinaBankAccountFail(errorException);
                    }
                });
    }

    @Override
    public void checkAccountDetail(String accountID) {
        PsnAccountQueryAccountDetailParams params = new PsnAccountQueryAccountDetailParams();
        params.setAccountId(accountID);
        psnLoanService.psnAccountQueryAccountDetail(params)
                .compose(this.<PsnAccountQueryAccountDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnAccountQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnAccountQueryAccountDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        drawConfirmView.checkAccountDetailFail(errorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnAccountQueryAccountDetailResult result) {
                        PrepayAccountDetailModel.AccountDetaiListBean accountDetaiListBean=new PrepayAccountDetailModel.AccountDetaiListBean();
                        for (PsnAccountQueryAccountDetailResult.AccountDetaiListBean bean : result .getAccountDetaiList()){
                            if ("001".equals(bean.getCurrencyCode())){
                                accountDetaiListBean.setAvailableBalance(bean.getAvailableBalance());
                            }
                        }
                        drawConfirmView.checkAccountDetailSuccess(result);
                    }
                });
    }

    @Override
    public void queryLoanCycleMinAmount(final PsnLOANCycleLoanMinAmountQueryParams params) {
        psnLoanService.psnLOANCycleLoanMinAmountQuery(params)
                .compose(this.<String>bindToLifecycle())
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<String>() {
                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        drawView.queryLoanCycleMinAmountFail(errorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(String result) {
                        drawView.queryLoanCycleMinAmountSuccess(result);
                    }
                });
    }

    @Override
    public void checkPayeeAccount(PsnLOANPayeeAcountCheckParams params) {
        psnLoanService.psnLOANPayeeAcountCheck(params)
                .compose(this.<PsnLOANPayeeAcountCheckResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnLOANPayeeAcountCheckResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnLOANPayeeAcountCheckResult>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnLOANPayeeAcountCheckResult psnLOANPayeeAcountCheckResult) {
                        //收款账户检查，成功调用
                        drawView.doPayeeAccountCheckSuccess(psnLOANPayeeAcountCheckResult);
                    }
                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        //收款账户检查，失败调用
                        drawView.doPayeeAccountCheckFail(errorException);
                    }
                });
    }


    //---------------构造返回参数
    private List<QueryAllChinaBankAccountRes> queryAllChinaBankAccountRes(List<PsnCommonQueryAllChinaBankAccountResult> result){
        List<QueryAllChinaBankAccountRes> list
                = new ArrayList<QueryAllChinaBankAccountRes>();
        for(int i=0;i<result.size();i++){
            QueryAllChinaBankAccountRes queryAllChinaBankAccountRes
                    = new QueryAllChinaBankAccountRes();
            queryAllChinaBankAccountRes.setAccountId(result.get(i).getAccountId());
            queryAllChinaBankAccountRes.setAccountName(result.get(i).getAccountName());
            queryAllChinaBankAccountRes.setIsECashAccount(result.get(i).getIsECashAccount());
            queryAllChinaBankAccountRes.setIsMedicalAccount(result.get(i).getIsMedicalAccount());
            queryAllChinaBankAccountRes.setAccountNumber(result.get(i).getAccountNumber());
            queryAllChinaBankAccountRes.setAccountIbkNum(result.get(i).getAccountIbkNum());
            queryAllChinaBankAccountRes.setAccountType(result.get(i).getAccountType());
            queryAllChinaBankAccountRes.setBranchId(result.get(i).getBranchId());
            queryAllChinaBankAccountRes.setNickName(result.get(i).getNickName());
            queryAllChinaBankAccountRes.setAccountStatus(result.get(i).getAccountStatus());
            queryAllChinaBankAccountRes.setCustomerId(result.get(i).getCustomerId());
            queryAllChinaBankAccountRes.setCurrencyCode(result.get(i).getCurrencyCode());
            queryAllChinaBankAccountRes.setCurrencyCode2(result.get(i).getCurrencyCode2());
            queryAllChinaBankAccountRes.setBranchName(result.get(i).getBranchName());
            queryAllChinaBankAccountRes.setCardDescription(result.get(i).getCardDescription());
            queryAllChinaBankAccountRes.setHasOldAccountFlag(result.get(i).getHasOldAccountFlag());
            queryAllChinaBankAccountRes.setCardDescriptionCode(result.get(i).getCardDescriptionCode());
            queryAllChinaBankAccountRes.setVerifyFactor(result.get(i).getVerifyFactor());
            queryAllChinaBankAccountRes.setEcard(result.get(i).getEcard());
            list.add(queryAllChinaBankAccountRes);
        }

        return list;
    }
}
