package com.chinamworld.bocmbci.widget.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
/**
 * 带文字说明下拉列表2013-6-20 22:40:32
 * @author WJP
 *
 */
public class SpinnerAdapter extends BaseAdapter{
	
	private Context context;
	private List<String> list;
	private boolean selected;
	
	private String defaultText;

	public SpinnerAdapter(Context context, List<String> list ,String defaultText){
		this.context = context;
		this.list = list;
		this.defaultText = defaultText;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.dept_spinner, null);
		}
		TextView text = (TextView) convertView.findViewById(R.id.dept_spinner_textview);
		if (!selected) {
			text.setText(defaultText);
		} else {
			text.setText(list.get(position));
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
		text.setText(list.get(position));
		return convertView;
	};

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public void setData(List<String> mList,boolean selected,String defaultText) {
		this.defaultText = defaultText;
		this.selected = selected;
		this.list = mList;
		notifyDataSetChanged();
	}

	public List<String> getData(){
		return list;
	}
}
