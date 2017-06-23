package com.chinamworld.bocmbci.biz.crcd.mycrcd;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
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
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.MainActivity;
import com.chinamworld.bocmbci.biz.acc.adapter.AccTransGalleryAdapter;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.biz.crcd.adapter.CrcdHasTrannsAdapter;
import com.chinamworld.bocmbci.biz.crcd.adapter.CrcdTransferAdapter;
import com.chinamworld.bocmbci.biz.crcd.adapter.CrcdXiaofeiAdapter;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.transquery.CrcdHasTransQueryListActivity;
import com.chinamworld.bocmbci.biz.crcd.utils.SortUtils;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
//import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomGallery;


/**
 * 未出账单 已出账单 查询条件、查询结果页面
 * 
 * @author huangyuchao
 * 
 */
public class MyCrcdDetailActivity extends CrcdBaseActivity {
	private static final String TAG = "MyCrcdDetailActivity";
	/** 账户明细查询界面视图 */
	private View view;
	// 查询条件页面
	/** 查询条件整个布局 */
//	private View search_condition_view = null;
//	/** 未出账单 */
//	RadioButton loanradiobtn1;
//	/** 已出账单 */
//	RadioButton loanradiobtn2;
//	/** 未出账单显示查询按钮 */
//	private Button btnLoanHistoryQuery;
	private LinearLayout ll_upLayout;
	/** 卡片 */
	private CustomGallery viewpager;
	/** 左箭头 */
	private ImageView acc_frame_left;
	/** 右箭头 */
	private ImageView acc_btn_goitem;
	private View spinnerView = null;
	/** 已出查询按钮 */
//	private Button yiChuSearchButton = null;

	// 查询结果页面
	private ListView lv_acc_query_result;
	/** 排序按钮 */
	private Button sort_text;
	/** 排序布局 */
//	private LinearLayout ll_sort;
	private LinearLayout acc_query_result_condition,acc_query_search_condition;
	/** 查询结果页面整个布局 */
	RelativeLayout rl_query_transfer_result;
	private Spinner timesSpinner = null;
	private Button	btn_search;
	
	private Spinner yichuorweichu = null;
	
	
	
	private RadioGroup crcd_trans_radioGroup;
	
	
	/** 未出账单查询---adapter */
	CrcdTransferAdapter ctfAdapter;
	/** 已出账单查询---adapter */
	CrcdHasTrannsAdapter chtAdapter;
	/** 消费查询---adapter */
	CrcdXiaofeiAdapter cxaAdapter;
	/** 结果页面---顶部---账号 */
	private TextView tv_acc_info_currency_value;
	/** 结果页面---顶部---收入金额 */
	TextView tv_acc_query_date_value;
	/** 支出金额 */
	TextView tv_crcd_query_pay_value;
	TextView tv_crcd_ying_huan, tv_crcd_dangqi_low_huan;
	/** 已出账单显示----详情图片按钮 */
	private ImageView img_crcd_detail;
	TextView crcd_shouru_currency, crcd_zhichu_currency;
	TextView crcd_ying_currencycode, crcd_min_currencycode;
	LinearLayout ll_currentyingForeign, ll_currentlowmoneyForeign;
	TextView crcd_forei_ying_huan, crcd_forei_ying_currencycode, crcd_forei_dangqi_low_huan,
			crcd_foreign_min_currencycode;

	LinearLayout ll_foreignzhichu, ll_foreignshouru;
	TextView tv_forei_shouru_money, tv_foreign_shuru_currency, tv_foreign_zhichu_money, tv_foreign_zhichu_currency;
//	RadioGroup radiomonthGroup;
	Button btn_acc_query_transfer;
	View query_condition;
	/** 未出账单：收入、支出 */
	private LinearLayout ll_shouandzhi;
	/** 已出账单：应还、最低 */
	private LinearLayout ll_currentandlowmoney;
	RelativeLayout load_more;

	/** 1-已出，2-未出 */
	private int checkedNum;
	/** 已出账单 */
	private static final int HASTRANS = 1;
	/** 未出账单 */
	private static final int NOTRANS = 2;
	/** 币种区域 */
	private LinearLayout ll_beedtype;
	private static final String GO_MAIN = "main";
	private static final String GO_DIVIDED = "divide";
	/** 已出账单--只有当前月份才显示账单分期 */
	public static boolean isLastMonth;

	private int isTrans = 0;
	public String cardType;
	public String fromQuery;
	static String accountId;
	String currency1 = "";
	String currency2 = "";
	String strCurrency1 = "";
	String strCurrency2 = "";
	Map<String, Object> currencyMap1;
	Map<String, Object> currencyMap2;
	int list_item;
	int currentIndex = 0;
	int pageSize = 10;
	int maxNum;
	public static String bankName;
	public static String nickName;
	/** 信用卡卡号 */
	public static String bankNumber;
	/** 已出账单月份----yyyy/MM */
	private String numMonth = "";
	/** 一月份、二月份。。。。 */
	private String strMonth = "";
	private String titlePreix = "";

	private int isShow = 2;
	/** crcdTransInfo:信用卡已出账单交易信息---币种1 */
	List<Map<String, Object>> crcdhasTransInfo = new ArrayList<Map<String, Object>>();
	/** crcdTransInfo:信用卡已出账单交易信息---币种2 */
	List<Map<String, Object>> crcdhasTransInfo1 = new ArrayList<Map<String, Object>>();
	/** 已出账单查询---币种1信息 ------PsnCrcdQueryBilledTrans查询信用卡已出账 */
	static Map<String, Object> returnMap = new HashMap<String, Object>();
	/** 已出账单查询---币种2信息-----PsnCrcdQueryBilledTrans查询信用卡已出账 */
	static Map<String, Object> returnMap1 = new HashMap<String, Object>();
	public static String strCurrencyCode;

	/** 未出账户明细信息 */
	List<Map<String, Object>> notransferList = new ArrayList<Map<String, Object>>();
	/** 保留上次未出账单数据 */
	private List<Map<String, Object>> wcDateList = null;
	/** 保留上次未出账单总计数据 */
	private List<Map<String, Object>> wcTotalList = null;
	/** 保留上次已出账单数据-接口返回的数据 */
	// private List<Map<String, Object>> ycDateList = null;
	/** 未出账单合计---查询未出账单的收入、支出 */
	private List<Map<String, Object>> totalList;
	private boolean isInitShowPop = true;
	private boolean isDissmissPop = true;
	private boolean isWcSaveLastDate = false;
	private int currentPosition = -1;
	private List<Map<String, Object>> accountInfoList = null;
	private Map<String, Object> beforeWCMap = null;
	/** 未出账单排序----1全部，2收入，3支出 */
	private int wcSort = 1;
	/** 已出账单排序----1全部，2收入，3支出 */
	private int ycSort = 1;
	/** 未出账单排序----1全部，2收入，3支出 */
	private int tag = 1;
	/** 已出账单排序----1全部，2收入，3支出 */
	// private int ycTag = 1;
	/** 未出是否请求数据 */
	private boolean isRequestWcDate = false;
	/** 已出是否请求数据 */
	private boolean isRequestYcDate = false;
	/** false-未出,true-已出 */
	private boolean isSelectWcOrYcButton = false;
	/** false-未入账,true-已入账 */
	private boolean isWrOrYr= true;
	/** 已出账单当前月份 */
	private int currentMoth = 0;
	/** 初始化Spinner */
	private boolean isInit = true;
	/** 时间数组 */
	private List<String> dateList = null;
	/** 时间数组 */
	private List<String> yichuorweichuList = null;
	// 是否从 账面信息过来
	private boolean fromcacd=false;
	private boolean isBillExist=false; // 当月账单是否已出
	
