package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.ui.ChooseWealthAccountContact;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadAccountQueryViewModel;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter：中银理财-选择理财帐号页面
 * Created by zhx on 2016/9/20
 */
public class ChooseWealthAccountPresenter implements ChooseWealthAccountContact.Presenter {
    private ChooseWealthAccountContact.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private GlobalService globalService;
    private WealthManagementService wealthManagementService;

    public ChooseWealthAccountPresenter(ChooseWealthAccountContact.View view) {
        this.mView = view;
        mView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();

        globalService = new GlobalService();
        wealthManagementService = new WealthManagementService();
    }

    @Override
    public void psnXpadAccountQuery(final XpadAccountQueryViewModel xpadAccountQueryViewModel) {
        ((BussFragment) mView).showLoadingDialog("请稍候...");
        PsnXpadAccountQueryParams psnXpadAccountQueryParams = buildPsnXpadAccountQueryParams(xpadAccountQueryViewModel);

        wealthManagementService.psnXpadAccountQuery(psnXpadAccountQueryParams)
                .compose(SchedulersCompat.<PsnXpadAccountQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadAccountQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) mView).closeProgressDialog();
                        //                        mView.psnXpadAccountQueryFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnXpadAccountQueryResult psnXpadAccountQueryResult) {
                        ((BussFragment) mView).closeProgressDialog();
                        mView.psnXpadAccountQuerySuccess(generateXpadAccountQueryViewModel(xpadAccountQueryViewModel, psnXpadAccountQueryResult));


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
