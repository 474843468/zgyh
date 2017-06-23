package com.chinamworld.bocmbci.biz.finc.myfund;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.adapter.FundFDYKListAdapter;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.SpinnerAdapter;

/**
 * 基金浮动盈亏
 * 
 * @author xyl
 * 
 */
public class FinFdykcActivity extends FincBaseActivity implements
		OnItemSelectedListener, OnTouchListener {
	private static final String TAG = "FinFdykcActivity";
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
	private Button qureryMy;
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
	private Animation animation_down;
	/**
	 * 查询条件页面
	 */
	private LinearLayout queryBeforLayout;
	/**
	 * 查询结果
	 */
	private LinearLayout query_resultLayout;
	private TextView queryTimeTextView;

	private LinearLayout queryBeforLayoutOut;
	private OnItemClickListener onItemClickListener;
	private String fundCodeStr;
	private TextView fundQueryTv;
	private Spinner fundCodeSpinner;
	private ArrayList<String> fundCodeList;
	private ArrayList<String> fundCodeNameList;
	private String fundCodeAndNameStr;
	private SpinnerAdapter adapter;
	private int position = 0;
	// 查询后界面
	private LinearLayout query_after;

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
	public void getFDYKListCallback(Object resultObj) {
		super.getFDYKListCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		queryTimeTextView.setText(startDateStr + "-" + endDateStr);
		resultList = (List<Map<String, Object>>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(resultList)) {
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
			// query_resultLayout.setVisibility(View.GONE);
			queryListView.setVisibility(View.GONE);
			return;
		} else {
			if(queryBeforLayout.isShown()){
				queryBeforLayout.setVisibility(View.GONE);
				query_after.setVisibility(View.VISIBLE);
				query_resultLayout.setVisibility(View.VISIBLE);
				
			}else {
				queryBeforLayout.setVisibility(View.VISIBLE);
				query_after.setVisibility(View.GONE);
				query_resultLayout.setVisibility(View.VISIBLE);
			}
			queryListView.setAdapter(new FundFDYKListAdapter(this, resultList));
			queryListView.setOnItemClickListener(onItemClickListener);
//			queryBeforLayout.setBackgroundResource(R.drawable.img_bg_query_no);
//			query_resultLayout.setVisibility(View.VISIBLE);
//			queryBeforLayout.setVisibility(View.VISIBLE);
//			queryBeforLayout.startAnimation(animation_up);
			fundQueryTv.setText(fundCodeAndNameStr);
		}
	}

	private void clearData() {
		if (resultList != null && queryListView != null) {
			queryListView.setAdapter(new FundFDYKListAdapter(this,
					new ArrayList<Map<String, Object>>()));
		}
	}

	private void init() {
		tabcontent.setPadding(0, 0, 0, 0);

		String title = getResources().getString(R.string.finc_title_fdyk);
		setTitle(title);
		View view = mainInflater.inflate(R.layout.finc_fdyk_main, null);
		tabcontent.addView(view);
		initListHeaderView(R.string.finc_fundcode, R.string.finc_fundname,
				R.string.finc_total_Float);
		queryListView = (ListView) findViewById(R.id.finc_querydeal_listview);
		startDateTv = (TextView) findViewById(R.id.finc_query_deal_startdate);
		endDateTv = (TextView) findViewById(R.id.finc_query_deal_enddate);
		startDateTv.setInputType(InputType.TYPE_NULL);
		endDateTv.setInputType(InputType.TYPE_NULL);
		fundQueryTv = (TextView) findViewById(R.id.finc_fund_query_tv);

		startDateStr = QueryDateUtils.getFincLastYear(dateTime);
		endDateStr = QueryDateUtils.getFincLastDay(dateTime);
		// 系统当前时间格式化
		currenttime = QueryDateUtils.getcurrentDate(dateTime);
		// 初始结束时间赋值
		startDateTv.setText(startDateStr);// 开始日期
		endDateTv.setText(endDateStr);
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
		startDateTv.setOnClickListener(this);
		endDateTv.setOnClickListener(this);
		upLayout.setOnClickListener(this);
		downLayout.setOnClickListener(this);
		onItemClickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				BaseDroidApp.getInstanse().getBizDataMap()
						.put(Finc.D_FDYKMAP, resultList.get(position));
				startActivity(new Intent(FinFdykcActivity.this,
						FundFdykDetailsActivity.class));
			}
		};
		initRightBtnForMain();
		prePareSpinnerList();
		fundCodeSpinner = (Spinner) findViewById(R.id.finc_fundcode_spinner);
		fundCodeSpinner.setOnItemSelectedListener(this);
		fundCodeSpinner.setOnTouchListener(this);
		if (fundCodeNameList.size() == 0) {
			adapter = new SpinnerAdapter(this, fundCodeNameList, "");
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.finc_slelectfundplease_new));

		} else if (fundCodeNameList.size() == 1) {
			adapter = new SpinnerAdapter(this, fundCodeNameList,
					fundCodeNameList.get(0));
			adapter.setSelected(true);
			fundCodeStr = fundCodeList.get(0);
		} else {
			fundCodeNameList.add(0, getString(R.string.all));
			fundCodeList.add(0, "");
			adapter = new SpinnerAdapter(this, fundCodeNameList,
					getString(R.string.all));
			fundCodeStr = "";
			adapter.setSelected(true);
		}
		fundCodeSpinner.setAdapter(adapter);
	}

