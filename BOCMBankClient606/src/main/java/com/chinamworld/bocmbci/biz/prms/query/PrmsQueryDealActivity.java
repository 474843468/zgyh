package com.chinamworld.bocmbci.biz.prms.query;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.prms.PrmsBaseActivity;
import com.chinamworld.bocmbci.biz.prms.adapter.PrmsQueryDealListAdapter;
import com.chinamworld.bocmbci.biz.prms.control.PrmsControl;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.ViewUtils;

/**
 * 贵金属成效状况查询
 * 
 * @author xyl
 * 
 */
public class PrmsQueryDealActivity extends PrmsBaseActivity {
	private static final String TAG = "PrmsQueryDealActivity";
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
	/**
	 * 查询到的结果
	 */
	private List<Map<String, Object>> resultList;
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

	private PrmsQueryDealListAdapter adatper;
	private View footerView;
	private View query_result_condition_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.prms_title_query_deal_new));
		setLeftSelectedPosition("prmsManage_3");
		if (getIntent().getBooleanExtra(ConstantGloble.COMEFROMFOOTFAST, false)) {
			BaseHttpEngine.showProgressDialogCanGoBack();
			checkRequestPsnInvestmentManageIsOpen();
		} else {
			BaseHttpEngine.showProgressDialogCanGoBack();
			requestSystemDateTime();
		}

	}

	public void checkRequestPsnInvestmentManageIsOpen() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_API);
		biiRequestBody.setConversationId(null);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this,
				"checkRequestPsnInvestmentManageIsOpenCallback");
	}

	public void checkRequestPsnInvestmentManageIsOpenCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String isOpenOr = (String) biiResponseBody.getResult();
		if (StringUtil.parseStrToBoolean(isOpenOr)) {
			prmsControl.ifInvestMent = true;
		} else {
			prmsControl.ifInvestMent = false;
		}
		queryPrmsAcc();
	}

	@Override
	public void queryPrmsAccCallBack(Object resultObj) {
		super.queryPrmsAccCallBack(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getResult() == null) {
			prmsControl.ifhavPrmsAcc = false;
			prmsControl.accMessage = null;
			prmsControl.accId = null;
		} else {
			prmsControl.accMessage = (Map<String, String>) biiResponseBodys
					.get(0).getResult();
			prmsControl.accNum = String.valueOf(prmsControl.accMessage
					.get(Prms.QUERY_PRMSACC_ACCOUNT));
			prmsControl.accId = String.valueOf(prmsControl.accMessage
					.get(Prms.QUERY_PRMSACC_ACCOUNTID));
			prmsControl.ifhavPrmsAcc = true;
		}
		if (!prmsControl.ifhavPrmsAcc || !prmsControl.ifInvestMent) {
			BaseHttpEngine.dissMissProgressDialog();
			getPopup();
		} else {
			requestSystemDateTime();
		}
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		init();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		BaseDroidApp.getInstanse().setCurrentAct(this);
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ConstantGloble.ACTIVITY_REQUEST_SETPRMSACC_CODE:// 设置贵金属账户
			switch (resultCode) {
			case RESULT_OK:
				prmsControl.ifhavPrmsAcc = true;
				BaseDroidApp.getInstanse().dismissMessageDialog();
				BaseHttpEngine.showProgressDialogCanGoBack();
				requestSystemDateTime();
				break;
			default:
				prmsControl.ifhavPrmsAcc = false;
				getPopup();
				break;
			}
			break;
		case ConstantGloble.ACTIVITY_REQUEST_MANAGE_CODE:// 开通理财服务
			switch (resultCode) {
			case RESULT_OK:
				prmsControl.ifInvestMent = true;
				if (prmsControl.ifhavPrmsAcc) {
					BaseDroidApp.getInstanse().dismissMessageDialog();
					BaseHttpEngine.showProgressDialogCanGoBack();
					requestSystemDateTime();
				} else {
					getPopup();
				}
				break;
			default:
				prmsControl.ifInvestMent = false;
				getPopup();
				break;
			}
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void queryPrmsTradeDealeCallBack(Object resultObj) {
		super.queryPrmsTradeDealeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		queryTimeTextView.setText(startDateStr + "-" + endDateStr);
		if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
			return;
		}
		Map<String, Object> data = (Map<String, Object>) (biiResponseBody
				.getResult());
		List<Map<String, Object>> tempList = (List<Map<String, Object>>) data
				.get(Prms.PRMS_QUERY_DEAL_LIST);
		if (StringUtil.isNullOrEmpty(tempList) && isNewQuery) {// 没有查询到数据
			return;
		}
		//wuhan
		queryBeforLayout.setVisibility(View.GONE);

		query_result_condition_layout.setVisibility(View.VISIBLE);
		
		
		query_resultLayout.setVisibility(View.VISIBLE);
		queryListView.setVisibility(View.VISIBLE);
		
		totalNum = Integer.valueOf((String) data
				.get(Prms.PRMS_QUERY_DEAL_RECORDNUMBER));
		currentIndex += 10;
		if (!isNewQuery && resultList != null && adatper != null) {
			resultList.addAll(tempList);
			adatper.notifyDataSetChanged(resultList);
			// if(pageSize*currentIndex>=totalNum){//全部显示完鸟
			if (resultList.size() >= totalNum) {// 全部显示完鸟\
				removeFooterView(queryListView);
			}
		} else {
			resultList = (tempList);
			adatper = new PrmsQueryDealListAdapter(this, resultList);
			if (totalNum > 10) {// 不能显示完
				addfooteeView(queryListView);
			} else {
				removeFooterView(queryListView);
			}
			queryListView.setAdapter(adatper);
			queryBeforLayout.setBackgroundResource(R.drawable.img_bg_query_no);
//			queryBeforLayout.startAnimation(animation_up);sss
			queryBeforLayout.setVisibility(View.GONE);
			query_result_condition_layout.setVisibility(View.VISIBLE);
			query_resultLayout.setVisibility(View.VISIBLE);
			queryListView.setVisibility(View.VISIBLE);
		}

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
				queryPrmsTradeDeale(startDateStr, endDateStr,
						String.valueOf(currentIndex), String.valueOf(pageSize),
						null, isNewQuery);
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
				R.string.prms_title_query_deal_new);
		setTitle(title);
		View view = mainInflater.inflate(R.layout.prms_query_deal_main, null);
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

		query_result_condition_layout = findViewById(R.id.query_result_condition_layout);
		
		query_resultLayout = (LinearLayout) findViewById(R.id.query_reult_layout);
		queryTimeTextView = (TextView) findViewById(R.id.prms_query_result_time);
		queryBeforLayoutOut = (LinearLayout) findViewById(R.id.prms_query_deal_befor_layout);
		queryBeforLayoutOut.setVisibility(View.VISIBLE);
		query_resultLayout.setVisibility(View.GONE);
