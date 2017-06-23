package com.boc.bocma.tools;

import android.os.Build;

public class MAVersionUtils {
	/**
	 * @return true if SDK >= 8
	 */
	public static boolean laterThanFroyo() {
		// Can use static final constants like FROYO, declared in later versions
		// of the OS since they are inlined at compile time. This is guaranteed
		// behavior.
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	/**
	 * @return true if SDK >= 9
	 */
	public static boolean laterThanGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	/**
	 * @return true if SDK >= 11
	 */
	// public static boolean laterThanHoneycomb() {delete by lgw 2015.10.28
	// 由于要使用SDK 2.3.3(Android 10) 编译
	// return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	// }

	/**
	 * @return true if SDK >= 12
	 */
	// public static boolean laterThanHoneycombMR1() {delete by lgw 2015.10.28
	// 由于要使用SDK 2.3.3(Android 10) 编译
	// return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	// }

	/**
	 * @return true if SDK >= 16
	 */
	// public static boolean laterThanJellyBean() {delete by lgw 2015.10.28
	// 由于要使用SDK 2.3.3(Android 10) 编译
	// return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	// }
}
