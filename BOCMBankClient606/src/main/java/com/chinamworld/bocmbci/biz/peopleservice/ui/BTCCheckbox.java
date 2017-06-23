package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLable;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLableAttribute;
import com.chinamworld.bocmbci.log.LogGloble;

public class BTCCheckbox extends BTCDrawLable{
	/** 上下文对象 */
//	private Context context;// 上下文对象
	private static final String TAG = BTCCheckbox.class.getSimpleName();
	/**界面栈对象*/
	private BTCActivityManager activityManager;// 界面栈对象
	/**全局变量对象*/
	private BTCCMWApplication cmwApplication;
	static String paramKeyss = "";
	Map<String, String> postParams = new HashMap<String, String>();
	List<String> optvalueList = new ArrayList<String>();
	String label;
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCCheckbox(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Checkbox;
	//	this.context = context;
		activityManager = BTCActivityManager.getInstance();
		cmwApplication = BTCUiActivity.getApp();
	}
	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		List <String> lst = new ArrayList<String>();
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		Map<String, Object> msg = new HashMap<String, Object>();
//		msg.put("name", params.get(BTCLableAttribute.NAME));
		msg.put("parent", view);
		String paramKey= "";
		if(params.containsKey("paramKey")){
			paramKey = params.get("paramKey");
			msg.put("paramKey",paramKey );
		}
		if(params.containsKey(BTCLableAttribute.LABEL)){
			label=params.get(BTCLableAttribute.LABEL);
		}else{
			label=childElements.get(0).getText();
		}
		String tag ="";
		if(params.get(BTCLableAttribute.NAME)!=null){
			msg.put("name", params.get(BTCLableAttribute.NAME));
//			tag = params.get(BTCLableAttribute.NAME) +  "-"+ params.get(BTCLableAttribute.VALUE);
			tag = params.get(BTCLableAttribute.NAME) +"-"+"paramKey"+paramKey;
		}else if(params.containsKey(BTCLableAttribute.LABEL)){
			msg.put("name", label);
			tag = label+"-"+"paramKey"+paramKey;
		}
		
		
		CheckBox  checkbox=new CheckBox(context);
		checkbox.setTextSize(15);
		checkbox.setTextColor(context.getResources().getColor(R.color.black));
		
		if(params.containsKey(BTCLableAttribute.BOUNDFLAG)){
			String tags=params.get(BTCLableAttribute.BOUNDFLAG);
			msg.put(BTCLableAttribute.BOUNDFLAG, tags);
		}
		if (params.containsKey(BTCLableAttribute.USEPROMPT)) {
			String useprompt = params.get(BTCLableAttribute.USEPROMPT);
			msg.put(BTCLableAttribute.USEPROMPT, useprompt);
		}
		
		if (params.containsKey(BTCLableAttribute.PROMPT)) {
			String prompt = params.get(BTCLableAttribute.PROMPT);
			msg.put(BTCLableAttribute.PROMPT, prompt);
		}
		
		if (params.containsKey(BTCLableAttribute.CHECKRULE)) {

			Object checkrule = params.get(BTCLableAttribute.CHECKRULE);
			msg.put("checkrule", checkrule);
			activityManager.setWidgetcheckrule(checkbox.getId(), checkrule);
		}
		msg.put("tag", tag);
		
		
		if(BTCCMWApplication.flowFileLangMap.get(label)!=null&&!BTCCMWApplication.flowFileLangMap.get(label).equals("")){
			checkbox.setText(BTCCMWApplication.flowFileLangMap.get(label).toString());	
		}else{
			checkbox.setText(label);		
		}
		cmwApplication.checkboxlist.put(paramKey, lst);
		if(params.containsKey(BTCLableAttribute.NAME)){
			cmwApplication.setInputCheckbox(params.get(BTCLableAttribute.NAME),tag);
		}else if(params.containsKey(BTCLableAttribute.LABEL)){
			cmwApplication.setInputCheckbox(params.get(BTCLableAttribute.LABEL),tag);
		}
		
		if (!cmwApplication.checklist.contains(tag)) {
			cmwApplication.checklist.add(tag);
		}
		int id = tag.hashCode();//262474127  262474127
		id = id > 0 ? id : -id;
		checkbox.setId(id);
		
		BTCElement childElement;
		BTCDrawLable btcDrawLable;

		for (int i = 0; i < childElements.size(); i++) {
			childElement = childElements.get(i);
			btcDrawLable = childElement.getBTCDrawLable();
			if (BTCLable.OPTVALUE.equals(childElement.getElementName())) {
				btcDrawLable.drawLable(postParams, null);
				for(Map.Entry<String, String> entrys : postParams.entrySet()){
					optvalueList.add(entrys.getValue());
				}
			}
		}
		
		if(optvalueList.size()!=0){
			msg.put("optValue", optvalueList.get(0));
		}
		checkbox.setTag(msg);
		checkbox.setOnCheckedChangeListener((OnCheckedChangeListener) checkboxOnClickListener);
		activityManager.putWidgetId(tag, id);
		((ViewGroup) view).addView(checkbox);
		return true;
	}
	
	/**
	 * checkbox按钮点击事件
	 */
	private OnCheckedChangeListener checkboxOnClickListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			Map<String, Object> msg = (Map<String, Object>)buttonView.getTag();
			
			String tag = (String) msg.get("tag");
			String name = (String) msg.get("name");
			String value = (String) msg.get("value");
			String optValue = (String) msg.get("optValue");
			List<String> optList = new ArrayList<String>();
			
			//保存点击请求参数
			Map<String, String> requestMap = new HashMap<String, String>();
//			requestMap = (Map<String, String>) msg.get("params");
//			BTCCMWApplication.getRadioRequest = requestMap;
			ArrayList<String> list = cmwApplication
					.getInputCheckboxValues(name);
			CheckBox checkBox ;
			for (int i = 0; i < list.size(); i++) {
				String currentTag = list.get(i);
				int id = currentTag.hashCode();
				if(currentTag.contains("paramKey")){
					String param = currentTag.substring(currentTag.indexOf("paramkey")+1, currentTag.length());
					paramKeyss = param;
				}
				id = id > 0 ? id : -id;
				checkBox = (CheckBox) ((Activity) context).findViewById(id);
				if (checkBox != null) {
					if (tag.equals(currentTag)) {
						checkBox.setChecked(true);
						optList.add(optValue);
						
					}
				}
				
			}
			if(isChecked){
//				Map<String, List<String>>
				if(!"".equals(paramKeyss)){
					cmwApplication.checkboxlist.put(paramKeyss, optList);
				}
				LogGloble.i("wuhan", cmwApplication.checkboxlist.toString());
			}else{
//				cmwApplication.checkboxlist.remove(postParams);
				LogGloble.i("wuhan", cmwApplication.checkboxlist.toString());
			}
			
		}
	};
}
