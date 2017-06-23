package com.chinamworld.bocmbci.biz.epay.myPayService.treaty.adapter;

import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.TreatyConstants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.utils.StringUtil;

public class RelationMerchantAdapter extends BaseAdapter {

	private String tag = "RelationMerchantAdapter";

	private BaseActivity context;

	private int selectedPosition;

	private OnItemClickListener delItemClickListener;
	
	private Context treatyContext;

	public RelationMerchantAdapter(BaseActivity context) {
		this.context = context;
		treatyContext = TransContext.getTreatyTransContext();
	}

	@Override
	public int getCount() {
		return treatyContext.getList(TreatyConstants.PUB_FEILD_TREATY_MERCHANTS).size();
	}

	@Override
	public Object getItem(int position) {
		return treatyContext.getList(TreatyConstants.PUB_FEILD_TREATY_MERCHANTS).get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.epay_treaty_merchant_item, null);
			holder.rl_merchant_card = (RelativeLayout) convertView.findViewById(R.id.rl_merchant_card);
//			holder.rl_arrow = (RelativeLayout) convertView.findViewById(R.id.rl_arrow);
			holder.tv_merchant_name = (TextView) convertView.findViewById(R.id.tv_merchant_name);
			holder.tv_agreement_id = (TextView) convertView.findViewById(R.id.tv_agreement_id);
			holder.tv_pay_account = (TextView) convertView.findViewById(R.id.tv_pay_account);
			holder.tv_relation_status = (TextView) convertView.findViewById(R.id.tv_relation_status);
			holder.tv_merchant_id = (TextView) convertView.findViewById(R.id.tv_merchant_id);
			holder.tv_cust_max_quota = (TextView) convertView.findViewById(R.id.tv_cust_max_quota);
			holder.ibt_del = (ImageButton) convertView.findViewById(R.id.bt_del);
//			holder.img_redirect = (ImageView) convertView.findViewById(R.id.img_redirect);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Map<Object, Object> merchant = EpayUtil.getMap(getItem(position));
		String status = EpayUtil.getString(merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_STATUS), "");
		
		holder.tv_merchant_name.setText(EpayUtil.getString(merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_MERCHANT_NAME), ""));
		holder.tv_agreement_id.setText(EpayUtil.getString(merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_AGREEMENT_ID), ""));
		holder.tv_pay_account.setText(StringUtil.getForSixForString(EpayUtil.getString(merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_CARD_NO), "")));
		holder.tv_merchant_id.setText(EpayUtil.getString(merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_HOLDER_MERID), ""));
		holder.tv_cust_max_quota.setText(StringUtil.parseStringPattern(EpayUtil.getString(merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_DAILY_QUOTA), ""), 2));
		holder.tv_relation_status.setText(TreatyConstants.TREATY_STATUS.get(EpayUtil.getString(merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_STATUS), "")));
		
		if (treatyContext.isRightButtonClick()) {
//			holder.img_redirect.setVisibility(View.GONE);
			if("V".equals(status)) {
				holder.ibt_del.setClickable(true);
				holder.ibt_del.setVisibility(View.VISIBLE);
				holder.ibt_del.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						delItemClickListener.onItemClick(null, null, position, position);
					}
				});
			} else {
				holder.ibt_del.setClickable(false);
				holder.ibt_del.setVisibility(View.GONE);
			}
		} else {
			holder.ibt_del.setVisibility(View.GONE);
//			holder.img_redirect.setVisibility(View.VISIBLE);
		}

//		LayoutParams rlMerchantCardParams = holder.rl_merchant_card.getLayoutParams();
//		rlMerchantCardParams.height = LayoutValue.SCREEN_WIDTH * 83 / 320;

//		LayoutParams llDeleteItemParams = holder.ll_delete.getLayoutParams();
//		llDeleteItemParams.height = LayoutValue.SCREEN_WIDTH * 83 / 320 / 2;
//		llDeleteItemParams.width = LayoutValue.SCREEN_WIDTH * 83 / 320 / 2;
		return convertView;
	}

	class ViewHolder {
//		ImageView img_redirect;
		RelativeLayout rl_merchant_card;
//		RelativeLayout rl_arrow;
		TextView tv_merchant_name;
		TextView tv_agreement_id;
		TextView tv_pay_account;
		TextView tv_merchant_id;
		TextView tv_cust_max_quota;
		TextView tv_relation_status;
		ImageButton ibt_del;
	}

	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
		notifyDataSetChanged();
	}

	public void setDelItemClickListener(OnItemClickListener delItemClickListener) {
		this.delItemClickListener = delItemClickListener;
	}

	public OnItemClickListener getDelItemClickListener() {
		return delItemClickListener;
	}
	
}
