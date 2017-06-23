package com.chinamworld.bocmbci.biz.tran.atmremit.adapter;

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
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * Atm汇款查询列表适配器
 * 
 * @author wangmengmeng
 * 
 */
public class AtmQueryTransferAdapter extends BaseAdapter {

	/** 查询结果列表信息 */
	private List<Map<String, Object>> recordQueryList;
	private Context context;

	public AtmQueryTransferAdapter(Context context,
			List<Map<String, Object>> recordQueryList) {
		this.context = context;
		this.recordQueryList = recordQueryList;
	}

	@Override
	public int getCount() {
		return recordQueryList.size();
	}

	@Override
	public Object getItem(int position) {
		return recordQueryList.get(position);

	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.bocinvt_hisproduct_list_item, null);
		}
		if (StringUtil.isNullOrEmpty(recordQueryList)) {
			return convertView;
		}
		/** 汇款日期 */
		TextView boci_product_name = (TextView) convertView
				.findViewById(R.id.boci_product_name);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				boci_product_name);
		/** 汇款金额 */
		TextView boci_yearlyRR = (TextView) convertView
				.findViewById(R.id.boci_yearlyRR);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				boci_yearlyRR);
		/** 汇款状态 */
		TextView boci_timeLimit = (TextView) convertView
				.findViewById(R.id.boci_timeLimit);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				boci_timeLimit);
		/** 右三角 */
		ImageView goDetail = (ImageView) convertView
				.findViewById(R.id.boci_gotoDetail);
		goDetail.setVisibility(View.VISIBLE);
		// 赋值操作
		boci_product_name.setText(String.valueOf(recordQueryList.get(position)
				.get(Tran.TRAN_ATM_QUERY_PAYMENTDATE_RES)));
		String amount = (String) recordQueryList.get(position).get(
				Tran.TRAN_ATM_QUERY_PAYMENTAMOUNT_RES);
		boci_yearlyRR
				.setTextColor(context.getResources().getColor(R.color.red));
		boci_yearlyRR.setText(StringUtil.parseStringPattern(amount, 2));
		String holdingQuantity = (String) recordQueryList.get(position).get(
				Tran.TRAN_ATM_QUERY_STATUS_RES);
		boci_timeLimit.setText((String) TranBaseActivity.AtmStatusMap
				.get(holdingQuantity));
		goDetail.setVisibility(View.VISIBLE);
		return convertView;
	}

}
