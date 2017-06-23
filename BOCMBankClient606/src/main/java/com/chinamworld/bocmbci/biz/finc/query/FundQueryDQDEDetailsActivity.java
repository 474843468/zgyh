package com.chinamworld.bocmbci.biz.finc.query;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.FincUtils;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.ViewUtils;

import java.util.Map;

/**
 * 基金有效定投申请页面
 * 
 * @author xyl
 * 
 */
public class FundQueryDQDEDetailsActivity extends FincBaseActivity {
	private TextView fundCodeTv;
	private TextView fundNameTv;
	private TextView tradetypeTv;
	private TextView amountPreTv;
	/** 每月扣款日期 */
	private TextView paymentDateTv;
	/** 每月赎回日期 */
	private TextView dayInMonthBuyTv;
	/** 定期交易金额/份额 */
	private TextView amontTv;
	private Button cancelBtn;
	private Button modifyBtn;

	private String fundCodeStr;
	private String fundNameStr;
	private String fundTransTypeStr;
	private String applyAmountStr;
	private String dayInMonthStr;
	private String paymentDateStr;
	private String applyDateStr;
	private String fundSellFlagStr;
	private String fundSeqStr;
	private String feeTypeStr;
	private String cashFlagCode;
	
	private TextView applayDateTv;
	
	private LinearLayout schedull;
	private LinearLayout schedusalell;
	
	private TextView finc_dayInWeek_buy, finc_dayInWeek_sell, successCount,
			paymentAmount, endTime, transCount,
			finc_scheduled_transAmountCount;
	
	private TextView paymentAmountTv, transAmountCountTv;
	private TextView finc_tradestate, finc_tradeCurrency;//交易状态，交易币种
    
