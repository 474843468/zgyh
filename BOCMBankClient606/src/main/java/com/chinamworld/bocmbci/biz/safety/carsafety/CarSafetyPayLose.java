package com.chinamworld.bocmbci.biz.safety.carsafety;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.safety.SafetyBaseActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.safetyhold.SafetyHoldProductQueryActivity;
import com.chinamworld.bocmbci.biz.safety.safetyproduct.SafetyProductListActivity;
import com.chinamworld.bocmbci.biz.safety.safetytemp.SafetyTempProductListActivity;

/**
 * 缴费失败界面
 * 
 * @author Zhi
 */
public class CarSafetyPayLose extends SafetyBaseActivity {

	/** 主显示视图 */
	private View mMainView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.safety_msgfill_title);
		mLeftButton.setVisibility(View.GONE);
		mMainView = addView(R.layout.safety_carsafety_paysubmit_lose);
		mMainView.findViewById(R.id.btnConfirm).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent();
				SafetyDataCenter.getInstance().clearAllData();
				setResult(4);
				finish();
			}
		});
	}
	
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			intent();
			SafetyDataCenter.getInstance().clearAllData();
			setResult(4);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	};
	
	/** 跳转逻辑，判断投保流程、暂存单继续投保流程、持有保单续保流程成功后跳转页面，在清空数据中心数据之前调用 */
	private void intent() {
		if (SafetyDataCenter.getInstance().isSaveToThere) {
			startActivity(new Intent(CarSafetyPayLose.this, SafetyTempProductListActivity.class));
		} else if (SafetyDataCenter.getInstance().isHoldToThere) {
			startActivity(new Intent(CarSafetyPayLose.this, SafetyHoldProductQueryActivity.class));
		} else {
			startActivity(new Intent(CarSafetyPayLose.this, SafetyProductListActivity.class));
		}
	}
}
