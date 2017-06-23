package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.presenter;

import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryExternalBankInfo.PsnTransQueryExternalBankInfoParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryExternalBankInfo.PsnTransQueryExternalBankInfoResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransQueryExternalBankInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui.ChooseOpenBankContact;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import java.util.List;

/**
 * Presenter:选择开户行
 * Created by zhx on 2016/7/22
 */
public class ChooseOpenBankPresenter implements ChooseOpenBankContact.Presenter {
    private ChooseOpenBankContact.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private TransferService transService;

    private PsnTransQueryExternalBankInfoViewModel viewModel;

    public ChooseOpenBankPresenter(ChooseOpenBankContact.View view) {
        this.mView = view;
        mRxLifecycleManager = new RxLifecycleManager();
        transService = new TransferService();

        mView.setPresenter(this);
    }

    @Override
    public void psnTransQueryExternalBankInfo(PsnTransQueryExternalBankInfoViewModel viewModel) {
        this.viewModel = viewModel;
        // 根据viewModel构建请求参数
        PsnTransQueryExternalBankInfoParams params = buildRequestParams(viewModel);
        transService.psnTransQueryExternalBankInfo(params)
                .compose(mRxLifecycleManager.<PsnTransQueryExternalBankInfoResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnTransQueryExternalBankInfoResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransQueryExternalBankInfoResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.psnTransQueryExternalBankInfoFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnTransQueryExternalBankInfoResult result) {
                        mView.psnTransQueryExternalBankInfoSuccess(buildResult2ViewModel(result));
                    }
                });
    }

    private PsnTransQueryExternalBankInfoParams buildRequestParams(PsnTransQueryExternalBankInfoViewModel viewModel) {
        PsnTransQueryExternalBankInfoParams params = new PsnTransQueryExternalBankInfoParams();
        params.setBankName(viewModel.getBankName());
        params.setCurrentIndex(viewModel.getCurrentIndex());
        params.setPageSize(viewModel.getPageSize());
        params.setToBankCode(viewModel.getToBankCode());
        return params;
    }

    private PsnTransQueryExternalBankInfoViewModel buildResult2ViewModel(PsnTransQueryExternalBankInfoResult result) {
        viewModel.setRecordnumber(result.getRecordnumber());
        List<PsnTransQueryExternalBankInfoViewModel.OpenBankBean> viewList = viewModel.getList();
        List<PsnTransQueryExternalBankInfoResult.OpenBankBean> list = result.getList();

        for (PsnTransQueryExternalBankInfoResult.OpenBankBean bean : list) {
            PsnTransQueryExternalBankInfoViewModel.OpenBankBean viewBean = new PsnTransQueryExternalBankInfoViewModel.OpenBankBean();
            viewBean.setBankName(bean.getBankName());
            viewBean.setCnapsCode(bean.getCnapsCode());

            viewList.add(viewBean);
        }

        return viewModel;
    }


    @Override
    public void subscribe() {
        // TODO onResume时需要做的工作
    }

    @Override
    public void unsubscribe() {
        // TODO 防止外界已经销毁，而后台线程的任务还在执行
        mRxLifecycleManager.onDestroy();
    }
}