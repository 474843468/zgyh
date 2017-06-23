package com.chinamworld.bocmbci.widget;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.widget.adapter.ButtonAdapter;
import com.chinamworld.bocmbci.widget.adapter.HistoryListAdapter;

/**
 * RadioButton+ListView组合，实现PopupWindow显示在界面顶端的功能 PopupWindow的显示、隐藏都带有动画
 * 引用ButtonListViewPopupWindow实例的activity最好是全屏显示
 * 
 * @author Administrator
 * 
 */
public class CurtainPopupWindow extends View {

	private LayoutInflater inflater = null;// 渲染布局
	private Context context = null;// 上下文
	private PopupWindow pop = null;

	private LinearLayout popLinearLayout = null;
	private LinearLayout contentLayout = null;// listLayout
	private ImageButton bottomButton = null;
	/** 预留信息 */
	private Button leaveMessageBtn = null;
	/** 登录历史 */
	private Button historyBtn = null;
	/** 通知信息 */
	private Button notifyBtn = null;
	/** 最新消息 */
	private Button newMessageBtn = null;

//	private TextView message = null;
//	private TextView history = null;
	private TextView notifyTv = null;
	private TextView newMessageTv = null;

	private LinearLayout listViewLayout = null;
	private ListView listView = null;

	private OnClickListener buttomOnClickListener = null;
	/**
	 * @ onItemClickListener:listView中的每一项被点击触发的事件
	 */
//	private OnItemClickListener onItemClickListener = null;
	/**
	 * @param itemButtonClickListener
	 *            :listView中的每一子项的Button被点击触发的事件
	 */
	private OnItemClickListener itemNewMessageButtonClickListener = null;
	
	private OnItemClickListener itemNotifyButtonClickListener = null;

	public CurtainPopupWindow(Context context) {
		super(context);
		this.context = context;
		init();
	}

	/**
	 * @ popLinearLayout显示PopupWindow的布局文件---n_pop.xml
	 * 
	 * @param v
	 *            :popupWindow显示的位置即在哪里显示
	 * @return 返回popupWindow的整个布局内容
	 */
	public void showPopupWindow(ImageButton ibPullDown ) {
		int[] location = new int[2];
		ibPullDown.getLocationOnScreen(location);
		int y = location[1];
		int x = location[0];
		pop = new PopupWindow(popLinearLayout, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		// 点击其他点，popupWindow不会消失
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		// 添加动画
		pop.setAnimationStyle(R.style.popAnimation);
		pop.setHeight(LayoutValue.SCREEN_HEIGHT/2);
		pop.setWidth((LayoutValue.SCREEN_WIDTH));
		pop.showAtLocation(this, Gravity.TOP, 0, y);
		pop.update();
	}

	/**
	 * 初始化所有的控件
	 * 
	 * @listLinearLayout：四个RadioButton对应内容显示的地方
	 * @screenWidth：屏幕宽度，
	 * @screenHeight:屏幕高度
	 * @pop:PopupWindow的实例对象
	 */
	public void init() {
		inflater = LayoutInflater.from(context);
		popLinearLayout = (LinearLayout) inflater.inflate(R.layout.popwindow,
				null);
		contentLayout = (LinearLayout) popLinearLayout
				.findViewById(R.id.content_layout);

		// listview容器
		listViewLayout = (LinearLayout) inflater.inflate(
				R.layout.popwindow_list, null);
		listView = (ListView) listViewLayout.findViewById(R.id.listView1);
		// 得到popupWindow里面的所有的控件
		bottomButton = (ImageButton) popLinearLayout
				.findViewById(R.id.bottomButton);
		bottomButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pop.isShowing()) {
					pop.dismiss();
				}
			}
		});

		// 四个按钮
		leaveMessageBtn = (Button) popLinearLayout.findViewById(R.id.message);
		historyBtn = (Button) popLinearLayout.findViewById(R.id.history);
		notifyBtn = (Button) popLinearLayout.findViewById(R.id.nitify);
		newMessageBtn = (Button) popLinearLayout.findViewById(R.id.newMessage);
		// 四个TextView,默认是隐藏的
