package com.chinamworld.bocmbci.biz.prms.query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.prms.PrmsBaseActivity;
import com.chinamworld.bocmbci.biz.prms.adapter.PrmsQueryEntrustNowListAdapter;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.ViewUtils;

/**
 * 贵金属历史委托查询查询
 * 
 * @author xyl
 * 
 */
public class PrmsQueryEntrustHistoryActivity extends PrmsBaseActivity {
	private static final String TAG = "PrmsQueryEntrustHistoryActivity";
	/**
	 * 我的账户贵金属View
	 */
	private ListView queryListView = null;
	private OnClickListener footerOnclickListenner = null;
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
	 * 查询最近一个星期
	 */
	private Button queryOneWeek;
	/**
	 * 查询最近三个月
	 */
	private Button queryOneMonth;
	/**
	 * 查询最近三个月的交易记录
	 */
	private Button queryThreeMonths;
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
	/** 系统时间 */
	private String currenttime;
//	/** 退出动画 */
//	private Animation animation_up;
//	/** 进入动画 */
//	private Animation animation_down;
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

	private int currentIndex = 0;
	private int pageSize = 10;
	/** 总数 */
	private int totalNum;

	/** 是否 是新的查询 */
	private boolean isNewQuery = true;

	private PrmsQueryEntrustNowListAdapter adatper;
	private View footerView;
	private View view;
	private View query_result_condition_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestSystemDateTime();
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		init();
	}

	private void addfooteeView(ListView listView) {
		if (footerView != null) {
			removeFooterView(listView);
		}
		footerView = ViewUtils.getQuryListFooterView(this);
		footerOnclickListenner = new OnClickListener() {

			@Override
			public void onClick(View v) {
				isNewQuery = false;
				BaseHttpEngine.showProgressDialog();
				queryPrmsTradeEntrustHistory(startDateStr, endDateStr,
						currentIndex, pageSize, null, isNewQuery);

			}
		};
		footerView.setOnClickListener(footerOnclickListenner);
		listView.addFooterView(footerView, null, true);
	}

	private void removeFooterView(ListView listView) {
		listView.removeFooterView(footerView);
	}

	private void init() {
		tabcontent.setPadding(0, 0, 0, 0);
		String title = getResources().getString(
				R.string.prms_query_entrust_history);
		setTitle(title);
		view = mainInflater.inflate(R.layout.prms_query_deal_main, null);
		tabcontent.addView(view);
		queryListView = (ListView) findViewById(R.id.prms_querydeal_listview);
		startDateTv = (TextView) findViewById(R.id.prms_query_deal_startdate);
		endDateTv = (TextView) findViewById(R.id.prms_query_deal_enddate);
		startDateTv.setInputType(InputType.TYPE_NULL);
		endDateTv.setInputType(InputType.TYPE_NULL);
		// 三天前的日期
		String startThreeDate = QueryDateUtils.getlastthreeDate(dateTime);
		startDateTv.setText(startThreeDate);// 开始日期
		// 系统当前时间格式化
		currenttime = QueryDateUtils.getcurrentDate(dateTime);
		// 初始结束时间赋值
		endDateTv.setText(currenttime);
		startDateStr = startDateTv.getText().toString();
		endDateStr = endDateTv.getText().toString();
		qureryMy = (Button) findViewById(R.id.prms_query_deal_querymy);
		queryOneWeek = (Button) findViewById(R.id.prms_queryquery_deal_queryoneweek);
		queryOneMonth = (Button) findViewById(R.id.prms_queryquery_deal_queryonemouth);
		queryThreeMonths = (Button) findViewById(R.id.prms_queryquery_deal_querythreemouths);
		upBtn = (ImageView) findViewById(R.id.prms_up_imageView);
		dowBtn = (ImageView) findViewById(R.id.prms_down_imageView);
		upLayout = (LinearLayout) findViewById(R.id.prms_query_deal_main_uplayout);
		downLayout = (LinearLayout) findViewById(R.id.prms_down_LinearLayout);
		queryBeforLayout = (LinearLayout) findViewById(R.id.prms_query_deal_befor_layout1);
		query_resultLayout = (LinearLayout) findViewById(R.id.query_reult_layout);
		queryTimeTextView = (TextView) findViewById(R.id.prms_query_result_time);
		queryBeforLayoutOut = (LinearLayout) findViewById(R.id.prms_query_deal_befor_layout);
		queryBeforLayoutOut.setVisibility(View.VISIBLE);
		query_resultLayout.setVisibility(View.GONE);
//		initanimation();
		query_result_condition_layout = findViewById(R.id.query_result_condition_layout);
		queryBeforLayoutOut.bringToFront();
		View headerView = findViewById(R.id.prms_listheader_layout);
		initListHeaderView(headerView, R.string.prms_query_detailes_entrustId,
				R.string.prms_buycurrency_no, R.string.prms_salecurrency_no);
		initRightBtnForMain();
		initListenner();
//		BaseHttpEngine.showProgressDialog();
//		requestCommConversationId();
		
		//wuhan
		queryBeforLayout.setVisibility(View.VISIBLE);
		query_result_condition_layout.setVisibility(View.GONE);
		query_resultLayout.setVisibility(View.GONE);
		queryListView.setVisibility(View.GONE);
	}

	private void initListenner() {
		upBtn.setOnClickListener(this);
		dowBtn.setOnClickListener(this);
		qureryMy.setOnClickListener(this);
		queryOneMonth.setOnClickListener(this);
		queryOneWeek.setOnClickListener(this);
		queryThreeMonths.setOnClickListener(this);
		startDateTv.setOnClickListener(this);
		endDateTv.setOnClickListener(this);
		upLayout.setOnClickListener(this);
		downLayout.setOnClickListener(this);
		queryListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent i = new Intent();
				i.putExtra(Prms.I_POSITION, position);
				i.putExtra(
						Prms.I_ENTRUSTQUERY_TITLE,
						getResources().getString(
								R.string.prms_query_entrust_history));
				i.setClass(PrmsQueryEntrustHistoryActivity.this,
						PrmsQueryEntrustNowDetailsActivity.class);
				startActivityForResult(i, 1);
			}
		});
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
////				queryTimeTextView.setText(startDateStr + "-" + endDateStr);
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
		case R.id.prms_up_imageView:// up button to show the query view
		case R.id.prms_query_deal_main_uplayout:
