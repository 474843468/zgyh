package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.changeaccount.presenter;

import android.util.Log;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANCycleLoanAccountListQuery.PsnCycleLoanAccountEQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANCycleLoanAccountListQuery.PsnCycleLoanAccountEQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeLoanRepayAccountSubmit.PsnLOANChangeLoanRepayAccountSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeLoanRepayAccountSubmit.PsnLOANChangeLoanRepayAccountSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeLoanRepayAccountVerify.PsnLOANChangeLoanRepayAccountVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeLoanRepayAccountVerify.PsnLOANChangeLoanRepayAccountVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.changeaccount.model.ChangeAccountSubmitReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.changeaccount.model.ChangeAccountVerifyReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.changeaccount.model.ChangeAccountVerifyRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.changeaccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.changeaccount.ui.ChangeAccountContract;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepayAccountDetailModel;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by liuzc on 2016/8/25
 */
public class ChangeAccountPresenter extends RxPresenter implements ChangeAccountContract.Presenter{
    /**
     * 贷款申请service
     */
    private PsnLoanService psnLoanService;

    private ChangeAccountContract.ChangeAccountView changeAView;

    private GlobalService globalService;

    private ChangeAccountVerifyReq changeAccountVerifyReq;
    private ChangeAccountSubmitReq
            changeAccountSubmitReq;
    /**
     * 申请贷款预交易前会话
     */
    private String conversationId;
    private String tokenId;

    /**
     * 账户类型
     * 获取中行所有帐户列表，请求参数
     */
    private List<String> accountType;


    public ChangeAccountPresenter(ChangeAccountContract.ChangeAccountView changeAView) {
        this.changeAView = changeAView;
        psnLoanService = new PsnLoanService();
        globalService = new GlobalService();
        changeAView.setPresenter(this);
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setChangeAccountVerifyReq(ChangeAccountVerifyReq changeAccountVerifyReq) {
        this.changeAccountVerifyReq = changeAccountVerifyReq;
    }

    public void setChangeAccountSubmitReq(ChangeAccountSubmitReq changeAccountSubmitReq) {
        this.changeAccountSubmitReq = changeAccountSubmitReq;
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
                        changeAView.obtainConversationSuccess(s);
                    }
                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        changeAView.obtainConversationFail(errorException);
                    }
                });
    }

    @Override
    public void getSecurityFactor() {
        //获取安全因子
        PsnGetSecurityFactorParams params = new PsnGetSecurityFactorParams();
        params.setConversationId(conversationId);
        params.setServiceId("PB095");
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
                        changeAView.obtainSecurityFactorSuccess(securityFactorModel);
                    }
                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        changeAView.obtainSecurityFactorFail(errorException);
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
                        changeAView.obtainRandomSuccess(result);
                    }
                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        changeAView.obtainRandomFail(errorException);
                    }
                });
    }

    //变更循环非循环还款账户预交易        PsnLOANChangeLoanRepayAccountVerify
    @Override
    public void changeAccountOEVerify() {
        PsnLOANChangeLoanRepayAccountVerifyParams psnLOANChangeLoanRepayAccountVerifyParams
                =buildChangeAccountVerify(changeAccountVerifyReq);
        psnLoanService.psnLOANChangeLoanRepayAccountVerify(psnLOANChangeLoanRepayAccountVerifyParams)
                .compose(this.<PsnLOANChangeLoanRepayAccountVerifyResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnLOANChangeLoanRepayAccountVerifyResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnLOANChangeLoanRepayAccountVerifyResult>() {
                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        //变更循环非循环还款账户预交易，失败调用
                        changeAView.changeAccountOEVerifyFail(errorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnLOANChangeLoanRepayAccountVerifyResult result) {
                        ChangeAccountVerifyRes res = new ChangeAccountVerifyRes();
                        res.set_certDN(result.get_certDN());
                        res.set_plainData(result.get_plainData());
                        res.setFactorList(result.getFactorList());
                        res.setSmcTrigerInterval(result.getSmcTrigerInterval());
                        //变更循环非循环还款账户预交易，成功调用
                        changeAView.changeAccountOEVerifySuccess(res);
                    }
                });
    }

    //变更循环非循环还款账户提交交易       PsnLOANChangeLoanRepayAccountSubmit
    @Override
    public void changeAccountOESubmit() {
        //请求token
        PSNGetTokenIdParams psnGetTokenIdParams=new PSNGetTokenIdParams();
        psnGetTokenIdParams.setConversationId(conversationId);
        globalService.psnGetTokenId(psnGetTokenIdParams)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnLOANChangeLoanRepayAccountSubmitResult>>() {
                    @Override
                    public Observable<PsnLOANChangeLoanRepayAccountSubmitResult> call(String token) {
                        tokenId = token;
                        Log.i("token=------------->",token);
                        PsnLOANChangeLoanRepayAccountSubmitParams psnLOANChangeLoanRepayAccountSubmitParams
                                = buildChangeAccountSubmit(changeAccountSubmitReq);
                        psnLOANChangeLoanRepayAccountSubmitParams.setToken(tokenId);
                        return psnLoanService.psnLOANChangeLoanRepayAccountSubmit(psnLOANChangeLoanRepayAccountSubmitParams);
                    }
                })
                .compose(SchedulersCompat.<PsnLOANChangeLoanRepayAccountSubmitResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnLOANChangeLoanRepayAccountSubmitResult>() {
                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        //变变更中E贷还款账户提交交易，失败调用
                        changeAView.changeAccountOESubmitFail(errorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnLOANChangeLoanRepayAccountSubmitResult psnLOANChangeLoanRepayAccountSubmitResult) {
                        //变变更中E贷还款账户提交交易，成功调用
                        changeAView.changeAccountOESubmitSuccess();
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
                        changeAView.checkAccountDetailFail(errorException);
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
                        changeAView.checkAccountDetailSuccess(result);
                    }
                });
    }
    
