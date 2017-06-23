package com.chinamworld.bocmbci.biz.peopleservice.controlcenter;

import org.w3c.dom.Element;

import android.content.Context;

import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCPageData;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCUiData;
import com.chinamworld.bocmbci.biz.peopleservice.ui.BTCActivityManager;
import com.chinamworld.bocmbci.biz.peopleservice.ui.BTCUiActivity;
import com.chinamworld.bocmbci.biz.peopleservice.xmlinterface.BTCKxmlParser;



/**
 * 类功能描述：控制程序的流程，进行数据的分析和准备。
 * 
 * @author：ql
 * @version：1.0
 * @see 包名：com.chinamworld.btwapview.controlcenter
 */
public class BTCControlCenter {

	/** 上下文对象 */
	private static Context context;
	 
	
	/**
	 * 通过Element节点，构造BTCElement树
	 * btw浏览器的入口，跳转到服务器返回的第一个页面。
	 * 
	 * @param con
	 * @param root
	 */
	public static void createPage(Context con, Element root) {
		context = con;
		BTCElement rootElement = BTCKxmlParser.parseElement(con,root);		
		BTCUiData data = new BTCUiData(rootElement,rootElement.getChildElements(), null, null, "");
		BTCActivityManager.getInstance().setPageData(new BTCPageData(BTCUiActivity.Instance(),rootElement, data));
	}

	
	
	

	/**
	 * 上下文对象的get方法
	 * 
	 * @return 上下文对象
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * 上下文对象的set方法
	 * 
	 * @param c
	 *            上下文对象
	 */
	public void setContext(Context c) {
		context = c;
	}


}
