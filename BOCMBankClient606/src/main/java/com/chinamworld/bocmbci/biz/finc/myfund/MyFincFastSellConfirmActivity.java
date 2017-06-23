package com.chinamworld.bocmbci.biz.finc.myfund;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * 我的基金--快速赎回确认页面
 * 
 * @author fsm
 * 
 */
public class MyFincFastSellConfirmActivity extends FincBaseActivity {
	public static final String TAG = "MyFincSellConfirmActivity";

	/** 上一步 */
//	private Button lastButton = null;
	/** 确定 */
	private Button sureButton = null;
	private String tokenId = null;
	/** 基金账户 */
	private String investAccount = null;
	/** 资金账户 */
	private String account = null;
	/** 资金账户别名 */

	private int flag;
	private static final int FINCSELL = 0;
	private static final int FINCNIGHTSELL = 1;
	
	
	
	/** 基金持仓主view */
	private View myFincView = null;
	/** 基金名称 */
	private TextView fincNameText = null;
	/** 基金公司 */
	private TextView finc_myfinc_follow_compnay = null;
	/** 收费方式 */
	private TextView finc_feetype_colon = null;
	/** 钞汇标识 */
//	private TextView finc_netPrice = null;
	/** 交易币种 */
	private TextView fincCurrencyText = null;
	/** 单位净值 */
	private TextView netPriceText = null;
	/** 基金状态 */
	private TextView fundStateText = null;
	/** 赎回下限 */
	private TextView sellLowLimitText = null;
	/** 最低持有份额 */
	private TextView holdQutyLowLimitText = null;
	/** 基金交易账户 */
	private TextView fincTradeAccountText = null;
	/** 资金账户名 */
	private TextView fincFundAccontName = null;
	/** 资金账户 */
	private TextView fincFundAccontNumber = null;
	/** 资金账户别名 */
	private TextView fincFundAccontNickName = null;
    //用户输入的份额
	private TextView finc_money = null;
	

	/** 用户输入的份额 */
	private String sellTotalValue = null;
	/** 基金代码 */
	private String foundCode = null;
	/** 基金名称 */
	private String foundName = null;
	private String tradeCurrencyStr;
	/* 可用份额， 对私快速赎回下限， 对私快速赎回上限， 单人单笔额度，对私快速赎回最低持有份额 */
	private String totalCount, sellLowLimit, sellUpLimit, perLimit, holdQutyLowLimit;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initData();
		setFundCompanyInfo();
//		initOnClick();
	}


	private void init() {
		myFincView = mainInflater.inflate(R.layout.finc_myfinc_sell_confirm_fast,
				null);
		tabcontent.addView(myFincView);
//		lastButton = (Button) findViewById(R.id.lastButton);
		sureButton = (Button) findViewById(R.id.sureButton);
		// 确定
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				flag = FINCSELL;
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();
			}
		});
		FincControl.setOnShowAllTextListener(this, 
				 fincNameText, (TextView)findViewById(R.id.finc_sellType_pre));
		StepTitleUtils.getInstance().initTitldStep(this,
				fincControl.getStepsForSell());
		StepTitleUtils.getInstance().setTitleStep(2);
		
		
		
		
		setTitle(getResources().getString(R.string.finc_myfinc_fastsellFound));
		((TextView)findViewById(R.id.finc_myfinc_sellLowLimit))
		.setText(getString(R.string.finc_myfinc_fastsellLowLimit));
		((TextView)findViewById(R.id.finc_money_tv))
		.setText(getString(R.string.finc_myfinc_sell_fastSellAmount));
		fincNameText = (TextView) findViewById(R.id.finc_fincName);
		fincCurrencyText = (TextView) findViewById(R.id.finc_currency);
		netPriceText = (TextView) findViewById(R.id.finc_netPrice);
		fundStateText = (TextView) findViewById(R.id.finc_type);
		sellLowLimitText = (TextView) findViewById(R.id.finc_sellLowLimit);
		holdQutyLowLimitText = (TextView) findViewById(R.id.finc_holdQutyLowLimit);
		finc_money = (TextView) findViewById(R.id.finc_money);
		
		fincTradeAccountText = (TextView) findViewById(R.id.finc_trade_account);
		fincFundAccontName = (TextView) findViewById(R.id.finc_fund_accont_name);
		fincFundAccontNumber = (TextView) findViewById(R.id.finc_fund_accont_number);
		fincFundAccontNickName =  (TextView) findViewById(R.id.finc_fund_accont_type);
		finc_feetype_colon = (TextView) findViewById(R.id.finc_feetype_colon);
		finc_myfinc_follow_compnay = (TextView) findViewById(R.id.finc_myfinc_follow_compnay);
