package com.chinamworld.bocmbci.biz.lsforex.query;

import java.util.HashMap;
import java.util.List;
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
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.biz.lsforex.IsforexDataCenter;
import com.chinamworld.bocmbci.biz.lsforex.trade.IsForexTradeSubmitActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 未平仓交易状况 详情页面 */
public class IsForexWpcQueryDetailActivity extends IsForexBaseActivity {
	public static final String TAG = "IsForexWpcQueryDetailActivity";
	/** activity View */
	private View queryView = null;
	/**
	 * backButton:返回按钮
	 */
	private Button backButton = null;
	private TextView wtNumber = null;
	private TextView jsCode = null;
	private TextView codeText = null;
	private TextView sellTag = null;
	private TextView jcTag = null;
	private TextView jyMoney = null;
	private TextView wtRate = null;
	private TextView jyQudao = null;
	/** 平仓按钮 */
	private Button sureButton = null;
	/** 货币对 */
	private String code = null;
	/** 结算币种 */
	private String settleCurrecny = null;
	/** 买卖方向名称 */
	private String direction = null;
	/** 建仓标志 */
	private String openPositionFlag = null;
	/** 货币对在货币对集合里的位置 */
	private boolean codePosition = false;
	/** 结算币种在结算币种集合了的位置 */
	private boolean settleCurrecnyPosition = false;
	/** 委托类型 */
	private String exchangeTranType = null;
	/** 成交时间 */
	private TextView tradeTimesText = null;
	/** 成交类型 */
	private TextView tradeTypeText = null;
	/** 成交汇率 */
	private TextView tradeRateText = null;
	/** 成交金额 */
	private TextView tradeMoneyText = null;
	/** 未平仓金额 */
	private String unClosedBalance = null;
	/** 结算币种 */
	private String settle = null;
	//指定平仓
	private int position = 0;
	private String tradeBackground ="";
	private String vfgTransactionId ="";//交易序号--> 原开仓编号
	private String vfgType = "";
	String consignNumber  = "";
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
		queryView = LayoutInflater.from(IsForexWpcQueryDetailActivity.this).inflate(R.layout.isforex_query_wpc_detail,
				null);
		tabcontent.addView(queryView);
		/** 为界面标题赋值 */
		setTitle(getResources().getString(R.string.query_title_three));
		backButton = (Button) findViewById(R.id.ib_back);
		sureButton = (Button) findViewById(R.id.forex_query_deal_detailes_ok);
		wtNumber = (TextView) findViewById(R.id.forex_trade_number);
		jsCode = (TextView) findViewById(R.id.forex_trade_sell);
		codeText = (TextView) findViewById(R.id.forex_trade_sell_code);
		sellTag = (TextView) findViewById(R.id.forex_trade_buy);
		jcTag = (TextView) findViewById(R.id.forex_trade_code);
		jyMoney = (TextView) findViewById(R.id.forex_trade_type);
		wtRate = (TextView) findViewById(R.id.forex_trade_times);
		jyQudao = (TextView) findViewById(R.id.forex_trade_wtType);
		tradeTimesText = (TextView) findViewById(R.id.isForex_myrate_okTimes);
		tradeTypeText = (TextView) findViewById(R.id.forex_trade_type1);
		tradeRateText = (TextView) findViewById(R.id.isForex_myrate_okRate);
		tradeMoneyText = (TextView) findViewById(R.id.isForex_wpc_money);
	}
	Map<String, Object> lradeDetailMap = new HashMap<String, Object>();
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

		if (!StringUtil.isNull(settleCurrecny) && LocalData.Currency.containsKey(settleCurrecny)) {
			settle = LocalData.Currency.get(settleCurrecny);
		}
		position = intent.getIntExtra(ConstantGloble.ISFOREX_SELECTEDPOSITION, -1);
		if (position == -1) {
			return;
		}
		Map<String, Object> map = queryResultList.get(position);
		
		
		consignNumber = (String) map.get(IsForex.ISFOREX_CONSIGNNUMBER_REQ);
		Map<String, String> currency1 = (Map<String, String>) map.get(IsForex.ISFOREX_CURRENCY1_REQ);
		Map<String, String> currency2 = (Map<String, String>) map.get(IsForex.ISFOREX_CURRENCY2_REQ);
		code = null;
		if (!StringUtil.isNullOrEmpty(currency1) && !StringUtil.isNullOrEmpty(currency2)) {
			String code1 = currency1.get(IsForex.ISFOREX_CODE_REQ);
			String code2 = currency2.get(IsForex.ISFOREX_CODE_REQ);
			if (!StringUtil.isNull(code1) && !StringUtil.isNull(code2)) {
				String codes1 = LocalData.Currency.get(code1);
				String codes2 = LocalData.Currency.get(code2);
				code = codes1 + "/" + codes2;
				// codeCode = code1 + "/" + code2;
			}
		}
		direction = (String) map.get(IsForex.ISFOREX_DIRECTION_REQ);
		if (!StringUtil.isNull(direction)) {
			direction = LocalData.isForexdirectionMap.get(direction);
		}
		openPositionFlag = (String) map.get(IsForex.ISFOREX_OPENPOSITIONFLAG_REQ);
