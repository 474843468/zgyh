package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.TradeQuery.presenter;

import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryHibsExchangeTrans.PsnFessQueryHibsExchangeTransParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryHibsExchangeTrans.PsnFessQueryHibsExchangeTransResult;
import com.boc.bocsoft.mobile.bii.bus.fess.service.FessService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BaseMobileActivity;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.TradeQuery.model.TradeQueryListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.TradeQuery.ui.TradeQueryListContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * 结汇购汇交易查询--历史交易查询列表界面
 * Created by wzn7074 on 2016/12/14.
 */
public class TradeQueryListPresenter extends RxPresenter implements TradeQueryListContract.Presenter{
    //test
    private String wznTag = "wzn7074-TradeQueryListPresenter";
    private int i = 0;

    private TradeQueryListContract.View mTradeQueryListFragmentView;
    private RxLifecycleManager mRxLifecycleManager;
    private GlobalService mGlobalService;
    private FessService mFessService;
    private String conversationId;

    public TradeQueryListPresenter(TradeQueryListContract.View view) {
        this.mTradeQueryListFragmentView = view;
        mTradeQueryListFragmentView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();
        mGlobalService = new GlobalService();
        mFessService = new FessService();

    }

    @Override
    public void psnTradeQueryListQuery(final TradeQueryListModel model) {
        PsnFessQueryHibsExchangeTransParams psnFessQueryHibsExchangeTransParams = new PsnFessQueryHibsExchangeTransParams();
        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();

        mGlobalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnFessQueryHibsExchangeTransResult>>() {
                    @Override
                    public Observable<PsnFessQueryHibsExchangeTransResult> call(String conversation) {
                        //test
                        i++;
                        LogUtils.d(wznTag, "第" + i +"次调用交易列表接口");

                    PsnFessQueryHibsExchangeTransParams params = new PsnFessQueryHibsExchangeTransParams();
                        TradeQueryListPresenter.this.conversationId = conversation;
                        params.setConversationId(conversation);
                        params.setCurrentIndex(model.getCurrentIndex());
                        params.set_refresh("0".equals(model.getCurrentIndex()) ? "true":"false");
                        params.setPageSize(model.getPageSize());
                        params.setStartDate(model.getStartDate());
                        params.setEndDate(model.getEndDate());
                        params.setFessFlag(model.getFessFlag());
                        return mFessService.psnFessQueryHibsExchangeTrans(params);
                    }
                })
                .compose(SchedulersCompat.<PsnFessQueryHibsExchangeTransResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFessQueryHibsExchangeTransResult>() {
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {//拦截无查询结果抛出的错误 自己处理
                        //test
                        LogUtils.d(wznTag,"ErrorCode: " + biiResultErrorException.getErrorCode());
                        LogUtils.d(wznTag,"ErrorMessage: " + biiResultErrorException.getErrorMessage());
                        LogUtils.d(wznTag,"ErrorType: " + biiResultErrorException.getErrorType());

                        if (biiResultErrorException.getErrorCode().equals("HIBS.00002")) {
                            mTradeQueryListFragmentView.psnFessQueryHibsExchangeTransFail(biiResultErrorException);
                        } else {
                            BaseMobileActivity curActivity =
                                    (BaseMobileActivity) ActivityManager.getAppManager().currentActivity();
                            curActivity.showErrorDialog(biiResultErrorException.getErrorMessage());

                        }
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        LogUtils.d(wznTag,"get PsnFessQueryHibsExchangeTrans Exception handler " ); //test
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext( PsnFessQueryHibsExchangeTransResult psnFessQueryHibsExchangeTransResult) {
                        LogUtils.d(wznTag,"get PsnFessQueryHibsExchangeTransDetail success! " ); //test
                        mTradeQueryListFragmentView.psnFessQueryHibsExchangeTransSuccess(
                                generateTradeQueryListModel(model, psnFessQueryHibsExchangeTransResult));

                    }
                });
    }

    //生成结汇购汇交易查询的Model（用result对Model进行填充）
    private TradeQueryListModel generateTradeQueryListModel(TradeQueryListModel model, PsnFessQueryHibsExchangeTransResult result) {

        List<PsnFessQueryHibsExchangeTransResult.FessExchangeTrans> histroyTransList = result.getFessExchangeTransList();
        List<TradeQueryListModel.TradeQueryResultEntity> viewList = new ArrayList<TradeQueryListModel.TradeQueryResultEntity>();

        for (PsnFessQueryHibsExchangeTransResult.FessExchangeTrans openTradEntitiy : histroyTransList) {
            TradeQueryListModel.TradeQueryResultEntity entity = new TradeQueryListModel.TradeQueryResultEntity();

            BeanConvertor.toBean(openTradEntitiy, entity);
            viewList.add(entity);
        }
        model.setList(viewList);
        model.setConversationId(conversationId);
        return model;
    }
}
