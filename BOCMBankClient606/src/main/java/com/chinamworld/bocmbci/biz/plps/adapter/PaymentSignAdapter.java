package com.chinamworld.bocmbci.biz.plps.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 签约信息ListView适配器
 * @author panwe
 *
 */
public class PaymentSignAdapter extends BaseAdapter{
	private Context mContext;
	private List<Map<String, Object>> mList;
	
	public PaymentSignAdapter(Context c,List<Map<String, Object>> list){
		this.mContext = c;
		this.mList = list;
	}

	@Override
	public int getCount() {
		if (StringUtil.isNullOrEmpty(mList)) {
			return 0;
		}
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		if (StringUtil.isNullOrEmpty(mList)) {
			return null;
		}
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHodler h;
		if (convertView == null) {
			h = new ViewHodler();
			convertView = LinearLayout.inflate(mContext, R.layout.plps_payment_sign_list_item, null);
			h.agentName = (TextView) convertView.findViewById(R.id.agentname);
			h.custName = (TextView) convertView.findViewById(R.id.custname);
			h.signStatue = (TextView) convertView.findViewById(R.id.signstatue);
			convertView.setTag(h);
		}else{
			h = (ViewHodler) convertView.getTag();
		}
		final Map<String, Object> map = mList.get(position);
		h.agentName.setText((String)map.get(Plps.AGENTNAME));
		h.custName.setText((String)map.get(Plps.PAYUSERNO));
		h.signStatue.setText(PlpsDataCenter.signType.get(map.get(Plps.SIGNTYPE)));
		PlpsUtils.setOnShowAllTextListener(mContext, h.agentName,h.custName);
		return convertView;
	}
	
	public void setData(List<Map<String, Object>> list) {
		this.mList = list;
		notifyDataSetChanged();
	}
	
	public class ViewHodler {
		public TextView agentName;
		public TextView custName;
		public TextView signStatue;
	}
}
