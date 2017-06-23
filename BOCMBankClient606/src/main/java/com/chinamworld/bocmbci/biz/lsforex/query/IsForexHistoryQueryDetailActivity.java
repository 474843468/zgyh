package com.chinamworld.bocmbci.biz.lsforex.query;

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
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.biz.lsforex.IsforexDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 成交状况查询 详情页面 */
public class IsForexHistoryQueryDetailActivity extends IsForexBaseActivity {
	public static final String TAG = "IsForexHistoryQueryDetailActivity";
	/** activity View */
	private View queryView = null;
	/** backButton:返回按钮 */
	private Button backButton = null;

	private TextView wtNumber = null;
	private TextView jsCode = null;
	private TextView codeText = null;
	private TextView sellTag = null;
	private TextView jcTag = null;
	private TextView jyMoney = null;
	/** 委托汇率 */
	private TextView wtRate = null;
	private TextView wtType = null;
	private TextView jyStatus = null;
	private TextView jyQudao = null;
	private TextView wtTimes = null;
	private TextView sxTimes = null;
	private Button sureButton = null;
	private TextView cjTypeText = null;
	/** 成交汇率 */
	private TextView forex_txRate = null;
	/** 优惠点差 */
	private TextView isForex_discountat = null;
	private LinearLayout myrate_discountat;
	private LinearLayout ll_currency_marketRate;
	// /////////////////////////////
	/** 市场汇率 */
	private String txRate = null;
	/** 优惠/惩罚点差 */
	private String offSet = null;
	/** 优惠/惩罚点差 */
	private String gprType = null;
	/** 交易类型 */
	private String exchangeTranType = null;
	private String tranType = null;
	private String tradeBackground = null;
	private LinearLayout zhijizhisun_layout;
	private TextView foSet;
	private TextView tv_weTuoRate ; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		init();
		initData();
		initClick();
	}

	/** 初始化界面 */
	private void init() {
		queryView = LayoutInflater.from(IsForexHistoryQueryDetailActivity.this).inflate(
				R.layout.isforex_query_history_detail, null);
		tabcontent.addView(queryView);
		/** 为界面标题赋值 */
		setTitle(getResources().getString(R.string.forex_trade));
		backButton = (Button) findViewById(R.id.ib_back);
		sureButton = (Button) findViewById(R.id.forex_query_deal_detailes_ok);
		wtNumber = (TextView) findViewById(R.id.forex_trade_number);
		jsCode = (TextView) findViewById(R.id.forex_trade_sell);
		codeText = (TextView) findViewById(R.id.forex_trade_sell_code);
		sellTag = (TextView) findViewById(R.id.forex_trade_buy);
		jcTag = (TextView) findViewById(R.id.forex_trade_code);
		jyMoney = (TextView) findViewById(R.id.forex_trade_type);
		wtRate = (TextView) findViewById(R.id.forex_trade_times);
		tv_weTuoRate = (TextView)findViewById(R.id.tv_weTuoRate);
		wtType = (TextView) findViewById(R.id.forex_trade_wtType);
		jyStatus = (TextView) findViewById(R.id.forex_trade_jtStatus);
		jyQudao = (TextView) findViewById(R.id.forex_trade_jyQudao);
		wtTimes = (TextView) findViewById(R.id.forex_trade_wtTimes);
		sxTimes = (TextView) findViewById(R.id.forex_trade_sxTimes);
		cjTypeText = (TextView) findViewById(R.id.forex_trade_type1);
		forex_txRate = (TextView) findViewById(R.id.forex_Deal_txRate);
		isForex_discountat = (TextView) findViewById(R.id.isForex_myrate_discountat);
		myrate_discountat = (LinearLayout) findViewById(R.id.myrate_discountat);
		ll_currency_marketRate = (LinearLayout) findViewById(R.id.ll_currency_marketRate);
		ll_currency_marketRate.setVisibility(View.VISIBLE);
		zhijizhisun_layout = (LinearLayout) findViewById(R.id.zhijizhisun_layout);
		foSet = (TextView) findViewById(R.id.forex_zhuijidiancha);
		zhijizhisun_layout.setVisibility(View.GONE);
	}

	private void initData() {
		queryResultList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(IsForex.ISFOREX_LIST_REQ);
		if (queryResultList.size() <= 0 || queryResultList == null) {
			return;
		}
		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		String settleCurrecny = intent.getStringExtra(IsForex.ISFOREX_SETTLECURRENCY_REQ);
		String settle = null;
		if (!StringUtil.isNull(settleCurrecny) && LocalData.Currency.containsKey(settleCurrecny)) {
			settle = LocalData.Currency.get(settleCurrecny);
		}
		int position = intent.getIntExtra(ConstantGloble.ISFOREX_SELECTEDPOSITION, -1);
		if (position == -1) {
			return;
		}

		Map<String, Object> map = queryResultList.get(position);
		String consignNumber = (String) map.get(IsForex.ISFOREX_CONSIGNNUMBER_REQ);

		Map<String, String> currency1 = (Map<String, String>) map.get(IsForex.ISFOREX_CURRENCY1_REQ);
		Map<String, String> currency2 = (Map<String, String>) map.get(IsForex.ISFOREX_CURRENCY2_REQ);
		String code = null;
		if (!StringUtil.isNullOrEmpty(currency1) && !StringUtil.isNullOrEmpty(currency2)) {
			String code1 = currency1.get(IsForex.ISFOREX_CODE_REQ);
			String code2 = currency2.get(IsForex.ISFOREX_CODE_REQ);
			if (!StringUtil.isNull(code1) && !StringUtil.isNull(code2)) {
				String codes1 = LocalData.Currency.get(code1);
				String codes2 = LocalData.Currency.get(code2);
				code = codes1 + "/" + codes2;
			}
		}
		String direction = (String) map.get(IsForex.ISFOREX_DIRECTION_REQ);
		if (!StringUtil.isNull(direction)) {
			direction = LocalData.isForexdirectionMap.get(direction);
		}
		String openPositionFlag = (String) map.get(IsForex.ISFOREX_OPENPOSITIONFLAG_REQ);
		
	
		String amount = (String) map.get(IsForex.ISFOREX_AMOUNT_REQ);
		String customerRate = null;
		String firstCustomerRate = (String) map.get(IsForex.ISFOREX_FIRSTRATE_REQ);
		String secondCustomerRate = (String) map.get(IsForex.ISFOREX_SECONDRATE_REQ);
		String thirdCustomerRate = (String) map.get(IsForex.ISFOREX_THIRDRATE_REQ);
		if (!StringUtil.isNull(firstCustomerRate)) {
			customerRate = firstCustomerRate;
		} else {
			if (!StringUtil.isNull(secondCustomerRate)) {
				customerRate = secondCustomerRate;
			} else {
				if (!StringUtil.isNull(thirdCustomerRate)) {
					customerRate = thirdCustomerRate;
				}
			}
		}

		exchangeTranType = (String) map.get(IsForex.ISFOREX_EXCHANGETRANTYPE_REQ);
		if (!StringUtil.isNull(exchangeTranType)) {
			tranType = LocalData.isForexExchangeTranType.get(exchangeTranType);
		}

		String orderStatus = (String) map.get(IsForex.ISFOREX_ORDERSTATUS_REQ);
		if (!StringUtil.isNull(orderStatus)) {
			orderStatus = LocalData.isForexOrderStatusMap.get(orderStatus);
		}

		String channelType = (String) map.get(IsForex.ISFOREX_CHANNELTYPE_REQ);
		if (!StringUtil.isNull(channelType)) {
			channelType = LocalData.isForexchannelTypeMap.get(channelType);
		}

		String paymentDate = (String) map.get(IsForex.ISFOREX_PAYMENTDATE_REQ);
		String dueDate = (String) map.get(IsForex.ISFOREX_DUEDATE_REQ);
		// 使用第一成交类型
		String tradeType = null;
		String firstType = (String) map.get(IsForex.ISFOREX_FIRSTTYPE_REQ);
		if (!StringUtil.isNull(firstType) && LocalData.isForexFirstTypeMap.containsKey(firstType)) {
			tradeType = LocalData.isForexFirstTypeMap.get(firstType);
		} else {
			tradeType = "-";
		}
		wtNumber.setText(consignNumber);
		if (StringUtil.isNull(settle)) {
			jsCode.setText("-");
		} else {
			jsCode.setText(settle);
		}
		
	
		codeText.setText(code);
		sellTag.setText(direction);
//		jcTag.setText(openPositionFlag);
		
		if (!StringUtil.isNull(amount) && !StringUtil.isNull(settleCurrecny)) {
			if (LocalData.codeNoNumber.contains(settleCurrecny)) {
				amount = StringUtil.parseStringPattern(amount, twoNumber);
			} else {
				amount = StringUtil.parseStringPattern(amount, fourNumber);
			}
		}
		jyMoney.setText(amount);
		if("MI".equals(exchangeTranType)){
			tv_weTuoRate.setText("市场汇率：");
		}else{
			tv_weTuoRate.setText("委托汇率：");
		}
		wtRate.setText(firstCustomerRate);
		wtType.setText(tranType);
		jyStatus.setText(orderStatus);
		jyQudao.setText(channelType);
		wtTimes.setText(paymentDate);
		if (StringUtil.isNull(dueDate)) {
			sxTimes.setText("-");
		} else {
			sxTimes.setText(dueDate);
		}
		cjTypeText.setText(tradeType);
		// ///////////新加字段
		Map<String, Object> lradeDetailMap = IsforexDataCenter.getInstance().getLradeDetailMap();
		// TODO gprType 优惠/惩罚类型 P – 优惠R – 惩罚
		txRate = (String) lradeDetailMap.get(IsForex.ISFOREX_txRate_REQ);
		gprType = (String) lradeDetailMap.get(IsForex.ISFOREX_gprType_REQ);
		offSet = (String) lradeDetailMap.get(IsForex.ISFOREX_offSet_REQ);
		tradeBackground = (String) lradeDetailMap.get("tradeBackground");
		tradeBackground = LocalData.mapTradeBack.get(tradeBackground);
		jcTag.setText(tradeBackground);
		
//		if(exchangeTranType.equals("FO")){
//			zhijizhisun_layout.setVisibility(View.VISIBLE);
//			foSet.setText(lradeDetailMap.get("foSet").toString());
//		}else {
//			zhijizhisun_layout.setVisibility(View.GONE);
//		}
		
		
		
		// 成交
		if (!StringUtil.isNull(txRate)) {
			String txRates = StringUtil.parseStringPattern(txRate, fourNumber);
			forex_txRate.setText(txRates);
		} else {
			forex_txRate.setText("-");
		}
		
		// TODO gprType 优惠/惩罚类型 P – 优惠R – 惩罚
		if ("MI".equals(exchangeTranType) && !StringUtil.isNull(offSet)) {
			if ("P".equals(gprType) || "R".equals(gprType)) {
				myrate_discountat.setVisibility(View.VISIBLE);
				if (StringUtil.isDigit(offSet) || StringUtil.behindTheDecimalPointIsWhole0(offSet)) {
					isForex_discountat.setText(offSet + "bp");
				} else {
					isForex_discountat.setText(offSet);
				}
			}
		} else if ("MI".equals(exchangeTranType) && StringUtil.isNull(offSet)) {
			// 如果是市价即时 优惠点差显示 红色-
			myrate_discountat.setVisibility(View.VISIBLE);
			isForex_discountat.setTextColor(this.getResources().getColor(R.color.red));
			isForex_discountat.setText(IsForex.ISFOREX_isNull_Show);
		} else {
			myrate_discountat.setVisibility(View.GONE);
		}
	}

	private void initClick() {
		// 返回
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
