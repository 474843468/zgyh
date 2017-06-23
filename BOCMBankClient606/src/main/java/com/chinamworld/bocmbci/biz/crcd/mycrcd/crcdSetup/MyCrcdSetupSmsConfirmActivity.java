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
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
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
import com.chinamworld.bocmbci.biz.crcd.mycrcd.MyMasterAndSupplSetActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 设置交易短信确认
 * 
 * @author huangyuchao
 * 
 */
public class MyCrcdSetupSmsConfirmActivity extends CrcdBaseActivity {

	private View view;

	private static final int REQUEST_RANDOM = 1;
	private static final int REQUEST_RESULT = 2;

	private static int request_num;

	TextView finc_accNumber, finc_accId, finc_fincName;

	Button lastButton, sureButton;

	EditText et_finc_password;

	// LinearLayout ll_sip;
	/** 加密控件 */
//	private SipBox sipBox = null;

	/** 输入框密码 */
//	private String password = "";
	/** 加密之后加密随机数 */
//	private String password_RC = "";

	/** 安全因子数组 */
	public List<Map<String, Object>> factorList;
	Map<String, Object> factorMap;

	/** 动态口令布局 */
	private LinearLayout ll_active_code;
	/** 手机交易码布局 */
	private LinearLayout ll_smc;

	/** 动态口令 */
//	private String otpStr;
	/** 手机交易码 */
//	private String smcStr;
	/** 加密之后加密随机数 */
//	private String otp_password_RC = "";
//	private String smc_password_RC = "";
	private String accountNumber = null;
	private String subaccountNumber = null;
	
	private String accountId = null;
	private	String currencyCode = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_setup_jiaoyi_message));
		view = addView(R.layout.crcd_setup_sms_message_confirm);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);

		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		request_num = REQUEST_RANDOM;
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		subaccountNumber= getIntent().getStringExtra(Crcd.CRCD_SUPPLYCARD_RES);
		currencyCode= getIntent().getStringExtra(Crcd.CRCD_CURRENCYCODE);
		// BaseHttpEngine.showProgressDialog();
		// requestForRandomNumber();
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		init();
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
		sipBoxActiveCode.setRandomKey_S(randomNumber);
		sipBoxSmc.setRandomKey_S(randomNumber);
		// init();
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
		initOptAndSmc();

		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getResources().getString(R.string.mycrcd_write_step_info),
						this.getResources().getString(R.string.mycrcd_setup_info),
						this.getResources().getString(R.string.mycrcd_service_setup_success) });
		StepTitleUtils.getInstance().setTitleStep(2);

		finc_accNumber = (TextView) view.findViewById(R.id.finc_accNumber);
		finc_accId = (TextView) view.findViewById(R.id.finc_accId);
		finc_fincName = (TextView) view.findViewById(R.id.finc_fincName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_fincName);
		finc_accNumber.setText(StringUtil.getForSixForString(accountNumber));
		finc_accId.setText(StringUtil.getForSixForString(subaccountNumber));
		finc_fincName.setText(MyMasterAndSupplSetActivity.strSendMessMode);

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
				request_num = REQUEST_RESULT;
				try {
					pSNGetTokenId();

				} catch (Exception e) {
					LogGloble.exceptionPrint(e);

				}
			}
		});

	}

	/** 加密控件——动态口令 */
	private SipBox sipBoxActiveCode = null;
	/** 加密控件——手机交易码 */
	private SipBox sipBoxSmc = null;
	/** 是否上传动态口令 */
	private boolean isOtp = false;
	/** 是否上传短信验证码 */
	private boolean isSmc = false;

	public void initOptAndSmc() {
		LinearLayout.LayoutParams param = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		// 动态口令
		ll_active_code = (LinearLayout) view.findViewById(R.id.ll_active_code);

		LinearLayout ll_activecode_sip = (LinearLayout) view.findViewById(R.id.ll_activecode_sip);
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
		// ll_activecode_sip.addView(sipBoxActiveCode);

		// 手机交易码
		ll_smc = (LinearLayout) view.findViewById(R.id.ll_smc);
		RelativeLayout ll_smc_sip = (RelativeLayout) view.findViewById(R.id.ll_smc_sip);
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
		// ll_smc_sip.addView(sipBoxSmc);

		Button smsBtn = (Button) view.findViewById(R.id.smsbtn);
		SmsCodeUtils.getInstance().addSmsCodeListner(smsBtn, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 发送验证码到手机
				sendMSCToMobile();
			}
		});

		showOptOrSmc();
	}

	public void showOptOrSmc() {
		/** 判断是动态口令还是手机交易码 */
		if (!StringUtil.isNullOrEmpty(factorList)) {
			for (int i = 0; i < factorList.size(); i++) {
				Map<String, Object> itemMap = factorList.get(i);
				Map<String, Object> securityMap = (Map<String, Object>) itemMap.get(Crcd.FIELD);
				String name = String.valueOf(securityMap.get(Crcd.NAME));
				if (Comm.Otp.equals(name)) {
					isOtp = true;
					ll_active_code.setVisibility(View.VISIBLE);
				} else if (Comm.Smc.equals(name)) {
					isSmc = true;
					ll_smc.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	@Override
	public void aquirePSNGetTokenIdCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.aquirePSNGetTokenIdCallBack(resultObj);
		// 附属卡短信设置处理
		psnCrcdAppertainMessSetResult();
	}

	public void psnCrcdAppertainMessSetResult() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDAPPERTAINNMESSSETRESULT);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		map.put(Crcd.CRCD_CADRNO_RES,subaccountNumber );
//		map.put(Crcd.CRCD_APPLICATIONID_RES, MySupplymentDetailActivity.applicationId);
		map.put(Crcd.CRCD_SENDMESSMODE, MyMasterAndSupplSetActivity.sendMessMode + "");
		map.put(Crcd.CRCD_TOKEN, tokenId);
		// map.put(Comm.Otp, password);
		// SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdAppertainMessSetResultCallBack");
	}

	public static Map<String, Object> returnMap;

	public void psnCrcdAppertainMessSetResultCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		returnMap = (Map<String, Object>) body.getResult();

		Intent it = new Intent(MyCrcdSetupSmsConfirmActivity.this, MyCrcdSetupSmsSuccessActivity.class);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_SUPPLYCARD_RES, subaccountNumber);
		
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
