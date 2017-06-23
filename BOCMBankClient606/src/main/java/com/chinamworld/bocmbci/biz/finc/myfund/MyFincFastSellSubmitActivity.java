package com.chinamworld.bocmbci.biz.finc.myfund;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * 我的基金---快速赎回填写页面
 * 
 * @author fsm
 * 
 */
public class MyFincFastSellSubmitActivity extends FincBaseActivity {
	/** 基金持仓主view */
	private View myFincView = null;
	/** 基金代码 */
	private TextView fincCodeText = null;
	/** 基金名称 */
	private TextView fincNameText = null;
	/** 基金公司 */
	private TextView finc_myfinc_follow_compnay = null;
	/** 收费方式 */
	private TextView finc_feetype_colon = null;
	/** 钞汇标识 */
	//	private TextView finc_netPrice = null;
	/** 币种 */
	private TextView fincCurrencyText = null;
	/** 单位净值 */
	private TextView netPriceText = null;
	/** 可用份额 */
	private TextView totalCountText = null;
	/** 当前市值 */
	private TextView currentCapitalisationText = null;
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
	/** 赎回份额 */
	private EditText sellTotalEdit = null;


	/** 巨额赎回方式 */
	private Spinner sellTypeSpinner = null;
	/** 下一步 */
	private Button nextButton = null;
	/** 用户输入的份额 */
	private String sellTotalValue = null;
	/** 基金代码 */
	private String foundCode = null;
	/** 基金名称 */
	private String foundName = null;
	private String tradeCurrencyStr;

