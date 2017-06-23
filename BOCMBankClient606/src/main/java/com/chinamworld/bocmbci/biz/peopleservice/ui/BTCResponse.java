package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;

import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.log.LogGloble;

public class BTCResponse extends BTCDrawLable{
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCResponse.class.getSimpleName();
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCResponse(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Response;
		//this.context = context;
	}

	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		String name=	btcElement.getElementName();
		BTCElement childElement;
		String value = null;
		BTCDrawLable btcDrawLable;
		if(childElements!=null && childElements.size()!=0){
			for (int i = 0; i < childElements.size(); i++) {
				childElement = childElements.get(i);

//				btcDrawLable = BTCLableFactory.createLableInstance(null,context,
//						childElement.getElementName());
				btcDrawLable = childElement.getBTCDrawLable();
				if(btcDrawLable != null){//P503生产，wuhan
					if( btcDrawLable instanceof BTCDatalist){
						if(btcElement.getChildElements().get(0).getName("name")!=null){
							 value=btcElement.getChildElements().get(0).getName("name");
						}else{
							value = "";
						}
						dbMap.put(value, value);
					}else {
						dbMap.put("response", "");
						btcDrawLable.drawLable(dbMap, null);
					}
				}
				LogGloble.i("info", "dbmap=="+dbMap);

			}
			return dbMap;
		}
		return null;
		
	}
}
