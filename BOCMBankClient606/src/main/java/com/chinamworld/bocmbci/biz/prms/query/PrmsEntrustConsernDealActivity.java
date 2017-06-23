package com.chinamworld.bocmbci.biz.prms.query;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.prms.PrmsBaseActivity;
import com.chinamworld.bocmbci.biz.prms.control.PrmsControl;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 贵金属委托撤单页面
 * 
 * @author xyl
 * 
 */
public class PrmsEntrustConsernDealActivity extends PrmsBaseActivity {
	private static final String TAG = "PrmsEntrustConsernDealActivity";
	/**
	 * 交易币种
	 */
	private TextView buyCurrencyTextView;
	private TextView saleCurrencyTextView;
	/**
	 * 委托序号
	 */
	private TextView enturstIdTextView;
	/**
	 * 买入数量
	 */
	private TextView tradeBuyNumTextView;
	private TextView tradeBuyNumUnitTv;
	/**
	 * 卖出数量
	 */
	private TextView tradeSaleNumTextView;
	private TextView tradeSaleNumUnitTv;
	/**
	 * 成交方式
	 */
	private TextView tradeMethodTextView;
	private TextView winPriceTv, losPriceTv, winPriceUnitTv, losPriceUnitTv;
	/**
	 * 撤销按钮
	 */
	private Button okBtn;

