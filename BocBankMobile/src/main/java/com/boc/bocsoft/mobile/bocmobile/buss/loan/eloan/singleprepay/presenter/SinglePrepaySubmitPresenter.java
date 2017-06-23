package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQueryAllChinaBankAccount.PsnCommonQueryAllChinaBankAccountParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQueryAllChinaBankAccount.PsnCommonQueryAllChinaBankAccountResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANRepayCount.PsnELOANRepayCountParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANRepayCount.PsnELOANRepayCountResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANSingleRepaySubmit.PsnELOANSingleRepaySubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANSingleRepaySubmit.PsnELOANSingleRepaySubmitResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayerAcountCheck.PsnLOANPayerAcountCheckParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayerAcountCheck.PsnLOANPayerAcountCheckResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepayAccountDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.QueryAllChinaBankAccountRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.RepaymentAccountCheckRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.model.SinglePrepaySubmitReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.model.SinglePrepaySubmitRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.ui.SinglePrepaySubmitContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 贷款管理-中银E贷-单笔交易提前还款
 * Created by liuzc on 2016/9/2.
 */
public class SinglePrepaySubmitPresenter extends RxPresenter implements SinglePrepaySubmitContract.Presenter{

    private SinglePrepaySubmitContract.View mContractView;
    private PsnLoanService mLoanService;
    private GlobalService globalService;