	private String accountIbkNum = null;
	// 第一次进入
	private boolean isfirstin = true;
	private ArrayAdapter<String> yichuorweichuAdapter;
	
	/** 退出动画 */
	private Animation animation_up;
	/** 进入动画 */
	private Animation animation_down;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		// 为界面标题赋值
		setTitle(this.getString(R.string.card_account_search));
		// 右上角按钮赋值
		setText(this.getString(R.string.acc_rightbtn_go_main));
		setTextTag(GO_MAIN);
		view = addView(R.layout.crcd_querytranns_detail);
//		initanimation();
		wcDateList = new ArrayList<Map<String, Object>>();
		// ycDateList = new ArrayList<Map<String, Object>>();
		wcTotalList = new ArrayList<Map<String, Object>>();
		totalList = new ArrayList<Map<String, Object>>();
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 0);
		}
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(3);
				finish();
			}
		});
		// setLeftSelectedPosition(2);
		BaseDroidApp.getInstanse().setCurrentAct(this);
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialogCanGoBack();
		list_item = this.getIntent().getIntExtra(ConstantGloble.ACC_POSITION, 0);
//		if(fromcacd){
		// 账面信息过来
			accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
			// 信用卡当月是否已出账单查询
			PsnQueryCrcdBillIsExist(accountId);
//		}else{
//			// 请求系统时间
//			requestSystemDateTime();	
//		}
		
	}

	@SuppressWarnings("static-access")
	private void initanimation() {
		animation_up = new AnimationUtils().loadAnimation(
				MyCrcdDetailActivity.this, R.anim.query_exit);
		animation_down = new AnimationUtils().loadAnimation(
				MyCrcdDetailActivity.this, R.anim.query_enter);
	}
	
	
	
	private void PsnQueryCrcdBillIsExist(String accountId) {

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNQUERYCRCDBILLISEXIST);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnQueryCrcdBillIsExistCallBack");
	
		
	}


	public void requestPsnQueryCrcdBillIsExistCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		String BillExist=(String)resultMap.get(Crcd.CRCD_ISBILLEXIST);
//		0：没有出账单  1：已出
		if("0".equals(BillExist)){
			
			isBillExist=false;	
		}else if("1".equals(BillExist)){
			isBillExist=true;
		}else{
			isBillExist=false;	
		}
		if(isfirstin){
			requestSystemDateTime();		
		}else{
			if(StringUtil.isNullOrEmpty(yichuorweichuList)){
				yichuorweichuList = new ArrayList<String>();	
			}
			getBillDate();
			yichuorweichuList.clear();
			yichuorweichuList.add(getResources().getString(R.string.mycrcd_no_bill));
			yichuorweichuList.addAll(dateList);
			yichuorweichuAdapter.notifyDataSetChanged();
			yichuorweichu.setSelection(0);
		}
	
	}



	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		LogGloble.d(TAG, "dateTime");
		dateTime = (String) resultMap.get(Comm.DATETME);
		if (StringUtil.isNull(dateTime)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		getBillDate();
		if (StringUtil.isNullOrEmpty(dateList)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		initConditionView();
		initSpinner();
		init();
//		PopupWindowUtils.getInstance().showQueryPopupWindowFirst();
		acc_query_search_condition.setVisibility(View.VISIBLE);
		crcd_trans_radioGroup.setVisibility(View.GONE);
		isInitShowPop = false;
		BaseHttpEngine.canGoBack = false;
		ll_upLayout.setClickable(false);
		requestDate();

	}

	/** 计算账单日期 */
	private void getBillDate() {
		if(StringUtil.isNullOrEmpty(dateList)){
			dateList = new ArrayList<String>();	
		}
		dateList.clear();
		if(isBillExist){
			// 已出
			for (int i = 0; i < 12; i++) {
				String t = "-" + i;
				int j = Integer.valueOf(t);
				String times = QueryDateUtils.getLastNumMonthAgo(dateTime, j);
				dateList.add(times);
			}
		}else{
			for (int i = 1; i < 13; i++) {
				String t = "-" + i;
				int j = Integer.valueOf(t);
				String times = QueryDateUtils.getLastNumMonthAgo(dateTime, j);
				dateList.add(times);
			}
		}
		
	}

	/** 初始化查询条件页面 */
	private void initConditionView() {
//		search_condition_view = LayoutInflater.from(this).inflate(R.layout.crcd_querytrans_condition_modify, null);
		
//		loanradiobtn1 = (RadioButton) search_condition_view.findViewById(R.id.loan_his_btn1);
//		loanradiobtn2 = (RadioButton) search_condition_view.findViewById(R.id.loan_his_btn2);
//		btnLoanHistoryQuery = (Button) search_condition_view.findViewById(R.id.btnLoanHistoryQuery);
//		radiomonthGroup = (RadioGroup) search_condition_view.findViewById(R.id.radioGroup);
//		radiomonthGroup.setOnCheckedChangeListener(checkedChangeListener);
		timesSpinner = (Spinner) view.findViewById(R.id.times_spinner); 		
		yichuorweichu= (Spinner) view.findViewById(R.id.yichuorweichu);
		btn_search= (Button) view.findViewById(R.id.btn_search);
//		yiChuSearchButton = (Button) search_condition_view.findViewById(R.id.yichi_search);
		query_condition = view.findViewById(R.id.ll_query_condition);
		spinnerView = view.findViewById(R.id.spinner_view);
		ll_upLayout = (LinearLayout) view.findViewById(R.id.ll_upLayout);
		viewpager = (CustomGallery) view.findViewById(R.id.viewPager);
		acc_frame_left = (ImageView) view.findViewById(R.id.acc_frame_left);
		acc_btn_goitem = (ImageView) view.findViewById(R.id.acc_btn_goitem);
		
		btn_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(isSelectWcOrYcButton){
					isSelectWcOrYcButton = true;
					
					// 得到当前卡片账户信息
					beforeWCMap = accountInfoList.get(currentPosition);
					TranDataCenter.getInstance().setAccInInfoMap(beforeWCMap);
					getCurrentDate(beforeWCMap);
					BaseHttpEngine.showProgressDialog();
					requestCommConversationId();
				}else{
					query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
					cleanWeiChuDate();
					cleanMoreButton();
					cleanWCDate();
					isWcSaveLastDate = false;
					isRequestWcDate = false;
					isSelectWcOrYcButton = false;
					isTrans = NOTRANS;
					tag = 1;
					beforeWCMap = accountInfoList.get(currentPosition);
					getCurrentDate(beforeWCMap);
					
					crcd_trans_radioGroup.setVisibility(View.VISIBLE);
					requestCommConversationId();
					BaseHttpEngine.showProgressDialog();
					ll_upLayout.setClickable(false);
					currentIndex = 0;
					sort_text.setText(LocalData.sortMap[0]);
				
				}
			}
		}); 
//		// 未出账单查询按钮
//		btnLoanHistoryQuery.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// 清空已出账单数据
//				query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
//				cleanWeiChuDate();
//				cleanMoreButton();
//				cleanWCDate();
//				isWcSaveLastDate = false;
//				isRequestWcDate = false;
//				isSelectWcOrYcButton = false;
//				isTrans = NOTRANS;
//				tag = 1;
//				beforeWCMap = accountInfoList.get(currentPosition);
//				getCurrentDate(beforeWCMap);
//				requestCommConversationId();
//				BaseHttpEngine.showProgressDialog();
//				ll_upLayout.setClickable(false);
//				currentIndex = 0;
//				sort_text.setText(LocalData.sortMap[0]);
//			}
//		}); 
		// 已出查询按钮
