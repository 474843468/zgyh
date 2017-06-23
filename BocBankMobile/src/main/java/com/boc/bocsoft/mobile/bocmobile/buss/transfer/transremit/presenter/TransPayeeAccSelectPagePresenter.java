package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnCardQueryBindInfo.PsnCardQueryBindInfoParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnCardQueryBindInfo.PsnCardQueryBindInfoResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnCrcdChargeOnRMBAccountQuery.PsnCrcdChargeOnRMBAccountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnCrcdChargeOnRMBAccountQuery.PsnCrcdChargeOnRMBAccountQueryResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnOFAAccountStateQuery.PsnOFAAccountStateQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnOFAAccountStateQuery.PsnOFAAccountStateQueryResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.AccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.CardQueryBindInfoResult;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.CrcdQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.OFAAccountStateQueryResult;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.ui.TransContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by WYme on 2016/9/28.
 */
public class TransPayeeAccSelectPagePresenter implements TransContract.TransPresenterPayeeAccSelectPage {


    /**
     * 转账汇款service
     */

    private TransContract.TransViewPayeeAccSelectPage mTransPayeeAccSelectView;//转账填写页面
    private AccountService accountService;
    private TransferService transService;

    private RxLifecycleManager rxLifecycleManager;
    public TransPayeeAccSelectPagePresenter(TransContract.TransViewPayeeAccSelectPage mTransPayeeAccSelectView){

        this.mTransPayeeAccSelectView=mTransPayeeAccSelectView;
        mTransPayeeAccSelectView.setPresenter(this);

        transService=new TransferService();
        accountService=new AccountService();

        rxLifecycleManager = new RxLifecycleManager();
    }

//    @Override
//    public void querPsnCardQueryBindInfo(PsnCardQueryBindInfoParams params) {
//        transService.psnCardQueryBindInfo(params)
//                .compose(rxLifecycleManager.<PsnCardQueryBindInfoResult>bindToLifecycle())
//                .compose(SchedulersCompat.<PsnCardQueryBindInfoResult>applyIoSchedulers())
//                .subscribe(new BIIBaseSubscriber<PsnCardQueryBindInfoResult>() {
//                    @Override
//                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
//                    }
//                    @Override
//                    public void handleException(BiiResultErrorException biiResultErrorException) {
//                        mTransPayeeAccSelectView.queryPsnCardQueryBindInfoFailed(biiResultErrorException);
//                    }
//                    @Override
//                    public void onCompleted() {
//                    }
//                    @Override
//                    public void onNext(PsnCardQueryBindInfoResult result) {
//                        CardQueryBindInfoResult newResult=new CardQueryBindInfoResult();
//                        BeanConvertor.toBean(result,newResult);
//                        mTransPayeeAccSelectView.queryPsnCardQueryBindInfoSuccess(newResult);
//                    }
//                });
//    }

    @Override
    public void queryAccountBalance(String accountId) {
        Observable.just(accountId)
                .compose(rxLifecycleManager.<String>bindToLifecycle())
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
                        mTransPayeeAccSelectView.queryAccountBalanceFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnAccountQueryAccountDetailResult result) {
                        AccountQueryAccountDetailResult newResult=new AccountQueryAccountDetailResult();
                        BeanConvertor.toBean(result,newResult);
                        mTransPayeeAccSelectView.queryAccountBalanceSuccess(newResult);
                    }
                });
    }

    @Override
    public void queryCrcdAccountDetail(String accountId, final String currency) {
        Observable.just(accountId)
                .compose(rxLifecycleManager.<String>bindToLifecycle())
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
                        mTransPayeeAccSelectView.queryCrcdAccountBalanceFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnCrcdQueryAccountDetailResult result) {
                        CrcdQueryAccountDetailResult newResult=new CrcdQueryAccountDetailResult();
                        BeanConvertor.toBean(result,newResult);
                        mTransPayeeAccSelectView.queryCrcdAccountBalanceSuccess(newResult);
                    }
                });
    }

    @Override
    public void queryPsnCrcdChargeOnRMBAccount(String accoutId) {
        Observable.just(accoutId)
                .compose(rxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnCrcdChargeOnRMBAccountQueryResult>>() {
                    @Override
                    public Observable<PsnCrcdChargeOnRMBAccountQueryResult> call(String s) {
                        PsnCrcdChargeOnRMBAccountQueryParams params=new PsnCrcdChargeOnRMBAccountQueryParams();
                        params.setAccountId(s);
                        return transService.psnCrcdChargeOnRMBAccountQuery(params);
                    }
                })
                .compose(SchedulersCompat.<PsnCrcdChargeOnRMBAccountQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdChargeOnRMBAccountQueryResult>() {
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransPayeeAccSelectView.queryPsnCrcdChargeOnRMBAccountFaild(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnCrcdChargeOnRMBAccountQueryResult result) {
                        mTransPayeeAccSelectView.queryPsnCrcdChargeOnRMBAccountSuccess(result);
                    }
                });
    }

//    @Override
//    public void queryPsnOFAAccountState() {
//        PsnOFAAccountStateQueryParams prams=new PsnOFAAccountStateQueryParams();
//        accountService.queryPsnOFAAccountState(prams)
//                .compose(SchedulersCompat.<PsnOFAAccountStateQueryResult>applyIoSchedulers())
//                .subscribe(new BIIBaseSubscriber<PsnOFAAccountStateQueryResult>() {
//                    @Override
//                    public void handleException(BiiResultErrorException biiResultErrorException) {
//                        mTransPayeeAccSelectView.queryPsnOFAAccountStateFailed(biiResultErrorException);
//                    }
//                    @Override
//                    public void onCompleted() {
//                    }
//                    @Override
//                    public void onNext(PsnOFAAccountStateQueryResult result) {
//                        OFAAccountStateQueryResult newResult=new OFAAccountStateQueryResult();
//                        BeanConvertor.toBean(result,newResult);
//                        mTransPayeeAccSelectView.queryPsnOFAAccountStateSuccess(newResult);
//                    }
//                });
//    }
    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
      rxLifecycleManager.onDestroy();
    }
}
