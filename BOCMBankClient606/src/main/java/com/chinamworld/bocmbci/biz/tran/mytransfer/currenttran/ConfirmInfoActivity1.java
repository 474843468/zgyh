package com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.CheckRegExp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.GetPhoneInfo;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 非关联信用卡还款 转账确认信息
 * 
 * @author
 * 
 */
@SuppressWarnings("ResourceType")
public class ConfirmInfoActivity1 extends TranBaseActivity implements
		OnClickListener {

	private static final String TAG = ConfirmInfoActivity1.class
			.getSimpleName();
	/** 上一步按钮 */
	private Button mLastBtn = null;
	/** 下一步按钮 */
	private Button mNextBtn = null;

	private String randomNumber;

	// private LinearLayout commPayeeLl = null;
	// private String bocFlag = null;
	/** 用户输入的动态口令 */
	/** 转出账户 */
	private TextView accOutTv = null;
	private TextView accOutNicknameTv;
	/** 转入账户 */
	private TextView accInTv = null;
	private TextView accInNicknameTv;
	/** 收款人 */
	private TextView payeeTv = null;
	/** 币种 */
	private TextView currencyTv = null;
	/** 转账金额 */
	private TextView transAmountTv = null;
	/** 转账类型 */
	// private TextView transTypeTv = null;
	/** 附言 */
	private TextView remarkTv = null;
	// 是否保存为常用收款人
	private CheckBox payeeCheckBox;
	// private boolean isChecked;
	private Button smsbtn;
	/** 手机交易码 */
	private LinearLayout smcLinearLayout;
	private SipBox sipBoxSmc = null;
	/** 动态口令卡 加密控件 */
	private LinearLayout optLinearLayout;
	private SipBox sipBoxOpt = null;
	/** 中银E盾 */
	private UsbKeyText usbKeyText;

	// 页面上显示数据
	private String accOutNo;
	private String accOutNickname;
	private String accInNickname;

	// private String combinId;
	// 接口发送数据
	// 转出账户ID
	private String fromAccountId = "";
	// 收款人ID
	private String payeeId = "";
	// 转入账户账号
	private String payeeActno = "";
	// 收款人姓名
	private String payeeName = "";
	// 转入账户类型
	private String toAccountType = "";
	// 收款人手机号
	private String payeeMobile = "";
	// 转入账户地区
	private String payeeBankNum = "";
	// 汇款用途
	private String remittanceInfo = "";
	// 转账金额
	private String amount = "";
	// 备注
	private String remark = "";
	// 币种
	private String currency = "";
	// 付款日期
	private String dueDate = "";
	// 周期
	private String cycleSelect = "";
	// 执行日期
	private String executeDate = "";
	// 起始日期
	private String startDate = "";
	// 结束日期
	private String endDate = "";
	// 手机交易码
	private String smc = "";
	// 动态口令
	private String otp = "";
	private String smc_rc = "";
	private String otp_rc = "";
	// 执行类型
	private String executeType = ConstantGloble.NOWEXE;
	private String tokenId;

	/** 手机交易码有效时间 */
	// private String smcTrigerInterval;
	// private int smcTime;

	// private List<Map<String, Object>> factorList;
	/** 是否发送手机短信通知收款人标示 */
	private String isSendSmc;
	/** 手机号 */
	private LinearLayout mobileLayout;
	private TextView mobileTv;

	/** 应收费用 */
	private TextView actChargeTv;
	/** 拟收费用 */
	private TextView farkChargeTv;
	private String actCharge;
	private String fakeCharge;

	private String _plainData;
	/** 安全因子数据返回 */
	private Map<String, Object> noRelCrcdRepayMap = null;
	/** 动态口令 */
	private Boolean isOtp;
	/** 手机验证码 */
	private Boolean isSmc;

	/**基准费用*/
	private LinearLayout ll_need; 
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.tran_my_trans);
		View view = mInflater
				.inflate(R.layout.tran_confirm_info_activity, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		/** 预交易数据返回 */
		noRelCrcdRepayMap = TranDataCenter.getInstance()
				.getNoRelCrcdRepayCallBackMap();
		// 请求随机数
		requestForRandomNumber();
		// setupView();
		// initData();
		// displayTextView();

	}

	/**
	 * 显示数据
	 */
	private void initData() {
		tranTypeFlag = this.getIntent().getIntExtra(TRANS_TYPE, -1);
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		fromAccountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
		accOutNo = (String) accOutInfoMap.get(Comm.ACCOUNTNUMBER);
		accOutNickname = (String) accOutInfoMap.get(Comm.NICKNAME);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		if (tranTypeFlag == TRANTYPE_NOREL_CRCD) {
			// 行内转账
			payeeId = (String) accInInfoMap.get(Tran.TRANS_PAYEETID_RES);
		} else if (tranTypeFlag == TRANTYPE_DIR_CRCD) {
			// 行内定向
			payeeId = (String) accInInfoMap
					.get(Tran.TRAN_DIR_QUERYLIST_PAYEEID_RES);
		}
		payeeActno = (String) accInInfoMap.get(Comm.ACCOUNTNUMBER);
		accInNickname = (String) accInInfoMap.get(Tran.TRANS_PAYEEALIAS_RES);
		payeeName = (String) accInInfoMap.get(Comm.ACCOUNT_NAME);
		// toAccountType = (String) accInInfoMap.get(Tran.TRANS_TYPE_RES);
		toAccountType = (String) TranDataCenter.getInstance()
				.getNoRelCrcdRepayCallBackMap()
				.get(Tran.TRANS_TOACCOUNTTYPE_REQ);

		// payeeBankNum = (String)
		// accInInfoMap.get(Tran.TRANS_ACCOUNTIBKNUM_RES);
		payeeBankNum = (String) TranDataCenter.getInstance()
				.getNoRelCrcdRepayCallBackMap().get(Tran.TRANS_PAYEE_BANK_NUM);
		currency = ConstantGloble.PRMS_CODE_RMB;

		Map<String, Object> chargeMissionMap = TranDataCenter.getInstance()
				.getCommissionChargeMap();
		if (!StringUtil.isNullOrEmpty(chargeMissionMap)) {
			TextView tv_toast = (TextView) findViewById(R.id.tv_toast);
			String remitSetMealFlag = (String) chargeMissionMap
					.get(Tran.REMITSETMEAL_FLAG);
			if (!StringUtil.isNull(remitSetMealFlag)) {
				if (remitSetMealFlag
						.equals(ConstantGloble.ACC_CURRENTINDEX_VALUE)) {
					tv_toast.setText(getString(R.string.tran_remitSetMealFlag));
				}
			}
			String getChargeFlag = (String) chargeMissionMap
					.get(Tran.GETCHARGE_FLAG);
			if(ConstantGloble.ACC_CURRENTINDEX_VALUE.equals(getChargeFlag)
					&&!chargeMissionMap.containsKey(Tran.NEED_COMMISSION_CHARGE)){
				
			}else{
				ll_need.setVisibility(View.VISIBLE);	
			}
			// 应收费用
			actCharge = (String) chargeMissionMap
					.get(Tran.NEED_COMMISSION_CHARGE);
			if (!StringUtil.isNullOrEmpty(actCharge)) {
				actCharge = StringUtil.parseStringPattern(actCharge, 2);
				// 转出是借记卡 转入是信用卡 基准费用为0 不显示基准费用
				String fromAccountType=(String) accOutInfoMap.get(Tran.RELTRANS_FROMACCOUNTTYPE_RES);
				if(StringUtil.isNullOrEmpty(fromAccountType)){
					fromAccountType=(String) accOutInfoMap.get(Acc.ACC_ACCOUNTTYPE_RES);
				}
				String toAccountType=(String) accInInfoMap.get(Tran.RELTRANS_TOACCOUNTTYPE_RES);
				if(StringUtil.isNullOrEmpty(toAccountType)){
					toAccountType=(String) accInInfoMap.get(Acc.ACC_ACCOUNTTYPE_RES);
				}
				if(ConstantGloble.ACC_TYPE_BRO.equals(fromAccountType)
//					&&LocalData.mycardList.contains(toAccountType) 转入一定是信用卡
					&&Double.parseDouble(actCharge)==0){
					ll_need.setVisibility(View.GONE);	
				}
			}
			// 拟收费用
			fakeCharge = (String) chargeMissionMap
					.get(Tran.PRECOMMISSION_CHARGE);
			if (!StringUtil.isNullOrEmpty(fakeCharge)) {
				fakeCharge = StringUtil.parseStringPattern(fakeCharge, 2);
				
			}
		}

		Map<String, String> userInputMap = TranDataCenter.getInstance()
				.getUserInputMap();
		// 如果是有短信通知收款人 则手机号码为用户输入手机号
		isSendSmc = userInputMap.get(ConstantGloble.IS_SEND_SMC);
		if (!StringUtil.isNullOrEmpty(isSendSmc)
				&& isSendSmc.equals(ConstantGloble.TRUE)) {
			payeeMobile = userInputMap.get(Tran.INPUT_PAYEE_TRANSFER_MOBILE);
			mobileLayout.setVisibility(View.VISIBLE);
			mobileTv.setText(payeeMobile);
		} else {
			payeeMobile = "";
		}
		amount = userInputMap.get(Tran.INPUT_TRANSFER_AMOUNT);
		remark = userInputMap.get(Tran.INPUT_TRANSFER_REMARK);
		// combinId = userInputMap.get(Tran._COMBINID_REQ);

		// noRelCrcdRepayMap = TranDataCenter.getInstance()
		// .getNoRelCrcdRepayCallBackMap();
		// smcTrigerInterval = (String) noRelCrcdRepayMap
		// .get(Tran.CREDIT_CARD_SMCTRIGERINTERVAL_RES);
		_plainData = (String) noRelCrcdRepayMap
				.get(Tran.CREDIT_CARD__PLAINDATA_RES);
		// @SuppressWarnings("unchecked")
		// List<Map<String, Object>> factorList = (List<Map<String, Object>>)
		// noRelCrcdRepayMap
		// .get(Login.FACTORLIST);
		// // 手机交易码 和 动态口令的显示 根据返回数据里面 factorlist
		// if (StringUtil.isNullOrEmpty(factorList)) {
		// return;
		// }
		// if (factorList.size() == 1) {
		// @SuppressWarnings("unchecked")
		// Map<String, String> field = (Map<String, String>) factorList.get(0)
		// .get(Login.FIELD);
		// String name = field.get(Login.NAME);
		// if (name.equals(Login.OTP)) {// 动态口令
		// optLinearLayout.setVisibility(View.VISIBLE);
		// } else if (name.equals(Login.SMC)) {// 手机验证码
		// smcLinearLayout.setVisibility(View.VISIBLE);
		// }
		// } else {
		// optLinearLayout.setVisibility(View.VISIBLE);
		// smcLinearLayout.setVisibility(View.VISIBLE);
		// }
	}

	/**
	 * 初始化加载XML里的控件
	 */
	private void setupView() {
		ll_need= (LinearLayout) findViewById(R.id.ll_need);
		accOutTv = (TextView) findViewById(R.id.tv_acc_out_confirm);
		accOutNicknameTv = (TextView) findViewById(R.id.tran_out_nickname_tv);
		accInTv = (TextView) findViewById(R.id.tv_acc_in_confirm);
		accInNicknameTv = (TextView) findViewById(R.id.tran_in_nickname_tv);
		payeeTv = (TextView) findViewById(R.id.tv_payee_confirm);
		currencyTv = (TextView) findViewById(R.id.tv_tran_currency_confirm);
		transAmountTv = (TextView) findViewById(R.id.tv_transferAmount_confirm);
		// transTypeTv = (TextView) findViewById(R.id.tv_amount_type_confirm);
		remarkTv = (TextView) findViewById(R.id.tv_remark_info_confirm);
		remarkTv.setText(StringUtil.isNull(remark) ? "-" : remark);

		mobileLayout = (LinearLayout) findViewById(R.id.mobile_layout);
		mobileTv = (TextView) findViewById(R.id.mobile_number_tv);

		actChargeTv = (TextView) findViewById(R.id.tran_act_charge_tv);
		farkChargeTv = (TextView) findViewById(R.id.tran_fack_charge_tv);

		/** 跟据返回初始化中银E盾加密控件 */
		initSipBox();

		if (isSmc) {
			smcLinearLayout = (LinearLayout) findViewById(R.id.tran_smc_layout);
			smcLinearLayout.setVisibility(View.VISIBLE);
			sipBoxSmc = (SipBox) findViewById(R.id.et_smc_confirm_info);
			sipBoxSmc.setCipherType(SystemConfig.CIPHERTYPE);
			sipBoxSmc.setPasswordRegularExpression(CheckRegExp.OTP);
			sipBoxSmc.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
			sipBoxSmc.setPasswordMaxLength(6);
			sipBoxSmc.setPasswordMinLength(6);
			sipBoxSmc.setId(10003);
			sipBoxSmc.setSipDelegator(this);
			sipBoxSmc.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
			sipBoxSmc.setRandomKey_S(randomNumber);

			smsbtn = (Button) findViewById(R.id.smsbtn);
			SmsCodeUtils.getInstance().addSmsCodeListner(smsbtn,
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							// 发送获取短信验证码的请求
							requestForSendSMSCodeToMobile();// PsnSendSMSCodeToMobile获取并发送手机交易码
						}
					});
		}

		if (isOtp) {
			optLinearLayout = (LinearLayout) findViewById(R.id.ll_opt);
			optLinearLayout.setVisibility(View.VISIBLE);
			sipBoxOpt = (SipBox) findViewById(R.id.et_opt_confirm_info);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(sipBoxOpt, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//			sipBoxOpt.setCipherType(SystemConfig.CIPHERTYPE);
//			sipBoxOpt.setPasswordRegularExpression(CheckRegExp.OTP);// \\S*
//			sipBoxOpt.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			sipBoxOpt.setPasswordMaxLength(6);
//			sipBoxOpt.setPasswordMinLength(6);
//			sipBoxOpt.setId(10002);
//			sipBoxOpt.setSipDelegator(this);
//			sipBoxOpt.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			sipBoxOpt.setRandomKey_S(randomNumber);
		}

		mLastBtn = (Button) findViewById(R.id.btn_last_trans_confirm);
		mNextBtn = (Button) findViewById(R.id.btn_next_trans_confirm);

		// commPayeeLl = (LinearLayout) findViewById(R.id.ll_comm_payee);

		StepTitleUtils.getInstance().initTitldStep(this,
				this.getResources().getStringArray(R.array.tran_my_trans));
		StepTitleUtils.getInstance().setTitleStep(2);

		payeeCheckBox = (CheckBox) findViewById(R.id.tv_save_payee_trans_success);
		payeeCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// LogGloble.i(TAG,
				// "payeeCheckBox.isChecked()="+payeeCheckBox.isChecked());
				LogGloble.i(TAG, "isChecked=" + isChecked);
			}
		});
		mLastBtn.setOnClickListener(this);
		mNextBtn.setOnClickListener(this);

	}

	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox() {
		usbKeyText = (UsbKeyText) findViewById(R.id.sip_usbkey);
		String mmconversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeyText
				.Init(mmconversationId, randomNumber, noRelCrcdRepayMap, this);
		isOtp = usbKeyText.getIsOtp();
		isSmc = usbKeyText.getIsSmc();
	}

	private void displayTextView() {
		accOutTv.setText(StringUtil.getForSixForString(accOutNo));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accOutTv);
		accOutNicknameTv.setText(accOutNickname);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accOutNicknameTv);
		accInTv.setText(StringUtil.getForSixForString(payeeActno));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accInTv);
		accInNicknameTv.setText(accInNickname);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accInNicknameTv);
		payeeTv.setText(payeeName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payeeTv);
		currencyTv
				.setText(LocalData.Currency.get(ConstantGloble.PRMS_CODE_RMB));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				currencyTv);
		transAmountTv.setText(StringUtil.parseStringPattern(amount, 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				transAmountTv);
		remarkTv.setText(StringUtil.isNull(remark) ? "-" : remark);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, remarkTv);

		// actChargeTv.setText(StringUtil.isNull(actCharge) ? "-"
		// : actCharge);
		// farkChargeTv.setText(StringUtil.isNull(fakeCharge) ? "-"
		// : fakeCharge);
		actChargeTv.setText(actCharge);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				actChargeTv);
		farkChargeTv.setText(fakeCharge);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				farkChargeTv);
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_last_trans_confirm:// 用户点击上一步
			finish();
			break;
		case R.id.btn_next_trans_confirm:// 用户点击下一步
			// if (smcLinearLayout.isShown() && !optLinearLayout.isShown()) {
			// //
			// try {
			// String str = sipBoxSmc.getText().toString().trim();
			// if (StringUtil.isNullOrEmpty(str)) {
			// BaseDroidApp.getInstanse().showInfoMessageDialog(
			// this.getString(R.string.reg_smc_str));
			// return;
			// }
			// //
			// smc_rc = sipBoxSmc.getValue().getEncryptRandomNum();
			// smc = sipBoxSmc.getValue().getEncryptPassword();
			// } catch (CodeException e) {
			// LogGloble.exceptionPrint(e);
			// BaseDroidApp.getInstanse().showInfoMessageDialog(
			// this.getString(R.string.tran_acc_opt));
			// return;
			// }
			// }
			// if (optLinearLayout.isShown() && !smcLinearLayout.isShown()) {
			// try {
			// String str = sipBoxOpt.getText().toString().trim();
			// if (StringUtil.isNullOrEmpty(str)) {
			// BaseDroidApp.getInstanse().showInfoMessageDialog(
			// this.getString(R.string.forex_inves_notify));
			// return;
			// }
			// otp_rc = sipBoxOpt.getValue().getEncryptRandomNum();
			// otp = sipBoxOpt.getValue().getEncryptPassword();
			// } catch (CodeException e) {
			// LogGloble.exceptionPrint(e);
			// BaseDroidApp.getInstanse().showInfoMessageDialog(
			// this.getString(R.string.tran_acc_smc));
			// return;
			// }
			// }
			//
			// if (optLinearLayout.isShown() && smcLinearLayout.isShown()) {
			//
			// try {
			// String str = sipBoxSmc.getText().toString().trim();
			// if (StringUtil.isNullOrEmpty(str)) {
			// BaseDroidApp.getInstanse().showInfoMessageDialog(
			// this.getString(R.string.reg_smc_str));
			// return;
			// }
			// smc_rc = sipBoxSmc.getValue().getEncryptRandomNum();
			// smc = sipBoxSmc.getValue().getEncryptPassword();
			// } catch (CodeException e) {
			// LogGloble.exceptionPrint(e);
			// BaseDroidApp.getInstanse().showInfoMessageDialog(
			// this.getString(R.string.tran_acc_smc));
			// return;
			// }
			// try {
			// String str = sipBoxOpt.getText().toString().trim();
			// if (StringUtil.isNullOrEmpty(str)) {
			// BaseDroidApp.getInstanse().showInfoMessageDialog(
			// this.getString(R.string.forex_inves_notify));
			// return;
			// }
			// //
			// otp_rc = sipBoxOpt.getValue().getEncryptRandomNum();
			// otp = sipBoxOpt.getValue().getEncryptPassword();
			// } catch (CodeException e) {
			// LogGloble.exceptionPrint(e);
			// BaseDroidApp.getInstanse().showInfoMessageDialog(
			// this.getString(R.string.tran_acc_opt));
			// return;
			// }
			// }
			/** 安全工具数据校验 */
			usbKeyText.checkDataUsbKey(sipBoxOpt, sipBoxSmc,
					new IUsbKeyTextSuccess() {

						@Override
						public void SuccessCallBack(String result, int errorCode) {
							// TODO Auto-generated method stub
							// 非关联信用卡还款
							BiiHttpEngine.showProgressDialog();
							requestPSNGetTokenId((String) BaseDroidApp
									.getInstanse().getBizDataMap()
									.get(ConstantGloble.CONVERSATION_ID));
						}
					});
			// 非关联信用卡还款
			// BiiHttpEngine.showProgressDialog();
			// requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
			// .getBizDataMap().get(ConstantGloble.CONVERSATION_ID));

			break;
		}
	}

	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber() {
		BiiHttpEngine.showProgressDialog();
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
		LogGloble.i(TAG, "randomNumber=" + randomNumber);
		// 加密控件设置随机数
		// sipBoxOpt.setRandomKey_S(randomNumber);
		// sipBoxSmc.setRandomKey_S(randomNumber);
		setupView();
		initData();
		displayTextView();
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		tokenId = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		requestForTransBocTransferSubmit();
	}

	/**
	 * 非关联信用卡还款
	 */
	private void requestForTransBocTransferSubmit() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		if (tranTypeFlag == TRANTYPE_NOREL_CRCD) {
			biiRequestBody.setMethod(Tran.TRANSBOCTRANSFERSUBMIT);
		} else if (tranTypeFlag == TRANTYPE_DIR_CRCD) {
			biiRequestBody.setMethod(Tran.TRANSDIRBOCTRANSFERSUBMIT);
		}
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		SipBoxUtils.setSipBoxParams(map);
		// TODO
		// map.put(Tran.TRANS_SIGNEDDATA_REQ, _plainData);
		// 执行方式
		map.put(Tran.TRANS_EXECUTETYPE_REQ, executeType);
		// 转出账户ID
		map.put(Tran.TRANS_FROMACCOUNTID_REQ, fromAccountId);
		// 收款人ID
		map.put(Tran.TRANS_PAYEEID_REQ, payeeId);
		// 转入账户账号
		map.put(Tran.TRANS_PAYEEACTNO_REQ, payeeActno);
		// 收款人姓名
		map.put(Tran.TRANS_PAYEENAME_REQ, payeeName);
		// 转入账户类型
		map.put(Tran.TRANS_TOACCOUNTTYPE_REQ, toAccountType);
		// 收款人手机号
		map.put(Tran.TRANS_PAYEEMOBILE_REQ, payeeMobile);
		// 转入账户地区
		map.put(Tran.TRANS_PAYEEBANKNUM_REQ, payeeBankNum);
		// 汇款用途
		map.put(Tran.TRANS_REMITTANCEINFO_REQ, remittanceInfo);
		// 转账金额
		map.put(Tran.TRANS_AMOUNT_REQ, amount);
		// 备注
		map.put(Tran.TRANS_REMARK_REQ, remark);
		// 币种
		map.put(Tran.TRANS_CURRENCY_REQ, currency);
		// 付款日期
		map.put(Tran.TRANS_DUE_DATE, dueDate);
		// 周期
		map.put(Tran.TRANS_CYCLESELECT_REQ, cycleSelect);
		// 执行日期
		map.put(Tran.TRANS_EXECUTEDATE_REQ, executeDate);
		// 起始日期
		map.put(Tran.TRANS_STARTDATE_REQ, startDate);
		// 结束日期
		map.put(Tran.TRANS_ENDDATE_REQ, endDate);
		// 手机交易码
		// map.put(Tran.TRANS_SMC_REQ, smc);
		// // 动态口令
		// map.put(Tran.TRANS_OTP_REQ, otp);
		// // 加密控件，产生随机数，需要添加
		// map.put(Tran.TRANS_SMC_RC_REQ, smc_rc);
		// map.put(Tran.TRANS_OPT_RC_REQ, otp_rc);
		/** 安全工具参数获取 */
		usbKeyText.InitUsbKeyResult(map);
		// 防重机制，通过PSNGetTokenId接口获取
		map.put(Tran.TRANS_TOKEN_REQ, tokenId);
		// 设备指纹
		GetPhoneInfo.addPhoneInfo(map);// 反欺诈 设备指纹
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForTransBocTransferSubmitCallBack");
	}

	/**
	 * 非关联信用卡还款返回
	 */
	public void requestForTransBocTransferSubmitCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setNoRelCrcdRepaySubmitCallBackMap(result);

		Intent intent = new Intent();
		intent.putExtra(Tran.ACC_ACCOUNTID_REQ, fromAccountId);
		intent.setClass(this, TranSuccessActivity1.class);
		startActivity(intent);
	}

	/**
	 * PsnSendSMSCodeToMobile获取并发送手机交易码 req
	 */
	public void requestForSendSMSCodeToMobile() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.SENDSMSCODETOMOBILE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this,
				"requestForSendSMSCodeToMobileCallBack");
	}

	/**
	 * PsnSendSMSCodeToMobile获取并发送手机交易码 res
	 */
	public void requestForSendSMSCodeToMobileCallBack(Object resultObj) {

	}

}
