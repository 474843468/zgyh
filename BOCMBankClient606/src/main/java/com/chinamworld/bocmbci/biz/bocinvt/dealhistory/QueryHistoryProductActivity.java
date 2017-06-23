package com.chinamworld.bocmbci.biz.bocinvt.dealhistory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.SecondMainActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt.dialogActivity.BocBindinglDialogActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.tradequery.InverstQueryDetailActivity;
import com.chinamworld.bocmbci.biz.investTask.BOCDoThreeTask;
import com.chinamworld.bocmbci.biz.investTask.IAction;
import com.chinamworld.bocmbci.biz.plps.adapter.AnnuitySpinnerAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.mode.IFunc;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.PublicTools;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;

/**
 * 产品历史查询页面
 * 
 * @author wangmengmeng
 * 
 */
public class QueryHistoryProductActivity extends BociBaseActivity implements
OnClickListener,ICommonAdapter<Map<String,Object>> {
	/** 查询信息页 */
	private View view;
	/** 查询开始日期 */
	private TextView tv_startdate;
	/** 查询结束日期 */
	private TextView tv_enddate;
	// 下拉列表
	/** 产品种类 */
	//	private Spinner productType;
	//	private String type;
	//	private String typename;
	/** 产品币种 */
	private Spinner productCurCode,spAcctCurrency,spAcctType;

	private String curcode;
	// 按钮
	/** 组合查询 */
	private Button combinateQueryproduct;
	// 查询结果页面
	private LinearLayout query_result;
	/** 查询列表 */
	private ListView queryListView;
	// 查询结果头
	/** 组合查询头 */
	private LinearLayout header_for_combinate;
	/** 产品种类 */
	//	private TextView boci_producttype;
	/** 产品币种 */
	private TextView boci_currency;
	/** 查询条件 */
	private TextView boci_date,boci_acct,spAcct,boci_tratype;
	// 下拉列表内容
	/** 产品种类 */
	//	private List<Map<String, String>> productTypeList;
	/** 组合查询结果 */
	private List<Map<String, Object>> combinateList;
	/** 单项产品详情查询结果 **/
	private Map<String, Object> infoDetailMap;
	/** 系统时间 */
	private String currenttime;
	private String startDate;
	private String endDate;
	/** 查询一周 */
	private Button btn_query_onweek;
	/** 查询一个月 */
	private Button btn_query_onmonth;
	/** 查询三个月 */
	private Button btn_query_threemonth;
	/** 收起 */
	private LinearLayout img_up;
	/** 查询条件返回结果 */
	//	private Map<String, Object> preTradMap;
	private RelativeLayout load_more,rl_tranhistory;
	private Button btn_load_more;
	private int recordNumber = 0;
	private int loadNumber = 0;
	/**自定义adapter**/
//	private MyProductQueryAdapter adapter;
	/**采用通用适配器接口实现**/
	private CommonAdapter<Map<String, Object>> adapter;
	private LinearLayout ll_query_transfer,ll_query_result_header;
	private LinearLayout combinateLayout;
	private boolean isOpen = false;
	private boolean isevaluation = false;
	/** 登记账户信息 */
	private List<Map<String, Object>> investBindingInfo = new ArrayList<Map<String, Object>>();
	private int mIndex;
	private Map<String, Object> accItemMap;
	//最近操作的账户
	private String recentAccount;
	//最近操作账户的accountkey
	private String recentAccAccountKey;
	
	
	private Intent intentTemp;
//	private 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
//		setTitle(this.getResources().getString(R.string.bocinvt_historyquery_titile));
		setTitle(this.getResources().getString(R.string.bocinvt_history_titile));
		Button btn_right = (Button) findViewById(R.id.ib_top_right_btn);
		btn_right.setVisibility(View.GONE);
		setBackBtnClick(backBtnClick);
		setLeftSelectedPosition("bocinvtManager_6");
		BOCDoThreeTask task = BOCDoThreeTask.getInstance(this);
		registActivityEvent(task);
		task.doTask(new IAction() {
			@Override
			public void SuccessCallBack(Object param) {
				requestCommConversationId();
//				requestRecentAccount();
				BaseHttpEngine.showProgressDialogCanGoBack();
			}
		},2);
	}
	/** 返回点击事件 */
	OnClickListener backBtnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			ActivityTaskManager.getInstance().removeAllSecondActivity();
//			Intent intent = new Intent(QueryHistoryProductActivity.this,
//					SecondMainActivity.class);
//			startActivity(intent);
			goToMainActivity();
		}
	};
	/** 初始化界面 */
	private void init() {// 添加布局
		view = addView(R.layout.bocinvt_historyquery_activity);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0,
					0, 0);
		}
		//加载 交易查询主页面顶部条件选择框
//		ll_query_transfer = (RelativeLayout) LayoutInflater.from(this).inflate(
//				R.layout.bocinvt_historyquery_condition, null);
//		PopupWindowUtils.getInstance().getQueryPopupWindow(ll_query_transfer,
//				BaseDroidApp.getInstanse().getCurrentAct());
//		PopupWindowUtils.getInstance().showQueryPopupWindowFirst();
		ll_query_transfer = (LinearLayout) view.findViewById(R.id.ll_combinate_queryproduct);
		ll_query_result_header = (LinearLayout) view.findViewById(R.id.query_result_header);
		rl_tranhistory = (RelativeLayout) view.findViewById(R.id.rl_tranhistory);
		spAcct = (TextView) ll_query_transfer
				.findViewById(R.id.boci_acct_trfAcc);//交易账户
		spAcctType = (Spinner) ll_query_transfer.findViewById(R.id.boci_acct_trfType);//交易类型
		spAcctCurrency = (Spinner) ll_query_transfer.findViewById(R.id.boci_acct_trfCurrency);//交易币种

		//		productType = (Spinner) ll_query_transfer
		//				.findViewById(R.id.boci_productType_query);
		productCurCode = (Spinner) ll_query_transfer
				.findViewById(R.id.boci_productCurCode_query);
