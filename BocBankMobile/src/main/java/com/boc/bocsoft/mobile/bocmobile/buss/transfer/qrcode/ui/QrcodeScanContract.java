package com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.model.ScanResultAccountModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * Created by xdy4486 on 2016/6/23.
 */
public class QrcodeScanContract {
    public interface View {
        /**
         * 解码成功
         * @param model
         */
        void onDecodeSuccess(ScanResultAccountModel model);

        /**
         * 解码失败
         * @param message
         */
        void onDecodeFailed(String message);

        /**
         * 二维码解码结果是本人关联账户
         */
        void onIsMyAccount();
    }

    public interface Presenter extends BasePresenter {
        /**
         * 解码二维码
         */
        void decodeQrcode(String content);
    }
}
