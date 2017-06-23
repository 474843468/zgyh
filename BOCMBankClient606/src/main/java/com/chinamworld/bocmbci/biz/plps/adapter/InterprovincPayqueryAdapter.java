package com.chinamworld.bocmbci.biz.plps.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 跨省异地交通罚款
 * 缴纳查询记录Adapter
 * @author Administrator zxj*/
public class InterprovincPayqueryAdapter extends BaseAdapter{
	private Context mContext;
	private List<Map<String, Object>> mList;
	private String mKey1, mKey2, mKey3;
	
	public InterprovincPayqueryAdapter(Context c,List<Map<String, Object>> list, String key1, String key2, String key3) {
		// TODO Auto-generated constructor stub
		this.mContext = c;
		this.mList = list;
		this.mKey1 = key1;
		this.mKey2 = key2;
		this.mKey3 = key3;
	}
	@Override
	public int getCount() {
		if(StringUtil.isNullOrEmpty(mList)){
			return 0;
		}else {
			return mList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		if(StringUtil.isNullOrEmpty(mList)){
			return null;
		}else {
			return mList.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder h;
		if(convertView == null){
			h = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.plps_interprov_simple_item, null);
			h.mTextView1 = (TextView)convertView.findViewById(R.id.text1);
			h.mTextView2 = (TextView)convertView.findViewById(R.id.text2);
			h.mTextView3 = (TextView)convertView.findViewById(R.id.text3);
			PlpsUtils.setOnShowAllTextListener(mContext, h.mTextView1, h.mTextView2, h.mTextView3);
			convertView.setTag(h);
		}else {
			h = (ViewHolder)convertView.getTag();
		}
		final Map<String, Object> map = mList.get(position);
		h.mTextView1.setText((String)map.get(mKey1));
		h.mTextView2.setText((String)map.get(mKey2));
		h.mTextView3.setText(StringUtil.parseStringPattern((String)map.get(mKey3), 2));
		return convertView;
	}
	public void setData(List<Map<String, Object>> list){
		this.mList = list;
		notifyDataSetChanged();
	}
	public class ViewHolder{
		public TextView mTextView1;
		public TextView mTextView2;
		public TextView mTextView3;
	}

}
