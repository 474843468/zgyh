package com.boc.bocsoft.remoteopenacc.common.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class ImageUtil {
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

	public static Bitmap decodeSampledBitmapFromResource(String pathName,
			int reqWidth, int reqHei) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHei);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(pathName, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// 源图片的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			// 一定都会大于等于目标的宽和高。
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	public static Bitmap loadBitmap(String pathName) {
		Bitmap b = null;
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			b = BitmapFactory.decodeFile(pathName, opts);
		} catch (Exception e) {
			e.printStackTrace();
			b = null;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			b = null;
		}
		return b;
	}
}
