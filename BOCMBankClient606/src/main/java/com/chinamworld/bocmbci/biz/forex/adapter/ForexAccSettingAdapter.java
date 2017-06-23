package com.chinamworld.bocmbci.biz.forex.adapter;

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
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

public class ForexAccSettingAdapter extends BaseAdapter {
	// 默认选中第一个
	public static int selectedPosition = -1;

	private Context context;
	private LayoutInflater mInflater;
	private List<Map<String, String>> list;

	public ForexAccSettingAdapter(Context context, List<Map<String, String>> list) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.list = list;
	}

	public ForexAccSettingAdapter(Context context) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.forex_acc_setting_item, null);
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
		// 账户类型 TODO 改成内容
		String types = map.get(Forex.FOREX_ACCOUNTTYPE_RES);
		String type = null;
		if (LocalData.AccountType.containsKey(types)) {
			type = LocalData.AccountType.get(types);
		}
		String alias = map.get(Forex.FOREX_NICKNAME_RES);
		String account = map.get(Forex.FOREX_ACCOUNTNUMBER_RES);
		String accountNumber = StringUtil.getForSixForString(account);
		if (StringUtil.isNullOrEmpty(type)) {
			holder.forexStyle.setText(types);
		} else {
			holder.forexStyle.setText(type);
		}

		holder.forexAlias.setText(alias);
		holder.forexCountNum.setText(accountNumber);
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
