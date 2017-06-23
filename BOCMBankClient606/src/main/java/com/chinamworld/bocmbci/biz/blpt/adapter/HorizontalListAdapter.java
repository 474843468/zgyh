package com.chinamworld.bocmbci.biz.blpt.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;

/**
 * 已申请服务横向列表
 * @author panwe
 *
 */
public class HorizontalListAdapter extends BaseAdapter{

	private Context mContext;
	private List<Map<String, Object>> mList;
	
	public HorizontalListAdapter(Context cn,List<Map<String, Object>> list){
		this.mContext = cn;
		this.mList = list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mList != null && mList.size() > 0) {
			return mList.size();
		}else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (mList != null && mList.size() > 0) {
			return mList.get(position);
		}else{
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHodler h;
		if (convertView == null) {
			h = new ViewHodler();
			convertView = LinearLayout.inflate(mContext, R.layout.blpt_hadapplied_horizontal_item, null);
			h.tvPtionName = (TextView)convertView.findViewById(R.id.tv_company);
			h.tvDisName = (TextView)convertView.findViewById(R.id.tv_dispname);
			convertView.setTag(h);
		}else{
			h = (ViewHodler) convertView.getTag();
		}
		Map<String, Object> map = (Map<String, Object>) mList.get(position);
//		h.tvPtionName.setText((String)(map.get(Blpt.BILL_COMMONPTIONNAME)));
//		h.tvDisName.setText((String)(map.get(Blpt.BILL_COMMON_DISPNAME)));
		h.tvPtionName.setText((String)map.get("test1"));
		h.tvDisName.setText((String)map.get("test2"));
		return convertView;
	}
	
	public class ViewHodler {
		public TextView tvPtionName;
		public TextView tvDisName;
	}

}
