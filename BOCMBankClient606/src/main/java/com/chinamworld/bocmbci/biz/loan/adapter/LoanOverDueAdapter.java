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
import com.chinamworld.bocmbci.biz.loan.LoanAccountListActivity;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 逾期还款信息适配器
 * 
 * @author wangmengmeng
 * 
 */
public class LoanOverDueAdapter extends BaseAdapter {
	/** 逾期还款信息列表 */
	private List<Map<String, Object>> overdueList;
	private Context context;

	public LoanOverDueAdapter(Context context,
			List<Map<String, Object>> overdueList) {
		this.context = context;
		this.overdueList = overdueList;
	}

	@Override
	public int getCount() {

		return overdueList.size();
	}

	@Override
	public Object getItem(int position) {

		return overdueList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.loan_overdue_list_item, null);
		}
		String currency = LoanAccountListActivity.hCurrencyCode;
		/** 应还款日期 */
		TextView pymtDate = (TextView) convertView
				.findViewById(R.id.loan_pymtDate_value);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				pymtDate);
		/** 期号 */
		TextView loanId = (TextView) convertView
				.findViewById(R.id.loan_loanId_value);
		PopupWindowUtils.getInstance()
				.setOnShowAllTextListener(context, loanId);
		/** 逾期金额 */
		TextView overdueAmount = (TextView) convertView
				.findViewById(R.id.loan_overdueAmount_value);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				overdueAmount);
		/** 逾期本金 */
		TextView overdueCapital = (TextView) convertView
				.findViewById(R.id.loan_overdueCapital_value);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				overdueCapital);
		/** 逾期利息 */
		TextView overdueInterest = (TextView) convertView
				.findViewById(R.id.loan_overdueInterest_value);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				overdueInterest);
		/** 应还款日期 */
		pymtDate.setText((String) overdueList.get(position).get(
				Loan.OVERDUE_LOAN_PYMTDATE_RES));
		/** 期号 */
		loanId.setText((String) overdueList.get(position).get(
				Loan.OVERDUE_LOAN_LOANID_RES));
		/** 逾期金额 */
		String amount = String.valueOf(overdueList.get(position).get(
				Loan.OVERDUE_LOAN_OVERDUEAMOUNT_RES));
		overdueAmount.setText(StringUtil.parseStringCodePattern(currency,amount, 2));
		/** 逾期本金 */
		String capital = String.valueOf(overdueList.get(position).get(
				Loan.OVERDUE_LOAN_OVERDUECAPITAL_RES));
		overdueCapital.setText(StringUtil.parseStringCodePattern(currency,capital, 2));
		/** 逾期利息 */
		String interest = String.valueOf(overdueList.get(position).get(
				Loan.OVERDUE_LOAN_OVERDUEINTEREST_RES));
		overdueInterest.setText(StringUtil.parseStringCodePattern(currency,interest, 2));
		return convertView;

	}

}
