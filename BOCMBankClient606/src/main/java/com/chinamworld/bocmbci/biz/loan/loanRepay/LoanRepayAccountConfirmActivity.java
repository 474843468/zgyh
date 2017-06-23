package com.chinamworld.bocmbci.biz.loan.loanRepay;

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
import com.chinamworld.bocmbci.biz.loan.LoanData;
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

/** 提前还款------确认页面 */
public class LoanRepayAccountConfirmActivity extends LoanBaseActivity {
	private static final String TAG = "LoanRepayAccountConfirmActivity";
	private View confirmView = null;
	private TextView typeText = null;
	private TextView accText = null;
	/** 提前还款金额 */
	private TextView moneyText = null;
	private TextView codeText = null;
	/** 提前还款本金 */
	private TextView loanMoneyText = null;
	private TextView loanRateText = null;
	private TextView loanOmoneyText = null;
	/** 优惠后手续费 */
	private TextView loan_repay_charges1;
	private TextView loanEveryMoneyText = null;
	private TextView chargesText = null;
	private View chargesView = null;
	private List<Map<String, String>> loanAccountList = null;
	private Map<String, Object> detailRasultMap = null;
	private Button loanButton = null;
	private TextView leftText = null;
	private TextView leftText2 = null;
	private LinearLayout comm_view_left_title = null;

