package com.chinamworld.bocmbci.biz.branchorder.unvip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Order;
import com.chinamworld.bocmbci.biz.branchorder.BranchOrderBaseActivity;
import com.chinamworld.bocmbci.biz.branchorder.BranchOrderDataCenter;
import com.chinamworld.bocmbci.biz.branchorder.BranchOrderUtils;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class BranchOrderUnvipInputInfoActivity extends BranchOrderBaseActivity {

	private Spinner serviceType, orderAheadTipNumber;
	private TextView orderDate, orderCurrentWaitNumber;
	private Button lastStep, nextStep;
	
	private String serviceNameStr, serviceIdStr;
	private List<String> serviceNameList;
	private Map<String, Object> serviceIdName;
	private List<String> numList = new ArrayList<String>();
	private String aheadTip ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.order_unvip_input_book_info);
		setTitle(R.string.order_main_title);
		setPadding(0, 0, 0, 0);
		setStepBackground();
		setupView();
		setDataForView();
	}
	
	private void setStepBackground(){
		((LinearLayout)findViewById(R.id.layout_step1))
			.setBackgroundResource(R.drawable.safety_step1);
		((TextView)findViewById(R.id.index1))
			.setTextColor(getResources().getColor(R.color.red));
		((TextView)findViewById(R.id.text1))
			.setTextColor(getResources().getColor(R.color.red));
	}
	
	private void setupView(){
		lastStep = (Button)findViewById(R.id.finc_btn1);
		nextStep = (Button)findViewById(R.id.finc_btn2);
		orderDate = (TextView) findViewById(R.id.orderTimeTv);
		orderCurrentWaitNumber = (TextView) findViewById(R.id.orderCurrentWaitNumber);
		serviceType = (Spinner) findViewById(R.id.serviceName);
		orderAheadTipNumber = (Spinner) findViewById(R.id.orderAheadTipNumber);
		BranchOrderUtils.setOnShowAllTextListener(this, orderDate, orderCurrentWaitNumber);
		lastStep.setOnClickListener(this);
		nextStep.setOnClickListener(this);
		serviceType.setOnItemSelectedListener(this);
		orderAheadTipNumber.setOnItemSelectedListener(this);
	}
	
	private void setDataForView(){
		Map<String, Object> orderDetail = BranchOrderDataCenter.getInstance().getOrderDetail();
		if(!StringUtil.isNullOrEmpty(orderDetail)){
			List<Map<String, Object>> normalBusinessList = (List<Map<String, Object>>)
					orderDetail.get(Order.NORMALBUSINESSLIST);
			serviceIdName = BranchOrderUtils.getMapFromMapListByDate(normalBusinessList, 
					Order.BSID, Order.BSNAME, QueryDateUtils.getcurrentDate(BranchOrderDataCenter.getInstance().
							getCustomerInfo(Order.DATETIME)));
			serviceNameList = BranchOrderUtils.initServiceSpinnerView(this, serviceType, serviceIdName);
		}
		setText(QueryDateUtils.getcurrentDate(BranchOrderDataCenter.getInstance().
				getCustomerInfo(Order.DATETIME)), orderDate);
		numList = getListString1To10();
		BranchOrderUtils.initSpinnerView(this, orderAheadTipNumber, numList);
	}
	
	private List<String> getListString1To10(){
		List<String> list = new ArrayList<String>();
		list.add("无须提醒");
		for(int i = 1 ; i <= 10; i ++){
			list.add(i + "");
		}
		return list;
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_btn1:
			finish();
			break;
		case R.id.finc_btn2:
			if(saveCustomerInfo())
				startActivityForResult(new Intent(BranchOrderUnvipInputInfoActivity.this, 
						BranchOrderUnvipConfirmInfoActivity.class), BranchOrderDataCenter.UN_VIP);
			break;

		default:
			break;
		}
	}
	
	private boolean saveCustomerInfo(){
		if(BranchOrderDataCenter.isTipChecked){
			BranchOrderDataCenter.getInstance().setCustomerInfo(
					Order.REMINDNUMBER, aheadTip);
		}
		BranchOrderDataCenter.getInstance().setCustomerInfo(
				Order.BSNAME, serviceNameStr);
		BranchOrderDataCenter.getInstance().setCustomerInfo(
				Order.BSID, serviceIdStr);
		return true;
	}
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		super.onItemSelected(arg0, arg1, arg2, arg3);
		switch (arg0.getId()) {
		case R.id.serviceName:
			if(StringUtil.isNullOrEmpty(serviceNameList))
				return;
			serviceNameStr = serviceNameList.get(arg2);
			serviceIdStr = BranchOrderUtils.getKeyByValue(serviceIdName, serviceNameStr);
			BaseHttpEngine.showProgressDialog();
			requestLoginPreConversationId();
			break;
		case R.id.orderAheadTipNumber:
			aheadTip = numList.get(arg2);
			BranchOrderDataCenter.isTipChecked = arg2 == 0 ? false : true;
			break;
		default:
			break;
		}
	}
	
	@Override
	public void requestLoginPreConversationIdCallBack(Object resultObj) {
		super.requestLoginPreConversationIdCallBack(resultObj);
		requestPsnOrderInfoQuery();
	}
	
	public void requestPsnOrderInfoQuery(){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOGIN_PRECONVERSATIONID).toString());
		Map<String, Object> map = BranchOrderDataCenter.getInstance().getOrderListItem();
		biiRequestBody.setMethod(Order.METHOD_BRANCHORDER_INFO_QUERY);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Order.ORDERORGCODE, (String)map.get(Order.ORDERORGCODE));
		params.put(Order.BSID, serviceIdStr);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnOrderInfoQueryCallBack");
	}
	
	public void requestPsnOrderInfoQueryCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		BranchOrderDataCenter.getInstance().setOrderInfoQueryMap(map);
		orderCurrentWaitNumber.setText(StringUtil.
				valueOf1((String)map.get(Order.ORGORDERNUMBER)) + "人");
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case BranchOrderDataCenter.UN_VIP:
			switch (resultCode) {
			case RESULT_OK:
				setResult(RESULT_OK);
				finish();
				break;
			case 1001:
				setResult(1001);
				finish();
				break;
			default:
				break;
			}
			break;

		default:
			break;
		}
	}
}