//		String tradeBackground = (String) map.get("tradeBackground");
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
		String type = null;
		if (!StringUtil.isNull(exchangeTranType) && LocalData.isForexExchangeTypeCodeMap.containsKey(exchangeTranType)) {
			type = LocalData.isForexExchangeTypeCodeMap.get(exchangeTranType);
		} else {
			type = "-";
		}
		String channelType = (String) map.get(IsForex.ISFOREX_CHANNELTYPE_REQ);
		if (!StringUtil.isNull(channelType)) {
			channelType = LocalData.isForexchannelTypeMap.get(channelType);
		}
		// 成交时间
		String exchangeTransDate = (String) map.get(IsForex.ISFOREX_EXCHANGETRANSDATE_REQ);
		// 成交汇率
		String exchangeRate = (String) map.get(IsForex.ISFOREX_EXCHANGERATE_REQ);
		// 未平仓金额
		unClosedBalance = (String) map.get(IsForex.ISFOREX_UNCLOSEDBALANCE_REQ);
		LogGloble.d(TAG, unClosedBalance + "-----");
		String amount = (String) map.get(IsForex.ISFOREX_AMOUNT_REQ);
		String unMoney = null;
		if (!StringUtil.isNull(unClosedBalance)) {
			unMoney = StringUtil.parseStringCodePattern(settleCurrecny, unClosedBalance, fourNumber);
		}
		// 使用第一成交类型
		String tradeType = null;
		String firstType = (String) map.get(IsForex.ISFOREX_FIRSTTYPE_REQ);
		if (!StringUtil.isNull(firstType) && LocalData.isForexFirstTypeMap.containsKey(firstType)) {
			tradeType = LocalData.isForexFirstTypeMap.get(firstType);
		}
		wtNumber.setText(consignNumber);
		jsCode.setText(settle);
		codeText.setText(code);
		sellTag.setText(direction);
//		jcTag.setText(openPositionFlag);
		//P603
		lradeDetailMap = IsforexDataCenter.getInstance().getLradeDetailMap();
		if(lradeDetailMap!=null && lradeDetailMap.containsKey("tradeBackground")){
			tradeBackground = (String) lradeDetailMap.get("tradeBackground");
			//p603
			tradeBackground = LocalData.mapTradeBack.get(tradeBackground);
			jcTag.setText(tradeBackground);
			vfgTransactionId = (String) lradeDetailMap.get("vfgTransactionId");
		}
		queryTag = intent.getIntExtra("queryTag", 1);
		
	
		
		if (!StringUtil.isNull(amount) && !StringUtil.isNull(settleCurrecny)) {
			if (LocalData.codeNoNumber.contains(settleCurrecny)) {
				amount = StringUtil.parseStringPattern(amount, twoNumber);
			} else {
				amount = StringUtil.parseStringPattern(amount, fourNumber);
			}
		}
		jyMoney.setText(amount);
		wtRate.setText(type);
		jyQudao.setText(channelType);
		tradeTimesText.setText(exchangeTransDate);
		tradeTypeText.setText(tradeType);
		tradeRateText.setText(exchangeRate);
		tradeMoneyText.setText(unMoney);

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
				
				// 查询货币对   
				BaseHttpEngine.showProgressDialog();
				requestPsnVFGGetAllRate("");
				
