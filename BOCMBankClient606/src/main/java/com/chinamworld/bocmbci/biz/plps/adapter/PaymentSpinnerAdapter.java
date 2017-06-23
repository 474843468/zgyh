package com.chinamworld.bocmbci.biz.plps.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.utils.StringUtil;

public class PaymentSpinnerAdapter extends BaseAdapter{
	
	private Context mContext;
	private List<String> mList;
	

	public PaymentSpinnerAdapter(Context context, List<String> list){
		this.mContext = context;
		this.mList = list;
	}
	
	@Override
	public int getCount() {
		if (StringUtil.isNullOrEmpty(mList)) {
			return 0;
		}
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		if (StringUtil.isNullOrEmpty(mList)) {
			return null;
		}
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.dept_spinner, null);
		}
		TextView text = (TextView) convertView.findViewById(R.id.dept_spinner_textview);
		text.setTextSize(Float.parseFloat(mContext
				.getString(R.string.textsize_default_blpt)));
		text.setText(mList.get(position));
		return convertView;
	}
	
	public View getDropDownView(int position, View convertView,
			ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					android.R.layout.simple_spinner_dropdown_item, null);
		}
		TextView text = (TextView) convertView.findViewById(android.R.id.text1);
		text.setText(mList.get(position));
		return convertView;
	}

	public List<String> getList() {
		return mList;
	}

	public void setList(List<String> list) {
		this.mList = list;
		notifyDataSetChanged();
	};
}
