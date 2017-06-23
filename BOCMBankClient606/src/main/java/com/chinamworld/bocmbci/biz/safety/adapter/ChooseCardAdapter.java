package com.chinamworld.bocmbci.biz.safety.adapter;

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
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 选卡适配器，寿险选择付款账户时需要显示卡类型等信息，需要一个复杂适配器
 * 
 * @author Zhi
 */
public class ChooseCardAdapter extends BaseAdapter {

	/** 上下文 */
	private Context context;
	/** 卡列表 */
	private List<Map<String, Object>> cardList;
	
	public ChooseCardAdapter(Context context, List<Map<String, Object>> cardList) {
		this.context = context;
		this.cardList = cardList;
	}
	
	@Override
	public int getCount() {
		if (cardList == null) {
			return 0;
		}
		return cardList.size();
	}

	@Override
	public Object getItem(int position) {
		if (cardList == null) {
			return null;
		}
		return cardList.get(position);
	}
	
	/** 获取指定下标卡类型 */
	@SuppressWarnings("unchecked")
	private String getAccountType(int position) {
		Map<String, Object> map = (Map<String, Object>) getItem(position);
		return (String) map.get(Acc.ACC_ACCOUNTTYPE);
	}
	
	/** 获取指定下标卡号码 */
	@SuppressWarnings("unchecked")
	private String getAccountNumber(int position) {
		Map<String, Object> map = (Map<String, Object>) getItem(position);
		return (String) map.get(Acc.ACC_CUSTOMERINFO_ACCOUNTNUMBER);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.dept_spinner, null);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.dept_spinner_textview);
		if (getCount() == 1) {
			tv.setText(StringUtil.getForSixForString(getAccountNumber(position)));
		} else {
			if (position == 0) {
				tv.setText(getAccountType(position));
			} else {
				tv.setText(StringUtil.getForSixForString(getAccountNumber(position)));
			}
		}
		return convertView;
	}
	
	@Override
	public View getDropDownView(final int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.safety_life_product_pay_verify_choose_card, null);
			vh.tvAccountType = (TextView) convertView.findViewById(R.id.tv_accountType);
			vh.tvAccountNumber = (TextView) convertView.findViewById(R.id.tv_accountNumber);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		if (getCount() == 1) {
			vh.tvAccountType.setText(LocalData.AccountType.get(getAccountType(position)));
			vh.tvAccountNumber.setText(StringUtil.getForSixForString(getAccountNumber(position)));
		} else {
			if (position == 0) {
				vh.tvAccountType.setText(getAccountType(position));
				vh.tvAccountNumber.setText("");
			} else {
				vh.tvAccountType.setText(LocalData.AccountType.get(getAccountType(position)));
				vh.tvAccountNumber.setText(StringUtil.getForSixForString(getAccountNumber(position)));
			}
		}
		return convertView;
	}
	
	private class ViewHolder {
		TextView tvAccountType;
		TextView tvAccountNumber;
	}
}
