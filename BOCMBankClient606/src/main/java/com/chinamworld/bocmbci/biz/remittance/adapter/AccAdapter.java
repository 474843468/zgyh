package com.chinamworld.bocmbci.biz.remittance.adapter;
/**
 * 扣款账户的Adapter
 */
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.utils.StringUtil;

@SuppressLint("ResourceAsColor")
public class AccAdapter extends BaseAdapter {

	/** 数据源 */
	private List<String> mAccList;
	/** 上下文 */
	private Context mContext;
	/** 是否是手动触摸 */
	private boolean isTouch = false;
	
	public AccAdapter(Context context,List<String> list) {
		this.mContext = context;
		this.mAccList = list;
	}
	
	@Override
	public int getCount() {
		
		if(mAccList == null){
			return 0;
		} else {
			return mAccList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		if(mAccList == null){
			return null;
		}
		return mAccList.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.spinner_item, null);
			
		}
		TextView accTv = (TextView) convertView.findViewById(android.R.id.text1);
		accTv.setText(StringUtil.getForSixForString(mAccList.get(position)));
		if(position == 0){
			accTv.setTextColor(R.color.gray_6c);
		}
		return convertView;
		
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if(convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.simple_spinner_item_not_single_line, null);
			vh = new ViewHolder();
			vh.tv =(TextView) convertView.findViewById(R.id.text1);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		vh.tv.setText(StringUtil.getForSixForString(mAccList.get(position)));
        convertView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				isTouch = true;
				return false;
			}
		});
		return convertView;
	}
	class ViewHolder{
		TextView tv;
	}
	
	public boolean isTouch() {
		return isTouch;
	}

	public void setTouch(boolean isTouch) {
		this.isTouch = isTouch;
	}
}
