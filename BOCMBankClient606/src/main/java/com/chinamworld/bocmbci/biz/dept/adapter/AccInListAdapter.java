package com.chinamworld.bocmbci.biz.dept.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

public class AccInListAdapter extends BaseAdapter {

	private Context context = null;

	private List<Map<String, Object>> accountInList = null;
	
//	private int padding;

	public AccInListAdapter(Context context,
			List<Map<String, Object>> accountInList) {
		this.context = context;
		this.accountInList = accountInList;
//		padding = context.getResources().getDimensionPixelSize(R.dimen.fill_margin_left) * 2;
	}

	@Override
	public int getCount() {
		return accountInList.size();
	}

	@Override
	public Object getItem(int position) {
		return accountInList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
				if (accountInList.get(position) == null) {
					return null;
				}
				convertView = LayoutInflater.from(context).inflate(
						R.layout.dept_acc_in_list_item, null);
			}
			
//			convertView.measure(0, 0);
//			int itemHeight = convertView.getMeasuredHeight();
//			int itemWidth = LayoutValue.SCREEN_WIDTH - padding;
//			LayoutParams lp = new LayoutParams(itemWidth, itemHeight);
//			convertView.setLayoutParams(lp);
			
			//账户类型
			TextView accountTypeTv = (TextView) convertView
					.findViewById(R.id.dept_type_tv);
			String strAccountType = (String) accountInList.get(position).get(Comm.ACCOUNT_TYPE);
			accountTypeTv.setText(LocalData.AccountType.get(strAccountType));
			//别名
			TextView accAddressTv = (TextView) convertView
					.findViewById(R.id.tv_acc_address);
//			String strAddress = LocalData.Province
//					.get((String) accountInList.get(position).get(
//							Comm.NICKNAME));
			String nickName = (String) accountInList.get(position).get(
					Comm.NICKNAME);
			accAddressTv.setText(nickName);
			//账号
			TextView accNoTv = (TextView) convertView
					.findViewById(R.id.tv_acc_no);
			String accNo = (String) accountInList.get(position).get(
					Comm.ACCOUNTNUMBER);
			accNoTv.setText(StringUtil.getForSixForString(accNo));
		return convertView;
	}

}
