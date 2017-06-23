package com.chinamworld.bocmbci.biz.forex.adapter;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 我的外汇首页--活期卖出适配器
 * 
 * @author Administrator
 * 
 */
public class ForexCustomerRateInfoAdapter extends BaseAdapter {
	private final String TAG = "ForexCustomerRateInfoAdapter";
	private Activity context;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> accDealList = null;
	String codeTrans = null;
	String cashTrans = null;
	String availableBalanceTrans = null;
	String cashRemit = null;
	String code = null;
	// 默认选中第一个
	private int customerSelectedPosition = -1;
	/** 卖出事件 */
	private OnItemClickListener customerOnItemClickListener = null;

	public OnItemClickListener getCustomerOnItemClickListener() {
		return customerOnItemClickListener;
	}

	public void setCustomerOnItemClickListener(OnItemClickListener customerOnItemClickListener) {
		this.customerOnItemClickListener = customerOnItemClickListener;
	}

	public ForexCustomerRateInfoAdapter(Activity context, List<Map<String, Object>> accDealList) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.accDealList = accDealList;
	}

	@Override
	public int getCount() {
		return accDealList.size();
	}

	@Override
	public Object getItem(int position) {
		return accDealList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void dateChange(List<Map<String, Object>> accDealList) {
		this.accDealList = accDealList;
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.forex_customer_main_listitem, null);
			holder = new ViewHolder();
			holder.customerCode = (TextView) convertView.findViewById(R.id.forex_accCode_and_cash);
			holder.customerBalance = (TextView) convertView.findViewById(R.id.forex_accBalance);
			holder.customerSellBuuton = convertView.findViewById(R.id.forex_sellButton);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		LayoutParams lp = null;
		int dataSize = position + 1;
		if (getCount() == 1) {
			convertView.setBackgroundResource(R.drawable.prms_img_card_only);
			lp = getButtomLayoutParams();
		} else if (getCount() == 2) {
			if (dataSize == 1) {// 第一个
				convertView.setBackgroundResource(R.drawable.prms_img_card_top);
				lp = getTopLayoutParams();
			} else if (dataSize == 2) {
				convertView.setBackgroundResource(R.drawable.prms_img_card_buttom);
				lp = getButtomLayoutParams();
			}
		} else {
			if (dataSize == 1) {// 第一个
				convertView.setBackgroundResource(R.drawable.prms_img_card_top);
				lp = getTopLayoutParams();
			} else if (dataSize == getCount()) {// 最后一个
				convertView.setBackgroundResource(R.drawable.prms_img_card_buttom);
				lp = getButtomLayoutParams();
			} else {// 中间
				convertView.setBackgroundResource(R.drawable.prms_img_card_mid);
				lp = getMidLayoutParams();
			}
		}
		convertView.setLayoutParams(lp);
		holder.customerCode.setTag(position);
		holder.customerBalance.setTag(position);
		holder.customerSellBuuton.setTag(position);
		Map<String, Object> sellListMap = accDealList.get(position);
		// 钞汇
		cashRemit = (String) sellListMap.get(Forex.FOREX_CASHREMIT_RES);
		if (LocalData.cashMapValue.containsKey(cashRemit)) {
			cashTrans = LocalData.cashMapValue.get(cashRemit);
		}
		Map<String, String> currency = (Map<String, String>) sellListMap.get(Forex.FOREX_CURRENCY_RES);
		// 卖出币种
		code = currency.get(Forex.FOREX_CURRENCY_CODE_RES).trim();
		if (LocalData.Currency.containsKey(code)) {
			codeTrans = LocalData.Currency.get(code);
		} else {
			codeTrans = code;
		}
		if (ConstantGloble.FOREX_RMB_TAG1.equals(code) || ConstantGloble.FOREX_RMB_CNA_TAG2.equals(code)) {
			holder.customerSellBuuton.setVisibility(View.INVISIBLE);
			holder.customerCode.setText(codeTrans);
		} else {
			holder.customerSellBuuton.setVisibility(View.VISIBLE);
			holder.customerCode.setText(codeTrans + "/" + cashTrans);
		}
		Map<String, Object> balance = (Map<String, Object>) sellListMap.get(Forex.FOREX_BALANCE_RES);
		// 可用余额
		availableBalanceTrans = (String) balance.get(Forex.FOREX_AVAILABLEBALANCE_RES);
		if (!StringUtil.isNullOrEmpty(availableBalanceTrans)) {
			double a = Double.valueOf(availableBalanceTrans);
			if (a <= 0) {
				holder.customerSellBuuton.setVisibility(View.INVISIBLE);
			}
			String b = availableBalanceTrans;
			if (!StringUtil.isNull(code)) {
				b = StringUtil.parseStringCodePattern(code, b, 2);
			}
			holder.customerBalance.setText(b);
		} else {
			holder.customerBalance.setText("-");
		}

		// PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
		// holder.customerCode);
		holder.customerSellBuuton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (customerOnItemClickListener != null) {
					customerOnItemClickListener.onItemClick(null, v, position, position);

				}
			}
		});

		return convertView;
	}

	/**
	 * 存放控件
	 */
	public final class ViewHolder {

		public TextView customerCode;
		public TextView customerBalance;

		public View customerSellBuuton;
	}

	/**
	 * 中间卡片的布局尺寸
	 * 
	 * @return
	 */
	private LayoutParams getMidLayoutParams() {
		int height = (int) context.getResources().getDimension(R.dimen.prms_accbalance_mid_height);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, height);
		return lp;
	}

	/**
	 * 底部卡片的布局尺寸
	 * 
	 * @return
	 */
	private LayoutParams getButtomLayoutParams() {
		int height = (int) context.getResources().getDimension(R.dimen.prms_accbalance_buttom_height);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, height);
		return lp;
	}

	/**
	 * 头部卡片的布局尺寸
	 * 
	 * @return
	 */
	private LayoutParams getTopLayoutParams() {
		int height = (int) context.getResources().getDimension(R.dimen.prms_accbalance_top_height);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, height);
		return lp;
	}

}
