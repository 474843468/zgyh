package com.chinamworld.bocmbci.biz.acc.adapter;

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
 * 临时挂失list适配器 可进行单项选择
 * 
 * @author wangmengmeng
 * 
 */
public class LossBankAccountAdapter extends BaseAdapter {
	/** 账户列表信息 */
	private List<Map<String, Object>> bankAccountList;
	private Context context;
	/** 选中项记录 */
	private int selectedPosition = -1;

	public LossBankAccountAdapter(Context context,
			List<Map<String, Object>> bankAccountList) {
		this.context = context;
		this.bankAccountList = bankAccountList;
	}

	@Override
	public int getCount() {

		return bankAccountList.size();
	}

	@Override
	public Object getItem(int position) {

		return bankAccountList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	/**
	 * 给被选中的某一项设置高亮背景
	 * 
	 * @param selectedPosition
	 * @author nl.
	 */
	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.acc_mybankaccount_list_item, null);
		}
		RelativeLayout back=(RelativeLayout)convertView.findViewById(R.id.back);
		back.setVisibility(View.GONE);
		/** 头部 */
		LinearLayout ll_back_top = (LinearLayout) convertView
				.findViewById(R.id.ll_bankact_top);
		ll_back_top.setVisibility(View.GONE);
		/** 账户类型 */
		TextView tv_acc_type_value = (TextView) convertView
				.findViewById(R.id.acc_type_value);
		String acc_type = String.valueOf(bankAccountList.get(position).get(
				Acc.ACC_ACCOUNTTYPE_RES));
		tv_acc_type_value.setText(LocalData.AccountType.get(acc_type.trim()));
		/** 账户别名 */
		TextView tv_acc_account_nickname = (TextView) convertView
				.findViewById(R.id.acc_account_nickname);
		tv_acc_account_nickname.setText(String.valueOf(bankAccountList.get(
				position).get(Acc.ACC_NICKNAME_RES)));
		/** 账户账号 */
		TextView tv_acc_account_num = (TextView) convertView
				.findViewById(R.id.acc_account_num);
		String acc_account_num = String.valueOf(bankAccountList.get(position)
				.get(Acc.ACC_ACCOUNTNUMBER_RES));
		tv_acc_account_num.setText(StringUtil
				.getForSixForString(acc_account_num));

		ImageView acc_btn_goitem = (ImageView) convertView
				.findViewById(R.id.acc_btn_goitem);
		acc_btn_goitem.setVisibility(View.GONE);
		ImageView img_acc_detail = (ImageView) convertView
				.findViewById(R.id.img_acc_detail);
		img_acc_detail.setVisibility(View.GONE);

		ImageView checked = (ImageView) convertView
				.findViewById(R.id.imageViewright);
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
		FrameLayout acc_frame = (FrameLayout) convertView
				.findViewById(R.id.acc_frame);
		acc_frame.setVisibility(View.GONE);
		return convertView;
	}
}
