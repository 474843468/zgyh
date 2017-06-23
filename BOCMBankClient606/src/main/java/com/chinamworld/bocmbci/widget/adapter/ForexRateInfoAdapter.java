package com.chinamworld.bocmbci.widget.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;

/** 外汇行情适配器 */
public class ForexRateInfoAdapter extends BaseAdapter {
	private final String TAG = "ForexRateInfoAdapter";
	private Context context = null;
	private List<Map<String, Object>> list = null;
	/** 银行卖价按钮事件 */
	private OnItemClickListener sellImgOnItemClickListener = null;
	/** 银行买价按钮事件 */
	private OnItemClickListener buyImgOnItemClickListener = null;

	public OnItemClickListener getSellImgOnItemClickListener() {
		return sellImgOnItemClickListener;
	}

	public void setSellImgOnItemClickListener(OnItemClickListener sellImgOnItemClickListener) {
		this.sellImgOnItemClickListener = sellImgOnItemClickListener;
	}

	public OnItemClickListener getBuyImgOnItemClickListener() {
		return buyImgOnItemClickListener;
	}

	public void setBuyImgOnItemClickListener(OnItemClickListener buyImgOnItemClickListener) {
		this.buyImgOnItemClickListener = buyImgOnItemClickListener;
	}

	public ForexRateInfoAdapter(Context context, List<Map<String, Object>> list) {
		this.context = context;
		this.list = list;
	}

	/**
	 * 更新银行卖价、银行买价
	 * 
	 * @param list
	 */
	public void dataChaged(List<Map<String, Object>> list) {
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
			LogGloble.d(TAG, "getView");
			convertView = LayoutInflater.from(context).inflate(R.layout.forex_rate_main_item, null);
			holder = new ViewHolder();
			holder.sourceCode = (TextView) convertView.findViewById(R.id.rate_sourceCurCde);
			holder.buyRate = (TextView) convertView.findViewById(R.id.rate_buy_img);
			holder.sellRate = (TextView) convertView.findViewById(R.id.rate_sell_img);
			holder.button = (ImageView) convertView.findViewById(R.id.rate_gotoDetail);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(position%2!=0){
			convertView.setBackgroundColor(Color.parseColor("#f2f2f2"));
		}else{
			convertView.setBackgroundColor(Color.parseColor("#ffffff"));
		}
		Map<String, Object> map = (Map<String, Object>) list.get(position);
		holder.sourceCode.setTag(position);
		holder.buyRate.setTag(position);
		holder.button.setTag(position);
		holder.sellRate.setTag(position);
		// 得到源货币的代码
		String sourceCurrencyCode = (String) map.get(Forex.FOREX_RATE_SOURCECODE_RES);
		String sourceDealCode = null;
		if (LocalData.Currency.containsKey(sourceCurrencyCode)) {
			sourceDealCode = LocalData.Currency.get(sourceCurrencyCode);

		}
		/**
		 * 得到目标货币代码
		 */
		String targetCurrencyCode = (String) map.get(Forex.FOREX_RATE_TARGETCODE_RES);
		String targetDealCode = null;
		if (LocalData.Currency.containsKey(targetCurrencyCode)) {
			targetDealCode = LocalData.Currency.get(targetCurrencyCode);

		}
		StringBuilder sb = new StringBuilder(sourceDealCode);
		sb.append("/");
		sb.append(targetDealCode);
		holder.sourceCode.setText(sb.toString().trim());
		/**
		 * X-FUND涨跌标志
		 */
		String flag = (String) map.get(Forex.FOREX_RATE_FLAG_RES);
		/**
		 * 根据涨跌标志，设置买入牌价、卖出牌价的背景图片 图片需重新修改
		 */
		int red = context.getResources().getColor(R.color.red);
		int black = context.getResources().getColor(R.color.gray_title);
		int green = context.getResources().getColor(R.color.greens);
		if (flag.equals(ConstantGloble.FOREX_FLAG0)) {
			holder.buyRate.setTextColor(black);
			holder.sellRate.setTextColor(black);
		} else if (flag.equals(ConstantGloble.FOREX_FLAG1)) {
			holder.buyRate.setTextColor(red);
			holder.sellRate.setTextColor(red);
		} else if (flag.equals(ConstantGloble.FOREX_FLAG2)) {
			holder.buyRate.setTextColor(green);
			holder.sellRate.setTextColor(green);
		}

		String buyRate = (String) map.get(Forex.FOREX_RATE_BUYRATE_RES);
		holder.buyRate.setText(buyRate);

		String sellRate = (String) map.get(Forex.FOREX_RATE_SELLRATE_RES);
		holder.sellRate.setText(sellRate);

		/**
		 * 买入按钮事件
		 */
		holder.buyRate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (buyImgOnItemClickListener != null) {
					buyImgOnItemClickListener.onItemClick(null, v, position, position);

				}
			}
		});
		/**
		 * 卖出按钮事件
		 */

		holder.sellRate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sellImgOnItemClickListener != null) {
					sellImgOnItemClickListener.onItemClick(null, v, position, position);

				}
			}
		});

		/**
		 * 点击右边的图片按钮，跳转到外汇详情页面
		 */
		holder.button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		// listView的监听事件
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		return convertView;
	}

	/**
	 * 内部类--控件
	 */
	public final class ViewHolder {
		/**
		 * 货币对
		 */
		public TextView sourceCode = null;
		/**
		 * buyRate：买价
		 */
		public TextView buyRate;
		/**
		 * sellRate：卖价
		 */
		public TextView sellRate;
		/**
		 * button:进入详情页面
		 */
		public ImageView button;
	}
}