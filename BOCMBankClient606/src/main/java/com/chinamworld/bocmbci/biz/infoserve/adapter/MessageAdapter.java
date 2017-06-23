package com.chinamworld.bocmbci.biz.infoserve.adapter;

import android.app.Activity;
import android.text.TextUtils.TruncateAt;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.infoserve.InfoServeMainActivity;
import com.chinamworld.bocmbci.biz.infoserve.InfoServeMainFragment;
import com.chinamworld.bocmbci.database.entity.PushMessage;
import com.chinamworld.bocmbci.database.entity.PushMessage.MessageType;
import com.chinamworld.bocmbci.log.LogGloble;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.support.v4.app.Fragment;
public class MessageAdapter extends BaseAdapter {
	private static final String TAG = MessageAdapter.class.getSimpleName();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private Activity context;
	private List<? extends PushMessage> messages;
	private float x, ux;
	private float y, uy;
	private ListView listView;
	private List<Boolean> isShowDelList;
	private Fragment fragment;
	public MessageAdapter(Fragment fragment,Activity context, List<? extends PushMessage> messages, ListView listView) {
		super();
		this.context = context;
		this.fragment=fragment;
		this.messages = messages;
		this.listView = listView;
		this.isShowDelList = new ArrayList<Boolean>();
		for (int i = 0; i < messages.size(); i++) {
			isShowDelList.add(false);
		}
	}


	public MessageAdapter(Activity context, List<? extends PushMessage> messages, ListView listView) {
		super();
		this.context = context;
		this.messages = messages;
		this.listView = listView;
		this.isShowDelList = new ArrayList<Boolean>();
		for (int i = 0; i < messages.size(); i++) {
			isShowDelList.add(false);
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final PushMessage message = messages.get(position);

		ViewHolder h = null;
		if (convertView == null) {
			h = new ViewHolder();
			convertView = LinearLayout.inflate(context, R.layout.infoserver_item, null);
			h.tvNotification = (TextView) convertView.findViewById(R.id.text_notification);
			h.tvTime = (TextView) convertView.findViewById(R.id.text_time);
			h.indicatorView = convertView.findViewById(R.id.indicatorView);
			h.view_image = convertView.findViewById(R.id.view_image);
			h.btnDel = (Button) convertView.findViewById(R.id.btn_delete);
			convertView.setTag(h);
		} else {
			h = (ViewHolder) convertView.getTag();
		}

		if (message.getMessageType()==MessageType.System) {
			h.tvNotification.setSingleLine(true);
			h.tvNotification.setEllipsize(TruncateAt.END);
		}
		h.tvNotification.setText(message.getNotification());

		if (message.getMessageType() == MessageType.System) {
			h.tvTime.setVisibility(View.GONE);
			h.view_image.setVisibility(View.GONE);
		} else {
			try {
				h.tvTime.setVisibility(View.VISIBLE);
				h.tvTime.setText(sdf.format(new Date(message.getDateTime())));
			} catch (Exception e) {
				LogGloble.e(TAG, e.getMessage(), e);
			}
		}

		if (message.getMessageType() == MessageType.System) {
			h.indicatorView.setVisibility(View.INVISIBLE);
			h.btnDel.setVisibility(View.INVISIBLE);
		} else {
			h.indicatorView.setVisibility(message.isReaded() ? View.INVISIBLE : View.VISIBLE);
			// 是否显示删除按钮
			h.btnDel.setVisibility(isShowDelList.get(position) ? View.VISIBLE : View.GONE);

			// 为每一个view项设置触控监听
			convertView.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					// 当按下时处理
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						// TODO设置背景为选中状态
						// 获取按下时的x轴坐标
						x = event.getX();
						y = event.getY();

					} else if (event.getAction() == MotionEvent.ACTION_UP
							|| event.getAction() == MotionEvent.ACTION_CANCEL) {
						// 松开处理
						// TODO设置背景为未选中正常状态
						// 获取松开时的x坐标
						ux = event.getX();
						uy = event.getY();

						boolean flag = isShowDelList.get(position);
						for (int i = 0; i < isShowDelList.size(); i++) {
							if (isShowDelList.get(i)) {
								flag = true;
								break;
							}
						}
						if (Math.abs(x - ux) > 20) {
							for (int i = 0; i < isShowDelList.size(); i++) {
								if (i == position) {
									isShowDelList.set(position, !isShowDelList.get(position));
								} else {
									isShowDelList.set(i, false);
								}
							}
						} else {
							for (int i = 0; i < isShowDelList.size(); i++) {
								isShowDelList.set(i, false);
							}
							if (!flag && Math.abs(y - uy) < 20)
								listView.performItemClick(v, position, listView.getItemIdAtPosition(position));
						}
						notifyDataSetChanged();
					} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
						// TODO当滑动时背景为选中状态
					}

					return true;

				}
			});
			

			// 为删除按钮添加监听事件，实现点击删除按钮时删除该项
			h.btnDel.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					messages.remove(position);
					isShowDelList.remove(position);
					notifyDataSetChanged();
					// 删除消息
//					((InfoServeMainActivity) context).deleteMessage(message);
					((InfoServeMainFragment)fragment).deleteMessage(message);
				}
			});
		}
		if(message.isReaded()){
			h.tvNotification.setTextColor(context.getResources().getColor(R.color.fonts_gray_li));
			h.tvTime.setTextColor(context.getResources().getColor(R.color.fonts_gray_li));
		}else{

			h.tvNotification.setTextColor(context.getResources().getColor(R.color.fonts_black));
			h.tvTime.setTextColor(context.getResources().getColor(R.color.fonts_black));
		}
		return convertView;
	}

	private static class ViewHolder {
		public TextView tvNotification;
		public TextView tvTime;
		public View indicatorView;
		public View view_image;
		public Button btnDel;
	}
}
