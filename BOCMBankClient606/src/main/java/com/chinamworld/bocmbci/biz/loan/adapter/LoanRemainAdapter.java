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
 * 剩余还款信息适配器
 * 
 * @author wangmengmeng
 * 
 */
public class LoanRemainAdapter extends BaseAdapter {
	/** 剩余还款信息列表 */
	private List<Map<String, Object>> remainList;
	private Context context;

	public LoanRemainAdapter(Context context,
			List<Map<String, Object>> remainList) {
		this.context = context;
		this.remainList = remainList;
	}

	@Override
	public int getCount() {

		return remainList.size();
	}

	@Override
	public Object getItem(int position) {

		return remainList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.loan_remain_list_item, null);
		}
		String currency = LoanAccountListActivity.hCurrencyCode;
		/** 还款日期 */
		TextView repayDate = (TextView) convertView
				.findViewById(R.id.loan_repayDate_value);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				repayDate);
		/** 期号 */
		TextView loanId = (TextView) convertView
				.findViewById(R.id.loan_loanId_value);
		PopupWindowUtils.getInstance()
				.setOnShowAllTextListener(context, loanId);
		/** 还款金额 */
		TextView remainAmount = (TextView) convertView
				.findViewById(R.id.loan_remainAmount_value);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				remainAmount);
		/** 还款本金 */
		TextView remainCapital = (TextView) convertView
				.findViewById(R.id.loan_remainCapital_value);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				remainCapital);
		/** 还款利息 */
		TextView remainInterest = (TextView) convertView
				.findViewById(R.id.loan_remainInterest_value);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				remainInterest);
		/** 还款日期 */
		repayDate.setText((String) remainList.get(position).get(
				Loan.REMAIN_LOAN_REPAYDATE_RES));
		/** 期号 */
		String loanLoanId=(String) remainList.get(position).get(
				Loan.REMAIN_LOAN_LOANID_RES);
		
		if(StringUtil.isNullOrEmpty(loanLoanId)){
			loanId.setText("-");
		}else{
			loanId.setText(loanLoanId);
		}
			
		
		/** 还款金额 */
		String amount = String.valueOf(remainList.get(position).get(
				Loan.REMAIN_LOAN_REMAINAMOUNT_RES));
		remainAmount.setText(StringUtil.parseStringCodePattern(currency,amount, 2));
		/** 还款本金 */
		String capital = String.valueOf(remainList.get(position).get(
				Loan.REMAIN_LOAN_REMAINCAPITAL_RES));
		remainCapital.setText(StringUtil.parseStringCodePattern(currency,capital, 2));
		/** 还款利息 */
		String interest = String.valueOf(remainList.get(position).get(
				Loan.REMAIN_LOAN_REMAININTEREST_RES));
		remainInterest.setText(StringUtil.parseStringCodePattern(currency,interest, 2));
		return convertView;

	}

}
