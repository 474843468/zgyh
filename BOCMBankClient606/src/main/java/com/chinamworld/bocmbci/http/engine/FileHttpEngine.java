/**
 * 文件名	：FileHttpEngine.java
 * 
 * 创建日期	：2013-03-22
 * 
 * Copyright (c) 2003-2012 北京联龙博通

 * All rights reserved.
 */
package com.chinamworld.bocmbci.http.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.http.HttpResponse;

import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.log.LogGloble;

/**
 * FileHttpEngine</p>
 * 
 * 普通File专用 http引擎</p>
 * 
 * 对应:CommonRequestThread</p>
 * 
 * 返回数据类型:File</p>
 * 
 * @author wez
 * 
 */
public class FileHttpEngine extends CommonHttpEngine {

	private static final String TAG = "FileHttpEngine"; 
	
	private String filePath ;
	
	
	/**
	 * 
	 * @param filePath  文件路径
	 */
	public FileHttpEngine(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * 设置通信进度框展示或者消失
	 * 
	 * @param flag
	 *            true 展示progressdialog false dismiss progressdialog
	 * @author wez
	 */
	protected void setAlertStatus(final boolean flag) {
		// 轮询无需进度条
	}

	/**
	 * 转换Response </p>
	 * 
	 * 得到的是File
	 * 
	 * @param response
	 * @throws Exception
	 */
	protected void convertResponse(HttpResponse response,
			HashMap<String, Object> result) throws Exception {
		// 获取响应的实体流
		InputStream is = response.getEntity().getContent();
		try {
			LogGloble.i(TAG, "convertResponse InputStream to File:");

			File file = new File(filePath);
			writeFile(is, file);
			result.put(ConstantGloble.HTTP_RESPOSE_CONTENT, file);
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	/**
	 * 
	 * @param is
	 * @param f
	 * @throws Exception
	 */
	public void writeFile(InputStream is, File f) throws Exception {
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(f);
			@SuppressWarnings("unused")
			int len;
			byte[] buffer = new byte[8192];
			while (( len = is.read(buffer)) != -1) {

				os.flush();
			}

		} finally {
			if (os != null) {
				os.close();
			}
		}
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
}
