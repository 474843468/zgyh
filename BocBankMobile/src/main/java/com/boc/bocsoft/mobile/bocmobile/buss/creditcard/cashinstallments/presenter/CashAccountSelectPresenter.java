package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.cashinstallments.presenter;

import android.util.Log;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryCashDivBalance.PsnCrcdQueryCashDivBalanceParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryCashDivBalance.PsnCrcdQueryCashDivBalanceResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.service.CrcdService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.cashinstallments.ui.CashAccountSelectContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

/**
 * Created by cry7096 on 2016/11/30.
 */
public class CashAccountSelectPresenter extends RxPresenter
        implements CashAccountSelectContract.Presenter {
    protected CrcdService mCrcdService;
    private CashAccountSelectContract.View mCashAccountSelectView;

    public CashAccountSelectPresenter(CashAccountSelectContract.View view) {
        mCashAccountSelectView = view;
        mCrcdService = new CrcdService();
    }

    @Override
    public void cashSelectAccount(AccountBean accountBean) {
        PsnCrcdQueryCashDivBalanceParams params = new PsnCrcdQueryCashDivBalanceParams();
        params.setAccountId(accountBean.getAccountId());
        mCrcdService.psnCrcdQueryCashDivBalance(params)
                .compose(this.<PsnCrcdQueryCashDivBalanceResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdQueryCashDivBalanceResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdQueryCashDivBalanceResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdQueryCashDivBalanceResult psnCrcdQueryCashDivBalanceResult) {
                        mCashAccountSelectView.onQueryCashDivBalanceSuccess(psnCrcdQueryCashDivBalanceResult.getAvailableBalance());
                    }
                });
    }

}
