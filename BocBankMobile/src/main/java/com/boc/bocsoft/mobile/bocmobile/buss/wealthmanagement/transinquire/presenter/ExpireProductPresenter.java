package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadDueProductProfitQuery.PsnXpadDueProductProfitQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadDueProductProfitQuery.PsnXpadDueProductProfitQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRecentAccountQuery.PsnXpadRecentAccountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRecentAccountQuery.PsnXpadRecentAccountQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadAccountQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadDueProductProfitQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadRecentAccountQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.ui.ExpireProductContact;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Presenter：中银理财-交易查询-已到期产品页面
 * Created by zhx on 2016/9/18
 */
public class ExpireProductPresenter implements ExpireProductContact.Presenter {
    private ExpireProductContact.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private GlobalService globalService;
    private WealthManagementService wealthManagementService;

    public ExpireProductPresenter(ExpireProductContact.View view) {
        this.mView = view;
        mView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();

        globalService = new GlobalService();
        wealthManagementService = new WealthManagementService();
    }

    @Override
    public void psnXpadAccountQuery(final XpadAccountQueryViewModel xpadAccountQueryViewModel) {
//        ((BussFragment) mView).showLoadingDialog("请稍候...");
        PsnXpadAccountQueryParams psnXpadAccountQueryParams = buildPsnXpadAccountQueryParams(xpadAccountQueryViewModel);

        wealthManagementService.psnXpadAccountQuery(psnXpadAccountQueryParams)
                .compose(SchedulersCompat.<PsnXpadAccountQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadAccountQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
//                        ((BussFragment) mView).closeProgressDialog();
                        //                        mView.psnXpadAccountQueryFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnXpadAccountQueryResult psnXpadAccountQueryResult) {
//                        ((BussFragment) mView).closeProgressDialog();
                        mView.psnXpadAccountQuerySuccess(generateXpadAccountQueryViewModel(xpadAccountQueryViewModel, psnXpadAccountQueryResult));


                    }
                });
    }

    @Override
    public void psnXpadRecentAccountQuery(final XpadRecentAccountQueryViewModel xpadRecentAccountQueryViewModel) {
//        ((BussFragment) mView).showLoadingDialog("请稍候...");

        PsnXpadRecentAccountQueryParams psnXpadRecentAccountQueryParams = new PsnXpadRecentAccountQueryParams();
        wealthManagementService.psnXpadRecentAccountQuery(psnXpadRecentAccountQueryParams)
                .compose(SchedulersCompat.<PsnXpadRecentAccountQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadRecentAccountQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
//                        ((BussFragment) mView).closeProgressDialog();

                        mView.psnXpadRecentAccountQueryFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnXpadRecentAccountQueryResult psnXpadAccountQueryResult) {
//                        ((BussFragment) mView).closeProgressDialog();

                        BeanConvertor.toBean(psnXpadAccountQueryResult, xpadRecentAccountQueryViewModel);

                        mView.psnXpadRecentAccountQuerySuccess(xpadRecentAccountQueryViewModel);
                    }
                });
    }

    @Override
    public void psnXpadDueProductProfitQuery(final XpadDueProductProfitQueryViewModel xpadDueProductProfitQueryViewModel) {
//        ((BussFragment) mView).showLoadingDialog("请稍候...");

        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnXpadDueProductProfitQueryResult>>() {
                    @Override
                    public Observable<PsnXpadDueProductProfitQueryResult> call(String conversation) {
                        PsnXpadDueProductProfitQueryParams params = new PsnXpadDueProductProfitQueryParams();
                        params.setAccountKey(xpadDueProductProfitQueryViewModel.getAccountKey());
                        params.setCurrentIndex(xpadDueProductProfitQueryViewModel.getCurrentIndex());
                        params.setPageSize(xpadDueProductProfitQueryViewModel.getPageSize());
                        params.setStartDate(xpadDueProductProfitQueryViewModel.getStartDate());
                        params.setEndDate(xpadDueProductProfitQueryViewModel.getEndDate());
                        params.setCurrency(xpadDueProductProfitQueryViewModel.getCurrency());
                        params.setConversationId(conversation);
                        params.set_refresh(xpadDueProductProfitQueryViewModel.get_refresh());
                        return wealthManagementService.psnXpadDueProductProfitQuery(params);
                    }
                })
                .compose(SchedulersCompat.<PsnXpadDueProductProfitQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadDueProductProfitQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
//                        ((BussFragment) mView).closeProgressDialog();
                        mView.psnXpadDueProductProfitQueryFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnXpadDueProductProfitQueryResult result) {
//                        ((BussFragment) mView).closeProgressDialog();

                        mView.psnXpadDueProductProfitQuerySuccess(generateXpadDueProductProfitQueryViewModel(xpadDueProductProfitQueryViewModel, result));


                    }
                });
    }

    // 生成"查询客户理财账户信息"的请求参数
    private XpadDueProductProfitQueryViewModel generateXpadDueProductProfitQueryViewModel(XpadDueProductProfitQueryViewModel viewModel, PsnXpadDueProductProfitQueryResult result) {
        List<XpadDueProductProfitQueryViewModel.DueProductEntity> viewList = new ArrayList<XpadDueProductProfitQueryViewModel.DueProductEntity>();
        List<PsnXpadDueProductProfitQueryResult.DueProductEntity> list = result.getList();
        for (PsnXpadDueProductProfitQueryResult.DueProductEntity entity : list) {
            XpadDueProductProfitQueryViewModel.DueProductEntity productEntity = new XpadDueProductProfitQueryViewModel.DueProductEntity();
            BeanConvertor.toBean(entity, productEntity);
            viewList.add(productEntity);
        }

        viewModel.setList(viewList);
        viewModel.setRecordNumber(result.getRecordNumber());

        return viewModel;
    }


    // 生成"查询客户理财账户信息"的请求参数
    private PsnXpadAccountQueryParams buildPsnXpadAccountQueryParams(XpadAccountQueryViewModel xpadAccountQueryViewModel) {
        PsnXpadAccountQueryParams psnXpadAccountQueryParams = new PsnXpadAccountQueryParams();
        psnXpadAccountQueryParams.setXpadAccountSatus(xpadAccountQueryViewModel.getXpadAccountSatus());
        psnXpadAccountQueryParams.setQueryType(xpadAccountQueryViewModel.getQueryType());
        return psnXpadAccountQueryParams;
    }

    // 生成"查询客户理财账户信息"的ViewModel
    private XpadAccountQueryViewModel generateXpadAccountQueryViewModel(XpadAccountQueryViewModel xpadAccountQueryViewModel, PsnXpadAccountQueryResult psnXpadAccountQueryResult) {
        List<PsnXpadAccountQueryResult.XPadAccountEntity> list = psnXpadAccountQueryResult.getList();
        List<XpadAccountQueryViewModel.XPadAccountEntity> modelList = new ArrayList<XpadAccountQueryViewModel.XPadAccountEntity>();

        for (PsnXpadAccountQueryResult.XPadAccountEntity accountEntity : list) {
            XpadAccountQueryViewModel.XPadAccountEntity padAccountEntity = new XpadAccountQueryViewModel.XPadAccountEntity();
            BeanConvertor.toBean(accountEntity, padAccountEntity);

            modelList.add(padAccountEntity);
        }

        xpadAccountQueryViewModel.setList(modelList);
        return xpadAccountQueryViewModel;
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