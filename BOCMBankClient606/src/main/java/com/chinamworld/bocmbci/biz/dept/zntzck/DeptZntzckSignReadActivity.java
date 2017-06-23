package com.chinamworld.bocmbci.biz.dept.zntzck;

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
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 智能通知存款--协议页面
 * 
 * @author Administrator
 * 
 */
public class DeptZntzckSignReadActivity extends DeptBaseActivity {
	private static final String TAG = "DeptZntzckSignReadActivity";
	private LinearLayout tabcontent;// 主Activity显示
	private View detailView = null;
	private Button receptButton = null;
	private Button refuseButton = null;
	private TextView yfText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.dept_zntzck_service_protocol));
		ibRight.setVisibility(View.VISIBLE);
		ibBack.setVisibility(View.GONE);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		detailView = LayoutInflater.from(this).inflate(R.layout.dept_zntzck_sign_read, null);
		tabcontent.addView(detailView);
		receptButton = (Button) findViewById(R.id.sureButton);
		refuseButton = (Button) findViewById(R.id.lastButton);
		yfText = (TextView) findViewById(R.id.tv_yifang);
		Map<String, Object> returnMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);
		String loginName = String.valueOf(returnMap.get(Crcd.CRCD_CUSTOMERNAME));
		String text = getResources().getString(R.string.dept_dqydzc_read_yf);
		yfText.setText(text + loginName);
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
				Intent intent = new Intent(DeptZntzckSignReadActivity.this, DeptZntzckSignConfirmActivity.class);
				String tag = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.DEPT_GOTO_TAG);
				if(!StringUtil.isNull(tag)&&"1".equals(tag)){
					//查询页面跳转到此页面
					String accountNumber=getIntent().getStringExtra(Comm.ACCOUNTNUMBER);
					intent.putExtra(Comm.ACCOUNTNUMBER, accountNumber);
				}
				startActivity(intent);
			}
		});

	}
}
