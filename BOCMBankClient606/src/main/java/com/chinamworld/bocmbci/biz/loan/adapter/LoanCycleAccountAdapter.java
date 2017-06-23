package com.chinamworld.bocmbci.biz.loan.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.LoanData;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 个人循环贷款列表适配器
 * 
 * @author wanbing
 * 
 */
public class LoanCycleAccountAdapter extends BaseAdapter {
	/** 贷款账户列表信息 */
	private List<Map<String, Object>> loanlist;
	private Context context;

	public LoanCycleAccountAdapter(Context context,
			List<Map<String, Object>> loanlist) {
		this.context = context;
		this.loanlist = loanlist;
	}

	@Override
	public int getCount() {
		return loanlist.size();
	}

	@Override
	public Object getItem(int position) {
		return loanlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.loan_account_list_item, null);
		}
		/** 右箭头—进入账户信息页面 */
		ImageView btGoItem = (ImageView) convertView
				.findViewById(R.id.loan_btn_goitem);
		btGoItem.setVisibility(View.GONE);

		/** 贷款方式 */
		TextView tv_loan_type = (TextView) convertView
				.findViewById(R.id.loan_type_value);
		/** 贷款账号 */
		TextView tv_loan_account = (TextView) convertView
				.findViewById(R.id.loan_account_value);
		/** 贷款币种 */
		TextView tv_loan_currencycode = (TextView) convertView
				.findViewById(R.id.loan_currencycode_value);
		/**可用金额*/
		TextView loan_amount_title = (TextView) convertView
				.findViewById(R.id.loan_amount);
		loan_amount_title.setText(R.string.forex_rate_currency_money);
		/** 贷款金额(可用金额) */
		TextView tv_loan_amount = (TextView) convertView
				.findViewById(R.id.loan_amount_value);
		String loan_type = (String) loanlist.get(position).get(
				Loan.LOANACC_LOAN_TYPE_RES);
		tv_loan_type.setText(LoanData.loanTypeData.get(loan_type));
		String loan_account = (String) loanlist.get(position).get(
				Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES);
		tv_loan_account.setText(StringUtil.getForSixForString(loan_account));
		tv_loan_account.setTextColor(context.getResources().getColor(R.color.black));
		String currency = (String) loanlist.get(position).get(
				Loan.LOANACC_CURRENCYCODE_RES);
		tv_loan_currencycode.setText(LocalData.Currency.get(currency));
		String loan_amount = (String.valueOf(loanlist.get(position).get(
				Loan.LOAN_CYCLE_AVA_AMOUNT)));
		tv_loan_amount.setText(StringUtil.parseStringCodePattern(currency,loan_amount, 2));
		return convertView;
	}
}
