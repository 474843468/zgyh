package com.chinamworld.bocmbci.biz.peopleservice.ui;

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
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLable;

public class BTCTranslate extends BTCDrawLable{
	/** 上下文对象 */
//	private Context context;// 上下文对象
	private static final String TAG = BTCTranslate.class.getSimpleName();
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCTranslate(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Translate;
		//this.context = context;
	}
	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> elements = btcElement.getParentElement().getChildElements();
		List<BTCElement> childElements = btcElement.getChildElements();
		String name=	btcElement.getElementName();
		BTCElement childElement = btcElement;
		BTCDrawLable btcDrawLable;
		String text = childElements.get(0).getText();//birthday_tip
		String key = params.get("namespace");//input.check
	
		TextView textview = new TextView(context);
		textview.setTextSize(15);
		textview.setTextColor(context.getResources().getColor(R.color.black));
		String keys ="";
		if(key !=null){
			if(text != null){
				keys =key+"."+text;
			}else if( childElements.size()==1){
				childElement = childElements.get(0);
				btcDrawLable = childElement.getBTCDrawLable();
				String textuse = "";
				if(btcDrawLable!=null){
					textuse = (String) btcDrawLable.drawLable(dbMap, null);
				}
				keys = key +"."+ textuse;
			}
		}else if(text !=null){
			keys =text;
		}else if(childElements.size()!=0){
			childElement = childElements.get(0);
			btcDrawLable = childElement.getBTCDrawLable();
			String textuse = "";
			if(btcDrawLable!=null){
				textuse = (String) btcDrawLable.drawLable(dbMap, null);
			}
			keys =textuse;
		}
		
//		if(key !=null){//当namespace不为空是，取translate里面的text值，要么取子标签里在的值进行拼接。两者都不满足的情况下再时行解析下面的标签。
//			if(key !=null && text != null){
//				keys =key+"."+text;
//			}else if(key !=null &&childElements.size()==1){
//				childElement = childElements.get(0);
//				btcDrawLable = childElement.getBTCDrawLable();
//				String textuse = "";
//				if(btcDrawLable!=null){
//					textuse = (String) btcDrawLable.drawLable(dbMap, null);
//				}
//				keys = key +"."+ textuse;
//			}else if(key != null && text == null){
//				keys = key;
//			}else if(key !=null && childElements.size()==0){
//				keys = key;
//			}else if(key == null && text !=null){
//				keys = text;
//			}else if(key == null && childElements.size()!=0){
//				childElement = childElements.get(0);
//				btcDrawLable = childElement.getBTCDrawLable();
//				String textuse = "";
//				if(btcDrawLable!=null){
//					textuse = (String) btcDrawLable.drawLable(dbMap, null);
//				}
//				keys = textuse;
//			}
//		}
		
//		if(key!=null && text !=null){
//			keys =key+"."+text;
//		}else if(key!=null){
//			 keys =key;
//		}else {//1
//			 keys =text;
//		}
	
//		if(dbMap!=null && dbMap.containsKey("cell")){//table 里面有cell。北京海空总医院
//			textview.setText("");
//		}else{
			if(keys!=null&&!"".equals(keys)){
				if(BTCUiActivity.getInstance().getApp().getFlowFileLangMapByKey(keys)!=null&&!BTCUiActivity.getInstance().getApp().getFlowFileLangMapByKey(keys).equals("")){
					String str=BTCUiActivity.getInstance().getApp().getFlowFileLangMapByKey(keys).toString().trim();
					/*by dl 增加判断  ，浙江杭州福彩定投	*/
//					String namespace = params.get("namespace");
//					if (namespace!=null&&namespace.equals("fcdt_is_con")) {
//						textview.setText("");
//					}else {
						textview.setText(str);	
//					}
				}else{
					textview.setText("");		
				}
			}
		
//		}
			
		
		if(BTCUiActivity.getInstance().getApp().getFlowFileLangMapByKey(keys)!=null&&!BTCUiActivity.getInstance().getApp().getFlowFileLangMapByKey(keys).equals("")){
			LinearLayout.LayoutParams childParams =null;
			if(dbMap!=null && dbMap.containsKey("label")&&(btcElement.getChildElements().size()==0||btcElement.getChildElements().size()==1)
					&&!textview.getText().toString().equals("")//by dl +浙江杭州福彩
					&&!elements.toString().contains(BTCLable.COMBO)//by dl+ 杭州网络快捷贷
					){
				childParams = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1);
				textview.setEllipsize(TruncateAt.END);
				textview.setSingleLine();
				textview.setGravity(Gravity.LEFT);
			}else if (!elements.toString().contains(BTCLable.COMBO)&&(elements.size()==2||elements.size()==3||elements.size()==5)) {//by dl + 北京301医院 非医保挂号，缴费成功,size==5为北京移动缴费成功页
				childParams = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1);
			}
			else{
				childParams = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			}
			textview.setLayoutParams(childParams);
		}
		
		
		if(dbMap!=null && dbMap.containsKey("color")){
			textview.setTextColor(context.getResources().getColor(R.color.red));
		}
		
		
//		LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(
//				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//		textview.setLayoutParams(childParams);
		((ViewGroup) view).addView(textview);//by dl
//		for (int i = 0; i < childElements.size(); i++) {
//			childElement = childElements.get(i);
//			btcDrawLable = childElement.getBTCDrawLable();
////			if(key !=null&&text==null){//1
////				dbMap.put("namespace", key);
////			}
//			if(btcDrawLable!=null){
//				btcDrawLable.drawLable(dbMap, view);
//			}
//		}
//		((ViewGroup) view).addView(textview);
		return null;
	}

}
