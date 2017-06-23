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
 * 我的基金--交易流水详情
 * 
 * @author fsm
 * 
 */
public class FincFundTransSactionDetailActivity extends FincBaseActivity {
	/**
	 * 交易日期,交易类型,基金代码,基金名称,交易金额,交易份额,记录状态,确认日期,基金流水号,银行交易账号
	 */
	private TextView fincDealDate, fincTradeType, finc_fundcode_tv,
			finc_fundname_tv, fincTradeAmount, fincTradeCount,
			fincRecordStatus, fincConfirmDate, fincFundAccountNo, fincFundSeq,
			fincTradeCurrency;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tabcontent.addView(mainInflater.inflate(R.layout.finc_myfinc_transaction_detail, null));
		setTitle(getResources().getString(R.string.finc_myfinc_fundStatementBalance));
		initViews();
		initData();
	}

	private void initViews(){
		fincDealDate = (TextView)findViewById(R.id.fincDealDate);
		fincTradeType = (TextView)findViewById(R.id.fincTradeType);
		finc_fundcode_tv = (TextView)findViewById(R.id.finc_fundcode_tv);
		finc_fundname_tv = (TextView)findViewById(R.id.finc_fundname_tv);
		fincTradeAmount = (TextView)findViewById(R.id.fincTradeAmount);
		fincTradeCount = (TextView)findViewById(R.id.fincTradeCount);
		fincRecordStatus = (TextView)findViewById(R.id.fincRecordStatus);
		fincConfirmDate = (TextView)findViewById(R.id.fincConfirmDate);
		fincFundAccountNo = (TextView)findViewById(R.id.fincFundAccountNo);
		fincFundSeq = (TextView)findViewById(R.id.fincFundSeq);
		fincTradeCurrency = (TextView)findViewById(R.id.finc_tradeCurrency);
		FincUtils.setOnShowAllTextListener(this, fincDealDate, fincTradeType,
				finc_fundcode_tv, finc_fundname_tv, fincTradeAmount,
				fincTradeAmount, fincTradeCount, fincRecordStatus,
				fincConfirmDate, fincFundAccountNo, fincFundSeq, fincTradeCurrency);
	}

	private void initData() {
		Map<String, Object> map = fincControl.transSactionDetail;
		if(!StringUtil.isNullOrEmpty(map)){
			finc_fundcode_tv.setText(StringUtil.valueOf1((String)map.get(Finc.I_FUNDCODE)));
			finc_fundname_tv.setText(StringUtil.valueOf1((String)map.get(Finc.I_FUNDNAME)));
			String currency = (String)map.get(Finc.I_CURRENCYCODE);
			String cashRemit = (String)map.get(Finc.FINC_CASEREMIT_RES);
			fincDealDate.setText((String) map.get(Finc.I_APPLYDATE));
			fincTradeType.setText((String) LocalData.fundTranTypeMap
					.get((String) map
							.get(Finc.FINC_FUNDTODAYQUERY_FUNDTRANTYPE)));
			fincTradeAmount.setText(StringUtil.parseStringCodePattern(currency,
					(String) map.get(Finc.FUNDCONFIRMAMOUNT), 2));
			fincTradeCount.setText(StringUtil.valueOf1((String) map.get(Finc.FUNDCONFIRMCOUNT)));
			fincRecordStatus
					.setText((String) LocalData.fincHistoryStatusTypeCodeToStr
							.get((String) map.get(Finc.TRSSTATUS)));
			fincConfirmDate.setText(StringUtil.valueOf1((String) map.get(Finc.I_PAYMENTDATESTR)));
			fincFundAccountNo.setText(StringUtil.valueOf1((String) map.get(Finc.I_FINCBUYSQL)));
			fincFundSeq.setText(StringUtil.getForSixForString((String) map.get(Finc.FUNDACCOUNTNO)));
			fincTradeCurrency.setText(FincControl.fincCurrencyAndCashFlag(currency, cashRemit));
		}
	}

}
