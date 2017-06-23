package com.chinamworld.bocmbci.biz.dept.ydzc;

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
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.log.LogGloble;

public class DeptYdzcAppointDeleteSuccessActivity extends DeptBaseActivity {
	private static final String TAG = "DeptYdzcAppointDeleteConfiemActivity";
	private LinearLayout tabcontent;// 主Activity显示
	private View detailView = null;
	private TextView accToText = null;
	private TextView volumberText = null;
	private TextView cdnumberText = null;
	private Button sureButton = null;
	private String accountNumber = null;
	private String volumeNumber = null;
	private String cdNumber = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.dept_dqydzc_appoint_delete_tite));
		ibRight.setVisibility(View.VISIBLE);
		ibBack.setVisibility(View.GONE);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		detailView = LayoutInflater.from(this).inflate(R.layout.dept_dqydzc_appoint_delete_success, null);
		tabcontent.addView(detailView);
		accountNumber = getIntent().getStringExtra(Comm.ACCOUNTNUMBER);
		volumeNumber = getIntent().getStringExtra(Dept.DEPT_VOLUMENUMBER_RES);
		cdNumber = getIntent().getStringExtra(Dept.DEPT_CDNUMBER_RES);
		init();
	}

	private void init() {
		accToText = (TextView) findViewById(R.id.dept_dqydzc_input_acc);
		volumberText = (TextView) findViewById(R.id.dept_dqydzc_input_volumber);
		cdnumberText = (TextView) findViewById(R.id.dept_dqydzc_input_cdnumber);
		sureButton = (Button) findViewById(R.id.sureButton);
		accToText.setText(accountNumber);
		volumberText.setText(volumeNumber);
		cdnumberText.setText(cdNumber);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent(DeptYdzcAppointDeleteSuccessActivity.this, DeptYdzcQueryActivity.class);
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