	/** 币种, 定投定赎周期，结束标志，结束日期，*/
	private String currency, dtdsFlag, endFlag, endDate, endSum, endAmt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initData();

	}

	/**
	 * 初始化布局
	 */
	private void init() {
		View childView = mainInflater.inflate(R.layout.finc_query_dqde_details,
				null);
		tabcontent.addView(childView);
		setTitle(R.string.finc_scheduled_available);
		schedull = (LinearLayout) childView.findViewById(R.id.finc_schedubuy_ll);
		schedusalell = (LinearLayout) childView.findViewById(R.id.finc_dayInMonth_buy_ll);
		applayDateTv = (TextView) childView.findViewById(R.id.finc_applayDate_tv);
		fundCodeTv = (TextView) childView.findViewById(R.id.finc_fundcode_tv);
		fundNameTv = (TextView) childView.findViewById(R.id.finc_fundname_tv);
		tradetypeTv = (TextView) childView.findViewById(R.id.finc_tradetype_tv);
		amountPreTv = (TextView) childView.findViewById(R.id.finc_amont_flag_tv);
		dayInMonthBuyTv = (TextView) childView
				.findViewById(R.id.finc_dayInMonth_buy_tv);
		paymentDateTv = (TextView) childView
				.findViewById(R.id.finc_dayInMonth_sale_tv);
		amontTv = (TextView) childView.findViewById(R.id.finc_amont_tv);
		cancelBtn = (Button) childView
				.findViewById(R.id.finc_query_dqde_concel_btn);
		modifyBtn = (Button) childView
				.findViewById(R.id.finc_query_dqde_modifi_btn);
		ViewUtils.initBtnParamsTwoLeft(modifyBtn, this);
		ViewUtils.initBtnParamsTwoRight(cancelBtn, this);
		finc_dayInWeek_buy = (TextView)findViewById(R.id.finc_dayInWeek_buy);
		finc_dayInWeek_sell = (TextView)findViewById(R.id.finc_dayInWeek_sell);
		successCount = (TextView)findViewById(R.id.successCount);
		paymentAmount = (TextView)findViewById(R.id.paymentAmount);
		endTime = (TextView)findViewById(R.id.endTime);
		transCount = (TextView)findViewById(R.id.transCount);
		finc_scheduled_transAmountCount = (TextView)findViewById(R.id.finc_scheduled_transAmountCount);
		paymentAmountTv = (TextView)findViewById(R.id.paymentAmountTv);
		transAmountCountTv = (TextView)findViewById(R.id.finc_scheduled_transAmountCountTv);
		
		finc_tradestate = (TextView)findViewById(R.id.finc_tradestate);
		finc_tradeCurrency = (TextView)findViewById(R.id.finc_tradeCurrency);
		
		FincUtils.setOnShowAllTextListener(this, fundNameTv, successCount,
				paymentAmount, endTime, transCount,
				finc_scheduled_transAmountCount, paymentAmountTv,
				transAmountCountTv);
		initRightBtnForMain();
		modifyBtn.setOnClickListener(this);
		if (getBtnSelectors().length == 1) {
			cancelBtn.setOnClickListener(this);
		}else{
			cancelBtn.setText(getString(R.string.more));
			PopupWindowUtils.getInstance().setshowMoreChooseUpListener(
					this, cancelBtn, getBtnSelectors(), new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							Integer tag = (Integer) v.getTag();
							String selector = getBtnSelectors()[tag];
							if(FincUtils.isStrEquals(selector, "撤销")){
								startActivity(FLAG_DDABORT);
							}else if(FincUtils.isStrEquals(selector, "暂停")){
								startActivity(FLAG_PAUSE);
							}else if(FincUtils.isStrEquals(selector, "恢复")){
								startActivity(FLAG_RESUME);
							}
						}
					});
		}
		
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		if (!StringUtil.isNullOrEmpty(fincControl.fundScheduQueryMap)) {
			fundCodeStr = (String) fincControl.fundScheduQueryMap
					.get(Finc.FINC_FUNDCODE);
			fundNameStr = (String) fincControl.fundScheduQueryMap
					.get(Finc.FINC_FUNDNAME);
			fundTransTypeStr = (String) fincControl.fundScheduQueryMap
					.get(Finc.FINC_FUNDQUERYDQDT_TRANSTYPE);
			applyAmountStr = (String) fincControl.fundScheduQueryMap
					.get(Finc.FINC_FUNDQUERYDQDT_APPLYAMOUNT);
			paymentDateStr = (String) fincControl.fundScheduQueryMap
					.get(Finc.FINC_FUNDQUERYDQDT_PAYMENTDATE);
			dayInMonthStr = (String) fincControl.fundScheduQueryMap
					.get(Finc.FINC_FUNDQUERYDQDT_DAYINMONTH);
			fundSellFlagStr = (String) fincControl.fundScheduQueryMap
					.get(Finc.FINC_FUNDQUERYDQDT_SELLFLAG);
			fundSeqStr = (String) fincControl.fundScheduQueryMap
					.get(Finc.FINC_FUNDBUY_FUNDSEQ);
			
			dtdsFlag = (String) fincControl.fundScheduQueryMap
					.get(Finc.DTDSFLAG);
			endFlag = (String) fincControl.fundScheduQueryMap.get(Finc.ENDFLAG);
			endDate = (String) fincControl.fundScheduQueryMap
					.get(Finc.FINC_ENDDATE);
			endSum = (String) fincControl.fundScheduQueryMap.get(Finc.ENDSUM);
			endAmt = (String) fincControl.fundScheduQueryMap.get(Finc.ENDAMT);
			
			Map<String, String> map = (Map<String, String>) fincControl.fundScheduQueryMap
					.get(Finc.FINC_FUNDINFO);
			currency = map.get(Finc.FINC_CURRENCY);
			feeTypeStr = map.get(Finc.FINC_FUNDBUY_FUNDSEQ);
			cashFlagCode = (String) map.get(Finc.FINC_CASHFLAG );
			applyDateStr  =(String) fincControl.fundScheduQueryMap
					.get(Finc.FINC_FUNDQUERYDQDT_APPLYDATE);
		}
		if(fundTransTypeStr==null||fundTransTypeStr.equals(ConstantGloble.FINC_TRANSTYPE_SECHEDUBUY)){//定投
			schedull.setVisibility(View.VISIBLE);
			schedusalell.setVisibility(View.GONE);
			amountPreTv.setText(getString(R.string.finc_scheduledbuyAmount_colon));
		}else{
			schedull.setVisibility(View.GONE);
			schedusalell.setVisibility(View.VISIBLE);
			amountPreTv.setText(getString(R.string.finc_scheduledsellAmount_colon));
			paymentAmountTv.setText(getString(R.string.finc_have_deal_share));
			transAmountCountTv.setText(getString(R.string.finc_scheduled_transAmountCount));
			((TextView) findViewById(R.id.scheduledBuyTradeSituation))
					.setText(getString(R.string.finc_scheduled_sell_trade_situation));
			((TextView) findViewById(R.id.scheduledBuyEndCondition))
					.setText(getString(R.string.finc_scheduled_sell_end_conditon));
		}
		fundCodeTv.setText(fundCodeStr);
		fundNameTv.setText(fundNameStr);
		fundCodeTv.setText(fundCodeStr);
		paymentDateTv.setText(StringUtil.valueOf1(paymentDateStr));
		dayInMonthBuyTv.setText(StringUtil.valueOf1(dayInMonthStr));
		if(LocalData.tradeTypeCodeToStrMap.get(fundTransTypeStr).contains("申购")){
			amontTv.setText(StringUtil.parseStringCodePattern(
					(String) fincControl.fundScheduQueryMap
					.get(Finc.FINC_CURRENCY), applyAmountStr, 2));
		}else{
			amontTv.setText(StringUtil.parseStringPattern(applyAmountStr, 2));
		}
		FincControl.setTextColor(amontTv, this);	
		tradetypeTv.setText(LocalData.tradeTypeCodeToStrMap
				.get(fundTransTypeStr));
