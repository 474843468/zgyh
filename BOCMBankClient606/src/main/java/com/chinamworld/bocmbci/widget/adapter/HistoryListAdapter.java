package com.chinamworld.bocmbci.widget.adapter;


import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;

public class HistoryListAdapter extends BaseAdapter {
    
	private Context context=null;
	private ArrayList<String> textList=null;
	/**
     *@onItemClickListener: ListView中的每一项被单击时触发的事件
     */
	private OnItemClickListener onItemClickListener;
	/**
	 * @itemButtonClickListener:ListView中的每一项都添加Button按钮，
	 * itemButtonClickListener是Button按钮的监听事件
	 */
	private OnClickListener itemButtonClickListener;
	
	public OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}
	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}
	public OnClickListener getItemButtonClickListener() {
		return itemButtonClickListener;
	}
	public void setItemButtonClickListener(OnClickListener itemButtonClickListener) {
		this.itemButtonClickListener = itemButtonClickListener;
	}
	
	
	public HistoryListAdapter(Context context,ArrayList<String> textList){
		this.context=context;
		this.textList=textList;
	}
	
	
	
	@Override
	public int getCount() {
		return textList.size();
	}

	@Override
	public Object getItem(int position) {
		return textList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder=new ViewHolder();
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.popwindow_history_list_item, null);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		convertView.setTag(holder);
		//设置每一个TextView的值
		holder.itemText=(TextView) convertView.findViewById(R.id.tv_history_time);
		holder.itemText.setText(textList.get(position));
		//设置每一个Button
		
		return convertView;
	}
	
   private class ViewHolder{
	   private TextView itemText=null;//内容
   }
	
}