//		yiChuSearchButton.setOnClickListener(ycSearchListener);
		// 收起事件监听
		ll_upLayout.setOnClickListener(upQueryClick);
//		PopupWindowUtils.getInstance().getQueryPopupWindow(search_condition_view, this);
		fromQuery = this.getIntent().getStringExtra("fromQuery");
		
		currentPosition = list_item;
		if ("fromHasQuery".equals(fromQuery)) {
			// 已出账单查询列表
			cardType = CrcdHasTransQueryListActivity.accountType;
			bankSetupList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
					.get(ConstantGloble.CRCD_QUERY_LIST);
			accountInfoList = bankSetupList;
			currentBankList = bankSetupList.get(list_item);
			initViewPager(bankSetupList);

		} else if ("fromNoQuery".equals(fromQuery)) {
			// 未出账单查询列表
//			cardType = CrcdNoTransQueryListActivity.accountType;
			bankSetupList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
					.get(ConstantGloble.CRCD_QUERY_LIST);
			accountInfoList = bankSetupList;
			currentBankList = bankSetupList.get(list_item);
			beforeWCMap = bankSetupList.get(list_item);
			initViewPager(bankSetupList);

		} else if ("fromAccQuery".equals(fromQuery)) {
			// 账户管理页面
			bankSetupList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
					.get(ConstantGloble.CRCD_QUERY_LIST);
			cardType = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTTYPE_RES);
			accountInfoList = bankSetupList;
			currentBankList = bankSetupList.get(list_item);
			beforeWCMap = bankSetupList.get(list_item);
			initViewPager(bankSetupList);
		} else {
			// 我的信用卡
			cardType = MyCreditCardActivity.cardType;
			bankAccountList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
					.get(ConstantGloble.CRCD_BANKACCOUNTLIST);
	
			accountInfoList = bankAccountList;
			currentBankList = bankAccountList.get(list_item);
			beforeWCMap = bankAccountList.get(list_item);
			initViewPager(bankAccountList);
		
		}
		getCurrentDate(currentBankList);
	}

	/** 已出查询按钮 */
	private OnClickListener ycSearchListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			isSelectWcOrYcButton = true;
			LogGloble.d(TAG, "numMonth======" + numMonth);
			LogGloble.d(TAG, "isLastMonth======" + isLastMonth);
			// 得到当前卡片账户信息
			beforeWCMap = accountInfoList.get(currentPosition);
			TranDataCenter.getInstance().setAccInInfoMap(beforeWCMap);
			getCurrentDate(beforeWCMap);
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
		}
	};
	/** 初始化未出账单，已出账单 */
	/** 初始化已出账单月份 */
	private void initSpinner() {
		
		yichuorweichuList = new ArrayList<String>();
		yichuorweichuList.add(getResources().getString(R.string.mycrcd_no_bill));
//		yichuorweichuList.add(getResources().getString(R.string.mycrcd_has_bill));
		yichuorweichuList.addAll(dateList);
		yichuorweichuAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, yichuorweichuList);
		yichuorweichuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		yichuorweichu.setAdapter(yichuorweichuAdapter);
		yichuorweichu.setSelection(0);
		spinnerView.setVisibility(View.GONE);
		
		yichuorweichu.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
			
				if (position == 0) {
					isSelectWcOrYcButton = false;
					spinnerView.setVisibility(View.GONE);
					query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
					ll_upLayout.setVisibility(View.VISIBLE);
					
					// 隐藏已出账单查询结果页面顶部控件
					hideYcView();
					isWcSaveLastDate = true;
					isTrans = NOTRANS;
//					changeLoanBtn1();
					// 得到未出账单--账户数据
					if (!StringUtil.isNullOrEmpty(beforeWCMap)) {
						getCurrentDate(beforeWCMap);
					}
					// 未出账单已经请求数据，和已出账单切换后，需恢复上次的数据
					if (isRequestWcDate) {
						crcd_trans_radioGroup.setVisibility(View.VISIBLE);
						wcDateSort();
					}
					if (chtAdapter != null) {
						chtAdapter.changeDate(new ArrayList<Map<String, Object>>());
					}
					if (StringUtil.isNullOrEmpty(wcTotalList)) {
						ll_upLayout.setClickable(false);
					} else {
						if (wcDateList == null || wcDateList.isEmpty() || wcDateList.size() <= 0) {
							ll_upLayout.setClickable(false);
						} else {
							setTitle(getResources().getString(R.string.mycrcd_no_bill));
							// 将数据进行反显
							ll_upLayout.setClickable(true);
							dealWcBillSum(wcTotalList);
						}
					}
				} else {
					setTitle(getResources().getString(R.string.card_account_search));
					crcd_trans_radioGroup.setVisibility(View.GONE);
					spinnerView.setVisibility(View.GONE);
					isSelectWcOrYcButton = true;
					query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
					ll_upLayout.setVisibility(View.GONE);
					isTrans = HASTRANS;
//					changeLoanBtn2();
					cleanMoreButton();
					// 隐藏未出账单查询结果页面顶部控件
					hideWcView();
					// 将未出账单查询适配器置为空
					if (ctfAdapter != null) {
						ctfAdapter.changeDate(new ArrayList<Map<String, Object>>());
					}
					
					
					numMonth = yichuorweichu.getSelectedItem().toString();
					if (position == 1) {
						isLastMonth = true;
					} else {
						isLastMonth = false;
					}
					

				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		
		ArrayAdapter<String> timesAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, dateList);
		timesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		timesSpinner.setAdapter(timesAdapter);
		timesSpinner.setSelection(0);
		numMonth = timesSpinner.getSelectedItem().toString();
		isLastMonth = true;
		timesSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
				numMonth = timesSpinner.getSelectedItem().toString();
				if (position == 0) {
					isLastMonth = true;
				} else {
					isLastMonth = false;
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	private void init() {
		LogGloble.d(TAG, "init--");
		acc_query_result_condition = (LinearLayout) view.findViewById(R.id.acc_query_result_condition);
		acc_query_search_condition=(LinearLayout) view.findViewById(R.id.acc_query_search_condition);
		checkedNum = this.getIntent().getIntExtra(Crcd.CRCD_NUM, 0);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 25);
		}
		ll_beedtype = (LinearLayout) view.findViewById(R.id.ll_beedtype);
		crcd_shouru_currency = (TextView) view.findViewById(R.id.crcd_shouru_currency);
		crcd_zhichu_currency = (TextView) view.findViewById(R.id.crcd_zhichu_currency);
		crcd_ying_currencycode = (TextView) view.findViewById(R.id.crcd_ying_currencycode);
		crcd_min_currencycode = (TextView) view.findViewById(R.id.crcd_min_currencycode);

		ll_foreignshouru = (LinearLayout) view.findViewById(R.id.ll_foreignshouru);
		ll_foreignzhichu = (LinearLayout) view.findViewById(R.id.ll_foreignzhichu);

		tv_forei_shouru_money = (TextView) view.findViewById(R.id.tv_forei_shouru_money);
		tv_foreign_shuru_currency = (TextView) view.findViewById(R.id.tv_foreign_shuru_currency);
		tv_foreign_zhichu_money = (TextView) view.findViewById(R.id.tv_foreign_zhichu_money);
		tv_foreign_zhichu_currency = (TextView) view.findViewById(R.id.tv_foreign_zhichu_currency);

		tv_crcd_ying_huan = (TextView) view.findViewById(R.id.tv_crcd_ying_huan);
		tv_crcd_dangqi_low_huan = (TextView) view.findViewById(R.id.tv_crcd_dangqi_low_huan);
		ll_currentyingForeign = (LinearLayout) view.findViewById(R.id.ll_currentyingForeign);
		ll_currentlowmoneyForeign = (LinearLayout) view.findViewById(R.id.ll_currentlowmoneyForeign);

		crcd_forei_ying_huan = (TextView) view.findViewById(R.id.crcd_forei_ying_huan);
		crcd_forei_ying_currencycode = (TextView) view.findViewById(R.id.crcd_forei_ying_currencycode);
		crcd_forei_dangqi_low_huan = (TextView) view.findViewById(R.id.crcd_forei_dangqi_low_huan);
		crcd_foreign_min_currencycode = (TextView) view.findViewById(R.id.crcd_foreign_min_currencycode);
		// 账号
		tv_acc_info_currency_value = (TextView) findViewById(R.id.tv_acc_info_currency_value);
		tv_acc_query_date_value = (TextView) findViewById(R.id.tv_acc_query_date_value);
		tv_crcd_query_pay_value = (TextView) findViewById(R.id.tv_crcd_query_pay_value);

		rl_query_transfer_result = (RelativeLayout) findViewById(R.id.acc_query_result_layout);

		notransferList = new ArrayList<Map<String, Object>>();
		lv_acc_query_result = (ListView) findViewById(R.id.lv_acc_query_result);

		load_more = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.acc_load_more, null);
		Button btn_load_more = (Button) load_more.findViewById(R.id.btn_load_more);
		btn_load_more.setOnClickListener(goMoreClickListener);
		ctfAdapter = new CrcdTransferAdapter(this, notransferList, isShow);
		lv_acc_query_result.setAdapter(ctfAdapter);
		acc_query_result_condition.setOnClickListener(downQueryClick);

		btn_acc_query_transfer = (Button) view.findViewById(R.id.btn_acc_query_transfer);
		// ctfAdapter.setOncrcdTransDivideListener(onCrcdTransDivideListener);
		ll_shouandzhi = (LinearLayout) view.findViewById(R.id.ll_shouandzhi);
		ll_currentandlowmoney = (LinearLayout) view.findViewById(R.id.ll_currentandlowmoney);
		img_crcd_detail = (ImageView) view.findViewById(R.id.img_crcd_detail);

		// 查询结果页面初始化
		sort_text = (Button) view.findViewById(R.id.sort_text);
		sort_text.setText(LocalData.sortMap[0]);
