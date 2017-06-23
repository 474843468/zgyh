package com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.ui;

import android.content.Context;
import android.graphics.Bitmap;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;

/**
 * Created by XieDu on 2016/7/4.
 */
public interface QrcodeMeContract {
    interface View {

        Context getContext();

        /**
         * 二维码生成成功
         *
         * @param bitmap 生成的二维码
         */
        void onGenerateSuccess(Bitmap bitmap);
    }

    interface Prensenter {

        /**
         * 生成二维码
         */
        void generateQrcode(AccountBean accountBean);
    }
}
