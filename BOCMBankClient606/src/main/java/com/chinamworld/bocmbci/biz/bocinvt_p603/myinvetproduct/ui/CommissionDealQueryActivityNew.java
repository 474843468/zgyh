package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt.dialogActivity.BocBindinglDialogActivity;
import com.chinamworld.bocmbci.biz.bocinvt.inflateView.InflaterViewDialog;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.adapter.CommissionDealQueryAdapter;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.CommissionDealForGeneralInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.CommissionDealForGroupInfo;
import com.chinamworld.bocmbci.biz.plps.adapter.AnnuitySpinnerAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.PublicTools;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 委托交易查询页面
 * 
 * @author niuchf
 * 
 */
public class CommissionDealQueryActivityNew extends BociBaseActivity implements
OnClickListener {
	/** 查询信息页 */
	private View view;
	/** 查询开始日期 */
	private TextView tv_startdate;
	/** 查询结束日期 */
	private TextView tv_enddate;
	/** 交易账户 */
	private TextView spAcct;
	/** 交易类型 */
	private Spinner boci_acct_trfType;
	/** 交易类型 */
	private LinearLayout container_trfType;
	/** 交易币种 */
	private LinearLayout container_account;
	/** 查询日期 */
	private LinearLayout acc_query_date;
	/** 委托日期 */
	private TextView boci_product_name;
	/** 产品名称 */
	private TextView boci_yearlyRR;
	/** 交易类型 */
	private TextView boci_timeLimit;
	/** 灰色分割线 */
	private ImageView img_line;
	// 按钮
	/** 查询 */
	private Button combinateQueryproduct;
	// 查询结果页面
	private LinearLayout query_result;
	/** 查询列表 */
	private ListView queryListView;
	// 查询结果头
	/** 组合查询头 */
	private LinearLayout header_for_combinate;
	/** 查询条件 查询日期、交易类型*/
	private TextView boci_date,boci_tratype;
	/** 查询条件 交易账户*/
	private TextView boci_acct;
	/** 查询日期反显 */
	private LinearLayout date_query_layout;
	/** 查询结果 */
	private List<Map<String, Object>> list;
	/** 系统时间 */
	private String currenttime;
	private String startDate;
	private String endDate;
	/** 收起 */
	private LinearLayout img_up;
	/** 查询条件返回结果 */
	private RelativeLayout load_more;
//	private RelativeLayout rl_query_transfer;
	private LinearLayout combinateLayout;
	private CommissionDealQueryAdapter adapter;
	private Map<String, Object> accItemMap;

//	private ArrayList<BocProductForDeprecateInfo> infos;
	//最近操作的账户
	private String recentAccount;
	private int mIndex;
	private boolean isOpen = false;
	private boolean isevaluation = false;
	
	private ArrayList<CommissionDealForGeneralInfo> generalInfos;
	private ArrayList<CommissionDealForGroupInfo> groupInfos;
	private String ibknum;
	private String typeOfAccount;
	
	
	/** 登记账户信息 */
	private List<Map<String, Object>> investBindingInfo = new ArrayList<Map<String, Object>>();
	private LinearLayout rl_query_transfer,ll_query_result_header;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getResources().getString(R.string.bocinvt_query_commission_deal));
		//requestSystemDateTime();
		//initfastfoot();
		requestCommConversationId();
		BiiHttpEngine.showProgressDialogCanGoBack();
