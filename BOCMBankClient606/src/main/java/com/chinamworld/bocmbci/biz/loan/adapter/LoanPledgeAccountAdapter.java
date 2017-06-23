package com.chinamworld.bocmbci.biz.loan.adapter;

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
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

public class LoanPledgeAccountAdapter extends BaseAdapter {
	private List<Map<String, String>> list = null;
	private Context context = null;
	// 默认选中第一个
	public static int selectedPosition = -1;

	public LoanPledgeAccountAdapter(Context context, List<Map<String, String>> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public void dataChange(List<Map<String, String>> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.forex_acc_setting_item, null);
			holder = new ViewHolder();
			holder.loanStyle = (TextView) convertView.findViewById(R.id.forex_acc_type);
			holder.loanAlias = (TextView) convertView.findViewById(R.id.forex_acc_alias);
			holder.loanAccountNum = (TextView) convertView.findViewById(R.id.forex_acc_accountnumber);
			holder.img = (ImageView) convertView.findViewById(R.id.imageViewright);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 选中的颜色
		if (position == selectedPosition) {
			convertView.setBackgroundResource(R.drawable.bg_for_listview_item_half_black);
			holder.img.setVisibility(View.VISIBLE);
		} else {
			convertView.setBackgroundResource(R.drawable.bg_for_listview_item_write);
			holder.img.setVisibility(View.INVISIBLE);
		}
		Map<String, String> map = list.get(position);
		String accountType = map.get(Loan.LOAN_ACCOUNTTYPE_RES);
		String accountNumber = map.get(Loan.LOAN_ACCOUNTNUMBER_RES);
		String nickName = map.get(Loan.LOAN_NICKNAME_RES);
		String number = null;
		if (StringUtil.isNull(accountNumber)) {
			number = "-";
		} else {
			number = StringUtil.getForSixForString(accountNumber);
		}
		String type = null;
		if (StringUtil.isNull(accountType)) {
			type = "-";
		} else {
			if (LocalData.AccountType.containsKey(accountType)) {
				type = LocalData.AccountType.get(accountType);
			} else {
				type = "-";
			}
		}
		holder.loanStyle.setText(type);
		holder.loanAccountNum.setText(number);
		holder.loanAlias.setText(nickName);
		return convertView;
	}

	/**
	 * 存放控件
	 * 
	 * @author Administrator
	 * 
	 */
	public final class ViewHolder {
		/**
		 * 账户类型
		 */
		public TextView loanStyle;
		/**
		 * 账户别名
		 */
		public TextView loanAlias;
		/**
		 * 账户号码
		 */
		public TextView loanAccountNum;

		public ImageView img;
	}
}
