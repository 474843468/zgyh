package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.presenter;

import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPayeeListqueryForDim.PsnTransPayeeListqueryForDimParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPayeeListqueryForDim.PsnTransPayeeListqueryForDimResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransPayeeListqueryForDimViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui.PsnTransPayeeListqueryForDimContact;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import java.util.List;

/**
 * Presenter:查询收款人列表
 * Created by zhx on 2016/8/31
 */
public class PsnTransPayeeListqueryForDimPresenter implements PsnTransPayeeListqueryForDimContact.Presenter {
    private PsnTransPayeeListqueryForDimContact.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private TransferService transferService;

    private PsnTransPayeeListqueryForDimViewModel dimViewModel;

    public PsnTransPayeeListqueryForDimPresenter(PsnTransPayeeListqueryForDimContact.View view) {
        mView = view;
        this.mRxLifecycleManager = new RxLifecycleManager();

        transferService = new TransferService();

        mView.setPresenter(this);
    }

    @Override
    public void psnTransPayeeListqueryForDim(PsnTransPayeeListqueryForDimViewModel viewModel) {
        // 显示修改中的对话框
        this.dimViewModel = viewModel;
        PsnTransPayeeListqueryForDimParams params = new PsnTransPayeeListqueryForDimParams();
        String[] bocFlag = {"0", "1", "3"};
        params.setBocFlag(bocFlag);
        params.setIsAppointed(""); // 是否定向收款人(0：非定向 1：定向 输入为空时（""）查全部)
        params.setPayeeName("");
        params.setCurrentIndex("0");
        params.setPageSize("500");

        transferService.psnTransPayeeListqueryForDim(params)
                .compose(mRxLifecycleManager.<PsnTransPayeeListqueryForDimResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnTransPayeeListqueryForDimResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransPayeeListqueryForDimResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.psnTransPayeeListqueryForDimFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnTransPayeeListqueryForDimResult psnTransPayeeListqueryForDimResult) {
                        mView.psnTransPayeeListqueryForDimSuccess(transResult2ViewModel(psnTransPayeeListqueryForDimResult));
                    }
                });

    }

    // 把请求结果转换为ViewModel
    private PsnTransPayeeListqueryForDimViewModel transResult2ViewModel(PsnTransPayeeListqueryForDimResult psnTransPayeeListqueryForDimResult) {
        dimViewModel.getPayeeEntityList().clear();
        List<PsnTransPayeeListqueryForDimResult.PayeeAccountBean> accountBeanList = psnTransPayeeListqueryForDimResult.getList();

        for (PsnTransPayeeListqueryForDimResult.PayeeAccountBean payeeAccountBean : accountBeanList) {
            PsnTransPayeeListqueryForDimViewModel.PayeeEntity payeeEntity = new PsnTransPayeeListqueryForDimViewModel.PayeeEntity();

            BeanConvertor.toBean(payeeAccountBean, payeeEntity);
            payeeEntity.setPinyin("");

            dimViewModel.getPayeeEntityList().add(payeeEntity);
        }

        return dimViewModel;
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
