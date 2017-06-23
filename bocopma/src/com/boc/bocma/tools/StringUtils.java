package com.boc.bocma.tools;

import android.text.TextUtils;

/**
 * 工具类
 * 
 * @author gwluo
 * 
 */
public class StringUtils {
	/**
	 * 是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmptyOrNull(String str) {
		if (TextUtils.isEmpty(str) || "null".equalsIgnoreCase(str)) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(String str) {
		return TextUtils.isEmpty(str);
	}
}
