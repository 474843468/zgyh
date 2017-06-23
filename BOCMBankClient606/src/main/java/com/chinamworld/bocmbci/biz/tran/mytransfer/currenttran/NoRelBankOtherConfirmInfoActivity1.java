package com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
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
import com.chinamworld.bocmbci.bii.constant.Ecard;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.managetrans.managetranrecords.ManageTransRecordActivity1;
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
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.IntentSpan;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 
 * @author WJP
 * 
 */
@SuppressWarnings("ResourceType")
public class NoRelBankOtherConfirmInfoActivity1 extends TranBaseActivity
		implements OnClickListener {

	private static final String TAG = NoRelBankOtherConfirmInfoActivity1.class
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
	/** 转账金额 */
	private TextView transAmountTv = null;
	/** 转账类型 */
	// private TextView transTypeTv = null;
	/** 执行方式 */
	private TextView exeTypeTv = null;
	/** 附言 */
	private TextView remarkTv = null;
	/** 执行方式 */
	private String relExecuteType = "";

	private LinearLayout preDateLL;

	/** 应收费用 */
	private TextView actChargeTv;
	/** 拟收费用 */
	private TextView farkChargeTv;

	/** 执行日期 */
	private TextView exeDateTv = null;
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
	/** 转出账户昵称 */
	private TextView accoutNicknameTv;
	/** 收款人姓名 */
	private TextView accinNickNameTv;

	private String accOutNo;
	private String outNickName;
	private String fromAccountArea;
	private String fromAccountBankName;
	// private String toAccountArea;
	// private String toAccountBankName;
	private String actCharge;
	private String fakeCharge;
	private String bankName = "";

	// /**转账类型*/
	// private TextView tranTypeTv;
	// 转出账户
	private String fromAccountId = "";
	private String toAccountId = "";
	private String payeeActno = "";
	/** 收款人姓名 */
	private String payeeName = "";
	/** 收款人手机号 */
	private String mobileNumber = "";

	private String currencySer = "";
	private String relTransRemark = "";
	private String relTransAmount = "";
	private String tokenId = "";
	private String dueDate = "";
	/** 汇款用途 */
	private String remittanceInfo = "";
	/** 转入账户开户行名称 */
	private String toOrgName = "";
	/** 省行联行号 */
	private String cnapsCode = "";
	/** CA认证生成的密文 */
	private String _signedData = "";
	/** 是否打开非交易时间转预约 */
	// private String openChangeBooking = "";
	// 转账日期 //立即执行：该字段为空，BII自动取为当前系统日期
	// 预约日期执行：为预约日期
	// 预约周期执行：为起始日期
	/** 手机交易码有效时间 */
	// private String smcTrigerInterval;
	// private int smcTime;

	private String randomNumber;

	// private List<Map<String, Object>> factorList;

	/** 预交易返回数据 */
	// private String toAccountType;
	// private String payeeBankNum;
	/** 是否发送手机短信通知收款人标示 */
	private String isSendSmc = "";
	/** 手机号 */
	private LinearLayout mobileLayout;
	private TextView mobileTv;

	private int Flag = 0;
	private static final int SUBMIT = 1;// 提交
	private static final int ADD = 2;// 新增收款人
	private static final int MYNEW = 3;// 国内跨行转账非工作时间转预约
	private static final int DIRADD = 4;// 定向收款人维护
	private static final int EBPSDIRADD = 5;// 实时定向维护
	private static final int EBPSREALADD = 6;// 实时保存新增收款人
	/** 预交易数据返回 */
	private Map<String, Object> noRelBankOtherMap;
	/** 动态口令 */
	private Boolean isOtp;
	/** 手机交易码 */
	private Boolean isSmc;

	/**是否点击了到转账记录*/
	private Boolean ischeck;
	
	/**开户行*/
	private LinearLayout ll_bankname; 
	/**基准费用*/
	private LinearLayout ll_need; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.tran_my_trans));
		View view = mInflater.inflate(
				R.layout.tran_confirm_info_no_rel_bank_other_activity, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		/** 预交易数据返回结果 */
		noRelBankOtherMap = TranDataCenter.getInstance()
				.getNoRelBankOtherCallBackMap();
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
		if(tranTypeFlag == TRANTYPE_SHISHI_BANKOTHER
				||tranTypeFlag == TRANTYPE_SHISHI_DIR_BANKOTHER	){
				ll_bankname.setVisibility(View.GONE);
				
			}
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
		if (tranTypeFlag == TRANTYPE_NOREL_BANKOTHER) {
			// 跨行转账
			toAccountId = (String) accInInfoMap.get(Tran.TRANS_PAYEETID_RES);
		} else if (tranTypeFlag == TRANTYPE_DIR_BANKOTHER
				|| tranTypeFlag == TRANTYPE_SHISHI_DIR_BANKOTHER) {
			// 跨行定向 、跨行实时定向
			toAccountId = (String) accInInfoMap
					.get(Tran.TRAN_DIR_QUERYLIST_PAYEEID_RES);
		}
		// 转入账户
		payeeActno = (String) accInInfoMap.get(Tran.ACCOUNTNUMBER_RES);

		if(StringUtil.isNullOrEmpty(payeeActno)){
			payeeActno=	 (String) accInInfoMap.get(Ecard.ECARD_PAYEEACCOUNTNUMBER_RES);
		}

		// 账户名称
		payeeName = (String) accInInfoMap.get(Tran.ACCOUNTNAME_RES);
		if(StringUtil.isNullOrEmpty(payeeName)){
			payeeName=(String) accInInfoMap.get(Ecard.ECARD_PAYEEACCOUNTNAME_RES);
		}
		// 转入银行名称 // 转入账户开户行
		bankName = (String) accInInfoMap.get(Tran.TRANS_BANKNAME_RES);

		if(StringUtil.isNullOrEmpty(bankName)){
			bankName=(String) accInInfoMap.get(Ecard.ECARD_PAYEEBANKNAME_RES);
		}

		// 转入账户开户行名称
		toOrgName = (String) accInInfoMap.get(Tran.TRANS_ADDRESS_RES);
		if(StringUtil.isNullOrEmpty(toOrgName)){
			toOrgName=(String) accInInfoMap.get(Ecard.ECARD_OPENINGBANKNAME_RES);
		}
		cnapsCode = (String) accInInfoMap.get(Tran.TRANS_CNAPSCODE_RES);

//		if(StringUtil.isNullOrEmpty(cnapsCode)){
//			cnapsCode=	 (String) accInInfoMap.get(Ecard.ECARD_CNAPSCODE_RES);
//		}
		Intent it=this.getIntent();
		if(it.hasExtra("comm2shishi")){
			cnapsCode=it.getStringExtra("comm2shishi");
		}
		

		if (tranTypeFlag == TRANTYPE_SHISHI_BANKOTHER) {
			
			findViewById(R.id.textview_tishi).setVisibility(View.GONE);
			// 跨行实时
			toAccountId = (String) accInInfoMap
					.get(Tran.EBPSQUERY_PAYEEACTID_REQ);
			payeeActno = (String) accInInfoMap
					.get(Tran.EBPSQUERY_PAYEEACTNO_REQ);

			if(StringUtil.isNullOrEmpty(payeeActno)){
				payeeActno=	 (String) accInInfoMap.get(Ecard.ECARD_PAYEEACCOUNTNUMBER_RES);
			}

			payeeName = (String) accInInfoMap
					.get(Tran.EBPSQUERY_PAYEEACTNAME_REQ);
			if(StringUtil.isNullOrEmpty(payeeName)){
				payeeName=(String) accInInfoMap.get(Ecard.ECARD_PAYEEACCOUNTNAME_RES);
			}
			toOrgName = (String) accInInfoMap
					.get(Tran.EBPSQUERY_PAYEEBANKNAME_REQ);

			cnapsCode = (String) accInInfoMap
					.get(Tran.EBPSQUERY_PAYEECNAPS_REQ);

//			if(StringUtil.isNullOrEmpty(cnapsCode)){
//				cnapsCode=(String) accInInfoMap.get(Ecard.ECARD_PAYEECNAPS_RES);
//			}

			bankName = (String) accInInfoMap
					.get(Tran.EBPSQUERY_PAYEEBANKNAME_REQ);

//			if(StringUtil.isNullOrEmpty(bankName)){
//				bankName=(String) accInInfoMap.get(Ecard.ECARD_PAYEEBANKNAME_RES);
//			}
			Intent its=this.getIntent();
			if(its.hasExtra("comm2shishi")){
				cnapsCode=it.getStringExtra("comm2shishi");
			}

		}
		if (StringUtil.isNullOrEmpty(cnapsCode)) {// 为空处理
			cnapsCode = "";
		}
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
		if (!StringUtil.isNullOrEmpty(userInputMap)) {
			isSendSmc = userInputMap.get(ConstantGloble.IS_SEND_SMC);
			if (!StringUtil.isNullOrEmpty(isSendSmc)
					&& isSendSmc.equals(ConstantGloble.TRUE)) {
				mobileNumber = userInputMap
						.get(Tran.INPUT_PAYEE_TRANSFER_MOBILE);
				mobileLayout.setVisibility(View.VISIBLE);
				mobileTv.setText(mobileNumber);
			} else {
				mobileNumber = "";
			}

			// 执行方式
			relExecuteType = userInputMap.get(Tran.MANAGE_PRE_transMode_RES);
			// combinId = userInputMap.get(Tran._COMBINID_REQ);
			relTransRemark = userInputMap.get(Tran.INPUT_TRANSFER_REMARK);
			relTransAmount = userInputMap.get(Tran.INPUT_TRANSFER_AMOUNT);
		}
		switch (Integer.parseInt(relExecuteType)) {
		case 0:
			break;
		// 预约日期执行
		case 1:
			preDateLL.setVisibility(View.VISIBLE);
			// 预约日期执行 执行日期
			preDateExecuteDate = (String) userInputMap.get(Tran.INPUT_PRE_DATE);
			dueDate = preDateExecuteDate;
			exeDateTv.setText(preDateExecuteDate);
			break;
		}

		// noRelBankOtherMap = TranDataCenter.getInstance()
		// .getNoRelBankOtherCallBackMap();
		_signedData = (String) noRelBankOtherMap.get(Tran.TRANS_SIGNEDDATA_REQ);
		// @SuppressWarnings("unchecked")
		// List<Map<String, Object>> factorList = (List<Map<String, Object>>)
		// noRelBankOtherMap
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

		
		ll_bankname= (LinearLayout) findViewById(R.id.ll_bankname);

		ll_need= (LinearLayout) findViewById(R.id.ll_need);

		inBranchName = (TextView) findViewById(R.id.tv_branch_name_in_rel_confirm);
		// currencyTv = (TextView)
		// findViewById(R.id.tv_tran_currency_rel_confirm);
		transAmountTv = (TextView) findViewById(R.id.tv_transferAmount_rel_confirm);
		actChargeTv = (TextView) findViewById(R.id.tran_act_charge_tv);
		farkChargeTv = (TextView) findViewById(R.id.tran_fack_charge_tv);
		remarkTv = (TextView) findViewById(R.id.tv_remark_info_rel_confirm);
		exeTypeTv = (TextView) findViewById(R.id.tv_executeType_rel_confirm);

		preDateLL = (LinearLayout) findViewById(R.id.ll_preDate_confirm_info);
		exeDateTv = (TextView) findViewById(R.id.tv_exeDate_info_rel_confirm);

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
		usbKeyText
				.Init(mmconversationId, randomNumber, noRelBankOtherMap, this);
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
		accInAreaTv.setText(bankName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accInAreaTv);
		inBranchName.setText(toOrgName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				inBranchName);
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


	private Handler handler = new Handler(){   
		public void handleMessage(Message msg) { 
			Map<String, Object> noRelBankOtherMap = TranDataCenter.getInstance()
					.getNoRelBankOtherDealCallBackMap();
			String transactionId = (String) noRelBankOtherMap
					.get(Tran.EBPSSUB_TRANSACTIONID_RES);
			switch (msg.what) {
			case 1:
				int time = TranDataCenter.getInstance().getTimer();
				int timeChange = TranDataCenter.getInstance().getTimerChange();
				TranDataCenter.getInstance().getTextview().setText(timeChange+"");
				int interval = TranDataCenter.getInstance().getTimerInterval();
				if( (time - timeChange) % interval == 0 && timeChange > 0){
					//每隔interval秒发送一次请求 查询转账状态
					requestSingleTransQueryTransferRecordDedicated(transactionId);
				}else if(timeChange == 0){
					TranDataCenter.getInstance().setColseDialog(true);
					BaseHttpEngine.dissMissProgressDialog();
					Intent intent = new Intent();
					intent.putExtra(Tran.MANAGE_PRE_transMode_RES, relExecuteType);
					intent.putExtra(Tran.ACC_ACCOUNTID_REQ, fromAccountId);
					intent.putExtra(TRANS_TYPE, tranTypeFlag);
					intent.putExtra(Tran.WEIXIN_RAFFLE, true);// 到成功界面就抽奖
					intent.setClass(NoRelBankOtherConfirmInfoActivity1.this,
							NoRelBankOtherSuccessActivity1.class);
					startActivity(intent);
				}

				break;
			case 2:
				
				if(!ischeck){
				BaseHttpEngine.dissMissProgressDialog();
				finish();
				ActivityTaskManager.getInstance().removeAllActivityInFirstChange();
				Intent intent = new Intent();
				intent.setClass(NoRelBankOtherConfirmInfoActivity1.this, TransferManagerActivity1.class);
				startActivity(intent);
				}
				break;
			default:
				break;
			}
		}  

	};

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
			//
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
			if (tranTypeFlag == TRANTYPE_SHISHI_BANKOTHER) {
				// 跨行实时
				psnEbpsRealTimePaymentTransfer();
			} else {
				requestForTransNationalTransferSubmit();
			}
		} else if (Flag == ADD) {
			requestPsnTransNationalAddPayee();
		} else if (Flag == MYNEW) {
			requestPsnTransNationalChangeBooking(tokenId);
		} else if (Flag == DIRADD) {
			// 跨行定向新增收款人
			requestDirPsnTransNationalAddPayee();
		} else if (Flag == EBPSDIRADD) {
			// 跨行实时定向
			requestDirPsnTransNationalAddPayee();
		} else if (Flag == EBPSREALADD) {
			// 新增实时收款人
			requestPsnTransNationalAddPayee();
		}
	}

	/**
	 * 跨行转账
	 */
	private void requestForTransNationalTransferSubmit() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		if (tranTypeFlag == TRANTYPE_NOREL_BANKOTHER) {
			biiRequestBody.setMethod(Tran.TRANSNATIONALTRANSFERSUBMIT);
		} else if (tranTypeFlag == TRANTYPE_DIR_BANKOTHER) {
			biiRequestBody.setMethod(Tran.TRANSDIRNATIONALTRANSFERSUBMIT);
		} else if (tranTypeFlag == TRANTYPE_SHISHI_DIR_BANKOTHER) {
			biiRequestBody.setMethod(Tran.TRANSDIREBPSNATIONALTRANSFERSUBMIT);
		}
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));

		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.RELTRANS_FROMACCOUNTID_REQ, fromAccountId);
		map.put(Tran.RELTRANS_PAYEEACTNO_REQ, payeeActno);
		map.put(Tran.RELTRANS_EXECUTETYPE_REQ, relExecuteType);
		map.put(Tran.RELTRANS_REMARK_REQ, relTransRemark);
		map.put(Tran.RELTRANS_AMOUNT_REQ,
				StringUtil.parseStringPattern(relTransAmount, 2));
		map.put(Tran.RELTRANS_PAYEENAME_REQ, payeeName);
		map.put(Tran.TRANS_REMITTANCEINFO_REQ, remittanceInfo);

		map.put(Tran.RELTRANS_CURRENCY_REQ, currencySer);
		map.put(Tran.TRANS_BOCNATIONAL_PAYEEID_REQ, toAccountId);
		map.put(Tran.TRANS_PAYEEMOBILE_REQ, mobileNumber);
		map.put(Tran.TRANS_TRANSUBMIT_BANKNAME_REQ, bankName);
		map.put(Tran.TRANS_TRANSUBMIT_TOORGNAME_REQ, toOrgName);	
		if (tranTypeFlag == TRANTYPE_SHISHI_DIR_BANKOTHER) {
			if(StringUtil.isNullOrEmpty(toOrgName)){
				map.put(Tran.TRANS_BOCNATIONAL_TOORGNAME_REQ, bankName);	
			}
		}
		map.put(Tran.TRANS_TRANSUBMIT_CNAPSCODE_REQ, cnapsCode);
		// map.put(Tran.TRANS_SMC_REQ, smc);
		// map.put(Tran.TRANS_SMC_RC_REQ, smc_rc);
		// map.put(Tran.TRANS_OTP_REQ, otp);
		// map.put(Tran.TRANS_OPT_RC_REQ, otp_rc);
		/** 安全工具参数获取 */
		usbKeyText.InitUsbKeyResult(map);

		map.put(Tran.RELTRANS_TOKEN_REQ, tokenId);
		// map.put(Tran.TRANS_SIGNEDDATA_REQ, _signedData);
		if (tranTypeFlag != TRANTYPE_SHISHI_DIR_BANKOTHER) {
			map.put(Tran.TRANS_TRANSUBMIT_OPENCHANGEBOOKING_REQ,
					OPENCHANGEBOOKING);
			map.put(Tran.TRANS_DUE_DATE, dueDate);
			map.put(Tran.TRANS_EXECUTEDATE_REQ, preDateExecuteDate);
		} else {
			map.put(Tran.TRANS_BOCNATIONAL__COMBINID_REQ, BaseDroidApp
					.getInstanse().getSecurityChoosed());
			switch (Integer.parseInt(relExecuteType)) {
			case 0:
				break;
			case 1:// 预约日期执行
				map.put(Tran.TRANS_DUE_DATE, dueDate);
				map.put(Tran.TRANS_EXECUTEDATE_REQ, preDateExecuteDate);
				break;
			default:
				break;
			}
		}
		GetPhoneInfo.addPhoneInfo(map);// 反欺诈 设备指纹
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForTransNationalTransferSubmitCallBack");
	}

	/**
	 * 跨行转账返回
	 * 
	 * @param resultObj
	 */
	public void requestForTransNationalTransferSubmitCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setNoRelBankOtherDealCallBackMap(result);
		
		if(tranTypeFlag == TRANTYPE_SHISHI_DIR_BANKOTHER){
			Map<String, Object> noRelBankOtherMap = TranDataCenter.getInstance()
					.getNoRelBankOtherDealCallBackMap();
			String transactionId = (String) noRelBankOtherMap
					.get(Tran.EBPSSUB_TRANSACTIONID_RES);

			requestSingleTransQueryTransferRecord(transactionId);

			return;

		}
		String workFlag = (String) result
				.get(Tran.TRANS_BOCNATIONAL_WORKDAYFLAG_RES);
		// 判断是否非工作时间标识
		if (Integer.parseInt(relExecuteType) == 0) {
			if (!StringUtil.isNull(workFlag)
					&& workFlag.equals(ConstantGloble.ACC_CURRENTINDEX_VALUE)
					&& Double.valueOf(relTransAmount) >= 50000) {
				Flag = MYNEW;
				requestSystemDateTime();
				BiiHttpEngine.showProgressDialog();
			} else {
				if (!StringUtil.isNullOrEmpty(TranDataCenter.getInstance()
						.getNationalChangeBookingMap())) {
					TranDataCenter.getInstance().getNationalChangeBookingMap()
							.clear();
				}
				// 保存常用收款人
				Map<String, String> userInputMap = TranDataCenter.getInstance()
						.getUserInputMap();
				String flag = userInputMap.get(Tran.INPUT_ADD_PAYEE_FALG);
				if (!StringUtil.isNullOrEmpty(flag)
						&& flag.equals(ConstantGloble.TRUE)) {
					// 发送保存常用收款人接口
					Flag = ADD;
					requestCommConversationId();
					// requestPsnTransNationalAddPayee();
				}
				deal();
				Intent intent = new Intent();
				intent.putExtra(Tran.MANAGE_PRE_transMode_RES, relExecuteType);
				intent.putExtra(Tran.ACC_ACCOUNTID_REQ, fromAccountId);
				intent.putExtra(TRANS_TYPE, tranTypeFlag);
				intent.putExtra(Tran.WEIXIN_RAFFLE, true);
				intent.setClass(NoRelBankOtherConfirmInfoActivity1.this,
						NoRelBankOtherSuccessActivity1.class);
				startActivity(intent);

			}
		} else {
			if (!StringUtil.isNullOrEmpty(TranDataCenter.getInstance()
					.getNationalChangeBookingMap())) {
				TranDataCenter.getInstance().getNationalChangeBookingMap()
						.clear();
			}
			// 保存常用收款人
			Map<String, String> userInputMap = TranDataCenter.getInstance()
					.getUserInputMap();
			String flag = userInputMap.get(Tran.INPUT_ADD_PAYEE_FALG);
			if (!StringUtil.isNullOrEmpty(flag)
					&& flag.equals(ConstantGloble.TRUE)) {
				// 发送保存常用收款人接口
				Flag = ADD;
				requestCommConversationId();
				// requestPsnTransNationalAddPayee();
			}
			deal();
			Intent intent = new Intent();
			intent.putExtra(Tran.MANAGE_PRE_transMode_RES, relExecuteType);
			intent.putExtra(Tran.ACC_ACCOUNTID_REQ, fromAccountId);
			intent.putExtra(TRANS_TYPE, tranTypeFlag);
			intent.putExtra(Tran.WEIXIN_RAFFLE, true);
			intent.setClass(NoRelBankOtherConfirmInfoActivity1.this,
					NoRelBankOtherSuccessActivity1.class);
			startActivity(intent);

		}

	}

	/**
	 * 处理定向和非定向
	 */
	public void deal() {
		Map<String, Object> map = TranDataCenter.getInstance()
				.getAccInInfoMap();
		if (tranTypeFlag == TRANTYPE_DIR_BANKOTHER) {
			// 跨行定向——第一次要保存定向收款人——是否有所属银行
			boolean ishavebankname = (Boolean) map.get(ISHAVEBANKNAME);
			if (!ishavebankname) {
				// 没有所属银行——需要维护
				Flag = DIRADD;
				requestCommConversationId();
			}
		} else if (tranTypeFlag == TRANTYPE_NOREL_BANKOTHER) {
			// 普通跨行——如果第一次没有维护上开户行——是否有开户行
//			boolean ishaveaddress = (Boolean) map.get(ISHAVEADDRESS);
//			if (!ishaveaddress) {
//				// 没有开户行——需要维护——走修改流程
//			}
		} else if (tranTypeFlag == TRANTYPE_SHISHI_DIR_BANKOTHER) {
			// 实时定向
			boolean ishavebankname = (Boolean) map.get(ISHAVEBANKNAME);
			if (!ishavebankname) {
				// 没有所属银行——需要维护
				Flag = EBPSDIRADD;
				requestCommConversationId();
			}
		}
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		String exeuteTime = (String) TranDataCenter.getInstance()
				.getNoRelBankOtherDealCallBackMap()
				.get(Tran.TRANS_BOCNATIONAL_EXECUTEDATE_RES);
		int i = QueryDateUtils.compareTranDateTime(dateTime, exeuteTime);
		if (i == 1) {
			BiiHttpEngine.dissMissProgressDialog();
			BaseDroidApp
					.getInstanse()
					.showErrorDialog(
							NoRelBankOtherConfirmInfoActivity1.this
									.getString(R.string.tran_error_1),
							R.string.cancle, R.string.confirm,
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									switch (Integer.parseInt(v.getTag() + "")) {
									case CustomDialog.TAG_SURE:
										// 确定操作
										BaseDroidApp.getInstanse()
												.dismissErrorDialog();
										requestPSNGetTokenId((String) BaseDroidApp
												.getInstanse()
												.getBizDataMap()
												.get(ConstantGloble.CONVERSATION_ID));
										BiiHttpEngine.showProgressDialog();
										break;
									case CustomDialog.TAG_CANCLE:
										// 取消操作
										BaseDroidApp.getInstanse()
												.dismissErrorDialog();
										BiiHttpEngine.dissMissProgressDialog();
										break;
									}
								}
							});
		} else if (i == 2) {
			BiiHttpEngine.dissMissProgressDialog();
			BaseDroidApp
					.getInstanse()
					.showErrorDialog(
							NoRelBankOtherConfirmInfoActivity1.this
									.getString(R.string.tran_error_2),
							R.string.cancle, R.string.tran_order,
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									switch (Integer.parseInt(v.getTag() + "")) {
									case CustomDialog.TAG_SURE:
										// 确定操作
										BaseDroidApp.getInstanse()
												.dismissErrorDialog();
										requestPSNGetTokenId((String) BaseDroidApp
												.getInstanse()
												.getBizDataMap()
												.get(ConstantGloble.CONVERSATION_ID));
										BiiHttpEngine.showProgressDialog();
										break;
									case CustomDialog.TAG_CANCLE:
										// 取消操作
										BaseDroidApp.getInstanse()
												.dismissErrorDialog();
										BiiHttpEngine.dissMissProgressDialog();
										break;
									}
								}
							});
		} else {
			requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
					.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		}
	}

	/** 国内跨行转账非工作时间转预约 */
	public void requestPsnTransNationalChangeBooking(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.TRANS_PSNTRANSNATIONALCHANGEBOOKING_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
//		paramsmap.put(Tran.TRANS_NATIONAL_FROMACCOUNTID_REQ, fromAccountId);
//		paramsmap.put(Tran.TRANS_NATIONAL_PAYEEACTNO_REQ, payeeActno);
//		paramsmap.put(Tran.TRANS_NATIONAL_REMARK_REQ, relTransRemark);
//		paramsmap.put(Tran.TRANS_NATIONAL_PAYEENAME_REQ, payeeName);
//		paramsmap.put(Tran.TRANS_NATIONAL_REMITTANCEINFO_REQ, remittanceInfo);
//		paramsmap.put(
//				Tran.TRANS_NATIONAL_EXECUTEDATE_REQ,
//				(String) TranDataCenter.getInstance()
//						.getNoRelBankOtherDealCallBackMap()
//						.get(Tran.TRANS_BOCNATIONAL_EXECUTEDATE_RES));
//		paramsmap.put(Tran.TRANS_NATIONAL_CURRENCY_REQ, currencySer);
//		paramsmap.put(Tran.TRANS_NATIONAL_PAYEEID_REQ, toAccountId);
//		paramsmap.put(Tran.TRANS_NATIONAL_PAYEEMOBILE_REQ, mobileNumber);
//		paramsmap.put(Tran.TRANS_NATIONAL_BANKNAME_REQ, bankName);
//		paramsmap.put(Tran.TRANS_NATIONAL_TOORGNAME_REQ, toOrgName);
//		paramsmap.put(Tran.TRANS_NATIONAL_CNAPSCODE_REQ, cnapsCode);
		if (tranTypeFlag == TRANTYPE_NOREL_BANKOTHER) {
			paramsmap.put(Tran.TRANS_NATIONAL_TRANSTYPE_REQ,
					ConstantGloble.LOAN_CURRENTINDEX_VALUE);
		} else if (tranTypeFlag == TRANTYPE_DIR_BANKOTHER) {
			// 定向
			paramsmap.put(Tran.TRANS_NATIONAL_TRANSTYPE_REQ,
					ConstantGloble.ACC_CURRENTINDEX_VALUE);
		}
		paramsmap.put(Tran.TRANS_NATIONAL_TOKEN_REQ, tokenId);
//		paramsmap.put(Tran.TRANS_NATIONAL_AMOUNT_REQ,
//				StringUtil.parseStringPattern(relTransAmount, 2));
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnTransNationalChangeBookingCallBack");
	}

	/** 国内跨行转账非工作时间转预约 */
	public void requestPsnTransNationalChangeBookingCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// TODO
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setNationalChangeBookingMap(resultMap);
		// 保存常用收款人
		Map<String, String> userInputMap = TranDataCenter.getInstance()
				.getUserInputMap();
		String flag = userInputMap.get(Tran.INPUT_ADD_PAYEE_FALG);
		if (!StringUtil.isNullOrEmpty(flag) && flag.equals(ConstantGloble.TRUE)) {
			// 发送保存常用收款人接口
			Flag = ADD;
			requestCommConversationId();
			// requestPsnTransNationalAddPayee();
		}
		deal();
		Intent intent = new Intent();
		intent.putExtra(Tran.MANAGE_PRE_transMode_RES, relExecuteType);
		intent.putExtra(Tran.ACC_ACCOUNTID_REQ, fromAccountId);
		intent.putExtra(TRANS_TYPE, tranTypeFlag);
		intent.putExtra(Tran.WEIXIN_RAFFLE, false);
		intent.setClass(NoRelBankOtherConfirmInfoActivity1.this,
				NoRelBankOtherSuccessActivity1.class);
		startActivity(intent);
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
	}

	/**
	 * PsnSendSMSCodeToMobile获取并发送手机交易码 req
	 */
	public void requestForSendSMSCodeToMobile() {
		BaseHttpEngine.showProgressDialog();
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
		BaseHttpEngine.dissMissProgressDialog();
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
	 * 跨行保存常用收款人
	 */
	private void requestPsnTransNationalAddPayee() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		if (Flag == ADD) {
			biiRequestBody.setMethod(Tran.PSNTRANS_NATIONAL_ADDPAYEE);
			map.put(Tran.BOCADDPAYEE_PAYEENAME_TOACCOUNTID_REQ, payeeActno);
			map.put(Tran.BOCADDPAYEE_PAYEENAME_REQ, payeeName);
			map.put(Tran.TRANS_CNAPSCODE_RES, cnapsCode);
			map.put(Tran.TRANS_BANKNAME_RES, bankName);
			map.put(Tran.NATIONALADDPAYEE_TOORGNAME_REQ, toOrgName);
			map.put(Tran.BOCADDPAYEE_PAYEEMOBILE_REQ,
					TranDataCenter.getInstance().getAccInInfoMap()
							.get(Tran.TRANS_BOCNATIONAL_PAYEEMOBILE_REQ));
			map.put(Tran.BOCADDPAYEE_TOKEN_REQ, tokenId);
		} else if (Flag == EBPSREALADD) {
			// 实时转账
			biiRequestBody.setMethod(Tran.PSNEBPSREALTIMEPAYMENTSAVEPAYEE_API);
			map.put(Tran.SAVEEBPS_PAYEEACTNO_REQ, payeeActno);
			map.put(Tran.SAVEEBPS_PAYEENAME_REQ, payeeName);
			map.put(Tran.SAVEEBPS_PAYEEORGNAME_REQ, toOrgName);
			map.put(Tran.SAVEEBPS_PAYEEBANKNAME_REQ, toOrgName);
			map.put(Tran.SAVEEBPS_PAYEECNAPS_REQ, cnapsCode);
			map.put(Tran.SAVEEBPS_PAYEEMOBILE_REQ,
					TranDataCenter.getInstance().getAccInInfoMap()
							.get(Tran.TRANS_BOCNATIONAL_PAYEEMOBILE_REQ));
			map.put(Tran.SAVEEBPS_TOKEN_REQ, tokenId);
		}
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnTransNationalAddPayeeCallBack");
	}

	public void requestPsnTransNationalAddPayeeCallBack(Object resultObj) {
//		CustomDialog.toastShow(NoRelBankOtherConfirmInfoActivity1.this,
//				NoRelBankOtherConfirmInfoActivity1.this
//				.getString(R.string.new_add_payee_success));
	}

	/**
	 * 跨行定向保存常用收款人
	 */
	private void requestDirPsnTransNationalAddPayee() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		if (tranTypeFlag == TRANTYPE_DIR_BANKOTHER) {
			biiRequestBody.setMethod(Tran.PSNDIRTRANSNATIONALADDPAYEE_API);
		} else if (tranTypeFlag == TRANTYPE_SHISHI_DIR_BANKOTHER) {
			biiRequestBody.setMethod(Tran.PSNDIREBPSTRANSNATIONALADDPAYEE_API);
		}
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.DIR_ADDPAYEE_PAYEEACTNO_RES, payeeActno);
		map.put(Tran.DIR_ADDPAYEE_PAYEEID_RES, toAccountId);
		map.put(Tran.DIR_ADDPAYEE_PAYEENAME_RES, payeeName);
		map.put(Tran.DIR_ADDPAYEE_CNAPSCODE_RES, cnapsCode);
		map.put(Tran.DIR_ADDPAYEE_BANKNAME_RES, bankName);
		map.put(Tran.DIR_ADDPAYEE_TOORGNAME_RES, toOrgName);
		map.put(Tran.DIR_ADDPAYEE_PAYEEMOBILE_RES, TranDataCenter.getInstance()
				.getAccInInfoMap().get(Tran.TRANS_BOCNATIONAL_PAYEEMOBILE_REQ));
		map.put(Tran.DIR_ADDPAYEE_TOKEN_RES, tokenId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestDirPsnTransNationalAddPayeeCallBack");
	}

	public void requestDirPsnTransNationalAddPayeeCallBack(Object resultObj) {
		// 暂不提示
		// CustomDialog.toastShow(NoRelBankOtherConfirmInfoActivity1.this,
		// NoRelBankOtherConfirmInfoActivity1.this
		// .getString(R.string.new_add_payee_success));
	}

	/**
	 * 跨行实时转账
	 */
	private void psnEbpsRealTimePaymentTransfer() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.PSNEBPSREALTIMEPAYMENTTRANSFER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));

		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.EBPSSUB_FROMACCOUNTID_REQ, fromAccountId);
		map.put(Tran.EBPSSUB_PAYEEACTNO_REQ, payeeActno);
		map.put(Tran.EBPSSUB_PAYEENAME_REQ, payeeName);
		map.put(Tran.EBPSSUB_PAYEECNAPS_REQ, cnapsCode);
		map.put(Tran.EBPSSUB_PAYEEBANKNAME_REQ, bankName);
		map.put(Tran.EBPSSUB_PAYEEORGNAME_REQ, toOrgName);
		map.put(Tran.EBPSSUB_CURRENCY_REQ, currencySer);
		map.put(Tran.EBPSSUB_AMOUNT_REQ,
				StringUtil.parseStringPattern(relTransAmount, 2));
		map.put(Tran.EBPSSUB_MEMO_REQ, relTransRemark);
		if (isSendSmc.equals(ConstantGloble.TRUE)) {
			map.put(Tran.EBPSSUB_SENDMSGFLAG_REQ, "1");
		} else {
			map.put(Tran.EBPSSUB_SENDMSGFLAG_REQ, "");
		}

		map.put(Tran.EBPSSUB_PAYEEMOBILE_REQ, mobileNumber);
		map.put(Tran.EBPSSUB_REMITTANCEINFO_REQ, "");
		map.put(Tran.EBPSSUB_EXECUTETYPE_REQ, relExecuteType);
		// map.put(Tran.TRANS_SMC_REQ, smc);
		// map.put(Tran.TRANS_SMC_RC_REQ, smc_rc);
		// map.put(Tran.TRANS_OTP_REQ, otp);
		// map.put(Tran.TRANS_OPT_RC_REQ, otp_rc);
		/** 安全工具参数获取 */
		usbKeyText.InitUsbKeyResult(map);
		map.put(Tran.EBPSSUB_DUEDATE_REQ, dueDate);
		map.put(Tran.RELTRANS_TOKEN_REQ, tokenId);
		// map.put(Tran.TRANS_SIGNEDDATA_REQ, _signedData);
		// 起始日期
		map.put(Tran.EBPSSUB_STARTDATE_REQ, "");
		// 结束日期
		map.put(Tran.EBPSSUB_ENDDATE_REQ, "");
		// 周期
		map.put(Tran.EBPSSUB_CYCLESELECT_REQ, "");
		map.put(Tran.EBPSSUB_EXECUTEDATE_REQ, preDateExecuteDate);
		GetPhoneInfo.addPhoneInfo(map);// 反欺诈 设备指纹
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"psnEbpsRealTimePaymentTransferCallBack");
	}

	public void psnEbpsRealTimePaymentTransferCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponseBody biiResponseBody = callBackResponse(resultObj);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setNoRelBankOtherDealCallBackMap(result);
		// 保存常用收款人
		Map<String, String> userInputMap = TranDataCenter.getInstance()
				.getUserInputMap();
		String flag = userInputMap.get(Tran.INPUT_ADD_PAYEE_FALG);
		if (!StringUtil.isNullOrEmpty(flag) && flag.equals(ConstantGloble.TRUE)) {
			Flag = EBPSREALADD;
			requestCommConversationId();
		}

		Map<String, Object> noRelBankOtherMap = TranDataCenter.getInstance()
				.getNoRelBankOtherDealCallBackMap();
		String transactionId = (String) noRelBankOtherMap
				.get(Tran.EBPSSUB_TRANSACTIONID_RES);



		requestSingleTransQueryTransferRecord(transactionId);


	}


	/**查询单笔跨行转账 回调*/
	public void requestSingleTransQueryTransferRecordCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponseBody biiResponseBody = callBackResponse(resultObj);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		String status = (String) result.get("status");//返回状态
