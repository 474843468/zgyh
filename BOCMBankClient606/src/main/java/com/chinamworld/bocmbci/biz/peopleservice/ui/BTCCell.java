package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;

public class BTCCell extends BTCDrawLable{
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCCell.class.getSimpleName();
	/**界面栈对象*/
	private BTCActivityManager activityManager;// 界面栈对象
	/**全局变量对象*/
	private BTCCMWApplication cmwApplication;
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCCell(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Cell;
		//this.context = context;
		activityManager = BTCActivityManager.getInstance();
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
		Map<String, String> db = dbMap;
		String type = params.get("type");//header
		LinearLayout childLayout = new LinearLayout(context);
		childLayout.setOrientation(LinearLayout.HORIZONTAL);
//		childLayout.setLayoutParams(new LayoutParams(0,LayoutParams.WRAP_CONTENT, 1));
		childLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
//		((ViewGroup) view).addView(childLayout);
		String keys= "" ;
		String radioPosition="" ;
		String othersPosition="";
		LinearLayout valueLinearLayout = new LinearLayout(context);
		for (int i = 0; i < childElements.size(); i++) {
			childElement = childElements.get(i);
			//wuhan
			btcDrawLable = childElement.getBTCDrawLable();
			if (btcDrawLable != null) {
				if (btcDrawLable instanceof BTCRadio) {
					BTCRadio radio  = new BTCRadio(context,childElement);
					if("0".equals(dbMap.get("loopIndex"))) {
						 //第一个默认为选中
						radio.setCheck(true);
					}
//					radio.drawLableCell(childElement, dbMap, view);
					radioPosition  = dbMap.get("position");
					int posi = Integer.parseInt(radioPosition);
					String keyHeader = BTCCMWApplication.headerList.get(posi);
					TextView textview = new TextView(context);
					textview.setTextSize(15);
					textview.setTextColor(context.getResources().getColor(R.color.black));
					if(keyHeader!=null&&!"".equals(keyHeader)){
						if(BTCUiActivity.getApp().getFlowFileLangMapByKey(keyHeader)!=null&&!BTCUiActivity.getApp().getFlowFileLangMapByKey(keyHeader).equals("")){
							textview.setText(BTCUiActivity.getApp().getFlowFileLangMapByKey(keyHeader).toString()+":");	
						}else{
							textview.setText(keyHeader);		
						}
					
					}
//					LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(
//							ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				
//					((ViewGroup) childLayout).addView(textview,childParams);
//					((ViewGroup) childLayout).addView(textview,new LayoutParams(0,LayoutParams.WRAP_CONTENT, 1));//by dl //
					((ViewGroup) childLayout).addView(textview,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, 1));//by dl +
					radio.drawLableCell(childElement, dbMap, childLayout);
//					btcDrawLable.drawLable(childElement, dbMap, childLayout);
				}else if(type!=null && type.equals("header")){//table 头的标志
					List<BTCElement> child = childElement.getChildElements();
					Map<String, String> paramMap = childElement.getParams();
					String text = child.get(0).getText();
					String key = paramMap.get("namespace");
					if(key!=null){
						keys =key+"."+text;//GHStatic.DepartmentName
					}else{
						keys =text;
					}
					
//					headerList.add(keys);
//					btcDrawLable.drawLable(childElement, dbMap, childLayout);
				}
				else if(BTCCMWApplication.headerList !=null && BTCCMWApplication.headerList.size()!=0)
				{
					if(othersPosition.equals(radioPosition)){
						othersPosition  = dbMap.get("position");
						int posi = Integer.parseInt(othersPosition);
						String keyHeader = BTCCMWApplication.headerList.get(posi);
						TextView textview = new TextView(context);
						textview.setTextSize(15);
						textview.setTextColor(context.getResources().getColor(R.color.black));
						if(keyHeader!=null&&!"".equals(keyHeader)){
							if(BTCUiActivity.getApp().getFlowFileLangMapByKey(keyHeader)!=null&&!BTCUiActivity.getApp().getFlowFileLangMapByKey(keyHeader).equals("")){
								textview.setText(BTCUiActivity.getApp().getFlowFileLangMapByKey((keyHeader).toString())+":");	
							}else{
								textview.setText(keyHeader);		
							}
						
						}
//						new LayoutParams(0,LayoutParams.WRAP_CONTENT, 1);
//						LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(
//								ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//						childLayout.setLayoutParams(new LayoutParams(0,LayoutParams.WRAP_CONTENT, 1));
//						((ViewGroup) childLayout).addView(textview,new LayoutParams(0,LayoutParams.WRAP_CONTENT, 1));//by dl  //
						
//						
						((ViewGroup) childLayout).addView(textview,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT, 1));//by dl + 
						dbMap.put("label", "aa");
						dbMap.put("cell", "cell");
						
						((ViewGroup) childLayout).addView(valueLinearLayout,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT,1));
						btcDrawLable.drawLable(dbMap, valueLinearLayout);
						
					}else{
						btcDrawLable.drawLable(dbMap, valueLinearLayout);
					}
				}else{
					
					btcDrawLable.drawLable(dbMap, childLayout);
				}
				
			}


		}
		((ViewGroup) view).addView(childLayout);
		return keys;
	}

}
