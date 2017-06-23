/**
 * 文件名	：ImageFileHttpEngine.java
 * 
 * 创建日期	：2013-03-22
 * 
 * Copyright (c) 2003-2012 北京联龙博通

 * All rights reserved.
 */
package com.chinamworld.bocmbci.http.engine;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.http.HttpResponse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.log.LogGloble;

/**
 * ImageFileHttpEngine</p>
 * 
 * 图片专用 http引擎</p>
 * 
 * 对应:CommonRequestThread</p>
 * 
 * 返回数据类型:Bitmap</p>
 * 
 * @author wez
 * 
 */
public class ImageFileHttpEngine extends CommonHttpEngine {

	private static final String TAG = "ImageFileHttpEngine"; // add by wez
																// 2011.01.06

	/**
	 * 设置通信进度框展示或者消失
	 * 
	 * @param flag
	 *            true 展示progressdialog false dismiss progressdialog
	 * @author wez
	 */
	protected void setAlertStatus(final boolean flag) {
		// 下载图片无需进度条
	}

	/**
	 * 转换Response</p>
	 * 
	 * 得到的是bitmap
	 * 
	 * @param response
	 * @throws Exception
	 */
	protected void convertResponse(HttpResponse response,
			HashMap<String, Object> result) throws Exception {
		// 获取响应的实体流
		InputStream is = response.getEntity().getContent();
		try {
			LogGloble.i(TAG, "convertResponse InputStream to bitmap:");

			BufferedInputStream bfis = new BufferedInputStream(is);

			Bitmap bmp = BitmapFactory.decodeStream(bfis);

			result.put(ConstantGloble.HTTP_RESPOSE_CONTENT, bmp);

		} finally {
			if (is != null) {
				is.close();
			}
		}
	}
}
