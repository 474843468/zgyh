package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.TextPaint;
import android.text.TextUtils.TruncateAt;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IFunction;

public class BTCLink  extends BTCDrawLable{
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCLink.class.getSimpleName();
	private BTCCMWApplication cmwApplication;
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCLink(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Link;
		//this.context = context;
		cmwApplication = BTCUiActivity.getApp();
	}
	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		 Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		String name=	btcElement.getElementName();
		BTCElement childElement;
		BTCDrawLable btcDrawLable;		
		Button button = new Button(context);
		Map<String, String> map = dbMap;
		String text=params.get("label");
		
		if(dbMap!=null && dbMap.containsKey("label")){
			LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1);
			//params.get(BTCLableAttribute.NAME);//CUST_ID  getDepartments.hospitalName  
			button.setEllipsize(TruncateAt.END);
			button.setSingleLine();
			button.setGravity(Gravity.CENTER);
			button.setLayoutParams(childParams);
		}else{
			LinearLayout.LayoutParams arams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			button.setLayoutParams(arams);
		}
		if(BTCCMWApplication.flowFileLangMap.containsKey(text)&&BTCCMWApplication.flowFileLangMap.get(text).toString()!=null ){//
			button.setText(BTCCMWApplication.flowFileLangMap.get(text).toString());
		} else{
			button.setText(text);//
		}
//		{label=button_CancelBind, 
//		step=cancel, 
//		params=CARD_NO=hosidInfo1.CARD_NO,HOS_ID=hosidInfo1.HOS_ID,
//		flow=QueryAndCancelBind_flow}
		String flow = "";
		if(params.containsKey("flow")){
			flow = params.get("flow");
		}
		
		final String flows = flow;
		final String step= params.get("step");
