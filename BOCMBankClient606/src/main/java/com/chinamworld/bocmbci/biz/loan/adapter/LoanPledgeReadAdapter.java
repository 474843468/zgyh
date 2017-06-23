package com.chinamworld.bocmbci.biz.loan.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.AdapterForLinearLayout;

public class LoanPledgeReadAdapter extends AdapterForLinearLayout {
	private List<Map<String, String>> list = null;
	private Context context = null;

	public LoanPledgeReadAdapter(Context context, List<Map<String, String>> list) {
		super(context, list);
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
		// if (convertView == null) {
		convertView = LayoutInflater.from(context).inflate(R.layout.loan_choise_read_item, null);
		holder = new ViewHolder();
		holder.accountNumberText = (TextView) convertView.findViewById(R.id.loan_read_accnumber);
		holder.cdNumberText = (TextView) convertView.findViewById(R.id.loan_read_cdNumber);
		holder.volumberText = (TextView) convertView.findViewById(R.id.loan_read_volunumber);
		holder.typeText = (TextView) convertView.findViewById(R.id.loan_read_type);
		holder.currencyText = (TextView) convertView.findViewById(R.id.loan_read_currency);
		holder.moneyText = (TextView) convertView.findViewById(R.id.loan_read_money);
		// convertView.setTag(holder);
		// } else {
		// holder = (ViewHolder) convertView.getTag();
		// }
		Map<String, String> map = list.get(position);
		String accountNumber = map.get(Loan.LOAN_ACCOUNTNUMBER_RES);
		String volumeNumber = map.get(Loan.LOAN_VOLUMBERNUMBER_RES);
		String cdNumber = map.get(Loan.LOAN_CDNUMBER_RES);
		String type = map.get(Loan.LOAN_TYPE_RES);
		String currencyCode = map.get(Loan.LOAN_CURRENCYCODE_RES);
		String availableBalance = map.get(Loan.LOAN_AVAILABLEBALANCE_RES);
		String number = null;
		if (!StringUtil.isNull(accountNumber)) {
			number = StringUtil.getForSixForString(accountNumber);
		} else {
			number = "-";
		}

		String accType = null;
		if (!StringUtil.isNull(type) && LocalData.fixAccTypeMap.containsKey(type)) {
			accType = LocalData.fixAccTypeMap.get(type);
		} else {
			accType = "-";
		}
		String code = null;
		if (!StringUtil.isNull(currencyCode) && LocalData.Currency.containsKey(currencyCode)) {
			code = LocalData.Currency.get(currencyCode);
		} else {
			code = "-";
		}
		String blance = null;
		if (!StringUtil.isNull(availableBalance) && !StringUtil.isNull(currencyCode)) {
			blance = StringUtil.parseStringCodePattern(currencyCode, availableBalance, 2);
		} else {
			blance = "-";
		}
		String convertType = map.get(Loan.LOAN_CONVERTTYPES_RES);
		String convert = null;
		if (!StringUtil.isNull(convertType) && LocalData.ConvertType.containsKey(convertType)) {
			convert = LocalData.ConvertType.get(convertType);
		} else {
			convert = "-";
		}
		holder.accountNumberText.setText(number);
		holder.cdNumberText.setText(volumeNumber + "/" + cdNumber);
		holder.volumberText.setText(convert);
		holder.typeText.setText(accType);
		holder.currencyText.setText(code);
		holder.moneyText.setText(blance);
		return convertView;
	}

	private class ViewHolder {
		private TextView accountNumberText = null;
		private TextView cdNumberText = null;
		/** 转存标志 */
		private TextView volumberText = null;
		private TextView typeText = null;
		private TextView currencyText = null;
		private TextView moneyText = null;
	}
}