//	/**
//	 * 初始化下拉 和上拉动画
//	 * 
//	 * @Author xyl
//	 */
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
//			queryBeforLayoutOut.setVisibility(View.VISIBLE);
//			queryBeforLayoutOut.bringToFront();
//			query_resultLayout.setVisibility(View.VISIBLE);
//			queryBeforLayout.startAnimation(animation_down);
			if(queryBeforLayout.isShown()){
				queryBeforLayout.setVisibility(View.GONE);
				query_after.setVisibility(View.VISIBLE);
				query_resultLayout.setVisibility(View.VISIBLE);
				
			}else {
				queryBeforLayout.setVisibility(View.VISIBLE);
				query_after.setVisibility(View.GONE);
				query_resultLayout.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.finc_query_deal_querymy:// 查询按钮
			if (!adapter.isSelected()) {// 如果没有选择基金
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getString(R.string.finc_slelectfundplease));
				return;

			}
			getQueryCondition();
			startDateStr = startDateTv.getText().toString();
			endDateStr = endDateTv.getText().toString();
			if (!validationDate(startDateStr, endDateStr))
				return;
			/** 判断2个时间相隔不能超过1年 */
			int days;
			try {
				days = QueryDateUtils.daysBetween(startDateStr, endDateStr);
				if (days > 365) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getString(R.string.acc_query_date_in_year));
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			BaseHttpEngine.showProgressDialog();
			clearData();
			getFDYKList(fundCodeStr, startDateStr, endDateStr);
			break;
		case R.id.finc_query_deal_startdate:
			c = QueryDateUtils.getCalendarWithDate(startDateTv.getText()
					.toString());
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

					/** 为EditText赋值 */
					startDateTv.setText(date.toString());
				}
			}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH));
			dialog.show();
			break;
		case R.id.finc_query_deal_enddate:
			c = QueryDateUtils.getCalendarWithDate(endDateTv.getText()
					.toString());
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
					endDateTv.setText(date.toString());

				}
			}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH));
			dialog.show();
			break;
		default:
			break;
		}
	}

	/**
	 * 初始化查询条件的起止时间
	 * 
	 * @param sysDate
	 *            系统时间
	 * @param startDateEt
	 *            起始日期显示控件
	 * @param endDateEt
	 *            结束日期显示控件
	 */
	// private void initQueryDate(String sysDate, TextView startDateEt, TextView
	// endDateEt){
	// if(sysDate == null)
	// return;
	// startDateEt.setText(QueryDateUtils.getFincLastYear(sysDate));
	// if(QueryDateUtils.isFirstSecondDayOfYear(sysDate)){
	// endDateEt.setText(QueryDateUtils.getFincLastDayOfYear(sysDate));
	// }else{
	// endDateEt.setText(QueryDateUtils.getFincLastDay(sysDate));
	// }
	// }

	private boolean validationDate(String startDate, String endDate) {
		if (!QueryDateUtils.compareDate(startDate, endDate)) {
			// 开始日期在结束日期之前
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.acc_query_errordate));
			return false;
		}
		return true;
	}

	private void getQueryCondition() {
		fundCodeStr = fundCodeList.get(position);
		fundCodeAndNameStr = fundCodeNameList.get(position);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.finc_fundcode_spinner:
			adapter.setSelected(true);
			adapter.notifyDataSetChanged();
			fundCodeStr = fundCodeList.get(0);
			fundCodeAndNameStr = fundCodeNameList.get(0);
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
			this.position = position;
			break;

		default:
			break;
		}

	}

	private void prePareSpinnerList() {
		fundCodeList = new ArrayList<String>();
		fundCodeNameList = new ArrayList<String>();
		for (Map<String, Object> map : fincControl.fundBalancList) {
			Map<String, String> fundInfoMap = (Map<String, String>) map
					.get(Finc.FINC_FUNDINFO);
			String fundCode = fundInfoMap.get(Finc.FINC_FUNDCODE_REQ);
			String fundName = fundInfoMap.get(Finc.FINC_FUNDNAME);
			fundCodeList.add(fundCode);
			fundCodeNameList.add(fundCode + " " + fundName);

		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

}
