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
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;

public class NotMgNotifyListAdapter extends BaseAdapter {

	private Context context = null;

	private List<Map<String, Object>> accountList = null;
	
	private OnItemClickListener imageItemClickListener = null;

	public NotMgNotifyListAdapter(Context context, List<Map<String, Object>> accountList) {
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.dept_notmg_list_item, null);
		}
		TextView notifyNoTv = (TextView) convertView
				.findViewById(R.id.dept_cd_number_tv);
		TextView drawDateTv = (TextView) convertView
				.findViewById(R.id.dept_type_tv);
		TextView notifyStatusTv = (TextView) convertView
				.findViewById(R.id.dept_avaliable_balance_tv);
		
		ImageView goDetailIv = (ImageView) convertView
				.findViewById(R.id.dept_notify_detail_iv);
		
		String notifyNo = (String) accountList.get(position).get(Dept.NOTIFY_ID);
		notifyNoTv.setText(notifyNo);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, notifyNoTv);
		
		String drawDate = (String) accountList.get(position).get(Dept.DRAW_DATE);
		drawDateTv.setText(drawDate);
		
		String notifyStatus = (String) accountList.get(position).get(Dept.NOTIFY_STATUS);
		notifyStatusTv.setText(LocalData.notifyStatus.get(notifyStatus));
		
		return convertView;
	}

	public OnItemClickListener getImageItemClickListener() {
		return imageItemClickListener;
	}

	public void setImageItemClickListener(OnItemClickListener imageItemClickListener) {
		this.imageItemClickListener = imageItemClickListener;
	}

	
}
