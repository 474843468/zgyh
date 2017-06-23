package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.collection.ui;

import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayDoTransfer.QRPayDoTransferResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayeeResult.QRPayGetPayeeResultResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.payment.model.QRPayGetQRCodeViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * 二维码收款
 * Created by fanbin on 16/9/29.
 */
public class QRCollectionCodeContract {

    public interface GetQrCollectionCodeView {
        /***
         * 获取二维码成功
         */
        void loadQRCollectionGetQRCodeSuccess(QRPayGetQRCodeViewModel qrCodeViewModel);

        /***
         * 获取二维码失败
         */
        void loadQRCollectionGetQRCodeFail(BiiResultErrorException biiResultErrorException);
    }

    public interface QrCollectionCodeView {
        /***
         * 收款结果查询 成功
         */
        void loadQRPayGetTransRecordSuccess(QRPayGetPayeeResultResult ViewModel);

        /***
         * 收款结果查询 失败
         */
        void loadQRPayGetTransRecordFail(BiiResultErrorException biiResultErrorException);


    }

    public interface QrTransCodeView {

        /***
         * 转账交易 成功
         */
        void loadQRPayDoTransferSuccess(QRPayDoTransferResult ViewModel);

        /***
         * 转账交易 失败
         */
        void loadQRPayDoTransferFail(BiiResultErrorException biiResultErrorException);

        /***
         * 获取随机数成功
         */
        void loadGetRandomSuccess(String random);

        /***
         * 获取随机数失败
         */
        void loadGetRandomFail(BiiResultErrorException biiResultErrorException);

    }


    public interface Presenter extends BasePresenter {
        /***
         * 获取二维码
         */
        void loadQRPayGetQRCode(String accSeq,String amount,String payeeComments);

        /***
         * 获取随机数
         */
        void loadGetRandom();

        /***
         * 收款结果查询
         */
        void loadQRPayGetTransRecord(String qrNo);

        void loadQRPayDoTransfer(String actSeq, String password, String password_RC, String tranAmount,
                                 String qrNo, String payerComments, String payeeAccNo, String payeeName);

    }
}
