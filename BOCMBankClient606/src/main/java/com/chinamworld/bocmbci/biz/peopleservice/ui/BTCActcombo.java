package com.chinamworld.bocmbci.biz.peopleservice.ui;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLableAttribute;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IHttpCallBack;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;


public class BTCActcombo extends BTCDrawLable {
	/** 界面栈对象 */
	private BTCActivityManager activityManager;// 界面栈对象
	/** 全局变量对象 */
	private BTCCMWApplication cmwApplication;
	private BTCUiActivity btcuiActivity;

	public List<String> spinnerList = new ArrayList<String>();
//	int index=0;
	
	private boolean isRequestData = false;
	/** 账户请求数据列表 */
	List<String> accTypeList = new ArrayList<String>();
	private Button button =null;
	
	/** 存储标签属性 */
	Map<String, Object> attrMap = new HashMap<String,Object>();
	public static ArrayList<String> accNumberList=new ArrayList<String>();//by dl 存放未加密的卡号
	String accountId ="";
	ArrayList <String> accountIdList = new ArrayList<String>();
	boolean isselected = false;
	 /* 
	 * @param context
	 */
	public BTCActcombo(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Actcombo;
		btcuiActivity = BTCUiActivity.getInstance();
		activityManager = BTCActivityManager.getInstance();
		cmwApplication = BTCUiActivity.getApp();
	}
	List<String> accoNum = new ArrayList<String>();
	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		
		Map<String, String> params = btcElement.getParams();
		
		List<BTCElement> childElements = btcElement.getChildElements();
		String name = btcElement.getElementName();
		accountId = "";
		boolean flag =true;
		//Map<String, Object> msg = new HashMap<String, Object>();
		if(params == null)
			return false;
		
