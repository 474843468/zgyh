package com.chinamworld.bocmbci.biz.finc.query;

import java.util.Map;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 基金在途交易撤单成功页
 * 
 * @author xyl
 * 
 */
public class FundQueryEffectiveConcernSuccessActivity extends FincBaseActivity {
	private static final String TAG = "FundQueryEffectiveConcernSuccessActivity";

	/** 基金交易流水号,基金代码,基金名称,交易币种,交易金额/份额 ,交易类型,交易状态,TA 账号,
	 * 注册基金公司代码,注册基金公司名称 
	 * 交易序号,钞汇标志，委托日期，交易金额, 转入基金*/
	private TextView fundSeqTv, fundCodeTv, currencyTv, transCountTv,
			transTypeTv,  appointDateTv, fincTradeSerialNumTv, fincEntrustDateTv,
			fincTradeamountTv, fincMyfincThrowThowInTv;
	/**
	 * 基金交易流水号,基金代码,基金名称,交易币种,炒汇,交易金额/份额 ,交易类型,交易状态,TA
	 * 账号,注册基金公司代码,注册基金公司名称,是否可撤销
	 */
	private String fundSeq,fundCode, fundName, currency, cashFlag,
			transCount, transType, transState, taAccNum, regCommpanyCod,
			regCommpanyName, cancleFlag,appointDate,bonusType,inFundCode,inFundName;
	private String tradeSerialNum, entrustDate, tradeAmount;
	/** 撤单按钮 */
	private Button consernBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		settingbaseinit();
		initView();
		initListenner();
		initData();
	}

	/**
	 * 初始化布局
	 */
	private void initView() {
		View childView = mainInflater.inflate(
				R.layout.finc_entrust_deal_consern_success, null);
		tabcontent.addView(childView);
		setTitle(R.string.fincn_effective_consern);
		fincTradeSerialNumTv = (TextView) findViewById(R.id.finc_trade_serial_num_tv);
		fincEntrustDateTv = (TextView) findViewById(R.id.finc_entrust_date_tv);
		fincTradeamountTv = (TextView) findViewById(R.id.finc_tradeamount_tv);
		fundSeqTv = (TextView) findViewById(R.id.finc_fundsql_tv);
		fundCodeTv = (TextView) findViewById(R.id.finc_myfinc_fincCode_tv);
		currencyTv = (TextView) findViewById(R.id.finc_tradecurrency_tv);
		transCountTv = (TextView) findViewById(R.id.finc_tradenum_tv);
		transTypeTv = (TextView) findViewById(R.id.finc_tradetype_tv);
		appointDateTv = (TextView) findViewById(R.id.finc_extraDay_tv);
		fincMyfincThrowThowInTv = (TextView) findViewById(R.id.finc_myfinc_throw_thow_in_tv);
		consernBtn = (Button) findViewById(R.id.finc_query_today_concel_btn);
		setSuccessView();
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fundCodeTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fincMyfincThrowThowInTv);
	}
	
	/**
	 * 根据交易类型显示提示信息
	 * 根据交易类型判断是否显示指定日期
	 */
	private void setSuccessView(){
//		View v = findViewById(R.id.finc_fundstate_view);
//		v.setVisibility(View.GONE);
		if(FincControl.transTypeResult.equals("指定日期申购") || 
				FincControl.transTypeResult.equals("指定日期赎回")){
			((TextView)findViewById(R.id.info_alert)).setText(
					getString(R.string.fincn_appoint_dealconsern_success_2));
			((LinearLayout)findViewById(R.id.finc_extraDay_ll)).setVisibility(View.VISIBLE);
		}else{
			((TextView)findViewById(R.id.info_alert)).setText(
					getString(R.string.fincn_entrust_dealconsern_success_2));
		}
		consernBtn.setText(getString(R.string.finish));
		right.setVisibility(View.GONE);
		back.setVisibility(View.GONE);
	}

	private void initListenner() {
		consernBtn.setOnClickListener(this);
	}

	private void initData() {
		@SuppressWarnings("unchecked")
		Map<String, Object> dataMap = (Map<String, Object>) BaseDroidApp
				.getInstanse().getBizDataMap().get(Finc.D_ENTRUST_DETAILMAP);
		if (!StringUtil.isNullOrEmpty(dataMap)) {
			fundCode = (String) dataMap.get(Finc.QUERYTRANSONTRAN_FUNDCODE);
			fundName = (String) dataMap.get(Finc.QUERYTRANSONTRAN_FUNDNAME);
			currency = (String) dataMap.get(Finc.QUERYTRANSONTRAN_CURRENCYCODE);
			cashFlag = (String) dataMap.get(Finc.QUERYTRANSONTRAN_CASHFLAG);
			transCount = (String) dataMap.get(Finc.QUERYTRANSONTRAN_TRANSCOUNT);
			transType = (String) dataMap
					.get(Finc.QUERYTRANSONTRAN_FUNDTRANTYPE);
			transState = (String) dataMap
					.get(Finc.QUERYTRANSONTRAN_TRANSSTATUS);
			taAccNum = (String) dataMap.get(Finc.QUERYTRANSONTRAN_TAACCOUNTNO);
			regCommpanyCod = (String) dataMap
					.get(Finc.QUERYTRANSONTRAN_FUNDREGCODE);
			regCommpanyName = (String) dataMap
					.get(Finc.QUERYTRANSONTRAN_FUNDREGNAME);
			cancleFlag = (String) dataMap.get(Finc.QUERYTRANSONTRAN_CANCLEFLAG);
			appointDate = (String) dataMap.get(Finc.QUERYTRANSONTRAN_APPOINTDATE);
			bonusType = (String) dataMap.get(Finc.QUERYTRANSONTRAN_BONUSTYPE);
			inFundCode = (String) dataMap.get(Finc.QUERYTRANSONTRAN_INFUNDCODE);
			inFundName = (String) dataMap.get(Finc.QUERYTRANSONTRAN_INFUNDNAME);
//			tradeSerialNum = (String) dataMap.get(Finc.I_TRANSACTIONID);
			entrustDate = (String) dataMap.get(Finc.QUERYTRANSONTRAN_PAYMENTDATE);
			tradeAmount = (String) dataMap.get(Finc.QUERYTRANSONTRAN_TRANSAMOUNT);
			
		}
		tradeSerialNum = getIntent().getStringExtra(Finc.I_TRANSACTIONID);
		fincTradeSerialNumTv.setText(StringUtil.valueOf1(tradeSerialNum));
		fundSeq = getIntent().getStringExtra(Finc.I_FINCBUYSQL);
		fundSeqTv.setText(StringUtil.valueOf1(fundSeq));
		fundCodeTv.setText(FincControl.connFundCodeAndeName(fundCode, fundName));
		String currencyAndCashRemmit;
		if (currency == null) {
			currencyAndCashRemmit = "-";
		}else {
			if (LocalData.Currency.get(currency) != null
					&& LocalData.Currency.get(currency).equals(LocalData.Currency.get(ConstantGloble.PRMS_CODE_RMB))) {
				currencyAndCashRemmit = LocalData.Currency.get(currency);
			}
			else {
				currencyAndCashRemmit = LocalData.Currency.get(currency) + LocalData.CurrencyCashremit.get(cashFlag);
			}
		}
		currencyTv.setText(currencyAndCashRemmit);
		
		fincEntrustDateTv.setText(StringUtil.valueOf1(entrustDate));
		appointDateTv.setText(StringUtil.valueOf1(appointDate));
		fincTradeamountTv.setText(StringUtil.parseStringPattern(tradeAmount, 2));
		transCountTv.setText(StringUtil.parseStringPattern(transCount, 2));
		transTypeTv.setText(FincControl.transTypeResult);
		if(FincControl.transTypeResult != null && 
				(FincControl.transTypeResult.equals("基金转换"))){
			((LinearLayout)findViewById(R.id.finc_myfinc_throw_thow_in_ll)).setVisibility(View.VISIBLE);
			fincMyfincThrowThowInTv.setText(inFundName);
		}
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				fundCodeTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				fincMyfincThrowThowInTv);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_query_today_concel_btn:// 撤销按钮
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void finish() {
		setResult(RESULT_OK);
		super.finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
