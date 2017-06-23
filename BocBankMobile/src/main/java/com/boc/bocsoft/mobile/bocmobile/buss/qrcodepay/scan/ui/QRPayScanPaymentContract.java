package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.scan.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.scan.model.QRPayScanPaymentViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * 扫码成功后的支付
 * Created by wangf on 2016/9/5.
 */
public class QRPayScanPaymentContract {

    public interface QRPayScanPaymentView{
        /*** 获取随机数成功 */
        void loadGetRandomSuccess(String random);

        /*** 获取随机数失败 */
        void loadGetRandomFail(BiiResultErrorException biiResultErrorException);

        /*** 正扫支付成功 */
        void loadQRPayDoPaymentSuccess(QRPayScanPaymentViewModel viewModel);

        /*** 正扫支付失败 */
        void loadQRPayDoPaymentFail(BiiResultErrorException biiResultErrorException);
    }

    public interface ScanPaymentPresenter extends BasePresenter {
        /*** 获取随机数  */
        void loadGetRandom();

        /*** 正扫支付  */
        void loadQRPayDoPayment(QRPayScanPaymentViewModel paymentViewModel);
        
        /*** 正扫支付 - 免密  */
        void loadQRPayDoPaymentFreePass(QRPayScanPaymentViewModel paymentViewModel);
    }

}
