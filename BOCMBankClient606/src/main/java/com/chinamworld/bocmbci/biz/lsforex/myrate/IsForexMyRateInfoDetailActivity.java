package com.chinamworld.bocmbci.biz.lsforex.myrate;

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
import com.chinamworld.bocmbci.biz.lsforex.trade.IsForexTradeSubmitActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 我的外汇双向宝详情页面 */
public class IsForexMyRateInfoDetailActivity extends IsForexBaseActivity {
	public static final String TAG = "IsForexMyRateInfoDetailActivity";
	/**
	 * IsForexMyRateInfoActivity的主布局
	 */
	private View rateInfoView = null;
	/** 返回按钮 */
	private Button backButton = null;
	/** 此项不在显示 */
	private TextView accNumberText = null;
	private TextView jsCodeText = null;
	private TextView codeText = null;
	private TextView sellTagText = null;
	private TextView priceText = null;
	private TextView ccMoneyText = null;
	/** 平仓按钮 */
	private Button tradeButton = null;
	private View buttonView = null;
	/** 货币对 */
	private String code = null;
	// 结算币种名称
	String settleCode = null;
	/** 货币对在货币对集合里的位置 */
	private boolean codePosition = false;
	/** 结算币种在结算币种集合了的位置 */
	private boolean settleCurrecnyPosition = false;
	/** 买卖方向 */
	private String direction = null;
	/** 持仓余额 */
	private String balance = null;
	private String vfgType = "";
//	我的双向宝平仓wuhan
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.isForex_myRate_title));
		init();
		initDate();
		initOnClick();
	}

	private void init() {
		rateInfoView = LayoutInflater.from(IsForexMyRateInfoDetailActivity.this).inflate(
				R.layout.isforex_myrate_detail, null);
		tabcontent.addView(rateInfoView);
		backButton = (Button) findViewById(R.id.ib_back);
		accNumberText = (TextView) findViewById(R.id.forex_rate_fix_accNumber);
		jsCodeText = (TextView) findViewById(R.id.forex_rate_fix_number);
		codeText = (TextView) findViewById(R.id.forex_custoner_fix_code);
		sellTagText = (TextView) findViewById(R.id.forex_customer_fix_cash);
		priceText = (TextView) findViewById(R.id.forex_custoner_fix_type);
		ccMoneyText = (TextView) findViewById(R.id.forex_custoner_fix_interestStartsDate);
		tradeButton = (Button) findViewById(R.id.trade_nextButton);
		buttonView = findViewById(R.id.btn_sure);
		// 返回按钮
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initDate() {
		String accountNumber = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(IsForex.ISFOREX_ACCOUNTNUMBER_REQ);
		if (StringUtil.isNull(accountNumber)) {
			return;
		}
		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		int position = intent.getIntExtra(ConstantGloble.ISFOREX_SELECTEDPOSITION, -1);
		if (position == -1) {
			return;
		}
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.ISFOREX_RESULTLIST_KEY);
		if (resultList.size() <= 0 || resultList == null) {
			return;
		}
		Map<String, Object> map = resultList.get(position);
		if (StringUtil.isNullOrEmpty(map)) {
			return;
		}
		String settle = (String) map.get(ConstantGloble.ISFOREX_SETTLE);

		if (!StringUtil.isNull(settle) && LocalData.Currency.containsKey(settle)) {
			settleCode = LocalData.Currency.get(settle);
		}
		Map<String, Object> details = (Map<String, Object>) map.get(ConstantGloble.ISFOREX_DETAILSMAP);
		if (StringUtil.isNullOrEmpty(details)) {
			return;
		}
		Map<String, String> currency1 = (Map<String, String>) details.get(IsForex.ISFOREX_CURRENCY1_RES);
		Map<String, String> currency2 = (Map<String, String>) details.get(IsForex.ISFOREX_CURRENCY2_RES);
		String code1 = currency1.get(IsForex.ISFOREX_CODE_RES);
		String code2 = currency2.get(IsForex.ISFOREX_CODE_RES);
		code = null;
		if (!StringUtil.isNull(code1) && LocalData.Currency.containsKey(code1) && !StringUtil.isNull(code2)
				&& LocalData.Currency.containsKey(code2)) {
			code1 = LocalData.Currency.get(code1);
			code2 = LocalData.Currency.get(code2);
			code = code1 + "/" + code2;
//			人民币金、银、铂、钯、美元金、银、铂、钯   贵金属的币种
			if(LocalData.goldLists.contains(code1)||LocalData.goldLists.contains(code2)){
				vfgType = "G";
			}else {
				vfgType = "F";
			}
		}
		direction = (String) details.get(IsForex.ISFOREX_DIRECTION_RES);
		if (!StringUtil.isNull(direction) && LocalData.isForexdirectionMap.containsKey(direction)) {
			direction = LocalData.isForexdirectionMap.get(direction);
		}
		String meanPrice = (String) details.get(IsForex.ISFOREX_MEANPRICE_RES);
		balance = (String) details.get(IsForex.ISFOREX_BALANCE_RES);
		float f = Float.valueOf(balance);
		if (f > 0) {
			buttonView.setVisibility(View.VISIBLE);
		} else {
			buttonView.setVisibility(View.GONE);
		}
		String mon = null;
		if (!StringUtil.isNull(balance) && !StringUtil.isNull(settle)) {
			if (LocalData.codeNoNumber.contains(settle)) {
				mon = StringUtil.parseStringPattern(balance, twoNumber);
			} else {
				mon = StringUtil.parseStringPattern(balance, fourNumber);
			}
		}
		accountNumber = StringUtil.getForSixForString(accountNumber);
		accNumberText.setText(accountNumber);
		jsCodeText.setText(settleCode);
		codeText.setText(code);
		sellTagText.setText(direction);
		priceText.setText(meanPrice);
		ccMoneyText.setText(mon);

	}

	private void initOnClick() {
		tradeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 查询货币对
				BaseHttpEngine.showProgressDialog();
				requestPsnVFGGetAllRate(vfgType);
			}
		});
	}

	// 查询货币对---回调
	@Override
	public void requestPsnVFGGetAllRateCallback(Object resultObj) {
		super.requestPsnVFGGetAllRateCallback(resultObj);
		if (codeResultList == null || codeResultList.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse()
					.showInfoMessageDialog(this.getResources().getString(R.string.isForex_trade_code));
			return;
		}
		boolean i = isIncodeResultList();
		if (!i) {
			// 货币对
			String msg1 = getResources().getString(R.string.isForex_trade_emg_code1);
			String msg2 = code;
			String msg3 = getResources().getString(R.string.isForex_trade_emg_code2);
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(msg1 + msg2 + msg3);
			return;
		} else {
			// 查询结算币种
			requestPsnVFGGetRegCurrency(vfgType);
		}
	}

	// 查询结算币种----回调
	@Override
	public void requestPsnVFGGetRegCurrencyCallback(Object resultObj) {
		super.requestPsnVFGGetRegCurrencyCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (vfgRegCurrencyList.size() <= 0 || vfgRegCurrencyList == null) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getResources().getString(R.string.isForex_trade_jcCode));
			return;
		}
		boolean i = isInVfgRegCurrencyList();
		if (!i) {
			// 结算币种
			String msg1 = getResources().getString(R.string.isForex_trade_emg_code1);
			String msg2 = settleCode;
			String msg3 = getResources().getString(R.string.isForex_trade_emg_code3);
			BaseDroidApp.getInstanse().showInfoMessageDialog(msg1 + msg2 + msg3);
			return;
		} else {
//			在我的双向宝详情中进入时，增加追击止损交易方式。
			Intent intent = new Intent(this, IsForexTradeSubmitActivity.class);
			intent.putExtra(ConstantGloble.ISFOREX_CODECODENAME_KEY, code);// 货币对名称
			intent.putExtra(ConstantGloble.ISFOREX_SETTLECURRECNY_KEY, settleCode);// 结算币种名称
			String buy = LocalData.isForexSellTagList.get(0);
			String sell = LocalData.isForexSellTagList.get(1);
			String dir = null;
			if (direction.equals(buy)) {
				dir = sell;
			} else if (direction.equals(sell)) {
				dir = buy;
			}
			intent.putExtra(ConstantGloble.ISFOREX_DIRECTION_KEY, dir);// 买卖方向
			intent.putExtra(IsForex.ISFOREX_BALANCE_RES, balance);// 持仓余额
			intent.putExtra(ConstantGloble.ISFOREX_REQUEXTTRADE_KEY, ConstantGloble.ISFOREX_MINE_TRADE_ACTIVITY);// 301
			startActivity(intent);
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
						this.getResources().getString(R.string.isForex_trade_code));
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
					// 货币对在
					codePosition = true;
					break;
				}
			}
		}
		return codePosition;
	}

	/** 结算币种是否在结算币种集合里面 */
	private boolean isInVfgRegCurrencyList() {
		int len = vfgRegCurrencyList.size();
		for (int i = 0; i < len; i++) {
			String codes = vfgRegCurrencyList.get(i);
			if (!StringUtil.isNull(codes) && LocalData.Currency.containsKey(codes)) {
				String code = LocalData.Currency.get(codes);
				if (code.equals(settleCode)) {
					settleCurrecnyPosition = true;
					break;
				}
			}
		}
		return settleCurrecnyPosition;
	}
}
