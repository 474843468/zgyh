package com.chinamworld.bocmbci.biz.acc.mybankaccount.creditcardrepay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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
 * 本人关联信用卡还款 确认
 * 
 * @author WJP
 * 
 */
public class RelSelfCreditCardConfirmActivity1 extends AccBaseActivity implements OnClickListener {
	/** 下一步 */
	private Button mConfirmBtn = null;
	/** 转出账户 */
	private TextView accOutTv = null;
	/** 转入账户 */
	private TextView accInTv = null;
	/** 币种 */
	private TextView currencyTv = null;
	/** 还款金额 */
	private TextView repayAmountTv = null;
	/** 转出账户别名 */
	private TextView accOutNickNameTV = null;
	/** 转入账户别名 */
	private TextView accInNickNameTV = null;
	/** 转出账户标示 */
	private String fromAccountId = "";
	/** 转入账户标示 */
	private String toAccountId = "";
	/** 转入账户名 */
	private String toName = "";
	/** 币种 */
	private String currency = "";
	/** 钞汇标识 */
	private String cashRemit = "";

	// 显示数据
	private String outAccNo;
	private String outNickName;
	private String inAccNo;
	private String inNickName;
	/** 还款金额 */
	private String repayAmount = "";
	private String tokenId;
	/** 转账类型*/
	private TextView type_tv;
	
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
		View view = LayoutInflater.from(this).inflate(
				R.layout.tran_relation_self_credit_card_confirm_activity, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		
		// 其他模块如果要跳转此页面，需要在intent中put这两个字段
		currency = getIntent().getStringExtra(Acc.DETAIL_CURRENCYCODE_RES);
		cashRemit = getIntent().getStringExtra(ConstantGloble.TRAN_CASHREMIT);
		setupView(); // 初始化布局控件
		initData();
		displayTextView();
	}

