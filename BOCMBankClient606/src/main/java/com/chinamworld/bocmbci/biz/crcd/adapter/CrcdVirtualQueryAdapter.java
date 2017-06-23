package com.chinamworld.bocmbci.biz.crcd.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 虚拟卡查询适配器 */
public class CrcdVirtualQueryAdapter extends BaseAdapter {
	private Context mContext;

	private List<Map<String, Object>> mList;

	public CrcdVirtualQueryAdapter(Context context, List<Map<String, Object>> list) {
		mContext = context;
		mList = list;
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.crcd_myaccount_detail_item, null);
			viewHolder.crcd_type_value = (TextView) convertView.findViewById(R.id.crcd_type_value);
			viewHolder.crcd_account_num = (TextView) convertView.findViewById(R.id.crcd_account_num);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.crcd_type_value.setText(mContext.getString(R.string.mycrcd_xuni_crcd));
		Map<String, Object> virCardItem = (Map<String, Object>) mList.get(position);
		String virtualCardNo = String.valueOf(virCardItem.get(Crcd.CRCD_VIRTUALCARDNO));
		viewHolder.crcd_account_num.setText(StringUtil.getForSixForString(virtualCardNo));
		return convertView;
	}

	private class ViewHolder {
		public TextView crcd_type_value;
		public TextView crcd_account_num;
		public TextView tv_acc_account_nickname;
	}
}
