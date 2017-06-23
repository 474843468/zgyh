package com.boc.bocsoft.mobile.framework.zxing.encode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.view.Gravity;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * 条形码的生成
 * 默认二维码图片的背景色0xFFFFFFFF
 * 默认二维码的编码块颜色0xFF000000
 * Created by wangf on 2016/8/18.
 */
public class BarCodeEncoder {
    private static final String TAG = BarCodeEncoder.class.getSimpleName();

    private final Builder mConfigBuilder;


    private BarCodeEncoder(Builder configBuilder) {
        mConfigBuilder = configBuilder;
    }

    /**
     * 生成条形码
     *
     * @param context
     * @param codeInfo 需要生成的内容
     * @return
     */
    public Bitmap creatBarcode(Context context, String codeInfo) {
        try {
            Bitmap ruseltBitmap = null;
            /**
             * 图片两端所保留的空白的宽度
             */
            int marginW = mConfigBuilder.mOutputMarginWidth;
            /**
             * 条形码的编码类型
             */
            BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;
            int desiredWidth = mConfigBuilder.mOutputBitmapWidth;
            int desiredHeight = mConfigBuilder.mOutputBitmapHeight;
            boolean isDisplayCodeText = mConfigBuilder.mIsDisplayCodeText;
            boolean isTextDisplayTop = mConfigBuilder.mIsTextDisplayTop;

            if (isDisplayCodeText) {
                Bitmap barcodeBitmap = encodeAsBitmap(codeInfo, barcodeFormat, desiredWidth, desiredHeight);
                Bitmap codeBitmap = creatCodeBitmap(codeInfo, desiredWidth + 2 * marginW, desiredHeight, context);
                if (isTextDisplayTop) {
                    ruseltBitmap = mixtureBitmapTop(barcodeBitmap, codeBitmap, new PointF(0, codeBitmap.getHeight()));
                } else {
                    ruseltBitmap = mixtureBitmap(barcodeBitmap, codeBitmap, new PointF(0, barcodeBitmap.getHeight()));
                }
            } else {
                ruseltBitmap = encodeAsBitmap(codeInfo, barcodeFormat, desiredWidth, desiredHeight);
            }

            return ruseltBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成条形码的Bitmap
     *
     * @param contents      需要生成的内容
     * @param format        编码格式
     * @param desiredWidth
     * @param desiredHeight
     * @return
     * @throws WriterException
     */
    private Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int desiredWidth, int desiredHeight) {
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = null;
        try {
            result = writer.encode(contents, format, desiredWidth, desiredHeight, null);
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        // All are 0, or black, by default
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? mConfigBuilder.mCodeColor : mConfigBuilder.mBackgroundColor;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 生成显示编码的Bitmap
     *
     * @param contents
     * @param width
     * @param height
     * @param context
     * @return
     */
    private Bitmap creatCodeBitmap(String contents, int width, int height, Context context) {
        TextView tv = new TextView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(layoutParams);
        if (mConfigBuilder.mIsFormatCodeNumber) {
            tv.setText(NumberUtils.formatBarCodeNumber(contents));
        } else {
            tv.setText(contents);
        }
//        tv.setHeight(height);//此处不需要设置TextView的高度
        tv.setWidth(width);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setDrawingCacheEnabled(true);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(mConfigBuilder.mDisplayCodeTextSize);
        tv.getPaint().setFakeBoldText(mConfigBuilder.mIsDisplayCodeTextBold);
        tv.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());

        tv.buildDrawingCache();
        Bitmap bitmapCode = tv.getDrawingCache();
        return bitmapCode;
    }

    /**
     * 将两个Bitmap合并成一个
     * 条码显示在上面，条码内容文字显示在下面
     *
     * @param first
     * @param second
     * @param fromPoint 第二个Bitmap开始绘制的起始位置（相对于第一个Bitmap）
     * @return
     */
    private Bitmap mixtureBitmap(Bitmap first, Bitmap second, PointF fromPoint) {
        if (first == null || second == null || fromPoint == null) {
            return null;
        }
        int marginW = mConfigBuilder.mOutputMarginWidth;
        Bitmap newBitmap = Bitmap.createBitmap(second.getWidth(), first.getHeight() + second.getHeight() + mConfigBuilder.mOutputMarginBar2Text + 10, Config.ARGB_4444);
        Canvas cv = new Canvas(newBitmap);
        cv.drawBitmap(first, marginW, 0, null);
        cv.drawBitmap(second, fromPoint.x, fromPoint.y + mConfigBuilder.mOutputMarginBar2Text, null);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();

        return newBitmap;
    }

    /**
     * 将两个Bitmap合并成一个
     * 条码内容显示在上面，条码显示在下面
     *
     * @param first
     * @param second
     * @param fromPoint 第二个Bitmap开始绘制的起始位置（相对于第一个Bitmap）
     * @return
     */
    private Bitmap mixtureBitmapTop(Bitmap first, Bitmap second, PointF fromPoint) {
        if (first == null || second == null || fromPoint == null) {
            return null;
        }
        int marginW = mConfigBuilder.mOutputMarginWidth;
        Bitmap newBitmap = Bitmap.createBitmap(second.getWidth(), first.getHeight() + second.getHeight() + mConfigBuilder.mOutputMarginBar2Text, Config.ARGB_4444);
        Canvas cv = new Canvas(newBitmap);
        cv.drawBitmap(second, fromPoint.x, 0, null);
        cv.drawBitmap(first, marginW, fromPoint.y + mConfigBuilder.mOutputMarginBar2Text, null);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();

        return newBitmap;
    }


    public static class Builder {
        /**
         * 生成条形码图片的背景色
         */
        private int mBackgroundColor = 0xFFFFFFFF;
        /**
         * 条形码的编码块颜色
         */
        private int mCodeColor = 0xFF000000;
        /**
         * 输出图片的宽度
         */
        private int mOutputBitmapWidth = 500;
        /**
         * 输出图片的高度
         */
        private int mOutputBitmapHeight = 200;

        /**
         * 图片两端所保留的空白的宽度
         */
        private int mOutputMarginWidth = 20;

        /**
         * 条码与条码文字之间的距离
         */
        private int mOutputMarginBar2Text = 0;

        /**
         * 是否显示条码内容
         */
        private boolean mIsDisplayCodeText = false;

        /**
         * 条码内容是否显示在顶部
         */
        private boolean mIsTextDisplayTop = false;

        /**
         * 条码内容的文字大小
         */
        private int mDisplayCodeTextSize = 14;

        /**
         * 条码内容的文字是否加粗
         */
        private boolean mIsDisplayCodeTextBold = false;

        /**
         * 条码显示内容是否格式化
         */
        private boolean mIsFormatCodeNumber = false;


        /**
         * 设置生成条形码图片的背景色
         *
         * @param backgroundColor 背景色，如 0xFFFFFFFF
         * @return Builder，用于链式调用
         */
        public Builder setBackgroundColor(int backgroundColor) {
            mBackgroundColor = backgroundColor;
            return this;
        }

        /**
         * 设置条形码的编码块颜色
         *
         * @param codeColor 编码块颜色，如 0xFF000000
         * @return Builder，用于链式调用
         */
        public Builder setCodeColor(int codeColor) {
            mCodeColor = codeColor;
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
         * 设置图片两端所保留的空白的宽度
         *
         * @param outputMarginWidth
         * @return
         */
        public Builder setOutputMarginWidth(int outputMarginWidth) {
            mOutputMarginWidth = outputMarginWidth;
            return this;
        }


        /**
         * 设置条码与文字之间的距离
         *
         * @param outputMarginBar2Text
         * @return
         */
        public Builder setOutputMarginBar2Text(int outputMarginBar2Text) {
            mOutputMarginBar2Text = outputMarginBar2Text;
            return this;
        }

        /**
         * 设置是否显示条码内容
         *
         * @param isDisplayCodeText
         * @return
         */
        public Builder setIsDisplayCodeText(boolean isDisplayCodeText) {
            mIsDisplayCodeText = isDisplayCodeText;
            return this;
        }

        /**
         * 设置条码内容是否显示在顶部
         *
         * @param isTextDisplayTop
         * @return
         */
        public Builder setIsTextDisplayTop(boolean isTextDisplayTop) {
            mIsTextDisplayTop = isTextDisplayTop;
            return this;
        }

        /**
         * 设置条码内容的文字大小
         *
         * @param textSize
         * @return
         */
        public Builder setDisplayCodeTextSize(int textSize) {
            mDisplayCodeTextSize = textSize;
            return this;
        }

        /**
         * 设置条码内容的文字是否加粗
         *
         * @param isDisplayCodeTextBold
         * @return
         */
        public Builder setIsDisplayCodeTextBold(boolean isDisplayCodeTextBold) {
            mIsDisplayCodeTextBold = isDisplayCodeTextBold;
            return this;
        }

        /**
         * 设置条码显示的内容是否格式化
         *
         * @param isFormatCodeNumber
         * @return
         */
        public Builder setIsFormatCodeNumber(boolean isFormatCodeNumber) {
            mIsFormatCodeNumber = isFormatCodeNumber;
            return this;
        }


        /**
         * @return BarCode生成器对象
         */
        public BarCodeEncoder build() {
            return new BarCodeEncoder(this);
        }
    }


}
