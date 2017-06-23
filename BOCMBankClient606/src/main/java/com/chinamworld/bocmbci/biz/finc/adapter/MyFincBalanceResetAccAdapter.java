package com.chinamworld.bocmbci.biz.finc.adapter;

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
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

public class MyFincBalanceResetAccAdapter extends BaseAdapter {
	private static final String TAG = "MyFincBalanceResetAccAdapter";
	// 默认选中第一个
	public  int selectedPosition = -1;

	private Context context;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> list;

	public MyFincBalanceResetAccAdapter(Context context,
			List<Map<String, Object>> list) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.list = list;
		this.selectedPosition = -1;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.finc_myfinc_balance_reset_submit_item, null);
			holder = new ViewHolder();
			holder.fincxStyle = (TextView) convertView
					.findViewById(R.id.finc_accType);
			holder.fincxAlias = (TextView) convertView
					.findViewById(R.id.finc_accAlias);
			holder.fincxCountNum = (TextView) convertView
					.findViewById(R.id.finc_accNumber);
			holder.img = (ImageView) convertView
					.findViewById(R.id.imageViewright);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 选中的颜色
		if (position == selectedPosition) {
			convertView
					.setBackgroundResource(R.drawable.bg_for_listview_item_half_black);
			holder.img.setVisibility(View.VISIBLE);
		} else {
			convertView
					.setBackgroundResource(R.drawable.bg_for_listview_item_write);
			holder.img.setVisibility(View.GONE);
		}
		holder.fincxStyle.setTag(position);
		holder.fincxAlias.setTag(position);
		holder.fincxCountNum.setTag(position);
		holder.img.setTag(position);
		
		
		Map<String, Object> map = list.get(position);
		String accountType = (String) map.get(Finc.FINC_QUERYACCLIST_ACCOUNTTYPE);
		String nickName = (String) map.get(Finc.FINC_QUERYACCLIST_NICKNAME);
		String accountNumber = (String) map.get(Finc.FINC_QUERYACCLIST_ACCOUNTNUMBER);
		
		
		holder.fincxStyle.setText(LocalData.AccountType.get(accountType));
		if (nickName == null) {
			holder.fincxAlias.setText(" ");
		} else {
			holder.fincxAlias.setText(nickName);
		}
		String number = accountNumber;
		number = StringUtil.getForSixForString(number);
		holder.fincxCountNum.setText(number);

		
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
//				holder.fincxStyle);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
//				holder.fincxAlias);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
//				holder.fincxCountNum);
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
		public TextView fincxStyle;
		/**
		 * 账户别名
		 */
		public TextView fincxAlias;
		/**
		 * 账户号码
		 */
		public TextView fincxCountNum;
		public ImageView img;

	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
	}
	
}
