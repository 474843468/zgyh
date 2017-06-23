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
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Tran;
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
 * 关联信用卡购汇 成功
 * 
 * @author
 * 
 */
public class RelCreditCardRemitSuccessActivity1 extends TranBaseActivity {

	/** 上一步 */
	private Button mLastBtn = null;
	/** 下一步 */
	private Button mConfirmBtn = null;
	/** 还款金额 */
	private TextView repayAmountTv = null;
	/** 信用卡外币消费账户 */
	private TextView crcdAccTv = null;
	/** 汇率 */
	private TextView exchangeRateTv = null;
	/** 人民币还款账户 */
	private TextView rmbRepayAccTv = null;
	/** 购汇币种 */
	private TextView remitCurrencyTv = null;
	/** 购汇还款金额设定 */
	private TextView remitAmountSetingTv = null;
	/** 转入账户账号 */
	private TextView accInNoTv;
	/** 转出账户账号 */
	private TextView accOutNoTv;
	/** 头部返回按钮 */
	private Button back;
	/** 交易序列号 */
	private TextView transactionTv;

	private String transactionId;
	// private String accOutNickname;
	private String accOutNo;
	private String accInNickname;
	private String accInNo;
	/** 购汇币种 */
	private String currency;
	/** 汇率 */
	private String exchangeRate;
	/** 购汇还款金额设定 */
	private String repayAmountSet;
	/** 还款金额 */
	private String repayAmount;

