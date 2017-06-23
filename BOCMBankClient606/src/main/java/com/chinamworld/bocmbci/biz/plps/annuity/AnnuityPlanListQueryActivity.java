package com.chinamworld.bocmbci.biz.plps.annuity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
import com.chinamworld.bocmbci.biz.plps.adapter.AnnuitySpinnerAdapter;
import com.chinamworld.bocmbci.biz.plps.adapter.PlpsCommAdapter;
import com.chinamworld.bocmbci.biz.plps.adapter.PlpsCommAdapter.Count;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
/*
 * 申请结果查询
 * zxj
 */
public class AnnuityPlanListQueryActivity extends PlpsBaseActivity{
	private int position;
	//用户编号和计划名称
	private List<String> planData;
	//列表信息
	private List<Map<String, Object>> mList;
	//用户编号
	private String planNumber;
	private TextView spinnerText;
	private int selectposition = -1;
	//结果布局
	private RelativeLayout resultCondition;
	//显示布局
	private ListView resultListView;
	//显示的adapter
	private PlpsCommAdapter mAdapter;
	//结果显示数据
	private List<Map<String, Object>> resultList= new ArrayList<Map<String,Object>>();
	private int index = 1;
	private View mFooterView;
	private final int PLANNAME = 101;
	private int planNameIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		position = getIntent().getIntExtra("p", -1);
		setTitle(PlpsDataCenter.annuity[position]);
		mRightButton.setVisibility(View.VISIBLE);
		((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0,0, 0);
		cityAdress.setVisibility(View.GONE);
//		initData();
		setUpView();
	}
//	private void initData(){
//		mRightButton.setVisibility(View.VISIBLE);
//		cityAdress.setVisibility(View.GONE);
//		planData = new ArrayList<String>();
//		mList = PlpsDataCenter.getInstance().getPlanList();
//		for(int i=0; i<mList.size(); i++){
//			planNumber = (String)(mList.get(i).get(Plps.PLANNO)+"/"+mList.get(i).get(Plps.PLANNAME));
//			planData.add(planNumber);
//		}
////		setUpView();
//	}
	private void setUpView(){
		inflateLayout(R.layout.plps_annuity_plan_query);
		spinnerText = (TextView)findViewById(R.id.spinner_plan);
		spinnerText.setOnClickListener(this);
		resultCondition = (RelativeLayout)findViewById(R.id.result_condition);
		resultListView = (ListView)findViewById(R.id.listview);
		resultListView.setOnItemClickListener(this);
		mAdapter = new PlpsCommAdapter(this,resultList,Plps.BUSSINESSTYPE, Plps.APPLYDATE, Plps.RESULT,Count.TWO);
		//初始化spinner
//		initSpinner();
		setUpGetMoreView();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.spinner_plan:
			List<String> mList = PlpsUtils.initSpinnerPlanData(PlpsDataCenter.getInstance().getPlanList(), Plps.PLANNO,Plps.PLANNAME,null);
			createDialog(mList, "计划编号",PLANNAME,planNameIndex);
			break;
		}
	}
	/**
	 * 创建spinner弹窗框
	 * @param list
	 * @param title
	 * @param vId
	 * @param position
	 */
	private void createDialog(List<String> list,String title,int vId,int position){
		ListView mListView = new ListView(this);
		mListView.setId(vId);
		mListView.setFadingEdgeLength(0);
		mListView.setOnItemClickListener(this);
		mListView.setScrollingCacheEnabled(false);
		AnnuitySpinnerAdapter mAdapter = new AnnuitySpinnerAdapter(this, list,position);
		mListView.setAdapter(mAdapter);
		BaseDroidApp.getInstanse().showSpinnerDialog(mListView, title);
	}
	/**初始化更多an按钮布局*/
	private void setUpGetMoreView(){
		mFooterView = inflateView(R.layout.epay_tq_list_more);
		((RelativeLayout) mFooterView.findViewById(R.id.rl_get_more))
				.setBackgroundColor(getResources().getColor(
						R.color.transparent_00));
	}
	
//	private void initSpinner(){
//		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.plps_payroll_spinner, planData);
//		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spinner.setAdapter(spinnerAdapter);
//		spinner.setSelection(0);
//		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent,
//					View view, int position, long id) {
//				// TODO Auto-generated method stub
//				if(selectposition == position) return;
//				selectposition = position;
//				spinner.setSelection(selectposition);
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> paramAdapterView) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//	}
	/**
	 * 查询按钮*/
	public void btnNextOnclick(View v){
		if(selectposition > -1){
			BaseHttpEngine.showProgressDialog();
			resultList.clear();
			requestSystemDateTime(); return;
		}
		BaseDroidApp.getInstanse().showInfoMessageDialog("请选择选择计划");
	}
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestSystemDateTimeCallBack(resultObj);
		PlpsDataCenter.getInstance().setSysDate(dateTime);
		queryAnnuity();
	}
	/**
	 * 查询养老金 --类型固定为 "8"
	 */
	private void queryAnnuity(){
		String planNo = (String) PlpsDataCenter.getInstance().getPlanList().get(selectposition).get(Plps.PLANNO);
		String startDate = QueryDateUtils.getlastOneMonth(PlpsDataCenter.getInstance().getSysDate());
		String endDate = QueryDateUtils.getcurrentDate(PlpsDataCenter.getInstance().getSysDate());
		requestAnnuity(String.valueOf(index), planNo, startDate, endDate, "8");
	}
	@Override
	public void annuityCallBack(Object resultObj) {
		super.annuityCallBack(resultObj);
		if (StringUtil.isNullOrEmpty(PlpsDataCenter.getInstance().getAnnuityList())) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("没有符合查询条件的结果"); return;
		}
//		String planNo = (String) PlpsDataCenter.getInstance().getPlanList().get(selectposition).get(Plps.PLANNO);
		resultCondition.setVisibility(View.VISIBLE);
//		index = index+Integer.valueOf(ConstantGloble.FOREX_PAGESIZE);
		resultList.addAll(PlpsDataCenter.getInstance().getAnnuityList());
		addFooterView(recordNumber);
		mAdapter.setData(resultList);
		resultListView.setAdapter(mAdapter);
	}
	/**添加更多按钮*/
	private void addFooterView(String totalCount){
		if(Integer.valueOf(totalCount) > resultList.size()){
			if(resultListView.getFooterViewsCount()<=0){
				resultListView.addFooterView(mFooterView);
			}
			resultListView.setClickable(true);
		}else {
			if(resultListView.getFooterViewsCount() >0){
				resultListView.removeFooterView(mFooterView);
			}
		}
		mFooterView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				index = index+1;
				queryAnnuity();
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		super.onItemClick(parent, view, position, id);
		BaseDroidApp.getInstanse().dismissMessageDialog();
		switch (parent.getId()) {
		case PLANNAME:
			planNameIndex = position;
			selectposition = position;
			List<Map<String, Object>> planList = PlpsDataCenter.getInstance()
					.getPlanList();
			String planno = (String) planList.get(position).get(Plps.PLANNO);
			String planname = (String) planList.get(position)
					.get(Plps.PLANNAME);
			spinnerText.setText(planno + "/" + planname);
			break;
		default:
			PlpsDataCenter.getInstance().setAnnuityListDetail(
					resultList.get(position));
			startActivityForResult(new Intent(this,
					AnnuityApplyDetailInfoActivity.class), 1001);
			break;
		}
	}
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode == RESULT_OK) {
//			setResult(RESULT_OK);
//			finish();
//		}
//	}
}
