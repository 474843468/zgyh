package com.chinamworld.bocmbci.biz.acc.mybankaccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.biz.acc.adapter.AccTransGalleryAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.DateUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomGallery;

/*
 * @author zxj
 */
public class AccountIsPayrollDetailActivity extends AccBaseActivity implements
		OnClickListener {
	/**账户列表信息页*/
//	private View view;
	/**账户查询Layout*/
	private View rl_query_transfer;
	private CustomGallery gallery;
	/**查询条件视图*/
	private LinearLayout query_condition;
	/**查询日期年*/
	private Spinner times_year_spinner;
	/**查询日期月*/
	private Spinner times_month_spinner;
	/**查询按钮*/
	private Button btn_query;
	/**查询条件收起三角*/
//	private ImageView img_up;
	/**工资卡账户明细*/
//	private List<Map<String, String>> transferList;
	/**查询结果layout*/
	private RelativeLayout rl_query_transfer_result;
//	private LinearLayout ll_sort;
	/**查询结果姓名*/
	private LinearLayout tv_acc_info_name;
	private TextView tv_acc_info_name_value;
	/**查询结果卡号*/
	private LinearLayout tv_acc_cardnumber;
	private TextView tv_acc_query_result_cardnumber;
	/**单位名称*/
	private Spinner acc_query_transfer_orgnativtion;
	/**交易结果列表信息*/
	private LinearLayout lv_acc_query_result;
//	/**账户列表*/
	private List<Map<String, Object>> bankList= new ArrayList<Map<String,Object>>();
	/** 选择的账户详情 */
	protected Map<String, Object> bankListAccount;
	/**当前账户*/
	private Map<String, Object> currentBankList;
	/** 第一次进入 */
	private boolean isshowfirst = true;
	/**时间数组月*/
	private List<String> dateList;
	/**时间数组年*/
	private List<String> dateListYear;
	/**spinner 年*/
	private String yearDate;
	/**spinner 月*/
	private String monthDate;
	/**服务器时间年*/
	private String year;
	/**服务器时间月*/
	private int month;
	/**工资卡list信息*/
	private List<Map<String, Object>> queryProllList;
	/**工资卡查询 姓名*/
	private String queryPayrollName;
//	private List<String> queryPayrollNameList = new ArrayList<String>();
	/**工资卡查询时间*/
	private String queryPayrollTime;
	private List<String> queryPayrollTimeList = new ArrayList<String>();
	/**公司名*/
	private String queryPayrollCompanyName;
	private List<String> queryPayrollCommanyNameList = new ArrayList<String>();
	/**工资卡查询信息info*/
	private String queryPayrollInfo;
	/**工资卡信息组*/
	private List<String> queryPayrollInfoList = new ArrayList<String>();
	/**查询具体时间*/
	private TextView tv_acc_time_currency_value;
	//获取分辨率
	 float densityRadio;
	 /**本月查询*/
	 private Button btn_query_onweek ;
	 /**上月查询*/
	 private Button btn_query_threemonth;
	 /**查询时间*/
	 private String times;
	// 查询后结果头视图
	private LinearLayout acc_query_result_condition;
	//下拉
	private LinearLayout ll_down;
	/**收起*/
	private LinearLayout ll_up;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics dm = getApplicationContext().getResources()
				.getDisplayMetrics();
		densityRadio=dm.density;

		// 为界面赋值
		setTitle(R.string.acc_finance_menu_4);
		//右上角按钮复制
		setText(this.getString(R.string.acc_rightbtn_go_main));
		//右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		setBackBtnClick(backBtnClick);
		//添加布局
//		view = addView(R.layout.acc_queryispayrolldetail_querytransfer);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0,
					0, 0);
		}
		BaseHttpEngine.showProgressDialog();
		/**查询系统时间*/
		requestSystemDateTime();
		
	}
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.requestSystemDateTimeCallBack(resultObj);
		BiiResponse biiResponse = (BiiResponse)resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>)biiResponseBody.getResult();
		dateTime = (String)resultMap.get(Comm.DATETME);
		LogGloble.i("====dateTime 服务器时间", dateTime);
		if(StringUtil.isNull(dateTime)){
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		getBillDate();
		if(StringUtil.isNullOrEmpty(dateList)){
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		if(StringUtil.isNullOrEmpty(dateListYear)){
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		init();
		initSpinner();
	}
	/**初始化spinner*/
	private void initSpinner(){
		ArrayAdapter<String> timesYearAdapter = new ArrayAdapter<String>(this, R.layout.acc_payroll_spinner, dateListYear);
		timesYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		times_year_spinner.setAdapter(timesYearAdapter);
		times_year_spinner.setSelection(0);
		yearDate = times_year_spinner.getSelectedItem().toString();
		final ArrayAdapter<String> timesMonthAdapter = new ArrayAdapter<String>(this, R.layout.acc_payroll_spinner, dateList);
		timesMonthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		times_year_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
				yearDate = times_year_spinner.getSelectedItem().toString();
				
				if(Integer.valueOf(yearDate) == (Integer.valueOf(year)-1)){
					dateList.clear();
					for(int i = month+1; i <=12;i++){
						String t = "" + i;
						int j = Integer.valueOf(t);
						
						dateList.add(String.valueOf(j)+"月");
					}
					times_month_spinner.setSelection(0);
				}
				else {
					dateList.clear();
						
					for(int i=1; i<= month; i++){
						String t = "" + i;
						int j = Integer.valueOf(t);
						
						dateList.add(String.valueOf(j)+"月");
					}
					times_month_spinner.setSelection(month);
				}
				timesMonthAdapter.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		
		times_month_spinner.setAdapter(timesMonthAdapter);
		times_month_spinner.setSelection(month-1);
		monthDate = times_year_spinner.getSelectedItem().toString();
		times_month_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
				monthDate = times_month_spinner.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}
	/**计算时间日期*/
	private void getBillDate(){
		String date = DateUtils.signDateFormatter(dateTime);
		LogGloble.i("====服务器时间转换date==xpj", date);
		month = Integer.valueOf(date.substring(5, 7));
		LogGloble.i("===month==", String.valueOf(month));
		year = dateTime.substring(0, 4);
		
		int timesYear = Integer.valueOf(year);
		dateList = new ArrayList<String>();
		dateListYear = new ArrayList<String>();
		if(month== 12){
			dateListYear.add(String.valueOf(year));
			for(int i=1; i<= 12; i++){
				String t = "" + i;
				int j = Integer.valueOf(t);
				
				dateList.add(String.valueOf(j)+"月");
			}
		}else {
			for(int i=0; i<2; i++){
				
				timesYear = timesYear-i;
				LogGloble.i("===timesYear==", String.valueOf(timesYear));
				dateListYear.add(String.valueOf(timesYear));
			}
			
			for(int i=1; i<= month+1; i++){
				String t = "" + i;
				int j = Integer.valueOf(t);
				
				dateList.add(String.valueOf(j)+"月");
			}
		}
		
	}
	
	
	/**初始化界面*/
	private void init(){
//		rl_query_transfer = (RelativeLayout)LayoutInflater.from(
//				AccountIsPayrollDetailActivity.this).inflate(
//						R.layout.acc_queryispayrolldetail_condition, null);
		rl_query_transfer= addView(R.layout.acc_queryispayrolldetail_condition);
		gallery = (CustomGallery)rl_query_transfer.findViewById(R.id.viewPager);
		//条形视图
		query_condition = (LinearLayout)rl_query_transfer.findViewById(R.id.query_condition);
//		query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
		btn_query_onweek = (Button) rl_query_transfer
				.findViewById(R.id.btn_acc_onweek);
		btn_query_threemonth = (Button) rl_query_transfer
				.findViewById(R.id.btn_acc_threemonth);
		//查询条件初始化
		times_year_spinner = (Spinner) rl_query_transfer.findViewById(R.id.times_year_spinner);
		times_month_spinner = (Spinner)rl_query_transfer.findViewById(R.id.times_month_spinner);
		btn_query = (Button)rl_query_transfer.findViewById(R.id.btn_acc_query_transfer);
		btn_query.setOnClickListener(this);
		ll_up = (LinearLayout)rl_query_transfer.findViewById(R.id.ll_up);
//		img_up = (ImageView)rl_query_transfer.findViewById(R.id.acc_query_up);
		ll_up.setOnClickListener(this);
//		img_up.setOnClickListener(upClick);
		btn_query_onweek.setOnClickListener(this);
		btn_query_threemonth.setOnClickListener(this);
		rl_query_transfer_result = (RelativeLayout)rl_query_transfer.findViewById(R.id.layout_result);
		/**获得当前日期1年前*/
//		String oneDateTime = QueryDateUtils.getsysDateOneYear(dateTime);
//		LogGloble.i("oneDateTime==xpj", oneDateTime);
		
//		times_year_spinner.setOnClickListener(chooseYearDate);
//		tv_monthdate.setOnClickListener(chooseMonthDate);
		initPage();
		
		initFirst();
		setLeftSelectedPosition("accountManager_1");
	}

	/** 初始化page容器 */
	private void initPage() {

		int list_item = this.getIntent().getIntExtra(
				ConstantGloble.ACC_POSITION, 0);
		bankListAccount = AccDataCenter.getInstance().getChooseBankAccount();
		bankList.add(bankListAccount);
		LogGloble.e("===bankAcount", bankListAccount.toString());
		LogGloble.i("===bankList==xpj", bankList.toString());
		queryPayrollName = (String)bankListAccount.get(Acc.ACC_PAYROLL_NAME);
		LogGloble.i("...queryPayrollName", queryPayrollName);
		/** 刚进入时，当前账户为选择账户 */
		AccTransGalleryAdapter adapter = new AccTransGalleryAdapter(this,
				bankList);
		gallery.setAdapter(adapter);
		gallery.setSelection(list_item);
	}

	/**初始化查询结果页面*/
	public void initFirst() {
		// 查询后结果头视图
		acc_query_result_condition = (LinearLayout) rl_query_transfer
				.findViewById(R.id.acc_query_result_condition);
//		ll_sort = (LinearLayout) view.findViewById(R.id.ll_sort);
		tv_acc_time_currency_value = (TextView)rl_query_transfer.findViewById(R.id.tv_acc_time_currency_value);
		tv_acc_info_name = (LinearLayout)rl_query_transfer.findViewById(R.id.tv_acc_info_name);
		tv_acc_info_name_value = (TextView)rl_query_transfer.findViewById(R.id.tv_acc_info_currency_value);
		tv_acc_cardnumber = (LinearLayout)rl_query_transfer.findViewById(R.id.tv_acc_cardnumber);
		tv_acc_query_result_cardnumber = (TextView)rl_query_transfer.findViewById(R.id.tv_acc_info_cashremit_value);
		acc_query_transfer_orgnativtion = (Spinner)rl_query_transfer.findViewById(R.id.acc_query_transfer_enddate);
		lv_acc_query_result = (LinearLayout)rl_query_transfer.findViewById(R.id.lv_acc_query_result);
		ll_down = (LinearLayout)rl_query_transfer.findViewById(R.id.ll_down);
		ll_down.setOnClickListener(this);
//		acc_query_result_condition.setOnClickListener(backQueryClick);
	}

//	@Override
//	public void onWindowFocusChanged(boolean hasFocus) {
//		super.onWindowFocusChanged(hasFocus);
//		if (hasFocus && isshowfirst) {
//			isshowfirst = false;
//			PopupWindowUtils.getInstance().getQueryPopupWindow(
//					rl_query_transfer,
//					BaseDroidApp.getInstanse().getCurrentAct());
//			PopupWindowUtils.getInstance().showQueryPopupWindowFirst();
//		}
//	}

	/**查询结果头倒三角点击事件*/
//	OnClickListener backQueryClick = new OnClickListener() {
//		
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			query_condition.setBackgroundResource(R.drawable.img_bg_query_no);
//			PopupWindowUtils.getInstance().showQueryPopupWindow();
////			ll_sort.setVisibility(View.VISIBLE);
////			ll_sort.setClickable(false);
//		}
//	};
	/**收起监听事件*/
//	OnClickListener upClick = new OnClickListener() {
//		
//		@Override
//		public void onClick(View v) {
//			// 判断是否已经进行了查询-如果已经进行:收起所有;否则收起查询条件区域
//			if(queryPayrollInfoList == null || queryPayrollInfoList.size()  == 0){
//				
//			}else {
////				PopupWindowUtils.getInstance().dissMissQueryPopupWindow();			
////				ll_sort.setClickable(true);
////				ll_sort.setVisibility(View.VISIBLE);
//				tv_acc_info_name.setVisibility(View.VISIBLE);
//				tv_acc_cardnumber.setVisibility(View.VISIBLE);
//				rl_query_transfer_result.setVisibility(View.VISIBLE);
//			}
//			
//		}
//	};
//	@Override
//	protected void onResume() {
//		super.onResume();
//		setLeftSelectedPosition("accountManager_1");
//	}
	/**右侧按钮点击事件*/
	OnClickListener rightBtnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// 回主页
			ActivityTaskManager.getInstance().removeAllActivity();
			
		}
	};
	/**左侧返回按钮点击事件*/
	OnClickListener backBtnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			setResult(RESULT_OK);
			finish();
		}
	};
	
	
	
	@Override
	public void onClick(View v) {
		// 查询事件
		switch (v.getId()) {
		//收起
		case R.id.ll_up:
			if(isshowfirst){
				ll_up.setClickable(false);
			}else {
				ll_up.setClickable(true);
				findViewById(R.id.query_condition).setVisibility(View.GONE);
				findViewById(R.id.acc_query_result_condition).setVisibility(View.VISIBLE);
			}
			break;
			//下拉
		case R.id.ll_down:
			findViewById(R.id.acc_query_result_condition).setVisibility(View.GONE);
			findViewById(R.id.query_condition).setVisibility(View.VISIBLE);
			break;
		case R.id.btn_acc_query_transfer:
			//直接进行查询
			if(!StringUtil.isNullOrEmpty(queryPayrollInfoList)){
				queryPayrollInfoList.clear();
			}
//			query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
//			rl_query_transfer_result.setVisibility(View.GONE);
			String timeMonth = monthDate.substring(0, monthDate.indexOf("月"));
			if(Integer.valueOf(timeMonth)<10){
				times = yearDate + "/0"+ timeMonth;
			}else {
				times = yearDate + "/"+ timeMonth;
			}	
			LogGloble.i("===服务器时间==dateTime", dateTime);
			LogGloble.i("===查询按钮==time", times);
			requestQueryPayrollInfo(times);
			break;
		case R.id.btn_acc_onweek:
			//本月查询
			if(!StringUtil.isNullOrEmpty(queryPayrollInfoList)){
				queryPayrollInfoList.clear();
			}
			times = dateTime.substring(0, 7);
			LogGloble.i("===服务器时间==dateTime", dateTime);
			LogGloble.i("===本月工资单==times", times);
			requestQueryPayrollInfo(times);
			break;
		case R.id.btn_acc_threemonth:
			//上月查询
			if(!StringUtil.isNullOrEmpty(queryPayrollInfoList)){
				queryPayrollInfoList.clear();
			}
			times = (String)QueryDateUtils.getlastOneMonth(dateTime);
			times = times.substring(0, 7);
			LogGloble.i("===服务器时间==dateTime", dateTime);
			LogGloble.i("===上月工资单==times", times);
			requestQueryPayrollInfo(times);
			break;
		default:
			break;
		}
		
	}
	/**工资卡账户查询详细信息*/
	private void requestQueryPayrollInfo(String time){
//		Date date;
		BaseHttpEngine.showProgressDialog();
		String accountId = String.valueOf(bankListAccount.get(Acc.ACC_ACCOUNTID_RES));
		isshowfirst = false;
//		try {
//		SimpleDateFormat dataformat = new SimpleDateFormat("yyyy/MM");
//		 date = dataformat.parse(time);
//		} catch (ParseException e) {
//			
//		}
//		Date dateTime = new Date(time);
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_PSNQUERYPAYROLLINFO);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Acc.ACC_ACCOUNTID_RES, accountId);
		params.put(Acc.ACC_PAYROLLDATE, time);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestQueryPayrollInfoCallBack");
	}
	/**工资卡账户查询详细信息回调*/
	public void requestQueryPayrollInfoCallBack(Object object){
		
		BiiResponse biiResponse = (BiiResponse)object;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		queryProllList = (List<Map<String, Object>>)biiResponseBody.getResult();
		BaseHttpEngine.dissMissProgressDialog();
//		if(StringUtil.isNullOrEmpty(queryProllList)){
//			return;
//		}
		
//		LogGloble.e("11111", queryProllList.toString());
//		queryPayrollName = (String)resultMap.get(Acc.ACC_PAYROLL_NAME);
//		queryPayrollTime = (String)resultMap.get(Acc.ACC_PAYROLL_TIME);
//		queryPayrollInfo = (String)resultMap.get(Acc.ACC_PAYROLL_INFO);
//		queryProllList = (List<Map<String,Object>>)resultMap.get(Acc.ACC_PAYROLL_RESULTLIST);
		queryPayrollCommanyNameList.clear();
		queryPayrollTimeList.clear();
//		queryPayrollNameList.clear();
		queryPayrollInfoList.clear();
		if (!StringUtil.isNullOrEmpty(queryProllList)) {
			for (int i = 0; i < queryProllList.size(); i++) {
				String timeDates = (String) queryProllList.get(i).get(
						Acc.ACC_PAYROLL_TIME);
				timeDates = DateUtils.DateFormatter(timeDates);
				timeDates = timeDates.substring(0, 7);
				LogGloble.i("===报文数据时间==timeDates", timeDates);
				if (times.equals(timeDates)) {
					queryPayrollTime = (String) queryProllList.get(i).get(
							Acc.ACC_PAYROLL_TIME);
					// queryPayrollName =
					// (String)queryProllList.get(i).get(Acc.ACC_PAYROLL_NAME);
					queryPayrollCompanyName = (String) queryProllList.get(i)
							.get(Acc.ACC_PAYROLL_COMPANYNAME);
					queryPayrollInfo = (String) queryProllList.get(i).get(
							Acc.ACC_PAYROLL_INFO);
					queryPayrollTimeList.add(queryPayrollTime);
					LogGloble.i("...queryPayrollTimeList",
							queryPayrollTimeList.toString());
					// queryPayrollNameList.add(queryPayrollName);
					// LogGloble.i("...queryPayrollNameList",
					// queryPayrollNameList.toString());
					queryPayrollCommanyNameList.add(DateUtils
							.DateFormatter(queryPayrollTime)
							+ queryPayrollCompanyName);
					LogGloble.i("...queryPayrollCommanyNameList",
							queryPayrollCommanyNameList.toString());
					queryPayrollInfoList.add(queryPayrollInfo);
					LogGloble.i("...queryPayrollCommanyNameList",
							queryPayrollCommanyNameList.toString());
				}
			}
		}
		LogGloble.i("-==queryPayrollInfoList==xpj",String.valueOf((StringUtil.isNullOrEmpty(queryPayrollInfoList))));
		if(queryPayrollInfoList == null || queryPayrollInfoList.size() == 0){
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					BaseDroidApp.getInstanse().getCurrentAct()
							.getString(R.string.acc_error_querypayroll));
			ll_up.setClickable(false);
			rl_query_transfer_result.setVisibility(View.GONE);
			return;
		}
		getDate();
		
	}
	/**查询结果页面加载数据*/
	private void getDate(){
//		PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
		query_condition.setVisibility(View.GONE);
		acc_query_result_condition.setVisibility(View.VISIBLE);
		rl_query_transfer_result.setVisibility(View.VISIBLE);
//		ll_sort.setClickable(true);
		ArrayAdapter<String> comanyNameAdapter = new ArrayAdapter<String>(this, R.layout.acc_payroll_spinner, queryPayrollCommanyNameList);
		comanyNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		acc_query_transfer_orgnativtion.setAdapter(comanyNameAdapter);
		acc_query_transfer_orgnativtion.setSelection(0);
		queryPayrollCompanyName = acc_query_transfer_orgnativtion.getSelectedItem().toString();
		LogGloble.i("...queryPayrollCompanyName==xpj", queryPayrollCompanyName);
		acc_query_transfer_orgnativtion.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
				lv_acc_query_result.removeAllViews();
				queryPayrollCompanyName = acc_query_transfer_orgnativtion.getSelectedItem().toString();
				initResultDate(position);
				LogGloble.i("...acc_query_transfer_orgnativtion.getId()==xpj", String.valueOf(position));
				LogGloble.i("...queryPayrollCompanyName==xpj", queryPayrollCompanyName);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		
		
	}
	/**查询结果页面根据单位加载其他的数据*/
	private void initResultDate(int param){
		String timesMonth = queryPayrollTimeList.get(param);
		timesMonth = DateUtils.DateFormatter(timesMonth);
		timesMonth = DateUtils.formatStr(timesMonth);
		LogGloble.i("...timesMonth==xpj", timesMonth);
		timesMonth = timesMonth.substring(0, 8);
		tv_acc_time_currency_value.setText(timesMonth);
		tv_acc_info_name_value.setText(queryPayrollName);
//		StringUtil
//		.getForSixForString(acc_account_num)
//		tv_acc_query_result_cardnumber.setText(text);
		String accountNumber = StringUtil.getForSixForString((String)bankListAccount.get(Acc.ACC_ACCOUNTNUMBER_RES));
		tv_acc_query_result_cardnumber.setText(accountNumber);
		String payrollInfo = queryPayrollInfoList.get(param);
		if(payrollInfo == null || payrollInfo.length() == 0){
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					BaseDroidApp.getInstanse().getCurrentAct()
							.getString(R.string.acc_error_querypayrollnull));
//			rl_query_transfer_result.setVisibility(View.GONE);
			return;
		}
		String[] payrollInfot = payrollInfo.split("\\|");
		for(int i=0; i<payrollInfot.length; i++){
			String[] payrollInfott = payrollInfot[i].split(",|\\，");
			String payrollKey = payrollInfott[0];
			String payrollValue;
			try {
				payrollValue = payrollInfott[1];
			} catch (Exception e) {
				payrollValue = "-";
			}
			
			/**603需求变更 不进行格式化 针对添加了证件类型和证件号做了调整*/
//			payrollValue = StringUtil.parseStringPattern(payrollValue, 2);
			LogGloble.i("====payrollKey==xpj"+i, payrollKey);
			LogGloble.i("====payrollValue==xpj"+i, payrollValue);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			LinearLayout childLayout = new LinearLayout(this);
			childLayout.setLayoutParams(layoutParams);
			childLayout.setOrientation(LinearLayout.HORIZONTAL);
			TextView textKey = new TextView(this);
			TextView textValue = new TextView(this);
			textKey.setGravity(Gravity.RIGHT);
			textValue.setGravity(Gravity.RIGHT);
			textKey.setTextSize(getResources().getDimensionPixelOffset(R.dimen.textsize_default)/densityRadio);
			textValue.setTextSize(getResources().getDimensionPixelSize(R.dimen.textsize_default)/densityRadio);
			textKey.setText(payrollKey+" : ");
			if(StringUtil.isNullOrEmpty(payrollValue)){
				textValue.setText("-");
			}else {
				textValue.setText(payrollValue);
			}
			LogGloble.i("====textKey"+i, textKey.getText().toString());
			LogGloble.i("====textValue"+i, textValue.getText().toString());
//			childLayout.addView(textKey);
//			childLayout.addView(textValue);
			childLayout.addView(textKey, new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));
			childLayout.addView(textValue, new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));
			lv_acc_query_result.addView(childLayout);
			
		}
		
	}
	
}
