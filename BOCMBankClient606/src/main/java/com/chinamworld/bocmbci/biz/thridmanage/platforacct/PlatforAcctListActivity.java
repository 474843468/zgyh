package com.chinamworld.bocmbci.biz.thridmanage.platforacct;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
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
import com.chinamworld.bocmbci.biz.thridmanage.BiiConstant.CurrencyType;
import com.chinamworld.bocmbci.biz.thridmanage.ThirdDataCenter;
import com.chinamworld.bocmbci.biz.thridmanage.ThirdManagerBaseActivity;
import com.chinamworld.bocmbci.biz.thridmanage.openacct.adapter.AccOpenCardAdapter;
import com.chinamworld.bocmbci.biz.thridmanage.platforacct.adapter.PlatforAdapter;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomGallery;
import com.chinamworld.bocmbci.widget.LoadMoreListView;

/**
 * 台账查询列表
 * 
 * @author panwe
 * 
 */
public class PlatforAcctListActivity extends ThirdManagerBaseActivity implements OnClickListener {
	private static final String TAG = PlatforAcctListActivity.class.getSimpleName();
	private View mView;
	/** 查询结果整体布局 */
	private View mResultLayoutView;
	/** 展示查询时间 */
	private TextView mQueryDateView;
	/** 保证金账户 */
	private TextView mCecurityAccountView;
	/** 查询列表展示 */
	private LoadMoreListView mLoadMoreListView;

	/** 查询主布局PopupWindow */
	private View mQueryPopupView;
	/** 账户列表 */
	private CustomGallery mAccountGalleryView;
	/** 时间 */
	private TextView mTimeDataView;
	/** 保证金账户 */
	private Spinner mCecuritytradeSpinnerView;
	/** 提示语信息 */
	protected View mPromptView;

	private int mIntentPosition;

	/** 账户列表 */
	private List<Map<String, Object>> mAccountList;

//	private PopupWindowUtils mPopupWindowUtils;

