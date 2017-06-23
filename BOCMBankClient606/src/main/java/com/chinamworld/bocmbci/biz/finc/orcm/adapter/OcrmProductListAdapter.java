package com.chinamworld.bocmbci.biz.finc.orcm.adapter;

import java.util.List;
import java.util.Map;

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

/**
 * 基金推荐 适配器
 * 
 * @author fsm
 * 
 */
public class OcrmProductListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> list;

	public OcrmProductListAdapter(Context context, List<Map<String, Object>> list) {
		this.context = context;
		this.list = list;
		mInflater = LayoutInflater.from(context);
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
		if(convertView == null){
		   convertView = mInflater.inflate(R.layout.finc_ocrm_list_item,null);
		   holder = new ViewHolder();
		   holder.quickSellIv = (ImageView) convertView.findViewById(R.id.quick_sell_iv);
		   holder.mFundNameTv=(TextView) convertView.findViewById(R.id.finc_listiterm_tv1);
		   holder.mNetValueTv=(TextView) convertView.findViewById(R.id.finc_listiterm_tv2);
		   holder.mTradeAmountTv=(TextView) convertView.findViewById(R.id.finc_listiterm_tv3);
		   convertView.setTag(holder);
		}
		else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.quickSellIv.setVisibility(View.GONE);
		Map<String, Object> map = list.get(position);
		String fundName = (String)map.get(Finc.PRODUCTNAME);
		String charCode = (String) map.get(Finc.FINC_CHARCODE);
//		String netValue = (String) map.get(Finc.FUNDNETVAL);
//		String tradeAmount = (String) map.get(Finc.TRANSSUM);
		String currencyCode = (String) map.get(Finc.I_CURRENCYCODE);
		
		String currency = dispose(charCode , currencyCode);
		
		String fundNetVal = (String) map.get(Finc.FUNDNETVAL);
//		String netPrice="11.442";
//		if(map.containsKey(Finc.FINC_NETPRICE)){
//			 netPrice = (String) map.get(Finc.FINC_NETPRICE);
//		}
		
		holder.mFundNameTv.setText(StringUtil.valueOf1(fundName));
		holder.mNetValueTv.setText(currency);
		holder.mTradeAmountTv.setText(StringUtil.parseStringPattern(fundNetVal, 4));
//		holder.mNetValueTv.setText(StringUtil.parseStringPattern(netValue, 4));
//		holder.mTradeAmountTv.setText(StringUtil.parseStringCodePattern(currencyCode, tradeAmount, 2));
//		holder.mTradeAmountTv.setTextColor(context.getResources().getColor(R.color.red));

		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.mFundNameTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.mNetValueTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.mTradeAmountTv);
		return convertView;
	}
	
	private String dispose(String charCode, String currencyCode){
		if("001".equals(currencyCode)){
			
		}else{
			return  LocalData.Currency.get(currencyCode)+"/"+LocalData.CurrencyCashremitT.get(charCode);
			
		}
		
		return LocalData.Currency.get(currencyCode);
	}
	

	/**
	 * 内部类--存放控件
	 */
	public final class ViewHolder {
		/** 快速赎回标志*/
		public ImageView quickSellIv;
		//基金名称 ，基金净值，交易金额
		private TextView mFundNameTv;
		private TextView mNetValueTv;
		private TextView mTradeAmountTv;
	}

}
