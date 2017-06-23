/**
 * 文件名	：PollingHttpEngine.java
 * 
 * 创建日期	：2013-03-22
 * 
 * Copyright (c) 2003-2012 北京联龙博通

 * All rights reserved.
 */
package com.chinamworld.bocmbci.http.engine;


/**
 * PollingHttpEngine</p>
 * 
 * 轮询专用 http引擎</p>
 * 
 * 对应:PollingRequestThread</p>
 * 
 * 返回数据类型:String</p>
 * 
 * @author wez
 * 
 */
public class PollingCommonHttpEngine extends CommonHttpEngine {
	

	/**
	 * 设置通信进度框展示或者消失
	 * 
	 * @param flag
	 *            true 展示progressdialog false dismiss progressdialog
	 * @author wez
	 */
	protected void setAlertStatus(final boolean flag) {
		//轮询无需进度条
	}
}
