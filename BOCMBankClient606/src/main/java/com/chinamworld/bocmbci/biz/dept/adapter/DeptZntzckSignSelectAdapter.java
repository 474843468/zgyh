package com.chinamworld.bocmbci.biz.dept.adapter;

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
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

public class DeptZntzckSignSelectAdapter extends BaseAdapter {

	// 默认选中第一个
	public static int selectedPosition = -1;

	private Context context;
	private List<Map<String, String>> list;

	public DeptZntzckSignSelectAdapter(Context context, List<Map<String, String>> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void changeDate(List<Map<String, String>> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.forex_acc_setting_item, null);
			holder = new ViewHolder();
			holder.forexStyle = (TextView) convertView.findViewById(R.id.forex_acc_type);
			holder.forexAlias = (TextView) convertView.findViewById(R.id.forex_acc_alias);
			holder.forexCountNum = (TextView) convertView.findViewById(R.id.forex_acc_accountnumber);
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
			holder.img.setVisibility(View.GONE);
		}
		String accountType = map.get(Comm.ACCOUNT_TYPE);
		String accountNumber = map.get(Comm.ACCOUNTNUMBER);
		String nickName = map.get(Comm.NICKNAME);
		String type = null;
		if (StringUtil.isNull(accountType)) {
			type = "-";
		} else {
			if (LocalData.AccountType.containsKey(accountType)) {
				type = LocalData.AccountType.get(accountType);
			}
		}
		String accountNumbers = null;
		if (StringUtil.isNull(accountNumber)) {
			accountNumbers = "-";
		} else {
			accountNumbers = StringUtil.getForSixForString(accountNumber);
		}
		holder.forexStyle.setText(type);
		holder.forexAlias.setText(nickName);
		holder.forexCountNum.setText(accountNumbers);
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
		public TextView forexStyle;
		/**
		 * 账户别名
		 */
		public TextView forexAlias;
		/**
		 * 账户号码
		 */
		public TextView forexCountNum;

		public ImageView img;
	}
}
