package com.chinamworld.bocmbci.biz.plps.payment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsMenu;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
/**
 * 签约代缴费主页
 * @author panwe
 *
 */
public class PaymentMainActivity extends PlpsBaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.plps_main);
		setTitle(R.string.plps_liveservice_title);
//		mRightButton.setVisibility(View.GONE);
		setUpView();
	}
	
	private void setUpView(){
		ListView mListView = (ListView) findViewById(R.id.listview);
		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, R.layout.plps_main_item,R.id.menusName, PlpsDataCenter.payment);
		mListView.setOnItemClickListener(this);
		mListView.setAdapter(mAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		super.onItemClick(parent, view, position, id);
		if (position == 0) {
			BaseHttpEngine.showProgressDialog();
			requestCustomerInfo();
		}
		if (position == 1) {
//			requestServiceType();
		}
	}

	@Override
	public void customerInfoCallBack(Object resultObj) {
		super.customerInfoCallBack(resultObj);
		requestSignList("0");
	}

	@Override
	public void signListCallBack(Object resultObj) {
		super.signListCallBack(resultObj);
		annuityIntentAction(0, PlpsMenu.PAYMENT,phoneNumber);
	}

//	@Override
//	public void serviceTypeCallBack(Object resultObj) {
//		super.serviceTypeCallBack(resultObj);
//		annuityIntentAction(1, PlpsMenu.PAYMENT);
//	}
}
