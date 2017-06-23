package com.chinamworld.bocmbci.biz.bocnet.debitcard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Bocnet;
import com.chinamworld.bocmbci.biz.bocnet.BocnetBaseActivity;
import com.chinamworld.bocmbci.biz.bocnet.BocnetDataCenter;
import com.chinamworld.bocmbci.biz.bocnet.BocnetUtils;
import com.chinamworld.bocmbci.biz.bocnet.adapter.DebitTransDetailAdapter;
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
 * 借记卡账户明细
 * @author panwe
 *
 */
public class DebitCardTransDetailActivity extends BocnetBaseActivity{
	/** 查询框 */
	private RelativeLayout queryCondition;
	private View queryView;
	private View conditionView;
	private View resultView;
	/** 结果头视图 */
	private LinearLayout resultCondition;
	private View hideView;
	/** 币种 */
	private Spinner currencySpinner;
	/** 汇钞标志  */
	private Spinner cashRemitSpinner;
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
	
	private List<String> currencyList;
	
	private List<String> RMBCodeList;
	
	private String currency, cashRemit, startDate, endDate;
	private boolean _refresh ;
	private int currentIndex, recordNum;
	private List<Map<String, Object>> transDetailList = new ArrayList<Map<String, Object>>();
	private DebitTransDetailAdapter detailAdapter;
	/**查询最大跨度和最长时间范围*/
	private Map<String,Object> queryMaxTime;
	/**账户明细中的最长时间*/
	private int maxYears;
	/**账户明细中的最大跨度*/
	private int maxMonths;

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
//		queryView = View.inflate(this, R.layout.bocnet_debittrandetail_condition, null);
		queryCondition = (RelativeLayout) findViewById(R.id.layout_query);
		conditionView = (LinearLayout) findViewById(R.id.ll_query_condition);
		currencySpinner = (Spinner) findViewById(R.id.currency_spinner);
		cashRemitSpinner = (Spinner) findViewById(R.id.cashRemit_spinner);
		mBtnOneWeek = (Button) findViewById(R.id.btn_onweek);
		mBtnOneMonth = (Button) findViewById(R.id.btn_onmonth);
		mBtnThreeMonth = (Button) findViewById(R.id.btn_threemonth);
		mBtnQuery = (Button) findViewById(R.id.btn_query);
		mStartdate = (TextView) findViewById(R.id.startdate);
		mEnddate = (TextView) findViewById(R.id.enddate);
		findViewById(R.id.down_layout).setOnClickListener(this);
		
		hideView=findViewById(R.id.ll_up);
		
		resultView = findViewById(R.id.layout_result);
		initSpinnerViews();
		requestPsnAccBocnetInquiryRangeQuery();
		mBtnOneWeek.setOnClickListener(this);
		mBtnOneMonth.setOnClickListener(this);
		mBtnThreeMonth.setOnClickListener(this);
		mBtnQuery.setOnClickListener(this);
		
