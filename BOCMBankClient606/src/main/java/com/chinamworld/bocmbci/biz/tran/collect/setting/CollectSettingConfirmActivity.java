package com.chinamworld.bocmbci.biz.tran.collect.setting;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Collect;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectCycleCodeType;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectCycleType;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectImputationMode;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.collect.CollectBaseActivity;
import com.chinamworld.bocmbci.constant.CheckRegExp;
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
 * @ClassName: CollectSettingConfirmActivity
 * @Description: 跨行资金归集确定
 * @author luql
 * @date 2014-3-18 下午02:33:12
 */
public class CollectSettingConfirmActivity extends CollectBaseActivity {

	private static final int request_setting_Code = 10001;
	private View mViewContent;
	private View mNextView;
	private SipBox mSmsSipBox;
	private SipBox mOptSipBox;
	/** 中银E盾 */
	private UsbKeyText usbKeyText;
	// --------------------------------------
	// 上传参数
	/** 客户手机号 */
	private String mMobileParam;
	/** 金额参数 */
	private String mAmountParam;
	/** 归集方式参数 */
	private String mImputationModeParam;
	/** 安全因子组合id参数 */
	private String combinIdParam;
	/** 归集周期参数 */
	private String mCycleParam;
	/** 归集周期执行日参数 */
	private String mCycleCodeParam;
	/** 是否发送短信通知 */
	private boolean mIfMessageParam;
	private String mAccountIdParam;
	/** 归集账户卡号 */
	private String mAccCardParam;
	/** 归集账户卡号 */
	private String mAccountNumParam;
	/** 归集账户户名 */
	private String mAccountNameParam;
	/** 归集账户类型 */
	private String mAccountTypeParam;
	/** 归集账户开户行名称 */
	private String mBankNameParam;
	/** 被归集账户人行行号 */
	private String mPayerAcctBankNo;
	/** 被归集账户开户行名称 */
	private String mPayerAccBankNameParam;
	/** 被归集账户账号/卡号 */
	private String mPayerAccountParam;
	/** 被归集账户户名 */
	private String mPayerAccNameParam;
	/** 被归集账户类型 */
	private String mPayerAcctTypeParam;
	/** RCPS查询协议号 */
	private String mQueryNoParam;
	/** RCPS查询协议号开始日期 */
	private String mQueryBeginParam;
	/** RCPS查询协议号结束日期 */
	private String mQueryEndParam;
	/** RCPS查询协议状态 */
	private String mQueryStatusParam;
	/** RCPS支付协议号 */
	private String mPayNoParam;
	/** RCPS支付协议号开始日期 */
	private String mPayBeginParam;
	/** RCPS支付协议号结束日期 */
	private String mPayEndParam;
	/** RCPS支付协议状态 */
	private String mPayStatusParam;
	/** 业务类型编码 */
	private String mPayTypeNoParam;
	/** 业务种类编码 */
	private String mPaySortNoParam;
	/** 支付协议单笔业务金额上限 */
	private String mPayQuotePerParam;
	/** 支付协议日累计业务笔数上限 */
	private String mPayLimitDParam;
	/** 支付协议日累计金额上限 */
	private String mPayQuotaDParam;
	/** 支付协议月累计业务笔数上限 */
	private String mPayLimitMParam;
	/** 支付协议月累计金额上限 */
	private String mPayQuotaMParam;
	/** 是否需要手机验证码 */
	private boolean isNeedSmc;
	/** 是否需要动态口令 */
	private boolean isNeedOtp;
	private String randomNumber;
	private String tokenId;

