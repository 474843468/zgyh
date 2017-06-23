package com.chinamworld.bocmbci.biz.lsforex.query;

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
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 斩仓交易查询 详情页面 */
public class IsForexZCQueryDetailActivity extends IsForexBaseActivity {
	public static final String TAG = "IsForexZCQueryDetailActivity";
	/** activity View */
	private View queryView = null;
	/**
	 * backButton:返回按钮
	 */
	private Button backButton = null;
	private Button sureButton = null;
	private TextView zcqNumberText = null;
	private TextView zcNumberText = null;
	private TextView jsCodeText = null;
	private TextView zcTimesText = null;
	private TextView codeText = null;
	private TextView zcMoneyText = null;
	private TextView zcqCzlText = null;
	private TextView zchCzlText = null;
	private TextView leftText1 = null;
	private TextView leftText2 = null;
	private TextView leftText3 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		init();
		initData();
		initClick();
	}

	private void init() {
		queryView = LayoutInflater.from(IsForexZCQueryDetailActivity.this).inflate(R.layout.isforex_query_zc_detail,
				null);
		tabcontent.addView(queryView);
		/** 为界面标题赋值 */
		setTitle(getResources().getString(R.string.query_title_two));
		backButton = (Button) findViewById(R.id.ib_back);
		sureButton = (Button) findViewById(R.id.forex_query_deal_detailes_ok);
		zcqNumberText = (TextView) findViewById(R.id.forex_trade_number);
		zcNumberText = (TextView) findViewById(R.id.forex_trade_sell);
		jsCodeText = (TextView) findViewById(R.id.forex_trade_sell_code);
		zcTimesText = (TextView) findViewById(R.id.forex_trade_buy);
		codeText = (TextView) findViewById(R.id.forex_trade_code);
		zcMoneyText = (TextView) findViewById(R.id.forex_trade_type);
		zcqCzlText = (TextView) findViewById(R.id.forex_trade_times);
		zchCzlText = (TextView) findViewById(R.id.forex_trade_wtType);
		leftText1 = (TextView) findViewById(R.id.number1);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, leftText1);
		leftText2 = (TextView) findViewById(R.id.number2);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, leftText2);
		leftText3 = (TextView) findViewById(R.id.number3);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, leftText3);
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
		String vfgRegCode = intent.getStringExtra(ConstantGloble.ISFOREX_VFGREGCODE);
		String jsCode = null;
		if (!StringUtil.isNull(vfgRegCode)) {
			jsCode = LocalData.Currency.get(vfgRegCode);
		}
		int position = intent.getIntExtra(ConstantGloble.ISFOREX_SELECTEDPOSITION, -1);
		if (position == -1) {
			return;
		}
		Map<String, Object> map = queryResultList.get(position);

		String oldExchangeSeq = (String) map.get(IsForex.ISFOREX_OLDEXCHANGESEQ_REQ);
		String liquidationNo = (String) map.get(IsForex.ISFOREX_LIQUIDATIONNO_REQ);
		String liquidationDate = (String) map.get(IsForex.ISFOREX_LIQUIDATIONDATE_REQ);
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
		String liquidationAmount = (String) map.get(IsForex.ISFOREX_LIQUIDATIONAMOUNT_REQ);
		String beforeLiquidRatio = (String) map.get(IsForex.ISFOREX_BEFORELIQUIDRATIO_REQ);
		String afterLiquidRatio = (String) map.get(IsForex.ISFOREX_AFTERLIQUIDRATIO_REQ);
		zcqNumberText.setText(oldExchangeSeq);
		zcNumberText.setText(liquidationNo);
		jsCodeText.setText(jsCode);
		zcTimesText.setText(liquidationDate);
		codeText.setText(code);
		String amount = null;
		if (!StringUtil.isNull(liquidationAmount)) {
			if (LocalData.codeNoNumber.contains(vfgRegCode)) {
				amount = StringUtil.parseStringPattern(liquidationAmount, twoNumber);
			} else {
				amount = StringUtil.parseStringPattern(liquidationAmount, fourNumber);
			}
		} else {
			amount = "-";
		}
		zcMoneyText.setText(amount);
		String before = null;
		if (StringUtil.isNull(beforeLiquidRatio)) {
			before = "-";
		} else {
			before = StringUtil.dealNumber(beforeLiquidRatio);
		}
		zcqCzlText.setText(before);
		String after = null;
		if (StringUtil.isNull(afterLiquidRatio)) {
			after = "-";
		} else {
			after = StringUtil.dealNumber(afterLiquidRatio);
		}
		zchCzlText.setText(after);
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
