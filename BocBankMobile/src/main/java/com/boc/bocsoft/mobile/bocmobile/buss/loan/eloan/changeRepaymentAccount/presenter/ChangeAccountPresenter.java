package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.presenter;

import android.util.Log;

import com.boc.bocma.tools.LogUtil;
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
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeLoanERepayAccountSubmit.PsnLOANChangeLoanERepayAccountSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeLoanERepayAccountSubmit.PsnLOANChangeLoanERepayAccountSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeLoanERepayAccountVerify.PsnLOANChangeLoanERepayAccountVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeLoanERepayAccountVerify.PsnLOANChangeLoanERepayAccountVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayeeAcountCheck.PsnLOANPayeeAcountCheckParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayeeAcountCheck.PsnLOANPayeeAcountCheckResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayerAcountCheck.PsnLOANPayerAcountCheckParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayerAcountCheck.PsnLOANPayerAcountCheckResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepayAccountDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ChangeAccountSubmitReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ChangeAccountVerifyReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ChangeAccountVerifyRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.CollectionAccountCheckReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.CollectionAccountCheckRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.QueryAllChinaBankAccountRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.RepaymentAccountCheckReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.RepaymentAccountCheckRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.ui.ChangeAccountContract;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by xintong on 2016/6/23.
 */
public class ChangeAccountPresenter extends RxPresenter implements ChangeAccountContract.Presenter {
    /**
     * 贷款申请service
     */
    private PsnLoanService psnLoanService;

    private ChangeAccountContract.changeAccountView changeAView;
    private ChangeAccountContract.AccountListView changeAccountView;

    private GlobalService globalService;

    private CollectionAccountCheckReq collectionAccountCheckReq;
    private RepaymentAccountCheckReq repaymentAccountCheckReq;
    private ChangeAccountVerifyReq changeAccountVerifyReq;
    private ChangeAccountSubmitReq changeAccountSubmitReq;
    /**
     * 申请贷款预交易前会话
     */
    private String conversationId;
    private String tokenId;
    /**
     * 是否是中银E贷
     */
    private boolean isBocELoan = true;
    /**
     * 账户类型
     * 获取中行所有帐户列表，请求参数
     */
    private List<String> accountType;
    /**
     * 筛选币种-非中银E贷需要
     */
    private String currencyCode = "";

    public ChangeAccountPresenter(ChangeAccountContract.AccountListView changeAccountView) {
        this.changeAccountView = changeAccountView;
        psnLoanService = new PsnLoanService();
        globalService = new GlobalService();
        changeAccountView.setPresenter(this);
    }

