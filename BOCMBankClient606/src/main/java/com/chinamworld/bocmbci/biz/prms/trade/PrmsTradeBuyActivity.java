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
import com.chinamworld.bocmbci.log.LogGloble;
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
public class PrmsTradeBuyActivity extends PrmsBaseActivity implements
		OnItemSelectedListener {
	private static final String TAG = "PrmsTradeBuyActivity";
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
	 * 贵金属种类
	 * 
	 */
	private Spinner tradeStyleSpinner;
	/**
	 * 交易币种
	 */
	private Spinner currencySpinner;
	private ArrayAdapter<String> currencyAdapter;
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
	 * 限价 获利 止损 委托日期
	 */
	private LinearLayout limitPriceLayout, winPriceView, losPriceView,
			entrustDateView ,prms_run_lose_ll;
	private EditText limitPriceEdit, winPriceEdt, losPriceEdt;
	private TextView entrustDateTv;
	private Spinner entrustTimeSpinner;
	/** 截止日 截止时 */
	private String endDate = null, endHour = null;

	/**
	 * 交易贵金属类型 G /S /钯金
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

	/** 交易数量 的验证 内容 */
	private String rexTradeNumStr;
	/**
	 * 交易数量
	 */
	private BigDecimal tradeNum;
	/**
	 * 交易货币码
	 */
	private String targetCurrencyCodeStr;
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
	private BigDecimal limitPrice, losPriceBig, winPriceBig;

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
	private String sourceCurrencyCodeStr;

	/** 是否是第一次选择 第一次选择不用查询行情　 */
	private boolean isFirstSelect = true;

	private List<String> prmsTypeList;
	private String rate0 = "-";
	private String rate1 = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initData();
		initListenner();
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
				ConstantGloble.PRMS_TRADE_TRANSFLAG_BUY, tradeNum,
				targetCurrencyCodeStr, cashRemit, tradeStyleStr);
