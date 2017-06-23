package com.chinamworld.bocmbci.biz.lsforex.trade;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseNewActivity;
import com.chinamworld.bocmbci.biz.lsforex.rate.IsForexTwoWayDetailActivity;
import com.chinamworld.bocmbci.biz.lsforex.rate.IsForexTwoWayTreasureNewActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.Map;

/** 双向宝交易 快速交易 成功页面 */
public class IsForexTradeSuccessActivity extends IsForexBaseActivity {
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
	/** 建仓标志 */
	private TextView jcTagText = null;
	/** 确定按钮 */
	private Button sureButton = null;
	private TextView numberText = null;
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
	private View limitRateView = null;
	/** 成交时间 */
	private TextView timesText = null;
	/** 委托汇率 */
	private String weiTuoRate = null;
	private View jiChuRateView = null;
	private View youHuiRateView = null;
	/** 委托序号 */
	private TextView weiTuoNumber = null;
	/** 委托序号 view */
	private View weiTuoNumberView = null;
	/** 委托汇率View */
	private View weiTuoRateView = null;
	/** 止损汇率View */
	private View zhiSunRateView = null;
	/** 获利汇率View */
	private View huoLiRateView = null;
	/** 截止时间View */
	private View timesView = null;
	/** 委托时间View */
	private View weiTuoTimesView = null;
	private TextView weiTuoTimesText = null;
	private View cjRateView = null;
	private View cjTiesView = null;
	/** 委托汇率 */
	private TextView weiTuoRateText = null;
	/** 止损汇率 */
	private TextView zhiSunRateText = null;
	/** 获利汇率 */
	private TextView huoLiRateText = null;
	/** 截止时间 */
	private TextView jzTimesText = null;
	private String shiJiaCode = null;
	private String xianJiaCode = null;
	private String weiTuoCode = null;
	private String zhiSunCode = null;
	private String twoCode = null;
	private String custonerTimes = null;
	private TextView topText = null;
	/** 止损汇率 */
	private String zhiSunRate = null;
	/** 获利汇率 */
	private String huoLiRate = null;

	// /////////////新增
	/** 优惠点差 */
	private TextView isForex_discountat = null;
	private LinearLayout myrate_discountat;
	/** 市场汇率 */
	private TextView market = null;
	private LinearLayout ll_currency_marketRate;
	/** 市场汇率 */
	private String marketRate = null;
	/** 优惠/惩罚点差 */
	private String offSet = null;
	/** 优惠/惩罚点差 */
	private String gprType = null;
	
