package com.chinamworld.bocmbci.biz.bocinvt.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 收益累进适配器
 * 
 * @author panwe
 * 
 */
public class ProgressAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> mList;

	public ProgressAdapter(Context cn, List<Map<String, Object>> list) {
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
			convertView = View.inflate(mContext, R.layout.bocinvt_progression_item, null);
			h.holdTime = (TextView) convertView.findViewById(R.id.holddate);
			h.yield = (TextView) convertView.findViewById(R.id.yield);
			convertView.setTag(h);
		} else {
			h = (ViewHodler) convertView.getTag();
		}
		Map<String, Object> map = mList.get(position);
		h.holdTime.setText(getTime(map));
		h.yield.setText(StringUtil.append2Decimals((String)map.get(BocInvt.BOCI_YEARLYRR_RES), 2)+"%");
		PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext, h.holdTime);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext, h.yield);
		return convertView;
	}
	
	private String getTime(Map<String, Object> map){
		String minDays = (String) map.get(BocInvt.MIN_DAYS);
		String maxDays = (String) map.get(BocInvt.MAX_DAYS);
		if (!StringUtil.isNull(maxDays) && maxDays.equals("-1")) {
			return minDays+"天及以上";
		}
		return minDays+"-"+maxDays+"天";
	}

	public void setData(List<Map<String, Object>> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	public class ViewHodler {
		public TextView holdTime;
		public TextView yield;
	}
}
