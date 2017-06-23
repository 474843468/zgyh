package com.chinamworld.bocmbci.biz.peopleservice.ui;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLableAttribute;

public class BTCRadio extends BTCDrawLable{
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCRadio.class.getSimpleName();
	/**全局变量对象*/
	private BTCCMWApplication cmwApplication;
	/** 界面栈对象 */
	private BTCActivityManager activityManager;// 界面栈对象
	String check;
	String label;
	/** 是否选中 */
	
	private boolean isCheck = false;
	
	public boolean isCheck() {
		return isCheck;
	}


	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}


	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCRadio(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Radio;
	//	this.context = context;
		activityManager = BTCActivityManager.getInstance();
		cmwApplication = BTCUiActivity.getApp();
	}
	
	
	public Object drawLableCell(BTCElement btcElement, Map<String, String> dbMap,
			View view) {
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		String name=	btcElement.getElementName();
		Map<String, String> dd=dbMap;
		RadioButton radioBotton = new RadioButton(context);
		if(dbMap!=null && dbMap.containsKey("label")){
			LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1);
			//params.get(BTCLableAttribute.NAME);//CUST_ID  getDepartments.hospitalName  
			radioBotton.setEllipsize(TruncateAt.END);
			radioBotton.setSingleLine();
			radioBotton.setGravity(Gravity.LEFT);
			radioBotton.setLayoutParams(childParams);
		}
//		{department.admintitle=1, department.sex=1, 
//		department.specialties=呼吸内科常见疾病及胸部影像阅片，支气管镜检查与治疗, 
//				department.index=0, department.id=6fb0b6e7-9ee9-4738-8001-18e8aa78b948, 
//				department.name=柏长青, department.academictype=3}
		String tag ="";
		if(dbMap!=null){
			for(Map.Entry<String, String> entry:dd.entrySet()){
	    		String key = entry.getKey();
	    		String value = entry.getValue();
	    		tag = tag+key+"="+value+",";
	    	}
		}
		
		Map<String, Object> msg = new HashMap<String, Object>();
		
		msg.put("name", params.get(BTCLableAttribute.NAME));

		//radio中有params时，上送params的数据，没有时，有value，上送value的值。
		
		msg.put("parent", view);
//		{params=departmentId:${department.id},departmentName:${department.name},currentIndex:${department.index}, name=departmentId}
		String reqeustParams = params.get("params");
		//		departmentId:${department.id},departmentName:${department.name},currentIndex:${department.index}
		//将上送数据组织为上面的格式。
		String key,value,truValue;
		Map<String, String> requestMap = new HashMap<String, String>();
		if (reqeustParams!=null) {
			if(reqeustParams.contains(",")){
				String amount[] = reqeustParams.split(",");
				//departmentId:${department.id},departmentName:${department.name},currentIndex:${department.index}
				for(int i=0;i<amount.length;i++){
					String str = amount[i];
					if(str.contains(":")){//departmentId:${department.id}
						String item[] = str.split(":");
						key = item[0];
						value = item[1].substring(2, item[1].length()-1);
						truValue=dbMap.get(value);
						requestMap.put(key, truValue);
	//  departmentId：6fb0b6e7-9ee9-4738-8001-18e8aa78b948将其组织成这样的形式，在监听事件中，取出tag中的值，放入到requestMap中，做为上送的值。
					}
//					departmentName:department.name
				}
				msg.put("params", requestMap);//将其保存在application中。
			}else{
				String str = reqeustParams;
				if(str.contains(":")){//departmentId:${department.id}
					String item[] = str.split(":");
					key = item[0];
					value = item[1].substring(2, item[1].length()-1);
					truValue=dbMap.get(value);
					requestMap.put(key, truValue);
				}
				msg.put("params", requestMap);//将其保存在application中。
			}
			
		}
		
		if(params.containsKey(BTCLableAttribute.CHECKED)){
			check=params.get(BTCLableAttribute.CHECKED);	
		}else{
			check="false";
		}
		if(params.containsKey(BTCLableAttribute.BOUNDFLAG)){
			msg.put("boundflag", params.get(BTCLableAttribute.BOUNDFLAG));
		}
		if(params.containsKey(BTCLableAttribute.LABEL)){
			label=params.get(BTCLableAttribute.LABEL);
		}else{
//			label=childElements.get(0).getText();
		}
		
		radioBotton.setTextSize(15);
		radioBotton.setTextColor(context.getResources().getColor(R.color.black));
		int id = tag.hashCode();//取dbmap里面的唯一值进行 284926938  28188248  1217820153
		id = id > 0 ? id : -id;
		radioBotton.setId(id);
