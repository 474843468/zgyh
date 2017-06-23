//package com.chinamworld.bocmbci.biz.plps.annuity;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemSelectedListener;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.Spinner;
//
//import com.chinamworld.bocmbci.R;
//import com.chinamworld.bocmbci.base.application.BaseDroidApp;
//import com.chinamworld.bocmbci.bii.BiiRequestBody;
//import com.chinamworld.bocmbci.bii.constant.Plps;
//import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
//import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
//import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
//import com.chinamworld.bocmbci.biz.plps.adapter.AnnuityPlanAdapter;
//import com.chinamworld.bocmbci.http.HttpManager;
//import com.chinamworld.bocmbci.http.HttpTools;
//import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
//import com.chinamworld.bocmbci.utils.QueryDateUtils;
//import com.chinamworld.bocmbci.utils.StringUtil;
//
///**
// * 变更申请列表
// * @author panwe
// *
// */
//public class AnnuityPlanListActivity extends PlpsBaseActivity{
//	private int position;
//	private int selectposition = -1;
//	private AnnuityPlanAdapter mAdapter;
//	//列表信息
//	private List<Map<String, Object>> mList;
//	//用户编号
//	private String planNumber;
//	//用户编号和计划名称
//	private List<String> planData;
//	private Spinner spinner;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
////		inflateLayout(R.layout.plps_annuity_plan_list);
//		position = getIntent().getIntExtra("p", -1);
//		setTitle(PlpsDataCenter.annuity[position]);
//		initData();
//		
////		setUpView();
//	}
//	private void initData(){
//		mRightButton.setVisibility(View.VISIBLE);
//		cityAdress.setVisibility(View.GONE);
//		planData = new ArrayList<String>();
//		mList = PlpsDataCenter.getInstance().getPlanList();
//		for(int i=0; i<mList.size();i++){
//			planNumber = (String)(mList.get(i).get(Plps.PLANNO)+"/"+mList.get(i).get(Plps.PLANNAME));
//			planData.add(planNumber);
//		}
//		setUpView();
//	}
//	
//	private void setUpView(){
//		inflateLayout(R.layout.plps_annuity_plan_list);
////		ListView mListView = (ListView) findViewById(R.id.listview);
////		mListView.setOnItemClickListener(this);
//		spinner = (Spinner)findViewById(R.id.spinner_plan);
////		spinner.setOnItemClickListener(this);
////		mAdapter = new AnnuityPlanAdapter(this, PlpsDataCenter.getInstance().getPlanList());
////		mListView.setAdapter(mAdapter);
//		//初始化spinner
//		initSpinner();
//	}
//	/*
//	 * 初始化spinner
//	 */
//	private void initSpinner(){
//		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.plps_payroll_spinner, planData);
////		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spinner.setAdapter(spinnerAdapter);
//		spinner.setSelection(0);
//		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent,
//					View view, int position, long id) {
//				// TODO Auto-generated method stub
//				if(selectposition == position)return;
//				selectposition = position;
//				spinner.setSelection(selectposition);
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> paramAdapterView) {
//				// TODO Auto-generated method stub	
//			}
//		});
//	}
//	
////	@Override
////	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
////		super.onItemClick(parent, view, position, id);
////		if (selectposition == position) return;
////		selectposition = position;
////		mAdapter.setSelectedPosition(selectposition);
////	}
//
//	/**
//	 * 下一步
//	 * @param v
//	 */
//	public void btnNextOnclick(View v){
//		if (selectposition > -1) {
//			if (position == 2) {
//				String planNo = (String) PlpsDataCenter.getInstance().getPlanList().get(selectposition).get(Plps.PLANNO);
//				requestAnnuityUserInfo(planNo); return;
//			}
//			BaseHttpEngine.showProgressDialog();
//			requestSystemDateTime(); return;
//		} 
//		BaseDroidApp.getInstanse().showInfoMessageDialog("请选择计划信息");
//	}
//	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode == RESULT_OK) {
//			BaseHttpEngine.showProgressDialogCanGoBack();
//			requestAnnuityPlanList();
//		}
//	}
//	
//	/**
//	 * 查询养老金 --类型固定为 "8"
//	 */
//	private void queryAnnuity(){
//		String planNo = (String) PlpsDataCenter.getInstance().getPlanList().get(selectposition).get(Plps.PLANNO);
//		String startDate = QueryDateUtils.getlastOneMonth(PlpsDataCenter.getInstance().getSysDate());
//		String endDate = QueryDateUtils.getcurrentDate(PlpsDataCenter.getInstance().getSysDate());
//		requestAnnuity("0", planNo, startDate, endDate, "8");
//	}
//	
//	@Override
//	public void annuityCallBack(Object resultObj) {
//		super.annuityCallBack(resultObj);
//		if (StringUtil.isNullOrEmpty(PlpsDataCenter.getInstance().getAnnuityList())) return;
//		String planNo = (String) PlpsDataCenter.getInstance().getPlanList().get(selectposition).get(Plps.PLANNO);
//		startActivity(new Intent(this, AnnuityApplyDetailActivity.class).putExtra(Plps.RECORDNUMBER, recordNumber)
//				.putExtra(Plps.PLANNO, planNo));
//	}
//
//	@Override
//	public void requestSystemDateTimeCallBack(Object resultObj) {
//		super.requestSystemDateTimeCallBack(resultObj);
//		PlpsDataCenter.getInstance().setSysDate(dateTime);
//		queryAnnuity();
//	}
//
//	/**
//	 * 请求养老金个人信息
//	 * @param planNo
//	 */
//	private void requestAnnuityUserInfo(String planNo){
//		BaseHttpEngine.showProgressDialog();
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(Plps.METHODANNUITYUSERINFO);
//		HashMap<String, Object> param = new HashMap<String, Object>();
//		param.put(Plps.PLANNO, planNo);
//		biiRequestBody.setParams(null);
//		HttpManager.requestBii(biiRequestBody, this, "annuityUserInfoCallBack");
//	}
//	
//	@SuppressWarnings("unchecked")
//	public void annuityUserInfoCallBack(Object resultObj) {
//		BaseHttpEngine.dissMissProgressDialog();
//		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
//		if (StringUtil.isNullOrEmpty(result)) return;
//		PlpsDataCenter.getInstance().setAnnuityUserInfo(result);
//		startActivityForResult(new Intent(this, AnnuityPlanApplyConfirmActivity.class),1001);
//	}
//
//	@Override
//	public void annuityPlanListCallBack(Object resultObj) {
//		super.annuityPlanListCallBack(resultObj);
//		mAdapter.setmList(PlpsDataCenter.getInstance().getPlanList());
//	}
//
//}
