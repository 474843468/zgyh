package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IHttpCallBack;
import com.chinamworld.bocmbci.constant.ConstantGloble;

public class BTCRegisterdb extends BTCDrawLable {
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private BTCCMWApplication cmwApplication;
	BTCUiActivity uiActivity;
	private static final String TAG = BTCRegisterdb.class.getSimpleName();
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCRegisterdb(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Registerdb;
	//	this.context = context;
		cmwApplication = BTCUiActivity.getApp();
		this.uiActivity = BTCUiActivity.getInstance();
	}
	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		String name=	btcElement.getElementName();
		BTCElement childElement;
		BTCDrawLable btcDrawLable;
		String summarys = "";
		String summary = "";
		String summaryValue = "";
		String argskey = "";
		
		String flowname = "";
		String stepname = "";
		String isdisp = "";
		if(params!=null && params.containsKey("summary"))
		{
			summary = params.get("summary");//简要信息
		}
		if(params!=null && params.containsKey("flowname")){
			flowname = params.get("flowname");
		}
		if(params != null && params.containsKey("stepname")){
			stepname = params.get("stepname");
		}
		if(params !=null && params.containsKey("isdisp")){
			isdisp = params.get("isdisp");
			if(isdisp.equals("true")){
				isdisp = "1";
			}else if(isdisp.equals("false")){
				isdisp = "0";
			}else{
				isdisp = params.get("isdisp");
			}
		}
		String argskeyb = "";
		if(params!=null && params.containsKey("argskey"))//记录参数
		{
//			UserCodedx,fillAmount
			argskey = params.get("argskey");
			if(argskey.contains(",")){
				String argskeys [] = argskey.split(",");
				for(int i = 0 ;i<argskeys.length;i++){
					argskeyb += getValue(dbMap, argskeys[i])+"&";
				}
				argskeyb = argskeyb.substring(0, argskeyb.length()-1);
			}else{
				argskeyb = getValue(dbMap, argskey);
			}
			
		}
		if(params!=null && params.containsKey("argskey"))//记录参数
		{
//			UserCodedx,fillAmount
			argskey = params.get("argskey");
			if(argskey.contains(",")){
				argskey = argskey.replace(",", "&");
//				String argskeys [] = argskey.split(",");
//				for(int i = 0 ;i<argskeys.length;i++){
//					argskeyb += getValue(dbMap, argskeys[i])+",";
//				}
//				argskeyb = argskeyb.substring(0, argskeyb.length()-1);
			}else{
//				argskeyb = getValue(dbMap, argskey);
			}
			
		}
		
		
//		String summValuesb="";
//		 if(BTCCMWApplication.flowFileLangMap.get(summary)!=null&&!BTCCMWApplication.flowFileLangMap.get(summary).equals("")){
////			 上次缴费联通手机号：{0},缴费金额：{1}
//			 summaryValue = BTCCMWApplication.flowFileLangMap.get(summary).toString();
//			 
//			 if(summaryValue.contains(",")){
//				 String summaryValues[] = summaryValue.split(",");
//				 for( int i = 0 ;i<summaryValues.length;i++){
//					 String suValue = summaryValues[i].substring(0,summaryValues[i].indexOf("{")-1);
//					 summValuesb += suValue+",";
//				 }
//				 summValuesb = summValuesb.substring(0,summValuesb.length()-1);
//			 }else{
//				 String suValue = summaryValue.substring(0,summaryValue.indexOf("{")-1);
//				 summValuesb = suValue;
//			 }
//		 }
		 
//		 /**
//			 * 组装简要信息
//			 */
//		 if(summValuesb.contains(",")){
//			 String summValues[] = summValuesb.split(",");
//			 String argskeyValues [] = argskeyb.split(",");
//		 
//			 if(summValues.length == argskeyValues.length){
//				 for(int j = 0;j <summValues.length;j++){
//					summarys +=summValues[j]+":"+argskeyValues[j]+",";
//				}
//				 summarys = summarys.substring(0, summarys.length()-1);
//			 }
//		 }else{
//			 summarys = summValuesb+argskeyb;
//		 }
		 
		 final String flowNam = flowname;
		 final String isdis = isdisp;
		 final String stepNam = stepname;
		 final String argske = argskey;
		 final String summar = summary;
		 final String argsvalu = argskeyb;
		 requestToken(new IHttpCallBack() {
			@Override
			public void requestHttpSuccess(Object result) {
				String token = (String)result;
				RegisterDbRecord(isdis, argske, argsvalu, summar, flowNam, stepNam, token, null);
				
			}
		}); 
		 TextView  textview= new TextView(context);	
		 textview.setTextSize(15);
		 textview.setTextColor(context.getResources().getColor(R.color.black));
//		 textview.setText(summarys);
		 
//		for (int i = 0; i < childElements.size(); i++) {
//			childElement = childElements.get(i);
//			btcDrawLable = BTCLableFactory.createLableInstance(null,context,
//					childElement.getElementName());
//			if (btcDrawLable != null) {
//				btcDrawLable.drawLable(childElement, null, view);
//			}
//		}
		 ((ViewGroup) view).addView(textview);
		return view;
	}
	
	
//	String token = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
	
	private void requestToken(final IHttpCallBack callback){
//     
		uiActivity.requestRegisterDbTokenCallBack(new IHttpCallBack() {
			
			@Override
			public void requestHttpSuccess(Object result) {
				String token = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
			    if(callback != null) {
			    	callback.requestHttpSuccess(token);
			    }
			
			}
		});
		
	}
	
	private void RegisterDbRecord(String isDisp,String argsKey,String argsVal,
			String summary,String flowName,String stepName,String token,final IHttpCallBack callback){
		uiActivity.RegisterDbRecordCallBack(isDisp, argsKey, argsVal, summary, flowName, stepName, token,callback);
	}
	
	private String getValue(Map<String, String> dbMap,String key){
		String value = "";
		 if(key!=null&&!"".equals(key)){
				if(BTCCMWApplication.responsemap.containsKey(key)&&BTCCMWApplication.responsemap.get(key).toString()!=null ){//
					value = BTCCMWApplication.responsemap.get(key).toString();
				} else if(BTCCMWApplication.hiddenboxmap.containsKey(key)&&BTCCMWApplication.hiddenboxmap.get(key).toString()!=null &&!"".equals(BTCCMWApplication.hiddenboxmap.get(key).toString())){
					value =BTCCMWApplication.hiddenboxmap.get(key).toString();
				}else if(dbMap!=null&&dbMap.containsKey(key)&&!"".equals(key)&&dbMap.get(key)!=null){
					value =dbMap.get(key);
				}else if(BTCCMWApplication.flowFileLangMap.containsKey(key)&&BTCCMWApplication.flowFileLangMap.get(key).toString()!=null ){
					value =BTCCMWApplication.flowFileLangMap.get(key).toString();
				
				}else if(cmwApplication.getVar(key)!=null){//<param name="CUST_ID"><use>CUST_ID</use></param>
					value =cmwApplication.getVar(key);
				}
//				else if(BTCCMWApplication.resultmap.containsKey(key)&&BTCCMWApplication.resultmap.get(key).toString()!=null){
//					//网络请求回来的数据
//					value =BTCCMWApplication.resultmap.get(key).toString();
//				}
				else if(BTCCMWApplication.getRadioRequest.containsKey(key)&&BTCCMWApplication.getRadioRequest.get(key).toString()!=null){//在loop中有radio的情况
					value =BTCCMWApplication.getRadioRequest.get(key).toString();
				}
			}
		 return value;
	}
	
	
}
