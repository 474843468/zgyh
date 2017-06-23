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
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 定期快速交易 填写页面 输入外汇买卖信息
 * 
 * @author 宁焰红
 * 
 */
public class ForexQuickTradeSubmitActivity extends ForexBaseActivity implements OnItemSelectedListener {

	// TODO ------------------------成员变量------------------------------//
	
	private static final String TAG = "ForexQuickTradeSubmitActivity";
	/** ForexQuickTradeSubmitActivity的主布局 */
	private View rateInfoView = null;
	/** 返回按钮 */
	private Button backButton = null;
	/** 直接成交按钮 */
	private Button quickTradeButton = null;
	/** 下一步 */
	private Button nextButton = null;

	/** 资金账户 */
	private String investAccount = null;
	/** 交易方式 */
	private Spinner typeSpinner = null;
	/** 存折册号 */
	private Spinner volumeNumberSpinner = null;
	/** 存单号 */
	private Spinner cdNumberListSpinner = null;
	/** 买入币种 */
	private Spinner buyCodeSpinner = null;
	/** 当前汇率 */
	private TextView fixRateText = null;
	/** 限价汇率 */
	private EditText fixLimitRateEditText = null;
	/*** 交易方式 */
	private String exchangeType = null;
	private View rateView = null;
	/** 查询前，所有的折册号列表 */
	private List<String> volumeNumberList = null;
	// 用户选择的信息
	/** 存折册号 --用户选择 */
	private String volumeNumberUsers = null;
	/*** 存单号 */
	private String cdNumberUser = null;
	/** 买入币种 */
	private String buyCodeUser = null;
	/** 存单号的 币种代码 */
	private String sellCodeUser = null;
	/** 存单号---钞汇 */
	private String cashUser = null;
	/** 存单号---可用余额 **/
	private String balanceUser = null;

	/** 卖出币种名称 */
	private String onlySellCodeDealUser = null;
	private boolean isInitType = true, isInitVolumber = true, isInitCDNumber = true, isInitBuyCode = true;
	/** 买入币种信息 */
//	private List<Map<String, String>> buyCodeResultList = null;
	/**
	 * @处理后，买入币种
	 * @将币种代码翻译成币种名称
	 */
	private List<String> buyCodeDealList = null;
	/** 处理前，买入币种 */
	private List<String> buyCodeList = null;
	// 查询存单号信息后的处理
	/** 查询后--存折册号 */
	private List<String> volumeNumberQueryList = null;// 存折册号
	/** 查询后--存单号详细信息 */
	private List<String> cdnumberTypeCodeList = null;// 存单号详细信息，数据进行处理
	private List<Map<String, Object>> cdDealList = null;
	/** 交易方式名称 */
	private String exchangeTypeTrans = null;
	private String buyTransCode = null;
	/** 界面上显示的存单全部内容 */
	private String sellTransCode = null;
	/** 接受用户输入的限价汇率 */
	private String limitRate = null;
	/** 账户类型 */
	private String numberTyper = null;
	private String tokenId = null;
	/** 直接成交的汇率 */
	private String quickrate = null;
	/**
	 * @用于判断是那个交易发出的请求
	 * @101---我的外汇
	 */
	private int tag = -1;
	/** 我的外汇 用户选择的position,默认为-1 **/
//	private int customerSelectedPosition = -1;
	/** 外汇行情页面 买入币种名称 */
//	private String buyCodeNameRate = null;
	/** 外汇行情页面卖出币种代码 */
//	private String sellCodeRate = null;
	/** 存折存单关联数据 */
	private List<Map<String, Object>> volumesAndCdnumbers;
	// private Map<String, Object> volumberAndCDMap = null;
	/** 存折册号、存单号交易使用的数据集合 */
	private List<Map<String, Object>> volumberAndCDMapList = null;
	/** 我的外汇 、银行卖价 存折册号 */
	private String customerVolumber = null;
	/** 我的外汇 、银行卖价 存折单号 */
	private String customerCDNmber = null;
	/** 存单号的适配器 */
	private ArrayAdapter<String> cdAdapter = null;
	/** 存折册号---存单号原始数据 */
	private List<Map<String, Object>> termSubAccountList = null;
	/** 卖出币种的所有数据 */
	private List<Map<String, Object>> subAccountList = null;
	/** 限价汇率是否可见 1-不可见，2-可见 */
//	private int rateViewIsGone = 1;
	/** 委托汇率View */
	private View weiTuoRateView = null;
	/** 截止时间---年月日View */
	private View startTimesView = null;
	/** 获利汇率View */
	private View huoLiRateView = null;
	/** 止损汇率View */
	private View zhiSunRateView = null;
	/** 追击点差View */
	private View pursuitOfStopLossView = null;
	/** 追击点差范围提示 */
	private TextView loseRateTip = null;
	/** 追击止损委托时可输入的最大点差 */
//	private double maxPendingSet;
	/** 追击止损委托时可输入的最小点差 */
//	private double minPendingSet;
	/** 委托汇率 */
	private TextView weiTuoRateText = null;
	/** 追击点差输入框 */
	private TextView pursuitOfSpreadText = null;
	/** 年月日 */
	private TextView startTimes = null;
	/** 时 */
	private Spinner endTimesSpinner = null;
	/** 获利汇率 */
	private TextView huoLiRateText = null;
	/** 止损汇率 */
	private TextView zhiSunRateText = null;
	/** 系统当前时间格式化 */
//	private String currenttime = null;
	/** 年月日 */
	private String startDateStr = null;
	/** 时 */
	private String endDateStr = null;
//	private String currentHours = null;
	/** 委托汇率 */
	private String weiTuoRate = null;
	/** 止损汇率 */
	private String zhiSunRate = null;
	/** 获利汇率 */
	private String huoLiRate = null;
	/** 止损汇率-追击止损委托时使用，从pursuitOfSpreadText取出，追击点差 */
	private String loseRate = null;
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
	/** 当前汇率---view */
	private View currentRateView = null;
	private String custonerTimes = null;
	private View timesHoursView = null;
	private View messageView = null;
	/** 交易方式标记： 0市价即时 1限价即时 2获利委托 3止损委托 4二选一委托 */
	private int index;

