package com.chinamworld.bocmbci.biz.lsforex.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.biz.lsforex.IsforexDataCenter;
import com.chinamworld.bocmbci.biz.lsforex.adapter.IsForexQueryResultAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.http.thread.PollingRequestThread;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 当前有效委托查询 */
public class IsForexCurrenyWTActivity extends IsForexBaseActivity {
	private static final String TAG = "IsForexCurrenyWTActivity";
	/** 查询条件布局页面 */
	private View conditionView = null;
	/** 开始日期 结束日期区域 */
	private View searchTimesView = null;
	/** 结算币种 */
	private Spinner codeSpinner = null;
	/** 收起图片按钮 */
	private ImageView searchUpImageView = null;
	/** 收起图片区域 */
	private View searchUpView = null;
	/** 前3天时间 */
	private String startTwoDate = null;
	/** 系统时间 */
	private String currenttime;
	/** 结算币种区域 */
	private View codeView = null;
	/** 查询条件整个页面 */
//	private View conditionAllView = null;
	/** activity View */
	private View queryView = null;
	/**
	 * backButton:返回按钮
	 */
	private Button backButton = null;
	/** 查询时间 */
	private TextView resultTime = null;
	/** 查询时间区域 */
	private View resultTimeView = null;
	/** 下拉图片按钮 */
	private ImageView downImg = null;
	/** 查询结果listView */
	private ListView resultListView = null;
	/** 委托序号 */
	private TextView titleNumber = null;
	/** 货币对 */
	private TextView titleCode = null;
	/** 委托类型 */
	private TextView titleType = null;
	/** 查询结果页面整个布局 */
	private View resultAllView = null;
	/** 结算币种 */
	private TextView jsCodeNameText = null;
	/** 显示更多区域 */
	private RelativeLayout load_more;
	/** 显示更多 */
	private TextView moreButton = null;
	private View timesView = null;
	/** listView的title */
	private View titleView = null;
	/** 结算币种名称List */
	private List<String> vfgRegCodeNameList = null;
	/** 未平仓交易状况查询按钮 */
	private Button wpcButton = null;
	/** 用户选择的结算币种代码 */
	private String vfgRegCode = null;
	/** 当前页 */
	private int currentIndex = 0;
	private int pageSize = 10;
	/** 刷新标志 */
	private boolean isRefresh = true;
	private IsForexQueryResultAdapter adapter = null;
	/** 选择的位置 */
	private int selectedPosition = -1;
	/** 选择的结算币种位置 */
	private int jsCodePositionBefore = -1;
	private boolean isInit = true;
	private int initTag = 1;
	private String lastJsCode = null;
	/** 1-初始化条件，2-撤单 */
	private int tag = 1;
	
	//wuhanP603
	private Button isforex_xiankaixianping;
	/** 交易序号 */
	public String transactionNumber = null;
	/** 内部序号 */
	public String internaNumber = null;
	
	private String vfgType = "";
	private View layout_main =null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		queryTag = (Integer) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.ISFOREX_QUERYTAG);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 20);
		}
		setTitle(getResources().getString(R.string.query_title_five));
		initTag = 1;
		// 查询需要重新获取commConversationId
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestCommConversationId();
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		if (isInit) {
			// 获取结算币种
			requestPsnVFGGetRegCurrency(vfgType);
		} else {
			// 点击查询按钮
			requestPsnVFGTradeInfoQuery(vfgRegCode, ConstantGloble.ISFOREX_ACCSEARCH_TYPE5, null, null,
					String.valueOf(pageSize), String.valueOf(currentIndex), String.valueOf(isRefresh));
		}

	}

	/** 获得结算币种---回调 */
	@Override
	public void requestPsnVFGGetRegCurrencyCallback(Object resultObj) {
		super.requestPsnVFGGetRegCurrencyCallback(resultObj);
		if (vfgRegCurrencyList == null || vfgRegCurrencyList.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					IsForexCurrenyWTActivity.this.getResources().getString(R.string.isForex_trade_jcCode));
			return;
		} else {
			vfgRegCurrencyList = dealVfgRegCode(vfgRegCurrencyList);
			if (vfgRegCurrencyList == null || vfgRegCurrencyList.size() <= 0) {
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						IsForexCurrenyWTActivity.this.getResources().getString(R.string.isForex_trade_jcCode));
				return;
			}
			vfgRegCode = vfgRegCurrencyList.get(0);
			BaseHttpEngine.canGoBack = false;
			if (initTag == 1) {
				init();
				initCondition();
				initOnClick();
			}
			getCodeNameList();
			conditionView.setVisibility(View.VISIBLE);
			resultAllView.setVisibility(View.GONE);
