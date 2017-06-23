package com.chinamworld.bocmbci.biz.plps.annuity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.chinamworld.bocmbci.biz.plps.adapter.AnnuityAdapter;
import com.chinamworld.bocmbci.biz.plps.adapter.AnnuitySpinnerAdapter;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 业务信息查询
 * @author panwe
 *
 */
public class AnnuityListActivity extends PlpsBaseActivity{
	private final int PLANNAME = 101;
	private final int SERVICETYPE = 102;
	private boolean isShowfirst = true;
//	private View queryView;
	private LinearLayout upLayout;
	private TextView tvStartDate,tvEndDate;
	//选择用户编号和计划名称
	private TextView spinnerPlan;
	//用户编号和计划名称
	private List<String> planData;
	//列表信息
	private List<Map<String, Object>> mPlanList;
	//用户编号和计划名称
	private String planNumber;
	private int selectposition = -1;
	private TextView spServiceType;
	private View mFooterView;
	private TextView tvPlanName,tvPlanType;
	private TextView tvQueryDate;
	private ListView mListView;
	private AnnuityAdapter mAdapter;
	private List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
	private int index = 1;
	private int planNameIndex;
	private int serviceTypeIndex;
	private String startDate,endDate;
	/**查询框*/
	private LinearLayout ll_query_condition;
	/**查询后两个布局*/
	private LinearLayout result_condition;
	private RelativeLayout layout_result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.plps_annuity_condition);
		((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0,0, 0);
		setTitle(PlpsDataCenter.annuity[1]);
		initData();
		setUpQueryView();
		setUpResultView();
		setUpGetMoreView();
	}
	/**
	 * 组装编号和计划名称数据*/
	private void initData(){
		planData = new ArrayList<String>();
		planData.add("请选择");
		mPlanList = PlpsDataCenter.getInstance().getPlanList();
		for(int i=0; i<mPlanList.size(); i++){
			planNumber = (String)(mPlanList.get(i).get(Plps.PLANNO)+"/"+mPlanList.get(i).get(Plps.PLANNAME));
			planData.add(planNumber);
		}
	}
	/**
	 * 初始化查询控件
	 */
	private void setUpQueryView(){
//		queryView = View.inflate(this, R.layout.plps_annuity_condition, null);
		ll_query_condition = (LinearLayout)findViewById(R.id.ll_query_condition);
		result_condition = (LinearLayout)findViewById(R.id.result_condition);
		layout_result = (RelativeLayout)findViewById(R.id.layout_result);
		upLayout = (LinearLayout) findViewById(R.id.ll_up);
		findViewById(R.id.btn_query).setOnClickListener(this);
		findViewById(R.id.btn_onweek).setOnClickListener(this);
		findViewById(R.id.btn_onmonth).setOnClickListener(this);
		findViewById(R.id.btn_threemonth).setOnClickListener(this);
		tvStartDate = (TextView) findViewById(R.id.startdate);
		tvEndDate = (TextView) findViewById(R.id.enddate);
//		spPlanName = (TextView) queryView.findViewById(R.id.spPlanname);
		spinnerPlan = (TextView)findViewById(R.id.spPlanname);
		//初始化spinner
		spinnerPlan.setOnClickListener(this);
//		initSpinner();
		spServiceType = (TextView) findViewById(R.id.spServicetype);
//		spPlanName.setOnClickListener(this);
		spServiceType.setOnClickListener(this);
		upLayout.setOnClickListener(this);
		upLayout.setClickable(false);
		
		tvStartDate.setText(QueryDateUtils.getlastWeek(PlpsDataCenter.getInstance().getSysDate()));
		tvEndDate.setText(QueryDateUtils.getcurrentDate(PlpsDataCenter.getInstance().getSysDate()));
		tvStartDate.setOnClickListener(plpsChooseDateClick);
		tvEndDate.setOnClickListener(plpsChooseDateClick);
	}
//	private void initSpinner(){
//		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.plps_payroll_spinner, planData);
//		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spinnerPlan.setAdapter(spinnerAdapter);
//		spinnerPlan.setSelection(0);
//		spinnerPlan.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent,
//					View view, int position, long id) {
//				// TODO Auto-generated method stub
//				selectposition = position;
//				spinnerPlan.setSelection(selectposition);
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
	 * 初始化结果布局控件
	 */
	private void setUpResultView(){
		tvPlanName = (TextView)findViewById(R.id.planname);
		tvPlanType = (TextView)findViewById(R.id.servicetype);
		tvQueryDate = (TextView)findViewById(R.id.query_date);
		findViewById(R.id.down_layout).setOnClickListener(this);
		mListView = (ListView) findViewById(R.id.listview);
		mAdapter = new AnnuityAdapter(this, mList);
	}
	
	/**
	 * 初始化分页布局
	 */
	private void setUpGetMoreView(){
		mFooterView = inflateView(R.layout.epay_tq_list_more);
		((RelativeLayout) mFooterView.findViewById(R.id.rl_get_more))
				.setBackgroundColor(getResources().getColor(
						R.color.transparent_00));
	}
	
