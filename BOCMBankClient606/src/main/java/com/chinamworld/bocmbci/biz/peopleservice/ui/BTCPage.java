package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;

public class BTCPage extends BTCDrawLable{
	/** 上下文对象 */
//	private Context context;// 上下文对象
	private static final String TAG = BTCPage.class.getSimpleName();
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCPage(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Page;
	//	this.context = context;
	}
	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		String name=	btcElement.getElementName();
		BTCElement childElement;
		BTCDrawLable btcDrawLable;
		String pagetext = params.get("label");	
		TextView textview = new TextView(context);
		textview.setTextSize(15);
		textview.setTextColor(context.getResources().getColor(R.color.black));
		if(BTCCMWApplication.flowFileLangMap.get(pagetext)!=null&&!BTCCMWApplication.flowFileLangMap.get(pagetext).equals("")){
//			温馨提示：\n  1、带*号的输入栏为必填项\n  2、本业务服务时间为每天的00:00-20:30
//			String test = BTCCMWApplication.flowFileLangMap.get(pagetext).toString();
			String textviews = BTCCMWApplication.flowFileLangMap.get(pagetext).toString();
			if(BTCCMWApplication.flowFileLangMap.get(pagetext).toString().contains("\\n")){
				textviews = textviews.replace("\\n", "\n");
				textview.setText(textviews);
			}
			if(BTCCMWApplication.flowFileLangMap.get(pagetext).toString().contains("&nbsp")){
				String str [] = textviews.split("&nbsp");
				textviews = textviews.replace("&nbsp", "");
			}//<br />
			if(BTCCMWApplication.flowFileLangMap.get(pagetext).toString().contains("<br />")){
				textviews = textviews.replace("<br />", "");
			}
			if(BTCCMWApplication.flowFileLangMap.get(pagetext).toString().contains("</b>")){
				textviews = textviews.replace("</b>", "");
			}
			if(BTCCMWApplication.flowFileLangMap.get(pagetext).toString().contains("<b>")){
				textviews = textviews.replace("<b>", "");
			}
			if(BTCCMWApplication.flowFileLangMap.get(pagetext).toString().contains("</br>")){
				textviews = textviews.replace("</br>", "");//</br>
			}
			while(BTCCMWApplication.flowFileLangMap.get(pagetext).toString().contains("<font")){
				int starPosition = textviews.indexOf("<font");
				int endposition = textviews.indexOf(">",starPosition);
				if(endposition == -1){
					break;
				}
				textviews = textviews.substring(0,starPosition) + textviews.substring(endposition+1);
			}
			textviews = textviews.replace("</font>", "");
			textview.setText(textviews);
		}else{
			textview.setText(pagetext);		
		}
		((ViewGroup) view).addView(textview);
		return null;
	}

}
