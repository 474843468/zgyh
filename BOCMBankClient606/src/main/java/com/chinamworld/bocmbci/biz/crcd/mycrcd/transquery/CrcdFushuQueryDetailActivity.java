package com.chinamworld.bocmbci.biz.crcd.mycrcd.transquery;

import java.util.ArrayList;
import java.util.Calendar;
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
import android.view.ViewGroup;
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
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.MainActivity;
import com.chinamworld.bocmbci.biz.acc.adapter.AccTransGalleryAdapter;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.biz.crcd.adapter.SpinnerAdapter;
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
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;

/**
 * 附属卡交易明细页面----查询结果页面
 * 
 * @author huangyuchao
 * 
 */
public class CrcdFushuQueryDetailActivity extends CrcdBaseActivity implements ICommonAdapter<Map<String, Object>> {
	private static final String TAG = "CrcdFushuQueryDetailActivity";
	ListView lv_acc_query_result;

	/** 附属卡交易明细视图 */
	private View view;
	public String bankName;
	public String nickName;
	public String bankNumber;
	/** 系统时间 */
	private String currenttime;
	TextView tv_startdate;
	TextView tv_enddate;
	/** 查询按钮 */
	private Button btn_acc_query_transfer;
	Button btn_acc_onweek;
	Button btn_acc_onmonth;
	Button btn_acc_threemonth;

	ImageView img_up;
	// LinearLayout query_down_ll;
	ImageView query_down;
	/** 查询条件---除卡片外 */
	private LinearLayout query_condition;
	/** 查询条件整个布局页面 */
//	private RelativeLayout rl_query_transfer;

	/** 查询结果头下拉三角 */
	private ImageView img_down;
//	/** 附属卡卡号 */
//	private Spinner et_loandate;  P503信用卡优化

	/** 请求回来的分期列表 */
	public static List<Map<String, Object>> divideList = new ArrayList<Map<String, Object>>();

	TextView tv_acc_info_currency_value, tv_acc_query_date_value;

	TextView tv_crcd_fushu;

//	CrcdFushuAdapter cfaAdapter;
	CommonAdapter cfaAdapter;
	/** 附属卡的卡号---格式化 */
	private List<String> list = new ArrayList<String>();
	/** 附属卡的卡号----未格式化 */
	private List<String> cardList = new ArrayList<String>();

	int list_item;
//	/** 用户选中的附属卡卡号---未格式化 */
//	public String supplyCardNumber;  P503信用卡优化

	LinearLayout ll_upLayout;
	/** 查询结果顶部 */
	private LinearLayout acc_query_result_condition,acc_query_search_condition;
	/** 附属卡卡号----下拉框适配器 */
	private SpinnerAdapter saAdapter;

	String startThreeDate;

	private ImageView acc_frame_left;
	private ImageView acc_btn_goitem;

	RelativeLayout load_more;
	private LinearLayout ll_sort;
	private ImageView img_sort_icon;
	private Button sort_text = null;
	private Button btn_load_more = null;
	/** 附属卡查询结果list---附属卡号 */
	public List<Map<String, Object>> returnList;
	/** 平列水表viewPager */
	private CustomGallery gallery;
	/** 是否是第一次触发平列水表viewPager的监听事件 */
	private boolean isfirst = true;
	private boolean isgalleryClick = true;
	/** 是否是首次查询附属卡卡号 */
	private boolean searchNum = true;
	int currentIndex = 0;
	int pageSize = 10;
	int maxNum;
	private boolean isRefresh = true;
	private int number = 0;

	public static String accountId = "";
	private String startDate = null;
	private String endDate = null;
	/** 1.首次查询 2.查询附属卡明细 3.查询附属卡卡号 */
	private int isClick = 1;
	private View resultView = null;
	/** 附属卡明细查询结果 */
	public List<Map<String, Object>> crcdTransInfo = new ArrayList<Map<String, Object>>();

	private Map<String, Object> beforeCurrentMap = null;
	private int beforeCurrentPosition = -1;
	/** 默认附属卡卡号----未格式化 */
	private String beforeSubCrcdNo = null;
	
	
	
