package com.chinamworld.bocmbci.biz.dept.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 存款管理 - 我的定期存款 - 存单列表
 * 
 * @author luqp 2015年9月17日11:28:48
 */
public class MyRegSaveAdapte extends BaseAdapter {
	private Context context;
	private List<Map<String, Object>> accountList;
	private OnItemClickListener imageItemClickListener;
	private String currencyCode;

	public MyRegSaveAdapte(Context context, List<Map<String, Object>> accountList) {
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

	/** 刷新适配器 */
	public void refreshData(List<Map<String, Object>> accountList) {
		this.accountList = accountList;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.dept_account_list_item, null);
			holder = new ViewHolder();
			/** 隐藏右箭头 */
			holder.btGoItem = (ImageView) convertView.findViewById(R.id.loan_btn_goitem);
			holder.btGoItem.setVisibility(View.VISIBLE);
			/** 存款金额 */
			holder.accValue = (TextView) convertView.findViewById(R.id.dept_account_value);
			/** 册号/序号 */
			holder.accNumber = (TextView) convertView.findViewById(R.id.dept_account_number);
			/** 存期 */
			holder.accTime = (TextView) convertView.findViewById(R.id.dept_account_time);
			/** 币种/钞汇 */
			holder.accCode = (TextView) convertView.findViewById(R.id.dept_account_code);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 币种
		String currencyCode = (String) accountList.get(position).get(Comm.CURRENCYCODE);
		String code = LocalData.Currency.get(currencyCode); // LocalData.Currency()
		// 钞汇
		String strCashRemit = (String) accountList.get(position).get(Dept.CASHREMIT);
		String remit = LocalData.CurrencyCashremit.get(strCashRemit);
		// 如果币种为人民币 不显示钞汇 只显示币种
		if (currencyCode.equals("001") || currencyCode.equals("CNY")) {
			holder.accCode.setText(code);
		} else {
			holder.accCode.setText(code + remit);
		}
		// 币种钞汇显示不全时 气泡显示
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, holder.accCode);

		// 可用余额
		String bookBalance = (String) accountList.get(position).get(Dept.DEPT_BOOKBALANCE_RES);
		String accBookBalance = StringUtil.parseStringCodePattern(currencyCode, bookBalance, 2);
		holder.accValue.setText(accBookBalance);
		// 册号 序号
		String volumeNumber = (String) accountList.get(position).get(Dept.VOLUME_NUMBER);
		String cdNumber = (String) accountList.get(position).get(Dept.CD_NUMBER);
		holder.accNumber.setText(volumeNumber + "/" + cdNumber);
		// 存期 一天七天存款
		String strCdPeriod = (String) accountList.get(position).get(Dept.CD_PERIOD);
		String type = (String) accountList.get(position).get(Dept.TYPE);
		if (type.equals("166")) { // 通知存款
			if (strCdPeriod.equals("1")) { // 一天通知存款
				holder.accTime.setText("一天");
			} else if (strCdPeriod.equals("7")) { // 七天通知存款
				holder.accTime.setText("七天");
			}
		} else { // 正常显示存期
			holder.accTime.setText(LocalData.depositReceipt.get(strCdPeriod));
		}
		return convertView;
	}

	public class ViewHolder {
		// 存款金额
		private TextView accValue;
		// 册号/序号
		private TextView accNumber;
		// 存期
		private TextView accTime;
		// 币种/钞汇
		private TextView accCode;
		// 右箭头
		private ImageView btGoItem;
	}

	public OnItemClickListener getImageItemClickListener() {
		return imageItemClickListener;
	}

	public void setImageItemClickListener(OnItemClickListener imageItemClickListener) {
		this.imageItemClickListener = imageItemClickListener;
	}
}
