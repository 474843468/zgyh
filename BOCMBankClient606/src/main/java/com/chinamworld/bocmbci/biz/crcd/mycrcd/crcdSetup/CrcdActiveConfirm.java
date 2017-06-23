package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
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
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.GetPhoneInfo;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 信用卡激活确认
 * 
 * @author huangyuchao
 * 
 */
public class CrcdActiveConfirm extends CrcdBaseActivity {

	/** 信用卡激活确认 */
	private View view;

	Button nextBtn;

	TextView tv_cardNumber;
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
	/** 加密控件——动态口令 */
	private SipBox sipBoxActiveCode = null;
	/** 加密控件——手机交易码 */
	private SipBox sipBoxSmc = null;
	/**中银E盾*/
	private UsbKeyText usbKeyBox;
	/** 是否上传动态口令 */
	private boolean isOtp = false;
	/** 是否上传短信验证码 */
	private boolean isSmc = false;
	
	/** 密码 */
	private String password1;
	/** 确认密码 */
	private String password2;
	private String password_RC1;
	private String password_RC2;
	String passWordIsSet;
	/** 加密控件里的随机数 */
	protected String randomNumber;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_creditcard_active_title));
		if (view == null) {
			view = addView(R.layout.crcd_active_info_confirm);
		}
		
		passWordIsSet=getIntent().getStringExtra("passWordIsSet");
		if("0".equals(passWordIsSet)){			
			
			password1 = getIntent().getStringExtra(Crcd.CRCD_QUERYPW_RES);
			password_RC1 = getIntent().getStringExtra(Crcd.CRCD_QUERYPW_RC_RES);
			password2 = getIntent().getStringExtra(Crcd.CRCD_QUERYPWCONFIRM_RES);
			password_RC2 = getIntent().getStringExtra(Crcd.CRCD_QUERYPWCONFIRM_RC_RES);
			randomNumber= getIntent().getStringExtra("randomNumber");
			init();
		}else{
			requestForRandomNumber();	
			BaseHttpEngine.showProgressDialog();
		}
		
		
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});

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

	public void init() {

		factorMap = CrcdActiveInfo.resultMap;
		factorList = (List<Map<String, Object>>) factorMap.get(Crcd.FACTORLIST);
		commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));

