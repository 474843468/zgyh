package com.chinamworld.bocmbci.biz.prms.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.prms.trade.PrmsTradeSaleActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 我的账户贵金属中的列表
 * 
 * @author xyl
 * 
 */
public class PrmsAccBalanceAdapter401 extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> list;

	public PrmsAccBalanceAdapter401(Context context,
			List<Map<String, Object>> list) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.list = getSortDateList(list);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	public void notifyDataSetChanged(List<Map<String, Object>> list) {
		this.list = getSortDateList(list);
		notifyDataSetChanged();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.prms_acc_balance_iterm,
					null);
			holder = new ViewHolder();
			holder.currencyTextView = (TextView) convertView
					.findViewById(R.id.prms_price_grideview_sourcecurrency);
			holder.prmsBalanceTextView = (TextView) convertView
					.findViewById(R.id.prms_price_grideview_accbalance);
			holder.prmsBalanceUnitTextView = (TextView) convertView
					.findViewById(R.id.prms_price_grideview_accbalance_unit);
			holder.saleBtnView = convertView
					.findViewById(R.id.prms_price_grideview_sale);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		LayoutParams lp;
		Map<String, Object> map = list.get(position);
		int dataSize = position + 1;
		if (getCount() == 1) {
			convertView.setBackgroundResource(R.drawable.prms_img_card_only);
			lp = getButtomLayoutParams();
		} else {
			if (dataSize == 1) {// 第一个
				convertView.setBackgroundResource(R.drawable.prms_img_card_top);
				lp = getTopLayoutParams();
			} else if (dataSize == getCount()) {// 最后一个
				convertView
						.setBackgroundResource(R.drawable.prms_img_card_buttom);
				lp = getButtomLayoutParams();
			} else {// 中间
				convertView.setBackgroundResource(R.drawable.prms_img_card_mid);
				lp = getMidLayoutParams();
			}
		}
		convertView.setLayoutParams(lp);
		// 持仓
		if (map.get(Prms.PRMS_CODE).equals(
				ConstantGloble.PRMS_CURRENCYCODE_DOLORG)
				|| map.get(Prms.PRMS_CODE).equals(
						ConstantGloble.PRMS_CURRENCYCODE_DOLORS)
				|| map.get(Prms.PRMS_CODE).equals(
						ConstantGloble.PRMS_CURRENCYCODE_RMBS)
				|| map.get(Prms.PRMS_CODE).equals(
						ConstantGloble.PRMS_CURRENCYCODE_RMBG)
				|| map.get(Prms.PRMS_CODE).equals(
						ConstantGloble.PRMS_CURRENCYCODE_RMBBOG)
				|| map.get(Prms.PRMS_CODE).equals(
						ConstantGloble.PRMS_CURRENCYCODE_RMBBAG)
				|| map.get(Prms.PRMS_CODE).equals(
						ConstantGloble.PRMS_CURRENCYCODE_DOLORBAG)
				|| map.get(Prms.PRMS_CODE).equals(
						ConstantGloble.PRMS_CURRENCYCODE_DOLORBOG)) {
			String sourceCurrency = (String) map.get(Prms.PRMS_CODE);
			holder.saleBtnView.setVisibility(View.VISIBLE);

			// 炒汇标示
			final String currencyCode = String.valueOf(map.get(Prms.PRMS_CODE));
			if (currencyCode.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORG)
					|| currencyCode
							.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORS)
					|| currencyCode
							.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORBAG)
					|| currencyCode
							.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORBOG)) {// 美元金或者美元银
				String cashimit = String.valueOf(map.get(Prms.PRMS_CASHREMIT));
				final String currency = LocalData.prmsTradeStyle1MaptoChi
						.get(currencyCode)
						+ "（"
						+ LocalData.CurrencyCashremit.get(cashimit) + "）";
				holder.currencyTextView.setText(currency);
			} else {
				final String currency = LocalData.prmsTradeStyle1MaptoChi
						.get(currencyCode);
				holder.currencyTextView.setText(currency);
			}

			final String balance = String.valueOf(map
					.get(Prms.QUERY_PEMSACTBALANCE_AVAILABLEBALANCE));

			final String unit = LocalData.prmsUnitMaptoChi.get(currencyCode);
			holder.prmsBalanceTextView.setVisibility(View.VISIBLE);
			holder.prmsBalanceUnitTextView.setVisibility(View.VISIBLE);
