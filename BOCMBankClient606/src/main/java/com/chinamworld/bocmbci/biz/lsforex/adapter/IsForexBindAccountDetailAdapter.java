package com.chinamworld.bocmbci.biz.lsforex.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 双向宝交易账户---详情页面  适配器*/
public class IsForexBindAccountDetailAdapter extends BaseAdapter {
	private final String TAG = "IsForexBindAccountDetailAdapter";
	private Context context;
	private List<Map<String, String>> list;

	public IsForexBindAccountDetailAdapter(Context content, List<Map<String, String>> list) {
		this.context = content;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.isforex_bindaccount_detail_item, null);
			holder = new ViewHolder();
			holder.fincCode = (TextView) convertView.findViewById(R.id.finc_code);
			//PopupWindowUtils.getInstance().setOnShowAllTextListener(context, holder.fincCode);
			holder.fincBalance = (TextView) convertView.findViewById(R.id.finc_balance);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.fincCode.setTag(position);
		holder.fincBalance.setTag(position);
		Map<String, String> accountDetaiMap = list.get(position);
		// 币种
		String currencyCode = accountDetaiMap.get(IsForex.ISFOREX_CURRENCYCODE_RES);
		String code = null;
		if (!StringUtil.isNull(currencyCode) && LocalData.Currency.containsKey(currencyCode)) {
			code = LocalData.Currency.get(currencyCode);
		}
		// 钞汇
		String cashRemit = accountDetaiMap.get(IsForex.ISFOREX_CASEREMIT_RES);
		String cash = null;
		if (!StringUtil.isNull(cashRemit) && LocalData.CurrencyCashremit.containsKey(cashRemit)) {
			cash = LocalData.CurrencyCashremit.get(cashRemit);
		}
		// 可用余额
		String availableBalance = accountDetaiMap.get(IsForex.ISFOREX_AVAILABLEBALANCE_RES);
		String balance = null;
		if (!StringUtil.isNull(availableBalance) && !StringUtil.isNull(currencyCode)) {
			balance = StringUtil.parseStringCodePattern(currencyCode, availableBalance, 2);
		}
		if (!StringUtil.isNull(code) && !StringUtil.isNull(balance)) {
			if (LocalData.rebList.contains(currencyCode)) {
				holder.fincCode.setText(code);
			} else {
				holder.fincCode.setText(code + "/" + cash);
			}
			holder.fincBalance.setText(balance);
		}
		if(StringUtil.isNull(availableBalance)){
			holder.fincBalance.setText("-");
		}
		return convertView;
	}

	/**
	 * 内部类--存放控件
	 */
	public final class ViewHolder {
		/** 币种 钞汇 */
		private TextView fincCode;
		/** 可用余额 */
		private TextView fincBalance;

	}
}