//		infos = new ArrayList<BocProductForDeprecateInfo>();
		adapter = new CommissionDealQueryAdapter();
		setBackBtnClick(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				Intent intent = new Intent(CommissionDealQueryActivityNew.this,
						MyInvetProductActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	/** 初始化界面 */
	private void init() {// 添加布局
		view = addView(R.layout.bocinvt_commissionquery_activity_p603);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0,
					0, 0);
		}
		date_query_layout = (LinearLayout) view.findViewById(R.id.date_query_layout);
		date_query_layout.setVisibility(View.GONE);
		img_line = (ImageView) view.findViewById(R.id.img_line);
		//加载 交易查询主页面顶部条件选择框
//		rl_query_transfer = (RelativeLayout) LayoutInflater.from(this).inflate(
//				R.layout.bocinvt_commissionquery_condition, null);
//		PopupWindowUtils.getInstance().getQueryPopupWindow(rl_query_transfer,
//				BaseDroidApp.getInstanse().getCurrentAct());
//		PopupWindowUtils.getInstance().showQueryPopupWindowFirst();
		rl_query_transfer = (LinearLayout) view.findViewById(R.id.ll_query_condition);
		ll_query_result_header = (LinearLayout) view.findViewById(R.id.query_result_header);
		boci_acct_trfType = (Spinner) rl_query_transfer.findViewById(R.id.boci_acct_trfType);
		spAcct = (TextView) rl_query_transfer
				.findViewById(R.id.boci_acct_trfAcc);//交易账户
		container_trfType = (LinearLayout) rl_query_transfer.findViewById(R.id.container_trfType);
		container_account = (LinearLayout) rl_query_transfer.findViewById(R.id.container_account);
		acc_query_date = (LinearLayout) rl_query_transfer.findViewById(R.id.acc_query_date);
		acc_query_date.setVisibility(View.GONE);
		container_trfType.setVisibility(View.VISIBLE);
		container_account.setVisibility(View.GONE);
		rl_query_transfer.setVisibility(View.VISIBLE);
		ll_query_result_header.setVisibility(View.GONE);
		clickSpinner();
		spAcct.setOnClickListener(acctOnClick);
		if(!spAcct.getText().toString().trim().equals("")){//初始化页面时 存储默认账户信息
			accItemMap = BociDataCenter.getInstance().getBocinvtAcctList().get(0);
		}
		if (!StringUtil.isNull(recentAccount)) {
			List<Map<String,Object>> list = BociDataCenter.getInstance().getBocinvtAcctList();
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).get(BocInvt.ACCOUNTQUERY_ACCOUNTKEY_RES).equals(recentAccount)) {
					spAcct.setText(StringUtil.getForSixForString((String)list.get(i).get(BocInvt.ACCOUNTNO)));
					mIndex = i; 
					accItemMap = list.get(i);
					break;
				}
			}
		}else{
			if (BociDataCenter.getInstance().getBocinvtAcctList().size() == 1) {
				spAcct.setText(PublicTools.getSpinnerData(BociDataCenter.getInstance().getBocinvtAcctList(), BocInvt.ACCOUNTNO).get(mIndex));
			}
		}
		// 按钮初始化
		combinateQueryproduct = (Button) rl_query_transfer
				.findViewById(R.id.btn_combinate_queryproduct);
		combinateQueryproduct.setOnClickListener(this);
		img_up = (LinearLayout) rl_query_transfer.findViewById(R.id.ll_up);
		tv_startdate = (TextView) rl_query_transfer
				.findViewById(R.id.acc_query_transfer_startdate);
		tv_enddate = (TextView) rl_query_transfer
				.findViewById(R.id.acc_query_transfer_enddate);
		combinateLayout = (LinearLayout) rl_query_transfer
				.findViewById(R.id.ll_combinate_queryproduct);
		// 三天前的日期
		String startThreeDate = QueryDateUtils.getlastthreeDate(dateTime);
		tv_startdate.setText(startThreeDate);
		// 系统当前时间格式化
		currenttime = QueryDateUtils.getcurrentDate(dateTime);
		// 初始结束时间赋值
		tv_enddate.setText(currenttime);
		tv_startdate.setOnClickListener(chooseDateClick);
		tv_enddate.setOnClickListener(chooseDateClick);
		// 收起事件监听
		img_up.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断是否已经进行了查询—如果已经进行:收起所有;否则收起查询条件区域
				if (list == null || list.size() == 0) {
					// 隐藏所有

				} else {
					PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
					query_result.setVisibility(View.VISIBLE);
					ll_query_result_header.setVisibility(View.VISIBLE);
					rl_query_transfer.setVisibility(View.GONE);
					initHead(boci_acct_trfType.getSelectedItemPosition());
				}
			}
		});
		boci_product_name = (TextView) view.findViewById(R.id.boci_product_name);
		boci_yearlyRR = (TextView) view.findViewById(R.id.boci_yearlyRR);
		boci_timeLimit = (TextView) view.findViewById(R.id.boci_timeLimit);
		boci_yearlyRR.setSingleLine(false);
		boci_product_name.setSingleLine(false);
		boci_timeLimit.setSingleLine(false);
		query_result = (LinearLayout) view.findViewById(R.id.ll_query_result);

		boci_date = (TextView) view.findViewById(R.id.tv_boci_date_value);//查询日期
		boci_tratype = (TextView) view.findViewById(R.id.textview_tratype_value);//交易类型
		queryListView = (ListView) view.findViewById(R.id.boci_query_list);
		boci_acct = (TextView) view.findViewById(R.id.bocinvt_acct_value3);//交易账户
		// 查询结果头返回
		ImageView boci_query_back = (ImageView) view
				.findViewById(R.id.img_boci_query_back);
		boci_query_back.setOnClickListener(backQueryClick);
		header_for_combinate = (LinearLayout) view
				.findViewById(R.id.header_for_combinate);
		header_for_combinate.setOnClickListener(backQueryClick);
	}

	/** 账户选择点击事件 */
	OnClickListener acctOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			createDialog(PublicTools.getSpinnerData(BociDataCenter.getInstance().getBocinvtAcctList(), BocInvt.ACCOUNTNO), "理财交易账户",mIndex);
		}
	};
	
	/**
	 * 创建spinner弹窗框
	 * @param list
	 * @param title
	 * @param position
	 */
	private void createDialog(List<String> list,String title,int position){
		ListView mListView = new ListView(this);
		mListView.setFadingEdgeLength(0);
		mListView.setOnItemClickListener(acctItemOnClick);
		mListView.setScrollingCacheEnabled(false);
		AnnuitySpinnerAdapter mAdapter = new AnnuitySpinnerAdapter(this, list,position);
		mListView.setAdapter(mAdapter);
		BaseDroidApp.getInstanse().showSpinnerDialog(mListView, title);
	}
	
	/**
	 * 账户列表点击事件
	 */
	private OnItemClickListener acctItemOnClick = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			mIndex = position;
			accItemMap = BociDataCenter.getInstance().getBocinvtAcctList().get(mIndex);
			BaseDroidApp.getInstanse().dismissMessageDialog();
			spAcct.setText(PublicTools.getSpinnerData(BociDataCenter.getInstance().getBocinvtAcctList(), BocInvt.ACCOUNTNO).get(position));
		}
	};
	
	/** 设置查询日期 */
	OnClickListener chooseDateClick = new OnClickListener() {

		@Override
		public void onClick(final View v) {
			TextView tv = (TextView) v;
			String time = tv.getText().toString();
			int startYear = Integer.parseInt(time.substring(0, 4));
			int startMonth = Integer.parseInt(time.substring(5, 7));
			int startDay = Integer.parseInt(time.substring(8, 10));
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			DatePickerDialog dialog = new DatePickerDialog(
					CommissionDealQueryActivityNew.this, new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							StringBuilder date = new StringBuilder();
							date.append(String.valueOf(year));
							date.append("/");
							int month = monthOfYear + 1;
							date.append(((month < 10) ? ("0" + month)
									: (month + "")));
							date.append("/");
							date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth)
									: (dayOfMonth + "")));
							// 为日期赋值
							((TextView) v).setText(String.valueOf(date));
						}
					}, startYear, startMonth - 1, startDay);
			dialog.show();
		}
	};
	/**
	 * 中银理财 查询
	 */
	public void queryProduct() {
		startDate = tv_startdate.getText().toString().trim();
		endDate = tv_enddate.getText().toString().trim();

		if (QueryDateUtils.compareDateOneYear(startDate, dateTime)) {
			// 起始日期不能早于系统当前日期一年前
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					CommissionDealQueryActivityNew.this
					.getString(R.string.acc_check_start_enddate));
			return;
		}
		if (QueryDateUtils.compareDate(endDate, dateTime)) {
			// 结束日期在服务器日期之前
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					CommissionDealQueryActivityNew.this
					.getString(R.string.acc_check_enddate));
			return;
		}
		if (QueryDateUtils.compareDate(startDate, endDate)) {
			// 开始日期在结束日期之前

		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					CommissionDealQueryActivityNew.this
					.getString(R.string.acc_query_errordate));
			return;
		}

		if (QueryDateUtils.compareDateThree(startDate, endDate)) {
			// 起始日期与结束日期最大间隔为三个自然月
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					CommissionDealQueryActivityNew.this
					.getString(R.string.acc_check_start_end_date));
			return;
		}
		if (queryListView.getFooterViewsCount() > 0) {
			queryListView.removeFooterView(load_more);
		}
