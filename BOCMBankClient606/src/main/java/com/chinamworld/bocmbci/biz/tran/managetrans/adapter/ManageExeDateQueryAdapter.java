package com.chinamworld.bocmbci.biz.tran.managetrans.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 转账管理adapter
 * 
 * @author wangchao
 * 
 */
public class ManageExeDateQueryAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> queryResultList = null ;
	public ManageExeDateQueryAdapter(Context context, List<Map<String, Object>> queryResultList) {
		this.mContext = context;
		this.queryResultList = queryResultList;
	}

	@Override
	public int getCount() {
		int count = 0;
		if(!StringUtil.isNullOrEmpty(queryResultList)) count =queryResultList.size();
		return count;
	}

	@Override
	public Object getItem(int position) {
		if(StringUtil.isNullOrEmpty(queryResultList)) return null;
		return queryResultList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext,R.layout.tran_manage_list_3iterms, null);
			holder = new ViewHolder();
			
			holder.dateTV = (TextView) convertView.findViewById(R.id.tv_one_item);
			holder.batSeqTV= (TextView) convertView.findViewById(R.id.tv_two_item);
			holder.payeeAccountNameTV = (TextView) convertView.findViewById(R.id.tv_three_item);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		if(holder != null) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>)getItem(position);
			if (map != null) {
				holder.dateTV.setText((String)map.get(Tran.MANAGE_PRE_PAYMENTDATE_RES));
				holder.batSeqTV.setText((String)map.get(Tran.MANAGE_PRE_BATSEQ_RES));
				holder.payeeAccountNameTV.setText((String)map.get(Tran.MANAGE_PRE_PAYEEACCOUNTNAME_RES));
				PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext, holder.dateTV);
				PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext, holder.batSeqTV);
				PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext, holder.payeeAccountNameTV);
			}
		}
		
		return convertView;
	}
	
	private class ViewHolder {
		
		public TextView dateTV;
		public TextView batSeqTV;
		public TextView payeeAccountNameTV;
	}
	
	public void setDate(List<Map<String, Object>> queryResultList) {
		this.queryResultList = queryResultList;
		notifyDataSetChanged();
	}
}
