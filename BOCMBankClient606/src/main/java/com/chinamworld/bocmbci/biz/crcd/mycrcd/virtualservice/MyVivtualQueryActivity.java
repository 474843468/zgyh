package com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.MainActivity;
import com.chinamworld.bocmbci.biz.crcd.CrcdAccBaseActivity;
import com.chinamworld.bocmbci.biz.crcd.adapter.CrcdTransferAdapter;
import com.chinamworld.bocmbci.biz.crcd.adapter.CrcdVirtualQueryAdapter;
import com.chinamworld.bocmbci.biz.crcd.utils.SortUtils;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomGallery;

/**
 * 虚拟卡账单明细查询
 * 
 * @author huangyuchao
 * 
 */
public class MyVivtualQueryActivity extends CrcdAccBaseActivity {

	private static final String TAG = "MyVivtualQueryActivity";
	/** 虚拟卡账单明细查询界面视图 */
	private View view;

	/** 查询条件收起三角 */
	private ImageView img_up;

	ListView lv_acc_query_result;

	LinearLayout query_condition;

	private int isShow = 1;

	/** 排序布局 */
	private Button sort_text;
	private LinearLayout ll_sort;
	private ImageView img_sort_icon;
	public String bankName;
	public String nickName;
	public String bankNumber;

	Button btnLoanHistoryQuery;

	RadioGroup radiomonthGroup;
	RadioButton month_btn1;
	RadioButton month_btn2;
	RadioButton month_btn3;
	RadioButton month_btn4;

	ImageView query_down;
	/** 查询条件整个布局 */
//	private View rl_query_transfer_view;

//	RelativeLayout rl_query_transfer_result;

	RadioGroup radioloanGroup;
	RadioButton loanradiobtn1, loanradiobtn2;

	/** 查询结果头下拉三角 */
	private ImageView img_down;

	String[] sArray = new String[] { "人民币" };

	TextView tv_acc_info_currency_value, tv_acc_info_cashremit_value, tv_acc_query_date_value, tv_crcd_query_pay_value;

	LinearLayout ll_shouandzhi, ll_currentandlowmoney;

	TextView tv_crcd_ying_huan, tv_crcd_dangqi_low_huan;

	private int isTrans = 0;
	/** 已出账单 */
	private static final int HASTRANS = 1;
	/** 未出账单 */
	private static final int NOTRANS = 2;

	public static String numMonth = "";
	private static String strMonth = "";

	TextView crcd_shouru_currency, crcd_zhichu_currency;
	TextView crcd_ying_currencycode, crcd_min_currencycode;

	private CustomGallery viewpager;
	int list_item;

	LinearLayout acc_query_result_condition,acc_query_search_condition;

	LinearLayout ll_uplayout;
	/** 虚拟银行卡列表 */
	List<Map<String, Object>> virCardList;
	/** 已出账单详情 */
	ImageView img_crcd_detail;

	String cardType;

	LinearLayout ll_foreignzhichu, ll_foreignshouru;
	TextView tv_forei_shouru_money, tv_foreign_shuru_currency, tv_foreign_zhichu_money, tv_foreign_zhichu_currency;

	LinearLayout ll_currentyingForeign, ll_currentlowmoneyForeign;
	TextView crcd_forei_ying_huan, crcd_forei_ying_currencycode, crcd_forei_dangqi_low_huan,
			crcd_foreign_min_currencycode;

	String strCurrencyCode;

	RelativeLayout load_more;
	/** 当前用户选择的虚拟卡 */
	Map<String, Object> virCardItem = new HashMap<String, Object>();
	String virtualCardNo = null;
	private ImageView acc_frame_left;
	private ImageView acc_btn_goitem;

	/** 虚拟卡----未出账单合计查询----结果 */
	List<Map<String, Object>> totalList;
	String accountId;

	protected static String currencyCode;
	static Map<String, Object> returnMap = new HashMap<String, Object>();
	static Map<String, Object> returnMap1 = new HashMap<String, Object>();
	/** 已出账单查询结果 */
	List<Map<String, Object>> transList = new ArrayList<Map<String, Object>>();
	/** 账单查询适配器 */
	CrcdTransferAdapter ctfAdapter;
	/** 保留上次未出账单数据 */
	private List<Map<String, Object>> wcDateList = null;
	/** 保留上次未出账单总计数据 */
	private List<Map<String, Object>> wcTotalList = null;
	/** 保留上次已出账单数据 */
	private List<Map<String, Object>> ycDateList = null;
	private boolean isInit = true;
	private int currentIndex = 0;
	private int pageSize = 10;
	private int maxNum;
	private boolean isShowPop = true;
	private boolean isWcSaveLastDate = false;
	private boolean isYcSaveLastDate = false;

	private Map<String, Object> beforeWCMap = null;
	private Map<String, Object> beforeYCMap = null;
	private int currentPosition = -1;
	/** 未出账单排序----1全部，2收入，3支出 */
	private int wcSort = 1;
	/** 已出账单排序----1全部，2收入，3支出 */
	private int ycSort = 1;
	/** 未出账单排序--- 1全部，2收入，3支出 */
	private int tag = 1;
	/** 已出账单排序--- 1全部，2收入，3支出 */
	private int ycTag = 1;
	/** 未出是否请求数据 */
	private boolean isRequestWcDate = false;
	/** 已出是否请求数据 */
	private boolean isRequestYcDate = false;
	/** false-未出,true-已出 */
	private boolean isSelectWcOrYcButton = false;
	/** 账户户名 */
	private String accountName = null;
	private View shitiCrcdView = null;
	/** true-信用卡,false-账户管理 */
	private boolean isAccOrCrcd = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_virtual_myxunicard_detial));
		// 右上角按钮赋值
		setText(this.getString(R.string.acc_rightbtn_go_main));
		view = addView(R.layout.mycrcd_vertailquery_detail);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(4);
				finish();
			}
		});
		setLeftSelectedPosition("myCrcd_4");
		isAccOrCrcd = getIntent().getBooleanExtra(ConstantGloble.FALSE, false);
		virCardList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CRCD_VIRCRCDLIST);
		if (virCardList != null && virCardList.size() > 0) {

		} else {
			return;
		}
		virtualCardNo = getIntent().getStringExtra(Crcd.CRCD_VIRTUALCARDNO);
		list_item = this.getIntent().getIntExtra(ConstantGloble.ACC_POSITION, 0);
		accountName = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNAME_RES);
		if (StringUtil.isNull(accountName)) {
			return;
		}
		isTrans = NOTRANS;
		wcDateList = new ArrayList<Map<String, Object>>();
		ycDateList = new ArrayList<Map<String, Object>>();
		wcTotalList = new ArrayList<Map<String, Object>>();
		BaseHttpEngine.showProgressDialogCanGoBack();
		// 请求系统时间
		requestSystemDateTime();

	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		initCondition();
		init();
		initPage();
