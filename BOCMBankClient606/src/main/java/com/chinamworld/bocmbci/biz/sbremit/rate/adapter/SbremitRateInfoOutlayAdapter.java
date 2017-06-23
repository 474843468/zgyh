package com.chinamworld.bocmbci.biz.sbremit.rate.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.SBRemit;
import com.chinamworld.bocmbci.biz.sbremit.rate.Price_DetailActivity;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 结汇购汇牌价列表(未登录外汇牌价)Adapter
 * 
 */
public class SbremitRateInfoOutlayAdapter extends BaseAdapter {

	private List<Map<String, Object>> data;
	private Context mContext;
	private OnItemClickListener itemClickListener;
	private boolean isLogin;
	private String curCode;

	public SbremitRateInfoOutlayAdapter(Context mContext,
			List<Map<String, Object>> data, boolean isLogin) {
		this.mContext = mContext;
		this.data = data;
		this.isLogin = isLogin;
	}

	public void changeData(List<Map<String, Object>> data, boolean isLogin) {
		this.data = data;
		this.isLogin = isLogin;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.sbremit_quotations_list_item, null);
			holder.currency_name = (TextView) convertView
					.findViewById(R.id.currency_name_tv);
			holder.buy_rate = (TextView) convertView
					.findViewById(R.id.buy_rate_tv);
			holder.sell_rate = (TextView) convertView
					.findViewById(R.id.sell_rate_tv);
			holder.buy_note_rate = (TextView) convertView
					.findViewById(R.id.buy_note_rate_tv);
			// holder.sell_note_rate = (TextView)
			// convertView.findViewById(R.id.sell_note_rate_tv);
			holder.arrow = (ImageButton) convertView.findViewById(R.id.arrow);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Map<String, Object> map = data.get(position);

		if (position % 2 == 0) {
			convertView.setBackgroundColor(mContext.getResources().getColor(
					R.color.white));
			holder.arrow.setBackgroundColor(mContext.getResources().getColor(
					R.color.white));
		} else {
			convertView.setBackgroundColor(mContext.getResources().getColor(
					R.color.outlay_one_bg));
			holder.arrow.setBackgroundColor(mContext.getResources().getColor(
					R.color.outlay_one_bg));
		}

		if (isLogin) {
			curCode = (String) map.get(SBRemit.CURRENCY);
		} else {
			curCode = (String) map.get(SBRemit.CUR_CODE);
		}

		holder.arrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				BaseHttpEngine.showProgressDialog();
				Intent intent = new Intent(mContext, Price_DetailActivity.class);
				if (isLogin) {
					intent.putExtra(SBRemit.CURRENCY,
							(String) map.get(SBRemit.CURRENCY));
				} else {
					intent.putExtra(SBRemit.CURRENCY,
							(String) map.get(SBRemit.CUR_CODE));
				}
				intent.putExtra(
						SBRemit.BUY_RATE,
						map.get(SBRemit.BUY_RATE) == null ? "" : map.get(
								SBRemit.BUY_RATE).toString());

				intent.putExtra(
						SBRemit.SELL_RATE,
						map.get(SBRemit.SELL_RATE) == null ? "" : map.get(
								SBRemit.SELL_RATE).toString());

				intent.putExtra(
						SBRemit.BUY_NOTE_RATE,
						map.get(SBRemit.BUY_NOTE_RATE) == null ? "" : map.get(
								SBRemit.BUY_NOTE_RATE).toString());

				intent.putExtra(
						SBRemit.SELL_NOTE_RATE,
						map.get(SBRemit.SELL_NOTE_RATE) == null ? "" : map.get(
								SBRemit.SELL_NOTE_RATE).toString());

				mContext.startActivity(intent);
				BaseHttpEngine.showProgressDialog();
			}
		});

		holder.currency_name.setText(LocalData.moneyTypeValueKey.get(curCode));

		setViews(holder.buy_rate, map.get(SBRemit.BUY_RATE), SBRemit.BUY_RATE,
				position);
		setViews(holder.sell_rate, map.get(SBRemit.SELL_RATE),
				SBRemit.SELL_RATE, position);
		setViews(holder.buy_note_rate, map.get(SBRemit.BUY_NOTE_RATE),
				SBRemit.BUY_NOTE_RATE, position);
		// setViews(holder.sell_note_rate, map.get(SBRemit.SELL_NOTE_RATE) ,
		// SBRemit.SELL_NOTE_RATE, position);

		return convertView;
	}

	private void setViews(TextView tv, Object data, String tag,
			final int position) {
		tv.setTextColor(mContext.getResources().getColor(R.color.black));

		// if (StringUtil.isNull(data)) {
		// tv.setText("-");
		// tv.setTextColor(mContext.getResources().getColor(R.color.black));
		// } else {
		// tv.setText(StringUtil.parseStringPattern((String)data, 2));
		tv.setText(StringUtil.valueisNotNullAndZero((String) data));
		// tv.setTextColor(mContext.getResources().getColor(R.color.red));
		tv.setTag(tag);
		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (itemClickListener != null) {
					itemClickListener.onItemClick(null, v, position, position);
				}
			}
		});
		// }
	}

	protected class ViewHolder {
		/** 币种名称 */
		protected TextView currency_name;
		/** 银行现汇买入价 */
		protected TextView buy_rate;
		/** 银行现汇卖出价 */
		protected TextView sell_rate;
		/** 银行现钞买入价 */
		protected TextView buy_note_rate;
		/** 银行现钞卖出价 */
		// protected TextView sell_note_rate;
		/** 箭头 */
		protected ImageButton arrow;
	}

	public void setItemClickListener(OnItemClickListener itemClickListener) {
		this.itemClickListener = itemClickListener;
	}

}
