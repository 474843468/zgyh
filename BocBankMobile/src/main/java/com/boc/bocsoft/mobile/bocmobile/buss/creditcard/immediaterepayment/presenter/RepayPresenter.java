package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.presenter;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdChargeOnRMBAccountQuery.PsnCrcdChargeOnRMBAccountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdChargeOnRMBAccountQuery.PsnCrcdChargeOnRMBAccountQueryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCheckPaymentInfo.PsnCrcdCheckPaymentInfoParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCheckPaymentInfo.PsnCrcdCheckPaymentInfoResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdForeignPayOff.PsnCrcdForeignPayOffParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdForeignPayOff.PsnCrcdForeignPayOffResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdForeignPayOffFee.PsnCrcdForeignPayOffFeeParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdForeignPayOffFee.PsnCrcdForeignPayOffFeeResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdForeignPayQuery.PsnCrcdForeignPayQueryParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdForeignPayQuery.PsnCrcdForeignPayQueryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryCrcdPaymentWay.PsnCrcdQueryCrcdPaymentWayParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryCrcdPaymentWay.PsnCrcdQueryCrcdPaymentWayResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryForeignPayOff.PsnCrcdQueryForeignPayOffParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryForeignPayOff.PsnCrcdQueryForeignPayOffResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQuerySettingsInfo.PsnCrcdQuerySettingsInfoParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQuerySettingsInfo.PsnCrcdQuerySettingsInfoResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdTransferPayOff.PsnCrcdTransferPayOffParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdTransferPayOff.PsnCrcdTransferPayOffResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnQueryCrcdRTBill.PsnQueryCrcdRTBillParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnQueryCrcdRTBill.PsnQueryCrcdRTBillResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.service.CrcdService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetBocTransferCommissionCharge.PsnTransGetBocTransferCommissionChargeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetBocTransferCommissionCharge.PsnTransGetBocTransferCommissionChargeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.model.AccountInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.model.CrcdAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.model.CrcdBillInfoBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.model.RepaymentInfoBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.model.TransCommissionChargeBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.ui.RepaymentMainFragment;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * 作者：xwg on 16/11/23 15:50
 * 立即还款
 */
public class RepayPresenter extends BaseCrcdPresenter implements RepaymentContract.Presenter{

    private  AccountService accountService;
    private  TransferService transferService;
    private RepaymentContract.RepaymentView repaymentView;
    private RepaymentContract.RepaymentMainView repaymentMainView;
    private RepaymentContract.RepaymentConfirmView repaymentConfirmView;
    private RepaymentContract.ForeignRepaymentView foreignRepaymentView;
    private CrcdService crcdService;

    public RepayPresenter(RepaymentContract.RepaymentMainView repaymentMainView) {
        this.repaymentMainView = repaymentMainView;
        crcdService=new CrcdService();
    }
    public RepayPresenter(RepaymentContract.RepaymentView repaymentView) {
        this.repaymentView = repaymentView;
        crcdService=new CrcdService();
        transferService=new TransferService();
        accountService =new AccountService();
    }
    public RepayPresenter(RepaymentContract.ForeignRepaymentView repaymentView) {
        this.foreignRepaymentView = repaymentView;
        crcdService=new CrcdService();
        transferService=new TransferService();
        accountService =new AccountService();
    }
    public RepayPresenter(RepaymentContract.RepaymentConfirmView repaymentConfirmView) {
        this.repaymentConfirmView = repaymentConfirmView;
        crcdService=new CrcdService();
        transferService=new TransferService();
        accountService =new AccountService();
    }


