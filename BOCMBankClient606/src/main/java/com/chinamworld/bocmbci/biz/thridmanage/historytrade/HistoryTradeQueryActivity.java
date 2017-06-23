package com.chinamworld.bocmbci.biz.thridmanage.historytrade;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Third;
import com.chinamworld.bocmbci.biz.thridmanage.BiiConstant.TransferWayType;
import com.chinamworld.bocmbci.biz.thridmanage.ThirdDataCenter;
import com.chinamworld.bocmbci.biz.thridmanage.ThirdManagerBaseActivity;
import com.chinamworld.bocmbci.biz.thridmanage.ThirdUtil;
import com.chinamworld.bocmbci.biz.thridmanage.ThirdUtil.QueryDate;
import com.chinamworld.bocmbci.biz.thridmanage.historytrade.adapter.HistoryTradeAdapter;
import com.chinamworld.bocmbci.biz.thridmanage.openacct.adapter.AccOpenCardAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomGallery;
import com.chinamworld.bocmbci.widget.LoadMoreListView;
import com.chinamworld.bocmbci.widget.LoadMoreListView.OnLoadMoreListener;
import com.chinamworld.bocmbci.widget.LoadMoreListView.Status;

/**
 * 历史交易查询
 */
public class HistoryTradeQueryActivity extends ThirdManagerBaseActivity implements OnClickListener {

	private static final String TAG = HistoryTradeQueryActivity.class.getSimpleName();

	private View mView;
	/** 查询结果整体布局 */
	private View mResultLayoutView;
	/** 展示币种 */
	private TextView mCurrencyView;
	/** 展示保证金资金 */
	private TextView mCashRemitInfoView;
	/** 展示证券公司 */
	private TextView mCashCompanyView;
	/** 展示查询时间 */
	private TextView mQueryDateView;
	/** 条件筛选 */
	private TextView mSortView;
	/** 查询结果 */
	private LoadMoreListView mResultListView;
	/** 查询主布局PopupWindow */
	private View mQueryPopupView;
	/** 账户列表 */
	private CustomGallery mAccountGalleryView;
	/** 自定义开始时间 */
	private TextView mStartTimeView;
	/** 自定义结束时间 */
	private TextView mEndTimeView;
	/** 币种类型 */
	private Spinner mCurrencySpinnerView;
	/** 保证金 */
	private Spinner mCashRemitSpinnerView;

	private int mIntentPosition;

	/** 账户列表 */
	private List<Map<String, Object>> mAccountList;
	/** 查询结果 */
	private List<Map<String, String>> mAccountResultList;

	private HistoryTradeAdapter mHistoryTradeAdapter;

	/** 请求参数账户ID */
	private String mAccountIdParams;
	/** 请求参数开始时间 */
	private String mStartDateParams;
	/** 请求参数结束时间 */
	private String mEndDateParams;
	/** 请求币种 */
	private String mCurrencyStr;
	/** 请求证券账户 */
	private String mCashRemitParams;
	/** 请求证券公司 */
	private String mCashCompanyParams;
	/** 请求证券券商代码 */
	private String mStockCodeParams;
	/** 请求参数 分页 */
	private int mCurrentPagIndexParams;

	private PopupWindowUtils mPopupWindowUtils;
	private BaseDroidApp mBaseDroidApp;
	/** 返回数据总条数 */
	private int mRecordNum;
	private View lyt_query_result;
	private View acc_query_result_condition;

