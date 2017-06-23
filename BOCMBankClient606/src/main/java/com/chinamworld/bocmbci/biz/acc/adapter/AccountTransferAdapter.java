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
import com.chinamworld.bocmbci.bii.constant.Bocnet;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 账户交易明细适配器
 * 
 * @author wangmengmeng
 * 
 */
public class AccountTransferAdapter extends BaseAdapter {

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
	/** 对方账户名称/账号 */
	private TextView tv_payeeAccName;
	/** 附言 */
	private TextView tv_furInfo;
	/** 币种 */
	private String currency;
	/**交易渠道*/
	private TextView acc_query_bocnet_trade_ditch_tv;
	/**交易场所*/
	private TextView acc_query_bocnet_name_point_tv;

	public AccountTransferAdapter(Context context,
			List<Map<String, Object>> transferList, String currency) {
		this.context = context;
		this.transferList = transferList;
		this.currency = currency;
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
					R.layout.acc_mybank_query_list_item, null);
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
		/** 对方账户 */
		tv_payeeAccName = (TextView) convertView
				.findViewById(R.id.payeeAccName);
		/** 附言 */
		tv_furInfo = (TextView) convertView.findViewById(R.id.furInfo);
		acc_query_bocnet_trade_ditch_tv = (TextView) convertView.
				findViewById(R.id.acc_query_bocnet_trade_ditch_value);
		acc_query_bocnet_name_point_tv = (TextView) convertView.
				findViewById(R.id.acc_query_bocnet_name_point_value);
		// 赋值操作
		String amount = String.valueOf(transferList.get(position).get(
				Acc.QUERYTRANSFER_ACC_AMOUNT_RES));
		if (StringUtil.isNull(amount)) {

		} else {
			if (amount.startsWith(ConstantGloble.BOCINVT_DATE_ADD)) {
				// 支出
				tv_acc_query_result_amount.setTextColor(context.getResources()
						.getColor(R.color.greens));
				tv_acc_query_result_acm.setText(R.string.acc_query_amount_out);
				String result_amount = amount.subSequence(1, amount.length())
						.toString();
				tv_acc_query_result_amount.setText(StringUtil
						.parseStringCodePattern(currency, result_amount, 2));

			} else {
				tv_acc_query_result_amount.setTextColor(context.getResources()
						.getColor(R.color.red));
				tv_acc_query_result_acm.setText(R.string.acc_query_amount_in);
				// 收入
				tv_acc_query_result_amount.setText(StringUtil
						.parseStringCodePattern(currency, amount, 2));
			}
		}
		String paymentdate = String.valueOf(transferList.get(position).get(
				Acc.QUERYTRANSFER_ACC_PAYMENTDATE_RES));
		tv_acc_query_result_paymentdate
		.setText(StringUtil.isNull(paymentdate) ? ConstantGloble.BOCINVT_DATE_ADD
				: paymentdate);
		String result_balance = (String) (transferList.get(position)
				.get(Acc.QUERYTRANSFER_ACC_BALANCE_RES));
		tv_acc_query_result_balance.setText(StringUtil.parseStringCodePattern(
				currency, result_balance, 2));
		String businessDigest = (String) (transferList.get(position)
				.get(Acc.QUERYTRANSFER_ACC_BUSINESSDIGEST_RES));
		tv_acc_query_result_businessDigest.setText(StringUtil
				.isNull(businessDigest) ? ConstantGloble.BOCINVT_DATE_ADD
						: businessDigest);
		String payeeAccName = (String) transferList.get(position).get(
				Acc.QUERYTRANSFER_ACC_PAYEEACCOUNTNAME_RES);
		String payeeAccNumber = (String) transferList.get(position).get(
				Acc.QUERYTRANSFER_ACC_PAYEEACCOUNTNUMBER_RES);
		if (StringUtil.isNull(payeeAccName)
				&& StringUtil.isNull(payeeAccNumber)) {
			tv_payeeAccName.setText(ConstantGloble.BOCINVT_DATE_ADD);
		} else if (!StringUtil.isNull(payeeAccName)
				&& !StringUtil.isNull(payeeAccNumber)) {
			tv_payeeAccName.setText(payeeAccName + ConstantGloble.ACC_STRING
					+ StringUtil.getForSixForString(payeeAccNumber));
		} else {
			tv_payeeAccName
			.setText((StringUtil.isNull(payeeAccName) ? ConstantGloble.BOCINVT_DATE_ADD
					: payeeAccName)
					+ ConstantGloble.ACC_STRING
					+ StringUtil.getForSixForString(payeeAccNumber));

		}

		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				tv_payeeAccName);

		String furInfo = (String) transferList.get(position).get(
				Acc.QUERYTRANSFER_ACC_FURINFO_RES);
		tv_furInfo
		.setText(StringUtil.isNull(furInfo) ? ConstantGloble.BOCINVT_DATE_ADD
				: furInfo);
		if(StringUtil.isNullOrEmpty(transferList.get(position).get(Bocnet.TRANSCHNL))){
			acc_query_bocnet_trade_ditch_tv.setText("-");
		}else{
			acc_query_bocnet_trade_ditch_tv.setText((String)transferList.get(position).get(Bocnet.TRANSCHNL));
		}
		
		if(StringUtil.isNullOrEmpty(transferList.get(position).get(Bocnet.CHNLDETAIL))){
			acc_query_bocnet_name_point_tv.setText("-");
		}else{
			acc_query_bocnet_name_point_tv.setText((String) transferList.get(position).get(Bocnet.CHNLDETAIL));	
		}
		
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				tv_furInfo);
		return convertView;
	}

	public void addItem(Map<String, Object> map) {
		transferList.add(map);
	}
}
