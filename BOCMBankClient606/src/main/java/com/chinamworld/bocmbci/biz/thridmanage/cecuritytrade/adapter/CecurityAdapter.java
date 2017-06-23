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
import com.chinamworld.bocmbci.bii.constant.Third;

/**
 * @ClassName: CecurityAdapter
 * @Description: 转入账号列表
 * @author lql
 * @date 2013-9-11 上午10:45:19
 * 
 */
public class CecurityAdapter extends BaseAdapter {
	private Context mContext;

	private List<Map<String, Object>> mBankAccList = null;

	public CecurityAdapter(Context context, List<Map<String, Object>> bankAccList) {
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
		ViewHodler h;
		if (convertView == null) {
			h = new ViewHodler();
			convertView = LinearLayout.inflate(mContext, R.layout.third_cecuritytrade_item, null);

			// h.tvAccType = (TextView)
			// convertView.findViewById(R.id.dept_type_tv);
			// h.tvNickname = (TextView)
			// convertView.findViewById(R.id.tv_acc_address);
			h.tvAccNum = (TextView) convertView.findViewById(R.id.tv_acc_no);

			convertView.setTag(h);
		} else {
			h = (ViewHodler) convertView.getTag();
		}
		Map<String, Object> map = mBankAccList.get(position);
		if (map != null) {
			// h.tvNickname.setVisibility(View.GONE);
			// h.tvAccType.setVisibility(View.GONE);
			// h.tvAccType.setText((String)
			// map.get(Third.CECURITYTRADE_COMPANY));
			String accountNumber = (String) map.get(Third.CECURITYTRADE_BANKACCNUM_RE);
			// h.tvAccNum.setText(StringUtil.getForSixForString(accountNumber));
			h.tvAccNum.setText(accountNumber);
		}
		return convertView;
	}

	private static class ViewHodler {
		// public TextView tvAccType;
		// public TextView tvNickname;
		public TextView tvAccNum;
	}
}
