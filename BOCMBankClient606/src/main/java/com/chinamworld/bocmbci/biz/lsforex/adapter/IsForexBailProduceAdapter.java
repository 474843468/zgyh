package com.chinamworld.bocmbci.biz.lsforex.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 签约账户列表页面 适配器*/
public class IsForexBailProduceAdapter extends BaseAdapter {
	private Context context;
	private List<Map<String, String>> list;
	private OnItemClickListener onItemClickListener = null;

	public OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public IsForexBailProduceAdapter(Context context, List<Map<String, String>> list) {
		super();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.isforex_manage_produce_item, null);
			holder = new ViewHolder();
			holder.baillAccText = (TextView) convertView.findViewById(R.id.isForex_bailAcc);
			holder.tradeAccText = (TextView) convertView.findViewById(R.id.isForex_tradeAcc);
			holder.codeAndCashText = (TextView) convertView.findViewById(R.id.isForex_manage_produce_code);
			holder.timesText = (TextView) convertView.findViewById(R.id.isForex_manage_produce_times);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String, String> map = list.get(position);
		String accountNumber = map.get(IsForex.ISFOREX_ACCOUNTNUMBER_RES);
		// String nickName = map.get(IsForex.ISFOREX_NICKNAME_RES1);
		String subAcctType = map.get(IsForex.ISFOREX_SUBACCTTYPE);
		String marginAccountNo = map.get(IsForex.ISFOREX_MARGINACCOUNTNO_RES1);
		String settleCurrency = map.get(IsForex.ISFOREX_SETTLECURRENCY_RES1);
		String signDate = map.get(IsForex.ISFOREX_SIGNDATE_RES);
		String bailAcc = null;
		if (!StringUtil.isNull(marginAccountNo)) {
			bailAcc = StringUtil.getForSixForString(marginAccountNo);
		}
		String tradeAcc = null;
		if (!StringUtil.isNull(accountNumber)) {
			tradeAcc = StringUtil.getForSixForString(accountNumber);
		}
		String code = null;
		if (!StringUtil.isNull(settleCurrency) && LocalData.Currency.containsKey(settleCurrency)) {
			code = LocalData.Currency.get(settleCurrency);
		}
		String cash = null;
		// 0 – 人民币户 1 – 单钞户 3 – 钞汇户 即 1、人民币账户 2、XX现钞账户 3、XX钞汇账户
		if (!StringUtil.isNull(subAcctType)) {
			if (ConstantGloble.FOREX_FLAG0.equals(subAcctType)) {
				cash = context.getResources().getString(R.string.isForex_manage_cash_rmb);
				holder.codeAndCashText.setText(cash);
			} else if (ConstantGloble.FOREX_FLAG1.equals(subAcctType)) {
				cash = context.getResources().getString(R.string.isForex_manage_cash_cash1);
				holder.codeAndCashText.setText(code + cash);
			} else if (ConstantGloble.FOREX_FLAG3.equals(subAcctType)) {
				cash = context.getResources().getString(R.string.isForex_manage_cash_cash2);
				holder.codeAndCashText.setText(code + cash);
			} else {
				holder.codeAndCashText.setText(code);
			}
		}
		String type = context.getResources().getString(R.string.isForex_manage_type);
		holder.baillAccText.setText(bailAcc);
		holder.tradeAccText.setText(type + " " + tradeAcc);
		holder.timesText.setText(signDate);
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onItemClickListener != null) {
					onItemClickListener.onItemClick(null, v, position, position);
				}
			}
		});
		return convertView;
	}

	private final class ViewHolder {
		private TextView baillAccText = null;
		private TextView tradeAccText = null;
		private TextView codeAndCashText = null;
		private TextView timesText = null;
	}

}
