package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLable;
import com.chinamworld.bocmbci.log.LogGloble;

/**
 * 类功能描述：用于画item标签
 * 
 * @author：zld
 * @version： 1.0
 * @see 包名： 父类：：
 */

public class BTCItem extends BTCDrawLable {
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCItem.class.getSimpleName();


	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCItem(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Item;
	//	this.context = context;
	}

	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		BTCCMWApplication.isContainSecurityInit = false;
		BTCCMWApplication.isContainSecurityActive = false;
//		BTCCMWApplication.acctype = null;
		BTCCMWApplication.securityRandom = "";
		BTCCMWApplication.factorList = null;
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		String name = btcElement.getElementName();
		BTCElement childElement;
		BTCDrawLable btcDrawLable;
		String demo ="\"d\"";
		String buttontext = childElements.get(0).getText();
		String key = btcElement.getParams().get(BTCLable.FLOW);
		
//		LinearLayout.LayoutParams partext = new LinearLayout.
//				LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT);
//		TextView textview = new TextView(context);
//		textview.setBackgroundResource(R.drawable.right_arrow);
//		textview.setLayoutParams(partext);
//		LinearLayout.LayoutParams paramtext = new LinearLayout.
//				LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//		LinearLayout childLayout = new LinearLayout(context);
//		childLayout.setLayoutParams(paramtext);
//		
//		LinearLayout.LayoutParams parbtn = new LinearLayout.
//				LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT);
//		parbtn.weight = 1;
		
		
		Button button = new Button(context);
		//yaogai
//		button.setText(buttontext);
//		以后有字典了再用
//		button.setText(BTCCMWApplication.flowFileLangMap.get(buttontext).toString());
		if(BTCCMWApplication.flowFileLangMap.get(buttontext)!=null&&!BTCCMWApplication.flowFileLangMap.get(buttontext).equals("")){
			button.setText(BTCCMWApplication.flowFileLangMap.get(buttontext).toString());	
		}else{
			button.setText(buttontext);		
		}
		button.setTag(key);
		button.setOnClickListener(onClickListener);
		
//		button.setBackgroundResource(R.drawable.selector_for_click_item);
//		button.setText(context.getResources().getResourceName(R.dimen.textsize_default));
//		button.setGravity(Gravity.LEFT);
//		button.setLayoutParams(parbtn);
//		childLayout.addView(button);
//		childLayout.addView(textview);
//		((ViewGroup) view).addView(childLayout);
		
		((ViewGroup) view).addView(button);

		return true;
	}

	/**
	 * 监听事件,用于点击按钮向服务器发起通讯时使用
	 */
	public OnClickListener onClickListener = new View.OnClickListener() {
		BTCElement childElement;
		BTCDrawLable btcDrawLable;
		List<BTCElement> widgetList = new ArrayList<BTCElement>(); // 存放标签信息

		public void onClick(View v) {
//			widgetList.clear();
//				childElement = BTCCMWApplication.getFolwmap().get(v.getTag());
//				childElement = childElement.getChildElements().get(0);
//				widgetList.add(childElement);
				LogGloble.i("info", TAG);		
				
				BTCElement element = BTCItem.this.FindElementBy(v.getTag().toString());
				BTCUiActivity.Instance().navigationToActivity(element);
		
		}
	};
}
