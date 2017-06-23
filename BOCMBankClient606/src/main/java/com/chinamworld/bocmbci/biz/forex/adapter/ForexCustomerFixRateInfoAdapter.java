package com.chinamworld.bocmbci.biz.forex.adapter;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.biz.forex.customer.ForexCustomerFixRateDatailActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/***
 * 我的外汇首页--定期适配器
 * 
 * @author Administrator
 * 
 */
public class ForexCustomerFixRateInfoAdapter extends BaseAdapter {
	private final String TAG = "ForexCustomerFixRateInfoAdapter";
	private Activity context;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> accInfoList = null;
	// 默认选中第一个
	private int customerSelectedPosition = -1;

	public ForexCustomerFixRateInfoAdapter(Activity context, List<Map<String, Object>> accInfoList) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.accInfoList = accInfoList;
	}

	@Override
	public int getCount() {
		return accInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		return accInfoList.get(position);
	}

	public void changeDate(List<Map<String, Object>> accInfoList) {
		this.accInfoList = accInfoList;
		notifyDataSetChanged();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			LogGloble.d(TAG, "getView");
			convertView = mInflater.inflate(R.layout.forex_customer_fix_listitem, null);
			holder = new ViewHolder();
			holder.customerVolumNumber = (TextView) convertView.findViewById(R.id.forex_custoner_fix_volunber);
			// holder.customercdNumber = (TextView)
			// convertView.findViewById(R.id.forex_custoner_fix_cd);
			holder.customerCode = (TextView) convertView.findViewById(R.id.forex_custoner_fix_code);
			holder.customerBalance = (TextView) convertView.findViewById(R.id.forex_custoner_fix_balance);
			holder.rightArrow = (ImageView) convertView.findViewById(R.id.rate_gotoDetail);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.customerVolumNumber.setTag(position);
		// holder.customercdNumber.setTag(position);
		holder.customerCode.setTag(position);
		holder.customerBalance.setTag(position);
		holder.rightArrow.setTag(position);

		Map<String, Object> termSubAccountMap = accInfoList.get(position);
		String cashRemit = (String) termSubAccountMap.get(Forex.FOREX_CASHREMIT_RES);
		String cash = null;
		if (LocalData.CurrencyCashremit.containsKey(cashRemit)) {
			cash = LocalData.CurrencyCashremit.get(cashRemit);
		}
		String volumeNumber = (String) termSubAccountMap.get(Forex.FOREX_VOLUMENUMBER_RES);
		String cdnumber = (String) termSubAccountMap.get(Forex.FOREX_CDNUMBER_RES);
		Map<String, String> currency = (Map<String, String>) termSubAccountMap.get(Forex.FOREX_CURRENCY_RES);
		String code = currency.get(Forex.FOREX_CURRENCY_CODE_RES);
		String transCode = null;
		if (LocalData.Currency.containsKey(code)) {
			transCode = LocalData.Currency.get(code);

		}
		Map<String, Object> balanceMap = (Map<String, Object>) termSubAccountMap.get(Forex.FOREX_BALANCE_RES);
		String availableBalance = (String) balanceMap.get(Forex.FOREX_AVAILABLEBALANCE_RES);
		holder.customerVolumNumber.setText(volumeNumber + "/" + cdnumber);
		// holder.customercdNumber.setText(cdnumber);
		holder.customerCode.setText(transCode + cash);
		if (StringUtil.isNullOrEmpty(availableBalance)) {
			holder.customerBalance.setText("-");
		} else {
			availableBalance = StringUtil.parseStringCodePattern(code, availableBalance, 2);
			holder.customerBalance.setText(availableBalance);
		}
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				customerSelectedPosition = position;
				Intent intent = new Intent(context, ForexCustomerFixRateDatailActivity.class);
				intent.putExtra(ConstantGloble.FOREX_customerSelectedPosition, customerSelectedPosition);
				context.startActivity(intent);
			}
		});
		holder.rightArrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				customerSelectedPosition = position;
				Intent intent = new Intent(context, ForexCustomerFixRateDatailActivity.class);
				intent.putExtra(ConstantGloble.FOREX_customerSelectedPosition, customerSelectedPosition);
				context.startActivity(intent);
			}
		});

		return convertView;
	}

	/**
	 * 存放控件
	 */
	public final class ViewHolder {

		public TextView customerVolumNumber;

		// public TextView customercdNumber;

		public TextView customerCode;

		public TextView customerBalance;

		public ImageView rightArrow;
	}
}