//		initanimation();
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
		currentIndex = 0;
		isNewQuery = true;
		if (PrmsControl.is401) {
			right.setVisibility(View.VISIBLE);
		} else {
			right.setVisibility(View.GONE);
		}
		queryBeforLayoutOut.bringToFront();
		initListHeaderView(tabcontent, R.string.prms_buycurrency_no,
				R.string.prms_salecurrency_no, R.string.prms_cjprice_no);
		initRightBtnForMain();
		
		//wuhan
		queryBeforLayout.setVisibility(View.VISIBLE);
		query_result_condition_layout.setVisibility(View.GONE);
		query_resultLayout.setVisibility(View.GONE);
		queryListView.setVisibility(View.GONE);
//		requestCommConversationId();
//		BaseHttpEngine.showProgressDialog();
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		queryPrmsTradeDeale(startDateStr, endDateStr,
				String.valueOf(currentIndex), String.valueOf(pageSize), null,
				isNewQuery);
	}

	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		// 随机数获取异常
		if (Prms.PRMS_QUERY_DEAL.equals(((BiiResponse) resultObj).getResponse()
				.get(0).getMethod())) {
			if (((BiiResponse) resultObj).isBiiexception()) {// 代表返回数据异常
				BaseHttpEngine.dissMissProgressDialog();

			}
		}
		return super.httpRequestCallBackPre(resultObj);
	}



	@Override
	public void onClick(View v) {
		super.onClick(v);
		Calendar c;
		DatePickerDialog dialog;
		switch (v.getId()) {
		case R.id.prms_up_imageView:// up button to show the query view
		case R.id.prms_query_deal_main_uplayout:
//			if (!StringUtil.isNullOrEmpty(resultList)) {// 如果还未查询// 收起查询页面
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
			cleanList();
			currentIndex = 0;
			isNewQuery = true;
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();
			break;
		case R.id.prms_queryquery_deal_queryoneweek:// 最近一周
			endDateStr = QueryDateUtils.getcurrentDate(currenttime);
			startDateStr = QueryDateUtils.getlastWeek(currenttime);
			currentIndex = 0;
			isNewQuery = true;
			cleanList();
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();
			break;
		case R.id.prms_queryquery_deal_queryonemouth:// 最近一个月
			endDateStr = QueryDateUtils.getcurrentDate(currenttime);
			startDateStr = QueryDateUtils.getlastOneMonth(currenttime);
			currentIndex = 0;
			isNewQuery = true;
			cleanList();
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();
			break;
		case R.id.prms_queryquery_deal_querythreemouths:// 最近三个月
			endDateStr = QueryDateUtils.getcurrentDate(currenttime);
			startDateStr = QueryDateUtils.getlastThreeMonth(currenttime);
			currentIndex = 0;
			isNewQuery = true;
			cleanList();
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();
			break;
		case R.id.prms_query_deal_startdate: // 设定开始日期
			c = QueryDateUtils.getCalendarWithDate(startDateTv.getText().toString());
			dialog = new DatePickerDialog(PrmsQueryDealActivity.this,
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

//	@Override
//	protected void onResume() {
//		super.onResume();
//		setLeftSelectedPosition("prmsManage_3");
//	}

	protected void initListHeaderView(View parentView, int text1ResID,
			int textRes2Id, int textRes3Id) {
		TextView headerView1 = (TextView) parentView
				.findViewById(R.id.list_header_tv1);
		TextView headerView2 = (TextView) parentView
				.findViewById(R.id.list_header_tv2);
		TextView headerView3 = (TextView) parentView
				.findViewById(R.id.list_header_tv3);
		headerView1.setText(text1ResID);
		headerView2.setText(textRes2Id);
		headerView3.setText(textRes3Id);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				headerView1);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				headerView2);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				headerView3);
	}

	private void cleanList() {
		resultList = null;
		queryListView.setVisibility(View.GONE);
		query_resultLayout.setVisibility(View.GONE);
	}
}