//		ll_sort = (LinearLayout) view.findViewById(R.id.ll_sort);
		crcd_trans_radioGroup=(RadioGroup) view.findViewById(R.id.crcd_trans_radioGroup);
//		// 初始化排序框
//		PopupWindowUtils.getInstance().setOnPullSelecterListener(MyCrcdDetailActivity.this, ll_sort, LocalData.sortMap,
//				null, sortClick);

	}

	/** 请求数据 */
	private void requestDate() {
		if (checkedNum == ALL_REN) {
			isTrans = NOTRANS;
			// 选中未出账单按钮
//			changeLoanBtn1();
			query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
			tag = 1;
			isWcSaveLastDate = false;
			isRequestWcDate = false;
			isSelectWcOrYcButton = false;
			// 默认查询---start
			// requestCommConversationId();
			// 默认查询----end
			// 不进行默认查询
			BaseHttpEngine.dissMissProgressDialog();

		} else if (checkedNum == ALL_FOREIGN) {
			// 查询已出账单
			isTrans = HASTRANS;
			// 选中已出账单按钮
//			changeLoanBtn2();
			isSelectWcOrYcButton = true;
			cleanMoreButton();
			strMonth = QueryDateUtils.getStrNumMonthAgo(dateTime, 0);
			isLastMonth = true;
			BaseHttpEngine.dissMissProgressDialog();
		}
	}

	/** 请求ConversationId */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		if (isSelectWcOrYcButton) {
			// 已出账单查询
			psnCrcdQueryBilledTrans(accountId, numMonth);
		} else {
			if(isWrOrYr){
				// 查询信用卡未出账单   已入账
				requestPsnCrcdQueryFutureBill(accountId);
			}else{
				// 查询信用卡未出账单   未入账	
				requestPsnCrcdQueryUnauthorizedTrans();
			}
			
		}
	}
	/** 未出账单查询 未入账 */
	private void requestPsnCrcdQueryUnauthorizedTrans() {
		// 通讯开始,展示通讯框
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDQUERYUNAUTHORIZEDTRANS);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		params.put(Crcd.CRCD_CURRENTINDEX, String.valueOf(currentIndex));
		params.put(Crcd.CRCD_PAGESIZE, String.valueOf(pageSize));
		params.put(Crcd.CRCD_REFRESH, String.valueOf(true));
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnCrcdQueryUnauthorizedTransCallBack");
		
	}
	public void requestPsnCrcdQueryUnauthorizedTransCallBack(Object object) {

		BiiResponse response = (BiiResponse) object;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		Map<String, Object> returnMap = (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.mycrcd_total_result_empty));			
			ll_upLayout.setClickable(false);
			if (wcDateList != null && !wcDateList.isEmpty()) {
				wcDateList.clear();
			}
			if (notransferList != null && !notransferList.isEmpty()) {
				notransferList.clear();
			}
			acc_query_search_condition.setVisibility(View.VISIBLE);
			acc_query_result_condition.setVisibility(View.GONE);
			

			return;
		}
		notransferList = (List<Map<String, Object>>) returnMap.get(Crcd.CRCD_TRANSATIONLIST);
		if (StringUtil.isNullOrEmpty(notransferList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.mycrcd_total_result_empty));
			ll_upLayout.setClickable(false);
			if (wcDateList != null && !wcDateList.isEmpty()) {
				wcDateList.clear();
			}
			if (notransferList != null && !notransferList.isEmpty()) {
				notransferList.clear();
			}
			acc_query_search_condition.setVisibility(View.VISIBLE);
			acc_query_result_condition.setVisibility(View.GONE);
			return;
		}
		String recordNum = String.valueOf(returnMap.get(Crcd.CRCD_RECORDNUMBER));
		if (StringUtil.isNull(recordNum)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.mycrcd_total_result_empty));
			ll_upLayout.setClickable(false);
			return;
		}
		if (wcDateList != null && !wcDateList.isEmpty()) {
			wcDateList.clear();
		}
		maxNum = Integer.valueOf(recordNum);
		// 查询信用卡未出账未入账单合计
		PsnCrcdQueryUnauthorizedTransTotal(accountId);

		
	}
	



	private void PsnCrcdQueryUnauthorizedTransTotal(String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDQUERYUNAUTHORIZEDTRANSTOTAL);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "PsnCrcdQueryUnauthorizedTransTotalCallBack");
	}

	public void PsnCrcdQueryUnauthorizedTransTotalCallBack(Object object) {


		BiiResponse response = (BiiResponse) object;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		
//		Map<String, Object> returnMap = (Map<String, Object>) body.getResult();
//		if (StringUtil.isNullOrEmpty(returnMap)) {
//			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.mycrcd_total_result_empty));
//			BaseHttpEngine.dissMissProgressDialog();
//			ll_upLayout.setClickable(false);
//			return;
//		}
//		List<Map<String, Object>> lists = (List<Map<String, Object>>) returnMap.get(Crcd.CRCD_CRCDTOTALINCOMELIST);
		List<Map<String, Object>> lists = (List<Map<String, Object>>) body.getResult();
		if(!StringUtil.isNullOrEmpty(totalList)){
			totalList.clear();	
		}
		totalList.addAll(lists);
		if (StringUtil.isNullOrEmpty(totalList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.acc_transferquery_null));
			return;
		} else {
			BaseHttpEngine.dissMissProgressDialog();
			if (wcTotalList != null && !wcTotalList.isEmpty()) {
				wcTotalList.clear();
			}
			query_condition.setBackgroundResource(R.drawable.img_bg_query_no);
			isRequestWcDate = true;
			wcTotalList.addAll(lists);
			showWcBottomDateView();
			dealWcBillSum(wcTotalList);
		}

	

	}
	



	/** 未出账单查询 */
	private void requestPsnCrcdQueryFutureBill(String accountId) {
		// 通讯开始,展示通讯框
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDQUERY_API);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		params.put(Crcd.CRCD_CURRENTINDEX, String.valueOf(currentIndex));
		params.put(Crcd.CRCD_PAGESIZE, String.valueOf(pageSize));
		params.put(Crcd.CRCD_REFRESH, String.valueOf(true));
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnCrcdQueryFutureBillCallBack");

	}

	/** 未出账单查询------回调 */
	public void requestPsnCrcdQueryFutureBillCallBack(Object object) {
		BiiResponse response = (BiiResponse) object;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		Map<String, Object> returnMap = (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnMap)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.mycrcd_total_result_empty));
			BaseHttpEngine.dissMissProgressDialog();
			ll_upLayout.setClickable(false);
			if (wcDateList != null && !wcDateList.isEmpty()) {
				wcDateList.clear();
			}
			if (notransferList != null && !notransferList.isEmpty()) {
				notransferList.clear();
			}
			acc_query_search_condition.setVisibility(View.VISIBLE);
			acc_query_result_condition.setVisibility(View.GONE);
			return;
		}
		notransferList = (List<Map<String, Object>>) returnMap.get(Crcd.CRCD_TRANSATIONLIST);
		if (StringUtil.isNullOrEmpty(notransferList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.mycrcd_total_result_empty));
			ll_upLayout.setClickable(false);
			if (wcDateList != null && !wcDateList.isEmpty()) {
				wcDateList.clear();
			}
			if (notransferList != null && !notransferList.isEmpty()) {
				notransferList.clear();
			}
			acc_query_search_condition.setVisibility(View.VISIBLE);
			acc_query_result_condition.setVisibility(View.GONE);
			return;
		}
		String recordNum = String.valueOf(returnMap.get(Crcd.CRCD_RECORDNUMBER));
		if (StringUtil.isNull(recordNum)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.mycrcd_total_result_empty));
			ll_upLayout.setClickable(false);
			return;
		}
		if (wcDateList != null && !wcDateList.isEmpty()) {
			wcDateList.clear();
		}
		maxNum = Integer.valueOf(recordNum);
		// 查询信用卡未出账单合计
		psnCrcdQueryFutureBillTotalIncome(accountId);

	}

	/**
	 * 未出账单合计 查询未出账单的收入、支出
	 */
	private void psnCrcdQueryFutureBillTotalIncome(String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDQUERYFUTUREBILLTOTALINCOME);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdQueryFutureBillTotalIncomeCallBack");
	}

	/**
	 * 未出账单合计 查询未出账单的收入、支出-----回调
	 */
	public void psnCrcdQueryFutureBillTotalIncomeCallBack(Object object) {

		BiiResponse response = (BiiResponse) object;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		List<Map<String, Object>> lists = (List<Map<String, Object>>) body.getResult();
		if(!StringUtil.isNullOrEmpty(totalList)){
			totalList.clear();	
		}
		totalList.addAll(lists);
		if (StringUtil.isNullOrEmpty(totalList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.acc_transferquery_null));
			return;
		} else {
			BaseHttpEngine.dissMissProgressDialog();
			if (wcTotalList != null && !wcTotalList.isEmpty()) {
				wcTotalList.clear();
			}
			query_condition.setBackgroundResource(R.drawable.img_bg_query_no);
			isRequestWcDate = true;
			wcTotalList.addAll(lists);
			showWcBottomDateView();
			dealWcBillSum(wcTotalList);
			setTitle(this.getString(R.string.mycrcd_no_bill));
		}

	}

	
	
	
	
	/** 未出账单查询底部布局 */
	private void showWcBottomDateView() {
		ll_upLayout.setClickable(true);
//		ll_sort.setVisibility(View.VISIBLE);
//		ll_sort.setClickable(true);		
		crcd_trans_radioGroup.setVisibility(View.VISIBLE);
		crcd_trans_radioGroup.setClickable(true);
		sort_text.setText(LocalData.sortMap[0]);
//		PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
		acc_query_search_condition.setVisibility(View.GONE);
		acc_query_result_condition.setVisibility(View.VISIBLE);
		crcd_trans_radioGroup.setVisibility(View.VISIBLE);
		wcDateList.addAll(notransferList);
		dealWcList(wcDateList);
	}

	/** 处理未出账单查询list */
	private void dealWcList(List<Map<String, Object>> notransferList) {
		// 显示更多
		if (maxNum > pageSize) {
			lv_acc_query_result.addFooterView(load_more);
		}
		rl_query_transfer_result.setVisibility(View.VISIBLE);
		ll_shouandzhi.setVisibility(View.VISIBLE);
		ll_currentandlowmoney.setVisibility(View.GONE);
		img_crcd_detail.setVisibility(View.GONE);
		ll_beedtype.setVisibility(View.GONE);
		ll_currentyingForeign.setVisibility(View.GONE);
		ll_currentlowmoneyForeign.setVisibility(View.GONE);
		// 右上角按钮赋值
		setText(this.getString(R.string.acc_rightbtn_go_main));
		setTextTag(GO_MAIN);
		// 未出账单
//		if(ctfAdapter==null){
			ctfAdapter = new CrcdTransferAdapter(this, notransferList, isShow);
//		}else{
//			ctfAdapter.changeDate(notransferList);
//		}
		ctfAdapter.setIsWrOrYr(isWrOrYr);
		lv_acc_query_result.setAdapter(ctfAdapter);
		// ctfAdapter.setOncrcdTransDivideListener(onCrcdTransDivideListener);
	}

	/** 处理未出账单总计数据 */
	private void dealWcBillSum(List<Map<String, Object>> totalList) {
		tv_acc_info_currency_value.setText(StringUtil.getForSixForString(bankNumber));
		Map<String, Object> map = (Map<String, Object>) totalList.get(0);
		/** 贷方合计 */
		String creditsum = String.valueOf(map.get(Crcd.CRCD_CREDITSUM));
		/** 借方合计 */
		String debitsum = String.valueOf(map.get(Crcd.CRCD_DEBITSUM));
		String currency = String.valueOf(map.get(Crcd.CRCD_CURRENCY));
		String strCurrcyCode = LocalData.Currency.get(currency);
		tv_acc_query_date_value.setText(StringUtil.parseStringCodePattern(currency, creditsum, 2));
		tv_crcd_query_pay_value.setText(StringUtil.parseStringCodePattern(currency, debitsum, 2));
		crcd_shouru_currency.setText(strCurrcyCode);
		crcd_zhichu_currency.setText(strCurrcyCode);

		if (totalList.size() == 1) {
			ll_foreignshouru.setVisibility(View.GONE);
			ll_foreignzhichu.setVisibility(View.GONE);
		}

		if (totalList.size() > 1) {
			Map<String, Object> map1 = (Map<String, Object>) totalList.get(1);
			String foreignCreditsum = String.valueOf(map1.get(Crcd.CRCD_CREDITSUM));
			String foreignDebitsum = String.valueOf(map1.get(Crcd.CRCD_DEBITSUM));
			currency = String.valueOf(map1.get(Crcd.CRCD_CURRENCY));
			strCurrcyCode = LocalData.Currency.get(currency);

			ll_foreignshouru.setVisibility(View.VISIBLE);
			ll_foreignzhichu.setVisibility(View.VISIBLE);

			tv_forei_shouru_money.setText(StringUtil.parseStringCodePattern(currency, foreignCreditsum, 2));
			tv_foreign_zhichu_money.setText(StringUtil.parseStringCodePattern(currency, foreignDebitsum, 2));
			tv_foreign_shuru_currency.setText(strCurrcyCode);
			tv_foreign_zhichu_currency.setText(strCurrcyCode);
		}
	}

	/** 点击“更多”----未出账单查询 */
	public void requestPsnCrcdQueryFutureBillForMore(String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDQUERY_API);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		params.put(Crcd.CRCD_CURRENTINDEX, String.valueOf(currentIndex));
		params.put(Crcd.CRCD_PAGESIZE, String.valueOf(pageSize));
		params.put(Crcd.CRCD_REFRESH, String.valueOf(false));
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnCrcdQueryFutureBillCallBackForMore");
	}

	/** 点击“更多”----未出账单查询-----回调 */
	public void requestPsnCrcdQueryFutureBillCallBackForMore(Object object) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse response = (BiiResponse) object;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		Map<String, Object> returnMap = (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnMap)) {
			return;
		}
		if (!returnMap.containsKey(Crcd.CRCD_TRANSATIONLIST)) {
			return;
		}
		List<Map<String, Object>> lists = (List<Map<String, Object>>) returnMap.get(Crcd.CRCD_TRANSATIONLIST);
		if (lists == null || lists.size() <= 0) {
			return;
		}
