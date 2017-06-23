package com.chinamworld.bocmbci.biz.acc.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 未关联借记卡账户关联成功适配器
 * 
 * @author wangmengmeng
 * 
 */
public class RelevanceDebitSuccessAdapter extends BaseAdapter {
	/** 借记卡列表信息 */
	private List<Map<String, Object>> debitList;
	private Context context;
	/** 待关联账户类型 */
	private TextView acc_relevance_type;
	/** 待关联账号 */
	private TextView acc_relevance_actnum;

	public RelevanceDebitSuccessAdapter(Context context,
			List<Map<String, Object>> debitList) {
		this.context = context;
		this.debitList = debitList;
	}

	@Override
	public int getCount() {

		return debitList.size();
	}

	@Override
	public Object getItem(int position) {

		return debitList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.acc_relevanceaccount_debit_succ_list_item, null);
		}
		LinearLayout ll_back = (LinearLayout) convertView
				.findViewById(R.id.ll_for_relevance);
		if (position % 2 == 0) {
			ll_back.setBackgroundResource(R.color.whitefornet);
		}
		acc_relevance_type = (TextView) convertView
				.findViewById(R.id.tv_relevance_type_value);
		String acc_type = (String) debitList.get(position).get(
				Acc.RELEVANCEACCRES_CASHSUCCED_ACCOUNTTYPE_RES);
		acc_relevance_type.setText(AccBaseActivity.bankAccountType.get(acc_type
				.trim()));
		acc_relevance_actnum = (TextView) convertView
				.findViewById(R.id.tv_relevance_actnum_value);
		String actnumber = (String) debitList.get(position).get(
				Acc.RELEVANCEACCRES_CASHSUCCED_ACCOUNTNUMBER_RES);
		acc_relevance_actnum.setText(StringUtil.getForSixForString(actnumber));

		return convertView;
	}
}
