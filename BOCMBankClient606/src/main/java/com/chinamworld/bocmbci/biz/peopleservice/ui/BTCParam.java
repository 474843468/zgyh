package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;

import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLable;

public class BTCParam extends BTCDrawLable {
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCParam.class.getSimpleName();

	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCParam(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Param;
	//	this.context = context;
	}

	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		BTCElement childElement;
		BTCDrawLable btcDrawLable;
		String value = null;
		String _CIFNUMBER="";
		Map<String, String> params = btcElement.getParams();
		String key = params.get(BTCLable.NAME);// hospitalId
		List<BTCElement> childElements = btcElement.getChildElements();
		if(childElements.size()!=0){
			childElement = childElements.get(0);
			btcDrawLable = childElement.getBTCDrawLable();
			if (btcDrawLable != null) {
				//wuhan 兼容response标签里面含有use标签的情况
				if(dbMap != null &&dbMap.containsKey("response")&&"use".equals(childElement.getElementName())){
					 value=btcElement.getChildElements().get(0).getChildElements().get(0).getText();
				}else{
					if("use".equals(childElement.getElementName())&& childElement.getChildElements().size()!=0){
						value= (String) btcDrawLable.drawLable(dbMap, view);
					}else{
						if(btcElement.getChildElements().get(0).getText()!=null){
							 value=btcElement.getChildElements().get(0).getText();
						}else{
							value = "";
						}
						
					}
				}
				
				
				
			} 
		}else{
			value = "";
		}
		dbMap.put(key, value);

		return dbMap;
	}
}