//		radioBotton.setText(label);
		if(label!=null){
			radioBotton.setText(label);
		}else {
			if(childElements.size()!=0){
				String keys =childElements.get(0).getText();
				if(BTCCMWApplication.flowFileLangMap.containsKey(keys)&&BTCCMWApplication.flowFileLangMap.get(keys).toString()!=null ){//
					radioBotton.setText(BTCCMWApplication.flowFileLangMap.get(keys).toString());
				}
			}			
		}
		msg.put("tag", tag);
		if(isCheck() || check.equals("true")){
			radioBotton.setChecked(true);
		}
		radioBotton.setTag(msg);
		cmwApplication.setInputCheckbox(params.get(BTCLableAttribute.NAME),tag);
		if (!cmwApplication.listradio.contains(tag)) {
			cmwApplication.listradio.add(tag);
		}
		/*by dl + lines 6 北京301医院预约挂号、查询预约*/
		if (radioBotton.getText().toString().equals("")) {
			radioBotton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		}else{
			radioBotton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));
		}
//		radioBotton.setTextSize(13);//by dl // 调整同意字体
		radioBotton.setOnClickListener(radioOnClickListener);

		activityManager.putWidgetId(tag, id);//柏长青  284926938  李艳  28188248 牛文凯  1217820153
//		activityManager.putWidgetId(params.get(BTCLableAttribute.NAME), id);
		((ViewGroup) view).addView(radioBotton);
		return null;
	}
	
	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		Map<String, String> dd=dbMap;
		String tag ="";
		if(params.get(BTCLableAttribute.VALUE)!=null){
			tag = params.get(BTCLableAttribute.NAME) +  "-"+ params.get(BTCLableAttribute.VALUE);
		}else if(params.get(BTCLableAttribute.LABEL)!=null){
			tag = params.get(BTCLableAttribute.NAME) +  "-"+ params.get(BTCLableAttribute.LABEL);
		}else {
			if(dbMap!=null){
				String keys="";
				 for(Map.Entry<String, String> entrys : dbMap.entrySet()){
					   keys = entrys.getKey().toString();
					   break;
				   }
				 tag = params.get(BTCLableAttribute.NAME) +  "-"+ dbMap.get(keys); 
			}
		}
//		String tag = params.get(BTCLableAttribute.NAME) +  "-"
//				+ params.get(BTCLableAttribute.VALUE);
		Map<String, Object> msg = new HashMap<String, Object>();
		msg.put("tag", tag);
		msg.put("name", params.get(BTCLableAttribute.NAME));

		//radio中有params时，上送params的数据，没有时，有value，上送value的值。
		
		if(!"".equals(params.get(BTCLableAttribute.VALUE))&&params.get(BTCLableAttribute.VALUE)!=null){
			msg.put("value", params.get(BTCLableAttribute.VALUE));
		}else{
			msg.put("value", "");
		}
		
		msg.put("parent", view);
//		{params=departmentId:${department.id},departmentName:${department.name},currentIndex:${department.index}, name=departmentId}
		String reqeustParams = params.get("params");
		//		departmentId:${department.id},departmentName:${department.name},currentIndex:${department.index}
		//将上送数据组织为上面的格式。
		String key,value,truValue;
		Map<String, String> requestMap = new HashMap<String, String>();

		