	/** 信用卡账号 */
	private String accountNumber = null;
	TextView mastercrcdNum;
	/** 附属卡卡号 */
	private String subaccountNumber = null;
	TextView subcrcdNum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_trans_detail_new));
		// 右上角按钮赋值
		setText(this.getString(R.string.acc_rightbtn_go_main));
		view = addView(R.layout.crcd_fuhuscard_history_query_detail);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 0);
		}
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		subaccountNumber= getIntent().getStringExtra(Crcd.CRCD_SUPPLYCARD_RES);
		
		
		
//		P503信用卡优化	
		// 通讯开始,展示通讯框
//		BaseHttpEngine.showProgressDialogCanGoBack();
//		// 附属卡账户集合
//		bankSetupList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
//				.get(ConstantGloble.CRCD_BANKFUSHULIST);
//		if (StringUtil.isNullOrEmpty(bankSetupList)) {
//			BaseHttpEngine.dissMissProgressDialog();
//			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
//			return;
//		}
		// 默认账户的附属卡卡号
//		returnList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
//				.get(ConstantGloble.FOREX_ACTAVI_RESULT_KEY);
//		if (StringUtil.isNullOrEmpty(returnList)) {
//			BaseHttpEngine.dissMissProgressDialog();
//			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
//			return;
//		}		
		// 得到默认账户的数据信息
//		getAccountId();
		
//		P503信用卡优化
		
		searchNum = true;
		// 请求系统时间
		requestSystemDateTime();

	}

	/** 初始化查询条件页面 */
	private void initConditionView() {
//		rl_query_transfer = (RelativeLayout) LayoutInflater.from(this).inflate(
//				R.layout.crcd_fushucrcd_query_conditicon, null);
//		et_loandate = (Spinner) rl_query_transfer.findViewById(R.id.et_loandate); P503信用卡优化
//		P503信用卡优化 start	
		mastercrcdNum= (TextView) view.findViewById(R.id.mastercrcdNum);
		mastercrcdNum.setText(StringUtil.getForSixForString(accountNumber));
		subcrcdNum= (TextView) view.findViewById(R.id.subcrcdNum);
		subcrcdNum.setText(StringUtil.getForSixForString(subaccountNumber));
		
//		P503信用卡优化end	
		tv_startdate = (TextView) view.findViewById(R.id.acc_query_transfer_startdate);
		tv_enddate = (TextView) view.findViewById(R.id.acc_query_transfer_enddate);
		tv_startdate.setOnClickListener(choosestartDateClick);
		tv_enddate.setOnClickListener(chooseendDateClick);

		btn_acc_onweek = (Button) view.findViewById(R.id.btn_acc_onweek);
		btn_acc_onmonth = (Button) view.findViewById(R.id.btn_acc_onmonth);
		btn_acc_threemonth = (Button) view.findViewById(R.id.btn_acc_threemonth);
		btn_acc_query_transfer = (Button) view.findViewById(R.id.btn_acc_query_transfer);
		img_up = (ImageView) view.findViewById(R.id.acc_query_up);
		// 收起
		ll_upLayout = (LinearLayout) view.findViewById(R.id.ll_upLayout);
		query_condition = (LinearLayout) view.findViewById(R.id.ll_query_condition);
		acc_frame_left = (ImageView) view.findViewById(R.id.acc_frame_left);
		acc_btn_goitem = (ImageView) view.findViewById(R.id.acc_btn_goitem);
//		gallery = (CustomGallery) rl_query_transfer.findViewById(R.id.viewPager); P503信用卡优化
//		PopupWindowUtils.getInstance().getQueryPopupWindow(rl_query_transfer, this);
		btn_acc_query_transfer.setOnClickListener(searchListener);
		btn_acc_onweek.setOnClickListener(fushuQueryListener);
		btn_acc_onmonth.setOnClickListener(fushuQueryListener);
		btn_acc_threemonth.setOnClickListener(fushuQueryListener);
		ll_upLayout.setOnClickListener(upQueryClick);
	}

	/** 初始化查询结果页面布局 */
	private void initResulrView() {
		resultView = view.findViewById(R.id.acc_query_result_layout);
		tv_acc_info_currency_value = (TextView) view.findViewById(R.id.tv_acc_info_currency_value);
		tv_crcd_fushu = (TextView) view.findViewById(R.id.tv_crcd_fushu);
		tv_acc_query_date_value = (TextView) view.findViewById(R.id.tv_acc_query_date_value);
		img_down = (ImageView) view.findViewById(R.id.img_acc_query_back);
		acc_query_result_condition = (LinearLayout) view.findViewById(R.id.acc_query_result_condition);
		acc_query_result_condition.setOnClickListener(downQueryClick);
		acc_query_search_condition= (LinearLayout) view.findViewById(R.id.acc_query_search_condition);
		sort_text = (Button) view.findViewById(R.id.sort_text);
		sort_text.setText(LocalData.sortMap[0]);
		ll_sort = (LinearLayout) view.findViewById(R.id.ll_sort);
		// 初始化排序框
		PopupWindowUtils.getInstance().setOnPullSelecterListener(CrcdFushuQueryDetailActivity.this, ll_sort,
				LocalData.sortMap, null, sortClick);
		img_sort_icon = (ImageView) view.findViewById(R.id.img_sort_icon);
		img_sort_icon.setBackgroundResource(R.drawable.icon_paixu_time);
		lv_acc_query_result = (ListView) view.findViewById(R.id.lv_acc_query_result);
		load_more = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.acc_load_more, null);
		btn_load_more = (Button) load_more.findViewById(R.id.btn_load_more);
		btn_load_more.setOnClickListener(goMoreClickListener);
	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 回主页面
