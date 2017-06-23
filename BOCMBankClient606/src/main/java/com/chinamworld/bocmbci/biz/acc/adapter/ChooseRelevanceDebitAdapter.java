package com.chinamworld.bocmbci.biz.acc.adapter;

import java.util.ArrayList;
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
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 未关联借记卡账户列表多选适配器
 * 
 * @author wangmengmeng
 * 
 */
public class ChooseRelevanceDebitAdapter extends BaseAdapter {
	/** 未关联借记卡账户列表信息 */
	private List<Map<String, String>> falseDebitList;
	private Context context;
	/** 是否选中 true代表可选,false代表不可选 */
	private List<Boolean> booleanSelect = new ArrayList<Boolean>();

	public ChooseRelevanceDebitAdapter(Context context,
			List<Map<String, String>> falseDebitList) {
		this.context = context;
		this.falseDebitList = falseDebitList;
	}

	@Override
	public int getCount() {

		return falseDebitList.size();
	}

	@Override
	public Object getItem(int position) {

		return falseDebitList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.acc_relevance_debit_list_item, null);

		}
		/** 账户类型 */
		TextView tv_acc_type_value = (TextView) convertView
				.findViewById(R.id.acc_accounttype_value);
		String acc_type = falseDebitList.get(position).get(
				Acc.RELEVANCEACCPRE_ACC_DEBITACCOUNTTYPE_RES);
		tv_acc_type_value
				.setText(StringUtil.isNull(acc_type) ? ConstantGloble.BOCINVT_DATE_ADD
						: AccBaseActivity.bankAccountType.get(acc_type.trim()));
		/** 账户关联状态 */
		TextView tv_acc_linkflag = (TextView) convertView
				.findViewById(R.id.acc_relevance_linkedflag_value);
		String flag;
		flag = LocalData.linkList.get(0);
		tv_acc_linkflag.setText(flag);
		/** 账户账号 */
		TextView tv_acc_account_num = (TextView) convertView
				.findViewById(R.id.acc_relevance_accountnumber_value);
		String acc_account_num = falseDebitList.get(position).get(
				Acc.RELEVANCEACCPRE_ACC_DEBITACCOUNTNUMBER_RES);
		tv_acc_account_num.setText(StringUtil
				.getForSixForString(acc_account_num));
		ImageView checked = (ImageView) convertView
				.findViewById(R.id.imageViewright);
		if (booleanSelect.get(position)) {
			checked.setVisibility(View.GONE);
			convertView
					.setBackgroundResource(R.drawable.bg_for_listview_item_write);
		} else {
			checked.setVisibility(View.VISIBLE);
			convertView
					.setBackgroundResource(R.drawable.bg_for_listview_item_half_black);
		}
		return convertView;
	}

	/**
	 * @return the booleanSelect
	 */
	public List<Boolean> getBooleanSelect() {
		return booleanSelect;
	}

	/**
	 * @param booleanSelect
	 *            the booleanSelect to set
	 */
	public void setBooleanSelect(List<Boolean> booleanSelect) {
		this.booleanSelect = booleanSelect;
		notifyDataSetChanged();
	}

}
