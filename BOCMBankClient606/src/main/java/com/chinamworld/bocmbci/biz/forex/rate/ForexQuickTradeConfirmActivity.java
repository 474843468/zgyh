package com.chinamworld.bocmbci.biz.forex.rate;

import java.util.HashMap;
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
 * 定期确认页面
 * 
 * @author Administrator
 * 
 */
public class ForexQuickTradeConfirmActivity extends ForexBaseActivity {
	private static final String TAG = "ForexQuickTradeConfirmActivity";
	/**
	 * ForexQuickTradeConfirmActivity的主布局
	 */
	private View rateInfoView = null;
	/**
	 * 返回按钮
	 */
	private Button backButton = null;
	private TextView typeText = null;
	private TextView limitRateText = null;
	private TextView volumeNumberText = null;
	private TextView cdNumberText = null;
	private TextView buyCodeText = null;
	private TextView rateText = null;
	/** 限价汇率View */
	private View limitRateView = null;
	/** 上一步按钮 */
	// private Button lastButton = null;
	private Button sureButton = null;
	private String tokenId = null;
	/** 投资账号 */
	private String investAccount = null;
	/** 买入币别 */
	private String bCurrency = null;
	/** 卖出币别 */
	private String sCurrency = null;
	/** 卖出金额 */
	private String sAmount = null;
	/** 交易类型 */
	private String exchangeType = null;
	/** 市价汇率 */
	private String exchangeRate = null;
	/** 限价汇率 */
	private String limitRate = null;
	/** 钞汇标识 */
	private String cashRemit = null;
	/*** 定一本册号 */
	private String volumeNumber = null;
	/** 存单号 */
	private String cdNumber = null;
	/** 储种 **/
	private String cDType = null;
	/** 交易方式 */
	private String exchangeTypeTrans = null;
	/** 卖出币种名称 */
	private String sCodeTrans = null;
	/** 买入币种名称 */
	private String bCodeTrans = null;
	/** 当前汇率View */
	private View currentRateView = null;
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
	/** 止损汇率-追击止损委托时使用，追击点差 */
	private String loseRate = null;
	/** 年月日 */
	private String times1 = null;
	/** 时分秒 */
	private String times2 = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		dealDates();
		init();
		inintOnClick();
	}

	private void dealDates() {
//		eight = LocalData.forexTradeStyleTransList.get(0);
		seven = LocalData.forexTradeStyleTransList.get(1);
		three = LocalData.forexTradeStyleTransList.get(2);
		four = LocalData.forexTradeStyleTransList.get(3);
		five = LocalData.forexTradeStyleTransList.get(4);
		eleven = LocalData.forexTradeStyleTransList.get(5);
	}

	@Override
	protected void onStart() {
		super.onStart();
		investAccount = (String) BaseDroidApp.getInstanse().getBizDataMap().get(Forex.FOREX_RATE_INVESTACCOUNT_RES);
		Intent intent = getIntent();
		if (intent == null) {
			return;
		} else {
			exchangeType = intent.getStringExtra(Forex.FOREX_EXCHANGETYPE_REQ);
			exchangeTypeTrans = intent.getStringExtra(ConstantGloble.FOREX_CHANGETYPE_KEY);
			LogGloble.d(TAG + " " + "exchangeTypeTrans", exchangeTypeTrans);
			typeText.setText(exchangeTypeTrans);
			if (exchangeType.equals(seven)) {
				loseRateView.setVisibility(View.GONE);
				currentRateView.setVisibility(View.VISIBLE);
				exchangeRate = intent.getStringExtra(ConstantGloble.FOREX_CURRENTRATE_KEY);
				rateText.setText(exchangeRate);
				limitRateView.setVisibility(View.VISIBLE);
				limitRate = intent.getStringExtra(ConstantGloble.FOREX_LIMITRATE_KEY);
				limitRateText.setText(limitRate);
			} else if (exchangeType.equals(three) || exchangeType.equals(four)) {
				loseRateView.setVisibility(View.GONE);
				currentRateView.setVisibility(View.GONE);
				weiTuoRateView.setVisibility(View.VISIBLE);
				timesView.setVisibility(View.VISIBLE);
				weiTuoRate = intent.getStringExtra(ConstantGloble.FOREX_WEITUORATE_KEY);
				times1 = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY1);
				times2 = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY2);
				String times = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY3);
				weiTuoRateText.setText(weiTuoRate);
				timesText.setText(times);
			} else if (exchangeType.equals(five)) {
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
				String times = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY3);
				timesText.setText(times);
			} else if (exchangeType.equals(eleven)) {
				loseRateView.setVisibility(View.VISIBLE);
				currentRateView.setVisibility(View.GONE);
				zhiSunRateView.setVisibility(View.GONE);
				huoLiRateView.setVisibility(View.GONE);
				timesView.setVisibility(View.VISIBLE);
				
				loseRate = intent.getStringExtra(Forex.FOREX_LOSERATE_RES);
				times1 = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY1);
				times2 = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY2);
				
				loseRateText.setText(loseRate);
				String times = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY3);
				timesText.setText(times);
			}

			volumeNumber = intent.getStringExtra(Forex.FOREX_VOLUMENUMBER_REQ);
			volumeNumberText.setText(volumeNumber);

			String cdNumberTypeCode = intent.getStringExtra(ConstantGloble.FOREX_SELLTRANSCODE_KEY);
			cdNumberText.setText(cdNumberTypeCode);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this, cdNumberText);
			bCodeTrans = intent.getStringExtra(ConstantGloble.FOREX_BUYCODESP_KEY);
			buyCodeText.setText(bCodeTrans);

			bCurrency = intent.getStringExtra(Forex.FOREX_BCURRENCY_REQ);
			sCurrency = intent.getStringExtra(Forex.FOREX_SCURRENCYT_REQ);
			sAmount = intent.getStringExtra(Forex.FOREX_SAMOUNT_REQ);
			cashRemit = intent.getStringExtra(Forex.FOREX_CASHREMIT_REQ);
			cdNumber = intent.getStringExtra(Forex.FOREX_CDNUMBER_REQ);
			cDType = intent.getStringExtra(Forex.FOREX_CDTYPE_REQ);
			sCodeTrans = intent.getStringExtra(ConstantGloble.FOREX_ONLYSELLCODEDEALUSER_KEY);
		}
	}

	private void init() {
		rateInfoView = LayoutInflater.from(ForexQuickTradeConfirmActivity.this).inflate(R.layout.forex_rate_fix_cinfirm, null);
		tabcontent.removeAllViews();
		tabcontent.addView(rateInfoView);
		setTitle(getResources().getString(R.string.forex_rate_qick1));
		backButton = (Button) findViewById(R.id.ib_back);
		limitRateText = (TextView) findViewById(R.id.forex_rate_currency_limitRate);
		limitRateView = findViewById(R.id.fix_limitRate);
		limitRateView.setVisibility(View.GONE);
		typeText = (TextView) findViewById(R.id.forex_rate_currency_type);
		volumeNumberText = (TextView) findViewById(R.id.forex_rate_fix_accNumber);
		cdNumberText = (TextView) findViewById(R.id.forex_rate_fix_number);
		buyCodeText = (TextView) findViewById(R.id.forex_trade_buy1);
		rateText = (TextView) findViewById(R.id.forex_rate_currency_rate);
		weiTuoRateView = findViewById(R.id.weiTuoRate_view);
		zhiSunRateView = findViewById(R.id.zhiSunRate_view);
		huoLiRateView = findViewById(R.id.huoLiRate_view);
		loseRateView = findViewById(R.id.loseRate_view);
		timesView = findViewById(R.id.times_view);
		weiTuoRateText = (TextView) findViewById(R.id.currency_weiTuoRate);
		zhiSunRateText = (TextView) findViewById(R.id.currency_zhiSunRate);
		huoLiRateText = (TextView) findViewById(R.id.currency_huoLiRate);
		loseRateText = (TextView) findViewById(R.id.currency_loseRate);
		timesText = (TextView) findViewById(R.id.currency_times);
		currentRateView = findViewById(R.id.current_rate_layout);
		limitRateView.setVisibility(View.GONE);
		weiTuoRateView.setVisibility(View.GONE);
		zhiSunRateView.setVisibility(View.GONE);
		huoLiRateView.setVisibility(View.GONE);
		loseRateView.setVisibility(View.GONE);
		timesView.setVisibility(View.GONE);
		// lastButton = (Button) findViewById(R.id.lastButton);
		sureButton = (Button) findViewById(R.id.sureButton);
		StepTitleUtils.getInstance().initTitldStep(this, new String[] { this.getResources().getString(R.string.forex_rate_tep1), this.getResources().getString(R.string.forex_rate_tep2), this.getResources().getString(R.string.forex_rate_tep3) });
		StepTitleUtils.getInstance().setTitleStep(2);
	}

	public void inintOnClick() {
		// 返回按钮
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		// 上一步
		// lastButton.setOnClickListener(new OnClickListener() {

		// @Override
		// public void onClick(View v) {
		// finish();
		// }
		// });
		// 确定
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();
			}
		});
	}

	/**
	 * 请求ConversationId--回调
	 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		if (StringUtil.isNull(commConversationId)) {
			BaseHttpEngine.dissMissProgressDialog();
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
		LogGloble.d(TAG + " tokenId", tokenId);
		if (StringUtil.isNull(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			if (StringUtil.isNull(investAccount)) {
				BaseHttpEngine.dissMissProgressDialog();
				return;
			} else {
				LogGloble.d(TAG + " investAccount", investAccount);
				Map<String, Object> params = getParams();
				getHttpTools().requestHttp(Forex.FOREX_PSNFOREXTRADE_API, "requestPsnForexTradeCallback", params, true);
			}
		}
	}

	/**
	 * 获取参数
	 * 
	 * @return 参数列表
	 */
	private Map<String, Object> getParams() {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put(Forex.FOREX_INVESTACCOUNT_REQ, investAccount);
		params.put(Forex.FOREX_BCURRENCY_REQ, bCurrency);
		params.put(Forex.FOREX_SCURRENCYT_REQ, sCurrency);
		params.put(Forex.FOREX_TRANSFLAG_REQ, LocalData.forexTradeSellOrBuyList.get(1));
		params.put(Forex.FOREX_EXCHANGETYPET_REQ, exchangeType);

		params.put(Forex.FOREX_CASHREMIT_REQ, cashRemit);
		params.put(Forex.FOREX_VOLUMENUMBER_REQ, volumeNumber);
		params.put(Forex.FOREX_CDNUMBER_REQ, cdNumber);
		params.put(Forex.FOREX_TOKEN_REQ, tokenId);
		params.put(Forex.FOREX_CDTYPE_REQ, cDType);

		// 卖出
		String s = sAmount;
		// 小数位数最多为2位
		s = StringUtil.splitStringwithCode(sCurrency, s, 2);
		params.put(Forex.FOREX_SAMOUNT_REQ, s);

		int type = Integer.valueOf(exchangeType);
		switch (type) {
		case ConstantGloble.FOREX_TRADE_SEVEN:// 限价即时
			params.put(Forex.FOREX_EXCHANGERATE, exchangeRate);
			params.put(Forex.FOREX_LIMITRATE_REQ, limitRate);
			break;
		case ConstantGloble.FOREX_TRADE_EIGHT:// 市价即时
			params.put(Forex.FOREX_EXCHANGERATE, exchangeRate);
			break;
		case ConstantGloble.FOREX_TRADE_THREE:// 获利委托
			params.put(Forex.FOREX_WINRATE_REQ, weiTuoRate);
			params.put(Forex.FOREX_CONSIGNAYS_REQ, times1);
			params.put(Forex.FOREX_CONSIGNHOUR_REQ, times2);
			break;
		case ConstantGloble.FOREX_TRADE_FOUR:// 止损委托
			params.put(Forex.FOREX_LOSERATE_REQ, weiTuoRate);
			params.put(Forex.FOREX_CONSIGNAYS_REQ, times1);
			params.put(Forex.FOREX_CONSIGNHOUR_REQ, times2);
			break;
		case ConstantGloble.FOREX_TRADE_FIVE:// 二选一委托
			params.put(Forex.FOREX_WINRATE_REQ, huoLiRate);
			params.put(Forex.FOREX_LOSERATE_REQ, zhiSunRate);
			params.put(Forex.FOREX_CONSIGNAYS_REQ, times1);
			params.put(Forex.FOREX_CONSIGNHOUR_REQ, times2);
			break;
		case ConstantGloble.FOREX_TRADE_ELEVEN:// 追击止损委托
			params.put(Forex.FOREX_LOSERATE_REQ, loseRate);
			params.put(Forex.FOREX_CONSIGNAYS_REQ, times1);
			params.put(Forex.FOREX_CONSIGNHOUR_REQ, times2);
			break;
		default:
			break;
		}
		return params;
	}

	/**
	 * 外汇买卖确认提交--回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnForexTradeCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> tradeResult = getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(tradeResult)) {
			return;
		} else {
			String exchangeRate = (String) tradeResult.get(Forex.FOREX_EXCHANGERATE_RES);
			LogGloble.d(TAG + " exchangeRate", exchangeRate);
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_TRADERESULT_KEY, tradeResult);
			Intent intent = new Intent(ForexQuickTradeConfirmActivity.this, ForexQuickTradeSuccessActivity.class);
			intent.putExtra(Forex.FOREX_VOLUMENUMBER_REQ, volumeNumber);// 存折册号
			intent.putExtra(Forex.FOREX_CDNUMBER_REQ, cdNumber);// 存单序号
			intent.putExtra(Forex.FOREX_SAMOUNT_REQ, sAmount);// 卖出金额
			intent.putExtra(ConstantGloble.FOREX_CHANGETYPE_KEY, exchangeTypeTrans);// 交易方式
			intent.putExtra(ConstantGloble.FOREX_ONLYSELLCODEDEALUSER_KEY, sCodeTrans);// 卖出币种名称
			intent.putExtra(ConstantGloble.FOREX_BUYCODESP_KEY, bCodeTrans);// 买入币种名称
			intent.putExtra(Forex.FOREX_SCURRENCYT_REQ, sCurrency);// 卖出币种代码
			intent.putExtra(Forex.FOREX_BCURRENCY_REQ, bCurrency);// 买入币种代码
			intent.putExtra(Forex.FOREX_CASHREMIT_REQ, cashRemit);// 钞汇
			intent.putExtra(Forex.FOREX_EXCHANGETYPE_REQ, exchangeType);
			if (exchangeType.equals(three) || exchangeType.equals(four)) {
				intent.putExtra(ConstantGloble.FOREX_WEITUORATE_KEY, weiTuoRate);// 委托汇率
			} else if (exchangeType.equals(eleven)) {
				intent.putExtra(Forex.FOREX_LOSERATE_RES, loseRate);// 追击点差 
			}
			startActivity(intent);
		}
	}
}
