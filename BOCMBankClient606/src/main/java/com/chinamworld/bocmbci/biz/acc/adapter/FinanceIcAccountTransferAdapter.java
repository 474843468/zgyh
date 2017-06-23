package com.chinamworld.bocmbci.biz.acc.adapter;

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
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 电子现金账户交易明细适配器
 * 
 * @author wangmengmeng
 * 
 */
public class FinanceIcAccountTransferAdapter extends BaseAdapter {

	/** 账户交易列表信息 */
	private List<Map<String, Object>> transferList;
	private Context context;
	/** 账户交易收入支出金额 */
	private TextView tv_acc_query_result_amount;
	/** 账户交易时间 */
	private TextView tv_acc_query_result_paymentdate;
	/** 账户交易账户余额 */
	private TextView tv_acc_query_result_balance;
	/** 账户交易业务摘要 */
	private TextView tv_acc_query_result_businessDigest;

	public FinanceIcAccountTransferAdapter(Context context,
			List<Map<String, Object>> transferList) {
		this.context = context;
		this.transferList = transferList;
	}

	@Override
	public int getCount() {

		return transferList.size();
	}

	@Override
	public Object getItem(int position) {

		return transferList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.acc_mybankaccount_query_list_item, null);
		}
		TextView tv_acc_query_result_acm = (TextView) convertView
				.findViewById(R.id.acc_query_transfer_amount);
		/** 账户交易收入支出金额 */
		tv_acc_query_result_amount = (TextView) convertView
				.findViewById(R.id.acc_query_transfer_amount_value);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				tv_acc_query_result_amount);
		/** 账户交易时间 */
		tv_acc_query_result_paymentdate = (TextView) convertView
				.findViewById(R.id.acc_query_transfer_paymentdate_value);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				tv_acc_query_result_paymentdate);
		/** 账户交易账户余额 */
		tv_acc_query_result_balance = (TextView) convertView
				.findViewById(R.id.acc_query_transfer_balance_value);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				tv_acc_query_result_balance);
		/** 账户交易业务摘要 */
		tv_acc_query_result_businessDigest = (TextView) convertView
				.findViewById(R.id.acc_query_transfer_businessdigest_value);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				tv_acc_query_result_businessDigest);
		// 赋值操作
		String amount = String.valueOf(transferList.get(position).get(
				Acc.ICQUERYTRANSFER_ACC_AMOUNT_RES));
		if (StringUtil.isNull(amount)) {

		} else {
			if (amount.startsWith("-")) {
				// 支出
				tv_acc_query_result_amount.setTextColor(context.getResources()
						.getColor(R.color.greens));
				tv_acc_query_result_acm.setText(R.string.acc_query_amount_out);
				String result_amount = amount.subSequence(1, amount.length())
						.toString();
				tv_acc_query_result_amount.setText(StringUtil
						.parseStringPattern(result_amount, 2));

			} else {
				tv_acc_query_result_amount.setTextColor(context.getResources()
						.getColor(R.color.red));
				tv_acc_query_result_acm.setText(R.string.acc_query_amount_in);
				// 收入
				tv_acc_query_result_amount.setText(StringUtil
						.parseStringPattern(amount, 2));
			}
		}
		String paymentdate = String.valueOf(transferList.get(position).get(
				Acc.ICQUERYTRANSFER_ACC_PAYMENTDATE_RES));
		tv_acc_query_result_paymentdate
				.setText(StringUtil.isNull(paymentdate) ? ConstantGloble.BOCINVT_DATE_ADD
						: paymentdate);
		// String result_balance = (String) (transferList.get(position)
		// .get(Acc.ICQUERYTRANSFER_ACC_BALANCE_RES));
		// tv_acc_query_result_balance.setText(StringUtil.parseStringPattern(
		// result_balance, 2));
		String currency = (String) (transferList.get(position)
				.get(Acc.QUERYTRANSFER_ACC_CURRENCY_REQ));
		tv_acc_query_result_balance.setText(LocalData.Currency.get(currency));
		String businessDigest = (String) (transferList.get(position)
				.get(Acc.ICQUERYTRANSFER_ACC_BUSINESSDIGEST_RES));
		tv_acc_query_result_businessDigest.setText(StringUtil
				.isNull(businessDigest) ? ConstantGloble.BOCINVT_DATE_ADD
				: AccDataCenter.getInstance().transferTypeList
						.get(businessDigest));

		return convertView;
	}

}
