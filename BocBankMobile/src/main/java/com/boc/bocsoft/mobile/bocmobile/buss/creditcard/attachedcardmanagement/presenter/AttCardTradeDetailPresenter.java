package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.presenter;

import android.util.Log;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainTranQuery.PsnCrcdAppertainTranQueryParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainTranQuery.PsnCrcdAppertainTranQueryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.service.CrcdService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model.AttCardTradeDetailModel;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.functions.Func1;

/**
 * Name: liukai
 * Time：2016/12/10 13:22.
 * Created by lk7066 on 2016/12/10.
 * It's used to 交易明细页面的presenter
 */

public class AttCardTradeDetailPresenter implements AttCardTradeDetailContract.AttCardTradeDetailPresenter {

    private RxLifecycleManager mRxLifecycleManager;

    /**
     * 信用卡service
     */
    private CrcdService crcdService;

    private GlobalService globalService;

    private AttCardTradeDetailContract.AttCardTradeDetailView attCardTradeDetailView;

    public static  String attCardConversationId = "";

    public AttCardTradeDetailPresenter(AttCardTradeDetailContract.AttCardTradeDetailView mView){

        attCardTradeDetailView = mView;
        attCardTradeDetailView.setPresenter(this);
        crcdService = new CrcdService();
        globalService = new GlobalService();
        mRxLifecycleManager= new RxLifecycleManager();

    }

    /**
     * 交易明细查询
     * 第一次查询
     * */
    @Override
    public void queryAppertainFirstTranDetail(final AttCardTradeDetailModel attCardDetailModel) {

        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();

        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnCrcdAppertainTranQueryResult>>() {

                    @Override
                    public Observable<PsnCrcdAppertainTranQueryResult> call(String s) {
                        attCardConversationId = s;
                        PsnCrcdAppertainTranQueryParams params = new PsnCrcdAppertainTranQueryParams();
                        params.setAccountId(attCardDetailModel.getSubCrcdNo());
                        params.setStartDate(attCardDetailModel.getStartData());
                        params.setEndDate(attCardDetailModel.getEndData());
                        params.setCurrentIndex(attCardDetailModel.getCurrentIndex());
                        params.setPageSize(attCardDetailModel.getPageSize());
                        params.set_refresh(attCardDetailModel.get_refresh());
                        params.setConversationId(s);
                        return crcdService.psnCrcdAppertainTranQuery(params);
                    }

                })
                .compose(SchedulersCompat.<PsnCrcdAppertainTranQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdAppertainTranQueryResult>(){

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        Log.d("--liukai--交易记录查询", "失败");
                        attCardTradeDetailView.appertainTranDetailFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                        Log.d("--liukai--交易记录查询", "完成");
                    }

                    @Override
                    public void onNext(PsnCrcdAppertainTranQueryResult psnCrcdAppertainTranQueryResult) {
                        Log.d("--liukai--交易记录查询", "成功");
                        attCardTradeDetailView.appertainTranDetailSuccess(psnCrcdAppertainTranQueryResult);
                    }

                });

    }

    /**
     * 上拉加载查询明细
     * 主要用于上拉加载查询明细使用的是同一个conversationId
     * 使用第一次查询请求的conversationId就可以
     * */
    @Override
    public void queryAppertainLoadTranDetail(AttCardTradeDetailModel attCardDetailModel) {

        PsnCrcdAppertainTranQueryParams params = new PsnCrcdAppertainTranQueryParams();
        params.setAccountId(attCardDetailModel.getSubCrcdNo());
        params.setStartDate(attCardDetailModel.getStartData());
        params.setEndDate(attCardDetailModel.getEndData());
        params.setCurrentIndex(attCardDetailModel.getCurrentIndex());
        params.setPageSize(attCardDetailModel.getPageSize());
        params.set_refresh(attCardDetailModel.get_refresh());
        params.setConversationId(attCardConversationId);

        crcdService.psnCrcdAppertainTranQuery(params)
                .compose(mRxLifecycleManager.<PsnCrcdAppertainTranQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdAppertainTranQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdAppertainTranQueryResult>() {

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        Log.d("--liukai--", "上拉失败");
                        attCardTradeDetailView.appertainTranDetailFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                        Log.d("--liukai--", "上拉完成");
                    }

                    @Override
                    public void onNext(PsnCrcdAppertainTranQueryResult psnCrcdAppertainTranQueryResult) {
                        Log.d("--liukai--", "上拉成功");
                        attCardTradeDetailView.appertainTranDetailSuccess(psnCrcdAppertainTranQueryResult);
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
                        attCardTradeDetailView.crcdCurrencyQueryFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                        Log.d("--liukai信用卡币种查询--", "完成情况");
                    }

                    @Override
                    public void onNext(PsnCrcdCurrencyQueryResult psnCrcdCurrencyQueryResult) {
                        Log.d("--liukai信用卡币种查询--", "" + psnCrcdCurrencyQueryResult);
                        attCardTradeDetailView.crcdCurrencyQuerySuccess(psnCrcdCurrencyQueryResult);
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