//		// 判断交易方式
//		if (tradeMethodStr.equals(ConstantGloble.PRMS_TRADEMETHOD_NOW)) {// 市价即时
//			prmsTradeResult08(ConstantGloble.PRMS_TRADE_TRANSFLAG_BUY,
//					String.valueOf(tradeNum), targetCurrencyCodeStr, cashRemit,
//					tradeStyleStr, direct, marketPrice, token);
//		} else if (tradeMethodStr.equals(ConstantGloble.PRMS_TRADEMETHOD_LIMIT)) {
//			prmsTradeResultLimit(ConstantGloble.PRMS_TRADE_TRANSFLAG_BUY,
//					String.valueOf(tradeNum), String.valueOf(limitPrice),
//					targetCurrencyCodeStr, cashRemit, tradeStyleStr, direct,
//					marketPrice, token);
//		}
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
		prmsTradeResult08(ConstantGloble.PRMS_TRADE_TRANSFLAG_BUY,
				String.valueOf(tradeNum), targetCurrencyCodeStr, cashRemit,
				tradeStyleStr, direct, marketPrice, token);
	}

	@Override
	public void prmsTradeResultLimitCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> map = (Map<String, String>) biiResponseBody
				.getResult();
		// 委托序号
		transactionIdStr = map.get(Prms.PRMS_TTADE_RESOUT_TRANSACTIONID);
		tradeAccNumStr = map.get(Prms.PRMS_TTADE_RESOUT_ACCOUNTNUM);
		String buyAmount = map.get(Prms.PRMS_QUSH_SUBMIT_BUYAMOUNT);
		String buyAmountUnit = map.get(Prms.PRMS_QUERY_DEAL_BUYCURRENCYCODE);
		String sellAmount = map.get(Prms.PRMS_QUSH_SUBMIT_SELLAMOUNT);
		String sellAmountUnit = map.get(Prms.PRMS_QUERY_DEAL_SELLCURRENCYCODE);
		// TODO 返回的成交价格都是空
		Intent intent = new Intent();
		// 参数
		// 用于判断显示买入还是卖出数量
		// 交易序列号
		intent.putExtra(Prms.PRMS_TTADE_RESOUT_TRANSACTIONID, transactionIdStr);
		// 交易账户号码
		intent.putExtra(Prms.PRMS_ACCOUNTNUM, tradeAccNumStr);
		intent.putExtra(Prms.PRMS_CURRENCYCODE, targetCurrencyCodeStr);
		intent.putExtra(Prms.PRMS_CASHREMIT, cashRemit);
		intent.putExtra(Prms.PRMS_TRADETYPE, tradeStyleStr);
		intent.putExtra(Prms.PRMS_TRADENUM, tradeNum.toString());
		intent.putExtra(Prms.PRMS_TRADEMETHOD, tradeMethodStr);
		intent.putExtra(Prms.PRMS_LIMITPRICE, limitPrice.toString());
		intent.putExtra(Prms.PRMS_TRADEPRICE, buyRateTexView.getText()
				.toString());
		intent.putExtra(Prms.PRMS_BALACNE, accBalanceStr);
		intent.putExtra(Prms.PRMS_QUSH_SUBMIT_BUYAMOUNT, buyAmount);//交易金额
		intent.putExtra(Prms.PRMS_QUERY_DEAL_BUYCURRENCYCODE, buyAmountUnit);//交易货币
		intent.putExtra(Prms.PRMS_QUSH_SUBMIT_SELLAMOUNT, sellAmount);
		intent.putExtra(Prms.PRMS_QUERY_DEAL_SELLCURRENCYCODE, sellAmountUnit);
		intent.setClass(PrmsTradeBuyActivity.this, PrmsBuySuccessActivity.class);
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
		String baseRateStr = map.get(Prms.PRMS_TTADE_RESOUT_BASERATE);
		String exchangeRatStr = map.get(Prms.PRMS_TTADE_RESOUT_EXCHANGERATE);
		// 委托序号
		transactionIdStr = map.get(Prms.PRMS_TTADE_RESOUT_TRANSACTIONID);
		tradeAccNumStr = map.get(Prms.PRMS_TTADE_RESOUT_ACCOUNTNUM);
		String buyAmount = map.get(Prms.PRMS_QUSH_SUBMIT_BUYAMOUNT);
		String buyAmountUnit = map.get(Prms.PRMS_QUERY_DEAL_BUYCURRENCYCODE);
		String sellAmount = map.get(Prms.PRMS_QUSH_SUBMIT_SELLAMOUNT);
		String sellAmountUnit = map.get(Prms.PRMS_QUERY_DEAL_SELLCURRENCYCODE);
		Intent intent = new Intent();
		// 参数
		// 交易序列号
		intent.putExtra(Prms.PRMS_TTADE_RESOUT_TRANSACTIONID, transactionIdStr);
		// 交易账户号码
		intent.putExtra(Prms.PRMS_ACCOUNTNUM, tradeAccNumStr);
		intent.putExtra(Prms.PRMS_CURRENCYCODE, targetCurrencyCodeStr);
		intent.putExtra(Prms.PRMS_CASHREMIT, cashRemit);
		intent.putExtra(Prms.PRMS_TRADETYPE, tradeStyleStr);
		intent.putExtra(Prms.PRMS_TRADENUM, tradeNum.toString());
		intent.putExtra(Prms.PRMS_TRADEMETHOD, tradeMethodStr);
		intent.putExtra(Prms.PRMS_TTADE_RESOUT_BASERATE, baseRateStr);
		intent.putExtra(Prms.PRMS_TTADE_RESOUT_EXCHANGERATE, exchangeRatStr);
		intent.putExtra(Prms.PRMS_QUSH_SUBMIT_BUYAMOUNT, buyAmount);//交易金额
		intent.putExtra(Prms.PRMS_QUERY_DEAL_BUYCURRENCYCODE, buyAmountUnit);//交易货币
		intent.putExtra(Prms.PRMS_QUSH_SUBMIT_SELLAMOUNT, sellAmount);
		intent.putExtra(Prms.PRMS_QUERY_DEAL_SELLCURRENCYCODE, sellAmountUnit);
		intent.setClass(PrmsTradeBuyActivity.this, PrmsBuySuccessActivity.class);
		startActivity(intent);
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		endDate = QueryDateUtils.getcurrentDate(dateTime);
		entrustDateTv.setText(endDate);
		String hour = QueryDateUtils.getHours(dateTime);
		String min = QueryDateUtils.getMinute(dateTime);
		entrustTimeSpinner.setSelection(PrmsControl.getMinEndHourByTime(min,
				hour));
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
		for (Map<String, String> map : data) {
			if (map.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE).equals(
					prmsControl.getCurrencyCode(targetCurrencyCodeStr,
							tradeStyleStr))
					&& map.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE).equals(
							targetCurrencyCodeStr)) {
				rate0 = StringUtil.valueOf1(map
						.get(Prms.QUERY_TRADERATE_SALERATE));
			}
		}
		if (rate0.equals("") || rate0.equals("-")) {// 如果没有价格的
			buyRateTexView.setText("-");
			buyRateUnitTv.setText("");
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					"该地区尚未开通该贵金属服务，暂不能交易！");
			return;
		} else {
			rate1 = LocalData.Currency.get(targetCurrencyCodeStr)
					+ LocalData.prmsTradePricUnitMaptoChi
							.get(targetCurrencyCodeStr);

			buyRateTexView.setText(rate0);
			buyRateUnitTv.setText(rate1);
//			setMaxBuyNum(accBalanceStr, rate0, LocalData.prmsUnitMaptoChi1.get(targetCurrencyCodeStr));
			setMaxBuyNum(accBalanceStr, rate0, targetCurrencyCodeStr);
		}

	}

	/**
	 * 初始化数据
	 */
	private void init() {
		View v = mainInflater.inflate(R.layout.prms_trade_buy, null);
		tabcontent.addView(v);
		setLeftButtonPopupGone();
		setTitle(getResources().getString(R.string.prms_title_trade_buy));
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
		tradeStyleSpinner = (Spinner) findViewById(R.id.prms_trade_buy_prmsstyle);
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
		
		prms_run_lose_ll = (LinearLayout) findViewById(R.id.prms_run_lose_ll);
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, 
				(TextView)findViewById(R.id.prms_trade_max_buy_num_tv));
		if (prmsControl.accNum != null) {
			tradeAcc.setText(StringUtil.getForSixForString(prmsControl.accNum));
		}
		
		//p603加入
		PrmsControl.isSale = false;
		if(LocalData.prmsTradeMethodList.contains("追击止损委托")){
		int i = LocalData.prmsTradeMethodList.indexOf("追击止损委托");
			LocalData.prmsTradeMethodList.remove(i);
		}
		ArrayAdapter<String> tradeMethodAdapter = new ArrayAdapter<String>(
				this, R.layout.dept_spinner, LocalData.prmsTradeMethodList);
		tradeMethodAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		prmsTypeList = getTradeTypeDateList();
		if (prmsTypeList.size() > 0) {
			ArrayAdapter<String> tradeStyleAdapter = new ArrayAdapter<String>(
					this, R.layout.dept_spinner, prmsTypeList);
			tradeStyleAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			tradeStyleSpinner.setAdapter(tradeStyleAdapter);
		}
		List<String> currencyDataList = getCurrencyShowList();
		if (currencyDataList.size() <= 0) {// 对没有任何余额时做处理
			buyRateTexView1.setText("-");
			currencyAdapter = null;
		} else {
			currencyAdapter = new ArrayAdapter<String>(this,
					R.layout.dept_spinner, currencyDataList);
			currencyAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			currencySpinner.setAdapter(currencyAdapter);
		}
		tradeMethodSpinner.setAdapter(tradeMethodAdapter);
		ArrayAdapter<String> entrustTimeAdapter = new ArrayAdapter<String>(
				this, R.layout.dept_spinner, getHourList());
		entrustTimeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		entrustTimeSpinner.setAdapter(entrustTimeAdapter);
		initRightBtnForMain();
	}

	private void initListenner() {
		tradeMethodSpinner.setOnItemSelectedListener(this);
		tradeStyleSpinner.setOnItemSelectedListener(this);
		currencySpinner.setOnItemSelectedListener(this);
		entrustTimeSpinner.setOnItemSelectedListener(this);

		entrustDateTv.setOnClickListener(this);
		turnOver.setOnClickListener(this);
		next.setOnClickListener(this);
	}

	private void initData() {
		Intent i = getIntent();
		targetCurrencyCodeStr = i
				.getStringExtra(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);//
		sourceCurrencyCodeStr = i
				.getStringExtra(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);//
		if (sourceCurrencyCodeStr != null) {
			tradeStyleStr = LocalData.prmsStyle1MaptoChi
					.get(sourceCurrencyCodeStr);
			int position = getTypePosition(sourceCurrencyCodeStr);
			tradeStyleSpinner.setSelection(position);
			currencySpinner.setSelection(getCurrencyAdapterSelection(
					targetCurrencyCodeStr, getCurrencyShowList()));
		}

		// 当前买入价格
		String buyRateStrtemp = getResources().getString(
				R.string.prms_trade_buy_price);
		buyRateTexView1.setText(buyRateStrtemp);
		/* 买入数量 */
		String buyNumtemp = getResources().getString(
				R.string.prms_trade_buy_num);
		rexTradeNumStr = getResources().getString(
				R.string.prms_trade_buy_num_no);
		tradeNumTextView.setText(buyNumtemp);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (currencyAdapter == null) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.prms_balancesale_all_null_error));
			return;
		}
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		RegexpBean regexpTradeNum;
		RegexpBean regexpLimitPrice;
		String tradeNumStr;
		switch (v.getId()) {
		case R.id.prms_entrust_date_tv:// 委托日期
			Calendar c = QueryDateUtils.getCalendarWithDate(endDate);
			DatePickerDialog dialog = new DatePickerDialog(
					PrmsTradeBuyActivity.this, new OnDateSetListener() {

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
			if (buyRateTexView.getText().toString().equals("-")) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getString(R.string.prms_noprice_error));
				return;
			}
			tradeNumStr = StringUtil
					.trim(tradeNumEditText.getText().toString());
			regexpTradeNum = PrmsControl.getRegexpBeanBySourceCurrency(
					rexTradeNumStr, prmsControl.getCurrencyCode(
							targetCurrencyCodeStr, tradeStyleStr), StringUtil
							.trim(tradeNumStr));
			lists.add(regexpTradeNum);
			if (RegexpUtils.regexpDate(lists)) {
				tradeNum = new BigDecimal(tradeNumStr.toCharArray());
			} else {
				return;
			}
			// 如果数据格式都正确
			direct = ConstantGloble.PRMS_DIRECT_TURNOVER;
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();

			break;
		case R.id.prms_trade_buy_next:// 下一步
			if (buyRateTexView.getText().toString().equals("-")) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getString(R.string.prms_noprice_error));
				return;
			}
			tradeNumStr = StringUtil
					.trim(tradeNumEditText.getText().toString());
			regexpTradeNum = PrmsControl.getRegexpBeanBySourceCurrency(
					rexTradeNumStr, prmsControl.getCurrencyCode(
							targetCurrencyCodeStr, tradeStyleStr), StringUtil
							.trim(tradeNumStr));
			lists.add(regexpTradeNum);
			direct = ConstantGloble.PRMS_DIRECT_CONFIRM;
			Intent intent = new Intent();
			intent.setClass(PrmsTradeBuyActivity.this,
					PrmsBuyConfirmActivity.class);
			if (tradeMethodStr.equals(ConstantGloble.PRMS_TRADEMETHOD_NOW)) {// 市价即时
				if (RegexpUtils.regexpDate(lists)) {
					tradeNum = new BigDecimal(tradeNumStr.toCharArray());
				} else {
					return;
				}
			} else if (tradeMethodStr
					.equals(ConstantGloble.PRMS_TRADEMETHOD_LIMIT)) {// 限价即时
				String limitPricStr = StringUtil.trim(limitPriceEdit.getText()
						.toString());
				// regexpLimitPrice = new RegexpBean(
				// getString(R.string.prms_str_limitprice_no),
				// limitPricStr, "price");
				regexpLimitPrice = PrmsControl
						.getRegexpBeanPriceBySourceCurrency(
								getString(R.string.prms_str_limitprice_no),
								prmsControl.getCurrencyCode(
										targetCurrencyCodeStr, tradeStyleStr),
								limitPricStr);
				lists.add(regexpLimitPrice);
				if (RegexpUtils.regexpDate(lists)) {
					limitPrice = new BigDecimal(limitPricStr.toCharArray());
					tradeNum = new BigDecimal(tradeNumStr.toCharArray());
					intent.putExtra(Prms.PRMS_LIMITPRICE, limitPricStr);
				} else {
					return;
				}
			} else if (tradeMethodStr
					.equals(ConstantGloble.PRMS_TRADEMETHOD_LOSE)) {// 止损委托
				String losePrice = StringUtil.trim(losPriceEdt.getText()
						.toString());
				// regexpLimitPrice = new RegexpBean(
				// getString(R.string.prms_str_loseprice), losePrice,
				// "price");
				regexpLimitPrice = PrmsControl
						.getRegexpBeanPriceBySourceCurrency(
								getString(R.string.prms_str_loseprice),
								prmsControl.getCurrencyCode(
										targetCurrencyCodeStr, tradeStyleStr),
								losePrice);
				lists.add(regexpLimitPrice);
				if (RegexpUtils.regexpDate(lists)) {
					losPriceBig = new BigDecimal(losePrice.toCharArray());
					tradeNum = new BigDecimal(tradeNumStr.toCharArray());
					intent.putExtra(Prms.PRMS_LOSEPRICE, losPriceBig.toString());
				} else {
					return;
				}
				intent.putExtra(Prms.PRMS_ENDDATE, entrustDateTv.getText()
						.toString());
				intent.putExtra(Prms.PRMS_ENDHOUR, endHour);
			} else if (tradeMethodStr
					.equals(ConstantGloble.PRMS_TRADEMETHOD_WIN)) {// 获利委托
				String winPrice = StringUtil.trim(winPriceEdt.getText()
						.toString());
				// regexpLimitPrice = new RegexpBean(
				// getString(R.string.prms_str_winprice), winPrice,
				// "price");
				regexpLimitPrice = PrmsControl
						.getRegexpBeanPriceBySourceCurrency(
								getString(R.string.prms_str_winprice),
								prmsControl.getCurrencyCode(
										targetCurrencyCodeStr, tradeStyleStr),
								winPrice);
				lists.add(regexpLimitPrice);
				if (RegexpUtils.regexpDate(lists)) {
					winPriceBig = new BigDecimal(winPrice.toCharArray());
					tradeNum = new BigDecimal(tradeNumStr.toCharArray());
					intent.putExtra(Prms.PRMS_WINPRICE, winPriceBig.toString());
				} else {
					return;
				}
				intent.putExtra(Prms.PRMS_ENDDATE, entrustDateTv.getText()
						.toString());
				intent.putExtra(Prms.PRMS_ENDHOUR, endHour);
			} else if (tradeMethodStr
					.equals(ConstantGloble.PRMS_TRADEMETHOD_ONEINWTO)) {// 二选一委托
				String losePrice = StringUtil.trim(losPriceEdt.getText()
						.toString());
				String winPrice = StringUtil.trim(winPriceEdt.getText()
						.toString());
				// RegexpBean regexpWinPrice = new RegexpBean(
				// getString(R.string.prms_str_winprice), winPrice,
				// "price");
				// RegexpBean regexpLosePrice = new RegexpBean(
				// getString(R.string.prms_str_loseprice), losePrice,
				// "price");
				RegexpBean regexpWinPrice = PrmsControl
						.getRegexpBeanPriceBySourceCurrency(
								getString(R.string.prms_str_winprice),
								prmsControl.getCurrencyCode(
										targetCurrencyCodeStr, tradeStyleStr),
								winPrice);
				RegexpBean regexpLosePrice = PrmsControl
						.getRegexpBeanPriceBySourceCurrency(
								getString(R.string.prms_str_loseprice),
								prmsControl.getCurrencyCode(
										targetCurrencyCodeStr, tradeStyleStr),
								losePrice);
				lists.add(regexpWinPrice);
				lists.add(regexpLosePrice);
				if (RegexpUtils.regexpDate(lists)) {
					losPriceBig = new BigDecimal(losePrice.toCharArray());
					winPriceBig = new BigDecimal(winPrice.toCharArray());
					tradeNum = new BigDecimal(tradeNumStr.toCharArray());
					intent.putExtra(Prms.PRMS_WINPRICE, winPriceBig.toString());
					intent.putExtra(Prms.PRMS_LOSEPRICE, losPriceBig.toString());
				} else {
					return;
				}
				intent.putExtra(Prms.PRMS_ENDDATE, entrustDateTv.getText()
						.toString());
				intent.putExtra(Prms.PRMS_ENDHOUR, endHour);
			}
			// 如果数据格式都正确

			// 交易数量
			intent.putExtra(Prms.PRMS_TRADENUM, tradeNum.toString());
			// 交易货币码
			intent.putExtra(Prms.PRMS_CURRENCYCODE, targetCurrencyCodeStr);
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
						.get(targetCurrencyCodeStr));
				break;
			case 2:// 获利
				tradeMethodStr = ConstantGloble.PRMS_TRADEMETHOD_WIN;
				setViewByTransMethod(tradeMethodStr);
				winUintTv.setText(LocalData.prmsTradePricUnitMaptoChi
						.get(targetCurrencyCodeStr));
				if (endDate == null) {
					BaseHttpEngine.showProgressDialogCanGoBack();
					requestSystemDateTime();
				}
				break;
			case 3:// 止损
				tradeMethodStr = ConstantGloble.PRMS_TRADEMETHOD_LOSE;
				setViewByTransMethod(tradeMethodStr);
				losUnitTv.setText(LocalData.prmsTradePricUnitMaptoChi
						.get(targetCurrencyCodeStr));
				if (endDate == null) {
					BaseHttpEngine.showProgressDialogCanGoBack();
					requestSystemDateTime();
				}
				break;
			case 4:// 二选一
				tradeMethodStr = ConstantGloble.PRMS_TRADEMETHOD_ONEINWTO;
				setViewByTransMethod(tradeMethodStr);
				winUintTv.setText(LocalData.prmsTradePricUnitMaptoChi
						.get(targetCurrencyCodeStr));
				losUnitTv.setText(LocalData.prmsTradePricUnitMaptoChi
						.get(targetCurrencyCodeStr));
				if (endDate == null) {
					BaseHttpEngine.showProgressDialogCanGoBack();
					requestSystemDateTime();
				}
				break;
			default:
				break;
			}
			break;
		case R.id.prms_trade_buy_prmsstyle:
			tradeNumEditText.requestFocus();
			cleanInputAccount();
			cleanInputPrice();
			tradeStyleStr = LocalData.prmsTradeStyleToS.get(prmsTypeList
					.get(position));
			BaseHttpEngine.showProgressDialog();
			queryPrmsPrice();

			break;
		case R.id.prms_trade_buy_currency:
			tradeNumEditText.requestFocus();
			cleanInputAccount();
			cleanInputPrice();
			Map<String, Object> map = prmsControl.getBuyCurrencyList().get(
					position);
			targetCurrencyCodeStr = String.valueOf(map
					.get(Prms.QUERY_PEMSACTBALANCE_CODE));
			prmsUint.setText(LocalData.prmsUnitMaptoChi1
					.get(targetCurrencyCodeStr));
			cashRemit = String.valueOf(map
					.get(Prms.QUERY_PEMSACTBALANCE_CASHREMIT));
			limitUintTv.setText(LocalData.prmsTradePricUnitMaptoChi
					.get(targetCurrencyCodeStr));
			winUintTv.setText(LocalData.prmsTradePricUnitMaptoChi
					.get(targetCurrencyCodeStr));
			losUnitTv.setText(LocalData.prmsTradePricUnitMaptoChi
					.get(targetCurrencyCodeStr));
			accBalanceStr = String.valueOf(map
					.get(Prms.QUERY_PEMSACTBALANCE_AVAILABLEBALANCE));
			accBalance.setText(StringUtil.parseStringPattern(accBalanceStr, 2));
			accBalanceUnitTv.setText(LocalData.Currency
					.get(targetCurrencyCodeStr));
			if (!isFirstSelect) {// 如果不是第一次选择 重新查询行情
				BaseHttpEngine.showProgressDialog();
				queryPrmsPrice();
			} else {
				isFirstSelect = false;
				String rate0 = "-";
				String rate1 = "";
				for (Map<String, String> map1 : (List<Map<String, String>>) BaseDroidApp
						.getInstanse().getBizDataMap().get(Prms.PRMS_PRICE)) {
					if (map1.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE)
							.equals(prmsControl.getCurrencyCode(
									targetCurrencyCodeStr, tradeStyleStr))
							&& map1.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE)
									.equals(targetCurrencyCodeStr)) {
						rate0 = StringUtil.valueOf1(map1
								.get(Prms.QUERY_TRADERATE_SALERATE));
					}
				}
				if (rate0.equals("") || rate0.equals("-")) {// 如果没有价格的
					buyRateTexView.setText("-");
					buyRateUnitTv.setText("");
					if (buyRateTexView.getText().toString().equals("-")) {
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								"该地区尚未开通该贵金属服务，暂不能交易！");
						return;
					}
				} else {
					rate1 = LocalData.Currency.get(targetCurrencyCodeStr)
							+ LocalData.prmsTradePricUnitMaptoChi
									.get(targetCurrencyCodeStr);

					buyRateTexView.setText(rate0);
					buyRateUnitTv.setText(rate1);
//					setMaxBuyNum(accBalanceStr, rate0, LocalData.prmsUnitMaptoChi1.get(targetCurrencyCodeStr));
					setMaxBuyNum(accBalanceStr, rate0, targetCurrencyCodeStr);
				}
			}

			break;

		default:
			break;
		}

	}
	
	/**
	 * 设置最大买入数量
	 * @param accBalanceStr  可用余额
	 * @param rate0 当前买入价
	 */
	private void setMaxBuyNum(String accBalanceStr, String rate0, String unitCode){
		TextView maxBuyNum = (TextView)findViewById(R.id.prms_trade_max_buy_num);
		TextView maxBuyNumUnit = (TextView)findViewById(R.id.prms_trade_max_buy_num_unit);
		if(StringUtil.isNull(accBalanceStr) || StringUtil.isNull(rate0)){
			maxBuyNum.setText(ConstantGloble.BOCINVT_DATE_ADD);
			return;
		}
		try{
			double accBanlace = Double.parseDouble(accBalanceStr);
			double rate = Double.parseDouble(rate0);
			double maxNum = accBanlace / rate;
//			unitCode.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBG) || 
//			unitCode.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBS) ||
//			unitCode.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBBAG) || 
//			unitCode.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBBOG) ||
			if(LocalData.rmbCodeList.contains(unitCode)){
				maxBuyNum.setText(StringUtil.parseStringPattern3(maxNum + "", 0));
			}else{
				maxBuyNum.setText(StringUtil.parseStringPattern3(maxNum + "", 1));
			}
			maxBuyNumUnit.setText(StringUtil.valueOf1(LocalData.prmsUnitMaptoChi1.get(unitCode)));
		}catch(Exception e){
			LogGloble.e(TAG, e.toString());
			maxBuyNum.setText(ConstantGloble.BOCINVT_DATE_ADD);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	/**
	 * 
	 * @param transMethod
	 */
	private void setViewByTransMethod(String transMethod) {
		switch (PrmsControl.tradeMethodSwitch(transMethod)) {
		case PrmsControl.PRMS_TRADEMETHOD_LIMIT:
			losPriceView.setVisibility(View.GONE);
			winPriceView.setVisibility(View.GONE);
			limitPriceLayout.setVisibility(View.VISIBLE);
			entrustDateView.setVisibility(View.GONE);
			prms_run_lose_ll.setVisibility(View.GONE);
			turnOver.setVisibility(View.GONE);
			next.setVisibility(View.VISIBLE);
			break;
		case PrmsControl.PRMS_TRADEMETHOD_LOSE:
			losPriceView.setVisibility(View.VISIBLE);
			winPriceView.setVisibility(View.GONE);
			limitPriceLayout.setVisibility(View.GONE);
			entrustDateView.setVisibility(View.VISIBLE);
			prms_run_lose_ll.setVisibility(View.GONE);
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
			entrustDateView.setVisibility(View.VISIBLE);
			prms_run_lose_ll.setVisibility(View.GONE);
			turnOver.setVisibility(View.GONE);
			next.setVisibility(View.VISIBLE);
			break;
		case PrmsControl.PRMS_TRADEMETHOD_NOW:
			losPriceView.setVisibility(View.GONE);
			winPriceView.setVisibility(View.GONE);
			limitPriceLayout.setVisibility(View.GONE);
			entrustDateView.setVisibility(View.GONE);
			prms_run_lose_ll.setVisibility(View.GONE);
			turnOver.setVisibility(View.VISIBLE);
			next.setVisibility(View.GONE);
			break;
		}
	}

	private int getSelection() {
		if (StringUtil.isNullOrEmpty(targetCurrencyCodeStr)) {
			return 0;
		}
		int i = 0;
		for (Map<String, Object> map : prmsControl.getBuyCurrencyList()) {
			if (targetCurrencyCodeStr.equals(map
					.get(Prms.QUERY_PEMSACTBALANCE_CODE))) {
				return i;
			}
			i++;
		}
		return 0;
	}

	// /**
	// * 币种列表的默认选中
	// *
	// * @param cashremit
	// * @param list
	// * @return
	// */
	private int getCurrencyAdapterSelection(String targetCurrencyCode,
			List<String> list) {
		if (list.size() == 0 || targetCurrencyCode == null) {
			return 0;
		}
		for (int i = 0; i < list.size(); i++) {
			String showStr = list.get(i);
			if (showStr.contains(LocalData.Currency.get(targetCurrencyCode))) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * 传入 是黄金还是白银 还是G S 获取 显示列表 币种
	 * 
	 * @return
	 */
	private List<String> getCurrencyShowList() {
		List<String> list = new ArrayList<String>();
		for (Map<String, Object> map : prmsControl.getBuyCurrencyList()) {
			String currencCode = String.valueOf(map
					.get(Prms.QUERY_PEMSACTBALANCE_CODE));
			String cashRemit = String.valueOf(map
					.get(Prms.QUERY_PEMSACTBALANCE_CASHREMIT));
			StringBuffer sb = new StringBuffer();
			sb.append(LocalData.Currency.get(currencCode));
			// sb.append(LocalData.prmsCurrencyMap.get(currencCode));
			// sb.append(LocalData.prmsBuyStyleMaptoChi.get(PrmsType));
			if (!currencCode.equals(ConstantGloble.PRMS_CODE_RMB)) {// 如果不是人民币
				sb.append(" ");
				sb.append(LocalData.CurrencyCashremit.get(cashRemit));
			}
			list.add(sb.toString());
		}
		return list;
	}

	private List<String> getTradeTypeDateList() {
		List<Map<String, String>> rateSourceList = (List<Map<String, String>>) BaseDroidApp
				.getInstanse().getBizDataMap().get(Prms.PRMS_PRICE);
		// 筛选过后的 List
		List<Map<String, String>> rateSourceListNew = prmsControl.getDataList(
				rateSourceList, this);
		// 获取源币种
		// prmsControl.getSourceCurrencyCode(rateSourceListNew);

		List<String> resultList = new ArrayList<String>();

		for (String sourceCurrencyCode : prmsControl
				.getSourceCurrencyCode(rateSourceListNew)) {
			if (!resultList.contains(LocalData.prmsTradeStyleGSMapShow
					.get(sourceCurrencyCode))) {
				resultList.add(LocalData.prmsTradeStyleGSMapShow
						.get(sourceCurrencyCode));
			}
		}
		return resultList;
	}

	/**
	 * 用sourceCurrencyCode
	 * 
	 * @param sourceCurrencyCode
	 * @return
	 */
	private int getTypePosition(String sourceCurrencyCode) {
		if (prmsTypeList == null) {
			return 0;
		} else {
			// 根据 返回黄金 白银 钯金 铂金 判断
			for (int i = 0; i < prmsTypeList.size(); i++) {
				String prmsType = prmsTypeList.get(i);
				if (prmsType.equals(LocalData.prmsTradeStyleGSMapShow
						.get(sourceCurrencyCode))) {
					return i;
				}
			}
			return 0;
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
}