	// private static final String[] CURRENCY_TYPES = new String[] {
	// RUtil.getString(R.string.tran_currency_rmb) };
	private static final String[] TRANSFER_WAY_TYPE = TransferWayType.getSortTransferWayTypeList();
	/** 默认第一次查询 */
	private boolean isFirstRequestAccountFlag = false;;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getIntentData()) {
			initData();
			mView = View.inflate(this, R.layout.third_historytrade_query_activity, null);
			addView(mView);
			if (findViewById(R.id.sliding_body) != null) {
				findViewById(R.id.sliding_body).setPadding(0, 0, 0, 0);
			}
			findView();
			// 为界面标题赋值
			setTitle(R.string.bocinvt_history_titile);
			BaseHttpEngine.showProgressDialogCanGoBack();
			requestCommConversationId();
		} else {
			finish();
		}
	}

	// --------------------------------------------------------------------------------
	// request

	/**
	 * @param accountId 账号Id
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param accountPosition 保证金位置
	 */
	private void requestAccountList(boolean isRefresh) {
		if (isRefresh) {
			mCurrentPagIndexParams = 0;
			// 刷新初始化请求参数
			int accountPositionParams = mAccountGalleryView.getSelectedItemPosition();
			mCurrencyStr = mCurrencySpinnerView.getSelectedItem().toString();
			mAccountIdParams = (String) mAccountList.get(accountPositionParams).get(Comm.ACCOUNT_ID);
			List<Map<String, Object>> cecList = ThirdDataCenter.getInstance().getCecAccountList();
			Map<String, Object> cecMap = cecList.get(mCashRemitSpinnerView.getSelectedItemPosition() - 1);
			mStockCodeParams = (String) cecMap.get(Third.CECURITYTRADE_STOCKNO);
			mCashRemitParams = (String) cecMap.get(Third.CECURITYTRADE_BANKACCNUM_RE);
			mCashCompanyParams = (String) cecMap.get(Third.CECURITY_AMOUT_COMANY);

			initResultView();
		} else {
			mCurrentPagIndexParams += Third.PAGESIZE;
		}
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Third.METHOD_HISTORY_TRADE_LIST);
		biiRequestBody.setConversationId((String) mBaseDroidApp.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));

		Map<String, Object> params = new HashMap<String, Object>();
		// params.put(Third.PLATFORACC_LIST_ACCID,
		// mAccountList.get(mAccountPositionParams).get(Comm.ACCOUNT_ID));
		params.put(Third.PLATFORACC_LIST_ACCID, mAccountIdParams);
		params.put(Third.QUERY_START_DATE, mStartDateParams);
		params.put(Third.QUERY_END_DATE, mEndDateParams);
		params.put(Third.QUERY_CURPAG, String.valueOf(mCurrentPagIndexParams));
		params.put(Third.QUERY_PAGSIZE, String.valueOf(Third.PAGESIZE));
		params.put(Third.QUERY_REFRESH, String.valueOf(isRefresh));
		// params.put(Third.QUERY_CURPAG, "0");
		// params.put(Third.QUERY_PAGSIZE, "10");
		// params.put(Third.QUERY_REFRESH, "true");
		params.put(Third.CECURITY_AMOUT_COMANY, mCashCompanyParams);
		params.put(Third.CECURITY_AMOUT_STCODE, mStockCodeParams);
		params.put(Third.CECURITY_AMOUT_CAACC, mCashRemitParams);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "accInfoListCallBack");
	}

	// ------------------------------------------------------------
	// callback

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestSystemDateTime();
	}

	/**
	 * 请求系统时间回调
	 * */
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		if (StringUtil.isNull(dateTime)) {
			return;
		}
		// 获取证券账户列表
		initQueryPopupView();
