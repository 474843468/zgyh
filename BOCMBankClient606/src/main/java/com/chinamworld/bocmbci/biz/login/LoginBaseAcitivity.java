package com.chinamworld.bocmbci.biz.login;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;

public class LoginBaseAcitivity extends LoginTopBaseActivity {
	/** 左侧菜单 */
	private Button showBtn;
	/** 返回按钮 */
	protected Button ibBack;
	
	protected LinearLayout tabcontent;// 主Activity显示

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		initPulldownBtn(); // 加载上边下拉菜单
		initFootMenu(); // 加载底部菜单栏
		
		
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		// 隐藏左侧菜单
		showBtn = (Button) this.findViewById(R.id.btn_show);
		showBtn.setVisibility(View.GONE);
		((Button)this.findViewById(R.id.ib_top_right_btn)).setVisibility(View.GONE);

		ibBack = (Button) this.findViewById(R.id.ib_back);
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
