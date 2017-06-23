package com.chinamworld.bocmbci.biz.prms.trade;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseNewActivity;
import com.chinamworld.bocmbci.biz.lsforex.rate.IsForexTwoWayDetailActivity;
import com.chinamworld.bocmbci.biz.lsforex.rate.IsForexTwoWayTreasureNewActivity;
import com.chinamworld.bocmbci.biz.prms.PrmsBaseActivity;
import com.chinamworld.bocmbci.biz.prms.PrmsNewBaseActivity;
import com.chinamworld.bocmbci.biz.prms.control.PrmsControl;
import com.chinamworld.bocmbci.biz.prms.myaccount.PrmsAccActivity401;
import com.chinamworld.bocmbci.biz.prms.myaccount.PrmsAccMeneActivity;
import com.chinamworld.bocmbci.biz.prms.price.PrmsNewPricesActivity;
import com.chinamworld.bocmbci.biz.prms.price.PrmsNewPricesDetailActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 贵金属交易成功页面
 * 
 * @author xyl
 * 
 */
public class PrmsBuySuccessActivity extends PrmsBaseActivity {
	private static final String TAG = "PrmsBuySuccessActivity";
	/**
	 * 交易序号
	 */
	private TextView transactionIdTextView;
	/**
	 * 交易币种
	 */
	private TextView tradecurrencyTextView;
	/**
	 * 钞汇标志
	 */
	// private TextView cashremitTextView;
	/**
	 * 交易种类
	 */
	private TextView tradestyleTextView;
	/**
	 * 买入数量
	 */
	private TextView tradenumTextView;
	private TextView tradenumTextView1;
	private TextView tradenumUnitTv;
	/**
	 * 交易方式
	 */
	private TextView trademethodTextView;
	/**
	 * 确定按钮
	 */
	private Button ok;

	/**
	 * 交易序号,委托序号
	 */
	private String transactionIdStr,consignNumStr;
	/**
	 * 交易币种
	 */
	private String tradecurrencyStr;
	/**
	 * 钞汇标志
	 */
	private String cashremitStr;
	/**
	 * 交易种类
	 */
	private String tradestyleStr;
	/**
	 * 买入数量
	 */
	private String tradenumStr;
	/**
	 * 交易方式
	 */
	private String tradeMethodStr;

	/**
	 * 限价即时
	 */
	private TextView limitPriceTv, losPriceTv, winPriceTv, 
	entrustDateTv;
	private TextView limitPriceUnitTv, losPriceUnitTv, winPriceUnitTv;
	/** 限价即时布局,获利委托布局,止损委托布局,委托日期布局,即时交易布局(成交价格,基础价格) */
	private View limitPriceView, winPriceView, losPriceView, entrustDateView,
	nowDealView, prms_trade_confirm_runlostlay;
	/** 委托日期,委托时间,止损价格,获利价格 */
	private String endDate,endHour,losPrice,winPrice;
	

