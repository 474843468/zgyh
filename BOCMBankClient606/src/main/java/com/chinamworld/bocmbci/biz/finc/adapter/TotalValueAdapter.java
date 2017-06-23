package com.chinamworld.bocmbci.biz.finc.adapter;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 基金持仓 累计市值 适配器
 * 
 * @author xyl
 * 
 */
public class TotalValueAdapter extends BaseAdapter {
	private final String TAG = "TotalValueAdapter";
	private Context context;
	private LayoutInflater mInflater;
	private List<Map<String, String>> list;
	private static String CASHFLAG = "cashflag";
	private static String TOTALVALUE = "totalValue";

	public TotalValueAdapter(Context context, List<Map<String, Object>> list) {
		this.context = context;
		this.list = getTotalValueList(list);
		LogGloble.d(TAG, "list"+this.list.size());
		mInflater = LayoutInflater.from(context);
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

	public void notifyDataSetChanged(List<Map<String, Object>> list) {
		this.list = getTotalValueList(list);
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.finc_query_history_list_item, null);
			holder = new ViewHolder();
			holder.currencyCrashRemit = (TextView) convertView
					.findViewById(R.id.finc_listiterm_tv1);
			holder.fincValue = (TextView) convertView
					.findViewById(R.id.finc_listiterm_tv2);
			holder.rubishTv = (TextView) convertView
					.findViewById(R.id.finc_listiterm_tv3);
			holder.im = (ImageView) convertView.findViewById(R.id.right_iv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String, String> map = list.get(position);
		holder.currencyCrashRemit.setText(map.get(CASHFLAG));
		holder.fincValue.setText(StringUtil.parseStringPattern(map.get(TOTALVALUE), 2));
		holder.rubishTv.setVisibility(View.GONE);
		holder.im.setVisibility(View.GONE);
		
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
//				holder.fincValue);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
//				holder.currencyCrashRemit);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
//				holder.rubishTv);
		return convertView;
	}

	/**
	 * 内部类--存放控件
	 */
	public final class ViewHolder {
		/** 币种 */
		private TextView currencyCrashRemit;
		/** 累计市值 */
		private TextView fincValue;
		
		
		private ImageView im;
		private TextView rubishTv;
	}

	private List<Map<String, String>> getTotalValueList(
			List<Map<String, Object>> accbalancList) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (Map<String, Object> map : accbalancList) {
			Map<String, String> fundInfoMap = (Map<String, String>) map
					.get(Finc.FINC_FUNDINFO_REQ);
			String currency = fundInfoMap.get(Finc.FINC_CURRENCY);
			String cashRemit = fundInfoMap.get(Finc.FINC_CASHFLAG);
			Double totalValue;
			if (currency.equals(ConstantGloble.PRMS_CODE_RMB)) {// 币种为人民币时
				boolean isadd = true;
				totalValue = Double.valueOf((String)map
						.get(Finc.FINC_CURRENTCAPITALISATION_REQ));
				for (Map<String, String> resultmap : list) {
					if (resultmap.get(CASHFLAG).equals(
							LocalData.Currency.get(currency))) {// 里面包含人民币
						totalValue += Double.valueOf(resultmap.get(TOTALVALUE));
						isadd = false;
						resultmap.remove(TOTALVALUE);
						resultmap.put(TOTALVALUE, String.valueOf(totalValue));
					} 
				}
				if(isadd){// 如果不包含人民币
					Map<String, String> newMap = new HashMap<String, String>();
					newMap.put(CASHFLAG, LocalData.Currency.get(currency));
					newMap.put(TOTALVALUE, String.valueOf(totalValue));
					list.add(newMap);
				}
			} else {
				boolean isadd = true;
				totalValue = Double.valueOf((String)map
						.get(Finc.FINC_CURRENTCAPITALISATION_REQ));
				for (Map<String, String> resultmap : list) {
					if (resultmap.get(CASHFLAG).equals(
							LocalData.Currency.get(currency)
									+ LocalData.CurrencyCashremit
											.get(cashRemit))) {
						isadd = false;
						totalValue += Double.valueOf(resultmap.get(TOTALVALUE));
						resultmap.remove(TOTALVALUE);
						resultmap.put(TOTALVALUE, String.valueOf(totalValue));
					} 
				}
				if(isadd){// 如果不包含人民币
					isadd = true;
					Map<String, String> newMap = new HashMap<String, String>();
					newMap.put(CASHFLAG, LocalData.Currency.get(currency)
							+ LocalData.CurrencyCashremit.get(cashRemit));
					newMap.put(TOTALVALUE, String.valueOf(totalValue));
					list.add(newMap);
				}
			}
		}
		return list;

	}
}
