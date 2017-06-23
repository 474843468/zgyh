package com.chinamworld.bocmbci.biz.bocnet.creditcard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Bocnet;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.bocnet.BocnetBaseActivity;
import com.chinamworld.bocmbci.biz.bocnet.BocnetDataCenter;
import com.chinamworld.bocmbci.biz.bocnet.adapter.CrcdUnsettledBillAdapter2;
import com.chinamworld.bocmbci.biz.crcd.utils.SortUtils;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.SearchView;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.ViewUtils;

/**  
 * 信用卡账单查询页面
 * @author fsm
 *
 */
public class CreditCardBillQueryActivity extends BocnetBaseActivity implements OnCheckedChangeListener{

	/** 查询条件整个布局 */
	private View search_condition_view = null;
	
	/** 卡类型， 卡号， 卡昵称 */
	private TextView crcd_type_value, crcd_account_num, crcd_account_nickname;
	
	private RadioGroup radioloanGroup;
	/** 未出账单显示查询按钮 , 已出账单查询按钮， 筛选按钮  */
	private Button btnLoanHistoryQuery, yichi_search,sort_text ;
	/** 查询条件页上拉按钮 , 下拉按钮 , 收入支出合计结果 ，筛选*/
	private LinearLayout ll_upLayout, xiala_view, ll_shouandzhi, ll_sort;
	/** 查询条件页  已出账单条件布局*/
	private View spinnerView = null;
	/** 未出账单查询结果*/
	private ListView unsettledBillsLv;
	/** 查询结果页面整个布局 */
	private RelativeLayout acc_query_result_layout;
	/** 时间选择下拉框*/
	private Spinner timesSpinner = null;
	/** 结果页面---顶部---账号 */
	private TextView acc_num;
	/** 收入总额, 收入总额单位, 外币收入总额， 外币收总额单位 */
	private TextView creditSum, creditSumUnit, foreignCreditSum, foreignCreditSumUnit;
	/** 支出总额, 支出总额单位, 外币支出总额， 外币支出总额单位 */
	private TextView debitSum, debitSumUnit, foreignDebitSum, foreignDebitSumUnit;
	
	private boolean isUnsettledBill = true;//账单查询标志   true:未出账单   false:已出账单
	/** 未出账单  */
	/** 未出账单查询---adapter */
	private View footerView;//更多布局
	private OnClickListener footerOnclickListenner;
	private CrcdUnsettledBillAdapter2 ctfAdapter;
	private int pageSize = 10;
	private int currentIndex = 0;
	private boolean _refresh = true;
	private int recordNum1;//未出账单记录
	private List<Map<String, Object>> crcdTransactionList;//未出账单交易列表
	private List<Map<String, Object>> unsettledBillTotal;//未出账单合计
	
	/** 已出账单   */
	private List<String> dateList = null;//已出账单日期选择列表
	private String queryMonth ;//查询月份
	private boolean isLastMonth;
	/** 账户ID accountId */
	private String accountId=null;
	
	private boolean isBillExist=false;// 当月账单是否已出
	
	
	/** false-未入账,true-已入账 */
	private boolean isWrOrYr= true;
	private RadioGroup crcd_trans_radioGroup;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.bocnet_bill_query_result);
		setTitle(getString(R.string.bocnet_bill_query));
		((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 0);
		
		accountId=getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		BaseHttpEngine.showProgressDialog();
		requestPsnAccBocnetQryCrcdBillIsExist();	
	}
	
	private void requestPsnAccBocnetQryCrcdBillIsExist() {

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNACCBOCNETQUERYCRCDBILLISEXIST);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTSEQ_RES, accountId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnAccBocnetQryCrcdBillIsExistCallBack");
		
	}
	public void requestPsnAccBocnetQryCrcdBillIsExistCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		String BillExist=(String)resultMap.get(Crcd.CRCD_ISBILLEXIST);
