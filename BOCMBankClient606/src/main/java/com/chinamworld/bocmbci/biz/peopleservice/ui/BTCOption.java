package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;

import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLable;

public class BTCOption extends BTCDrawLable{
	/** 上下文对象 */
//	private Context context;// 上下文对象
	private static final String TAG = BTCOption.class.getSimpleName();
	
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCOption(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Option;
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
		String key =params.get(BTCLable.VALUE);
		String value=childElements.get(0).getText();
		if(params.containsKey("currentvalue")){
			String currentV = params.get("currentvalue");
			currentV  = currentV.substring(currentV.indexOf("{")+1, currentV.length()-1);
			BTCCMWApplication.optionValue.put("currentvalue", currentV);
		}
		
		if(childElements.get(0).getChildElements().size()!=0){
			//有子标签，寻找上层节点是否包含loop，针对<loop><option><use>的标签组合需特殊处理
			boolean bFoundLoop = false;
			BTCElement tempElement = btcElement;
			while(true) {
				ElementType tempType = tempElement.getParentElement().getBTCDrawLable().elementType;
				if((tempType == ElementType.None) || (tempType == ElementType.View)) {
					//找不到或找到根节点，退出
					break;
				}else if (tempType == ElementType.Loop) {
					//找到loop节点
					bFoundLoop = true;
					break;
				}
				tempElement = tempElement.getParentElement();
				if(tempElement == null){
					break;
				}
			}
			String valuesss="";
			for (int i = 0; i < childElements.size(); i++) {
				childElement = childElements.get(0);
				btcDrawLable = childElement.getBTCDrawLable();
//				btcDrawLable = BTCLableFactory.createLableInstance(null,context,
//						childElement.getElementName());
				if(btcDrawLable!=null && btcDrawLable instanceof BTCUse){
					if(bFoundLoop){
						/*针对<loop><option><use>的框架，use需返回当前字符串item.Display,在loop标签中进行具体数值的解析。
						*  <loop use="res.DATA_LIST" for="itemName">
						*      <option value = itemName.Key>
						*          <use>itemName.Display</use>
						*      </option>
						*  </loop>
						 */
						valuesss =childElement.getChildElements().get(0).getText().toString();
					}else{
						valuesss=(String)btcDrawLable.drawLable(null, null);//view
					}
				}
				else if(btcDrawLable!=null){
					
					valuesss=(String)btcDrawLable.drawLable(null, null);//view
				}else{
					valuesss = value;
				}
				dbMap.put(valuesss,key );
			}
			return dbMap;
		}else 
		{//， <option value="0101">RanQi.jhrq</option>
			if(BTCCMWApplication.flowFileLangMap.containsKey(value)){
				String valueName = (String) BTCCMWApplication.flowFileLangMap.get(value);
				dbMap.put(valueName,key);//济华燃气, 0101
//				return valueName;
				return dbMap;
			}else{
				dbMap.put(value,key);
//				return value;
				return dbMap;
			}
			
		}
		
		
	
		
	}

}
