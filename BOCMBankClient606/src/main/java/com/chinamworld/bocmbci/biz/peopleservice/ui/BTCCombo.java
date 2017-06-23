package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLable;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLableAttribute;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IFunction;

public class BTCCombo extends BTCDrawLable{
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCCombo.class.getSimpleName();
	/** 界面栈对象 */
	private BTCActivityManager activityManager;// 界面栈对象
	/** 全局变量对象 */
	private BTCCMWApplication cmwApplication;
	public List<String> spinnerList = new ArrayList<String>();
	//wuhan正确的P503 等某个批次再打开
	public String itemCotent="";
	boolean isfristLoad = false;
	int defaultItemIndex = 0;
	boolean defaultIndex = false;
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCCombo(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Combo;
		//this.context = context;
		activityManager = BTCActivityManager.getInstance();
		cmwApplication = BTCUiActivity.getApp();
	}
	
	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		ArrayList<String> accNumberList=new ArrayList<String>();
		spinnerList.clear();
//		BTCActcombo.accNumberList.clear();
		final Map<String, String> params = btcElement.getParams();
		final String  key=params.get(BTCLable.NAME);
		String lable = params.get("label");
		int hashcode =key.hashCode();
		hashcode = hashcode > 0 ? hashcode : -hashcode;
		if(lable==null){
			lable = "";
		}
		List<BTCElement> childElements = btcElement.getChildElements();
		Map<String, String> optionParams = new HashMap<String, String>();
		BTCElement childElement ;
		BTCDrawLable btcDrawLable;
//		wuhan正确的P503 等某个批次再打开
		isfristLoad = true;
		Spinner spinner = new Spinner(context);
		spinner.setId(hashcode);
//		LinearLayout.LayoutParams partext = new 
//		LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,35);
//		spinner.setLayoutParams(partext);
//		spinner.setBackgroundResource(R.drawable.bg_spinner);
		activityManager.putWidgetId(key,hashcode);
		//wuhan
		Map<String, Object> msg = new HashMap<String, Object>();
//		boolean flag =false;
		//wuhan
		if (params.containsKey(BTCLableAttribute.USEPROMPT)) {
			String useprompt = params.get(BTCLableAttribute.USEPROMPT);
			msg.put(BTCLableAttribute.USEPROMPT, useprompt);
		}
		if(params.containsKey(BTCLableAttribute.BINDTARGET)){
			String tag=params.get(BTCLableAttribute.BINDTARGET);
			msg.put(BTCLableAttribute.BINDTARGET, tag);
//			edittext.setTag(tag);
		}
		if (params.containsKey(BTCLableAttribute.PROMPT)) {
			String prompt = params.get(BTCLableAttribute.PROMPT);
			msg.put(BTCLableAttribute.PROMPT, prompt);
		}
		if (params.containsKey(BTCLableAttribute.CHECKRULE)) {

			Object checkrule = params.get(BTCLableAttribute.CHECKRULE);
//			if(checkrule.toString().contains("required:true")){
//				flag = true;
//			}
			activityManager.setWidgetcheckrule(spinner.getId(), checkrule);
		}
		
		String branch_stepp = "";
		 String branch_flowp = "";
		if(params.containsKey("onchange")){
//			onchange="branch_flow=NomedicalRegisterAppoint_flow,branch_step=twoLevelOfficeQuery"
			String onchange = params.get("onchange");
			String onchanges[] = onchange.split(",");
			String branch_flows[] = onchanges[0].split("=");
			branch_flowp = branch_flows[1];
			String branch_steps[] = onchanges[1].split("=");
			branch_stepp = branch_steps[1];
			
		}
		final String branch_step = branch_stepp;
		final String branch_flow = branch_flowp;
		
		Map<String, String> optionMap = new HashMap<String, String>();
		for (int i = 0; i < childElements.size(); i++) {
			childElement = childElements.get(i);
			btcDrawLable = childElement.getBTCDrawLable();
			if (BTCLable.OPTION.equals(childElement.getElementName())) {
//				String value=(String)btcDrawLable.drawLable(optionParams, view);
//				spinnerList.add(value);
				optionMap = (Map<String, String>) btcDrawLable.drawLable(optionParams, view);
//				optionParams = (Map<String, String>) btcDrawLable.drawLable(childElement, optionParams, view);
				for(Map.Entry<String, String> entry : optionMap.entrySet()){
					String optionKey = entry.getKey();
					String optionValue = entry.getValue();
					spinnerList.add(optionKey);//济华燃气, 0101
					BTCCMWApplication.optionMap.put(optionKey, optionValue);//key是显示的值，value是上送的值
				}
				optionMap.clear();
			}
			msg.put("chose", accNumberList);
			
			if (BTCLable.LOOP.equals(childElement.getElementName())) {
			 spinnerList = (List<String>)btcDrawLable.drawLable(optionParams, view);	
			}
								 
		}
		
