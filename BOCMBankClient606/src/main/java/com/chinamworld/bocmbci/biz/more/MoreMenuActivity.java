
package com.chinamworld.bocmbci.biz.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.infoserve.InfoServeMainActivity;
import com.chinamworld.bocmbci.biz.servicerecord.MoreApp;
import com.chinamworld.bocmbci.biz.servicerecord.ServiceRecordQueryActivity;
import com.chinamworld.bocmbci.fidget.BTCFidgetManager;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.LoginTask.LoginCallback;

/**
 * 
 * 
 * 
 * 功能：更多按钮的首页
 * 
 * 作者：wuhan
 * 
 * 日期：2015年5月5日下午5:13:04
 * 
 * 引用JAR包：
 * 
 * 说明文档名字及位置：
 * 
 */
public class MoreMenuActivity extends MoreBaseActivity implements
		OnClickListener {

	private static final int REQUEST_LOGIN_CODE = 10001;
	/**
	 * 消息服务，关于手机银行，检测新版本
	 */
	private RelativeLayout rl_info_service, rl_about_bank, rl_check_version,service_record,more_app;
	// 消息服务还要写
	private TextView tv_prompt;
	
	int vipMessageCount,newMessageCount,totalMessage;
	//判断是否登陆
	private boolean isLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.more));
		
		addView(R.layout.more_menu_firstpage);
		initView();
		initData();
		initListener();
	}
	

	private void initView() {
		hineTitlebarLoginButton();
		isLogin=BaseDroidApp.getInstanse().isLogin();
		tv_prompt = (TextView) findViewById(R.id.tv_prompt);
		rl_about_bank = (RelativeLayout) this.findViewById(R.id.rl_about_bank);
		rl_info_service = (RelativeLayout) this.findViewById(R.id.rl_info_service);
		rl_check_version = (RelativeLayout) this.findViewById(R.id.rl_check_version);
		service_record=(RelativeLayout)this.findViewById(R.id.service_record);
		more_app=(RelativeLayout) this.findViewById(R.id.more_app);
//		btn_right.setVisibility(View.INVISIBLE);/
	}

	private void initData() {
		Intent intent = getIntent();
		vipMessageCount =intent.getIntExtra("vipMessageCount", 0);
		newMessageCount = intent.getIntExtra("newMessageCount", 0);
		totalMessage = intent.getIntExtra("totalMessage", 0);
		if(totalMessage!=0){
			tv_prompt.setVisibility(View.VISIBLE);
//			tv_prompt.setText(vipMessageCount+newMessageCount+"");
			tv_prompt.setText(totalMessage+"");
		}else{
			tv_prompt.setVisibility(View.INVISIBLE);
		}

	}

	private void initListener() {
		this.tv_prompt.setOnClickListener(this);
		this.rl_about_bank.setOnClickListener(this);
		this.rl_check_version.setOnClickListener(this);
		this.rl_info_service.setOnClickListener(this);
		this.service_record.setOnClickListener(this);
		this.more_app.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.ib_back:
			this.finish();
			break;
		case R.id.rl_about_bank:
			Intent intent1 = new Intent(MoreMenuActivity.this,MoreAboutBankActivity.class);
			startActivity(intent1);
			break;
		case R.id.rl_info_service:
			tv_prompt.setVisibility(View.INVISIBLE);
			Intent in = new Intent(MoreMenuActivity.this,InfoServeMainActivity.class);
			in.putExtra("vipMessageCount", vipMessageCount);
			in.putExtra("newMessageCount", newMessageCount);
			startActivity(in);
			vipMessageCount =0;
			newMessageCount = 0;
			totalMessage= 0;
			
			break;
		case R.id.rl_check_version:
			// 检测版本是否需要更新

				try {
					BTCFidgetManager.init(this);
				} catch (InterruptedException e) {
					LogGloble.exceptionPrint(e);
				}

			break;
		case R.id.tv_prompt:
			tv_prompt.setVisibility(View.INVISIBLE);
			break;
		case R.id.service_record:
			
			if(isLogin){
				Intent intent=new Intent(MoreMenuActivity.this,ServiceRecordQueryActivity.class);
				startActivity(intent);
			}else {
//				Intent intent = new Intent();
//				intent.setClass(MoreMenuActivity.this,
//						LoginActivity.class);
//				startActivityForResult(intent, REQUEST_LOGIN_CODE);
				BaseActivity.getLoginUtils(MoreMenuActivity.this).exe(new LoginCallback() {
					
					@Override
					public void loginStatua(boolean isLogin) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(MoreMenuActivity.this, ServiceRecordQueryActivity.class);
						startActivity(intent);
						finish();
					}
				});
			}
			break;
		case R.id.more_app:
			Intent intent=new Intent(MoreMenuActivity.this, MoreApp.class);
			startActivity(intent);
		default:
			break;
		}

	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		isLogin=BaseDroidApp.getInstanse().isLogin();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_LOGIN_CODE:
			switch (resultCode) {
			case RESULT_OK:
				Intent intent=new Intent(MoreMenuActivity.this,ServiceRecordQueryActivity.class);
				startActivity(intent);
				break;
			default:
				break;
			}
			break;

		default:
			break;
		}
		
	}
}

