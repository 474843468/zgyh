package com.chinamworld.bocmbci.biz.forex.strike;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.biz.forex.adapter.ForexStrikeQueryInfoAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;

/**
 * 成交查询结果页面----此页面不再使用
 * 
 * @author 宁焰红
 * 
 */
public class ForexStrikeQueryInfoActivity extends BaseActivity {
	private static final String TAG = "ForexStrikeQueryInfoActivity";
	/** 主视图布局 */
	private LinearLayout tabcontent;// 主Activity显示

	/**
	 * ForexStrikeQueryInfoActivity的主布局
	 */
	private View rateInfoView = null;
	/**
	 * backButton:返回按钮
	 */
	private Button backButton = null;
	private ListView listView = null;
	private TextView timeText = null;
	private String startDate = null;
	private String endDate = null;
	/**
	 * 存储查询后的信息
	 */
	List<Map<String, Object>> tradeList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		LogGloble.d(TAG, "onCreate");
		// 初始化弹窗按钮
		initPulldownBtn();
		// 初始化左边菜单
		initLeftSideList(this, LocalData.forexStorageCashLeftList);
		// 初始化底部菜单
		init();

	}

	@Override
	protected void onStart() {
		super.onStart();
		Intent intent = getIntent();
		if (intent == null) {
			return;
		} else {
			startDate = intent.getStringExtra(Forex.FOREX_STARTDATE_REQ);
			LogGloble.d(TAG, startDate);
			endDate = intent.getStringExtra(Forex.FOREX_ENDDATE_REQ);
			LogGloble.d(TAG, endDate);
		}
		String text=getResources().getString(R.string.acc_query_date);
		timeText.setText(text+startDate + "-" + endDate);
		tradeList = (List<Map<String, Object>>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.FOREX_TRADE_LIST_KEY);
		if (tradeList == null) {
			tradeList = null;
			listView.setVisibility(View.GONE);
		} else {
			LogGloble.d(TAG, String.valueOf(tradeList.size()));
			translateCode();
			listView.setAdapter(new ForexStrikeQueryInfoAdapter(
					ForexStrikeQueryInfoActivity.this, tradeList));
		}
	}



//	// 选择二级菜单
//	@Override
//	protected void setSelectedMenu(int clickIndex) {
//		super.setSelectedMenu(clickIndex);
//		switch (clickIndex) {
//		case 0:// 外汇行情页面
//			Intent intent0 = new Intent();
//			intent0.setClass(this, ForexRateInfoOutlayActivity.class);
//			startActivity(intent0);
//			break;
//		case 1:// 我的外汇
//			
//			break;
//		case 2:// 委托状况查询
//			
//			break;
//		case 3:// 成交状况查询
//			Intent intent3 = new Intent();
//			intent3.setClass(this, ForexStrikeQueryActivity.class);
//			startActivity(intent3);
//			break;
//		default:// 到贵金属详情页面
//
//			break;
//		}
//
//	}

	
	
	
	
	/**
	 * 初始化所有的控件
	 */
	private void init() {
		LogGloble.d(TAG, "init");
		initFootMenu();
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		rateInfoView = LayoutInflater.from(ForexStrikeQueryInfoActivity.this)
				.inflate(R.layout.forex_trade_query_info, null);
		tabcontent.addView(rateInfoView);
		/** 为界面标题赋值 */
		setTitle(getResources().getString(R.string.forex_trade));
		backButton = (Button) findViewById(R.id.ib_back);
		listView = (ListView) findViewById(R.id.rate_listView);
		timeText = (TextView) findViewById(R.id.trade_time);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 返回到成交状况查询页面
				Intent intent = new Intent(ForexStrikeQueryInfoActivity.this,
						ForexStrikeQueryActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	public void translateCode() {
		int len = tradeList.size();
		for (int i = 0; i < len; i++) {
			Map<String, Object> trdeMap = tradeList.get(i);
			Map<String, String> firstBuyCurrency = (Map<String, String>) trdeMap
					.get(Forex.FOREX_FIRSTBUYCURRENY_RES);
			String buyCode = firstBuyCurrency.get(Forex.FOREX_CODE_RES);
			LogGloble.d(TAG+" buyCode", buyCode);
			if (LocalData.Currency.containsKey(buyCode)) {
				String currencyCode = LocalData.Currency.get(buyCode);
				LogGloble.d(TAG+" currencyCode", currencyCode);
				firstBuyCurrency.put(Forex.FOREX_CODE_RES, currencyCode);
				trdeMap.put(Forex.FOREX_FIRSTBUYCURRENY_RES, firstBuyCurrency);
				tradeList.set(i, trdeMap);
			}
			Map<String, String> firstSellCurrency = (Map<String, String>) trdeMap
					.get(Forex.FOREX_FIRESTSELLCURRENCY_RES);
			String sellCode = firstSellCurrency.get(Forex.FOREX_CODE_RES);
			LogGloble.d(TAG+" sellCode", sellCode);
			if (LocalData.Currency.containsKey(sellCode)) {
				String currencyCode = LocalData.Currency.get(sellCode);
				LogGloble.d(TAG+" currencyCode", currencyCode);
				firstSellCurrency.put(Forex.FOREX_CODE_RES, currencyCode);
				trdeMap.put(Forex.FOREX_FIRESTSELLCURRENCY_RES,
						firstSellCurrency);
				tradeList.set(i, trdeMap);
			}
		}

	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.TwoTask;
	}

}
