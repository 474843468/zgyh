package com.chinamworld.bocmbci.biz.plps.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 *常用服务历史缴费记录
 * @author panwe
 *
 */
public class PlpsCommAdapter extends BaseAdapter{
	private Context mContext;
	private List<Map<String, Object>> mList;
	private String mKey1,mKey2,mKey3;
	private Count mCount;
	
	public PlpsCommAdapter(Context c,List<Map<String, Object>> list,String key1,String key2,String key3,Count count){
		this.mContext = c;
		this.mList = list;
		this.mKey1 = key1;
		this.mKey2 = key2;
		this.mKey3 = key3;
		this.mCount = count;
	}

	@Override
	public int getCount() {
		if (StringUtil.isNullOrEmpty(mList)) {
			return 0;
		}else{
			return mList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		if (StringUtil.isNullOrEmpty(mList)) {
			return null;
		}else{
			return mList.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHodler h;
		if (convertView == null) {
			h = new ViewHodler();
			convertView = View.inflate(mContext, R.layout.plps_simple_item, null);
			h.mTextView1 = (TextView) convertView.findViewById(R.id.text1);
			PlpsUtils.setOnShowAllTextListener(mContext, h.mTextView1);
			h.mTextView2 = (TextView) convertView.findViewById(R.id.text2);
			h.mTextView3 = (TextView) convertView.findViewById(R.id.text3);
			convertView.setTag(h);
		}else{
			h = (ViewHodler) convertView.getTag();
		}
		final Map<String, Object> map = mList.get(position);
		switch (mCount) {
		case ONE:
			h.mTextView1.setText((String)map.get(mKey1));
			h.mTextView2.setVisibility(View.GONE);
			h.mTextView3.setVisibility(View.GONE);
			break;
		case TWO:
			h.mTextView1.setText((String)map.get(mKey1));
			h.mTextView2.setText((String)map.get(mKey2));
//			h.mTextView3.setVisibility(View.GONE);
			h.mTextView3.setText((String)map.get(mKey3));
			break;
		case THREE:
			h.mTextView1.setText((String)map.get(mKey1));
			h.mTextView2.setText((String)map.get(mKey2));
//			h.mTextView3.setText((String)map.get(mKey3));
			h.mTextView3.setText(PlpsDataCenter.planStatus.get(map.get(mKey3)));
			break;
		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext,h.mTextView2);
		return convertView;
	}
	
	public void setData(List<Map<String, Object>> list) {
		this.mList = list;
		notifyDataSetChanged();
	}
	
	public enum Count{
		ONE,TWO,THREE;
	}
	
	public class ViewHodler {
		public TextView mTextView1;
		public TextView mTextView2;
		public TextView mTextView3;
	}
}
