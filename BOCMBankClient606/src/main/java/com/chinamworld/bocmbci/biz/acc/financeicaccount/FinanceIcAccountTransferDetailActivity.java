package com.chinamworld.bocmbci.biz.acc.financeicaccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.biz.acc.adapter.FinanceIcAccountTransferAdapter;
import com.chinamworld.bocmbci.biz.acc.adapter.FinanceIcTransGalleryAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomGallery;

/**
 * 电子现金账户交易明细查询
 * 
 * @author wangmengmeng
 * 
 */
public class FinanceIcAccountTransferDetailActivity extends AccBaseActivity
		implements OnClickListener {
	/** 账户列表信息页 */
	private View view;
	/** 查询开始日期 */
	private TextView tv_startdate;
	/** 查询结束日期 */
	private TextView tv_enddate;
	/** 查询一周 */
	private Button btn_query_onweek;
	/** 查询一个月 */
	private Button btn_query_onmonth;
	/** 查询三个月 */
	private Button btn_query_threemonth;
	/** 查询按钮 */
	private Button btn_query;
	/** 选择的币种 */
	private String currency;
	/** 选择的钞汇 */
	private String cashremit;
	/** 选择的开始日期 */
	private String startdate = null;
	/** 选择的结束日期 */
	private String enddate = null;
	/** 当前账户 */
	Map<String, Object> currentfinanceIcAccountList;
	/** 交易账户明细信息 */
	private List<Map<String, Object>> transferList;
	/** 交易结果布局内容 */
	/** 币种 */
	private TextView tv_acc_query_result_currency;
	/** 钞汇 */
	private TextView tv_acc_query_result_cashremit;
	/** 交易时间 */
	private TextView tv_acc_query_result_date;
	/** 交易结果列表信息 */
	private ListView lv_acc_query_result;
	/** 账户当前页面标志 */
	public int item_flag = 0;
	/** 查询条件视图 */
	private LinearLayout query_condition;
	/** 查询条件收起三角 */
	private ImageView img_up;
	/** 系统时间 */
	private String currenttime;
	/** 排序布局 */
	private Button sort_text;
	private LinearLayout ll_sort;
	private ImageView img_sort_icon;
	/** 水平列表viewPager */
	private CustomGallery gallery;
	/** 查询条件 */
	private RelativeLayout acc_query_transfer_layout;
	/** 加载更多 */
	private RelativeLayout load_more;
	/** 更多按钮 */
	private Button btn_load_more;
	/** 总条数 */
	private int recordNumber = 0;
	/** 加载页数 */
	private int loadNumber = 1;
	/** 电子现金账户明细适配器 */
	private FinanceIcAccountTransferAdapter detailadapter;
	/** 左侧箭头 */
	private ImageView acc_frame_left;
	/** 右侧箭头 */
	private ImageView acc_btn_goitem;
	/** 查询结果Layout */
	private RelativeLayout rl_query_transfer_result;
	/** 查询结果头列表 */
	private LinearLayout acc_query_result_condition;
	/** 查询条件Map */
	private Map<String, String> queryMap = new HashMap<String, String>();
	/** 第一次进入 */
	private boolean isshowfirst = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.acc_myaccount_trans_title));
		// 右上角按钮赋值
		setText(this.getString(R.string.acc_rightbtn_go_main));
		// 添加布局
		view = addView(R.layout.acc_financeic_querytransfer);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0,
					0, 0);
		}
		setLeftSelectedPosition("accountManager_4");
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(5);
				finish();
			}
		});
		dateTime = this.getIntent().getStringExtra(ConstantGloble.DATE_TIEM);
		init();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && isshowfirst) {
			isshowfirst = false;
//			PopupWindowUtils.getInstance().getQueryPopupWindow(
//					acc_query_transfer_layout,
//					BaseDroidApp.getInstanse().getCurrentAct());
//			PopupWindowUtils.getInstance().showQueryPopupWindowFirst();
		}
	}

	/** 初始化界面 */
	private void init() {
		// 取得账户列表信息
		financeIcAccountList = AccDataCenter.getInstance()
				.getFinanceIcAccountList();
		acc_query_transfer_layout=(RelativeLayout) view.findViewById(R.id.acc_query_transfer_layout);
		acc_query_transfer_layout.setVisibility(View.VISIBLE);
//		acc_query_transfer_layout = (RelativeLayout) LayoutInflater.from(
//				BaseDroidApp.getInstanse().getCurrentAct()).inflate(
//				R.layout.acc_financeic_querytransfer_condition, null);
		// 添加水平橫滑页
		gallery = (CustomGallery) view
				.findViewById(R.id.viewPager);

		acc_frame_left = (ImageView) view
				.findViewById(R.id.acc_frame_left);
		acc_btn_goitem = (ImageView) view
				.findViewById(R.id.acc_btn_goitem);
		// 条件视图
		query_condition = (LinearLayout) view
				.findViewById(R.id.ll_query_condition);
		query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
		// 查询条件页面初始化
		tv_startdate = (TextView) view
				.findViewById(R.id.acc_query_transfer_startdate);
		tv_enddate = (TextView) view
				.findViewById(R.id.acc_query_transfer_enddate);
		btn_query_onweek = (Button) view
				.findViewById(R.id.btn_acc_onweek);
		btn_query_onmonth = (Button) view
				.findViewById(R.id.btn_acc_onmonth);
		btn_query_threemonth = (Button) view
				.findViewById(R.id.btn_acc_threemonth);
		btn_query = (Button) view
				.findViewById(R.id.btn_acc_query_transfer);
		img_up = (ImageView) view
				.findViewById(R.id.acc_query_up);
		// 三天前的日期
		String startThreeDate = QueryDateUtils.getlastthreeDate(dateTime);
		tv_startdate.setText(startThreeDate);
		// 系统当前时间格式化
		currenttime = QueryDateUtils.getcurrentDate(dateTime);
		// 初始结束时间赋值
		tv_enddate.setText(currenttime);
		tv_startdate.setOnClickListener(chooseDateClick);
		tv_enddate.setOnClickListener(chooseDateClick);
		btn_query_onweek.setOnClickListener(this);
		btn_query_onmonth.setOnClickListener(this);
		btn_query_threemonth.setOnClickListener(this);
		btn_query.setOnClickListener(this);
		// 收起事件监听
		LinearLayout ll_up = (LinearLayout) view
				.findViewById(R.id.ll_up);
		ll_up.setOnClickListener(upClick);
		img_up.setOnClickListener(upClick);
		acc_query_result_condition = (LinearLayout) view
				.findViewById(R.id.acc_query_result_condition);
		acc_query_result_condition.setOnClickListener(backQueryClick);

		currency = LocalData.queryCurrencyList.get(0);
		cashremit = LocalData.nullcashremitList.get(0);
		initPage();
		initfirst();
//		queryDetail();
	}

	/**
	 * 初始化page容器
	 */
	private void initPage() {
		int list_item = this.getIntent().getIntExtra(
				ConstantGloble.ACC_POSITION, 0);
		// 刚进入时，当前账户为选择的账户
		currentfinanceIcAccountList = financeIcAccountList.get(list_item);
		FinanceIcTransGalleryAdapter adapter = new FinanceIcTransGalleryAdapter(
				this, financeIcAccountList);
		gallery.setAdapter(adapter);
		gallery.setSelection(list_item);
		if (list_item == 0) {
			acc_frame_left.setVisibility(View.INVISIBLE);
			if (financeIcAccountList.size() == 1) {
				acc_btn_goitem.setVisibility(View.INVISIBLE);
			} else {
				acc_btn_goitem.setVisibility(View.VISIBLE);
			}
		} else if (list_item == financeIcAccountList.size() - 1) {
			acc_frame_left.setVisibility(View.VISIBLE);
			acc_btn_goitem.setVisibility(View.INVISIBLE);
		} else {
			acc_frame_left.setVisibility(View.VISIBLE);
			acc_btn_goitem.setVisibility(View.VISIBLE);
		}
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				if (position == 0) {
					acc_frame_left.setVisibility(View.INVISIBLE);
					if (financeIcAccountList.size() == 1) {
						acc_btn_goitem.setVisibility(View.INVISIBLE);
					} else {
						acc_btn_goitem.setVisibility(View.VISIBLE);
					}
				} else if (position == financeIcAccountList.size() - 1) {
					acc_frame_left.setVisibility(View.VISIBLE);
					acc_btn_goitem.setVisibility(View.INVISIBLE);
				} else {
					acc_frame_left.setVisibility(View.VISIBLE);
					acc_btn_goitem.setVisibility(View.VISIBLE);
				}
				currentfinanceIcAccountList = financeIcAccountList
						.get(position);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	public void initfirst() {
		rl_query_transfer_result = (RelativeLayout) view
				.findViewById(R.id.acc_query_result_layout);
		// 查询结果页面初始化
		sort_text = (Button) view.findViewById(R.id.sort_text);
		sort_text.setText(LocalData.sortMap[0]);
		ll_sort = (LinearLayout) view.findViewById(R.id.ll_sort);
		img_sort_icon = (ImageView) view.findViewById(R.id.img_sort_icon);
		img_sort_icon.setBackgroundResource(R.drawable.icon_paixu_time);
		// 初始化排序框
		PopupWindowUtils.getInstance().setOnPullSelecterListener(
				FinanceIcAccountTransferDetailActivity.this, ll_sort,
				LocalData.sortMap, null, sortClick);
		tv_acc_query_result_currency = (TextView) view
				.findViewById(R.id.tv_acc_info_currency_value);
		tv_acc_query_result_cashremit = (TextView) view
				.findViewById(R.id.tv_acc_info_cashremit_value);
		tv_acc_query_result_date = (TextView) view
				.findViewById(R.id.tv_acc_query_date_value);
		lv_acc_query_result = (ListView) view
				.findViewById(R.id.lv_acc_query_result);
		load_more = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.acc_load_more, null);
		btn_load_more = (Button) load_more.findViewById(R.id.btn_load_more);

		btn_load_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestTransferListForMore();
			}
		});

		startdate = QueryDateUtils.getlastthreeDate(dateTime);
		enddate = QueryDateUtils.getcurrentDate(dateTime);
		tv_acc_query_result_currency.setText(currency);
		tv_acc_query_result_cashremit.setText(cashremit);
		tv_acc_query_result_date.setText(startdate + "-" + enddate);
	}

	/** 直接查询 */
	public void queryDetail() {
		// 直接进行查询
		startdate = tv_startdate.getText().toString().trim();
		enddate = tv_enddate.getText().toString().trim();

		if (QueryDateUtils.compareDateOneYear(startdate, dateTime)) {
			// 起始日期不能早于系统当前日期一年前
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					FinanceIcAccountTransferDetailActivity.this
							.getString(R.string.acc_check_start_enddate));
			return;
		}
		if (QueryDateUtils.compareDate(enddate, dateTime)) {
			// 结束日期在系统日期之前
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					FinanceIcAccountTransferDetailActivity.this
							.getString(R.string.acc_check_enddate));
			return;
		}
		if (QueryDateUtils.compareDate(startdate, enddate)) {
			// 起始日期在系统当前日期前

		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					FinanceIcAccountTransferDetailActivity.this
							.getString(R.string.acc_query_errordate));
			return;
		}
		if (QueryDateUtils.compareDateThree(startdate, enddate)) {
			// 起始日期与结束日期最大间隔为三个自然月
			if (lv_acc_query_result.getFooterViewsCount() > 0) {
				lv_acc_query_result.removeFooterView(load_more);
			}
			// 请求账户明细信息
			requestTransferList();
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					FinanceIcAccountTransferDetailActivity.this
							.getString(R.string.acc_check_start_end_date));
			return;
		}

	}

	/** 收起监听事件 */
	OnClickListener upClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 判断是否已经进行了查询—如果已经进行:收起所有;否则收起查询条件区域
			if (transferList == null || transferList.size() == 0) {

			} else {
//				PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
				acc_query_transfer_layout.setVisibility(View.GONE);
				rl_query_transfer_result.setVisibility(View.VISIBLE);
				acc_query_result_condition.setVisibility(View.VISIBLE);
				ll_sort.setVisibility(View.VISIBLE);
			}
		}
	};
	/** 筛选点击事件 */
	OnClickListener sortClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_text1:
				sort_text.setText(LocalData.sortMap[0]);
				img_sort_icon.setBackgroundResource(R.drawable.icon_paixu_time);
				if (transferList.size() < recordNumber) {
					if (lv_acc_query_result.getFooterViewsCount() > 0) {

					} else {
						lv_acc_query_result.addFooterView(load_more);
					}

				}
				detailadapter.notifyDataSetChanged();
				lv_acc_query_result.setAdapter(detailadapter);
				break;
			case R.id.tv_text2:
				// 收入在前
				List<Map<String, Object>> listIn = insort(transferList);
				if (listIn == null || listIn.size() == 0) {
					BaseDroidApp
							.getInstanse()
							.showInfoMessageDialog(
									FinanceIcAccountTransferDetailActivity.this
											.getString(R.string.acc_transferquery_nullin));
					return;
				}
				sort_text.setText(LocalData.sortMap[1]);
				img_sort_icon
						.setBackgroundResource(R.drawable.icon_paixu_shouru);
				if (lv_acc_query_result.getFooterViewsCount() > 0) {
					lv_acc_query_result.removeFooterView(load_more);
				}
				FinanceIcAccountTransferAdapter indetailadapter = new FinanceIcAccountTransferAdapter(
						FinanceIcAccountTransferDetailActivity.this, listIn);
				lv_acc_query_result.setAdapter(indetailadapter);
				break;
			case R.id.tv_text3:
				// 支出在前
				List<Map<String, Object>> listOut = outsort(transferList);
				if (listOut == null || listOut.size() == 0) {
					BaseDroidApp
							.getInstanse()
							.showInfoMessageDialog(
									FinanceIcAccountTransferDetailActivity.this
											.getString(R.string.acc_transferquery_nullout));
					return;
				}
				sort_text.setText(LocalData.sortMap[2]);
				img_sort_icon
						.setBackgroundResource(R.drawable.icon_paixu_zhichu);
				if (lv_acc_query_result.getFooterViewsCount() > 0) {
					lv_acc_query_result.removeFooterView(load_more);
				}
				FinanceIcAccountTransferAdapter outdetailadapter = new FinanceIcAccountTransferAdapter(
						FinanceIcAccountTransferDetailActivity.this, listOut);
				lv_acc_query_result.setAdapter(outdetailadapter);
				break;
			default:
				break;
			}
		}
	};

	/** 收入 */
	public List<Map<String, Object>> insort(List<Map<String, Object>> list) {
		List<Map<String, Object>> inList = new ArrayList<Map<String, Object>>();
		if (!list.isEmpty()) {
			for (Map<String, Object> map : list) {
				String amount = (String) map
						.get(Acc.QUERYTRANSFER_ACC_AMOUNT_RES);
				if (amount.startsWith(ConstantGloble.BOCINVT_DATE_ADD)) {

				} else {
					inList.add(map);
				}
			}
		}
		return inList;
	}

	/** 支出 */
	public List<Map<String, Object>> outsort(List<Map<String, Object>> list) {
		List<Map<String, Object>> outList = new ArrayList<Map<String, Object>>();
		if (!list.isEmpty()) {
			for (Map<String, Object> map : list) {
				String amount = (String) map
						.get(Acc.QUERYTRANSFER_ACC_AMOUNT_RES);
				if (amount.startsWith(ConstantGloble.BOCINVT_DATE_ADD)) {
					outList.add(map);
				} else {

				}
			}
		}
		return outList;
	}

	/** 请求账户明细信息 */
	public void requestTransferList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_ICQUERYTRANSFERDETAIL_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Acc.ICQUERYTRANSFER_ACC_ACCOUNTID_REQ,
				String.valueOf(currentfinanceIcAccountList
						.get(Acc.ACC_ACCOUNTID_RES)));
		paramsmap.put(Acc.ICQUERYTRANSFER_ACC_STARTDATE_REQ, startdate);
		paramsmap.put(Acc.ICQUERYTRANSFER_ACC_ENDDATE_REQ, enddate);
		paramsmap.put(Acc.ICQUERYTRANSFER_ACC_PAGESIZE_REQ,
				ConstantGloble.LOAN_PAGESIZE_VALUE);
		paramsmap.put(Acc.ICQUERYTRANSFER_ACC_CURRENTINDEX_REQ,
				ConstantGloble.ACC_CURRENTINDEX_VALUE);
		biiRequestBody.setParams(paramsmap);
		queryMap = paramsmap;
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "transferListCallBack");
	}

	/**
	 * 请求账户明细回调
	 * 
	 * @param resultObj
	 */
	public void transferListCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> transferback = (Map<String, Object>) biiResponseBody
				.getResult();
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(transferback)) {
			return;
		}
		recordNumber = Integer.valueOf((String) transferback
				.get(Acc.RECORDNUMBER_RES));
		transferList = (List<Map<String, Object>>) (transferback
				.get(Acc.ICQUERYTRANSFER_ACC_LIST_RES));
		sort_text.setText(LocalData.sortMap[0]);
		loadNumber++;
		if (transferList == null || transferList.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					FinanceIcAccountTransferDetailActivity.this
							.getString(R.string.acc_transferquery_null));

			return;
		} else {
			if (recordNumber > 10) {
				lv_acc_query_result.addFooterView(load_more);
			}
//			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
			acc_query_transfer_layout.setVisibility(View.GONE);
			rl_query_transfer_result.setVisibility(View.VISIBLE);
			acc_query_result_condition.setVisibility(View.VISIBLE);
			ll_sort.setVisibility(View.VISIBLE);
			tv_acc_query_result_currency.setText(currency);
			tv_acc_query_result_cashremit.setText(cashremit);
			tv_acc_query_result_date.setText(startdate + "-" + enddate);
			ll_sort.setVisibility(View.VISIBLE);
			ll_sort.setClickable(true);
			detailadapter = new FinanceIcAccountTransferAdapter(
					FinanceIcAccountTransferDetailActivity.this, transferList);
			lv_acc_query_result.setAdapter(detailadapter);

		}
	}

	/** 请求账户明细信息 */
	public void requestTransferListForMore() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_ICQUERYTRANSFERDETAIL_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Acc.ICQUERYTRANSFER_ACC_ACCOUNTID_REQ,
				queryMap.get(Acc.ICQUERYTRANSFER_ACC_ACCOUNTID_REQ));
		paramsmap.put(Acc.ICQUERYTRANSFER_ACC_STARTDATE_REQ, startdate);
		paramsmap.put(Acc.ICQUERYTRANSFER_ACC_ENDDATE_REQ, enddate);
		paramsmap.put(Acc.ICQUERYTRANSFER_ACC_PAGESIZE_REQ,
				ConstantGloble.LOAN_PAGESIZE_VALUE);
		paramsmap.put(Acc.ICQUERYTRANSFER_ACC_CURRENTINDEX_REQ,
				String.valueOf(loadNumber));
		biiRequestBody.setParams(paramsmap);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestTransferListForMoreCallBack");
	}

	/**
	 * 请求账户明细回调
	 * 
	 * @param resultObj
	 */
	public void requestTransferListForMoreCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> transferback = (Map<String, Object>) biiResponseBody
				.getResult();
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(transferback)) {
			return;
		}
		recordNumber = Integer.valueOf((String) transferback
				.get(Acc.RECORDNUMBER_RES));
		List<Map<String, Object>> transferListMore = (List<Map<String, Object>>) (transferback
				.get(Acc.ICQUERYTRANSFER_ACC_LIST_RES));
		if (transferListMore == null || transferListMore.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					FinanceIcAccountTransferDetailActivity.this
							.getString(R.string.acc_transferquery_null));

			return;
		} else {
			loadNumber++;
			for (Map<String, Object> map : transferListMore) {
				transferList.add(map);
			}
			if (transferList.size() < recordNumber) {

			} else {
				lv_acc_query_result.removeFooterView(load_more);
			}
			// 隐藏条件界面
			ll_sort.setVisibility(View.VISIBLE);
			ll_sort.setClickable(true);
			if (sort_text.getText().toString().equals(LocalData.sortMap[0])) {
				detailadapter.notifyDataSetChanged();
			} else {
				sort_text.setText(LocalData.sortMap[0]);
				detailadapter.notifyDataSetChanged();
				lv_acc_query_result.setAdapter(detailadapter);
			}
		}
	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 回主页面
			ActivityTaskManager.getInstance().removeAllActivity();
		}
	};
	/** 查询结果头倒三角点击监听事件 */
	OnClickListener backQueryClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
