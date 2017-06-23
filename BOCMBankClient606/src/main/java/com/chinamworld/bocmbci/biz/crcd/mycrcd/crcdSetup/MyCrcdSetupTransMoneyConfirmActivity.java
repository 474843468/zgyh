package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.MyMasterAndSupplSetActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 设置交易限额确认
 * 
 * @author huangyuchao
 * 
 */
public class MyCrcdSetupTransMoneyConfirmActivity extends CrcdBaseActivity {

	private View view;
	TextView finc_accNumber, finc_accId, tv_cardNumber, finc_fincName;
	Button lastButton, sureButton;

	EditText et_finc_password;

	/** 安全因子数组 */
	public List<Map<String, Object>> factorList;
	Map<String, Object> factorMap;

	/** 动态口令布局 */
	private LinearLayout ll_active_code;
	/** 手机交易码布局 */
	private LinearLayout ll_smc;

	/** 动态口令 */
	private String otpStr;
	/** 手机交易码 */
	private String smcStr;
	/** 加密之后加密随机数 */
	private String otp_password_RC = "";
	private String smc_password_RC = "";
	/** 币种代码 */
	private String currencyCode = null;
	private String accountNumber = null;
	/** 附属卡卡号 */
	private String subaccountNumber = null;
	private String accountId = null;
	private String commConversationId = null;
	private String amount = null;
	/** 币种代码名称 */
	private	String strCurrencyCode = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_setup_jiaoe_money));
		view = addView(R.layout.crcd_setup_trans_money_confirm);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		currencyCode = getIntent().getStringExtra(ConstantGloble.CRCD_CODE);
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		subaccountNumber= getIntent().getStringExtra(Crcd.CRCD_SUPPLYCARD_RES);
		amount = getIntent().getStringExtra(Crcd.CRCD_AMOUNT);
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		strCurrencyCode= getIntent().getStringExtra(Crcd.CRCD_CURRENCYCODE);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		requestForRandomNumber();
//		init();
		BaseHttpEngine.showProgressDialog();
//		requestForRandomNumber();
	}

	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "queryRandomNumberCallBack");
	}

	/** 加密控件里的随机数 */
	protected String randomNumber;

	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		randomNumber = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(randomNumber)) {
			return;
		}
		// 加密控件设置随机数
//		sipBoxActiveCode.setRandomKey_S(randomNumber);
//		sipBoxSmc.setRandomKey_S(randomNumber);
		init();
	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	/** 初始化界面 */
	private void init() {

		factorMap = MyMasterAndSupplSetActivity.returnMap;
		factorList = (List<Map<String, Object>>) factorMap.get(Crcd.FACTORLIST);
		commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));

//		initOptAndSmc()
		/** 跟据返回初始化中银E盾加密控件 */
		initSipBox();

		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getResources().getString(R.string.mycrcd_setup_jiaoe_money),
						this.getResources().getString(R.string.mycrcd_setup_info),
						this.getResources().getString(R.string.mycrcd_service_setup_success) });
		StepTitleUtils.getInstance().setTitleStep(2);

		finc_accNumber = (TextView) view.findViewById(R.id.finc_accNumber);
		finc_accId = (TextView) view.findViewById(R.id.finc_accId);
		tv_cardNumber = (TextView) view.findViewById(R.id.tv_cardNumber);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_cardNumber);
		finc_fincName = (TextView) view.findViewById(R.id.finc_fincName);

		finc_accNumber.setText(StringUtil.getForSixForString(accountNumber));
		finc_accId.setText(StringUtil.getForSixForString(subaccountNumber));
		tv_cardNumber.setText(strCurrencyCode);
		finc_fincName.setText(StringUtil.parseStringCodePattern(currencyCode, amount, 2));

		lastButton = (Button) view.findViewById(R.id.lastButton);
		lastButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		sureButton = (Button) view.findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 动态口令