//		finc_netPrice = (TextView) findViewById(R.id.finc_money_colon_remit);
		StepTitleUtils.getInstance().initTitldStep(this,
				fincControl.getStepsForSell());
		StepTitleUtils.getInstance().setTitleStep(1);
		initRightBtnForMain();
		FincControl.setOnShowAllTextListener(this, fincNameText,finc_myfinc_follow_compnay);
		
		
		getViewValue();
	}

	/***
	 * 初始化卖出页面传递的数据
	 */
	private void initData() {
		investAccount = fincControl.invAccId;
		account = fincControl.accNum;
		Intent intent = getIntent();
		sellTotalValue = intent.getStringExtra(Finc.FINC_SELLAMOUNT_REQ);
		if(StringUtil.isNull(sellTotalValue)){
			sellTotalValue = "-";
		}else{
			sellTotalValue = StringUtil.parseStringPattern(sellTotalValue, 2);
		}
		finc_money.setText(sellTotalValue);
		
//		if (!StringUtil.isNullOrEmpty(fincControl.tradeFundDetails)) {
//			Map<String, String> fundInfoMap = (Map<String, String>) fincControl.tradeFundDetails
//					.get(Finc.FINC_FUNDINFO_REQ);
////			foundCode = fundInfoMap.get(Finc.FINC_FUNDCODE);
////			foundName = fundInfoMap.get(Finc.FINC_FUNDNAME);
//		}

	}

	private void initOnClick() {
		// 上一步
//		lastButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// 返回到卖出填写页面
//				finish();
//			}
//		});
		// 确定
//		sureButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				flag = FINCSELL;
//				requestCommConversationId();
//				BaseHttpEngine.showProgressDialog();
//			}
//		});
	}

	/** 重写ConversationId回调方法 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 获取TokenId
		requestPSNGetTokenId();
	}

	/** 重写tokenId回调方法 */
	@Override
	public void requestPSNGetTokenIdCallback(Object resultObj) {
		super.requestPSNGetTokenIdCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		tokenId = (String) biiResponseBody.getResult();
		if ("".equals(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			switch (flag) {
			case FINCSELL:
				requestPsnFundFastSell(foundCode, sellTotalValue, tokenId);
				break;
			}

		}
	}
	
	@Override
	public void requestPsnFundFastSellCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.requestPsnFundFastSellCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> resultMap = (Map<String, String>) biiResponseBody
				.getResult();
		// 基金交易流水号
		String fundSeq = resultMap.get(Finc.TAACCCANCEL_FUNDSEQ);
		// 交易流水号
		String transactionId = resultMap.get(Finc.FINC_TRANSACTIONID_REQ);
		Intent intent = getIntent();
		intent.setClass(MyFincFastSellConfirmActivity.this,
				MyFincFastSellSuccessActivity.class);
		intent.putExtra(Finc.FINC_FUNDSEQ_REQ, fundSeq);
		intent.putExtra(Finc.FINC_TRANSACTIONID_REQ, transactionId);
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;

		default:
			break;
		}
	}
	
	
	/** 为控件赋值 */
	private void getViewValue() {
		if (!StringUtil.isNullOrEmpty(fincControl.tradeFundDetails)) {
			Map<String, Object> fundBalanceMap = fincControl.tradeFundDetails;
			String currentCapitalisation = (String) fundBalanceMap
					.get(Finc.FINC_CURRENTCAPITALISATION_REQ);
			Map<String, String> fundInfoMap = (Map<String, String>) fundBalanceMap
					.get(Finc.FINC_FUNDINFO_REQ);

			foundCode = fundInfoMap.get(Finc.FINC_FUNDCODE_REQ);
			foundName = fundInfoMap.get(Finc.FINC_FUNDNAME_REQ);
			tradeCurrencyStr = fundInfoMap.get(Finc.FINC_CURRENCY_REQ);
			String cashFlagCode = fundInfoMap.get(Finc.FINC_CASHFLAG);
			String netPrice = fundInfoMap.get(Finc.FINC_NETPRICE_REQ);
			totalCount = (String) fundBalanceMap
					.get(Finc.FINC_TOTALAVAILABLEBALANCE) != null ? (String) fundBalanceMap
							.get(Finc.FINC_TOTALAVAILABLEBALANCE) : "0";
							sellLowLimit = fundInfoMap.get(Finc.FINC_FASTFUNDSELL_LOW_LIMIT);
							sellUpLimit = fundInfoMap.get(Finc.FINC_FASTFUNDSELL_UP_LIMIT);
							perLimit = (String)fundInfoMap.get(Finc.FINC_FASTFUNDSELL_PER_LIMIT);
							holdQutyLowLimit = fundInfoMap.get(Finc.FINC_FASTFUNDSELL_HOLD_LOW_LIMIT);
							String fundState = fundInfoMap.get(Finc.FINC_FUNDSTATE_REQ);
							String state = null;
							//基金公司
							String fincCompnay = fundInfoMap.get(Finc.FINC_FUNDCOMPANYNAME_RES);
							//收费方式
							String feeType = fundInfoMap.get(Finc.FINC_FEETYPE_REQ);
							//钞汇标识
							String cashFlag = fundInfoMap.get(Finc.FINC_ATTENTIONQUERYLIST_CASHFLAG);
							if(StringUtil.isNull(cashFlag)){
								cashFlag = "-";
							}else{
								cashFlag = LocalData.CurrencyCashremit.get(cashFlag);
							}
							
							if (LocalData.fincFundStateMap.containsKey(fundState)) {
								state = LocalData.fincFundStateMap.get(fundState);
							}
							
							finc_myfinc_follow_compnay.setText(fincCompnay);
//							finc_netPrice.setText(cashFlag);
							finc_feetype_colon.setText(LocalData.fundfeeTypeCodeToStr.get(feeType));
							fincNameText.setText(foundName);
							fincCurrencyText.setText(FincControl.fincCurrencyAndCashFlag(
									tradeCurrencyStr, cashFlagCode));
							netPriceText.setText(StringUtil.parseStringPattern(netPrice, 4));

							String fincTradeAccount = null;
							if(!StringUtil.isNullOrEmpty(fincControl.accDetailsMap)){

								if(!StringUtil.isNull(fincControl.accDetailsMap.get(Finc.FINC_INVESTACCOUNT_RES))){
									fincTradeAccount = fincControl.accDetailsMap.get(Finc.FINC_INVESTACCOUNT_RES);
								}else{
									fincTradeAccount = "-";
								}
							}
							fincTradeAccountText.setText(fincTradeAccount);
							PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fincTradeAccountText);
							String fincFundAccontNameText = null;
							if(!StringUtil.isNull(fincControl.accDetailsMap.get(Finc.FINC_QUERYACCLIST_ACCOUNTTYPE))){
								fincFundAccontNameText = fincControl.accDetailsMap.get(Finc.FINC_QUERYACCLIST_ACCOUNTTYPE);
								fincFundAccontNameText = LocalData.AccountType.get(fincFundAccontNameText.trim());
							}else{
								fincFundAccontNameText = "-";
							}
							fincFundAccontName.setText(fincFundAccontNameText);

							String fincFundAccontNumberText = null;
							if(!StringUtil.isNull(fincControl.accDetailsMap.get(Finc.FINC_ACCOUNT_RES))){
								fincFundAccontNumberText = fincControl.accDetailsMap.get(Finc.FINC_ACCOUNT_RES);
								fincFundAccontNumberText = StringUtil.getForSixForString(fincFundAccontNumberText);
							}else{
								fincFundAccontNumberText = "-";
							}
							fincFundAccontNumber.setText(fincFundAccontNumberText);

							String fincFundAccontNickNameText = null;
							if(!StringUtil.isNull(fincControl.accDetailsMap.get(Finc.FINC_ACCOUNTNICKNAME_RES))){
								fincFundAccontNickNameText = fincControl.accDetailsMap.get(Finc.FINC_ACCOUNTNICKNAME_RES);
							}else{
								fincFundAccontNickNameText = "-";
							}
							fincFundAccontNickName.setText(fincFundAccontNickNameText);

							fundStateText.setText(state);
							sellLowLimitText.setText(StringUtil.parseStringPattern(
									sellLowLimit, 2));
							holdQutyLowLimitText.setText(StringUtil.parseStringPattern(
									holdQutyLowLimit, 2));
		}
	}

}
