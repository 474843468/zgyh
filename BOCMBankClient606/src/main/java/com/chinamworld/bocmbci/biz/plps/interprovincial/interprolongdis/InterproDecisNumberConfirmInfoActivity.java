package com.chinamworld.bocmbci.biz.plps.interprovincial.interprolongdis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;
import com.chinamworld.bocmbci.widget.UsbkeyFieldProcess;
/*
 * 跨省异地交通违法罚款  确认信息页面
 * @author zxj
 */
public class InterproDecisNumberConfirmInfoActivity extends PlpsBaseActivity{
	
	/**决定书编号*/
	private String decisionNo;
	private TextView decisionNoTextView;
	/**驾驶证号*/
	private String driverLicenseNo;
	private TextView driverLicenseNoTextView;
	/**当事人*/
	private String party;
	private TextView parytTextView;
	/**处理时间*/
	private String dealTime;
	private TextView dealTimeTextView;
	/**处理机关*/
	private String dealAuthority;
	private TextView dealAuthorityTextView;
	/**处理机关名称*/
	private String dealAuthorityName;
	private TextView dealAuthorityNameTextView;
	/**违法地点*/
	private String illegalPlace;
	private TextView illegalPlaceTextView;
	/**罚款金额*/
	private String fineAmount;
	private TextView fineAmountTextView;
	/**加处罚款*/
	private String additionalAmount;
	private TextView additionalAmountTextView;
	/**合计金额*/
	private String totalAmount;
	private TextView totalAmountTextView;
	/**缴费帐号*/
	private String paymentAccount;
	private TextView paymentAccountTextView;
	/**手机交易码控件*/
	private SipBox smcSipBox;
	/**动态口令控件*/
	private SipBox otpSipBox;
	/**中银E盾*/
	private UsbKeyText usbKeyText;
	/**随机数*/
	private String randaomNumber;
	/**手机验证码是否显示*/
	private Boolean isOtp = false;
	/**动态口令是否显示*/
	private Boolean isSmc = false;
	/**流水号*/
	private String traceNo;
	/**缴费帐号accountId*/
	private String accountId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(R.string.plps_interprov_titlename);
		inflateLayout(R.layout.plps_interprov_dicisnumconfirminfo);
		getIntentData();
		init();
	}
	/**获取上一个页面传过来的数据*/
	private void getIntentData(){
		paymentAccount = getIntent().getStringExtra(Comm.ACCOUNTNUMBER);
		decisionNo = getIntent().getStringExtra(Plps.DECISIONNO);
		driverLicenseNo = getIntent().getStringExtra(Plps.DRIVERLICENSENO);
		party = getIntent().getStringExtra(Plps.PARTY);
		dealTime = getIntent().getStringExtra(Plps.DEALTIME);
		dealAuthority = getIntent().getStringExtra(Plps.DEALAUTHORITY);
		dealAuthorityName = getIntent().getStringExtra(Plps.DEALAUTHORITYNAME);
		illegalPlace = getIntent().getStringExtra(Plps.ILLEGAPLACE);
		fineAmount = getIntent().getStringExtra(Plps.FINEAMOUNT);
		additionalAmount = getIntent().getStringExtra(Plps.ADDITIONALAMOUNT);
		totalAmount = getIntent().getStringExtra(Plps.TOTALAMOUNT);
		randaomNumber = getIntent().getStringExtra(Plps.RANDOMNUMBER);
		accountId = getIntent().getStringExtra(Comm.ACCOUNT_ID);

	}
	/**初始化页面*/
	private void init(){
		decisionNoTextView = (TextView)findViewById(R.id.decision);
		driverLicenseNoTextView = (TextView)findViewById(R.id.driverno);
		parytTextView = (TextView)findViewById(R.id.party);
		dealTimeTextView = (TextView)findViewById(R.id.dealtime);
		dealAuthorityTextView = (TextView)findViewById(R.id.dealauthority);
		dealAuthorityNameTextView = (TextView)findViewById(R.id.dealauthorityname);
		illegalPlaceTextView = (TextView)findViewById(R.id.illegalplace);
		fineAmountTextView = (TextView)findViewById(R.id.the_fines);
		additionalAmountTextView = (TextView)findViewById(R.id.additional);
		totalAmountTextView = (TextView)findViewById(R.id.total_amount);
		paymentAccountTextView = (TextView)findViewById(R.id.paymnetacct);
		
		if(!StringUtil.isNullOrEmpty(decisionNo)){
			decisionNoTextView.setText(decisionNo);
		}
		if(!StringUtil.isNullOrEmpty(driverLicenseNo)){
			driverLicenseNoTextView.setText(driverLicenseNo);
		}
		if(!StringUtil.isNullOrEmpty(party)){
			parytTextView.setText(party);
		}
		if(!StringUtil.isNullOrEmpty(dealTime)){
			dealTimeTextView.setText(dealTime);
		}
		if(!StringUtil.isNullOrEmpty(dealAuthority)){
			dealAuthorityTextView.setText(dealAuthority);
		}
		if(!StringUtil.isNullOrEmpty(dealAuthorityName)){
			dealAuthorityNameTextView.setText(dealAuthorityName);
		}
		if(!StringUtil.isNullOrEmpty(illegalPlace)){
			illegalPlaceTextView.setText(illegalPlace);
		}
		if(!StringUtil.isNullOrEmpty(fineAmount)){
			fineAmountTextView.setText(StringUtil.parseStringPattern(fineAmount, 2));
		}
		if(!StringUtil.isNullOrEmpty(additionalAmount)){
			additionalAmountTextView.setText(StringUtil.parseStringPattern(additionalAmount, 2));
		}
		if(!StringUtil.isNullOrEmpty(totalAmount)){
			totalAmountTextView.setText(StringUtil.parseStringPattern(totalAmount, 2));
		}
		if(!StringUtil.isNullOrEmpty(paymentAccount)){
			paymentAccountTextView.setText(StringUtil.getForSixForString(paymentAccount));
		}
		PlpsUtils.setOnShowAllTextListener(this,  decisionNoTextView,driverLicenseNoTextView, parytTextView,dealTimeTextView,
				dealAuthorityTextView,dealAuthorityNameTextView, illegalPlaceTextView, fineAmountTextView,
				additionalAmountTextView, totalAmountTextView,paymentAccountTextView);
		initSipBox();
	}
	
	/**安全工具初始化*/
	private void initSipBox(){
		Map<String, Object> resultObj = (Map<String, Object>)PlpsDataCenter.getInstance().getInterMapresult();
		traceNo = (String)resultObj.get(Plps.TRACENO);
		usbKeyText = (UsbKeyText)findViewById(R.id.sip_usbkey);
		String commConversationId = (String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		UsbkeyFieldProcess usbkeyFieldProcess = new UsbkeyFieldProcess((List<Map<String,Object>>)resultObj.get(Plps.PLPS_CONFIRM_FACTORLIST), (String)resultObj.get(Plps.PLPS_CONFIRM_PLAINDATA), randaomNumber, commConversationId);
		usbKeyText.InitDate(usbkeyFieldProcess, this);
//		usbKeyText.Init(commConversationId, randaomNumber, resultObj, this);
		isSmc = usbKeyText.getIsSmc();
		isOtp = usbKeyText.getIsOtp();
		initSmcSipBox();
		initOtpSipBox();
	}
	/**初始化手机验证码*/
	private void initSmcSipBox(){
		if(isSmc){
			findViewById(R.id.layout_sms).setVisibility(View.VISIBLE);
			Button btnSendSms = (Button) findViewById(R.id.smsbtn);
			SmsCodeUtils.getInstance().addSmsCodeListner(btnSendSms,smcOnclickListener);
			smcSipBox = (SipBox) findViewById(R.id.sip_sms);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(smcSipBox, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randaomNumber, this);
//			smcSipBox.setCipherType(SystemConfig.CIPHERTYPE);
//			smcSipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			smcSipBox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			smcSipBox.setId(10002);
//			smcSipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			smcSipBox.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			smcSipBox.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			smcSipBox.setSipDelegator(this);
//			smcSipBox.setRandomKey_S(randaomNumber);
		}
	}
	/** 获取手机交易码监听事件 */
	private OnClickListener smcOnclickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			sendSMSCToMobile();
		}
	};
	
	/**初始化动态口令*/
	private void initOtpSipBox(){
		if(isOtp){
			findViewById(R.id.layout_sip).setVisibility(View.VISIBLE);
			otpSipBox = (SipBox) findViewById(R.id.et_cecurity_ps);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(otpSipBox, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randaomNumber, this);
//			otpSipBox.setCipherType(SystemConfig.CIPHERTYPE);
//			otpSipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			otpSipBox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			otpSipBox.setId(10002);
//			otpSipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			otpSipBox.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			otpSipBox.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			otpSipBox.setSipDelegator(this);
//			otpSipBox.setRandomKey_S(randaomNumber);
		}
	}
	
	/**确定按钮*/
	public void nextBtnClick(View v){
		/**安全工具认证*/
		usbKeyText.checkDataUsbKey(otpSipBox, smcSipBox, new IUsbKeyTextSuccess() {
			
			@Override
			public void SuccessCallBack(String result, int errorCode) {
				// TODO Auto-generated method stub
				BaseHttpEngine.showProgressDialog();
				requestPSNGetTokenId((String) BaseDroidApp
						.getInstanse().getBizDataMap()
						.get(ConstantGloble.CONVERSATION_ID));
			}
		});
	}
	
	/**token回调*/
	@SuppressWarnings("unchecked")
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Plps.TRACENO, traceNo);
		params.put(Plps.DECISIONNO, decisionNo);
		params.put(Comm.ACCOUNT_ID, accountId);
		params.put(Plps.ACCOUNTNOM, paymentAccount);
		params.put(Plps.FINEAMOUNT, fineAmount);
		params.put(Plps.ADDITIONALAMOUNT, additionalAmount);
		params.put(Plps.TOTALAMOUNT, totalAmount);
		params.put(ConstantGloble.PUBLIC_TOKEN, BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID));
		/**安全工具参数获取*/
		usbKeyText.InitUsbKeyResult(params);
		SipBoxUtils.setSipBoxParams(params);
		requestHttp(Plps.PSNTRAFFICFINESPAYMENTSUBMIT, "requestPsnTrafficFinesPaymentSubmitCallBack", params, true);
	}
	
	/**提交交易回调*/
	@SuppressWarnings("unchecked")
	public void requestPsnTrafficFinesPaymentSubmitCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		PlpsDataCenter.getInstance().setInterMapSubmitresult(null);
		Map<String, Object> statusResult = HttpTools.getResponseResult(resultObj);
		if(StringUtil.isNullOrEmpty(statusResult)){
			return;
		}else {
			PlpsDataCenter.getInstance().setInterMapSubmitresult(statusResult);
		}
		startActivityForResult(new Intent(InterproDecisNumberConfirmInfoActivity.this, InterproDecisNumberResultInfoActivity.class), 1011);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			setResult(RESULT_OK);
			finish();
		}
	}
}
