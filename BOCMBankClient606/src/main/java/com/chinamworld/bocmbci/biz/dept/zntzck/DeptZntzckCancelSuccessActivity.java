package com.chinamworld.bocmbci.biz.dept.zntzck;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.log.LogGloble;

/** 智能通知存款---解约成功页面 */
public class DeptZntzckCancelSuccessActivity extends DeptBaseActivity {
	private static final String TAG = "DeptZntzckCancelConfirmActivity";
	private LinearLayout tabcontent;// 主Activity显示
	private View queryView = null;
	private TextView signAccText = null;
	private String accountNumber = null;
	private Button sureButton = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		ibRight.setVisibility(View.VISIBLE);
		ibBack.setVisibility(View.GONE);
		setTitle(getResources().getString(R.string.dept_zntzck_query_cancel));
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		queryView = LayoutInflater.from(this).inflate(R.layout.dept_zntzck_cancel_success, null);
		tabcontent.addView(queryView);
		accountNumber = getIntent().getStringExtra(Comm.ACCOUNTNUMBER);
		init();
	}

	private void init() {
		signAccText = (TextView) findViewById(R.id.dept_zntzck_query_signAcc);
		sureButton = (Button) findViewById(R.id.sureButton);
		signAccText.setText(accountNumber);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent=new Intent(DeptZntzckCancelSuccessActivity.this,DeptZntzckThreeMenuActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(KeyEvent.KEYCODE_BACK==keyCode){
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
