package com.chinamworld.bocmbci.biz.finc.query;

import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.FincUtils;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 我的基金--交易流水详情
 * 
 * @author fsm
 * 
 */
public class FincQueryDQDEUnavailableDetailActivity extends FincBaseActivity {
	/**
	 * 基金代码,基金名称,交易类型,申请日期，失效日期，交易周期,交易日期,每次买入金额,每次赎回份额,交易状态
	 */
	private TextView finc_fundcode_tv, finc_fundname_tv, finc_tradetype_tv,
			finc_applayDate_tv, invalidationDate, transCycle, transDate,
			finc_scheduledbuyAmount, finc_scheduledsellAmount, finc_tradestate;
	
	/**
	 * 巨额赎回方式， 累计成交次数，累计成交金额/份额， 结束日期，累计交易次数，累计交易金额/份额
	 */
	private TextView finc_sellFlag, successCount, paymentAmount, endTime,
			transCount, finc_scheduled_transAmountCount;
	private TextView paymentAmountTv, transAmountCountTv, finc_tradeCurrency;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tabcontent.addView(mainInflater.inflate(R.layout.finc_query_dqde_unavailable_detail, null));
		setTitle(getResources().getString(R.string.finc_scheduled_unavailable));
		initViews();
		initData();
	}

	private void initViews(){
		finc_fundcode_tv = (TextView)findViewById(R.id.finc_fundcode_tv);
		finc_fundname_tv = (TextView)findViewById(R.id.finc_fundname_tv);
		finc_tradetype_tv = (TextView)findViewById(R.id.finc_tradetype_tv);
		finc_applayDate_tv = (TextView)findViewById(R.id.finc_applayDate_tv);
		invalidationDate = (TextView)findViewById(R.id.invalidationDate);
		transCycle = (TextView)findViewById(R.id.transCycle);
		transDate = (TextView)findViewById(R.id.transDate);
		finc_scheduledbuyAmount = (TextView)findViewById(R.id.finc_scheduledbuyAmount);
		finc_scheduledsellAmount = (TextView)findViewById(R.id.finc_scheduledsellAmount);
		finc_tradestate = (TextView)findViewById(R.id.finc_tradestate);
		
		finc_sellFlag = (TextView)findViewById(R.id.finc_sellFlag);
		successCount = (TextView)findViewById(R.id.successCount);
		paymentAmount = (TextView)findViewById(R.id.paymentAmount);
		endTime = (TextView)findViewById(R.id.endTime);
		transCount = (TextView)findViewById(R.id.transCount);
		finc_scheduled_transAmountCount = (TextView)findViewById(R.id.finc_scheduled_transAmountCount);
		paymentAmountTv = (TextView)findViewById(R.id.paymentAmountTv);
		transAmountCountTv = (TextView)findViewById(R.id.finc_scheduled_transAmountCountTv);
		finc_tradeCurrency = (TextView)findViewById(R.id.finc_tradeCurrency);
		
		FincUtils.setOnShowAllTextListener(this, finc_fundcode_tv,
				finc_fundname_tv, finc_tradetype_tv, finc_applayDate_tv,
				invalidationDate, transCycle, transDate,
				finc_scheduledbuyAmount, finc_scheduledsellAmount,
				finc_tradestate, finc_sellFlag, successCount, paymentAmount,
				endTime, transCount, finc_scheduled_transAmountCount, paymentAmountTv, 
				transAmountCountTv);
	}

	private void initData() {
		Map<String, Object> map = fincControl.fundUnavailableMap;
		String currency = "";
		if(!StringUtil.isNullOrEmpty(map)){
			finc_fundcode_tv.setText(StringUtil.valueOf1((String)map.get(Finc.I_FUNDCODE)));
			finc_fundname_tv.setText(StringUtil.valueOf1((String)map.get(Finc.I_FUNDNAME)));
			String applyType = (String) map.get(Finc.APPLYTYPE);
			finc_tradetype_tv.setText(String.valueOf(LocalData.applyTypeMap.get(applyType)));
			finc_applayDate_tv.setText((String)map.get(Finc.I_APPLYDATE));
			invalidationDate.setText((String)map.get(Finc.INVALIDATIONDATE));
			String transCycleType = map.get(Finc.TRANSCYCLE).toString();
			String transDateStr =  map.get(Finc.QUERYHISTORY401_TRANSDATE).toString();
			transCycle.setText((String)LocalData.transCycleMap.get(transCycleType));
			if("0".equals(transCycleType)) /**按月*/
				transDate.setText(transDateStr);
			else 
				transDate.setText(getWeekByValue(transDateStr));  /**按周 显示 周* */
			currency = (String)map.get(Finc.I_CURRENCYCODE);
			if(FincUtils.isStrEquals(applyType, "1")){
				findViewById(R.id.finc_scheduledbuyAmount_ll).setVisibility(View.VISIBLE);
				finc_scheduledbuyAmount.setText(StringUtil.parseStringCodePattern(currency, (String) map.get(Finc.FINC_FUNDTODAYQUERY_TRANSCOUNT), 2));
				finc_tradestate.setText((String)LocalData.scheduledBuyStatusMap.get((String) map.get(Finc.I_STATUS)));
			}else if(FincUtils.isStrEquals(applyType, "2")){
				findViewById(R.id.finc_scheduledsellAmount_ll).setVisibility(View.VISIBLE);
				finc_scheduledsellAmount.setText(StringUtil.parseStringCodePattern(currency, (String) map.get(Finc.FINC_FUNDTODAYQUERY_TRANSCOUNT), 2));
				finc_tradestate.setText((String)LocalData.scheduledSellStatusMap.get((String) map.get(Finc.I_STATUS)));
				paymentAmountTv.setText(getString(R.string.finc_have_deal_share));
				transAmountCountTv.setText(getString(R.string.finc_scheduled_transAmountCount));
				((TextView) findViewById(R.id.scheduledBuyTradeSituation))
						.setText(getString(R.string.finc_scheduled_sell_trade_situation));
				((TextView) findViewById(R.id.scheduledBuyEndCondition))
						.setText(getString(R.string.finc_scheduled_sell_end_conditon));
			}
			finc_tradeCurrency.setText(StringUtil.valueOf1(LocalData.Currency.get(currency)));
			//endTime.setText(StringUtil.valueOf1((String)map.get(Finc.FINC_ENDDATE)));
			//transCount.setText(StringUtil.valueOf1((String)map.get(Finc.ENDSUM)));
			//finc_scheduled_transAmountCount.setText(StringUtil.parseStringCodePattern(currency,(String)map.get(Finc.ENDCOUNT) , 2));
			
			String end = (String) map.get(Finc.FINC_ENDDATE);
			endTime.setText(StringUtil.valueOf1(end));
			String transC = (String) map.get(Finc.ENDSUM);
			if (FincUtils.isZero(transC)) {
				transCount.setText("-");
			} else {
				transCount.setText(StringUtil.valueOf1(transC));
			}
			String transAC = StringUtil
					.parseStringCodePattern(currency,(String)map.get(Finc.ENDCOUNT), 2);
			if (FincUtils.isZero(transAC)) {
				finc_scheduled_transAmountCount.setText("-");
			} else {
				finc_scheduled_transAmountCount.setText(transAC);
			}
		}
		Map<String, Object> scheduledBuySellDetail = fincControl.scheduledBuySellDetail;
		if(!StringUtil.isNullOrEmpty(scheduledBuySellDetail)){
			successCount.setText(StringUtil.valueOf1((String) scheduledBuySellDetail.get(Finc.SUCCESSCOUNT)));
			paymentAmount.setText(StringUtil.parseStringCodePattern(currency, (String)scheduledBuySellDetail.get(Finc.PAYMENTAMOUNT), 2));
		}
	}

}
