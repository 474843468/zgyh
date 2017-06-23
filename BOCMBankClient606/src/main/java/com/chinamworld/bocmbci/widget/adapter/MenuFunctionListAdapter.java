package com.chinamworld.bocmbci.widget.adapter;


import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;

public class MenuFunctionListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<String> addFunctionData;
	
	private OnItemClickListener onFunctionItemClickListener;
	
	public MenuFunctionListAdapter(Context context, ArrayList<String> addFunctionData) {
		this.context = context;
		this.addFunctionData = addFunctionData;
	}
	
	@Override
	public int getCount() {
		return addFunctionData.size();
	}

	@Override
	public Object getItem(int position) {
		return addFunctionData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.add_function_lv_item, null);
			Button goFunctionBtn = (Button) convertView.findViewById(R.id.btn_go_function);
			TextView functionNameTv = (TextView) convertView
					.findViewById(R.id.tv_add_name);// 功能名称
			functionNameTv.setText(addFunctionData.get(position));
			// TODO 进入到选择详细添加按钮功能界面 待确定 是点击返回按钮才触发 还是点击listviewItem就触发
			goFunctionBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(onFunctionItemClickListener != null){
						onFunctionItemClickListener.onItemClick(null, v, position, position);
					}
				}
			});
		}
		
		
		return convertView;
	}

	public OnItemClickListener getOnFunctionItemClickListener() {
		return onFunctionItemClickListener;
	}

	public void setOnFunctionItemClickListener(
			OnItemClickListener onFunctionItemClickListener) {
		this.onFunctionItemClickListener = onFunctionItemClickListener;
	}
}
