package com.chinamworld.bocmbci.biz.acc.mybankaccount;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 关联账户转账成功
 * 
 * @author wangmengmeng
 * 
 */
public class RelTranSuccessActivity1 extends AccBaseActivity {

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
	private String needCount = null;
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
	/** 403转出账户余额 */
	private LinearLayout tran_success_out_balance;
	private TextView tv_tran_success_out_balance;
	private String fromAccountType;
	private String fromAccountId;
	/**基准费用*/
	private LinearLayout ll_need; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.rela_acc_tran));
		View view = addView(R.layout.tran_confirm_info_rel_activity);
		back = (Button) findViewById(R.id.ib_back);
		back.setVisibility(View.INVISIBLE);
		setupView(); // 初始化布局控件
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {

		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
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
			String getChargeFlag = (String) chargeMissionMap
					.get(Tran.GETCHARGE_FLAG);
			if(ConstantGloble.ACC_CURRENTINDEX_VALUE.equals(getChargeFlag)
					&&!chargeMissionMap.containsKey(Tran.NEED_COMMISSION_CHARGE)){
				
			}else{
				ll_need.setVisibility(View.VISIBLE);	
			}
			if (!StringUtil.isNull(remitSetMealFlag)) {
				if (remitSetMealFlag
						.equals(ConstantGloble.ACC_CURRENTINDEX_VALUE)) {
					tv_toast.setVisibility(View.VISIBLE);
					tv_toast.setText(getString(R.string.tran_remitSetMealFlag_success));
				}
			}
			shouldCharge = (String) chargeMissionMap
					.get(Tran.NEED_COMMISSION_CHARGE);
			if (!StringUtil.isNullOrEmpty(shouldCharge)) {
				shouldCharge = StringUtil.parseStringCodePattern(currency,
						shouldCharge, 2);
				// 转出是借记卡 转入是信用卡 基准费用为0 不显示基准费用
				String toAccountType=(String) accInInfoMap.get(Tran.RELTRANS_TOACCOUNTTYPE_RES);
				if(StringUtil.isNullOrEmpty(toAccountType)){
					toAccountType=(String) accInInfoMap.get(Acc.ACC_ACCOUNTTYPE_RES);
				}
				if(ConstantGloble.ACC_TYPE_BRO.equals(fromAccountType)
					&&LocalData.mycardList.contains(toAccountType)
					&&Double.parseDouble(shouldCharge)==0){
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
			// 执行次数
			// strExecuteTimes = (String) userInputMap
			// .get(Tran.INPUT_PRE_PERIOD_EXECUTE_TIMES);
			/** 执行周期 */
			strWeek = (String) userInputMap.get(Tran.INPUT_PRE_PERIOD_WEEK);
			// 执行方式
			relExecuteType = userInputMap.get(Tran.MANAGE_PRE_transMode_RES);
		}

		displayTextView();
	}

	/**
	 * 初始化加载XML里的控件
	 */
	private void setupView() {
		ll_need= (LinearLayout) findViewById(R.id.ll_need);
		StepTitleUtils.getInstance().initTitldStep(this,
				this.getResources().getStringArray(R.array.tran_my_trans));
		StepTitleUtils.getInstance().setTitleStep(3);
		LinearLayout ll_tran_type = (LinearLayout) findViewById(R.id.ll_tran_type);
		ll_tran_type.setVisibility(View.GONE);
		transactionLayout = (LinearLayout) findViewById(R.id.tran_transaction_layout);
		transactionTv = (TextView) findViewById(R.id.tran_transaction_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				transactionTv);
		batSeqLayout = (LinearLayout) findViewById(R.id.tran_batseq_layout);
		batSeqTv = (TextView) findViewById(R.id.tran_batseq_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, batSeqTv);
		titleTv = (TextView) findViewById(R.id.tv_content);

		accInTv = (TextView) findViewById(R.id.tv_acc_in_rel_confirm);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accInTv);
		accountIbkNumTv = (TextView) findViewById(R.id.tv_acc_out_area_rel_confirm);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accountIbkNumTv);
		accOutTv = (TextView) findViewById(R.id.tv_acc_out_rel_confirm);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accOutTv);
		currencyTv = (TextView) findViewById(R.id.tv_tran_currency_rel_confirm);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				currencyTv);
		transAmountTv = (TextView) findViewById(R.id.tv_transferAmount_rel_confirm);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				transAmountTv);
		exeTypeTv = (TextView) findViewById(R.id.tv_executeType_rel_confirm);
		PopupWindowUtils.getInstance()
				.setOnShowAllTextListener(this, exeTypeTv);
		remarkTv = (TextView) findViewById(R.id.tv_remark_info_rel_confirm);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, remarkTv);
		cashRemitTv = (TextView) findViewById(R.id.tv_tran_cashRemit_rel_confirm);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				cashRemitTv);
		preDateLL = (LinearLayout) findViewById(R.id.ll_preDate_confirm_info);
		prePeriodLl = (LinearLayout) findViewById(R.id.ll_prePeriod_confirm_info);
		cashRemitLinearLayout = (LinearLayout) findViewById(R.id.dept_cashremit_layout);

		accoutNicknameTv = (TextView) findViewById(R.id.tran_out_nickname_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accoutNicknameTv);
		accinNickNameTv = (TextView) findViewById(R.id.tran_in_nickname_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accinNickNameTv);
		actChargeDispalyTv = (TextView) findViewById(R.id.tran_act_charge_display_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				actChargeDispalyTv);
		actChargeTv = (TextView) findViewById(R.id.tran_act_charge_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				actChargeTv);
		farkChargeDisplayTv = (TextView) findViewById(R.id.tran_fack_charge_display_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				farkChargeDisplayTv);
		farkChargeTv = (TextView) findViewById(R.id.tran_fack_charge_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				farkChargeTv);
		exeDateTv = (TextView) findViewById(R.id.tv_exeDate_info_rel_confirm);
		PopupWindowUtils.getInstance()
				.setOnShowAllTextListener(this, exeDateTv);
		startDateTv = (TextView) findViewById(R.id.tv_startDate_info_rel_confirm);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				startDateTv);
		endDateTv = (TextView) findViewById(R.id.tv_endDate_info_rel_confirm);
		PopupWindowUtils.getInstance()
				.setOnShowAllTextListener(this, endDateTv);
		cycleDateTv = (TextView) findViewById(R.id.tv_cycleDate_info_rel_confirm);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				cycleDateTv);
		executeTimesTv = (TextView) findViewById(R.id.tv_execute_times_info_rel_confirm);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				executeTimesTv);
		accInAreaTv = (TextView) findViewById(R.id.tv_acc_in_area_rel_confirm);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accInAreaTv);
		inBranchName = (TextView) findViewById(R.id.tv_branch_name_in_rel_confirm);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				inBranchName);
		outBranchName = (TextView) findViewById(R.id.tv_branch_name_rel_confirm);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				outBranchName);
