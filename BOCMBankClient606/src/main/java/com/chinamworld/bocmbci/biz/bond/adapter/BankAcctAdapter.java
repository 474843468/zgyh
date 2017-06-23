package com.chinamworld.bocmbci.biz.bond.adapter;

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
/**
 * 资金账户ListView适配器
 * @author panwe
 *
 */
public class BankAcctAdapter extends BaseAdapter{
	
	private Context mContext;
	
	private List<Map<String, Object>> cardList;
	
	private int selectedPosition = -1;
	
	public BankAcctAdapter(Context cn,List<Map<String, Object>> list){
		this.mContext = cn;
		this.cardList = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (cardList == null || cardList.size() == 0) {
			return 0;
		}else{
			return cardList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (cardList == null || cardList.size() == 0) {
			return null;
		}else{
			return cardList.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHodler h;
		if (convertView == null) {
			h = new ViewHodler();
			convertView = LinearLayout.inflate(mContext, R.layout.bond_card_item, null);
			h.tvAccType = (TextView) convertView.findViewById(R.id.acc_type_value);
			h.tvNickname = (TextView) convertView.findViewById(R.id.acc_account_nickname);
			h.tvAccNum = (TextView) convertView.findViewById(R.id.acc_account_num);
			
			h.imChecked = (ImageView) convertView.findViewById(R.id.imageViewright);
			
			h.layoutAccMsg = (FrameLayout) convertView
					.findViewById(R.id.ll_acc_msg);
			convertView.setTag(h);
		}else{
			h = (ViewHodler) convertView.getTag();
		}
		String acc_type = String.valueOf(cardList.get(position).get(
				Acc.ACC_ACCOUNTTYPE_RES));
		h.tvAccType.setText(LocalData.AccountType.get(acc_type.trim()));
		
		h.tvNickname.setText(String.valueOf(cardList.get(
				position).get(Acc.ACC_NICKNAME_RES)));
		
		String acc_account_num = String.valueOf(cardList.get(position)
				.get(Acc.ACC_ACCOUNTNUMBER_RES));
		h.tvAccNum.setText(StringUtil
				.getForSixForString(acc_account_num));
		
		if (position == selectedPosition) {
			h.layoutAccMsg
					.setBackgroundResource(R.drawable.bg_for_listview_item_half_black);
			h.imChecked.setVisibility(View.VISIBLE);
		} else {
			h.layoutAccMsg
					.setBackgroundResource(R.drawable.bg_for_listview_item_write);
			h.imChecked.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	
	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
		notifyDataSetChanged();
	}
	
	public class ViewHodler {
		public TextView tvAccType;
		public TextView tvNickname;
		public TextView tvAccNum;
		public ImageView imChecked;
		public FrameLayout layoutAccMsg;
	}
}
