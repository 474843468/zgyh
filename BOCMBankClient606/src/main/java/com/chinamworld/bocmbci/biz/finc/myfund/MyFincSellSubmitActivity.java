package com.chinamworld.bocmbci.biz.finc.myfund;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 我的基金---赎回填写页面
 * 
 * @author Administrator
 * 
 */
public class MyFincSellSubmitActivity extends FincBaseActivity {
	private final String TAG = "MyFincSellSubmitActivity";
	/** 基金持仓主view */
	private View myFincView = null;
	/** 基金代码 */
	private TextView fincCodeText = null;
	/** 基金名称 */
	private TextView fincNameText = null;
	/** 币种 */
	private TextView fincCurrencyText = null;
	/** 单位净值 */
	private TextView netPriceText = null;
	/** 可用份额 */
	private TextView totalCountText = null;
	/**2014-11-18
	 * 单日赎回上限
	 */
	private TextView dayTopLimit=null;
//	/** 当前市值 */
//	private TextView currentCapitalisationText = null;
//	/** 基金状态 */
//	private TextView fundStateText = null;
	/** 赎回下限 */
	private TextView sellLowLimitText = null;
	/** 最低持有份额 */
	private TextView holdQutyLowLimitText = null;
	/** 赎回份额 */
	private EditText sellTotalEdit = null;
	/** 巨额赎回方式 */
	private Spinner sellTypeSpinner = null;
//	/** 钞汇标识  */
//	private TextView finMoneyColonRemit;
	/** 下一步 */
	private Button nextButton = null;
	/** 用户输入的份额 */
	private String sellTotalValue = null;
	/** 用户选择的赎回方式,方式名称 */
	private String sellTypeValue = null;
	/** 基金代码 */
	private String foundCode = null;
	/** 基金名称 */
	private String foundName = null;
	private String tradeCurrencyStr;
	/** 0-不连续赎回(取消赎回) */
	// private String zero = null;
	/** 1-连续赎回(顺延赎回) */
	// private String one = null;

	private String isZisSale = null;
	private Button extrasDayButton;
	/** 可用份额 */
	private String totalAvailableBalance;
	/** 实际份额 */
	private String totalCount;
	/** 对私最低持有份额 */
	private String holdQutyLowLimit;
	private String sellLowLimit;
	/**短期理财产品明细查询按钮布局*/
	private View fincFundDueDateLayout;

	/**短期理财显示文本*/
	private Button fincFundDueDateText;

	/**短期理财提示信息布局*/
	private View fincFundDuePromptLayout;

	/**单日赎回上限 layout*/
	private View finc_fast_sell;

	//	/**短期理财产品界面控件初始化*/
	//	private TextView dueFundCode;
	//	private TextView dueFundName;
	//	private TextView dueTypeColon;
	//	private TextView dueRealityShare;
	//	private TextView duetotalCount;
	//	private TextView dueRegisterDate;
	//	private TextView dueRedemptionDate;

	//	/**短期理财详情的整个布局视图*/
	//	private View dueDateView;

