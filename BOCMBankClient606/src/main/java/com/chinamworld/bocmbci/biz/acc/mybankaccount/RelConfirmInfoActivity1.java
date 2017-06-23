package com.chinamworld.bocmbci.biz.acc.mybankaccount;

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

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.GetPhoneInfo;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 关联账户转账确认页面
 * 
 * @author wangmengmeng
 * 
 */
public class RelConfirmInfoActivity1 extends AccBaseActivity implements
		OnClickListener {

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
	private TextView currencyTv = null;
	/** 钞汇标志 */
	private TextView cashRemitTv = null;
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

	private LinearLayout cashRemitLinearLayout;

	/** 应收费用 */
	private TextView actChargeDispalyTv;
	private TextView actChargeTv;
	/** 拟收费用 */
	private TextView farkChargeDisplayTv;
	private TextView farkChargeTv;

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
	/** 转入账户昵称 */
	private TextView accinNickNameTv;

	// /**转账类型*/
	// private TextView tranTypeTv;
	// 转出账户
	private String fromAccountId = "";
	private String toAccountId = "";
	private String payeeActno = "";
	private String payeeName = "";
	private String currencySer = "";
	private String cashRemitSer = "";
	private String relTransRemark = "";
	private String relTransAmount = "";
	private String tokenId = "";
	// 转账日期 //立即执行：该字段为空，BII自动取为当前系统日期
	// 预约日期执行：为预约日期
	// 预约周期执行：为起始日期
	private String dueDate = "";

	private String actCharge;
	// 拟收费用
	private String fakeCharge;
	/**基准费用*/
	private LinearLayout ll_need; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.rela_acc_tran));
		View view = addView(R.layout.tran_confirm_info_rel_activity);
		setupView();
		initData();
	}

	/**
	 * 显示数据
	 */
	private void initData() {
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		fromAccountId = (String) accOutInfoMap.get(Tran.ACC_ACCOUNTID_REQ);
		// 转出账户
		String fromAccountNumber = (String) accOutInfoMap
				.get(Tran.ACCOUNTNUMBER_RES);
		// 转出账户昵称
		String outNickName = (String) accOutInfoMap.get(Tran.NICKNAME_RES);
		// 转账账户地区
		String fromAccountArea = (String) accOutInfoMap
				.get(Tran.ACCOUNTIBKNUM_RES);
		// 转出账户开户行
		String fromAccountBankName = (String) accOutInfoMap
				.get(Tran.BRANCHNAME_RES);

		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		toAccountId = (String) accInInfoMap.get(Tran.ACC_ACCOUNTID_REQ);
		// 转入账户
		payeeActno = (String) accInInfoMap.get(Tran.ACCOUNTNUMBER_RES);
		// 账户名称
		payeeName = (String) accInInfoMap.get(Tran.ACCOUNTNAME_RES);
		// 转入账户昵称
		String inNickName = (String) accInInfoMap.get(Tran.NICKNAME_RES);
		// 转入账户地区
		String toAccountArea = (String) accInInfoMap
				.get(Tran.ACCOUNTIBKNUM_RES);
		// 转瑞账户开户行
		String toAccountBankName = (String) accInInfoMap
				.get(Tran.BRANCHNAME_RES);

		Map<String, Object> chargeMissionMap = TranDataCenter.getInstance()
				.getCommissionChargeMap();

		Map<String, String> userInputMap = TranDataCenter.getInstance()
				.getUserInputMap();
		if (!StringUtil.isNullOrEmpty(userInputMap)) {
			currencySer = userInputMap.get(Tran.INPUT_CURRENCY_CODE);
			cashRemitSer = userInputMap.get(Tran.INPUT_CASHREMIT_CODE);
			relTransRemark = userInputMap.get(Tran.INPUT_TRANSFER_REMARK);
			relTransAmount = userInputMap.get(Tran.INPUT_TRANSFER_AMOUNT);
			// 执行方式
			relExecuteType = userInputMap.get(Tran.MANAGE_PRE_transMode_RES);
		}
		if (!StringUtil.isNullOrEmpty(chargeMissionMap)) {
			TextView tv_toast = (TextView) findViewById(R.id.tv_toast);
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
					tv_toast.setText(getString(R.string.tran_remitSetMealFlag));
				}
			}
			// 应收费用
			actCharge = (String) chargeMissionMap
					.get(Tran.NEED_COMMISSION_CHARGE);
			if (!StringUtil.isNullOrEmpty(actCharge)) {
				actCharge = StringUtil.parseStringCodePattern(currencySer,
						actCharge, 2);
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
				fakeCharge = StringUtil.parseStringCodePattern(currencySer,
						fakeCharge, 2);
				
			}
		}
		if ((ConstantGloble.PRMS_CODE_RMB).equals(currencySer)) {
			currencyTv.setText(LocalData.Currency.get(currencySer));
			cashRemitLinearLayout.setVisibility(View.GONE);
		} else {
			currencyTv.setText(LocalData.Currency.get(currencySer));
			// 币种不为人民币时，显示钞汇标志
			cashRemitTv.setText(LocalData.CurrencyCashremit.get(cashRemitSer));
		}
		accoutNicknameTv.setText(outNickName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accoutNicknameTv);
		accinNickNameTv.setText(inNickName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accinNickNameTv);
		transAmountTv.setText(StringUtil.parseStringCodePattern(currencySer,
				relTransAmount, 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				transAmountTv);
		remarkTv.setText(StringUtil.isNullChange(relTransRemark));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, remarkTv);
		accInTv.setText(StringUtil.getForSixForString(payeeActno));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accInTv);
		accInAreaTv.setText(LocalData.Province.get(toAccountArea));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accInAreaTv);
		inBranchName.setText(toAccountBankName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				inBranchName);
		accOutTv.setText(StringUtil.getForSixForString(fromAccountNumber));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accOutTv);
		accountIbkNumTv.setText(LocalData.Province.get(fromAccountArea));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accountIbkNumTv);
		outBranchName.setText(fromAccountBankName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				outBranchName);

		exeTypeTv.setText(LocalData.TransModeDisplay.get(relExecuteType));

		actChargeTv.setText(actCharge);
		farkChargeTv.setText(fakeCharge);

		switch (Integer.parseInt(relExecuteType)) {
		case 0:
			break;
		// 预约日期执行
		case 1:
			preDateLL.setVisibility(View.VISIBLE);
			prePeriodLl.setVisibility(View.GONE);

			// 预约日期执行 执行日期
			preDateExecuteDate = (String) userInputMap.get(Tran.INPUT_PRE_DATE);
			dueDate = preDateExecuteDate;
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

			dueDate = strStartDate;
			startDateTv.setText(strStartDate);
			endDateTv.setText(strEndDate);
			cycleDateTv.setText(LocalData.Frequency.get(strWeek));
			executeTimesTv.setText(strExecuteTimes);
			break;
		}

	}

	/**
	 * 初始化加载XML里的控件
	 */
	private void setupView() {
		ll_need= (LinearLayout) findViewById(R.id.ll_need);
		
		StepTitleUtils.getInstance().initTitldStep(this,
				this.getResources().getStringArray(R.array.tran_my_trans));
		StepTitleUtils.getInstance().setTitleStep(2);
		LinearLayout ll_tran_type = (LinearLayout) findViewById(R.id.ll_tran_type);
		ll_tran_type.setVisibility(View.GONE);
		mLastBtn = (Button) findViewById(R.id.btn_last_trans_rel_confirm);
		mNextBtn = (Button) findViewById(R.id.btn_next_trans_rel_confirm);

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
		actChargeTv = (TextView) findViewById(R.id.tran_act_charge_tv);
		actChargeDispalyTv = (TextView) findViewById(R.id.tran_act_charge_display_tv);
		farkChargeTv = (TextView) findViewById(R.id.tran_fack_charge_tv);
		farkChargeDisplayTv = (TextView) findViewById(R.id.tran_fack_charge_display_tv);

		exeDateTv = (TextView) findViewById(R.id.tv_exeDate_info_rel_confirm);
		startDateTv = (TextView) findViewById(R.id.tv_startDate_info_rel_confirm);
		endDateTv = (TextView) findViewById(R.id.tv_endDate_info_rel_confirm);
		cycleDateTv = (TextView) findViewById(R.id.tv_cycleDate_info_rel_confirm);
		executeTimesTv = (TextView) findViewById(R.id.tv_execute_times_info_rel_confirm);

		accInAreaTv = (TextView) findViewById(R.id.tv_acc_in_area_rel_confirm);
		inBranchName = (TextView) findViewById(R.id.tv_branch_name_in_rel_confirm);
		outBranchName = (TextView) findViewById(R.id.tv_branch_name_rel_confirm);
		// tranTypeTv = (TextView) findViewById(R.id.tran_acc_type_tv);

		mLastBtn.setOnClickListener(this);
		mNextBtn.setOnClickListener(this);
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_last_trans_rel_confirm:// 用户点击上一步
			finish();
			break;
		case R.id.btn_next_trans_rel_confirm:// 用户点击下一步
			// 显示通讯框
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
			// requestForRelTransConversationId(relTransAmount, relTransRemark,
			// currencySer, cashRemitSer, preDateExecuteDate,
			// strStartDate, strEndDate, strWeek);
			break;
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));

	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		tokenId = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		requestForLinkTransferSubmit();
	}

	/**
	 * 关联账户转账
	 */
	private void requestForLinkTransferSubmit() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.TRANSLINKTRANSFERSUBMIT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.RELTRANS_FROMACCOUNTID_REQ, fromAccountId);
		map.put(Tran.RELTRANS_TOACCOUNTID_REQ, toAccountId);
		map.put(Tran.RELTRANS_PAYEEACTNO_REQ, payeeActno);
		map.put(Tran.RELTRANS_PAYEENAME_REQ, payeeName);
		map.put(Tran.RELTRANS_CURRENCY_REQ, currencySer);
		map.put(Tran.RELTRANS_AMOUNT_REQ, relTransAmount);
		map.put(Tran.RELTRANS_CASHREMIT_REQ, cashRemitSer);
		map.put(Tran.RELTRANS_REMARK_REQ, relTransRemark);
		map.put(Tran.RELTRANS_EXECUTETYPE_REQ, relExecuteType);
		map.put(Tran.RELTRANS_DUEDATE_REQ, dueDate);
		map.put(Tran.RELTRANS_EXECUTEDATE_REQ, preDateExecuteDate);
		map.put(Tran.RELTRANS_STARTDATE_REQ, strStartDate);
		map.put(Tran.RELTRANS_ENDDATE_REQ, strEndDate);
		map.put(Tran.RELTRANS_CYCLESELECT_REQ, strWeek);
		map.put(Tran.RELTRANS_TOKEN_REQ, tokenId);
		GetPhoneInfo.addPhoneInfo(map);// 反欺诈 设备指纹
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForLinkTransferSubmitCallBack");
	}

	/**
	 * 关联账户转账返回
	 * 
	 * @param resultObj
	 */
	public void requestForLinkTransferSubmitCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setRelTranCallBackMap(result);

		Intent intent = new Intent();
		intent.putExtra(Tran.MANAGE_PRE_transMode_RES, relExecuteType);
		intent.putExtra(Tran.ACC_ACCOUNTID_REQ, fromAccountId);
		intent.setClass(RelConfirmInfoActivity1.this,
				RelTranSuccessActivity1.class);
		startActivity(intent);
	}

}
