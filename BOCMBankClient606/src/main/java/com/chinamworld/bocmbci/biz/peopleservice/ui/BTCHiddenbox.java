package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;

import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLable;

public class BTCHiddenbox extends BTCDrawLable {
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCHiddenbox.class.getSimpleName();
	private BTCCMWApplication cmwApplication;
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCHiddenbox(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Hiddenbox;
	//	this.context = context;
		cmwApplication = BTCUiActivity.getApp();
	}

	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
//		if(dbMap == null)
//			dbMap = new HashMap<String,HashMap<String,String>()String>();
		Map<String,Object> dbMaps= cmwApplication.hiddenboxmap;
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		String name = btcElement.getElementName();
		//getDepartments.hospitalId
		String  value= params.get(BTCLable.VALUELIST);//  amount 需要传递的数据域值的变量名称，使用此名称从应用上下文中取值，与数据域名称一一对应
		String key = params.get(BTCLable.FIELDLIST);//需要传递的数据域名称  hospitalId PayAmt
//		fileddlist key
//		valuelist value
		if(value.contains(",")){
			String valueParams[] = value.split(",");
			String keyParams[] = key.split(",");
			if(valueParams.length == keyParams.length){
				for(int j = 0;j<valueParams.length;j++){
					if(BTCCMWApplication.isDisp != null&&BTCCMWApplication.dispMap!=null &&BTCCMWApplication.isDisp.equals("1") && BTCCMWApplication.dispMap.containsKey(valueParams[j])){
						String values =BTCCMWApplication.dispMap.get(valueParams[j]).toString();
						dbMaps.put(keyParams[j], values);
					}
					else 
					if(BTCCMWApplication.linkParamsMap !=null && BTCCMWApplication.linkParamsMap.containsKey(valueParams[j])){
						String values =(String)BTCCMWApplication.linkParamsMap.get(valueParams[j]);
						dbMaps.put(keyParams[j], values);
					}
					else if(BTCCMWApplication.responsemap!=null&&BTCCMWApplication.responsemap.containsKey(valueParams[j])){
						Object values =(Object) BTCCMWApplication.responsemap.get(valueParams[j]);
						dbMaps.put(keyParams[j], values);
						if(cmwApplication.getVar(valueParams[j])!=null){
							cmwApplication.setVar(keyParams[j], values.toString());
						}
					}else if(cmwApplication.getVar(valueParams[j])!=null){
						String values = cmwApplication.getVar(valueParams[j]);
						dbMaps.put(keyParams[j], values);
					}else if(cmwApplication.hiddenboxmap.containsKey(valueParams[j])){
						cmwApplication.hiddenboxmap.put(keyParams[j], cmwApplication.hiddenboxmap.get(valueParams[j]));
					}else if(keyParams[j].equals("limitDate") && valueParams[j].equals("_SYSTEMDATETIME")){
						String dateSystem = BTCCMWApplication.flowFileLangMap.get("_SYSTEMDATETIME").toString();//2017/11/09 
						dateSystem=dateSystem.substring(0, dateSystem.indexOf(" "));
						cmwApplication.hiddenboxmap.put(keyParams[j], dateSystem);
					}
				}
			}else{
			
			}
		}else{
			if(BTCCMWApplication.isDisp != null&&BTCCMWApplication.dispMap!=null &&BTCCMWApplication.isDisp.equals("1") && BTCCMWApplication.dispMap.containsKey(value)){
				String values =BTCCMWApplication.dispMap.get(value).toString();
				dbMaps.put(value, values);
			}
			else if(BTCCMWApplication.linkParamsMap !=null && BTCCMWApplication.linkParamsMap.containsKey(value)){
				String values =(String)BTCCMWApplication.linkParamsMap.get(value);
				dbMaps.put(key, values);
			}else if(BTCCMWApplication.responsemap!=null&&BTCCMWApplication.responsemap.containsKey(value)){
				Object values =(Object) BTCCMWApplication.responsemap.get(value);
				dbMaps.put(key, values);
				if(cmwApplication.getVar(key)!=null){
					cmwApplication.setVar(key, values.toString());
				}
			}else if(cmwApplication.getVar(value)!=null){
				String values = cmwApplication.getVar(value);
				dbMaps.put(key, values);
			}else if(cmwApplication.getVar(key)!=null){
				if(BTCCMWApplication.optionMap.containsKey(cmwApplication.getVar(key))){//combox中的option
					String values = BTCCMWApplication.optionMap.get(cmwApplication.getVar(key));
					dbMaps.put(key, values);
				}else{
					String values =cmwApplication.getVar(key);
					dbMaps.put(key, values);
				}
				
			}else if(BTCCMWApplication.getRadioRequest !=null && BTCCMWApplication.getRadioRequest.containsKey(key)&&BTCCMWApplication.getRadioRequest.get(key)!=null){//在loop中有radio的情况
				dbMaps.put(key,BTCCMWApplication.getRadioRequest.get(key).toString());
			}else if(cmwApplication.hiddenboxmap.containsKey(value)){
				cmwApplication.hiddenboxmap.put(key, cmwApplication.hiddenboxmap.get(value));
			}else if(key.equals("limitDate") && value.equals("_SYSTEMDATETIME")){
				String dateSystem = BTCCMWApplication.flowFileLangMap.get("_SYSTEMDATETIME").toString();//2017/11/09 
				dateSystem=dateSystem.substring(0, dateSystem.indexOf(" "));
				cmwApplication.hiddenboxmap.put(key, dateSystem);
			}
		}
		
		
		
		return dbMap;
	}

}