//		initQueryConditon();
//		mQueryPopupView.setVisibility(View.VISIBLE);
//		mResultLayoutView.setVisibility(View.GONE);
//		mPopupWindowUtils.showQueryPopupWindowFirst();
		mResultLayoutView.setVisibility(View.VISIBLE);
		mQueryPopupView.setVisibility(View.VISIBLE);
		acc_query_result_condition.setVisibility(View.GONE);
		lyt_query_result.setVisibility(View.GONE);
	}

	/**
	 * 账户信息回调
	 */
	public void accInfoListCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> map = (Map<String, Object>) (biiResponseBody.getResult());
		if (map == null) {
			return;
		}
		mRecordNum = Integer.valueOf((String) map.get(Third.QUERY_RECODNUM));
		List<Map<String, String>> list = (List<Map<String, String>>) map.get(Third.QUERY_DATALIST);
		LogGloble.d(TAG, "curRseultListSize : " + list.size() + " | curRecords : " + mRecordNum);
		mAccountResultList.addAll(list);
		mCashCompanyView.setText(mCashCompanyParams);
		mCashRemitInfoView.setText(mCashRemitParams);
		mCurrencyView.setText(mCurrencyStr);
		mQueryDateView.setText(mStartDateParams + ConstantGloble.BOCINVT_DATE_ADD + mEndDateParams);
		refreshResultListView();

	}

	/**
	 * 请求保证金账户回调
	 * */
	@Override
	public void cecurityListCallBack(Object resultObj) {
		super.cecurityListCallBack(resultObj);
		ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, initSinnerData());
		timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mCashRemitSpinnerView.setAdapter(timeAdapter);

		// 默认查询三个月
		if (!isFirstRequestAccountFlag && mCashRemitSpinnerView.getAdapter().getCount() > 1) {
			isFirstRequestAccountFlag = true;
			mCashRemitSpinnerView.setSelection(1);
			mCurrencySpinnerView.setSelection(1);
			// mQueryPopupView.findViewById(R.id.bt_query).performClick();
		}
	}

	// ---------------------------------------------------------------------------------
	// event
	/** 加载更多 */
	private OnLoadMoreListener loadMoreListener = new OnLoadMoreListener() {
		@Override
		public void onLoadMore(Status status) {
			if (status == Status.LOADING) {
				requestAccountList(false);
			}
		}
	};

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
			DatePickerDialog dialog = new DatePickerDialog(getActivity(), new OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					StringBuilder dateStr = new StringBuilder();
					dateStr.append(String.valueOf(year));
					dateStr.append("/");
					int month = monthOfYear + 1;
					dateStr.append(((month < 10) ? ("0" + month) : String.valueOf(month)));
					dateStr.append("/");
					dateStr.append(((dayOfMonth < 10) ? ("0" + dayOfMonth) : String.valueOf(dayOfMonth)));
					// 为日期赋值
					tv.setText(String.valueOf(dateStr));
				}
			}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			dialog.show();
		}
	};

	/** 筛选点击事件 */
	private OnClickListener sortClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_text1:
				mSortView.setText(TRANSFER_WAY_TYPE[0]);
				// mSortView.setTag(BiiConstant.TransferWayType.ALL);
				refreshResultListView();
				break;
			case R.id.tv_text2:
				// 银转证
				mSortView.setText(TRANSFER_WAY_TYPE[1]);
				// mSortView.setTag(BiiConstant.TransferWayType.BANK_TO_STOCK_CODE);
				refreshResultListView();
				break;
			case R.id.tv_text3:
				// 证转银
				mSortView.setText(TRANSFER_WAY_TYPE[2]);
				// mSortView.setTag(BiiConstant.TransferWayType.STOCK_TO_BANK_CODE);
				refreshResultListView();
				break;
			default:
				break;
			}
		}
	};

	private OnItemClickListener listItemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Map<String, String> mm = (Map<String, String>) mHistoryTradeAdapter.getItem(position);
			if (mm != null) {
				HashMap<String, String> maps = new HashMap<String, String>();
				for (Entry<String, String> m : mm.entrySet()) {
					maps.put(m.getKey(), m.getValue());
				}
				Intent it = new Intent(getActivity(), HistoryTradeInfoActivity.class);
				it.putExtra("data", maps);
				startActivity(it);
			}
		}
	};

	/** 账户选择事件 */
	private OnItemSelectedListener galleryListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
			// 恢复其他选项
			ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item,
					new String[] { getText(R.string.third_opendacc_com_tip).toString() });
			timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mCashRemitSpinnerView.setAdapter(timeAdapter);
			// 初始化币种
			initCurrencySpinner();

			// 设置账户列表箭头状态
			View arrowLeft = mQueryPopupView.findViewById(R.id.acc_frame_left);
			View arrowRight = mQueryPopupView.findViewById(R.id.acc_frame_right);
			if (position == 0) {
				arrowLeft.setVisibility(View.INVISIBLE);
				if (mAccountList.size() == 1) {
					arrowRight.setVisibility(View.INVISIBLE);
				} else {
					arrowRight.setVisibility(View.VISIBLE);
				}
			} else if (position == mAccountList.size() - 1) {
				arrowLeft.setVisibility(View.VISIBLE);
				arrowRight.setVisibility(View.INVISIBLE);
			} else {
				arrowLeft.setVisibility(View.VISIBLE);
				arrowRight.setVisibility(View.VISIBLE);
			}

			// 银行帐户查询资金帐户列表
			Map<String, Object> map = mAccountList.get(position);
			String accountNum = (String) map.get(Comm.ACCOUNTNUMBER);

			requestCecurityForBankAcc(accountNum);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_acc_onweek:
			// 查询一周
			if (checkSubmit()) {
				mStartDateParams = QueryDateUtils.getlastWeek(dateTime);
				mEndDateParams = QueryDateUtils.getcurrentDate(dateTime).trim();
				requestAccountList(true);
			}
			break;
		case R.id.btn_acc_onmonth:
			// 查询一个月
			if (checkSubmit()) {
				mStartDateParams = QueryDateUtils.getlastOneMonth(dateTime);
				mEndDateParams = QueryDateUtils.getcurrentDate(dateTime).trim();
				requestAccountList(true);
			}
			break;
		case R.id.btn_acc_threemonth:
			// 查询三个月
			if (checkSubmit()) {
				mStartDateParams = QueryDateUtils.getlastThreeMonth(dateTime);
				mEndDateParams = QueryDateUtils.getcurrentDate(dateTime).trim();
				// 初始化请求参数
				requestAccountList(true);
			}
			break;
		// case R.id.btn_acc_query_transfer:
		// // 自定义查询设置
		// View customDateView =
		// mQueryPopupView.findViewById(R.id.acc_query_date);
		// customDateView.setVisibility(View.VISIBLE);
		// break;
		case R.id.bt_query:
			// 自定义查询
			if (checkSubmit()) {
				String startDate = mStartTimeView.getText().toString();
				String endDate = mEndTimeView.getText().toString();

				List<QueryDate> qds = new ArrayList<ThirdUtil.QueryDate>();
				qds.add(QueryDate.START_SYSDATE_ONE_YEAR);
				qds.add(QueryDate.END_SYSDATE);
				qds.add(QueryDate.START_END);
				qds.add(QueryDate.START_END_THREE_MONTH);
				if (ThirdUtil.compareDate(this, dateTime, startDate, endDate, "yyyy/MM/dd", qds)) {
					mStartDateParams = startDate;
					mEndDateParams = endDate;
					requestAccountList(true);
				}
			}
			break;
		case R.id.ll_up:
			// 收起
