package com.chinamworld.bocmbci.biz.plps.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 预付卡查询结果列表数据适配器
 * 
 * @author Zhi
 */
public class PrePaidCardResultQueryAdapter extends BaseAdapter {

	/** 上下文对象 */
	private Context mContext;
	/** 数据源 */
	private List<Map<String, Object>> mList;
	
	/** 构造方法 */
	public PrePaidCardResultQueryAdapter(Context context, List<Map<String, Object>> list) {
		mContext = context;
		mList = list;
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}
	
	public Map<String, Object> getMapItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.plps_prepaid_card_query_item, null);
			vh.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
			vh.tvPrepaidCardNumber = (TextView) convertView.findViewById(R.id.tv_prepaidCardNumber);
			vh.tvTransValue = (TextView) convertView.findViewById(R.id.tv_transValue);
			vh.tvTrade = (TextView) convertView.findViewById(R.id.tv_trade);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		Map<String, Object> map = getMapItem(position);
		vh.tvDate.setText((String) map.get(Plps.TRANDATE));
		vh.tvPrepaidCardNumber.setText((String) map.get(Plps.CARDNUM));
		vh.tvTransValue.setText(StringUtil.parseStringPattern((String) map.get(Plps.AMOUNT), 2));
		vh.tvTrade.setText(PlpsDataCenter.channel.get((String) map.get(Plps.CHANNEL)));
		return convertView;
	}
	
	public void setData(List<Map<String, Object>> list) {
		this.mList = list;
		notifyDataSetChanged();
	}
	
	class ViewHolder {
		/** 交易日期 */
		public TextView tvDate;
		/** 预付卡号 */
		public TextView tvPrepaidCardNumber;
		/** 充值金额 */
		public TextView tvTransValue;
		/** 充值渠道 */
		public TextView tvTrade;
	}
}