//		if(!spAcct.getText().toString().trim().equals("")){//初始化页面时 存储默认账户信息
//			accItemMap = BociDataCenter.getInstance().getBocinvtAcctList().get(0);
//		}
		// 按钮初始化
		combinateQueryproduct = (Button) ll_query_transfer
				.findViewById(R.id.btn_combinate_queryproduct);
		combinateQueryproduct.setOnClickListener(this);
		img_up = (LinearLayout) ll_query_transfer.findViewById(R.id.ll_up);
		tv_startdate = (TextView) ll_query_transfer
				.findViewById(R.id.acc_query_transfer_startdate);
		tv_enddate = (TextView) ll_query_transfer
				.findViewById(R.id.acc_query_transfer_enddate);
		combinateLayout = (LinearLayout) ll_query_transfer
				.findViewById(R.id.ll_combinate_queryproduct);
		// 三天前的日期
		String startThreeDate = QueryDateUtils.getlastthreeDate(dateTime);
		tv_startdate.setText(startThreeDate);
//		// 一周前的日期
//		String startLastWeekDate = QueryDateUtils.getlastWeek(dateTime);
//		tv_startdate.setText(startLastWeekDate);
		// 系统当前时间格式化
		currenttime = QueryDateUtils.getcurrentDate(dateTime);
		// 初始结束时间赋值
		tv_enddate.setText(currenttime);
		tv_startdate.setOnClickListener(chooseDateClick);
		tv_enddate.setOnClickListener(chooseDateClick);
		btn_query_onweek = (Button) ll_query_transfer
				.findViewById(R.id.btn_acc_onweek);
		btn_query_onmonth = (Button) ll_query_transfer
				.findViewById(R.id.btn_acc_onmonth);
		btn_query_threemonth = (Button) ll_query_transfer
				.findViewById(R.id.btn_acc_threemonth);
		btn_query_onweek.setOnClickListener(this);
		btn_query_onmonth.setOnClickListener(this);
		btn_query_threemonth.setOnClickListener(this);
		// 收起事件监听
		img_up.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断是否已经进行了查询—如果已经进行:收起所有;否则收起查询条件区域
				if (combinateList == null || combinateList.size() == 0) {
					// 隐藏所有

				} else {
//					PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
					query_result.setVisibility(View.VISIBLE);
					ll_query_transfer.setVisibility(View.GONE);
					ll_query_result_header.setVisibility(View.VISIBLE);
				}
			}
		});
		TextView boci_product_name = (TextView) view
				.findViewById(R.id.boci_product_name);
		boci_product_name.setText(this
				.getString(R.string.bocinvt_paymentDatenull));
		TextView boci_yearlyRR = (TextView) view
				.findViewById(R.id.boci_yearlyRR);
		boci_yearlyRR.setText(this.getString(R.string.prod_name));
		TextView boci_timeLimit = (TextView) view
				.findViewById(R.id.boci_timeLimit);
		boci_timeLimit.setText(this.getString(R.string.bocinvt_payPrice));
		boci_yearlyRR.setSingleLine(false);
		boci_product_name.setSingleLine(false);
		boci_timeLimit.setSingleLine(false);
		query_result = (LinearLayout) view.findViewById(R.id.ll_query_result);
		//		boci_producttype = (TextView) view
		//				.findViewById(R.id.tv_boci_producttype_value);
		boci_currency = (TextView) view
				.findViewById(R.id.tv_boci_currency_value);
		boci_date = (TextView) view.findViewById(R.id.tv_boci_date_value);
		//		boci_acct = (TextView) view.findViewById(R.id.bocinvt_acct_value);
		boci_acct = (TextView) view.findViewById(R.id.bocinvt_acct_value3);
		queryListView = (ListView) view.findViewById(R.id.boci_query_list);
		boci_tratype = (TextView) view.findViewById(R.id.textview_tratype_value);
		// 查询结果头返回
		ImageView boci_query_back = (ImageView) view
				.findViewById(R.id.img_boci_query_back);
		boci_query_back.setOnClickListener(backQueryClick);
		header_for_combinate = (LinearLayout) view
				.findViewById(R.id.header_for_combinate);

		header_for_combinate.setOnClickListener(backQueryClick);

		load_more = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.acc_load_more, null);
		btn_load_more = (Button) load_more.findViewById(R.id.btn_load_more);
		btn_load_more.setBackgroundResource(R.color.transparent_00);
		btn_load_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestXpadTradForMore();
			}
		});
		
		adapter = new CommonAdapter<Map<String,Object>>(this, queryListView,combinateList, this);
	    adapter.setTotalNumber(recordNumber);
	    adapter.setRequestMoreDataListener(new IFunc<Boolean>(){

			@Override
			public Boolean callBack(Object param) {
				requestXpadTradForMore();
				return true;
			}
	    	
	    });
		queryListView.setOnItemClickListener(onListClickListener);

		
		
		
		// Spinner点击事件
		clickSpinner();
		spAcct.setOnClickListener(acctOnClick);
		if (!StringUtil.isNull(recentAccAccountKey)) {
			List<Map<String,Object>> list = BociDataCenter.getInstance().getBocinvtAcctList();
			for (int i = 0; i < list.size(); i++) {
				if (recentAccAccountKey.equals(list.get(i).get(BocInvt.ACCOUNTQUERY_ACCOUNTKEY_RES))) {
					spAcct.setText(StringUtil.getForSixForString(String.valueOf(list.get(i).get(BocInvt.ACCOUNTNO))));
					mIndex = i;
					//初始化页面时存储选择账户的信息
					accItemMap = list.get(i);
					BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.BOCINVT_ACCINFO_MAP, accItemMap);
					return;
				}
			}
			spAcct.setText(StringUtil.getForSixForString(String.valueOf(list.get(0).get(BocInvt.ACCOUNTNO))));
			accItemMap = list.get(0);
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.BOCINVT_ACCINFO_MAP, accItemMap);
		}else{
			if (BociDataCenter.getInstance().getBocinvtAcctList().size() == 1) {
				spAcct.setText(PublicTools.getSpinnerData(BociDataCenter.getInstance().getBocinvtAcctList(), BocInvt.ACCOUNTNO).get(mIndex));
			}
		}
		
	
		
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
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.BOCINVT_ACCINFO_MAP, accItemMap);
			BaseDroidApp.getInstanse().dismissMessageDialog();
			spAcct.setText(PublicTools.getSpinnerData(BociDataCenter.getInstance().getBocinvtAcctList(), BocInvt.ACCOUNTNO).get(position));
		}
	};

	/** Spinner点击事件 */
	private void clickSpinner() {
		//		// 产品种类
		//		final List<String> typeNameList = new ArrayList<String>();
		//		final List<String> typeIdList = new ArrayList<String>();
		//		for (int i = 0; i < productTypeList.size(); i++) {
		//			typeNameList.add(productTypeList.get(i).get(
		//					String.valueOf(BocInvt.TYPE_NAME_RES)));
		//			typeIdList.add(productTypeList.get(i).get(
		//					String.valueOf(BocInvt.TYPE_BRANDID_RES)));
		//		}
		// 下拉列表操作
		// 产品种类
		//		ArrayAdapter<ArrayList<String>> adapter1 = new ArrayAdapter(this,
		//				R.layout.custom_spinner_item, typeNameList);
		//		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//		productType.setAdapter(adapter1);
		//		productType.setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		//			@Override
		//			public void onItemSelected(AdapterView<?> parent, View view,
		//					int position, long id) {
		//				type = typeIdList.get(position);
		//				typename = typeNameList.get(position);
		//			}
		//
		//			@Override
		//			public void onNothingSelected(AdapterView<?> parent) {
		//				type = typeIdList.get(0);
		//				typename = typeNameList.get(0);
		//			}
		//		});
		// 产品币种
		ArrayAdapter<ArrayList<String>> adapter2 = new ArrayAdapter(this,
				R.layout.custom_spinner_item,
				LocalData.bocinvtProductCurCode);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//		productCurCode.setAdapter(adapter2);
		spAcctCurrency.setAdapter(adapter2);
		//		productCurCode.setOnItemSelectedListener(new OnItemSelectedListener() {
		spAcctCurrency.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				curcode = LocalData.bocinvtProductCurCode.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				curcode = LocalData.bocinvtProductCurCode.get(0);
			}
		});
	}

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
					QueryHistoryProductActivity.this, new OnDateSetListener() {

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
					QueryHistoryProductActivity.this
					.getString(R.string.acc_check_start_enddate));
			return;
		}
		if (QueryDateUtils.compareDate(endDate, dateTime)) {
			// 结束日期在服务器日期之前
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					QueryHistoryProductActivity.this
					.getString(R.string.acc_check_enddate));
			return;
		}
		if (false == QueryDateUtils.compareDate(startDate, endDate)) {
			// 开始日期在结束日期之前
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					QueryHistoryProductActivity.this
					.getString(R.string.acc_query_errordate));
			return;
		}

		if (QueryDateUtils.compareDateThree(startDate, endDate)) {
			// 起始日期与结束日期最大间隔为三个自然月
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					QueryHistoryProductActivity.this
					.getString(R.string.acc_check_start_end_date));
			return;
		}
