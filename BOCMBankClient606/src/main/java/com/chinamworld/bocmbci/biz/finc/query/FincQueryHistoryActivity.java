package com.chinamworld.bocmbci.biz.finc.query;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.adapter.FundQueryHistoryListAdapter;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.ViewUtils;
import com.chinamworld.bocmbci.widget.adapter.SpinnerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * 历史交易查询
 * 
 * @author xyl
 * 
 */
public class FincQueryHistoryActivity extends FincBaseActivity implements
		OnItemSelectedListener, OnTouchListener {
	private static final String TAG = "FincQueryHistoryActivity";
	/**
	 * 查询后的基金列表View
	 */
	private ListView queryListView = null;
	/**
	 * 查询开始日期
	 */
	private TextView startDateTv;
	/**
	 * 查询结束日期
	 */
	private TextView endDateTv;
	/**
	 * 查询按钮
	 */
	private Button  qureryMy;
	/**
	 * 日期radioButton
	 */
	private RadioButton oneWeekBtn, oneMonthBtn, threeMonthsBtn;
	/**
	 * 开始查询日期
	 */
	private String startDateStr;
	/**
	 * 结束查询日期
	 */
	private String endDateStr;
	/**
	 * 上升按钮 这个是查询效果
	 */
	private ImageView upBtn;
	private LinearLayout upLayout;
	/**
	 * 下拉按钮 这个是 到查询界面
	 */
	private ImageView dowBtn;
	private LinearLayout downLayout;
	/**
	 * 查询到的结果
	 */
	private List<Map<String, Object>> resultList;
	/** 系统时间 */
	private String currenttime;
	/** 退出动画 */
	private Animation animation_up;
	/** 进入动画 */
//	private Animation animation_down;

	/**
	 * 查询结果
	 */
	private LinearLayout query_resultLayout;
	private TextView queryTimeTextView;

	private LinearLayout queryBeforLayoutOut;
	private OnItemClickListener onItemClickListener;
	private String transTypeCode, transTypeName;
	/** 交易类型位置*/
	private int transTypePosition = 0;
	private TextView fundQueryTv;
	private Spinner transTypeSpinner;
	private List<String> transTypeCodeList;
	private List<String> transTypeNameList;
	private SpinnerAdapter adapter;
	private int pageSize = 10, currentIndex, totalNum;
	private boolean isNewQuery;
	private View footerView;
	private OnClickListener footerOnclickListenner;
	private FundQueryHistoryListAdapter hisToryAdapter;
	//查询后界面
	private LinearLayout query_after;
	/**
	 * 查询条件页面
	 */
	private LinearLayout queryBeforLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BaseHttpEngine.showProgressDialog();
		requestSystemDateTime();

	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		init();
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		currentIndex = 0;
		isNewQuery = true;
		clearData();
		fundQueryTv.setText(transTypeName);
//		startDateStr = startDateTv.getText().toString();
//		endDateStr = endDateTv.getText().toString();
		fundQueryHistory401(startDateStr, endDateStr, transTypeCode,
				currentIndex, pageSize, isNewQuery);
	}

	@Override
	public void fundQueryHistory401Callback(Object resultObj) {
		super.fundQueryHistory401Callback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		queryTimeTextView.setText(startDateStr + "-" + endDateStr);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty((List<Map<String, Object>>) resultMap
				.get(Finc.FINC_QUERYEXTRADAY_RESULTLIST))) {
			if(queryBeforLayout.isShown()){
				queryBeforLayout.setVisibility(View.VISIBLE);
				query_after.setVisibility(View.GONE);
				query_resultLayout.setVisibility(View.GONE);
			}else {
				queryBeforLayout.setVisibility(View.GONE);
				query_after.setVisibility(View.VISIBLE);
				query_resultLayout.setVisibility(View.GONE);
			}
			
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.finc_query_noresult_error));
			return;
		}
		if(queryBeforLayout.isShown()){
			queryBeforLayout.setVisibility(View.GONE);
			query_after.setVisibility(View.VISIBLE);
			query_resultLayout.setVisibility(View.VISIBLE);
			
		}else {
			queryBeforLayout.setVisibility(View.VISIBLE);
			query_after.setVisibility(View.GONE);
			query_resultLayout.setVisibility(View.VISIBLE);
		}
		
		
		totalNum = Integer.valueOf((String) resultMap
				.get(Prms.PRMS_QUERY_DEAL_RECORDNUMBER));
		currentIndex += 10;
		if (!isNewQuery && resultList != null && hisToryAdapter != null) {
			resultList.addAll((List<Map<String, Object>>) resultMap
					.get(Finc.FINC_QUERYEXTRADAY_RESULTLIST));
			hisToryAdapter.notifyDataSetChanged(resultList);
			if (resultList.size() >= totalNum) {// 全部显示完鸟\
				removeFooterView(queryListView);
			}
		} else {
			resultList = (List<Map<String, Object>>) resultMap
					.get(Finc.FINC_QUERYEXTRADAY_RESULTLIST);
			hisToryAdapter = new FundQueryHistoryListAdapter(this, resultList);
			if (totalNum > 10) {// 不能显示完
				addfooteeView(queryListView);
			} else {
				removeFooterView(queryListView);
			}
			queryListView.setAdapter(hisToryAdapter);
			queryListView.setOnItemClickListener(onItemClickListener);
			queryBeforLayout.setBackgroundResource(R.drawable.img_bg_query_no);
			queryBeforLayout.startAnimation(animation_up);
		}

	}

	private void clearData() {
		if (resultList != null && queryListView != null
				&& hisToryAdapter != null) {
			removeFooterView(queryListView);
			hisToryAdapter
					.notifyDataSetChanged(new ArrayList<Map<String, Object>>());
		}
	}

	private void init() {
		tabcontent.setPadding(0, 0, 0, 0);
		String title = getResources().getString(R.string.fincn_query_history);
		setTitle(title);
		View view = mainInflater.inflate(R.layout.finc_query_history_main_401,
				null);
		tabcontent.addView(view);
		initListHeaderView(R.string.finc_dealDate, R.string.finc_fundname,
				R.string.finc_tradetype);
		queryListView = (ListView) findViewById(R.id.finc_querydeal_listview);
		startDateTv = (TextView) findViewById(R.id.finc_query_deal_startdate);
		endDateTv = (TextView) findViewById(R.id.finc_query_deal_enddate);
		startDateTv.setInputType(InputType.TYPE_NULL);
		endDateTv.setInputType(InputType.TYPE_NULL);
		fundQueryTv = (TextView) findViewById(R.id.finc_fund_query_tv);
		oneWeekBtn = (RadioButton) findViewById(R.id.finc_queryquery_deal_queryoneweek);
		oneMonthBtn = (RadioButton) findViewById(R.id.finc_queryquery_deal_queryonemouth);
		threeMonthsBtn = (RadioButton) findViewById(R.id.finc_queryquery_deal_querythreemouths);

		// 三天前的日期
		String startThreeDate = QueryDateUtils.getlastthreeDate(dateTime);
		startDateTv.setText(startThreeDate);// 开始日期
		// 系统当前时间格式化
		currenttime = QueryDateUtils.getcurrentDate(dateTime);
		// 初始结束时间赋值
		endDateTv.setText(currenttime);
		
		startDateStr = startDateTv.getText().toString();
		endDateStr = endDateTv.getText().toString();
		qureryMy = (Button) findViewById(R.id.finc_query_deal_querymy);
		upBtn = (ImageView) findViewById(R.id.finc_up_imageView);
		dowBtn = (ImageView) findViewById(R.id.finc_down_imageView);
		upLayout = (LinearLayout) findViewById(R.id.finc_query_deal_main_uplayout);
		downLayout = (LinearLayout) findViewById(R.id.finc_down_LinearLayout);
		queryBeforLayout = (LinearLayout) findViewById(R.id.finc_query_deal_befor_layout1);
		query_after=(LinearLayout)findViewById(R.id.query_after);
		query_after.setVisibility(View.GONE);
		query_resultLayout = (LinearLayout) findViewById(R.id.query_reult_layout);
		queryTimeTextView = (TextView) findViewById(R.id.finc_query_result_time);
		queryBeforLayoutOut = (LinearLayout) findViewById(R.id.finc_query_deal_befor_layout);
		queryBeforLayoutOut.setVisibility(View.VISIBLE);
		query_resultLayout.setVisibility(View.GONE);
//		initanimation();
		upBtn.setOnClickListener(this);
		dowBtn.setOnClickListener(this);
		qureryMy.setOnClickListener(this);
		oneWeekBtn.setOnClickListener(this);
		oneMonthBtn.setOnClickListener(this);
		threeMonthsBtn.setOnClickListener(this);
		startDateTv.setOnClickListener(this);
		endDateTv.setOnClickListener(this);
		upLayout.setOnClickListener(this);
		downLayout.setOnClickListener(this);
		onItemClickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				BaseDroidApp.getInstanse().getBizDataMap()
						.put(Finc.D_HISTORY_DETAIL, resultList.get(position));
				startActivity(new Intent(FincQueryHistoryActivity.this,
						FundQueryHistoryDetailsActivity.class));
			}
		};
		initRightBtnForMain();
		prePareSpinnerList();
		transTypeSpinner = (Spinner) findViewById(R.id.finc_fundcode_spinner);
		transTypeSpinner.setOnItemSelectedListener(this);
		transTypeSpinner.setOnTouchListener(this);
		adapter = new SpinnerAdapter(this, transTypeNameList,
				getString(R.string.all));
		transTypeSpinner.setAdapter(adapter);
		
	}

	private void addfooteeView(ListView listView) {
		if (footerView != null) {
			removeFooterView(listView);
		}
		if (footerOnclickListenner == null) {
			footerOnclickListenner = new OnClickListener() {

				@Override
				public void onClick(View v) {
					isNewQuery = false;
					BaseHttpEngine.showProgressDialog();
					fundQueryHistory401(startDateStr, endDateStr,
							transTypeCode, currentIndex, pageSize, isNewQuery);
				}
			};
		}
		footerView = ViewUtils.getQuryListFooterView(this);
		footerView.setOnClickListener(footerOnclickListenner);
		listView.addFooterView(footerView, null, true);
	}

	private void removeFooterView(ListView listView) {
		listView.removeFooterView(footerView);
	}

	/**
	 * 初始化下拉 和上拉动画
	 * 
	 * @Author xyl
	 */
