package com.chinamworld.bocmbci.biz.epay.myPayService.bomobile.adapter;

import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

public class AccountListAdapter extends BaseAdapter {

	private BaseActivity context;

	private String tag = "EPayAccountListAdapter";

	private int selectedPosition = -1;

	private Context bomTransContext;

	public AccountListAdapter(BaseActivity context) {
		bomTransContext = TransContext.getBomContext();
		this.context = context;
	}

	@Override
	public int getCount() {
		return bomTransContext.getList(PubConstants.CONTEXT_FIELD_ALL_ACCLIST).size();
	}

	@Override
	public Object getItem(int position) {
		return bomTransContext.getList(PubConstants.CONTEXT_FIELD_ALL_ACCLIST).get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.epay_bom_service_open_account_list_item, null);
			holder.tv_accType = (TextView) convertView.findViewById(R.id.tv_acc_type);
			holder.tv_nickName = (TextView) convertView.findViewById(R.id.tv_nickname);
			holder.tv_account = (TextView) convertView.findViewById(R.id.tv_account);
//			holder.img_gou = (ImageView) convertView.findViewById(R.id.img_gou);
			// holder.tv_currency =
			// (TextView)convertView.findViewById(R.id.epay_service_open_account_select_tv_currencycode);
			// holder.tv_availableBalance =
			// (TextView)convertView.findViewById(R.id.epay_service_open_account_select_tv_availablebalance);
			// holder.tv_field_balanace =
			// (TextView)convertView.findViewById(R.id.epay_service_open_account_select_tv_field_balance);
			holder.rl_item = (RelativeLayout) convertView.findViewById(R.id.rl_item);
			holder.rl_image = (RelativeLayout) convertView.findViewById(R.id.rl_selected_img);
			holder.rl_image.setVisibility(View.GONE);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map<Object, Object> tempMap = EpayUtil.getMap(bomTransContext.getList(PubConstants.CONTEXT_FIELD_ALL_ACCLIST).get(position));
		String accType = EpayUtil.getString(tempMap.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_TYPE), "");
		holder.tv_accType.setText(LocalData.AccountType.get(accType));
		holder.tv_nickName.setText(EpayUtil.getString(tempMap.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_NICKNAME), ""));
		holder.tv_account.setText(StringUtil.getForSixForString(EpayUtil.getString(tempMap.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NUMBER), "")));
		// holder.tv_currency.setText(LocalData.Currency.get(EpayUtil.getString(tempMap.get(PubConstants.METHOD_QUERY_ACCOUNT_DETAIL_FIELD_CURRENCY_CODE), "");

		if (!(selectedPosition == -1)) {

			boolean isSelected = false;

			if (selectedPosition != position) {
				isSelected = EpayUtil.getBoolean(tempMap.get(PubConstants.PUB_FIELD_ISSELECTED));
			} else {
				Map<Object, Object> selectedPositionMap = EpayUtil.getMap(bomTransContext.getList(PubConstants.CONTEXT_FIELD_ALL_ACCLIST).get(selectedPosition));
				isSelected = EpayUtil.getBoolean(selectedPositionMap.get(PubConstants.PUB_FIELD_ISSELECTED));
			}

			if (isSelected) {
				holder.rl_image.setVisibility(View.VISIBLE);
//				holder.ll_item.setBackgroundResource(R.drawable.bg_for_listview_item_half_black);
//				holder.img_gou.setVisibility(View.VISIBLE);
			} else {
				holder.rl_image.setVisibility(View.GONE);
//				holder.ll_item.setBackgroundResource(R.drawable.acc_card);
//				holder.img_gou.setVisibility(View.GONE);
			}
		}

		return convertView;
	}

	class ViewHolder {
		RelativeLayout rl_item;
		RelativeLayout rl_image;
		TextView tv_accType;
		TextView tv_nickName;
		TextView tv_account;
//		ImageView img_gou;
		// TextView tv_currency;
		// TextView tv_field_balanace;
		// TextView tv_availableBalance;
	}

	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
		notifyDataSetChanged();
	}

}
