package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGGetRegCurrency.PsnVFGGetRegCurrencyParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeInfoQuery.PsnVFGTradeInfoQueryParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeInfoQuery.PsnVFGTradeInfoQueryResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.service.LongShortForexService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BaseMobileActivity;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadPsnVFGGetRegCurrencyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadVFGTradeInfoQueryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.ui.OpenTradingContract;
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
 * 双向宝交易查询--未平仓交易查询列表界面
 * Created by zc on 2016/11/17
 */
public class OpenTradingPresenter extends RxPresenter implements OpenTradingContract.Presenter {

    private OpenTradingContract.View mOpenTradingView;
    private RxLifecycleManager mRxLifecycleManager;
    private GlobalService globalService;
    private LongShortForexService mLongShortForexService;
    public String conversationId;//会话ID，与列表查询ID相同

    public OpenTradingPresenter(OpenTradingContract.View view) {
        this.mOpenTradingView = view;
        mOpenTradingView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        mLongShortForexService = new LongShortForexService();

    }

    /**
     * 双向宝结算币种查询
     * @param viewmodel
     */
    @Override
    public void psnXpadGetRegCurrency(final XpadPsnVFGGetRegCurrencyModel viewmodel) {
        PsnVFGGetRegCurrencyParams psnVFGGetRegCurrencyParams = new PsnVFGGetRegCurrencyParams();

        mLongShortForexService.psnXpadGetRegCurrency(psnVFGGetRegCurrencyParams)
                .compose(SchedulersCompat.<List<String>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<String>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        //                        ((BussFragment) mWealthHistoryListView).closeProgressDialog();

                        mOpenTradingView.psnXpadGetRegCurrencyFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(List<String> psnVFGGetRegCurrencyResults) {
                        mOpenTradingView.psnXpadGetRegCurrencySuccess(psnVFGGetRegCurrencyResults);
                    }
                });
    }
    /**
     *双向宝交易查询——未平仓交易查询
     */
    public void psnXpadOpentradingQuery(final XpadVFGTradeInfoQueryModel viewModel){

        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnVFGTradeInfoQueryResult>>() {
                    @Override
                    public Observable<PsnVFGTradeInfoQueryResult> call(String conversation) {
                        PsnVFGTradeInfoQueryParams params= new PsnVFGTradeInfoQueryParams();
                        OpenTradingPresenter.this.conversationId = conversation;
                        params.setConversationId(conversation);
                        params.setCurrentIndex(viewModel.getCurrentIndex());
                        params.set_refresh("0".equals(viewModel.getCurrentIndex()) ? "true":"false");
                        params.setPageSize(viewModel.getPageSize());
                        params.setCurrencyCode(viewModel.getCurrencyCode());
                        params.setQueryType(viewModel.getQueryType());
                        return mLongShortForexService.psnXpadTradeInfoQuery(params);
                    }
                })
                .compose(SchedulersCompat.<PsnVFGTradeInfoQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnVFGTradeInfoQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mOpenTradingView.psnXpadOpentradingQueryFail(biiResultErrorException);
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        if ("validation.no.relating.acc".equals(biiResultErrorException.getErrorCode())){
                            mOpenTradingView.psnXpadOpentradingQueryFail(biiResultErrorException);
                        }else {
                            BaseMobileActivity curActivity =
                                    (BaseMobileActivity) ActivityManager.getAppManager().currentActivity();
                            curActivity.showErrorDialog(biiResultErrorException.getErrorMessage());
                        }
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnVFGTradeInfoQueryResult psnVFGTradeInfoQueryResult) {
                        mOpenTradingView.psnXpadOpentradingQuerySuccess(generateXpadVFGTradeInfoQueryModel(viewModel,psnVFGTradeInfoQueryResult));
                    }
                });
    }

    // 生成未平仓交易查询的ViewModel（用result对ViewModel进行填充）
    private XpadVFGTradeInfoQueryModel generateXpadVFGTradeInfoQueryModel(XpadVFGTradeInfoQueryModel viewModel, PsnVFGTradeInfoQueryResult result) {
        List<PsnVFGTradeInfoQueryResult.XpadPsnVFGTradeInfoQueryResultEntity> historyEntrustList = result.getList();
        List<XpadVFGTradeInfoQueryModel.XpadPsnVFGTradeInfoQueryResultEntity> viewList = new ArrayList<XpadVFGTradeInfoQueryModel.XpadPsnVFGTradeInfoQueryResultEntity>();

        for (PsnVFGTradeInfoQueryResult.XpadPsnVFGTradeInfoQueryResultEntity openTradEntitiy : historyEntrustList) {
            XpadVFGTradeInfoQueryModel.XpadPsnVFGTradeInfoQueryResultEntity entitiy = new XpadVFGTradeInfoQueryModel.XpadPsnVFGTradeInfoQueryResultEntity();
            XpadVFGTradeInfoQueryModel.XpadPsnVFGTradeInfoQueryResultEntity.Currency1Entity currency1 = new XpadVFGTradeInfoQueryModel.XpadPsnVFGTradeInfoQueryResultEntity.Currency1Entity();
            XpadVFGTradeInfoQueryModel.XpadPsnVFGTradeInfoQueryResultEntity.Currency2Entity currency2 = new XpadVFGTradeInfoQueryModel.XpadPsnVFGTradeInfoQueryResultEntity.Currency2Entity();

            BeanConvertor.toBean(openTradEntitiy, entitiy);
            BeanConvertor.toBean(openTradEntitiy.getCurrency1(),currency1);
            BeanConvertor.toBean(openTradEntitiy.getCurrency2(),currency2);

            entitiy.setCurrency1(currency1);
            entitiy.setCurrency2(currency2);
            viewList.add(entitiy);
        }
        viewModel.setList(viewList);
        viewModel.setRecordNumber(result.getRecordNumber());
        viewModel.setConversationId(conversationId);
        return viewModel;
    }

}
