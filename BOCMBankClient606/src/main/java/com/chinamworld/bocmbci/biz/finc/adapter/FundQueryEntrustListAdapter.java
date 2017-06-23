package com.chinamworld.bocmbci.biz.finc.adapter;

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
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 基金当日交易查询 适配器
 * 
 * @author xyl
 * 
 */
public class FundQueryEntrustListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> list;

	public FundQueryEntrustListAdapter(Context context,
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
			holder.mEntrustDateTv = (TextView) convertView
					.findViewById(R.id.finc_listiterm_tv1);
			holder.mFundNameTv = (TextView) convertView
					.findViewById(R.id.finc_listiterm_tv2);
			holder.mTradeTypeTv = (TextView) convertView
					.findViewById(R.id.finc_listiterm_tv3);
			// holder.arrowTextView = (TextView) convertView
			// .findViewById(R.id.finc_rightarrrow);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Map<String, Object> map = list.get(position);
		final String entrustDate = (String) map
				.get(Finc.FINC_FUNDTODAYQUERY_PAYMENTDATE);
		final String fundName = (String) map
				.get(Finc.FINC_FUNDTODAYQUERY_FUNDNAME);
		
		//交易类型  transType
		final String tradeTypeCode = (String) map
				.get(Finc.FINC_FUNDTODAYQUERY_FUNDTRANTYPE);
		holder.mEntrustDateTv.setText(StringUtil.valueOf1(entrustDate));
		holder.mFundNameTv.setText(StringUtil.valueOf1(fundName));
		
		final String specialTransFlag = (String) map.get(Finc.QUERYHISTORY401_SPECIALTRANSFLAG);
		final String fundTranType = (String) map.get(Finc.FINC_FUNDTODAYQUERY_FUNDTRANTYPE);
		final String transTypeResult = FincControl.parseSpeCodeTradeTypeEffective(specialTransFlag, fundTranType);
		holder.mTradeTypeTv.setText(StringUtil.valueOf1(transTypeResult));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.mEntrustDateTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.mFundNameTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.mTradeTypeTv);
		return convertView;
	}

	/**
	 * 存放控件
	 * 
	 * @author Administrator
	 * 
	 */
	public final class ViewHolder {
		//委托日期，基金名称，交易类型
		public TextView mEntrustDateTv,mFundNameTv,mTradeTypeTv;
	}

}
