package com.chinamworld.bocmbci.biz.epay.myPayService.withoutcard.adapter;

import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

public class DredgedAccountListAdapter extends BaseAdapter {

	private BaseActivity context;

	private Context withoutCardTransContext;

	private OnItemClickListener delItemClickListener;
	private OnItemClickListener settingClickListener;

	public DredgedAccountListAdapter(BaseActivity context) {
		withoutCardTransContext = TransContext.getWithoutCardContext();
		this.context = context;
	}

	@Override
	public int getCount() {
		return withoutCardTransContext.getList(PubConstants.CONTEXT_FIELD_DREDGED_LIST).size();
	}

	@Override
	public Object getItem(int position) {
		return withoutCardTransContext.getList(PubConstants.CONTEXT_FIELD_DREDGED_LIST).get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.epay_wc_dredged_account_list_item, null);
			holder.tv_cardType = (TextView) convertView.findViewById(R.id.tv_cardtype);
			holder.tv_nickName = (TextView) convertView.findViewById(R.id.tv_nickname);
			holder.tv_account = (TextView) convertView.findViewById(R.id.tv_account);
			// holder.tv_cust_max_quota = (TextView)
			// convertView.findViewById(R.id.tv_cust_max_quota);
//			holder.rl_item = (RelativeLayout) convertView.findViewById(R.id.rl_item);
			holder.ibt_del = (ImageButton) convertView.findViewById(R.id.bt_del);
			holder.ibt_setting = (ImageButton) convertView.findViewById(R.id.ibt_setting);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map<Object, Object> tempMap = EpayUtil.getMap(withoutCardTransContext.getList(PubConstants.CONTEXT_FIELD_DREDGED_LIST).get(position));
		holder.tv_cardType.setText(LocalData.AccountType.get(EpayUtil.getString(tempMap.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_TYPE), "")));
		holder.tv_nickName.setText(EpayUtil.getString(tempMap.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_NICKNAME), ""));
		holder.tv_account.setText(StringUtil.getForSixForString(EpayUtil.getString(tempMap.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NUMBER), "")));
		// holder.tv_cust_max_quota.setText(StringUtil.parseStringPattern(EpayUtil.getString(tempMap.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_QUOTA),
		// ""), 2));
		// 设置item大小
		// int[] size = {598, 114};
		// LayoutParams ll_item_params = holder.ll_item.getLayoutParams();
		// int card_height = EpayPubUtil.getScaleHeight(context, size);
		// ll_item_params.height = card_height;
		//
		// LayoutParams ll_delete = holder.ll_delete.getLayoutParams();
		// ll_delete.height = card_height;
		//
		// //设置删除按钮大小
		// LayoutParams ibt_del_params = holder.ibt_del.getLayoutParams();
		// ibt_del_params.height = card_height / 2;
		// ibt_del_params.width = card_height / 2;

		if (withoutCardTransContext.isRightButtonClick()) {
			holder.ibt_del.setVisibility(View.VISIBLE);
			holder.ibt_setting.setVisibility(View.INVISIBLE);
			holder.ibt_del.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					getDelItemClickListener().onItemClick(null, null, position, position);
				}
			});
//			holder.ibt_setting.setEnabled(false);
		} else{
			holder.ibt_del.setVisibility(View.GONE);
//			holder.ibt_setting.setEnabled(true);
			holder.ibt_setting.setVisibility(View.VISIBLE);
		}
		
		holder.ibt_setting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getSettingClickListener().onItemClick(null, null, position, position);
			}
		});

		return convertView;
	}

	class ViewHolder {
//		RelativeLayout rl_item;
		TextView tv_cardType;
		TextView tv_nickName;
		TextView tv_account;
		// TextView tv_cust_max_quota;
		ImageButton ibt_setting;
		ImageButton ibt_del;
	}

	public void notifyListViewData() {
		notifyDataSetChanged();
	}

	public void setDelItemClickListener(OnItemClickListener delItemClickListener) {
		this.delItemClickListener = delItemClickListener;
	}

	public OnItemClickListener getDelItemClickListener() {
		return delItemClickListener;
	}

	public void setSettingClickListener(OnItemClickListener settingClickListener) {
		this.settingClickListener = settingClickListener;
	}

	public OnItemClickListener getSettingClickListener() {
		return settingClickListener;
	}
}
