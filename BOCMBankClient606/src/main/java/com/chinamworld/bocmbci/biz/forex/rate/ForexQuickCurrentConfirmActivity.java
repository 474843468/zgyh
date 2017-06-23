package com.chinamworld.bocmbci.biz.forex.rate;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.biz.forex.ForexBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 活期确认页面
 * 
 * @author 宁焰红
 * 
 */
public class ForexQuickCurrentConfirmActivity extends ForexBaseActivity implements OnClickListener {
	private static final String TAG = "ForexQuickCurrentConfirmActivity";
	/**
	 * ForexQuickCurrentConfirmActivity的主布局
	 */
	private View rateInfoView = null;
	/**
	 * 返回按钮
	 */
	private Button backButton = null;
	/** 卖出币种 */
	private TextView codeText = null;
	/** 卖出币种的值 */
	private TextView codeValueText = null;
	/** 买入金额币种 */
	private TextView moneyText = null;
	/** 买入money */
	private TextView moneyValueText = null;
	/** 买入币种 */
	private TextView moneyCodeValueText = null;
	private TextView rateText = null;
	private TextView typeText = null;
	private TextView limitRateText = null;
	/** 上一步按钮 */
	// private Button lastButton = null;
	private Button sureButton = null;
	/** 交易方式名称 */
	private String exchangeType = null;
	private String sellCode = null;
	private String buyCode = null;
	private String money = null;
	/** 记录用户选择的单选按钮 1-卖出，0—买入 */
	private String radioGroup = "0";
	private String limitRate = null;
	private String rate = null;
	/** 资金账户 */
	private String investAccount = null;
	/** 卖出币种代码 */
	private String sellNoDealCode = null;
	/** 买入币种代码 */
	private String buyNoDealCode = null;
	private String cashNoDealCode = null;
	/** 输入的卖出金额 */
	private String sellCodeEditText = null;
	/** 输入的买入金额 */
	private String buyCodeEditText = null;
	private String tokenId = null;
	/** 08--市价即时 */
	// private String eight = LocalData.forexTradeStyleTransList.get(0);
	/** 07-限价即时 */
	// private String seven = LocalData.forexTradeStyleTransList.get(1);
	/** 0--买入 */
	private String buyTag = LocalData.forexTradeSellOrBuyList.get(0);
	/** 1--卖出 */
	private String sellTag = LocalData.forexTradeSellOrBuyList.get(1);
	/** 市价即时---08 */
//	private String eightName = LocalData.forexTradeStyleList.get(0);
	/** 限价即时---07 */
//	private String sevenName = LocalData.forexTradeStyleList.get(1);
	/*** 交易方式代码 */
	private String exchangeTypeCode = null;
	private View limitRateView = null;
	/** 用于区分是定期还是活期，1-活期，2-定期 */
//	private int currencyOrFixTag = 1;
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
//	private String eight = null;
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
	private String weiTuoRate = null;
	/** 止损汇率 */
	private String zhiSunRate = null;
	/** 获利汇率 */
	private String huoLiRate = null;
	/** 追击点差 */
	private String loseRate = null;
	/** 年月日 */
	private String times1 = null;
	/** 时分秒 */
	private String times2 = null;
	private View currentRateView = null;
	private String custonerTimes = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG + " info", "onCreate");
		dealDates();
		init();
		initOnClick();
	}

	@Override
	protected void onStart() {
		super.onStart();
		Intent intent = getIntent();
		investAccount = (String) BaseDroidApp.getInstanse().getBizDataMap().get(Forex.FOREX_RATE_INVESTACCOUNT_RES);
		if (intent == null) {
			return;
		} else {
			exchangeType = intent.getStringExtra(ConstantGloble.FOREX_CHANGETYPE_KEY);
			LogGloble.d(TAG, exchangeType);
			typeText.setText(exchangeType);
			exchangeTypeCode = intent.getStringExtra(Forex.FOREX_EXCHANGETYPE_REQ);

			if (exchangeTypeCode.equals(seven)) {
				loseRateView.setVisibility(View.GONE);
				rate = intent.getStringExtra(ConstantGloble.FOREX_CURRENTRATE_KEY);
				rateText.setText(rate);
				limitRateView.setVisibility(View.VISIBLE);
				limitRate = intent.getStringExtra(ConstantGloble.FOREX_LIMITRATE_KEY);
				LogGloble.d(TAG + "   limitRate", limitRate);
				limitRateText.setText(limitRate);
			} else if (exchangeTypeCode.equals(three) || exchangeTypeCode.equals(four)) {
				loseRateView.setVisibility(View.GONE);
				currentRateView.setVisibility(View.GONE);
				weiTuoRateView.setVisibility(View.VISIBLE);
				timesView.setVisibility(View.VISIBLE);
				weiTuoRate = intent.getStringExtra(ConstantGloble.FOREX_WEITUORATE_KEY);
				times1 = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY1);
				times2 = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY2);
				custonerTimes = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY3);
				weiTuoRateText.setText(weiTuoRate);
				timesText.setText(custonerTimes);
			} else if (exchangeTypeCode.equals(five)) {
				loseRateView.setVisibility(View.GONE);
				currentRateView.setVisibility(View.GONE);
				zhiSunRateView.setVisibility(View.VISIBLE);
				huoLiRateView.setVisibility(View.VISIBLE);
				timesView.setVisibility(View.VISIBLE);
				zhiSunRate = intent.getStringExtra(ConstantGloble.FOREX_ZHISUNRATE_KEY);
				huoLiRate = intent.getStringExtra(ConstantGloble.FOREX_HUOLIRATE_KEY);
				zhiSunRateText.setText(zhiSunRate);
				huoLiRateText.setText(huoLiRate);
				times1 = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY1);
				times2 = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY2);
				custonerTimes = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY3);
				timesText.setText(custonerTimes);
			} else if (exchangeTypeCode.equals(eleven)) {
				loseRateView.setVisibility(View.VISIBLE);
				currentRateView.setVisibility(View.GONE);
				zhiSunRateView.setVisibility(View.GONE);
				huoLiRateView.setVisibility(View.GONE);
				timesView.setVisibility(View.VISIBLE);
				loseRate = intent.getStringExtra(Forex.FOREX_LOSERATE_RES);
				times1 = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY1);
				times2 = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY2);
				custonerTimes = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY3);
				loseRateText.setText(loseRate);
				timesText.setText(custonerTimes);
			}
			radioGroup = intent.getStringExtra(ConstantGloble.FOREX_RADIOBUTTON_KEY);
			sellCode = intent.getStringExtra(ConstantGloble.FOREX_SELLCODESP_KEY);
			buyCode = intent.getStringExtra(ConstantGloble.FOREX_BUYCODESP_KEY);
			sellCodeEditText = intent.getStringExtra(ConstantGloble.FOREX_SELLEDITTEXT_KEY);
			buyCodeEditText = intent.getStringExtra(ConstantGloble.FOREX_BUYEDITTEXT_KEY);
			money = intent.getStringExtra(ConstantGloble.FOREX_COLUMONEY_KEY);
			sellNoDealCode = intent.getStringExtra(ConstantGloble.FOREX_SELLCODENODEAL_KEY);
			buyNoDealCode = intent.getStringExtra(ConstantGloble.FOREX_BUYCODENODEAL_KEY);
			cashNoDealCode = intent.getStringExtra(ConstantGloble.FOREX_CASHNODEAL_KEY);
			LogGloble.d(TAG + "cashNoDealCode", cashNoDealCode);
			if (sellTag.equals(radioGroup)) {
				// 卖出
				codeText.setText(getResources().getString(R.string.forex_trade_buy1));
				codeValueText.setText(buyCode);
				moneyText.setText(getResources().getString(R.string.forex_rate_currency_sellCode));
				String a = null;
				if (LocalData.codeNoNumber.contains(sellNoDealCode)) {
					a = StringUtil.parseStringPattern(sellCodeEditText, twoNumber);
				} else {
					a = StringUtil.parseStringPattern(sellCodeEditText, fourNumber);
				}

				moneyValueText.setText(a);
				moneyCodeValueText.setText(sellCode);
				PopupWindowUtils.getInstance().setOnShowAllTextListener(ForexQuickCurrentConfirmActivity.this, moneyCodeValueText);
			} else if (buyTag.equals(radioGroup)) {
				// 买入
				codeText.setText(getResources().getString(R.string.forex_trade_sell1));
				codeValueText.setText(sellCode);
				moneyText.setText(getResources().getString(R.string.forex_rate_currency_buyCode));
				String b = StringUtil.parseStringCodePattern(buyNoDealCode, money, 2);
				moneyValueText.setText(b);
				moneyCodeValueText.setText(buyCode);
			}

		}
	}

	/**
	 * 初始化所有的控件
	 */
	private void init() {
		rateInfoView = LayoutInflater.from(ForexQuickCurrentConfirmActivity.this).inflate(R.layout.forex_rate_currency_confirm, null);
		tabcontent.addView(rateInfoView);
		setTitle(getResources().getString(R.string.forex_rate_qick1));
		backButton = (Button) findViewById(R.id.ib_back);
		codeText = (TextView) findViewById(R.id.currency_sellcode);
		codeValueText = (TextView) findViewById(R.id.currency_sellcode_code);
		moneyText = (TextView) findViewById(R.id.currency_buycode);
		moneyValueText = (TextView) findViewById(R.id.currency_buycode_money);
		moneyCodeValueText = (TextView) findViewById(R.id.currency_buycode_code);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, moneyCodeValueText);
		rateText = (TextView) findViewById(R.id.currency_rate);
		typeText = (TextView) findViewById(R.id.currency_type);
		limitRateText = (TextView) findViewById(R.id.currency_limitRate);
		limitRateView = findViewById(R.id.limitRate_view);
		weiTuoRateView = findViewById(R.id.weiTuoRate_view);
		zhiSunRateView = findViewById(R.id.zhiSunRate_view);
		huoLiRateView = findViewById(R.id.huoLiRate_view);
		loseRateView = findViewById(R.id.loseRate_view);
		timesView = findViewById(R.id.times_view);
		weiTuoRateText = (TextView) findViewById(R.id.currency_weiTuoRate);
		zhiSunRateText = (TextView) findViewById(R.id.currency_zhiSunRate);
		huoLiRateText = (TextView) findViewById(R.id.currency_huoLiRate);
		loseRateText = (TextView) findViewById(R.id.currency_loseRate);
		currentRateView = findViewById(R.id.current_rate_layout);
		timesText = (TextView) findViewById(R.id.currency_times);
		limitRateView.setVisibility(View.GONE);
		weiTuoRateView.setVisibility(View.GONE);
		zhiSunRateView.setVisibility(View.GONE);
		huoLiRateView.setVisibility(View.GONE);
		timesView.setVisibility(View.GONE);
		sureButton = (Button) findViewById(R.id.sureButton);
		StepTitleUtils.getInstance().initTitldStep(this, new String[] { this.getResources().getString(R.string.forex_rate_tep1), this.getResources().getString(R.string.forex_rate_tep2), this.getResources().getString(R.string.forex_rate_tep3) });
		StepTitleUtils.getInstance().setTitleStep(2);
	}

	private void dealDates() {
//		eight = LocalData.forexTradeStyleTransList.get(0);
		seven = LocalData.forexTradeStyleTransList.get(1);
		three = LocalData.forexTradeStyleTransList.get(2);
		four = LocalData.forexTradeStyleTransList.get(3);
		five = LocalData.forexTradeStyleTransList.get(4);
		eleven = LocalData.forexTradeStyleTransList.get(5);
	}

	public void initOnClick() {
		backButton.setOnClickListener(this);
		sureButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:// 返回按钮
			finish();
			break;
		// case R.id.lastButton:// 上一步
		// finish();
		// break;
		case R.id.sureButton:// 确定
			if ("".equals(exchangeType)) {
				return;
			}
			if ("".equals(buyNoDealCode)) {
				return;
			}
			if ("".equals(sellNoDealCode)) {
				return;
			}
			if ("".equals(cashNoDealCode)) {
				return;
			}
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();
			break;

		default:
			break;
		}
	}

	/**
	 * 请求ConversationId--回调
	 */

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		if (StringUtil.isNull(commConversationId)) {
			return;
		} else {
			requestPSNGetTokenId();
		}
	}

	/**
	 * 获取tocken
	 */
	private void requestPSNGetTokenId() {
		getHttpTools().requestHttp(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API, "requestPSNGetTokenIdCallback", null, true);
	}

	/**
	 * 获取tokenId----回调
	 * 
	 * @param resultObj
	 *            :返回结果
	 */
	public void requestPSNGetTokenIdCallback(Object resultObj) {
		tokenId = getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			int type = Integer.valueOf(exchangeTypeCode);
			LogGloble.d(TAG + " type", String.valueOf(type));
			if (buyTag.equals(radioGroup)) {
				// 买入
				buyRequestDate(type);
			} else {
				// 卖出
				sellRequestDate(type);
			}
		}
	}

	/** 卖出请求数据 */
	private void sellRequestDate(int type) {
		switch (type) {
		case ConstantGloble.FOREX_TRADE_SEVEN:// 限价即时
			requestCurrencyPsnForexTrade(investAccount, buyNoDealCode, sellNoDealCode, radioGroup, sellCodeEditText, null, exchangeTypeCode, rate, limitRate, cashNoDealCode, null, null, null, null, null, tokenId, null, null);
			break;
		case ConstantGloble.FOREX_TRADE_EIGHT:// 市价即时
			requestCurrencyPsnForexTrade(investAccount, buyNoDealCode, sellNoDealCode, radioGroup, sellCodeEditText, null, exchangeTypeCode, rate, null, cashNoDealCode, null, null, null, null, null, tokenId, null, null);
			break;
		case ConstantGloble.FOREX_TRADE_THREE:// 获利委托
			requestCurrencyPsnForexTrade(investAccount, buyNoDealCode, sellNoDealCode, radioGroup, sellCodeEditText, null, exchangeTypeCode, null, null, cashNoDealCode, null, null, weiTuoRate, null, null, tokenId, times1, times2);
			break;
		case ConstantGloble.FOREX_TRADE_FOUR:// 止损委托
			requestCurrencyPsnForexTrade(investAccount, buyNoDealCode, sellNoDealCode, radioGroup, sellCodeEditText, null, exchangeTypeCode, null, null, cashNoDealCode, null, null, null, null, weiTuoRate, tokenId, times1, times2);
			break;
		case ConstantGloble.FOREX_TRADE_FIVE:// 二选一委托
			requestCurrencyPsnForexTrade(investAccount, buyNoDealCode, sellNoDealCode, radioGroup, sellCodeEditText, null, exchangeTypeCode, null, null, cashNoDealCode, null, null, huoLiRate, null, zhiSunRate, tokenId, times1, times2);
			break;
		case ConstantGloble.FOREX_TRADE_ELEVEN:// 追击止损委托
			requestCurrencyPsnForexTrade(investAccount, buyNoDealCode, sellNoDealCode, radioGroup, sellCodeEditText, null, exchangeTypeCode, null, null, cashNoDealCode, null, null, null, null, loseRate, tokenId, times1, times2);
			break;
		default:
			break;
		}
	}

	/** 买入请求数据 */
	private void buyRequestDate(int type) {
		switch (type) {
		case ConstantGloble.FOREX_TRADE_SEVEN:// 限价即时
			requestCurrencyPsnForexTrade(investAccount, buyNoDealCode, sellNoDealCode, radioGroup, null, buyCodeEditText, exchangeTypeCode, rate, limitRate, cashNoDealCode, null, null, null, null, null, tokenId, null, null);
			break;
		case ConstantGloble.FOREX_TRADE_EIGHT:// 市价即时
			requestCurrencyPsnForexTrade(investAccount, buyNoDealCode, sellNoDealCode, radioGroup, null, buyCodeEditText, exchangeTypeCode, rate, null, cashNoDealCode, null, null, null, null, null, tokenId, null, null);
			break;
		case ConstantGloble.FOREX_TRADE_THREE:// 获利委托
			requestCurrencyPsnForexTrade(investAccount, buyNoDealCode, sellNoDealCode, radioGroup, null, buyCodeEditText, exchangeTypeCode, rate, null, cashNoDealCode, null, null, weiTuoRate, null, null, tokenId, times1, times2);
			break;
		case ConstantGloble.FOREX_TRADE_FOUR:// 止损委托
			requestCurrencyPsnForexTrade(investAccount, buyNoDealCode, sellNoDealCode, radioGroup, null, buyCodeEditText, exchangeTypeCode, rate, null, cashNoDealCode, null, null, null, null, weiTuoRate, tokenId, times1, times2);
			break;
		case ConstantGloble.FOREX_TRADE_FIVE:// 二选一委托
			requestCurrencyPsnForexTrade(investAccount, buyNoDealCode, sellNoDealCode, radioGroup, null, buyCodeEditText, exchangeTypeCode, rate, null, cashNoDealCode, null, null, huoLiRate, null, zhiSunRate, tokenId, times1, times2);
			break;
		default:
			break;
		}
	}

	/**
	 * 外汇买卖确认提交--回调
	 * 
	 * @param resultObj
	 */
	public void requestCurrencyPsnForexTradeCallback(Object resultObj) {
		super.requestCurrencyPsnForexTradeCallback(resultObj);
		Map<String, Object> tradeResult = getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(tradeResult)) {
			return;
		} else {
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_TRADERESULT_KEY, tradeResult);
			Intent intent = new Intent(ForexQuickCurrentConfirmActivity.this, ForexQuickCurrentSuccessActivity.class);
			intent.putExtra(ConstantGloble.FOREX_CHANGETYPE_KEY, exchangeType);// 交易方式名称
			intent.putExtra(Forex.FOREX_EXCHANGETYPE_REQ, exchangeTypeCode);// 交易方式
			intent.putExtra(Forex.FOREX_RATE_INVESTACCOUNT_RES, investAccount);
			intent.putExtra(ConstantGloble.FOREX_SELLCODESP_KEY, sellCode);// 卖出币种名称+钞汇
			intent.putExtra(ConstantGloble.FOREX_BUYCODESP_KEY, buyCode);// 买入币种名称
			intent.putExtra(ConstantGloble.FOREX_SELLCODENODEAL_KEY, sellNoDealCode);// 卖出币种代码
			intent.putExtra(ConstantGloble.FOREX_BUYCODENODEAL_KEY, buyNoDealCode);// 买入币种代码
			intent.putExtra(Forex.FOREX_CASHREMIT_REQ, cashNoDealCode);
			if (exchangeTypeCode.equals(three) || exchangeTypeCode.equals(four)) {
				intent.putExtra(ConstantGloble.FOREX_WEITUORATE_KEY, weiTuoRate);// 委托汇率
			} else if (exchangeTypeCode.equals(eleven)) {
				intent.putExtra(Forex.FOREX_LOSERATE_RES, loseRate);
			}
			if (sellTag.equals(radioGroup)) {
				// 1-- 卖出
				intent.putExtra(ConstantGloble.FOREX_RADIOBUTTON_KEY, sellTag);// 卖出标志
				intent.putExtra(ConstantGloble.FOREX_SELLEDITTEXT_KEY, sellCodeEditText);// 卖出金额
			} else if (buyTag.equals(radioGroup)) {
				// 0-- 买入
				intent.putExtra(ConstantGloble.FOREX_RADIOBUTTON_KEY, buyTag);// 买入标志
				intent.putExtra(ConstantGloble.FOREX_COLUMONEY_KEY, money);// 买入金额
			}
			startActivity(intent);
		}
	}
}
