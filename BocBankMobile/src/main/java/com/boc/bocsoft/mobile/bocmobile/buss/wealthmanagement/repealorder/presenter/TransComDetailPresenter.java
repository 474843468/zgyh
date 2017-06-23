package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryGuarantyProductResult.PsnXpadQueryGuarantyProductResultParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryGuarantyProductResult.PsnXpadQueryGuarantyProductResultResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRemoveGuarantyProductResult.PsnXpadRemoveGuarantyProductResultParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRemoveGuarantyProductResult.PsnXpadRemoveGuarantyProductResultResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.model.XpadQueryGuarantyProductViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.model.XpadRemoveGuarantyProductViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.ui.TransComDetailContact;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Contact：中银理财-组合购买详情
 * Created by zhx on 2016/9/20
 */
public class TransComDetailPresenter implements TransComDetailContact.Presenter {
    private TransComDetailContact.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private GlobalService globalService;
    private WealthManagementService wealthManagementService;

    private String conversationId;

    public TransComDetailPresenter(TransComDetailContact.View view) {
        this.mView = view;
        mView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();

        globalService = new GlobalService();
        wealthManagementService = new WealthManagementService();
    }

    // 中银理财-组合购买已押押品查询
    @Override
    public void psnXpadQueryGuarantyProductResult(final XpadQueryGuarantyProductViewModel viewModel) {
        ((BussFragment) mView).showLoadingDialog("请稍候...");
        PsnXpadQueryGuarantyProductResultParams params = new PsnXpadQueryGuarantyProductResultParams();
        BeanConvertor.toBean(viewModel, params);

        wealthManagementService.psnXpadQueryGuarantyProductResult(params)
                .compose(SchedulersCompat.<List<PsnXpadQueryGuarantyProductResultResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnXpadQueryGuarantyProductResultResult>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) mView).closeProgressDialog();
                        mView.psnXpadQueryGuarantyProductResultFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(List<PsnXpadQueryGuarantyProductResultResult> results) {
                        ((BussFragment) mView).closeProgressDialog();
                        mView.psnXpadQueryGuarantyProductResultSuccess(generateXpadQueryGuarantyProductViewModel(viewModel, results));

                    }
                });
    }

    // 组合购买解除
    @Override
    public void psnXpadRemoveGuarantyProductResult(final XpadRemoveGuarantyProductViewModel viewModel) {
        ((BussFragment) mView).showLoadingDialog("请稍候...");
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversation) {
                        TransComDetailPresenter.this.conversationId = conversation;
                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        psnGetTokenIdParams.setConversationId(conversation);
                        return globalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnXpadRemoveGuarantyProductResultResult>>() {
                    @Override
                    public Observable<PsnXpadRemoveGuarantyProductResultResult> call(String token) {
                        PsnXpadRemoveGuarantyProductResultParams params = new PsnXpadRemoveGuarantyProductResultParams();
                        BeanConvertor.toBean(viewModel, params);
                        params.setToken(token);
                        params.setConversationId(TransComDetailPresenter.this.conversationId);
                        return wealthManagementService.psnXpadRemoveGuarantyProductResult(params);
                    }
                })
                .compose(SchedulersCompat.<PsnXpadRemoveGuarantyProductResultResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadRemoveGuarantyProductResultResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) mView).closeProgressDialog();
                        mView.psnXpadRemoveGuarantyProductResultFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnXpadRemoveGuarantyProductResultResult result) {
                        ((BussFragment) mView).closeProgressDialog();
                        mView.psnXpadRemoveGuarantyProductResultSuccess(generateXpadRemoveGuarantyProductViewModel(result, viewModel));


                    }
                });
    }

    // 生成“组合购买解除”的ViewModel
    private XpadRemoveGuarantyProductViewModel generateXpadRemoveGuarantyProductViewModel(PsnXpadRemoveGuarantyProductResultResult result, XpadRemoveGuarantyProductViewModel viewModel) {
        viewModel.setTransactionId(result.getTransactionId());

        XpadRemoveGuarantyProductViewModel.GuarantyBuyInfoEntity buyInfoEntity = new XpadRemoveGuarantyProductViewModel.GuarantyBuyInfoEntity();
        BeanConvertor.toBean(result.getGuarantyBuyInfo(), buyInfoEntity);

        viewModel.setGuarantyBuyInfo(buyInfoEntity);
        return viewModel;
    }

    // 生成“组合购买已押押品查询”的ViewModel
    private XpadQueryGuarantyProductViewModel generateXpadQueryGuarantyProductViewModel(XpadQueryGuarantyProductViewModel viewModel, List<PsnXpadQueryGuarantyProductResultResult> results) {
        if (results != null) {
            List<XpadQueryGuarantyProductViewModel.GuarantyProductEntity> viewList = new ArrayList<XpadQueryGuarantyProductViewModel.GuarantyProductEntity>();
            for (PsnXpadQueryGuarantyProductResultResult result : results) {
                XpadQueryGuarantyProductViewModel.GuarantyProductEntity guarantyProductEntity = new XpadQueryGuarantyProductViewModel.GuarantyProductEntity();
                BeanConvertor.toBean(result, guarantyProductEntity);

                viewList.add(guarantyProductEntity);
            }
            viewModel.setList(viewList);
        }
        return viewModel;
    }

    @Override
    public void subscribe() {
        //TODO onResume时需要做的工作
    }

    @Override
    public void unsubscribe() {
        //TODO 防止外界已经销毁，而后台线程的任务还在执行
        mRxLifecycleManager.onDestroy();
    }
}
