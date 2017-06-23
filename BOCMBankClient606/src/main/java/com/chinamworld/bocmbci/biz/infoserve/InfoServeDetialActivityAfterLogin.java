package com.chinamworld.bocmbci.biz.infoserve;

import java.util.ArrayList;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.infoserve.bean.NeedReadMessage;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;

public class InfoServeDetialActivityAfterLogin extends InfoServeBaseActivity {
	Button btnRight;
	private ArrayList<NeedReadMessage> needReadMessageList;
	private TextView tv_subject;
	private TextView tv_content;
	private Button btu_before;
	private Button btu_next;
	private Button btu_sure;
	private int message_size;
	private int message_index;
	private LinearLayout layout_before;
	private LinearLayout layout_next;
	private LinearLayout layout_sure;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 只支持竖屏
//		setContentView(R.layout.main_activity2);
		// 初始化弹出框
		initPulldownBtn();
		// 初始化底部菜单栏
		initFootMenu();
		initMainUI();
		int resource=R.layout.activity_info_serve_detial_activity_after_login;
		addView(resource);
		initUI();//初始化该页面内容组件
		addInfoServeMessage();//展示必读消息
		
	}
	@SuppressWarnings("deprecation")
	private void initMainUI(){
		Button ib_top_right_btn = (Button) findViewById(R.id.ib_top_right_btn);
		Button ib_back = (Button) findViewById(R.id.ib_back);
		Button ib_top_right_btn_b = (Button) findViewById(R.id.ib_top_right_btn_b);//关闭
		ib_top_right_btn_b.setVisibility(View.GONE);
		ib_back.setVisibility(View.GONE);
		LayoutParams rpParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		rpParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rpParams.addRule(RelativeLayout.CENTER_VERTICAL);
		rpParams.setMargins(0, 0, 15, 0);
		ib_top_right_btn.setLayoutParams(rpParams);
		ib_top_right_btn.setVisibility(View.VISIBLE);
		ib_top_right_btn.setText("关闭");
		ib_top_right_btn.setOnClickListener(clickListener);
		setTitle("消息详情");
	}
	/**
	 * 初始化该页面内容组件
	 * @param resource
	 */
	private void initUI(){
		tv_subject = (TextView) findViewById(R.id.tv_subject);
		tv_content = (TextView) findViewById(R.id.tv_content);
		btu_before = (Button) findViewById(R.id.btu_before);
		btu_next = (Button) findViewById(R.id.btu_next);
		btu_sure = (Button) findViewById(R.id.btu_sure);
		layout_before = (LinearLayout) findViewById(R.id.layout_before);
		layout_next = (LinearLayout) findViewById(R.id.layout_next);
		layout_sure = (LinearLayout) findViewById(R.id.layout_sure);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_subject);
		btu_before.setOnClickListener(clickListener);
		btu_next.setOnClickListener(clickListener);
		btu_sure.setOnClickListener(clickListener);
	}
	private OnClickListener clickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btu_before:
					addInfoServeMessageByIndex(message_index,-1);
				break;
			case R.id.btu_next:
					addInfoServeMessageByIndex(message_index,1);
				break;
			case R.id.ib_top_right_btn:{
				setResult(RESULT_OK);
				finish();
				break;
				}
			case R.id.btu_sure:
				setResult(RESULT_OK);
				finish();
				break;

			default:
				break;
			}
		}
	};
	
	/**
	 * 获取必读消息列表
	 * @return
	 */
	private boolean initMessages(){
		needReadMessageList = (ArrayList<NeedReadMessage>) InfoServeDataCenter.getInstance().getmNeedReadMessageList();
		if (needReadMessageList.size()==0) {
			return false;
		}
		
		return true;
	}
	/**
	 * 展示必读消息
	 */
	private void addInfoServeMessage(){
		if (!initMessages()) {
			return;
		}
		message_size = needReadMessageList.size();
		switch (message_size) {
		case 1:{
			layout_before.setVisibility(View.GONE);
			layout_next.setVisibility(View.GONE);
			addFistInfoServeMessage();
			break;}

		default:
			layout_before.setVisibility(View.GONE);
			layout_sure.setVisibility(View.GONE);
			addFistInfoServeMessage();
			break;
		}
	}
	/**
	 * 展示第一条信息
	 */
	private void addFistInfoServeMessage(){
		message_index = 0;
		NeedReadMessage needReadMessage = needReadMessageList.get(0);
		tv_subject.setText(needReadMessage.getSubject());
		tv_content.setText(needReadMessage.getContent());
	}
	/**
	 * 展示上一条或下一条信息
	 * @param index
	 * @param increment 增量
	 */
	private void addInfoServeMessageByIndex(int index,int increment){
		if (0<=index+increment&&index+increment<=message_size-1) {
			message_index=index+increment;
			NeedReadMessage needReadMessage = needReadMessageList.get(message_index);
			tv_subject.setText(needReadMessage.getSubject());
			tv_content.setText(needReadMessage.getContent());
			if (index<index+increment) {
				layout_before.setVisibility(View.VISIBLE);
			}else {
				layout_next.setVisibility(View.VISIBLE);
			}
		}
		if (message_index==0) {
			layout_before.setVisibility(View.GONE);
			layout_next.setVisibility(View.VISIBLE);
		}else if (message_index==message_size-1) {
			layout_before.setVisibility(View.VISIBLE);
			layout_next.setVisibility(View.GONE);
		}else {
			layout_sure.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onBackPressed() {
		 return;
	}
	

}
