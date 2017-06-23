package com.chinamworld.bocmbci.biz.finc.query;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.FincUtils;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.MathUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 基金 定期定额申购修改 页面
 * 
 * 
 */
public class FundSecheduBuyModifyActivity extends FincBaseActivity implements
		OnItemSelectedListener {

	/**
	 * 基金公司下拉列表
	 */
	private Spinner fundCompanySpinner;
	/**
	 * 基金代码下拉列表
	 */
	private Spinner fundCodeSpinner;
	// 详情中进入定投
	/** 基金公司Tv */
	private TextView fundCompanyTextView;
	/** 基金代码Tv */
	private TextView fundCodeTextView;
	/** 单位净值Tv */
	private TextView netPriceTextView;
	/** 产品风险等级Tv */
	private TextView productRiskLevelTextView;
	/** 收费方式Tv */
	private TextView feeTypeTextView;
	/** 定投下限Tv */
	private TextView lowLimitTextView;
	/** 交易币种Tv */
	private TextView tradeCurrencyTextView;
	/** 每月申购日期 */
	private Spinner saleDayOfMonthSp;
	/**
	 * 申购金额 输入框
	 */
	private EditText saleAmountEditText;
	/**
	 * 下一步按钮
	 */
	private Button nextBtn;

	// 页面显示的值
	private String fundCompanyStr;
	private String fundCodeStr;
	private String fundNameStr;
	private String fundTransTypeStr;
	private String netPriceStr;
	private String productRiskLevelStr;
	private String feeTypeStr;
	private String tradeCurrencyStr, cashFlagCode;
	private String fundStateStr;
	private String lowLimitStr;
	private String dayInMonthStr;
	/**
	 * 申购金额
	 */
	private String saleAmoundStr;
	/**
	 * 根据基金代码查询的详情
	 */
	private List<Map<String, Object>> fundList;
	private List<String> fundCodeStrList;
	private List<String> fundNameStrList;

	private String canScheduBuy;
	private LinearLayout fundCodell;
	private LinearLayout fundNamell;
	private TextView fundNameTv;
	private LinearLayout fundnameandcodell;

	/** P405 */
	/** 定投周期， 每周定投日期，结束条件 */
	private TextView fincScheduledbuyPeriod;
	private Spinner fincSaleDayOfWeekSp, fincScheduledbuySetEndTime;
	private TextView fincScheduledbuyEndTime;// 结束日期
	private EditText fincScheduledbuyTotalDealCount,
			fincScheduledbuyTotalDealAmount;// 累计成交份数，累计成交份额

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
		initData();
	}

	/**
	 * 初始化布局
	 */
	private void init() {
		View childView = mainInflater.inflate(R.layout.fund_sechedu_buy_modify,
				null);
		tabcontent.addView(childView);
		StepTitleUtils.getInstance().initTitldStep(this,
				fincControl.getStepsForDingTou());
		StepTitleUtils.getInstance().setTitleStep(2);
		setTitle(R.string.finc_title_scheduedit_buy);
		TextView fincLowLimitTitleView = (TextView) childView
				.findViewById(R.id.finc_lowlimit_titleView);
		fundCompanyTextView = (TextView) childView
				.findViewById(R.id.finc_fundcompany_textView);
		netPriceTextView = (TextView) childView
				.findViewById(R.id.finc_netvalue_textView);
		productRiskLevelTextView = (TextView) childView
				.findViewById(R.id.finc_productrisklevel_textView);
		feeTypeTextView = (TextView) childView
				.findViewById(R.id.finc_feetype_textView);
		tradeCurrencyTextView = (TextView) childView
				.findViewById(R.id.finc_tradecurrency_textView);
		fundCompanySpinner = (Spinner) childView
				.findViewById(R.id.finc_fundcompany_spinner);
		/** 可能没有 */
		fundCodeSpinner = (Spinner) childView
				.findViewById(R.id.finc_fundnameandcode_spinner);
		fundCodeTextView = (TextView) childView
				.findViewById(R.id.finc_fundcode_tv);
		fundNameTv = (TextView) childView.findViewById(R.id.finc_fundname_tv);
		lowLimitTextView = (TextView) childView
				.findViewById(R.id.finc_saleLowlimit_textView);
		saleAmountEditText = (EditText) childView
				.findViewById(R.id.finc_saleamount_editText);
		saleDayOfMonthSp = (Spinner) childView
				.findViewById(R.id.finc_saleDayOfMonth_Sp);
		fundCodell = (LinearLayout) childView
				.findViewById(R.id.finc_fundcode_ll);
		fundNamell = (LinearLayout) childView
				.findViewById(R.id.finc_fundname_ll);
		fundnameandcodell = (LinearLayout) childView
				.findViewById(R.id.finc_fundnameandcode_ll);
		nextBtn = (Button) childView.findViewById(R.id.finc_next);
		nextBtn.setOnClickListener(this);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.dept_spinner, getDayInMonth());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		saleDayOfMonthSp.setAdapter(adapter);
		saleDayOfMonthSp.setOnItemSelectedListener(this);
		initRightBtnForMain();
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				fundCompanyTextView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				fundNameTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				fincLowLimitTitleView);

		fincScheduledbuyPeriod = (TextView) findViewById(R.id.fincScheduledbuyPeriod);
		fincSaleDayOfWeekSp = (Spinner) findViewById(R.id.fincSaleDayOfWeekSp);
		fincScheduledbuySetEndTime = (Spinner) findViewById(R.id.fincScheduledbuySetEndTime);
		fincScheduledbuyEndTime = (TextView) findViewById(R.id.fincScheduledbuyEndTime);
		fincScheduledbuyEndTime
				.setText(QueryDateUtils.getcurrentDate(dateTime));
		fincScheduledbuyTotalDealCount = (EditText) findViewById(R.id.fincScheduledbuyTotalDealCount);
		fincScheduledbuyTotalDealAmount = (EditText) findViewById(R.id.fincScheduledbuyTotalDealAmount);
		FincUtils.initSpinnerView(this, fincSaleDayOfWeekSp,
				new ArrayList<String>() {
					{
						add("周一");
						add("周二");
						add("周三");
						add("周四");
						add("周五");
					}
				});
		FincUtils.initSpinnerView(this, fincScheduledbuySetEndTime,
				LocalData.dtEndFlagMap);
		fincScheduledbuyEndTime.setOnClickListener(fincChooseDateClick);
		fincScheduledbuySetEndTime.setOnItemSelectedListener(this);
		detailInit();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		if (!StringUtil.isNullOrEmpty(fincControl.fundScheduQueryMap)) {
			detailInit();
			Map<String, Object> fundInfoMap = (Map<String, Object>) fincControl.fundScheduQueryMap
					.get(Finc.FINC_FUNDQUERYDQDT_FUNDINFO);
			fundCompanyStr = (String) fundInfoMap
					.get(Finc.FINC_FUNDCOMPANYNAME);
			fundCodeStr = (String) fincControl.fundScheduQueryMap
					.get(Finc.FINC_FUNDCODE);
			fundNameStr = (String) fincControl.fundScheduQueryMap
					.get(Finc.FINC_FUNDNAME);
			fundTransTypeStr = (String) fincControl.fundScheduQueryMap
					.get(Finc.FINC_FUNDQUERYDQDT_TRANSTYPE);
			netPriceStr = (String) fundInfoMap.get(Finc.FINC_NETPRICE);
			productRiskLevelStr = (String) fundInfoMap.get(Finc.FINC_RISKLV);
			feeTypeStr = (String) fundInfoMap.get(Finc.FINC_FEETYPE);
			fundStateStr = (String) fundInfoMap.get(Finc.FINC_FUNDSTATE);
			tradeCurrencyStr = (String) fundInfoMap.get(Finc.FINC_CURRENCY);
			StringUtil.setInPutValueByCurrency(saleAmountEditText,
					tradeCurrencyStr);
			cashFlagCode = (String) fundInfoMap.get(Finc.FINC_CASHFLAG);
			lowLimitStr = (String) fundInfoMap
					.get(Finc.FINC_SCHEDULEAPPLYLOWLIMIT);
			canScheduBuy = String.valueOf(fundInfoMap.get(Finc.CANSCHEDULEBUY));
			if (StringUtil.isNullOrEmpty(lowLimitStr)) {
				lowLimitStr = "-";
			}
			fundnameandcodell.setVisibility(View.GONE);
			fundCodell.setVisibility(View.VISIBLE);
			fundNamell.setVisibility(View.VISIBLE);
			fundCompanyTextView.setText(fundCompanyStr);
			fundCodeTextView.setText(fundCodeStr);
			fundNameTv.setText(fundNameStr);
			netPriceTextView.setText(StringUtil.parseStringPattern(netPriceStr,
					4));
			productRiskLevelTextView
					.setText(LocalData.fincRiskLevelCodeToStrFUND
							.get(productRiskLevelStr));
			feeTypeTextView.setText(LocalData.fundfeeTypeCodeToStr
					.get(feeTypeStr));
			tradeCurrencyTextView.setText(FincControl.fincCurrencyAndCashFlag(
					tradeCurrencyStr, cashFlagCode));
			if (lowLimitStr.equals("-")) {
				lowLimitTextView.setText(lowLimitStr);
			} else {
				lowLimitTextView.setText(StringUtil.parseStringCodePattern(
						tradeCurrencyStr, lowLimitStr, 2));
			}
			if(!StringUtil.isNullOrEmpty(fincControl.scheduledBuySellDetail)){
				scheduledbuyPeriodFlag = (String)fincControl.scheduledBuySellDetail.get(Finc.APPLY_CYCLE);
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

		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				fundCompanyTextView);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_next:// 下一步
			saleAmoundStr = StringUtil.trim(saleAmountEditText.getText()
					.toString());
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			RegexpBean regexsaleAmoundStr = StringUtil.getRegexBeanByCurrency(
					getResources().getString(R.string.forex_emg_money),
					saleAmoundStr, tradeCurrencyStr);

			lists.add(regexsaleAmoundStr);
			switch (Integer.parseInt(scheduledbuySetEndTimeFlag)) {
			case 2: // 累计交易次数
				RegexpBean countReg = new RegexpBean(
						FincUtils
								.getNoColonStr(getString(R.string.finc_scheduledbuy_total_deal_count)),
						fincScheduledbuyTotalDealCount.getText().toString(),
						"totleCount");
				lists.add(countReg);
				break;
			case 3: // 累计交易金额
				RegexpBean amountReg = StringUtil
						.getRegexBeanByCurrency(
								FincUtils
										.getNoColonStr(getString(R.string.finc_scheduledbuy_total_deal_amount)),
								fincScheduledbuyTotalDealAmount.getText()
										.toString(), tradeCurrencyStr);
				lists.add(amountReg);
				break;
			}
			if (RegexpUtils.regexpDate(lists)) {
				if (Integer.parseInt(scheduledbuySetEndTimeFlag) == 1) {
					if (!QueryDateUtils.compareDate(dateTime,
							fincScheduledbuyEndTime.getText().toString())) {
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								getString(R.string.finc_scheduled_date_rege));
						return;
					}
				}
				if (StringUtil.parseStrToBoolean(canScheduBuy)) {

					if (!lowLimitStr.equals("-")) {
						BigDecimal saleAmountBig = new BigDecimal(
								saleAmoundStr.toCharArray());
						BigDecimal lowLimitBig = new BigDecimal(
								lowLimitStr.toCharArray());
						if (saleAmountBig.compareTo(lowLimitBig) == -1) {// 定投数量小于定投下限
							BaseDroidApp
									.getInstanse()
									.showInfoMessageDialog(
											getString(R.string.finc_secheduedBuy_lowLimit));
							return;
						}
						if (!MathUtils.isMultiple(
								Double.parseDouble(saleAmoundStr), 0, 100)) {
							BaseDroidApp
									.getInstanse()
									.showInfoMessageDialog(
											getString(R.string.finc_scheduled_amount_times100));
							break;
						}
					}
					BaseHttpEngine.showProgressDialog();
					requestFundCompanyInfoQuery(fundCodeStr);
				} else {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getString(R.string.finc_secheduedbuy_error));
					return;
				}
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void fundCompanyInfoQueryCallback(Object resultObj) {
		super.fundCompanyInfoQueryCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent();
		intent.setClass(this, FundSecheduBuyModifyConfirmActivity.class);
		intent.putExtra(Finc.I_FUNDCODE, fundCodeStr);
		intent.putExtra(Finc.I_FUNDNAME, fundNameStr);
		intent.putExtra(Finc.I_NETPRICE, netPriceStr);
		intent.putExtra(Finc.I_RISKLEVEL, productRiskLevelStr);
		intent.putExtra(Finc.I_FEETYPE, feeTypeStr);
		intent.putExtra(Finc.I_SCHEDULEDBUYLOWLIMTE, lowLimitStr);
		intent.putExtra(Finc.I_CURRENCYCODE, tradeCurrencyStr);
		intent.putExtra(Finc.I_CASHFLAG, cashFlagCode);
		intent.putExtra(Finc.I_DAYINMOUNTH, dayInMonthStr);
		intent.putExtra(Finc.I_SALEAMOUNT, saleAmoundStr);

		intent.putExtra(Finc.I_TRANSTYPE, fundTransTypeStr);

		intent.putExtra(Finc.I_FUNDSTATE, fundStateStr);// 成功页面显示

		intent.putExtra(Finc.TRANSCYCLE, scheduledbuyPeriodFlag);// 定投周期
		intent.putExtra(Finc.PAYMENTDATEOFWEEK, fincSaleDayOfWeekSp
				.getSelectedItem().toString());// 每周定投日
		intent.putExtra(Finc.ENDFLAG, scheduledbuySetEndTimeFlag);// 结束条件
		intent.putExtra(Finc.FUNDPOINTENDDATE, fincScheduledbuyEndTime
				.getText().toString());// 结束日期
		intent.putExtra(Finc.ENDSUM, fincScheduledbuyTotalDealCount.getText()
				.toString());// 成交份额
		intent.putExtra(Finc.FUNDPOINTENDAMOUNT,
				fincScheduledbuyTotalDealAmount.getText().toString());// 成交金额
		startActivityForResult(intent, 1);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.finc_fundnameandcode_spinner:// 基金代码列表
			if (!StringUtil.isNullOrEmpty(fundNameStrList)
					&& !StringUtil.isNullOrEmpty(fundCodeStrList)) {// 如果列表显示不为空
				fundCodeStr = fundCodeStrList.get(position);
				fundNameStr = fundNameStrList.get(position);
				Map<String, Object> map = fundList.get(position);// 初始化布局数据
				netPriceStr = (String) map.get(Finc.FINC_NETPRICE);
				productRiskLevelStr = (String) map.get(Finc.FINC_RISKLV);
				if (StringUtil.isNullOrEmpty(productRiskLevelStr)) {
					// 如果返回为kong
					productRiskLevelStr = "1";
				}
				// Map<String, Object> fundInfoMap = (Map<String, Object>) map
				// .get(Finc.FINC_FUNDINFO);
				feeTypeStr = String.valueOf(map.get(Finc.FINC_FEETYPE));
				fundStateStr = String.valueOf(map.get(Finc.FINC_FUNDSTATE));
				tradeCurrencyStr = String.valueOf(map.get(Finc.FINC_CURRENCY));
				StringUtil.setInPutValueByCurrency(saleAmountEditText,
						tradeCurrencyStr);
				cashFlagCode = (String) map.get(Finc.FINC_CASHFLAG);
				lowLimitStr = String.valueOf(map
						.get(Finc.FINC_SCHEDULEAPPLYLOWLIMIT));
				if (lowLimitStr.equals("null")) {
					lowLimitTextView.setText("-");
				} else {
					lowLimitTextView.setText(StringUtil.parseStringCodePattern(
							tradeCurrencyStr, lowLimitStr, 2));
				}
				canScheduBuy = String.valueOf(map.get(Finc.CANSCHEDULEBUY));
				netPriceTextView.setText(StringUtil.parseStringPattern(
						netPriceStr, 4));
				productRiskLevelTextView
						.setText(LocalData.fincRiskLevelCodeToStrFUND
								.get(productRiskLevelStr));
				feeTypeTextView.setText(LocalData.fundfeeTypeCodeToStr
						.get(feeTypeStr));
				tradeCurrencyTextView
						.setText(FincControl.fincCurrencyAndCashFlag(
								tradeCurrencyStr, cashFlagCode));
			}
			break;

		case R.id.finc_saleDayOfMonth_Sp:// 一个月中的那一天
			dayInMonthStr = getDayInMonth().get(position);
			break;
//		case R.id.fincScheduledbuyPeriod:
//			scheduledbuyPeriodFlag = FincUtils.getKeyByValue(
//					LocalData.transCycleMap, fincScheduledbuyPeriod
//							.getSelectedItem().toString());
//			switch (Integer.parseInt(scheduledbuyPeriodFlag)) {
//			case 0:
//				findViewById(R.id.finc_saleDayOfMonth_ll).setVisibility(
//						View.VISIBLE);
//				findViewById(R.id.finc_saleDayOfWeek_ll).setVisibility(
//						View.GONE);
//				break;
//			case 1:
//				findViewById(R.id.finc_saleDayOfMonth_ll).setVisibility(
//						View.GONE);
//				findViewById(R.id.finc_saleDayOfWeek_ll).setVisibility(
//						View.VISIBLE);
//				break;
//			default:
//				break;
//			}
//			break;
		case R.id.fincScheduledbuySetEndTime:
			scheduledbuySetEndTimeFlag = FincUtils.getKeyByValue(
					LocalData.dtEndFlagMap, fincScheduledbuySetEndTime
							.getSelectedItem().toString());
			switch (Integer.parseInt(scheduledbuySetEndTimeFlag)) {
			case 0:
				findViewById(R.id.fincScheduledbuyEndTimeLl).setVisibility(
						View.GONE);
				findViewById(R.id.fincScheduledbuyTotalDealCountLl)
						.setVisibility(View.GONE);
				findViewById(R.id.fincScheduledbuyTotalDealAmountLl)
						.setVisibility(View.GONE);
				break;
			case 1:
				findViewById(R.id.fincScheduledbuyEndTimeLl).setVisibility(
						View.VISIBLE);
				findViewById(R.id.fincScheduledbuyTotalDealCountLl)
						.setVisibility(View.GONE);
				findViewById(R.id.fincScheduledbuyTotalDealAmountLl)
						.setVisibility(View.GONE);
				break;
			case 2:
				findViewById(R.id.fincScheduledbuyEndTimeLl).setVisibility(
						View.GONE);
				findViewById(R.id.fincScheduledbuyTotalDealCountLl)
						.setVisibility(View.VISIBLE);
				findViewById(R.id.fincScheduledbuyTotalDealAmountLl)
						.setVisibility(View.GONE);
				break;
			case 3:
				findViewById(R.id.fincScheduledbuyEndTimeLl).setVisibility(
						View.GONE);
				findViewById(R.id.fincScheduledbuyTotalDealCountLl)
						.setVisibility(View.GONE);
				findViewById(R.id.fincScheduledbuyTotalDealAmountLl)
						.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	/**
	 * 详情入口初始化
	 * 
	 * @Author xyl
	 */
	private void detailInit() {
		fundCodeSpinner.setVisibility(View.GONE);
		fundCompanySpinner.setVisibility(View.GONE);
		fundCodeTextView.setVisibility(View.VISIBLE);
		fundCompanyTextView.setVisibility(View.VISIBLE);
	}

	/**
	 * 一个月中的天数
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
