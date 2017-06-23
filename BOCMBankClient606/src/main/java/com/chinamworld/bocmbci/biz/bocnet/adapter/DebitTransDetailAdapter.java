package com.chinamworld.bocmbci.biz.bocnet.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Bocnet;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 借记卡交易明细适配器
 * 
 * @author fsm
 * 
 */
public class DebitTransDetailAdapter extends BaseAdapter {

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

	public DebitTransDetailAdapter(Context context,
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
		Map<String, Object> map = transferList.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.bocnet_mybank_query_list_item, null);
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
		String amount = String.valueOf(map.get(Bocnet.AMOUNT));
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
		String paymentdate = String.valueOf(map.get(Bocnet.PAYMENTDATE));
		tv_acc_query_result_paymentdate
				.setText(StringUtil.isNull(paymentdate) ? ConstantGloble.BOCINVT_DATE_ADD
						: paymentdate);
		String result_balance = (String) (map.get(Bocnet.BALANCE));
		tv_acc_query_result_balance.setText(StringUtil.parseStringCodePattern(
				currency, result_balance, 2));
		String businessDigest = (String) (map.get(Bocnet.BUSINESSDIGEST));
		tv_acc_query_result_businessDigest.setText(StringUtil
				.isNull(businessDigest) ? ConstantGloble.BOCINVT_DATE_ADD
				: businessDigest);
		String payeeAccName = (String) map.get(Bocnet.PAYEEACCOUNTNAME);
		String payeeAccNumber = (String) map.get(Bocnet.PAYEEACCOUNTNUMBER);
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


		String furInfo = (String) map.get(Bocnet.FURINFO);
		tv_furInfo
				.setText(StringUtil.isNull(furInfo) ? ConstantGloble.BOCINVT_DATE_ADD
						: furInfo);
		if(StringUtil.isNullOrEmpty(map.get(Bocnet.TRANSCHNL))){
			acc_query_bocnet_trade_ditch_tv.setText("-");
		}else{
			acc_query_bocnet_trade_ditch_tv.setText((String) (map.get(Bocnet.TRANSCHNL)));
		}
		
		if(StringUtil.isNullOrEmpty(map.get(Bocnet.CHNLDETAIL))){
			acc_query_bocnet_name_point_tv.setText("-");
		}else{
			acc_query_bocnet_name_point_tv.setText((String) (map.get(Bocnet.CHNLDETAIL)));
		}
	    
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				tv_payeeAccName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				tv_furInfo);
		return convertView;
	}

	public void setDatas(List<Map<String, Object>> transferList, String currency){
		this.transferList = transferList;
		this.currency = currency;
		notifyDataSetChanged();
	}
	
	public void clearDatas(){
		this.transferList = new ArrayList<Map<String,Object>>();
		notifyDataSetChanged();
	}
}
