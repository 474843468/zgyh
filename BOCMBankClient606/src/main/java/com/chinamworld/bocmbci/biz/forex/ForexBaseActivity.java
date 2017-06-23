package com.chinamworld.bocmbci.biz.forex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.biz.forex.customer.ForexCustomerRateInfoActivity;
import com.chinamworld.bocmbci.biz.forex.quash.ForexQuashQueryActivity;
import com.chinamworld.bocmbci.biz.forex.rate.ForexAccSettingActivity;
import com.chinamworld.bocmbci.biz.forex.rate.ForexRateInfoOutlayActivity;
import com.chinamworld.bocmbci.biz.forex.strike.ForexStrikeQueryActivity;
import com.chinamworld.bocmbci.biz.invest.activity.InvesAgreeActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.http.thread.PollingRequestThread;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * 外汇基类
 */
public class ForexBaseActivity extends BaseActivity {
	public static final String TAG = "ForexBaseActivity";

	/**
	 * isOpen:是否开通投资理财服务
	 */
	public static boolean isOpen = false;
	/**
	 * 判断用户是否设置外汇交易账户
	 * 
	 * @investBindingInfo:交易账户信息
	 */
	public Map<String, String> investBindingInfo = null;
	/** 是否设置外汇账户 */
	public static boolean isSettingAcc = false;

	public ImageView finishPopButton = null;
	/** 设置账户区域 */
	public View accButtonView = null;
	/** 理财服务区域 */
	public View moneyButtonView = null;
	public View accTextView = null;
	public View moneyTextView = null;
	/**
	 * taskPopCloseButton:任务提示框右上角关闭按钮
	 */
	public ImageView taskPopCloseButton = null;

	/**
	 * resultList：存储所有账户信息
	 */
	public List<Map<String, String>> resultList = null;
	/**
	 * 快速交易、我的外汇、成交状况操作条件判断标志,1-快速交易,2-我的外汇，3-成交查询,4-外汇行情,5-我的外汇汇率,6-全部汇率,7-委托查询
	 * ，8交易购买(功能位置添加)
	 * 
	 */
	public int taskTag = 1;
	/** 存折册号 */
	public List<String> volumeNumberList = null;
	/** 用于区分是二级菜单还是交易请求开通投资理财，1-交易，2-二级菜单 */
	public int menuOrTrade = 1;
	/** 判断买入币种是否存在 */
	public List<Map<String, String>> tradeBuyCodeResultList = null;
	/** 1-定期，2-活期 调用不同的卖出币种回调方法 **/
	public int tradeConditionFixOrCurr = 1;
	/** 活期账户---卖出币种是否存在 false-不存在 */
	public boolean tradeConditionSellTag = false;
	/** 定期账户---卖出币种是否存在 false-不存在 */
	public boolean tradeConditionFixSellTag = false;
	/** 用于区别是我的外汇汇率还是交易发出的请求1-交易，2-我的外汇汇率 3-全部汇率 */
	public int customerOrTrade = 1;
	/** 登陆后的ConversationId */
	public String commConversationId = null;
	/** 活期----卖出币种结果集合 */
	public List<Map<String, Object>> tradeSellCurrResultList = null;
	/** 账户类型 */
	public String accountType = null;
	/** 保留0位小数 */
	public int twoNumber = 0;
	/** 保留2位小数 */
	public int fourNumber = 2;
	/** 用于区分是定期还是活期，1-活期，2-定期 */
	public int currencyOrFixTag = 1;

	/**
	 * @用于区别是外汇行情还是我的外汇、成交状况查询
	 * @外汇行情不用查询账户类型
	 * @1-外汇行情，2-是其余的两个二级菜单
	 */
	public int isRateInfo = 1;
	/** 用于区分是任务提示框的重设账户还是我的外汇重设账户，false-任务提示框，true-我的外汇 */
	public boolean isCustomer = false;
	/** 特殊币种 日元、港元 */
	public List<String> spetialCodeList = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;

		{
			add("027");
			add("JPY");
			add("013");
			add("HKD");
		}
	};
	/** 没有小数点的币种 */
	public List<String> codeNoNumberName = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;

		{
			add("日元");
			add("韩元");
			add("越南盾");
		}
	};
	/** 外汇交易，无论买卖币种，含有日元，限价汇率保留2位小数 */
	public List<String> tradeCheckCodeNoNumber = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;

		{
			add("027");
			add("JPY");
		}
	};
	/** 交易金额 */
	// public String moneyEmg = ConstantGloble.FOREX_AMOUNT;
	/** 限价汇率 */
	public String rateEmg = ConstantGloble.FOREX_RATE;
	/** 主视图布局 */
	public LinearLayout tabcontent;// 主Activity显示
	/** 外汇账户是否是否可以作外汇二选一 */
	public String canTwoSided = null;
	/** 1-当前有效，2-历史有效 */
	public int currentOrHistory = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.biz_activity_layout);//P606理财换肤前旧样式
		setContentView(R.layout.biz_activity_606_layout);//P606理财换肤新样式
		// 初始化弹窗按钮
		initPulldownBtn();
		// 初始化左边菜单
