package com.chinamworld.bocmbci.biz.acc.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 账户列表适配器
 * 
 * @author wangmengmeng
 * 
 */
public class BankAccountAdapter extends BaseAdapter {
	/** 账户列表信息 */
	private List<Map<String, Object>> bankAccountList;
	private Context context;
	/** 账户详情点击事件 */
	private OnItemClickListener onbanklistItemDetailClickListener;
	/**
	 * i用来判断是否是取消关联 i=1,正常;i=2,取消关联;i=3,选择账户使用
	 * */
	private int i = 0;
	/** 选中项记录 */
	private int selectedPosition = -1;
	/** 取消账户关联事件 */
	private OnItemClickListener onbanklistCancelRelationClickListener;

	public BankAccountAdapter(Context context,
			List<Map<String, Object>> bankAccountList, int i) {
		this.context = context;
		this.bankAccountList = bankAccountList;
		this.i = i;
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.acc_mybankaccount_list_item, null);
		}
		LinearLayout ll_back_top = (LinearLayout) convertView
				.findViewById(R.id.ll_bankact_top);
		ImageView checked = (ImageView) convertView
				.findViewById(R.id.imageViewright);
		/** 账户类型 */
		TextView tv_acc_type_value = (TextView) convertView
				.findViewById(R.id.acc_type_value);
		String account_type = String.valueOf(bankAccountList.get(position).get(
				Acc.ACC_ACCOUNTTYPE_RES));
		tv_acc_type_value
				.setText(StringUtil.isNull(account_type) ? ConstantGloble.BOCINVT_DATE_ADD
						: LocalData.AccountType.get(account_type.trim()));
		/** 账户别名 */
		TextView tv_acc_account_nickname = (TextView) convertView
				.findViewById(R.id.acc_account_nickname);

		String account_nickname = String.valueOf(bankAccountList.get(position)
				.get(Acc.ACC_NICKNAME_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				tv_acc_account_nickname);

		tv_acc_account_nickname
				.setText(StringUtil.isNull(account_nickname) ? ConstantGloble.BOCINVT_DATE_ADD
						: account_nickname);
		/** 账户账号 */
		TextView tv_acc_account_num = (TextView) convertView
				.findViewById(R.id.acc_account_num);
		String acc_account_num = String.valueOf(bankAccountList.get(position)
				.get(Acc.ACC_ACCOUNTNUMBER_RES));
		tv_acc_account_num.setText(StringUtil
				.getForSixForString(acc_account_num));
		/** 账户详情查看按钮 */
		ImageView img_acc_detail = (ImageView) convertView
				.findViewById(R.id.img_acc_detail);
		img_acc_detail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (onbanklistItemDetailClickListener != null) {
					onbanklistItemDetailClickListener.onItemClick(null, v,
							position, position);
				}
			}
		});

		ImageView acc_btn_gocancelrelation = (ImageView) convertView
				.findViewById(R.id.acc_btn_gocancelrelation);
		ImageView acc_go_item = (ImageView) convertView
				.findViewById(R.id.acc_btn_goitem);
		FrameLayout fl = (FrameLayout) convertView.findViewById(R.id.acc_frame);
		if (i == 1) {
			acc_go_item.setVisibility(View.VISIBLE);
			acc_btn_gocancelrelation.setVisibility(View.GONE);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (onbanklistItemDetailClickListener != null) {
						onbanklistItemDetailClickListener.onItemClick(null, v,
								position, position);
					}
				}
			});
		} else if (i == 2) {
			acc_go_item.setVisibility(View.GONE);
			acc_btn_gocancelrelation.setVisibility(View.VISIBLE);
		} else if (i == 3) {
			RelativeLayout back = (RelativeLayout) convertView
					.findViewById(R.id.back);
			back.setVisibility(View.GONE);
			fl.setVisibility(View.GONE);
			ll_back_top.setVisibility(View.GONE);
			acc_go_item.setVisibility(View.GONE);
			acc_btn_gocancelrelation.setVisibility(View.GONE);
			img_acc_detail.setVisibility(View.GONE);
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
		}

		fl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onbanklistCancelRelationClickListener != null) {
					onbanklistCancelRelationClickListener.onItemClick(null, v,
							position, position);
				}
			}
		});
		acc_btn_gocancelrelation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (onbanklistCancelRelationClickListener != null) {
					onbanklistCancelRelationClickListener.onItemClick(null, v,
							position, position);
				}
			}
		});

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

	public int getSelectedPosition() {
		return selectedPosition;
	}

	public OnItemClickListener getOnbanklistItemDetailClickListener() {
		return onbanklistItemDetailClickListener;
	}

	public void setOnbanklistItemDetailClickListener(
			OnItemClickListener onbanklistItemDetailClickListener) {
		this.onbanklistItemDetailClickListener = onbanklistItemDetailClickListener;
	}

	public OnItemClickListener getOnbanklistCancelRelationClickListener() {
		return onbanklistCancelRelationClickListener;
	}

	public void setOnbanklistCancelRelationClickListener(
			OnItemClickListener onbanklistCancelRelationClickListener) {
		this.onbanklistCancelRelationClickListener = onbanklistCancelRelationClickListener;
	}

}
