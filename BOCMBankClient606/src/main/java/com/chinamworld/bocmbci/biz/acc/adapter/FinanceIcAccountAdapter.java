package com.chinamworld.bocmbci.biz.acc.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 电子现金账户列表适配器
 * 
 * @author wangmengmeng
 * 
 */
public class FinanceIcAccountAdapter extends BaseAdapter {
	/** 账户列表信息 */
	private List<Map<String, Object>> financeIcAccountList;
	private Context context;
	/** 账户详情点击事件 */
	private OnItemClickListener onbanklistItemDetailClickListener;

	public FinanceIcAccountAdapter(Context context,
			List<Map<String, Object>> financeIcAccountList) {
		this.context = context;
		this.financeIcAccountList = financeIcAccountList;
	}

	@Override
	public int getCount() {

		return financeIcAccountList.size();
	}

	@Override
	public Object getItem(int position) {

		return financeIcAccountList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.acc_financeic_list_item, null);
		}
		if (financeIcAccountList == null || financeIcAccountList.size() == 0) {
			return convertView;
		}
		/** 账户类型 */
		TextView tv_acc_type_value = (TextView) convertView
				.findViewById(R.id.tv_acc_financeic_actype);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
//				tv_acc_type_value);
		String accounttype = String.valueOf(financeIcAccountList.get(position)
				.get(Acc.ACC_ACCOUNTTYPE_RES));
		tv_acc_type_value
				.setText(StringUtil.isNull(accounttype) ? ConstantGloble.BOCINVT_DATE_ADD
						: LocalData.AccountType.get(accounttype));
		/** 账户别名 */
		TextView tv_acc_account_nickname = (TextView) convertView
				.findViewById(R.id.tv_acc_financeic_actnickname);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
//				tv_acc_account_nickname);
		String account_nickname = String.valueOf(financeIcAccountList.get(
				position).get(Acc.ACC_NICKNAME_RES));
		tv_acc_account_nickname
				.setText(StringUtil.isNull(account_nickname) ? ConstantGloble.BOCINVT_DATE_ADD
						: account_nickname);
		/** 账户账号 */
		TextView tv_acc_account_num = (TextView) convertView
				.findViewById(R.id.tv_acc_financeic_actnum);
		String acc_account_num = String.valueOf(financeIcAccountList.get(
				position).get(Acc.ACC_ACCOUNTNUMBER_RES));
		tv_acc_account_num.setText(StringUtil
				.getForSixForString(acc_account_num));
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onbanklistItemDetailClickListener != null) {
					onbanklistItemDetailClickListener.onItemClick(null, v,
							position, position);
				}
			}
		});

		return convertView;
	}

	public OnItemClickListener getOnbanklistItemDetailClickListener() {
		return onbanklistItemDetailClickListener;
	}

	public void setOnbanklistItemDetailClickListener(
			OnItemClickListener onbanklistItemDetailClickListener) {
		this.onbanklistItemDetailClickListener = onbanklistItemDetailClickListener;
	}

}
