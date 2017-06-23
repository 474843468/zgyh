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
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class AccInListAdapter extends BaseAdapter {

	private Context context = null;

	private List<Map<String, Object>> accountInList = null;

	// 关联账户
	private List<Map<String, Object>> relAccList;
	// 定向收款
	private List<Map<String, Object>> dirpayeeList;
	// 实时普通
	private List<Map<String, Object>> ebpsList;
	// 实时定向
	private List<Map<String, Object>> ebpsDirList;

	public AccInListAdapter(Context context,
			List<Map<String, Object>> accountInList) {
		this.context = context;
		this.accountInList = accountInList;
	}

	@Override
	public int getCount() {
		return accountInList.size();
	}

	@Override
	public Object getItem(int position) {
		return accountInList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setDate(List<Map<String, Object>> accountInList) {
		this.accountInList = accountInList;
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.dept_acc_in_list_item, null);
		}
		// 根据2个列表组合显示 常用收款人列表 和 转出账户 去掉选择条数据列表
		TextView accountTypeTv = (TextView) convertView
				.findViewById(R.id.dept_type_tv);
		TextView accAddressTv = (TextView) convertView
				.findViewById(R.id.tv_acc_address);
		// 账号
		TextView accNoTv = (TextView) convertView.findViewById(R.id.tv_acc_no);
		TextView accNameTV = (TextView) convertView
				.findViewById(R.id.tv_acc_addname);
		accNameTV.setTextColor(context.getResources().getColor(R.color.gray));
		String str1 = null;
		String str2 = null;
		String str3 = null;
		String str4 = null;
		Map<String, Object> map = (Map<String, Object>) accountInList
				.get(position);
		// 区分关联和非关联
		if (relAccList.contains(map)) {// 关联账户
			// 别名
			str1 = (String) map.get(Tran.NICKNAME_RES);
			// 卡类型
			str2 = LocalData.AccountType.get((String) map
					.get(Tran.ACCOUNTTYPE_RES));
			// 卡号
			str3 = StringUtil.getForSixForString((String) map
					.get(Tran.ACCOUNTNUMBER_RES));
			// 姓名
			str4 = (String) map.get(Tran.ACCOUNTNAME_RES);
		} else if (dirpayeeList.contains(map)) {
			// 定向收款人
			// 收款人
			str1 = (String) map.get(Tran.TRANS_ACCOUNTNAME_RES);
			if ((Boolean) map.get(TranBaseActivity.ISHAVEBANKNAME)) {
				// 银行名称
				str2 = (String) map.get(Tran.TRANS_BANKNAME_RES);
			} else {
				// 银行名称
				str2 = "";
			}
			// 卡号
			str3 = StringUtil.getForSixForString((String) map
					.get(Tran.TRANS_ACCOUNTNUMBER_RES));
			str4 = "【定向收款人】";
			accNameTV
					.setTextColor(context.getResources().getColor(R.color.red));
			
		} else if (ebpsDirList.contains(map)) {
			// 实时定向
			// 收款人
			str1 = (String) map.get(Tran.TRANS_ACCOUNTNAME_RES);
			if ((Boolean) map.get(TranBaseActivity.ISHAVEBANKNAME)) {
				// 银行名称
				str2 = (String) map.get(Tran.TRANS_BANKNAME_RES);
			} else {
				// 银行名称
				str2 = "";
			}
			// 卡号
			str3 = StringUtil.getForSixForString((String) map
					.get(Tran.TRANS_ACCOUNTNUMBER_RES));
			str4 = "【实时定向收款人】";
			accNameTV
					.setTextColor(context.getResources().getColor(R.color.red));
		} else if (ebpsList.contains(map)) {
			// 实时普通
			// 收款人
			str1 = (String) map.get(Tran.EBPSQUERY_PAYEEACTNAME_REQ);
			// 银行名称
			str2 = (String) map.get(Tran.EBPSQUERY_PAYEEBANKNAME_REQ);
			// 卡号
			str3 = StringUtil.getForSixForString((String) map
					.get(Tran.EBPSQUERY_PAYEEACTNO_REQ));
			str4 = "【实时收款人】";
			accNameTV
			.setTextColor(context.getResources().getColor(R.color.red));
		} else {// 非关联账户
				// 收款人
			str1 = (String) map.get(Tran.TRANS_ACCOUNTNAME_RES);
			if ((Boolean) map.get(TranBaseActivity.ISHAVEBANKNAME)) {
				// 银行名称
				str2 = (String) map.get(Tran.TRANS_BANKNAME_RES);
			} else {
				// 银行名称
				str2 = "";
			}
			// 卡号
			str3 = StringUtil.getForSixForString((String) map
					.get(Tran.TRANS_ACCOUNTNUMBER_RES));
			str4 = (String) map.get(Tran.TRANS_PAYEEALIAS_RES);
		}

		accountTypeTv.setText(str1);
		accAddressTv.setText(str2);
		accNoTv.setText(str3);
		accNameTV.setText(str4);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				accountTypeTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				accAddressTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				accNoTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				accNameTV);
		return convertView;
	}

	public List<Map<String, Object>> getRelAccList() {
		return relAccList;
	}

	public void setRelAccList(List<Map<String, Object>> relAccList) {
		this.relAccList = relAccList;
	}

	public List<Map<String, Object>> getDirpayeeList() {
		return dirpayeeList;
	}

	public void setDirpayeeList(List<Map<String, Object>> dirpayeeList) {
		this.dirpayeeList = dirpayeeList;
	}

	public List<Map<String, Object>> getEbpsList() {
		return ebpsList;
	}

	public void setEbpsList(List<Map<String, Object>> ebpsList) {
		this.ebpsList = ebpsList;
	}

	public List<Map<String, Object>> getEbpsDirList() {
		return ebpsDirList;
	}

	public void setEbpsDirList(List<Map<String, Object>> ebpsDirList) {
		this.ebpsDirList = ebpsDirList;
	}

}
