package com.chinamworld.bocmbci.biz.dept.largecd.sign;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;

/**
 * 大额存单 未签约 无符合要求
 * @author luqp 2016年1月8日17:16:38
 */
public class LargeSignNoAccordActivity extends DeptBaseActivity {
	private Context context = this;
	/** 加载布局 */
	private LayoutInflater inflater = null;
	private LinearLayout tabcontent;// 主Activity显示
	private View view = null;
	
	/** 未签约不能自动关联页面 */
	private LinearLayout noAccord = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.large_cd_sign_title));

		inflater = LayoutInflater.from(this);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		LayoutInflater inflater = LayoutInflater.from(this); // 加载布局
		view = inflater.inflate(R.layout.large_sign_not_accord, null);
		tabcontent.addView(view);
		// setLeftSelectedPosition(LARGE_CD_MENU); // 设置侧边栏
		
		noAccord = (LinearLayout) view.findViewById(R.id.ll_no_accord);
		noAccord.setVisibility(View.VISIBLE);
	}
}
