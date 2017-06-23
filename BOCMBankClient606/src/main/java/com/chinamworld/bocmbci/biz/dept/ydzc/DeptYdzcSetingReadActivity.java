package com.chinamworld.bocmbci.biz.dept.ydzc;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.log.LogGloble;

/** 设置约定转存--协议页面 */
public class DeptYdzcSetingReadActivity extends DeptBaseActivity {
	private static final String TAG = "DeptYdzcSetingInputActivity";
	private LinearLayout tabcontent;// 主Activity显示
	private View detailView = null;
	private Button receptButton = null;
	private Button refuseButton = null;
	private TextView yfText = null;
    private String moneyLeftText=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.dept_zntzck_service_protocol));
		ibRight.setVisibility(View.VISIBLE);
		ibBack.setVisibility(View.GONE);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		detailView = LayoutInflater.from(this).inflate(R.layout.dept_dqydzc_set_read, null);
		tabcontent.addView(detailView);
		receptButton = (Button) findViewById(R.id.sureButton);
		refuseButton = (Button) findViewById(R.id.lastButton);
		yfText = (TextView) findViewById(R.id.tv_yifang);
		Map<String, Object> returnMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);
		String loginName = String.valueOf(returnMap.get(Crcd.CRCD_CUSTOMERNAME));
		String text = getResources().getString(R.string.dept_dqydzc_read_yf);
		yfText.setText(text + loginName);
		moneyLeftText=getIntent().getStringExtra("text");
		refuseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		// 接受按钮
		receptButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DeptYdzcSetingReadActivity.this, DeptYdzcSetingConfirmActivity.class);
				intent.putExtra("text", moneyLeftText);
				startActivity(intent);
			}
		});
	}
}