	//wuhan
	private String zhuijizhisunCode = null;
	private View zhijizhisun_layout;
	private TextView forex_zhuijidiancha;
	private String diancha;
	private TextView tv_chenjiao;
	private TextView tv_xianjiarate;
//	private TextView tv_relevance_active_code;
	private LinearLayout lyt_isForex_query_jcTag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.isForex_trade));
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
		rateInfoView = LayoutInflater.from(IsForexTradeSuccessActivity.this).inflate(R.layout.isforex_myrate_trade_success,
				null);
		tabcontent.addView(rateInfoView);
		backButton = (Button) findViewById(R.id.ib_back);
		backButton.setVisibility(View.GONE);
		numberText = (TextView) findViewById(R.id.isForex_myrate_tradeNumber);
		accText = (TextView) findViewById(R.id.currency_sellcode_code);
		typeText = (TextView) findViewById(R.id.isForex_myRate_tradeType1);
		codeText = (TextView) findViewById(R.id.isForex_myrate_code);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, codeText);
		jsCodeText = (TextView) findViewById(R.id.isForex_vfgRegCurrency);
		sellTagText = (TextView) findViewById(R.id.isForex_query_sellTag);
		rateText = (TextView) findViewById(R.id.currency_rate);
		tv_chenjiao = (TextView) findViewById(R.id.tv_chenjiao);
		limiRateText = (TextView) findViewById(R.id.currency_limitRate);
		tv_xianjiarate = (TextView) findViewById(R.id.tv_xianjiarate);
		jyMoneyText = (TextView) findViewById(R.id.isForex_myRate_tradeMoney);
		sureButton = (Button) findViewById(R.id.sureButton);
		limitRateView = findViewById(R.id.limitRate_view);
		jcTagText = (TextView) findViewById(R.id.isForex_query_jcTag);
		timesText = (TextView) findViewById(R.id.isForex_myrate_okTimes);
		jiChuRateView = findViewById(R.id.jiChuRate_layouy);
		youHuiRateView = findViewById(R.id.youHuiRate_layouy);
		weiTuoRateView = findViewById(R.id.weiTuoRate_view);
		zhiSunRateView = findViewById(R.id.zhiSunRate_view);
		huoLiRateView = findViewById(R.id.huoLiRate_view);
		cjRateView = findViewById(R.id.cj_rate_layout);
		cjTiesView = findViewById(R.id.cj_times_layout);
		weiTuoNumber = (TextView) findViewById(R.id.weituo_number);
		weiTuoNumberView = findViewById(R.id.weituo_number_layout);
		weiTuoNumberView.setVisibility(View.GONE);
		timesView = findViewById(R.id.times_view);
		weiTuoTimesView = findViewById(R.id.weituo_times_view);
		weiTuoRateText = (TextView) findViewById(R.id.currency_weiTuoRate);
		zhiSunRateText = (TextView) findViewById(R.id.currency_zhiSunRate);
		huoLiRateText = (TextView) findViewById(R.id.currency_huoLiRate);
		jzTimesText = (TextView) findViewById(R.id.currency_times);
		
		//wuhan P603 追击止损委托
		zhijizhisun_layout = findViewById(R.id.zhijizhisun_layout);
		forex_zhuijidiancha = (TextView) findViewById(R.id.forex_zhuijidiancha);
		
		
		lyt_isForex_query_jcTag = (LinearLayout) findViewById(R.id.lyt_isForex_query_jcTag);
		
		zhijizhisun_layout.setVisibility(View.GONE);
		weiTuoRateView.setVisibility(View.GONE);
		zhiSunRateView.setVisibility(View.GONE);
		huoLiRateView.setVisibility(View.GONE);
		timesView.setVisibility(View.GONE);
		weiTuoTimesView.setVisibility(View.GONE);
		weiTuoTimesText = (TextView) findViewById(R.id.weituo_times);
		topText = (TextView) findViewById(R.id.top_text);

		// TODO
		isForex_discountat = (TextView) findViewById(R.id.isForex_myrate_discountat);
		market = (TextView) findViewById(R.id.currency_marketRate);
		myrate_discountat = (LinearLayout) findViewById(R.id.myrate_discountat);
		ll_currency_marketRate = (LinearLayout) findViewById(R.id.ll_currency_marketRate);
	}

	private void initDate() {
		String account = (String) BaseDroidApp.getInstanse().getBizDataMap().get(IsForex.ISFOREX_ACCOUNTNUMBER_REQ);
		if (!StringUtil.isNull(account)) {
			account = StringUtil.getForSixForString(account);
		}

		Map<String, String> result = (Map<String, String>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.ISFOREX_RESULT_KEY);
		if (StringUtil.isNullOrEmpty(result)) {
			return;
		}
		String transactionId = result.get(IsForex.ISFOREX_TRANSACTIONID_RES);
		String txRate ="";
		String txTimrs ="";
		if(result.containsKey("txRate")){
			txRate = result.get(IsForex.ISFOREX_TXRATE_RES);
		}else if(result.containsKey("TxRate")){
			txRate = result.get("TxRate");
		}
		
		if(result.containsKey("txTime")){
			txTimrs = result.get(IsForex.ISFOREX_TXTIME_RES);
		}else if(result.containsKey("TxTime")){
			txTimrs = result.get("TxTime");
		}
		if(result.containsKey("MacketRate")){
			marketRate = result.get(IsForex.ISFOREX_MACKETRATE_REQ);
		}else if(result.containsKey("marketRate")){
			marketRate = result.get("marketRate");
		}
		
		lyt_isForex_query_jcTag.setVisibility(View.VISIBLE);
		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		exchangeTypeCode = intent.getStringExtra(IsForex.ISFOREX_TRADETYPES_REQ);
		if (StringUtil.isNull(exchangeTypeCode)) {
			return;
		}
		// 委托序号
		String entrustNo ="";
		String entrustTime="";
		
		int point = intent.getIntExtra("point", 0);
		if(point == 2 && (shiJiaCode.equals(exchangeTypeCode)||xianJiaCode.equals(exchangeTypeCode))){
//			lyt_isForex_query_jcTag.setVisibility(View.GONE);
		}else{
			// 委托序号
			entrustNo= result.get(IsForex.ISFOREX_ENTRUSTNO_RES);
			// 委托时间
			entrustTime = result.get(IsForex.ISFOREX_ENTRUSTTIME_RES);
		}
		
		
		
		if (exchangeTypeCode.equals(shiJiaCode) || exchangeTypeCode.equals(xianJiaCode)) {

		} else {
			topText.setText(getResources().getString(R.string.forex_wt_success_top_title));
		}
		String type = LocalData.isForexExchangeTypeCodeMap.get(exchangeTypeCode);
		jsCodeCode = intent.getStringExtra(IsForex.ISFOREX_CURRENCYCODES_REQ);
		String jsCode = null;
		if (!StringUtil.isNull(jsCodeCode)) {
			jsCode = LocalData.Currency.get(jsCodeCode);
		}
		sellTageCode = intent.getStringExtra(IsForex.ISFOREX_DIRECTIONS_REQ);
		String sellTag = null;
		if (!StringUtil.isNull(sellTageCode)) {
			sellTag = LocalData.isForexdirectionMap.get(sellTageCode);
		}
		String codeCode = intent.getStringExtra(IsForex.ISFOREX_CURRENCYPAIRCODES_REQ);
		// 货币对名称
		String codeCodeName = null;
		if (!StringUtil.isNull(codeCode)) {
			codeCodeName = intent.getStringExtra(ConstantGloble.ISFOREX_CODECODENAME_KEY);
		}
		String[] sourceCurrence = codeCode.split("/");
		String sourceCurrenceCode = sourceCurrence[0];
		jcTagCode = intent.getStringExtra(ConstantGloble.ISFOREX_JCTAG_KEY);
		money = intent.getStringExtra(ConstantGloble.ISFOREX_AMOUNT_KEY);
		tv_xianjiarate.setText(getResources().getString(R.string.forex_rate_currency_limitRate));
		tv_chenjiao.setText(getResources().getString(R.string.isForex_myrate_okRate));
		if (exchangeTypeCode.equals(shiJiaCode)) {
			// 市价交易
			cjRateView.setVisibility(View.VISIBLE);
			limitRateView.setVisibility(View.GONE);
			if (StringUtil.isNullOrEmpty(txTimrs)) {
				timesText.setText("-");
			} else {
				timesText.setText(txTimrs);
			}
			if (StringUtil.isNullOrEmpty(txRate)) {
				rateText.setText("-");
			} else {
				rateText.setText(txRate);
			}
		} else if (xianJiaCode.equals(exchangeTypeCode)) {
			// 限价交易
			limitRate = intent.getStringExtra(ConstantGloble.ISFOREX_LIMITRATE_KEY);
			limiRateText.setText(limitRate);
			tv_xianjiarate.setText("客户指定限价汇率:");
			limitRateView.setVisibility(View.VISIBLE);
			cjRateView.setVisibility(View.VISIBLE);
//			lyt_isForex_query_jcTag.setVisibility(View.VISIBLE);
			if (StringUtil.isNullOrEmpty(txTimrs)) {
				timesText.setText("-");
			} else {
				timesText.setText(txTimrs);
			}
			tv_chenjiao.setText("实际成交汇率:");
			if (StringUtil.isNullOrEmpty(txRate)) {
				rateText.setText("-");
			} else {
				rateText.setText(txRate);
			}
		} else if (weiTuoCode.equals(exchangeTypeCode) || zhiSunCode.equals(exchangeTypeCode)) {
			weiTuoNumberView.setVisibility(View.VISIBLE);
			weiTuoRateView.setVisibility(View.VISIBLE);
			zhiSunRateView.setVisibility(View.GONE);
			huoLiRateView.setVisibility(View.GONE);
			timesView.setVisibility(View.VISIBLE);
			weiTuoTimesView.setVisibility(View.VISIBLE);
			cjRateView.setVisibility(View.GONE);
			cjTiesView.setVisibility(View.GONE);
			limitRateView.setVisibility(View.GONE);
			zhijizhisun_layout.setVisibility(View.GONE);
			weiTuoNumber.setText(entrustNo);
			weiTuoTimesText.setText(entrustTime);
			weiTuoRate = intent.getStringExtra(ConstantGloble.FOREX_WEITUORATE_KEY);
			weiTuoRateText.setText(weiTuoRate);
			custonerTimes = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY3);
			jzTimesText.setText(custonerTimes);
		} else if (twoCode.equals(exchangeTypeCode)) {
			weiTuoNumberView.setVisibility(View.VISIBLE);
			weiTuoRateView.setVisibility(View.GONE);
			zhiSunRateView.setVisibility(View.VISIBLE);
			huoLiRateView.setVisibility(View.VISIBLE);
			timesView.setVisibility(View.VISIBLE);
			weiTuoTimesView.setVisibility(View.VISIBLE);
			cjRateView.setVisibility(View.GONE);
			cjTiesView.setVisibility(View.GONE);
			limitRateView.setVisibility(View.GONE);
			zhijizhisun_layout.setVisibility(View.GONE);
			weiTuoNumber.setText(entrustNo);
			weiTuoTimesText.setText(entrustTime);
			custonerTimes = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY3);
			jzTimesText.setText(custonerTimes);
			zhiSunRate = intent.getStringExtra(ConstantGloble.FOREX_ZHISUNRATE_KEY);
			huoLiRate = intent.getStringExtra(ConstantGloble.FOREX_HUOLIRATE_KEY);
			zhiSunRateText.setText(zhiSunRate);
			huoLiRateText.setText(huoLiRate);
		}else if(zhuijizhisunCode.equals(exchangeTypeCode)){
			//追击止损委托 p603
			weiTuoNumberView.setVisibility(View.VISIBLE);
			weiTuoRateView.setVisibility(View.GONE);
			zhiSunRateView.setVisibility(View.GONE);
			huoLiRateView.setVisibility(View.GONE);
			timesView.setVisibility(View.VISIBLE);
			weiTuoTimesView.setVisibility(View.VISIBLE);
			cjRateView.setVisibility(View.GONE);
			cjTiesView.setVisibility(View.GONE);
			limitRateView.setVisibility(View.GONE);
			zhijizhisun_layout.setVisibility(View.VISIBLE);
			weiTuoNumber.setText(entrustNo);
			weiTuoTimesText.setText(entrustTime);
			custonerTimes = intent.getStringExtra(ConstantGloble.FOREX_TIMES_KEY3);
			jzTimesText.setText(custonerTimes);
			diancha = intent.getStringExtra("offSet");
			forex_zhuijidiancha.setText(diancha);
			
		}
		numberText.setText(transactionId);
		accText.setText(account);
		typeText.setText(type);
		codeText.setText(codeCodeName);
		jsCodeText.setText(jsCode);
		sellTagText.setText(sellTag);

		// TODO gprType 优惠/惩罚类型 P – 优惠R – 惩罚
	
		gprType = result.get(IsForex.ISFOREX_GPRTYPE_REQ);
		offSet = result.get(IsForex.ISFOREX_OFFSET_REQ);

		if (("MI").equals(exchangeTypeCode)) {
			if (!StringUtil.isNull(marketRate)) {
				ll_currency_marketRate.setVisibility(View.VISIBLE);
				market.setText(marketRate);
			}else{
				market.setText("-");
			}
		} else {
			market.setText("-");
			ll_currency_marketRate.setVisibility(View.GONE);
////			wuhan
//			if (!StringUtil.isNull(marketRate)) {
//				ll_currency_marketRate.setVisibility(View.VISIBLE);
//				market.setText(marketRate);
//			}else{
//				market.setText("-");
//			}
		}
		
		// TODO gprType 优惠/惩罚类型 P – 优惠R – 惩罚
		if ("MI".equals(exchangeTypeCode) && !StringUtil.isNull(offSet)) {
			if ( "P".equals(gprType) ||  "R".equals(gprType)) {
				myrate_discountat.setVisibility(View.VISIBLE);
				if (StringUtil.isDigit(offSet) || StringUtil.behindTheDecimalPointIsWhole0(offSet)) {
					isForex_discountat.setText(offSet + "bp");
				} else {
					isForex_discountat.setText(offSet);
				}
			}
		} else {
			myrate_discountat.setVisibility(View.GONE);
		}
		if(LocalData.isForexJczhuijizhisunMap.containsKey(jcTagCode)){
			jcTagCode = LocalData.isForexJczhuijizhisunMap.get(jcTagCode);
		}
		jcTagText.setText(jcTagCode);
		String jyMoney = null;
		// if (!StringUtil.isNull(money)) {
		// jyMoney = StringUtil.parseStringCodePattern(jsCodeCode, money,
		// fourNumber);
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

	private void initClick() {
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(IsForexTradeSuccessActivity.this, IsForexMyRateInfoActivity.class);
//				startActivityForResult(intent, 0);
				//行情页面
				if(IsForexBaseNewActivity.isFlagGoWay == 0){
					IsForexBaseNewActivity.isFlagGoWay = -1;
					ActivityTaskManager.getInstance().removeAllSecondActivity();
					Intent intent = new Intent(IsForexTradeSuccessActivity.this, IsForexTwoWayTreasureNewActivity.class);
					startActivity(intent);
				}else if(IsForexBaseNewActivity.isFlagGoWay == 1){
					IsForexBaseNewActivity.isFlagGoWay = -1;
					ActivityTaskManager.getInstance().removeAllSecondActivity();
					Intent intent = new Intent(IsForexTradeSuccessActivity.this, IsForexTwoWayDetailActivity.class);
					String sourceCode = (String)BaseDroidApp.getInstance().getBizDataMap().get("sourceCode");
					String targetCode = (String)BaseDroidApp.getInstance().getBizDataMap().get("targetCode");
					intent.putExtra("sourceCode", sourceCode);
					intent.putExtra("targetCode", targetCode);
					intent.putExtra("isQrcode", true);
					startActivity(intent);
				}else{
					ActivityTaskManager.getInstance().removeAllSecondActivity();
					goToMainActivity();
				}
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
