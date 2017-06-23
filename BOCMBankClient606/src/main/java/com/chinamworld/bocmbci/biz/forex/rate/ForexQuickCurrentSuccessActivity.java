package com.chinamworld.bocmbci.biz.forex.rate;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.biz.forex.ForexBaseActivity;
import com.chinamworld.bocmbci.biz.forex.customer.ForexCustomerRateInfoActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 活期卖出成功页面
 * 
 * @author 宁焰红
 * 
 */
public class ForexQuickCurrentSuccessActivity extends ForexBaseActivity {
	private static final String TAG = "ForexQuickCurrentSuccessActivity";
	/**
	 * ForexQuickCurrentSuccessActivity的主布局
	 */
	private View rateInfoView = null;
	/**
	 * 返回按钮
	 */
	private Button backButton = null;
	private Button sureButton = null;
	private TextView numberText = null;
	/** 卖出金额money */
	private TextView sellCodeText = null;
	/** 卖出金额code */
	private TextView sellCodeValueText = null;
	/** 买入金额money */
	private TextView buyCodeText = null;
	/** 买入金额code */
	private TextView buyCodeValueText = null;
	private TextView typeText = null;
	private TextView baseRateText = null;
	private TextView exchangeRateText = null;
	/** 0--买入 */
	private String buyTag = LocalData.forexTradeSellOrBuyList.get(0);
	/** 1--卖出 0--买入 */
	private String sellTag = LocalData.forexTradeSellOrBuyList.get(1);
	/** 交易账户 */
	private String account = null;
	/** 卖出金额/币种 */
	private TextView sellText = null;
	/** 买入金额/币种 */
	private TextView buyText = null;
	/** 委托汇率View */
	private View weiTuoRateView = null;
	/** 止损汇率View */
	private View zhiSunRateView = null;
	/** 获利汇率View */
	private View huoLiRateView = null;
	/** 追击点差View */
	private View loseRateView = null;
	/** 截止时间View */
	private View timesView = null;
	/** 委托汇率 */
	private TextView weiTuoRateText = null;
	/** 止损汇率 */
	private TextView zhiSunRateText = null;
	/** 获利汇率 */
	private TextView huoLiRateText = null;
	/** 追击点差 */
	private TextView loseRateText = null;
	/** 截止时间 */
	private TextView timesText = null;
	/** 08--市价即时 */
	private String eight = null;
	/** 07-限价即时 */
	private String seven = null;
	/** 03---获利委托 */
	private String three = null;
	/** 04--止损委托 */
	private String four = null;
	/** 05--二选一委托 */
	private String five = null;
	/** 11--追击止损委托 */
	private String eleven = null;
	/** 委托汇率 */
//	private String weiTuoRate = null;
	private View jiChuRateView = null;
	private View youHuiRateView = null;
	/** 委托序号 */
	private TextView weiTuoNumber = null;
	/** 委托序号 view */
	private View weiTuoNumberView = null;
	private TextView topText = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.forex_rate_qick1));
		dealDates();
		init();
		initDate();
		inintOnClick();
	}

	private void init() {
		rateInfoView = LayoutInflater.from(ForexQuickCurrentSuccessActivity.this).inflate(R.layout.forex_rate_currency_success, null);
		tabcontent.addView(rateInfoView);
		backButton = (Button) findViewById(R.id.ib_back);
		sureButton = (Button) findViewById(R.id.sureButton);
		numberText = (TextView) findViewById(R.id.rate_currency_number);
		sellCodeText = (TextView) findViewById(R.id.rate_currency_sellCode);
		sellCodeValueText = (TextView) findViewById(R.id.currency_sellcode_code);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, sellCodeValueText);
		buyCodeText = (TextView) findViewById(R.id.rate_currency_buyCode);
		buyCodeValueText = (TextView) findViewById(R.id.currency_buycode_code);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, buyCodeValueText);
		typeText = (TextView) findViewById(R.id.rate_currency_type);
		baseRateText = (TextView) findViewById(R.id.rate_currency_papRate);
		exchangeRateText = (TextView) findViewById(R.id.rate_currency_comRate);
		jiChuRateView = findViewById(R.id.jiChuRate_layouy);
		youHuiRateView = findViewById(R.id.youHuiRate_layouy);
		weiTuoRateView = findViewById(R.id.weiTuoRate_view);
		zhiSunRateView = findViewById(R.id.zhiSunRate_view);
		huoLiRateView = findViewById(R.id.huoLiRate_view);
		loseRateView = findViewById(R.id.loseRate_view);
		weiTuoNumber = (TextView) findViewById(R.id.weituo_number);
		weiTuoNumberView = findViewById(R.id.weituo_number_layout);
		weiTuoNumberView.setVisibility(View.GONE);
		timesView = findViewById(R.id.times_view);
		weiTuoRateText = (TextView) findViewById(R.id.currency_weiTuoRate);
		zhiSunRateText = (TextView) findViewById(R.id.currency_zhiSunRate);
		huoLiRateText = (TextView) findViewById(R.id.currency_huoLiRate);
		loseRateText = (TextView) findViewById(R.id.currency_loseRate);
		timesText = (TextView) findViewById(R.id.currency_times);
		weiTuoRateView.setVisibility(View.GONE);
		zhiSunRateView.setVisibility(View.GONE);
		huoLiRateView.setVisibility(View.GONE);
		loseRateView.setVisibility(View.GONE);
		timesView.setVisibility(View.GONE);
		sellText = (TextView) findViewById(R.id.sell_text);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, sellText);
		buyText = (TextView) findViewById(R.id.buy_text);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, buyText);
		StepTitleUtils.getInstance().initTitldStep(this, new String[] { this.getResources().getString(R.string.forex_rate_tep1), this.getResources().getString(R.string.forex_rate_tep2), this.getResources().getString(R.string.forex_rate_tep3) });
		StepTitleUtils.getInstance().setTitleStep(3);
		backButton.setVisibility(View.GONE);// 返回按钮隐藏
		topText = (TextView) findViewById(R.id.top_text);
	}

	private void dealDates() {
		eight = LocalData.forexTradeStyleTransList.get(0);
		seven = LocalData.forexTradeStyleTransList.get(1);
		three = LocalData.forexTradeStyleTransList.get(2);
		four = LocalData.forexTradeStyleTransList.get(3);
		five = LocalData.forexTradeStyleTransList.get(4);
		eleven = LocalData.forexTradeStyleTransList.get(5);
	}

	@SuppressWarnings("unchecked")
	private void initDate() {
		// 委托序号
		String consignNumber = null;
		// 委托到期时间
		String dueDate = null;
		// 获利汇率
		String winRate = null;
		// 止损汇率
		String loseRate = null;
		String sellAmount = null;
		String buyAmount = null;
		Map<String, Object> tradeResult = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.FOREX_TRADERESULT_KEY);
		if (StringUtil.isNullOrEmpty(tradeResult)) {
			return;
		} else {
			String transactionId = (String) tradeResult.get(Forex.FOREX_TRANSACTIONID_RES);
			String baseRate = (String) tradeResult.get(Forex.FOREX_BASERATE_RES);
			String exchangeRate = (String) tradeResult.get(Forex.FOREX_EXCHANGERATE_RES);
			consignNumber = (String) tradeResult.get(Forex.FOREX_CONSIGNNUMBERT_RES);
			dueDate = (String) tradeResult.get(Forex.FOREX_DUEDATE_RES);
			winRate = (String) tradeResult.get(Forex.FOREX_WINRATE_RES);
			loseRate = (String) tradeResult.get(Forex.FOREX_LOSERATE_RES);
			sellAmount = (String) tradeResult.get(Forex.FOREX_SELLAMOUNT);
			buyAmount = (String) tradeResult.get(Forex.FOREX_BUYAMOUNT);
			numberText.setText(transactionId);
			baseRateText.setText(baseRate);
			exchangeRateText.setText(exchangeRate);
		}
		Intent intent = getIntent();
		if (intent == null) {
			return;
		} else {
			// 交易方式代码
			String exchangeTypeCode = intent.getStringExtra(Forex.FOREX_EXCHANGETYPE_REQ);
			if (!StringUtil.isNull(exchangeTypeCode)) {
				if (exchangeTypeCode.equals(seven) || exchangeTypeCode.equals(eight)) {
				} else {
					topText.setText(getResources().getString(R.string.forex_wt_success_top_title));
				}
			}
			showView(exchangeTypeCode);
//			weiTuoRate = intent.getStringExtra(ConstantGloble.FOREX_WEITUORATE_KEY);
			if (exchangeTypeCode.equals(three)) {
				weiTuoRateText.setText(winRate);
				timesText.setText(dueDate);
				if (StringUtil.isNull(consignNumber)) {
					weiTuoNumber.setText("-");
				} else {
					weiTuoNumber.setText(consignNumber);
				}
			} else if (exchangeTypeCode.equals(four)) {
				weiTuoRateText.setText(loseRate);
				timesText.setText(dueDate);
				if (StringUtil.isNull(consignNumber)) {
					weiTuoNumber.setText("-");
				} else {
					weiTuoNumber.setText(consignNumber);
				}
			} else if (exchangeTypeCode.equals(five)) {
				huoLiRateText.setText(winRate);
				zhiSunRateText.setText(loseRate);
				timesText.setText(dueDate);
				if (StringUtil.isNull(consignNumber)) {
					weiTuoNumber.setText("-");
				} else {
					weiTuoNumber.setText(consignNumber);
				}
			} else if (exchangeTypeCode.equals(eleven)) {
				loseRateText.setText(intent.getStringExtra(Forex.FOREX_LOSERATE_RES));
				timesText.setText(dueDate);
				if (StringUtil.isNull(consignNumber)) {
					weiTuoNumber.setText("-");
				} else {
					weiTuoNumber.setText(consignNumber);
				}
			}
			// 交易方式
			String exchangeType = intent.getStringExtra(ConstantGloble.FOREX_CHANGETYPE_KEY);
			// 卖出币种名称+钞汇
			String sellCodeAndCash = intent.getStringExtra(ConstantGloble.FOREX_SELLCODESP_KEY);
			// 买入币种名称
			String buyCodeName = intent.getStringExtra(ConstantGloble.FOREX_BUYCODESP_KEY);
			// 卖出币种代码
			String sellNoDealCode = intent.getStringExtra(ConstantGloble.FOREX_SELLCODENODEAL_KEY);
			// 买入币种代码
			String buyNoDealCode = intent.getStringExtra(ConstantGloble.FOREX_BUYCODENODEAL_KEY);
			// 买卖标志
			String radioGroup = intent.getStringExtra(ConstantGloble.FOREX_RADIOBUTTON_KEY);
			String cashNoDealCode = intent.getStringExtra(Forex.FOREX_CASHREMIT_REQ);
			String cash = null;
			if (StringUtil.isNull(cashNoDealCode)) {
				cash = "-";
			} else {
				if (LocalData.CurrencyCashremit.containsKey(cashNoDealCode)) {
					cash = LocalData.CurrencyCashremit.get(cashNoDealCode);
				} else {
					cash = "-";
				}
			}
			// 卖出金额
			String sellMoney = null;
			// 买入金额
			String buyMoney = null;
			// 为控件赋值
			typeText.setText(exchangeType);
			if (sellTag.equals(radioGroup)) {
				// 卖出
				sellMoney = intent.getStringExtra(ConstantGloble.FOREX_SELLEDITTEXT_KEY);
				String m = StringUtil.parseStringCodePattern(sellNoDealCode, sellMoney, fourNumber);
				sellCodeText.setText(m);
				sellCodeValueText.setText(sellCodeAndCash);
				// buyText.setText(getResources().getString(R.string.forex_trade_buy1));
				String buy = StringUtil.parseStringCodePattern(buyNoDealCode, buyAmount, fourNumber);
				buyCodeText.setVisibility(View.VISIBLE);
				buyCodeText.setText(buy);
				buyCodeValueText.setText(buyCodeName + " " + cash);
			} else if (buyTag.equals(radioGroup)) {
				// 买入
				buyMoney = intent.getStringExtra(ConstantGloble.FOREX_COLUMONEY_KEY);
				// sellText.setText(getResources().getString(R.string.forex_rate_currency_buyCode));
				// buyText.setText(getResources().getString(R.string.forex_trade_sell1));
				String sell = StringUtil.parseStringCodePattern(sellNoDealCode, sellAmount, fourNumber);
				sellCodeText.setText(sell);
				sellCodeValueText.setText(sellCodeAndCash);
				buyCodeText.setVisibility(View.VISIBLE);
				String m = StringUtil.parseStringCodePattern(buyNoDealCode, buyMoney, fourNumber);
				buyCodeText.setText(m);
				buyCodeValueText.setText(buyCodeName + " " + cash);
			}
		}
	}

	private void showView(String type) {
		if (type.equals(seven) || type.equals(eight)) {
			loseRateView.setVisibility(View.GONE);
			weiTuoNumberView.setVisibility(View.GONE);
		} else if (type.equals(three) || type.equals(four)) {
			loseRateView.setVisibility(View.GONE);
			weiTuoRateView.setVisibility(View.VISIBLE);
			timesView.setVisibility(View.VISIBLE);
			jiChuRateView.setVisibility(View.GONE);
			youHuiRateView.setVisibility(View.GONE);
			weiTuoNumberView.setVisibility(View.VISIBLE);
		} else if (type.equals(five)) {
			loseRateView.setVisibility(View.GONE);
			huoLiRateView.setVisibility(View.VISIBLE);
			zhiSunRateView.setVisibility(View.VISIBLE);
			timesView.setVisibility(View.VISIBLE);
			jiChuRateView.setVisibility(View.GONE);
			youHuiRateView.setVisibility(View.GONE);
			weiTuoNumberView.setVisibility(View.VISIBLE);
		} else if (type.equals(eleven)) {
			loseRateView.setVisibility(View.VISIBLE);
			huoLiRateView.setVisibility(View.GONE);
			zhiSunRateView.setVisibility(View.GONE);
			timesView.setVisibility(View.VISIBLE);
			jiChuRateView.setVisibility(View.GONE);
			youHuiRateView.setVisibility(View.GONE);
			weiTuoNumberView.setVisibility(View.VISIBLE);
		}
	}

	public void inintOnClick() {
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 返回到我的外汇页面
				Intent intent = new Intent(ForexQuickCurrentSuccessActivity.this, ForexCustomerRateInfoActivity.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * 根据投资交易类型，查询交易账户---回调
	 * 
	 * @param resultObj
	 */
	public void requestQueryInvtBindingInfoCallback(Object resultObj) {
		Map<String, String> result = getHttpTools().getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(result)) {
			return;
		} else {
			account = result.get(Forex.FOREX_ACCOUNT_RES);
			if (StringUtil.isNull(account)) {
				return;
			}
			init();
			initDate();
			inintOnClick();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
