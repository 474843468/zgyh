package com.boc.bocsoft.mobile.framework.zxing.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

public class BitmapUtils {


    /**
     * 将view生成bitmap
     *
     * @param context context
     * @param view    view
     * @return 是否生成成功
     */
    public static Bitmap createViewForBitmap(Context context, View view) {

        view.clearFocus();
        view.setPressed(false);
        boolean willNotCache = view.willNotCacheDrawing();

        view.buildDrawingCache();
        Bitmap cacheBitmap = view.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        view.destroyDrawingCache();
        view.setWillNotCacheDrawing(willNotCache);
        return bitmap;
        // if (checkSDK(c)) {
        // save2DimenBitmap(bitmap);
        // }

    }


    /**
     * 旋转Bitmap
     *
     * @param b
     * @param rotateDegree
     * @return
     */
    public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) rotateDegree);
        Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
                b.getHeight(), matrix, false);
        return rotaBitmap;
    }


    /**
     * 保存图片到相册
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static boolean saveBitmap(Context context, Bitmap bitmap) {
        String title = new StringBuilder("genterate2Dimen_").append(System.currentTimeMillis()).append(".jpg").toString();
        String result = MediaStore.Images.Media.insertImage(
                context.getContentResolver(), bitmap, title, "");
        if (TextUtils.isEmpty(result))
            return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.parse(result)));
        } else {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
                    .parse(result)));
        }
        return true;
    }

    public static Bitmap readBitmap(String picturePath) {
        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, options);
        options.inJustDecodeBounds = false;
        int saleX = options.outWidth / 400;
        int saleY = options.outHeight / 400;
        int sale = saleX > saleY ? saleX : saleY;
        options.inSampleSize = sale;
        bm = BitmapFactory.decodeFile(picturePath, options);
        return bm;
    }
}

