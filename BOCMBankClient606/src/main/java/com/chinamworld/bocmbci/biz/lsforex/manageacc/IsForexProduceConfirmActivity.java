package com.chinamworld.bocmbci.biz.lsforex.manageacc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
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
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/** 签约确认页面 */
public class IsForexProduceConfirmActivity extends IsForexBaseActivity {
	public static final String TAG = "IsForexProduceConfirmActivity";
	private Button backButton = null;
	private View rateInfoView = null;
	private TextView tradeAccSpinner = null;
	private TextView baillAccSpinner = null;
	private TextView typeText = null;
	private TextView nickText = null;
	private TextView cNameText = null;
	private TextView eNameText = null;
	private TextView jsCodeText = null;
	private TextView zcRateText = null;
	private TextView warnRateText = null;
	private TextView needRateText = null;
	private TextView openRateText = null;
	private TextView left_needText = null;
	private Button nextButton = null;
	/** 保证金产品list */
	private List<Map<String, String>> baillResultList = null;
	/** 符合条件的借记卡信息 */
	private List<Map<String, String>> resultList = null;
	/** 随机数 */
	private String randomNumber = null;
	/** 预交易结果----factorList */
	private List<Map<String, Object>> factorList = null;
	/** CA 加签数据报文音频Key */
	private String _plainData = null;
	/** CA DN值 音频key */
	private String _certDN = null;
	/** 安全因子Map */
	private Map<String, Object> securityMap = null;
	/** 标识是否有动态口令 */
	private boolean isOtp = false;
	/** 标识是否有短息验证码 */
	private boolean isSmc = false;

	/** 加密控件 */
	private SipBox otpSipBxo;
	private SipBox smcSipBxo;

	private String otpPassword;
	private String smcPassword;

	private String otpRandomNum;
	private String smcRandomNum;

