package com.chinamworld.bocmbci.biz.plps.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.utils.StringUtil;

public class AnnuitySpinnerAdapter extends BaseAdapter{
	
	private Context context;
	private List<String> mList;
	private int mPosition;

	public AnnuitySpinnerAdapter(Context context, List<String> list,int position){
		this.context = context;
		this.mList = list;
		this.mPosition = position;
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
		ViewHodler h;
		if (convertView == null) {
			h = new ViewHodler();
			convertView = LayoutInflater.from(context).inflate(R.layout.plps_spinner_item, null);
			h.text = (TextView) convertView.findViewById(R.id.text);
			h.btn = (RadioButton) convertView.findViewById(R.id.radBtn);
			convertView.setTag(h);
		}else{
			h = (ViewHodler) convertView.getTag();
		}
		h.text.setText(mList.get(position));
		if (position == mPosition) {
			h.btn.setChecked(true);
		}else {
			h.btn.setChecked(false);
		}
		return convertView;
	}
	
	public View getDropDownView(int position, View convertView,
			ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					android.R.layout.simple_spinner_dropdown_item, null);
		}
		TextView text = (TextView) convertView.findViewById(android.R.id.text1);
		text.setText(mList.get(position));
		return convertView;
	}

	public class ViewHodler {
		public TextView text;
		public RadioButton btn;
	}
}
