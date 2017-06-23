package com.chinamworld.bocmbci.biz.finc.myfund;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.FincUtils;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 我的基金---定期定额赎回填写页面
 * 
 * @author Administrator
 * 
 */
public class MyFincScheduSellActivity extends FincBaseActivity implements OnItemSelectedListener{
	private final String TAG = "MyFincScheduSellActivity";
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
	/** 当前市值 */
	private TextView currentCapitalisationText = null;
	/** 基金状态 */
	private TextView fundStateText = null;
	/** 赎回下限 */
	private TextView sellLowLimitText = null;
	/** 赎回份额 */
	private EditText sellTotalEdit = null;
	/** 巨额赎回方式 */
	private Spinner sellTypeSpinner = null;
	private Spinner dayInMonthSp = null;
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
	/** 可用份额 */
	private String totalAvailableBalance = null;
	/** 实际份额 */
	private String totalCount = null;
	/** 对私最低持有份额 */
	private String holdQutyLowLimit = null;
	/**
	 * 用户选择的赎回方式,基金卖出参数 0-不连续赎回(取消赎回) ，1-连续赎回(顺延赎回)
	 */
	// private String spinnerSelectedType = null;
	/** 0-不连续赎回(取消赎回) */
	// private String zero = null;
	// /** 1-连续赎回(顺延赎回) */
	// private String one = null;

	private String dayInMonthStr;

	/** P405*/
	/** 定投周期， 每周定投日期，结束条件*/
	private Spinner fincScheduledbuyPeriod, fincSaleDayOfWeekSp, fincScheduledbuySetEndTime;
	private TextView fincScheduledbuyEndTime;//结束日期
	private EditText fincScheduledbuyTotalDealCount, fincScheduledbuyTotalDealAmount;//累计成交份数，累计成交份额

