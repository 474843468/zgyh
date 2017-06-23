package com.chinamworld.bocmbci.biz.plps.payment.project.dialogactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsMenu;
import com.chinamworld.bocmbci.biz.plps.adapter.PaymentDialogAdapter;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

public class PaymentMainDialogActivity extends PlpsBaseActivity{
	private ArrayList<HashMap<String, Object>> accountList;
	//签约缴费图片
	private int[] imageIds= new int[]{
			R.drawable.payment_info_query,
			R.drawable.payment
	};
	//签约缴费列表
	private String[] textIds = new String[]{
			"签约信息查询",
			"签约"
	};
//	private RelativeLayout rl_bankt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		BaseDroidApp.getInstanse().setDialogAct(true);
//		setContentView(R.layout.plps_payment_dialog);
		setTitle(R.string.plps_contract_payment);
		inflateLayout(R.layout.plps_payment_dialog);
//		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		accountList = new ArrayList<HashMap<String,Object>>();
		for(int i=0; i<imageIds.length; i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("image", imageIds[i]);
			map.put("text", textIds[i]);
			accountList.add(map);
		}
		initView();
	}

	/**
	 * 布局初始化
	 */
	private void initView() {
//		rl_bankt = (RelativeLayout) findViewById(R.id.rl_bankt);
		GridView grid = (GridView) findViewById(R.id.gridview);
		PaymentDialogAdapter paymentProjectAdapter = new PaymentDialogAdapter(
				this, accountList);
		grid.setOnItemClickListener(this);
		grid.setAdapter(paymentProjectAdapter);
//		rl_bankt.setOnTouchListener(new OnTouchListener() {
//
//			@SuppressLint("ClickableViewAccessibility")
//			@Override
//			public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
//				// TODO Auto-generated method stub
//				finish();
//				return false;
//			}
//		});
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		super.onItemClick(parent, view, position, id);
		if(position==0){
			BaseHttpEngine.showProgressDialog();
			//请求客户手机信息
			requestCustomerInfo();
		}
		if(position==1){
//			requestServiceType();
			requestProxyPaymentCityQuery();
		}
	}
	/*
	 * 请求客户手机信息回调
	 */
	@Override
	public void customerInfoCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.customerInfoCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNull(phoneNumber)) {
			BaseDroidApp.getInstanse().showMessageDialog("尊敬的客户，您尚未签约任何缴费项目。", new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					BaseDroidApp.getInstanse().dismissMessageDialog();
				}
			});
			return;
		}
		//客户签约信息查询
		requestSignList("0");
	}
	@Override
	public void signListCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.signListCallBack(resultObj);
		annuityIntentAction(0, PlpsMenu.PAYMENT, phoneNumber);
	}
	/**
	 * 业务类型签约回调*/
//	@Override
//	public void serviceTypeCallBack(Object resultObj) {
//		// TODO Auto-generated method stub
//		super.serviceTypeCallBack(resultObj);
//		annuityIntentAction(1, PlpsMenu.PAYMENT);
//	}
	@Override
	public void requestProxyPaymentCityQueryCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestProxyPaymentCityQueryCallBack(resultObj);
		List<Map<String, Object>> cityList = PlpsDataCenter.getInstance().getCityList();
		if(StringUtil.isNullOrEmpty(cityList)){
			BaseDroidApp.getInstanse().showMessageDialog("该默认地区没有可签约项目，请选择其他默认地区", new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					BaseDroidApp.getInstanse().dismissMessageDialog();
				}
			});
		}else {
			annuityIntentAction(1, PlpsMenu.PAYMENT);
		}
	}
}
