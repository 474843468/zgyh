package com.chinamworld.bocmbci.biz.drawmoney.remitout;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;

/**
 * @ClassName: ProvinceListAdapter
 * @Description: 省份列表的adapter
 * @author JiangWei
 * @date 2013-7-30 上午11:06:30
 */
public class ProvinceListAdapter extends BaseAdapter {
	private Context mContext;
	private List<String> mList;

	public ProvinceListAdapter(Context context, List<String> list) {
		mContext = context;
		mList = list;
	}
	
	public void setData(List<String> list){
		mList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.blpt_province_item, null);
			viewHolder = new ViewHolder();
			viewHolder.provinceStr = (TextView) convertView
					.findViewById(R.id.blpt_province_name);
			viewHolder.rightArrow = (ImageView) convertView
					.findViewById(R.id.right_arrow);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.rightArrow.setVisibility(View.VISIBLE);
		viewHolder.provinceStr.setText(mList.get(position));
		return convertView;
	}

	class ViewHolder {
		TextView provinceStr;
		ImageView rightArrow;
	}

}
