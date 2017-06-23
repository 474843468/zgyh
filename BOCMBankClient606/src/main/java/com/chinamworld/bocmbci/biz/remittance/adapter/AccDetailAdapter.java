package com.chinamworld.bocmbci.biz.remittance.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.remittance.utils.RemittanceUtils;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

public class AccDetailAdapter extends BaseAdapter {

	private static final String TAG = "AccDetailAdapter";
	/** 数据源 */
	private List<Map<String, Object>> detailList;
	/** 上下文 */
	private Context context;
	
	public AccDetailAdapter(Context context, List<Map<String, Object>> detailList) {
		this.detailList = detailList;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return detailList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return detailList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder vh = null;
		if (arg1 == null) {
			arg1 = LayoutInflater.from(context).inflate(R.layout.remittance_info_input_acc_detail_dialog_lv_item, null);
			vh = new ViewHolder();
			vh.tvKey = (TextView) arg1.findViewById(R.id.tv_key);
			vh.tvValue = (TextView) arg1.findViewById(R.id.tv_value);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}
		
		Map<String, Object> map = (Map<String, Object>) getItem(arg0);
		if (map.get(Acc.DETAIL_CURRENCYCODE_RES).equals("001")) {
			vh.tvKey.setText("人民币元");
		} else {
			vh.tvKey.setText(LocalData.Currency.get(map.get(Comm.CURRENCYCODE)) + RemittanceUtils.getCashRimit((String) map.get(Acc.DETAIL_CASHREMIT_RES)));
		}
		vh.tvValue.setText(StringUtil.parseStringCodePattern((String) map.get(Acc.DETAIL_CURRENCYCODE_RES), (String) map.get(Acc.DETAIL_AVAILABLEBALANCE_RES), 2));
		LogGloble.i(TAG, arg0 + "->" + (String) map.get(Acc.DETAIL_AVAILABLEBALANCE_RES));
		return arg1;
	}

	class ViewHolder {
		TextView tvKey;
		TextView tvValue;
	}
}
