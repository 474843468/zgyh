package com.chinamworld.bocmbci.biz.forex.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 账户设置确认页面的适配器
 * 
 * @author Administrator
 * 
 */
public class ForexAccSettingConfirmAdapter extends BaseAdapter {
	private final String TAG = "ForexAccSettingConfirmAdapter";
	private Context context;
	private LayoutInflater mInflater;
	private List<Map<String, String>> list;

	public ForexAccSettingConfirmAdapter(Context context, List<Map<String, String>> list) {
		super();
		this.context = context;
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
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.forex_acc_setting_confirm_item, null);
			holder = new ViewHolder();
			holder.forexCode = (TextView) convertView.findViewById(R.id.acc_code);
			holder.forexCash = (TextView) convertView.findViewById(R.id.acc_cash);
			holder.forexMoney = (TextView) convertView.findViewById(R.id.acc_balance);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.forexCode.setTag(position);
		holder.forexCash.setTag(position);
		holder.forexMoney.setTag(position);
		Map<String, String> map = list.get(position);
		String code1 = map.get(Forex.FOREX_CURRENCYCODE_REQ);
		// 币种
		String code = null;
		if (LocalData.Currency.containsKey(code1)) {
			code = LocalData.Currency.get(code1);
		}
		// 钞汇
		String cash1 = map.get(Forex.FOREX_CASEREMIT_REQ);
		String cash = null;
		if (LocalData.CurrencyCashremit.containsKey(cash1)) {
			cash = LocalData.CurrencyCashremit.get(cash1);
		}

		// 可用余额
		String balance = map.get(Forex.FOREX_AVAILABLEBALANCE_REQ);	
		holder.forexCode.setText(code);
		holder.forexCash.setText(cash);
//		if (LocalData.codeNoNumber.contains(code1)) {
//			if (StringUtil.isNullOrEmpty(balance)) {
//				holder.forexMoney.setText("0");
//			} else {
//				balance = StringUtil.parseStringPattern(balance, 0);
//				holder.forexMoney.setText(balance);
//			}
//		} else {
//			if (StringUtil.isNullOrEmpty(balance)) {
//				holder.forexMoney.setText("0.00");
//			} else {
//				balance = StringUtil.parseStringPattern(balance, 2);
//				holder.forexMoney.setText(balance);
//			}
//		}
		if(!StringUtil.isNull(code1)&&!StringUtil.isNull(balance)){
			balance = StringUtil.parseStringCodePattern(code1,balance, 2);
		}
		holder.forexMoney.setText(balance);
		return convertView;
	}

	/**
	 * 存放控件
	 * 
	 * @author Administrator
	 * 
	 */
	public final class ViewHolder {
		/**
		 * 币种
		 */
		public TextView forexCode;
		/**
		 * 钞汇
		 */
		public TextView forexCash;
		/**
		 * 可用余额
		 */
		public TextView forexMoney;
		public TextView balanceText = null;
	}

}
