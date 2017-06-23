package com.chinamworld.bocmbci.biz.thridmanage.cecuritytrade.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: BankAdapter
 * @Description:转出账号列表
 * @author lql
 * @date 2013-9-11 上午10:46:24
 * 
 */
public class BankAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> mBankAccList = null;

	public BankAdapter(Context context, List<Map<String, Object>> bankAccList) {
		this.mContext = context;
		this.mBankAccList = bankAccList;
	}

	@Override
	public int getCount() {
		return mBankAccList.size();
	}

	@Override
	public Object getItem(int position) {
		if (mBankAccList != null && mBankAccList.size() > position) {
			return mBankAccList.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHodler hviewHolder;
		if (convertView == null) {
			hviewHolder = new ViewHodler();
			convertView = LinearLayout.inflate(mContext, R.layout.third_bankaccount_item, null);

			hviewHolder.tvAccType = (TextView) convertView.findViewById(R.id.dept_type_tv);
			hviewHolder.tvNickname = (TextView) convertView.findViewById(R.id.tv_acc_address);
			hviewHolder.tvAccNum = (TextView) convertView.findViewById(R.id.tv_acc_no);

			convertView.setTag(hviewHolder);
		} else {
			hviewHolder = (ViewHodler) convertView.getTag();
		}
		Map<String, Object> map = mBankAccList.get(position);
		if (map != null) {
			String strAccountType = LocalData.AccountType.get((String) map.get(Comm.ACCOUNT_TYPE));
			hviewHolder.tvAccType.setText(strAccountType);
			hviewHolder.tvNickname.setText((String) map.get(Comm.NICKNAME));
			String accountNumber = (String) map.get(Comm.ACCOUNTNUMBER);
			hviewHolder.tvAccNum.setText(StringUtil.getForSixForString(accountNumber));
		}
		return convertView;
	}

	private static class ViewHodler {
		public TextView tvAccType;
		public TextView tvNickname;
		public TextView tvAccNum;
	}
}