    public ChangeAccountPresenter(ChangeAccountContract.changeAccountView changeAView) {
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


    public void setCollectionAccountCheckReq(CollectionAccountCheckReq collectionAccountCheckReq) {
        this.collectionAccountCheckReq = collectionAccountCheckReq;
    }

    public void setRepaymentAccountCheckReq(RepaymentAccountCheckReq repaymentAccountCheckReq) {
        this.repaymentAccountCheckReq = repaymentAccountCheckReq;
    }

    public void setAccountType(List<String> accountType) {
        this.accountType = accountType;
    }

    /**
     * 设置是否是中银E贷功能进入的
     * @param isBocELoan
     */
    public void setIsBocELoan(boolean isBocELoan) {
        this.isBocELoan = isBocELoan;
    }

    /**
     *
     * @param isBocELoan 设置是否是中银E贷功能进入的
     * @param currencyCode 设置非中银E贷 提前还款功能-币种筛选
     */
    public void setIsBocELoan(boolean isBocELoan,String currencyCode) {
        this.isBocELoan = isBocELoan;
        this.currencyCode = currencyCode;
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

    //    @Override
//    public void requestAmount(final int position, final AccountListItemViewModel item) {
//        Observable.just(item)
//                .delay(3, TimeUnit.SECONDS)
//                .flatMap(
//                        new Func1<AccountListItemViewModel, Observable<List<AccountListItemViewModel.CardAmountViewModel>>>() {
//                            @Override
//                            public Observable<List<AccountListItemViewModel.CardAmountViewModel>> call(
//                                    AccountListItemViewModel acountListItemViewModel) {
//                                List<AccountListItemViewModel.CardAmountViewModel> amountList =
//                                        new ArrayList<AccountListItemViewModel.CardAmountViewModel>();
//                                AccountListItemViewModel.CardAmountViewModel amountModel =
//                                        new AccountListItemViewModel.CardAmountViewModel();
//                                amountModel.setAmount("1000");
//                                amountModel.setCashRemit("01");
//                                amountModel.setCurrencyCode("014");
//                                AccountListItemViewModel.CardAmountViewModel amountModel1 =
//                                        new AccountListItemViewModel.CardAmountViewModel();
//                                amountModel1.setAmount("2000");
//                                amountModel1.setCashRemit("02");
//                                amountModel1.setCurrencyCode("001");
//                                amountList.add(amountModel1);
//                                return Observable.just(amountList);
//                            }
//                        })
//                .compose(
//                        this.<List<AccountListItemViewModel.CardAmountViewModel>>bindToLifecycle())
//                .compose(
//                        SchedulersCompat.<List<AccountListItemViewModel.CardAmountViewModel>>applyIoSchedulers())
//                .subscribe(
//                        new BIIBaseSubscriber<List<AccountListItemViewModel.CardAmountViewModel>>() {
//                            @Override
//                            public void handleException(
//                                    BiiResultErrorException biiResultErrorException) {
//                                changeAccountView.onLoadAmountError(position,
//                                        biiResultErrorException.getMessage());
//                            }
//
//                            @Override
//                            public void onCompleted() {
//
//                            }
//
//                            @Override
//                            public void onNext(
//                                    List<AccountListItemViewModel.CardAmountViewModel> list) {
//                                changeAccountView.onLoadAmountSuccess(position, list);
//                            }
//                        });
//
//    }
    @Override
    public void getSecurityFactor() {
        //获取安全因子
        PsnGetSecurityFactorParams params = new PsnGetSecurityFactorParams();
        params.setConversationId(conversationId);
        params.setServiceId("PB123");
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

    //PsnLOANPayerAcountCheck还款账户检查
    @Override
    public void checkRepaymentAccount() {
        PsnLOANPayerAcountCheckParams psnLOANPayerAcountCheckParams
                = buildRepaymentAccountCheck(repaymentAccountCheckReq);
        psnLoanService.psnLOANPayerAcountCheck(psnLOANPayerAcountCheckParams)
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
                        changeAccountView.doRepaymentAccountCheckSuccess(repaymentAccountCheckRes);
                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        //还款账户检查，失败调用
                        changeAccountView.doRepaymentAccountCheckFail(errorException);
                    }
                });
    }

