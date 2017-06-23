package com.chinamworld.bocmbci.biz.dept.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class InterestDetailAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> mList;
	private int type;

	public InterestDetailAdapter(Context cn, List<Map<String, Object>> list,
			int type) {
		this.mContext = cn;
		this.mList = list;
		this.type = type;
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
			convertView = View.inflate(mContext,
					R.layout.dept_interestdetail_item, null);
			h.holdTime = (TextView) convertView.findViewById(R.id.holddate);
			h.yield = (TextView) convertView.findViewById(R.id.yield);
			convertView.setTag(h);
		} else {
			h = (ViewHodler) convertView.getTag();
		}
		String unit;
		if (type == 15) {
			unit = "天";//(String) mList.get(0).get(Dept.flexibleInterest_gradeType);
		} else {
			unit = "元";
		}

		if (position == 0) {
			if (type == 15) {
				h.holdTime.setText(mList.get(position).get("gradeLimit") + unit
						+ "（以下）");
			} else {
				h.holdTime.setText(StringUtil.parseStringPattern((String)mList.get(position).get("gradeAmount"), 0)
						+ unit + "（以下）");
			}
			h.yield.setText("按活期利率");
		} else if (position == getCount()-1) {
			if (type == 15) {
				h.holdTime.setText("365"
						/*mList.get(position).get("gradeLimit")*/
						+ unit
						+ "（含）以上");
			} else {
				h.holdTime.setText(StringUtil.parseStringPattern("10000000", 0)
					/*	mList.get(position).get("gradeAmount")*/
						+ unit + "（含）以上");
			}
			h.yield.setText(StringUtil.append2Decimals(
					(String) mList.get(position).get(
							Dept.flexibleInterest_gradeValue), 2)
					+ "%");
//			h.yield.setText(
//					(String) mList.get(position).get(
//							Dept.flexibleInterest_gradeValue)
//					+ "%");
		} else {
			if (type == 15) {
				if(position+1== getCount()-1){
					h.holdTime
					.setText(mList.get(position ).get("gradeLimit")
							+ unit + "（含）" + "-"
							+ "365" + unit
							+ "（不含）");	
				}else{
					h.holdTime
					.setText(mList.get(position ).get("gradeLimit")
							+ unit + "（含）" + "-"
							+ mList.get(position+1).get("gradeLimit") + unit
							+ "（不含）");	
				}
				
			} else {
				if(position+1== getCount()-1){
					h.holdTime.setText(StringUtil.parseStringPattern((String)mList.get(position).get("gradeAmount"), 0)
							+ unit + "（含）" + "-"
							+ StringUtil.parseStringPattern("10000000", 0) + unit
							+ "（不含）");
				}else{
					h.holdTime.setText(StringUtil.parseStringPattern((String)mList.get(position).get("gradeAmount"), 0)
							+ unit + "（含）" + "-"
							+ StringUtil.parseStringPattern((String)mList.get(position+1).get("gradeAmount"), 0) + unit
							+ "（不含）");	
				}
				
			}
			h.yield.setText(StringUtil.append2Decimals(
					(String) mList.get(position).get(
							Dept.flexibleInterest_gradeValue), 2)
					+ "%");
//			h.yield.setText(
//					(String) mList.get(position).get(Dept.flexibleInterest_gradeValue)
//					+ "%");	
			
		}

		PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext,
				h.holdTime);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext,
				h.yield);
		return convertView;
	}

	public class ViewHodler {
		public TextView holdTime;
		public TextView yield;
	}
}