//			if (!StringUtil.isNullOrEmpty(prmsControl.queryEntrustNowList)) {// 如果还未查询
//				// 收起查询页面
//				queryBeforLayout.startAnimation(animation_up);
//			}
			queryBeforLayout.setVisibility(View.GONE);
			query_result_condition_layout.setVisibility(View.VISIBLE);
			query_resultLayout.setVisibility(View.VISIBLE);
			queryListView.setVisibility(View.VISIBLE);
			
			break;
		case R.id.prms_down_imageView:// 下拉按钮
		case R.id.prms_down_LinearLayout:
//			query_resultLayout.setVisibility(View.VISIBLE);
			queryBeforLayoutOut.bringToFront();
//			queryBeforLayoutOut.setVisibility(View.VISIBLE);
//			queryBeforLayout.startAnimation(animation_down);
			queryBeforLayout.setVisibility(View.VISIBLE);
			query_result_condition_layout.setVisibility(View.GONE);
			query_resultLayout.setVisibility(View.VISIBLE);
			queryListView.setVisibility(View.VISIBLE);
			break;
		case R.id.prms_query_deal_querymy:// 查询按钮
			if(!QueryDateUtils.commQueryStartAndEndateReg(this, startDateTv.getText().toString(), 
					endDateTv.getText().toString(), dateTime))
				return;
			startDateStr = startDateTv.getText().toString();
			endDateStr = endDateTv.getText().toString();
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();
			break;
		case R.id.prms_queryquery_deal_queryoneweek:// 最近一周
			endDateStr = QueryDateUtils.getcurrentDate(currenttime);
			startDateStr = QueryDateUtils.getlastWeek(currenttime);
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();
			break;
		case R.id.prms_queryquery_deal_queryonemouth:// 最近一个月
			endDateStr = QueryDateUtils.getcurrentDate(currenttime);
			startDateStr = QueryDateUtils.getlastOneMonth(currenttime);
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();
			break;
		case R.id.prms_queryquery_deal_querythreemouths:// 最近三个月
			endDateStr = QueryDateUtils.getcurrentDate(currenttime);
			startDateStr = QueryDateUtils.getlastThreeMonth(currenttime);
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();
			break;
		case R.id.prms_query_deal_startdate: // 设定开始日期
			c = QueryDateUtils.getCalendarWithDate(startDateTv.getText().toString());
			dialog = new DatePickerDialog(PrmsQueryEntrustHistoryActivity.this,
					new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							/** 选择的开始日期 */
							StringBuffer date = new StringBuffer();
							date.append(String.valueOf(year));
							date.append("/");
							int month = monthOfYear + 1;
							date.append(((month < 10) ? ("0" + month)
									: (month + "")));
							date.append("/");
							date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth)
									: (dayOfMonth + "")));

