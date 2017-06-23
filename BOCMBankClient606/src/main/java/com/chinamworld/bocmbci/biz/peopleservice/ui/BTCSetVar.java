package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCGlobal;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLableAttribute;


/**
 * 类功能描述：用于处理setvar标签
 * 
 * @author：秦
 * @version： 1.0
 * @see 包名：com.chinamworld.btwapview.ui
 *      父类：com.chinamworld.btwapview.ui.BTCDrawLable 相关数据：
 */
public class BTCSetVar extends BTCDrawLable {

	@SuppressWarnings("unused")
	/**上下文对象*/
//	private Context context;// 上下文对象
	/** 界面栈管理对象 */
	private BTCActivityManager activityManager;// 界面栈管理对象
	/** 全局变量管理对象 */
	private BTCCMWApplication cmwApplication;// 全局变量管理对象

	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCSetVar(Context context,BTCElement element) {
		super(context,element);
		//this.context = context;
		cmwApplication = BTCUiActivity.getApp();
		activityManager = BTCActivityManager.getInstance();
	}

	/**
	 * 方法功能说明 ：重写父类BTCDrawLable的该方法,在这个方法中,处理setvar标签
	 * 
	 * @param btcElement
	 *            setvar标签的实体对象 dbMap 数据库查询结果集的一条记录的数据信息 view 父容器
	 * @return BTCKeyValuePair
	 */
	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		if (btcElement == null || btcElement.getParams() == null) {
			return null;
		}
		Map<String, String> params = btcElement.getParams();
		cmwApplication.setVar(params.get(BTCLableAttribute.NAME),
				params.get(BTCLableAttribute.VALUE));
		return true;
	}

	/**
	 * 方法功能说明 ：设置本地变量
	 * 
	 * @param varMap
	 * @param dbMap
	 */
	@SuppressWarnings("unchecked")
	protected void setVar(Map<String, String> varMap, Map<String, String> dbMap) {
		Iterator it = varMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String name = (String) entry.getKey();
			String value = (String) entry.getValue();
			if (value == null) {
				value = "";
			} else if (value.startsWith(BTCGlobal.DOLOR_LEFT_S)
					&& value.endsWith(BTCGlobal.DOLOR_RIGHT_S)) {
				// 先从控件中取，如果没有相应的就从本地变量中取
				String widgetName = value.substring(2, value.length() - 1);
				int hashcode = widgetName.hashCode();
				hashcode = hashcode > 0 ? hashcode : -hashcode;
				View view = activityManager.currentActivity().findViewById(
						hashcode);
				if (view != null) {
					if (view instanceof EditText) {
						// 从相应的变量中取得用户输入或者选择的值
						EditText editText = (EditText) view;
						value = editText.getText().toString();
					} else if (view instanceof Spinner) {
						Spinner spinner = (Spinner) view;
						Map<String, String> map1 = (Map<String, String>) spinner
								.getSelectedItem();
						value = map1.get(BTCLableAttribute.VALUE);
					}
				} else {
					String value1 = cmwApplication.getVar(value.substring(2,
							value.length() - 1));
					// if(value1 == null){
					// value1 =
					// cmwApplication.getInputCheckboxValues(value.substring(2,
					// value.length() - 1)).toString();
					// }
					value = value1;
				}
				// 设置全局变量
				cmwApplication.setVar(name, value);
			} else if (value.startsWith(BTCGlobal.DOLOR_LEFT)
					&& value.endsWith(BTCGlobal.DOLOR_RIGHT)) {
				String columnName = value.substring(2, value.length() - 1);
				value = dbMap.get(columnName);
			}
			// 设置全局变量
			cmwApplication.setVar(name, value);
		}
	}

}