//		PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
		acc_query_search_condition.setVisibility(View.GONE);
		acc_query_result_condition.setVisibility(View.VISIBLE);
		notransferList.addAll(lists);
		wcDateList.addAll(lists);
		String recordNum = String.valueOf(returnMap.get(Crcd.CRCD_RECORDNUMBER));
		maxNum = Integer.valueOf(recordNum);

		if ((currentIndex + pageSize) >= maxNum) {
			lv_acc_query_result.removeFooterView(load_more);
		}
//		ll_sort.setVisibility(View.VISIBLE);
//		ll_sort.setClickable(true);
		crcd_trans_radioGroup.setVisibility(View.VISIBLE);
		crcd_trans_radioGroup.setClickable(true);
		sort_text.setText(LocalData.sortMap[0]);
		ctfAdapter.changeDate(notransferList);
		// ctfAdapter.setOncrcdTransDivideListener(onCrcdTransDivideListener);
	}

	/** 查询已出账单 */
	public void psnCrcdQueryBilledTrans(String accountId, String month) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDQUERYBIIEDTRANSN_API);
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTIDN_REQ, accountId);
		params.put(Crcd.CRCD_STATEMENTMONTHN_REQ, month);

		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnCrcdQueryBilledTransCallBack");
	}

	/** 查询已出账单-----回调 */
	public void requestPsnCrcdQueryBilledTransCallBack(Object obj) {
		BiiResponse response = (BiiResponse) obj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		Map<String, Object> result = (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.acc_transferquery_null));
			ll_upLayout.setClickable(false);
			return;
		}
		ll_upLayout.setClickable(false);
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.CRCD_YCTRANS_RESULT, result);
		Intent intent = new Intent(this, CrcdTransDetailActivity.class);
		intent.putExtra(ConstantGloble.CRCD_ISLASTMONTH, isLastMonth);
		intent.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		intent.putExtra(Crcd.CRCD_ACCOUNTTYPE_RES, cardType);
		intent.putExtra(Tran.ACCOUNTIBKNUM_RES, accountIbkNum);
		intent.putExtra(Crcd.CRCD_NICKNAME_RES, nickName);	
		intent.putExtra(Crcd.CRCD_STATEMENTMONTHN_REQ, numMonth);
		intent.putExtra(Crcd.CRCD_ISBILLEXIST_REQ, isBillExist);		
		startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_CHECKINFUNCACC_CODE);
		BaseHttpEngine.dissMissProgressDialog();
	}

	public boolean httpRequestCallBackPre(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码

			if (Crcd.CRCD_PSNCRCDQUERYBIIEDTRANSN_API.equals(biiResponseBody.getMethod())) {
				if (biiResponse.isBiiexception()) {// 代表返回数据异常
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null) {
						if (biiError.getCode() != null) {
							if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
								// 要重新登录
								showTimeOutDialog(biiError.getMessage());
							} else {
								// 非会话超时错误拦截
								BaseDroidApp.getInstanse().createDialog("", biiError.getMessage(),
										new OnClickListener() {

											@Override
											public void onClick(View v) {
												BaseDroidApp.getInstanse().dismissMessageDialog();
												BaseHttpEngine.dissMissProgressDialog();
												if (isInitShowPop) {
//													PopupWindowUtils.getInstance().showQueryPopupWindow();
													acc_query_search_condition.setVisibility(View.VISIBLE);
													acc_query_result_condition.setVisibility(View.GONE);
													isInitShowPop = false;
												}
												ll_upLayout.setClickable(false);
											}
										});
								return true;
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
	};

//	private void showTimeOutDialog(BiiError biiError) {
//		BaseDroidApp.getInstanse().showMessageDialog(biiError.getMessage(), new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				BaseDroidApp.getInstanse().dismissErrorDialog();
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(MyCrcdDetailActivity.this, LoginActivity.class);
//				startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
//			}
//		});
//	}

	/** 未出账单排序---恢复数据 */
	private void wcDateSort() {
		if (tag == 1) {
			// 选择全部
			sort_text.setText(LocalData.sortMap[0]);
			wcAllSort();
		} else if (tag == 2) {
			// 选择收入
			sort_text.setText(LocalData.sortMap[1]);
			wcInSort();
		} else if (tag == 3) {
			// 选择支出
			sort_text.setText(LocalData.sortMap[2]);
			wcOutSort();
		}
	}

	OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.loan_his_btn1:// 未出账单按钮
				query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
				ll_upLayout.setVisibility(View.VISIBLE);
				isSelectWcOrYcButton = false;
				// 隐藏已出账单查询结果页面顶部控件
				hideYcView();
				isWcSaveLastDate = true;
				isTrans = NOTRANS;
//				changeLoanBtn1();
				// 得到未出账单--账户数据
				if (!StringUtil.isNullOrEmpty(beforeWCMap)) {
					getCurrentDate(beforeWCMap);
				}
				// 未出账单已经请求数据，和已出账单切换后，需恢复上次的数据
				if (isRequestWcDate) {
					wcDateSort();
				}
				if (chtAdapter != null) {
					chtAdapter.changeDate(new ArrayList<Map<String, Object>>());
				}
				if (StringUtil.isNullOrEmpty(wcTotalList)) {
					ll_upLayout.setClickable(false);
				} else {
					if (wcDateList == null || wcDateList.isEmpty() || wcDateList.size() <= 0) {
						ll_upLayout.setClickable(false);
					} else {
						// 将数据进行反显
						ll_upLayout.setClickable(true);
						dealWcBillSum(wcTotalList);
					}
				}
				break;
			case R.id.loan_his_btn2:// 已出账单按钮-----没有分页
				isSelectWcOrYcButton = true;
				query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
				ll_upLayout.setVisibility(View.GONE);
				isTrans = HASTRANS;
//				changeLoanBtn2();
				cleanMoreButton();
				// 隐藏未出账单查询结果页面顶部控件
				hideWcView();
				// 将未出账单查询适配器置为空
				if (ctfAdapter != null) {
					ctfAdapter.changeDate(new ArrayList<Map<String, Object>>());
				}

				break;

			}
		};

	};
	/** 未出账单-----更多监听事件，已出账单---不分页 */
	OnClickListener goMoreClickListener = new OnClickListener() {
		public void onClick(View v) {
			if (!StringUtil.isNullOrEmpty(beforeWCMap)) {
				getCurrentDate(beforeWCMap);
			}

			// 已出、未出相互切换时，保留上次的数据，需将保留的数据赋值给notransferList
			if (notransferList.isEmpty() && wcDateList.size() > 0) {
				for (int i = 0; i < wcDateList.size(); i++) {
					Map<String, Object> map = wcDateList.get(i);
					notransferList.add(map);
				}
			}
			currentIndex += pageSize;
			// 查询信用卡未出账单
			BaseHttpEngine.showProgressDialog();
			
			if(isWrOrYr){
				requestPsnCrcdQueryFutureBillForMore(accountId);
			}else{
				// 查询信用卡未出账单   未入账	
				requestPsnCrcdQueryUnauthorizedTransForMore(accountId);
			}
			
		};
	};
	
	protected void requestPsnCrcdQueryUnauthorizedTransForMore(String accountId) {

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDQUERYUNAUTHORIZEDTRANS);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		params.put(Crcd.CRCD_CURRENTINDEX, String.valueOf(currentIndex));
		params.put(Crcd.CRCD_PAGESIZE, String.valueOf(pageSize));
		params.put(Crcd.CRCD_REFRESH, String.valueOf(false));
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnCrcdQueryUnauthorizedTransMoreForMore");
	
		
	}
	

	/** 点击“更多”----未出账单查询-----回调 */
	public void requestPsnCrcdQueryUnauthorizedTransMoreForMore(Object object) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse response = (BiiResponse) object;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		Map<String, Object> returnMap = (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnMap)) {
			return;
		}
		if (!returnMap.containsKey(Crcd.CRCD_TRANSATIONLIST)) {
			return;
		}
		List<Map<String, Object>> lists = (List<Map<String, Object>>) returnMap.get(Crcd.CRCD_TRANSATIONLIST);
		if (lists == null || lists.size() <= 0) {
			return;
		}