	private TextView entrustStateTv, entrustDateTv, entrustEndDateTv;
	/** 委托状态 委托序号 止损价格,获利价格 */
	private String entrustState, consignNumber, losPrice, winPrice;
	private String cashRemit;
	private String buyNum;
	private String saleNum;
	private String exchangeTranType;
	private String buyCurrency, saleCurrency;
	private String tokenId;
	private String transactionId;// 交易流水号
	private View prms_runlose_price_ll;
	private TextView prms_runlose_price_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		settingbaseinit();
		initView();
		initDate();
		initListtenner();
	}

	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	private void initView() {
		View child = mainInflater.inflate(
				R.layout.prms_query_entrust_consern_deal_main, null);
		tabcontent.addView(child);
		// 委托序号
		enturstIdTextView = (TextView) findViewById(R.id.prms_query_deal_detailes_entrusetid);
		// 交易币种
		buyCurrencyTextView = (TextView) findViewById(R.id.prms_query_deal_detailes_buycurrency);
		saleCurrencyTextView = (TextView) findViewById(R.id.prms_query_deal_detailes_salecurrency);
		// 买入数量
		tradeBuyNumTextView = (TextView) findViewById(R.id.prms_query_deal_detailes_tradebuynum);
		tradeBuyNumUnitTv = (TextView) findViewById(R.id.prms_query_deal_detailes_tradebuynum_unit);
		tradeSaleNumTextView = (TextView) findViewById(R.id.prms_query_deal_detailes_tradesalenum);
		tradeSaleNumUnitTv = (TextView) findViewById(R.id.prms_query_deal_detailes_tradesalenum_unit);
		tradeMethodTextView = (TextView) findViewById(R.id.prms_entrust_type_tv);
		entrustStateTv = (TextView) findViewById(R.id.prms_entrust_state_tv);
		entrustDateTv = (TextView) findViewById(R.id.prms_entrust_date_tv);
		entrustEndDateTv = (TextView) findViewById(R.id.prms_entrust_enddate_tv);

		winPriceTv = (TextView) findViewById(R.id.prms_trade_confirm_winprice);
		losPriceTv = (TextView) findViewById(R.id.prms_trade_confirm_losprice);
		winPriceUnitTv = (TextView) findViewById(R.id.prms_trade_confirm_winprice_unit);
		losPriceUnitTv = (TextView) findViewById(R.id.prms_trade_confirm_losprice_unit);
		
		prms_runlose_price_ll = findViewById(R.id.prms_runlose_price_ll);
		prms_runlose_price_tv = (TextView) findViewById(R.id.prms_runlose_price_tv);
		prms_runlose_price_ll.setVisibility(View.GONE);
		
		// 确定按钮
		okBtn = (Button) findViewById(R.id.prms_query_deal_detailes_ok);
		String title = getResources().getString(
				R.string.prms_title_consern_entrust);
		setTitle(title);
		initRightBtnForMain();
		back.setVisibility(View.GONE);
		right.setText(getString(R.string.switch_off));
	}

	private void initListtenner() {
		okBtn.setOnClickListener(this);
		right.setOnClickListener(this);
	}

	/**
	 * 初始化数据
	 * 
	 * @Author xyl
	 */
	private void initDate() {
		Intent intent = getIntent();
		int position = intent.getIntExtra(Prms.I_POSITION, 0);
		Map<String, Object> map = prmsControl.queryEntrustNowList.get(position);
		// 委托序号
		consignNumber = (String) map.get(Prms.PRMS_QUERY_DEAL_CONSIGNNUMBER);
		// 买入币种
		final Map<String, String> firstBuyCurrency = (Map<String, String>) map
				.get(Prms.PRMS_QUERY_DEAL_FIRSTBUYCURRENCY);
		// 卖出币种
		final Map<String, String> firstSaleCurrency = (Map<String, String>) map
				.get(Prms.PRMS_QUERY_DEAL_FIRSTSELLCURRENCY);

		// 钞汇标志
		cashRemit = (String) map.get(Prms.PRMS_QUERY_DEAL_CASHREMIT);
		// 买入数量
		// +LocalData.prmsUnitMaptoChi.get(map.get(Prms.PRMS_QUERY_DEAL_FIRSTBUYCURRENCY));
		buyNum = (String) map.get(Prms.PRMS_QUERY_DEAL_FIRSTBUYAMOUNT);// TODO
																		// 接口不确定
		saleNum = (String) map.get(Prms.PRMS_QUERY_DEAL_FIRSTSELLAMOUNT);// TODO
																			// 接口不确定
		// 成交类型
		exchangeTranType = (String) map
				.get(Prms.PRMS_QUERY_DEAL_EXCHANGETRANTYPE);

		buyCurrency = firstBuyCurrency.get(Prms.PRMS_QUERY_DEAL_CODE);
		saleCurrency = firstSaleCurrency.get(Prms.PRMS_QUERY_DEAL_CODE);

		final String endEntrustDate = (String) map
				.get(Prms.PRMS_QUERY_DEAL_DUEDATE);// 委托截止时间
		final String consignDate = (String) map
				.get(Prms.PRMS_QUERY_DEAL_CONSIGNDATE);// 委托时间
		// 委托序号
		enturstIdTextView.setText(consignNumber);
		buyCurrencyTextView.setText(LocalData.prmsTradeStyleMaptoChi
				.get(buyCurrency));
		if (!LocalData.cashMapValue.get(cashRemit).equals("-")) {
			saleCurrencyTextView.setText(LocalData.prmsTradeStyleMaptoChi
					.get(saleCurrency) + LocalData.cashMapValue.get(cashRemit));
		} else {
			saleCurrencyTextView.setText(LocalData.prmsTradeStyleMaptoChi
					.get(saleCurrency));
		}
		// 卖出 买入币种为人民币或美元 卖出币种为美元金
		if (buyCurrency.equals(ConstantGloble.PRMS_CODE_RMB)
				|| buyCurrency.equals(ConstantGloble.PRMS_CODE_DOLOR)) {
			tradeBuyNumTextView.setText("-");
			tradeBuyNumUnitTv.setText("");
			tradeBuyNumTextView.setTextColor(getResources().getColor(
					R.color.black));
			tradeSaleNumTextView.setText(PrmsControl
					.parseStringPatternTradeNum(saleCurrency, saleNum));
			tradeSaleNumUnitTv.setText(LocalData.prmsUnitMaptoChi1.get(saleCurrency));
		} else {//买入  买入币种为美元金  卖出币种为人民币
			tradeBuyNumTextView.setText(PrmsControl.parseStringPatternTradeNum(
					buyCurrency, buyNum));
			tradeBuyNumUnitTv.setText(LocalData.prmsUnitMaptoChi1.get(buyCurrency));
			tradeSaleNumTextView.setText("-");
			tradeSaleNumUnitTv.setText("");
			tradeSaleNumTextView.setTextColor(getResources().getColor(
					R.color.black));
		}
		tradeMethodTextView.setText(LocalData.prmsTradeMethodMaptoChi
				.get(exchangeTranType));

		if (exchangeTranType.equals(Prms.C_TRADEMETHOD_CHOINTWO)
				&& map.get(Prms.PRMS_QUERY_DEAL_FIRSTSTATUS).equals(
						Prms.C_ENTRUSTSTATE_OTHER)) {
			entrustState = (String) map.get(Prms.PRMS_QUERY_DEAL_SECONDSTATUS);
		} else {
			entrustState = (String) map.get(Prms.PRMS_QUERY_DEAL_FIRSTSTATUS);
		}
		
		/**
		 * p603
		 */
		if(exchangeTranType.equals(Prms.C_TRADEMETHOD_RUNLOSENTRUST)){
			prms_runlose_price_ll.setVisibility(View.VISIBLE);
			String dia = (String) map.get(Prms.PRMS_QUERY_DEAL_FIRSTCUSTOMERRATE);
			if(dia.contains(".")){
				dia = dia.substring(0, dia.indexOf("."));
			}
			prms_runlose_price_tv.setText(dia);
		}
		/*
		 * 当委托类型是“获利委托”，获利价格为“第一客户汇率firstCustomerRate”
		 * 当委托类型是“止损委托”，止损价格为“第二客户汇率secondCustomerRate”
		 * 当委托类型是“二选一委托”，需要先判断firstType和secondType
		 * ，如果firstType=P，则获利价格是“第一客户汇率firstCustomerRate
		 * ”，止损价格为“第二客户汇率secondCustomerRate
		 * ”；如果firstType=S，则获利价格是“第二客户汇率secondCustomerRate
		 * ”，止损价格为“第一客户汇率firstCustomerRate”。
		 */
		if (exchangeTranType.equals(Prms.C_TRADEMETHOD_LOSENTRUST)) {// 止损委托
//			losPrice = (String) map
//					.get(Prms.PRMS_QUERY_DEAL_FIRSTCUSTOMERRATE);
			//:文档上写的是secend实际在first
			losPrice = (String) map
					.get(Prms.PRMS_QUERY_DEAL_FIRSTCUSTOMERRATE);
			winPrice = null;
		} else if (exchangeTranType.equals(Prms.C_TRADEMETHOD_WINENTRUST)) {// 获利委托
			winPrice = (String) map.get(Prms.PRMS_QUERY_DEAL_FIRSTCUSTOMERRATE);
			losPrice = null;
		} else {// 二选一
			if (map.get(Prms.PRMS_QUERY_DEAL_FIRSTTYPE).equals(
					Prms.C_ENTRUSTPRICE_TYPE)) {
				winPrice = (String) map
						.get(Prms.PRMS_QUERY_DEAL_FIRSTCUSTOMERRATE);
				losPrice = (String) map
						.get(Prms.PRMS_QUERY_DEAL_SECONDCUSTOMERRATE);
			} else {
				winPrice = (String) map
						.get(Prms.PRMS_QUERY_DEAL_SECONDCUSTOMERRATE);
				losPrice = (String) map
						.get(Prms.PRMS_QUERY_DEAL_FIRSTCUSTOMERRATE);
			}
		}
		losPriceUnitTv.setText(LocalData.prmsTradePricUnitMaptoChi.get(saleCurrency));
		winPriceUnitTv.setText(LocalData.prmsTradePricUnitMaptoChi.get(saleCurrency));
		winPriceTv.setText(StringUtil.valueOf1(winPrice));
		losPriceTv.setText(StringUtil.valueOf1(losPrice));
		PrmsControl.setUtilTvShow(winPriceTv, winPriceUnitTv);
		PrmsControl.setUtilTvShow(losPriceTv, losPriceUnitTv);
		entrustStateTv.setText(LocalData.entrustSatetMap.get(entrustState));
		entrustDateTv.setText(StringUtil.valueOf1(consignDate));
		entrustEndDateTv.setText(StringUtil.valueOf1(endEntrustDate));
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.prms_query_deal_detailes_ok:// 确定按钮
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
			break;

		case R.id.ib_top_right_btn:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId();
	}

	@Override
	public void requestPSNGetTokenIdCallback(Object resultObj) {
		super.requestPSNGetTokenIdCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		tokenId = (String) biiResponseBody.getResult();
		prmsConserDeal(tokenId, consignNumber, saleCurrency, buyCurrency,
				cashRemit, exchangeTranType, saleNum, buyNum, winPrice,
				losPrice);
	}

	@Override
	public void prmsConserDealCallBack(Object resultObj) {
		super.prmsConserDealCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> resultMap = (Map<String, String>) biiResponseBody
				.getResult();
		transactionId = resultMap.get(Prms.PRMS_TRANSACTIONID);
		Intent intent = getIntent();
		intent.putExtra(Prms.PRMS_TRANSACTIONID, transactionId);
		intent.setClass(this, PrmsEntrustConsernDealSuccessActivity.class);
		startActivityForResult(intent, 1);
	}
}
