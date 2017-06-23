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
import com.chinamworld.bocmbci.constant.LocalData;

public class ForexQuashQueryAdapter extends BaseAdapter {
	private final String TAG = "ForexQuashQueryAdapter";
	private Context context;
	private List<Map<String, Object>> list;
	/** 1-当前有效，2-历史有效 */
	private int tag;
	private OnItemClickListener onItemClickListener = null;

	public OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public ForexQuashQueryAdapter(Context context, List<Map<String, Object>> list, int tag) {
		super();
		this.context = context;
		this.list = list;
		this.tag = tag;
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
		Map<String, Object> map = list.get(position);
		holder.number.setTag(position);
		holder.sellCode.setTag(position);
		holder.buyCode.setTag(position);
		holder.arrow.setTag(position);
		String consignNumber = (String) map.get(Forex.FOREX_CONSIGNNUMBER_RES);
		Map<String, String> firstBuyCurrency = (Map<String, String>) map.get(Forex.FOREX_FIRSTBUYCURRENY_RES);
		String buyCode = firstBuyCurrency.get(Forex.FOREX_CODE_RES);
		String buyCodeName = LocalData.Currency.get(buyCode);
		Map<String, String> firstSellCurrency = (Map<String, String>) map.get(Forex.FOREX_FIRESTSELLCURRENCY_RES);
		String sellCode = firstSellCurrency.get(Forex.FOREX_CODE_RES);
		String sellCodeName = LocalData.Currency.get(sellCode);
		if (tag == 1) {
			holder.number.setText(consignNumber);
			holder.sellCode.setText(sellCodeName);
			holder.buyCode.setText(buyCodeName);
		} else if (tag == 2) {
			holder.number.setText(consignNumber);
			holder.sellCode.setText(sellCodeName);
			holder.buyCode.setText(buyCodeName);
		}

		// listView的事件
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Map<String, Object> map = list.get(position);
				// Intent intent = new Intent(context,
				// ForexQuashQueryDateilActivity.class);
				// intent.putExtra(ConstantGloble.FOREX_TAG, tag);
				// BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_TRADE_LIST_KEY,
				// map);
				// context.startActivity(intent);
				if (onItemClickListener != null) {
					onItemClickListener.onItemClick(null, v, position, position);
				}
			}
		});
		holder.arrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Map<String, Object> map = list.get(position);
				// Intent intent = new Intent(context,
				// ForexQuashQueryDateilActivity.class);
				// intent.putExtra(ConstantGloble.FOREX_TAG, tag);
				// BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_TRADE_LIST_KEY,
				// map);
				// context.startActivity(intent);
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
