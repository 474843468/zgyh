package com.chinamworld.bocmbci.widget.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Login;

/**
 * 安全因子的适配器
 * 
 * @author wjp
 * 
 */
public class SecurityFactorAdapter extends BaseAdapter {

	private LayoutInflater inflater;// 布局解析器
	public int index = 0;// 用于记住用户选择项的标识
	private List<Map<String,Object>> list;// 数据源

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * 构造器
	 * 
	 * @param context
	 * @param list
	 */
	public SecurityFactorAdapter(Context context, List<Map<String,Object>> list) {
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	/**
	 * 改变数据调用此方法
	 * 
	 * @param list
	 */
	public void changeDate(int index) {
		this.index = index;
		notifyDataSetChanged();
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
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.security_list_item, null);
			holder = new ViewHolder();
			holder.tvAttribuite = (TextView) convertView.findViewById(R.id.tvAttribute);
			holder.ivCheck = (ImageView) convertView.findViewById(R.id.iv_check);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if("96".equals(list.get(position).get(Login.ID)))
			holder.tvAttribuite.setText("手机交易码");
		else
			holder.tvAttribuite.setText((String)list.get(position).get(Login.NAME));
		
		if (index == position) {
			holder.ivCheck.setBackgroundResource(R.drawable.tran_select_yes);
//			holder.ivCheck.setVisibility(View.VISIBLE);
			holder.tvAttribuite.setTextColor(Color.RED);
		} else {
			holder.ivCheck.setBackgroundResource(R.drawable.checkbox_selector);
//			holder.ivCheck.setVisibility(View.INVISIBLE);
			holder.tvAttribuite.setTextColor(Color.BLACK);
		}
		return convertView;
	}

	class ViewHolder {
		TextView tvAttribuite;
		ImageView ivCheck;
	}
}
