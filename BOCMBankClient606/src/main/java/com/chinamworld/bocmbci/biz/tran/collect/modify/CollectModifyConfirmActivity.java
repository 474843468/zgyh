package com.chinamworld.bocmbci.biz.tran.collect.modify;

import java.math.BigDecimal;
import java.util.HashMap;
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
import com.chinamworld.bocmbci.bii.constant.Collect;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectCycleCodeType;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectCycleType;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectImputationMode;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.collect.CollectBaseActivity;
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
 * @ClassName: CollectModifyConfirmActivity
 * @Description: 跨行资金归集确定
 * @author luql
 * @date 2014-3-18 下午02:33:12
 */
public class CollectModifyConfirmActivity extends CollectBaseActivity {

	private static final int request_modifyCode = 10001;
	private View mViewContent;
	private View mNextView;
	private SipBox mSmsSipBox;
	private SipBox mOptSipBox;
	/** 中银E盾 */
	private UsbKeyText usbKeyText;
	// --------------------------------------
	// 上传参数
	/** 规则编号 */
	private String mRuleNoParam;
	/** 归集账户ID */
	private String mAccountId;
	/** 归集账户卡号 */
	private String mAccCardParam;
	/** 归集账户账户 */
	private String mAccountNumParam;
	/** 被归集账户开户行名称 */
	private String mPayerAccBankNameParam;
	/** 被归集账户账号/卡号 */
	private String mPayerAccountParam;
	/** 被归集账户户名 */
	private String mPayerAccNameParam;
	/** 客户手机号 */
	private String mMobileParam;
	/** 金额参数 */
	private String mAmountParam;
	/** 归集方式参数 */
	private String mImputationModeParam;
	/** 归集周期参数 */
	private String mCycleParam;
	/** 归集周期执行日参数 */
	private String mCycleCodeParam;
	/** 是否发送短信通知 */
	private boolean mIfMessageParam;

