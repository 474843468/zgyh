package com.chinamworld.bocmbci.biz.lsforex.trade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
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
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.biz.lsforex.query.IsForexHistoryQueryActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IntentSpanLine;

/** 双向宝交易 填写信息页面 */
public class IsForexTradeSubmitActivity extends IsForexBaseActivity implements OnItemSelectedListener, OnClickListener {

	public static final String TAG = "IsForexTradeSubmitActivity";
	/** IsForexTradeSubmitActivity的主布局 */
	private View rateInfoView = null;
	/** 返回按钮 */
	private Button backButton = null;
	/** 交易方式 */
	private Spinner typeSpinner = null;
	/** 货币对 */
	private Spinner codeSpinner = null;
	/** 结算币种 */
	private Spinner jsCodeSpinner = null;
	/** 买卖方向 */
	private Spinner sellTagSpinner = null;
	/** 建仓标志 */
	private Spinner jcTagSpinner = null;
	/** 当前汇率 */
	private TextView currentRateText = null;
	/** 限价汇率 */
	private EditText limitRateText = null;
	/** 交易金额 */
	private EditText moneyText = null;
	/** 直接成交 */
	private Button tradeButton = null;
	/** 下一步 */
	private Button nextButton = null;
	/** 限价汇率区域 */
	private View limitRateView = null;
	/** 建仓标志区域 */
	private View jcTagView = null;
	/** 请求标志 */
	private int requestTag = -1;
	/** 处理后---结算币种代码 */
	private List<String> vfgRegDealCodeList = null;
	/** 处理后---结算币种代码名称 */
	private List<String> vfgRegDealCodeNameList = null;
	/** 处理后---源货币对代码 */
	private List<String> sourceCodeDealCodeList = null;
	/** 处理后---目标货币对代码 */
	private List<String> targetCodeDealCodeList = null;
	/** 处理后---货币对代码名称 */
	private List<String> codeDealCodeNameList = null;
	/** 处理后---货币对代码 */
	private List<String> codeDealCodeList = null;
	/** 交易方式代码 */
	private String exchangeTypeCode = null;
	/** 结算币种代码 */
	private String jsCodeCode = null;
	/** 买卖方向代码 */
	private String sellTageCode = null;
	/** 货币对代码 */
	private String codeCode = null;
	/** 建仓标志代码 */
	private String jcTagCode = null;
	/** 当前汇率 */
	private String rate = null;
	/** 交易金额 */
	private String money = null;
	/** 源货币对代码 */
	private String sourceCodeCode = null;
	/** 目标货币对代码 */
	private String targetCodeCode = null;
	/** 选中货币对名称 */
	private String codeCodeName = null;
	private Boolean isInitType = true, isInitCode = true, isInitJsCode = true, isInitSellTag = true, isInitJcTag = true;
	/** 限价汇率 */
	private String limitRate = null;
	/** 默认直接成交，2-下一步 */
	private int nextOrQuickTag = 1;
	private String token = null;
	/** 反向持仓标志 */
	private boolean showOpenPositionFlag = true;
	/** 1-快速交易，2-未平仓中的中指定平仓，3-我的双向宝，4-银行买买价  5.未平仓中指定平仓   wuhan*/
	private int tradeTag = 1;

	/** 平仓-货币对位置 */
	private int codePosition = -1;
	/** 平仓-结算币种位置 */
	private int settleCurrecnyPosition = -1;
	/** 平仓-买卖方向名称 */
	private String direction = null;
	/** 平仓-建仓标志 */
	// private String openPositionFlag = null;
	/** 平仓-委托类型 */
	private String exchangeTranTypePC = null;
	/** 我的双向宝-结算币种名称 */
	private String myJcCodeName = null;
	/** 我的双向宝-货币对名称 */
	private String myCodeCodeName = null;
	/** 我的双向宝--买卖方向 */
	private String myDirectionName = null;
	/** 我的双向宝---持仓余额 */
	private String myMoney = null;
	/** 未平仓查询-货币对名称 */
	private String pcCodeCodeName = null;
	/** 未平仓查询-结算币种名称 */
	private String pcJsCodeName = null;
	/** 未平仓查询--未平仓金额 */
	private String pcMoney = null;
	/** 银行买卖价---货币对名称 */
	private String rateCodeCodeName = null;
	/** 银行买卖价---买卖方向 */
	private String rateDirectionName = null;
	/** 平仓金额 */
	private String tradeMoney = null;
	/** 平仓---货币对 */
	private String codeName = null;

	/** 委托汇率View */
	private View weiTuoRateView = null;
	/** 截止时间---年月日View */
	private View startTimesView = null;
	private View endTimesView = null;
	/** 获利汇率View */
	private View huoLiRateView = null;
	/** 止损汇率View */
	private View zhiSunRateView = null;
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
	/** 系统当前时间格式化 年月日 时分秒 */
	private String currenttime = null;
	private String currentHours = null;
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
	private String shiJiaCode = null;
	private String xianJiaCode = null;
	private String weiTuoCode = null;
	private String zhiSunCode = null;
	private String twoCode = null;
	
	private View messageView = null;
	
	////////////////////////////
	/** 市场汇率（市价即时） */
	private String macketRate = null;
	/** 优惠/惩罚点差 */
	private String offSet = null;
	/** 优惠/惩罚点差 */
	private String gprType = null;
	
	//wuhan P603  我的双向宝详情中进入的追击止损交易方式
	private View zhijizhisun_layout;
	private EditText forex_zhuijidiancha;
	private TextView fores_dianchafanwei;
	private String zhuijizhisunCode= null;
	String maxPendingSet= null;
	String minPendingSet = null;
	String dianCha = null;
	private int Mposition ;
	
	
	
	//P03反显
	private View lyt_common;
	private View lyt_gone;
	