//		message = (TextView) popLinearLayout.findViewById(R.id.message1);
//		history = (TextView) popLinearLayout.findViewById(R.id.history1);
		notifyTv = (TextView) popLinearLayout.findViewById(R.id.nitify1);
		newMessageTv = (TextView) popLinearLayout.findViewById(R.id.newMessage1);
	}

	/**
	 * 触发底部Button按钮
	 * 
	 * @param onClickListener监听事件
	 */
	public void setOnBottomButtonClickListener(OnClickListener onClickListener) {
		bottomButton.setOnClickListener(onClickListener);
	}

	/**
	 * 点击popupWindow底部的按钮，关闭popupWindow
	 */
	public void close() {
		if (pop.isShowing()) {
			pop.dismiss();
		}
	}

	/**
	 * messageRadio的监听事件
	 */
	public void setOnLeaveMessageBtnClick(OnClickListener onClickListener) {
		leaveMessageBtn.setOnClickListener(onClickListener);
	}

	/**
	 * historyRadio的监听事件
	 */
	public void setOnHistoryBtnClick(OnClickListener onClickListene) {
		historyBtn.setOnClickListener(onClickListene);
	}

	/**
	 * notifyRadio的监听事件
	 */
	public void setOnNotifyBtnClick(OnClickListener onClickListener) {
		notifyBtn.setOnClickListener(onClickListener);
	}

	/**
	 * newMessageRadio的监听事件
	 */
	public void setOnNewMessageBtnClick(OnClickListener onClickListener) {
		newMessageBtn.setOnClickListener(onClickListener);
	}

	/**
	 * 刷新listView中的数据
	 * 
	 * @onItemClickListener:listView中每一子项被选中时触发的事件
	 * @itemButtonClickListener:listView中每一子项的Button事件
	 */
	public void refreshNewMessageList(ArrayList<Map<String,String>> arrayList) {
		contentLayout.removeAllViews();

		ButtonAdapter adapter = new ButtonAdapter(context, arrayList);
//		adapter.notifyDataSetChanged();
//		adapter.setOnItemClickListener(onItemClickListener);
		adapter.setItemButtonClickListener(itemNewMessageButtonClickListener);
		listView.setAdapter(adapter);
		listView.setFocusable(false);
		contentLayout.addView(listViewLayout);
	}
	
	public void refreshNotifyList(ArrayList<Map<String,String>> arrayList) {
		contentLayout.removeAllViews();

		ButtonAdapter adapter = new ButtonAdapter(context, arrayList);
//		adapter.notifyDataSetChanged();
//		adapter.setOnItemClickListener(onItemClickListener);
		adapter.setItemButtonClickListener(itemNotifyButtonClickListener);
		listView.setAdapter(adapter);
		listView.setFocusable(false);
		contentLayout.addView(listViewLayout);
	}

	public void refreshHistory(ArrayList<String> arrayList) {
		contentLayout.removeAllViews();

		HistoryListAdapter adapter = new HistoryListAdapter(context, arrayList);
		adapter.notifyDataSetChanged();
//		adapter.setOnItemClickListener(onItemClickListener);
//		adapter.setItemButtonClickListener(itemButtonClickListener);
		listView.setAdapter(adapter);
		contentLayout.addView(listViewLayout);
	}
	
	public void refreshLeaveMessage(String leaveMessage){
		contentLayout.removeAllViews();
		LinearLayout layout = (LinearLayout) inflater.inflate(
				 R.layout.popwindow_leave_message, null);
		TextView tvLeaveMessage = (TextView) layout.findViewById(R.id.tv_leave_message);
		tvLeaveMessage.setText(leaveMessage);
		contentLayout.addView(layout);
	}

	/**
	 * 根据数据的条数，修改Button上的数子
	 * 
	 * @param btn
	 *            你修改的按钮
	 * @param number
	 *            Button按钮上心事的数字
	 */
	public void updateNewMessageTextNumber(int number) {
		if (number <= 0) {
			newMessageTv.setVisibility(View.INVISIBLE);
		} else {
			newMessageTv.setText(String.valueOf(number));
			newMessageTv.setVisibility(View.VISIBLE);
		}
	}
	
	public void updateNotifyTextNumber(int number) {
		if (number <= 0) {
			notifyTv.setVisibility(View.INVISIBLE);
		} else {
			notifyTv.setText(String.valueOf(number));
			notifyTv.setVisibility(View.VISIBLE);
		}
	}

	public OnClickListener getButtomOnClickListener() {
		return buttomOnClickListener;
	}

	public void setButtomOnClickListener(OnClickListener buttomOnClickListener) {
		this.buttomOnClickListener = buttomOnClickListener;
	}

//	public OnItemClickListener getOnItemClickListener() {
//		return onItemClickListener;
//	}

	/**
	 * 
	 * @param onItemClickListener
	 *            :listView中的每一项被点击触发的事件
	 */
//	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
//		this.onItemClickListener = onItemClickListener;
//	}

	

	/**
	 * 
	 * @param itemButtonClickListener
	 *            :listView中的每一子项的Button被点击触发的事件
	 */
	

	public void setLeaveMessageBackground(int resId){
		leaveMessageBtn.setBackgroundResource(resId);
	}
	
	

	public void setHistoryBtnBackground(int resId){
		historyBtn.setBackgroundResource(resId);
	}
	
	public void setNotifyBtnBackground(int resId){
		notifyBtn.setBackgroundResource(resId);
	}
	
	public void setNewMessageBtnBackground(int resId){
		newMessageBtn.setBackgroundResource(resId);
	}

	public OnItemClickListener getItemNotifyButtonClickListener() {
		return itemNotifyButtonClickListener;
	}

	public void setItemNotifyButtonClickListener(
			OnItemClickListener itemNotifyButtonClickListener) {
		this.itemNotifyButtonClickListener = itemNotifyButtonClickListener;
	}

	public OnItemClickListener getItemNewMessageButtonClickListener() {
		return itemNewMessageButtonClickListener;
	}

	public void setItemNewMessageButtonClickListener(
			OnItemClickListener itemNewMessageButtonClickListener) {
		this.itemNewMessageButtonClickListener = itemNewMessageButtonClickListener;
	}

	public TextView getNotifyTv() {
		return notifyTv;
	}


	public TextView getNewMessageTv() {
		return newMessageTv;
	}

}
