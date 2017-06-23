package com.chinamworld.bocmbci.biz.epay.transquery;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Third;
import com.chinamworld.bocmbci.biz.BusinessModelControl;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.epay.EPayBaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.constants.TQConstants;
import com.chinamworld.bocmbci.biz.epay.constants.TQConstants.PaymentType;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.observer.PubHttpObserver;
import com.chinamworld.bocmbci.biz.epay.transquery.adapter.ResultListAdapter;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.SearchView;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.LoadMoreListView;
import com.chinamworld.bocmbci.widget.LoadMoreListView.OnLoadMoreListener;
import com.chinamworld.bocmbci.widget.LoadMoreListView.Status;

/**
 * 
 * @ClassName: TransQueryActivity
 * @Description: 支付交易查询-查询页面
 * @author luql
 * @date 2013-9-15 上午10:15:00
 * 
 */
public class TransQueryActivity extends EPayBaseActivity implements
		OnClickListener {
	private static final String TAG = TransQueryActivity.class.getSimpleName();
	private static final int REQUEST_ACCNUMBER_CODE = 100;
	/** 查询结果页面 */
	private View mContentLayout;
	
	private View rl_result;
	
	/** 查询卡号展示 */
	private TextView mAccNumberView;
	/** 支付类型展示 */
	private TextView mPaymentTypeView;
	/** 支付方式展示 */
	private TextView mPaymentWayView;
	/** 查询时间展示 */
	private TextView mTransDateView;
	/** 加载更多ListVIew */
	private LoadMoreListView mLoadMoreListView;

	/** 提示信息 */
	private View mPromptView;

	// //PopWindow
//	private PopupWindowUtils mPopupWindowUtils;
	/** 查询PopView */
	private View mQueryPopupView;
	/** 支付类型选择 */
	private Spinner mPaymentTypeSpinnerView;
	/** 支付账户选择 */
	private Spinner mPaymentAccountSpinnerView;
	/** 支付方式选择 */
	private Spinner mPaymentWaySpinnerView;
	/** 开始时间 */
	private TextView mStartDatePopView;
	/** 结束时间 */
	private TextView mEndDatePopView;

	private Context tqTransContext;
	private PubHttpObserver httpObserver;
	private int mPaymentWaySpinnerLastPosition = -1;

	// /结果
	/** 查询结果 */
	private List<Object> mRestListData;
	/** 分页当前页 */
	private int mCurrentIndex;
	/** 返回最后一次总条数 */
	private Integer mQueryRecordNumber;
	private ResultListAdapter resultListAdapter;

	// /** 查询账户类型下表 */
	// private int mPaymentTypeNameIndexParams;
	/** 查询支付方式参数 */
	private String mPaymentTypeQuery;
	/** 查询开始时间参数 */
	private String mStartDateParam;
	/** 查询结束时间参数 */
	private String mEndDateParam;
	/** 查询支付类型参数 */
	private String mPaymentWayParam;
	/** 查询支付账户参数 */
	private String mPaymentAccountParam;
	private String mPaymentAccountNum;

	/** 中银查询账户Id参数 */
	private String mZyAccountIdParam;
	/** 中银查询账户参数 */
	private String zyAccountNumParam;
	// /** 中银查询账户类型参数 */
	// private String zyAccountTypeParam;
	/** 中银快付支付账户标识 */
	private ArrayList<Object> mZyAccountTypes;
	/** 协议支付交易记录参数 */
	private HashMap<Object, Object> treatyParams;
	private String[] paymentTypeNames = TQConstants.PaymentType
			.getPaymentTypeList();
	// private int[] paymentTypeValues = new int[] {
	// TQConstants.PAYMENT_TYPE_BOM, TQConstants.PAYMENT_TYPE_ZY_QUCIK,
	// TQConstants.PAYMENT_TYPE_TREATY_QUCIK };

	private String[] paymentWayNames = new String[] { "全部", "一次性全额支付", "分期支付" };
	private String[] paymentWayValues = new String[] {
			TQConstants.PAYMENT_WAY_ALL, TQConstants.PAYMENT_WAY_CLEAR,
			TQConstants.PAYMENT_WAY_BY_STAGES };
	private List<Object> accountList;

	/** 支付交易记录参数 */
	private HashMap<Object, Object> paymentParams;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		tqTransContext = TransContext.getTQTransContext();
		httpObserver = PubHttpObserver.getInstance(this,
				PubConstants.CONTEXT_TQ);
		View transQuery = LayoutInflater.from(this).inflate(
				R.layout.epay_trans_query, null);
		super.setType(1);
		super.setShowBackBtn(true);
		super.setTitleName(PubConstants.TITLE_TRANS_QUERY);
		super.setContentView(transQuery);
		super.onCreate(savedInstanceState);
		slidingBody.setPadding(0, 0, 0, 0);
		findView();
		initData();
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestSystemDateTime();
		Button ib_top_right_btn = (Button) findViewById(R.id.ib_top_right_btn);
		ib_top_right_btn.setVisibility(View.GONE);
	}

	private void initData() {
//		mPopupWindowUtils = PopupWindowUtils.getInstance();
		mRestListData = new ArrayList<Object>();
		mZyAccountTypes = new ArrayList<Object>();
		mZyAccountTypes.add("119");
		mZyAccountTypes.add("103");
		mZyAccountTypes.add("104");
		mZyAccountTypes.add("107");
		mZyAccountTypes.add("108");
		mZyAccountTypes.add("109");
		mZyAccountTypes.add("110");
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		if (StringUtil.isNull(dateTime)) {
			return;
		}
		// BaseHttpEngine.dissMissProgressDialog();
		showBocAllAccount();
		// initQueryPopupView();
		// mPopupWindowUtils.showQueryPopupWindowFirst();
	}

	private void initQueryPopupView() {
		mQueryPopupView = View.inflate(this, R.layout.epay_tq_condition_bar,
				null);
		LinearLayout ll_result=(LinearLayout)findViewById(R.id.ll_result);
//		mPopupWindowUtils.getQueryPopupWindow(mQueryPopupView, this);
		SearchView.getInstance().addSearchView(ll_result, mQueryPopupView);
		mPaymentTypeSpinnerView = (Spinner) mQueryPopupView
				.findViewById(R.id.s_payment_type);
		mPaymentAccountSpinnerView = (Spinner) mQueryPopupView
				.findViewById(R.id.s_payment_account);
		mPaymentWaySpinnerView = (Spinner) mQueryPopupView
				.findViewById(R.id.s_payment_way);
		mStartDatePopView = (TextView) mQueryPopupView
				.findViewById(R.id.tv_start_date);
		mEndDatePopView = (TextView) mQueryPopupView
				.findViewById(R.id.tv_end_date);

		// 账户支持类型
		ArrayAdapter<String> paymentTypeAdapter = new ArrayAdapter<String>(
				getActivity(), R.layout.custom_spinner_item, paymentTypeNames);
		paymentTypeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mPaymentTypeSpinnerView.setAdapter(paymentTypeAdapter);
		mPaymentTypeSpinnerView.setOnItemSelectedListener(spSelectedClick);
		mPaymentTypeSpinnerView.setSelection(0);

		// 支付账户选择
		List<String> payAccountList = new ArrayList<String>();
		payAccountList.add("全部");
		for (int i = 0; i < accountList.size(); i++) {
			Map<Object, Object> tempMap = EpayUtil.getMap(accountList.get(i));
			String accType = EpayUtil
					.getString(
							tempMap.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_TYPE),
							"");
			String accountType = LocalData.AccountType.get(accType);
			String accNum = StringUtil
					.getForSixForString(EpayUtil.getString(
							tempMap.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NUMBER),
							""));
			payAccountList.add(accountType + " " + accNum);
		}

		ArrayAdapter<String> paymentAccountAdapter = new ArrayAdapter<String>(
				getActivity(), R.layout.custom_spinner_item, payAccountList);
		paymentAccountAdapter
				.setDropDownViewResource(R.layout.epay_spinner_list_item);
		mPaymentAccountSpinnerView.setAdapter(paymentAccountAdapter);
		mPaymentAccountSpinnerView.setOnItemSelectedListener(spSelectedClick);
		mPaymentAccountSpinnerView.setSelection(0);

		// 初始自定义时间
		mStartDatePopView.setText(QueryDateUtils.getlastthreeDate(dateTime));
		mEndDatePopView.setText(QueryDateUtils.getcurrentDate(dateTime));

		mStartDatePopView.setOnClickListener(chooseDateClick);
		mEndDatePopView.setOnClickListener(chooseDateClick);
		// 查询
		mQueryPopupView.findViewById(R.id.bt_query).setOnClickListener(this);
		// 收起
		mQueryPopupView.findViewById(R.id.rl_slip_up).setOnClickListener(this);
		// 时间
		mQueryPopupView.findViewById(R.id.bt_week).setOnClickListener(this);
		mQueryPopupView.findViewById(R.id.bt_month).setOnClickListener(this);
		mQueryPopupView.findViewById(R.id.bt_three_month).setOnClickListener(
				this);
		// 显示提示信息
		showPrompt(mQueryPopupView);
	}

	/** sp点击事件 **/
	private OnItemSelectedListener spSelectedClick = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {

			if (parent.getId() == R.id.s_payment_type) {
				// String paymentType = paymentTypeNames[position];
				// if
				// (PaymentType.PAYMENT_TYPE_TREATY_QUCIK.equals(paymentType)) {
				// // 选择协议支付
				// mPaymentWaySpinnerView.setClickable(false);
				// mPaymentWaySpinnerView.setBackgroundResource(R.drawable.bg_spinner_default);
				// ArrayAdapter<String> paymentWayAdapter = new
				// ArrayAdapter<String>(TransQueryActivity.this,
				// R.layout.custom_spinner_item, new String[] { "-" });
				// paymentWayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				// mPaymentWaySpinnerView.setAdapter(paymentWayAdapter);
				// } else {
				// mPaymentWaySpinnerView.setClickable(true);
				// mPaymentWaySpinnerView.setBackgroundResource(R.drawable.bg_spinner);
				// ArrayAdapter<String> paymentWayAdapter = new
				// ArrayAdapter<String>(TransQueryActivity.this,
				// R.layout.custom_spinner_item, paymentWayNames);
				// paymentWayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				// mPaymentWaySpinnerView.setAdapter(paymentWayAdapter);
				// }
				//
				// if (PaymentType.PAYMENT_TYPE_ZY_QUCIK.equals(paymentType)) {
				// showBocAllAccount();
				// } else {
				// // 记录支付方式下标
				// mPaymentWaySpinnerLastPosition = position;
				// }
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}

	};

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.bt_query:
			// Pop 查询
			if (checkSubmit()) {
				String startDate = mStartDatePopView.getText().toString();
				String endDate = mEndDatePopView.getText().toString();
				if (checkSubmitData(dateTime, startDate, endDate)) {
					mStartDateParam = startDate;
					mEndDateParam = endDate;
					mPaymentTypeQuery = getCurrentPaymentTypeParam();
					mPaymentWayParam = getCurrentPaymentWayParam();
					mPaymentAccountParam = getCurrentPaymentAccountParam();
					queryTransRecordList(true);
				}
			}
			break;
		case R.id.rl_slip_up:
			// Pop收起
			if (mContentLayout.isEnabled()) {
//				mPopupWindowUtils.dissMissQueryPopupWindow();
				SearchView.getInstance().hideSearchView();
			}
			break;
		case R.id.bt_week:
			// Pop一周查询
			if (checkSubmit()) {
				mStartDateParam = QueryDateUtils.getlastWeek(dateTime);
				mEndDateParam = QueryDateUtils.getcurrentDate(dateTime).trim();
				mPaymentWayParam = getCurrentPaymentWayParam();
				mPaymentTypeQuery = getCurrentPaymentTypeParam();
				mPaymentAccountParam = getCurrentPaymentAccountParam();
				queryTransRecordList(true);
			}
			break;
		case R.id.bt_month:
			// Pop一个月查询
			if (checkSubmit()) {
				mStartDateParam = QueryDateUtils.getlastOneMonth(dateTime);
				mEndDateParam = QueryDateUtils.getcurrentDate(dateTime).trim();
				mPaymentTypeQuery = getCurrentPaymentTypeParam();
				mPaymentWayParam = getCurrentPaymentWayParam();
				mPaymentAccountParam = getCurrentPaymentAccountParam();
				queryTransRecordList(true);
			}
			break;
		case R.id.bt_three_month:
			// Pop三个月查询
			if (checkSubmit()) {
				mStartDateParam = QueryDateUtils.getlastThreeMonth(dateTime);
				mEndDateParam = QueryDateUtils.getcurrentDate(dateTime).trim();
				mPaymentTypeQuery = getCurrentPaymentTypeParam();
				mPaymentWayParam = getCurrentPaymentWayParam();
				mPaymentAccountParam = getCurrentPaymentAccountParam();
				queryTransRecordList(true);
			}
			break;
		case R.id.slip_down:
			// 下拉
			if (mQueryPopupView != null) {
//				mPopupWindowUtils.getQueryPopupWindow(mQueryPopupView, this);
//				mPopupWindowUtils.showQueryPopupWindow();
//				
				SearchView.getInstance().showSearchView();
			}
			break;

		default:
			break;
		}

	}

	/** 设置日期 */
	private OnClickListener chooseDateClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			final TextView tv = (TextView) v;
			String time = tv.getText().toString();
			Date date = null;
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
				date = formatter.parse(time);
			} catch (ParseException e) {
				LogGloble.e(TAG, e.getMessage(), e);
				date = new Date();
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			DatePickerDialog dialog = new DatePickerDialog(
					getActivity(),
					new OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							StringBuilder dateStr = new StringBuilder();
							dateStr.append(String.valueOf(year));
							dateStr.append("/");
							int month = monthOfYear + 1;
							dateStr.append(((month < 10) ? ("0" + month)
									: String.valueOf(month)));
							dateStr.append("/");
							dateStr.append(((dayOfMonth < 10) ? ("0" + dayOfMonth)
									: String.valueOf(dayOfMonth)));
							// 为日期赋值
							tv.setText(String.valueOf(dateStr));
						}
					}, calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
			dialog.show();
		}

	};

	/**
	 * 校验交易参数
	 */
	private boolean checkSubmitData(String dateTime, String startDate,
			String endDate) {
		boolean success = true;

		Date ly = null;
		Date sd = null;
		Date ed = null;
		Date curSystemDate = null;
		try {
			// 上一年
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			curSystemDate = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
					.parse(dateTime);
			ed = dateFormat.parse(endDate);
			sd = dateFormat.parse(startDate);
			String lastYearStr = EpayUtil.getDateStr(curSystemDate, -1, 0, 0);
			ly = dateFormat.parse(lastYearStr);

			if (sd.after(curSystemDate)) {
				// BaseDroidApp.getInstanse().showInfoMessageDialog("起始日期超过当前日期，请重新选择起始日期！");
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getText(R.string.acc_query_errordate)
								.toString());
				success = false;
			} else if (ed.after(curSystemDate)) {
				// BaseDroidApp.getInstanse().showInfoMessageDialog("结束日期超过当前日期，请重新选择结束日期！");
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getText(R.string.acc_check_enddate).toString());
				success = false;
			} else if (sd.after(ed)) {
				// BaseDroidApp.getInstanse().showInfoMessageDialog("起始日期超过结束日期，请重新选择查询日期！");
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getText(R.string.acc_query_errordate).toString());
				success = false;
			} else if (sd.before(ly)) {
				// BaseDroidApp.getInstanse().showInfoMessageDialog("起始日期超过查询范围，交易查询只能查询一年之内的交易记录，请重新选择起始日期！");
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getText(R.string.starttimedate_endtimedate_one_year)
								.toString());
				success = false;
			} else if (!QueryDateUtils.compareDateThree(startDate, endDate)) {
				// 起始日期与结束日期最大间隔为三个自然月
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getText(R.string.acc_check_start_end_date).toString());
				success = false;
			}
		} catch (ParseException e) {
			success = false;
		}

		return success;
	}

	/**
	 * 查询交易记录列表
	 * 
	 * @param index
	 */
	private void queryTransRecordList(boolean isRefresh) {
		if (isRefresh) {
			// 刷新->点击查询按钮触发，如果重新选择支付方式不是中银mZyAccountIdParam清空
			// int paymentTypePosition = mPaymentTypeSpinnerView
			// .getSelectedItemPosition();
			// String paymentType = paymentTypeNames[paymentTypePosition];
			// if (!PaymentType.PAYMENT_TYPE_ZY_QUCIK.equals(paymentType)) {
			// mZyAccountIdParam = "";
			// }
			refreshResultListView(null);
		} else {
		}

		// if (PaymentType.PAYMENT_TYPE_BOM.equals(mPaymentTypeQuery)) {
		// // 电子支付查询
		// req_queryBOMTransRecord(isRefresh);
		// } else if (PaymentType.PAYMENT_TYPE_TREATY_QUCIK
		// .equals(mPaymentTypeQuery)) {
		// // 协议支付查询
		// req_queryTreatyTransRecord(isRefresh);
		// } else if
		// (PaymentType.PAYMENT_TYPE_ZY_QUCIK.equals(mPaymentTypeQuery)) {
		// // 中银快付查询
		// req_queryZYTransRecord(isRefresh);
		// }
		req_queryEpayQueryAllTypeRecord(isRefresh);
	}

	/**
	 * 查询电子支付 交易记录
	 * 
	 * @param index
	 * @param isRefresh
	 */
	private void req_queryBOMTransRecord(boolean isRefresh) {
		if (isRefresh) {
			mCurrentIndex = 0;
		} else {
			mCurrentIndex += Third.PAGESIZE;
		}
		BiiHttpEngine.showProgressDialog();
		HashMap<Object, Object> bomParams = new HashMap<Object, Object>();
		bomParams.put(PubConstants.PUB_QUERY_FEILD_PAGE_SIZE,
				String.valueOf(Third.PAGESIZE));
		bomParams.put(TQConstants.PUB_QUERY_FEILD_START_DATE, mStartDateParam);
		bomParams.put(TQConstants.PUB_QUERY_FEILD_END_DATE, mEndDateParam);
		// bomParams.put(TQConstants.PUB_QUERY_FEILD_IS_REFRESH, isRefresh);
		bomParams.put(PubConstants.PUB_QUERY_FEILD_CURRENT_INDEX,
				EpayUtil.getString(mCurrentIndex, "0"));
		bomParams.put(TQConstants.PUB_QUERY_FEILD_INSTALMENT_PLAN,
				mPaymentWayParam);
		httpObserver.req_queryBOMTransRecord(bomParams,
				"queryBOMTransRecordCallback");
	}

	/**
	 * 查询协议支付交易记录
	 * 
	 * @param index
	 */
	private void req_queryTreatyTransRecord(boolean isRefresh) {
		if (isRefresh) {
			mCurrentIndex = 0;
		} else {
			mCurrentIndex += Third.PAGESIZE;
		}
		BiiHttpEngine.showProgressDialog();
		treatyParams = new HashMap<Object, Object>();
		treatyParams.put(PubConstants.PUB_QUERY_FEILD_PAGE_SIZE,
				String.valueOf(Third.PAGESIZE));
		treatyParams.put(TQConstants.PUB_QUERY_FEILD_START_DATE,
				mStartDateParam);
		treatyParams.put(TQConstants.PUB_QUERY_FEILD_END_DATE, mEndDateParam);
		treatyParams.put(PubConstants.PUB_QUERY_FEILD_CURRENT_INDEX,
				EpayUtil.getString(mCurrentIndex, "0"));
		treatyParams.put(PubConstants.PUB_QUERY_FEILD_IS_REFRESH,
				EpayUtil.getString(isRefresh, "true"));
		httpObserver.req_getConversationId("requestCommConversationIdCallBack");
	}

	/**
	 * 查询电子支付交易记录
	 * 
	 * @param index
	 */
	private void req_queryEpayQueryAllTypeRecord(boolean isRefresh) {
		if (isRefresh) {
			mCurrentIndex = 0;
		} else {
			mCurrentIndex += Third.PAGESIZE;
		}
		BiiHttpEngine.showProgressDialog();
		paymentParams = new HashMap<Object, Object>();
		// 支付方式
		if (mPaymentTypeQuery.equals(PaymentType.PAYMENT_ALL)) {
			// 全部
			paymentParams.put(PubConstants.PUB_QUERY_PAY_METHODS, new String[] {
					"1", "2", "4" });
		} else if (mPaymentTypeQuery.equals(PaymentType.PAYMENT_TYPE_BOM)) {
			paymentParams.put(PubConstants.PUB_QUERY_PAY_METHODS,
					new String[] { "1" });
		} else if (mPaymentTypeQuery.equals(PaymentType.PAYMENT_TYPE_ZY_QUCIK)) {
			paymentParams.put(PubConstants.PUB_QUERY_PAY_METHODS,
					new String[] { "2" });
		} else if (mPaymentTypeQuery
				.equals(PaymentType.PAYMENT_TYPE_TREATY_QUCIK)) {
			paymentParams.put(PubConstants.PUB_QUERY_PAY_METHODS,
					new String[] { "4" });
		}
		// 查询账户
		if (mPaymentAccountParam.equals("全部")) {
			String[] idList = new String[accountList.size()];
			for (int i = 0; i < accountList.size(); i++) {
				Map<Object, Object> account = EpayUtil.getMap(accountList
						.get(i));
				idList[i] = EpayUtil
						.getString(
								account.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_ID),
								"");
			}
			paymentParams.put(PubConstants.PUB_QUERY_ACCOUNT_IDS, idList);
		} else {
			paymentParams.put(PubConstants.PUB_QUERY_ACCOUNT_IDS,
					new String[] { mPaymentAccountParam });
		}
		paymentParams.put(PubConstants.PUB_QUERY_FEILD_PAGE_SIZE,
				String.valueOf(Third.PAGESIZE));
		paymentParams.put(TQConstants.PUB_QUERY_FEILD_START_DATE,
				mStartDateParam);
		paymentParams.put(TQConstants.PUB_QUERY_FEILD_END_DATE, mEndDateParam);
		paymentParams.put(PubConstants.PUB_QUERY_FEILD_CURRENT_INDEX,
				EpayUtil.getString(mCurrentIndex, "0"));
		paymentParams.put(PubConstants.PUB_QUERY_FEILD_IS_REFRESH,
				EpayUtil.getString(isRefresh, "true"));
		httpObserver.req_getConversationId("requestCommConversationIdCallBack");
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		String conversationId = EpayUtil.getString(
				httpObserver.getResult(resultObj), "");
		httpObserver.setConversationId(conversationId);
		httpObserver.req_queryEpayQueryAllTypeRecord(paymentParams,
				"queryEpayQueryAllTypeRecordCallback");
	}

	/**
	 * 查询中银快付交易记录
	 * 
	 * @param accountId
	 * @param index
	 * @param isRefresh
	 */
	public void req_queryZYTransRecord(boolean isRefresh) {
		if (isRefresh) {
			mCurrentIndex = 0;
		} else {
			mCurrentIndex += Third.PAGESIZE;
		}
		BiiHttpEngine.showProgressDialog();
		HashMap<Object, Object> zyParams = new HashMap<Object, Object>();
		zyParams.put(PubConstants.PUB_QUERY_FEILD_PAGE_SIZE,
				String.valueOf(Third.PAGESIZE));
		zyParams.put(TQConstants.PUB_QUERY_FEILD_ACCOUNT_ID, mZyAccountIdParam);
		zyParams.put(TQConstants.PUB_QUERY_FEILD_START_DATE, mStartDateParam);
		zyParams.put(TQConstants.PUB_QUERY_FEILD_END_DATE, mEndDateParam);
		zyParams.put(PubConstants.PUB_QUERY_FEILD_CURRENT_INDEX,
				EpayUtil.getString(mCurrentIndex, "0"));
		// if(!"119".equals(accountType))
		zyParams.put(TQConstants.PUB_QUERY_FEILD_INSTALMENT_PLAN,
				mPaymentWayParam);
		httpObserver.req_queryZYTransRecord(zyParams,
				"queryZYTransRecordCallback");
	}

	// ---------------------------------------------------------------------------------
	// callback

	/**
	 * 支付交易记录
	 * 
	 * @param resultObj
	 */
	public void queryEpayQueryAllTypeRecordCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		Object result = httpObserver.getResult(resultObj);
		Map<Object, Object> resultMap = EpayUtil.getMap(result);
		mQueryRecordNumber = Integer
				.valueOf(EpayUtil.getString(
						resultMap
								.get(TQConstants.METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_RECORD_NUMBER),
						""));
		List<Object> resultList = EpayUtil.getList(resultMap
				.get(TQConstants.METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_LIST));
		if (resultList == null) {
			return;
		}
		mRestListData.addAll(resultList);
		refreshResultListView(mPaymentTypeQuery);
	}

	/**
	 * 查询电子支付交易记录
	 * 
	 * @param resultObj
	 */
	public void queryBOMTransRecordCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		Object result = httpObserver.getResult(resultObj);
		Map<Object, Object> resultMap = EpayUtil.getMap(result);
		mQueryRecordNumber = Integer
				.valueOf(EpayUtil.getString(
						resultMap
								.get(TQConstants.METHOD_QUERY_BOM_TRANS_RECORD_FEILD_RECORD_NUMBER),
						""));
		List<Object> bomResultList = EpayUtil.getList(resultMap
				.get(TQConstants.METHOD_QUERY_BOM_TRANS_RECORD_FEILD_LIST));
		if (bomResultList == null) {
			return;
		}
		mRestListData.addAll(bomResultList);
		refreshResultListView(PaymentType.PAYMENT_TYPE_BOM);
	}

	/**
	 * 查询协议支付交易记录
	 * 
	 * @param resultObj
	 */
	public void queryTreatyTransRecordCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		Object result = httpObserver.getResult(resultObj);
		Map<Object, Object> resultMap = EpayUtil.getMap(result);
		mQueryRecordNumber = Integer
				.valueOf(EpayUtil.getString(
						resultMap
								.get(TQConstants.METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_RECORD_NUMBER),
						""));
		List<Object> treatyResultList = EpayUtil.getList(resultMap
				.get(TQConstants.METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_LIST));
		if (treatyResultList == null) {
			return;
		}

		mRestListData.addAll(treatyResultList);
		refreshResultListView(PaymentType.PAYMENT_TYPE_TREATY_QUCIK);
	}

	/**
	 * 查询中银支付交易记录回调方法
	 * 
	 * @param resultObj
	 */
	public void queryZYTransRecordCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		Object result = httpObserver.getResult(resultObj);
		Map<Object, Object> resultMap = EpayUtil.getMap(result);
		mQueryRecordNumber = Integer
				.valueOf(EpayUtil.getString(
						resultMap
								.get(TQConstants.METHOD_QUERY_ZY_TRANS_RECORD_FEILD_RECORD_NUMBER),
						""));

		List<Object> zyResultList = EpayUtil.getList(resultMap
				.get(TQConstants.METHOD_QUERY_ZY_TRANS_RECORD_FEILD_LIST));
		if (zyResultList == null) {
			return;
		}
		mRestListData.addAll(zyResultList);
		refreshResultListView(PaymentType.PAYMENT_TYPE_ZY_QUCIK);
	}

	/**
	 * 设置查询结果列表
	 */
	public void refreshResultListView(final String paymentType) {

		if (paymentType == null) {
			// 刷新，重新查询
			mRestListData.clear();
			mQueryRecordNumber = -1;
			mContentLayout.setEnabled(false);
			// mContentLayout.setVisibility(View.VISIBLE);
			// mPromptView.setVisibility(View.GONE);
		} else {
			if (mRestListData.isEmpty()) {
				// 第一次查询结果为空不做任何事情
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getString(R.string.blpt_common_error));
				if (mPromptView.getVisibility() == View.VISIBLE) {
					return;
				}
			} else {
//				mPopupWindowUtils.dissMissQueryPopupWindow();
				SearchView.getInstance().hideSearchView();
				
			}
			// 设置查询日期返显数据
			mContentLayout.setEnabled(true);
			mContentLayout.setVisibility(View.VISIBLE);
			rl_result.setVisibility(View.VISIBLE);
			mPromptView.setVisibility(View.GONE);

			mTransDateView.setText(new StringBuffer(mStartDateParam)
					.append("-").append(mEndDateParam));

			// for (int i = 0; i < paymentWayValues.length; i++) {
			// if (paymentWayValues[i].equals(mPaymentWayParam)) {
			// // 设置支付方式返显数据
			// mPaymentWayView.setText(paymentWayNames[i]);
			// break;
			// }
			// }

			// if (PaymentType.PAYMENT_TYPE_BOM.equals(paymentType)) {
			// mPaymentTypeView.setText(paymentType);
			// findViewById(R.id.ll_zy_account).setVisibility(View.GONE);
			// } else if (PaymentType.PAYMENT_TYPE_TREATY_QUCIK
			// .equals(paymentType)) {
			// // 协议支付查询
			// mPaymentTypeView.setText(paymentType);
			// mPaymentWayView.setText("-");
			// findViewById(R.id.ll_zy_account).setVisibility(View.GONE);
			// } else if (PaymentType.PAYMENT_TYPE_ZY_QUCIK.equals(paymentType))
			// {
			// // 中银快付查询
			// mPaymentTypeView.setText(paymentType);
			// mAccNumberView.setText(StringUtil
			// .getForSixForString(this.zyAccountNumParam));
			// findViewById(R.id.ll_zy_account).setVisibility(View.VISIBLE);
			// } else if (PaymentType.PAYMENT_ALL.equals(paymentType)) {
			// // 全部查询
			// mPaymentTypeView.setText(paymentType);
			// mAccNumberView.setText(StringUtil
			// .getForSixForString(this.zyAccountNumParam));
			// findViewById(R.id.ll_zy_account).setVisibility(View.VISIBLE);
			// }
			// // 支付方式
			// if (mPaymentTypeQuery.equals(PaymentType.PAYMENT_ALL)) {
			// // 全部
			// paymentParams.put(PubConstants.PUB_QUERY_PAY_METHODS, new
			// String[] {
			// "1", "2", "4" });
			// } else if
			// (mPaymentTypeQuery.equals(PaymentType.PAYMENT_TYPE_BOM)) {
			// paymentParams.put(PubConstants.PUB_QUERY_PAY_METHODS,
			// new String[] { "1" });
			// } else if
			// (mPaymentTypeQuery.equals(PaymentType.PAYMENT_TYPE_ZY_QUCIK)) {
			// paymentParams.put(PubConstants.PUB_QUERY_PAY_METHODS,
			// new String[] { "2" });
			// } else if (mPaymentTypeQuery
			// .equals(PaymentType.PAYMENT_TYPE_TREATY_QUCIK)) {
			// paymentParams.put(PubConstants.PUB_QUERY_PAY_METHODS,
			// new String[] { "4" });
			// }

			mPaymentWayView.setText(mPaymentTypeQuery);
			mAccNumberView
					.setText(mPaymentAccountParam.equals("全部") ? mPaymentAccountParam
							: StringUtil
									.getForSixForString(mPaymentAccountNum));
			findViewById(R.id.ll_zy_account).setVisibility(View.VISIBLE);
		}

		if (mQueryRecordNumber > mRestListData.size()) {
			mLoadMoreListView.showLoadMoreView();
		} else {
			mLoadMoreListView.hideLoadMoreView();
		}

		if (resultListAdapter == null) {
			resultListAdapter = new ResultListAdapter(this, mRestListData,
					paymentType);
			mLoadMoreListView.setAdapter(resultListAdapter);
		} else {
			resultListAdapter.setData(mRestListData, paymentType);
			resultListAdapter.notifyDataSetChanged();
		}

		mLoadMoreListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (paymentType != null) {
					Object item = resultListAdapter.getItem(position);
					if (item != null) {
						tqTransContext.setData(TQConstants.PUB_SELECTED_RESULT,
								item);
						Intent intent = new Intent();
						// if (PaymentType.PAYMENT_TYPE_BOM.equals(paymentType))
						// {
						// intent.setClass(TransQueryActivity.this,
						// TransBomDetailActivity.class);
						// } else if (PaymentType.PAYMENT_TYPE_TREATY_QUCIK
						// .equals(paymentType)) {
						intent.setClass(TransQueryActivity.this,
								TransTreatyDetailActivity.class);
						// } else if (PaymentType.PAYMENT_TYPE_ZY_QUCIK
						// .equals(paymentType)) {
						// intent.setClass(TransQueryActivity.this,
						// TransZYDetailActivity.class);
						// }
						startActivityForResult(intent, 0);
					}
				}
			}
		});
	}

	/**
	 * 获取中银快付查询账户回调方法
	 * 
	 * @param resultObj
	 */
	public void queryZYAccountCallback(Object resultObj) {
		Object result = httpObserver.getResult(resultObj);
		accountList = EpayUtil.getList(result);
		tqTransContext.setData(TQConstants.PUB_ZY_ACCOUNTS, accountList);

		BiiHttpEngine.dissMissProgressDialog();
		if (accountList.isEmpty()) {

			// StringBuffer sb = new
			// StringBuffer("没有可供选择的查询账户!是否立即关联新账户？\n支持中银快付查询功能的卡类型有：");
			StringBuffer sb = new StringBuffer(getText(
					R.string.no_open_electronic_payment_is_correlation)
					.toString());
//			String temp = "";
//			for (Object accTypeStr : mZyAccountTypes) {
//				String accTypeName = LocalData.AccountType.get(accTypeStr);
//				if (!temp.equals(accTypeName)) {
//					temp = accTypeName;
//					sb.append(accTypeName).append("、");
//				}
//			}

			
			BaseDroidApp.getInstanse().createDialog("", sb.toString(), new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					BaseDroidApp.getInstanse().dismissErrorDialog();	
					goRelevanceAccount();
				}
			});
			
