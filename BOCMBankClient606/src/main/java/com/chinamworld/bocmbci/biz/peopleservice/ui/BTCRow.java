package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;

public class BTCRow extends BTCDrawLable{
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCRow.class.getSimpleName();
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCRow(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Row;
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
		String cellHeader = "";
		ArrayList<String> headerList = new ArrayList<String>();
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		LinearLayout childLayout = new LinearLayout(context);
		childLayout.setLayoutParams(layoutParams);
		//???
		childLayout.setOrientation(LinearLayout.VERTICAL);//wuhan行，应改为verical显示
		((ViewGroup) view).addView(childLayout, layoutParams);
		for (int i = 0; i < childElements.size(); i++) {
			childElement = childElements.get(i);
//			btcDrawLable = BTCLableFactory.createLableInstance(null,context,
//					childElement.getElementName());
			btcDrawLable = childElement.getBTCDrawLable();
			if (btcDrawLable != null) {
				Map<String, String> paramMap = childElement.getParams();
				List<BTCElement> child = childElement.getChildElements();
				String header = paramMap.get("type");
				if(header!=null && header.equals("header")){
					cellHeader = (String) btcDrawLable.drawLable(dbMap, childLayout);
					headerList.add(cellHeader);
				}else{
					dbMap.put("position", i+"");
					btcDrawLable.drawLable(dbMap, childLayout);//画cell
				}
			}

		}
		LinearLayout.LayoutParams layoutPara = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,40);
//		LinearLayout childLayout = new LinearLayout(context);
//		childLayout.setLayoutParams(layoutParams);
		TextView text = new TextView(context);
		text.setLayoutParams(layoutPara);
		text.setText("     ");
		((ViewGroup) view).addView(text);
		if(headerList.size()!=0){
			BTCCMWApplication.headerList = headerList;
		}
		return true;
	}

}
