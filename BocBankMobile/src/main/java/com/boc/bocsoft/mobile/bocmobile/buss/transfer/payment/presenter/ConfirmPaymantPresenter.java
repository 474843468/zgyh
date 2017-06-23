package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQuotaQuery.PsnTransQuotaQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQuotaQuery.PsnTransQuotaQueryResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.model.ConfirmPaymentModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.ui.ConfirmPaymentContact;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.List;

/**
 * Created by wangtong on 2016/7/21.
 */
public class ConfirmPaymantPresenter extends RxPresenter implements ConfirmPaymentContact.Presenter {
    private ConfirmPaymentContact.View view;
    private RxLifecycleManager mRxLifecycleManager;
    private TransferService psnPaymentService;
    private AccountService accountService;
    private ConfirmPaymentModel uiModel;

    public ConfirmPaymantPresenter(ConfirmPaymentContact.View view) {
        this.view = view;
        mRxLifecycleManager = new RxLifecycleManager();
        psnPaymentService = new TransferService();
        accountService = new AccountService();
        uiModel = view.getModel();
    }

    @Override
    public void psnAccountQueryAccountDetail() {
        PsnAccountQueryAccountDetailParams params = new PsnAccountQueryAccountDetailParams();
        params.setAccountId(uiModel.getAccountId());
        ((BussFragment) view).showLoadingDialog();
        accountService.psnAccountQueryAccountDetail(params)
                .compose(mRxLifecycleManager.<PsnAccountQueryAccountDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnAccountQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnAccountQueryAccountDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        uiModel.setQueryStates(false);
                        ((BussFragment) view).showErrorDialog(biiResultErrorException.getErrorMessage());
                        ((BussFragment) view).closeProgressDialog();
                        view.psnAccountQueryAccountDetailReturned();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnAccountQueryAccountDetailResult result) {
                        List<PsnAccountQueryAccountDetailResult.AccountDetaiListBean> list = result.getAccountDetaiList();
                        // 是否有人民币余额
                        boolean isHaveRMB = false;
                        PsnAccountQueryAccountDetailResult.AccountDetaiListBean bean = null;
                        for (PsnAccountQueryAccountDetailResult.AccountDetaiListBean item : list) {
                            if ("001".equals(item.getCurrencyCode())) {
                                isHaveRMB = true;
                                bean = item;
                                break;
                            } else {
                                isHaveRMB = false;
                            }
                        }
                        if (isHaveRMB) {
                            uiModel.setQueryStates(true);
                            uiModel.setRemainAmount(bean.getAvailableBalance());
                            uiModel.setRemainCurrency(bean.getCurrencyCode());
                        } else {
                            uiModel.setQueryStates(false);
                            uiModel.setWarning("该账户无人民币余额，请修改");
                            uiModel.setWarnType("1");
                        }
                        view.psnAccountQueryAccountDetailReturned();
                        ((BussFragment) view).closeProgressDialog();
                    }
                });
    }


    @Override
    public void queryCreditAccountDetail(String accountId) {
        ((BussFragment) view).showLoadingDialog();
        PsnCrcdQueryAccountDetailParams params = new PsnCrcdQueryAccountDetailParams();
        params.setAccountId(accountId);
        params.setCurrency("001");
        accountService.psnCrcdQueryAccountDetail(params)
                .compose(mRxLifecycleManager.<PsnCrcdQueryAccountDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdQueryAccountDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        uiModel.setQueryStates(false);
                        view.queryCreditAccountDetailReturned();
                        ((BussFragment) view).closeProgressDialog();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdQueryAccountDetailResult result) {
                        // 是否有人民币余额
                        boolean isHaveRMB = false;
                        PsnCrcdQueryAccountDetailResult.CrcdAccountDetailListBean bean = null;
                        for (PsnCrcdQueryAccountDetailResult.CrcdAccountDetailListBean item : result.getCrcdAccountDetailList()) {
                            if ("001".equals(item.getCurrency())) {
                                isHaveRMB = true;
                                bean = item;
                                break;
                            } else {
                                isHaveRMB = false;
                            }
                        }
                        if (isHaveRMB) {
                            uiModel.setQueryStates(true);
                            uiModel.setLoanBalanceLimitFlag(bean.getLoanBalanceLimitFlag());
                            uiModel.setRemainAmount(bean.getLoanBalanceLimit());
                            uiModel.setRemainCurrency(bean.getCurrency());
                        } else {
                            uiModel.setQueryStates(false);
                            uiModel.setWarning("该账户无人民币余额，请修改");
                            uiModel.setWarnType("1");
                        }
                        view.queryCreditAccountDetailReturned();
                        ((BussFragment) view).closeProgressDialog();
                    }
                });
    }

    @Override
    public void psnTransQuotaquery() {
        PsnTransQuotaQueryParams params = new PsnTransQuotaQueryParams();
        psnPaymentService.psnTransQuotaQuery(params)
                .compose(this.<PsnTransQuotaQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnTransQuotaQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransQuotaQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        view.queryQuotaForTransFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnTransQuotaQueryResult result) {
                        view.queryQuotaForTransSuccess(result);
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }
                });
    }

}
