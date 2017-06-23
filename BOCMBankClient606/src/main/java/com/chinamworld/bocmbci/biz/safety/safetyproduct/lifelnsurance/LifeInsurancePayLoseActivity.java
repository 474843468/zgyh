package com.chinamworld.bocmbci.biz.safety.safetyproduct.lifelnsurance;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyConstant;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.safetyhold.LifeInsuCancleOrFullActivity;
import com.chinamworld.bocmbci.biz.safety.safetyhold.SafetyHoldProductQueryActivity;
import com.chinamworld.bocmbci.biz.safety.safetyproduct.SafetyProductListActivity;

public class LifeInsurancePayLoseActivity extends LifeInsuranceBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainView = addView(R.layout.safety_life_product_pay_lose);
		setTitle(getString(R.string.safety_msgfill_title));
		findView();
		viewSet();
	}
	
	@Override
	protected void findView() {

	}

	@Override
	protected void viewSet() {
		mLeftButton.setVisibility(View.GONE);
		TextView tip = (TextView) mMainView.findViewById(R.id.tv_tip);
		TextView loseInfo = (TextView) mMainView.findViewById(R.id.tv_loseInfo);
		String jumpFlag = getIntent().getStringExtra("jumpFlag");
		if (LifeInsurancePaySubmitActivity.TAG.equals(jumpFlag)) {
			setTitle(R.string.safety_msgfill_title);
			tip.setText("尊敬的客户，您的投保业务失败。");
		} else if (LifeInsuCancleOrFullActivity.TAG.equals(jumpFlag)) {
			setTitle(R.string.safety_lifeInsurance_hold_cancleFinish);
			tip.setText("尊敬的客户，您的退保/满期给付业务失败。");
		}
		loseInfo.setText(getIntent().getStringExtra(Safety.ERRORMSG));
		
		mMainView.findViewById(R.id.btnFinish).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();

				String jumpFlag = getIntent().getStringExtra("jumpFlag");
				if (LifeInsurancePaySubmitActivity.TAG.equals(jumpFlag)) {
					intent.setClass(LifeInsurancePayLoseActivity.this, SafetyProductListActivity.class);
				} else if (LifeInsuCancleOrFullActivity.TAG.equals(jumpFlag)) {
					intent.setClass(LifeInsurancePayLoseActivity.this, SafetyHoldProductQueryActivity.class);
				}
				startActivity(intent);
				SafetyDataCenter.getInstance().clearAllData();
				setResult(SafetyConstant.QUIT_RESULT_CODE);
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