//		PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
		acc_query_search_condition.setVisibility(View.GONE);
		acc_query_result_condition.setVisibility(View.VISIBLE);
		notransferList.addAll(lists);
		wcDateList.addAll(lists);
		String recordNum = String.valueOf(returnMap.get(Crcd.CRCD_RECORDNUMBER));
		maxNum = Integer.valueOf(recordNum);

		if ((currentIndex + pageSize) >= maxNum) {
			lv_acc_query_result.removeFooterView(load_more);
		}
//		ll_sort.setVisibility(View.VISIBLE);
//		ll_sort.setClickable(true);
		crcd_trans_radioGroup.setVisibility(View.VISIBLE);
		crcd_trans_radioGroup.setClickable(true);
//		sort_text.setText(LocalData.sortMap[0]);
		ctfAdapter.changeDate(notransferList);
		// ctfAdapter.setOncrcdTransDivideListener(onCrcdTransDivideListener);
	}
	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (GO_MAIN.equals(v.getTag())) {
				// 回主页面
//				Intent intent = new Intent(MyCrcdDetailActivity.this, MainActivity.class);
//				startActivity(intent);
//				ActivityTaskManager.getInstance().removeAllActivity();
				goToMainActivity();
			} else if (GO_DIVIDED.equals(v.getTag())) {
				// 当期账单总计
				Intent it = new Intent(MyCrcdDetailActivity.this, CrcdTransDetailActivity.class);
				it.putExtra("month", titlePreix);
				startActivity(it);
			}
		}
	};
	/** 隐藏查询条件 */
	OnClickListener upQueryClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
