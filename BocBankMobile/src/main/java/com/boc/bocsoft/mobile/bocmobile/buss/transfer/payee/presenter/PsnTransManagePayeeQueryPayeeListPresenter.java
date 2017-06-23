package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.presenter;

import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransQueryPayeeList.PsnDirTransQueryPayeeListParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransQueryPayeeList.PsnDirTransQueryPayeeListResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransManagePayeeQueryPayeeList.PsnTransManagePayeeQueryPayeeListParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransManagePayeeQueryPayeeList.PsnTransManagePayeeQueryPayeeListResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnDirTransQueryPayeeListViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransManagePayeeQueryPayeeListViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui.PsnTransManagePayeeQueryPayeeListContact;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import java.util.List;

/**
 * Presenter:查询收款人列表
 * Created by zhx on 2016/7/25
 */
public class PsnTransManagePayeeQueryPayeeListPresenter implements PsnTransManagePayeeQueryPayeeListContact.Presenter {
    private PsnTransManagePayeeQueryPayeeListContact.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private TransferService transferService;

    private PsnTransManagePayeeQueryPayeeListViewModel viewModel;
    private PsnDirTransQueryPayeeListViewModel viewModel2;

    public PsnTransManagePayeeQueryPayeeListPresenter(PsnTransManagePayeeQueryPayeeListContact.View view) {
        mView = view;
        mRxLifecycleManager = new RxLifecycleManager();
        transferService = new TransferService();

        mView.setPresenter(this);
    }

    @Override
    public void psnTransManagePayeeQueryPayeeList(final PsnTransManagePayeeQueryPayeeListViewModel viewModel) {
        // 显示修改中的对话框
        ((BussFragment) mView).showLoadingDialog("请稍后...");

        this.viewModel = viewModel;
        PsnTransManagePayeeQueryPayeeListParams params = new PsnTransManagePayeeQueryPayeeListParams();
        params.setBocFlag(viewModel.getBocFlag());

        transferService.getPsnTransManagePayeeQueryPayeeList(params)
                .compose(mRxLifecycleManager.<List<PsnTransManagePayeeQueryPayeeListResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnTransManagePayeeQueryPayeeListResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnTransManagePayeeQueryPayeeListResult>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) mView).closeProgressDialog();
                        mView.psnTransManagePayeeQueryPayeeListFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(List<PsnTransManagePayeeQueryPayeeListResult> payeeListResults) {
                        transResult2ViewModel(payeeListResults);
                        mView.psnTransManagePayeeQueryPayeeListSuccess(viewModel);

                        ((BussFragment) mView).closeProgressDialog();
                    }
                });

    }

    @Override
    public void psnDirTransQueryPayeeList(final PsnDirTransQueryPayeeListViewModel viewModel2) {
        this.viewModel2 = viewModel2;
        PsnDirTransQueryPayeeListParams params = new PsnDirTransQueryPayeeListParams();
        params.setBocFlag(viewModel2.getBocFlag());

        transferService.getPsnDirTransQueryPayeeList(params)
                .compose(mRxLifecycleManager.<List<PsnDirTransQueryPayeeListResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnDirTransQueryPayeeListResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnDirTransQueryPayeeListResult>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.psnDirTransQueryPayeeListFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(List<PsnDirTransQueryPayeeListResult> payeeListResults) {
                        List<PsnDirTransQueryPayeeListViewModel.PayeeEntity> payeeEntityList = viewModel2.getPayeeEntityList();

                        for (PsnDirTransQueryPayeeListResult psnDirTransQueryPayeeListResult : payeeListResults) {
                            PsnDirTransQueryPayeeListViewModel.PayeeEntity payeeEntity = new PsnDirTransQueryPayeeListViewModel.PayeeEntity();

                            BeanConvertor.toBean(psnDirTransQueryPayeeListResult, payeeEntity);

                            payeeEntityList.add(payeeEntity);

                        }

                        mView.psnDirTransQueryPayeeListSuccess(viewModel2);
                    }
                });

    }

    // 把请求结果转换为ViewModel
    private PsnTransManagePayeeQueryPayeeListViewModel transResult2ViewModel(List<PsnTransManagePayeeQueryPayeeListResult> payeeListResults) {
        List<PsnTransManagePayeeQueryPayeeListViewModel.PayeeEntity> mPayeeEntityList = viewModel.getPayeeEntityList();
        mPayeeEntityList.clear();

        for (PsnTransManagePayeeQueryPayeeListResult result : payeeListResults) {
            PsnTransManagePayeeQueryPayeeListViewModel.PayeeEntity payeeEntity = new PsnTransManagePayeeQueryPayeeListViewModel.PayeeEntity();

            payeeEntity.setAddress(result.getAddress());
            payeeEntity.setType(result.getType());
            payeeEntity.setAccountNumber(result.getAccountNumber());
            payeeEntity.setPayeeCNName(result.getPayeeCNName());
            payeeEntity.setBankName(result.getBankName());
            payeeEntity.setPayeeAlias(result.getPayeeAlias());
            payeeEntity.setMobile(result.getMobile());
            payeeEntity.setBankCode(result.getBankCode());
            payeeEntity.setRegionCode(result.getRegionCode());
            payeeEntity.setPostcode(result.getPostcode());
            payeeEntity.setBankNum(result.getBankNum());
            payeeEntity.setCountryCode(result.getCountryCode());
            payeeEntity.setPayBankCode(result.getPayBankCode());
            payeeEntity.setPayBankName(result.getPayBankName());
            payeeEntity.setAccountName(result.getAccountName());
            payeeEntity.setAccountIbkNum(result.getAccountIbkNum());
            payeeEntity.setSwift(result.getSwift());
            payeeEntity.setPayeetId(result.getPayeetId());
            payeeEntity.setBocFlag(result.getBocFlag());
            payeeEntity.setCnapsCode(result.getCnapsCode());

            payeeEntity.setPinyin(""); // 设置拼音

            mPayeeEntityList.add(payeeEntity);
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
