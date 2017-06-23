package com.chinamworld.bocmbci.widget.adapter;


import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.log.LogGloble;

public class ButtonAdapter extends BaseAdapter {
	
	private static String TAG = "ButtonAdapter";
    
	private Context context=null;
	private ArrayList<Map<String,String>> textList=null;
	/**
     *@onItemClickListener: ListView中的每一项被单击时触发的事件
     */
	private OnItemClickListener onItemClickListener;
	/**
	 * @itemButtonClickListener:ListView中的每一项都添加Button按钮，
	 * itemButtonClickListener是Button按钮的监听事件
	 */
	private OnItemClickListener itemButtonClickListener;
	
	public OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}
	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}
	public OnItemClickListener getItemButtonClickListener() {
		return itemButtonClickListener;
	}
	public void setItemButtonClickListener(OnItemClickListener itemButtonClickListener) {
		this.itemButtonClickListener = itemButtonClickListener;
	}
	
	
	public ButtonAdapter(Context context,ArrayList<Map<String,String>> textList){
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
			convertView=LayoutInflater.from(context).inflate(R.layout.popwindow_list_row, null);
			holder.itemText=(TextView) convertView.findViewById(R.id.listTextView);
		    holder.itemButton=(ImageView) convertView.findViewById(R.id.imgbutton);
		    convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		int itemHeight = (LayoutValue.SCREEN_HEIGHT-25)/12;
		int itemWidth = LayoutValue.SCREEN_WIDTH;
		LayoutParams lp = new LayoutParams(itemWidth, itemHeight);
		convertView.setLayoutParams(lp);
		//设置每一个TextView的值
		holder.itemText.setText(textList.get(position).get("content"));
		//设置每一个Button
		holder.itemButton.setTag(position);
		holder.itemButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LogGloble.i(TAG, position+"************");
				if(itemButtonClickListener != null){
					itemButtonClickListener.onItemClick(null, v, position, position);
				}
			}
		});
		//设置每一个ListView的子项被点击是触发的事件
//		convertView.setOnClickListener(new OnClickListener() {
//		    @Override
//			public void onClick(View v) {
//		    	onItemClickListener.onItemClick(null, v, position, position);
//			}
//		});
		return convertView;
	}
	
   private class ViewHolder{
	   private TextView itemText=null;//内容
	   private ImageView itemButton=null;//跳转按钮
   }
	
}