		mEnddate.setText(QueryDateUtils.getcurrentDate(systemTime));
		mStartdate.setText(QueryDateUtils.getlastthreeDate(mEnddate.getText().toString()));
		mEnddate.setOnClickListener(bocnetChooseDateClick);
		mStartdate.setOnClickListener(bocnetChooseDateClick);
		
	}
	
	private void initSpinnerViews(){
		Map<String, Object> debitDetail = BocnetDataCenter.getInstance().getDebitDetail();
		if(!StringUtil.isNullOrEmpty(debitDetail)){
			final List<Map<String, Object>> currency = (List<Map<String, Object>>) debitDetail
					.get(Bocnet.ACCOUNTDETAILIST);
			currencyList = BocnetUtils.getListFromListMapByKey(currency, Bocnet.CURRENCYCODE, true);
			
			RMBCodeList=new ArrayList<String>(){{add("");add("000");add("001");add("068");
			add("035");add("844");add("844");add("845");add("845");}};
			
			List<String> currencyStrList = new ArrayList<String>();
			if(!StringUtil.isNullOrEmpty(currencyList)){
				for(String string : currencyList){
					currencyStrList.add(LocalData.Currency.get(string));
				}}
			BocnetUtils.initSpinnerView(this, currencySpinner, currencyStrList);
			//人民币时钞汇选择框不可先
			currencySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					
					
					
					String curCode = currencyList.get(arg2);
					if(RMBCodeList.contains(curCode)){
						List<String> cashRemitStrList = new ArrayList<String>(){{add("-");}};
						BocnetUtils.initSpinnerView(
										DebitCardTransDetailActivity.this,
										cashRemitSpinner, cashRemitStrList);
						cashRemitSpinner.setSelection(0);
						cashRemitSpinner.setClickable(false);
						cashRemitSpinner.setBackgroundResource(R.drawable.bg_spinner_default);
						
					}else{
						List<String> cashRemitStrList = BocnetUtils.getCashRemitListByCurrency(currency, curCode);
						BocnetUtils.initSpinnerView(DebitCardTransDetailActivity.this, cashRemitSpinner, cashRemitStrList);
						cashRemitSpinner.setClickable(true);
						cashRemitSpinner.setBackgroundResource(R.drawable.bg_spinner);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}
	
	/**
	 * 初始化结果控件 
	 */
	private void initResultView(){
		resultCondition = (LinearLayout) findViewById(R.id.result_condition);
		resultCondition.setOnClickListener(this);
		mBtnSort = (Button) findViewById(R.id.sort_text);
		mBtnSort.setText(LocalData.sortMap[0]);
		mLayoutsort = (LinearLayout) findViewById(R.id.ll_sort);
		tvCurrency = (TextView) findViewById(R.id.currency);
		tvCashRemit = (TextView) findViewById(R.id.tv_acc_info_cashremit_value);
		tvQueryDate = (TextView) findViewById(R.id.tv_acc_query_date_value);
		PopupWindowUtils.getInstance().setOnPullSelecterListener(this,
				mBtnSort, LocalData.sortMap, null, sortOnClick);
		accQueryResult = (ListView)findViewById(R.id.lv_acc_query_result);
//		detailAdapter = new DebitTransDetailAdapter(this, transDetailList,
//				BocnetUtils.getKeyByValue(LocalData.Currency, currencySpinner
//						.getSelectedItem().toString()));
//		accQueryResult.setAdapter(detailAdapter);
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && isshowfirst) {
			isshowfirst = false;
//			PopupWindowUtils.getInstance().getQueryPopupWindow(
//					queryView,BaseDroidApp.getInstanse().getCurrentAct());
//			PopupWindowUtils.getInstance().showQueryPopupWindowFirst();
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		//收起
		case R.id.ll_up:
//			if(PopupWindowUtils.getInstance().isQueryPopupShowing())
			if(transDetailList==null||transDetailList.size()==0){
				
			}else{
//				PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
				resultCondition.setVisibility(View.VISIBLE);
				resultView.setVisibility(View.VISIBLE);
				mBtnSort.setClickable(true);
				mLayoutsort.setVisibility(View.VISIBLE);
				queryCondition.setVisibility(View.GONE);
			}
			break;
		//下拉
		case R.id.down_layout:
//			conditionView.setBackgroundResource(R.drawable.img_bg_query_no);
//			if(!PopupWindowUtils.getInstance().isQueryPopupShowing())
			if(transDetailList==null||transDetailList.size()==0){
				
			}else{
//				PopupWindowUtils.getInstance().showQueryPopupWindow();
				queryCondition.setVisibility(View.VISIBLE);
				mLayoutsort.setVisibility(View.GONE);
				resultCondition.setVisibility(View.GONE);
				mBtnSort.setClickable(false);
			}
			break;
		case R.id.btn_onweek:
			endDate = QueryDateUtils.getcurrentDate(systemTime);
			startDate = QueryDateUtils.getlastWeek(endDate);
			currency = currencySpinner.getSelectedItem().toString();
			cashRemit = cashRemitSpinner.getSelectedItem().toString();
			clearQueryCondition();
			if (QueryDateUtils.commQueryStartAndEndateReg(this, startDate,
					endDate, QueryDateUtils.getcurrentDate(systemTime))) {
				BaseHttpEngine.showProgressDialog();
				requestConversation();
			}
			break;
		case R.id.btn_onmonth:
			endDate = QueryDateUtils.getcurrentDate(systemTime);
			startDate = QueryDateUtils.getlastOneMonth(endDate);
			currency = currencySpinner.getSelectedItem().toString();
			cashRemit = cashRemitSpinner.getSelectedItem().toString();
			clearQueryCondition();
			if (QueryDateUtils.commQueryStartAndEndateReg(this, startDate,
					endDate, QueryDateUtils.getcurrentDate(systemTime))) {
				BaseHttpEngine.showProgressDialog();
				requestConversation();
			}
			break;
		case R.id.btn_threemonth:
			endDate = QueryDateUtils.getcurrentDate(systemTime);
			startDate = QueryDateUtils.getlastThreeMonth(endDate);
			currency = currencySpinner.getSelectedItem().toString();
			cashRemit = cashRemitSpinner.getSelectedItem().toString();
			clearQueryCondition();
			if (QueryDateUtils.commQueryStartAndEndateReg(this, startDate,
					endDate, QueryDateUtils.getcurrentDate(systemTime))) {
				BaseHttpEngine.showProgressDialog();
				requestConversation();
			}
			break;
		case R.id.btn_query:
			startDate = mStartdate.getText().toString();
			endDate = mEnddate.getText().toString();
			currency = currencySpinner.getSelectedItem().toString();
			cashRemit = cashRemitSpinner.getSelectedItem().toString();
			
			clearQueryCondition();
			if (QueryDateUtils.commQueryStartAndEndDateReg(this, startDate,
					endDate, QueryDateUtils.getcurrentDate(systemTime),maxYears,maxMonths)) {
				BaseHttpEngine.showProgressDialog();
				requestConversation();
			}
			break;
		}
	}
	
	@Override
	public void conversationCallBack(Object resultObj) {
		super.conversationCallBack(resultObj);
		requestPsnAccBocnetQryDebitTransDetail();
	}
	
	private void clearQueryCondition(){
		_refresh = true;
		currentIndex = 0;
		if(detailAdapter != null)
			detailAdapter.clearDatas();
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
	 * 请求借记卡交易明细
	 */
	public void requestPsnAccBocnetQryDebitTransDetail() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bocnet.METHODACCBOCNETQRYDEBITTRANSDETAIL);
		biiRequestBody.setConversationId(BocnetDataCenter.getInstance().getConversationId());
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Bocnet.ACCOUNTSEQ, BocnetDataCenter.getInstance().getAccountSeq());
		params.put(Bocnet.CURRENCY, BocnetUtils.getKeyByValue(BocnetDataCenter.Currency, currency));
		if(BocnetUtils.isStrEquals(currency, "人民币元"))
			params.put(Bocnet.CASHREMIT, "");
		else
			params.put(Bocnet.CASHREMIT, LocalData.cashRemitMap.get(cashRemit));
		params.put(Bocnet.STARTDATE, startDate);
		params.put(Bocnet.ENDDATE, endDate);
		params.put(Bocnet._REFRESH, _refresh + "");
		params.put(Bocnet.PAGESIZE, ConstantGloble.FOREX_PAGESIZE);
		params.put(Bocnet.CURRENTINDEX, currentIndex + "");
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "accBocnetQryDebitTransDetailCallBack");
	}
	
	@SuppressWarnings("unchecked")
	public void accBocnetQryDebitTransDetailCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
		if(!StringUtil.isNullOrEmpty(result) && !StringUtil.isNullOrEmpty(result.get(Bocnet.LIST))){
			hideView.setOnClickListener(this);
			mBtnSort.setClickable(true);mBtnSort.setText(LocalData.sortMap[0]);
			recordNum = Integer.parseInt((String)result.get(Bocnet.RECORDNUMBER));
			currentIndex += 10;
			if (StringUtil.isNullOrEmpty(transDetailList))
				transDetailList = (List<Map<String, Object>>) result
						.get(Bocnet.LIST);
			else
				transDetailList.addAll((List<Map<String, Object>>) result
						.get(Bocnet.LIST));
			mLayoutsort.setVisibility(View.VISIBLE);
			resultCondition.setVisibility(View.VISIBLE);
			queryCondition.setVisibility(View.GONE);
			resultView.setVisibility(View.VISIBLE);
			if (recordNum > transDetailList.size())
				addfooteeView(accQueryResult);
			else {
				if (accQueryResult.getFooterViewsCount() > 0
						&& footerView != null)
					accQueryResult.removeFooterView(footerView);
			}
			if (detailAdapter == null) {
				detailAdapter = new DebitTransDetailAdapter(this,
						transDetailList, BocnetUtils.getKeyByValue(
								BocnetDataCenter.Currency, currency));
				accQueryResult.setAdapter(detailAdapter);
			} else {
				detailAdapter.setDatas(transDetailList, BocnetUtils
						.getKeyByValue(BocnetDataCenter.Currency, currency));
			}
		}else{
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_query_noresult_error));
			return;
		}
		tvCurrency.setText(StringUtil.valueOf1(currency));
		tvCashRemit.setText(StringUtil.valueOf1(cashRemit));
		tvQueryDate.setText(startDate + "-" + endDate);
	}
	/**
	 * 查询最大跨度和最长时间范围
	 */
	public void requestPsnAccBocnetInquiryRangeQuery(){
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody=new BiiRequestBody();
		biiRequestBody.setMethod(Bocnet.BOCNET_PSNACCBOCNETINQUIRYRANGEQUERY);
		HttpManager.requestBii(biiRequestBody, this, "PsnAccBocnetInquiryRangeQueryCallBack");
	}
	/**
	 * 查询最大跨度和最长时间范围返回
	 */
	public void PsnAccBocnetInquiryRangeQueryCallBack(Object resultObj){
		BiiResponse biiResponse=(BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodies=biiResponse.getResponse();
		BiiResponseBody biiResponseBody=biiResponseBodies.get(0);
		queryMaxTime=(Map<String, Object>) biiResponseBody.getResult();
		BaseHttpEngine.dissMissProgressDialog();
		maxYears=Integer.valueOf((String)queryMaxTime.get(Bocnet.BOCNET_QUERY_MAXYEARS));
		maxMonths=Integer.valueOf((String)queryMaxTime.get(Bocnet.BOCNET_QUERY_MAXMONTHS));
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
					requestPsnAccBocnetQryDebitTransDetail();
				}
			};
		}
		footerView = ViewUtils.getQuryListFooterView(this);
		footerView.setBackgroundDrawable(getResources().getDrawable(R.drawable.listview_more));
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
		detailAdapter.setDatas(transDetailList, BocnetUtils.getKeyByValue(BocnetDataCenter.Currency, currency));
	}

	/** 未出账单---收入排序 */
	private void wcInSort() {
		cleanMoreButton();
		// 清空数据
		if (detailAdapter != null) {
			detailAdapter.setDatas(new ArrayList<Map<String, Object>>(), BocnetUtils.getKeyByValue(BocnetDataCenter.Currency, currency));
		}
		List<Map<String, Object>> wcInSortlist = insort(transDetailList);
		if (StringUtil.isNullOrEmpty(wcInSortlist)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_nullin));
			return;
		}
		if(detailAdapter == null)
			return;
		detailAdapter.setDatas(wcInSortlist, BocnetUtils.getKeyByValue(BocnetDataCenter.Currency, currency));
	}

	/** 未出账单---支出排序 */
	private void wcOutSort() {
		cleanMoreButton();
		if (detailAdapter != null) {
			detailAdapter.setDatas(new ArrayList<Map<String, Object>>(), BocnetUtils.getKeyByValue(BocnetDataCenter.Currency, currency));
		}
		List<Map<String, Object>> wcOutSortlist = outsort(transDetailList);
		if (wcOutSortlist == null || wcOutSortlist.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_nullout));
			return;
		}
		if(detailAdapter == null)
			return;
		detailAdapter.setDatas(wcOutSortlist, BocnetUtils.getKeyByValue(BocnetDataCenter.Currency, currency));
	}
	
	/** 移除更多按钮 */
	private void cleanMoreButton() {
		if (accQueryResult.getFooterViewsCount() > 0) {
			if(footerView != null)
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
