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

/***
 * 定期交易成功页面
 * 
 * @author Administrator
 * 
 */
public class ForexQuickTradeSuccessActivity extends ForexBaseActivity {
	private static final String TAG = "ForexQuickTradeSuccessActivity";
	/**
	 * ForexQuickTradeSuccessActivity的主布局
	 */
	private View rateInfoView = null;
	/**
	 * 返回按钮
	 */
	private Button backButton = null;
	private Button sureButton = null;
	/** 交易序号 */
	private TextView numberText = null;
	/** 交易账户 */
	private TextView accNumber = null;
	private TextView volumberNumbetText = null;
	private TextView cdNumberText = null;
	/** 卖出币种金额 */
	private TextView sellCodeText = null;
	/** 卖出币种 */
	private TextView sellCodeCodeText = null;
	/** 买入币种金额 */
	// private TextView buyCodeText = null;
	/** 买入币种 */
	private TextView buyCodeCodeTex = null;
	private TextView exchangeTypeText = null;
	private TextView baseRateText = null;
	private TextView exchangeRateText = null;
	private TextView buyCodeMoneyText = null;
	/** 交易账户 */
	private String account = null;
	private TextView sellCodeLeftText = null;
	private TextView buyCodeLeftText = null;
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
	private String weiTuoRate = null;
	private View jiChuRateView = null;
	private View youHuiRateView = null;
	/** 委托序号 */
	private TextView weiTuoNumber = null;
	/** 委托序号 view */
	private View weiTuoNumberView = null;
	private TextView leftText = null;
	private TextView topText = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.forex_rate_qick1));
		dealDates();
		init();
	}

	public void init() {
		rateInfoView = LayoutInflater.from(ForexQuickTradeSuccessActivity.this).inflate(R.layout.forex_rate_fix_success, null);
		tabcontent.addView(rateInfoView);

		backButton = (Button) findViewById(R.id.ib_back);
		sureButton = (Button) findViewById(R.id.sureButton);
		numberText = (TextView) findViewById(R.id.rate_fix_number);
		accNumber = (TextView) findViewById(R.id.rate_fix_accNumber);
		volumberNumbetText = (TextView) findViewById(R.id.forex_rate_fix_accNumber);
		cdNumberText = (TextView) findViewById(R.id.forex_rate_fix_number);
		sellCodeText = (TextView) findViewById(R.id.rate_fix_sellCode);
		sellCodeCodeText = (TextView) findViewById(R.id.fix_sellcode_code);
		buyCodeMoneyText = (TextView) findViewById(R.id.currency_buycode_money);
		buyCodeCodeTex = (TextView) findViewById(R.id.currency_buycode_code);
		exchangeTypeText = (TextView) findViewById(R.id.rate_currency_type);
		baseRateText = (TextView) findViewById(R.id.rate_fix_papRate);
		exchangeRateText = (TextView) findViewById(R.id.rate_fix_comRate);
		sellCodeLeftText = (TextView) findViewById(R.id.fix_sellcode);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(ForexQuickTradeSuccessActivity.this, sellCodeLeftText);
		buyCodeLeftText = (TextView) findViewById(R.id.currency_buycode);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(ForexQuickTradeSuccessActivity.this, buyCodeLeftText);
		leftText = (TextView) findViewById(R.id.left_text);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(ForexQuickTradeSuccessActivity.this, leftText);
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
		PopupWindowUtils.getInstance().setOnShowAllTextListener(ForexQuickTradeSuccessActivity.this, buyCodeLeftText);
		// buyCodeLeftText.setText(getResources().getString(R.string.forex_trade_buy1));
		StepTitleUtils.getInstance().initTitldStep(this, new String[] { this.getResources().getString(R.string.forex_rate_tep1), this.getResources().getString(R.string.forex_rate_tep2), this.getResources().getString(R.string.forex_rate_tep3) });

		StepTitleUtils.getInstance().setTitleStep(3);
		backButton.setVisibility(View.GONE);// 隐藏返回按钮
		topText = (TextView) findViewById(R.id.top_text);
		initData();
		initOnClick();
	}

	private void dealDates() {
		eight = LocalData.forexTradeStyleTransList.get(0);
		seven = LocalData.forexTradeStyleTransList.get(1);
		three = LocalData.forexTradeStyleTransList.get(2);
		four = LocalData.forexTradeStyleTransList.get(3);
		five = LocalData.forexTradeStyleTransList.get(4);
		eleven = LocalData.forexTradeStyleTransList.get(5);
	}

	/** 初始化页面数据 */
	@SuppressWarnings("unchecked")
	public void initData() {
		// 委托序号
		String consignNumber = null;
		// 委托到期时间
		String dueDate = null;
		// 获利汇率
		String winRate = null;
		// 止损汇率
		String loseRate = null;
		String buyAmount = null;
		Map<String, Object> tradeResult = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.FOREX_TRADERESULT_KEY);
		if (StringUtil.isNullOrEmpty(tradeResult)) {
			return;
		} else {
			String transactionId = (String) tradeResult.get(Forex.FOREX_TRANSACTIONID_RES);
			String baseRate = (String) tradeResult.get(Forex.FOREX_BASERATE_RES);
			String exchangeRate = (String) tradeResult.get(Forex.FOREX_EXCHANGERATE_RES);
			dueDate = (String) tradeResult.get(Forex.FOREX_DUEDATE_RES);
			winRate = (String) tradeResult.get(Forex.FOREX_WINRATE_RES);
			loseRate = (String) tradeResult.get(Forex.FOREX_LOSERATE_RES);
			consignNumber = (String) tradeResult.get(Forex.FOREX_CONSIGNNUMBERT_RES);
			buyAmount = (String) tradeResult.get(Forex.FOREX_BUYAMOUNT);
			numberText.setText(transactionId);
			baseRateText.setText(baseRate);
			exchangeRateText.setText(exchangeRate);
		}
		// account = StringUtil.getForSixForString(account);
		// accNumber.setText(account);
		Intent intent = getIntent();
		if (intent == null) {
			return;
		} else {
			String volumeNumber = intent.getStringExtra(Forex.FOREX_VOLUMENUMBER_REQ);
			volumberNumbetText.setText(volumeNumber);
			String cdNumber = intent.getStringExtra(Forex.FOREX_CDNUMBER_REQ);
			cdNumberText.setText(cdNumber);
			String exchangeType = intent.getStringExtra(ConstantGloble.FOREX_CHANGETYPE_KEY);
			LogGloble.d(TAG + " exchangeType", exchangeType);
			exchangeTypeText.setText(exchangeType);
			// 交易方式代码
			String exchangeTypeCode = intent.getStringExtra(Forex.FOREX_EXCHANGETYPE_REQ);
			if (!StringUtil.isNull(exchangeTypeCode)) {
				if (exchangeTypeCode.equals(seven) || exchangeTypeCode.equals(eight)) {
				} else {
					topText.setText(getResources().getString(R.string.forex_wt_success_top_title));
				}
			}
			showView(exchangeTypeCode);
			weiTuoRate = intent.getStringExtra(ConstantGloble.FOREX_WEITUORATE_KEY);
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
				loseRateText.setText(getIntent().getStringExtra(Forex.FOREX_LOSERATE_RES));
				if (StringUtil.isNull(consignNumber)) {
					weiTuoNumber.setText("-");
				} else {
					weiTuoNumber.setText(consignNumber);
				}
				timesText.setText(dueDate);
			}

			// 卖出金额
			String sellMoney = intent.getStringExtra(Forex.FOREX_SAMOUNT_REQ);
			// 卖出币种名称
			String sellCodeName = intent.getStringExtra(ConstantGloble.FOREX_ONLYSELLCODEDEALUSER_KEY);
			// 卖出币种代码
			String sCurrency = intent.getStringExtra(Forex.FOREX_SCURRENCYT_REQ);
			// 钞汇
			String cashRemit = intent.getStringExtra(Forex.FOREX_CASHREMIT_REQ);// 钞汇
			String cash = LocalData.CurrencyCashremit.get(cashRemit);
			// 买入币种名称
			String buyCodeName = intent.getStringExtra(ConstantGloble.FOREX_BUYCODESP_KEY);
			// 买入币种代码
			String bCurrency = intent.getStringExtra(Forex.FOREX_BCURRENCY_REQ);
			String m = StringUtil.parseStringCodePattern(sCurrency, sellMoney, fourNumber);
			sellCodeText.setText(m);
			sellCodeCodeText.setText(sellCodeName + " " + cash);
			buyCodeMoneyText.setVisibility(View.VISIBLE);
			buyCodeCodeTex.setText(buyCodeName + " " + cash);
			String buy = StringUtil.parseStringCodePattern(bCurrency, buyAmount, 2);
			buyCodeMoneyText.setText(buy);
		}

	}

	private void showView(String type) {
		if (type.equals(seven) || type.equals(eight)) {
			weiTuoNumberView.setVisibility(View.GONE);
			loseRateView.setVisibility(View.GONE);
		} else if (type.equals(three) || type.equals(four)) {
			weiTuoRateView.setVisibility(View.VISIBLE);
			timesView.setVisibility(View.VISIBLE);
			jiChuRateView.setVisibility(View.GONE);
			youHuiRateView.setVisibility(View.GONE);
			weiTuoNumberView.setVisibility(View.VISIBLE);
			loseRateView.setVisibility(View.GONE);
		} else if (type.equals(five)) {
			huoLiRateView.setVisibility(View.VISIBLE);
			zhiSunRateView.setVisibility(View.VISIBLE);
			timesView.setVisibility(View.VISIBLE);
			jiChuRateView.setVisibility(View.GONE);
			youHuiRateView.setVisibility(View.GONE);
			weiTuoNumberView.setVisibility(View.VISIBLE);
			loseRateView.setVisibility(View.GONE);
		} else if (type.equals(eleven)) {
			huoLiRateView.setVisibility(View.GONE);
			zhiSunRateView.setVisibility(View.GONE);
			timesView.setVisibility(View.VISIBLE);
			jiChuRateView.setVisibility(View.GONE);
			youHuiRateView.setVisibility(View.GONE);
			weiTuoNumberView.setVisibility(View.VISIBLE);
			loseRateView.setVisibility(View.VISIBLE);
		}
	}

	public void initOnClick() {
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 返回到我的外汇页面
				Intent intent = new Intent(ForexQuickTradeSuccessActivity.this, ForexCustomerRateInfoActivity.class);
				startActivity(intent);
				finish();
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