//		initLeftSideList(this, LocalData.forexStorageCashLeftList);
		Button leftButton = (Button) findViewById(R.id.btn_show);//606换肤隐藏侧边栏
		leftButton.setVisibility(View.GONE);


		// 初始化底部菜单
		initFootMenu();
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);// 左侧的菜单栏
	}

	/**
	 * 判断是否开通投资理财服务
	 */
	public void requestPsnInvestmentManageIsOpen() {
		if (menuOrTrade == 2) {
			getHttpTools().requestHttp(Comm.PSNINVESTMENTMANAGEISOPEN_API, "requestMenuIsOpenCallback", null, true);
		} else {
			// 快速交易
			getHttpTools().requestHttp(Comm.PSNINVESTMENTMANAGEISOPEN_API, "requestPsnInvestmentManageIsOpenCallback", null, true);
		}
	}

	/**
	 * 判断是否开通投资理财服务---回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnInvestmentManageIsOpenCallback(Object resultObj) {
		String isOpenOr = getHttpTools().getResponseResult(resultObj);
		// isOpen
		if ("false".equals(isOpenOr)) {
			isOpen = false;
		} else {
			isOpen = true;
		}
		// TODO-------------------------------------------------------------------------
		// isOpen = false;
	}

	/***
	 * 判断用户是否已设置外汇账户---是定期还是活期
	 */
	public void requestPsnForexActIsset() {
		if (menuOrTrade == 2) {
			// 二级菜单
			getHttpTools().requestHttp(Forex.FOREX_RATE_ACTISSET, "requestMenuActIssetCallback", null, true);
		} else {
			// 快速交易
			getHttpTools().requestHttp(Forex.FOREX_RATE_ACTISSET, "requestPsnForexActIssetCallback", null, true);
		}
	}

	/**
	 * 外汇交易账户信息03---回调 交易使用
	 * 
	 * @param resultObj
	 *            :返回结果
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnForexActIssetCallback(Object resultObj) {
		Map<String, Object> result = getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			isSettingAcc = false;
			return;
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
		// TODO-----------------------------------------------------------------------------------
		// isSettingAcc = false;
		if (!isOpen || !isSettingAcc) {
			BaseHttpEngine.dissMissProgressDialog();
			getPopup();
			return;
		}
	}

	/**
	 * 任务提示框
	 */
	public void getPopup() {
		// popupView:任务提示框的布局
		View popupView = LayoutInflater.from(this).inflate(R.layout.forex_task_notify, null);
		taskPopCloseButton = (ImageView) popupView.findViewById(R.id.top_right_close);
		// accButtonView:设置账户按钮
		accButtonView = popupView.findViewById(R.id.forex_acc_button_show);
		// moneyButtonView:理财服务按钮
		moneyButtonView = popupView.findViewById(R.id.forex_money_button_show);
		// accTextView:设置账户文本框
		accTextView = popupView.findViewById(R.id.forex_acc_text_hide);
		// moneyTextView:理财服务文本框
		moneyTextView = popupView.findViewById(R.id.forex_money_text_hide);
		if (isOpen) {
			// 开通投资理财服务
			moneyButtonView.setVisibility(View.GONE);
			moneyTextView.setVisibility(View.VISIBLE);
			if (isSettingAcc) {
				// 用户定义外汇账户
				accButtonView.setVisibility(View.GONE);
				accTextView.setVisibility(View.VISIBLE);

			} else {
				// 用户没有定义外汇账户,必须显示设定账户按钮
				accButtonView.setVisibility(View.VISIBLE);
				accTextView.setVisibility(View.GONE);
				accButtonView.setClickable(true);
				// 外汇账户设定
			}
		} else {
			// 没有开通投资理财服务
			moneyButtonView.setVisibility(View.VISIBLE);
			moneyTextView.setVisibility(View.GONE);
			if (isSettingAcc) {
				// 用户定义外汇账户
				accButtonView.setVisibility(View.GONE);
				accTextView.setVisibility(View.VISIBLE);
			} else {
				// 没有设置账户
				accButtonView.setVisibility(View.VISIBLE);
				accTextView.setVisibility(View.GONE);
			}
		}
		// 设置账户
		accButtonView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isOpen) {
					// 查询所有的账户----ConstantGloble.FOREX_RATE_QUICK_ACC
					BaseHttpEngine.showProgressDialog();
					isCustomer = false;
					requestPsnForexActAvai();

				} else {
					// 没有开通投资理财，不允许设定账户
					CustomDialog.toastInCenter(BaseDroidApp.getInstanse().getCurrentAct(), BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.bocinvt_task_toast_1));
				}
			}
		});

		// 开通投资理财服务
		moneyButtonView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isOpen) {
					// 跳转到投资理财服务协议页面
					Intent gotoIntent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(), InvesAgreeActivity.class);
					startActivityForResult(gotoIntent, ConstantGloble.ACTIVITY_RESULT_CODE);
				}
			}
		});
		// 右上角的关闭按钮
		taskPopCloseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 强制关闭任务提示框，返回到九宫格
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().dismissMessageDialogFore();
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				Intent intent = new Intent(ForexBaseActivity.this, SecondMainActivity.class);
//				startActivity(intent);
				goToMainActivity();
				finish();
			}
		});
		BaseDroidApp.getInstanse().showForexMessageDialog(popupView);
	}

	/**
	 * 第一次进入外汇行情页面 查询用户定制的外汇汇率信息
	 */
	public void requestInitPsnCustomerRate() {
		getHttpTools().requestHttp(Forex.FOREX_CUSTOMER_RATE, "requestInitPsnCustomerRateCallback", null, true);
	}

	public void requestInitPsnCustomerRateCallback(Object resultObj) {
	}

	/**
	 * 外汇交易账户列表-01----设置外汇账户 查询所有的账户
	 */
	public void requestPsnForexActAvai() {
		getHttpTools().requestHttp(Forex.FOREX_PSNFOREXACTAVAI_API, "requestPsnForexActAvaiCallback", null, true);
	}

	/**
	 * 外汇交易账户列表----回调---设置外汇账户
	 * 
	 * @param resultObj
	 *            :返回结果
	 */
	public void requestPsnForexActAvaiCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		resultList = getHttpTools().getResponseResult(resultObj);
		// 得到result
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.forex_set_no_acc));
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_ACTAVI_RESULT_KEY, resultList);
		// 跳转到账户设置页面
		Intent intent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(), ForexAccSettingActivity.class);
		startActivityForResult(intent, ConstantGloble.FOREX_RATE_QUICK_ACC);
	}

	/**
	 * 账户分活期和定期 查询外汇账户类型--进行外汇买卖 外汇交易使用 我的外汇---活期页面
	 */
	public void ratePsnForexActIsset() {
		getHttpTools().requestHttp(Forex.FOREX_RATE_ACTISSET, "ratePsnForexActIssetCallback", null, true);
	}

	/**
	 * @账户分活期和定期 查询外汇账户类型--进行外汇买卖
	 * @外汇交易使用
	 */
	public void ratePsnForexActIssetCallback(Object resultObj) {
	}

	/**
	 * 根据不同的账户跳转到不同的页面 活期我的外汇汇率页面用于判断是否跳转到定期页面
	 */
	public void tradetPsnForexActIsset() {
		getHttpTools().requestHttp(Forex.FOREX_RATE_ACTISSET, "tradetPsnForexActIssetCallback", null, true);
	}

	/** 根据不同的账户跳转到不同的页面----回调 */
	@SuppressWarnings({ "unchecked" })
	public void tradetPsnForexActIssetCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = getHttpTools().getResponseResult(resultObj);
		String accountType = null;
		if (StringUtil.isNullOrEmpty(result)) {
			return;
		} else {
			if (StringUtil.isNullOrEmpty(result.get(Forex.FOREX_RATE_INVESTBINDINGINFO_RES))) {
				return;
			} else {
				investBindingInfo = (Map<String, String>) result.get(Forex.FOREX_RATE_INVESTBINDINGINFO_RES);
				// 存储investBindingInfo
				BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_INVESTBINDINGINFO_RES, investBindingInfo);
				accountType = investBindingInfo.get(Forex.FOREX_RATE_ACCOUNTTYPE_RES);
				if (StringUtil.isNull(accountType)) {
					return;
				}
				BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_ACCOUNTTYPE_RES, accountType);
			}
			if (result.containsKey(Forex.FOREX_VOLUMENUMBERLIST_RES)) {
				volumeNumberList = (List<String>) result.get(Forex.FOREX_VOLUMENUMBERLIST_RES);
				// 存储volumeNumberList
				BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_VOLUMENUMBERLIST_RES, volumeNumberList);
			}
			if (StringUtil.isNullOrEmpty(investBindingInfo)) {
				return;
			} else {
				String accountId = investBindingInfo.get(Forex.FOREX_RATE_ACCOUNTID_RES);
				// 存储accountId
				BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_ACCOUNTID_RES, accountId);
				String investAccount = investBindingInfo.get(Forex.FOREX_RATE_INVESTACCOUNT_RES);
				// 存储投资账号investAccount
				BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_INVESTACCOUNT_RES, investAccount);
				String account = investBindingInfo.get(Forex.Forex_RATE_ACCOUNT_RES);
				// 存储账号
				BaseDroidApp.getInstanse().getBizDataMap().put(Forex.Forex_RATE_ACCOUNT_RES, account);
			}
		}
	}

	// 选择二级菜单 左侧
	@Override
	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
		super.selectedMenuItemHandler(context, menuItem);
		menuOrTrade = 2;
		String menuId = menuItem.MenuID;
		if(menuId.equals("forexStorageCash_1")){// 外汇行情页面
			isRateInfo = 1;
			taskTag = 4;

			if (isOpen && isSettingAcc) {
				// 进入到外汇行情页面
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				Intent intent = new Intent();
				intent.setClass(this, ForexRateInfoOutlayActivity.class);
				context.startActivity(intent);
			} else {
				// 是否开通理财服务
				BaseHttpEngine.showProgressDialogCanGoBack();
				requestPsnInvestmentManageIsOpen();
			}
			
		}
		else if(menuId.equals("forexStorageCash_2")){// 我的外汇
			isRateInfo = 2;
			taskTag = 2;
			if (isOpen && isSettingAcc) {
				// 默认进入到 活期--我的外汇
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				Intent intent3 = new Intent();
				intent3.setClass(this, ForexCustomerRateInfoActivity.class);
				context.startActivity(intent3);
			} else {
				// 是否开通理财服务
				BaseHttpEngine.showProgressDialogCanGoBack();
				requestPsnInvestmentManageIsOpen();
			}
			
		}
		else if(menuId.equals("forexStorageCash_3")){// 成交状况查询
			isRateInfo = 2;
			taskTag = 3;
			if (isOpen && isSettingAcc) {
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				Intent intent3 = new Intent();
				intent3.setClass(this, ForexStrikeQueryActivity.class);
				context.startActivity(intent3);

			} else {
				// 是否开通理财服务
				BaseHttpEngine.showProgressDialogCanGoBack();
				requestPsnInvestmentManageIsOpen();

			}
			
		}
		else if(menuId.equals("forexStorageCash_4")){// 委托查询-----新功能401
			isRateInfo = 2;
			taskTag = 7;
			if (isOpen && isSettingAcc) {
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				Intent intent3 = new Intent();
				intent3.setClass(this, ForexQuashQueryActivity.class);
				context.startActivity(intent3);

			} else {
				// 是否开通理财服务
				BaseHttpEngine.showProgressDialogCanGoBack();
				requestPsnInvestmentManageIsOpen();

			}
			
		}
		return true;
		
		
		
		
