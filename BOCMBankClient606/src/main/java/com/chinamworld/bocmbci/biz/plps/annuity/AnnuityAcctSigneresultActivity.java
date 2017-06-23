package com.chinamworld.bocmbci.biz.plps.annuity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsMenu;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;

/**
 * 养老金签约成功
 * @author panwe
 *
 */
public class AnnuityAcctSigneresultActivity extends PlpsBaseActivity{
	private int mPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.plps_annuity_signe_result);
		setTitle(PlpsDataCenter.annuity[0]);
		mPosition = getIntent().getIntExtra("p", -1);
	}
	
	/**
	 * 确认操作
	 * @param v
	 */
	public void btnOkOnclick(View v){
		if (mPosition == 1) {
			BaseHttpEngine.showProgressDialog();
			requestSystemDateTime(); return;
		}
		requestAnnuityPlanList();
	}
	
	
	@Override
	public void annuityPlanListCallBack(Object resultObj) {
		super.annuityPlanListCallBack(resultObj);
		annuityIntentAction(mPosition, PlpsMenu.ANNUITY);
		finish();
	}
	
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		PlpsDataCenter.getInstance().setSysDate(dateTime);
		requestAnnuityPlanList();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
}
