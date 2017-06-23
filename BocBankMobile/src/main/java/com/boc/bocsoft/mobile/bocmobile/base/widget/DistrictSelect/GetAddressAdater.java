package com.boc.bocsoft.mobile.bocmobile.base.widget.DistrictSelect;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * 地址数据适配器
 * Created by xintong on 2016/6/4.
 */
public class GetAddressAdater extends BaseAdapter {

	private Context mContext;
	//填充视图
	private LayoutInflater mInflater;
	//视图缓存
	private ViewHolder holder;
	//数据列表
	private List<String> list;
	//标记选中条目
	private int selectPosition;

	public GetAddressAdater(Context context, List<String> list) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.list = list;
		selectPosition =-1;
	}
	

	public List<String> getList() {
		return list;
	}


	public int getSelectPosition() {
		return selectPosition;
	}

	public void setSelectPosition(int selectPosition) {
		this.selectPosition = selectPosition;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		if (convertView == null) {
			mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.boc_districtselect_listitem, parent, false);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.selected = (ImageView) convertView.findViewById(R.id.selected);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(list!=null){
			if(selectPosition ==-1){
				String name = list.get(position).toString();
				holder.name.setTextColor(Color.BLACK);
				holder.name.setText(name);
				holder.selected.setVisibility(View.INVISIBLE);
				convertView.setBackgroundColor(Color.WHITE);
			}else{
				if(selectPosition ==position){
					String name = list.get(position).toString();
					holder.name.setTextColor(Color.RED);
					holder.name.setText(name);
					holder.selected.setVisibility(View.INVISIBLE);
					convertView.setBackgroundColor(Color.LTGRAY);
				}else{
					String name = list.get(position).toString();
					holder.name.setTextColor(Color.BLACK);
					holder.name.setText(name);
					holder.selected.setVisibility(View.INVISIBLE);
					convertView.setBackgroundColor(Color.WHITE);
				}
			}
			
		}
		
		return convertView;
	}


	private static class ViewHolder {
		TextView name;
		ImageView selected;
	
	}
	

}
