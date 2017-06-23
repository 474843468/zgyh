package com.chinamworld.bocmbci.biz.forex.rate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.biz.forex.ForexBaseActivity;
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
import com.chinamworld.bocmbci.widget.adapter.ForexRateInfoAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 外汇行情页面，默认显示全部外汇汇率
 * 
 * 功能外置 @see ForexRateInfoOutlayActivity
 * 
 * @author 宁焰红
 * @category 显示外汇汇率信息。
 */
public class ForexRateInfoOutlayActivity extends ForexBaseActivity implements OnClickListener {
	private static final String TAG = "ForexRateInfoOutlayActivity";
	/** ForexRateInfoOutlayActivity的主布局 */
	private View rateInfoView = null;
	/** 全部外汇汇率 */
	private Button allRateButton = null;
	/** 我的外汇汇率 */
	private Button CustomerRateButton = null;
	/** 汇率定制按钮 在全部外汇汇率页面中，是隐藏的。 在我的外汇汇率页面中，是显示出来的。 */
	private View makeRate = null;
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
	/** quickTrade：快速交易 */
	private Button quickTrade = null;
	private ForexRateInfoAdapter adapter = null;
	/** 用户没有定制外汇汇率时，点击“我的外汇汇率”时加载新布局 */
//	private View noMakeView = null;
	/** noMakeView布局里面的按钮--汇率定制按钮 */
	private Button noMakeButton = null;
	/** 区分用户选中的是那个按钮 1:allRateButton;2:CustomerRateButton进行刷新数据 */
	private int coinId = 2;
	/** 请求标志 0-快速交易 1-银行买价 2-银行卖价 */
	private int requestTag = -1;
	/** 买入币种的position */
	// private int buySelectedPosition = -1;
	/** 卖出币种的 position */
	// private int sellSelectedPosition = -1;
	/** 用户选择的 数据 */
	private Map<String, Object> selectedMap;
	/** 0-全部货币对，1-用户定制的货币战对 */
	private int allOrCustomerReq = -1;
	/** 银行卖价 目标货币对 卖出币种代码 */
	private String targetCode = null;

	/** 银行买价 目标货币对 买入币种代码 */
	private String sourceCurCde = null;
	/** 银行买价 目标货币对 买入币种名称 */
//	private String sourceCodeName = null;

	/** 用于区别是定期还是活期请求查询买入币种列表信息 1-活期 2-定期 */
	private int fixOrCurrency = -1;
	/** 点击快速交易还是银行卖价，1-快速交易，2-银行卖价,3-银行买价 */
	private int quickOrRateSell = 1;
	/** 顶部按钮view */
	private View makeRateView = null;
	/** 处理前，买入币种代码 */
	private List<String> buyCodeList = null;
	/** 处理后，买入币种名称 */
	private List<String> buyCodeDealList = null;
	/** 全部汇率是否有数据 */
	private boolean isHasAllList = false;
	/** 我的外汇汇率是否有数据 */
	private boolean isHasCustomerList = false;

	// -----------------------------------功能外置
	/** 用户手动刷新布局 */
	private View userRefreshLayout;
	/** 用户手动刷新按钮 */
	private View manualRefreshView;
	/** 手动刷新时间标签 */
	private TextView updateTimeView;

	// ---------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		menuOrTrade = 1;
		taskTag = 4;
		init();
		initOnClick();
		// 将左侧菜单选中第一个

		setLeftSelectedPosition(BaseDroidApp.getInstanse().isLogin() ? "forexStorageCash_1" : null);