	private String scheduledbuyPeriodFlag = "0";
	private String scheduledbuySetEndTimeFlag = "0";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestSystemDateTime();
	}
	
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		init();
		getViewValue();
		initOnClick();
	}


	/** 初始化控件 */
	private void init() {
		myFincView = mainInflater
				.inflate(R.layout.finc_myfinc_schedusell, null);
		tabcontent.addView(myFincView);
		setTitle(getResources().getString(
				R.string.finc_myfinc_scheduedsellFound));
		fincCodeText = (TextView) findViewById(R.id.finc_fincCode);
		fincNameText = (TextView) findViewById(R.id.finc_fincName);
		fincCurrencyText = (TextView) findViewById(R.id.finc_currency);
		netPriceText = (TextView) findViewById(R.id.finc_netPrice);
		totalCountText = (TextView) findViewById(R.id.finc_totalCount);
		currentCapitalisationText = (TextView) findViewById(R.id.finc_currentCapitalisation);
		fundStateText = (TextView) findViewById(R.id.finc_type);
		sellLowLimitText = (TextView) findViewById(R.id.finc_sellLowLimit);
		sellTotalEdit = (EditText) findViewById(R.id.finc_money);
		sellTypeSpinner = (Spinner) findViewById(R.id.finc_sellType);
		dayInMonthSp = (Spinner) findViewById(R.id.finc_dayInMonth_sp);
		nextButton = (Button) findViewById(R.id.nextButton);
		StepTitleUtils.getInstance().initTitldStep(this,
				fincControl.getStepsForSell());
		StepTitleUtils.getInstance().setTitleStep(1);
		// zero = LocalData.fundSellDealTypeCodeList.get(1);
		// one = LocalData.fundSellDealTypeCodeList.get(0);
		initRightBtnForMain();
		TextView finc_myfinc_sell_sellType = (TextView) findViewById(R.id.finc_myfinc_sell_sellType);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_myfinc_sell_sellType);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fincNameText);
		
		fincScheduledbuyPeriod = (Spinner)findViewById(R.id.fincScheduledbuyPeriod);
		fincSaleDayOfWeekSp = (Spinner)findViewById(R.id.fincSaleDayOfWeekSp);
		fincScheduledbuySetEndTime = (Spinner)findViewById(R.id.fincScheduledbuySetEndTime);
		fincScheduledbuyEndTime = (TextView)findViewById(R.id.fincScheduledbuyEndTime);
		fincScheduledbuyEndTime.setText(QueryDateUtils.getcurrentDate(dateTime));
		fincScheduledbuyTotalDealCount = (EditText)findViewById(R.id.fincScheduledbuyTotalDealCount);
		fincScheduledbuyTotalDealAmount = (EditText)findViewById(R.id.fincScheduledbuyTotalDealAmount);
		FincUtils.initSpinnerView(this, fincScheduledbuyPeriod, LocalData.transCycleMap);
		FincUtils.initSpinnerView(this, fincSaleDayOfWeekSp, new ArrayList<String>() {
			{
				add("周一");
				add("周二");
				add("周三");
				add("周四");
				add("周五");
			}
		});
		FincUtils.initSpinnerView(this, fincScheduledbuySetEndTime, LocalData.dsEndFlagMap);
		fincScheduledbuyEndTime.setOnClickListener(fincChooseDateClick);
		fincScheduledbuyPeriod.setOnItemSelectedListener(this);
		fincScheduledbuySetEndTime.setOnItemSelectedListener(this);
	}

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
			
			foundCode = (String)fundInfoMap.get(Finc.FINC_FUNDCODE_REQ);
			foundName = (String)fundInfoMap.get(Finc.FINC_FUNDNAME_REQ);
			tradeCurrencyStr = (String)fundInfoMap.get(Finc.FINC_CURRENCY_REQ);
			StringUtil.setInPutValueByCurrency(sellTotalEdit, tradeCurrencyStr);
			String cashFlagCode = (String)fundInfoMap.get(Finc.FINC_CASHFLAG);
			String netPrice = (String)fundInfoMap.get(Finc.FINC_NETPRICE_REQ);
			totalAvailableBalance = (String) (fundBalanceMap
					.get(Finc.FINC_TOTALAVAILABLEBALANCE) != null ? (String) fundBalanceMap
					.get(Finc.FINC_TOTALAVAILABLEBALANCE) : "0");
			totalCount = (String) fundBalanceMap
					.get(Finc.FINC_SELLALLLIMIT) != null ? (String) fundBalanceMap
							.get(Finc.FINC_SELLALLLIMIT) : "0";
			String sellLowLimit = (String)fundInfoMap.get(Finc.FINC_SELLLOWLIMIT_REQ);
			holdQutyLowLimit = (String)fundInfoMap.get(Finc.FINC_FASTFUNDSELL_HOLD_LOW_LIMIT);
			String fundState = (String)fundInfoMap.get(Finc.FINC_FUNDSTATE_REQ);
