package com.chinamworld.bocmbci.biz.lsforex.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 首页主题页面 适配器*/
public class IsForexBailChoiseBailAdapter extends BaseAdapter {
	private final String TAG = "IsForexMyRateInfoAdapter";
	private Context context;
	private List<Map<String, Object>> list = null;
	private OnClickListener onClickListener = null;
	public static int selectedPosition = -1;
	private OnItemClickListener onItemClickListener = null;

	public OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public OnClickListener getOnClickListener() {
		return onClickListener;
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	public IsForexBailChoiseBailAdapter(Context context, List<Map<String, Object>> list) {
		this.context = context;
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

	public void dataChanged(List<Map<String, Object>> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.isforex_bail_choise_item, null);
			holder = new ViewHolder();
			holder.bailNameText = (TextView) convertView.findViewById(R.id.isForex_numberName);
			holder.bailNoText = (TextView) convertView.findViewById(R.id.isForex_number);
			holder.bailCodeText = (TextView) convertView.findViewById(R.id.isforex_bzjAccCode);
			holder.bailMoneyText = (TextView) convertView.findViewById(R.id.isforex_bzjAccMoney);
			holder.bailCodeTextT = (TextView)convertView.findViewById(R.id.isforex_bzjAccCodet);
			holder.bailMoneyTextT = (TextView)convertView.findViewById(R.id.isforex_bzjAccMoneyt);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		selectedPosition = position;
		Map<String, Object> map = list.get(position);
		Map<String, String> settleCurrency = (Map<String, String>) map.get(IsForex.ISFOREX_SETTLECURRENCY1_RES);
		String jsCode = null;
		if (!StringUtil.isNullOrEmpty(settleCurrency)) {
			jsCode = settleCurrency.get(IsForex.ISFOREX_CODE1_RES);
		}
		String settle = null;
		if (!StringUtil.isNull(jsCode) && LocalData.Currency.containsKey(jsCode)) {
			settle = LocalData.Currency.get(jsCode);
		}
//		List<Map<String, Object>> fundList = (List<Map<String,Object>>)map.get(IsForex.ISFOREX_FUNDLIST_RES);


		String money ;
		//现钞汇
		String cashNameC ;
		String cashNameH ;
		cashNameC = (String)map.get("cashBanlance");
//		//现钞汇余额
		if (!StringUtil.isNull(cashNameC)) {
			cashNameC = StringUtil.parseStringCodePattern(jsCode, cashNameC, 2);
		}
		else{
			cashNameC = "-";
		}
		holder.bailCodeText.setText(settle+"现钞");
		holder.bailMoneyText.setText(cashNameC);
		cashNameH = (String) map.get("remitBanlance");
		if (!StringUtil.isNull(cashNameH)) {
			cashNameH = StringUtil.parseStringCodePattern(jsCode, cashNameH, 2);
		}
		else{
			cashNameH = "-";
		}
		holder.bailCodeTextT.setText(settle+"现汇");
		holder.bailMoneyTextT.setText(cashNameH);
		
//		for(int j=0; j<fundList.size(); j++){
//			money = (String)fundList.get(j).get(IsForex.ISFOREX_AMOUNT_RES);
//			cashName = (String)fundList.get(j).get(IsForex.ISFOREX_NOTECASHFLAG_RES);
//
//			//现钞汇余额
//			if (!StringUtil.isNull(money)) {
//				money = StringUtil.parseStringCodePattern(jsCode, money, 2);
//			}
//			else{
//				money = "-";
//			}
//			 if(!StringUtil.isNull(cashName)&& LocalData.CurrencyCashremitT.containsKey(cashName)){
//				cashName = LocalData.CurrencyCashremitT.get(cashName);
//			 }
//			 else {
//				cashName = "";
//			 }
//			 if( j == 0){
//				holder.bailCodeText.setText(settle+cashName);
//				holder.bailMoneyText.setText(money);
//			}
//			 else if(j == 1){
//				holder.bailCodeTextT.setText(settle+cashName);
//				holder.bailMoneyTextT.setText(money);
//			}	
//		}
		String marginAccountNo = (String) map.get(IsForex.ISFOREX_MARGINACCOUNTNO_RES);
		String bailNo = null;
		if (!StringUtil.isNull(marginAccountNo)) {
			bailNo = StringUtil.getForSixForString(marginAccountNo);
		}
		String marginAccountName = (String) map.get(IsForex.ISFOREX_MARGINACCOUNTNAME_RES);
		if (StringUtil.isNull(marginAccountName)) {
			holder.bailNameText.setText("-");
		} else {
			holder.bailNameText.setText(marginAccountName);
		}
		holder.bailNoText.setText(bailNo);
		
		// convertView.setOnClickListener(onClickListener);
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onItemClickListener != null) {
					onItemClickListener.onItemClick(null, v, position, position);
				}
			}
		});
		return convertView;
	}

	final class ViewHolder {
		public TextView bailNameText;
		public TextView bailNoText;
		public TextView bailCodeText;
		public TextView bailMoneyText;
		public TextView bailCodeTextT;
		public TextView bailMoneyTextT;
	}
}