//			Intent intent = new Intent(CrcdFushuQueryDetailActivity.this, MainActivity.class);
//			startActivity(intent);
			goToMainActivity();
		}
	};

	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		if (isClick == 2) {
			// 点击查询按钮------ 查询附属卡明细
			if (StringUtil.isNull(subaccountNumber)) {
				return;
			}
			supplymentCardQuery(subaccountNumber, String.valueOf(isRefresh));
		} else if (isClick == 3) {
			// 附属卡卡号查询
			psnCrcdAppertainTranSetQuery(accountId);
		}

	};

	/** 附属卡查询----查询附属卡的卡号 */
	private void psnCrcdAppertainTranSetQuery(String accountId) {
		// 通讯开始,展示通讯框
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDAPPERTAINNTRANQUERY);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		biiRequestBody.setParams(paramsmap);
		if (!searchNum) {
			// 滑动卡片查询
			HttpManager.requestBii(biiRequestBody, this, "psnCrcdAppertainTranSetQuerySlipCallBack");
		}

	}

	/**
	 * 因网络问题（超时，无网络等）引起的通信异常的默认回调( 返回码 不是200 )<br>
	 * 子类可重写进行特殊化处理<br>
	 * 
	 * @param 请求失败的接口名称
	 *            Method
	 */
	@Override
	public void commonHttpErrorCallBack(String requestMethod) {
		if (Crcd.CRCD_PSNCRCDAPPERTAINNTRANQUERY.equals(requestMethod)) {
			BaseHttpEngine.dissMissProgressDialog();
			finish();
		} else {
			super.commonHttpErrorCallBack(requestMethod);
		}
	}

	public boolean httpRequestCallBackPre(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码

			if (Crcd.CRCD_PSNCRCDAPPERTAINNTRANQUERY.equals(biiResponseBody.getMethod())) {
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
												finish();
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
//				intent.setClass(CrcdFushuQueryDetailActivity.this, LoginActivity.class);
//				startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
//			}
//		});
//	}

	/** 滑动卡片--- 查询附属卡的卡号---回调 */
	public void psnCrcdAppertainTranSetQuerySlipCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		// 清空附属卡数据
		clearSubCrcd();
		returnList = (List<Map<String, Object>>) body.getResult();
		if (returnList == null || returnList.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
//			et_loandate.setEnabled(false);
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		getSubCreditCardNum();
	}

	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		if (StringUtil.isNull(dateTime)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		initConditionView();
		initResulrView();
		
//		P503信用卡优化 start
		// 卡片滑动监听事件
//		searchSubCreditCardNum();
		// 为卡片--账户赋值
//		initPage();
		// 得到附属卡卡号
//		getSubCreditCardNum();
//		P503信用卡优化 end
		// 为查询条件页面赋值
		initSetDate();
//		PopupWindowUtils.getInstance().showQueryPopupWindowFirst();
		
		acc_query_search_condition.setVisibility(View.VISIBLE);
		// 附属卡交易明细查询
		setValue();
		if (StringUtil.isNull(subaccountNumber)) {
			return;
		}		
		//默认查询start
		//supplymentCardQuery(supplyCardNumber, String.valueOf(isRefresh));
		//默认查询end
		BaseHttpEngine.dissMissProgressDialog();	
	}

	/** 得到用户选择的账户Id */
	private void getAccountId() {
		list_item = this.getIntent().getIntExtra(ConstantGloble.ACC_POSITION, 0);
		// 刚进入时，当前账户为选择的账户
		currentBankList = bankSetupList.get(list_item);
		beforeCurrentMap = bankSetupList.get(list_item);
		beforeCurrentPosition = list_item;
		getAccinfoMessage(currentBankList);
	}

	/** 得到当前账户的信息 */
	private void getAccinfoMessage(Map<String, Object> currentBankList) {
		accountId = (String) currentBankList.get(Crcd.CRCD_ACCOUNTID_RES);
		bankName = (String) currentBankList.get(Crcd.CRCD_ACCOUNTNAME_RES);
		nickName = (String) currentBankList.get(Crcd.CRCD_NICKNAME_RES);
		bankNumber = (String) currentBankList.get(Crcd.CRCD_ACCOUNTNUMBER_RES);
		LogGloble.d(TAG + " accountId", accountId);
	}

	/**
	 * 初始化page容器
	 */
	private void initPage() {
		AccTransGalleryAdapter adapter = new AccTransGalleryAdapter(this, bankSetupList);
		gallery.setAdapter(adapter);
		gallery.setSelection(list_item);
		// 得到卡片选择的位置
		getPosition(list_item);
	}

	/** 滑动卡片查询附属卡卡号-----page监听事件 */
	private void searchSubCreditCardNum() {
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				LogGloble.d(TAG, "gallery--onItemSelected===position= " + position);
				beforeCurrentPosition = position;
				// 得到卡片选择的位置
				getPosition(position);
				currentBankList = bankSetupList.get(position);
				// 得到当前账户信息
				getAccinfoMessage(currentBankList);
				searchNum = false;
				if (!isfirst) {
					// 查询附属卡卡号
					BaseHttpEngine.showProgressDialog();
					requestCommConversationId();
					isClick = 3;
				} else {
					isfirst = false;
				}
				//非默认查询
				isfirst = false;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	/** 得到卡片选择的位置 */
	private void getPosition(int position) {
		if (position == 0) {
			acc_frame_left.setVisibility(View.INVISIBLE);
			if (bankSetupList.size() == 1) {
				acc_btn_goitem.setVisibility(View.INVISIBLE);
			} else {
				acc_btn_goitem.setVisibility(View.VISIBLE);
			}
		} else if (position == bankSetupList.size() - 1) {
			acc_frame_left.setVisibility(View.VISIBLE);
			acc_btn_goitem.setVisibility(View.INVISIBLE);
		} else {
			acc_frame_left.setVisibility(View.VISIBLE);
			acc_btn_goitem.setVisibility(View.VISIBLE);
		}
	}

	/** 清除附属卡的数据 */
	private void clearSubCrcd() {
		if (returnList != null && !returnList.isEmpty()) {
			returnList.clear();
		}
		if (list != null && !list.isEmpty()) {
			list.clear();
		}
		if (cardList != null && !cardList.isEmpty()) {
			cardList.clear();
		}
	}

	/** 得到附属卡的卡号 */
	private void getSubCreditCardNum() {
		List<Map<String, Object>> supplyList = returnList;
		for (int i = 0; i < supplyList.size(); i++) {
			Map<String, Object> map = supplyList.get(i);
			String cardNum = String.valueOf(map.get(Crcd.CRCD_SUBCREDITCARDNUM));
			String passCardNum = StringUtil.getForSixForString(cardNum);
			list.add(passCardNum);
			cardList.add(cardNum);
		}
//		SpinnerDate();
	}

	/** 为查询条件页面控件赋值 */
	private void initSetDate() {
		// 三天前的日期
		startThreeDate = QueryDateUtils.getlastthreeDate(dateTime);
		tv_startdate.setText(startThreeDate);
		startDate = startThreeDate;
		// 系统当前时间格式化
		currenttime = QueryDateUtils.getcurrentDate(dateTime);
		endDate = currenttime;
		// 初始结束时间赋值
		tv_enddate.setText(currenttime);

	}

//	/** 附属卡下拉框数据 */  P503信用卡优化 start
//	private void SpinnerDate() {
//		if (list == null || list.size() <= 0) {
//			BaseHttpEngine.dissMissProgressDialog();
//			return;
//		}
//		saAdapter = new SpinnerAdapter(this, R.layout.custom_spinner_item, list);
//		saAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		et_loandate.setAdapter(saAdapter);
//		supplyCardNumber = cardList.get(0);
//		et_loandate.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//				if (cardList != null && cardList.size() > position) {
//					supplyCardNumber = cardList.get(position);
//				}
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//
//			}
//		});
//	}
//	P503信用卡优化 end
	/** 附属卡明细查询 */
	private void supplymentCardQuery(String supplyCardNumber, String refresh) {
		// 通讯开始,展示通讯框
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDAPPERTAINTRANQUERY_API);
		String conversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(conversationId);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTID_RES, supplyCardNumber);
		params.put(Crcd.CRCD_CURRENTINDEX, String.valueOf(currentIndex));
		params.put(Crcd.CRCD_PAGESIZE, String.valueOf(pageSize));
		params.put(Crcd.START_DATE, startDate);
		params.put(Crcd.END_DATE, endDate);
		params.put(Crcd.CRCD_REFRESH, refresh);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "supplymentCardQueryBillCallBack");
	}

	/** 为查询结果页面顶部赋值 */
	private void setValue() {
		tv_acc_info_currency_value.setText(StringUtil.getForSixForString(accountNumber));
		beforeSubCrcdNo = subaccountNumber;
		tv_crcd_fushu.setText(StringUtil.getForSixForString(subaccountNumber));
		tv_acc_query_date_value.setText(startDate + "-" + endDate);
	}

	/** 附属卡明细查询----回调 */
	public void supplymentCardQueryBillCallBack(Object object) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse response = (BiiResponse) object;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		Map<String, Object> returnList = (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_null));
			return;
		}
		List<Map<String, Object>> lists = (List<Map<String, Object>>) returnList.get(Crcd.CRCD_SUPPLYLIIST);
		if (lists == null || lists.size() <= 0) {
			// 查询结果为空
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		} else {
			crcdTransInfo.addAll(lists);
//			PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
			acc_query_search_condition.setVisibility(View.GONE);
			acc_query_result_condition.setVisibility(View.VISIBLE);
			resultView.setVisibility(View.VISIBLE);
			query_condition.setBackgroundResource(R.drawable.img_bg_query_no);
			String recordNumber = (String) returnList.get(Crcd.CRCD_RECORDNUMBER);
			number = Integer.valueOf(recordNumber);
			LogGloble.d(TAG + "   recordNumber", recordNumber);
			if (number > pageSize && isRefresh) {
				lv_acc_query_result.addFooterView(load_more);
			}

			if (!isRefresh) {
				if (currentIndex + pageSize >= number) {
					lv_acc_query_result.removeFooterView(load_more);
				}
			}
			ll_sort.setClickable(true);
			ll_sort.setVisibility(View.VISIBLE);

			if (isRefresh) {
//				cfaAdapter = new CrcdFushuAdapter(this, crcdTransInfo);
				cfaAdapter = new CommonAdapter<Map<String, Object>>(CrcdFushuQueryDetailActivity.this, crcdTransInfo, this);
				lv_acc_query_result.setAdapter(cfaAdapter);
			} else {
				cfaAdapter.notifyDataSetChanged();
			}

		}
		isfirst = false;
	}

	/** 更多监听事件 */
	OnClickListener goMoreClickListener = new OnClickListener() {
		public void onClick(View v) {
//			getAccinfoMessage(beforeCurrentMap);
//			subaccountNumber = beforeSubCrcdNo;
			isRefresh = false;
			currentIndex += pageSize;
			// 查询附属卡交易明细
			BaseHttpEngine.showProgressDialog();
			if (StringUtil.isNull(subaccountNumber)) {
				return;
			}
			supplymentCardQuery(subaccountNumber, String.valueOf(isRefresh));
		};
	};
	/** 排序点击事件 */
	OnClickListener sortClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_text1:
				sort_text.setText(LocalData.sortMap[0]);
