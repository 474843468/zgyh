package com.chinamworld.bocmbci.biz.finc.myfund;

import java.util.Map;

import android.os.Bundle;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.FincUtils;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 我的基金--对账单详情
 * 
 * @author fsm
 * 
 */
public class FincFundStatementBalanceDetailActivity extends FincBaseActivity {
	/**
	 * 基金代码，基金名称，币种，当日总余额，当日冻结余额，当日有效余额，分红方式，参考基金净值，净值日期，参考基金市值，参考盈亏
	 */
	private TextView finc_fundcode_tv, finc_fundname_tv, finc_tradecurrency_tv,
			finc_myfinc_totalBalance, finc_myfinc_freezeBlance,
			finc_myfinc_availableBalance, finc_myfinc_bounds,
			finc_myfinc_netPrice, finc_myfinc_netPriceDate,
			finc_myfinc_marketValue, finc_myfinc_floatLoss;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tabcontent.addView(mainInflater.inflate(R.layout.finc_myfinc_statement_balance_detail, null));
		setTitle(getResources().getString(R.string.finc_myfinc_fundStatementBalance));
		initViews();
		initData();
	}

	private void initViews(){
		finc_fundcode_tv = (TextView)findViewById(R.id.finc_fundcode_tv);
		finc_fundname_tv = (TextView)findViewById(R.id.finc_fundname_tv);
		finc_tradecurrency_tv = (TextView)findViewById(R.id.finc_tradecurrency_tv);
		finc_myfinc_totalBalance = (TextView)findViewById(R.id.finc_myfinc_totalBalance);
		finc_myfinc_freezeBlance = (TextView)findViewById(R.id.finc_myfinc_freezeBlance);
		finc_myfinc_availableBalance = (TextView)findViewById(R.id.finc_myfinc_availableBalance);
		finc_myfinc_bounds = (TextView)findViewById(R.id.finc_myfinc_bounds);
		finc_myfinc_netPrice = (TextView)findViewById(R.id.finc_myfinc_netPrice);
		finc_myfinc_netPriceDate = (TextView)findViewById(R.id.finc_myfinc_netPriceDate);
		finc_myfinc_marketValue = (TextView)findViewById(R.id.finc_myfinc_marketValue);
		finc_myfinc_floatLoss = (TextView)findViewById(R.id.finc_myfinc_floatLoss);
		FincUtils.setOnShowAllTextListener(this, finc_fundcode_tv, finc_fundname_tv,
				finc_tradecurrency_tv, finc_myfinc_totalBalance, finc_myfinc_freezeBlance,
				finc_myfinc_availableBalance, finc_myfinc_bounds, finc_myfinc_netPrice,
				finc_myfinc_netPriceDate, finc_myfinc_marketValue, finc_myfinc_floatLoss);
	}

	private void initData() {
		Map<String, Object> map = fincControl.fundStatementBalanceDetail;
		if(!StringUtil.isNullOrEmpty(map)){
			finc_fundcode_tv.setText(StringUtil.valueOf1((String)map.get(Finc.I_FUNDCODE)));
			finc_fundname_tv.setText(StringUtil.valueOf1((String)map.get(Finc.I_FUNDNAME)));
			String currency = (String)map.get(Finc.I_CURRENCYCODE);
			String cashFlag = (String)map.get(Finc.I_CASHFLAG);
			finc_tradecurrency_tv .setText(FincControl.fincCurrencyAndCashFlag(currency, cashFlag));
			finc_myfinc_totalBalance .setText(StringUtil.parseStringCodePattern(currency, (String)map.get(Finc.TOTALBALANCE), 2));
			finc_myfinc_freezeBlance.setText(StringUtil.parseStringCodePattern(currency, (String)map.get(Finc.FREEZEBLANCE), 2));
			finc_myfinc_availableBalance.setText(StringUtil.parseStringCodePattern(currency, (String)map.get(Finc.AVAILABLEBALANCE), 2));
			finc_myfinc_bounds.setText(StringUtil.valueOf1(LocalData.bonusTypeMap.get((String)map.get(Finc.FINC_BOUNDSTYPE_REQ))));
			finc_myfinc_netPrice .setText(StringUtil.valueOf1((String)map.get(Finc.I_NETPRICE)));
			finc_myfinc_netPriceDate.setText(StringUtil.valueOf1((String)map.get(Finc.NETPRICEDATE)));
			finc_myfinc_marketValue.setText(StringUtil.valueOf1((String)map.get(Finc.MARKETVALUE)));
			finc_myfinc_floatLoss.setText(StringUtil.valueOf1((String)map.get(Finc.FLOATLOSS)));
		}
	}

}
