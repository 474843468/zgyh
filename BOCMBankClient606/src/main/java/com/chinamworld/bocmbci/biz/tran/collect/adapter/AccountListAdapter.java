package com.chinamworld.bocmbci.biz.tran.collect.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Collect;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectStatusType;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: AccountListAdapter
 * @Description: 被归集列表
 * @author luql
 * @date 2014-3-17 下午04:12:20
 */
public class AccountListAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> data = null;
	private List<Map<String, Object>> selectData = null;

	private boolean isSelect = false;

	public AccountListAdapter(Context context, List<Map<String, Object>> data) {
		this.mContext = context;
		if(data != null){
			this.data = data;
		}else{
			this.data = new ArrayList<Map<String,Object>>();
		}
		selectData = new ArrayList<Map<String, Object>>();
		initSelectData();
	}

	
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Map<String, Object> getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return data == null || data.isEmpty();
	}

	public void setData(List<Map<String, Object>> data) {
		this.data = data;
		initSelectData();
	}

	public List<Map<String, Object>> getData() {
		return this.data;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.collect_list_item, null);
			holder = new ViewHolder();
			holder.selectView = (CheckBox) convertView.findViewById(R.id.cb_select);
			holder.accountTV = (TextView) convertView.findViewById(R.id.tv_one_item);
			holder.nameTV = (TextView) convertView.findViewById(R.id.tv_two_item);
			holder.bankTV = (TextView) convertView.findViewById(R.id.tv_three_item);
			holder.arrowView = convertView.findViewById(R.id.rate_gotoDetail);
			holder.rootView = convertView.findViewById(R.id.root);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		@SuppressWarnings("unchecked")
		final Map<String, Object> map = (Map<String, Object>) data.get(position);
		holder.accountTV.setText(StringUtil.getForSixForString((String) map.get(Collect.payerAccount)));
		holder.nameTV.setText(StringUtil.isNullChange((String) map.get(Collect.payerAccountName)));
		holder.bankTV.setText(StringUtil.isNullChange((String) map.get(Collect.payerAccBankName)));
		final boolean isStatus = CollectStatusType.isValidStatus((String) map.get(Collect.status));
		if (this.isSelect) {
			holder.selectView.setVisibility(View.VISIBLE);
			holder.arrowView.setVisibility(View.GONE);
			holder.rootView.setVisibility(View.VISIBLE);
			if (isStatus) {
				// 有效
				holder.selectView.setEnabled(true);
				holder.selectView.setChecked(selectData.get(position) != null);
				holder.rootView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ViewGroup vg = (ViewGroup) v.getParent();
						CheckBox selectView = (CheckBox) vg.findViewById(R.id.cb_select);
						if (selectView.isEnabled()) {
							selectView.performClick();
						}
					}
				});
			} else {
				// 无效
				holder.selectView.setEnabled(false);
				holder.selectView.setChecked(false);
				holder.rootView.setOnLongClickListener(null);
			}
			
		} else {
			holder.selectView.setVisibility(View.GONE);
			holder.selectView.setChecked(false);
			holder.selectView.setEnabled(false);
			holder.arrowView.setVisibility(View.VISIBLE);
			holder.rootView.setVisibility(View.GONE);
		}
		holder.selectView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				selectData.set(position, isChecked ? map : null);
			}
		});

		PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext, holder.accountTV);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext, holder.nameTV);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext, holder.bankTV);
		return convertView;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
		initSelectData();
		notifyDataSetChanged();
	}

	public boolean isSelect() {
		return isSelect;
	}

	public List<Map<String, Object>> getSelectData() {
		List<Map<String, Object>> result = null;
		if (this.isSelect) {
			result = new ArrayList<Map<String, Object>>();
			ListIterator<Map<String, Object>> listIterator = selectData.listIterator();
			while (listIterator.hasNext()) {
				Map<String, Object> _data = listIterator.next();
				if (_data != null) {
					result.add(_data);
				}
			}
		}
		return result;
	}

	private void initSelectData() {
		selectData.clear();
		if (data != null) {
			for (int i = 0; i < data.size(); i++) {
				selectData.add(null);
			}
		}
	}

	private static class ViewHolder {
		public CheckBox selectView;
		public TextView accountTV;
		public TextView nameTV;
		public TextView bankTV;
		public View arrowView;
		public View rootView;
	}

}