//				cfaAdapter.changeDate(new ArrayList<Map<String, Object>>());
				cfaAdapter.setSourceList(new ArrayList<Map<String, Object>>(), 0);
				img_sort_icon.setBackgroundResource(R.drawable.icon_paixu_time);
				// 全部
				if (number > crcdTransInfo.size()) {
					if (lv_acc_query_result.getFooterViewsCount() > 0) {

					} else {
						lv_acc_query_result.addFooterView(load_more);
					}

				}
//				cfaAdapter = new CrcdFushuAdapter(CrcdFushuQueryDetailActivity.this, crcdTransInfo);
				cfaAdapter = new CommonAdapter<Map<String, Object>>(CrcdFushuQueryDetailActivity.this, crcdTransInfo, CrcdFushuQueryDetailActivity.this);
				lv_acc_query_result.setAdapter(cfaAdapter);
				cfaAdapter.notifyDataSetChanged();
				break;
			case R.id.tv_text2:
				// 收入
				sort_text.setText(LocalData.sortMap[1]);
//				cfaAdapter.changeDate(new ArrayList<Map<String, Object>>());
				cfaAdapter.setSourceList(new ArrayList<Map<String, Object>>(), 0);
				if (lv_acc_query_result.getFooterViewsCount() > 0) {
					lv_acc_query_result.removeFooterView(load_more);
				}
				List<Map<String, Object>> listIn = insort(crcdTransInfo);
				if (listIn == null || listIn.size() == 0) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							CrcdFushuQueryDetailActivity.this.getString(R.string.acc_transferquery_nullin));
					return;
				}

				img_sort_icon.setBackgroundResource(R.drawable.icon_paixu_shouru);

