package com.chinamworld.bocmbci.biz.plps.liveservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IHttpCallBack;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.adapter.AnnuitySpinnerAdapter;
import com.chinamworld.bocmbci.biz.plps.adapter.PlpsCommAdapter;
import com.chinamworld.bocmbci.biz.plps.adapter.PlpsCommAdapter.Count;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.PublicTools;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的民生列表
 * @author panwe
 *
 */
public class CommServiceActivity extends PlpsBaseActivity{
	private boolean isShowfirst = true;
//	private View queryView;
	private View mFooterView;
	private LinearLayout upLayout;
	private TextView tvStartDate,tvEndDate,tvQueryDate;
	private ListView mListView;
	private PlpsCommAdapter mAdapter;
	private List<Map<String, Object>> mResultList = new ArrayList<Map<String, Object>>();
	private int index = 0;
	private String menusId;
	private String startDate,endDate;
	//服务类别
	private TextView serviceCategory;
	private TextView serviceType;
	private final int SERVICECATEGORY=101;
	private int serviceCategoryIndex;
	/**是否是第一次*/
	private Boolean isFirst = true;
	//是否进入过详情页面
//	private Boolean isFirstDetail = true;
	/**选择点击位置*/
	private int selectionPosition;
	//是否需要刷新
	private Boolean refresht=false;
	//记录查询条数
	private String recordNumber=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		inflateLayout(R.layout.plps_live_list);
		setTitle(R.string.plps_payment_query);
		setLeftSelectedPosition("plps_2");
		((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0,0, 0);
//		menusId = getIntent().getStringExtra(Plps.MENUSID);
		BaseHttpEngine.showProgressDialog();
		requestSystemDateTime();

//		setUpResultView();
//		setUpGetMoreView();
	}
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestSystemDateTimeCallBack(resultObj);
		PlpsDataCenter.getInstance().setSysDate(dateTime);
		BaseHttpEngine.dissMissProgressDialog();
//		if(isFirstDetail){
			setUpQueryView();
			setUpResultView();
			setUpGetMoreView();
//		}else {
//			findViewById(R.id.layout_queryviews).setVisibility(View.VISIBLE);
//		}
	}

	/**
	 * 初始化查询控件
	 */
	private void setUpQueryView(){
//		queryView = View.inflate(this, R.layout.plps_live_queryview, null);
		inflateLayout(R.layout.plps_live_list_activity);
		upLayout = (LinearLayout) findViewById(R.id.ll_up);
		findViewById(R.id.btn_query).setOnClickListener(this);
		findViewById(R.id.btn_onweek).setOnClickListener(this);
		findViewById(R.id.btn_onmonth).setOnClickListener(this);
		findViewById(R.id.btn_threemonth).setOnClickListener(this);
		findViewById(R.id.btn_halfyear).setOnClickListener(this);
		findViewById(R.id.btn_oneyear).setOnClickListener(this);
		tvStartDate = (TextView) findViewById(R.id.startdate);
		tvEndDate = (TextView) findViewById(R.id.enddate);
		serviceCategory = (TextView)findViewById(R.id.spPlanname);
		serviceCategory.setOnClickListener(this);
		upLayout.setOnClickListener(this);
		upLayout.setClickable(false);

		tvStartDate.setText(QueryDateUtils.getlastWeek(PlpsDataCenter.getInstance().getSysDate()));
		tvEndDate.setText(QueryDateUtils.getcurrentDate(PlpsDataCenter.getInstance().getSysDate()));
		tvStartDate.setOnClickListener(plpsChooseDateClick);
		tvEndDate.setOnClickListener(plpsChooseDateClick);
	}

	/*OnClickListener serviceCategoryListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			List<String> mList = PlpsUtils.initSpinnerData(PlpsDataCenter
					.getInstance().getLiveMenus(), Plps.MENUSNAME, null);
			createDialog(mList, "全部", SERVICECATEGORY, serviceCategoryIndex);
		}
	};*/
	/**
	 * 初始化结果布局控件
	 */
	private void setUpResultView(){
		tvQueryDate = (TextView)findViewById(R.id.query_date);
		serviceType = (TextView)findViewById(R.id.plps_servicetext);
		findViewById(R.id.down_layout).setOnClickListener(this);
		mListView = (ListView) findViewById(R.id.listview);
		mListView.setOnItemClickListener(this);
		findViewById(R.id.btn_onweek).performClick();
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
		//服务类别
		case R.id.spPlanname:
			List<String> mList = PublicTools.getSpinnerDataWithDefaultValue(PlpsDataCenter.getInstance().getLiveMenus(), Plps.MENUSNAME,Plps.SP_ALLTXT);
//			PlpsUtils.initSpinnerView(this, mSpProvice, PlpsUtils.initSpinnerData(
//					provinceList, Plps.PRVCDISPNAME,Plps.SP_DEFUALTTXT));
			createDialog(mList,"服务类别",SERVICECATEGORY,serviceCategoryIndex);
			break;
			//收起
		case R.id.ll_up:
//			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
			if(isFirst){
				upLayout.setClickable(false);
			}else {
				upLayout.setClickable(true);
//				findViewById(R.id.layout_queryviews).setVisibility(View.GONE);
				findViewById(R.id.ll_query_condition).setVisibility(View.GONE);
				findViewById(R.id.result_condition).setVisibility(View.VISIBLE);
			}
			break;
			//下拉
		case R.id.down_layout:
//			PopupWindowUtils.getInstance().getQueryPopupWindow(queryView, this);
//			PopupWindowUtils.getInstance().showQueryPopupWindow();
//			findViewById(R.id.layout_queryviews).setVisibility(View.VISIBLE);
			findViewById(R.id.result_condition).setVisibility(View.GONE);
			findViewById(R.id.ll_query_condition).setVisibility(View.VISIBLE);
			//mResultList.clear();
			break;
			//一周
		case R.id.btn_onweek:
			upLayout.setClickable(false);
			startDate = QueryDateUtils.getlastWeek(PlpsDataCenter.getInstance().getSysDate());
			endDate = QueryDateUtils.getcurrentDate(PlpsDataCenter.getInstance().getSysDate());
			tvQueryDate.setText(startDate + "-" + endDate);
			mResultList.clear();
			requestHistoryRecods(true);
			break;
			//一个月
		case R.id.btn_onmonth:
			upLayout.setClickable(false);
			startDate = QueryDateUtils.getlastOneMonth(PlpsDataCenter.getInstance().getSysDate());
			endDate = QueryDateUtils.getcurrentDate(PlpsDataCenter.getInstance().getSysDate());
			tvQueryDate.setText(startDate + "-" + endDate);
			mResultList.clear();
			requestHistoryRecods(true);
			break;
			//三个月
		case R.id.btn_threemonth:
			upLayout.setClickable(false);
			startDate = QueryDateUtils.getlastThreeMonth(PlpsDataCenter.getInstance().getSysDate());
			endDate = QueryDateUtils.getcurrentDate(PlpsDataCenter.getInstance().getSysDate());
			tvQueryDate.setText(startDate + "-" + endDate);
			mResultList.clear();
			requestHistoryRecods(true);
			break;
			//半年
		case R.id.btn_halfyear:
			upLayout.setClickable(false);
			startDate = QueryDateUtils.getsysDateOneHalfYear(PlpsDataCenter.getInstance().getSysDate());
			endDate= QueryDateUtils.getcurrentDate(PlpsDataCenter.getInstance().getSysDate());
			tvQueryDate.setText(startDate+"-"+endDate);
			mResultList.clear();
			requestHistoryRecods(true);
			break;
			//一年
		case R.id.btn_oneyear:
			upLayout.setClickable(false);
			startDate = QueryDateUtils.getsysDateOneYear(PlpsDataCenter.getInstance().getSysDate());
			endDate = QueryDateUtils.getcurrentDate(PlpsDataCenter.getInstance().getSysDate());
			tvQueryDate.setText(startDate+"-"+endDate);
			mResultList.clear();
			requestHistoryRecods(true);
			break;
			//查询
		case R.id.btn_query:
			upLayout.setClickable(false);
			startDate = tvStartDate.getText().toString();
			endDate = tvEndDate.getText().toString();
			tvQueryDate.setText(startDate + "-" + endDate);
			mResultList.clear();
			if (queryChecks(startDate, endDate)) {
				requestHistoryRecods(true);
			}
			break;
		}

	}
	/**
	 * 查询日期校验
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public boolean queryChecks(String startDate,String endDate){
		String dateTime = PlpsDataCenter.getInstance().getSysDate();
		// 起始日期不能早于系统当前日期一年前
		if (!QueryDateUtils.compareDateOneYear(startDate, dateTime)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_check_start_enddate));
			return false;
		}
		// 结束日期在服务器日期之前
		if (!QueryDateUtils.compareDate(endDate, dateTime)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_check_enddate));
			return false;
		}
		// 开始日期在结束日期之前
		if (!QueryDateUtils.compareDate(startDate, endDate)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_query_errordate));
			return false;
		}
		// 起始日期与结束日期最大间隔为三个自然月
//		if (!QueryDateUtils.compareDateThree(startDate, endDate)) {
//			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_check_start_end_date));
//			return false;
//		}
		return true;
	}

	/**
	 * 创建spinner弹窗框
	 */
	private void createDialog(List<String> list,String title, int vId, int position){
		ListView listView = new ListView(this);
		listView.setId(vId);
		listView.setFadingEdgeLength(0);
		listView.setOnItemClickListener(serviceCategoryItemListener);
		listView.setScrollbarFadingEnabled(false);
		AnnuitySpinnerAdapter adapter = new AnnuitySpinnerAdapter(this, list, position);
		listView.setAdapter(adapter);
		BaseDroidApp.getInstanse().showSpinnerDialog(listView, title);
	}
	public OnItemClickListener serviceCategoryItemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			BaseDroidApp.getInstanse().dismissMessageDialog();
			serviceCategoryIndex = position;
			serviceCategory.setText(PublicTools.getSpinnerDataWithDefaultValue(
					PlpsDataCenter.getInstance().getLiveMenus(),
					Plps.MENUSNAME, Plps.SP_ALLTXT).get(position));
			if(position>0){
				menusId = (String)PlpsDataCenter.getInstance().getLiveMenus().get(position-1).get(Plps.MENUSID);
			}

		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		super.onItemClick(parent, view, position, id);
		selectionPosition = position;
		PlpsDataCenter.getInstance().setHistoryRecodDetail(mResultList.get(position));
		String flowFileId = (String)mResultList.get(position).get(Plps.FLOWFILEID);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Plps.FLOWFILEID, flowFileId);
		BaseHttpEngine.showProgressDialog();
		requestHttp(Plps.PSNPLPSGETHISVALFLAG, "requestPsnPlpsGetHisValFlageCallBack", params, false);
	}
	/**判断某缴费记录对应的缴费项目是否在本终端可用回调*/
	@SuppressWarnings("unchecked")
	public void requestPsnPlpsGetHisValFlageCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		if(StringUtil.isNullOrEmpty(resultMap)){
			return;
		}
		String valFlag = (String)resultMap.get("valFlag");
		startActivityForResult(new Intent(this, CommServiceDetailActivity.class)
			.putExtra("valFlag", valFlag), 1001);
	}

	/**
	 * 添加更多按钮
	 * @param totalCount
	 */
	private void addFooterView(String totalCount) {
		if (Integer.valueOf(totalCount) > mResultList.size()) {
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
				requestHistoryRecods(false);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {

		}else if (resultCode == 10111) {
//			isFirstDetail = false;
			/**将在缴费详情页面添加常用成功的服务的isOftenUse改为false*/
			setFlowisOftenUse();
		}else if (resultCode == 111) {
			mResultList.remove(selectionPosition);
			mAdapter.notifyDataSetChanged();
			if(mResultList.size() ==0){
				findViewById(R.id.ll_query_condition).setVisibility(View.VISIBLE);
				findViewById(R.id.result_condition).setVisibility(View.GONE);
				findViewById(R.id.layout_result).setVisibility(View.GONE);
				upLayout.setClickable(false);
			}
		}
	}
	/**将在缴费详情页面添加常用成功的服务的isOftenUse改为false*/
	private void setFlowisOftenUse(){
		for(int i=0; i<mResultList.size(); i++){
			String flowFileId = (String)mResultList.get(selectionPosition).get(Plps.FLOWFILEID);
			String flowFileIdt = (String)mResultList.get(i).get(Plps.FLOWFILEID);
			if(flowFileId.equals(flowFileIdt)){
				mResultList.get(i).put(Plps.ISOFTENUSE, "1");
			}
		}
	}

	/**
	 * 请求常用缴费列表
	 * @param refresh
	 */
	private void requestHistoryRecods(final boolean refresh){
		if(refresh == true) {//如果需要刷新，则说明不是点击更多查询，因此当前下标清零,数据清空
			index = 0;
			mResultList.clear();
		}
		if(refresh == false){
			sendHistoryRecodsRequest(refresh);
			return;
		}
		requestCommConversationId(new IHttpCallBack(){

			@Override
			public void requestHttpSuccess(Object result) {
				// TODO Auto-generated method stub
				sendHistoryRecodsRequest(refresh);
			}});
		}

	private void sendHistoryRecodsRequest(boolean refresh){
		//如果需要刷新，则说明不是点击更多查询，因此当前下标清零
		if(refresh == true) {
			index = 0;
			refresht = true;
		}else {
			refresht=false;
		}
		isFirst = false;
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.METHODHISTORYRECORDS);
		biiRequestBody.setConversationId((String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Plps.PAGESIZE, ConstantGloble.FOREX_PAGESIZE);
		params.put(Plps.REFRESH, String.valueOf(refresh));
		params.put(Plps.CURRENTINDEX, String.valueOf(index));
		if(serviceCategory.getText().toString().equals("全部")){

		}else {
			params.put(Plps.MUNUID, menusId);
		}
		params.put(Plps.STARTDATE, startDate);
		params.put(Plps.ENDDATE, endDate);
		biiRequestBody.setParams(params);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, CommServiceActivity.this, "historyRecordsCallBack");

	}

	@SuppressWarnings("unchecked")
	public void historyRecordsCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		if (refresht) {
			recordNumber = (String) result.get(Plps.RECORDNUMBER);
		}
		List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(Plps.HISTORYRECORDSLIST);
		if (StringUtil.isNullOrEmpty(list)) {
			BiiHttpEngine.dissMissProgressDialog();
			findViewById(R.id.layout_result).setVisibility(View.GONE);
			upLayout.setClickable(false);
			BaseDroidApp.getInstanse().showInfoMessageDialog("没有符合查询条件的结果"); return;
		}
		BiiHttpEngine.dissMissProgressDialog();
		PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
		findViewById(R.id.ll_query_condition).setVisibility(View.GONE);
		findViewById(R.id.result_condition).setVisibility(View.VISIBLE);
		findViewById(R.id.layout_result).setVisibility(View.VISIBLE);
		serviceType.setText(serviceCategory.getText().toString());
		index = index+Integer.valueOf(ConstantGloble.FOREX_PAGESIZE);
		upLayout.setClickable(true);
		mResultList.addAll(list);
		addFooterView(recordNumber);
//		findViewById(R.id.ll_query_condition).setVisibility(View.GONE);
		if (mAdapter == null) {
			mAdapter = new PlpsCommAdapter(this, mResultList, Plps.TRANSTIME, Plps.MENUNAME, Plps.CITYNAME, Count.TWO);
			mListView.setAdapter(mAdapter);
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}
		mAdapter.setData(mResultList);

	}
}