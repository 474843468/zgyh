package com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran;

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
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.acc.mybankaccount.AccManageActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.MyCreditCardActivity;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
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
 * 关联账户转账成功
 * 
 * @author
 * 
 */
public class RelTranSuccessActivity1 extends TranBaseActivity {

	/** 确定按钮 */
	private Button mConfirmBtn = null;
	/** 标题 */
	private TextView titleTv = null;
	/** 交易序号 */
	private TextView transactionTv = null;
	/** 转账批次号 */
	private TextView batSeqTv;
	/** 转出账户 */
	private TextView accOutTv = null;
	/** 转出账户地区 */
	private TextView accountIbkNumTv = null;
	/** 转入账户 */
	private TextView accInTv = null;
	/** 币种 */
	private TextView currencyTv = null;
	/** 钞汇标志 */
	private TextView cashRemitTv = null;
	/** 转账金额 */
	private TextView transAmountTv = null;
	/** 应收费用 */
	// private TextView commissionChargeTv = null;
	/** 实收费用 */
	// private TextView dianHuiTv = null;
	/** 转账类型 */
	// private TextView transTypeTv = null;
	/** 执行方式 */
	private TextView exeTypeTv = null;
	/** 附言 */
	private TextView remarkTv = null;

	private String relExecuteType = null;

	private LinearLayout preDateLL, prePeriodLl;

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
	/** 转出账户昵称 */
	private TextView accoutNicknameTv;
	/** 转入账户昵称 */
	private TextView accinNickNameTv;
	private LinearLayout cashRemitLinearLayout;
	/** 应收费用 */
	private TextView actChargeDispalyTv;
	private TextView actChargeTv;
	/** 拟收费用 */
	private TextView farkChargeDisplayTv;
	private TextView farkChargeTv;
	/** 头部返回按钮 */
	private Button back;

	/** 交易序列号 */
	private LinearLayout transactionLayout;
	/** 转账批次号 */
	private LinearLayout batSeqLayout;

	/** 周期 */
	private String strWeek = null;
	/** 起始日期 */
	private String strStartDate = null;
	/** 结束日期 */
	private String strEndDate = null;
	/** 执行次数 */
	// private String strExecuteTimes = null;
	/** 预约日期执行 执行日期 */
	private String preDateExecuteDate = null;

	private String currency;
	private String transactionId;
	private String amount;
	private String batSeq;
	private String commissionCharge;
	private String furInfo;
	// private String postage;
	private String cashRemit;
	// private String fromAccountType;
	private String fromAccountNumber;
	private String fromAccountNickName;
	private String fromAccountIbknum;
	// private String toAccountType;
	private String toAccountNumber;
	private String toAccountNickname;
	private String toAccountIbknum;
	// private String needCount;
	/** 应收费用 */
	private String shouldCharge;
	private String shouldCharge2;
	/** 转入账户开户行 */
	private String toAccountBankName;
	/** 转出账户开户行 */
	private String fromAccountBankName;
	/** 执行次数 */
	private String needCount;

	/** 403转出账户余额 */
	private LinearLayout tran_success_out_balance;
	private TextView tv_tran_success_out_balance;
	private String fromAccountType;
	private String fromAccountId;

	/** 基准费用 */
	private LinearLayout ll_need;

	/* OnClickListener btnRightlistener,mConfirmBtnlistener; */

