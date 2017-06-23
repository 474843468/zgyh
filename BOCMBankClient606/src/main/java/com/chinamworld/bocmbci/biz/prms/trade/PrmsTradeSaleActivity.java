package com.chinamworld.bocmbci.biz.prms.trade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.prms.PrmsBaseActivity;
import com.chinamworld.bocmbci.biz.prms.control.PrmsControl;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 贵金属买卖，信息输入页面
 * 
 * @author xyl
 * 
 */
public class PrmsTradeSaleActivity extends PrmsBaseActivity implements
		OnItemSelectedListener {
	private static final String TAG = "PrmsTradeSaleActivity";
	/**
	 * 直接交易
	 */
	private Button turnOver;
	/**
	 * 下一步
	 */
	private Button next;
	/**
	 * 交易账户号
	 */
	private TextView tradeAcc;
	/**
	 * 交易账户余额
	 */
	private TextView accBalance;
	private TextView accBalanceUnitTv;
	private TextView limitUintTv, winUintTv, losUnitTv;

	/**
	 * 交易方式
	 */
	// private Spinner tradeStyleSpinner;
	/**
	 * 交易币种
	 */
	private Spinner currencySpinner;
	// private ArrayAdapter<String> currencyAdapter;
	/**
	 * 交易方式
	 */
	private Spinner tradeMethodSpinner;
	/**
	 * 买入数量：||卖出数量
	 */
	private TextView tradeNumTextView;
	/**
	 * 买入数量单位
	 */
	private TextView prmsUint;
	/**
	 * 买入价格
	 */
	private TextView buyRateTexView;
	private TextView buyRateUnitTv;
	/**
	 * 买入价格||卖出价格
	 */
	private TextView buyRateTexView1;
	/**
	 * 交易数量
	 */
	private EditText tradeNumEditText;

	/**
	 * 限价
	 */
	private LinearLayout limitPriceLayout, winPriceView, losPriceView,
			entrustDateView,prms_run_lose_ll;
	private EditText limitPriceEdit, winPriceEdt, losPriceEdt,prms_runlost_diancha_edit;
	private TextView entrustDateTv;
	private Spinner entrustTimeSpinner;
	/** 截止日 截止时 */
	private String endDate = null, endHour = null;
	/**
	 * 交易贵金属类型 G /S
	 */
	private String tradeStyleStr;
	/**
	 * 贵金属账户可用余额
	 */
	private String accBalanceStr;
	/**
	 * 交易方式
	 */
	private String tradeMethodStr;

	/**
	 * 交易数量
	 */
	private BigDecimal tradeNum;
	private String losPrice, winPrice;
	/**
	 * 交易货币码
	 */
	private String currencyCodeStr;
	/**
	 * 钞汇
	 */
	private String cashRemit = "-";
	/**
	 * 确认后交易还是直接交易
	 */
	private String direct;
	/**
	 * 限制价格
	 */
	private BigDecimal limitPrice;
	/**
	 * 交易时汇率
	 */
	private String marketPrice;
	/**
	 * 防重机制
	 */
	private String token;
	/**
	 * 委托序号
	 */
	private String transactionIdStr;
	/**
	 * 交易账户
	 */
	private String tradeAccNumStr;
	/** 　是否是第一次 选择 */
	private boolean isFirstSelect = true;
	/** 　是否是从行情过来的 */
	private boolean isFromPrice = false;
	
	private String runDianCha ="";
//	private String buyTradeType = "";//买入贵金属种类标识
	private TextView prms_runlost_dianchaEdit;
	String maxPendingSet = null;
	String minPendingSet = null;
//	private LinearLayout lyt_trade_code;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initRightBtnForMain();
		initListenner();
		initData();
//		setLeftSelectedPosition(null);
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId();
	}

	@Override
	public void requestPSNGetTokenIdCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		token = (String) biiResponseBody.getResult();
		prmsTradeConfirm(tradeMethodStr,
				ConstantGloble.PRMS_TRADE_TRANSFLAG_SALE, tradeNum,
				currencyCodeStr, cashRemit, tradeStyleStr);

	}

	/**
	 * 贵金属卖卖确认 回调处理
	 */
	@Override
	public void prmsTradeConfirmCallBack(Object resultObj) {
		super.prmsTradeConfirmCallBack(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> map = (Map<String, String>) biiResponseBody
				.getResult();
		marketPrice = String.valueOf(map.get(Prms.PRMS_TTADE_CONFIRM_RATE));
		// 判断交易方式
		if (tradeMethodStr.equals(ConstantGloble.PRMS_TRADEMETHOD_NOW)) {// 市价即时
			prmsTradeResult08(ConstantGloble.PRMS_TRADE_TRANSFLAG_SALE,
					String.valueOf(tradeNum), currencyCodeStr, cashRemit,
					tradeStyleStr, direct, marketPrice, token);
		} else if (tradeMethodStr.equals(ConstantGloble.PRMS_TRADEMETHOD_LIMIT)) {
			prmsTradeResultLimit(ConstantGloble.PRMS_TRADE_TRANSFLAG_SALE,
					String.valueOf(tradeNum), String.valueOf(limitPrice),
					currencyCodeStr, cashRemit, tradeStyleStr, direct,
					marketPrice, token);
		}
	}

	@Override
	public void prmsTradeResultLimitCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> map = (Map<String, String>) biiResponseBody
				.getResult();
		transactionIdStr = map.get(Prms.PRMS_TTADE_RESOUT_TRANSACTIONID);
		tradeAccNumStr = map.get(Prms.PRMS_TTADE_RESOUT_ACCOUNTNUM);
		String baseRateStr = map.get(Prms.PRMS_TTADE_RESOUT_BASERATE);
		String exchangeRatStr = map.get(Prms.PRMS_TTADE_RESOUT_EXCHANGERATE);
		String sellAmount = map.get(Prms.PRMS_QUSH_SUBMIT_SELLAMOUNT);
		String sellAmountUnit = map.get(Prms.PRMS_QUERY_DEAL_SELLCURRENCYCODE);
		String buyAmount = map.get(Prms.PRMS_QUSH_SUBMIT_BUYAMOUNT);
		String buyAmountUnit = map.get(Prms.PRMS_QUERY_DEAL_BUYCURRENCYCODE);
		Intent intent = new Intent();
		intent.putExtra(Prms.PRMS_TTADE_RESOUT_TRANSACTIONID, transactionIdStr);
		// 交易账户号码
		intent.putExtra(Prms.PRMS_ACCOUNTNUM, tradeAccNumStr);
		intent.putExtra(Prms.PRMS_CURRENCYCODE, currencyCodeStr);
		intent.putExtra(Prms.PRMS_CASHREMIT, cashRemit);
		intent.putExtra(Prms.PRMS_TRADETYPE, tradeStyleStr);
		intent.putExtra(Prms.PRMS_TRADENUM, tradeNum.toString());
		intent.putExtra(Prms.PRMS_TRADEMETHOD, tradeMethodStr);
		intent.putExtra(Prms.PRMS_LIMITPRICE, limitPrice.toString());
		intent.putExtra(Prms.PRMS_TRADEPRICE, buyRateTexView.getText()
				.toString());
		intent.putExtra(Prms.PRMS_BALACNE, accBalanceStr);
		intent.putExtra(Prms.PRMS_TTADE_RESOUT_BASERATE, baseRateStr);
		intent.putExtra(Prms.PRMS_TTADE_RESOUT_EXCHANGERATE, exchangeRatStr);
		intent.putExtra(Prms.PRMS_QUSH_SUBMIT_SELLAMOUNT, sellAmount);
		intent.putExtra(Prms.PRMS_QUERY_DEAL_SELLCURRENCYCODE, sellAmountUnit);
		intent.putExtra(Prms.PRMS_QUSH_SUBMIT_BUYAMOUNT, buyAmount);//交易金额
		intent.putExtra(Prms.PRMS_QUERY_DEAL_BUYCURRENCYCODE, buyAmountUnit);//交易货币
		intent.setClass(PrmsTradeSaleActivity.this,
				PrmsSaleSuccessActivity.class);
		startActivity(intent);
	}

	/**
	 * 市价即时交易回调处理
	 */
	@Override
	public void prmsTradeResult08CallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> map = (Map<String, String>) biiResponseBody
				.getResult();
		// 委托序号
		transactionIdStr = map.get(Prms.PRMS_TTADE_RESOUT_TRANSACTIONID);
		tradeAccNumStr = map.get(Prms.PRMS_TTADE_RESOUT_ACCOUNTNUM);
		String baseRateStr = map.get(Prms.PRMS_TTADE_RESOUT_BASERATE);
		String exchangeRatStr = map.get(Prms.PRMS_TTADE_RESOUT_EXCHANGERATE);
		String sellAmount = map.get(Prms.PRMS_QUSH_SUBMIT_SELLAMOUNT);
		String sellAmountUnit = map.get(Prms.PRMS_QUERY_DEAL_SELLCURRENCYCODE);
		String buyAmount = map.get(Prms.PRMS_QUSH_SUBMIT_BUYAMOUNT);
		String buyAmountUnit = map.get(Prms.PRMS_QUERY_DEAL_BUYCURRENCYCODE);
		Intent intent = new Intent();
		// 参数
		// 交易序列号
		intent.putExtra(Prms.PRMS_TTADE_RESOUT_TRANSACTIONID, transactionIdStr);
		// 交易账户号码
		intent.putExtra(Prms.PRMS_ACCOUNTNUM, tradeAccNumStr);
		intent.putExtra(Prms.PRMS_CURRENCYCODE, currencyCodeStr);
		intent.putExtra(Prms.PRMS_CASHREMIT, cashRemit);
		intent.putExtra(Prms.PRMS_TRADETYPE, tradeStyleStr);
		intent.putExtra(Prms.PRMS_TRADENUM, tradeNum.toString());
		intent.putExtra(Prms.PRMS_TRADEMETHOD, tradeMethodStr);
		intent.putExtra(Prms.PRMS_TTADE_RESOUT_BASERATE, baseRateStr);
		intent.putExtra(Prms.PRMS_TTADE_RESOUT_EXCHANGERATE, exchangeRatStr);
		intent.putExtra(Prms.PRMS_QUSH_SUBMIT_SELLAMOUNT, sellAmount);
		intent.putExtra(Prms.PRMS_QUERY_DEAL_SELLCURRENCYCODE, sellAmountUnit);
		intent.putExtra(Prms.PRMS_QUSH_SUBMIT_BUYAMOUNT, buyAmount);//交易金额
		intent.putExtra(Prms.PRMS_QUERY_DEAL_BUYCURRENCYCODE, buyAmountUnit);//交易货币
		intent.setClass(PrmsTradeSaleActivity.this,
				PrmsSaleSuccessActivity.class);

		startActivity(intent);
	}

	/**
	 * 获取贵金属行情回调处理
	 * 
	 * @Author xyl
	 * @param resultObj
	 */
	@Override
	public void queryPrmsPriceCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, String>> data = (List<Map<String, String>>) (biiResponseBody
				.getResult());
		String rate0 = "";
		String rate1;
		if (StringUtil.isNullOrEmpty(data)) {
			return;
		}
		Map<String, String> map;
		if (isFirstSelect && isFromPrice) {// 如果是从行情过来的 并且是第一次
			isFirstSelect = false;
			map = PrmsControl.getPrmsDetailsByCurrencyCode(
					(List<Map<String, String>>) BaseDroidApp.getInstanse()
							.getBizDataMap().get(Prms.PRMS_PRICE),
					currencyCodeStr);
		} else {
			map = PrmsControl.getPrmsDetailsByCurrencyCode(data,
					currencyCodeStr);
		}

		if (map != null) {
			rate0 = String.valueOf(map.get(Prms.QUERY_TRADERATE_BUYRATE));
			rate1 = LocalData.prmsAccUnitMaptoChi.get(currencyCodeStr)
					+ LocalData.prmsTradePricUnitMaptoChi.get(currencyCodeStr);
			buyRateTexView.setText(rate0);
			buyRateUnitTv.setText(rate1);
		} else {
			buyRateTexView.setText("-");
			buyRateUnitTv.setText("");
		}

	}


	/**
	 * 初始化数据
	 */
	private void initView() {
		View v = mainInflater.inflate(R.layout.prms_trade_sale, null);
		tabcontent.addView(v);
		setTitle(getResources().getString(R.string.prms_title_trade_sale));
		StepTitleUtils.getInstance().initTitldStep(this,
				prmsControl.getStepsForPrmsTrade());
		StepTitleUtils.getInstance().setTitleStep(1);
		turnOver = (Button) findViewById(R.id.prms_trade_buy_turnover);
		next = (Button) findViewById(R.id.prms_trade_buy_next);
		tradeAcc = (TextView) findViewById(R.id.prms_trade_buy_acc);
		accBalance = (TextView) findViewById(R.id.prms_trade_buy_accbalance);
		accBalanceUnitTv = (TextView) findViewById(R.id.prms_trade_buy_accbalance_unit);
		limitUintTv = (TextView) findViewById(R.id.prms_trade_buy_limit_unit);
		winUintTv = (TextView) findViewById(R.id.prms_win_price_unit_tv);
		losUnitTv = (TextView) findViewById(R.id.prms_lose_price_unit_tv);
		buyRateTexView = (TextView) findViewById(R.id.prms_trade_buy_buyRate);
		buyRateTexView1 = (TextView) findViewById(R.id.prms_trade_buy_buyRate1);
		buyRateUnitTv = (TextView) findViewById(R.id.prms_trade_buy_buyRate_unit);
		tradeNumEditText = (EditText) findViewById(R.id.prms_trade_buy_prmsnum);
		prmsUint = (TextView) findViewById(R.id.prms_trade_buy_unit);
		tradeMethodSpinner = (Spinner) findViewById(R.id.prms_trade_buy_trademethod);
		currencySpinner = (Spinner) findViewById(R.id.prms_trade_buy_currency);
		limitPriceLayout = (LinearLayout) findViewById(R.id.prms_trade_buy_limitprice_linerlayout);
		winPriceView = (LinearLayout) findViewById(R.id.prms_trade_buy_winPrice_ll);
		losPriceView = (LinearLayout) findViewById(R.id.prms_lose_price_ll);
		entrustDateView = (LinearLayout) findViewById(R.id.prms_entrust_date_View);
		entrustDateTv = (TextView) findViewById(R.id.prms_entrust_date_tv);
		entrustTimeSpinner = (Spinner) findViewById(R.id.prms_entrust_time_sp);
		limitPriceEdit = (EditText) findViewById(R.id.prms_trade_buy_limitprice);
		winPriceEdt = (EditText) findViewById(R.id.prms_win_price_edt);
		losPriceEdt = (EditText) findViewById(R.id.prms_lose_price_edt);
		tradeNumTextView = (TextView) findViewById(R.id.prms_trade_buy_prmsnum_textview);
		
		prms_run_lose_ll = (LinearLayout)findViewById(R.id.prms_run_lose_ll);
		prms_runlost_diancha_edit = (EditText) findViewById(R.id.prms_runlost_diancha_edit);
		prms_runlost_dianchaEdit =  (TextView) findViewById(R.id.prms_runlost_dianchaEdit);
		
//		lyt_trade_code = (LinearLayout) this.findViewById(R.id.lyt_trade_code);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, 
				(TextView)findViewById(R.id.prms_trade_max_sale_num_tv));
		if (prmsControl.accId != null) {
			tradeAcc.setText(StringUtil.getForSixForString(prmsControl.accNum));
		}
		/**
		 * 603 卖出时加入
		 */
