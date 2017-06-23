package com.chinamworld.bocmbci.biz.more;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.fidget.BTCConstant;
import com.chinamworld.bocmbci.fidget.BTCFidgetManager;
/**
 * 
 * 
 * 
 * 功能：关于手机银行页面
 * 
 * 作者：wuhan
 * 
 * 日期：2015年5月6日下午1:34:04
 * 
 * 引用JAR包：
 * 
 * 说明文档名字及位置：
 *
 */
public class MoreAboutBankActivity extends MoreBaseActivity implements OnClickListener{
	
	TextView tv_version;
	//客户服务24小时热线,信用卡24小时专线
	TextView tv_telenume,tv_credit_telenum;
	SharedPreferences app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.more_about_bank));
		addView(R.layout.more_about_bank);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		hineTitlebarLoginButton();
		tv_credit_telenum = (TextView) findViewById(R.id.tv_credit_telenum);
		tv_telenume = (TextView) findViewById(R.id.tv_telenume);
		tv_telenume.setMovementMethod(LinkMovementMethod.getInstance());
		tv_version = (TextView)this.findViewById(R.id.tv_version);
//		btn_right.setVisibility(View.INVISIBLE);
	}
	
	private void initData() {
		BTCFidgetManager.initParams(this);
		// 获取程序版本信息，fidget机构
		if (app == null) {
			app = this.getSharedPreferences(BTCConstant.APP_INFO,Activity.MODE_PRIVATE);
		}
		String version = app.getString("version",SystemConfig.APP_VERSION);
		tv_version.setText("V"+version+"");
	}

	private void initListener() {
		tv_credit_telenum.setOnClickListener(this);
		tv_telenume.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ib_back:
			this.finish();
			break;
		case R.id.tv_credit_telenum:
			String credit_telenum = tv_credit_telenum.getText().toString();
			Intent inten=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+credit_telenum));
			startActivity(inten);
			break;
		case R.id.tv_telenume:
			String phoneno = tv_telenume.getText().toString();
			Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneno));
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	
}