//		PopupWindowUtils.getInstance().showQueryPopupWindowFirst();
		acc_query_search_condition.setVisibility(View.VISIBLE);
		isInit = false;
		isWcSaveLastDate = false;
		isRequestWcDate = false;
		// 默认查询start
		// 请求Conversation
		//requestCommConversationId();
		// 默认查询end
		BaseHttpEngine.dissMissProgressDialog();
	}

	private void initCondition() {
//		rl_query_transfer_view = LayoutInflater.from(this).inflate(R.layout.crcd_virtual_query_condition, null);
		acc_frame_left = (ImageView) view.findViewById(R.id.acc_frame_left);
		acc_btn_goitem = (ImageView) view.findViewById(R.id.acc_btn_goitem);
		viewpager = (CustomGallery) view.findViewById(R.id.viewPager);
		radioloanGroup = (RadioGroup) view.findViewById(R.id.radioloanGroup);
		query_condition = (LinearLayout) view.findViewById(R.id.ll_query_condition);
		loanradiobtn1 = (RadioButton) view.findViewById(R.id.loan_his_btn1);
		loanradiobtn1.setChecked(true);
		loanradiobtn2 = (RadioButton) view.findViewById(R.id.loan_his_btn2);
		radioloanGroup.setOnCheckedChangeListener(checkedChangeListener);
		radiomonthGroup = (RadioGroup) view.findViewById(R.id.radiomonthGroup);
		month_btn1 = (RadioButton) view.findViewById(R.id.month_btn1);
		month_btn2 = (RadioButton) view.findViewById(R.id.month_btn2);
		month_btn3 = (RadioButton) view.findViewById(R.id.month_btn3);
		month_btn4 = (RadioButton) view.findViewById(R.id.month_btn4);
		month_btn1.setText(QueryDateUtils.getInstance().getStrNumMonthAgo(dateTime, 0));
		month_btn2.setText(QueryDateUtils.getInstance().getStrNumMonthAgo(dateTime, -1));
		month_btn3.setText(QueryDateUtils.getInstance().getStrNumMonthAgo(dateTime, -2));
		month_btn4.setText(QueryDateUtils.getInstance().getStrNumMonthAgo(dateTime, -3));
		radiomonthGroup.setOnCheckedChangeListener(checkedChangeListener);
		btnLoanHistoryQuery = (Button) view.findViewById(R.id.btnLoanHistoryQuery);
		// 未出账单查询按钮
		btnLoanHistoryQuery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				beforeWCMap = virCardList.get(currentPosition);
				getAccinfoMassage(beforeWCMap);
				query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
				cleanMore();
				clearWeiChuDate();
				cleanWCDate();
				requestCommConversationId();
				isWcSaveLastDate = false;
				isRequestWcDate = false;
				isSelectWcOrYcButton = false;
				tag = 1;
				BaseHttpEngine.showProgressDialog();
				ll_uplayout.setClickable(false);
				isTrans = NOTRANS;
				currentIndex = 0;

				sort_text.setText(LocalData.sortMap[0]);
			}
		});
		ll_uplayout = (LinearLayout) view.findViewById(R.id.ll_uplayout);
		ll_uplayout.setOnClickListener(upQueryClick);
//		PopupWindowUtils.getInstance().getQueryPopupWindow(rl_query_transfer_view, this);
	}

	private void init() {
		lv_acc_query_result = (ListView) findViewById(R.id.lv_acc_query_result);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 20);
		}
		shitiCrcdView = findViewById(R.id.shiti_crcd_layout);
		if (isAccOrCrcd) {
			// 选择虚拟卡页面
			shitiCrcdView.setVisibility(View.VISIBLE);
		} else {
			// 账户管理
			shitiCrcdView.setVisibility(View.GONE);
		}
		load_more = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.acc_load_more, null);
		Button btn_load_more = (Button) load_more.findViewById(R.id.btn_load_more);
		btn_load_more.setOnClickListener(goMoreClickListener);

		ll_foreignshouru = (LinearLayout) view.findViewById(R.id.ll_foreignshouru);
		ll_foreignzhichu = (LinearLayout) view.findViewById(R.id.ll_foreignzhichu);
		tv_forei_shouru_money = (TextView) view.findViewById(R.id.tv_forei_shouru_money);
		tv_foreign_shuru_currency = (TextView) view.findViewById(R.id.tv_foreign_shuru_currency);
		tv_foreign_zhichu_money = (TextView) view.findViewById(R.id.tv_foreign_zhichu_money);
		tv_foreign_zhichu_currency = (TextView) view.findViewById(R.id.tv_foreign_zhichu_currency);

		ll_currentyingForeign = (LinearLayout) view.findViewById(R.id.ll_currentyingForeign);
		ll_currentlowmoneyForeign = (LinearLayout) view.findViewById(R.id.ll_currentlowmoneyForeign);
		crcd_forei_ying_huan = (TextView) view.findViewById(R.id.crcd_forei_ying_huan);
		crcd_forei_ying_currencycode = (TextView) view.findViewById(R.id.crcd_forei_ying_currencycode);
		crcd_forei_dangqi_low_huan = (TextView) view.findViewById(R.id.crcd_forei_dangqi_low_huan);
		crcd_foreign_min_currencycode = (TextView) view.findViewById(R.id.crcd_foreign_min_currencycode);
		img_up = (ImageView) view.findViewById(R.id.acc_query_up);
		// int position = MyVirtualBCListActivity.accNum - 1;
		// // 刚进入时，当前账户为选择的账户
		// if (bankSetupList.size() > 0) {
		// currentBankList = bankSetupList.get(position);
		// }
		// bankName = (String) currentBankList.get(Crcd.CRCD_ACCOUNTNAME_RES);
		// nickName = (String) currentBankList.get(Crcd.CRCD_NICKNAME_RES);
		// bankNumber = (String)
		// currentBankList.get(Crcd.CRCD_ACCOUNTNUMBER_RES);
		crcd_shouru_currency = (TextView) view.findViewById(R.id.crcd_shouru_currency);
		crcd_zhichu_currency = (TextView) view.findViewById(R.id.crcd_zhichu_currency);
		crcd_ying_currencycode = (TextView) view.findViewById(R.id.crcd_ying_currencycode);
		crcd_min_currencycode = (TextView) view.findViewById(R.id.crcd_min_currencycode);

