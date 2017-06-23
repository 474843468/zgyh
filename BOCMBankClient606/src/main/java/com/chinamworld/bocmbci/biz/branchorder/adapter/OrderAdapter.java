package com.chinamworld.bocmbci.biz.branchorder.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Order;
import com.chinamworld.bocmbci.biz.branchorder.BranchOrderUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 网点适配器
 * 
 * @author panwe
 * 
 */
public class OrderAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> mList;

	public OrderAdapter(Context cn, List<Map<String, Object>> list) {
		this.mContext = cn;
		this.mList = list;
	}

	@Override
	public int getCount() {
		if (!StringUtil.isNullOrEmpty(mList)) {
			return mList.size();
		} 
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (!StringUtil.isNullOrEmpty(mList)) {
			return mList.get(position);
		} 
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHodler h;
		if (convertView == null) {
			h = new ViewHodler();
			convertView = View.inflate(mContext, R.layout.order_main_list_item, null);
			h.orderName = (TextView) convertView.findViewById(R.id.orderName);
			h.orderAdress = (TextView) convertView.findViewById(R.id.orderAdress);
			h.orderMobile = (TextView) convertView.findViewById(R.id.orderMobile);
			h.icon = (ImageView) convertView.findViewById(R.id.right_arrow);
			convertView.setTag(h);
		} else {
			h = (ViewHodler) convertView.getTag();
		}
		Map<String, Object> map = mList.get(position);
		h.orderName.setText((String) (map.get(Order.ORDERORGNAME)));
		h.orderAdress.setText((String) (map.get(Order.ORDERORGADRESS)));
		h.orderMobile.setText((String) (map.get(Order.ORDERORGPHONE)));
		BranchOrderUtils.setOnShowAllTextListener(mContext, h.orderName, h.orderAdress);
		return convertView;
	}
	
	public void clearDatas(){
		this.mList = new ArrayList<Map<String, Object>>();
		notifyDataSetChanged();
	}
	
	public class ViewHodler {
		public TextView orderName;
		public TextView orderAdress;
		public TextView orderMobile;
		public FrameLayout layout;
		public ImageView icon;
	}
}
