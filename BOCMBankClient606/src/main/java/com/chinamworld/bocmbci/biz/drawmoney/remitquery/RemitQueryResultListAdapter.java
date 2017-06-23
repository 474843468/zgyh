package com.chinamworld.bocmbci.biz.drawmoney.remitquery;

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
import com.chinamworld.bocmbci.bii.constant.DrawMoney;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;

/**
 * @ClassName: RemitQueryResultListAdapter
 * @Description: 汇款查询结果列表的adapter
 * @author JiangWei
 * @date 2013-7-23 上午10:51:48
 */
public class RemitQueryResultListAdapter extends BaseAdapter {

	private Context context = null;

	private List<Map<String, Object>> accountList = null;

	private OnItemClickListener imageItemClickListener = null;
	/** 是否是取款查询 */
	private boolean isDraw = false;
	
	public RemitQueryResultListAdapter(Context context,
			List<Map<String, Object>> accountList) {
		this.context = context;
		this.accountList = accountList;
	}
	
	public void setIsDraw(boolean isDrawal){
		isDraw = isDrawal;
	}
	
	public void setListData(List<Map<String, Object>> list){
		accountList = list;
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
			convertView = LayoutInflater.from(context).inflate(
					R.layout.dept_notmg_list_item, null);
			viewHolder.tranDateTv = (TextView) convertView
					.findViewById(R.id.dept_cd_number_tv);
			viewHolder.getRemitNameTv = (TextView) convertView
					.findViewById(R.id.dept_type_tv);
			viewHolder.remitStatusTv = (TextView) convertView
					.findViewById(R.id.dept_avaliable_balance_tv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		String tranDate = (String) accountList.get(position)
				.get(DrawMoney.TRAN_DATE);
		String strDate = tranDate.substring(0, 10);
		viewHolder.tranDateTv.setText(strDate);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
//				viewHolder.tranDateTv);

		String getRemitName = (String) accountList.get(position)
				.get(DrawMoney.PAYEE_NAME);
		

//		String remitStatus = (String) accountList.get(position).get(
//				DrawMoney.REMIT_STATUS);
		
		if(isDraw){
			viewHolder.remitStatusTv.setText((String) accountList.get(position)
					.get(DrawMoney.REMIT_NO));
			viewHolder.getRemitNameTv.setText(getRemitName);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(context, viewHolder.getRemitNameTv);
		}else{
//			viewHolder.remitStatusTv.setText(LocalData.remitStatusThree
//					.get(remitStatus));
			viewHolder.remitStatusTv.setText(getRemitName);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(context, viewHolder.remitStatusTv);
		}
		
		return convertView;
	}

	public OnItemClickListener getImageItemClickListener() {
		return imageItemClickListener;
	}

	public void setImageItemClickListener(
			OnItemClickListener imageItemClickListener) {
		this.imageItemClickListener = imageItemClickListener;
	}

	private class ViewHolder {
		public TextView tranDateTv;
		public TextView getRemitNameTv;
		public TextView remitStatusTv;
	}

}
