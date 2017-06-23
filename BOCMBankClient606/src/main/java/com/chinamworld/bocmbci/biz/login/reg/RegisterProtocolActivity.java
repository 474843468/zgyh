package com.chinamworld.bocmbci.biz.login.reg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.login.LoginBaseAcitivity;

/**
 * 约定转存协议 用户选择接受或不接受
 * 
 * @author wjp
 * 
 */
public class RegisterProtocolActivity extends LoginBaseAcitivity {
//	private static final String TAG = "RegisterProtocolActivity";
	private View view = null;
	/**
	 * quickTrade：关闭
	 */
	private Button quickTrade = null;

	/**
	 * agreeButton：接受按钮
	 */
	private Button agreeButton = null;
	/**
	 * noAgreeButton：不接受按钮
	 */
	private Button noAgreeButton = null;
	
	

	/** 底部layout */
	private LinearLayout footLayout;
	/** 左侧菜单 */
	private Button showBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.self_reg_title);

		LayoutInflater inflater = LayoutInflater.from(this); // 加载理财产品布局
		view = inflater.inflate(R.layout.register_protocol_info, null);
		tabcontent.addView(view);

		// 隐藏底部菜单
		footLayout = (LinearLayout) this.findViewById(R.id.foot_layout);
		footLayout.setVisibility(View.GONE);
		// 隐藏左侧菜单
		showBtn = (Button) this.findViewById(R.id.btn_show);
		showBtn.setVisibility(View.GONE);

		init();
	}

	/**
	 * 初始化所有的控件
	 */
	private void init() {
		ibBack.setVisibility(View.GONE);

		quickTrade = (Button) findViewById(R.id.ib_top_right_btn);
		quickTrade.setVisibility(View.VISIBLE);
		quickTrade.setText(this.getResources().getString(R.string.close));
		
		agreeButton = (Button) findViewById(R.id.btnNo);
		noAgreeButton = (Button) findViewById(R.id.btnYes);
		// 关闭按钮事件
		quickTrade.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 返回到跳转页面
				finish();
			}
		});
		// 不接受按钮事件
		noAgreeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 返回到外汇行情页面
				setResult(101);
				finish();

			}
		});
		// 接受按钮事件
		agreeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(100);
				finish();
			}
		});
	}

}
