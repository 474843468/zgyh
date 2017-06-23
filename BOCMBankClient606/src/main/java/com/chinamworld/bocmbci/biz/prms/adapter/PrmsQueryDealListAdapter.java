package com.chinamworld.bocmbci.biz.prms.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.prms.control.PrmsControl;
import com.chinamworld.bocmbci.biz.prms.query.PrmsQueryDealDetailsActivity;
import com.chinamworld.bocmbci.constant.LocalData;

/**
 * 查询历史交易适配器，同样适用于委托交易查询
 * 
 * @author xyl
 * 
 */
public class PrmsQueryDealListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> list;

	public PrmsQueryDealListAdapter(Context context,
			List<Map<String, Object>> list) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	public void notifyDataSetChanged(List<Map<String, Object>> list) {
		this.list = list;
		notifyDataSetChanged();
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
			holder.buyCurrency = (TextView) convertView
					.findViewById(R.id.finc_listiterm_tv1);
			holder.saleCurrency = (TextView) convertView
					.findViewById(R.id.finc_listiterm_tv2);
			holder.tradePrice = (TextView) convertView
					.findViewById(R.id.finc_listiterm_tv3);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Map<String, Object> map = list.get(position);
		

		// 委托序号
		final String consignNumber = (String) map
				.get(Prms.PRMS_QUERY_DEAL_CONSIGNNUMBER);
		// 交易账户
		final String tradeAcc = PrmsControl.getInstance().accNum;
		// 买入币种
		final Map<String, String> firstBuyCurrency = (Map<String, String>) map
				.get(Prms.PRMS_QUERY_DEAL_FIRSTBUYCURRENCY);
		// 卖出币种
		final Map<String, String> firstSaleCurrency = (Map<String, String>) map
				.get(Prms.PRMS_QUERY_DEAL_FIRSTSELLCURRENCY);

		// 钞汇标志
		final String cashRemit = (String) map
				.get(Prms.PRMS_QUERY_DEAL_CASHREMIT);
		// 买入数量
		// +LocalData.prmsUnitMaptoChi.get(map.get(Prms.PRMS_QUERY_DEAL_FIRSTBUYCURRENCY));
		final String buyNum = (String) map
				.get(Prms.PRMS_QUERY_DEAL_FIRSTBUYAMOUNT);// TODO 接口不确定
		final String saleNum = (String) map
				.get(Prms.PRMS_QUERY_DEAL_FIRSTSELLAMOUNT);// TODO 接口不确定
		// 成交价格
		// final String tradePrice = (String) map
		// .get(Prms.PRMS_QUERY_DEAL_FIRSTRATE);

		// 成交时间
		final String realTransTime = (String) map
				.get(Prms.PRMS_QUERY_DEAL_REALTRANSTIME);
		// 成交类型
		final String exchangeTranType = (String) map
				.get(Prms.PRMS_QUERY_DEAL_EXCHANGETRANTYPE);

		final String buyCurrency = firstBuyCurrency
				.get(Prms.PRMS_QUERY_DEAL_CODE);
		final String saleCurrency = firstSaleCurrency
				.get(Prms.PRMS_QUERY_DEAL_CODE);
		final String tradePrice ;
		if(map.get(Prms.PRMS_QUERY_DEAL_SECONDSTATUS) != null &&
				map.get(Prms.PRMS_QUERY_DEAL_SECONDSTATUS).equals(Prms.C_ENTRUSTPRICE_TYPE_S)){
			tradePrice = (String) map
					.get(Prms.PRMS_QUERY_DEAL_SECONDRATE);
		}else {
			tradePrice = (String) map
					.get(Prms.PRMS_QUERY_DEAL_FIRSTRATE);
		}
		holder.saleCurrency.setText(LocalData.prmsTradeStyleMaptoChi
				.get(saleCurrency));
		holder.buyCurrency.setText(LocalData.prmsTradeStyleMaptoChi
				.get(buyCurrency));
		holder.tradePrice.setTextColor(context.getResources().getColor(
				R.color.red));
		holder.tradePrice.setText(tradePrice);
		convertView.setOnClickListener(new OnClickListener() {// 整条数据点击有效
					// holder.arrow.setOnClickListener(new OnClickListener()
					// {//只有箭头点击有效
					@Override
					public void onClick(View v) {
						PrmsControl.getInstance().queryDealMap = map;
						Intent i = new Intent();
						i.putExtra(Prms.PRMS_CONSIGNNUMBER, consignNumber);
						i.putExtra(Prms.PRMS_ACCOUNTNUM, tradeAcc);
						i.putExtra(Prms.PRMS_FIRSTBUYCURRENCY, buyCurrency);
						i.putExtra(Prms.PRMS_FIRSTSALECURRENCY, saleCurrency);
						i.putExtra(Prms.PRMS_CASHREMIT, cashRemit);
						i.putExtra(Prms.PRMS_BUYNUM, buyNum);
						i.putExtra(Prms.PRMS_SALENUM, saleNum);
						i.putExtra(Prms.PRMS_TRADEPRICE, tradePrice);
						i.putExtra(Prms.PRMS_REALTRANSTIME, realTransTime);
						i.putExtra(Prms.PRMS_EXCHANGETRANTYPE, exchangeTranType);

						i.setClass(context, PrmsQueryDealDetailsActivity.class);
						context.startActivity(i);
					}
				});

		return convertView;
	}

	/**
	 * 存放控件
	 * 
	 * @author Administrator
	 * 
	 */
	public final class ViewHolder {
		public TextView buyCurrency;// 买入币种
		public TextView saleCurrency;// 卖出币种
		public TextView tradePrice;// 成交价格
	}

}
