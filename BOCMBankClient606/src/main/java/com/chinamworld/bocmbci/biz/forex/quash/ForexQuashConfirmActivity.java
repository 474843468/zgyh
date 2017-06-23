package com.chinamworld.bocmbci.biz.forex.quash;

import java.util.HashMap;
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
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 撤单确认页面 */
public class ForexQuashConfirmActivity extends BaseActivity {
	private static final String TAG = "ForexQuashConfirmActivity";
	/** 主视图布局 */
	private LinearLayout tabcontent;// 主Activity显示

	/** ForexStrikeQueryDetailActivity的主布局 */
	private View rateInfoView = null;
	/** backButton:返回按钮 */
	private Button backButton = null;
	/** 撤单按钮 */
	private Button cancelButton = null;
	private TextView numberText = null;
	private TextView sellCodeText = null;
	private TextView buyCodeText = null;
	private TextView chashText = null;
	private TextView sellMoneyText = null;
	private TextView buyMoneyText = null;
	/** 委托方式 */
	private TextView weiTuoTypeText = null;
	private TextView huiLiRateText = null;
	private TextView zhiSunRateText = null;
	private TextView loseRateText = null;
	/** 委托状态 */
	private TextView weiTuoStatusText = null;
	private TextView weiTuoTimesText = null;
	private TextView weiTuoEndTimesText = null;
	/** 1-当前有效，2-历史有效 */
	private int tag = 0;
	/** 08--市价即时 */
	private String eight = null;
	/** 07-限价即时 */
	private String seven = null;
	/** 03---获利委托 */
	private String three = null;
	/** 04--止损委托 */
	private String four = null;
	/** 05--二选一委托 */
	private String five = null;
	/** 11--追击止损委托 */
	private String eleven = null;
	private View huoLiRateView = null;
	private View zhiSunRateView = null;
	private View weiTuoStatusView = null;
	private View loseRateView = null;
	private String accountId = null;
	private String sellCode = null;
	private String buyCode = null;
	private String sAmount = null;
	private String cashRemit = null;
	private String consignNumber = null;
	private String commConversationId = null;
	private Button quickTrade = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setContentView(R.layout.biz_activity_606_layout);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		View footView = findViewById(R.id.rl_menu);
		footView.setVisibility(View.GONE);
		Button leftButton = (Button) findViewById(R.id.btn_show);
		leftButton.setVisibility(View.GONE);
		quickTrade = (Button) findViewById(R.id.ib_top_right_btn);
		quickTrade.setVisibility(View.VISIBLE);
		quickTrade.setText(getResources().getString(R.string.forex_rate_close));
		// 初始化弹窗按钮
		initPulldownBtn();
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 20);
		}
		tag = getIntent().getIntExtra(ConstantGloble.FOREX_TAG, 0);
		dealDates();
		init();
		initDate();
		initOnClick();
		accountId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(Forex.FOREX_RATE_ACCOUNTID_RES);
	}

	private void dealDates() {
		eight = LocalData.forexTradeStyleTransList.get(0);
		seven = LocalData.forexTradeStyleTransList.get(1);
		three = LocalData.forexTradeStyleTransList.get(2);
		four = LocalData.forexTradeStyleTransList.get(3);
		five = LocalData.forexTradeStyleTransList.get(4);
		eleven = LocalData.forexTradeStyleTransList.get(5);
	}

	/**
	 * 初始化所有的控件
	 */
	private void init() {
		rateInfoView = LayoutInflater.from(ForexQuashConfirmActivity.this).inflate(R.layout.forex_quash_confirm, null);
		tabcontent.addView(rateInfoView);
		/** 为界面标题赋值 */
		setTitle(getResources().getString(R.string.forex_quash_title));
		backButton = (Button) findViewById(R.id.ib_back);
		backButton.setVisibility(View.GONE);
		cancelButton = (Button) findViewById(R.id.sureButton);
		numberText = (TextView) findViewById(R.id.forex_trade_number);
		sellCodeText = (TextView) findViewById(R.id.forex_trade_sell);
		buyCodeText = (TextView) findViewById(R.id.forex_trade_buy);
		chashText = (TextView) findViewById(R.id.forex_customer_fix_cash);
		sellMoneyText = (TextView) findViewById(R.id.forex_rate_currency_sellMoney);
		buyMoneyText = (TextView) findViewById(R.id.forex_rate_currency_buyMoney);
		weiTuoTypeText = (TextView) findViewById(R.id.forex_quash_weituo_types);
		huiLiRateText = (TextView) findViewById(R.id.forex_trade_two_huoli);
		zhiSunRateText = (TextView) findViewById(R.id.forex_trade_two_zhisun);
		weiTuoStatusText = (TextView) findViewById(R.id.prms_entrust_state_colon);
		weiTuoTimesText = (TextView) findViewById(R.id.prms_entrust_date_colon);
		weiTuoEndTimesText = (TextView) findViewById(R.id.prms_entrust_enddate_colon);
		loseRateText = (TextView) findViewById(R.id.forex_trade_loseRate);
		huoLiRateView = findViewById(R.id.huoLi_layout);
		zhiSunRateView = findViewById(R.id.zhiSun_layout);
		weiTuoStatusView = findViewById(R.id.weiTuoStatus_layout);
		loseRateView = findViewById(R.id.loseRate_layout);
		weiTuoStatusView.setVisibility(View.GONE);
	}

	@SuppressWarnings("unchecked")
	private void initDate() {
		Map<String, Object> map = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.FOREX_TRADE_LIST_KEY);
		if (StringUtil.isNullOrEmpty(map)) {
			return;
		}
		consignNumber = (String) map.get(Forex.FOREX_CONSIGNNUMBER_RES);
		Map<String, String> firstBuyCurrency = (Map<String, String>) map.get(Forex.FOREX_FIRSTBUYCURRENY_RES);
		buyCode = firstBuyCurrency.get(Forex.FOREX_CODE_RES);
		String buyCodeName = LocalData.Currency.get(buyCode);
		Map<String, String> firstSellCurrency = (Map<String, String>) map.get(Forex.FOREX_FIRESTSELLCURRENCY_RES);
		sellCode = firstSellCurrency.get(Forex.FOREX_CODE_RES);
		String sellCodeName = LocalData.Currency.get(sellCode);

		cashRemit = (String) map.get(Forex.FOREX_CASHREMIT_RES);
		String cash = null;
		if (!StringUtil.isNull(cashRemit)) {
			cash = LocalData.CurrencyCashremit.get(cashRemit);
		}
		String firstSellAmount = (String) map.get(Forex.FOREX_FIRSTSELLAMOUNT_RES);
		String firstBuyAmount = (String) map.get(Forex.FOREX_FIRSTBUYAMOUNT_RES);
		String secondSellAmount = (String) map.get(Forex.FOREX_SECONDSELLAMOUNT_RES);
		String secondBuyAmount = (String) map.get(Forex.FOREX_SECONDBUYAMOUNT_RES);
		String exchangeTranType = (String) map.get(Forex.FOREX_EXCHANGETRANTYPE_RES);
		String firstStatus = (String) map.get(Forex.FOREX_FRESTSTATUS_RES);
		String secondStatus = (String) map.get(Forex.FOREX_SECONDSTATUS_RES);
		String firstType = (String) map.get(Forex.FOREX_FIRSTTYPE_RES);
		String secondType = (String) map.get(Forex.FOREX_SECONDTYPE_RES);
		String firstCustomerRate = (String) map.get(Forex.FOREX_FIRSTCUSTOMERRATE_RES);
		String secondCustomerRate = (String) map.get(Forex.FOREX_SECONDCUSTOMERRATE_RES);
		String consignDate = (String) map.get(Forex.FOREX_CONSIGNDATE_RES);
		String dueDate = (String) map.get(Forex.FOREX_DUEDATE1_RES);

		String type = null;
		if (!StringUtil.isNull(exchangeTranType) && LocalData.forexTradeStyleCodeMap.containsKey(exchangeTranType)) {
			type = LocalData.forexTradeStyleCodeMap.get(exchangeTranType);
		}
		numberText.setText(consignNumber);
		sellCodeText.setText(sellCodeName);
		buyCodeText.setText(buyCodeName);
		chashText.setText(cash);
		weiTuoTypeText.setText(type);
		weiTuoTimesText.setText(consignDate);
		if (StringUtil.isNull(dueDate)) {
			weiTuoEndTimesText.setText("-");
		} else {
			weiTuoEndTimesText.setText(dueDate);
		}
		if (tag == 1) {
			// 当前有效
			String sellMoney = StringUtil.parseStringCodePattern(sellCode, firstSellAmount, 2);
			String buyMoney = StringUtil.parseStringCodePattern(buyCode, firstBuyAmount, 2);
			sellMoneyText.setText(sellMoney);
			buyMoneyText.setText(buyMoney);
			sAmount = firstSellAmount;
		} else if (tag == 2) {
			if (!StringUtil.isNull(secondStatus) && ConstantGloble.FOREX_STATUS.equals(secondStatus)) {
				String sellMoney = StringUtil.parseStringCodePattern(sellCode, secondSellAmount, 2);
				sellMoneyText.setText(sellMoney);
				sAmount = secondSellAmount;
			} else {
				String sellMoney = StringUtil.parseStringCodePattern(sellCode, firstSellAmount, 2);
				sellMoneyText.setText(sellMoney);
				sAmount = firstSellAmount;
			}
			if (!StringUtil.isNull(secondStatus) && ConstantGloble.FOREX_STATUSS.equals(secondStatus)) {
				String buyMoney = StringUtil.parseStringCodePattern(buyCode, secondBuyAmount, 2);
				buyMoneyText.setText(buyMoney);
			} else {
				String buyMoney = StringUtil.parseStringCodePattern(buyCode, firstBuyAmount, 2);
				buyMoneyText.setText(buyMoney);
			}
		}
		if (exchangeTranType.equals(seven) || exchangeTranType.equals(eight)) {
			loseRateView.setVisibility(View.GONE);
			huoLiRateView.setVisibility(View.GONE);
			zhiSunRateView.setVisibility(View.GONE);
			weiTuoStatusView.setVisibility(View.GONE);
		} else if (exchangeTranType.equals(three)) {
			// 显示获利汇率
			loseRateView.setVisibility(View.GONE);
			huoLiRateView.setVisibility(View.VISIBLE);
			zhiSunRateView.setVisibility(View.GONE);
			weiTuoStatusView.setVisibility(View.GONE);
		} else if (exchangeTranType.equals(four)) {
			// 显示止损汇率
			loseRateView.setVisibility(View.GONE);
			huoLiRateView.setVisibility(View.GONE);
			zhiSunRateView.setVisibility(View.VISIBLE);
			weiTuoStatusView.setVisibility(View.GONE);
		} else if (exchangeTranType.equals(five)) {
			// 显示获利汇率、止损汇率
			loseRateView.setVisibility(View.GONE);
			huoLiRateView.setVisibility(View.VISIBLE);
			zhiSunRateView.setVisibility(View.VISIBLE);
			weiTuoStatusView.setVisibility(View.GONE);
		} else if (exchangeTranType.equals(eleven)) {
			loseRateView.setVisibility(View.VISIBLE);
			huoLiRateView.setVisibility(View.GONE);
			zhiSunRateView.setVisibility(View.GONE);
			weiTuoStatusView.setVisibility(View.GONE);
		}
		// 委托状态
		if (exchangeTranType.equals(three) || exchangeTranType.equals(four)) {
			if (!StringUtil.isNull(firstStatus) && LocalData.isForexQuashStateMap.containsKey(firstStatus)) {
				String status = LocalData.isForexQuashStateMap.get(firstStatus);
				weiTuoStatusText.setText(status);
			}

		} else if (exchangeTranType.equals(five)) {
			if (!StringUtil.isNull(firstStatus) && firstStatus.equals(ConstantGloble.FOREX_STATUSO)) {
				if (!StringUtil.isNull(secondStatus) && LocalData.isForexQuashStateMap.containsKey(secondStatus)) {
					String status = LocalData.isForexQuashStateMap.get(secondStatus);
					weiTuoStatusText.setText(status);
				}
			} else {
				if (!StringUtil.isNull(firstStatus) && LocalData.isForexQuashStateMap.containsKey(firstStatus)) {
					String status = LocalData.isForexQuashStateMap.get(firstStatus);
					weiTuoStatusText.setText(status);
				}
			}
		}
		// 获利汇率
		if (!StringUtil.isNull(firstType) && ConstantGloble.FOREX_STATUSP.equals(firstType)) {
			huiLiRateText.setText(firstCustomerRate);
		} else if (!StringUtil.isNull(secondType) && ConstantGloble.FOREX_STATUSP.equals(secondType)) {
			huiLiRateText.setText(secondCustomerRate);
		}
		// 止损汇率
		if (!StringUtil.isNull(firstType) && ConstantGloble.FOREX_STATUSS.equals(firstType)) {
			zhiSunRateText.setText(firstCustomerRate);
		} else if (!StringUtil.isNull(secondType) && ConstantGloble.FOREX_STATUSS.equals(secondType)) {
			zhiSunRateText.setText(secondCustomerRate);
		}
		// 追击点差
		if (exchangeTranType.equals(eleven)) {
			loseRateText.setText(firstCustomerRate);
		}
	}

	private void initOnClick() {
		quickTrade.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		// 确定按钮
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
		});
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		requestPSNGetTokenId(commConversationId);
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		sAmount = StringUtil.splitStringwith2point(sAmount, 2);
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		requestPsnForexQuashSubmit(accountId, sellCode, buyCode, sAmount, cashRemit, consignNumber, token);
	}

	private void requestPsnForexQuashSubmit(String forexAccountId, String sCurrency, String bCurrency, String sAmount, String cashRemit, String exchangeSeq, String token) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Forex.FOREX_FOREXACCOUNTID_REQ, forexAccountId);
		map.put(Forex.FOREX_SCURRENCY_RES, sCurrency);
		map.put(Forex.FOREX_BCURRENCY_RES, bCurrency);
		map.put(Forex.FOREX_SAMOUNT_RES, sAmount);
		map.put(Forex.FOREX_CASHREMIT1_RES, cashRemit);
		map.put(Forex.FOREX_EXCHANGESEQ_RES, exchangeSeq);
		map.put(Forex.FOREX_TOKEN, token);
		getHttpTools().requestHttp(Forex.FOREX_PSNFOREXQUASHSUBMIT_API, "requestPsnForexQuashSubmitCallback", map, true);
	}

	public void requestPsnForexQuashSubmitCallback(Object resultObj) {
		Map<String, String> result = getHttpTools().getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(result)) {
			return;
		}
		String transactionId = result.get(Forex.FOREX_TRANSACTIONID_TRQ);
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_RESULT_KEY, result);
		Intent intent = new Intent(ForexQuashConfirmActivity.this, ForexQuashSuccessActivity.class);
		intent.putExtra(Forex.FOREX_TRANSACTIONID_TRQ, transactionId);
		intent.putExtra(ConstantGloble.FOREX_TAG, tag);
		if (tag == 1) {
			startActivity(intent);
		} else if (tag == 2) {
			startActivityForResult(intent, ConstantGloble.ISFOREX_WT_ACTIVITY);
		}
		overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ConstantGloble.ISFOREX_WT_ACTIVITY:
			switch (resultCode) {
			case RESULT_OK:
				setResult(RESULT_OK);
				finish();
				break;
			case RESULT_CANCELED:

				break;
			default:
				break;
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.no_animation, R.anim.slide_down_out);
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.TwoTask;
	}
}