//			foundCode = String.valueOf(fundBalanceMap.get(Finc.FINC_FUNDCODE_REQ));
//			foundName = String.valueOf(fundBalanceMap.get(Finc.FINC_FUNDNAME_REQ));
//			tradeCurrencyStr = String.valueOf(fundBalanceMap.get(Finc.FINC_CURRENCY_REQ));
//			StringUtil.setInPutValueByCurrency(sellTotalEdit, tradeCurrencyStr);
//			String cashFlagCode = String.valueOf(fundBalanceMap.get(Finc.FINC_CASHFLAG));
//			String netPrice = String.valueOf(fundBalanceMap.get(Finc.FINC_NETPRICE_REQ));
//			String totalCount = (String) (fundBalanceMap
//					.get(Finc.FINC_TOTALAVAILABLEBALANCE) != null ? (String) fundBalanceMap
//					.get(Finc.FINC_TOTALAVAILABLEBALANCE) : "0");
//			String sellLowLimit = String.valueOf(fundBalanceMap.get(Finc.FINC_SELLLOWLIMIT_REQ));
//			String fundState = String.valueOf(fundBalanceMap.get(Finc.FINC_FUNDSTATE_REQ));
//			;
			
			fincCodeText.setText(foundCode);
			fincNameText.setText(foundName);
			fincCurrencyText.setText(FincControl.fincCurrencyAndCashFlag(
					tradeCurrencyStr, cashFlagCode));
			netPriceText.setText(StringUtil.parseStringPattern(netPrice, 4));
			totalCountText
					.setText(StringUtil.parseStringPattern(totalCount, 2));
			currentCapitalisationText.setText(currentCapitalisation);
			fundStateText.setText(String.valueOf(LocalData.fincFundStateMap
					.get(fundState)));
			sellLowLimitText.setText(StringUtil.parseStringPattern(
					sellLowLimit, 2));
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					R.layout.dept_spinner, LocalData.fundSellDealTypeList);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sellTypeSpinner.setAdapter(adapter);

			ArrayAdapter<String> dayInMonthAdapter = new ArrayAdapter<String>(
					this, R.layout.dept_spinner, getDayInMonth());
			dayInMonthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			dayInMonthSp.setAdapter(dayInMonthAdapter);
			setOcrmTradeAmount();
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

	private void initOnClick() {
		dayInMonthSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int arg2, long arg3) {
				dayInMonthStr = getDayInMonth().get(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
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
		// 下一步
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sellTotalValue = sellTotalEdit.getText().toString().trim();
				if("".equals(sellTotalValue) || sellTotalValue == null){
					BaseDroidApp.getInstanse().showInfoMessageDialog("请输入交易份额");
					return ;
				}
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				RegexpBean rgb;
				if (ConstantGloble.FOREX_JPY1.equals(tradeCurrencyStr) 
						|| ConstantGloble.FOREX_JPY2.equals(tradeCurrencyStr)
						|| ConstantGloble.FOREX_KRW1.equals(tradeCurrencyStr)
						|| ConstantGloble.FOREX_KRW2.equals(tradeCurrencyStr)) {
					rgb = new RegexpBean("", sellTotalValue, "jpnAmount601");
				} else {
					rgb = new RegexpBean("", sellTotalValue, "fincprice601");
				}
				lists.add(rgb);
				switch (Integer.parseInt(scheduledbuySetEndTimeFlag)) {
				case 2:
					RegexpBean countReg = new RegexpBean(
							FincUtils
									.getNoColonStr(getString(R.string.finc_scheduledbuy_total_deal_count)),
							fincScheduledbuyTotalDealCount.getText().toString(),
							"totleCount");
					lists.add(countReg);
					break;
				case 3:
					RegexpBean amountReg = StringUtil.getRegexBeanByCurrency(
							FincUtils.getNoColonStr(getString(
									R.string.finc_scheduledbuy_total_deal_share)),
							fincScheduledbuyTotalDealAmount.getText().toString(),
							tradeCurrencyStr);
					lists.add(amountReg);
					break;
				}
				if (RegexpUtils.regexpDate(lists)) {
					if(Integer.parseInt(scheduledbuySetEndTimeFlag) == 1){
						if(!QueryDateUtils.compareDate(dateTime, fincScheduledbuyEndTime.getText().toString())){
							BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_scheduled_date_rege));
							return;
						}
					}
					if (Double.valueOf(sellTotalValue) % 100 != 0) { // 交易份额是100的整数倍
						BaseDroidApp
						.getInstanse()
						.showInfoMessageDialog(
								getString(R.string.finc_xpadMaxRedeemAmt_100_error));
						return;
					}
					
					// TODO 当赎回份额等于持仓份额表示全额赎回，不进行控制；
//					if (Double.valueOf(sellTotalValue) == Double.valueOf(totalCount)) {
//						if (sellTotalValue.equals(totalCount)) {
//							BaseHttpEngine.showProgressDialog();
//							requestFundCompanyInfoQuery(foundCode);
//							return;
//						}
//					}
					// 赎回份额小于等于客户当前持有的份额
					if(Double.valueOf(sellTotalValue)>Double.valueOf(totalAvailableBalance)){
						 BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_xpadScheduMaxRedeemAmt_error));
						 return;
					 }
					
					// 不小于赎回下限
					if(Double.valueOf(sellTotalValue)<Double.valueOf(sellLowLimitText.getText().toString())){
						 BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_secheduedSell_lowLimit));
						 return;
					}
					// TODO 修改提示语
					if (Double.valueOf(totalCount) - Double.valueOf(sellTotalValue) 
							< Double.valueOf(holdQutyLowLimit)) { // 可用份额-赎回份额>=基金最低持有份额。
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
		});
	}
	
	
	@Override
	public void fundCompanyInfoQueryCallback(Object resultObj) {
		super.fundCompanyInfoQueryCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		// 跳转到确认信息页面
		Intent intent = new Intent(MyFincScheduSellActivity.this,
				MyFincScheduleSellConfirmActivity.class);
		// 页面显示的内容
		intent.putExtra(Finc.FINC_SELLAMOUNT_REQ, sellTotalValue);
		intent.putExtra(Finc.I_DAYINMONTH, dayInMonthStr);
		intent.putExtra(Finc.I_SELLTYPEVALUE, sellTypeValue);
		
		
		intent.putExtra(Finc.TRANSCYCLE, scheduledbuyPeriodFlag);// 定投周期
		intent.putExtra(Finc.PAYMENTDATEOFWEEK, fincSaleDayOfWeekSp.getSelectedItem().toString());// 每周定投日
		intent.putExtra(Finc.ENDFLAG, scheduledbuySetEndTimeFlag);// 结束条件
		intent.putExtra(Finc.FUNDPOINTENDDATE, fincScheduledbuyEndTime.getText().toString());// 结束日期
		intent.putExtra(Finc.ENDSUM, fincScheduledbuyTotalDealCount.getText().toString());// 成交份额
		intent.putExtra(Finc.FUNDPOINTENDAMOUNT, fincScheduledbuyTotalDealAmount.getText().toString());// 成交金额

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

	/**
	 * 一个月中的天
	 * 
	 * @return
	 */
	private List<String> getDayInMonth() {
		List<String> list = new ArrayList<String>();
		String iS;
		for (int i = 1; i <= 28; i++) {
			if (i < 10) {
				iS = "0" + i;
			} else {
				iS = String.valueOf(i);
			}
			list.add(iS);
		}
		return list;

	}
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		switch (arg0.getId()) {
		case R.id.fincScheduledbuyPeriod:
			scheduledbuyPeriodFlag = FincUtils.getKeyByValue(
					LocalData.transCycleMap, fincScheduledbuyPeriod
					.getSelectedItem().toString());
			switch (Integer.parseInt(scheduledbuyPeriodFlag)) {
			case 0:
				findViewById(R.id.finc_saleDayOfMonth_ll).setVisibility(View.VISIBLE);
				findViewById(R.id.finc_saleDayOfWeek_ll).setVisibility(View.GONE);
				break;
			case 1:
				findViewById(R.id.finc_saleDayOfMonth_ll).setVisibility(View.GONE);
				findViewById(R.id.finc_saleDayOfWeek_ll).setVisibility(View.VISIBLE);
				break;
			}
			break;
		case R.id.fincScheduledbuySetEndTime:
			scheduledbuySetEndTimeFlag = FincUtils.getKeyByValue(
					LocalData.dsEndFlagMap, fincScheduledbuySetEndTime
					.getSelectedItem().toString());
			switch (Integer.parseInt(scheduledbuySetEndTimeFlag)) {
			case 0:
				findViewById(R.id.fincScheduledbuyEndTimeLl).setVisibility(View.GONE);
				findViewById(R.id.fincScheduledbuyTotalDealCountLl).setVisibility(View.GONE);
				findViewById(R.id.fincScheduledbuyTotalDealAmountLl).setVisibility(View.GONE);
				break;
			case 1:
				findViewById(R.id.fincScheduledbuyEndTimeLl).setVisibility(View.VISIBLE);
				findViewById(R.id.fincScheduledbuyTotalDealCountLl).setVisibility(View.GONE);
				findViewById(R.id.fincScheduledbuyTotalDealAmountLl).setVisibility(View.GONE);
				break;
			case 2:
				findViewById(R.id.fincScheduledbuyEndTimeLl).setVisibility(View.GONE);
				findViewById(R.id.fincScheduledbuyTotalDealCountLl).setVisibility(View.VISIBLE);
				findViewById(R.id.fincScheduledbuyTotalDealAmountLl).setVisibility(View.GONE);
				break;
			case 3:
				findViewById(R.id.fincScheduledbuyEndTimeLl).setVisibility(View.GONE);
				findViewById(R.id.fincScheduledbuyTotalDealCountLl).setVisibility(View.GONE);
				findViewById(R.id.fincScheduledbuyTotalDealAmountLl).setVisibility(View.VISIBLE);
				break;
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
