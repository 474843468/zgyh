package com.chinamworld.bocmbci.biz.tran.collect.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Collect;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectResultType;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: CollectQueryDetailAdapter
 * @Description: 跨行资金归集查询结果详情
 * @author luql
 * @date 2014-4-8 下午02:49:05
 */
public class CollectQueryDetailAdapter extends BaseAdapter {

	private Context context = null;

	private List<Map<String, Object>> accountList = null;

	public CollectQueryDetailAdapter(Context context, List<Map<String, Object>> accountList) {
		this.context = context;
		this.accountList = accountList;
	}

	public void setListData(List<Map<String, Object>> list) {
		accountList = list;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.collect_query_detail_item, null);
			viewHolder.transNoView = (TextView) convertView.findViewById(R.id.tv_workno);
			viewHolder.amountView = (TextView) convertView.findViewById(R.id.tv_amount);
			viewHolder.statusView = (TextView) convertView.findViewById(R.id.tv_status);
			//viewHolder.codeView = (TextView) convertView.findViewById(R.id.tv_code);
			viewHolder.messageView = (TextView) convertView.findViewById(R.id.tv_message);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Map<String, Object> map = accountList.get(position);

		String transNo = (String) map.get(Collect.cbTransNo);
		String status = (String) map.get(Collect.cbStatus);
		String amount = (String) map.get(Collect.cbTransAmt);
		String code = (String) map.get(Collect.errorCode);
		String message = (String) map.get(Collect.errorMsg);

		viewHolder.transNoView.setText(transNo);
		viewHolder.amountView.setText(StringUtil.parseStringPattern(amount,2));
		viewHolder.statusView.setText(CollectResultType.getResultStr(status));
		//viewHolder.codeView.setText(code);
		viewHolder.messageView.setText(message);
		
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, viewHolder.transNoView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, viewHolder.messageView);

		return convertView;
	}

	private class ViewHolder {
		public TextView transNoView;
		public TextView amountView;
		public TextView statusView;
		//public TextView codeView;
		public TextView messageView;
	}

}
