package com.chinamworld.bocmbci.biz.safety.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 持有保单列表适配器
 * 
 * @author fsm
 * 
 */
public class SafetyHoldQueryListAdapter extends BaseAdapter {
	/** 账户列表信息 */
	private List<Map<String, Object>> list;
	private Context context;
	/** 选中项记录 */
	private int selectedPosition = -1;

	public SafetyHoldQueryListAdapter(Context context, List<Map<String, Object>> list) {
		this.context = context;
		this.list = list;
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
		final Map<String, Object> map = list.get(position);
		ViewHolder vh;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.safety_hold_query_list_item, null);
			
			vh = new ViewHolder();
			vh.tvSafetyName = (TextView) convertView.findViewById(R.id.tv_safetyName);
			vh.tvBuyDate = (TextView) convertView.findViewById(R.id.tv_buyDate);
			vh.tvAmount = (TextView) convertView.findViewById(R.id.tv_amount);
			
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		
		vh.tvSafetyName.setText((String) map.get(Safety.SAFETY_HOLD_RISK_NAME));
		// 交易日期字段在P503批次接口文档取消，改为显示保单生效日期
		vh.tvBuyDate.setText((String) map.get(Safety.SAFETY_HOLD_POL_EFF_DATE));
		vh.tvAmount.setText(StringUtil.parseStringPattern((String) map.get(Safety.SAFETY_HOLD_RISK_PREM), 2));
		
//		/** 保单号   */
//		TextView productBillCode = (TextView) convertView.findViewById(R.id.product_billcode);
//		/** 产品名称 */
//		TextView product_name = (TextView) convertView.findViewById(R.id.product_name);
		/** 生效日期 */
//		TextView effe_date = (TextView) convertView.findViewById(R.id.effe_date);
//		/** 终止日期 */
//		TextView end_date = (TextView) convertView.findViewById(R.id.end_date);
//		final String mProBillCode = (String) map.get(Safety.SAFETY_HOLD_POLICY_NO);
//		final String mProNameStr = (String) map.get(Safety.SAFETY_HOLD_RISK_NAME);
//		final String mProStartDateStr = (String) map.get(Safety.SAFETY_HOLD_POL_EFF_DATE);
//		final String mProEndDateStr = (String) map.get(Safety.SAFETY_HOLD_POL_END_DATE);
//		productBillCode.setText(StringUtil.valueOf1(mProBillCode));
//		product_name.setText(StringUtil.valueOf1(mProNameStr));
//		effe_date.setText(StringUtil.valueOf1(mProStartDateStr));
//		end_date.setText(StringUtil.valueOf1(mProEndDateStr));
//		
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, productBillCode);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, product_name);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, effe_date);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, end_date);
		return convertView;
	}
	
	class ViewHolder{
		/** 保险名称 */
		TextView tvSafetyName;
		/** 交易日期 */
		TextView tvBuyDate;
		/** 保费 */
		TextView tvAmount;
	}

	/**
	 * 给被选中的某一项设置高亮背景
	 * 
	 * @param selectedPosition
	 */
	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
		notifyDataSetChanged();
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

}
