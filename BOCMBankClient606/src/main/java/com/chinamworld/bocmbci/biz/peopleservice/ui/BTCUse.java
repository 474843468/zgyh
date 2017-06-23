package com.chinamworld.bocmbci.biz.peopleservice.ui;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.login.LoginActivity;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BTCUse extends BTCDrawLable{
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCUse.class.getSimpleName();
	/**界面栈对象*/
	private BTCActivityManager activityManager;// 界面栈对象
	/**全局变量对象*/
	private BTCCMWApplication cmwApplication;
	List<String> optList = new ArrayList<String>();
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCUse(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Use;
	//	this.context = context;
		activityManager = BTCActivityManager.getInstance();
		cmwApplication = BTCUiActivity.getApp();
	}
	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		
		boolean isDataList = false;
		if(view !=null && "IS_DATA_LIST".equals(view.getTag())){
			view = null;
			isDataList = true;  // 表示是DataList 标签取上送数据。如果use未取到，则返回原字符串
		}
		
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		String element_text = btcElement.getText();
		/*by dl + lines 6  浙江杭州福彩定投*/
		String parent_elementName = btcElement.getParentElement().getElementName();
		Map<String, String> parent_params = btcElement.getParentElement().getParams();
		List<BTCElement> elements = btcElement.getParentElement().getChildElements();
		String parent_element_label = null;
		if (parent_params!=null&&parent_params.size()>0) {
			parent_element_label = parent_params.get("label");
		}
	
		String key = childElements.get(0).getText();
		//"getDepartments.hospitalName"=key
		final TextView  textview= new TextView(context);
		textview.setTextSize(15);
		textview.setTextColor(context.getResources().getColor(R.color.black));
//		textview.setSingleLine();
		if(dbMap!=null && dbMap.containsKey("label")){
			LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1);
			//params.get(BTCLableAttribute.NAME);//CUST_ID  getDepartments.hospitalName  
			textview.setEllipsize(TruncateAt.END);
			textview.setSingleLine();
			textview.setGravity(Gravity.LEFT);
			textview.setLayoutParams(childParams);
		}else{
			/*by dl 增加判断 301医院 非医保挂号*/
			LinearLayout.LayoutParams arams =null;
			if (elements.size()==2||elements.size()==3) {
				arams = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1);
			}else {
				 arams = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			}
			textview.setLayoutParams(arams);
		}
		
		LogGloble.i("info", "dbma==" + dbMap);//TRAN_TELLER
		//在
		 if(key!=null&&!"".equals(key)){//_SYSTEMDATETIME
			 if(BTCCMWApplication.isDisp != null&&BTCCMWApplication.dispMap!=null &&BTCCMWApplication.isDisp.equals("1") && BTCCMWApplication.dispMap.containsKey(key)){
				 textview.setText(BTCCMWApplication.dispMap.get(key).toString());
			 }
			 //福建燃气。link
			 else if(BTCCMWApplication.linkParamsMap.containsKey(key) &&BTCCMWApplication.linkParamsMap.get(key)!=null){
					textview.setText(BTCCMWApplication.linkParamsMap.get(key));
			} else if(BTCCMWApplication.responsemap.containsKey(key)&&BTCCMWApplication.responsemap.get(key)!=null ){//
				if(view!=null){//进行显示的天津市和平区华夏未来实验幼儿园=20150713161826656,key显示 value上送
					 if(BTCCMWApplication.optionMap.containsValue(BTCCMWApplication.responsemap.get(key))){
						for(Map.Entry<String, String> entry:BTCCMWApplication.optionMap.entrySet()){
							String entryValue = entry.getValue();
							if(entryValue.equals(BTCCMWApplication.responsemap.get(key))){
								textview.setText(entry.getKey());
								break;
							}
						}
					}
					else{
						String content = BTCCMWApplication.responsemap.get(key).toString();
						textview.setText(content);
					}
				}else{//进行返回的值
					if(BTCCMWApplication.optionMap.containsKey(BTCCMWApplication.responsemap.get(key).toString())){//combox中的option
						textview.setText(BTCCMWApplication.optionMap.get(BTCCMWApplication.responsemap.get(key).toString()));
					}else{
						String content = BTCCMWApplication.responsemap.get(key).toString();
						textview.setText(content);
					}
				}
				
			}
			else if(cmwApplication.getVar(key)!=null){//<param name="CUST_ID"><use>CUST_ID</use></param>
				if(view!=null){//进行显示的
					 //wuhanp503
					if(BTCCMWApplication.hiddenboxmap.containsKey(key)&&BTCCMWApplication.hiddenboxmap.get(key)!=null &&!"".equals(BTCCMWApplication.hiddenboxmap.get(key).toString())){
						String content =BTCCMWApplication.hiddenboxmap.get(key).toString();
						textview.setText(content);
					}else if(BTCCMWApplication.optionMap.containsValue(cmwApplication.getVar(key))){//||BTCCMWApplication.optionMap.containsKey(key)天津学费
//						textview.setText(BTCCMWApplication.optionMap.get(cmwApplication.getVar(key)));
						for(Map.Entry<String, String> entry:BTCCMWApplication.optionMap.entrySet()){
							String entryValue = entry.getValue();
							if(entryValue.equals(cmwApplication.getVar(key))){
								/*by dl +lines 6浙江杭州福彩 定投*/
								if (parent_elementName!=null&&parent_element_label!=null) {
									if (parent_element_label.equals("fcdt_termno")||parent_element_label.equals("fcdt_times")||parent_element_label.equals("fcdt_nums")) {
										textview.setText(entry.getValue());
									}else {
										textview.setText(entry.getKey());
									}
								}else {
									textview.setText(entry.getKey());
								}
//								textview.setText(entry.getKey());//by dl //浙江杭州福彩 定投
								break;
							}
						}
					}
					else{
						textview.setText(cmwApplication.getVar(key));
					}
				}else{//进行返回的值
					
					if(BTCCMWApplication.optionMap.containsKey(cmwApplication.getVar(key))){//combox中的option
						textview.setText(BTCCMWApplication.optionMap.get(cmwApplication.getVar(key)));
					}
//					else if("DATA_LIST".equals(btcElement.getParentElement().getParentElement().getName(BTCLable.NAME))
//							&&BTCCMWApplication.hiddenboxmap.containsKey("DATA_LIST")){
//						Map<String, String> hiddenMap = new HashMap<String, String>();
//						hiddenMap = (Map<String, String>) BTCCMWApplication.hiddenboxmap.get("DATA_LIST");
//						String text = hiddenMap.get(key);
//						textview.setText(text);
//					}
//					wuhan正确的P503 等某个批次再打开
					else if(BTCCMWApplication.hiddenboxmap.containsKey(key)&&BTCCMWApplication.hiddenboxmap.get(key)!=null &&!"".equals(BTCCMWApplication.hiddenboxmap.get(key).toString())){
						String content =BTCCMWApplication.hiddenboxmap.get(key).toString();
						textview.setText(content);
					}
					else{
//						if(cmwApplication.getVar(key).contains("/")){
//							textview.setText(cmwApplication.getVar(key).replace("/", ""));
//						}else{
							textview.setText(cmwApplication.getVar(key));
//						}
						
					}
				}
				
				
			} 
			else if(BTCCMWApplication.hiddenboxmap.containsKey(key)&&BTCCMWApplication.hiddenboxmap.get(key)!=null &&!"".equals(BTCCMWApplication.hiddenboxmap.get(key).toString())){
				String content =BTCCMWApplication.hiddenboxmap.get(key).toString();
				textview.setText(content);
			}else if(dbMap!=null&&dbMap.containsKey(key)&&!"".equals(key)&&dbMap.get(key)!=null){
				String content = dbMap.get(key).toString();
				textview.setText(content);
			}
			else if(BTCCMWApplication.flowFileLangMap.containsKey(key)&&BTCCMWApplication.flowFileLangMap.get(key)!=null ){
				if(view!=null){////进行显示的
					String content =BTCCMWApplication.flowFileLangMap.get(key).toString();
					textview.setText(content);
				}
				else{//进行返回的
					String content =BTCCMWApplication.flowFileLangMap.get(key).toString();
					textview.setText(content);//福建厦门交通卡
//					textview.setText("");// 上送也要上送空
				}
			}else if(BTCCMWApplication.requestParamsMap.containsKey(key)&&null!=BTCCMWApplication.requestParamsMap.get(key) ){
				if(null !=BTCCMWApplication.requestParamsMap.get(key)){
					textview.setText(BTCCMWApplication.requestParamsMap.get(key));
				}
				else{
					textview.setText("");
				}
			}
			else if("_CIFNUMBER".equals(key)){
				if(LoginActivity.cifNumber!=null){
					textview.setText(LoginActivity.cifNumber);//取登录时的cifnumber;
				}
			}
			else if(key.equals("_USERMOBILE")){//
				if(LoginActivity.moblies!=null){
					textview.setText(LoginActivity.moblies);//取登录时的moblies;
				}
			}
//			else if(cmwApplication.checkboxlist.containsKey(key)){
//				optList = cmwApplication.checkboxlist.get(key);
//			}
//			else if(BTCCMWApplication.resultmap.containsKey(key)&&BTCCMWApplication.resultmap.get(key)!=null){
//				//网络请求回来的数据
//				textview.setText(BTCCMWApplication.resultmap.get(key).toString());
//			}
			else if(BTCCMWApplication.getRadioRequest !=null && BTCCMWApplication.getRadioRequest.containsKey(key)&&BTCCMWApplication.getRadioRequest.get(key)!=null){//在loop中有radio的情况
				textview.setText(BTCCMWApplication.getRadioRequest.get(key).toString());
			}else if(key.equals("currentIndex")){
				if(BTCCMWApplication.totalNumber == 0){
					textview.setText("");
				}else{
					textview.setText(BTCCMWApplication.totalNumber + "");
				}
			}
//			else if(key.equals("DATA_LIST")){
//				textview.setText(key);
//			}
			else if(BTCCMWApplication.requestMap.containsKey(key)){
				textview.setText(BTCCMWApplication.requestMap.get(key));
			}
			else{
				if(view!=null){//301
					textview.setText("");
				}
				else{
					textview.setText("");
				}
			}
		}else{
			if(view!=null){
				textview.setText("");
			}
			else{
				textview.setText(key);
			}
		}
		 textview.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				PopupWindowUtils.getInstance().setOnShowAllTextListener(BTCUiActivity.getInstance(), (TextView) textview);
			}
		});

		 if(view!=null){
			 ((ViewGroup) view).addView(textview);
			 return null;
		 }else{
//			 if(optList.size()!=0 &&(textview.getText().toString()==null ||"".equals(textview.getText().toString()))){
//				 return optList;
//			 }else 
			 if(textview.getText().toString().contains("请选择")){
				 textview.setText("");
			 }
			 if(isDataList== true && StringUtil.isNull(textview.getText().toString())){
				 return key;
			 }
				return textview.getText().toString();
		 }
		 
	}

	
	private String getText(String content){
		if(content.contains(" ")){
			content=content.replace(" ", "");
		}else if(content.contains(" ")){
			content=content.replace(" ", "");
		}
		return content ;
	}
}

