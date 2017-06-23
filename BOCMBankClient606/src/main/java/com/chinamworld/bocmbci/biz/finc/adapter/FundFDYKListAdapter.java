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
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 浮动盈亏适配器
 * 
 * @author xyl
 * 
 */
public class FundFDYKListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> list;

	public FundFDYKListAdapter(Context context, List<Map<String, Object>> list) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
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
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.finc_query_history_list_listiterm, null);
			holder = new ViewHolder();
			convertView.setTag(holder);
			holder.Tv1 = (TextView) convertView
					.findViewById(R.id.finc_listiterm_tv1);
			holder.Tv2 = (TextView) convertView
					.findViewById(R.id.finc_listiterm_tv2);
			holder.Tv3 = (TextView) convertView
					.findViewById(R.id.finc_listiterm_tv3);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Map<String, Object> map = list.get(position);
		// Map<String,Object> fundInfoMap = (Map<String, Object>)
		// map.get(Finc.FINC_FUNDINFO);
		final String fundCode = (String) map.get(Finc.FINC_FUNDCODE);
		final String fundName = (String) map.get(Finc.FINC_FUNDNAME);
		final String middleFloat = (String) map
				.get(Finc.FINC_FLOATPROFITANDLOSS_MIDDLEFLOAT);
//		holder.Tv1.setText(fundCode == null ? "-" : fundCode);//modi by fsm2013-10-14 9:21:20
//		holder.Tv2.setText(fundName == null ? "-" : fundName);
		holder.Tv1.setText(StringUtil.valueOf1(fundCode));
		holder.Tv2.setText(StringUtil.valueOf1(fundName));
		holder.Tv3.setText(middleFloat);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
//				holder.Tv1);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.Tv2);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
//				holder.Tv3);
		return convertView;
	}

	/**
	 * 存放控件
	 * 
	 * @author Administrator
	 * 
	 */
	public final class ViewHolder {
		/**
		 * 基金代码 1
		 */
		public TextView Tv1;
		/**
		 * 基金名称 2
		 */
		public TextView Tv2;
		/**
		 * 总盈亏 3
		 */
		public TextView Tv3;
	}

}
