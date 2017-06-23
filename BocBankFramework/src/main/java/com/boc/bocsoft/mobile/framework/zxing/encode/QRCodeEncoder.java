package com.boc.bocsoft.mobile.framework.zxing.encode;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.EnumMap;

/**
 * 生成二维码。默认字符编码为UTF-8，默认二维码图片的背景色0xFFFFFFFF
 * 默认二维码的编码块颜色0xFF000000
 * Created by XieDu on 2016/6/16.
 */
public class QRCodeEncoder {
    public static final String TAG = QRCodeEncoder.class.getSimpleName();

    private QRCodeWriter qrCodeWriter;

    private final Builder mConfigBuilder;

    private QRCodeEncoder(Builder configBuilder) {
        mConfigBuilder = configBuilder;
        qrCodeWriter = new QRCodeWriter();
    }

    /**
     * 将制定内容生成二维码
     *
     * @param codeInfo 文本内容
     * @return bitmap二维码图片
     */
    public Bitmap encode(String codeInfo) {
        if (TextUtils.isEmpty(codeInfo)) {
            return null;
        }
        EnumMap<EncodeHintType, Object> hints =
                new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
        //指定编码格式
        hints.put(EncodeHintType.CHARACTER_SET, mConfigBuilder.mCharset);
        //指定纠错级别（L -- 7%，M -- 15%，Q -- 25%，H -- 30%）
        if (mConfigBuilder.errorCorrectionLevel != null) {
            hints.put(EncodeHintType.ERROR_CORRECTION, mConfigBuilder.errorCorrectionLevel);
        }
        if (mConfigBuilder.mHintPadding >= 0) {
            // 输出图片外边距
            hints.put(EncodeHintType.MARGIN, mConfigBuilder.mHintPadding);
        }
        BitMatrix bitMatrix;
        try {
            bitMatrix = qrCodeWriter.encode(codeInfo, BarcodeFormat.QR_CODE,
                    mConfigBuilder.mOutputBitmapWidth, mConfigBuilder.mOutputBitmapHeight, hints);
        } catch (Exception e) {
            Log.w(TAG, e);
            return null;
        }
        int finalBitmapWidth = mConfigBuilder.mOutputBitmapWidth;
        int finalBitmapHeight = mConfigBuilder.mOutputBitmapHeight;
        //int finalBitmapWidth = bitMatrix.getWidth();
        //int finalBitmapHeight = bitMatrix.getHeight();
        int[] pixels = new int[finalBitmapWidth * finalBitmapHeight];
        for (int y = 0; y < finalBitmapHeight; y++) {
            int offset = y * finalBitmapWidth;
            for (int x = 0; x < finalBitmapWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? mConfigBuilder.mCodeColor
                        : mConfigBuilder.mBackgroundColor;
            }
        }
        Bitmap bitmap =
                Bitmap.createBitmap(finalBitmapWidth, finalBitmapHeight, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, finalBitmapWidth, 0, 0, finalBitmapWidth, finalBitmapHeight);
        // 添加Logo
        return addLogoToQRCode(bitmap, mConfigBuilder.logoBitmap);
    }

    /**
     * 向二维码图中添加logo
     *
     * @param srcBitmap 原始二维码图
     * @param logoBitmap logo
     * @return 添加了logo的二维码图
     */
    private Bitmap addLogoToQRCode(Bitmap srcBitmap, Bitmap logoBitmap) {
        if (srcBitmap == null || logoBitmap == null) {
            return srcBitmap;
        }

        //modify by wangf on 2016-11-23 09:46:41 可以设置logo占二维码区域的比例
        int scale = mConfigBuilder.logoScale;
        Canvas canvas = new Canvas(srcBitmap);
        Rect srcRect = new Rect(0, 0, logoBitmap.getWidth(), logoBitmap.getHeight());
        int bitmapWidht = srcBitmap.getWidth();
        int bitmapHeight = srcBitmap.getHeight();
        int logoDstBitmapWidth = bitmapWidht / scale;
        int logoDstBitmapHeight = bitmapHeight / scale;
        int logoOffsetLeft = (bitmapWidht - logoDstBitmapWidth) / 2;
        int logoOffsetTop = (bitmapHeight - logoDstBitmapHeight) / 2;
        Rect dstRect = new Rect(logoOffsetLeft, logoOffsetTop, logoOffsetLeft + logoDstBitmapWidth,
                logoOffsetTop + logoDstBitmapHeight);
        canvas.drawBitmap(logoBitmap, srcRect, dstRect, null);
        return srcBitmap;
    }

