package com.chinamworld.bocmbci.biz.loan.loanPledge;

import java.util.ArrayList;
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
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
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

/** 存单质押贷款——确认页面 */
public class LoanPledgeconfirmActivity extends LoanBaseActivity {
	private static final String TAG = "LoanPledgeconfirmActivity";
	private View confirmView = null;
	private TextView toAccTextView = null;
	private TextView payAccText = null;
	private TextView moneyText = null;
	private TextView codeText = null;
	private TextView timeText = null;
	private TextView rateText = null;
	private Button confirmButton = null;
	/** 随机数 */
	private String randomNumber = null;
	/** 预交易结果----factorList */
	private List<Map<String, Object>> factorList = null;
	/** 安全因子Map */
	private Map<String, Object> securityMap = null;
	/** 标识是否有动态口令 */
	private boolean isOtp = false;
	/** 标识是否有短息验证码 */
	private boolean isSmc = false;
	/** 加密控件 */
	private SipBox otpSipBxo;
	private SipBox smcSipBxo;
	private UsbKeyText usbKeyText;
	private String otpPassword;
	private String smcPassword;
	private String otpRandomNum;
	private String smcRandomNum;
	private String currencyCode = null;
	private String availableAmount = null;
	private String amount = null;
	private String loanPeriod = null;
	private String loanRate = null;
	private String toActNum = null;
	private String toAccount = null;
	private String payAccount = null;
	private String toActNumber = null;
	private String payAccountNumber = null;
	private List<Map<String, String>> volNoAndCerNoList = null;
	private String transactionId = null;
	private String accountNumber = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.loan_two_one));
		confirmView = LayoutInflater.from(this).inflate(
				R.layout.loan_select_confirm, null);
		tabcontentView.addView(confirmView);
		Button backButton = (Button) findViewById(R.id.ib_back);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		init();

		initDate();
		/** 跟据返回初始化中银E盾加密控件 */
		initSipBox();
		setValue();
		getCdnumberList();
	}

	private void init() {
		toAccTextView = (TextView) findViewById(R.id.loan_choise_input_toActNum);
		payAccText = (TextView) findViewById(R.id.loan_choise_input_payAccoun);
		moneyText = (TextView) findViewById(R.id.loan_choise_input_amount);
		codeText = (TextView) findViewById(R.id.loan_choise_input_code);
		timeText = (TextView) findViewById(R.id.loan_choise_input_loanPeriod);
		rateText = (TextView) findViewById(R.id.loan_choise_input_loanRate);
		confirmButton = (Button) findViewById(R.id.trade_nextButton);
		confirmButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkDate();
			}
		});
	}

	private void initDate() {
		currencyCode = getIntent().getStringExtra(Loan.LOAN_CURRENCYCODE_RES);
		availableAmount = getIntent().getStringExtra(
				Loan.LOAN_AVAILABLEBALANCE_RES);
		amount = getIntent().getStringExtra(Loan.LOAN_AMOUNT_REQ);
		loanPeriod = getIntent().getStringExtra(Loan.LOAN_LOANPERIOD_REQ);
		loanRate = getIntent().getStringExtra(Loan.LOAN_LOANRATE_RES);
		toActNum = getIntent().getStringExtra(ConstantGloble.LOAN_TOACTNUM1);
		toAccount = getIntent().getStringExtra(ConstantGloble.LOAN_TOACCOUNTID);
		payAccount = getIntent().getStringExtra(
				ConstantGloble.LOAN_PAYACCOUNTID);
		toActNumber = getIntent().getStringExtra(ConstantGloble.LOAN_TOACTNUM);
		payAccountNumber = getIntent().getStringExtra(
				ConstantGloble.LOAN_PAYACCOUNT);
		randomNumber = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_RANDOMNUMBER);
		securityMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.LOAN_FACTORLIST);
		// if (securityMap.containsKey(Bond.BUY_CONFIRM_FACTORLIST)) {
		// factorList = (List<Map<String, Object>>)
		// securityMap.get(Bond.BUY_CONFIRM_FACTORLIST);
		// initSipBox(factorList);
		// }
	}

	private void setValue() {
		String code = null;
		if (!StringUtil.isNull(currencyCode)
				&& LocalData.Currency.containsKey(currencyCode)) {
			code = LocalData.Currency.get(currencyCode);
		} else {
			code = "-";
		}

		String money = null;
		if (!StringUtil.isNull(amount) && !StringUtil.isNull(currencyCode)) {
			money = StringUtil.parseStringCodePattern(currencyCode, amount, 2);
		} else {
			money = "-";
		}
		codeText.setText("人民币元");
		String toNumber = StringUtil.getForSixForString(toActNumber);
		toAccTextView.setText(toNumber);
		payAccText.setText(payAccountNumber);
		moneyText.setText(money);
		String month = getResources().getString(R.string.month);
		timeText.setText(loanPeriod + month);
		rateText.setText(StringUtil.isNullOrEmptyCaseNullString(loanRate) ? ConstantGloble.BOCINVT_DATE_ADD
				: loanRate + "%");
	}

	/** 跟据返回初始化加密控件 */
	// private void initSipBox(List<Map<String, Object>> sipList) {
	// for (int i = 0; i < sipList.size(); i++) {
	// Map<String, Object> map = (Map<String, Object>)
	// sipList.get(i).get(Bond.SIGN_PAY_CONFIRM_SIP_FIED);
	// String sipType = (String) map.get(Bond.SIGN_PAY_CONFIRM_SIP_NAME);
	// if (sipType.equals(Comm.Otp)) {
	// isOtp = true;
	// initOtpSipBox();
	// } else if (sipType.equals(Comm.Smc)) {
	// isSmc = true;
	// initSmcSipBox();
	// }
	// }
	// // 加密控件设置随机数
	// if (otpSipBxo != null) {
	// otpSipBxo.setRandomKey_S(randomNumber);
	// }
	// if (smcSipBxo != null) {
	// smcSipBxo.setRandomKey_S(randomNumber);
	// }
	// }
	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox() {
		usbKeyText = (UsbKeyText) confirmView.findViewById(R.id.sip_usbkey);
		String mmconversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeyText.Init(mmconversationId, randomNumber, securityMap, this);
		isOtp = usbKeyText.getIsOtp();
		isSmc = usbKeyText.getIsSmc();
		initOtpSipBox();
		initSmcSipBox();

	}

	/** 初始化otp -----动态口令 */
	private void initOtpSipBox() {
		if (isOtp) {
			LinearLayout layoutSip = (LinearLayout) confirmView
					.findViewById(R.id.ll_active_code);
			layoutSip.setVisibility(View.VISIBLE);
			otpSipBxo = (SipBox) confirmView.findViewById(R.id.sipbox_active);
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

	/** 初始化smc---手机校验码 */
	private void initSmcSipBox() {
		if (isSmc) {
			LinearLayout layoutSmc = (LinearLayout) confirmView
					.findViewById(R.id.ll_smc);
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
			smcSipBxo = (SipBox) confirmView.findViewById(R.id.sipbox_smc);
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

		// try {
		// ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		// if (isSmc) {
		// // 短信验证码
		// RegexpBean rebSms = new RegexpBean(getString(R.string.acc_smc_regex),
		// smcSipBxo.getText().toString(),
		// ConstantGloble.SIPSMCPSW);
		// lists.add(rebSms);
		// }
		// if (isOtp) {
		// // 动态口令
		// RegexpBean rebOtp = new
		// RegexpBean(getString(R.string.active_code_regex), otpSipBxo.getText()
		// .toString(), ConstantGloble.SIPOTPPSW);
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
		// String commConversationId = (String)
		// BaseDroidApp.getInstanse().getBizDataMap()
		// .get(ConstantGloble.CONVERSATION_ID);
		// requestPSNGetTokenId(commConversationId);
		// }
		// } catch (CodeException e) {
		// BaseDroidApp.getInstanse().createDialog(null, e.getMessage());
		// LogGloble.exceptionPrint(e);
		// }
		/** 安全工具数据校验 */
		usbKeyText.checkDataUsbKey(otpSipBxo, smcSipBxo,
				new IUsbKeyTextSuccess() {

					@Override
					public void SuccessCallBack(String result, int errorCode) {
						// TODO Auto-generated method stub
						// 获取token请求
						BaseHttpEngine.showProgressDialog();
						String commConversationId = (String) BaseDroidApp
								.getInstanse().getBizDataMap()
								.get(ConstantGloble.CONVERSATION_ID);
						requestPSNGetTokenId(commConversationId);
					}
				});
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		if (StringUtil.isNullOrEmpty(volNoAndCerNoList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		requestPsnLOANPledgeSubmit(token);
	}

	/** 存单质押贷款提交交易 */
	private void requestPsnLOANPledgeSubmit(String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_PSNLOANPLEDGESUBMIT_API);
		String conversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		biiRequestBody.setConversationId(conversationId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Loan.LOAN_LOANPERIOD_REQ, loanPeriod);
		// if (isOtp) {
		// // 动态口令
		// map.put(Comm.Otp, otpPassword);
		// map.put(Comm.Otp_Rc, otpRandomNum);
		// }
		// if (isSmc) {
		// // 手机校验码
		// map.put(Comm.Smc, smcPassword);
		// map.put(Comm.Smc_Rc, smcRandomNum);
		// }
		/** 安全工具参数获取 */
		usbKeyText.InitUsbKeyResult(map);
		map.put(Loan.LOAN_LOANTYPE_REQ, ConstantGloble.LOAN_TYPE);
		String availableAmounts = StringUtil.splitStringwithCode(currencyCode,
				availableAmount, 2);
		map.put(Loan.LOAN_AVAILABLEAMOUNT_REQ, availableAmounts);
		String amounts = StringUtil
				.splitStringwithCode(currencyCode, amount, 2);
		map.put(Loan.LOAN_AMOUNT_REQ, amounts);
		map.put(Loan.LOAN_LOANPERIOD_REQ,
				String.valueOf(Integer.parseInt(loanPeriod)));
		map.put(Loan.LOAN_LOANRATE_RES, loanRate);
		map.put(Loan.LOAN_PAYTYPE_REQ, ConstantGloble.LOAN_B);
		map.put(Loan.LOAN_PAYCYCLE_REQ, ConstantGloble.LOAN_P);
		map.put(Loan.LOAN_TOACTNUM_REQ, toActNum);
		map.put(Loan.LOAN_TOACCOUNT_REQ, toAccount);
		map.put(Loan.LOAN_PAYACCOUNT_REQ, payAccount);
		map.put(Loan.LOAN_CURRENCYCODE_RES, "001");
		String accountId = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(Loan.LOAN_ACCOUNTID_RES);
		map.put(Loan.LOAN_ACCOUNTID_RES, accountId);
		map.put(Loan.LOAN_TOKEN_REQ, token);
		map.put(Loan.LOAN_VOLNOANDCERNOLIST_REQ, volNoAndCerNoList);
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnLOANPledgeSubmitCallback");
	}

	public void requestPsnLOANPledgeSubmitCallback(Object resultObj) {
		super.requestPsnLoanRateQueryCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		Map<String, String> result = (Map<String, String>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		transactionId = result.get(Loan.LOAN_TRANSACTIONID_REQ);
		accountNumber = result.get(Loan.LOAN_ACCOUNTNUMBER_REQ);
		if (StringUtil.isNull(transactionId)
				|| StringUtil.isNull(accountNumber)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		gotoActivity();
	}

	private void gotoActivity() {
		Intent intent = new Intent(LoanPledgeconfirmActivity.this,
				LoanPledgeSuccessActivity.class);
		intent.putExtra(Loan.LOAN_CURRENCYCODE_RES, currencyCode);
		intent.putExtra(Loan.LOAN_AVAILABLEBALANCE_RES, availableAmount);
		intent.putExtra(Loan.LOAN_AMOUNT_REQ, amount);
		intent.putExtra(Loan.LOAN_LOANPERIOD_REQ, loanPeriod);
		intent.putExtra(Loan.LOAN_LOANRATE_RES, loanRate);
		intent.putExtra(ConstantGloble.LOAN_TOACTNUM1, toActNum);
		intent.putExtra(ConstantGloble.LOAN_TOACTNUM, toActNumber);
		intent.putExtra(ConstantGloble.LOAN_PAYACCOUNT, payAccountNumber);
		intent.putExtra(Loan.LOAN_TRANSACTIONID_REQ, transactionId);
		intent.putExtra(Loan.LOAN_ACCOUNTNUMBER_REQ, accountNumber);
		BaseHttpEngine.dissMissProgressDialog();
		startActivity(intent);
	}

	/** 得到存折册号、存单号集合 */
	private void getCdnumberList() {
		List<Map<String, String>> cdNumberList = (List<Map<String, String>>) BaseDroidApp
				.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_CDNUMBERLIST);
		List<Boolean> listFlag = (List<Boolean>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.LOAN_LISTFLAG);
		if (StringUtil.isNullOrEmpty(cdNumberList)
				|| StringUtil.isNullOrEmpty(listFlag)) {
			return;
		}
		volNoAndCerNoList = new ArrayList<Map<String, String>>();
		int len = listFlag.size();
		for (int i = 0; i < len; i++) {
			if (listFlag.get(i)) {
				String volumeNumber = cdNumberList.get(i).get(
						Loan.LOAN_VOLUMBERNUMBER_RES);
				String cdNumber = cdNumberList.get(i).get(
						Loan.LOAN_CDNUMBER_RES);
				String currencyCode = cdNumberList.get(i).get(
						Loan.LOAN_CURRENCYCODE_RES);
				String availableBalance = cdNumberList.get(i).get(
						Loan.LOAN_AVAILABLEBALANCE_RES);
				String pingNo = cdNumberList.get(i).get(Loan.LOAN_PINGNO_RES);
				Map<String, String> map = new HashMap<String, String>();
				map.put(Loan.LOAN_VOLNOS_REQ, volumeNumber);
				map.put(Loan.LOAN_CERNO_REQ, cdNumber);
				map.put(Loan.LOAN_PINGNO_REQ, pingNo);
				map.put(Loan.LOAN_CURRENCYCODE_RES, currencyCode);
				String availableBalances = StringUtil.splitStringwithCode(
						currencyCode, availableBalance, 2);
				map.put(Loan.LOAN_AVAILABLEBALANCE_RES, availableBalances);
				volNoAndCerNoList.add(map);
			}
		}
	}
}
