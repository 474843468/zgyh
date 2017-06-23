/**
 * 文件名	：Utils.java
 * 创建日期	：2012-10-15
 * Copyright (c) 2003-2012 北京联龙博通

 * All rights reserved.
 */

package com.chinamworld.bocmbci.utils;

import java.lang.reflect.Field;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.R.array;
import com.chinamworld.bocmbci.R.drawable;
import com.chinamworld.bocmbci.R.string;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.log.LogGloble;

/**
 * 描述:针对R.java工具类</p>
 * 
 * 主要通过反射取值
 * 
 * @version 1.00
 * @author wez
 * @date 2012-10-17
 * 
 */
public class RUtil {

	private static Class<string> stringClazz = R.string.class;
	private static Class<drawable> drawableClazz = R.drawable.class;
	private static Class<array> arrayClazz = R.array.class;

	/**
	 * 通过keyID在Drawable中获取int值
	 * 
	 * @param key
	 * @return
	 */
	public static int getDrawableIntValue(String key) {
		Field field;
		int value = 0;
		try {
			field = drawableClazz.getDeclaredField(key);
			value = field.getInt(null);
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (NoSuchFieldException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e) {
			LogGloble.exceptionPrint(e);
		} catch (IllegalAccessException e) {
			LogGloble.exceptionPrint(e);
		}

		return value;
	}

	public static String getString(int resId) {
		return BaseDroidApp.getContext().getString(resId);
	}

}
