package com.chinamworld.bocmbci.biz.forex.customer;

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
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.biz.forex.ForexBaseActivity;
import com.chinamworld.bocmbci.biz.forex.rate.ForexQuickTradeSubmitActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/***
 * 我的外汇-定期账户详情页面
 * 
 * @author 宁焰红
 * 
 */
public class ForexCustomerFixRateDatailActivity extends ForexBaseActivity {
	private static final String TAG = "ForexCustomerFixRateDatailActivity";
	/** 主视图布局 */
	private LinearLayout tabcontent;// 主Activity显示

	/**
	 * ForexCustomerFixRateDatailActivity的主布局
	 */
	private View rateInfoView = null;
	/**
	 * 返回按钮
	 */
	private Button backButton = null;
	/** 主界面 */
	private Button rightButton = null;
	private TextView volumberText = null;
	private TextView cdNumberText = null;
	/** 钞汇和币种连在一起显示 */
	private TextView codeText = null;
	// private TextView cashText = null;
	private TextView typeText = null;
	/** 起息日 */
	private TextView interestStartsDateText = null;
	/** 存期 */
	private TextView cdperiodText = null;
	/** 利率 */
	private TextView interestRateText = null;
	private TextView balanceText = null;
	private Button sellButton = null;
	/** 用户选择了list集合中的第几个 */
	private int customerSelectedPosition = -1;

