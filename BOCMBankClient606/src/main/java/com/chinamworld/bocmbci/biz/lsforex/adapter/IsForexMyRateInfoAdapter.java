package com.chinamworld.bocmbci.biz.lsforex.adapter;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.myrate.IsForexMyRateInfoDetailActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 我的外汇双向宝首页 适配器 */
public class IsForexMyRateInfoAdapter extends BaseAdapter {
	private final String TAG = "IsForexMyRateInfoAdapter";
	private Activity context;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> accInfoList = null;
	// 默认选中第一个
	private int customerSelectedPosition = -1;

	public IsForexMyRateInfoAdapter(Activity context, List<Map<String, Object>> accInfoList) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.accInfoList = accInfoList;
	}

	@Override
	public int getCount() {
		return accInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		return accInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.isforex_myrate_main_item, null);
			holder = new ViewHolder();
			holder.jcCodeText = (TextView) convertView.findViewById(R.id.forex_custoner_fix_volunber);
			holder.codeText = (TextView) convertView.findViewById(R.id.forex_custoner_fix_cd);
			holder.sellTagText = (TextView) convertView.findViewById(R.id.forex_custoner_fix_code);
			holder.rightArrow = (ImageView) convertView.findViewById(R.id.rate_gotoDetail);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.jcCodeText.setTag(position);
		holder.codeText.setTag(position);
		holder.sellTagText.setTag(position);
		holder.rightArrow.setTag(position);
		Map<String, Object> map = accInfoList.get(position);
		String settle = (String) map.get(ConstantGloble.ISFOREX_SETTLE);
		// 结算币种
		String settleCode = null;
		if (!StringUtil.isNull(settle) && LocalData.Currency.containsKey(settle)) {
			settleCode = LocalData.Currency.get(settle);
		}
		Map<String, Object> detailsMap = (Map<String, Object>) map.get(ConstantGloble.ISFOREX_DETAILSMAP);

		Map<String, String> currency1 = (Map<String, String>) detailsMap.get(IsForex.ISFOREX_CURRENCY1_RES);
		Map<String, String> currency2 = (Map<String, String>) detailsMap.get(IsForex.ISFOREX_CURRENCY2_RES);
		String code1 = currency1.get(IsForex.ISFOREX_CODE_RES);
		String code2 = currency2.get(IsForex.ISFOREX_CODE_RES);
		String code = null;
		if (!StringUtil.isNull(code1) && LocalData.Currency.containsKey(code1) && !StringUtil.isNull(code2)
				&& LocalData.Currency.containsKey(code2)) {
			code1 = LocalData.Currency.get(code1);
			code2 = LocalData.Currency.get(code2);
			code = code1 + "/" + code2;
		}
		String direction = (String) detailsMap.get(IsForex.ISFOREX_DIRECTION_RES);
		if (!StringUtil.isNull(direction) && LocalData.isForexdirectionMap.containsKey(direction)) {
			direction = LocalData.isForexdirectionMap.get(direction);
		}
		holder.jcCodeText.setText(settleCode);
		holder.codeText.setText(code);
		holder.sellTagText.setText(direction);

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				customerSelectedPosition = position;
				Intent intent = new Intent(context, IsForexMyRateInfoDetailActivity.class);
				intent.putExtra(ConstantGloble.ISFOREX_SELECTEDPOSITION, customerSelectedPosition);
				context.startActivity(intent);
			}
		});
		holder.rightArrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				customerSelectedPosition = position;
				Intent intent = new Intent(context, IsForexMyRateInfoDetailActivity.class);
				intent.putExtra(ConstantGloble.ISFOREX_SELECTEDPOSITION, customerSelectedPosition);
				context.startActivity(intent);
			}
		});

		return convertView;
	}

	/**
	 * 存放控件ISFOREX_CODE_RES
	 */
	public final class ViewHolder {

		public TextView jcCodeText;

		public TextView codeText;

		public TextView sellTagText;

		public ImageView rightArrow;
	}
}