	private int index = 0;
	/** 是否在抽奖活动的有效日期内 */
	private boolean weixinRaffleflag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.tran_my_trans));
		View view = mInflater.inflate(R.layout.tran_confirm_info_rel_activity,
				null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		back = (Button) findViewById(R.id.ib_back);
		back.setVisibility(View.INVISIBLE);
		mTopRightBtn.setText(this.getString(R.string.go_main));
		mTopRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(weixinRaffleflag&&index == 0){
					index++;
					BaseDroidApp.getInstanse().showMsgDialogOneBtn(	Weixin.getInstance().getWXRaffleCue(1, transactionId),"关闭",
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									BaseDroidApp.getInstanse()
											.dismissMessageDialog();
								}
							});
				}else{
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
		initData();
		Map<String, Object> relTranCallBackMap = TranDataCenter.getInstance()
				.getRelTranCallBackMap();
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		String toAccountType = (String) accInInfoMap
				.get(Tran.RELTRANS_TOACCOUNTTYPE_RES);
		if (StringUtil.isNullOrEmpty(toAccountType)) {
			toAccountType = (String) accInInfoMap.get(Acc.ACC_ACCOUNTTYPE_RES);
		}
		/** 转账金额 */
		amount = (String) relTranCallBackMap.get(Tran.RELTRANS_AMOUNT_RES);
		double currentAmount = Double.parseDouble(amount);
		if (LocalData.mycardList.contains(toAccountType)
				/*&& currentAmount >= Tran.WEIXIN_RAFFLE_AMOUNT*/ 
				&&"0".equals(relExecuteType)) {
			Weixin.getInstance()
					.doweixin(tabcontent, RelTranSuccessActivity1.this,
							new WeixinSuccess() {

								public void SuccessCallBack(boolean param) {
									// TODO Auto-generated method stub
									weixinRaffleflag = param;
									setupView(); // 初始化布局控件
									displayTextView();
								}
							}, transactionId,"C",currentAmount);
		}else{
		setupView(); // 初始化布局控件
		displayTextView();
		}

	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		ll_need = (LinearLayout) findViewById(R.id.ll_need);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		Map<String, Object> relTranCallBackMap = TranDataCenter.getInstance()
				.getRelTranCallBackMap();
		fromAccountType = (String) relTranCallBackMap
				.get(Tran.RELTRANS_FROMACCOUNTTYPE_RES);
		fromAccountId = getIntent().getStringExtra(Tran.ACC_ACCOUNTID_REQ);
		fromAccountNumber = (String) relTranCallBackMap
				.get(Tran.RELTRANS_FROMACCOUNTNUMBER_RES);
		fromAccountNickName = (String) relTranCallBackMap
				.get(Tran.RELTRANS_FROMACCOUNTNICKNAME_RES);
		fromAccountIbknum = (String) relTranCallBackMap
				.get(Tran.RELTRANS_FROMACCOUNTIBKNUM_RES);
		toAccountNumber = (String) relTranCallBackMap
				.get(Tran.RELTRANS_TOACCOUNTNUMBER_RES);
		toAccountNickname = (String) relTranCallBackMap
				.get(Tran.RELTRANS_TOACCOUNTNICKNAME_RES);
		toAccountIbknum = (String) relTranCallBackMap
				.get(Tran.RELTRANS_TOACCOUNTIBKNUM_RES);
		currency = (String) relTranCallBackMap.get(Tran.RELTRANS_CURRENCY_RES);
		transactionId = (String) relTranCallBackMap
				.get(Tran.RELTRANS_TRANSACTIONID_RES);
		amount = (String) relTranCallBackMap.get(Tran.RELTRANS_AMOUNT_RES);
		batSeq = (String) relTranCallBackMap.get(Tran.RELTRANS_BATSEQ_RES);
		commissionCharge = (String) relTranCallBackMap
				.get(Tran.FINAL_COMMISSIONCHARGE);
		furInfo = (String) relTranCallBackMap.get(Tran.RELTRANS_FURINFO_RES);
		cashRemit = (String) relTranCallBackMap
				.get(Tran.RELTRANS_CASHREMIT_RES);
		needCount = (String) relTranCallBackMap
				.get(Tran.RELTRANS_NEEDCOUNT_RES);
		if (!StringUtil.isNullOrEmpty(commissionCharge)) {
			commissionCharge = StringUtil.parseStringCodePattern(currency,
					commissionCharge, 2);

		}
		Map<String, Object> chargeMissionMap = TranDataCenter.getInstance()
				.getCommissionChargeMap();
		// 应收费用
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
			shouldCharge = (String) chargeMissionMap
					.get(Tran.NEED_COMMISSION_CHARGE);
			if (!StringUtil.isNullOrEmpty(shouldCharge)) {
				shouldCharge = StringUtil.parseStringCodePattern(currency,
						shouldCharge, 2);
				// 转出不是信用卡 转入是信用卡 基准费用为0 不显示基准费用
				// if(!LocalData.mycardList.contains(fromAccountType)
				// &&LocalData.mycardList.contains((String)
				// accInInfoMap.get(Acc.ACC_ACCOUNTTYPE_RES))
				// &&Double.parseDouble(shouldCharge)==0){
				// ll_need.setVisibility(View.GONE);
				// }

				// 转出是借记卡 转入是信用卡 基准费用为0 不显示基准费用
				String fromAccountType = (String) relTranCallBackMap
						.get(Tran.RELTRANS_FROMACCOUNTTYPE_RES);
				if (StringUtil.isNullOrEmpty(fromAccountType)) {
					fromAccountType = (String) relTranCallBackMap
							.get(Acc.ACC_ACCOUNTTYPE_RES);
				}
				String toAccountType = (String) accInInfoMap
						.get(Tran.RELTRANS_TOACCOUNTTYPE_RES);
				if (StringUtil.isNullOrEmpty(toAccountType)) {
					toAccountType = (String) accInInfoMap
							.get(Acc.ACC_ACCOUNTTYPE_RES);
				}
				if (ConstantGloble.ACC_TYPE_BRO.equals(fromAccountType)
						&& LocalData.mycardList.contains(toAccountType)
						&& Double.parseDouble(shouldCharge) == 0) {
					ll_need.setVisibility(View.GONE);
				}
			}
			shouldCharge2 = (String) chargeMissionMap
					.get(Tran.PRECOMMISSION_CHARGE);
			if (!StringUtil.isNullOrEmpty(shouldCharge2)) {
				shouldCharge2 = StringUtil.parseStringCodePattern(currency,
						shouldCharge2, 2);

			}
		}

		// 转瑞账户开户行
		toAccountBankName = (String) accInInfoMap.get(Tran.BRANCHNAME_RES);

		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		fromAccountBankName = (String) accOutInfoMap.get(Tran.BRANCHNAME_RES);

		Map<String, String> userInputMap = TranDataCenter.getInstance()
				.getUserInputMap();
		if (!StringUtil.isNullOrEmpty(userInputMap)) {
			// 预约日期执行 执行日期
			preDateExecuteDate = (String) userInputMap.get(Tran.INPUT_PRE_DATE);
			// 起始日期
			strStartDate = (String) userInputMap
					.get(Tran.INPUT_PRE_PERIOD_START_DATE);
			// 结束日期
			strEndDate = (String) userInputMap
					.get(Tran.INPUT_PRE_PERIOD_END_DATE);
			// // 执行次数
			// strExecuteTimes = (String) userInputMap
			// .get(Tran.INPUT_PRE_PERIOD_EXECUTE_TIMES);
			/** 执行周期 */
			strWeek = (String) userInputMap.get(Tran.INPUT_PRE_PERIOD_WEEK);
			// 执行方式
			relExecuteType = userInputMap.get(Tran.MANAGE_PRE_transMode_RES);
		}

			
		

	}

	/**
	 * 初始化加载XML里的控件
	 */
	private void setupView() {

	
		StepTitleUtils.getInstance().initTitldStep(this,
				this.getResources().getStringArray(R.array.tran_my_trans));
		StepTitleUtils.getInstance().setTitleStep(3);

		transactionLayout = (LinearLayout) findViewById(R.id.tran_transaction_layout);
		transactionTv = (TextView) findViewById(R.id.tran_transaction_tv);
		batSeqLayout = (LinearLayout) findViewById(R.id.tran_batseq_layout);
		batSeqTv = (TextView) findViewById(R.id.tran_batseq_tv);
		titleTv = (TextView) findViewById(R.id.tv_content);

		accInTv = (TextView) findViewById(R.id.tv_acc_in_rel_confirm);
		accountIbkNumTv = (TextView) findViewById(R.id.tv_acc_out_area_rel_confirm);
		accOutTv = (TextView) findViewById(R.id.tv_acc_out_rel_confirm);
		currencyTv = (TextView) findViewById(R.id.tv_tran_currency_rel_confirm);
		transAmountTv = (TextView) findViewById(R.id.tv_transferAmount_rel_confirm);
		exeTypeTv = (TextView) findViewById(R.id.tv_executeType_rel_confirm);
		remarkTv = (TextView) findViewById(R.id.tv_remark_info_rel_confirm);
		cashRemitTv = (TextView) findViewById(R.id.tv_tran_cashRemit_rel_confirm);

		preDateLL = (LinearLayout) findViewById(R.id.ll_preDate_confirm_info);
		prePeriodLl = (LinearLayout) findViewById(R.id.ll_prePeriod_confirm_info);
		cashRemitLinearLayout = (LinearLayout) findViewById(R.id.dept_cashremit_layout);

		accoutNicknameTv = (TextView) findViewById(R.id.tran_out_nickname_tv);
		accinNickNameTv = (TextView) findViewById(R.id.tran_in_nickname_tv);
		actChargeDispalyTv = (TextView) findViewById(R.id.tran_act_charge_display_tv);
		actChargeTv = (TextView) findViewById(R.id.tran_act_charge_tv);
		farkChargeDisplayTv = (TextView) findViewById(R.id.tran_fack_charge_display_tv);
		farkChargeTv = (TextView) findViewById(R.id.tran_fack_charge_tv);

		exeDateTv = (TextView) findViewById(R.id.tv_exeDate_info_rel_confirm);
		startDateTv = (TextView) findViewById(R.id.tv_startDate_info_rel_confirm);
		endDateTv = (TextView) findViewById(R.id.tv_endDate_info_rel_confirm);
		cycleDateTv = (TextView) findViewById(R.id.tv_cycleDate_info_rel_confirm);
		executeTimesTv = (TextView) findViewById(R.id.tv_execute_times_info_rel_confirm);

		accInAreaTv = (TextView) findViewById(R.id.tv_acc_in_area_rel_confirm);
		inBranchName = (TextView) findViewById(R.id.tv_branch_name_in_rel_confirm);
		outBranchName = (TextView) findViewById(R.id.tv_branch_name_rel_confirm);

		tran_success_out_balance = (LinearLayout) findViewById(R.id.tran_success_out_balance);
		tv_tran_success_out_balance = (TextView) findViewById(R.id.tv_tran_success_out_balance);
		// TextView tv_toast = (TextView) findViewById(R.id.tv_toast);
		// tv_toast.setVisibility(View.GONE);
		mConfirmBtn = (Button) findViewById(R.id.btn_next_trans_rel_confirm);
		mConfirmBtn.setText(this.getString(R.string.finish));
		mConfirmBtn.setOnClickListener(listener);
	}

	private void displayTextView() {

		transactionTv.setText(transactionId);
		batSeqTv.setText(batSeq);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				transactionTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, batSeqTv);
		if ((ConstantGloble.PRMS_CODE_RMB).equals(currency)) {
			currencyTv.setText(LocalData.Currency.get(currency));
			cashRemitLinearLayout.setVisibility(View.GONE);
		} else {
			currencyTv.setText(LocalData.Currency.get(currency));
			// 币种不为人民币时，显示钞汇标志
			if (!StringUtil.isNull(cashRemit) && !"00".equals(cashRemit)) {
				cashRemitTv.setText(LocalData.CurrencyCashremit.get(cashRemit));
			}
		}
		accoutNicknameTv.setText(fromAccountNickName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accoutNicknameTv);
		accinNickNameTv.setText(toAccountNickname);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accinNickNameTv);
		transAmountTv.setText(StringUtil.parseStringCodePattern(currency,
				amount, 2));
		remarkTv.setText(StringUtil.isNullChange(furInfo));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, remarkTv);

		accInTv.setText(StringUtil.getForSixForString(toAccountNumber));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accInTv);
		accInAreaTv.setText(LocalData.Province.get(toAccountIbknum));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accInAreaTv);
		inBranchName.setText(toAccountBankName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				inBranchName);

		accOutTv.setText(StringUtil.getForSixForString(fromAccountNumber));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accOutTv);
		accountIbkNumTv.setText(LocalData.Province.get(fromAccountIbknum));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accountIbkNumTv);
		outBranchName.setText(fromAccountBankName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				outBranchName);
		exeTypeTv.setText(LocalData.TransModeDisplay.get(relExecuteType));
		// actChargeTv.setText(StringUtil.isNull(shouldCharge) ? "-"
		// : shouldCharge);
		actChargeTv.setText(shouldCharge);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				actChargeTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				farkChargeTv);
		switch (Integer.parseInt(relExecuteType)) {
		case 0:
			titleTv.setText(this.getResources().getString(
					R.string.mobile_tran_have_acc_success_title));
			// 显示实收费用
			// farkChargeDisplayTv.setText(this.getResources().getString(
			// R.string.trans_fact_charge));
			transactionLayout.setVisibility(View.VISIBLE);
			// farkChargeTv.setText(StringUtil.isNull(commissionCharge) ? "-"
			// : commissionCharge);
			farkChargeTv.setText(commissionCharge);
			// P403 请求账户余额
			if (fromAccountType.equals(ConstantGloble.GREATWALL)
					|| fromAccountType.equals(ConstantGloble.ZHONGYIN)
					|| fromAccountType.equals(ConstantGloble.SINGLEWAIBI)) {
				// 信用卡
				requestPsnCrcdOutDetail(fromAccountId, currency);
			} else {
				requestAccBankOutDetail(fromAccountId);
			}
			break;
		// 预约日期执行
		case 1:
			titleTv.setText(this.getResources().getString(
					R.string.tran_success_title_date));
			// farkChargeDisplayTv.setText(this.getResources().getString(
			// R.string.trans_fact_charge));
			// farkChargeTv.setText(StringUtil.isNull(shouldCharge2) ? "-"
			// : shouldCharge2);
			farkChargeTv.setText(shouldCharge2);
			batSeqLayout.setVisibility(View.VISIBLE);
			preDateLL.setVisibility(View.VISIBLE);
			prePeriodLl.setVisibility(View.GONE);
			exeDateTv.setText(preDateExecuteDate);
			break;
		case 2:
			titleTv.setText(this.getResources().getString(
					R.string.tran_success_title_date));
			// 单笔应收 单笔拟收
			actChargeDispalyTv.setText(this.getResources().getString(
					R.string.trans_single_act_charge));
			farkChargeDisplayTv.setText(this.getResources().getString(
					R.string.trans_single_fack_charge));

			// farkChargeTv.setText(StringUtil.isNull(shouldCharge2) ? "-"
			// : shouldCharge2);
			farkChargeTv.setText(shouldCharge2);
			batSeqLayout.setVisibility(View.VISIBLE);
			prePeriodLl.setVisibility(View.VISIBLE);
			preDateLL.setVisibility(View.GONE);
			startDateTv.setText(strStartDate);
			endDateTv.setText(strEndDate);
			cycleDateTv.setText(LocalData.Frequency.get(strWeek));
			executeTimesTv.setText(needCount);
			break;
		}
	}

	/**
	 * 屏蔽返回键
	 */
	@Override
	public void onBackPressed() {
	}

	/**
	 * 点击事件
	 */
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
				intent.setClass(RelTranSuccessActivity1.this,
						AccManageActivity.class);
				startActivity(intent);
				finish();
				break;
			case ConstantGloble.CRCE_TYPE:// 信用卡模块
			
					// 用户重新发起新转账，关闭所有活动的Activity，除了MainActivity,销毁数据
					ActivityTaskManager.getInstance().removeAllActivity();
					intent.setClass(RelTranSuccessActivity1.this,
							MyCreditCardActivity.class);
					startActivity(intent);
					finish();
				
				break;
			default:
				
					// 用户重新发起新转账，关闭所有活动的Activity，除了MainActivity,销毁数据
					ActivityTaskManager.getInstance().removeAllActivity();
					intent.setClass(RelTranSuccessActivity1.this,
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
				tv_tran_success_out_balance, accountDetailList, currency,
				cashRemit);
	}

	@Override
	public void requestPsnCrcdOutDetailCallBack(Object resultObj) {
		super.requestPsnCrcdOutDetailCallBack(resultObj);
		refreshTranOutBalance(true, tran_success_out_balance,
				tv_tran_success_out_balance, detailList, currency, cashRemit);
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
