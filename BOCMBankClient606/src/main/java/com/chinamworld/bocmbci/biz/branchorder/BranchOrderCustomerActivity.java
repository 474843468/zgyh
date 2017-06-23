package com.chinamworld.bocmbci.biz.branchorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.MyJSON;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Order;
import com.chinamworld.bocmbci.biz.branchorder.unvip.BranchOrderUnvipInputInfoActivity;
import com.chinamworld.bocmbci.biz.branchorder.vip.BranchOrderVipInputInfoActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.mapabc.bc.MyActivityManager;

/**
 * 用户信息输入页面
 * @author panwe
 *
 */
public class BranchOrderCustomerActivity extends BranchOrderBaseActivity implements OnCheckedChangeListener{
	
	private Button btnnext, btnlast;
	/** 网点名称, 网点地址 , 联系电话 */
	private TextView orderName, orderAdress, orderMobile;
	private RadioButton raBtn1,raBtn2;
	
	/** 客户名称  */
	private EditText customerName;
	/** 预约凭证类型  */
	private Spinner customerIDtype;
	/** 预约证件号码  */
	private EditText customerIDnum;
	/** 预约手机号码  */
	private EditText customerPhone;
	
	private List<String> cardTypeList;
	private String cardType;
	private boolean fromMain;
	/** 网点信息*/
	private Map<String, Object> orderInfo;
	