	// TODO ------------------------成员方法------------------------------//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		backButton = (Button) findViewById(R.id.ib_back);
		// 返回按钮
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		if (StringUtil.isNull(commConversationId)) {
			return;
		}
		canTwoSided = (String) BaseDroidApp.getInstanse().getBizDataMap().get(Forex.FOREX_RATE_CANTWOSIDED_RES);
		if (StringUtil.isNull(canTwoSided)) {
			return;
		}
		dealDates();
		init();
		dealCustomerOrQuickRequest();
		initClick();
	}

	private void dealDates() {
		eight = LocalData.forexTradeStyleTransList.get(0);
		seven = LocalData.forexTradeStyleTransList.get(1);
		three = LocalData.forexTradeStyleTransList.get(2);
		four = LocalData.forexTradeStyleTransList.get(3);
		five = LocalData.forexTradeStyleTransList.get(4);
		eleven = LocalData.forexTradeStyleTransList.get(5);
		eightName = LocalData.forexTradeStyleList.get(0);
		sevenName = LocalData.forexTradeStyleList.get(1);
		if (LocalData.forexTradeStyleList.size() > 2) {
			threeName = LocalData.forexTradeStyleList.get(2);
			fourName = LocalData.forexTradeStyleList.get(3);
			fiveName = LocalData.forexTradeStyleList.get(4);
			elevenName = LocalData.forexTradeStyleList.get(5);
		}
	}

	@SuppressWarnings("unchecked")
	private void init() {
		rateInfoView = LayoutInflater.from(ForexQuickTradeSubmitActivity.this).inflate(R.layout.forex_rate_fix_submit, null);
		tabcontent.addView(rateInfoView);
		setTitle(getResources().getString(R.string.forex_rate_qick1));
		messageView = findViewById(R.id.message);
		quickTradeButton = (Button) findViewById(R.id.trade_quickButton);
		nextButton = (Button) findViewById(R.id.trade_nextButton);
		nextButton.setVisibility(View.GONE);
		rateView = findViewById(R.id.trade_limiRate);
		rateView.setVisibility(View.GONE);
		weiTuoRateView = findViewById(R.id.weiTuoHuiLv_layout);
		huoLiRateView = findViewById(R.id.twoHuoLi_layout);
		zhiSunRateView = findViewById(R.id.two_zhisun_layout);
		pursuitOfStopLossView = findViewById(R.id.pursuitOfStop_loss_layout);
		startTimesView = findViewById(R.id.weiTuoTimes1_layout);
		timesHoursView = findViewById(R.id.weiTuoTimes2_layout);
		pursuitOfSpreadText = (TextView) findViewById(R.id.et_pursuitOfSpread);
		weiTuoRateText = (TextView) findViewById(R.id.forex_wuituo_rate);
		huoLiRateText = (TextView) findViewById(R.id.forex_two_huoli);
		zhiSunRateText = (TextView) findViewById(R.id.forex_two_zhisun);
		loseRateTip = (TextView) findViewById(R.id.tv_loseRateTip);
		startTimes = (TextView) findViewById(R.id.forex_query_deal_startdate);
		endTimesSpinner = (Spinner) findViewById(R.id.forex_query_deal_enddate);
		endTimesSpinner.setOnItemSelectedListener(this);
		currentRateView = findViewById(R.id.current_rate_layout);
		hideView();
		typeSpinner = (Spinner) findViewById(R.id.forex_rate_currency_type);
		typeSpinner.setOnItemSelectedListener(this);
		fixLimitRateEditText = (EditText) findViewById(R.id.forex_trade_limiRate_Edit);
		volumeNumberSpinner = (Spinner) findViewById(R.id.forex_rate_fix_accNumber);
		volumeNumberSpinner.setOnItemSelectedListener(this);
		cdNumberListSpinner = (Spinner) findViewById(R.id.forex_rate_fix_number);
		cdNumberListSpinner.setOnItemSelectedListener(this);
		buyCodeSpinner = (Spinner) findViewById(R.id.forex_rate_currency_buylCode);
		buyCodeSpinner.setOnItemSelectedListener(this);
		fixRateText = (TextView) findViewById(R.id.forex_rate_currency_rate);

		investAccount = (String) BaseDroidApp.getInstanse().getBizDataMap().get(Forex.FOREX_RATE_INVESTACCOUNT_RES);
		volumeNumberList = (List<String>) BaseDroidApp.getInstanse().getBizDataMap().get(Forex.FOREX_VOLUMENUMBERLIST_RES);
		volumeNumberQueryList = new ArrayList<String>();// 存折册号
		cdnumberTypeCodeList = new ArrayList<String>();// 存单号详细信息
		termSubAccountList = new ArrayList<Map<String, Object>>();
		volumesAndCdnumbers = new ArrayList<Map<String, Object>>();
		cdDealList = new ArrayList<Map<String, Object>>();
		volumberAndCDMapList = new ArrayList<Map<String, Object>>();
		// 默认值“08”
		exchangeType = LocalData.forexTradeStyleTransList.get(0);
		// 默认市价即时
		exchangeTypeTrans = LocalData.forexTradeStyleList.get(0);
		StepTitleUtils.getInstance().initTitldStep(this, new String[] { this.getResources().getString(R.string.forex_rate_tep1), this.getResources().getString(R.string.forex_rate_tep2), this.getResources().getString(R.string.forex_rate_tep3) });
		StepTitleUtils.getInstance().setTitleStep(1);

	}

	/** 隐藏区域 */
	private void hideView() {
		weiTuoRateView.setVisibility(View.GONE);
		huoLiRateView.setVisibility(View.GONE);
		zhiSunRateView.setVisibility(View.GONE);
		startTimesView.setVisibility(View.GONE);
		timesHoursView.setVisibility(View.GONE);
		pursuitOfStopLossView.setVisibility(View.GONE);
		currentRateView.setVisibility(View.VISIBLE);
		messageView.setVisibility(View.GONE);
	}

	/**
	 * 用于判断是我的外汇发出的请求，还是点击快速交易发出的请求。
	 */
	@SuppressWarnings("unchecked")
	public void dealCustomerOrQuickRequest() {
		Intent intent = getIntent();
		if (intent == null) {
			return;
		} else {
			tag = intent.getIntExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, -1);

		}
		switch (tag) {
		case ConstantGloble.FOREX_TRADE_QUICK:// 快速交易 -1
//			customerSelectedPosition = -1;
			break;
		case ConstantGloble.FOREX_CUSTOMER_FIX_TAG:// 我的外汇 101
//			customerSelectedPosition = -3;
			customerVolumber = intent.getStringExtra(Forex.FOREX_VOLUMENUMBER_RES);
			customerCDNmber = intent.getStringExtra(Forex.FOREX_CDNUMBER_RES);
			break;
		case ConstantGloble.FOREX_TRADE_QUICK_RATEINFO:// 外汇行情页面 301 银行买价
//			customerSelectedPosition = -2;
			break;
		case ConstantGloble.FOREX_TRADE_QUICK_SELL_FIX:// 外汇行情页面 601 银行卖价
//			customerSelectedPosition = -4;
			break;
		default:// 快速交易
			break;
		}
		subAccountList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap().get(Forex.FORX_TERMSUBACCOUNT_RES);
		if (subAccountList == null || subAccountList.size() <= 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexQuickTradeSubmitActivity.this.getResources().getString(R.string.forex_customer_fix_sell));
			return;
		}
		buyCodeDealList = (List<String>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.FOREX_BUYCODEDEALLIST_KEY);
		buyCodeList = (List<String>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.FOREX_BUYCODELIST_KEY);
		deleteRMB();
		getInitData();
	}

	/** 将卖出币种里的人民币去除 */
	@SuppressWarnings("unchecked")
	private void deleteRMB() {
		int len = subAccountList.size();
		for (int i = 0; i < len; i++) {
			Map<String, Object> map = subAccountList.get(i);
			Map<String, String> currency = (Map<String, String>) map.get(Forex.FOREX_CURRENCY_RES);
			String code = null;
			if (StringUtil.isNullOrEmpty(currency)) {
				continue;
			} else {
				// 得到币种
				code = currency.get(Forex.FOREX_CURRENCY_CODE_RES);
				if (StringUtil.isNull(code)) {
					continue;
				} else {
					if (ConstantGloble.FOREX_RMB_TAG1.equals(code) || ConstantGloble.FOREX_RMB_CNA_TAG2.equals(code)) {
						// 币种为人民币，什么也不做
					} else {
						termSubAccountList.add(map);
					}
				}
			}
		}
	}

	/** 选择小时数 */
	private void getPosition(int number) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, LocalData.forexTimesList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		endTimesSpinner.setAdapter(adapter);
		endTimesSpinner.setSelection(0);
		endDateStr = endTimesSpinner.getSelectedItem().toString().trim();
		// for (int i = 0; i < 24; i++) {
		// if (number == i) {
		// endTimesSpinner.setSelection(i);
		// endDateStr = endTimesSpinner.getSelectedItem().toString().trim();
		// }
		// }
	}

	/** 得到最终的存折册号 */
	@SuppressWarnings("unchecked")
	private void getVolumeNumberList() {
		int len = volumeNumberList.size();
		int len2 = termSubAccountList.size();
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len2; j++) {
				Map<String, Object> termSubAccountMap = termSubAccountList.get(j);
				String status = (String) termSubAccountMap.get(Forex.FOREX_STATUS_RES);
				// 存单类型
				String type = (String) termSubAccountMap.get(Forex.FOREX_TYPE_RES);
				// 允许的存单类型有哪些
				// 账户状态正常并且存单类型是整存整取或定活两遍
				if (ConstantGloble.FOREX_ACCTYPE_NORMAL.equals(status) && (ConstantGloble.FOREX_ACCTYPE_ZCZQ.equals(type) || ConstantGloble.FOREX_ACCTYPE_DHLB.equals(type))) {
					// 得到可用余额，并判断是否大于0
					Map<String, Object> balanceMap = (Map<String, Object>) termSubAccountMap.get(Forex.FOREX_BALANCE_RES);
					if (!StringUtil.isNullOrEmpty(balanceMap)) {
						String balance = (String) balanceMap.get(Forex.FOREX_AVAILABLEBALANCE_RES);
						double b = Double.valueOf(balance);
						if (b > 0) {
							// 存折册号
							String volumeNumber = (String) termSubAccountMap.get(Forex.FOREX_VOLUMENUMBER_RES);
							if (volumeNumber.equals(volumeNumberList.get(i))) {
								if (volumeNumberQueryList.size() > 0) {
									if (!volumeNumberQueryList.contains(volumeNumber)) {
										volumeNumberQueryList.add(volumeNumber);
									}
								} else {
									volumeNumberQueryList.add(volumeNumber);
								}
							}

						}
					}
				}
			} // for
		} // for
		if (StringUtil.isNullOrEmpty(volumeNumberQueryList)) {
			return;
		} else {
			getCommData();
		}
	}

	/** 将存折册号、存单号关联起来 */
	@SuppressWarnings("unchecked")
	private void getCommData() {
		int len = volumeNumberQueryList.size();
		int len2 = termSubAccountList.size();
		for (int i = 0; i < len; i++) {
			ArrayList<Map<String, Object>> list = null;
			list = new ArrayList<Map<String, Object>>();
			Map<String, Object> mapContent = null;
			for (int j = 0; j < len2; j++) {
				Map<String, Object> termSubAccountMap = termSubAccountList.get(j);
				String status = (String) termSubAccountMap.get(Forex.FOREX_STATUS_RES);
				// 存单类型
				String type = (String) termSubAccountMap.get(Forex.FOREX_TYPE_RES);
				Map<String, Object> balanceMap = (Map<String, Object>) termSubAccountMap.get(Forex.FOREX_BALANCE_RES);
				String volumeNumber = (String) termSubAccountMap.get(Forex.FOREX_VOLUMENUMBER_RES);
				String cdnumber = (String) termSubAccountMap.get(Forex.FOREX_CDNUMBER_RES);
				// 账户状态正常、存单类型为整存整取、定活两遍
				if (ConstantGloble.FOREX_ACCTYPE_NORMAL.equals(status) && (ConstantGloble.FOREX_ACCTYPE_ZCZQ.equals(type) || ConstantGloble.FOREX_ACCTYPE_DHLB.equals(type))) {
					String availableBalance = (String) balanceMap.get(Forex.FOREX_AVAILABLEBALANCE_RES);
					double b = Double.valueOf(availableBalance);
					// 账户余额>0
					if (b > 0) {
						if (volumeNumber.equals(volumeNumberQueryList.get(i))) {
							mapContent = new HashMap<String, Object>();
							mapContent.put(Forex.FOREX_CDNUMBER_RES, cdnumber);
							mapContent.put(ConstantGloble.FOREX_TERMSUBACCOUNTMAP_KEY, termSubAccountMap);
							list.add(mapContent);
						}
					}
				}
			} // for
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Forex.FOREX_VOLUMENUMBER_RES, volumeNumberQueryList.get(i));
			map.put(ConstantGloble.FOREX_LIST_KEY, list);
			volumesAndCdnumbers.add(map);
		} // for
	}

	/** 处理存折册号、存单号信息,页面显示的内容 */
	@SuppressWarnings("unchecked")
	private void dealVolumberAndCDNumber() {
		int len = volumesAndCdnumbers.size();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < len; i++) {
			Map<String, Object> map = volumesAndCdnumbers.get(i);
			// 册号
			String volumber = (String) map.get(Forex.FOREX_VOLUMENUMBER_RES);
			List<String> cdListDeal = new ArrayList<String>();// 数据已处理
			List<Map<String, Object>> cdInfoMapList = new ArrayList<Map<String, Object>>();
			// list是一个册号对应的所有数据
			list = (List<Map<String, Object>>) map.get(ConstantGloble.FOREX_LIST_KEY);
			for (int j = 0; j < list.size(); j++) {
				Map<String, Object> mapContent = list.get(j);
				// 单号
				String cdNumber = (String) mapContent.get(Forex.FOREX_CDNUMBER_RES);
				Map<String, Object> termSubAccountMap = (Map<String, Object>) mapContent.get(ConstantGloble.FOREX_TERMSUBACCOUNTMAP_KEY);
				// 账户类型
				String type = (String) termSubAccountMap.get(Forex.FOREX_TYPE_RES);
				String typeTrans = null;
				if (LocalData.fixAccTypeMap.containsKey(type)) {
					typeTrans = LocalData.fixAccTypeMap.get(type);
				} else {
					typeTrans = type;
				}
				// 钞汇
				String cashRemit = (String) termSubAccountMap.get(Forex.FOREX_CASHREMIT_RES);
				Map<String, Object> balance = (Map<String, Object>) termSubAccountMap.get(Forex.FOREX_BALANCE_RES);
				// 可用余额
				String availableBalance = (String) balance.get(Forex.FOREX_AVAILABLEBALANCE_RES);
				Map<String, String> currency = (Map<String, String>) balance.get(Forex.FOREX_CURRENCY_RES);
				// 币种
				String code = currency.get(Forex.FOREX_CURRENCY_CODE_RES);
				String codeTrans = null;
				if (LocalData.Currency.containsKey(code)) {
					codeTrans = LocalData.Currency.get(code);
				}
				StringBuilder sb = new StringBuilder(cdNumber);
				sb.append(" ");
				sb.append(typeTrans);
				sb.append(" ");
				sb.append(codeTrans);
				cdListDeal.add(sb.toString());
				Map<String, Object> cdInfoMap = new HashMap<String, Object>();
				Map<String, String> cdContentMap = new HashMap<String, String>();
				cdContentMap.put(Forex.FOREX_TYPE_RES, type);
				cdContentMap.put(Forex.FOREX_CASHREMIT_RES, cashRemit);
				cdContentMap.put(Forex.FOREX_AVAILABLEBALANCE_RES, availableBalance);
				cdContentMap.put(Forex.FOREX_CURRENCY_CODE_RES, code);

				cdInfoMap.put(Forex.FOREX_CDNUMBER_RES, cdNumber);
				cdInfoMap.put(ConstantGloble.FOREX_CDCONTENTMAP_KEY, cdContentMap);
				cdInfoMapList.add(cdInfoMap);

			}
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put(Forex.FOREX_VOLUMENUMBER_RES, volumber);
			map1.put(ConstantGloble.FOREX_CDLISTDEAL, cdListDeal);
			cdDealList.add(map1);
			Map<String, Object> volumberAndCDMap = new HashMap<String, Object>();
			volumberAndCDMap.put(Forex.FOREX_VOLUMENUMBER_RES, volumber);
			volumberAndCDMap.put(ConstantGloble.FOREX_CDINFOMAPLIST_KEY, cdInfoMapList);
			volumberAndCDMapList.add(volumberAndCDMap);
		}
	}

	/** 根据存折册号获取存单号的list */
	@SuppressWarnings("unchecked")
	private List<String> getCDNumberList(String volumeNumberUsers) {
		int cdDealListLen = cdDealList.size();
		for (int i = 0; i < cdDealListLen; i++) {
			Map<String, Object> map = cdDealList.get(i);
			String volumber = (String) map.get(Forex.FOREX_VOLUMENUMBER_RES);
			List<String> cdListDeal = (List<String>) map.get(ConstantGloble.FOREX_CDLISTDEAL);
			if (volumber.equals(volumeNumberUsers)) {
				return cdListDeal;
			}
		}
		return null;
	}

	/**
	 * 根据存单号、存折册号查询卖出币种、钞汇、可用余额信息
	 * 
	 * @param volumeNumberUsers
	 *            :册号
	 * @param cdNumberUser
	 *            ：单号
	 */
	@SuppressWarnings("unchecked")
	private void getOthereData(String volumeNumberUsers, String cdNumberUser) {
		int len = volumberAndCDMapList.size();
		for (int i = 0; i < len; i++) {
			Map<String, Object> volumberAndCDMap = volumberAndCDMapList.get(i);
			// 册号
			String volumber = (String) volumberAndCDMap.get(Forex.FOREX_VOLUMENUMBER_RES);
			// 册号对应所有的单号信息
			List<Map<String, Object>> cdInfoMapList = (List<Map<String, Object>>) volumberAndCDMap.get(ConstantGloble.FOREX_CDINFOMAPLIST_KEY);
			if (volumber.equals(volumeNumberUsers)) {
				int len2 = cdInfoMapList.size();
				for (int j = 0; j < len2; j++) {
					Map<String, Object> cdInfoMap = cdInfoMapList.get(j);
					// 单号
					String cdNumber = (String) cdInfoMap.get(Forex.FOREX_CDNUMBER_RES);
					Map<String, String> cdContentMap = (Map<String, String>) cdInfoMap.get(ConstantGloble.FOREX_CDCONTENTMAP_KEY);
					if (cdNumber.equals(cdNumberUser)) {
						numberTyper = cdContentMap.get(Forex.FOREX_TYPE_RES);
						sellCodeUser = cdContentMap.get(Forex.FOREX_CURRENCY_CODE_RES);
						cashUser = cdContentMap.get(Forex.FOREX_CASHREMIT_RES);
						balanceUser = cdContentMap.get(Forex.FOREX_AVAILABLEBALANCE_RES);
					}
				}
			}
		}
	}

	private void dealType(String isTrue) {
		// 交易方式
		ArrayAdapter<String> typeAdapter = null;
		if (ConstantGloble.TRUE.equals(isTrue)) {
			// 不可做二选一委托
			typeAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, LocalData.forexTradeStyleList);
		} else {
			typeAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, LocalData.forexTradeNoTwoList);
			huoLiRateView.setVisibility(View.GONE);
			zhiSunRateView.setVisibility(View.GONE);
		}
		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		typeSpinner.setAdapter(typeAdapter);
		typeSpinner.setSelection(0);
		exchangeType = LocalData.forexTradeStyleTransList.get(0);
		exchangeTypeTrans = typeSpinner.getSelectedItem().toString().trim();
	}

	/** 对存单号、存折册号赋初始值 **/
	public void initSpinnerData() {
		int volumberPosition = -1;
		int buyCodePosition = -1;
		dealType(canTwoSided);
		// 存折册号不能为空
		if (StringUtil.isNullOrEmpty(volumeNumberQueryList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexQuickTradeSubmitActivity.this.getResources().getString(R.string.forex_no_acc));
			return;
		}

		switch (tag) {
		case ConstantGloble.FOREX_TRADE_QUICK:// 快速交易
			// 存折册号初始化
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, volumeNumberQueryList);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			volumeNumberSpinner.setAdapter(adapter);
			volumeNumberSpinner.setSelection(0);
			volumeNumberUsers = volumeNumberSpinner.getSelectedItem().toString().trim();
			// 存折册号取第一个
			cdnumberTypeCodeList = getCDNumberList(volumeNumberUsers);
			initCDNumber();

			// 买入币种
			ArrayAdapter<String> buyAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, buyCodeDealList);
			buyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			buyCodeSpinner.setAdapter(buyAdapter);
			initBuyCode();

			break;
		case ConstantGloble.FOREX_CUSTOMER_FIX_TAG:// 我的外汇101
