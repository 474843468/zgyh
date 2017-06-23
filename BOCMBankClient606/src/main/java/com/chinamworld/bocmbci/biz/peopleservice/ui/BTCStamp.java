package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;

import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;

public class BTCStamp extends BTCDrawLable{
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCStamp.class.getSimpleName();
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCStamp(Context context,BTCElement element) {
		super(context,element);
		//this.context = context;
	}


	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		String name=	btcElement.getElementName();
		BTCElement childElement;
		BTCDrawLable btcDrawLable;
		
		String content = childElements.get(0).getText();
		
		Map<String, String> dbb= new HashMap<String, String>();
		dbb.put("stamp", content);
		
//		if(params.containsKey("action")){
//			dbb.put("action", params.get("action"));
//		}
//		
//		for (int i = 0; i < childElements.size(); i++) {
//			childElement = childElements.get(i);
//
//			btcDrawLable = BTCLableFactory.createLableInstance(null,context,
//					childElement.getElementName());
//			if (btcDrawLable != null) {
//
//				btcDrawLable.drawLable(childElement, null, view);
//			}
//
//
//		}
		return dbb;
	}
}