//		if (queryListView.getFooterViewsCount() > 0) {
//			queryListView.removeFooterView(load_more);
//		}
		adapter.setSourceList(null,0);
		//发送请求
		if(LocalData.bociTraType.get(spAcctTypeText).equals("01")){//常规交易
			requestXpadTrad();
		}else if(LocalData.bociTraType.get(spAcctTypeText).equals("02")){//组合查询
			requestXpadGuaranty();
		}
		
	}

	private String spAcctTypeText,spAcctCurrencyText;//交易类型  币种 

	/** 事件快捷方式点击事件 */
	@Override
	public void onClick(View v) {
		// 查询事件
		if (spAcct.getText().toString().equals("请选择账户")) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请选择账户"); return;
		}
		//获取选取的交易类型与币种的值
		spAcctTypeText = spAcctType.getSelectedItem().toString();
		spAcctCurrencyText = spAcctCurrency.getSelectedItem().toString();
		switch (v.getId()) {
		case R.id.btn_combinate_queryproduct://查询
			if (!StringUtil.isNullOrEmpty(combinateList)) {
				combinateList.clear();
			}
			adapter.setSourceList(combinateList,0);
			combinateLayout.setBackgroundResource(R.drawable.img_bg_query_j);
			queryProduct();
			return;
		case R.id.btn_acc_onweek:
			// 选择一周
			startDate = QueryDateUtils.getlastWeek(dateTime).trim();
			break;
		case R.id.btn_acc_onmonth:
			// 选择一个月
			startDate = QueryDateUtils.getlastOneMonth(dateTime).trim();
			break;
		case R.id.btn_acc_threemonth:
			// 选择三个月
			startDate = QueryDateUtils.getlastThreeMonth(dateTime).trim();
			break; 
		}
		
		if (!StringUtil.isNullOrEmpty(combinateList)) {
			combinateList.clear();
		}
		adapter.setSourceList(combinateList,0);
		combinateLayout.setBackgroundResource(R.drawable.img_bg_query_j);
		endDate = QueryDateUtils.getcurrentDate(dateTime).trim();
		tv_startdate.setText(startDate);
		tv_enddate.setText(endDate);
		if(LocalData.bociTraType.get(spAcctTypeText).equals("01")){
			requestXpadTrad();
		}else if(LocalData.bociTraType.get(spAcctTypeText).equals("02")){
			requestXpadGuaranty();
		}
		
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestBociAcctList("1","0");
		/** 屏蔽之前判断任务的方式 **/
//		initfastfoot();
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
			//TODO...修改匹配账户标示
//			recentAccount = (String) result.get(BocInvt.ACCOUNTNO);
			recentAccAccountKey = (String) result.get(BocInvt.ACCOUNTQUERY_ACCOUNTKEY_RES);
		}
		requestSystemDateTime();

	}

	//	/** 请求交易状况条件 */
	//	public void requestXpadPreTrad() {
	//		BiiRequestBody biiRequestBody = new BiiRequestBody();
	//		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADPRETRADSTATUS_API);
	//		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
	//				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
	//		biiRequestBody.setParams(null);
	//		HttpManager.requestBii(biiRequestBody, this,
	//				"requestXpadPreTradCallBack");
	//	}

	/**
	 * 请求交易状况条件回调
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	//	public void requestXpadPreTradCallBack(Object resultObj) {
	//		BiiResponse biiResponse = (BiiResponse) resultObj;
	//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
	//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
	//		preTradMap = (Map<String, Object>) biiResponseBody.getResult();
	//		dateTime = (String) preTradMap
	//				.get(BocInvt.BOCINVT_XPADPRETRAD_CURRENTDATE_RES);
	//		BiiHttpEngine.dissMissProgressDialog();
	//		if (StringUtil.isNullOrEmpty(preTradMap)) {
	//			// 结束通讯,隐藏通讯框
	//			BiiHttpEngine.dissMissProgressDialog();
	//			return;
	//		}
	//		// 初始化界面
	//		init();
	//		final List<String> typeNameList = new ArrayList<String>();
	//		final List<String> typeIdList = new ArrayList<String>();
	//		for (int i = 0; i < productTypeList.size(); i++) {
	//			typeNameList.add(productTypeList.get(i).get(
	//					String.valueOf(BocInvt.TYPE_NAME_RES)));
	//			typeIdList.add(productTypeList.get(i).get(
	//					String.valueOf(BocInvt.TYPE_BRANDID_RES)));
	//		}
	//		type = typeIdList.get(0);
	//		typename = typeNameList.get(0);
	//		curcode = LocalData.bocinvtProductCurCode.get(0);
	//		// queryProduct();
	//	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BiiHttpEngine.dissMissProgressDialog();
		curcode = LocalData.bocinvtProductCurCode.get(0);
		init();
	}

	/** 请求常规交易状况 */
	public void requestXpadTrad() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADTRADSTATUS_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
