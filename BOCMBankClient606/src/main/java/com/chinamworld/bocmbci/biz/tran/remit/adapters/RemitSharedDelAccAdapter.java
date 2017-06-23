package com.chinamworld.bocmbci.biz.tran.remit.adapters;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;

/**
 * 删除共享账户适配器
 * 
 * @author wangmengmeng
 * 
 */
public class RemitSharedDelAccAdapter extends BaseAdapter {
	private Context context;
	private List<Map<String, String>> sharedAccountList;
	/** 是否选中 true代表可选,false代表不可选 */
	private List<Boolean> booleanSelect = new ArrayList<Boolean>();

	public RemitSharedDelAccAdapter(Context context,
			List<Map<String, String>> sharedAccountList) {
		this.context = context;
		this.sharedAccountList = sharedAccountList;
	}

	@Override
	public int getCount() {
		return sharedAccountList.size();
	}

	@Override
	public Object getItem(int position) {
		return sharedAccountList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public class ViewHolder {
		// 共享账户信息
		private ImageView cb_sharedName;
		private TextView tv_shared;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.tran_remit_del_sharedacc_item, null);
			holder = new ViewHolder();
			holder.cb_sharedName = (ImageView) convertView
					.findViewById(R.id.checkBox_del);
			holder.tv_shared = (TextView) convertView
					.findViewById(R.id.tv_shared);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.tv_shared);
		Map<String, String> sharedmap = new HashMap<String, String>();
		sharedmap = sharedAccountList.get(position);
		String accname = sharedmap.get(Tran.TRAN_REMIT_APP_SHAREACCNAME_REQ);
		String accNo = sharedmap.get(Tran.TRAN_REMIT_APP_SHARECARDNO_REQ);
		holder.tv_shared.setText(accNo + ConstantGloble.ACC_STRING + accname);
		if (booleanSelect.get(position)) {
			holder.cb_sharedName.setImageResource(R.drawable.bg_check_box);
		} else {
			holder.cb_sharedName
					.setImageResource(R.drawable.bg_check_box_checked);
		}
		return convertView;
	}

	/**
	 * @return the booleanSelect
	 */
	public List<Boolean> getBooleanSelect() {
		return booleanSelect;
	}

	/**
	 * @param booleanSelect
	 *            the booleanSelect to set
	 */
	public void setBooleanSelect(List<Boolean> booleanSelect) {
		this.booleanSelect = booleanSelect;
		notifyDataSetChanged();
	}
}
