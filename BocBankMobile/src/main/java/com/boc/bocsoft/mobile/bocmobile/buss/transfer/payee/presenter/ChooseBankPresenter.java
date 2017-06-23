package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.presenter;

import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnOtherBankQueryForTransToAccount.PsnOtherBankQueryForTransToAccountParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnOtherBankQueryForTransToAccount.PsnOtherBankQueryForTransToAccountResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnOtherBankQueryForTransToAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui.ChooseBankContact;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter:
 * Created by zhx on 2016/7/20
 */
public class ChooseBankPresenter implements ChooseBankContact.Presenter {
    private ChooseBankContact.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private TransferService transService;

    private PsnOtherBankQueryForTransToAccountResult hotBankList;

    public PsnOtherBankQueryForTransToAccountResult getHotBankList() {
        return hotBankList;
    }

    public ChooseBankPresenter(ChooseBankContact.View view) {
        this.mView = view;
        mRxLifecycleManager = new RxLifecycleManager();
        transService = new TransferService();

        mView.setPresenter(this);
    }

    @Override
    public void psnOtherBankQueryForTransToAccount(final PsnOtherBankQueryForTransToAccountViewModel viewModel) {
        ((BussFragment) mView).showLoadingDialog("请稍后...");

        final PsnOtherBankQueryForTransToAccountParams params = new PsnOtherBankQueryForTransToAccountParams();
        params.setType(viewModel.getType());
        params.setBankName("");
        transService.psnOtherBankQueryForTransToAccount(params)
                .compose(mRxLifecycleManager.<PsnOtherBankQueryForTransToAccountResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnOtherBankQueryForTransToAccountResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnOtherBankQueryForTransToAccountResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) mView).closeProgressDialog();
                        mView.psnOtherBankQueryForTransToAccountFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnOtherBankQueryForTransToAccountResult psnOtherBankQueryForTransToAccountResult) {
                        ((BussFragment) mView).closeProgressDialog();
                        fillViewModel(psnOtherBankQueryForTransToAccountResult, viewModel); // 填充ViewModel

                        mView.psnOtherBankQueryForTransToAccountSuccess(viewModel, params.getType());
                    }
                });
    }

    // 填充ViewModel
    private PsnOtherBankQueryForTransToAccountViewModel fillViewModel(PsnOtherBankQueryForTransToAccountResult psnOtherBankQueryForTransToAccountResult, PsnOtherBankQueryForTransToAccountViewModel viewModel) {
        List<PsnOtherBankQueryForTransToAccountResult.AccountOfBankListEntity> accountOfBankList = psnOtherBankQueryForTransToAccountResult.getAccountOfBankList();
        List<PsnOtherBankQueryForTransToAccountViewModel.AccountOfBankListEntity> bankList = new ArrayList<PsnOtherBankQueryForTransToAccountViewModel.AccountOfBankListEntity>();
        for (PsnOtherBankQueryForTransToAccountResult.AccountOfBankListEntity accountOfBankListEntity : accountOfBankList) {
            PsnOtherBankQueryForTransToAccountViewModel.AccountOfBankListEntity accountOfBankListEntity1 = new PsnOtherBankQueryForTransToAccountViewModel.AccountOfBankListEntity();
            BeanConvertor.toBean(accountOfBankListEntity, accountOfBankListEntity1);
            bankList.add(accountOfBankListEntity1);
        }

        viewModel.setAccountOfBankList(bankList);
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
