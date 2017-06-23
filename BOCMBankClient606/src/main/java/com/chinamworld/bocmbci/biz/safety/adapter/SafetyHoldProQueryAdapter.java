package com.chinamworld.bocmbci.biz.safety.adapter;

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
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 保险 持有保单列表页
 * 
 * @author fsm
 * 
 */
public class SafetyHoldProQueryAdapter extends BaseAdapter {
//	private final String TAG = "SatetyHoldProQueryAdapter";
	private Context context;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> list;

	public SafetyHoldProQueryAdapter(Context context, List<Map<String, Object>> list) {
		this.context = context;
		this.list = list;
		mInflater = LayoutInflater.from(context);
	}
	
	public void setData(List<Map<String, Object>> list){
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		final Map<String, Object> map=list.get(position);
		if(convertView == null){
		   convertView = mInflater.inflate(R.layout.finc_listheader,null);
		   holder = new ViewHolder();
		   ((ImageView)convertView.findViewById(R.id.list_header_right))
		   		.setVisibility(View.VISIBLE);
		   holder.mProName=(TextView) convertView.findViewById(R.id.finc_listiterm_tv1);
		   holder.mProStartDate=(TextView) convertView.findViewById(R.id.finc_listiterm_tv2);
		   holder.mProEndDate=(TextView) convertView.findViewById(R.id.finc_listiterm_tv3);
		   convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		final String mProNameStr = (String) map.get(Safety.SAFETY_HOLD_RISK_NAME);
		final String mProStartDateStr = (String) map.get(Safety.SAFETY_HOLD_POL_EFF_DATE);
		final String mProEndDateStr = (String) map.get(Safety.SAFETY_HOLD_POL_END_DATE);
		holder.mProName.setText(StringUtil.valueOf1(mProNameStr));
		holder.mProStartDate.setText(StringUtil.valueOf1(mProStartDateStr));
		holder.mProEndDate.setText(StringUtil.valueOf1(mProEndDateStr));
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.mProName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.mProStartDate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.mProEndDate);
		return convertView;
	}

	/**
	 * 内部类--存放控件
	 */
	public final class ViewHolder {
		/** 产品名称， 保单生效日期， 保单终止日期*/
		private TextView mProName;
		private TextView mProStartDate;
		private TextView mProEndDate;
	}

}
