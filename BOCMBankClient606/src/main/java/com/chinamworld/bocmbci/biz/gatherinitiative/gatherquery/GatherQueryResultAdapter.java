package com.chinamworld.bocmbci.biz.gatherinitiative.gatherquery;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.GatherInitiative;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;

/**
 * @ClassName: GatherQueryResultAdapter
 * @Description: 汇款指令查询结果列表adapter
 * @author JiangWei
 * @date 2013-8-20下午4:33:58
 */
public class GatherQueryResultAdapter extends BaseAdapter {

	private Context context = null;

	private List<Map<String, Object>> accountList = null;

	private OnItemClickListener imageItemClickListener = null;
	
	private boolean mIsPayQuery = false;

	public GatherQueryResultAdapter(Context context, List<Map<String, Object>> accountList) {
		this.context = context;
		this.accountList = accountList;
	}

	public void setListData(List<Map<String, Object>> list) {
		accountList = list;
	}
	
	public void setIsPayQuery(boolean isPayQuery){
		mIsPayQuery = isPayQuery;
	}

	@Override
	public int getCount() {
		return accountList.size();
	}

	@Override
	public Object getItem(int position) {
		return accountList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.dept_notmg_list_item, null);
			viewHolder.creatDateTv = (TextView) convertView.findViewById(R.id.dept_cd_number_tv);
			viewHolder.payerTv = (TextView) convertView.findViewById(R.id.dept_type_tv);
			viewHolder.tranStatusTv = (TextView) convertView.findViewById(R.id.dept_avaliable_balance_tv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 起始日期
		String tranDate = (String) accountList.get(position).get(GatherInitiative.CREATE_DATE);
		tranDate = tranDate.substring(0, 10);
		viewHolder.creatDateTv.setText(tranDate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, viewHolder.creatDateTv);
		// 付款人姓名
		String payerName;
		if(mIsPayQuery){
			payerName = (String) accountList.get(position).get(GatherInitiative.PAYEE_NAME);
		}else{
			payerName = (String) accountList.get(position).get(GatherInitiative.PAYER_NAME);
		}
		viewHolder.payerTv.setText(payerName);
		// 交易状态
		String status = (String) accountList.get(position).get(GatherInitiative.STATUS);
		viewHolder.tranStatusTv.setText(LocalData.gatherTranStatus.get(status));

		return convertView;
	}

	public OnItemClickListener getImageItemClickListener() {
		return imageItemClickListener;
	}

	public void setImageItemClickListener(OnItemClickListener imageItemClickListener) {
		this.imageItemClickListener = imageItemClickListener;
	}

	private class ViewHolder {
		public TextView creatDateTv;
		public TextView payerTv;
		public TextView tranStatusTv;
	}

}
