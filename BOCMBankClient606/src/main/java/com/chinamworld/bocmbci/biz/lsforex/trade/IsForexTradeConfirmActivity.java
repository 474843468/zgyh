package com.chinamworld.bocmbci.biz.lsforex.trade;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 快速交易 确认页面 */
public class IsForexTradeConfirmActivity extends IsForexBaseActivity {
	public static final String TAG = "IsForexTradeConfirmActivity";
	/** IsForexTradeConfirmActivity的主布局 */
	private View rateInfoView = null;
	/** 返回按钮 */
	private Button backButton = null;
	/** 交易账户 */
	private TextView accText = null;
	/** 交易方式 */
	private TextView typeText = null;
	/** 交易货币对 */
	private TextView codeText = null;
	/** 结算币种 */
	private TextView jsCodeText = null;
	/** 买卖方向 */
	private TextView sellTagText = null;
	/** 当前汇率 */
	private TextView rateText = null;
	/** 限价汇率 */
	private TextView limiRateText = null;
	/** 交易金额 */
	private TextView jyMoneyText = null;
	/** 确定按钮 */
	private Button sureButton = null;
	/** 建仓标志 */
	private TextView jcTagText = null;
	/** 交易方式代码 */
	private String exchangeTypeCode = null;
	/** 结算币种代码 */
	private String jsCodeCode = null;
	/** 买卖方向代码 */
	private String sellTageCode = null;
	/** 货币对代码 */
	private String codeCode = null;
	/** 建仓标志代码 */
	private String jcTagCode = null;
	/** 交易金额 */
	private String money = null;
	/** 限价汇率 */
	private String limitRate = null;
	/** 当前汇率 */
	private String rate = null;
	// 货币对名称
	private String codeCodeName = null;
	private String token = null;
	/** 委托汇率View */
	private View weiTuoRateView = null;
	/** 止损汇率View */
	private View zhiSunRateView = null;
	/** 获利汇率View */
	private View huoLiRateView = null;
	/** 截止时间View */
	private View timesView = null;
	private View limitRateView = null;
	/** 委托汇率 */
	private TextView weiTuoRateText = null;
	/** 止损汇率 */
	private TextView zhiSunRateText = null;
	/** 获利汇率 */
	private TextView huoLiRateText = null;
	/** 截止时间 */
	private TextView timesText = null;
	/** 委托汇率 */
	private String weiTuoRate = null;
	/** 止损汇率 */
	private String zhiSunRate = null;
	/** 获利汇率 */
	private String huoLiRate = null;
	/** 年月日 */
	private String times1 = null;
	/** 时分秒 */
	private String times2 = null;
	private View currentRateView = null;
	private String custonerTimes = null;
	private String shiJiaCode = null;
	private String xianJiaCode = null;
	private String weiTuoCode = null;
	private String zhiSunCode = null;
	private String twoCode = null;
	
	//wuhan
	private String zhuijizhisunCode = null;
	private View zhijizhisun_layout;
	private TextView forex_zhuijidiancha;
	private String diancha;
	
	//指定平仓
	private int point =9;
	private int position ;
	String internaNumber ;
	String consignNumber;
	