//		setTitle(LocalData.tradeTypeCodeToStrMap
//				.get(fundTransTypeStr));
		applayDateTv.setText(applyDateStr);
		setP405AddData();
	}
	
	/**
	 * 初始化数据 
	 * 每月申购日期、每周申购日期、每月赎回日期、每周赎回日期、累计成交次数、累计成交金额/份额
	 *	结束日期、累计交易次数、 累计交易金额/份额
	 */
	private void setP405AddData(){//dtdsFlag, endFlag, endDate, endSum, endAmt
		dtdsFlag = (String)fincControl.fundScheduQueryMap.get(Finc.DTDSFLAG);
		String subDate = (String)fincControl.fundScheduQueryMap.get(Finc.SUBDATE);
		String recordStatus = (String)fincControl.fundScheduQueryMap.get(Finc.I_RECORDSTATUS);
		if(LocalData.tradeTypeCodeToStrMap.get(fundTransTypeStr).contains("申购")){
			finc_tradestate.setText(StringUtil.valueOf1((String)LocalData.scheduledBuyStatusMap.get(recordStatus)));
			if(FincUtils.isStrEquals(dtdsFlag, "0")){
				findViewById(R.id.finc_dayInWeek_buy_ll).setVisibility(View.GONE);
				findViewById(R.id.finc_schedubuy_ll).setVisibility(View.VISIBLE);
				paymentDateTv.setText(StringUtil.valueOf1(subDate));
			}else if(FincUtils.isStrEquals(dtdsFlag, "1")){
				findViewById(R.id.finc_schedubuy_ll).setVisibility(View.GONE);
				findViewById(R.id.finc_dayInWeek_buy_ll).setVisibility(View.VISIBLE);
				finc_dayInWeek_buy.setText(getWeekByValue(subDate));
			}
		}else{
			finc_tradestate.setText(StringUtil.valueOf1((String)LocalData.scheduledSellStatusMap.get(recordStatus)));
			if(FincUtils.isStrEquals(dtdsFlag, "0")){
				findViewById(R.id.finc_dayInWeek_buy_ll).setVisibility(View.GONE);
				findViewById(R.id.finc_dayInMonth_buy_ll).setVisibility(View.VISIBLE);
				findViewById(R.id.finc_dayInWeek_sell_ll).setVisibility(View.GONE);
				dayInMonthBuyTv.setText(StringUtil.valueOf1(subDate));
			}else if(FincUtils.isStrEquals(dtdsFlag, "1")){
				findViewById(R.id.finc_schedubuy_ll).setVisibility(View.GONE);
				findViewById(R.id.finc_dayInWeek_sell_ll).setVisibility(View.VISIBLE);
				findViewById(R.id.finc_dayInMonth_buy_ll).setVisibility(View.GONE);
				finc_dayInWeek_sell.setText(getWeekByValue(subDate));
			}
		}
		String end = (String) fincControl.fundScheduQueryMap.get(Finc.FINC_ENDDATE);
		endTime.setText(StringUtil.valueOf1(end));
		
		String transC = (String) fincControl.fundScheduQueryMap.get(Finc.ENDSUM);
		if (FincUtils.isZero(transC)) {
			transCount.setText("-");
		} else {
			transCount.setText(StringUtil.valueOf1(transC));
		}
		String transAC = StringUtil
				.parseStringCodePattern(currency,(String) fincControl.fundScheduQueryMap.get(Finc.ENDAMT), 2);
		if (FincUtils.isZero(transAC)) {
			finc_scheduled_transAmountCount.setText("-");
		} else {
			finc_scheduled_transAmountCount.setText(transAC);
		}
		Map<String, Object> scheduledBuySellDetail = fincControl.scheduledBuySellDetail;
		if (!StringUtil.isNullOrEmpty(scheduledBuySellDetail)) {
//			finc_tradeCurrency.setText(StringUtil.valueOf1(LocalData.Currency.get(currency)));
			finc_tradeCurrency.setText(FincControl.fincCurrencyAndCashFlag(
					currency, cashFlagCode));
			successCount.setText(StringUtil
					.valueOf1((String) scheduledBuySellDetail
							.get(Finc.SUCCESSCOUNT)));
			paymentAmount.setText(StringUtil.parseStringCodePattern(currency,
					(String) fincControl.scheduledBuySellDetail.get(Finc.PAYMENTCOUNT), 2));
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent;
		switch (v.getId()) {
		case R.id.finc_query_dqde_concel_btn:// 撤单
			startActivity(FLAG_DDABORT);
			break;
		case R.id.finc_query_dqde_modifi_btn:// 定期定额修改
			// 交易类型为定投的时候修改调用接口PsnFundScheduledBuyModify，交易类型为定赎的时候修改调用PsnFundScheduledSellModify
			intent = new Intent();
			//intent.setClass(this, FundSecheduModifyActivity.class);
			/** P405改造,分离定投、定赎页面 */
			if(ConstantGloble.FINC_TRANSTYPE_SECHEDUBUY.equals(fundTransTypeStr)){		//定投
				intent.setClass(this, FundSecheduBuyModifyActivity.class);
			}
			if(ConstantGloble.FINC_TRANSTYPE_SECHEDUSELL.equals(fundTransTypeStr)){		//定赎
				intent.setClass(this, FundSecheduSellModifyActivity.class);
				intent.putExtra(Finc.I_FUNDCODE, fundCodeStr);
			}
			startActivityForResult(intent, 1);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 启动撤消、暂停、恢复功能，三个功能使用了同一套流程，通过标志来判断不同功能
	 * @param Flag  功能标志
	 */
	private void startActivity(int Flag){
		Intent intent = new Intent();
		intent.putExtra(ConstantGloble.ISFOREX_FLAG, Flag);
//		intent.putExtra(Finc.DTDSFLAG, dtdsFlag);
//		intent.putExtra(Finc.ENDFLAG, endFlag);
//		intent.putExtra(Finc.FINC_ENDDATE, endDate);
//		intent.putExtra(Finc.ENDSUM, endSum);
//		intent.putExtra(Finc.ENDAMT, endAmt);
		if (fundTransTypeStr
				.equals(ConstantGloble.FINC_TRANSTYPE_SECHEDUBUY)) {// 定投
			intent.setClass(this, FundDDAbortConfirmActivity.class);
			intent.putExtra(Finc.I_FUNDCODE, fundCodeStr);
			intent.putExtra(Finc.I_FUNDNAME, fundNameStr);
			intent.putExtra(Finc.I_TRANSTYPE, fundTransTypeStr);
			intent.putExtra(Finc.I_FINCBUYSQL, fundSeqStr);
			intent.putExtra(Finc.I_AMOUNT, applyAmountStr);// TODO 就这么一个字段
			intent.putExtra(Finc.I_PAYMENTDATESTR, paymentDateStr);
			intent.putExtra(Finc.I_APPLYDATE, applyDateStr);
			intent.putExtra(Finc.I_FEETYPE, feeTypeStr);
			startActivityForResult(intent, 1);
		} else {
			intent.setClass(this, FundDDAbortConfirmActivity.class);
			intent.putExtra(Finc.I_FUNDCODE, fundCodeStr);
			intent.putExtra(Finc.I_FUNDNAME, fundNameStr);
			intent.putExtra(Finc.I_TRANSTYPE, fundTransTypeStr);
			intent.putExtra(Finc.I_FINCBUYSQL, fundSeqStr);
			intent.putExtra(Finc.I_APPLYDATE, applyDateStr);
			intent.putExtra(Finc.I_AMOUNT, applyAmountStr);// TODO
			intent.putExtra(Finc.I_SELLFLAG, fundSellFlagStr);
			intent.putExtra(Finc.I_DAYINMONTH, dayInMonthStr);
			startActivityForResult(intent, 1);
		}
	}
	
	private String[] getBtnSelectors(){
		String[] strArray ;
		String recordeStatus = (String)fincControl.fundScheduQueryMap.get(Finc.I_RECORDSTATUS);
		if(FincUtils.isStrEquals(recordeStatus, "0")){
			strArray = new String[2];
			strArray[0] = "撤销";
			strArray[1] = "暂停";
		}else if(FincUtils.isStrEquals(recordeStatus, "1")){
			strArray = new String[2];
			strArray[0] = "撤销";
			strArray[1] = "恢复";
		}else{
			strArray = new String[1];
			strArray[0] = "撤销";
		}
		return strArray;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			this.finish();
			break;

		default:
			
			break;
		}
	}

}
