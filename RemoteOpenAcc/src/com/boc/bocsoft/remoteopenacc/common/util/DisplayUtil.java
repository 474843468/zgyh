package com.boc.bocsoft.remoteopenacc.common.util;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;

public class DisplayUtil {
	/**
	 * 获取屏幕宽度和高度，单位为px
	 * 
	 * @param context
	 * @return
	 */
	public static Point getScreenMetrics(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int w_screen = dm.widthPixels;
		int h_screen = dm.heightPixels;
		return new Point(w_screen, h_screen);

	}

	/**
	 * 获取应用界面宽度和高度（屏幕除去状态栏），单位为px
	 * 
	 * @param context
	 * @return
	 */
	public static Point getAppScreenMetrics(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int w_screen = dm.widthPixels;
		int h_screen = dm.heightPixels - getStatusBarHeight(context);// 减去状态栏高度
		return new Point(w_screen, h_screen);

	}

	/**
	 * 获取状态栏高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier(
				"status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	/**
	 * 获取屏幕长宽比
	 * 
	 * @param context
	 * @return
	 */
	public static float getScreenRate(Context context) {
		Point P = getScreenMetrics(context);
		float H = P.y;
		float W = P.x;
		return (H / W);
	}
}