//		infos.clear();
		requestdeprecateProductList(this.startDate, this.endDate, true);
		
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		init();
		BiiHttpEngine.dissMissProgressDialog();
	}
	
	/**
	 * 组合购买查询
	 * @param startDate
	 * @param endDate
	 * @param refresh
	 */
	private void requestdeprecateProductList(String startDate, String endDate,
			boolean refresh) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
//		map.put("pageSize", 15);
//		map.put("_refresh", refresh + "");
		map.put("accountKey", accItemMap.get(BocInvt.ACCOUNTQUERY_ACCOUNTKEY_RES));
//		map.put("currentIndex", 0);
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod("PsnXpadAutComTradStatus");
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestAutComTradStatusCallback");
		BaseHttpEngine.showProgressDialog();
	}

	/**
	 * 组合购买查询回调
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestAutComTradStatusCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		String recordNumber = (String) map.get(BocInvt.PROGRESS_RECORDNUM);
		List<Map<String, Object>> listFirst = (List<Map<String, Object>>) map.get(BocInvt.BOCI_LIST_RES);
		if (StringUtil.isNullOrEmpty(listFirst)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.acc_transferquery_null));
			return;
		}else{
			list = (List<Map<String, Object>>) map.get(BocInvt.BOCI_LIST_RES);
			groupInfos = new ArrayList<CommissionDealForGroupInfo>();
			for (Map<String, Object> mapInfo : list) {
				groupInfos.add(new CommissionDealForGroupInfo(mapInfo));
			}
			boci_tratype.setText(spAcctTypeText);//交易币种
			date_query_layout.setVisibility(View.VISIBLE);
			boci_date.setText(startDate + "-" + endDate);
			boci_acct.setText(spAcctAccountText);//交易账户
//			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
			rl_query_transfer.setVisibility(View.GONE);
			ll_query_result_header.setVisibility(View.VISIBLE);
//			ArrayList<BocProductForDeprecateInfo> infos = new ArrayList<BocProductForDeprecateInfo>();
//			for (Map<String, Object> mapInfo : list) {
//				infos.add(new BocProductForDeprecateInfo(mapInfo));
//			}
//			this.infos.addAll(infos);
			initHead(boci_acct_trfType.getSelectedItemPosition());
			// 配置适配器
			adapter = new CommissionDealQueryAdapter(this,list,boci_acct_trfType.getSelectedItemPosition());
			queryListView.setAdapter(adapter);
			queryListView.setOnItemClickListener(onListClickListenerGroup);
		}
	}
	
	/** 常规交易查询列表点击事件 */
	OnItemClickListener onListClickListenerGeneral = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