//				cfaAdapter = new CrcdFushuAdapter(CrcdFushuQueryDetailActivity.this, listIn);
				cfaAdapter = new CommonAdapter<Map<String, Object>>(CrcdFushuQueryDetailActivity.this, listIn, CrcdFushuQueryDetailActivity.this);
				
				lv_acc_query_result.setAdapter(cfaAdapter);
				cfaAdapter.notifyDataSetChanged();
				break;
			case R.id.tv_text3:
				// 支出
				sort_text.setText(LocalData.sortMap[2]);
//				cfaAdapter.changeDate(new ArrayList<Map<String, Object>>());
				cfaAdapter.setSourceList(new ArrayList<Map<String, Object>>(), 0);
				if (lv_acc_query_result.getFooterViewsCount() > 0) {
					lv_acc_query_result.removeFooterView(load_more);
				}
				List<Map<String, Object>> listOut = outsort(crcdTransInfo);
				if (listOut == null || listOut.size() == 0) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							CrcdFushuQueryDetailActivity.this.getString(R.string.acc_transferquery_nullout));
					return;
				}
				img_sort_icon.setBackgroundResource(R.drawable.icon_paixu_zhichu);
//				cfaAdapter = new CrcdFushuAdapter(CrcdFushuQueryDetailActivity.this, listOut);
				cfaAdapter = new CommonAdapter<Map<String, Object>>(CrcdFushuQueryDetailActivity.this, listOut, CrcdFushuQueryDetailActivity.this);
				lv_acc_query_result.setAdapter(cfaAdapter);
				cfaAdapter.notifyDataSetChanged();
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
				String amount = (String) map.get(Crcd.CRCD_DEBITFLAG);
				if (amount.startsWith(ConstantGloble.CRCD_CRED)) {
					inList.add(map);
				} else {
					continue;
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
				String amount = (String) map.get(Crcd.CRCD_DEBITFLAG);
				if (LocalData.debtList.contains(amount)) {
					outList.add(map);
				} else {
					continue;
				}
			}
		}
		return outList;
	}

	public static Map<String, Object> dividedMap;

	/** 清空数据 */
	private void cleanDate() {
		isRefresh = true;
		number = 0;
		currentIndex = 0;
		if (crcdTransInfo != null && !crcdTransInfo.isEmpty()) {
			crcdTransInfo.clear();
			cfaAdapter.notifyDataSetChanged();
		}
		if (lv_acc_query_result.getFooterViewsCount() > 0) {
			lv_acc_query_result.removeFooterView(load_more);
		}
		tv_acc_info_currency_value.setText("");
		tv_crcd_fushu.setText("");
		tv_acc_query_date_value.setText("");

	}

	/** 查询按钮监听事件 */
	private OnClickListener searchListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			searchCondition();
			cleanDate();
			sort_text.setText(LocalData.sortMap[0]);			
			isClick = 2;
			startDate = tv_startdate.getText().toString().trim();
			endDate = tv_enddate.getText().toString().trim();