//---------------构造上传参数

    //构造变更中E贷还款账户预交易，请求参数
    private PsnLOANChangeLoanRepayAccountVerifyParams buildChangeAccountVerify(ChangeAccountVerifyReq req){
        PsnLOANChangeLoanRepayAccountVerifyParams params
                = new PsnLOANChangeLoanRepayAccountVerifyParams();
        params.setConversationId(conversationId);
        params.set_combinId(req.get_combinId());
        params.setLoanType(req.getLoanType());
        params.setLoanActNum(req.getLoanActNum());
        params.setOldPayAccountNum(req.getOldPayAccountNum());
        params.setOldPayCardNum(req.getOldPayCardNum());
        params.setNewPayAccountNum(req.getNewPayAccountNum());
        params.setNewPayAccountId(req.getNewPayAccountId());
        params.setCurrencyCode(req.getCurrencyCode());
        params.setCashRemit(req.getCashRemit());
        return params;
    }

    //构造变变更中E贷还款账户提交交易，请求参数
    private PsnLOANChangeLoanRepayAccountSubmitParams buildChangeAccountSubmit(ChangeAccountSubmitReq req){
        PsnLOANChangeLoanRepayAccountSubmitParams params = new PsnLOANChangeLoanRepayAccountSubmitParams();

        params.setConversationId(conversationId);
        params.setSmc(req.getSmc());//可选
        params.setOtp(req.getOtp());
        params.setToken(tokenId);
        params.set_signedData(req.get_signedData());
        params.setLoanType(req.getLoanType());
        params.setLoanActNum(req.getLoanActNum());
        params.setOldPayAccountNum(req.getOldPayAccountNum());
        params.setOldPayCardNum(req.getOldPayCardNum());
        params.setNewPayAccountNum(req.getNewPayAccountNum());
        params.setNewPayAccountId(req.getNewPayAccountId());
        params.setCurrencyCode(req.getCurrencyCode());
        params.setCashRemit(req.getCashRemit());
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

}