//		if(params.containsKey("step")){
//			step = params.get("step");
//		}
		
		Map<String, Object> msg = new HashMap<String, Object>();
		msg.put("params", params);
		msg.put("map", map);
		button.setTag(msg);
		
		
		
		button.setOnClickListener(new OnClickListener() {
			
			BTCElement childElement;
			
			@Override
			public void onClick(View v) {
				BTCCMWApplication.linkParamsMap.clear();
				Map<String, Object> msg =(Map<String, Object>) v.getTag();
				Map<String, String> lopmap = (Map<String, String>) msg.get("map");
				Map<String, String> lopParams = (Map<String, String>) msg.get("params");
				Map<String, String> paramsMap = new HashMap<String, String>();
				String paramStrs="";
				if(lopParams.containsKey("params")){
					paramStrs = lopParams.get("params");
					if(paramStrs.contains(",")){
						String paramsStrs [] = paramStrs.split(",");
						//CARD_NO=hosidInfo1.CARD_NO
						for(int j = 0 ;j < paramsStrs.length;j++){
							String value[]= paramsStrs[j].split("=");
							if(lopmap!=null && lopmap.containsKey(value[1])){
								paramsMap.put(value[0], lopmap.get(value[1]));	
								BTCCMWApplication.requestMap.put(value[0], lopmap.get(value[1]));
							}else if(cmwApplication.getVar(value[1])!=null){
								paramsMap.put(value[0], cmwApplication.getVar(value[1]));
								BTCCMWApplication.requestMap.put(value[0], cmwApplication.getVar(value[1]));
							}else if(BTCCMWApplication.responsemap.containsKey(value[1])){
								paramsMap.put(value[0], BTCCMWApplication.responsemap.get(value[1]).toString());
							}
						}
						
					}else{
						String value[]=  lopParams.get("params").split("=");
//						paramsMap.put(value[0], value[1]);	//CARD_NO=hosidInfo1.CARD_NO
						if(lopmap!=null && lopmap.containsKey(value[1])){
							paramsMap.put(value[0], lopmap.get(value[1]));	
							BTCCMWApplication.requestMap.put(value[0], lopmap.get(value[1]));
						}else if(cmwApplication.getVar(value[1])!=null){
							paramsMap.put(value[0], cmwApplication.getVar(value[1]));
							BTCCMWApplication.requestMap.put(value[0], cmwApplication.getVar(value[1]));
						}else if(BTCCMWApplication.responsemap.containsKey(value[1])){
							paramsMap.put(value[0], BTCCMWApplication.responsemap.get(value[1]).toString());
						}
					}
				}
				
				BTCCMWApplication.linkParamsMap = paramsMap;
				
			String namflow = BTCCMWApplication.getFlowElement();
			
				if(!"".equals(flows)&&flows!=null && !namflow.equals(flows)){
					
					if(BTCCMWApplication.isContainSecurityInit ){//1.判断当前step中是否含有security，init
						//2.
						childElement = BTCCMWApplication.getStepmap().get(step);
						List<BTCElement> childList = childElement.getChildElements();//action,view
						if(childList.toString().contains("active")){
							//弹出安全因子对话框。
							BaseDroidApp.getInstanse().showSeurityChooseDialog(
									new OnClickListener() {
										@Override
										public void onClick(View v) {
											 BTCElement bt = BTCLink.this.findNearElemntBy(new IFunction(){
													@Override
													public <T> boolean func(T t) {
														String name = ((BTCElement)t).getName(null);
														if(name != null &&  name.equals(flows)){
															return true;
														}
														return false;
													}});	

											BTCUiActivity.Instance().navigationToActivity(bt);
//											BTCUiActivity.Instance().navigationToActivity(childElement);
											BTCCMWApplication.isContainSecurityActive = true;
//											BaseDroidApp.getInstanse().getSecurityChoosed();//选中的安全因子。
										}
									});
						}else{//不含有active
//							childElement = BTCCMWApplication.getStepmap().get(step);
//							BTCUiActivity.Instance().navigationToActivity(childElement);
							 BTCElement bt = BTCLink.this.findNearElemntBy(new IFunction(){
									@Override
									public <T> boolean func(T t) {
										String name = ((BTCElement)t).getName(null);
										if(name != null &&  name.equals(flows)){
											return true;
										}
										return false;
									}});	

							BTCUiActivity.Instance().navigationToActivity(bt);
						}
					}else{
//						childElement = BTCCMWApplication.getStepmap().get(step);
//						BTCUiActivity.Instance().navigationToActivity(childElement);
						 BTCElement bt = BTCLink.this.findNearElemntBy(new IFunction(){
								@Override
								public <T> boolean func(T t) {
									String name = ((BTCElement)t).getName(null);
									if(name != null &&  name.equals(flows)){
										return true;
									}
									return false;
								}});	

						BTCUiActivity.Instance().navigationToActivity(bt);
					}
					
					
				}else if(!"".equals(step) && flows!=null && namflow.equals(flows)){
					if(BTCCMWApplication.isContainSecurityInit ){//1.判断当前step中是否含有security，init
						//2.
						childElement = BTCCMWApplication.getStepmap().get(step);
						List<BTCElement> childList = childElement.getChildElements();//action,view
						if(childList.toString().contains("active")){
							//弹出安全因子对话框。
							BaseDroidApp.getInstanse().showSeurityChooseDialog(
									new OnClickListener() {
										@Override
										public void onClick(View v) {
											BTCUiActivity.Instance().navigationToActivity(childElement);
											BTCCMWApplication.isContainSecurityActive = true;
//											BaseDroidApp.getInstanse().getSecurityChoosed();//选中的安全因子。
										}
									});
						}else{//不含有active
							childElement = BTCCMWApplication.getStepmap().get(step);
							BTCUiActivity.Instance().navigationToActivity(childElement);
						}
					}else{
						childElement = BTCCMWApplication.getStepmap().get(step);
						BTCUiActivity.Instance().navigationToActivity(childElement);
					}
					
				}
					
				
			}
		} );
		
		((ViewGroup) view).addView(button);
		return true;
	}

	
	// IntentSpan 类
		public class IntentSpan extends ClickableSpan {

			private final OnClickListener listener;

			public IntentSpan(View.OnClickListener listener) {
				this.listener = listener;
			}

			@Override
			public void onClick(View view) {
				listener.onClick(view);
			}
			
			 @Override
			    public void updateDrawState(TextPaint ds) {
			        ds.setColor(ds.linkColor);
			        ds.setUnderlineText(false); 
			    }

		}
		
}
