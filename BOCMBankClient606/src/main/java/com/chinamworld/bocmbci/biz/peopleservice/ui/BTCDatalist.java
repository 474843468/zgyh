package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;

import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLable;
import com.chinamworld.bocmbci.log.LogGloble;

public class BTCDatalist extends BTCDrawLable {
	/** 上下文对象 */
	// private Context context;// 上下文对象
	private static final String TAG = BTCDatalist.class.getSimpleName();

	/**
	 * 构造函数
	 *
	 * @param context
	 */
	public BTCDatalist(Context context, BTCElement element) {
		super(context, element);
		elementType = ElementType.Datalist;
		// this.context = context;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object drawLable(Map<String, String> dbMap, View view) {
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		String name = btcElement.getElementName();
		LogGloble.e(TAG, params.toString());
		LogGloble.e(TAG, childElements.toString());
		LogGloble.e(TAG, name.toString());
		LogGloble.e("info", TAG);
		BTCElement childElement;
		BTCDrawLable btcDrawLable;
		Map<String,String> paramList = new HashMap<String,String>(),tmpParamMap;
		View v = new View(context);
		v.setTag("IS_DATA_LIST");
		tmpParamMap = null;
		for (int i = 0; i < childElements.size(); i++) {
			childElement = childElements.get(i);
			btcDrawLable = childElement.getBTCDrawLable();
			if (btcDrawLable != null) {
				tmpParamMap =(Map<String,String>)btcDrawLable.drawLable(dbMap, v);
			}
		}
		boolean bKeyInHiddenbox = false;
		if(tmpParamMap != null){
			for(String k : tmpParamMap.keySet()){
				if (BTCCMWApplication.hiddenboxmap.containsKey("DATA_LIST")) {
					//hiddenbox中有DATA_LIST,由于list中每个数据的key都是相同的，只获取第一条数据判断是否key在hiddenbox中
					List<Object> list = new ArrayList<Object>();
					list = (List<Object>) BTCCMWApplication.hiddenboxmap.get("DATA_LIST");
					if((list != null) && (((Map<String,String>)list.get(0)).get(k) != null)){
						//key在hiddenBox, value在后续处理中设定
						paramList.put(k,k);
						bKeyInHiddenbox = true;
					}else{
						//key不在hiddenBox, value使用<param><use>的结果
						paramList.put(k,tmpParamMap.get(k));
					}
				}else{
					//hiddenbox中没有DATA_LIST，value使用<param><use>的结果
					paramList.put(k,tmpParamMap.get(k));
				}
			}
		}


		String key = params.get(BTCLable.NAME);// hospitalId
		String requestStr = "[";

		List<Object> list = new ArrayList<Object>();

		if (bKeyInHiddenbox == true) {
			//key在hiddenBox, value使用hiddenbox中保存的数据，会有多条数据
			list = (List<Object>) BTCCMWApplication.hiddenboxmap.get("DATA_LIST");
			for (int ia = 0; ia < list.size(); ia++) {
				requestStr +="{";
				for(String k : paramList.keySet()){
					requestStr = requestStr+"\""+k+"\""+":\""+((Map<String,String>)list.get(ia)).get(k)+"\""+",";
				}
				requestStr = requestStr.substring(0, requestStr.length() - 1);
				requestStr +="},";
			}
		}else{
			//key不在hiddenBox中，只有一条数据，value使用<param><use>的结果
			requestStr +="{";
			for(String k : paramList.keySet()){
				requestStr = requestStr+"\""+k+"\""+":\""+paramList.get(k)+"\""+",";
			}
			requestStr = requestStr.substring(0, requestStr.length() - 1);
			requestStr +="},";
		}

		requestStr = requestStr.substring(0, requestStr.length() - 1);
		requestStr = requestStr + "]";
		dbMap.clear();
		dbMap.put(key, requestStr);
		return dbMap;
	}
}