	private PlatforAdapter mAdatper;
	/** 查询结果(台账查询返回是一条数据不是集合) */
	private List<Map<String, String>> mRresultListMap;
	// private BaseDroidApp mBaseDroidApp;
	// /** 请求参数资金帐号 */
	// private String mCecurityAccountParam;
	/** 请求参数银行账户 */
	private String mBankAccountParam;
	/** 请求参数查询时间 */
	private String mQueryDateParam;
	private View lyt_query_result;
//	private View acc_query_result_layout;
	private View acc_query_result_condition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getIntentData()) {
			initData();
			mView = View.inflate(this, R.layout.third_platforacc_query_activity, null);
			addView(mView);
			if (findViewById(R.id.sliding_body) != null) {
				findViewById(R.id.sliding_body).setPadding(0, 0, 0, 0);
			}
			setTitle(R.string.third_platfor);
			findView();
//			initQueryPopupView();
			BaseHttpEngine.showProgressDialogCanGoBack();
			requestSystemDateTime();
		} else {
			finish();
		}
	}

	// --------------------------------------------------------------------------------
	// request
	private void requestAccountList(int accountPosition, String timeDate, boolean isRefresh) {
		if (isRefresh) {
			initResultView();
		}
		// 没有分页查询不需要保存参数。如果有分页查询保证金位置要保存
		List<Map<String, Object>> cecList = ThirdDataCenter.getInstance().getCecAccountList();
		Map<String, Object> map = cecList.get(mCecuritytradeSpinnerView.getSelectedItemPosition() - 1);

		if (map != null) {
			mBankAccountParam = mAccountList.get(accountPosition).get(Comm.ACCOUNTNUMBER).toString();
			mQueryDateParam = timeDate;

			BiiRequestBody biiRequestBody = new BiiRequestBody();
			biiRequestBody.setMethod(Third.METHOD_PLATFORACC_ACCINFO_LIST);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(Third.PLATFORACC_LIST_ACCID, mAccountList.get(accountPosition).get(Comm.ACCOUNT_ID));
			params.put(Third.PLATFORACC_LIST_DATE, timeDate);
			params.put(Third.CECURITY_AMOUT_BZ, CurrencyType.RMB);
			params.put(Third.CECURITY_AMOUT_COMANY, map.get(Third.CECURITY_AMOUT_COMANY));
			params.put(Third.CECURITY_AMOUT_STCODE, map.get(Third.CECURITYTRADE_STOCKNO));
			params.put(Third.CECURITY_AMOUT_CAACC, map.get(Third.CECURITYTRADE_BANKACCNUM_RE));
			biiRequestBody.setParams(params);
			// 通讯开始,展示通讯框
			BaseHttpEngine.showProgressDialog();
			HttpManager.requestBii(biiRequestBody, this, "accInfoListCallBack");
		}
	}

	// ------------------------------------------------------------
	// callback
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
//		mPopupWindowUtils.showQueryPopupWindowFirst();
		mQueryPopupView.setVisibility(View.VISIBLE);
		mResultLayoutView.setVisibility(View.GONE);
		showPrompt();
	}

	private void showPrompt() {
		mQueryPopupView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				mPromptView = mView.findViewById(R.id.tv_cecurity_info_prompt);
				// 只第一次显示提示：提示布局不可见并且查询结果布局不可见
				if (mPromptView.getVisibility() != View.VISIBLE && mResultLayoutView.getVisibility() != View.VISIBLE) {
					mPromptView.postDelayed(new Runnable() {
						@Override
						public void run() {
							MarginLayoutParams layoutParams = (MarginLayoutParams) mPromptView.getLayoutParams();
							layoutParams.topMargin = mQueryPopupView.getMeasuredHeight();
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
	 * 账户信息回调
	 */
	public void accInfoListCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> map = (Map<String, String>) (biiResponseBody.getResult());
		if (map == null) {
			return;
		}

		// 提示语隐藏
		if (mPromptView.getVisibility() != View.GONE) {
			mPromptView.setVisibility(View.GONE);
		}
		String capitalAcc = map.get(Third.CECURITY_AMOUT_CAACC);
		mQueryDateView.setText(mQueryDateParam);
		mCecurityAccountView.setText(capitalAcc);
		mRresultListMap.clear();
		mRresultListMap.add(map);
		// 空提示
		if (mRresultListMap.isEmpty()) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.third_common_error));
		} else {
			dissMissPopupWindow();
		}
		// 查询结果
		setResutlView();
		mResultLayoutView.setEnabled(true);
		mResultLayoutView.setVisibility(View.VISIBLE);
	}

	private void setResutlView() {
		if (mAdatper == null) {
			mAdatper = new PlatforAdapter(this, mRresultListMap);
			mLoadMoreListView.setAdapter(mAdatper);
		} else {
			mAdatper.setData(mRresultListMap);
			mAdatper.notifyDataSetChanged();
		}
	}

	/**
	 * 请求保证金账户回调
	 * */
	@Override
	public void cecurityListCallBack(Object resultObj) {
		super.cecurityListCallBack(resultObj);
		ArrayAdapter<String> snnerData = new ArrayAdapter<String>(this, R.layout.spinner_item, initSinnerData());
		snnerData.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mCecuritytradeSpinnerView.setAdapter(snnerData);
	}

	// ---------------------------------------------------------------------------------
	// event

	private OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Map<String, String> map = (Map<String, String>) mAdatper.getItem(position);
			if (map != null) {
				HashMap<String, String> extraMap = new HashMap<String, String>();
				// 把银行账户添加到集合
				extraMap.put(Comm.ACCOUNTNUMBER, mBankAccountParam);
				for (Map.Entry<String, String> entry : map.entrySet()) {
					extraMap.put(entry.getKey(), entry.getValue());
				}
				Intent intent = new Intent(getActivity(), PlatforAcctInfoActivity.class);
				intent.putExtra("data", extraMap);
				startActivity(intent);
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

	/** 账户选择事件 */
	private OnItemSelectedListener galleryListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
			// 恢复其他选项
			ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item,
					new String[] { getText(R.string.third_opendacc_com_tip).toString() });
			timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mCecuritytradeSpinnerView.setAdapter(timeAdapter);

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
		case R.id.btn_query_submit:
			// 查询
			if (checkSubmit()) {
				String time = mTimeDataView.getText().toString();
				requestAccountList(mAccountGalleryView.getSelectedItemPosition(), time, true);
			}
			break;
		case R.id.ll_up:
			// 收起
			if (mResultLayoutView.isEnabled()) {
				dissMissPopupWindow();
			}
			break;
		case R.id.layout_down:
			// 下拉
			if (mResultLayoutView.isEnabled()) {
				mQueryPopupView.setVisibility(View.VISIBLE);
				mResultLayoutView.setVisibility(View.VISIBLE);
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

	private void dissMissPopupWindow() {
//		mPopupWindowUtils.dissMissQueryPopupWindow();
		mQueryPopupView.setVisibility(View.GONE);
		mResultLayoutView.setVisibility(View.VISIBLE);
		acc_query_result_condition.setVisibility(View.VISIBLE);
		lyt_query_result.setVisibility(View.VISIBLE);
	}

	/**
	 * 如果用户手动关闭通讯框，检测提交条件
	 * 
	 * @return true满足查询条件
	 */
	private boolean checkSubmit() {
		if (mCecuritytradeSpinnerView.getSelectedItemPosition() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.third_opendacc_com_tip));
			return false;
		}
		return true;
	}

	/***
	 * 初始化resultView();
	 */
	private void initResultView() {
		mResultLayoutView.setEnabled(false);
		mQueryDateView.setText("");
		mCecurityAccountView.setText("");
		mRresultListMap.clear();
		setResutlView();
	}

	private void initQueryPopupView() {
//		mQueryPopupView = View.inflate(this, R.layout.third_platforacct_query_condition, null);
		mQueryPopupView = mView.findViewById(R.id.third_platforacct_query_condition_id);
//		mPopupWindowUtils.getQueryPopupWindow(mQueryPopupView, this);

		mCecuritytradeSpinnerView = (Spinner) mView.findViewById(R.id.acc_cecuritytrade_spinner);
		mTimeDataView = (TextView) mView.findViewById(R.id.acc_time);

		mTimeDataView.setText(QueryDateUtils.getFincLastDay(dateTime));
		mTimeDataView.setOnClickListener(chooseDateClick);

		// FinanceIcTransGalleryAdapter adapter = new
		// FinanceIcTransGalleryAdapter(this, mAccountList);
		AccOpenCardAdapter adapter = new AccOpenCardAdapter(this, mAccountList);
		mAccountGalleryView = (CustomGallery) mView.findViewById(R.id.galy_account);
		mAccountGalleryView.setOnItemSelectedListener(galleryListener);
		mAccountGalleryView.setAdapter(adapter);
		mAccountGalleryView.setSelection(mIntentPosition);

		// 收起
		mView.findViewById(R.id.btn_query_submit).setOnClickListener(this);
		mView.findViewById(R.id.ll_up).setOnClickListener(this);
		
		mQueryPopupView.setVisibility(View.VISIBLE);
		mResultLayoutView.setVisibility(View.GONE);
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
		
		mCecurityAccountView = (TextView) mView.findViewById(R.id.tv_cecurity_account);
		mQueryDateView = (TextView) mView.findViewById(R.id.tv_acc_query_date);
		mLoadMoreListView = (LoadMoreListView) findViewById(R.id.lv_result_list);
		mResultLayoutView = mView.findViewById(R.id.acc_query_result_layout);
		
		lyt_query_result = mView.findViewById(R.id.lyt_query_result);
		acc_query_result_condition = mView.findViewById(R.id.acc_query_result_condition);
		
		// 下拉
		mView.findViewById(R.id.layout_down).setOnClickListener(this);

		mLoadMoreListView.hideLoadMoreView();
		mLoadMoreListView.setOnItemClickListener(itemClickListener);
		mResultLayoutView.setVisibility(View.INVISIBLE);
		mResultLayoutView.setEnabled(false);
	}

	private boolean getIntentData() {
		mIntentPosition = getIntent().getIntExtra("position", 0);
		return true;
	}

	private void initData() {
//		mPopupWindowUtils = PopupWindowUtils.getInstance();
		mAccountList = ThirdDataCenter.getInstance().getBankAccountList();
		mRresultListMap = new ArrayList<Map<String, String>>();
	}

	private PlatforAcctListActivity getActivity() {
		return this;
	}
}
