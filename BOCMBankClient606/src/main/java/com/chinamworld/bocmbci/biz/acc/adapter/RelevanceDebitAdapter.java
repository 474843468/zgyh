package com.chinamworld.bocmbci.biz.acc.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 未关联借记卡账户列表适配器
 * @author wangmengmeng
 *
 */
public class RelevanceDebitAdapter extends BaseAdapter {
	/** 借记卡列表信息 */
	private List<Map<String, String>> debitList;
	private Context context;
	

	public RelevanceDebitAdapter(Context context,
			List<Map<String, String>> debitList) {
		this.context = context;
		this.debitList = debitList;
	}

	@Override
	public int getCount() {

		return debitList.size();
	}

	@Override
	public Object getItem(int position) {

		return debitList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.acc_relevanceaccount_debit_confir_list_item, null);
			holder = new ViewHolder();
			holder.acc_type = (TextView) convertView.findViewById(R.id.tv_relevance_type_value);
			holder.acc_actnum = (TextView) convertView.findViewById(R.id.tv_relevance_actnum_value);
			convertView.setTag(holder);
		} else { 
			holder = (ViewHolder)convertView.getTag(); 
		}
		
        if(holder != null) {
        	String acc_type=debitList.get(position).get(
					Acc.RELEVANCEACCPRE_ACC_DEBITACCOUNTTYPE_RES);
        	holder.acc_type.setText(AccBaseActivity.bankAccountType.get(acc_type.trim()));
        	
        	String actnumber=debitList.get(position).get(
					Acc.RELEVANCEACCPRE_ACC_DEBITACCOUNTNUMBER_RES);
        	holder.acc_actnum.setText(StringUtil.getForSixForString(actnumber));
        }
			
		
		return convertView;
	}
	
	private class ViewHolder {
		
		/** 待关联账户类型 */
		public TextView acc_type = null;
		/** 待关联账号 */
	    public TextView acc_actnum = null;
		
	}
}
