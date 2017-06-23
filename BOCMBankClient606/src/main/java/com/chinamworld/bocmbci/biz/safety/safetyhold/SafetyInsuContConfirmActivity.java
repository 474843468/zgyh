package com.chinamworld.bocmbci.biz.safety.safetyhold;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.SBRemit;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.safety.SafetyBaseActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;
/**
 * 续保确认页面
 * @author fsm
 *
 */
public class SafetyInsuContConfirmActivity extends SafetyBaseActivity {

	/** 保险公司名称， 产品名称， 投保人姓名， 币种， 保费金额, 付款账户*/
	private TextView safety_company, product_name, safety_applicant_name, 
		currency, safety_insurance_fee_amount, acc_payout;
	/**  手机交易码， 动态口令*/
	private SipBox sipbox_smc, sipbox_active;
	//确认， 获取
	private Button confirm, smsbtn;
	
	/** 保险公司名称， 产品名称， 投保人姓名， 保单号， 币种， 保费金额， 付款账户*/
	private String safetyCompany, productName, safetyApplicantName, 
		currencyStr, insuranceFeeAmount, accPayout;
	/** 动态口令 , 手机交易码  */
	private String otpStr, smcStr;
	/** 加密之后加密随机数 */
	private String otp_password_RC = "";
	private String smc_password_RC = "";
	/** 加密控件里的随机数 */
	protected String randomNumber;
	/** 安全因子数组 */
	public List<Map<String, Object>> factorList;
	/** 是否上传短信验证码 */
	private boolean isSms = false;
	/** 是否上传动态口令 */
	private boolean isOtp = false;
//	/** 手机验证码有效时间, CA加签数据XML报文, CA的DN值*/
//	private String smcTrigerInterval, _plainData, _certDN;
	/** 新单/续保标志， 交易日期， 交易流水号， 产品代码（银行内部）， 产品名称， 保险公司代码， 保险公司名称，
	 *  保费， 保单生效日期， 付款方式， 账户id，被 保人姓名， 被保人证件号, 结束日期*/
	private String continueFlag, transDate, transAccNo, riskCode, riskName, insuId, insuName, 
		riskPrem, polEffDate, payMethod, accId, benName, benIdNo, endDate;
	
	/** 中银E盾加密控件 */
	UsbKeyText usbKeytext;

