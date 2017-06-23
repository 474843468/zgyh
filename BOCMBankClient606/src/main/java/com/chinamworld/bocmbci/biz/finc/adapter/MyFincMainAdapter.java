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
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * 基金持仓首页 适配器
 * 
 * @author 宁焰红
 * 
 */
public class MyFincMainAdapter extends BaseAdapter {
	private final String TAG = "MyFincMainAdapter";
	private Context context;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> list;

	public MyFincMainAdapter(Context context, List<Map<String, Object>> list) {
		this.context = context;
		this.list = list;
		mInflater = LayoutInflater.from(context);
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
		   convertView = mInflater.inflate(R.layout.finc_balance_list_item,null);
		   holder = new ViewHolder();
		   holder.quickSellIv = (ImageView) convertView.findViewById(R.id.quick_sell_iv);
		   holder.mFundNameTv=(TextView) convertView.findViewById(R.id.finc_listiterm_tv1);
		   holder.mAvailableBalanceTv=(TextView) convertView.findViewById(R.id.finc_listiterm_tv2);
		   holder.mNetValueTv=(TextView) convertView.findViewById(R.id.finc_listiterm_tv3);
		   convertView.setTag(holder);
		}
		else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.mFundNameTv.setTag(position);
		holder.mAvailableBalanceTv.setTag(position);
		holder.mNetValueTv.setTag(position);
		Map<String, Object> map = list.get(position);
		String availableBalance = (String) map.get(Finc.FINC_TOTALAVAILABLEBALANCE);
		Map<String,String> fundInfoMap = (Map<String, String>) map.get(Finc.FINC_FUNDINFO_REQ);
		String netValue = fundInfoMap == null ? "" : (String) fundInfoMap.get(Finc.FINC_NETPRICE);
		String fundName = fundInfoMap == null ? "" : fundInfoMap.get(Finc.FINC_FUNDNAME_REQ);
		holder.mFundNameTv.setText(StringUtil.valueOf1(fundName));
		holder.mAvailableBalanceTv.setText(StringUtil.parseStringPattern(availableBalance, 2));
		holder.mNetValueTv.setText(StringUtil.parseStringPattern(netValue, 4));
		holder.mAvailableBalanceTv.setTextColor(context.getResources().getColor(R.color.red));
		
		setQuickSellFlag(map, holder);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.mFundNameTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.mAvailableBalanceTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.mNetValueTv);
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
			holder.quickSellIv.setVisibility(View.INVISIBLE);
		else
			holder.quickSellIv.setVisibility(View.INVISIBLE);
	}

	/**
	 * 内部类--存放控件
	 */
	public final class ViewHolder {
		/** 快速赎回标志*/
		public ImageView quickSellIv;
		//基金名称 ，可用份额，单位净值
		private TextView mFundNameTv;
		private TextView mAvailableBalanceTv;
		private TextView mNetValueTv;
	}

}
