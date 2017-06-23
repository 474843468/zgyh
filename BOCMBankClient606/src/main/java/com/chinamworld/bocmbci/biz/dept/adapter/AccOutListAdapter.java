package com.chinamworld.bocmbci.biz.dept.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 转出账户列表适配器
 * 
 * @author
 * 
 */
public class AccOutListAdapter extends BaseAdapter {

	private Context mContext;
	/** 查询账户列表 */
	private List<Map<String, Object>> accountList = null;

	// private int padding;

	public AccOutListAdapter(Context context,
			List<Map<String, Object>> accountList) {
		this.mContext = context;
		this.accountList = accountList;
		// padding =
		// mContext.getResources().getDimensionPixelSize(R.dimen.fill_margin_left)
		// * 2;
	}

	@Override
	public int getCount() {
		return accountList.size();
	}

	@Override
	public Object getItem(int position) {
		return accountList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.dept_acc_in_list_item, null);
		}

		// convertView.measure(0, 0);
		// int itemHeight = convertView.getMeasuredHeight();
		// int itemWidth = LayoutValue.SCREEN_WIDTH - padding;
		// LayoutParams lp = new LayoutParams(itemWidth, itemHeight);
		// convertView.setLayoutParams(lp);

		TextView accountTypeTv = (TextView) convertView
				.findViewById(R.id.dept_type_tv);
		TextView nickNameTv = (TextView) convertView
				.findViewById(R.id.tv_acc_address);
		TextView accountNumberTypeTv = (TextView) convertView
				.findViewById(R.id.tv_acc_no);

		Map<String, Object> map = accountList.get(position);
		if (map != null) {
			String strAccountType = LocalData.AccountType.get((String) map
					.get(Comm.ACCOUNT_TYPE));
			accountTypeTv.setText(StringUtil.isNullChange(strAccountType));
			nickNameTv.setText(StringUtil.isNullChange((String) map
					.get(Comm.NICKNAME)));
			String accountNumber = (String) map.get(Comm.ACCOUNTNUMBER);
			accountNumberTypeTv.setText(StringUtil
					.getForSixForString(accountNumber));
	// add luqp 2016年4月15日 如果是查询版电子卡账户修改别名  为"中银E财账户 "
			accountNumber  = accountNumber.substring(0, 4);
			if ("6216".equals(accountNumber)) {
				accountTypeTv.setText("中银E财账户 ");
			} else {
				accountTypeTv.setText(StringUtil.isNullChange(strAccountType));
			}
			// ===========================================================================
			
			PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext,
					accountTypeTv);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext,
					nickNameTv);
		
		}

		return convertView;
	}

}