	private Map<String, Object> securityMap = new HashMap<String, Object>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.safety_insu_cont_confirm);
		setTitle(getString(R.string.safety_hold_pro_detail_btn_continu));
		initParamsInfo();
		initViews();
		setViewInfos();
	}
	
	private void initViews(){
		safety_company = (TextView)findViewById(R.id.safety_company);
		product_name = (TextView)findViewById(R.id.product_name);
		safety_applicant_name = (TextView)findViewById(R.id.safety_applicant_name);
		currency = (TextView)findViewById(R.id.currency);
		safety_insurance_fee_amount = (TextView)findViewById(R.id.safety_insurance_fee_amount);
		acc_payout = (TextView)findViewById(R.id.acc_payout);
		sipbox_smc = (SipBox)findViewById(R.id.sipbox_smc);
		sipbox_smc.setCipherType(SystemConfig.CIPHERTYPE);
		sipbox_active = (SipBox)findViewById(R.id.sipbox_active);
		sipbox_active.setCipherType(SystemConfig.CIPHERTYPE);
		confirm = (Button)findViewById(R.id.confirm);
		smsbtn = (Button)findViewById(R.id.smsbtn);
		confirm.setOnClickListener(confirmClick);
		
		String commConversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		
		randomNumber=SafetyDataCenter.getInstance().getRandomNumer();
		
		usbKeytext = (UsbKeyText)findViewById(R.id.sip_usbkey);
		usbKeytext.Init(commConversationId, randomNumber, securityMap,this);
		isOtp = usbKeytext.getIsOtp();
		isSms = usbKeytext.getIsSmc();
		//动态口令
		if (isOtp) {
			((LinearLayout)findViewById(R.id.ll_active_code)).setVisibility(View.VISIBLE);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(sipbox_active, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, SafetyDataCenter.getInstance().getRandomNumer(), this);
//			sipbox_active.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			sipbox_active.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			sipbox_active.setId(10002);
//			sipbox_active.setBackgroundResource(R.drawable.bg_for_edittext);
//			sipbox_active.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			sipbox_active.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			sipbox_active.setSingleLine(true);
//			sipbox_active.setSipDelegator(this);
//			sipbox_active.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			sipbox_active.setRandomKey_S(SafetyDataCenter.getInstance().getRandomNumer());
		}
		// 手机交易码
		if (isSms) {
			((LinearLayout)findViewById(R.id.ll_smc)).setVisibility(View.VISIBLE);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(sipbox_smc, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, SafetyDataCenter.getInstance().getRandomNumer(), this);
//			sipbox_smc.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			sipbox_smc.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			sipbox_smc.setId(10002);
//			sipbox_smc.setBackgroundResource(R.drawable.bg_for_edittext);
//			sipbox_smc.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			sipbox_smc.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			sipbox_smc.setSingleLine(true);
//			sipbox_smc.setSipDelegator(this);
//			sipbox_smc.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			sipbox_smc.setRandomKey_S(SafetyDataCenter.getInstance().getRandomNumer());
		}
		SmsCodeUtils.getInstance().addSmsCodeListner(smsbtn,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 发送验证码到手机
						sendMSCToMobile();
					}
				});
		//initSmcAndOtp();
	}
	
	@SuppressWarnings("unchecked")
	private void initParamsInfo(){
		Map<String, Object> info = SafetyDataCenter.getInstance().getHoldProDetail();
		Map<String, Object> insuranceNewVerify = SafetyDataCenter.getInstance().getInsuranceNewVerify();
		securityMap= SafetyDataCenter.getInstance().getInsuranceNewVerify();	
		if(info != null && insuranceNewVerify != null){
			safetyCompany = (String)info.get(Safety.SAFETY_HOLD_INSU_NAME);
			productName = (String)info.get(Safety.SAFETY_HOLD_RISK_NAME);
			safetyApplicantName = (String)info.get(Safety.SAFETY_HOLD_APPNAME);  
			currencyStr = (String)info.get(Safety.SAFETY_HOLD_CURRENCY);  
			insuranceFeeAmount = (String)info.get(Safety.SAFETY_HOLD_RISK_PREM);  
			accPayout = (String)insuranceNewVerify.get(Acc.ACC_ACCOUNTNUMBER_RES);
			accId = (String)insuranceNewVerify.get(Acc.ACC_ACCOUNTID_RES);
			factorList = (List<Map<String, Object>>) insuranceNewVerify.get(Acc.LOSSCONFIRM_ACC_FACTORLIST_RES);
		
//			smcTrigerInterval = (String) insuranceNewVerify.get(Safety.SMC_TRIGER_INTERVAL);
//			_plainData = (String) insuranceNewVerify.get(Safety._PLAIN_DATA);
//			_certDN = (String) insuranceNewVerify.get(Safety._CERT_DN);
			riskCode = (String)info.get(Safety.SAFETY_HOLD_RISK_CODE);
			riskName = (String)info.get(Safety.SAFETY_HOLD_RISK_NAME);
			insuId = (String)info.get(Safety.SAFETY_HOLD_INSU_ID);
			insuName = (String)info.get(Safety.SAFETY_HOLD_INSU_NAME);
			riskPrem = (String)info.get(Safety.SAFETY_HOLD_RISK_PREM);
			polEffDate = (String)info.get(Safety.SAFETY_HOLD_POL_EFF_DATE);
			endDate = (String)info.get(Safety.SAFETY_HOLD_POL_END_DATE);
			benName = (String)info.get(Safety.SAFETY_HOLD_BENNAME);
			benIdNo = (String)info.get(Safety.SAFETY_HOLD_BENIDNO);
			continueFlag = (String)info.get(Safety.SAFETY_HOLD_CONTINUE_FLAG);
			payMethod = (String)insuranceNewVerify.get(Safety.PAY_METHOD);
		}
		Map<String, Object> insuranceContinueQuery = SafetyDataCenter.getInstance().getInsuranceContinueQuery();
		if(insuranceContinueQuery != null){
			transDate = (String)insuranceContinueQuery.get(Safety.SAFETY_HOLD_TRANS_DATE);
			transAccNo = (String)insuranceContinueQuery.get(Safety.SAFETY_HOLD_TRANS_ACCNO);
		}
	}
	
	private void setViewInfos(){
		safety_company.setText(StringUtil.valueOf1(safetyCompany));
		product_name.setText(StringUtil.valueOf1(productName));
		safety_applicant_name.setText(StringUtil.valueOf1(safetyApplicantName));
		currency.setText(StringUtil.valueOf1(LocalData.Currency.get(currencyStr)));
		safety_insurance_fee_amount.setText(StringUtil.parseStringCodePattern(currencyStr, insuranceFeeAmount, 2));
		acc_payout.setText(StringUtil.getForSixForString(accPayout));
	}
	
	OnClickListener confirmClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			checkDate();
			/*if(validation()){
				if (isSms) {
					try {
						smcStr = sipbox_smc.getValue().getEncryptPassword();
						smc_password_RC = sipbox_smc.getValue()
								.getEncryptRandomNum();
					} catch (CodeException e) {
						LogGloble.exceptionPrint(e);
					}

				}
				if (isOtp) {
					try {
						otpStr = sipbox_active.getValue()
								.getEncryptPassword();
						otp_password_RC = sipbox_active.getValue()
								.getEncryptRandomNum();
					} catch (CodeException e) {
						LogGloble.exceptionPrint(e);
					}

				}
				// TODO
				requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
						.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
				BiiHttpEngine.showProgressDialog();
			}*/
		}
	};
	/** 验证动态口令 */
	private void checkDate() {
		/** 音频Key安全工具认证 */
		usbKeytext.checkDataUsbKey(sipbox_active, sipbox_smc,
				new IUsbKeyTextSuccess() {

					@Override
					public void SuccessCallBack(String result, int errorCode) {
						// TODO Auto-generated method stub

						BaseHttpEngine.showProgressDialog();
						String commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap()
								.get(ConstantGloble.CONVERSATION_ID);
						requestPSNGetTokenId(commConversationId);

					}
				});

	}
	
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		requestSystemDateTime();
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		requestPsnInsuranceNewSubmit();
	}

	/**
	 * 新单投保提交交易
	 */
	public void requestPsnInsuranceNewSubmit() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Safety.SafetyInsuranceNewSubmit);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Safety.SAFETY_HOLD_CONTINUE_FLAG, (continueFlag != null && 
				continueFlag.equals("1") ? "true" : "false"));
		map.put(Safety.SAFETY_HOLD_TRANS_DATE, transDate);
		map.put(Safety.SAFETY_HOLD_TRANS_ACCNO, transAccNo);
		map.put(Safety.SAFETY_HOLD_RISK_CODE, riskCode);
		map.put(Safety.SAFETY_HOLD_RISK_NAME, riskName);
		map.put(Safety.SAFETY_HOLD_INSU_ID, insuId);
		map.put(Safety.SAFETY_HOLD_INSU_NAME, insuName);
		map.put(Safety.SAFETY_HOLD_RISK_UNIT, 1);
		map.put(Safety.SAFETY_HOLD_RISK_PREM, riskPrem);
		map.put(Safety.SAFETY_HOLD_POL_EFF_DATE, polEffDate);
		map.put(Safety.SAFETY_HOLD_POL_END_DATE, endDate);
		map.put(Safety.PAY_METHOD, payMethod);//payMethod
		map.put(Safety.SAFETY_HOLD_CURRENCY, currencyStr);
		map.put(Safety.ACC_ID, accId);
		map.put(Safety.SAFETY_HOLD_BENNAME, benName);
		map.put(Safety.SAFETY_HOLD_BENIDNO, benIdNo);
		/*if (isOtp) {
			map.put(Comm.Otp, otpStr);
			map.put(Comm.Otp_Rc, otp_password_RC);

		}
		if (isSms) {
			map.put(Comm.Smc, smcStr);
			map.put(Comm.Smc_Rc, smc_password_RC);
		}*/
		map.put(SBRemit.TOKEN, BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID));
		/** 安全工具参数获取 */
		usbKeytext.InitUsbKeyResult(map);
		
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnInsuranceNewSubmitCallBack");
	}

	/**
	 * 新单投保提交交易回调
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnInsuranceNewSubmitCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> atmmap = (Map<String, Object>) (biiResponseBody
				.getResult());
		if (StringUtil.isNullOrEmpty(atmmap)) {
			return;
		}
		SafetyDataCenter.getInstance().setInsuranceNewSubmit(atmmap);
		startActivityForResult(new Intent(this, SafetyInsuContSuccActivity.class), 1);
	}
	
	private boolean validation(){
		// 动态口令
		RegexpBean sipRegexpBean = new RegexpBean(getString(R.string.active_code_regex),
				sipbox_active.getText().toString(),
				ConstantGloble.SIPOTPPSW);
		RegexpBean sipSmcpBean = new RegexpBean(getString(R.string.acc_smc_regex),
				sipbox_smc.getText().toString(), ConstantGloble.SIPSMCPSW);
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		if (isSms) {
			lists.add(sipSmcpBean);
		}
		if (isOtp) {
			lists.add(sipRegexpBean);
		}
		if (RegexpUtils.regexpDate(lists)) {// 校验通过
			return true;
		}
		return false;
	}
	
	
	/** 判断是动态口令还是手机交易码 */
	@SuppressWarnings("unchecked")
	public void initSmcAndOtp() {
		/*if (!StringUtil.isNullOrEmpty(factorList)) {
			for (int i = 0; i < factorList.size(); i++) {
				Map<String, Object> itemMap = factorList.get(i);
				Map<String, String> securityMap = (Map<String, String>) itemMap
						.get(Inves.FIELD);
				String name = securityMap.get(Inves.NAME);
				if (Inves.Smc.equals(name)) {
					isSms = true;
					((LinearLayout)findViewById(R.id.ll_smc)).setVisibility(View.VISIBLE);
				} else if (Inves.Otp.equals(name)) {
					isOtp = true;
					((LinearLayout)findViewById(R.id.ll_active_code)).setVisibility(View.VISIBLE);
				}
			}
		}*/
		// 加密控件设置随机数, 
		sipbox_smc.setRandomKey_S(SafetyDataCenter.getInstance().getRandomNumer());
		sipbox_active.setRandomKey_S(SafetyDataCenter.getInstance().getRandomNumer());
	}

	
	public void sendMSCToMobile() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.SET_SENDMSC);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "sendMSCToMobileCallback");
	}

	public void sendMSCToMobileCallback(Object resultObj) {
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;
		default:
			break;
		}
	}
}
