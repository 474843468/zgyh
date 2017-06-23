package com.chinamworld.bocmbci.biz.bocinvt.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 资金账户列表适配器
 * 
 * @author wangmengmeng
 * 
 */
public class BindingResetAdapter extends BaseAdapter {
	/** 账户列表信息 */
	private List<Map<String, Object>> bindingResetList;
	private Context context;
	/** 选中项记录 */
	private int selectedPosition = -1;

	public BindingResetAdapter(Context context,
			List<Map<String, Object>> bindingResetList) {
		this.context = context;
		this.bindingResetList = bindingResetList;
	}

	@Override
	public int getCount() {

		return bindingResetList.size();
	}

	@Override
	public Object getItem(int position) {

		return bindingResetList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.acc_mybankaccount_list_item, null);
		}
		RelativeLayout back = (RelativeLayout) convertView
				.findViewById(R.id.back);
		back.setVisibility(View.GONE);
		LinearLayout ll_back_top = (LinearLayout) convertView
				.findViewById(R.id.ll_bankact_top);
		ImageView checked = (ImageView) convertView
				.findViewById(R.id.imageViewright);
		/** 账户类型 */
		TextView tv_acc_type_value = (TextView) convertView
				.findViewById(R.id.acc_type_value);
		String account_type = String.valueOf(bindingResetList.get(position)
				.get(Acc.ACC_ACCOUNTTYPE_RES));
		tv_acc_type_value
				.setText(LocalData.AccountType.get(account_type.trim()));
		/** 账户别名 */
		TextView tv_acc_account_nickname = (TextView) convertView
				.findViewById(R.id.acc_account_nickname);
		tv_acc_account_nickname.setText(String.valueOf(bindingResetList.get(
				position).get(Acc.ACC_NICKNAME_RES)));
		/** 账户账号 */
		TextView tv_acc_account_num = (TextView) convertView
				.findViewById(R.id.acc_account_num);
		String acc_account_num = String.valueOf(bindingResetList.get(position)
				.get(Acc.ACC_ACCOUNTNUMBER_RES));
		tv_acc_account_num.setText(StringUtil
				.getForSixForString(acc_account_num));
		/** 账户详情查看按钮 */
		ImageView acc_btn_gocancelrelation = (ImageView) convertView
				.findViewById(R.id.acc_btn_gocancelrelation);
		ImageView acc_go_item = (ImageView) convertView
				.findViewById(R.id.acc_btn_goitem);
		acc_go_item.setFocusable(false);
		acc_btn_gocancelrelation.setFocusable(false);
		ll_back_top.setVisibility(View.GONE);
		acc_go_item.setVisibility(View.GONE);
		acc_btn_gocancelrelation.setVisibility(View.GONE);
		FrameLayout acc_frame = (FrameLayout) convertView
				.findViewById(R.id.acc_frame);
		acc_frame.setVisibility(View.GONE);
		acc_frame.setFocusable(false);
		FrameLayout ll_acc_msg = (FrameLayout) convertView
				.findViewById(R.id.ll_acc_msg);
		if (position == selectedPosition) {
			ll_acc_msg
					.setBackgroundResource(R.drawable.bg_for_listview_item_half_black);
			checked.setVisibility(View.VISIBLE);
		} else {
			ll_acc_msg
					.setBackgroundResource(R.drawable.bg_for_listview_item_write);
			checked.setVisibility(View.GONE);
		}
		return convertView;
	}

	/**
	 * 给被选中的某一项设置高亮背景
	 * 
	 * @param selectedPosition
	 */
	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
		notifyDataSetChanged();

	}

	/**
	 * @return the selectedPosition
	 */
	public int getSelectedPosition() {
		return selectedPosition;
	}
}
