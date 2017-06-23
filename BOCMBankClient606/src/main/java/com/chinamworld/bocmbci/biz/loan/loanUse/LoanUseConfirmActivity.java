package com.chinamworld.bocmbci.biz.loan.loanUse;

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
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.biz.loan.LoanData;
import com.chinamworld.bocmbci.biz.loan.LoanDataCenter;
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
 * 用款确认信息页面
 * 
 * @author wanbing
 * 
 */
public class LoanUseConfirmActivity extends LoanBaseActivity {
	private View contentView = null;

	/** 贷款品种 */
	private TextView tv_loan_type;
	/** 可用金额 */
	// private TextView tv_loan_cycleAvaAmount;
	/** 本次用款金额 */
	private TextView tv_use_amt;
	/** 贷款期限 */
	private TextView tv_loan_CycleLifeTerm;
	/** 放款截止日 */
	private TextView tv_loan_cycleDrawdownDate;
	/** 贷款到期日 */
	private TextView tv_loan_cycleMatDate;
	/** 贷款利率 */
	private TextView tv_loan_CycleRate;
	/** 还款账户 */
	private TextView tv_loan_CycleRepayAccount;
	/** 收款账户 */
	private TextView tv_loan_toActNum;
	private Button nextBtn;
	/** 加密之后加密随机数 */
	private String otp_password_RC = "";
	private String smc_password_RC = "";
	/** 加密控件——动态口令 */
	private SipBox sipBoxActiveCode = null;
	/** 加密控件——手机交易码 */
	private SipBox sipBoxSmc = null;
	/** 加密控件里的随机数 */
	protected String randomNumber;
	/** 手机交易码 */
	private LinearLayout ll_smc;
	/** 动态口令 */
	private LinearLayout ll_active_code;
	/** 安全因子数组 */
	public List<Map<String, Object>> factorList;
	/** 是否上传短信验证码 */
	private boolean isSms = false;
	/** 是否上传动态口令 */
	private boolean isOtp = false;
	/** 用款信息 */
	private Map<String, Object> loanUsemap;
	/** 用款预交易信息 */
	private Map<String, Object> loanUsePremap;
	/** 用款预交易结果信息 */
	private Map<String, Object> loanUsePreResultmap;
	/** 动态口令 */
	private String otpStr;
	/** 手机交易码 */
	private String smcStr;

