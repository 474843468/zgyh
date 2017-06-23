package com.chinamworld.bocmbci.biz.tran.remit.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class RemitSharedAccTypeAdapter extends BaseAdapter {
	private Context context;
	private List<Map<String, String>> sharedAccountList;
	private OnClickListener deleteOnClickListener;

	public RemitSharedAccTypeAdapter(Context context, List<Map<String, String>> sharedAccountList) {
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
		private TextView tv_sharedName;
		private TextView tv_sharedNo;
		private TextView tv_deleteView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.tran_remit_share_acc_list_item, null);
			holder = new ViewHolder();
			holder.tv_sharedNo = (TextView) convertView.findViewById(R.id.shared_acc_no);
			holder.tv_sharedName = (TextView) convertView.findViewById(R.id.shared_acc_name);
			holder.tv_deleteView = (TextView) convertView.findViewById(R.id.tv_delete);
			SpannableStringBuilder spannedDelete = new SpannableStringBuilder(holder.tv_deleteView.getText());
			spannedDelete.setSpan(new URLSpan(""), 0, spannedDelete.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			spannedDelete.setSpan(new ForegroundColorSpan(Color.RED), 0, spannedDelete.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			holder.tv_deleteView.setText(spannedDelete);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, holder.tv_sharedName);
		Map<String, String> sharedmap = sharedAccountList.get(position);
		String accname = sharedmap.get(Tran.TRAN_REMIT_APP_SHAREACCNAME_REQ);
		String accNo = sharedmap.get(Tran.TRAN_REMIT_APP_SHARECARDNO_REQ);
		holder.tv_sharedName.setText(accname);
		holder.tv_sharedNo.setText(StringUtil.getForSixForString(accNo));
		holder.tv_deleteView.setTag(sharedmap);
		holder.tv_deleteView.setOnClickListener(this.deleteOnClickListener);
		return convertView;
	}

	public void setDeleteOnClickListener(OnClickListener deleteOnClickListener) {
		this.deleteOnClickListener = deleteOnClickListener;
	}

	/**
	 * 获取修改上传参数
	 */
	public List<Map<String, String>> getModifyRequestList() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (sharedAccountList != null) {
			for (int i = 0; i < sharedAccountList.size(); i++) {
				Map<String, String> shareMap = sharedAccountList.get(i);
				Map<String, String> map = new HashMap<String, String>();
				map.put(Tran.TRAN_REMIT_APP_SHARECARDNO_REQ, shareMap.get(Tran.TRAN_REMIT_APP_SHARECARDNO_REQ));
				map.put(Tran.TRAN_REMIT_APP_SHAREACCNAME_REQ, shareMap.get(Tran.TRAN_REMIT_APP_SHAREACCNAME_REQ));
				// 当输入为R时，则该账号页面显示的共享状态栏位必须为N-无效，当输入为S时，则该账号页面显示的共享状态栏位必须为Y-有效
				// A-新增 R-恢复 S-停用
				map.put(Tran.TRAN_REMIT_APP_SHAREOPTYPE_REQ, "R"); // TODOFs
				list.add(map);
			}
		}
		return list;
	}
}
