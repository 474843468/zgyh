package com.chinamworld.bocmbci.biz.loan.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.utils.StringUtil;

public class LoanApplyadapterselectplace extends BaseAdapter {
	private List<Map<String, Object>> list = null;
	private Context context = null;
	public static int selection = -1;
	private int selectPosition;


	public LoanApplyadapterselectplace(Context context,
			List<Map<String, Object>> listMap) {
		// TODO Auto-generated constructor stub
		this.list = listMap;
		this.context = context;
	}

	public void setListData(List<Map<String, Object>> list) {
		this.list = list;
	}


	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.loan_apply_adapter_select_place_item, null);
			holder.adapter_deptName = (TextView) convertView
					.findViewById(R.id.adapter_deptName);
			holder.adapter_deptAddr = (TextView) convertView
					.findViewById(R.id.adapter_deptAddr);
//			holder.adapter_deptPhone = (TextView) convertView
//					.findViewById(R.id.adapter_deptPhone);
			holder.imageView = (ImageView) convertView.findViewById(R.id.iv_bt);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String, Object> map = list.get(position);
		String deptName = (String) map.get(Loan.LOAN_APPLY_DEPTNAME_QRY);
		// String deptAddr=LocalData.AccountType.get(accountType);
		String deptAddr = (String) map.get(Loan.LOAN_APPLY_DEPTADDR_QRY);
		String deptPhone = (String) map.get(Loan.LOAN_APPLY_DEPTPHONE_QRY);
		// holder.imageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.img_dian_red));

		if (StringUtil.isNullOrEmptyCaseNullString(deptName)) {
			holder.adapter_deptName.setVisibility(View.GONE);
		}
		if (StringUtil.isNullOrEmptyCaseNullString(deptAddr)) {
			holder.adapter_deptAddr.setVisibility(View.GONE);
		}
//		if (StringUtil.isNullOrEmptyCaseNullString(deptPhone)) {
//			holder.adapter_deptPhone.setVisibility(View.GONE);
//		}
		holder.adapter_deptName.setText(deptName);
		holder.adapter_deptAddr.setText(deptAddr);
//		holder.adapter_deptPhone.setText(deptPhone);
		
		holder.imageView.setSelected(position == this.selectPosition);
		return convertView;
	}

	private class ViewHolder {
		private TextView adapter_deptName = null;
		private TextView adapter_deptAddr = null;
//		private TextView adapter_deptPhone = null;
		private ImageView imageView = null;
	}

	public int getSelectPosition() {
		return selectPosition;
	}

	public void setSelectPosition(int selectPosition) {
		this.selectPosition = selectPosition;
	}

}
 