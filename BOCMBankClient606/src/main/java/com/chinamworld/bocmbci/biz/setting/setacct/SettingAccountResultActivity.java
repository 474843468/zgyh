package com.chinamworld.bocmbci.biz.setting.setacct;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.setting.SettingBaseActivity;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 设置默认账户成功页
 * 
 * @author panwe
 * 
 */
public class SettingAccountResultActivity extends SettingBaseActivity {
	
	private String mobile;
	private String acctNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getIntentData();
		initViews();
	}
	
	private void getIntentData(){
		mobile = getIntent().getStringExtra(Setting.MOBILE);
		acctNum = getIntent().getStringExtra(Comm.ACCOUNTNUMBER);
	}

	private void initViews() {
		View mMainView = View.inflate(this, R.layout.setting_account_result, null);
		setTitle(R.string.setting_title);
		back.setVisibility(View.GONE);
		tabcontent.addView(mMainView);
		((TextView) mMainView.findViewById(R.id.mobile)).setText(mobile);
		((TextView) mMainView.findViewById(R.id.account)).setText(StringUtil.getForSixForString(acctNum));
	}
	
	/**
	 * 完成
	 * @param v
	 */
	public void btnFinishOnclick(View v){
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
}
