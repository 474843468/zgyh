package com.chinamworld.bocmbci.biz.finc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * 基金行情 适配器
 * 
 * @author xyl
 * 
 */
public class FundPricesListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> list;

	public FundPricesListAdapter(Context context, List<Map<String, Object>> list) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.list = list;
	}

	public void notifyDataSetChanged(List<Map<String, Object>> list) {
		this.list = list;
		super.notifyDataSetChanged();
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
					R.layout.finc_price_list_item, null);
			holder = new ViewHolder();
			convertView.setTag(holder);
			holder.quickSellIv = (ImageView) convertView
					.findViewById(R.id.quick_sell_iv);
//			holder.bondLeftBg = (ImageView) convertView.findViewById(R.id.left_bg);
			holder.mFundNameAndCodeTv = (TextView) convertView
					.findViewById(R.id.tv_fund_name_and_code);
			holder.mNetValueTv = (TextView) convertView
					.findViewById(R.id.tv_fund_net_value);
			holder.mDayIncomeRatioTv = (TextView) convertView
					.findViewById(R.id.tv_day_income_ratio);
			holder.mFundStateTv = (TextView) convertView
					.findViewById(R.id.tv_fund_status);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Map<String, Object> map = list.get(position);
//		if (position % 2 == 0) {
//			holder.bondLeftBg.setBackgroundResource(R.drawable.outlay_left_shencheng);
//		} else {
//			holder.bondLeftBg.setBackgroundResource(R.drawable.outlay_left_qiancheng);
//		}
		boolean isFastSale = StringUtil
				.parseStrToBoolean((String)map.get(Finc.ISFASTSALE));
		final String fundName = (String) map.get(Finc.FINC_FUNDNAME);
		final String fundCode = (String) map.get(Finc.FINC_FUNDCODE);
		final String netValue = (String) map.get(Finc.FINC_NETPRICE);
		 String dayIncomeRatio = (String) map.get(Finc.FINC_DAYINCOMERATIO);
		final String fundState = (String) map.get(Finc.FINC_FUNDSTATE);
		if(isFastSale)
			holder.quickSellIv.setVisibility(View.GONE);
		else
			holder.quickSellIv.setVisibility(View.GONE);
		StringBuffer sb = new StringBuffer();
		sb.append(StringUtil.valueOf1(fundName));
		sb.append("(");
		sb.append(StringUtil.valueOf1(fundCode));
		sb.append(")");
		holder.mFundNameAndCodeTv.setText(sb.toString());
		String netValueStr = StringUtil.parseStringPattern(netValue, 4);
		if ("-".equals(netValueStr)) {
			holder.mNetValueTv.setText(netValueStr);
		} else {
			holder.mNetValueTv.setText(netValueStr + "元");
		}
//		String dayIncomeRatioStr = StringUtil.parseStringPattern(dayIncomeRatio, 2);
		if (dayIncomeRatio == null || "".equals(dayIncomeRatio)) {
			dayIncomeRatio = "-";
		}
		if ("-".equals(dayIncomeRatio)) {
			holder.mDayIncomeRatioTv.setText(dayIncomeRatio);
			holder.mDayIncomeRatioTv.setTextColor(context.getResources().getColor(R.color.outlay_text_color));
		} else {
			if (Double.parseDouble(dayIncomeRatio)>0) {
				holder.mDayIncomeRatioTv.setText("+"+dayIncomeRatio + "%");
//				holder.mDayIncomeRatioTv.setTextColor(context.getResources().getColor(R.color.firebrick));
				holder.mDayIncomeRatioTv.setTextColor(context.getResources().getColor(R.color.outlay_text_color));
			}else if (Double.parseDouble(dayIncomeRatio)<0) {
				holder.mDayIncomeRatioTv.setText(dayIncomeRatio + "%");
//				holder.mDayIncomeRatioTv.setTextColor(context.getResources().getColor(R.color.qianlv));
				holder.mDayIncomeRatioTv.setTextColor(context.getResources().getColor(R.color.outlay_text_color));
			}else {
				holder.mDayIncomeRatioTv.setText("0.00%");
//				holder.mDayIncomeRatioTv.setTextColor(context.getResources().getColor(R.color.dimgray));
				holder.mDayIncomeRatioTv.setTextColor(context.getResources().getColor(R.color.outlay_text_color));
			}
		}
//		if (dayIncomeRatioStr.contains("+")) {
//			holder.mDayIncomeRatioTv.setTextColor(context.getResources().getColor(R.color.firebrick));
//		} else if (dayIncomeRatioStr.contains("-")) {
//			holder.mDayIncomeRatioTv.setTextColor(context.getResources().getColor(R.color.qianlv));
//		} else {
//			holder.mDayIncomeRatioTv.setTextColor(context.getResources().getColor(R.color.dimgray));
//		}
		holder.mFundStateTv.setText(StringUtil.valueOf1(LocalData.fincFundStateCodeToStr.get(fundState)));
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.mFundNameAndCodeTv);
		 PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
		 holder.mNetValueTv);
		 PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				 holder.mDayIncomeRatioTv);
		 PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
		 holder.mFundStateTv);

		return convertView;
	}

	/**
	 * 存放控件
	 * 
	 * @author Administrator
	 * 
	 */
	public final class ViewHolder {
		/** 快速赎回标志*/
		public ImageView quickSellIv;
		/** 基金名称和代码 */
		public TextView mFundNameAndCodeTv;
		/** 单位净值 */
		public TextView mNetValueTv;
		/** 日增长率 */
		public TextView mDayIncomeRatioTv;
		/** 基金状态 */
		public TextView mFundStateTv;
		public ImageView bondLeftBg;
	}

}