//			query_condition.setBackgroundResource(R.drawable.img_bg_query_no);
			// 显示查询条件
//			PopupWindowUtils.getInstance().showQueryPopupWindow();
			acc_query_transfer_layout.setVisibility(View.VISIBLE);
			acc_query_result_condition.setVisibility(View.GONE);
			ll_sort.setVisibility(View.GONE);
		}
	};
	/** 设置查询日期 */
	OnClickListener chooseDateClick = new OnClickListener() {

		@Override
		public void onClick(final View v) {
			TextView tv = (TextView) v;
			String time = tv.getText().toString();
			int startYear = Integer.parseInt(time.substring(0, 4));
			int startMonth = Integer.parseInt(time.substring(5, 7));
			int startDay = Integer.parseInt(time.substring(8, 10));
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			DatePickerDialog dialog = new DatePickerDialog(
					FinanceIcAccountTransferDetailActivity.this,
					new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							StringBuilder date = new StringBuilder();
							date.append(String.valueOf(year));
							date.append("/");
							int month = monthOfYear + 1;
							date.append(((month < 10) ? ("0" + month)
									: (month + "")));
							date.append("/");
							date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth)
									: (dayOfMonth + "")));
							// 为日期赋值
							((TextView) v).setText(String.valueOf(date));
						}
					}, startYear, startMonth - 1, startDay);
			dialog.show();
		}
	};

	/** 事件快捷方式点击事件 */
	@Override
	public void onClick(View v) {
		// 查询事件
		switch (v.getId()) {
		case R.id.btn_acc_query_transfer:
			loadNumber = 1;
			if (!StringUtil.isNullOrEmpty(transferList)) {
				transferList.clear();
			}
			query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
			rl_query_transfer_result.setVisibility(View.GONE);
			queryDetail();
			break;
		case R.id.btn_acc_onweek:
			// 选择一周
			loadNumber = 1;
			if (!StringUtil.isNullOrEmpty(transferList)) {
				transferList.clear();
			}
			query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
			rl_query_transfer_result.setVisibility(View.GONE);
			enddate = QueryDateUtils.getcurrentDate(dateTime).trim();
			startdate = QueryDateUtils.getlastWeek(dateTime).trim();
			if (lv_acc_query_result.getFooterViewsCount() > 0) {
				lv_acc_query_result.removeFooterView(load_more);
			}
			// 请求账户明细信息
			requestTransferList();
			break;
		case R.id.btn_acc_onmonth:
			// 选择一个月
			loadNumber = 1;
			if (!StringUtil.isNullOrEmpty(transferList)) {
				transferList.clear();
			}
			query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
			rl_query_transfer_result.setVisibility(View.GONE);
			enddate = QueryDateUtils.getcurrentDate(dateTime).trim();
			startdate = QueryDateUtils.getlastOneMonth(dateTime).trim();
			if (lv_acc_query_result.getFooterViewsCount() > 0) {
				lv_acc_query_result.removeFooterView(load_more);
			}
			// 请求账户明细信息
			requestTransferList();
			break;
		case R.id.btn_acc_threemonth:
			// 选择三个月
			loadNumber = 1;
			if (!StringUtil.isNullOrEmpty(transferList)) {
				transferList.clear();
			}
			query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
			rl_query_transfer_result.setVisibility(View.GONE);
			enddate = QueryDateUtils.getcurrentDate(dateTime).trim();
			startdate = QueryDateUtils.getlastThreeMonth(dateTime).trim();
			if (lv_acc_query_result.getFooterViewsCount() > 0) {
				lv_acc_query_result.removeFooterView(load_more);
			}
			// 请求账户明细信息
			requestTransferList();
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(5);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
