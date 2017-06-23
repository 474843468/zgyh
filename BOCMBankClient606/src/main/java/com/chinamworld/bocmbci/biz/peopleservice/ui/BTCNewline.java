package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;

import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;

public class BTCNewline extends BTCDrawLable{
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCNewline.class.getSimpleName();
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCNewline(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Newline;
		//this.context = context;
	}
	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		String count="";
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		String name=	btcElement.getElementName();
		BTCElement childElement;
		BTCDrawLable btcDrawLable;
//		if (params.containsKey("count")){
//			count=params.get("count");
//		}else{
//			count="1";
//		}
		
//		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//				ViewGroup.LayoutParams.FILL_PARENT,
//				ViewGroup.LayoutParams.WRAP_CONTENT);
//		layoutParams.setMargins(0, 50*(Integer.parseInt(count)), 0, 0);
//		LinearLayout childLayout = new LinearLayout(context);
//		childLayout.setLayoutParams(layoutParams);
//		((ViewGroup) view).addView(childLayout);
		return null;
	}

}