//		wuhan正确的P503 等某个批次再打开

		if (reqeustParams!=null) {
			if(reqeustParams.contains(",")){
				String amount[] = reqeustParams.split(",");
				//departmentId:${department.id},departmentName:${department.name},currentIndex:${department.index}
				for(int i=0;i<amount.length;i++){
					String str = amount[i];
					if(str.contains(":")){//departmentId:${department.id}
						String item[] = str.split(":");
						key = item[0];
						value = item[1].substring(2, item[1].length()-1);
						truValue=dbMap.get(value);
						requestMap.put(key, truValue);
	//  departmentId：6fb0b6e7-9ee9-4738-8001-18e8aa78b948将其组织成这样的形式，在监听事件中，取出tag中的值，放入到requestMap中，做为上送的值。
					}
//					departmentName:department.name
				}
				msg.put("params", requestMap);//将其保存在application中。
			}else{
				String str = reqeustParams;
				if(str.contains(":")){//departmentId:${department.id}
					String item[] = str.split(":");
					key = item[0];
					value = item[1].substring(2, item[1].length()-1);
					truValue=dbMap.get(value);
					requestMap.put(key, truValue);
				}
				msg.put("params", requestMap);//将其保存在application中。
			}
			
		}
		
		if(params.containsKey(BTCLableAttribute.CHECKED)){
			check=params.get(BTCLableAttribute.CHECKED);	
		}else{
			check="false";
		}
		if(params.containsKey(BTCLableAttribute.BOUNDFLAG)){
			msg.put("boundflag", params.get(BTCLableAttribute.BOUNDFLAG));
		}
		if(params.containsKey(BTCLableAttribute.LABEL)){
			label=params.get(BTCLableAttribute.LABEL);
		}else{
//			label=childElements.get(0).getText();
		}
		RadioButton radioBotton = new RadioButton(context);
		/*by dl + lines 2*/
		radioBotton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));
		radioBotton.setTextSize(15);
		radioBotton.setTextColor(context.getResources().getColor(R.color.black));
		int id = tag.hashCode();//262474127  262474127
		id = id > 0 ? id : -id;
		radioBotton.setId(id);
		if(label!=null){
			if(BTCCMWApplication.flowFileLangMap.containsKey(label)){
				radioBotton.setText(BTCCMWApplication.flowFileLangMap.get(label).toString());
			}else{
				radioBotton.setText(label);
			}
		}else {
			String keys =childElements.get(0).getText();
			if(BTCCMWApplication.flowFileLangMap.containsKey(keys)&&BTCCMWApplication.flowFileLangMap.get(keys).toString()!=null ){//
				radioBotton.setText(BTCCMWApplication.flowFileLangMap.get(keys).toString());
			}
		}
		if(isCheck() || check.equals("true")){
			radioBotton.setChecked(true);
		}
		radioBotton.setTag(msg);
		cmwApplication.setInputCheckbox(params.get(BTCLableAttribute.NAME),tag);
//		if (!cmwApplication.listradio.contains(params.get(BTCLableAttribute.NAME))) {
//			cmwApplication.listradio.add(params.get(BTCLableAttribute.NAME));
//		}
		if (!cmwApplication.listradio.contains(tag)) {
			cmwApplication.listradio.add(tag);
		}
		
		radioBotton.setOnClickListener(radioOnClickListener);
		activityManager.putWidgetId(tag, id);
		((ViewGroup) view).addView(radioBotton);
		return null;
	}
	/**
	 * 单选按钮点击事件
	 */
	private OnClickListener radioOnClickListener = new View.OnClickListener() {

		@SuppressWarnings("static-access")
		@Override
		public void onClick(View v) {
			Map<String, Object> msg = (Map<String, Object>) v.getTag();
			
			String tag = (String) msg.get("tag");
			//department.admintitle=10,department.specialties=null,department.sex=1,department.index=0,department.id=4a78a057-7e42-4a0a-b8e2-2addacd50c3e,department.name=牛文凯,department.academictype=3,
			String name = (String) msg.get("name");
			String value = (String) msg.get("value");
			
			//保存点击请求参数
			Map<String, String> requestMap = new HashMap<String, String>();
			requestMap = (Map<String, String>) msg.get("params");
			BTCCMWApplication.getRadioRequest = requestMap;
			ArrayList<String> list = cmwApplication
					.getInputCheckboxValues(name);
			RadioButton radioButton;

			for (int i = 0; i < list.size(); i++) {
				String currentTag = list.get(i);
				int id = currentTag.hashCode();
				id = id > 0 ? id : -id;
				radioButton = (RadioButton) ((Activity) context)
						.findViewById(id);
				if (radioButton != null) {
					if (tag.equals(currentTag)) {
						radioButton.setChecked(true);
						cmwApplication.setRadioVar(name, value);//name,value并不是准确的，有候会为空
					} else {
						radioButton.setChecked(false);
						
					}
				}
				
			}
		}
	};
}

