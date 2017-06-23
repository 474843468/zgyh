package com.chinamworld.bocmbci.fidget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
/**
 * 飞聚基类
 * @author  xbybaoying
 *
 * 2013-5-14
 */
public class FidgetBaseActiviy extends BaseActivity {
	/** 主视图布局 */
	protected LinearLayout tabcontent;// 主Activity显示
	/** 左侧返回按钮 */
	protected Button back;
	/** 右上角按钮 */
	protected Button btn_right;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		// 初始化弹窗按钮
		initPulldownBtn();
		// 初始化底部菜单栏
		initFootMenu();
		 Button btn_show = (Button) findViewById(R.id.btn_show);
		 btn_show.setVisibility(View.GONE);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
	
		btn_right = (Button) findViewById(R.id.ib_top_right_btn);
		back = (Button) findViewById(R.id.ib_back);
		
		
	}
	/**
	 * 在slidingbody中引入自己布局文件
	 * 
	 * @param resource
	 * @return 引入布局
	 */
	public View addView(int resource) {
		View view = LayoutInflater.from(this).inflate(resource, null);
		tabcontent.addView(view);
		return view;
	}
	@Override
	public ActivityTaskType getActivityTaskType() {
		return ActivityTaskType.ThreeTask;
	}
	
	
}
