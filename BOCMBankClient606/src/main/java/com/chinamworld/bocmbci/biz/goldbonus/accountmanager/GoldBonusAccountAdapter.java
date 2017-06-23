package com.chinamworld.bocmbci.biz.goldbonus.accountmanager;

import java.util.List;
import java.util.Map;

import net.tsz.afinal.core.Arrays;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.GoldBonus;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;

public class GoldBonusAccountAdapter extends BaseAdapter {
	private AccountManagerMainActivity context;
	private List<Map<String, Object>> productList;
	public GoldBonusAccountAdapter(AccountManagerMainActivity context,
			List<Map<String, Object>> productList) {
		super();
		this.context = context;
		this.productList = productList;
	}
	public void datachanged(List<Map<String, Object>> list) {
		this.productList = list;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return productList.size();
	}

	@Override
	public Object getItem(int position) {
		return productList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.goldbouns_positionmessage_listiterm, null);
			viewHolder=new ViewHolder();
			viewHolder.v1=(TextView) convertView.findViewById(R.id.first_item);
			viewHolder.v2=(TextView) convertView.findViewById(R.id.second_item);
			viewHolder.v3=(TextView) convertView.findViewById(R.id.third_item);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		Map<String, Object> map =  (Map<String, Object>) productList.get(position);
		viewHolder.v1.setText((String)map.get(GoldBonus.ISSUENAME));
		viewHolder.v2.setText((String)(map.get(GoldBonus.TRADEDATE)));
		viewHolder.v3.setText((String)map.get(GoldBonus.EXPDATE));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				context, viewHolder.v1);

		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				context, viewHolder.v2);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				context, viewHolder.v3);
		return convertView;
	}
	
	private List<String> getStringArray(int resouseId) {
		return Arrays.asList(context.getResources().getStringArray(resouseId));
	}
	
	public class ViewHolder{
		private TextView v1;
		private TextView v2;
		private TextView v3;
	}
}
