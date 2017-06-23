package com.chinamworld.bocmbci.biz.crcd.adapter;

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
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

public class CrcdSetupAdapter extends BaseAdapter {

	private List<Map<String, Object>> bankAccountList;

	private Context context;

	private int selectedPosition;

	public int getSelectedPosition() {
		return selectedPosition;
	}

	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
		notifyDataSetChanged();
	}

	public CrcdSetupAdapter(Context c, List<Map<String, Object>> bankAccountList, int position) {
		this.context = c;
		this.bankAccountList = bankAccountList;
		this.selectedPosition = position;
	}

	@Override
	public int getCount() {
		return bankAccountList.size();
	}

	@Override
	public Object getItem(int position) {
		return bankAccountList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CrcdViewHolder holder;
		if (convertView == null) {
			// 原用布局crcd_setup_list_item
			convertView = LayoutInflater.from(context).inflate(R.layout.forex_acc_setting_item, null);
			holder = new CrcdViewHolder();
			holder.forexStyle = (TextView) convertView.findViewById(R.id.forex_acc_type);
			holder.forexAlias = (TextView) convertView.findViewById(R.id.forex_acc_alias);
			holder.forexCountNum = (TextView) convertView.findViewById(R.id.forex_acc_accountnumber);
			holder.img = (ImageView) convertView.findViewById(R.id.imageViewright);
			convertView.setTag(holder);
		} else {
			holder = (CrcdViewHolder) convertView.getTag();
		}
		holder.forexStyle.setTag(position);
		holder.forexAlias.setTag(position);
		holder.forexCountNum.setTag(position);
		holder.img.setTag(position);

		Map<String, Object> map = bankAccountList.get(position);
		// 选中的颜色
		if (position == selectedPosition) {
			convertView.setBackgroundResource(R.drawable.bg_for_listview_item_half_black);
			holder.img.setVisibility(View.VISIBLE);
		} else {
			convertView.setBackgroundResource(R.drawable.bg_for_listview_item_write);
			holder.img.setVisibility(View.GONE);
		}
		// 账户类型
		String accountType = (String) map.get(Crcd.CRCD_ACCOUNTTYPE_RES);
		String accountTypeTrans = null;
		if (!StringUtil.isNull(accountType) && LocalData.AccountType.containsKey(accountType)) {
			accountTypeTrans = LocalData.AccountType.get(accountType);
		}
		// 账户别名
		String nickName = (String) map.get(Crcd.CRCD_NICKNAME_RES);
		// 账号
		String accountNumber = (String) map.get(Crcd.CRCD_ACCOUNTNUMBER_RES);
		String number = null;
		if (!StringUtil.isNull(accountNumber)) {
			number = StringUtil.getForSixForString(accountNumber);
		}
		holder.forexStyle.setText(accountTypeTrans);
		holder.forexAlias.setText(nickName);
		holder.forexCountNum.setText(number);

		return convertView;
	}

	class CrcdViewHolder {
		/**
		 * 账户类型
		 */
		public TextView forexStyle;
		/**
		 * 账户别名
		 */
		public TextView forexAlias;
		/**
		 * 账户号码
		 */
		public TextView forexCountNum;

		public ImageView img;

	}

}
