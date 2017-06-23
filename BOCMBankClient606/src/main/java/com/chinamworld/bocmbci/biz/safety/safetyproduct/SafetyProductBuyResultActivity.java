package com.chinamworld.bocmbci.biz.safety.safetyproduct;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyBaseActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyConstant;
import com.chinamworld.bocmbci.biz.safety.safetytemp.SafetyTempProductListActivity;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 投保成功页
 * @author panwe
 *
 */
public class SafetyProductBuyResultActivity extends SafetyBaseActivity{
	private View mMainView;
	private boolean isFromList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainView = View.inflate(this, R.layout.safety_product_buy_result, null);
		setTitle(getString(R.string.safety_success_title));
		setLeftTopGone();
		addView(mMainView);
		initViews();
	}

	private void initViews() {
		Intent it = getIntent();
		isFromList = it.getBooleanExtra(SafetyConstant.PRODUCTORSAVE, false);
		((TextView) mMainView.findViewById(R.id.companyname)).setText(it.getStringExtra(Safety.INSURANCE_COMANY));
		((TextView) mMainView.findViewById(R.id.tv_subCompany)).setText(it.getStringExtra(Safety.SUBINSUNAME));
		((TextView) mMainView.findViewById(R.id.productname)).setText(it.getStringExtra(Safety.RISKNAME));
		((TextView) mMainView.findViewById(R.id.billcode)).setText(it.getStringExtra(Safety.SAFETY_HOLD_POLICY_NO));
		((TextView) mMainView.findViewById(R.id.money)).setText(StringUtil.parseStringPattern(it.getStringExtra(Safety.RISKPAEM), 2));
		((TextView) mMainView.findViewById(R.id.surstartdate)).setText(it.getStringExtra(Safety.SAFETY_HOLD_APPL_DATE));
		((TextView) mMainView.findViewById(R.id.surlastdate)).setText(it.getStringExtra(Safety.SAFETY_HOLD_POL_END_DATE));
		((TextView) mMainView.findViewById(R.id.email)).setText(it.getStringExtra(Safety.APPL_EMAIL));
	}

	/**
	 * 完成
	 * @param v
	 */
	public void insurFinishOnclick(View v){
		ActivityTaskManager.getInstance().removeAllActivity();
		Intent mIntent = new Intent();
		if (isFromList) {
			mIntent.setClass(this, SafetyProductListActivity.class);
		}else{
			mIntent.putExtra(SafetyConstant.NEEDDELETE, true);
			mIntent.setClass(this, SafetyTempProductListActivity.class);
		}
		startActivity(mIntent);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
}
