package com.chinamworld.bocmbci.biz.epay.myPayService.bomobile.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;

public class MenuListAdapter extends BaseAdapter {
	
	private String tag = "EPayMenuListAdapter";
	//菜单列表
	private List<Map<String, Object>> menuList;
	//当前activity
	private Context context;
	
	public MenuListAdapter(Context context, List<Map<String, Object>> menuList) {
		this.context = context;
		this.menuList = menuList;
	}
	
	@Override
	public int getCount() {
		return menuList.size();
	}

	@Override
	public Object getItem(int position) {
		return menuList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.epay_menu_list_item, null);
			holder.rl_item = (RelativeLayout)convertView.findViewById(R.id.epay_menu_rl_list_item);
			holder.tv_menu_name = (TextView)convertView.findViewById(R.id.epay_menu_item_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		holder.tv_menu_name.setText(EpayUtil.getString(menuList.get(position).get(PubConstants.MAIN_FIELD_MENU_NAME), ""));
		holder.rl_item.setOnClickListener((View.OnClickListener)menuList.get(position).get(PubConstants.MAIN_FIELD_MENU_ITEM_ONCLICK_LISTENER));
		return convertView;
	}
	
	class ViewHolder {
		TextView tv_menu_name;
		TextView tv_menu_button;
		RelativeLayout rl_item;
	}

}
