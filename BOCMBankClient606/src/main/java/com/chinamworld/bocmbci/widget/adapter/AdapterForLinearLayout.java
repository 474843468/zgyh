package com.chinamworld.bocmbci.widget.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.infoserve.InfoServeDetialActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;

public class AdapterForLinearLayout extends BaseAdapter {

	private LayoutInflater mInflater;
	// private List<? extends Map<String, ?>> data;
	private List<Map<String, String>> data;
	private Context context;
	public AdapterForLinearLayout(Context context,
			List<Map<String, String>> list) {
		this.data = list;
		this.context=context;
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder= new ViewHolder();
		convertView = mInflater.inflate(R.layout.item_for_new_info, null);
		holder.titleTextView = (TextView) convertView
				.findViewById(R.id.display);
		holder.contentTextView = (TextView) convertView
				.findViewById(R.id.tvcontent);
		convertView.setId(position);

		holder.titleTextView.setText(data.get(position).get(Login.SUBJECT));
		holder.contentTextView.setText(data.get(position).get(Login.CONTENT));
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,InfoServeDetialActivity.class);
				intent.putExtra(ConstantGloble.SERVICEINFOTITLE, data.get(position).get(Login.SUBJECT));
				intent.putExtra(ConstantGloble.SERVICEINFOCONTENT, data.get(position).get(Login.CONTENT));
				BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
//				if (holder.contentTextView.getVisibility() == View.VISIBLE) {
//					holder.contentTextView.setVisibility(View.GONE);
//				}else{
//					holder.contentTextView.setVisibility(View.VISIBLE);
//				}

			}
		});
		if (position == data.size() - 1) {
			((ImageView) convertView.findViewById(R.id.tvdivider))
					.setVisibility(View.GONE);
			convertView.setBackgroundResource(R.drawable.selector_for_click_last_item);
		} else {
			((ImageView) convertView.findViewById(R.id.tvdivider))
					.setVisibility(View.VISIBLE);
			convertView.setBackgroundResource(R.drawable.selector_for_click_item);
		}
		return convertView;
	}

	public class ViewHolder {
		public TextView titleTextView;
		public TextView contentTextView;

	}

}
