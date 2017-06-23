package com.chinamworld.bocmbci.biz.remittance;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import com.chinamworld.bocmbci.R;

public class RemittanceMustKnowActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remittance_must_know);
		
		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		android.view.WindowManager.LayoutParams p = getWindow().getAttributes(); 
		p.height = (int) (d.getHeight() * 0.8); // 高度设置为屏幕的0.8
//		p.width = (int) (d.getWidth() * 0.7); // 宽度设置为屏幕的0.7
		getWindow().setAttributes(p);
		
		findViewById(R.id.btnConfirm).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RemittanceContent.RESULT_CODE_MUSTKNOW_RESULT);
				finish();
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RemittanceContent.RESULT_CODE_MUSTKNOW_RESULT);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