//	@Override
//	public void onWindowFocusChanged(boolean hasFocus) {
//		super.onWindowFocusChanged(hasFocus);
//		if (hasFocus && isShowfirst) {
//			isShowfirst = false;
//			PopupWindowUtils.getInstance().getQueryPopupWindow(
//					queryView,BaseDroidApp.getInstanse().getCurrentAct());
//			PopupWindowUtils.getInstance().showQueryPopupWindowFirst();
//		}
//	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			//收起
		case R.id.ll_up:
//			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
			if(isShowfirst){
				upLayout.setClickable(false);
			}else {
				upLayout.setClickable(true);
				ll_query_condition.setVisibility(View.GONE);
				result_condition.setVisibility(View.VISIBLE);
			}
			break;
			//下拉
		case R.id.down_layout:
//			PopupWindowUtils.getInstance().getQueryPopupWindow(queryView, this);
//			PopupWindowUtils.getInstance().showQueryPopupWindow();
			result_condition.setVisibility(View.GONE);
			ll_query_condition.setVisibility(View.VISIBLE);
			break;
			//一周
		case R.id.btn_onweek:
			if(spinnerPlan.getText().toString().equals("请选择")){
				BaseDroidApp.getInstanse().showInfoMessageDialog("请选择计划");
				return;
			}
			if (spServiceType.getText().toString().equals("请选择")) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("请选择服务类别");
				return;
			}
			startDate = QueryDateUtils.getlastWeek(PlpsDataCenter.getInstance()
					.getSysDate());
			endDate = QueryDateUtils.getcurrentDate(PlpsDataCenter.getInstance().getSysDate());
			tvQueryDate.setText(startDate + "-" + endDate);
			mList.clear();
			queryAnnuity(false);
			break;
			//一个月
		case R.id.btn_onmonth:
			if(spinnerPlan.getText().toString().equals("请选择")){
				BaseDroidApp.getInstanse().showInfoMessageDialog("请选择计划");
				return;
			}
			if (spServiceType.getText().toString().equals("请选择")) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("请选择服务类别");
				return;
			}
			startDate = QueryDateUtils.getlastOneMonth(PlpsDataCenter.getInstance().getSysDate());
			endDate = QueryDateUtils.getcurrentDate(PlpsDataCenter.getInstance().getSysDate());
			tvQueryDate.setText(startDate + "-" + endDate);
			mList.clear();
			queryAnnuity(false);
			break;
			//三个月
		case R.id.btn_threemonth:
			if(spinnerPlan.getText().toString().equals("请选择")){
				BaseDroidApp.getInstanse().showInfoMessageDialog("请选择计划");
				return;
			}
			if (spServiceType.getText().toString().equals("请选择")) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("请选择服务类别");
				return;
			}
			startDate = QueryDateUtils.getlastThreeMonth(PlpsDataCenter.getInstance().getSysDate());
			endDate = QueryDateUtils.getcurrentDate(PlpsDataCenter.getInstance().getSysDate());
			tvQueryDate.setText(startDate + "-" + endDate);
			mList.clear();
			queryAnnuity(false);
			break;
			//查询
		case R.id.btn_query:
			if(spinnerPlan.getText().toString().equals("请选择")){
				BaseDroidApp.getInstanse().showInfoMessageDialog("请选择计划");
				return;
			}
			if (spServiceType.getText().toString().equals("请选择")) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("请选择服务类别");
				return;
			}
			startDate = tvStartDate.getText().toString();
			endDate = tvEndDate.getText().toString();
			tvQueryDate.setText(startDate + "-" + endDate);
			mList.clear();
			if (queryCheckt(startDate, endDate)) {
				queryAnnuity(false);
			}
			break;
			//选择计划
		case R.id.spPlanname:
			List<String> mList = PlpsUtils.initSpinnerPlanData(PlpsDataCenter.getInstance().getPlanList(), Plps.PLANNO,Plps.PLANNAME,null);
			createDialog(mList, "计划编号",PLANNAME,planNameIndex);
			break;
			//服务类型
		case R.id.spServicetype:
			createDialog(PlpsDataCenter.queryType, "服务类别",SERVICETYPE,serviceTypeIndex);
			break;
		}
	}
	/**
	 * 查询日期校验
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public boolean queryCheckt(String startDate,String endDate){
		String dateTime = PlpsDataCenter.getInstance().getSysDate();
		// 起始日期不能早于系统当前日期一年前
		if (!QueryDateUtils.compareDateOneYear(startDate, dateTime)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("起始日期需要在系统当前日期一年以内");
			return false;
		}
		// 结束日期在服务器日期之前
		if (!QueryDateUtils.compareDate(endDate, dateTime)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("结束日期需要早于系统当前日期");
			return false;
		}
		// 开始日期在结束日期之前
		if (!QueryDateUtils.compareDate(startDate, endDate)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("您输入的开始日期有误，请重新输入开始日期");
			return false;
		}
		// 起始日期与结束日期最大间隔为三个自然月
		if (!QueryDateUtils.compareDateThree(startDate, endDate)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("查询跨度最大为三个月");
			return false;
		}
		return true;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		super.onItemClick(parent, view, position, id);
		BaseDroidApp.getInstanse().dismissMessageDialog();
		switch (parent.getId()) {
		case PLANNAME:
			planNameIndex = position;
			selectposition = position;
			List<Map<String, Object>> planList = PlpsDataCenter.getInstance().getPlanList();
			String planno = (String)planList.get(position).get(Plps.PLANNO);
			String planname = (String)planList.get(position).get(Plps.PLANNAME);
			spinnerPlan.setText(planno+"/"+planname);
			break;
		case SERVICETYPE:
			serviceTypeIndex = position;
			spServiceType.setText(PlpsDataCenter.queryType.get(position));
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
	
	/**
	 * 添加更多按钮
	 * @param totalCount
	 */
	private void addFooterView(String totalCount) {
		if (Integer.valueOf(totalCount) > mList.size()) {
			if (mListView.getFooterViewsCount() <= 0) {
				mListView.addFooterView(mFooterView);
			}
			mListView.setClickable(true);
		} else {
			if (mListView.getFooterViewsCount() > 0) {
				mListView.removeFooterView(mFooterView);
			}
		}
		mFooterView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				index = index+Integer.valueOf(ConstantGloble.FOREX_PAGESIZE);
				index = index+1;
				queryAnnuity(true);
			}
		});
	}
	
	/**
	 * 查询养老金
	 */
	private void queryAnnuity(Boolean referesh){
		BaseHttpEngine.showProgressDialog();
		String planNo = (String) PlpsDataCenter.getInstance().getPlanList().get(selectposition).get(Plps.PLANNO);
		upLayout.setClickable(false);
		if(referesh){
			requestAnnuity(String.valueOf(index), planNo, startDate, endDate, String.valueOf(serviceTypeIndex+1));
		}else {
			index = 1;
			requestAnnuity(String.valueOf(index), planNo, startDate, endDate, String.valueOf(serviceTypeIndex+1));
		}
		
	}
	
	@Override
	public void annuityCallBack(Object resultObj) {
//		super.customerInfoCallBack(resultObj);
		super.annuityCallBack(resultObj);
		isShowfirst = false;
		List<Map<String, Object>> list = PlpsDataCenter.getInstance().getAnnuityList();
		if (StringUtil.isNullOrEmpty(list)) {
			BaseHttpEngine.dissMissProgressDialog();
			layout_result.setVisibility(View.GONE);
			upLayout.setClickable(false);
			BaseDroidApp.getInstanse().showInfoMessageDialog("没有符合查询条件的结果"); return;
		}
		BaseHttpEngine.dissMissProgressDialog();
//		PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
		findViewById(R.id.ll_query_condition).setVisibility(View.GONE);
		findViewById(R.id.layout_result).setVisibility(View.VISIBLE);
		findViewById(R.id.result_condition).setVisibility(View.VISIBLE);
		tvPlanName.setText(mPlanList.get(selectposition).get(Plps.PLANNO)+"/"+mPlanList.get(selectposition).get(Plps.PLANNAME));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,tvPlanName);
		tvPlanType.setText(spServiceType.getText().toString());
		upLayout.setClickable(true);
		mList.addAll(list);
		addFooterView(recordNumber);
		mAdapter.setData(mList,String.valueOf(serviceTypeIndex+1));
		mListView.setAdapter(mAdapter);
	}
}