//		initOptAndSmc()
		/** 跟据返回初始化中银E盾加密控件 */
		initSipBox();

		tv_cardNumber = (TextView) findViewById(R.id.tv_cardNumber);
		tv_cardNumber.setText(StringUtil.getForSixForString(CrcdActiveInfo.cardNumber));

		nextBtn = (Button) findViewById(R.id.nextButton);
		nextBtn.setOnClickListener(nextClick);
	}
	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox(){
		String mmconversationId = (String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeyBox = (UsbKeyText)view.findViewById(R.id.sip_usbkey);
		usbKeyBox.Init(mmconversationId, randomNumber, factorMap, this);
		isOtp = usbKeyBox.getIsOtp();
		isSmc = usbKeyBox.getIsSmc();
		initOptAndSmc();
	}
	public void initOptAndSmc() {
		if(isOtp){
		// 动态口令
		ll_active_code = (LinearLayout) view.findViewById(R.id.ll_active_code);
		ll_active_code.setVisibility(View.VISIBLE);
		sipBoxActiveCode = (SipBox) view.findViewById(R.id.sipbox_active);
//		sipBoxActiveCode.setCipherType(SystemConfig.CIPHERTYPE);
		// sipBoxActiveCode.setLayoutParams(param);
		sipBoxActiveCode.setTextColor(getResources().getColor(android.R.color.black));
		// add by 2016年9月12日 luqp 修改
		sipBoxActiveCode.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
		SipBoxUtils.initSipBoxWithTwoType(sipBoxActiveCode, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		sipBoxActiveCode.setGravity(Gravity.CENTER_VERTICAL);
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
//		/**设置随机数*/
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
		// sipBoxSmc.setLayoutParams(param);
		// sipBoxSmc.setGravity(Gravity.CENTER_VERTICAL);
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
//		/**设置随机数*/
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

	static String cardNumber;

	OnClickListener nextClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			cardNumber = tv_cardNumber.getText().toString();
			// 动态口令
//			RegexpBean sipSmcpBean = new RegexpBean(getString(R.string.acc_smc_regex), sipBoxSmc.getText().toString(),
//					ConstantGloble.SIPSMCPSW);
//			RegexpBean sipRegexpBean = new RegexpBean(getString(R.string.active_code_regex), sipBoxActiveCode.getText()
//					.toString(), ConstantGloble.SIPOTPPSW);
//
//			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
//			if (isSmc) {
//				lists.add(sipSmcpBean);
//			}
//			if (isOtp) {
//				lists.add(sipRegexpBean);
//			}
//
//			if (RegexpUtils.regexpDate(lists)) {// 校验通过
//				if (isSmc) {
//					try {
//						smcStr = sipBoxSmc.getValue().getEncryptPassword();
//						smc_password_RC = sipBoxSmc.getValue().getEncryptRandomNum();
//					} catch (CodeException e) {
//						LogGloble.exceptionPrint(e);
//					}
//
//				}
//				if (isOtp) {
//					try {
//						otpStr = sipBoxActiveCode.getValue().getEncryptPassword();
//						otp_password_RC = sipBoxActiveCode.getValue().getEncryptRandomNum();
//					} catch (CodeException e) {
//						LogGloble.exceptionPrint(e);
//					}
//
//				}
//
//				pSNGetTokenId();
//			}
			usbKeyBox.checkDataUsbKey(sipBoxActiveCode, sipBoxSmc, new IUsbKeyTextSuccess() {
				
				@Override
				public void SuccessCallBack(String result, int errorCode) {
					// TODO Auto-generated method stub
					pSNGetTokenId();
				}
			});
			
		}
	};

	/**
	 * 获取tokenId
	 */
	public void pSNGetTokenId() {
		BiiHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenId");
	}

	/**
	 * 处理获取tokenId的数据得到tokenId
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void aquirePSNGetTokenId(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(tokenId)) {
			return;
		}

		// 信用卡激活
		psnCrcdActivate();
	}

	public void psnCrcdActivate() {
		// 通讯开始,展示通讯框
		// BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDACTIVATE);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_TOACTIVEACT, CrcdActiveInfo.cardNumber);
		params.put(Crcd.CRCD_TOKEN, tokenId);
		
		if("0".equals(passWordIsSet)){
			params.put(Crcd.CRCD_QUERYPW_RES, password1);//
			params.put(Crcd.CRCD_QUERYPWCONFIRM_RES, password2);//
			params.put(Crcd.CRCD_QUERYPW_RC_RES, password_RC1);//
			params.put(Crcd.CRCD_QUERYPWCONFIRM_RC_RES, password_RC2);//
		}
		
//		if (isOtp) {
//			params.put(Comm.Otp, otpStr);
//			params.put(Comm.Otp_Rc, otp_password_RC);
//		}
//		if (isSmc) {
//			params.put(Comm.Smc, smcStr);
//			params.put(Comm.Smc_Rc, smc_password_RC);
//		}
		/**安全工具参数获取*/
		usbKeyBox.InitUsbKeyResult(params);
		SipBoxUtils.setSipBoxParams(params);
		GetPhoneInfo.initActFirst(this);
		GetPhoneInfo.addPhoneInfo(params);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdActivatePreCallBack");
	}

	public  Map<String, Object> returnMap;

	public void psnCrcdActivatePreCallBack(Object resultObj) {

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		returnMap = (Map<String, Object>) body.getResult();
		BaseHttpEngine.dissMissProgressDialog();
		Intent it = new Intent(CrcdActiveConfirm.this, CrcdActiveSuccess.class);
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
