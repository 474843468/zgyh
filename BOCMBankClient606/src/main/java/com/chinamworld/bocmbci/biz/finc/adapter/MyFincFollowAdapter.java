package com.chinamworld.bocmbci.biz.finc.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 我关注的基金 */
public class MyFincFollowAdapter extends BaseAdapter {
	private final String TAG = "MyFincFollowAdapter";
	private Context context;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> infoList = null;

	private OnItemClickListener convertViewItermClicListener;

	public MyFincFollowAdapter(Context context,
			List<Map<String, Object>> infoList) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.infoList = infoList;
	}

	public OnItemClickListener getConvertViewItermClicListener() {
		return convertViewItermClicListener;
	}

	public void setConvertViewItermClicListener(
			OnItemClickListener convertViewItermClicListener) {
		this.convertViewItermClicListener = convertViewItermClicListener;
	}
	public void notifyDataSetChanged(List<Map<String, Object>> infoList) {
		this.infoList = infoList;
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return infoList.size();
	}

	@Override
	public Object getItem(int position) {
		return infoList.get(position);
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
					R.layout.finc_myfinc_follow_listitem, null);
			holder = new ViewHolder();
			holder.quickSellIv = (ImageView) convertView
					.findViewById(R.id.quick_sell_iv);
			holder.mFundNameTv = (TextView) convertView
					.findViewById(R.id.finc_listiterm_tv1);
			holder.mNetvalueTv = (TextView) convertView
					.findViewById(R.id.finc_listiterm_tv2);
			holder.mDayNetvalueRateTv = (TextView) convertView
					.findViewById(R.id.finc_listiterm_tv3);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mFundNameTv.setTag(position);
		holder.mNetvalueTv.setTag(position);
		holder.mDayNetvalueRateTv.setTag(position);
		Map<String, Object> map = infoList.get(position);
		String fundName = (String) map
				.get(Finc.FINC_ATTENTIONQUERYLIST_FUNDSHORTNAME);
		String netValue = (String) map
				.get(Finc.FINC_ATTENTIONQUERYLIST_NETPRICE);
		String dayincomeratio = (String) map
				.get(Finc.FINC_ATTENTIONQUERYLIST_DAYINCOMERATIO);
		holder.mFundNameTv.setText(StringUtil.valueOf1(fundName));
		holder.mNetvalueTv.setText(StringUtil.valueOf1(netValue));
		holder.mDayNetvalueRateTv.setText(StringUtil.valueOf1(dayincomeratio));
		
		setQuickSellFlag(map, holder);
		
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (convertViewItermClicListener != null) {
					convertViewItermClicListener.onItemClick(null, v,
							position, position);
				}
			}
		});
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.mFundNameTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.mNetvalueTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.mDayNetvalueRateTv);
		return convertView;
	}
	
	/**
	 * 设置快速赎回标志
	 * @param map 关注列表项信息
	 * @param holder
	 */
	void setQuickSellFlag(Map<String, Object> map, ViewHolder holder){
		Map<String, String> fundInfoMap = (Map<String, String>) map
				.get(Finc.FINC_FUNDINFO_REQ);
		final boolean isFastSale = fundInfoMap == null ? false : StringUtil
				.parseStrToBoolean((String)fundInfoMap.get(Finc.ISFASTSALE));
		if(isFastSale)
			holder.quickSellIv.setVisibility(View.VISIBLE);
		else
			holder.quickSellIv.setVisibility(View.INVISIBLE);
	}

	/**
	 * 存放控件
	 */
	public final class ViewHolder {
		/** 快速赎回标志*/
		public ImageView quickSellIv;
		//基金名称,单位净值,日净值增长率
		public TextView mFundNameTv;

		public TextView mNetvalueTv;

		public TextView mDayNetvalueRateTv;

	}
}
