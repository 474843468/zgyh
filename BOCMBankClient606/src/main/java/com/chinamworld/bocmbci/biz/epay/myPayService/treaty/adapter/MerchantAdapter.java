package com.chinamworld.bocmbci.biz.epay.myPayService.treaty.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.TreatyConstants;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.PublicTools;

public class MerchantAdapter extends BaseAdapter {

	private BaseActivity context;

	private int selectedPosition = -1;

	private List<Object> merchants;

	public MerchantAdapter(BaseActivity context, List<Object> merchants) {
		this.context = context;
		this.merchants = merchants;
	}

	@Override
	public int getCount() {
		return merchants.size();
	}

	@Override
	public Object getItem(int position) {
		return merchants.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.epay_treaty_merchant_item_2, null);
			holder.ll_merchant_card = (LinearLayout) convertView.findViewById(R.id.ll_merchant_card);
			holder.tv_acc_types = (TextView) convertView.findViewById(R.id.tv_acc_types);
			holder.tv_merchant_name = (TextView) convertView.findViewById(R.id.tv_merchant_name);
			holder.img_gou = (ImageView) convertView.findViewById(R.id.img_gou);
			// holder.rl_selected_img.setVisibility(View.GONE);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map<Object, Object> tempMap = EpayUtil.getMap(merchants.get(position));
		// 商户名称
		String merchantName = EpayUtil.getString(tempMap.get(TreatyConstants.METHOD_QUERY_MERCHANTS_FIELD_MERCHANT_NAME), "");
		holder.tv_merchant_name.setText(merchantName);
		// 是否支持借记卡
		boolean isDebitCard = PublicTools.getInt(tempMap.get(TreatyConstants.METHOD_QUERY_MERCHANTS_FIELD_IS_DEBIT_CARD), 0) == 1 ? true : false;
		// 是否支持长城信用卡
		boolean isQccCard = PublicTools.getInt(tempMap.get(TreatyConstants.METHOD_QUERY_MERCHANTS_FIELD_IS_QCCCARD), 0) == 1 ? true : false;
		// 是否支持信用卡
		boolean isCreditCard = PublicTools.getInt(tempMap.get(TreatyConstants.METHOD_QUERY_MERCHANTS_FIELD_IS_CREDIT_CARD), 0) == 1 ? true : false;
		StringBuffer sb = new StringBuffer();

		if (isDebitCard) {
			sb.append("借记卡").append("、");
		}

		if (isQccCard) {
			sb.append("长城系列信用卡").append("、");
		}

		if (isCreditCard) {
			sb.append("中银系列信用卡").append("、");
		}

		// 所支持的账户类型
		holder.tv_acc_types.setText(sb.subSequence(0, sb.length() - 1));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.tv_acc_types);

		if (selectedPosition == position) {
			holder.ll_merchant_card.setBackgroundResource(R.drawable.bg_for_listview_item_half_black);
			holder.img_gou.setVisibility(View.VISIBLE);
		} else {
			holder.ll_merchant_card.setBackgroundResource(R.drawable.img_card);
			holder.img_gou.setVisibility(View.GONE);
		}
		return convertView;
	}

	class ViewHolder {
		LinearLayout ll_merchant_card;
		TextView tv_merchant_name;
		TextView tv_acc_types;
		ImageView img_gou;
	}

	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
		refreshList();
	}

	public void setMerchants(ArrayList<Object> merchants) {
		this.merchants = merchants;
	}

	public List<Object> getMerchants() {
		return merchants;
	}

	public void refreshList() {
		this.notifyDataSetChanged();
	}
}
