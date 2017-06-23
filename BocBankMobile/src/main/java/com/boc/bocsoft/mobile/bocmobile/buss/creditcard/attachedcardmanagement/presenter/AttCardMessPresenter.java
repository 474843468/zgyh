package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.presenter;

import android.util.Log;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainMessSetResult.PsnCrcdAppertainMessSetResultResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAppertainAndMess.PsnCrcdQueryAppertainAndMessParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAppertainAndMess.PsnCrcdQueryAppertainAndMessResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.service.CrcdService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model.AttCardMessModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model.AttCardSetUpModel;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.functions.Func1;

/**
 * Name: liukai
 * Time：2016/12/8 21:36.
 * Created by lk7066 on 2016/12/8.
 * It's used to
 */

public class AttCardMessPresenter implements AttCardMessContract.AttCardMessPresenter{

    private RxLifecycleManager mRxLifecycleManager;

    /**
     * 信用卡service
     */
    private CrcdService crcdService;

    private GlobalService globalService;

    private AttCardMessContract.AttCardMessView attCardMessView;

    public AttCardMessPresenter(AttCardMessContract.AttCardMessView messView){
        attCardMessView = messView;
        attCardMessView.setPresenter(this);
        crcdService = new CrcdService();
        globalService = new GlobalService();
        mRxLifecycleManager= new RxLifecycleManager();
    }

    /**
     * 附属卡设置对象查询
     * */
    @Override
    public void queryAppertainAndMess(AttCardSetUpModel attCardSetUpModel) {

        PsnCrcdQueryAppertainAndMessParams params = new PsnCrcdQueryAppertainAndMessParams();
        params.setCardNo(attCardSetUpModel.getSubCrcdNo());
        params.setCurrency(attCardSetUpModel.getCurrency1());

        crcdService.psnCrcdQueryAppertainAndMess(params)
                .compose(mRxLifecycleManager.<PsnCrcdQueryAppertainAndMessResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdQueryAppertainAndMessResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdQueryAppertainAndMessResult>() {

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        Log.d("--liukai--对象查询", "失败");
                        attCardMessView.appertainAndMessFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                        Log.d("--liukai--对象查询", "完成");
                    }

                    @Override
                    public void onNext(PsnCrcdQueryAppertainAndMessResult psnCrcdQueryAppertainAndMessResult) {
                        Log.d("--liukai--对象查询", "成功");
                        attCardMessView.appertainAndMessSuccess(psnCrcdQueryAppertainAndMessResult);
                    }

                });

    }

    /**
     * 附属卡短信提醒设置
     * */
    @Override
    public void setAppertainMessResult(final AttCardMessModel attCardMessModel) {

        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
        params.setConversationId(AttCardTradeDetailPresenter.attCardConversationId);

        globalService.psnGetTokenId(params)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnCrcdAppertainMessSetResultResult>>() {

                    @Override
                    public Observable<PsnCrcdAppertainMessSetResultResult> call(String tokenId) {
                        attCardMessModel.setConversationId(AttCardTradeDetailPresenter.attCardConversationId);
                        attCardMessModel.setToken(tokenId);
                        return crcdService.psnCrcdAppertainMessSetResult(attCardMessModel.setMessResultParams());
                    }

                })
                .compose(SchedulersCompat.<PsnCrcdAppertainMessSetResultResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdAppertainMessSetResultResult>(){

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        Log.d("--liukai--短信设定", "设定异常");
                        attCardMessView.setAppertainMessResultFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                        Log.d("--liukai--短信设定", "完成");
                    }

                    @Override
                    public void onNext(PsnCrcdAppertainMessSetResultResult psnCrcdAppertainMessSetResultResult) {
                        Log.d("--liukai--短信设定", "成功");
                        attCardMessView.setAppertainMessResultSuccess(psnCrcdAppertainMessSetResultResult);
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
                        Log.d("--liukai信用卡币种查询--", "异常情况");
                        attCardMessView.crcdCurrencyQueryFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                        Log.d("--liukai信用卡币种查询--", "完成情况");
                    }

                    @Override
                    public void onNext(PsnCrcdCurrencyQueryResult psnCrcdCurrencyQueryResult) {
                        Log.d("--liukai信用卡币种查询--", "成功情况");
                        attCardMessView.crcdCurrencyQuerySuccess(psnCrcdCurrencyQueryResult);
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
