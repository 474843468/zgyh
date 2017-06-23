package com.chinamworld.bocmbci.biz.prms.query;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
 * 贵金属交易历史查询详情页面
 * 
 * @author xyl
 * 
 */
public class PrmsQueryDealDetailsActivity extends PrmsBaseActivity {
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
	 * 钞汇标志
	 */
	private TextView cashRemitTextView;
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
	 * 成交价格
	 */
	private TextView tradePriceTextView;
	private TextView tradePriceUnitTv;
	/**
	 * 交易时间 精确到分
	 */
	private TextView tradeTimeTextView;
	/**
	 * 成交方式
	 */
	private TextView tradeMethodTextView;
	/**
	 * 确定按钮
	 */
	private Button okBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initDate();
		// 确定按钮
		okBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PrmsQueryDealDetailsActivity.this.onBackPressed();
			}
		});

	}

	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	private void init() {
		View child = mainInflater.inflate(R.layout.prms_query_deal_details,
				null);
		tabcontent.addView(child);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				((TextView) findViewById(R.id.salecurrency_alert)));
		// // 委托序号
		// enturstIdTextView = (TextView)
		// findViewById(R.id.prms_query_deal_detailes_entrusetid);
		// 交易账户
		// tradeAccNumTextView = (TextView)
		// findViewById(R.id.prms_query_deal_detailes_tradeacc);
		// 交易币种
		buyCurrencyTextView = (TextView) findViewById(R.id.prms_query_deal_detailes_buycurrency);
		saleCurrencyTextView = (TextView) findViewById(R.id.prms_query_deal_detailes_salecurrency);
		// 钞汇标志
		cashRemitTextView = (TextView) findViewById(R.id.prms_query_deal_detailes_cashremit);
		// 买入数量
		tradeBuyNumTextView = (TextView) findViewById(R.id.prms_query_deal_detailes_tradebuynum);
		tradeBuyNumUnitTv = (TextView) findViewById(R.id.prms_query_deal_detailes_tradebuynum_unit);
		tradeSaleNumTextView = (TextView) findViewById(R.id.prms_query_deal_detailes_tradesalenum);
		tradeSaleNumUnitTv = (TextView) findViewById(R.id.prms_query_deal_detailes_tradesalenum_unit);
		// 成交价格
		tradePriceTextView = (TextView) findViewById(R.id.prms_query_deal_detailes_tradeprice);
		tradePriceUnitTv = (TextView) findViewById(R.id.prms_query_deal_detailes_tradeprice_unit);
		// 交易时间 精确到分
		tradeTimeTextView = (TextView) findViewById(R.id.prms_query_deal_detailes_tradetime);
		// 成交方式
		tradeMethodTextView = (TextView) findViewById(R.id.prms_query_deal_detailes_trademethod);
		// 确定按钮
		okBtn = (Button) findViewById(R.id.prms_query_deal_detailes_ok);
		String title = getResources().getString(R.string.prms_title_query_deal_new);
		setTitle(title);
		initRightBtnForMain();
		
	}

	/**
	 * 初始化数据
	 * 
	 * @Author xyl
	 */
	private void initDate() {
		Intent intent = getIntent();
		Bundle extra = intent.getExtras();
		// 委托序号
		// enturstIdTextView.setText(extra.getString(Prms.PRMS_CONSIGNNUMBER));
		// tradeAccNumTextView.setText(StringUtil.getForSixForString(extra.getString(Prms.PRMS_ACCOUNTNUM)));
		buyCurrencyTextView.setText(LocalData.prmsTradeStyleMaptoChi.get(extra
				.getString(Prms.PRMS_FIRSTBUYCURRENCY)));
		if(!LocalData.cashMapValue.get(extra
				.getString(Prms.PRMS_CASHREMIT)).equals("-")){
			saleCurrencyTextView.setText(LocalData.prmsTradeStyleMaptoChi.get(extra
					.getString(Prms.PRMS_FIRSTSALECURRENCY))+LocalData.cashMapValue.get(extra
							.getString(Prms.PRMS_CASHREMIT)));
		}else{
			saleCurrencyTextView.setText(LocalData.prmsTradeStyleMaptoChi.get(extra
					.getString(Prms.PRMS_FIRSTSALECURRENCY)));
		}
		//买入  如果买入币种 是人民币或者美元 是买入
		if (extra.getString(Prms.PRMS_FIRSTBUYCURRENCY).equals(
				ConstantGloble.PRMS_CODE_RMB)
				|| extra.getString(Prms.PRMS_FIRSTBUYCURRENCY).equals(
						ConstantGloble.PRMS_CODE_DOLOR)) {
			tradeBuyNumTextView.setText("-");
			tradeBuyNumUnitTv.setText("");
			tradeBuyNumTextView.setTextColor(getResources().getColor(R.color.black));
		} else {
			tradeBuyNumTextView.setText(StringUtil.valueOf1(extra.getString(Prms.PRMS_BUYNUM)));
			tradeBuyNumUnitTv.setText(LocalData.prmsUnitMaptoChi1.get(extra
					.getString(Prms.PRMS_FIRSTBUYCURRENCY)));
			calculateTradeAmount(true);
		}
		//卖出   
		if (extra.getString(Prms.PRMS_FIRSTSALECURRENCY).equals(
				ConstantGloble.PRMS_CODE_RMB)
				|| extra.getString(Prms.PRMS_FIRSTSALECURRENCY).equals(
						ConstantGloble.PRMS_CODE_DOLOR)) {
			tradeSaleNumTextView.setText("-");
			tradeSaleNumUnitTv.setText("");
			tradeSaleNumTextView.setTextColor(getResources().getColor(R.color.black));
		}else{
			tradeSaleNumTextView.setText(StringUtil.valueOf1(extra.getString(Prms.PRMS_SALENUM)));
			tradeSaleNumUnitTv.setText(LocalData.prmsUnitMaptoChi1.get(extra
					.getString(Prms.PRMS_FIRSTSALECURRENCY)));
			calculateTradeAmount(false);
		}
		
		
		tradePriceTextView.setText(extra.getString(Prms.PRMS_TRADEPRICE));
		tradePriceUnitTv.setText(LocalData.prmsTradePricUnitMaptoChi.get(extra
				.getString(Prms.PRMS_FIRSTBUYCURRENCY)));
		tradeTimeTextView.setText(extra.getString(Prms.PRMS_REALTRANSTIME));
		tradeMethodTextView.setText(LocalData.prmsTradeMethodMaptoChi.get(extra
				.getString(Prms.PRMS_EXCHANGETRANTYPE)));

	}
	
	/**
	 * 交易金额  并显示
	 * @param num 交易数量
	 * @param tradePrice 成交价格
	 * @param unit 单位
	 */
	public void calculateTradeAmount(boolean isBuy) {
		Map<String, Object> map = PrmsControl.getInstance().queryDealMap;
		findViewById(R.id.tradeAmountLl).setVisibility(View.VISIBLE);
		TextView tradeAmount = (TextView) findViewById(R.id.tradeAmount);
		if (map.get(Prms.PRMS_QUERY_DEAL_FIRSTSTATUS) != null
				&& map.get(Prms.PRMS_QUERY_DEAL_FIRSTSTATUS).equals(
						Prms.C_ENTRUSTPRICE_TYPE_S)) {
			if (isBuy) {
				String ta = (String) map
						.get(Prms.PRMS_QUERY_DEAL_FIRSTSELLAMOUNT);
				tradeAmount.setText(StringUtil.parseStringPattern(ta, 2));
			} else {
				String ta = (String) map
						.get(Prms.PRMS_QUERY_DEAL_FIRSTBUYAMOUNT);
				tradeAmount.setText(StringUtil.parseStringPattern(ta, 2));
			}
		} else {
			if (isBuy) {
				String ta = (String) map
						.get(Prms.PRMS_QUERY_DEAL_SECONDSELLAMOUNT);
				tradeAmount.setText(StringUtil.parseStringPattern(ta, 2));
			} else {
				String ta = (String) map
						.get(Prms.PRMS_QUERY_DEAL_SECONDBUYAMOUNT);
				tradeAmount.setText(StringUtil.parseStringPattern(ta, 2));
			}
		}

	}

}
