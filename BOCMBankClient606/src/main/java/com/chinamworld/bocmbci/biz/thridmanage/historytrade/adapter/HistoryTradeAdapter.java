package com.chinamworld.bocmbci.biz.thridmanage.historytrade.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Third;
import com.chinamworld.bocmbci.biz.thridmanage.BiiConstant.TransferWayType;

/**
* @ClassName: HistoryTradeAdapter
* @Description: 交易历史查询
* @author lql
* @date 2013-8-21 上午11:36:04
*
 */
public class HistoryTradeAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, String>> mList;

	public HistoryTradeAdapter(Context cn, List<Map<String, String>> list) {
		this.mContext = cn;
		this.mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		if (mList.size() > position) {
			return mList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHodler h;
		if (convertView == null) {
			h = new ViewHodler();
			convertView = LinearLayout.inflate(mContext, R.layout.third_historytrade_query_item, null);
			h.tvTime = (TextView) convertView.findViewById(R.id.tv_trade_time);
			h.tvType = (TextView) convertView.findViewById(R.id.tv_trade_type);
			h.tvAmout = (TextView) convertView.findViewById(R.id.tv_trade_amout);

			convertView.setTag(h);
		} else {
			h = (ViewHodler) convertView.getTag();
		}

		Map<String, String> map = mList.get(position);

		h.tvTime.setText(map.get(Third.QUERY_PAYDATE));
		String typeCode = map.get(Third.QOERY_TRADE_TYPE);
		h.tvType.setText(TransferWayType.getTransferWayTypeSimpleStr(typeCode));
//		if (!StringUtil.isNullOrEmpty((String) map.get(Third.QOERY_TRADE_TYPE))) {
//			if (((String) map.get(Third.QOERY_TRADE_TYPE)).equals(Third.QOERY_TRADE_TYPE)) {
//				h.tvType.setText(Third.sortMap[2]);
//			} else {
//				h.tvType.setText(Third.sortMap[1]);
//			}
//		}
		h.tvAmout.setText(map.get(Third.TRANSFER_COMIT_AMOUNT));
		return convertView;
	}

	public void setData(List<Map<String, String>> data) {
		this.mList = data;
	}

	private static class ViewHodler {
		public TextView tvTime;
		public TextView tvType;
		public TextView tvAmout;
	}
}
