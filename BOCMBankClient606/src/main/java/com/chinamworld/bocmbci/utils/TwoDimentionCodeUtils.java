package com.chinamworld.bocmbci.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

/**
 * Created by Administrator on 2016/11/1.
 */

public class TwoDimentionCodeUtils {

    /**
     * 将制定内容生成二维码
     *
     * @param codeInfo
     *            文本内容
     * @return bitmap二维码图片
     */
    public static  Bitmap create2DimentionCode(Context context, String codeInfo) {
        try {
            QRCodeWriter writer = new QRCodeWriter();

            if (codeInfo == null || "".equals(codeInfo)
                    || codeInfo.length() < 1) {
                return null;
            }

            // 把输入的文本转为二维码
            BitMatrix martix = writer.encode(codeInfo, BarcodeFormat.QR_CODE,
                    LayoutValue.QR_WIDTH, LayoutValue.QR_HEIGHT);


            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(codeInfo,
                    BarcodeFormat.QR_CODE, LayoutValue.QR_WIDTH,
                    LayoutValue.QR_HEIGHT, hints);
            int[] pixels = new int[LayoutValue.QR_WIDTH * LayoutValue.QR_HEIGHT];
            for (int y = 0; y < LayoutValue.QR_HEIGHT; y++) {
                for (int x = 0; x < LayoutValue.QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * LayoutValue.QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * LayoutValue.QR_WIDTH + x] = 0xffffffff;
                    }

                }
            }

            Bitmap bitmap = Bitmap.createBitmap(LayoutValue.QR_WIDTH,
                    LayoutValue.QR_HEIGHT, Bitmap.Config.ARGB_8888);

            bitmap.setPixels(pixels, 0, LayoutValue.QR_WIDTH, 0, 0,
                    LayoutValue.QR_WIDTH, LayoutValue.QR_HEIGHT);
            // 添加Logo,logo绘制二维码上的比例
            int scale = 7;
            Bitmap logoBitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.logo_boc);
            Canvas canvas = new Canvas(bitmap);
            Rect srcRect = new Rect(0, 0, logoBitmap.getWidth(),
                    logoBitmap.getHeight());
            int bitmapWidht = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            int logoDstBitmapWidth = bitmapWidht / scale;
            int logoDstBitmapHeight = bitmapHeight / scale;
            int logoOffsetLeft = (bitmapWidht - logoDstBitmapWidth) / 2;
            int logoOffsetTop = (bitmapHeight - logoDstBitmapHeight) / 2;
            Rect dstRect = new Rect(logoOffsetLeft, logoOffsetTop,
                    logoOffsetLeft + logoDstBitmapWidth, logoOffsetTop
                    + logoDstBitmapHeight);
            canvas.drawBitmap(logoBitmap, srcRect, dstRect, null);
            return bitmap;
        } catch (WriterException e) {

            return null;
        }
    }
}
