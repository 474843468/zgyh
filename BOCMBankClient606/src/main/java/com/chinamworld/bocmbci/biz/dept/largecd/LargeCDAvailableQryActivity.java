package com.chinamworld.bocmbci.biz.dept.largecd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.bii.constant.Third;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.biz.dept.adapter.LargeCDAvailableListAdapter;
import com.chinamworld.bocmbci.biz.dept.largecd.sign.LargeSignNotSignedActivity;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 查询可购买大额存单
 * @author liuh
 */
public class LargeCDAvailableQryActivity extends DeptBaseActivity {
	private Context context = this;
	/** 存储查询后的数据 */
	private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
	/** 查询期限 */
	private String[] queryPeriodArray;
	private LinearLayout tabcontent;
	private View view;
	/** 底部ListView Layout */
	private LinearLayout listContentLayout;
	/** 查询结果展示ListView */
	private ListView listView;
	/** 下拉箭头 */
	private LinearLayout ivPullDown;
	/** 收起箭头 */
	private LinearLayout ivPullUp;
	/** 下拉布局 */
	private LinearLayout popupLayout;
	/** 查询期限Spinner */
	private Spinner queryDateSp;
	/** 签约账户TextView */
	private TextView accNumberTv;
	/** 查询后的签约账户TextView */
	private TextView queryDateContent;
	/** 查询后的查询期限TextView */
	private TextView queryPeriodContent;
	/** ListView的更多条目 */
	private View viewFooter;
	/** 更多Button */
	private Button btnMore;
	/** 当前请求list数据的index */
	private int mCurrentIndex = 0;
	/** 总条目数 */
	private int totalCount = 0;
	/** 记录当前的Pop窗口状态 */
	private boolean isPullDownPop = false;
	private boolean isShowFirst = true;
	/** 签约账户信息 */
	private Map<String, Object> signedAcc;
	/** 查询期限ArrayAdapter */
	private ArrayAdapter<String> queryDateAdapter;
	/** 列表数据的Adapter */
	private LargeCDAvailableListAdapter adapter;
	/** 查询类型 */
	protected String tranType;
	/** 签约账户 */
	private String accountNumber;
	/** 查询期限 */
	protected String queryPeriod = "";
	private int position = 0;
	private boolean isQueryBtnClick = false;
	private View rootView;
	/** 签约关联 */
	private String linkAcctFlag = null;
	/** 查询后布局 */
	private LinearLayout topupLayout;
	/** 账户类型*/
	private TextView accType ;
	/** 查询结果页面 账户类型*/
	private TextView accQueryType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
		queryPeriodArray = getResources().getStringArray(R.array.queryPeriodArray);
		signedAcc =	DeptDataCenter.getInstance().getSignedAcc();
		initData();
	}

	/** 初始化数据*/
	private void initData() {
		accountNumber = StringUtil.getForSixForString((String) signedAcc.get(Dept.ACCOUNT_NUMBER));
		ibBack = (Button) this.findViewById(R.id.ib_back); // 返回按钮
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(LargeCDAvailableQryActivity.this, LargeCDMenuActivity.class);
				startActivity(intent);
				finish();
			}
		});
		rootView = view.findViewById(R.id.large_cd_query_layout);
		isQueryBtnClick = true;
		execuseQuery();
		init();
	}

	/** 初始化view和控件 */
	@SuppressLint("InflateParams")
	private void init() {
//		popupLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.large_cd_available_query_popwindow, null);
		queryDateContent = (TextView) findViewById(R.id.large_cd_query_cdnumber_tv);
		accQueryType = (TextView) findViewById(R.id.tv_query_acc_type);
		queryPeriodContent = (TextView) findViewById(R.id.large_cd_query_period_tv);
		popupLayout = (LinearLayout) view.findViewById(R.id.layout_notmg_pop);
		topupLayout = (LinearLayout) view.findViewById(R.id.layout_the_top);

		ivPullUp = (LinearLayout) popupLayout.findViewById(R.id.ll_pull_up_query_preandexe); 	// 收起箭头
		initQueryBeforeLayout();
		initQueryAfterLayout();
		ivPullUp.setClickable(false);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && isShowFirst) {
			isShowFirst = false;
		}
	}

	/** 查询后layout */
	private void initQueryAfterLayout() {
		ivPullDown = (LinearLayout) findViewById(R.id.img_arrow_down); // 下拉箭头
		//===============================================================================
		// add by luqp 2016年4月7日 修改查询条件遮挡ListView
		ivPullDown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupLayout.setVisibility(View.VISIBLE);
				topupLayout.setVisibility(View.GONE);
			}
		});
		//===============================================================================
		listContentLayout = (LinearLayout) this.findViewById(R.id.large_cd_list_layout);
		listView = (ListView) findViewById(R.id.dept_notmg_querylist);
		viewFooter = View.inflate(this, R.layout.acc_load_more, null);
		btnMore = (Button) viewFooter.findViewById(R.id.btn_load_more);
		btnMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialogCanGoBack();
				isQueryBtnClick = false;
				requestForAvailableQuery(false);
			}
		});
		initAfterLayout();
	}

	/** 查询后layout */
	private void initAfterLayout() {
		queryDateContent.setText(accountNumber);
		// add by luqp 2016年5月3日   账户后面添加账户类型    如:1005****8561 活期一本通 
		String accTypeStr = (String) signedAcc.get(Dept.LargeSign_accountType);
		String accountType =LocalData.AccountType.get(accTypeStr);
		accQueryType.setText(accountType);
		if (isQueryBtnClick) {
			setQueryPeriod();
		}
	}

	/** 设置查询期限 */
	private void setQueryPeriod() {
		if ("".equals(queryPeriod)) {
			position = 0;
			queryPeriodContent.setText(queryPeriodArray[position]);
		} else {
			position = Integer.parseInt(queryPeriod);
			queryPeriodContent.setText(queryPeriodArray[position]);
		}
	}

	private void initQueryBeforeLayout() {
		queryDateSp = (Spinner) popupLayout.findViewById(R.id.large_cd_tran_type);
		accNumberTv = (TextView) popupLayout.findViewById(R.id.large_cd_acc_number_tv);
		accNumberTv.setText(accountNumber);
		// add by luqp 2016年5月3日   账户后面添加账户类型    如:1005****8561 活期一本通 
		accType = (TextView) view.findViewById(R.id.tv_acc_type);
		String accTypeStr = (String) signedAcc.get(Dept.LargeSign_accountType);
		String accountType =LocalData.AccountType.get(accTypeStr);
		accType.setText(accountType);
		// 初始化查询期限
		queryDateAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, queryPeriodArray);
		queryDateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		queryDateSp.setAdapter(queryDateAdapter);
		// 查询按钮
		Button queryBtn = (Button) popupLayout.findViewById(R.id.btn_query_date_preandexe);
		queryBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
				popupLayout.setVisibility(View.GONE);
				topupLayout.setVisibility(View.VISIBLE);
				isQueryBtnClick = true;
				BaseHttpEngine.showProgressDialog();
				execuseQuery();
			}
		});
		//===============================================================================
		// add by luqp 2016年4月7日 修改查询条件遮挡ListView
		ivPullUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
				popupLayout.setVisibility(View.GONE);
				topupLayout.setVisibility(View.VISIBLE);
			}
		});
		//===============================================================================
		// 查询类型
		queryDateSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view, int position, long arg3) {
				if (position > 0) {
					queryPeriod = position + "";
				} else if (position == 0) {
					queryPeriod = "";
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
	}

	/** 执行查询操作 */
	private void execuseQuery() {
		listData.clear();
		requestForAvailableQuery(true);
	}

	/** PsnLargeCDAvailableQry查询查询已购买大额存单 req */
	public void requestForAvailableQuery(boolean isRefresh) {
		if (isRefresh) {
			mCurrentIndex = 0;
		} else {
			mCurrentIndex += Third.PAGESIZE;
		}
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.PSN_LARGE_CD_AVAILABLE_QRY);
		String conversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(conversationId);
		Map<String, Object> params = new HashMap<String, Object>();
		String accountId = String.valueOf(signedAcc.get(Dept.ACCOUNT_ID));
		params.put(Dept.ACCOUNT_ID, accountId);
		params.put(Dept.QUERY_PERIOD, queryPeriod);
		params.put(PubConstants.PUB_QUERY_FEILD_PAGE_SIZE, String.valueOf(Third.PAGESIZE));
		params.put(PubConstants.PUB_QUERY_FEILD_CURRENT_INDEX, EpayUtil.getString(mCurrentIndex, "0"));
		params.put(PubConstants.PUB_QUERY_FEILD_IS_REFRESH, EpayUtil.getString(isRefresh, "true"));
		biiRequestBody.setParams(params);

		HttpManager.requestBii(biiRequestBody, this, "availableQueryCallBack");
	}

	/** PsnLargeCDAvailableQry查询查询已购买大额存单 res */
	@SuppressWarnings("unchecked")
	public void availableQueryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> map = (Map<String, Object>) biiResponseBody.getResult();
		String recordNumber = (String) map.get(Dept.RECORD_NUMBER);
		if (!StringUtil.isNullOrEmpty(recordNumber)) {
			totalCount = Integer.parseInt(recordNumber);
		}
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) map.get(ConstantGloble.LIST);
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getResources().getString(R.string.no_list_data));
			listData.clear();
			showQueryResultView(false);
			return;
		}
		for (int i = 0; i < resultList.size(); i++) {
			listData.add((Map<String, Object>) resultList.get(i));
		}
		if (listData.size() >= totalCount) {
			listView.removeFooterView(viewFooter);
		} else {
			if (listView.getFooterViewsCount() > 0) {

			} else {
				listView.addFooterView(viewFooter);
			}
		}
		showQueryResultView(true);
	}

	/** 展示查询后的页面 */
	private void showQueryResultView(boolean isDissPop) {
		if (rootView.getVisibility() == View.INVISIBLE) {
			rootView.setVisibility(View.VISIBLE);
		}
		if (isDissPop) {
			isPullDownPop = false;
			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
			listContentLayout.setVisibility(View.VISIBLE);
			ivPullUp.setClickable(true);
			initAfterLayout();
			refreshListView(listData);
		} else {// 查询结果数据为空的时候
			ivPullUp.setClickable(false);
			listContentLayout.setVisibility(View.GONE);
			refreshListView(listData);
		}
		isQueryBtnClick = false;
	}

	/** 列表 */
	private void refreshListView(List<Map<String, Object>> listData) {
		if (adapter == null) {
			adapter = new LargeCDAvailableListAdapter(this, listData);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(onItemClickListener);
		} else {
			adapter.setListData(listData);
			adapter.notifyDataSetChanged();
		}
	}

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			Map<String, Object> map = listData.get(position);
			DeptDataCenter.getInstance().setAvailableDetial(map);
			Intent intent = new Intent();
			intent.setClass(LargeCDAvailableQryActivity.this, LargeCDDetailActivity.class);
			startActivity(intent);
		}
	};

	@SuppressLint("InflateParams")
	private void initViews() {
		setTitle(getString(R.string.large_cd_add_title));
		view = mInflater.inflate(R.layout.large_cd_available_qry, null);
		tabcontent = (LinearLayout) findViewById(R.id.sliding_body);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 0);
		}
	}

	@Override
	protected void onPause() {
		isPullDownPop = PopupWindowUtils.getInstance().isQueryPopupShowing();
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (isPullDownPop) {
			ivPullDown.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (isPullDownPop) {
						PopupWindowUtils.getInstance().showQueryPopupWindow();
						isPullDownPop = false;
					}
				}
			}, 500);
		}
	}
}
