package com.boc.bocsoft.mobile.bocmobile.buss.account.overview.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQryRecentTransDetail.PsnAccountQryRecentTransDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQryRecentTransDetail.PsnAccountQryRecentTransDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnMedicalInsurAcctDetailQuery.PsnMedicalInsurAcctDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnMedicalInsurAcctDetailQuery.PsnMedicalInsurAcctDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.model.MedicalModel;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;

/**
 * 医保账户BII通信逻辑处理
 * Created by niuguobin on 2016/6/28.
 */
public class MedicalInsurancePresenter extends BaseAccountPresenter implements OverviewContract.MedicalInsurancePresenter{

    /** Service */
    private AccountService accountService;
    /** 回调界面 */
    private OverviewContract.MedicalView medicalView;

    public MedicalInsurancePresenter(OverviewContract.MedicalView medicalView) {
        super();
        this.medicalView = medicalView;
        accountService = new AccountService();
    }

    @Override
    public void queryMedicalDetail(final String accountId) {
        accountService.psnMedicalInsurAcctDetailQuery(new PsnMedicalInsurAcctDetailQueryParams(accountId))
                .compose(this.<PsnMedicalInsurAcctDetailQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnMedicalInsurAcctDetailQueryResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnMedicalInsurAcctDetailQueryResult>() {
                    @Override
                    public void onNext(PsnMedicalInsurAcctDetailQueryResult result) {
                        medicalView.queryMedicalDetail(ModelUtil.generateMedicalModel(accountId,result));
                    }
                });
    }

    @Override
    public void queryMedicalTransferDetail(final MedicalModel medicalModel) {
        accountService.psnAccountQryRecentTransDetail(new PsnAccountQryRecentTransDetailParams(medicalModel.getAccountId(),true))
        .compose(this.<PsnAccountQryRecentTransDetailResult>bindToLifecycle())
        .compose(SchedulersCompat.<PsnAccountQryRecentTransDetailResult>applyIoSchedulers())
        .subscribe(new BaseAccountSubscriber<PsnAccountQryRecentTransDetailResult>() {
            @Override
            public void onNext(PsnAccountQryRecentTransDetailResult result) {
                medicalView.queryMedicalTransferDetail(ModelUtil.generateTransactionBean(result));
            }
        });
    }
}
