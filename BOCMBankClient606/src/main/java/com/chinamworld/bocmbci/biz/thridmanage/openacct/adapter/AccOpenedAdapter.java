package com.chinamworld.bocmbci.biz.thridmanage.openacct.adapter;

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
import com.chinamworld.bocmbci.biz.thridmanage.BiiConstant.BookingStatus;
import com.chinamworld.bocmbci.utils.QueryDateUtils;

/**
 * @ClassName: AccOpenedAdapter
 * @Description: 预约开户查询
 * @author lql
 * @date 2013-8-21 下午04:31:08
 * 
 */
public class AccOpenedAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> mList;

	public AccOpenedAdapter(Context cn, List<Map<String, Object>> list) {
		this.mContext = cn;
		this.mList = list;
	}

	@Override
	public int getCount() {
		if (mList == null) {
			return -1;
		}
		return mList.size();
	}

	@Override
	public Map<String, Object> getItem(int position) {
		if (mList == null || mList.size() <= position) {
			return null;
		} else {
			return mList.get(position);
		}
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
			convertView = LinearLayout.inflate(mContext, R.layout.third_acctopened_query_item, null);
			h.tvTime = (TextView) convertView.findViewById(R.id.tv_trade_time);
			h.tvInvalidDateTime = (TextView) convertView.findViewById(R.id.tv_invalid_date);
			h.tvState = (TextView) convertView.findViewById(R.id.tv_state);

			convertView.setTag(h);
		} else {
			h = (ViewHodler) convertView.getTag();
		}

		Map<String, Object> map = (Map<String, Object>) mList.get(position);
		h.tvTime.setText(QueryDateUtils.getCurDate(Long.parseLong((String) map.get(Third.OPENED_HISTROY_BOOKDETE))));
		h.tvInvalidDateTime.setText(QueryDateUtils.getCurDate(Long.parseLong((String) map
				.get(Third.OPENED_HISTORY_INIDATE))));
		h.tvState.setText(BookingStatus.getBookingStatusStr((String) map.get(Third.OPENED_HISTORY_BOOKSTATE)));
		return convertView;
	}

	public void setData(List<Map<String, Object>> list) {
		mList = list;
	}

	private static class ViewHodler {
		public TextView tvTime;
		public TextView tvInvalidDateTime;
		public TextView tvState;
	}
}
