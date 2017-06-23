package com.chinamworld.bocmbci.biz.safety.safetyproduct.riskAssessment;

import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;

/**
 * 风险评估结果页
 * 
 * @author Zhi
 */
public class RiskAssessmentResultActivity extends RiskAssessmentBaseActivity {

	/** 主视图 */
	private View mMainView;
	/** 重新测按钮 */
	private TextView btnAgain;
	/** 确定按钮 */
	private TextView btnConfirm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainView = addView(R.layout.safety_riskassessment_result);
		setTitle(R.string.boci_evaluation_title);
		findView();
		viewSet();
	}
	
	private void findView() {
		btnAgain = (TextView) mMainView.findViewById(R.id.btnConfirm);
		btnConfirm = (TextView) mMainView.findViewById(R.id.btnQuit);
		btnAgain.setOnClickListener(againListener);
		btnConfirm.setOnClickListener(confirmListener);
	}
	
	private void viewSet() {
		Map<String, Object> map = SafetyDataCenter.getInstance().getMapInsuranceRiskEvaluationQuery();
		((TextView) mMainView.findViewById(R.id.tv_evaluationDate)).setText((String) map.get(Safety.EVALUATIONDATE));
		((TextView) mMainView.findViewById(R.id.tv_validDate)).setText((String) map.get(Safety.VALIDDATE));
		((TextView) mMainView.findViewById(R.id.tv_custType)).setText((String) map.get(Safety.CUSTTYPE));
		((TextView) mMainView.findViewById(R.id.tv_invest)).setText((String) map.get(Safety.INVEST));
		
		String machFlag = getIntent().getStringExtra(Safety.MACHFLAG);
		if ("0".equals(machFlag)) {
			btnAgain.setVisibility(View.VISIBLE);
			btnConfirm.setVisibility(View.GONE);
		} else {
			btnAgain.setVisibility(View.GONE);
			btnConfirm.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public ActivityTaskType getActivityTaskType() {
		return ActivityTaskType.TwoTask;
	}
	
	private OnClickListener againListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			setResult(1);
			finish();
		}
	};
	
	private OnClickListener confirmListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			setResult(4);
			finish();
		}
	};
}
