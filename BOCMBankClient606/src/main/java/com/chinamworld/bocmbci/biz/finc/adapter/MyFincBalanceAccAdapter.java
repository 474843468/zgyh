package com.chinamworld.bocmbci.biz.finc.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 基金持仓---投资基金账户详细信息listView的适配器
 * 
 * @author Administrator
 * 
 */
public class MyFincBalanceAccAdapter extends BaseAdapter {
	private final String TAG = "MyFincBalanceAccAdapter";
	private Context context;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> list;
	// 判断卡的类型是否为信用卡
	private String accountType;

	public MyFincBalanceAccAdapter(Context context,
			List<Map<String, Object>> detailList, String accountType) {
		this.context = context;
		this.list = reSetlist(detailList);
		mInflater = LayoutInflater.from(context);
		this.accountType = accountType;
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

	public void notifyDataSetChanged(List<Map<String, Object>> list,
			String accountType) {
		this.list = list;
		this.accountType = accountType;
		super.notifyDataSetChanged();

	};

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.finc_myfinc_balance_accset_item, null);
			holder = new ViewHolder();
			holder.fincCode = (TextView) convertView
					.findViewById(R.id.finc_code);
			// holder.fincCash = (TextView)
			// convertView.findViewById(R.id.finc_cash);
			holder.fincBalance = (TextView) convertView
					.findViewById(R.id.finc_balance);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.fincCode.setTag(position);
		// holder.fincCash.setTag(position);
		holder.fincBalance.setTag(position);
		Map<String, Object> accountDetaiMap = list.get(position);
		// 币种
				String currencyCode;
				String code;
				// 可用余额
				String availableBalance;
				// 钞汇
				String cashRemit;
		if(accountType.equals("103")||accountType.equals("104")||accountType.equals("107")){
			// 币种
			 currencyCode =  (String) (accountDetaiMap.get(Finc.FINC_QUERYBANCE_CODE));
			 code = LocalData.Currency.get(currencyCode);
			// 可用余额
			 availableBalance = (String) accountDetaiMap
					.get(Finc.FINC_QUERYBANCE_CURRENTBALANCE);
			 availableBalance = StringUtil.parseStringCodePattern(currencyCode,availableBalance, 2);
				// 钞汇
			 cashRemit = (String) accountDetaiMap.get(Finc.FINC_CASEREMIT_RES);
		}else {
			// 币种
			 currencyCode = (String) accountDetaiMap.get(Finc.FINC_CURRENCYCODE_RES);
			 code = LocalData.Currency.get(currencyCode);
			// 可用余额
			 availableBalance = (String) accountDetaiMap
					.get(Finc.FINC_AVAILABLEBALANCE_RES);
			 availableBalance = StringUtil.parseStringCodePattern(currencyCode,availableBalance, 2);
				// 钞汇
			 cashRemit = (String) accountDetaiMap.get(Finc.FINC_CASEREMIT_RES);
		}
		

		
		

		if (!LocalData.cashMapValue.get(cashRemit).equals("-")) {
			holder.fincCode.setText(code
					+ LocalData.cashMapValue.get(cashRemit));
		} else {
			holder.fincCode.setText(code);
		}

		holder.fincBalance.setText(StringUtil.parseStringCodePattern(
				currencyCode, availableBalance, 2));
		holder.fincBalance.setTextColor(context.getResources().getColor(
				R.color.red));
		// PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
		// holder.fincBalance);
		// PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
		// holder.fincCode);
		return convertView;
	}

	/**
	 * 内部类--存放控件
	 */
	public final class ViewHolder {
		/** 币种 */
		private TextView fincCode;
		/** 钞汇 */
		// private TextView fincCash;
		/** 可用余额 */
		private TextView fincBalance;

	}

	/**
	 * 剔除 贵金属的持仓
	 * 
	 * @param sourceList
	 * @return
	 */
	private List<Map<String, Object>> reSetlist(
			List<Map<String, Object>> sourceList) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : sourceList) {
			if (!isPrms(map.get(Finc.FINC_CURRENCYCODE_RES))) {
				resultList.add(map);
			}
		}
		return resultList;

	}

	/**
	 * 判断是否是贵金属的持仓
	 * 
	 * @return
	 */
	private boolean isPrms(Object currencyCode) {
		if (currencyCode == null) {
			return false;
		}
		for (String code : LocalData.prmsTradeStyleCodeList) {
			if (((String) currencyCode).endsWith(code)) {
				return true;
			}
		}
		return false;

	}

}
