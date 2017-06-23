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
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 保险产品适配器
 * 
 * @author panwe
 * 
 */
public class InsuranceProductAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> mList;

	public InsuranceProductAdapter(Context cn, List<Map<String, Object>> list) {
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
		Map<String, Object> map = mList.get(position);
		ViewHodler h;
		if (convertView == null) {
			h = new ViewHodler();
			convertView = LinearLayout.inflate(mContext, R.layout.safety_product_item, null);
			h.riskName = (TextView) convertView.findViewById(R.id.insurancename);
			h.riskType = (TextView) convertView.findViewById(R.id.insurancetype);
			h.riskPrem = (TextView) convertView.findViewById(R.id.insuranceprem);
			convertView.setTag(h);
		} else {
			h = (ViewHodler) convertView.getTag();
		}
		
		h.riskName.setText((String) (map.get(Safety.RISKNAME)));
		h.riskType.setText(SafetyDataCenter.insuranceType.get(map.get(Safety.RISKTYPE)));
		if (!StringUtil.isNull((String) map.get(Safety.QUOTAPREM))) {
			h.riskPrem.setText(StringUtil.parseStringPattern((String) (map.get(Safety.QUOTAPREM)), 2));
		} else if (!StringUtil.isNull((String) map.get(Safety.RISKPAEM))) {
			h.riskPrem.setText(StringUtil.parseStringPattern((String) (map.get(Safety.RISKPAEM)), 2));
		} else {
			h.riskPrem.setText("-");
		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext, h.riskName);
		return convertView;
	}

	public void setData(List<Map<String, Object>> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	private class ViewHodler {
		public TextView riskName;
		public TextView riskType;
		public TextView riskPrem;
	}
}
