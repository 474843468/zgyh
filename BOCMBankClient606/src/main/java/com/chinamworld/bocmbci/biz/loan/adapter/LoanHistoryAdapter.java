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
import com.chinamworld.bocmbci.biz.loan.LoanData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 历史还款信息适配器
 * 
 * @author wangmengmeng
 * 
 */
public class LoanHistoryAdapter extends BaseAdapter {
	/** 历史还款信息列表 */
	private List<Map<String, Object>> historyList;
	private Context context;

	public LoanHistoryAdapter(Context context,
			List<Map<String, Object>> historyList) {
		this.context = context;
		this.historyList = historyList;
	}

	@Override
	public int getCount() {

		return historyList.size();
	}

	@Override
	public Object getItem(int position) {

		return historyList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.loan_history_list_item, null);
		}
		String currency = LoanAccountListActivity.hCurrencyCode;
		System.out.println("currency====="+currency);
		/** 还款日期 */
		TextView repayDate = (TextView) convertView
				.findViewById(R.id.loan_repayDate_value);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				repayDate);
		/** 期号 */
		TextView loanId = (TextView) convertView
				.findViewById(R.id.loan_loanId_value);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				loanId);
		/** 还款流水号 */
		TextView repayId = (TextView) convertView
				.findViewById(R.id.loan_repayId_value);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				repayId);
		/** 还款金额 */
		TextView repayAmount = (TextView) convertView
				.findViewById(R.id.loan_repayAmount_value);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				repayAmount);
		/** 还款方式 */
		TextView transType = (TextView) convertView
				.findViewById(R.id.loan_transType_value);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				transType);
		/** 还款本金 */
		TextView repayCapital = (TextView) convertView
				.findViewById(R.id.loan_repayCapital_value);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				repayCapital);
		/** 还款利息 */
		TextView repayInterest = (TextView) convertView
				.findViewById(R.id.loan_repayInterest_value);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				repayInterest);
		// 赋值操作
		repayDate.setText((String) historyList.get(position).get(
				Loan.HISTORY_LOAN_REPAYDATE_RES));
		loanId.setText((String) historyList.get(position).get(
				Loan.HISTORY_LOAN_LOANID_RES));
		repayId.setText((String) historyList.get(position).get(
				Loan.HISTORY_LOAN_REPAYID_RES));
		String repayamount = String.valueOf(historyList.get(position).get(
				Loan.HISTORY_LOAN_REPAYAMOUNT_RES));
		repayAmount.setText(StringUtil.parseStringCodePattern(currency,repayamount, 2));
		String type = (String) historyList.get(position).get(
				Loan.HISTORY_LOAN_TRANSTYPE_RES);
		transType.setText(LoanData.historyTypeMap.get(type));
		String capital = String.valueOf(historyList.get(position).get(
				Loan.HISTORY_LOAN_REPAYCAPITAL_RES));
		repayCapital.setText(StringUtil.parseStringCodePattern(currency,capital, 2));
		String interest = String.valueOf(historyList.get(position).get(
				Loan.HISTORY_LOAN_REPAYINTEREST_RES));
		repayInterest.setText(StringUtil.parseStringCodePattern(currency, interest, 2));

		return convertView;

	}

}
