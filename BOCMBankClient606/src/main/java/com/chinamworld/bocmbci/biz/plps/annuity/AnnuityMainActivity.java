package com.chinamworld.bocmbci.biz.plps.annuity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsMenu;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
/**
 * 养老金主页
 * @author panwe
 *
 */
public class AnnuityMainActivity extends PlpsBaseActivity{
	private int mPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.plps_main);
		setTitle(R.string.plps_annuity_title);
//		mRightButton.setVisibility(View.GONE);
		setUpView();
	}
	
	private void setUpView(){
		ListView mListView = (ListView) findViewById(R.id.listview);
		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, R.layout.plps_main_item,R.id.menusName, PlpsDataCenter.annuity);
		mListView.setOnItemClickListener(this);
		mListView.setAdapter(mAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		super.onItemClick(parent, view, position, id);
		mPosition = position;
		requestAnnuityAccIsSigned();
	}
	
	/**
	 * 判断是否签约
	 */
	private void requestAnnuityAccIsSigned(){
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.METHODANNUITYACCISSIGNED);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "annuityAccIsSignedCallBack");
	}
	
	public void annuityAccIsSignedCallBack(Object resultObj) {
		String isSigned = HttpTools.getResponseResult(resultObj);
		if (isSigned.equals("true")) {
			if (mPosition == 1) {
				requestSystemDateTime(); return;
			}
			requestAnnuityPlanList(); return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		startActivity(new Intent(this, AnnuityAcctSigneConfirmActivity.class).putExtra("p", mPosition));
	}
	
	@Override
	public void annuityPlanListCallBack(Object resultObj) {
		super.annuityPlanListCallBack(resultObj);
		annuityIntentAction(mPosition, PlpsMenu.ANNUITY);
	}
	
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		PlpsDataCenter.getInstance().setSysDate(dateTime);
		requestAnnuityPlanList();
	}
}
