package com.chinamworld.bocmbci.biz.epay.myPayService.withoutcard.adapter;

import java.util.List;
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

public class SPSAccountListAdapter extends BaseAdapter {

	private BaseActivity context;

	private String tag = "EPayOPSAccountListAdapter";

	private int selectedPosition = -1;

	private Context withoutCardTransContext;

	private List<Object> accountList;

	public SPSAccountListAdapter(BaseActivity context) {
		withoutCardTransContext = TransContext.getWithoutCardContext();
		accountList = withoutCardTransContext.getList(PubConstants.CONTEXT_FIELD_UN_DREDGED_ACCOUNT_LIST);
		this.context = context;
	}

	@Override
	public int getCount() {
		return accountList.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.epay_bom_service_open_account_list_item, null);
			holder.tv_accType = (TextView) convertView.findViewById(R.id.tv_acc_type);
			holder.tv_nickName = (TextView) convertView.findViewById(R.id.tv_nickname);
			holder.tv_account = (TextView) convertView.findViewById(R.id.tv_account);
			holder.rl_item = (RelativeLayout) convertView.findViewById(R.id.rl_item);
			// holder.img_gou = (ImageView)
			// convertView.findViewById(R.id.img_gou);
			holder.rl_image = (RelativeLayout) convertView.findViewById(R.id.rl_selected_img);
			holder.rl_image.setVisibility(View.GONE);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map<Object, Object> tempMap = EpayUtil.getMap(accountList.get(position));
		// 账户类型
		String accType = EpayUtil.getString(tempMap.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_TYPE), "");
		holder.tv_accType.setText(LocalData.AccountType.get(accType));
		// 账户别名
		holder.tv_nickName.setText(EpayUtil.getString(tempMap.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_NICKNAME), ""));
		// 账号
		holder.tv_account.setText(StringUtil.getForSixForString(EpayUtil.getString(tempMap.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NUMBER), "")));
		// 设置item大小
		// int[] size = { 598, 114 };
		// LayoutParams ll_image_params = holder.rl_image.getLayoutParams();
		// ll_image_params.height = EpayPubUtil.getScaleHeight(context, size);
		// LayoutParams ll_item_params = holder.ll_item.getLayoutParams();
		// ll_item_params.height = EpayPubUtil.getScaleHeight(context, size);

		if (selectedPosition == position) {
			holder.rl_image.setVisibility(View.VISIBLE);
			// holder.ll_item.setBackgroundResource(R.drawable.bg_for_listview_item_half_black);
			// holder.img_gou.setVisibility(View.VISIBLE);
		} else {
			// holder.ll_item.setBackgroundResource(R.drawable.acc_card);
			// holder.img_gou.setVisibility(View.GONE);
			holder.rl_image.setVisibility(View.GONE);
		}

		return convertView;
	}

	class ViewHolder {
		RelativeLayout rl_item;
		RelativeLayout rl_image;
		TextView tv_accType;
		TextView tv_nickName;
		TextView tv_account;
		// ImageView img_gou;
	}

	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
		notifyDataSetChanged();
	}

}
