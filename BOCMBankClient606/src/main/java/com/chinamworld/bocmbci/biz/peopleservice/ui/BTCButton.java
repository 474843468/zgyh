package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cfca.mobile.log.CodeException;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCUiData;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLableAttribute;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IFunction;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IHttpCallBack;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IInputCheck;
import com.chinamworld.bocmbci.biz.peopleservice.utils.UnitTransition;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.UsbKeyText;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import cfca.mobile.sip.CFCASipDelegator;
import cfca.mobile.sip.SipBox;

public class BTCButton extends BTCDrawLable {
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCButton.class.getSimpleName();
	String key;
	/** 界面栈对象 */
	private BTCActivityManager activityManager;// 界面栈对象
	/** 全局变量对象 */
	private BTCCMWApplication cmwApplication;
	private final int BTU_TEXT_SIZE=16;// by dl +

	BTCUiData pageData;
	Map<String,String> boundtargetListradio = new HashMap<String, String>();
	Map<String,String> boundtargetlistDynamic =  new HashMap<String, String>();
	Map<String,String> boundtargetspinner =  new HashMap<String, String>();
//	List<Map<String, String>> boundtargetEdittexts = new ArrayList<Map<String,String>>();
	Map<String,String> boundtargetEdittext =  new HashMap<String, String>();
	Map<String, String> boungtragetValue = new HashMap<String,String>();
	BTCUiActivity self;
	boolean isSms = false;
	String checkform = "";
//	boolean isRight = true;
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCButton(Context context,BTCElement element) {
		super(context,element);
		elementType = ElementType.Button;
		//this.context = context;
		activityManager = BTCActivityManager.getInstance();
		cmwApplication = BTCUiActivity.getApp();
		this.self = BTCUiActivity.getInstance();
	}

	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		String name = btcElement.getElementName();
		BTCElement childElement;
		BTCDrawLable btcDrawLable;
		key = params.get("target");//button中的target 与step中的name相同，
		
		if(params.containsKey("checkform")){
			checkform = params.get("checkform");
		}
		
		String buttontext = childElements.get(0).getText();
//		RelativeLayout reLayout = new RelativeLayout(context);
//		RelativeLayout.LayoutParams reParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
//				ViewGroup.LayoutParams.WRAP_CONTENT);
		
		
		LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		LinearLayout Layout = new LinearLayout(context);
		Layout.setOrientation(LinearLayout.VERTICAL);
		Layout.setLayoutParams(Params);
//		LinearLayout Layout1 = new LinearLayout(context);//存放安全因子控件
//		Layout1.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)); 
//		Layout1.setOrientation(LinearLayout.HORIZONTAL);
//		LinearLayout Layout2 = new LinearLayout(context);//存放button
//		Layout2.setOrientation(LinearLayout.HORIZONTAL);
//		Layout2.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)); 
//		Layout2.setGravity(Gravity.CENTER);
		
//		Layout1.setId(001);
//		Layout2.setId(002);
		
		
		//在这时画安全控件。
		if(BTCCMWApplication.isContainSecurityActive&&BTCCMWApplication.factorList!=null&&BTCCMWApplication.factorList.size()!=0){
			cmwApplication.removeVar("Smc");
			cmwApplication.removeVar("Otp");
			cmwApplication.removeVar("_signedData");
			UsbKeyText usb = addUsbKey(view);
			for(int i = 0 ;i<BTCCMWApplication.factorList.size();i++){
					Map<String, Object> fieldMap = BTCCMWApplication.factorList.get(i);
					Map<String, Object> field = (Map<String, Object>) fieldMap.get("field");
					String securityName = (String) field.get("name");
					String securityType = (String) field.get("type");
					if(securityName.equals("Smc")){//短信验证码
						LinearLayout Layout1 = new LinearLayout(context);//存放安全因子控件
						Layout1.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,(int)context.getResources().getDimension(R.dimen.btn_bottom_height))); 
						Layout1.setOrientation(LinearLayout.HORIZONTAL);
						Layout1.setGravity(Gravity.CENTER_VERTICAL);
						TextView textview = new TextView(context);
						textview.setTextSize(15);
						textview.setTextColor(context.getResources().getColor(R.color.black));
						LinearLayout.LayoutParams paramtext = new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
						paramtext.weight = 1;
						textview.setText("手机交易码：");
						textview.setLayoutParams(paramtext);
						
						LinearLayout LayoutSipBox = new LinearLayout(context);
						LinearLayout.LayoutParams paramSipBox = new LinearLayout.LayoutParams(
								 LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT);
						paramSipBox.weight = 1;
						LayoutSipBox.setLayoutParams(paramSipBox);
						
						LinearLayout LayoutsipSms = new LinearLayout(context);
						LinearLayout.LayoutParams paramsipSms = new LinearLayout.LayoutParams(
								 LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
						paramsipSms.weight = 1;
						LayoutsipSms.setLayoutParams(paramsipSms);
						
						SipBox sipSms = new SipBox((Activity) context);
						sipSms.setLayoutParams(paramsipSms);
						int hashcode = securityName.hashCode();//  ??????
						hashcode = hashcode > 0 ? hashcode : -hashcode;
						sipSms.setId(hashcode);
						activityManager.putWidgetId(securityName, hashcode);
						sipSms.setCipherType(SystemConfig.CIPHERTYPE);
						sipSms.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
						sipSms.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
					
						sipSms.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
						sipSms.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
						sipSms.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
						sipSms.setSipDelegator((CFCASipDelegator) context);
						sipSms.setRandomKey_S(BTCCMWApplication.securityRandom);
						sipSms.setHeight((int) context.getResources().getDimension(R.dimen.edittext_height));
						sipSms.setWidth(LayoutParams.MATCH_PARENT);
						sipSms.setTextColor(context.getResources().getColor(R.color.black));
						sipSms.setBackgroundResource(R.drawable.bg_for_edittext);
						LayoutsipSms.addView(sipSms);
						Button button1 = new Button(context);
						
//						@dimen/textsize_default
//						context.getResources().getDimension(R.dimen.menuitem_text)
//						button1.setTextSize(10);
						button1.setText("获取");
						button1.setWidth((int)context.getResources().getDimension(R.dimen.dp_five_zero));
						button1.setHeight((int)context.getResources().getDimension(R.dimen.btn_smscode_height));
//						button1.setBackgroundResource(R.drawable.timebutton);
						SmsCodeUtils.getInstance().addSmsCodeListner(button1,
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										// 发送验证码到手机
										isSms = true;
										self.sendMSCToMobileCallback(new IHttpCallBack() {
											
											@Override
											public void requestHttpSuccess(Object result) {
												Map<String, Object> map = BTCCMWApplication.resultmap;
												BiiHttpEngine.dissMissProgressDialog();
												isSms = false;
											}
										});
									}
								});
						
