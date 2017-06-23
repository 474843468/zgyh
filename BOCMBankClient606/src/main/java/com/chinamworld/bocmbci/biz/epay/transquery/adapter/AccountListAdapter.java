package com.chinamworld.bocmbci.biz.epay.transquery.adapter;

import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

public class AccountListAdapter extends BaseAdapter {

	private BaseActivity context;

	private int selectedPosition = -1;

	private List<Object> accountList;

	public AccountListAdapter(BaseActivity context, List<Object> accountList) {
		this.accountList = accountList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return accountList.size();
	}

	@Override
	public Object getItem(int position) {
		return accountList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.epay_tq_zy_account_list_item, null);
			holder.tv_acc_type = (TextView) convertView.findViewById(R.id.tv_acc_type);
			holder.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
			holder.tv_account = (TextView) convertView.findViewById(R.id.tv_account);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map<Object, Object> tempMap = EpayUtil.getMap(accountList.get(position));
		String accType = EpayUtil.getString(tempMap.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_TYPE), "");
		holder.tv_acc_type.setText(LocalData.AccountType.get(accType));
		holder.tv_nickname.setText(EpayUtil.getString(tempMap.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_NICKNAME), ""));
		holder.tv_account.setText(StringUtil.getForSixForString(EpayUtil.getString(tempMap.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NUMBER), "")));

		return convertView;
	}

	class ViewHolder {
		TextView tv_acc_type;
		TextView tv_nickname;
		TextView tv_account;
	}

	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
		notifyDataSetChanged();
	}

}
