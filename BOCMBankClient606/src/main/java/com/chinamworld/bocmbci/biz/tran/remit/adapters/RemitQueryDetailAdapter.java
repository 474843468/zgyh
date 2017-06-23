package com.chinamworld.bocmbci.biz.tran.remit.adapters;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.AdapterForLinearLayout;

public class RemitQueryDetailAdapter extends AdapterForLinearLayout {
	private List<Map<String, String>> list = null;
	private Context context = null;

	public RemitQueryDetailAdapter(Context context, List<Map<String, String>> list) {
		super(context, list);
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		convertView = LayoutInflater.from(context).inflate(R.layout.tran_remit_search_detail_item, null);
		// holder.nameText = (TextView)
		// convertView.findViewById(R.id.remit_shareAccName);
		holder.crcdNoText = (TextView) convertView.findViewById(R.id.remit_displayCardNo);
		holder.statusText = (TextView) convertView.findViewById(R.id.remit_shareStatus);
		convertView.setTag(holder);
		Map<String, String> map = list.get(position);
		String shareAccName = map.get(Tran.TRAN_SHAREACCNAME_RES);
		String shareCardNo = map.get(Tran.TRAN_SHARECARDNO_RES);
		String shareStatus = map.get(Tran.TRAN_SHARESTATUS_RES);
		// if (StringUtil.isNull(shareAccName)) {
		// holder.nameText.setText("-");
		// } else {
		// holder.nameText.setText(shareAccName);
		// }
		if (StringUtil.isNull(shareCardNo)) {
			holder.crcdNoText.setText("-");
		} else {
			String shareCardNos = StringUtil.getForSixForString(shareCardNo);
			holder.crcdNoText.setText(shareCardNos);
		}

		if (StringUtil.isNull(shareStatus)) {
//			holder.statusText.setText("-");
			holder.statusText.setText(context.getResources().getString(R.string.tran_remit_n_query_Y));
		} else {
			if (ConstantGloble.COMBINE_FLAG_Y.equals(shareStatus)) {
				// Y-有效
				holder.statusText.setText(context.getResources().getString(R.string.tran_remit_n_query_Y));
			} else {
				// N-无效
				holder.statusText.setText(context.getResources().getString(R.string.tran_remit_n_query_N));
			}
		}
		// PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
		// holder.nameText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, holder.crcdNoText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, holder.statusText);
		return convertView;
	}

	private class ViewHolder {
		// private TextView nameText = null;//接口不返回数据
		private TextView crcdNoText = null;
		private TextView statusText = null;
	}
}
