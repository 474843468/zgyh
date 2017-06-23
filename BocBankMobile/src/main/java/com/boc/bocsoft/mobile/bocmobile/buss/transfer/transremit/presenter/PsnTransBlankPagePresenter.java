package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.activitymanagementpaltform.model.PsnQueryTransActivityStatus.PsnQueryTransActivityStatusParams;
import com.boc.bocsoft.mobile.bii.bus.activitymanagementpaltform.model.PsnQueryTransActivityStatus.PsnQueryTransActivityStatusResult;
import com.boc.bocsoft.mobile.bii.bus.activitymanagementpaltform.service.ActivityManagementPaltformService;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnCardQueryBindInfo.PsnCardQueryBindInfoParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnCardQueryBindInfo.PsnCardQueryBindInfoResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocNationalTransferVerify.PsnDirTransBocNationalTransferVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocNationalTransferVerify.PsnDirTransBocNationalTransferVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocTransferVerify.PsnDirTransBocTransferVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocTransferVerify.PsnDirTransBocTransferVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankTransfer.PsnDirTransCrossBankTransferParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankTransfer.PsnDirTransCrossBankTransferResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentConfirm.PsnEbpsRealTimePaymentConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentConfirm.PsnEbpsRealTimePaymentConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnOFAAccountStateQuery.PsnOFAAccountStateQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnOFAAccountStateQuery.PsnOFAAccountStateQueryResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryBankInfobyCardBin.PsnQueryBankInfobyCardBinParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryBankInfobyCardBin.PsnQueryBankInfobyCardBinResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryRecentPayeeInfo.PsnQueryRecentPayeeInfoBean;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryRecentPayeeInfo.PsnQueryRecentPayeeInfoParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocNationalTransferVerify.PsnTransBocNationalTransferVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocNationalTransferVerify.PsnTransBocNationalTransferVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferVerify.PsnTransBocTransferVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferVerify.PsnTransBocTransferVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetBocTransferCommissionCharge.PsnTransGetBocTransferCommissionChargeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetBocTransferCommissionCharge.PsnTransGetBocTransferCommissionChargeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetNationalTransferCommissionCharge.PsnTransGetNationalTransferCommissionChargeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetNationalTransferCommissionCharge.PsnTransGetNationalTransferCommissionChargeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPayeeListqueryForDim.PsnTransPayeeListqueryForDimParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPayeeListqueryForDim.PsnTransPayeeListqueryForDimResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQuotaQuery.PsnTransQuotaQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQuotaQuery.PsnTransQuotaQueryResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransPayeeListqueryForDimViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.AccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.CardQueryBindInfoResult;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.CrcdQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.OFAAccountStateQueryResult;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.ui.TransContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by wangyuan on 2016/6/13.
 * 委托填写页面的Presenter
 */
public class PsnTransBlankPagePresenter extends RxPresenter implements TransContract.TransPresenterBlankPage{

    /**
     * 转账汇款service
     */
    private TransferService transService;
    private TransContract.TransViewBlankPage mTransBlankView;//转账填写页面
    private AccountService accountService;
    /**
     * 公用service
     */
    private GlobalService globalService;

    private String conversationId;
    private PsnGetSecurityFactorResult securityFactorResult;
    public PsnTransBlankPagePresenter(TransContract.TransViewBlankPage blankView){

          mTransBlankView=blankView;
//          mTransConfirmView=confirmView;
          mTransBlankView.setPresenter(this);
//          mTransConfirmView.setPresenter(this);
          transService=new TransferService();
          globalService=new GlobalService();
         accountService=new AccountService();
//        psnSettingService = new PsnSettingService();
//        globalService = new GlobalService();
    }

