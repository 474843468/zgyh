package com.chinamworld.bocmbci.biz.tran.twodimentrans.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 选择账户
 * 
 * @author wangchao
 * 
 */
public class SelectAccAdapter extends BaseAdapter {
	private Context mContext;
	private List<Map<String, Object>> manageTransList = null;
	private int selectedPosition;

	public SelectAccAdapter(Context context,
			List<Map<String, Object>> manageTransList, int selectedPosition) {
		this.mContext = context;
		this.manageTransList = manageTransList;
		this.selectedPosition = selectedPosition;
	}

	public void dataChanaged(List<Map<String, Object>> manageTransList,
			int selectedPosition) {
		this.manageTransList = manageTransList;
		this.selectedPosition = selectedPosition;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return manageTransList.size();
	}

	@Override
	public Object getItem(int position) {
		return manageTransList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView grayItemIv = null;
		if(convertView == null){
			convertView = View.inflate(mContext,
					R.layout.tran_2dimen_accout_list_select_acc, null);
		}

		TextView accountTypeTv = (TextView) convertView
				.findViewById(R.id.tv_cardName_2dimen_accOut_list_select);
		TextView nickNameTv = (TextView) convertView
				.findViewById(R.id.tv_cardType_2dimen_accOut_list_select);
		TextView accountNumberTypeTv = (TextView) convertView
				.findViewById(R.id.tv_cardNum_2dimen_accountNumber_accOut_list_select);

		// TextView availableBalanceView = (TextView) convertView
		// .findViewById(R.id.tv_lastprice_accOut_list_detail);

		grayItemIv = (ImageView) convertView
				.findViewById(R.id.tran_imageViewright);

		if (position == selectedPosition) {
			convertView
					.setBackgroundResource(R.drawable.bg_for_listview_item_half_black);
			grayItemIv.setVisibility(View.VISIBLE);
		} else {
			grayItemIv.setVisibility(View.INVISIBLE);
			convertView
					.setBackgroundResource(R.drawable.bg_for_listview_item_write);
		}

		Map<String, Object> map = manageTransList.get(position);

		if (map != null) {
			accountTypeTv.setText(LocalData.AccountType.get((String) map
					.get(Tran.ACCOUNTTYPE_RES)));
			nickNameTv.setText((String) map.get(Tran.NICKNAME_RES));
			// availableBalanceView.setText((String)
			// map.get(Tran.AVAILABLEBALANCE_RES));
			accountNumberTypeTv.setText(StringUtil
					.getForSixForString((String) map
							.get(Tran.ACCOUNTNUMBER_RES)));
		}

		return convertView;
	}
}