	/** 未进行格式化的账户列表 */
	private List<String> accountNumberList = null;
	private List<String> accountIdList = null;
	private int position = -1;
	private int accPosition = -1;
	/** 预交易结果----factorList */
	// private List<Map<String, Object>> factorList = null;
	/** 预交易数据 */
	private Map<String, Object> securityMap = null;
	/** 线上标识 */
	private String onlineFlag = null;
	/** 循环类型 */
	private String cycleType;
	/** 优惠后手续费 */
	private String privilegeProcedure;
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
	/** 随机数 */
	private String randomNumber = null;
	private String loanType = null;
	private String loanAccount = null;
	private String currency = null;
	private String loanAmount = null;
	private String loanPeriod = null;
	private String loanToDate = null;
	private String advanceRepayInterest = null;
	private String advanceRepayCapital = null;
	private String repayAmount = null;
	private String fromAccountId = null;
	private String accoutNumber = null;
	private String loanPeriodUnit = null;
	private String remainCapital = null;
	private String thisIssueRepayInterest = null;
	private String remainIssue = null;
	private String charges = null;
	private String thisIssueRepayDate = null;
	private String thisIssueRepayAmount = null;
	private String everyTermAmount = null;
	private String repayAmountInAdvance = null;
	private String interestType = null;
	private String remainIssueforAdvance = null;
	// TODO
	/** 中银E盾加密控件 */
	UsbKeyText usbKeytext;
	// 手续费
	private String sxf = null;
	public static String repayAmountInAdvanceNew;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.loan_three_one));
		Button backButton = (Button) findViewById(R.id.ib_back);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		position = getIntent().getIntExtra(ConstantGloble.POSITION, -1);
		accPosition = getIntent().getIntExtra(
				ConstantGloble.FOREX_SELLINLIST_POSITION, -1);
		privilegeProcedure = getIntent().getStringExtra(
				Loan.LOAN_PRIVILEGE_PROCEDURE_RES);
		loanAccountList = (List<Map<String, String>>) BaseDroidApp
				.getInstanse().getBizDataMap()
				.get(Loan.LOAN_LOANACCOUNTLIST_REQ);
		detailRasultMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.LOAN_RESULT);
		accountNumberList = (List<String>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.LOAN_ACCOUNTNUMBERLIST_KEY);
		accountIdList = (List<String>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.LOAN_ACCOUNTIDLIST_KEY);
		randomNumber = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_RANDOMNUMBER);
		securityMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.LOAN_FACTORLIST);
		repayAmount = getIntent().getStringExtra("repayAmount");
		fromAccountId = getIntent().getStringExtra(Loan.LOAN_FROMACCOUNTID_RES);
		accoutNumber = getIntent().getStringExtra(Loan.LOAN_ACCOUNTNUMBERS_RES);

		if (StringUtil.isNullOrEmpty(loanAccountList)
				|| StringUtil.isNullOrEmpty(detailRasultMap)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		if (position < 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		if (StringUtil.isNullOrEmpty(accountNumberList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		init();
		getDate();
		// 中银E盾初始化
		initSipBox();
		// if (securityMap.containsKey(Bond.BUY_CONFIRM_FACTORLIST)) {
		// factorList = (List<Map<String, Object>>)
		// securityMap.get(Bond.BUY_CONFIRM_FACTORLIST);
		//
		// }
	}

	/** 跟据返回初始化加密控件 */
	/*
	 * private void initSipBox(List<Map<String, Object>> sipList) { for (int i =
	 * 0; i < sipList.size(); i++) { Map<String, Object> map = (Map<String,
	 * Object>) sipList.get(i).get(Bond.SIGN_PAY_CONFIRM_SIP_FIED); String
	 * sipType = (String) map.get(Bond.SIGN_PAY_CONFIRM_SIP_NAME); if
	 * (sipType.equals(Comm.Otp)) { isOtp = true; initOtpSipBox(); } else if
	 * (sipType.equals(Comm.Smc)) { isSmc = true; initSmcSipBox(); } } //
	 * 加密控件设置随机数 if (otpSipBxo != null) {
	 * otpSipBxo.setRandomKey_S(randomNumber); } if (smcSipBxo != null) {
	 * smcSipBxo.setRandomKey_S(randomNumber); } }
	 */
	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox() {
		String commConversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeytext = (UsbKeyText) confirmView.findViewById(R.id.sip_usbkey);
		usbKeytext.Init(commConversationId, randomNumber, securityMap, this);
		isOtp = usbKeytext.getIsOtp();
		isSmc = usbKeytext.getIsSmc();
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

	private void init() {
		confirmView = LayoutInflater.from(this).inflate(
				R.layout.loan_repay_confirm, null);
		tabcontentView.addView(confirmView);
		typeText = (TextView) findViewById(R.id.loan_choise_input_loanType);
		accText = (TextView) findViewById(R.id.loan_choise_accountNumber1);
		moneyText = (TextView) findViewById(R.id.loan_repay_edit_money_m);
		codeText = (TextView) findViewById(R.id.loan_choise_input_code);
		loanMoneyText = (TextView) findViewById(R.id.loan_repay_advanceRepayCapital_n);
		loanRateText = (TextView) findViewById(R.id.loan_repay_advanceRepayInterest);
		loanOmoneyText = (TextView) findViewById(R.id.loan_repay_repayAmountInAdvance);
		// loanEveryMoneyText = (TextView)
		// findViewById(R.id.loan_repay_everyTermAmount);
		chargesText = (TextView) findViewById(R.id.loan_repay_charges);
		chargesView = findViewById(R.id.loan_chages_view);
		comm_view_left_title = (LinearLayout) findViewById(R.id.comm_view_left_title);
		loan_repay_charges1 = (TextView) findViewById(R.id.loan_repay_charges1);
		leftText = (TextView) findViewById(R.id.left_text1);
		leftText2 = (TextView) findViewById(R.id.left_text2);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, leftText);
		PopupWindowUtils.getInstance()
				.setOnShowAllTextListener(this, leftText2);
		loanButton = (Button) findViewById(R.id.trade_nextButton);
		loanButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkDate();
			}
		});
	}

	private void getDate() {
		// 线上标识
		onlineFlag = LoanRepayAccountActivity.onlineFlag;
		// 循环类型
		cycleType = LoanRepayAccountActivity.cycleType;
		// Map<String, String> map = loanAccountList.get(position);
		loanType = (String) detailRasultMap.get(Loan.LOAN_LOANTYPE_REQ);
		loanAccount = (String) detailRasultMap.get(Loan.LOAN_LOANACCOUNT_REQ);
		loanAmount = (String) detailRasultMap.get(Loan.LOANACC_LOAN_AMOUNT_RES);
		currency = (String) detailRasultMap.get(Loan.LOAN_CURRENCY_REQ);
		String number = null;
		if (!StringUtil.isNull(loanAccount)) {
			number = StringUtil.getForSixForString(loanAccount);
		} else {
			number = "-";
		}
		String code = null;
		if (!StringUtil.isNull(currency)
				&& LocalData.Currency.containsKey(currency)) {
			code = LocalData.Currency.get(currency);
		} else {
			code = "-";
		}
		String money = null;
		if (!StringUtil.isNull(repayAmount) && !StringUtil.isNull(currency)) {
			money = StringUtil.parseStringCodePattern(currency, repayAmount, 2);
		} else {
			money = "-";
		}
		loanPeriod = (String) detailRasultMap.get(Loan.LOAN_LOANPERIOD_REQ);
		loanToDate = (String) detailRasultMap.get(Loan.LOAN_LOANTODATE_REQ);
		remainCapital = (String) detailRasultMap
				.get(Loan.LOAN_REMAINCAPITAL_REQ);
		interestType = (String) detailRasultMap.get(Loan.LOAN_INTTYPETERES_REQ);
		remainIssue = (String) detailRasultMap.get(Loan.LOAN_REMAINISSUE_REQ);
		thisIssueRepayDate = (String) detailRasultMap
				.get(Loan.LOAN_THISISSUEREPAYDATE_REQ);
		thisIssueRepayAmount = (String) detailRasultMap
				.get(Loan.LOAN_THISISSUEREPAYAMOUNT_REQ);
		thisIssueRepayInterest = (String) detailRasultMap
				.get(Loan.LOAN_THISISSUEREPAYINTEREST_REQ);
		loanPeriodUnit = (String) detailRasultMap
				.get(Loan.LOAN_LOANPERIODUNIT_REQ);

		Map<String, String> loanRepayCount = (Map<String, String>) securityMap
				.get(Loan.LOAN_LOANREPAYCOUNT_RES);
		if (StringUtil.isNullOrEmpty(loanRepayCount)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		advanceRepayCapital = loanRepayCount
				.get(Loan.LOAN_ADVANCEREPAYCAPITAL_RES);
		advanceRepayInterest = loanRepayCount
				.get(Loan.LOAN_ADVANCEREPAYINTEREST_RES);
		repayAmountInAdvance = loanRepayCount
				.get(Loan.LOAN_REPAYAMOUNTINADVANCE_RES);
		repayAmountInAdvanceNew = repayAmountInAdvance;
		everyTermAmount = loanRepayCount.get(Loan.LOAN_EVERYTERMAMOUNT_RES);
		charges = loanRepayCount.get(Loan.LOAN_CHARGES_RES);
		remainIssueforAdvance = loanRepayCount
				.get(Loan.LOAN_REMAINISSUEFORADVANCE_RES);
		String advanceRepayCapitals = null;
		if (!StringUtil.isNull(advanceRepayCapital)
				&& !StringUtil.isNull(currency)) {
			advanceRepayCapitals = StringUtil.parseStringCodePattern(currency,
					advanceRepayCapital, 2);
		} else {
			advanceRepayCapitals = "-";
		}
		String advanceRepayInterests = null;
		if (!StringUtil.isNull(advanceRepayInterest)
				&& !StringUtil.isNull(currency)) {
			advanceRepayInterests = StringUtil.parseStringCodePattern(currency,
					advanceRepayInterest, 2);
		} else {
			advanceRepayInterests = "-";
		}
		String repayAmountInAdvances = null;
		if (!StringUtil.isNull(repayAmountInAdvance)
				&& !StringUtil.isNull(currency)) {
			repayAmountInAdvances = StringUtil.parseStringCodePattern(currency,
					repayAmountInAdvance, 2);
		} else {
			repayAmountInAdvances = "-";
		}
		String everyTermAmounts = null;
		if (!StringUtil.isNull(everyTermAmount) && !StringUtil.isNull(currency)) {
			everyTermAmounts = StringUtil.parseStringCodePattern(currency,
					everyTermAmount, 2);
		} else {
			everyTermAmounts = "-";
		}
		String type = null;
		if (LoanData.loanTypeData.containsKey(loanType)) {
			type = LoanData.loanTypeData.get(loanType);
		} else {
			type = "-";
		}
		if (LoanData.loanCycleList.contains(loanType)) {
			chargesView.setVisibility(View.VISIBLE);
		} else {
			chargesView.setVisibility(View.GONE);
		}
		typeText.setText(type);
		accText.setText(number);
		double moneyt = 0;
		if(!money.equals("-")){
			moneyt = Double.valueOf(money);
		}
		//生产问题修改 预交易repayAmount上送“提前还款本金”（用户输入）和“thisIssueRepayInterest截止当前应还利息”（详情接口返回数据）的总和。
		String thisIssueRepayInterestst=null;
		if(StringUtil.isNullOrEmpty((String) detailRasultMap.get(Loan.LOAN_THISISSUEREPAYINTEREST_REQ))){
			thisIssueRepayInterestst = "0";
		}else {
			thisIssueRepayInterestst = StringUtil.parseStringCodePattern(currency, (String) detailRasultMap.get(Loan.LOAN_THISISSUEREPAYINTEREST_REQ), 2);
		}
		String zongjine = StringUtil.parseStringCodePattern(currency, String.valueOf(moneyt+Double.valueOf(thisIssueRepayInterestst)), 2);
		moneyText.setText(zongjine);
		codeText.setText(code);
		loanMoneyText.setText(advanceRepayCapitals);
		loanRateText.setText(advanceRepayInterests);
		loanOmoneyText.setText(repayAmountInAdvances);
		// loanEveryMoneyText.setText(everyTermAmounts);
		sxf = StringUtil.parseStringCodePattern(currency, charges, 2);
		chargesText.setText(sxf);
		if ("R".equals(cycleType) && "1".equals(onlineFlag)) {
			privilegeProcedure = StringUtil.parseStringCodePattern(currency, privilegeProcedure, 2);
			loan_repay_charges1.setText(privilegeProcedure);
		} else {
			comm_view_left_title.setVisibility(View.GONE);
		}
	}

	/** 验证动态口令 */
	/*
	 * private void checkDate() {
	 * 
	 * try { ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>(); if
	 * (isSmc) { // 短信验证码 RegexpBean rebSms = new
	 * RegexpBean(getString(R.string.acc_smc_regex),
	 * smcSipBxo.getText().toString(), ConstantGloble.SIPSMCPSW);
	 * lists.add(rebSms); } if (isOtp) { // 动态口令 RegexpBean rebOtp = new
	 * RegexpBean(getString(R.string.active_code_regex), otpSipBxo.getText()
	 * .toString(), ConstantGloble.SIPOTPPSW); lists.add(rebOtp); } if
	 * (RegexpUtils.regexpDate(lists)) { if (isOtp) { otpPassword =
	 * otpSipBxo.getValue().getEncryptPassword(); otpRandomNum =
	 * otpSipBxo.getValue().getEncryptRandomNum(); } if (isSmc) { smcPassword =
	 * smcSipBxo.getValue().getEncryptPassword(); smcRandomNum =
	 * smcSipBxo.getValue().getEncryptRandomNum(); } // 获取token请求
	 * BaseHttpEngine.showProgressDialog(); String commConversationId = (String)
	 * BaseDroidApp.getInstanse().getBizDataMap()
	 * .get(ConstantGloble.CONVERSATION_ID);
	 * 
	 * requestPSNGetTokenId(commConversationId); } } catch (CodeException e) {
	 * BaseDroidApp.getInstanse().createDialog(null, e.getMessage());
	 * LogGloble.exceptionPrint(e); } }
	 */

	/** 验证动态口令 */
	private void checkDate() {
		/** 音频Key安全工具认证 */
		usbKeytext.checkDataUsbKey(otpSipBxo, smcSipBxo,
				new IUsbKeyTextSuccess() {

					@Override
					public void SuccessCallBack(String result, int errorCode) {
						// TODO Auto-generated method stub

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
		requestPsnLOANAdvanceRepaySubmit(token);
	}

	/** 提前还款提交交易 */
	private void requestPsnLOANAdvanceRepaySubmit(String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_PSNLOANADVANCEREPAYSUBMIT_API);
		String commConversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Loan.LOAN_LOANTYPE_REQ, loanType);
		map.put(Loan.LOAN_LOANACCOUNT_REQ, loanAccount);
		map.put(Loan.LOAN_CURRENCY_REQ, currency);
		String amount = StringUtil.splitStringwithCode(currency, loanAmount, 2);
		map.put(Loan.LOANACC_LOAN_AMOUNT_RES, amount);
		map.put(Loan.LOAN_LOANPERIOD_REQ, loanPeriod);
		map.put(Loan.LOAN_LOANTODATE_REQ, loanToDate);
		String advanceRepayInterests = StringUtil.splitStringwithCode(currency,
				advanceRepayInterest, 2);
		map.put(Loan.LOAN_ADVANCEREPAYINTEREST_RES, advanceRepayInterests);
		String advanceRepayCapitals = StringUtil.splitStringwithCode(currency,
				advanceRepayCapital, 2);
		map.put(Loan.LOAN_ADVANCEREPAYCAPITAL_RES, advanceRepayCapitals);
		//生产问题修改 预交易repayAmount上送“提前还款本金”（用户输入）和“thisIssueRepayInterest截止当前应还利息”（详情接口返回数据）的总和。
		String thisIssueRepayInterestst=null;
		if(StringUtil.isNullOrEmpty((String) detailRasultMap.get(Loan.LOAN_THISISSUEREPAYINTEREST_REQ))){
			thisIssueRepayInterestst = "0";
		}else {
			thisIssueRepayInterestst = StringUtil.parseStringCodePattern(currency, (String) detailRasultMap.get(Loan.LOAN_THISISSUEREPAYINTEREST_REQ), 2);
		}
		Double thisIssueRepayInterestss = Double.valueOf(thisIssueRepayInterestst);
		map.put(Loan.LOAN_REPAYAMOUNT_RES, String.valueOf(thisIssueRepayInterestss+Double.valueOf(repayAmount)));
		map.put(Loan.LOAN_FROMACCOUNTID_RES, fromAccountId);
		map.put(Loan.LOAN_ACCOUNTNUMBERS_RES, accoutNumber);
		map.put(Loan.LOAN_LOANPERIODUNIT_REQ, loanPeriodUnit);
		String remainCapitals = StringUtil.splitStringwithCode(currency,
				remainCapital, 2);
		map.put(Loan.LOAN_REMAINCAPITAL_REQ, remainCapitals);
		String thisIssueRepayInterests = StringUtil.splitStringwithCode(
				currency, thisIssueRepayInterest, 2);
		map.put(Loan.LOAN_THISISSUEREPAYINTEREST_REQ, thisIssueRepayInterests);
		map.put(Loan.LOAN_REMAINISSUE_REQ, remainIssue);
		String sxf = StringUtil.splitStringwithCode(currency, charges, 2);
		map.put(Loan.LOAN_CHARGES_RES, sxf);
		map.put(Loan.LOAN_THISISSUEREPAYDATE_REQ, thisIssueRepayDate);
		String thisIssueRepayAmounts = StringUtil.splitStringwithCode(currency,
				thisIssueRepayAmount, 2);
		map.put(Loan.LOAN_THISISSUEREPAYAMOUNT_REQ, thisIssueRepayAmounts);
		String everyTermAmounts = StringUtil.splitStringwithCode(currency,
				everyTermAmount, 2);
		map.put(Loan.LOAN_AFTERREPAYISSUEAMOUNT_RES, everyTermAmounts);
		String repayAmountInAdvances = StringUtil.splitStringwithCode(currency,
				repayAmountInAdvance, 2);
		map.put(Loan.LOAN_AFTERREPAYREMAINAMOUNT_RES, repayAmountInAdvances);
		map.put(Loan.LOAN_INTTYPETERES_REQ, interestType);
		map.put(Loan.LOAN_AFTERREPAYISSUES_RES, remainIssueforAdvance);
		// 循环类型
		map.put(Loan.CYCLE_TYPE, cycleType);
		// 线上标识
		map.put(Loan.LOAN_ONLINE_FLAG, onlineFlag);
		map.put(Loan.LOAN_TOKEN_REQ, token);
		/*
		 * if (isOtp) { // 动态口令--- map.put(Comm.Otp, otpPassword);
		 * map.put(Comm.Otp_Rc, otpRandomNum); } if (isSmc) { // 手机校验码
		 * map.put(Comm.Smc, smcPassword); map.put(Comm.Smc_Rc, smcRandomNum); }
		 */
		/** 安全工具参数获取 */
		usbKeytext.InitUsbKeyResult(map);

		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnLOANAdvanceRepaySubmitCallback");
	}

	public void requestPsnLOANAdvanceRepaySubmitCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> result = (Map<String, String>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.LOAN_REPAY_SUCCESS, result);
		gotoActivity();
	}

	private void gotoActivity() {
		Intent intent = new Intent(LoanRepayAccountConfirmActivity.this,
				LoanRepayAccountSuccessActivity.class);
		intent.putExtra(Loan.LOAN_PRIVILEGE_PROCEDURE_RES, privilegeProcedure);
		BaseHttpEngine.dissMissProgressDialog();
		startActivity(intent);
	}
}
