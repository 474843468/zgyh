package com.chinamworld.bocmbci.biz.bocinvt.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;

/**
 * 定时定额/自动投资协议查询适配器
 * 
 * @author wangmengmeng
 * 
 */
public class AgreeQueryExtendAdapter extends BaseAdapter {
	/** 查询结果列表信息 */
	private List<Map<String, Object>> agreeQueryList;
	private Context context;

	public AgreeQueryExtendAdapter(Context context,
			List<Map<String, Object>> agreeQueryList) {
		this.context = context;
		this.agreeQueryList = agreeQueryList;
	}

	@Override
	public int getCount() {
		return agreeQueryList.size();
	}

	@Override
	public Object getItem(int position) {
		return agreeQueryList.get(position);

	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.bocinvt_hisproduct_list_item, null);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.boci_product_name);
			viewHolder.tv_number = (TextView) convertView
					.findViewById(R.id.boci_yearlyRR);
			viewHolder.tv_currency = (TextView) convertView
					.findViewById(R.id.boci_timeLimit);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
					viewHolder.tv_name);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
					viewHolder.tv_number);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
					viewHolder.tv_currency);
			viewHolder.iv_go = (ImageView) convertView
					.findViewById(R.id.boci_gotoDetail);
			viewHolder.iv_go.setVisibility(View.VISIBLE);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// 赋值操作
		viewHolder.tv_name.setText(String.valueOf(agreeQueryList.get(position)
				.get(BocInvt.BOC_EXTEND_SERIALNAME_RES)));
		viewHolder.tv_number.setText(String.valueOf(agreeQueryList
				.get(position).get(BocInvt.BOC_EXTEND_CONTRACTSEQ_RES)));
		String currency = (String) agreeQueryList.get(position).get(
				BocInvt.BOC_EXTEND_PROCUR_RES);
		viewHolder.tv_currency.setText(LocalData.Currency.get(currency));
		return convertView;
	}

	public List<Map<String, Object>> getAgreeQueryList() {
		return agreeQueryList;
	}

	public void setAgreeQueryList(List<Map<String, Object>> agreeQueryList) {
		this.agreeQueryList = agreeQueryList;
		notifyDataSetChanged();
	}

	private class ViewHolder {
		/** 名称 */
		public TextView tv_name;
		/** 协议序号 */
		public TextView tv_number;
		/** 币种 */
		public TextView tv_currency;
		public ImageView iv_go;
	}

}
