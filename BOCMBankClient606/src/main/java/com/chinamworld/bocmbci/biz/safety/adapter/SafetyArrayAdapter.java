package com.chinamworld.bocmbci.biz.safety.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Safety;

public class SafetyArrayAdapter extends BaseAdapter{
	
	private List<Map<String, Object>> mList;
	private Context mContext;
	
	public SafetyArrayAdapter (Context c,List<Map<String, Object>> list){
		this.mList = list;
		this.mContext = c;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
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
			convertView = LinearLayout.inflate(mContext,
					R.layout.safety_spinner_item, null);
			h.text = (TextView) convertView
					.findViewById(R.id.text);
			convertView.setTag(h);
		} else {
			h = (ViewHodler) convertView.getTag();
		}
		Map<String, Object> map = mList.get(position);
		h.text.setText((String) (map.get(Safety.NAME)));
		return convertView;
	}

	public void setData(List<Map<String, Object>> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	public class ViewHodler {
		public TextView text;
	}
}
