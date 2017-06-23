package com.chinamworld.bocmbci.biz.tran.remit.adapters;

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
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

public class RemitQueryChoiseAccAdapter extends BaseAdapter {
	private List<Map<String, String>> list = null;
	private Context context = null;
	public static int selectedPosition = -1;

	public RemitQueryChoiseAccAdapter(Context context, List<Map<String, String>> list) {
		this.list = list;
		this.context = context;
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
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.forex_acc_setting_item, null);
			holder = new ViewHolder();
			holder.typeText = (TextView) convertView.findViewById(R.id.forex_acc_type);
			holder.numberText = (TextView) convertView.findViewById(R.id.forex_acc_accountnumber);
			holder.nickText = (TextView) convertView.findViewById(R.id.forex_acc_alias);
			holder.img = (ImageView) convertView.findViewById(R.id.imageViewright);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String, String> map = list.get(position);
		// 选中的颜色
		if (position == selectedPosition) {
			convertView.setBackgroundResource(R.drawable.bg_for_listview_item_half_black);
			holder.img.setVisibility(View.VISIBLE);
		} else {
			convertView.setBackgroundResource(R.drawable.bg_for_listview_item_write);
			holder.img.setVisibility(View.INVISIBLE);
		}
		String accountType = map.get(Crcd.CRCD_ACCOUNTTYPE_RES);
		String accountNumber = map.get(Crcd.CRCD_ACCOUNTNUMBER_RES);
		String nickName = map.get(Crcd.CRCD_NICKNAME_RES);
		String type = null;
		if (!StringUtil.isNull(accountType) && LocalData.AccountType.containsKey(accountType)) {
			type = LocalData.AccountType.get(accountType);
		}
		String number = null;
		if (!StringUtil.isNull(accountNumber)) {
			number = StringUtil.getForSixForString(accountNumber);
		}
		holder.typeText.setText(type);
		holder.numberText.setText(number);
		holder.nickText.setText(nickName);
		return convertView;
	}

	private class ViewHolder {
		private TextView typeText = null;
		private TextView numberText = null;
		private TextView nickText = null;
		private ImageView img = null;
	}
}