	private Button extrasDayButton;
	/* 可用份额， 对私快速赎回下限， 对私快速赎回上限， 单人单笔额度，对私快速赎回最低持有份额 */
	private String totalAvailableBalance, sellLowLimit, sellUpLimit, perLimit, holdQutyLowLimit;
	/** 实际份额*/
	private String totalCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BaseHttpEngine.showProgressDialogCanGoBack();
		doCheckRequestAccInfo();

	}

	/**
	 * 查询基金账户check
	 */
	public void doCheckRequestAccInfo() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_QUERYINVTBINDINGINFO_API);
		biiRequestBody.setConversationId(null);
		Map<String, String> map = new Hashtable<String, String>();
		map.put(Finc.FINC_INVTTYPE_REQ, ConstantGloble.FINC_SERVICECODE);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"doCheckRequestAccInfoCallback");
	}

	public void doCheckRequestAccInfoCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		Map<String, String> map = (Map<String, String>) biiResponseBodys
				.get(0).getResult();
		fincControl.accDetailsMap = map;
		fincControl.ifhaveaccId = true;
		if (!StringUtil.isNullOrEmpty(fincControl.tradeFundDetails)) {
			Map<String, Object> fundBalanceMap = fincControl.tradeFundDetails;
			String currentCapitalisation = (String) fundBalanceMap
					.get(Finc.FINC_CURRENTCAPITALISATION_REQ);
			Map<String, String> fundInfoMap = (Map<String, String>) fundBalanceMap
					.get(Finc.FINC_FUNDINFO_REQ);
			foundCode = fundInfoMap.get(Finc.FINC_FUNDCODE_REQ);
			//基金快速额度赎回查询
			getFastSellFund(foundCode);
		}

	}


	/**
	 * 基金快速赎回额度查询 回调处理
	 */
	@Override
	public void getFastSellFundCallback(Object resultObj) {    
		// TODO Auto-generated method stub
		super.getFastSellFundCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		fincControl.fastSellFundDetails= (Map<String, Object>) biiResponseBody
				.getResult();

		init();
		getViewValue();
		BaseHttpEngine.dissMissProgressDialog();

		//		if(!StringUtil.isNull(foundCode)){
		//			getFincFund(foundCode);
		//		}

	}




	@Override
	protected void onResume() {
		super.onResume();
		saleFlag = true;
	}

	/** 初始化控件 */
	private void init() {
		myFincView = mainInflater.inflate(R.layout.finc_myfinc_fast_sell_submit,
				null);
		tabcontent.addView(myFincView);
		((LinearLayout)findViewById(R.id.finc_fast_sell)).setVisibility(View.GONE);
		setTitle(getResources().getString(R.string.finc_myfinc_fastsellFound));
		//		((LinearLayout)findViewById(R.id.finc_fastsellfund)).setVisibility(View.VISIBLE);
		((LinearLayout)findViewById(R.id.finc_sellType_ll)).setVisibility(View.GONE);
		((LinearLayout)findViewById(R.id.titleText_ll)).setVisibility(View.VISIBLE);
		((LinearLayout)findViewById(R.id.titleText_ll1)).setVisibility(View.VISIBLE);
		((LinearLayout)findViewById(R.id.finc_sellUpLimit_ll)).setVisibility(View.VISIBLE);
		((LinearLayout)findViewById(R.id.finc_fincCode_layout)).setVisibility(View.GONE);
		((LinearLayout)findViewById(R.id.finc_totalCount_layout)).setVisibility(View.GONE);
		((LinearLayout)findViewById(R.id.finc_currentCapitalisation_layout)).setVisibility(View.GONE);
		((LinearLayout)findViewById(R.id.finc_feetype_colon_layout)).setVisibility(View.VISIBLE);
		((LinearLayout)findViewById(R.id.finc_myfinc_follow_compnay_layout )).setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.finc_myfinc_sellLowLimit))
		.setText(getString(R.string.finc_myfinc_fastsellLowLimit));
		((TextView)findViewById(R.id.finc_money_tv))
		.setText(getString(R.string.finc_myfinc_sell_fastSellAmount));
		fincCodeText = (TextView) findViewById(R.id.finc_fincCode);
		fincNameText = (TextView) findViewById(R.id.finc_fincName);
		fincCurrencyText = (TextView) findViewById(R.id.finc_currency);
		netPriceText = (TextView) findViewById(R.id.finc_netPrice);
		totalCountText = (TextView) findViewById(R.id.finc_totalCount);
		currentCapitalisationText = (TextView) findViewById(R.id.finc_currentCapitalisation);
		fundStateText = (TextView) findViewById(R.id.finc_type);
		sellLowLimitText = (TextView) findViewById(R.id.finc_sellLowLimit);
		holdQutyLowLimitText = (TextView) findViewById(R.id.finc_holdQutyLowLimit);
		sellTotalEdit = (EditText) findViewById(R.id.finc_money);
		fincTradeAccountText = (TextView) findViewById(R.id.finc_trade_account);
		fincFundAccontName = (TextView) findViewById(R.id.finc_fund_accont_name);
		fincFundAccontNumber = (TextView) findViewById(R.id.finc_fund_accont_number);
		fincFundAccontNickName =  (TextView) findViewById(R.id.finc_fund_accont_type);
		finc_feetype_colon = (TextView) findViewById(R.id.finc_feetype_colon);
		finc_myfinc_follow_compnay = (TextView) findViewById(R.id.finc_myfinc_follow_compnay);
		//		finc_netPrice = (TextView) findViewById(R.id.finc_money_colon_remit);
		findViewById(R.id.fund_detail_layout).setVisibility(View.VISIBLE);
		findViewById(R.id.trade_info_layout).setVisibility(View.VISIBLE);
		findViewById(R.id.finc_fund_accont_layout_left).setVisibility(View.VISIBLE);
		findViewById(R.id.finc_trade_account_layout).setVisibility(View.VISIBLE);
		sellTypeSpinner = (Spinner) findViewById(R.id.finc_sellType);
		nextButton = (Button) findViewById(R.id.nextButton);
//		extrasDayButton = (Button) findViewById(R.id.extrasDayButton);
//		extrasDayButton.setVisibility(View.GONE);
		nextButton.setText(getString(R.string.next));