//		status="B";
		String waitTimeForRealTime = (String) result.get(Tran.WAITIMEFORREALTIME);//倒计时
		String defaultTimeForRealTime = (String) result.get(Tran.DEFAULTTIMEFORREALTIME);//默认几秒执行一次
		TranDataCenter.getInstance().setTimer(Integer.parseInt(waitTimeForRealTime));
		TranDataCenter.getInstance().setTimerInterval(Integer.parseInt(defaultTimeForRealTime));
		if("A".equals(status)){
			// 转账成功  关闭通讯框 跳转到转账完成界面
			Intent intent = new Intent();
			intent.putExtra(Tran.MANAGE_PRE_transMode_RES, relExecuteType);
			intent.putExtra(Tran.ACC_ACCOUNTID_REQ, fromAccountId);
			intent.putExtra(TRANS_TYPE, tranTypeFlag);
			intent.putExtra(Tran.WEIXIN_RAFFLE, true);// 成功抽奖
			intent.setClass(NoRelBankOtherConfirmInfoActivity1.this,
					NoRelBankOtherSuccessActivity1.class);
			startActivity(intent);

		}else if("8".equals(status) || "B".equals(status) ||
				"12".equals(status)){
			// 转账失败 停止 请求接口  关闭20倒计时通讯框  显示失败通信框
			
			ischeck=false;
			SpannableString sp = new SpannableString("您可以通过转账记录查询失败原因");
			final Intent userIntent = new Intent();

			userIntent.setClass(NoRelBankOtherConfirmInfoActivity1.this, ManageTransRecordActivity1.class);
		
			sp.setSpan(new IntentSpan(new OnClickListener() {

				public void onClick(View view) {
					finish();
					ActivityTaskManager.getInstance().removeAllActivityInFirstChange();
					NoRelBankOtherConfirmInfoActivity1.this.startActivity(userIntent);
					ischeck=true;

				}

			}), 5, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);	
			
			BaseHttpEngine.showFailProgressDialog( sp,  new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
					ActivityTaskManager.getInstance().removeAllActivityInFirstChange();
					Intent intent = new Intent();
					intent.setClass(NoRelBankOtherConfirmInfoActivity1.this, TransferManagerActivity1.class);
					startActivity(intent);
				}
			},handler);
		}else if("G".equals(status) || "7".equals(status) ||"H".equals(status)
				|| "F".equals(status) ||"I".equals(status) || "L".equals(status)
				|| "K".equals(status)){
			// 等待银行处理状态
			BaseHttpEngine.showTranProgressDialog( new OnClickListener() {

				@Override
				public void onClick(View v) {
					TranDataCenter.getInstance().setColseDialog(true);
					BaseHttpEngine.dissMissProgressDialog();
					Intent intent = new Intent();
					intent.putExtra(Tran.MANAGE_PRE_transMode_RES, relExecuteType);
					intent.putExtra(Tran.ACC_ACCOUNTID_REQ, fromAccountId);
					intent.putExtra(TRANS_TYPE, tranTypeFlag);
					intent.putExtra(Tran.WEIXIN_RAFFLE, true);// 到成功界面就抽奖
					intent.setClass(NoRelBankOtherConfirmInfoActivity1.this,
							NoRelBankOtherSuccessActivity1.class);
					startActivity(intent);
				}
			} , handler);
		}
		else{
			// 等待银行处理状态
			BaseHttpEngine.showTranProgressDialog( new OnClickListener() {

				@Override
				public void onClick(View v) {
					TranDataCenter.getInstance().setColseDialog(true);
					BaseHttpEngine.dissMissProgressDialog();
					Intent intent = new Intent();
					intent.putExtra(Tran.MANAGE_PRE_transMode_RES, relExecuteType);
					intent.putExtra(Tran.ACC_ACCOUNTID_REQ, fromAccountId);
					intent.putExtra(TRANS_TYPE, tranTypeFlag);
					intent.putExtra(Tran.WEIXIN_RAFFLE, true);// 到成功界面就抽奖
					intent.setClass(NoRelBankOtherConfirmInfoActivity1.this,
							NoRelBankOtherSuccessActivity1.class);
					startActivity(intent);
				}
			} , handler);
		}

	}

	public void requestSingleTransQueryTransferRecordDedicatedCallBack(Object resultObj){

		
		
		BiiResponseBody biiResponseBody = callBackResponse(resultObj);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		String status = (String) result.get("status");//返回状态
		String waitTimeForRealTime = (String) result.get(Tran.WAITIMEFORREALTIME);//倒计时
		String defaultTimeForRealTime = (String) result.get(Tran.DEFAULTTIMEFORREALTIME);//默认几秒执行一次
		TranDataCenter.getInstance().setTimer(Integer.parseInt(waitTimeForRealTime));
		TranDataCenter.getInstance().setTimerInterval(Integer.parseInt(defaultTimeForRealTime));
		if("A".equals(status)){
			// 转账成功  关闭通讯框 跳转到转账完成界面
			BaseHttpEngine.dissMissProgressDialog();
			Intent intent = new Intent();
			intent.putExtra(Tran.MANAGE_PRE_transMode_RES, relExecuteType);
			intent.putExtra(Tran.ACC_ACCOUNTID_REQ, fromAccountId);
			intent.putExtra(TRANS_TYPE, tranTypeFlag);
			intent.putExtra(Tran.WEIXIN_RAFFLE, true);//轮询成功抽奖
			intent.setClass(NoRelBankOtherConfirmInfoActivity1.this,
					NoRelBankOtherSuccessActivity1.class);
			startActivity(intent);

		}else if("8".equals(status) || "B".equals(status) ||
				"12".equals(status)){
			ischeck=false;

			SpannableString sp = new SpannableString("您可以通过转账记录查询失败原因");
			final Intent userIntent = new Intent();

			userIntent.setClass(NoRelBankOtherConfirmInfoActivity1.this, ManageTransRecordActivity1.class);
		
			sp.setSpan(new IntentSpan(new OnClickListener() {

				public void onClick(View view) {
					finish();
					ActivityTaskManager.getInstance().removeAllActivityInFirstChange();
					NoRelBankOtherConfirmInfoActivity1.this.startActivity(userIntent);
					ischeck=true;
				
				}

			}), 5, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);	
			
			// 转账失败 停止 请求接口  关闭20倒计时通讯框  显示失败通信框
			BaseHttpEngine.dissMissProgressDialog();
			BaseHttpEngine.showFailProgressDialog(sp,new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
					ActivityTaskManager.getInstance().removeAllActivityInFirstChange();
					Intent intent = new Intent();
					intent.setClass(NoRelBankOtherConfirmInfoActivity1.this, TransferManagerActivity1.class);
					startActivity(intent);
				}
			},handler);
		}else if("G".equals(status) || "7".equals(status) ||"H".equals(status)
				|| "F".equals(status) ||"I".equals(status) || "L".equals(status)
				|| "K".equals(status)){
			// 等待银行处理状态
		}else{
			
		}

		
	}

}
