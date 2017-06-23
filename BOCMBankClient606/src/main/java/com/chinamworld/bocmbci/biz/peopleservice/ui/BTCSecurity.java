package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;

import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;

public class BTCSecurity extends BTCDrawLable{
	/** 上下文对象 */
//	private Context context;// 上下文对象
	private static final String TAG = BTCSecurity.class.getSimpleName();
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCSecurity(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Security;
	//	this.context = context;
	}

	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		String name=	btcElement.getElementName();
		BTCElement childElement;
		BTCDrawLable btcDrawLable;
		Map<String, String> dbb= new HashMap<String, String>();
		if(params.containsKey("action")){
			dbb.put("action", params.get("action"));
		}
		Map<String, String> sonMap= new HashMap<String, String>();
		for (int i = 0; i < childElements.size(); i++) {
			childElement = childElements.get(i);

//			 btcDrawLable = BTCLableFactory.createLableInstance(null,context,
//					childElement.getElementName());
			 btcDrawLable = childElement.getBTCDrawLable();
			if (btcDrawLable != null) {

				sonMap =(Map<String, String>) btcDrawLable.drawLable(null, view);
				if(sonMap!=null && sonMap.containsKey("ssm")){ 
					
					dbb.put("ssm", sonMap.get("ssm"));
				}
//				"stamp":"{display=V;k=PlpsFlowFileTransTip;type=s}",
//				"ssm":"PlpsSmsCodeMessage"
				if(sonMap!=null && sonMap.containsKey("stamp"))
				{
					//还要对value进行处理。卡号：cardNumber ；充值金额：chargeAmount
					dbb.put("stamp", sonMap.get("stamp"));
				}
			}


		}
		return dbb;
	}
}