//				paramsmap.put(BocInvt.BOCINCT_XPADTRAD_ACCOUNTID_REQ,
//						preTradMap.get(BocInvt.BOCINVT_XPADPRETRAD_ACCOUNTID_RES));
		//		paramsmap.put(BocInvt.BOCINCT_XPADTRAD_XPADPRODUCTTYPE_REQ, type);
//		Map<String, Object> map = BociDataCenter.getInstance().getBocinvtAcctList().get(mIndex);
		paramsmap.put(BocInvt.BOCINCT_XPADTRAD_ACCOUNTKEY_REQ,accItemMap.get(BocInvt.ACCOUNTQUERY_ACCOUNTKEY_RES));//账户缓存标示
		paramsmap.put(BocInvt.BOCINCT_XPADTRAD_XPADPRODUCTCURCODE_REQ,
				LocalData.bociCurcodeMap.get(curcode));
		paramsmap.put(BocInvt.BOCINCT_XPADTRAD_STARTDATE_REQ, startDate);
		paramsmap.put(BocInvt.BOCINCT_XPADTRAD_ENDDATE_REQ, endDate);
		paramsmap.put(BocInvt.BOCI_CURRENTINDEX_REQ,
				ConstantGloble.LOAN_CURRENTINDEX_VALUE);
		paramsmap.put(BocInvt.BOCI_PAGESIZE_REQ,
				ConstantGloble.LOAN_PAGESIZE_VALUE);
		paramsmap.put(BocInvt.BOCI_REFRESH_REQ,
				ConstantGloble.LOAN_REFRESH_FALSE);
//		paramsmap.put(Comm.ACCOUNT_TYPE,map.get(Comm.ACCOUNT_TYPE));
//		paramsmap.put(BocInvt.BANCACCOUNTNO,map.get(BocInvt.ACCOUNTNO));
//		paramsmap.put(BocInvt.IBKNUM,map.get(BocInvt.IBKNUMBER));
		biiRequestBody.setParams(paramsmap);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestXpadTradCallBack");
		
	}
	/**
	 * 请求 中银理财 组合购买查询
	 */
	public void requestXpadGuaranty(){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADQUERYHISGUARANTYPRODUCTLISTRESULT_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(BocInvt.BOCINVT_XPADQUERY_ACCOUNTkEY_REQ,accItemMap.get(BocInvt.ACCOUNTQUERY_ACCOUNTKEY_RES));//账户缓存标示
		paramsmap.put(BocInvt.BOCINVT_XPADQUERY_CURCODE_REQ,
				LocalData.bociCurcodeMap.get(curcode));//币种
		paramsmap.put(BocInvt.BOCINVT_XPADQUERY_STARTDATE_REQ, startDate);
		paramsmap.put(BocInvt.BOCINVT_XPADQUERY_ENDDATE_REQ, endDate);
		paramsmap.put(BocInvt.BOCINVT_XPADQUERY_CURRENTINDEX_REQ,
				ConstantGloble.LOAN_CURRENTINDEX_VALUE);
		paramsmap.put(BocInvt.BOCINVT_XPADQUERY_PAGESIZE_REQ,
				ConstantGloble.LOAN_PAGESIZE_VALUE);
		paramsmap.put(BocInvt.BOCINVT_XPADQUERY_REFRESH_REQ,
				ConstantGloble.LOAN_REFRESH_FALSE);
//		Map<String, Object> map = BociDataCenter.getInstance().getBocinvtAcctList().get(mIndex);
//		paramsmap.put(Comm.ACCOUNT_TYPE,map.get(Comm.ACCOUNT_TYPE));
//		paramsmap.put(BocInvt.BANCACCOUNTNO,map.get(BocInvt.ACCOUNTNO));
//		paramsmap.put(BocInvt.IBKNUM,map.get(BocInvt.IBKNUMBER));
		biiRequestBody.setParams(paramsmap);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestXpadGuarantyCallBack");
	}

	/**
	 * 请求常规交易状况回调
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void requestXpadTradCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 结束通讯,隐藏通讯框
		BiiHttpEngine.dissMissProgressDialog();
		Map<String, Object> combinateMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(combinateMap)) {
			return;
		}
		recordNumber = Integer.valueOf((String) combinateMap
				.get(Acc.RECORDNUMBER_RES));
		List<Map<String, Object>> combinateListFirst = (List<Map<String, Object>>) combinateMap
				.get(BocInvt.BOCI_LIST_RES);
		if (combinateListFirst == null || combinateListFirst.size() == 0) {
			ll_query_transfer.setVisibility(View.VISIBLE);
			ll_query_result_header.setVisibility(View.GONE);
			rl_tranhistory.setVisibility(View.GONE);
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					QueryHistoryProductActivity.this
					.getString(R.string.acc_transferquery_null));
			
			return;
		} else {
			ll_query_transfer.setVisibility(View.GONE);
			ll_query_result_header.setVisibility(View.VISIBLE);
			rl_tranhistory.setVisibility(View.VISIBLE);
			combinateList = (List<Map<String, Object>>) combinateMap
					.get(BocInvt.BOCI_LIST_RES);
//			if (recordNumber > 10) {
//				queryListView.addFooterView(load_more);
//			}
			loadNumber = combinateList.size();
			//			boci_producttype.setText(typename);
			boci_currency.setText(curcode);//币种
			boci_tratype.setText(spAcctTypeText);//交易类型
			boci_date.setText(startDate + "-" + endDate);
			boci_acct.setText(spAcct.getText().toString());//交易账户
//			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
			query_result.setVisibility(View.VISIBLE);
			adapter.setSourceList(combinateList,recordNumber);
			// 配置适配器
//			adapter = new MyProductQueryAdapter(this, combinateList,1);
//			adapter = new CommonAdapter<Map<String,Object>>(this, queryListView,combinateList, this);
//		    adapter.setTotalNumber(recordNumber);
////			queryListView.setAdapter(adapter);
//		    adapter.setRequestMoreDataListener(new IFunc<Boolean>(){
//
//				@Override
//				public Boolean callBack(Object param) {
//					requestXpadTradForMore();
//					return true;
//				}
//		    	
//		    });
//			queryListView.setOnItemClickListener(onListClickListener);
		}
	}
	/**
	 * 请求中银理财  组合购买接口回调  
	 * @param resultObj 
	 * 服务端返回的数据
	 */
	public void requestXpadGuarantyCallBack(Object resultObj){
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		//结束通信  隐藏对话框
		BiiHttpEngine.dissMissProgressDialog();
		Map<String,Object> resultMap = HttpTools.getResponseResult(resultObj);
		if(StringUtil.isNullOrEmpty(resultMap))
			return ;//判断返回数据Map是否为空
		recordNumber = Integer.valueOf((String) resultMap.get(Acc.RECORDNUMBER_RES));//获取返回数据的记录条数
		List<Map<String,Object>> resultListFirst = (List<Map<String, Object>>) resultMap.get(BocInvt.BOCI_LIST_RES);
		if(resultListFirst == null || resultListFirst.size() == 0){//返回数据list是否为空
			ll_query_transfer.setVisibility(View.VISIBLE);
			ll_query_result_header.setVisibility(View.GONE);
			rl_tranhistory.setVisibility(View.GONE);
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.acc_transferquery_null));
			return;
		}
		//返回list数据不为空
		ll_query_transfer.setVisibility(View.GONE);
		ll_query_result_header.setVisibility(View.VISIBLE);
		rl_tranhistory.setVisibility(View.VISIBLE);
		combinateList = (List<Map<String, Object>>) resultMap.get(BocInvt.BOCI_LIST_RES);
