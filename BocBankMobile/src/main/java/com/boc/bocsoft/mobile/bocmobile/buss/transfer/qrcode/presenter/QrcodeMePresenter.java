package com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnAccountInfoEncrypt.PsnAccountInfoEncryptParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnAccountInfoEncrypt.PsnAccountInfoEncryptResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.ui.QrcodeMeContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;
import com.boc.bocsoft.mobile.framework.utils.ResUtils;
import com.boc.bocsoft.mobile.framework.zxing.encode.QRCodeEncoder;

import rx.functions.Func1;

/**
 * Created by xdy4486 on 2016/6/28.
 */
public class QrcodeMePresenter extends RxPresenter implements QrcodeMeContract.Prensenter {

    private QrcodeMeContract.View mQrcodeGenerateView;
    private TransferService mTransferService;
    private QRCodeEncoder mQRCodeEncoder;

    public QrcodeMePresenter(QrcodeMeContract.View view) {
        mQrcodeGenerateView = view;
        mTransferService = new TransferService();
        mQRCodeEncoder = new QRCodeEncoder.Builder().setLogoScale(8).setLogoBitmap(
                BitmapFactory.decodeResource(mQrcodeGenerateView.getContext().getResources(),
                        R.drawable.boc_qrcode_logo))
                                                    .setOutputBitmapHeight(ResUtils.dip2px(
                                                            mQrcodeGenerateView.getContext(), 300))
                                                    .setOutputBitmapWidth(ResUtils.dip2px(
                                                            mQrcodeGenerateView.getContext(), 300))
                                                    .build();
    }

    @Override
    public void generateQrcode(AccountBean accountBean) {
        PsnAccountInfoEncryptParams params = new PsnAccountInfoEncryptParams();
        params.setCustActNum(accountBean.getAccountNumber());
        params.setCustName(accountBean.getAccountName());
        mTransferService.psnAccountInfoEncrypt(params)
                        .compose(this.<PsnAccountInfoEncryptResult>bindToLifecycle())
                        .map(new Func1<PsnAccountInfoEncryptResult, Bitmap>() {
                            @Override
                            public Bitmap call(
                                    PsnAccountInfoEncryptResult psnAccountInfoEncryptResult) {
                                return mQRCodeEncoder.encode(
                                        psnAccountInfoEncryptResult.getEncryptStr());
                            }
                        })
                        .compose(SchedulersCompat.<Bitmap>applyIoSchedulers())
                        .subscribe(new BIIBaseSubscriber<Bitmap>() {
                            @Override
                            public void handleException(
                                    BiiResultErrorException biiResultErrorException) {
                            }

                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onNext(Bitmap bitmap) {
                                mQrcodeGenerateView.onGenerateSuccess(bitmap);
                            }
                        });
    }
}