//			getAccinfoMessage(currentBankList);
//			beforeCurrentMap = bankSetupList.get(beforeCurrentPosition);
//			int position = et_loandate.getSelectedItemPosition();
//			supplyCardNumber = cardList.get(position);
			if (QueryDateUtils.compareDateOneYear(startDate, dateTime)) {
				// 起始日期不能早于系统当前日期一年前
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						CrcdFushuQueryDetailActivity.this.getString(R.string.acc_check_start_enddate));
				return;
			}
			if (QueryDateUtils.compareDate(endDate, dateTime)) {
				// 结束日期在服务器日期之前
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						CrcdFushuQueryDetailActivity.this.getString(R.string.acc_check_enddate));
				return;
			}
			if (QueryDateUtils.compareDate(startDate, endDate)) {
				// 开始日期在结束日期之前

			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						CrcdFushuQueryDetailActivity.this.getString(R.string.acc_query_errordate));
				return;
			}

			if (QueryDateUtils.compareDateThree(startDate, endDate)) {
				// 起始日期与结束日期最大间隔为三个自然月
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						CrcdFushuQueryDetailActivity.this.getString(R.string.acc_check_start_end_date));
				return;
			}
			setValue();
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
		}
	};

	OnClickListener fushuQueryListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			searchCondition();
			cleanDate();
			sort_text.setText(LocalData.sortMap[0]);			
			isClick = 2;
			switch (v.getId()) {
			case R.id.btn_acc_onweek:
				startDate = QueryDateUtils.getlastWeek(dateTime).trim();
				endDate = QueryDateUtils.getcurrentDate(dateTime).trim();
				break;
			case R.id.btn_acc_onmonth:
				startDate = QueryDateUtils.getlastOneMonth(dateTime).trim();
				endDate = QueryDateUtils.getcurrentDate(dateTime).trim();
				break;
			case R.id.btn_acc_threemonth:
				startDate = QueryDateUtils.getlastThreeMonth(dateTime).trim();
				endDate = QueryDateUtils.getcurrentDate(dateTime).trim();
				break;
			}
