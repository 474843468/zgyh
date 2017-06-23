package com.chinamworld.bocmbci.biz.tran.managetrans.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 转账管理adapter
 * 
 * @author wangchao
 * 
 */
public class ManageBocPayeeAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> queryResultList = null;

	public ManageBocPayeeAdapter(Context context,
			List<Map<String, Object>> queryResultList) {
		this.mContext = context;
		this.queryResultList = queryResultList;
	}

	@Override
	public int getCount() {
		return queryResultList.size();
	}

	@Override
	public Object getItem(int position) {
		return queryResultList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setData(List<Map<String, Object>> queryResultList) {
		this.queryResultList = queryResultList;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.tran_manage_list_bankin_items, null);
			holder = new ViewHolder();

			holder.mobileTV = (TextView) convertView
					.findViewById(R.id.tv_one_item);
			holder.accountNumberTV = (TextView) convertView
					.findViewById(R.id.tv_two_item);
			holder.accountNameTV = (TextView) convertView
					.findViewById(R.id.tv_three_item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (holder != null) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) getItem(position);
			if (map != null) {
				holder.mobileTV.setText(StringUtil.isNullChange((String) map
						.get(Tran.MANAGE_PAYEELIST_MOBILE_RES)));
				PopupWindowUtils.getInstance().setOnShowAllTextListener(
						mContext, holder.mobileTV);
				holder.accountNumberTV.setText(StringUtil
						.getForSixForString((String) map
								.get(Tran.MANAGE_PAYEELIST_ACCOUNTNUMBER_RES)));
				PopupWindowUtils.getInstance().setOnShowAllTextListener(
						mContext, holder.accountNumberTV);
				holder.accountNameTV.setText(StringUtil
						.isNullChange((String) map
								.get(Tran.MANAGE_PAYEELIST_ACCOUNTNAME_RES)));
				PopupWindowUtils.getInstance().setOnShowAllTextListener(
						mContext, holder.accountNameTV);
			}

		}
		return convertView;
	}

	private class ViewHolder {
		public TextView mobileTV;
		public TextView accountNumberTV;
		public TextView accountNameTV;
	}

}
