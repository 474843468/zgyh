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
import com.chinamworld.bocmbci.utils.StringUtil;

/** 对账单 交易查询 详情页面 */
public class IsForexAccQueryDetailActivity extends IsForexBaseActivity {
	public static final String TAG = "IsForexAccQueryDetailActivity";
	/** activity View */
	private View queryView = null;
	/**
	 * backButton:返回按钮
	 */
	private Button backButton = null;
	private Button sureButton = null;
	private TextView jzNumberText = null;
	private TextView jsCodeText = null;
	private TextView cashText = null;
	private TextView accTypeText = null;
	private TextView accTagText = null;
	private TextView accJeText = null;
	private TextView accMoneyText = null;
	private TextView jyTimes = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		init();
		initData();
		initClick();
	}

	private void init() {
		queryView = LayoutInflater.from(IsForexAccQueryDetailActivity.this).inflate(R.layout.isforex_query_acc_detail,
				null);
		tabcontent.addView(queryView);
		/** 为界面标题赋值 */
		setTitle(getResources().getString(R.string.query_title_four));
		backButton = (Button) findViewById(R.id.ib_back);
		sureButton = (Button) findViewById(R.id.forex_query_deal_detailes_ok);

		jzNumberText = (TextView) findViewById(R.id.forex_trade_number);
		jsCodeText = (TextView) findViewById(R.id.forex_trade_sell);
		cashText = (TextView) findViewById(R.id.forex_trade_sell_code);
		accTypeText = (TextView) findViewById(R.id.forex_trade_buy);
		accTagText = (TextView) findViewById(R.id.forex_trade_code);
		accJeText = (TextView) findViewById(R.id.forex_trade_type);
		accMoneyText = (TextView) findViewById(R.id.forex_trade_times);
		jyTimes = (TextView) findViewById(R.id.forex_trade_wtType);

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
		String fundSeq = (String) map.get(IsForex.ISFOREX_FUNDSEQ_REQ);
		// Map<String, String> fundCurrency = (Map<String, String>)
		// map.get(IsForex.ISFOREX_FUNDCURRENCY_REQ);
		// String code1 = null;

		// if (!StringUtil.isNullOrEmpty(fundCurrency)) {
		// code1 = fundCurrency.get(IsForex.ISFOREX_CODE_REQ);
		// }
		// String codes = null;
		// if (!StringUtil.isNull(code1)) {
		// codes = LocalData.Currency.get(code1);
		// }
		String noteCashFlag = (String) map.get(IsForex.ISFOREX_NOTECASHFLAG_REQ);
		String cash = null;
		if (!StringUtil.isNull(noteCashFlag)) {
			cash = LocalData.CurrencyCashremit.get(noteCashFlag);
		}
		String fundTransferType = (String) map.get(IsForex.ISFOREX_FUNDTRANSFERTYPE_REQ);
		String accType = null;
		if (!StringUtil.isNull(fundTransferType)) {
			accType = LocalData.isForexfundTransferTypeMap.get(fundTransferType);
		}
		String transferDir = (String) map.get(IsForex.ISFOREX_TRANSFERDIR_REQ);
		String accTag = null;
		if (!StringUtil.isNull(transferDir)) {
			accTag = LocalData.isForextransferDirMap.get(transferDir);
		}

		String transferAmount = (String) map.get(IsForex.ISFOREX_TRANSFERAMOUNT_REQ);
		if (!StringUtil.isNull(transferAmount) && !StringUtil.isNull(settleCurrecny)) {
			if (LocalData.codeNoNumber.contains(settleCurrecny)) {
				transferAmount = StringUtil.parseStringPattern(transferAmount, twoNumber);
			} else {
				transferAmount = StringUtil.parseStringPattern(transferAmount, fourNumber);
			}
		}
		String balance = (String) map.get(IsForex.ISFOREX_BALANCE_REQ);
		if (!StringUtil.isNull(balance) && !StringUtil.isNull(settleCurrecny)) {
			if (LocalData.codeNoNumber.contains(settleCurrecny)) {
				balance = StringUtil.parseStringPattern(balance, twoNumber);
			} else {
				balance = StringUtil.parseStringPattern(balance, fourNumber);
			}
		}
		String transferDate = (String) map.get(IsForex.ISFOREX_TRANSFERDATE_REQ);
		jzNumberText.setText(fundSeq);
		jsCodeText.setText(settle);
		cashText.setText(cash);
		accTypeText.setText(accType);
		accTagText.setText(accTag);
		accJeText.setText(transferAmount);
		accMoneyText.setText(balance);
		jyTimes.setText(transferDate);

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