		//wuhan 
		Spinner spinner = new Spinner(context);
		Iterator it = params.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (BTCLableAttribute.NAME.equals(entry.getKey())) {
				// 为输入框设置id
				int hashcode = ((String) entry.getValue()).hashCode();
				hashcode = hashcode > 0 ? hashcode : -hashcode;
				spinner.setId(hashcode);
//				if(cmwApplication.getVar(String.valueOf(hashcode))!=null){
//					 index=Integer.valueOf( cmwApplication.getVar(String.valueOf(hashcode)));
//				}
				activityManager.putWidgetId((String) entry.getValue(),
						hashcode);
			} 

		}
		
		//是否查询余额
		button = new Button(context);
		button.setTextSize(15);
		button.setTextColor(context.getResources().getColor(R.color.black));
		if(params.containsKey("querybal") &&"true".equals(params.get("querybal"))){
			button.setText("账户详情");
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if("".equals(accountId) || !isselected){
						BaseDroidApp.getInstanse().showInfoMessageDialog("请选择列表");
						return;
					}
					//发送账户详情报文
					requestAccountDetaile(accountId, new IHttpCallBack() {
						
						@Override
						public void requestHttpSuccess(Object result) {
							handleAccountDetaileRequest(result);
						}
					});
				}
			});
		}
		
		//wuhan
		if (params.containsKey(BTCLableAttribute.USEPROMPT)) {
			String useprompt = params.get(BTCLableAttribute.USEPROMPT);
			attrMap.put(BTCLableAttribute.USEPROMPT, useprompt);
		}
		
		if (params.containsKey(BTCLableAttribute.PROMPT)) {
			String prompt = params.get(BTCLableAttribute.PROMPT);
			attrMap.put(BTCLableAttribute.PROMPT, prompt);
		}
		if(params.containsKey("querybal")){//是否需要查余额
			Object querybal = params.get("querybal");
			attrMap.put("querybal", querybal);
		}
		
		
	
		if (params.containsKey(BTCLableAttribute.CHECKRULE)) {

			Object checkrule = params.get(BTCLableAttribute.CHECKRULE);
			if(checkrule.toString().contains("required:true")){
				flag = true;
			}
			activityManager.setWidgetcheckrule(spinner.getId(), checkrule);
		}
		
		String acttype ;
		//？？？？？？
		if(params.containsKey("acttype")){//封装的数据list 将放入spinner中
			acttype = params.get("acttype");
//				List<String> type = new ArrayList<String>();
			
			if (acttype.contains(",")) {
				String total[] = acttype.split(",");
				//封装上送的accountType数据项
				for(int i = 0 ;i<total.length;i++){
					accTypeList.add(total[i]);
				}
			}else{//只含有一项。
				accTypeList.add(acttype);
			}
		}else {
			accTypeList.add("119");
		}
		if(params.containsKey("querybal")&&"true".equals(params.get("querybal"))){
			initListView(spinner,params,view,button);
		}else{
			initListView(spinner,params,view,null);
		}
		
		return flag;
	}

	private boolean isDefault = false;
	private void initListView(final Spinner spinner,Map<String,String> params,View view,final Button button){
		spinnerList.clear();
		accNumberList.clear();//by dl
		isRequestData = false;
		isDefault = false;
		if(params.containsKey("default")){//默认选中值  --请选择--
			isDefault = true;
			String defaults = params.get("default");
			String sele = params.get("default");
			if(BTCUiActivity.getApp().containKeyFromFlowFileLangMap(sele)){
				spinnerList.add(0,BTCUiActivity.getApp().getFlowFileLangMapByKey(sele).toString());
				accNumberList.add(0,BTCCMWApplication.flowFileLangMap.get(sele).toString());//by dl
			}else{
				spinnerList.add(0,defaults);
				accNumberList.add(0,defaults);//by dl
			}
			attrMap.put("default", defaults);
		}
		attrMap.put("chose", accNumberList);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, spinnerList);
		spinner.setTag(attrMap);
		
		adapter.setDropDownViewResource(R.layout.peopleservice_spinner_item);//by dl 重新定义spinner的item
		spinner.setAdapter(adapter);
		spinner.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				BTCCMWApplication.selfs.dis();
				if((arg1.getAction() != MotionEvent.ACTION_UP)&&!isRequestData){
					return true;
				}
				if(isRequestData == false) {
					requestAccountList(new IHttpCallBack(){
						@Override
						public void requestHttpSuccess(Object result) {
							isRequestData = true;
							spinner.performClick();
						}},accTypeList);
					
					
				}
				return false;
			}
		});
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int arg2, long arg3) {
				/*by dl*/
				if (isDefault) {
					if(arg2 == 0)
					{
						isselected = false;
						return;
					}
					
					TextView tv=(TextView) view;
					String string = spinnerList.get(arg2);
					String ssString=get2_3_2(string);
					tv.setText(ssString);
					isselected = true;
					accountId = accountIdList.get(arg2-1);
				}
				else
				{
					TextView tv=(TextView) view;
					String string = spinnerList.get(arg2);
					String ssString=get2_3_2(string);
					tv.setText(ssString);
					isselected = true;
					accountId = accountIdList.get(arg2);
				}
//				if(spinnerList.get(arg2).contains("请选择")){
//					isselected = false;
//				}
				/*by dl end*/
				
//				if(isRequestData == false) {
//					requestAccountList(new IHttpCallBack(){
//						@Override
//						public void requestHttpSuccess(Object result) {
//							isRequestData = true;
////							if(spinnerList.size() > 2){
////								spinner.setSelection(1);
////							}
//						}},accTypeList);
//				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		((ViewGroup) view).addView(spinner, new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, 1));
		if(button!=null){//by dl 增加判断
			((ViewGroup) view).addView(button);
		}

	}
	
	//by dl 选中项信息封装成  **...**的形式
	private String get2_3_2(String str){
		StringBuffer buffer = new StringBuffer(str.subSequence(0, 2));
		buffer.append("...");
			buffer.append(str.subSequence(str.length()-4, str.length()-1));
		return buffer.toString();
	}
	
	/**
	 * 请求用户列表方法封装
	 */
	private void requestAccountList(final IHttpCallBack callback,List<String> type){
		BTCUiActivity.getInstance().callBackQueryAvaliableAccountList(type,  new IHttpCallBack() {
			
			@Override
			public void requestHttpSuccess(Object result) {
				handleAccountListRequest(result);
				if(callback != null)
					callback.requestHttpSuccess(null);
			}
		});
	}
	
	private void handleAccountListRequest(Object result){
		try {
			BaseHttpEngine.dissMissProgressDialog();
			accountIdList.clear();
			BiiResponse biiResponse = (BiiResponse) result;
			List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
			BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
			List<HashMap<String, String>> reee = (List<HashMap<String, String>>) biiResponseBody.getResult();
			BTCCMWApplication.ListAccountMap.clear();
			BTCCMWApplication.ListAccountMap = reee;	
			for(int i = 0 ;i<reee.size();i++){
				Map<String, String> map = new HashMap<String, String>();
				map = reee.get(i);
				
				/*by dl*/
				String accountType = map.get("accountType");
				String strAccountType = LocalData.AccountType.get(accountType);//代号转成卡类型
				String accountNumber = map.get("accountNumber");
				String nickName = map.get("nickName");
				accountId = map.get("accountId");
				accountIdList.add(accountId);
				String forSixForString = StringUtil.getForSixForString(accountNumber);//4 6 4(4******4)卡号格式化
				accNumberList.add(accountNumber);
				spinnerList.add(strAccountType+forSixForString+nickName);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	
	}
	
	/**
	 * 发送账户详情请求
	 */
	private void requestAccountDetaile(String accountId,final IHttpCallBack callback ){
		btcuiActivity.requestAccBankAccountDetail(accountId, callback);
	}
	
	
	private void handleAccountDetaileRequest(Object result){
		BiiHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) result;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		Map<String, Object> callbackmap = (Map<String, Object>) (biiResponseBody.getResult());
		String string = biiResponseBody.getResult().toString();
		org.json.JSONObject jsonObject;
		int int_currencyCode =0,int_cashRemit=0;
		String str_bookBalance =null,str_availableBalance=null;
		try {
			jsonObject = new org.json.JSONObject(string);
			JSONArray jsonArray = jsonObject.getJSONArray("accountDetaiList");
			org.json.JSONObject jsonObject2 = jsonArray.getJSONObject(0);
			int_currencyCode=Integer.parseInt(jsonObject2.getString("currencyCode"));
			int_cashRemit = Integer.parseInt(jsonObject2.getString("cashRemit"));
			str_bookBalance = jsonObject2.getString("bookBalance");
			str_availableBalance = jsonObject2.getString("availableBalance");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		final PopupWindow popupWindow = new PopupWindow(context);
		popupWindow.setAnimationStyle(R.style.popAnimation);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(btcuiActivity.getResources().getDrawable(R.color.transparent_00));
		DisplayMetrics metrics = new DisplayMetrics();
		btcuiActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int widthPixels = metrics.widthPixels;
		int heightPixels = metrics.heightPixels;
		int realWidth=widthPixels*3/4;
		int realHeight=heightPixels*1/2;
		popupWindow.setWidth(realWidth);
		popupWindow.setHeight(realHeight);
		View view = LayoutInflater.from(context).inflate(R.layout.peopleservice_xian_popupwindow, null);
		TextView currencyCode = (TextView) view.findViewById(R.id.currencyCode);//币种
		TextView cashRemit = (TextView) view.findViewById(R.id.cashRemit);//钞/汇
		TextView bookBalance = (TextView) view.findViewById(R.id.bookBalance);//账户余额
		TextView availableBalance = (TextView) view.findViewById(R.id.availableBalance);//可用余额
		String str_currencyCode=null;
		switch (int_currencyCode) {
		case 001:
			str_currencyCode="人民币元";
			break;
		case 014:
			str_currencyCode="美元";
			break;
		case 027:
			str_currencyCode="日元";
			break;

		default:
			str_currencyCode="-";
			break;
		}
		currencyCode.setText(str_currencyCode);
		String str_cashRemit = null;
		switch (int_cashRemit) {
		case 01:
			str_cashRemit="现钞";
			break;
		case 02:
			str_cashRemit="现汇";
			break;

		default:
			str_cashRemit="-";
			break;
		}
		cashRemit.setText(str_cashRemit);
		String parse_str_bookBalance = BTCFormatnumber.parseStringPattern(str_bookBalance, 2);
		String parse_str_availableBalance = BTCFormatnumber.parseStringPattern(str_availableBalance, 2);
		bookBalance.setText(parse_str_bookBalance);
		availableBalance.setText(parse_str_availableBalance);
		TextView close = (TextView) view.findViewById(R.id.close);
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (popupWindow!=null) {
					popupWindow.dismiss();
				}
			}
		});
		popupWindow.setContentView(view);
		popupWindow.showAtLocation(button, Gravity.CENTER, 0, 0);
	}
}
