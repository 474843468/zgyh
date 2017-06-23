package com.chinamworld.bocmbci.biz.dept.adapter;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.biz.dept.largecd.LargeCDBuyActivity;
import com.chinamworld.bocmbci.biz.dept.largecd.LargeCDDetailActivity;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @description 查询可购买大额存单Adapter
 * @author liuh
 * 
 */
public class LargeCDAvailableListAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> list;

	public LargeCDAvailableListAdapter(Context mContext, List<Map<String, Object>> list) {
		this.mContext = mContext;
		this.list = list;
	}

	public void setListData(List<Map<String, Object>> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.large_cd_available_list_item, null);
			holder = new ViewHolder();
			holder.productNumTV = (TextView) convertView.findViewById(R.id.tv_product_num_item);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext, holder.productNumTV);
			holder.rateTV = (TextView) convertView.findViewById(R.id.tv_rate_item);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext, holder.rateTV);
			holder.beginMoneyTV = (TextView) convertView.findViewById(R.id.tv_begin_money_item);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext, holder.beginMoneyTV);
			holder.buyBtn = (TextView) convertView.findViewById(R.id.tv_operate_item);
			holder.detailBtn = (ImageView) convertView.findViewById(R.id.rate_gotoDetail);
			/////////////////////////////////////////////////////////////////
			// add 2016年3月2日  隐藏购买按钮
			holder.buyBtn.setVisibility(View.GONE);
			////////////////////////////////////////////////////////////////
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Map<String, Object> map = (Map<String, Object>) list.get(position);
		String productCode = StringUtil.valueOf1((String) map.get(Dept.PRODUCT_CODE));
		String rate = StringUtil.valueOf1((String) map.get(Dept.RATE));
		String beginMoney = StringUtil.valueOf1((String) map.get(Dept.BEGIN_MONEY));

		holder.productNumTV.setText(productCode);
		holder.rateTV.setText(rate+"%"); // add by luqp 2016年3月4日 追加%号
		holder.beginMoneyTV.setText("-".equals(beginMoney) ? beginMoney : StringUtil.parseStringPattern(beginMoney, 2));
		holder.buyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				DeptDataCenter.getInstance().setAvailableDetial(map);
				Intent intent = new Intent();
				intent.setClass(mContext, LargeCDBuyActivity.class);
				mContext.startActivity(intent);
			}
		});
		holder.detailBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				DeptDataCenter.getInstance().setAvailableDetial(map);
				Intent intent = new Intent();
				intent.setClass(mContext, LargeCDDetailActivity.class);
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}

	class ViewHolder {
		// 产品编码
		private TextView productNumTV;
		// 利率
		private TextView rateTV;
		// 起存金额
		private TextView beginMoneyTV;
		// 购买
		private TextView buyBtn;
		// 明细
		private ImageView detailBtn;
	}

}
