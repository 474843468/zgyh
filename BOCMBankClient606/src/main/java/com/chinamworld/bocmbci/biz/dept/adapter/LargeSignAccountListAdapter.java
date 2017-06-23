package com.chinamworld.bocmbci.biz.dept.adapter;

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
 * 大额存单签约 账户列表Adapter
 * 
 * @author luqp 2016年1月8日17:16:38
 */
public class LargeSignAccountListAdapter extends BaseAdapter {
	private Context context;
	/** 账户列表信息 */
	private List<Map<String, Object>> bankAccountList;
	/** 选中项记录 */
	private int selectedPosition = -1;

	public LargeSignAccountListAdapter(Context context, List<Map<String, Object>> bankAccountList) {
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
	 * 
	 * @param selectedPosition
	 * @author nl.
	 */
	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.large_sign_account_list_item, null);
			/** 账户类型 */
			holder.tv_acc_type_value = (TextView) convertView.findViewById(R.id.acc_type_value);
			/** 账户别名 */
			holder.tv_acc_account_nickname = (TextView) convertView.findViewById(R.id.acc_account_nickname);
			/** 账户账号 */
			holder.tv_acc_account_num = (TextView) convertView.findViewById(R.id.acc_account_num);
			holder.checked = (ImageView) convertView.findViewById(R.id.imageViewright);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String acc_type = String.valueOf(bankAccountList.get(position).get(Acc.ACC_ACCOUNTTYPE_RES));
		holder.tv_acc_type_value.setText(LocalData.AccountType.get(acc_type.trim()));

		holder.tv_acc_account_nickname.setText(String.valueOf(bankAccountList.get(position).get(Acc.ACC_NICKNAME_RES)));

		String acc_account_num = String.valueOf(bankAccountList.get(position).get(Acc.ACC_ACCOUNTNUMBER_RES));
		holder.tv_acc_account_num.setText(StringUtil.getForSixForString(acc_account_num));

		if (position == selectedPosition) {
			holder.checked.setVisibility(View.VISIBLE);
			convertView.setBackgroundResource(R.drawable.bg_for_listview_item_half_black);
		} else {
			holder.checked.setVisibility(View.GONE);
			convertView.setBackgroundResource(R.drawable.bg_for_listview_item_write);
		}
		return convertView;
	}
	
	public class ViewHolder {
		public TextView tv_acc_type_value;
		public TextView tv_acc_account_nickname;
		public TextView tv_acc_account_num;
		public ImageView checked;
	}
}