//		ViewUtils.initBtnParams(nextButton,this);
		nextButton.setOnClickListener(this);
		StepTitleUtils.getInstance().initTitldStep(this,
				fincControl.getStepsForSell());
		StepTitleUtils.getInstance().setTitleStep(1);
		initRightBtnForMain();
		FincControl.setOnShowAllTextListener(this, fincNameText,finc_myfinc_follow_compnay);
	}

	/** 为控件赋值 */
	private void getViewValue() {
		if (!StringUtil.isNullOrEmpty(fincControl.tradeFundDetails)) {
			Map<String, Object> fundBalanceMap = fincControl.tradeFundDetails;
			String currentCapitalisation = (String) fundBalanceMap
					.get(Finc.FINC_CURRENTCAPITALISATION_REQ);
			Map<String, String> fundInfoMap = (Map<String, String>) fundBalanceMap
					.get(Finc.FINC_FUNDINFO_REQ);
			setStepTitle();
			setStepTitle1();

			foundCode = fundInfoMap.get(Finc.FINC_FUNDCODE_REQ);
			foundName = fundInfoMap.get(Finc.FINC_FUNDNAME_REQ);
			tradeCurrencyStr = fundInfoMap.get(Finc.FINC_CURRENCY_REQ);
			StringUtil.setInPutValueByCurrency(sellTotalEdit, tradeCurrencyStr);
			String cashFlagCode = fundInfoMap.get(Finc.FINC_CASHFLAG);
			String netPrice = fundInfoMap.get(Finc.FINC_NETPRICE_REQ);
			totalAvailableBalance = (String) fundBalanceMap
					.get(Finc.FINC_TOTALAVAILABLEBALANCE) != null ? (String) fundBalanceMap
							.get(Finc.FINC_TOTALAVAILABLEBALANCE) : "0";
			totalCount = (String) fundBalanceMap
					.get(Finc.FINC_SELLALLLIMIT) != null ? (String) fundBalanceMap
							.get(Finc.FINC_SELLALLLIMIT) : "0";
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
							fincCodeText.setText(foundCode);
							fincNameText.setText(foundName);
							fincCurrencyText.setText(FincControl.fincCurrencyAndCashFlag(
									tradeCurrencyStr, cashFlagCode));
							netPriceText.setText(StringUtil.parseStringPattern(netPrice, 4));
							if (!StringUtil.isNullOrEmpty(totalAvailableBalance)) {
								totalCountText.setText(StringUtil.parseStringPattern(
										totalAvailableBalance, 2));
							}

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

							currentCapitalisationText.setText(currentCapitalisation);
							fundStateText.setText(state);
							sellLowLimitText.setText(StringUtil.parseStringPattern(
									sellLowLimit, 2));
							holdQutyLowLimitText.setText(StringUtil.parseStringPattern(
									holdQutyLowLimit, 2));
							ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
									R.layout.dept_spinner, LocalData.fundSellDealTypeList);
							adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							sellTypeSpinner.setAdapter(adapter);
							sellTypeSpinner.setSelection(0);
		}
	}

	/**
	 * 初始化快速赎回规则提示信息 1
	 * @param
	 */
	private void setStepTitle() {
		if (!StringUtil.isNullOrEmpty(fincControl.fastSellFundDetails)) {
			Map<String, Object> fastSellMap = fincControl.fastSellFundDetails;	
			String strContext = getString(R.string.finc_step_fastsell_inputs);
			String perDealNum = (String)fastSellMap.get(Finc.FINC_PER_DEALNUM);
			String perLimit = (String)fastSellMap.get(Finc.FINC_PER_LIMIT);
			String daylimit = (String)fastSellMap.get(Finc.FINC_DAY_LIMIT);
			String result = strContext.replace("A", StringUtil.valueOf1(perDealNum))
					.replace("B", StringUtil.valueOf1(perLimit))
					.replace("C", StringUtil.valueOf1(daylimit));
			TextView stepTitle = (TextView)findViewById(R.id.titleText);
			stepTitle.setText(result);
		}
	}
	/**
	 * 初始化快速赎回规则提示信息 2
	 * @param
	 */
	private void setStepTitle1() {
		if (!StringUtil.isNullOrEmpty(fincControl.fastSellFundDetails)) {
			Map<String, Object> fastSellMap = fincControl.fastSellFundDetails;
			String strContext = getString(R.string.finc_step_fastsell_inputs1);
			String dayCompleteNum = (String)fastSellMap.get(Finc.FINC_DAY_COMPLETE_NUM);
			String dayCompleteShare = (String)fastSellMap.get(Finc.FINC_DAY_COMPLETE_SHARE);
			String dayQuickSellLimit = (String)fastSellMap.get(Finc.FINC_DAY_QUICK_SELL_LIMIT);
			String result = strContext.replace("D", StringUtil.valueOf1(dayCompleteNum))
					.replace("E", StringUtil.valueOf1(dayCompleteShare))
					.replace("F", StringUtil.valueOf1(dayQuickSellLimit));
			TextView stepTitle = (TextView)findViewById(R.id.titleText1);
			stepTitle.setText(result);
		}
	}

	private boolean saleFlag = true;// 防止重复点击

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ib_back:
			finish();
			break;
		default:
			sellTotalValue = sellTotalEdit.getText().toString().trim();

			//获得单日赎回上限 金额
			//			String daySellLimit=(String)fincControl.fincFundDetails.
			//					                                 get(Finc.FINC_DAY_TOPLIMIT);
			//			
			//			if(regexpData(sellTotalValue, totalCount, sellLowLimit, 
			//					perLimit, holdQutyLowLimit)){
			//				if(Double.valueOf(sellTotalValue) > Double
			//						.valueOf(daySellLimit)){//高于当日赎回上限
			//					BaseDroidApp.getInstanse().showInfoMessageDialog(
			//							getString(R.string.finc_xpadManRedeemAmt_error));
			//					return;
			//				}
			//				BaseHttpEngine.showProgressDialog();
			//				requestFundCompanyInfoQuery(foundCode);



			if(regexpData(sellTotalValue, totalAvailableBalance, sellLowLimit, 
					perLimit, holdQutyLowLimit)){
				BaseHttpEngine.showProgressDialog();
				requestFundCompanyInfoQuery(foundCode);
			}
			break;
		}
	}

	public void fundCompanyInfoQueryCallback(Object resultObj) {
		super.fundCompanyInfoQueryCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent(MyFincFastSellSubmitActivity.this,
				MyFincFastSellConfirmActivity.class);
		// 页面显示的内容
		intent.putExtra(Finc.FINC_SELLAMOUNT_REQ, sellTotalValue);
		saleFlag = false;
		startActivityForResult(intent, 1);
	}

	/**
	 * 快速赎回份额校验
	 *  全额赎回(赎回份额=可用份额)，不需要判断赎回份额或最低持有份额
	 *   部分赎回(赎回份额<可用份额)时需同时满足如下条件：
	 *   赎回份额>=对私快速赎回下限
	 *   赎回份额<=单人单笔额度
	 *   可用份额-赎回份额>=快速赎回最低持有份额
	 * @param sellTotalValue 快速赎回总份额
	 * @param avilibleValue 可用份额
	 * @param quickSaleLowLimit 对私快速赎回下限
	 * @param perLimit 单人单笔额度
	 * @param holdQutyLowLimit 对私快速赎回最低持有份额 
	 */
	private boolean regexpData(String sellTotalValue, String avilibleValue, 
			String quickSaleLowLimit, String perLimit, String holdQutyLowLimit){
		ArrayList<RegexpBean> list = new ArrayList<RegexpBean>();
		RegexpBean regexp = StringUtil.getRegexBeanByCurrency(
				getString(R.string.finc_myfinc_sell_fastSellAmount_nocolon), sellTotalValue,
				tradeCurrencyStr);
		list.add(regexp);
		//校验其是否合法
		if (RegexpUtils.regexpDate(list)) {
			if(avilibleValue == null || quickSaleLowLimit == null 
					|| perLimit == null || holdQutyLowLimit == null)
				return false;
			// 当赎回份额等于持仓份额表示全额赎回，不进行控制；（可用份额）
			if (sellTotalValue.equals(totalAvailableBalance)) {
				return true;
			}
			if(Double.valueOf(sellTotalValue) < Double.valueOf(avilibleValue)){

				if(Double.valueOf(sellTotalValue) >= Double.valueOf(quickSaleLowLimit)){
					if(Double.valueOf(sellTotalValue) <= Double.valueOf(perLimit)){
						if(Double.valueOf(avilibleValue) - 
								Double.valueOf(sellTotalValue) >= 
								Double.valueOf(holdQutyLowLimit)){
							return true;
						}else{
							BaseDroidApp
							.getInstanse()
							.showInfoMessageDialog(
									getString(R.string.sell_amount_lessthan_holdlowlimit));
							return false;
						}
					}else{
						BaseDroidApp
						.getInstanse()
						.showInfoMessageDialog(
								getString(R.string.sell_amount_morethan_perlimit));
						return false;
					}
				}else{
					BaseDroidApp
					.getInstanse()
					.showInfoMessageDialog(
							getString(R.string.sell_amount_lessthan_lowlimit));
					return false;
				}
				
			}else if(Double.valueOf(sellTotalValue) == Double.valueOf(avilibleValue)){
				return true;
			}else{
				BaseDroidApp
				.getInstanse()
				.showInfoMessageDialog(
						getString(R.string.finc_xpadFastMaxRedeemAmt_error));
				return false;
			}
		}		
		return false;
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

}
