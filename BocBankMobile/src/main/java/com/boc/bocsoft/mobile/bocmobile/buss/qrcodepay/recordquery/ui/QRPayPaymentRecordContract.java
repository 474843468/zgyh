package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.recordquery.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.recordquery.model.QRPayPaymentRecordViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * Created by wangf on 2016/9/8.
 */
public class QRPayPaymentRecordContract {

    public interface PaymentRecordView {
        /*** 交易记录查询列表成功回调*/
        void queryPaymentRecordListSuccess(QRPayPaymentRecordViewModel paymentRecordViewModel);

        /*** 交易记录查询列表失败回调*/
        void queryPaymentRecordListFail(BiiResultErrorException biiResultErrorException);
    }


    public interface Presenter extends BasePresenter {

        /*** 交易记录查询列表*/
        void queryPaymentRecordList(QRPayPaymentRecordViewModel paymentRecordViewModel);
    }
}
