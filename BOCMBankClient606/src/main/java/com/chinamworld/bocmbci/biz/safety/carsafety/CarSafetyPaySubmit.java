package com.chinamworld.bocmbci.biz.safety.carsafety;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Inves;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyBaseActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyConstant;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 车险缴费提交页面
 * 
 * @author Zhi
 */
public class CarSafetyPaySubmit extends SafetyBaseActivity {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** 主显示视图 */
	private View mMainView;
	/** 随机数 */
	private String random;
	/** 手机验证码sip */
	private SipBox otpSipBxo;
	/** 短信验证码sip*/
	private SipBox smcSipBxo;
	/**中银E盾*/
	private UsbKeyText usbKeyText;
	/** 标识是否有短息验证码、动态口令 */
	private boolean isOtp = false;
	private boolean isSmc = false;
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.safety_msgfill_title);
		mMainView = LayoutInflater.from(this).inflate(R.layout.safety_carsafety_pay_submit, null);
		findViewById(R.id.sliding_body).setPadding(0, 0, 0, 0);
		initView();
		addView(mMainView);
	}
	
	private void initView() {
		setStep3();
		((TextView) mMainView.findViewById(R.id.tv_product_type)).setText("车险");
		((TextView) mMainView.findViewById(R.id.tv_company_name)).setText("中银保险有限公司");
		((TextView) mMainView.findViewById(R.id.tv_buyer_name)).setText((String) SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Inves.CUSTOMERNAME));
		((TextView) mMainView.findViewById(R.id.tv_bizhong)).setText("人民币");
		((TextView) mMainView.findViewById(R.id.tv_amount)).setText(StringUtil.parseStringPattern((String) SafetyDataCenter.getInstance().getMapAutoInsuranceCalculation().get(Safety.TOTALREALPREMIUM), 2));
		int accSelectPosition = (Integer) SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get("accSelectPosition");
		Map<String, Object> accMap = SafetyDataCenter.getInstance().getAcctList().get(accSelectPosition);
		String accInfo = StringUtil.getForSixForString((String) accMap.get(Comm.ACCOUNTNUMBER));
		((TextView) mMainView.findViewById(R.id.tv_payacct)).setText(accInfo);

		otpSipBxo = (SipBox) mMainView.findViewById(R.id.et_cecurity_ps);
		smcSipBxo = (SipBox) mMainView.findViewById(R.id.sip_sms);
		isOtp = false;
		isSmc = false;
		
		mMainView.findViewById(R.id.btnConfirm).setOnClickListener(submitClick);
		random = (String) SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(SafetyConstant.RANDOMNUMBER);
		initSipBox(SafetyDataCenter.getInstance().getMapAutoInsurancePayVerify());
	}
	
	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox(Map<String, Object> result){
		usbKeyText = (UsbKeyText) mMainView.findViewById(R.id.sip_usbkey);
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
			SmsCodeUtils.getInstance().addSmsCodeListner(btnSendSms,smcOnclickListener);
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
	
	@Override
	public boolean doHttpErrorHandler(String method,BiiError biiError) {
		Intent intent = new Intent(this, CarSafetyPayLose.class);
		startActivityForResult(intent, 4);
		return true;
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
	
	private void setStep3() {
		mMainView.findViewById(R.id.layout_step1).setBackgroundResource(R.drawable.safety_step3);
		mMainView.findViewById(R.id.layout_step2).setBackgroundResource(R.drawable.safety_step2);
		mMainView.findViewById(R.id.layout_step3).setBackgroundResource(R.drawable.safety_step5);
		((TextView) mMainView.findViewById(R.id.index1)).setTextColor(getResources().getColor(R.color.gray));
		((TextView) mMainView.findViewById(R.id.index2)).setTextColor(getResources().getColor(R.color.gray));
		((TextView) mMainView.findViewById(R.id.index3)).setTextColor(getResources().getColor(R.color.red));
		((TextView) mMainView.findViewById(R.id.text1)).setTextColor(getResources().getColor(R.color.gray));
		((TextView) mMainView.findViewById(R.id.text2)).setTextColor(getResources().getColor(R.color.gray));
		((TextView) mMainView.findViewById(R.id.text3)).setTextColor(getResources().getColor(R.color.red));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 4) {
			setResult(4);
			finish();
		}
	}
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------控件事件-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private OnClickListener smcOnclickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			sendSMSCToMobile();
		}
	};
	
	OnClickListener submitClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			submitRegexp();
		}
	};
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO ---------------------------------------网络请求与回调方法--------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> map = SafetyDataCenter.getInstance().getMapAutoInsuranceCreatPolicy();
		Map<String, Object> userInput = SafetyDataCenter.getInstance().getMapCarSafetyUserInput();
		params.put(Safety.TRANDATE, map.get(Safety.TRANDATE));
		params.put(Safety.JQXTRACENO, map.get(Safety.JQXTRACENO));
		params.put(Safety.BIZTRACENO, map.get(Safety.BIZTRACENO));
		params.put(Safety.TRADEAMOUNT, map.get(Safety.AMOUNT));
		params.put(Comm.ACCOUNT_ID, SafetyDataCenter.getInstance().getAcctList().get((Integer) userInput.get("accSelectPosition")).get(Comm.ACCOUNT_ID));
		params.put(ConstantGloble.PUBLIC_TOKEN, BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID));
		// 币种默认上送人民币元“001”
		params.put(Safety.CURRENCYCODE, "001");
		params.put(Safety.APPNTNAME, userInput.get(Inves.CUSTOMERNAME));
		params.put(Safety.INSUREDNAME, userInput.get(Safety.BEN_NAME));
		params.put(Safety.INSUREDIDENTIFYNO, userInput.get(Safety.BEN_IDNUM));
		// P501批次保险公司代码默认上送中银保险2001
		params.put(Safety.INSUCOMID, "2001");
		params.put(Safety.INSUCOMNAME, "中银保险有限公司");
		params.put(Safety.TOTALFEERATE, map.get(Safety.TOTALFEERATE));
		params.put(Safety.TOTALFEE, map.get(Safety.TOTALFEE));
		/**安全工具参数获取*/
		usbKeyText.InitUsbKeyResult(params);
		SipBoxUtils.setSipBoxParams(params);
		httpTools.requestHttp(Safety.METHOD_PSNAUTOINSURANCEPAYSUBMIT, "requestPsnAutoInsurancePaySubmitCallBack", params, true);
		httpTools.registErrorCode(Safety.METHOD_PSNAUTOINSURANCEPAYSUBMIT,"IBAS.T1135");
	}
	
	/** 车险投保缴费提交交易回调方法 */
	@SuppressWarnings("unchecked")
	public void requestPsnAutoInsurancePaySubmitCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = this.getHttpTools().getResponseResult(resultObj);
		
		if (StringUtil.isNullOrEmpty(resultMap) || StringUtil.isNullOrEmpty(resultMap.get(Safety.JQXPOLICYNO))) {
			// 交强险保单号如果为空，跳到未明界面
			startActivityForResult((new Intent(this, CarSafetyPayLose.class)), 4);
			return;
		} else {
			if (StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getMapAutoInsuranceQueryCompulsory().get(Safety.BIZINSBEGINDATE))) {
				// 交易成功
				SafetyDataCenter.getInstance().setMapAutoInsurancePaySubmit(resultMap);
				startActivityForResult(new Intent(this, CarSafetyPaySuccess.class), 4);
			} else {
				if (StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get("bizList"))) {
					// 交易成功
					SafetyDataCenter.getInstance().setMapAutoInsurancePaySubmit(resultMap);
					startActivityForResult(new Intent(this, CarSafetyPaySuccess.class), 4);
				} else {
					if (StringUtil.isNullOrEmpty(resultMap.get(Safety.BIZPOLICYNO))) {
						// 商业险保单号如果为空，跳到未明界面
						startActivityForResult((new Intent(this, CarSafetyPayLose.class)), 4);
						return;
					} else {
						// 交易成功
						SafetyDataCenter.getInstance().setMapAutoInsurancePaySubmit(resultMap);
						startActivityForResult(new Intent(this, CarSafetyPaySuccess.class), 4);
					}
				}
			}
		}
	}
}