	/**设置一个boolean,用来判断今天是否是用户的生日*/
	public static boolean isBirthday;
	 /**中行客户isBirthday*/
	 String isBankVip;
	 /**用于存储处理过的系统月,日*/
	 String isSystemTime;
	 /**用于存储非中行用户月,日*/
	 String isNoBankVipTime,isSubNoBankVipTime;
	 
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.order_customerinput);
		setTitle(R.string.order_main_title);
		setupView();
		setExtroData();
	}
	
	private void setIsBirthday() {
		if(!StringUtil.isNull(dateTime)){
			 String[] strtime=	dateTime.split(" ");
			 String[] str= strtime[0].split("/");
			 isSystemTime=str[1]+str[2];
		}
		System.out.println("isSystemTime=="+isSystemTime);
		//获取非中行用户的身份证字符串
		//判断条件为,非中行客户,且spinner的位置为0
		if(BranchOrderDataCenter.customerType == BranchOrderDataCenter.NON_CHINA_BANK_CUSTOMER){
		//	int itemPosition=customerIDtype.getSelectedItemPosition();
		//	String idType=cardTypeList.get(itemPosition);
		//	if("身份证".equals(idType)||"临时身份证".equals(idType)){
		if(customerIDtype.getSelectedItemPosition()==0 ||customerIDtype.getSelectedItemPosition()==1){
				isNoBankVipTime=customerIDnum.getText().toString();
				System.out.println("isNoBankVipTime==="+isNoBankVipTime);
				if(isNoBankVipTime.length()==18){
					isSubNoBankVipTime=isNoBankVipTime.substring(10, 14);
				}else {
					isSubNoBankVipTime=	isNoBankVipTime.substring(8, 12);
				}
				if(isSystemTime.equals(isSubNoBankVipTime)){
					isBirthday=true;
				}else{
					isBirthday=false;
					
				}
			}
		}
	}
	
	public void setExtroData(){
		fromMain = BranchOrderDataCenter.isFromMain;
		if(!fromMain || BranchOrderDataCenter.isFromMyAround){
//			mLeftButton.setText("网点信息");
			System.out.println("========"+ getIntent().getExtras().getString(
					Comm.MAPQUEUEINFO_KEY));
			String orderItem = getIntent().getExtras().getString(
					Comm.MAPQUEUEINFO_KEY);
			LogGloble.d("BranchOrderCustomerActivity", orderItem);
			Map<String, Object> orderItemMap = (Map<String, Object>) MyJSON
					.parse(orderItem);
			LogGloble.d("BranchOrderCustomerActivity", "MyJSON"+orderItem);
			BranchOrderDataCenter.getInstance().setOrderListItem(orderItemMap);
		}
		setDataForView();
	}
	
	private void setupView(){
		btnlast = (Button)findViewById(R.id.finc_btn1);
		btnnext = (Button)findViewById(R.id.finc_btn2);
		btnnext.setOnClickListener(this);btnlast.setOnClickListener(this);
		raBtn1 = (RadioButton) findViewById(R.id.rabtn1);
		raBtn2 = (RadioButton) findViewById(R.id.rabtn2);
		orderName = (TextView) findViewById(R.id.orderName);
		orderAdress = (TextView) findViewById(R.id.orderAdress);
		orderMobile = (TextView) findViewById(R.id.orderMobile);
		customerName = (EditText) findViewById(R.id.customerName);
		customerIDtype = (Spinner) findViewById(R.id.customerIDtype);
		customerIDnum = (EditText) findViewById(R.id.customerIDnum);
		customerPhone = (EditText) findViewById(R.id.customerPhone);
		customerIDtype.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(!StringUtil.isNullOrEmpty(cardTypeList)){
					if(!BranchOrderUtils.isStrEquals(cardType, cardTypeList.get(arg2))){
						customerIDnum.setText("");
					}
					cardType = cardTypeList.get(arg2);
				}
				if(BranchOrderUtils.getKeyByValue(BranchOrderDataCenter.cardType, cardType).equals("01")
						|| BranchOrderUtils.getKeyByValue(BranchOrderDataCenter.cardType, cardType).equals("52")){
					customerIDnum.setFilters(new InputFilter[] {
						new InputFilter.LengthFilter(18)
					});
//					customerIDnum.setKeyListener(new NumberKeyListener() {
//						
//						@Override
//						public int getInputType() {
//							return android.text.InputType.TYPE_CLASS_TEXT;
//						}
//						
//						@Override
//						protected char[] getAcceptedChars() {
//							return new char[] { '1', '2', '3', '4', '5', '6', '7',
//									'8', '9', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
//									'H', 'I', 'G' ,'K', 'L', 'M', 'N', 'O', 'P', 'Q',
//									'R', 'S', 'T','U' ,'V', 'W', 'X', 'Y', 'Z'};
//						}
//					});
				}else if(BranchOrderUtils.getKeyByValue(BranchOrderDataCenter.cardType, cardType).equals("02")){
					//借记卡
					customerIDnum.setFilters(new InputFilter[] {
							new InputFilter.LengthFilter(19)
						});
				}else if(BranchOrderUtils.getKeyByValue(BranchOrderDataCenter.cardType, cardType).equals("03")){
					//信用卡
					customerIDnum.setFilters(new InputFilter[] {
							new InputFilter.LengthFilter(16)
						});
				}else{
					customerIDnum.setFilters(new InputFilter[] {
							new InputFilter.LengthFilter(180)
						});
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		BranchOrderUtils.setOnShowAllTextListener(this, orderName);
		((RadioGroup)findViewById(R.id.radiogroup)).setOnCheckedChangeListener(this);
		onCheckedChanged((RadioGroup)findViewById(R.id.radiogroup), R.id.rabtn1);
	}
	
	private void setDataForView(){
		orderInfo = BranchOrderDataCenter.getInstance().getOrderListItem();
		if(!StringUtil.isNullOrEmpty(orderInfo)){
			setText((String)orderInfo.get(Order.ORDERORGNAME), orderName);
			setText((String)orderInfo.get(Order.ORDERORGADRESS), orderAdress);
			setText((String)orderInfo.get(Order.ORDERORGPHONE), orderMobile);
		}
		cardTypeList = BranchOrderUtils.initSpinnerView(this, 
				customerIDtype, BranchOrderDataCenter.cardType);
	}
	
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_btn1:
			finish();
			break;
		case  R.id.finc_btn2:
			if(saveCustomerInfo()){
				//中行客户   请求判断是否为X级以上  以此确认是否可以进行特殊业务预约   非中行客户只进行远程取号
				if(raBtn1.isChecked())
					BranchOrderDataCenter.customerType = BranchOrderDataCenter.CHINA_BANK_CUSTOMER;
				else
					BranchOrderDataCenter.customerType = BranchOrderDataCenter.NON_CHINA_BANK_CUSTOMER;
				
				BaseHttpEngine.showProgressDialog();
				requestSystemDateTimeForLoginPre();
			}
			break;

		default:
			break;
		}
	}
	
	public void requestSystemDateTimeForLoginPreCallBack(Object resultObj) {
		super.requestSystemDateTimeForLoginPreCallBack(resultObj);
		BranchOrderDataCenter.getInstance().setCustomerInfo(
				Order.DATETIME, dateTime);
		requestBranchOrderDetail();
	};
	
	private boolean saveCustomerInfo(){
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		if(StringUtil.isNull(customerName.getText().toString())){
			BaseDroidApp.getInstanse().showInfoMessageDialog("请输入" + getNoColonStr(getString(R.string.order_customer_name)));
			return false;
		}
		if(BranchOrderUtils.getKeyByValue(BranchOrderDataCenter.cardType, cardType).equals("01")
				|| BranchOrderUtils.getKeyByValue(BranchOrderDataCenter.cardType, cardType).equals("52")){
			RegexpBean regID = new RegexpBean(getString(R.string.blpt_tip_idcode), 
					customerIDnum.getText().toString(),"safetyIdentityNumber");
			lists.add(regID);
		}else if(BranchOrderUtils.getKeyByValue(BranchOrderDataCenter.cardType, cardType).equals("02")){
			RegexpBean regID = new RegexpBean("借记卡号", customerIDnum.getText()
					.toString(), "branchdebitCard");
			lists.add(regID);
		}else if(BranchOrderUtils.getKeyByValue(BranchOrderDataCenter.cardType, cardType).equals("03")){
			RegexpBean regID = new RegexpBean("信用卡号", customerIDnum.getText()
					.toString(), "creditCard");
			lists.add(regID);
		}else{
			if(StringUtil.isNull(customerIDnum.getText().toString())){
				BaseDroidApp.getInstanse().showInfoMessageDialog("请输入" + 
						getNoColonStr(((TextView)findViewById(R.id.customerIDnumTv)).getText().toString()));
				return false;
			}
		}
		RegexpBean regPhone = new RegexpBean(getNoColonStr(getString(R.string.order_customer_phone)), 
				customerPhone.getText().toString(),"shoujiH_01_11");
		lists.add(regPhone);
		if (!RegexpUtils.regexpDate(lists)) {// 校验不通过
			return false;
		}
		
		BranchOrderDataCenter.getInstance().setCustomerInfo(
				Order.CUSTOMERNAME, customerName.getText().toString());
		BranchOrderDataCenter.getInstance().setCustomerInfo(
				Order.ORDERCERTTYPE, 
				BranchOrderUtils.getKeyByValue(BranchOrderDataCenter.cardType, cardType));
		BranchOrderDataCenter.getInstance().setCustomerInfo(
				Order.ORDERCERTNO, customerIDnum.getText().toString());
		BranchOrderDataCenter.getInstance().setCustomerInfo(
				Order.ORDERMOBILE, customerPhone.getText().toString());
		return true;
	}
	
	/**
	 * 请求网点详情
	 * @param orderCode
	 */
	private void requestBranchOrderDetail() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		Map<String, Object> map = BranchOrderDataCenter.getInstance().getOrderListItem();
		biiRequestBody.setMethod(Order.METHOD_BRANCHORDER_DETAIL);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Order.ORDERORGCODE, (String)map.get(Order.ORDERORGCODE));
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "branchOrderDetailCallBack");
	}

	@SuppressWarnings("unchecked")
	public void branchOrderDetailCallBack(Object resultObj) {
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		BranchOrderDataCenter.getInstance().setOrderDetail(map);
		if(BranchOrderDataCenter.customerType == BranchOrderDataCenter.CHINA_BANK_CUSTOMER){
			//中行客户继续请求客户等级  X级以上执行预约流程，X以下同非中行客户一致执行远程取号流程
			requestLoginPreConversationId();
		}else{
			setIsBirthday();
			//非中行  执行远程取号
			BaseHttpEngine.dissMissProgressDialog();
			if(!isHaveData()){
				BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.order_no_book_service));
				return;
			}
			startActivityForResult(new Intent(this, BranchOrderUnvipInputInfoActivity.class), 
					BranchOrderDataCenter.UN_VIP);
		}
	}
	
	@Override
	public void requestLoginPreConversationIdCallBack(Object resultObj) {
		super.requestLoginPreConversationIdCallBack(resultObj);
		requestPsnOrderQueryVip();
	}
	
	private void requestPsnOrderQueryVip(){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOGIN_PRECONVERSATIONID).toString());
		biiRequestBody.setMethod(Order.METHOD_BRANCHORDER_QUERY_VIP);
		Map<String, String> map = BranchOrderDataCenter.getInstance().getCustomerInfo();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Order.ORDERCERTTYPE,map.get(Order.ORDERCERTTYPE));
		params.put(Order.ORDERCERTNO,map.get(Order.ORDERCERTNO));
		params.put(Order.CUSTOMERNAME,map.get(Order.CUSTOMERNAME));
		params.put(Order.BRANCHAPTSTARTLEVEL, BranchOrderDataCenter.
				getInstance().getOrderDetail().get(Order.CUSTLEVELTYPE));
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnOrderQueryVipCallBack");
	}
	
	public void requestPsnOrderQueryVipCallBack(Object resultObj){
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		boolean appointmentFlag = StringUtil.parseStrToBoolean((String)map.
				get(Order.APPOINTMENTFLAG));
		isBankVip=(String) map.get(Order.ISBIRTHDAY);
		System.out.println("isbankVip===="+isBankVip);
		if(("1").equals(isBankVip)){
			isBirthday=true;
		}else{
			isBirthday=false;
		}
		if(appointmentFlag){
			//X级以上，vip
			BaseHttpEngine.dissMissProgressDialog();
			BranchOrderDataCenter.isXGrade = true;
			startActivityForResult(new Intent(this, BranchOrderVipInputInfoActivity.class), 
					BranchOrderDataCenter.UN_VIP);
		}else{
			//X级以下  执行远程取号
			if(!isHaveData()){
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.order_no_book_service));
				return;
			}
			BaseHttpEngine.dissMissProgressDialog();
			BranchOrderDataCenter.isXGrade = false;
			startActivityForResult(new Intent(this, BranchOrderUnvipInputInfoActivity.class), 
					BranchOrderDataCenter.UN_VIP);
		}
	}
	
	private boolean isHaveData(){
		if(StringUtil.isNullOrEmpty(BranchOrderDataCenter.getInstance().getOrderDetail()))
			return false;
		List<Map<String, Object>> normalBusinessList = (List<Map<String, Object>>)
				BranchOrderDataCenter.getInstance().getOrderDetail().get(Order.NORMALBUSINESSLIST);
		if(StringUtil.isNullOrEmpty(normalBusinessList))
			return false;
		Map<String, Object> serviceIdName = BranchOrderUtils.getMapFromMapListByDate(normalBusinessList, 
				Order.BSID, Order.BSNAME, QueryDateUtils.getcurrentDate(BranchOrderDataCenter.getInstance().
						getCustomerInfo(Order.DATETIME)));
		if(StringUtil.isNullOrEmpty(serviceIdName)){
			return false;
		}
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case BranchOrderDataCenter.VIP:
		case BranchOrderDataCenter.UN_VIP:
			switch (resultCode) {
			case RESULT_OK:
				if(fromMain && !BranchOrderDataCenter.isFromMyAround){
					setResult(RESULT_OK);
					finish();
				}else if(!fromMain){
					MyActivityManager.newInstance().finishActivity(MyActivityManager.ACTIVIYT_TYPE_POIINFO);
					finish();
				}else{
					//从我的周边进入地图，且做完一个流程
					MyActivityManager.newInstance().finishActivity(MyActivityManager.ACTIVIYT_TYPE_POIINFO);
					MyActivityManager.newInstance().finishActivity(MyActivityManager.ACTIVIYT_TYPE_MAP);
					if(BranchOrderMainActivity.instance != null)
						BranchOrderMainActivity.instance.finish();
					finish();
				}
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rabtn1:
			BranchOrderDataCenter.customerType = BranchOrderDataCenter.CHINA_BANK_CUSTOMER;
			((TextView) findViewById(R.id.customerIDtypeTv))
					.setText(getString(R.string.order_customer_idtype));
			((TextView) findViewById(R.id.customerIDnumTv))
					.setText(getString(R.string.order_customer_idnumber));
			cardTypeList = BranchOrderUtils.initSpinnerView(this,
					customerIDtype, BranchOrderDataCenter.cardType);
			break;
		case R.id.rabtn2:
			BranchOrderDataCenter.customerType = BranchOrderDataCenter.NON_CHINA_BANK_CUSTOMER;
			((TextView) findViewById(R.id.customerIDtypeTv))
					.setText(getString(R.string.bocinvt_eva_identitytype));
			((TextView) findViewById(R.id.customerIDnumTv))
					.setText(getString(R.string.bocinvt_eva_identityactnum));
			cardTypeList = BranchOrderUtils.initSpinnerView(this,
					customerIDtype, BranchOrderDataCenter.cardTypeFzh);
			break;
		default:
			break;
		}
	}
}
