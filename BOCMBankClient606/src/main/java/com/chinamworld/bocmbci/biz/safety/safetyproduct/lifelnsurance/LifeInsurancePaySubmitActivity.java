package com.chinamworld.bocmbci.biz.safety.safetyproduct.lifelnsurance;

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
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyConstant;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 寿险提交页面
 * 
 * @author Zhi
 */
public class LifeInsurancePaySubmitActivity extends LifeInsuranceBaseActivity {

	public static String TAG = "LifeInsurancePaySubmitActivity";
	/** 随机数 */
	private String random;
	/** 手机验证码sip */
	private SipBox otpSipBxo;
	/** 短信验证码sip*/
	private SipBox smcSipBxo;
	/** 中银E盾 */
	private UsbKeyText usbKeyText;
	/** 标识是否有短息验证码、动态口令 */
	private boolean isOtp = false;
	private boolean isSmc = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainView = addView(R.layout.safety_life_product_pay_submit);
		setTitle(getString(R.string.safety_msgfill_title));
		random = getIntent().getStringExtra(SafetyConstant.RANDOMNUMBER);
		findView();
		viewSet();
	}

	@Override
	protected void findView() {
		otpSipBxo = (SipBox) mMainView.findViewById(R.id.et_cecurity_ps);
		smcSipBxo = (SipBox) mMainView.findViewById(R.id.sip_sms);
		usbKeyText = (UsbKeyText) mMainView.findViewById(R.id.sip_usbkey);
	}

	@Override
	protected void viewSet() {
		Map<String, Object> userInput = SafetyDataCenter.getInstance().getMapUserInput();
		((TextView) mMainView.findViewById(R.id.tv_company)).setText((String) userInput.get(Safety.INSURANCE_COMANY));
		((TextView) mMainView.findViewById(R.id.tv_subCompany)).setText((String) userInput.get(Safety.SUBINSUNAME));
		((TextView) mMainView.findViewById(R.id.tv_prodName)).setText((String) userInput.get(Safety.RISKNAME));
		((TextView) mMainView.findViewById(R.id.tv_applicant_name)).setText((String) userInput.get(Safety.APPL_NAME));
		((TextView) mMainView.findViewById(R.id.tv_bizhong)).setText("人民币元");
		((TextView) mMainView.findViewById(R.id.tv_amount)).setText(StringUtil.parseStringPattern((String) SafetyDataCenter.getInstance().getMapLifeInsuranceCalculation().get(Safety.AMOUNT), 2));
		((TextView) mMainView.findViewById(R.id.tv_payacct)).setText(StringUtil.getForSixForString((String) userInput.get(Safety.ACCOUNTNO)));
		((TextView) mMainView.findViewById(R.id.tv_accMoney)).setText(StringUtil.parseStringPattern(getIntent().getStringExtra(Safety.AMOUNT), 2));

		if (userInput.get(Safety.TRADEWAY).equals("01")) {
			((TextView) mMainView.findViewById(R.id.tv_keyForAmount)).setText(getResources().getString(R.string.safety_lifeInsurance_confirm_zong));
		} else {
			((TextView) mMainView.findViewById(R.id.tv_keyForAmount)).setText(getResources().getString(R.string.safety_lifeInsurance_confirm_shouqi));
		}
		mMainView.findViewById(R.id.btnNext).setOnClickListener(clickListener);

		initSipBox(SafetyDataCenter.getInstance().getMapLifeInsurancePayVerify());
		PopupWindowUtils.getInstance().setOnShowAllTextListenerByViewGroup(this, (LinearLayout) mMainView.findViewById(R.id.ll_content));
	}

	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox(Map<String, Object> result){
		String mmconversationId = (String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeyText.Init(mmconversationId, random, result, this);
		isOtp = usbKeyText.getIsOtp();
		isSmc = usbKeyText.getIsSmc();
		initOtpSipBox();
		initSmcSipBox();
	}
	
	/** 初始化otp */
	private void initOtpSipBox() {
		if (isOtp) {
			mMainView.findViewById(R.id.layout_sip).setVisibility(View.VISIBLE);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(otpSipBxo, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, random, this);
//			otpSipBxo.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			otpSipBxo.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			otpSipBxo.setId(10002);
//			otpSipBxo.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			otpSipBxo.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			otpSipBxo.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			otpSipBxo.setSipDelegator(this);
//			otpSipBxo.setRandomKey_S(random);
		}
	}

	/** 初始化smc */
	private void initSmcSipBox() {
		if (isSmc) {
			mMainView.findViewById(R.id.layout_sms).setVisibility(View.VISIBLE);
			Button btnSendSms = (Button) mMainView.findViewById(R.id.smsbtn);
			SmsCodeUtils.getInstance().addSmsCodeListner(btnSendSms, smcOnclickListener);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(smcSipBxo, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, random, this);
//			smcSipBxo.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			smcSipBxo.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			smcSipBxo.setId(10002);
//			smcSipBxo.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			smcSipBxo.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			smcSipBxo.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			smcSipBxo.setSipDelegator(this);
//			smcSipBxo.setRandomKey_S(random);
		}
	}

	/** 安全控件提交校验 */
	private void submitRegexp() {
		usbKeyText.checkDataUsbKey(otpSipBxo, smcSipBxo, new IUsbKeyTextSuccess() {
			
			@Override
			public void SuccessCallBack(String result, int errorCode) {
				// 获取token请求
				BaseHttpEngine.showProgressDialog();
				requestPSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
			}
		});
	}
	
	/** 发送短信验证码监听 */
	private OnClickListener smcOnclickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			sendSMSCToMobile();
		}
	};
	
	/** 下一步 */
	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			submitRegexp();
		}
	};
	
	/** 请求token回调 */
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> userInput = SafetyDataCenter.getInstance().getMapUserInput();
		params.put(Safety.SAFETY_HOLD_TRANS_ACCNO, SafetyDataCenter.getInstance().getMapLifeInsuranceCalculation().get(Safety.SAFETY_HOLD_TRANS_ACCNO));
		params.put(Safety.TRANDATE, SafetyDataCenter.getInstance().getMapLifeInsuranceCalculation().get(Safety.TRANDATE));
		params.put(Safety.INSURANCE_ID, userInput.get(Safety.INSURANCE_ID));
		params.put(Safety.INSURANCE_COMANY, userInput.get(Safety.INSURANCE_COMANY));
		params.put(Safety.RISKCODE, userInput.get(Safety.RISKCODE));
		params.put(Safety.RISKNAME, userInput.get(Safety.RISKNAME));
		params.put(Acc.ACC_ACCOUNTID, userInput.get(Acc.DETAIL_ACC_ACCOUNTID_REQ));
		params.put(Safety.ACCOUNTNO, userInput.get(Safety.ACCOUNTNO));
		params.put(Acc.FINANCEICDETAIL_CURRENCY_RES, SafetyConstant.CURRENCY);
		params.put(Safety.SAFETY_HOLD_RISK_PREM, SafetyDataCenter.getInstance().getMapLifeInsuranceCalculation().get(Safety.AMOUNT));
		params.put(Safety.INSUREDNAME, userInput.get(Safety.BEN_NAME));
		params.put(Safety.INSUREDIDENTIFYNO, userInput.get(Safety.BEN_IDNUM));
		params.put(ConstantGloble.PUBLIC_TOKEN, BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID));
		/**安全工具参数获取*/
		usbKeyText.InitUsbKeyResult(params);
		SipBoxUtils.setSipBoxParams(params);
		getHttpTools().requestHttp(Safety.PSNLIFEINSURANCEPAYSUBMIT, "requestPsnLifeInsurancePaySubmitCallBack", params, true);
	};
	
	/** 投保提交回调 */
	public void requestPsnLifeInsurancePaySubmitCallBack(Object resultObj) {
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		if (!StringUtil.isNull((String) resultMap.get(Safety.RETURNCODE)) && resultMap.get(Safety.RETURNCODE).equals("IBAS.T1029")) {
			// 后台报错
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(Safety.TRANDATE, resultMap.get(Safety.TRSDATE));
			params.put(Safety.TRANSEQ, resultMap.get(Safety.TRANSEQ));
			getHttpTools().requestHttp(Safety.PSNINSURANCEERRORINFOQUERY, "requestPsnInsuranceErrorInfoQueryCallBack", params);
		} else {
			SafetyDataCenter.getInstance().setMapLifeInsurancePaySubmit(resultMap);
			Intent intent = new Intent(this, LifeInsurancePaySuccessActivity.class);
			intent.putExtra(Safety.SAFETY_HOLD_POLICY_NO, (String) resultMap.get(Safety.SAFETY_HOLD_POLICY_NO));
			intent.putExtra(Safety.SAFETY_HOLD_POLICY_NO, (String) resultMap.get(Safety.SAFETY_HOLD_POLICY_NO));
			intent.putExtra(Safety.SAFETY_HOLD_POLICY_NO, (String) resultMap.get(Safety.SAFETY_HOLD_POLICY_NO));
			startActivityForResult(intent, 1);
		}
	}
	
	/** 保险公司失败返回信息查询回调 */
	public void requestPsnInsuranceErrorInfoQueryCallBack(Object resultObj) {
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		String loseInfo = (String) resultMap.get(Safety.ERRORMSG);
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent(this, LifeInsurancePayLoseActivity.class);
		intent.putExtra(Safety.ERRORMSG, loseInfo);
		intent.putExtra("jumpFlag", TAG);
		intent.putExtra("title", getResources().getString(R.string.safety_msgfill_title));
		startActivityForResult(intent, 1);
	}
}
