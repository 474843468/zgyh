package com.chinamworld.bocmbci.biz.drawmoney.remitout;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: RemitOutListAdapter
 * @Description: 汇往收款人列表adapter
 * @author JiangWei
 * @date 2013-7-16 上午10:57:23
 */
public class RemitOutListAdapter extends BaseAdapter {
	private Context mContext;
	private List<Map<String, Object>> mList;
	private int selectedPosition = -1;;

	public RemitOutListAdapter(Context context, List<Map<String, Object>> list) {
		mContext = context;
		mList = list;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
		notifyDataSetChanged();
	}
	
	public void setData(List<Map<String, Object>> list){
		mList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.crcd_setup_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.type_value = (TextView) convertView
					.findViewById(R.id.crcd_type_value);
			viewHolder.account_nickname = (TextView) convertView
					.findViewById(R.id.crcd_account_nickname);
			viewHolder.currencycode_value = (TextView) convertView
					.findViewById(R.id.crcd_currencycode_value);
			viewHolder.account_num = (TextView) convertView
					.findViewById(R.id.crcd_account_num);
			viewHolder.bookbalance_value = (TextView) convertView
					.findViewById(R.id.crcd_bookbalance_value);
			viewHolder.ivdetail = (ImageView) convertView
					.findViewById(R.id.img_crcd_detail);
			viewHolder.ivgoitem = (ImageView) convertView
					.findViewById(R.id.crcd_btn_goitem);
			viewHolder.ivCancel = (ImageView) convertView
					.findViewById(R.id.crcd_btn_gocancelrelation);
			viewHolder.ivCurrCode = (ImageView) convertView
					.findViewById(R.id.img_crcd_currencycode);
			viewHolder.rightFlag = (ImageView) convertView
					.findViewById(R.id.imageViewright);
			viewHolder.ll_acc_msg = (FrameLayout) convertView
					.findViewById(R.id.ll_acc_msg);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (position == selectedPosition) {
			viewHolder.ll_acc_msg
					.setBackgroundResource(R.drawable.bg_for_listview_item_half_black);
			viewHolder.rightFlag.setVisibility(View.VISIBLE);
		} else {
			viewHolder.rightFlag.setVisibility(View.INVISIBLE);
			viewHolder.ll_acc_msg
					.setBackgroundResource(R.drawable.bg_for_listview_item_write);
		}

		Map<String, Object> map = mList.get(position);
		String accountType = String.valueOf(map.get(Crcd.CRCD_ACCOUNTTYPE_RES));
		String strAccountType = LocalData.AccountType.get(accountType);

		viewHolder.type_value.setText(strAccountType);
		viewHolder.account_nickname.setText((String) map
				.get(Crcd.CRCD_NICKNAME_RES));

		viewHolder.account_num.setText(StringUtil.getForSixForString(String
				.valueOf(map.get(Crcd.CRCD_ACCOUNTNUMBER_RES))));

		viewHolder.currencycode_value.setVisibility(View.GONE);
		viewHolder.bookbalance_value.setVisibility(View.GONE);
		viewHolder.ivgoitem.setVisibility(View.GONE);
		viewHolder.ivdetail.setVisibility(View.GONE);
		viewHolder.ivCurrCode.setVisibility(View.GONE);
		return convertView;
	}

	class ViewHolder {
		TextView type_value;
		TextView account_nickname;
		TextView currencycode_value;
		TextView account_num;
		TextView bookbalance_value;

		ImageView ivdetail;
		ImageView ivgoitem;
		ImageView ivCancel;
		ImageView ivCurrCode;
		ImageView rightFlag;
		FrameLayout ll_acc_msg;
	}

}
