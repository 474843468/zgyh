package com.chinamworld.bocmbci.biz.setting.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

public class SettingAccountAdapter extends BaseAdapter{
	private Context mContext;

	private List<Map<String, Object>> cardList;

	private int selectedPosition = -1;

	public SettingAccountAdapter(Context cn, List<Map<String, Object>> list) {
		this.mContext = cn;
		this.cardList = list;
	}

	@Override
	public int getCount() {
		if (cardList == null || cardList.size() == 0) {
			return 0;
		} else {
			return cardList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		if (cardList == null || cardList.size() <= position) {
			return null;
		} else {
			return cardList.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHodler viewHodler;
		if (convertView == null) {
			viewHodler = new ViewHodler();
			convertView = LinearLayout.inflate(mContext, R.layout.third_openacc_card_item, null);
			viewHodler.tvAccType = (TextView) convertView.findViewById(R.id.acc_type_value);
			viewHodler.tvNickname = (TextView) convertView.findViewById(R.id.acc_account_nickname);
			viewHodler.tvAccNum = (TextView) convertView.findViewById(R.id.acc_account_num);

			viewHodler.imChecked = (ImageView) convertView.findViewById(R.id.imageViewright);

			viewHodler.layoutAccMsg = (FrameLayout) convertView.findViewById(R.id.ll_acc_msg);
			convertView.setTag(viewHodler);
		} else {
			viewHodler = (ViewHodler) convertView.getTag();
		}
		String acc_type = String.valueOf(cardList.get(position).get(Acc.ACC_ACCOUNTTYPE_RES));
		viewHodler.tvAccType.setText(LocalData.AccountType.get(acc_type.trim()));

		viewHodler.tvNickname.setText(String.valueOf(cardList.get(position).get(Acc.ACC_NICKNAME_RES)));

		String acc_account_num = String.valueOf(cardList.get(position).get(Acc.ACC_ACCOUNTNUMBER_RES));
		viewHodler.tvAccNum.setText(StringUtil.getForSixForString(acc_account_num));

		if (position == selectedPosition) {
			viewHodler.layoutAccMsg.setBackgroundResource(R.drawable.bg_for_listview_item_half_black);
			viewHodler.imChecked.setVisibility(View.VISIBLE);
		} else {
			viewHodler.layoutAccMsg.setBackgroundResource(R.drawable.bg_for_listview_item_write);
			viewHodler.imChecked.setVisibility(View.GONE);
		}

		return convertView;
	}

	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
		notifyDataSetChanged();
	}
	
	public void setData(List<Map<String, Object>> list) {
		this.cardList = list;
		notifyDataSetChanged();
	}

	private static class ViewHodler {
		public TextView tvAccType;
		public TextView tvNickname;
		public TextView tvAccNum;
		public ImageView imChecked;
		public FrameLayout layoutAccMsg;
	}
}