//			ll_sort.setClickable(true);
			crcd_trans_radioGroup.setClickable(true);
//			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
		
//			
//			animation_up.setAnimationListener(new AnimationListener() {
//				@Override
//				public void onAnimationStart(Animation animation) {
//				}
//
//				@Override
//				public void onAnimationRepeat(Animation animation) {
//				}
//
//				@Override
//				public void onAnimationEnd(Animation animation) {
					
					
					acc_query_result_condition.setVisibility(View.VISIBLE);
					acc_query_search_condition.setVisibility(View.GONE);
//				}
//			});
//			acc_query_search_condition.startAnimation(animation_up);
//			
		
		}
	};

	/** 显示查询条件 */
	OnClickListener downQueryClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
//			ll_sort.setClickable(false);
			crcd_trans_radioGroup.setClickable(false);
//			PopupWindowUtils.getInstance().showQueryPopupWindow();
			
//			animation_down.setAnimationListener(new AnimationListener() {
//				@Override
//				public void onAnimationStart(Animation animation) {
//				}
//
//				@Override
//				public void onAnimationRepeat(Animation animation) {
//				}
//
//				@Override
//				public void onAnimationEnd(Animation animation) {
					acc_query_search_condition.setVisibility(View.VISIBLE);
					acc_query_result_condition.setVisibility(View.GONE);
//				}
//			});
//			
//			acc_query_search_condition.startAnimation(animation_down);
			
		}
	};


	/** 隐藏未出账单控件 */
	private void hideWcView() {
		ll_shouandzhi.setVisibility(View.GONE);
		ll_currentandlowmoney.setVisibility(View.VISIBLE);
		img_crcd_detail.setVisibility(View.VISIBLE);

	}

	/** 隐藏已出账单控件 */
	private void hideYcView() {
		ll_shouandzhi.setVisibility(View.VISIBLE);
		ll_currentandlowmoney.setVisibility(View.GONE);
		img_crcd_detail.setVisibility(View.INVISIBLE);
	}

	/** 移除更多按钮 */
	private void cleanMoreButton() {
		if (lv_acc_query_result.getFooterViewsCount() > 0) {
			lv_acc_query_result.removeFooterView(load_more);
		}

	}

	/** 清空未出账单数据 */
	private void cleanWeiChuDate() {
		if (notransferList != null && !notransferList.isEmpty()) {
			notransferList.clear();
			if (ctfAdapter != null) {
				ctfAdapter.changeDate(new ArrayList<Map<String, Object>>());
			}

		}
	}

	/** 清空未出账单保留的数据 */
	private void cleanWCDate() {
		if (wcDateList != null && !wcDateList.isEmpty()) {
			wcDateList.clear();
		}
		if (wcTotalList != null && !wcTotalList.isEmpty()) {
			wcTotalList.clear();
		}
	}

	/** 未出账单 */
//	public void changeLoanBtn1() {
//		loanradiobtn1.setTextColor(Color.WHITE);
//		loanradiobtn1.setChecked(true);
//		btnLoanHistoryQuery.setVisibility(View.VISIBLE);
//		spinnerView.setVisibility(View.GONE);
//		loanradiobtn2.setTextColor(Color.BLACK);
//	}

	/** 已出账单 */
