package com.chinamworld.bocmbci.biz.blpt.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Blpt;
import com.chinamworld.bocmbci.utils.StringUtil;

public class CityAdapter extends BaseAdapter{
	private Context mContext;

	private List<Map<String, Object>> mList;

	private int selectedPosition = -1;

	public CityAdapter(Context cn, List<Map<String, Object>> list) {
		this.mContext = cn;
		this.mList = list;
	}

	@Override
	public int getCount() {
		if (StringUtil.isNullOrEmpty(mList)) {
			return 0;
		} else {
			return mList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		if (StringUtil.isNullOrEmpty(mList)) {
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
		ViewHodler viewHodler;
		if (convertView == null) {
			viewHodler = new ViewHodler();
			convertView = LinearLayout.inflate(mContext, R.layout.blpt_city_item, null);
			viewHodler.imIcon = (CheckBox) convertView.findViewById(R.id.cb);
			viewHodler.cityName = (TextView) convertView.findViewById(R.id.blpt_province_name);
			convertView.setTag(viewHodler);
		} else {
			viewHodler = (ViewHodler) convertView.getTag();
		}
		viewHodler.cityName.setText((String)mList.get(position).get(Blpt.CITY_DISNAME));

		if (position == selectedPosition) {
			viewHodler.imIcon.setChecked(true);
		} else {
			viewHodler.imIcon.setChecked(false);
		}

		return convertView;
	}

	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
		notifyDataSetChanged();
	}
	
	public void setData(List<Map<String, Object>> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	private static class ViewHodler {
		public TextView cityName;
		public CheckBox imIcon;
	}
}
