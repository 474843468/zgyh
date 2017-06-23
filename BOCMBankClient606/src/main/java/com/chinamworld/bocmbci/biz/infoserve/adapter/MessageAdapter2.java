package com.chinamworld.bocmbci.biz.infoserve.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.database.entity.PushMessage;
import com.chinamworld.bocmbci.database.entity.PushMessage.MessageType;
import com.chinamworld.bocmbci.log.LogGloble;

public class MessageAdapter2 extends DeleteBaseAdapter {
	private static final String TAG = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private List<? extends PushMessage> messages;

	public MessageAdapter2(BaseActivity activity, ListView listView, List<? extends PushMessage> messages) {
		super(activity, listView);
		this.messages = messages;
	}

	@Override
	protected IViewHodler getViewHodler() {
		ViewHolder holder = new ViewHolder();
		View view = LinearLayout.inflate(mActivity, R.layout.infoserver_item, null);
		holder.rootView = view;
		holder.tvNotification = (TextView) view.findViewById(R.id.text_notification);
		holder.tvTime = (TextView) view.findViewById(R.id.text_time);
		holder.indicatorView = view.findViewById(R.id.indicatorView);
		return holder;
	}

	@Override
	protected void bindView(int position, IViewHodler ivh, ViewGroup parent) {
		PushMessage message = messages.get(position);
		ViewHolder viewHolder = (ViewHolder) ivh;
		viewHolder.tvNotification.setText(message.getNotification());

		if (message.getMessageType() == MessageType.System) {
			viewHolder.tvTime.setVisibility(View.INVISIBLE);
			viewHolder.indicatorView.setVisibility(View.INVISIBLE);
			unDeleteListener(position);
		} else {
			viewHolder.indicatorView.setVisibility(View.VISIBLE);
			try {
				viewHolder.tvTime.setVisibility(View.VISIBLE);
				viewHolder.tvTime.setText(sdf.format(new Date(message.getDateTime())));
			} catch (Exception e) {
				LogGloble.e(TAG, e.getMessage(), e);
			}
		}
	}

	private static class ViewHolder implements IViewHodler {
		public View rootView;
		public TextView tvNotification;
		public TextView tvTime;
		public View indicatorView;

		@Override
		public View getRootView() {
			return rootView;
		}
	}

	@Override
	public int getCount() {
		return messages.size();
	}

	@Override
	public Object getItem(int position) {
		if (messages.size() > position) {
			return messages.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
