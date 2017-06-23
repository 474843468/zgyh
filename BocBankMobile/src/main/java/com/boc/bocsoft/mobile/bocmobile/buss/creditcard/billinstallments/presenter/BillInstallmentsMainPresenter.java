package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billinstallments.presenter;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetInput.PsnCrcdDividedPayBillSetInputParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetInput.PsnCrcdDividedPayBillSetInputResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.service.CrcdService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billinstallments.ui.BillInstallmentMainContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

/**
 * Name: liukai
 * Time：2017/1/3 16:51.
 * Created by lk7066 on 2017/1/3.
 * It's used to
 */

public class BillInstallmentsMainPresenter implements BillInstallmentMainContract.BillInstallmentMainPresenter {

    private RxLifecycleManager mRxLifecycleManager;

    /**
     * 信用卡service
     */
    private CrcdService crcdService;
    private BillInstallmentMainContract.BillInstallmentMainView mainView;

    public BillInstallmentsMainPresenter(BillInstallmentMainContract.BillInstallmentMainView view){
        mainView = view;
        mainView.setPresenter(this);
        crcdService = new CrcdService();
        mRxLifecycleManager = new RxLifecycleManager();
    }

    @Override
    public void queryBillInput(String accountID) {

        PsnCrcdDividedPayBillSetInputParams params = new PsnCrcdDividedPayBillSetInputParams();
        params.setAccountId(accountID);
        params.setCurrencyCode(ApplicationConst.CURRENCY_CNY);

        crcdService.psnCrcdDividedPayBillSetInput(params)
                .compose(mRxLifecycleManager.<PsnCrcdDividedPayBillSetInputResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdDividedPayBillSetInputResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdDividedPayBillSetInputResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mainView.queryBillInputFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdDividedPayBillSetInputResult result) {
                        mainView.queryBillInputSuccess(result.getUpInstmtAmount(), result.getLowInstmtAmount());
                    }
                });
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

}