//		rl_query_transfer_result = (RelativeLayout) findViewById(R.id.acc_query_result_layout);

		img_down = (ImageView) view.findViewById(R.id.img_acc_query_back);
		acc_query_result_condition = (LinearLayout) view.findViewById(R.id.acc_query_result_condition);

		acc_query_result_condition.setOnClickListener(downQueryClick);
		acc_query_search_condition=(LinearLayout) view.findViewById(R.id.acc_query_search_condition);
		query_down = (ImageView) view.findViewById(R.id.acc_querycondition_down);

		tv_acc_info_currency_value = (TextView) findViewById(R.id.tv_acc_info_currency_value);
		tv_acc_info_cashremit_value = (TextView) findViewById(R.id.tv_acc_info_cashremit_value);
		tv_acc_query_date_value = (TextView) findViewById(R.id.tv_acc_query_date_value);
		tv_crcd_query_pay_value = (TextView) findViewById(R.id.tv_crcd_query_pay_value);

		ll_shouandzhi = (LinearLayout) findViewById(R.id.ll_shouandzhi);
		ll_currentandlowmoney = (LinearLayout) findViewById(R.id.ll_currentandlowmoney);
		tv_crcd_ying_huan = (TextView) findViewById(R.id.tv_crcd_ying_huan);
		tv_crcd_dangqi_low_huan = (TextView) findViewById(R.id.tv_crcd_dangqi_low_huan);

		// 查询结果页面初始化
		sort_text = (Button) view.findViewById(R.id.sort_text);
		sort_text.setText(LocalData.sortMap[0]);
		ll_sort = (LinearLayout) view.findViewById(R.id.ll_sort);
		img_sort_icon = (ImageView) view.findViewById(R.id.img_sort_icon);
		// 初始化排序框
		PopupWindowUtils.getInstance().setOnPullSelecterListener(MyVivtualQueryActivity.this, ll_sort,
				LocalData.sortMap, null, sortClick);

		img_crcd_detail = (ImageView) view.findViewById(R.id.img_crcd_detail);
		img_crcd_detail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent(MyVivtualQueryActivity.this, MyVirtualHasQueryDetailActivity.class);
				it.putExtra(Crcd.CRCD_VIRTUALCARDNO, virtualCardNo);
				startActivity(it);
			}
		});
	}

	/** 初始化容器 */
	private void initPage() {
		CrcdVirtualQueryAdapter adapter = new CrcdVirtualQueryAdapter(this, virCardList);
		viewpager.setAdapter(adapter);
		viewpager.setSelection(list_item);
		getPosition(list_item, virCardList);
		virCardItem = (Map<String, Object>) virCardList.get(list_item);
		beforeWCMap = (Map<String, Object>) virCardList.get(list_item);
		currentPosition = list_item;
		// 得到虚拟卡的卡号
		getAccinfoMassage(virCardItem);
		viewpager.setOnItemSelectedListener(viewPageListener);
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

	private OnItemSelectedListener viewPageListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
			currentPosition = position;
			getPosition(position, virCardList);
			virCardItem = (Map<String, Object>) virCardList.get(position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
	};

	/** 得到虚拟卡账户信息---虚拟卡卡号 */
	private void getAccinfoMassage(Map<String, Object> virCardItem) {
		virtualCardNo = String.valueOf(virCardItem.get(Crcd.CRCD_VIRTUALCARDNO));
		if (!isAccOrCrcd) {
			accountName = (String) virCardItem.get(Acc.ACC_ACCOUNTNAME_RES);
		}
	}

	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 查询虚拟卡未出账单
		psnCrcdVirtualCardUnsettledbillQuery(virtualCardNo, accountName);
	};

	/** 查询虚拟卡未出账单 */
	public void psnCrcdVirtualCardUnsettledbillQuery(String virtualCardNo, String accountName) {
		// 通讯开始,展示通讯框
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);// 获取conversationId

		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDVIRTUALCARDUNSETTLEDBILLQUERY);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTNAME_RES, accountName);
		params.put(Crcd.CRCD_VIRTUALCARDNO, virtualCardNo);
		params.put(Crcd.CRCD_CURRENTINDEX, String.valueOf(currentIndex));
		params.put(Crcd.CRCD_PAGESIZE, String.valueOf(pageSize));
		params.put(Crcd.CRCD_REFRESH, "true");
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdVirtualCardUnsettledbillQueryCallBack");
	}

	/** 查询虚拟卡未出账单----回调结果 */
	List<Map<String, Object>> crcdTransList;

	/** 查询虚拟卡未出账单----回调 */
	public void psnCrcdVirtualCardUnsettledbillQueryCallBack(Object resultObj) {
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		Map<String, Object> returnList = (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.mycrcd_total_result_empty));
			ll_uplayout.setClickable(false);
			return;
		}
		if (!returnList.containsKey(Crcd.CRCD_TRANSATIONLIST)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.mycrcd_total_result_empty));
			ll_uplayout.setClickable(false);
			return;
		}
		String recordNum = String.valueOf(returnList.get(Crcd.CRCD_RECORDNUMBER));
		if (StringUtil.isNull(recordNum)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.mycrcd_total_result_empty));
			ll_uplayout.setClickable(false);
			return;
		}
		crcdTransList = (List<Map<String, Object>>) returnList.get(Crcd.CRCD_TRANSATIONLIST);

		if (StringUtil.isNullOrEmpty(crcdTransList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.mycrcd_total_result_empty));
			ll_uplayout.setClickable(false);
			return;
		}
		if (wcDateList != null && !wcDateList.isEmpty()) {
			wcDateList.clear();
		}
		maxNum = Integer.valueOf(recordNum);
		// 查询未出账单合计
		psnCrcdVirtualCardUnsettledbillSum(virtualCardNo, accountName);
	}

	/** 未出账单顶部卡片赋值 */
	private void dealWcList(List<Map<String, Object>> crcdTransList) {
		ll_sort.setVisibility(View.VISIBLE);
		ll_sort.setClickable(true);
		ll_uplayout.setClickable(true);
		LogGloble.d(TAG + " maxNum", maxNum + "");
		if (maxNum > pageSize) {
			lv_acc_query_result.addFooterView(load_more);
		}
//		rl_query_transfer_result.setVisibility(View.VISIBLE);
		isShow = 2;
		ctfAdapter = new CrcdTransferAdapter(this, crcdTransList, isShow);		
		lv_acc_query_result.setAdapter(ctfAdapter);
		ctfAdapter.notifyDataSetChanged();
		ll_shouandzhi.setVisibility(View.VISIBLE);
		ll_currentandlowmoney.setVisibility(View.GONE);
		crcd_shouru_currency.setText(currencyCode);
		crcd_zhichu_currency.setText(currencyCode);
		img_crcd_detail.setVisibility(View.GONE);
	}

	/** 虚拟卡----未出账单合计查询 */
	public void psnCrcdVirtualCardUnsettledbillSum(String virtualCardNo, String accountName) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDVIRTUALCARDUNSETTLEDBILLSUM);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTNAME_RES, accountName);
		params.put(Crcd.CRCD_VIRTUALCARDNO, virtualCardNo);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdVirtualCardUnsettledbillSumCallBack");
	}

	/** 虚拟卡----未出账单合计查询----回调 */
	public void psnCrcdVirtualCardUnsettledbillSumCallBack(Object resultObj) {
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		totalList = (List<Map<String, Object>>) body.getResult();
		if (StringUtil.isNullOrEmpty(totalList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.mycrcd_total_result_empty));
			return;
		} else {
			BaseHttpEngine.dissMissProgressDialog();
			query_condition.setBackgroundResource(R.drawable.img_bg_query_no);
			if (wcTotalList != null && !wcTotalList.isEmpty()) {
				wcTotalList.clear();
			}
			isRequestWcDate = true;
			wcTotalList.addAll(totalList);
			showWcBottomDateView();
			dealWcBillSum(wcTotalList);
		}
	}

	/** 未出账单底部数据赋值 */
	private void showWcBottomDateView() {
		ll_uplayout.setClickable(true);
		wcDateList.addAll(crcdTransList);
//		PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
		acc_query_search_condition.setVisibility(View.GONE);
		acc_query_result_condition.setVisibility(View.VISIBLE);
		dealWcList(wcDateList);
	}

	/** 处理未出账单总计数据 */
	private void dealWcBillSum(List<Map<String, Object>> totalList) {
		Map<String, Object> map = (Map<String, Object>) totalList.get(0);
		String creditsum = String.valueOf(map.get(Crcd.CRCD_CREDITSUM));
		String debitsum = String.valueOf(map.get(Crcd.CRCD_DEBITSUM));
		String currency = String.valueOf(map.get(Crcd.CRCD_CURRENCY));
		String strCurrcyCode = LocalData.Currency.get(currency);
		// 收入总计
		tv_acc_query_date_value.setText(StringUtil.parseStringCodePattern(currency, creditsum, 2));
		// 支出总计
		tv_crcd_query_pay_value.setText(StringUtil.parseStringCodePattern(currency, debitsum, 2));
		tv_acc_info_currency_value.setText(StringUtil.getForSixForString(MyVirtualBCListActivity.accountNumber));
		tv_acc_info_cashremit_value.setText(StringUtil.getForSixForString(virtualCardNo));
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
			tv_acc_info_cashremit_value.setText(StringUtil.getForSixForString(virtualCardNo));
		}
	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 回主页面
