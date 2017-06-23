package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLableAttribute;

public class BTCFormatdate extends BTCDrawLable{
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCFormatdate.class.getSimpleName();
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCFormatdate(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Formatdate;
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
		String value = null;
		String pattern = null;
		if (params != null) {
			Iterator it = params.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				if (BTCLableAttribute.VALUE.equals(entry.getKey())) {
					value = (String) entry.getValue();
					//p503生产修改问题  以后再打开
					if(value.contains("${")){//${shiftcase.shiftDate}
						value= value.substring(value.indexOf("{")+1, value.length()-1);
					}
				}
				if (BTCLableAttribute.PATTERN.equals(entry.getKey())) {
					pattern = (String) entry.getValue();
				}
			}
		}
		TextView textview=new TextView(context);
		textview.setTextSize(15);
		textview.setTextColor(context.getResources().getColor(R.color.black));
		if(dbMap!=null && dbMap.containsKey("label")){
			LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1);
			//params.get(BTCLableAttribute.NAME);//CUST_ID  getDepartments.hospitalName  
			textview.setEllipsize(TruncateAt.END);
			textview.setSingleLine();
			textview.setGravity(Gravity.LEFT);
			textview.setLayoutParams(childParams);
		}else{
			LinearLayout.LayoutParams arams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			textview.setLayoutParams(arams);
		}
		if (value != null && pattern != null) {
			if(pattern.equals("yyyy/MM/dd")){
				if(BTCCMWApplication.isDisp != null&&BTCCMWApplication.isDisp.equals("1") && BTCCMWApplication.dispMap!=null &&BTCCMWApplication.dispMap.containsKey(value)){
					 textview.setText(BTCCMWApplication.dispMap.get(value).toString());
				 }
				else 
				if(BTCCMWApplication.responsemap.containsKey(value)){//yyyy/MM/dd
					textview.setText(DateFormatter(BTCCMWApplication.responsemap.get(value).toString()));
				}
				else if(dbMap!=null &&dbMap.containsKey(value)){
					textview.setText(DateFormatter(dbMap.get(value).toString()));
				}else if(BTCCMWApplication.hiddenboxmap.containsKey(value)){
					textview.setText(DateFormatter(BTCCMWApplication.hiddenboxmap.get(value).toString()));
				}else if(BTCCMWApplication.linkParamsMap.containsKey(value)){
					textview.setText(DateFormatter(BTCCMWApplication.linkParamsMap.get(value).toString()));
				}
			}
			else if(pattern.equals("yyyy/MM/dd HH:mm:ss")){
				if(BTCCMWApplication.isDisp != null&&BTCCMWApplication.isDisp.equals("1") && BTCCMWApplication.dispMap!=null &&BTCCMWApplication.dispMap.containsKey(value)){
					 textview.setText(BTCCMWApplication.dispMap.get(value).toString());
				 }
				else if(BTCCMWApplication.responsemap.containsKey(value)){
					textview.setText(signDateFormatter(BTCCMWApplication.responsemap.get(value).toString()));
				}else if(dbMap!=null &&dbMap.containsKey(value)){
					textview.setText(DateFormatter(dbMap.get(value).toString()));
				}else if(BTCCMWApplication.hiddenboxmap.containsKey(value)){
					textview.setText(DateFormatter(BTCCMWApplication.hiddenboxmap.get(value).toString()));
				}else if(BTCCMWApplication.linkParamsMap.containsKey(value)){
					textview.setText(DateFormatter(BTCCMWApplication.linkParamsMap.get(value).toString()));
				}
				
			}
			
//			((ViewGroup) view).addView(textview,childParams);
			((ViewGroup) view).addView(textview);
		}
		return true;
	}
	
	/**
	 * 时间格式化
	 * 
	 * @param date
	 *            需要格式化时间 需要格式化时间样式为20151120110206,格式结果为2015/11/20 11:02:06
	 *            如不符合格式化要求返回原结果
	 */
	public static String signDateFormatter(String date) {
		if (TextUtils.isEmpty(date)) {
			return date;
		}
		if (date.length() != 14) {
			return date;
		}
		// 年
		String year = date.substring(0, 4);
		// 月
		String month = date.substring(4, 6);
		// 日
		String day = date.substring(6, 8);
		
		// 时
		String hour = date.substring(8, 10);
		// 分
		String minute = date.substring(10, 12);
		// 秒
		String second = date.substring(12, 14);
		return year + "/" + month + "/" + day + " " + hour + ":" + minute + ":"
				+ second;
	}
	public static String DateFormatter(String date) {
		if (TextUtils.isEmpty(date)) {
			return date;
		}
		if (date.length() != 8) {
			return date;
		}
		// 年
		String year = date.substring(0, 4);
		// 月
		String month = date.substring(4, 6);
		// 日
		String day = date.substring(6, 8);

		return year + "/" + month + "/" + day ;
	}
}

