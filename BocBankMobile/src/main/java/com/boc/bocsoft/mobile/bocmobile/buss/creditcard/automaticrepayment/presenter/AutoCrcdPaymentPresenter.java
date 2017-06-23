package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.automaticrepayment.presenter;

import android.util.Log;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdPaymentWaySetup.PsnCrcdPaymentWaySetupResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryCrcdPaymentWay.PsnCrcdQueryCrcdPaymentWayParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryCrcdPaymentWay.PsnCrcdQueryCrcdPaymentWayResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.service.CrcdService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.automaticrepayment.model.AutoCrcdPayModel;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.functions.Func1;

/**
 * Name: liukai
 * Time：2016/11/25 15:58.
 * Created by lk7066 on 2016/11/25.
 * It's used to
 */

public class AutoCrcdPaymentPresenter implements AutoCrcdPaymentContract.AutoCrcdPaymentPresenter{

    private RxLifecycleManager mRxLifecycleManager;

    /**
     * 信用卡service
     */
    private CrcdService crcdService;

    /**
     * 公共service
     */
    private GlobalService globalService;

    private AutoCrcdPaymentContract.AutoCrcdPaymentView autoCrcdPaymentView;

    public AutoCrcdPaymentPresenter(AutoCrcdPaymentContract.AutoCrcdPaymentView mView){
        autoCrcdPaymentView = mView;
        autoCrcdPaymentView.setPresenter(this);
        crcdService = new CrcdService();
        globalService = new GlobalService();
        mRxLifecycleManager= new RxLifecycleManager();
    }

    /**
     * 还款方式查询
     */
    @Override
    public void queryCrcdPaymentWay(String s) {

        PsnCrcdQueryCrcdPaymentWayParams psnCrcdQueryCrcdPaymentWayParams = new PsnCrcdQueryCrcdPaymentWayParams();
        psnCrcdQueryCrcdPaymentWayParams.setAccountId(s);//上送accountId

        crcdService.psnCrcdQueryCrcdPaymentWay(psnCrcdQueryCrcdPaymentWayParams)
                .compose(mRxLifecycleManager.<PsnCrcdQueryCrcdPaymentWayResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdQueryCrcdPaymentWayResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdQueryCrcdPaymentWayResult>() {

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        autoCrcdPaymentView.crcdPaymentWayFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdQueryCrcdPaymentWayResult crcdPaymentWayResult) {
                        autoCrcdPaymentView.crcdPaymentWaySuccess(crcdPaymentWayResult);
                    }

                });

    }


    /**
     * 信用卡币种查询
     */
    @Override
    public void queryCrcdCurrency(String s){

        PsnCrcdCurrencyQueryParams mCurrencyParams = new PsnCrcdCurrencyQueryParams();
        mCurrencyParams.setAccountNumber(s);//上送accountnumber

        crcdService.psnCrcdCurrencyQuery(mCurrencyParams)
                .compose(mRxLifecycleManager.<PsnCrcdCurrencyQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdCurrencyQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdCurrencyQueryResult>() {

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        autoCrcdPaymentView.crcdCurrencyQueryFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdCurrencyQueryResult psnCrcdCurrencyQueryResult) {
                        autoCrcdPaymentView.crcdCurrencyQuerySuccess(psnCrcdCurrencyQueryResult);
                    }

                });

    }

    /**
     * 信用卡还款方式设定
     */
    @Override
    public void setCrcdPaymentWay(final AutoCrcdPayModel autoCrcdPayModel){

        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        final String[] tmp = new String[1];

        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        tmp[0] = s;
                        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
                        params.setConversationId(s);
                        return globalService.psnGetTokenId(params);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnCrcdPaymentWaySetupResult>>() {
                    @Override
                    public Observable<PsnCrcdPaymentWaySetupResult> call(String tokenId) {
                        autoCrcdPayModel.setConversationId(tmp[0]);
                        autoCrcdPayModel.setToken(tokenId);
                        return crcdService.psnCrcdPaymentWaySetupResult(autoCrcdPayModel.setPaymentWayParams());
                    }
                })
                .compose(SchedulersCompat.<PsnCrcdPaymentWaySetupResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdPaymentWaySetupResult>(){

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        autoCrcdPaymentView.setCrcdPaymentWayFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdPaymentWaySetupResult result) {
                        autoCrcdPaymentView.setCrcdPaymentWaySuccess(result);
                    }

                });

    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
    }

}
