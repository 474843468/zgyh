package com.chinamworld.bocmbci.biz.dept;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.dept.appointmanager.QueryDateActivity1;
import com.chinamworld.bocmbci.biz.dept.recordmanager.RegularRecordActivity;
import com.chinamworld.bocmbci.biz.investTask.BocInvestTask;
import com.chinamworld.bocmbci.biz.investTask.IAction;


/**
 * @ClassName: QureyRecordManagerActivity
 * @Description: 管理定期记录查询
 * @author JiangWei
 * @date 2013-7-1 下午2:59:50
 */
public class QureyRecordManagerActivity extends DeptBaseActivity {
	
	private static final String TAG = QureyRecordManagerActivity.class.getSimpleName();

	private LinearLayout tabcontent;
	private LinearLayout mPreManageLl, mRecordsManageLl;
	private TextView labelRecord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//ActivityTaskManager.getInstance().removeAllActivity();
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.to_regular_record_search));
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater
				.inflate(R.layout.qurey_record_manager_activity, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		((ImageView) findViewById(R.id.img_line)).setVisibility(View.GONE);
		((LinearLayout)findViewById(R.id.ll_records_manage_trans)).setVisibility(View.GONE);
		
		newTranBtn = (Button) this.findViewById(R.id.ib_top_right_btn);
		newTranBtn.setVisibility(View.GONE);
//		if(LocalData.deptStorageCashLeftList.size()==6){
			setLeftSelectedPosition("deptStorageCash_6");
//		}else{
//			setLeftSelectedPosition(RECORD_QUERY+1);
//		}
		// add lqp 2015年12月14日11:58:43 判断是否开通投资理财!
		BocInvestTask task = BocInvestTask.getInstance(this);
		registActivityEvent(task);
		task.doTask(new IAction() {

			@Override
			public void SuccessCallBack(Object param) {
				// 查询 通知管理 账户列表
				setupView();
			}
		},null);
		// 修改判断是否开通投资理财!
//		setupView();
	}
	
	/**
	 * 初始化加载XML里的控件
	 */
	private void setupView() {
		mPreManageLl = (LinearLayout) findViewById(R.id.ll_pre_manage_trans);
		mRecordsManageLl = (LinearLayout) findViewById(R.id.ll_payee_manage_trans);
		labelRecord = (TextView) findViewById(R.id.label_trans_recrods);
		
		mPreManageLl.setOnClickListener(preManageListener);
		mRecordsManageLl.setOnClickListener(recordsManageListener);
		labelRecord.setText(this.getResources().getString(R.string.regular_record_query));
	}

	private OnClickListener preManageListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(QureyRecordManagerActivity.this,QueryDateActivity1.class);
			startActivity(intent);
		}
	};

	private OnClickListener recordsManageListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(QureyRecordManagerActivity.this,RegularRecordActivity.class);
			startActivity(intent);
		}
	};
	
	
	

}
