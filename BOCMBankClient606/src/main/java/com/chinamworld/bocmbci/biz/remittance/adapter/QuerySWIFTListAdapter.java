package com.chinamworld.bocmbci.biz.remittance.adapter;

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
import com.chinamworld.bocmbci.bii.constant.Remittance;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;

public class QuerySWIFTListAdapter extends BaseAdapter {

	/** 上下文 */
	private Context context;
	/** SWIFT信息 */
	private List<Map<String, Object>> listSWIFT;
	/** 选择监听 */
	private OnItemClickListener onitemClickListener;
	
	public QuerySWIFTListAdapter(Context context, List<Map<String, Object>> listSWIFT) {
		this.context = context;
		this.listSWIFT = listSWIFT;
		
		for (int i = 0; i < listSWIFT.size(); i++) {
			listSWIFT.get(i).put("isOpen", false);
		}
	}
	
	@Override
	public int getCount() {
		return listSWIFT.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listSWIFT.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder vh;
		if (arg1 == null) {
			arg1 = LayoutInflater.from(context).inflate(R.layout.remittance_info_input_query_swift_list_item, null);
			vh = new ViewHolder();
			vh.tvSWIFTCode = (TextView) arg1.findViewById(R.id.tv_swiftCode);
			vh.tvBankName = (TextView) arg1.findViewById(R.id.tv_bankname);
			vh.tvCity = (TextView) arg1.findViewById(R.id.tv_city);
			vh.tvCountry = (TextView) arg1.findViewById(R.id.tv_country);
			vh.tvAddress = (TextView) arg1.findViewById(R.id.tv_adress);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}

		final Map<String, Object> map = (Map<String, Object>) getItem(arg0);
		vh.tvSWIFTCode.setText((String) map.get(Remittance.SWIFTCODE));
		vh.tvBankName.setText((String) map.get(Remittance.BANKNAME));
		vh.tvCity.setText((String) map.get(Remittance.BANKADDRESS2));
		vh.tvCountry.setText((String) map.get(Remittance.BANKADDRESS3));
		vh.tvAddress.setText((String) map.get(Remittance.BANKADDRESS1));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, vh.tvSWIFTCode);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, vh.tvBankName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, vh.tvCity);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, vh.tvCountry);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, vh.tvAddress);
		if ((Boolean) map.get("isOpen")) {
			arg1.findViewById(R.id.ll_push).setVisibility(View.GONE);
			arg1.findViewById(R.id.ll_detail).setVisibility(View.VISIBLE);
			arg1.findViewById(R.id.ll_up).setVisibility(View.VISIBLE);
		} else {
			arg1.findViewById(R.id.ll_push).setVisibility(View.VISIBLE);
			arg1.findViewById(R.id.ll_detail).setVisibility(View.GONE);
			arg1.findViewById(R.id.ll_up).setVisibility(View.GONE);
		}
		
		final View view = arg1;
		final int position = arg0;
		arg1.findViewById(R.id.ll_push).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				map.put("isOpen", true);
				notifyDataSetChanged();
				view.findViewById(R.id.ll_push).setVisibility(View.GONE);
				view.findViewById(R.id.ll_up).setVisibility(View.VISIBLE);
			}
		});
		arg1.findViewById(R.id.ll_up).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				map.put("isOpen", false);
				notifyDataSetChanged();
				view.findViewById(R.id.ll_push).setVisibility(View.VISIBLE);
				view.findViewById(R.id.ll_up).setVisibility(View.GONE);
			}
		});
		arg1.findViewById(R.id.tv_choose).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onitemClickListener != null) {
					onitemClickListener.onItemClick(null, view, position, 0);
				}
			}
		});
		return arg1;
	}
	
	private class ViewHolder {
		TextView tvSWIFTCode;
		TextView tvBankName;
		TextView tvCity;
		TextView tvCountry;
		TextView tvAddress;
	}
	
	public void setData(List<Map<String, Object>> listSWIFT) {
		this.listSWIFT = listSWIFT;

		for (int i = 0; i < listSWIFT.size(); i++) {
			listSWIFT.get(i).put("isOpen", false);
		}
		notifyDataSetChanged();
	}
	
	public void setOnItemClickListener(OnItemClickListener onitemClickListener) {
		this.onitemClickListener = onitemClickListener;
	}
}
