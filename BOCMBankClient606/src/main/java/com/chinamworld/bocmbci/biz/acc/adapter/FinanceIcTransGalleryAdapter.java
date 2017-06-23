package com.chinamworld.bocmbci.biz.acc.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 账户管理账户明细橫滑
 * 
 */
public class FinanceIcTransGalleryAdapter extends BaseAdapter {
	private Context mContext;

	private List<Map<String, Object>> mList;

	public FinanceIcTransGalleryAdapter(Context context,
			List<Map<String, Object>> list) {
		mContext = context;
		mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.acc_financeic_horizentollist_item, null);
			viewHolder.tv_acc_type_value = (TextView) convertView
					.findViewById(R.id.tv_acc_financeic_actype);
			viewHolder.tv_acc_account_nickname = (TextView) convertView
					.findViewById(R.id.tv_acc_financeic_actnickname);
			viewHolder.tv_acc_account_num = (TextView) convertView
					.findViewById(R.id.tv_acc_financeic_actnum);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		String accounttype = String.valueOf(mList.get(position).get(
				Acc.ACC_ACCOUNTTYPE_RES));
		String acc_account_num = String.valueOf(mList.get(position).get(
				Acc.ACC_ACCOUNTNUMBER_RES));
		viewHolder.tv_acc_type_value.setText(LocalData.AccountType
				.get(accounttype));
		viewHolder.tv_acc_account_nickname.setText(String.valueOf(mList.get(
				position).get(Acc.ACC_NICKNAME_RES)));
		viewHolder.tv_acc_account_num.setText(StringUtil
				.getForSixForString(acc_account_num));
		return convertView;
	}

	private class ViewHolder {
		public TextView tv_acc_account_num;
		public TextView tv_acc_type_value;
		public TextView tv_acc_account_nickname;
	}

}