    /**
     *  查询信用卡实时账单
     */
    @Override
    public void queryCrcdRTBill(String accounId) {
        PsnQueryCrcdRTBillParams params=new PsnQueryCrcdRTBillParams();
        params.setAccountId(accounId);
        crcdService.psnQueryCrcdRTBill(params)
                .compose(this.<PsnQueryCrcdRTBillResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnQueryCrcdRTBillResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnQueryCrcdRTBillResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnQueryCrcdRTBillResult psnQueryCrcdRTBillResults) {
                            List<CrcdBillInfoBean> beanList=new ArrayList<CrcdBillInfoBean>();
                            for(PsnQueryCrcdRTBillResult.BillInfo result:psnQueryCrcdRTBillResults.getCrcdRTBillList()){
                                CrcdBillInfoBean bean=new CrcdBillInfoBean();
                                BeanConvertor.toBean(result,bean);
                                beanList.add(bean);
                            }
                            if (repaymentMainView!=null)
                                repaymentMainView.queryCrcdRTBillSuccess(beanList);
                            if (repaymentConfirmView!=null)
                                repaymentConfirmView.queryCrcdRTBillSuccess(beanList);
                    }
                });
    }

    /**
     *  全球交易人民币记账功能查询
     *  如果非单外币信用卡，且开通了2个账户的信用卡
     *  只有双币信用卡才能调用此接口进行查询
     *  如果该信用卡开通该功能，只能进行人民币账单还款，不能进行外币账单还款；
     */
    @Override
    public void crcdChargeOnRMBAccountQuery(String accountId) {

        PsnCrcdChargeOnRMBAccountQueryParams params=new PsnCrcdChargeOnRMBAccountQueryParams();
        params.setAccountId(accountId);
        crcdService.psnCrcdChargeOnRMBAccountQuery(params)
                .compose(this.<PsnCrcdChargeOnRMBAccountQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdChargeOnRMBAccountQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdChargeOnRMBAccountQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdChargeOnRMBAccountQueryResult result) {

                        repaymentMainView.crcdChargeOnRMBAccountQuery("true".equals(result.getOpenFlag()),result.getDisplayNum());
                    }
                });

    }


    /**
     *  查询还款方式
     */
    @Override
    public void queryCrcdPayway(String accounId) {
        PsnCrcdQueryCrcdPaymentWayParams params = new PsnCrcdQueryCrcdPaymentWayParams();
        params.setAccountId(accounId);
        crcdService.psnCrcdQueryCrcdPaymentWay(params)
                .compose(this.<PsnCrcdQueryCrcdPaymentWayResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdQueryCrcdPaymentWayResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdQueryCrcdPaymentWayResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdQueryCrcdPaymentWayResult result) {
                        repaymentMainView.queryCrcdPayway(result.getLocalCurrencyPayment(),result.getForeignCurrencyPayment());
                    }
                });

    }


    /**
     * 查询信用卡账户详情
     * @param accountBean
     */
    @Override
    public void queryCrcdAccountDetail(AccountBean accountBean) {

        PsnCrcdQueryAccountDetailParams params = new PsnCrcdQueryAccountDetailParams();
        params.setAccountId(accountBean.getAccountId());
        crcdService.psnCrcdQueryAccountDetailResult(params)
                .compose(this.<PsnCrcdQueryAccountDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdQueryAccountDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnCrcdQueryAccountDetailResult psnCrcdQueryAccountDetailResult) {
                        if (repaymentView!=null)
                            repaymentView.queryCrcdAccountDetails(resultModelToViewModel(psnCrcdQueryAccountDetailResult));
                        else if (foreignRepaymentView!=null)
                            foreignRepaymentView.queryCrcdAccountDetails(resultModelToViewModel(psnCrcdQueryAccountDetailResult));

                    }
                });

    }

    /**
     * 请求普通账户列表详情
     *
     * @param accountBean
     */
    public void queryNormalAccountDetail(final AccountBean accountBean) {
        accountService.psnAccountQueryAccountDetail(new PsnAccountQueryAccountDetailParams(accountBean.getAccountId()))
                .compose(this.<PsnAccountQueryAccountDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnAccountQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnAccountQueryAccountDetailResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnAccountQueryAccountDetailResult psnAccountQueryAccountDetailResult) {
                        if (repaymentView!=null)
                            repaymentView.queryNormalAccountDetails(resultModelToViewModel(psnAccountQueryAccountDetailResult));
                        else if (foreignRepaymentView!=null)
                            foreignRepaymentView.queryNormalAccountDetails(resultModelToViewModel(psnAccountQueryAccountDetailResult));
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                    }
                });

    }



    /**
     *   信用卡还款信息校验
     */
    @Override
    public void checkPaymentInfo(final RepaymentInfoBean repaymentInfoBean) {
        getConversation().flatMap(new Func1<String, Observable<PsnCrcdCheckPaymentInfoResult>>() {
            @Override
            public Observable<PsnCrcdCheckPaymentInfoResult> call(String conversation) {
                setConversationId(conversation);
                PsnCrcdCheckPaymentInfoParams params=new PsnCrcdCheckPaymentInfoParams();
                params.setAccountId(repaymentInfoBean.getToAccountId());
                params.setCurrency(repaymentInfoBean.getCurrency());
                params.setConversationId(getConversationId());
                return crcdService.psnCrcdCheckPaymentInfo(params);
            }
        })
                .compose(this.<PsnCrcdCheckPaymentInfoResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdCheckPaymentInfoResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdCheckPaymentInfoResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdCheckPaymentInfoResult result) {
                        if (repaymentView!=null)
                            repaymentView.checkPaymentInfoSucc(result.getCrcdInvalidDate(),result.getCrcdOverdueFlag(),result.getCrcdState(),repaymentInfoBean);
                        if (foreignRepaymentView!=null)
                            foreignRepaymentView.checkPaymentInfoSucc(result.getCrcdInvalidDate(),result.getCrcdOverdueFlag(),result.getCrcdState(),repaymentInfoBean);
                    }
                });

    }

    @Override
    public void getBocTransCommCharge(final RepaymentInfoBean repaymentInfoBean) {
        PsnTransGetBocTransferCommissionChargeParams params = new PsnTransGetBocTransferCommissionChargeParams();
        params.setConversationId(getConversationId());
        params.setServiceId(ApplicationConst.PB021);
        params.setCurrency(repaymentInfoBean.getCurrency());
        params.setAmount(repaymentInfoBean.getAmount());
        params.setFromAccountId(repaymentInfoBean.getFromAccountId());
        params.setToAccountId(repaymentInfoBean.getToAccountId());
        params.setCashRemit(repaymentInfoBean.getCashRemit());

        transferService.psnTransGetBocTransferCommissionCharge(params)
                .compose(this.<PsnTransGetBocTransferCommissionChargeResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnTransGetBocTransferCommissionChargeResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransGetBocTransferCommissionChargeResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnTransGetBocTransferCommissionChargeResult result) {
                        if (repaymentView!=null)
                            repaymentView.transferCommissionChargeResult(BeanConvertor.toBean(result,new TransCommissionChargeBean()));
                        else if (foreignRepaymentView!=null)
                            foreignRepaymentView.transferCommissionChargeResult(BeanConvertor.toBean(result,new TransCommissionChargeBean()));
                    }
                });
    }



    @Override
    public void crcdForeignPayoffFee(final RepaymentInfoBean repaymentInfoBean) {
        PsnCrcdForeignPayOffFeeParams params=new PsnCrcdForeignPayOffFeeParams();
        params.setConversationId(getConversationId());
        params.setAmount(repaymentInfoBean.getAmount());
        params.setRmbAccId(repaymentInfoBean.getFromAccountId());
        params.setCrcdId(repaymentInfoBean.getToAccountId());
        params.setCrcdAutoRepayMode(RepaymentMainFragment.PAY_WAY_FOREIGN_RMB_FULL==repaymentInfoBean.getPayway()?"FULL":"MINP");
        params.setCrcdAcctNo(repaymentInfoBean.getToAccountNo());
        params.setCrcdAcctName(repaymentInfoBean.getToName());
        crcdService.psnCrcdForeignPayOffFee(params)

                .compose(this.<PsnCrcdForeignPayOffFeeResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdForeignPayOffFeeResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdForeignPayOffFeeResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdForeignPayOffFeeResult result) {
                        foreignRepaymentView.transferCommissionChargeResult(BeanConvertor.toBean(result,new TransCommissionChargeBean()));
                    }
                });

    }

    /**
     *  购汇还款 提交交易
     */
    @Override
    public void crcdForeignPayOff(final RepaymentInfoBean repaymentInfoBean) {
        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
        params.setConversationId(repaymentInfoBean.getConversationId());
        setConversationId(repaymentInfoBean.getConversationId());
        getGlobalService().psnGetTokenId(params)
                .flatMap(new Func1<String, Observable<PsnCrcdForeignPayOffResult >>() {
                    @Override
                    public Observable<PsnCrcdForeignPayOffResult> call(String token) {
                        PsnCrcdForeignPayOffParams params=new PsnCrcdForeignPayOffParams();
                        params.setToken(token);
                        params.setConversationId(getConversationId());
                        params.setRmbAccId(repaymentInfoBean.getFromAccountId());
                        params.setCrcdId(repaymentInfoBean.getToAccountId());
                        params.setCrcdAutoRepayMode(RepaymentMainFragment.PAY_WAY_FOREIGN_RMB_FULL==repaymentInfoBean.getPayway()?"FULL":"MINP");
                        params.setCrcdAcctNo(repaymentInfoBean.getToAccountNo());
                        params.setCrcdAcctName(repaymentInfoBean.getToName());
                        params.setAmount(repaymentInfoBean.getAmount());
                        params.setDevicePrint(repaymentInfoBean.getDevicePrint());
                        return crcdService.psnCrcdForeignPayOff(params);
                    }
                }).flatMap(new Func1<PsnCrcdForeignPayOffResult, Observable<PsnCrcdQuerySettingsInfoResult>>() {
            @Override
            public Observable<PsnCrcdQuerySettingsInfoResult> call(PsnCrcdForeignPayOffResult result) {
                repaymentInfoBean.setTranFee(MoneyUtils.transMoneyFormat(result.getFinalCommissionCharge(),ApplicationConst.CURRENCY_CNY));
                repaymentInfoBean.setTransactionId(result.getTransactionId());
                repaymentInfoBean.setExchangeRate(result.getExchangeRate());

                PsnCrcdQuerySettingsInfoParams params=new PsnCrcdQuerySettingsInfoParams();
                params.setAccountId(repaymentInfoBean.getToAccountId());
                params.setConversationId(getConversationId());
                return crcdService.psnCrcdQuerySettingsInfo(params);
            }
        })
                .compose(this.<PsnCrcdQuerySettingsInfoResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdQuerySettingsInfoResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdQuerySettingsInfoResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        if (!StringUtil.isNullOrEmpty(repaymentInfoBean.getTransactionId())&&!StringUtil.isNullOrEmpty(repaymentInfoBean.getTranFee()))
                            repaymentConfirmView.getTransPayoffResult(repaymentInfoBean);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdQuerySettingsInfoResult result) {
                        repaymentInfoBean.setForeignPayOffStatus(result.getForeignPayOffStatus());
                        repaymentInfoBean.setChargeFlag(result.getChargeFlag());
                        repaymentConfirmView.getTransPayoffResult(repaymentInfoBean);
                        clearConversation();
                    }

                });


    }

    /**
     *  非购汇还款提交交易
     */
    @Override
    public void transferPayoffResult(final RepaymentInfoBean repaymentInfoBean) {
        getToken().flatMap(new Func1<String, Observable<PsnCrcdTransferPayOffResult>>() {
            @Override
            public Observable<PsnCrcdTransferPayOffResult> call(String tokenId) {
                PsnCrcdTransferPayOffParams params=new PsnCrcdTransferPayOffParams();
                params.setAmount(repaymentInfoBean.getAmount());
                params.setToName(repaymentInfoBean.getToName());
                params.setToken(tokenId);
                params.setToAccountId(repaymentInfoBean.getToAccountId());
                params.setFromAccountId(repaymentInfoBean.getFromAccountId());
                params.setDevicePrint(repaymentInfoBean.getDevicePrint());
                params.setCurrency(repaymentInfoBean.getCurrency());
                params.setConversationId(getConversationId());
                params.setCashRemit(repaymentInfoBean.getCashRemit());
                return crcdService.psnCrcdTransferPayOff(params);
            }
        }).flatMap(new Func1<PsnCrcdTransferPayOffResult, Observable<PsnCrcdQuerySettingsInfoResult>>() {
            @Override
            public Observable<PsnCrcdQuerySettingsInfoResult> call(PsnCrcdTransferPayOffResult result) {
                repaymentInfoBean.setTranFee(result.getTranFee());
                repaymentInfoBean.setAccountName(result.getAccountName());
                repaymentInfoBean.setCardDate(result.getCardDate());
                repaymentInfoBean.setTransactionId(result.getTransactionId()+"");
                repaymentInfoBean.setNickName(result.getNickName());
//                repaymentConfirmView.getTransPayoffResult(repaymentInfoBean);
                PsnCrcdQuerySettingsInfoParams params=new PsnCrcdQuerySettingsInfoParams();
                params.setAccountId(repaymentInfoBean.getToAccountId());
                params.setConversationId(getConversationId());
                return crcdService.psnCrcdQuerySettingsInfo(params);
            }
        })
                .compose(this.<PsnCrcdQuerySettingsInfoResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdQuerySettingsInfoResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdQuerySettingsInfoResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        if (!StringUtil.isNullOrEmpty(repaymentInfoBean.getTransactionId())&&!StringUtil.isNullOrEmpty(repaymentInfoBean.getTranFee()))
                            repaymentConfirmView.getTransPayoffResult(repaymentInfoBean);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdQuerySettingsInfoResult result) {
                        repaymentInfoBean.setForeignPayOffStatus(result.getForeignPayOffStatus());
                        repaymentInfoBean.setChargeFlag(result.getChargeFlag());
                        repaymentConfirmView.getTransPayoffResult(repaymentInfoBean);
                        clearConversation();
                    }

                });


    }

    /**
     *  信用卡查询购汇还款信息
     */
    @Override
    public void crcdQueryForeignPayOff(final String accountId, final String currType) {
        getConversation().flatMap(new Func1<String, Observable<PsnCrcdQueryForeignPayOffResult>>() {
            @Override
            public Observable<PsnCrcdQueryForeignPayOffResult> call(String conversation) {
                setConversationId(conversation);
                PsnCrcdQueryForeignPayOffParams params=new PsnCrcdQueryForeignPayOffParams();
                params.setAccountId(accountId);
                params.setCurrType(currType);
                params.setConversationId(conversation);
                return crcdService.psnCrcdQueryForeignPayOff(params);
            }
        })
                .compose(this.<PsnCrcdQueryForeignPayOffResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdQueryForeignPayOffResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdQueryForeignPayOffResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdQueryForeignPayOffResult result) {
                        foreignRepaymentView.crcdQueryForeignPayOffResult(result.getPayedAmt(),result.getExchangeRate());
                    }
                });
    }


    /**
     *  查询支持购汇还款信用卡列表
     */
    @Override
    public void crcdForeignPayQuery(String accountNumber) {
        PsnCrcdForeignPayQueryParams params=new PsnCrcdForeignPayQueryParams();
        params.setAccountNumber(accountNumber);
        crcdService.psnCrcdForeignPayQuery(params)
                .compose(this.<List<PsnCrcdForeignPayQueryResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnCrcdForeignPayQueryResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnCrcdForeignPayQueryResult>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<PsnCrcdForeignPayQueryResult> psnCrcdForeignPayQueryResultList) {
                        List<String> accountNoList=new ArrayList<String>();
                        for (PsnCrcdForeignPayQueryResult result:psnCrcdForeignPayQueryResultList){
                            accountNoList.add(result.getAccountNumber());
                        }
                        foreignRepaymentView.crcdForeignPayQuery(accountNoList);
                    }
                });
    }



    /**
     * 账户详情数据复制到ViewModel中
     * @param result
     * @return
     */
    private List<AccountInfoViewModel> resultModelToViewModel(PsnAccountQueryAccountDetailResult result){
        List<AccountInfoViewModel> viewModelList=new ArrayList<AccountInfoViewModel>();
        for (PsnAccountQueryAccountDetailResult.AccountDetaiListBean item : result.getAccountDetaiList()) {
            AccountInfoViewModel viewModel = new AccountInfoViewModel();
            viewModel.setCurrencyCode(item.getCurrencyCode());// 币种
            viewModel.setAvailableBalance(item.getAvailableBalance());// 可用余额
            viewModel.setCashRemit(item.getCashRemit());
            viewModelList.add(viewModel);
        }
        return viewModelList;
    }
    /**
     * 信用卡账户详情数据复制到ViewModel中
     * @param result
     * @return
     */
    private List<CrcdAccountViewModel> resultModelToViewModel(PsnCrcdQueryAccountDetailResult result){
        List<CrcdAccountViewModel> viewModelList=new ArrayList<CrcdAccountViewModel>();
        for (PsnCrcdQueryAccountDetailResult.CrcdAccountDetailListBean item : result.getCrcdAccountDetailList()) {
            CrcdAccountViewModel viewModel = new CrcdAccountViewModel();
            viewModel.setCurrency(item.getCurrency());// 币种
            viewModel.setCurrentBalance(item.getCurrentBalance());
            viewModel.setCurrentBalanceflag(item.getCurrentBalanceflag());
            viewModel.setLoanBalanceLimit(item.getLoanBalanceLimit());
            viewModel.setLoanBalanceLimitFlag(item.getLoanBalanceLimitFlag());
            viewModel.setCashRemit("01");//信用卡只有抄 没有汇
            viewModelList.add(viewModel);
        }
        return viewModelList;
    }
}
