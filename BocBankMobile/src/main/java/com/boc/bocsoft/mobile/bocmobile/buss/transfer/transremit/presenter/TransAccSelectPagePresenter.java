package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnCardQueryBindInfo.PsnCardQueryBindInfoParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnCardQueryBindInfo.PsnCardQueryBindInfoResult;
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
//import com.boc.bocsoft.mobile.framework.rx.lifecycle.this;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by WYme on 2016/9/28.
 */
public class TransAccSelectPagePresenter extends RxPresenter implements TransContract.TransPresenterAccSelectPage {


    /**
     * 转账汇款service
     */

    private TransContract.TransViewAccSelectPage mTransAccSelectView;//转账填写页面
    private AccountService accountService;
    private TransferService transService;

    //    private this this;
    public TransAccSelectPagePresenter(TransContract.TransViewAccSelectPage mTransAccSelectView) {

        this.mTransAccSelectView = mTransAccSelectView;
//          mTransConfirmView=confirmView;
        mTransAccSelectView.setPresenter(this);
//
        transService = new TransferService();
        accountService = new AccountService();

//        this = new this();
    }

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
                        mTransAccSelectView.queryPsnCardQueryBindInfoFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnCardQueryBindInfoResult result) {
                        CardQueryBindInfoResult newResult = new CardQueryBindInfoResult();
                        BeanConvertor.toBean(result, newResult);
                        mTransAccSelectView.queryPsnCardQueryBindInfoSuccess(newResult);
                    }
                });
    }

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
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransAccSelectView.queryAccountBalanceFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnAccountQueryAccountDetailResult result) {
                        AccountQueryAccountDetailResult newResult = new AccountQueryAccountDetailResult();
                        BeanConvertor.toBean(result, newResult);
                        mTransAccSelectView.queryAccountBalanceSuccess(newResult);
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
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransAccSelectView.queryCrcdAccountBalanceFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnCrcdQueryAccountDetailResult result) {
                        CrcdQueryAccountDetailResult newResult = new CrcdQueryAccountDetailResult();
                        BeanConvertor.toBean(result, newResult);
                        mTransAccSelectView.queryCrcdAccountBalanceSuccess(newResult);
                    }
                });
    }

    @Override
    public void queryPsnOFAAccountState() {
        PsnOFAAccountStateQueryParams prams = new PsnOFAAccountStateQueryParams();
        accountService.queryPsnOFAAccountState(prams)
                .compose(SchedulersCompat.<PsnOFAAccountStateQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnOFAAccountStateQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransAccSelectView.queryPsnOFAAccountStateFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnOFAAccountStateQueryResult result) {
                        OFAAccountStateQueryResult newResult = new OFAAccountStateQueryResult();
                        BeanConvertor.toBean(result, newResult);
                        mTransAccSelectView.queryPsnOFAAccountStateSuccess(newResult);
                    }
                });
    }

}
//    @Override
//    public void subscribe() {
//
//    }
//
//    @Override
//    public void unsubscribe() {
//      this.onDestroy();
//    }
//}