    //仅供测试，可以删除
    /**
     * 判断是否满足抽奖资格，并返回票信息
     * @param transactionId 交易码
     */
    @Override
    public void queryTransActivityStatusTest(String transactionId) {
        ActivityManagementPaltformService activityManagementPaltformService = new ActivityManagementPaltformService();
        PsnQueryTransActivityStatusParams psnQueryTransActivityStatusParams = new PsnQueryTransActivityStatusParams();
        psnQueryTransActivityStatusParams.setTransactionId(transactionId);
        activityManagementPaltformService.psnQueryTransActivityStatus(psnQueryTransActivityStatusParams)
                .compose(this.<PsnQueryTransActivityStatusResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnQueryTransActivityStatusResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnQueryTransActivityStatusResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
//                        mTransResultView.queryTransActivityStatusFailed();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnQueryTransActivityStatusResult psnQueryTransActivityStatusResult) {
//                        mTransResultView.queryTransActivityStatusSuccess(psnQueryTransActivityStatusResult);
                    }
                });
    }

//    private String tmpFlag=null;
//    @Override
//    public void queryRecentAndCommenPayee(PsnQueryRecentPayeeInfoParams params) {
//        Observable.mergeDelayError(queryRecentPayeeInfo(params),queryPayeeListForDim())
//                .compose(bindToLifecycle())
//                .subscribe(new BIIBaseSubscriber<Object>() {
//                    @Override
//                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
//                    }
//                    @Override
//                    public void onCompleted() {
//                        tmpFlag=null;
//                        mTransBlankView.queryRecentAndCommenPayeeFinished();
//                    }
//
//                    @Override
//                    public void onNext(Object o) {
//                        if (o instanceof List) {
//                            tmpFlag="1";
//                            mTransBlankView.queryRecentPayeeInfoSuccess( (List<PsnQueryRecentPayeeInfoBean>) o);
//                        } else if (o instanceof PsnTransPayeeListqueryForDimResult){
//                            tmpFlag="0";
//                            mTransBlankView.queryPayeeListForDimSuccess((PsnTransPayeeListqueryForDimResult) o);
//                        }
//                    }
//                    @Override
//                    public void handleException(BiiResultErrorException biiResultErrorException) {
////                        if ("1".equals(tmpFlag)) {
////                            mTransBlankView.queryRecentPayeeInfoFailed(biiResultErrorException);
////                        }
////                        if ("0".equals(tmpFlag)) {
//                            mTransBlankView.queryPayeeListForDimFailed(biiResultErrorException);
////                        }
//                    }
////                    @Override
////                    public void onError(Throwable e) {
////                        if (e instanceof CompositeException) {
////                            CompositeException compositeException = (CompositeException) e;
////                            e = compositeException.getExceptions().get(0);
////                        }
////                        super.onError(e);
////                    }
//                });
//    }

    @Override
    public void queryRecentPayeeInfo(PsnQueryRecentPayeeInfoParams params) {
        transService.psnQueryRecentPayeeInfo(params)
                .compose(this.<List<PsnQueryRecentPayeeInfoBean>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnQueryRecentPayeeInfoBean>>applyIoSchedulers())
               .subscribe(new BIIBaseSubscriber<List<PsnQueryRecentPayeeInfoBean>>() {
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransBlankView.queryRecentPayeeInfoFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(List list) {
                        mTransBlankView.queryRecentPayeeInfoSuccess(list);
                    }
                });
    }

//    @Override
//    public Observable<List<PsnQueryRecentPayeeInfoBean>> queryRecentPayeeInfo(PsnQueryRecentPayeeInfoParams params){
//       return transService.psnQueryRecentPayeeInfo(params)
//                .compose(this.<List<PsnQueryRecentPayeeInfoBean>>bindToLifecycle())
//                .onErrorResumeNext(Observable.<List<PsnQueryRecentPayeeInfoBean>>empty())
//                .compose(SchedulersCompat.<List<PsnQueryRecentPayeeInfoBean>>applyIoSchedulers());
//
////                .subscribe(new BIIBaseSubscriber<List>() {
////                    @Override
////                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
////                    }
////                    @Override
////                    public void handleException(BiiResultErrorException biiResultErrorException) {
////                        mTransBlankView.queryRecentPayeeInfoFailed(biiResultErrorException);
////                    }
////                    @Override
////                    public void onCompleted() {
////                    }
////                    @Override
////                    public void onNext(List list) {
////                        mTransBlankView.queryRecentPayeeInfoSuccess(list);
////                    }
////                });
//    }
    private PsnTransPayeeListqueryForDimViewModel viewModel;
