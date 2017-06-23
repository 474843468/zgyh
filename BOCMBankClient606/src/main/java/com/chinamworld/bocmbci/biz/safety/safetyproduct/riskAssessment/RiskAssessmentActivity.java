package com.chinamworld.bocmbci.biz.safety.safetyproduct.riskAssessment;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyConstant;

/**
 * 提示风险评估页面，风评到期页面，风评等级不够页面
 * 
 * @author Zhi
 */
public class RiskAssessmentActivity extends RiskAssessmentBaseActivity implements OnClickListener {

	/** 主显示视图 */
	private View mMainView;
	/** 页面状态 0-提示风险评估页面 1-风评到期页面 2-风评等级不够页面 */
	private String pageState;
	/** 确定/重新测按钮 */
	private Button btnConfirm;
	/** 取消按钮 */
	private Button btnCancle;
	/** 底部查看上次评估结果链接 */
	private View link;
	/** 信息显示tv */
	private TextView tvInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainView = addView(R.layout.safety_riskassessment_act);
		setTitle(R.string.boci_evaluation_title);
		findView();
		viewSet();
		String flag = getIntent().getStringExtra(SafetyConstant.RISKFLAG);
		setPageState(flag);
	}
	
	private void findView() {
		tvInfo = (TextView) getView(R.id.tv_info);
		link = getView(R.id.tv_link);
		btnConfirm = (Button) getView(R.id.btnConfirm);
		btnCancle = (Button) getView(R.id.btnCancle);
	}
	
	private void viewSet() {
		link.setOnClickListener(this);
		btnCancle.setOnClickListener(this);
		btnConfirm.setOnClickListener(this);
		
		mLeftButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}
	
	private String getPageState() {
		return pageState;
	}

	/** 设置页面状态，在请求是否做了风险评估接口回调中调用 */
	private void setPageState(String pageState) {
		this.pageState = pageState;
		if (pageState.equals("0")) {
			setRiskAssesmentPage();
		} else if (pageState.equals("1")) {
			setRiskAssesmentDuePage();
		} else if (pageState.equals("2")) {
			setRiskAssesmentDontDeal();
		}
	}
	
	/** 设置页面显示为提示风险评估页 */
	private void setRiskAssesmentPage() {
		tvInfo.setText(getResources().getString(R.string.safety_riskAssessment_tip1));
		getView(R.id.tv_link).setVisibility(View.GONE);
		btnConfirm.setText(getResources().getString(R.string.confirm));
		btnCancle.setText(getResources().getString(R.string.cancle));
	}

	/** 设置页面显示为已到期页 */
	private void setRiskAssesmentDuePage() {
		tvInfo.setText(getResources().getString(R.string.safety_riskAssessment_tip2));
		getView(R.id.tv_link).setVisibility(View.VISIBLE);
		btnConfirm.setText(getResources().getString(R.string.safety_riskAssessment_continueBtn));
		btnCancle.setText(getResources().getString(R.string.cancle));
	}

	/** 设置页面显示为等级不够页 */
	private void setRiskAssesmentDontDeal() {
		tvInfo.setText(getResources().getString(R.string.safety_riskAssessment_tip3));
		getView(R.id.tv_link).setVisibility(View.VISIBLE);
		btnConfirm.setText(getResources().getString(R.string.safety_riskAssessment_continueBtn));
		btnCancle.setText(getResources().getString(R.string.cancle));
	}
	
	private View getView(int sourseId) {
		return mMainView.findViewById(sourseId);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 6) {
			if (resultCode == 4) {
				setResult(4);
				finish();
			} else if (resultCode == RESULT_CANCELED) {
				setResult(RESULT_CANCELED);
				finish();
				return;
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_CANCELED);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnConfirm:
			Intent intent0 = new Intent(this, RiskAssessmentDetailActivity.class);
			if (getPageState().equals("0")) {
				intent0.putExtra(SafetyConstant.RISKFLAG, 0);
			} else if (getPageState().equals("1")) {
				intent0.putExtra(SafetyConstant.RISKFLAG, 0);
			} else if (getPageState().equals("2")) {
				intent0.putExtra(SafetyConstant.RISKFLAG, 0);
			}

			intent0.putExtra(Safety.INSURANCE_ID, getIntent().getStringExtra(Safety.INSURANCE_ID));
			intent0.putExtra(Safety.RISKCODE, getIntent().getStringExtra(Safety.RISKCODE));
			startActivityForResult(intent0, 6);
			break;

		case R.id.btnCancle:
			setResult(RESULT_CANCELED);
			finish();
			break;
			
		case R.id.tv_link:
			Intent intent1 = new Intent(this, RiskAssessmentDetailActivity.class);
			intent1.putExtra(SafetyConstant.RISKFLAG, 1);
			intent1.putExtra(Safety.INSURANCE_ID, getIntent().getStringExtra(Safety.INSURANCE_ID));
			intent1.putExtra(Safety.RISKCODE, getIntent().getStringExtra(Safety.RISKCODE));
			startActivityForResult(intent1, 6);
			break;
		}
	}
}
