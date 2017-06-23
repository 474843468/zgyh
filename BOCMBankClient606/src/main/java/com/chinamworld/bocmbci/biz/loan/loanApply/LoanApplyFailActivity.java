package com.chinamworld.bocmbci.biz.loan.loanApply;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.log.LogGloble;

/** 贷款申请--失败页面 */
public class LoanApplyFailActivity extends LoanBaseActivity {
	private static final String TAG = "LoanApplySuccessActivity";
	private View detailView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.loan_apply_loan_title));
		Button backButton = (Button) findViewById(R.id.ib_back);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ibBack.setVisibility(View.GONE);
		init();
	
	}
	private void init() {
		detailView = LayoutInflater.from(this).inflate(R.layout.loan_apply_fail, null);
		tabcontentView.addView(detailView);
       	
		Button loan_tradeButton1 = (Button) findViewById(R.id.loan_tradeButton1);
		
		loan_tradeButton1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				gotoActivity();
			}
		});
	
	}

	private void gotoActivity() {
		Intent intent = new Intent(LoanApplyFailActivity.this,
				LoanApplyChooseActivity.class);
		startActivity(intent);
		finish();
		ActivityTaskManager.getInstance().removeAllActivityInFirstChange();
	}
	/**
	 * 屏蔽返回键
	 * */
	@Override
	public void onBackPressed() {
	}

}
