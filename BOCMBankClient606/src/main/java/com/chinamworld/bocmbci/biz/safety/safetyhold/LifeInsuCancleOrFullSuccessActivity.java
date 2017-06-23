package com.chinamworld.bocmbci.biz.safety.safetyhold;

import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.safety.safetyproduct.lifelnsurance.LifeInsuranceBaseActivity;
import com.chinamworld.bocmbci.utils.StringUtil;

public class LifeInsuCancleOrFullSuccessActivity extends LifeInsuranceBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainView = addView(R.layout.safety_hold_life_cancleorfull_success);
		setTitle(R.string.safety_lifeInsurance_hold_cancleFinish);
		findView();
		viewSet();
	}
	
	@Override
	protected void findView() {
		
	}

	@Override
	protected void viewSet() {
		mLeftButton.setVisibility(View.GONE);
		((TextView) mMainView.findViewById(R.id.tv_tip)).setText(Html.fromHtml("此保单返还金额 <font color=\"#ba001d\">" + StringUtil.parseStringPattern(getIntent().getStringExtra("backPrem"), 2) + "</font> 元，请在15个工作日后 查询到账情况。（保费到账时间需要跟各家保险公司商定）"));
		mMainView.findViewById(R.id.btn_finish).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});
	}

	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			SafetyDataCenter.getInstance().clearAllData();
//			setResult(RESULT_OK);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	};
}