	/**
	 * 显示数据
	 */
	private void initData() {
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance().getAccOutInfoMap();
		outAccNo = (String) accOutInfoMap.get(Comm.ACCOUNTNUMBER);
		outNickName = (String) accOutInfoMap.get(Comm.NICKNAME);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance().getAccInInfoMap();
		System.out.println("accOutInfoMap-->" + accOutInfoMap);
		System.out.println("accInInfoMap-->" + accInInfoMap);
		inAccNo = (String) accInInfoMap.get(Comm.ACCOUNTNUMBER);
		inNickName = (String) accInInfoMap.get(Comm.NICKNAME);
		Map<String, String> userInputMap = TranDataCenter.getInstance().getUserInputMap();
		repayAmount = userInputMap.get(Tran.INPUT_TRANSFER_AMOUNT);

		fromAccountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
		toName = (String) accOutInfoMap.get(Comm.ACCOUNT_NAME);
		toAccountId = (String) accInInfoMap.get(Comm.ACCOUNT_ID);
		
		

		/**手续费试算*/
		Map<String, Object> chargeMissionMap = TranDataCenter.getInstance()
				.getCommissionChargeMap();
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
				actCharge = StringUtil.parseStringPattern(
						actCharge, 2);
				String fromAccountType=(String) accOutInfoMap.get(Tran.RELTRANS_FROMACCOUNTTYPE_RES);
				if(StringUtil.isNullOrEmpty(fromAccountType)){
					fromAccountType=(String) accOutInfoMap.get(Acc.ACC_ACCOUNTTYPE_RES);
				}
//				String toAccountType=(String) accInInfoMap.get(Tran.RELTRANS_TOACCOUNTTYPE_RES);
//				if(StringUtil.isNullOrEmpty(toAccountType)){
//					toAccountType=(String) accInInfoMap.get(Acc.ACC_ACCOUNTTYPE_RES);
//				}
				// 转出是借记卡 转入是信用卡 基准费用为0 不显示基准费用
				if(ConstantGloble.ACC_TYPE_BRO.equals(fromAccountType)
//					&&LocalData.mycardList.contains(toAccountType)转入一定是信用卡
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

	private void displayTextView() {
		accOutTv.setText(StringUtil.getForSixForString(outAccNo));
		accOutNickNameTV.setText(outNickName);
		accInTv.setText(StringUtil.getForSixForString(inAccNo));
		accInNickNameTV.setText(inNickName);
		repayAmountTv.setText(StringUtil.parseStringPattern(repayAmount, 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accOutTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accOutNickNameTV);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accInTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accInNickNameTV);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, repayAmountTv);
	}

	/**
	 * 初始化加载XML里的控件
	 */
	private void setupView() {
		StepTitleUtils.getInstance().initTitldStep(this, this.getResources().getStringArray(R.array.tran_repay));
		StepTitleUtils.getInstance().setTitleStep(2);

		

		ll_need= (LinearLayout) findViewById(R.id.ll_need);
		actChargeTv = (TextView) findViewById(R.id.tran_act_charge_tv);
		actChargeDispalyTv = (TextView) findViewById(R.id.tran_act_charge_display_tv);
		farkChargeTv = (TextView) findViewById(R.id.tran_fack_charge_tv);
		farkChargeDisplayTv = (TextView) findViewById(R.id.tran_fack_charge_display_tv);
		
		
		mConfirmBtn = (Button) findViewById(R.id.btn_confirm_relSelf_creditCard_confirm);

		accOutTv = (TextView) findViewById(R.id.tv_accOut_relSelf_creditCard_confirm);
		accInTv = (TextView) findViewById(R.id.tv_accIn_relSelf_creditCard_confirm);
		currencyTv = (TextView) findViewById(R.id.tv_currency_relSelf_creditCard_confirm);
		repayAmountTv = (TextView) findViewById(R.id.tv_repayAmountValue_relSelf_creditCard_confirm);
		accOutNickNameTV = (TextView) findViewById(R.id.tv_accOut_relSelf_creditCard_nick_name);
		accInNickNameTV = (TextView) findViewById(R.id.tv_accIn_relSelf_creditCard_nick_name);
		 type_tv=(TextView)findViewById(R.id.tran_acc_type_tv); 
		((TextView) findViewById(R.id.tv_keyForcurrency_rel_creditCard_success)).setText(getResources().getString(R.string.tran_currency));
		
		
		if (currency.equals("001")) {
			currencyTv.setText("人民币元");
			type_tv.setText(getResources().getString(R.string.tran_rmb_card_repay));//人民币还款
		} else {
			String text = "";
			if (!StringUtil.isNull(cashRemit)&&cashRemit.equals("00")) {
				text = LocalData.Currency.get(currency);
			} else {
				text = LocalData.Currency.get(currency) + LocalData.CurrencyCashremitT.get(cashRemit);
			}
			currencyTv.setText(text);
			type_tv.setText(getResources().getString(R.string.tran_wb_card_repay));//外币还款
		}
		
		mConfirmBtn.setOnClickListener(this);
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm_relSelf_creditCard_confirm:// 下一步
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
			break;
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		tokenId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		requestForCrcdTransferPayOffResult();
	}

	/**
	 * 关联信用卡还款确认PsnCrcdTransferPayOffResult req
	 */
	public void requestForCrcdTransferPayOffResult() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.CRCDTRANSFERPAYOFFRESULT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.CREDITCARD_SELF_FROMACCOUNTID_REQ, fromAccountId);
		map.put(Tran.CREDITCARD_SELF_TOACCOUNTID_REQ, toAccountId);
		map.put(Tran.CREDITCARD_SELF_TOACCOUNT_REQ, inAccNo);
		map.put(Tran.ACCOUNTDETAIL_CASHREMIT_RES, cashRemit);
		map.put(Tran.CREDITCARD_SELF_TONAME_REQ, toName);
		map.put(Tran.CREDITCARD_SELF_AMOUNT_REQ, repayAmount);
		map.put(Tran.CREDITCARD_SELF_CURRENCY_REQ, currency);
		map.put(Tran.CREDITCARD_SELF_TOKEN_REQ, tokenId);
		GetPhoneInfo.addPhoneInfo(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "crcdTransferPayOffResultCallBack");
	}

	/**
	 * 关联信用卡还款确认res
	 */
	@SuppressWarnings("unchecked")
	public void crcdTransferPayOffResultCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		TranDataCenter.getInstance().setRelCrcdRepayCallBackMap(result);

		Intent intent = new Intent()
		.putExtra(ConstantGloble.TRAN_CASHREMIT, cashRemit)
		.putExtra(Acc.DETAIL_CURRENCYCODE_RES, currency);
		intent.setClass(RelSelfCreditCardConfirmActivity1.this, RelSelfCreditCardSuccessActivity1.class);
		startActivity(intent);
	}
}
