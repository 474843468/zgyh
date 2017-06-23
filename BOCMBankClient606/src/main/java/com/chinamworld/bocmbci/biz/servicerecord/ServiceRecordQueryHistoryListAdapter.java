package com.chinamworld.bocmbci.biz.servicerecord;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 历史交易查询适配器
 * 
 * @author xyl
 * 
 */
public class ServiceRecordQueryHistoryListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> list;

	public ServiceRecordQueryHistoryListAdapter(Context context,
			List<Map<String, Object>> list) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.list = list;
	}

	public void notifyDataSetChanged(List<Map<String, Object>> list) {
		this.list = list;
		notifyDataSetChanged();
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
			convertView = mInflater.inflate(
					R.layout.finc_query_history_list_listiterm, null);
			holder = new ViewHolder();
			convertView.setTag(holder);
			holder.mDealDateTv = (TextView) convertView
					.findViewById(R.id.finc_listiterm_tv1);

			holder.mTransTypeTv = (TextView) convertView
					.findViewById(R.id.finc_listiterm_tv2);
			holder.money = (TextView) convertView
					.findViewById(R.id.finc_listiterm_tv3);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Map<String, Object> map = list.get(position);
		final String amount = (String) map.get(Finc.AMOUNT);
		final String dealDate = (String) map.get(Finc.CREATE_TIME);
		final String transType = (String) map.get(Finc.Svr_Rec_Type);
		String finalAmountString=StringUtil
				.parseStringCodePattern((String) map.get("CurrencyCode"),
						amount, 2);
		holder.mDealDateTv.setText(StringUtil.valueOf1(dealDate));
		if(!StringUtil.isNullOrEmpty(finalAmountString)){
				if(finalAmountString.indexOf(".") < 0){
					
				}else {
					finalAmountString = finalAmountString.replaceAll("0+?$", "");// 去掉多余的0
					finalAmountString = finalAmountString.replaceAll("[.]$", "");// 如最后一位是.则去掉
				}
		}
		holder.money.setText(finalAmountString);
		holder.money.setTextColor(Color.RED);

		final String specialTransFlag = (String) map
				.get(Finc.QUERYHISTORY401_SPECIALTRANSFLAG);// add by fsm
		final String transCode = (String) map
				.get(Finc.QUERYHISTORY401_TRANSCODE);
		final String transTypeResult = (String) map.get(Finc.Svr_Rec_Type);
		// 业务种类？？
		holder.mTransTypeTv.setText(LocalData.service_record
				.get(transTypeResult));

		// holder.mTransTypeTv.setText(StringUtil.valueOf1(LocalData.fincTradeTypeCodeToStr.get(transType)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.mDealDateTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.money);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.mTransTypeTv);
		return convertView;
	}

	/**
	 * 存放控件
	 * 
	 * @author Administrator
	 * 
	 */
	public final class ViewHolder {
		// 日期，业务类型，金额
		public TextView mDealDateTv, mTransTypeTv, money;
	}
}