//    @Override
//    public Observable<PsnTransPayeeListqueryForDimResult> queryPayeeListForDim() {
//        PsnTransPayeeListqueryForDimParams params = new PsnTransPayeeListqueryForDimParams();
//        String[] bocFlag = {"0", "1", "3"};
//        params.setBocFlag(bocFlag);
//        params.setIsAppointed(""); // 是否定向收款人(0：非定向 1：定向 输入为空时（""）查全部)
//        params.setPayeeName("");
//        params.setCurrentIndex("0");
//        params.setPageSize("500");
//
//       return transService.psnTransPayeeListqueryForDim(params)
//                .compose(this.<PsnTransPayeeListqueryForDimResult>bindToLifecycle())
//                .compose(SchedulersCompat.<PsnTransPayeeListqueryForDimResult>applyIoSchedulers());
////                .subscribe(new BIIBaseSubscriber<PsnTransPayeeListqueryForDimResult>() {
////                    @Override
////                    public void handleException(BiiResultErrorException biiResultErrorException) {
////                        mTransBlankView.queryPayeeListForDimFailed(biiResultErrorException);
////                    }
////                    @Override
////                    public void onCompleted() {
////                    }
////                    @Override
////                    public void onNext(PsnTransPayeeListqueryForDimResult result) {
////                        mTransBlankView.queryPayeeListForDimSuccess(result);
////                    }
////                });
//    }
    @Override
    public void queryPayeeListForDim() {
        PsnTransPayeeListqueryForDimParams params = new PsnTransPayeeListqueryForDimParams();
        String[] bocFlag = {"0", "1", "3"};
        params.setBocFlag(bocFlag);
        params.setIsAppointed(""); // 是否定向收款人(0：非定向 1：定向 输入为空时（""）查全部)
        params.setPayeeName("");
        params.setCurrentIndex("0");
        params.setPageSize("500");

        transService.psnTransPayeeListqueryForDim(params)
                .compose(this.<PsnTransPayeeListqueryForDimResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnTransPayeeListqueryForDimResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransPayeeListqueryForDimResult>() {
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransBlankView.queryPayeeListForDimFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnTransPayeeListqueryForDimResult result) {
                        mTransBlankView.queryPayeeListForDimSuccess(result);
                    }
                });
    }


    @Override
    public void queryQueryBankInfobyCardBin(PsnQueryBankInfobyCardBinParams params) {
        transService.psnQueryBankInfobyCardBin(params)
                .compose(this.<PsnQueryBankInfobyCardBinResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnQueryBankInfobyCardBinResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnQueryBankInfobyCardBinResult>() {
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransBlankView.queryBankInfobyCardBinFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnQueryBankInfobyCardBinResult result) {
                        mTransBlankView.queryBankInfobyCardBinSuccess(result);
                    }
                });
    }

    @Override
    public void queryQuotaForTrans(PsnTransQuotaQueryParams params) {
        transService.psnTransQuotaQuery(params)
                 .compose(this.<PsnTransQuotaQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnTransQuotaQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransQuotaQueryResult>() {
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransBlankView.queryQuotaForTransFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnTransQuotaQueryResult result) {
                        mTransBlankView.queryQuotaForTransSuccess(result);
                    }
                });

    }

    /**
     * 查接口电子卡绑定账户
     * @param params
     */
    @Override
    public void querPsnCardQueryBindInfo(PsnCardQueryBindInfoParams params) {
        transService.psnCardQueryBindInfo(params)
                .compose(this.<PsnCardQueryBindInfoResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCardQueryBindInfoResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCardQueryBindInfoResult>() {
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransBlankView.queryPsnCardQueryBindInfoFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnCardQueryBindInfoResult result) {
                        CardQueryBindInfoResult newResult=new CardQueryBindInfoResult();
                        BeanConvertor.toBean(result,newResult);
                        mTransBlankView.queryPsnCardQueryBindInfoSuccess(newResult);
                    }
                });
    }

    /**
     * 查询账户余额
     * @param accountId
     */
    @Override
    public void queryAccountBalance(String accountId) {
        Observable.just(accountId)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnAccountQueryAccountDetailResult>>() {
                    @Override
                    public Observable<PsnAccountQueryAccountDetailResult> call(String accountId) {
                        PsnAccountQueryAccountDetailParams params = new PsnAccountQueryAccountDetailParams();
                        params.setAccountId(accountId);
                        return accountService.psnAccountQueryAccountDetail(params);
                    }
                })
                .compose(SchedulersCompat.<PsnAccountQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnAccountQueryAccountDetailResult>() {
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransBlankView.queryAccountBalanceFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnAccountQueryAccountDetailResult result) {
                        AccountQueryAccountDetailResult newResult=new AccountQueryAccountDetailResult();
                        BeanConvertor.toBean(result,newResult);
                        mTransBlankView.queryAccountBalanceSuccess(newResult);
                    }
                });
    }

    @Override
    public void queryCrcdAccountDetail(String accountId, final String currency) {
        Observable.just(accountId)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnCrcdQueryAccountDetailResult>>() {
                    @Override
                    public Observable<PsnCrcdQueryAccountDetailResult> call(String accountId) {
                        PsnCrcdQueryAccountDetailParams params = new PsnCrcdQueryAccountDetailParams();
                        params.setAccountId(accountId);
                        params.setCurrency(currency);
                        return accountService.psnCrcdQueryAccountDetail(params);
                    }
                })
                .compose(SchedulersCompat.<PsnCrcdQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdQueryAccountDetailResult>() {
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransBlankView.queryCrcdAccountBalanceFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnCrcdQueryAccountDetailResult result) {
                        CrcdQueryAccountDetailResult newResult=new CrcdQueryAccountDetailResult();
                        BeanConvertor.toBean(result,newResult);
                        mTransBlankView.queryCrcdAccountBalanceSuccess(newResult);
                    }
                });
    }

    /**
     * 查询绑定的理财账户
     */
    @Override
    public void queryPsnOFAAccountState() {
        PsnOFAAccountStateQueryParams prams=new PsnOFAAccountStateQueryParams();
        accountService.queryPsnOFAAccountState(prams)
                .compose(SchedulersCompat.<PsnOFAAccountStateQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnOFAAccountStateQueryResult>() {
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransBlankView.queryPsnOFAAccountStateFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnOFAAccountStateQueryResult result) {
                        OFAAccountStateQueryResult  newResult=new OFAAccountStateQueryResult();
                        BeanConvertor.toBean(result,newResult);
                        mTransBlankView.queryPsnOFAAccountStateSuccess(newResult);
                    }
                });
    }