//		PrmsControl.isSale = true;
		if(!LocalData.prmsTradeMethodList.contains("追击止损委托")){
			LocalData.prmsTradeMethodList.add("追击止损委托");
		}
		ArrayAdapter<String> tradeMethodAdapter = new ArrayAdapter<String>(
				this, R.layout.dept_spinner, LocalData.prmsTradeMethodList);
		tradeMethodAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(this,
				R.layout.dept_spinner, getCurrencyAndCashmitList());
		currencyAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		tradeMethodSpinner.setAdapter(tradeMethodAdapter);
		currencySpinner.setAdapter(currencyAdapter);
		tradeMethodSpinner.setAdapter(tradeMethodAdapter);
		ArrayAdapter<String> entrustTimeAdapter = new ArrayAdapter<String>(
				this, R.layout.dept_spinner, getHourList());
		entrustTimeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		entrustTimeSpinner.setAdapter(entrustTimeAdapter);
		
	}

	private void initListenner() {
		tradeMethodSpinner.setOnItemSelectedListener(this);
		entrustTimeSpinner.setOnItemSelectedListener(this);
		currencySpinner.setOnItemSelectedListener(this);

		prms_runlost_diancha_edit.setOnClickListener(this);
		
		entrustDateTv.setOnClickListener(this);
		turnOver.setOnClickListener(this);
		next.setOnClickListener(this);
	}

	private void initData() {
		Intent i = getIntent();
		isFirstSelect = true;
		isFromPrice = i.getBooleanExtra(Prms.PRMS_IFFROMPRICE, false);
		currencyCodeStr = i
				.getStringExtra(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
		currencySpinner.setSelection(getDefaultCurrencyPosition());
		Map<String, Object> map;
		map = prmsControl.getSaleCurrencyList().get(getDefaultCurrencyPosition());
//		buyTradeType = getbuyTradeType(map);
		/* 当前卖出价格 */
		String saleRateStrtemp = getResources().getString(
				R.string.prms_trade_sale_price);
		buyRateTexView1.setText(saleRateStrtemp);
		/* 卖出数量 */
		String saleNumStrtemp = getResources().getString(
				R.string.prms_trade_sale_num);

		tradeNumTextView.setText(saleNumStrtemp);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		String tradeNumStr;
		String rexTradeNumStr = getResources().getString(
				R.string.prms_trade_sale_num_no);
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		RegexpBean regexpTradeNum, regexpLimitPrice, regexpLosPrice, regexwinPrice;

		switch (v.getId()) {
		case R.id.prms_runlost_diancha_edit:
//			//wuhan P603   贵金属挂单点差范围查询
//			queryGoldDianCha(currencyCodeStr, buyTradeType);
			break;
		case R.id.prms_entrust_date_tv:// 委托日期
			Calendar c = QueryDateUtils.getCalendarWithDate(endDate);
			DatePickerDialog dialog = new DatePickerDialog(
					PrmsTradeSaleActivity.this, new OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							/** 选择的开始日期 */
							StringBuffer date = new StringBuffer();
							date.append(String.valueOf(year));
							date.append("/");
							int month = monthOfYear + 1;
							date.append(((month < 10) ? ("0" + month)
									: (month + "")));
							date.append("/");
							date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth)
									: (dayOfMonth + "")));

							endDate = date.toString();
							/** 为EditText赋值 */
							entrustDateTv.setText(endDate);
						}
					}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH));
			dialog.show();
			break;
		case R.id.prms_trade_buy_turnover:// 直接成交
			tradeNumStr = StringUtil
					.trim(tradeNumEditText.getText().toString());
			if (buyRateTexView.getText().toString().equals("-")) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getString(R.string.prms_noprice_error));
				return;
			}
			regexpTradeNum = PrmsControl.getRegexpBeanBySourceCurrency(
					rexTradeNumStr, currencyCodeStr,
					StringUtil.trim(tradeNumStr));
			lists.add(regexpTradeNum);

			switch (PrmsControl.tradeMethodSwitch(tradeMethodStr)) {
			case PrmsControl.PRMS_TRADEMETHOD_NOW:
				if (RegexpUtils.regexpDate(lists)) {
					tradeNum = new BigDecimal(tradeNumEditText.getText()
							.toString().toCharArray());
				} else {
					return;
				}
				if (accBalanceStr != null) {
					BigDecimal accBalanceNum = new BigDecimal(
							accBalanceStr.toCharArray());
					if (tradeNum.compareTo(accBalanceNum) == 1) {
						BaseDroidApp
								.getInstanse()
								.showInfoMessageDialog(
										getString(R.string.prms_sale_accbalance_loss_error));
						return;
					}
				}
				break;
			case PrmsControl.PRMS_TRADEMETHOD_LIMIT:
				String limitPricStr = StringUtil.trim(limitPriceEdit.getText()
						.toString());
//				regexpLimitPrice = new RegexpBean(tradeNumTextView.getText()
//						.toString(), limitPricStr, "price");
				regexpLimitPrice = PrmsControl.getRegexpBeanPriceBySourceCurrency(
						rexTradeNumStr, currencyCodeStr,
						StringUtil.trim(limitPricStr));
				lists.add(regexpLimitPrice);
				if (RegexpUtils.regexpDate(lists)) {
					tradeNum = new BigDecimal(tradeNumStr.toCharArray());
					limitPrice = new BigDecimal(limitPricStr.toCharArray());
				} else {
					return;
				}
				BigDecimal balance = new BigDecimal(accBalance.getText()
						.toString());
				if (tradeNum.compareTo(balance) == 1) {// tradeNum 大于账户余额
					BaseDroidApp
							.getInstanse()
							.showInfoMessageDialog(
									getString(R.string.prms_sale_accbalance_loss_error));
					return;
				}
				break;
			case PrmsControl.PRMS_TRADEMETHOD_WIN:
				regexwinPrice = new RegexpBean(
						getString(R.string.prms_str_winprice),
						StringUtil.trim(winPriceEdt.getText().toString()),
						"price");
				lists.add(regexwinPrice);
				break;
			case PrmsControl.PRMS_TRADEMETHOD_LOSE:
				regexpLosPrice = new RegexpBean(
						getString(R.string.prms_str_loseprice),
						StringUtil.trim(losPriceEdt.getText().toString()),
						"price");
				lists.add(regexpLosPrice);
				break;
			case PrmsControl.PRMS_TRADEMETHOD_ONEINWTO:
				regexwinPrice = new RegexpBean(
						getString(R.string.prms_str_winprice),
						StringUtil.trim(winPriceEdt.getText().toString()),
						"price");
				regexpLosPrice = new RegexpBean(
						getString(R.string.prms_str_loseprice),
						StringUtil.trim(losPriceEdt.getText().toString()),
						"price");
				lists.add(regexwinPrice);
				lists.add(regexpLosPrice);
				break;
			}
			// 如果数据格式都正确
			direct = ConstantGloble.PRMS_DIRECT_TURNOVER;
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();
			break;
		case R.id.prms_trade_buy_next:// 下一步
			Intent intent = new Intent();
			intent.setClass(PrmsTradeSaleActivity.this,
					PrmsSaleConfirmActivity.class);
			direct = ConstantGloble.PRMS_DIRECT_CONFIRM;
			tradeNumStr = StringUtil
					.trim(tradeNumEditText.getText().toString());//当前卖出数量
			if (buyRateTexView.getText().toString().equals("-")) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						"该地区尚未开通该贵金属服务，暂不能交易！");
				return;
			}
			regexpTradeNum = PrmsControl.getRegexpBeanBySourceCurrency(
					rexTradeNumStr, currencyCodeStr,
					StringUtil.trim(tradeNumStr));
			lists.add(regexpTradeNum);
			if (RegexpUtils.regexpDate(lists)) {
				tradeNum = new BigDecimal(tradeNumEditText.getText()
						.toString().toCharArray());
			} else {
				return;
			}
			if (accBalanceStr != null) {
				if (tradeNum.compareTo(new BigDecimal(
						accBalanceStr.toCharArray())) == 1) {
					BaseDroidApp
							.getInstanse()
							.showInfoMessageDialog(
									getString(R.string.prms_sale_accbalance_loss_error));
					return;
				}
			}
			switch (PrmsControl.tradeMethodSwitch(tradeMethodStr)) {
			case PrmsControl.PRMS_TRADEMETHOD_NOW:
				if (RegexpUtils.regexpDate(lists)) {
				} else {
					return;
				}
				break;
			case PrmsControl.PRMS_TRADEMETHOD_LIMIT:
				String limitPricStr = StringUtil.trim(limitPriceEdit.getText()
						.toString());
//				regexpLimitPrice = new RegexpBean(getResources().getString(R.string.prms_str_limitprice_no), limitPricStr, "price");
				regexpLimitPrice = PrmsControl.getRegexpBeanPriceBySourceCurrency(
						getResources().getString(R.string.prms_str_limitprice_no), currencyCodeStr,
						StringUtil.trim(limitPricStr));
				lists.add(regexpLimitPrice);
				if (RegexpUtils.regexpDate(lists)) {
					limitPrice = new BigDecimal(limitPricStr.toCharArray());
					intent.putExtra(Prms.PRMS_LIMITPRICE, limitPricStr);
				} else {
					return;
				}
				break;
			case PrmsControl.PRMS_TRADEMETHOD_WIN:
				winPrice = StringUtil.trim(winPriceEdt.getText().toString());
				regexwinPrice = PrmsControl.getRegexpBeanPriceBySourceCurrency(
						getString(R.string.prms_str_winprice), currencyCodeStr,
						winPrice);
				lists.add(regexwinPrice);
				if (RegexpUtils.regexpDate(lists)) {
					intent.putExtra(Prms.PRMS_WINPRICE, winPrice);
				} else {
					return;
				}
				intent.putExtra(Prms.PRMS_ENDDATE, entrustDateTv.getText()
						.toString());
				intent.putExtra(Prms.PRMS_ENDHOUR, endHour);
				break;
			case PrmsControl.PRMS_TRADEMETHOD_LOSE:
				losPrice = StringUtil.trim(losPriceEdt.getText().toString());
//				regexwinPrice = new RegexpBean(
//						getString(R.string.prms_str_loseprice), losPrice,
//						"price");
				regexwinPrice = PrmsControl.getRegexpBeanPriceBySourceCurrency(
				getString(R.string.prms_str_loseprice), currencyCodeStr,
				losPrice);
				lists.add(regexwinPrice);
				if (RegexpUtils.regexpDate(lists)) {
					intent.putExtra(Prms.PRMS_LOSEPRICE, losPrice);
				} else {
					return;
				}
				intent.putExtra(Prms.PRMS_ENDDATE, entrustDateTv.getText()
						.toString());
				intent.putExtra(Prms.PRMS_ENDHOUR, endHour);
				break;
			case PrmsControl.PRMS_TRADEMETHOD_ONEINWTO:
				losPrice = StringUtil.trim(losPriceEdt.getText().toString());
				winPrice = StringUtil.trim(winPriceEdt.getText().toString());
				regexwinPrice =  PrmsControl.getRegexpBeanPriceBySourceCurrency(
						getString(R.string.prms_str_winprice), currencyCodeStr,
						winPrice);
				regexpLosPrice = PrmsControl.getRegexpBeanPriceBySourceCurrency(
						getString(R.string.prms_str_loseprice), currencyCodeStr,
						losPrice);
				lists.add(regexwinPrice);
				lists.add(regexpLosPrice);
				if (RegexpUtils.regexpDate(lists)) {
					intent.putExtra(Prms.PRMS_WINPRICE, winPrice);
					intent.putExtra(Prms.PRMS_LOSEPRICE, losPrice);
				} else {
					return;
				}
				intent.putExtra(Prms.PRMS_ENDDATE, entrustDateTv.getText()
						.toString());
				intent.putExtra(Prms.PRMS_ENDHOUR, endHour);
				break;
			case PrmsControl.PRMS_TRADEMETHOD_RUNLOST://追击止损委托
				runDianCha = StringUtil.trim(prms_runlost_diancha_edit.getText().toString());
//				int max = Integer.valueOf(maxPendingSet) ;
//				int min = Integer.valueOf(minPendingSet);
//				if(max>=Integer.valueOf(runDianCha)&& min <= Integer.valueOf(runDianCha)){
//					intent.putExtra(Prms.PRMS_LOSEPRICE, runDianCha);
//					intent.putExtra(Prms.PRMS_ENDDATE, entrustDateTv.getText()
//							.toString());
//					intent.putExtra(Prms.PRMS_ENDHOUR, endHour);
//					break;
//				}else 
				if("0".equals(runDianCha)||runDianCha.contains(".")){
					BaseDroidApp.getInstanse().showInfoMessageDialog("只能输入整数且不能为0");
					return;
				}
				else if("".equals(runDianCha)||runDianCha == null){
					BaseDroidApp.getInstanse().showInfoMessageDialog("请输入追击点差");
					return;
				}else
				{
//					BaseDroidApp.getInstanse().showInfoMessageDialog("请输入正确的追击点差");
//					return;
					intent.putExtra(Prms.PRMS_LOSEPRICE, runDianCha);
					intent.putExtra(Prms.PRMS_ENDDATE, entrustDateTv.getText()
							.toString());
					intent.putExtra(Prms.PRMS_ENDHOUR, endHour);
					break;
				}
				
				

			}
			// 如果数据格式都正确tradeNum
			// 交易数量
			intent.putExtra(Prms.PRMS_TRADENUM, tradeNum.toString());
			// 交易货币码
			intent.putExtra(Prms.PRMS_CURRENCYCODE, currencyCodeStr);
			// 钞汇
			intent.putExtra(Prms.PRMS_CASHREMIT, cashRemit);
			// 交易种类 s还是G
			intent.putExtra(Prms.PRMS_TRADETYPE, tradeStyleStr);
			intent.putExtra(Prms.PRMS_TRADEPRICE, buyRateTexView.getText()
					.toString());
			intent.putExtra(Prms.PRMS_BALACNE, accBalanceStr);
			// 交易方式
			intent.putExtra(Prms.PRMS_TRADEMETHOD, tradeMethodStr);
			startActivity(intent);
			break;
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.prms_entrust_time_sp:// 日期选择
			endHour = getHourList().get(position);
			break;
		case R.id.prms_trade_buy_trademethod:// 交易方式
			cleanInputPrice();
			switch (position) {
			case 0:
				// 市价即时
				tradeMethodStr = ConstantGloble.PRMS_TRADEMETHOD_NOW;
				setViewByTransMethod(tradeMethodStr);
				break;
			case 1:
				// 限价即时
				tradeMethodStr = ConstantGloble.PRMS_TRADEMETHOD_LIMIT;
				setViewByTransMethod(tradeMethodStr);
				limitUintTv.setText(LocalData.prmsTradePricUnitMaptoChi
						.get(currencyCodeStr));
				break;
			case 2:// 获利
				tradeMethodStr = ConstantGloble.PRMS_TRADEMETHOD_WIN;
				setViewByTransMethod(tradeMethodStr);
				winUintTv.setText(LocalData.prmsTradePricUnitMaptoChi
						.get(currencyCodeStr));
				if (endDate == null) {
					BaseHttpEngine.showProgressDialogCanGoBack();
					requestSystemDateTime();
				}
				break;
			case 3:// 止损
				tradeMethodStr = ConstantGloble.PRMS_TRADEMETHOD_LOSE;
				setViewByTransMethod(tradeMethodStr);
				losUnitTv.setText(LocalData.prmsTradePricUnitMaptoChi
						.get(currencyCodeStr));
				if (endDate == null) {
					BaseHttpEngine.showProgressDialogCanGoBack();
					requestSystemDateTime();
				}
				break;
			case 4:// 二选一
				tradeMethodStr = ConstantGloble.PRMS_TRADEMETHOD_ONEINWTO;
				setViewByTransMethod(tradeMethodStr);
				winUintTv.setText(LocalData.prmsTradePricUnitMaptoChi
						.get(currencyCodeStr));
				losUnitTv.setText(LocalData.prmsTradePricUnitMaptoChi
						.get(currencyCodeStr));
				if (endDate == null) {
					BaseHttpEngine.showProgressDialogCanGoBack();
					requestSystemDateTime();
				}
				break;
			case 5://追击止损  委 托（卖出）
				tradeMethodStr = ConstantGloble.PRMS_TRADEMETHOD_RUNLOSE;
				setViewByTransMethod(tradeMethodStr);
//				lyt_trade_code.setVisibility(View.GONE);
				prmsUint.setText(LocalData.prmsTradePricUnitMaptoChi.get(currencyCodeStr));
				if (endDate == null) {
					BaseHttpEngine.showProgressDialogCanGoBack();
					requestSystemDateTime();
				}
				//wuhan P603   贵金属挂单点差范围查询
				queryGoldDianCha(currencyCodeStr, tradeStyleStr,ConstantGloble.PRMS_TRADE_TRANSFLAG_SALE);//buyTradeType
				break;
			}
			break;
		case R.id.prms_trade_buy_currency:// 币种
			tradeNumEditText.requestFocus();
			cleanInputAccount();
			cleanInputPrice();
			Map<String, Object> map;
			map = prmsControl.getSaleCurrencyList().get(position);
			currencyCodeStr = String.valueOf(map
					.get(Prms.QUERY_PEMSACTBALANCE_CODE));
			//wuhan P603
