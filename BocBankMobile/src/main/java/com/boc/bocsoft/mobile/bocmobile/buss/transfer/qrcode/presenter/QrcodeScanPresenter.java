package com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnAccountInfoDecipher.PsnAccountInfoDecipherParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnAccountInfoDecipher.PsnAccountInfoDecipherResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.model.ScanResultAccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.ui.QrcodeScanContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

/**
 * Created by xdy4486 on 2016/6/23.
 */
public class QrcodeScanPresenter extends RxPresenter implements QrcodeScanContract.Presenter {

    private QrcodeScanContract.View mScanView;
    private GlobalService globalService;
    private TransferService psnQrcodeService;
    private RxLifecycleManager mRxLifecycleManager;

    public QrcodeScanPresenter(QrcodeScanContract.View scanView) {
        mScanView = scanView;
        globalService = new GlobalService();
        psnQrcodeService = new TransferService();
        mRxLifecycleManager = new RxLifecycleManager();
    }

    @Override
    public void decodeQrcode(String content) {
        PsnAccountInfoDecipherParams decipherParams = new PsnAccountInfoDecipherParams();
        decipherParams.setEncryptStr(content);
        psnQrcodeService.psnAccountInfoDecipher(decipherParams)
                .compose(
                        mRxLifecycleManager.<PsnAccountInfoDecipherResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnAccountInfoDecipherResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnAccountInfoDecipherResult>() {

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }

                    @Override
                    public void handleException(
                            BiiResultErrorException biiResultErrorException) {
                        mScanView.onDecodeFailed(biiResultErrorException.getMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(
                            PsnAccountInfoDecipherResult psnAccountInfoDecipherResult) {
                        if (isMyAccount(psnAccountInfoDecipherResult)) {
                            mScanView.onIsMyAccount();
                        } else {
                            ScanResultAccountModel model = new ScanResultAccountModel();
                            model.setCustActNum(
                                    psnAccountInfoDecipherResult.getCustActNum());
                            model.setCustName(psnAccountInfoDecipherResult.getCustName());
                            mScanView.onDecodeSuccess(model);
                        }
                    }
                });
    }

    /**
     * 判断是否为自己的关联账户
     */
    private boolean isMyAccount(PsnAccountInfoDecipherResult psnAccountInfoDecipherResult) {
        for (AccountBean accountBean : ApplicationContext.getInstance()
                .getChinaBankAccountList(null)) {
            if (accountBean.getAccountNumber()
                    .equals(psnAccountInfoDecipherResult.getCustActNum())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void subscribe() {
        mRxLifecycleManager.onStart();
    }

    @Override
    public void unsubscribe() {
        mRxLifecycleManager.onDestroy();
    }
}
