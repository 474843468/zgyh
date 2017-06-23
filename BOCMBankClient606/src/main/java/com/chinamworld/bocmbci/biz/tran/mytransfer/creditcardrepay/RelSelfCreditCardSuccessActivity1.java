package com.chinamworld.bocmbci.biz.tran.mytransfer.creditcardrepay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.acc.financeicaccount.MyFinanceIcAccountActivity;
import com.chinamworld.bocmbci.biz.acc.mybankaccount.AccManageActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.MyCardPaymentActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.MyCreditCardActivity;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran.TransferManagerActivity1;
import com.chinamworld.bocmbci.biz.tran.weixin.Weixin;
import com.chinamworld.bocmbci.biz.tran.weixin.WeixinSuccess;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 本人关联信用卡还款成功
 * 
 * @author
 * 
 */
public class RelSelfCreditCardSuccessActivity1 extends TranBaseActivity {

	/** 交易完成 确认 */
	private Button mConfirmBtn = null;
	/** 交易序号 */
	private TextView transactionTv = null;
	/** 转出账户 */
	private TextView accOutTv = null;
	/** 转入账户 */
	private TextView accInTv = null;
	/** 币种 */
	private TextView currencyTv = null;
	/** 还款金额 */
	private TextView repayAmountValueTv = null;
	/** 手续费 */
	private TextView tranFeeTv;

	/** 头部返回按钮 */
	private Button back;

	private String transactionId;
	private Map<String, String> fromAccount;
	private Map<String, String> toAccount;
	private String amount;
	/** 手续费 */
	private String tranFee = null;
	/** 转出账户别名 */
	private TextView accOutNickNameTV = null;
	/** 转入账户别名 */
	private TextView accInNickNameTV = null;
	private String outNickName;
	private String inNickName;
	/** 403转出账户余额 */
	private LinearLayout tran_success_out_balance;
	private TextView tv_tran_success_out_balance;
	private String fromAccountType;
	private String fromAccountId;

	/** 币种 */
	private String currency = "";
	/** 钞汇标识 */
	private String cashRemitCode = "";
	/** 转账类型 */
	private TextView type_tv;

	private boolean crcdflag = true;

	/** 手续费试算 */
	/** 基准费用 */
	private LinearLayout ll_need;

	private String actCharge;
	// 拟收费用
	private String fakeCharge;
	/** 应收费用 */
	private TextView actChargeDispalyTv;
	private TextView actChargeTv;
	/** 拟收费用 */
	private TextView farkChargeDisplayTv;
	private TextView farkChargeTv;

