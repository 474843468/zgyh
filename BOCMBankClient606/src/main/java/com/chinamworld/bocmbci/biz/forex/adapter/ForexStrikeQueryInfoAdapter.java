package com.chinamworld.bocmbci.biz.forex.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Forex;

/**
 * 
 * @author 宁焰红
 * 
 */
public class ForexStrikeQueryInfoAdapter extends BaseAdapter {
	private final String TAG = "ForexStrikeQueryInfoAdapter";
	private Context context;
	private List<Map<String, Object>> list;
	private OnItemClickListener onItemClickListener = null;

	private String sellAmount = null;// 卖出金额
	private String buyAmount = null;// 买入金额
	private String rate = null;// 成交汇率
	private String realTransTime = null;// 成交时间
	private String consignNumber = null;
	private String sellCode = null;
	private String buyCode = null;
	private String exchangeTranType = null;// 成交类型

	private Map<String, Object> map = null;

	public OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public ForexStrikeQueryInfoAdapter(Context context, List<Map<String, Object>> list) {
		super();
		this.context = context;
		this.list = list;
	}

	public void dataChanged(List<Map<String, Object>> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.forex_trade_query_info_listitem, null);
			holder = new ViewHolder();
			holder.number = (TextView) convertView.findViewById(R.id.forex_query_tradenum);
			holder.sellCode = (TextView) convertView.findViewById(R.id.forex_query_tradeprice);
			holder.buyCode = (TextView) convertView.findViewById(R.id.forex_query_trademethod);
			holder.arrow = (ImageView) convertView.findViewById(R.id.rate_gotoDetail);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		map = list.get(position);
		holder.number.setTag(position);
		holder.sellCode.setTag(position);
		holder.buyCode.setTag(position);
		holder.arrow.setTag(position);

		// getDate();
		final String consignNumber = (String) map.get(Forex.FOREX_CONSIGNNUMBER_RES);

		Map<String, String> firstBuyCurrency = (Map<String, String>) map.get(Forex.FOREX_FIRSTBUYCURRENY_RES);
		final String buyCode = firstBuyCurrency.get(Forex.FOREX_CODE_RES);

		Map<String, String> firstSellCurrency = (Map<String, String>) map.get(Forex.FOREX_FIRESTSELLCURRENCY_RES);
		final String sellCode = firstSellCurrency.get(Forex.FOREX_CODE_RES);
		String firstStatus = (String) map.get(Forex.FOREX_FRESTSTATUS_RES);
		String secondStatus = (String) map.get(Forex.FOREX_SECONDSTATUS_RES);
		// 成交类型
		final String exchangeTranType = (String) map.get(Forex.FOREX_EXCHANGETRANTYPE_RES);
		// 成交时间
		final String realTransTime = (String) map.get(Forex.FOREX_REALTRANSTIME);
//		if (firstStatus.equalsIgnoreCase(ConstantGloble.FOREX_STATUS)) {
//			sellAmount = (String) map.get(Forex.FOREX_FIRSTSELLAMOUNT_RES);
//			buyAmount = (String) map.get(Forex.FOREX_FIRSTBUYAMOUNT_RES);
//			rate = (String) map.get(Forex.FOREX_FRESTRATE_RES);
//		} else if (secondStatus.equalsIgnoreCase(ConstantGloble.FOREX_STATUS)) {
//			sellAmount = (String) map.get(Forex.FOREX_SECONDSELLAMOUNT_RES);
//			buyAmount = (String) map.get(Forex.FOREX_SECONDBUYAMOUNT_RES);
//			rate = (String) map.get(Forex.FOREX_SECONDRATE_RES);
//		}

		holder.number.setText(consignNumber);
		holder.sellCode.setText(sellCode);
		holder.buyCode.setText(buyCode);
		// listView的事件
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onItemClickListener != null) {
					onItemClickListener.onItemClick(null, v, position, position);
				}
			}
		});
		holder.arrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onItemClickListener != null) {
					onItemClickListener.onItemClick(null, v, position, position);
				}
			}
		});

		return convertView;
	}

	/**
	 * 内部类--控件
	 */
	public class ViewHolder {
		public TextView number;
		public TextView sellCode;
		public TextView buyCode;
		public ImageView arrow;
	}

}
