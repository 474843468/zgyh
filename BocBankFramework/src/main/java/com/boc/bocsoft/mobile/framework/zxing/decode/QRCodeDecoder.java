package com.boc.bocsoft.mobile.framework.zxing.decode;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

/**
 * 解析二维码图片
 * Created by XieDu on 2016/6/16.
 */
public class QRCodeDecoder {

    /**
     * 解析二维码图片
     *
     * @param bitmap   要解析的二维码图片
     * @param decodeListener 解析二维码图片的监听器
     */
    public static void decodeQRCode(final Bitmap bitmap, final DecodeListener decodeListener) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    int[] pixels = new int[width * height];
                    bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
                    RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
                    Result result = new MultiFormatReader().decode(new BinaryBitmap(new HybridBinarizer(source)), DecodeFormatManager.HINTS);
                    return result.getText();
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if (decodeListener != null) {
                    if (TextUtils.isEmpty(result)) {
                        decodeListener.onDecodeQRCodeFailure();
                    } else {
                        decodeListener.onDecodeQRCodeSuccess(result);
                    }
                }
            }
        }.execute();
    }

    public interface DecodeListener {
        /**
         * 解析二维码成功
         *
         * @param result 从二维码中解析的文本，如果该方法有被调用，result不会为空
         */
        void onDecodeQRCodeSuccess(String result);

        /**
         * 解析二维码失败
         */
        void onDecodeQRCodeFailure();
    }
}
