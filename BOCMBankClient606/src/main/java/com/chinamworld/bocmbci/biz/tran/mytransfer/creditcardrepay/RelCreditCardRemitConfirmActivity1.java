package com.chinamworld.bocmbci.biz.tran.mytransfer.creditcardrepay;

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
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
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
 * 关联信用卡购汇 确认
 * 
 * @author
 * 
 */
public class RelCreditCardRemitConfirmActivity1 extends TranBaseActivity
		implements OnClickListener {

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
	/** 当前账户余额 */
	private TextView currentBalanceTv = null;
	/** 上期结欠金额 */
	private TextView oweAmtTv = null;
	/** 未还款金额 */
	private TextView repayedAmountTv = null;
	/** 最小还款金额 */
	private TextView mixAmtTv = null;
	/** 到期还款日 */
	private TextView expenDateTv = null;
	/** 将扣减人民币金额 */
	// private TextView rmbAmountTv = null;

	/** 转入账户账号 */
	private TextView accInNoTv;
	/** 转出账户账号 */
	private TextView accOutNoTv;

	private String accOutNickname;
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
	/** 当前账户余额 */
	private String ballanceAmt;
	/** 上期结欠金额 */
	private String oweAmt;
	/** 未还款金额 */
	private String payedAmt;
	/** 最小还款金额 */
	private String mixAmt;
	/** 到期还款日 */
	private String expenDate;

	private String accInId;
	private String accInAccountName;
	private String accOutId;
	/** 当前账户余额（人民币元）标识位 */
	private String ballanceAmtFlag;
	/** 上期结欠金额（人民币元）标识位 */
	private String oweAmtFlag;

	/** 转出账户类型 */
	private String tranOutType;

	/**手续费试算*/
	/**基准费用*/
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.tran_my_trans));
		View view = mInflater
				.inflate(
						R.layout.tran_relation_credit_card_remit_confirm_activity,
						null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		setupView(); // 初始化布局控件
		initData();
		displayTextView();
	}

	private void initData() {
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		accOutNickname = (String) accOutInfoMap.get(Comm.NICKNAME);
		accOutNo = (String) accOutInfoMap.get(Comm.ACCOUNTNUMBER);
		accOutId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
		tranOutType = (String) accOutInfoMap.get(Acc.ACC_ACCOUNTTYPE_RES);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		accInNickname = (String) accInInfoMap.get(Comm.NICKNAME);
		if(StringUtil.isNullOrEmpty(accInNickname)){
			accInNickname = (String) accInInfoMap.get(Crcd.CRCD_PRODUCTNAME);	
		}
		accInNo = (String) accInInfoMap.get(Comm.ACCOUNTNUMBER);
		if(StringUtil.isNullOrEmpty(accInNo)){
			accInNo = (String) accInInfoMap.get(Crcd.CRCD_ACCTNUM);	
		}
		accInId = (String) accInInfoMap.get(Comm.ACCOUNT_ID);
		accInAccountName = (String) accInInfoMap.get(Comm.ACCOUNT_NAME);
		if(StringUtil.isNullOrEmpty(accInAccountName)){
			accInAccountName = (String) accInInfoMap.get(Crcd.CRCD_ACCTNAME_RES);	
		}
		
		
		Map<String, String> userInputMap = TranDataCenter.getInstance()
				.getUserInputMap();
		currency = userInputMap.get(Tran.INPUT_CURRENCY_CODE);
		repayAmount = userInputMap.get(Tran.INPUT_TRANSFER_AMOUNT);
		repayAmountSet = userInputMap
				.get(Tran.CREDITCARD_FOREIGN_CRCDAUTOREPAYMODE_REQ);
		Map<String, Object> relCrcdBuyMap = TranDataCenter.getInstance()
				.getRelCrcdBuyCallBackMap();
		ballanceAmt = (String) relCrcdBuyMap
				.get(Tran.CREDITCARD_BALLANCEAMT_RES);
		if(StringUtil.isNullOrEmpty(ballanceAmt)){
			ballanceAmt = (String) relCrcdBuyMap.get(Crcd.CRCD_FOR_CURRENTBALANCE);	
		}
		
		oweAmt = (String) relCrcdBuyMap.get(Tran.CREDITCARD_OWEAMT_RES);
		if(StringUtil.isNullOrEmpty(oweAmt)){
			oweAmt = (String) relCrcdBuyMap.get(Crcd.CRCD_FOR_PRIOROWEAMT);	
		}
		ballanceAmtFlag = (String) relCrcdBuyMap
				.get(Tran.CREDITCARD_BALLANCEAMTFLAG_RES);
		if(StringUtil.isNullOrEmpty(ballanceAmtFlag)){
			ballanceAmtFlag = (String) relCrcdBuyMap.get(Crcd.CRCD_FOR_BALANCEFLAG);	
		}
		oweAmtFlag = (String) relCrcdBuyMap.get(Tran.CREDITCARD_OWEAMTFLAG_RES);
		if(StringUtil.isNullOrEmpty(oweAmtFlag)){
			oweAmtFlag = (String) relCrcdBuyMap.get(Crcd.CRCD_FOR_OWEFLAH);	
		}
		
		payedAmt = (String) relCrcdBuyMap.get(Tran.CREDITCARD_PAYEDAMT_RES);
		
		
		mixAmt = (String) relCrcdBuyMap.get(Tran.CREDITCARD_MIXAMT_RES);
		if(StringUtil.isNullOrEmpty(mixAmt)){
			mixAmt = (String) relCrcdBuyMap.get(Crcd.CRCD_FOR_MIMPAYAMT);	
		}
		
		expenDate = (String) relCrcdBuyMap.get(Tran.CREDITCARD_EXPENDATE_RES);
		if(StringUtil.isNullOrEmpty(expenDate)){
			expenDate = (String) relCrcdBuyMap.get(Crcd.CRCD_FOR_DUEDATE);	
		}

		exchangeRate = (String) relCrcdBuyMap
				.get(Tran.CREDITCARD_EXCHANGERATE_RES);
		
		
		
		/**手续费试算*/
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
				actCharge = StringUtil.parseStringPattern(
						actCharge, 2);
				// 转出是借记卡转入是信用卡 基准费用为0 不显示基准费用
				String fromAccountType=(String) accOutInfoMap.get(Tran.RELTRANS_FROMACCOUNTTYPE_RES);
				if(StringUtil.isNullOrEmpty(fromAccountType)){
					fromAccountType=(String) accOutInfoMap.get(Acc.ACC_ACCOUNTTYPE_RES);
				}
//				String toAccountType=(String) accInInfoMap.get(Tran.RELTRANS_TOACCOUNTTYPE_RES);
//				if(StringUtil.isNullOrEmpty(toAccountType)){
//					toAccountType=(String) accInInfoMap.get(Acc.ACC_ACCOUNTTYPE_RES);
//				}
					if(ConstantGloble.ACC_TYPE_BRO.equals(fromAccountType)
//						&&LocalData.mycardList.contains(toAccountType)// 转入一定是信用卡
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
		actChargeTv.setText(actCharge);
		farkChargeTv.setText(fakeCharge);
	}

	/**
	 * 初始化加载XML里的控件
	 */
	private void setupView() {
		StepTitleUtils.getInstance().initTitldStep(this,
				this.getResources().getStringArray(R.array.tran_repay));
		StepTitleUtils.getInstance().setTitleStep(2);

		ll_need= (LinearLayout) findViewById(R.id.ll_need);
		actChargeTv = (TextView) findViewById(R.id.tran_act_charge_tv);
		actChargeDispalyTv = (TextView) findViewById(R.id.tran_act_charge_display_tv);
		farkChargeTv = (TextView) findViewById(R.id.tran_fack_charge_tv);
		farkChargeDisplayTv = (TextView) findViewById(R.id.tran_fack_charge_display_tv);
		
		crcdAccTv = (TextView) findViewById(R.id.tv_accOut_creditCard_remit_confirm);
		accInNoTv = (TextView) findViewById(R.id.tran_in_no_tv);
		accOutNoTv = (TextView) findViewById(R.id.tran_out_no_tv);
		rmbRepayAccTv = (TextView) findViewById(R.id.tv_accIn_creditCard_remit_confirm);
		remitAmountSetingTv = (TextView) findViewById(R.id.tv_amout_set_creditCard_remit_confirm);
		exchangeRateTv = (TextView) findViewById(R.id.tv_exchangeRate_creditCard_remit_confirm);
		remitCurrencyTv = (TextView) findViewById(R.id.tv_credmit_currency_success);

		repayAmountTv = (TextView) findViewById(R.id.tv_repay_amount_creditCard_remit_success);
		currentBalanceTv = (TextView) findViewById(R.id.tv_current_balance_creditCard_remit_confirm);
		oweAmtTv = (TextView) findViewById(R.id.tv_oweAmt_creditCard_remit_confirm);
		repayedAmountTv = (TextView) findViewById(R.id.tv_payedAmt_creditCard_remit_confirm);
		mixAmtTv = (TextView) findViewById(R.id.tv_mixAmt_creditCard_remit_confirm);
		expenDateTv = (TextView) findViewById(R.id.tv_expenDate_creditCard_remit_confirm);
		// 前面字段显示需弹窗
		TextView current_balance_pre = (TextView) findViewById(R.id.current_balance_pre);
		TextView oweAmt_pre = (TextView) findViewById(R.id.oweAmt_pre);
		TextView mixAmt_pre = (TextView) findViewById(R.id.mixAmt_pre);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				current_balance_pre);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				oweAmt_pre);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				mixAmt_pre);
		mLastBtn = (Button) findViewById(R.id.btn_last_rel_creditCard_confirm);
		mConfirmBtn = (Button) findViewById(R.id.btn_confirm_rel_creditCard_confirm);

		mLastBtn.setOnClickListener(this);
		mConfirmBtn.setOnClickListener(this);
	}

	/**
	 * 显示视图
	 */
	private void displayTextView() {
		TextView tv_one = (TextView) findViewById(R.id.tv_add_one);
		TextView tv_two = (TextView) findViewById(R.id.tv_add_two);
		crcdAccTv.setText(accInNickname);
		PopupWindowUtils.getInstance()
				.setOnShowAllTextListener(this, crcdAccTv);
		accInNoTv.setText(StringUtil.getForSixForString(accInNo));
		PopupWindowUtils.getInstance()
				.setOnShowAllTextListener(this, accInNoTv);
		rmbRepayAccTv.setText(accOutNickname);
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
		repayAmountTv.setText(StringUtil.parseStringPattern(repayAmount, 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				repayAmountTv);
		if (StringUtil.isNull(ballanceAmtFlag)) {
			currentBalanceTv.setText(StringUtil.parseStringPattern(ballanceAmt,
					2));
		} else {
			if (ballanceAmtFlag.equals(ConstantGloble.ACC_CURRENTINDEX_VALUE)) {
				currentBalanceTv.setText(StringUtil.parseStringPattern(
						ballanceAmt, 2));
				tv_one.setText(this.getString(R.string.left));
			} else if (ballanceAmtFlag
					.equals(ConstantGloble.LOAN_CURRENTINDEX_VALUE)) {
				currentBalanceTv.setText(StringUtil.parseStringPattern(
						ballanceAmt, 2));
				tv_one.setText(this.getString(R.string.own));
			} else {
				currentBalanceTv.setText(StringUtil.parseStringPattern(
						ballanceAmt, 2));
			}
		}
		// currentBalanceTv.setText(StringUtil.parseStringPattern(ballanceAmt,
		// 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				currentBalanceTv);
		if (StringUtil.isNull(oweAmtFlag)) {
			oweAmtTv.setText(StringUtil.parseStringPattern(oweAmt, 2));
		} else {
			if (oweAmtFlag.equals(ConstantGloble.ACC_CURRENTINDEX_VALUE)) {
				oweAmtTv.setText(StringUtil.parseStringPattern(oweAmt, 2));
				tv_two.setText(this.getString(R.string.left));
			} else if (oweAmtFlag
					.equals(ConstantGloble.LOAN_CURRENTINDEX_VALUE)) {
				oweAmtTv.setText(StringUtil.parseStringPattern(oweAmt, 2));
				tv_two.setText(this.getString(R.string.own));
			} else {
				oweAmtTv.setText(StringUtil.parseStringPattern(oweAmt, 2));
			}
		}
		// oweAmtTv.setText(StringUtil.parseStringPattern(oweAmt, 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, oweAmtTv);
		repayedAmountTv.setText(StringUtil.parseStringPattern(payedAmt, 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				repayedAmountTv);
		mixAmtTv.setText(StringUtil.parseStringPattern(mixAmt, 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mixAmtTv);
		expenDateTv.setText(expenDate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				expenDateTv);
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_last_rel_creditCard_confirm:// 上一步
			finish();
			break;
		case R.id.btn_confirm_rel_creditCard_confirm:// 下一步
			requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
					.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
			BaseHttpEngine.showProgressDialog();
			break;
		}

	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String tokenId = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		requestForCrcdForeignPayOffResult(tokenId);
	}

	/**
	 * 信用卡购汇还款处理PsnCrcdForeignPayOffResult req
	 */
	public void requestForCrcdForeignPayOffResult(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.CRCDFOREIGNPAYOFFRESULT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		// 转入账户信息 ID
		map.put(Tran.CREDITCARD_FOREIGN_CRCDID_REQ, accInId);
		// 信用卡持卡人姓名
		map.put(Tran.CREDITCARD_FOREIGN_CRCDACCTNAME_REQ, accInAccountName);
		// 信用卡卡号
		map.put(Tran.CREDITCARD_FOREIGN_CRCDACCTNO_REQ, accInNo);
		// 转出账户信息 ID
		map.put(Tran.CREDITCARD_FOREIGN_RMBACCID_REQ, accOutId);
		map.put(Tran.CREDITCARD_FOREIGN_CRCDAUTOREPAYMODE_REQ, repayAmountSet);
		map.put(Tran.CREDITCARD_FOREIGN_TOKEN_REQ, tokenId);
		map.put(Tran.CREDITCARD_FOREIGN_AMOUNT_REQ, repayAmount);
		GetPhoneInfo.addPhoneInfoMapString(map);// 反欺诈 设备指纹
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"crcdForeignPayOffResultCallBack");
	}

	/**
	 * 信用卡购汇还款处理res
	 */
	@SuppressWarnings("unchecked")
	public void crcdForeignPayOffResultCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setRelCrcdBuyDealCallBackMap(result);

		Intent intent = new Intent();
		intent.putExtra(Tran.ACC_ACCOUNTID_REQ, accOutId);
		intent.putExtra(Acc.ACC_ACCOUNTTYPE_RES, tranOutType);
		intent.setClass(RelCreditCardRemitConfirmActivity1.this,
				RelCreditCardRemitSuccessActivity1.class);
		startActivity(intent);
		finish();// 重复提交 销毁数据
	}

}