//		if(recordNumber >10){
//			queryListView.addFooterView(load_more);
//		}
		loadNumber = combinateList.size();
		boci_currency.setText(curcode);//币种
		boci_tratype.setText(spAcctTypeText);//交易类型
		boci_date.setText(startDate + "-" + endDate);
		boci_acct.setText(spAcct.getText().toString());//交易账户
//			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
		query_result.setVisibility(View.VISIBLE);
		//配置适配器
//			adapter = new MyProductQueryAdapter(this, combinateList,2);
//		adapter = new CommonAdapter<Map<String,Object>>(QueryHistoryProductActivity.this, combinateList, this);
//		queryListView.setAdapter(adapter);
//		queryListView.setOnItemClickListener(onListClickListener);
	   adapter.setSourceList(combinateList,recordNumber);
		
	}

	/** 请求交易状况显示更多 */
	public void requestXpadTradForMore() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADTRADSTATUS_API);
		if(LocalData.bociTraType.get(spAcctTypeText).equals("01")){//常规交易
			biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADTRADSTATUS_API);
		}else if(LocalData.bociTraType.get(spAcctTypeText).equals("02")){//组合购买
			biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADQUERYHISGUARANTYPRODUCTLISTRESULT_API);
		}
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		//		paramsmap.put(BocInvt.BOCINCT_XPADTRAD_ACCOUNTID_REQ,
		//				preTradMap.get(BocInvt.BOCINVT_XPADPRETRAD_ACCOUNTID_RES));
		//		for (int i = 0; i < productTypeList.size(); i++) {
		//			if (boci_producttype
		//					.getText()
		//					.toString()
		//					.equals(productTypeList.get(i).get(
		//							String.valueOf(BocInvt.TYPE_NAME_RES)))) {
		//				paramsmap.put(
		//						BocInvt.BOCINCT_XPADTRAD_XPADPRODUCTTYPE_REQ,
		//						productTypeList.get(i).get(
		//								String.valueOf(BocInvt.TYPE_BRANDID_RES)));
		//				break;
		//			}
		//		}
		paramsmap.put(BocInvt.BOCINCT_XPADTRAD_ACCOUNTKEY_REQ,accItemMap.get(BocInvt.ACCOUNTQUERY_ACCOUNTKEY_RES));//账户缓存标示
		paramsmap.put(BocInvt.BOCINCT_XPADTRAD_XPADPRODUCTCURCODE_REQ,
				LocalData.bociCurcodeMap.get(curcode));
		paramsmap.put(BocInvt.BOCINCT_XPADTRAD_STARTDATE_REQ, startDate);
		paramsmap.put(BocInvt.BOCINCT_XPADTRAD_ENDDATE_REQ, endDate);
		paramsmap
		.put(BocInvt.BOCI_CURRENTINDEX_REQ, String.valueOf(loadNumber));
		paramsmap.put(BocInvt.BOCI_PAGESIZE_REQ,
				ConstantGloble.LOAN_PAGESIZE_VALUE);
		paramsmap
		.put(BocInvt.BOCI_REFRESH_REQ, ConstantGloble.REFRESH_FOR_MORE);
		paramsmap.put(BocInvt.BOCINCT_XPADTRAD_TRATYPE_REQ,LocalData.bociTraType.get(spAcctTypeText));
