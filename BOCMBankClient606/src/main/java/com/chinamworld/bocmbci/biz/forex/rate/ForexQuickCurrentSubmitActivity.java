package com.chinamworld.bocmbci.biz.forex.rate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.biz.forex.ForexBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 活期快速交易页面 输入相关信息
 * 
 * @author Administrator
 * 
 */
public class ForexQuickCurrentSubmitActivity extends ForexBaseActivity implements OnItemSelectedListener {

	// TODO ------------------------成员变量------------------------------//

	private static final String TAG = "ForexQuickCurrentSubmitActivity";
	/** ForexQuickCurrentSubmitActivity的主布局 */
	private View rateInfoView = null;
	/** 返回按钮 */
	private Button backButton = null;
	/** 存储符合条件的卖出币种信息 */
	List<Map<String, Object>> accList = null;
	/** 交易方式 */
	private Spinner currencyTypeSpinner = null;
	/** 限价汇率 */
	private EditText currencyLimitRateEdit = null;
	/** 限价汇率 */
	private String limitRate = null;
	/** 追击点差 */
	private String loseRate = null;
	/** 卖出币种 */
	private Spinner currencySellCodeSpinner = null;
	/** 可用余额 */
	private TextView currencyBalanceText = null;
	/** 买入币种 */
	private Spinner currencyBuyCodeSpinner = null;
	/** 当前汇率 */
	private TextView currencyRate = null;
	/** 记录用户选择的单选按钮 1-卖出，2—买入 */
	private String radioGroup = "0";
	/** 卖出单选按钮 */
	private RadioButton sellCodeRadioButton = null;
	private EditText sellCodeEdit = null;
	/** 买入单选按钮 */
	private RadioButton buyCodeRadioButton = null;
	private EditText buyCodeEdit = null;
	/** 直接成交按钮 */
	private Button quickTradeButton = null;
	/** 下一步 */
	private Button nextButton = null;
	/** 处理后的卖出币种和钞汇标志 */
	private List<String> sellCodeCashList = null;
	/** 未处理时，卖出币种代码 */
	private List<String> sellCodeList = null;
	/** 处理后的可用金额 */
	private List<String> balanceList = null;
	/** 买入币种信息 */
	// private List<Map<String, String>> buyCodeResultList = null;
	/** 处理前，买入币种代码 */
	private List<String> buyCodeList = null;
	/** 处理后，买入币种名称 */
	private List<String> buyCodeDealList = null;
	/** 限价汇率view */
	private View rateView = null;
	/** 输入的卖出金额 */
	private String sellCodeEditText = null;
	/** 输入的买入金额 */
	private String buyCodeEditText = null;
	/** 选择的卖出币种 */
	private String sellCodeNoDeal = null;
	/** 选择的买入币种 */
	private String buyCodeNoDeal = null;
	/** 交易方式 */
	private String exchangeType = null;
	/** 交易方式名称 */
	private String exchangeTypeName = null;
	/** 用户选择的卖出币种 */
	private String sellCodeSp = null;
	/** 用户选择的买入币种 */
	private String buyCodeSp = null;
	/** 交易金额 */
	private String coluMoney = null;
	/** 卖出币种的钞汇标志 */
	private List<String> cashList = null;
	/** 选择的币种对应的钞汇 */
	private String cashNoDeal = null;

	private String tokenId = null;
	/** 资金账户 */
	private String investAccount = null;
	/** 直接成交查询的rate */
	private String reqRate = null;
	private Intent customerIntent = null;
	/** tag:201--我的外汇 **/
	private int tag = -1;
	private int customerSelectedPosition = -1;
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
	/** 0--买入 */
	private String buyTag = null;
	/** 1--卖出 */
	private String sellTag = null;
	/** 市价即时---08 */
	private String eightName = null;
	/** 限价即时---07 */
	private String sevenName = null;
	/** 03---获利委托 */
	private String threeName = null;
	/** 04--止损委托 */
	private String fourName = null;
	/** 05--二选一委托 */
	private String fiveName = null;
	/** 11--追击止损委托 */
	private String elevenName = null;
	/** 卖出币种发生改变，对应的可用余额也发生改变 */
	private String refreshBalance = null;
	private boolean isInitType = true, isInitSellCode = true, isInitBuyCode = true;
	/** 直接成交还是下一步，1-下一步，2-直接成交 */
	private int nextOrQuickReqRate = 1;
	/** 存储用户的可用余额，没有进行格式化 */
	private String customerMoney = null;
	/** 限价汇率是否可见 1-不可见，2-可见 */
	private int rateViewIsGone = 1;
	/** 委托汇率View */
	private View weiTuoRateView = null;
	/** 截止时间---年月日View */
	private View startTimesView = null;
	private View endTimesView = null;
	/** 获利汇率View */
	private View huoLiRateView = null;
	/** 止损汇率View */
	private View zhiSunRateView = null;
	/** 追击点差View */
	private View loseRateView = null;
	/** 委托汇率 */
	private TextView weiTuoRateText = null;
	/** 年月日 */
	private TextView startTimes = null;
	/** 时 */
	private Spinner endTimesSpinner = null;
	/** 获利汇率 */
	private TextView huoLiRateText = null;
	/** 止损汇率 */
	private TextView zhiSunRateText = null;
	/** 追击点差 */
	private TextView loseRateText = null;
	/** 追击点差范围提示 */
	private TextView loseRateTip = null;
	/** 追击止损委托时可输入的最大点差 */
//	private double maxPendingSet;
	/** 追击止损委托时可输入的最小点差 */
//	private double minPendingSet;
	/** 系统当前时间格式化 年月日 时分秒 */
	// private String currenttime = null;
	// private String currentHours = null;
	/** 年月日 */
	private String startDateStr = null;
	/** 时 */
	private String endDateStr = null;
	/** 委托汇率 */
	private String weiTuoRate = null;
	/** 止损汇率 */
	private String zhiSunRate = null;
	/** 获利汇率 */
	private String huoLiRate = null;
	private View currentRateView = null;
	/** 用户选择的时间 年月日时分秒 */
	private String custonerTimes = null;
	private View messageView = null;

	/** 交易方式标记： 0市价即时 1限价即时 2获利委托 3止损委托 4二选一委托 */
	private int index;
	private TextView leftText = null;
	private TextView maxMoneyText = null;
	private String sourceCurrencyCode = null;
	private String sellRate = null;
	private String buyRate = null;
	/** 已知卖出金额计算买入金额 */
	private String sellJsBuyMoney = null;
	/** 已知买入金额计算卖出金额 */
	private String buyJsSellMoney = null;
	private String nextRate = null;
	/**卖出金额 买入金额 标识*/
	private Boolean isSellOrBuy= true;

