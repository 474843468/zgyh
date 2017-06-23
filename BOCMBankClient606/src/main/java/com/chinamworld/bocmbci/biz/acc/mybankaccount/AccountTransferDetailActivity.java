package com.chinamworld.bocmbci.biz.acc.mybankaccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.biz.acc.adapter.AccTransGalleryAdapter;
import com.chinamworld.bocmbci.biz.acc.adapter.AccountTransferAdapter;
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
 * 账户交易明细查询
 * 
 * @author wangmengmeng
 * 
 */
public class AccountTransferDetailActivity extends AccBaseActivity implements
		OnClickListener {
	/** 账户列表信息页 */
	private View view;
	/** 账户列表 */
	private List<Map<String, Object>> bankList;
	/** 账户查询Layout */
	// private View rl_query_transfer;
	/** 币种下拉菜单 */
	private Spinner currency_choose;
	/** 钞汇下拉菜单 */
	private Spinner cashRemit_choose;
	/** 钞汇适配 器 */
	private ArrayAdapter<ArrayList<String>> cashRemitadapter;
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
	private Map<String, Object> currentBankList;
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
	/** 查询结果Layout */
	private RelativeLayout rl_query_transfer_result;
	private RelativeLayout load_more;
	private Button btn_load_more;
	private int recordNumber = 0;
	private int loadNumber = 0;
	private AccountTransferAdapter detailadapter;
	private ImageView acc_frame_left;
	private ImageView acc_btn_goitem;
	private static String currency1 = "";
	private static String currency2 = "";
	/** 币种 */
	public List<String> queryCurrencyList = new ArrayList<String>();
	public List<String> queryCodeList = new ArrayList<String>();
	public List<List<String>> queryCashRemitList = new ArrayList<List<String>>();
	public List<List<String>> queryCashRemitCodeList = new ArrayList<List<String>>();
	private int cashPosition = 0;
	private boolean isfirst = true;
	private boolean canclick = true;
	private Map<String, String> queryMap = new HashMap<String, String>();
	/** 第一次进入 */
	private boolean isshowfirst = true;
	/** 查询布局 */
	private RelativeLayout queryLayout;
	/** 结果头视图 */
	private LinearLayout acc_query_result_condition;
	/** 查询最大跨度和最长时间范围 */
	private Map<String, Object> queryMaxTime;
	/** 账户明细中的最长时间 */
	private int maxYears;
	/** 账户明细中的最大跨度 */
	private int maxMonths;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.acc_myaccount_trans_title));
		// 右上角按钮赋值
		setText(this.getString(R.string.acc_rightbtn_go_main));
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		setBackBtnClick(backBtnClick);
		// 添加布局
		view = addView(R.layout.acc_mybankaccount_querytransfer);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0,
					0, 0);
		}
		dateTime = this.getIntent().getStringExtra(ConstantGloble.DATE_TIEM);
		init();
		setLeftSelectedPosition("accountManager_1");
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		setLeftSelectedPosition("accountManager_1");
//	}

	/** 左侧返回按钮点击事件 */
	OnClickListener backBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setResult(RESULT_OK);
			finish();
		}
	};

	/** 初始化界面 */
	private void init() {
		// 取得账户列表信息
		bankList = AccDataCenter.getInstance().getCommCardList();
		// rl_query_transfer = (RelativeLayout) LayoutInflater.from(
		// AccountTransferDetailActivity.this).inflate(
		// R.layout.acc_querytransfer_condition, null);
		queryLayout = (RelativeLayout) view
				.findViewById(R.id.layout_queryviews);
		acc_frame_left = (ImageView) view.findViewById(R.id.acc_frame_left);
		acc_btn_goitem = (ImageView) view.findViewById(R.id.acc_btn_goitem);
		gallery = (CustomGallery) view.findViewById(R.id.viewPager);
		// 条件视图
		query_condition = (LinearLayout) view
				.findViewById(R.id.ll_query_condition);
		query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
		// 查询条件页面初始化
		currency_choose = (Spinner) view
				.findViewById(R.id.acc_currency_spinner);
		cashRemit_choose = (Spinner) view
				.findViewById(R.id.acc_cashRemit_spinner);

		tv_startdate = (TextView) view
				.findViewById(R.id.acc_query_transfer_startdate);
		tv_enddate = (TextView) view
				.findViewById(R.id.acc_query_transfer_enddate);
		// 三天前的日期
		String startThreeDate = QueryDateUtils.getlastthreeDate(dateTime);
		tv_startdate.setText(startThreeDate);
		// 系统当前时间格式化
		currenttime = QueryDateUtils.getcurrentDate(dateTime);
		// 初始结束时间赋值
		tv_enddate.setText(currenttime);
		tv_startdate.setOnClickListener(chooseDateClick);
		tv_enddate.setOnClickListener(chooseDateClick);
		btn_query_onweek = (Button) view.findViewById(R.id.btn_acc_onweek);
		btn_query_onmonth = (Button) view.findViewById(R.id.btn_acc_onmonth);
		btn_query_threemonth = (Button) view
				.findViewById(R.id.btn_acc_threemonth);
		btn_query = (Button) view.findViewById(R.id.btn_acc_query_transfer);

		btn_query_onweek.setOnClickListener(this);
		btn_query_onmonth.setOnClickListener(this);
		btn_query_threemonth.setOnClickListener(this);
		btn_query.setOnClickListener(this);
		LinearLayout ll_up = (LinearLayout) view.findViewById(R.id.ll_up);
		img_up = (ImageView) view.findViewById(R.id.acc_query_up);
		// 收起事件监听
		ll_up.setOnClickListener(upClick);
		img_up.setOnClickListener(upClick);
		rl_query_transfer_result = (RelativeLayout) view
				.findViewById(R.id.acc_query_result_layout);
		queryCodeList = AccDataCenter.getInstance().getQueryCodeList();
		queryCurrencyList = AccDataCenter.getInstance().getQueryCurrencyList();
		queryCashRemitCodeList = AccDataCenter.getInstance()
				.getQueryCashRemitCodeList();
		queryCashRemitList = AccDataCenter.getInstance()
				.getQueryCashRemitList();
		currency = queryCodeList.get(0);
		spinnerInit();

		if (LocalData.Currency.get(currency).equals(ConstantGloble.ACC_RMB)) {
			// 人民币
			cashremit = cashMapList.get(0);
		} else {
			cashremit = queryCashRemitCodeList.get(0).get(0);
		}

		initPage();
		initfirst();
		// queryDetail();

	}

	/*
	 * @Override public void onWindowFocusChanged(boolean hasFocus) {
	 * super.onWindowFocusChanged(hasFocus); if (hasFocus && isshowfirst) {
	 * isshowfirst = false; PopupWindowUtils.getInstance().getQueryPopupWindow(
	 * rl_query_transfer, BaseDroidApp.getInstanse().getCurrentAct());
	 * PopupWindowUtils.getInstance().showQueryPopupWindowFirst(); } }
	 */

	/**
	 * 初始化page容器
	 */
	private void initPage() {
		int list_item = this.getIntent().getIntExtra(
				ConstantGloble.ACC_POSITION, 0);
		// 刚进入时，当前账户为选择的账户
		currentBankList = bankList.get(list_item);
		AccTransGalleryAdapter adapter = new AccTransGalleryAdapter(this,
				bankList);
		gallery.setAdapter(adapter);
		gallery.setSelection(list_item);
		if (list_item == 0) {
			acc_frame_left.setVisibility(View.INVISIBLE);
			if (bankList.size() == 1) {
				acc_btn_goitem.setVisibility(View.INVISIBLE);
			} else {
				acc_btn_goitem.setVisibility(View.VISIBLE);
			}

		} else if (list_item == bankList.size() - 1) {
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
				if (isfirst) {
					isfirst = false;
				} else {
					if (position == 0) {
						acc_frame_left.setVisibility(View.INVISIBLE);
						if (bankList.size() == 1) {
							acc_btn_goitem.setVisibility(View.INVISIBLE);
						} else {
							acc_btn_goitem.setVisibility(View.VISIBLE);
						}
					} else if (position == bankList.size() - 1) {
						acc_frame_left.setVisibility(View.VISIBLE);
						acc_btn_goitem.setVisibility(View.INVISIBLE);
					} else {
						acc_frame_left.setVisibility(View.VISIBLE);
						acc_btn_goitem.setVisibility(View.VISIBLE);
					}

					currentBankList = bankList.get(position);
					// 请求详情接口
					String acctype = (String) currentBankList
							.get(Acc.ACC_ACCOUNTTYPE_RES);
					if (acctype.equals(ZHONGYIN) || acctype.equals(GREATWALL)
							|| acctype.equals(SINGLEWAIBI)) {
						// 信用卡
						// 查询信用卡币种
						psnCrcdCurrencyQuery();
					} else if (acctype.equals(ICCARD)) {
						// 电子现金账户
						clearList();
						canclick = true;
						currency = LocalData.queryCurrencyMap
								.get(ConstantGloble.ACC_RMB);
						queryCurrencyList.add(ConstantGloble.ACC_RMB);
						queryCodeList.add(LocalData.queryCurrencyMap
								.get(ConstantGloble.ACC_RMB));
						spinnerInit();
					} else if (acctype
							.equalsIgnoreCase(ConstantGloble.ACC_TYPE_REG)
							|| acctype
									.equalsIgnoreCase(ConstantGloble.ACC_TYPE_EDU)
							|| acctype
									.equalsIgnoreCase(ConstantGloble.ACC_TYPE_ZOR)) {
						// 定期
						canclick = true;
						clearList();
						currency = LocalData.queryCurrencyMap
								.get(ConstantGloble.ACC_RMB);
						queryCurrencyList.add(ConstantGloble.ACC_RMB);
						queryCodeList.add(LocalData.queryCurrencyMap
								.get(ConstantGloble.ACC_RMB));
						spinnerInit();
					} else {
						// 请求余额
						requestAccBankAccountDetail(String
								.valueOf(currentBankList
										.get(Acc.ACC_ACCOUNTID_RES)));
					}

				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
			if (Acc.QRY_ACC_BALANCE_API.equals(biiResponseBody.getMethod())) {
				if (biiResponse.isBiiexception()) {// 代表返回数据异常
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null) {
						if (biiError.getCode() != null) {
							if (LocalData.timeOutCode.contains(biiError
									.getCode())) {// 表示回话超时 要重新登录
								showTimeOutDialog(biiError.getMessage());

							} else {// 非会话超时错误拦截
								BaseDroidApp.getInstanse().createDialog("",
										biiError.getMessage(),
										new OnClickListener() {

											@Override
											public void onClick(View v) {
												BaseDroidApp.getInstanse()
														.dismissErrorDialog();
												nobackClick.onClick(v);
											}
										});

							}
						}
					}
					return true;
				}
				return false;// 没有异常
			} else {

				return super.httpRequestCallBackPre(resultObj);
			}
		}
		// 随机数获取异常

		return super.httpRequestCallBackPre(resultObj);

	}

	@Override
	public void commonHttpErrorCallBack(String requestMethod) {
		if (Acc.QRY_ACC_BALANCE_API.equals(requestMethod)) {
			// 代表返回数据异常
			String message = getResources().getString(
					R.string.communication_fail);
			BaseDroidApp.getInstanse().showMessageDialog(message,
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							BaseDroidApp.getInstanse().dismissErrorDialog();
							nobackClick.onClick(v);
						}
					});

		} else {
			super.commonHttpErrorCallBack(requestMethod);
		}
	}

	/** 错误返回 */
	OnClickListener nobackClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			canclick = false;
			// 币种适配
			ArrayAdapter<ArrayList<String>> currencyAdapter = new ArrayAdapter(
					BaseDroidApp.getInstanse().getCurrentAct(),
					R.layout.custom_spinner_item, LocalData.nullcashremitList);
			currencyAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			currency_choose.setAdapter(currencyAdapter);
			currency_choose
					.setBackgroundResource(R.drawable.bg_spinner_default);
			currency_choose.setEnabled(false);
			// 人民币
			cashRemitadapter = new ArrayAdapter(BaseDroidApp.getInstanse()
					.getCurrentAct(), R.layout.custom_spinner_item,
					LocalData.nullcashremitList);
			cashRemitadapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			cashRemit_choose.setAdapter(cashRemitadapter);
			cashRemit_choose
					.setBackgroundResource(R.drawable.bg_spinner_default);
			cashRemit_choose.setEnabled(false);
			currency_choose
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {

						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {

						}
					});

			cashRemit_choose
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {

						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {

						}
					});
		}
	};

	/**
	 * 请求账户余额
	 */
	public void requestAccBankAccountDetail(String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.QRY_ACC_BALANCE_API);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Acc.DETAIL_ACC_ACCOUNTID_REQ, accountId);
		BiiHttpEngine.showProgressDialog();
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"accBankAccountDetailCallback");

	}

	/**
	 * 请求账户余额回调
	 * 
	 * @param resultObj
	 *            返回结果
	 */
	public void accBankAccountDetailCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> callbackmap = (Map<String, Object>) (biiResponseBody
				.getResult());
		canclick = true;
		if (StringUtil.isNullOrEmpty(callbackmap)) {

		} else {
			/**
			 * 账户详情列表信息
			 */
			List<Map<String, Object>> accountDetailList = (List<Map<String, Object>>) (callbackmap
					.get(ConstantGloble.ACC_DETAILIST));
			if (accountDetailList == null || accountDetailList.size() == 0) {

			} else {
				clearList();
				List<String> currencylist = new ArrayList<String>();
				List<String> codelist = new ArrayList<String>();
				for (int i = 0; i < accountDetailList.size(); i++) {
					String currencyname = (String) accountDetailList.get(i)
							.get(Acc.DETAIL_CURRENCYCODE_RES);
					// 过滤
					if (StringUtil.isNull(LocalData.currencyboci
							.get(currencyname))) {
						currencylist.add(LocalData.Currency.get(currencyname));
						codelist.add(currencyname);
					}
				}
				for (int i = 0; i < accCurrencyList.size(); i++) {
					for (int j = 0; j < currencylist.size(); j++) {
						if (currencylist.get(j).equals(accCurrencyList.get(i))) {
							queryCurrencyList.add(currencylist.get(j));
							queryCodeList.add(codelist.get(j));
							List<String> cashRemitList = new ArrayList<String>();
							List<String> cashRemitCodeList = new ArrayList<String>();
							for (int k = 0; k < accountDetailList.size(); k++) {
								if (currencylist
										.get(j)
										.equals(LocalData.Currency
												.get(accountDetailList
														.get(k)
														.get(Acc.DETAIL_CURRENCYCODE_RES)))) {
									String cash = (String) accountDetailList
											.get(k).get(
													Acc.DETAIL_CASHREMIT_RES);
									cashRemitCodeList.add(cash);
									cashRemitList.add(LocalData.cashMapValue
											.get(cash));
								}

							}
							queryCashRemitList.add(cashRemitList);
							queryCashRemitCodeList.add(cashRemitCodeList);
							break;
						}
					}
				}
			}
			BiiHttpEngine.dissMissProgressDialog();
			spinnerInit();
		}
	}

	/** 请求信用卡币种 */
	public void psnCrcdCurrencyQuery() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDCURRENCYQUERY);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Crcd.CRCD_ACCOUNTNUMBER_RES,
				(String) (currentBankList.get(Crcd.CRCD_ACCOUNTNUMBER_RES)));
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"psnCrcdCurrencyQueryCallBack");
	}

	public void psnCrcdCurrencyQueryCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		Map<String, Object> currencyMap1 = (Map<String, Object>) resultMap
				.get(Crcd.CRCD_CURRENCY1);
		Map<String, Object> currencyMap2 = (Map<String, Object>) resultMap
				.get(Crcd.CRCD_CURRENCY2);
		clearList();
		canclick = true;
		if (!StringUtil.isNullOrEmpty(currencyMap1)) {
			currency1 = String.valueOf(currencyMap1.get(Crcd.CRCD_CODE));
			queryCurrencyList.add(LocalData.Currency.get(currency1));
			queryCodeList.add(currency1);
			queryCashRemitList.add(LocalData.cashremitList);
			queryCashRemitCodeList.add(cashNullList);
		}
		if (!StringUtil.isNullOrEmpty(currencyMap2)) {
			currency2 = String.valueOf(currencyMap2.get(Crcd.CRCD_CODE));
			queryCurrencyList.add(LocalData.Currency.get(currency2));
			queryCodeList.add(currency2);
			queryCashRemitList.add(LocalData.cashremitList);
			queryCashRemitCodeList.add(cashNullList);
		}
		spinnerInit();
	}

	/**
	 * 查询最大跨度和最长时间范围
	 */
	public void requestPsnInquiryRangeQuery() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_PSNINQUIRYRANGEQUERY);
		HttpManager.requestBii(biiRequestBody, this,
				"psnInquiryRangeQueryCallBack");
	}

	/**
	 * 查询最大跨度和最长时间范围返回
	 */
	public void psnInquiryRangeQueryCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodies = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodies.get(0);
		queryMaxTime = (Map<String, Object>) biiResponseBody.getResult();
		BaseHttpEngine.dissMissProgressDialog();
		maxYears = Integer.valueOf((String) queryMaxTime
				.get(Acc.ACC_QUERY_MAXYEARS));
		maxMonths = Integer.valueOf((String) queryMaxTime
				.get(Acc.ACC_QUERY_MAXMONTHS));
		queryDetail();
	}

	public void initfirst() {
		// 结果头视图
		acc_query_result_condition = (LinearLayout) view
				.findViewById(R.id.acc_query_result_condition);

		// 查询结果页面初始化
		sort_text = (Button) view.findViewById(R.id.sort_text);
		sort_text.setText(LocalData.sortMap[0]);
		ll_sort = (LinearLayout) view.findViewById(R.id.ll_sort);

		img_sort_icon = (ImageView) view.findViewById(R.id.img_sort_icon);
		img_sort_icon.setBackgroundResource(R.drawable.icon_paixu_time);
		// 初始化排序框
		PopupWindowUtils.getInstance().setOnPullSelecterListener(
				AccountTransferDetailActivity.this, ll_sort, LocalData.sortMap,
				null, sortClick);
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
		acc_query_result_condition.setOnClickListener(backQueryClick);
	}

	public void spinnerInit() {
		canclick = true;
		// 币种适配
		ArrayAdapter<ArrayList<String>> currencyAdapter = new ArrayAdapter(
				this, R.layout.custom_spinner_item, queryCurrencyList);
		currencyAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		currency_choose.setAdapter(currencyAdapter);
		currency_choose.setBackgroundResource(R.drawable.bg_spinner);
		currency_choose.setEnabled(true);
		// 钞汇适配
		if (LocalData.Currency.get(currency).equals(ConstantGloble.ACC_RMB)) {
			// 人民币
			cashRemitadapter = new ArrayAdapter(this,
					R.layout.custom_spinner_item, LocalData.nullcashremitList);
		} else {
			cashRemitadapter = new ArrayAdapter(this,
					R.layout.custom_spinner_item, queryCashRemitList.get(0));
		}
		cashRemitadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cashRemit_choose.setAdapter(cashRemitadapter);
		currency_choose.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (!StringUtil.isNullOrEmpty(queryCodeList)
						&& !StringUtil.isNullOrEmpty(queryCurrencyList)) {

					currency = queryCodeList.get(position);
					String mycurrency = queryCurrencyList.get(position);
					String rmb = ConstantGloble.ACC_RMB;
					cashPosition = position;
					if (mycurrency.equals(rmb)) {
						cashRemitadapter = new ArrayAdapter(
								AccountTransferDetailActivity.this,
								R.layout.custom_spinner_item,
								LocalData.nullcashremitList);
						cashRemit_choose.setAdapter(cashRemitadapter);
						cashRemit_choose
								.setBackgroundResource(R.drawable.bg_spinner_default);
						cashRemit_choose.setEnabled(false);
					} else {
						cashRemit_choose.setEnabled(true);
						cashRemitadapter = new ArrayAdapter(
								AccountTransferDetailActivity.this,
								R.layout.custom_spinner_item,
								queryCashRemitList.get(position));
						cashRemitadapter
								.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						cashRemit_choose.setAdapter(cashRemitadapter);
						cashRemit_choose
								.setBackgroundResource(R.drawable.bg_spinner);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if (!StringUtil.isNullOrEmpty(queryCodeList)
						&& !StringUtil.isNullOrEmpty(queryCurrencyList)) {
					cashPosition = 0;
					currency = queryCodeList.get(0);
					String mycurrency = queryCurrencyList.get(0);
					String rmb = ConstantGloble.ACC_RMB;
					if (mycurrency.equals(rmb)) {
						cashRemitadapter = new ArrayAdapter(
								AccountTransferDetailActivity.this,
								R.layout.custom_spinner_item,
								LocalData.nullcashremitList);
						cashRemit_choose.setAdapter(cashRemitadapter);
						cashRemit_choose
								.setBackgroundResource(R.drawable.bg_spinner_default);
						cashRemit_choose.setEnabled(false);
					} else {

						cashRemit_choose.setEnabled(true);
						cashRemitadapter = new ArrayAdapter(
								AccountTransferDetailActivity.this,
								R.layout.custom_spinner_item,
								queryCashRemitList.get(0));
						cashRemitadapter
								.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						cashRemit_choose.setAdapter(cashRemitadapter);
						cashRemit_choose
								.setBackgroundResource(R.drawable.bg_spinner);
					}
				}
			}
		});

		cashRemit_choose
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						if (LocalData.Currency.get(currency).equals(
								ConstantGloble.ACC_RMB)) {
							// 选择的人民币
							cashremit = LocalData.cashRemitMap
									.get(LocalData.nullcashremitList.get(0));
							return;
						}
						if (!StringUtil.isNullOrEmpty(queryCashRemitCodeList)
								&& !StringUtil
										.isNullOrEmpty(queryCashRemitList)) {
							cashremit = queryCashRemitCodeList
									.get(cashPosition).get(position);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						if (LocalData.Currency.get(currency).equals(
								ConstantGloble.ACC_RMB)) {
							// 选择的人民币
							cashremit = LocalData.cashRemitMap
									.get(LocalData.nullcashremitList.get(0));
							return;
						}
						if (!StringUtil.isNullOrEmpty(queryCashRemitCodeList)
								&& !StringUtil
										.isNullOrEmpty(queryCashRemitList)) {
							cashremit = queryCashRemitCodeList
									.get(cashPosition).get(0);
						}
					}
				});
	}

	/** 收起监听事件 */
	OnClickListener upClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 判断是否已经进行了查询—如果已经进行:收起所有;否则收起查询条件区域
			if (transferList == null || transferList.size() == 0) {

			} else {
				PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
				rl_query_transfer_result.setVisibility(View.VISIBLE);
				acc_query_result_condition.setVisibility(View.VISIBLE);
				queryLayout.setVisibility(View.GONE);
				ll_sort.setClickable(true);
				ll_sort.setVisibility(View.VISIBLE);
			}

		}
	};

	/**
	 * 请求conversationid回调
	 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {

		super.requestCommConversationIdCallBack(resultObj);
		// 请求账户明细信息
		requestTransferList();
	}

	/** 排序点击事件 */
	OnClickListener sortClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_text1:
				sort_text.setText(LocalData.sortMap[0]);
				img_sort_icon.setBackgroundResource(R.drawable.icon_paixu_time);
				// 全部
				if (recordNumber > loadNumber) {
					if (lv_acc_query_result.getFooterViewsCount() > 0) {

					} else {
						lv_acc_query_result.addFooterView(load_more);
					}

				}
				detailadapter.notifyDataSetChanged();
				lv_acc_query_result.setAdapter(detailadapter);
				break;
			case R.id.tv_text2:
				// 收入

				List<Map<String, Object>> listIn = insort(transferList);
				if (listIn == null || listIn.size() == 0) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							AccountTransferDetailActivity.this.getString

							(R.string.acc_transferquery_nullin));
					return;
				}
				sort_text.setText(LocalData.sortMap[1]);
				img_sort_icon
						.setBackgroundResource(R.drawable.icon_paixu_shouru);
				if (lv_acc_query_result.getFooterViewsCount() > 0) {
					lv_acc_query_result.removeFooterView(load_more);
				}
				AccountTransferAdapter indetailadapter = new AccountTransferAdapter(
						AccountTransferDetailActivity.this, listIn,
						queryMap.get(Acc.QUERYTRANSFER_ACC_CURRENCY_REQ));
				lv_acc_query_result.setAdapter(indetailadapter);
				break;
			case R.id.tv_text3:
				// 支出

				List<Map<String, Object>> listOut = outsort(transferList);
				if (listOut == null || listOut.size() == 0) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							AccountTransferDetailActivity.this.getString

							(R.string.acc_transferquery_nullout));
					return;
				}
				sort_text.setText(LocalData.sortMap[2]);
				img_sort_icon
						.setBackgroundResource(R.drawable.icon_paixu_zhichu);
				if (lv_acc_query_result.getFooterViewsCount() > 0) {
					lv_acc_query_result.removeFooterView(load_more);
				}
				AccountTransferAdapter outdetailadapter = new AccountTransferAdapter(
						AccountTransferDetailActivity.this, listOut,
						queryMap.get(Acc.QUERYTRANSFER_ACC_CURRENCY_REQ));
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

	/**
	 * 清除币种、钞汇列表数据
	 */
	public void clearList() {
		if (!StringUtil.isNullOrEmpty(queryCurrencyList)) {
			queryCurrencyList.clear();
		}
		if (!StringUtil.isNullOrEmpty(queryCodeList)) {
			queryCodeList.clear();
		}
		if (!StringUtil.isNullOrEmpty(queryCashRemitCodeList)) {
			queryCashRemitCodeList.clear();
		}
		if (!StringUtil.isNullOrEmpty(queryCashRemitList)) {
			queryCashRemitList.clear();
		}
	}

	/** 请求账户明细信息 */
	public void requestTransferList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_QUERYTRANSFERDETAIL_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Acc.QUERYTRANSFER_ACC_ACCOUNTID_REQ,
				String.valueOf(currentBankList.get(Acc.ACC_ACCOUNTID_RES)));
		paramsmap.put(Acc.QUERYTRANSFER_ACC_CURRENCY_REQ, currency);
		paramsmap.put(Acc.QUERYTRANSFER_ACC_CASHREMIT_REQ, cashremit);
		paramsmap.put(Acc.QUERYTRANSFER_ACC_STARTDATE_REQ, startdate);
		paramsmap.put(Acc.QUERYTRANSFER_ACC_ENDDATE_REQ, enddate);
		paramsmap.put(Acc.QUERYTRANSFER_ACC_REFRESH_REQ,
				ConstantGloble.LOAN_REFRESH_FALSE);
		paramsmap.put(Acc.QUERYTRANSFER_ACC_PAGESIZE_REQ,
				ConstantGloble.LOAN_PAGESIZE_VALUE);
		paramsmap.put(Acc.QUERYTRANSFER_ACC_CURRENTINDEX_REQ,
				ConstantGloble.LOAN_CURRENTINDEX_VALUE);
		biiRequestBody.setParams(paramsmap);
		queryMap = paramsmap;
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
				.get(Acc.QUERYTRANSFER_ACC_LIST_RES));
		sort_text.setText(LocalData.sortMap[0]);

		if (transferList == null || transferList.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					AccountTransferDetailActivity.this
							.getString(R.string.acc_transferquery_null));

			return;
		} else {
			if (recordNumber > 10) {
				lv_acc_query_result.addFooterView(load_more);
			}
			loadNumber = transferList.size();
			// 按全部
			// 隐藏条件界面
//			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
			acc_query_result_condition.setVisibility(View.VISIBLE);
			ll_sort.setVisibility(View.VISIBLE);
			rl_query_transfer_result.setVisibility(View.VISIBLE);
			ll_sort.setClickable(true);
			queryLayout.setVisibility(View.GONE);
			tv_acc_query_result_currency.setText(LocalData.Currency
					.get(currency));
			tv_acc_query_result_cashremit.setText(LocalData.cashRemitBackMap
					.get(cashremit));
			tv_acc_query_result_date.setText(startdate + "-" + enddate);

			ll_sort.setVisibility(View.VISIBLE);
			detailadapter = new AccountTransferAdapter(
					AccountTransferDetailActivity.this, transferList,
					queryMap.get(Acc.QUERYTRANSFER_ACC_CURRENCY_REQ));
			lv_acc_query_result.setAdapter(detailadapter);

		}
	}

	/** 请求账户明细信息 */
	public void requestTransferListForMore() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_QUERYTRANSFERDETAIL_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Acc.QUERYTRANSFER_ACC_ACCOUNTID_REQ,
				queryMap.get(Acc.QUERYTRANSFER_ACC_ACCOUNTID_REQ));
		paramsmap.put(Acc.QUERYTRANSFER_ACC_CURRENCY_REQ,
				queryMap.get(Acc.QUERYTRANSFER_ACC_CURRENCY_REQ));
		paramsmap.put(Acc.QUERYTRANSFER_ACC_CASHREMIT_REQ,
				queryMap.get(Acc.QUERYTRANSFER_ACC_CASHREMIT_REQ));
		paramsmap.put(Acc.QUERYTRANSFER_ACC_STARTDATE_REQ, startdate);
		paramsmap.put(Acc.QUERYTRANSFER_ACC_ENDDATE_REQ, enddate);
		paramsmap.put(Acc.QUERYTRANSFER_ACC_REFRESH_REQ,
				ConstantGloble.REFRESH_FOR_MORE);
		paramsmap.put(Acc.QUERYTRANSFER_ACC_PAGESIZE_REQ,
				ConstantGloble.LOAN_PAGESIZE_VALUE);
		paramsmap.put(Acc.QUERYTRANSFER_ACC_CURRENTINDEX_REQ,
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
				.get(Acc.QUERYTRANSFER_ACC_LIST_RES));

		if (transferListMore == null || transferListMore.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					AccountTransferDetailActivity.this
							.getString(R.string.acc_transferquery_null));

			return;
		} else {
			for (Map<String, Object> map : transferListMore) {
				loadNumber++;
				transferList.add(map);
			}
			if (loadNumber < recordNumber) {

			} else {
				lv_acc_query_result.removeFooterView(load_more);
			}
			// 按全部
			// 隐藏条件界面
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
//			PopupWindowUtils.getInstance().showQueryPopupWindow();
			acc_query_result_condition.setVisibility(View.GONE);
			ll_sort.setVisibility(View.GONE);
			queryLayout.setVisibility(View.VISIBLE);
			ll_sort.setClickable(false);

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
					AccountTransferDetailActivity.this,
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

	/** 直接查询 */
	public void queryDetail() {
		if (canclick) {
			// 直接进行查询
			startdate = tv_startdate.getText().toString().trim();
			enddate = tv_enddate.getText().toString().trim();
			// if (QueryDateUtils.compareDateOneYear(startdate, dateTime)) {
			// // 起始日期不能早于系统当前日期一年前
			// } else {
			// BaseDroidApp.getInstanse().showInfoMessageDialog(
			// AccountTransferDetailActivity.this
			// .getString(R.string.acc_check_start_enddate));
			// return;
			// }
			if (QueryDateUtils.compareDateMaxYears(startdate, dateTime,
					maxYears)) {
				// 起始日期不能早于系统当前日期的最长时间之前
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						"起始日期最早为系统当前日期"
								+ AccDataCenter.getInstance().messageInfo
										.get(String.valueOf(maxYears)) + "年前");
				return;
			}
			if (QueryDateUtils.compareDate(enddate, dateTime)) {
				// 结束日期在服务器日期之前
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						AccountTransferDetailActivity.this
								.getString(R.string.acc_check_enddate));
				return;
			}
			if (QueryDateUtils.compareDate(startdate, enddate)) {
				// 开始日期在结束日期之前

			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						AccountTransferDetailActivity.this
								.getString(R.string.acc_query_errordate));
				return;
			}

			// if (QueryDateUtils.compareDateThree(startdate, enddate)) {
			// // 起始日期与结束日期最大间隔为三个自然月
			// } else {
			// BaseDroidApp.getInstanse().showInfoMessageDialog(
			// AccountTransferDetailActivity.this
			// .getString(R.string.acc_check_start_end_date));
			// return;
			// }
			if (QueryDateUtils.compareDateMaxMonths(startdate, enddate,
					maxMonths)) {
				// 限定起始日期与结束日期的最大跨度
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						"查询范围最大为"
								+ AccDataCenter.getInstance().messageInfo
										.get(String.valueOf(maxMonths)) + "个月");
				return;
			}
			String type = (String) currentBankList.get(Acc.ACC_ACCOUNTTYPE_RES);
			if (type.equalsIgnoreCase(ConstantGloble.ACC_ACTYPENOT)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						AccountTransferDetailActivity.this
								.getString(R.string.acc_cannot_query));
				return;
			}
			if (lv_acc_query_result.getFooterViewsCount() > 0) {
				lv_acc_query_result.removeFooterView(load_more);
			}
			// 得到conversationid请求查询账户交易列表
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					BaseDroidApp.getInstanse().getCurrentAct()
							.getString(R.string.acc_transferquery_null));
		}
	}

	/** 事件快捷方式点击事件 */
	@Override
	public void onClick(View v) {
		// 查询事件
		switch (v.getId()) {
		case R.id.btn_acc_query_transfer:
			// 直接进行查询
			if (!StringUtil.isNullOrEmpty(transferList)) {
				transferList.clear();
			}
			query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
			rl_query_transfer_result.setVisibility(View.GONE);
			requestPsnInquiryRangeQuery();
			break;
		case R.id.btn_acc_onweek:
			if (!StringUtil.isNullOrEmpty(transferList)) {
				transferList.clear();
			}
			query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
			rl_query_transfer_result.setVisibility(View.GONE);
			if (canclick) {
				// 选择一周
				enddate = QueryDateUtils.getcurrentDate(dateTime).trim();
				startdate = QueryDateUtils.getlastWeek(dateTime).trim();
				String type = (String) currentBankList
						.get(Acc.ACC_ACCOUNTTYPE_RES);
				if (type.equalsIgnoreCase(ConstantGloble.ACC_ACTYPENOT)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							AccountTransferDetailActivity.this
									.getString(R.string.acc_cannot_query));
					return;
				}
				if (lv_acc_query_result.getFooterViewsCount() > 0) {
					lv_acc_query_result.removeFooterView(load_more);
				}
				// 得到conversationid请求查询账户交易列表
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();
			} else {
				noResponseClick.onClick(v);
			}
			break;
		case R.id.btn_acc_onmonth:
			// 选择一个月
			if (!StringUtil.isNullOrEmpty(transferList)) {
				transferList.clear();
			}
			query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
			rl_query_transfer_result.setVisibility(View.GONE);
			if (canclick) {
				enddate = QueryDateUtils.getcurrentDate(dateTime).trim();
				startdate = QueryDateUtils.getlastOneMonth(dateTime).trim();
				String type = (String) currentBankList
						.get(Acc.ACC_ACCOUNTTYPE_RES);
				if (type.equalsIgnoreCase(ConstantGloble.ACC_ACTYPENOT)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							AccountTransferDetailActivity.this
									.getString(R.string.acc_cannot_query));
					return;
				}
				if (lv_acc_query_result.getFooterViewsCount() > 0) {
					lv_acc_query_result.removeFooterView(load_more);
				}
				// 得到conversationid请求查询账户交易列表
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();
			} else {
				noResponseClick.onClick(v);
			}
			break;
		case R.id.btn_acc_threemonth:
			// 选择三个月
			if (!StringUtil.isNullOrEmpty(transferList)) {
				transferList.clear();
			}
			query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
			rl_query_transfer_result.setVisibility(View.GONE);
			if (canclick) {
				enddate = QueryDateUtils.getcurrentDate(dateTime).trim();
				startdate = QueryDateUtils.getlastThreeMonth(dateTime).trim();
				String type = (String) currentBankList
						.get(Acc.ACC_ACCOUNTTYPE_RES);
				if (type.equalsIgnoreCase(ConstantGloble.ACC_ACTYPENOT)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							AccountTransferDetailActivity.this
									.getString(R.string.acc_cannot_query));
					return;
				}
				if (lv_acc_query_result.getFooterViewsCount() > 0) {
					lv_acc_query_result.removeFooterView(load_more);
				}
				// 得到conversationid请求查询账户交易列表
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();
			} else {
				noResponseClick.onClick(v);
			}
			break;

		default:
			break;
		}

	}

	OnClickListener noResponseClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					BaseDroidApp.getInstanse().getCurrentAct()
							.getString(R.string.acc_transferquery_null));

		}
	};
}