		BTCCMWApplication.combomap.put(key, optionParams);//???

//		spinnerList.clear();
//		for(Map.Entry<String, String> entry:optionParams.entrySet()){
//			String keyOption = entry.getKey();//AD
//			String valueOption = entry.getValue();//=country002
//			if(BTCCMWApplication.flowFileLangMap.containsKey(valueOption)){
//				String valueName = (String) BTCCMWApplication.flowFileLangMap.get(valueOption);
//				spinnerList.add(valueName);
//			}else{
//				spinnerList.add(valueOption);
//			}
//		}
	
		if(params.containsKey("default")){//默认选中值  --请选择--
			String sele = params.get("default");
			if(BTCCMWApplication.flowFileLangMap.containsKey(sele)){
				itemCotent =BTCCMWApplication.flowFileLangMap.get(sele).toString();
				if(spinnerList.contains(itemCotent)){
					defaultIndex = true;
					defaultItemIndex = spinnerList.indexOf(itemCotent);
				}else{
					spinnerList.add(0,itemCotent);
				}
				
//				wuhan正确的P503 等某个批次再打开
				
			}else{
				spinnerList.add(0,sele);
				itemCotent = sele;
			}
		}
		
	
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
//				wuhan正确的P503 等某个批次再打开
				if(isfristLoad){
					isfristLoad  = false;
					return;
				}
//				defaultIndex = false;
				BTCCMWApplication.optionValue.put(key, spinnerList.get(arg2).toString());
				if(params.containsKey("onchange")&& !spinnerList.get(arg2).equals(itemCotent)){
					String widgetValue = spinnerList.get(arg2).toString();
					if(BTCCMWApplication.optionMap!=null && BTCCMWApplication.optionMap.containsKey(widgetValue)){
						cmwApplication.setVar(key,BTCCMWApplication.optionMap.get(widgetValue));//CityList,呈贡 UnitNO
					}else{
						cmwApplication.setVar(key,widgetValue);//CityList,呈贡 UnitNO
					}
					drawHidebox();
					BTCUiActivity.Instance().navigationToActivity(BTCCMWApplication.getStepmap().get(branch_step));
					
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, spinnerList);
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		//WUHAN
		spinner.setTag(msg);  
		String currentValue = "";
		if(BTCCMWApplication.optionValue.containsKey("currentvalue")&&key.equals(BTCCMWApplication.optionValue.get("currentvalue"))){
			if(BTCCMWApplication.optionValue.containsKey(key)&&BTCCMWApplication.optionValue.get(key)!=null){
				currentValue = BTCCMWApplication.optionValue.get(key);
				int position = spinnerList.indexOf(currentValue);
				spinner.setSelection(position);
//				spinner.notify();
			}
		}
		if(defaultIndex){
			defaultIndex = false;
			spinner.setSelection(defaultItemIndex);
		}
		((ViewGroup) view).addView(spinner, new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, 1));
		dbMap.put("lable", lable);
		return dbMap;
	}
	
	public void drawHidebox(){
		BTCDrawLable btcDrawLable;
		BTCElement viewElement = findNearElemntBy(new IFunction(){

			@Override
			public <T> boolean func(T t) {
				return ((BTCElement)t).getBTCDrawLable() instanceof BTCView;
			}});
		// 
		List<BTCElement> hiddenboxList = FindElementBy(null, viewElement, new IFunction(){

			@Override
			public <T> boolean func(T t) {
				return ((BTCElement)t).getBTCDrawLable() instanceof BTCHiddenbox;
			}});
		
		for(int i = 0;i < hiddenboxList.size();i++){
			BTCElement e = hiddenboxList.get(i);
			btcDrawLable = e.getBTCDrawLable();
			btcDrawLable.drawLable(null, null);
		}
		
	}
}