//			BaseDroidApp.getInstanse().getBizDataMap().
//				put("bocinvtDeprecateDetailMap",list.get(position));
			Intent intent = new Intent(CommissionDealQueryActivityNew.this, 
					CommissionDealDetailActivity.class);
			intent.putExtra("info", generalInfos.get(position));
			startActivity(intent);
		}

	};
	
	/** 组合购买查询列表点击事件 */
	OnItemClickListener onListClickListenerGroup = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
//			BaseDroidApp.getInstanse().getBizDataMap().
//				put("bocinvtDeprecateDetailMap",list.get(position));
			Intent intent = new Intent(CommissionDealQueryActivityNew.this, 
					CommissionDealDetailActivity.class);
			intent.putExtra("info", groupInfos.get(position));
			intent.putExtra("ibknum", ibknum);
			intent.putExtra("typeOfAccount", typeOfAccount);
			startActivity(intent);
		}

	};
	
	/** 查询结果头倒三角点击监听事件 */
	OnClickListener backQueryClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			combinateLayout.setBackgroundResource(R.drawable.img_bg_query_no);
			rl_query_transfer.setVisibility(View.VISIBLE);
			ll_query_result_header.setVisibility(View.GONE);
//			PopupWindowUtils.getInstance().getQueryPopupWindow(
//					rl_query_transfer,
//					BaseDroidApp.getInstanse().getCurrentAct());
//			PopupWindowUtils.getInstance().showQueryPopupWindow();
		}
	};
	private String spAcctTypeText,spAcctAccountText;//交易账户  交易类型 
	@Override
	public void onClick(View v) {
		if (spAcct.getText().toString().equals("请选择账户")) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请选择理财交易账户"); return;
		}
		spAcctTypeText = boci_acct_trfType.getSelectedItem().toString();
		spAcctAccountText = spAcct.getText().toString();
		switch (v.getId()) {
		case R.id.btn_combinate_queryproduct://查询
			ibknum=(String)accItemMap.get(BocInvt.IBKNUMBER);
			typeOfAccount=(String)accItemMap.get(BocInvt.BOCINVT_BINDING_ACCOUNTTYPE_RES);
			if (!StringUtil.isNullOrEmpty(list)) {
				list.clear();
			}
			adapter.changeData();
			combinateLayout.setBackgroundResource(R.drawable.img_bg_query_j);
			//常规交易
			if(boci_acct_trfType.getSelectedItemPosition() == 0){
				requestGeneralCommissionDealInfos();
			}
			//组合购买
			if(boci_acct_trfType.getSelectedItemPosition() == 1){
				queryProduct();
			}
			
			break;
		default:
			break;
		}
	}

	/**
	 * 常规交易查询
	 */
	private void requestGeneralCommissionDealInfos() {
		HashMap<String, Object> map = new HashMap<String, Object>();
//		map.put("pageSize", 15);
//		map.put("_refresh", true);
		map.put("accountKey", accItemMap.get(BocInvt.ACCOUNTQUERY_ACCOUNTKEY_RES));
//		map.put("currentIndex", 0);
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod("PsnXpadAutTradStatus");
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestGeneralCommissionDealInfosCallback");
	}

	/**
	 * 常规交易查询回调
	 */
	@SuppressWarnings("unchecked")
	public void requestGeneralCommissionDealInfosCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		String recordNumber = (String) map.get(BocInvt.PROGRESS_RECORDNUM);
		List<Map<String, Object>> listFirst = (List<Map<String, Object>>) map
				.get(BocInvt.BOCI_LIST_RES);
		if (StringUtil.isNullOrEmpty(listFirst)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.acc_transferquery_null));
			return;
		}else{
			list = (List<Map<String, Object>>) map.get(BocInvt.BOCI_LIST_RES);
			generalInfos = new ArrayList<CommissionDealForGeneralInfo>();
			for (Map<String, Object> mapInfo : list) {
				generalInfos.add(new CommissionDealForGeneralInfo(mapInfo));
			}
			boci_tratype.setText(spAcctTypeText);//交易类型
			date_query_layout.setVisibility(View.GONE);
			boci_acct.setText(spAcctAccountText);//交易账户
//			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
			rl_query_transfer.setVisibility(View.GONE);
			ll_query_result_header.setVisibility(View.VISIBLE);
			initHead(boci_acct_trfType.getSelectedItemPosition());
			// 配置适配器
			adapter = new CommissionDealQueryAdapter(this,list,boci_acct_trfType.getSelectedItemPosition());
			queryListView.setAdapter(adapter);
			queryListView.setOnItemClickListener(onListClickListenerGeneral);
		}
	}
	/**
	 * spinner初始化
	 */
	private void clickSpinner() {
		ArrayAdapter<String> transactionAdapter = new ArrayAdapter<String>(
				this, R.layout.custom_spinner_item, getResources().getStringArray(
								R.array.bocinvt_query_box_transaction_type));
		transactionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		boci_acct_trfType.setAdapter(transactionAdapter);
		boci_acct_trfType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				switch (position) {
				case 0:// 常规交易
					acc_query_date.setVisibility(View.GONE);
					break;
				case 1:// 组合查询
					acc_query_date.setVisibility(View.VISIBLE);
					break;
				default:
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	/*
	 * 给列表头赋值
	 */
	private void initHead(int type) {
		boci_product_name.setText(R.string.bocinvt__commission_date2);
		boci_yearlyRR.setText(R.string.bocinvt__product_name2);
		if(type == 0){
			boci_timeLimit.setText(R.string.bocinvt__deal_type2);
		}
		if(type == 1){
			boci_timeLimit.setText(R.string.bocinvt__buy_amount);
		}
		img_line.setVisibility(View.VISIBLE);
	}
	/**
	 * 请求最近操作理财账户
	 */
	public void requestRecentAccount() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.RECENTACCOUNTQUERY);
		HttpManager.requestBii(biiRequestBody, this,
				"requestRecentAccountCallBack");
	}

	@SuppressWarnings("unchecked")
	public void requestRecentAccountCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		if (!StringUtil.isNullOrEmpty(result)) {
			recentAccount = (String) result.get(BocInvt.ACCOUNTQUERY_ACCOUNTKEY_RES);
		}
		requestSystemDateTime();

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_FIRST_USER:
			if (!PopupWindowUtils.getInstance().isQueryPopupShowing()) {
				combinateLayout.setBackgroundResource(R.drawable.img_bg_query_no);
//				PopupWindowUtils.getInstance().getQueryPopupWindow(
//						rl_query_transfer, CommissionDealQueryActivityNew.this);
//				PopupWindowUtils.getInstance().showQueryPopupWindow();
				rl_query_transfer.setVisibility(View.VISIBLE);
				ll_query_result_header.setVisibility(View.GONE);
			}
			break;
		case RESULT_OK:
			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
				// 开通成功的响应
				isOpenBefore = true;
				isOpen = true;
			} else if (requestCode == ConstantGloble.ACTIVITY_REQUEST_INVTBINDING_CODE) {
				// 登记账户成功的响应
				investBinding = (List<Map<String, Object>>) BaseDroidApp
						.getInstanse().getBizDataMap()
						.get(ConstantGloble.BOCINVT_XPADRESET_CHOOSE);
				investBindingInfo = (List<Map<String, Object>>) BaseDroidApp
						.getInstanse().getBizDataMap()
						.get(ConstantGloble.BOCINVT_XPADRESET_CHOOSE);
			} else if (requestCode == ConstantGloble.ACTIVITY_REQUEST_INVTEVALUATION_CODE) {
				// 风险评估成功的响应
				if (BociDataCenter.getInstance().getI() == 1) {

				} else {
//					isevaluated = true;
//					isevaluation = true;
				}
			} else {
				if (queryListView.getFooterViewsCount() > 0) {
					queryListView.removeFooterView(load_more);
				}
//				requestdeprecateProductList(this.startDate, this.endDate, true);
				break;
			}
			if (isOpen && !StringUtil.isNullOrEmpty(investBindingInfo)
					/*&& isevaluation*/) {
				requestRecentAccount();
//				BaseHttpEngine.showProgressDialogCanGoBack();
			} else {
				InflaterViewDialog inflater = new InflaterViewDialog(
						CommissionDealQueryActivityNew.this);
//				dialogView2 = (RelativeLayout) inflater.judgeViewDialog(isOpen,
//						investBindingInfo, isevaluation, manageOpenClick,
//						invtBindingClick, invtEvaluationClick, exitClick);
				dialogView2 = (RelativeLayout) inflater.judgeViewDialog_choice(isOpen,
						investBindingInfo, true, manageOpenClick, invtBindingClick, null, exitDialogClick);
				TextView tv = (TextView) dialogView2
						.findViewById(R.id.tv_acc_account_accountState);
				// 查询
				tv.setText(this.getString(R.string.bocinvt_tv_76));

				BaseDroidApp.getInstanse()
				.showAccountMessageDialog(dialogView2);
			}

			break;
		case RESULT_CANCELED:
			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE
			|| requestCode == ConstantGloble.ACTIVITY_REQUEST_INVTBINDING_CODE
			|| requestCode == ConstantGloble.ACTIVITY_REQUEST_INVTEVALUATION_CODE) {

			} else {

				break;
			}
			if (isOpen && !StringUtil.isNullOrEmpty(investBindingInfo)
					/*&& isevaluation*/) {
			} else {
				InflaterViewDialog inflater = new InflaterViewDialog(
						CommissionDealQueryActivityNew.this);
//				dialogView2 = (RelativeLayout) inflater.judgeViewDialog(isOpen,
//						investBindingInfo, isevaluation, manageOpenClick,
//						invtBindingClick, invtEvaluationClick, exitClick);
				dialogView2 = (RelativeLayout) inflater.judgeViewDialog_choice(isOpen,
						investBindingInfo, true, manageOpenClick, invtBindingClick, null, exitDialogClick);
				TextView tv = (TextView) dialogView2
						.findViewById(R.id.tv_acc_account_accountState);
				// 查询
				tv.setText(this.getString(R.string.bocinvt_tv_76));
				BaseDroidApp.getInstanse()
				.showAccountMessageDialog(dialogView2);

			}
			break;
		}
	}
	protected OnClickListener exitClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
			finish();
		}
	};
	/**
	 * 账户余额查询
	 */
	public void requestAccBankAccountDetail() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.QRY_ACC_BALANCE_API);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Comm.ACCOUNT_ID, (String)BociDataCenter.getInstance().getBocinvtAcctList()
				.get(mIndex).get(Comm.ACCOUNT_ID));
		biiRequestBody.setParams(paramsmap);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"accBankAccountDetailCallback");

	}

	@SuppressWarnings("unchecked")
	public void accBankAccountDetailCallback(Object resultObj) {
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		List<Map<String, Object>> balanceList = (List<Map<String, Object>>) map
				.get(Acc.DETAIL_ACCOUNTDETAILIST_RES);
		if (StringUtil.isNullOrEmpty(balanceList)) return;
		BociDataCenter.getInstance().setBalanceList(balanceList);
		BaseHttpEngine.dissMissProgressDialog();
		startActivityForResult((new Intent(this, BocBindinglDialogActivity.class).putExtra("p", mIndex)),1004);
	}

	@Override
	public void bocinvtAcctCallback(Object resultObj) {
		super.bocinvtAcctCallback(resultObj);
		isOpen = isOpenBefore;
//		isevaluation = isevaluated;
		investBindingInfo = investBinding;
		BaseDroidApp.getInstanse().getBizDataMap()
		.put(ConstantGloble.BOCINVT_XPADRESET_CHOOSE, investBinding);
		if (isOpenBefore && !StringUtil.isNullOrEmpty(investBinding)
				/*&& isevaluated*/) {
			requestRecentAccount();
//			BaseHttpEngine.showProgressDialogCanGoBack();
		} else {
			// 得到result
			BaseHttpEngine.dissMissProgressDialog();
			InflaterViewDialog inflater = new InflaterViewDialog(
					CommissionDealQueryActivityNew.this);
//			dialogView2 = (RelativeLayout) inflater.judgeViewDialog(
//					isOpenBefore, investBinding, isevaluated, manageOpenClick,
//					invtBindingClick, invtEvaluationClick, exitClick);
			dialogView2 = (RelativeLayout) inflater.judgeViewDialog_choice(isOpen,
					investBindingInfo, true, manageOpenClick, invtBindingClick, null, exitDialogClick);
			TextView tv = (TextView) dialogView2
					.findViewById(R.id.tv_acc_account_accountState);
			// 查询
			tv.setText(this.getString(R.string.bocinvt_tv_76));

			BaseDroidApp.getInstanse().showAccountMessageDialog(dialogView2);

		}
	}
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		initfastfoot();
	}
	
	/**
	 * 请求conversationId 来登录之外的conversation出Id
	 */
	public void requestCommConversationId() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.CONVERSATION_ID_API);
		HttpManager.requestBii(biiRequestBody, this,
				"requestCommConversationIdCallBack");
	}
	
	/** 用于区分是否是快捷键启动 */
	public void initfastfoot() {
		//		if (getIntent().getBooleanExtra(ConstantGloble.COMEFROMFOOTFAST, false)) {
		// 快捷键启动
		// 判断用户是否开通投资理财服务
		requestPsnInvestmentisOpenBefore();
		//		} else {
		//			requestRecentAccount();
		//		}
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		ActivityTaskManager.getInstance().removeAllSecondActivity();
		Intent intent = new Intent(CommissionDealQueryActivityNew.this,
				MyInvetProductActivity.class);
		startActivity(intent);
		finish();
	}
}
