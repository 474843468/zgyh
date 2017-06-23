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

public class LoanRepayAccountAdapter extends BaseAdapter {
	private List<Map<String, String>> list = null;
	private Context context = null;
	public static int selection = -1;

	public LoanRepayAccountAdapter(Context context, List<Map<String, String>> list) {
		this.list = list;
		this.context = context;
	}

	public void changeDate(List<Map<String, String>> list) {
		this.list = list;
		notifyDataSetChanged();
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
			convertView = LayoutInflater.from(context).inflate(R.layout.loan_repay_choise_acc_item, null);
			holder.loanTypeText = (TextView) convertView.findViewById(R.id.loan_choise_input_loanType);
			holder.loanAccText = (TextView) convertView.findViewById(R.id.loan_choise_accountNumber1);
			holder.loanMoneyText = (TextView) convertView.findViewById(R.id.loan_choise_pa_moneys);
			holder.loanCodeText = (TextView) convertView.findViewById(R.id.loan_choise_input_code);
			holder.img = (ImageView) convertView.findViewById(R.id.imageViewright);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position == selection) {
			convertView.setBackgroundResource(R.drawable.bg_for_listview_item_half_black);
			holder.img.setVisibility(View.VISIBLE);
		} else {
			convertView.setBackgroundResource(R.drawable.bg_for_listview_item_write);
			holder.img.setVisibility(View.INVISIBLE);
		}
		Map<String, String> map = list.get(position);
		String loanType = map.get(Loan.LOAN_LOANTYPE_REQ);
		String loanAccount = map.get(Loan.LOAN_LOANACCOUNT_REQ);
		String loanAmount = map.get(Loan.LOANACC_LOAN_AMOUNT_RES);
		String currency = map.get(Loan.LOAN_CURRENCY_REQ);
		String number = null;
		if (!StringUtil.isNull(loanAccount)) {
			number = StringUtil.getForSixForString(loanAccount);
		} else {
			number = "-";
		}
		String code = null;
		if (!StringUtil.isNull(currency) && LocalData.Currency.containsKey(currency)) {
			code = LocalData.Currency.get(currency);
		} else {
			code = "-";
		}
		String money = null;
		if (!StringUtil.isNull(loanAmount) && !StringUtil.isNull(currency)) {
			money = StringUtil.parseStringCodePattern(currency, loanAmount, 2);
		} else {
			money = "-";
		}
		// TODO 贷款品种--数据字典
		String type=null;
		if(LoanData.loanTypeData.containsKey(loanType)){
			type=LoanData.loanTypeData.get(loanType);
		}else{
			type="-";
		}
		holder.loanTypeText.setText(type);
		holder.loanAccText.setText(number);
		holder.loanCodeText.setText(code);
		holder.loanMoneyText.setText(money);
		return convertView;
	}

	private class ViewHolder {
		private TextView loanTypeText = null;
		private TextView loanAccText = null;
		private TextView loanMoneyText = null;
		private TextView loanCodeText = null;
		private ImageView img = null;
	}
}
