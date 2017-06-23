package com.chinamworld.bocmbci.biz.bond.acct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
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
import com.chinamworld.bocmbci.bii.constant.Bond;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.bond.BondBaseActivity;
import com.chinamworld.bocmbci.biz.bond.BondConstant;
import com.chinamworld.bocmbci.biz.bond.BondDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
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
 * 开户信息确认页
 * 
 * @author panwe
 * 
 */
public class OpendAcctConfirmActivity extends BondBaseActivity implements
		OnClickListener {
	/** 主布局 */
	private View mainView;
	/** 标识是否有短息验证码、动态口令 */
	private boolean isOtp = false;
	private boolean isSmc = false;
	/** 加密控件 */
	private SipBox otpSipBxo;
	private SipBox smcSipBxo;
	/** 中银E盾 */
	private UsbKeyText usbKeyText;
	private String otpPassword;
	private String smcPassword;
	private String otpRandomNum;
	private String smcRandomNum;
	/** 资金账户id */
	private String acctId;
	private String acctNum;
	// 随机数
	private String randomNumber;
	private boolean isBuy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 添加布局
		mainView = (View) LayoutInflater.from(this).inflate(
				R.layout.bond_openacct_confirm, null);
		setTitle(this.getString(R.string.bond_acct_open_title));
		BondDataCenter.getInstance().addActivity(this);
		// 隐藏左侧菜单
		setLeftButtonPopupGone();
		// 隐藏底部tab
		setBottomTabGone();
		addView(mainView);
		setText(this.getString(R.string.close));
		setRightBtnClick(coloseBtnClick);

		init();
	}

	@SuppressWarnings("unchecked")
	private void init() {
		acctId = getIntent().getStringExtra(BANKACCTID);
		acctNum = getIntent().getStringExtra(BANKACCTNUM);
		isBuy = getIntent().getBooleanExtra(ISBUY, false);

		TextView tvBnakAcctType = (TextView) findViewById(R.id.bondtype_title);
		tvBnakAcctType.setEllipsize(TruncateAt.MIDDLE);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tvBnakAcctType);

		TextView tvBnakAcct = (TextView) findViewById(R.id.tv_bankacct);
		TextView tvBondAcctType = (TextView) findViewById(R.id.tv_bondaccttype);
		TextView tvName = (TextView) findViewById(R.id.tv_customName);
		TextView tvIdType = (TextView) findViewById(R.id.tv_idtype);
		TextView tvIdNumber = (TextView) findViewById(R.id.tv_idnum);
		TextView tvGender = (TextView) findViewById(R.id.tv_gender);

		TextView tvCountryCode = (TextView) findViewById(R.id.tv_countrycode);
		TextView tvAdress = (TextView) findViewById(R.id.tv_adress);
		TextView tvPostCode = (TextView) findViewById(R.id.tv_postcode);
		TextView tvPhone = (TextView) findViewById(R.id.tv_phone);
		TextView tvFax = (TextView) findViewById(R.id.tv_fax);
		TextView tvEmail = (TextView) findViewById(R.id.tv_email);

		Button btnConfirm = (Button) findViewById(R.id.btnConfirm);
		btnConfirm.setOnClickListener(this);

		Map<String, Object> custMap = BondDataCenter.getInstance()
				.getOpenBondCustomerInfoMap();
		/** 资金账号 */
		tvBnakAcct.setText(StringUtil.getForSixForString(acctNum));
		/** 购买债券类型 */
		tvBondAcctType.setText(BondDataCenter.getInstance().bondType().get(0));
		/** 姓名 */
		tvName.setText(commSetText((String) custMap.get(Bond.CUSTOMERNAME)));
		/** 证件类型 */
		tvIdType.setText(BondDataCenter.IDENTITYTYPE.get(custMap
				.get(Bond.IDENTIFYTYPE)));
		/** 证件号码 */
		tvIdNumber.setText(commSetText((String) custMap
				.get(Bond.IDENTIFYNUMBER)));
		/** 性别 */
		tvGender.setText(commSetText(BondDataCenter.gender_hq.get(custMap
				.get(Bond.CUSTOMERGENDER))));
		/** 国籍 */
		tvCountryCode.setText(BondDataCenter.countryCode.get(custMap
				.get(Bond.CUSTMOERCOUNTRY)));
		/** 地址 */
		tvAdress.setText(commSetText((String) custMap.get(Bond.CUSTMOERADRESS)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvAdress);
		/** 邮编 */
		tvPostCode.setText(commSetText((String) custMap
				.get(Bond.CUSTMOERPOSTCODE)));
		/** 手机号 */
		tvPhone.setText(commSetText((String) custMap.get(Bond.CUSTMOERPHONE)));
		/** 传真 */
		tvFax.setText(commSetText((String) custMap.get(Bond.CUSTMOERFAX)));
		/** 邮箱 */
		tvEmail.setText(commSetText((String) custMap.get(Bond.CUSTMOEREMAIL)));

		// 初始化加密控件
		// initSipBox((List<Map<String, Object>>) BondDataCenter.getInstance()
		// .getOpenBondConfirm().get(Bond.BUY_CONFIRM_FACTORLIST));
		// 获取随机数
		requestForRandomNumber();

	}

	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox() {
		Map<String, Object> result = new HashMap<String, Object>();
		result = BondDataCenter.getInstance().getOpenBondConfirm();
		String mmconversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeyText = (UsbKeyText) mainView.findViewById(R.id.sip_usbkey);
		usbKeyText.Init(mmconversationId, randomNumber, result, this);
		isOtp = usbKeyText.getIsOtp();
		isSmc = usbKeyText.getIsSmc();
		initOtpSipBox();
		initSmcSipBox();
	}

	/** 跟据返回初始化加密控件 */
	// private void initSipBox(List<Map<String, Object>> sipList) {
	// for (int i = 0; i < sipList.size(); i++) {
	// @SuppressWarnings("unchecked")
	// Map<String, Object> map = (Map<String, Object>) sipList.get(i).get(
	// Bond.SIGN_PAY_CONFIRM_SIP_FIED);
	// String sipType = (String) map.get(Bond.SIGN_PAY_CONFIRM_SIP_NAME);
	// if (sipType.equals(Comm.Otp)) {
	// isOtp = true;
	// initOtpSipBox();
	// } else if (sipType.equals(Comm.Smc)) {
	// isSmc = true;
	// initSmcSipBox();
	// }
	// }
	//
	// }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		contentCheck();
	}

	/** 初始化otp */
	private void initOtpSipBox() {
		if (isOtp) {
			LinearLayout layoutSip = (LinearLayout) mainView
					.findViewById(R.id.layout_sip);
			layoutSip.setVisibility(View.VISIBLE);
			otpSipBxo = (SipBox) mainView.findViewById(R.id.et_cecurity_ps);
			SipBoxUtils.initSipBoxWithTwoType(otpSipBxo, ConstantGloble.MIN_LENGTH, ConstantGloble.MIN_LENGTH,
					SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//			otpSipBxo.setCipherType(SystemConfig.CIPHERTYPE);
//			otpSipBxo.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			otpSipBxo.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			otpSipBxo.setId(10002);
//			otpSipBxo.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			otpSipBxo.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			otpSipBxo
//					.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			otpSipBxo.setSipDelegator(this);
//			otpSipBxo.setRandomKey_S(randomNumber);
		}

	}

	/** 初始化smc */
	private void initSmcSipBox() {
		if (isSmc) {
			LinearLayout layoutSmc = (LinearLayout) mainView
					.findViewById(R.id.layout_sms);
			layoutSmc.setVisibility(View.VISIBLE);
			Button btnSendSms = (Button) findViewById(R.id.smsbtn);
			SmsCodeUtils.getInstance().addSmsCodeListner(btnSendSms,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// 发送验证码请求
							sendSMSCToMobile();
						}
					});
			smcSipBxo = (SipBox) mainView.findViewById(R.id.sip_sms);
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

	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this,
				"queryRandomNumberCallBack");
	}

	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		randomNumber = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(randomNumber)) {
			return;
		}
		// 加密控件设置随机数
		// if (otpSipBxo != null) {
		// otpSipBxo.setRandomKey_S(randomNumber);
		// }
		// if (smcSipBxo != null) {
		// smcSipBxo.setRandomKey_S(randomNumber);
		// }
		/** 跟据返回初始化中银E盾加密控件 */
		initSipBox();
	}

	/** 提交校验 **/
	private void contentCheck() {
		// try {
		// ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		// if (isSmc) {
		// // 短信验证码
		// RegexpBean rebSms = new RegexpBean(
		// getString(R.string.acc_smc_regex), smcSipBxo.getText()
		// .toString(), ConstantGloble.SIPSMCPSW);
		// lists.add(rebSms);
		// }
		// if (isOtp) {
		// // 动态口令
		// RegexpBean rebOtp = new RegexpBean(
		// getString(R.string.active_code_regex), otpSipBxo
		// .getText().toString(), ConstantGloble.SIPOTPPSW);
		// lists.add(rebOtp);
		// }
		// if (RegexpUtils.regexpDate(lists)) {
		// if (isOtp) {
		// otpPassword = otpSipBxo.getValue().getEncryptPassword();
		// otpRandomNum = otpSipBxo.getValue().getEncryptRandomNum();
		// }
		// if (isSmc) {
		// smcPassword = smcSipBxo.getValue().getEncryptPassword();
		// smcRandomNum = smcSipBxo.getValue().getEncryptRandomNum();
		// }
		// // 获取token请求
		// BaseHttpEngine.showProgressDialog();
		// requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
		// .getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		// }
		// } catch (CodeException e) {
		// BaseDroidApp.getInstanse().createDialog(null, e.getMessage());
		// LogGloble.exceptionPrint(e);
		// }
		usbKeyText.checkDataUsbKey(otpSipBxo, smcSipBxo,
				new IUsbKeyTextSuccess() {

					@Override
					public void SuccessCallBack(String result, int errorCode) {
						// TODO Auto-generated method stub
						// 获取token请求
						BaseHttpEngine.showProgressDialog();
						requestPSNGetTokenId((String) BaseDroidApp
								.getInstanse().getBizDataMap()
								.get(ConstantGloble.CONVERSATION_ID));
					}
				});

	}

	// token返回
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		requestOpenAcctResult(token);
	}

	/**
	 * 开户提交
	 * 
	 * @param token
	 */
	private void requestOpenAcctResult(String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bond.METHOD_OPENACCT_RESULT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> custMap = BondDataCenter.getInstance()
				.getOpenBondCustomerInfoMap();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Bond.BOND_BUYRESULT_TOKEN, token);
		map.put(Bond.BANKACCT_ID, acctId);
		map.put(Bond.ACCOUNTID, acctId);
		map.put(Bond.BOND_TYPE, BondDataCenter.bondAcctType_re.get(2));
		map.put(Bond.BONDACCT_TYPE, BondDataCenter.bondAcctType_re.get(2));
		map.put(Bond.CUSTOMERNAME, custMap.get(Bond.CUSTOMERNAME));
		map.put(Bond.IDENTITYPE, custMap.get(Bond.IDENTIFYTYPE));
		map.put(Bond.IDENTINUMBER, custMap.get(Bond.IDENTIFYNUMBER));

		// if (!StringUtil.isNullOrEmpty(otpPassword)) {
		// map.put(Comm.Otp, otpPassword);
		// }
		// if (!StringUtil.isNullOrEmpty(otpRandomNum)) {
		// map.put(Comm.Otp_Rc, otpRandomNum);
		// }
		// if (!StringUtil.isNullOrEmpty(smcPassword)) {
		// map.put(Comm.Smc, smcPassword);
		// }
		// if (!StringUtil.isNullOrEmpty(smcRandomNum)) {
		// map.put(Comm.Smc_Rc, smcRandomNum);
		// }
		// 安全工具随机数添加
		usbKeyText.InitUsbKeyResult(map);

		SipBoxUtils.setSipBoxParams(map);

		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"openAcctResultComitCallBack");
	}

	/** 开户提交返回处理 **/
	public void openAcctResultComitCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BaseHttpEngine.dissMissProgressDialog();
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.bond_acctopen_error));
			return;
		}
		Intent it = new Intent(this, OpendAcctResultActivity.class);
		it.putExtra(BANKACCTNUM, acctNum);
		if (isBuy) {
			it.putExtra(ISBUY, isBuy);
			startActivityForResult(it, BondConstant.BOND_REQUEST_OPEN_ACCT_CODE);
		} else {
			startActivity(it);
			finish();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case BondConstant.BOND_REQUEST_OPEN_ACCT_CODE:
			switch (resultCode) {
			case RESULT_OK:
				setResult(RESULT_OK);
				finish();
				break;

			default:
				break;
			}
			break;

		default:
			break;
		}
	}
//	@Override
//	protected void onResume() {
//		super.onResume();
//		setLeftButtonPopupGone();
//	}
}
