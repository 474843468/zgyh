package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLableAttribute;


public class BTCFormatnumber extends BTCDrawLable {
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCFormatnumber.class.getSimpleName();
	private BTCCMWApplication cmwApplication;
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCFormatnumber(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Formatnumber;
	//	this.context = context;
		cmwApplication = BTCUiActivity.getApp();
	}

	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		LinearLayout linearLayout=null;
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		String name = btcElement.getElementName();
		BTCElement childElement;
		BTCDrawLable btcDrawLable;
		String value = null;
		String currency = null;
//		{value=TOTAL_AMT, currency=001}
	
		if (params != null) {
			Iterator it = params.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				if (BTCLableAttribute.VALUE.equals(entry.getKey())&& entry.getValue().toString().contains("{")) {
					value = (String) entry.getValue();
					value = value.substring(value.indexOf("{")+1, value.length()-1);
				}else if(BTCLableAttribute.VALUE.equals(entry.getKey())&& !entry.getValue().toString().contains("{")){
					value = (String) entry.getValue();
				}
				if (BTCLableAttribute.CURRENCY.equals(entry.getKey())) {
					currency = (String) entry.getValue();
				}
			}
		}
		String fillAmount ="";//{applyInfo.FREELIMIT}
		if(cmwApplication.getVar(value)!=null){//<param name="CUST_ID"><use>CUST_ID</use></param>
			fillAmount=cmwApplication.getVar(value);
		}else if(cmwApplication.responsemap.get(value)!=null){
			fillAmount= cmwApplication.responsemap.get(value).toString();
		}else if(BTCCMWApplication.hiddenboxmap.get(value)!=null){
			fillAmount= BTCCMWApplication.hiddenboxmap.get(value).toString();
		}else if(dbMap!=null && dbMap.get(value)!=null){
			fillAmount= dbMap.get(value).toString();
		}else if(BTCCMWApplication.linkParamsMap.containsKey(value)){
			fillAmount=BTCCMWApplication.linkParamsMap.get(value).toString();
		}
		
		if (value != null && currency != null&&fillAmount!=null) {
//			value="1234566";
			int scale=2;
			TextView textview=new TextView(context);
			textview.setTextSize(15);
			textview.setTextColor(context.getResources().getColor(R.color.black));
			LinearLayout.LayoutParams childParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			if(dbMap!=null && dbMap.containsKey("label")){
				LinearLayout.LayoutParams childParamss = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1);
				textview.setGravity(Gravity.LEFT);
				textview.setLayoutParams(childParamss);
			}else{
				LinearLayout.LayoutParams arams = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				textview.setLayoutParams(arams);
			}
			if(currency.equals("001")){
				scale=2;
			}
			String str=parseStringPattern(fillAmount, scale);
			textview.setText(str);//by dl 提取成变量
			/*by dl*/
			linearLayout = new LinearLayout(context);
			//浙江杭州 网络快捷贷 ，增加判断
			Map<String, String> parent_element_params = btcElement.getParentElement().getParams();
			String parent_label = parent_element_params.get("label");
			List<BTCElement> elements = btcElement.getParentElement().getChildElements();
			if (parent_label!=null&&elements.size()==5) {
				linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				linearLayout.setOrientation(LinearLayout.HORIZONTAL);
				linearLayout.setGravity(Gravity.CENTER_VERTICAL);
				linearLayout.setPadding(0, 0, 0, 0);
				linearLayout.addView(textview, childParams);
				((LinearLayout) view).setGravity(Gravity.CENTER_VERTICAL);
				((ViewGroup) view).addView(linearLayout);
			}
			else {
				((ViewGroup) view).addView(textview);
//				linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,1));
			}
			//浙江杭州 网络快捷贷  end
			
//			
		}
		return true;
	}

	public static String parseStringPattern(String text, int scale) {
		if (text == null || "".equals(text) || "null".equals(text)) {
			return "-";
		}
		if (text.contains(",") || text.contains("，")) {
			return text;
		}
		String temp = "###,###,###,###,###,###,###,##0";
		if (scale > 0)
			temp += ".";
		for (int i = 0; i < scale; i++)
			temp += "0";
		try{
			DecimalFormat format = new DecimalFormat(temp);
			BigDecimal d = new BigDecimal(text);
			return format.format(d).toString();
		}
		catch (Exception e){
			return text;
		}
		
	}
}
