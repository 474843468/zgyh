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
import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 申请结果ListView适配器
 * @author panwe
 *
 */
public class AnnuityApplResultAdapter extends BaseAdapter{
	private Context mContext;
	private List<Map<String, Object>> mList;
	
	public AnnuityApplResultAdapter(Context c,List<Map<String, Object>> list){
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
			convertView = LinearLayout.inflate(mContext, R.layout.plps_annuity_apply_query_item, null);
			h.serviceType = (TextView) convertView.findViewById(R.id.servicetype);
			h.seqNo = (TextView) convertView.findViewById(R.id.seqno);
			h.applyDate = (TextView) convertView.findViewById(R.id.applydate);
			h.handleDate = (TextView) convertView.findViewById(R.id.handledate);
			h.handleResult = (TextView) convertView.findViewById(R.id.handleresult);
			h.explain = (TextView) convertView.findViewById(R.id.explain);
			convertView.setTag(h);
		}else{
			h = (ViewHodler) convertView.getTag();
		}
		final Map<String, Object> map = mList.get(position);
		h.serviceType.setText((String)map.get(Plps.BUSSINESSTYPE));
		h.seqNo.setText((String)map.get(Plps.SEQNO));
		h.applyDate.setText((String)map.get(Plps.APPLYDATE));
		h.handleDate.setText((String)map.get(Plps.DISPOSEDATE));
		h.handleResult.setText((String)map.get(Plps.RESULT));
		h.explain.setText((String)map.get(Plps.EXPLAIN));
		PlpsUtils.setOnShowAllTextListener(mContext, h.serviceType,h.seqNo,h.applyDate,h.handleDate,h.handleResult,h.explain);
		return convertView;
	}
	
	public void setData(List<Map<String, Object>> list) {
		this.mList = list;
		notifyDataSetChanged();
	}
	
	public class ViewHodler {
		private TextView serviceType;
		private TextView seqNo;
		private TextView applyDate;
		private TextView handleDate;
		private TextView handleResult;
		private TextView explain;
	}
}