	/** 交易方式 */
	private Spinner typeSpinner_gone = null;
	private TextView forex_rate_typeName_gone = null;
	/** 货币对 */
	private Spinner codeSpinner_gone = null;
	/** 结算币种 */
	private Spinner jsCodeSpinner_gone = null;
	/** 买卖方向 */
	private Spinner sellTagSpinner_gone = null;
	/** 建仓标志 */
	private Spinner jcTagSpinner_gone = null;
	/** 当前汇率 */
	private TextView currentRateText_gone = null;
	/** 限价汇率 */
	private EditText limitRateText_gone = null;
	private TextView limitRateTextName_gone = null;
	/** 交易金额 */
	private EditText moneyText_gone = null;
	/** 限价汇率区域 */
	private View limitRateView_gone = null;
	/** 建仓标志区域 */
	private View jcTagView_gone = null;
	/** 委托汇率View */
//	private View weiTuoRateView_gone = null;
	/** 截止时间---年月日View */
//	private View startTimesView_gone = null;
//	private View endTimesView_gone = null;
	/** 获利汇率View */
//	private View huoLiRateView_gone = null;
	/** 止损汇率View */
//	private View zhiSunRateView_gone = null;
	/** 委托汇率 */
	private TextView weiTuoRateText_gone = null;
	/** 年月日 */
	private TextView startTimes_gone = null;
	/** 时 */
	private Spinner endTimesSpinner_gone = null;
	/** 获利汇率 */
	private TextView huoLiRateText_gone = null;
	/** 止损汇率 */
	private TextView zhiSunRateText_gone= null;
	private View messageView_gone = null;
	private EditText forex_zhuijidiancha_gone;
	private TextView fores_dianchafanwei_gone;
	private View currentRateView_gone = null;
	private TextView textview_query_pingcang;
	private View jcTag_code_view ;//原开仓编号
	private TextView isForex_jcCode;
	private String tradeBackground;
	private String vfgType = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.isForex_trade));
		LogGloble.d(TAG, "onCreate");
		backButton = (Button) findViewById(R.id.ib_back);
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
		getTypeCode();
		init();
		detalRequestTag();
	}

	private void getTypeCode() {
		shiJiaCode = LocalData.isForexExchangeTypeCodeList.get(0);
		xianJiaCode = LocalData.isForexExchangeTypeCodeList.get(1);
		weiTuoCode = LocalData.isForexExchangeTypeCodeList.get(2);
		zhiSunCode = LocalData.isForexExchangeTypeCodeList.get(3);
		twoCode = LocalData.isForexExchangeTypeCodeList.get(4);
		zhuijizhisunCode = LocalData.isForexExchangeTypeCodeList.get(7);		
		
	}


	/** 用于区分是谁发送的请求 */
	private void detalRequestTag() {
		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		
		
		requestTag = intent.getIntExtra(ConstantGloble.ISFOREX_REQUEXTTRADE_KEY, -1);
		String codeN = intent.getStringExtra(ConstantGloble.ISFOREX_CODECODENAME_KEY);
		if(codeN!=null && codeN.contains("/")){
			String codes[] = codeN.split("/");
			String sourceCurrency = codes[0];
			String targetCurrency = codes[1];
//			人民币金、银、铂、钯、美元金、银、铂、钯   贵金属的币种
			if(LocalData.goldLists.contains(sourceCurrency)||LocalData.goldLists.contains(targetCurrency)){
				vfgType = "G";
			}else {
				vfgType = "F";
			}
		}
		
		switch (requestTag) {
		case ConstantGloble.ISFOREX_MYRATE_TRADE_ACTIVITY:// 我的外汇双向宝 快速交易101
			tradeTag = 1;
			break;
		case ConstantGloble.ISFOREX_MINE_RATE_ACTIVITY:// 行情页面--银行买卖价
			tradeTag = 4;
			rateCodeCodeName = intent.getStringExtra(ConstantGloble.ISFOREX_CODECODENAME_KEY);
			rateDirectionName = intent.getStringExtra(ConstantGloble.ISFOREX_DIRECTION_KEY);
			break;
		case ConstantGloble.ISFOREX_WPC_TRADE_ACTIVITY:// 平仓按钮
			//未平仓页面中的指定平仓  wuhan   //只有限价即时，和 市价即时次易
			Mposition = intent.getIntExtra("position", 0);
			tradeTag = 2;
			jcTagView.setVisibility(View.GONE);
			
			queryTag = intent.getIntExtra("queryTag", 1);
			pcCodeCodeName = intent.getStringExtra(ConstantGloble.ISFOREX_CODECODENAME_KEY);//货币对
			pcJsCodeName = intent.getStringExtra(ConstantGloble.ISFOREX_SETTLECURRECNY_KEY);//结算
			direction = intent.getStringExtra(ConstantGloble.ISFOREX_DIRECTION_KEY);
			pcMoney = intent.getStringExtra(IsForex.ISFOREX_UNCLOSEDBALANCE_REQ);
			tradeMoney = pcMoney;
			codeName = pcCodeCodeName;
			
			
			//P603 get原建仓编号：
			String jcCode = intent.getStringExtra("jcCode");
			isForex_jcCode.setText(jcCode);
			
			tradeBackground = intent.getStringExtra("tradeBackground");
			if(tradeBackground!=null && !"".equals(tradeBackground)){
				jcTagView.setVisibility(View.VISIBLE);
			}else {
				jcTagView.setVisibility(View.GONE);
			}
			if(queryTag == 10 ||tradeTag ==2){
				forex_rate_typeName_gone.setTextColor(getResources().getColor(R.color.black));
				typeSpinner_gone.setEnabled(true);
				limitRateTextName_gone.setTextColor(getResources().getColor(R.color.black));
				limitRateText_gone.setEnabled(true);
			}else{
				forex_rate_typeName_gone.setTextColor(getResources().getColor(R.color.gray));
				typeSpinner_gone.setEnabled(false);
				limitRateTextName_gone.setTextColor(getResources().getColor(R.color.gray));
				limitRateText_gone.setEnabled(false);
			}
			exchangeTranTypePC = intent.getStringExtra(IsForex.ISFOREX_EXCHANGETRANTYPE_REQ);
			if (exchangeTranTypePC.equals(LocalData.isForexExchangeTypeCodeList.get(1))) {
				limitRateView.setVisibility(View.VISIBLE);
				limitRateText.requestFocus();
			}
			
			
			
			break;
		case ConstantGloble.ISFOREX_WPC_TRADE_XIANKAIXIANPING_ACTIVITY:
//			未平仓中的   先开先平按钮
			tradeTag = 5;
			
			break;
		case ConstantGloble.ISFOREX_MINE_TRADE_ACTIVITY:// 301 我的双向宝详情页面的平仓按钮
			tradeTag = 3;
			myJcCodeName = intent.getStringExtra(ConstantGloble.ISFOREX_SETTLECURRECNY_KEY);
			myCodeCodeName = intent.getStringExtra(ConstantGloble.ISFOREX_CODECODENAME_KEY);
			myDirectionName = intent.getStringExtra(ConstantGloble.ISFOREX_DIRECTION_KEY);
			myMoney = intent.getStringExtra(IsForex.ISFOREX_BALANCE_RES);
			tradeMoney = myMoney;
			codeName = myCodeCodeName;
			
			
			
			
			break;

		default:
			break;
		}
		codeResultList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.ISFOREX_CODERESULTLIST_KEY);
		if (StringUtil.isNullOrEmpty(codeResultList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					IsForexTradeSubmitActivity.this.getResources().getString(R.string.isForex_trade_code));
			return;
		}

		vfgRegCurrencyList = (List<String>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.ISFOREX_VFGREGCURRENCYLISTTLIST_KEY);
		if (StringUtil.isNullOrEmpty(vfgRegCurrencyList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					IsForexTradeSubmitActivity.this.getResources().getString(R.string.isForex_trade_jcCode));
			return;
		}
		getDate();
	}

	private void init() {
		rateInfoView = LayoutInflater.from(IsForexTradeSubmitActivity.this).inflate(R.layout.isforex_myrate_trade_submit, null);
		tabcontent.addView(rateInfoView);
		typeSpinner = (Spinner) findViewById(R.id.forex_rate_type);
		typeSpinner.setOnItemSelectedListener(this);
		limitRateText = (EditText) findViewById(R.id.forex_trade_limiRate_Edit);
		codeSpinner = (Spinner) findViewById(R.id.forex_rate_currency_sellCode);
		codeSpinner.setOnItemSelectedListener(this);
		currentRateText = (TextView) findViewById(R.id.forex_rate_currency_rate);
		jsCodeSpinner = (Spinner) findViewById(R.id.forex_rate_currency_buylCode);
		jsCodeSpinner.setOnItemSelectedListener(this);
		sellTagSpinner = (Spinner) findViewById(R.id.isForex_query_sellTag);
		sellTagSpinner.setOnItemSelectedListener(this);
		jcTagSpinner = (Spinner) findViewById(R.id.isForex_query_openPositionFlag);
		jcTagSpinner.setOnItemSelectedListener(this);
		moneyText = (EditText) findViewById(R.id.isForex_myRate_tradeMoney);
		tradeButton = (Button) findViewById(R.id.trade_quickButton);
		tradeButton.setOnClickListener(this);
		nextButton = (Button) findViewById(R.id.trade_nextButton);
		nextButton.setOnClickListener(this);
		limitRateView = findViewById(R.id.trade_limiRate);
		weiTuoRateView = findViewById(R.id.weiTuoHuiLv_layout);
		huoLiRateView = findViewById(R.id.twoHuoLi_layout);
		zhiSunRateView = findViewById(R.id.two_zhisun_layout);
		startTimesView = findViewById(R.id.weiTuoTimes1_layout);
		endTimesView = findViewById(R.id.weiTuoTimes2_layout);
		currentRateView = findViewById(R.id.current_rate_layout);
		jcTagView = findViewById(R.id.jcTag_view);
		messageView = findViewById(R.id.message);
		//追击止损  wuhan
		zhijizhisun_layout = findViewById(R.id.zhijizhisun_layout);
		fores_dianchafanwei = (TextView) findViewById(R.id.fores_dianchafanwei);
		forex_zhuijidiancha = (EditText) findViewById(R.id.forex_zhuijidiancha);
		
		
		//P603反显
		lyt_common = findViewById(R.id.lyt_common);
		lyt_gone = findViewById(R.id.lyt_gone);
		typeSpinner_gone =  (Spinner) findViewById(R.id.forex_rate_type_gone);
		typeSpinner_gone.setOnItemSelectedListener(this);
		forex_rate_typeName_gone = (TextView) findViewById(R.id.forex_rate_typeName_gone);
		limitRateText_gone = (EditText) findViewById(R.id.forex_trade_limiRate_Edit_gone);
		limitRateTextName_gone = (TextView)findViewById(R.id.acc_financeic_trans_icact_gone);
		codeSpinner_gone = (Spinner) findViewById(R.id.forex_rate_currency_sellCode_gone);
		currentRateText_gone = (TextView) findViewById(R.id.forex_rate_currency_rate_gone);
		jsCodeSpinner_gone = (Spinner) findViewById(R.id.forex_rate_currency_buylCode_gone);
		sellTagSpinner_gone = (Spinner) findViewById(R.id.isForex_query_sellTag_gone);
		jcTagSpinner_gone = (Spinner) findViewById(R.id.isForex_query_openPositionFlag_gone);
		jcTagSpinner_gone.setOnItemSelectedListener(this);
		moneyText_gone = (EditText) findViewById(R.id.isForex_myRate_tradeMoney_gone);
		limitRateView_gone = findViewById(R.id.trade_limiRate_gone);
		currentRateView_gone = findViewById(R.id.current_rate_layout_gone);
		jcTagView_gone = findViewById(R.id.jcTag_view_gone);
		messageView_gone = findViewById(R.id.message_gone);
		textview_query_pingcang = (TextView) findViewById(R.id.textview_query_pingcang);
		SpannableString sp = new SpannableString(this.getString(R.string.isForex_myRate_pingcang_query));
		final Intent userIntent = new Intent();
		int length = sp.toString().length();
		userIntent.setClass(IsForexTradeSubmitActivity.this, IsForexHistoryQueryActivity.class);
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_QUERYTAG, 10);
		sp.setSpan(new IntentSpanLine(new OnClickListener() {

			public void onClick(View view) {
				IsForexTradeSubmitActivity.this.startActivity(userIntent);
//				finish();
			}

		}), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);	
		textview_query_pingcang.setText(sp);
		textview_query_pingcang.setMovementMethod(LinkMovementMethod.getInstance());	
		
		jcTag_code_view = findViewById(R.id.jcTag_code_view);
		isForex_jcCode = (TextView) findViewById(R.id.isForex_jcCode);
		
		
		hideView();
		jcTag_code_view.setVisibility(View.GONE);
		weiTuoRateText = (TextView) findViewById(R.id.forex_wuituo_rate);
		huoLiRateText = (TextView) findViewById(R.id.forex_two_huoli);
		zhiSunRateText = (TextView) findViewById(R.id.forex_two_zhisun);
		startTimes = (TextView) findViewById(R.id.forex_query_deal_startdate);
		startTimes.setOnClickListener(this);
		endTimesSpinner = (Spinner) findViewById(R.id.forex_query_deal_enddate);
		endTimesSpinner.setOnItemSelectedListener(this);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,currentRateText_gone);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,currentRateText);
	}

	/** 隐藏区域 */
	private void hideView() {
//		P603
		lyt_common.setVisibility(View.VISIBLE);
		lyt_gone.setVisibility(View.GONE);
		currentRateView_gone.setVisibility(View.VISIBLE);
		messageView_gone.setVisibility(View.GONE);
		
		
		
		weiTuoRateView.setVisibility(View.GONE);
		huoLiRateView.setVisibility(View.GONE);
		zhiSunRateView.setVisibility(View.GONE);
		startTimesView.setVisibility(View.GONE);
		endTimesView.setVisibility(View.GONE);
		currentRateView.setVisibility(View.VISIBLE);
		messageView.setVisibility(View.GONE);
		
		//wuhan 追击止损
		zhijizhisun_layout.setVisibility(View.GONE);
		
		
	}
	/** 隐藏区域 */
	private void hideView_Gone() {
//		P603
		lyt_common.setVisibility(View.GONE);
		lyt_gone.setVisibility(View.VISIBLE);
		currentRateView_gone.setVisibility(View.VISIBLE);
		messageView_gone.setVisibility(View.GONE);
		
		
		
		weiTuoRateView.setVisibility(View.GONE);
		huoLiRateView.setVisibility(View.GONE);
		zhiSunRateView.setVisibility(View.GONE);
		startTimesView.setVisibility(View.GONE);
		endTimesView.setVisibility(View.GONE);
		currentRateView.setVisibility(View.VISIBLE);
		messageView.setVisibility(View.GONE);
		
		//wuhan 追击止损
		zhijizhisun_layout.setVisibility(View.GONE);
		
		
	}
	/** 处理数据 初始化数据 查询当前汇率 */
	private void getDate() {
		// 处理结算币种
		dealVfgRegDate();
		// 处理货币币
		dealCodeDate();
		initSpinnerDate();
		// 请求系统时间
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestSystemDateTime();
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.canGoBack = false;
		currenttime = dateTime;
		startDateStr = QueryDateUtils.getcurrentDate(dateTime);
		// int hours = Integer.valueOf(QueryDateUtils.getHours(dateTime));
		getPosition(0);
		if (StringUtil.isNull(jsCodeCode) || StringUtil.isNull(sellTageCode) || StringUtil.isNull(codeCode)
				|| StringUtil.isNull(exchangeTypeCode)) {
			return;
		}
		switch (tradeTag) {
		case 1:// 快速交易，需要查询反向建仓标志
				// 反向持仓标识判断
			requestPsnVFGPositionFlag(jsCodeCode, sellTageCode, codeCode);
			break;
		case 2:// 平仓按钮2-未平仓中的指定平仓，
			
			moneyText_gone.setText(pcMoney);
			requestPsnVFGTradeConfirm(jsCodeCode, sellTageCode, codeCode, exchangeTypeCode);
			break;
		case 3:// 我的双向宝
			moneyText.setText(myMoney);
			moneyText_gone.setText(myMoney);
			jcTagView.setVisibility(View.VISIBLE);
			requestPsnVFGTradeConfirm(jsCodeCode, sellTageCode, codeCode, exchangeTypeCode);
			break;
		case 4:// 银行买买价 
			requestPsnVFGPositionFlag(jsCodeCode, sellTageCode, codeCode);
			break;
		case 5:    //5.未平先开先平平仓   wuhan
			requestPsnVFGPositionFlag(jsCodeCode, sellTageCode, codeCode);
			break;
		default:
			break;
		}
	}

	private void getPosition(int number) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, LocalData.forexTimesList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		endTimesSpinner.setAdapter(adapter);
		for (int i = 0; i < 24; i++) {
			if (number == i) {
				endTimesSpinner.setSelection(i);
				endDateStr = endTimesSpinner.getSelectedItem().toString().trim();
			}
		}
	}

	/** 反向建仓----回调 */
	@Override
	public void requestPsnVFGPositionFlagCallback(Object resultObj) {
		super.requestPsnVFGPositionFlagCallback(resultObj);
		String tag = (String) BaseDroidApp.getInstanse().getBizDataMap().get(IsForex.ISFOREX_SHOWOPENPOSITIONFLAG_RES);
		showOpenPositionFlag = Boolean.valueOf(tag);
		if (showOpenPositionFlag) {
			// 显示建仓标志下拉框
			jcTagView.setVisibility(View.VISIBLE);
			
			if(exchangeTypeCode != null &&exchangeTypeCode.equals("FO")){
				LocalData.isForexExchangeTypeList.clear();
				LocalData.isForexExchangeTypeList.add("市价即时");
				LocalData.isForexExchangeTypeList.add("限价即时");
				LocalData.isForexExchangeTypeList.add("获利委托");
				LocalData.isForexExchangeTypeList.add("止损委托");
				LocalData.isForexExchangeTypeList.add("二选一委托");
				initTypeSpinner(0);
			}
		} else {
			jcTagView.setVisibility(View.GONE);
			jcTagCode = ConstantGloble.ISFOREX_JCTAG_Y;
				LocalData.isForexExchangeTypeList.clear();
				LocalData.isForexExchangeTypeList.add("市价即时");
				LocalData.isForexExchangeTypeList.add("限价即时");
				LocalData.isForexExchangeTypeList.add("获利委托");
				LocalData.isForexExchangeTypeList.add("止损委托");
				LocalData.isForexExchangeTypeList.add("二选一委托");
				if(exchangeTypeCode != null &&exchangeTypeCode.equals("FO")){
					initTypeSpinner(0);
				}
		}
		requestRate();
	}

	/** 处理结算币种数据 */
	private void dealVfgRegDate() {
		int len = vfgRegCurrencyList.size();
		vfgRegDealCodeList = new ArrayList<String>();
		vfgRegDealCodeNameList = new ArrayList<String>();
		for (int i = 0; i < len; i++) {
			String codes = vfgRegCurrencyList.get(i);
			if (!StringUtil.isNull(codes) && LocalData.Currency.containsKey(codes)) {
				vfgRegDealCodeList.add(codes);
				vfgRegDealCodeNameList.add(LocalData.Currency.get(codes));
			}
		}
	}

	/** 处理货币对 */
	private void dealCodeDate() {
		int len = codeResultList.size();
		sourceCodeDealCodeList = new ArrayList<String>();
		targetCodeDealCodeList = new ArrayList<String>();
		codeDealCodeNameList = new ArrayList<String>();
		codeDealCodeList = new ArrayList<String>();
		for (int i = 0; i < len; i++) {
			Map<String, Object> map = codeResultList.get(i);
			String sourceCurrencyCode = (String) map.get(IsForex.ISFOREX_SOURCECURRENCYCODE_RES);
			String targetCurrencyCode = (String) map.get(IsForex.ISFOREX_TARGETCURRENCYCODE_RES);
			if (!StringUtil.isNull(sourceCurrencyCode) && LocalData.Currency.containsKey(sourceCurrencyCode)
					&& !StringUtil.isNull(targetCurrencyCode) && LocalData.Currency.containsKey(targetCurrencyCode)) {
				sourceCodeDealCodeList.add(sourceCurrencyCode);
				targetCodeDealCodeList.add(targetCurrencyCode);
				String source = LocalData.Currency.get(sourceCurrencyCode);
				String target = LocalData.Currency.get(targetCurrencyCode);
				codeDealCodeList.add(sourceCurrencyCode + "/" + targetCurrencyCode);
				codeDealCodeNameList.add(source + "/" + target);
			}
		}
	}

	/** 得到交易方式的位置 */
	private int getTypePosition(String name) {
		int position = -1;
		int len = LocalData.isForexExchangeTypeList.size();
		for (int i = 0; i < len; i++) {
			if (LocalData.isForexExchangeTypeList.get(i).trim().equals(name)) {
				position = i;
				break;
			}
		}
		return position;
	}

	/**
	 * 得到货币对在货币对列表中的位置
	 * 
	 * @param codeName
	 *            :货币对名称
	 */
	private int getCodePosition(String codeName) {
		int position = -1;
		int len = codeDealCodeNameList.size();
		for (int i = 0; i < len; i++) {
			if (codeDealCodeNameList.get(i).trim().equals(codeName)) {
				position = i;
				break;
			}
		}
		return position;
	}

	/**
	 * 得到结算币种在结算币种列表中的位置
	 * 
	 * @param codeName
	 *            :结算币种名称
	 */
	private int getJcCodePosition(String codeName) {
		int position = -1;
		int len = vfgRegDealCodeNameList.size();
		for (int i = 0; i < len; i++) {
			if (vfgRegDealCodeNameList.get(i).equals(codeName)) {
				position = i;
				break;
			}
		}
		return position;
	}

	/** 得到买卖方向的position */
	private int getDirectionPosition(String driection) {
		int position = -1;
		int len = LocalData.isForexSellTagList.size();
		for (int i = 0; i < len; i++) {
			if (LocalData.isForexSellTagList.get(i).equals(driection)) {
				position = i;
				break;
			}
		}
		return position;
	}

	/** 得到建仓标志的position */
