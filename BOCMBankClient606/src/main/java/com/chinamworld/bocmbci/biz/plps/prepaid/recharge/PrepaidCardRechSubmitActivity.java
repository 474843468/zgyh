package com.chinamworld.bocmbci.biz.plps.prepaid.recharge;

import java.util.HashMap;
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
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 预付卡支付提交交易
 * 
 * @author Zhi
 */
public class PrepaidCardRechSubmitActivity extends PlpsBaseActivity {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 手机验证码sip */
	private SipBox otpSipBxo;
	/** 短信验证码sip*/
	private SipBox smcSipBxo;
	
//	private String otp;
//	private String smc;
	private String otpPassword;
	/** 动态口令控件随机数 */
	private String otpRandomNum;
	private String smcPassword;
	/** 手机验证码控件随机数 */
	private String smcRandomNum;
	/**中银E盾密码控件*/
	private UsbKeyText usbKeyText;
	/**是否有动态口令*/
	private Boolean isOtp;
	/**是否有短信验证*/
	private Boolean isSmc;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("预付卡充值");
		inflateLayout(R.layout.plps_prepaid_card_replenishment_submit);
		initView();
	}
	
	/** 初始化视图显示 */
	@SuppressWarnings("unchecked")
	private void initView() {
		Map<String, Object> map = PlpsDataCenter.getInstance().getMapPrepaidCardRechPre();
		((TextView) findViewById(R.id.tv_pripaidType)).setText((String) ( map.get(Plps.MERCHNAME)));
		((TextView) findViewById(R.id.tv_pripaidCarNum)).setText((String) map.get(Plps.PREPARDQUERYNUMBER));
		String name = (String) map.get(Plps.NAME);
		if (!StringUtil.isNull(name)) {
			((TextView) findViewById(R.id.tv_name)).setText(name);
		} else {
//			findViewById(R.id.ll_name).setVisibility(View.GONE);
		}
		TextView account = (TextView)findViewById(R.id.tv_account);
		account.setText(StringUtil.getForSixForString((String) map.get(Comm.ACCOUNTNUMBER))+"（"+map.get(Comm.NICKNAME)+"）");
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, account);
		((TextView) findViewById(R.id.tv_transValue)).setText((String) map.get(Plps.AMOUNT));
		initSipBox();
