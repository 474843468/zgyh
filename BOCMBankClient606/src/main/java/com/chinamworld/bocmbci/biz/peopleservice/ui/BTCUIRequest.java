package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;

import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;

public class BTCUIRequest extends BTCDrawLable{
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCUIRequest.class.getSimpleName();
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCUIRequest(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Request;
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
	
		String requestStr = "{";
		for (int i = 0; i < childElements.size(); i++) {
			childElement = childElements.get(i);
//			btcDrawLable = BTCLableFactory.createLableInstance(null,context,
//					childElement.getElementName());
			btcDrawLable = childElement.getBTCDrawLable();
			
			if (btcDrawLable != null && btcDrawLable instanceof BTCParam) {
				dbMap.clear();
				btcDrawLable.drawLable(dbMap, null);
				if(dbMap.size()!=0){
					for(Map.Entry<String, String> entry:dbMap.entrySet()){
						requestStr = requestStr+"\""+entry.getKey()+"\""+":\""+entry.getValue()+"\""+",";
					}
				}
			}else if(btcDrawLable !=null && btcDrawLable instanceof BTCDatalist){
				
				dbMap.clear();
				btcDrawLable.drawLable(dbMap, null);
				if(dbMap.size()!=0){
					for(Map.Entry<String, String> entry:dbMap.entrySet()){
						requestStr = requestStr+"\""+entry.getKey()+"\""+":\""+entry.getValue()+"\""+",";
					}
				}
			}
		}
		requestStr = requestStr.substring(0, requestStr.length()-1);
		requestStr = requestStr+"}";
		return requestStr;
	}
}
