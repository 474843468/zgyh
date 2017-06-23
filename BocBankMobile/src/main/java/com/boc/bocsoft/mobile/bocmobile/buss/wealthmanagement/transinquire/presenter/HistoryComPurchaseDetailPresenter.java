package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryGuarantyProductResult.PsnXpadQueryGuarantyProductResultParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryGuarantyProductResult.PsnXpadQueryGuarantyProductResultResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadQueryGuarantyProductResultModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.ui.HistoryComPurchaseDetailContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
/**
 * * Fragment：中银理财-历史交易-组合购买交易详情
 * Created by zc on 2016/9/12
 *
 */
public class HistoryComPurchaseDetailPresenter extends RxPresenter implements HistoryComPurchaseDetailContract.Presenter {

    private HistoryComPurchaseDetailContract.View mHistoryComPurchaseDetailView;
    private RxLifecycleManager mRxLifecycleManager;
    private GlobalService globalService;
    private WealthManagementService wealthManagementService;

    public HistoryComPurchaseDetailPresenter(HistoryComPurchaseDetailContract.View view) {
        mHistoryComPurchaseDetailView = view;
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        wealthManagementService = new WealthManagementService();
    }

    //查询组合购买交易详情
    @Override
    public void psnXpadQueryCombinationGuarantyProductResult(final XpadQueryGuarantyProductResultModel viewModel) {
        ((BussFragment) mHistoryComPurchaseDetailView).showLoadingDialog("请稍候...");

        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<List<PsnXpadQueryGuarantyProductResultResult>>>() {
                    @Override
                    public Observable<List<PsnXpadQueryGuarantyProductResultResult>> call(String conversation) {
                        PsnXpadQueryGuarantyProductResultParams psnXpadQueryGuarantyProductResultParams = new PsnXpadQueryGuarantyProductResultParams();
//                        Log.i("zc111","----------"+viewModel.getCurCode());
                        psnXpadQueryGuarantyProductResultParams.setAccountKey(viewModel.getAccountKey());
                        psnXpadQueryGuarantyProductResultParams.setTranSeq(viewModel.getTranSeq());
                        psnXpadQueryGuarantyProductResultParams.setIbknum(viewModel.getIbknum());
                        psnXpadQueryGuarantyProductResultParams.setTypeOfAccount(viewModel.getTypeOfAccount());
                        return wealthManagementService.psnXpadQueryGuarantyProductResult(psnXpadQueryGuarantyProductResultParams);
                    }
                })
                .compose(SchedulersCompat.<List<PsnXpadQueryGuarantyProductResultResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnXpadQueryGuarantyProductResultResult>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) mHistoryComPurchaseDetailView).closeProgressDialog();
                        mHistoryComPurchaseDetailView.psnXpadQueryCombinationGuarantyProductFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(List<PsnXpadQueryGuarantyProductResultResult> result) {
                        ((BussFragment) mHistoryComPurchaseDetailView).closeProgressDialog();

                        List<XpadQueryGuarantyProductResultModel.QueryGuarantyProductResultEntity> results = new ArrayList<>();
                        for(PsnXpadQueryGuarantyProductResultResult item:result){
                            XpadQueryGuarantyProductResultModel.QueryGuarantyProductResultEntity entity =new XpadQueryGuarantyProductResultModel.QueryGuarantyProductResultEntity();
                            entity.setProdName(item.getProdName());
                            entity.setFreezeUnit(item.getFreezeUnit());
                            entity.setImpawnPermit(item.getImpawnPermit());
                            results.add(entity);
                        }
                        mHistoryComPurchaseDetailView.psnXpadQueryCombinationProductComSuccess(results);
                    }
                });
    }
}
