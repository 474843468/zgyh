package com.chinamworld.bocmbci.biz.loan.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class LoanUseQueryResultAdapter extends BaseAdapter {

	private Context context = null;

	private List<Map<String, Object>> dataList = null;
	private String currency = null;

	public LoanUseQueryResultAdapter(Context context, List<Map<String, Object>> dataList, String currency) {
		this.context = context;
		this.dataList = dataList;
		this.currency = currency;
	}

	public void setListData(List<Map<String, Object>> list) {
		dataList = list;
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.loan_query_query_list_item, null);
			viewHolder.creatDateTv = (TextView) convertView.findViewById(R.id.dept_cd_number_tv);
			viewHolder.payerTv = (TextView) convertView.findViewById(R.id.dept_type_tv);
			viewHolder.tranStatusTv = (TextView) convertView.findViewById(R.id.dept_avaliable_balance_tv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 用款日期
		String tranDate = (String) dataList.get(position).get(Loan.LOAN_APPLY_DATE);
		viewHolder.creatDateTv.setText(tranDate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, viewHolder.creatDateTv);
		// 用款金额
		String loanApplyAmount = (String) dataList.get(position).get(Loan.LOAN_APPLY_AMOUNT);

		String loanApplyAmounts = null;
		if (StringUtil.isNull(loanApplyAmount)) {
			loanApplyAmounts = "-";
		} else {
			loanApplyAmounts = StringUtil.parseStringCodePattern(currency, loanApplyAmount, 2);
		}
		viewHolder.payerTv.setText(loanApplyAmounts);
		// 用款流水号
		String applyId = (String) dataList.get(position).get(Loan.LOAN_APPLY_ID);
		viewHolder.tranStatusTv.setText(applyId);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, viewHolder.tranStatusTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, viewHolder.payerTv);
		return convertView;
	}

	private class ViewHolder {
		public TextView creatDateTv;
		public TextView payerTv;
		public TextView tranStatusTv;
	}

}
