package com.chinamworld.bocmbci.biz.login.findpwd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.login.LoginBaseAcitivity;
import com.chinamworld.bocmbci.utils.StepTitleUtils;

/**
 * 找回密码---设置密码成功
 * 
 * @author WJP
 * 
 */
public class PwdResetSuccessActivity extends LoginBaseAcitivity {

	private static final String TAG = "PwdResetSuccessActivity";
	/** 确定按钮 */
	private Button comfirmBtn;

	/** 返回*/
	private Button ibBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle(R.string.retrieve_pwd_title);
		
		View view = LayoutInflater.from(this).inflate(R.layout.findpwd_resetpwd_success_activity, null);
		tabcontent.addView(view);

		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources().getString(R.string.title_step1),
						this.getResources().getString(R.string.findpwd_step2),
						this.getResources().getString(R.string.title_step3) });
		StepTitleUtils.getInstance().setTitleStep(3);

		comfirmBtn = (Button) findViewById(R.id.findpwd_btn_conf);
		comfirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(PwdResetSuccessActivity.this,
//						LoginActivity.class);
//				startActivity(intent);
				finish();
			}
		});
		

		ibBack = (Button) findViewById(R.id.ib_back);
		ibBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		ibBack.setVisibility(View.GONE);
		

	}

	@Override
	public void finish() {
		setResult(UN_FORGET_PWD_SUCCESS);
		super.finish();
	}
}
