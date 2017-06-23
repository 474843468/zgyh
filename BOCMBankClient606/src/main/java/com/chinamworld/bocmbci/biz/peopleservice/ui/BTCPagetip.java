package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;

public class BTCPagetip extends BTCDrawLable{
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCPagetip.class.getSimpleName();
	String position=null;
	String viewId=null;
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCPagetip(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Pagetip;
	//	this.context = context;
	}
	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		String name=btcElement.getElementName();
		BTCElement childElement;
		BTCDrawLable btcDrawLable;
		//wuhan
		if (params != null && params.containsKey("viewid")) {
			viewId=params.get("viewid");	
		}
		
		if (params != null && params.containsKey("position")) {
			position=params.get("position");	
		}
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			LinearLayout childLayout = new LinearLayout(context);
			childLayout.setLayoutParams(layoutParams);
			TextView textview =new TextView(context);
			textview.setTextSize(15);
			textview.setTextColor(Color.parseColor("#ba001d"));
			if(BTCCMWApplication.flowFileLangMap.get(viewId)!=null&&!BTCCMWApplication.flowFileLangMap.get(viewId).equals("")){
				String textviews = BTCCMWApplication.flowFileLangMap.get(viewId).toString();
//				温馨提示：\n  1、带*号的输入栏为必填项\n  2、本业务服务时间为每天的00:00-20:30
				if(BTCCMWApplication.flowFileLangMap.get(viewId).toString().contains("\\n")){
//					String textviews = "";
//					String test = BTCCMWApplication.flowFileLangMap.get(viewId).toString();
					textviews = textviews.replace("\\n", "\n");
//					textview.setText(textviews);
				}
				if(BTCCMWApplication.flowFileLangMap.get(viewId).toString().contains("&nbsp")){
					String str [] = textviews.split("&nbsp");
					textviews = textviews.replace("&nbsp", "");
				}//<br />
				if(BTCCMWApplication.flowFileLangMap.get(viewId).toString().contains("<br />")){
					textviews = textviews.replace("<br />", "");
				}
				if(BTCCMWApplication.flowFileLangMap.get(viewId).toString().contains("</b>")){
					textviews = textviews.replace("</b>", "");
				}
				if(BTCCMWApplication.flowFileLangMap.get(viewId).toString().contains("</b>")){
					textviews = textviews.replace("<b>", "");
				}
				if(BTCCMWApplication.flowFileLangMap.get(viewId).toString().contains("</br>")){
					textviews = textviews.replace("</br>", "");//</br>
				}
				if(BTCCMWApplication.flowFileLangMap.get(viewId).toString().contains("<font")){
					int starPosition = textviews.indexOf("<");
					int endposition = textviews.indexOf(">");
					int lastend  = textviews.indexOf("</");
					textviews =textviews.substring(endposition+1, lastend);
				}
				textview.setText(textviews);
				
			}else{
				textview.setText(viewId);		
			}
			childLayout.addView(textview,new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));
			((ViewGroup) view).addView(childLayout,layoutParams);

		super.drawLable(null, textview);
//		for (int i = 0; i < childElements.size(); i++) {
//			childElement = childElements.get(i);
//			btcDrawLable = childElement.getBTCDrawLable();
//			if (btcDrawLable != null) {
//
//				btcDrawLable.drawLable(null, view);
//			}
//
//		}
		return true;
	}

}
