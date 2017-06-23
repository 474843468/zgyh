package com.chinamworld.bocmbci.biz.safety.safetyproduct.lifelnsurance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.adapter.LifeInsurChoosePayTypeAdapter;
import com.chinamworld.bocmbci.constant.LayoutValue;

public class ChoosePayTypeDialogActivity extends BaseActivity {

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
		View view = LayoutInflater.from(this).inflate(R.layout.safety_life_input_insurance_choose_paytype, null);
		ListView lv = (ListView) view.findViewById(R.id.lv_payType);
		LifeInsurChoosePayTypeAdapter adapter = new LifeInsurChoosePayTypeAdapter(this, SafetyDataCenter.getInstance().getListInsurancePayTypeInfoQuery());
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(selectListener);
		view.findViewById(R.id.img_exit_accdetail).setOnClickListener(closeListener);
		return view;
	}
	
	/** 关闭监听 */
	private OnClickListener closeListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			setResult(-1);
			finish();
		}
	};
	
	/** 选择列表项监听 */
	private OnItemClickListener selectListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			setResult(position);
			finish();
		}
	};

	@Override
	public ActivityTaskType getActivityTaskType() {
		return ActivityTaskType.TwoTask;
	}
}
