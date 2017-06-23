package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.payment.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.payment.model.QRPayGetQRCodeViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.payment.model.QRPayTransInfoViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * 二维码付款
 * Created by wangf on 2016/8/29.
 */
public class QRPayCodeContract {

    public interface GetQrCodeView{
        /*** 获取二维码成功 */
        void loadQRPayGetQRCodeSuccess(QRPayGetQRCodeViewModel qrCodeViewModel);

        /*** 获取二维码失败*/
        void loadQRPayGetQRCodeFail(BiiResultErrorException biiResultErrorException);
    }

    public interface QrCodeView {
        /*** 查询反扫后的交易确认通知 成功 */
        void loadQRPayGetConfirmInfoSuccess(QRPayGetQRCodeViewModel qrCodeViewModel);

        /*** 查询反扫后的交易确认通知 失败 */
        void loadQRPayGetConfirmInfoFail(BiiResultErrorException biiResultErrorException);

        /*** 反扫支付成功 */
        void loadQRPayDoScannedPaymentSuccess();

        /*** 反扫支付失败 */
        void loadQRPayDoScannedPaymentFail(BiiResultErrorException biiResultErrorException);

        /*** 获取随机数成功 */
        void loadGetRandomSuccess(String random);

        /*** 获取随机数失败 */
        void loadGetRandomFail(BiiResultErrorException biiResultErrorException);

        /*** 查询反扫支付交易信息 成功 */
        void loadQRPayGetTransInfoSuccess(QRPayTransInfoViewModel infoViewModel);

        /*** 查询反扫支付交易信息 失败 */
        void loadQRPayGetTransInfoFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /*** 获取二维码 */
        void loadQRPayGetQRCode(String accSeq);

        /*** 查询反扫后的交易确认通知 */
        void loadQRPayGetConfirmInfo();

        /*** 反扫支付 */
        void loadQRPayDoScannedPayment(String password,String password_RC, String confirmInfoConversationID);

        /*** 获取随机数 */
        void loadGetRandom();

        /*** 查询反扫支付交易信息 */
        void loadQRPayGetTransInfo(String settleKey);
    }

}
