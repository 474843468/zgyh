package com.chinamworld.bocmbci.biz.finc.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 短期理财  适配器
 * 
 * @author xyl
 * 
 */
public class FincFundDueDateListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> list;

	public FincFundDueDateListAdapter(Context context, List<Map<String, Object>> list) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.list = list;
	}

	public void notifyDataSetChanged(List<Map<String, Object>> list) {
		this.list = list;
		super.notifyDataSetChanged();
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
					R.layout.finc_due_list_item, null);
			holder = new ViewHolder();
			convertView.setTag(holder);
			holder.mRealityTv = (TextView) convertView
					.findViewById(R.id.finc_listiterm_tv1);
			holder.mDateTv = (TextView) convertView
					.findViewById(R.id.finc_listiterm_tv2);
			holder.mFundStateTv = (TextView) convertView
					.findViewById(R.id.finc_listiterm_tv3);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Map<String, Object> map = list.get(position);
		final String reality = (String) map.get(Finc.FINC_MATURE_TOTAL);
		final String registerDate = (String) map.get(Finc.FINC_QUTY_REGIST);
		final String redemptionDate = (String) map.get(Finc.FINC_ALLOW_REDEEM);
		holder.mRealityTv.setText(StringUtil.parseStringPattern(reality, 2));
		holder.mDateTv.setText(registerDate);
		holder.mFundStateTv.setText(redemptionDate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.mRealityTv);
		 PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
		 holder.mDateTv);
		 PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
		 holder.mFundStateTv);

		return convertView;
	}

	/**
	 * 存放控件
	 * 
	 * @author Administrator
	 * 
	 */
	public final class ViewHolder {
		/** 实际份额,份额注册日期,可赎回日期*/
		public TextView mRealityTv;
		public TextView mDateTv;
		public TextView mFundStateTv;
	}

}
