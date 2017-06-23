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
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

public class LoanSpinnerCustomAdapter extends BaseAdapter {
	private List<Map<String, Object>> list = null;
	private Context context = null;
	public static int selection = -1;

	public LoanSpinnerCustomAdapter(Context context, List<Map<String, Object>> list) {
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
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.spinner_myitem, null);
			holder.loan_spinner_card_type = (TextView) convertView.findViewById(R.id.loan_spinner_card_type);
			holder.loan_spinner_card_number = (TextView) convertView.findViewById(R.id.loan_spinner_card_number);
			holder.loan_spinner_card_alias = (TextView) convertView.findViewById(R.id.loan_spinner_card_alias);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String, Object> map = list.get(position);
		String accountType = (String) map.get(Loan.LOAN_ACCOUNTTYPE_RES);
		String accountTypes=LocalData.AccountType.get(accountType);
		String accountNumber = (String) map.get(Loan.LOAN_ACCOUNTNUMBER_RES);
		String nickName = (String) map.get(Loan.LOAN_NICKNAME_RES);
		
		
		if (StringUtil.isNull(accountTypes)) {
			accountType = "-";
		}
		
		if(StringUtil.isNull(accountNumber)){
			accountNumber = "-";
		}else{
			accountNumber = StringUtil.getForSixForString(accountNumber);
		}
	
		if(StringUtil.isNull(nickName)){
			nickName = "-";
		}

		holder.loan_spinner_card_type.setText(accountTypes);
		holder.loan_spinner_card_number.setText(accountNumber);
		holder.loan_spinner_card_alias.setText(nickName);
		return convertView;
	}
	private class ViewHolder {
		private TextView loan_spinner_card_type = null;
		private TextView loan_spinner_card_number = null;
		private TextView loan_spinner_card_alias = null;
	}
}
