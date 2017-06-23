package com.chinamworld.bocmbci.biz.peopleservice.entity;

import java.util.Map;

import android.app.Activity;
import android.view.View;

/**
 * 类功能描述：封装页面信息
 * 
 * @author：秦
 * @version：1.0
 * @see 包名：com.chinamworld.btwapview.domain
 */
public class BTCPageData {
	/**当前avtivity对象*/
	private Activity activity; // 当前avtivity对象
	/**当前页中出于激活状态的现象卡的实例对象地址*/
	private View globalTabView; // 当前页中出于激活状态的现象卡的实例对象地址
	/**当前页中所有设置了id号的控件的信息*/
	private Map<String, Integer> widgetIds; // 当前页中所有设置了id号的控件的信息
	/**当前页是否入栈标志*/
	private String nav; // 当前页是否入栈标志
	/**card 的id属性*/
	private String id;// card 的id属性
	/**card类型为group时缓存大小*/
	private String groupsize;// card类型为group时缓存大小
	/**当前页中输入框的输入长度最小值的限定*/
	private Map<String, String> editViewMinLength; // 当前页中输入框的输入长度最小值的限定
	/**当前页中输入框的数字的限定*/
	private Map<String, String> editViewOnlyLength; // 当前页中输入框的数字的限定
	
	
	private Map<Integer, Object> checkrule; // 验证规则
	
	/** xml原始根节点 */
	private BTCElement rootElement;
	/** 获得根节点 */
	public BTCElement getRootElement(){
		return rootElement;
	}
	
	public Map<Integer, Object> getCheckrule() {
		return checkrule;
	}

	public void setCheckrule(Map<Integer, Object> checkrule) {
		this.checkrule = checkrule;
	}

	/**
	 * set/get方法对
	 * @return
	 */
	public Map<String, String> getEditViewOnlyLength() {
		return editViewOnlyLength;
	}

	public void setEditViewOnlyLength(Map<String, String> editViewOnlyLength) {
		this.editViewOnlyLength = editViewOnlyLength;
	}

	/**当前页的页面信息,包括页面中的标签对象,数据库查询结果集*/
	private BTCUiData uiData; // 当前页的页面信息,包括页面中的标签对象,数据库查询结果集


	/**
	 * 带参构造函数1
	 * 
	 * @param activity
	 * @param uiData
	 * @param rTop
	 * @param lLinearLayoutBotton
	 * @param toolBarTop
	 */
	public BTCPageData(Activity activity, BTCElement element, BTCUiData uiData) {
		super();
		this.activity = activity;
		this.uiData = uiData;
		rootElement = element;
	}


	/**
	 * get/set方法对
	 * @return
	 */
	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public View getGlobalTabView() {
		return globalTabView;
	}

	public void setGlobalTabView(View globalTabView) {
		this.globalTabView = globalTabView;
	}

	public Map<String, Integer> getWidgetIds() {
		return widgetIds;
	}

	public void setWidgetIds(Map<String, Integer> widgetIds) {
		this.widgetIds = widgetIds;
	}

	public String getNav() {
		return nav;
	}

	public void setNav(String nav) {
		this.nav = nav;
	}

	public Map<String, String> getEditViewMinLength() {
		return editViewMinLength;
	}

	public void setEditViewMinLength(Map<String, String> editViewMinLength) {
		this.editViewMinLength = editViewMinLength;
	}

	public BTCUiData getUiData() {
		return uiData;
	}

	public void setUiData(BTCUiData uiData) {
		this.uiData = uiData;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupsize() {
		return groupsize;
	}

	public void setGroupsize(String groupsize) {
		this.groupsize = groupsize;
	}
	
	
	
	
	
	
}
