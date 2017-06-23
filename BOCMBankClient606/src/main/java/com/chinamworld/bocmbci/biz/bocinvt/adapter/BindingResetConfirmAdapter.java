package com.chinamworld.bocmbci.biz.bocinvt.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 登记账户确认页面
 * 
 * @author wangmengmeng
 * 
 */
public class BindingResetConfirmAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<Map<String, Object>> list;

	public BindingResetConfirmAdapter(Context context,
			List<Map<String, Object>> list) {
		super();

		this.mInflater = LayoutInflater.from(context);
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.forex_acc_setting_confirm_item, null);
			/** 币种 */
			TextView bocinvtCode = (TextView) convertView
					.findViewById(R.id.acc_code);
			String currencycode = (String) (list.get(position))
					.get(Acc.DETAIL_CURRENCYCODE_RES);
			bocinvtCode.setText(LocalData.Currency.get(currencycode));
			TextView bocinvtCash = (TextView) convertView
					.findViewById(R.id.acc_cash);
			bocinvtCash.setVisibility(View.GONE);
			/** 可用余额 */
			TextView bocinvtMoney = (TextView) convertView
					.findViewById(R.id.acc_balance);
			String bookbalance = String.valueOf(list.get(position).get(
					Acc.DETAIL_AVAILABLEBALANCE_RES));
			bocinvtMoney.setText(StringUtil.parseStringPattern(bookbalance, 2));
		}
		return convertView;
	}

}