//			if (mResultLayoutView.isEnabled()) {
				dissMissPopupWindow();
//			}
			break;
		case R.id.layout_down:
			// 下拉
			if (mQueryPopupView != null) {
//				mQueryPopupView.setVisibility(View.VISIBLE);
//				mResultLayoutView.setVisibility(View.VISIBLE);
//				acc_query_result_condition.setVisibility(View.GONE);
//				lyt_query_result.setVisibility(View.VISIBLE);
				mResultLayoutView.setVisibility(View.VISIBLE);
				mQueryPopupView.setVisibility(View.VISIBLE);
				acc_query_result_condition.setVisibility(View.GONE);
				lyt_query_result.setVisibility(View.VISIBLE);
//				mPopupWindowUtils.getQueryPopupWindow(mQueryPopupView, this);
//				mPopupWindowUtils.showQueryPopupWindow();
			}
			break;
		default:
			break;
		}
	}

	// ---------------------------------------------------------------------
	// private mothod

	private List<Map<String, String>> sortTransaction(String twDesc) {
		// 全部
		String code = TransferWayType.getTransferWayTypeCode(twDesc);
		if (code == null || TransferWayType.ALL.equalsIgnoreCase(code)) {
			return mAccountResultList;
		}
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		for (Map<String, String> map : mAccountResultList) {
			String tradeType = map.get(Third.QOERY_TRADE_TYPE);
			if (code.equalsIgnoreCase(tradeType)) {
				result.add(map);
			}
		}
		return result;
	}

	private void dissMissPopupWindow() {
//		mPopupWindowUtils.dissMissQueryPopupWindow();
//		mQueryPopupView.setVisibility(View.GONE);
//		mResultLayoutView.setVisibility(View.VISIBLE);
//		acc_query_result_condition.setVisibility(View.VISIBLE);
//		lyt_query_result.setVisibility(View.VISIBLE);
		
		mResultLayoutView.setVisibility(View.VISIBLE);
		mQueryPopupView.setVisibility(View.GONE);
		acc_query_result_condition.setVisibility(View.VISIBLE);
		lyt_query_result.setVisibility(View.VISIBLE);
	}

	/**
	 * 如果用户手动关闭通讯框，检测提交条件
	 * 
	 * @return true满足查询条件
	 */
	private boolean checkSubmit() {
		if (mCurrencySpinnerView.getSelectedItemPosition() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.please_select_currency));
			return false;
		}

		if (mCashRemitSpinnerView.getSelectedItemPosition() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.third_opendacc_com_tip));
			return false;
		}

		return mCurrencySpinnerView.getAdapter() != null;
	}

	private void refreshResultListView() {
		String twDesc = mSortView.getText().toString();
		List<Map<String, String>> data = sortTransaction(twDesc);
		String code = TransferWayType.getTransferWayTypeCode(twDesc);

		if (mHistoryTradeAdapter == null) {
			mHistoryTradeAdapter = new HistoryTradeAdapter(this, data);
			mResultListView.setAdapter(mHistoryTradeAdapter);
			mResultListView.setOnItemClickListener(listItemListener);
		} else {
			mHistoryTradeAdapter.setData(data);
			mHistoryTradeAdapter.notifyDataSetChanged();
		}

		// 查询结果为空隐藏加载更多，否则根据总条数比较
		if (data.isEmpty()) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.third_common_error));
			mResultListView.hideLoadMoreView();
		} else {
			dissMissPopupWindow();
			if (TransferWayType.ALL.equalsIgnoreCase(code)) {
				// 全部
				if (mRecordNum > mAccountResultList.size()) {
					mResultListView.showLoadMoreView();
				} else {
					mResultListView.hideLoadMoreView();
				}
			} else {
				// 不是全部隐藏
				mResultListView.hideLoadMoreView();
			}

		}

		mResultLayoutView.setEnabled(true);
		if (mResultLayoutView.getVisibility() != View.VISIBLE) {
			mResultLayoutView.setVisibility(View.VISIBLE);
		}

	}

	/***
	 * 初始化resultView();
	 */
	private void initResultView() {
		mResultLayoutView.setEnabled(false);
		mResultListView.hideLoadMoreView();
		mSortView.setText(TRANSFER_WAY_TYPE[0]);
		mSortView.setTag(null);
		mAccountResultList.clear();
		if (mHistoryTradeAdapter != null) {
			mHistoryTradeAdapter.setData(mAccountResultList);
			mHistoryTradeAdapter.notifyDataSetChanged();
		}
	}


	private void initQueryPopupView() {
//		mQueryPopupView = View.inflate(this, R.layout.third_historytrade_query_condition, null);
//		mPopupWindowUtils.getQueryPopupWindow(mQueryPopupView, this);
		mQueryPopupView = mView.findViewById(R.id.third_historytrade_query_condition_id);
		mCurrencySpinnerView = (Spinner) mView.findViewById(R.id.acc_currency_spinner);
		mCashRemitSpinnerView = (Spinner) mView.findViewById(R.id.acc_cashRemit_spinner);
		mStartTimeView = (TextView) mView.findViewById(R.id.acc_query_transfer_startdate);
		mEndTimeView = (TextView) mView.findViewById(R.id.acc_query_transfer_enddate);

		// FinanceIcTransGalleryAdapter adapter = new
		// FinanceIcTransGalleryAdapter(this, mAccountList);
		AccOpenCardAdapter adapter = new AccOpenCardAdapter(this, mAccountList);
		mAccountGalleryView = (CustomGallery) mView.findViewById(R.id.galy_account);
		mAccountGalleryView.setOnItemSelectedListener(galleryListener);
		mAccountGalleryView.setAdapter(adapter);
		mAccountGalleryView.setSelection(mIntentPosition);

		// 币种
		initCurrencySpinner();
//
//		// 初始自定义时间
		mStartTimeView.setText(QueryDateUtils.getlastthreeDate(dateTime));
		mEndTimeView.setText(QueryDateUtils.getcurrentDate(dateTime));

		mView.findViewById(R.id.btn_acc_onweek).setOnClickListener(this);
		mView.findViewById(R.id.btn_acc_onmonth).setOnClickListener(this);
		mView.findViewById(R.id.btn_acc_threemonth).setOnClickListener(this);
		// mQueryPopupView.findViewById(R.id.btn_myTime).setOnClickListener(this);
		mView.findViewById(R.id.bt_query).setOnClickListener(this);
		mStartTimeView.setOnClickListener(chooseDateClick);
		mEndTimeView.setOnClickListener(chooseDateClick);

		// 收起
		mView.findViewById(R.id.ll_up).setOnClickListener(this);
//		mResultLayoutView.setVisibility(View.VISIBLE);
//		mQueryPopupView.setVisibility(View.VISIBLE);
//		acc_query_result_condition.setVisibility(View.GONE);
//		lyt_query_result.setVisibility(View.GONE);
		
		mResultLayoutView.setVisibility(View.VISIBLE);
		mQueryPopupView.setVisibility(View.VISIBLE);
		acc_query_result_condition.setVisibility(View.GONE);
		lyt_query_result.setVisibility(View.GONE);
	}

	private void findView() {
		// 右上角按钮赋值
		setTitleRightText(getString(R.string.go_main));
		setRightBtnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goMainActivity();
			}
		});
		
		
		
		mCurrencyView = (TextView) mView.findViewById(R.id.tv_acc_info_currency_value);
		mCashRemitInfoView = (TextView) mView.findViewById(R.id.tv_acc_info_cashremit_value);
		mCashCompanyView = (TextView) mView.findViewById(R.id.tv_stock_company);
		mQueryDateView = (TextView) mView.findViewById(R.id.tv_acc_query_date_value);
		mResultLayoutView = mView.findViewById(R.id.acc_query_result_layout);
		mResultListView = (LoadMoreListView) mView.findViewById(R.id.lv_result_list);
		
		lyt_query_result = mView.findViewById(R.id.lyt_query_result);
		acc_query_result_condition = mView.findViewById(R.id.acc_query_result_condition);
		mSortView = (TextView) mView.findViewById(R.id.sort_text);
		mPopupWindowUtils.setOnPullSelecterListener(this, mSortView, TRANSFER_WAY_TYPE, null, sortClick);
		// 下拉
		mView.findViewById(R.id.layout_down).setOnClickListener(this);
		mResultListView.setOnLoadMoreListener(loadMoreListener);
		mResultLayoutView.setEnabled(false);

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mCashCompanyView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mCashRemitInfoView);
	}
	private boolean getIntentData() {
		mIntentPosition = getIntent().getIntExtra("position", 0);
		return true;
	}

	private void initData() {
		mPopupWindowUtils = PopupWindowUtils.getInstance();
		mBaseDroidApp = BaseDroidApp.getInstanse();
		mAccountList = ThirdDataCenter.getInstance().getBankAccountList();
		mAccountResultList = new ArrayList<Map<String, String>>();
	}

	public void initCurrencySpinner() {
		List<String> list = new ArrayList<String>();
		list.add(getText(R.string.please_select_currency).toString());
		list.add(getText(R.string.tran_currency_rmb).toString());
		ArrayAdapter<String> accAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, list);
		accAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mCurrencySpinnerView.setAdapter(accAdapter);
	}

	private HistoryTradeQueryActivity getActivity() {
		return this;
	}

}
