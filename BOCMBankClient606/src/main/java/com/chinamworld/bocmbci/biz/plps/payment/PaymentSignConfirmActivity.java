package com.chinamworld.bocmbci.biz.plps.payment;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;
/**
 * 签约确认页
 * @author panwe
 *
 */
public class PaymentSignConfirmActivity extends PlpsBaseActivity{
	/** sip */
	private SipBox otpSipBxo;
	private SipBox smcSipBxo;
	/** 随机数 */
	private String randomNumber;
	/** 加密类型 */
	private String otp;
	private String smc;
	private String otpPassword;
	private String otpRandomNum;
	private String smcPassword;
	private String smcRandomNum;
	private int flag;
	private String phoneNumber;
	private String agentName;
	private String agentCode;
	private String serviceName;
	private String payUserNo;
	private String custName;
	private String customerAlias;
	private String remarks;
	private String subAgentCode;
	private String cspCode;
	private String serviceType;
	private String acctId;
	private String acctNum;
	private String nickName;
	private String accountIbkNum;
	private String accountType;
	private String accountTypeName;
	/**中银E盾密码控件*/
	private UsbKeyText usbKeyText;
	private Boolean isOtp;
	private Boolean isSmc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.plps_payment_sign_confirm);
		setTitle(PlpsDataCenter.payment[1]);
		getIntentData();
		setUpViews();
	}
	
	private void getIntentData(){
		Intent it = getIntent();
		flag = it.getIntExtra("flag", 0);
//		otp = it.getStringExtra(Comm.Otp);
//		smc = it.getStringExtra(Comm.Smc);
		accountType = it.getStringExtra(Comm.ACCOUNT_TYPE);
		accountTypeName = LocalData.AccountType.get(accountType);
		acctNum = it.getStringExtra(Comm.ACCOUNTNUMBER);
		nickName = it.getStringExtra(Comm.NICKNAME);
		accountIbkNum = it.getStringExtra(Comm.ACCOUNTIBKNUM);
		acctId = it.getStringExtra(Comm.ACCOUNT_ID);
		randomNumber = it.getStringExtra(Plps.RANDOMNUMBER);
		phoneNumber = it.getStringExtra(Plps.PHONENUMBER);
		agentName = it.getStringExtra(Plps.AGENTNAME);
		agentCode = it.getStringExtra(Plps.AGENTCODE);
		subAgentCode = it.getStringExtra(Plps.SUBAGENTCODE);
		cspCode = it.getStringExtra(Plps.CSPCODE);
		serviceType = it.getStringExtra(Plps.SERVICETYPE);
		serviceName = it.getStringExtra(Plps.SERVICENAME);
		payUserNo = it.getStringExtra(Plps.PAYUSERNO);
		custName = it.getStringExtra(Plps.CUSTNAME);
		remarks = it.getStringExtra(Plps.REMARKS);
		customerAlias = it.getStringExtra(Plps.CUSTOMERALIAS);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.ib_back) {
			setResult(10);
			finish();
		}
		else if(v.getId() == R.id.ib_top_right_btn){
			PlpsDataCenter.getInstance().clearAllData();
			ActivityTaskManager.getInstance().removeAllActivity();
		}
	}
	
	private void setUpViews(){
		((TextView) findViewById(R.id.servicename)).setText(serviceName);
		((TextView) findViewById(R.id.agent)).setText(agentName);
		if (!StringUtil.isNullOrEmpty(customerAlias)) {
			((TextView) findViewById(R.id.nickname)).setText(customerAlias);
		}
		TextView customerName = (TextView)findViewById(R.id.customerName);
		customerName.setText(custName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, customerName);
//		((TextView) findViewById(R.id.customerName)).setText(custName);
		((TextView) findViewById(R.id.phone)).setText(phoneNumber);
		((TextView) findViewById(R.id.payuserno)).setText(payUserNo);
		TextView signAcct = (TextView)findViewById(R.id.signacct);
		TextView signAcctext = (TextView)findViewById(R.id.signacctext);
		signAcct.setText(accountTypeName);
		signAcctext.setText(StringUtil.getForSixForString(acctNum)+" "+nickName+" "+accountIbkNum);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, signAcct);