	// TODO ------------------------成员变量------------------------------//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		backButton = (Button) findViewById(R.id.ib_back);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		dealDates();
		init();
		canTwoSided = (String) BaseDroidApp.getInstanse().getBizDataMap().get(Forex.FOREX_RATE_CANTWOSIDED_RES);
		if (StringUtil.isNull(canTwoSided)) {
			return;
		}
		dealCustomerOrQuickRequest();
		// 初始化时默认选择卖出按钮，设置卖出最大金额
		setSellMaxMoney();
		// 初始化按钮事件
		initClick();
	}

	private void dealDates() {
		eight = LocalData.forexTradeStyleTransList.get(0);
		seven = LocalData.forexTradeStyleTransList.get(1);
		three = LocalData.forexTradeStyleTransList.get(2);
		four = LocalData.forexTradeStyleTransList.get(3);
		five = LocalData.forexTradeStyleTransList.get(4);
		eleven = LocalData.forexTradeStyleTransList.get(5);
		buyTag = LocalData.forexTradeSellOrBuyList.get(0);
		sellTag = LocalData.forexTradeSellOrBuyList.get(1);
		eightName = LocalData.forexTradeStyleList.get(0);
		sevenName = LocalData.forexTradeStyleList.get(1);
		if (LocalData.forexTradeStyleList.size() > 2) {
			threeName = LocalData.forexTradeStyleList.get(2);
			fourName = LocalData.forexTradeStyleList.get(3);
			fiveName = LocalData.forexTradeStyleList.get(4);
			elevenName = LocalData.forexTradeStyleList.get(5);
		}
	}

	/**
	 * 用于判断是我的外汇发出的请求，还是点击快速交易发出的请求。
	 */
	@SuppressWarnings("unchecked")
	public void dealCustomerOrQuickRequest() {
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		if (StringUtil.isNull(commConversationId)) {
			return;
		}
		customerIntent = getIntent();

		if (customerIntent != null) {
			tag = customerIntent.getIntExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, -1);

		} else {
			return;
		}
		accList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.FOREX_CURR_TRADESELLCODERESULTLIST);
		if (accList == null || accList.size() <= 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexQuickCurrentSubmitActivity.this.getResources().getString(R.string.forex_customer_fix_sell));
			return;
		}
		buyCodeDealList = (List<String>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.FOREX_BUYCODEDEALLIST_KEY);
		buyCodeList = (List<String>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.FOREX_BUYCODELIST_KEY);
		dealAllData();
	}

	/**
	 * 初始化所有的控件
	 */
	private void init() {
		rateInfoView = LayoutInflater.from(ForexQuickCurrentSubmitActivity.this).inflate(R.layout.forex_rate_currency_submit, null);
		tabcontent.addView(rateInfoView);
		setTitle(getResources().getString(R.string.forex_rate_qick1));
		accList = new ArrayList<Map<String, Object>>();
		sellCodeCashList = new ArrayList<String>();
		sellCodeList = new ArrayList<String>();
		balanceList = new ArrayList<String>();
		// buyCodeResultList = new ArrayList<Map<String, String>>();
		cashList = new ArrayList<String>();
		investAccount = (String) BaseDroidApp.getInstanse().getBizDataMap().get(Forex.FOREX_RATE_INVESTACCOUNT_RES);

		currencyTypeSpinner = (Spinner) findViewById(R.id.forex_rate_currency_type);
		rateView = findViewById(R.id.trade_limiRate);
		weiTuoRateView = findViewById(R.id.weiTuoHuiLv_layout);
		huoLiRateView = findViewById(R.id.twoHuoLi_layout);
		zhiSunRateView = findViewById(R.id.two_zhisun_layout);
		loseRateView = findViewById(R.id.loseRate_layout);
		startTimesView = findViewById(R.id.weiTuoTimes1_layout);
		endTimesView = findViewById(R.id.weiTuoTimes2_layout);
		currentRateView = findViewById(R.id.current_rate_layout);
		messageView = findViewById(R.id.message);
		leftText = (TextView) findViewById(R.id.money_left);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, leftText);
		maxMoneyText = (TextView) findViewById(R.id.forex_rate_maxmoney);
		hideView();
		rateView.setVisibility(View.GONE);
		weiTuoRateText = (TextView) findViewById(R.id.forex_wuituo_rate);
		huoLiRateText = (TextView) findViewById(R.id.forex_two_huoli);
		zhiSunRateText = (TextView) findViewById(R.id.forex_two_zhisun);
		loseRateText = (TextView) findViewById(R.id.et_loseRate);
		loseRateTip = (TextView) findViewById(R.id.tv_loseRateTip);
		startTimes = (TextView) findViewById(R.id.forex_query_deal_startdate);
		endTimesSpinner = (Spinner) findViewById(R.id.forex_query_deal_enddate);
		endTimesSpinner.setOnItemSelectedListener(this);
		currencyLimitRateEdit = (EditText) findViewById(R.id.forex_trade_limiRate_Edit);
		currencySellCodeSpinner = (Spinner) findViewById(R.id.forex_rate_currency_sellCode);
		currencySellCodeSpinner.setOnItemSelectedListener(this);
		currencyBalanceText = (TextView) findViewById(R.id.forex_rate_currency_money);
		currencyBuyCodeSpinner = (Spinner) findViewById(R.id.forex_rate_currency_buylCode);
		currencyBuyCodeSpinner.setOnItemSelectedListener(this);
		currencyRate = (TextView) findViewById(R.id.forex_rate_currency_rate);
		sellCodeRadioButton = (RadioButton) findViewById(R.id.forex_trade_sellMoney);
		sellCodeEdit = (EditText) findViewById(R.id.forex_trade_sellmoney_Edit);
		buyCodeRadioButton = (RadioButton) findViewById(R.id.forex_trade_buyMoney);
		buyCodeEdit = (EditText) findViewById(R.id.forex_trade_buymoney_Edit);
		quickTradeButton = (Button) findViewById(R.id.trade_quickButton);
		nextButton = (Button) findViewById(R.id.trade_nextButton);
		nextButton.setVisibility(View.GONE);
		StepTitleUtils.getInstance().initTitldStep(this, new String[] { this.getResources().getString(R.string.forex_rate_tep1), this.getResources().getString(R.string.forex_rate_tep2), this.getResources().getString(R.string.forex_rate_tep3) });
		StepTitleUtils.getInstance().setTitleStep(1);
		// 默认卖出币种按钮被选中
		selectedSellRadioButton();
	}

	/** 隐藏区域 */
	private void hideView() {
		weiTuoRateView.setVisibility(View.GONE);
		huoLiRateView.setVisibility(View.GONE);
		zhiSunRateView.setVisibility(View.GONE);
		startTimesView.setVisibility(View.GONE);
		loseRateView.setVisibility(View.GONE);
		endTimesView.setVisibility(View.GONE);
		currentRateView.setVisibility(View.VISIBLE);
		messageView.setVisibility(View.GONE);
	}

	/**
	 * 买入卖出币种不在调用接口，从上一页面获取 处理买入卖出数据，查询汇率
	 */
	private void dealAllData() {
		getSellCode();
		// 初始化Spinner的默认值
		inintSpinnerData();
		// 请求系统时间
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestSystemDateTime();

	}

	/** 选择小时数 */
	private void getPosition(int number) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, LocalData.forexTimesList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		endTimesSpinner.setAdapter(adapter);
		endTimesSpinner.setSelection(0);
		endDateStr = endTimesSpinner.getSelectedItem().toString().trim();
	}

	/**
	 * 处理卖出币种以及可用余额
	 */
	@SuppressWarnings("unchecked")
	public void getSellCode() {
		int len = accList.size();
		for (int i = 0; i < len; i++) {
			Map<String, Object> accListMap = accList.get(i);

			Map<String, String> currency = (Map<String, String>) accListMap.get(Forex.FOREX_CURRENCY_RES);
			String code = currency.get(Forex.FOREX_CURRENCY_CODE_RES).trim();
			String sellCode = null;
			if (LocalData.Currency.containsKey(code)) {
				sellCode = LocalData.Currency.get(code);
			}

			String cashRemit = (String) accListMap.get(Forex.FOREX_CASHREMIT_RES);
			String cash = null;

			if (LocalData.CurrencyCashremit.containsKey(cashRemit)) {
				cash = LocalData.CurrencyCashremit.get(cashRemit);
			}
			StringBuilder sb = new StringBuilder();
			sb.append(sellCode);
			sb.append(" ");
			sb.append(cash);
			sellCodeCashList.add(sb.toString());
			sellCodeList.add(code);
			cashList.add(cashRemit);
			Map<String, Object> balance = (Map<String, Object>) accListMap.get(Forex.FOREX_BALANCE_RES);
			String availableBalance = (String) balance.get(Forex.FOREX_AVAILABLEBALANCE_RES);
			balanceList.add(availableBalance);

		}
		if (StringUtil.isNullOrEmpty(sellCodeCashList)) {
			return;
		} else {
			if (StringUtil.isNullOrEmpty(balanceList)) {
				return;
			}
		}
	}

	/**
	 * 根据账户类型，交易方式传不同的参数
	 * 
	 * @param isTrue
	 *            外汇账户是否是否可以作外汇二选一
	 * @param haveLoseRate
	 *            是否有追击止损委托
	 */
	private void dealType(String isTrue, boolean haveLoseRate) {
		currencyTypeSpinner.setOnItemSelectedListener(null);
		ArrayAdapter<String> currencyAdapter = null;
		List<String> list= new ArrayList<String>();
		if (ConstantGloble.TRUE.equals(isTrue)) {
			list.addAll(LocalData.forexTradeStyleList);
			currencyAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, list);
		} else {
			// 不可做二选一委托
			list.addAll(LocalData.forexTradeNoTwoList);
			currencyAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, LocalData.forexTradeNoTwoList);
			huoLiRateView.setVisibility(View.GONE);
			zhiSunRateView.setVisibility(View.GONE);
		}
		if (!haveLoseRate) {
			list.remove("追击止损委托");
		} else {
			if (!list.contains("追击止损委托")) {
				list.add("追击止损委托");
			}
		}
		currencyAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, list);
		currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		currencyTypeSpinner.setAdapter(currencyAdapter);
		if (currencyTypeSpinner.getSelectedItemPosition() != 0) {
			currencyTypeSpinner.setSelection(currencyTypeSpinner.getSelectedItemPosition(), true);
		} else {
			currencyTypeSpinner.setSelection(0);
			exchangeType = eight;
		}
		currencyTypeSpinner.setOnItemSelectedListener(this);
	}

	/** 初始化Spinner控件的默认值 */
	public void inintSpinnerData() {
		int position = -1;
		int buyPosition = -1;
		// 交易方式
		dealType(canTwoSided, true);
		if (StringUtil.isNullOrEmpty(sellCodeCashList) || StringUtil.isNullOrEmpty(balanceList) || StringUtil.isNullOrEmpty(cashList) || StringUtil.isNullOrEmpty(buyCodeDealList) || StringUtil.isNullOrEmpty(buyCodeList) || StringUtil.isNullOrEmpty(sellCodeList)) {
			return;
		}

		switch (tag) {
		case ConstantGloble.FOREX_TRADE_QUICK:// 快速交易 -1
			// 卖出Spinner
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, sellCodeCashList);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			currencySellCodeSpinner.setAdapter(adapter);
			initSellCode(0);

			// 买入Spinner
			ArrayAdapter<String> buyAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, buyCodeDealList);
			buyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			currencyBuyCodeSpinner.setAdapter(buyAdapter);
			initBuyCode();
			break;
		case ConstantGloble.FOREX_CUSTOMER_CURRENT_TAG:// 我的外汇 201
			customerSelectedPosition = customerIntent.getIntExtra(ConstantGloble.FOREX_customerSelectedPosition, -1);
			// 卖出Spinner
			ArrayAdapter<String> custonerSellAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, sellCodeCashList);
			custonerSellAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			currencySellCodeSpinner.setAdapter(custonerSellAdapter);
			initSellCode(customerSelectedPosition);

			// 买入Spinner
			ArrayAdapter<String> customerBuyAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, buyCodeDealList);
			customerBuyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			currencyBuyCodeSpinner.setAdapter(customerBuyAdapter);
			initBuyCode();

			break;
		case ConstantGloble.FOREX_TRADE_QUICK_RATEINFO_CURR:// 外汇行情页面 银行买价 401
			position = (Integer) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.FOREX_SELLINLIST_POSITION);
			// 卖出Spinner
			ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.dept_spinner, sellCodeCashList);
			adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			currencySellCodeSpinner.setAdapter(adapter1);
			initSellCode(position);

			// 买入Spinner
			ArrayAdapter<String> buyAdapter1 = new ArrayAdapter<String>(this, R.layout.dept_spinner, buyCodeDealList);
			buyAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			currencyBuyCodeSpinner.setAdapter(buyAdapter1);
			buyPosition = (Integer) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.FOREX_BUYINLIST_POSITION);
			initBuyCode(buyPosition);

			break;
		case ConstantGloble.FOREX_TRADE_QUICK_SELL_CURR:// 外汇行情页面 银行卖价 501
			position = (Integer) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.FOREX_SELLINLIST_POSITION);
			// 卖出Spinner
			ArrayAdapter<String> adapter0 = new ArrayAdapter<String>(this, R.layout.dept_spinner, sellCodeCashList);
			adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			currencySellCodeSpinner.setAdapter(adapter0);
			initSellCode(position);

			// 买入Spinner
			ArrayAdapter<String> buyAdapter0 = new ArrayAdapter<String>(this, R.layout.dept_spinner, buyCodeDealList);
			buyAdapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			currencyBuyCodeSpinner.setAdapter(buyAdapter0);
			buyPosition = (Integer) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.FOREX_BUYINLIST_POSITION);
			initBuyCode(buyPosition);

			break;
		default:
			break;
		}
	}

	/** 初始化卖出币种 */
	private void initSellCode(int position) {
		currencySellCodeSpinner.setSelection(position);
		sellCodeNoDeal = sellCodeList.get(position);
		String b4 = StringUtil.parseStringCodePattern(sellCodeNoDeal, balanceList.get(position), fourNumber);
		customerMoney = balanceList.get(position);
		refreshBalance = balanceList.get(position);
		currencyBalanceText.setText(b4);
		cashNoDeal = cashList.get(position);
		sellCodeSp = currencySellCodeSpinner.getSelectedItem().toString().trim();
		// 初始化的时候默认选中卖出按钮，修改卖出金额的Hint
		updateMoneyHint(sellCodeNoDeal, sellCodeEdit);
	}

	/** 初始化买入币种 */
	private void initBuyCode(int buyPosition) {
		if (buyPosition > -1) {
			// 存在，币种不会相同
			currencyBuyCodeSpinner.setSelection(buyPosition);
			buyCodeNoDeal = buyCodeList.get(buyPosition);
		} else {
			// 不存在
			String buyCode = LocalData.Currency.get(buyCodeList.get(0));
			String sellCode = LocalData.Currency.get(sellCodeNoDeal);
			if (buyCode.equals(sellCode)) {
				// 买卖币种相同
				if (buyCodeList.size() >= 2) {
					currencyBuyCodeSpinner.setSelection(1);
					buyCodeNoDeal = buyCodeList.get(1);
				} else {
					return;
				}

			} else {
				currencyBuyCodeSpinner.setSelection(0);
				buyCodeNoDeal = buyCodeList.get(0);
			}
		}
	}

	private void initBuyCode() {
		String buyCode = LocalData.Currency.get(buyCodeList.get(0));
		String sellCode = LocalData.Currency.get(sellCodeNoDeal);
		if (buyCode.equals(sellCode)) {
			// 买卖币种相同
			if (buyCodeList.size() >= 2) {
				currencyBuyCodeSpinner.setSelection(1);
				buyCodeNoDeal = buyCodeList.get(1);
			} else {
				return;
			}

		} else {
			currencyBuyCodeSpinner.setSelection(0);
			buyCodeNoDeal = buyCodeList.get(0);
		}
	}

	/** 买入请求数据 */
	private void buyRequestDate(int type) {
		switch (type) {
		case ConstantGloble.FOREX_TRADE_SEVEN:// 限价即时
			requestCurrencyPsnForexTrade(investAccount, buyCodeNoDeal, sellCodeNoDeal, radioGroup, null, buyCodeEditText, exchangeType, reqRate, limitRate, cashNoDeal, null, null, null, null, null, tokenId, null, null);
			break;
		case ConstantGloble.FOREX_TRADE_EIGHT:// 市价即时
			requestCurrencyPsnForexTrade(investAccount, buyCodeNoDeal, sellCodeNoDeal, radioGroup, null, buyCodeEditText, exchangeType, reqRate, null, cashNoDeal, null, null, null, null, null, tokenId, null, null);
			break;
		default:
			break;
		}
	}

	/** 卖出请求数据 */
	private void sellRequestDate(int type) {
		switch (type) {
		case ConstantGloble.FOREX_TRADE_SEVEN:// 限价即时
			requestCurrencyPsnForexTrade(investAccount, buyCodeNoDeal, sellCodeNoDeal, radioGroup, sellCodeEditText, null, exchangeType, reqRate, limitRate, cashNoDeal, null, null, null, null, null, tokenId, null, null);
			break;
		case ConstantGloble.FOREX_TRADE_EIGHT:// 市价即时
			requestCurrencyPsnForexTrade(investAccount, buyCodeNoDeal, sellCodeNoDeal, radioGroup, sellCodeEditText, null, exchangeType, reqRate, null, cashNoDeal, null, null, null, null, null, tokenId, null, null);
			break;
		default:
			break;
		}
	}

	/** 点击下一步----进入确认页面 */
	private void gotoNextActivity() {
		Intent intent = new Intent(ForexQuickCurrentSubmitActivity.this, ForexQuickCurrentConfirmActivity.class);
		if (seven.equals(exchangeType)) {// 限价交易
			intent.putExtra(ConstantGloble.FOREX_LIMITRATE_KEY, limitRate);
			intent.putExtra(ConstantGloble.FOREX_CURRENTRATE_KEY, nextRate);
		} else if (three.equals(exchangeType) || four.equals(exchangeType)) {// 获利委托、止损委托
			intent.putExtra(ConstantGloble.FOREX_WEITUORATE_KEY, weiTuoRate);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY1, startDateStr);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY2, endDateStr);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY3, custonerTimes);
		} else if (five.equals(exchangeType)) {// 二选一委托
			intent.putExtra(ConstantGloble.FOREX_HUOLIRATE_KEY, huoLiRate);
			intent.putExtra(ConstantGloble.FOREX_ZHISUNRATE_KEY, zhiSunRate);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY1, startDateStr);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY2, endDateStr);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY3, custonerTimes);
		} else if (eleven.equals(exchangeType)) {
			intent.putExtra(Forex.FOREX_LOSERATE_RES, loseRate);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY1, startDateStr);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY2, endDateStr);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY3, custonerTimes);
		}

		if (sellTag.equals(radioGroup)) {
			// 1-- 卖出
			intent.putExtra(ConstantGloble.FOREX_RADIOBUTTON_KEY, sellTag);
			intent.putExtra(ConstantGloble.FOREX_SELLEDITTEXT_KEY, sellCodeEditText);
			intent.putExtra(ConstantGloble.FOREX_SELLJSBUYMONEY_KEY, sellJsBuyMoney);
		} else if (buyTag.equals(radioGroup)) {
			// 0-- 买入
			intent.putExtra(ConstantGloble.FOREX_RADIOBUTTON_KEY, buyTag);
			intent.putExtra(ConstantGloble.FOREX_COLUMONEY_KEY, coluMoney);
			intent.putExtra(ConstantGloble.FOREX_BUYEDITTEXT_KEY, buyCodeEditText);
			intent.putExtra(ConstantGloble.FOREX_BUYJSSELLMONEY_KEY, buyJsSellMoney);
		}
		intent.putExtra(ConstantGloble.FOREX_CHANGETYPE_KEY, exchangeTypeName);
		intent.putExtra(Forex.FOREX_EXCHANGETYPE_REQ, exchangeType);
		// code+cash
		intent.putExtra(ConstantGloble.FOREX_SELLCODESP_KEY, sellCodeSp);
		// code
		intent.putExtra(ConstantGloble.FOREX_BUYCODESP_KEY, buyCodeSp);
		intent.putExtra(ConstantGloble.FOREX_SELLCODENODEAL_KEY, sellCodeNoDeal);
		intent.putExtra(ConstantGloble.FOREX_BUYCODENODEAL_KEY, buyCodeNoDeal);
		intent.putExtra(ConstantGloble.FOREX_CASHNODEAL_KEY, cashNoDeal);
		startActivity(intent);
	}

	/** 卖出金额的最大值 */
	private void setSellMaxMoney() {
		maxMoneyText.setText("");
		String sellMaxMoney = currencyBalanceText.getText().toString().trim();
		maxMoneyText.setText(sellMaxMoney);
	}

	/**
	 * 买入金额的最大值
	 * 
	 * @param sourceCurrencyCode
	 *            :源货币
	 * @param sellRate
	 *            ：银行卖价
	 * @param buyRate
	 *            ：银行买价
	 */
	private void setBuyMaxMoney() {
		try {
			maxMoneyText.setText("");
			float balance = Float.valueOf(refreshBalance);
			String buyMoney = null;
			if (sourceCurrencyCode.equals(buyCodeNoDeal)) {
				// 买入币种是首货币 ------持有币种/银行卖价
				if (!StringUtil.isNull(sellRate)) {
					float sRate = Float.valueOf(sellRate);
					float buy = balance / sRate;
					buyMoney = String.valueOf(buy);
					buyMoney = StringUtil.parseStringCodePattern(buyCodeNoDeal, buyMoney, 2);
				}
			} else {
				// 买入币种不是首货币------持有币种*银行买价
				if (!StringUtil.isNull(buyRate)) {
					float bRate = Float.valueOf(buyRate);
					float buy = balance * bRate;
					buyMoney = String.valueOf(buy);
					buyMoney = StringUtil.parseStringCodePattern(buyCodeNoDeal, buyMoney, 2);
				}
			}
			maxMoneyText.setText(buyMoney);
		} catch (Exception ex) {
			LogGloble.e(TAG, ex.getMessage(), ex);
			maxMoneyText.setText("-");
		}
	}

	/** 计算买入金额、卖出金额 */
	private void colMoney() {
		float rate = Float.valueOf(nextRate);
		if (sellCodeRadioButton.isChecked()) {
			float sellMoney = Float.valueOf(sellCodeEditText);
			float sellBuyMoney = 0;
			// 计算买入金额
			if (sourceCurrencyCode.equals(buyCodeNoDeal)) {
				// 买入首货币
				sellBuyMoney = sellMoney / rate;
			} else {
				// 买入非首货币
				sellBuyMoney = sellMoney * rate;
			}
			sellJsBuyMoney = String.valueOf(sellBuyMoney);
			sellJsBuyMoney = StringUtil.parseStringCodePattern(buyCodeNoDeal, sellJsBuyMoney, 2);
		} else if (buyCodeRadioButton.isChecked()) {
			float buyMoney = Float.valueOf(buyCodeEditText);
			float buySellMoney = 0;
			// 计算买入金额
			if (sourceCurrencyCode.equals(buyCodeNoDeal)) {
				// 买入首货币
				buySellMoney = buyMoney * rate;
			} else {
				// 买入非首货币
				buySellMoney = buyMoney / rate;
			}
			buyJsSellMoney = String.valueOf(buySellMoney);
			buyJsSellMoney = StringUtil.parseStringCodePattern(sellCodeNoDeal, buyJsSellMoney, 2);
		}
	}

	/** 验证汇率 */
	private void checkRate(int type) {
		switch (type) {
		case ConstantGloble.FOREX_TRADE_EIGHT:// 市价
			if (sellCodeRadioButton.isChecked()) {
				checkSellMoney();
			} else if (buyCodeRadioButton.isChecked()) {
				checkBuyMoney();
			}
			break;
		case ConstantGloble.FOREX_TRADE_SEVEN:// 限价7
			chechSellLimitRate();
			break;
		case ConstantGloble.FOREX_TRADE_THREE:// 获利委托3
			checkWeiTuoRate();
			break;
		case ConstantGloble.FOREX_TRADE_FOUR:// 止损委托4
			checkWeiTuoRate();
			break;
		case ConstantGloble.FOREX_TRADE_FIVE:// 二选一委托5
			checkTwoRate();
			break;
		case ConstantGloble.FOREX_TRADE_ELEVEN:// 追击止损委托11
			loseRate = loseRateText.getText().toString().trim();
//			double loseRateDouble = Double.parseDouble(loseRate);
//			if ((loseRateDouble < minPendingSet) || (loseRateDouble > maxPendingSet)) {
//				BaseDroidApp.getInstanse().showInfoMessageDialog("");
//			}

			if (StringUtil.isNullOrEmpty(loseRate)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.forex_buy_pursuitOfSpread_null_error));
				return;
			}
			if (Double.parseDouble(loseRate) == 0.0) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.forex_buy_pursuitOfSpread_zero_error));
				return;
			}
			checkTimes();
			break;
		default:
			break;
		}
	}

	/** 验证限价汇率 */
	private void chechSellLimitRate() {
		limitRate = currencyLimitRateEdit.getText().toString().trim();
		// 限价交易
		// 买入币种或卖出币种同时为日元、港元时小数点4位特殊的货币对，港元/日元、日元/港元，小数点后为四位
		if (spetialCodeList.contains(sellCodeNoDeal) && spetialCodeList.contains(buyCodeNoDeal)) {
			RegexpBean reb1 = new RegexpBean(rateEmg, limitRate, "spetialRate", true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb1);
			if (RegexpUtils.regexpDate(lists)) {
				if (sellCodeRadioButton.isChecked()) {
					checkSellMoney();
				} else if (buyCodeRadioButton.isChecked()) {
					checkBuyMoney();
				}

			} else {
				return;
			}
			return;
		} else {
			if (tradeCheckCodeNoNumber.contains(sellCodeNoDeal) || tradeCheckCodeNoNumber.contains(buyCodeNoDeal)) {
				RegexpBean reb1 = new RegexpBean(rateEmg, limitRate, "spetialLimitRate", true);
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(reb1);
				if (RegexpUtils.regexpDate(lists)) {
					if (sellCodeRadioButton.isChecked()) {
						checkSellMoney();
					} else if (buyCodeRadioButton.isChecked()) {
						checkBuyMoney();
					}
				} else {
					return;
				}
				return;
			} else {
				// 一般币种验证方式
				RegexpBean reb1 = new RegexpBean(rateEmg, limitRate, "limitRate", true);
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(reb1);
				if (RegexpUtils.regexpDate(lists)) {
					if (sellCodeRadioButton.isChecked()) {
						checkSellMoney();
					} else if (buyCodeRadioButton.isChecked()) {
						checkBuyMoney();
					}
				} else {
					return;
				}
				return;
			}
		}
	}

	/** 获利委托、止损委托----------委托汇率 */
	private void checkWeiTuoRate() {
		weiTuoRate = weiTuoRateText.getText().toString().trim();
		String text = getResources().getString(R.string.forex_trade_zhiSun_weituo1);// 委托汇率
		// 买入币种或卖出币种同时为日元、港元时小数点4位特殊的货币对，港元/日元、日元/港元，小数点后为四位
		if (spetialCodeList.contains(sellCodeNoDeal) && spetialCodeList.contains(buyCodeNoDeal)) {
			RegexpBean reb1 = new RegexpBean(text, weiTuoRate, "spetialRate", true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb1);
			if (RegexpUtils.regexpDate(lists)) {
				checkTimes();
			} else {
				return;
			}
			return;
		} else {
			if (tradeCheckCodeNoNumber.contains(sellCodeNoDeal) || tradeCheckCodeNoNumber.contains(buyCodeNoDeal)) {
				RegexpBean reb1 = new RegexpBean(text, weiTuoRate, "spetialLimitRate", true);
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(reb1);
				if (RegexpUtils.regexpDate(lists)) {
					checkTimes();
				} else {
					return;
				}
				return;
			} else {
				// 一般币种验证方式
				RegexpBean reb1 = new RegexpBean(text, weiTuoRate, "limitRate", true);
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(reb1);
				if (RegexpUtils.regexpDate(lists)) {
					checkTimes();
				} else {
					return;
				}
				return;
			}
		}
	}

	/** 二选一委托-----获利汇率、止损汇率 */
	private void checkTwoRate() {
		huoLiRate = huoLiRateText.getText().toString().trim();
		zhiSunRate = zhiSunRateText.getText().toString().trim();
		String text1 = getResources().getString(R.string.forex_trade_two_huoli1);// 获利汇率
		String text2 = getResources().getString(R.string.forex_trade_two_zhisun1);// 止损汇率
		// 买入币种或卖出币种同时为日元、港元时小数点4位特殊的货币对，港元/日元、日元/港元，小数点后为四位
		if (spetialCodeList.contains(sellCodeNoDeal) && spetialCodeList.contains(buyCodeNoDeal)) {
			RegexpBean reb1 = new RegexpBean(text1, huoLiRate, "spetialRate", true);
			RegexpBean reb2 = new RegexpBean(text2, zhiSunRate, "spetialRate", true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb1);
			lists.add(reb2);
			if (RegexpUtils.regexpDate(lists)) {
				checkTimes();
			} else {
				return;
			}
			return;
		}
		if (tradeCheckCodeNoNumber.contains(sellCodeNoDeal) || tradeCheckCodeNoNumber.contains(buyCodeNoDeal)) {
			RegexpBean reb1 = new RegexpBean(text1, huoLiRate, "spetialLimitRate", true);
			RegexpBean reb2 = new RegexpBean(text2, zhiSunRate, "spetialLimitRate", true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb1);
			lists.add(reb2);
			if (RegexpUtils.regexpDate(lists)) {
				checkTimes();
			} else {
				return;
			}
			return;
		} else {
			// 一般币种验证方式
			RegexpBean reb1 = new RegexpBean(text1, huoLiRate, "limitRate", true);
			RegexpBean reb2 = new RegexpBean(text2, zhiSunRate, "limitRate", true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb1);
			lists.add(reb2);
			if (RegexpUtils.regexpDate(lists)) {
				checkTimes();
			} else {
				return;
			}
			return;
		}
	}

	/** 验证时间 */
	private void checkTimes() {
		StringBuilder sb = new StringBuilder(startDateStr);
		sb.append(" ");
		sb.append(endDateStr);
		sb.append(":00:00");
		custonerTimes = sb.toString().trim();
		if (sellCodeRadioButton.isChecked()) {
			checkSellMoney();
		} else if (buyCodeRadioButton.isChecked()) {
			checkBuyMoney();
		}
	}

	/** 验证卖出金额 */
	private void checkSellMoney() {
		// 卖出金额
		sellCodeEditText = sellCodeEdit.getText().toString().trim();
		// 卖出时，无论买入币种、卖出币种含有特殊币种，限价汇率2位
		if (tradeCheckCodeNoNumber.contains(sellCodeNoDeal)) {
			// 特殊币种验证方式spetialLimitRate----limitRate
			RegexpBean reb = new RegexpBean(getResources().getString(R.string.forex_currency_sell), sellCodeEditText, "spetialAmount", true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb);
			if (RegexpUtils.regexpDate(lists)) {
				lastCheckAndReq();
			} else {
				return;
			}
			return;
		} else {
			// 一般币种验证方式
			RegexpBean reb = new RegexpBean(getResources().getString(R.string.forex_currency_sell), sellCodeEditText, "amount", true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb);
			if (RegexpUtils.regexpDate(lists)) {
				lastCheckAndReq();
			} else {
				return;
			}
			return;
		}
	}

	/** 验证买入金额 */
	private void checkBuyMoney() {
		buyCodeEditText = buyCodeEdit.getText().toString().trim();
		// 日元、韩元没有小数点spetialLimitRate-----limitRate
		if (tradeCheckCodeNoNumber.contains(buyCodeNoDeal)) {
			RegexpBean reb = new RegexpBean(getResources().getString(R.string.forex_currency_buy), buyCodeEditText, "spetialAmount", true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb);
			if (RegexpUtils.regexpDate(lists)) {
				buyLastCheckAndReq();
			} else {
				return;
			}

		} else {
			RegexpBean reb = new RegexpBean(getResources().getString(R.string.forex_currency_buy), buyCodeEditText, "amount", true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb);
			if (RegexpUtils.regexpDate(lists)) {
				buyLastCheckAndReq();
			} else {
				return;
			}

		}
	}

	/** 卖出金额是否大于可用余额 */
	private void lastCheckAndReq() {
		// 卖出金额
		double moneyEdit = Double.valueOf(sellCodeEditText);
		double balance = Double.valueOf(customerMoney);
		if (moneyEdit > balance) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexQuickCurrentSubmitActivity.this.getString(R.string.forex_currency_info));
			return;
		} else {
			String buyCodeNoDealName = LocalData.Currency.get(buyCodeNoDeal);
			String sellCodeNoDealName = LocalData.Currency.get(sellCodeNoDeal);
			if (buyCodeNoDealName.equals(sellCodeNoDealName)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(ForexQuickCurrentSubmitActivity.this.getString(R.string.forex_trade_sellBuyCode));
				return;
			} else {
				if (StringUtil.isNullOrEmpty(exchangeType) || StringUtil.isNullOrEmpty(buyCodeNoDeal) || StringUtil.isNullOrEmpty(sellCodeNoDeal)) {
					return;

				}
				if (nextOrQuickReqRate == 2) {
					// 直接成交
					requestCommConversationId();
					BaseHttpEngine.showProgressDialog();
				} else {
					// 下一步
					isRequest();
				}
			}
		}
	}

	/** 点击下一步的判断 */
	private void isRequest() {
		if (exchangeType.equals(seven)) {
			tradePsnForexTradePre(exchangeType, buyCodeNoDeal, sellCodeNoDeal);
		} else {
			tradePsnForexTradePre(eight, buyCodeNoDeal, sellCodeNoDeal);
		}
	}

	/** 买入金额币种 */
	private void buyLastCheckAndReq() {
		coluMoney = buyCodeEditText;
		String buyCodeNoDealName = LocalData.Currency.get(buyCodeNoDeal);
		String sellCodeNoDealName = LocalData.Currency.get(sellCodeNoDeal);
		if (buyCodeNoDealName.equals(sellCodeNoDealName)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexQuickCurrentSubmitActivity.this.getString(R.string.forex_trade_sellBuyCode));
			return;
		} else {
			if (StringUtil.isNullOrEmpty(exchangeType) || StringUtil.isNullOrEmpty(buyCodeNoDeal) || StringUtil.isNullOrEmpty(sellCodeNoDeal)) {
				return;

			}
			if (nextOrQuickReqRate == 2) {
				// 直接成交
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();
			} else {
				// 下一步
				isRequest();
			}
		}
	}

	/**
	 * 根据选择的交易方式下标控制视图
	 * 
	 * @param position
	 */
	private void choiceExchangeType(int position) {
		switch (position) {
		case 0:// 市价即时
			buyCodeRadioButton.setEnabled(true);
			buyCodeEdit.setEnabled(true);
			rateView.setVisibility(View.GONE);
			rateViewIsGone = 1;
			quickTradeButton.setVisibility(View.VISIBLE);
			exchangeType = eight;
			exchangeTypeName = eightName;
			// 市价即时不需要下一步按钮
			nextButton.setVisibility(View.GONE);
			hideView();
			break;
		case 1:// 限价即时
			buyCodeRadioButton.setEnabled(true);
			buyCodeEdit.setEnabled(true);
			hideView();
			rateView.setVisibility(View.VISIBLE);
			rateViewIsGone = 2;
			quickTradeButton.setVisibility(View.GONE);
			nextButton.setVisibility(View.VISIBLE);
			exchangeType = seven;
			exchangeTypeName = sevenName;
			currencyLimitRateEdit.requestFocus();
			limitTextHint();
			break;
		case 2:// 获利委托
			rateView.setVisibility(View.GONE);
			rateViewIsGone = 1;
			showPartView();
			setTimes(startDateStr, endDateStr);
			exchangeType = three;
			exchangeTypeName = threeName;
			limitTextHint();
			break;
		case 3:// 止损委托
			rateView.setVisibility(View.GONE);
			rateViewIsGone = 1;
			showPartView();
			setTimes(startDateStr, endDateStr);
			exchangeType = four;
			exchangeTypeName = fourName;
			limitTextHint();
			break;
		case 4:// 二选一委托
			rateView.setVisibility(View.GONE);
			rateViewIsGone = 1;
			showPartsView();
			setTimes(startDateStr, endDateStr);
			exchangeType = five;
			exchangeTypeName = fiveName;
			limitTextHint();
			break;
		case 5:// 追击止损委托
			rateView.setVisibility(View.GONE);
			rateViewIsGone = 1;
			showloseRateView();
			setTimes(startDateStr, endDateStr);
			exchangeType = eleven;
			exchangeTypeName = elevenName;
			break;
		default:
			break;
		}
	}

	/** 设置委托截止时间 */
	private void setTimes(String currenttime, String endDateStr) {
		startTimes.setText(currenttime);
	}

	/** 止损委托---获利委托--使用 */
	private void showPartView() {
		weiTuoRateText.requestFocus();
		weiTuoRateView.setVisibility(View.VISIBLE);
		startTimesView.setVisibility(View.VISIBLE);
		loseRateView.setVisibility(View.GONE);
		endTimesView.setVisibility(View.VISIBLE);
		quickTradeButton.setVisibility(View.GONE);
		nextButton.setVisibility(View.VISIBLE);
		huoLiRateView.setVisibility(View.GONE);
		zhiSunRateView.setVisibility(View.GONE);
		currentRateView.setVisibility(View.GONE);
		messageView.setVisibility(View.VISIBLE);
		buyCodeRadioButton.setEnabled(true);
		buyCodeEdit.setEnabled(true);
	}

	/** 二选一委托--使用 */
	private void showPartsView() {
		huoLiRateText.requestFocus();
		weiTuoRateView.setVisibility(View.GONE);
		startTimesView.setVisibility(View.VISIBLE);
		loseRateView.setVisibility(View.GONE);
		endTimesView.setVisibility(View.VISIBLE);
		quickTradeButton.setVisibility(View.GONE);
		nextButton.setVisibility(View.VISIBLE);
		huoLiRateView.setVisibility(View.VISIBLE);
		zhiSunRateView.setVisibility(View.VISIBLE);
		currentRateView.setVisibility(View.GONE);
		messageView.setVisibility(View.VISIBLE);
		buyCodeRadioButton.setEnabled(true);
		buyCodeEdit.setEnabled(true);
	}

	/** 追击止损委托--使用 */
	private void showloseRateView() {
		// huoLiRateText.requestFocus();
		weiTuoRateView.setVisibility(View.GONE);
		startTimesView.setVisibility(View.VISIBLE);
		loseRateView.setVisibility(View.VISIBLE);
		endTimesView.setVisibility(View.VISIBLE);
		quickTradeButton.setVisibility(View.GONE);
		nextButton.setVisibility(View.VISIBLE);
		huoLiRateView.setVisibility(View.GONE);
		zhiSunRateView.setVisibility(View.GONE);
		currentRateView.setVisibility(View.GONE);
		messageView.setVisibility(View.VISIBLE);
		sellCodeRadioButton.setSelected(true);
		buyCodeRadioButton.setSelected(false);
		buyCodeRadioButton.setEnabled(false);
		buyCodeEdit.setEnabled(false);
	}

	/** 清空汇率数据 */
	private void clearRate() {
		currencyLimitRateEdit.setText("");
		weiTuoRateText.setText("");
		huoLiRateText.setText("");
		if (index == 4)// 二选一委托时数据清空时 设置获利委托输入框获得焦点
			huoLiRateText.requestFocus();
		zhiSunRateText.setText("");
		currencyRate.setText("");
	}

	/** 动态修改限价汇率的hint */
	private void limitTextHint() {
		String spText = getResources().getString(R.string.forex_rate_currency_limitRate_info1);
		String text = getResources().getString(R.string.forex_rate_currency_limitRate_info);
		if (spetialCodeList.contains(buyCodeNoDeal) && spetialCodeList.contains(sellCodeNoDeal)) {
			currencyLimitRateEdit.setHint(text);
			weiTuoRateText.setHint(text);
			huoLiRateText.setHint(text);
			zhiSunRateText.setHint(text);
			return;
		} else {
			// 卖出
			if (tradeCheckCodeNoNumber.contains(sellCodeNoDeal) || tradeCheckCodeNoNumber.contains(buyCodeNoDeal)) {
				// 特殊币种
				currencyLimitRateEdit.setHint(spText);
				weiTuoRateText.setHint(spText);
				huoLiRateText.setHint(spText);
				zhiSunRateText.setHint(spText);

			} else {
				currencyLimitRateEdit.setHint(text);
				weiTuoRateText.setHint(text);
				huoLiRateText.setHint(text);
				zhiSunRateText.setHint(text);
			}
			return;
		}
	}

	/**
	 * 动态修改买入金额、卖出金额的hint值 code :币种代码 view：输入框
	 */
	private void updateMoneyHint(String code, EditText view) {
		String text = getResources().getString(R.string.forex_rate_currency_number);
		String noText = getResources().getString(R.string.forex_rate_currency_no_number);
		if (StringUtil.isNull(code)) {
			view.setHint(text);
		}
		if (tradeCheckCodeNoNumber.contains(code)) {
			view.setHint(noText);
		} else {
			view.setHint(text);
		}
	}

	// TODO ------------------------控件事件------------------------------//

	/**
	 * 初始化所有的OnClickListener
	 */
	public void initClick() {
		sellCodeRadioButton.setOnClickListener(sellCodeRadioButtonListener);
		buyCodeRadioButton.setOnClickListener(buyCodeRadioButtonListener);
		// 直接成交
		quickTradeButton.setOnClickListener(quickTradeListener);
		// 下一步
		nextButton.setOnClickListener(nextListener);
		startTimes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Calendar c = Calendar.getInstance(); // 实例化一个日期与时间操作对象
				QueryDateUtils.checkDate(startTimes, c);
				// 第二个参数为用户选择设置按钮后的响应事件
				// 最后的三个参数为缺省显示的年度，月份，及日期信息 DatePickerDialog
				DatePickerDialog dialog = new DatePickerDialog(ForexQuickCurrentSubmitActivity.this, new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						// ** 选择的截止日期 *//*
						StringBuilder date = new StringBuilder();
						date.append(String.valueOf(year));
						date.append("/");
						int month = monthOfYear + 1;
						date.append(((month < 10) ? ("0" + month) : (month + "")));
						date.append("/");
						date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth) : (dayOfMonth + "")));
						startDateStr = date.toString().trim();
						startTimes.setText(startDateStr);
					}
				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
				dialog.show();
			}
		});
	}

	// 卖出单选按钮监听事件
	private OnClickListener sellCodeRadioButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
