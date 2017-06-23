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
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 账户列表适配器
 * 
 * @author wangmengmeng
 * 
 */
public class ACCBankAccountAdapter extends BaseAdapter {
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
	/** 默认账户 */
	private String defaultAccIdStr;

	private int startDragIndex;
	private int enDragIndex;
	private boolean isDragging = false;
	private String status;
	/**
	 * 当前指向的位置
	 */
	private int currentPointPosition = -1;

	public void setDateDrageChanged(int startDragIndex, int enDragIndex,
			boolean isDragging) {
		this.isDragging = isDragging;
		this.startDragIndex = startDragIndex;
		this.enDragIndex = enDragIndex;
		if (enDragIndex == -1 && currentPointPosition != -1) {
			this.startDragIndex = 0;
			this.enDragIndex = 0;
		}
		notifyDataSetChanged();
		currentPointPosition = enDragIndex;
	}

	public ACCBankAccountAdapter(Context context,
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
					R.layout.acc_mybank_list_item, null);
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
		
		String cardDescription= String.valueOf(bankAccountList.get(position).get(Crcd.CRCD_CARDDESCRIPTION));
		if(!StringUtil.isNullOrEmptyCaseNullString(cardDescription)){
			tv_acc_type_value.setText(cardDescription);
		}else{
			tv_acc_type_value
			.setText(StringUtil.isNull(account_type) ? ConstantGloble.BOCINVT_DATE_ADD
					: LocalData.AccountType.get(account_type.trim()));	
		}
		
		
		
		// Shader shader =new LinearGradient(0, 0, 0, 60,
		// Color.WHITE,context.getResources()
		// .getColor(R.color.red), TileMode.CLAMP);
		// tv_acc_type_value.getPaint().setShader(shader);

		/** 账户别名 */
		TextView tv_acc_account_nickname = (TextView) convertView
				.findViewById(R.id.acc_account_nickname);
		String account_nickname = (String) bankAccountList.get(position).get(
				Acc.ACC_NICKNAME_RES);
		tv_acc_account_nickname.setText(account_nickname);
		/** 账户账号 */
		TextView tv_acc_account_num = (TextView) convertView
				.findViewById(R.id.acc_account_num);
		String acc_account_num = String.valueOf(bankAccountList.get(position)
				.get(Acc.ACC_ACCOUNTNUMBER_RES));
		// PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
		// tv_acc_account_num);
		tv_acc_account_num.setText(StringUtil
				.getForSixForString(acc_account_num));
		ImageView defaultAccview = (ImageView) convertView
				.findViewById(R.id.iv_default);

		ImageView acc_btn_gocancelrelation = (ImageView) convertView
				.findViewById(R.id.acc_btn_gocancelrelation);
		ImageView acc_go_item = (ImageView) convertView
				.findViewById(R.id.acc_btn_goitem);
		String accId = (String) bankAccountList.get(position).get(
				Acc.ACC_ACCOUNTNUMBER_RES);
		if (!StringUtil.isNullOrEmpty(defaultAccIdStr)
				&& !StringUtil.isNullOrEmpty(status)
				&& accId.equals(defaultAccIdStr)
				&& status.equals(ConstantGloble.STATUS_SUCCESS)) {
			defaultAccview.setVisibility(View.VISIBLE);
		} else {
			defaultAccview.setVisibility(View.INVISIBLE);
		}
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
			acc_btn_gocancelrelation.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (onbanklistCancelRelationClickListener != null) {
						onbanklistCancelRelationClickListener.onItemClick(null,
								v, position, position);
					}
				}
			});
		} else if (i == 3) {
			RelativeLayout back = (RelativeLayout) convertView
					.findViewById(R.id.back);
			back.setVisibility(View.GONE);
			fl.setVisibility(View.GONE);
			ll_back_top.setVisibility(View.GONE);
			acc_go_item.setVisibility(View.GONE);
			acc_btn_gocancelrelation.setVisibility(View.GONE);
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

		if (isDragging) {
			if (position == startDragIndex) {
				// LogGloble.d("info",
				// ">>>>>>>>>position == startDragIndex<<<<<<<<<<<<<<");
				// AlphaAnimation animation = new AlphaAnimation(0.0f, 0.0f);
				// convertView.startAnimation(animation);
				// convertView
				// .setBackgroundDrawable(null);
				// ((RelativeLayout)convertView).removeAllViews();
				// convertView = null;
				convertView.setVisibility(View.INVISIBLE);
			} else if (position == enDragIndex && startDragIndex != position) {
				// AlphaAnimation animation = new AlphaAnimation(0, 1);
				// convertView.startAnimation(animation);
				convertView.setVisibility(View.VISIBLE);
				convertView
						.setBackgroundResource(R.drawable.shap_for_drag_list);
			} else {
				convertView.setVisibility(View.VISIBLE);
				convertView.setBackgroundDrawable(null);
			}

		} else {
			convertView.setVisibility(View.VISIBLE);
			convertView.setBackgroundDrawable(null);
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

	public void setDefaultAccIdStr(String defaultAccIdStr, String status) {
		this.defaultAccIdStr = defaultAccIdStr;
		this.status = status;
		notifyDataSetChanged();
	}

	public boolean isDragging() {
		return isDragging;
	}

	public void setDragging(boolean isDragging) {
		this.isDragging = isDragging;
	}

}