	// TODO
	/** 中银E盾加密控件 */
	UsbKeyText usbKeytext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.loan_use_1));
		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setVisibility(View.VISIBLE);
		setLeftSelectedPosition("loan_2");
		contentView = mInflater.inflate(R.layout.loan_use_confirm, null);
		tabcontentView.addView(contentView);
		// init();
		requestForRandomNumber();
	}

	private void init() {
		loanUsemap = LoanDataCenter.getInstance().getLoanUsemap();
		loanUsePremap = LoanDataCenter.getInstance().getLoanUsePremap();
		loanUsePreResultmap = LoanDataCenter.getInstance()
				.getLoanUsePreResultmap();
		factorList = (List<Map<String, Object>>) loanUsePreResultmap
				.get(Acc.LOSSCONFIRM_ACC_FACTORLIST_RES);

		tv_loan_type = (TextView) findViewById(R.id.tv_loan_type);
		// tv_loan_cycleAvaAmount = (TextView)
		// findViewById(R.id.tv_loan_cycleAvaAmount);
		tv_use_amt = (TextView) findViewById(R.id.tv_use_amt);
		tv_loan_CycleLifeTerm = (TextView) findViewById(R.id.tv_loan_CycleLifeTerm);
		tv_loan_cycleDrawdownDate = (TextView) findViewById(R.id.tv_loan_cycleDrawdownDate);
		tv_loan_cycleMatDate = (TextView) findViewById(R.id.tv_loan_cycleMatDate);
		tv_loan_CycleRate = (TextView) findViewById(R.id.tv_loan_CycleRate);
		tv_loan_CycleRepayAccount = (TextView) findViewById(R.id.tv_loan_CycleRepayAccount);
		tv_loan_toActNum = (TextView) findViewById(R.id.tv_loan_toActNum);
		nextBtn = (Button) this.findViewById(R.id.next_btn);

		String loan_type = (String) loanUsemap.get(Loan.LOANACC_LOAN_TYPE_RES);
		tv_loan_type.setText(LoanData.loanTypeData.get(loan_type));
		String loan_amount = (String.valueOf(loanUsemap
				.get(Loan.LOAN_CYCLE_AVA_AMOUNT)));
		// tv_loan_cycleAvaAmount.setText(StringUtil.parseStringPattern2(
		// loan_amount, 2));
		tv_loan_CycleLifeTerm.setText(String.valueOf(loanUsemap
				.get(Loan.LOAN_CYCLE_LIFETERM)) + "月");
		tv_loan_cycleDrawdownDate.setText(String.valueOf(loanUsemap
				.get(Loan.LOAN_CYCLE_DRAWDOWNDATE)));
		String tv_loan_cycleMatDates = String.valueOf(loanUsemap
				.get(Loan.LOAN_CYCLE_MATDATE));
		tv_loan_cycleMatDate
				.setText(StringUtil
						.isNullOrEmptyCaseNullString(tv_loan_cycleMatDates) ? ConstantGloble.BOCINVT_DATE_ADD
						: tv_loan_cycleMatDates);
		String loanRate = String.valueOf(loanUsemap.get(Loan.LOAN_CYCLERATE));
		float loanRate1 = Float.valueOf(loanRate);
		if (0.0 == loanRate1) {
			tv_loan_CycleRate.setText(ConstantGloble.BOCINVT_DATE_ADD);
		} else {
			tv_loan_CycleRate
					.setText(StringUtil.isNullOrEmptyCaseNullString(loanRate) ? ConstantGloble.BOCINVT_DATE_ADD
							: loanRate + "%");
		}
		String loanCycleRepayAccount = (String) loanUsemap
				.get(Loan.LOAN_CYCLE_REPAYACCOUNT);
		if ("0".equals(loanCycleRepayAccount)) {
			tv_loan_CycleRepayAccount.setText(ConstantGloble.BOCINVT_DATE_ADD);
		} else {
			tv_loan_CycleRepayAccount
					.setText(StringUtil
							.isNullOrEmptyCaseNullString(loanCycleRepayAccount) ? ConstantGloble.BOCINVT_DATE_ADD
							: StringUtil
									.getForSixForString(loanCycleRepayAccount)
					// StringUtil.getForSixForString(String.valueOf(loanCycleRepayAccount))
					);
		}
		tv_loan_toActNum.setText(StringUtil.getForSixForString(String
				.valueOf(loanUsePremap.get(Loan.LOAN_CYCLE_TOACTNUM_REQ))));
		String useAmt = (String
				.valueOf(loanUsePremap.get(Loan.LOAN_AMOUNT_REQ)));
		tv_use_amt.setText(StringUtil.parseStringCodePattern(LoanCycleAccountActivity.currency,useAmt, 2));

		String commConversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeytext = (UsbKeyText) contentView.findViewById(R.id.sip_usbkey);
		usbKeytext.Init(commConversationId, randomNumber, loanUsePreResultmap,
				this);
		isOtp = usbKeytext.getIsOtp();
		isSms = usbKeytext.getIsSmc();

		if (isOtp) {
			// 动态口令
			ll_active_code = (LinearLayout) findViewById(R.id.ll_active_code);
			ll_active_code.setVisibility(View.VISIBLE);
			// 动态口令
			sipBoxActiveCode = (SipBox) findViewById(R.id.sipbox_active);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(sipBoxActiveCode, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//			sipBoxActiveCode.setCipherType(SystemConfig.CIPHERTYPE);
//			sipBoxActiveCode.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			sipBoxActiveCode.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			sipBoxActiveCode.setId(10002);
//			sipBoxActiveCode.setBackgroundResource(R.drawable.bg_for_edittext);
//			sipBoxActiveCode.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			sipBoxActiveCode.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			sipBoxActiveCode.setSingleLine(true);
//			sipBoxActiveCode.setSipDelegator(this);
//			sipBoxActiveCode.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			sipBoxActiveCode.setRandomKey_S(randomNumber);
		}
		if (isSms) {
			// 手机交易码
			ll_smc = (LinearLayout) findViewById(R.id.ll_smc);
			ll_smc.setVisibility(View.VISIBLE);
			// 手机交易码
			sipBoxSmc = (SipBox) findViewById(R.id.sipbox_smc);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(sipBoxSmc, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//			sipBoxSmc.setCipherType(SystemConfig.CIPHERTYPE);
//			sipBoxSmc.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			sipBoxSmc.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			sipBoxSmc.setId(10002);
//			sipBoxSmc.setBackgroundResource(R.drawable.bg_for_edittext);
//			sipBoxSmc.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			sipBoxSmc.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			sipBoxSmc.setSingleLine(true);
//			sipBoxSmc.setSipDelegator(this);
//			sipBoxSmc.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			sipBoxSmc.setRandomKey_S(randomNumber);

			Button smsBtn = (Button) findViewById(R.id.smsbtn);
			SmsCodeUtils.getInstance().addSmsCodeListner(smsBtn,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// 发送验证码到手机
							sendMSCToMobile();
						}
					});
		}

		// 判断
		// initSmcAndOtp();
		nextBtn.setOnClickListener(confirmClickListener);
	}

	/** 确定按钮点击事件 */
	OnClickListener confirmClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			checkDate();
			// 动态口令
			/*
			 * RegexpBean sipRegexpBean = new RegexpBean(
			 * LoanUseConfirmActivity.this
			 * .getString(R.string.active_code_regex),
			 * sipBoxActiveCode.getText().toString(), ConstantGloble.SIPOTPPSW);
			 * RegexpBean sipSmcpBean = new RegexpBean(
			 * LoanUseConfirmActivity.this .getString(R.string.acc_smc_regex),
			 * sipBoxSmc.getText().toString(), ConstantGloble.SIPSMCPSW);
			 * ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>(); if
			 * (isSms) { lists.add(sipSmcpBean); } if (isOtp) {
			 * lists.add(sipRegexpBean); } if (RegexpUtils.regexpDate(lists))
			 * {// 校验通过 if (isSms) { try { smcStr =
			 * sipBoxSmc.getValue().getEncryptPassword(); smc_password_RC =
			 * sipBoxSmc.getValue() .getEncryptRandomNum(); } catch
			 * (CodeException e) { LogGloble.exceptionPrint(e); }
			 * 
			 * } if (isOtp) { try { otpStr = sipBoxActiveCode.getValue()
			 * .getEncryptPassword(); otp_password_RC =
			 * sipBoxActiveCode.getValue() .getEncryptRandomNum(); } catch
			 * (CodeException e) { LogGloble.exceptionPrint(e); }
			 * 
			 * } requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
			 * .getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
			 * BiiHttpEngine.showProgressDialog(); }
			 */
		}
	};

	/** 验证动态口令 */
	private void checkDate() {
		/** 音频Key安全工具认证 */
		usbKeytext.checkDataUsbKey(sipBoxActiveCode, sipBoxSmc,
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
		requestPsnLOANCycleLoanApplySubmit(token);
	}

	/**
	 * 个人循环贷款提交交易
	 */
	public void requestPsnLOANCycleLoanApplySubmit(String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.PSN_LOAN_CYCLELOAN_APPLYSUBMIT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		// token
		loanUsePremap.put(Comm.TOKEN_REQ, token);
		// loanUsePremap.put(Tran.TRAN_ATM_SUB_SIGNEDDATA_REQ,
		// loanUsePreResultmap.get(Tran.TRAN_ATM_PLAINDATA_RES));
		if (isOtp) {
			loanUsePremap.put(Comm.Otp, otpStr);
			loanUsePremap.put(Comm.Otp_Rc, otp_password_RC);

		}
		if (isSms) {
			loanUsePremap.put(Comm.Smc, smcStr);
			loanUsePremap.put(Comm.Smc_Rc, smc_password_RC);
		}
		/** 安全工具参数获取 */
		usbKeytext.InitUsbKeyResult(loanUsePremap);

		SipBoxUtils.setSipBoxParams(loanUsePremap);
		GetPhoneInfo.initActFirst(this);
		GetPhoneInfo.addPhoneInfo(loanUsePremap);
		biiRequestBody.setParams(loanUsePremap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnLOANCycleLoanApplySubmitCallBack");
	}

	/**
	 * 个人循环贷款提交交易回调
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void requestPsnLOANCycleLoanApplySubmitCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = (Map<String, Object>) (biiResponseBody
				.getResult());
		if (StringUtil.isNullOrEmpty(result)) {
			return;
		}
		String transactionId = (String) result.get("transactionId");
		Intent intent = new Intent(this, LoanUseSuccessActivity.class);
		intent.putExtra("transactionId", transactionId);
		startActivity(intent);
	}

	/** 判断是动态口令还是手机交易码 */
	// public void initSmcAndOtp() {
	// if (!StringUtil.isNullOrEmpty(factorList)) {
	// for (int i = 0; i < factorList.size(); i++) {
	// Map<String, Object> itemMap = factorList.get(i);
	// Map<String, String> securityMap = (Map<String, String>) itemMap
	// .get(Inves.FIELD);
	// String name = securityMap.get(Inves.NAME);
	// if (Inves.Smc.equals(name)) {
	// isSms = true;
	// ll_smc.setVisibility(View.VISIBLE);
	// } else if (Inves.Otp.equals(name)) {
	// isOtp = true;
	// ll_active_code.setVisibility(View.VISIBLE);
	// }
	// }
	// }
	// requestForRandomNumber();
	// }

	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"queryRandomNumberCallBack");
	}

	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		randomNumber = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(randomNumber)) {
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}
		// 加密控件设置随机数
		// sipBoxActiveCode.setRandomKey_S(randomNumber);
		// sipBoxSmc.setRandomKey_S(randomNumber);
		BiiHttpEngine.dissMissProgressDialog();
		init();

	}

	public void sendMSCToMobile() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.SET_SENDMSC);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "sendMSCToMobileCallback");
	}

	public void sendMSCToMobileCallback(Object resultObj) {
	}

}
