package com.chinamworld.bocmbci.biz.prms.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 查询当前委托交易适配器，同样适用于委托交易查询
 * 
 * @author xyl
 * 
 */
public class PrmsQueryEntrustNowListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> list;

	public PrmsQueryEntrustNowListAdapter(Context context,
			List<Map<String, Object>> list) {
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
					R.layout.finc_query_entrust_now_list_item, null);
			holder = new ViewHolder();
			holder.consignNumTv = (TextView) convertView
					.findViewById(R.id.finc_listiterm_tv1);
			holder.buyCurrency = (TextView) convertView
					.findViewById(R.id.finc_listiterm_tv2);
			holder.saleCurrency = (TextView) convertView
					.findViewById(R.id.finc_listiterm_tv3);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Map<String, Object> map = list.get(position);

		// 委托序号
		final String consignNumber = (String) map
				.get(Prms.PRMS_QUERY_DEAL_CONSIGNNUMBER);
		// 买入币种
		final Map<String, String> firstBuyCurrency = (Map<String, String>) map
				.get(Prms.PRMS_QUERY_DEAL_FIRSTBUYCURRENCY);
		// 卖出币种
		final Map<String, String> firstSaleCurrency = (Map<String, String>) map
				.get(Prms.PRMS_QUERY_DEAL_FIRSTSELLCURRENCY);


		final String buyCurrency = firstBuyCurrency
				.get(Prms.PRMS_QUERY_DEAL_CODE);
		final String saleCurrency = firstSaleCurrency
				.get(Prms.PRMS_QUERY_DEAL_CODE);

		holder.saleCurrency.setText(StringUtil.valueOf1(LocalData.Currency
				.get(saleCurrency)));
		holder.buyCurrency.setText(StringUtil.valueOf1(LocalData.Currency
				.get(buyCurrency)));
		holder.consignNumTv.setText(consignNumber);

		return convertView;
	}

	/**
	 * 存放控件
	 * 
	 * @author Administrator
	 * 
	 */
	public final class ViewHolder {
		public TextView consignNumTv;// 委托序号
		public TextView buyCurrency;// 买入币种
		public TextView saleCurrency;// 卖出币种
	}

}