//    @Override
//    public void queryLinkedAccountList(PsnQueryChinaBankAccountParams linkedParams) {
//            transService.psnQueryLinkedAccount(linkedParams)
//                    .compose(this.<PsnQueryChinaBankAccountResult>bindToLifecycle())
//                    .compose(SchedulersCompat.<PsnQueryChinaBankAccountResult>applyIoSchedulers())
//                    .subscribe(new BIIBaseSubscriber<PsnQueryChinaBankAccountResult>() {
//                        @Override
//                        public void handleException(BiiResultErrorException biiResultErrorException) {
//                            mTransBlankView.queryLinkedAccountFailed(biiResultErrorException);
//                        }
//                        @Override
//                        public void onCompleted() {
//                        }
//                        @Override
//                        public void onNext(PsnQueryChinaBankAccountResult result) {
//                            mTransBlankView.queryLinkedAccountSuccess(result);
//                        }
//                    });
//
//    }

//    @Override
//    public void queryTransPayeeList(PsnTransPayeeListqueryForDimParams bocParams) {
//        PsnTransPayeeListqueryForDimParams payeelistParams=new PsnTransPayeeListqueryForDimParams();
//        String[] aa= {""};
//        payeelistParams.setBocFlag(aa);//测试参数，这些数据应取自转账类型
////      payeelistParams.setPayeeName();
//        payeelistParams.setIsAppointed("");
//        payeelistParams.setCurrentIndex("0");
//        payeelistParams.setPageSize("20");
//        transService.getPsnTransPayeeListqueryForDim(payeelistParams)
//                .subscribe(new BIIBaseSubscriber<PsnTransPayeeListqueryForDimResult>() {
//                    @Override
//                    public void handleException(BiiResultErrorException biiResultErrorException) {
//                        Log.d("wy","失败了+"+biiResultErrorException.getMessage());
//                        Toast.makeText(((TransRemitBlankFragment)mTransBlankView).getContext(),  biiResultErrorException.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onNext(PsnTransPayeeListqueryForDimResult psnTransPayeeListqueryForDimResult) {
//                        //调用查询成功后处理逻辑方法
//                        mTransBlankView.queryTransPayeeListSuccess(psnTransPayeeListqueryForDimResult);
//
//                    }
//                });
//    }

    /**
     * 增加收款人
     */
