package com.chinamworld.bocmbci.biz.lsforex.rate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.SecondMainActivity;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.biz.lsforex.adapter.IsForexRateInfoAdapter;
import com.chinamworld.bocmbci.biz.lsforex.trade.IsForexTradeSubmitActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpObserver;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.thread.PollingRequestThread;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.server.LocalDataService;
import com.chinamworld.bocmbci.utils.LoginTask.LoginCallback;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 双向宝行情页面 */
public class IsForexTwoWayTreasureActivity extends IsForexBaseActivity implements OnClickListener {
	private static final String TAG = "IsForexTwoWayTreasureActivity";
	/** IsForexRateInfoActivity的主布局 */
	private View rateInfoView = null;
	/** 全部外汇汇率 */
	private Button rate_waihui_Button = null;
	/** 我的外汇汇率 */
	private Button CustomerRateButton = null;
	/** 显示汇率的信息 */
	private ListView rateListView = null;
	/** 汇率更新时间 */
	private TextView rateTimes = null;
	/** @customerRateList:用户定制的外汇详情list */
	private List<Map<String, Object>> customerRateList = null;
	/** @allRateList:全部外汇详情的list */
	private List<Map<String, Object>> allRateList = null;
	/** backButton:返回按钮 */
	private Button backButton = null;
	/** mainUI：快速交易*/
	private Button mainUI = null;
	/** 汇率定制按钮 在全部外汇汇率页面中，是隐藏的。 在我的外汇汇率页面中，是显示出来的。 */
	private View makeRate = null;
	/** 顶部按钮view */
	private View makeRateView = null;
	/** 全部汇率是否有数据 */
	private boolean isHasAllList = false;
	/** 我的外汇汇率是否有数据 */
	private boolean isHasCustomerList = false;
	private IsForexRateInfoAdapter adapter = null;
	/** 区分用户选中的是那个按钮 1:allRateButton;2:CustomerRateButton进行刷新数据 0：是贵金属 */
	private int coinId = 2;//2
	/** 0-全部货币对，1-用户定制的货币战对---->0-外汇货币对，2-贵金属货币对，1-双向宝 */
	private int allOrCustomerReq = -1;
	/** noMakeView布局里面的按钮--汇率定制按钮 */
	private Button noMakeButton = null;
	/** 买卖方向名称 */
	private String directionName = null;
	/** 保存全部汇率的数据 */
	private List<Map<String, Object>> tradeDateList = null;
	/** 用户选择的货币对 */
	private String codeName = null;
	/** 1-快速交易，2-银行买卖价 */
	private int quickOrRate = 1;
	// /////////////////////////////////////////////////////
	// 布局是否显示
	/** 全部汇率 我的汇率布局 */
	private LinearLayout layout_button;
	/** 登陆按钮 */
	private Button login = null;
	/** 手动刷新 */
	private Button isFinish = null;
	
//	P603 wuhan
	private Button rate_goldButton =null;
	private boolean isGold = true;
	private String paritiesType = "";//牌价
	private String vfgType = "";
	private List<Map<String, Object>> goldRateList = null;
	private boolean isHasGoldList = false;
	private View left_title_ll;
	private TextView left_title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		taskTag = 4;
		init();
		initOnClick();
		// 侧边栏的显示 判断是否登陆,如果登陆显示右侧菜单 并选中第一个,如果未登录则不显示
		setLeftSelectedPosition(BaseDroidApp.getInstanse().isLogin() ? "isForexStorageCash_1" : null);
		// BaseHttpEngine.showProgressDialogCanGoBack();
		// 请求登录后的CommConversationId
		// requestCommConversationId();
		/**
		 * 功能外置修改,以登陆先判断是否开通理财，未登录获取未登录全部行情 用户点击标题栏登陆按钮:
		 * ->登陆成功回调onActivityResult通过ACTIVITY_REQUEST_LOGIN_CODE判断是标题栏入口，
		 * 然后判断是否开通理财进而进行页面数据刷新 ->登陆失败不做任何事情 用户点击买入或卖出后登陆 ->登陆成功先判断是否已经开通理财和设置账户
		 * ->没有开通理财和设置账户，taskTag = 8调用判断理财是否开通 ->已经开通理财和设置账号直接进行交易 ->登陆失败不做任何事情
		 * 启动轮询查询机制在 onActivityResult 通过ISFOREX_TRADE_CODE_ACTIVITY
		 * 判断是从上一个交易页面返回， 如果全部汇率按钮和我的汇率按钮都没有被点击则默认按下我的汇率
		 */
		if (BaseDroidApp.getInstanse().isLogin()) {
			left_title_ll.setVisibility(View.VISIBLE);
			left_title.setText(getResources().getString(R.string.for_reference_only));
			commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
			BaseHttpEngine.showProgressDialogCanGoBack();
			if (StringUtil.isNull(commConversationId)) {
				// 请求登录后的CommConversationId
				requestCommConversationId();
			} else {
				// 判断用户是否开通投资理财服务
				requestPsnInvestmentManageIsOpen();
			}
		} else {
			left_title_ll.setVisibility(View.VISIBLE);
			left_title.setText(getResources().getString(R.string.for_reference_only));
			isGold = true;//一进入时默认为贵金属行情
			isFinish.performClick();
			
		}
	}


	@Override
	public void finish() {
		super.finish();
		if (HttpManager.mPollingRequestThread != null && PollingRequestThread.pollingFlag) {
			LogGloble.e(TAG, "onPause() mPollingRequestThread  stopPolling()  ");
			HttpManager.stopPolling();
		}
	}

	/** 停止轮询 */
	private void stopPollingFlag() {
		if (HttpManager.mPollingRequestThread != null && PollingRequestThread.pollingFlag) {
			LogGloble.e(TAG, "onPause() mPollingRequestThread  stopPolling()  ");
			HttpManager.stopPolling();
		}
	}

	/** 初始化所有的控件 */
	private void init() {
		rateInfoView = LayoutInflater.from(this).inflate(R.layout.isforex_rate_main, null);
		tabcontent.addView(rateInfoView);
		// 汇率定制页面
		makeRateView = findViewById(R.id.layout_make_rate);
		makeRateView.setVisibility(View.GONE);
		/** 为界面标题赋值 */
//		setTitle(getResources().getString(R.string.isForex_bothway));
		isFinish = (Button) findViewById(R.id.manual_refresh);
		rate_waihui_Button = (Button) findViewById(R.id.rate_waihui_Button);
		CustomerRateButton = (Button) findViewById(R.id.rate_customerButton);
		rateListView = (ListView) findViewById(R.id.rate_listView);
		rateTimes = (TextView) findViewById(R.id.update_time);
		noMakeButton = (Button) findViewById(R.id.no_rate_make);
		noMakeButton.setVisibility(View.GONE);
		makeRate = findViewById(R.id.rate_make);
		makeRate.setVisibility(View.GONE);
		backButton = (Button) findViewById(R.id.ib_back);
		// ///////////////////////功能外置 按钮显示控制///////////////////////////////
		layout_button = (LinearLayout) findViewById(R.id.layout_button); // 全部汇率,我的汇率
//		layout_button.setVisibility(View.GONE);z
		mainUI = (Button) findViewById(R.id.ib_top_right_btn);
		mainUI.setVisibility(View.VISIBLE);
		mainUI.setText(getResources().getString(R.string.forex_rate_qick));
		// 右上角登陆按钮
		login = (Button) findViewById(R.id.ib_top_right_login_btn);
		login.setText(getResources().getString(R.string.forex_longin));
		login.setVisibility(View.GONE);
		allRateList = new ArrayList<Map<String, Object>>();
		customerRateList = new ArrayList<Map<String, Object>>();
		tradeDateList = new ArrayList<Map<String, Object>>();
		//wuhan
		rate_goldButton = (Button) this.findViewById(R.id.rate_goldButton);
		goldRateList = new ArrayList<Map<String,Object>>();
		left_title_ll = this.findViewById(R.id.left_title_ll);
		left_title = (TextView) this.findViewById(R.id.left_title);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(IsForexTwoWayTreasureActivity.this,(Button)CustomerRateButton);
		
		left_title_ll = this.findViewById(R.id.left_title_ll);
		left_title = (TextView) this.findViewById(R.id.left_title);
		rate_waihui_Button.setBackgroundResource(R.drawable.btn_top_waihui_write);//btn_top_waihui_write
		rate_waihui_Button.setTextColor(getResources().getColor(R.color.gray));
		rate_goldButton.setBackgroundResource(R.drawable.btn_top_waihui_red);//btn_top_waihui_red
		rate_goldButton.setTextColor(getResources().getColor(R.color.white));
	}

	public void initOnClick() {
		// 手动刷新按钮事件
		isFinish.setOnClickListener(this);
		// 返回按钮事件
		backButton.setOnClickListener(backBtnClick);
		// 主界面事件
		mainUI.setOnClickListener(this);
		// 汇率定制按钮事件
		makeRate.setOnClickListener(listener);
		// 全部外汇汇率
		rate_waihui_Button.setOnClickListener(this);
		// 查询用户定制的外汇汇率
		CustomerRateButton.setOnClickListener(this);
		// 登陆按钮点击事件
		login.setOnClickListener(this);
		rate_goldButton.setOnClickListener(this);
	}

	/** 返回点击事件 */
	OnClickListener backBtnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};

	@Override
	protected void onResumeFromLogin(boolean isLogin) {
		super.onResumeFromLogin(isLogin);
		/*
		 * 登陆状态 显示(右上角显示主界面,显示我的双向宝行情,全部行情,不显示我的汇率按钮)
		 * 在未登录状态显示(右上角显示登陆,不显示我的双向宝行情,全部行情按钮,不显示我的汇率按钮);
		 */
		if (BaseDroidApp.getInstanse().isLogin()) { // 登陆状态
			/** 为界面标题赋值 登录状态显示 双向宝行情 */
			setTitle(getResources().getString(R.string.isForex_rates));
			mainUI.setVisibility(View.VISIBLE); // 快速交易按钮显示
//			layout_button.setVisibility(View.VISIBLE); // 全部汇率,
			CustomerRateButton.setVisibility(View.VISIBLE);
			login.setVisibility(View.GONE); // 登陆按钮不显示
			isFinish.setVisibility(View.GONE); // 不显示刷新按钮
		} else { // 为登录状态
			/** 为界面标题赋值 未登录状态显示 双向宝 */
			setTitle(getResources().getString(R.string.isForex_bothway));
			login.setVisibility(View.VISIBLE); // 登陆按钮显示
//			layout_button.setVisibility(View.); // 全部汇率,登陆按钮显示
			CustomerRateButton.setVisibility(View.GONE);
			mainUI.setVisibility(View.GONE); // 快速交易按钮不显示
			isFinish.setVisibility(View.VISIBLE); // 显示刷新按钮
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		if (StringUtil.isNullOrEmpty(commConversationId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			// 判断用户是否开通投资理财服务
			requestPsnInvestmentManageIsOpen();
		}
	}

	/** 判断是否开通投资理财服务--二级菜单----回调 */
	@Override
	public void requestMenuIsOpenCallback(Object resultObj) {
		super.requestMenuIsOpenCallback(resultObj);
		if (isOpen) {
			// 查询是否签约保证金产品
			searchBaillAfterOpen = 1;
			requestPsnVFGBailListQueryCondition();
		} else {
			BaseHttpEngine.dissMissProgressDialog();
			searchBaillAfterOpen = 2;
			getPop();
		}
	}

	/** 查询是否签约保证金产品--------回调 */
	@Override
	public void requestPsnVFGBailListQueryConditionCallback(Object resultObj) {
		super.requestPsnVFGBailListQueryConditionCallback(resultObj);
		List<Map<String, String>> result = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.ISFOREX_RESULT_SIGN);
		if (result == null || result.size() <= 0) {
			isSign = false;
			isSettingAcc = false;
			BaseHttpEngine.dissMissProgressDialog();
			// 弹出任提示框
			getPop();
			return;
		} else {
			isSign = true;
			isCondition = true;
			// 是否设置双向宝账户
			requestPsnVFGGetBindAccount();
		}
	}

	/** 判断是否设置外汇双向宝账户 --------条件判断 */
	@Override
	public void requestConditionAccountCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> accReaultMap = (Map<String, String>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(accReaultMap)) {
			isSettingAcc = false;
		} else {
			String accountNumber = accReaultMap.get(IsForex.ISFOREX_ACCOUNTNUMBER_REQ);
			if (StringUtil.isNull(accountNumber)) {
				isSettingAcc = false;
			} else {
				isSettingAcc = true;
				BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_ACCREAULTMAP, accReaultMap);
			}
		}