//			buyTradeType = getbuyTradeType(map);
			
			prmsUint.setText(LocalData.prmsUnitMaptoChi1.get(currencyCodeStr));
			cashRemit = String.valueOf(map
					.get(Prms.QUERY_PEMSACTBALANCE_CASHREMIT));
			limitUintTv.setText(LocalData.prmsTradePricUnitMaptoChi
					.get(currencyCodeStr));
			winUintTv.setText(LocalData.prmsTradePricUnitMaptoChi
					.get(currencyCodeStr));
			losUnitTv.setText(LocalData.prmsTradePricUnitMaptoChi
					.get(currencyCodeStr));
			tradeStyleStr = LocalData.prmsStyle1MaptoChi.get(currencyCodeStr);
			accBalanceStr = String.valueOf(map
					.get(Prms.QUERY_PEMSACTBALANCE_AVAILABLEBALANCE));
			accBalance.setText(accBalanceStr);
			accBalanceUnitTv.setText(LocalData.prmsUnitMaptoChi
					.get(currencyCodeStr));
			((TextView)findViewById(R.id.prms_trade_max_sale_num)).setText(accBalanceStr);
			((TextView)findViewById(R.id.prms_trade_max_sale_num_unit))
				.setText(LocalData.prmsUnitMaptoChi1.get(currencyCodeStr));
			BaseHttpEngine.showProgressDialog();
			queryPrmsPrice();
			break;
		default:
			break;
		}

	}

	public String getbuyTradeType(Map<String, Object> map){
		String buyTraeType = "";
		if (map.get(Prms.QUERY_PEMSACTBALANCE_CODE).equals(
				ConstantGloble.PRMS_CURRENCYCODE_DOLORG)
				|| // 美元金
				map.get(Prms.QUERY_PEMSACTBALANCE_CODE).equals(
						ConstantGloble.PRMS_CURRENCYCODE_RMBG)
				// 人民币金
				
		){
			buyTraeType = "G";
		} else if(map.get(Prms.QUERY_PEMSACTBALANCE_CODE).equals(
				ConstantGloble.PRMS_CURRENCYCODE_RMBS)||map.get(Prms.QUERY_PEMSACTBALANCE_CODE).equals(
						ConstantGloble.PRMS_CURRENCYCODE_DOLORS)
				)// 美元银
		{
			buyTraeType = "S";
		}	else if(map.get(Prms.QUERY_PEMSACTBALANCE_CODE).equals(
				ConstantGloble.PRMS_CURRENCYCODE_DOLORBOG)||
				map.get(Prms.QUERY_PEMSACTBALANCE_CODE).equals(
						ConstantGloble.PRMS_CURRENCYCODE_RMBBOG)){
			buyTraeType = "T";
		}else if(map.get(Prms.QUERY_PEMSACTBALANCE_CODE).equals(
				ConstantGloble.PRMS_CURRENCYCODE_RMBBAG) ||
		map.get(Prms.QUERY_PEMSACTBALANCE_CODE).equals(
				ConstantGloble.PRMS_CURRENCYCODE_DOLORBAG)){
			buyTraeType = "D";
		}
		
			
		return buyTraeType;
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	/**
	 * 获取显示的币种和炒汇数据列表
	 * 
	 * @return
	 */
	private List<String> getCurrencyAndCashmitList() {
		List<String> tempList = new ArrayList<String>();
		for (Map<String, Object> map : prmsControl
				.getSaleCurrencyList(prmsControl.accBalanceList)) {
			String currencyCode = String.valueOf(map
					.get(Prms.QUERY_PEMSACTBALANCE_CODE));
			String cashmit = String.valueOf(map.get(Prms.PRMS_CASHREMIT));
			String temp = LocalData.Currency.get(currencyCode);
			if (!LocalData.CurrencyCashremit.get(cashmit).equals("-")) {// 如果不是人民币
				temp += " ";
				temp += LocalData.CurrencyCashremit.get(cashmit);
			}
			tempList.add(temp);
		}
		return tempList;
	}

	/**
	 * 币种默认显示的位置
	 * 
	 * @return
	 */
	private int getDefaultCurrencyPosition() {
		if (StringUtil.isNullOrEmpty(currencyCodeStr)) {
			return 0;
		} else {
			int i = 0;
			for (Map<String, Object> map : prmsControl.getSaleCurrencyList()) {
				String currencyCode = String.valueOf(map
						.get(Prms.QUERY_PEMSACTBALANCE_CODE));
				if (currencyCode.equals(currencyCodeStr)) {
					return i;
				}
				i++;
			}
		}
		return 0;

	}

	private void setViewByTransMethod(String transMethod) {
		switch (PrmsControl.tradeMethodSwitch(transMethod)) {
		case PrmsControl.PRMS_TRADEMETHOD_LIMIT:
			losPriceView.setVisibility(View.GONE);
			winPriceView.setVisibility(View.GONE);
			prms_run_lose_ll.setVisibility(View.GONE);
			limitPriceLayout.setVisibility(View.VISIBLE);
			entrustDateView.setVisibility(View.GONE);
			turnOver.setVisibility(View.GONE);
			next.setVisibility(View.VISIBLE);
			break;
		case PrmsControl.PRMS_TRADEMETHOD_LOSE:
			losPriceView.setVisibility(View.VISIBLE);
			winPriceView.setVisibility(View.GONE);
			limitPriceLayout.setVisibility(View.GONE);
			prms_run_lose_ll.setVisibility(View.GONE);
			entrustDateView.setVisibility(View.VISIBLE);
			turnOver.setVisibility(View.GONE);
			next.setVisibility(View.VISIBLE);
			break;
		case PrmsControl.PRMS_TRADEMETHOD_WIN:
			losPriceView.setVisibility(View.GONE);
			winPriceView.setVisibility(View.VISIBLE);
			limitPriceLayout.setVisibility(View.GONE);
			entrustDateView.setVisibility(View.VISIBLE);
			prms_run_lose_ll.setVisibility(View.GONE);
			turnOver.setVisibility(View.GONE);
			next.setVisibility(View.VISIBLE);
			break;
		case PrmsControl.PRMS_TRADEMETHOD_ONEINWTO:
			losPriceView.setVisibility(View.VISIBLE);
			winPriceView.setVisibility(View.VISIBLE);
			limitPriceLayout.setVisibility(View.GONE);
			prms_run_lose_ll.setVisibility(View.GONE);
			entrustDateView.setVisibility(View.VISIBLE);
			turnOver.setVisibility(View.GONE);
			next.setVisibility(View.VISIBLE);
			break;
		case PrmsControl.PRMS_TRADEMETHOD_NOW:
			losPriceView.setVisibility(View.GONE);
			winPriceView.setVisibility(View.GONE);
			limitPriceLayout.setVisibility(View.GONE);
			entrustDateView.setVisibility(View.GONE);
			turnOver.setVisibility(View.VISIBLE);
			prms_run_lose_ll.setVisibility(View.GONE);
			next.setVisibility(View.GONE);
			break;
		case PrmsControl.PRMS_TRADEMETHOD_RUNLOST://显示追击止损委托的布局
			//添加一下追击止损布局
			prms_run_lose_ll.setVisibility(View.VISIBLE);
			losPriceView.setVisibility(View.GONE);
			winPriceView.setVisibility(View.GONE);
			limitPriceLayout.setVisibility(View.GONE);
			entrustDateView.setVisibility(View.VISIBLE);
			turnOver.setVisibility(View.GONE);
			next.setVisibility(View.VISIBLE);
			
			break;
		}
	}

	/**
	 * endHour 数据源
	 * 
	 * @return
	 */
	private List<String> getHourList() {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 24; i++) {
			if (i < 10) {
				list.add("0" + i);
			} else {
				list.add(String.valueOf(i));
			}
		}
		return list;
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		endDate = QueryDateUtils.getcurrentDate(dateTime);
		entrustDateTv.setText(endDate);
		String min = QueryDateUtils.getMinute(dateTime);
		String hour = QueryDateUtils.getHours(dateTime);
		entrustTimeSpinner.setSelection(PrmsControl.getMinEndHourByTime(min,
				hour));
	}
	/**
	 * 清空数量输入框
	 */
	private final void cleanInputAccount(){
		tradeNumEditText.setText("");
	}
	/**
	 * 清空价格输入框
	 */
	private final void cleanInputPrice(){
		limitPriceEdit.setText("");
		winPriceEdt.setText("");
		losPriceEdt.setText("");
	}
	
	@Override
	public void queryGoldDianChaCallBack(Object resultObj) {
		super.queryGoldDianChaCallBack(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> map = (Map<String, String>) biiResponseBody.getResult();
		maxPendingSet = map.get("maxPendingSet");
		minPendingSet = map.get("minPendingSet");
		prms_runlost_dianchaEdit.setText(getResources().getString(R.string.prms_runlost_dianchaEdit)+minPendingSet+"-"+maxPendingSet);
		
	}
	
}
