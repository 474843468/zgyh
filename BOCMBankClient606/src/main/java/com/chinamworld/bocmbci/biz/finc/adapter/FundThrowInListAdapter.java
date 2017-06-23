package com.chinamworld.bocmbci.biz.finc.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.constant.LayoutValue;

/**
 * 基金行情 适配器
 * 
 * @author xyl
 * 
 */
public class FundThrowInListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> list;
	private int padding;

	public FundThrowInListAdapter(Context context, List<Map<String, Object>> list) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.list = list;
		padding = context.getResources().getDimensionPixelSize(R.dimen.fill_margin_left) * 2;
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.finc_myfinc_balance_throw_list_item, null);
			holder = new ViewHolder();
			convertView.setTag(holder);
			holder.fundCodeTextView = (TextView) convertView
					.findViewById(R.id.finc_codeAndName);
			holder.risklevelTextView = (TextView) convertView
					.findViewById(R.id.finc_risk);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		setBackgroudDrawble(convertView, position, list.size());
		convertView.measure(0, 0);
		int itemHeight = (int)context.getResources().getDimension(R.dimen.btn_bottom_height);
		int itemWidth = LayoutValue.SCREEN_WIDTH - padding;
		LayoutParams lp = new LayoutParams(itemWidth, itemHeight);
		convertView.setLayoutParams(lp);
		Map<String, Object> map = list.get(position);
		final String fundCode = (String) map.get(Finc.FINC_FUNDCODE);
		final String fundName = (String) map.get(Finc.FINC_FUNDNAME);
		holder.fundCodeTextView.setText(fundCode);
		holder.risklevelTextView.setText(fundName);
		
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
//				holder.fundCodeTextView);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
//				holder.risklevelTextView);
		return convertView;
	}
	
	//设置list项背景
	private void setBackgroudDrawble(View view, final int position, int size){
		if(size > 1){
			if(position == 0)
				view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.cards_top));
			else if(position == size - 1)
				view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.cards_buttom));
			else
				view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.cards_mid));
		}
	}

	/**
	 * 存放控件
	 * 
	 * @author Administrator
	 * 
	 */
	public final class ViewHolder {
		/**
		 * 基金代码
		 */
		public TextView fundCodeTextView;
		/**
		 * 风险等级
		 */
		public TextView risklevelTextView;
	}

}