//		0：没有出账单  1：已出
		if("0".equals(BillExist)){
			
			isBillExist=false;	
		}else if("1".equals(BillExist)){
			isBillExist=true;
		}else{
			isBillExist=false;	
		}
		
		requestSystemTime();	
	}
	@Override
	public void systemTimeCallBack(Object resultObj) {
		super.systemTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		setupViews();
		setBtnListeners();
		getQueryBillTime();
		initQueryTime();
//		PopupWindowUtils.getInstance().showQueryPopupWindow();
		SearchView.getInstance().showSearchView();
	}
	
	private void getQueryBillTime(){
		dateList = new ArrayList<String>();
		dateList.add(getResources().getString(R.string.mycrcd_no_bill));
		
		if(isBillExist){
			// 已出
			for (int i = 0; i < 12; i++) {
				String t = "-" + i;
				int j = Integer.valueOf(t);
				String times = QueryDateUtils.getLastNumMonthAgo(systemTime, j);
				dateList.add(times);
			}
		}else{
			for (int i = 1; i < 13; i++) {
				String t = "-" + i;
				int j = Integer.valueOf(t);
				String times = QueryDateUtils.getLastNumMonthAgo(systemTime, j);
				dateList.add(times);
			}
		}
	}
	
	private void initQueryTime(){
		ArrayAdapter<String> timesAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, dateList);
		timesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		timesSpinner.setAdapter(timesAdapter);
		timesSpinner.setSelection(0);
		queryMonth = timesSpinner.getSelectedItem().toString();
	}
	
	private void setupViews(){
		search_condition_view = LayoutInflater.from(this).inflate(R.layout.bocnet_bill_query_condition, null);
//		PopupWindowUtils.getInstance().getQueryPopupWindow(search_condition_view, 
//				this);
		
		Map<String, Object> loginInfo = BocnetDataCenter.getInstance().getLoginInfo();
		

		crcd_type_value = (TextView)search_condition_view.findViewById(R.id.crcd_type_value);
		crcd_account_num = (TextView)search_condition_view.findViewById(R.id.crcd_account_num);
		crcd_account_nickname = (TextView)search_condition_view.findViewById(R.id.crcd_account_nickname);
		if (!StringUtil.isNullOrEmpty(loginInfo)) {
			crcd_type_value.setText(LocalData.AccountType.get((String)loginInfo.get(Bocnet.ACCOUNTTYPE)));
			crcd_account_num.setText(StringUtil.getForSixForString((String)loginInfo.get(Bocnet.ACCOUNTNUMBER)));
			crcd_account_nickname.setText(StringUtil.valueOf1((String)loginInfo.get(Bocnet.NAME)));
		
		}
		
//		TextView accNum = (TextView)search_condition_view.findViewById(R.id.accNum);
//		Map<String, Object> loginInfo = BocnetDataCenter.getInstance().getLoginInfo();
//		if (!StringUtil.isNullOrEmpty(loginInfo)) {
//			accNum.setText(StringUtil.getForSixForString((String) loginInfo
//					.get(Bocnet.ACCOUNTNUMBER)));
//		}
		
		radioloanGroup = (RadioGroup) search_condition_view.findViewById(R.id.radioGroup);
		btnLoanHistoryQuery = (Button) search_condition_view.findViewById(R.id.btnLoanHistoryQuery);
		yichi_search = (Button) search_condition_view.findViewById(R.id.yichi_search);
		ll_upLayout = (LinearLayout) search_condition_view.findViewById(R.id.ll_upLayout);
		spinnerView = (LinearLayout) search_condition_view.findViewById(R.id.spinner_view);
		timesSpinner = (Spinner) search_condition_view.findViewById(R.id.times_spinner);
		
		sort_text = (Button) findViewById(R.id.sort_text);
		ll_sort = (LinearLayout) findViewById(R.id.ll_sort);
		crcd_trans_radioGroup=(RadioGroup) findViewById(R.id.crcd_trans_radioGroup);
		xiala_view = (LinearLayout) findViewById(R.id.xiala_view);
		ll_shouandzhi = (LinearLayout) findViewById(R.id.ll_shouandzhi);
		unsettledBillsLv = (ListView) findViewById(R.id.lv_acc_query_result);
		acc_query_result_layout = (RelativeLayout) findViewById(R.id.acc_query_result_layout);
		acc_num = (TextView) findViewById(R.id.acc_num);
		creditSum = (TextView) findViewById(R.id.creditSum);
		creditSumUnit = (TextView) findViewById(R.id.creditSumUnit);
		foreignCreditSum = (TextView) findViewById(R.id.foreignCreditSum);
		foreignCreditSumUnit = (TextView) findViewById(R.id.foreignCreditSumUnit);
		debitSum = (TextView) findViewById(R.id.debitSum);
		debitSumUnit = (TextView) findViewById(R.id.debitSumUnit);
		foreignDebitSum = (TextView) findViewById(R.id.foreignDebitSum);
		foreignDebitSumUnit = (TextView) findViewById(R.id.foreignDebitSumUnit);
		
//		onCheckedChanged(radioloanGroup, R.id.loan_his_btn1);
		PopupWindowUtils.getInstance().setOnPullSelecterListener(this, sort_text
				, LocalData.sortMap, null, this);
		LinearLayout acc_query_result_condition=(LinearLayout)findViewById(R.id.acc_query_result_condition);
		SearchView.getInstance().addSearchView(acc_query_result_condition, search_condition_view);
	}
	private void setBtnListeners(){
		radioloanGroup.setOnCheckedChangeListener(this);
		btnLoanHistoryQuery.setOnClickListener(this);
		yichi_search.setOnClickListener(this);
		xiala_view.setOnClickListener(this);
//		ll_upLayout.setOnClickListener(this);
		timesSpinner.setOnItemSelectedListener(onItemSelectedListener);
	}
	
	OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			switch (arg0.getId()) {
			case R.id.times_spinner:
				if(!StringUtil.isNullOrEmpty(dateList))
					if (arg2 == 0) {
						isUnsettledBill = true;
						
						if(acc_query_result_layout.getVisibility()==View.VISIBLE){
							setTitle(getResources().getString(R.string.mycrcd_no_bill));	
						}
						
						if (!StringUtil.isNullOrEmpty(crcdTransactionList)){
						crcd_trans_radioGroup.setVisibility(View.VISIBLE);	
					}
						
						unsettledBillsLv.setVisibility(View.VISIBLE);
						
					}else{
						setTitle(getResources().getString(R.string.card_account_search));
						queryMonth = dateList.get(arg2);	
						isUnsettledBill = false;
						crcd_trans_radioGroup.setVisibility(View.GONE);
						unsettledBillsLv.setVisibility(View.GONE);
					}
					
				
				break;
			default:
				break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btnLoanHistoryQuery://查询未出账单
			BaseHttpEngine.showProgressDialog();
			_refresh = true;
			if(!StringUtil.isNullOrEmpty(unsettledBillTotal))
				unsettledBillTotal.clear();
			requestPsnAccBocnetQryCrcdUnsettledTotal();
			break;
		case R.id.yichi_search://查询已出账单
			
			if(isUnsettledBill){
				//查询未出账单
				BaseHttpEngine.showProgressDialog();
				_refresh = true;
				if(!StringUtil.isNullOrEmpty(unsettledBillTotal))
					unsettledBillTotal.clear();
				requestPsnAccBocnetQryCrcdUnsettledTotal();	
			}else{
				//查询已出账单
				BaseHttpEngine.showProgressDialog();
				requestConversation();
			}
			
			break;
		case R.id.xiala_view://下拉按钮
//			if(!PopupWindowUtils.getInstance().isQueryPopupShowing()){
//				PopupWindowUtils.getInstance().showQueryPopupWindow();
				SearchView.getInstance().showSearchView();
				sort_text.setVisibility(View.INVISIBLE);
//				crcd_trans_radioGroup.setVisibility(View.INVISIBLE);
//			}
			break;
		case R.id.ll_upLayout://收起按钮
//			if(PopupWindowUtils.getInstance().isQueryPopupShowing()){
//				PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
				SearchView.getInstance().hideSearchView();
				sort_text.setVisibility(View.VISIBLE);
//				crcd_trans_radioGroup.setVisibility(View.VISIBLE);
//			}
			break;
		case R.id.tv_text1:// 全部
			sort_text.setText(LocalData.sortMap[0]);
			if (isUnsettledBill) {
				soreAllWcDate(crcdTransactionList);
			}
			break;
		case R.id.tv_text2:// 收入
			sort_text.setText(LocalData.sortMap[1]);
			if (isUnsettledBill) {
				wcInSort();
			}
			break;
		case R.id.tv_text3:// 支出
			sort_text.setText(LocalData.sortMap[2]);
			if (isUnsettledBill) {
				wcOutSort();
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (group.getId()) {
		case R.id.radioGroup:
			switch (checkedId) {
			case R.id.loan_his_btn1:
				isUnsettledBill = true;
				btnLoanHistoryQuery.setVisibility(View.VISIBLE);
				spinnerView.setVisibility(View.GONE);
				
				sort_text.setClickable(true);
				if(!StringUtil.isNullOrEmpty(crcdTransactionList)){
					acc_query_result_layout.setVisibility(View.VISIBLE);
					ll_upLayout.setClickable(true);
				}else{
					acc_query_result_layout.setVisibility(View.INVISIBLE);
					ll_upLayout.setClickable(false);
				}
				break;
			case R.id.loan_his_btn2:
				isUnsettledBill = false;
				btnLoanHistoryQuery.setVisibility(View.GONE);
				spinnerView.setVisibility(View.VISIBLE);
				
				acc_query_result_layout.setVisibility(View.INVISIBLE);
				sort_text.setClickable(false);
				ll_upLayout.setClickable(false);
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public void conversationCallBack(Object resultObj) {
		super.conversationCallBack(resultObj);
		if(isUnsettledBill){
			if(isWrOrYr){
				requestPsnAccBocnetQryCrcdUnsettledBills();	
			}else{
				// 查询信用卡未出账单   未入账
				
				requestPsnAccBocnetQryUnauthorizedTrans();
			}
			
		}else{
			requestPsnAccBocnetQryCrcdStatement();
		}
	}
	
	/**
	 * 请求信用卡未出账单
	 */
	public void requestPsnAccBocnetQryCrcdUnsettledBills() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bocnet.METHODACCBOCNETQRYCRCDUNSETTLEDBILLS );
		biiRequestBody.setConversationId(BocnetDataCenter.getInstance().getConversationId());
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put(Bocnet.ACCOUNTSEQ, BocnetDataCenter.getInstance().getAccountSeq());
		param.put(Bocnet.PAGESIZE, pageSize + "");
		param.put(Bocnet.CURRENTINDEX, currentIndex + "");
		param.put(Bocnet._REFRESH, _refresh + "");
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "accBocnetQryCrcdUnsettledBillsCallBack");
	}
	
	public void accBocnetQryCrcdUnsettledBillsCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		acc_query_result_layout.setVisibility(View.VISIBLE);
		crcd_trans_radioGroup.setVisibility(View.VISIBLE);
		setTitle(this.getString(R.string.mycrcd_no_bill));
		ll_upLayout.setOnClickListener(this);
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		if(StringUtil.isNullOrEmpty(map) || StringUtil.isNullOrEmpty(map.get(Bocnet.CRCDTRANSACTIONLIST))){
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_query_noresult_error));
			
			SearchView.getInstance().showSearchView();
			if (crcdTransactionList != null && !crcdTransactionList.isEmpty()) {
				crcdTransactionList.clear();
			}
			
			return;
		}
		if(search_condition_view.isShown())