//			getAccinfoMessage(currentBankList);
//			beforeCurrentMap = bankSetupList.get(beforeCurrentPosition);
//			int position = et_loandate.getSelectedItemPosition();
//			supplyCardNumber = cardList.get(position);
			setValue();
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();
		}
	};

	/** 时间空间反显值 */
	private void setTimesText(String star, String end) {
		tv_startdate.setText(star);
		tv_enddate.setText(end);
	}

	/** 隐藏查询结果页面 */
	private void searchCondition() {
		resultView.setVisibility(View.INVISIBLE);
		query_condition.setBackgroundResource(R.drawable.img_bg_query_j);
	}

	/** 收起事件------隐藏查询条件 */
	OnClickListener upQueryClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			LogGloble.d(TAG + "---", "dis");
			if (crcdTransInfo == null || crcdTransInfo.size() <= 0) {

			} else {
//				PopupWindowUtils.getInstance().dissMissQueryPopupWindow();
				acc_query_search_condition.setVisibility(View.GONE);
				acc_query_result_condition.setVisibility(View.VISIBLE);
				resultView.setVisibility(View.VISIBLE);
				ll_sort.setVisibility(View.VISIBLE);
			}

		}
	};

	/** 显示查询条件 */
	OnClickListener downQueryClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