//			if (sellCodeRadioButton.isChecked()) {
//				return
//			}
			if(isSellOrBuy){
				return;
			}
			String sell = getResources().getString(R.string.forex_sell_max);
			leftText.setText("");
			leftText.setText(sell);
			selectedSellRadioButton();
			// 修改卖出金额的Hint
			updateMoneyHint(sellCodeNoDeal, sellCodeEdit);
			// 设置卖出币种的最大值
			setSellMaxMoney();
		}
	};

	/** 选择卖出单选按钮 */
	private void selectedSellRadioButton() {
		dealType(canTwoSided, true);
		LogGloble.d("info", "selectedSellRadioButton");
		sellCodeRadioButton.setChecked(true);
		buyCodeRadioButton.setChecked(false);
		sellCodeEdit.setEnabled(true);
		sellCodeEdit.requestFocus();
		sellCodeEdit.setFocusable(true);
		sellCodeEdit.setFocusableInTouchMode(true);
		buyCodeEdit.setFocusable(false);
		buyCodeEdit.setEnabled(false);
		buyCodeEdit.setText("");
		radioGroup = "1";
		isSellOrBuy = true;
		switch (rateViewIsGone) {
		case 2:// 限价汇率可见
			limitTextHint();
			break;
		default:
			break;
		}
	}

	private OnClickListener buyCodeRadioButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
