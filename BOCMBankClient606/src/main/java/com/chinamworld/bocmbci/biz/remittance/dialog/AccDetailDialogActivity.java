package com.chinamworld.bocmbci.biz.remittance.dialog;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.remittance.RemittanceDataCenter;
import com.chinamworld.bocmbci.biz.remittance.adapter.AccDetailAdapter;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.log.LogGloble;

/**
 * 跨境汇款账户详情弹出框
 * 
 * @author Zhi
 */
public class AccDetailDialogActivity extends BaseActivity{

	private static final String TAG = "AccDetailDialogActivity";
	
	private RelativeLayout rl_bank;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BaseDroidApp.getInstanse().setDialogAct(true);
		// 添加布局
		setContentView(R.layout.acc_for_dialog);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		rl_bank = (RelativeLayout) findViewById(R.id.rl_bank);
		android.widget.RelativeLayout.LayoutParams lp = new android.widget.RelativeLayout.LayoutParams(LayoutValue.SCREEN_WIDTH, LayoutValue.SCREEN_HEIGHT * 7 / 8);
		rl_bank.setLayoutParams(lp);
		rl_bank.removeAllViews();
		rl_bank.addView(initView());
	}
	
	private View initView() {
		View view = LayoutInflater.from(this).inflate(R.layout.remittance_info_input_acc_detail_dialog, null);
		ListView lv = (ListView) view.findViewById(R.id.lv_accDetail);
		List<Map<String, Object>> detailList = RemittanceDataCenter.getInstance().getAccDetail();
		AccDetailAdapter adapter = new AccDetailAdapter(this, detailList);
		LogGloble.i(TAG, detailList.toString());
		lv.setAdapter(adapter);
		
		view.findViewById(R.id.btnClose).setOnClickListener(closeListener);
		view.findViewById(R.id.img_exit_accdetail).setOnClickListener(closeListener);
		
		return view;
	}
	
	private OnClickListener closeListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			finish();
		}
	};

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}
}
