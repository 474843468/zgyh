package com.chinamworld.bocmbci.biz.login.binding;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.audio.AudioKeyManager;
import com.chinamworld.bocmbci.biz.login.LoginBaseAcitivity;
import com.chinamworld.bocmbci.biz.login.binding.fragment.LoginBindMachineFragment;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 
 * 设备绑定宿主Activity
 * 
 * 登录后强制绑定设备
 * 
 * @author MeePwn
 * 
 */
public class LoginBindingActivity extends LoginBaseAcitivity implements
		OnClickListener {

	private Button leftBtn;
	private Button rightBtn;
	private TextView titleTv;

	private FrameLayout contentLayout;

	/**
	 * 安全工具设置页面传值，用于判断绑定完成后，返回页面 （其他不可用）
	 */
	public String type;
	
	/**
	 * 判断返回键的动作，是否回到主页面
	 */
	public String leftBtnClickType = "go_main";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hardware_binding_activity);
		titleTv = (TextView) findViewById(R.id.tv_title);
		setTitle(R.string.hardware_bind_machine);
		
		initDatas();
		initViews();
		setViews();
		switchFragment();
	}

	private void initDatas() {}

	private void initViews() {
		leftBtn = (Button) findViewById(R.id.ib_back);
		rightBtn = (Button) findViewById(R.id.ib_main);
		rightBtn.setText("退出");
		rightBtn.setVisibility(View.VISIBLE);
		contentLayout = (FrameLayout) findViewById(R.id.hardware_content_layout);
		findViewById(R.id.btn_show).setVisibility(View.GONE);
		findViewById(R.id.foot_layout).setVisibility(View.GONE);
		leftBtn.setVisibility(View.GONE);
	}

	private void setViews() {
		leftBtn.setOnClickListener(this);
		rightBtn.setOnClickListener(this);
		contentLayout.setOnClickListener(this);
	}
	
	public void setTitle(int resId){
		String title = getResources().getString(resId);
		titleTv.setText(title);
	}
	
	public void setLeftBtnGone(){
		leftBtn.setVisibility(View.GONE);
	}
	
	public void setLeftBtnVisible(){
		leftBtn.setVisibility(View.VISIBLE);
	}
	
	public void setRightBtnVisible(){
		rightBtn.setVisibility(View.VISIBLE);
	}
	
	public void setRightBtnGone() {
		rightBtn.setVisibility(View.GONE);
	}

	public void showFragment(Fragment fragment) {
		if (fragment == null) {
			//TODO 提示用户无法绑定 
			BaseDroidApp.getInstanse().showMessageDialog(getResources().getString(R.string.hardware_unbind_can_not_unbind_info),
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							BaseDroidApp.getInstanse()
									.dismissMessageDialog();
							finish();
						}
					});
			return;
		}
		Bundle bundle = new Bundle();
		bundle.putString("is_open_activated", type);
		fragment.setArguments(bundle);

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.add(R.id.hardware_content_layout, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			if ("go_main".equals(leftBtnClickType)) {
				finish();
			}else{				
				leftBtnClickType = "go_main";
				setLeftBtnGone();
				getSupportFragmentManager().popBackStack();
			}
			break;
		case R.id.ib_main:
			backPressed();
			break;

		default:
			break;
		}
	}
	
	private void switchFragment(){
		Fragment fragment = new LoginBindMachineFragment();
		showFragment(fragment);
	}
	
	@Override
	public void onBackPressed() {
		backPressed();
//		super.onBackPressed();
	}
	
	private void backPressed(){
		String message = LoginBindingActivity.this.getResources().getString(R.string.exit_confirm);
		BaseDroidApp.getInstanse().showErrorDialog(null, message, new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseDroidApp.getInstanse().dismissErrorDialog();
				switch ((Integer) v.getTag()) {
				case CustomDialog.TAG_SURE:// 确定
					// P501音频key
					boolean connected = AudioKeyManager.getInstance().isConnected();
					if (connected) {
						BaseDroidApp.getInstanse().showErrorDialog(getString(R.string.safe_exit_device_tips),R.string.cancle,R.string.quit, 
								new View.OnClickListener() {
									
									@Override
									public void onClick(View v) {
										switch ((Integer) v.getTag()) {
										case CustomDialog.TAG_CANCLE:
											BaseDroidApp.getInstanse().dismissMessageDialog();
											break;
										case CustomDialog.TAG_SURE:
											// 发送通讯请求退出
											requestForLogout();
											CustomDialog.toastInCenter(LoginBindingActivity.this, getString(R.string.logoutsucess));
											BaseDroidApp.getInstanse().clientLogOut();
											ActivityTaskManager.getInstance().removeAllActivity();
											ActivityTaskManager.getInstance().removeAllSecondActivity();
											break;
										}
									}
								});
					} else {
						// 发送通讯请求退出
						requestForLogout();
						CustomDialog.toastInCenter(LoginBindingActivity.this, getString(R.string.logoutsucess));
						BaseDroidApp.getInstanse().clientLogOut();
						ActivityTaskManager.getInstance().removeAllActivity();
						ActivityTaskManager.getInstance().removeAllSecondActivity();
						
					}
				}
			}
		});
	}
	
}
