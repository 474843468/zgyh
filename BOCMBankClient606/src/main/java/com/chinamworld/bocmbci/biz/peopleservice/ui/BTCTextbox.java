package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLableAttribute;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IInputCheck;

public class BTCTextbox extends BTCDrawLable implements IInputCheck {
	/** 上下文对象 */
//	private Context context;// 上下文对象
	private static final String TAG = BTCTextbox.class.getSimpleName();
	/** 界面栈对象 */
	private BTCActivityManager activityManager;// 界面栈对象
	/** 全局变量对象 */
	private BTCCMWApplication cmwApplication;

	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCTextbox(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Textbox;
		//this.context = context;
		activityManager = BTCActivityManager.getInstance();
		cmwApplication = BTCUiActivity.getInstance().getApp();
	}

	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		
		/*by dl*/
//		LinearLayout linearLayout=null;
		BTCElement parentElement = btcElement.getParentElement();
		List<BTCElement> elements = parentElement.getChildElements();
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		if(params.containsKey(BTCLableAttribute.LABEL)){
			String label=params.get(BTCLableAttribute.LABEL);
			TextView text = new TextView(context);
			text.setTextSize(15);
			text.setTextColor(context.getResources().getColor(R.color.black));
			text.setText(label);
			LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
//					ViewGroup.LayoutParams.WRAP_CONTENT,1);//by dl
			((ViewGroup) view).addView(text,childParams);	
		}
		
		final EditText edittext = new EditText(context);
		edittext.setTextSize(15);
		edittext.setTextColor(context.getResources().getColor(R.color.black));
		edittext.setSingleLine(true);

		LinearLayout.LayoutParams childParams=null;
			childParams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT, 1);

		
		// 为输入框设置id
		String name = params.get(BTCLableAttribute.NAME);//CUST_ID
		int hashcode = name.hashCode();
		hashcode = hashcode > 0 ? hashcode : -hashcode;
		edittext.setId(hashcode);
		Map<String, Object> msg = new HashMap<String, Object>();
		if(params.containsKey(BTCLableAttribute.TYPE)){
			String type=params.get(BTCLableAttribute.TYPE);
			if(type.equals("password")){
				edittext.setInputType(129);
			}
		}
		
		if(params.containsKey(BTCLableAttribute.BINDTARGET)){
			String tag=params.get(BTCLableAttribute.BINDTARGET);
			msg.put(BTCLableAttribute.BINDTARGET, tag);
//			edittext.setTag(tag);
			
			
		}
		
		//BTCPagitem 中label标签，用于当pogmt为null时，做为提示语显示
		if(dbMap!=null && dbMap.containsKey("label")){
			msg.put("label", dbMap.get("label"));
		}
		
		if (params.containsKey(BTCLableAttribute.USEPROMPT)) {
			String useprompt = params.get(BTCLableAttribute.USEPROMPT);
			msg.put(BTCLableAttribute.USEPROMPT, useprompt);
		}
		
		if (params.containsKey(BTCLableAttribute.PROMPT)) {
			String prompt = params.get(BTCLableAttribute.PROMPT);
			msg.put(BTCLableAttribute.PROMPT, prompt);
		}
		
		if (params.containsKey(BTCLableAttribute.CHECKRULE)) {

			Object checkrule = params.get(BTCLableAttribute.CHECKRULE);
			activityManager.setWidgetcheckrule(edittext.getId(), checkrule);
		}
		edittext.setTag(msg);
//		String str=cmwApplication.getVar(String.valueOf(hashcode));//by dl
//		edittext.setText(str);//by dl str提取成变量
		
		activityManager.putWidgetId(name, hashcode);
		if(dbMap!=null){
			dbMap.put(name, String.valueOf(hashcode));
		}else{
			// 把当前输入框的id号放到全局变量中
			activityManager.putWidgetId(name, hashcode);
		}
		 if(BTCCMWApplication.isDisp != null&&BTCCMWApplication.isDisp.equals("1") && BTCCMWApplication.dispMap!=null &&BTCCMWApplication.dispMap.containsKey(name)){
			 edittext.setText(BTCCMWApplication.dispMap.get(name).toString());
		 }
		 if(BTCCMWApplication.isDisp != null&&BTCCMWApplication.isDisp.equals("1") &&BTCCMWApplication.dispMap!=null && BTCCMWApplication.dispMap.containsKey(name)){
			 edittext.setText(BTCCMWApplication.dispMap.get(name).toString());
		 }
		/*by dl*/
//		linearLayout = new LinearLayout(context);
//		linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,1));
//		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//		linearLayout.setGravity(Gravity.CENTER_VERTICAL);
//		linearLayout.addView(edittext, childParams);
//		((LinearLayout) view).setGravity(Gravity.CENTER_VERTICAL);
//		((ViewGroup) view).addView(linearLayout);
		((ViewGroup) view).addView(edittext, childParams);
		
		BTCElement childElement;
		BTCDrawLable btcDrawLable;
	//	super.drawLable(null, view);// TextBox 不在画子标签。因为TextBox 有些包含Use标签

		return view;
//		return (View)linearLayout;
	}

	private Map<String, String> String2map(String map) {
		Map<String, String> params = new HashMap<String, String>();
		map.replace("{", null);
		map.replace("}", null);
		String[] array = map.split(",");
		for (int i = 0; i < array.length; i++) {
			String[] data = array[i].split(":");
			params.put(data[0], data[1]);
		}
		return params;

	}

	@Override
	public boolean Check(Object param) {
		// TODO Auto-generated method stub
		return false;
	}
}
