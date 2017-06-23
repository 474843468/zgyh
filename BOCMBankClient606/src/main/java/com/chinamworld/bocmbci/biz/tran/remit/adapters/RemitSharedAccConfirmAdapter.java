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
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 共享账户确认页面适配器
 * 
 * @author wangmengmeng
 * 
 */
public class RemitSharedAccConfirmAdapter extends BaseAdapter {
	private Context context;
	private List<Map<String, String>> sharedAccountList;

	public RemitSharedAccConfirmAdapter(Context context,
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
		/**共享卡号 /用户名/卡号 */
		private TextView tv_sharedName;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.tran_remit_share_acc_list_item_input, null);
			holder = new ViewHolder();
			holder.tv_sharedName = (TextView) convertView
					.findViewById(R.id.shared_acc_names);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.tv_sharedName);
		Map<String, String> sharedmap = new HashMap<String, String>();
		sharedmap = sharedAccountList.get(position);
		String accname = sharedmap.get(Tran.TRAN_REMIT_APP_SHAREACCNAME_REQ);
		String accNo = sharedmap.get(Tran.TRAN_REMIT_APP_SHARECARDNO_REQ);
		holder.tv_sharedName.setText(StringUtil.getForSixForString(accNo)
				+ ConstantGloble.ACC_STRING + accname);
		return convertView;
	}

}