	private LinearLayout lyt_isForex_query_jcTag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.isForex_trade));
		LogGloble.d(TAG, "onCreate");
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		if (StringUtil.isNull(commConversationId)) {
			return;
		}
		getTypeCode();
		init();
		initDate();
		initClick();
	}

	private void getTypeCode() {
		shiJiaCode = LocalData.isForexExchangeTypeCodeList.get(0);
		xianJiaCode = LocalData.isForexExchangeTypeCodeList.get(1);
		weiTuoCode = LocalData.isForexExchangeTypeCodeList.get(2);
		zhiSunCode = LocalData.isForexExchangeTypeCodeList.get(3);
		twoCode = LocalData.isForexExchangeTypeCodeList.get(4);
		zhuijizhisunCode = "FO";	
	}

	private void init() {
		rateInfoView = LayoutInflater.from(IsForexTradeConfirmActivity.this).inflate(R.layout.isforex_myrate_trade_confirm,
				null);
		tabcontent.addView(rateInfoView);
		backButton = (Button) findViewById(R.id.ib_back);
		accText = (TextView) findViewById(R.id.currency_sellcode_code);
		typeText = (TextView) findViewById(R.id.isForex_myRate_tradeType1);
		codeText = (TextView) findViewById(R.id.isForex_myrate_code);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, codeText);
		jsCodeText = (TextView) findViewById(R.id.isForex_vfgRegCurrency);
		sellTagText = (TextView) findViewById(R.id.isForex_query_sellTag);
		rateText = (TextView) findViewById(R.id.currency_rate);
		limiRateText = (TextView) findViewById(R.id.currency_limitRate);
		jyMoneyText = (TextView) findViewById(R.id.isForex_myRate_tradeMoney);
		sureButton = (Button) findViewById(R.id.sureButton);
		jcTagText = (TextView) findViewById(R.id.isForex_query_jcTag);
		limitRateView = findViewById(R.id.limitRate_view);
		weiTuoRateView = findViewById(R.id.weiTuoRate_view);
		zhiSunRateView = findViewById(R.id.zhiSunRate_view);
		huoLiRateView = findViewById(R.id.huoLiRate_view);
		timesView = findViewById(R.id.times_view);
		weiTuoRateText = (TextView) findViewById(R.id.currency_weiTuoRate);
		zhiSunRateText = (TextView) findViewById(R.id.currency_zhiSunRate);
		huoLiRateText = (TextView) findViewById(R.id.currency_huoLiRate);
		currentRateView = findViewById(R.id.current_rate_layout);
		timesText = (TextView) findViewById(R.id.currency_times);
		
		//wuhan P603 追击止损委托
		zhijizhisun_layout = findViewById(R.id.zhijizhisun_layout);
		forex_zhuijidiancha = (TextView) findViewById(R.id.forex_zhuijidiancha);
		
		//指定平仓中用到的
		lyt_isForex_query_jcTag = (LinearLayout) findViewById(R.id.lyt_isForex_query_jcTag);
		
		
		
		limitRateView.setVisibility(View.VISIBLE);
		weiTuoRateView.setVisibility(View.GONE);
		zhiSunRateView.setVisibility(View.GONE);
		huoLiRateView.setVisibility(View.GONE);
		timesView.setVisibility(View.GONE);
		zhijizhisun_layout.setVisibility(View.GONE);
	}

	private void initDate() {
		String account = (String) BaseDroidApp.getInstanse().getBizDataMap().get(IsForex.ISFOREX_ACCOUNTNUMBER_REQ);
		if (!StringUtil.isNull(account)) {
			account = StringUtil.getForSixForString(account);
		}
		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		exchangeTypeCode = intent.getStringExtra(IsForex.ISFOREX_TRADETYPES_REQ);
		if (StringUtil.isNull(exchangeTypeCode)) {
			return;
		}
		lyt_isForex_query_jcTag.setVisibility(View.VISIBLE);
		
		String type = LocalData.isForexExchangeTypeCodeMap.get(exchangeTypeCode);
		if(zhuijizhisunCode.equals(exchangeTypeCode)){//追击止损
			times1 = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY1);
			times2 = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY2);
			custonerTimes = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY3);
			timesText.setText(custonerTimes);
			diancha = intent.getStringExtra("foSet");
			forex_zhuijidiancha.setText(diancha);
			rate = intent.getStringExtra(ConstantGloble.ISFOREX_RATE_KEY);
			rateText.setText(rate);
			limitRateView.setVisibility(View.GONE);
			weiTuoRateView.setVisibility(View.GONE);
			zhiSunRateView.setVisibility(View.GONE);
			huoLiRateView.setVisibility(View.GONE);
			timesView.setVisibility(View.VISIBLE);
			zhijizhisun_layout.setVisibility(View.VISIBLE);
			
		}else if (xianJiaCode.equals(exchangeTypeCode)) {
			 limitRateView.setVisibility(View.VISIBLE);
			 currentRateView.setVisibility(View.VISIBLE);
			int tag = intent.getIntExtra("pointCang", 0);
			if(tag == 2){
				position = intent.getIntExtra("position", 0);
				queryResultList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
						.get(IsForex.ISFOREX_LIST_REQ);
						Map<String, Object> map = queryResultList.get(position);
				internaNumber = (String) map.get(IsForex.ISFOREX_internalSeq_REQ);
				consignNumber = (String) map.get(IsForex.ISFOREX_CONSIGNNUMBER_REQ);
			}
//			jcTagCode = intent.getStringExtra(ConstantGloble.ISFOREX_JCTAG_KEY);
//			lyt_isForex_query_jcTag.setVisibility(View.VISIBLE);
			limitRate = intent.getStringExtra(ConstantGloble.ISFOREX_LIMITRATE_KEY);
			rate = intent.getStringExtra(ConstantGloble.ISFOREX_RATE_KEY);
			rateText.setText(rate);
			limiRateText.setText(limitRate);
			zhijizhisun_layout.setVisibility(View.GONE);
		}else if(shiJiaCode.equals(exchangeTypeCode)){
			int tag = intent.getIntExtra("pointCang", 0);
			if(tag == 2){
				position = intent.getIntExtra("position", 0);
				queryResultList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
						.get(IsForex.ISFOREX_LIST_REQ);
						Map<String, Object> map = queryResultList.get(position);
				internaNumber = (String) map.get(IsForex.ISFOREX_internalSeq_REQ);
				consignNumber = (String) map.get(IsForex.ISFOREX_CONSIGNNUMBER_REQ);
			}
			zhijizhisun_layout.setVisibility(View.GONE);
		} 
		else if (weiTuoCode.equals(exchangeTypeCode) || zhiSunCode.equals(exchangeTypeCode)) {
			currentRateView.setVisibility(View.GONE);
			limitRateView.setVisibility(View.GONE);
			weiTuoRateView.setVisibility(View.VISIBLE);
			timesView.setVisibility(View.VISIBLE);
			zhijizhisun_layout.setVisibility(View.GONE);
			weiTuoRate = intent.getStringExtra(ConstantGloble.FOREX_WEITUORATE_KEY);
			times1 = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY1);
			times2 = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY2);
			custonerTimes = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY3);
			weiTuoRateText.setText(weiTuoRate);
			timesText.setText(custonerTimes);
		} else if (twoCode.equals(exchangeTypeCode)) {
			currentRateView.setVisibility(View.GONE);
			zhiSunRateView.setVisibility(View.VISIBLE);
			huoLiRateView.setVisibility(View.VISIBLE);
			timesView.setVisibility(View.VISIBLE);
			limitRateView.setVisibility(View.GONE);
			zhijizhisun_layout.setVisibility(View.GONE);
			zhiSunRate = intent.getStringExtra(ConstantGloble.FOREX_ZHISUNRATE_KEY);
			huoLiRate = intent.getStringExtra(ConstantGloble.FOREX_HUOLIRATE_KEY);
			zhiSunRateText.setText(zhiSunRate);
			huoLiRateText.setText(huoLiRate);
			times1 = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY1);
			times2 = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY2);
			custonerTimes = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY3);
			timesText.setText(custonerTimes);
		}
		//指定平仓
		point = intent.getIntExtra("pointCang", 0);
		
		jsCodeCode = intent.getStringExtra(IsForex.ISFOREX_CURRENCYCODES_REQ);
		if (StringUtil.isNull(jsCodeCode)) {
			return;
		}
		String jsCode = LocalData.Currency.get(jsCodeCode);
		sellTageCode = intent.getStringExtra(IsForex.ISFOREX_DIRECTIONS_REQ);
		if (StringUtil.isNull(sellTageCode)) {
			return;
		}
		String sellTag = LocalData.isForexdirectionMap.get(sellTageCode);
		codeCode = intent.getStringExtra(IsForex.ISFOREX_CURRENCYPAIRCODES_REQ);
		if (StringUtil.isNull(codeCode)) {
			return;
		}
		// 货币对名称
		codeCodeName = intent.getStringExtra(ConstantGloble.ISFOREX_CODECODENAME_KEY);
		jcTagCode = intent.getStringExtra(ConstantGloble.ISFOREX_JCTAG_KEY);
		
		if (StringUtil.isNull(jcTagCode)) {
			return;
		}
		money = intent.getStringExtra(ConstantGloble.ISFOREX_AMOUNT_KEY);
		if (StringUtil.isNull(money)) {
			return;
		}
		accText.setText(account);
		typeText.setText(type);
		codeText.setText(codeCodeName);
		String[] sourceCurrence = codeCode.split("/");
		String sourceCurrenceCode = sourceCurrence[0];
		jsCodeText.setText(jsCode);
		sellTagText.setText(sellTag);
		
		String jcTag = jcTagCode;
		if(LocalData.isForexJczhuijizhisunMap.containsKey(jcTag)){
			jcTag = LocalData.isForexJczhuijizhisunMap.get(jcTag);
		}else if(LocalData.isForexJczhuijizhisunMap.containsValue(jcTag)){
			if(jcTag.equals("建仓")){
				jcTagCode = "Y";
			}		else	if(jcTag.equals("先开先平")){
				jcTagCode = "N";
			}	else	if(jcTag.equals("指定平仓")){
				jcTagCode = "C";
			}
		}
		jcTagText.setText(jcTag);
		
		String jyMoney = null;
		// if (LocalData.codeNoNumber.contains(jsCodeCode)) {
		// jyMoney = StringUtil.parseStringPattern(money, twoNumber);
		// } else {
		// jyMoney = StringUtil.parseStringPattern(money, fourNumber);
		// }
		// 保留一位小数
		if (LocalData.goldList.contains(sourceCurrenceCode)) {
			jyMoney = StringUtil.parseStringPattern(money, oneNumber);
		} else {
			// 整数
			jyMoney = StringUtil.parseStringPattern(money, twoNumber);
		}
		jyMoneyText.setText(jyMoney);
	}

	/** 点击时间*/
	private void initClick() {
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
		});
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		if (StringUtil.isNull(commConversationId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		requestPSNGetTokenId(commConversationId);
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		token = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		if (StringUtil.isNull(token)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		//指定平仓
		if(point == 2 && shiJiaCode.equals(exchangeTypeCode)){
			requestPSNVFGZHIDINGTRADE(jsCodeCode, consignNumber, internaNumber, money, exchangeTypeCode, null, codeCode, sellTageCode, token,commConversationId);
		}else if(point == 2 && xianJiaCode.equals(exchangeTypeCode)){
			requestPSNVFGZHIDINGTRADE(jsCodeCode, consignNumber, internaNumber, money, exchangeTypeCode, limitRate, codeCode, sellTageCode, token,commConversationId);
		}else if (xianJiaCode.equals(exchangeTypeCode)) {
			requestPsnVFGTrade(codeCode, jsCodeCode, sellTageCode, jcTagCode, money, exchangeTypeCode, limitRate, null,
					null, null, null, null, null, token);
		} else if (weiTuoCode.equals(exchangeTypeCode)) {
			String times1 = QueryDateUtils.getSeondsTimes(custonerTimes);
			requestPsnVFGTrade(codeCode, jsCodeCode, sellTageCode, jcTagCode, money, exchangeTypeCode, weiTuoRate, null,
					null, null, null, null, times1, token);
		} else if (zhiSunCode.equals(exchangeTypeCode)) {
			String times1 = QueryDateUtils.getSeondsTimes(custonerTimes);
			requestPsnVFGTrade(codeCode, jsCodeCode, sellTageCode, jcTagCode, money, exchangeTypeCode, weiTuoRate, null,
					null, null, null, null, times1, token);
		} else if (twoCode.equals(exchangeTypeCode)) {
			String times1 = QueryDateUtils.getSeondsTimes(custonerTimes);
			requestPsnVFGTrade(codeCode, jsCodeCode, sellTageCode, jcTagCode, money, exchangeTypeCode, huoLiRate,
					zhiSunRate, null, null, null, null, times1, token);
		} else if(zhuijizhisunCode.equals(exchangeTypeCode)){//追击止损委托
			String times1 = QueryDateUtils.getSeondsTimes(custonerTimes);
			requestPsnVFGTrade(codeCode, jsCodeCode, sellTageCode, jcTagCode, money, exchangeTypeCode, diancha,
					null, null, null, null, null, times1, token);
		}
	}

	@Override
	public void requestPSNVFGZHIDINGTRADECallback(Object resultObj) {
		super.requestPSNVFGZHIDINGTRADECallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> map = (Map<String, String>) biiResponseBody.getResult();
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_RESULT_KEY, map);
		
		Intent intent = new Intent(IsForexTradeConfirmActivity.this, IsForexTradeSuccessActivity.class);
		// intent.putExtra(IsForex.ISFOREX_TRANSACTIONID_RES, transactionId);
		intent.putExtra("point", point);
		intent.putExtra(IsForex.ISFOREX_TRADETYPES_REQ, exchangeTypeCode);
		intent.putExtra(IsForex.ISFOREX_CURRENCYPAIRCODES_REQ, codeCode);
		intent.putExtra(IsForex.ISFOREX_CURRENCYCODES_REQ, jsCodeCode);
		intent.putExtra(IsForex.ISFOREX_DIRECTIONS_REQ, sellTageCode);
		intent.putExtra(ConstantGloble.ISFOREX_RATE_KEY, rate);
		intent.putExtra(ConstantGloble.ISFOREX_LIMITRATE_KEY, limitRate);
		intent.putExtra(ConstantGloble.ISFOREX_JCTAG_KEY, jcTagCode);
		intent.putExtra(ConstantGloble.ISFOREX_AMOUNT_KEY, money);
		intent.putExtra(ConstantGloble.ISFOREX_CODECODENAME_KEY, codeCodeName);
		startActivity(intent);
		
	}
	
	@Override
	public void requestPsnVFGTradeCallback(Object resultObj) {
		super.requestPsnVFGTradeCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> result = (Map<String, String>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			return;
		}
		// String transactionId = result.get(IsForex.ISFOREX_TRANSACTIONID_RES);
		// if (StringUtil.isNullOrEmpty(transactionId)) {
		// return;
		// }
		// String txRate = result.get(IsForex.ISFOREX_TXRATE_RES);
		// String txTimrs = result.get(IsForex.ISFOREX_TXTIME_RES);
		// if (StringUtil.isNullOrEmpty(txRate) ||
		// StringUtil.isNullOrEmpty(txTimrs)) {
		// return;
		// }
		Intent intent = new Intent(IsForexTradeConfirmActivity.this, IsForexTradeSuccessActivity.class);
		// intent.putExtra(IsForex.ISFOREX_TRANSACTIONID_RES, transactionId);
		intent.putExtra(IsForex.ISFOREX_TRADETYPES_REQ, exchangeTypeCode);
		intent.putExtra(IsForex.ISFOREX_CURRENCYPAIRCODES_REQ, codeCode);
		intent.putExtra(IsForex.ISFOREX_CURRENCYCODES_REQ, jsCodeCode);
		intent.putExtra(IsForex.ISFOREX_DIRECTIONS_REQ, sellTageCode);
		intent.putExtra(ConstantGloble.ISFOREX_RATE_KEY, rate);
		intent.putExtra(ConstantGloble.ISFOREX_LIMITRATE_KEY, limitRate);
		intent.putExtra(ConstantGloble.ISFOREX_JCTAG_KEY, jcTagCode);
		intent.putExtra(ConstantGloble.ISFOREX_AMOUNT_KEY, money);
		intent.putExtra(ConstantGloble.ISFOREX_CODECODENAME_KEY, codeCodeName);
	
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_RESULT_KEY, result);
		if (xianJiaCode.equals(exchangeTypeCode)) {
		} else if (weiTuoCode.equals(exchangeTypeCode) || zhiSunCode.equals(exchangeTypeCode)) {
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY3, custonerTimes);
			intent.putExtra(ConstantGloble.FOREX_WEITUORATE_KEY, weiTuoRate);
		} else if (twoCode.equals(exchangeTypeCode)) {
			intent.putExtra(ConstantGloble.FOREX_HUOLIRATE_KEY, huoLiRate);
			intent.putExtra(ConstantGloble.FOREX_ZHISUNRATE_KEY, zhiSunRate);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY3, custonerTimes);
		} else if(zhuijizhisunCode.equals(exchangeTypeCode)){
			//追击止损委托wuhan
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY3, custonerTimes);
			intent.putExtra("offSet", diancha);
			
			
		}
		startActivity(intent);
	}
}