//				RegexpBean sipSmcpBean = new RegexpBean(getString(R.string.acc_smc_regex), sipBoxSmc.getText()
//						.toString(), ConstantGloble.SIPSMCPSW);
//				RegexpBean sipRegexpBean = new RegexpBean(getString(R.string.active_code_regex), sipBoxActiveCode
//						.getText().toString(), ConstantGloble.SIPOTPPSW);
//
//				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
//				if (isSmc) {
//					lists.add(sipSmcpBean);
//				}
//				if (isOtp) {
//					lists.add(sipRegexpBean);
//				}
//
//				if (RegexpUtils.regexpDate(lists)) {// 校验通过
//					if (isSmc) {
//						try {
//							smcStr = sipBoxSmc.getValue().getEncryptPassword();
//							smc_password_RC = sipBoxSmc.getValue().getEncryptRandomNum();
//						} catch (CodeException e) {
//							LogGloble.exceptionPrint(e);
//						}
//
//					}
//					if (isOtp) {
//						try {
//							otpStr = sipBoxActiveCode.getValue().getEncryptPassword();
//							otp_password_RC = sipBoxActiveCode.getValue().getEncryptRandomNum();
//						} catch (CodeException e) {
//							LogGloble.exceptionPrint(e);
//						}
//
//					}
//
//					pSNGetTokenId();
//				}
				/**安全工具数据校验*/
				checketDate();
				
			}
		});

	}
	
	/**安全工具数据校验*/
	private void checketDate(){
		usbKeyText.checkDataUsbKey(sipBoxActiveCode, sipBoxSmc, new IUsbKeyTextSuccess() {
			
			@Override
			public void SuccessCallBack(String result, int errorCode) {
				// TODO Auto-generated method stub
				BaseHttpEngine.showProgressDialog();
				pSNGetTokenId();
			}
		});
	}

	/** 加密控件——动态口令 */
	private SipBox sipBoxActiveCode = null;
	/** 加密控件——手机交易码 */
	private SipBox sipBoxSmc = null;
	/**中银E盾*/
	private UsbKeyText usbKeyText;
	/** 是否上传动态口令 */
	private boolean isOtp = false;
	/** 是否上传短信验证码 */
	private boolean isSmc = false;
	
	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox(){
		usbKeyText = (UsbKeyText)view.findViewById(R.id.sip_usbkey);
		usbKeyText.Init(commConversationId, randomNumber, factorMap, this);
		isOtp = usbKeyText.getIsOtp();
		isSmc = usbKeyText.getIsSmc();
		initOptAndSmc();
	}
	
	public void initOptAndSmc() {
		if(isOtp){
		// 动态口令
		ll_active_code = (LinearLayout) view.findViewById(R.id.ll_active_code);
		ll_active_code.setVisibility(View.VISIBLE);
		sipBoxActiveCode = (SipBox) view.findViewById(R.id.sipbox_active);
			// add by 2016年9月12日 luqp 修改
			sipBoxActiveCode.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
			sipBoxActiveCode.setTextColor(getResources().getColor(android.R.color.black));
			SipBoxUtils.initSipBoxWithTwoType(sipBoxActiveCode, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		sipBoxActiveCode.setCipherType(SystemConfig.CIPHERTYPE);
//		// sipBoxActiveCode.setLayoutParams(param);
//		sipBoxActiveCode.setTextColor(getResources().getColor(android.R.color.black));
//		// sipBoxActiveCode.setGravity(Gravity.CENTER_VERTICAL);
//		sipBoxActiveCode.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		sipBoxActiveCode.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		sipBoxActiveCode.setId(10002);
//		sipBoxActiveCode.setBackgroundResource(R.drawable.bg_for_edittext);
//		sipBoxActiveCode.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		sipBoxActiveCode.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//		sipBoxActiveCode.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
//		sipBoxActiveCode.setSingleLine(true);
//		sipBoxActiveCode.setSipDelegator(this);
//		sipBoxActiveCode.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		sipBoxActiveCode.setRandomKey_S(randomNumber);
		}
		// ll_activecode_sip.addView(sipBoxActiveCode);
		if(isSmc){
		// 手机交易码
		ll_smc = (LinearLayout) view.findViewById(R.id.ll_smc);
		ll_smc.setVisibility(View.VISIBLE);
		sipBoxSmc = (SipBox) view.findViewById(R.id.sipbox_smc);
			// add by 2016年9月12日 luqp 修改
			sipBoxSmc.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
			sipBoxSmc.setTextColor(getResources().getColor(android.R.color.black));
			SipBoxUtils.initSipBoxWithTwoType(sipBoxSmc, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		sipBoxSmc.setCipherType(SystemConfig.CIPHERTYPE);
//		// sipBoxSmc.setLayoutParams(param);
//		// sipBoxSmc.setGravity(Gravity.CENTER_VERTICAL);
//		sipBoxSmc.setTextColor(getResources().getColor(android.R.color.black));
//		sipBoxSmc.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		sipBoxSmc.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		sipBoxSmc.setId(10002);
//		sipBoxSmc.setBackgroundResource(R.drawable.bg_for_edittext);
//		sipBoxSmc.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		sipBoxSmc.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//		sipBoxSmc.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
//		sipBoxSmc.setSingleLine(true);
//		sipBoxSmc.setSipDelegator(this);
//		sipBoxSmc.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		sipBoxSmc.setRandomKey_S(randomNumber);
		// ll_smc_sip.addView(sipBoxSmc);

		Button smsBtn = (Button) view.findViewById(R.id.smsbtn);
		SmsCodeUtils.getInstance().addSmsCodeListner(smsBtn, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 发送验证码到手机
				sendMSCToMobile();
			}
		});
		}
//		showOptOrSmc();
	}