						while(!BTCCMWApplication.flag){
							while(isSms){
								Map<String, Object> map = BTCCMWApplication.resultmap;
								BiiHttpEngine.dissMissProgressDialog();
								isSms = false;
							}
							break;
						}
						
//						button1.setLayoutParams(parambtn);
						Layout1.addView(textview);
						LayoutSipBox.addView(LayoutsipSms);
						LayoutSipBox.addView(button1);
						Layout1.addView(LayoutSipBox);
//						Layout1.addView(sipSms);
//						Layout1.addView(button1);
						Layout.addView(Layout1);
					}else if(securityName.equals("Otp")){//动态口令
						LinearLayout Layout3 = new LinearLayout(context);//存放安全因子控件
						Layout3.setOrientation(LinearLayout.HORIZONTAL);
						TextView textview = new TextView(context);
						textview.setTextSize(15);
						textview.setTextColor(context.getResources().getColor(R.color.black));
						LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,(int)context.getResources().getDimension(R.dimen.btn_bottom_height));
						Layout3.setGravity(Gravity.CENTER_VERTICAL);
						Layout3.setLayoutParams(mParams);
						
						
						LinearLayout.LayoutParams paramtext = new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
						paramtext.weight = 1;
						textview.setText("动态口令：");
						textview.setLayoutParams(paramtext);
						
						LinearLayout LayoutSipBox = new LinearLayout(context);
						LinearLayout.LayoutParams paramSipBox = new LinearLayout.LayoutParams(
								 LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT);
						paramSipBox.weight = 1;
						LayoutSipBox.setLayoutParams(paramSipBox);
						LayoutSipBox.setPadding(0, 0, 0, 0);
						
						SipBox sipBox = new SipBox((Activity) context);
						
						int hashcode = securityName.hashCode();//  ??????
						hashcode = hashcode > 0 ? hashcode : -hashcode;
						sipBox.setId(hashcode);
						activityManager.putWidgetId(securityName, hashcode);
						sipBox.setCipherType(SystemConfig.CIPHERTYPE);
						LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
								LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
						sipBox.setLayoutParams(param);
						sipBox.setTextColor(context.getResources().getColor(R.color.black));
						sipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
						sipBox.setGravity(Gravity.CENTER_VERTICAL);
						sipBox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
						sipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//						sipBox.setId(10002);
						sipBox.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
						sipBox.setBackgroundResource(R.drawable.bg_for_edittext);
						sipBox.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
						sipBox.setSipDelegator((CFCASipDelegator) context);
						sipBox.setRandomKey_S(BTCCMWApplication.securityRandom);
						sipBox.setHeight((int) context.getResources().getDimension(R.dimen.edittext_height));
						sipBox.setWidth(LayoutParams.MATCH_PARENT);
						sipBox.setBackgroundResource(R.drawable.bg_for_edittext);
						LayoutSipBox.addView(sipBox);
						Layout3.addView(textview);
						Layout3.addView(LayoutSipBox);
						
						Layout.addView(Layout3);
						
					}else if(securityName.equals("_signedData")){//中银E盾
						int hashcode = securityName.hashCode();//  ??????
						hashcode = hashcode > 0 ? hashcode : -hashcode;
						usb.setId(hashcode);
						cmwApplication.setVar(securityName, "");
					}else if(securityName.equals("deviceInfo")){//硬件绑定
						
					}
					
				}
				BTCCMWApplication.factorList = null;
				BTCCMWApplication.isContainSecurityActive = false;
			
		
	}
		
		Button button = new Button(context);
//		button.setTextSize(context.getResources().getDimension(R.dimen.menuitem_text));
		button.setBackgroundResource(R.drawable.btn_red_big_long);//by dl+
		button.setTextSize(BTU_TEXT_SIZE);// by dl+
		button.setTextColor(context.getResources().getColor(R.color.white));//by dl+
		
		if(BTCCMWApplication.flowFileLangMap.get(buttontext)!=null&&!BTCCMWApplication.flowFileLangMap.get(buttontext).equals("")){
			button.setText(BTCCMWApplication.flowFileLangMap.get(buttontext).toString());	
		}else{
			button.setText(buttontext);		
		}
		button.setOnClickListener(onClickListener);
//		Layout2.addView(button);
//		Layout.addView(Layout2);
		
		/*by dl + 浙江杭州 网络快捷贷*/
		Map<String, String> parent_element_params = btcElement.getParentElement().getParams();
		String parent_label = parent_element_params.get("label");
		List<BTCElement> elements = btcElement.getParentElement().getChildElements();
		if (parent_label!=null&&parent_label.equals("NetLoan_FREELIMIT")&&elements.size()==5) {
			button.setLayoutParams(new LinearLayout.LayoutParams(UnitTransition.dip2px(context, 120), UnitTransition.dip2px(context, 50)));
		}else {
		((ViewGroup) view).addView(Layout);
		}/*by dl end*/