//			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
		SearchView.getInstance().hideSearchView();
		BocnetDataCenter.getInstance().setCrcdUnsettledBills(map);
		recordNum1 = Integer.valueOf((String) map.get(Bocnet.RECORDNUMBER));
		currentIndex += 10;
		setUnsettledBillsData();
		
		
	}
	
	/**
	 * 设置未出账单查询结果
	 */
	private void setUnsettledBillsData(){
		List<Map<String, Object>> list = (List<Map<String, Object>>)BocnetDataCenter.getInstance().
				getCrcdUnsettledBills().get(Bocnet.CRCDTRANSACTIONLIST);
		if (!_refresh && list != null && ctfAdapter != null) {
			crcdTransactionList.addAll(list);
			ctfAdapter.changeDate(crcdTransactionList);
			if (crcdTransactionList.size() >= recordNum1) {// 全部显示完鸟\
				removeFooterView(unsettledBillsLv);
			}
		} else {
			crcdTransactionList = list;
			if(ctfAdapter==null){
				ctfAdapter = new CrcdUnsettledBillAdapter2(this, crcdTransactionList);
			}else{
				ctfAdapter.changeDate(crcdTransactionList);
			}
			ctfAdapter.setIsWrOrYr(isWrOrYr);
			if (recordNum1 > 10) {// 不能显示完
				addfooteeView(unsettledBillsLv);
			} else {
				removeFooterView(unsettledBillsLv);
			}
			unsettledBillsLv.setAdapter(ctfAdapter);
		}
		sort_text.setClickable(true);sort_text.setText(LocalData.sortMap[0]);
	}
	
	private void addfooteeView(ListView listView) {
		if (footerView != null) {
			removeFooterView(listView);
		}
		if (footerOnclickListenner == null) {
			footerOnclickListenner = new OnClickListener() {

				@Override
				public void onClick(View v) {
					_refresh = false;
					BaseHttpEngine.showProgressDialog();
					if(isWrOrYr){
						requestPsnAccBocnetQryCrcdUnsettledBills();
					}else{
						// 查询信用卡未出账单   未入账
						
						requestPsnAccBocnetQryUnauthorizedTrans();
					}
					
					
				}
			};
		}
		footerView = ViewUtils.getQuryListFooterView(this, R.layout.acc_load_more);
		footerView.setOnClickListener(footerOnclickListenner);
		listView.addFooterView(footerView, null, true);
	}

	private void removeFooterView(ListView listView) {
		listView.removeFooterView(footerView);
	}
	
	/**
	 * 请求信用卡未出账单合计
	 */
	
	private void requestPsnAccBocnetQryCrcdUnsettledTotal() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bocnet.METHODACCBOCNETQRYCRCDUNSETTLEDTOTAL );
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put(Bocnet.ACCOUNTSEQ, BocnetDataCenter.getInstance().getAccountSeq());
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "accBocnetQryCrcdUnsettledTotalCallBack");
	}
	
	public void accBocnetQryCrcdUnsettledTotalCallBack(Object resultObj){
		List<Map<String, Object>> list = HttpTools.getResponseResult(resultObj);
		if(StringUtil.isNullOrEmpty(list)){
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_query_noresult_error));
			return;
		}
		if(StringUtil.isNullOrEmpty(unsettledBillTotal))
			unsettledBillTotal = list;
		else
			unsettledBillTotal.addAll(list);
		sort_text.setText(LocalData.sortMap[0]);
		BocnetDataCenter.getInstance().setCrcdUnsettledTotal(unsettledBillTotal);
		setUnsettledTotal(unsettledBillTotal);
		requestConversation();
	}
	
	/**
	 * 设置未出账单合计
	 */
	private void setUnsettledTotal(List<Map<String, Object>> totalList){
		ll_shouandzhi.setVisibility(View.VISIBLE);
		Map<String, Object> loginInfo = BocnetDataCenter.getInstance().getLoginInfo();
		if(!StringUtil.isNullOrEmpty(loginInfo))
			acc_num.setText(StringUtil.getForSixForString((String)loginInfo.get(Bocnet.ACCOUNTNUMBER)));
		Map<String, Object> map = (Map<String, Object>) totalList.get(0);
		/** 贷方合计 */
		String creditsum = String.valueOf(map.get(Bocnet.CREDITSUM));
		/** 借方合计 */
		String debitsum = String.valueOf(map.get(Bocnet.DEBITSUM));
		String currency = String.valueOf(map.get(Bocnet.CURRENCY));
		String strCurrcyCode = LocalData.Currency.get(currency);
		creditSum.setText(StringUtil.parseStringCodePattern(currency, creditsum, 2));
		debitSum.setText(StringUtil.parseStringCodePattern(currency, debitsum, 2));
		creditSumUnit.setText(strCurrcyCode);
		debitSumUnit.setText(strCurrcyCode);

		if (totalList.size() == 1) {
			((LinearLayout)findViewById(R.id.ll_foreignshouru)).setVisibility(View.GONE);
			((LinearLayout)findViewById(R.id.ll_foreignzhichu)).setVisibility(View.GONE);
		}

		if (totalList.size() > 1) {
			Map<String, Object> map1 = (Map<String, Object>) totalList.get(1);
			String foreignCreditsum = String.valueOf(map1.get(Bocnet.CREDITSUM));
			String foreignDebitsum = String.valueOf(map1.get(Bocnet.DEBITSUM));
			currency = String.valueOf(map1.get(Bocnet.CURRENCY));
			strCurrcyCode = LocalData.Currency.get(currency);

			((LinearLayout)findViewById(R.id.ll_foreignshouru)).setVisibility(View.VISIBLE);
			((LinearLayout)findViewById(R.id.ll_foreignzhichu)).setVisibility(View.VISIBLE);

			foreignCreditSum.setText(StringUtil.parseStringCodePattern(currency, foreignCreditsum, 2));
			foreignDebitSum.setText(StringUtil.parseStringCodePattern(currency, foreignDebitsum, 2));
			foreignCreditSumUnit.setText(strCurrcyCode);
			foreignDebitSumUnit.setText(strCurrcyCode);
		}
	}
	
	/**
	 * 请求信用卡已出账单
	 */
	public void requestPsnAccBocnetQryCrcdStatement() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bocnet.METHODACCBOCNETQRYCRCDSTATEMENT );
		biiRequestBody.setConversationId(BocnetDataCenter.getInstance().getConversationId());
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put(Bocnet.ACCOUNTSEQ, BocnetDataCenter.getInstance().getAccountSeq());
		param.put(Bocnet.STATEMENTMONTH, queryMonth);	
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "accBocnetQryCrcdStatementCallBack");
	}
	
	public void accBocnetQryCrcdStatementCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		if(StringUtil.isNullOrEmpty(map)){
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_query_noresult_error));
			return;
		}
		BocnetDataCenter.getInstance().setCrcdStatement(map);
		isLastMonth = timesSpinner.getSelectedItemPosition() == 0 ? true : false;
		Intent intent = new Intent(this, CreditCardStatementActivity.class);
		intent.putExtra(ConstantGloble.CRCD_ISLASTMONTH, isLastMonth);
		intent.putExtra(Bocnet.STATEMENTMONTH, queryMonth);
		startActivityForResult(intent, 0);
	}
	
	/** 未出账单--排序--全部 */
	private void soreAllWcDate(List<Map<String, Object>> notransferList) {
		if (StringUtil.isNullOrEmpty(notransferList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_nullin));
			return;
		}
		if (recordNum1 > notransferList.size()) {
			if (unsettledBillsLv.getFooterViewsCount() > 0) {
//				unsettledBillsLv.removeFooterView(footerView);
			} else {
				unsettledBillsLv.addFooterView(footerView);
			}
		}
		setUnSettledBillData(notransferList);
	}

	/** 未出账单---收入排序 */
	private void wcInSort() {
		cleanMoreButton();
		// 清空数据
		if (ctfAdapter != null) {
			ctfAdapter.changeDate(new ArrayList<Map<String, Object>>());
		}
		List<Map<String, Object>> wcInSortlist = SortUtils.getIn(crcdTransactionList);
		if (StringUtil.isNullOrEmpty(wcInSortlist)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_nullin));
			return;
		}
		setUnSettledBillData(wcInSortlist);
	}

	/** 未出账单---支出排序 */
	private void wcOutSort() {
		cleanMoreButton();
		if (ctfAdapter != null) {
			ctfAdapter.changeDate(new ArrayList<Map<String, Object>>());
		}
		List<Map<String, Object>> wcOutSortlist = SortUtils.getOut(crcdTransactionList);
		if (wcOutSortlist == null || wcOutSortlist.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_nullout));
			return;
		}
		setUnSettledBillData(wcOutSortlist);
	}
	
	private void setUnSettledBillData(List<Map<String, Object>> data){
		if(ctfAdapter == null){
			ctfAdapter = new CrcdUnsettledBillAdapter2(this, data);
			unsettledBillsLv.setAdapter(ctfAdapter);
		}else{
			ctfAdapter.changeDate(data);
		}
		ctfAdapter.setIsWrOrYr(isWrOrYr);
	}
	
	/** 移除更多按钮 */
	private void cleanMoreButton() {
		if (unsettledBillsLv.getFooterViewsCount() > 0) {
			unsettledBillsLv.removeFooterView(footerView);
		}

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_CANCELED){
//			PopupWindowUtils.getInstance().showQueryPopupWindow();
			SearchView.getInstance().showSearchView();
			if(radioloanGroup.getCheckedRadioButtonId() == R.id.loan_his_btn2)
				ll_upLayout.setClickable(false);
			else{
				if(!StringUtil.isNullOrEmpty(crcdTransactionList))
					ll_upLayout.setClickable(true);
				else
					ll_upLayout.setClickable(false);
			}
		}
	}
	
	// 603 add
	public void btnCardTransYrOnclick(View v){
		isWrOrYr=true;
		currentIndex=0;
		BaseHttpEngine.showProgressDialog();
		_refresh = true;
		if(!StringUtil.isNullOrEmpty(unsettledBillTotal))
			unsettledBillTotal.clear();
		requestPsnAccBocnetQryCrcdUnsettledTotal();	
	}
	public void btnCardTransWrOnclick(View v){
		isWrOrYr=false;
		currentIndex=0;
		BaseHttpEngine.showProgressDialog();
		_refresh = true;
		if(!StringUtil.isNullOrEmpty(unsettledBillTotal))
			unsettledBillTotal.clear();
		requestPsnAccBocnetQryUnauthorizedTotal();
	}

	private void requestPsnAccBocnetQryUnauthorizedTotal() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bocnet.METHODACCBOCNETQRYUNAUTHORIZEDTOTAL );
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put(Bocnet.ACCOUNTSEQ, BocnetDataCenter.getInstance().getAccountSeq());
//		param.put(Bocnet.ACCOUNTID, BocnetDataCenter.getInstance().getAccountSeq());
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnAccBocnetQryUnauthorizedTotalCallBack");
	}
	public void requestPsnAccBocnetQryUnauthorizedTotalCallBack(Object resultObj){
		List<Map<String, Object>> list = HttpTools.getResponseResult(resultObj);
		if(StringUtil.isNullOrEmpty(list)){
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_query_noresult_error));
			return;
		}
		if(StringUtil.isNullOrEmpty(unsettledBillTotal))
			unsettledBillTotal = list;
		else
			unsettledBillTotal.addAll(list);
		
		BocnetDataCenter.getInstance().setCrcdUnsettledTotal(unsettledBillTotal);
		setUnsettledTotal(unsettledBillTotal);
		requestConversation();
	}
	
	public void requestPsnAccBocnetQryUnauthorizedTrans(){

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bocnet.METHODACCBOCNETQRYUNAUTHORIZEDTRANS );
		biiRequestBody.setConversationId(BocnetDataCenter.getInstance().getConversationId());
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put(Bocnet.ACCOUNTSEQ, BocnetDataCenter.getInstance().getAccountSeq());
//		param.put(Bocnet.ACCOUNTID, BocnetDataCenter.getInstance().getAccountSeq());
		param.put(Bocnet.PAGESIZE, pageSize + "");
		param.put(Bocnet.CURRENTINDEX, currentIndex + "");
		param.put(Bocnet._REFRESH, _refresh + "");
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnAccBocnetQryUnauthorizedTransCallBack");
		
	}
	public void requestPsnAccBocnetQryUnauthorizedTransCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		acc_query_result_layout.setVisibility(View.VISIBLE);
		crcd_trans_radioGroup.setVisibility(View.VISIBLE);
		setTitle(this.getString(R.string.mycrcd_no_bill));
		ll_upLayout.setOnClickListener(this);
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		if(StringUtil.isNullOrEmpty(map) || StringUtil.isNullOrEmpty(map.get(Bocnet.CRCDTRANSACTIONLIST))){
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_query_noresult_error));
			SearchView.getInstance().showSearchView();
			if (crcdTransactionList != null && !crcdTransactionList.isEmpty()) {
				crcdTransactionList.clear();
			}
			return;
		}
		if(search_condition_view.isShown())
//			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
			SearchView.getInstance().hideSearchView();
		BocnetDataCenter.getInstance().setCrcdUnsettledBills(map);
		recordNum1 = Integer.valueOf((String) map.get(Bocnet.RECORDNUMBER));
		currentIndex += 10;
		setUnsettledBillsData();	
	}
}
