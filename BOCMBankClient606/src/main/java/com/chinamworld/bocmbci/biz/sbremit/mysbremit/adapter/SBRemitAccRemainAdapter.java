package com.chinamworld.bocmbci.biz.sbremit.mysbremit.adapter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.SBRemit;
import com.chinamworld.bocmbci.biz.sbremit.SBRemitBaseActivity;
import com.chinamworld.bocmbci.biz.sbremit.SBRemitDataCenter;
import com.chinamworld.bocmbci.biz.sbremit.mysbremit.AccountRemainActivity;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 账户余额列表适配器
 * 
 * @author fengsimin
 * 
 */
public class SBRemitAccRemainAdapter extends BaseAdapter {
	private Context mContext;
	private List<Map<String, Object>> mList;
	private int selectedPosition = -1;
	private String identityType1;

	public SBRemitAccRemainAdapter(Context context,
			AccountRemainActivity activity, List<Map<String, Object>> list,
			String accountId, String accountNumber, String identityType) {
		mContext = context;
		mList = list;
		identityType1 = identityType;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
		notifyDataSetChanged();
	}

	public void setData(List<Map<String, Object>> list) {
		mList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		final Map<String, Object> map = mList.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.sbremit_account_remain_listitem, null);
			viewHolder.money_type = (TextView) convertView
					.findViewById(R.id.acc_remain_listiterm_tv1);
			viewHolder.money_remit = (TextView) convertView
					.findViewById(R.id.acc_remain_listiterm_tv2);
			viewHolder.money_remain = (TextView) convertView
					.findViewById(R.id.acc_remain_listiterm_tv3);
			viewHolder.exchange_type = (TextView) convertView
					.findViewById(R.id.right_tv);
			viewHolder.next = (TextView) convertView.findViewById(R.id.next);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 币种
		String currency = (String) mList.get(position).get(SBRemit.CURRENCY);
		// 钞汇
		String cashRemit = (String) mList.get(position).get(SBRemit.CASH_REMIT);
		// 可用余额
		String availableBalance = (String) mList.get(position).get(
				SBRemit.AVAILABLE_BALANCE);

		if (SBRemitDataCenter.getInstance().getMoneyType()
				.contains(map.get(SBRemit.CURRENCY))) {
			viewHolder.money_type.setText(SBRemitBaseActivity
					.fincCurrencyAndCashFlag(
							(String) map.get(SBRemit.CURRENCY),
							(String) map.get(SBRemit.CASH_REMIT)));
			viewHolder.money_remit.setText(StringUtil.parseStringCodePattern(
					map.get(SBRemit.CURRENCY).toString(),
					map.get(SBRemit.AVAILABLE_BALANCE).toString(),
					SBRemitDataCenter.getInstance().CASH_PATTERN_PARAM));
			viewHolder.exchange_type.setText(mContext
					.getString(R.string.sbremit_trade_type_sremit));

			// 判断是否是大陆居民身份证否则不显示购汇图片
			if (currency.equals("001")
					&& (LocalData.identityTypecollection
							.contains(identityType1))) {
				viewHolder.next.setVisibility(View.INVISIBLE);
				viewHolder.exchange_type.setVisibility(View.INVISIBLE);
			} else {
				viewHolder.next.setVisibility(View.VISIBLE);
				viewHolder.exchange_type.setVisibility(View.VISIBLE);
			}
		} else if (SBRemitDataCenter.getInstance().getRmbCode()
				.containsKey(map.get(SBRemit.CURRENCY))) {
			viewHolder.money_type.setText(SBRemitBaseActivity
					.fincCurrencyAndCashFlag(
							(String) map.get(SBRemit.CURRENCY),
							(String) map.get(SBRemit.CASH_REMIT)));
			viewHolder.money_remit.setText(StringUtil.parseStringCodePattern(
					map.get(SBRemit.CURRENCY).toString(),
					map.get(SBRemit.AVAILABLE_BALANCE).toString(),
					SBRemitDataCenter.getInstance().CASH_PATTERN_PARAM));
			viewHolder.exchange_type.setText(mContext
					.getString(R.string.sbremit_trade_type_bremit));
			// 判断是否是大陆居民身份证否则不显示购汇图片
			if (currency.equals("001")
					&& LocalData.identityTypecollection.contains(identityType1)) {
				viewHolder.next.setVisibility(View.INVISIBLE);
				viewHolder.exchange_type.setVisibility(View.INVISIBLE);
			} else {
				viewHolder.next.setVisibility(View.VISIBLE);
				viewHolder.exchange_type.setVisibility(View.VISIBLE);
			}
		}
		
		Map<String ,Object> hashMap = new HashMap<String,Object>();  // 金额比较大时，以"1.00516301E10"形式存在
		hashMap.put(SBRemit.CURRENCY, map.get(SBRemit.CURRENCY));
		hashMap.put(SBRemit.CASH_REMIT, map.get(SBRemit.CASH_REMIT));
		hashMap.put(SBRemit.AVAILABLE_BALANCE, new BigDecimal(map.get(SBRemit.AVAILABLE_BALANCE).toString()).toString());
		viewHolder.money_type.setTag(hashMap);
		return convertView;
	}

	class ViewHolder {
		TextView money_type;
		TextView money_remit;
		TextView money_remain;
		TextView exchange_type;
		TextView next;
	}
	// //是某个position不能点击
	// @Override
	// public boolean isEnabled(int position) {
	// // TODO Auto-generated method stub
	// return false;
	// }

}
