package com.chinamworld.bocmbci.biz.epay.transquery.adapter;

import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.TQConstants;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.utils.StringUtil;

public class ResultListAdapter extends BaseAdapter {

	private BaseActivity context;

	private String type;
	private List<Object> accountList;

	private String transDate;
	private String orderNum;
	private String businessName;

	public ResultListAdapter(BaseActivity context, List<Object> accountList,
			String type) {
		this.type = type;
		this.accountList = accountList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return accountList.size();
	}

	@Override
	public Object getItem(int position) {
		if (accountList.size() < position) {
			return null;
		}
		return accountList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.epay_tq_result_list_item, null);
			holder.ll_content = (LinearLayout) convertView
					.findViewById(R.id.ll_content);
			holder.img_redirect = (ImageView) convertView
					.findViewById(R.id.img_redirect);
			holder.tv_trans_date = (TextView) convertView
					.findViewById(R.id.tv_trans_date);
			holder.tv_order_num = (TextView) convertView
					.findViewById(R.id.tv_order_num);
			holder.tv_business_name = (TextView) convertView
					.findViewById(R.id.tv_business_name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Map<Object, Object> tempMap = EpayUtil
				.getMap(accountList.get(position));

		// if (PaymentType.PAYMENT_TYPE_BOM.equals(type)) {
		// // 电子支付查询
		// transDate =
		// EpayUtil.getString(tempMap.get(TQConstants.METHOD_QUERY_BOM_TRANS_RECORD_FEILD_EPAY_TIME),
		// "");
		// orderNum =
		// EpayUtil.getString(tempMap.get(TQConstants.METHOD_QUERY_BOM_TRANS_RECORD_FEILD_ORDER_NO),
		// "");
		// businessName = EpayUtil.getString(
		// tempMap.get(TQConstants.METHOD_QUERY_BOM_TRANS_RECORD_FEILD_MERCHANT_NAME),
		// "");
		// } else if (PaymentType.PAYMENT_TYPE_TREATY_QUCIK.equals(type)) {
		// // 协议支付查询
		// transDate = EpayUtil
		// .getString(tempMap.get(TQConstants.METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_PAY_TIME),
		// "");
		// orderNum =
		// EpayUtil.getString(tempMap.get(TQConstants.METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_ORDER_NO),
		// "");
		// businessName = EpayUtil.getString(
		// tempMap.get(TQConstants.METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_MERCHANT_NAME),
		// "");
		// } else if (PaymentType.PAYMENT_TYPE_ZY_QUCIK.equals(type)) {
		// // 中银快付查询
		// transDate =
		// EpayUtil.getString(tempMap.get(TQConstants.METHOD_QUERY_ZY_TRANS_RECORD_FEILD_PAY_TIME),
		// "");
		// orderNum =
		// EpayUtil.getString(tempMap.get(TQConstants.METHOD_QUERY_ZY_TRANS_RECORD_FEILD_ORDER_NO),
		// "");
		// businessName = EpayUtil.getString(
		// tempMap.get(TQConstants.METHOD_QUERY_ZY_TRANS_RECORD_FEILD_MERCHANT_NAME),
		// "");
		// }

		transDate = EpayUtil
				.getString(
						tempMap.get(TQConstants.METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_PAY_TIME),
						"");
		orderNum = EpayUtil
				.getString(
						tempMap.get(TQConstants.METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_ORDER_NO),
						"");
		businessName = EpayUtil
				.getString(
						tempMap.get(TQConstants.METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_MERCHANT_NAME),
						"");

		String payTime = "";
		if (!StringUtil.isNull(transDate) && transDate.length() >= 8) {
			payTime = transDate.substring(0, 4) + "/"
					+ transDate.substring(4, 6) + "/"
					+ transDate.substring(6, 8);
		} else {
			payTime = transDate;
		}
		holder.tv_trans_date.setText(payTime);
		holder.tv_order_num.setText(orderNum);
		holder.tv_business_name.setText(businessName);

		LayoutParams ll_content_params = holder.ll_content.getLayoutParams();
		ll_content_params.height = LayoutValue.SCREEN_WIDTH * 80 / 598;

		LayoutParams img_redirect_params = holder.img_redirect
				.getLayoutParams();
		img_redirect_params.width = LayoutValue.SCREEN_WIDTH * 20 / 320;
		img_redirect_params.height = LayoutValue.SCREEN_WIDTH * 20 / 320;

		return convertView;
	}

	class ViewHolder {
		LinearLayout ll_content;
		ImageView img_redirect;
		TextView tv_trans_date;
		TextView tv_order_num;
		TextView tv_business_name;
	}

	//
	// public void setSelectedPosition(int selectedPosition) {
	// this.selectedPosition = selectedPosition;
	// notifyDataSetChanged();
	// }

	// public void setAccountList(List<Object> accountList) {
	// this.accountList = accountList;
	// this.notifyDataSetChanged();
	// }

	// public List<Object> getAccountList() {
	// return accountList;
	// }

	public void setData(List<Object> restListData, String type) {
		this.accountList = restListData;
		this.type = type;
	}

}