//  @Override
//    public void addTransPayee() {
//      PsnTransBocAddPayeeParams addPayeeParams=new PsnTransBocAddPayeeParams();
//    }


    public void getConverIdandSaftyFator(final String serviceid){
        //查询之前请求会话
        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnGetSecurityFactorResult>>() {
                    @Override
                    public Observable<PsnGetSecurityFactorResult> call(String conId) {
                        PsnGetSecurityFactorParams factorParams=new PsnGetSecurityFactorParams();
                        conversationId=conId;
                        factorParams.setConversationId(conId);
                        factorParams.setServiceId(serviceid);
                        return globalService.psnGetSecurityFactor(factorParams);
                    }
                })
                .compose(SchedulersCompat.<PsnGetSecurityFactorResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnGetSecurityFactorResult>() {
            @Override
            public void handleException(BiiResultErrorException biiResultErrorException) {
                mTransBlankView.getConverIdandSaftyFatorFailed(biiResultErrorException);
            }
            @Override
            public void onCompleted() {
            }
            @Override
            public void onNext(PsnGetSecurityFactorResult factorResult) {
                   mTransBlankView.getConverIdandSaftyFatorSuccess(factorResult,conversationId);
            }
        });
    }



    /**
     * 转账预交易
     */
    @Override
    public void transBocVerify(final  PsnTransBocTransferVerifyParams transbocverifyparams) {
        //查询之前请求会话
//        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
//        globalService.psnCreatConversation(psnCreatConversationParams)
//                .compose(this.<String>bindToLifecycle())
//                .flatMap(new Func1<String, Observable<PsnGetSecurityFactorResult>>() {
//                    @Override
//                    public Observable<PsnGetSecurityFactorResult> call(String conId) {
//                        PsnGetSecurityFactorParams factorParams=new PsnGetSecurityFactorParams();
//                        conversationId=conId;
//                        factorParams.setConversationId(conId);
//                        factorParams.setServiceId("PB031");
//                        return globalService.psnGetSecurityFactor(factorParams);
//                    }
//                })
//                .flatMap(new Func1<PsnGetSecurityFactorResult, Observable<PsnTransBocTransferVerifyResult>>() {
//                    @Override
//                    public Observable<PsnTransBocTransferVerifyResult> call(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
////                        0:虚拟 4:USBKey证书 8:动态口令令牌 32:短信认证码 40:动态口令令牌+短信认证码 96:短信认证码+硬件绑定
//                        securityFactorResult=psnGetSecurityFactorResult;
//                        CombinListBean factorbean=SecurityVerity.getInstance().getDefaultSecurityFactorId(new SecurityFactorModel(securityFactorResult));//安全认证工具
////                        CombinListBean combinListBean=psnGetSecurityFactorResult.get_defaultCombin();
////                        if (null!=combinListBean){
////                            transbocverifyparams.set_combinId(combinListBean.getId());
////                        }else{
////                            combinListBean=psnGetSecurityFactorResult.get_combinList().get(0);
////                            transbocverifyparams.set_combinId(combinListBean.getId());
////                        }
//                        transbocverifyparams.set_combinId(factorbean.getId());
//                        transbocverifyparams.setConversationId(conversationId);
//
//                        return transService.psnTransBocTransferVerify(transbocverifyparams);
//                    }
//                })
                transService.psnTransBocTransferVerify(transbocverifyparams)
                        .compose(this.<PsnTransBocTransferVerifyResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnTransBocTransferVerifyResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransBocTransferVerifyResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
//                        mTransBlankView.trans(biiResultErrorException);
                        mTransBlankView.transBocVerifyFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnTransBocTransferVerifyResult transVerifyResult) {
                        mTransBlankView.transBocVerifySuccess(transVerifyResult,securityFactorResult,conversationId);
                    }
                });
    }
    /**
     * 定向转账预交易
     */
    @Override
    public void transDirBocVerify(final PsnDirTransBocTransferVerifyParams dirBocverifyParams) {

        //查询之前请求会话
//
//        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
//        globalService.psnCreatConversation(psnCreatConversationParams)
//                .compose(this.<String>bindToLifecycle())
//                .flatMap(new Func1<String, Observable<PsnGetSecurityFactorResult>>() {
//                    @Override
//                    public Observable<PsnGetSecurityFactorResult> call(String conId) {
//                        PsnGetSecurityFactorParams factorParams=new PsnGetSecurityFactorParams();
//                        conversationId=conId;
//                        factorParams.setConversationId(conId);
//                        factorParams.setServiceId("PB033");
//                        return globalService.psnGetSecurityFactor(factorParams);
//                    }
//                })
//                .flatMap(new Func1<PsnGetSecurityFactorResult, Observable<PsnDirTransBocTransferVerifyResult>>() {
//                    @Override
//                    public Observable<PsnDirTransBocTransferVerifyResult> call(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
//                        securityFactorResult=psnGetSecurityFactorResult;
////                        CombinListBean combinListBean=psnGetSecurityFactorResult.get_defaultCombin();
////                        if (null!=combinListBean){
////                            dirBocverifyParams.set_combinId(combinListBean.getId());
////                        }else{
////                            combinListBean=psnGetSecurityFactorResult.get_combinList().get(0);
////                            dirBocverifyParams.set_combinId(combinListBean.getId());
////                        }
//                        CombinListBean factorbean=SecurityVerity.getInstance().getDefaultSecurityFactorId(new SecurityFactorModel(securityFactorResult));//安全认证工具
////                        CombinListBean combinListBean=psnGetSecurityFactorResult.get_defaultCombin();
//                        dirBocverifyParams.set_combinId(factorbean.getId());
//                        dirBocverifyParams.setConversationId(conversationId);
//                        return transService.psnDirTransBocTransferVerify(dirBocverifyParams);
//                    }
//                })
                transService.psnDirTransBocTransferVerify(dirBocverifyParams)
                 .compose(this.<PsnDirTransBocTransferVerifyResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnDirTransBocTransferVerifyResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnDirTransBocTransferVerifyResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransBlankView.transBocVerifyFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnDirTransBocTransferVerifyResult result) {
                        mTransBlankView.transDirBocVerifySuccess(result,securityFactorResult,conversationId);
                    }
                });
    }
    /**
     * 费用试算
     */
    @Override
    public void getTransBocCommissionCharge(PsnTransGetBocTransferCommissionChargeParams params) {
        transService.psnTransGetBocTransferCommissionCharge(params)
                .compose(SchedulersCompat.<PsnTransGetBocTransferCommissionChargeResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransGetBocTransferCommissionChargeResult>() {
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransBlankView.getTransBocCommissionChargeFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnTransGetBocTransferCommissionChargeResult psnTransGetBocTransferCommissionChargeResult) {
                        mTransBlankView.getTransBocCommissionChargeSuccess(psnTransGetBocTransferCommissionChargeResult);
                    }
                });
    }

    // TODO: 2016/6/14 调用账户管理下查询账户明细接口，查余额
    @Override
    public void getBocAccountBalance() {
        // TODO: 2016/6/14  传入accoutid查询后台余额 ，余额调用后台客户端
    }

    @Override
    public void getTransNationalCommissionCharge(PsnTransGetNationalTransferCommissionChargeParams params) {
        transService.psnTransGetNationalTransferCommissionCharge(params)
                .compose(SchedulersCompat.<PsnTransGetNationalTransferCommissionChargeResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransGetNationalTransferCommissionChargeResult>() {
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransBlankView.getTransNationalCommissionChargeFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnTransGetNationalTransferCommissionChargeResult psnTransGetNationalTransferCommissionChargeResult) {
                        mTransBlankView.getTransNationalCommissionChargeSuccess(psnTransGetNationalTransferCommissionChargeResult);
                    }
                });
    }


    @Override
    public void transDirNationalRealTimeVerify(final PsnDirTransCrossBankTransferParams params) {
//        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
//        globalService.psnCreatConversation(psnCreatConversationParams)
//                .compose(this.<String>bindToLifecycle())
//                .flatMap(new Func1<String, Observable<PsnGetSecurityFactorResult>>() {
//                    @Override
//                    public Observable<PsnGetSecurityFactorResult> call(String conId) {
//                        PsnGetSecurityFactorParams factorParams=new PsnGetSecurityFactorParams();
//                        conversationId=conId;
//                        factorParams.setConversationId(conId);
//                        factorParams.setServiceId("PB118");
//                        return globalService.psnGetSecurityFactor(factorParams);
//                    }
//                })
//                .flatMap(new Func1<PsnGetSecurityFactorResult, Observable<PsnDirTransCrossBankTransferResult>>() {
//                    @Override
//                    public Observable<PsnDirTransCrossBankTransferResult> call(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
//                        securityFactorResult=psnGetSecurityFactorResult;
////                        CombinListBean combinListBean=psnGetSecurityFactorResult.get_defaultCombin();
////                        if (null!=combinListBean){
////                            params.set_combinId(combinListBean.getId());
////                        }else{
////                            combinListBean=psnGetSecurityFactorResult.get_combinList().get(0);
////                            params.set_combinId(combinListBean.getId());
////                        }
//                        CombinListBean factorbean=SecurityVerity.getInstance().getDefaultSecurityFactorId(new SecurityFactorModel(securityFactorResult));//安全认证工具
////                        CombinListBean combinListBean=psnGetSecurityFactorResult.get_defaultCombin();
//                        params.set_combinId(factorbean.getId());
//                        params.setConversationId(conversationId);
//                        return transService.psnDirTransNationalRealtimeVerify(params);
//                    }
//                })
                 transService.psnDirTransNationalRealtimeVerify(params)
                         .compose(this.<PsnDirTransCrossBankTransferResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnDirTransCrossBankTransferResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnDirTransCrossBankTransferResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransBlankView.transDirNationalRealTimeVerifyFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnDirTransCrossBankTransferResult result) {
                        mTransBlankView.transDirNationalRealTimeVerifySuccess(result,securityFactorResult,conversationId);
                    }
                });
    }

    @Override
    public void transNationalRealTimeVerify(final PsnEbpsRealTimePaymentConfirmParams params) {

                 transService.psnTransNationalRealtimeVerify(params)
                         .compose(this.<PsnEbpsRealTimePaymentConfirmResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnEbpsRealTimePaymentConfirmResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnEbpsRealTimePaymentConfirmResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransBlankView.transBocVerifyFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnEbpsRealTimePaymentConfirmResult result) {
                        mTransBlankView.transNationalRealTimeVerifySuccess(result,securityFactorResult,conversationId);
                    }
                });

    }

    @Override
    public void transDirNationalVerify(final PsnDirTransBocNationalTransferVerifyParams dirNationalVerifyParams) {
//        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
//        globalService.psnCreatConversation(psnCreatConversationParams)
//                .compose(this.<String>bindToLifecycle())
//                .flatMap(new Func1<String, Observable<PsnGetSecurityFactorResult>>() {
//                    @Override
//                    public Observable<PsnGetSecurityFactorResult> call(String conId) {
//                        PsnGetSecurityFactorParams factorParams=new PsnGetSecurityFactorParams();
//                        conversationId=conId;
//                        factorParams.setConversationId(conId);
//                        factorParams.setServiceId("PB034");
//
//                        return globalService.psnGetSecurityFactor(factorParams);
//                    }
//                })
//                .flatMap(new Func1<PsnGetSecurityFactorResult, Observable<PsnDirTransBocNationalTransferVerifyResult>>() {
//
//                    @Override
//                    public Observable<PsnDirTransBocNationalTransferVerifyResult> call(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
//                        securityFactorResult=psnGetSecurityFactorResult;
////                        CombinListBean combinListBean=psnGetSecurityFactorResult.get_defaultCombin();
////                        if (null!=combinListBean){
////                            dirNationalVerifyParams.set_combinId(combinListBean.getId());
////                        }else{
////                            combinListBean=psnGetSecurityFactorResult.get_combinList().get(0);
////                            dirNationalVerifyParams.set_combinId(combinListBean.getId());
////                        }
//                        CombinListBean factorbean=SecurityVerity.getInstance().getDefaultSecurityFactorId(new SecurityFactorModel(securityFactorResult));//安全认证工具
////                        CombinListBean combinListBean=psnGetSecurityFactorResult.get_defaultCombin();
//                        dirNationalVerifyParams.set_combinId(factorbean.getId());
//                        dirNationalVerifyParams.setConversationId(conversationId);
//                        return transService.psnDirTransNationalVerify(dirNationalVerifyParams);
//                    }
//                })
                 transService.psnDirTransNationalVerify(dirNationalVerifyParams)
                         .compose(this.<PsnDirTransBocNationalTransferVerifyResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnDirTransBocNationalTransferVerifyResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnDirTransBocNationalTransferVerifyResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnDirTransBocNationalTransferVerifyResult result) {
                        mTransBlankView.transDirNationalVerifySuccess(result,securityFactorResult,conversationId);
                    }
                });
    }

    @Override
    public void transNationalVerify(final PsnTransBocNationalTransferVerifyParams nationalVerifyParams) {
//        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
//        globalService.psnCreatConversation(psnCreatConversationParams)
//                .compose(this.<String>bindToLifecycle())
//                .flatMap(new Func1<String, Observable<PsnGetSecurityFactorResult>>() {
//                    @Override
//                    public Observable<PsnGetSecurityFactorResult> call(String conId) {
//                        PsnGetSecurityFactorParams factorParams=new PsnGetSecurityFactorParams();
//                        conversationId=conId;
//                        factorParams.setConversationId(conId);
//                        factorParams.setServiceId("PB032");
//                        return globalService.psnGetSecurityFactor(factorParams);
//                    }
//                })
//                .flatMap(new Func1<PsnGetSecurityFactorResult, Observable<PsnTransBocNationalTransferVerifyResult>>() {
//
//                    @Override
//                    public Observable<PsnTransBocNationalTransferVerifyResult> call(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
////                        Log.e("wywy","psnGetSecurityFactorResult大学"+psnGetSecurityFactorResult.get_combinList().size());
//                        securityFactorResult=psnGetSecurityFactorResult;
////                        CombinListBean combinListBean=psnGetSecurityFactorResult.get_defaultCombin();
////                        if (null!=combinListBean){
////                            nationalVerifyParams.set_combinId(combinListBean.getId());
////                        }else{
////                            combinListBean=psnGetSecurityFactorResult.get_combinList().get(0);
////                            nationalVerifyParams.set_combinId(combinListBean.getId());
////                        }
//                        CombinListBean factorbean=SecurityVerity.getInstance().getDefaultSecurityFactorId(new SecurityFactorModel(securityFactorResult));//安全认证工具
////                        CombinListBean combinListBean=psnGetSecurityFactorResult.get_defaultCombin();
//                        nationalVerifyParams.set_combinId(factorbean.getId());
//                        nationalVerifyParams.setConversationId(conversationId);
//                        return transService.psnTransferNationalVerify(nationalVerifyParams);
//                    }
//                })
                 transService.psnTransferNationalVerify(nationalVerifyParams)
                         .compose(this.<PsnTransBocNationalTransferVerifyResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnTransBocNationalTransferVerifyResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransBocNationalTransferVerifyResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnTransBocNationalTransferVerifyResult result) {
                        mTransBlankView.transNationalVerifySuccess(result,securityFactorResult,conversationId);
                    }
                });
    }


    @Override
    public void transNationQueryBankInfo() {

    }

//    private String getLastVerityType() {
//        SharedPreferences preferences = activity.getSharedPreferences(activity.getString(R.string.app_name), Context.MODE_PRIVATE);
//        return preferences.getString(SECURITY_VERIFY_TYPE, "");
//    }
}
