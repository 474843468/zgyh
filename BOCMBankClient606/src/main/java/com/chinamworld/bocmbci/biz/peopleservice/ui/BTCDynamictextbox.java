package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLableAttribute;

public class BTCDynamictextbox extends BTCDrawLable{
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCDynamictextbox.class.getSimpleName();
	/**全局变量对象*/
//	private BTCCMWApplication cmwApplication;
	/** 界面栈对象 */
	private BTCActivityManager activityManager;// 界面栈对象
	int max=-1;
	LinearLayout Dynamictextbox;  
	LinearLayout childview;
	LinearLayout lin;
	LinearLayout.LayoutParams childlayoutParams ;
 
	 BTCElement childElement;
	 BTCDrawLable btcDrawLable;
	 List<BTCElement> childElements; 
	
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCDynamictextbox(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Dynamictextbox;
	//	this.context = context;
		activityManager = BTCActivityManager.getInstance();
//		cmwApplication = BTCUiActivity.getApp();

	}
	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		Map<String, String> params = btcElement.getParams();
		childElements=btcElement.getChildElements();
		if(params.containsKey(BTCLableAttribute.MAX)){		
		max=Integer.parseInt(params.get(BTCLableAttribute.MAX));
		}
		
		if(params.containsKey(BTCLableAttribute.BINDTARGET)){
			String tag=params.get(BTCLableAttribute.BINDTARGET);	
		}
		
		LinearLayout.LayoutParams  DynamictextboxParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		 Dynamictextbox = new LinearLayout(context);	
		 childview = new LinearLayout(context);	
	
		 String name = String.valueOf(System.currentTimeMillis());
		int hashcode = name.hashCode();
		hashcode = hashcode > 0 ? hashcode : -hashcode;			
		childview.setId(hashcode);	 
		if(params.containsKey(BTCLableAttribute.BINDTARGET)){
			String tag=params.get(BTCLableAttribute.BINDTARGET);
			childview.setTag(tag);
			
		}
		
		for (int i = 0; i < childElements.size(); i++) {
			childElement = childElements.get(i);

			btcDrawLable = childElement.getBTCDrawLable();
			if (btcDrawLable != null) {
				btcDrawLable.drawLable(BTCCMWApplication.dynamictextboxmap, lin);
			}
		}
		
		
		BTCCMWApplication.listDynamictextbox.add(name);
		 activityManager.putWidgetId(name, hashcode);
			lin= new LinearLayout(context);	
			lin.setOrientation(LinearLayout.HORIZONTAL);
		
		 childlayoutParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		
		childview.setOrientation(LinearLayout.VERTICAL);
		((ViewGroup) childview).addView(lin,childlayoutParams);
		((ViewGroup) Dynamictextbox).addView(childview,childlayoutParams);
		((ViewGroup) view).addView(Dynamictextbox,DynamictextboxParams);
		Button add=new Button(context);
		add.setText("添加");
		add.setOnClickListener(addlistener);
		((ViewGroup) Dynamictextbox).addView(add,childlayoutParams);
		Button remove=new Button(context);
		remove.setText("删除");
		remove.setOnClickListener(removelistener);
		((ViewGroup) Dynamictextbox).addView(remove,childlayoutParams);		
			
		return true;
//		return null;
	}
	
	
	OnClickListener addlistener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			if(childview.getChildCount()<max){
				LinearLayout view = new LinearLayout(context);				
				view.setOrientation(LinearLayout.HORIZONTAL);
				LinearLayout.LayoutParams  childviewParams = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
				 BTCElement childElement;
				 BTCDrawLable btcDrawLable;
				for (int i = 0; i < childElements.size(); i++) {
					childElement = childElements.get(i);
//					btcDrawLable = BTCLableFactory.createLableInstance(null,context,
//							childElement.getElementName());
					btcDrawLable = childElement.getBTCDrawLable();
					if (btcDrawLable != null) {
						btcDrawLable.drawLable( BTCCMWApplication.dynamictextboxmap, view);
					}
				}
				((ViewGroup) childview).addView(view,childviewParams);
			}

		}
	};
OnClickListener removelistener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			if(childview.getChildCount()>1){
				
//				View view =childview.getChildAt(childview.getChildCount());
				childview.removeViewAt(childview.getChildCount()-1);
			}

		}
	};
}
