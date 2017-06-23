package com.chinamworld.bocmbci.biz.finc.adapter;

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
import com.chinamworld.bocmbci.biz.finc.FincUtils;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 基金对账单适配器
 * 
 * @author fsm
 * 
 */
public class FincFundTransSactionAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> list;

	public FincFundTransSactionAdapter(Context context, List<Map<String, Object>> list) {
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
		   holder.mFundNameTv = (TextView) convertView.findViewById(R.id.finc_listiterm_tv1);
		   holder.mFundTranTypeTv = (TextView) convertView.findViewById(R.id.finc_listiterm_tv2);
		   holder.mTrsStatusTv = (TextView) convertView.findViewById(R.id.finc_listiterm_tv3);
		   convertView.setTag(holder);
		}
		else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.quickSellIv.setVisibility(View.GONE);
		Map<String, Object> map = list.get(position);
		String fundName = (String)map.get(Finc.I_FUNDNAME);
		String fundTranType = (String)map.get(Finc.FINC_FUNDTODAYQUERY_FUNDTRANTYPE);
		String trsStatus = (String)map.get(Finc.TRSSTATUS);
		holder.mFundNameTv.setText(StringUtil.valueOf1(fundName));
		holder.mFundTranTypeTv.setText(StringUtil.valueOf1((String)LocalData.fundTranTypeMap.get(fundTranType)));
		holder.mTrsStatusTv.setText(StringUtil.valueOf1((String)LocalData.fincHistoryStatusTypeCodeToStr.get(trsStatus)));
		
		FincUtils.setOnShowAllTextListener(context, holder.mFundNameTv, holder.mFundTranTypeTv, holder.mTrsStatusTv);
		return convertView;
	}
	
	public void setDatas(List<Map<String, Object>> list){
		this.list = list;
		notifyDataSetChanged();
	}
	
	/**
	 * 内部类--存放控件
	 */
	public final class ViewHolder {
		/** 快速赎回标志*/
		public ImageView quickSellIv;
		//基金名称 ，交易状态， 交易类型
		private TextView mFundNameTv;
		private TextView mFundTranTypeTv;
		private TextView mTrsStatusTv;
	}

}
