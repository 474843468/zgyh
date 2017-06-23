package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHisTradStatus.PsnXpadHisTradStatusParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHisTradStatus.PsnXpadHisTradStatusResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryHisGuarantyProductListResult.PsnXpadQueryHisGuarantyProductListResultParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryHisGuarantyProductListResult.PsnXpadQueryHisGuarantyProductListResultResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRecentAccountQuery.PsnXpadRecentAccountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRecentAccountQuery.PsnXpadRecentAccountQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadAccountQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadHisTradStatusViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadQueryGuarantyProductListViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadRecentAccountQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.ui.WealthHistoryListContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;
import com.tencent.mm.sdk.platformtools.Log;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Presenter：中银理财-历史交易列表
 * Created by zc on 2016/9/14
 */
public class WealthHistoryListPresenter extends RxPresenter implements WealthHistoryListContract.Presenter {

    private WealthHistoryListContract.View mWealthHistoryListView;
    private RxLifecycleManager mRxLifecycleManager;
    private GlobalService globalService;
    private WealthManagementService wealthManagementService;

    public WealthHistoryListPresenter(WealthHistoryListContract.View view) {
        this.mWealthHistoryListView = view;
        mWealthHistoryListView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();
        wealthManagementService = new WealthManagementService();
        globalService = new GlobalService();

    }

