package com.chinamworld.bocmbci.biz.tran.remit.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 套餐修改删除共享账户适配器
 * 
 * @author wangmengmeng
 * 
 */
public class RemitModifyDelAccAdapter extends BaseAdapter {
	private Context context;
	private List<Map<String, String>> sharedAccountList;
	/** 是否选中 true代表可选,false代表不可选 */
	private List<Boolean> booleanSelect = new ArrayList<Boolean>();
	/** i==1:多选情况；否则:只做显示 */
	private boolean isSelect;

	public RemitModifyDelAccAdapter(Context context, List<Map<String, String>> sharedAccountList, boolean isSelect) {
		this.context = context;
		this.sharedAccountList = sharedAccountList;
		this.isSelect = isSelect;

		for (int i = 0; i < sharedAccountList.size(); i++) {
			Map<String, String> map = sharedAccountList.get(i);
			//booleanSelect.add("N".equalsIgnoreCase(map.get("shareStatus")));
			booleanSelect.add(false);
		}
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
		private LinearLayout ll_image;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.tran_remit_del_modify_sharedacc_item, null);
			holder = new ViewHolder();
			holder.cb_sharedName = (ImageView) convertView.findViewById(R.id.checkBox_del);
			holder.tv_shared = (TextView) convertView.findViewById(R.id.tv_shared);
			holder.ll_image = (LinearLayout) convertView.findViewById(R.id.ll_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String, String> sharedmap = new HashMap<String, String>();
		sharedmap = sharedAccountList.get(position);
		// String accname = sharedmap.get(Tran.TRAN_REMIT_APP_SHAREACCNAME_REQ);
		String accNo = sharedmap.get(Tran.TRAN_SHARECARDNO_RES);
		holder.tv_shared.setText(StringUtil.getForSixForString(accNo));
		if (isSelect) {
			if (booleanSelect.get(position)) {
				holder.cb_sharedName.setImageResource(R.drawable.bg_check_box_checked);
			} else {
				holder.cb_sharedName.setImageResource(R.drawable.bg_check_box);
			}
		} else {
			holder.ll_image.setVisibility(View.GONE);
		}

		holder.cb_sharedName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Boolean b = booleanSelect.get(position);
				booleanSelect.set(position, !b);
				notifyDataSetChanged();
			}
		});
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

	public List<Map<String, String>> getModifyRequestList() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (sharedAccountList != null && booleanSelect != null) {
			for (int i = 0; i < sharedAccountList.size(); i++) {
				if (booleanSelect.get(i)) {
					Map<String, String> shareMap = sharedAccountList.get(i);
					Map<String, String> map = new HashMap<String, String>(); 
					map.put(Tran.TRAN_REMIT_APP_SHARECARDNO_REQ, shareMap.get(Tran.TRAN_REMIT_APP_SHARECARDNO_REQ));
					map.put(Tran.TRAN_REMIT_APP_SHAREACCNAME_REQ, shareMap.get(Tran.TRAN_REMIT_APP_REMITSETMEALCUSTNAME_REQ)); 
					// 当输入为R时，则该账号页面显示的共享状态栏位必须为N-无效，当输入为S时，则该账号页面显示的共享状态栏位必须为Y-有效
					// A-新增 R-恢复 S-停用
					map.put(Tran.TRAN_REMIT_APP_SHAREOPTYPE_REQ, "S"); // TODOFs
					list.add(map);
				}
			}
		}
		return list;
	}
}
