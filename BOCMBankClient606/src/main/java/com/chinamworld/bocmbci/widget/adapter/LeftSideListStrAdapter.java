package com.chinamworld.bocmbci.widget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;

public class LeftSideListStrAdapter extends BaseAdapter {

	private Context context=null;
	
	private String[] listData = null;
	
	
	public LeftSideListStrAdapter(Context context, String[] listData){
		this.context = context;
		this.listData = listData;
	}
	
	@Override
	public int getCount() {
		return listData.length;
	}

	@Override
	public Object getItem(int position) {
		return listData[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.left_list_str_item, null);
			TextView listText = (TextView) convertView.findViewById(R.id.list_text);
			listText.setText(listData[position]);
		}
		return convertView;
	}

}