//	private void initanimation() {
//		animation_up = AnimationUtils.loadAnimation(this, R.anim.scale_out);
//		animation_down = AnimationUtils.loadAnimation(this, R.anim.scale_in);
//		animation_up.setAnimationListener(new AnimationListener() {
//			@Override
//			public void onAnimationStart(Animation animation) {
//				query_resultLayout.setVisibility(View.VISIBLE);
//			}
//
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//			}
//
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				queryBeforLayoutOut.setVisibility(View.GONE);
//				query_resultLayout.setVisibility(View.VISIBLE);
//			}
//		});
//		animation_down.setAnimationListener(new AnimationListener() {
//			@Override
//			public void onAnimationStart(Animation animation) {
//				query_resultLayout.setVisibility(View.VISIBLE);
//			}
//
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//			}
//
//			@Override
//			public void onAnimationEnd(Animation animation) {
//
//				queryBeforLayoutOut.setVisibility(View.VISIBLE);
//			}
//		});
//
//	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Calendar c;
		DatePickerDialog dialog;
		switch (v.getId()) {
		case R.id.finc_up_imageView:// up button to show the query view
		case R.id.finc_query_deal_main_uplayout:
			if (resultList == null) {// 如果还未查询
			} else {
				// 收起查询页面
//				queryBeforLayout.startAnimation(animation_up);
				
					queryBeforLayout.setVisibility(View.GONE);
					query_after.setVisibility(View.VISIBLE);
					query_resultLayout.setVisibility(View.VISIBLE);
					
				
			}
			break;
		case R.id.finc_down_imageView:// 下拉按钮
		case R.id.finc_down_LinearLayout:
			if(queryBeforLayout.isShown()){
				queryBeforLayout.setVisibility(View.GONE);
				query_after.setVisibility(View.VISIBLE);
				query_resultLayout.setVisibility(View.VISIBLE);
				
			}else {
				queryBeforLayout.setVisibility(View.VISIBLE);
				query_after.setVisibility(View.GONE);
				query_resultLayout.setVisibility(View.VISIBLE);
			}
//			queryBeforLayoutOut.setVisibility(View.VISIBLE);
//			queryBeforLayoutOut.bringToFront();
//			query_resultLayout.setVisibility(View.VISIBLE);
//			queryBeforLayout.startAnimation(animation_down);
			break;
		case R.id.finc_query_deal_startdate:
			c = QueryDateUtils.getCalendarWithDate(startDateTv.getText().toString());
			dialog = new DatePickerDialog(this, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					/** 选择的开始日期 */
					StringBuffer date = new StringBuffer();
					date.append(String.valueOf(year));
					date.append("/");
					int month = monthOfYear + 1;
					date.append(((month < 10) ? ("0" + month) : (month + "")));
					date.append("/");
					date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth)
							: (dayOfMonth + "")));