//		 if (isOpen && isSign && isSettingAcc) {
//		 // 查询用户定制的汇率
//		 BaseDroidApp.getInstanse().dismissMessageDialogFore();
//		 requestPsnVFGCustomerSetRate();
//		 return;
//		 } else {
//		 BaseHttpEngine.dissMissProgressDialog();
//		 // 弹出任提示框
//		 getPop();
//		 return;
//		 }

		if (isOpen && isSign && isSettingAcc) {
			// /////////////////////功能外置数据
			switch (taskTag) {
			case 8: // 进入买卖页面交易购买
				getCustomerButton(); // TODO
				requestCustomerSetRate();
//				requestPsnVFGCustomerSetRate();
				requestAllRate("");
				break;
			case 4: // 第一次进入双向宝行情(wuhanP603默认双向宝行情)
				requestPsnVFGCustomerSetRate();
				break;			
			default:
				break;
			}
		} else {
			BaseHttpEngine.dissMissProgressDialog();
			// 弹出任提示框
			getPop();
			return;
		}
	}

	/** 查询用户定制的汇率-----回调 */
	public void requestPsnVFGCustomerSetRateCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		customerRateList = (List<Map<String, Object>>) biiResponseBody.getResult();
		if (customerRateList == null || customerRateList.size() <= 0) {
			// 用户定制的汇率为空，查询全部汇率 ,查询虚盘全部行情时上送空
			requestPsnVFGGetAllRate("");
//			requestPsnVFGCustomerSetRate();
			return;
		} else {
			// 我的外汇汇率选中效果-->我的双向宝
			getCustomerButton();
			BaseHttpEngine.dissMissProgressDialog();
			// 我的汇率数据
			getCustomerDate();
			// 7秒刷新
			refreshCustomerData();
		}
	}

	/** 全部汇率数据 G贵金属F外汇*/
	public void requestPsnVFGGetAllRateCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		allRateList = (List<Map<String, Object>>) biiResponseBody.getResult();
		// 按钮选中效果
		selectedAllButton(vfgType);
		// 7秒刷新
		refreshAllData(vfgType);
		if (allRateList == null || allRateList.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		tradeDateList.addAll(allRateList);
		BaseHttpEngine.dissMissProgressDialog();
		// 全部汇率数据 wuhan要改
		selectedAllRates();
	}

	/** 全部外汇汇率---按钮效果  -->外汇汇率 *///wuhan
	private void selectedAllButton(String paritiesType) {
		if(paritiesType.equals("F")){
			// 全部外汇汇率按钮默认被选中->外汇汇率
			coinId = 1;
			rate_waihui_Button.setBackgroundResource(R.drawable.btn_top_waihui_red);//btn_top_waihui_write
			rate_waihui_Button.setTextColor(getResources().getColor(R.color.white));
			CustomerRateButton.setBackgroundResource(R.drawable.btn_top_waihui_write);//btn_top_waihui_red
			CustomerRateButton.setTextColor(getResources().getColor(R.color.gray));
			rate_goldButton.setBackgroundResource(R.drawable.btn_top_waihui_write);//btn_top_waihui_red
			rate_goldButton.setTextColor(getResources().getColor(R.color.gray));
			rate_waihui_Button.setFocusable(true);
			rate_waihui_Button.setPressed(true);
			CustomerRateButton.setPressed(false);
			CustomerRateButton.setFocusable(false);
			rate_goldButton.setPressed(false);
			rate_goldButton.setFocusable(false);
			makeRate.setVisibility(View.GONE);
			makeRateView.setVisibility(View.GONE);
		}else if(paritiesType.equals("G")){
			coinId = 0;
			rate_waihui_Button.setBackgroundResource(R.drawable.btn_top_waihui_write);//btn_top_waihui_write
			rate_waihui_Button.setTextColor(getResources().getColor(R.color.gray));
			rate_goldButton.setBackgroundResource(R.drawable.btn_top_waihui_red);//btn_top_waihui_red
			rate_goldButton.setTextColor(getResources().getColor(R.color.white));
			
			CustomerRateButton.setBackgroundResource(R.drawable.btn_top_waihui_write);//btn_top_waihui_red
			CustomerRateButton.setTextColor(getResources().getColor(R.color.gray));
			rate_waihui_Button.setFocusable(false);
			rate_waihui_Button.setPressed(false);
			CustomerRateButton.setPressed(false);
			CustomerRateButton.setFocusable(false);
			rate_goldButton.setPressed(true);
			rate_goldButton.setFocusable(true);
			makeRate.setVisibility(View.GONE);
			makeRateView.setVisibility(View.GONE);
		}
	}

	/** 选择我的外汇按钮   我的双向宝*///wuhan
	private void getCustomerButton() {
		coinId = 2;
		CustomerRateButton.setBackgroundResource(R.drawable.btn_top_waihui_red);
		rate_waihui_Button.setBackgroundResource(R.drawable.btn_top_waihui_write);
		rate_goldButton.setBackgroundResource(R.drawable.btn_top_waihui_write);
		rate_waihui_Button.setTextColor(getResources().getColor(R.color.gray));
		CustomerRateButton.setTextColor(getResources().getColor(R.color.white));
		rate_goldButton.setTextColor(getResources().getColor(R.color.gray));
		CustomerRateButton.setFocusable(true);
		CustomerRateButton.setPressed(true);
		rate_waihui_Button.setFocusable(false);
		rate_waihui_Button.setPressed(false);
		rate_goldButton.setPressed(false);
		rate_goldButton.setFocusable(false);
		makeRate.setVisibility(View.VISIBLE);
		makeRateView.setVisibility(View.GONE);
	}
	
	/** 选择全部汇率按钮 */
	private void selectedAllRates() {
		// 处理数据
		allRateList = getTrueDate(allRateList);
		if (allRateList == null || allRateList.size() <= 0) {
			return;
		}
		isHasAllList = true;
		getAllAdapterListener(allRateList);
		// 刷新时间
		refreshTimes(allRateList);
	}

	/** 全部外汇汇率-----适配器监听事件 */
	private void getAllAdapterListener(List<Map<String, Object>> allRateList) {
		adapter = new IsForexRateInfoAdapter(IsForexTwoWayTreasureActivity.this, allRateList);
		rateListView.setAdapter(adapter);
		adapter.setBuyImgOnItemClickListener(buyImgAllOnItemClickListener);
		adapter.setSellImgOnItemClickListener(sellImgAllOnItemClickListener);
	}

	/** 刷新时间 */
	private void refreshTimes(List<Map<String, Object>> list) {
		Map<String, Object> map = list.get(0);
		String times = (String) map.get(IsForex.ISFOREX_RATE_UPDATEDATE_RES);
		rateTimes.setText(times);
	}

	/** 处理货币对，返回的货币对可能没有对应的名称，将其除去 */
	private List<Map<String, Object>> getTrueDate(List<Map<String, Object>> list) {
		int len = list.size();
		List<Map<String, Object>> dateList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < len; i++) {
			Map<String, Object> map = (Map<String, Object>) list.get(i);
			// 得到源货币的代码
			String sourceCurrencyCode = (String) map.get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
			String targetCurrencyCode = (String) map.get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
			if (!StringUtil.isNull(sourceCurrencyCode) && !StringUtil.isNull(targetCurrencyCode)
					&& LocalData.Currency.containsKey(sourceCurrencyCode) && LocalData.Currency.containsKey(targetCurrencyCode)) {
				dateList.add(map);
			}
		}
		return dateList;
	}

	

	/** 我的外汇汇率数据 */
	private void getCustomerDate() {
		customerRateList = getTrueDate(customerRateList);
		if (customerRateList == null || customerRateList.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		isHasCustomerList = true;
		// 监听事件
		getCustomerAdapterListener(customerRateList);
		// 刷新时间
		refreshTimes(customerRateList);
	}

	/** 我的外汇汇率----监听事件 */
	private void getCustomerAdapterListener(List<Map<String, Object>> customerRateList) {
		adapter = new IsForexRateInfoAdapter(IsForexTwoWayTreasureActivity.this, customerRateList);
		rateListView.setAdapter(adapter);
		adapter.setBuyImgOnItemClickListener(buyImgCusOnItemClickListener);
		adapter.setSellImgOnItemClickListener(sellImgCusOnItemClickListener);
	}

	/** 用户定制的货币对-7秒刷新 */
	private void refreshCustomerData() {
		if (!PollingRequestThread.pollingFlag) {
			requestCustomerRatesPoling();
		}
	}

	/** 全部汇率-7秒刷新 */
	private void refreshAllData(String vfgType) {
		if (!PollingRequestThread.pollingFlag) {
			requestAllRatesPoling(vfgType);
		}
	}

	/** 7秒刷新 我的外汇汇率 --->我的双向宝*/
	private void requestCustomerRatesPoling() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGCUSTOMERSETRATE_API);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(null);
		HttpManager.requestPollingBii(biiRequestBody, allHttpHandler, ConstantGloble.FOREX_REFRESH_TIMES);// 7秒刷新
	}

	/** 7秒刷新-全部汇率 --->外汇*/
	private void requestAllRatesPoling(String vfgType) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		Map<String, String> params = new HashMap<String, String>();
		params.put("vfgType", vfgType);
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGGETALLRATE_API);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(params);
		HttpManager.requestPollingBii(biiRequestBody, allHttpHandler, ConstantGloble.FOREX_REFRESH_TIMES);// 7秒刷新
	}

	/** 每次查询前清空更新时间数据 */
	private void clearTimes() {
		rateTimes.setText("");
	}

	/** 每次点击按钮前，清空数据 */
	private void clearDate() {
		if (allRateList != null && !allRateList.isEmpty()) {
			allRateList.clear();
			adapter.notifyDataSetChanged();
		}
		if (customerRateList != null && !customerRateList.isEmpty()) {
			customerRateList.clear();
			adapter.notifyDataSetChanged();
		}
		
		//wuhan要添加账户贵金属的
		if(goldRateList !=null && !goldRateList.isEmpty()){
			goldRateList.clear();
			adapter.notifyDataSetChanged();
		}
	}

	private Handler allHttpHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// http状态码
			String resultHttpCode = (String) ((Map<String, Object>) msg.obj).get(ConstantGloble.HTTP_RESULT_CODE);
			// 返回数据
			Object resultObj = ((Map<String, Object>) msg.obj).get(ConstantGloble.HTTP_RESULT_DATA);
			// 回调对象
			HttpObserver callbackObject = (HttpObserver) ((Map<String, Object>) msg.obj)
					.get(ConstantGloble.HTTP_CALLBACK_OBJECT);
			// 回调方法
			String callBackMethod = (String) ((Map<String, Object>) msg.obj).get(ConstantGloble.HTTP_CALLBACK_METHOD);
			switch (msg.what) {
			// 正常http请求数据返回
			case ConstantGloble.HTTP_STAGE_CONTENT:
				/** 执行全局前拦截器 */
				if (BaseDroidApp.getInstanse().httpRequestCallBackPre(resultObj)) {
					break;
				}

				/** 执行callbackObject回调前拦截器 */
				if (httpRequestCallBackPre(resultObj)) {
					break;
				}
				// 清空更新时间
				clearTimes();
				// 清空数据
				clearDate();
				BiiResponse biiResponse = (BiiResponse) ((Map<String, Object>) msg.obj).get(ConstantGloble.HTTP_RESULT_DATA);
				List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
				BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
				if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
					return;
				}
				switch (coinId) {
				case 0://贵金属wuhan
//					goldRateList = (List<Map<String, Object>>) biiResponseBody.getResult();
//					if(goldRateList == null || goldRateList.size() <=0){
//						isHasGoldList = false;
//						return ;
//					}else {
//						
//					}
					
					allRateList = (List<Map<String, Object>>) biiResponseBody.getResult();
					if (allRateList == null || allRateList.size() <= 0) {
						isHasAllList = false;
						return;
					}else{

						if (tradeDateList != null && !tradeDateList.isEmpty()) {
							tradeDateList.clear();
						}
						tradeDateList.addAll(allRateList);
						// 将得到的数据进行处理
						allRateList = getTrueDate(allRateList);
						if (allRateList == null || allRateList.size() <= 0) {
							isHasAllList = false;
							return;
						}
						refreshTimes(allRateList);
						if (isHasAllList) {
							adapter.dataChaged(allRateList);
						} else {
							isHasAllList = true;
							getAllAdapterListener(allRateList);
						}
					
					} 
					
					
					break;
				case 1:// 全部外汇--->外汇
					allRateList = (List<Map<String, Object>>) biiResponseBody.getResult();
					if (allRateList == null || allRateList.size() <= 0) {
						isHasAllList = false;
						return;
					} else {
						if (tradeDateList != null && !tradeDateList.isEmpty()) {
							tradeDateList.clear();
						}
						tradeDateList.addAll(allRateList);
						// 将得到的数据进行处理
						allRateList = getTrueDate(allRateList);
						if (allRateList == null || allRateList.size() <= 0) {
							isHasAllList = false;
							return;
						}
						refreshTimes(allRateList);
						if (isHasAllList) {
							adapter.dataChaged(allRateList);
						} else {
							isHasAllList = true;
							getAllAdapterListener(allRateList);
						}
					}
					break;
				case 2:// 我的外汇汇率---->双向宝
						// 得到customerRateList
					customerRateList = (List<Map<String, Object>>) biiResponseBody.getResult();
					if (customerRateList == null || customerRateList.size() <= 0) {
						isHasCustomerList = false;
						return;
					} else {
						// 将得到的数据进行处理
						customerRateList = getTrueDate(customerRateList);
						if (customerRateList == null || customerRateList.size() <= 0) {
							isHasCustomerList = false;
							return;
						}
						refreshTimes(customerRateList);
						if (isHasCustomerList) {
							adapter.dataChaged(customerRateList);
						} else {
							isHasCustomerList = true;
							getCustomerAdapterListener(customerRateList);
						}
						//保存省联号
						String ibkNum = (String) customerRateList.get(0).get(IsForex.ISFOREX_ibkNum);
						LocalDataService.getInstance().saveIbkNum(ConstantGloble.IsForex,ibkNum);
					}
					break;
				default:
					break;
				}
				/** 执行callbackObject回调后拦截器 */
				if (httpRequestCallBackAfter(resultObj)) {
					break;
				}

				/** 执行全局后拦截器 */
				if (BaseDroidApp.getInstanse().httpRequestCallBackAfter(resultObj)) {
					break;
				}
				break;

			// 请求失败错误情况处理
			case ConstantGloble.HTTP_STAGE_CODE:
				/** 执行code error 全局前拦截器 */
				if (BaseDroidApp.getInstanse().httpCodeErrorCallBackPre(resultHttpCode)) {
					break;
				}

				/** 执行callbackObject error code 回调前拦截器 */
				if (callbackObject.httpCodeErrorCallBackPre(resultHttpCode)) {
					break;
				}

				Method httpCodeCallbackMethod = null;
				try {
					// 回调
					httpCodeCallbackMethod = callbackObject.getClass().getMethod(callBackMethod, String.class);
					httpCodeCallbackMethod.invoke(callbackObject, resultHttpCode);
				} catch (SecurityException e) {
					LogGloble.e(TAG, "SecurityException ", e);
				} catch (NoSuchMethodException e) {
					LogGloble.e(TAG, "NoSuchMethodException ", e);
				} catch (IllegalArgumentException e) {
					LogGloble.e(TAG, "IllegalArgumentException ", e);
				} catch (IllegalAccessException e) {
					LogGloble.e(TAG, "IllegalAccessException ", e);
				} catch (InvocationTargetException e) {
					LogGloble.e(TAG, "InvocationTargetException ", e);
				} catch (NullPointerException e) {
					// add by wez 2012.11.06
					LogGloble.e(TAG, "NullPointerException ", e);
					throw e;
				} catch (ClassCastException e) {
					// add by wez 2012.11.06
					LogGloble.e(TAG, "ClassCastException ", e);
					throw e;
				}

				/** 执行code error 全局后拦截器 */
				if (BaseDroidApp.getInstanse().httpCodeErrorCallBackAfter(resultHttpCode)) {
					break;
				}

				/** 执行callbackObject code error 后拦截器 */
				if (callbackObject.httpCodeErrorCallBackAfter(resultHttpCode)) {
					break;
				}
				break;
			default:
				break;
			}
		}
	};
	
	/** 银行买价图片事件---- 用户 双向宝行情*/
	private OnItemClickListener buyImgCusOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			quickOrRate = 2;
			// 买卖方向--卖出
			directionName = LocalData.isForexSellTagList.get(1);
			allOrCustomerReq = 1;
			codeName = getCodeName(customerRateList, position);
			if (StringUtil.isNull(codeName)) {
				return;
			}
			BaseHttpEngine.showProgressDialog();
			requestAllRate(vfgType);
		}
	};
	/** 银行卖价图片事件 用户定制的货币对 */
	private OnItemClickListener sellImgCusOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			quickOrRate = 2;
			// 买卖方向--卖出
			directionName = LocalData.isForexSellTagList.get(0);
			allOrCustomerReq = 1;
			codeName = getCodeName(customerRateList, position);
			if (StringUtil.isNull(codeName)) {
				return;
			}
			// BaseHttpEngine.showProgressDialog();
			// requestAllRate();
			exeRatePsnForexActIsset(vfgType);// 功能外置修改
		}
	};
	
	/** 银行买价图片事件 全部货币对 */
	private OnItemClickListener buyImgAllOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			quickOrRate = 2;
			// 买卖方向--卖出
			directionName = LocalData.isForexSellTagList.get(1);
			if(BaseDroidApp.getInstanse().isLogin()){
				if(vfgType.equals("G")){
					allOrCustomerReq = 2;
				}else{
					allOrCustomerReq = 0;
				}
			}else{
				if(paritiesType.equals("G")){
					allOrCustomerReq = 2;
				}else{
					allOrCustomerReq = 0;
				}
			}
