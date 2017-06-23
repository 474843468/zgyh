package com.chinamworld.bocmbci.biz.tran.remit.adapters;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @Title: SelectBaseAdapter.java
 */

public class RemitValidAccountAdapter extends BaseAdapter {

	private int selectPosition = -1;

	private Context context = null;
	private List<Map<String, String>> list = null;

	public RemitValidAccountAdapter(Context context, List<Map<String, String>> list) {
		this.context = context;
		this.list = list;
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
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.tran_remit_query_validaccount_item, null);
			holder = new ViewHolder();
//			holder.item =   convertView.findViewById(R.id.item);
			holder.account = (TextView) convertView.findViewById(R.id.tv_account);
			holder.status = (TextView) convertView.findViewById(R.id.tv_status);
			holder.custName = (TextView) convertView.findViewById(R.id.tv_custName);
			holder.balance = (TextView) convertView.findViewById(R.id.tv_balance);
//			holder.imageViewright = convertView.findViewById(R.id.imageViewright);
//			holder.boci_gotoDetail = (ImageView) convertView.findViewById(R.id.boci_gotoDetail);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String, String> map = list.get(position);
		/** 共享账户/卡号*/
		String shareCardNo = map.get("shareCardNo");
		// 将卡号格式化 4 6 4型
//		String shareCard = StringUtil.getForSixForString(shareCardNo);
		/** 账号类型     M-主账号(显示:签约账户)  S-共享账号(显示:共享账户)*/
		String remitSetMealAccountType = map.get("remitSetMealAccountType");
		/** 共享状态*/
		String shareStatus = map.get("shareStatus");
		/** 户主姓名*/
		String remitSetMealCustName = map.get("remitSetMealCustName");
		/** 已使用笔数*/
		String remitSetMealActBalance = map.get("remitSetMealActBalance");

		holder.account.setText(StringUtil.getForSixForString(shareCardNo));
		if ("M".equalsIgnoreCase(remitSetMealAccountType)) {
			holder.status.setText("签约账户");
		}else{
			holder.status.setText("Y".equalsIgnoreCase(shareStatus) ? "共享账号"+"/"+"有效" : "共享账号"+"/"+"无效");
		}
		holder.custName.setText(remitSetMealCustName);
		holder.balance.setText(remitSetMealActBalance);

//		if (getSelectPosition() == position) {
//			convertView.setBackgroundResource(R.drawable.bg_for_listview_item_half_black);
//			holder.imageViewright.setVisibility(View.VISIBLE);
//		} else {
//			convertView.setBackgroundResource(R.drawable.bg_for_listview_item_write);
//			holder.imageViewright.setVisibility(View.INVISIBLE);
//		}

//		holder.item.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (getSelectPosition() != position) {
//					setSelectPosition(position);
//					notifyDataSetChanged();
//				} 
//			}
//		});
		return convertView;
	}

	private class ViewHolder {
//		private View item = null;
		private TextView account = null;
		private TextView custName = null;
		private TextView status = null;
		private TextView balance = null;
//		private View imageViewright = null;
//		private ImageView boci_gotoDetail = null;
	}

	public int getSelectPosition() {
		return selectPosition;
	}

	public void setSelectPosition(int selectPosition) {
		this.selectPosition = selectPosition;
	}

	public boolean isSelected() {
		return getSelectPosition() >= 0;
	}

}
