package com.chinamworld.bocmbci.biz.bocnet.cashacct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Bocnet;
import com.chinamworld.bocmbci.biz.bocnet.BocnetBaseActivity;
import com.chinamworld.bocmbci.biz.bocnet.BocnetDataCenter;
import com.chinamworld.bocmbci.biz.bocnet.adapter.CashAcctTransDetailAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.ViewUtils;

/**
 * 电子现金账户交易明细
 * @author fsm
 *
 */
public class CashTransDetailActivity extends BocnetBaseActivity{
	private View queryView;
	private View conditionView;
	private View resultView;
	
	private View hideView;
	/** 查询时间段  */
	private Button mBtnOneWeek,mBtnOneMonth,mBtnThreeMonth,mBtnQuery;
	private TextView mStartdate,mEnddate;
	/** 排序   */
	private LinearLayout mLayoutsort;
	private Button mBtnSort;
	/** 查询结果   币种   钞汇标志    查询时间 */
	private TextView tvCurrency, tvCashRemit, tvQueryDate;
	private boolean isshowfirst = true;
	private ListView accQueryResult;
	private View footerView;
	private OnClickListener footerOnclickListenner;
	
	private String startDate, endDate;
	private boolean _refresh ;
	private int currentIndex = 1, recordNum;
	private List<Map<String, Object>> transDetailList = new ArrayList<Map<String, Object>>();
	private CashAcctTransDetailAdapter detailAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.bocnet_debittrandetail);
		setTitle(R.string.mycrcd_bill_detail);
		((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0,0, 0);
		systemTime = BocnetDataCenter.getInstance().getSystemTime();
		if(StringUtil.isNull(systemTime))
			return;
		initQueryView();
		initResultView();
	}
	
	/**
	 * 初始化查询控件
	 */
	private void initQueryView(){
		queryView = View.inflate(this, R.layout.bocnet_debittrandetail_condition, null);
		((View)queryView.findViewById(R.id.ccr_ll)).setVisibility(View.GONE);
		conditionView = (LinearLayout) queryView.findViewById(R.id.ll_query_condition);
		mBtnOneWeek = (Button) queryView.findViewById(R.id.btn_onweek);
		mBtnOneMonth = (Button) queryView.findViewById(R.id.btn_onmonth);
		mBtnThreeMonth = (Button) queryView.findViewById(R.id.btn_threemonth);
		mBtnQuery = (Button) queryView.findViewById(R.id.btn_query);
		mStartdate = (TextView) queryView.findViewById(R.id.startdate);
		mEnddate = (TextView) queryView.findViewById(R.id.enddate);
		findViewById(R.id.down_layout).setOnClickListener(this);		
		hideView=queryView.findViewById(R.id.ll_up);	
		resultView = findViewById(R.id.layout_result);
		mBtnOneWeek.setOnClickListener(this);
		mBtnOneMonth.setOnClickListener(this);
		mBtnThreeMonth.setOnClickListener(this);
		mBtnQuery.setOnClickListener(this);
		
		mEnddate.setText(QueryDateUtils.getcurrentDate(systemTime));
		mStartdate.setText(QueryDateUtils.getlastthreeDate(mEnddate.getText().toString()));
		mEnddate.setOnClickListener(bocnetChooseDateClick);
		mStartdate.setOnClickListener(bocnetChooseDateClick);
		
	}
	
	/**
	 * 初始化结果控件 
	 */
	private void initResultView(){
		findViewById(R.id.result_condition).setOnClickListener(this);
		mBtnSort = (Button) findViewById(R.id.sort_text);
		mBtnSort.setText(LocalData.sortMap[0]);
		mLayoutsort = (LinearLayout) findViewById(R.id.ll_sort);
		tvCurrency = (TextView) findViewById(R.id.currency);
		tvCurrency.setText(getString(R.string.rmb_currency));
		tvCashRemit = (TextView) findViewById(R.id.tv_acc_info_cashremit_value);
		tvCashRemit.setText(ConstantGloble.BOCINVT_DATE_ADD);
		tvQueryDate = (TextView) findViewById(R.id.tv_acc_query_date_value);
		PopupWindowUtils.getInstance().setOnPullSelecterListener(this,
				mLayoutsort, LocalData.sortMap, null, sortOnClick);
		accQueryResult = (ListView)findViewById(R.id.lv_acc_query_result);
//		detailAdapter = new CashAcctTransDetailAdapter(this, transDetailList);
//		accQueryResult.setAdapter(detailAdapter);
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && isshowfirst) {
			isshowfirst = false;
			BaseActivity activity = (BaseActivity)BaseDroidApp.getInstanse().getCurrentAct();
			PopupWindowUtils.getInstance().getQueryPopupWindow(
					queryView,activity);
			PopupWindowUtils.getInstance().showQueryPopupWindowFirst();
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		//收起
		case R.id.ll_up:
			if(PopupWindowUtils.getInstance().isQueryPopupShowing())
				PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
			resultView.setVisibility(View.VISIBLE);
			mLayoutsort.setClickable(true);
			mLayoutsort.setVisibility(View.VISIBLE);
			break;
		//下拉
		case R.id.down_layout:
			conditionView.setBackgroundResource(R.drawable.img_bg_query_no);
			if(!PopupWindowUtils.getInstance().isQueryPopupShowing())
				PopupWindowUtils.getInstance().showQueryPopupWindow();
			mLayoutsort.setVisibility(View.VISIBLE);
			mLayoutsort.setClickable(false);
			break;
		case R.id.btn_onweek:
			endDate = QueryDateUtils.getcurrentDate(systemTime);
			startDate = QueryDateUtils.getlastWeek(endDate);
			clearQueryCondition();
			if (QueryDateUtils.commQueryStartAndEndateReg(this, startDate,
					endDate, QueryDateUtils.getcurrentDate(systemTime))) {
				BaseHttpEngine.showProgressDialog();
				requestPsnAccBocnetQryEcashTransDetail();
			}
			break;
		case R.id.btn_onmonth:
			endDate = QueryDateUtils.getcurrentDate(systemTime);
			startDate = QueryDateUtils.getlastOneMonth(endDate);
			clearQueryCondition();
			if (QueryDateUtils.commQueryStartAndEndateReg(this, startDate,
					endDate, QueryDateUtils.getcurrentDate(systemTime))) {
				BaseHttpEngine.showProgressDialog();
				requestPsnAccBocnetQryEcashTransDetail();
			}
			break;
		case R.id.btn_threemonth:
			endDate = QueryDateUtils.getcurrentDate(systemTime);
			startDate = QueryDateUtils.getlastThreeMonth(endDate);
			clearQueryCondition();
			if (QueryDateUtils.commQueryStartAndEndateReg(this, startDate,
					endDate, QueryDateUtils.getcurrentDate(systemTime))) {
				BaseHttpEngine.showProgressDialog();
				requestPsnAccBocnetQryEcashTransDetail();
			}
			break;
		case R.id.btn_query:
			startDate = mStartdate.getText().toString();
			endDate = mEnddate.getText().toString();
			clearQueryCondition();
			if (QueryDateUtils.commQueryStartAndEndateReg(this, startDate,
					endDate, QueryDateUtils.getcurrentDate(systemTime))) {
				BaseHttpEngine.showProgressDialog();
				requestPsnAccBocnetQryEcashTransDetail();
			}
			
			
			break;
			
			
		}
	}
	
	private void clearQueryCondition(){
		_refresh = true;
		currentIndex = 1;
		if(detailAdapter != null)
			detailAdapter.clearData();
		if(transDetailList != null)
			transDetailList.clear();
	}
	
	/**
	 * 排序点击事件 
	 */
	private OnClickListener sortOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_text1:
				mBtnSort.setText(LocalData.sortMap[0]);
				soreAllWcDate();
				break;
			case R.id.tv_text2:
				mBtnSort.setText(LocalData.sortMap[1]);
				wcInSort();
				break;
			case R.id.tv_text3:
				mBtnSort.setText(LocalData.sortMap[2]);
				wcOutSort();
				break;
			}
		}
	};
	
	/**
	 * 查询电子现金账户交易明细
	 */
	public void requestPsnAccBocnetQryEcashTransDetail() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bocnet.METHODACCBOCNETQRYECASHTRANSDETAIL);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Bocnet.ACCOUNTSEQ, BocnetDataCenter.getInstance().getAccountSeq());
		params.put(Bocnet.STARTDATE, startDate);
		params.put(Bocnet.ENDDATE, endDate);
		params.put(Bocnet._REFRESH, _refresh + "");
		params.put(Bocnet.PAGESIZE, ConstantGloble.FOREX_PAGESIZE);
		params.put(Bocnet.CURRENTINDEX, currentIndex + "");
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "accBocnetQryEcashTransDetailCallBack");
	}
	
	@SuppressWarnings("unchecked")
	public void accBocnetQryEcashTransDetailCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if(!StringUtil.isNullOrEmpty(result) && !StringUtil.isNullOrEmpty(result.get(Bocnet.TRANSDETAILS))){
			recordNum = Integer.parseInt((String)result.get(Bocnet.RECORDNUMBER));
			hideView.setOnClickListener(this);
			currentIndex += 1;
			if (StringUtil.isNullOrEmpty(transDetailList))
				transDetailList = (List<Map<String, Object>>) result
						.get(Bocnet.TRANSDETAILS);
			else
				transDetailList.addAll((List<Map<String, Object>>) result
						.get(Bocnet.TRANSDETAILS));
			resultView.setVisibility(View.VISIBLE);
			if (recordNum > transDetailList.size())
				addfooteeView(accQueryResult);
			else {
				if (accQueryResult.getFooterViewsCount() > 0
						&& footerView != null)
					accQueryResult.removeFooterView(footerView);
			}
			if(detailAdapter == null ){
				detailAdapter = new CashAcctTransDetailAdapter(this, transDetailList);
				accQueryResult.setAdapter(detailAdapter);
			}else{
				detailAdapter.setDatas(transDetailList);
			}
		}else{
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_query_noresult_error));
			return;
		}
		PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
		tvQueryDate.setText(startDate + "-" + endDate);
	}
	
	private void addfooteeView(ListView listView) {
		if (footerView != null) {
			listView.removeFooterView(footerView);
		}
		if (footerOnclickListenner == null) {
			footerOnclickListenner = new OnClickListener() {

				@Override
				public void onClick(View v) {
					_refresh = false;
					BaseHttpEngine.showProgressDialog();
					requestPsnAccBocnetQryEcashTransDetail();
				}
			};
		}
		footerView = ViewUtils.getQuryListFooterView(this, R.layout.acc_load_more);
		footerView.setOnClickListener(footerOnclickListenner);
		listView.addFooterView(footerView, null, true);
	}
	
	/** 未出账单--排序--全部 */
	private void soreAllWcDate() {
		if (StringUtil.isNullOrEmpty(transDetailList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_nullin));
			return;
		}
		if (recordNum > transDetailList.size()) {
			if (accQueryResult.getFooterViewsCount() > 0) {
				
			} else {
				accQueryResult.addFooterView(footerView);
			}
		}
		if(detailAdapter == null)
			return;
		detailAdapter.setDatas(transDetailList);
	}

	/** 未出账单---收入排序 */
	private void wcInSort() {
		cleanMoreButton();
		// 清空数据
		if (detailAdapter != null) {
			detailAdapter.setDatas(new ArrayList<Map<String, Object>>());
		}
		List<Map<String, Object>> wcInSortlist = insort(transDetailList);
		if (StringUtil.isNullOrEmpty(wcInSortlist)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_nullin));
			return;
		}
		if(detailAdapter == null)
			return;
		detailAdapter.setDatas(wcInSortlist);
	}

	/** 未出账单---支出排序 */
	private void wcOutSort() {
		cleanMoreButton();
		if (detailAdapter != null) {
			detailAdapter.setDatas(new ArrayList<Map<String, Object>>());
		}
		List<Map<String, Object>> wcOutSortlist = outsort(transDetailList);
		if (wcOutSortlist == null || wcOutSortlist.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_nullout));
			return;
		}
		if(detailAdapter == null)
			return;
		detailAdapter.setDatas(wcOutSortlist);
	}
	
	/** 移除更多按钮 */
	private void cleanMoreButton() {
		if (accQueryResult.getFooterViewsCount() > 0) {
			accQueryResult.removeFooterView(footerView);
		}

	}
	
	/** 收入 */
	public List<Map<String, Object>> insort(List<Map<String, Object>> list) {
		List<Map<String, Object>> inList = new ArrayList<Map<String, Object>>();
		if (!list.isEmpty()) {
			for (Map<String, Object> map : list) {
				String amount = (String) map
						.get(Bocnet.AMOUNT);
				if (amount.startsWith(ConstantGloble.BOCINVT_DATE_ADD)) {

				} else {
					inList.add(map);
				}
			}
		}
		return inList;
	}

	/** 支出 */
	public List<Map<String, Object>> outsort(List<Map<String, Object>> list) {
		List<Map<String, Object>> outList = new ArrayList<Map<String, Object>>();
		if (!list.isEmpty()) {
			for (Map<String, Object> map : list) {
				String amount = (String) map
						.get(Bocnet.AMOUNT);
				if (amount.startsWith(ConstantGloble.BOCINVT_DATE_ADD)) {
					outList.add(map);
				} else {

				}
			}
		}
		return outList;
	}
	
}
