package com.chinamworld.bocmbci.biz.peopleservice.entity;

import java.util.List;
import java.util.Map;

import android.database.Cursor;

/**
 * 类功能描述：封装页面信息
 * 
 * @author：秦
 * @version：1.0
 * @see 包名：com.chinamworld.btwapview.domain
 */
public class BTCUiData {
	

	/**存放标签信息*/
	private List<BTCElement> widgetList; // 存放标签信息
	/**数据查询结果集*/
	private Map<String, Cursor> cursorMap;// 数据查询结果集
	/**数据查询结果集大小*/
	private Map<String, String> cursorSize; // 数据查询结果集大小
	/**当前页面数据字符串*/
	private String xml;// 当前页面数据字符串

	
	/**
	 * get/set方法对
	 * @return
	 */
	public Map<String, String> getCursorSize() {
		return cursorSize;
	}

	public void setCursorSize(Map<String, String> cursorSize) {
		this.cursorSize = cursorSize;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public List<BTCElement> getWidgetList() {
		return widgetList;
	}

	public void setWidgetList(List<BTCElement> widgetList) {
		this.widgetList = widgetList;
	}

	public Map<String, Cursor> getCursorMap() {
		return cursorMap;
	}

	public void setCursorMap(Map<String, Cursor> cursorMap) {
		this.cursorMap = cursorMap;
	}

	
	/**
	 * 带参构造函数
	 * @param widgetList
	 * @param cursorMap
	 * @param cursorSize
	 * @param xml
	 */
	public BTCUiData(BTCElement root,List<BTCElement> widgetList,
			Map<String, Cursor> cursorMap, Map<String, String> cursorSize,
			String xml) {
		super();
		this.widgetList = widgetList;
		this.cursorMap = cursorMap;
		this.xml = xml;
		this.cursorSize = cursorSize;
	}

	/**
	 * 返回所有信息
	 */
	@Override
	public String toString() {
		return "BTCUiData [cursorMap=" + cursorMap + ", widgetList="
				+ widgetList + ", xml=" + xml + "]";
	}
}
