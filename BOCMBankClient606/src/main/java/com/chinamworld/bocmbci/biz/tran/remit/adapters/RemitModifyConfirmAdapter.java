package com.chinamworld.bocmbci.biz.tran.remit.adapters;

import java.util.HashMap;
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
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 共享账户确认页面适配器
 * 
 * @author wangmengmeng
 * 
 */
public class RemitModifyConfirmAdapter extends BaseAdapter {
	private Context context;
	private List<Map<String, String>> sharedAccountList;

	public RemitModifyConfirmAdapter(Context context, List<Map<String, String>> sharedAccountList) {
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
		/*** 共享账户 / 卡号*/
		private TextView tv_sharedcad;
		/*** 共享账户 /用户名*/
		private TextView tv_sharedName;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.tran_remit_share_acc_list_item_confirm, null);
			holder = new ViewHolder();
			holder.tv_sharedcad = (TextView) convertView.findViewById(R.id.tv_sharedcad);
			holder.tv_sharedName = (TextView) convertView.findViewById(R.id.tv_sharedName);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(context,holder.tv_sharedName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, holder.tv_sharedcad);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, holder.tv_sharedName);
		Map<String, String> sharedmap = new HashMap<String, String>();
		sharedmap = sharedAccountList.get(position);
		String accNo = sharedmap.get(Tran.TRAN_REMIT_APP_SHARECARDNO_REQ);
		String accname = sharedmap.get(Tran.TRAN_REMIT_APP_SHAREACCNAME_REQ);
		holder.tv_sharedcad.setText(StringUtil.getForSixForString(accNo));
		holder.tv_sharedName.setText(accname);
		return convertView;
	}

}
