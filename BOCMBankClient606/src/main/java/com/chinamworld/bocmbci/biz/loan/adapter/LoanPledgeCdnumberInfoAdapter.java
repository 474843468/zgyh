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
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

public class LoanPledgeCdnumberInfoAdapter extends BaseAdapter {
	private List<Map<String, String>> list = null;
	private Context context = null;
	public int selectionPosition = -1;
	private List<Boolean> listFlag = null;

	public LoanPledgeCdnumberInfoAdapter(Context context, List<Map<String, String>> list, List<Boolean> listFlag) {
		this.listFlag = listFlag;
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

	public void dateChange(List<Map<String, String>> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public void dateChanged(List<Boolean> listFlag) {
		this.listFlag = listFlag;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.loan_select_cdnumber_item, null);
			holder = new ViewHolder();
			holder.cdNumberText = (TextView) convertView.findViewById(R.id.forex_custoner_fix_volunber);
			holder.codeText = (TextView) convertView.findViewById(R.id.forex_custoner_fix_code);
			holder.moneyText = (TextView) convertView.findViewById(R.id.forex_custoner_fix_balance);
			holder.imgView = (ImageView) convertView.findViewById(R.id.imageViewright);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (listFlag.get(position)) {
			holder.imgView.setVisibility(View.VISIBLE);
			convertView.setBackgroundResource(R.drawable.bg_for_listview_item_half_black);
		} else {
			holder.imgView.setVisibility(View.INVISIBLE);
			convertView.setBackgroundResource(R.drawable.bg_for_listview_item_write);
		}
		Map<String, String> map = list.get(position);
		String volumeNumber = map.get(Loan.LOAN_VOLUMBERNUMBER_RES);
		String cdNumber = map.get(Loan.LOAN_CDNUMBER_RES);
		String currencyCode = map.get(Loan.LOAN_CURRENCYCODE_RES);
		String cashRemit = map.get(Loan.LOAN_CASHREMIT_RES);
		String availableBalance = map.get(Loan.LOAN_AVAILABLEBALANCE_RES);
		String code = null;
		if (StringUtil.isNull(currencyCode)) {
			code = "-";
		} else {
			if (LocalData.Currency.containsKey(currencyCode)) {
				code = LocalData.Currency.get(currencyCode);
			} else {
				code = "-";
			}
		}
		String cash = null;
		if (StringUtil.isNull(cashRemit)) {
			cash = "-";
		} else {
			if (LocalData.CurrencyCashremit.containsKey(cashRemit)) {
				cash = LocalData.CurrencyCashremit.get(cashRemit);
			} else {
				cash = "-";
			}
		}
		String money = null;
		if (StringUtil.isNull(availableBalance)) {
			money = "-";
		} else {
			money =StringUtil.parseStringCodePattern(currencyCode, availableBalance, 2);
		}
		holder.cdNumberText.setText(volumeNumber + "/" + cdNumber);
		if (LocalData.rmbCodeList.contains(currencyCode)) {
			holder.codeText.setText(code);
		} else {
			holder.codeText.setText(code + "/" + cash);
		}
		holder.moneyText.setText(money);
		return convertView;
	}

	private class ViewHolder {
		private TextView cdNumberText;
		private TextView moneyText;
		private TextView codeText;
		private ImageView imgView;
	}
}
