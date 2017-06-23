package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.TradeQuery.presenter;


import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryForErrormesg.PsnFessQueryForErrormesgParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryForErrormesg.PsnFessQueryForErrormesgResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryHibsExchangeTransDetail.PsnFessQueryHibsExchangeTransDetailParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryHibsExchangeTransDetail.PsnFessQueryHibsExchangeTransDetailResult;
import com.boc.bocsoft.mobile.bii.bus.fess.service.FessService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.TradeQuery.model.TradeQueryTransDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.TradeQuery.ui.TradeQueryDetailContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 *结汇购汇交易明细查询
 * Created by wzn7074 on 2016/12/1.
 */
public class TradeQueryDetailPresenter extends RxPresenter implements TradeQueryDetailContract.Presenter {

    private String wznTag = "wzn7074-TradeQueryDetailPresenter";  //test

    private String conversationId; //会话ID
    private RxLifecycleManager mRxLifecycleManager;
    private FessService mFessService;
    private GlobalService mGlobalService;

    private TradeQueryDetailContract.View mView;
    private TradeQueryTransDetailModel mModel;

    //装载Presenter，Presenter持有View
    public TradeQueryDetailPresenter(TradeQueryDetailContract.View view) {
        LogUtils.d(wznTag, "new a presenter"); //test

        mRxLifecycleManager = new RxLifecycleManager();
        mFessService = new FessService();
        mGlobalService = new GlobalService();

        mView = view;
        mModel = new TradeQueryTransDetailModel();
        view.setPresenter(this);
    }

//    // 生成请求参数
//    private PsnFessQueryHibsExchangeTransDetailParams generateRequestParams() {
//        PsnFessQueryHibsExchangeTransDetailParams params = new PsnFessQueryHibsExchangeTransDetailParams();
//        params.setBankSelfNum(mBankSelfNum);
//        params.setPaymentDate(mPaymentDate);
//        params.setRefNum(mRefNum);
//        return params;
//    }


    @Override
    public void queryTransDetail(final TradeQueryTransDetailModel model) {

        LogUtils.d(wznTag, "try to get TransDetail "); //test

        ((BussFragment) mView).showLoadingDialog("请稍候...");

        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();

        mGlobalService.psnCreatConversation(psnCreatConversationParams)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnFessQueryHibsExchangeTransDetailResult>>() {
                    @Override
                    public Observable<PsnFessQueryHibsExchangeTransDetailResult> call(String conversationId) {
                        PsnFessQueryHibsExchangeTransDetailParams mPsnFessQueryHibsExchangeTransDetailParams = new PsnFessQueryHibsExchangeTransDetailParams();

                        /**
                         * PsnFessQueryHibsExchangeTransDetail上送字段
                         * 需要与“PsnFessQueryHibsExchangeTrans 查询全渠道结购汇交易列表”接口共用同一个conversation
                         */
                        mPsnFessQueryHibsExchangeTransDetailParams.setConversationId(model.getConversationId());
                        mPsnFessQueryHibsExchangeTransDetailParams.setBankSelfNum(model.getBankSelfNum());
                        mPsnFessQueryHibsExchangeTransDetailParams.setPaymentDate(model.getPaymentDate());
                        mPsnFessQueryHibsExchangeTransDetailParams.setRefNum(model.getRefNum());

                        //test
//                        LogUtils.d(wznTag,"TransDetailParams.getConversationId() "+ mPsnFessQueryHibsExchangeTransDetailParams.getConversationId()+"model.getConversationId()" +model.getConversationId()); //test
//                        LogUtils.d(wznTag,"TransDetailParams.getBankSelfNum() "+ mPsnFessQueryHibsExchangeTransDetailParams.getBankSelfNum() +"model.getBankSelfNum()" + model.getBankSelfNum()); //test
//                        LogUtils.d(wznTag,"TransDetailParams.getPaymentDate() "+ mPsnFessQueryHibsExchangeTransDetailParams.getPaymentDate() + "model.getPaymentDate() "+model.getPaymentDate()); //test
//                        LogUtils.d(wznTag,"TransDetailParams.getRefNum() "+ mPsnFessQueryHibsExchangeTransDetailParams.getRefNum() +"model.getRefNum()" +model.getRefNum() ); //test
                        LogUtils.d(wznTag, "try to get TransDetail "); //test

                        return mFessService.psnFessQueryHibsExchangeTransDetail(mPsnFessQueryHibsExchangeTransDetailParams);
                    }
                })
                .compose(SchedulersCompat.<PsnFessQueryHibsExchangeTransDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFessQueryHibsExchangeTransDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                        LogUtils.d(wznTag, "get TransDetail Exception Handler! ");

                        mView.psnFessQueryHibsExchangeTransDetailFail(biiResultErrorException);
                        // TODO: 2016/12/21 回调失败方法

                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnFessQueryHibsExchangeTransDetailResult result) {
                        LogUtils.d(wznTag, "get TransDetail success! "); //test
//                        mModel.setAccountNumber(result.getAccountNumber());
//                        mModel.setCurrencyCode(result.getCurrencyCode());
//                        mModel.setCashRemit(result.getCashRemit());
//                        mModel.setAmount(result.getAmount());
//                        mModel.setStatus(result.getAmount());
//                        mModel.setExchangeRate(result.getExchangeRate());
//                        mModel.setReturnCnyAmt(result.getReturnCnyAmt());
//                        mModel.setTransType(result.getTransType());
//                        mModel.setPaymentDate(result.getPaymentDate());
//                        mModel.setPaymentTime(result.getPaymentTime());
//                        mModel.setFurInfo(result.getFurInfo());
//                        mModel.setChannel(result.getChannel());
                        mView.psnFessQueryHibsExchangeTransDetailSuccess(result); //// TODO: 2016/12/21 回调成功方法
                    }
                });
    }

    @Override
    public void queryTranRetMesg(final TradeQueryTransDetailModel model) {
        //test
        LogUtils.d(wznTag,"try to get TransDetail");

        ((BussFragment) mView).showLoadingDialog("请稍候...");

        PsnFessQueryForErrormesgParams psnFessQueryForErrormesgParams = new PsnFessQueryForErrormesgParams();

        psnFessQueryForErrormesgParams.setTranRetCode(model.getTranRetCode());

        mFessService.psnFessQueryForErrormesg(psnFessQueryForErrormesgParams)
                .compose(this.<PsnFessQueryForErrormesgResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFessQueryForErrormesgResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFessQueryForErrormesgResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        //test
                        LogUtils.d(wznTag, "get Errormesg Exception Handler! ");

                        mView.psnFessQueryForErrormesgFail(biiResultErrorException);

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFessQueryForErrormesgResult result) {
                        //test
                        LogUtils.d(wznTag, "get Errormesg success! ");

                        mView.psnFessQueryForErrormesgSuccess(result);

                    }
                });

    }
}

