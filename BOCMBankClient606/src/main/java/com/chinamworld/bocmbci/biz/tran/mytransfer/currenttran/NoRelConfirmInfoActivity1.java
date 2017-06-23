package com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran;

import java.util.HashMap;
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
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.twodimentrans.TwoDimenTransActivity1;
import com.chinamworld.bocmbci.constant.CheckRegExp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.GetPhoneInfo;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 
 * @author WJP
 * 行内转账
 */
@SuppressWarnings("ResourceType")
public class NoRelConfirmInfoActivity1 extends TranBaseActivity implements
		OnClickListener {

	private static final String TAG = NoRelConfirmInfoActivity1.class
			.getSimpleName();
	/** 上一步按钮 */
	private Button mLastBtn = null;
	/** 下一步按钮 */
	private Button mNextBtn = null;
	/** 转出账户 */
	private TextView accOutTv = null;
	/** 转出账户地区 */
	private TextView accountIbkNumTv = null;
	/** 转入账户 */
	private TextView accInTv = null;
	/** 币种 */
	// private TextView currencyTv = null;
	/** 钞汇标志 */
	// private TextView cashRemitTv = null;
	/** 转账金额 */
	private TextView transAmountTv = null;
	/** 转账类型 */
	// private TextView transTypeTv = null;
	/** 执行方式 */
	private TextView exeTypeTv = null;
	/** 附言 */
	private TextView remarkTv = null;
	/** 执行方式 */
	private String relExecuteType = null;

	private LinearLayout preDateLL, prePeriodLl;

	// private LinearLayout cashRemitLinearLayout;

	/** 应收费用 */
	private TextView actChargeTv;
	/** 拟收费用 */
	private TextView farkChargeTv;

	private TextView actChargeDispalyTv;
	private TextView farkChargeDisplayTv;

	/** 执行日期 */
	private TextView exeDateTv = null;
	/** 开始日期 */
	private TextView startDateTv = null;
	/** 结束日期 */
	private TextView endDateTv = null;
	/** 执行周期 */
	private TextView cycleDateTv = null;
	/** 执行次数 */
	private TextView executeTimesTv = null;
	/** 转入账户地区 */
	private TextView accInAreaTv = null;
	/** 转出账户开户行 */
	private TextView outBranchName = null;
	/** 转入账户开户行 */
	private TextView inBranchName = null;

	/** 手机交易码 */
	private LinearLayout smcLinearLayout;
	private SipBox sipBoxSmc = null;
	/** 动态口令卡 加密控件 */
	private LinearLayout optLinearLayout;
	private SipBox sipBoxOpt = null;
	private Button smsbtn;
	/** 中银E盾 */
	private UsbKeyText usbKeyText;
	// private String combinId;
	// 手机交易码
	private String smc = "";
	// 动态口令
	private String otp = "";
	private String smc_rc = "";
	private String otp_rc = "";
	/** 预约日期执行 执行日期 */
	private String preDateExecuteDate = "";

	/** 起始日期 */
	private String strStartDate = "";
	/** 结束日期 */
	private String strEndDate = "";
	/** 执行次数 */
	private String strExecuteTimes = "";
	/** 执行周期 */
	private String strWeek = "";
	/** 转出账户昵称 */
	private TextView accoutNicknameTv;
	/** 收款人姓名 */
	private TextView accinNickNameTv;

	private String accOutNo;
	private String outNickName;
	private String fromAccountArea;
	private String fromAccountBankName;
	private String toAccountBankName;
	private String actCharge;
	private String fakeCharge;

	// /**转账类型*/
	// private TextView tranTypeTv;
	// 转出账户
	private String fromAccountId;
	private String toAccountId;
	private String payeeActno;
	/** 收款人姓名 */
	private String payeeName;
	/** 收款人手机号 */
	private String mobileNumber;

	private String currencySer;
	// private String cashRemitSer;
	private String relTransRemark;
	private String relTransAmount;
	private String tokenId;
	// 转账日期 //立即执行：该字段为空，BII自动取为当前系统日期
	// 预约日期执行：为预约日期
	// 预约周期执行：为起始日期
	/** 手机交易码有效时间 */
	// private String smcTrigerInterval;
	// private int smcTime;

	private String randomNumber;

	// private List<Map<String, Object>> factorList;

	/** 预交易返回数据 */
	private String toAccountType;
	private String payeeBankNum;

	/** 是否发送手机短信通知收款人标示 */
	private String isSendSmc;
	/** 手机号 */
	private LinearLayout mobileLayout;
	private TextView mobileTv;
	/** CA认证生成的密文 */
	private String _signedData = "";

	private int Flag = 0;
	private static final int SUBMIT = 1;// 提交
	private static final int ADD = 2;// 新增收款人
	/** 预交易返回数据 */
	private Map<String, Object> norelBankInMap = null;
	/** 动态口令 */
	private Boolean isOtp;
	/** 手机短信 */
	private Boolean isSmc;
	/**基准费用*/
	private LinearLayout ll_need; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 二级菜单标识
		secondMenuFlag = getIntent().getIntExtra(SENCOND_MENU_KEY, -1);
		if (secondMenuFlag == TWO_DIMEN_TRAN) {// 从二维码转账那边跳转过来
			setTitle(R.string.two_dimen_scan);
			mTopRightBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ActivityTaskManager.getInstance().removeAllActivity();
					Intent intent = new Intent();
					intent.setClass(NoRelConfirmInfoActivity1.this,
							TwoDimenTransActivity1.class);
					startActivity(intent);
					finish();
				}
			});
		} else {
			setTitle(this.getString(R.string.tran_my_trans));
		}
		View view = mInflater.inflate(
				R.layout.tran_confirm_info_no_rel_activity, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		// 预交易返回数据
		norelBankInMap = TranDataCenter.getInstance()
				.getNoRelBankInCallBackMap();
		// 请求随机数
		requestForRandomNumber();
		// setupView();
		// initData();
		// displayTextView();
		// // 请求随机数
		// requestForRandomNumber();
	}

	/**
	 * 显示数据
	 */
	private void initData() {
		tranTypeFlag = this.getIntent().getIntExtra(TRANS_TYPE, -1);
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		fromAccountId = (String) accOutInfoMap.get(Tran.ACC_ACCOUNTID_REQ);
		// 转出账户
		accOutNo = (String) accOutInfoMap.get(Tran.ACCOUNTNUMBER_RES);
		// 转出账户昵称
		outNickName = (String) accOutInfoMap.get(Tran.NICKNAME_RES);
		// 转账账户地区
		fromAccountArea = (String) accOutInfoMap.get(Tran.ACCOUNTIBKNUM_RES);
		// 转出账户开户行
		fromAccountBankName = (String) accOutInfoMap.get(Tran.BRANCHNAME_RES);

		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		if (tranTypeFlag == TRANTYPE_NOREL_BANKIN) {
			// 行内转账
			toAccountId = (String) accInInfoMap.get(Tran.TRANS_PAYEETID_RES);
		} else if (tranTypeFlag == TRANTYPE_DIR_BANKIN) {
			// 行内定向
			toAccountId = (String) accInInfoMap
					.get(Tran.TRAN_DIR_QUERYLIST_PAYEEID_RES);
		}
		// 转入账户
		payeeActno = (String) accInInfoMap.get(Tran.ACCOUNTNUMBER_RES);
		if(StringUtil.isNullOrEmpty(payeeActno)){
			payeeActno = (String) accInInfoMap.get(Crcd.CRCD_ACCTNUM);	
		}
		// 账户名称
		payeeName = (String) accInInfoMap.get(Tran.ACCOUNTNAME_RES);
		if(StringUtil.isNullOrEmpty(payeeName)){
			payeeName = (String) accInInfoMap.get(Crcd.CRCD_ACCTNAME_RES);	
		}
		// 转入账户昵称
		// String inNickName = (String) accInInfoMap.get(Tran.NICKNAME_RES);
		// 转入账户地区
		// payeeBankNum = (String) accInInfoMap.get(Tran.ACCOUNTIBKNUM_RES);
		// 转瑞账户开户行 TODO 字段带确认
		toAccountBankName = (String) accInInfoMap.get(Tran.TRANS_BANKNAME_RES);
		if(StringUtil.isNullOrEmpty(toAccountBankName)){
			toAccountBankName = (String) accInInfoMap.get(Crcd.CRCD_ACCTBANK);	
		}
		// // 收款人手机号
		// mobileNumber = (String) accInInfoMap.get(Tran.TRANS_MOBILE_RES);

		currencySer = ConstantGloble.PRMS_CODE_RMB;

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
					&&LocalData.mycardList.contains(toAccountType)
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
			mobileNumber = userInputMap.get(Tran.INPUT_PAYEE_TRANSFER_MOBILE);
			mobileLayout.setVisibility(View.VISIBLE);
			mobileTv.setText(mobileNumber);
		} else {
			mobileNumber = "";
		}

		if (!StringUtil.isNullOrEmpty(userInputMap)) {
			// combinId = userInputMap.get(Tran._COMBINID_REQ);
			relTransRemark = userInputMap.get(Tran.INPUT_TRANSFER_REMARK);
			relTransAmount = userInputMap.get(Tran.INPUT_TRANSFER_AMOUNT);
			// 执行方式
			relExecuteType = userInputMap.get(Tran.MANAGE_PRE_transMode_RES);
		}

		// // 预交易返回数据
		// norelBankInMap = TranDataCenter.getInstance()
		// .getNoRelBankInCallBackMap();
		toAccountType = (String) norelBankInMap.get(Tran.TRANS_TO_ACCOUNT_TYPE);
		payeeBankNum = (String) norelBankInMap.get(Tran.TRANS_PAYEE_BANK_NUM);
		_signedData = (String) norelBankInMap.get(Tran.TRANS_SIGNEDDATA_REQ);
		switch (Integer.parseInt(relExecuteType)) {
		case 0:
			break;
		// 预约日期执行
		case 1:
			preDateLL.setVisibility(View.VISIBLE);
			prePeriodLl.setVisibility(View.GONE);

			// 预约日期执行 执行日期
			preDateExecuteDate = (String) userInputMap.get(Tran.INPUT_PRE_DATE);
			exeDateTv.setText(preDateExecuteDate);
			break;
		case 2:
			prePeriodLl.setVisibility(View.VISIBLE);
			preDateLL.setVisibility(View.GONE);

			// 单笔应收 单笔拟收
			actChargeDispalyTv.setText(this.getResources().getString(
					R.string.trans_single_act_charge));
			farkChargeDisplayTv.setText(this.getResources().getString(
					R.string.trans_single_fack_charge));

			// 起始日期
			strStartDate = (String) userInputMap
					.get(Tran.INPUT_PRE_PERIOD_START_DATE);
			// 结束日期
			strEndDate = (String) userInputMap
					.get(Tran.INPUT_PRE_PERIOD_END_DATE);
			// 执行次数
			strExecuteTimes = (String) userInputMap
					.get(Tran.INPUT_PRE_PERIOD_EXECUTE_TIMES);
			// 执行周期
			strWeek = (String) userInputMap.get(Tran.INPUT_PRE_PERIOD_WEEK);
			startDateTv.setText(strStartDate);
			endDateTv.setText(strEndDate);
			cycleDateTv.setText(LocalData.Frequency.get(strWeek));
			executeTimesTv.setText(strExecuteTimes);
			break;
		}

		// Map<String, Object> noRelBankInMap = TranDataCenter.getInstance()
		// .getNoRelBankInCallBackMap();
		// smcTrigerInterval = (String) noRelBankInMap
		// .get(Tran.CREDIT_CARD_SMCTRIGERINTERVAL_RES);
		// @SuppressWarnings("unchecked")
		// List<Map<String, Object>> factorList = (List<Map<String, Object>>)
		// noRelBankInMap
		// .get(Login.FACTORLIST);
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
		StepTitleUtils.getInstance().initTitldStep(this,
				this.getResources().getStringArray(R.array.tran_my_trans));
		StepTitleUtils.getInstance().setTitleStep(2);
		mLastBtn = (Button) findViewById(R.id.btn_last_trans_rel_confirm);
		mNextBtn = (Button) findViewById(R.id.btn_next_trans_rel_confirm);

		accOutTv = (TextView) findViewById(R.id.tv_acc_out_rel_confirm);
		accoutNicknameTv = (TextView) findViewById(R.id.tran_out_nickname_tv);
		accountIbkNumTv = (TextView) findViewById(R.id.tv_acc_out_area_rel_confirm);
		accInTv = (TextView) findViewById(R.id.tv_acc_in_rel_confirm);
		accinNickNameTv = (TextView) findViewById(R.id.tran_in_nickname_tv);
		accInAreaTv = (TextView) findViewById(R.id.tv_acc_in_area_rel_confirm);
		inBranchName = (TextView) findViewById(R.id.tv_branch_name_in_rel_confirm);
		// currencyTv = (TextView)
		// findViewById(R.id.tv_tran_currency_rel_confirm);
		transAmountTv = (TextView) findViewById(R.id.tv_transferAmount_rel_confirm);
		actChargeTv = (TextView) findViewById(R.id.tran_act_charge_tv);
		farkChargeTv = (TextView) findViewById(R.id.tran_fack_charge_tv);

		actChargeDispalyTv = (TextView) findViewById(R.id.tran_act_charge_display_tv);
		farkChargeDisplayTv = (TextView) findViewById(R.id.tran_fack_charge_display_tv);

		remarkTv = (TextView) findViewById(R.id.tv_remark_info_rel_confirm);
		exeTypeTv = (TextView) findViewById(R.id.tv_executeType_rel_confirm);

		preDateLL = (LinearLayout) findViewById(R.id.ll_preDate_confirm_info);
		prePeriodLl = (LinearLayout) findViewById(R.id.ll_prePeriod_confirm_info);
		exeDateTv = (TextView) findViewById(R.id.tv_exeDate_info_rel_confirm);
		startDateTv = (TextView) findViewById(R.id.tv_startDate_info_rel_confirm);
		endDateTv = (TextView) findViewById(R.id.tv_endDate_info_rel_confirm);
		cycleDateTv = (TextView) findViewById(R.id.tv_cycleDate_info_rel_confirm);
		executeTimesTv = (TextView) findViewById(R.id.tv_execute_times_info_rel_confirm);

		mobileLayout = (LinearLayout) findViewById(R.id.mobile_layout);
		mobileTv = (TextView) findViewById(R.id.mobile_number_tv);

		outBranchName = (TextView) findViewById(R.id.tv_branch_name_rel_confirm);
		// tranTypeTv = (TextView) findViewById(R.id.tran_acc_type_tv);
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
							sendSMSCToMobile();// PsnSendSMSCodeToMobile获取并发送手机交易码
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

		mLastBtn.setOnClickListener(this);
		mNextBtn.setOnClickListener(this);

		// SmsCodeUtils.getInstance().addSmsCodeListner(smsbtn,
		// new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // 发送获取短信验证码的请求
		// sendSMSCToMobile();// PsnSendSMSCodeToMobile获取并发送手机交易码
		// }
		// });
	}

	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox() {
		usbKeyText = (UsbKeyText) findViewById(R.id.sip_usbkey);
		String mmconversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeyText.Init(mmconversationId, randomNumber, norelBankInMap, this);
		isOtp = usbKeyText.getIsOtp();
		isSmc = usbKeyText.getIsSmc();
	}

	private void displayTextView() {
		accOutTv.setText(StringUtil.getForSixForString(accOutNo));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accOutTv);
		accoutNicknameTv.setText(outNickName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accoutNicknameTv);
		accountIbkNumTv.setText(LocalData.Province.get(fromAccountArea));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accountIbkNumTv);
		accInTv.setText(StringUtil.getForSixForString(payeeActno));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accInTv);
		accinNickNameTv.setText(payeeName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accinNickNameTv);
		accInAreaTv.setText(LocalData.Province.get(payeeBankNum));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accInAreaTv);
		inBranchName.setText(StringUtil.isNullChange(toAccountBankName));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				inBranchName);
		// currencyTv.setText(LocalData.Currency.get(currencySer));
		transAmountTv.setText(StringUtil.parseStringPattern(relTransAmount, 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				transAmountTv);
		exeTypeTv.setText(LocalData.TransModeDisplay.get(relExecuteType));
		remarkTv.setText(StringUtil.isNull(relTransRemark) ? "-"
				: relTransRemark);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, remarkTv);

		outBranchName.setText(fromAccountBankName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				outBranchName);
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
		case R.id.btn_last_trans_rel_confirm:// 用户点击上一步
			finish();
			break;
		case R.id.btn_next_trans_rel_confirm:// 用户点击下一步

			// if (smcLinearLayout.isShown() && !optLinearLayout.isShown()) {
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
			// }
			// if (optLinearLayout.isShown() && !smcLinearLayout.isShown()) {
			// //
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
			//
			// if (optLinearLayout.isShown() && smcLinearLayout.isShown()) {
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
							// 显示通讯框
							Flag = SUBMIT;
							BaseHttpEngine.showProgressDialog();
							requestPSNGetTokenId((String) BaseDroidApp
									.getInstanse().getBizDataMap()
									.get(ConstantGloble.CONVERSATION_ID));
						}
					});
			// // 显示通讯框
			// Flag = SUBMIT;
			// BaseHttpEngine.showProgressDialog();
			// requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
			// .getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
			break;
		}
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		tokenId = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		if (Flag == SUBMIT) {
			requestForTransBocTransferSubmit();
		} else if (Flag == ADD) {
			requestPsnTransBocAddPayee();
		}

	}

	/**
	 * 行内转账
	 */
	private void requestForTransBocTransferSubmit() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		if (tranTypeFlag == TRANTYPE_NOREL_BANKIN) {
			biiRequestBody.setMethod(Tran.TRANSBOCTRANSFERSUBMIT);
		} else if (tranTypeFlag == TRANTYPE_DIR_BANKIN) {
			biiRequestBody.setMethod(Tran.TRANSDIRBOCTRANSFERSUBMIT);
		}
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));

		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.RELTRANS_FROMACCOUNTID_REQ, fromAccountId);
		map.put(Tran.RELTRANS_PAYEEACTNO_REQ, payeeActno);
		map.put(Tran.RELTRANS_EXECUTETYPE_REQ, relExecuteType);
		map.put(Tran.TRANS_TO_ACCOUNT_TYPE, toAccountType);
		map.put(Tran.TRANS_PAYEEMOBILE_REQ, mobileNumber);
		map.put(Tran.TRANS_PAYEEBANKNUM_REQ, payeeBankNum);
		map.put(Tran.RELTRANS_REMARK_REQ, relTransRemark);
		map.put(Tran.RELTRANS_AMOUNT_REQ, relTransAmount);
		map.put(Tran.RELTRANS_PAYEENAME_REQ, payeeName);
		map.put(Tran.TRANS_REMITTANCEINFO_REQ, "");
		map.put(Tran.RELTRANS_CURRENCY_REQ, currencySer);
		map.put(Tran.TRANS_PAYEEID_REQ, toAccountId);
		map.put(Tran.RELTRANS_CYCLESELECT_REQ, strWeek);
		map.put(Tran.TRANS_EXECUTEDATE_REQ, preDateExecuteDate);
		map.put(Tran.RELTRANS_STARTDATE_REQ, strStartDate);
		map.put(Tran.RELTRANS_ENDDATE_REQ, strEndDate);
		// map.put(Tran.TRANS_SMC_REQ, smc);
		// map.put(Tran.TRANS_SMC_RC_REQ, smc_rc);
		// map.put(Tran.TRANS_OTP_REQ, otp);
		// map.put(Tran.TRANS_OPT_RC_REQ, otp_rc);
		/** 安全工具参数获取 */
		usbKeyText.InitUsbKeyResult(map);
		map.put(Tran.RELTRANS_TOKEN_REQ, tokenId);
		// map.put(Tran.TRANS_SIGNEDDATA_REQ, _signedData);
		SipBoxUtils.setSipBoxParams(map);
		GetPhoneInfo.addPhoneInfo(map);// 反欺诈 设备指纹
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForTransBocTransferSubmitCallBack");
	}

	/**
	 * 行内转账返回
	 * 
	 * @param resultObj
	 */
	public void requestForTransBocTransferSubmitCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setNoRelBankInDealCallBackMap(result);
		// 判断是否发送保存常用收款人接口
		Map<String, String> userInputMap = TranDataCenter.getInstance()
				.getUserInputMap();
		String flag = userInputMap.get(Tran.INPUT_ADD_PAYEE_FALG);
		if (!StringUtil.isNullOrEmpty(flag) && flag.equals(ConstantGloble.TRUE)) {
			// 发送保存常用收款人接口
			Flag = ADD;
			requestCommConversationId();
		}
		Intent intent = getIntent();
		// intent.putExtra(Tran.MANAGE_PRE_transMode_RES, relExecuteType);
		intent.putExtra(Tran.ACC_ACCOUNTID_REQ, fromAccountId);
		intent.setClass(NoRelConfirmInfoActivity1.this,
				NoRelSuccessActivity1.class);
		startActivity(intent);
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
	}

	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber() {
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

	/**
	 * 行内保存常用收款人
	 */
	private void requestPsnTransBocAddPayee() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.PSNTRANSBOCADDPAYEE_INTERFACE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.BOCADDPAYEE_PAYEENAME_TOACCOUNTID_REQ, payeeActno);
		map.put(Tran.BOCADDPAYEE_PAYEENAME_REQ, payeeName);
		map.put(Tran.BOCADDPAYEE_PAYEEBANKNUM_REQ, payeeBankNum);
		map.put(Tran.BOCADDPAYEE_TOACCOUNTTYPE_REQ, toAccountType);
		map.put(Tran.BOCADDPAYEE_PAYEEMOBILE_REQ, TranDataCenter.getInstance()
				.getAccInInfoMap().get(Tran.TRANS_MOBILE_RES));
		map.put(Tran.BOCADDPAYEE_TOKEN_REQ, tokenId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnTransBocAddPayeeCallBack");
	}

	public void requestPsnTransBocAddPayeeCallBack(Object resultObj) {
		// BiiResponse biiResponse = (BiiResponse) resultObj;
		// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// Map<String, Object> result = (Map<String, Object>) biiResponseBody
		// .getResult();

//		CustomDialog.toastShow(NoRelConfirmInfoActivity1.this,
//				NoRelConfirmInfoActivity1.this
//						.getString(R.string.new_add_payee_success));
	}

}
