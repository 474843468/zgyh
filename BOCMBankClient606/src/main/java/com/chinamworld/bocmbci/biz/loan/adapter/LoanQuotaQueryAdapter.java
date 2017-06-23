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
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class LoanQuotaQueryAdapter extends BaseAdapter {
	private static final String TAG = "LoanQuotaQueryAdapter";
	private Context context = null;
	private List<Map<String, String>> list = null;

	public LoanQuotaQueryAdapter(Context context, List<Map<String, String>> list) {
		this.context = context;
		this.list = list;
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
			LogGloble.d(TAG, "getView");
			convertView = LayoutInflater.from(context).inflate(R.layout.loan_quato_query_item, null);
			holder = new ViewHolder();
			holder.customerVolumNumber = (TextView) convertView.findViewById(R.id.forex_custoner_fix_volunber);
			holder.customerCode = (TextView) convertView.findViewById(R.id.forex_custoner_fix_code);
			holder.customerBalance = (TextView) convertView.findViewById(R.id.forex_custoner_fix_balance);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String, String> map = list.get(position);
		String loanType = map.get(Loan.LOAN_LOANTYPE_REQ);
		String quotaNumber = map.get(Loan.LOAN_QUOTANUMBER_RES);
		String availableQuota = map.get(Loan.LOAN_QUOTA_RES);
		String currencyCode = map.get(Loan.LOAN_CURRENCYCODES_RES);
		String money = null;
		if (!StringUtil.isNull(availableQuota) && !StringUtil.isNull(currencyCode)) {
			money = StringUtil.parseStringCodePattern(currencyCode, availableQuota, 2);
		} else {
			money = "-";
		}
		String loantype = null;
		if (StringUtil.isNull(loanType)) {
			loantype = "-";
		} else {
			if (LoanData.loanTypeDataLimit.containsKey(loanType)) {
				loantype = LoanData.loanTypeDataLimit.get(loanType);
			} else {
				loantype = "-";
			}
		}
		holder.customerVolumNumber.setText(loantype);
		holder.customerCode.setText(quotaNumber);
		holder.customerBalance.setText(money);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, holder.customerVolumNumber);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, holder.customerCode);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, holder.customerBalance);
		return convertView;
	}

	/**
	 * 存放控件
	 */
	public final class ViewHolder {

		public TextView customerVolumNumber;
		public TextView customerCode;
		public TextView customerBalance;

	}
}