	private String smcPwdParam;
	private String smcPwdRcParam;
	private String otpPwdParam;
	private String otpPwdRcParam;
	/** 报文返回result */
	private Map<String, Object> resultRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewContent = addView(R.layout.collect_setting_confirm);
		setTitle(getString(R.string.collect_setting_title));
		resultRequest = TranDataCenter.getInstance().getAtmpremap();
		if (getIntentData()) {
			toprightBtn();
			findView();
			setListener();
		} else {
			finish();
		}
	}

	private void setListener() {
		mNextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// if (checkSubmit()) {
				// // 获取tokenId
				// BaseHttpEngine.showProgressDialog();
				// requestPSNGetTokenId((String)
				// BaseDroidApp.getInstanse().getBizDataMap()
				// .get(ConstantGloble.CONVERSATION_ID));
				// }
				/** 安全工具数据校验 */
				usbKeyText.checkDataUsbKey(mOptSipBox, mSmsSipBox,
						new IUsbKeyTextSuccess() {

							@Override
							public void SuccessCallBack(String result,
									int errorCode) {
								// TODO Auto-generated method stub
								// 获取tokenId
								BaseHttpEngine.showProgressDialog();
								requestPSNGetTokenId((String) BaseDroidApp
										.getInstanse().getBizDataMap()
										.get(ConstantGloble.CONVERSATION_ID));
							}
						});

			}
		});
	}

	// private boolean checkSubmit() {
	// boolean isSuccess = true;
	// RegexpBean rbSms = new
	// RegexpBean(getResources().getString(R.string.set_smc_no),
	// EpayUtil.getString(
	// mSmsSipBox.getText(), ""), "smc");
	// RegexpBean rbOtp = new
	// RegexpBean(getResources().getString(R.string.active_code_regex),
	// EpayUtil.getString(
	// mOptSipBox.getText(), ""), "otp");
	// ArrayList<RegexpBean> list = new ArrayList<RegexpBean>();
	//
	// if (isNeedSmc) {
	// list.add(rbSms);
	// }
	// if (isNeedOtp) {
	// list.add(rbOtp);
	// }
	//
	// if (RegexpUtils.regexpDate(list)) {
	// if (isNeedOtp) {
	// try {
	// otpPwdParam = mOptSipBox.getValue().getEncryptPassword();
	// otpPwdRcParam = mOptSipBox.getValue().getEncryptRandomNum();
	// } catch (CodeException e) {
	// BaseDroidApp.getInstanse().showInfoMessageDialog(e.getMessage());
	// isSuccess = false;
	// }
	// }
	// if (isNeedSmc) {
	// try {
	// smcPwdParam = mSmsSipBox.getValue().getEncryptPassword();
	// smcPwdRcParam = mSmsSipBox.getValue().getEncryptRandomNum();
	// } catch (CodeException e) {
	// BaseDroidApp.getInstanse().showInfoMessageDialog(e.getMessage());
	// isSuccess = false;
	// }
	// }
	// } else {
	// isSuccess = false;
	// }
	// return isSuccess;
	// }

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		tokenId = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		requestPsnCBCollectAdd();
	}

	private void requestPsnCBCollectAdd() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Collect.PsnCBCollectAdd);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Collect.cbAccId, mAccountIdParam);
		map.put(Collect.cbAccCard, mAccCardParam);
		map.put(Collect.cbAccNum, mAccountNumParam);
		map.put(Collect.cbAccName, mAccountNameParam);
		map.put(Collect.cbAccType, mAccountTypeParam);
		map.put(Collect.cbAccBankName, mBankNameParam);
		map.put(Collect.payerAcctBankNo, mPayerAcctBankNo);
		map.put(Collect.payerAccBankName, mPayerAccBankNameParam);
		map.put(Collect.payerAccount, mPayerAccountParam);
		map.put(Collect.payerAcntName, mPayerAccNameParam);
		map.put(Collect.payerAcctType, mPayerAcctTypeParam);
		map.put(Collect.queryNo, mQueryNoParam);
		map.put(Collect.queryBegin, mQueryBeginParam);
		map.put(Collect.queryEnd, mQueryEndParam);
		map.put(Collect.queryStatus, mQueryStatusParam);
		map.put(Collect.payNo, mPayNoParam);
		map.put(Collect.payBegin, mPayBeginParam);
		map.put(Collect.payEnd, mPayEndParam);
		map.put(Collect.payStatus, mPayStatusParam);
		map.put(Collect.payTypeNo, mPayTypeNoParam);
		map.put(Collect.paySortNo, mPaySortNoParam);
		map.put(Collect.payQuotaPer, mPayQuotePerParam);
		map.put(Collect.payLimitD, mPayLimitDParam);
		map.put(Collect.payQuotaD, mPayQuotaDParam);
		map.put(Collect.payLimitM, mPayLimitMParam);
		map.put(Collect.payQuotaM, mPayQuotaMParam);
		map.put(Collect.currency, "001"); // "人民币元"
		// map.put(Collect.noteType, "02"); // "现汇"="02", "现钞"="01"

		// 规则参数
		map.put(Collect.imputationMode, mImputationModeParam);
		map.put(Collect.cycle, mCycleParam);
		if (!CollectCycleType.DAY.equalsIgnoreCase(mCycleParam)) {
			map.put(Collect.cycleCode, mCycleCodeParam);
		}
		if (CollectImputationMode.KEEP.equals(mImputationModeParam)) {
			map.put(Collect.retainAmt, new BigDecimal(mAmountParam));
		} else if (CollectImputationMode.QUOTA.equals(mImputationModeParam)) {
			map.put(Collect.transferAmount, new BigDecimal(mAmountParam));
		}
		map.put(Collect.ifMessage, mIfMessageParam ? "Y" : "N");
		if (mIfMessageParam) {
			map.put(Collect.cbMobile, mMobileParam);
		}
		map.put(Collect.combinId, combinIdParam);

		map.put(Comm.PUBLIC_TOKEN, tokenId);
		// if (isNeedOtp) {
		// map.put(Comm.Otp, otpPwdParam);
		// map.put(Comm.Otp_Rc, otpPwdRcParam);
		// }
		// if (isNeedSmc) {
		// map.put(Comm.Smc, smcPwdParam);
		// map.put(Comm.Smc_Rc, smcPwdRcParam);
		// }
		/** 安全工具参数获取 */
		usbKeyText.InitUsbKeyResult(map);
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnCBCollectAddCallback");
	}

	public void requestPsnCBCollectAddCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> map = (Map<String, Object>) biiResponseBody
				.getResult();
		String ruleNo = (String) map.get(Collect.ruleNo);
		openSeetingResultActivity(ruleNo);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == request_setting_Code) {
				setResult(RESULT_OK);
				finish();
			}
		}
	}

	private void openSeetingResultActivity(String ruleNo) {
		Intent intent = new Intent(this, CollectSettingResultActivity.class);
		intent.putExtra(Collect.ruleNo, ruleNo);
		intent.putExtra(Collect.cbAccCard, mAccCardParam);
		intent.putExtra(Collect.cbAccNum, mAccountNumParam);
		intent.putExtra(Collect.cbAccName, mAccountNameParam);
		intent.putExtra(Collect.cbAccType, mAccountTypeParam);
		intent.putExtra(Collect.cbAccBankName, mBankNameParam);
		intent.putExtra(Collect.payerAcctBankNo, mPayerAcctBankNo);
		intent.putExtra(Collect.payerAccBankName, mPayerAccBankNameParam);
		intent.putExtra(Collect.payerAccount, mPayerAccountParam);
		intent.putExtra(Collect.payerAccountName, mPayerAccNameParam);
		intent.putExtra(Collect.imputationMode, mImputationModeParam);
		intent.putExtra(Collect.cycle, mCycleParam);
		intent.putExtra(Collect.cycleCode, mCycleCodeParam);
		intent.putExtra(Collect.amount, mAmountParam);
		intent.putExtra(Collect.ifMessage, mIfMessageParam);
		intent.putExtra(Collect.cbMobile, mMobileParam);
		startActivityForResult(intent, request_setting_Code);
	}

	private void findView() {
		/** 归集账户 */
		TextView payeeAccountView = (TextView) mViewContent
				.findViewById(R.id.tv_payee_account);
		/** 被归集账户 */
		TextView payerAccountView = (TextView) mViewContent
				.findViewById(R.id.tv_payer_account);
		/** 被归集账户户名 */
		TextView payerNameView = (TextView) mViewContent
				.findViewById(R.id.tv_payer_name);
		/** 被归集账户开户行 */
		TextView payerBankView = (TextView) mViewContent
				.findViewById(R.id.tv_payer_bank);
		/** 归集方式 */
		TextView collectModeView = (TextView) mViewContent
				.findViewById(R.id.tv_collect_mode);

		TextView amountLableView = (TextView) mViewContent
				.findViewById(R.id.tv_amount_lable);
		/** 留存金额 */
		TextView retainAmountView = (TextView) mViewContent
				.findViewById(R.id.tv_retain_amount);
		/** 归集规则 */
		TextView collectRuleView = (TextView) mViewContent
				.findViewById(R.id.tv_rule);
		/** 归集规则执行日 */
		TextView collectRuleCodeView = (TextView) mViewContent
				.findViewById(R.id.tv_rule_code);
		/** 归集手机号 */
		TextView payeeMobileView = (TextView) mViewContent
				.findViewById(R.id.tv_payee_mobile);

		/** 跟据返回初始化中银E盾加密控件 */
		initSipBox();

		// findViewById(R.id.layout_sms).setVisibility(isNeedSmc ? View.VISIBLE
		// : View.GONE);
		// findViewById(R.id.layout_otp).setVisibility(isNeedOtp ? View.VISIBLE
		// : View.GONE);

		if (isNeedSmc) {
			findViewById(R.id.layout_sms).setVisibility(View.VISIBLE);
			mSmsSipBox = (SipBox) findViewById(R.id.sip_sms);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(mSmsSipBox, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//			mSmsSipBox.setCipherType(SystemConfig.CIPHERTYPE);
//			mSmsSipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			mSmsSipBox.setId(10002);
//			mSmsSipBox.setPasswordMinLength(6);
//			mSmsSipBox.setPasswordMaxLength(6);
//			mSmsSipBox.setPasswordRegularExpression(CheckRegExp.OTP);
//			mSmsSipBox.setSipDelegator(this);
//			mSmsSipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			mSmsSipBox.setRandomKey_S(randomNumber);
			Button bt_smsbtn = (Button) mViewContent.findViewById(R.id.smsbtn);
			SmsCodeUtils.getInstance().addSmsCodeListner(bt_smsbtn,
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							// 发送获取短信验证码的请求
							sendSMSCToMobile();
						}
					});
		}
		if (isNeedOtp) {
			findViewById(R.id.layout_otp).setVisibility(View.VISIBLE);
			mOptSipBox = (SipBox) findViewById(R.id.sip_opt);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(mOptSipBox, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//			mOptSipBox.setCipherType(SystemConfig.CIPHERTYPE);
//			mOptSipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			mOptSipBox.setId(10002);
//			mOptSipBox.setPasswordMinLength(6);
//			mOptSipBox.setPasswordMaxLength(6);
//			mOptSipBox.setPasswordRegularExpression(CheckRegExp.OTP);
//			mOptSipBox.setSipDelegator(this);
//			mOptSipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			mOptSipBox.setRandomKey_S(randomNumber);
		}

		/** */
		mNextView = findViewById(R.id.btnconfirm);

		// Button bt_smsbtn = (Button) mViewContent.findViewById(R.id.smsbtn);
		// SmsCodeUtils.getInstance().addSmsCodeListner(bt_smsbtn, new
		// View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // 发送获取短信验证码的请求
		// sendSMSCToMobile();
		// }
		// });

		payeeAccountView.setText(StringUtil
				.getForSixForString(mAccountNumParam));
		payerAccountView.setText(StringUtil
				.getForSixForString(mPayerAccountParam));
		payerNameView.setText(mPayerAccNameParam);
		payerBankView.setText(mPayerAccBankNameParam);
		collectModeView.setText(CollectImputationMode
				.getTypeStr(mImputationModeParam));

		if (CollectImputationMode.ALL.equals(mImputationModeParam)) {
			findViewById(R.id.amount_layout).setVisibility(View.GONE);
			retainAmountView.setVisibility(View.GONE);
		} else if (CollectImputationMode.KEEP.equals(mImputationModeParam)) {
			findViewById(R.id.amount_layout).setVisibility(View.VISIBLE);
			// 留存金额
			retainAmountView.setVisibility(View.VISIBLE);
			retainAmountView.setText(StringUtil.parseStringPattern(
					mAmountParam, 2));
			amountLableView.setText(R.string.collect_retain_amount);
		} else if (CollectImputationMode.QUOTA.equals(mImputationModeParam)) {
			findViewById(R.id.amount_layout).setVisibility(View.VISIBLE);
			// 定额金额
			retainAmountView.setVisibility(View.VISIBLE);
			retainAmountView.setText(StringUtil.parseStringPattern(
					mAmountParam, 2));
			amountLableView.setText(R.string.collect_transferAmount_amount);
		}

		collectRuleView.setText(CollectCycleType.getCycleTypeStr(mCycleParam));
		// 执行日
		if (CollectCycleType.DAY.equals(mCycleParam)) {// 日
			collectRuleCodeView.setText("-");
			mViewContent.findViewById(R.id.rule_code_layout).setVisibility(
					View.GONE);
		} else if (CollectCycleType.WEEK.equals(mCycleParam)) {// 周
			collectRuleCodeView.setText(CollectCycleCodeType
					.getWeekTypeStr(mCycleCodeParam));
			mViewContent.findViewById(R.id.rule_code_layout).setVisibility(
					View.VISIBLE);
		} else if (CollectCycleType.MONTH.equals(mCycleParam)) { // 月
			collectRuleCodeView.setText(CollectCycleCodeType
					.getMonthTypeStr(mCycleCodeParam));
			mViewContent.findViewById(R.id.rule_code_layout).setVisibility(
					View.VISIBLE);
		}

		if (mIfMessageParam) {
			payeeMobileView.setText(mMobileParam);
			findViewById(R.id.mobile_layout).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.mobile_layout).setVisibility(View.GONE);
		}

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				payeeAccountView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				payerAccountView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				payerNameView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				payerBankView);
	}

	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox() {
		usbKeyText = (UsbKeyText) mViewContent.findViewById(R.id.sip_usbkey);
		TextView textUsb = (TextView) findViewById(R.id.text_usb);
		String mmconversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeyText.Init(mmconversationId, randomNumber, resultRequest, this);
		isNeedOtp = usbKeyText.getIsOtp();
		isNeedSmc = usbKeyText.getIsSmc();
		if (usbKeyText.isNeedUsbKey()) {
			textUsb.setVisibility(View.VISIBLE);
		}
	}

	private boolean getIntentData() {
		Intent intent = getIntent();

		// intent.putExtra(Collect.cbAccId, mAccountIdParam);
		// intent.putExtra(Collect.cbAccCard, mAccCardParam);
		mAccountIdParam = intent.getStringExtra(Collect.cbAccId);
		mAccCardParam = intent.getStringExtra(Collect.cbAccCard);
		mAccountNumParam = intent.getStringExtra(Collect.cbAccCard);
		mAccountNameParam = intent.getStringExtra(Collect.cbAccName);
		mAccountTypeParam = intent.getStringExtra(Collect.cbAccType);
		mBankNameParam = intent.getStringExtra(Collect.cbAccBankName);
		mPayerAcctBankNo = intent.getStringExtra(Collect.payerAcctBankNo);
		mPayerAccBankNameParam = intent
				.getStringExtra(Collect.payerAccBankName);
		mPayerAccountParam = intent.getStringExtra(Collect.payerAccount);
		mPayerAccNameParam = intent.getStringExtra(Collect.payerAccountName);
		mPayerAcctTypeParam = intent.getStringExtra(Collect.payerAcctType);
		mQueryNoParam = intent.getStringExtra(Collect.queryNo);
		mQueryBeginParam = intent.getStringExtra(Collect.queryBegin);
		mQueryEndParam = intent.getStringExtra(Collect.queryEnd);
		mQueryStatusParam = intent.getStringExtra(Collect.queryStatus);
		mPayNoParam = intent.getStringExtra(Collect.payNo);
		mPayBeginParam = intent.getStringExtra(Collect.payBegin);
		mPayEndParam = intent.getStringExtra(Collect.payEnd);
		mPayStatusParam = intent.getStringExtra(Collect.payStatus);
		mPayTypeNoParam = intent.getStringExtra(Collect.payTypeNo);
		mPaySortNoParam = intent.getStringExtra(Collect.paySortNo);
		mPayQuotePerParam = intent.getStringExtra(Collect.payQuotaPer);
		mPayLimitDParam = intent.getStringExtra(Collect.payLimitD);
		mPayQuotaDParam = intent.getStringExtra(Collect.payQuotaD);
		mPayLimitMParam = intent.getStringExtra(Collect.payLimitM);
		mPayQuotaMParam = intent.getStringExtra(Collect.payQuotaM);

		mImputationModeParam = intent.getStringExtra(Collect.imputationMode);
		mCycleParam = intent.getStringExtra(Collect.cycle);
		mCycleCodeParam = intent.getStringExtra(Collect.cycleCode);
		mAmountParam = intent.getStringExtra(Collect.amount);
		mIfMessageParam = intent.getBooleanExtra(Collect.ifMessage, false);

		mMobileParam = intent.getStringExtra(Collect.cbMobile);

		// isNeedSmc = intent.getBooleanExtra("isNeedSmc", true);
		// isNeedOtp = intent.getBooleanExtra("isNeedOtp", true);
		randomNumber = intent.getStringExtra("randomNum");
		return true;
	}
}