//		((TextView) findViewById(R.id.signacct)).setText(StringUtil.getForSixForString(acctNum));
		if(!StringUtil.isNullOrEmpty(remarks)){
//			((TextView) findViewById(R.id.remarks)).setText(remarks);
			TextView remarkst = (TextView)findViewById(R.id.remarks);
			remarkst.setText(remarks);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this, remarkst);
		}
		PlpsUtils.setOnShowAllTextListener(this, (TextView) findViewById(R.id.servicename)
				,(TextView) findViewById(R.id.agent),(TextView) findViewById(R.id.remarks));
		initSipBox();
//		if (!StringUtil.isNull(otp)) {
//			initOtpSipBox();
//		}
//		if (!StringUtil.isNull(smc)) {
//			initSmcSipBox();
//		}
	}
	private void initSipBox(){
		Map<String, Object> resultObj =(Map<String, Object>)PlpsDataCenter.getInstance().getResultObj();
		usbKeyText = (UsbKeyText) findViewById(R.id.sip_usbkey);
		String commConversationId = (String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeyText.Init(commConversationId, randomNumber, resultObj,this);
		isOtp = usbKeyText.getIsOtp();
		isSmc = usbKeyText.getIsSmc();
		initOtpSipBox();
		initSmcSipBox();
	}

	/** 初始化otp */
	private void initOtpSipBox() {
		if(isOtp){
		((LinearLayout) findViewById(R.id.layout_sip)).setVisibility(View.VISIBLE);
		otpSipBxo = (SipBox) findViewById(R.id.et_cecurity_ps);
		// add by 2016年9月12日 luqp 修改
		SipBoxUtils.initSipBoxWithTwoType(otpSipBxo, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		otpSipBxo.setCipherType(SystemConfig.CIPHERTYPE);
//		otpSipBxo.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		otpSipBxo.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		otpSipBxo.setId(10002);
//		otpSipBxo.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		otpSipBxo.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		otpSipBxo.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//		otpSipBxo.setSipDelegator(this);
//		otpSipBxo.setRandomKey_S(randomNumber);
		}
	}

	/** 初始化smc */
	private void initSmcSipBox() {
		if(isSmc){
		((LinearLayout) findViewById(R.id.layout_sms)).setVisibility(View.VISIBLE);
		Button btnSendSms = (Button) findViewById(R.id.smsbtn);
		SmsCodeUtils.getInstance().addSmsCodeListner(btnSendSms,smcOnclickListener);
		smcSipBxo = (SipBox) findViewById(R.id.sip_sms);
		// add by 2016年9月12日 luqp 修改
		SipBoxUtils.initSipBoxWithTwoType(smcSipBxo, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		smcSipBxo.setCipherType(SystemConfig.CIPHERTYPE);
//		smcSipBxo.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		smcSipBxo.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		smcSipBxo.setId(10002);
//		smcSipBxo.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		smcSipBxo.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		smcSipBxo.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//		smcSipBxo.setSipDelegator(this);
//		smcSipBxo.setRandomKey_S(randomNumber);
		}
	}
	
	/**
	 * 确定操作
	 * @param v
	 */
	public void btnNextOnclick(View v) {
		// if (submitCheck()) {
		// BaseHttpEngine.showProgressDialog();
		// requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
		// .getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		// }
		/** 音频Key安全工具认证 */
		usbKeyText.checkDataUsbKey(otpSipBxo, smcSipBxo,
				new IUsbKeyTextSuccess() {

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
	
	/**
	 * 安全控件校验
	 * @return
	 */
//	private boolean submitCheck() {
//		try {
//			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
//			if (!StringUtil.isNull(smc)) {
//				RegexpBean rebSms = new RegexpBean(
//						getString(R.string.acc_smc_regex), smcSipBxo.getText()
//								.toString(), ConstantGloble.SIPSMCPSW);
//				lists.add(rebSms);
//			}
//			if (!StringUtil.isNull(otp)) {
//				RegexpBean rebOtp = new RegexpBean(
//						getString(R.string.active_code_regex), otpSipBxo
//								.getText().toString(), ConstantGloble.SIPOTPPSW);
//				lists.add(rebOtp);
//			}
//			if (RegexpUtils.regexpDate(lists)) {
//				if (!StringUtil.isNull(smc)) {
//					smcPassword = smcSipBxo.getValue().getEncryptPassword();
//					smcRandomNum = smcSipBxo.getValue().getEncryptRandomNum();
//				}
//				if (!StringUtil.isNull(otp)) {
//					otpPassword = otpSipBxo.getValue().getEncryptPassword();
//					otpRandomNum = otpSipBxo.getValue().getEncryptRandomNum();
//				}
//				return true;
//			}
//		} catch (CodeException e) {
//			BaseDroidApp.getInstanse().createDialog(null, e.getMessage());
//			LogGloble.exceptionPrint(e);
//			return false;
//		}
//		return false;
//	}
	
	private OnClickListener smcOnclickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			sendSMSCToMobile();
		}
	};
	
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		signSubmit(token);
	}
	
	/**
	 * 发送签约提交请求
	 * @param token
	 */
	private void signSubmit(String token){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.METHODSIGNSUBMIT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Plps.TOKEN, token);
		map.put(Plps.PHONENUMBER, phoneNumber);
		map.put(Plps.AGENTNAME, agentName);
		map.put(Plps.AGENTCODE, agentCode);
		map.put(Plps.SUBAGENTCODE, subAgentCode);
		map.put(Plps.CSPCODE, cspCode);
		map.put(Plps.SERVICENAME, serviceName);
		map.put(Plps.SERVICETYPE, serviceType);
		map.put(Plps.PAYUSERNO, payUserNo);
		map.put(Plps.CUSTNAME, custName);
		map.put(Plps.REMARKS, remarks);
		map.put(Plps.CUSTOMERALIAS, customerAlias);
		map.put(Comm.ACCOUNT_ID, acctId);
		if (flag == 1) {
			map.put(Plps.SIGNTYPE, "1");
		}else{
			map.put(Plps.SIGNTYPE, "0");
		}
//		if (!StringUtil.isNull(otp)) {
//			map.put(Comm.Otp, otpPassword);
//			map.put(Comm.Otp_Rc, otpRandomNum);
//		}
//		if (!StringUtil.isNull(smc)) {
//			map.put(Comm.Smc, smcPassword);
//			map.put(Comm.Smc_Rc, smcRandomNum);
//		}
		/**安全工具参数获取*/
		usbKeyText.InitUsbKeyResult(map);
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,"signSubmitCallBack");
	}
	
	public void signSubmitCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		startActivityForResult(new Intent(this, PaymentSignResultActivity.class)
		.putExtra(Plps.PHONENUMBER, phoneNumber)
		.putExtra(Plps.AGENTNAME, agentName)
		.putExtra(Comm.ACCOUNT_TYPE, accountTypeName)
		.putExtra(Comm.ACCOUNTNUMBER, acctNum)
		.putExtra(Comm.NICKNAME, nickName)
		.putExtra(Comm.ACCOUNTIBKNUM, accountIbkNum)
		.putExtra(Plps.SERVICENAME, serviceName)
		.putExtra(Plps.PAYUSERNO, payUserNo)
		.putExtra(Plps.CUSTNAME, custName)
		.putExtra(Plps.REMARKS, remarks)
		.putExtra(Plps.CUSTOMERALIAS, customerAlias),1001);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			setResult(RESULT_OK);
			finish();
		}
	}
}
