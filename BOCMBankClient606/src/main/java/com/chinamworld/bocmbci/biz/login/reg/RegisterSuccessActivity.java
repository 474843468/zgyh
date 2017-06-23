package com.chinamworld.bocmbci.biz.login.reg;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.bocnet.BocnetDataCenter;
import com.chinamworld.bocmbci.biz.login.LoginBaseAcitivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.StepTitleUtils;

/**
 * 注册---注册成功
 * 
 * @author WJP
 * 
 */
public class RegisterSuccessActivity extends LoginBaseAcitivity {

	/** 注册信息 */
	private TextView registerInfoTv;
	/** 注册客户号 */
	private TextView registerNumTv;
	/** 用户名 */
	private TextView registerNameTv;
	/** 注册日期 */
	private TextView registerDateTv;
	/** 确定 */
	private Button confirmBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle(R.string.self_reg_title);

		View view = LayoutInflater.from(this).inflate(R.layout.register_success_activity, null);
		tabcontent.addView(view);

		StepTitleUtils.getInstance().initTitldStep(this, new String[] { this.getResources().getString(R.string.title_step1), this.getResources().getString(R.string.register_step2), this.getResources().getString(R.string.title_step3) });
		StepTitleUtils.getInstance().setTitleStep(3);

		//注册用户姓名
		String userName = this.getIntent().getStringExtra(Login.LOGIN_NAME);
		registerInfoTv = (TextView) findViewById(R.id.tv_register_info);
	
		Map<String, Object> result = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.REGISTER_VERIFY_CALLBACK_KEY);

//		String gender = LocalData.gender.get((String) result.get(Login.GENDER));
//		if (gender == null) { //性别 默认为男
//			gender = LocalData.gender.get(ConstantGloble.GENDER_MAN);
//		}
		
		StringBuffer successMsg = new StringBuffer("尊敬的 ");
		successMsg.append(userName).append(" ").append("，恭喜您成功注册为中国银行手机银行用户");
//		successMsg.append(userName).append(" ").append(gender).append("，恭喜您成功注册为中国银行手机银行用户");
		
		registerInfoTv.setText(successMsg);

		String registerNum = this.getIntent().getStringExtra(Login.REGISTER_MOBILE);
		registerNumTv = (TextView) findViewById(R.id.tv_register_number);
		registerNumTv.setText(registerNum);

//		String loginName = this.getIntent().getStringExtra(Login.LOGIN_NAME);
//		registerNameTv = (TextView) findViewById(R.id.tv_register_name);
//		registerNameTv.setText(loginName);
//
//		String registerDate = this.getIntent().getStringExtra(Login.REGISTER_DATE);
//		registerDateTv = (TextView) findViewById(R.id.tv_register_date);
//		registerDateTv.setText(registerDate);

		confirmBtn = (Button) findViewById(R.id.register_btn_conf);
		confirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isBocnet = getIntent().getBooleanExtra(BocnetDataCenter.ModleName, false);
				if(isBocnet){
//					startActivity(new Intent(RegisterSuccessActivity.this, LoginActivity.class));
					BaseActivity.getLoginUtils(RegisterSuccessActivity.this).exe(new LoginTask.LoginCallback() {

						@Override
						public void loginStatua(boolean isLogin) {

						}
					});
					ActivityTaskManager.getInstance().removeAllActivity();
				}else{finish();}
			}
		});

		ibBack.setVisibility(View.GONE);
	}

	@Override
	public void finish() {
		setResult(REGIST_SUCCESS);
		super.finish();
	}
	
	
}