	private int tradePosition = -1;
	private int bailPosition = -1;
	private String jsCode = null;
	/** 中银E盾加密控件 */
	UsbKeyText usbKeytext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.isForex_manage_right));
		LogGloble.d(TAG, "onCreate");
		backButton = (Button) findViewById(R.id.ib_back);
		rateInfoView = LayoutInflater.from(this).inflate(R.layout.isforex_manage_sign_confirm, null);
		tabcontent.addView(rateInfoView);
		baillResultList = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap().get(IsForex.ISFOREX_LIST);
		resultList = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap().get(IsForex.ISFOREX_LIST_REQ);
		randomNumber = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.ISFOREX_RANDOMNUMBER_KEY);
		securityMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.ISFOREX_PRERESULT_KEY);
		tradePosition = getIntent().getIntExtra(ConstantGloble.POSITION, -1);
		bailPosition = getIntent().getIntExtra(ConstantGloble.ACC_POSITION, -1);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		init();
		initSipBox();
		setTradeValue();
		setBailValue();

	}

	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox() {
		usbKeytext = (UsbKeyText) rateInfoView.findViewById(R.id.sip_usbkey);
		usbKeytext.Init(commConversationId, randomNumber, securityMap, this);
		isOtp = usbKeytext.getIsOtp();
		isSmc = usbKeytext.getIsSmc();
		initOtpSipBox();
		initSmcSipBox();

	}

	/** 初始化otp -----动态口令 */
	private void initOtpSipBox() {
		if (isOtp == false)
			return;
		LinearLayout layoutSip = (LinearLayout) rateInfoView.findViewById(R.id.ll_active_code);
		layoutSip.setVisibility(View.VISIBLE);
		otpSipBxo = (SipBox) rateInfoView.findViewById(R.id.sipbox_active);
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

	/** 初始化smc---手机校验码 */
	private void initSmcSipBox() {
		if (isSmc == false)
			return;
		LinearLayout layoutSmc = (LinearLayout) rateInfoView.findViewById(R.id.ll_smc);
		layoutSmc.setVisibility(View.VISIBLE);
		Button btnSendSms = (Button) findViewById(R.id.smsbtn);
		SmsCodeUtils.getInstance().addSmsCodeListner(btnSendSms, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 发送验证码请求
				sendSMSCToMobile();
			}
		});
		smcSipBxo = (SipBox) rateInfoView.findViewById(R.id.sipbox_smc);
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

	private void init() {
		tradeAccSpinner = (TextView) findViewById(R.id.isForex_tradeAcc);
		typeText = (TextView) findViewById(R.id.forex_customer_accType);
		nickText = (TextView) findViewById(R.id.forex_customer_accAlias);
		baillAccSpinner = (TextView) findViewById(R.id.isForex_BaillAcc);
		cNameText = (TextView) findViewById(R.id.isForex_manage_bailCNamen);
		eNameText = (TextView) findViewById(R.id.isForex_manage_bailEName);
		jsCodeText = (TextView) findViewById(R.id.isForex_vfgRegCurrency1);
		zcRateText = (TextView) findViewById(R.id.isForex_manage_liquidationRatio);
		warnRateText = (TextView) findViewById(R.id.isForex_manage_warnRatio);
		needRateText = (TextView) findViewById(R.id.isForex_manage_needMarginRatio);
		openRateText = (TextView) findViewById(R.id.isForex_manage_openRate);
		left_needText = (TextView) findViewById(R.id.left_text);
		nextButton = (Button) findViewById(R.id.trade_nextButton);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, left_needText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, nickText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, cNameText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, eNameText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, zcRateText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, warnRateText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, needRateText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, openRateText);

		// 保证金产品编号 保证金中文名称 保证金英文名称 弹出框
		TextView tv_chinese = (TextView) findViewById(R.id.tv_chinese);
		TextView tv_english = (TextView) findViewById(R.id.tv_english);
		TextView tv_number = (TextView) findViewById(R.id.tv_number);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_chinese);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_english);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_number);

		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (securityMap.containsKey(Bond.BUY_CONFIRM_FACTORLIST)) {
					BaseHttpEngine.showProgressDialog();
					checkDate();
				} else {
					// 获取token请求
					BaseHttpEngine.showProgressDialog();
					requestPSNGetTokenId(commConversationId);

				}

			}
		});

	}

	/** 为控件赋值 */
	private void setTradeValue() {
		// 资金账户
		Map<String, String> map = resultList.get(tradePosition);
		String accountNumber = map.get(IsForex.ISFOREX_ACCOUNTNUMBER_REQ1);
		String account = null;
		if (!StringUtil.isNull(accountNumber)) {
			account = StringUtil.getForSixForString(accountNumber);
		}
		String accountType = map.get(IsForex.ISFOREX_ACCOUNTTYPE_REQ1);
		String type = null;
		if (!StringUtil.isNull(accountType) && LocalData.AccountType.containsKey(accountType)) {
			type = LocalData.AccountType.get(accountType);
		}
		String nickName = map.get(IsForex.ISFOREX_NICKNAME_REQ1);
		tradeAccSpinner.setText(account);
		typeText.setText(type);
		nickText.setText(nickName);
	}

	/** 保证金账户信息 */
	private void setBailValue() {
		Map<String, String> map = baillResultList.get(bailPosition);
		String bailNo = map.get(IsForex.ISFOREX_BAILNO_RES);
		String bailCName = map.get(IsForex.ISFOREX_BAILCNAME_RES);
		String bailEName = map.get(IsForex.ISFOREX_BAILENAME_RES);
		String settleCurrency = map.get(IsForex.ISFOREX_SETTLECURRENCY_RES1);
		String liquidationRatio = map.get(IsForex.ISFOREX_LIQUIDATIONRATIO_RES);
		String warnRatio = map.get(IsForex.ISFOREX_WARNRATIO_RES);
		String needMarginRatio = map.get(IsForex.ISFOREX_NEEDMARGINRATE_RES);
		String openRate = map.get(IsForex.ISFOREX_OPENRATE_RES);
		baillAccSpinner.setText(bailNo);
		cNameText.setText(bailCName);
		eNameText.setText(bailEName);

		if (!StringUtil.isNull(settleCurrency) && LocalData.Currency.containsKey(settleCurrency)) {
			jsCode = LocalData.Currency.get(settleCurrency);
			jsCodeText.setText(jsCode);
		}
		String zcRate = null;
		if (StringUtil.isNull(liquidationRatio)) {
			zcRateText.setText("-");
		} else {
			zcRate = StringUtil.dealNumber(liquidationRatio);
			zcRateText.setText(zcRate);
		}
		String bjRate = null;
		if (StringUtil.isNull(warnRatio)) {
			warnRateText.setText("-");
		} else {
			bjRate = StringUtil.dealNumber(warnRatio);
			warnRateText.setText(bjRate);
		}
		String kcRate = StringUtil.dealNumber(openRate);
		if (StringUtil.isNull(openRate)) {
			openRateText.setText("-");
		} else {
			kcRate = StringUtil.dealNumber(openRate);
			openRateText.setText(kcRate);
		}

		String needRate = StringUtil.dealNumber(needMarginRatio);
		if (StringUtil.isNull(needMarginRatio)) {
			needRateText.setText("-");
		} else {
			needRate = StringUtil.dealNumber(needMarginRatio);
			needRateText.setText(needRate);
		}
	}

	/** 验证动态口令 */
	private void checkDate() {
		/** 音频Key安全工具认证 */
		usbKeytext.checkDataUsbKey(otpSipBxo, smcSipBxo, new IUsbKeyTextSuccess() {

			@Override
			public void SuccessCallBack(String result, int errorCode) {
				// TODO Auto-generated method stub

				BaseHttpEngine.showProgressDialog();
				requestPSNGetTokenId(commConversationId);

			}
		});

	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		Map<String, String> map = resultList.get(tradePosition);
		String accountNumber = map.get(IsForex.ISFOREX_ACCOUNTNUMBER_REQ1);
		String accountId = map.get(IsForex.ISFOREX_ACCOUNT_REQ);
		Map<String, String> bailMap = baillResultList.get(bailPosition);
		String bailEName = bailMap.get(IsForex.ISFOREX_BAILCNAME_RES);
		String bailNo = bailMap.get(IsForex.ISFOREX_BAILNO_RES);
		String settleCurrency = bailMap.get(IsForex.ISFOREX_SETTLECURRENCY_RES1);
		// 双向宝签约提交交易
		PsnVFGSignSubmit(accountId, accountNumber, bailEName, bailNo, settleCurrency, token);
	}

	/**
	 * 双向宝签约提交交易
	 * 
	 * @param accountId
	 *            :账户标识
	 * @param accountNumber
	 *            :借记卡卡号
	 * @param bailName
	 *            :保证金产品名称
	 * @param bailNo
	 *            :保证金产品序号
	 * @param settleCurrency
	 *            :结算货币
	 * @param token
	 */
	private void PsnVFGSignSubmit(String accountId, String accountNumber, String bailName, String bailNo,
			String settleCurrency, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGSIGNSUBMIT_API);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(IsForex.ISFOREX_ACCOUNT_REQ, accountId);
		map.put(IsForex.ISFOREX_ACCOUNTNUMBER_REQ1, accountNumber);
		map.put(IsForex.ISFOREX_BAILNAME_RES, bailName);
		map.put(IsForex.ISFOREX_BAILNO_RES, bailNo);
		map.put(IsForex.ISFOREX_SETTLECURRENCY_RES1, settleCurrency);
		map.put(IsForex.ISFOREX_TOCKEN_REQ, token);

		/** 安全工具参数获取 */
		usbKeytext.InitUsbKeyResult(map);

		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "PsnVFGSignSubmitCallback");
	}

	/** 双向宝签约提交交易----回调 */
	public void PsnVFGSignSubmitCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> result = (Map<String, String>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		Intent intent = new Intent(IsForexProduceConfirmActivity.this, IsForexProduceSuccessActivity.class);
		intent.putExtra(ConstantGloble.POSITION, tradePosition);// 资金账户position
		intent.putExtra(IsForex.ISFOREX_SETTLECURRENCY_RES1, jsCode);
		BaseDroidApp.getInstanse().getBizDataMap().put(IsForex.ISFOREX_LIST, result);
		BaseHttpEngine.dissMissProgressDialog();
		// startActivity(intent);
		startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_REGISTFUNCACC_CODE);// 10

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
