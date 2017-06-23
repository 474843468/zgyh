/**
 * 文件名	：TabGridAdapter.java
 * 创建日期	：2012-10-15
 * Copyright (c) 2003-2012 北京联龙博通

 * All rights reserved.
 */
package com.chinamworld.bocmbci.widget.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.widget.entity.ImageTextAndAct;


public class ShotcutTabGridAdapter extends BaseAdapter {
//	private int[] mImages;
//	private String[] mTexts;
	
	private ArrayList<ImageTextAndAct> data;
	
	private LayoutInflater mInflater;
	private int layoutResource;
	
	private Map<Integer,View> viewList;
	
	private Context context;
	
	public ShotcutTabGridAdapter(Context context, ArrayList<ImageTextAndAct> data, int layoutResource){
		this.layoutResource = layoutResource;
		this.data = data;
		mInflater = LayoutInflater.from(context);
		viewList = new HashMap<Integer,View>();
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		if(convertView == null) {
			convertView = mInflater.inflate(layoutResource, null);
		}
		ImageView gridIcon = (ImageView) convertView.findViewById(R.id.grid_icon);
		gridIcon.setBackgroundResource(data.get(position).getImageId());
		TextView gridText = (TextView) convertView.findViewById(R.id.grid_text);
		gridText.setText(data.get(position).getText());
		
		return convertView;
	}

	public View getItemView(int position){
		return viewList.get(position);
	}

	private void setViewText(TextView v, String text) {
		v.setText(text);
	}

	private void setViewImage(ImageView v, int value) {
		v.setImageResource(value);
	}
	
	private void setViewImage(ImageView v, String value) {
		try {
            v.setImageResource(Integer.parseInt(value));
        } catch (NumberFormatException nfe) {
            v.setImageURI(Uri.parse(value));
        }
	}
	
	public void dataChanged(ArrayList<ImageTextAndAct> data){
		this.data = data;
//		notifyDataSetChanged();
		
		notifyDataSetInvalidated();
		
	}
	
}
