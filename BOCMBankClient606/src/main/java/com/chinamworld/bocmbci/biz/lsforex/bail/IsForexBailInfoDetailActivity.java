package com.chinamworld.bocmbci.biz.lsforex.bail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.biz.lsforex.adapter.IsForexBailInfoDetailAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 保证金账户详情页面 */
public class IsForexBailInfoDetailActivity extends IsForexBaseActivity {
	public static final String TAG = "IsForexBailInfoDetailActivity";
	/** 保证金账户信息 listView */
	private ListView listView = null;
	/** 返回 */
	private Button backButton = null;
	/**
	 * IsForexBailInfoDetailActivity的主布局
	 */
	private View rateInfoView = null;
	/** 保证金账户信息 */
	private List<Map<String, Object>> result = null;
	private IsForexBailInfoDetailAdapter adapter = null;
	private List<Map<String, String>> dealList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.isForex_myRate_title));
		// result：保证金交易填写页面
		result = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.ISFOREX_RESULT_KEY);
		dealList = dealDate();
		if (dealList == null || dealList.size() <= 0) {
			return;
		}
		init();
	}

	/** 处理保证金数据 */
	private List<Map<String, String>> dealDate() {
		List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
		if (result == null || result.size() <= 0) {
			return null;
		}
		int len = result.size();
		for (int i = 0; i < len; i++) {
			Map<String, Object> map = result.get(i);
			// 结算币种
			Map<String, String> settleCurrency = (Map<String, String>) map.get(IsForex.ISFOREX_SETTLECURRENCY1_RES);
			String jsCode = null;
			String code = null;
			if (!StringUtil.isNullOrEmpty(settleCurrency)) {
				code = settleCurrency.get(IsForex.ISFOREX_CODE1_RES);
				if (!StringUtil.isNull(code) && LocalData.Currency.containsKey(code)) {
					jsCode = LocalData.Currency.get(code);
				}
			}
			// 保证金净值
			String marginNetBalance = (String) map.get(IsForex.ISFOREX_MARGINNETBALANCE_RES);
			// 账户暂计盈亏
			String currentProfitLoss = (String) map.get(IsForex.ISFOREX_CURRENTPROFITLOSS_RES);
			// 暂计盈亏标识
			String profitLossFlag = (String) map.get(IsForex.ISFOREX_PROFITLOSSFLAG_RES);
			// 占用保证金
			String marginOccupied = (String) map.get(IsForex.ISFOREX_MARGINOCCUPIED_RES);
			// 可用保证金
			String marginAvailable = (String) map.get(IsForex.ISFOREX_MARGINAVAILABLE_RES);
			// 最大可交易额
			String maxTradeAmount = (String) map.get(IsForex.ISFOREX_MAXTRADEAMOUNT_RES);
			// 保证金充足率
			String marginRate = (String) map.get(IsForex.ISFOREX_MARGINRATE_RES);
			// 提示信息
			String message = (String) map.get("alarmFlag");
			// 账户余额
			String marginFund = (String) map.get(IsForex.ISFOREX_MARGINFUND_RES);
			List<Map<String, Object>> fundList = (List<Map<String, Object>>) map.get(IsForex.ISFOREX_FUNDLIST_RES);
			if (fundList == null || fundList.size() <= 0) {
				Map<String, String> dealMap = new HashMap<String, String>();
				dealMap.put(IsForex.ISFOREX_SETTLECURRENCY1_RES, code);
				dealMap.put(IsForex.ISFOREX_MARGINNETBALANCE_RES, marginNetBalance);
				dealMap.put(IsForex.ISFOREX_CURRENTPROFITLOSS_RES, currentProfitLoss);
				dealMap.put(IsForex.ISFOREX_PROFITLOSSFLAG_RES, profitLossFlag);
				dealMap.put(IsForex.ISFOREX_MARGINOCCUPIED_RES, marginOccupied);
				dealMap.put(IsForex.ISFOREX_MARGINAVAILABLE_RES, marginAvailable);
				dealMap.put(IsForex.ISFOREX_MAXTRADEAMOUNT_RES, maxTradeAmount);
				dealMap.put(IsForex.ISFOREX_MARGINRATE_RES, marginRate);
				dealMap.put("alarmFlag", message);
				if (StringUtil.isNullOrEmpty(marginFund)){
					dealMap.put(IsForex.ISFOREX_MARGINFUND_RES, "-");
				}else{
					dealMap.put(IsForex.ISFOREX_MARGINFUND_RES, marginFund);
				}
				dealMap.put(ConstantGloble.ISFOREX_CASH1, null);
				dealMap.put(ConstantGloble.ISFOREX_CASH2, null);
				dealMap.put(ConstantGloble.ISFOREX_AMOUNT1, null);
				dealMap.put(ConstantGloble.ISFOREX_AMOUNT2, null);
				lists.add(dealMap);
			} else {
				int len1 = fundList.size();
				String amount1 = null;
				String noteCashFlag1 = null;
				String amount2 = null;
				String noteCashFlag2 = null;
				if (len1 == 1) {
					Map<String, Object> maps = fundList.get(0);
					amount1 = (String) maps.get(IsForex.ISFOREX_AMOUNT_RES);
					noteCashFlag1 = (String) maps.get(IsForex.ISFOREX_NOTECASHFLAG_RES);
				} else if (len1 > 1) {
					Map<String, Object> map1= fundList.get(0);
					amount1 = (String) map1.get(IsForex.ISFOREX_AMOUNT_RES);
					noteCashFlag1 = (String) map1.get(IsForex.ISFOREX_NOTECASHFLAG_RES);
					Map<String, Object> maps = fundList.get(1);
					amount2 = (String) maps.get(IsForex.ISFOREX_AMOUNT_RES);
					noteCashFlag2 = (String) maps.get(IsForex.ISFOREX_NOTECASHFLAG_RES);
				}
				Map<String, String> dealMap = new HashMap<String, String>();
				dealMap.put(IsForex.ISFOREX_SETTLECURRENCY1_RES, code);
				dealMap.put(IsForex.ISFOREX_MARGINNETBALANCE_RES, marginNetBalance);
				dealMap.put(IsForex.ISFOREX_CURRENTPROFITLOSS_RES, currentProfitLoss);
				dealMap.put(IsForex.ISFOREX_PROFITLOSSFLAG_RES, profitLossFlag);
				dealMap.put(IsForex.ISFOREX_MARGINOCCUPIED_RES, marginOccupied);
				dealMap.put(IsForex.ISFOREX_MARGINAVAILABLE_RES, marginAvailable);
				dealMap.put(IsForex.ISFOREX_MAXTRADEAMOUNT_RES, maxTradeAmount);
				dealMap.put(IsForex.ISFOREX_MARGINRATE_RES, marginRate);
				dealMap.put(IsForex.ISFOREX_MESSAGE_RES, message);
				dealMap.put(IsForex.ISFOREX_MARGINFUND_RES, marginFund);
				dealMap.put(ConstantGloble.ISFOREX_CASH1, noteCashFlag1);
				dealMap.put(ConstantGloble.ISFOREX_CASH2, noteCashFlag2);
				dealMap.put(ConstantGloble.ISFOREX_AMOUNT1, amount1);
				dealMap.put(ConstantGloble.ISFOREX_AMOUNT2, amount2);
				lists.add(dealMap);
			}
		}
		return lists;
	}

	private void init() {
		rateInfoView = LayoutInflater.from(this).inflate(R.layout.isforex_bail_bzjinfo, null);
		tabcontent.addView(rateInfoView);
		listView = (ListView) findViewById(R.id.rate_listView);
		backButton = (Button) findViewById(R.id.ib_back);
		adapter = new IsForexBailInfoDetailAdapter(this, dealList);
		listView.setAdapter(adapter);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}
