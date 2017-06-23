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
 * 保险暂存产品适配器
 * 
 * @author panwe
 * 
 */
public class SafetyProductTempAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> mList;

	public SafetyProductTempAdapter(Context cn, List<Map<String, Object>> list) {
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
			convertView = LinearLayout.inflate(mContext,
					R.layout.safety_producttemp_item, null);
			h.tempName = (TextView) convertView
					.findViewById(R.id.insurtempName);
			h.company = (TextView) convertView
					.findViewById(R.id.company);
			h.riskName = (TextView) convertView
					.findViewById(R.id.insurancename);
			h.riskType = (TextView) convertView
					.findViewById(R.id.insurancetype);
			h.tempDate = (TextView) convertView
					.findViewById(R.id.insurtempdate);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext, h.tempName);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext, h.company);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext, h.riskName);
			convertView.setTag(h);
		} else {
			h = (ViewHodler) convertView.getTag();
		}
		Map<String, Object> map = mList.get(position);
		if (StringUtil.isNull((String) map.get(Safety.RISKTYPE))) {
			h.tempName.setText((String) (map.get(Safety.ALIAS_ID)));
			h.company.setText((String) (map.get(Safety.INSURANCE_COMANY)));
			h.riskType.setText(SafetyDataCenter.insurTrantyperp.get((map.get(Safety.TRANTYPE_FLAG))));
			if (StringUtil.isNull((String) map.get(Safety.MODIFYDATE))) {
				h.tempDate.setText((String) (map.get(Safety.SAVE_DATE)));
			} else {
				h.tempDate.setText((String) (map.get(Safety.MODIFYDATE)));
			}
		} else {
			h.tempName.setText((String) (map.get(Safety.POLICYNAME)));
			h.company.setText((String) (map.get(Safety.INSURCOMPANY)));
			h.riskType.setText(SafetyDataCenter.insuranceType.get((map.get(Safety.RISKTYPE))));
			h.tempDate.setText((String) (map.get(Safety.RECEIVINGDATE)));
		}
		h.riskName.setText((String) (map.get(Safety.RISKNAME)));
		return convertView;
	}

	public void setData(List<Map<String, Object>> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	public class ViewHodler {
		public TextView company;
		public TextView riskName;
		public TextView riskType;
		public TextView tempName;
		public TextView tempDate;
	}
}
