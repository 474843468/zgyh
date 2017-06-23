package com.chinamworld.bocmbci.biz.bond.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Bond;
import com.chinamworld.bocmbci.biz.bond.BondDataCenter;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 债券行情ListView适配器
 * 
 * @author panwe
 *
 */
public class BondAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> mList;

	public BondAdapter(Context context, List<Map<String, Object>> list) {
		super();
		this.mContext = context;
		this.mList = list;
	}

	public void changeData(List<Map<String, Object>> list) {
		if (list != null) {
			this.mList = list;
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		if (mList != null && mList.size() > 0) {
			return mList.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		if (mList != null && mList.size() > 0) {
			return mList.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder h;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.bond_allbond_item, null);
			h = new ViewHolder();
			h.bondNameAndCode = (TextView) convertView
					.findViewById(R.id.tv_bond_name_and_code);
			h.bondRate = (TextView) convertView.findViewById(R.id.tv_bond_rate);
			h.bondDate = (TextView) convertView.findViewById(R.id.tv_bond_date);
			h.bondStatus = (TextView) convertView
					.findViewById(R.id.tv_bond_status);
//			h.bondLeftBg = (ImageView) convertView.findViewById(R.id.left_bg);
			convertView.setTag(h);
		} else {
			h = (ViewHolder) convertView.getTag();
		}
		Map<String, Object> dataMap = mList.get(position);

//		if (position % 2 == 0) {
//			h.bondLeftBg.setBackgroundResource(R.drawable.outlay_left_lan);
//		} else {
//			h.bondLeftBg.setBackgroundResource(R.drawable.outlay_left_skyblue);
//		}

		String bondNameStr = StringUtil.valueOf1((String) dataMap
				.get(Bond.BOND_SHORTNAME));
		String bondCodeStr = StringUtil.valueOf1((String) dataMap
				.get(Bond.BOND_CODE));
		StringBuffer sb = new StringBuffer();
		sb.append(bondNameStr);
		sb.append("(");
		sb.append(bondCodeStr);
		sb.append(")");
		h.bondNameAndCode.setText(sb.toString());

		// h.buyFullPrice.setText(StringUtil.parseStringPattern((String)
		// dataMap.get(Bond.BOND_BUYFILL_PRICE), 2));
		// h.sellFullPrice.setText(StringUtil.parseStringPattern((String)
		// dataMap.get(Bond.BOND_SELLFILL_PRICE),2));

		String bondDateStr = StringUtil.valueOf1((String) dataMap
				.get(Bond.BOND_BONDTERM));
		try{
			int date = Integer.parseInt(bondDateStr);
			bondDateStr = String.valueOf(date);
		}
		catch(Exception e){
			bondDateStr = "-";
		}
		
		
		if ("-".equals(bondDateStr)) {
			h.bondDate.setText(bondDateStr);
		} else {
			h.bondDate.setText(bondDateStr + "个月");
		}

		String bondRateStr = StringUtil.valueOf1((String) dataMap
				.get(Bond.BOND_INT));
		if ("-".equals(bondRateStr)) {
			h.bondRate.setText(bondRateStr);
		} else {
			h.bondRate.setText(StringUtil.parseStringPattern(bondRateStr, 2)
					+ "%");
		}

		String bondStatusStr = BondDataCenter.bondStatus.get(dataMap
				.get(Bond.BOND_STATUS));
		h.bondStatus.setText(bondStatusStr);


		PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext,
				h.bondNameAndCode);

		return convertView;
	}

	public final class ViewHolder {
		// public TextView bondCode;
		// public TextView bondName;
		// public TextView buyFullPrice;
		// public TextView sellFullPrice;
		public TextView bondNameAndCode;
		public TextView bondRate;
		public TextView bondDate;
		public TextView bondStatus;
//		public ImageView bondLeftBg;
	}
}
