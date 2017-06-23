package com.chinamworld.bocmbci.biz.invest.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;

/**
 * 安全因子的适配器
 * 
 * @author xby
 * 
 */
public class SecurityAdapter extends BaseAdapter {

	private LayoutInflater inflater;// 布局解析器
	public String defaultIndex = "-1";// 用于记住用户选择项的标识
	private ArrayList<String> list;// 数据源
	/** 安全因子组合的id */
	private ArrayList<String> securityIdList;

	public String getDefaultIndex() {
		return defaultIndex;
	}

	public void setDefaultIndex(String defaultIndex) {
		this.defaultIndex = defaultIndex;
	}

	/**
	 * 构造器
	 * 
	 * @param context
	 * @param list
	 */
	public SecurityAdapter(Context context, ArrayList<String> list,ArrayList<String> securityIdList,String defaultIndex) {
		if (list != null) {
			this.list = list;
		} else {
			this.list = new ArrayList<String>();
		}
		this.securityIdList = securityIdList;
		this.inflater = LayoutInflater.from(context);
		this.defaultIndex = defaultIndex;
	}

	/**
	 * 改变数据调用此方法
	 * 
	 * @param list
	 */
	public void changeDate() {
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = null;
		TextView fir = null;
		ImageView ivCheck = null;
		if (convertView == null) {
			v = inflater.inflate(R.layout.security_list_item, null);
		} else {
			v = (View) convertView;
		}
		fir = (TextView) v.findViewById(R.id.tvAttribute);
		ivCheck = (ImageView) v.findViewById(R.id.iv_check);
		fir.setText(list.get(position));
		if (defaultIndex.equals(securityIdList.get(position))) {
			ivCheck.setBackgroundResource(R.drawable.tran_select_yes);
			fir.setTextColor(Color.RED);
		} else {
			ivCheck.setBackgroundResource(R.drawable.checkbox_selector);
			fir.setTextColor(Color.BLACK);
		}

		return v;
	}

	class ViewHolder {
		TextView tvAttribuite;
		TextView tvContent;
		ImageView ivCheck;
	}
}
