package com.chinamworld.bocmbci.biz.finc.trade;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.FincUtils;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.biz.finc.control.entity.FundCompany;
import com.chinamworld.bocmbci.biz.finc.fundprice.FundFundCompanyActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.ChineseCharToEn;
import com.chinamworld.bocmbci.utils.MathUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 基金定投页面
 * 
 * @author xyl
 * 
 */
public class FincTradeScheduledBuyActivity extends FincBaseActivity implements
OnItemSelectedListener {
	private static final String TAG = "FincTradeScheduledBuyActivity";
	
	/** 基金公司下拉列表 */
	private TextView fundCompanySpinner;
	private int fundCompanyPos = 0;
	private int oldPosition = 0 ;
	/**
	 * 基金代码下拉列表
	 */
	private Spinner fundCodeSpinner;

	/**spinner 基金代码/名称 文本显示 */
	private TextView CodeTextSpinner;

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
	private String fundCompanyCodeStr;
	private String fundCodeStr;
	private String fundNameStr;
	private String netPriceStr;
	private String productRiskLevelStr;
	private String feeTypeStr;
	private String tradeCurrencyStr, cashFlagCode;
	private String fundStateStr;
	private String lowLimitStr;
	private String dayInMonthStr;

	/**基金公司sipnner 是否被点击*/
	private boolean isClickCompany = false;

	/**基金代码/名称 sipnner 是否被点击*/
	private boolean isClickCode = false;
	
	private boolean fastTradeEnter = false;

	/**避免基金名称 构建时第一次初始化*/
    private boolean firstInit = false;
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
	/** 基金公司名称集合 */
	private ArrayList<FundCompany> companyList= new ArrayList<FundCompany>() ;

	private String canScheduBuy;
	private LinearLayout fundCodell;
	private LinearLayout fundNamell;
	private TextView fundNameTv;
	private LinearLayout fundnameandcodell;

	/** P405 */
	/** 定投周期， 每周定投日期，结束条件 */
	private Spinner fincScheduledbuyPeriod, fincSaleDayOfWeekSp,
	fincScheduledbuySetEndTime;
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

	@Override
	public void getFundCompanyListCallback(Object resultObj) {
		super.getFundCompanyListCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		fincControl.fundCompanyList = (List<Map<String, String>>) biiResponseBody
				.getResult();
		for (Map<String, String> map : fincControl.fundCompanyList) {
			String companyName = map.get(Finc.FINC_GETFUNDCOMPANCYLIST_FUNDCOMPANYNAME);
			// 为基金公司下拉列表赋值
			FundCompany fundCompany = new FundCompany();
			fundCompany.setFundCompanyName(companyName);
			fundCompany.setFundCompanyCode(map
					.get(Finc.FINC_GETFUNDCOMPANCYLIST_FUNDCOMPANYCODE));
			fundCompany.setChecked(false);
			fundCompany.setAlpha(ChineseCharToEn.cn2py(companyName));
			companyList.add(fundCompany);
			
		}
		Collections.sort(companyList, new Comparator<FundCompany>() {

			public int compare(FundCompany o1, FundCompany o2) {  
		        //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序  
		        if (o2.getAlpha().equals("#")) {  
		            return -1;  
		        } else if (o1.getAlpha().equals("#")) {  
		            return 1;  
		        } else {  
		            return o1.getAlpha().compareTo(o2.getAlpha());  
		        }  
		    }});
		initFundCompany();
	}

	@Override
	public void queryfundDetailByFundComanyCodeCallback(Object resultObj) {
		super.queryfundDetailByFundComanyCodeCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		fundList = (List<Map<String, Object>>) biiResponseBody.getResult();

		if (StringUtil.isNullOrEmpty(fundList)) {
			fundCompanySpinner.setText("请选择");
			isClickCompany = false;
			BaseDroidApp.getInstanse().showInfoMessageDialog("该公司暂无基金产品在售，请选择其他公司产品。");
			return;
		}
		// 如果不为空
//		List<String> fundCodeNameList = new ArrayList<String>();
//		if (!StringUtil.isNullOrEmpty(fundList)) {// 如果不为空
//			fundCodeStrList = new ArrayList<String>();
//			fundNameStrList = new ArrayList<String>();
//			for (Map<String, Object> map : fundList) {
//				fundNameStrList.add((String) map.get(Finc.FINC_FUNDNAME));
//				fundCodeStrList.add((String) map.get(Finc.FINC_FUNDCODE));
//				fundCodeNameList.add((String) map.get(Finc.FINC_FUNDNAME) + "/"
//						+ (String) map.get(Finc.FINC_FUNDCODE));
//			}
//			fundCodeSpinner.setAdapter(new ArrayAdapter<String>(this,
//					R.layout.dept_spinner, fundCodeNameList));
//			oldPosition = newPosition;
//		} else {
//			fundCompanySpinner.setSelection(oldPosition);
//			BaseDroidApp.getInstanse().showInfoMessageDialog("该基金公司没有基金!");
//			return;
//		}

	}

	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	private void init() {
		setRightToMainHome();
		View childview = mainInflater.inflate(
				R.layout.finc_trade_scheduledbuy_main, null);
		tabcontent.addView(childview);
		StepTitleUtils.getInstance().initTitldStep(this,
				fincControl.getStepsForDingTou());
		StepTitleUtils.getInstance().setTitleStep(2);
		setTitle(R.string.finc_title_scheduledbuy_new);
		TextView fincLowLimitTitleView = (TextView) childview
				.findViewById(R.id.finc_lowlimit_titleView);
		fundCompanyTextView = (TextView) childview
				.findViewById(R.id.finc_fundcompany_textView);
		//单位净值
		netPriceTextView = (TextView) childview
				.findViewById(R.id.finc_netvalue_textView);
		//产品风险级别
		productRiskLevelTextView = (TextView) childview
				.findViewById(R.id.finc_productrisklevel_textView);
		//收费方式
		feeTypeTextView = (TextView) childview				
				.findViewById(R.id.finc_feetype_textView);
		//交易币种
		tradeCurrencyTextView = (TextView) childview
				.findViewById(R.id.finc_tradecurrency_textView);
		fundCompanySpinner = (TextView) childview
				.findViewById(R.id.finc_fundcompany_spinner);
		fundCodeSpinner = (Spinner) childview
				.findViewById(R.id.finc_fundnameandcode_spinner);
		fundCodeTextView = (TextView) childview
				.findViewById(R.id.finc_fundcode_tv);
		fundNameTv = (TextView) childview.findViewById(R.id.finc_fundname_tv);
		//定期定额申购下限
		lowLimitTextView = (TextView) childview
				.findViewById(R.id.finc_saleLowlimit_textView);
		CodeTextSpinner = (TextView) childview
				.findViewById(R.id.code_text_spinner);
		saleAmountEditText = (EditText) childview
				.findViewById(R.id.finc_saleamount_editText);
		saleDayOfMonthSp = (Spinner) childview
				.findViewById(R.id.finc_saleDayOfMonth_Sp);
		fundCodell = (LinearLayout) childview
				.findViewById(R.id.finc_fundcode_ll);
		fundNamell = (LinearLayout) childview
				.findViewById(R.id.finc_fundname_ll);
		fundnameandcodell = (LinearLayout) childview
				.findViewById(R.id.finc_fundnameandcode_ll);
		nextBtn = (Button) childview.findViewById(R.id.finc_next);
		nextBtn.setOnClickListener(this);
		findViewById(R.id.finc_fundcompany_spinner_layout).setVisibility(View.GONE);
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

		fincScheduledbuyPeriod = (Spinner) findViewById(R.id.fincScheduledbuyPeriod);
		fincSaleDayOfWeekSp = (Spinner) findViewById(R.id.fincSaleDayOfWeekSp);
		fincScheduledbuySetEndTime = (Spinner) findViewById(R.id.fincScheduledbuySetEndTime);
		fincScheduledbuyEndTime = (TextView) findViewById(R.id.fincScheduledbuyEndTime);
		fincScheduledbuyEndTime
		.setText(QueryDateUtils.getcurrentDate(dateTime));
		fincScheduledbuyTotalDealCount = (EditText) findViewById(R.id.fincScheduledbuyTotalDealCount);
		fincScheduledbuyTotalDealAmount = (EditText) findViewById(R.id.fincScheduledbuyTotalDealAmount);
		FincUtils.initSpinnerView(this, fincScheduledbuyPeriod,
				LocalData.transCycleMap);
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
		fincScheduledbuyPeriod.setOnItemSelectedListener(this);
		fincScheduledbuySetEndTime.setOnItemSelectedListener(this);
		CodeTextSpinner.setOnClickListener(SpinnerTextListener);
	}
	
	
	private void initHide(){
		netPriceTextView.setText("-");
		productRiskLevelTextView.setText("-");
		feeTypeTextView.setText("-");
		lowLimitTextView.setText("-");
		tradeCurrencyTextView.setText("-");
	}

	/**
	 * 初始化数据
	 * 
	 * @Author xyl
	 */
	private void initData() {
		if (StringUtil.isNullOrEmpty(fincControl.tradeFundDetails)) {// 快速交易中的买入
			fastTradeInit();
//			BaseHttpEngine.showProgressDialogCanGoBack();
			getFundCompanyList();
			initHide();
			
			fastTradeEnter = true;
		} else {// 详情页面中的买入
			detailInit();
			Map<String, Object> map = fincControl.tradeFundDetails;
			fundCompanyStr = (String) map.get(Finc.FINC_FUNDCOMPANYNAME);
			fundCompanyCodeStr = (String) map.get(Finc.FINC_FUNDCOMPANYCODE);
			fundCodeStr = (String) map.get(Finc.FINC_FUNDCODE);
			fundNameStr = (String) map.get(Finc.FINC_FUNDNAME);
			netPriceStr = (String) map.get(Finc.FINC_NETPRICE);
			productRiskLevelStr = (String) map.get(Finc.FINC_RISKLV);
			feeTypeStr = (String) map.get(Finc.FINC_FEETYPE);// 收费方式.
			fundStateStr = (String) map.get(Finc.FINC_FUNDSTATE);
			tradeCurrencyStr = (String) map.get(Finc.FINC_CURRENCY);
			StringUtil.setInPutValueByCurrency(saleAmountEditText,
					tradeCurrencyStr);
			cashFlagCode = (String) map.get(Finc.FINC_CASHFLAG);
			lowLimitStr = (String) map.get(Finc.FINC_SCHEDULEAPPLYLOWLIMIT);
			canScheduBuy = String.valueOf(map.get(Finc.CANSCHEDULEBUY));
			if (StringUtil.isNullOrEmpty(lowLimitStr)) {
				lowLimitStr = "-";
			}
			fundnameandcodell.setVisibility(View.GONE);
			fundCodell.setVisibility(View.VISIBLE);
			fundNamell.setVisibility(View.VISIBLE);
			fundCompanyTextView.setText(fundCompanyStr);// 只有详情中进入才显示
			fundCodeTextView.setText(fundCodeStr); // 只有详情中详情中进入才显示
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
			setOcrmTradeAmount();
		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				fundCompanyTextView);

	}

	private void setOcrmTradeAmount() {
		if (getIntent().getBooleanExtra(Finc.orcmflag, false)) {
			if (!StringUtil.isNullOrEmpty(fincControl.OcrmProductDetailMap)) {
				String transSum = (String) fincControl.OcrmProductDetailMap
						.get(Finc.TRANSSUM);
				if(tradeCurrencyTextView.getText().toString().startsWith(Finc.YEN)){
					transSum = FincUtils.getYenIntegerStr(transSum);
				}
				saleAmountEditText.setText(StringUtil.valueOf1(transSum));
			}
		}
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
			case 2:
				RegexpBean countReg = new RegexpBean(
						FincUtils
						.getNoColonStr(getString(R.string.finc_scheduledbuy_total_deal_count)),
						fincScheduledbuyTotalDealCount.getText().toString(),
						"totleCount");
				lists.add(countReg);
				break;
			case 3:
				RegexpBean amountReg = StringUtil
				.getRegexBeanByCurrency(
						FincUtils
						.getNoColonStr(getString(R.string.finc_scheduledbuy_total_deal_amount)),
						fincScheduledbuyTotalDealAmount.getText()
						.toString(), tradeCurrencyStr);
				lists.add(amountReg);
				break;
			}
			if(!isClickCompany && fastTradeEnter ){
				BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_choose_company));
				return;
			}
			if(!isClickCode && fastTradeEnter){
				BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_choose_code));
				return;
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
		intent.setClass(this, FincTradeScheduledBuyConfirmActivity.class);
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
		intent.putExtra(Finc.I_FUNDSTATE, fundStateStr);// 成功页面显示

		intent.putExtra(Finc.TRANSCYCLE, scheduledbuyPeriodFlag);// 定投周期
		intent.putExtra(Finc.PAYMENTDATEOFWEEK, fincSaleDayOfWeekSp.getSelectedItem().toString());// 每周定投日
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
			if(!firstInit){
				firstInit = true;
				return;
			}
			
			if(isClickCode ){
				if(!isClickCompany){
				BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_choose_company));
				return;
				}
			}
			upDateText(position);
			break;

		case R.id.finc_saleDayOfMonth_Sp:// 一个月中的那一天
			dayInMonthStr = getDayInMonth().get(position);
			break;
		case R.id.fincScheduledbuyPeriod:
			scheduledbuyPeriodFlag = FincUtils.getKeyByValue(
					LocalData.transCycleMap, fincScheduledbuyPeriod
					.getSelectedItem().toString());
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
			break;
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
	 * 快速交易进入初始化
	 * 
	 * @Author xyl
	 */
	private void fastTradeInit() {
		fundCodeSpinner.setVisibility(View.VISIBLE);
		fundCompanySpinner.setVisibility(View.VISIBLE);
		findViewById(R.id.finc_fundcompany_spinner_layout).setVisibility(View.VISIBLE);
		fundCodeTextView.setVisibility(View.GONE);
		fundCompanyTextView.setVisibility(View.GONE);
		fundCodeSpinner.setOnItemSelectedListener(this);
		fundnameandcodell.setVisibility(View.VISIBLE);
		fundCodell.setVisibility(View.GONE);
		fundNamell.setVisibility(View.GONE);
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
			if(requestCode == 101){
				fundCompanyPos = data.getIntExtra("fundCompanyPos", 0) ;
				if (!isClickCompany) isClickCompany = true;
				fundCompanySpinner.setText(companyList.get(fundCompanyPos).getFundCompanyName());
				fundCompanyCodeStr = companyList.get(fundCompanyPos).getFundCompanyCode();
				CodeTextSpinner.setVisibility(View.VISIBLE);
				isClickCode = false;
				BaseHttpEngine.showProgressDialog();
				initHide();
				queryfundDetailByFundComanyCode(fundCompanyCodeStr,
						ConstantGloble.FINC_FILTERFOREX_1);// 查询
			}else{
				setResult(RESULT_OK);
				finish();
			}
			break;

		default:
			break;
		}
	}

	public OnClickListener SpinnerTextListener =new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.code_text_spinner:
				
				if((!isClickCompany) || StringUtil.isNullOrEmpty(fundList)){
					BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_choose_company));
					return;
				}

				if(!isClickCode){
					isClickCode = true;
					CodeTextSpinner.setVisibility(View.GONE);
				}
				
				//初始化基金代码spinner 
				List<String> fundCodeNameList = new ArrayList<String>();
				if (!StringUtil.isNullOrEmpty(fundList)) {// 如果不为空
					fundCodeStrList = new ArrayList<String>();
					fundNameStrList = new ArrayList<String>();
					for (Map<String, Object> map : fundList) {
						fundNameStrList.add((String) map.get(Finc.FINC_FUNDNAME));
						fundCodeStrList.add((String) map.get(Finc.FINC_FUNDCODE));
						fundCodeNameList.add((String) map.get(Finc.FINC_FUNDNAME) + "/"
								+ (String) map.get(Finc.FINC_FUNDCODE));
					}
					fundCodeSpinner.setAdapter(new ArrayAdapter<String>(FincTradeScheduledBuyActivity.this,
							R.layout.dept_spinner, fundCodeNameList));
					oldPosition = fundCompanyPos;
				} 
				
				fundCodeSpinner.performClick();
				break;
			default:
				break;
			}
		}
	};

	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus && isClickCode && 
				fundCodeSpinner.getSelectedItemPosition() == 0){
			upDateText(0);
		}
	}
	private void upDateText(int position){
		if (!StringUtil.isNullOrEmpty(fundNameStrList)
				&& !StringUtil.isNullOrEmpty(fundCodeStrList)) {// 如果列表显示不为空
			fundCodeStr = fundCodeStrList.get(position);
			fundNameStr = fundNameStrList.get(position);
			Map<String, Object> map = fundList.get(position);// 初始化布局数据
			netPriceStr = (String) map.get(Finc.FINC_NETPRICE);
			productRiskLevelStr = (String) map.get(Finc.FINC_RISKLV);
			if (StringUtil.isNullOrEmpty(productRiskLevelStr)) {// TODO
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
	}
	
	/***
	 * 基金公司
	 */
	private void initFundCompany(){
		
		FundCompany fundCompany = new FundCompany();
		fundCompany.setFundCompanyName("中银基金管理有限公司");
		fundCompany.setFundCompanyCode("50400000");
		fundCompany.setAlpha("推荐");
		fundCompany.setChecked(false);
		companyList.add(0,fundCompany);
		
		fundCompany = new FundCompany();
		fundCompany.setFundCompanyName("中银国际证券有限责任公司");
		fundCompany.setFundCompanyCode("13190000");
		fundCompany.setAlpha("推荐");
		fundCompany.setChecked(false);
		companyList.add(1,fundCompany);
		
		fundCompanySpinner.setText("请选择");
		fundCompanySpinner.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FincTradeScheduledBuyActivity.this , FundFundCompanyActivity.class);
				intent.putExtra("flag", "no_all");
				intent.putParcelableArrayListExtra("companyList", companyList);
				startActivityForResult(intent, 101);
				
			}
		});
	}

}