//			customerSelectedPosition = -3;
			// 存折册号初始化
			ArrayAdapter<String> customerVolumberAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, volumeNumberQueryList);
			customerVolumberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			volumeNumberSpinner.setAdapter(customerVolumberAdapter);
			// 得到存折册号的position
			volumberPosition = getVolumberPosition(customerVolumber);
			volumeNumberSpinner.setSelection(volumberPosition);
			volumeNumberUsers = volumeNumberSpinner.getSelectedItem().toString().trim();
			// 获取存折单号list
			cdnumberTypeCodeList = getCDNumberList(volumeNumberUsers);
			initCDNumber(customerCDNmber);

			// 买入币种
			ArrayAdapter<String> customerBuyAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, buyCodeDealList);
			customerBuyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			buyCodeSpinner.setAdapter(customerBuyAdapter);
			initBuyCode();

			break;
		case ConstantGloble.FOREX_TRADE_QUICK_RATEINFO:// 外汇行情页面 301 买入币种
			// 存折册号----------
			String volumeNumber = (String) BaseDroidApp.getInstanse().getBizDataMap().get(Forex.FOREX_VOLUMENUMBER_RES);
			String cdnumber = (String) BaseDroidApp.getInstanse().getBizDataMap().get(Forex.FOREX_CDNUMBER_RES);
			ArrayAdapter<String> volumdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, volumeNumberQueryList);
			volumdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			volumeNumberSpinner.setAdapter(volumdapter);
			// 得到存折册号的position
			volumberPosition = getVolumberPosition(volumeNumber);
			volumeNumberSpinner.setSelection(volumberPosition);
			volumeNumberUsers = volumeNumberSpinner.getSelectedItem().toString().trim();
			// 获取存折单号list
			cdnumberTypeCodeList = getCDNumberList(volumeNumberUsers);
			initCDNumber(cdnumber);

			// 买入币种
			ArrayAdapter<String> buyAdapter1 = new ArrayAdapter<String>(this, R.layout.dept_spinner, buyCodeDealList);
			buyAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			buyCodeSpinner.setAdapter(buyAdapter1);
			buyCodePosition = (Integer) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.FOREX_BUYINLIST_POSITION);
			initBuyCode(buyCodePosition);
			break;
		case ConstantGloble.FOREX_TRADE_QUICK_SELL_FIX:// 外汇行情页面 601 银行卖价
			String volumeNumber1 = (String) BaseDroidApp.getInstanse().getBizDataMap().get(Forex.FOREX_VOLUMENUMBER_RES);
			String cdnumber1 = (String) BaseDroidApp.getInstanse().getBizDataMap().get(Forex.FOREX_CDNUMBER_RES);
			// 存折册号初始化
			ArrayAdapter<String> volumberAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, volumeNumberQueryList);
			volumberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			volumeNumberSpinner.setAdapter(volumberAdapter);
			// 得到存折册号的position
			volumberPosition = getVolumberPosition(volumeNumber1);
			volumeNumberSpinner.setSelection(volumberPosition);
			volumeNumberUsers = volumeNumberSpinner.getSelectedItem().toString().trim();
			// 获取存折单号list
			cdnumberTypeCodeList = getCDNumberList(volumeNumberUsers);
			initCDNumber(cdnumber1);

			// 买入币种
			ArrayAdapter<String> sellBuyAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, buyCodeDealList);
			sellBuyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			buyCodeSpinner.setAdapter(sellBuyAdapter);
			buyCodePosition = (Integer) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.FOREX_BUYINLIST_POSITION);
			initBuyCode(buyCodePosition);

			break;
		default:
			break;
		}// switch
	}

	/** 得到存折册号的position */
	private int getVolumberPosition(String volumber) {
		int len = volumeNumberQueryList.size();
		int j = -1;
		for (int i = 0; i < len; i++) {
			if (volumber.equals(volumeNumberQueryList.get(i))) {
				j = i;
			}
		}
		return j;
	}

	/** 存折单号信息 */
	private void initCDNumber() {
		if (StringUtil.isNullOrEmpty(cdnumberTypeCodeList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexQuickTradeSubmitActivity.this.getResources().getString(R.string.forex_no_acc));
			return;
		} else {
			// 存折单号
			cdAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, cdnumberTypeCodeList);
			cdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			cdNumberListSpinner.setAdapter(cdAdapter);
			cdNumberListSpinner.setSelection(0);
			sellTransCode = cdNumberListSpinner.getSelectedItem().toString().trim();
			int s1 = sellTransCode.indexOf(" ");
			int s2 = sellTransCode.lastIndexOf(" ");
			int sellLen = sellTransCode.length();
			// 存折单号
			cdNumberUser = sellTransCode.substring(0, s1).trim();
			// 卖出币种名称
			onlySellCodeDealUser = sellTransCode.substring(s2, sellLen).trim();
			getOthereData(volumeNumberUsers, cdNumberUser);
		}
	}

	/** 存折单号信息 */
	private void initCDNumber(String cdNumber) {
		if (StringUtil.isNullOrEmpty(cdnumberTypeCodeList)) {
			return;
		} else {
			// 存单号----------
			cdAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, cdnumberTypeCodeList);
			cdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			cdNumberListSpinner.setAdapter(cdAdapter);
			for (int j = 0; j < cdnumberTypeCodeList.size(); j++) {
				String info = cdnumberTypeCodeList.get(j);
				int s1 = info.indexOf(" ");
				String cd = info.substring(0, s1).toString().trim();
				if (cd.equals(cdNumber)) {
					cdNumberListSpinner.setSelection(j);
					break;
				}
			}
			sellTransCode = cdNumberListSpinner.getSelectedItem().toString().trim();
			int s1 = sellTransCode.indexOf(" ");
			int s2 = sellTransCode.lastIndexOf(" ");
			int sellLen = sellTransCode.length();
			// 存折单号
			cdNumberUser = sellTransCode.substring(0, s1).trim();
			// 卖出币种名称
			onlySellCodeDealUser = sellTransCode.substring(s2, sellLen).trim();
			getOthereData(volumeNumberUsers, cdNumberUser);
		}
	}

	private void initBuyCode() {
		buyCodeSpinner.setSelection(0);
		buyCodeUser = buyCodeList.get(0);
		buyTransCode = buyCodeSpinner.getSelectedItem().toString().trim();
		String buyCodeUserName = LocalData.Currency.get(buyCodeUser);
		if (buyCodeUserName.equals(onlySellCodeDealUser)) {
			if (buyCodeList.size() >= 2) {
				buyCodeSpinner.setSelection(1);
				buyCodeUser = buyCodeList.get(1);
				buyTransCode = buyCodeSpinner.getSelectedItem().toString().trim();
			} else {
				return;
			}
		}
	}

	/** 初始化买入币种 */
	private void initBuyCode(int buyCodePosition) {
		if (buyCodePosition > -1) {
			buyCodeSpinner.setSelection(buyCodePosition);
			buyCodeUser = buyCodeList.get(buyCodePosition);// 币种代码
			buyTransCode = buyCodeSpinner.getSelectedItem().toString().trim();// 币种名称
		} else {
			buyCodeSpinner.setSelection(0);
			buyCodeUser = buyCodeList.get(0);
			buyTransCode = buyCodeSpinner.getSelectedItem().toString().trim();
			String buyCodeName = LocalData.Currency.get(buyCodeUser);
			// 卖出币种名称和买入币种名称相同
			if (onlySellCodeDealUser.equals(buyCodeName)) {
				if (buyCodeList.size() >= 2) {
					buyCodeSpinner.setSelection(1);
					buyCodeUser = buyCodeList.get(1);
					buyTransCode = buyCodeSpinner.getSelectedItem().toString().trim();
				} else {
					return;
				}

			}
		}
	}

	/**
	 * 准备卖出提交参数
	 * 
	 * @return 参数列表
	 */
	private Map<String, Object> getParams() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Forex.FOREX_INVESTACCOUNT_REQ, investAccount);
		params.put(Forex.FOREX_BCURRENCY_REQ, buyCodeUser);
		params.put(Forex.FOREX_SCURRENCYT_REQ, sellCodeUser);
		params.put(Forex.FOREX_TRANSFLAG_REQ, LocalData.forexTradeSellOrBuyList.get(1));
		params.put(Forex.FOREX_EXCHANGETYPET_REQ, exchangeType);

		params.put(Forex.FOREX_CASHREMIT_REQ, cashUser);
		params.put(Forex.FOREX_VOLUMENUMBER_REQ, volumeNumberUsers);
		params.put(Forex.FOREX_CDNUMBER_REQ, cdNumberUser);
		params.put(Forex.FOREX_TOKEN_REQ, tokenId);
		params.put(Forex.FOREX_CDTYPE_REQ, numberTyper);

		// 卖出
		String s = balanceUser;
		// 小数位数最多为2位
		s = StringUtil.splitStringwithCode(sellCodeUser, s, 2);
		params.put(Forex.FOREX_SAMOUNT_REQ, s);

		int type = Integer.valueOf(exchangeType);
		switch (type) {

		case ConstantGloble.FOREX_TRADE_SEVEN:// 限价
			params.put(Forex.FOREX_EXCHANGERATE, quickrate);
			params.put(Forex.FOREX_LIMITRATE_REQ, limitRate);
			break;
		case ConstantGloble.FOREX_TRADE_EIGHT:// 市价
			params.put(Forex.FOREX_EXCHANGERATE, quickrate);
			break;
		case ConstantGloble.FOREX_TRADE_THREE:// 获利委托
			params.put(Forex.FOREX_WINRATE_REQ, null);
			params.put(Forex.FOREX_CONSIGNAYS_REQ, null);
			params.put(Forex.FOREX_CONSIGNHOUR_REQ, null);
			break;
		case ConstantGloble.FOREX_TRADE_FOUR:// 止损委托
			params.put(Forex.FOREX_LOSERATE_REQ, null);
			params.put(Forex.FOREX_CONSIGNAYS_REQ, null);
			params.put(Forex.FOREX_CONSIGNHOUR_REQ, null);
			break;
		case ConstantGloble.FOREX_TRADE_FIVE:// 二选一委托
			params.put(Forex.FOREX_WINRATE_REQ, null);
			params.put(Forex.FOREX_CONSIGNAYS_REQ, null);
			params.put(Forex.FOREX_CONSIGNHOUR_REQ, null);
			break;
		case ConstantGloble.FOREX_TRADE_ELEVEN:// 追击止损委托
			params.put(Forex.FOREX_LOSERATE_REQ, null);
			break;
		default:
			break;
		}

		return params;
	}

	/** 动态修改limitRate的hint */
	private void updateLimitHint() {
		String spText = getResources().getString(R.string.forex_rate_currency_limitRate_info1);
		String text = getResources().getString(R.string.forex_rate_currency_limitRate_info);
		if (spetialCodeList.contains(sellCodeUser) && spetialCodeList.contains(buyCodeUser)) {
			fixLimitRateEditText.setHint(text);
			weiTuoRateText.setHint(text);
			huoLiRateText.setHint(text);
			zhiSunRateText.setHint(text);
		} else {
			if (tradeCheckCodeNoNumber.contains(sellCodeUser) || tradeCheckCodeNoNumber.contains(buyCodeUser)) {
				fixLimitRateEditText.setHint(spText);
				weiTuoRateText.setHint(spText);
				huoLiRateText.setHint(spText);
				zhiSunRateText.setHint(spText);
			} else {
				fixLimitRateEditText.setHint(text);
				weiTuoRateText.setHint(text);
				huoLiRateText.setHint(text);
				zhiSunRateText.setHint(text);
			}
		}
	}

	private void choiceExchangeType(int position) {
		switch (position) {
		case 0:// 市价即时
			rateView.setVisibility(View.GONE);
//			rateViewIsGone = 1;
			quickTradeButton.setVisibility(View.VISIBLE);
			exchangeType = eight;
			exchangeTypeTrans = eightName;
			// 市价即时不需要下一步按钮
			nextButton.setVisibility(View.GONE);
			hideView();
			break;
		case 1:// 限价即时
			hideView();
			rateView.setVisibility(View.VISIBLE);
//			rateViewIsGone = 2;
			quickTradeButton.setVisibility(View.GONE);
			nextButton.setVisibility(View.VISIBLE);
			exchangeType = seven;
			exchangeTypeTrans = sevenName;
			fixLimitRateEditText.requestFocus();
			updateLimitHint();
			break;
		case 2:// 获利委托
			rateView.setVisibility(View.GONE);
//			rateViewIsGone = 1;
			showPartView();
			setTimes(startDateStr, endDateStr);
			exchangeType = three;
			exchangeTypeTrans = threeName;
			updateLimitHint();
			break;
		case 3:// 止损委托
			rateView.setVisibility(View.GONE);
//			rateViewIsGone = 1;
			showPartView();
			setTimes(startDateStr, endDateStr);
			exchangeType = four;
			exchangeTypeTrans = fourName;
			updateLimitHint();
			break;
		case 4:// 二选一委托
			rateView.setVisibility(View.GONE);
//			rateViewIsGone = 1;
			showPartsView();
			setTimes(startDateStr, endDateStr);
			exchangeType = five;
			exchangeTypeTrans = fiveName;
			updateLimitHint();
			break;
		case 5:// 追击止损委托
			rateView.setVisibility(View.GONE);
//			rateViewIsGone = 1;
			showPursuitOfStopLossView();
			setTimes(startDateStr, endDateStr);
			exchangeType = eleven;
			exchangeTypeTrans = elevenName;
			break;
		default:
			break;
		}
	}

	/** 止损委托---获利委托--使用 */
	private void showPartView() {
		weiTuoRateText.requestFocus();
		pursuitOfStopLossView.setVisibility(View.GONE);
		weiTuoRateView.setVisibility(View.VISIBLE);
		startTimesView.setVisibility(View.VISIBLE);
		timesHoursView.setVisibility(View.VISIBLE);
		quickTradeButton.setVisibility(View.GONE);
		nextButton.setVisibility(View.VISIBLE);
		huoLiRateView.setVisibility(View.GONE);
		zhiSunRateView.setVisibility(View.GONE);
		currentRateView.setVisibility(View.GONE);
		messageView.setVisibility(View.VISIBLE);
	}

	/** 二选一委托--使用 */
	private void showPartsView() {
		huoLiRateText.requestFocus();
		weiTuoRateView.setVisibility(View.GONE);
		pursuitOfStopLossView.setVisibility(View.GONE);
		startTimesView.setVisibility(View.VISIBLE);
		timesHoursView.setVisibility(View.VISIBLE);
		quickTradeButton.setVisibility(View.GONE);
		nextButton.setVisibility(View.VISIBLE);
		huoLiRateView.setVisibility(View.VISIBLE);
		zhiSunRateView.setVisibility(View.VISIBLE);
		currentRateView.setVisibility(View.GONE);
		messageView.setVisibility(View.VISIBLE);
	}
	
	/** 追击止损委托--使用 */
	private void showPursuitOfStopLossView() {
		huoLiRateText.requestFocus();
		weiTuoRateView.setVisibility(View.GONE);
		pursuitOfStopLossView.setVisibility(View.VISIBLE);
		startTimesView.setVisibility(View.VISIBLE);
		timesHoursView.setVisibility(View.VISIBLE);
		quickTradeButton.setVisibility(View.GONE);
		nextButton.setVisibility(View.VISIBLE);
		huoLiRateView.setVisibility(View.GONE);
		zhiSunRateView.setVisibility(View.GONE);
		currentRateView.setVisibility(View.GONE);
		messageView.setVisibility(View.VISIBLE);
	}

	/** 设置委托截止时间 */
	private void setTimes(String currenttime, String endDateStr) {
		startTimes.setText(currenttime);
	}

	/** 清空汇率数据 */
	private void clearRate() {
		fixLimitRateEditText.setText("");
		weiTuoRateText.setText("");
		huoLiRateText.setText("");
		if (index == 4)// 二选一委托时数据清空时 设置获利委托输入框获得焦点
			huoLiRateText.requestFocus();
		zhiSunRateText.setText("");
		fixRateText.setText("");
	}


	/** 根据交易方式验证不同的数据 */
	private void checkDate(String type) {
		if (type.equals(seven)) {
			// 验证限价汇率
			checkSellDate();
		} else if (type.equals(three) || type.equals(four)) {
			// 委托、止损--验证委托汇率、委托截止时间
			checkWeituoRate();
		} else if (type.equals(five)) {
			// 二选一-----验证获利汇率、止损汇率、截止时间
			checkTwoRate();
		} else if (type.equals(eleven)) {
			loseRate = pursuitOfSpreadText.getText().toString().trim();
			if (StringUtil.isNullOrEmpty(loseRate)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.forex_buy_pursuitOfSpread_null_error));
				return;
			}
			if (Double.parseDouble(loseRate) == 0.0) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.forex_buy_pursuitOfSpread_zero_error));
				return;
			}
			checkTimes();
		}
	}

	/** 验证限价汇率 */
	private void checkSellDate() {
		limitRate = fixLimitRateEditText.getText().toString().trim();
		String buyCodeName = LocalData.Currency.get(buyCodeUser);
		String sellCodeName = LocalData.Currency.get(sellCodeUser);
		// 买入、卖出币种为日元或港元，小数点后4位
		if (spetialCodeList.contains(sellCodeUser) && spetialCodeList.contains(buyCodeUser)) {
			RegexpBean reb1 = new RegexpBean(rateEmg, limitRate, "spetialRate", true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb1);
			if (RegexpUtils.regexpDate(lists)) {
				getRate(buyCodeName, sellCodeName);
			} else {
				return;
			}
		} else {
			// 卖出币种为特殊币种
			if (tradeCheckCodeNoNumber.contains(sellCodeUser) || tradeCheckCodeNoNumber.contains(buyCodeUser)) {
				RegexpBean reb1 = new RegexpBean(rateEmg, limitRate, "spetialLimitRate", true);
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(reb1);
				if (RegexpUtils.regexpDate(lists)) {
					getRate(buyCodeName, sellCodeName);
				} else {
					return;
				}
			} else {
				RegexpBean reb1 = new RegexpBean(rateEmg, limitRate, "limitRate", true);
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(reb1);
				if (RegexpUtils.regexpDate(lists)) {
					getRate(buyCodeName, sellCodeName);
				} else {
					return;
				}
			}
		}

	}

	/** 验证委托汇率 */
	private void checkWeituoRate() {
		weiTuoRate = weiTuoRateText.getText().toString().trim();
		// 买入、卖出币种为日元或港元，小数点后4位
		if (spetialCodeList.contains(sellCodeUser) && spetialCodeList.contains(buyCodeUser)) {
			RegexpBean reb1 = new RegexpBean(getResources().getString(R.string.forex_trade_zhiSun_weituo1), weiTuoRate, "spetialRate", true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb1);
			if (RegexpUtils.regexpDate(lists)) {
				checkTimes();
			} else {
				return;
			}
		} else {
			// 卖出币种为特殊币种
			if (tradeCheckCodeNoNumber.contains(sellCodeUser) || tradeCheckCodeNoNumber.contains(buyCodeUser)) {
				RegexpBean reb1 = new RegexpBean(getResources().getString(R.string.forex_trade_zhiSun_weituo1), weiTuoRate, "spetialLimitRate", true);
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(reb1);
				if (RegexpUtils.regexpDate(lists)) {
					checkTimes();
				} else {
					return;
				}
			} else {
				RegexpBean reb1 = new RegexpBean(getResources().getString(R.string.forex_trade_zhiSun_weituo1), weiTuoRate, "limitRate", true);
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(reb1);
				if (RegexpUtils.regexpDate(lists)) {
					checkTimes();
				} else {
					return;
				}
			}
		}
	}

	/** 验证委托截止时间 ----查询汇率 */
	private void checkTimes() {
		StringBuilder sb = new StringBuilder(startDateStr);
		sb.append(" ");
		sb.append(endDateStr);
		sb.append(":00:00");
		custonerTimes = sb.toString().trim();
		String buyCodeName = LocalData.Currency.get(buyCodeUser);
		String sellCodeName = LocalData.Currency.get(sellCodeUser);
		if (buyCodeName.equals(sellCodeName)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexQuickTradeSubmitActivity.this.getString(R.string.forex_trade_sellBuyCode));
			return;
		}
		goToConfirmActivity();
	}

	/** 进入到确认页面 */
	private void goToConfirmActivity() {
		Intent intent = new Intent(ForexQuickTradeSubmitActivity.this, ForexQuickTradeConfirmActivity.class);
		// 页面显示的内容
		intent.putExtra(ConstantGloble.FOREX_CHANGETYPE_KEY, exchangeTypeTrans);
		intent.putExtra(Forex.FOREX_EXCHANGETYPE_REQ, exchangeType);
		intent.putExtra(Forex.FOREX_VOLUMENUMBER_REQ, volumeNumberUsers);
		intent.putExtra(ConstantGloble.FOREX_SELLTRANSCODE_KEY, sellTransCode);
		intent.putExtra(ConstantGloble.FOREX_BUYCODESP_KEY, buyTransCode);
		intent.putExtra(ConstantGloble.FOREX_ONLYSELLCODEDEALUSER_KEY, onlySellCodeDealUser);
		if (exchangeType.equals(three) || exchangeType.equals(four)) {
			intent.putExtra(ConstantGloble.FOREX_WEITUORATE_KEY, weiTuoRate);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY1, startDateStr);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY2, endDateStr);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY3, custonerTimes);
		} else if (exchangeType.equals(five)) {
			intent.putExtra(ConstantGloble.FOREX_HUOLIRATE_KEY, huoLiRate);
			intent.putExtra(ConstantGloble.FOREX_ZHISUNRATE_KEY, zhiSunRate);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY1, startDateStr);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY2, endDateStr);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY3, custonerTimes);
		} else if (exchangeType.equals(eleven)) {
			intent.putExtra(Forex.FOREX_LOSERATE_RES, loseRate);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY1, startDateStr);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY2, endDateStr);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY3, custonerTimes);
		}
		// 外汇买卖交易参数
		intent.putExtra(Forex.FOREX_BCURRENCY_REQ, buyCodeUser);
		intent.putExtra(Forex.FOREX_SCURRENCYT_REQ, sellCodeUser);
		intent.putExtra(Forex.FOREX_SAMOUNT_REQ, balanceUser);
		intent.putExtra(Forex.FOREX_CASHREMIT_REQ, cashUser);
		intent.putExtra(Forex.FOREX_VOLUMENUMBER_REQ, volumeNumberUsers);
		intent.putExtra(Forex.FOREX_CDNUMBER_REQ, cdNumberUser);
		intent.putExtra(Forex.FOREX_CDTYPE_REQ, numberTyper);
		startActivity(intent);
	}

	/** 验证获利汇率、止损汇率 */
	private void checkTwoRate() {
		huoLiRate = huoLiRateText.getText().toString().trim();
		zhiSunRate = zhiSunRateText.getText().toString().trim();
		// 买入、卖出币种为日元或港元，小数点后4位
		if (spetialCodeList.contains(sellCodeUser) && spetialCodeList.contains(buyCodeUser)) {
			RegexpBean reb1 = new RegexpBean(getResources().getString(R.string.forex_trade_two_huoli1), huoLiRate, "spetialRate", true);
			RegexpBean reb2 = new RegexpBean(getResources().getString(R.string.forex_trade_two_zhisun1), zhiSunRate, "spetialRate", true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb1);
			lists.add(reb2);
			if (RegexpUtils.regexpDate(lists)) {
				checkTimes();
			} else {
				return;
			}
		} else {
			// 卖出币种为特殊币种
			if (tradeCheckCodeNoNumber.contains(sellCodeUser) || tradeCheckCodeNoNumber.contains(buyCodeUser)) {
				RegexpBean reb1 = new RegexpBean(getResources().getString(R.string.forex_trade_two_huoli1), huoLiRate, "spetialLimitRate", true);
				RegexpBean reb2 = new RegexpBean(getResources().getString(R.string.forex_trade_two_zhisun1), zhiSunRate, "spetialLimitRate", true);
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(reb1);
				lists.add(reb2);
				if (RegexpUtils.regexpDate(lists)) {
					checkTimes();
				} else {
					return;
				}
			} else {
				RegexpBean reb1 = new RegexpBean(getResources().getString(R.string.forex_trade_two_huoli1), huoLiRate, "limitRate", true);
				RegexpBean reb2 = new RegexpBean(getResources().getString(R.string.forex_trade_two_zhisun1), zhiSunRate, "limitRate", true);
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(reb1);
				lists.add(reb2);
				if (RegexpUtils.regexpDate(lists)) {
					checkTimes();
				} else {
					return;
				}
			}
		}
	}

	/** 下一步------ 查询汇率 */
	private void getRate(String buyCode, String sellCode) {
		if (buyCode.equals(sellCode)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexQuickTradeSubmitActivity.this.getString(R.string.forex_trade_sellBuyCode));
			return;
		} else {
			if (StringUtil.isNullOrEmpty(exchangeType) || StringUtil.isNullOrEmpty(buyCodeUser) || StringUtil.isNullOrEmpty(sellCodeUser)) {
				return;
			}
			// 市价交易
			tradePsnForexTradePre(exchangeType, buyCodeUser, sellCodeUser);
		}
	}

	/**
	 * 买入卖出币种不在使用通讯 处理买入卖出币种，初始化页面数据
	 */
	private void getInitData() {
		getVolumeNumberList();
		dealVolumberAndCDNumber();
		initSpinnerData();
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestSystemDateTime();
	}

	// TODO ------------------------控件事件------------------------------//

	public void initClick() {
		// 下一步
		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checkDate(exchangeType);
			}
		});
		// 直接成交按钮
		quickTradeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String buyCodeName = LocalData.Currency.get(buyCodeUser);
				String sellCodeName = LocalData.Currency.get(sellCodeUser);
				if (buyCodeName.equals(sellCodeName)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(ForexQuickTradeSubmitActivity.this.getString(R.string.forex_trade_sellBuyCode));
					return;
				} else {
					if (StringUtil.isNullOrEmpty(exchangeType) || StringUtil.isNullOrEmpty(buyCodeUser) || StringUtil.isNullOrEmpty(sellCodeUser)) {
						return;
					}
					// 请求conversationId
					BaseHttpEngine.showProgressDialog();
					requestCommConversationId();
				}
			}
		});
		startTimes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Calendar c = Calendar.getInstance(); // 实例化一个日期与时间操作对象
				QueryDateUtils.checkDate(startTimes, c);
				// 第二个参数为用户选择设置按钮后的响应事件
				// 最后的三个参数为缺省显示的年度，月份，及日期信息 DatePickerDialog
				DatePickerDialog dialog = new DatePickerDialog(ForexQuickTradeSubmitActivity.this, new OnDateSetListener() {
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

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		switch (parent.getId()) {
		case R.id.forex_rate_currency_type:// 交易方式
			choiceExchangeType(position);
			index = position;// 标记交易方式
			String buyCodeName4 = LocalData.Currency.get(buyCodeUser);
			String sellCodeName4 = LocalData.Currency.get(sellCodeUser);
			if (buyCodeName4.equals(sellCodeName4)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(ForexQuickTradeSubmitActivity.this.getString(R.string.forex_trade_sellBuyCode));
				fixRateText.setText("");
				return;
			}
			if (StringUtil.isNullOrEmpty(exchangeType) || StringUtil.isNullOrEmpty(buyCodeUser) || StringUtil.isNullOrEmpty(sellCodeUser)) {
				return;
			}
			if (!isInitType) {
				clearRate();
				if (exchangeType.equals(three) || exchangeType.equals(four) || exchangeType.equals(five) || exchangeType.equals(eleven)) {
					BaseHttpEngine.showProgressDialog();
					currentRateView.setVisibility(View.VISIBLE);
					requestPsnForexTradePre(eight, buyCodeUser, sellCodeUser);
				} else {
					// 查询汇率
					currentRateView.setVisibility(View.VISIBLE);
					BaseHttpEngine.showProgressDialog();
					requestPsnForexTradePre(exchangeType, buyCodeUser, sellCodeUser);
				}

			} else {
				isInitType = false;
			}
			break;
		case R.id.forex_rate_fix_accNumber:// 存折册号
			if (!isInitVolumber) {
				clearRate();
				volumeNumberUsers = volumeNumberSpinner.getSelectedItem().toString().trim();
				// 存折册号发生变化，存单号也要发生改变
				cdnumberTypeCodeList = getCDNumberList(volumeNumberUsers);
				if (cdnumberTypeCodeList == null || cdnumberTypeCodeList.size() == 0) {
					return;
				} else {
					cdAdapter = new ArrayAdapter<String>(ForexQuickTradeSubmitActivity.this, R.layout.dept_spinner, cdnumberTypeCodeList);
					cdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					cdNumberListSpinner.setAdapter(cdAdapter);
					cdNumberListSpinner.setSelection(0);
					sellTransCode = cdNumberListSpinner.getSelectedItem().toString().trim();
					int s1 = sellTransCode.indexOf(" ");
					int s2 = sellTransCode.lastIndexOf(" ");
					int sellLen = sellTransCode.length();
					// 存折单号
					cdNumberUser = sellTransCode.substring(0, s1).trim();
					// 卖出币种名称
					onlySellCodeDealUser = sellTransCode.substring(s2, sellLen).trim();
					getOthereData(volumeNumberUsers, cdNumberUser);
					String buyCodeName = LocalData.Currency.get(buyCodeUser);
					String sellCodeName = LocalData.Currency.get(sellCodeUser);
					if (buyCodeName.equals(sellCodeName)) {
						BaseDroidApp.getInstanse().showInfoMessageDialog(ForexQuickTradeSubmitActivity.this.getString(R.string.forex_trade_sellBuyCode));
						return;
					}
					if (StringUtil.isNull(exchangeType) || StringUtil.isNull(buyCodeUser) || StringUtil.isNull(sellCodeUser)) {
						return;
					}
					// 存折册号发生变化，存单号就一定会跟着变化，存折册号可以不用查询汇率
					// 查询汇率
					// BaseHttpEngine.showProgressDialog();
					// requestPsnForexTradePre(exchangeType, buyCodeUser,
					// sellCodeUser);
				}
			} else {
				isInitVolumber = false;
			}

			break;
		case R.id.forex_rate_fix_number:// 存单号
			sellTransCode = cdNumberListSpinner.getSelectedItem().toString().trim();
			int s1 = sellTransCode.indexOf(" ");
			int s2 = sellTransCode.lastIndexOf(" ");
			int sellLen = sellTransCode.length();
			// 存折单号
			cdNumberUser = sellTransCode.substring(0, s1).trim();
			// 卖出币种名称
			onlySellCodeDealUser = sellTransCode.substring(s2, sellLen).trim();
			getOthereData(volumeNumberUsers, cdNumberUser);
			updateLimitHint();
			String buyCodeName = LocalData.Currency.get(buyCodeUser);
			String sellCodeName = LocalData.Currency.get(sellCodeUser);
			if (buyCodeName.equals(sellCodeName)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(ForexQuickTradeSubmitActivity.this.getString(R.string.forex_trade_sellBuyCode));
				fixRateText.setText("");
				return;
			}

			if (StringUtil.isNullOrEmpty(exchangeType) || StringUtil.isNullOrEmpty(buyCodeUser) || StringUtil.isNullOrEmpty(sellCodeUser)) {
				return;
			}

			if (!isInitCDNumber) {
				// 查询汇率
				clearRate();
				if (exchangeType.equals(three) || exchangeType.equals(four) || exchangeType.equals(five) || exchangeType.equals(eleven)) {
					BaseHttpEngine.showProgressDialog();
					requestPsnForexTradePre(eight, buyCodeUser, sellCodeUser);
					currentRateView.setVisibility(View.VISIBLE);
				} else {
					// 查询汇率
					currentRateView.setVisibility(View.VISIBLE);
					BaseHttpEngine.showProgressDialog();
					requestPsnForexTradePre(exchangeType, buyCodeUser, sellCodeUser);
				}
			} else {
				isInitCDNumber = false;
			}

			break;
		case R.id.forex_rate_currency_buylCode:// 买入币种
			buyCodeUser = buyCodeList.get(position);
			buyTransCode = buyCodeSpinner.getSelectedItem().toString().trim();
			updateLimitHint();
			String buyCodeName1 = LocalData.Currency.get(buyCodeUser);
			String sellCodeName1 = LocalData.Currency.get(sellCodeUser);
			if (buyCodeName1.equals(sellCodeName1)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(ForexQuickTradeSubmitActivity.this.getString(R.string.forex_trade_sellBuyCode));
				fixRateText.setText("");
				return;
			}

			if (StringUtil.isNull(exchangeType) || StringUtil.isNull(buyCodeUser) || StringUtil.isNull(sellCodeUser)) {
				return;
			}

			if (!isInitBuyCode) {
				clearRate();
				// 查询汇率
				if (exchangeType.equals(three) || exchangeType.equals(four) || exchangeType.equals(five) || exchangeType.equals(eleven)) {
					BaseHttpEngine.showProgressDialog();
					requestPsnForexTradePre(eight, buyCodeUser, sellCodeUser);
					currentRateView.setVisibility(View.VISIBLE);
				} else {
					// 查询汇率
					currentRateView.setVisibility(View.VISIBLE);
					BaseHttpEngine.showProgressDialog();
					requestPsnForexTradePre(exchangeType, buyCodeUser, sellCodeUser);
				}
			} else {
				isInitBuyCode = false;
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
	
	// TODO ------------------------网络回调------------------------------//

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.canGoBack = false;
//		currenttime = dateTime;
		startDateStr = QueryDateUtils.getcurrentDate(dateTime);
		// int hours = Integer.valueOf(QueryDateUtils.getHours(dateTime));
		getPosition(0);
		if (StringUtil.isNull(exchangeType) || StringUtil.isNull(buyCodeUser) || StringUtil.isNull(sellCodeUser)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		requestPsnForexTradePre(exchangeType, buyCodeUser, sellCodeUser);
	}

	/**
	 * 查询当前汇率--初始化使用
	 */
	private void requestPsnForexTradePre(String exchangeType, String bCurrency, String sCurrency) {
		Map<String, Object> map = new Hashtable<String, Object>();
		map.put(Forex.FOREX_EXCHANGETYPE_REQ, exchangeType);
		map.put(Forex.FOREX_BCURRENC_REQ, bCurrency);
		map.put(Forex.FOREX_SCURRENCY_REQ, sCurrency);
		getHttpTools().requestHttp(Forex.FOREX_PSNFOREXTREDEPRE_API, "requestPsnForexTradePreCallback", map, true);
	}

	/**
	 * 查询当前汇率--回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnForexTradePreCallback(Object resultObj) {
		String rate = getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(rate)) {
			return;
		}
		if (StringUtil.isNullOrEmpty(rate)) {
			return;
		} else {
			LogGloble.d(TAG + " rate", rate);
			fixRateText.setText(rate);
		}
		
		if (exchangeType.equals(eleven)) {
			BaseHttpEngine.showProgressDialog();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(Forex.FOREX_BCURRENC_REQ, buyCodeUser);
			params.put(Forex.FOREX_SCURRENCY_REQ, sellCodeUser);
			getHttpTools().requestHttp(Forex.PSNFOREXPENDINGSETRANGEQUERY, "requestPsnForexPendingSetRangeQueryCallBack", params);
		} else {
			BaseHttpEngine.dissMissProgressDialog();
		}
	}

	/**
	 * @外汇买卖预交易-----查询当前汇率
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
	 * @外汇买卖预交易--查询当前汇率--回调
	 * 
	 * @param resultObj
	 */
	public void tradePsnForexTradePreCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		String rate = getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(rate)) {
			return;
		} else {
			LogGloble.d(TAG + " rate", rate);
			Intent intent = new Intent(ForexQuickTradeSubmitActivity.this, ForexQuickTradeConfirmActivity.class);
			// 页面显示的内容
			intent.putExtra(ConstantGloble.FOREX_CHANGETYPE_KEY, exchangeTypeTrans);
			intent.putExtra(Forex.FOREX_EXCHANGETYPE_REQ, exchangeType);
			intent.putExtra(Forex.FOREX_VOLUMENUMBER_REQ, volumeNumberUsers);
			intent.putExtra(ConstantGloble.FOREX_SELLTRANSCODE_KEY, sellTransCode);
			intent.putExtra(ConstantGloble.FOREX_BUYCODESP_KEY, buyTransCode);
			intent.putExtra(ConstantGloble.FOREX_CURRENTRATE_KEY, rate);
			intent.putExtra(ConstantGloble.FOREX_ONLYSELLCODEDEALUSER_KEY, onlySellCodeDealUser);
			// 限价才进行
			String tag2 = LocalData.forexTradeStyleTransList.get(1);// 07
			if (exchangeType.equals(tag2)) {
				intent.putExtra(ConstantGloble.FOREX_LIMITRATE_KEY, limitRate);
			}
			// 外汇买卖交易参数
			intent.putExtra(Forex.FOREX_BCURRENCY_REQ, buyCodeUser);
			intent.putExtra(Forex.FOREX_SCURRENCYT_REQ, sellCodeUser);
			intent.putExtra(Forex.FOREX_SAMOUNT_REQ, balanceUser);
			intent.putExtra(Forex.FOREX_CASHREMIT_REQ, cashUser);
			intent.putExtra(Forex.FOREX_VOLUMENUMBER_REQ, volumeNumberUsers);
			intent.putExtra(Forex.FOREX_CDNUMBER_REQ, cdNumberUser);
			intent.putExtra(Forex.FOREX_CDTYPE_REQ, numberTyper);

			startActivity(intent);
		}
	}

	/**
	 * @直接成交--请求ConversationId--回调
	 */
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		if (StringUtil.isNull(commConversationId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			if (StringUtil.isNull(exchangeType) || StringUtil.isNull(buyCodeUser) || StringUtil.isNull(sellCodeUser)) {
				BaseHttpEngine.dissMissProgressDialog();
				return;
			}
			quickPsnForexTradePre(exchangeType, buyCodeUser, sellCodeUser);
		}
	}

	/**
	 * 直接成交 ---查询汇率
	 * 
	 */
	private void quickPsnForexTradePre(String exchangeType, String bCurrency, String sCurrency) {
		Map<String, Object> map = new Hashtable<String, Object>();
		map.put(Forex.FOREX_EXCHANGETYPE_REQ, exchangeType);
		map.put(Forex.FOREX_BCURRENC_REQ, bCurrency);
		map.put(Forex.FOREX_SCURRENCY_REQ, sCurrency);
		getHttpTools().requestHttp(Forex.FOREX_PSNFOREXTREDEPRE_API, "quickPsnForexTradePreCallback", map, true);
	}

	/**
	 * @直接成交 --查询当前汇率--回调
	 * 
	 * @param resultObj
	 */
	public void quickPsnForexTradePreCallback(Object resultObj) {
		quickrate = getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNull(quickrate)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			requestPSNGetTokenId();
		}
	}

	/**
	 * @直接成交---- 获取tocken
	 */
	private void requestPSNGetTokenId() {
		getHttpTools().requestHttp(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API, "requestPSNGetTokenIdCallback", null, true);
	}

	/**
	 * @直接成交--- 获取tokenId----回调
	 * @param resultObj
	 *            :返回结果
	 */
	public void requestPSNGetTokenIdCallback(Object resultObj) {
		tokenId = getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNull(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			LogGloble.d(TAG + " tokenId", tokenId);
			if (StringUtil.isNull(investAccount)) {
				BaseHttpEngine.dissMissProgressDialog();
				return;
			} else {
				Map<String, Object> params = getParams();
				getHttpTools().requestHttp(Forex.FOREX_PSNFOREXTRADE_API, "requestPsnForexTradeCallback", params, true);
			}
		}
	}

	/**
	 * @直接成交------ 外汇买卖确认提交--回调
	 * @param resultObj
	 */
	public void requestPsnForexTradeCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> tradeResult = getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(tradeResult)) {
			return;
		} else {
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_TRADERESULT_KEY, tradeResult);
			Intent intent = new Intent(ForexQuickTradeSubmitActivity.this, ForexQuickTradeSuccessActivity.class);
			intent.putExtra(Forex.FOREX_VOLUMENUMBER_REQ, volumeNumberUsers);// 存折册号
			intent.putExtra(Forex.FOREX_CDNUMBER_REQ, cdNumberUser);// 存单号
			intent.putExtra(ConstantGloble.FOREX_ONLYSELLCODEDEALUSER_KEY, onlySellCodeDealUser);// 卖出币种名称
			intent.putExtra(Forex.FOREX_SCURRENCYT_REQ, sellCodeUser);// 卖出币种代码
			intent.putExtra(ConstantGloble.FOREX_BUYCODESP_KEY, buyTransCode);// 买入币种名称
			intent.putExtra(Forex.FOREX_SAMOUNT_REQ, balanceUser);// 可用余额
			intent.putExtra(ConstantGloble.FOREX_CHANGETYPE_KEY, exchangeTypeTrans);// 交易方式
			intent.putExtra(Forex.FOREX_CASHREMIT_REQ, cashUser);// 钞汇
			intent.putExtra(Forex.FOREX_EXCHANGETYPE_REQ, exchangeType);
			intent.putExtra(Forex.FOREX_LOSERATE_RES, loseRate);// 追击点差
			// quickrate
			startActivity(intent);
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
