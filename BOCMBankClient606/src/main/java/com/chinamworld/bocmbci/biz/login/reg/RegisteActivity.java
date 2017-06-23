package com.chinamworld.bocmbci.biz.login.reg;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.login.LoginBaseAcitivity;

/**
 * 注册---注册条款
 * 
 * @author WJP
 * 
 */
public class RegisteActivity extends LoginBaseAcitivity {
	
	private static final String TAG = "RegisteActivity";
	
	private Button confirmBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置标题
		setTitle(R.string.self_reg_title);
		
		View view = LayoutInflater.from(this).inflate(R.layout.registe_activity, null);
		tabcontent.addView(view);
		
		confirmBtn = (Button) findViewById(R.id.findpwd_btn_conf);
		confirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(RegisteActivity.this,
						RegisteVerifyActivity.class);
				startActivity(intent);
			}
		});
		
	}
	

}