//			if (buyCodeRadioButton.isChecked()) {
//				return;
//			}
			if(!isSellOrBuy){
				return;
			}
			String buy = getResources().getString(R.string.forex_buy_max);
			leftText.setText("");
			leftText.setText(buy);
			selectedBuyRadioButton();
			// 修改买入金额的Hint
			updateMoneyHint(buyCodeNoDeal, buyCodeEdit);
			// 设置买入最大金额的最大值
			setBuyMaxMoney();
		}
	};

	/** 选择买入单选按钮 */
	private void selectedBuyRadioButton() {
		dealType(canTwoSided, false);
		buyCodeRadioButton.setChecked(true);
		sellCodeRadioButton.setChecked(false);
		sellCodeEdit.setEnabled(false);
		sellCodeEdit.setText("");
		buyCodeEdit.setEnabled(true);
		buyCodeEdit.requestFocus();
		buyCodeEdit.setFocusable(true);
		buyCodeEdit.setFocusableInTouchMode(true);
		sellCodeEdit.setFocusable(false);
		radioGroup = "0";
		isSellOrBuy = false;
		switch (rateViewIsGone) {
		case 2:// 限价汇率可见
			limitTextHint();
			break;
		default:
			break;
		}
	}

	private OnClickListener quickTradeListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			nextOrQuickReqRate = 2;
			int tt = Integer.valueOf(exchangeType);
			checkRate(tt);
		}

	};

	// 下一步
	OnClickListener nextListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			nextOrQuickReqRate = 1;
			int tt = Integer.valueOf(exchangeType);
			checkRate(tt);
		}
	};

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		String sellCodeName = LocalData.Currency.get(sellCodeNoDeal);
		String buyCodeName = LocalData.Currency.get(buyCodeNoDeal);
		switch (parent.getId()) {
		case R.id.forex_rate_currency_type:// 交易方式
			// 选择交易方式
			choiceExchangeType(position);
			index = position;// 标记交易方式
			if (sellCodeName.equals(buyCodeName)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(ForexQuickCurrentSubmitActivity.this.getString(R.string.forex_trade_sellBuyCode));
				currencyRate.setText("");
				return;
			}
			if (StringUtil.isNullOrEmpty(exchangeType) || StringUtil.isNullOrEmpty(buyCodeNoDeal) || StringUtil.isNullOrEmpty(sellCodeNoDeal)) {
				return;
			}

			if (!isInitType) {
				clearRate();
				if (exchangeType.equals(seven) || exchangeType.equals(eight)) {
					currencyRate.setText("");
					currentRateView.setVisibility(View.VISIBLE);
					// 查询汇率
					BaseHttpEngine.showProgressDialog();
					requestPsnForexTradePre(exchangeType, buyCodeNoDeal, sellCodeNoDeal);
				} else {
					BaseHttpEngine.showProgressDialog();
					requestPsnForexTradePre(eight, buyCodeNoDeal, sellCodeNoDeal);
					currentRateView.setVisibility(View.VISIBLE);
				}

			} else {
				isInitType = false;
			}
			break;
		case R.id.forex_rate_currency_sellCode:// 卖出币种
			maxMoneyText.setText("");
			sellCodeSp = currencySellCodeSpinner.getSelectedItem().toString().trim();
			sellCodeNoDeal = sellCodeList.get(position);
			cashNoDeal = cashList.get(position);
			refreshBalance = balanceList.get(position);
			customerMoney = refreshBalance;
			// 可用余额的值也发生改变
			String bb = refreshBalance;
			if (LocalData.codeNoNumber.contains(sellCodeNoDeal)) {
				bb = StringUtil.parseStringPattern(bb, twoNumber);
			} else {
				bb = StringUtil.parseStringPattern(bb, fourNumber);
			}
			currencyBalanceText.setText(bb);
			if (sellCodeRadioButton.isChecked()) {
				// 设置卖出金额的最大值
				setSellMaxMoney();
			} else if (buyCodeRadioButton.isChecked()) {

			}
			limitTextHint();
			if (sellCodeRadioButton.isChecked()) {
				// 修改卖出金额的Hint
				updateMoneyHint(sellCodeNoDeal, sellCodeEdit);
			}
			String sellCodeName1 = LocalData.Currency.get(sellCodeNoDeal);
			String buyCodeName1 = LocalData.Currency.get(buyCodeNoDeal);
			if (sellCodeName1.equals(buyCodeName1)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(ForexQuickCurrentSubmitActivity.this.getString(R.string.forex_trade_sellBuyCode));
				currencyRate.setText("");
				return;
			}
			if (StringUtil.isNullOrEmpty(exchangeType) || StringUtil.isNullOrEmpty(buyCodeNoDeal) || StringUtil.isNullOrEmpty(sellCodeNoDeal)) {
				return;
			}

			if (!isInitSellCode) {
				clearRate();
				if (exchangeType.equals(seven) || exchangeType.equals(eight)) {
					// 查询汇率
					BaseHttpEngine.showProgressDialog();
					currentRateView.setVisibility(View.VISIBLE);
					requestPsnForexTradePre(exchangeType, buyCodeNoDeal, sellCodeNoDeal);
				} else {
					BaseHttpEngine.showProgressDialog();
					currentRateView.setVisibility(View.VISIBLE);
					requestPsnForexTradePre(eight, buyCodeNoDeal, sellCodeNoDeal);
				}
			} else {
				isInitSellCode = false;
			}

			break;
		case R.id.forex_rate_currency_buylCode:// 买入币种
			maxMoneyText.setText("");
			buyCodeSp = currencyBuyCodeSpinner.getSelectedItem().toString().trim();
			buyCodeNoDeal = buyCodeList.get(position);
			// 修改买入金额的Hint
			updateMoneyHint(buyCodeNoDeal, buyCodeEdit);
			limitTextHint();
			if (buyCodeRadioButton.isChecked()) {
				// 修改买入金额的Hint
				updateMoneyHint(buyCodeNoDeal, buyCodeEdit);
			}

			String sellCodeName2 = LocalData.Currency.get(sellCodeNoDeal);
			String buyCodeName2 = LocalData.Currency.get(buyCodeNoDeal);
			if (sellCodeName2.equals(buyCodeName2)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(ForexQuickCurrentSubmitActivity.this.getString(R.string.forex_trade_sellBuyCode));
				currencyRate.setText("");
				return;
			} else {
				if (StringUtil.isNullOrEmpty(exchangeType) || StringUtil.isNullOrEmpty(buyCodeNoDeal) || StringUtil.isNullOrEmpty(sellCodeNoDeal)) {
					return;
				}

				if (!isInitBuyCode) {
					clearRate();
					if (exchangeType.equals(seven) || exchangeType.equals(eight)) {
						currentRateView.setVisibility(View.VISIBLE);
						// 查询汇率
						BaseHttpEngine.showProgressDialog();
						requestPsnForexTradePre(exchangeType, buyCodeNoDeal, sellCodeNoDeal);
					} else {
						BaseHttpEngine.showProgressDialog();
						currentRateView.setVisibility(View.VISIBLE);
						requestPsnForexTradePre(eight, buyCodeNoDeal, sellCodeNoDeal);
					}
				} else {
					isInitBuyCode = false;
				}
			}
			break;
		case R.id.forex_query_deal_enddate:// 小时
			endDateStr = endTimesSpinner.getSelectedItem().toString().trim();
			break;
		default:
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {}

	// TODO ------------------------网络请求------------------------------//

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		// BaseHttpEngine.canGoBack = false;
		// currenttime = dateTime;
		startDateStr = QueryDateUtils.getcurrentDate(dateTime);
		// int hours = Integer.valueOf(QueryDateUtils.getHours(dateTime));
		getPosition(0);
		if (StringUtil.isNull(eight) || StringUtil.isNull(buyCodeNoDeal) || StringUtil.isNull(sellCodeNoDeal)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			requestPsnForexTradePre(eight, buyCodeNoDeal, sellCodeNoDeal);
		}
	}

	/**
	 * @快速交易、我的外汇、外汇行情页面 公用 为currencyRate赋值
	 * @查询当前汇率
	 */
	private void requestPsnForexTradePre(String exchangeType, String bCurrency, String sCurrency) {
		Map<String, Object> map = new Hashtable<String, Object>();
		map.put(Forex.FOREX_EXCHANGETYPE_REQ, exchangeType);
		map.put(Forex.FOREX_BCURRENC_REQ, bCurrency);
		map.put(Forex.FOREX_SCURRENCY_REQ, sCurrency);
		getHttpTools().requestHttp(Forex.FOREX_PSNFOREXTREDEPRE_API, "requestPsnForexTradePreCallback", map, true);
	}

	/**
	 * @快速交易、我的外汇、外汇行情页面 公用--回调 为currencyRate赋值
	 * @param resultObj
	 */
	public void requestPsnForexTradePreCallback(Object resultObj) {
		String rate = getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNull(rate)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			currencyRate.setText(rate);
			// 查询单笔外汇行情----首货币
			requestPsnForexQuerySingleRate(buyCodeNoDeal, sellCodeNoDeal);
		}
	}

	/***
	 * 外汇买卖即时交易预交易 查询rate 下一步
	 */
	private void tradePsnForexTradePre(String exchangeType, String bCurrency, String sCurrency) {
		BaseHttpEngine.showProgressDialog();
		Map<String, Object> map = new Hashtable<String, Object>();
		map.put(Forex.FOREX_EXCHANGETYPE_REQ, exchangeType);
		map.put(Forex.FOREX_BCURRENC_REQ, bCurrency);
		map.put(Forex.FOREX_SCURRENCY_REQ, sCurrency);
		getHttpTools().requestHttp(Forex.FOREX_PSNFOREXTREDEPRE_API, "tradePsnForexTradePreCallback", map, true);
	}

	/**
	 * 外汇买卖即时交易预交易---回调 查询rate
	 * 
	 * @param resultObj
	 */
	public void tradePsnForexTradePreCallback(Object resultObj) {
		nextRate = getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNull(nextRate)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			colMoney();
			gotoNextActivity();
			BaseHttpEngine.dissMissProgressDialog();
		}
	}

	/**
	 * 直接成交---请求ConversationId
	 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		if (StringUtil.isNull(commConversationId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			if (StringUtil.isNull(exchangeType) || StringUtil.isNull(buyCodeNoDeal) || StringUtil.isNull(sellCodeNoDeal)) {
				BaseHttpEngine.dissMissProgressDialog();
				return;
			}
			requestPsnForexTradeRate(exchangeType, buyCodeNoDeal, sellCodeNoDeal);
		}
	}

	/***
	 * 直接成交---- 查询rate
	 */
	private void requestPsnForexTradeRate(String exchangeType, String bCurrency, String sCurrency) {
		Map<String, Object> map = new Hashtable<String, Object>();
		map.put(Forex.FOREX_EXCHANGETYPE_REQ, exchangeType);
		map.put(Forex.FOREX_BCURRENC_REQ, bCurrency);
		map.put(Forex.FOREX_SCURRENCY_REQ, sCurrency);
		getHttpTools().requestHttp(Forex.FOREX_PSNFOREXTREDEPRE_API, "requestPsnForexTradeRateCallback", map, true);
	}

	/***
	 * 直接成交----回调 查询rate
	 */
	public void requestPsnForexTradeRateCallback(Object resultObj) {
		reqRate = getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNull(reqRate)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			requestPSNGetTokenId();
		}
	}

	/**
	 * 获取tocken
	 */
	private void requestPSNGetTokenId() {
		getHttpTools().requestHttp(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API, "requestPSNGetTokenIdCallback", null, true);
	}

	/**
	 * 获取tokenId----回调
	 * 
	 * @param resultObj
	 *            :返回结果
	 */
	public void requestPSNGetTokenIdCallback(Object resultObj) {
		tokenId = getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNull(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			LogGloble.d(TAG + " tokenId-----", tokenId);
			int type = Integer.valueOf(exchangeType);
			if (buyTag.equals(radioGroup)) {
				// 买入
				buyRequestDate(type);
			} else {
				// 卖出
				sellRequestDate(type);
			}
		}
	}

	/**
	 * 外汇买卖确认提交--回调
	 * 
	 * @param resultObj
	 */
	public void requestCurrencyPsnForexTradeCallback(Object resultObj) {
		super.requestCurrencyPsnForexTradeCallback(resultObj);
		Map<String, Object> tradeResult = getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(tradeResult)) {
			return;
		} else {
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_TRADERESULT_KEY, tradeResult);
			Intent intent = new Intent(ForexQuickCurrentSubmitActivity.this, ForexQuickCurrentSuccessActivity.class);
			intent.putExtra(ConstantGloble.FOREX_CHANGETYPE_KEY, exchangeTypeName);// 交易方式名称
			intent.putExtra(Forex.FOREX_RATE_INVESTACCOUNT_RES, investAccount);
			intent.putExtra(ConstantGloble.FOREX_SELLCODESP_KEY, sellCodeSp);// 卖出币种名称+钞汇
			intent.putExtra(ConstantGloble.FOREX_BUYCODESP_KEY, buyCodeSp);// 买入币种名称
			intent.putExtra(ConstantGloble.FOREX_SELLCODENODEAL_KEY, sellCodeNoDeal);// 卖出币种代码
			intent.putExtra(ConstantGloble.FOREX_BUYCODENODEAL_KEY, buyCodeNoDeal);// 买入币种代码
			intent.putExtra(Forex.FOREX_EXCHANGETYPE_REQ, exchangeType);// 交易方式
			intent.putExtra(Forex.FOREX_CASHREMIT_REQ, cashNoDeal);
			if (sellTag.equals(radioGroup)) {
				// 1-- 卖出
				intent.putExtra(ConstantGloble.FOREX_RADIOBUTTON_KEY, sellTag);
				intent.putExtra(ConstantGloble.FOREX_SELLEDITTEXT_KEY, sellCodeEditText);
			} else if (buyTag.equals(radioGroup)) {
				// 0-- 买入
				intent.putExtra(ConstantGloble.FOREX_RADIOBUTTON_KEY, buyTag);
				intent.putExtra(ConstantGloble.FOREX_COLUMONEY_KEY, coluMoney);
			}
			startActivity(intent);
		}
	}

	/** 查询单笔外汇行情-----回调 */
	@Override
	public void requestPsnForexQuerySingleRateCallback(Object resultObj) {
		super.requestPsnForexQuerySingleRateCallback(resultObj);
		Map<String, Object> result = getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		sourceCurrencyCode = (String) result.get(Forex.FOREX_RATE_SOURCECODE_RES);
		sellRate = (String) result.get(Forex.FOREX_RATE_SELLRATE_RES);
		buyRate = (String) result.get(Forex.FOREX_RATE_BUYRATE_RES);
		if (sellCodeRadioButton.isChecked()) {
			// 设置卖出金额最大值
			setSellMaxMoney();
		} else if (buyCodeRadioButton.isChecked()) {
			// 设置买入金额的最大值
			setBuyMaxMoney();
		}

		if (exchangeType.equals(eleven)) {
			BaseHttpEngine.showProgressDialog();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(Forex.FOREX_BCURRENC_REQ, buyCodeNoDeal);
			params.put(Forex.FOREX_SCURRENCY_REQ, sellCodeNoDeal);
			getHttpTools().requestHttp(Forex.PSNFOREXPENDINGSETRANGEQUERY, "requestPsnForexPendingSetRangeQueryCallBack", params);
		} else {
			BaseHttpEngine.dissMissProgressDialog();
		}
	}
	
	/** 外汇挂单点差范围查询回调 */
	public void requestPsnForexPendingSetRangeQueryCallBack(Object resultObj) {
		Map<String, Object> resultMap = getHttpTools().getResponseResult(resultObj);
		String max = (String) resultMap.get(Forex.MAXPENDINGSET);
		String min = (String) resultMap.get(Forex.MINPENDINGSET);
//		maxPendingSet = Double.parseDouble(max);
//		minPendingSet = Double.parseDouble(min);
		loseRateTip.setText(getResources().getString(R.string.forex_buy_pursuitOfSpread_tip) + min + "-" + max);
		BaseHttpEngine.dissMissProgressDialog();
	}
}