//		Map<String, Object> map = BociDataCenter.getInstance().getBocinvtAcctList().get(mIndex);
//		paramsmap.put(Comm.ACCOUNT_TYPE,map.get(Comm.ACCOUNT_TYPE));
//		paramsmap.put(BocInvt.BANCACCOUNTNO,map.get(BocInvt.ACCOUNTNO));
//		paramsmap.put(BocInvt.IBKNUM,map.get(BocInvt.IBKNUMBER));
		biiRequestBody.setParams(paramsmap);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestXpadTradForMoreCallBack");
	}

	/**
	 * 请求交易状况显示更多回调
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void requestXpadTradForMoreCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 结束通讯,隐藏通讯框
		BiiHttpEngine.dissMissProgressDialog();
		Map<String, Object> combinateMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(combinateMap)) {
			return;
		}
		recordNumber = Integer.valueOf((String) combinateMap
				.get(Acc.RECORDNUMBER_RES));
		List<Map<String, Object>> combinateListForMore = (List<Map<String, Object>>) combinateMap
				.get(BocInvt.BOCI_LIST_RES);
		if (combinateListForMore == null || combinateListForMore.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					QueryHistoryProductActivity.this
					.getString(R.string.acc_transferquery_null));

			return;
		} else {
			for (Map<String, Object> map : combinateListForMore) {
//				loadNumber++;
				combinateList.add(map);
			}
//			if (loadNumber < recordNumber) {
//
//			} else {
//				queryListView.removeFooterView(load_more);
//			}
			// 配置适配器
			adapter.notifyDataSetChanged();
		}
	}

	/** 理财产品点击事件 */
	OnItemClickListener onListClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (StringUtil.isNullOrEmpty(combinateList)) return;
			if(!StringUtil.isNullOrEmpty(view.getTag())){
				int index = Integer.valueOf(view.getTag().toString());
				if(index == position){
					return;
				}
			}
			Map<String, Object> map = BociDataCenter.getInstance().getBocinvtAcctList().get(mIndex);
			combinateList
			.get(position)
			.put(BocInvt.BOCINVT_XPADPRETRAD_ACCOUNTID_RES,
					map.get(BocInvt.BOCINVT_XPADPRETRAD_ACCOUNTID_RES));
			combinateList.get(position).put(Comm.ACCOUNT_TYPE,map.get(Comm.ACCOUNT_TYPE));
			combinateList.get(position).put(BocInvt.IBKNUM,map.get(BocInvt.IBKNUMBER));
			combinateList.get(position).put(Comm.ACCOUNTNUMBER,map.get(BocInvt.ACCOUNTNO));
			
			//				Intent intent = new Intent(QueryHistoryProductActivity.this,
			//						HistoryProductDetailActivity.class);
			//				startActivityForResult(intent, ACTIVITY_BUY_CODE);
//			ActivityIntentTools.intentToActivityForResult(QueryHistoryProductActivity.this, InverstQueryDetailActivity.class, ACTIVITY_BUY_CODE);
//			requestPsnXpadTransInfoDetailQuery();
//			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.BOCINVT_INFODETAIL_MAP, infoDetailMap);
//			intentTemp = new Intent(QueryHistoryProductActivity.this, InverstQueryDetailActivity.class);
			if(LocalData.bociTraType.get(spAcctTypeText).equals("01")){//常规交易
//				intentTemp.putExtra("type", "01");
				BaseDroidApp
				.getInstanse()
				.getBizDataMap()
				.put(ConstantGloble.BOCINVT_HISDETAIL_MAP,
						combinateList.get(position));//存储常规map数据
				requestPsnXpadTransInfoDetailQuery();//常规详情接口
			}else if(LocalData.bociTraType.get(spAcctTypeText).equals("02")){//组合购买
				intentTemp = new Intent(QueryHistoryProductActivity.this, InverstQueryDetailActivity.class);
				intentTemp.putExtra("type", "02");
				BaseDroidApp
				.getInstanse()
				.getBizDataMap()
				.put(ConstantGloble.BOCINVT_GUARANTY_MAP,
						combinateList.get(position));//存储组合map数据
				startActivityForResult(intentTemp, ACTIVITY_BUY_CODE);
			}
//			startActivityForResult(intentTemp, ACTIVITY_BUY_CODE);
			
		}

	};
//	/** 组合购买详情查询接口 **/
//	public void requestPsnXpadQueryGuarantyProductResult(){
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADTRANSINFODETAILQUERY_API);
//	}
	
	/** 请求中银理财 交易状况详细信息查询 接口 **/
	@SuppressWarnings("unchecked")
	public void requestPsnXpadTransInfoDetailQuery(){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADTRANSINFODETAILQUERY_API);
//		biiRequestBody.setConversationId((String)BaseDroidApp.getInstanse()
//				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String,Object> paramsmap = new HashMap<String, Object>();
		Map<String, Object> map = BociDataCenter.getInstance().getBocinvtAcctList().get(mIndex);
//		paramsmap.put(BocInvt.BOCINVT_INFODETAILQUERY_TYPEOFACCOUNT_REQ, map.get(Comm.ACCOUNT_TYPE));
		Map<String,Object> historyDetailMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BOCINVT_HISDETAIL_MAP);
		paramsmap.put(BocInvt.BOCINVT_INFODETAILQUERY_TRANSEQ_REQ, String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_TRANSEQ_RES)));
//		paramsmap.put(BocInvt.BOCINVT_INFODETAILQUERY_ACCOUNTKEY_REQ, map.get(BocInvt.ACCOUTKEY));
		paramsmap.put(BocInvt.BOCINVT_INFODETAILQUERY_ACCOUNTKEY_REQ, accItemMap.get(BocInvt.ACCOUNTQUERY_ACCOUNTKEY_RES));
		biiRequestBody.setParams(paramsmap);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestPsnXpadTransInfoDetailQueryCallBack");
		
	}
	/**
	 * 中银理财 交易状况详细信息查询接口回调
	 * @param resultObj
	 * 服务端返回数据
	 */
	public void requestPsnXpadTransInfoDetailQueryCallBack(Object resultObj){
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 结束通讯,隐藏通讯框
		BiiHttpEngine.dissMissProgressDialog();
		infoDetailMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(infoDetailMap)) {
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.BOCINVT_INFODETAIL_MAP, infoDetailMap);
		intentTemp = new Intent(QueryHistoryProductActivity.this, InverstQueryDetailActivity.class);
//		if(LocalData.bociTraType.get(spAcctTypeText).equals("01")){
			intentTemp.putExtra("type", "01");
//			BaseDroidApp
//			.getInstanse()
//			.getBizDataMap()
//			.put(ConstantGloble.BOCINVT_HISDETAIL_MAP,
//					combinateList.get(position));//存储常规map数据
//		}else if(LocalData.bociTraType.get(spAcctTypeText).equals("02")){
//			intentTemp.putExtra("type", "02");
//			BaseDroidApp
//			.getInstanse()
//			.getBizDataMap()
//			.put(ConstantGloble.BOCINVT_GUARANTY_MAP,
//					combinateList.get(position));//存储组合map数据
//		}
		startActivityForResult(intentTemp, ACTIVITY_BUY_CODE);
		
		
	}
	/** 查询结果头倒三角点击监听事件 */
	OnClickListener backQueryClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			combinateLayout.setBackgroundResource(R.drawable.img_bg_query_no);