//			Intent intent = new Intent(MyVivtualQueryActivity.this, MainActivity.class);
//			startActivity(intent);
			goToMainActivity();
		}
	};

	OnClickListener goMoreClickListener = new OnClickListener() {
		public void onClick(View v) {
			currentIndex += pageSize;
			if (!StringUtil.isNullOrEmpty(beforeWCMap)) {
				getAccinfoMassage(beforeWCMap);
			}
			// 已出、未出相互切换时，保留上次的数据，需将保留的数据赋值给notransferList
			if (crcdTransList.isEmpty() && wcDateList.size() > 0) {
				for (int i = 0; i < wcDateList.size(); i++) {
					Map<String, Object> map = wcDateList.get(i);
					crcdTransList.add(map);
				}
			}

			// 查询虚拟卡未出账单
			psnCrcdVirtualCardUnsettledbillQueryForMore(virtualCardNo, accountName);
		};
	};

	/** 隐藏查询条件 */
	OnClickListener upQueryClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ll_sort.setClickable(true);
//			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
			acc_query_search_condition.setVisibility(View.GONE);
			acc_query_result_condition.setVisibility(View.VISIBLE);
			ll_sort.setVisibility(View.VISIBLE);
//			rl_query_transfer_result.setVisibility(View.VISIBLE);
		}
	};

	/** 显示查询条件 */
	OnClickListener downQueryClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ll_sort.setClickable(false);