    public SinglePrepaySubmitPresenter(SinglePrepaySubmitContract.View view){
        mContractView = view;
        mContractView.setPresenter(this);

        mLoanService = new PsnLoanService();
        globalService = new GlobalService();
    }

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
                        mContractView.obtainConversationSuccess(s);
                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        mContractView.obtainConversationFail(errorException);
                    }
                });
    }

    /**
     * 获取中行所有帐户列表
     *
     * @param mListDataAccountType
     *            账户类型
     */
    @Override
    public void queryAllChinaBankAccount(List<String> mListDataAccountType) {
        PsnCommonQueryAllChinaBankAccountParams psnCommonQueryAllChinaBankAccountParams = new PsnCommonQueryAllChinaBankAccountParams();
        psnCommonQueryAllChinaBankAccountParams
                .setAccountType(mListDataAccountType);
        globalService
                .psnCommonQueryAllChinaBankAccount(psnCommonQueryAllChinaBankAccountParams)
                .compose(
                        this.<List<PsnCommonQueryAllChinaBankAccountResult>> bindToLifecycle())
                .compose(
                        SchedulersCompat
                                .<List<PsnCommonQueryAllChinaBankAccountResult>> applyIoSchedulers())
                .subscribe(
                        new BIIBaseSubscriber<List<PsnCommonQueryAllChinaBankAccountResult>>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onNext(
                                    List<PsnCommonQueryAllChinaBankAccountResult> result) {
                                // 获取中行所有帐户列表，成功调用
                                mContractView.obtainAllChinaBankAccountSuccess(queryAllChinaBankAccountRes(result));
                            }

                            @Override
                            public void handleException(
                                    BiiResultErrorException e) {
                                ErrorException errorException = new ErrorException();
                                errorException.setErrorCode(e.getErrorCode());
                                errorException.setErrorMessage(e.getErrorMessage());
                                errorException.setErrorType(e.getErrorType());
                                // 获取中行所有帐户列表，失败调用
                                mContractView
                                        .obtainAllChinaBankAccountFail(errorException);
                            }
                        });
    }

    @Override
    public void prepayCheckAccountDetail(String accountID) {
//        PrepayAccountDetailModel.AccountDetaiListBean accountDetaiListBean=new PrepayAccountDetailModel.AccountDetaiListBean();
//        accountDetaiListBean.setAvailableBalance(new BigDecimal(20000));
//        mContractView.prepayCheckAccountDetailSuccess(accountDetaiListBean);

        PsnAccountQueryAccountDetailParams params = new PsnAccountQueryAccountDetailParams();
        params.setAccountId(accountID);
        mLoanService.psnAccountQueryAccountDetail(params)
                .compose(this.<PsnAccountQueryAccountDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnAccountQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnAccountQueryAccountDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorCode(e.getErrorMessage());
                        errorException.setErrorCode(e.getErrorType());
                        mContractView.prepayCheckAccountDetailFail(errorException);
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
                        mContractView.prepayCheckAccountDetailSuccess(accountDetaiListBean);
                    }
                });
    }

    @Override
    public void calcCharges(PsnELOANRepayCountParams params) {

//        PsnELOANRepayCountResult result = new PsnELOANRepayCountResult();
//        result.setCharges("200");
//        mContractView.calcChargesSuccess(result);

        mLoanService.psnELOANRepayCount(params)
                .compose(this.<PsnELOANRepayCountResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnELOANRepayCountResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnELOANRepayCountResult>() {
                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorCode(e.getErrorMessage());
                        errorException.setErrorCode(e.getErrorType());
                        mContractView.calcChargesFail(errorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnELOANRepayCountResult result) {
                        mContractView.calcChargesSuccess(result);
                    }
                });
    }

    @Override
    public void checkRepaymentAccount(PsnLOANPayerAcountCheckParams psnLOANPayerAcountCheckParams) {
        mLoanService.psnLOANPayerAcountCheck(psnLOANPayerAcountCheckParams)
                .compose(this.<PsnLOANPayerAcountCheckResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnLOANPayerAcountCheckResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnLOANPayerAcountCheckResult>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnLOANPayerAcountCheckResult psnLOANPayerAcountCheckResult) {
                        RepaymentAccountCheckRes repaymentAccountCheckRes
                                = new RepaymentAccountCheckRes();
                        repaymentAccountCheckRes.setCheckResult(psnLOANPayerAcountCheckResult.getCheckResult());
                        //还款账户检查，成功调用
                        mContractView.doRepaymentAccountCheckSuccess(repaymentAccountCheckRes);
                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        //还款账户检查，失败调用
                        mContractView.doRepaymentAccountCheckFail(errorException);
                    }
                });
    }

    // ---------------构造返回参数
    /**
     * 账户列表 119 返回结果数据
     *
     * @param result
     * @return
     */
    private List<QueryAllChinaBankAccountRes> queryAllChinaBankAccountRes(
            List<PsnCommonQueryAllChinaBankAccountResult> result) {
        List<QueryAllChinaBankAccountRes> list = new ArrayList<QueryAllChinaBankAccountRes>();
        for (int i = 0; i < result.size(); i++) {
            QueryAllChinaBankAccountRes queryAllChinaBankAccountRes = new QueryAllChinaBankAccountRes();
            queryAllChinaBankAccountRes.setAccountId(result.get(i)
                    .getAccountId());
            queryAllChinaBankAccountRes.setAccountName(result.get(i)
                    .getAccountName());
            queryAllChinaBankAccountRes.setIsECashAccount(result.get(i)
                    .getIsECashAccount());
            queryAllChinaBankAccountRes.setIsMedicalAccount(result.get(i)
                    .getIsMedicalAccount());
            queryAllChinaBankAccountRes.setAccountNumber(result.get(i)
                    .getAccountNumber());
            queryAllChinaBankAccountRes.setAccountIbkNum(result.get(i)
                    .getAccountIbkNum());
            queryAllChinaBankAccountRes.setAccountType(result.get(i)
                    .getAccountType());
            queryAllChinaBankAccountRes
                    .setBranchId(result.get(i).getBranchId());
            queryAllChinaBankAccountRes
                    .setNickName(result.get(i).getNickName());
            queryAllChinaBankAccountRes.setAccountStatus(result.get(i)
                    .getAccountStatus());
            queryAllChinaBankAccountRes.setCustomerId(result.get(i)
                    .getCustomerId());
            queryAllChinaBankAccountRes.setCurrencyCode(result.get(i)
                    .getCurrencyCode());
            queryAllChinaBankAccountRes.setCurrencyCode2(result.get(i)
                    .getCurrencyCode2());
            queryAllChinaBankAccountRes.setBranchName(result.get(i)
                    .getBranchName());
            queryAllChinaBankAccountRes.setCardDescription(result.get(i)
                    .getCardDescription());
            queryAllChinaBankAccountRes.setHasOldAccountFlag(result.get(i)
                    .getHasOldAccountFlag());
            queryAllChinaBankAccountRes.setCardDescriptionCode(result.get(i)
                    .getCardDescriptionCode());
            queryAllChinaBankAccountRes.setVerifyFactor(result.get(i)
                    .getVerifyFactor());
            queryAllChinaBankAccountRes.setEcard(result.get(i).getEcard());
            list.add(queryAllChinaBankAccountRes);
        }

        return list;
    }
}