	/** 存折册号 */
	private String voumber = null;
	/** 存单号 */
	private String cdNumber = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.biz_activity_606_layout);
		LogGloble.d(TAG, "onCreate");
		initPulldownBtn();
		initLeftSideList(this, LocalData.forexStorageCashLeftList);
		initFootMenu();
		volumeNumberList = (List<String>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(Forex.FOREX_VOLUMENUMBERLIST_RES);
		init();
		initData();
		initOnClick();
	}

	public void init() {
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		rateInfoView = LayoutInflater.from(ForexCustomerFixRateDatailActivity.this).inflate(
				R.layout.forex_customer_fix_detail, null);
		tabcontent.addView(rateInfoView);
		setTitle(getResources().getString(R.string.forex_customer));
		backButton = (Button) findViewById(R.id.ib_back);
		rightButton = (Button) findViewById(R.id.ib_top_right_btn);
		rightButton.setVisibility(View.VISIBLE);
		rightButton.setText(getResources().getString(R.string.forex_right));
		volumberText = (TextView) findViewById(R.id.forex_rate_fix_accNumber);
		cdNumberText = (TextView) findViewById(R.id.forex_rate_fix_number);
		codeText = (TextView) findViewById(R.id.forex_custoner_fix_code);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, codeText);
		// cashText = (TextView) findViewById(R.id.forex_customer_fix_cash);
		typeText = (TextView) findViewById(R.id.forex_custoner_fix_type);
		interestStartsDateText = (TextView) findViewById(R.id.forex_custoner_fix_interestStartsDate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, interestStartsDateText);
		cdperiodText = (TextView) findViewById(R.id.forex_custoner_fix_cdperiod);
		interestRateText = (TextView) findViewById(R.id.forex_custoner_fix_interestRate);
		balanceText = (TextView) findViewById(R.id.forex_custoner_fix_balance);
		sellButton = (Button) findViewById(R.id.forex_query_deal_detailes_ok);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
	}

	public void initData() {
		List<Map<String, Object>> accInfoList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.FOREX_ACCINFOLIST_KEY);
		if (accInfoList == null || accInfoList.size() == 0) {
			return;
		}
		Intent intent = getIntent();
		if (intent == null) {
			return;
		} else {
			customerSelectedPosition = intent.getIntExtra(ConstantGloble.FOREX_customerSelectedPosition, -1);
			if (customerSelectedPosition == -1) {
				return;
			} else {
				Map<String, Object> accInfoMap = accInfoList.get(customerSelectedPosition);
				if (accInfoMap == null) {
					return;
				} else {
					voumber = (String) accInfoMap.get(Forex.FOREX_VOLUMENUMBER_RES);
					cdNumber = (String) accInfoMap.get(Forex.FOREX_CDNUMBER_RES);
					Map<String, String> currency = (Map<String, String>) accInfoMap.get(Forex.FOREX_CURRENCY_RES);
					String code = currency.get(Forex.FOREX_CURRENCY_CODE_RES);
					String transCode = null;
					if (LocalData.Currency.containsKey(code)) {
						transCode = LocalData.Currency.get(code);

					}
					String cashRemit = (String) accInfoMap.get(Forex.FOREX_CASHREMIT_RES);
					String cash = null;
					if (LocalData.CurrencyCashremit.containsKey(cashRemit)) {
						cash = LocalData.CurrencyCashremit.get(cashRemit);
					}

					// 存款种类
					String type = (String) accInfoMap.get(Forex.FOREX_TYPE_RES);
					String transType = null;
					if (LocalData.fixAccTypeMap.containsKey(type)) {
						transType = LocalData.fixAccTypeMap.get(type);
					}
					String interestStartsDate = (String) accInfoMap.get(Forex.FOREX_INTERESTSTARTSDATE_RES);
					String cdperiod = (String) accInfoMap.get(Forex.FOREX_CDPERIOD_RES);
					String interestRate = (String) accInfoMap.get(Forex.FOREX_INTERESTRATE_RES);
					Map<String, Object> balanceMap = (Map<String, Object>) accInfoMap.get(Forex.FOREX_BALANCE_RES);
					String availableBalance = (String) balanceMap.get(Forex.FOREX_AVAILABLEBALANCE_RES);
					LogGloble.d(TAG + " availableBalance", availableBalance);
					volumberText.setText(voumber);
					cdNumberText.setText(cdNumber);
					codeText.setText(transCode + cash);
					// cashText.setText(cash);
					typeText.setText(transType);
					interestStartsDateText.setText(interestStartsDate);
					String cunqi = null;
					if (!StringUtil.isNull(cdperiod) && LocalData.cdperiodMap.containsKey(cdperiod)) {
						cunqi = LocalData.cdperiodMap.get(cdperiod);
					}
					cdperiodText.setText(cunqi);
					interestRateText.setText(interestRate);
					if (!StringUtil.isNullOrEmpty(availableBalance)) {
						double a = Double.valueOf(availableBalance);
						// 可用余额《=0不显示卖出按钮
						if (a <= 0) {
							sellButton.setVisibility(View.GONE);
						}
						if (!StringUtil.isNull(code)) {
							availableBalance = StringUtil.parseStringCodePattern(code, availableBalance, fourNumber);
						}
						balanceText.setText(availableBalance);
					} else {
						balanceText.setText("-");
					}
					// 币种为人民币，不显示卖出按钮
					if (code.equals(ConstantGloble.FOREX_RMB_TAG1) || code.equals(ConstantGloble.FOREX_RMB_CNA_TAG2)) {
						sellButton.setVisibility(View.GONE);
					}

				}
			}
		}
	}

	public void initOnClick() {
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		rightButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				Intent intent = new Intent(ForexCustomerFixRateDatailActivity.this, SecondMainActivity.class);
//				startActivity(intent);
				goToMainActivity();
				finish();
			}
		});
		// 卖出按钮
		sellButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LogGloble.d(TAG, "sell------");
				if (volumeNumberList == null || volumeNumberList.size() == 0) {
					return;
				}
				int len = volumeNumberList.size();
				boolean t = false;
				for (int i = 0; i < len; i++) {
					if (voumber.equals(volumeNumberList.get(i))) {
						// 查询买入币种是否存在
						BaseHttpEngine.showProgressDialog();
						tradeConditionPsnForexQueryBuyCucyList();
						t = true;
						break;
					}
				}
				if (!t) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							ForexCustomerFixRateDatailActivity.this.getString(R.string.forex_customer_fix_sell));
					return;
				}

			}
		});
	}

	/** 买入币种是否存在----回调 */
	@Override
	public void tradeConditionPsnForexQueryBuyCucyListCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.forex_rateinfo_buy_codes));
			return;
		}
		tradeBuyCodeResultList = (List<Map<String, String>>) biiResponseBody.getResult();
		if (tradeBuyCodeResultList == null || tradeBuyCodeResultList.size() <= 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					ForexCustomerFixRateDatailActivity.this.getString(R.string.forex_rateinfo_buy_codes));
			return;
		} else {
			getBuyCode(tradeBuyCodeResultList);
			Intent intent = new Intent(ForexCustomerFixRateDatailActivity.this, ForexQuickTradeSubmitActivity.class);
			intent.putExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, ConstantGloble.FOREX_CUSTOMER_FIX_TAG);
			intent.putExtra(Forex.FOREX_VOLUMENUMBER_RES, voumber);
			intent.putExtra(Forex.FOREX_CDNUMBER_RES, cdNumber);
			startActivity(intent);
		}
	}

	/** 处理买入币种信息 */
	private void getBuyCode(List<Map<String, String>> list) {
		List<Map<String, String>> buyCodeResultList = list;
		// (List<Map<String, String>>) BaseDroidApp.getInstanse()
		// .getBizDataMap().get(ConstantGloble.FOREX_BUYCODERESULTLIST)
		int len = buyCodeResultList.size();
		List<String> buyCodeList = new ArrayList<String>();
		List<String> buyCodeDealList = new ArrayList<String>();
		for (int i = 0; i < len; i++) {
			Map<String, String> buyCodeResulMap = buyCodeResultList.get(i);
			String buyCodeResul = buyCodeResulMap.get(Forex.FOREX_BUY_CODE_RES).trim();
			String code = null;
			if (LocalData.Currency.containsKey(buyCodeResul)) {
				code = LocalData.Currency.get(buyCodeResul);
				buyCodeList.add(buyCodeResul);
				buyCodeDealList.add(code);
			} else {
				continue;
			}
		}
		if (buyCodeDealList == null || buyCodeDealList.size() <= 0 || buyCodeList == null || buyCodeList.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_BUYCODEDEALLIST_KEY, buyCodeDealList);
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_BUYCODELIST_KEY, buyCodeList);
		}
	}
}