//			PopupWindowUtils.getInstance().getQueryPopupWindow(rl_query_transfer_view, MyVivtualQueryActivity.this);
//			PopupWindowUtils.getInstance().showQueryPopupWindow();
			acc_query_search_condition.setVisibility(View.VISIBLE);
			acc_query_result_condition.setVisibility(View.GONE);
			ll_sort.setVisibility(View.GONE);
		}
	};

	/** 未出账单--排序-全部 */
	private void sortWcAllDate(List<Map<String, Object>> crcdTransList) {
		if (crcdTransList == null || crcdTransList.size() <= 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_nullin));
			return;
		}
		if (maxNum > crcdTransList.size()) {
			if (lv_acc_query_result.getFooterViewsCount() > 0) {

			} else {
				lv_acc_query_result.addFooterView(load_more);
			}
		}
		ctfAdapter = new CrcdTransferAdapter(MyVivtualQueryActivity.this, crcdTransList, isShow);
		lv_acc_query_result.setAdapter(ctfAdapter);
	}

	/** 已出账单--排序-全部 */
	private void sortYcAllDate(List<Map<String, Object>> transList) {
		cleanMore();
		if (transList == null || transList.size() <= 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_nullin));
			return;
		}
		ctfAdapter = new CrcdTransferAdapter(MyVivtualQueryActivity.this, transList, isShow);
		lv_acc_query_result.setAdapter(ctfAdapter);
	}

	/** 未出账单---全部排序 */
	private void wcAllSort() {
		// 未出账单---全部排序
		if (isWcSaveLastDate) {
			sortWcAllDate(wcDateList);
		} else {
			sortWcAllDate(crcdTransList);
		}
	}

	/** 未出账单---收入排序 */
	private void wcInSort() {
		cleanMore();
		List<Map<String, Object>> sortlist = null;
		if (isWcSaveLastDate) {
			sortlist = SortUtils.getIn(wcDateList);
		} else {
			sortlist = SortUtils.getIn(crcdTransList);
		}
		if (sortlist == null || sortlist.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_nullin));
			return;
		}
		ctfAdapter = new CrcdTransferAdapter(MyVivtualQueryActivity.this, sortlist, isShow);
		lv_acc_query_result.setAdapter(ctfAdapter);

	}

	/** 已出账单---收入排序 */
	private void ycInSort() {
		cleanMore();
		List<Map<String, Object>> sortlist = null;
		sortlist = SortUtils.getIn(transList);
		if (sortlist == null || sortlist.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_nullin));
			return;
		}
		ctfAdapter = new CrcdTransferAdapter(MyVivtualQueryActivity.this, sortlist, isShow);
		lv_acc_query_result.setAdapter(ctfAdapter);

	}

	/** 未出账单---支出排序 */
	private void wcOutSort() {
		cleanMore();
		List<Map<String, Object>> outsortlist = null;
		if (isWcSaveLastDate) {
			outsortlist = SortUtils.getOut(wcDateList);
		} else {
			outsortlist = SortUtils.getOut(crcdTransList);
		}

		if (outsortlist == null || outsortlist.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_nullout));
			return;
		}
		ctfAdapter = new CrcdTransferAdapter(MyVivtualQueryActivity.this, outsortlist, isShow);
		lv_acc_query_result.setAdapter(ctfAdapter);
	}

	/** 已出账单---支出排序 */
	private void ycOutSort() {
		cleanMore();
		List<Map<String, Object>> outsortlist = null;
		outsortlist = SortUtils.getOut(transList);
		if (outsortlist == null || outsortlist.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_nullout));
			return;
		}
		ctfAdapter = new CrcdTransferAdapter(MyVivtualQueryActivity.this, outsortlist, isShow);
		lv_acc_query_result.setAdapter(ctfAdapter);
	}

	/** 排序点击事件 */
	OnClickListener sortClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_text1:
				if (isSelectWcOrYcButton) {
					ycTag = 1;
				} else {
					tag = 1;
				}
				// 全部
				if (ctfAdapter != null) {
					ctfAdapter.changeDate(new ArrayList<Map<String, Object>>());
				}
				sort_text.setText(LocalData.sortMap[0]);
				if (isTrans == NOTRANS) {// 未出账单
					wcAllSort();
				} else if (isTrans == HASTRANS) {
					sortYcAllDate(transList);
				}
				break;
			case R.id.tv_text2:
				// 收入
				if (isSelectWcOrYcButton) {
					ycTag = 2;
				} else {
					tag = 2;
				}
				sort_text.setText(LocalData.sortMap[1]);
				if (ctfAdapter != null) {
					ctfAdapter.changeDate(new ArrayList<Map<String, Object>>());
				}
				if (isTrans == NOTRANS) {
					wcInSort();
				} else if (isTrans == HASTRANS) {
					ycInSort();
				}

				break;
			case R.id.tv_text3:
				sort_text.setText(LocalData.sortMap[2]);
				// 支出
				if (isSelectWcOrYcButton) {
					ycTag = 3;
				} else {
					tag = 3;
				}
				if (ctfAdapter != null) {
					ctfAdapter.changeDate(new ArrayList<Map<String, Object>>());
				}
				if (isTrans == NOTRANS) {
					wcOutSort();
				} else if (isTrans == HASTRANS) {
					ycOutSort();
				}

				break;
			}
		}
	};

	static String titlePreix = "";

	/** 清空未出账单数据 */
	private void clearWeiChuDate() {
		if (crcdTransList != null && !crcdTransList.isEmpty()) {
			crcdTransList.clear();
			if (ctfAdapter != null) {
				ctfAdapter.changeDate(new ArrayList<Map<String, Object>>());
			}
		}
	}

	/** 清空已出账单数据 */
	private void clearYiChuDate() {
		if (transList != null && !transList.isEmpty()) {
			transList.clear();
			if (ctfAdapter != null) {
				ctfAdapter.changeDate(new ArrayList<Map<String, Object>>());
			}
		}
	}

	/** 清空已出账单保留的数据 */
	private void cleanYCDate() {
		if (ycDateList != null && !ycDateList.isEmpty()) {
			ycDateList.clear();
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

	private void clearMap() {
		if (returnMap != null && !returnMap.isEmpty()) {
			returnMap.clear();
		}
		if (returnMap1 != null && !returnMap1.isEmpty()) {
			returnMap1.clear();
		}
	}

	private void cleanMore() {
		if (lv_acc_query_result.getFooterViewsCount() > 0) {
			lv_acc_query_result.removeFooterView(load_more);
		}
	}

	/** 未出账单排序---恢复数据 */
	private void wcDateSort() {
		wcSort = tag;
		if (wcSort == 1) {
			// 选择全部
			sort_text.setText(LocalData.sortMap[0]);
			wcAllSort();
		} else if (wcSort == 2) {
			// 选择收入
			sort_text.setText(LocalData.sortMap[1]);
			wcInSort();
		} else if (wcSort == 3) {
			// 选择支出
			sort_text.setText(LocalData.sortMap[2]);
			wcOutSort();
		}
	}

	/** 已出账单排序---恢复数据 */
	private void ycDateSort() {
		ycSort = ycTag;
		if (ycSort == 1) {
			// 选择全部
			sort_text.setText(LocalData.sortMap[0]);
			sortYcAllDate(transList);
		} else if (ycSort == 2) {
			// 选择收入
			sort_text.setText(LocalData.sortMap[1]);
			ycInSort();
		} else if (ycSort == 3) {
			// 选择支出
			sort_text.setText(LocalData.sortMap[2]);
			ycOutSort();
		}
	}

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

	OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.loan_his_btn1:// 未出账单查询按
				isSelectWcOrYcButton = false;
				// 隐藏已出账单查询结果页面顶部控件
				hideYcView();
				if (!StringUtil.isNullOrEmpty(beforeWCMap)) {
					getAccinfoMassage(beforeWCMap);
				}
				if (ctfAdapter != null) {
					ctfAdapter.changeDate(new ArrayList<Map<String, Object>>());
				}
				// 未出账单已经请求数据，和已出账单切换后，需恢复上次的数据
				if (isRequestWcDate) {
					wcDateSort();
				}
				isWcSaveLastDate = true;
				isTrans = NOTRANS;
				loanradiobtn1.setTextColor(Color.WHITE);
				loanradiobtn2.setTextColor(Color.BLACK);
				btnLoanHistoryQuery.setVisibility(View.VISIBLE);
				radiomonthGroup.setVisibility(View.GONE);
				if (StringUtil.isNullOrEmpty(wcTotalList)) {
					ll_uplayout.setClickable(false);
				} else {
					if (wcDateList == null || wcDateList.isEmpty() || wcDateList.size() <= 0) {
						ll_uplayout.setClickable(false);
					} else {
						// 将数据进行反显
						ll_uplayout.setClickable(true);
						dealWcBillSum(wcTotalList);
					}
				}
				break;
			case R.id.loan_his_btn2:// 已出账单查询按钮
				isSelectWcOrYcButton = true;
				// 隐藏未出账单查询结果页面顶部控件
				hideWcView();
				cleanMore();
				if (!StringUtil.isNullOrEmpty(beforeYCMap)) {
					getAccinfoMassage(beforeYCMap);
				}
				if (ctfAdapter != null) {
					ctfAdapter.changeDate(new ArrayList<Map<String, Object>>());
				}
				// 已出账单已经请求数据，未出和账单切换后，需恢复上次的数据
				if (isRequestYcDate) {
					ycDateSort();
				}

				isTrans = HASTRANS;
				isYcSaveLastDate = true;
				loanradiobtn2.setTextColor(Color.WHITE);
				loanradiobtn1.setTextColor(Color.BLACK);
				btnLoanHistoryQuery.setVisibility(View.GONE);
				radiomonthGroup.setVisibility(View.VISIBLE);

				if (StringUtil.isNullOrEmpty(ycDateList)) {
					ll_uplayout.setClickable(false);
				} else {
					isShowPop = false;
					dealYcDate(ycDateList);
					ll_uplayout.setClickable(true);
				}
				break;
			case R.id.month_btn1:
				isYcSaveLastDate = false;
				ycTag = 1;
				isSelectWcOrYcButton = true;
				beforeYCMap = virCardList.get(currentPosition);
				getAccinfoMassage(beforeYCMap);
				query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
				cleanMore();
				clearYiChuDate();
				cleanYCDate();
				clearMap();
				titlePreix = MyVivtualQueryActivity.this.getString(R.string.mycrcd_ninmonth);
				numMonth = QueryDateUtils.getNumMonthAgo(dateTime, 0);
				strMonth = QueryDateUtils.getStrNumMonthAgo(dateTime, 0);
				sort_text.setText(LocalData.sortMap[0]);
				ll_uplayout.setClickable(false);
				// 查询附属卡已出账单
				psnCrcdVirtualCardSettledbillQuery(virtualCardNo, numMonth, accountName);
				break;
			case R.id.month_btn2:
				ycTag = 1;
				isSelectWcOrYcButton = true;
				isYcSaveLastDate = false;
				isRequestYcDate = false;
				beforeYCMap = virCardList.get(currentPosition);
				getAccinfoMassage(beforeYCMap);
				query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
				titlePreix = MyVivtualQueryActivity.this.getString(R.string.mycrcd_eightmonth);
				// 查询附属卡已出账单
				numMonth = QueryDateUtils.getNumMonthAgo(dateTime, -1);
				strMonth = QueryDateUtils.getStrNumMonthAgo(dateTime, -1);
				cleanMore();
				clearYiChuDate();
				cleanYCDate();
				clearMap();
				sort_text.setText(LocalData.sortMap[0]);
				psnCrcdVirtualCardSettledbillQuery(virtualCardNo, numMonth, accountName);
				ll_uplayout.setClickable(false);
				break;
			case R.id.month_btn3:
				ycTag = 1;
				isSelectWcOrYcButton = true;
				isYcSaveLastDate = false;
				isRequestYcDate = false;
				beforeYCMap = virCardList.get(currentPosition);
				getAccinfoMassage(beforeYCMap);
				query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
				titlePreix = MyVivtualQueryActivity.this.getString(R.string.mycrcd_sivenmonth);
				numMonth = QueryDateUtils.getNumMonthAgo(dateTime, -2);
				strMonth = QueryDateUtils.getStrNumMonthAgo(dateTime, -2);
				cleanMore();
				clearYiChuDate();
				cleanYCDate();
				clearMap();
				sort_text.setText(LocalData.sortMap[0]);
				// 查询附属卡已出账单
				psnCrcdVirtualCardSettledbillQuery(virtualCardNo, numMonth, accountName);
				ll_uplayout.setClickable(false);
				break;
			case R.id.month_btn4:
				ycTag = 1;
				isSelectWcOrYcButton = true;
				isYcSaveLastDate = false;
				isRequestYcDate = false;
				beforeYCMap = virCardList.get(currentPosition);
				getAccinfoMassage(beforeYCMap);
				query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
				titlePreix = MyVivtualQueryActivity.this.getString(R.string.mycrcd_sixmonth);
				numMonth = QueryDateUtils.getNumMonthAgo(dateTime, -3);
				strMonth = QueryDateUtils.getStrNumMonthAgo(dateTime, -3);
				cleanMore();
				clearYiChuDate();
				cleanYCDate();
				clearMap();
				sort_text.setText(LocalData.sortMap[0]);
				// 查询附属卡已出账单
				psnCrcdVirtualCardSettledbillQuery(virtualCardNo, numMonth, accountName);
				ll_uplayout.setClickable(false);
				break;
			default:
				break;
			}
		};

	};

	/** 已出账单查询 */
	public void psnCrcdVirtualCardSettledbillQuery(String virtualCardNo, String month, String accountName) {
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCCRCDVIRTUALCARDSETTLEBILLQUERY);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTNAME_RES, accountName);
		params.put(Crcd.CRCD_VIRTUALCARDNO, virtualCardNo);
		params.put(Crcd.CRCD_PSN_STATEMENTMONTH, month);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdVirtualCardSettledbillQueryCallBack");
	}

	/** 已出账单查询-----回调 */
	public void psnCrcdVirtualCardSettledbillQueryCallBack(Object resultObj) {
		if (transList != null && !transList.isEmpty()) {
			transList.clear();
		}
		if (ycDateList != null && !ycDateList.isEmpty()) {
			ycDateList.clear();
		}
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		List<Map<String, Object>> returnList = (List<Map<String, Object>>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_null));
			ll_uplayout.setClickable(false);
			return;
		}
		ycDateList.addAll(returnList);
		BaseHttpEngine.dissMissProgressDialog();
		query_condition.setBackgroundResource(R.drawable.img_bg_query_no);
		isShowPop = true;
		isRequestYcDate = true;
		dealYcDate(ycDateList);

	}

	/** 为已出账单赋值 */
	private void dealYcDate(List<Map<String, Object>> returnList) {
		if (!transList.isEmpty()) {
			transList.clear();
		}
		returnMap = returnList.get(0);
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		if (StringUtil.isNullOrEmpty(returnMap)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_null));
			ll_uplayout.setClickable(false);
			return;
		}

		List<Map<String, Object>> crcdhasTransInfo = (List<Map<String, Object>>) returnList.get(0).get(
				Crcd.CRCD_PSN_CRCDTRANSINFO);
		if (crcdhasTransInfo == null || crcdhasTransInfo.size() <= 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_null));
			ll_uplayout.setClickable(false);
			return;
		}
		if (crcdhasTransInfo.size() <= 0 || crcdhasTransInfo.isEmpty()) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_null));
			ll_uplayout.setClickable(false);
			return;
		}
		for (Map<String, Object> map : crcdhasTransInfo) {
			lists.add(map);
		}
		ll_shouandzhi.setVisibility(View.GONE);
		ll_currentandlowmoney.setVisibility(View.VISIBLE);
		img_crcd_detail.setVisibility(View.VISIBLE);
		tv_crcd_ying_huan.setText(StringUtil.parseStringCodePattern(String.valueOf(returnMap.get(Crcd.CRCD_CURRENCY)),
				String.valueOf(returnMap.get(Crcd.CRCD_CURRENTBALANCE)), 2));
		tv_crcd_dangqi_low_huan.setText(StringUtil.parseStringCodePattern(
				String.valueOf(returnMap.get(Crcd.CRCD_CURRENCY)),
				String.valueOf(returnMap.get(Crcd.CRCD_MINPAYMENTAMOUNT)), 2));
		strCurrency = LocalData.Currency.get(returnMap.get(Crcd.CRCD_CURRENCY));
		crcd_ying_currencycode.setText(strCurrency);
		crcd_min_currencycode.setText(strCurrency);
		ll_uplayout.setClickable(true);
		if (returnList.size() == 1) {
			ll_currentyingForeign.setVisibility(View.GONE);
			ll_currentlowmoneyForeign.setVisibility(View.GONE);
		}
		if (returnList.size() > 1) {
			returnMap1 = returnList.get(1);
			ll_shouandzhi.setVisibility(View.GONE);
			ll_currentandlowmoney.setVisibility(View.VISIBLE);
			ll_currentyingForeign.setVisibility(View.VISIBLE);
			ll_currentlowmoneyForeign.setVisibility(View.VISIBLE);

			strCurrencyCode = LocalData.Currency.get(returnMap1.get(Crcd.CRCD_CURRENCY));
			crcd_forei_ying_huan.setText(StringUtil.parseStringCodePattern(
					String.valueOf(returnMap1.get(Crcd.CRCD_CURRENCY)),
					String.valueOf(returnMap1.get(Crcd.CRCD_CURRENTBALANCE)), 2));
			crcd_forei_ying_currencycode.setText(strCurrencyCode);
			crcd_forei_dangqi_low_huan.setText(StringUtil.parseStringCodePattern(
					String.valueOf(returnMap1.get(Crcd.CRCD_CURRENCY)),
					String.valueOf(returnMap1.get(Crcd.CRCD_MINPAYMENTAMOUNT)), 2));
			crcd_foreign_min_currencycode.setText(strCurrencyCode);
			List<Map<String, Object>> crcdhasTransInfo1 = (List<Map<String, Object>>) returnMap1
					.get(Crcd.CRCD_PSN_CRCDTRANSINFO);
			if (crcdhasTransInfo1 != null && crcdhasTransInfo1.size() > 0) {
				for (Map<String, Object> map : crcdhasTransInfo1) {
					lists.add(map);
				}
			}

		}
		if (lists == null || lists.size() <= 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.acc_transferquery_null));
			ll_uplayout.setClickable(false);
			return;
		} else {
			ll_uplayout.setClickable(true);
			ll_sort.setVisibility(View.VISIBLE);
			ll_sort.setClickable(true);
			if (isShowPop) {
//				PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
				acc_query_search_condition.setVisibility(View.GONE);
				acc_query_result_condition.setVisibility(View.VISIBLE);
			}
			transList.addAll(lists);
//			rl_query_transfer_result.setVisibility(View.VISIBLE);
			tv_acc_info_currency_value.setText(StringUtil.getForSixForString(String
					.valueOf(MyVirtualBCListActivity.accountNumber)));
			tv_acc_info_cashremit_value.setText(StringUtil.getForSixForString(virtualCardNo));
			currencyCode = LocalData.Currency.get(MyVirtualBCListActivity.currencyCode);
			isShow = 2;
			if (!isYcSaveLastDate) {
				ctfAdapter = new CrcdTransferAdapter(this, lists, isShow);
				lv_acc_query_result.setAdapter(ctfAdapter);
				ctfAdapter.notifyDataSetChanged();
			}
			ll_shouandzhi.setVisibility(View.GONE);
			ll_currentandlowmoney.setVisibility(View.VISIBLE);
			Map<String, Object> map = (Map<String, Object>) returnList.get(0);
			crcd_ying_currencycode.setText(currencyCode);
			crcd_min_currencycode.setText(currencyCode);
			String currentbalance = String.valueOf(map.get(Crcd.CRCD_CURRENTBALANCE));
			String minpaymentAmont = String.valueOf(map.get(Crcd.CRCD_MINPAYMENTAMOUNT));
			tv_crcd_ying_huan.setText(StringUtil.parseStringCodePattern(
					String.valueOf(MyVirtualBCListActivity.currencyCode), currentbalance, 2));
			tv_crcd_dangqi_low_huan.setText(StringUtil.parseStringCodePattern(
					String.valueOf(MyVirtualBCListActivity.currencyCode), minpaymentAmont, 2));
			img_crcd_detail.setVisibility(View.VISIBLE);
		}

	}

	public boolean httpRequestCallBackPre(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码

			if (Crcd.CRCD_PSNCCRCDVIRTUALCARDSETTLEBILLQUERY.equals(biiResponseBody.getMethod())) {
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
												if (isInit) {
													isInit = false;
//													PopupWindowUtils.getInstance().showQueryPopupWindow();
													acc_query_search_condition.setVisibility(View.VISIBLE);
													acc_query_result_condition.setVisibility(View.GONE);
												}
												ll_uplayout.setClickable(false);
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
//				intent.setClass(MyVivtualQueryActivity.this, LoginActivity.class);
//				startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
//			}
//		});
//	}

	/** 虚拟银行卡----未出账单查询----点击“更多” */
	public void psnCrcdVirtualCardUnsettledbillQueryForMore(String virtualCardNo, String accountName) {
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);// 获取conversationId

		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDVIRTUALCARDUNSETTLEDBILLQUERY);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTNAME_RES, accountName);
		params.put(Crcd.CRCD_VIRTUALCARDNO, virtualCardNo);
		params.put(Crcd.CRCD_CURRENTINDEX, currentIndex);
		params.put(Crcd.CRCD_PAGESIZE, pageSize);
		params.put(Crcd.CRCD_REFRESH, "false");
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdVirtualCardUnsettledbillQueryForMoreCallBack");
	}

	/** 虚拟银行卡----未出账单查询----点击“更多”----回调 */
	public void psnCrcdVirtualCardUnsettledbillQueryForMoreCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		Map<String, Object> returnList = (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnList)) {
			return;
		}
		List<Map<String, Object>> lists = (List<Map<String, Object>>) returnList.get(Crcd.CRCD_TRANSATIONLIST);
		crcdTransList.addAll(lists);
		wcDateList.addAll(lists);
		String recordNum = String.valueOf(returnList.get(Crcd.CRCD_RECORDNUMBER));
		maxNum = Integer.valueOf(recordNum);

		if ((currentIndex + pageSize) >= maxNum) {
			lv_acc_query_result.removeFooterView(load_more);
		}
		ctfAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(4);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