//			PopupWindowUtils.getInstance().getQueryPopupWindow(
//					ll_query_transfer,
//					BaseDroidApp.getInstanse().getCurrentAct());
//			PopupWindowUtils.getInstance().showQueryPopupWindow();
			ll_query_transfer.setVisibility(View.VISIBLE);
			ll_query_result_header.setVisibility(View.GONE);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_FIRST_USER:
			if (!PopupWindowUtils.getInstance().isQueryPopupShowing()) {
				combinateLayout.setBackgroundResource(R.drawable.img_bg_query_no);
//				PopupWindowUtils.getInstance().getQueryPopupWindow(
//						ll_query_transfer, QueryHistoryProductActivity.this);
//				PopupWindowUtils.getInstance().showQueryPopupWindow();
				ll_query_transfer.setVisibility(View.VISIBLE);
				ll_query_result_header.setVisibility(View.GONE);
			}
			break;
			/**屏蔽之前判断任务的方式**/
//		case RESULT_OK:
//			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
//				// 开通成功的响应
//				isOpenBefore = true;
//				isOpen = true;
//			} else if (requestCode == ConstantGloble.ACTIVITY_REQUEST_INVTBINDING_CODE) {
//				// 登记账户成功的响应
//				investBinding = (List<Map<String, Object>>) BaseDroidApp
//						.getInstanse().getBizDataMap()
//						.get(ConstantGloble.BOCINVT_XPADRESET_CHOOSE);
//				investBindingInfo = (List<Map<String, Object>>) BaseDroidApp
//						.getInstanse().getBizDataMap()
//						.get(ConstantGloble.BOCINVT_XPADRESET_CHOOSE);
//			} else if (requestCode == ConstantGloble.ACTIVITY_REQUEST_INVTEVALUATION_CODE) {
//				// 风险评估成功的响应
//				if (BociDataCenter.getInstance().getI() == 1) {
//
//				} else {
//					isevaluated = true;
//					isevaluation = true;
//				}
//			} else {
//				if (queryListView.getFooterViewsCount() > 0) {
//					queryListView.removeFooterView(load_more);
//				}
//				requestXpadTrad();
//				break;
//			}
//			if (isOpen && !StringUtil.isNullOrEmpty(investBindingInfo)
//					&& isevaluation) {
//				requestRecentAccount();
//				BaseHttpEngine.showProgressDialogCanGoBack();
//			} else {
//				InflaterViewDialog inflater = new InflaterViewDialog(
//						QueryHistoryProductActivity.this);
//				dialogView2 = (RelativeLayout) inflater.judgeViewDialog(isOpen,
//						investBindingInfo, isevaluation, manageOpenClick,
//						invtBindingClick, invtEvaluationClick, exitClick);
//				TextView tv = (TextView) dialogView2
//						.findViewById(R.id.tv_acc_account_accountState);
//				// 查询
//				tv.setText(this.getString(R.string.bocinvt_query_title));
//
//				BaseDroidApp.getInstanse()
//				.showAccountMessageDialog(dialogView2);
//			}
//
//			break;
//		case RESULT_CANCELED:
//			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE
//			|| requestCode == ConstantGloble.ACTIVITY_REQUEST_INVTBINDING_CODE
//			|| requestCode == ConstantGloble.ACTIVITY_REQUEST_INVTEVALUATION_CODE) {
//
//			} else {
//
//				break;
//			}
//			if (isOpen && !StringUtil.isNullOrEmpty(investBindingInfo)
//					&& isevaluation) {
//			} else {
//				InflaterViewDialog inflater = new InflaterViewDialog(
//						QueryHistoryProductActivity.this);
//				dialogView2 = (RelativeLayout) inflater.judgeViewDialog(isOpen,
//						investBindingInfo, isevaluation, manageOpenClick,
//						invtBindingClick, invtEvaluationClick, exitClick);
//				TextView tv = (TextView) dialogView2
//						.findViewById(R.id.tv_acc_account_accountState);
//				// 查询
//				tv.setText(this.getString(R.string.bocinvt_query_title));
//				BaseDroidApp.getInstanse()
//				.showAccountMessageDialog(dialogView2);
//
//			}
//			break;
		}
	}

	protected OnClickListener exitClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
			finish();
		}
	};

//	/**
//	 * 账户详情查询操作
//	 * @param v
//	 */
//	public void acctDetailOnclick(View v){
//		if (spAcct.getText().toString().equals("请选择账户")) {
//			BaseDroidApp.getInstanse().showInfoMessageDialog("请选择账户"); return;
//		}
//		requestAccBankAccountDetail();
//	}

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
		requestRecentAccount();
		BaseHttpEngine.showProgressDialogCanGoBack();
		/**屏蔽之前判断任务的方式**/
//		isOpen = isOpenBefore;
//		isevaluation = isevaluated;
//		investBindingInfo = investBinding;
//		BaseDroidApp.getInstanse().getBizDataMap()
//		.put(ConstantGloble.BOCINVT_XPADRESET_CHOOSE, investBinding);
//		if (isOpenBefore && !StringUtil.isNullOrEmpty(investBinding)
//				&& isevaluated) {
//			requestRecentAccount();
//			BaseHttpEngine.showProgressDialogCanGoBack();
//		} else {
//			// 得到result
//			BaseHttpEngine.dissMissProgressDialog();
//			InflaterViewDialog inflater = new InflaterViewDialog(
//					QueryHistoryProductActivity.this);
//			dialogView2 = (RelativeLayout) inflater.judgeViewDialog(
//					isOpenBefore, investBinding, isevaluated, manageOpenClick,
//					invtBindingClick, invtEvaluationClick, exitClick);
//			TextView tv = (TextView) dialogView2
//					.findViewById(R.id.tv_acc_account_accountState);
//			// 查询
//			tv.setText(this.getString(R.string.bocinvt_query_title));
//
//			BaseDroidApp.getInstanse().showAccountMessageDialog(dialogView2);

