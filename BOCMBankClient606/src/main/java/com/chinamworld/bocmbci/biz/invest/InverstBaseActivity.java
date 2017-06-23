package com.chinamworld.bocmbci.biz.invest;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.setting.SettingBaseActivity;

/**
 * 投资理财服务基类
 * 
 * @author xbybaoying
 * 
 *         2013-5-16
 */
public class InverstBaseActivity extends SettingBaseActivity {
	/** 主视图布局 */
	protected LinearLayout tabcontent,tabcontentTwo;// 主Activity显示
	/** 左侧返回按钮 */
	protected View back;
	/** 右侧按钮点击事件 */
	protected OnClickListener rightBtnClick;
	/** 右上角按钮 */
	public Button btn_right;
	public LayoutInflater mInflater;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置上页面的外层
		setContentView(R.layout.biz_activity_layout);
		// 初始化弹窗按钮
		initPulldownBtn();
//		if(getIntent().getBooleanExtra(InvestConstant.FROMMYSELF, false)){//从投资理财自己模块进来的
		
			// 初始化底部菜单栏
			initFootMenu();
//		}else{//其他模块跳转过来的
//			(findViewById(R.id.foot_layout)).setVisibility(View.GONE);
//		}
			
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		tabcontentTwo = (LinearLayout) this.findViewById(R.id.sliding_bodytwo);
		tabcontent.setPadding(0, 0, 0, 0);
		btn_right = (Button) findViewById(R.id.ib_top_right_btn);
		back =  findViewById(R.id.ib_back);
		back.setVisibility(View.GONE);
		btn_right.setVisibility(View.GONE);
		// 左上角返回点击事件
		clickTopLeftClick();
		mInflater = LayoutInflater.from(this);
		goneLeftView();
		
	}

	/** 左上角返回点击事件 */
	public void clickTopLeftClick() {
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	
	/**
	 * @param rightBtnClick
	 *            the rightBtnClick to set
	 */
	public void setRightBtnClick(OnClickListener rightBtnClick) {
		this.rightBtnClick = rightBtnClick;
	}
	
	/**
	 * 设置右侧按钮文字
	 * 
	 * @param title
	 */
	public void setText(String title) {
		btn_right.setVisibility(View.GONE);
		btn_right.setText(title);
		btn_right.setTextColor(Color.WHITE);
		btn_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (rightBtnClick != null) {
					rightBtnClick.onClick(v);
				}
			}
		});
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
//	/**
//	 * 隐藏左侧菜单栏
//	 */
//	public void goneLeftView(){
//		//隐藏左侧菜单
//				LinearLayout slidingTab = (LinearLayout) findViewById(R.id.sliding_tab);
//				Button btn_show = (Button) findViewById(R.id.btn_show);
//				Button btn_hide = (Button) findViewById(R.id.btn_hide);
//				Button btn_fill_show = (Button) findViewById(R.id.btn_fill_show);
//				slidingTab.setVisibility(View.GONE);
//				btn_show.setVisibility(View.GONE);
//				btn_hide.setVisibility(View.GONE);
//				btn_fill_show.setVisibility(View.GONE);
//	}


}