//	private int getJcTagPosition(String jcTag) {
//		int position = -1;
//		int len = LocalData.isForexJcTagList.size();
//		for (int i = 0; i < len; i++) {
//			if (LocalData.isForexJcTagList.get(i).trim().equals(jcTag)) {
//				position = i;
//				break;
//			}
//		}
//		return position;
//	}
	
	
	private int getJcTagPositions(String jcTag) {
		int position = -1;
		int len = LocalData.isForexJcTagzhuijizhisunList.size();
		for (int i = 0; i < len; i++) {
			if (LocalData.isForexJcTagzhuijizhisunList.get(i).trim().equals(jcTag)) {
				position = i;
				break;
			}
		}
		return position;
	}

	/** 对Spinner赋初始值 */
	private void initSpinnerDate() {
		if (vfgRegDealCodeList.size() <= 0 || vfgRegDealCodeList == null) {
			return;
		}
		if (vfgRegDealCodeNameList.size() <= 0 || vfgRegDealCodeNameList == null) {
			return;
		}
		if (codeDealCodeList.size() <= 0 || codeDealCodeList == null) {
			return;
		}
		if (codeDealCodeNameList.size() <= 0 || codeDealCodeNameList == null) {
			return;
		}
		switch (requestTag) {
		case ConstantGloble.ISFOREX_MYRATE_TRADE_ACTIVITY:// 快速交易 101
			// 交易方式    
			initTypeSpinner(0);
			initTypeSpinnerGone(0);
			// 货币对
			initCodeSpinner(0);
			initCodeSpinnerGone(0);
			// 结算币种
			initJsCodeSpinner(0);
			initJsCodeSpinnerGone(0);
			// 买卖方向
			initDirectionSpinner(0);
			initDirectionSpinnerGone(0);
			// 建仓标志  
//			initJcTagSpinner(LocalData.isForexJcTagList,0);
			initJcTagSpinner(LocalData.isForexJcTagzhuijizhisunList, 0);
			initJcTagSpinnerGone(0);
			break;
		case ConstantGloble.ISFOREX_MINE_RATE_ACTIVITY:// 银行买卖价
			// 交易方式
			initTypeSpinner(0);
			initTypeSpinnerGone(0);
			// 货币对
			int ratePosition = getCodePosition(rateCodeCodeName);
			if (ratePosition < 0) {
				ratePosition = 0;
			}
			initCodeSpinner(ratePosition);
			initCodeSpinnerGone(ratePosition);
			// 结算币种
			initJsCodeSpinner(0);
			initJsCodeSpinnerGone(0);
			// 买卖方向
			int rateDPositon = getDirectionPosition(rateDirectionName);
			if (rateDPositon < 0) {
				rateDPositon = 0;
			}
			initDirectionSpinner(rateDPositon);
			initDirectionSpinnerGone(rateDPositon);
			
			// 建仓标志  P603
//			initJcTagSpinner(LocalData.isForexJcTagList,0);
			initJcTagSpinner(LocalData.isForexJcTagzhuijizhisunList, 0);
			initJcTagSpinnerGone(0);
			break;

		case ConstantGloble.ISFOREX_WPC_TRADE_ACTIVITY:// 201 平仓按钮
			//未平仓中的 指定平仓按钮  
			// 交易方式   (只有市价即时，现价即时)
			int pcTPositon = getTypePosition(LocalData.isForexExchangeTypeCodeMap.get(exchangeTranTypePC).trim());
			if (pcTPositon < 0) {
				pcTPositon = 0;
			}
			initTypeSpinner(pcTPositon);
			initTypeSpinnerGone(pcTPositon);
			// 货币对
			int pcPosition = getCodePosition(pcCodeCodeName);
			if (pcPosition < 0) {
				pcPosition = 0;
			}
			initCodeSpinner(pcPosition);
			initCodeSpinnerGone(pcPosition);
			// 结算币种
			int pcJsPosition = getJcCodePosition(pcJsCodeName);
			if (pcJsPosition < 0) {
				pcJsPosition = 0;
			}
			initJsCodeSpinner(pcJsPosition);
			initJsCodeSpinnerGone(pcJsPosition);
			// 买卖方向
			int pcDPositon = getDirectionPosition(direction);
			if (pcDPositon < 0) {
				pcDPositon = 0;
			}
			initDirectionSpinner(pcDPositon);
			initDirectionSpinnerGone(pcDPositon);
			// 建仓标志  p603
//			initJcTagSpinner(LocalData.isForexJcTagList,1);
			int jcTagPosition = getJcTagPositions(tradeBackground);
//			initJcTagSpinner(LocalData.isForexJcTagzhuijizhisunList, jcTagPosition);
//			initJcTagSpinnerGone(jcTagPosition);
			initJcTagSpinner(LocalData.isForexJcTagzhuijizhisunList, 2);
			initJcTagSpinnerGone(2);
			break;
		case ConstantGloble.ISFOREX_WPC_TRADE_XIANKAIXIANPING_ACTIVITY:
//			未平仓页面中的先开先平  wuhan   
			// 交易方式
			initTypeSpinner(0);
			initTypeSpinnerGone(0);
			// 货币对
			initCodeSpinner(0);
			initCodeSpinnerGone(0);
			// 结算币种
			initJsCodeSpinner(0);
			initJsCodeSpinnerGone(0);
			// 买卖方向
			initDirectionSpinner(0);
			initDirectionSpinnerGone(0);
			// 建仓标志
//			initJcTagSpinner(LocalData.isForexJcTagList,0);
			initJcTagSpinner(LocalData.isForexJcTagzhuijizhisunList, 0);
			initJcTagSpinnerGone(0);
			break;
		case ConstantGloble.ISFOREX_MINE_TRADE_ACTIVITY:// 301 我的双向宝详情页面的平仓按钮
			// 交易方式   增加追击止损 FO
			initTypeSpinner(0);
			initTypeSpinnerGone(0);
			// 货币对
			int position = getCodePosition(myCodeCodeName.trim());
			if (position < 0) {
				position = 0;
			}
			initCodeSpinner(position);
			initCodeSpinnerGone(position);
			// 结算币种
			int jsPosition = getJcCodePosition(myJcCodeName);
			if (jsPosition < 0) {
				jsPosition = 0;
			}
			initJsCodeSpinner(jsPosition);
			initJsCodeSpinnerGone(jsPosition);
			// 买卖方向
			int dPosition = getDirectionPosition(myDirectionName);
			if (dPosition <= 0) {
				dPosition = 0;
			}
			initDirectionSpinner(dPosition);
			initDirectionSpinnerGone(dPosition);
			// 建仓标志  P603
			initJcTagSpinner(LocalData.isForexJcTagzhuijizhisunList, 1);
			initJcTagSpinnerGone(1);
			break;
		default:
			break;
		}

	}
	
	/** 初始化 建仓标志下拉列表框 */
	private void initJcTagSpinnerGone(int position) {
		ArrayAdapter<String> jcTagAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, LocalData.isForexJcTagzhuijizhisunList);
		jcTagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		jcTagSpinner_gone.setAdapter(jcTagAdapter);
		jcTagSpinner_gone.setSelection(position, true);
		jcTagCode = jcTagSpinner_gone.getSelectedItem().toString().trim();