//			PopupWindowUtils.getInstance().showQueryPopupWindowFirst();
			// requestPsnVFGTradeInfoQuery(vfgRegCode,
			// ConstantGloble.ISFOREX_ACCSEARCH_TYPE5, null, null,
			// String.valueOf(pageSize), String.valueOf(currentIndex),
			// String.valueOf(isRefresh));
			BaseHttpEngine.dissMissProgressDialog();
		}
	}

	@Override
	public void requestPsnVFGTradeInfoQueryCallback(Object resultObj) {
		super.requestPsnVFGTradeInfoQueryCallback(resultObj);
		if (StringUtil.isNullOrEmpty(queryResultList) || recordNumber <= 0) {
			if (tag == 1) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getResources().getString(R.string.acc_transferquery_null));
			} else if (tag == 2) {
				// 显示查询条件
				dealNoRecordShowView();
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getResources().getString(R.string.acc_transferquery_null));
			}

			return;
		} else {
			showLoadMore();
			getView();
			isShow = true;
			setJsCode();
			timesView.setVisibility(View.GONE);
			titleType.setText(getResources().getString(R.string.forex_emg_money));
			titleView.setVisibility(View.VISIBLE);
			searchTimesView.setVisibility(View.GONE);
			if (isRefresh) {
				adapter = new IsForexQueryResultAdapter(IsForexCurrenyWTActivity.this, queryResultList, queryTag,
						vfgRegCode);
				resultListView.setAdapter(adapter);
			} else {
				adapter.dataChange(queryResultList);
			}
			adapter.setOnItemClickListener(listener);
		}
	}

	/** 处理结算币种 ,币种代码一定要含有币种名称 */
	private List<String> dealVfgRegCode(List<String> list) {
		int len = list.size();
		List<String> dateList = new ArrayList<String>();
		for (int i = 0; i < len; i++) {
			String s1 = list.get(i);
			if (!StringUtil.isNull(s1) && LocalData.Currency.containsKey(s1)) {
				dateList.add(s1);
			}
		}
		return dateList;

	}

	/** 得到结算币种的名称 */
	private void getCodeNameList() {
		if (vfgRegCodeNameList != null && vfgRegCodeNameList.size() > 0) {
			vfgRegCodeNameList.clear();
		}
		int len = vfgRegCurrencyList.size();
		String code1 = null;
		for (int i = 0; i < len; i++) {
			String code = vfgRegCurrencyList.get(i);
			if (LocalData.Currency.containsKey(code)) {
				code1 = LocalData.Currency.get(code);
				vfgRegCodeNameList.add(code1);
			}
		}
		if (vfgRegCodeNameList.size() <= 0 || vfgRegCodeNameList == null) {
			return;
		} else {
			ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(this, R.layout.custom_spinner_item,
					vfgRegCodeNameList);
			currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			codeSpinner.setAdapter(currencyAdapter);
			codeSpinner.setSelection(0);
			jsCodePositionBefore = 0;
		}
	}

	/** 是否显示更多按钮 */
	private void showLoadMore() {
		if (recordNumber > pageSize && isRefresh) {
			resultListView.addFooterView(load_more);
		}
		if (!isRefresh) {
			if (currentIndex + pageSize >= recordNumber) {
				resultListView.removeFooterView(load_more);
			}
		}
	}

	/** 关闭查询条件，显示查询结果 */
	private void getView() {
		conditionView.setVisibility(View.GONE);
		resultAllView.setVisibility(View.VISIBLE);
		resultTimeView.setVisibility(View.VISIBLE);
		layout_main.setVisibility(View.VISIBLE);
		conditionView.setBackgroundResource(R.drawable.img_bg_query_no);
		
	}

	/** 隐藏查询结果页面 */
	private void showCondition() {
		conditionView.setBackgroundResource(R.drawable.img_bg_query_j);
		conditionView.setVisibility(View.VISIBLE);
		resultAllView.setVisibility(View.INVISIBLE);
	}

	/** 查询结果页面-结算币种赋值 */
	private void setJsCode() {
		String jsCode = getResources().getString(R.string.isForex_vfgRegCurrency1);
		String code = LocalData.Currency.get(vfgRegCode);
		jsCodeNameText.setText(jsCode + code);
	}

	private void init() {
		queryView = LayoutInflater.from(IsForexCurrenyWTActivity.this).inflate(R.layout.isforex_query_result, null);
		tabcontent.addView(queryView);
		resultAllView = findViewById(R.id.forex_query_result_layout);
		layout_main = findViewById(R.id.layout_main);
		isforex_xiankaixianping = (Button) findViewById(R.id.isforex_xiankaixianping);
		isforex_xiankaixianping.setVisibility(View.GONE);
		
		backButton = (Button) findViewById(R.id.ib_back);
		// 查询结果页面
		resultTime = (TextView) findViewById(R.id.trade_time);
		timesView = findViewById(R.id.times_layout);
		jsCodeNameText = (TextView) findViewById(R.id.trade_jsCode);
		resultTimeView = findViewById(R.id.forex_query_result_condition);
		// 下拉
		downImg = (ImageView) findViewById(R.id.img_acc_query_back);
		resultListView = (ListView) findViewById(R.id.rate_listView);
		titleNumber = (TextView) findViewById(R.id.title_number);
		titleCode = (TextView) findViewById(R.id.title_code);
		titleType = (TextView) findViewById(R.id.title_type);
		titleView = findViewById(R.id.listView_title);
		titleView.setVisibility(View.GONE);
		load_more = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.query_list_footer, null);
		moreButton = (TextView) load_more.findViewById(R.id.finc_listiterm_tv1);
		vfgRegCodeNameList = new ArrayList<String>();
	}

	private void initCondition() {
		// 查询条件
//		conditionView = LayoutInflater.from(IsForexCurrenyWTActivity.this).inflate(R.layout.isforex_query_condition,
//				null);
		conditionView = findViewById(R.id.isforex_query_cond);
//		conditionAllView = conditionView.findViewById(R.id.ll_query_condition);
		searchTimesView = findViewById(R.id.acc_query_date);
		searchTimesView.setVisibility(View.GONE);
		codeView =findViewById(R.id.code_layout);
		codeSpinner = (Spinner) findViewById(R.id.isForex_vfgReg);
		View timesAndSearchView = findViewById(R.id.acc_query_choosedate);
		wpcButton = (Button) findViewById(R.id.isforex_query);
		wpcButton.setVisibility(View.VISIBLE);
		timesAndSearchView.setVisibility(View.GONE);
		View includeView = findViewById(R.id.include_view);
		View buttonView = findViewById(R.id.queryButton_view);
		includeView.setVisibility(View.GONE);
		buttonView.setVisibility(View.GONE);
		// 查询条件页面-收起图片
		searchUpImageView = (ImageView) findViewById(R.id.forex_query_up);
		// 查询条件页面-收起区域
		searchUpView = conditionView.findViewById(R.id.search_shouqi_up);
//		PopupWindowUtils.getInstance().getQueryPopupWindow(conditionView, this);
		conditionView.setVisibility(View.VISIBLE);
		resultAllView.setVisibility(View.GONE);
	}

	private void initOnClick() {
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		wpcButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showCondition();
				cleanData();
				setJsCode();
				cleanMoreButton();
				jsCodePositionBefore = codeSpinner.getSelectedItemPosition();
				vfgRegCode = vfgRegCurrencyList.get(jsCodePositionBefore);
				currentIndex = 0;
				isRefresh = true;
				isInit = false;
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
		});
		codeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				codeSpinner.setSelection(position, true);
				vfgRegCode = vfgRegCurrencyList.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		moreButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isRefresh = false;
				currentIndex += pageSize;
				vfgRegCode = vfgRegCurrencyList.get(jsCodePositionBefore);
				BaseHttpEngine.showProgressDialog();
				requestPsnVFGTradeInfoQuery(vfgRegCode, ConstantGloble.ISFOREX_ACCSEARCH_TYPE5, null, null,
						String.valueOf(pageSize), String.valueOf(currentIndex), String.valueOf(isRefresh));
			}
		});
		// 收起
		searchUpImageView.setOnClickListener(searchUpOnClickListener);
		searchUpView.setOnClickListener(searchUpOnClickListener);

		// 下拉
		downImg.setOnClickListener(resultOnClickListener);
		resultTimeView.setOnClickListener(resultOnClickListener);
	}

	/** 收起事件 */
	private OnClickListener searchUpOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 关闭查询条件
			if (queryResultList != null && queryResultList.size() > 0) {
//				PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
				conditionView.setVisibility(View.GONE);
				resultAllView.setVisibility(View.VISIBLE);
				resultTimeView.setVisibility(View.VISIBLE);
				layout_main.setVisibility(View.VISIBLE);
			}
		}
	};
	/** 下拉事件 */
	private OnClickListener resultOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {// 下拉
			// 显示查询条件
//			PopupWindowUtils.getInstance().showQueryPopupWindow();
			conditionView.setVisibility(View.VISIBLE);
			resultAllView.setVisibility(View.VISIBLE);
			resultTimeView.setVisibility(View.GONE);
			layout_main.setVisibility(View.VISIBLE);
		}
	};

	/** 每次查询前清空上一次查询的数据 */
	private void cleanData() {
		if (isShow) {
			if (queryResultList != null && !queryResultList.isEmpty()) {
				queryResultList.clear();
			}
			adapter.dataChange(queryResultList);
		}
	}

	/** 移除更多按钮 */
	private void cleanMoreButton() {
		if (resultListView.getFooterViewsCount() > 0) {
			resultListView.removeFooterView(load_more);
		}
	}

	/** listView 的事件 */
	private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
			//当追击止损委托的时候，增加PsnVFGTradeDetailQuery
			
			Map<String, Object> result = queryResultList.get(position);
			transactionNumber = (String) result.get(IsForex.ISFOREX_CONSIGNNUMBER_REQ);
			internaNumber = (String) result.get(IsForex.ISFOREX_internalSeq_REQ);
			requestPsnVFGTradeDetailQuery(transactionNumber, internaNumber);
			
			selectedPosition = position;
			
			

		}
	};
	
	
	public void requestPsnVFGTradeDetailQueryCallback(Object resultObj) {
		super.requestPsnVFGTradeDetailQueryCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
//		String foSet = result.get("foSet").toString();
		
		IsforexDataCenter.getInstance().setLradeDetailMap(result);
//		queryResultList.get(selectedPosition).put("foSet", foSet);
		BaseDroidApp.getInstanse().getBizDataMap().put(IsForex.ISFOREX_LIST_REQ, queryResultList);
		
		Intent intent = new Intent(IsForexCurrenyWTActivity.this, IsForexWTDetaiActivity.class);
		intent.putExtra(ConstantGloble.ISFOREX_SELECTEDPOSITION, selectedPosition);
		vfgRegCode = vfgRegCurrencyList.get(jsCodePositionBefore);
		intent.putExtra(ConstantGloble.ISFOREX_VFGREGCODE, vfgRegCode);
		lastJsCode = vfgRegCode;
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_QUERYTAG, queryTag);
		startActivityForResult(intent, ConstantGloble.ISFOREX_WT_ACTIVITY);
		
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:// 成功
			if (requestCode == ConstantGloble.ISFOREX_WT_ACTIVITY) {
				// 重新加载页面数据
				LogGloble.d(TAG, "onActivityResult===");
				BaseHttpEngine.dissMissProgressDialog();
				onActivityCleanDate();
				BaseHttpEngine.showProgressDialog();
				initTag = 2;
				tag = 2;
				isInit = false;
				vfgRegCode = lastJsCode;
				// codeSpinner.setSelection(jsCodePositionBefore);
				requestCommConversationId();
			}
			break;
		case RESULT_CANCELED:
			break;
		default:
			break;
		}
	};

	/** 重新请求数据 */
	private void onActivityCleanDate() {
		resultAllView.setVisibility(View.INVISIBLE);
		currentIndex = 0;
		isRefresh = true;
		cleanMoreButton();
		if (queryResultList != null && !queryResultList.isEmpty()) {
			queryResultList.clear();
		}
		if (adapter != null) {
			adapter.dataChange(new ArrayList<Map<String, Object>>());
		}
	}

	public boolean httpRequestCallBackPre(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
			if (IsForex.ISFOREX_PSNVFGTRADEINFOQUERY_KEY.equals(biiResponseBody.getMethod())) {
				if (biiResponse.isBiiexception()) {// 代表返回数据异常
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null) {
						if (biiError.getCode() != null) {
							if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
								if (HttpManager.mPollingRequestThread != null && PollingRequestThread.pollingFlag) {
									HttpManager.stopPolling();
								} // 要重新登录
								showTimeOutDialog(biiError.getMessage());
							} else {
								// 非会话超时错误拦截
								if (tag == 2) {
									dealNoRecordShowView();
								}
								BaseDroidApp.getInstanse().createDialog("", biiError.getMessage(),
										new OnClickListener() {

											@Override
											public void onClick(View v) {
												BaseDroidApp.getInstanse().dismissMessageDialog();

											}
										});
								return true;
							}
						}
					}
					return true;
				}
				return false;
			} else {
				return super.httpRequestCallBackPre(resultObj);
			}
		}// 返回错误码
			// 随机数获取异常
		return super.httpRequestCallBackPre(resultObj);
	};

	/** 撤单后没有交易记录，页面显示情况 */
	private void dealNoRecordShowView() {
//		PopupWindowUtils.getInstance().showQueryPopupWindow();
		conditionView.setVisibility(View.VISIBLE);
		resultAllView.setVisibility(View.GONE);
//		resultAllView.setVisibility(View.INVISIBLE);
		codeSpinner.setSelection(jsCodePositionBefore);
		conditionView.setBackgroundResource(R.drawable.img_bg_query_j);

	}

//	private void showTimeOutDialog(BiiError biiError) {
//		BaseDroidApp.getInstanse().showMessageDialog(biiError.getMessage(), new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				BaseDroidApp.getInstanse().dismissErrorDialog();
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(IsForexCurrenyWTActivity.this, LoginActivity.class);
//				startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
//			}
//		});
//	}
}