    // 中银理财-交易查询-历史交易（委托常规交易状况查询）
    @Override
    public void psnXpadHisTradStatus(final XpadHisTradStatusViewModel viewModel) {
//        ((BussFragment) mWealthHistoryListView).showLoadingDialog("请稍候...");

        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnXpadHisTradStatusResult>>() {
                    @Override
                    public Observable<PsnXpadHisTradStatusResult> call(String conversation) {
                        PsnXpadHisTradStatusParams psnXpadHisTradStatusParams = new PsnXpadHisTradStatusParams();
                        psnXpadHisTradStatusParams.setConversationId(conversation);
                        Log.i("zc111","----------"+viewModel.getStartDate().toString());
                        psnXpadHisTradStatusParams.setPageSize(viewModel.getPageSize());
                        psnXpadHisTradStatusParams.set_refresh(viewModel.getCurrentIndex().equals("0") ? "true":"false");
                        psnXpadHisTradStatusParams.setCurrentIndex(viewModel.getCurrentIndex());
                        psnXpadHisTradStatusParams.setAccountType(viewModel.getAccountType());
                        psnXpadHisTradStatusParams.setAccountKey(viewModel.getAccountKey());
                        psnXpadHisTradStatusParams.setXpadProductCurCode(viewModel.getXpadProductCurCode());
                        psnXpadHisTradStatusParams.setStartDate(viewModel.getStartDate().toString());
                        psnXpadHisTradStatusParams.setEndDate(viewModel.getEndDate().toString());
                        return wealthManagementService.psnXpadHisTradStatus(psnXpadHisTradStatusParams);
                    }
                })
                .compose(SchedulersCompat.<PsnXpadHisTradStatusResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadHisTradStatusResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
//                        ((BussFragment) mWealthHistoryListView).closeProgressDialog();
                        mWealthHistoryListView.psnXpadHisTradStatusFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnXpadHisTradStatusResult psnXpadHisTradStatusResult) {
//                        ((BussFragment) mWealthHistoryListView).closeProgressDialog();
                        mWealthHistoryListView.psnXpadHisTradStatusSuccess(generateXpadHisTradStatusViewModel(viewModel, psnXpadHisTradStatusResult));
                        //                        Log.i("zc111","----------"+viewModel.);
                        System.out.println("查询结束，初始化");
                    }
                });
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mRxLifecycleManager.onDestroy();
    }

    // 查询账户列表
    @Override
    public void psnXpadAccountQuery(final XpadAccountQueryViewModel xpadAccountQueryViewModel) {
//        ((BussFragment) mWealthHistoryListView).showLoadingDialog("请稍候...");
        PsnXpadAccountQueryParams psnXpadAccountQueryParams = buildPsnXpadAccountQueryParams(xpadAccountQueryViewModel);

        wealthManagementService.psnXpadAccountQuery(psnXpadAccountQueryParams)
                .compose(SchedulersCompat.<PsnXpadAccountQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadAccountQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
//                        ((BussFragment) mWealthHistoryListView).closeProgressDialog();
                        //                        mView.psnXpadAccountQueryFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnXpadAccountQueryResult psnXpadAccountQueryResult) {
//                        ((BussFragment) mWealthHistoryListView).closeProgressDialog();
                        mWealthHistoryListView.psnXpadAccountQuerySuccess(generateXpadAccountQueryViewModel(xpadAccountQueryViewModel, psnXpadAccountQueryResult));


                    }
                });

    }

    // 查询最近操作账户查询
    @Override
    public void psnXpadRecentAccountQuery(final XpadRecentAccountQueryViewModel xpadRecentAccountQueryViewModel) {
//        ((BussFragment) mWealthHistoryListView).showLoadingDialog("请稍候...");

        PsnXpadRecentAccountQueryParams psnXpadRecentAccountQueryParams = new PsnXpadRecentAccountQueryParams();
        wealthManagementService.psnXpadRecentAccountQuery(psnXpadRecentAccountQueryParams)
                .compose(SchedulersCompat.<PsnXpadRecentAccountQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadRecentAccountQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
//                        ((BussFragment) mWealthHistoryListView).closeProgressDialog();

                        mWealthHistoryListView.psnXpadRecentAccountQueryFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnXpadRecentAccountQueryResult psnXpadAccountQueryResult) {
//                        ((BussFragment) mWealthHistoryListView).closeProgressDialog();
                        BeanConvertor.toBean(psnXpadAccountQueryResult, xpadRecentAccountQueryViewModel);
                        mWealthHistoryListView.psnXpadRecentAccountQuerySuccess(xpadRecentAccountQueryViewModel);
                    }
                });
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


    //这里还有问题，没有这个Model，组合购买，自己写的Model
    @Override
    public void psnXpadHisComTradStatus(final XpadQueryGuarantyProductListViewModel viewModel) {
//        ((BussFragment) mWealthHistoryListView).showLoadingDialog("请稍候...");

        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnXpadQueryHisGuarantyProductListResultResult>>() {
                         @Override
                         public Observable<PsnXpadQueryHisGuarantyProductListResultResult> call(String conversation) {
                             PsnXpadQueryHisGuarantyProductListResultParams psnXpadQueryHisGuarantyParams = new PsnXpadQueryHisGuarantyProductListResultParams();
                             Log.i("zc111","----------"+viewModel.getStartDate().toString());
                             psnXpadQueryHisGuarantyParams.setPageSize(viewModel.getPageSize());
                             psnXpadQueryHisGuarantyParams.setCurrentIndex(viewModel.getCurrentIndex());
                             psnXpadQueryHisGuarantyParams.setAccountKey(viewModel.getAccountKey());
                             psnXpadQueryHisGuarantyParams.set_refresh(viewModel.getCurrentIndex().equals("0") ? "true":"false");
                             psnXpadQueryHisGuarantyParams.setXpadProductCurCode(viewModel.getXpadProductCurCode());
                             psnXpadQueryHisGuarantyParams.setStartDate(viewModel.getStartDate().toString());
                             psnXpadQueryHisGuarantyParams.setEndDate(viewModel.getEndDate().toString());
                             psnXpadQueryHisGuarantyParams.setConversationId(conversation);

                             return wealthManagementService.psnXpadQueryHisGuarantyProductState(psnXpadQueryHisGuarantyParams);
                            }
                        })
                .compose(SchedulersCompat.<PsnXpadQueryHisGuarantyProductListResultResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadQueryHisGuarantyProductListResultResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
//                        ((BussFragment) mWealthHistoryListView).closeProgressDialog();

                        mWealthHistoryListView.psnXpadHisComTradStatusFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnXpadQueryHisGuarantyProductListResultResult psnXpadHisComTradStatusResult) {
//                        ((BussFragment) mWealthHistoryListView).closeProgressDialog();
                        mWealthHistoryListView.psnXpadHisComTradStatusSuccess(generateXpadHisComTradStatusViewModel(viewModel, psnXpadHisComTradStatusResult));

                    }
                });
    }

    // 生成“历史组合交易状况查询”的ViewModel（用result对ViewModel进行填充）,
    private XpadQueryGuarantyProductListViewModel generateXpadHisComTradStatusViewModel(XpadQueryGuarantyProductListViewModel viewModel, PsnXpadQueryHisGuarantyProductListResultResult result) {
        List<PsnXpadQueryHisGuarantyProductListResultResult.ListEntity> hisComTradList = result.getList();
        List<XpadQueryGuarantyProductListViewModel.QueryGuarantyProductListEntity> viewList = new ArrayList<XpadQueryGuarantyProductListViewModel.QueryGuarantyProductListEntity>();

        for (PsnXpadQueryHisGuarantyProductListResultResult.ListEntity hisComTradEntity : hisComTradList) {
            XpadQueryGuarantyProductListViewModel.QueryGuarantyProductListEntity entity = new XpadQueryGuarantyProductListViewModel.QueryGuarantyProductListEntity();

            BeanConvertor.toBean(hisComTradEntity, entity);

            viewList.add(entity);
        }

        viewModel.setList(viewList);
        viewModel.setRecordNumber(result.getRecordNumber());
        return  viewModel;
    }
    // 生成“历史常规交易状况查询”的ViewModel（用result对ViewModel进行填充）
    private XpadHisTradStatusViewModel generateXpadHisTradStatusViewModel(XpadHisTradStatusViewModel viewModel, PsnXpadHisTradStatusResult result) {
        List<PsnXpadHisTradStatusResult.XpadHisTradStatusResultEntity> hisTradList = result.getList();
        List<XpadHisTradStatusViewModel.XpadHisTradStatusResultEntity> viewList = new ArrayList<XpadHisTradStatusViewModel.XpadHisTradStatusResultEntity>();

        for (PsnXpadHisTradStatusResult.XpadHisTradStatusResultEntity hisTradEntitiy : hisTradList) {
            XpadHisTradStatusViewModel.XpadHisTradStatusResultEntity entitiy = new XpadHisTradStatusViewModel.XpadHisTradStatusResultEntity();
            BeanConvertor.toBean(hisTradEntitiy, entitiy);
            viewList.add(entitiy);
        }
        viewModel.setList(viewList);
        viewModel.setRecordNumber(result.getRecordNumber());
        return viewModel;
    }

}
