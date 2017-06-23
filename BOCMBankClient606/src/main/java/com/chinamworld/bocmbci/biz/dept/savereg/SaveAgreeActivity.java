package com.chinamworld.bocmbci.biz.dept.savereg;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.log.LogGloble;

/**
 * 约定转存协议 用户选择接受或不接受
 * 
 * @author wjp
 * 
 */
public class SaveAgreeActivity extends DeptBaseActivity {
	private static final String TAG = "SaveAgreeActivity";
	private LinearLayout tabcontent;// 主Activity显示
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
		LogGloble.d(TAG, "onCreate");
		setTitle(R.string.read_pro);

		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		LayoutInflater inflater = LayoutInflater.from(this); // 加载理财产品布局
		view = inflater.inflate(R.layout.dept_saveagree_info, null);
		tabcontent.addView(view);
		
		newTranBtn = (Button) this.findViewById(R.id.ib_top_right_btn);
		newTranBtn.setText(this.getResources().getString(R.string.new_save));

		newTranBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
						SaveRegularActivity.class);
				startActivity(intent);
				finish();
			}
		});
		// 隐藏底部菜单
		footLayout = (LinearLayout) this.findViewById(R.id.foot_layout);
		footLayout.setVisibility(View.GONE);
		// 隐藏左侧菜单
		showBtn = (Button) this.findViewById(R.id.btn_show);
		showBtn.setVisibility(View.GONE);
		setLeftButtonPopupGone();
		
		init();
	}

	/**
	 * 初始化所有的控件
	 */
	private void init() {
		ibBack.setVisibility(View.GONE);

		TextView titleTv = (TextView) findViewById(R.id.contract_title_tv);
		titleTv.setText(this.getResources().getString(R.string.agree_message));
		TextView firstTv = (TextView) findViewById(R.id.dept_first_tv);
		Map<String,Object> data = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA);
		String userName = (String) data.get(Login.CUSTOMER_NAME);
		firstTv.setText(userName);
		

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
