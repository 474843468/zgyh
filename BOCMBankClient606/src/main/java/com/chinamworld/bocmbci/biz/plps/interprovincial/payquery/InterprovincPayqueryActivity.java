package com.chinamworld.bocmbci.biz.plps.interprovincial.payquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IHttpCallBack;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.adapter.AnnuitySpinnerAdapter;
import com.chinamworld.bocmbci.biz.plps.adapter.InterprovincPayqueryAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.PublicTools;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
/*
 * 跨省异地交通违法罚款 查询页面
 * @author zxj
 */
public class InterprovincPayqueryActivity extends PlpsBaseActivity{
	/**收起按钮*/
	private LinearLayout upLayout;
	/**起始日期*/
	private TextView tvStartDate;
	/**结束日期*/
	private TextView tvEndDate;
	/**缴费帐号*/
	private TextView paymentAmount;
	/**结果列表*/
	private ListView mListView;
	/**更多布局*/
	private View mFooterView;
	/**标识*/
	private int ACCOUNTID = 101;
	/**选择的position*/
	private int accPosition=0;
	/**是否第一次进入标识*/
	private Boolean isFirst = true;
	/**查询起始日期和结束日期*/
	private String startDate,endDate;
	/**查询结果页面显示时间*/
	private TextView tvQueryDate;
	/**查询结果页面缴费帐号*/
	private TextView tvPaymentAcc;
	/**结果查询信息*/
	private List<Map<String, Object>> mResultList = new ArrayList<Map<String,Object>>();
	/**选择的缴费帐号id*/
	private String accountId;
	/**索引*/
	private int index = 0;
	/**查询结果是否需要刷新*/
	private Boolean refresht;
	/**记录查询条数*/
	private String recordNumber = null;
	/**选择的缴费帐号*/
	private String accountNumber;
	/**查询结果adapter*/
	private InterprovincPayqueryAdapter mAdapter;
	/**进入详情 点击 流水号*/
	private String traceNo;
	/**进入详情 点击 决定书编号*/
	private String decisionNo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(R.string.plps_interprov_paymentqu_undo);
		setUpView();
		setUpResultView();
		setUpGetMoreView();
	}
	/**初始化查询框布局*/
	private void setUpView(){
		inflateLayout(R.layout.plps_interprov_query);
		upLayout = (LinearLayout)findViewById(R.id.ll_up);
		findViewById(R.id.btn_query).setOnClickListener(this);
		findViewById(R.id.btn_onweek).setOnClickListener(this);
		findViewById(R.id.btn_onmonth).setOnClickListener(this);
		findViewById(R.id.btn_threemonth).setOnClickListener(this);
		tvStartDate = (TextView)findViewById(R.id.startdate);
		tvEndDate = (TextView)findViewById(R.id.enddate);
		paymentAmount = (TextView)findViewById(R.id.spaymount);
		paymentAmount.setText(PublicTools.getSpinnerData(
				PlpsDataCenter.getInstance().getAcctList(), Comm.ACCOUNTNUMBER).get(0));
		accountId = (String)PlpsDataCenter.getInstance().getAcctList().get(0).get(Comm.ACCOUNT_ID);
		accountNumber = (String)PlpsDataCenter.getInstance().getAcctList().get(0).get(Comm.ACCOUNTNUMBER);
		
		upLayout.setOnClickListener(this);
		tvStartDate.setOnClickListener(this);
		tvEndDate.setOnClickListener(this);
		paymentAmount.setOnClickListener(this);
		
		tvStartDate.setText(QueryDateUtils.getcurrentDate(PlpsDataCenter.getInstance().getSysDate()));
		tvEndDate.setText(QueryDateUtils.getcurrentDate(PlpsDataCenter.getInstance().getSysDate()));
		tvStartDate.setOnClickListener(plpsChooseDateClick);
		tvEndDate.setOnClickListener(plpsChooseDateClick);
	}
	/**初始化结果布局*/
	private void setUpResultView(){
		tvQueryDate = (TextView)findViewById(R.id.query_date);
		tvPaymentAcc = (TextView)findViewById(R.id.plps_paymentacc);
		findViewById(R.id.down_layout).setOnClickListener(this);
		mListView = (ListView) findViewById(R.id.listview);
		mListView.setOnItemClickListener(this);
	}
	/**
	 * 初始化更不多按钮布局*/
	private void setUpGetMoreView(){
		mFooterView = inflateView(R.layout.epay_tq_list_more);
		((RelativeLayout) mFooterView.findViewById(R.id.rl_get_more))
				.setBackgroundColor(getResources().getColor(
						R.color.transparent_00));
	}
	@Override
	public void onClick(View v) {
		
		super.onClick(v);
		//时间校验在PlpsBaseActivity queryCheck
		switch (v.getId()) {
		//缴费帐号
		case R.id.spaymount:
			List<String> mList = PublicTools.getSpinnerData(PlpsDataCenter.getInstance().getAcctList(), Comm.ACCOUNTNUMBER);
			createDialog(mList,"请选择",ACCOUNTID,accPosition);
			break;
		//收起
		case R.id.ll_up:
			if(isFirst){
				upLayout.setClickable(false);
			}else{
				upLayout.setClickable(true);
				findViewById(R.id.ll_query_condition).setVisibility(View.GONE);
				findViewById(R.id.result_condition).setVisibility(View.VISIBLE);
			}
			break;
	    //下拉	
		case R.id.down_layout:
			findViewById(R.id.ll_query_condition).setVisibility(View.VISIBLE);
			findViewById(R.id.result_condition).setVisibility(View.GONE);
			break;
		//最近一周
		case R.id.btn_onweek:
			upLayout.setClickable(false);
			startDate = QueryDateUtils.getlastWeek(PlpsDataCenter.getInstance().getSysDate());
			endDate = QueryDateUtils.getcurrentDate(PlpsDataCenter.getInstance().getSysDate());
			tvQueryDate.setText(startDate+"-"+endDate);
			mResultList.clear();
			sendPsnTrafficFinesTransListQuery(true);
			break;
		//最近一月
		case R.id.btn_onmonth:
			upLayout.setClickable(false);
			startDate = QueryDateUtils.getlastOneMonth(PlpsDataCenter.getInstance().getSysDate());
			endDate = QueryDateUtils.getcurrentDate(PlpsDataCenter.getInstance().getSysDate());
			tvQueryDate.setText(startDate + "-" + endDate);
			mResultList.clear();
			sendPsnTrafficFinesTransListQuery(true);
			break;
		//最近三月
		case R.id.btn_threemonth:
			upLayout.setClickable(false);
			startDate = QueryDateUtils.getlastThreeMonth(PlpsDataCenter.getInstance().getSysDate());
			endDate = QueryDateUtils.getcurrentDate(PlpsDataCenter.getInstance().getSysDate());
			tvQueryDate.setText(startDate+ "-"+endDate);
			mResultList.clear();
			sendPsnTrafficFinesTransListQuery(true);
			break;
		//查询
		case R.id.btn_query:
			upLayout.setClickable(false);
			startDate = tvStartDate.getText().toString();
			endDate = tvEndDate.getText().toString();
			tvQueryDate.setText(startDate + "-"+endDate);
			mResultList.clear();
			if(queryCheck(startDate, endDate)){
				sendPsnTrafficFinesTransListQuery(true);
			}
			break;
		default:
			break;
		}
	}
	/**跨省异地交通违法罚款缴纳交易列表查询*/
	private void sendPsnTrafficFinesTransListQuery(final Boolean refresh){
		if(StringUtil.isNullOrEmpty(accountNumber)){
			BaseDroidApp.getInstanse().showMessageDialog("请选择缴费帐号", new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					BaseDroidApp.getInstanse().dismissMessageDialog();
				}
			});
			return;
		}
		if(refresh == true){
			index = 0;
			mResultList.clear();
			if(mAdapter != null){
				mAdapter.notifyDataSetChanged();
				mListView.removeFooterView(mFooterView);
			}
		}else if (refresh == false) {
			BaseHttpEngine.showProgressDialog();
			requestPsnTrafficFinesTransListQuery(refresh);
			return;
		}
		BaseHttpEngine.showProgressDialog();
		requestCommConversationId(new IHttpCallBack(){
			@Override
			public void requestHttpSuccess(Object result) {
				// TODO Auto-generated method stub
				requestPsnTrafficFinesTransListQuery(refresh);
				
			}});
	}
	
	/**跨省异地交通违法罚款缴纳交易列表查询请求*/
	private void requestPsnTrafficFinesTransListQuery(Boolean refresh){
		//如果需要刷新，则说明不是点击更多查询，因此当前下标清零
		if (refresh == true) {
			index = 0;
			refresht = true;
		} else {
			refresht = false;
		}
		isFirst = false;
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.PSNTRAFFICFINESTRANSLISTQUERY);
		Map<String, Object> params = new HashMap<String, Object>();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		params.put(Plps.STARTDATE, startDate);
		params.put(Plps.ENDDATE, endDate);
		params.put(Comm.ACCOUNT_ID, accountId);
		params.put(Plps.REFRESH, String.valueOf(refresh));
		params.put(Plps.PAGESIZE, ConstantGloble.FOREX_PAGESIZE);
		params.put(Plps.CURRENTINDEX, String.valueOf(index));
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnTrafficFinesTransListQueryCallBack");
	}
	
	@SuppressWarnings("unchecked")
	public void requestPsnTrafficFinesTransListQueryCallBack(Object resultObj){
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		if(refresht){
			recordNumber = (String)result.get(Plps.RECORDNUMBER);
		}
		List<Map<String, Object>> list = (List<Map<String,Object>>)result.get(Plps.TRANSLIST);
		if(StringUtil.isNullOrEmpty(list)){
			BaseHttpEngine.dissMissProgressDialog();
			findViewById(R.id.layout_result).setVisibility(View.GONE);
			upLayout.setClickable(false);
			BaseDroidApp.getInstanse().showInfoMessageDialog("无符合查询条件的记录！");
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
		findViewById(R.id.ll_query_condition).setVisibility(View.GONE);
		findViewById(R.id.result_condition).setVisibility(View.VISIBLE);
		findViewById(R.id.layout_result).setVisibility(View.VISIBLE);
		tvPaymentAcc.setText(StringUtil.getForSixForString(accountNumber));
		index = index+Integer.valueOf(ConstantGloble.FOREX_PAGESIZE);
		upLayout.setClickable(true);
		mResultList.addAll(list);
		addFooterView(recordNumber);
		if(mAdapter == null){
			mAdapter = new InterprovincPayqueryAdapter(this, mResultList, Plps.TRACENO, Plps.TRANDATE, Plps.TOTALAMOUNT);
			mListView.setAdapter(mAdapter);	
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}
		mAdapter.setData(mResultList);
	}
	
	/**更多按钮点击事件*/
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
				sendPsnTrafficFinesTransListQuery(false);
			}
		});
	}
	
	/**创建spinner框*/
	private void createDialog(List<String> list, String title, int vId, int position){
		ListView listView = new ListView(this);
		listView.setId(vId);
		listView.setFadingEdgeLength(0);
		listView.setOnItemClickListener(accItemListener);
		listView.setScrollbarFadingEnabled(false);
		AnnuitySpinnerAdapter adapter = new AnnuitySpinnerAdapter(this, list, position);
		listView.setAdapter(adapter);
		BaseDroidApp.getInstanse().showSpinnerDialog(listView, title);
	}
	OnItemClickListener accItemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			BaseDroidApp.getInstanse().dismissMessageDialog();
			accPosition = position;
			paymentAmount.setText(PublicTools.getSpinnerData(
					PlpsDataCenter.getInstance().getAcctList(), Comm.ACCOUNTNUMBER).get(position));
			if(position >= 0){
				accountId = (String)PlpsDataCenter.getInstance().getAcctList().get(position).get(Comm.ACCOUNT_ID);
				accountNumber = (String)PlpsDataCenter.getInstance().getAcctList().get(position).get(Comm.ACCOUNTNUMBER);
			}
		}
	};
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		super.onItemClick(parent, view, position, id);
		traceNo = (String)mResultList.get(position).get(Plps.TRACENO);
		decisionNo = (String)mResultList.get(position).get(Plps.DECISIONNO);
		requestPsnTrafficFinesTransDetailQuery();
	}
	/**跨省异地交通违法罚款缴纳交易详情查询*/
	private void requestPsnTrafficFinesTransDetailQuery(){
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequest = new BiiRequestBody();
		biiRequest.setMethod(Plps.PSNTRAFFICFINESTRANSDETAILQUERY);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Plps.TRACENO, traceNo);
		params.put(Plps.DECISIONNO, decisionNo);
		biiRequest.setParams(params);
		HttpManager.requestBii(biiRequest, this, "requestPsnTrafficFinesTransDetailQueryCallBack");
	}
	
	@SuppressWarnings("unchecked")
	public void requestPsnTrafficFinesTransDetailQueryCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		PlpsDataCenter.getInstance().setInterDetailQueryResult(null);
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		if(StringUtil.isNullOrEmpty(result)){
			return;
		}
		PlpsDataCenter.getInstance().setInterDetailQueryResult(result);
		startActivity(new Intent(this, InterprovincPayqueryDetailActivity.class));
	}
}
