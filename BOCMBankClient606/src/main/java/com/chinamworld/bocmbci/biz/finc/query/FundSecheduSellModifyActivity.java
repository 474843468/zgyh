package com.chinamworld.bocmbci.biz.finc.query;

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
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 基金 定期定额赎回修改 页面
 * 
 */
public class FundSecheduSellModifyActivity extends FincBaseActivity implements
		OnItemSelectedListener {
	
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
	private Button nextButton;
	
	/** 基金代码 */
	private String foundCode = null;
	/** 基金名称 */
	private String foundName = null;
	private String tradeCurrencyStr;
	private String dayInMonthStr;
	/** 用户输入的份额 */
	private String sellTotalValue = null;
	/** 用户选择的赎回方式,方式名称 */
	private String sellTypeValue = null;
	
	
	
	/** P405*/
	/** 定投周期， 每周定投日期，结束条件*/
	private TextView fincScheduledbuyPeriod;
	private Spinner  fincSaleDayOfWeekSp, fincScheduledbuySetEndTime;
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
		Intent intent = getIntent();
		String fundCodeStr = intent.getStringExtra(Finc.I_FUNDCODE);
		if(!StringUtil.isNull(Finc.I_FUNDCODE)){
			requestPsnFundQueryFundBalance(fundCodeStr);
		}else{
			finish();
		}

	}
	
	public void requestPsnFundQueryFundBalanceCallback(Object resultObj) {
		super.requestPsnFundQueryFundBalanceCallback(resultObj);
		Map<String,Object> resultMap = HttpTools.getResponseResult(resultObj);
		List<Map<String,Object>> array = (List<Map<String,Object>>)resultMap.get("fundBalance");
		 
		if(array != null && !array.isEmpty()){
			fincControl.fundBalancMap =  array.get(0);
		}
		
//		fincControl.fundBalancMap = resultMap;
		BaseHttpEngine.dissMissProgressDialog();
		init();
		initData();
		initOnClick();
	}

	/**
	 * 初始化布局
	 */
	private void init() {
		myFincView = mainInflater.inflate(R.layout.fund_sechedu_sell_modify,
				null);
		tabcontent.addView(myFincView);
		StepTitleUtils.getInstance().initTitldStep(this,
				fincControl.getStepsForDingTou());
		StepTitleUtils.getInstance().setTitleStep(2);
		setTitle(R.string.finc_title_scheduedit_sell);
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
		nextButton.setOnClickListener(this);
		initRightBtnForMain();
		TextView finc_myfinc_sell_sellType = (TextView) findViewById(R.id.finc_myfinc_sell_sellType);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_myfinc_sell_sellType);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fincNameText);
		
		fincScheduledbuyPeriod = (TextView)findViewById(R.id.fincScheduledbuyPeriod);
		fincSaleDayOfWeekSp = (Spinner)findViewById(R.id.fincSaleDayOfWeekSp);
		fincScheduledbuySetEndTime = (Spinner)findViewById(R.id.fincScheduledbuySetEndTime);
		fincScheduledbuyEndTime = (TextView)findViewById(R.id.fincScheduledbuyEndTime);
		fincScheduledbuyEndTime.setText(QueryDateUtils.getcurrentDate(dateTime));
		fincScheduledbuyTotalDealCount = (EditText)findViewById(R.id.fincScheduledbuyTotalDealCount);
		fincScheduledbuyTotalDealAmount = (EditText)findViewById(R.id.fincScheduledbuyTotalDealAmount);
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
		fincScheduledbuySetEndTime.setOnItemSelectedListener(this);		
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		if(!StringUtil.isNullOrEmpty(fincControl.fundScheduQueryMap)){
			Map<String, Object> fundBalanceMap = fincControl.fundScheduQueryMap;
			Map<String, Object> fundInfoMap = (Map<String, Object>) fundBalanceMap
					.get(Finc.FINC_FUNDQUERYDQDT_FUNDINFO);
			//参考市值 BigDecimal
			String currentCapitalisationStr = String.valueOf((fincControl.fundBalancMap
					.get(Finc.FINC_CURRENTCAPITALISATION_REQ)));
			String currentCapitalisation = StringUtil.valueOf1(currentCapitalisationStr);
			foundCode = (String)fundInfoMap.get(Finc.FINC_FUNDCODE_REQ);
			foundName = (String)fundInfoMap.get(Finc.FINC_FUNDNAME_REQ);
			tradeCurrencyStr = (String)fundInfoMap.get(Finc.FINC_CURRENCY_REQ);
			StringUtil.setInPutValueByCurrency(sellTotalEdit, tradeCurrencyStr);
			String cashFlagCode = (String)fundInfoMap.get(Finc.FINC_CASHFLAG);
			String netPrice = (String)fundInfoMap.get(Finc.FINC_NETPRICE_REQ);
			//可用份额  fundBalanceMap
			String totalCount = (String) (fincControl.fundBalancMap
					.get(Finc.FINC_TOTALAVAILABLEBALANCE) != null ? (String) fincControl.fundBalancMap
					.get(Finc.FINC_TOTALAVAILABLEBALANCE) : "0");
			if(!StringUtil.isNullOrEmpty(fincControl.scheduledBuySellDetail)){
				scheduledbuyPeriodFlag = (String)fincControl.scheduledBuySellDetail.get(Finc.SELL_CYCLE);
				fincScheduledbuyPeriod.setText(StringUtil.valueOf1((String)LocalData.transCycleMap.get(scheduledbuyPeriodFlag)));
			}
			switch (Integer.parseInt(scheduledbuyPeriodFlag)) {
			case 0:
				findViewById(R.id.finc_saleDayOfMonth_ll).setVisibility(
						View.VISIBLE);
				findViewById(R.id.finc_saleDayOfWeek_ll).setVisibility(
						View.GONE);
				break;
			case 1:
				findViewById(R.id.finc_saleDayOfMonth_ll).setVisibility(
						View.GONE);
				findViewById(R.id.finc_saleDayOfWeek_ll).setVisibility(
						View.VISIBLE);
				break;
			default:
				break;
			}
			String sellLowLimit = (String)fundInfoMap.get(Finc.FINC_SELLLOWLIMIT_REQ);
			String fundState = (String)fundInfoMap.get(Finc.FINC_FUNDSTATE_REQ);
			fincCodeText.setText(foundCode);
			fincNameText.setText(foundName);
			fincCurrencyText.setText(FincControl.fincCurrencyAndCashFlag(
					tradeCurrencyStr, cashFlagCode));
			netPriceText.setText(StringUtil.parseStringPattern(netPrice, 4));
			//可用份额
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
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				RegexpBean regexsaleAmoundStr = StringUtil
						.getRegexBeanByCurrency(
								FincUtils.getNoColonStr(getString(R.string.fincn_transAmount_colon)),
								sellTotalValue, tradeCurrencyStr);
				lists.add(regexsaleAmoundStr);
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
		Intent intent = new Intent(this,
				FundSecheduSellModifyConfirmActivity.class);
		
		
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
//		case R.id.fincScheduledbuyPeriod:
//			scheduledbuyPeriodFlag = FincUtils.getKeyByValue(
//					LocalData.transCycleMap, fincScheduledbuyPeriod
//					.getSelectedItem().toString());
//			switch (Integer.parseInt(scheduledbuyPeriodFlag)) {
//			case 0:
//				findViewById(R.id.finc_saleDayOfMonth_ll).setVisibility(View.VISIBLE);
//				findViewById(R.id.finc_saleDayOfWeek_ll).setVisibility(View.GONE);
//				break;
//			case 1:
//				findViewById(R.id.finc_saleDayOfMonth_ll).setVisibility(View.GONE);
//				findViewById(R.id.finc_saleDayOfWeek_ll).setVisibility(View.VISIBLE);
//				break;
//			}
//			break;
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
