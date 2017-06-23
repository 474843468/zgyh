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
 * @author 
 * 
 */
public class ManageRecordsAdapter1 extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> queryResultList = null ;
	public ManageRecordsAdapter1(Context context, List<Map<String, Object>> queryResultList) {
		this.mContext = context;
		this.queryResultList = queryResultList;
		
		//LogGloble.i(ManageDateAdapter.class.getSimpleName(), "ManageDateAdapter...");
	}
	
	public void setDate(List<Map<String, Object>> queryResultList) {
		this.queryResultList = queryResultList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		//LogGloble.i(ManageDateAdapter.class.getSimpleName(), "getCount()...");
		int count = 0;
		if(!StringUtil.isNullOrEmpty(queryResultList))count = queryResultList.size() ;
		return count;
	}

	@Override
	public Object getItem(int position) {
		//LogGloble.i(ManageDateAdapter.class.getSimpleName(), "getItem()...");
		
		if(StringUtil.isNullOrEmpty(queryResultList)) {
			return null;
		}
		return queryResultList.get(position);
		/*if(position == 0) {
			return "headTitle";
		} else {
			return queryResultList.get(position-1);
		}*/
		//return null;//queryResultList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//LogGloble.i(ManageDateAdapter.class.getSimpleName(), "getView()...");
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext,R.layout.tran_manage_predate_list_item, null);
			holder = new ViewHolder();
			//holder.content = (LinearLayout)convertView.findViewById(R.id.ll_pre_date_listview_content);
			//holder.header = (LinearLayout)convertView.findViewById(R.id.pre_date_listview_header);	
			holder.dateView = (TextView) convertView.findViewById(R.id.tv_manage_predate_list_item);
			holder.batSeqView = (TextView) convertView.findViewById(R.id.tv_transeq_manage_predate_list_item);
			holder.payeeAccountNameView = (TextView) convertView.findViewById(R.id.tv_prepayee_manage_predate_list_item);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		if(holder != null) {

			//LogGloble.i(ManageDateAdapter.class.getSimpleName(), "position="+position);
			Object obj = getItem(position);
			if(obj == null)return null;
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>)obj;
			if (map != null) {
				holder.dateView.setText((String)map.get(Tran.MANAGE_PRE_PAYMENTDATE_RES));////DateFormat.format(inFormat, inDate)
				PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext, holder.dateView);
//				holder.batSeqView.setText((String)map.get(Tran.MANAGE_PRE_BATSEQ_RES));
				holder.batSeqView.setText((String)map.get(Tran.MANAGE_PRE_TRANSACTIONID_RES));//投产演练问题 暂不提交
				PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext, holder.batSeqView);
				holder.payeeAccountNameView.setText((String)map.get(Tran.MANAGE_PRE_PAYEEACCOUNTNAME_RES));
				PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext, holder.payeeAccountNameView);
			}
			
		}
		return convertView;
	}
	
	private class ViewHolder {
		
		public TextView dateView;
		public TextView batSeqView;
		public TextView payeeAccountNameView;
		//public LinearLayout header;
		//public LinearLayout content;
		
	}

}
