package com.chinamworld.bocmbci.biz.tran.mytransfer.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Tran;

/**
 * 转账管理adapter
 * 
 * @author wangchao
 * 
 */
public class QueryExternalBankAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, String>> queryResultList = null ;
	public QueryExternalBankAdapter(Context context, List<Map<String, String>> queryResultList) {
		this.mContext = context;
		this.queryResultList = queryResultList;
	}

	@Override
	public int getCount() {
		return queryResultList.size();
	}

	@Override
	public Object getItem(int position) {
		return queryResultList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.tran_payee_other_bank_query_list_item, null);
		} 
		TextView kBankTv = (TextView) convertView.findViewById(R.id.tv_kbank_other_bank_list_item);

		Map<String, String> map = queryResultList.get(position);
		if (map != null) {
			kBankTv.setText((String)map.get(Tran.PAYEE_BANKNAME_REQ));
		}
		
		return convertView;
	}
	
	public void setData(List<Map<String, String>> queryResultList){
		this.queryResultList = queryResultList;
		notifyDataSetChanged();
	}

}