//							startDateStr = date.toString();
							/** 为EditText赋值 */
							startDateTv.setText(date.toString());
						}
					}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH));
			dialog.show();
			break;
		case R.id.prms_query_deal_enddate:
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
		default:
			break;
		}
	}

	@Override
	public void queryPrmsTradeEntrustHistoryCallBack(Object resultObj) {
		super.queryPrmsTradeEntrustHistoryCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		queryTimeTextView.setText(startDateStr + "-" + endDateStr);
		if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
			query_resultLayout.setVisibility(View.INVISIBLE);
			return;
		}
		Map<String, Object> data = (Map<String, Object>) (biiResponseBody
				.getResult());
		List<Map<String, Object>> tempList = (List<Map<String, Object>>) data
				.get(Prms.PRMS_QUERY_DEAL_LIST);
		currentIndex += 10;
		if (StringUtil.isNullOrEmpty(tempList) && isNewQuery) {// 没有查询到数据
			//fsm 如果开启新的查询且没有查到数据则隐藏列表
//			query_resultLayout.setVisibility(View.INVISIBLE);
			prmsControl.queryEntrustNowList = null;
			queryBeforLayout.setBackgroundResource(R.drawable.img_bg_query_no);
//			queryBeforLayout.startAnimation(animation_up);
			queryBeforLayout.setVisibility(View.GONE);
			query_result_condition_layout.setVisibility(View.VISIBLE);
			query_resultLayout.setVisibility(View.VISIBLE);
			queryListView.setVisibility(View.VISIBLE);
			return;
		}
		query_resultLayout.setVisibility(View.VISIBLE);
		queryListView.setVisibility(View.VISIBLE);
		totalNum = Integer.valueOf((String) data
				.get(Prms.PRMS_QUERY_DEAL_RECORDNUMBER));

		if (!isNewQuery && prmsControl.queryEntrustNowList != null
				&& adatper != null) {
			prmsControl.queryEntrustNowList.addAll(tempList);
			adatper.notifyDataSetChanged(prmsControl.queryEntrustNowList);
			// if(pageSize*currentIndex>=totalNum){//全部显示完鸟
			if (prmsControl.queryEntrustNowList.size() >= totalNum) {// 全部显示完鸟\
				removeFooterView(queryListView);
			}
		} else {
			prmsControl.queryEntrustNowList = tempList;
			adatper = new PrmsQueryEntrustNowListAdapter(this,
					prmsControl.queryEntrustNowList);
			if (totalNum > 10) {// 不能显示完
				addfooteeView(queryListView);
			} else {
				removeFooterView(queryListView);
			}
			queryListView.setAdapter(adatper);
			queryBeforLayout.setBackgroundResource(R.drawable.img_bg_query_no);
//			queryBeforLayout.startAnimation(animation_up);
			queryBeforLayout.setVisibility(View.GONE);
			query_result_condition_layout.setVisibility(View.VISIBLE);
			query_resultLayout.setVisibility(View.VISIBLE);
			queryListView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		currentIndex = 0;
		isNewQuery = true;
		clearData();
		queryPrmsTradeEntrustHistory(startDateStr, endDateStr, currentIndex,
				pageSize, null, isNewQuery);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			BaseHttpEngine.showProgressDialogCanGoBack();
			requestCommConversationId();
			queryPrmsTradeEntrustHistory(startDateStr, endDateStr,
					currentIndex, pageSize, null, isNewQuery);
			break;

		default:
			break;
		}
	}

	private void clearData() {
		if (prmsControl.queryEntrustNowList != null && queryListView != null
				&& adatper != null) {
			removeFooterView(queryListView);
			prmsControl.queryEntrustNowList = new ArrayList<Map<String, Object>>();
			adatper.notifyDataSetChanged(prmsControl.queryEntrustNowList);
		}
	}
}
