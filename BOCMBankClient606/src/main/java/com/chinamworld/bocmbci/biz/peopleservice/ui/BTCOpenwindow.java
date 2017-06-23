package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;

public class BTCOpenwindow extends BTCDrawLable{
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCOpenwindow.class.getSimpleName();
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCOpenwindow(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Openwindow;
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
		String url ="";
		String label= "";
		url = params.get("url");
		label = params.get("label");
		Map<String, String> map= dbMap;
		TextView textview = new TextView(context);//要 改///直接连接到流览器
		label = label.substring(label.indexOf("{")+1, label.length()-1);
		if(dbMap!=null && dbMap.containsKey("label")){
			LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1);
			textview.setEllipsize(TruncateAt.END);
			textview.setSingleLine();
			textview.setGravity(Gravity.LEFT);
			textview.setLayoutParams(childParams);
		}else{

			LinearLayout.LayoutParams  arams = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			textview.setLayoutParams(arams);
		}
		
		textview.setTextColor(Color.BLUE);//Color.parseColor("#ba001d")#0000FF
		 if(BTCCMWApplication.flowFileLangMap.containsKey(label)&&BTCCMWApplication.flowFileLangMap.get(label)!=null ){
			 String content =BTCCMWApplication.flowFileLangMap.get(label).toString();
			 textview.setText(content);
		}else if(map.containsKey(label)&&map.get(label)!=null){
			 String content =map.get(label).toString();
			 textview.setText(content);
		}
		Map<String, Object> msg = new HashMap<String, Object>();
		msg.put("url", url);
		msg.put("map", map);
		textview.setTag(msg);
		textview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Map<String, Object> msg = (Map<String, Object>) arg0.getTag();
				String urls = (String) msg.get("url");
				Map<String, String> detailMap = (Map<String, String>) msg.get("map");
				if(detailMap.containsKey(urls)){
					urls = (String) detailMap.get(urls);
				}
				Intent intent = new Intent(context, BTCWebView.class);
				intent.putExtra("infourl", urls);
				context.startActivity(intent);
			}
		});
		
		((ViewGroup) view).addView(textview);
		return null;
	}

}
