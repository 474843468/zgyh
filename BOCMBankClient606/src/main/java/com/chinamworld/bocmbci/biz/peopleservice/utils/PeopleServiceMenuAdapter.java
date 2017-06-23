package com.chinamworld.bocmbci.biz.peopleservice.utils;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
/**
 * 
 * @author wuhan
 *
 */
public class PeopleServiceMenuAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<Map<String,String>> menuList;
	
	public PeopleServiceMenuAdapter(Context context,
			ArrayList<Map<String,String>> list){
		this.setList(list);
		this.inflater = LayoutInflater.from(context);
	}
	
	private void setList(ArrayList<Map<String,String>> list){
		if(list!=null){
			this.menuList = list;
		}else{
			this.menuList = new ArrayList<Map<String,String>>();
		}
	}
	
	@Override
	public int getCount() {
		return menuList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return menuList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return menuList.get(arg0).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Map<String,String> map = menuList.get(position);
		if(convertView == null){
//			convertView = inflater.inflate(R.layout.peopleservice_item, null);//by dl
			convertView = inflater.inflate(R.layout.peopleservice_item_1, null);//by dl
			holder = new ViewHolder();
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(BTCCMWApplication.flowFileLangMap.get(map.get("name"))!=null&&!BTCCMWApplication.flowFileLangMap.get(map.get("name")).equals("")){
			holder.tvName.setText(BTCCMWApplication.flowFileLangMap.get(map.get("name")).toString());	
		}else{
			holder.tvName.setText(map.get("name"));		
		}
//		convertView.setTag(map.get("tag"));
//		holder.tvName.setText(name);
		return convertView;
	}


	class ViewHolder{
		TextView tvName;
	}
}