//		((ViewGroup) view).addView(Layout);//注掉，浙江杭州 网络快捷...问题调试
		((ViewGroup) view).addView(button);
		
		
		return null;
	}
	
	private UsbKeyText addUsbKey(View view) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("factorList", BTCCMWApplication.factorList);
		// 还需要为_plainData,_certDN赋值
		map.put("_plainData",BTCCMWApplication._plainData);
		map.put("_certDN",BTCCMWApplication._certDN);
		
		UsbKeyText usb = new UsbKeyText(context);
		usb.Init(BTCCMWApplication.conversationId, BTCCMWApplication.securityRandom, map, BTCUiActivity.getInstance());
		usb.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		usb.setTextViewPosition(Gravity.LEFT);
		((ViewGroup) view).addView(usb);
		
		
		
		BTCCMWApplication.usbKeyText = usb;
		
		return usb;
	}
	
	boolean isviewNull = false;
	/**
	 * 监听事件,用于点击按钮向服务器发起通讯时使用
	 */
	public OnClickListener onClickListener = new View.OnClickListener() {
		BTCElement childElement;
		BTCDrawLable btcDrawLable;
		List<BTCElement> widgetList = new ArrayList<BTCElement>(); // 存放标签信息
		List<String> widgetList1 = new ArrayList<String>(); // 存放标签信息
		String radioBoundflag ="";
		String editTextbindtarget = "";
		String spinnerbindtarget = "";
		public void onClick(View v) {
//			{expertId=919441859, departmentId=1968697911}
			Button btn = (Button) v;
			
//			if(btn.getText().toString().contains("上")||btn.getText().toString().contains("返")||btn.getText().toString().contains("取消")){
//				childElement = BTCButton.this.FindElementBy(key);
//				BTCCMWApplication.listradio.clear();
//				BTCCMWApplication.checkboxlist.clear();
//				BTCCMWApplication.listDynamictextbox.clear();
//				//childElement = BTCCMWApplication.getStepmap().get(key);
//				widgetList.add(childElement);
//				BTCUiActivity.Instance().navigationToActivity(childElement);
//				return;
//			}
		 	 
			Map<String, Integer> widgetIds = activityManager.getWidgetIds();
//			if("true".equals(checkform)||"".equals(checkform)){
				if (BTCCMWApplication.listradio.size() > 0) {
					boolean isChecked = false;
					boundtargetListradio = new HashMap<String, String>();
					for (int i = 0; i < BTCCMWApplication.listradio.size(); i++) {

						String name = BTCCMWApplication.listradio.get(i);
						
						if (widgetIds.containsKey(name)) {
							View view = null;// 迭代使用的临时控件
							
							view = activityManager.currentActivity().findViewById(
									(Integer) widgetIds.get(name));
							if(view == null){
								isviewNull = true;
								continue;
							}
							isviewNull = false;
							RadioButton radioBotton = (RadioButton) view;
							Map<String, Object> msg = (Map<String, Object>) radioBotton.getTag();
							if(radioBotton.isChecked()){
								isChecked = true;
								msg.put("isChecked", isChecked);
								//只要是选中的，就可以是上送的值。用在有if标签的情况。
								BTCCMWApplication.ifMap = msg;//判断是否有bugnflag,如果有还有找与之关联的相同的buondflag.
								if(msg.size()!=0&&msg.containsKey("boundflag")){
									//textbox 可与radio绑定,还有别的可能。
									radioBoundflag = (String) msg.get("boundflag");
								}else{
									radioBoundflag = "";
								}
								//保存点击请求参数
								Map<String, String> requestMap = new HashMap<String, String>();
								requestMap = (Map<String, String>) msg.get("params");
								BTCCMWApplication.getRadioRequest = requestMap;
								
								if(BTCCMWApplication.flowFileLangMap.containsKey(name) &&BTCCMWApplication.flowFileLangMap.get(name).toString()!=null){
									BTCUiActivity.getApp().setVar(name, BTCCMWApplication.flowFileLangMap.get(name).toString());
								}else{
									if(msg.containsKey("name")&&msg.containsKey("value")){
										String valueName  = msg.get("name").toString();
										String valueV = (String) msg.get("value");
										cmwApplication.setVar(valueName, valueV);
									}
										
								}
							}
							if (msg.containsKey(BTCLableAttribute.BOUNDFLAG)) {
								boundtargetListradio.put("boundflag",(String) msg.get(BTCLableAttribute.BOUNDFLAG));
								boundtargetListradio.put("viewId",(Integer) widgetIds.get(name)+"");
								boundtargetListradio.put("value", "");
							}
							
							
						}

					}
					if(!isChecked&&!isviewNull){
						return;
					}
					
				}
				
				if (BTCCMWApplication.checklist.size() > 0) {
					boolean isChecked = false;
					String checkrule = "";
					String useprompt = "";
					String prompt = "";
//					boundtargetListradio = new HashMap<String, String>();
					for (int i = 0; i < BTCCMWApplication.checklist.size(); i++) {

						String name = BTCCMWApplication.checklist.get(i);
						
						if (widgetIds.containsKey(name)) {
							View view = null;// 迭代使用的临时控件

							view = activityManager.currentActivity().findViewById(
									(Integer) widgetIds.get(name));
							if(view == null){
								isviewNull = true;
								continue;
							}
							isviewNull = false;
							CheckBox checkBoxBotton = (CheckBox) view;
							Map<String, Object> msg = (Map<String, Object>) checkBoxBotton.getTag();
							if(msg.containsKey("checkrule")){
								checkrule = (String) msg.get("checkrule");
							}
							if(msg.containsKey(BTCLableAttribute.PROMPT)){
								prompt = (String) msg.get(BTCLableAttribute.PROMPT);
							}
							if(msg.containsKey(BTCLableAttribute.USEPROMPT)){
								useprompt = msg.get(BTCLableAttribute.USEPROMPT).toString();
							}
							if(checkBoxBotton.isChecked()){
								isChecked = true;
//								msg.put("isChecked", isChecked);
								//只要是选中的，就可以是上送的值。用在有if标签的情况。
//								BTCCMWApplication.ifMap = msg;//判断是否有bugnflag,如果有还有找与之关联的相同的buondflag.
//								if(msg.size()!=0&&msg.containsKey("boundflag")){
//									//textbox 可与radio绑定,还有别的可能。
//									radioBoundflag = (String) msg.get("boundflag");
//								}else{
//									radioBoundflag = "";
//								}
								//保存点击请求参数
//								Map<String, String> requestMap = new HashMap<String, String>();
//								requestMap = (Map<String, String>) msg.get("params");
//								BTCCMWApplication.getRadioRequest = requestMap;
//								
//								if(BTCCMWApplication.flowFileLangMap.containsKey(name) &&BTCCMWApplication.flowFileLangMap.get(name).toString()!=null){
//									BTCUiActivity.getApp().setVar(name, BTCCMWApplication.flowFileLangMap.get(name).toString());
//								}else{
//									if(msg.containsKey("name")&&msg.containsKey("value")){
//										String valueName  = msg.get("name").toString();
//										String valueV = (String) msg.get("value");
//										cmwApplication.setVar(valueName, valueV);
//									}
//										
//								}
							}
//							if (msg.containsKey(BTCLableAttribute.BOUNDFLAG)) {
//								boundtargetListradio.put("boundflag",(String) msg.get(BTCLableAttribute.BOUNDFLAG));
//								boundtargetListradio.put("viewId",(Integer) widgetIds.get(name)+"");
//								boundtargetListradio.put("value", "");
//							}
							
							
						}

					}
					
					if(!isChecked && checkrule.contains("required:true")&&!isviewNull &&("true".equals(checkform)||"".equals(checkform))){
						isCheckeds = false;
						if("true".equals(useprompt)&&prompt!=null){//使用自定义提示
							BaseDroidApp.getInstanse().showInfoMessageDialog((String) BTCCMWApplication.flowFileLangMap.get(prompt).toString()+"");
							return;
						}
						else{
							BaseDroidApp.getInstanse().showInfoMessageDialog("至少一个被选中");
							return;
						}
					}else{
						isNotNull = true;
						isRightMaxlength= true;
						isRightMinlength = true;
						isRightRangelength = true;
						isRightPatter = true;
						isnonlicetchar = true;
						isdatecompare = true;
						isCheckeds = true;
						isCheckeds = true;
					}
					
				}
				
				if (BTCCMWApplication.listDynamictextbox.size() > 0) {
					for (int i = 0; i < BTCCMWApplication.listDynamictextbox.size(); i++) {
						String id = BTCCMWApplication.listDynamictextbox.get(i);
						if (widgetIds.containsKey(id)) {
							View view = null;// 迭代使用的临时控件

							view = activityManager.currentActivity().findViewById(
								 widgetIds.get(id));
							LinearLayout childview = (LinearLayout) view;
							
							if (boundtargetlistDynamic != null) {
								if (boundtargetlistDynamic.containsValue(childview.getTag())) {
									for (int j = 0; j < childview.getChildCount(); j++) {
										Map<String, String> postParams = new HashMap<String, String>();
										Iterator widgetId_iterator = BTCCMWApplication.dynamictextboxmap
												.entrySet().iterator();
										while (widgetId_iterator.hasNext()) {
											Map.Entry entry = (Map.Entry) widgetId_iterator
													.next();
											View viewc = null;// 迭代使用的临时控件

											viewc = childview.getChildAt(j)
													.findViewById(
															Integer.parseInt(entry.getValue().toString()));
											

											if (viewc instanceof EditText) {// 输入框
												// 从相应的变量中取得用户输入或者选择的值
												EditText editText = (EditText) viewc;
											
												LogGloble.i(TAG, editText.getText().toString());
												postParams.put(entry.getKey().toString(), editText.getText().toString());
											}
										}
										
										BTCCMWApplication.dynamictextboxlist.add(postParams);
										
									}
								}
							}else{
								for (int j = 0; j < childview.getChildCount(); j++) {
									Map<String, String> postParams = new HashMap<String, String>();
									Iterator widgetId_iterator = BTCCMWApplication.dynamictextboxmap
											.entrySet().iterator();
									while (widgetId_iterator.hasNext()) {
										Map.Entry entry = (Map.Entry) widgetId_iterator
												.next();
										View viewc = null;// 迭代使用的临时控件
										viewc = childview.getChildAt(j).findViewById(Integer.parseInt(entry.getValue().toString()));
										if (viewc instanceof EditText) {// 输入框
											// 从相应的变量中取得用户输入或者选择的值
											EditText editText = (EditText) viewc;
											LogGloble.i(TAG, editText.getText().toString());
											postParams.put(entry.getKey().toString(), editText.getText().toString());
										}
									}
									
									BTCCMWApplication.dynamictextboxlist.add(postParams);
									
								}
							}
							
						}

					}
					LogGloble.i("info", BTCCMWApplication.dynamictextboxlist.toString());
				}

				if (widgetIds != null) {//1845930631
					Iterator widgetId_iterator = widgetIds.entrySet().iterator();
					String widgetValue = "";
					String values = "";
					String random = "";
//						CheckData(widgetId_iterator);
					// 遍历所有有设置id号的控件
					while (widgetId_iterator.hasNext()) {
						Map.Entry entry = (Map.Entry) widgetId_iterator.next();
						View view = null;// 迭代使用的临时控件

						view = activityManager.currentActivity().findViewById(
								(Integer) entry.getValue());
						String checkrule = (String) activityManager.getWidgetcheckrule((Integer) entry.getValue());

						if (view instanceof EditText) {// 输入框
							if(view instanceof SipBox){
								// 从相应的变量中取得用户输入或者选择的值
								SipBox sipBox = (SipBox) view;
								widgetValue = sipBox.getText().toString();
								try {
									values = sipBox.getValue().getEncryptPassword();
									random = sipBox.getValue().getEncryptRandomNum();
									cmwApplication.setVar((String) entry.getKey(),values);
									if(BTCCMWApplication.hiddenboxmap.containsKey((String) entry.getKey())){
										BTCCMWApplication.hiddenboxmap.put((String) entry.getKey(), values);
									}
									cmwApplication.setVar((String) entry.getKey()+"_RC",random);
								} catch (CodeException e) {
									e.printStackTrace();
								}
								if("true".equals(checkform)||"".equals(checkform)){
									if (!checkrequired(widgetValue)) {
										BaseDroidApp.getInstanse().showInfoMessageDialog("请输入手机交易码或请输入动态口令");
										return;
									}
									
									if(widgetValue.length()<6){
										isNotNull = false;
										BaseDroidApp.getInstanse().showInfoMessageDialog("请输入6位手机交易码或请输入动态口令");
										return;
									}
								}else{
									isNotNull = true;
									isRightMaxlength= true;
									isRightMinlength = true;
									isRightRangelength = true;
									isRightPatter = true;
									isnonlicetchar = true;
									isdatecompare = true;
									isCheckeds = true;
								}
								
							}else{
								// 从相应的变量中取得用户输入或者选择的值
								EditText editText = (EditText) view;
								Map<String, Object> msg = new HashMap<String, Object>();
								msg = (Map<String, Object>) editText.getTag();
								String tag  = "";
								String useprompt = "";
								String  prompt = "" ;
								String lable = "";
								
								
								if(msg.size()!=0){
									tag = (String)msg.get(BTCLableAttribute.BINDTARGET);
									 useprompt  = (String) msg.get(BTCLableAttribute.USEPROMPT);
									 prompt= (String) msg.get(BTCLableAttribute.PROMPT);
									 lable = (String) msg.get("label");
									 if(msg.containsKey("bindtarget")){
										//textbox 可与radio绑定,还有别的可能。
										 editTextbindtarget = (String) msg.get("bindtarget");
									 }else{
										 editTextbindtarget = "";
									 }
								}
								
								widgetValue = editText.getText().toString();
								cmwApplication.setVar((String) entry.getKey(),widgetValue);
								if(BTCCMWApplication.hiddenboxmap.containsKey((String) entry.getKey())){//&& "".equals(BTCCMWApplication.hiddenboxmap.get((String)entry.getKey()))
									BTCCMWApplication.hiddenboxmap.put((String) entry.getKey(), widgetValue);
								}
								if (msg.containsKey(BTCLableAttribute.BINDTARGET)) {
									
									boundtargetEdittext.put("boundflag",(String) msg.get(BTCLableAttribute.BINDTARGET));
									boundtargetEdittext.put("viewId",(Integer) entry.getValue()+"");
									boundtargetEdittext.put("value",widgetValue);
//									boundtargetEdittext.put("name", (String) entry.getKey());
									boundtargetEdittext.put((String) entry.getKey(), widgetValue);
//									boundtargetEdittexts.add(boundtargetEdittext);
								}
								
								if(msg.containsKey("bindtarget")){
									//绑定的判断
									if(!"".equals(radioBoundflag)){//证明radio此时有绑定的数据
										if(editTextbindtarget.equals(radioBoundflag)){
											//要判断editext是否为空
											if (checkrule != null&& checkrule.contains("required")&&("true".equals(checkform)||"".equals(checkform))) {
												 if(widgetValue.length() < 1) {
													isNotNull = false;
													if("true".equals(useprompt)&&prompt!=null){//使用自定义提示
														BaseDroidApp.getInstanse().showInfoMessageDialog((String) BTCCMWApplication.flowFileLangMap.get(prompt).toString()+"");
														return;
													}
													else{
														if(!"".equals(key)&&BTCCMWApplication.flowFileLangMap.containsKey(lable)){
															String tip = BTCCMWApplication.flowFileLangMap.get(lable).toString();
															if(tip.contains(":")){
																tip = tip.replace(":", "");
															}else if( tip.contains("：")){
																tip = tip.replace("：", "");
															}
															BaseDroidApp.getInstanse().showInfoMessageDialog(tip+"不能为空");
														}else{
															BaseDroidApp.getInstanse().showInfoMessageDialog("不能为空");
														}
//															BaseDroidApp.getInstanse().showInfoMessageDialog( "不能为空");
														return;
													}
												}else{
													 //输入域不能为空
													isNotNull = true;
//														return ;
												}
											}else{
												isNotNull = true;
												isRightMaxlength= true;
												isRightMinlength = true;
												isRightRangelength = true;
												isRightPatter = true;
												isnonlicetchar = true;
												isdatecompare = true;
												isCheckeds = true;
											}
										}
										
									}else if("".equals(radioBoundflag)&&!"".equals(editTextbindtarget)){//没有绑定的
										//不做空判断
										continue;
									}
								}else {
									if (checkrule != null&& checkrule.contains("required") &&("true".equals(checkform)||"".equals(checkform))) {
										 if(widgetValue.length() < 1) {
											isNotNull = false;
											if("true".equals(useprompt)&&prompt!=null){//使用自定义提示
												BaseDroidApp.getInstanse().showInfoMessageDialog((String) BTCCMWApplication.flowFileLangMap.get(prompt).toString()+"");
												return;
											}
											else{
												if(!"".equals(key)&&BTCCMWApplication.flowFileLangMap.containsKey(lable)){
													String tip = BTCCMWApplication.flowFileLangMap.get(lable).toString();
													if(tip.contains(":")){
														tip = tip.replace(":", "");
													}else if( tip.contains("：")){
														tip = tip.replace("：", "");
													}
													BaseDroidApp.getInstanse().showInfoMessageDialog(tip+"不能为空");
												}else{
													BaseDroidApp.getInstanse().showInfoMessageDialog("不能为空");
												}
//													BaseDroidApp.getInstanse().showInfoMessageDialog( "不能为空");
												return;
											}
										}else{
											 //输入域不能为空
											isNotNull = true;
//												return ;
										}
									}else {
										isNotNull = true;
										isRightMaxlength= true;
										isRightMinlength = true;
										isRightRangelength = true;
										isRightPatter = true;
										isnonlicetchar = true;
										isdatecompare = true;
										isCheckeds = true;
									}
									if (boundtargetEdittext.size() != 0) {
										if (boundtargetEdittext.containsKey(tag)) {
											
											if(false == queryCheckRules(lable, checkrule, widgetValue, useprompt, prompt))
												return;
										}
									} else {
											if(false == queryCheckRules(lable, checkrule, widgetValue, useprompt, prompt))
												return;
									}
								}
								
							}

						} else if (view instanceof Spinner) {// 下拉框
							// 得到下拉菜单的选中项的相应信息D
							Spinner spinner = (Spinner) view;
							boundtargetspinner = new HashMap<String, String>();
							//wuhan
							Map<String, Object> msg = new HashMap<String, Object>();
							msg = (Map<String, Object>) spinner.getTag();
							ArrayList<String> accNumberList = (ArrayList<String>) msg.get("chose");
							/*by dl*/
							if(accNumberList.size()!=0){
								widgetValue=BTCActcombo.accNumberList.get(spinner.getSelectedItemPosition());//by dl 上送的值
							}else{
								widgetValue = spinner.getSelectedItem().toString();
							}
//								cmwApplication.setVar(String.valueOf(spinner.getId()),String.valueOf(spinner.getSelectedItemPosition()));//   1
							if(BTCCMWApplication.optionMap!=null && BTCCMWApplication.optionMap.containsKey(widgetValue)){
								cmwApplication.setVar((String) entry.getKey(),BTCCMWApplication.optionMap.get(widgetValue));//CityList,呈贡 UnitNO
							}else{
								cmwApplication.setVar((String) entry.getKey(),widgetValue);//CityList,呈贡 UnitNO
							}
//								String str = cmwApplication.getVar((String) entry.getKey());
							if(BTCCMWApplication.hiddenboxmap.containsKey((String) entry.getKey())&&BTCCMWApplication.optionMap.containsKey(widgetValue)){
								BTCCMWApplication.hiddenboxmap.put((String) entry.getKey(), BTCCMWApplication.optionMap.get(widgetValue));
							}
							//当option中有currentVaule的情况下，保存值到optionValue里面。
//							cmwApplication.optionValue.put((String) entry.getKey(),widgetValue);
							//都要判断是否为null
							String tag  = "";
							if(msg.containsKey(BTCLableAttribute.BINDTARGET)){
								 tag = (String)msg.get(BTCLableAttribute.BINDTARGET);
							}else if(msg.containsKey(BTCLableAttribute.BOUNDFLAG)){
								tag = (String)msg.get(BTCLableAttribute.BOUNDFLAG);
							}
							if(msg.size()!=0){
								tag = (String)msg.get(BTCLableAttribute.BINDTARGET);
								 if(msg.containsKey("bindtarget")){
									//textbox 可与radio绑定,还有别的可能。
									 spinnerbindtarget = (String) msg.get("bindtarget");
								 }else{
									 spinnerbindtarget = "";
								 }
							}
							
							String useprompt  = (String) msg.get(BTCLableAttribute.USEPROMPT);
							String  prompt= (String) msg.get(BTCLableAttribute.PROMPT);
							
							if (msg.containsKey(BTCLableAttribute.BOUNDFLAG)) {
								boundtargetspinner.put("boundflag",(String) msg.get(BTCLableAttribute.BOUNDFLAG));
								boundtargetspinner.put("viewId",(Integer) entry.getValue()+"");
								boundtargetspinner.put("value",widgetValue);
							}else if(msg.containsKey(BTCLableAttribute.BINDTARGET)){
								boundtargetspinner.put("boundflag",(String) msg.get(BTCLableAttribute.BOUNDFLAG));
								boundtargetspinner.put("viewId",(Integer) entry.getValue()+"");
								boundtargetspinner.put("value",widgetValue);
							}
//								widgetValue = list.get(position);//得到spinner中选中的数据!!!!!
//								cmwApplication.setVar((String) entry.getKey(),widgetValue);
							if(msg!=null&&msg.containsKey("bindtarget")){//含有tag的标签控件
								//绑定的判断
								if(!"".equals(radioBoundflag)){//证明radio此时有绑定的数据
									if(spinnerbindtarget.equals(radioBoundflag)){
										//要判断editext是否为空
										if (checkrule != null&& checkrule.contains("required")&&("true".equals(checkform)||"".equals(checkform))) {
											if(widgetValue.contains("请选择")){
												if(useprompt!=null && prompt!=null){
													if("true".equals(useprompt)){//使用自定义提示
														BaseDroidApp.getInstanse().showInfoMessageDialog((String) BTCCMWApplication.flowFileLangMap.get(prompt).toString()+"");
														isNotNull = false;
														return;
													}else if("false".equals(useprompt)){//使用机制定义的提示信息
														BaseDroidApp.getInstanse().showInfoMessageDialog("此项为必选项");
														isNotNull = false;
														return;
													}
												}else{
													BaseDroidApp.getInstanse().showInfoMessageDialog("此项为必选项");
													isNotNull = false;
													return;
												}
											}else{
												isNotNull = true;
											}
										}else{
											isNotNull = true;
											isRightMaxlength= true;
											isRightMinlength = true;
											isRightRangelength = true;
											isRightPatter = true;
											isnonlicetchar = true;
											isdatecompare = true;
											isCheckeds = true;
										}
									}
									
								}else if("".equals(radioBoundflag)&&!"".equals(editTextbindtarget)){//没有绑定的
									//不做空判断
									continue;
								}
//								if (boundtargetspinner != null) {
////										if (boundtargetspinner.containsValue(tag))
//									 {
//										
//										if(false == checkRule("", checkrule, widgetValue, useprompt, prompt))
//											return;
//										
//										if(widgetValue.contains("请选择")){
//											if(useprompt!=null && prompt!=null){
//												if("true".equals(useprompt)){//使用自定义提示
//													BaseDroidApp.getInstanse().showInfoMessageDialog((String) BTCCMWApplication.flowFileLangMap.get(prompt).toString()+"");
//													isNotNull = false;
//													return;
//												}else if("false".equals(useprompt)){//使用机制定义的提示信息
//													BaseDroidApp.getInstanse().showInfoMessageDialog("此项为必选项");
//													isNotNull = false;
//													return;
//												}
//											}else{
//												BaseDroidApp.getInstanse().showInfoMessageDialog("此项为必选项");
//												isNotNull = false;
//												return;
//											}
//										}else{
//											isNotNull = true;
//										}
//										
//									}
//								} else {
//										if(false == checkRule("", checkrule, widgetValue, useprompt, prompt)) 
//											return;
//									if(widgetValue.contains("请选择")){
//										if(useprompt!=null && prompt!=null){
//											if("true".equals(useprompt)){//使用自定义提示
//												BaseDroidApp.getInstanse().showInfoMessageDialog((String) BTCCMWApplication.flowFileLangMap.get(prompt).toString()+"");
//												isNotNull = false;
//												return;
//											}else if("false".equals(useprompt)){//使用机制定义的提示信息
//												BaseDroidApp.getInstanse().showInfoMessageDialog("此项为必选项");
//												isNotNull = false;
//												return;
//											}
//										}else{
//											BaseDroidApp.getInstanse().showInfoMessageDialog("此项为必选项");
//											isNotNull = false;
//											return;
//										}
//									}else{
//										isNotNull = true;
//									}
//								}
							}else{//不含有tag的标签控件
								if("true".equals(checkform)||"".equals(checkform)){
									if(false == queryCheckRules("", checkrule, widgetValue, useprompt, prompt))
										return;
									if(!StringUtil.isNullOrEmpty(checkrule)&& widgetValue.contains("请选择")){
										if(useprompt!=null && prompt!=null){
											if("true".equals(useprompt)){//使用自定义提示
												BaseDroidApp.getInstanse().showInfoMessageDialog((String) BTCCMWApplication.flowFileLangMap.get(prompt).toString()+"");
												isNotNull = false;
												return;
											}else if("false".equals(useprompt)){//使用机制定义的提示信息
												BaseDroidApp.getInstanse().showInfoMessageDialog("此项为必选项");
												isNotNull = false;
												return;
											}
										}else{
											BaseDroidApp.getInstanse().showInfoMessageDialog("此项为必选项");
											isNotNull = false;
											return;
										}
									}else{
										isNotNull = true;
									}	
								}else{
									isNotNull = true;
									isRightMaxlength= true;
									isRightMinlength = true;
									isRightRangelength = true;
									isRightPatter = true;
									isnonlicetchar = true;
									isdatecompare = true;
									isCheckeds = true;
								}
								
								
							}
							
						}
					}
					
				}
//			}
			
			//判断是否有相同的bundflag值
//				boungtragetValue 将所有具有bundflag的值都放在这个map里面然后存在application里面
			//radio 与edittext 绑定的情况
			if(boundtargetListradio.size()!=0&&boundtargetEdittext.size()!=0){
				for(Map.Entry<String, String> entryRadio: boundtargetListradio.entrySet()){
					String radiokey = entryRadio.getKey();//targ boundflag
					String radiovalue = entryRadio.getValue();//值searchTypeUserid  mailYes
//					for(int i = 0;i <boundtargetEdittexts.size();i++){
//						Map<String, String> boundEdites = boundtargetEdittexts.get(i);
						if(boundtargetEdittext.containsValue(radiovalue)){
							String editValue = boundtargetEdittext.get("value");
//								boungtragetValue.put(radiokey, radiovalue);
							boungtragetValue.put(radiovalue,editValue);//bundflag,searchTypeUserid
							for(Map.Entry<String, String> entryEdit:boundtargetEdittext.entrySet()){
								boungtragetValue.put(entryEdit.getKey(), entryEdit.getValue());
							}
							
						}
//					}
//					if(boundtargetEdittext.containsValue(radiovalue)){
//						String editValue = boundtargetEdittext.get("value");
////							boungtragetValue.put(radiokey, radiovalue);
//						boungtragetValue.put(radiovalue,editValue);//bundflag,searchTypeUserid
//					}
				}
				
				BTCCMWApplication.boungtragetValue =boungtragetValue;
			}else if(boundtargetListradio.size()!=0 &&boundtargetspinner.size()!=0 ){
				for(Map.Entry<String, String> entryRadio: boundtargetListradio.entrySet()){
					String radiokey = entryRadio.getKey();//targ
					String radiovalue = entryRadio.getValue();//值searchTypeUserid
					if(boundtargetspinner.containsValue(radiovalue)){
						String editValue = boundtargetspinner.get("value");
//							boungtragetValue.put(radiokey, radiovalue);
						boungtragetValue.put(radiovalue,editValue);//bundflag,searchTypeUserid
					}
				}
				
				BTCCMWApplication.boungtragetValue =boungtragetValue;
			}
			
			
			//在这里进行判断是否含有安全因子active;
			//1.判断当前step中是否含有security，init
			//2.再根当前button中的target 与BTCCMWApplication.getStepmap().get(key)
			//  中相比，看里面是否含有active;  yes，弹出安全因子对话框。 no ,走正常流程。
//			if("true".equals(checkform)){
				if(isNotNull && isRightMaxlength && isRightMinlength && isRightRangelength && isRightPatter&&isnonlicetchar&&isdatecompare){
					drawHidebox();
						BTCCMWApplication.listradio.clear();
					 BTCCMWApplication.checklist.clear();
					 BTCCMWApplication.listDynamictextbox.clear();
					 if(BTCCMWApplication.isContainSecurityInit ){//1.判断当前step中是否含有security，init
						//2.
						childElement = BTCCMWApplication.getStepmap().get(key);
						List<BTCElement> childList = childElement.getChildElements();//action,view
						if(childList.toString().contains("active")){
							//弹出安全因子对话框。
							BaseDroidApp.getInstanse().showSeurityChooseDialog(
									new OnClickListener() {
										@Override
										public void onClick(View v) {
											widgetList.add(childElement);
											BTCUiActivity.Instance().navigationToActivity(childElement);
											BTCCMWApplication.isContainSecurityActive = true;
//												BaseDroidApp.getInstanse().getSecurityChoosed();//选中的安全因子。
										}
									});
						}else{//不含有active
							childElement = BTCCMWApplication.getStepmap().get(key);
							widgetList.add(childElement);
							BTCUiActivity.Instance().navigationToActivity(childElement);
						}
					}else{//什么都没有的情况
						childElement = BTCCMWApplication.getStepmap().get(key);
						widgetList.add(childElement);
						BTCUiActivity.Instance().navigationToActivity(childElement);
					}
				}
				else {
					return;
				}
			
			 
		}
		
	

		
	};
	
	public void drawHidebox(){
		BTCDrawLable btcDrawLable;
		BTCElement viewElement = findNearElemntBy(new IFunction(){

			@Override
			public <T> boolean func(T t) {
				return ((BTCElement)t).getBTCDrawLable() instanceof BTCView;
			}});
		// 
		List<BTCElement> hiddenboxList = FindElementBy(null, viewElement, new IFunction(){

			@Override
			public <T> boolean func(T t) {
				return ((BTCElement)t).getBTCDrawLable() instanceof BTCHiddenbox;
			}});
		
		for(int i = 0;i < hiddenboxList.size();i++){
			BTCElement e = hiddenboxList.get(i);
			btcDrawLable = e.getBTCDrawLable();
			btcDrawLable.drawLable(null, null);
		}
		
	}
	
	private Boolean checkrequired(String widgetValue) {
		 if(widgetValue.length() < 1) {
			isNotNull = false;
			return false;
		}else{
			 //输入域不能为空
			isNotNull = true;
			return true;
		}
		
	
	}
	
	

	/**
	 * 
	 * @param checkrule
	 * @param widgetValue
	 * @param useprompt
	 * @param prompt
	 */
	private boolean queryCheckRules(String Key,String checkrule,String widgetValue,String useprompt,String prompt){
		if(StringUtil.isNullOrEmpty(checkrule))
			return true;
		if(checkrule.contains("maxlength")){//最大长度验证maxlength
			if(!checkrequireds(widgetValue, 2,checkrule)){
				if(!"".equals(key)&&BTCCMWApplication.flowFileLangMap.containsKey(Key)){
					String tip = BTCCMWApplication.flowFileLangMap.get(Key).toString();
					if(tip.contains(":")){
						tip = tip.replace(":", "");
					}else if( tip.contains("：")){
						tip = tip.replace("：", "");
					}
					BaseDroidApp.getInstanse().showInfoMessageDialog(tip+"长度不能超过"+maxLength+"个字符");
				}else{
					BaseDroidApp.getInstanse().showInfoMessageDialog("长度不能超过"+maxLength+"个字符");
				}
			
				return false;
			}
		}
		
		if(checkrule!=null && checkrule.contains("minlength")){//最小长度验证minlength
			if(!checkrequireds(widgetValue, 3,checkrule)){
				if(!"".equals(key)&&BTCCMWApplication.flowFileLangMap.containsKey(Key)){
					String tip = BTCCMWApplication.flowFileLangMap.get(Key).toString();
					if(tip.contains(":")){
						tip = tip.replace(":", "");
					}else if( tip.contains("：")){
						tip = tip.replace("：", "");
					}
					BaseDroidApp.getInstanse().showInfoMessageDialog(tip+"长度不能小于"+minlength+"个字符");
				}else{
					BaseDroidApp.getInstanse().showInfoMessageDialog("长度不能小于"+minlength+"个字符");
				}
				return false;
//				
			}
		}
		
		
		if(checkrule!=null && checkrule.contains("rangelength")){//长度必须在5到15个字符内
			if(!checkrequireds(widgetValue,4,checkrule)){
				if(!"".equals(key)&&BTCCMWApplication.flowFileLangMap.containsKey(Key)){
					String tip = BTCCMWApplication.flowFileLangMap.get(Key).toString();
					if(tip.contains(":")){
						tip = tip.replace(":", "");
					}else if( tip.contains("：")){
						tip = tip.replace("：", "");
					}
					BaseDroidApp.getInstanse().showInfoMessageDialog(tip+"长度必须在"+min+"-"+max+"之间");
				}else{
					BaseDroidApp.getInstanse().showInfoMessageDialog("长度必须在"+min+"-"+max+"之间");
				}
				return false;				
			}
		}
		
		if(checkrule!=null && checkrule.contains("nonlicetchar")&&checkrule.contains("true")){//不允许输入特殊字符
//			[]^$\~@#%&<>{}:'"
//			String patterns = "^[a-zA-Z0-9\u4e00-\u9fa5_]{0,60}$";
//			String patterns = "[]^$\\~@#%&<>{}:'\"";
			String patternList [] = {"[]","^","$","\\","~","@","#","%","&","<",">","{","}",":","'","\""};
			for(int i = 0 ;i<patternList.length;i++){
				if(widgetValue.contains(patternList[i])){
		        	isnonlicetchar = false;
		        	if(!"".equals(Key)&&BTCCMWApplication.flowFileLangMap.containsKey(Key)){
		        		String tip = BTCCMWApplication.flowFileLangMap.get(Key).toString();
						if(tip.contains(":")){
							tip = tip.replace(":", "");
						}else if( tip.contains("：")){
							tip = tip.replace("：", "");
						}
						BaseDroidApp.getInstanse().showInfoMessageDialog(tip+"请输入正确的值");
					}else{
						BaseDroidApp.getInstanse().showInfoMessageDialog("请输入正确的值");
					}
		          break;
				}else{
					isnonlicetchar = true;
				}
			}
			if(!isnonlicetchar){
				return false;
			}
			//			Pattern pattern = Pattern.compile(patterns);  
//	        Matcher matcher = pattern.matcher(widgetValue);  
//	        if(!matcher.matches()){  
//	        	isnonlicetchar = false;
//	        	if(!"".equals(Key)&&BTCCMWApplication.flowFileLangMap.containsKey(Key)){
//	        		String tip = BTCCMWApplication.flowFileLangMap.get(Key).toString();
//					if(tip.contains(":")){
//						tip = tip.replace(":", "");
//					}else if( tip.contains("：")){
//						tip = tip.replace("：", "");
//					}
//					BaseDroidApp.getInstanse().showInfoMessageDialog(tip+"请输入正确的值");
//				}else{
//					BaseDroidApp.getInstanse().showInfoMessageDialog("请输入正确的值");
//				}
//	            return false;
//	        }else{
//	        	isnonlicetchar = true;
//	        }
		}
		
		if(checkrule!=null && checkrule.contains("pattern")){
//			{required:true,pattern:/(?!^([0,]*)(\.0{1,2})?$)^((\d{1,3}(,\d{3}){0,3})|(\d(,\d{3}){4})|(\d{1,13}))(\.\d{1,2})?$/,amountformat:'001'}
//			String regss ="(?!^([0,]*)(\\.0{1,2})?$)^((\\d{1,3}(,\\d{3}){0,3})|(\\d(,\\d{3}){4})|(\\d{1,13}))(\\.\\d{1,2})?$";
			String rule = "";
			checkrule.indexOf("pattern:/");
			rule = checkrule.substring(checkrule.indexOf("pattern:/")+9);
			rule = rule.substring(0,rule.indexOf("/"));
			Pattern pattern = Pattern.compile(rule);  
	        Matcher matcher = pattern.matcher(widgetValue);  
	        if(!matcher.matches()){  
	        	isRightPatter = false;
	        	if(!"".equals(Key)&&BTCCMWApplication.flowFileLangMap.containsKey(Key)){
	        		String tip = BTCCMWApplication.flowFileLangMap.get(Key).toString();
					if(tip.contains(":")){
						tip = tip.replace(":", "");
					}else if( tip.contains("：")){
						tip = tip.replace("：", "");
					}
					BaseDroidApp.getInstanse().showInfoMessageDialog(tip+"请输入正确的值");
				}else{
					BaseDroidApp.getInstanse().showInfoMessageDialog("请输入正确的值");
				}
	            return false;
	        }else{
	        	isRightPatter = true;
	        } 
		}
		
		if(checkrule!=null && checkrule.contains("datecompare")){
//		"{required:true,dateformat:true,datecompare:
//			['startDate','limitDate',12,-12]}"
			String rule = "";
			rule = checkrule.substring(checkrule.indexOf("datecompare:[")+13);
			rule = rule.substring(0,rule.indexOf("]"));
			String rules [] = rule.split(",");
			String limitDate = "";
//			isdatecompare
			if(rule.contains("limitDate")&&!rule.contains("endDate")){
				String startDate = rules[0].replace("'", "");
				limitDate = rules[1].replace("'", "");
				int startNum = Integer.parseInt(rules[2]);
				int limitNum = Integer.parseInt(rules[3]);
				String Max = "";
				String min = "";
				//还要获取limitDate的值。
				if(cmwApplication.hiddenboxmap.containsKey(limitDate)){
					limitDate = (String) cmwApplication.hiddenboxmap.get(limitDate);
				}else if(BTCCMWApplication.flowFileLangMap.containsKey(limitDate)){
					limitDate = (String) BTCCMWApplication.flowFileLangMap.get(limitDate);
				}
			if( limitDate ==null ||"limitDate".equals(limitDate)){
//				2016/01/10
				if(startNum > 0){
					startNum = -startNum;
				}
				startDate = getValues(startDate);
				String endDate = getValues("endDate");
				if(!"".equals(startDate) && startDate != null && !"".equals(endDate) && endDate != null){
					if(queryCheckDate(startDate,endDate ,startNum)){
						isdatecompare = true;
					}else{
						isdatecompare = false;
						return false;
					}
				}
			}else{
//				end = datecompare(start  lim  a  b)
//						if(b < 0){
//						  min = lim + b
//						  max = lim
//						}else if(b > 0){
//						  max = lim + b
//						  min = lim
//						}
//						end - start <= a 
//						min <= end <= max 
//						min <= start <= max
//						还有做一下兼容，如果lim数据获取不到，不再算max，min
//						只比较 end - start <= a
//				还要取endDate的日期
				String endDate = getValues("endDate");
				startDate = getValue("startDate");
				if(limitNum < 0){
					min = QueryDateUtils.getAnyMonth(limitDate, limitNum);
					Max = limitDate;
				}else if(limitNum > 0){
					Max = QueryDateUtils.getAnyMonth(limitDate, limitNum);
					min = limitDate;
				}
				if(!"".equals(startDate) && startDate != null &&!"".equals(endDate) && endDate != null){
					if(queryCheckDate(min, endDate, 0)
							&& queryCheckDate(endDate, Max, 0)
							&& queryCheckDate(min, startDate, 0)
							&& queryCheckDate(startDate, Max, 0)
							&& queryCheckDate(startDate, endDate, startNum)){
						isdatecompare = true;
					}else{
						isdatecompare = false;
						return false;
					}
					
				}
				
				queryCheckDate(endDate, Max, startNum);
				
				
			}
				
			}else if(rule.contains("endDate") ){
				String startDate = rules[0].replace("'", "");
				String endDate = rules[1].replace("'", "");
				int num = Integer.parseInt(rules[2]);
//				2016/01/10
				if(num > 0){
					num = -num;
				}
			
				if(queryCheckDate(getValues(startDate),getValues(endDate) ,num)){
					isdatecompare = true;
				}else{
					isdatecompare = false;
					return false;
				}
			}
			
		}
		return true;
	}
	
	public boolean queryCheckDate(String startDate,String endDate,int num){
		
		// 开始日期在结束日期之前
		if (!QueryDateUtils.compareDate(startDate, endDate)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(context.getString(R.string.acc_query_errordate));
				return false;
			}
		
		// 起始日期与结束日期最大间隔为num自然月
		if(num !=0){
			if (!QueryDateUtils.compareDateAny(startDate, endDate,num)) {
				if(num < 0){
					num = - num;
				}
				BaseDroidApp.getInstanse().showInfoMessageDialog("起始日期与结束日期最大间隔应为"+num+"自然月");
				return false;
			}
		}
		return true;
	}
	public String getValues(String dateName){
		Map<String, Integer> widgetIds = activityManager.getWidgetIds();
		Iterator widgetId_iterator = widgetIds.entrySet().iterator();
		String widgetValue ="";
		while (widgetId_iterator.hasNext()){
			Map.Entry entry = (Map.Entry) widgetId_iterator.next();
			View view = null;// 迭代使用的临时控件
			view = activityManager.currentActivity().findViewById((Integer) entry.getValue());
			if (view instanceof EditText){
				EditText editText = (EditText) view;
				if(dateName!=null && dateName.equals(entry.getKey())){
					 widgetValue= editText.getText().toString();
					 return widgetValue;
				}
			}
		}
		 return widgetValue;
	}
	
	/**
	 * 1,输入域不能为空;2最大长度验证maxlength;3minlength
	 * 4,rangelength 长度必须在5到15个字符内
	 * @param widgetValue
	 * @return
	 */
	ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
	private int  maxLength ;
	private int minlength;
	int min ;
	int max;
	boolean isNotNull = true;
	boolean isRightMaxlength= true;
	boolean isRightMinlength = true;
	boolean isRightRangelength = true;
	boolean isRightPatter = true;
	boolean isnonlicetchar = true;
	boolean isdatecompare = true;
	boolean isCheckeds = true;
	private Boolean checkrequireds(String widgetValue,int key,String checkrule) {
		checkrule = checkrule.substring(checkrule.indexOf("{")+1, checkrule.length()-1);
		switch (key) {
		case 2://2最大长度验证maxlength
			//{required:true,maxlength:20}
			if(checkrule.contains(",")){
				String total[] = checkrule.split(",");
				for(int i=0 ;i<total.length;i++){
					if(total[i].contains("maxlength")){
						String maxlenth[]= total[i].split(":");
						maxLength = Integer.parseInt(maxlenth[1]);
						if(widgetValue.length()<=Integer.parseInt(maxlenth[1])){
							isRightMaxlength= true;
							return true;
						}else{
							isRightMaxlength = false;
							return false;
						}
					}
				}
			}else{
				String maxlenth[]= checkrule.split(":");
				maxLength = Integer.parseInt(maxlenth[1]);
				if(widgetValue.length()<=Integer.parseInt(maxlenth[1])){
					isRightMaxlength= true;
					return true;
				}else{
					isRightMaxlength= false;
					return false;
				}
			}
			
			break;
		case 3://3minlength 长度不能小于5个字符
			if(checkrule.contains(",")){
				String total[] = checkrule.split(",");
				for(int i=0 ;i<total.length;i++){
					if(total[i].contains("minlength")){
						String maxlenth[]= total[i].split(":");
						minlength = Integer.parseInt(maxlenth[1]);
						if(widgetValue.length()<Integer.parseInt(maxlenth[1])){
							isRightMinlength = false;
							return false;
						}else{
							isRightMinlength = true;
							return true;
						}
					}
				}
			}else{
				String minlenth[]= checkrule.split(":");
				minlength = Integer.parseInt(minlenth[1]);
				if(widgetValue.length()<Integer.parseInt(minlenth[1])){
					isRightMinlength = false;
					return false;
				}else{
					isRightMinlength = true;
					return true;
				}
			}
			break;
		case 4://rangelength 长度必须在5到15个字符内
//			{ rangelength:[5,15],maxlength:20 }
			if(checkrule.contains(",")){
					if(checkrule.contains("rangelength")){
						String totalleng = checkrule.substring(checkrule.indexOf("[")+1, checkrule.indexOf("]"));
						String totalranleng[] = totalleng.split(",");
						min= Integer.parseInt(totalranleng[0]);
						 max= Integer.parseInt(totalranleng[1]);
						if(widgetValue.length()>= min && widgetValue.length() <= max){
							isRightRangelength = true;
							return true;
						}else{
							isRightRangelength = false;
							return false;
						}
					}
			}else{
				String totalleng = checkrule.substring(checkrule.indexOf("[")+1, checkrule.indexOf("]"));
				String totalranleng[] = totalleng.split(",");
				min= Integer.parseInt(totalranleng[0]);
				 max= Integer.parseInt(totalranleng[1]);
				if(widgetValue.length()>=min && widgetValue.length()<= max){
					isRightRangelength = true;
					return true;
				}else{
					isRightRangelength = false;
					return false;
				}
			}
			
			break;
		case 5:
//{  nonlicetchar:true,rangelength:[5,15],maxlength:20 }
//			RegexpBean name = new RegexpBean("",widgetValue,"safetypeopleservice");
//			lists.add(name);
//			return RegexpUtils.regexpData(lists);
//		String 	 pattern="^[^\\[\\]^$~@#%&amp;&lt;>{}&apos;&quot;\\r\\n＜＞＄～＃％＆]{1,60}$";
		String patterns = "^[a-zA-Z0-9\u4e00-\u9fa5_]{0,60}$";
		case 6://amountformat  以币种进行金额格式并验证 001:人民币元
			//"{required:true,pattern:/(?!^([0,]*)(\.0{1,2})?$)^((\d{1,3}(,\d{3}){0,3})|(\d(,\d{3}){4})|(\d{1,13}))(\.\d{1,2})?$/,amountformat:'001'}"
			break;
		case 7://pattern:
			
			
			break;
		default:
			break;
		}
		return false;
	}
	
	
	private boolean CheckData(Iterator list){
		while (list.hasNext()) {
			Map.Entry entry = (Map.Entry) list.next();
			View view = null;// 迭代使用的临时控件

			view = activityManager.currentActivity().findViewById(
					(Integer) entry.getValue());
			if(view instanceof IInputCheck == false){
				continue;
			}
			if(((IInputCheck)view).Check(null) == false){
				return false;
			}
		}
		return true;
	}
}