//					startDateStr = date.toString();
					/** 为EditText赋值 */
					startDateTv.setText(date.toString());
				}
			}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH));
			dialog.show();
			
			break;
		case R.id.finc_query_deal_enddate:
			c = QueryDateUtils.getCalendarWithDate(endDateTv.getText().toString());
			dialog = new DatePickerDialog(this, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					/** 选择的截止日期 */
					StringBuffer date = new StringBuffer();
					date.append(String.valueOf(year));
					date.append("/");
					int month = monthOfYear + 1;
					date.append(((month < 10) ? ("0" + month) : (month + "")));
					date.append("/");
					date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth)
							: (dayOfMonth + "")));
//					endDateStr = date.toString();
					endDateTv.setText(date.toString());

				}
			}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH));
			dialog.show();
			break;
		case R.id.finc_queryquery_deal_queryoneweek:
			if (checkIsSelected()) {
				getQueryCondition();
				endDateStr = QueryDateUtils.getcurrentDate(currenttime);
				startDateStr = QueryDateUtils.getlastWeek(currenttime);
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
			break;
		case R.id.finc_queryquery_deal_queryonemouth:
			if (checkIsSelected()) {
				getQueryCondition();
				endDateStr = QueryDateUtils.getcurrentDate(currenttime);
				startDateStr = QueryDateUtils.getlastOneMonth(currenttime);
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
			break;
		case R.id.finc_queryquery_deal_querythreemouths:
			if (checkIsSelected()) {
				getQueryCondition();
				endDateStr = QueryDateUtils.getcurrentDate(currenttime);
				startDateStr = QueryDateUtils.getlastThreeMonth(currenttime);
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
			break;
		case R.id.finc_query_deal_querymy:// 查询按钮
			getQueryCondition();
			
			startDateStr = startDateTv.getText().toString();
			endDateStr = endDateTv.getText().toString();
			if(StringUtil.isNull(transTypeCode)){
//				BaseDroidApp.getInstanse().showInfoMessageDialog(
//						getString(R.string.finc_slelectTransTypePlease));
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
				return;
			}
			if(!QueryDateUtils.commQueryStartAndEndateReg(this, startDateTv.getText().toString(),
					endDateTv.getText().toString(), currenttime))
				return;
			if (checkIsSelected()) {
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
			break;
		default:
			break;
		}
	}
	
	private void getQueryCondition(){
		
		
		transTypeCode = transTypeCodeList.get(transTypePosition);
		transTypeName = transTypeNameList.get(transTypePosition);
	}

	/**
	 * 检查是否选择有交易类型
	 */
	private boolean checkIsSelected() {
		if (!adapter.isSelected()) {// 如果没有选择基金
			adapter.setSelected(true);
			adapter.notifyDataSetChanged();
			transTypeSpinner.setSelection(0);
			transTypeCode = transTypeCodeList.get(0);
			transTypeName = transTypeNameList.get(0);
//			BaseDroidApp.getInstanse().showInfoMessageDialog(
//					getString(R.string.finc_slelectTransTypePlease));
			return true;
		}
		return true;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		super.onTouch(v, event);
		switch (v.getId()) {
		case R.id.finc_fundcode_spinner:
			adapter.setSelected(true);
			adapter.notifyDataSetChanged();
			transTypeCode = transTypeCodeList.get(0);
			transTypeName = transTypeNameList.get(0);
			break;

		default:
			break;
		}
		return false;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.finc_fundcode_spinner:
			transTypePosition = position;
			break;

		default:
			break;
		}

	}

	private void prePareSpinnerList() {
//		transTypeCodeList = new ArrayList<String>();
//		transTypeNameList = new ArrayList<String>();
//		transTypeNameList.add(getString(R.string.all));
//		transTypeCodeList.add(getString(R.string.all));
//		transTypeNameList.addAll(LocalData.fincTransTypeNameList);
//		transTypeCodeList.addAll(LocalData.fincTransTypeCodeList);
		transTypeNameList = LocalData.fincTransTypeNameList;
		transTypeCodeList = LocalData.fincTransTypeCodeList;
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}

}