//			holder.prmsBalanceTextView.setText(PrmsControl
//					.parseStringPatternPrmAccBalance(sourceCurrency, balance));
			holder.prmsBalanceTextView.setText(StringUtil.valueOf1(balance));
			holder.prmsBalanceUnitTextView.setText(unit);
			holder.saleBtnView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (Double.valueOf(balance) <= 0) {
						BaseDroidApp
								.getInstanse()
								.showInfoMessageDialog(
										context.getString(R.string.prms_balanceOne_null_error));
						return;
					}
					Intent i = new Intent();
					// 需要传入的参数 卖出价格/卖出标记/卖出贵金属种类
					i.putExtra(Prms.PRMS_TRANSFLAG,
							ConstantGloble.PRMS_TRADE_TRANSFLAG_SALE);
					i.putExtra(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE,
							currencyCode);
					i.setClass(context, PrmsTradeSaleActivity.class);
					context.startActivity(i);
				}
			});
		} else {
			holder.saleBtnView.setVisibility(View.GONE);
			// 余额显示
			final String currencyCode = String.valueOf(map.get(Prms.PRMS_CODE));
			String cashimit = String.valueOf(map.get(Prms.PRMS_CASHREMIT));
			final String balance = String.valueOf(map
					.get(Prms.QUERY_PEMSACTBALANCE_AVAILABLEBALANCE));
			if (cashimit.equals(ConstantGloble.CASHRMIT_RMB)) {// 人民币
				holder.currencyTextView.setText(LocalData.Currency
						.get(currencyCode));
			} else {
				holder.currencyTextView.setText(LocalData.Currency
						.get(currencyCode)
						+ "（"
						+ LocalData.CurrencyCashremit.get(cashimit) + "）");
			}
			holder.prmsBalanceTextView.setVisibility(View.VISIBLE);
			holder.prmsBalanceUnitTextView.setVisibility(View.GONE);
			holder.prmsBalanceTextView.setText(StringUtil.parseStringPattern(
					balance, 2));
			holder.prmsBalanceTextView.setTextColor(context.getResources()
					.getColor(R.color.red));
		}

		return convertView;
	}

	/**
	 * 存放控件
	 * 
	 * @author Administrator
	 * 
	 */
	public final class ViewHolder {
		public TextView currencyTextView;
		public TextView prmsBalanceTextView;
		public TextView prmsBalanceUnitTextView;
		public View saleBtnView;
	}

	/**
	 * 中间卡片的布局尺寸
	 * 
	 * @return
	 */
	private LayoutParams getMidLayoutParams() {
		int height = (int) context.getResources().getDimension(
				R.dimen.prms_accbalance_mid_height);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, height);
		return lp;
	}

	/**
	 * 底部卡片的布局尺寸
	 * 
	 * @return
	 */
	private LayoutParams getButtomLayoutParams() {
		int height = (int) context.getResources().getDimension(
				R.dimen.prms_accbalance_buttom_height);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, height);
		return lp;
	}

	/**
	 * 头部卡片的布局尺寸
	 * 
	 * @return
	 */
	private LayoutParams getTopLayoutParams() {
		int height = (int) context.getResources().getDimension(
				R.dimen.prms_accbalance_top_height);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, height);
		return lp;
	}

	/**
	 * 对贵金属持仓进行排序 人民币 美元 现钞 美元现汇
	 * 
	 * 人民币金 美元金现钞 美元金现汇
	 * 
	 * 银， 铂金，钯金
	 * 
	 * @param sourceList
	 * @return
	 */
	private List<Map<String, Object>> getSortDateList(
			List<Map<String, Object>> sourceList) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		for(Currency currency:sortCurrencyList){
			Map resultMap = getBalanceByCurrencyCode(sourceList,currency.getCurrencyCode(),currency.getCashRemit());
			if(resultMap != null){
				resultList.add(resultMap);
			}
		}
		return resultList;
	}

	private Map<String, Object> getBalanceByCurrencyCode(
			List<Map<String, Object>> sourceList, String sourceCurrencyCode,String cashRemit) {
		for (Map<String, Object> map : sourceList) {
			final String code = (String) map.get(Prms.PRMS_CODE);
			final String cashRemitStr = (String) map.get(Prms.QUERY_PEMSACTBALANCE_CASHREMIT);
			if (sourceCurrencyCode.equals(code)&&(cashRemit.equals(cashRemitStr))) {
				return map;
			}
		}
		return null;
	}

	/** 排序的顺序 */
	private static List<Currency> sortCurrencyList = new ArrayList<Currency>() {
		{
			add(new Currency(ConstantGloble.PRMS_CODE_RMB,
					ConstantGloble.CASHRMIT_RMB));
			add(new Currency(ConstantGloble.PRMS_CODE_DOLOR,
					ConstantGloble.CASHRMIT_CASH));
			add(new Currency(ConstantGloble.PRMS_CODE_DOLOR,
					ConstantGloble.CASHRMIT_PARITIES));
			add(new Currency(ConstantGloble.PRMS_CURRENCYCODE_RMBG,
					ConstantGloble.CASHRMIT_RMB));
			add(new Currency(ConstantGloble.PRMS_CURRENCYCODE_DOLORG,
					ConstantGloble.CASHRMIT_CASH));
			add(new Currency(ConstantGloble.PRMS_CURRENCYCODE_DOLORG,
					ConstantGloble.CASHRMIT_PARITIES));
			add(new Currency(ConstantGloble.PRMS_CURRENCYCODE_RMBS,
					ConstantGloble.CASHRMIT_RMB));
			add(new Currency(ConstantGloble.PRMS_CURRENCYCODE_DOLORS,
					ConstantGloble.CASHRMIT_CASH));
			add(new Currency(ConstantGloble.PRMS_CURRENCYCODE_DOLORS,
					ConstantGloble.CASHRMIT_PARITIES));
			add(new Currency(ConstantGloble.PRMS_CURRENCYCODE_RMBBOG,
					ConstantGloble.CASHRMIT_RMB));
			add(new Currency(ConstantGloble.PRMS_CURRENCYCODE_DOLORBOG,
					ConstantGloble.CASHRMIT_CASH));
			add(new Currency(ConstantGloble.PRMS_CURRENCYCODE_DOLORBOG,
					ConstantGloble.CASHRMIT_PARITIES));
			add(new Currency(ConstantGloble.PRMS_CURRENCYCODE_RMBBAG,
					ConstantGloble.CASHRMIT_RMB));
			add(new Currency(ConstantGloble.PRMS_CURRENCYCODE_DOLORBAG,
					ConstantGloble.CASHRMIT_CASH));
			add(new Currency(ConstantGloble.PRMS_CURRENCYCODE_DOLORBAG,
					ConstantGloble.CASHRMIT_PARITIES));
		}
	};

}

class Currency {
	private String currencyCode;
	private String cashRemit;

	public Currency(String currencyCode, String cashRemit) {
		this.cashRemit = cashRemit;
		this.currencyCode = currencyCode;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCashRemit() {
		return cashRemit;
	}

	public void setCashRemit(String cashRemit) {
		this.cashRemit = cashRemit;
	}

}
