package com.chinamworld.bocmbci.biz.safety.safetyproduct;

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
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyBaseActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyConstant;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 投保信息确认页
 * 
 * @author panwe
 *
 */
public class SafetyProductBuyMsgConfirmActivity extends SafetyBaseActivity {
	/** 是否支持续保 */
	private final String CONTINUE_FLAG = "false";
	/** sip */
	private SipBox otpSipBxo;
	private SipBox smcSipBxo;
	/** 随机数 */
	private String randomNumber;
	private String polEffDate;
	private String acctId;
	private String acctNum;
	private String payMethod;
	private String benName;
	private String benIdNum;
	private boolean isFromList;
	/** 中银E盾加密控件 */
	UsbKeyText usbKeytext;
	/** 标识是否有动态口令 */
	private boolean isOtp = false;
	/** 标识是否有短息验证码 */
	private boolean isSmc = false;
	private Map<String, Object> securityMap = new HashMap<String, Object>();
	private Button nextButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.safety_submit_title));
		addView(R.layout.safety_product_buy_msgconfirm);
		getDataFormIntent();
		initViews();
		initSipBox();
	}

	private void getDataFormIntent() {
		isFromList = getIntent().getBooleanExtra(SafetyConstant.PRODUCTORSAVE, false);
		acctId = getIntent().getStringExtra(Comm.ACCOUNT_ID);
		acctNum = getIntent().getStringExtra(Comm.ACCOUNTNUMBER);
		payMethod = getIntent().getStringExtra(Safety.PAY_METHOD);
		polEffDate = getIntent().getStringExtra(Safety.POLEFFDATE);
		benName = getIntent().getStringExtra(Safety.BEN_NAME);
		benIdNum = getIntent().getStringExtra(Safety.BEN_IDNUM);
		randomNumber = getIntent().getStringExtra(SafetyConstant.RANDOMNUMBER);
		nextButton = (Button) findViewById(R.id.btnConfirm);
		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checkDate();
			}
		});
	}

	private void initViews() {
		Map<String, Object> countMap = SafetyDataCenter.getInstance().getCountMap();
		securityMap = SafetyDataCenter.getInstance().getHoldProDetail();
		/** 公司名称 */
		((TextView) findViewById(R.id.companyname)).setText((String) countMap.get(Safety.INSURANCE_COMANY));
		/** 承保子公司名称 */
		((TextView) findViewById(R.id.tv_subCompany)).setText((String) countMap.get(Safety.SUBINSUID));
		/** 产品名称 */
		((TextView) findViewById(R.id.productname)).setText((String) countMap.get(Safety.RISKNAME));
		/** 投保人姓名 */
		((TextView) findViewById(R.id.buyername)).setText((String) countMap.get(Safety.APPL_NAME));
		/** 币种 */
		((TextView) findViewById(R.id.tv_bizhong)).setText(LocalData.queryCurrencyList.get(0));
		/** 保费 */
		((TextView) findViewById(R.id.money)).setText(StringUtil.parseStringPattern((String) countMap.get(Safety.RISKPAEM), 2));
		/** 付款账户 */
		((TextView) findViewById(R.id.acct)).setText(StringUtil.getForSixForString(acctNum));
	}

	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox() {
		String commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeytext = (UsbKeyText) findViewById(R.id.sip_usbkey);
		usbKeytext.Init(commConversationId, randomNumber, securityMap, this);
		String TAG = "123";
		LogGloble.i(TAG, securityMap.toString());
		isOtp = usbKeytext.getIsOtp();
		isSmc = usbKeytext.getIsSmc();
		initOtpSipBox();
		initSmcSipBox();

	}

	/** 初始化otp */
	private void initOtpSipBox() {
		if (isOtp) {
			((LinearLayout) findViewById(R.id.layout_sip)).setVisibility(View.VISIBLE);
			otpSipBxo = (SipBox) findViewById(R.id.et_cecurity_ps);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(otpSipBxo, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//			otpSipBxo.setCipherType(SystemConfig.CIPHERTYPE);
//			otpSipBxo.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			otpSipBxo.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			otpSipBxo.setId(10002);
//			otpSipBxo.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			otpSipBxo.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			otpSipBxo.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			otpSipBxo.setSipDelegator(this);
//			otpSipBxo.setRandomKey_S(randomNumber);
		}
	}

	/** 初始化smc */
	private void initSmcSipBox() {
		if (isSmc) {
			((LinearLayout) findViewById(R.id.layout_sms)).setVisibility(View.VISIBLE);
			Button btnSendSms = (Button) findViewById(R.id.smsbtn);
			SmsCodeUtils.getInstance().addSmsCodeListner(btnSendSms, new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 发送验证码请求
					sendSMSCToMobile();
				}
			});
			smcSipBxo = (SipBox) findViewById(R.id.sip_sms);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(smcSipBxo, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//			smcSipBxo.setCipherType(SystemConfig.CIPHERTYPE);
//			smcSipBxo.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			smcSipBxo.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			smcSipBxo.setId(10002);
//			smcSipBxo.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			smcSipBxo.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			smcSipBxo.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			smcSipBxo.setSipDelegator(this);
//			smcSipBxo.setRandomKey_S(randomNumber);
		}
	}

	/** 验证动态口令 */
	private void checkDate() {
		/** 音频Key安全工具认证 */
		usbKeytext.checkDataUsbKey(otpSipBxo, smcSipBxo, new IUsbKeyTextSuccess() {
			@Override
			public void SuccessCallBack(String result, int errorCode) {
				BaseHttpEngine.showProgressDialog();
				String commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
				requestPSNGetTokenId(commConversationId);
			}
		});
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		insurSubmit(token);
	}

	/**
	 * 发送保单提交请求
	 * 
	 * @param token
	 */
	private void insurSubmit(String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Safety.METHOD_BUY_SUBMIT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> countMap = SafetyDataCenter.getInstance().getCountMap();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Safety.TOKEN, token);
		map.put(Safety.SAFETY_HOLD_CONTINUE_FLAG, CONTINUE_FLAG);
		map.put(Safety.SAFETY_HOLD_TRANS_DATE, countMap.get(Safety.SAFETY_HOLD_TRANS_DATE));
		map.put(Safety.SAFETY_HOLD_TRANS_ACCNO, countMap.get(Safety.SAFETY_HOLD_TRANS_ACCNO));
		map.put(Safety.RISKCODE, countMap.get(Safety.RISKCODE));
		map.put(Safety.RISKNAME, countMap.get(Safety.RISKNAME));
		map.put(Safety.INSURANCE_ID, countMap.get(Safety.INSURANCE_ID));
		map.put(Safety.INSURANCE_COMANY, countMap.get(Safety.INSURANCE_COMANY));
		map.put(Safety.SAFETY_HOLD_RISK_UNIT, SafetyConstant.RISKUNIT);
		map.put(Safety.RISKPAEM, countMap.get(Safety.RISKPAEM));
		map.put(Safety.POLEFFDATE, polEffDate);
		map.put(Safety.PAY_METHOD, payMethod);
		map.put(Safety.SAFETY_HOLD_CURRENCY, SafetyConstant.CURRENCY);
		map.put(Safety.ACC_ID, acctId);
		map.put(Safety.BEN_NAME, benName);
		map.put(Safety.BEN_IDNUM, benIdNum);

		/** 安全工具参数获取 */
		usbKeytext.InitUsbKeyResult(map);

		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "insurSubmitCallBack");
	}

	/** 投保提交返回处理 **/
	public void insurSubmitCallBack(Object resultObj) {
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) this.getHttpTools().getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(result))
			return;
		Map<String, Object> countMap = SafetyDataCenter.getInstance().getCountMap();
		this.startActivity(new Intent(this, SafetyProductBuyResultActivity.class).putExtra(SafetyConstant.PRODUCTORSAVE, isFromList).putExtra(Safety.INSURANCE_COMANY, (String) countMap.get(Safety.INSURANCE_COMANY)).putExtra(Safety.SUBINSUNAME, (String) countMap.get(Safety.SUBINSUID)).putExtra(Safety.RISKNAME, (String) countMap.get(Safety.RISKNAME)).putExtra(Safety.SAFETY_HOLD_POLICY_NO, (String) result.get(Safety.SAFETY_HOLD_POLICY_NO)).putExtra(Safety.RISKPAEM, (String) countMap.get(Safety.RISKPAEM)).putExtra(Safety.SAFETY_HOLD_APPL_DATE, polEffDate).putExtra(Safety.SAFETY_HOLD_POL_END_DATE, (String) result.get(Safety.SAFETY_HOLD_POL_END_DATE)).putExtra(Safety.APPL_EMAIL, (String) result.get(Safety.APPL_EMAIL)));
		SafetyProductBuyMsgConfirmActivity.this.finish();
	}
}