    /**
     * 查询账户详情
     *
     * @param accountID
     */
    @Override
    public void prepayCheckAccountDetail(String accountID) {
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
                        changeAccountView.prepayCheckAccountDetailFail(errorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnAccountQueryAccountDetailResult result) {
                        PrepayAccountDetailModel.AccountDetaiListBean accountDetaiListBean = new PrepayAccountDetailModel.AccountDetaiListBean();
                        for (PsnAccountQueryAccountDetailResult.AccountDetaiListBean bean : result.getAccountDetaiList()) {
                            if(isBocELoan){
                                if ("001".equals(bean.getCurrencyCode())) {
                                    //币种
                                    accountDetaiListBean.setCurrencyCode(bean.getCurrencyCode());
                                    //钞汇标识
                                    accountDetaiListBean.setCashRemit(bean.getCashRemit());
                                    //当前余额
                                    accountDetaiListBean.setBookBalance(bean.getBookBalance());
                                    //可用余额
                                    accountDetaiListBean.setAvailableBalance(bean.getAvailableBalance());
                                    //存折册号
                                    accountDetaiListBean.setVolumeNumber(bean.getVolumeNumber());
                                    //存单类型
                                    accountDetaiListBean.setType(bean.getType());
                                    //利率
                                    accountDetaiListBean.setInterestRate(bean.getInterestRate());
                                    //子账户状态
                                    accountDetaiListBean.setStatus(bean.getStatus());
                                    //月存、支金额
                                    accountDetaiListBean.setMonthBalance(bean.getMonthBalance());
                                    //存单号
                                    accountDetaiListBean.setCdNumber(bean.getCdNumber());
                                    //存期
                                    accountDetaiListBean.setCdPeriod(bean.getCdPeriod());
                                    //开户日期
                                    accountDetaiListBean.setOpenDate(bean.getOpenDate());
                                    //起息日
                                    accountDetaiListBean.setInterestStartsDate(bean.getInterestStartsDate());
                                    //到期日
                                    accountDetaiListBean.setInterestEndDate(bean.getInterestEndDate());
                                    //结清日期
                                    accountDetaiListBean.setSettlementDate(bean.getSettlementDate());
                                    //自动转存标识
                                    accountDetaiListBean.setConvertType(bean.getConvertType());
                                    //凭证号码
                                    accountDetaiListBean.setPingNo(bean.getPingNo());
                                    //
                                    accountDetaiListBean.setHoldAmount(bean.getHoldAmount());
                                    //约定转存状态
                                    accountDetaiListBean.setAppointStatus(bean.getAppointStatus());
                                }
                            }else{
                                if(currencyCode.equalsIgnoreCase(bean.getCurrencyCode())){
                                    //币种
                                    accountDetaiListBean.setCurrencyCode(bean.getCurrencyCode());
                                    //钞汇标识
                                    accountDetaiListBean.setCashRemit(bean.getCashRemit());
                                    //当前余额
                                    accountDetaiListBean.setBookBalance(bean.getBookBalance());
                                    //可用余额
                                    accountDetaiListBean.setAvailableBalance(bean.getAvailableBalance());
                                    //存折册号
                                    accountDetaiListBean.setVolumeNumber(bean.getVolumeNumber());
                                    //存单类型
                                    accountDetaiListBean.setType(bean.getType());
                                    //利率
                                    accountDetaiListBean.setInterestRate(bean.getInterestRate());
                                    //子账户状态
                                    accountDetaiListBean.setStatus(bean.getStatus());
                                    //月存、支金额
                                    accountDetaiListBean.setMonthBalance(bean.getMonthBalance());
                                    //存单号
                                    accountDetaiListBean.setCdNumber(bean.getCdNumber());
                                    //存期
                                    accountDetaiListBean.setCdPeriod(bean.getCdPeriod());
                                    //开户日期
                                    accountDetaiListBean.setOpenDate(bean.getOpenDate());
                                    //起息日
                                    accountDetaiListBean.setInterestStartsDate(bean.getInterestStartsDate());
                                    //到期日
                                    accountDetaiListBean.setInterestEndDate(bean.getInterestEndDate());
                                    //结清日期
                                    accountDetaiListBean.setSettlementDate(bean.getSettlementDate());
                                    //自动转存标识
                                    accountDetaiListBean.setConvertType(bean.getConvertType());
                                    //凭证号码
                                    accountDetaiListBean.setPingNo(bean.getPingNo());
                                    //
                                    accountDetaiListBean.setHoldAmount(bean.getHoldAmount());
                                    //约定转存状态
                                    accountDetaiListBean.setAppointStatus(bean.getAppointStatus());
                                }

                            }

                        }
                        changeAccountView.prepayCheckAccountDetailSuccess(accountDetaiListBean);
                    }
                });
    }

    //PsnLOANPayeeAcountCheck收款账户检查
    @Override
    public void checkCollectionAccount() {
        PsnLOANPayeeAcountCheckParams psnLOANPayeeAcountCheckParams
                = buildCollectionAccountCheck(collectionAccountCheckReq);
        psnLoanService.psnLOANPayeeAcountCheck(psnLOANPayeeAcountCheckParams)
                .compose(this.<PsnLOANPayeeAcountCheckResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnLOANPayeeAcountCheckResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnLOANPayeeAcountCheckResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnLOANPayeeAcountCheckResult psnLOANPayeeAcountCheckResult) {
                        CollectionAccountCheckRes collectionAccountCheckRes
                                = new CollectionAccountCheckRes();
                        collectionAccountCheckRes.setCheckResult(psnLOANPayeeAcountCheckResult.getCheckResult());
                        //收款账户检查，成功调用
                        changeAccountView.doCollectionAccountCheckSuccess(collectionAccountCheckRes);
                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        //收款账户检查，失败调用
                        changeAccountView.doCollectionAccountCheckFail(errorException);
                    }
                });
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
                .compose(SchedulersCompat.<List<PsnCommonQueryAllChinaBankAccountResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnCommonQueryAllChinaBankAccountResult>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(List<PsnCommonQueryAllChinaBankAccountResult> result) {
                        //获取中行所有帐户列表，成功调用
                        changeAccountView.obtainAllChinaBankAccountSuccess(queryAllChinaBankAccountRes(result));
                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        //获取中行所有帐户列表，失败调用
                        changeAccountView.obtainAllChinaBankAccountFail(errorException);
                    }
                });
    }

    //PsnLOANChangeLoanERepayAccountVerify变更中E贷还款账户预交易
    @Override
    public void changeAccountVerify() {
        PsnLOANChangeLoanERepayAccountVerifyParams psnLOANChangeLoanERepayAccountVerifyParams
                = buildChangeAccountVerify(changeAccountVerifyReq);
        psnLoanService.psnLOANChangeLoanERepayAccountVerify(psnLOANChangeLoanERepayAccountVerifyParams)
                .compose(this.<PsnLOANChangeLoanERepayAccountVerifyResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnLOANChangeLoanERepayAccountVerifyResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnLOANChangeLoanERepayAccountVerifyResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnLOANChangeLoanERepayAccountVerifyResult result) {
                        ChangeAccountVerifyRes res = new ChangeAccountVerifyRes();
                        res.set_certDN(result.get_certDN());
                        res.set_plainData(result.get_plainData());
                        res.setFactorList(result.getFactorList());
                        res.setSmcTrigerInterval(result.getSmcTrigerInterval());
                        //变更中E贷还款账户预交易，成功调用
                        changeAView.changeAccountVerifySuccess(res);
                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        //变更中E贷还款账户预交易，失败调用
                        changeAView.changeAccountVerifyFail(errorException);
                    }
                });
    }

    //PsnLOANChangeLoanERepayAccountSubmit变变更中E贷还款账户提交交易
    @Override
    public void changeAccountSubmit() {
        //请求token
        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
        psnGetTokenIdParams.setConversationId(conversationId);
        globalService.psnGetTokenId(psnGetTokenIdParams)
                //将任务绑定到this上，调用将任务绑定到this.onDestroy即可取消任务
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnLOANChangeLoanERepayAccountSubmitResult>>() {
                    @Override
                    public Observable<PsnLOANChangeLoanERepayAccountSubmitResult> call(String token) {
                        tokenId = token;
                        Log.i("token=------------->", token);
                        PsnLOANChangeLoanERepayAccountSubmitParams psnLOANChangeLoanERepayAccountSubmitParams
                                = buildChangeAccountSubmit(changeAccountSubmitReq);
                        return psnLoanService.psnLOANChangeLoanERepayAccountSubmit(psnLOANChangeLoanERepayAccountSubmitParams);
                    }
                })
                .compose(SchedulersCompat.<PsnLOANChangeLoanERepayAccountSubmitResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnLOANChangeLoanERepayAccountSubmitResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnLOANChangeLoanERepayAccountSubmitResult psnLOANChangeLoanERepayAccountSubmitResult) {
                        //变变更中E贷还款账户提交交易，成功调用
                        changeAView.changeAccountSubmitSuccess();
                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        //变变更中E贷还款账户提交交易，失败调用
                        changeAView.changeAccountSubmitFail(errorException);
                    }
                });
    }

    //---------------构造上传参数
    //构造还款账户检查,请求参数
    private PsnLOANPayerAcountCheckParams buildRepaymentAccountCheck(RepaymentAccountCheckReq req) {
        PsnLOANPayerAcountCheckParams params = new PsnLOANPayerAcountCheckParams();
        params.setConversationId(conversationId);
        params.setAccountId(req.getAccountId());
        LogUtil.d("yx---------currencyCode--->"+req.getCurrencyCode());
        params.setCurrencyCode(req.getCurrencyCode());
        return params;
    }

    //构造收款账户检查,请求参数
    private PsnLOANPayeeAcountCheckParams buildCollectionAccountCheck(CollectionAccountCheckReq req) {
        PsnLOANPayeeAcountCheckParams params = new PsnLOANPayeeAcountCheckParams();
        params.setConversationId(conversationId);
        params.setAccountId(req.getAccountId());
        params.setCurrencyCode(req.getCurrencyCode());
        return params;
    }

    //构造变更中E贷还款账户预交易，请求参数
    private PsnLOANChangeLoanERepayAccountVerifyParams buildChangeAccountVerify(ChangeAccountVerifyReq req) {
        PsnLOANChangeLoanERepayAccountVerifyParams params
                = new PsnLOANChangeLoanERepayAccountVerifyParams();
        params.setConversationId(conversationId);
        params.set_combinId(req.get_combinId());
        params.setQuoteNo(req.getQuoteNo());
        params.setOldPayCardNum(req.getOldPayCardNum());
        params.setNewPayAccountNum(req.getNewPayAccountNum());
        params.setNewPayAccountId(req.getNewPayAccountId());
        params.setLoanType(req.getLoanType());
        params.setCurrency(req.getCurrency());
        return params;
    }

    //构造变变更中E贷还款账户提交交易，请求参数
    private PsnLOANChangeLoanERepayAccountSubmitParams buildChangeAccountSubmit(ChangeAccountSubmitReq req) {
        PsnLOANChangeLoanERepayAccountSubmitParams params = new PsnLOANChangeLoanERepayAccountSubmitParams();

        params.setConversationId(conversationId);

        Log.i("presenter", "conversationId1=" + conversationId);
        params.setConversationId(conversationId);

        if ("8".equals(req.getFactorId())) {
            //动态口令
            params.setOtp(req.getOtp());
            params.setOtp_RC(req.getOtp_RC());

        } else if ("32".equals(req.getFactorId())) {
            //短信验证码
            params.setSmc(req.getSmc());
            params.setSmc_RC(req.getSmc_RC());

        } else if ("40".equals(req.getFactorId())) {
            //动态口令+短信验证码
            params.setSmc(req.getSmc());
            params.setSmc_RC(req.getSmc_RC());
            params.setOtp(req.getOtp());
            params.setOtp_RC(req.getOtp_RC());

        } else if ("96".equals(req.getFactorId())) {
            //短信验证码+硬件绑定
            params.setSmc(req.getSmc());
            params.setSmc_RC(req.getSmc_RC());

            params.setDeviceInfo(req.getDeviceInfo());
            params.setDeviceInfo_RC(req.getDeviceInfo_RC());
            params.setDevicePrint(req.getDevicePrint());
        } else if ("4".equals(req.getFactorId())) {
            params.set_signedData(req.get_signedData());
        }

//        params.setSmc(req.getSmc());
//        params.setSmc_RC(req.getSmc_RC());
//        params.setOtp(req.getOtp());
//        params.setSmc_RC(req.getSmc_RC());

//        params.setDeviceInfo(req.getDeviceInfo());
//        params.setDeviceInfo_RC(req.getDeviceInfo_RC());
//        params.setDevicePrint(req.getDevicePrint());

        //cfn版本号和状态
        params.setActiv(SecurityVerity.getInstance().getCfcaVersion());
        params.setState(SecurityVerity.SECURITY_VERIFY_STATE);

        params.setToken(tokenId);
        params.setQuoteNo(req.getQuoteNo());
        params.setOldPayCardNum(req.getOldPayCardNum());
        params.setNewPayAccountNum(req.getNewPayAccountNum());
        params.setNewPayAccountId(req.getNewPayAccountId());
        params.setLoanType(req.getLoanType());
        params.setCurrency(req.getCurrency());
        return params;

    }

    //---------------构造返回参数
    private List<QueryAllChinaBankAccountRes> queryAllChinaBankAccountRes(List<PsnCommonQueryAllChinaBankAccountResult> result) {
        List<QueryAllChinaBankAccountRes> list
                = new ArrayList<QueryAllChinaBankAccountRes>();
        for (int i = 0; i < result.size(); i++) {
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
