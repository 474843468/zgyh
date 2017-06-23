package com.chinamworld.bocmbci.biz.remittance.adapter;

/**
 * 汇款币种的Adapter
 */
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.utils.StringUtil;

@SuppressLint("ResourceAsColor")
public class RemittanceCurrencyAdapter extends BaseAdapter {
	
	public static final String TAG = "RemittanceCurrency";
	
	/** 上下文 */
	private Context mContext;
	/** 数据源 */
	private List<String> mList;
	private boolean isDefault = true;
	/** 选择的监听事件 */
	private OnItemClickListener onItemClickListener;
	
	public RemittanceCurrencyAdapter(Context context, List<String> list) {
		if(StringUtil.isNullOrEmpty(list)) {
			list = new ArrayList<String>();
			list.add("");
			setDefault(false);
		}
		if(list.size() == 1){
			setDefault(false);
		}
		this.mContext = context;
		this.mList = list;
	}
	@Override
	public int getCount() {
		if(mList == null){
			return 0;
		} 
		
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		if(mList == null) {
			return null;
		} 
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.spinner_item, null);
		}
		TextView currencyTv = (TextView) convertView.findViewById(android.R.id.text1);
		currencyTv.setText(mList.get(position));
		if(position == 0) {
			currencyTv.setTextColor(R.color.gray_6c);
		}
		
		return convertView;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.simple_spinner_item_not_single_line, null);
			viewHolder = new ViewHolder();
			viewHolder.textView = (TextView) convertView.findViewById(R.id.text1);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.textView.setText(mList.get(position));
		final int mPosition = position;
		convertView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				notifyDataSetChanged();
				if(onItemClickListener != null) {
					onItemClickListener.onItemClick(null, null, mPosition, 0);
				}
				return false;
			}
		});
		return convertView;
	}
	class ViewHolder{
		TextView textView;
	}
	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}
	public boolean isDefault() {
		return isDefault;
	}
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
}