//P603  //先开先平  指定平仓
		int positions =getJcTagPositions(jcTagCode);
		jcTagCode = LocalData.isForexJczhuijizhisunTag.get(positions);

	}
	
	
	/** 初始化 买卖方向下拉列表框 */
	private void initDirectionSpinnerGone(int position) {
		ArrayAdapter<String> sellTagAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner,
				LocalData.isForexSellTagList);
		sellTagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sellTagSpinner_gone.setAdapter(sellTagAdapter);
		sellTagSpinner_gone.setSelection(position);
		sellTagSpinner_gone.setEnabled(false);
		sellTageCode = LocalData.isForexSellTagCodeList.get(position);
		
		
	}
	ArrayAdapter<String> myJsCodeAdapters  =null;
	private void initJsCodeSpinnerGone(int jsPosition) {
		myJsCodeAdapters	= new ArrayAdapter<String>(this, R.layout.dept_spinner, vfgRegDealCodeNameList);
		myJsCodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		jsCodeSpinner_gone.setAdapter(myJsCodeAdapter);
		jsCodeSpinner_gone.setSelection(jsPosition);
		jsCodeSpinner_gone.setEnabled(false);
		jsCodeCode = vfgRegDealCodeList.get(jsPosition);
	}
	
	//P603
	private void initCodeSpinnerGone(int position){
		ArrayAdapter<String> myCodeAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, codeDealCodeNameList);
		myCodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		codeSpinner_gone.setAdapter(myCodeAdapter);
		codeSpinner_gone.setSelection(position);
		codeCode = codeDealCodeList.get(position);
		sourceCodeCode = sourceCodeDealCodeList.get(position);
		targetCodeCode = targetCodeDealCodeList.get(position);
		codeCodeName = codeSpinner_gone.getSelectedItem().toString().trim();
		if (limitRateView.isShown()) {
			limitRateText.requestFocus();
			updateLimitHint();
		}
		updateJyMoneyHint(sourceCodeCode);
		codeSpinner_gone.setEnabled(false);
	}
	
	

	private void initTypeSpinnerGone(int position){
		//P603
		ArrayAdapter<String> typeAdapters = new ArrayAdapter<String>(this, R.layout.dept_spinner
				,LocalData.isForexExchangeTypeList);
		typeAdapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		typeSpinner_gone.setAdapter(typeAdapters);
		typeSpinner_gone.setSelection(position);
		
		
	}
	
	/** 初始化交易方式下拉列表框 */
	private void initTypeSpinner(int position) {
		
		ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner,
				LocalData.isForexExchangeTypeList);
		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		typeSpinner.setAdapter(typeAdapter);
		typeSpinner.setSelection(position, true);
		exchangeTypeCode = LocalData.isForexExchangeTypeCodeList.get(position);
		
	}

	/** 初始化 货币对下拉列表框 */
	private void initCodeSpinner(int position) {
		ArrayAdapter<String> myCodeAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, codeDealCodeNameList);
		myCodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		codeSpinner.setAdapter(myCodeAdapter);
		codeSpinner.setSelection(position, true);
		codeCode = codeDealCodeList.get(position);
		sourceCodeCode = sourceCodeDealCodeList.get(position);
		targetCodeCode = targetCodeDealCodeList.get(position);
		codeCodeName = codeSpinner.getSelectedItem().toString().trim();
		if (limitRateView.isShown()) {
			limitRateText.requestFocus();
			updateLimitHint();
		}
		updateJyMoneyHint(sourceCodeCode);

	}
	ArrayAdapter<String> myJsCodeAdapter =null;
	/** 初始化 结算币种下拉列表框 */
	private void initJsCodeSpinner(int jsPosition) {
		myJsCodeAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, vfgRegDealCodeNameList);
		myJsCodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		jsCodeSpinner.setAdapter(myJsCodeAdapter);
		jsCodeSpinner.setSelection(jsPosition, true);
		jsCodeCode = vfgRegDealCodeList.get(jsPosition);
		
	}

	/** 初始化 买卖方向下拉列表框 */
	private void initDirectionSpinner(int position) {
		ArrayAdapter<String> sellTagAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner,
				LocalData.isForexSellTagList);
		sellTagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sellTagSpinner.setAdapter(sellTagAdapter);
		sellTagSpinner.setSelection(position, true);
		sellTageCode = LocalData.isForexSellTagCodeList.get(position);
		
	}

	/** 初始化 建仓标志下拉列表框 */
	private void initJcTagSpinner(List<String> isForexJcTagList,int position) {
		ArrayAdapter<String> jcTagAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, isForexJcTagList);
		jcTagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		jcTagSpinner.setAdapter(jcTagAdapter);
		jcTagSpinner.setSelection(position, true);
		jcTagCode = jcTagSpinner.getSelectedItem().toString().trim();
		//P603  //先开先平  指定平仓
		int positions =getJcTagPositions(jcTagCode);
		jcTagCode = LocalData.isForexJczhuijizhisunTag.get(positions);
