package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.HashMap;
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
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLable;
import com.chinamworld.bocmbci.log.LogGloble;

public class BTCPageitem extends BTCDrawLable {
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCPageitem.class.getSimpleName();
	String lable = null;
	private int dateinput_sum;
	/**
	 * 构造函数
	 * 
	 * @param conteaxt
	 */
	public BTCPageitem(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Pageitem;
	//	this.context = context;
	}

	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		Map<String, String> dbmap = new HashMap<String, String>();
		Map<String, String> lablemap = new HashMap<String, String>();
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		String name = btcElement.getElementName();
		BTCElement childElement;
		BTCDrawLable btcDrawLable;
		lablemap.clear();
		if (params != null && params.containsKey("label")) {
			lable = params.get("label");
		}else{
			lable = "null";//pageitem中没有lable标签 
		}

		
		
//		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//				ViewGroup.LayoutParams.FILL_PARENT,
//				ViewGroup.LayoutParams.WRAP_CONTENT);
//		LinearLayout childLayout = new LinearLayout(context);
//		childLayout.setLayoutParams(layoutParams);
//		if (BTCLable.CHECKBOX.equals(childElements.get(0).getElementName())) {
//			childLayout.setOrientation(LinearLayout.VERTICAL);
//		}
//		else {
//			childLayout.setOrientation(LinearLayout.HORIZONTAL);
//		}

		LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		LinearLayout Layout = new LinearLayout(context);
		Layout.setLayoutParams(Params);
		LinearLayout Layout1 = new LinearLayout(context);
		LinearLayout Layout2 = new LinearLayout(context);
		LinearLayout Layout3 = new LinearLayout(context);//by dl +北京移动缴费 金额未对齐 ，使用时根据情况设定参数
		LinearLayout Layout4 = null,Layout5=null;//by dl + 北京移动缴费成功页
		Layout.setGravity(Gravity.CENTER_VERTICAL);
		Layout.setOrientation(LinearLayout.VERTICAL);
		Layout2.setOrientation(LinearLayout.HORIZONTAL);//front
		Layout2.setLayoutParams(Params);
		int layout_i=0;//by dl+ 云南公积金 dateinput换行显示
		if(childElements.size()>0){
			if (BTCLable.CHECKBOX.equals(childElements.get(0).getElementName())) {
				Layout1.setOrientation(LinearLayout.VERTICAL);
			}
			else if(params.containsKey("label")){
				lablemap.put("label", lable);
				Layout1.setOrientation(LinearLayout.HORIZONTAL);
				LinearLayout.LayoutParams layout1Params = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT,1);
				Layout1.setLayoutParams(layout1Params);
			}
			else {
				Layout1.setOrientation(LinearLayout.HORIZONTAL);
//				LinearLayout.LayoutParams layout1Params = new LinearLayout.LayoutParams(
//						ViewGroup.LayoutParams.FILL_PARENT,
//						ViewGroup.LayoutParams.WRAP_CONTENT,1);
				Layout1.setLayoutParams(Params);
			}
		}
		//p503生产修改问题
//		else{
//			lable = "";
//		}
		
		LogGloble.i("info", "pageitem lable ==" + lable);
		TextView textview = new TextView(context);
		textview.setTextSize(15);
		textview.setTextColor(context.getResources().getColor(R.color.black));
		 if(BTCCMWApplication.flowFileLangMap.get(lable)!=null&&!BTCCMWApplication.flowFileLangMap.get(lable).equals("")){
			 	String names = BTCCMWApplication.flowFileLangMap.get(lable).toString();
				if(names.contains(":")){
					names = names.replace(":", "：");
				}
				textview.setText( names);
				//by dl +北京301医院 非医保挂号,北京移动缴费成功后一个页面
				textview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,1));
		 }else{
			 textview.setText("");
			 //by dl +北京301医院 非医保挂号,北京移动缴费成功后一个页面
			 textview.setLayoutParams(new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT));
		 }

		/*by dl*/
		 dateinput_sum=0;
		 for (int i = 0; i < childElements.size(); i++) {
			 if (childElements.get(i).getElementName().equals(BTCLable.DATEINPUT)) {
				 dateinput_sum+=1;
			}
		}
		 /*by dl  end*/
		 Layout1.setGravity(Gravity.CENTER_VERTICAL);//by dl +
		 Layout1.addView(textview);
		((ViewGroup) Layout).addView(Layout1);
		((ViewGroup) Layout1).addView(Layout3);//by dl +
		((ViewGroup) Layout).addView(Layout2);
		((ViewGroup) view).addView(Layout, Params);
		
		
		for (int i = 0; i < childElements.size(); i++) {
			childElement = childElements.get(i);
			if (childElement.toString().contains("checkrule")&& childElement.toString().contains("required:true")) {
				if (BTCCMWApplication.flowFileLangMap.get(lable) != null&& !BTCCMWApplication.flowFileLangMap.get(lable).equals("")) {
					String names = BTCCMWApplication.flowFileLangMap.get(lable).toString();
					if(names.contains(":")){
						names = names.replace(":", "：");
					}
					textview.setText("*"+ names);
//					textview.setText("*"+ BTCCMWApplication.flowFileLangMap.get(lable).toString());
				} else {
					textview.setText("");
					/*by dl*/
					if (childElement.getElementName().equals(BTCLable.DATEINPUT)) {
					textview.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT));//by dl +北京301医院
					}/*by dl  end*/
				}
			} else {
				if (BTCCMWApplication.flowFileLangMap.get(lable) != null&& !BTCCMWApplication.flowFileLangMap.get(lable).equals("")) {
					String names = BTCCMWApplication.flowFileLangMap.get(lable).toString();
					if(names.contains(":")){
						names = names.replace(":", "：");
					}
					textview.setText(names);
					/*by dl + 浙江杭州 网络快捷贷*/
					String params_label = params.get("label");
					if (params_label!=null&&childElements.toString().contains(BTCLable.BUTTON)&&childElements.size()==5) {
						textview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
					}
//					textview.setText(BTCCMWApplication.flowFileLangMap.get(lable).toString());
				} else {
					textview.setText("");
					/*by dl*/
					if (childElement.getElementName().equals(BTCLable.DATEINPUT)) {
					textview.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT));//by dl + 北京301医院
					}/*by dl  end*/
				}
			}
