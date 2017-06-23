package com.chinamworld.bocmbci.biz.bocnet.adapter;

import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.bii.constant.Bocnet;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class CrcdUnsettledBillsAdapter extends BaseAdapter {

	private BaseActivity context;
	private List<Map<String, Object>> hisTradeList;
	
	public CrcdUnsettledBillsAdapter(BaseActivity context, List<Map<String, Object>> hisTradeList) {
		this.hisTradeList = hisTradeList;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return getDataList().size();
	}

	@Override
	public Object getItem(int position) {
		return getDataList().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final Map<String, Object> tempMap = hisTradeList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).
					inflate(R.layout.crcd_trans_query_list_item, null);
			holder.acc_query_transfer_paymentdate_value = (TextView) convertView
					.findViewById(R.id.acc_query_transfer_paymentdate_value);
			holder.acc_query_transfer_amount = (TextView) convertView
					.findViewById(R.id.acc_query_transfer_amount);
			holder.acc_query_transfer_amount_value = (TextView) convertView
					.findViewById(R.id.acc_query_transfer_amount_value);
			holder.acc_query_transfer_balance_value = (TextView) convertView
					.findViewById(R.id.acc_query_transfer_balance_value);
			holder.img_crcd_setup = (Button) convertView
					.findViewById(R.id.img_crcd_setup);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.img_crcd_setup.setVisibility(View.GONE);
		holder.acc_query_transfer_paymentdate_value.setText(
				QueryDateUtils.getcurrentDate((String)tempMap.get(Bocnet.TRANSDATE)));
		
		String result_bookAmount = (String) (tempMap.get(Bocnet.BOOKAMOUNT));
		String result_tranAmount = (String) (tempMap.get(Bocnet.TRANAMOUNT));
		if (!StringUtil.isNullOrEmpty(result_bookAmount)) {
			holder.acc_query_transfer_amount_value.setText(StringUtil.parseStringPattern(result_bookAmount, 2));
		} else if (!StringUtil.isNullOrEmpty(result_tranAmount)) {
			holder.acc_query_transfer_amount_value.setText(StringUtil.parseStringPattern(result_tranAmount, 2));
		}
		
		String transDesc = String.valueOf((tempMap.get(Bocnet.REMARK)));
		holder.acc_query_transfer_balance_value.setText(transDesc);
		
		String debitCreditFlag = String.valueOf((tempMap.get(Bocnet.DEBITCREDITFLAG)));
		// CRED表示贷方 DEBT表示借方 NMON表示减免年费
		if (ConstantGloble.CRCD_CRED.equals(debitCreditFlag)) {
			holder.acc_query_transfer_amount.setText(context.getString(R.string.acc_query_amount_in));
		} else if (ConstantGloble.CRCD_DEBT.equals(debitCreditFlag) || ConstantGloble.CRCD_NMON.equals(debitCreditFlag)) {
			holder.acc_query_transfer_amount.setText(context.getString(R.string.acc_query_amount_out));
		}
		return convertView;
	}

	class ViewHolder {
		TextView acc_query_transfer_paymentdate_value;//日期
		TextView acc_query_transfer_amount;//收入支出类型
		TextView acc_query_transfer_amount_value;//交易数额
		TextView acc_query_transfer_balance_value;//交易描述
		Button img_crcd_setup;//分期按钮
	}

	public void setDataList(List<Map<String, Object>> hisTradeList) {
		this.hisTradeList = hisTradeList;
		this.notifyDataSetChanged();
	}
	
	public void clearList(){
		if(hisTradeList != null && hisTradeList.size() > 0){
			hisTradeList.clear();
		}
	}

	public List<Map<String, Object>> getDataList() {
		return hisTradeList;
	}

}

