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
 * 历史交易查询适配器
 * 
 * @author xyl
 * 
 */
public class FundQueryHistoryListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> list;

	public FundQueryHistoryListAdapter(Context context,
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
			holder.mFundNameTv = (TextView) convertView
					.findViewById(R.id.finc_listiterm_tv2);
			holder.mTransTypeTv = (TextView) convertView
					.findViewById(R.id.finc_listiterm_tv3);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Map<String, Object> map = list.get(position);
		final String fundName = (String) map
				.get(Finc.FINC_FUNDTODAYQUERY_FUNDNAME);
		final String dealDate = (String) map
				.get(Finc.QUERYHISTORY401_TRANSDATE);
		final String transType = (String) map
				.get(Finc.QUERYHISTORY401_TRANSCODE);
		
		holder.mDealDateTv.setText(StringUtil.valueOf1(dealDate));
		holder.mFundNameTv.setText(StringUtil.valueOf1(fundName));
		final String specialTransFlag = (String) map.get(Finc.QUERYHISTORY401_SPECIALTRANSFLAG);//add by fsm
		final String transCode = (String) map.get(Finc.QUERYHISTORY401_TRANSCODE);
		final String transTypeResult = FincControl.parseSpeCodeTradeTypeHistory(specialTransFlag, transCode);
		holder.mTransTypeTv.setText(StringUtil.valueOf1(transTypeResult));
//		holder.mTransTypeTv.setText(StringUtil.valueOf1(LocalData.fincTradeTypeCodeToStr.get(transType)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.mDealDateTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.mFundNameTv);
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
		//交易日期，基金名称，交易类型
		public TextView mDealDateTv,mFundNameTv,mTransTypeTv;
	}
}
