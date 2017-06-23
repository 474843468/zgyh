package com.chinamworld.bocmbci.biz.finc.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 账户列表适配器
 * 
 * @author fsm
 * 
 */
public class FincTaAccountAdapter extends BaseAdapter {
	/** 账户列表信息 */
	private List<Map<String, Object>> list;
	private Context context;
	/** 选中项记录 */
	private int selectedPosition = -1;
	/**
	 * 当前指向的位置
	 */
	private int currentPointPosition = -1;

	public FincTaAccountAdapter(Context context,
			List<Map<String, Object>> list) {
		this.context = context;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Map<String, Object> map=list.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.finc_ta_list_item, null);
		}
		
		/** 注册基金公司名称 */
		TextView tv_acc_type_value = (TextView) convertView
				.findViewById(R.id.acc_type_value);
		String taCompany=StringUtil.valueOf1((String)map.get(Finc.QUERYTAACCDETAILLIST_FUNDREGNAME));
		tv_acc_type_value.setText(StringUtil.valueOf1(taCompany));
		
		/** 账户状态 */
		TextView tv_acc_account_nickname = (TextView) convertView
				.findViewById(R.id.acc_account_nickname);
		String accState = map.get(Finc.QUERYTAACCDETAILLIST_ACCOUNTSTATUS).toString();
		tv_acc_account_nickname.setText(StringUtil.valueOf1(LocalData.fincTaAccTypeMap.get(accState)));
		
		/** Ta 账户 */
		TextView tv_acc_account_num = (TextView) convertView
				.findViewById(R.id.acc_account_num);
		String taAcc = StringUtil.valueOf1((String)map.get(Finc.QUERYTAACCDETAILLIST_TAACCOUNTNO));
		tv_acc_account_num.setText(StringUtil.valueOf1(taAcc));
		
		return convertView;
	}

	/**
	 * 给被选中的某一项设置高亮背景
	 * 
	 * @param selectedPosition
	 */
	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
		notifyDataSetChanged();
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

}
