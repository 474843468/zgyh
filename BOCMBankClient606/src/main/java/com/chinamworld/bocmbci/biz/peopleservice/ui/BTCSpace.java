package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;

public class BTCSpace extends BTCDrawLable{
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCSpace.class.getSimpleName();
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCSpace(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Space;
	//	this.context = context;
	}
	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		String count="";
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		if(params.containsKey("count")){
			count=params.get("count");
		}else{
			count="1"; 
		}
		String space  ="";
//		for(int i = 0;i<Integer.parseInt(count);i++){
//			space= space+" ";
//		} 
		TextView textview = new TextView(context);
		textview.setText(space);
//		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//				ViewGroup.LayoutParams.WRAP_CONTENT,
//				ViewGroup.LayoutParams.WRAP_CONTENT);
//		layoutParams.setMargins(50*(Integer.parseInt(count)), 0, 0,0);
//		LinearLayout childLayout = new LinearLayout(context);
//		childLayout.setLayoutParams(layoutParams);
		((ViewGroup) view).addView(textview);
		return true;
	}

}
