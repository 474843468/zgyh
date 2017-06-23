package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class BTCActuse extends BTCDrawLable{
	/** 上下文对象 */
//	private Context context;// 上下文对象
	private static final String TAG = BTCActuse.class.getSimpleName();
	/**界面栈对象*/
	private BTCActivityManager activityManager;// 界面栈对象
	/**全局变量对象*/
	private BTCCMWApplication cmwApplication;
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCActuse(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Actuse;
		activityManager = BTCActivityManager.getInstance();
		cmwApplication = BTCUiActivity.getApp();
	}
	@Override
	public Object drawLable( Map<String, String> dbMap,
			View view) {
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		String name=	btcElement.getElementName();
		String key = childElements.get(0).getText();
		final TextView  textview= new TextView(context);	
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
//		LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(
//				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		String text=	cmwApplication.getVar(key);
		textview.setText(getText(text));
		textview.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				PopupWindowUtils.getInstance().setOnShowAllTextListener(BTCUiActivity.getInstance(), (TextView) textview);
			}
		});
		((ViewGroup) view).addView(textview);
		return null;
	}
	
	private String getText(String text){
		List<HashMap<String, String>> reee = BTCCMWApplication.ListAccountMap;
		for(int i = 0 ;i<reee.size();i++){
			Map<String, String> map = new HashMap<String, String>();
			map = reee.get(i);
			if(map.containsValue(text)){
				String accountType = map.get("accountType");
				String strAccountType = LocalData.AccountType.get(accountType);//代号转成卡类型
				String accountNumber = map.get("accountNumber");
				String nickName = map.get("nickName");
				
				String forSixForString = StringUtil.getForSixForString(accountNumber);//4 6 4(4******4)卡号格式化
				
				return strAccountType+forSixForString+nickName;
			}else{
				return text;
			}
		}
		return text;
	}
}
