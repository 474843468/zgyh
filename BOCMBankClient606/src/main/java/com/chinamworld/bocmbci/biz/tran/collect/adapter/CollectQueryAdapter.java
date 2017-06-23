package com.chinamworld.bocmbci.biz.tran.collect.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Collect;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectImputationMode;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectResultType;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: CollectQueryAdapter
 * @Description: 跨行资金归集查询结果列表adapter
 * @author luql
 * @date 2014-3-18 下午02:49:00
 */
public class CollectQueryAdapter extends BaseAdapter {

	private static final String TAG = CollectQueryAdapter.class.getSimpleName();

	private Context context = null;

	private List<Map<String, Object>> accountList = null;

	private OnClickListener onClickListener = null;

	public CollectQueryAdapter(Context context, List<Map<String, Object>> accountList) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.collect_query_list_item, null);
			viewHolder.bodyView = convertView.findViewById(R.id.body);
			viewHolder.workNoView = (TextView) convertView.findViewById(R.id.tv_workno);
			viewHolder.dateView = (TextView) convertView.findViewById(R.id.tv_date);
			viewHolder.statusView = (TextView) convertView.findViewById(R.id.tv_status);
			viewHolder.modeView = (TextView) convertView.findViewById(R.id.tv_mode);
			viewHolder.amountView = (TextView) convertView.findViewById(R.id.tv_amount);
			viewHolder.countView = (TextView) convertView.findViewById(R.id.tv_count);
			viewHolder.collectView = (TextView) convertView.findViewById(R.id.tv_collect);
			viewHolder.arrowView = convertView.findViewById(R.id.arrow);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Map<String, Object> map = accountList.get(position);

		String workNo = (String) map.get(Collect.workNo);
		String transDate = (String) map.get(Collect.transDate);
		String collectMode = CollectImputationMode.getTypeStr((String) map.get(Collect.imputationMode));
		String amount = (String) map.get(Collect.transAmt);
		String sccCount = (String) map.get(Collect.sccCount);
		String sumCount = (String) map.get(Collect.sumCount);
		String onTimeStatus = (String) map.get(Collect.onTimeStatus);
		String ifOntime = (String) map.get(Collect.ifOntime);

		int sum = 0;
		try {
			sum = Integer.valueOf(sumCount);
		} catch (NumberFormatException e) {
			LogGloble.e(TAG, e.getMessage(), e);
		}
		viewHolder.workNoView.setText(workNo);
		viewHolder.dateView.setText(transDate);
		viewHolder.modeView.setText(collectMode);
		viewHolder.amountView.setText(StringUtil.parseStringPattern(amount, 2));
		viewHolder.countView.setText(sccCount + " / " + sumCount);
		viewHolder.collectView.setText("1".equals(ifOntime) ? R.string.collect_ontime : R.string.collect_hand);

		if (sum > 1) {
			// 拆笔 1-成功 2-失败 3-处理中
			if ("3".equals(onTimeStatus)) {
				viewHolder.statusView.setText(R.string.collect_processing);
			} else {
				viewHolder.statusView.setText(R.string.collect_processed);
			}
			viewHolder.arrowView.setVisibility(View.VISIBLE);
			viewHolder.bodyView.setBackgroundResource(R.drawable.bg_for_listview_item_selector);
			viewHolder.bodyView.setOnClickListener(onClickListener);
			viewHolder.bodyView.setTag(map);
		} else {
			viewHolder.statusView.setText(CollectResultType.getResultStr(onTimeStatus));
			viewHolder.arrowView.setVisibility(View.INVISIBLE);
			viewHolder.bodyView.setBackgroundResource(R.drawable.bg_for_listview_item_write);
			viewHolder.bodyView.setOnClickListener(null);
			viewHolder.bodyView.setTag(null);
		}

		
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, viewHolder.workNoView);
		return convertView;
	}

	public void setOnClickListener(OnClickListener listener) {
		this.onClickListener = listener;
	}

	private class ViewHolder {
		public View bodyView;
		/** 功能流水号 */
		public TextView workNoView;
		/** 交易日期 */
		public TextView dateView;
		/** 状态 */
		public TextView statusView;
		/** 归集方式 */
		public TextView modeView;
		/** 归集成功金额 */
		public TextView amountView;
		/** 发起方式 */
		public TextView collectView;
		/** 归集笔数 */
		public TextView countView;
		public View arrowView;
	}

}
