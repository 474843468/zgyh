package com.chinamworld.bocmbci.biz.goldbonus.goldbonusoutside;

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

public class GoldBonusOutLayAdapter extends BaseAdapter {
	private GoldBonusOutLayActivity context;
	private List<Map<String, Object>> productList;
	public GoldBonusOutLayAdapter(GoldBonusOutLayActivity context,
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
			convertView=LayoutInflater.from(context).inflate(R.layout.gold_product_list_item, null);
			viewHolder=new ViewHolder();
			viewHolder.v1=(TextView) convertView.findViewById(R.id.list_header_tv1);
			viewHolder.v2=(TextView) convertView.findViewById(R.id.list_header_tv2);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		Map<String, Object> map =  (Map<String, Object>) getItem(position);
//		String temp = getStringArray(R.array.issueType_CN).get(getStringArray(R.array.issueType_code).indexOf(map.get(GoldBonus.ISSUETYPE)))
//				+ "（" + getStringArray(R.array.issueKind_CN).get(getStringArray(R.array.issueKind_code).indexOf(map.get(GoldBonus.ISSUEKIND)))
//				+ "）-" + getStringArray(R.array.xpadgPeriodType_CN).get(getStringArray(R.array.xpadgPeriodType_code).indexOf(map.get(GoldBonus.XPADGPERIODTYPE)));
		viewHolder.v1.setText((String)map.get(GoldBonus.ISSUENAME));
		viewHolder.v2.setText(paseEndZero((String)(map.get(GoldBonus.ISSUERATE))) + "%");
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, viewHolder.v1);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, viewHolder.v2);
		return convertView;
	}
	
	private List<String> getStringArray(int resouseId) {
		return Arrays.asList(context.getResources().getStringArray(resouseId));
	}
	
	public class ViewHolder{
		private TextView v1;
		private TextView v2;
	}
	// 末尾为0格式化方法,去掉末尾的0
			public String paseEndZero(String date) {
				if (date.indexOf(".") < 0) {

					return date;
				} else {
					date = date.replaceAll("0+?$", "");// 去掉多余的0
					date = date.replaceAll("[.]$", "");// 如最后一位是.则去掉
					return date;
				}
				
			}
}
