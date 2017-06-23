package com.chinamworld.bocmbci.biz.loan.loanRepay;

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
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 变更还款账户确认信息页面
 * 
 * @author wanbing
 * 
 */
public class LoanChangeLoanRepayAccountConfirmActivity extends LoanBaseActivity {
	private View contentView = null;

	/** 贷款品种 */
	private TextView tv_loan_type;
	/** 贷款账号 */
	private TextView tv_loan_acc_num;
	/** 原还款账号 */
	private TextView tv_loan_pay_acc_num;
	/** 对应还款账户借记卡卡号 */
	private TextView tv_loan_old_pay_acc;

	/** 新的还款卡号/账号 */
	private TextView tv_loan_new_pay_acc;

	private TextView tv_loan_old_pay_acc_label;
	private TextView tv_loan_new_pay_acc_label;

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
	/** 变更还款账户信息 */
	private Map<String, String> loanChangeRepayAccmap;
	/** 变更还款账户预交易请求信息 */
	private Map<String, Object> loanChangeRepayAccPremap;
	/** 变更还款账户预交易结果信息 */
	private Map<String, Object> loanChangeRepayAccPreResultmap;
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
		setTitle(this.getString(R.string.loan_change_repay_acc));
		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setVisibility(View.VISIBLE);
		setLeftSelectedPosition("loan_3");
		contentView = mInflater.inflate(R.layout.loan_change_repay_acc_confirm,
				null);
		tabcontentView.addView(contentView);
		requestForRandomNumber();
		// init();
	}

	private void init() {
		loanChangeRepayAccmap = LoanDataCenter.getInstance()
				.getLoanChangeRepayAccmap();
		loanChangeRepayAccPremap = LoanDataCenter.getInstance()
				.getLoanChangeRepayAccPremap();
		loanChangeRepayAccPreResultmap = LoanDataCenter.getInstance()
				.getLoanChangeRepayAccPreResultmap();
		factorList = (List<Map<String, Object>>) loanChangeRepayAccPreResultmap
				.get(Acc.LOSSCONFIRM_ACC_FACTORLIST_RES);

		// TODO

		String commConversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeytext = (UsbKeyText) contentView.findViewById(R.id.sip_usbkey);
		usbKeytext.Init(commConversationId, randomNumber,
				loanChangeRepayAccPreResultmap, this);
		isOtp = usbKeytext.getIsOtp();
		isSms = usbKeytext.getIsSmc();

		tv_loan_type = (TextView) findViewById(R.id.tv_loan_type);
		tv_loan_acc_num = (TextView) findViewById(R.id.tv_loan_acc_num);
		tv_loan_pay_acc_num = (TextView) findViewById(R.id.tv_loan_pay_acc_num);
		tv_loan_old_pay_acc = (TextView) findViewById(R.id.tv_loan_old_pay_acc);
		String payNumber = String.valueOf(loanChangeRepayAccmap
				.get(Loan.LOAN_CYCLE_REPAYACCOUNT));
		String payNum = null;
		if (StringUtil.isNullOrEmptyCaseNullString(payNumber)) {
			payNum = "-";
		} else {
			payNum = StringUtil.getForSixForString(payNumber);
		}
		tv_loan_pay_acc_num.setText(payNum);
		// 借记卡卡号,没处理过的
		String cardNumsrc = LoanChangeLoanRepayAccountChooseActivity.cardNumsrc;
		// 添加一个判断.如果原还款账户和借记卡卡号为同一个账号,则借记卡卡号显示"-"
		if (cardNumsrc.equals(payNumber)) {
			tv_loan_old_pay_acc.setText(ConstantGloble.BOCINVT_DATE_ADD);
		} else {
			tv_loan_old_pay_acc.setText(StringUtil
					.getForSixForString(cardNumsrc));
		}
		tv_loan_new_pay_acc = (TextView) findViewById(R.id.tv_loan_new_pay_acc);
		tv_loan_old_pay_acc_label = (TextView) findViewById(R.id.tv_loan_old_pay_acc_label);
		tv_loan_new_pay_acc_label = (TextView) findViewById(R.id.tv_loan_new_pay_acc_label);
		nextBtn = (Button) findViewById(R.id.next_btn);

		String loan_type = (String) loanChangeRepayAccmap
				.get(Loan.LOANACC_LOAN_TYPE_RES);
		String loantype = null;
		if (StringUtil.isNull(loan_type)) {
			loantype = "-";
		} else {
			if (LoanData.loanTypeData.containsKey(loan_type)) {
				loantype = LoanData.loanTypeData.get(loan_type);
			} else {
				loantype = "-";
			}
		}
		tv_loan_type.setText(loantype);

		tv_loan_acc_num.setText(StringUtil.getForSixForString(String
				.valueOf(loanChangeRepayAccmap
						.get(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES))));

		tv_loan_new_pay_acc.setText(StringUtil.getForSixForString(String
				.valueOf(loanChangeRepayAccPremap.get(Loan.NEW_PAY_ACCOUNTNUM)
				// LoanChangeLoanRepayReadActivity.numbers
				)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				LoanChangeLoanRepayAccountConfirmActivity.this,
				tv_loan_old_pay_acc_label);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				LoanChangeLoanRepayAccountConfirmActivity.this,
				tv_loan_new_pay_acc_label);

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
			/*
			 * // 动态口令 RegexpBean sipRegexpBean = new RegexpBean(
			 * LoanChangeLoanRepayAccountConfirmActivity
			 * .this.getString(R.string.active_code_regex),
			 * sipBoxActiveCode.getText().toString(), ConstantGloble.SIPOTPPSW);
			 * RegexpBean sipSmcpBean = new RegexpBean(
			 * LoanChangeLoanRepayAccountConfirmActivity
			 * .this.getString(R.string.acc_smc_regex), sipBoxSmc
			 * .getText().toString(), ConstantGloble.SIPSMCPSW);
			 * ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>(); if
			 * (isSms) { lists.add(sipSmcpBean); } if (isOtp) {
			 * lists.add(sipRegexpBean); } if (RegexpUtils.regexpDate(lists))
			 * {// 校验通过 if (isSms) { try { smcStr =
			 * sipBoxSmc.getValue().getEncryptPassword(); smc_password_RC =
			 * sipBoxSmc.getValue().getEncryptRandomNum(); } catch
			 * (CodeException e) { LogGloble.exceptionPrint(e); }
			 * 
			 * } if (isOtp) { try { otpStr =
			 * sipBoxActiveCode.getValue().getEncryptPassword(); otp_password_RC
			 * = sipBoxActiveCode.getValue().getEncryptRandomNum(); } catch
			 * (CodeException e) { LogGloble.exceptionPrint(e); }
			 * 
			 * } requestPSNGetTokenId((String)
			 * BaseDroidApp.getInstanse().getBizDataMap()
			 * .get(ConstantGloble.CONVERSATION_ID));
			 * BiiHttpEngine.showProgressDialog(); }
			 */
		}
	};

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		requestPsnLOANCycleLoanApplySubmit(token);
	}

	/**
	 * 变更还款账户提交交易接口
	 */
	public void requestPsnLOANCycleLoanApplySubmit(String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.PSN_LOAN_CHANGE_LOANREPAY_ACCOUNTSUBMIT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		// token
		loanChangeRepayAccPremap.put(Comm.TOKEN_REQ, token);
		// loanChangeRepayAccPremap.put(Tran.TRAN_ATM_SUB_SIGNEDDATA_REQ,
		// loanChangeRepayAccPremap.get(Tran.TRAN_ATM_PLAINDATA_RES));
		/*
		 * if (isOtp) { loanChangeRepayAccPremap.put(Comm.Otp, otpStr);
		 * loanChangeRepayAccPremap.put(Comm.Otp_Rc, otp_password_RC);
		 * 
		 * } if (isSms) { loanChangeRepayAccPremap.put(Comm.Smc, smcStr);
		 * loanChangeRepayAccPremap.put(Comm.Smc_Rc, smc_password_RC); }
		 */

		/** 安全工具参数获取 */
		usbKeytext.InitUsbKeyResult(loanChangeRepayAccPremap);

		SipBoxUtils.setSipBoxParams(loanChangeRepayAccPremap);
		GetPhoneInfo.initActFirst(this);
		GetPhoneInfo.addPhoneInfo(loanChangeRepayAccPremap);
		biiRequestBody.setParams(loanChangeRepayAccPremap);
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
		Intent intent = new Intent(this,
				LoanChangeLoanRepayAccountSuccessActivity.class);
		startActivity(intent);
	}

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

	/** 判断是动态口令还是手机交易码 */
	// public void initSmcAndOtp() {
	// if (!StringUtil.isNullOrEmpty(factorList)) {
	// for (int i = 0; i < factorList.size(); i++) {
	// Map<String, Object> itemMap = factorList.get(i);
	// Map<String, String> securityMap = (Map<String, String>)
	// itemMap.get(Inves.FIELD);
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
	// //requestForRandomNumber();
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