//		super.setSelectedMenu(clickIndex);
//		menuOrTrade = 2;
//		switch (clickIndex) {
//		case 0:// 外汇行情页面
//			isRateInfo = 1;
//			taskTag = 4;
//
//			if (isOpen && isSettingAcc) {
//				// 进入到外汇行情页面
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				Intent intent = new Intent();
//				intent.setClass(this, ForexRateInfoOutlayActivity.class);
//				startActivity(intent);
//			} else {
//				// 是否开通理财服务
//				BaseHttpEngine.showProgressDialogCanGoBack();
//				requestPsnInvestmentManageIsOpen();
//			}
//			break;
//		case 1:// 我的外汇
//			isRateInfo = 2;
//			taskTag = 2;
//			if (isOpen && isSettingAcc) {
//				// 默认进入到 活期--我的外汇
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				Intent intent3 = new Intent();
//				intent3.setClass(this, ForexCustomerRateInfoActivity.class);
//				startActivity(intent3);
//			} else {
//				// 是否开通理财服务
//				BaseHttpEngine.showProgressDialogCanGoBack();
//				requestPsnInvestmentManageIsOpen();
//			}
//
//			break;
//
//		case 2:// 成交状况查询
//			isRateInfo = 2;
//			taskTag = 3;
//			if (isOpen && isSettingAcc) {
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				Intent intent3 = new Intent();
//				intent3.setClass(this, ForexStrikeQueryActivity.class);
//				startActivity(intent3);
//
//			} else {
//				// 是否开通理财服务
//				BaseHttpEngine.showProgressDialogCanGoBack();
//				requestPsnInvestmentManageIsOpen();
//
//			}
//			break;
//		case 3:// 委托查询-----新功能401
//			isRateInfo = 2;
//			taskTag = 7;
//			if (isOpen && isSettingAcc) {
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				Intent intent3 = new Intent();
//				intent3.setClass(this, ForexQuashQueryActivity.class);
//				startActivity(intent3);
//
//			} else {
//				// 是否开通理财服务
//				BaseHttpEngine.showProgressDialogCanGoBack();
//				requestPsnInvestmentManageIsOpen();
//
//			}
//			break;
//		default:
//
//			break;
//		}

	}

	/** 二级菜单判断是否开通投资理财 */
	public void requestMenuIsOpenCallback(Object resultObj) {
		String isOpenOr = getHttpTools().getResponseResult(resultObj);
		// isOpen
		if ("false".equals(isOpenOr)) {
			isOpen = false;
		} else {
			isOpen = true;
		}
		// TODO-----------------------------------------------
		// isOpen = false;
		// 查询账户
		requestPsnForexActIsset();
	}

	/** 二级菜单---查询是否设置账户----回调 */
	@SuppressWarnings("unchecked")
	public void requestMenuActIssetCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(result)) {
			isSettingAcc = false;
		} else {
			if (StringUtil.isNullOrEmpty(result.get(Forex.FOREX_RATE_INVESTBINDINGINFO_RES))) {
				isSettingAcc = false;
			} else {
				investBindingInfo = (Map<String, String>) result.get(Forex.FOREX_RATE_INVESTBINDINGINFO_RES);
				BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_INVESTBINDINGINFO_RES, investBindingInfo);
				isSettingAcc = true;
				accountType = investBindingInfo.get(Forex.FOREX_RATE_ACCOUNTTYPE_RES);
				if (StringUtil.isNull(accountType)) {
					return;
				}
				if (result.containsKey(Forex.FOREX_VOLUMENUMBERLIST_RES)) {
					volumeNumberList = (List<String>) result.get(Forex.FOREX_VOLUMENUMBERLIST_RES);
					// 存储volumeNumberList
					BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_VOLUMENUMBERLIST_RES, volumeNumberList);
				}

				if (StringUtil.isNullOrEmpty(investBindingInfo)) {
					return;
				} else {
					String accountId = investBindingInfo.get(Forex.FOREX_RATE_ACCOUNTID_RES);
					// 存储accountId
					BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_ACCOUNTID_RES, accountId);
					String investAccount = investBindingInfo.get(Forex.FOREX_RATE_INVESTACCOUNT_RES);
					// 存储投资账号investAccount
					BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_INVESTACCOUNT_RES, investAccount);
					String account = investBindingInfo.get(Forex.Forex_RATE_ACCOUNT_RES);
					// 存储账号
					BaseDroidApp.getInstanse().getBizDataMap().put(Forex.Forex_RATE_ACCOUNT_RES, account);
				}
			}
		}
		// TODO------------------------------------------------
		// isSettingAcc=false;
		if (!isOpen || !isSettingAcc) {
			getPopup();
			return;
		}
		if (isOpen && isSettingAcc) {// 开通理财服务和设置外汇账户
			switch (taskTag) {
			case 4:// 外汇行情
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				Intent intent = new Intent();
				intent.setClass(this, ForexRateInfoOutlayActivity.class);
				curActivity.startActivity(intent);
				break;
			case 3:// 成交状况查询
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				Intent intent3 = new Intent();
				intent3.setClass(this, ForexStrikeQueryActivity.class);
				curActivity.startActivity(intent3);
				break;
			case 2:// 我的外汇
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				Intent intent4 = new Intent();
				intent4.setClass(this, ForexCustomerRateInfoActivity.class);
				curActivity.startActivity(intent4);
				break;
			case 7:
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				Intent intent6 = new Intent();
				intent6.setClass(this, ForexQuashQueryActivity.class);
				curActivity.startActivity(intent6);
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		BaseDroidApp.getInstanse().setCurrentAct(this);
		switch (resultCode) {
		case RESULT_OK:
			switch (requestCode) {
			case ConstantGloble.ACTIVITY_RESULT_CODE:// 投资理财1
				// 开通成功的响应
				moneyButtonView.setVisibility(View.GONE);
				moneyTextView.setVisibility(View.VISIBLE);
				isOpen = true;
				if (isSettingAcc) {
					// 已经设定账户
					switch (taskTag) {
					case 4:// 外汇行情
						BaseDroidApp.getInstanse().dismissMessageDialogFore();
						BaseHttpEngine.showProgressDialog();
						requestInitPsnCustomerRate();
						break;
					case 2:// 我的外汇
						BaseDroidApp.getInstanse().dismissMessageDialogFore();
						BaseHttpEngine.showProgressDialog();
						customerPsnForexActIsset();
						break;
					case 3:// 成交状况查询
						BaseDroidApp.getInstanse().dismissMessageDialogFore();
						BaseHttpEngine.showProgressDialog();
						requestSystemDateTime();
						break;
					// TODO------快速交易
					case 1:// 快速交易
						BaseDroidApp.getInstanse().dismissMessageDialogFore();
						BaseHttpEngine.showProgressDialog();
						ratePsnForexActIsset();
						break;
					case 5:// 我的外汇汇率
						BaseDroidApp.getInstanse().dismissMessageDialogFore();
						BaseHttpEngine.showProgressDialog();
						// 查询用户定制的外汇汇率
						requestPsnCustomerRate();
						break;
					case 6:
						BaseDroidApp.getInstanse().dismissMessageDialogFore();
						BaseHttpEngine.showProgressDialog();
						requestPsnAllRates();
						break;
					case 7:// 委托
						BaseDroidApp.getInstanse().dismissMessageDialogFore();
						BaseHttpEngine.showProgressDialog();
						// 查询当前有效委托
						requestPsnForexAllTransQuery1(null, null, ConstantGloble.FOREX_CURRENTTYPE, ConstantGloble.FOREX_PAGESIZE, ConstantGloble.FOREX_CURRENTINDEX, ConstantGloble.FOREX_REFRESH);
						break;
					}

				} else {
					// 没设定账户
					// getPopup();
					accButtonView.setVisibility(View.VISIBLE);
					accTextView.setVisibility(View.GONE);
				}
				break;
			case ConstantGloble.FOREX_RATE_QUICK_ACC:// 请求账户11
				isSettingAcc = true;
				isOpen = true;
				switch (taskTag) {
				case 4:// 外汇行情
					BaseDroidApp.getInstanse().dismissMessageDialogFore();
					BaseHttpEngine.showProgressDialog();
					requestInitPsnCustomerRate();
					break;
				case 2:// 我的外汇-活期-默认
					BaseDroidApp.getInstanse().dismissMessageDialogFore();
					BaseHttpEngine.showProgressDialog();
					customerPsnForexActIsset();
					break;
				case 3:// 成交状况查询
					BaseDroidApp.getInstanse().dismissMessageDialogFore();
					BaseHttpEngine.showProgressDialog();
					requestSystemDateTime();
					break;
				case 1:// 快速交易
					BaseDroidApp.getInstanse().dismissMessageDialogFore();
					BaseHttpEngine.showProgressDialog();
					ratePsnForexActIsset();
					break;
				case 5:// 我的外汇汇率
					BaseDroidApp.getInstanse().dismissMessageDialogFore();
					// 查询用户定制的外汇汇率
					BaseHttpEngine.showProgressDialog();
					requestPsnCustomerRate();
					break;
				case 7:// 委托
					BaseDroidApp.getInstanse().dismissMessageDialogFore();
					BaseHttpEngine.showProgressDialog();
					// 查询当前有效委托
					requestPsnForexAllTransQuery1(null, null, ConstantGloble.FOREX_CURRENTTYPE, ConstantGloble.FOREX_PAGESIZE, ConstantGloble.FOREX_CURRENTINDEX, ConstantGloble.FOREX_REFRESH);
					break;
				}

				break;

			}
			break;
		case RESULT_CANCELED:
			// 开通失败的响应，不能再调用getPopup()
			break;
		}
	}

	// 外汇买卖必须判断买入币种、卖出是否存在、以及用户绑定的账户是定期还是活期
	/** 外汇买卖条件----查询买入币种是否存在 */
	public void tradeConditionPsnForexQueryBuyCucyList() {
		getHttpTools().requestHttp(Forex.FOREX_PSNFOREXQUERYBUYCUCYLIST_API, "tradeConditionPsnForexQueryBuyCucyListCallback", null, true);
	}

	/** 外汇买卖条件----查询买入币种是否存在---回调 */
	public void tradeConditionPsnForexQueryBuyCucyListCallback(Object resultObj) {
		// BiiResponse biiResponse = (BiiResponse) resultObj;
		// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// // 得到response
		// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// // 得到result
		// if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
		// BaseHttpEngine.dissMissProgressDialog();
		// BaseDroidApp.getInstanse().showInfoMessageDialog(
		// getResources().getString(R.string.forex_rateinfo_buy_codes));
		// return;
		//
		// }
		// tradeBuyCodeResultList = (List<Map<String, String>>)
		// biiResponseBody.getResult();
		// BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_BUYCODERESULTLIST,
		// tradeBuyCodeResultList);
	}

	/** 外汇买卖条件----查询卖出币种信息 */
	public void tradeConditionPsnForexQueryBlanceCucyList() {
		if (tradeConditionFixOrCurr == 1) {
			// 定期
			getHttpTools().requestHttp(Forex.FOREX_PSNFOREXQUERYBLANCECUCYLIST_API, "tradeConditionFixBlanceCucyListCallback", null, true);
		} else if (tradeConditionFixOrCurr == 2) {
			// 活期
			getHttpTools().requestHttp(Forex.FOREX_PSNFOREXQUERYBLANCECUCYLIST_API, "tradeConditionCurrencyBlanceCucyListCallback", null, true);
		}
	}

	/** 外汇买卖条件----查询卖出币种信息-----定期回调 */
	@SuppressWarnings("unchecked")
	public void tradeConditionFixBlanceCucyListCallback(Object resultObj) {
		Map<String, Object> result = getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexBaseActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
			return;
		}
		// termSubAccount
		if (!result.containsKey(Forex.FORX_TERMSUBACCOUNT_RES)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexBaseActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
			return;
		}
		if (StringUtil.isNullOrEmpty(result.get(Forex.FORX_TERMSUBACCOUNT_RES))) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexBaseActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
			return;
		}
		List<Map<String, Object>> termSubAccountList = (List<Map<String, Object>>) result.get(Forex.FORX_TERMSUBACCOUNT_RES);
		if (termSubAccountList == null || termSubAccountList.size() == 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexBaseActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
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
	}

	/** 外汇买卖条件----查询卖出币种信息-----活期回调 */
	@SuppressWarnings("unchecked")
	public void tradeConditionCurrencyBlanceCucyListCallback(Object resultObj) {
		Map<String, Object> result = getHttpTools().getResponseResult(resultObj);
		tradeSellCurrResultList = new ArrayList<Map<String, Object>>();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexBaseActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
			return;
		}
		// 不包含sellList键
		if (!result.containsKey(Forex.FOREX_SELLLIST_RES)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexBaseActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
			return;
		}
		// sellList值为空
		if (StringUtil.isNullOrEmpty(result.get(Forex.FOREX_SELLLIST_RES))) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexBaseActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
			return;
		}
		List<Map<String, Object>> sellList = (List<Map<String, Object>>) result.get(Forex.FOREX_SELLLIST_RES);
		if (sellList == null || sellList.size() == 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(ForexBaseActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));

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
	}

	/**
	 * 我的外汇汇率按钮响应事件. 查询用户定制的外汇汇率信息
	 */
	public void requestPsnCustomerRate() {
		getHttpTools().requestHttp(Forex.FOREX_CUSTOMER_RATE, "requestPsnCustomerRateCallback", null, true);
	}

	/** 查询用户定制的外汇汇率信息---回调 */
	public void requestPsnCustomerRateCallback(Object resultObj) {
	}

	/**
	 * 查询全部外汇行情--12
	 */
	public void requestPsnAllRates() {
		getHttpTools().requestHttp(Forex.FOREX_ALLRATE, "requestPsnAllRateCallback", null, true);
	}

	public void requestPsnAllRateCallback(Object resultObj) {
	}

	/** 定期---外汇买卖回调 */
	public void requestPsnForexTradeCallback(Object resultObj) {
	}

	/**
	 * 活期-----外汇买卖确认---提交
	 * 
	 * @param investAccount
	 *            :投资账号
	 * @param bCurrency
	 *            :买入币别
	 * @param sCurrency
	 *            :卖出币别
	 * @param transFlag
	 *            :买卖标志,0 买入，1 卖出
	 * @param sAmount
	 *            :卖出金额
	 * @param bAmount
	 *            :买入金额
	 * @param exchangeType
	 *            :交易类型,03 获利委托 04 止损委托 05 二选一委托 07 限价即时 08 市价即时
	 * @param exchangeRate
	 *            :市价汇率
	 * @param limitRate
	 *            :限价汇率
	 * @param cashRemit
	 *            :钞汇标识
	 * @param volumeNumber
	 *            :定一本册号
	 * @param cdNumber
	 *            :存单号
	 * @param winRate
	 *            :获利汇率
	 * @param cDType
	 *            :储种
	 * @param loseRate
	 *            :止损汇率
	 * @param token
	 * @param consignDays
	 *            :委托截至日期
	 * @param consignHour
	 *            :委托截至时刻
	 */
	public void requestCurrencyPsnForexTrade(String investAccount, String bCurrency, String sCurrency, String transFlag, String sAmount, String bAmount, String exchangeType, String exchangeRate, String limitRate, String cashRemit, String volumeNumber, String cdNumber, String winRate, String cDType, String loseRate, String token, String consignDays, String consignHour) {
		Map<String, Object> map = new Hashtable<String, Object>();
		map.put(Forex.FOREX_INVESTACCOUNT_REQ, investAccount);
		map.put(Forex.FOREX_BCURRENCY_REQ, bCurrency);
		map.put(Forex.FOREX_SCURRENCYT_REQ, sCurrency);
		map.put(Forex.FOREX_TRANSFLAG_REQ, transFlag);
		map.put(Forex.FOREX_EXCHANGETYPET_REQ, exchangeType);

		map.put(Forex.FOREX_CASHREMIT_REQ, cashRemit);
		map.put(Forex.FOREX_TOKEN_REQ, token);

		int type = Integer.valueOf(exchangeType);
		LogGloble.d(TAG + " type", String.valueOf(type));
		String buyTag = LocalData.forexTradeSellOrBuyList.get(0);
		switch (currencyOrFixTag) {
		case 2:// 定期
			map.put(Forex.FOREX_VOLUMENUMBER_REQ, volumeNumber);
			map.put(Forex.FOREX_CDNUMBER_REQ, cdNumber);
			map.put(Forex.FOREX_CDTYPE_REQ, cDType);
			break;

		default:// 活期
			if (buyTag.equals(transFlag)) {
				map.put(Forex.FOREX_BAMOUNT_REQ, bAmount);
				// 买入
				switch (type) {
				case ConstantGloble.FOREX_TRADE_SEVEN:// 限价
					map.put(Forex.FOREX_EXCHANGERATE, exchangeRate);
					map.put(Forex.FOREX_LIMITRATE_REQ, limitRate);
					LogGloble.d(TAG + " 0", "7");
					break;
				case ConstantGloble.FOREX_TRADE_EIGHT:// 市价
					map.put(Forex.FOREX_EXCHANGERATE, exchangeRate);
					LogGloble.d(TAG + " 0", "8");
					break;
				case ConstantGloble.FOREX_TRADE_THREE:// 获利委托
					map.put(Forex.FOREX_WINRATE_REQ, winRate);
					map.put(Forex.FOREX_CONSIGNAYS_REQ, consignDays);
					map.put(Forex.FOREX_CONSIGNHOUR_REQ, consignHour);
					break;
				case ConstantGloble.FOREX_TRADE_FOUR:// 止损委托
					map.put(Forex.FOREX_LOSERATE_REQ, loseRate);
					map.put(Forex.FOREX_CONSIGNAYS_REQ, consignDays);
					map.put(Forex.FOREX_CONSIGNHOUR_REQ, consignHour);
					break;
				case ConstantGloble.FOREX_TRADE_FIVE:// 二选一委托
					map.put(Forex.FOREX_WINRATE_REQ, winRate);
					map.put(Forex.FOREX_LOSERATE_REQ, loseRate);
					map.put(Forex.FOREX_CONSIGNAYS_REQ, consignDays);
					map.put(Forex.FOREX_CONSIGNHOUR_REQ, consignHour);
					break;
				default:

					break;
				}

			} else {
				map.put(Forex.FOREX_SAMOUNT_REQ, sAmount);
				// 卖出
				switch (type) {

				case ConstantGloble.FOREX_TRADE_SEVEN:// 限价
					map.put(Forex.FOREX_EXCHANGERATE, exchangeRate);
					map.put(Forex.FOREX_LIMITRATE_REQ, limitRate);
					LogGloble.d(TAG + " 1", "7");
					break;
				case ConstantGloble.FOREX_TRADE_EIGHT:// 市价
					map.put(Forex.FOREX_EXCHANGERATE, exchangeRate);
					LogGloble.d(TAG + " 1", "8");
					break;
				case ConstantGloble.FOREX_TRADE_THREE:// 获利委托
					map.put(Forex.FOREX_WINRATE_REQ, winRate);
					map.put(Forex.FOREX_CONSIGNAYS_REQ, consignDays);
					map.put(Forex.FOREX_CONSIGNHOUR_REQ, consignHour);
					break;
				case ConstantGloble.FOREX_TRADE_FOUR:// 止损委托
					map.put(Forex.FOREX_LOSERATE_REQ, loseRate);
					map.put(Forex.FOREX_CONSIGNAYS_REQ, consignDays);
					map.put(Forex.FOREX_CONSIGNHOUR_REQ, consignHour);
					break;
				case ConstantGloble.FOREX_TRADE_FIVE:// 二选一委托
					map.put(Forex.FOREX_WINRATE_REQ, winRate);
					map.put(Forex.FOREX_LOSERATE_REQ, loseRate);
					map.put(Forex.FOREX_CONSIGNAYS_REQ, consignDays);
					map.put(Forex.FOREX_CONSIGNHOUR_REQ, consignHour);
					break;
				case ConstantGloble.FOREX_TRADE_ELEVEN:// 追击止损委托
					map.put(Forex.FOREX_LOSERATE_REQ, loseRate);
					map.put(Forex.FOREX_CONSIGNAYS_REQ, consignDays);
					map.put(Forex.FOREX_CONSIGNHOUR_REQ, consignHour);
					break;
				default:
					break;
				}
			}
			break;
		}
		getHttpTools().requestHttp(Forex.FOREX_PSNFOREXTRADE_API, "requestCurrencyPsnForexTradeCallback", map, true);
	}

	/** 活期-----外汇买卖确认---提交 */
	public void requestCurrencyPsnForexTradeCallback(Object resultObj) {

	}

	/**
	 * 根据投资交易类型，查询交易账户
	 */
	public void requestQueryInvtBindingInfo() {
		BaseHttpEngine.showProgressDialogCanGoBack();
		Map<String, Object> map = new Hashtable<String, Object>();
		map.put(Forex.FOREX_INVTTYPE_REQ, Forex.FOREX_TEN);
		getHttpTools().requestHttp(Forex.FOREX_QUERYINVTBINDINGINFO_API, "requestQueryInvtBindingInfoCallback", map, true);
	}

	/**
	 * 根据投资交易类型，查询交易账户---回调
	 * 
	 * @param resultObj
	 */
	public void requestQueryInvtBindingInfoCallback(Object resultObj) {
	}

	/** 我的外汇--页面初始化时需判断是定期还是活期 */
	public void customerPsnForexActIsset() {
		getHttpTools().requestHttp(Forex.FOREX_RATE_ACTISSET, "customerPsnForexActIssetCallback", null, true);
	}

	/** 我的外汇--页面初始化时需判断是定期还是活期 ---回调 */
	public void customerPsnForexActIssetCallback(Object resultObj) {
	}

	/** 在时间后面添加秒数 */
	public String dealTimes(String date) {
		String[] startStr = date.toString().trim().split(":");
		String hours = null;
		if (startStr.length == 2) {
			StringBuilder sb = new StringBuilder(date);
			sb.append(":");
			sb.append("00");
			hours = sb.toString().trim();
		}
		return hours;
	}

	/** 获取小时数 */
	public String getHour(String date) {
		String hours = null;
		String[] startStr = date.toString().trim().split(":");
		if (startStr.length > 1) {
			hours = startStr[0];
		}
		return hours;
	}

	public boolean httpRequestCallBackPre(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		LogGloble.d(TAG, "异常----------");
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
			if (Forex.FOREX_PSNFOREXALLTRANSQUERY_API.equals(biiResponseBody.getMethod()) || Forex.FOREX_CUSTOMER_RATE.equals(biiResponseBody.getMethod()) || Forex.FOREX_ALLRATE.equals(biiResponseBody.getMethod())) {
				if (biiResponse.isBiiexception()) {// 代表返回数据异常
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null) {
						if (biiError.getCode() != null) {
							if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
								if (HttpManager.mPollingRequestThread != null && PollingRequestThread.pollingFlag) {
									LogGloble.e(TAG, "onPause() mPollingRequestThread  stopPolling()  ");
									HttpManager.stopPolling();
								} // 要重新登录
								showTimeOutDialog(biiError.getMessage());
							} else {
								// 非会话超时错误拦截
								BaseDroidApp.getInstanse().createDialog("", biiError.getMessage(), new OnClickListener() {

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
//				intent.setClass(ForexBaseActivity.this, LoginActivity.class);
//				startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
//			}
//		});
//	}

	/**
	 * // * 成交状况查询 //
	 */
	// public void requestPsnForexAllTransQuery(String startDate, String
	// endDate, String queryType, String pageSize,
	// String currentIndex, String refresh) {
	// BiiRequestBody biiRequestBody = new BiiRequestBody();
	// biiRequestBody.setMethod(Forex.FOREX_PSNFOREXALLTRANSQUERY_API);
	// biiRequestBody.setConversationId(commConversationId);
	// Map<String, String> map = new Hashtable<String, String>();
	// map.put(Forex.FOREX_ENDDATE_REQ, endDate);
	// map.put(Forex.FOREX_STARTDATE_REQ, startDate);
	// map.put(Forex.FOREX_QUERYTYPE_REQ, queryType);
	// map.put(Forex.FOREX_PAGESIZE_REQ, pageSize);
	// map.put(Forex.FOREX_CURRENTINDEX_REQ, currentIndex);
	// map.put(Forex.FOREX_REFRESH_REQ, refresh);
	// biiRequestBody.setParams(map);
	// HttpManager.requestBii(biiRequestBody, this,
	// "requestPsnForexAllTransQueryCallback");
	//
	// }
	/**
	 * 委托-----成交状况查询
	 */
	public void requestPsnForexAllTransQuery1(String startDate, String endDate, String queryType, String pageSize, String currentIndex, String refresh) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Forex.FOREX_ENDDATE_REQ, endDate);
		map.put(Forex.FOREX_STARTDATE_REQ, startDate);
		map.put(Forex.FOREX_QUERYTYPE_REQ, queryType);
		map.put(Forex.FOREX_PAGESIZE_REQ, pageSize);
		map.put(Forex.FOREX_CURRENTINDEX_REQ, currentIndex);
		map.put(Forex.FOREX_REFRESH_REQ, refresh);
		if (currentOrHistory == 1) {
			// 当前
			getHttpTools().requestHttp(Forex.FOREX_PSNFOREXALLTRANSQUERY_API, "requestPsnCurrentTransQueryCallback", map, true);
		} else if (currentOrHistory == 2) {
			// 历史
			getHttpTools().requestHttp(Forex.FOREX_PSNFOREXALLTRANSQUERY_API, "requestPsnHistoryTransQueryCallback", map, true);
		}
	}

	/**
	 * 查询单笔外汇行情
	 * 
	 * @param bCurrency
	 *            :买入货币
	 * @param sCurrency
	 *            :卖出货币
	 */
	public void requestPsnForexQuerySingleRate(String bCurrency, String sCurrency) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Forex.FOREX_BCURRENCY_RES, bCurrency);
		map.put(Forex.FOREX_SCURRENCY_RES, sCurrency);
		getHttpTools().requestHttp(Forex.FOREX_PSNFOREXQUERYSINGLERATE_API, "requestPsnForexQuerySingleRateCallback", map, true);
	}

	/** 查询单笔外汇行情-----回调 */
	public void requestPsnForexQuerySingleRateCallback(Object resultObj) {
	}
	@Override
	protected void onResume() {
		super.onResume();
		boolean login = BaseDroidApp.getInstanse().isLogin();
		onResumeFromLogin(login);
	}
	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.TwoTask;
	}
}