		/**
		 * 功能外置修改,以登陆先判断是否开通理财，未登录获取未登录全部行情 用户点击标题栏登陆按钮:
		 * ->登陆成功回调onActivityResult通过ACTIVITY_REQUEST_LOGIN_CODE判断是标题栏入口，
		 * 然后判断是否开通理财进而进行页面数据刷新 ->登陆失败不做任何事情 用户点击买入或卖出后登陆 ->登陆成功先判断是否已经开通理财和设置账户
		 * ->没有开通理财和设置账户，taskTag = 8调用判断理财是否开通 ->已经开通理财和设置账号直接进行交易 ->登陆失败不做任何事情
		 * 启动轮询查询机制在 onActivityResult 通过FOREX_RATE_TRADE_TAG 判断是从上一个交易页面返回，
		 * 如果全部汇率按钮和我的汇率按钮都没有被按钮则默认按下我的汇率
		 */
		if (BaseDroidApp.getInstanse().isLogin()) {
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
			manualRefreshView.performClick();
		}
	}

	/**
	 * 初始化所有的控件
	 */
	private void init() {
		rateInfoView = LayoutInflater.from(ForexRateInfoOutlayActivity.this).inflate(R.layout.forex_rate_outlay_main, null);
		tabcontent.addView(rateInfoView);
		((TextView) rateInfoView.findViewById(R.id.left_title)).setText(getResources().getString(R.string.for_reference_only));
		// 汇率定制页面
		makeRateView = findViewById(R.id.layout_make_rate);
		makeRateView.setVisibility(View.GONE);
		noMakeButton = (Button) findViewById(R.id.no_rate_make);
		/** 为界面标题赋值 */
		setTitle(getResources().getString(R.string.forex_rate));
		allRateButton = (Button) findViewById(R.id.rate_allButton);
		CustomerRateButton = (Button) findViewById(R.id.rate_customerButton);
		rateListView = (ListView) findViewById(R.id.rate_listView);
		rateTimes = (TextView) findViewById(R.id.forex_rate_times);
		makeRate = findViewById(R.id.rate_make);
		makeRate.setVisibility(View.GONE);
		backButton = (Button) findViewById(R.id.ib_back);
		quickTrade = (Button) findViewById(R.id.ib_top_right_btn);

		// 功能外置
		userRefreshLayout = findViewById(R.id.user_refresh_layout);
		manualRefreshView = userRefreshLayout.findViewById(R.id.manual_refresh);
		updateTimeView = (TextView) userRefreshLayout.findViewById(R.id.update_time);

		quickTrade.setVisibility(View.VISIBLE);
		quickTrade.setText(getResources().getString(R.string.forex_rate_qick));
		// 7秒钟
		String time = ConstantGloble.FOREX_REFRESH_TIME;
		allRateList = new ArrayList<Map<String, Object>>();
		customerRateList = new ArrayList<Map<String, Object>>();
	}

	public void initOnClick() {
		// 全部外汇汇率
		allRateButton.setOnClickListener(this);
		// 查询用户定制的外汇汇率
		CustomerRateButton.setOnClickListener(this);
		// 返回按钮事件
		backButton.setOnClickListener(this);
		// 汇率定制按钮事件
		makeRate.setOnClickListener(listener);
		// 快速交易事件
		quickTrade.setOnClickListener(this);
		// ----------------功能外置
		// 手动刷新
		manualRefreshView.setOnClickListener(this);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == ConstantGloble.ACTIVITY_REQUEST_LOGIN_CODE) {
				// 功能外置添加，点击标题栏登陆按钮登陆成功
				setLeftSelectedPosition("forexStorageCash_1");
				taskTag = 4;
				requestCommConversationId();
			}
		}
		if (requestCode == ConstantGloble.FOREX_RATE_TRADE_TAG) {
			// 交易返回页面
			if (!allRateButton.isSelected() && !CustomerRateButton.isSelected()) {
				CustomerRateButton.performClick();
			}
		}
	}

	@Override
	protected void onResumeFromLogin(boolean isLogin) {
		super.onResumeFromLogin(isLogin);
		if (isLogin) {
			userRefreshLayout.setVisibility(View.GONE); // 手动刷新布局
			findViewById(R.id.layout_button).setVisibility(View.VISIBLE); // 汇率按钮
			findViewById(R.id.refresh_layout).setVisibility(View.VISIBLE); // 底部提示布局
		} else {
			stopPollingFlag(); // 停止轮询
			userRefreshLayout.setVisibility(View.VISIBLE); // 手动刷新布局
			findViewById(R.id.layout_button).setVisibility(View.GONE);// 汇率按钮
			findViewById(R.id.refresh_layout).setVisibility(View.GONE);// 底部提示布局
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void finish() {
		super.finish();
		if (HttpManager.mPollingRequestThread != null && PollingRequestThread.pollingFlag) {
			LogGloble.e(TAG, "onPause() mPollingRequestThread  stopPolling()  ");
			HttpManager.stopPolling();
		}
	}

	/** 7秒刷新-全部汇率 */
	private void requestAllRatesPoling() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Forex.FOREX_ALLRATE);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(null);
		HttpManager.requestPollingBii(biiRequestBody, allHttpHandler, ConstantGloble.FOREX_REFRESH_TIMES);// 7秒刷新
	}

	private Handler allHttpHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			// http状态码
			String resultHttpCode = (String) ((Map<String, Object>) msg.obj).get(ConstantGloble.HTTP_RESULT_CODE);
			// 返回数据
			Object resultObj = ((Map<String, Object>) msg.obj).get(ConstantGloble.HTTP_RESULT_DATA);
			// 回调对象
			HttpObserver callbackObject = (HttpObserver) ((Map<String, Object>) msg.obj).get(ConstantGloble.HTTP_CALLBACK_OBJECT);
			// 回调方法
			String callBackMethod = (String) ((Map<String, Object>) msg.obj).get(ConstantGloble.HTTP_CALLBACK_METHOD);
			switch (msg.what) {
			// 正常http请求数据返回
			case ConstantGloble.HTTP_STAGE_CONTENT:

				/**
				 * 执行全局前拦截器
				 */
				if (BaseDroidApp.getInstanse().httpRequestCallBackPre(resultObj)) {
					break;
				}

				/**
				 * 执行callbackObject回调前拦截器
				 */
				if (httpRequestCallBackPre(resultObj)) {
					break;
				}
				// 清空更新时间
				clearTimes();
				clearDate();
				BiiResponse biiResponse = (BiiResponse) ((Map<String, Object>) msg.obj).get(ConstantGloble.HTTP_RESULT_DATA);
				List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
				BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
				if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
					return;
				}
				switch (coinId) {
				case 1:// 全部外汇
					Map<String, List<Map<String, Object>>> result = (Map<String, List<Map<String, Object>>>) biiResponseBody.getResult();
					if (StringUtil.isNullOrEmpty(result) || !result.containsKey(Forex.FOREX_RATE_ALLRATELIST_RES) || StringUtil.isNullOrEmpty(result.get(Forex.FOREX_RATE_ALLRATELIST_RES))) {
						isHasAllList = false;
						return;
					} else {
						allRateList = result.get(Forex.FOREX_RATE_ALLRATELIST_RES);

						if (StringUtil.isNullOrEmpty(allRateList) || allRateList == null || allRateList.size() <= 0) {
							isHasAllList = false;
							return;
						} else {
							// 将得到的数据进行处理
							allRateList = getTrueDate(allRateList);
							if (StringUtil.isNullOrEmpty(allRateList) || allRateList == null || allRateList.size() <= 0) {
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
							// 保存省联号
							// String ibkNum = (String)
							// allRateList.get(0).get(Forex.FOREX_ibkNum);
							// LocalDataService.getInstance().saveIbkNum(ConstantGloble.Forex,ibkNum);
						}
					}
					break;
				case 2:// 我的外汇汇率
						// 得到result
					Map<String, List<Map<String, Object>>> customerResult = (Map<String, List<Map<String, Object>>>) biiResponseBody.getResult();
					if (StringUtil.isNullOrEmpty(customerResult) || !customerResult.containsKey(Forex.FOREX_RATE_CUSTOMERRATELIST_RES) || StringUtil.isNullOrEmpty(customerResult.get(Forex.FOREX_RATE_CUSTOMERRATELIST_RES))) {
						isHasCustomerList = false;
						return;
					} else {
						// 得到customerRateList
						customerRateList = customerResult.get(Forex.FOREX_RATE_CUSTOMERRATELIST_RES);
						if (StringUtil.isNullOrEmpty(customerRateList) || customerRateList == null || customerRateList.size() <= 0) {
							isHasCustomerList = false;
							return;
						} else {
							// 将得到的数据进行处理
							customerRateList = getTrueDate(customerRateList);
							if (StringUtil.isNullOrEmpty(customerRateList) || customerRateList == null || customerRateList.size() <= 0) {
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
							// 保存省联号
							String ibkNum = (String) customerRateList.get(0).get(Forex.FOREX_ibkNum);
							LocalDataService.getInstance().saveIbkNum(ConstantGloble.Forex, ibkNum);
						}
					}
					break;
				default:
					break;
				}
				/**
				 * 执行callbackObject回调后拦截器
				 */
				if (httpRequestCallBackAfter(resultObj)) {
					break;
				}

				/**
				 * 执行全局后拦截器
				 */
				if (BaseDroidApp.getInstanse().httpRequestCallBackAfter(resultObj)) {
					break;
				}

				break;

			// 请求失败错误情况处理
			case ConstantGloble.HTTP_STAGE_CODE:
				/**
				 * 执行code error 全局前拦截器
				 */
				if (BaseDroidApp.getInstanse().httpCodeErrorCallBackPre(resultHttpCode)) {
					break;
				}

				/**
				 * 执行callbackObject error code 回调前拦截器
				 */
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

				/**
				 * 执行code error 全局后拦截器
				 */
				if (BaseDroidApp.getInstanse().httpCodeErrorCallBackAfter(resultHttpCode)) {
					break;
				}

				/**
				 * 执行callbackObject code error 后拦截器
				 */
				if (callbackObject.httpCodeErrorCallBackAfter(resultHttpCode)) {
					break;
				}

				break;

			default:
				break;
			}

		}
	};

	/** 7秒刷新 我的外汇汇率 */
	private void requestCustomerRatesPoling() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Forex.FOREX_CUSTOMER_RATE);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(null);
		HttpManager.requestPollingBii(biiRequestBody, allHttpHandler, ConstantGloble.FOREX_REFRESH_TIMES);// 7秒刷新
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		// taskTag = 4; //功能外置去掉
		if (StringUtil.isNullOrEmpty(commConversationId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			// 判断用户是否开通投资理财服务
			requestPsnInvestmentManageIsOpen();
		}
	}

	/**
	 * 查询全部外汇行情---回调 rateTimes赋值
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnAllRateCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		Map<String, List<Map<String, Object>>> result = (Map<String, List<Map<String, Object>>>) biiResponseBody.getResult();
		// 按钮选中效果
		selectedAllButton();
		// 7秒刷新
		refreshAllData();
		// 得到allRateList
		if (StringUtil.isNullOrEmpty(result)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		} else {
			if (!result.containsKey(Forex.FOREX_RATE_ALLRATELIST_RES)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
				return;
			}
			if (StringUtil.isNullOrEmpty(result.get(Forex.FOREX_RATE_ALLRATELIST_RES))) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
				return;
			}
			allRateList = result.get(Forex.FOREX_RATE_ALLRATELIST_RES);
			if (allRateList == null || allRateList.size() <= 0) {
				return;
			} else {
				selectedAllRates();

			}
		}

	}

	/** 全部外汇汇率---按钮效果 */
	private void selectedAllButton() {
		// 全部外汇汇率按钮默认被选中
		allRateButton.setBackgroundResource(R.drawable.btn_top_waihui_red);
		allRateButton.setTextColor(getResources().getColor(R.color.white));
		CustomerRateButton.setBackgroundResource(R.drawable.btn_top_waihui_write);
		CustomerRateButton.setTextColor(getResources().getColor(R.color.gray));
		allRateButton.setFocusable(true);
		allRateButton.setPressed(true);
		allRateButton.setSelected(true);
		CustomerRateButton.setPressed(false);
		CustomerRateButton.setFocusable(false);
		CustomerRateButton.setSelected(false);
		makeRate.setVisibility(View.GONE);
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
		adapter = new ForexRateInfoAdapter(ForexRateInfoOutlayActivity.this, allRateList);
		rateListView.setAdapter(adapter);
		adapter.setBuyImgOnItemClickListener(buyImgAllOnItemClickListener);
		adapter.setSellImgOnItemClickListener(sellImgAllOnItemClickListener);
	}

	/** 刷新时间 */
	private void refreshTimes(List<Map<String, Object>> list) {
		Map<String, Object> map = list.get(0);
		String times = (String) map.get(Forex.FOREX_RATE_UPDATEDATE_RES);
		rateTimes.setText(times);
	}

	/** 处理货币对，返回的货币对可能没有对应的名称，将其除去 */
	private List<Map<String, Object>> getTrueDate(List<Map<String, Object>> list) {
		int len = list.size();
		List<Map<String, Object>> dateList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < len; i++) {
			Map<String, Object> map = (Map<String, Object>) list.get(i);
			// 得到源货币的代码
			String sourceCurrencyCode = (String) map.get(Forex.FOREX_RATE_SOURCECODE_RES);
			String targetCurrencyCode = (String) map.get(Forex.FOREX_RATE_TARGETCODE_RES);
			if (!StringUtil.isNull(sourceCurrencyCode) && !StringUtil.isNull(targetCurrencyCode) && LocalData.Currency.containsKey(sourceCurrencyCode) && LocalData.Currency.containsKey(targetCurrencyCode)) {
				dateList.add(map);
			}

		}
		return dateList;
	}

	/** 全部汇率-7秒刷新 */
	private void refreshAllData() {
		if (!PollingRequestThread.pollingFlag) {
			// if (allRateList == null || allRateList.size() <= 0) {
			// return;
			// } else {
			requestAllRatesPoling();
			// }
		}
	}

	/**
	 * 我的外汇汇率按钮响应事件---回调13 未定制外汇汇率提示页面
	 * 
	 * @resultObj:返回结果
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnCustomerRateCallback(Object resultObj) {
		super.requestPsnCustomerRateCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		Map<String, List<Map<String, Object>>> result = (Map<String, List<Map<String, Object>>>) biiResponseBody.getResult();
		// 按钮选中效果
		getCustomerButton();
		// 7秒刷新
		refreshCustomerData();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		} else {
			if (!result.containsKey(Forex.FOREX_RATE_CUSTOMERRATELIST_RES) || StringUtil.isNullOrEmpty(result.get(Forex.FOREX_RATE_CUSTOMERRATELIST_RES))) {
				// 用户没有定制货币对-------不在使用
				// makeRateView.setVisibility(View.VISIBLE);
				// 新布局里面的汇率定制按钮事件-------不在使用
				// noMakeButton.setOnClickListener(listener);
				// 不做任何处理
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
				return;
			} else {
				customerRateList = result.get(Forex.FOREX_RATE_CUSTOMERRATELIST_RES);
				if (customerRateList == null || customerRateList.size() <= 0) {
					return;
				}
				getCustomerDate();

			}
		}

	}

	/**
	 * 判断是否开通投资理财服务---回调 快速交易
	 * 
	 * @param resultObj
	 */
	public void requestPsnInvestmentManageIsOpenCallback(Object resultObj) {
		super.requestPsnInvestmentManageIsOpenCallback(resultObj);
		// 设定账户的请求
		requestPsnForexActIsset();
	}

	/**
	 * 交易条件
	 * 
	 * @任务提示框----判断是否设置账户
	 * @param resultObj
	 *            :返回结果
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnForexActIssetCallback(Object resultObj) {
		Map<String, Object> result = getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			isSettingAcc = false;
		} else {
			if (StringUtil.isNullOrEmpty(result.get(Forex.FOREX_RATE_INVESTBINDINGINFO_RES))) {
				isSettingAcc = false;
			} else {
				investBindingInfo = (Map<String, String>) result.get(Forex.FOREX_RATE_INVESTBINDINGINFO_RES);
				BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_INVESTBINDINGINFO_RES, investBindingInfo);

				if (StringUtil.isNullOrEmpty(investBindingInfo)) {
					isSettingAcc = false;
				} else {
					isSettingAcc = true;
				}
			}
		}
		if (!isOpen || !isSettingAcc) {
			BaseHttpEngine.dissMissProgressDialog();
			getPopup();
			return;
		}
		if (isOpen && isSettingAcc) {
			switch (taskTag) {
			case 1:// 快速交易
				ratePsnForexActIsset();
				break;
			case 5:// 我的外汇汇率
				requestPsnCustomerRate();
				break;
			case 4:// 外汇行情初始化时使用
					// 查询我的外汇汇率信息
				requestInitPsnCustomerRate();
				break;
			case 6:// 全部汇率
				requestPsnAllRates();
				break;
			case 8:// 交易购买(功能位置添加)
				getCustomerButton(); // TODO
				requestPsnCustomerRate();
				ratePsnForexActIsset();
				break;
			default:
				break;
			}
			// 查询我的外汇汇率信息
			// requestInitPsnCustomerRate();

		}

	}

	/**
	 * 第一次进入外汇行情页面 查询用户定制的外汇汇率信息-回调
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestInitPsnCustomerRateCallback(Object resultObj) {
		super.requestInitPsnCustomerRateCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		Map<String, List<Map<String, Object>>> result = (Map<String, List<Map<String, Object>>>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result) || !result.containsKey(Forex.FOREX_RATE_CUSTOMERRATELIST_RES) || StringUtil.isNullOrEmpty(result.get(Forex.FOREX_RATE_CUSTOMERRATELIST_RES))) {
			// 用户没有定制货币对
			// 查询所有的货币对
			requestPsnAllRates();
		} else {
			customerRateList = result.get(Forex.FOREX_RATE_CUSTOMERRATELIST_RES);
			if (StringUtil.isNullOrEmpty(customerRateList) || customerRateList == null || customerRateList.size() <= 0) {
				// 用户没有定制货币对
				// 查询所有的货币对
				requestPsnAllRates();
			} else {
				BaseHttpEngine.dissMissProgressDialog();
				getCustomerButton();
				getCustomerDate();
				refreshCustomerData();
			}
		}
	}

	/** 选择我的外汇按钮 */
	private void getCustomerButton() {
		// coinId = 2;
		allRateButton.setBackgroundResource(R.drawable.btn_top_waihui_write);
		CustomerRateButton.setBackgroundResource(R.drawable.btn_top_waihui_red);
		CustomerRateButton.setTextColor(getResources().getColor(R.color.white));
		allRateButton.setTextColor(getResources().getColor(R.color.gray));
		CustomerRateButton.setFocusable(true);
		CustomerRateButton.setPressed(true);
		CustomerRateButton.setSelected(true);
		allRateButton.setFocusable(false);
		allRateButton.setPressed(false);
		allRateButton.setSelected(false);
		makeRate.setVisibility(View.VISIBLE);
		makeRateView.setVisibility(View.GONE);
	}

	/** 我的外汇汇率数据 */
	private void getCustomerDate() {
		customerRateList = getTrueDate(customerRateList);
		if (customerRateList == null || customerRateList.size() <= 0) {
			return;
		}
		isHasCustomerList = true;
		getCustomerAdapterListener(customerRateList);
		// 刷新时间
		refreshTimes(customerRateList);

	}

	/** 我的外汇汇率----监听事件 */
	private void getCustomerAdapterListener(List<Map<String, Object>> customerRateList) {
		adapter = new ForexRateInfoAdapter(ForexRateInfoOutlayActivity.this, customerRateList);
		rateListView.setAdapter(adapter);
		adapter.setBuyImgOnItemClickListener(buyImgCusOnItemClickListener);
		adapter.setSellImgOnItemClickListener(sellImgCusOnItemClickListener);
	}

	/** 用户定制的货币对-7秒刷新 */
	private void refreshCustomerData() {
		if (!PollingRequestThread.pollingFlag) {
			// if (customerRateList == null || customerRateList.size() <= 0) {
			// return;
			// } else {
			requestCustomerRatesPoling();
			// }
		}
	}

	/**
	 * 查询外汇账户类型---回调 活期或者是定期 外汇买卖交易使用 快速交易
	 * 
	 * @param resultObj
	 *            :返回结果
	 */
	@SuppressWarnings("unchecked")
	public void ratePsnForexActIssetCallback(Object resultObj) {
		super.ratePsnForexActIssetCallback(resultObj);
		Map<String, Object> result = getHttpTools().getResponseResult(resultObj);
		String accountType = null;
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexRateInfoOutlayActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
			return;
		} else {
			canTwoSided = (String) result.get(Forex.FOREX_RATE_CANTWOSIDED_RES);
			BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_CANTWOSIDED_RES, canTwoSided);
			// 不含有InvestBindingInfo键
			if (!result.containsKey(Forex.FOREX_RATE_INVESTBINDINGINFO_RES)) {
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(ForexRateInfoOutlayActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
				return;
			}
			Map<String, String> investBindingInfo = (Map<String, String>) result.get(Forex.FOREX_RATE_INVESTBINDINGINFO_RES);
			if (StringUtil.isNullOrEmpty(investBindingInfo)) {
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(ForexRateInfoOutlayActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
				return;
			} else {
				String investAccount = investBindingInfo.get(Forex.FOREX_RATE_INVESTACCOUNT_RES);
				// 存储投资账号
				BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_INVESTACCOUNT_RES, investAccount);
				accountType = investBindingInfo.get(Forex.FOREX_RATE_ACCOUNTTYPE_RES);
				if (StringUtil.isNull(accountType)) {
					BaseHttpEngine.dissMissProgressDialog();
					BaseDroidApp.getInstanse().showInfoMessageDialog(ForexRateInfoOutlayActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
					return;
				}
			}
			if (!accountType.equals(ConstantGloble.FOREX_ACCTYPE_DQYBT)) {
				// 跳转到活期交易页面
				accIsCurr();
			} else {
				if (result.containsKey(Forex.FOREX_VOLUMENUMBERLIST_RES)) {
					if (StringUtil.isNullOrEmpty(result.get(Forex.FOREX_VOLUMENUMBERLIST_RES))) {
						BaseHttpEngine.dissMissProgressDialog();
						BaseDroidApp.getInstanse().showInfoMessageDialog(ForexRateInfoOutlayActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
						return;
					} else {
						volumeNumberList = (List<String>) result.get(Forex.FOREX_VOLUMENUMBERLIST_RES);
						if (volumeNumberList == null || volumeNumberList.size() <= 0) {
							BaseHttpEngine.dissMissProgressDialog();
							BaseDroidApp.getInstanse().showInfoMessageDialog(ForexRateInfoOutlayActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
							return;
						} else {
							BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_VOLUMENUMBERLIST_RES, volumeNumberList);
						}
					}
				}
				accIsFix();
			} // else
		}
	}

	/** 绑定的账户是活期 */
	private void accIsCurr() {
		tradeConditionFixOrCurr = 2;
		fixOrCurrency = 1;
		switch (requestTag) {
		case ConstantGloble.FOREX_QUICK_REQUEST:// 快速交易
			quickOrRateSell = 1;
			break;
		case ConstantGloble.FOREX_BUY_REQUEST:// 银行买价 目标货币对
			quickOrRateSell = 3;
			switch (allOrCustomerReq) {
			case ConstantGloble.FOREX_ALL:// 0----所有的货币对
				// 得到买入币种
				Map<String, Object> map1 = selectedMap;// allRateList.get(buySelectedPosition);
				sourceCurCde = (String) map1.get(Forex.FOREX_RATE_SOURCECODE_RES);
				targetCode = (String) map1.get(Forex.FOREX_RATE_TARGETCODE_RES);
				break;
			case ConstantGloble.FOREX_CUSTOMER:// 1---用户定制
				// 得到买入币种
				Map<String, Object> map2 = selectedMap;// customerRateList.get(buySelectedPosition);
				sourceCurCde = (String) map2.get(Forex.FOREX_RATE_SOURCECODE_RES);
				targetCode = (String) map2.get(Forex.FOREX_RATE_TARGETCODE_RES);
				break;

			default:
				break;
			}
			break;
		case ConstantGloble.FOREX_SELL_REQUEST:// 银行卖价 目标货币对
			quickOrRateSell = 2;
			switch (allOrCustomerReq) {
			case ConstantGloble.FOREX_ALL:// 0----所有的货币对
				// 得到卖出币种
				Map<String, Object> map1 = selectedMap;// allRateList.get(sellSelectedPosition);
				targetCode = (String) map1.get(Forex.FOREX_RATE_TARGETCODE_RES);
				sourceCurCde = (String) map1.get(Forex.FOREX_RATE_SOURCECODE_RES);
				break;
			case ConstantGloble.FOREX_CUSTOMER:// 1---用户定制
				// 得到卖出币种
				Map<String, Object> map2 = selectedMap;// customerRateList.get(sellSelectedPosition);
				targetCode = (String) map2.get(Forex.FOREX_RATE_TARGETCODE_RES);
				sourceCurCde = (String) map2.get(Forex.FOREX_RATE_SOURCECODE_RES);
				break;
			default:
				break;
			}
			break;

		default:
			break;
		}
		// 查询卖出币种是否存在
		tradeConditionPsnForexQueryBlanceCucyList();
	}

	/** 绑定的账户是定期 */
	private void accIsFix() {
		// 定期 外汇行情交易
		tradeConditionFixOrCurr = 1;
		fixOrCurrency = 2;
		BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_VOLUMENUMBERLIST_RES, volumeNumberList);
		switch (requestTag) {
		case ConstantGloble.FOREX_QUICK_REQUEST:// 快速交易
			quickOrRateSell = 1;
			break;
		case ConstantGloble.FOREX_BUY_REQUEST:// 银行买价 目标货币对
			quickOrRateSell = 3;
			switch (allOrCustomerReq) {
			case ConstantGloble.FOREX_ALL:// 0----所有的货币对
				Map<String, Object> map1 = selectedMap;// allRateList.get(buySelectedPosition);
				sourceCurCde = (String) map1.get(Forex.FOREX_RATE_SOURCECODE_RES);
				targetCode = (String) map1.get(Forex.FOREX_RATE_TARGETCODE_RES);
				break;

			case ConstantGloble.FOREX_CUSTOMER:// 1---用户定制
				Map<String, Object> map = selectedMap;// customerRateList.get(buySelectedPosition);
				sourceCurCde = (String) map.get(Forex.FOREX_RATE_SOURCECODE_RES);
				targetCode = (String) map.get(Forex.FOREX_RATE_TARGETCODE_RES);
				break;
			default:
				break;
			}

			break;
		case ConstantGloble.FOREX_SELL_REQUEST:// 银行卖价 目标货币对
			quickOrRateSell = 2;
			switch (allOrCustomerReq) {
			case ConstantGloble.FOREX_ALL:// 0----所有的货币对
				// 得到卖出币种
				Map<String, Object> map1 = selectedMap;// allRateList.get(sellSelectedPosition);
				targetCode = (String) map1.get(Forex.FOREX_RATE_TARGETCODE_RES);
				sourceCurCde = (String) map1.get(Forex.FOREX_RATE_SOURCECODE_RES);
				break;
			case ConstantGloble.FOREX_CUSTOMER:// 1---用户定制
				// 得到卖出币种
				Map<String, Object> map = selectedMap;// customerRateList.get(sellSelectedPosition);
				targetCode = (String) map.get(Forex.FOREX_RATE_TARGETCODE_RES);
				sourceCurCde = (String) map.get(Forex.FOREX_RATE_SOURCECODE_RES);
				break;

			default:
				break;
			}

			break;

		default:
			break;
		}
		// 查询卖出币种是否存在
		tradeConditionPsnForexQueryBlanceCucyList();

	}

	/** 活期账户 银行买价 查询买入币种列表信息 **/
	private void requestPsnForexQueryBuyCucyList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Forex.FOREX_PSNFOREXQUERYBUYCUCYLIST_API);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnForexQueryBuyCucyListCallback");
	}

	/** 活期 查询买入币种列表信息---回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnForexQueryBuyCucyListCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
			// 弹出不可以买的信息
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexRateInfoOutlayActivity.this.getString(R.string.forex_rateinfo_buy_codes));
			return;
		}
		// 得到result
		List<Map<String, String>> buyCodeResultList = (List<Map<String, String>>) biiResponseBody.getResult();
		if (buyCodeResultList == null || buyCodeResultList.size() == 0) {
			// 弹出不可以买的信息
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexRateInfoOutlayActivity.this.getString(R.string.forex_rateinfo_buy_codes));
			return;
		} else {
			// 存储买入币种信息
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_BUYCODERESULTLIST, buyCodeResultList);
			// 处理买入币种数据
			getBuyCode();
		}
	}

	/** 处理买入币种信息 */
	@SuppressWarnings("unchecked")
	private void getBuyCode() {
		List<Map<String, String>> buyCodeResultList = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.FOREX_BUYCODERESULTLIST);
		int len = buyCodeResultList.size();
		buyCodeList = new ArrayList<String>();
		buyCodeDealList = new ArrayList<String>();
		for (int i = 0; i < len; i++) {
			Map<String, String> buyCodeResulMap = buyCodeResultList.get(i);
			String buyCodeResul = buyCodeResulMap.get(Forex.FOREX_BUY_CODE_RES).trim();
			String code = null;
			if (LocalData.Currency.containsKey(buyCodeResul)) {
				code = LocalData.Currency.get(buyCodeResul);
				buyCodeList.add(buyCodeResul);
				buyCodeDealList.add(code);
			} else {
				continue;
			}
		}
		if (buyCodeDealList == null || buyCodeDealList.size() <= 0 || buyCodeList == null || buyCodeList.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_BUYCODEDEALLIST_KEY, buyCodeDealList);
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_BUYCODELIST_KEY, buyCodeList);
			switchTag();
		}
	}

	private void switchTag() {
		stopPollingFlag();
		if (!isFinishing()) {
			switch (quickOrRateSell) {
			case 1:// 快速交易
				switch (fixOrCurrency) {
				case 1:// 活期
					Intent intent3 = new Intent();
					intent3.setClass(ForexRateInfoOutlayActivity.this, ForexQuickCurrentSubmitActivity.class);
					intent3.putExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, ConstantGloble.FOREX_TRADE_QUICK);
					startActivityForResult(intent3, ConstantGloble.FOREX_RATE_TRADE_TAG);
					break;
				case 2:// 定期
					Intent intent2 = new Intent();
					intent2.setClass(ForexRateInfoOutlayActivity.this, ForexQuickTradeSubmitActivity.class);
					intent2.putExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, ConstantGloble.FOREX_TRADE_QUICK);// -1
					startActivityForResult(intent2, ConstantGloble.FOREX_RATE_TRADE_TAG);
					break;
				default:
					break;
				}
				break;
			case 2:// 银行卖价
				buy_isInBuyResultPosition(sourceCurCde);
				switch (fixOrCurrency) {
				case 1:// 活期
					Intent intent3 = new Intent();
					intent3.setClass(ForexRateInfoOutlayActivity.this, ForexQuickCurrentSubmitActivity.class);
					intent3.putExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, ConstantGloble.FOREX_TRADE_QUICK_SELL_CURR);
					startActivityForResult(intent3, ConstantGloble.FOREX_RATE_TRADE_TAG);
					break;
				case 2:// 定期
					Intent intent = new Intent();
					intent.setClass(ForexRateInfoOutlayActivity.this, ForexQuickTradeSubmitActivity.class);
					intent.putExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, ConstantGloble.FOREX_TRADE_QUICK_SELL_FIX);// 601
					startActivityForResult(intent, ConstantGloble.FOREX_RATE_TRADE_TAG);
					break;
				}

				break;
			case 3:// 银行买价
					// 买入币种是否存在买入币种集合里面
				buy_isInBuyResultPosition(targetCode);
				switch (fixOrCurrency) {
				case 1:// 活期
					Intent intent31 = new Intent();
					intent31.setClass(ForexRateInfoOutlayActivity.this, ForexQuickCurrentSubmitActivity.class);
					intent31.putExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, ConstantGloble.FOREX_TRADE_QUICK_RATEINFO_CURR);
					startActivity(intent31);
					break;
				case 2:// 定期
					Intent intent = new Intent();
					intent.setClass(ForexRateInfoOutlayActivity.this, ForexQuickTradeSubmitActivity.class);
					intent.putExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, ConstantGloble.FOREX_TRADE_QUICK_RATEINFO);// 301
					startActivity(intent);
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}
		}

	}

	/** 银行买价-买入币种是否在买入币种列表里面以及位置 */
	private void buy_isInBuyResultPosition(String buyCodeCode) {
		int len = buyCodeList.size();
		boolean tt = false;
		for (int i = 0; i < len; i++) {
			String buyCodeResul = buyCodeList.get(i);
			if (buyCodeCode.equals(buyCodeResul)) {
				tt = true;
				BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_BUYINLIST_POSITION, i);
				break;
			}
			if (!tt) {
				BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_BUYINLIST_POSITION, -1);
			}

		}
	}

	/**
	 * 汇率定制按钮统一事件
	 */
	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			makeRateListener();
		}
	};

	private void makeRateListener() {
		// 跳转到汇率定制页面
		stopPollingFlag();
		Intent intent = new Intent(ForexRateInfoOutlayActivity.this, ForexRateNotifyActivity.class);
		startActivityForResult(intent, ConstantGloble.FOREX_RATE_MAKE_TAG);
	}

	/** 银行买价图片事件 用户定制的货币对 */
	private OnItemClickListener buyImgCusOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// 判断是否登录

			requestTag = 1;
			allOrCustomerReq = 1;
			customerOrTrade = 1;
			// buySelectedPosition = position;
			selectedMap = customerRateList.get(position);
			// 开始交易
			BaseHttpEngine.showProgressDialog();
			ratePsnForexActIsset();
		}
	};
	/** 银行买价图片事件 全部货币对 */
	private OnItemClickListener buyImgAllOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// 点击买入按钮
			// 判断是否登录
			requestTag = 1;
			allOrCustomerReq = 0;
			customerOrTrade = 1;
			// buySelectedPosition = position;
			selectedMap = allRateList.get(position);
			// 开始交易
			// BaseHttpEngine.showProgressDialog();
			// ratePsnForexActIsset();
			exeRatePsnForexActIsset();// 功能外置修改
		}

	};
	/** 银行卖价图片事件 用户定制的货币对 */
	private OnItemClickListener sellImgCusOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// 点击卖出按钮
			// 判断是否登录
			requestTag = 2;
			allOrCustomerReq = 1;
			customerOrTrade = 1;
			// sellSelectedPosition = position;
			selectedMap = customerRateList.get(position);
			// 开始交易
			// BaseHttpEngine.showProgressDialog();
			// ratePsnForexActIsset();
			exeRatePsnForexActIsset();// 功能外置修改
		}
	};
	/** 银行卖价图片事件 全部货币对 */
	private OnItemClickListener sellImgAllOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// 判断是否登录

			// 点击卖出按钮
			requestTag = 2;
			allOrCustomerReq = 0;
			customerOrTrade = 1;
			// sellSelectedPosition = position;
			selectedMap = allRateList.get(position);
			// 开始交易
			// BaseHttpEngine.showProgressDialog();
			// ratePsnForexActIsset();
			exeRatePsnForexActIsset();// 功能外置修改
		}
	};

	private void exeRatePsnForexActIsset() {
		// 执行登陆
		BaseActivity.getLoginUtils(ForexRateInfoOutlayActivity.this).exe(new LoginCallback() {
			@Override
			public void loginStatua(boolean isLogin) {
				if (isLogin) {
					if (isOpen && isSettingAcc) {
						// 进行交易请求
						BaseHttpEngine.showProgressDialog();
						ratePsnForexActIsset();
					} else {
						// 判断是否开通
						taskTag = 8;
						requestCommConversationId();
					}
				}
			}
		});
	}

	/** 停止轮询 */
	private void stopPollingFlag() {
		if (HttpManager.mPollingRequestThread != null && PollingRequestThread.pollingFlag) {
			LogGloble.e(TAG, "onPause() mPollingRequestThread  stopPolling()  ");
			HttpManager.stopPolling();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rate_allButton:// 全部外汇汇率

			stopPollingFlag();
			coinId = 1;
			taskTag = 6;
			allOrCustomerReq = 0;
			customerOrTrade = 3;
			menuOrTrade = 1;
			clearDate();
			clearTimes();
			if (isOpen && isSettingAcc) {
				BaseHttpEngine.showProgressDialog();
				requestPsnAllRates();
			} else {
				BaseHttpEngine.showProgressDialog();
				requestPsnInvestmentManageIsOpen();
			}
			break;
		case R.id.rate_customerButton:// 我的外汇汇率
			stopPollingFlag();
			coinId = 2;
			taskTag = 5;
			customerOrTrade = 2;
			allOrCustomerReq = 1;
			menuOrTrade = 1;
			clearDate();
			clearTimes();
			if (isOpen && isSettingAcc) {
				makeRate.setVisibility(View.VISIBLE);
				BaseHttpEngine.showProgressDialog();
				requestPsnCustomerRate();
			} else {
				BaseHttpEngine.showProgressDialog();
				requestPsnInvestmentManageIsOpen();
			}
			break;
		case R.id.ib_back:// 返回按钮
			stopPollingFlag();
			if (getIntent().getBooleanExtra(ConstantGloble.FOREX_FOREXRATE_TO_ACC, false)) {
				// 账户管理跳转到外汇
				finish();
			} else if (!BaseDroidApp.getInstanse().isLogin()){
				finish();
			} else {
				// ActivityTaskManager.getInstance().closeAllActivityExceptOne("MainActivity");
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent(ForexRateInfoOutlayActivity.this, SecondMainActivity.class);
//				startActivity(intent);
				goToMainActivity();
			}

			break;
		case R.id.ib_top_right_btn:// 快速交易
			taskTag = 1;
			requestTag = 0;
			customerOrTrade = 1;
			quickOrRateSell = 1;
			menuOrTrade = 1;
			// 强制将任务提示框关闭，不允许操作
			if (isOpen && isSettingAcc) {
				BaseHttpEngine.showProgressDialog();
				ratePsnForexActIsset();
			} else {
				BaseHttpEngine.showProgressDialog();
				requestPsnInvestmentManageIsOpen();
			}
			break;
		case R.id.rate_make:// 汇率定制
			makeRateListener();
			break;
		case R.id.manual_refresh:// 手动刷新
			requestAllRatesOutlay();
			break;
		default:
			break;
		}
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
	}

	/** 每次查询前清空更新时间数据 */
	private void clearTimes() {
		rateTimes.setText("");
	}

	/** 活期------卖出币种 */
	@SuppressWarnings("unchecked")
	@Override
	public void tradeConditionCurrencyBlanceCucyListCallback(Object resultObj) {
		Map<String, Object> result = getHttpTools().getResponseResult(resultObj);
		tradeSellCurrResultList = new ArrayList<Map<String, Object>>();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexRateInfoOutlayActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
			return;
		}
		// 不包含sellList键
		if (!result.containsKey(Forex.FOREX_SELLLIST_RES)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexRateInfoOutlayActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
			return;
		}
		// sellList值为空
		if (StringUtil.isNullOrEmpty(result.get(Forex.FOREX_SELLLIST_RES))) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexRateInfoOutlayActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
			return;
		}
		List<Map<String, Object>> sellList = (List<Map<String, Object>>) result.get(Forex.FOREX_SELLLIST_RES);
		if (sellList == null || sellList.size() == 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexRateInfoOutlayActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
			return;
		} else {
			// 存储卖出币种的sellList
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_SELLCODE_SELLLIST_KEY, sellList);
			int len = sellList.size();
			for (int i = 0; i < len; i++) {
				// 账户状态
				String status = (String) sellList.get(i).get(Forex.FOREX_STATUS_RES);
				// 子账户状态正常
				String normal = ConstantGloble.FOREX_ACCTYPE_NORMAL;
				if (normal.equals(status)) {
					Map<String, Object> balance = (Map<String, Object>) sellList.get(i).get(Forex.FOREX_BALANCE_RES);
					if (!StringUtil.isNullOrEmpty(balance)) {
						String availableBalance = (String) balance.get(Forex.FOREX_AVAILABLEBALANCE_RES);
						double moneyBolance = Double.valueOf(availableBalance);
						Map<String, String> currency = (Map<String, String>) balance.get(Forex.FOREX_CURRENCY_RES);
						// 币种存在
						if (!StringUtil.isNullOrEmpty(currency)) {
							String code = currency.get(Forex.FOREX_CURRENCY_CODE_RES).trim();
							// 币种不允许为人民币
							if (!StringUtil.isNull(code) && (!code.equals(ConstantGloble.FOREX_RMB_TAG1) || !code.equalsIgnoreCase(ConstantGloble.FOREX_RMB_CNA_TAG2))) {
								// 账户余额大于0
								if (moneyBolance > 0) {
									tradeSellCurrResultList.add(sellList.get(i));
								}
							}

						}

					}

				} // if
			} // for
			if (tradeSellCurrResultList.size() <= 0 || tradeSellCurrResultList == null) {
				tradeConditionSellTag = false;
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.forex_rateinfo_sell_codes));

				return;
			} else {
				BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_CURR_TRADESELLCODERESULTLIST, tradeSellCurrResultList);
				tradeConditionSellTag = true;
			}

		}
		// super.tradeConditionCurrencyBlanceCucyListCallback(resultObj);
		if (!tradeConditionSellTag) {
			BaseHttpEngine.dissMissProgressDialog();
			// 活期卖出不币种存在
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexRateInfoOutlayActivity.this.getString(R.string.forex_rateinfo_sell_codes));
			return;
		} else {
			switch (quickOrRateSell) {
			case 1:// 快速交易
					// 活期卖出币种存在
					// 查询买入币种信息
				requestPsnForexQueryBuyCucyList();
				break;
			case 2:// 银行卖价，卖出币种必须存在，否则不再进入到外汇交易页面中
					// 判断卖出币种是否存在
				isInResult(targetCode);
				break;
			case 3:// 银行买价，卖出币种必须存在，否则不再进入到外汇交易页面中
					// 判断卖出币种是否存在
				isInResult(sourceCurCde);
				break;
			default:
				break;
			}
		} // else
	}

	/** 判断卖出币种是否在卖出result里面 */
	@SuppressWarnings("unchecked")
	private void isInResult(String sellCodeCode) {

		List<Map<String, Object>> sellList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.FOREX_SELLCODE_SELLLIST_KEY);
		int lens = sellList.size();
		boolean tt = false;
		boolean k = false;
		boolean b = false;
		for (int i = 0; i < lens; i++) {
			Map<String, Object> map = sellList.get(i);
			if (StringUtil.isNullOrEmpty(map)) {
				continue;
			}
			String cashRemit = (String) map.get(Forex.FOREX_CASHREMIT_RES);
			// String cash = null;
			// if (!StringUtil.isNull(cashRemit) &&
			// LocalData.CurrencyCashremit.containsKey(cashRemit)) {
			// cash = LocalData.CurrencyCashremit.get(cashRemit);
			// }
			// 账户状态
			String status = (String) map.get(Forex.FOREX_STATUS_RES);
			if (StringUtil.isNullOrEmpty(status)) {
				continue;
			}
			Map<String, Object> balance = (Map<String, Object>) map.get(Forex.FOREX_BALANCE_RES);
			if (StringUtil.isNullOrEmpty(balance)) {
				continue;
			}
			String availableBalance = (String) balance.get(Forex.FOREX_AVAILABLEBALANCE_RES);
			Map<String, String> currency = (Map<String, String>) balance.get(Forex.FOREX_CURRENCY_RES);
			if (StringUtil.isNullOrEmpty(currency)) {
				continue;
			}
			String code = currency.get(Forex.FOREX_CURRENCY_CODE_RES).trim();
			String codeName = null;
			String codeCodeName = null;
			if (!StringUtil.isNull(code) && LocalData.Currency.containsKey(code) && LocalData.Currency.containsKey(sellCodeCode)) {
				codeName = LocalData.Currency.get(code);
				codeCodeName = LocalData.Currency.get(sellCodeCode);
			}
			// 币种不为空，code和sourceCurCde相同
			if (codeName.equals(codeCodeName)) {
				// 币种存在
				tt = true;
				if (StringUtil.isNull(availableBalance) || (Double.valueOf(availableBalance) <= 0)) {
					// 账户余额
					String info1 = getResources().getString(R.string.forex_curr_acc_info1);
					String info2 = getResources().getString(R.string.forex_curr_acc_info2);

					BaseHttpEngine.dissMissProgressDialog();
					BaseDroidApp.getInstanse().showInfoMessageDialog(info1 + codeName + info2);
					b = false;
					break;
				} else {
					b = true;
					// 状态
					// 子账户状态正常
					String normal = ConstantGloble.FOREX_ACCTYPE_NORMAL;
					if (normal.equals(status)) {
						k = true;
					} else {
						String info = getResources().getString(R.string.forex_curr_acc_status);
						BaseHttpEngine.dissMissProgressDialog();
						BaseDroidApp.getInstanse().showInfoMessageDialog(codeName + info);
						k = false;
						break;
					}

				}
			}

		}

		if (!tt) {
			String codeName = LocalData.Currency.get(sellCodeCode);
			String info1 = getResources().getString(R.string.forex_curr_acc_code_info1);
			String info2 = getResources().getString(R.string.forex_curr_acc_code_info2);
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(info1 + codeName + info2);
			return;
		} else {
			if (k && b) {
				switch (quickOrRateSell) {
				case 2:// 银行卖价
					int position = getCurrSellCodeInListPosition(targetCode);
					if (position < 0) {
						String info1 = getResources().getString(R.string.forex_curr_acc_code_info1);
						String info2 = getResources().getString(R.string.forex_curr_acc_code_info2);
						BaseHttpEngine.dissMissProgressDialog();
						String code = LocalData.Currency.get(targetCode);
						BaseDroidApp.getInstanse().showInfoMessageDialog(info1 + code + info2);
						return;
					} else {
						// 查询买入币种信息
						requestPsnForexQueryBuyCucyList();
					}
					break;
				case 3:// 银行买价
					int po = getCurrSellCodeInListPosition(sourceCurCde);
					if (po < 0) {
						String info1 = getResources().getString(R.string.forex_curr_acc_code_info1);
						String info2 = getResources().getString(R.string.forex_curr_acc_code_info2);
						BaseHttpEngine.dissMissProgressDialog();
						String code = LocalData.Currency.get(sourceCurCde);
						BaseDroidApp.getInstanse().showInfoMessageDialog(info1 + code + info2);
					} else {
						// 查询买入币种信息
						requestPsnForexQueryBuyCucyList();
					}
					break;
				default:
					break;
				}

			}
		}

	}

	/** 得到卖出币种在tradeSellCurrResultList中的position */
	@SuppressWarnings("unchecked")
	private int getCurrSellCodeInListPosition(String sellCodeCode) {
		int position = -1;
		int len = tradeSellCurrResultList.size();
		for (int i = 0; i < len; i++) {
			Map<String, Object> map = tradeSellCurrResultList.get(i);
			if (StringUtil.isNullOrEmpty(map)) {
				continue;
			}
			Map<String, String> currency = (Map<String, String>) map.get(Forex.FOREX_CURRENCY_RES);
			if (StringUtil.isNullOrEmpty(currency)) {
				continue;
			}
			String code = currency.get(Forex.FOREX_CURRENCY_CODE_RES).trim();
			if (StringUtil.isNull(code)) {
				continue;
			}
			String cashRemit = (String) map.get(Forex.FOREX_CASHREMIT_RES);
			String cash = null;
			if (!StringUtil.isNull(cashRemit) && LocalData.CurrencyCashremit.containsKey(cashRemit)) {
				cash = LocalData.CurrencyCashremit.get(cashRemit);
			}
			// 外汇行情页面，所有币种的钞汇标志都是现汇
			if (code.equals(sellCodeCode)) {
				// 卖出币种在用户账户中
				position = i;
				BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_SELLINLIST_POSITION, i);
				break;
			}
		}
		return position;
	}

	/** 定期-----卖出币种 */
	@SuppressWarnings("unchecked")
	@Override
	public void tradeConditionFixBlanceCucyListCallback(Object resultObj) {
		Map<String, Object> result = getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexRateInfoOutlayActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
			return;
		}
		// termSubAccount
		if (!result.containsKey(Forex.FORX_TERMSUBACCOUNT_RES)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexRateInfoOutlayActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
			return;
		}
		if (StringUtil.isNullOrEmpty(result.get(Forex.FORX_TERMSUBACCOUNT_RES))) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexRateInfoOutlayActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
			return;
		}
		List<Map<String, Object>> termSubAccountList = (List<Map<String, Object>>) result.get(Forex.FORX_TERMSUBACCOUNT_RES);
		if (termSubAccountList == null || termSubAccountList.size() == 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexRateInfoOutlayActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
			return;
		} else {
			BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FORX_TERMSUBACCOUNT_RES, termSubAccountList);
			int len2 = termSubAccountList.size();
			for (int j = 0; j < len2; j++) {
				Map<String, Object> termSubAccountMap = termSubAccountList.get(j);
				String status = (String) termSubAccountMap.get(Forex.FOREX_STATUS_RES);
				// 存单类型
				String type = (String) termSubAccountMap.get(Forex.FOREX_TYPE_RES);
				// TODO----
				// 账户状态正常,存单类型为定活两遍，整存整取
				if (ConstantGloble.FOREX_ACCTYPE_NORMAL.equals(status) && (ConstantGloble.FOREX_ACCTYPE_ZCZQ.equals(type) || ConstantGloble.FOREX_ACCTYPE_DHLB.equals(type))) {
					// 得到可用余额，并判断是否大于0
					Map<String, Object> balanceMap = (Map<String, Object>) termSubAccountMap.get(Forex.FOREX_BALANCE_RES);
					if (!StringUtil.isNullOrEmpty(balanceMap)) {
						String balance = (String) balanceMap.get(Forex.FOREX_AVAILABLEBALANCE_RES);
						double b = Double.valueOf(balance);
						// 可用余额大于0
						if (b > 0) {
							tradeConditionFixSellTag = true;
							break;
						}
					} // if
				} // if
			} // for
		}

		// super.tradeConditionFixBlanceCucyListCallback(resultObj);
		if (!tradeConditionFixSellTag) {
			// 不存在卖出币种
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexRateInfoOutlayActivity.this.getString(R.string.forex_rateinfo_sell_codes));
			return;
		} else {

			switch (quickOrRateSell) {
			case 1:// 快速交易
					// 跳转到定期交易页面
					// 查询买入币种信息
				requestPsnForexQueryBuyCucyList();
				break;
			case 2:// 点击银行卖价，查询卖出币种信息
					// 卖出币种是否在卖出币种集合里面
				isInFixResult(targetCode);
				break;
			case 3:// 点击银行买价，查询卖出币种信息
					// 卖出币种是否在卖出币种集合里面
				isInFixResult(sourceCurCde);
				break;
			default:
				break;
			}// switch
		} // else
	}

	/** 卖出币种是否在卖出币种集合里面 */
	@SuppressWarnings("unchecked")
	private void isInFixResult(String sellCodeCode) {
		List<Map<String, Object>> termSubAccountList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap().get(Forex.FORX_TERMSUBACCOUNT_RES);
		int len2 = termSubAccountList.size();
		List<Map<String, Object>> littleResult = new ArrayList<Map<String, Object>>();
		for (int j = 0; j < len2; j++) {
			Map<String, Object> termSubAccountMap = termSubAccountList.get(j);
			if (StringUtil.isNullOrEmpty(termSubAccountMap)) {
				continue;
			}
			String type = (String) termSubAccountMap.get(Forex.FOREX_TYPE_RES);
			String cashRemit = (String) termSubAccountMap.get(Forex.FOREX_CASHREMIT_RES);
			if (StringUtil.isNull(cashRemit)) {
				continue;
			}
			String cash = LocalData.CurrencyCashremit.get(cashRemit);
			Map<String, Object> balanceMap = (Map<String, Object>) termSubAccountMap.get(Forex.FOREX_BALANCE_RES);
			if (StringUtil.isNullOrEmpty(balanceMap)) {
				continue;
			}
			Map<String, String> currency = (Map<String, String>) balanceMap.get(Forex.FOREX_CURRENCY_RES);
			if (StringUtil.isNullOrEmpty(currency)) {
				continue;
			}
			String code = currency.get(Forex.FOREX_CURRENCY_CODE_RES);
			if (StringUtil.isNull(code)) {
				continue;
			}
			String codeName = null;
			String codeCodeName = null;
			if (LocalData.Currency.containsKey(code) && LocalData.Currency.containsKey(sellCodeCode)) {
				codeName = LocalData.Currency.get(code);
				codeCodeName = LocalData.Currency.get(sellCodeCode);
			}
			// 整存整取-----不判断钞汇
			// TODO-----------------------不判断钞汇
			if (!StringUtil.isNull(type) && ConstantGloble.FOREX_ACCTYPE_ZCZQ.equals(type)) {
				if (codeName.equals(codeCodeName)) {
					littleResult.add(termSubAccountMap);
				}
			}
		} // for
		if (littleResult == null || littleResult.size() <= 0) {
			// 卖出币种不存在
			String codeName = LocalData.Currency.get(sellCodeCode);
			String info1 = getResources().getString(R.string.forex_curr_acc_code_info1);
			String info2 = getResources().getString(R.string.forex_curr_acc_code_info2);
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(info1 + codeName + info2);
			return;
		}
		// 卖出币种存在
		getReturnValue(littleResult);
	}

	/** 卖出币种存在时，判断可用余额、账户状态 */
	@SuppressWarnings("unchecked")
	private void getReturnValue(List<Map<String, Object>> list) {
		int len = list.size();
		boolean t = false;
		boolean k = false;
		Map<String, String> currency = null;
		String codeName = null;
		for (int i = 0; i < len; i++) {
			Map<String, Object> termSubAccountMap = list.get(i);
			if (StringUtil.isNullOrEmpty(termSubAccountMap)) {
				continue;
			}
			String status = (String) termSubAccountMap.get(Forex.FOREX_STATUS_RES);
			Map<String, Object> balanceMap = (Map<String, Object>) termSubAccountMap.get(Forex.FOREX_BALANCE_RES);
			if (StringUtil.isNullOrEmpty(balanceMap)) {
				continue;
			}
			currency = (Map<String, String>) balanceMap.get(Forex.FOREX_CURRENCY_RES);
			if (StringUtil.isNullOrEmpty(currency)) {
				continue;
			}
			String code = currency.get(Forex.FOREX_CURRENCY_CODE_RES);
			codeName = LocalData.Currency.get(code);
			String availableBalance = (String) balanceMap.get(Forex.FOREX_AVAILABLEBALANCE_RES);

			if (StringUtil.isNull(availableBalance) || (Double.valueOf(availableBalance) <= 0)) {
				t = false;
				continue;

			} else {
				t = true;
				if (ConstantGloble.FOREX_ACCTYPE_NORMAL.equals(status)) {
					String volumeNumber = (String) termSubAccountMap.get(Forex.FOREX_VOLUMENUMBER_RES);
					String cdnumber = (String) termSubAccountMap.get(Forex.FOREX_CDNUMBER_RES);
					k = true;
					BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_VOLUMENUMBER_RES, volumeNumber);
					BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_CDNUMBER_RES, cdnumber);

					break;
				} else {
					k = false;
				}
			}
		} // for
		if (!t) {
			// 账户余额
			String info1 = getResources().getString(R.string.forex_curr_acc_info1);
			String info2 = getResources().getString(R.string.forex_curr_acc_info2);
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(info1 + codeName + info2);
			return;
		}
		if (!k) {
			// 账户状态
			String info = getResources().getString(R.string.forex_curr_acc_status);
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(codeName + info);
			return;
		}
		if (t && k) {
			// 查询买入币种信息
			requestPsnForexQueryBuyCucyList();
		}
	}

	// -----------------------------功能外置
	/** 全部未登录时汇率 */
	private void requestAllRatesOutlay() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Forex.FOREX_PsnGetAllExchangeRatesOutlay);
		Map<String, String> params = new HashMap<String, String>();
		// 客户登记的交易账户的省行联行号
		params.put(Forex.FOREX_ibknum, LocalDataService.getInstance().getIbkNum(ConstantGloble.Forex));
		// 牌价类型(外汇f： 黄金：G)
		params.put(Forex.FOREX_paritiesType, "F");
		params.put(Forex.FOREX_offerType, "R");
		biiRequestBody.setParams(params);
		HttpManager.requestOutlayBii(biiRequestBody, this, "requestAllRatesOutlayCallback");
	}

	/** 全部未登录时汇率 回调 */
	@SuppressWarnings("unchecked")
	public void requestAllRatesOutlayCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> result = (List<Map<String, Object>>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result) || StringUtil.isNullOrEmpty(result)) {
			isHasAllList = false;
			refreshAllAdapterListenerOutlay(new ArrayList<Map<String, Object>>()); // 清空
		} else {
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
			adapter = new ForexRateInfoAdapter(ForexRateInfoOutlayActivity.this, allRateList);
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
		String times = (String) map.get(Forex.FOREX_RATE_UPDATEDATE_RES);
		updateTimeView.setText(times);
	}
}
