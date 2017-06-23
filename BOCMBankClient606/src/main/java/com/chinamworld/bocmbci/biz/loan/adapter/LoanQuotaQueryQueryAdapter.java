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
import com.chinamworld.bocmbci.biz.loan.LoanData;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

public class LoanQuotaQueryQueryAdapter extends BaseAdapter {
	private List<Map<String, String>> list = null;
	private Context context = null;

	public LoanQuotaQueryQueryAdapter(Context context, List<Map<String, String>> list) {
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.loan_quato_query_query_item, null);
			holder = new ViewHolder();
			holder.loanTypeText = (TextView) convertView.findViewById(R.id.loan_query_name);
			holder.accountNUmberText = (TextView) convertView.findViewById(R.id.loan_choise_accountNumber);
			holder.currencyText = (TextView) convertView.findViewById(R.id.loan_currency);
			holder.moneyText = (TextView) convertView.findViewById(R.id.loan_choise_pa_moneys);
			holder.timesText = (TextView) convertView.findViewById(R.id.loan_repay_loanPeriod);
			holder.dateText = (TextView) convertView.findViewById(R.id.loan_cycleMatDate);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String, String> map = list.get(position);
		String loanType = map.get(Loan.LOAN_LOANTYPE_REQ);
		String loanAccount = map.get(Loan.LOAN_ACCOUNTNUMBER_RES);
		String loanAccounts = null;
		if (!StringUtil.isNull(loanAccount)) {
			loanAccounts = StringUtil.getForSixForString(loanAccount);
		} else {
			loanAccounts = "-";
		}
		String currency = map.get(Loan.LOAN_CURRENCYCODES_RES);
		String code = null;
		if (!StringUtil.isNull(currency) && LocalData.Currency.containsKey(currency)) {
			code = LocalData.Currency.get(currency);
		} else {
			code = "-";
		}
		String loanAmount = map.get(Loan.LOAN_LOANAMOUNT_RES);
		String loanAmounts = null;
		if (!StringUtil.isNull(loanAmount) && !StringUtil.isNull(currency)) {
			loanAmounts = StringUtil.parseStringCodePattern(currency, loanAmount, 2);
		} else {
			loanAmounts = "-";
		}
		String loantype = null;
		if (StringUtil.isNull(loanType)) {
			loantype = "-";
		} else {
			if (LoanData.loanTypeData.containsKey(loanType)) {
				loantype = LoanData.loanTypeData.get(loanType);
			} else {
				loantype = "-";
			}
		}
		String loanPeriod = map.get(Loan.LOAN_LOANPERIOD_REQ);
		String loanToDate = map.get(Loan.LOAN_LOANTODATE_REQ);
		holder.loanTypeText.setText(loantype);
		holder.accountNUmberText.setText(loanAccounts);
		holder.currencyText.setText(code);
		holder.moneyText.setText(loanAmounts);
		String month = context.getString(R.string.month);
		if("0".equals(loanPeriod)){
			holder.timesText.setText(ConstantGloble.BOCINVT_DATE_ADD);
		}else{
			holder.timesText.setText(StringUtil.isNull(loanPeriod)?
					ConstantGloble.BOCINVT_DATE_ADD:loanPeriod+month);
		}
		holder.dateText.setText(StringUtil.isNullOrEmptyCaseNullString(loanToDate) ? 
				ConstantGloble.BOCINVT_DATE_ADD : loanToDate);
		return convertView;

	}

	private class ViewHolder {
		private TextView loanTypeText = null;
		private TextView accountNUmberText = null;
		private TextView currencyText = null;
		private TextView moneyText = null;
		private TextView timesText = null;
		private TextView dateText = null;

	}
}