//			allOrCustomerReq = 0;
			codeName = getCodeName(allRateList, position);
			if (StringUtil.isNull(codeName)) {
				return;
			}
			// BaseHttpEngine.showProgressDialog();
			// requestAllRate();
			exeRatePsnForexActIsset(vfgType);// 功能外置修改
		}
	};
	/** 银行卖价图片事件 全部货币对 */
	private OnItemClickListener sellImgAllOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			quickOrRate = 2;
			// 买卖方向--卖出
			directionName = LocalData.isForexSellTagList.get(0);
//			allOrCustomerReq = 1;
			if(BaseDroidApp.getInstanse().isLogin()){
				if(vfgType.equals("G")){
					allOrCustomerReq = 2;
				}else{
					allOrCustomerReq = 0;
				}
			}else{
				if(paritiesType.equals("G")){
					allOrCustomerReq = 2;
				}else{
					allOrCustomerReq = 0;
				}
			}
			codeName = getCodeName(allRateList, position);
			if (StringUtil.isNull(codeName)) {
				return;
			}
			// BaseHttpEngine.showProgressDialog();
			// requestAllRate();
			exeRatePsnForexActIsset(vfgType);// 功能外置修改
		}
	};


	/** 得到用户选择的货币对名称 */
	private String getCodeName(List<Map<String, Object>> lists, int position) {
		String codeNames = null;
		Map<String, Object> map = lists.get(position);
		String sourceCurrencyCode = (String) map.get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
		String targetCurrencyCode = (String) map.get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
		// 货币对
		if (!StringUtil.isNull(sourceCurrencyCode) && !StringUtil.isNull(targetCurrencyCode)) {
			String sourceCurrency = LocalData.Currency.get(sourceCurrencyCode);
			String targetCurrency = LocalData.Currency.get(targetCurrencyCode);
			codeNames = sourceCurrency + "/" + targetCurrency;
//			人民币金、银、铂、钯、美元金、银、铂、钯   贵金属的币种
			if(LocalData.goldLists.contains(sourceCurrency)||LocalData.goldLists.contains(targetCurrency)){
				vfgType = "G";
			}else {
				vfgType = "F";
			}
		}

		
		
		return codeNames;
	}

	/** 汇率定制按钮统一事件*/
	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 跳转到汇率定制页面
			stopPollingFlag();
			Intent intent = new Intent(IsForexTwoWayTreasureActivity.this, IsForexMakeRateActivity.class);
			startActivityForResult(intent, ConstantGloble.ISFOREX_TRADE_CODE_ACTIVITY);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.manual_refresh: // 手动刷新
			if(isGold){//贵金属行情
				paritiesType = "G";
			}else {//外汇行情
				paritiesType = "F";
			}
			requestPsnGetAllExchangeRatesOutlay(paritiesType);
			break;
		case R.id.rate_goldButton://贵金属行情
			if(BaseDroidApp.getInstanse().isLogin()){
				stopPollingFlag();
				vfgType = "G";
				coinId = 0;
				taskTag = 4;
				allOrCustomerReq = 2;
//				customerOrTrade	=	
				clearDate();
				clearTimes();
				// 查询贵金属
				BaseHttpEngine.showProgressDialog();
				requestPsnVFGGetAllRate(vfgType);
			}else{
				paritiesType = "G";
				isGold = true;
				requestPsnGetAllExchangeRatesOutlay(paritiesType);
			}
			break;
		case R.id.rate_waihui_Button: // 全部外汇汇率---->改为外汇汇率
			if(BaseDroidApp.getInstanse().isLogin()){
				stopPollingFlag();
				vfgType = "F";
				coinId = 1;
				taskTag = 4;
				allOrCustomerReq = 0;
				customerOrTrade = 3;
				clearDate();
				clearTimes();
				// 查询全部汇率
				BaseHttpEngine.showProgressDialog();
				//wuhan 要改的 改为外汇汇率的请求接口
				requestPsnVFGGetAllRate(vfgType);
			}else{
				paritiesType = "F";
				isGold = false;
				requestPsnGetAllExchangeRatesOutlay(paritiesType);
			}
		
			break;
		case R.id.rate_customerButton: // 我的外汇汇率--> 我的双向宝
			
			stopPollingFlag();
			coinId = 2;
			taskTag = 4;
			customerOrTrade = 2;
			allOrCustomerReq = 1;
			clearDate();
			clearTimes();
			// 查询用户定制的汇率
			BaseHttpEngine.showProgressDialog();
			requestCustomerSetRate();//??????????vfgType wuhan 
			break;
		case R.id.ib_back: // 返回按钮
			stopPollingFlag();