    public static class Builder {

        /**
         * 生成二维码图片的背景色
         */
        private int mBackgroundColor = 0xFFFFFFFF;
        /**
         * 二维码的编码块颜色
         */
        private int mCodeColor = 0xFF000000;
        /**
         * 文本编码格式
         */
        private String mCharset = "utf-8";
        /**
         * 输出图片的宽度
         */
        private int mOutputBitmapWidth;
        /**
         * 输出图片的高度
         */
        private int mOutputBitmapHeight;
        /**
         * 输出二维码与图片边缘的距离
         */
        private int mHintPadding = -1;

        /**
         * logo图片
         */
        private Bitmap logoBitmap;

        /**
         * logo图片占二维码区域的比例
         */
        private int logoScale = 7;

        private ErrorCorrectionLevel errorCorrectionLevel;

        /**
         * 设置生成二维码图片的背景色
         *
         * @param backgroundColor 背景色，如 0xFFFFFFFF
         * @return Builder，用于链式调用
         */
        public Builder setBackgroundColor(int backgroundColor) {
            mBackgroundColor = backgroundColor;
            return this;
        }

        /**
         * 设置二维码的编码块颜色
         *
         * @param codeColor 编码块颜色，如 0xFF000000
         * @return Builder，用于链式调用
         */
        public Builder setCodeColor(int codeColor) {
            mCodeColor = codeColor;
            return this;
        }

        /**
         * 设置文本编码格式
         *
         * @param charset 字符编码格式
         * @return Builder，用于链式调用
         */
        public Builder setCharset(String charset) {
            if (TextUtils.isEmpty(charset)) {
                throw new IllegalArgumentException("Illegal charset: " + charset);
            }
            mCharset = charset;
            return this;
        }

        /**
         * 设置输出图片的宽度
         *
         * @param outputBitmapWidth 宽度，单位：px
         * @return Builder，用于链式调用
         */
        public Builder setOutputBitmapWidth(int outputBitmapWidth) {
            mOutputBitmapWidth = outputBitmapWidth;
            return this;
        }

        /**
         * 设置输出图片的高度
         *
         * @param outputBitmapHeight 高度，单位：px
         * @return Builder，用于链式调用
         */
        public Builder setOutputBitmapHeight(int outputBitmapHeight) {
            mOutputBitmapHeight = outputBitmapHeight;
            return this;
        }

        /**
         * 设置输出二维码与图片边缘的距离
         *
         * @param hintPadding 距离值，正整数。
         */
        public Builder setOutputBitmapPadding(int hintPadding) {
            mHintPadding = hintPadding;
            return this;
        }

        /**
         * 设置二维码中间logo
         */
        public Builder setLogoBitmap(Bitmap logoBitmap) {
            this.logoBitmap = logoBitmap;
            return this;
        }

        /**
         * 设置logo占二维码区域的比例
         * added by wangf on 2016-11-23 09:45:33
         */
        public Builder setLogoScale(int logoScale) {
            this.logoScale = logoScale;
            return this;
        }

        /**
         * @return QRCode生成器对象
         */
        public QRCodeEncoder build() {
            return new QRCodeEncoder(this);
        }

        public Builder setErrorCorrectionLevel(ErrorCorrectionLevel errorCorrectionLevel) {
            this.errorCorrectionLevel = errorCorrectionLevel;
            return this;
        }
    }
}