	/** 转出账户信息 */
	private Map<String, Object> rmbAcct;
	private String outAccountName;
	/** 转入账户信息 */
	private Map<String, Object> crcdAcct;
	/** 403转出账户余额 */
	private LinearLayout tran_success_out_balance;
	private TextView tv_tran_success_out_balance;
	private String fromAccountType;
	private String fromAccountId;

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
		View view = mInflater
				.inflate(
						R.layout.tran_relation_credit_card_remit_success_activity,
						null);
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
			requestPsnCrcdOutDetail(fromAccountId, ConstantGloble.PRMS_CODE_RMB);
		} else {
			requestAccBankOutDetail(fromAccountId);
		}
	}

	@SuppressWarnings("unchecked")
	private void initData() {
		Map<String, Object> relCrcdDealMap = TranDataCenter.getInstance()
				.getRelCrcdBuyDealCallBackMap();
		fromAccountType = getIntent().getStringExtra(Acc.ACC_ACCOUNTTYPE_RES);
		fromAccountId = getIntent().getStringExtra(Tran.ACC_ACCOUNTID_REQ);
		transactionId = (String) relCrcdDealMap
				.get(Tran.CREDITCARD_TRANSACTIONID_RES);
		rmbAcct = (Map<String, Object>) relCrcdDealMap
				.get(Tran.CREDITCARD_RMBACCT_RES);
		// accOutNickname = (String) rmbAcct.get(Comm.NICKNAME);
		accOutNo = (String) rmbAcct.get(Comm.ACCOUNTNUMBER);
		outAccountName = (String) rmbAcct.get(Comm.ACCOUNT_NAME);
		crcdAcct = (Map<String, Object>) relCrcdDealMap
				.get(Tran.CREDITCARD_CRCDACCT_RES);
		accInNickname = (String) crcdAcct.get(Comm.NICKNAME);
		accInNo = (String) crcdAcct.get(Comm.ACCOUNTNUMBER);
		Map<String, String> userInputMap = TranDataCenter.getInstance()
				.getUserInputMap();
		currency = userInputMap.get(Tran.INPUT_CURRENCY_CODE);
		repayAmountSet = userInputMap
				.get(Tran.CREDITCARD_FOREIGN_CRCDAUTOREPAYMODE_REQ);

		exchangeRate = (String) relCrcdDealMap
				.get(Tran.CREDITCARD_EXCHANGERATE_RES);
		repayAmount = (String) relCrcdDealMap
				.get(Tran.CREDITCARD_FOREIGN_PAYAMT_RES);

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

				// 转出是借记卡 转入是信用卡 基准费用为0 不显示基准费用
				String fromAccountType = (String) rmbAcct
						.get(Tran.RELTRANS_FROMACCOUNTTYPE_RES);
				if (StringUtil.isNullOrEmpty(fromAccountType)) {
					fromAccountType = (String) rmbAcct
							.get(Acc.ACC_ACCOUNTTYPE_RES);
				}
				String toAccountType = (String) crcdAcct
						.get(Tran.RELTRANS_TOACCOUNTTYPE_RES);
				if (StringUtil.isNullOrEmpty(toAccountType)) {
					toAccountType = (String) crcdAcct
							.get(Acc.ACC_ACCOUNTTYPE_RES);
				}
				if (ConstantGloble.ACC_TYPE_BRO.equals(fromAccountType)
				// &&LocalData.mycardList.contains(toAccountType)// 转入一定是信用卡
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

		transactionTv = (TextView) findViewById(R.id.tv_transaction_creditCard_remit_success);
		crcdAccTv = (TextView) findViewById(R.id.tv_accOut_creditCard_remit_confirm);
		accInNoTv = (TextView) findViewById(R.id.tran_in_no_tv);
		accOutNoTv = (TextView) findViewById(R.id.tran_out_no_tv);
		rmbRepayAccTv = (TextView) findViewById(R.id.tv_accIn_creditCard_remit_confirm);
		remitAmountSetingTv = (TextView) findViewById(R.id.tv_amout_set_creditCard_remit_confirm);
		exchangeRateTv = (TextView) findViewById(R.id.tv_exchangeRate_creditCard_remit_confirm);
		remitCurrencyTv = (TextView) findViewById(R.id.tv_credmit_currency_success);

		repayAmountTv = (TextView) findViewById(R.id.tv_repay_amount_creditCard_remit_success);
		tran_success_out_balance = (LinearLayout) findViewById(R.id.tran_success_out_balance);
		tv_tran_success_out_balance = (TextView) findViewById(R.id.tv_tran_success_out_balance);
		mLastBtn = (Button) findViewById(R.id.btn_last_rel_creditCard_confirm);
		mConfirmBtn = (Button) findViewById(R.id.btn_confirm_creditCard_remit_success);

		mLastBtn.setOnClickListener(this);
		mConfirmBtn.setOnClickListener(listener);

	}

	// 屏蔽返回键
	@Override
	public void onBackPressed() {

	}

	private void displayTextView() {
		transactionTv.setText(transactionId);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				transactionTv);
		crcdAccTv.setText(accInNickname);
		PopupWindowUtils.getInstance()
				.setOnShowAllTextListener(this, crcdAccTv);
		accInNoTv.setText(StringUtil.getForSixForString(accInNo));
		PopupWindowUtils.getInstance()
				.setOnShowAllTextListener(this, accInNoTv);
		rmbRepayAccTv.setText(outAccountName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				rmbRepayAccTv);
		accOutNoTv.setText(StringUtil.getForSixForString(accOutNo));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accOutNoTv);
		remitCurrencyTv.setText(LocalData.Currency.get(currency));
		exchangeRateTv.setText(exchangeRate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				exchangeRateTv);
		if (repayAmountSet.equals(ConstantGloble.REPAY_ALL)) {
			remitAmountSetingTv.setText(LocalData.paymentModeType
					.get(ConstantGloble.REPAY_ALL));
		} else if (repayAmountSet.equals(ConstantGloble.REPAY_PART)) {
			remitAmountSetingTv.setText(LocalData.paymentModeType
					.get(ConstantGloble.REPAY_PART));
		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				remitAmountSetingTv);
		repayAmountTv.setText(StringUtil.parseStringPattern(repayAmount, 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				repayAmountTv);
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
					intent.setClass(RelCreditCardRemitSuccessActivity1.this,
							AccManageActivity.class);
					startActivity(intent);
					finish();
					break;
				case ConstantGloble.CRCE_TYPE:// 信用卡模块

					
						// 用户重新发起新转账，关闭所有活动的Activity，除了MainActivity,销毁数据
						ActivityTaskManager.getInstance().removeAllActivity();
						intent.setClass(RelCreditCardRemitSuccessActivity1.this,
								MyCreditCardActivity.class);
						startActivity(intent);
						finish();
					
					break;
				case ConstantGloble.CRCE_TYPE1:// 信用卡模块
					
						// 用户重新发起新转账，关闭所有活动的Activity，除了MainActivity,销毁数据
						ActivityTaskManager.getInstance().removeAllActivity();
						intent.setClass(RelCreditCardRemitSuccessActivity1.this,
								MyCardPaymentActivity.class);
						startActivity(intent);
						finish();
					
					break;
				default:
					ActivityTaskManager.getInstance().removeAllActivity();
						intent.setClass(RelCreditCardRemitSuccessActivity1.this,
								TransferManagerActivity1.class);
						startActivity(intent);
						finish();
					
					break;
				}
			}
	};

	@Override
	public void requestAccBankOutDetailCallback(Object resultObj) {
		super.requestAccBankOutDetailCallback(resultObj);
		refreshTranOutBalance(false, tran_success_out_balance,
				tv_tran_success_out_balance, accountDetailList,
				ConstantGloble.PRMS_CODE_RMB, null);

		double currentAmount = Double.parseDouble(repayAmount);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		String toAccountType = (String) accInInfoMap
				.get(Tran.RELTRANS_TOACCOUNTTYPE_RES);
		if (StringUtil.isNullOrEmpty(toAccountType)) {
			toAccountType = (String) accInInfoMap.get(Acc.ACC_ACCOUNTTYPE_RES);
		}
//		if (currentAmount >= Tran.WEIXIN_RAFFLE_AMOUNT) {
			Weixin.getInstance().doweixin(tabcontent,
					RelCreditCardRemitSuccessActivity1.this,
					new WeixinSuccess() {

						public void SuccessCallBack(boolean param) {
							// TODO Auto-generated method stub
							weixinRaffleflag = param;

						}
					}, transactionId,"C",currentAmount);
//		}

	}

	@Override
	public void requestPsnCrcdOutDetailCallBack(Object resultObj) {
		super.requestPsnCrcdOutDetailCallBack(resultObj);
		refreshTranOutBalance(true, tran_success_out_balance,
				tv_tran_success_out_balance, detailList,
				ConstantGloble.PRMS_CODE_RMB, null);

		double currentAmount = Double.parseDouble(repayAmount);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		String toAccountType = (String) accInInfoMap
				.get(Tran.RELTRANS_TOACCOUNTTYPE_RES);
		if (StringUtil.isNullOrEmpty(toAccountType)) {
			toAccountType = (String) accInInfoMap.get(Acc.ACC_ACCOUNTTYPE_RES);
		}
//		if (currentAmount >= Tran.WEIXIN_RAFFLE_AMOUNT) {
			Weixin.getInstance().doweixin(tabcontent,
					RelCreditCardRemitSuccessActivity1.this,
					new WeixinSuccess() {

						public void SuccessCallBack(boolean param) {
							// TODO Auto-generated method stub
							weixinRaffleflag = param;

						}
					}, transactionId,"C",currentAmount);
//		}
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