//			ActivityTaskManager.getInstance().removeAllSecondActivity();
//			Intent intent = new Intent();
//			intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), SecondMainActivity.class);
//			BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
			goToMainActivity();
			break;
		case R.id.ib_top_right_btn: // 快速交易
			customerOrTrade = 1;
			allOrCustomerReq = 1;
			BaseHttpEngine.showProgressDialog();
			requestAllRate("");
			break;
		case R.id.ib_top_right_login_btn: // 登录
//			stopPollingFlag();
//			ActivityTaskManager.getInstance().removeAllSecondActivity();
//			Intent intent1 = new Intent();
//			intent1.setClass(BaseDroidApp.getInstanse(), LoginActivity.class);
//			startActivityForResult(intent1, ConstantGloble.ISFOREX_TRADE_CODE_ACTIVITY);
//			break;
		}
	}

	/** 我的汇率---点击按钮时触发 */
	@Override
	public void requestCustomerSetRateCallback(Object resultObj) {
		super.requestCustomerSetRateCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		customerRateList = (List<Map<String, Object>>) biiResponseBody.getResult();
		// 我的外汇汇率选中效果
		getCustomerButton();
		BaseHttpEngine.dissMissProgressDialog();
		if (customerRateList == null || customerRateList.size() <= 0) {
			// 用户定制的汇率为空,需进行汇率定制
			makeRateView.setVisibility(View.VISIBLE);
			// 新布局里面的汇率定制按钮事件-------不在使用
			noMakeButton.setOnClickListener(listener);
			return;
		} else {
			// 我的汇率数据
			getCustomerDate();
			// 7秒刷新
			refreshCustomerData();
		}
	}

	@Override
	public void requestAllRateCallback(Object resultObj) {
		super.requestAllRateCallback(resultObj);
		if (codeResultList == null || codeResultList.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					IsForexTwoWayTreasureActivity.this.getResources().getString(R.string.isForex_trade_code));
			return;
		}
		if(quickOrRate == 1){
		// 查询结算币种dealCodeDate
		dealCodeDate();
		String 	codeCodeName =	codeDealCodeNameList.get(0);
		String codes[] = codeCodeName.split("/");
		String sourceCurrency = codes[0];
		String targetCurrency = codes[1];
//		人民币金、银、铂、钯、美元金、银、铂、钯   贵金属的币种
		if(LocalData.goldLists.contains(sourceCurrency)||LocalData.goldLists.contains(targetCurrency)){
				vfgType = "G";
		}else {
			vfgType = "F";
			}	
		}
			
		// 查询结算币种
		requestPsnVFGGetRegCurrency(vfgType);//wuhan 要改
	}

	// 查询结算币种----回调
	@Override
	public void requestPsnVFGGetRegCurrencyCallback(Object resultObj) {
		super.requestPsnVFGGetRegCurrencyCallback(resultObj);
		if (vfgRegCurrencyList == null || vfgRegCurrencyList.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					IsForexTwoWayTreasureActivity.this.getResources().getString(R.string.isForex_trade_jcCode));
			return;
		}
		tradeSwitch();
	}

	/** 处理结算币种数据 */
	private void dealVfgRegDate() {
		int len = vfgRegCurrencyList.size();
		vfgRegDealCodeList = new ArrayList<String>();
		vfgRegDealCodeNameList = new ArrayList<String>();
		for (int i = 0; i < len; i++) {
			String codes = vfgRegCurrencyList.get(i);
			if (!StringUtil.isNull(codes) && LocalData.Currency.containsKey(codes)) {
				vfgRegDealCodeList.add(codes);
				vfgRegDealCodeNameList.add(LocalData.Currency.get(codes));
			}
		}
	}
	/** 处理后---结算币种代码 */
	private List<String> vfgRegDealCodeList = null;
	/** 处理后---结算币种代码名称 */
	private List<String> vfgRegDealCodeNameList = null;
	/** 处理后---源货币对代码 */
	private List<String> sourceCodeDealCodeList = null;
	/** 处理后---目标货币对代码 */
	private List<String> targetCodeDealCodeList = null;
	/** 处理后---货币对代码名称 */
	private List<String> codeDealCodeNameList = null;
	/** 处理后---货币对代码 */
	private List<String> codeDealCodeList = null;
	
	/** 处理货币对 */
	private void dealCodeDate() {
		int len = codeResultList.size();
		sourceCodeDealCodeList = new ArrayList<String>();
		targetCodeDealCodeList = new ArrayList<String>();
		codeDealCodeNameList = new ArrayList<String>();
		codeDealCodeList = new ArrayList<String>();
		for (int i = 0; i < len; i++) {
			Map<String, Object> map = codeResultList.get(i);
			String sourceCurrencyCode = (String) map.get(IsForex.ISFOREX_SOURCECURRENCYCODE_RES);
			String targetCurrencyCode = (String) map.get(IsForex.ISFOREX_TARGETCURRENCYCODE_RES);
			if (!StringUtil.isNull(sourceCurrencyCode) && LocalData.Currency.containsKey(sourceCurrencyCode)
					&& !StringUtil.isNull(targetCurrencyCode) && LocalData.Currency.containsKey(targetCurrencyCode)) {
				sourceCodeDealCodeList.add(sourceCurrencyCode);
				targetCodeDealCodeList.add(targetCurrencyCode);
				String source = LocalData.Currency.get(sourceCurrencyCode);
				String target = LocalData.Currency.get(targetCurrencyCode);
				codeDealCodeList.add(sourceCurrencyCode + "/" + targetCurrencyCode);
				codeDealCodeNameList.add(source + "/" + target);
			}
		}
	}
	/** 快速交易--银行卖价---银行买价 --跳转到双向宝交易页面 */
	private void tradeSwitch() {
		stopPollingFlag();
		if (!isFinishing()) {
			Intent intent = new Intent(IsForexTwoWayTreasureActivity.this, IsForexTradeSubmitActivity.class);
			switch (quickOrRate) {
			case 1:// 快速交易
			
				
				intent.putExtra(ConstantGloble.ISFOREX_REQUEXTTRADE_KEY, ConstantGloble.ISFOREX_MYRATE_TRADE_ACTIVITY);// 101
				startActivityForResult(intent, ConstantGloble.ISFOREX_TRADE_CODE_ACTIVITY);
				break;
			case 2:// 银行买卖价
				intent.putExtra(ConstantGloble.ISFOREX_CODECODENAME_KEY, codeName);
				intent.putExtra(ConstantGloble.ISFOREX_DIRECTION_KEY, directionName);
				intent.putExtra(ConstantGloble.ISFOREX_REQUEXTTRADE_KEY, ConstantGloble.ISFOREX_MINE_RATE_ACTIVITY);// 401
				startActivityForResult(intent, ConstantGloble.ISFOREX_TRADE_CODE_ACTIVITY);
				break;
			default:
				break;
			}
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == ConstantGloble.ACTIVITY_REQUEST_LOGIN_CODE) {
				// 功能外置添加，点击标题栏登陆按钮登陆成功
				setLeftSelectedPosition("isForexStorageCash_1");
				taskTag = 4;
				requestCommConversationId();
			}
		}
		if (requestCode == ConstantGloble.ISFOREX_TRADE_CODE_ACTIVITY) {
			// 交易返回页面
			if (!rate_waihui_Button.isSelected() && !CustomerRateButton.isSelected()) {
				CustomerRateButton.performClick();
			}
		}
	}

	// ////////////////////////////////全部数据未登录///////////////////////////////////////
	private void exeRatePsnForexActIsset(final String vfgType) {
		// 执行登陆
		BaseActivity.getLoginUtils(IsForexTwoWayTreasureActivity.this).exe(new LoginCallback() {
			@Override
			public void loginStatua(boolean isLogin) {
				if (isLogin) {
					left_title_ll.setVisibility(View.VISIBLE);
					left_title.setText(getResources().getString(R.string.for_reference_only));
					if (isOpen && isSettingAcc) {
						// 进行交易请求
						// BaseHttpEngine.showProgressDialog();
						// requestCustomerSetRate();
						BaseHttpEngine.showProgressDialog();
						requestAllRate(vfgType);//wuhan  要改
					} else {
						// 判断是否开通
						taskTag = 8;
						// 功能外置添加，点击银行买入卖出价 未登录
						setLeftSelectedPosition("isForexStorageCash_1");
						requestCommConversationId();
					}
				}else{
					left_title_ll.setVisibility(View.VISIBLE);
					left_title.setText(getResources().getString(R.string.for_reference_only));
				}
			}
		});
	}

	/** 查询全部汇率----货币对 */
	public void requestPsnGetAllExchangeRatesOutlay(String paritiesType) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PsnGetAllExchangeRatesOutlay_API);
		Map<String, String> params = new HashMap<String, String>();
		// 客户登记的交易账户的省行联行号 X-FUND联行号
		params.put(IsForex.ISFOREX_ibknum, LocalDataService.getInstance().getIbkNum(ConstantGloble.IsForex));
		// 牌价类型(F外汇;G黄金;N 930牌价;M向宝双)
		params.put(IsForex.ISFOREX_paritiesType, paritiesType);
		params.put("offerType", "M");
		biiRequestBody.setParams(params);
		HttpManager.requestOutlayBii(biiRequestBody, this, "requestPsnGetAllExchangeRatesOutlayCallback");
		
		////////////////////////本地挡板
