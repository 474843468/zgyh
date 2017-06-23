package com.chinamworld.bocmbci.biz.dept.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @description 查询已购买大额存单Adapter
 * @author liuh
 *
 */
public class LargeCDPurchasedListAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> list = null;

	public LargeCDPurchasedListAdapter(Context context,
			List<Map<String, Object>> list) {
		this.mContext = context;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.large_cd_purchased_list_item, null);
			holder = new ViewHolder();
			holder.cdNumberTv = (TextView) convertView
					.findViewById(R.id.tv_manage_predate_list_item);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext,
					holder.cdNumberTv);
			holder.rateTv = (TextView) convertView
					.findViewById(R.id.tv_transeq_manage_predate_list_item);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext,
					holder.rateTv);
			holder.cdBalanceTv = (TextView) convertView
					.findViewById(R.id.tv_prepayee_manage_predate_list_item);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext,
					holder.cdBalanceTv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (holder != null) {

			Object obj = getItem(position);
			if (obj == null)
				return null;
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) obj;
			if (map != null || !map.isEmpty()) {
				String cdNumber = (String) map.get(Dept.CD_NUMBER);
				String rate = (String) map.get(Dept.RATE);
				String cdBalance = (String) map.get(Dept.CD_BALANCE);

				holder.cdNumberTv.setText(cdNumber);
				holder.rateTv.setText(rate+"%"); // add by luqp 2016年3月4日 追加%号
				holder.cdBalanceTv.setText("-".equals(cdBalance) ? cdBalance : StringUtil.parseStringPattern(
						cdBalance, 2));
			}

		}
		return convertView;
	}

	private class ViewHolder {
		//存单编号
		private TextView cdNumberTv;
		//利率
		private TextView rateTv;
		//存单面额
		private TextView cdBalanceTv;

	}

}
