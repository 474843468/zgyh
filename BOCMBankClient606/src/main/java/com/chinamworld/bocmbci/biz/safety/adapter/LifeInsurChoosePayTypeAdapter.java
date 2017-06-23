package com.chinamworld.bocmbci.biz.safety.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Safety;

public class LifeInsurChoosePayTypeAdapter extends BaseAdapter {

	private Context context;
	
	private List<Map<String, Object>> mList;
	
	public LifeInsurChoosePayTypeAdapter(Context context, List<Map<String, Object>> list) {
		this.context = context;
		mList = list;
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Map<String, Object> getItem(int position) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.safety_life_input_insurance_choose_paytype_item, null);
			vh.tvPayYearType = (TextView) convertView.findViewById(R.id.tv_payYearType);
			vh.tvPayYear = (TextView) convertView.findViewById(R.id.tv_payYear);
			vh.tvInsuYearType = (TextView) convertView.findViewById(R.id.tv_insuYearType);
			vh.tvInsuYear = (TextView) convertView.findViewById(R.id.tv_insuYear);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		
		vh.tvPayYearType.setText((String) getItem(position).get(Safety.PAYYEARNAME));
		vh.tvPayYear.setText((String) getItem(position).get(Safety.PAYYEAR));
		vh.tvInsuYearType.setText((String) getItem(position).get(Safety.INSUYEARNAME));
		vh.tvInsuYear.setText((String) getItem(position).get(Safety.INSUYEAR));
		return convertView;
	}
	
	private class ViewHolder {
		TextView tvPayYearType;
		TextView tvPayYear;
		TextView tvInsuYearType;
		TextView tvInsuYear;
	}
}