//		HttpManager.requestBii(biiRequestBody, this, "requestPsnGetAllExchangeRatesOutlayCallback");
	}
	
	private void getWaiOrGoldOutlayButton(String paritiesType) {
		if(paritiesType.equals("F")){
			rate_waihui_Button.setBackgroundResource(R.drawable.btn_top_waihui_red);
			rate_waihui_Button.setTextColor(getResources().getColor(R.color.white));
			rate_goldButton.setBackgroundResource(R.drawable.btn_top_waihui_write);
			rate_goldButton.setTextColor(getResources().getColor(R.color.gray));
			rate_waihui_Button.setFocusable(true);
			rate_waihui_Button.setPressed(true);
			rate_goldButton.setPressed(false);
			rate_goldButton.setFocusable(false);
			CustomerRateButton.setVisibility(View.GONE);
			makeRate.setVisibility(View.GONE);
			makeRateView.setVisibility(View.GONE);
		}else if(paritiesType.equals("G")){
			rate_waihui_Button.setBackgroundResource(R.drawable.btn_top_waihui_write);
			rate_waihui_Button.setTextColor(getResources().getColor(R.color.gray));
			rate_goldButton.setBackgroundResource(R.drawable.btn_top_waihui_red);
			rate_goldButton.setTextColor(getResources().getColor(R.color.white));
			rate_waihui_Button.setFocusable(false);
			rate_waihui_Button.setPressed(false);
			rate_goldButton.setPressed(true);
			rate_goldButton.setFocusable(true);
			CustomerRateButton.setVisibility(View.GONE);
			makeRate.setVisibility(View.GONE);
			makeRateView.setVisibility(View.GONE);
		}
		
		
	}
	
	/** 全部汇率数据 回调 */
	public void requestPsnGetAllExchangeRatesOutlayCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> result = (List<Map<String, Object>>) biiResponseBody.getResult();
		// TODO
		if (StringUtil.isNullOrEmpty(result) || StringUtil.isNullOrEmpty(result)) {
			isHasAllList = false;
			refreshAllAdapterListenerOutlay(new ArrayList<Map<String, Object>>()); // 清空
		} else {
			getWaiOrGoldOutlayButton(paritiesType);
			allRateList = result;
			// 将得到的数据进行处理
			allRateList = getTrueDate(allRateList);
			isHasAllList = true;
			refreshOutlayTimes(allRateList);
			refreshAllAdapterListenerOutlay(allRateList);
		}
	}

	/** 全部外汇汇率-----适配器监听事件 */
	private void refreshAllAdapterListenerOutlay(List<Map<String, Object>> allRateList) {
		if (adapter == null) {
			adapter = new IsForexRateInfoAdapter(IsForexTwoWayTreasureActivity.this, allRateList);
			rateListView.setAdapter(adapter);
			adapter.setBuyImgOnItemClickListener(buyImgAllOnItemClickListener);
			adapter.setSellImgOnItemClickListener(sellImgAllOnItemClickListener);
		} else {
			adapter.dataChaged(allRateList);
		}
	}

	/** 刷新功能外置时间 */
	private void refreshOutlayTimes(List<Map<String, Object>> list) {
		Map<String, Object> map = list.get(0);
		String times = (String) map.get(IsForex.ISFOREX_RATE_UPDATEDATE_RES);
		rateTimes.setText(times);
	}
}
