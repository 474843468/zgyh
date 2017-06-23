package com.chinamworld.bocmbci.biz.dept.adapter;

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
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 存款管理 我的定期存款 详情列表 支取 转出账户
 * 
 * @author luqp 2015年10月19日19:23:42
 */
public class AccOutNewListAdapter extends BaseAdapter {

	private Context context;
	/** 查询账户列表 */
	private List<Map<String, Object>> accountList = null;
	private OnItemClickListener imageItemClickListener;

	public AccOutNewListAdapter(Context context, List<Map<String, Object>> accountList) {
		this.context = context;
		this.accountList = accountList;
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
	
	/** 刷新适配器*/
	public void refreshData(List<Map<String, Object>> accountList) {
		this.accountList = accountList;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.dept_acc_in_list_item, null);
			holder = new ViewHolder();
			holder.accountTypeTv = (TextView) convertView.findViewById(R.id.dept_type_tv);
			holder.accountNumberTypeTv = (TextView) convertView.findViewById(R.id.tv_acc_no);
			holder.accVolumeNumber = (TextView) convertView.findViewById(R.id.tv_acc_addname);
			holder.accCdNumber = (TextView) convertView.findViewById(R.id.tv_acc_address);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 账户类型
		String accountType = (String) accountList.get(position).get(Comm.ACCOUNT_TYPE);
		// 账户
		String accountNumber = (String) accountList.get(position).get(Comm.ACCOUNTNUMBER);
		// 册号
		String volumeNumber = (String) accountList.get(position).get(Dept.VOLUME_NUMBER);
		// 序号
		String cdNumber = (String) accountList.get(position).get(Dept.CD_NUMBER);
		// 账户类型
		String strAccountType = LocalData.AccountType.get(accountType);
		holder.accountTypeTv.setText(StringUtil.isNullChange(strAccountType));
		holder.accountNumberTypeTv.setText(StringUtil.getForSixForString(accountNumber));
		holder.accVolumeNumber.setText(StringUtil.isNullChange(volumeNumber));
		holder.accCdNumber.setText(StringUtil.isNullChange(cdNumber));
		
		return convertView;
	}

	public class ViewHolder {
		/** 账户类型 */
		private TextView accountTypeTv;
		/** 册号 */
		private TextView accVolumeNumber;
		/** 存单账户 */
		private TextView accountNumberTypeTv;
		/** 序号 */
		private TextView accCdNumber;
	}
	
	public OnItemClickListener getImageItemClickListener() {
		return imageItemClickListener;
	}

	public void setImageItemClickListener(OnItemClickListener imageItemClickListener) {
		this.imageItemClickListener = imageItemClickListener;
	}
}
