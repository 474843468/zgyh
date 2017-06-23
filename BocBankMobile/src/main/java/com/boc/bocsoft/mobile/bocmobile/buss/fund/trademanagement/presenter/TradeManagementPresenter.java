package com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.presenter;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryHistoryDetail.PsnFundQueryHistoryDetailParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryHistoryDetail.PsnFundQueryHistoryDetailResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryTransOntran.PsnFundQueryTransOntranParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryTransOntran.PsnFundQueryTransOntranResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.model.PsnFundQueryHistoryDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.model.PsnFundQueryTransOntranModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.ui.TradeManagementContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by wy7105 on 2016/12/1.
 * 交易记录页面presenter
 */

public class TradeManagementPresenter extends RxPresenter implements TradeManagementContract.Presenter {

    private TradeManagementContract.View mTradeManagementView;
    private RxLifecycleManager mRxLifecycleManager;
    private GlobalService globalService;
    private FundService fundService;

    public TradeManagementPresenter(TradeManagementContract.View view) {
        mTradeManagementView = view;
        mTradeManagementView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        fundService = new FundService();
    }

    /**
     * 请求I41-046查询在途交易信息接口
     */
    @Override
    public void psnFundQueryTransOntran(final PsnFundQueryTransOntranModel model) {
        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnFundQueryTransOntranResult>>() {
                    @Override
                    public Observable<PsnFundQueryTransOntranResult> call(String conversation) {
                        PsnFundQueryTransOntranParams params = new PsnFundQueryTransOntranParams();
                        params.setRefresh(model.get_refresh());
                        params.setCurrentIndex(model.getCurrentIndex());
                        params.setPageSize(model.getPageSize());
                        params.setConversationId(conversation);
                        return fundService.psnFundQueryTransOntran(params);
                    }
                })
                .compose(SchedulersCompat.<PsnFundQueryTransOntranResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnFundQueryTransOntranResult>() {
                               @Override
                               public void handleException(BiiResultErrorException biiResultErrorException) {
                                   mTradeManagementView.psnFundQueryTransOntranFail(biiResultErrorException);
                               }

                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onNext(PsnFundQueryTransOntranResult result) {
                                   mTradeManagementView.psnFundQueryTransOntranSuccess(generatePsnFundQueryTransOntranModel(model, result));
                               }
                           }
                );
    }

    /**
     * 生成"查询在途交易列表"的viewmodel
     */
    private PsnFundQueryTransOntranModel generatePsnFundQueryTransOntranModel(PsnFundQueryTransOntranModel viewModel, PsnFundQueryTransOntranResult result) {
        List<PsnFundQueryTransOntranModel.ListBean> viewList = new ArrayList<PsnFundQueryTransOntranModel.ListBean>();
        List<PsnFundQueryTransOntranResult.ListBean> list = result.getList();
        for (PsnFundQueryTransOntranResult.ListBean List : list) {
            PsnFundQueryTransOntranModel.ListBean productList = new PsnFundQueryTransOntranModel.ListBean();
            BeanConvertor.toBean(List, productList);
            viewList.add(productList);
        }
        viewModel.setList(viewList);
        viewModel.setRecordNumber(result.getRecordNumber());

        return viewModel;
    }

    /**
     * 请求I41-047查询历史交易信息接口
     */
    @Override
    public void psnFundQueryHistoryDetail(final PsnFundQueryHistoryDetailModel model) {
        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnFundQueryHistoryDetailResult>>() {
                    @Override
                    public Observable<PsnFundQueryHistoryDetailResult> call(String conversation) {
                        PsnFundQueryHistoryDetailParams params = new PsnFundQueryHistoryDetailParams();
                        params.set_refresh(model.get_refresh());
                        params.setCurrentIndex(model.getCurrentIndex());
                        params.setPageSize(model.getPageSize());
                        params.setEndDate(model.getEndDate());
                        params.setStartDate(model.getStartDate());
                        params.setTransType(model.getTransType());
                        params.setConversationId(conversation);
                        return fundService.psnFundQueryHistoryDetail(params);
                    }
                })
                .compose(SchedulersCompat.<PsnFundQueryHistoryDetailResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnFundQueryHistoryDetailResult>() {
                               @Override
                               public void handleException(BiiResultErrorException biiResultErrorException) {
                                   mTradeManagementView.psnFundQueryHistoryDetailFail(biiResultErrorException);
                               }

                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onNext(PsnFundQueryHistoryDetailResult result) {
                                   mTradeManagementView.psnFundQueryHistoryDetailSuccess(generatePsnFundQueryHistoryDetailModel(model, result));
                               }
                           }
                );
    }

    /**
     * 生成"查询历史交易列表"的viewmodel
     */
    private PsnFundQueryHistoryDetailModel generatePsnFundQueryHistoryDetailModel(PsnFundQueryHistoryDetailModel viewModel, PsnFundQueryHistoryDetailResult result) {
        List<PsnFundQueryHistoryDetailModel.ListEntity> viewList = new ArrayList<PsnFundQueryHistoryDetailModel.ListEntity>();
        List<PsnFundQueryHistoryDetailResult.ListEntity> list = result.getList();
        for (PsnFundQueryHistoryDetailResult.ListEntity List : list) {
            PsnFundQueryHistoryDetailModel.ListEntity productList = new PsnFundQueryHistoryDetailModel.ListEntity();
            BeanConvertor.toBean(List, productList);
            viewList.add(productList);
        }
        viewModel.setList(viewList);
        viewModel.setRecordNumber(result.getRecordNumber());

        return viewModel;
    }
}