//	public void showOptOrSmc() {
//		/** 判断是动态口令还是手机交易码 */
//		if (!StringUtil.isNullOrEmpty(factorList)) {
//			for (int i = 0; i < factorList.size(); i++) {
//				Map<String, Object> itemMap = factorList.get(i);
//				Map<String, Object> securityMap = (Map<String, Object>) itemMap.get(Crcd.FIELD);
//				String name = String.valueOf(securityMap.get(Crcd.NAME));
//				if (Comm.Otp.equals(name)) {
//					isOtp = true;
//					ll_active_code.setVisibility(View.VISIBLE);
//				} else if (Comm.Smc.equals(name)) {
//					isSmc = true;
//					ll_smc.setVisibility(View.VISIBLE);
//				}
//			}
//		}
//	}

	@Override
	public void aquirePSNGetTokenIdCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.aquirePSNGetTokenIdCallBack(resultObj);

		psnCrcdAppertainTranSetResult();
	}

	public void psnCrcdAppertainTranSetResult() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDAPPERTAINSETRESULT);
		commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		map.put(Crcd.CRCD_CADRNO_RES,subaccountNumber );
	

//		map.put(Crcd.CRCD_APPLICATIONID_RES, MySupplymentDetailActivity.applicationId);
		map.put(Crcd.CRCD_CURRENCYCODE, MyMasterAndSupplSetActivity.currencyCode);
		map.put(Crcd.CRCD_AMOUNT, MyMasterAndSupplSetActivity.amount);
		map.put(Crcd.CRCD_TOKEN, tokenId);
//		if (isOtp) {
//			map.put(Comm.Otp, otpStr);
//			map.put(Comm.Otp_Rc, otp_password_RC);
//		}
//		if (isSmc) {
//			map.put(Comm.Smc, smcStr);
//			map.put(Comm.Smc_Rc, smc_password_RC);
//		}
		/**安全工具获取参数*/
		usbKeyText.InitUsbKeyResult(map);
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdAppertainTranSetResultCallBack");
	}

	public static Map<String, Object> returnMap;

	public void psnCrcdAppertainTranSetResultCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		returnMap = (Map<String, Object>) body.getResult();

		Intent it = new Intent(MyCrcdSetupTransMoneyConfirmActivity.this, MyCrcdSetupTransMoneySuccessActivity.class);
		it.putExtra(ConstantGloble.CRCD_CODE, currencyCode);
		it.putExtra(Crcd.CRCD_CURRENCYCODE, strCurrencyCode);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_SUPPLYCARD_RES, subaccountNumber);
		it.putExtra(Crcd.CRCD_AMOUNT, amount);
		
		// startActivity(it);
		startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
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
