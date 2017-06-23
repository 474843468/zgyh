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
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 保险产品适配器
 * 
 * @author panwe
 * 
 */
public class LifeInsuranceProductAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> mList;

	public LifeInsuranceProductAdapter(Context cn, List<Map<String, Object>> list) {
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
		LifeViewHodler lvh;
		if (convertView == null) {
			lvh = new LifeViewHodler();
			convertView = LinearLayout.inflate(mContext, R.layout.safety_life_product_item, null);
			lvh.insuCode = (TextView) convertView.findViewById(R.id.tv_insuId);
			lvh.riskName = (TextView) convertView.findViewById(R.id.insurancename);
			lvh.riskTypeName = (TextView) convertView.findViewById(R.id.insurancetype);
			convertView.setTag(lvh);
		} else {
			lvh = (LifeViewHodler) convertView.getTag();
		}
		lvh.insuCode.setText((String) map.get(Safety.INSUCODE));
		lvh.riskName.setText((String) map.get(Safety.RISKNAME));
//		lvh.riskTypeName.setText((String) map.get(Safety.RISKTYPENAME));
		lvh.riskTypeName.setText((String)(map.get("productAttr")));// 606 此处改为产品属性
		PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext, lvh.riskName);
		return convertView;
	}

	public void setData(List<Map<String, Object>> list) {
		this.mList = list;
		notifyDataSetChanged();
	}
	
	private class LifeViewHodler {
		public TextView insuCode;
		public TextView riskName;
		public TextView riskTypeName;
	}
}
