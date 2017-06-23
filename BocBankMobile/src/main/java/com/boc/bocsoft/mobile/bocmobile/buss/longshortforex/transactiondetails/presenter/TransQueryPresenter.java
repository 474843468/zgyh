package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGGetRegCurrency.PsnVFGGetRegCurrencyParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.service.LongShortForexService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadPsnVFGGetRegCurrencyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.ui.TransQueryContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.List;

/**
 * 双向宝——交易查询主界面
 * Created by zc on 2016/11/17
 */
public class TransQueryPresenter extends RxPresenter implements TransQueryContract.Presenter {

    private TransQueryContract.View mTransQueryView;
    private GlobalService mGlobalService;
    private LongShortForexService mLongShortForexService;
    private RxLifecycleManager mRxLifecycleManager;

    public TransQueryPresenter(TransQueryContract.View view) {
        this.mTransQueryView = view;
        mTransQueryView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();
        mGlobalService = new GlobalService();
        mLongShortForexService = new LongShortForexService();
    }

    /**
     * 双向宝结算币种查询
     * @param viewmodel
     */
    @Override
    public void psnXpadGetRegCurrency(final XpadPsnVFGGetRegCurrencyModel viewmodel) {
        PsnVFGGetRegCurrencyParams psnVFGGetRegCurrencyParams = new PsnVFGGetRegCurrencyParams();

        mLongShortForexService.psnXpadGetRegCurrency(psnVFGGetRegCurrencyParams)
                .compose(SchedulersCompat.<List<String>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<String>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransQueryView.psnXpadGetRegCurrencyFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(List<String> psnVFGGetRegCurrencyResults) {
                        mTransQueryView.psnXpadGetRegCurrencySuccess(psnVFGGetRegCurrencyResults);
                    }
                });
    }
}
