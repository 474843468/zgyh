package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;

import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;

public class BTCFlow extends BTCDrawLable {
	/** 上下文对象 */
	//private Context context;// 上下文对象
	public static final String TAG = BTCFlow.class.getSimpleName();

	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCFlow(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Flow;
	//	this.context = context;
	}

	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
//		Map<String,BTCElement> flowmap = new HashMap<String,BTCElement>();
//		flowmap.put(btcElement.getParams().get("name"), btcElement);
//		//flowname.add(btcElement.getParams().get("name"));
//		
//		BTCCMWApplication.setFolwmap(flowmap);
//		
		Map<String, String> params = btcElement.getParams();
		Map<String, BTCElement> stepmap = new HashMap<String, BTCElement>();
		List<BTCElement> childElements = btcElement.getChildElements();
		String name = btcElement.getElementName();
		BTCElement childElement;
		BTCDrawLable btcDrawLable;
		for (int i = 0; i < childElements.size(); i++) {
			childElement = childElements.get(i);
			stepmap.put(childElement.getParams().get("name"), childElement);
		}
		BTCCMWApplication.setStepmap(stepmap);//保存step列表，用于有security中安全因子的判断。
		childElement = childElements.get(0);
//		btcDrawLable = BTCLableFactory.createLableInstance(null,context,
//				childElement.getElementName());
		btcDrawLable = childElement.getBTCDrawLable();
		if (btcDrawLable != null) {
			btcDrawLable.drawLable( null, view);
		}
		return true;
	}

	
}
