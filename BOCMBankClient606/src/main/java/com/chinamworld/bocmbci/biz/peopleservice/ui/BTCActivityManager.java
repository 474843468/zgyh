package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCPageData;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCUiData;


/**
 * 类功能描述：自定义的栈管理器,里面存放了所有进行入栈处理的activity的相关信息,包括当前activity对象的地址,
 * 该对象对应的所有页面的所有页面信息
 * 
 * @author：秦
 * @version： 1.0
 * @see 包名：com.chinamworld.btwapview.ui
 * 
 *      相关数据：
 */
public class BTCActivityManager {

	/**保存PageData数据*/
	private static BTCPageData pageData; // 保存PageData数据
	/**栈实例对象*/
	private static BTCActivityManager instance;// 栈实例对象

	/**
	 * 方法功能说明 ：以单例模式实例化概栈管理器 *
	 * 
	 * @param
	 * @return BTCActivityManager
	 */
	public static BTCActivityManager getInstance() {
		if (instance == null) {
			instance = new BTCActivityManager();
		}
		return instance;
	}

	/**
	 * 方法功能说明 ：返回当前activity所对应的页面的相应信息
	 * 
	 * @param
	 * @return BTCPageData 当前activity所对应的页面的相应信息
	 */
	public BTCPageData getPageData() {
		return pageData;
	}
	private int current ;
	
	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}
	/**
	 * 方法功能说明 ：返回当前activity
	 * 
	 * @param
	 * @return BTCPageData 当前activity
	 */
	public Activity currentActivity() {
		return pageData.getActivity();
	}

	/**
	 * 设置页面数据
	 * @param pd
	 */
	public void setPageData(BTCPageData pd) {
		pageData = pd;
	}

	/**
	 * 方法功能说明 ：把页面中的输入框,下拉框等需要设置id号的控件的id号保存到对应的activity信息中
	 * 
	 * @param
	 * @return void
	 */
	public void putWidgetId(String value, int hashcode) {
		if (pageData == null) {
			return;
		}
		Map<String, Integer> widgetIds = pageData.getWidgetIds();
		if (widgetIds == null) {
			widgetIds = new HashMap<String, Integer>();
		}
		widgetIds.put(value, hashcode);
		pageData.setWidgetIds(widgetIds);
	}

	/**
	 * 方法功能说明 ：返回当前页面的所有设置了id的控件的名字和id号
	 * 
	 * @param
	 * @return Map<String, Integer>
	 */
	public Map<String, Integer> getWidgetIds() {
		if (pageData == null) {
			return null;
		}
		Map<String, Integer> map = pageData.getWidgetIds();
		if (map == null) {
			map = new HashMap<String, Integer>();
		}
		return map;
	}

	/**
	 * 方法功能说明 ：如果当前页面中有选项卡,返回当前出于激活状态的选项卡
	 * 
	 * @param
	 * @return view
	 */
	public View getGlobalTabView() {
		if (pageData == null) {
			return null;
		}
		return pageData.getGlobalTabView();
	}

	/**
	 * 方法功能说明 ：设置当前页面中出于激活状态的选项卡的对象地址
	 * 
	 * @param
	 * @return void
	 */
	public void setGlobalTabView(View globalTabView) {
		if (pageData == null) {
			return;
		}
		pageData.setGlobalTabView(globalTabView);
	}

	/**
	 * 方法功能说明 ：得到当前页面的数据信息
	 * 
	 * @param
	 * @return BTCUiData
	 */
	public BTCUiData getUiData() {
		if (pageData == null) {
			return null;
		}
		return pageData.getUiData();
	}

	/**
	 * 方法功能说明 ：设置当前页面的数据信息
	 * 
	 * @param
	 * @return void
	 */
	public void setUiData(BTCUiData uiData) {
		if (pageData == null) {
			return;
		}
		pageData.setUiData(uiData);
	}

	/**
	 * 方法功能说明 ：得到当前页面中指定名字的输入框 的输入内容长度的下限约束
	 * 
	 * @param key
	 *            输入框的名字
	 * @return void
	 */
	public String getEditViewMinLength(String key) {
		if (pageData == null) {
			return null;
		}
		Map<String, String> map = pageData.getEditViewMinLength();
		if (map != null) {
			return map.get(key);
		}
		return null;
	}

	/**
	 * 方法功能说明 ：得到当前页面中所有的输入框的输入内容长度的下限约束
	 * 
	 * @param key
	 *            输入框的名字
	 * @return Map<String, String>
	 */
	public Map<String, String> getAllEditViewMinLength() {
		if (pageData == null) {
			return null;
		}
		return pageData.getEditViewMinLength();
	}

	/**
	 * 方法功能说明 ：设置当前页面中指定名字的输入框的输入内容的长度
	 * 
	 * @param key
	 *            输入框的名字 value 下限数值
	 * @return void
	 */
	public void setEditViewMinLength(String name, String value) {
		if (pageData == null) {
			return;
		}
		Map<String, String> map = pageData.getEditViewMinLength();
		if (map == null) {
			map = new HashMap<String, String>();
		}
		map.put(name, value);
		pageData.setEditViewMinLength(map);
	}
	
	/**
	 * 方法功能说明 ：得到当前页面中指定名字的输入框 的输入内容的长度
	 * 
	 * @param key
	 *            输入框的名字
	 * @return void
	 */
	public String getEditViewOnlyLength(String key) {
		if (pageData == null) {
			return null;
		}
		Map<String, String> map = pageData.getEditViewOnlyLength();
		if (map != null) {
			return map.get(key);
		}
		return null;
	}

	/**
	 * 方法功能说明 ：设置当前页面中指定名字的输入框的输入内容长度的下限约束
	 * 
	 * @param key
	 *            输入框的名字 value 下限数值
	 * @return void
	 */
	public void setEditViewOnlyLength(String name, String value) {
		if (pageData == null) {
			return;
		}
		Map<String, String> map = pageData.getEditViewOnlyLength();
		if (map == null) {
			map = new HashMap<String, String>();
		}
		map.put(name, value);
		pageData.setEditViewOnlyLength(map);
	}

	/**
	 * 判断是否为当前页面
	 * @param context
	 * @return
	 */
	public boolean contrastContext(Context context) {
		if (pageData == null) {
			return false;
		}
		if (pageData.getActivity() != context) {
			return false;
		} else {
			return true;
		}
	}


	/**
	 * 方法功能说明 ：设置当前页面id
	 * 
	 * @param
	 * @return void
	 */
	public void setId(String id) {
		pageData.setId(id);
	}

	/**
	 * 方法功能说明 ：得到当前页面id
	 * 
	 * @param
	 * @return void
	 */
	public String getId() {
		return pageData.getId();
	}

	/**
	 * 方法功能说明 ：设置当前页面缓存总大小
	 * 
	 * @param
	 * @return void
	 */
	public void setGroupsize(String groupsize) {
		pageData.setGroupsize(groupsize);
	}

	/**
	 * 方法功能说明 ：得到当前页面缓存总大小
	 * 
	 * @param
	 * @return void
	 */
	public String getGroupsize() {
		return pageData.getGroupsize();
	}
	
	
	
	/**
	 * 方法功能说明 ：得到当前页面中指定控件验证规则
	 * 
	 * @param key
	 * @return 
	 *           
	 * @return void
	 */
	public  Object getWidgetcheckrule(int key) {
		if (pageData == null) {
			return null;
		}
		Map<Integer, Object> map = pageData.getCheckrule();
		if (map != null) {
			return map.get(key);
		}
		return null;
	}

	/**
	 * 方法功能说明 ：设置当前页面中指定控件验证规则
	 * 
	 * @param key
	 *           
	 * @return void
	 */
	public void setWidgetcheckrule(Integer id, Object object) {
		if (pageData == null) {
			return;
		}
		Map<Integer, Object> map = pageData.getCheckrule();
		if (map == null) {
			map = new HashMap<Integer, Object>();
		}
		map.put(id, object);
		pageData.setCheckrule(map);
	}
	
}