//		if(isForexJcTagList.size()>2){
//			
//		}
	}

	/**
	 * 买卖预交易---查询汇率
	 * 
	 * @param currencyCode
	 *            :结算币种
	 * @param direction
	 *            :买卖方向
	 * @param currencyPairCode
	 *            :货币对
	 * @param tradeType
	 *            :交易类型
	 */
	private void requestPsnVFGTradeConfirm(String currencyCode, String direction, String currencyPairCode, String tradeType) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGTRADECONFIRM_API);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> map = new HashMap<String, String>();
		map.put(IsForex.ISFOREX_CURRENCYCODES_REQ, currencyCode);
		map.put(IsForex.ISFOREX_DIRECTIONS_REQ, direction);
		map.put(IsForex.ISFOREX_CURRENCYPAIRCODES_REQ, currencyPairCode);
		map.put(IsForex.ISFOREX_TRADETYPES_REQ, tradeType);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGTradeConfirmCallback");
	}

	/**
	 * 买卖预交易---查询汇率----回调 currentRateText赋值
	 */
	public void requestPsnVFGTradeConfirmCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		rate = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(rate)) {
			return;
		}
		currentRateText.setText(rate);
		currentRateText_gone.setText(rate);
	}
	
	// 查询结算币种----回调
	@Override
	public void requestPsnVFGGetRegCurrencyCallback(Object resultObj) {
		super.requestPsnVFGGetRegCurrencyCallback(resultObj);
		if (vfgRegCurrencyList == null || vfgRegCurrencyList.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					IsForexTradeSubmitActivity.this.getResources().getString(R.string.isForex_trade_jcCode));
			return;
		}
		vfgRegCurrencyList = (List<String>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.ISFOREX_VFGREGCURRENCYLISTTLIST_KEY);
		if (StringUtil.isNullOrEmpty(vfgRegCurrencyList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					IsForexTradeSubmitActivity.this.getResources().getString(R.string.isForex_trade_jcCode));
			return;
		}
		
		dealVfgRegDate();
		initJsCodeSpinner(0);
		initJsCodeSpinnerGone(0);
		myJsCodeAdapter.notifyDataSetChanged();
		myJsCodeAdapters.notifyDataSetChanged();
		requestPsnVFGPositionFlag(jsCodeCode, sellTageCode, codeCode);
	}
	@Override
	public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {
		switch (adapter.getId()) {
		case R.id.forex_rate_type:// 交易方式
			choiceExchangeType(position);
//			initTypeSpinnerGone(position);
//			typePosition = position;
			if (!isInitType) {
				if (StringUtil.isNull(jsCodeCode) || StringUtil.isNull(sellTageCode) || StringUtil.isNull(codeCode)
						|| StringUtil.isNull(exchangeTypeCode)) {
					return;
				}
				clearRate();
				BaseHttpEngine.showProgressDialog();
				requestRate();
			} else {
				isInitType = false;
			}
			break;
		case R.id.forex_rate_type_gone:
			// 未平仓交易查收返回来---->交易方式可编缉
			choiceExchangeType_Gone(position);
			initTypeSpinner(position);
			if (!isInitType) {
				if (StringUtil.isNull(jsCodeCode) || StringUtil.isNull(sellTageCode) || StringUtil.isNull(codeCode)
						|| StringUtil.isNull(exchangeTypeCode)) {
					return;
				}
				clearRate();
				BaseHttpEngine.showProgressDialog();
				requestRate();
			} else {
				isInitType = false;
			}
			
			break;
		case R.id.forex_rate_currency_sellCode:// 币对货
			codeCode = codeDealCodeList.get(position);
			initCodeSpinnerGone(position);
			sourceCodeCode = sourceCodeDealCodeList.get(position);
			targetCodeCode = targetCodeDealCodeList.get(position);
			codeCodeName = codeSpinner.getSelectedItem().toString().trim();
			String codes[] = codeCodeName.split("/");
			String sourceCurrency = codes[0];
			String targetCurrency = codes[1];
//			人民币金、银、铂、钯、美元金、银、铂、钯   贵金属的币种
			if(LocalData.goldLists.contains(sourceCurrency)||LocalData.goldLists.contains(targetCurrency)){
				vfgType = "G";
			}else {
				vfgType = "F";
			}
			updateLimitHint();
			updateJyMoneyHint(sourceCodeCode);
			initCodeSpinnerGone(position);
			if (!isInitCode) {
				if (StringUtil.isNull(jsCodeCode) || StringUtil.isNull(sellTageCode) || StringUtil.isNull(codeCode)
						|| StringUtil.isNull(exchangeTypeCode)) {
					return;
				}
				clearRate();
				BaseHttpEngine.showProgressDialog();
				// 查询结算币种 wuhan
				requestPsnVFGGetRegCurrency(vfgType);
//				requestPsnVFGPositionFlag(jsCodeCode, sellTageCode, codeCode);
			} else {
				isInitCode = false;
			}
			break;
		case R.id.forex_rate_currency_buylCode:// 结算币种
			jsCodeCode = vfgRegDealCodeList.get(position); 
			initJsCodeSpinnerGone(position);
			if (!isInitJsCode) {
				if (StringUtil.isNull(jsCodeCode) || StringUtil.isNull(sellTageCode) || StringUtil.isNull(codeCode)
						|| StringUtil.isNull(exchangeTypeCode)) {
					return;
				}
				clearRate();
				BaseHttpEngine.showProgressDialog();
				requestPsnVFGPositionFlag(jsCodeCode, sellTageCode, codeCode);

			} else {
				isInitJsCode = false;
			}
			break;
		case R.id.isForex_query_sellTag:// 买卖方向
			sellTageCode = LocalData.isForexSellTagCodeList.get(position);
			initDirectionSpinnerGone(position);
			if (!isInitSellTag) {
				if (StringUtil.isNull(jsCodeCode) || StringUtil.isNull(sellTageCode) || StringUtil.isNull(codeCode)
						|| StringUtil.isNull(exchangeTypeCode)) {
					return;
				}
				clearRate();
				BaseHttpEngine.showProgressDialog();
				requestPsnVFGPositionFlag(jsCodeCode, sellTageCode, codeCode);

			} else {
				isInitSellTag = false;
			}
			break;
		case R.id.isForex_query_openPositionFlag:// 建仓标志
			jcTagCode = jcTagSpinner.getSelectedItem().toString().trim();
			//P603  //先开先平  指定平仓
			int positions =getJcTagPositions(jcTagCode);//LocalData.isForexJcTagzhuijizhisunList.get(ddd);
			
			jcTagCode = LocalData.isForexJczhuijizhisunTag.get(positions);
			
			if(requestTag ==ConstantGloble.ISFOREX_WPC_TRADE_ACTIVITY){//从指定平仓页面进入
				if(jcTagCode.equals("C")||jcTagCode.equals("指定平仓")){
					LocalData.isForexExchangeTypeList.clear();
					LocalData.isForexExchangeTypeList.add("市价即时");
					LocalData.isForexExchangeTypeList.add("限价即时");
					if(exchangeTypeCode.equals("LI")||exchangeTypeCode.equals("限价即时")){
						choiceExchangeType_Gone(1);
						initTypeSpinnerGone(1);
					}else{
						choiceExchangeType_Gone(0);
						initTypeSpinnerGone(0);
					}
					lyt_common.setVisibility(view.GONE);
					lyt_gone.setVisibility(View.VISIBLE);
					jcTag_code_view.setVisibility(View.VISIBLE);
					jcTagView_gone.setVisibility(View.VISIBLE);
					if(queryTag == 10||tradeTag ==2){
						forex_rate_typeName_gone.setTextColor(getResources().getColor(R.color.black));
						typeSpinner_gone.setEnabled(true);
						limitRateTextName_gone.setTextColor(getResources().getColor(R.color.black));
						limitRateText_gone.setEnabled(true);
					}else{
						forex_rate_typeName_gone.setTextColor(getResources().getColor(R.color.gray));
						typeSpinner_gone.setEnabled(false);
						limitRateTextName_gone.setTextColor(getResources().getColor(R.color.gray));
						limitRateText_gone.setEnabled(false);
					}
					initJcTagSpinnerGone(2);
					
				}else if(jcTagCode.equals("N")||jcTagCode.equals("先开先平")){
					lyt_common.setVisibility(view.VISIBLE);
					lyt_gone.setVisibility(View.GONE);
					jcTag_code_view.setVisibility(View.GONE);
				
					initJcTagSpinner(LocalData.isForexJcTagzhuijizhisunList,1);
					
					LocalData.isForexExchangeTypeList.clear();
					LocalData.isForexExchangeTypeList.add("市价即时");
					LocalData.isForexExchangeTypeList.add("限价即时");
					LocalData.isForexExchangeTypeList.add("获利委托");
					LocalData.isForexExchangeTypeList.add("止损委托");
					LocalData.isForexExchangeTypeList.add("二选一委托");
					LocalData.isForexExchangeTypeList.add("追击止损委托");
					LocalData.isForexExchangeTypeCodeList.add("FO");
				}else {
					lyt_common.setVisibility(View.VISIBLE);
					lyt_gone.setVisibility(View.GONE);
					jcTag_code_view.setVisibility(View.GONE);
					initJcTagSpinner(LocalData.isForexJcTagzhuijizhisunList,0);
					LocalData.isForexExchangeTypeList.clear();
					LocalData.isForexExchangeTypeList.add("市价即时");
					LocalData.isForexExchangeTypeList.add("限价即时");
					LocalData.isForexExchangeTypeList.add("获利委托");
					LocalData.isForexExchangeTypeList.add("止损委托");
					LocalData.isForexExchangeTypeList.add("二选一委托");
					if(exchangeTypeCode.equals("FO")){
						initTypeSpinner(0);
					}
				}
			}else{
				
				if((jcTagCode.equals("C")||jcTagCode.equals("指定平仓"))){
					
					LocalData.isForexExchangeTypeList.clear();
					LocalData.isForexExchangeTypeList.add("市价即时");
					LocalData.isForexExchangeTypeList.add("限价即时");
					if(exchangeTypeCode.equals("LI")||exchangeTypeCode.equals("限价即时")){
						choiceExchangeType_Gone(1);
						initTypeSpinnerGone(1);
					}else{
						choiceExchangeType_Gone(0);
						initTypeSpinnerGone(0);
					}
					lyt_common.setVisibility(view.GONE);
					lyt_gone.setVisibility(View.VISIBLE);
					jcTag_code_view.setVisibility(View.GONE);
					jcTagView_gone.setVisibility(View.VISIBLE);
					forex_rate_typeName_gone.setTextColor(getResources().getColor(R.color.gray));
					typeSpinner_gone.setEnabled(false);
					limitRateTextName_gone.setTextColor(getResources().getColor(R.color.gray));
					limitRateText_gone.setEnabled(false);
					initJcTagSpinnerGone(2);
				} else if(jcTagCode.equals("N")||jcTagCode.equals("先开先平")){
					lyt_common.setVisibility(View.VISIBLE);
					lyt_gone.setVisibility(View.GONE);
					initJcTagSpinner(LocalData.isForexJcTagzhuijizhisunList,1);
					jcTag_code_view.setVisibility(View.GONE);
					LocalData.isForexExchangeTypeList.clear();
					LocalData.isForexExchangeTypeList.add("市价即时");
					LocalData.isForexExchangeTypeList.add("限价即时");
					LocalData.isForexExchangeTypeList.add("获利委托");
					LocalData.isForexExchangeTypeList.add("止损委托");
					LocalData.isForexExchangeTypeList.add("二选一委托");
					LocalData.isForexExchangeTypeList.add("追击止损委托");
//					LocalData.isForexExchangeTypeCodeList.add("FO");
//					initTypeSpinner(typePosition);
				}else {
					lyt_common.setVisibility(View.VISIBLE);
					lyt_gone.setVisibility(View.GONE);
					jcTag_code_view.setVisibility(View.GONE);
					initJcTagSpinner(LocalData.isForexJcTagzhuijizhisunList,0);
					LocalData.isForexExchangeTypeList.clear();
					LocalData.isForexExchangeTypeList.add("市价即时");
					LocalData.isForexExchangeTypeList.add("限价即时");
					LocalData.isForexExchangeTypeList.add("获利委托");
					LocalData.isForexExchangeTypeList.add("止损委托");
					LocalData.isForexExchangeTypeList.add("二选一委托");
					if(exchangeTypeCode.equals("FO")){
						initTypeSpinner(0);
					}
				}
			}
			
			
			
			
			break;
		case R.id.forex_query_deal_enddate:// 小时
			endDateStr = endTimesSpinner.getSelectedItem().toString().trim();
			break;
			//P603反显
		case R.id.isForex_query_openPositionFlag_gone://建仓标志
			jcTagCode = jcTagSpinner_gone.getSelectedItem().toString().trim();
//			//P603  
			int posi =getJcTagPositions(jcTagCode);
			jcTagCode = LocalData.isForexJczhuijizhisunTag.get(posi);
			
			if(requestTag ==ConstantGloble.ISFOREX_WPC_TRADE_ACTIVITY){//从指定平仓页面进入
				if(jcTagCode.equals("C")||jcTagCode.equals("指定平仓")){
					jcTag_code_view.setVisibility(View.VISIBLE);
//					textview_query_pingcang.setVisibility(View.VISIBLE);
					lyt_common.setVisibility(view.GONE);
					lyt_gone.setVisibility(View.VISIBLE);
					LocalData.isForexExchangeTypeList.clear();
					LocalData.isForexExchangeTypeList.add("市价即时");
					LocalData.isForexExchangeTypeList.add("限价即时");
					if(queryTag == 10||tradeTag ==2){
						forex_rate_typeName_gone.setTextColor(getResources().getColor(R.color.black));
						typeSpinner_gone.setEnabled(true);
						limitRateTextName_gone.setTextColor(getResources().getColor(R.color.black));
						limitRateText_gone.setEnabled(true);
					}else{
						forex_rate_typeName_gone.setTextColor(getResources().getColor(R.color.gray));
						typeSpinner_gone.setEnabled(false);
						limitRateTextName_gone.setTextColor(getResources().getColor(R.color.gray));
						limitRateText_gone.setEnabled(false);
					}
					initJcTagSpinnerGone(2);
					return ;
				} else if(jcTagCode.equals("N")||jcTagCode.equals("先开先平")){
					lyt_common.setVisibility(view.VISIBLE);
					lyt_gone.setVisibility(View.GONE);
					jcTag_code_view.setVisibility(View.GONE);
				
					initJcTagSpinner(LocalData.isForexJcTagzhuijizhisunList,1);
					
					LocalData.isForexExchangeTypeList.clear();
					LocalData.isForexExchangeTypeList.add("市价即时");
					LocalData.isForexExchangeTypeList.add("限价即时");
					LocalData.isForexExchangeTypeList.add("获利委托");
					LocalData.isForexExchangeTypeList.add("止损委托");
					LocalData.isForexExchangeTypeList.add("二选一委托");
					LocalData.isForexExchangeTypeList.add("追击止损委托");
				}else {
					lyt_common.setVisibility(View.VISIBLE);
					lyt_gone.setVisibility(View.GONE);
					jcTag_code_view.setVisibility(View.GONE);
//					textview_query_pingcang.setVisibility(View.VISIBLE);
					initJcTagSpinner(LocalData.isForexJcTagzhuijizhisunList,0);
					LocalData.isForexExchangeTypeList.clear();
					LocalData.isForexExchangeTypeList.add("市价即时");
					LocalData.isForexExchangeTypeList.add("限价即时");
					LocalData.isForexExchangeTypeList.add("获利委托");
					LocalData.isForexExchangeTypeList.add("止损委托");
					LocalData.isForexExchangeTypeList.add("二选一委托");
					if(exchangeTypeCode.equals("FO")){
						initTypeSpinner(0);
					}
				}
			}else{
				
				if((jcTagCode.equals("C")||jcTagCode.equals("指定平仓"))){
					
					LocalData.isForexExchangeTypeList.clear();
					LocalData.isForexExchangeTypeList.add("市价即时");
					LocalData.isForexExchangeTypeList.add("限价即时");
					lyt_common.setVisibility(view.GONE);
					lyt_gone.setVisibility(View.VISIBLE);
					jcTag_code_view.setVisibility(View.GONE);
//					textview_query_pingcang.setVisibility(View.VISIBLE);
					jcTagView_gone.setVisibility(View.VISIBLE);
					forex_rate_typeName_gone.setTextColor(getResources().getColor(R.color.gray));
					typeSpinner_gone.setEnabled(false);
					limitRateTextName_gone.setTextColor(getResources().getColor(R.color.gray));
					limitRateText_gone.setEnabled(false);
					initJcTagSpinnerGone(2);
				} else if(jcTagCode.equals("N")||jcTagCode.equals("先开先平")){
					lyt_common.setVisibility(View.VISIBLE);
					lyt_gone.setVisibility(View.GONE);
					initJcTagSpinner(LocalData.isForexJcTagzhuijizhisunList,1);
					jcTag_code_view.setVisibility(View.GONE);
//					textview_query_pingcang.setVisibility(View.VISIBLE);
					LocalData.isForexExchangeTypeList.clear();
					LocalData.isForexExchangeTypeList.add("市价即时");
					LocalData.isForexExchangeTypeList.add("限价即时");
					LocalData.isForexExchangeTypeList.add("获利委托");
					LocalData.isForexExchangeTypeList.add("止损委托");
					LocalData.isForexExchangeTypeList.add("二选一委托");
					LocalData.isForexExchangeTypeList.add("追击止损委托");
				}else {
					lyt_common.setVisibility(View.VISIBLE);
					lyt_gone.setVisibility(View.GONE);
					jcTag_code_view.setVisibility(View.GONE);
					initJcTagSpinner(LocalData.isForexJcTagzhuijizhisunList,0);
					LocalData.isForexExchangeTypeList.clear();
					LocalData.isForexExchangeTypeList.add("市价即时");
					LocalData.isForexExchangeTypeList.add("限价即时");
					LocalData.isForexExchangeTypeList.add("获利委托");
					LocalData.isForexExchangeTypeList.add("止损委托");
					LocalData.isForexExchangeTypeList.add("二选一委托");
					if(exchangeTypeCode.equals("FO")){
						initTypeSpinner(0);
					}
				}
			}
			
			
			break;
		default:
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	/** 根据交易方式请求数据 */
	private void requestRate() {
		if (shiJiaCode.equals(exchangeTypeCode) || xianJiaCode.equals(exchangeTypeCode)) {
			requestPsnVFGTradeConfirm(jsCodeCode, sellTageCode, codeCode, exchangeTypeCode);
		} else {
			currentRateView.setVisibility(View.VISIBLE);
			requestPsnVFGTradeConfirm(jsCodeCode, sellTageCode, codeCode, shiJiaCode);//shiJiaCode
		}
	}

	/** 根据交易方式，显示隐藏不同的区域 */
	private void choiceExchangeType(int position) {
		switch (position) {
		case 0:// 市价
			limitRateView.setVisibility(View.GONE);
			tradeButton.setVisibility(View.VISIBLE);
			nextButton.setVisibility(View.GONE);
			exchangeTypeCode = shiJiaCode;
			
			limitRateView_gone.setVisibility(View.GONE);
			
			hideView();
			break;
		case 1:// 限价
			limitRateView.setVisibility(View.VISIBLE);
			tradeButton.setVisibility(View.GONE);
			nextButton.setVisibility(View.VISIBLE);
			exchangeTypeCode = xianJiaCode;
			limitRateText.requestFocus();
			
			limitRateView_gone.setVisibility(View.VISIBLE);
			
			updateLimitHint();
			hideView();
			break;
		case 2:// 获利委托
			showPartView();
			setTimes(startDateStr);
			exchangeTypeCode = weiTuoCode;
			// exchangeTypeName = threeName;
			updateLimitHint();
			break;
		case 3:// 止损委托
			showPartView();
			setTimes(startDateStr);
			exchangeTypeCode = zhiSunCode;
			updateLimitHint();
			break;
		case 4:// 二选一委托
			showPartsView();
			setTimes(startDateStr);
			exchangeTypeCode = twoCode;
			updateLimitHint();
			break;
		case 5://追击止损wuhan
			showZhisunweituoPartView();
			forex_zhuijidiancha.requestFocus();
			setTimes(startDateStr);
			exchangeTypeCode  =zhuijizhisunCode;
			initJcTagSpinner(LocalData.isForexJcTagzhuijizhisunList,1);
//			//发送挂单点差范围查询
			queryisForexDianCha(codeCode);
			break;
		default:
			break;
		}
	}

	/** 根据交易方式，显示隐藏不同的区域 */
	private void choiceExchangeType_Gone(int position) {
		switch (position) {
		case 0:// 市价
			limitRateView.setVisibility(View.GONE);
			tradeButton.setVisibility(View.VISIBLE);
			nextButton.setVisibility(View.GONE);
			exchangeTypeCode = shiJiaCode;
			
			limitRateView_gone.setVisibility(View.GONE);
			
			hideView_Gone();
			break;
		case 1:// 限价
			limitRateView.setVisibility(View.VISIBLE);
			tradeButton.setVisibility(View.GONE);
			nextButton.setVisibility(View.VISIBLE);
			exchangeTypeCode = xianJiaCode;
			limitRateText.requestFocus();
			limitRateView_gone.setVisibility(View.VISIBLE);
			
			
			updateLimitHint();
			hideView_Gone();
			break;

		default:
			break;
		}
	}
	
	@Override
	public void queryisForexDianChaCallBack(Object resultObj) {
		super.queryisForexDianChaCallBack(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> map = (Map<String, String>) biiResponseBody.getResult();
		maxPendingSet = map.get("maxPendingSet");
		minPendingSet = map.get("minPendingSet");
		fores_dianchafanwei.setText(getResources().getString(R.string.prms_runlost_dianchaEdit)+minPendingSet+"-"+maxPendingSet);
		updateLimitHint();
	}
	
	/** 设置委托截止时间 */
	private void setTimes(String currenttime) {
		startTimes.setText(currenttime);
	}

	/** 止损委托---获利委托--使用 */
	private void showPartView() {
		weiTuoRateText.requestFocus();
		weiTuoRateView.setVisibility(View.VISIBLE);
		startTimesView.setVisibility(View.VISIBLE);
		endTimesView.setVisibility(View.VISIBLE);
		tradeButton.setVisibility(View.GONE);
		nextButton.setVisibility(View.VISIBLE);
		huoLiRateView.setVisibility(View.GONE);
		zhiSunRateView.setVisibility(View.GONE);
		currentRateView.setVisibility(View.GONE);
		limitRateView.setVisibility(View.GONE);
		zhijizhisun_layout.setVisibility(View.GONE);
		messageView.setVisibility(View.VISIBLE);
		
		
		currentRateView_gone.setVisibility(View.GONE);
		limitRateView_gone.setVisibility(View.GONE);
		messageView_gone.setVisibility(View.VISIBLE);
	}

	/** 二选一委托--使用 */
	private void showPartsView() {
		huoLiRateText.requestFocus();
		weiTuoRateView.setVisibility(View.GONE);
		startTimesView.setVisibility(View.VISIBLE);
		endTimesView.setVisibility(View.VISIBLE);
		tradeButton.setVisibility(View.GONE);
		nextButton.setVisibility(View.VISIBLE);
		huoLiRateView.setVisibility(View.VISIBLE);
		zhiSunRateView.setVisibility(View.VISIBLE);
		currentRateView.setVisibility(View.GONE);
		limitRateView.setVisibility(View.GONE);
		zhijizhisun_layout.setVisibility(View.GONE);
		messageView.setVisibility(View.VISIBLE);
		
		
		currentRateView_gone.setVisibility(View.GONE);
		limitRateView_gone.setVisibility(View.GONE);
		messageView_gone.setVisibility(View.VISIBLE);
	}

	private void showZhisunweituoPartView() {
		weiTuoRateView.setVisibility(View.GONE);
		startTimesView.setVisibility(View.VISIBLE);
		endTimesView.setVisibility(View.VISIBLE);
		tradeButton.setVisibility(View.GONE);
		nextButton.setVisibility(View.VISIBLE);
		huoLiRateView.setVisibility(View.GONE);
		zhiSunRateView.setVisibility(View.GONE);
		currentRateView.setVisibility(View.VISIBLE);
		limitRateView.setVisibility(View.GONE);
		zhijizhisun_layout.setVisibility(View.VISIBLE);
		messageView.setVisibility(View.GONE);
		
		
		currentRateView_gone.setVisibility(View.VISIBLE);
		limitRateView_gone.setVisibility(View.GONE);
		messageView_gone.setVisibility(View.GONE);
	}
	
	/** 清空限价汇率数据 */
	private void clearRate() {
		currentRateText.setText("");
		limitRateText.setText("");
		weiTuoRateText.setText("");
		huoLiRateText.setText("");
		zhiSunRateText.setText("");
	}

	/** 动态修改限价汇率的hint */
	private void updateLimitHint() {
		String text = getResources().getString(R.string.isForex_rate_currency_limitRate_info);
		String spText = getResources().getString(R.string.isForex_rate_currency_limitRate_info1);
		String spTextT = getResources().getString(R.string.isForex_rate_currency_limitRate_info2);
		if (spetialCodeList.contains(sourceCodeCode) && spetialCodeList.contains(targetCodeCode)) {
			limitRateText.setHint(text);
			weiTuoRateText.setHint(text);
			huoLiRateText.setHint(text);
			zhiSunRateText.setHint(text);
			return;
		} else {
			// 源货币对为日元或黄金，小数点为2位
			if (spCodeList.contains(sourceCodeCode) || spCodeList.contains(targetCodeCode)) {
				limitRateText.setHint(spText);
				weiTuoRateText.setHint(spText);
				huoLiRateText.setHint(spText);
				zhiSunRateText.setHint(spText);
				// 货币源为白银，小数点为3位
			} else if (spCodeListT.contains(sourceCodeCode) || spCodeListT.contains(targetCodeCode)) {
				limitRateText.setHint(spTextT);
				weiTuoRateText.setHint(spTextT);
				huoLiRateText.setHint(spTextT);
				zhiSunRateText.setHint(spTextT);
			} else {
				limitRateText.setHint(text);
				weiTuoRateText.setHint(text);
				huoLiRateText.setHint(text);
				zhiSunRateText.setHint(text);
			}
		}
	}

	/** 修改交易金额的hint */
	private void updateJyMoneyHint(String sourceCode) {
		String rmbText = getResources().getString(R.string.isForex_trade_curr_money3);
		String wbText = getResources().getString(R.string.isForex_trade_curr_money4);
		String otherText = getResources().getString(R.string.isForex_trade_curr_money);
		// 人民币银
		String silverText = getResources().getString(R.string.isForex_trade_curr_money5);
		// 人民币钯金、铂金
		String platinumText = getResources().getString(R.string.isForex_trade_curr_money3);
		// 美元银
		String dollarText = getResources().getString(R.string.isForex_trade_curr_money6);
		// 美元钯金、铂金
		String dollarPlatText = getResources().getString(R.string.isForex_trade_curr_money4);
		if (LocalData.goldList.contains(sourceCode)) {
			// 保留一位小数
			moneyText.setHint(wbText);
			moneyText.setInputType(EditorInfo.TYPE_NUMBER_FLAG_DECIMAL | EditorInfo.TYPE_CLASS_NUMBER);
			// EditTextUtils.setTextMatcher(this, moneyText, "[\\d|\\.]*");
		} else if (LocalData.rmbGoldList.contains(sourceCode)) {
			// 人民币金 金额最小为10的整数
			moneyText.setInputType(InputType.TYPE_CLASS_NUMBER);
			moneyText.setHint(rmbText);
		} else if (LocalData.silverGoldList.contains(sourceCode)) {
			// 人民币银 金额最小为100的整数
			moneyText.setHint(silverText);
			moneyText.setInputType(InputType.TYPE_CLASS_NUMBER);
		} else if (LocalData.platinumGoldList.contains(sourceCode)) {
			// 人民币 钯金 铂金 金额最小为10的整数
			moneyText.setHint(platinumText);
			moneyText.setInputType(InputType.TYPE_CLASS_NUMBER);
		} else if (LocalData.dollarGoldList.contains(sourceCode)) {
			// 美元银 金额最小为5的整数
			moneyText.setHint(dollarText);
			moneyText.setInputType(InputType.TYPE_CLASS_NUMBER);
		} else if (LocalData.dollarPlatGoladList.contains(sourceCode)) {
			// 美元钯金 铂金 保留一位小数
			moneyText.setHint(dollarPlatText);
			moneyText.setInputType(EditorInfo.TYPE_NUMBER_FLAG_DECIMAL | EditorInfo.TYPE_CLASS_NUMBER);
		} else {
			// 金额须为100的整数倍
			moneyText.setInputType(InputType.TYPE_CLASS_NUMBER);
			moneyText.setHint(otherText);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.trade_quickButton:// 直接成交
			if((jcTagCode.equals("C")||jcTagCode.equals("指定平仓"))){
				
				if(isForex_jcCode.getText().toString().trim()!= null && !"".equals(isForex_jcCode.getText().toString().trim())){

					nextOrQuickTag = 1;
					checkDateZhiding();
				}else{
//					请先查询并选择未平仓交易记录
					BaseDroidApp.getInstanse().showInfoMessageDialog("请先查询并选择未平仓交易记录");
					return; 
				}
				
			}else{
				nextOrQuickTag = 1;
				getJcTagValue();
				checkDate();	
				
			}
			
			break;
		case R.id.trade_nextButton:// 下一步
			if((jcTagCode.equals("C")||jcTagCode.equals("指定平仓"))){
				if(isForex_jcCode.getText().toString().trim()!= null && !"".equals(isForex_jcCode.getText().toString().trim())){
					nextOrQuickTag = 2;
					checkDateZhiding();
				}else{
//					请先查询并选择未平仓交易记录
					BaseDroidApp.getInstanse().showInfoMessageDialog("请先查询并选择未平仓交易记录");
					return; 
				}
				
			}else{
				nextOrQuickTag = 2;
				getJcTagValue();
				checkDate();
			}
			
			break;
		case R.id.ib_back:
			setResult(RESULT_CANCELED);
			finish();
			break;
		case R.id.forex_query_deal_startdate:// 年月日
			Calendar c = Calendar.getInstance(); // 实例化一个日期与时间操作对象
			QueryDateUtils.checkDate(startTimes, c);
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息 DatePickerDialog
			DatePickerDialog dialog = new DatePickerDialog(IsForexTradeSubmitActivity.this, new OnDateSetListener() {
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
			break;
			//P603
			
		default:
			break;
		}

	}

	/** 得到建仓标志的值 */
	private void getJcTagValue() {
		if (jcTagView.isShown()) {
			// 显示建仓标志
			jcTagCode = jcTagSpinner.getSelectedItem().toString().trim();
		} else {
			// 不显示建仓标志 ，其值为N
			jcTagCode = ConstantGloble.ISFOREX_JCTAG_N1;
		}
	}

	/** 验证用户输入的信息 */
	private void checkDate() {
		money = moneyText.getText().toString().trim();
		if(moneyText_gone.getText().toString().trim()!=null && !"".equals(moneyText_gone.getText().toString().trim())){
			money = moneyText_gone.getText().toString().trim();
		}
		if (shiJiaCode.equals(exchangeTypeCode)) {
			// 市价交易---金额
			checkInputMoneyDate();
		} else if (xianJiaCode.equals(exchangeTypeCode)) {
			// 限价交易----汇率、金额
			preCheckLimit();
		} else if (weiTuoCode.equals(exchangeTypeCode) || zhiSunCode.equals(exchangeTypeCode)) {
			// 委托获利 、止损获利
			checkWeiTuoRate();
		} else if (twoCode.equals(exchangeTypeCode)) {
			// 二选一委托
			checkTwoRate();
		}else if(zhuijizhisunCode.equals(exchangeTypeCode)){
			//追击止损委托wuhan
			checkZJZSWeiTuo();
		}
	}

	/** 验证用户输入的信息 */
	private void checkDateZhiding() {
		money = moneyText.getText().toString().trim();
		if(moneyText_gone.getText().toString().trim()!=null && !"".equals(moneyText_gone.getText().toString().trim())){
			money = moneyText_gone.getText().toString().trim();
		}
		if (shiJiaCode.equals(exchangeTypeCode)) {
			// 市价交易---金额
			checkInputMoneyDateZhiding();
//			requestZhidingDate();
		} else if (xianJiaCode.equals(exchangeTypeCode)) {
			// 限价交易----汇率、金额
			preCheckLimitZhiding();
		} else if (weiTuoCode.equals(exchangeTypeCode) || zhiSunCode.equals(exchangeTypeCode)) {
			// 委托获利 、止损获利
			checkWeiTuoRate();
		} else if (twoCode.equals(exchangeTypeCode)) {
			// 二选一委托
			checkTwoRate();
		}else if(zhuijizhisunCode.equals(exchangeTypeCode)){
			//追击止损委托wuhan
			checkZJZSWeiTuo();
		}
	}

	
	/**追击止损委托验证wuhan **/
	private void checkZJZSWeiTuo(){
		dianCha = forex_zhuijidiancha.getText().toString().trim();
//		int max = Integer.valueOf(maxPendingSet) ;
//		int min = Integer.valueOf(minPendingSet);
//		if(max>=Integer.valueOf(dianCha)&& min <= Integer.valueOf(dianCha)){
//			checkTimes();
//		}else 
			if("0".equals(dianCha)||dianCha.contains(".")){
			BaseDroidApp.getInstanse().showInfoMessageDialog("只能输入整数且不能为0");
			return;
		}else if("".equals(dianCha)||dianCha == null){
			BaseDroidApp.getInstanse().showInfoMessageDialog("请输入追击点差");
			return;
		}else
		{
//			BaseDroidApp.getInstanse().showInfoMessageDialog("请输入正确的追击点差");
//			return;
			checkTimes();
		}
		
		
		
	}
	
	/*** 使用正则验证限价汇率 */
	private void preCheckLimit() {
		limitRate = limitRateText.getText().toString().trim();
		// 日元/港元 港元/日元 校验保留四位小数
		if (spetialCodeList.contains(sourceCodeCode) && spetialCodeList.contains(targetCodeCode)) {
			RegexpBean reb1 = new RegexpBean(rateEmg, limitRate, "spetialRate", true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb1);
			if (RegexpUtils.regexpDate(lists)) {
				checkInputMoneyDate();
			} else {
				return;
			}
			return;
		} else {
			// 日元、黄金等校验保留两位小数
			if (spCodeList.contains(sourceCodeCode) || spCodeList.contains(targetCodeCode)) {
				RegexpBean reb1 = new RegexpBean(rateEmg, limitRate, "splLimitRates", true);
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(reb1);
				if (RegexpUtils.regexpDate(lists)) {
					checkInputMoneyDate();
				} else {
					return;
				}
				// 白银(美元银 // 人民币银)校验 // 保留3位小数
			} else if (spCodeListT.contains(sourceCodeCode) || spCodeListT.contains(targetCodeCode)) {																						
				RegexpBean reb1 = new RegexpBean(rateEmg, limitRate, "splLimitRatest", true);
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(reb1);
				if (RegexpUtils.regexpDate(lists)) {
					checkInputMoneyDate();
				} else {
					return;
				}
				return;
			} else {
				RegexpBean reb1 = new RegexpBean(rateEmg, limitRate, "limitRate", true);
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(reb1);
				if (RegexpUtils.regexpDate(lists)) {
					checkInputMoneyDate();
				} else {
					return;
				}
			}
		}
	}

	/*** 使用正则验证限价汇率P605指定平仓*/
	private void preCheckLimitZhiding() {
		limitRate = limitRateText.getText().toString().trim();
		if(!"".equals(limitRateText_gone.getText().toString().trim())&& limitRateText_gone.getText().toString().trim()!=null){
			limitRate = limitRateText_gone.getText().toString().trim();
		}
		
		// 日元/港元 港元/日元 校验保留四位小数
		if (spetialCodeList.contains(sourceCodeCode) && spetialCodeList.contains(targetCodeCode)) {
			RegexpBean reb1 = new RegexpBean(rateEmg, limitRate, "spetialRate", true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb1);
			if (RegexpUtils.regexpDate(lists)) {
				requestZhidingDate();
			} else {
				return;
			}
			return;
		} else {
			// 日元、黄金等校验保留两位小数
			if (spCodeList.contains(sourceCodeCode) || spCodeList.contains(targetCodeCode)) {
				RegexpBean reb1 = new RegexpBean(rateEmg, limitRate, "splLimitRates", true);
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(reb1);
				if (RegexpUtils.regexpDate(lists)) {
					requestZhidingDate();
				} else {
					return;
				}
				// 白银(美元银 // 人民币银)校验 // 保留3位小数
			} else if (spCodeListT.contains(sourceCodeCode) || spCodeListT.contains(targetCodeCode)) {																						
				RegexpBean reb1 = new RegexpBean(rateEmg, limitRate, "splLimitRatest", true);
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(reb1);
				if (RegexpUtils.regexpDate(lists)) {
					requestZhidingDate();
				} else {
					return;
				}
				return;
			} else {
				RegexpBean reb1 = new RegexpBean(rateEmg, limitRate, "limitRate", true);
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(reb1);
				if (RegexpUtils.regexpDate(lists)) {
					requestZhidingDate();
				} else {
					return;
				}
			}
		}
	}
	
	
	/** 获利委托、止损委托----------委托汇率 */
	private void checkWeiTuoRate() {
		weiTuoRate = weiTuoRateText.getText().toString().trim();
		String text = getResources().getString(R.string.forex_trade_zhiSun_weituo1);// 委托汇率
		// 买入币种或卖出币种同时为日元、港元时小数点4位特殊的货币对，港元/日元、日元/港元，小数点后为四位
		if (spetialCodeList.contains(sourceCodeCode) && spetialCodeList.contains(targetCodeCode)) {
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
			if (spCodeList.contains(sourceCodeCode) || spCodeList.contains(targetCodeCode)) {
				RegexpBean reb1 = new RegexpBean(text, weiTuoRate, "splLimitRates", true);
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(reb1);
				if (RegexpUtils.regexpDate(lists)) {
					checkTimes();
				} else {
					return;
				}
				return;
			} else if (spCodeListT.contains(sourceCodeCode) || spCodeListT.contains(targetCodeCode)) {
				RegexpBean reb1 = new RegexpBean(text, weiTuoRate, "splLimitRatest", true);
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(reb1);
				if (RegexpUtils.regexpDate(lists)) {
					checkTimes();
				} else {
					return;
				}
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
		if (spetialCodeList.contains(sourceCodeCode) && spetialCodeList.contains(targetCodeCode)) {
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
		} else {
			if (spCodeList.contains(sourceCodeCode) || spCodeList.contains(targetCodeCode)) {
				RegexpBean reb1 = new RegexpBean(text1, huoLiRate, "splLimitRates", true);
				RegexpBean reb2 = new RegexpBean(text2, zhiSunRate, "splLimitRates", true);
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(reb1);
				lists.add(reb2);
				if (RegexpUtils.regexpDate(lists)) {
					checkTimes();
				} else {
					return;
				}
				return;
			} else if (spCodeListT.contains(sourceCodeCode) || spCodeListT.contains(targetCodeCode)) {// 白银(美元银																						
				// 保留3位小数 // 人民币银)校验
				RegexpBean reb1 = new RegexpBean(text1, huoLiRate, "splLimitRatest", true);
				RegexpBean reb2 = new RegexpBean(text2, zhiSunRate, "splLimitRatest", true);
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
	}

	/** 验证时间 */
	private void checkTimes() {
		StringBuilder sb = new StringBuilder(startDateStr);
		sb.append(" ");
		sb.append(endDateStr);
		sb.append(":00:00");
		custonerTimes = sb.toString().trim();
		checkInputMoneyDate();
	}

	/** 验证金额 */
	private void checkInputMoneyDate() {
//		tradeMoney = moneyText.getText().toString().trim();
		money = moneyText.getText().toString().trim();
//		if(moneyText_gone.getText().toString().trim()!=null && !"".equals(moneyText_gone.getText().toString().trim())){
//			money = moneyText_gone.getText().toString().trim();
//		}
		if (tradeMoney != null && tradeMoney.equals(money)) {
			// 不使用正则验证
			if (isDeal(codeName)) {
				requestDate();
			} else {
				// 正则验证金额
				checkLimitAndMoney();
			}
		} else {
			// 正则验证金额
			checkLimitAndMoney();
		}
	}

	
	/** 验证金额 */
	private void checkInputMoneyDateZhiding() {
//		tradeMoney = moneyText.getText().toString().trim();
		if (tradeMoney != null && tradeMoney.equals(money)) {
			// 不使用正则验证
			if (isDeal(codeName)) {
				requestZhidingDate();
			} else {
				// 正则验证金额
				checkLimitAndMoneyZhiding();
			}
		} else {
			// 正则验证金额
			checkLimitAndMoneyZhiding();
		}
	}
	
	private void checkLimitAndMoneyZhiding() {
		if (LocalData.goldList.contains(sourceCodeCode)) {
			// 美元金
			RegexpBean reb2 = new RegexpBean(getResources().getString(R.string.isForex_trade_money), money, "bondAmount1", true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb2);
			if (RegexpUtils.regexpDate(lists)) {
				requestZhidingDate();
			} else {
				return;
			}
		} else if (LocalData.rmbGoldList.contains(sourceCodeCode)) {
			// 人民币金
			RegexpBean reb2 = new RegexpBean(getResources().getString(R.string.isForex_trade_money), money, "bondAmount10",
					true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb2);
			if (RegexpUtils.regexpDate(lists)) {
				requestZhidingDate();
			} else {
				return;
			}
		} else if (LocalData.silverGoldList.contains(sourceCodeCode)) {
			// 人民币银
			RegexpBean reb2 = new RegexpBean(getResources().getString(R.string.isForex_trade_money), money, "bondAmount100",
					true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb2);
			if (RegexpUtils.regexpDate(lists)) {
				requestZhidingDate();
			} else {
				return;
			}
		} else if (LocalData.platinumGoldList.contains(sourceCodeCode)) {
			// 人民币钯金 、铂金
			RegexpBean reb2 = new RegexpBean(getResources().getString(R.string.isForex_trade_money), money, "bondAmount10",
					true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb2);
			if (RegexpUtils.regexpDate(lists)) {
				requestZhidingDate();
			} else {
				return;
			}
		} else if (LocalData.dollarGoldList.contains(sourceCodeCode)) {
			// 美元银
			RegexpBean reb2 = new RegexpBean(getResources().getString(R.string.isForex_trade_money), money, "bondAmount51",
					true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb2);
			if (RegexpUtils.regexpDate(lists)) {
				requestZhidingDate();
			} else {
				return;
			}
		} else if (LocalData.dollarPlatGoladList.contains(sourceCodeCode)) {
			// 美元铂金 钯金
			RegexpBean reb2 = new RegexpBean(getResources().getString(R.string.isForex_trade_money), money, "bondAmount1", true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb2);
			if (RegexpUtils.regexpDate(lists)) {
				requestZhidingDate();
			} else {
				return;
			}
		} else {
			RegexpBean reb2 = new RegexpBean(getResources().getString(R.string.isForex_trade_money), money, "spMoney", true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb2);
			if (RegexpUtils.regexpDate(lists)) {
				requestZhidingDate();
			} else {
				return;
			}
		}
	}
	/** 金额没有发生改变，有首货币对判断 */
	private boolean isDeal(String codeName) {
		int len1 = codeName.indexOf("/");
		String sourceName = codeName.substring(0, len1);
		int len2 = codeCodeName.indexOf("/");
		String sourceName2 = codeCodeName.substring(0, len2);
		if (sourceName2.equals(sourceName)) {
			return true;
		} else {
			return false;
		}
	}

	/** 用户点击“平仓”按钮---修改了输入金额---使用正则交易 */
	private void checkLimitAndMoney() {
		if (LocalData.goldList.contains(sourceCodeCode)) {
			// 美元金
			RegexpBean reb2 = new RegexpBean(getResources().getString(R.string.isForex_trade_money), money, "bondAmount1", true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb2);
			if (RegexpUtils.regexpDate(lists)) {
				requestDate();
			} else {
				return;
			}
		} else if (LocalData.rmbGoldList.contains(sourceCodeCode)) {
			// 人民币金
			RegexpBean reb2 = new RegexpBean(getResources().getString(R.string.isForex_trade_money), money, "bondAmount10",
					true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb2);
			if (RegexpUtils.regexpDate(lists)) {
				requestDate();
			} else {
				return;
			}
		} else if (LocalData.silverGoldList.contains(sourceCodeCode)) {
			// 人民币银
			RegexpBean reb2 = new RegexpBean(getResources().getString(R.string.isForex_trade_money), money, "bondAmount100",
					true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb2);
			if (RegexpUtils.regexpDate(lists)) {
				requestDate();
			} else {
				return;
			}
		} else if (LocalData.platinumGoldList.contains(sourceCodeCode)) {
			// 人民币钯金 、铂金
			RegexpBean reb2 = new RegexpBean(getResources().getString(R.string.isForex_trade_money), money, "bondAmount10",
					true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb2);
			if (RegexpUtils.regexpDate(lists)) {
				requestDate();
			} else {
				return;
			}
		} else if (LocalData.dollarGoldList.contains(sourceCodeCode)) {
			// 美元银
			RegexpBean reb2 = new RegexpBean(getResources().getString(R.string.isForex_trade_money), money, "bondAmount51",
					true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb2);
			if (RegexpUtils.regexpDate(lists)) {
				requestDate();
			} else {
				return;
			}
		} else if (LocalData.dollarPlatGoladList.contains(sourceCodeCode)) {
			// 美元铂金 钯金
			RegexpBean reb2 = new RegexpBean(getResources().getString(R.string.isForex_trade_money), money, "bondAmount1", true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb2);
			if (RegexpUtils.regexpDate(lists)) {
				requestDate();
			} else {
				return;
			}
		} else {
			RegexpBean reb2 = new RegexpBean(getResources().getString(R.string.isForex_trade_money), money, "spMoney", true);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb2);
			if (RegexpUtils.regexpDate(lists)) {
				requestDate();
			} else {
				return;
			}
		}
	}

	/** 请求数据 */
	private void requestDate() {
		isDealMoney();
		if (nextOrQuickTag == 1) {
			// 直接成交
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
		} else if (nextOrQuickTag == 2) {
			// 下一步
			if (StringUtil.isNull(jsCodeCode) || StringUtil.isNull(sellTageCode) || StringUtil.isNull(codeCode)
					|| StringUtil.isNull(exchangeTypeCode)) {
				return;
			}
			if (shiJiaCode.equals(exchangeTypeCode) || xianJiaCode.equals(exchangeTypeCode)) {
				BaseHttpEngine.showProgressDialog();
				getPsnVFGTradeConfirm(jsCodeCode, sellTageCode, codeCode, exchangeTypeCode);
			} else {
				gotoActivity();
			}
		}
	}
	String internaNumber="";
	String consignNumber="";
	/** 请求数据 (指定平仓)P603新加*/
	private void requestZhidingDate() {
		if (nextOrQuickTag == 1) {
			// 直接成交
			
			if(tradeTag == 2){
				queryResultList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
						.get(IsForex.ISFOREX_LIST_REQ);
				Map<String, Object> map = queryResultList.get(Mposition);
				internaNumber = (String) map.get(IsForex.ISFOREX_internalSeq_REQ);
				consignNumber = (String) map.get(IsForex.ISFOREX_CONSIGNNUMBER_REQ);
			}
//			发送指定平仓交易
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
//			requestPSNVFGZHIDINGTRADE(jsCodeCode, consignNumber, internaNumber, tradeMoney, exchangeTypeCode, null, codeCode, sellTageCode, token);
//			requestPSNVFGZHIDINGTRADE(jsCodeCode, consignNumber, internaNumber, isForex_myRate_tvtradeMoney.getText().toString(), exchangeTypeCode, null, codeCode, sellTageCode, token);
		} else if (nextOrQuickTag == 2) {
			// 下一步
//			if (StringUtil.isNull(jsCodeCode) || StringUtil.isNull(sellTageCode) || StringUtil.isNull(codeCode)
//					|| StringUtil.isNull(exchangeTypeCode)) {
//				return;
//			}
			if (xianJiaCode.equals(exchangeTypeCode)) {
				BaseHttpEngine.showProgressDialog();
				getPsnVFGTradeConfirm(jsCodeCode, sellTageCode, codeCode, exchangeTypeCode);
			} else {
				gotoActivity();
			}
		}
	}
	
	//(指定平仓)P603新加
	@Override
	public void requestPSNVFGZHIDINGTRADECallback(Object resultObj) {
		super.requestPSNVFGZHIDINGTRADECallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> result = (Map<String, String>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			return;
		}
		Intent intent = new Intent(IsForexTradeSubmitActivity.this, IsForexTradeSuccessActivity.class);
		intent.putExtra("point", tradeTag);
		
		intent.putExtra(IsForex.ISFOREX_TRADETYPES_REQ, exchangeTypeCode);
		intent.putExtra(IsForex.ISFOREX_CURRENCYPAIRCODES_REQ, codeCode);
		intent.putExtra(IsForex.ISFOREX_CURRENCYCODES_REQ, jsCodeCode);
		intent.putExtra(IsForex.ISFOREX_DIRECTIONS_REQ, sellTageCode);
		intent.putExtra(ConstantGloble.ISFOREX_RATE_KEY, rate);
		intent.putExtra(ConstantGloble.ISFOREX_JCTAG_KEY, jcTagCode);
		intent.putExtra(ConstantGloble.ISFOREX_AMOUNT_KEY, tradeMoney);
		intent.putExtra(ConstantGloble.ISFOREX_CODECODENAME_KEY, codeCodeName);
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_RESULT_KEY, result);
		startActivityForResult(intent, 0);
		
	}
	
	/** 是否需要去掉小数位 */
	private void isDealMoney() {
		if (LocalData.goldList.contains(sourceCodeCode)) {
			// 保留一位
			money = StringUtil.splitStringwith2point(money, 1);
		} else {
			// 去掉小数位
			money = StringUtil.deleateNumber(money);
		}
	}

	/** 买卖预交易---回调 */
	@Override
	public void getPsnVFGTradeConfirmCallback(Object resultObj) {
		super.getPsnVFGTradeConfirmCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		rate = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(rate)) {
			return;
		}
		gotoActivity();
	}

	private void gotoActivity() {
		Intent intent = new Intent(IsForexTradeSubmitActivity.this, IsForexTradeConfirmActivity.class);
		intent.putExtra(IsForex.ISFOREX_TRADETYPES_REQ, exchangeTypeCode);
		intent.putExtra(IsForex.ISFOREX_CURRENCYPAIRCODES_REQ, codeCode);
		intent.putExtra(IsForex.ISFOREX_CURRENCYCODES_REQ, jsCodeCode);
		intent.putExtra(IsForex.ISFOREX_DIRECTIONS_REQ, sellTageCode);
		intent.putExtra(ConstantGloble.ISFOREX_JCTAG_KEY, jcTagCode);
		intent.putExtra(ConstantGloble.ISFOREX_AMOUNT_KEY, money);
		intent.putExtra(ConstantGloble.ISFOREX_CODECODENAME_KEY, codeCodeName);
//		if(shiJiaCode.equals(exchangeTypeCode)){
//			//市价即时
//			intent.putExtra("pointCang", tradeTag);
//			intent.putExtra("position", Mposition);
//			intent.putExtra(ConstantGloble.ISFOREX_RATE_KEY, rate);
//			intent.putExtra(ConstantGloble.ISFOREX_LIMITRATE_KEY, limitRate);
//		}else
			if (xianJiaCode.equals(exchangeTypeCode)&& tradeTag == 2) {
			// 限价
			intent.putExtra("pointCang", tradeTag);
			intent.putExtra("position", Mposition);
			intent.putExtra(ConstantGloble.ISFOREX_RATE_KEY, rate);
			intent.putExtra(ConstantGloble.ISFOREX_AMOUNT_KEY, money);
			intent.putExtra(ConstantGloble.ISFOREX_LIMITRATE_KEY, limitRate);
		} else if(xianJiaCode.equals(exchangeTypeCode)&& tradeTag != 2){
			intent.putExtra("pointCang", tradeTag);
			intent.putExtra("position", Mposition);
			intent.putExtra(ConstantGloble.ISFOREX_RATE_KEY, rate);
			intent.putExtra(ConstantGloble.ISFOREX_LIMITRATE_KEY, limitRate);
			intent.putExtra(ConstantGloble.ISFOREX_AMOUNT_KEY, money);
		}
			else if (weiTuoCode.equals(exchangeTypeCode) || zhiSunCode.equals(exchangeTypeCode)) {
			// 获利委托、止损委托
			intent.putExtra(ConstantGloble.FOREX_WEITUORATE_KEY, weiTuoRate);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY1, startDateStr);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY2, endDateStr);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY3, custonerTimes);
		} else if (twoCode.equals(exchangeTypeCode)) {
			// 二选一委托
			intent.putExtra(ConstantGloble.FOREX_HUOLIRATE_KEY, huoLiRate);
			intent.putExtra(ConstantGloble.FOREX_ZHISUNRATE_KEY, zhiSunRate);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY1, startDateStr);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY2, endDateStr);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY3, custonerTimes);
		}else if(zhuijizhisunCode.equals(exchangeTypeCode)){
			//追击止损委托wuhan
			intent.putExtra(ConstantGloble.ISFOREX_RATE_KEY, rate);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY1, startDateStr);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY2, endDateStr);
			intent.putExtra(ConstantGloble.FOREX_TIMES_KEY3, custonerTimes);
			intent.putExtra("foSet", dianCha);
			
		}
		startActivity(intent);
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		if (StringUtil.isNull(commConversationId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		requestPSNGetTokenId(commConversationId);
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		token = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		if (StringUtil.isNull(token)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		if((jcTagCode.equals("C")||jcTagCode.equals("指定平仓"))){//closedVfgTransId  closedInternanlSeq  consignNumber
			if(exchangeTypeCode.equals("LI")){
				requestPSNVFGZHIDINGTRADE(jsCodeCode, consignNumber, internaNumber, tradeMoney, exchangeTypeCode, limitRate, codeCode, sellTageCode, token,commConversationId);
			}else{
				requestPSNVFGZHIDINGTRADE(jsCodeCode, consignNumber, internaNumber, tradeMoney, exchangeTypeCode, "0", codeCode, sellTageCode, token,commConversationId);
			}
			
		}else{
			requestPsnVFGTrade(codeCode, jsCodeCode, sellTageCode, jcTagCode, money, exchangeTypeCode, null, null, null, null,
					null, null, null, token);
		}
//		requestPsnVFGTrade(codeCode, jsCodeCode, sellTageCode, jcTagCode, money, exchangeTypeCode, null, null, null, null,
//				null, null, null, token);
	}

	/** 双向宝买卖交易---回调 */
	@Override
	public void requestPsnVFGTradeCallback(Object resultObj) {
		super.requestPsnVFGTradeCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> result = (Map<String, String>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			return;
		}
		Intent intent = new Intent(IsForexTradeSubmitActivity.this, IsForexTradeSuccessActivity.class);
		intent.putExtra(IsForex.ISFOREX_TRADETYPES_REQ, exchangeTypeCode);
		intent.putExtra(IsForex.ISFOREX_CURRENCYPAIRCODES_REQ, codeCode);
		intent.putExtra(IsForex.ISFOREX_CURRENCYCODES_REQ, jsCodeCode);
		intent.putExtra(IsForex.ISFOREX_DIRECTIONS_REQ, sellTageCode);
		intent.putExtra(ConstantGloble.ISFOREX_RATE_KEY, rate);
		intent.putExtra(ConstantGloble.ISFOREX_JCTAG_KEY, jcTagCode);
		intent.putExtra(ConstantGloble.ISFOREX_AMOUNT_KEY, money);
		intent.putExtra(ConstantGloble.ISFOREX_CODECODENAME_KEY, codeCodeName);
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_RESULT_KEY, result);
		startActivityForResult(intent, 0);
	}
}
