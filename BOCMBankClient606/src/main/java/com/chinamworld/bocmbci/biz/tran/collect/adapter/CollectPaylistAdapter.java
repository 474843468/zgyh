package com.chinamworld.bocmbci.biz.tran.collect.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Collect;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: CollectPaylistAdapter
 * @Description: 支付协议列表
 * @author luql
 * @date 2014-3-24 下午04:58:26
 */
public class CollectPaylistAdapter extends BaseAdapter {

	private Context context = null;
	private List<Map<String, Object>> payList = null;
	private static final String DATA = "9999/12/32";

	public CollectPaylistAdapter(Context context, List<Map<String, Object>> payList) {
		this.context = context;
		this.payList = payList;
	}

	public void setData(List<Map<String, Object>> list) {
		payList = list;
	}

	@Override
	public int getCount() {
		return payList.size();
	}

	@Override
	public Object getItem(int position) {
		return payList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.collect_paylist_item, null);
			viewHolder.protocolCodeView = (TextView) convertView.findViewById(R.id.tv_protocol_code);
			viewHolder.beginAmtView = (TextView) convertView.findViewById(R.id.tv_begin_amt);
			viewHolder.dayQuotaView = (TextView) convertView.findViewById(R.id.tv_day_quota);
			viewHolder.dayLimitView = (TextView) convertView.findViewById(R.id.tv_day_limit);
			viewHolder.monthLimitView = (TextView) convertView.findViewById(R.id.tv_month_limit);
			viewHolder.monthQuotaView = (TextView) convertView.findViewById(R.id.tv_month_quota);
			viewHolder.protocolBeginView = (TextView) convertView.findViewById(R.id.tv_protocol_begin);
			viewHolder.protocolEndView = (TextView) convertView.findViewById(R.id.tv_protocol_end);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Map<String, Object> map = payList.get(position);
		String protocolCode = (String) map.get(Collect.payNo);
		String beginAmt = (String) map.get(Collect.payQuotaPer);
		String dayQuota = (String) map.get(Collect.payQuotaD);
		String dayLimit = (String) map.get(Collect.payLimitD);
		String monthLimit = (String) map.get(Collect.payLimitM);
		String monthQuota = (String) map.get(Collect.payQuotaM);
		String protocolBegin = (String) map.get(Collect.payBegin);
		String protocolEnd = (String) map.get(Collect.payEnd);

		viewHolder.protocolCodeView.setText(protocolCode);
		viewHolder.beginAmtView.setText(StringUtil.parseStringPattern(beginAmt, 2));
		viewHolder.dayQuotaView.setText(StringUtil.parseStringPattern(dayQuota, 2));
		viewHolder.dayLimitView.setText(dayLimit);
		viewHolder.monthLimitView.setText(monthLimit);
		viewHolder.monthQuotaView.setText(StringUtil.parseStringPattern(monthQuota, 2));
		viewHolder.protocolBeginView.setText(protocolBegin);
		if(DATA.equals(protocolEnd)){
			viewHolder.protocolEndView.setText(R.string.collect_permanently_effective);
		}else{
			viewHolder.protocolEndView.setText(protocolEnd);
		}
		
		return convertView;
	}

	private class ViewHolder {
		/** 协议号 */
		public TextView protocolCodeView;
		/** 单笔业务金额上限 */
		public TextView beginAmtView;
		/** 日累计金额上限 */
		public TextView dayQuotaView;
		/** 日累计业务笔数上限 */
		public TextView dayLimitView;
		/** 月累计业务笔数上限 */
		public TextView monthLimitView;
		/** 月累计金额上限 */
		public TextView monthQuotaView;
		/** 签约日期 */
		public TextView protocolBeginView;
		/** 失效日期 */
		public TextView protocolEndView;
	}

}