//	public void changeLoanBtn2() {
//		loanradiobtn2.setTextColor(Color.WHITE);
//		loanradiobtn2.setChecked(true);
//		loanradiobtn1.setTextColor(Color.BLACK);
//		btnLoanHistoryQuery.setVisibility(View.GONE);
//		spinnerView.setVisibility(View.VISIBLE);
//	}

	/** 查询条件页面---账户卡片 */
	public void initViewPager(final List<Map<String, Object>> bankAccountList) {
		AccTransGalleryAdapter adapter = new AccTransGalleryAdapter(this, bankAccountList);
		viewpager.setAdapter(adapter);
		viewpager.setSelection(list_item);
		getPosition(list_item, bankAccountList);
		viewpager.setOnItemSelectedListener(viewPageListener);
	}

	private OnItemSelectedListener viewPageListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
			LogGloble.d(TAG + "viewPageListener======position", "position=======" + position);
			currentPosition = position;
			if ("fromHasQuery".equals(fromQuery) || "fromNoQuery".equals(fromQuery) || "fromAccQuery".equals(fromQuery)) {
				// 已出账单、未出账单
				currentBankList = bankSetupList.get(position);
				getPosition(position, bankSetupList);
			} else {
				// 我的信用卡
				currentBankList = bankAccountList.get(position);
				getPosition(position, bankAccountList);
			}
			
			if(!isfirstin){
				String accountId=String.valueOf(currentBankList.get(Crcd.CRCD_ACCOUNTID_RES));
				PsnQueryCrcdBillIsExist(accountId);		
			}
			isfirstin=false;
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
	};

	/** 获取当前卡片的数据 */
	private void getCurrentDate(Map<String, Object> currentBankList) {
		accountId = String.valueOf(currentBankList.get(Crcd.CRCD_ACCOUNTID_RES));
		bankName = String.valueOf(currentBankList.get(Crcd.CRCD_ACCOUNTNAME_RES));
		nickName = String.valueOf(currentBankList.get(Crcd.CRCD_NICKNAME_RES));
		bankNumber = String.valueOf(currentBankList.get(Crcd.CRCD_ACCOUNTNUMBER_RES));
		currency = String.valueOf(currentBankList.get(Crcd.CRCD_CURRENCYCODE));
		accountIbkNum=String.valueOf(currentBankList.get(Tran.ACCOUNTIBKNUM_RES));
		strCurrency = LocalData.Currency.get(currency);
	}

	private void getPosition(int position, List<Map<String, Object>> bankAccountList) {
		if (position == 0) {
			acc_frame_left.setVisibility(View.INVISIBLE);
			if (bankAccountList.size() == 1) {
				acc_btn_goitem.setVisibility(View.INVISIBLE);
			} else {
				acc_btn_goitem.setVisibility(View.VISIBLE);
			}
		} else if (position == bankAccountList.size() - 1) {
			acc_frame_left.setVisibility(View.VISIBLE);
			acc_btn_goitem.setVisibility(View.INVISIBLE);
		} else {
			acc_frame_left.setVisibility(View.VISIBLE);
			acc_btn_goitem.setVisibility(View.VISIBLE);
		}
	}

	/** 未出账单--排序--全部 */
	private void soreAllWcDate(List<Map<String, Object>> notransferList) {
		if (notransferList == null || notransferList.size() <= 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_nullin));
			return;
		}
		if (maxNum > notransferList.size()) {
			if (lv_acc_query_result.getFooterViewsCount() > 0) {

			} else {
				lv_acc_query_result.addFooterView(load_more);
			}
		}
		if (ctfAdapter != null) {
			ctfAdapter.changeDate(new ArrayList<Map<String, Object>>());
		}
		ctfAdapter = new CrcdTransferAdapter(MyCrcdDetailActivity.this, notransferList, isShow);
		ctfAdapter.setIsWrOrYr(isWrOrYr);
		lv_acc_query_result.setAdapter(ctfAdapter);
		ctfAdapter.notifyDataSetChanged();
	}

	/** 未出账单---全部排序 */
	private void wcAllSort() {
		// 未出账单---全部排序
		if (isWcSaveLastDate) {
			soreAllWcDate(wcDateList);
		} else {
			soreAllWcDate(notransferList);
		}
	}

	/** 未出账单---收入排序 */
	private void wcInSort() {
		cleanMoreButton();
		// 清空数据
		if (ctfAdapter != null) {
			ctfAdapter.changeDate(new ArrayList<Map<String, Object>>());
		}
		List<Map<String, Object>> wcInSortlist = null;
		if (isWcSaveLastDate) {
			wcInSortlist = SortUtils.getIn(wcDateList);

		} else {
			wcInSortlist = SortUtils.getIn(notransferList);
		}
		if (wcInSortlist == null || wcInSortlist.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_nullin));
			return;
		}
		// 未出账单---收入排序
		ctfAdapter = new CrcdTransferAdapter(MyCrcdDetailActivity.this, wcInSortlist, isShow);
		ctfAdapter.setIsWrOrYr(isWrOrYr);
		lv_acc_query_result.setAdapter(ctfAdapter);
	}

	/** 未出账单---支出排序 */
	private void wcOutSort() {
		cleanMoreButton();
		if (ctfAdapter != null) {
			ctfAdapter.changeDate(new ArrayList<Map<String, Object>>());
		}
		List<Map<String, Object>> wcOutSortlist = null;
		if (isWcSaveLastDate) {
			wcOutSortlist = SortUtils.getOut(wcDateList);
		} else {
			wcOutSortlist = SortUtils.getOut(notransferList);
		}
		if (wcOutSortlist == null || wcOutSortlist.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_nullout));
			return;
		}
		// 未出账单---支出排序
		ctfAdapter = new CrcdTransferAdapter(MyCrcdDetailActivity.this, wcOutSortlist, isShow);
		ctfAdapter.setIsWrOrYr(isWrOrYr);
		lv_acc_query_result.setAdapter(ctfAdapter);
	}

	/** 排序点击事件 */
	OnClickListener sortClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_text1:
				isShow = 2;
				// 全部
				sort_text.setText(LocalData.sortMap[0]);
				tag = 1;
				if (isTrans == NOTRANS) {
					wcAllSort();
				}
				break;
			case R.id.tv_text2:
				// 收入
				sort_text.setText(LocalData.sortMap[1]);
				tag = 2;
				if (lv_acc_query_result.getFooterViewsCount() > 0) {
					lv_acc_query_result.removeFooterView(load_more);
				}
				if (isTrans == NOTRANS) {
					wcInSort();
				}

				break;
			case R.id.tv_text3:
				// 支出
				sort_text.setText(LocalData.sortMap[2]);
				tag = 3;
				if (lv_acc_query_result.getFooterViewsCount() > 0) {
					lv_acc_query_result.removeFooterView(load_more);
				}
				if (isTrans == NOTRANS) {
					wcOutSort();

				}
				break;
			}

		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ConstantGloble.ACTIVITY_REQUEST_CHECKINFUNCACC_CODE:// 已出账单查询
			LogGloble.d(TAG, "onActivityResult-----");
//			PopupWindowUtils.getInstance().showQueryPopupWindow();
			acc_query_search_condition.setVisibility(View.VISIBLE);
			acc_query_result_condition.setVisibility(View.GONE);
			break;

		default:
			break;
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(3);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	// 603 add
	public void btnCardTransYrOnclick(View v){
		isWrOrYr=true;
		currentIndex=0;
		clearListView();
		BaseHttpEngine.showProgressDialog();
		requestCommConversationId();
	}
	public void btnCardTransWrOnclick(View v){
		isWrOrYr=false;
		currentIndex=0;
		clearListView();
		BaseHttpEngine.showProgressDialog();
		requestCommConversationId();
	}
	/**
	 * 清空listview数据 刷新listview视图
	 */
	private void clearListView() {
		// 将listview数据情况		
		if (ctfAdapter != null) {
			ctfAdapter.changeDate(new ArrayList<Map<String, Object>>());
			cleanMoreButton();

		}
	}
}