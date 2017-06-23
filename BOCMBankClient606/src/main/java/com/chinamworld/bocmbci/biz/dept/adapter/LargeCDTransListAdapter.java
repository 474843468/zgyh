package com.chinamworld.bocmbci.biz.dept.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @description 查询已购买大额存单交易明细Adapter
 * @author liuh
 */
public class LargeCDTransListAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> list = null;

	public LargeCDTransListAdapter(Context context, List<Map<String, Object>> list) {
		this.mContext = context;
		this.list = list;

	}

	public void changeData(List<Map<String, Object>> list) {
		if (list == null) {
			this.list = new ArrayList<Map<String, Object>>();
		} else {
			this.list = list;
		}
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.large_cd_purchased_trans_list_item, null);
			holder.transDateTv = (TextView) convertView.findViewById(R.id.large_cd_trans_date_tv);
			holder.serialTypeTv = (TextView) convertView.findViewById(R.id.tv_large_cd_tran_type);
			holder.transAmountTv = (TextView) convertView.findViewById(R.id.tv_large_cd_tran_transAmount);
			holder.serialNumberTv = (TextView) convertView.findViewById(R.id.tv_large_cd_serial_number);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext, holder.transAmountTv);
			holder.amountTv = (TextView) convertView.findViewById(R.id.tv_large_cd_tran_amount);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext, holder.amountTv);
			holder.incomeTv = (TextView) convertView.findViewById(R.id.tv_large_cd_tran_income);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext, holder.incomeTv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		// add by 2016年3月11日  luqp 修改adapter 数据
		Map<String, Object> maps = list.get(position);
		String transDate = StringUtil.valueOf1((String) maps.get(Dept.TRANS_DATE));
		String serialType = getSerialType(StringUtil.valueOf1((String) maps.get(Dept.TRANS_TYPE)));
		String transAmount = StringUtil.valueOf1((String) maps.get(Dept.TRANSAMOUNT));
		String amount = StringUtil.valueOf1((String) maps.get(Dept.TRANS_AMOUNT));
		String income = StringUtil.valueOf1((String) maps.get(Dept.TRANS_INCOME));
		String serialNumber = StringUtil.valueOf1((String) maps.get(Dept.Large_serialNumber));
		
		// add by luqp 流水号 2016-3-7
		holder.serialNumberTv.setText(serialNumber);
		holder.transDateTv.setText(transDate);
		holder.serialTypeTv.setText(serialType);
		holder.transAmountTv.setText("-".equals(transAmount) ? transAmount : StringUtil.parseStringPattern(
				transAmount, 2));
		holder.amountTv.setText("-".equals(amount) ? amount : StringUtil.parseStringPattern(amount, 2));
		holder.incomeTv.setText("-".equals(income) ? income : StringUtil.parseStringPattern(income, 2));

		// 2016年3月11日17:08:34 luqp 注释
//		if (holder != null) {
//			Object obj = getItem(position);
//			if (obj == null)
//				return null;
//			@SuppressWarnings("unchecked")
//			Map<String, Object> map = (Map<String, Object>) obj;
//			if (map != null) {
//				String transDate = StringUtil.valueOf1((String) map.get(Dept.TRANS_DATE));
//				String serialType = getSerialType(StringUtil.valueOf1((String) map.get(Dept.TRANS_TYPE)));
//				String transAmount = StringUtil.valueOf1((String) map.get(Dept.TRANSAMOUNT));
//				String amount = StringUtil.valueOf1((String) map.get(Dept.TRANS_AMOUNT));
//				String income = StringUtil.valueOf1((String) map.get(Dept.TRANS_INCOME));
//				String serialNumber = StringUtil.valueOf1((String) map.get(Dept.Large_serialNumber));
//				
//				// add by luqp 流水号 2016-3-7
//				holder.serialNumberTv.setText(serialNumber);
//				holder.transDateTv.setText(transDate);
//				holder.serialTypeTv.setText(serialType);
//				holder.transAmountTv.setText("-".equals(transAmount) ? transAmount : StringUtil.parseStringPattern(
//						transAmount, 2));
//				holder.amountTv.setText("-".equals(amount) ? amount : StringUtil.parseStringPattern(amount, 2));
//				holder.incomeTv.setText("-".equals(income) ? income : StringUtil.parseStringPattern(income, 2));
//			}
//		}
		return convertView;
	}

	/**
	 * 获取流水类型
	 * @param type 流水类型编号
	 * @return 流水类型
	 */
	private String getSerialType(String type) {
		String serialType = null;
		if (type.equals(ConstantGloble.SERIAL_TYPE_ONE)) { // 申购
			serialType = mContext.getString(R.string.large_cd_serialType_one);
		} else if (type.equals(ConstantGloble.SERIAL_TYPE_TWO)) { // 转让
			serialType = mContext.getString(R.string.large_cd_serialType_two);
		} else if (type.equals(ConstantGloble.SERIAL_TYPE_THREE)) { // 赎回
			serialType = mContext.getString(R.string.large_cd_serialType_three);
		} else if (type.equals(ConstantGloble.SERIAL_TYPE_FOUR)) { // 止付
			serialType = mContext.getString(R.string.large_cd_serialType_four);
		} else if (type.equals(ConstantGloble.SERIAL_TYPE_FIVE)) { // 赎回冲正
			serialType = mContext.getString(R.string.large_cd_serialType_five);
		} else if (type.equals(ConstantGloble.SERIAL_TYPE_SIX)) { // 解除止付
			serialType = mContext.getString(R.string.large_cd_serialType_six);
		} else if (type.equals(ConstantGloble.SERIAL_TYPE_SEVEN)) { // 部提
			serialType = mContext.getString(R.string.large_cd_serialType_seven);
		} else if (type.equals(ConstantGloble.SERIAL_TYPE_EIGHT)) { // 部提冲正
			serialType = mContext.getString(R.string.large_cd_serialType_eight);
		} else if (type.equals(ConstantGloble.SERIAL_TYPE_Nine)) { // 凭证配发
			serialType = mContext.getString(R.string.large_cd_serialType_Nine);
		} else if (type.equals(ConstantGloble.SERIAL_TYPE_Ten)) { // 存款证明开立
			serialType = mContext.getString(R.string.large_cd_serialType_Ten);
		} else if (type.equals(ConstantGloble.SERIAL_TYPE_Eleven)) { // 存款证明收回
			serialType = mContext.getString(R.string.large_cd_serialType_Eleven);
		} else {
			serialType = "-";
		}
		return serialType;
	}

	class ViewHolder {
		// 交易日期
		private TextView serialNumberTv;
		// 交易日期
		private TextView transDateTv;
		// 交易类型
		private TextView serialTypeTv;
		// 交易金额
		private TextView transAmountTv;
		// 存单金额
		private TextView amountTv;
		// 收益
		private TextView incomeTv;
	}
}