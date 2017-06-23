package com.chinamworld.bocmbci.biz.forex.strike;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.biz.forex.ForexBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 成交查询详情页面
 * 
 * @author 宁焰红
 * 
 */
public class ForexStrikeQueryDetailActivity extends ForexBaseActivity {
	private static final String TAG = "ForexStrikeQueryDetailActivity";
	/** 主视图布局 */
	// private LinearLayout tabcontent;// 主Activity显示

	/**
	 * ForexStrikeQueryDetailActivity的主布局
	 */
	private View rateInfoView = null;
	/**
	 * backButton:返回按钮
	 */
	private Button backButton = null;
	private TextView number = null;
	private TextView sellCode = null;
	private TextView sellMoney = null;
	private TextView buyCode = null;
	private TextView rate = null;
	private TextView type = null;
	private TextView times = null;
	private Button sureButton = null;

	String consignNumber = null;// 委托序号
	String sellCodeInput = null;
	String buyCodeInput = null;
	String sellAmount = null;// 卖出金额

	String rateInput = null;// 成交汇率
	String exchangeTranType = null;// 成交类型
	String realTransTime = null;// 成交时间
	private Button rightButton = null;
	private TextView buyMoneyText = null;
	private String buyAmount = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		init();
		initOnClick();
	}

	@Override
	protected void onStart() {
		super.onStart();
		Intent intent = getIntent();
		if (intent == null) {
			return;
		} else {
			consignNumber = intent.getStringExtra(Forex.FOREX_CONSIGNNUMBER_RES);
			sellCodeInput = intent.getStringExtra(Forex.FOREX_FIRESTSELLCURRENCY_RES);
			sellAmount = intent.getStringExtra(ConstantGloble.FOREX_SELLAMOUNT_KEY);
			buyAmount = intent.getStringExtra(Forex.FOREX_SECONDBUYAMOUNT_RES);
			if (!StringUtil.isNullOrEmpty(sellAmount)) {
				if ("-".equals(sellAmount)) {

				} else {
					if (codeNoNumberName.contains(sellCodeInput)) {
						sellAmount = StringUtil.parseStringPattern(sellAmount, twoNumber);
					} else {
						sellAmount = StringUtil.parseStringPattern(sellAmount, fourNumber);
					}
				}
			}
			
			buyCodeInput = intent.getStringExtra(Forex.FOREX_FIRSTBUYCURRENY_RES);
			rateInput = intent.getStringExtra(ConstantGloble.FOREX_RATE_KEY);
			exchangeTranType = intent.getStringExtra(Forex.FOREX_EXCHANGETRANTYPE_RES);
			exchangeTranType = LocalData.forexTradeStyleCodeMap.get(exchangeTranType);
			realTransTime = intent.getStringExtra(Forex.FOREX_REALTRANSTIME);
			if(!StringUtil.isNull(buyAmount)){
				if("-".equals(buyAmount)){
					
				}else{
					if(codeNoNumberName.equals(buyCodeInput)){
						buyAmount=StringUtil.parseStringPattern(buyAmount, twoNumber);
					}else{
						buyAmount=StringUtil.parseStringPattern(buyAmount, fourNumber);
					}
				}
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		number.setText(consignNumber);
		sellCode.setText(sellCodeInput);
		sellMoney.setText(sellAmount);
		buyCode.setText(buyCodeInput);
		rate.setText(rateInput);
		type.setText(exchangeTranType);
		times.setText(realTransTime);
		buyMoneyText.setText(buyAmount);
	}

	/**
	 * 初始化所有的控件
	 */
	private void init() {
		rateInfoView = LayoutInflater.from(ForexStrikeQueryDetailActivity.this).inflate(R.layout.forex_trade_detail,
				null);
		tabcontent.addView(rateInfoView);
		/** 为界面标题赋值 */
		setTitle(getResources().getString(R.string.forex_trade));
		backButton = (Button) findViewById(R.id.ib_back);
		number = (TextView) findViewById(R.id.forex_trade_number);
		sellCode = (TextView) findViewById(R.id.forex_trade_sell);
		sellMoney = (TextView) findViewById(R.id.forex_trade_sell_code);
		buyCode = (TextView) findViewById(R.id.forex_trade_buy);
		rate = (TextView) findViewById(R.id.forex_trade_code);
		type = (TextView) findViewById(R.id.forex_trade_type);
		times = (TextView) findViewById(R.id.forex_trade_times);
		sureButton = (Button) findViewById(R.id.forex_query_deal_detailes_ok);
		rightButton = (Button) findViewById(R.id.ib_top_right_btn);
		rightButton.setVisibility(View.VISIBLE);
		rightButton.setText(getResources().getString(R.string.forex_right));
		buyMoneyText = (TextView) findViewById(R.id.forex_rate_currency_buyMoney);

	}

	private void initOnClick() {
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 返回到查询页面
				finish();
			}
		});
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 返回到查询页面
				finish();
			}
		});
		rightButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				Intent intent = new Intent(ForexStrikeQueryDetailActivity.this, SecondMainActivity.class);
//				startActivity(intent);
				goToMainActivity();
				finish();
			}
		});
	}

}
