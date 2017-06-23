package com.chinamworld.bocmbci.biz.prms.query;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.prms.PrmsBaseActivity;
import com.chinamworld.bocmbci.biz.prms.control.PrmsControl;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 贵金属交易历史查询详情页面(当前有效委托)
 * 
 * @author xyl
 * 
 */
public class PrmsQueryEntrustNowDetailsActivity extends PrmsBaseActivity {
	private static final String TAG = "PrmsQueryDealDetailsActivity";
	/**
	 * 委托序号
	 */
	// private TextView enturstIdTextView;
	/**
	 * 交易账户
	 */
	// private TextView tradeAccNumTextView;
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
	// /**
	// * 成交价格
	// */
	// private TextView tradePriceTextView;
	// private TextView tradePriceUnitTv;
	// /**
	// * 交易时间 精确到分
	// */
	// private TextView tradeTimeTextView;
	/**
	 * 成交方式
	 */
	private TextView tradeMethodTextView;
	/**
	 * 撤销按钮
	 */
	private Button okBtn;

	private View winView, losView,prms_runlose_price_ll;
	private TextView winPriceTv, winPriceUnitTv, losPriceTv, losPriceUnitTv,
			entrustStateTv, entrustDateTv, entrustEndDateTv,prms_runlose_price_tv;
	/** 委托状态 委托序号 止损价格,获利价格 */
	private String entrustState, consignNumber, losPrice, winPrice;
	private String cashRemit;
	private String buyNum;
	private String saleNum;
	private String exchangeTranType;
	private String buyCurrency, saleCurrency;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
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
				R.layout.prms_query_entrust_now_details, null);
		tabcontent.addView(child);
		// // 委托序号
		enturstIdTextView = (TextView) findViewById(R.id.prms_query_deal_detailes_entrusetid);
		// 交易账户
		// tradeAccNumTextView = (TextView)
		// findViewById(R.id.prms_query_deal_detailes_tradeacc);
		// 交易币种
		buyCurrencyTextView = (TextView) findViewById(R.id.prms_query_deal_detailes_buycurrency);
		saleCurrencyTextView = (TextView) findViewById(R.id.prms_query_deal_detailes_salecurrency);
		// // 钞汇标志
		// cashRemitTextView = (TextView)
		// findViewById(R.id.prms_query_deal_detailes_cashremit);
		// 买入数量
		tradeBuyNumTextView = (TextView) findViewById(R.id.prms_query_deal_detailes_tradebuynum);
		tradeBuyNumUnitTv = (TextView) findViewById(R.id.prms_query_deal_detailes_tradebuynum_unit);
		tradeSaleNumTextView = (TextView) findViewById(R.id.prms_query_deal_detailes_tradesalenum);
		tradeSaleNumUnitTv = (TextView) findViewById(R.id.prms_query_deal_detailes_tradesalenum_unit);

		tradeMethodTextView = (TextView) findViewById(R.id.prms_entrust_type_tv);
		winView = findViewById(R.id.prms_win_price_ll);
		losView = findViewById(R.id.prms_lose_price_ll);

		winPriceTv = (TextView) findViewById(R.id.prms_win_price_tv);
		winPriceUnitTv = (TextView) findViewById(R.id.prms_win_price_tv_unit);
		losPriceTv = (TextView) findViewById(R.id.prms_lose_price_tv);
		losPriceUnitTv = (TextView) findViewById(R.id.prms_lose_price_tv_unit);

		entrustStateTv = (TextView) findViewById(R.id.prms_entrust_state_tv);
		entrustDateTv = (TextView) findViewById(R.id.prms_entrust_date_tv);
		entrustEndDateTv = (TextView) findViewById(R.id.prms_entrust_enddate_tv);
		
		
		prms_runlose_price_ll = findViewById(R.id.prms_runlose_price_ll);
		prms_runlose_price_tv = (TextView) findViewById(R.id.prms_runlose_price_tv);
		prms_runlose_price_ll.setVisibility(View.GONE);
		//add by fsm 解决卖出币种/钞汇文字显示不全的问题
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, 
				(TextView)findViewById(R.id.prms_query_deal_detailes_salecurrency_alert));
		//add by fsm 解决委托到期文字显示不全的问题
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, 
				(TextView)findViewById(R.id.prms_entrust_enddate_tv_alert));

		// 确定按钮
		okBtn = (Button) findViewById(R.id.prms_query_deal_detailes_ok);
		String title = getIntent().getStringExtra(Prms.I_ENTRUSTQUERY_TITLE);
		setTitle(title);
		initRightBtnForMain();

	}

	// private void setVisByTradeType(String tradeType)
	// {
	// if(tradeType.equals(LocalData.prmsTradeMethodCodeList.get(2))){//获利委托
	// winView.setVisibility(View.VISIBLE);
	// losView.setVisibility(View.GONE);
	// }else if(tradeType.equals(LocalData.prmsTradeMethodCodeList.get(3))){//止损
	// winView.setVisibility(View.GONE);
	// losView.setVisibility(View.VISIBLE);
	// }else
	// if(tradeType.equals(LocalData.prmsTradeMethodCodeList.get(4))){//二选一
	// losView.setVisibility(View.VISIBLE);
	// winView.setVisibility(View.VISIBLE);
	// }else{
	// losView.setVisibility(View.GONE);
	// winView.setVisibility(View.GONE);
	// }
	// }

	private void initListtenner() {
		okBtn.setOnClickListener(this);
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
		buyCurrencyTextView.setText(LocalData.Currency
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
			tradeSaleNumTextView.setText(StringUtil.valueOf1(saleNum));
			tradeSaleNumUnitTv.setText(LocalData.prmsUnitMaptoChi1
					.get(saleCurrency));
		} else {// 买入 买入币种为美元金 卖出币种为人民币
			tradeBuyNumTextView.setText(StringUtil.valueOf1(buyNum));
			tradeBuyNumUnitTv.setText(LocalData.prmsUnitMaptoChi1
					.get(buyCurrency));
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
		//P603
		if(exchangeTranType.equals(Prms.C_TRADEMETHOD_RUNLOSENTRUST)){
			prms_runlose_price_ll.setVisibility(View.VISIBLE);
			String dia =(String) map.get(Prms.PRMS_QUERY_DEAL_FIRSTCUSTOMERRATE);
			if(dia.contains(".")){
				dia = dia.substring(0, dia.indexOf("."));
			}
			prms_runlose_price_tv.setText(dia);
		}
		/*
		 * 当委托类型是“获利委托”，获利价格为“第一客户汇率firstCustomerRate” // *
		 * 当委托类型是“止损委托”，止损价格为“第二客户汇率secondCustomerRate” // 文档错误
		 * 当委托类型是“止损委托”，止损价格为“第二客户汇率firstCustomerRate”
		 * 当委托类型是“二选一委托”，需要先判断firstType和secondType
		 * ，如果firstType=P，则获利价格是“第一客户汇率firstCustomerRate
		 * ”，止损价格为“第二客户汇率secondCustomerRate
		 * ”；如果firstType=S，则获利价格是“第二客户汇率secondCustomerRate
		 * ”，止损价格为“第一客户汇率firstCustomerRate”。
		 */

		if (exchangeTranType.equals(Prms.C_TRADEMETHOD_LOSENTRUST)) {// 止损委托
			losPrice = (String) map.get(Prms.PRMS_QUERY_DEAL_FIRSTCUSTOMERRATE);
			// .get(Prms.PRMS_QUERY_DEAL_SECONDCUSTOMERRATE);//文档错误
			winPrice = null;
		} else if (exchangeTranType.equals(Prms.C_TRADEMETHOD_WINENTRUST)) {// 获利委托
			winPrice = (String) map.get(Prms.PRMS_QUERY_DEAL_FIRSTCUSTOMERRATE);
			losPrice = null;
		}
		else {// 二选一
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
		losPriceUnitTv.setText(LocalData.prmsTradePricUnitMaptoChi
				.get(saleCurrency));
		winPriceUnitTv.setText(LocalData.prmsTradePricUnitMaptoChi
				.get(saleCurrency));
		winPriceTv.setText(StringUtil.valueOf1(winPrice));
		losPriceTv.setText(StringUtil.valueOf1(losPrice));
		PrmsControl.setUtilTvShow(winPriceTv, winPriceUnitTv);
		PrmsControl.setUtilTvShow(losPriceTv, losPriceUnitTv);
		entrustStateTv.setText(LocalData.entrustSatetMap.get(entrustState));
		entrustDateTv.setText(StringUtil.valueOf1(consignDate));
		entrustEndDateTv.setText(StringUtil.valueOf1(endEntrustDate));
		if (entrustState.equals(Prms.C_ENTRUSTSTATE_NOTDEAL)) {// 显示撤单按钮
			okBtn.setVisibility(View.VISIBLE);
		} else {
			okBtn.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.prms_query_deal_detailes_ok:// 撤销按钮
			Intent intent = getIntent();
			intent.setClass(this, PrmsEntrustConsernDealActivity.class);
			startActivityForResult(intent, 1);
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
}
