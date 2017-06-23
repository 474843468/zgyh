package com.chinamworld.bocmbci.biz.sbremit.histrade.adapter;

import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.bii.constant.SBRemit;
import com.chinamworld.bocmbci.biz.sbremit.SBRemitBaseActivity;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class HistoyTradeListAdapter extends BaseAdapter {

	private BaseActivity context;
	private List<Map<String, Object>> hisTradeList;

	public HistoyTradeListAdapter(BaseActivity context,
			List<Map<String, Object>> hisTradeList) {
		this.hisTradeList = hisTradeList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return getAccountList().size();
	}

	@Override
	public Object getItem(int position) {
		return getAccountList().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final Map<String, Object> tempMap = hisTradeList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.sbremit_query_history_listheader, null);
			holder.right_btn = (ImageView) convertView
					.findViewById(R.id.right_btn);
			holder.list_header_tv1 = (TextView) convertView
					.findViewById(R.id.list_header_tv1);
			holder.list_header_tv2 = (TextView) convertView
					.findViewById(R.id.list_header_tv2);
			holder.list_header_tv3 = (TextView) convertView
					.findViewById(R.id.list_header_tv3);
			holder.divider = (TextView) convertView.findViewById(R.id.divider);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.list_header_tv1.setTag(tempMap);
		holder.right_btn.setVisibility(View.VISIBLE);
		holder.list_header_tv1.setText(QueryDateUtils.getcurrentDate(tempMap
				.get(SBRemit.TRANS_DATE).toString()));
		// holder.list_header_tv2.setText(StringUtil.valueOf1(SBRemitDataCenter.getInstance().getSbRemit().
		// get(tempMap.get(SBRemit.TRANS_TYPE))));

		holder.list_header_tv2.setText(StringUtil.valueOf1(SBRemitBaseActivity
				.getKeyByValue(LocalData.sbRemitValueKey_New,
						(String) tempMap.get(SBRemit.TRANS_TYPE))));

		holder.list_header_tv3.setText(LocalData.Currency.get(StringUtil
				.valueOf1(tempMap.get(SBRemit.CURRENCY_CODE).toString())));
		holder.divider.setVisibility(View.INVISIBLE);
		return convertView;
	}

	class ViewHolder {
		ImageView right_btn;
		TextView list_header_tv1;
		TextView list_header_tv2;
		TextView list_header_tv3;
		TextView divider;
	}

	// public void setSelectedPosition(int selectedPosition) {
	// this.selectedPosition = selectedPosition;
	// notifyDataSetChanged();
	// }

	public void setAccountList(List<Map<String, Object>> hisTradeList) {
		this.hisTradeList = hisTradeList;
		this.notifyDataSetChanged();
	}

	public void clearList() {
		if (hisTradeList != null && hisTradeList.size() > 0) {
			hisTradeList.clear();
		}
	}

	public List<Map<String, Object>> getAccountList() {
		return hisTradeList;
	}

}