//		otp = (String) map.get(Comm.Otp);
//		smc = (String) map.get(Comm.Smc);
//		if (!StringUtil.isNullOrEmpty(otp)) {
//			initOtpSipBox();
//		}
//		if (!StringUtil.isNullOrEmpty(smc)) {
//			initSmcSipBox();
//		}
		findViewById(R.id.btnConfirm).setOnClickListener(this);
	}
	private void initSipBox(){
		Map<String, Object> resultObj =(Map<String, Object>)PlpsDataCenter.getInstance().getPrepaidResultMap();
		String random = (String)PlpsDataCenter.getInstance().getMapPrepaidCardRechPre().get(Plps.RANDOMNUMBER);
		usbKeyText = (UsbKeyText) findViewById(R.id.sip_usbkey);
		String commConversationId = (String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeyText.Init(commConversationId, random, resultObj,this);
		isOtp = usbKeyText.getIsOtp();
		isSmc = usbKeyText.getIsSmc();
		initOtpSipBox();
		initSmcSipBox();
	}

	
	/** 初始化otp */
	private void initOtpSipBox() {
		if(isOtp){
			findViewById(R.id.layout_sip).setVisibility(View.VISIBLE);
			otpSipBxo = (SipBox) findViewById(R.id.et_cecurity_ps);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(otpSipBxo, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, (String) PlpsDataCenter.getInstance().getMapPrepaidCardRechPre().get(Plps.RANDOMNUMBER), this);
//			otpSipBxo.setCipherType(SystemConfig.CIPHERTYPE);
//			otpSipBxo.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			otpSipBxo.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			otpSipBxo.setId(10002);
//			otpSipBxo.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			otpSipBxo.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			otpSipBxo.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			otpSipBxo.setSipDelegator(this);
//			otpSipBxo.setRandomKey_S((String) PlpsDataCenter.getInstance().getMapPrepaidCardRechPre().get(Plps.RANDOMNUMBER));
		}
	}

	/** 初始化smc */
	private void initSmcSipBox() {
		if(isSmc){
			findViewById(R.id.layout_sms).setVisibility(View.VISIBLE);
			Button btnSendSms = (Button) findViewById(R.id.smsbtn);
			SmsCodeUtils.getInstance().addSmsCodeListner(btnSendSms,smcOnclickListener);
			smcSipBxo = (SipBox) findViewById(R.id.sip_sms);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(smcSipBxo, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, (String) PlpsDataCenter.getInstance().getMapPrepaidCardRechPre().get(Plps.RANDOMNUMBER), this);
//			smcSipBxo.setCipherType(SystemConfig.CIPHERTYPE);
//			smcSipBxo.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			smcSipBxo.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			smcSipBxo.setId(10002);
//			smcSipBxo.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			smcSipBxo.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			smcSipBxo.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			smcSipBxo.setSipDelegator(this);
//			smcSipBxo.setRandomKey_S((String) PlpsDataCenter.getInstance().getMapPrepaidCardRechPre().get(Plps.RANDOMNUMBER));
		}
	}

	/**
	 * 安全控件校验
	 * 
	 * @return true-校验成功 false-校验失败
	 */
//	private boolean submitCheck() {
//		try {
//			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
//			if (!StringUtil.isNullOrEmpty(smc)) {
//				RegexpBean rebSms = new RegexpBean(
//						getString(R.string.acc_smc_regex), smcSipBxo.getText()
//								.toString(), ConstantGloble.SIPSMCPSW);
//				lists.add(rebSms);
//			}
//			if (!StringUtil.isNullOrEmpty(otp)) {
//				RegexpBean rebOtp = new RegexpBean(
//						getString(R.string.active_code_regex), otpSipBxo
//								.getText().toString(), ConstantGloble.SIPOTPPSW);
//				lists.add(rebOtp);
//			}
//			if (RegexpUtils.regexpDate(lists)) {
//				if (!StringUtil.isNullOrEmpty(smc)) {
//					smcPassword = smcSipBxo.getValue().getEncryptPassword();
//					smcRandomNum = smcSipBxo.getValue().getEncryptRandomNum();
//				}
//				if (!StringUtil.isNullOrEmpty(otp)) {
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 4) {
			setResult(4);
			LogGloble.i("PrepaidCardRechSubmitActivity", "finish");
			finish();
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------控件事件-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** 获取手机交易码监听事件 */
	private OnClickListener smcOnclickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			sendSMSCToMobile();
		}
	};
	
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btnConfirm:
			// if (!submitCheck()) {
			// return;
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

			break;
		}
	};
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO ---------------------------------------网络请求与回调方法--------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("unchecked")
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> userInput = PlpsDataCenter.getInstance().getMapPrepaidCardRechPre();
		params.put(Comm.ACCOUNT_ID, userInput.get(Comm.ACCOUNT_ID));
		params.put(Plps.MERCHNO, userInput.get(Plps.MERCHNO));
		params.put(Plps.MERCHNAME, userInput.get(Plps.MERCHNAME));
		params.put(Plps.PREPARDQUERYNUMBER, userInput.get(Plps.PREPARDQUERYNUMBER));
		String name = (String) userInput.get(Plps.NAME);
		if (!StringUtil.isNull(name)) {
			params.put(Plps.NAME, name);
		}
		params.put(Plps.PREPARDQUERYCURRENCY, "001");
		params.put(Plps.AMOUNT, userInput.get(Plps.AMOUNT));
		params.put(ConstantGloble.PUBLIC_TOKEN, BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID));
//		if (!StringUtil.isNullOrEmpty(otp)) {
//			params.put(Comm.Otp, otpPassword);
//			params.put(Comm.Otp_Rc, otpRandomNum);
//		}
//		if (!StringUtil.isNullOrEmpty(smc)) {
//			params.put(Comm.Smc, smcPassword);
//			params.put(Comm.Smc_Rc, smcRandomNum);
//		}
		/**安全工具参数获取*/
		usbKeyText.InitUsbKeyResult(params);
		SipBoxUtils.setSipBoxParams(params);
		requestHttp(Plps.PSNPREPAIDCARDREPLENISHMENT, "requestPsnPrepaidCardReplenishmentCallBack", params, true);
	}
	
	/** 预付卡支付提交回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnPrepaidCardReplenishmentCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		PlpsDataCenter.getInstance().setMapPrepaidCardQuerySupportCardType(resultMap);
		Intent intent = new Intent(this, PrepaidCardRechSuccessActivity.class);
		startActivityForResult(intent, 4);
	}
}
