package com.chinamworld.bocmbci.biz.tran.remit.adapters;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.utils.StringUtil;

public class RemitQueryMealDetailAdapter extends BaseAdapter {
	private Context context = null;
	private List<Map<String, String>> list = null;

	public RemitQueryMealDetailAdapter(Context context, List<Map<String, String>> list) {
		this.context = context;
		this.list = list;
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

	public void dataChanged(List<Map<String, String>> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.tran_remit_query_detail_item, null);
			holder = new ViewHolder();
			holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			holder.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
			holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String, String> map = list.get(position);
		String tranDate = map.get(Tran.TRAN_TRANDATE_RES);
		String tran = map.get("tranAmount");
		String tranAmount = StringUtil.parseStringPattern(tran,2);
		String remitSetMealProducProperty = map.get("remitSetMealProducProperty");
		 
		holder.tv_date.setText(tranDate);
		holder.tv_amount.setText(tranAmount);
		holder.tv_type.setText(Tran.MealTypeMap.get(remitSetMealProducProperty));
		return convertView;
	}

	private class ViewHolder {
		private TextView tv_date = null;
		private TextView tv_amount = null;
		private TextView tv_type = null;
	}
}