//			btcDrawLable = BTCLableFactory.createLableInstance(null, context,
//					childElement.getElementName());
			btcDrawLable = childElement.getBTCDrawLable();
			if (btcDrawLable != null) {
				if(childElement.getElementName().equals(BTCLable.COMBO)){
					// wuhan
//					dbmap = (Map<String, String>) btcDrawLable.drawLable(dbmap, Layout1);
					Layout3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
					dbmap = (Map<String, String>) btcDrawLable.drawLable(dbmap, Layout3);//by dl 
					if(lable.equals("null")){//燃气中中pageitem中没有lable,所以要用combo，里面的lable
						if(dbmap.containsKey("lable")){
							lable = dbmap.get("lable");
							if (BTCCMWApplication.flowFileLangMap.get(lable) != null&& !BTCCMWApplication.flowFileLangMap.get(lable).equals("")) {
								String namesLable = BTCCMWApplication.flowFileLangMap.get(lable).toString();
								if(namesLable.contains(":")){
									namesLable = namesLable.replace(":", "：");
								}
								textview.setText(namesLable);
								/*by dl + 301医院 非医保挂号*/
								if (!namesLable.equals("")&&namesLable!=null) {
									textview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));
								}
							} else {
								textview.setText(lable);
								/*by dl + 301医院 非医保挂号*/
								if (!lable.equals("")&&lable!=null) {
									textview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));
								}
							}
						}
					}
				}else if(childElement.getElementName().equals(BTCLable.FONT)){
					// 如果里面有输入框，时间选择器，等，则认为需要换行
					if(this.btcElement.toString().contains("textbox") 
							|| childElement.toString().contains("actcombo")
							 || childElement.toString().contains("combo")
							 || childElement.toString().contains("radio")
							 || childElement.toString().contains("dateinput")
							 || childElement.toString().contains("dynamictextbox")
							 ){
						btcDrawLable.drawLable(null, Layout2);
					}
					else
						btcDrawLable.drawLable(null, Layout3);
				}else if (childElements.size()==3&&childElements.get(0).getElementName().equals(BTCLable.FROMATNUMBER)) {//by dl + 移动通讯  对齐
					Layout3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
					btcDrawLable.drawLable(lablemap, Layout3); 
				}else if (params.containsKey("label")&&childElements.size()>1&&childElements.toString().contains( BTCLable.TRANSLATE)) {//by dl + 河南许昌 诊疗账户充值、诊疗账户退款
					Layout3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
					btcDrawLable.drawLable(lablemap, Layout3); 
				}else if (i>0&&childElements.toString().contains(BTCLable.FROMATNUMBER)&&childElements.size()==5&&childElements.toString().contains(BTCLable.TRANSLATE)) {//by dl +北京移动缴费成功页
					if (childElement.getElementName().equals(BTCLable.FROMATNUMBER)) {
						Layout4 = new LinearLayout(context);
						Layout4.setOrientation(LinearLayout.HORIZONTAL);
						Layout1.addView(Layout4,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
					}
					if (Layout4!=null) {
						btcDrawLable.drawLable(lablemap, Layout4);
					}else {
						btcDrawLable.drawLable(lablemap, Layout1);
					}
				}
				else if (dateinput_sum==2&&childElement.getElementName().equals(BTCLable.DATEINPUT)) {//by dl +云南公积金  dateinput换行显示
					layout_i++;
					switch (layout_i) {
					case 1:
						Layout2.setGravity(Gravity.CENTER_VERTICAL);
						btcDrawLable.drawLable(lablemap, Layout2);
						break;
					case 2:
						Layout5=new LinearLayout(context);
						Layout5.setOrientation(LinearLayout.HORIZONTAL);
						Layout5.setGravity(Gravity.CENTER_VERTICAL);
						Layout.addView(Layout5,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
						btcDrawLable.drawLable(lablemap, Layout5);
						break;
 
					default:
						break;
					}
				}
				else{
					btcDrawLable.drawLable(lablemap, Layout1); 
				}
				
			}
		}

//		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//		for(int i =0 ;i < Layout3.getChildCount();i++){
//			if(Layout3.getChildAt(i) instanceof TextView)
//			Layout3.getChildAt(i).setLayoutParams(lp);
//		}
		return null;
	}
	
}

