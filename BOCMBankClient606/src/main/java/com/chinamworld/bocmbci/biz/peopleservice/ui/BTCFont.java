package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;

public class BTCFont extends BTCDrawLable{
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCFont.class.getSimpleName();
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCFont(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Font;
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
		String color="";
		if(params.containsKey("color")){
			color = params.get("color");
		}
		String text = "";
		if(childElements.size()!=0){
			text= childElements.get(0).getText();
		}
		
		TextView textview = new TextView(context);
		textview.setTextSize(15);
		textview.setTextColor(context.getResources().getColor(R.color.black));
		textview.setText(text);
		if(("red").equals(color)){
			textview.setTextColor(Color.parseColor("#ba001d"));
		}
		for (int i = 0; i < childElements.size(); i++) {
			childElement = childElements.get(i);
//			btcDrawLable = BTCLableFactory.createLableInstance(null,context,
//					childElement.getElementName());
			btcDrawLable = childElement.getBTCDrawLable();
			if (childElement.getChildElements() != null) {
				
				if (params.size()>0 ) {
					params.put("color", color);
					btcDrawLable.drawLable(params, view);
				}else{
					btcDrawLable.drawLable( null, view);
				}
			
			}
		}
		((ViewGroup) view).addView(textview);
		return true;
	}

}