	private ImageView imgExitAccdetail;
	/**是否需要判断单日赎回上限*/
	private boolean judgeLimit = false;
	private boolean isExtrasDay;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		getViewValue();
		initOnClick();
	}

	@Override
	protected void onResume() {
		super.onResume();
		saleFlag = true;
	}




	/** 初始化控件 */
	private void init() {
		setRightToMainHome();
		myFincView = mainInflater.inflate(R.layout.finc_myfinc_sell_submit,
				null);
		myFincView.setVisibility(View.INVISIBLE);
		tabcontent.addView(myFincView);
		setTitle(getResources().getString(R.string.finc_myfinc_sellFound));
		fincCodeText = (TextView) findViewById(R.id.finc_fincCode);
		fincNameText = (TextView) findViewById(R.id.finc_fincName);
		fincCurrencyText = (TextView) findViewById(R.id.finc_currency);
		netPriceText = (TextView) findViewById(R.id.finc_netPrice);
		totalCountText = (TextView) findViewById(R.id.finc_totalCount);
		dayTopLimit=(TextView) findViewById(R.id.finc_dayTopLimit);
//		currentCapitalisationText = (TextView) findViewById(R.id.finc_currentCapitalisation);
//		fundStateText = (TextView) findViewById(R.id.finc_type);
		sellLowLimitText = (TextView) findViewById(R.id.finc_sellLowLimit);
		holdQutyLowLimitText = (TextView) findViewById(R.id.finc_holdQutyLowLimit);
		sellTotalEdit = (EditText) findViewById(R.id.finc_money);
		sellTypeSpinner = (Spinner) findViewById(R.id.finc_sellType);
		nextButton = (Button) findViewById(R.id.nextButton);
		extrasDayButton = (Button) findViewById(R.id.extrasDayButton);
		fincFundDueDateText = (Button)findViewById(R.id.finc_fund_due_date_text);
		fincFundDueDateLayout = findViewById(R.id.finc_fund_due_date_layout);
		fincFundDuePromptLayout = findViewById(R.id.finc_fund_due_prompt_layout);
		finc_fast_sell = findViewById(R.id.finc_fast_sell);
//		finMoneyColonRemit = (TextView) findViewById(R.id.finc_money_colon_remit);
		extrasDayButton.setOnClickListener(this);
		StepTitleUtils.getInstance().initTitldStep(this,
				fincControl.getStepsForSell());
		StepTitleUtils.getInstance().setTitleStep(1);
		initRightBtnForMain();
		TextView finc_myfinc_sell_sellType = (TextView) findViewById(R.id.finc_myfinc_sell_sellType);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_myfinc_sell_sellType);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fincNameText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, dayTopLimit);

		if("Y".equals(fincControl.fincFundBasicDetails.get(Finc.FINC_IS_SHORT_FUND))){
			fincFundDueDateLayout.setVisibility(View.VISIBLE);
			fincFundDuePromptLayout.setVisibility(View.VISIBLE);

			fincFundDueDateText.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					BaseHttpEngine.showProgressDialog();
					String fundCode =(String) fincControl.fincFundBasicDetails.get(Finc.FINC_FUNDCODE);
					getFundFundDueDate(fundCode) ;
				}
			});

		}
		
		
	}


	private View initFundDueDate(){
		View dueDateView = LayoutInflater.from(
				MyFincSellSubmitActivity.this).inflate(
						R.layout.finc_myfinc_due_date_query, null);
		//		 dueFundCode = (TextView) dueDateView.findViewById(R.id.finc_fincCode);
		//		 dueFundName = (TextView) dueDateView.findViewById(R.id.finc_finc_name);
		//		 dueTypeColon = (TextView) dueDateView.findViewById(R.id.finc_feetype_colon);
		//		 dueRealityShare = (TextView) dueDateView.findViewById(R.id.finc_reality_share);
		//		 duetotalCount = (TextView) dueDateView.findViewById(R.id.finc_myfinc_totalCount);
		//		 dueRegisterDate = (TextView) dueDateView.findViewById(R.id.finc_myfinc_register_date);
		//		 dueRedemptionDate = (TextView) dueDateView.findViewById(R.id.finc_myfinc_redemption_date);
		imgExitAccdetail = (ImageView) dueDateView.findViewById(R.id.img_exit_accdetail);

		imgExitAccdetail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseDroidApp.getInstanse().dismissMessageDialog();
			}
		});


		return dueDateView;
	}

	//	private void setDateDue(){
	//		
	//		dueFundCode.setText((String) fincControl.fincFundDueDateQuery.get(Finc.FINC_FUNDCODE));
	//		dueFundName.setText((String) fincControl.fincFundDueDateQuery.get(Finc.FINC_FUNDNAME));
	//		dueTypeColon.setText
	//		(LocalData.fundfeeTypeCodeToStr.get((String) fincControl.fincFundDueDateQuery.get(Finc.FINC_FEETYPE)));
	//		dueRealityShare.setText(StringUtil.parseStringPattern
	//				(((String) fincControl.fincFundDueDateQuery.get(Finc.FINC_MATURE_TOTAL)), 2));
	//		duetotalCount.setText(StringUtil.parseStringPattern
	//				(((String) fincControl.fincFundDueDateQuery.get(Finc.FINC_FREE_QUTY)), 2));
	//		
	//		dueRegisterDate.setText((String) fincControl.fincFundDueDateQuery.get(Finc.FINC_QUTY_REGIST));
	//		dueRedemptionDate.setText((String) fincControl.fincFundDueDateQuery.get(Finc.FINC_ALLOW_REDEEM));
	//		
	//	}

	/** 为控件赋值 */
	private void getViewValue() {
		if (!StringUtil.isNullOrEmpty(fincControl.tradeFundDetails)) {
			Map<String, Object> fundBalanceMap = fincControl.tradeFundDetails;
			String currentCapitalisation = (String) fundBalanceMap
					.get(Finc.FINC_CURRENTCAPITALISATION_REQ);
			//			Map<String, String> fundInfoMap = (Map<String, String>) fundBalanceMap
			//					.get(Finc.FINC_FUNDINFO_REQ);
			Map<String, Object> fundInfoMap = (Map<String, Object>) fundBalanceMap
					.get(Finc.FINC_FUNDINFO_REQ);
			if(fundInfoMap == null){
				fundInfoMap = fundBalanceMap;
			}
			/*
			 * 2014-11-18
			 * 设置当日赎回上限
			 * */
			if (!StringUtil.isNullOrEmpty(fincControl.fincFundBasicDetails)) {
				if("0".equals(fincControl.fincFundBasicDetails.get(Finc.FINC_DAY_TOPLIMIT)) 
						|| StringUtil.isNullOrEmpty(fincControl.fincFundBasicDetails.get(Finc.FINC_DAY_TOPLIMIT))){
					finc_fast_sell.setVisibility(View.GONE);
					judgeLimit = false;
				}else{
					judgeLimit = true;
					finc_fast_sell.setVisibility(View.VISIBLE);
					dayTopLimit.setText(StringUtil.
							parseStringPattern((String)fincControl.fincFundBasicDetails.
									get(Finc.FINC_DAY_TOPLIMIT), 2));
				}

			} else if(!StringUtil.isNullOrEmpty(fincControl.tradeFundDetails)){
				if(StringUtil.isNullOrEmpty(fincControl.tradeFundDetails.
						get(Finc.FINC_DAY_TOPLIMIT)) || 
						"0".equals(fincControl.tradeFundDetails.
								get(Finc.FINC_DAY_TOPLIMIT))){
					finc_fast_sell.setVisibility(View.GONE);
					judgeLimit = false;
				}else{
					judgeLimit = true;
					finc_fast_sell.setVisibility(View.VISIBLE);
					dayTopLimit.setText(StringUtil.
							parseStringPattern((String)fincControl.tradeFundDetails.
									get(Finc.FINC_DAY_TOPLIMIT), 2));
				}
			}
			foundCode = (String)fundInfoMap.get(Finc.FINC_FUNDCODE_REQ);
			foundName = (String)fundInfoMap.get(Finc.FINC_FUNDNAME_REQ);
			tradeCurrencyStr = (String)fundInfoMap.get(Finc.FINC_CURRENCY_REQ);
			StringUtil.setInPutValueByCurrency(sellTotalEdit, tradeCurrencyStr);
			String cashFlagCode = (String)fundInfoMap.get(Finc.FINC_CASHFLAG);
			String netPrice = (String)fundInfoMap.get(Finc.FINC_NETPRICE_REQ);
			String cashFlag = (String) fundInfoMap.get(Finc.FINC_ATTENTIONQUERYLIST_CASHFLAG);
			totalAvailableBalance = (String) fundBalanceMap
					.get(Finc.FINC_TOTALAVAILABLEBALANCE) != null ? (String) fundBalanceMap
							.get(Finc.FINC_TOTALAVAILABLEBALANCE) : "0";
			totalCount = (String) fundBalanceMap
					.get(Finc.FINC_SELLALLLIMIT) != null ? (String) fundBalanceMap
							.get(Finc.FINC_SELLALLLIMIT) : "0";
							holdQutyLowLimit = (String)fundInfoMap.get(Finc.FINC_HOLDLOWCOUNT);
							sellLowLimit = (String)fundInfoMap.get(Finc.FINC_SELLLOWLIMIT_REQ);
							String fundState = (String)fundInfoMap.get(Finc.FINC_FUNDSTATE_REQ);
							//			foundCode = (String)fundBalanceMap.get(Finc.FINC_FUNDCODE_REQ);
							//			foundName = (String)fundBalanceMap.get(Finc.FINC_FUNDNAME_REQ);
							//			tradeCurrencyStr = (String)fundBalanceMap.get(Finc.FINC_CURRENCY_REQ);
							//			StringUtil.setInPutValueByCurrency(sellTotalEdit, tradeCurrencyStr);
							//			String cashFlagCode = (String)fundBalanceMap.get(Finc.FINC_CASHFLAG);
							//			String netPrice = (String)fundBalanceMap.get(Finc.FINC_NETPRICE_REQ);
							//			totalCount = (String) fundBalanceMap
							//					.get(Finc.FINC_TOTALAVAILABLEBALANCE) != null ? (String) fundBalanceMap
							//					.get(Finc.FINC_TOTALAVAILABLEBALANCE) : "0";
							//			sellLowLimit = (String)fundBalanceMap.get(Finc.FINC_SELLLOWLIMIT_REQ);
							//			String fundState = (String)fundBalanceMap.get(Finc.FINC_FUNDSTATE_REQ);

//							String state = null;
//							if (LocalData.fincFundStateMap.containsKey(fundState)) {
//								state = LocalData.fincFundStateMap.get(fundState);
//							}
							fincCodeText.setText(foundCode);
							fincNameText.setText(foundName);
							fincCurrencyText.setText(FincControl.fincCurrencyAndCashFlag(
									tradeCurrencyStr, cashFlagCode));
							netPriceText.setText(StringUtil.parseStringPattern(netPrice, 4));
							if (!StringUtil.isNullOrEmpty(totalAvailableBalance)) {
								totalCountText.setText(StringUtil.parseStringPattern(
										totalAvailableBalance, 2));
							}
//							currentCapitalisationText.setText(StringUtil.parseStringPattern(currentCapitalisation,2));
//							fundStateText.setText(state);
							sellLowLimitText.setText(StringUtil.parseStringPattern(
									sellLowLimit, 2));
							holdQutyLowLimitText.setText(StringUtil.parseStringPattern(holdQutyLowLimit, 2));
//							if(StringUtil.isNull(cashFlag)){
//								cashFlag = "-";
//							}else{
//								cashFlag = LocalData.CurrencyCashremit.get(cashFlag);
//							}
//							finMoneyColonRemit.setText(cashFlag);
							ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
									R.layout.dept_spinner, LocalData.fundSellDealTypeList);
							adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							sellTypeSpinner.setAdapter(adapter);
							sellTypeSpinner.setSelection(0);
							BaseHttpEngine.showProgressDialogCanGoBack();
							getFundDetailByFundCode(foundCode);
							setOcrmTradeAmount();
							isZisSale = (String)fundInfoMap.get(Finc.ISZISSALE);
							initbtnView(isZisSale);

		}
	}

	private void setOcrmTradeAmount(){
		if (getIntent().getBooleanExtra(Finc.orcmflag, false)) {
			if (!StringUtil.isNullOrEmpty(fincControl.OcrmProductDetailMap)) {
				String transSum = (String) fincControl.OcrmProductDetailMap
						.get(Finc.FINC_FUNDTODAYQUERY_TRANSCOUNT);
				sellTotalEdit.setText(StringUtil.valueOf1(transSum));
			}
		}
	}

	@Override
	public void getFundDetailByFundCodeCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.getFundDetailByFundCodeCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> resultMap = (Map<String, String>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			isZisSale = (String)resultMap.get(Finc.ISZISSALE);
			initbtnView(isZisSale);
		}
		myFincView.setVisibility(View.VISIBLE);
	}

	private void initOnClick() {
		// 赎回方式
		sellTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				sellTypeValue = LocalData.fundSellDealTypeCodeList
						.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		nextButton.setOnClickListener(this);
	}

	private boolean saleFlag = true;// 防止重复点击

	@Override
	public void onClick(View v) {
		super.onClick(v);
		ArrayList<RegexpBean> list = new ArrayList<RegexpBean>();
		//获得赎回上限,判断用户输入的和当日赎回上限
		sellTotalValue=sellTotalEdit.getText().toString();

		String daySellLimit = null;
		if(judgeLimit){
			daySellLimit=(String)fincControl.tradeFundDetails.
					get(Finc.FINC_DAY_TOPLIMIT);
			if(daySellLimit == null){
				daySellLimit=(String)fincControl.fincFundBasicDetails.
						get(Finc.FINC_DAY_TOPLIMIT);
			}
		}

		RegexpBean regexp = StringUtil.getRegexBeanByCurrency(
				getString(R.string.bocinvt_redeem_regex), sellTotalValue,
				tradeCurrencyStr);
		list.add(regexp);
		switch (v.getId()) {
		// 按指定日期赎回
		case R.id.extrasDayButton:
			isExtrasDay = true;
			// 跳转到确认信息页面
			if (RegexpUtils.regexpDate(list)) {
				//赎回份额是否大于单日赎回上限验证
//				if(judgeLimit){
//					if(Double.valueOf(sellTotalValue) > Double
//							.valueOf(daySellLimit)){//高于当日赎回上限
//						BaseDroidApp.getInstanse().showInfoMessageDialog(
//								getString(R.string.finc_xpadManRedeemAmt_error));
//						return;
//					}
//				}

				//TODO 当赎回份额等于持仓份额表示全额赎回，不进行控制；
				if (sellTotalValue.equals(totalAvailableBalance)) {
					BaseHttpEngine.showProgressDialog();
					requestFundCompanyInfoQuery(foundCode);
					return;
				}
				if (Double.parseDouble(sellTotalValue) == Double.parseDouble(totalAvailableBalance)) {
					BaseHttpEngine.showProgressDialog();
					requestFundCompanyInfoQuery(foundCode);
					return;
				}
				// 赎回份额要求大于0并且小于等于客户当前持有的份额（可用份额）
				if(Double.parseDouble(sellTotalValue)>Double.parseDouble(totalAvailableBalance)){
					 BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_xpadMaxRedeemAmt_error));
					 return;
				 }
				

				if (Double.parseDouble(sellTotalValue) < Double
						.parseDouble(sellLowLimit)) {// 低于

					// 赎回下限
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getString(R.string.finc_xpadMinRedeemAmt_error));
					return;
				}
				
				
				//				if (Double.valueOf(sellTotalValue) > Double.valueOf(totalCount)) {// 高于可用份额
				//					BaseDroidApp.getInstanse().showInfoMessageDialog(
				//							getString(R.string.finc_xpadMaxRedeemAmt_error));
				//					return;
				//				}
				
				// 可用份额-赎回份额>=基金最低持有份额
				if (Double.parseDouble(totalAvailableBalance) - Double.parseDouble(sellTotalValue) 
						< Double.parseDouble(holdQutyLowLimit)) { 
					BaseDroidApp
					.getInstanse()
					.showInfoMessageDialog(
							getString(R.string.finc_xpadMinHoldLowCountRedeemAmt_error));
					return;
				}
				
				BaseHttpEngine.showProgressDialog();
				requestFundCompanyInfoQuery(foundCode);
			}
			break;
			// 赎回
		case R.id.nextButton:
			isExtrasDay = false;
			if (RegexpUtils.regexpDate(list)) {
				//赎回份额是否大于单日赎回上限验证
				
				//TODO 客户端还有个赎回份额 <= 单日赎回上限 是否需要保留，建议删除由后台控制
//				if(judgeLimit){
//					if(Double.valueOf(sellTotalValue) > Double
//							.valueOf(daySellLimit)){//高于当日赎回上限
//						BaseDroidApp.getInstanse().showInfoMessageDialog(
//								getString(R.string.finc_xpadManRedeemAmt_error));
//						return;
//					}
//				}
				if (saleFlag) {
					//TODO 当赎回份额等于持仓份额表示全额赎回，不进行控制；
					if (sellTotalValue.equals(totalAvailableBalance)) {
						BaseHttpEngine.showProgressDialog();
						requestFundCompanyInfoQuery(foundCode);
						return;
					}
					if (Double.parseDouble(sellTotalValue) == Double.parseDouble(totalAvailableBalance)) {
						BaseHttpEngine.showProgressDialog();
						requestFundCompanyInfoQuery(foundCode);
						return;
					}
					// 赎回份额小于等于客户当前持有的份额（可用份额）
					if(Double.parseDouble(sellTotalValue)>Double.parseDouble(totalAvailableBalance)){
						 BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_xpadMaxRedeemAmt_error));
						 return;
					 }
					// 跳转到确认信息页面
					if (Double.parseDouble(sellTotalValue) < Double
							.parseDouble(sellLowLimit)) {// 低于
						// 赎回下限
						BaseDroidApp
						.getInstanse()
						.showInfoMessageDialog(
								getString(R.string.finc_xpadMinRedeemAmt_error));
						return;
					}
					// 可用份额-赎回份额>=基金最低持有份额
					if (Double.parseDouble(totalAvailableBalance) - Double.parseDouble(sellTotalValue) 
							< Double.parseDouble(holdQutyLowLimit)) {
						BaseDroidApp
						.getInstanse()
						.showInfoMessageDialog(
								getString(R.string.finc_xpadMinHoldLowCountRedeemAmt_error));
						return;
					}

					BaseHttpEngine.showProgressDialog();
					requestFundCompanyInfoQuery(foundCode);
				}
			}
			break;
		default:
			break;
		}


	}


	@Override
	public void getFundDueDateQueryCallback(Object resultObj) {
		super.getFundDueDateQueryCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		fincControl.fincFundDueDateQuery =( List<Map<String, Object>>)biiResponseBody
				.getResult();
		//		if(fincControl.fincFundDueDateQuery != null){
		//			View dueDateView =initFundDueDate();
		//			setDateDue();
		//			BaseDroidApp.getInstanse().showAccountMessageDialog(dueDateView);
		//		}

		Intent intent =new Intent(this, FincFundDueDateListActivity.class);
		startActivity(intent);
	}

	@Override
	public void fundCompanyInfoQueryCallback(Object resultObj) {
		super.fundCompanyInfoQueryCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent;
		if(isExtrasDay){
			intent = new Intent(MyFincSellSubmitActivity.this,
					MyFincExtranDaySellActivity.class);
			// 页面显示的内容
			intent.putExtra(Finc.FINC_SELLAMOUNT_REQ, sellTotalValue);
			intent.putExtra(Finc.I_SELLTYPEVALUE, sellTypeValue);
			intent.putExtra(Finc.ISZISSALE, isZisSale);
			intent.putExtra(Finc.I_FUNDCODE, foundCode);
			startActivityForResult(intent, 1);
		}else{
			intent = new Intent(MyFincSellSubmitActivity.this,
					MyFincSellConfirmActivity.class);
			// 页面显示的内容
			intent.putExtra(Finc.FINC_SELLAMOUNT_REQ, sellTotalValue);
			intent.putExtra(Finc.I_SELLTYPEVALUE, sellTypeValue);
			saleFlag = false;
			startActivityForResult(intent, 1);
		}
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


	private void initbtnView(String isZisSale ) {
		if ("1".equals(isZisSale) || "2".equals(isZisSale)) {// 可以指定日期赎回

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				//                api>=16
				nextButton.setBackground(getResources().getDrawable(R.drawable.fund_btn_common_oneline_left_press_state));
				nextButton.setTextColor(getResources().getColor(R.color.red));
			} else {
				//                api<16
				nextButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.fund_btn_common_oneline_left_press_state));
				nextButton.setTextColor(getResources().getColor(R.color.red));
			}

			nextButton.setText(R.string.finc_today_deal);
			extrasDayButton.setVisibility(View.VISIBLE);
			nextButton.setVisibility(View.VISIBLE);
			ViewUtils.initBtnParamsTwoLeft(nextButton, this);
			ViewUtils.initBtnParamsTwoRight(extrasDayButton, this);
		} else {// 不可以
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				//                api>=16
				nextButton.setBackground(getResources().getDrawable(R.drawable.fund_button_press_state));
				nextButton.setTextColor(Color.WHITE);
			} else {
				//                api<16
				nextButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.fund_button_press_state));
				nextButton.setTextColor(Color.WHITE);
			}
			nextButton.setText(R.string.next);
//			ViewUtils.initBtnParams(nextButton,this);
			extrasDayButton.setVisibility(View.GONE);
		}
	}



}