//			PopupWindowUtils.getInstance().showQueryPopupWindow();
			acc_query_search_condition.setVisibility(View.VISIBLE);
			acc_query_result_condition.setVisibility(View.GONE);
			ll_sort.setVisibility(View.GONE);
		}
	};

	/** 设置查询日期---开始日期 */
	OnClickListener choosestartDateClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Calendar c = Calendar.getInstance(); // 实例化一个日期与时间操作对象
			QueryDateUtils.checkDate(tv_startdate, c);
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			DatePickerDialog dialog = new DatePickerDialog(CrcdFushuQueryDetailActivity.this, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					StringBuilder date = new StringBuilder();
					date.append(String.valueOf(year));
					date.append("/");
					int month = monthOfYear + 1;
					date.append(((month < 10) ? ("0" + month) : (month + "")));
					date.append("/");
					date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth) : (dayOfMonth + "")));
					// // 为日期赋值
					String startDate = date.toString().trim();
					tv_startdate.setText(startDate);
				}
			}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
			dialog.show();
		}
	};

	/** 设置查询日期 */
	OnClickListener chooseendDateClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			Calendar c = Calendar.getInstance(); // 实例化一个日期与时间操作对象
			QueryDateUtils.checkDate(tv_enddate, c);

			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			DatePickerDialog dialog = new DatePickerDialog(CrcdFushuQueryDetailActivity.this, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					StringBuilder date = new StringBuilder();
					date.append(String.valueOf(year));
					date.append("/");
					int month = monthOfYear + 1;
					date.append(((month < 10) ? ("0" + month) : (month + "")));
					date.append("/");
					date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth) : (dayOfMonth + "")));
					// // 为日期赋值
					String endDate = date.toString().trim();
					tv_enddate.setText(endDate);
				}
			}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
			dialog.show();
		}
	};

	@Override
	public View getView(int arg0, Map<String, Object> currentItem,
			LayoutInflater inflater, View convertView, ViewGroup viewGroup) {
		ViewHolder viewholder;
		if (convertView == null) {
		
			convertView = inflater.inflate(R.layout.crcd_trans_query_list_item, null);
			viewholder = new ViewHolder();
			viewholder.acc_query_transfer_amount = (TextView) convertView.findViewById(R.id.acc_query_transfer_amount);

			/** 账户交易收入支出金额 */
			viewholder.tv_acc_query_result_amount = (TextView) convertView.findViewById(R.id.acc_query_transfer_amount_value);
			/** 账户交易时间 */
			viewholder.tv_acc_query_result_paymentdate = (TextView) convertView
					.findViewById(R.id.acc_query_transfer_paymentdate_value);
			/** 账户交易账户描述 */
			viewholder.tv_acc_query_result_balance = (TextView) convertView.findViewById(R.id.acc_query_transfer_balance_value);
			/** 左上角标志 */
			viewholder.img_flag = (ImageView) convertView.findViewById(R.id.img_flag);

			viewholder.img_crcd_setup = (Button) convertView.findViewById(R.id.img_crcd_setup);
			viewholder.img_crcd_setup.setVisibility(View.GONE);
			viewholder.buttonView = convertView.findViewById(R.id.button_lay);
			viewholder.buttonView.setVisibility(View.GONE);
			convertView.setTag(viewholder);
		}else{
			viewholder = (ViewHolder) convertView.getTag();	
		}
		
		viewholder.tv_acc_query_result_paymentdate.setText((String) currentItem.get(Crcd.CRCD_TRANSDATE));
		String result_bookAmount = (String)currentItem.get(Crcd.CRCD_BOOKAMOUNT);
		if (!StringUtil.isNullOrEmpty(result_bookAmount)) {
			viewholder.tv_acc_query_result_amount.setText(StringUtil.parseStringPattern(result_bookAmount, 2));
		}
		String businessDigest = (String) (currentItem.get(Crcd.CRCD_REMARK));
		viewholder.tv_acc_query_result_balance.setText(businessDigest);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(CrcdFushuQueryDetailActivity.this, viewholder.tv_acc_query_result_amount);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(CrcdFushuQueryDetailActivity.this, viewholder.tv_acc_query_result_balance);
		String debitCreditFlag = String.valueOf(currentItem.get(Crcd.CRCD_DEBITFLAG));

		if ("CRED".equals(debitCreditFlag)) {
			viewholder.img_flag.setImageResource(R.drawable.img_triangle_green);
			viewholder.acc_query_transfer_amount.setText(getString(R.string.acc_query_amount_in));
		} else if ("DEBT".equals(debitCreditFlag) || "NMON".equals(debitCreditFlag)) {
			viewholder.img_flag.setImageResource(R.drawable.img_triangle_red);
			viewholder.acc_query_transfer_amount.setText(getString(R.string.acc_query_amount_out));
		}
		return convertView;

	}
	class ViewHolder {
		/** 账户交易收入支出金额 */
		 TextView tv_acc_query_result_amount;
		/** 账户交易时间 */
		 TextView tv_acc_query_result_paymentdate;
		/** 账户交易账户描述 */
		 TextView tv_acc_query_result_balance;

		 TextView acc_query_transfer_amount;

		 Button img_crcd_setup;

		/** 左上角标识 */
		 ImageView img_flag;
		 View buttonView;
	}
}