//			BaseDroidApp.getInstanse().showErrorDialog(null,
//					/*sb.substring(0, sb.length() - 1),*/
//					sb.toString(),
//					new View.OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							BaseDroidApp.getInstanse().dismissErrorDialog();
//							switch (EpayUtil.getInt(v.getTag(), 0)) {
//							case CustomDialog.TAG_SURE:
//								goRelevanceAccount();
//								break;
//							}
//						}
//					});
			return;
		}

		initQueryPopupView();
//		mPopupWindowUtils.showQueryPopupWindowFirst();
		mContentLayout.setVisibility(View.VISIBLE);
		rl_result.setVisibility(View.GONE);
		SearchView.getInstance().showSearchView();
		// Intent intent = new Intent(this, ZYAccountSelectedActivity.class);
		// this.startActivityForResult(intent, REQUEST_ACCNUMBER_CODE);
		// this.overridePendingTransition(R.anim.n_pop_enter_bottom_up,
		// R.anim.no_animation);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;
		case RESULT_CANCELED:
			break;
		}

		if (requestCode == REQUEST_ACCNUMBER_CODE) {
			// 下拉
			findViewById(R.id.slip_down).performClick();

			if (ZY_QUERY == resultCode) {
				Map<Object, Object> account = tqTransContext
						.getMap(PubConstants.CONTEXT_FIELD_SELECTED_ACC);
				mZyAccountIdParam = EpayUtil
						.getString(
								account.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_ID),
								"");
				zyAccountNumParam = EpayUtil
						.getString(
								account.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NUMBER),
								"");
				// zyAccountTypeParam = EpayUtil.getString(
				// account.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_TYPE),
				// "");
			} else if (101 == resultCode) {
				// 点击关闭返回，显示原来的支付类型
				if (mPaymentWaySpinnerLastPosition != -1) {
					mPaymentTypeSpinnerView
							.setSelection(mPaymentWaySpinnerLastPosition);
					mPaymentWaySpinnerLastPosition = -1;
				}
			}
		}
	}

	/**
	 * 校验交易参数
	 */
	private boolean checkSubmit() {
		// int paymentTypePosition = mPaymentTypeSpinnerView
		// .getSelectedItemPosition();
		// String paymentType = paymentTypeNames[paymentTypePosition];
		// if (PaymentType.PAYMENT_TYPE_ZY_QUCIK.equals(paymentType)) {
		// // 中银支付
		// if (TextUtils.isEmpty(mZyAccountIdParam)) {
		// showBocAllAccount();
		// return false;
		// }
		// }
		return true;
	}

	private void findView() {
		mAccNumberView = (TextView) findViewById(R.id.tv_payment_acc_number);
		mPaymentTypeView = (TextView) findViewById(R.id.tv_payment_type);
		mPaymentWayView = (TextView) findViewById(R.id.tv_payment_way);
		mTransDateView = (TextView) findViewById(R.id.tv_trans_date);
		mPromptView = findViewById(R.id.tv_info_prompt);
		mContentLayout = findViewById(R.id.query_result_layout);
		
		rl_result=findViewById(R.id.rl_result);
		mLoadMoreListView = (LoadMoreListView) findViewById(R.id.lv_result_list);
		// view初始化
		mContentLayout.setEnabled(false);
		mContentLayout.setVisibility(View.GONE);
		mPromptView.setVisibility(View.GONE);
		findViewById(R.id.slip_down).setOnClickListener(this);

		mLoadMoreListView.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore(Status status) {
				if (status == Status.LOADING) {
					queryTransRecordList(false);
				}
			}
		});
	}

	private void showPrompt(final View anchorView) {
		anchorView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						// 只第一次显示提示：提示布局不可见并且查询结果布局不可见
						if (mPromptView.getVisibility() != View.VISIBLE
								&& mContentLayout.getVisibility() != View.VISIBLE) {
							mPromptView.postDelayed(new Runnable() {
								@Override
								public void run() {
									MarginLayoutParams layoutParams = (MarginLayoutParams) mPromptView
											.getLayoutParams();
									layoutParams.topMargin = anchorView
											.getMeasuredHeight();
									mPromptView.setLayoutParams(layoutParams);
									mPromptView.setVisibility(View.VISIBLE);
									mPromptView.requestLayout();
								}
							}, 100);
						}
					}
				});
	}

	/**
	 * 选择要查询的中银账户
	 */
	public void showBocAllAccount() {
		// 选择中银快付
		// BiiHttpEngine.showProgressDialog();
		httpObserver.req_queryAllAccount(mZyAccountTypes,
				"queryZYAccountCallback");
	}

	/**
	 * 获取当前支付类型参数，如果当前选择支付方式为协议支付返回null
	 * 
	 * @return
	 */
	public String getCurrentPaymentWayParam() {
		int paymentTypePosition = mPaymentTypeSpinnerView
				.getSelectedItemPosition();
		String paymentType = paymentTypeNames[paymentTypePosition];
		if (PaymentType.PAYMENT_TYPE_TREATY_QUCIK.equals(paymentType)) {
			// 当前类型为协议支付
			return null;
		}
		int paymentyWayPosition = mPaymentWaySpinnerView
				.getSelectedItemPosition();
		// return paymentWayValues[paymentyWayPosition];
		return "";
	}

	/**
	 * 获取当前查询支付方式，在点击查询需要更新此值，更多不需要更新参数
	 * 
	 * @return
	 */
	private String getCurrentPaymentTypeParam() {
		int paymentTypePosition = mPaymentTypeSpinnerView
				.getSelectedItemPosition();
		String paymentType = paymentTypeNames[paymentTypePosition];
		return paymentType;
	}

	/**
	 * 获取当前查询支付账户，在点击查询需要更新此值，更多不需要更新参数
	 * 
	 * @return
	 */
	private String getCurrentPaymentAccountParam() {
		int paymentTypePosition = mPaymentAccountSpinnerView
				.getSelectedItemPosition();
		String paymentAccount;
		if (paymentTypePosition == 0) {
			paymentAccount = "全部";
		} else {
			Map<Object, Object> account = EpayUtil.getMap(accountList
					.get(paymentTypePosition - 1));
			paymentAccount = EpayUtil
					.getString(
							account.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_ID),
							"");
			mPaymentAccountNum = EpayUtil.getString(account.get("accountNumber"), "");
		}
		return paymentAccount;
	}

	private TransQueryActivity getActivity() {
		return this;
	}

}
