package com.chinamworld.bocmbci.biz.gatherinitiative.commenpayer;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.GatherInitiative;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;

/**
 * @ClassName: CommenPayerListAdapter
 * @Description: 常用付款联系人列表的adapter
 * @author JiangWei
 * @date 2013-9-3下午4:08:52
 */
public class CommenPayerListAdapter extends BaseAdapter {

	private Context context = null;

	private List<Map<String, Object>> accountList = null;
	/** 列表的模式 */
	private int theMode = CommenPayerActivity.MODE_OPRATE;
	private OnOperateClickLisener mOnOperateClickLisener;
	private OnDeleteClickLisener mOnDeleteClickLisener;

	public CommenPayerListAdapter(Context context, List<Map<String, Object>> accountList) {
		this.context = context;
		this.accountList = accountList;
	}

	public void setTheMode(int mode) {
		this.theMode = mode;
	}

	public void setOnOperateClickLisener(OnOperateClickLisener onOperateClickLisener) {
		mOnOperateClickLisener = onOperateClickLisener;
	}

	public void setOnDeleteClickLisener(OnDeleteClickLisener onDeleteClickLisener) {
		mOnDeleteClickLisener = onDeleteClickLisener;
	}

	public void setListData(List<Map<String, Object>> list) {
		accountList = list;
		notifyDataSetChanged();
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
			convertView = LayoutInflater.from(context).inflate(R.layout.gather_commen_payer_list_item, null);
			viewHolder.payerName = (TextView) convertView.findViewById(R.id.payer_name);
			viewHolder.payerPhone = (TextView) convertView.findViewById(R.id.payer_phone);
			viewHolder.payerType = (TextView) convertView.findViewById(R.id.payer_type);
			viewHolder.payerCustomerId = (TextView) convertView.findViewById(R.id.payer_customer_id);
			viewHolder.imageOperate = (Button) convertView.findViewById(R.id.image_operate);
			viewHolder.imageDelete = (ImageView) convertView.findViewById(R.id.image_delete);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		String strPayerName = (String) accountList.get(position).get(GatherInitiative.PAYER_NAME);
		String strPayerPhone = (String) accountList.get(position).get(GatherInitiative.PAYER_MOBILE);
		String strPayerType = (String) accountList.get(position).get(GatherInitiative.IDENTIFY_TYPE);
		String strPayerCustomerId = (String) accountList.get(position).get(GatherInitiative.PAYER_CUSTOMER_ID);

		viewHolder.payerName.setText(strPayerName);
		viewHolder.payerPhone.setText(strPayerPhone);
		viewHolder.payerType.setText(LocalData.payerType.get(strPayerType));
		if ("2".equals(strPayerType)) {
			viewHolder.payerCustomerId.setVisibility(View.GONE);
			viewHolder.payerCustomerId.setText("");
		} else {
			viewHolder.payerCustomerId.setVisibility(View.VISIBLE);
			viewHolder.payerCustomerId.setText(strPayerCustomerId);
		}

		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, viewHolder.payerName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, viewHolder.payerPhone);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, viewHolder.payerType);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, viewHolder.payerCustomerId);

		if (theMode == CommenPayerActivity.MODE_OPRATE) {
			viewHolder.imageOperate.setVisibility(View.VISIBLE);
			viewHolder.imageDelete.setVisibility(View.GONE);
		} else if (theMode == CommenPayerActivity.MODE_DELETE) {
			viewHolder.imageOperate.setVisibility(View.GONE);
			viewHolder.imageDelete.setVisibility(View.VISIBLE);
		} else {
			viewHolder.imageOperate.setVisibility(View.GONE);
			viewHolder.imageDelete.setVisibility(View.GONE);
		}
		viewHolder.imageOperate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mOnOperateClickLisener.onOperateClick(v, position);
			}
		});
		viewHolder.imageDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mOnDeleteClickLisener.onDeleteClick(v, position);
			}
		});
		return convertView;
	}

	public interface OnOperateClickLisener {
		public void onOperateClick(View v, int position);
	}

	public interface OnDeleteClickLisener {
		public void onDeleteClick(View v, int position);
	}

	private class ViewHolder {
		public TextView payerName;
		public TextView payerPhone;
		public TextView payerType;
		public TextView payerCustomerId;
		public Button imageOperate;
		public ImageView imageDelete;
	}

}
