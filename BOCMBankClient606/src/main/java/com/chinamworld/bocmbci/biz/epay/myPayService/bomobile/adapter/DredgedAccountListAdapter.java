package com.chinamworld.bocmbci.biz.epay.myPayService.bomobile.adapter;

import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.BomConstants;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

public class DredgedAccountListAdapter extends BaseAdapter {

	private BaseActivity context;

	private String tag = "EPayDredgedAccountListAdapter";

	private Context bomTransContext;

	private OnItemClickListener delItemClickListener;

	public DredgedAccountListAdapter(BaseActivity context) {
		bomTransContext = TransContext.getBomContext();
		this.context = context;
	}

	@Override
	public int getCount() {
		return bomTransContext.getList(PubConstants.CONTEXT_FIELD_DREDGED_LIST).size();
	}

	@Override
	public Object getItem(int position) {
		return bomTransContext.getList(PubConstants.CONTEXT_FIELD_DREDGED_LIST).get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.epay_bom_dredged_account_list_item, null);
			holder.tv_cardType = (TextView) convertView.findViewById(R.id.tv_cardtype);
			holder.tv_nickName = (TextView) convertView.findViewById(R.id.tv_nickname);
			holder.tv_account = (TextView) convertView.findViewById(R.id.tv_account);
			holder.ibt_del = convertView.findViewById(R.id.bt_del);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Map<Object, Object> tempMap = EpayUtil.getMap(bomTransContext.getList(PubConstants.CONTEXT_FIELD_DREDGED_LIST)
				.get(position));
		String accType = EpayUtil.getString(tempMap.get(BomConstants.METHOD_QUERY_DREDGED_ACC_LIST_FIELD_ACC_TYPE), "");
		holder.tv_cardType.setText(LocalData.AccountType.get(accType));
		holder.tv_nickName.setText(EpayUtil.getString(
				tempMap.get(BomConstants.METHOD_QUERY_DREDGED_ACC_LIST_FIELD_ACC_NICKNAME), ""));
		holder.tv_account.setText(StringUtil.getForSixForString(EpayUtil.getString(
				tempMap.get(BomConstants.METHOD_QUERY_DREDGED_ACC_LIST_FIELD_ACC_NUMBER), "")));

		if (bomTransContext.isRightButtonClick()) {
			holder.ibt_del.setVisibility(View.VISIBLE);
			holder.ibt_del.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					getDelItemClickListener().onItemClick(null, null, position, position);
				}
			});
		} else
			holder.ibt_del.setVisibility(View.GONE);

		return convertView;
	}

	class ViewHolder {
		TextView tv_cardType;
		TextView tv_nickName;
		TextView tv_account;
		View ibt_del;
	}

	public void notifyListViewData() {
		notifyDataSetChanged();
	}

	public void setDelItemClickListener(OnItemClickListener delItemClickListener) {
		this.delItemClickListener = delItemClickListener;
	}

	public OnItemClickListener getDelItemClickListener() {
		return delItemClickListener;
	}

}