//				String msg1 = getResources().getString(R.string.isForex_emg_pc_msg1);
//				String msg2 = code;
//				String msg3 = getResources().getString(R.string.isForex_emg_pc_msg2);
//				BaseDroidApp.getInstanse().showErrorDialog(msg1 + msg2 + msg3, R.string.cancle, R.string.confirm,
//						new OnClickListener() {
//
//							@Override
//							public void onClick(View v) {
//								BaseDroidApp.getInstanse().dismissErrorDialog();
//								switch (v.getId()) {
//								case R.id.exit_btn:// 取消
//
//									break;
//								case R.id.retry_btn:// 确定
//									// 查询货币对   
//									BaseHttpEngine.showProgressDialog();
//									requestPsnVFGGetAllRate("");
//									break;
//								default:
//									break;
//								}
//							}
//						});

			}
		});
	}

	// 查询货币对---回调
	@Override
	public void requestPsnVFGGetAllRateCallback(Object resultObj) {
		super.requestPsnVFGGetAllRateCallback(resultObj);
		if (codeResultList == null || codeResultList.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					IsForexWpcQueryDetailActivity.this.getResources().getString(R.string.isForex_trade_code));
			return;
		}
		boolean i = isIncodeResultList();
		if (!i) {
			BaseHttpEngine.dissMissProgressDialog();
			// 货币对
			String msg1 = getResources().getString(R.string.isForex_trade_emg_code1);
			String msg2 = code;
			String msg3 = getResources().getString(R.string.isForex_trade_emg_code2);
			BaseDroidApp.getInstanse().showInfoMessageDialog(msg1 + msg2 + msg3);
			return;
		} else {
			// 查询结算币种
			requestPsnVFGGetRegCurrency(vfgType);
		}
	}

	/** 货币对是否在货币对集合里面 */
	private boolean isIncodeResultList() {
		int len = codeResultList.size();
		for (int i = 0; i < len; i++) {
			Map<String, Object> map = codeResultList.get(i);
			if (StringUtil.isNullOrEmpty(map)) {
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						IsForexWpcQueryDetailActivity.this.getResources().getString(R.string.isForex_trade_code));
				return false;
			}
			String sourceCurrencyCode = (String) map.get(IsForex.ISFOREX_SOURCECURRENCYCODE_RES);
			String targetCurrencyCode = (String) map.get(IsForex.ISFOREX_TARGETCURRENCYCODE_RES);
			if (!StringUtil.isNull(sourceCurrencyCode) && LocalData.Currency.containsKey(sourceCurrencyCode)
					&& !StringUtil.isNull(targetCurrencyCode) && LocalData.Currency.containsKey(targetCurrencyCode)) {
				String code1 = LocalData.Currency.get(sourceCurrencyCode);
				String code2 = LocalData.Currency.get(targetCurrencyCode);
				String coses = code1 + "/" + code2;
				if (coses.equals(code)) {
					codePosition = true;
					break;
				}
			}
		}
		return codePosition;
	}

	// 查询结算币种----回调
	@Override
	public void requestPsnVFGGetRegCurrencyCallback(Object resultObj) {
		super.requestPsnVFGGetRegCurrencyCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (vfgRegCurrencyList.size() <= 0 || vfgRegCurrencyList == null) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					IsForexWpcQueryDetailActivity.this.getResources().getString(R.string.isForex_trade_jcCode));
			return;
		}
		boolean i = isInVfgRegCurrencyList();
		if (!i) {
			// 结算币种
			String msg1 = getResources().getString(R.string.isForex_trade_emg_code1);
			String msg2 = settleCurrecny;
			String msg3 = getResources().getString(R.string.isForex_trade_emg_code3);
			BaseDroidApp.getInstanse().showInfoMessageDialog(msg1 + msg2 + msg3);
			return;
		} else {
			Intent intent = new Intent(IsForexWpcQueryDetailActivity.this, IsForexTradeSubmitActivity.class);
			intent.putExtra(ConstantGloble.ISFOREX_CODECODENAME_KEY, code);
			intent.putExtra(ConstantGloble.ISFOREX_SETTLECURRECNY_KEY, settle);
			String buy = LocalData.isForexSellTagList.get(0);
			String sell = LocalData.isForexSellTagList.get(1);
			String dir = null;
			if (buy.equals(direction)) {
				dir = sell;
			} else if (sell.equals(direction)) {
				dir = buy;
			}
			
			intent.putExtra(ConstantGloble.ISFOREX_DIRECTION_KEY, dir);// 买卖方向
			intent.putExtra(IsForex.ISFOREX_UNCLOSEDBALANCE_REQ, unClosedBalance);// 未平仓金额
			intent.putExtra(IsForex.ISFOREX_OPENPOSITIONFLAG_REQ, openPositionFlag);
			intent.putExtra(ConstantGloble.ISFOREX_REQUEXTTRADE_KEY, ConstantGloble.ISFOREX_WPC_TRADE_ACTIVITY);// 201
			intent.putExtra(IsForex.ISFOREX_EXCHANGETRANTYPE_REQ, exchangeTranType);// 委托类型代码
			
			//wuhan
			intent.putExtra("queryTag", queryTag);//到交易页面，保存交易方式和建仓标识可编辑   
			intent.putExtra("tradeBackground",tradeBackground);
			//指定平仓
			intent.putExtra("position",position);
			
			//p603原开仓编号
			intent.putExtra("jcCode", consignNumber);//consignNumber
			startActivity(intent);
		}
	}

	/** 结算币种是否在结算币种集合里面 */
	private boolean isInVfgRegCurrencyList() {
		int len = vfgRegCurrencyList.size();
		for (int i = 0; i < len; i++) {
			String codes = vfgRegCurrencyList.get(i);
			if (!StringUtil.isNull(codes) && LocalData.Currency.containsKey(codes)) {
				String code = LocalData.Currency.get(codes);
				if (code.equals(settle)) {
					settleCurrecnyPosition = true;
					break;
				}
			}
		}
		return settleCurrecnyPosition;
	}
}
