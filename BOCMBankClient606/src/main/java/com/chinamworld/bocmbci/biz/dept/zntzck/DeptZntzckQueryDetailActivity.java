package com.chinamworld.bocmbci.biz.dept.zntzck;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.log.LogGloble;

/** 智能通知存款---详情页面-----此页面不在使用 */
public class DeptZntzckQueryDetailActivity extends DeptBaseActivity {
	private static final String TAG = "DeptZntzckQueryDetailActivity";
	private LinearLayout tabcontent;// 主Activity显示
	private View queryView = null;
	private TextView signAccText = null;
	private TextView signTypeText = null;
	private TextView signDateText = null;
	private TextView signStatusText = null;
	private Button cancelButton = null;
	private String accountId = null;
	private View buttonView = null;
    private String accountNumber=null;
    private TextView cancelChnlFlagText=null;
    private TextView cancelDateText=null;
    private View cancelChnlFlagView=null;
    private View cancelDateView=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		ibRight.setVisibility(View.VISIBLE);
		setTitle(getResources().getString(R.string.dept_zntzck_menu));
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		queryView = LayoutInflater.from(this).inflate(R.layout.dept_zntzck_query_detail, null);
		tabcontent.addView(queryView);
		init();
	}

	private void init() {
		signAccText = (TextView) findViewById(R.id.dept_zntzck_query_signAcc);
		signTypeText = (TextView) findViewById(R.id.dept_zntzck_query_signType);
		signDateText = (TextView) findViewById(R.id.dept_zntzck_query_signDate);
		signStatusText = (TextView) findViewById(R.id.dept_zntzck_query_signStatus);
		cancelButton = (Button) findViewById(R.id.dept_dqyzc_detail_cancel);
		buttonView = findViewById(R.id.ll_btn);
		cancelChnlFlagText=(TextView) findViewById(R.id.dept_zntzck_query_cancelChnlFlag);
		cancelChnlFlagView=findViewById(R.id.cancelChnlFlag_layout);
		cancelDateText=(TextView) findViewById(R.id.dept_zntzck_query_cancelDate);
		cancelDateView=findViewById(R.id.cancelDate_layout);
		String signDate = getIntent().getStringExtra(Dept.DEPT_SIGNDATE_RES);//签约日期
		String signChnlFlags = getIntent().getStringExtra(Dept.DEPT_SIGNCHNLFLAG_RES);//签约标志
		String cancelDate = getIntent().getStringExtra(Dept.DEPT_CANCELDATE_RES);
		String cancelChnlFlag=getIntent().getStringExtra(Dept.DEPT_CANCELCHNLFLAG_RES);
		String status=getIntent().getStringExtra("status");
		accountId = getIntent().getStringExtra(Comm.ACCOUNT_ID);
		accountNumber = getIntent().getStringExtra(Comm.ACCOUNTNUMBER);
		int tag = getIntent().getIntExtra(ConstantGloble.FOREX_TAG, 0);
		if (tag == 1) {
			buttonView.setVisibility(View.VISIBLE);
			cancelChnlFlagView.setVisibility(View.GONE);
			cancelDateView.setVisibility(View.GONE);
		} else if(tag==2){
			//已解约
			buttonView.setVisibility(View.GONE);
			cancelChnlFlagView.setVisibility(View.VISIBLE);
			cancelDateView.setVisibility(View.VISIBLE);
			cancelChnlFlagText.setText(cancelChnlFlag);
			cancelDateText.setText(cancelDate);
		}
		signAccText.setText(accountNumber);
		signTypeText.setText(signChnlFlags);
		signDateText.setText(signDate);
		signStatusText.setText(status);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DeptZntzckQueryDetailActivity.this, DeptZntzckCancelConfirmActivity.class);
				intent.putExtra(Comm.ACCOUNT_ID, accountId);
				intent.putExtra(Comm.ACCOUNTNUMBER, accountNumber);
				startActivity(intent);
			}
		});
	}
}
