package com.chinamworld.bocmbci.biz.safety.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.utils.StringUtil;

public class SafetyCountyAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> mList;

	public SafetyCountyAdapter(Context c, List<Map<String, Object>> list) {
		this.mContext = c;
		this.mList = list;
	}

	public void setData(List<Map<String, Object>> list){
		this.mList = list;
		notifyDataSetChanged();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.safety_county_choose_item, null);
			holder.textview = (TextView) convertView.findViewById(R.id.text);
			holder.checkbox = (CheckBox) convertView.findViewById(R.id.cb);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final Map<String, Object> map = mList.get(position);
		holder.textview.setText((String)map.get(Safety.NAME)+" ("+(String)map.get(Safety.CODE)+")");
		holder.checkbox.setChecked((Boolean)map.get(Safety.SELECT));
		return convertView;
	}
	
	public class ViewHolder{
		public TextView textview;
		public CheckBox checkbox;
	}

}
