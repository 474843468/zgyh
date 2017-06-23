package com.chinamworld.bocmbci.biz.dept.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

public class DeptZntzckQueryAdapter extends BaseAdapter {
    private String SelectAccount;
	private List<Map<String, String>> list = null;
	private Context context = null;
	private OnItemClickListener onItemClickListener;

	public OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public DeptZntzckQueryAdapter(Context context, List<Map<String, String>> list,String SelectAccount) {
		this.context = context;
		this.SelectAccount=SelectAccount;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public void refshDate(List<Map<String, String>> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.dept_zntzck_query_item, null);
			holder = new ViewHolder();
			holder.signAccText = (TextView) convertView.findViewById(R.id.dept_zntzck_query_signAcc);
			holder.signTypeText = (TextView) convertView.findViewById(R.id.dept_zntzck_query_signType);
			holder.signDateText = (TextView) convertView.findViewById(R.id.dept_zntzck_query_signDate);
			holder.signStatusText = (TextView) convertView.findViewById(R.id.dept_zntzck_query_signStatus);
			holder.cancelChnlFlagText = (TextView) convertView.findViewById(R.id.dept_zntzck_query_cancelChnlFlag);
			holder.cancelDateText = (TextView) convertView.findViewById(R.id.dept_zntzck_query_cancelDate);
			holder.cancelButton = (Button) convertView.findViewById(R.id.dept_dqyzc_detail_cancel);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String, String> map = list.get(position);
		String signCardNo = map.get(Dept.DEPT_SIGNCARDNO_RES);
		String signDate = map.get(Dept.DEPT_SIGNDATE_RES);
		String signChnlFlag = map.get(Dept.DEPT_SIGNCHNLFLAG_RES);
		String cancelDate = map.get(Dept.DEPT_CANCELDATE_RES);// 解约日期
		String cancelChnlFlag = map.get(Dept.DEPT_CANCELCHNLFLAG_RES);// 解约标志
		String accountNumber = (String) map.get(Comm.ACCOUNTNUMBER);// 已经处理--464
		String accountType = map.get(Comm.ACCOUNT_TYPE);
		String signCardNos = null;
		if (StringUtil.isNull(signCardNo)) {
			signCardNos = StringUtil.getForSixForString(SelectAccount);
		} else {
			signCardNos = StringUtil.getForSixForString(signCardNo);
		}
		String status = null;

		String signChnlFlags = null;
		if (StringUtil.isNull(signChnlFlag)) {
			signChnlFlags = "-";
		} else {
			if (LocalData.signChnlFlagMap.containsKey(signChnlFlag)) {
				signChnlFlags = LocalData.signChnlFlagMap.get(signChnlFlag);
			}
		}
		String cancelChnlFlags = null;
		if (StringUtil.isNull(cancelChnlFlag)) {
			cancelChnlFlags = "-";
		} else {
			if (LocalData.signChnlFlagMap.containsKey(cancelChnlFlag)) {
				cancelChnlFlags = LocalData.signChnlFlagMap.get(cancelChnlFlag);
			}
		}
		if (position == 0) {
			if (StringUtil.isNull(cancelDate)) {
				// 正常
				status = context.getResources().getString(R.string.dept_zntzck_signStatus1);
				holder.cancelButton.setText(context.getResources().getString(R.string.dept_zntzck_query_cancel));
				holder.cancelButton.setVisibility(View.VISIBLE);// 解约按钮
			} else {
				// 已解约
				status = context.getResources().getString(R.string.dept_zntzck_signStatus2);
				holder.cancelButton.setText(context.getResources().getString(R.string.dept_zntzck_sign));// 签约
				holder.cancelButton.setVisibility(View.VISIBLE);
			}
		} else {
			if (StringUtil.isNull(cancelDate)) {
				// 正常
				status = context.getResources().getString(R.string.dept_zntzck_signStatus1);
			} else {
				// 已解约
				status = context.getResources().getString(R.string.dept_zntzck_signStatus2);
			}
			holder.cancelButton.setVisibility(View.INVISIBLE);
		}
		if (!StringUtil.isNull(accountType)) {
			if (ConstantGloble.ACC_TYPE_BRO.equals(accountType)) {
				// 借记卡--展示签约卡号
				holder.signAccText.setText(signCardNos);
			} else if (ConstantGloble.ACC_TYPE_ORD.equals(accountType)
					|| ConstantGloble.ACC_TYPE_RAN.equals(accountType)) {
				// 普活、活一本，展示账号
				holder.signAccText.setText(accountNumber);
			}

		} else {
			holder.signAccText.setText("-");
		}

		holder.signTypeText.setText(signChnlFlags);
		holder.signDateText.setText(StringUtil.isNull(signDate) ? "-" : signDate);
		holder.cancelChnlFlagText.setText(cancelChnlFlags);
		holder.cancelDateText.setText(StringUtil.isNull(cancelDate) ? "-" : cancelDate);
		holder.signStatusText.setText(status);

		holder.cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onItemClickListener != null) {
					onItemClickListener.onItemClick(null, v, position, position);
				}
			}
		});
		return convertView;
	}

	private class ViewHolder {
		private TextView signAccText = null;
		private TextView signTypeText = null;
		private TextView signDateText = null;
		private TextView signStatusText = null;
		private TextView cancelChnlFlagText = null;
		private TextView cancelDateText = null;
		private Button cancelButton = null;

	}

}