	private TextView basePriceTv;
	private TextView basePriceUnitTv;
	private TextView extranPriceTv;
	private TextView extranPriceUnitTv;
	private String exchangeRatStr;
	private String baseRateStr;
	private TextView mSuccessInfoTv;
	/**  买入金额，买入币种*/
	private String buyAmount, buyAmountUnit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		activityTaskManager.removeAllSecondActivity();
		super.onCreate(savedInstanceState);
		init();
		initDate();

	}



	/**
	 * 初始化 布局
	 * 
	 * @Author xyl
	 */
	private void init() {
		View v = mainInflater.inflate(R.layout.prms_trade_success, null);
		tabcontent.addView(v);
		mSuccessInfoTv = (TextView) findViewById(R.id.prms_trade_success_info_tv);
		transactionIdTextView = (TextView) findViewById(R.id.prms_trade_success_transactionid);
		tradecurrencyTextView = (TextView) findViewById(R.id.prms_trade_success_tradecurrency);
		// cashremitTextView = (TextView)
		// findViewById(R.id.prms_trade_success_cashremit);
		tradestyleTextView = (TextView) findViewById(R.id.prms_trade_success_tradestyle);
		tradenumTextView = (TextView) findViewById(R.id.prms_trade_success_tradenum);
		tradenumTextView1 = (TextView) findViewById(R.id.trade_num_tv);
		tradenumUnitTv = (TextView) findViewById(R.id.prms_trade_success_tradenum_unit);

		trademethodTextView = (TextView) findViewById(R.id.prms_trade_success_trademethod);
		
		limitPriceTv = (TextView) findViewById(R.id.prms_trade_confirm_limitprice);
		losPriceTv = (TextView) findViewById(R.id.prms_trade_confirm_losprice);
		winPriceTv = (TextView) findViewById(R.id.prms_trade_confirm_winprice);
		entrustDateTv = (TextView) findViewById(R.id.prms_trade_confirm_entrustDate_tv);

		winPriceUnitTv = (TextView) findViewById(R.id.prms_trade_confirm_winprice_unit);
		losPriceUnitTv = (TextView) findViewById(R.id.prms_trade_confirm_losprice_unit);
		limitPriceUnitTv = (TextView) findViewById(R.id.prms_trade_confirm_limitprice_unit);

		limitPriceView = findViewById(R.id.prms_trade_confirm_limitlay);
		losPriceView = findViewById(R.id.prms_trade_confirm_entrust_losPrice_view);
		winPriceView = findViewById(R.id.prms_trade_confirm_entrust_winPrice_view);
		entrustDateView = findViewById(R.id.prms_trade_confirm_entrust_date_view);
		nowDealView = findViewById(R.id.prms_trade_confirm_nowDeal_view);

		basePriceTv = (TextView) findViewById(R.id.prms_baseprice_tv);
		basePriceUnitTv = (TextView) findViewById(R.id.prms_baseprice_tv_uint);
		extranPriceTv = (TextView) findViewById(R.id.prms_extranprice_tv);
		extranPriceUnitTv = (TextView) findViewById(R.id.prms_extranprice_tv_unit);
		
		prms_trade_confirm_runlostlay = findViewById(R.id.prms_trade_confirm_runlostlay);
		prms_trade_confirm_runlostlay.setVisibility(View.GONE);
		
		ok = (Button) findViewById(R.id.prms_trade_success_ok);
		ok.setOnClickListener(this);
		setTitle(getResources().getString(R.string.prms_title_trade_buy));
		StepTitleUtils.getInstance().initTitldStep(this,
				prmsControl.getStepsForPrmsTrade());
		StepTitleUtils.getInstance().setTitleStep(3);
		back.setVisibility(View.INVISIBLE);
		initRightBtnForMain();
	};

	private void initDate() {
		Intent intent = getIntent();
		Bundle extra = intent.getExtras();
		setTradeAmount(extra);
		transactionIdStr = (String) extra
				.get(Prms.PRMS_TTADE_RESOUT_TRANSACTIONID);
		consignNumStr = (String)extra.get(Prms.PRMS_TTADE_RESOUT_CONSIGNNUM);
		tradecurrencyStr = (String) extra.get(Prms.PRMS_CURRENCYCODE);
		cashremitStr = (String) extra.get(Prms.PRMS_CASHREMIT);
		tradestyleStr = (String) extra.get(Prms.PRMS_TRADETYPE);
		tradenumStr = (String) extra.get(Prms.PRMS_TRADENUM);
		tradeMethodStr = (String) extra.get(Prms.PRMS_TRADEMETHOD);
		baseRateStr = extra.getString(Prms.PRMS_TTADE_RESOUT_BASERATE);
		exchangeRatStr = extra.getString(Prms.PRMS_TTADE_RESOUT_EXCHANGERATE);
		basePriceTv.setText(baseRateStr);
		basePriceUnitTv.setText(LocalData.prmsTradePricUnitMaptoChi
				.get(tradecurrencyStr));
		extranPriceTv.setText(exchangeRatStr);
		extranPriceUnitTv.setText(LocalData.prmsTradePricUnitMaptoChi
				.get(tradecurrencyStr));

		transactionIdTextView.setText(StringUtil.valueOf1(transactionIdStr));
		if (cashremitStr.equals(ConstantGloble.CASHRMIT_RMB)) {
			tradecurrencyTextView.setText(LocalData.prmsTradeStyleMaptoChi
					.get(tradecurrencyStr));
		} else {
			tradecurrencyTextView.setText(LocalData.prmsTradeStyleMaptoChi
					.get(tradecurrencyStr)
					+ " "
					+ LocalData.CurrencyCashremit.get(cashremitStr));
		}
		tradestyleTextView.setText(LocalData.prmsStyleMaptoChi
				.get(tradestyleStr));
		tradenumTextView.setText(tradenumStr);
//		tradenumTextView.setText(PrmsControl.parseStringPatternTradeNum(
//				prmsControl.getCurrencyCode(tradecurrencyStr, tradestyleStr),
//				tradenumStr));
		tradenumUnitTv.setText(LocalData.prmsUnitMaptoChi1
				.get(tradecurrencyStr));
		trademethodTextView.setText(LocalData.prmsTradeMethodMaptoChi
				.get(tradeMethodStr));
		setViewByTransMethod(tradeMethodStr);
		switch (PrmsControl.tradeMethodSwitch(tradeMethodStr)) {
		case PrmsControl.PRMS_TRADEMETHOD_NOW:
			mSuccessInfoTv.setText(R.string.prms_trademesuccess_info);
			break;
		case PrmsControl.PRMS_TRADEMETHOD_LIMIT:
			mSuccessInfoTv.setText(R.string.prms_trademesuccess_info);
			String limitPriceTemp = (String) extra.get(Prms.PRMS_LIMITPRICE);
			limitPriceView.setVisibility(View.VISIBLE);
			limitPriceTv.setText(PrmsControl.parsePriceByCurrency(limitPriceTemp,
					prmsControl.getCurrencyCode(
							tradecurrencyStr, tradestyleStr)));
			limitPriceUnitTv.setText(LocalData.prmsTradePricUnitMaptoChi
					.get(tradecurrencyStr));
			break;
		case PrmsControl.PRMS_TRADEMETHOD_WIN:
			mSuccessInfoTv.setText(R.string.forex_wt_success_top_title);
			setConsignNum();
			winPrice = (String) extra.get(Prms.PRMS_WINPRICE);
			endDate = (String) extra.get(Prms.PRMS_ENDDATE);
			endHour = (String) extra.get(Prms.PRMS_ENDHOUR);
			winPriceTv.setText(PrmsControl.parsePriceByCurrency(winPrice,
					prmsControl.getCurrencyCode(
							tradecurrencyStr, tradestyleStr)));
			winPriceUnitTv.setText(LocalData.prmsTradePricUnitMaptoChi
					.get(tradecurrencyStr));
			setEnTrustDate(endDate,endHour);
			break;
		case PrmsControl.PRMS_TRADEMETHOD_LOSE:
			mSuccessInfoTv.setText(R.string.forex_wt_success_top_title);
			setConsignNum();
			losPrice = (String) extra.get(Prms.PRMS_LOSEPRICE);
			endDate = (String) extra.get(Prms.PRMS_ENDDATE);
			endHour = (String) extra.get(Prms.PRMS_ENDHOUR);
			losPriceTv.setText(PrmsControl.parsePriceByCurrency(losPrice,
					prmsControl.getCurrencyCode(
							tradecurrencyStr, tradestyleStr)));
			losPriceUnitTv.setText(LocalData.prmsTradePricUnitMaptoChi
					.get(tradecurrencyStr));
			setEnTrustDate(endDate,endHour);
			break;
		case PrmsControl.PRMS_TRADEMETHOD_ONEINWTO:
			mSuccessInfoTv.setText(R.string.forex_wt_success_top_title);
			setConsignNum();
			winPrice = (String) extra.get(Prms.PRMS_WINPRICE);
			losPrice = (String) extra.get(Prms.PRMS_LOSEPRICE);
			endDate = (String) extra.get(Prms.PRMS_ENDDATE);
			endHour = (String) extra.get(Prms.PRMS_ENDHOUR);
			winPriceTv.setText(PrmsControl.parsePriceByCurrency(winPrice,
					prmsControl.getCurrencyCode(
							tradecurrencyStr, tradestyleStr)));
			winPriceUnitTv.setText(LocalData.prmsTradePricUnitMaptoChi
					.get(tradecurrencyStr));
			losPriceTv.setText(PrmsControl.parsePriceByCurrency(losPrice,
					prmsControl.getCurrencyCode(
							tradecurrencyStr, tradestyleStr)));
			losPriceUnitTv.setText(LocalData.prmsTradePricUnitMaptoChi
					.get(tradecurrencyStr));
			setEnTrustDate(endDate,endHour);
			break;

		}
		tradenumTextView1.setText(getString(R.string.prms_trade_buy_num));
	}
	
	/**
	 * 设置交易金额
	 */
	private void setTradeAmount(Bundle extra){
		buyAmount = extra.getString(Prms.PRMS_QUSH_SUBMIT_SELLAMOUNT);
		buyAmountUnit = extra.getString(Prms.PRMS_QUERY_DEAL_SELLCURRENCYCODE);
		TextView trade_amount_tv = (TextView)findViewById(R.id.trade_amount_tv);
		TextView trade_amount = (TextView)findViewById(R.id.trade_amount);
		TextView trade_amount_unit = (TextView)findViewById(R.id.trade_amount_unit);
		trade_amount_tv.setText(getString(R.string.finc_trAmount_colon));
		trade_amount.setText(StringUtil.parseStringCodePattern(buyAmountUnit, buyAmount, 2));
		trade_amount_unit.setText(LocalData.Currency.get(buyAmountUnit));
	}
	
	/**
	 * 设置委托序号
	 */
	private void setConsignNum(){
		((LinearLayout)findViewById(R.id.consign_num_ll)).setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.prms_trade_success_consign_num)).setText(
				StringUtil.valueOf1(consignNumStr));
	}
	/**
	 * 设置委托时间
	 */
	private void setEnTrustDate(String endDate,String endHour){
		entrustDateTv.setText(endDate + " " + endHour+getString(R.string.forex_rate_currency_hour));
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.prms_trade_success_ok:
//			startActivity(new Intent(PrmsBuySuccessActivity.this,
//					PrmsAccMeneActivity.class));
//			finish();
			if(PrmsNewBaseActivity.prmsFlagGoWay == 2){
				PrmsNewBaseActivity.prmsFlagGoWay = -1;
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				Intent intent = new Intent(PrmsBuySuccessActivity.this, PrmsNewPricesActivity.class);
				startActivity(intent);
			}else if(PrmsNewBaseActivity.prmsFlagGoWay == 1){
				PrmsNewBaseActivity.prmsFlagGoWay = -1;
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				Intent intent = new Intent(PrmsBuySuccessActivity.this, PrmsNewPricesDetailActivity.class);
				String sourceCode = (String) BaseDroidApp.getInstance().getBizDataMap().get("sourceCurrencyCodeStr");
				String targetCode = (String)BaseDroidApp.getInstance().getBizDataMap().get("targetCurrencyStr");
				intent.putExtra("sourceCurrencyCodeStr", sourceCode);
				intent.putExtra("targetCurrencyStr", targetCode);
				intent.putExtra("enters",1);
				startActivity(intent);
			}else if(PrmsNewBaseActivity.prmsFlagGoWay == 3){
				PrmsNewBaseActivity.prmsFlagGoWay = -1;
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				Intent intent = new Intent(PrmsBuySuccessActivity.this, PrmsNewPricesDetailActivity.class);
				String sourceCode = (String) BaseDroidApp.getInstance().getBizDataMap().get("sourceCurrencyCodeStr");
				String targetCode = (String)BaseDroidApp.getInstance().getBizDataMap().get("targetCurrencyStr");
				intent.putExtra("sourceCurrencyCodeStr", sourceCode);
				intent.putExtra("targetCurrencyStr", targetCode);
				intent.putExtra("enters",3);
				startActivity(intent);
			}else{
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				goToMainActivity();
			}


			break;

		default:
			break;
		}
	}
	@Override
	public void onBackPressed() {
		startActivity(new Intent(PrmsBuySuccessActivity.this,
				PrmsAccActivity401.class));
		finish();
	}
	/**
	 * 
	 * @param transMethod
	 */
	private void setViewByTransMethod(String transMethod) {
		switch (PrmsControl.tradeMethodSwitch(transMethod)) {
		case PrmsControl.PRMS_TRADEMETHOD_LIMIT:
			losPriceView.setVisibility(View.GONE);
			winPriceView.setVisibility(View.GONE);
			limitPriceView.setVisibility(View.VISIBLE);
			entrustDateView.setVisibility(View.GONE);
			nowDealView.setVisibility(View.VISIBLE);
			break;
		case PrmsControl.PRMS_TRADEMETHOD_LOSE:
			losPriceView.setVisibility(View.VISIBLE);
			winPriceView.setVisibility(View.GONE);
			limitPriceView.setVisibility(View.GONE);
			entrustDateView.setVisibility(View.VISIBLE);
			nowDealView.setVisibility(View.GONE);
			break;
		case PrmsControl.PRMS_TRADEMETHOD_WIN:
			losPriceView.setVisibility(View.GONE);
			winPriceView.setVisibility(View.VISIBLE);
			limitPriceView.setVisibility(View.GONE);
			entrustDateView.setVisibility(View.VISIBLE);
			nowDealView.setVisibility(View.GONE);
			break;
		case PrmsControl.PRMS_TRADEMETHOD_ONEINWTO:
			losPriceView.setVisibility(View.VISIBLE);
			winPriceView.setVisibility(View.VISIBLE);
			limitPriceView.setVisibility(View.GONE);
			entrustDateView.setVisibility(View.VISIBLE);
			nowDealView.setVisibility(View.GONE);
			break;
		case PrmsControl.PRMS_TRADEMETHOD_NOW:
			losPriceView.setVisibility(View.GONE);
			winPriceView.setVisibility(View.GONE);
			limitPriceView.setVisibility(View.GONE);
			entrustDateView.setVisibility(View.GONE);
			nowDealView.setVisibility(View.VISIBLE);
			break;
		}
	}
}
