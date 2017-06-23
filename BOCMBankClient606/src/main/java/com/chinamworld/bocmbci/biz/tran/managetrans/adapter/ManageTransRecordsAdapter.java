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

/**
 * 转账管理adapter
 * 
 * @author wangchao
 * 
 */
public class ManageTransRecordsAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> queryResultList = null;

	public ManageTransRecordsAdapter(Context context,
			List<Map<String, Object>> queryResultList) {
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
		View view = null;
		if (convertView != null) {
			view = convertView;
		} else {
			view = View.inflate(mContext,
					R.layout.tran_manage_predate_list_item, null);

			TextView preDateTv = (TextView) view
					.findViewById(R.id.tv_manage_predate_list_item);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext,
					preDateTv);
			TextView tranSeqTv = (TextView) view
					.findViewById(R.id.tv_transeq_manage_predate_list_item);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext,
					tranSeqTv);
			TextView prePayeeTv = (TextView) view
					.findViewById(R.id.tv_prepayee_manage_predate_list_item);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext,
					prePayeeTv);
			Map<String, Object> map = queryResultList.get(position);
			if (map != null) {
				preDateTv.setText((String) map
						.get(Tran.MANAGE_PRE_FIRSTSUBMITDATE_RES));
				tranSeqTv.setText((String) map.get(Tran.MANAGE_PRE_BATSEQ_RES));
				prePayeeTv.setText((String) map
						.get(Tran.MANAGE_PRE_PAYEEACCOUNTNAME_RES));
			}

		}
		return view;
	}

}
