package com.chinamworld.bocmbci.biz.acc.adapter;

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
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 新建签约账户选择list适配器
 *   可进行单项选择
 * @author wangmengmeng
 *
 */
public class FinanceIcSignChooseAdapter extends BaseAdapter {
	/** 账户列表信息 */
	private List<Map<String, String>> bankAccountList;
	private Context context;
	/** 选中项记录 */
	private int selectedPosition=-1;


	public FinanceIcSignChooseAdapter(Context context,
			List<Map<String, String>> bankAccountList) {
		this.context = context;
		this.bankAccountList = bankAccountList;
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

	/**
	 * 给被选中的某一项设置高亮背景
	 */
	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
		 notifyDataSetChanged();

	}
	/**
	 * @return the selectedPosition
	 */
	public int getSelectedPosition() {
		return selectedPosition;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.acc_financeic_icsign_list_item, null);
		}
		/** 账户类型 */
		TextView tv_acc_type_value = (TextView) convertView
				.findViewById(R.id.acc_type_value);
		String acc_type=bankAccountList.get(
				position).get(Acc.ACC_ACCOUNTTYPE_RES);
		tv_acc_type_value.setText(LocalData.AccountType.get(acc_type.trim()));
		/** 账户别名 */
		TextView tv_acc_account_nickname = (TextView) convertView
				.findViewById(R.id.acc_account_nickname);
		tv_acc_account_nickname.setText(bankAccountList.get(
				position).get(Acc.ACC_NICKNAME_RES));
		/** 账户账号 */
		TextView tv_acc_account_num = (TextView) convertView
				.findViewById(R.id.acc_account_num);
		String acc_account_num=bankAccountList.get(
				position).get(Acc.ACC_ACCOUNTNUMBER_RES);
		tv_acc_account_num.setText(StringUtil.getForSixForString(acc_account_num));
		ImageView checked =(ImageView) convertView.findViewById(R.id.imageViewright);
		if (position == selectedPosition) {
			checked.setVisibility(View.VISIBLE);
			convertView.setBackgroundResource(R.drawable.bg_for_listview_item_half_black);
		} else {
			checked.setVisibility(View.GONE);
			convertView.setBackgroundResource(R.drawable.bg_for_listview_item_write);
		}
		return convertView;
	}
}
