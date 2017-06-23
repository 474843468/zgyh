package com.chinamworld.bocmbci.biz.servicerecord;

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
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.ViewUtils;
import com.chinamworld.bocmbci.widget.adapter.SpinnerAdapter;

public class ServiceRecordQueryActivity extends ServiceRecordBaseActivity
		implements OnItemSelectedListener, OnTouchListener {
	/** 查询后的列表View */
	private ListView queryListView = null;
	/** 查询开始日期 */
	private TextView startDateTv;
	/** 查询结束日期 */
	private TextView endDateTv;
	/** 查询按钮 */
	private Button oneWeekBtn, oneMonthBtn, threeMonthsBtn, qureryMy;
	/*** 开始查询日期 */
	private String startDateStr;
	/** 结束查询日期 */
	private String endDateStr;
	/** 上升按钮 这个是查询效果 */
	private ImageView upBtn;
	private LinearLayout upLayout;
	/** 下拉按钮 这个是 到查询界面 */
	private ImageView dowBtn;
	private LinearLayout downLayout;
	/** 查询到的结果 */
	private List<Map<String, Object>> resultList;
	/** 业务类型 */
	private TextView fundQueryTv;
	/** 系统时间 */
	private String currenttime;
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
	private String svrRecType, transTypeName;
	private Spinner transTypeSpinner;
	private List<String> transTypeCodeList;
	private List<String> transTypeNameList;
	private SpinnerAdapter adapter;
	private int pageSize = 10, currentIndex, totalNum;
	private boolean isNewQuery;
	private View footerView;
	private OnClickListener footerOnclickListenner;
	private ServiceRecordQueryHistoryListAdapter hisToryAdapter;
	/** 退出动画 */
	private Animation animation_up;
	/** 进入动画 */
	private Animation animation_down;
	/** 交易类型位置 */
	private int transTypePosition = 0;
	// 查询后的布局
	private LinearLayout query_after;
	private boolean isMoreButtn=false;
	private boolean isfirst=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
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

	private void init() {
		tabcontent.setPadding(0, 0, 0, 0);
		String title = getResources().getString(
				R.string.servicerecord_query_history);
		setTitle(title);
		View view = mainInflater.inflate(R.layout.service_record_history, null);
		tabcontent.addView(view);
		initListHeaderView(R.string.service_record_date,
				R.string.service_record_type_service_new,
				R.string.service_record_money);
		queryListView = (ListView) findViewById(R.id.finc_querydeal_listview);
		startDateTv = (TextView) findViewById(R.id.finc_query_deal_startdate);
		endDateTv = (TextView) findViewById(R.id.finc_query_deal_enddate);
		startDateTv.setInputType(InputType.TYPE_NULL);
		endDateTv.setInputType(InputType.TYPE_NULL);
		fundQueryTv = (TextView) findViewById(R.id.finc_fund_query_tv);
		oneWeekBtn = (Button) findViewById(R.id.finc_queryquery_deal_queryoneweek);
		oneMonthBtn = (Button) findViewById(R.id.finc_queryquery_deal_queryonemouth);
		threeMonthsBtn = (Button) findViewById(R.id.finc_queryquery_deal_querythreemouths);

		resultList = new ArrayList<Map<String, Object>>();
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

		queryBeforLayout = (LinearLayout) findViewById(R.id.finc_query_deal_befor_layout);
		queryBeforLayout.setVisibility(View.VISIBLE);

		query_after = (LinearLayout) findViewById(R.id.query_after);
		query_after.setVisibility(View.GONE);

		query_resultLayout = (LinearLayout) findViewById(R.id.query_reult_layout);
		query_resultLayout.setVisibility(View.GONE);

		queryTimeTextView = (TextView) findViewById(R.id.finc_query_result_time);
		// queryBeforLayoutOut = (LinearLayout)
		// findViewById(R.id.finc_query_deal_befor_layout);
		// queryBeforLayoutOut.setVisibility(View.VISIBLE);
		query_resultLayout.setVisibility(View.GONE);
		// initanimation();
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
				// BaseDroidApp.getInstanse().getBizDataMap()
				// .put(Finc.D_HISTORY_DETAIL, resultList.get(position));
				Map<String, Object> resultMap = resultList.get(position);
				Intent intent = new Intent();
				intent.setClass(ServiceRecordQueryActivity.this,
						ServiceRecordHistoryDetailsActivity.class);
				intent.putExtra("TransStatus",
						(String) resultMap.get("TransStatus"));
				intent.putExtra("SvrRecType",
						(String) resultMap.get("SvrRecType"));
				intent.putExtra("CreatTime",
						(String) resultMap.get("CreatTime"));
				intent.putExtra("Channel", (String) resultMap.get("Channel"));
				intent.putExtra("CurrencyCode",
						(String) resultMap.get("CurrencyCode"));
				String amount=StringUtil
						.parseStringCodePattern((String) resultMap.get("CurrencyCode"),
								(String) resultMap.get("Amount"), 2);
				
				
				if(!StringUtil.isNullOrEmpty(amount)){
					if(amount.indexOf(".") < 0){
						intent.putExtra("Amount", amount);
					}else {
						amount = amount.replaceAll("0+?$", "");// 去掉多余的0
						amount = amount.replaceAll("[.]$", "");// 如最后一位是.则去掉
						intent.putExtra("Amount", amount);
					}
				}else {
					intent.putExtra("Amount",amount );
				}
				
				intent.putExtra("Summary", (String) resultMap.get("Summary"));
				startActivity(intent);
			}
		};
		// initRightBtnForMain();
		prePareSpinnerList();
		transTypeSpinner = (Spinner) findViewById(R.id.finc_fundcode_spinner);
		transTypeSpinner.setOnItemSelectedListener(this);
		transTypeSpinner.setOnTouchListener(this);
		adapter = new SpinnerAdapter(this, transTypeNameList,
				getString(R.string.all));
		transTypeSpinner.setAdapter(adapter);

	}

	// /**
	// * 初始化下拉 和上拉动画
	// *
	// * @Author xyl
	// */
	// private void initanimation() {
	// animation_up = AnimationUtils.loadAnimation(this, R.anim.scale_out);
	// animation_down = AnimationUtils.loadAnimation(this, R.anim.scale_in);
	// animation_up.setAnimationListener(new AnimationListener() {
	// @Override
	// public void onAnimationStart(Animation animation) {
	// query_resultLayout.setVisibility(View.VISIBLE);
	// }
	//
	// @Override
	// public void onAnimationRepeat(Animation animation) {
	// }
	//
	// @Override
	// public void onAnimationEnd(Animation animation) {
	// queryBeforLayoutOut.setVisibility(View.GONE);
	// query_resultLayout.setVisibility(View.VISIBLE);
	// }
	// });
	// animation_down.setAnimationListener(new AnimationListener() {
	// @Override
	// public void onAnimationStart(Animation animation) {
	// query_resultLayout.setVisibility(View.VISIBLE);
	// }
	//
	// @Override
	// public void onAnimationRepeat(Animation animation) {
	// }
	//
	// @Override
	// public void onAnimationEnd(Animation animation) {
	//
	// queryBeforLayoutOut.setVisibility(View.VISIBLE);
	// }
	// });
	//
	// }

	private void prePareSpinnerList() {
		transTypeNameList = LocalData.ServiceRecordTypeNameList;
		transTypeCodeList = LocalData.ServiceRecordTypeCodeList;
	}

	private void addfooteeView(ListView listView) {
		if (footerView != null) {
			removeFooterView(listView);
		}
		if (footerOnclickListenner == null) {
			footerOnclickListenner = new OnClickListener() {

				@Override
				public void onClick(View v) {
					BaseHttpEngine.showProgressDialog();
					isMoreButtn=true;
					PsnSVRServiceRecQueryDetail(startDateStr, endDateStr,
							svrRecType, currentIndex, pageSize);
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

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Calendar c;
		DatePickerDialog dialog;
		switch (v.getId()) {
		case R.id.finc_up_imageView:// up button to show the query view
		case R.id.finc_query_deal_main_uplayout:
			if (isfirst==false) {// 如果还未查询
			} else {
				// 收起查询页面
				// queryBeforLayout.startAnimation(animation_up);
				if (queryBeforLayout.isShown()) {
					queryBeforLayout.setVisibility(View.GONE);
					query_after.setVisibility(View.VISIBLE);
				} else {
					queryBeforLayout.setVisibility(View.VISIBLE);
					query_after.setVisibility(View.GONE);
				}
			}
			break;
		case R.id.finc_down_imageView:// 下拉按钮
		case R.id.finc_down_LinearLayout:

			if (queryBeforLayout.isShown()) {
				queryBeforLayout.setVisibility(View.GONE);
				query_after.setVisibility(View.VISIBLE);
			} else {
				queryBeforLayout.setVisibility(View.VISIBLE);
				query_after.setVisibility(View.GONE);
			}
			// queryBeforLayoutOut.setVisibility(View.VISIBLE);
			// queryBeforLayoutOut.bringToFront();
			// query_resultLayout.setVisibility(View.VISIBLE);
			// // queryBeforLayout.startAnimation(animation_down);
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

					// startDateStr = date.toString();
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
					// endDateStr = date.toString();
					endDateTv.setText(date.toString());

				}
			}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH));
			dialog.show();
			break;
		case R.id.finc_queryquery_deal_queryoneweek:
			if (checkIsSelected()) {
				getQueryCondition();
				isMoreButtn=false;
				endDateStr = QueryDateUtils.getcurrentDate(currenttime);
				startDateStr = QueryDateUtils.getlastWeek(currenttime);
				BaseHttpEngine.showProgressDialog();
				// requestCommConversationId();
				currentIndex = 0;
				clearData();
				fundQueryTv.setText(transTypeName);
				PsnSVRServiceRecQueryDetail(startDateStr, endDateStr,
						svrRecType, currentIndex, pageSize);
			}
			break;
		case R.id.finc_queryquery_deal_queryonemouth:
			if (checkIsSelected()) {
				getQueryCondition();
				isMoreButtn=false;
				endDateStr = QueryDateUtils.getcurrentDate(currenttime);
				startDateStr = QueryDateUtils.getlastOneMonth(currenttime);
				BaseHttpEngine.showProgressDialog();
				// requestCommConversationId();
				currentIndex = 0;
				clearData();
				fundQueryTv.setText(transTypeName);
				PsnSVRServiceRecQueryDetail(startDateStr, endDateStr,
						svrRecType, currentIndex, pageSize);
			}
			break;
		case R.id.finc_queryquery_deal_querythreemouths:
			if (checkIsSelected()) {
				getQueryCondition();
				isMoreButtn=false;
				endDateStr = QueryDateUtils.getcurrentDate(currenttime);
				startDateStr = QueryDateUtils.getlastThreeMonth(currenttime);
				BaseHttpEngine.showProgressDialog();
				// requestCommConversationId();
				currentIndex = 0;
				clearData();
				fundQueryTv.setText(transTypeName);
				PsnSVRServiceRecQueryDetail(startDateStr, endDateStr,
						svrRecType, currentIndex, pageSize);
			}
			break;
		case R.id.finc_query_deal_querymy:// 查询按钮
			getQueryCondition();
			isMoreButtn=false;
			startDateStr = startDateTv.getText().toString();
			endDateStr = endDateTv.getText().toString();
			if (!QueryDateUtils.commQueryStartAndEndateReg(this, startDateTv
					.getText().toString(), endDateTv.getText().toString(),
					currenttime))
				return;
			if (svrRecType != null) {
				// BaseDroidApp.getInstanse().showInfoMessageDialog(
				// getString(R.string.finc_slelectTransTypePlease));
				BaseHttpEngine.showProgressDialog();
				// requestCommConversationId();
				currentIndex = 0;
				clearData();
				fundQueryTv.setText(transTypeName);
				PsnSVRServiceRecQueryDetail(startDateStr, endDateStr,
						svrRecType, currentIndex, pageSize);
				return;
			}

			if (checkIsSelected()) {
				BaseHttpEngine.showProgressDialog();
				// requestCommConversationId();
				currentIndex = 0;
				clearData();
				fundQueryTv.setText(transTypeName);
				PsnSVRServiceRecQueryDetail(startDateStr, endDateStr,
						svrRecType, currentIndex, pageSize);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void psnsvrservicerecquerydetailCallback(Object resultObj) {
		super.psnsvrservicerecquerydetailCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		queryTimeTextView.setText(startDateStr + "-" + endDateStr);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		isfirst=true;
		if (StringUtil.isNullOrEmpty((List<Map<String, Object>>) resultMap
				.get(Finc.RESULLT_LIST))) {
			if (queryBeforLayout.isShown()) {
				queryBeforLayout.setVisibility(View.VISIBLE);
				query_after.setVisibility(View.GONE);
				query_resultLayout.setVisibility(View.GONE);
			}
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.finc_query_noresult_error));
			return;
		}
		if (queryBeforLayout.isShown()) {
			queryBeforLayout.setVisibility(View.GONE);
			query_after.setVisibility(View.VISIBLE);
			query_resultLayout.setVisibility(View.VISIBLE);
		} else {
			queryBeforLayout.setVisibility(View.VISIBLE);
			query_after.setVisibility(View.GONE);
			query_resultLayout.setVisibility(View.VISIBLE);
		}
		if(isMoreButtn){
			queryBeforLayout.setVisibility(View.GONE);
			query_after.setVisibility(View.VISIBLE);
			
		}
		totalNum = Integer.valueOf((String) resultMap
				.get(Prms.PRMS_QUERY_DEAL_RECORDNUMBER));
		currentIndex += 10;
		if (resultList.size() != 0 && hisToryAdapter != null
				&& resultList != null) {
			resultList.addAll((List<Map<String, Object>>) resultMap
					.get(Finc.RESULLT_LIST));
			hisToryAdapter.notifyDataSetChanged(resultList);
			if (resultList.size() >= totalNum) {// 全部显示完鸟\
				removeFooterView(queryListView);
			}
		} else {
			resultList = (List<Map<String, Object>>) resultMap
					.get(Finc.RESULLT_LIST);
			hisToryAdapter = new ServiceRecordQueryHistoryListAdapter(this,
					resultList);
			if (totalNum > 10) {// 不能显示完
				addfooteeView(queryListView);
			} else {
				removeFooterView(queryListView);
			}
			queryListView.setAdapter(hisToryAdapter);
			queryListView.setOnItemClickListener(onItemClickListener);
			queryBeforLayout.setBackgroundResource(R.drawable.img_bg_query_no);
			// queryBeforLayout.startAnimation(animation_up);
		}

	}

	private void getQueryCondition() {
		svrRecType = transTypeCodeList.get(transTypePosition);
		transTypeName = transTypeNameList.get(transTypePosition);
	}

	/**
	 * 检查是否选择有交易类型
	 */
	private boolean checkIsSelected() {
		if (!adapter.isSelected()) {// 如果没有选择
			adapter.setSelected(true);
			adapter.notifyDataSetChanged();
			transTypeSpinner.setSelection(0);
			svrRecType = transTypeCodeList.get(0);
			transTypeName = transTypeNameList.get(0);
			// BaseDroidApp.getInstanse().showInfoMessageDialog(
			// getString(R.string.finc_slelectTransTypePlease));
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
			svrRecType = transTypeCodeList.get(0);
			transTypeName = transTypeNameList.get(0);
			break;

		default:
			break;
		}
		return false;
	}

	private void clearData() {
		if (resultList != null && queryListView != null
				&& hisToryAdapter != null) {
			removeFooterView(queryListView);
			resultList.clear();
			hisToryAdapter
					.notifyDataSetChanged(new ArrayList<Map<String, Object>>());
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long arg3) {
		switch (parent.getId()) {
		case R.id.finc_fundcode_spinner:
			transTypePosition = position;
			break;

		default:
			break;
		}

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isfirst=false;
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
}
