package com.chinamworld.bocmbci.biz.tran.mobiletrans.adapter;

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
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class MobileQueryTransferAdapter extends BaseAdapter {

	/** 查询结果列表信息 */
	private List<Map<String, Object>> recordQueryList;
	private Context context;

	public MobileQueryTransferAdapter(Context context,
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
		/** 提交日期 */
		TextView boci_product_name = (TextView) convertView
				.findViewById(R.id.boci_product_name);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				boci_product_name);
		/** 指令流水号 */
		TextView boci_yearlyRR = (TextView) convertView
				.findViewById(R.id.boci_yearlyRR);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				boci_yearlyRR);
		/** 收款人姓名 */
		TextView boci_timeLimit = (TextView) convertView
				.findViewById(R.id.boci_timeLimit);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				boci_timeLimit);
		/** 右三角 */
		ImageView goDetail = (ImageView) convertView
				.findViewById(R.id.boci_gotoDetail);
		goDetail.setVisibility(View.VISIBLE);
		// 赋值操作
		boci_yearlyRR.setText(String.valueOf(recordQueryList.get(position).get(
				Tran.MOBILE_TRANSACTIONID_RES)));
		boci_timeLimit.setText(String.valueOf(recordQueryList.get(position)
				.get(Tran.MOBILE_PAYEENAME_RES)));
		String holdingQuantity = (String) recordQueryList.get(position).get(
				Tran.MOBILE_FIRSTSUBMITDATE_RES);
		boci_product_name.setText(holdingQuantity);
		goDetail.setVisibility(View.VISIBLE);
		return convertView;
	}

}