//		TextView tv_toast = (TextView) findViewById(R.id.tv_toast);
//		tv_toast.setVisibility(View.GONE);
		tran_success_out_balance = (LinearLayout) findViewById(R.id.tran_success_out_balance);
		tv_tran_success_out_balance = (TextView) findViewById(R.id.tv_tran_success_out_balance);
		mConfirmBtn = (Button) findViewById(R.id.btn_next_trans_rel_confirm);
		mConfirmBtn.setText(this.getString(R.string.finish));
		mConfirmBtn.setOnClickListener(listener);
	}

	private void displayTextView() {

		transactionTv.setText(transactionId);
		batSeqTv.setText(batSeq);

		if ((ConstantGloble.PRMS_CODE_RMB).equals(currency)) {
			currencyTv.setText(LocalData.Currency.get(currency));
			cashRemitLinearLayout.setVisibility(View.GONE);
		} else {
			currencyTv.setText(LocalData.Currency.get(currency));
			// 币种不为人民币时，显示钞汇标志
			cashRemitTv.setText(LocalData.CurrencyCashremit.get(cashRemit));
		}
		accoutNicknameTv.setText(fromAccountNickName);
		accinNickNameTv.setText(toAccountNickname);
		transAmountTv.setText(StringUtil.parseStringCodePattern(currency,
				amount, 2));
		remarkTv.setText(StringUtil.isNullChange(furInfo));

		accInTv.setText(StringUtil.getForSixForString(toAccountNumber));
		accInAreaTv.setText(LocalData.Province.get(toAccountIbknum));
		inBranchName.setText(toAccountBankName);
		accOutTv.setText(StringUtil.getForSixForString(fromAccountNumber));
		accountIbkNumTv.setText(LocalData.Province.get(fromAccountIbknum));
		outBranchName.setText(fromAccountBankName);
		exeTypeTv.setText(LocalData.TransModeDisplay.get(relExecuteType));
		// actChargeTv.setText(StringUtil.isNull(shouldCharge) ? "-"
		// : shouldCharge);
		actChargeTv.setText(shouldCharge);
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
			ActivityTaskManager.getInstance().removeAllActivity();
			Intent intent = new Intent(RelTranSuccessActivity1.this,
					AccManageActivity.class);
			startActivity(intent);
			finish();
		}
	};

	@Override
	public void requestAccBankOutDetailCallback(Object resultObj) {
		super.requestAccBankOutDetailCallback(resultObj);
		refreshTranOutBalance(false, tran_success_out_balance,
				tv_tran_success_out_balance, tranOutaccountDetailList,
				currency, cashRemit);
	}

	@Override
	public void requestPsnCrcdOutDetailCallBack(Object resultObj) {
		super.requestPsnCrcdOutDetailCallBack(resultObj);
		refreshTranOutBalance(true, tran_success_out_balance,
				tv_tran_success_out_balance, tranOutdetailList, currency,
				cashRemit);
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