	// /** 归集账户户名 */
	// private String mAccountNameParam;
	// /** 归集账户类型 */
	// private String mAccountTypeParam;
	// /** 归集账户开户行名称 */
	// private String mBankNameParam;
	// /** 被归集账户人行行号 */
	// private String mPayerAcctBankNo;

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
	/** 报文数据返回 */
	Map<String, Object> atmpremap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewContent = addView(R.layout.collect_modify_confirm);
		setTitle(getString(R.string.collect_modify_title));
		atmpremap = TranDataCenter.getInstance().getAtmpremap();
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
				checkSubmit();

			}
		});
	}

	private void checkSubmit() {
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
		/** 安全工具数据校验 */
		usbKeyText.checkDataUsbKey(mOptSipBox, mSmsSipBox,
				new IUsbKeyTextSuccess() {

					@Override
					public void SuccessCallBack(String result, int errorCode) {
						// TODO Auto-generated method stub
						// 获取tokenId
						// BaseHttpEngine.showProgressDialog();
						requestPSNGetTokenId((String) BaseDroidApp
								.getInstanse().getBizDataMap()
								.get(ConstantGloble.CONVERSATION_ID));
					}
				});
		// return isSuccess;
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		tokenId = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		requestPsnCBCollectModify();
	}

	private void requestPsnCBCollectModify() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Collect.PsnCBCollectModify);
		Map<String, Object> map = new HashMap<String, Object>();

		map.put(Collect.ruleNo, mRuleNoParam);
		map.put(Collect.cbAccId, mAccountId);
		map.put(Collect.cbAccCard, mAccCardParam);
		map.put(Collect.cbAccNum, mAccountNumParam);
		map.put(Collect.payerAccBankName, mPayerAccBankNameParam);
		map.put(Collect.payerAccount, mPayerAccountParam);
		map.put(Collect.payerAcntName, mPayerAccNameParam);
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
		map.put(Comm.PUBLIC_TOKEN, tokenId);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnCBCollectModifyCallback");
	}

	public void requestPsnCBCollectModifyCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		// BiiResponse biiResponse = (BiiResponse) resultObj;
		// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// Map<String, Object> map = (Map<String, Object>)
		// biiResponseBody.getResult();
		// String ruleNo = (String) map.get(Collect.ruleNo);
		openModifyResultActivity();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == request_modifyCode && resultCode == RESULT_OK) {
			setResult(RESULT_OK);
			finish();
		}
	}

	private void openModifyResultActivity() {
		Intent intent = new Intent(this, CollectModifyResultActivity.class);
		intent.putExtra(Collect.cbAccNum, mAccCardParam);
		intent.putExtra(Collect.payerAccBankName, mPayerAccBankNameParam);
		intent.putExtra(Collect.payerAccount, mPayerAccountParam);
		intent.putExtra(Collect.payerAccountName, mPayerAccNameParam);
		intent.putExtra(Collect.imputationMode, mImputationModeParam);
		intent.putExtra(Collect.cycle, mCycleParam);
		if (!CollectImputationMode.ALL.equals(mCycleParam)) {
			// 不是全集模式
			intent.putExtra(Collect.cycleCode, mCycleCodeParam);
		}
		intent.putExtra(Collect.amount, mAmountParam);
		// if (CollectImputationMode.KEEP.equals(mImputationModeParam)) {
		// intent.putExtra(Collect.amount, mAmountParam);
		// } else if (CollectImputationMode.QUOTA.equals(mImputationModeParam))
		// {
		// intent.putExtra(Collect.amount, mAmountParam);
		// }
		intent.putExtra(Collect.ifMessage, mIfMessageParam);
		intent.putExtra(Collect.cbMobile, mMobileParam);
		startActivityForResult(intent, request_modifyCode);
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
		/** 留存金额 */
		TextView retainAmountView = (TextView) mViewContent
				.findViewById(R.id.tv_retain_amount);
		TextView amountLableView = (TextView) mViewContent
				.findViewById(R.id.tv_amount_lable);
		/** 归集规则 */
		TextView collectRuleView = (TextView) mViewContent
				.findViewById(R.id.tv_rule);
		/** 归集规则执行日 */
		TextView collectRuleCodeView = (TextView) mViewContent
				.findViewById(R.id.tv_rule_code);
		/** 归集手机号 */
		TextView payeeMobileView = (TextView) findViewById(R.id.tv_payee_mobile);

		// mViewContent.findViewById(R.id.layout_sms).setVisibility(isNeedSmc ?
		// View.VISIBLE : View.GONE);
		// mViewContent.findViewById(R.id.layout_otp).setVisibility(isNeedOtp ?
		// View.VISIBLE : View.GONE);
		mViewContent.findViewById(R.id.collect_mobile).setVisibility(
				mIfMessageParam ? View.VISIBLE : View.GONE);
		payeeMobileView.setVisibility(mIfMessageParam ? View.VISIBLE
				: View.GONE);

		/** 跟据返回初始化中银E盾加密控件 */
		initSipBox();

		if (isNeedSmc) {
			mViewContent.findViewById(R.id.layout_sms).setVisibility(View.VISIBLE);
			mSmsSipBox = (SipBox) mViewContent.findViewById(R.id.sip_sms);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(mSmsSipBox, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//			mSmsSipBox.setCipherType(SystemConfig.CIPHERTYPE);
//			mSmsSipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			mSmsSipBox.setId(10002);
//			mSmsSipBox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			mSmsSipBox.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			mSmsSipBox.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
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
			mViewContent.findViewById(R.id.layout_otp).setVisibility(View.VISIBLE);
			mOptSipBox = (SipBox) mViewContent.findViewById(R.id.sip_opt);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(mOptSipBox, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//			mOptSipBox.setCipherType(SystemConfig.CIPHERTYPE);
//			mOptSipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			mOptSipBox.setId(10002);
//			mOptSipBox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			mOptSipBox.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			mOptSipBox.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			mOptSipBox.setSipDelegator(this);
//			mOptSipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			mOptSipBox.setRandomKey_S(randomNumber);
			/** */
			// mNextView = mViewContent.findViewById(R.id.btnconfirm);
		}
		mNextView = mViewContent.findViewById(R.id.btnconfirm);
		payeeAccountView.setText(StringUtil.getForSixForString(mAccCardParam));
		payerAccountView.setText(StringUtil
				.getForSixForString(mPayerAccountParam));
		payerNameView.setText(mPayerAccNameParam);
		payerBankView.setText(mPayerAccBankNameParam);
		collectModeView.setText(CollectImputationMode
				.getTypeStr(mImputationModeParam));

		if (CollectImputationMode.ALL.equals(mImputationModeParam)) {
			mViewContent.findViewById(R.id.amount_layout).setVisibility(
					View.GONE);
		} else if (CollectImputationMode.KEEP.equals(mImputationModeParam)) {
			mViewContent.findViewById(R.id.amount_layout).setVisibility(
					View.VISIBLE);
			retainAmountView.setText(StringUtil.parseStringPattern(
					mAmountParam, 2));
			amountLableView.setText(R.string.collect_retain_amount);
		} else if (CollectImputationMode.QUOTA.equals(mImputationModeParam)) {
			mViewContent.findViewById(R.id.amount_layout).setVisibility(
					View.VISIBLE);
			retainAmountView.setText(StringUtil.parseStringPattern(
					mAmountParam, 2));
			amountLableView.setText(R.string.collect_transferAmount_amount);
		}

		collectRuleView.setText(CollectCycleType.getCycleTypeStr(mCycleParam));
		// 执行日
		if (CollectCycleType.DAY.equals(mCycleParam)) {// 日
			mViewContent.findViewById(R.id.rule_code_layout).setVisibility(
					View.GONE);
			collectRuleCodeView.setText("-");
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

		payeeMobileView.setText(mMobileParam);
		// if (mIfMessageParam) {
		// findViewById(R.id.mobile_layout).setVisibility(View.VISIBLE);
		// } else {
		// findViewById(R.id.mobile_layout).setVisibility(View.GONE);
		// }

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
		usbKeyText.Init(mmconversationId, randomNumber, atmpremap, this);
		isNeedOtp = usbKeyText.getIsOtp();
		isNeedSmc = usbKeyText.getIsSmc();
		if (usbKeyText.isNeedUsbKey()) {
			textUsb.setVisibility(View.VISIBLE);
		}
	}

	private boolean getIntentData() {
		Intent intent = getIntent();
		mRuleNoParam = intent.getStringExtra(Collect.ruleNo);
		mAccountId = intent.getStringExtra(Collect.cbAccId);
		mAccCardParam = intent.getStringExtra(Collect.cbAccCard);
		mAccountNumParam = intent.getStringExtra(Collect.cbAccNum);

		mPayerAccBankNameParam = intent
				.getStringExtra(Collect.payerAccBankName);
		mPayerAccountParam = intent.getStringExtra(Collect.payerAccount);
		mPayerAccNameParam = intent.getStringExtra(Collect.payerAcntName);

		mImputationModeParam = intent.getStringExtra(Collect.imputationMode);
		mCycleParam = intent.getStringExtra(Collect.cycle);
		mCycleCodeParam = intent.getStringExtra(Collect.cycleCode);
		mAmountParam = intent.getStringExtra(Collect.amount);
		mIfMessageParam = intent.getBooleanExtra(Collect.ifMessage, true);
		mMobileParam = intent.getStringExtra(Collect.cbMobile);

		// isNeedSmc = intent.getBooleanExtra("isNeedSmc", true);
		// isNeedOtp = intent.getBooleanExtra("isNeedOtp", true);
		randomNumber = intent.getStringExtra("randomNum");
		return true;
	}
}