	private int index = 0;
	/** 是否在抽奖活动的有效日期内 */
	private boolean weixinRaffleflag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.tran_my_trans));
		View view = mInflater.inflate(
				R.layout.tran_relation_self_credit_card_success_activity, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);

		back = (Button) findViewById(R.id.ib_back);
		back.setVisibility(View.INVISIBLE);
		mTopRightBtn.setText(this.getString(R.string.go_main));
		mTopRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (weixinRaffleflag && index == 0) {
					index++;
					BaseDroidApp.getInstanse().showMsgDialogOneBtn(
							Weixin.getInstance().getWXRaffleCue(1, transactionId), "关闭",
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									BaseDroidApp.getInstanse()
											.dismissMessageDialog();
								}
							});
				} else {
//					ActivityTaskManager.getInstance().removeAllActivity();
//					Intent intent = new Intent();
//					intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//							MainActivity.class);
//					BaseDroidApp.getInstanse().getCurrentAct()
//							.startActivity(intent);
					goToMainActivity();
				}
			}
		});

		setupView(); // 初始化布局控件

		initData();

		displayTextView();
		// P403 请求账户余额
		if (fromAccountType.equals(ConstantGloble.GREATWALL)
				|| fromAccountType.equals(ConstantGloble.ZHONGYIN)
				|| fromAccountType.equals(ConstantGloble.SINGLEWAIBI)) {
			// 信用卡
			requestPsnCrcdOutDetail(fromAccountId, currency);
		} else {
			requestAccBankOutDetail(fromAccountId);
		}
	}

	/**
	 * 显示数据
	 */
	@SuppressWarnings("unchecked")
	private void initData() {
		Map<String, Object> relCrcdRepayCallBackMap = TranDataCenter
				.getInstance().getRelCrcdRepayCallBackMap();
		transactionId = (String) relCrcdRepayCallBackMap
				.get(Tran.RELTRANS_TRANSACTIONID_RES);
		fromAccount = (Map<String, String>) relCrcdRepayCallBackMap
				.get(Tran.CREDITCARD_SELF_FROMACCOUNT_RES);
		toAccount = (Map<String, String>) relCrcdRepayCallBackMap
				.get(Tran.CREDITCARD_SELF_TOACCOUNT_RES);
		amount = (String) relCrcdRepayCallBackMap
				.get(Tran.CREDITCARD_SELF_AMOUNT_RES);
		tranFee = (String) relCrcdRepayCallBackMap.get(Tran.TRAN_FEE);
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		outNickName = (String) accOutInfoMap.get(Comm.NICKNAME);
		fromAccountId = (String) accOutInfoMap.get(Acc.ACC_ACCOUNTID_RES);
		fromAccountType = (String) accOutInfoMap.get(Acc.ACC_ACCOUNTTYPE_RES);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		inNickName = (String) accInInfoMap.get(Comm.NICKNAME);
		if (StringUtil.isNullOrEmpty(inNickName)) {
			inNickName = (String) accInInfoMap.get(Crcd.CRCD_ACCTNAME_RES);
		}
		if (StringUtil.isNullOrEmpty(getIntent().getStringExtra(
				BocInvt.BOCINVT_CANCEL_CURRENCY_REQ))) {
			currency = ConstantGloble.PRMS_CODE_RMB;
		} else {
			currency = getIntent().getStringExtra(
					BocInvt.BOCINVT_CANCEL_CURRENCY_REQ);
		}

		if (StringUtil.isNullOrEmpty(getIntent().getStringExtra(
				Tran.ACCOUNTDETAIL_CASHREMIT_RES))) {
			cashRemitCode = "00";

		} else {
			cashRemitCode = getIntent().getStringExtra(
					Tran.ACCOUNTDETAIL_CASHREMIT_RES);
		}
		crcdflag = getIntent().getBooleanExtra("crcdflag", true);

		/** 手续费试算 */
		Map<String, Object> chargeMissionMap = TranDataCenter.getInstance()
				.getCommissionChargeMap();
		if (!StringUtil.isNullOrEmpty(chargeMissionMap)) {
			TextView tv_toast = (TextView) findViewById(R.id.tv_toast);
			tv_toast.setVisibility(View.GONE);
			String remitSetMealFlag = (String) chargeMissionMap
					.get(Tran.REMITSETMEAL_FLAG);
			if (!StringUtil.isNull(remitSetMealFlag)) {
				if (remitSetMealFlag
						.equals(ConstantGloble.ACC_CURRENTINDEX_VALUE)) {
					tv_toast.setVisibility(View.VISIBLE);
					tv_toast.setText(getString(R.string.tran_remitSetMealFlag_success));
				}
			}
			String getChargeFlag = (String) chargeMissionMap
					.get(Tran.GETCHARGE_FLAG);
			if (ConstantGloble.ACC_CURRENTINDEX_VALUE.equals(getChargeFlag)
					&& !chargeMissionMap
							.containsKey(Tran.NEED_COMMISSION_CHARGE)) {

			} else {
				ll_need.setVisibility(View.VISIBLE);
			}
			// 应收费用
			actCharge = (String) chargeMissionMap
					.get(Tran.NEED_COMMISSION_CHARGE);
			if (!StringUtil.isNullOrEmpty(actCharge)) {
				actCharge = StringUtil.parseStringPattern(actCharge, 2);
				// 转出是借记卡转入是信用卡 基准费用为0 不显示基准费用

				String fromAccountType = (String) accOutInfoMap
						.get(Tran.RELTRANS_FROMACCOUNTTYPE_RES);
				if (StringUtil.isNullOrEmpty(fromAccountType)) {
					fromAccountType = (String) accOutInfoMap
							.get(Acc.ACC_ACCOUNTTYPE_RES);
				}
				String toAccountType = (String) accInInfoMap
						.get(Tran.RELTRANS_TOACCOUNTTYPE_RES);
				if (StringUtil.isNullOrEmpty(toAccountType)) {
					toAccountType = (String) accInInfoMap
							.get(Acc.ACC_ACCOUNTTYPE_RES);
				}
				if (ConstantGloble.ACC_TYPE_BRO.equals(fromAccountType)
				// &&LocalData.mycardList.contains(toAccountType) 转入一定是信用卡
						&& Double.parseDouble(actCharge) == 0) {
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
		actChargeTv.setText(actCharge);
		farkChargeTv.setText(fakeCharge);
	}

	private void displayTextView() {
		transactionTv.setText(transactionId);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				transactionTv);
		String fromAccountNo = fromAccount.get(Comm.ACCOUNTNUMBER);
		accOutTv.setText(StringUtil.getForSixForString(fromAccountNo));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accOutTv);
		String toAccoutNo = toAccount.get(Comm.ACCOUNTNUMBER);
		if (StringUtil.isNullOrEmpty(inNickName)) {
			toAccoutNo = (String) TranDataCenter.getInstance()
					.getAccInInfoMap().get(Crcd.CRCD_ACCTNUM);
		}
		accInTv.setText(StringUtil.getForSixForString(toAccoutNo));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accInTv);
		repayAmountValueTv.setText(StringUtil.parseStringPattern(amount, 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				repayAmountValueTv);
		tranFeeTv.setText(tranFee);
		PopupWindowUtils.getInstance()
				.setOnShowAllTextListener(this, tranFeeTv);
		accOutNickNameTV.setText(outNickName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accOutNickNameTV);
		accInNickNameTV.setText(inNickName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accInNickNameTV);

		if ((ConstantGloble.PRMS_CODE_RMB).equals(currency)) {
			currencyTv.setText(LocalData.Currency.get(currency));
			type_tv.setText(getResources().getString(
					R.string.tran_rmb_card_repay));// 人民币还款
		} else {
			String cashRemitTv = "";
			if (!StringUtil.isNull(cashRemitCode)
					&& !"00".equals(cashRemitCode)) {
				cashRemitTv = LocalData.CurrencyCashremit.get(cashRemitCode);
			}
			currencyTv.setText(LocalData.Currency.get(currency) + cashRemitTv);
			type_tv.setText(getResources().getString(
					R.string.tran_wb_card_repay));// 外币还款
		}

		// if(crcdflag){
		// type_tv.setText(getResources().getString(R.string.acc_credit_card_payment));//信用卡还款
		// type_tv.setText("还款");//还款
		// }else{
		// type_tv.setText(getResources().getString(R.string.rela_acc_tran));
		// }
	}

	/**
	 * 初始化加载XML里的控件
	 */
	private void setupView() {
		StepTitleUtils.getInstance().initTitldStep(this,
				this.getResources().getStringArray(R.array.tran_repay));
		StepTitleUtils.getInstance().setTitleStep(3);

		ll_need = (LinearLayout) findViewById(R.id.ll_need);
		actChargeTv = (TextView) findViewById(R.id.tran_act_charge_tv);
		actChargeDispalyTv = (TextView) findViewById(R.id.tran_act_charge_display_tv);
		farkChargeTv = (TextView) findViewById(R.id.tran_fack_charge_tv);
		farkChargeDisplayTv = (TextView) findViewById(R.id.tran_fack_charge_display_tv);

		mConfirmBtn = (Button) findViewById(R.id.btn_confirm_rel_creditCard_success);

		accInTv = (TextView) findViewById(R.id.tv_accIn_rel_creditCard_success);
		accOutTv = (TextView) findViewById(R.id.tv_accOut_rel_creditCard_success);
		tranFeeTv = (TextView) findViewById(R.id.tv_commissionCharge_rel_creditCard_success);
		currencyTv = (TextView) findViewById(R.id.tv_currency_rel_creditCard_success);
		type_tv = (TextView) findViewById(R.id.tran_acc_type_tv);
		repayAmountValueTv = (TextView) findViewById(R.id.tv_repayAmountValue_rel_creditCard_success);
		transactionTv = (TextView) findViewById(R.id.tv_transaction_rel_creditCard_success);
		mConfirmBtn.setOnClickListener(listener);
		accOutNickNameTV = (TextView) findViewById(R.id.tv_accOut_relSelf_creditCard_nick_name);
		accInNickNameTV = (TextView) findViewById(R.id.tv_accIn_relSelf_creditCard_nick_name);
		tran_success_out_balance = (LinearLayout) findViewById(R.id.tran_success_out_balance);
		tv_tran_success_out_balance = (TextView) findViewById(R.id.tv_tran_success_out_balance);

	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			if(weixinRaffleflag&&index == 0){
				index++;
				BaseDroidApp.getInstanse().showMsgDialogOneBtn(Weixin.getInstance().getWXRaffleCue(1, transactionId),"关闭",
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								BaseDroidApp.getInstanse()
										.dismissMessageDialog();
							}
						});
				return;
			}
				Intent intent = new Intent();
				int moduleFlag = TranDataCenter.getInstance().getModuleType();
				switch (moduleFlag) {
				case ConstantGloble.ACC_MANAGE_TYPE:// 账户管理模块
					// 用户重新发起新转账，关闭所有活动的Activity，除了MainActivity,销毁数据
					ActivityTaskManager.getInstance().removeAllActivity();
					intent.setClass(RelSelfCreditCardSuccessActivity1.this,
							AccManageActivity.class);
					startActivity(intent);
					finish();
					break;
				case ConstantGloble.ACC_MANAGE_ELEC_TYPE:// 账户管理模块 电子现金账户
					// 用户重新发起新转账，关闭所有活动的Activity，除了MainActivity,销毁数据
					ActivityTaskManager.getInstance().removeAllActivity();
					intent.setClass(RelSelfCreditCardSuccessActivity1.this,
							MyFinanceIcAccountActivity.class);
					startActivity(intent);
					finish();
					break;
				case ConstantGloble.CRCE_TYPE:// 信用卡模块
					
						// 用户重新发起新转账，关闭所有活动的Activity，除了MainActivity,销毁数据
						ActivityTaskManager.getInstance().removeAllActivity();
						intent.setClass(RelSelfCreditCardSuccessActivity1.this,
								MyCreditCardActivity.class);
						startActivity(intent);
						finish();
					
					break;
				case ConstantGloble.CRCE_TYPE1:// 信用卡模块
					
						// 用户重新发起新转账，关闭所有活动的Activity，除了MainActivity,销毁数据
						ActivityTaskManager.getInstance().removeAllActivity();
						intent.setClass(RelSelfCreditCardSuccessActivity1.this,
								MyCardPaymentActivity.class);
						startActivity(intent);
						finish();
					
					break;
				default:
					
						// 用户重新发起新转账，关闭所有活动的Activity，除了MainActivity,销毁数据
						ActivityTaskManager.getInstance().removeAllActivity();
						intent.setClass(RelSelfCreditCardSuccessActivity1.this,
								TransferManagerActivity1.class);
						startActivity(intent);
						finish();
					
					break;
				}	
			}
	};

	/**
	 * 屏蔽返回键
	 */
	@Override
	public void onBackPressed() {
	}

	@Override
	public void requestAccBankOutDetailCallback(Object resultObj) {
		super.requestAccBankOutDetailCallback(resultObj);
		refreshTranOutBalance(false, tran_success_out_balance,
				tv_tran_success_out_balance, accountDetailList, currency,
				cashRemitCode);


		double currentAmount = Double.parseDouble(amount);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		String toAccountType = (String) accInInfoMap
				.get(Tran.RELTRANS_TOACCOUNTTYPE_RES);
		if (StringUtil.isNullOrEmpty(toAccountType)) {
			toAccountType = (String) accInInfoMap.get(Acc.ACC_ACCOUNTTYPE_RES);
		}
//		if (currentAmount >= Tran.WEIXIN_RAFFLE_AMOUNT) {
			Weixin.getInstance().doweixin(tabcontent,
					RelSelfCreditCardSuccessActivity1.this,
					new WeixinSuccess() {

						public void SuccessCallBack(boolean param) {
							// TODO Auto-generated method stub
							weixinRaffleflag = param;

						}
					}, transactionId/*
									 * ,btnRightlistener,mConfirmBtnlistener
									 */
					,"C",currentAmount);
//		}
	}

	@Override
	public void requestPsnCrcdOutDetailCallBack(Object resultObj) {
		super.requestPsnCrcdOutDetailCallBack(resultObj);
		refreshTranOutBalance(true, tran_success_out_balance,
				tv_tran_success_out_balance, detailList, currency,
				cashRemitCode);

		
		double currentAmount = Double.parseDouble(amount);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		String toAccountType = (String) accInInfoMap
				.get(Tran.RELTRANS_TOACCOUNTTYPE_RES);
		if (StringUtil.isNullOrEmpty(toAccountType)) {
			toAccountType = (String) accInInfoMap.get(Acc.ACC_ACCOUNTTYPE_RES);
		}
			Weixin.getInstance().doweixin(tabcontent,
					RelSelfCreditCardSuccessActivity1.this,
					new WeixinSuccess() {

						public void SuccessCallBack(boolean param) {
							// TODO Auto-generated method stub
							weixinRaffleflag = param;

						}
					}, transactionId/*
									 * ,btnRightlistener,mConfirmBtnlistener
									 */
					,"C",currentAmount);
		
	}

	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
			if (Acc.QRY_ACC_BALANCE_API.equals(biiResponseBody.getMethod())
					|| Crcd.CRCD_ACCOUNTDETAIL_API.equals(biiResponseBody
							.getMethod())) {// 余额显示
				if (biiResponse.isBiiexception()) {// 代表返回数据异常
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null) {
						if (biiError.getCode() != null) {
							if (LocalData.timeOutCode.contains(biiError
									.getCode())) {// 表示回话超时 要重新登录
								showTimeOutDialog(biiError.getMessage());
							} else {

							}
						}
					}
					return true;
				}
				return false;// 没有异常
			}
		}
		return super.httpRequestCallBackPre(resultObj);
	}

	@Override
	public void commonHttpErrorCallBack(String requestMethod) {
		if (Acc.QRY_ACC_BALANCE_API.equals(requestMethod)
				|| Crcd.CRCD_ACCOUNTDETAIL_API.equals(requestMethod)) {

		} else {
			super.commonHttpErrorCallBack(requestMethod);
		}
	}
}
