package com.chinamworld.bocmbci.biz.bocinvt.adapter;

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
/**功能外置  选择账号列表信息 适配器*/
public class BuyProductChooseCardOutlayAdapter extends BaseAdapter{

	private Context mContext;
	private List<Map<String, Object>> cardList;
	private int selectedPosition = -1;

	public BuyProductChooseCardOutlayAdapter(Context cn, List<Map<String, Object>> list) {
		this.mContext = cn;
		this.cardList = list;
	}

	@Override
	public int getCount() {
		return cardList.size();
	}

	@Override
	public Object getItem(int position) {
		return cardList.get(position);
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
			convertView = LinearLayout.inflate(mContext, R.layout.buy_choose_card_outlay_item, null);
			viewHodler.tvAccType = (TextView) convertView.findViewById(R.id.acc_type_value);
			viewHodler.tvNickname = (TextView) convertView.findViewById(R.id.acc_account_nickname);
			viewHodler.tvAccNum = (TextView) convertView.findViewById(R.id.acc_account_num);
			viewHodler.imChecked = (ImageView) convertView.findViewById(R.id.imageViewright);
			viewHodler.layoutAccMsg = (FrameLayout) convertView.findViewById(R.id.ll_acc_msg);
			viewHodler.productLeftBg = (ImageView) convertView
					.findViewById(R.id.left_bg);
			convertView.setTag(viewHodler);
		} else {
			viewHodler = (ViewHodler) convertView.getTag();
		}
		if (position % 3 == 0) {
			viewHodler.productLeftBg.setBackgroundResource(R.drawable.outlay_left_fen);
		} else if (position % 3 == 1) {
			viewHodler.productLeftBg.setBackgroundResource(R.drawable.outlay_left_ceng);
		} else if (position % 3 == 2) {
			viewHodler.productLeftBg.setBackgroundResource(R.drawable.outlay_left_lan);
		}
//		else if (position % 5 == 3) {
//			viewHodler.productLeftBg.setBackgroundResource(R.drawable.outlay_left_lv);
//		} else if (position % 5 == 4) {
//			viewHodler.productLeftBg.setBackgroundResource(R.drawable.outlay_left_huang);
//		}
		String acc_type = String.valueOf(cardList.get(position).get(Acc.ACC_ACCOUNTTYPE_RES));
		viewHodler.tvAccType.setText(LocalData.AccountType.get(acc_type.trim()));
		viewHodler.tvNickname.setText(prodEnd(String.valueOf(cardList.get(position).get(Acc.ACC_NICKNAME_RES))));
		String acc_account_num = String.valueOf(cardList.get(position).get("accountNo")); 
		viewHodler.tvAccNum.setText(StringUtil.getForSixForString(acc_account_num));

		if (position == selectedPosition) {
			viewHodler.layoutAccMsg.setBackgroundResource(R.drawable.outlay_choose_selectaccbg);
			viewHodler.imChecked.setVisibility(View.GONE);
		} else {
			viewHodler.layoutAccMsg.setBackgroundResource(R.drawable.bg_for_outlay_choose);
			viewHodler.imChecked.setVisibility(View.GONE);
		}

		return convertView;
	}

	
	private String prodEnd(String prodEnd){
		if (!StringUtil.isNull(prodEnd)) {	
			if(!prodEnd.equals("null")){
				return prodEnd;	
			}		
		}
		return "-";
	}
	
	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
		notifyDataSetChanged();
	}

	public int getSelectedPosition() {
		return this.selectedPosition;
	}
	private static class ViewHodler {
		public TextView tvAccType;
		public TextView tvNickname;
		public TextView tvAccNum;
		public ImageView imChecked;
		public FrameLayout layoutAccMsg;
		public ImageView productLeftBg;
	}
}