//		}
	}

	@Override
	public View getView(int arg0, Map<String, Object> currentItem,
			LayoutInflater inflater, View convertView, ViewGroup viewGroup) {
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.bocinvt_hispro_list_item, null);
		}
		/** 交易日期 */
		TextView boci_product_name = (TextView) convertView
				.findViewById(R.id.boci_product_name);
		/** 产品名称 */
		TextView boci_yearlyRR = (TextView) convertView
				.findViewById(R.id.boci_yearlyRR);
		/** 交易金额 */
		TextView boci_timeLimit = (TextView) convertView
				.findViewById(R.id.boci_timeLimit);
		boci_timeLimit.setTextColor(getResources()
				.getColor(R.color.red));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(QueryHistoryProductActivity.this,boci_yearlyRR);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(QueryHistoryProductActivity.this,boci_timeLimit);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(QueryHistoryProductActivity.this,boci_product_name);
		/** 右三角 */
		ImageView goDetail = (ImageView) convertView
				.findViewById(R.id.boci_gotoDetail);
		goDetail.setVisibility(View.VISIBLE);
		// 赋值操作
		if(LocalData.bociTraType.get(spAcctTypeText).equals("01")){//常规
			boci_product_name.setText(String.valueOf(currentItem
					.get(BocInvt.BOCINCT_XPADTRAD_PAYMENTDATE_RES)));
			boci_yearlyRR.setText(String.valueOf(currentItem
					.get(BocInvt.BOCINCT_XPADTRAD_PRODNAME_RES)));
			String timeLimit = (String)currentItem.get(
					BocInvt.BOCINCT_XPADTRAD_AMOUNT_RES);
			String currency = (String) currentItem.get(
					BocInvt.BOCINCT_XPADTRAD_CURRENCYCODE_RES);
			if(currentItem.get(BocInvt.BOCINCT_XPADTRAD_AMOUNT_RES) == null || StringUtil.parseStringCodePattern(currency,
					timeLimit, 2).equals("0") || StringUtil.parseStringCodePattern(currency,
							timeLimit, 2).equals("0.00") || timeLimit.trim().equals("")){
				boci_timeLimit.setText("-");
			}else{
				boci_timeLimit.setText(StringUtil.parseStringCodePattern(currency,
						timeLimit, 2));
			}
//			String trfType = String.valueOf(productQueryList.get(position)
//					.get(BocInvt.BOCINCT_XPADTRAD_TRFTYPE_RES));
//			String status = String.valueOf(productQueryList.get(position)
//					.get(BocInvt.BOCINCT_XPADTRAD_STATUS_RES));
//			String standardPro = String.valueOf(productQueryList.get(position)
//					.get(BocInvt.BOCINCT_XPADTRAD_STANDARDPRO_RES));
//			String productKind = String.valueOf(productQueryList.get(position)
//					.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTKIND_RES));
			/** 列表页控制进入详情页面 **/
			//判断item项是否可点击 进入详情页面  右三角显示隐藏状态设置  
//			if(status.equals("2")||status.equals("3")||status.equals("4")||status.equals("5")){//失败
//				goDetail.setVisibility(View.VISIBLE);
//			}else if(status.equals("1")){//成功
//				if(standardPro.equals("1") && productKind.equals("0")){//业绩
//					if(trfType.equals("02")){//常规业绩赎回成功 
//						goDetail.setVisibility(View.VISIBLE);
//					}else{//隐藏 不可点击进入详情
//						goDetail.setVisibility(View.INVISIBLE);
//						convertView.setTag(position);
//						//					convertView.setFocusable(false);
//						//					convertView.setFocusableInTouchMode(false);
//						//					convertView.setClickable(false);
//					}
//				}else if(productKind.equals("1") && standardPro.equals("0")){//净值
//					if(trfType.equals("00") || trfType.equals("01") || trfType.equals("02")){//认购/申购/赎回
//						goDetail.setVisibility(View.VISIBLE);
//					}else{
//						goDetail.setVisibility(View.INVISIBLE);
//						convertView.setTag(position);
//					}
//				}else{//非业绩、非净值
//					goDetail.setVisibility(View.INVISIBLE);
//					convertView.setTag(position);
//				}
//			}else if(status.equals("0")){//委托待处理
//				goDetail.setVisibility(View.INVISIBLE);
//				convertView.setTag(position);
//			}
//			if(standardPro.equals("1")){//业绩
//				if(!status.equals("1")){//交易失败
//					goDetail.setVisibility(View.VISIBLE);
//				}else{//成功交易
//					if(trfType.equals("02")){//常规业绩赎回成功 
//						goDetail.setVisibility(View.VISIBLE);
//					}else{//隐藏 不可点击进入详情
//						goDetail.setVisibility(View.GONE);
//						convertView.setTag(position);
//						//					convertView.setFocusable(false);
//						//					convertView.setFocusableInTouchMode(false);
//						//					convertView.setClickable(false);
//					}
//				}
//			}else if(productKind.equals("1")){//净值
//				goDetail.setVisibility(View.VISIBLE);
//			}else{
//				goDetail.setVisibility(View.GONE);
//				convertView.setTag(position);
//			}
		}else if(LocalData.bociTraType.get(spAcctTypeText).equals("02")){//组合
			boci_product_name.setText(String.valueOf(currentItem
					.get(BocInvt.BOCINVT_XPADQUERY_RETURNDATE_RES)));
			boci_yearlyRR.setText(String.valueOf(currentItem
					.get(BocInvt.BOCINVT_XPADQUERY_PRODNAME_RES)));
			String timeLimit_2 = (String) currentItem.get(
					BocInvt.BOCINVT_XPADQUERY_AMOUNT_RES);
			String currency_2 = (String) currentItem.get(
					BocInvt.BOCINVT_XPADQUERY_CURRENCY_RES);
			if(currentItem.get(BocInvt.BOCINVT_XPADQUERY_AMOUNT_RES) == null || StringUtil.parseStringCodePattern(currency_2,
					timeLimit_2, 2).equals("0") || StringUtil.parseStringCodePattern(currency_2,
							timeLimit_2, 2).equals("0.00") || timeLimit_2.trim().equals("") ){
				boci_timeLimit.setText("-");
			}else{
				boci_timeLimit.setText(StringUtil.parseStringCodePattern(currency_2,
						timeLimit_2, 2));
			}
			
//			goDetail.setVisibility(View.VISIBLE);
			
//		//交易状态status 为0 时 列表项不可点击状态
//		String status = String.valueOf(productQueryList.get(position)
//				.get(BocInvt.BOCINCT_XPADTRAD_STATUS_RES));
//		if(status.equals("0")){//委托代处理状态不可进详情
//			goDetail.setVisibility(View.GONE);
//			convertView.setTag(position);
//		}
		}
		return convertView;
	}
}
