package com.chinamworld.bocmbci.biz.infoserve.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Push;
import com.chinamworld.bocmbci.biz.infoserve.NonFixedProductRemindAccountListActivity;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

public class NonFixedProductRemindAccListAdapter extends BaseAdapter {

	private Activity context = null;

	private List<Map<String, Object>> accountList = null;

	private OnItemClickListener imageItemClickListener = null;

	private List<Boolean> isShowList;

	public NonFixedProductRemindAccListAdapter(Activity context, List<Map<String, Object>> accountList) {
		this.context = context;
		this.accountList = accountList;
		isShowList = new ArrayList<Boolean>();
		for (int i = 0; i < accountList.size(); i++) {
			isShowList.add(false);
		}
	}

	@Override
	public int getCount() {
		return accountList.size();
	}

	public List<Map<String, Object>> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<Map<String, Object>> accountList) {
		this.accountList = accountList;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.service_non_fixed_product_remind_acc_list_item,
					null);
		}
		TextView accTypeTv = (TextView) convertView.findViewById(R.id.acc_type_tv);
		TextView cardDescriptionTv = (TextView) convertView.findViewById(R.id.card_description_tv);
		TextView accNoTv = (TextView) convertView.findViewById(R.id.account_num_tv);
		String strAccountType = LocalData.AccountType.get((String) accountList.get(position).get(Comm.ACCOUNT_TYPE));
		accTypeTv.setText(strAccountType);

		cardDescriptionTv.setText((String) accountList.get(position).get(Comm.NICKNAME));
		String accountNumber = (String) accountList.get(position).get(Comm.ACCOUNTNUMBER);
		accNoTv.setText(StringUtil.getForSixForString(accountNumber));

		final ImageButton downOrUpBtn = (ImageButton) convertView.findViewById(R.id.down_or_up_btn);

		TextView sign_state_tv = (TextView) convertView.findViewById(R.id.sign_state_tv);

		final View hor_line = (View) convertView.findViewById(R.id.hor_line);

		final LinearLayout detail_llyt = (LinearLayout) convertView.findViewById(R.id.detail_llyt);

		LinearLayout sign_llyt = (LinearLayout) convertView.findViewById(R.id.sign_llyt);
		LinearLayout unsign_llyt = (LinearLayout) convertView.findViewById(R.id.unsign_llyt);
		LinearLayout acc_info_llyt = (LinearLayout) convertView.findViewById(R.id.acc_info_llyt);

		String signFlag = null;
		final Map<String, Object> detailInfo = (Map<String, Object>) accountList.get(position).get(
				Push.NON_FIXED_DETAIL);
		if (detailInfo != null) {
			signFlag = (String) detailInfo.get(Push.SIGN_FLAG);
		} else {
			detail_llyt.setVisibility(View.GONE);
			hor_line.setVisibility(View.GONE);
		}

		if (isShowList.get(position)) {
			detail_llyt.setVisibility(View.VISIBLE);
			hor_line.setVisibility(View.VISIBLE);
		} else {
			detail_llyt.setVisibility(View.GONE);
			hor_line.setVisibility(View.GONE);
		}

		if (detail_llyt.getVisibility() == View.GONE) {
			// 关闭状态
			downOrUpBtn.setImageResource(R.drawable.img_arrow_click_down);
			// downOrUpBtn.setOnClickListener(new View.OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// isShowList.set(position, true);
			// if (detailInfo == null) {
			// // 需要查询
			// ((NonFixedProductRemindAccountListActivity) context)
			// .requestPsnNonFixedProductRemindQuery(
			// (String) accountList.get(position).get(
			// Comm.ACCOUNT_ID), position);
			// } else {
			// notifyDataSetChanged();
			// }
			// }
			// });
		} else {
			// 打开状态
			downOrUpBtn.setImageResource(R.drawable.img_arrow_click_up);
			// downOrUpBtn.setOnClickListener(new View.OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// isShowList.set(position, false);s
			// notifyDataSetChanged();
			// }
			// });
		}
		downOrUpBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!isShowList.get(position)) {
					// 关闭状态时
					isShowList.set(position, true);
//					if (detailInfo == null) {
						// 需要查询
						((NonFixedProductRemindAccountListActivity) context).requestPsnNonFixedProductRemindQuery(
								(String) accountList.get(position).get(Comm.ACCOUNT_ID), position);
//					} else {
//						notifyDataSetChanged();
//					}
				} else {
					// 打开状态时
					isShowList.set(position, false);
					notifyDataSetChanged();
				}

			}
		});
		acc_info_llyt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				downOrUpBtn.performClick();
			}
		});

		if (signFlag == null || "".equals(signFlag)) {
			return convertView;
		}
		if (Push.NON_FIXED_SIGNED.equals(signFlag)) {
			// 已签约
			sign_state_tv.setText(context.getResources().getString((R.string.infoserve_daedaozhang_sign_state))
					+ context.getResources().getString((R.string.infoserve_daedaozhang_signed)));
			sign_llyt.setVisibility(View.GONE);
			unsign_llyt.setVisibility(View.VISIBLE);
		} else if (Push.NON_FIXED_UNSIGNED.equals(signFlag)) {
			// 未签约
			sign_state_tv.setText(context.getResources().getString((R.string.infoserve_daedaozhang_sign_state))
					+ context.getResources().getString((R.string.infoserve_daedaozhang_not_signed)));
			sign_llyt.setVisibility(View.VISIBLE);
			unsign_llyt.setVisibility(View.GONE);
		}
		sign_llyt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 操作类型，增-A，删-D，改-U
				detailInfo.put(Push.NON_FIXED_OPT_FLAG, Push.NON_FIXED_OPT_ADD);
				((NonFixedProductRemindAccountListActivity) context).nonFixedProductRemindSign((String) accountList
						.get(position).get(Comm.ACCOUNT_ID), position, detailInfo);
			}
		});
		unsign_llyt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 操作类型，增-A，删-D，改-U
				detailInfo.put(Push.NON_FIXED_OPT_FLAG, Push.NON_FIXED_OPT_DELETE);
				((NonFixedProductRemindAccountListActivity) context).nonFixedProductRemindDelete((String) accountList
						.get(position).get(Comm.ACCOUNT_ID), position, detailInfo);
			}
		});

		return convertView;
	}

	public OnItemClickListener getImageItemClickListener() {
		return imageItemClickListener;
	}

	public void setImageItemClickListener(OnItemClickListener imageItemClickListener) {
		this.imageItemClickListener = imageItemClickListener;
	}

	public void setShowState(int position, boolean state) {
		isShowList.set(position, state);
	}
}
