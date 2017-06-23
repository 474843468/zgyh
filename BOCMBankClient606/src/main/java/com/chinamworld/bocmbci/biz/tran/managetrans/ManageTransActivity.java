package com.chinamworld.bocmbci.biz.tran.managetrans;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.tran.ManageTransBaseActivity;
import com.chinamworld.bocmbci.biz.tran.managetrans.managepayee.PayeeListActivity;
import com.chinamworld.bocmbci.biz.tran.managetrans.managetranrecords.ManageTransRecordActivity1;
import com.chinamworld.bocmbci.biz.tran.managetrans.premanage.QueryDateActivity1;

/**
 * 转账管理
 * 
 * @author
 * 
 */
public class ManageTransActivity extends ManageTransBaseActivity {

	private LinearLayout mPreManageLl, mPayeeManageLl, mRecordsManageLl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// ActivityTaskManager.getInstance().removeAllActivity();
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.manage_trans));
		View view = mInflater
				.inflate(R.layout.tran_manage_trans_activity, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		mTopRightBtn = (Button) findViewById(R.id.ib_top_right_btn);
		// mainBtn.setVisibility(View.INVISIBLE);
		mTopRightBtn.setVisibility(View.INVISIBLE);
		setupView();
		setLeftSelectedPosition("tranManager_4");
		
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		setLeftSelectedPosition("tranManager_4");
//	}

	/**
	 * 初始化加载XML里的控件
	 */
	private void setupView() {
		mPreManageLl = (LinearLayout) findViewById(R.id.ll_pre_manage_trans);
		mPayeeManageLl = (LinearLayout) findViewById(R.id.ll_payee_manage_trans);
		mRecordsManageLl = (LinearLayout) findViewById(R.id.ll_records_manage_trans);

		mPreManageLl.setOnClickListener(preManageListener);
		mPayeeManageLl.setOnClickListener(payeeManageListener);
		mRecordsManageLl.setOnClickListener(recordsManageListener);

	}

	/**
	 * 预约管理
	 */
	private OnClickListener preManageListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(ManageTransActivity.this,
					QueryDateActivity1.class);
			startActivity(intent);
		}
	};
	/**
	 * 收款人管理
	 */
	private OnClickListener payeeManageListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(ManageTransActivity.this,
					PayeeListActivity.class);
			startActivity(intent);
			
		}
	};
	/**
	 * 转账记录管理
	 */
	private OnClickListener recordsManageListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(ManageTransActivity.this,
					ManageTransRecordActivity1.class);
			startActivity(intent);
		}
	};

}
