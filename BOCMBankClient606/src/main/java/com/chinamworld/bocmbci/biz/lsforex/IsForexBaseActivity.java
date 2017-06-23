package com.chinamworld.bocmbci.biz.lsforex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.invest.activity.InvesAgreeActivity;
import com.chinamworld.bocmbci.biz.lsforex.acc.IsForexSettingBindAccActivity;
import com.chinamworld.bocmbci.biz.lsforex.bail.IsForexBailInfoActivity;
import com.chinamworld.bocmbci.biz.lsforex.manageacc.IsForexBailProduceActivity;
import com.chinamworld.bocmbci.biz.lsforex.manageacc.IsForexProduceRadeActivity;
import com.chinamworld.bocmbci.biz.lsforex.myrate.IsForexMyRateInfoActivity;
import com.chinamworld.bocmbci.biz.lsforex.query.IsForexQueryMenuActivity;
import com.chinamworld.bocmbci.biz.lsforex.rate.IsForexTwoWayTreasureActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.ErrorCode;
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

/** 外汇双向宝的基类 */
public class IsForexBaseActivity extends BaseActivity implements OnClickListener{
	public static final String TAG = "IsForexBaseActivity";
	/** isOpen:是否开通投资理财服务 */
	public static boolean isOpen = false;
	/** 是否设置外汇双向宝账户 */
	public static boolean isSettingAcc = false;
	/** 是否签约保证金 */
	public static boolean isSign = false;
	/** 双向宝交易账户----是否是判断条件 */
	public boolean isCondition = false;
	/** 登陆后的ConversationId */
	public String commConversationId = null;
	/** 结算币种代码 list */
	public List<String> vfgRegCurrencyList = null;
	/** 用于区分是那种交易查询 1-成交状况查询，2-斩仓交易查询，3-未平仓交易查询，4-对账单查询,5-当前委托，6-历史委托,10-在快速交易中未平仓交易查询 */
	public int queryTag = 1;
	/** 查询结果 list */
	public List<Map<String, Object>> queryResultList = null;
	/** 保留0位小数 */
	public int twoNumber = 0;
	/** 保留2位小数 */
	public int fourNumber = 2;
	/**保留一位小数*/
	public int oneNumber = 1;
	/** 转账金额 */
	// public String moneyEmg = ConstantGloble.FOREX_AMOUNT1;
	/** 交易金额 */
	public String moneyEmg1 = ConstantGloble.FOREX_AMOUNT;
	/** 限价汇率 */
	public String rateEmg = ConstantGloble.FOREX_RATE;
	/** 用于区别是我的外汇汇率还是交易发出的请求1-交易，2-我的外汇汇率 3-全部汇率 */
	public int customerOrTrade = 1;
	/** 特殊币种 日元、港元保留4位小数 */
	public List<String> spetialCodeList = new ArrayList<String>() {
		{
			add("027");
			add("JPY");
			add("013");
			add("HKD");
		}
	};
	/** 日元 黄金 限价汇率保留2位小数 */
	public List<String> spCodeList = new ArrayList<String>() {
		{
			add("027");
			add("JPY");
			add("G");//黄金
			add("035");// 人民币金
			add("GLD");// 人民币金
			add("034");// 美元金
			add("XAU");// 美元金
			add("844");//人民币钯金
			add("845");//人民币铂金
			add("841");//美元钯金
			add("045");//美元铂金
		}
	};
	/**白银限价汇率保留3位小数*/
	public List<String> spCodeListT = new ArrayList<String>(){
		{	
			add("S"); //白银
			add("068");//白银    人民币银
			add("036");//白银    美元银
		}
		
	};
	/** 含有日元金额没有小数位 */
	public List<String> tradeCheckCodeNoNumber = new ArrayList<String>() {
		{
			add("027");
			add("JPY");
		}
	};

	/** 人民币 */
	public List<String> rmbCodeList = new ArrayList<String>() {
		{
			add("001");
			add("CNY");
		}
	};
	/** 主视图布局 */
	public LinearLayout tabcontent;// 主Activity显示
	/** 全部货币对 list */
	public List<Map<String, Object>> codeResultList = null;
	/** 1-我的双向宝，2-保证金交易，3-交易状况查询 4-双向宝行情,5-签约管理 ,6-快速交易,8交易购买(功能位置添加)*/
	public int taskTag = 1;
	/** 是否点击setSelectedMenu */
	public boolean isSelectedMenu = false;
	public int recordNumber = 0;
	public int internalSeq = 0;
	private View moneyButtonView = null;
	private View moneOpenyButtonView = null;
	private View signNoButtonView = null;
	private View signOpenButtonView = null;
	private View accNoButtonView = null;
	private View accOpenButtonView = null;
	/** 任务提示框顶部TextView */
	private TextView popTopText = null;
	/** 交易状况查询是否完成任务提示框 false-没有，true-是 */
	public boolean isSearchFinish = false;
	/** 1-已开通投资理财查询签约产品，2-先开投资理财，在查询签约产品 */
	public static int searchBaillAfterOpen = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_606_layout);
		// 初始化弹窗按钮
		initPulldownBtn();
		// 初始化左边菜单
//		initLeftSideList(this, LocalData.isForexStorageCashLeftList);
		// 初始化底部菜单
		initFootMenu();
		setLeftButtonPopupGone();
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		queryResultList = new ArrayList<Map<String, Object>>();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ib_back:
				finish();
				break;
			default:
				break;
		}
	}

	/** 选择二级菜单 */
	@Override
	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
		super.selectedMenuItemHandler(context, menuItem);
		isSelectedMenu = true;
		String menuId = menuItem.MenuID;
		if(menuId.equals("isForexStorageCash_1")){
			// 双向宝行情
			taskTag = 4;
			ActivityTaskManager.getInstance().removeAllSecondActivity();
			Intent intent = new Intent();
			intent.setClass(this, IsForexTwoWayTreasureActivity.class);
			context.startActivity(intent);
		}
		else if(menuId.equals("isForexStorageCash_2")){// 保证金交易
			taskTag = 2;
			ActivityTaskManager.getInstance().removeAllSecondActivity();
			Intent intent2 = new Intent();
			intent2.setClass(this, IsForexBailInfoActivity.class);
			context.startActivity(intent2);
		}
		else if(menuId.equals("isForexStorageCash_3")){// 我的双向宝
			taskTag = 1;
			ActivityTaskManager.getInstance().removeAllSecondActivity();
			Intent intent3 = new Intent();
			intent3.setClass(this, IsForexMyRateInfoActivity.class);
			context.startActivity(intent3);
		}
		else if(menuId.equals("isForexStorageCash_4")){// 成交状况查询
			taskTag = 3;
			ActivityTaskManager.getInstance().removeAllSecondActivity();
			Intent intent4 = new Intent();
			intent4.setClass(this, IsForexQueryMenuActivity.class);
			context.startActivity(intent4);
		}
		else if(menuId.equals("isForexStorageCash_5")){// 签约管理
			taskTag = 5;
			ActivityTaskManager.getInstance().removeAllSecondActivity();
			Intent intent1 = new Intent();
			intent1.setClass(this, IsForexBailProduceActivity.class);
			context.startActivity(intent1);
		}
		return true;
		
		
//		super.setSelectedMenu(clickIndex);
//		isSelectedMenu = true;
//		switch (clickIndex) {
//		case 0:// 双向宝行情
//			taskTag = 4;
//			ActivityTaskManager.getInstance().removeAllActivity();
//			Intent intent = new Intent();
//			intent.setClass(this, IsForexTwoWayTreasureActivity.class);
//			startActivity(intent);
//			break;
//		case 1:// 我的双向宝
//			taskTag = 1;
//			ActivityTaskManager.getInstance().removeAllActivity();
//			Intent intent3 = new Intent();
//			intent3.setClass(this, IsForexMyRateInfoActivity.class);
//			startActivity(intent3);
//
//			break;
//		case 2:// 保证金交易
//			taskTag = 2;
//			ActivityTaskManager.getInstance().removeAllActivity();
//			Intent intent2 = new Intent();
//			intent2.setClass(this, IsForexBailInfoActivity.class);
//			startActivity(intent2);
//
//			break;
//		case 3:// 签约管理
//			taskTag = 5;
//			ActivityTaskManager.getInstance().removeAllActivity();
//			Intent intent1 = new Intent();
//			intent1.setClass(this, IsForexBailProduceActivity.class);
//			startActivity(intent1);
//			break;
//		case 4:// 成交状况查询
//			taskTag = 3;
//			ActivityTaskManager.getInstance().removeAllActivity();
//			Intent intent4 = new Intent();
//			intent4.setClass(this, IsForexQueryMenuActivity.class);
//			startActivity(intent4);
//
//			break;
//
//		default:
//			break;
//		}
	}

	/**
	 * 判断是否开通投资理财服务--二级菜单---setSelectedMenu调用
	 */
	public void requestsetSelectedMenuIsOpen() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_API);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "requestsetSelectedMenuIsOpenCallback");

	}

	/** 判断是否开通投资理财服务--二级菜单----回调 ---setSelectedMenu调用 */
	public void requestsetSelectedMenuIsOpenCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		String isOpenOr = (String) biiResponseBody.getResult();
		// isOpen
		if (ConstantGloble.FALSE.equals(isOpenOr)) {
			isOpen = false;
			BaseHttpEngine.dissMissProgressDialog();
			getPop();
			return;
		} else {
			isOpen = true;
			requestsetSelectedMenuBindAccount();
		}
	}

	/** 双向宝交易账户----setSelectedMenu调用 */
	public void requestsetSelectedMenuBindAccount() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGGETBINDACCOUNT_KEY);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "requestsetSelectedMenuBindAccountCallback");

	}

	/** 双向宝交易账户----setSelectedMenu调用-----回调 */
	public void requestsetSelectedMenuBindAccountCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> accReaultMap = (Map<String, String>) biiResponseBody.getResult();
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(accReaultMap)) {
			isSettingAcc = false;
			BaseDroidApp.getInstanse().showMessageDialog(getResources().getString(R.string.isForex_emg_acc),
					noIsAccListener);
			return;
		} else {
			String accountNumber = accReaultMap.get(IsForex.ISFOREX_ACCOUNTNUMBER_REQ);
			if (StringUtil.isNull(accountNumber)) {
				isSettingAcc = false;
				BaseDroidApp.getInstanse().showMessageDialog(getResources().getString(R.string.isForex_emg_acc),
						noIsAccListener);
				return;
			} else {
				isSettingAcc = true;
				switch (taskTag) {
				case 1:// 我的双向宝
					ActivityTaskManager.getInstance().removeAllSecondActivity();
					Intent intent = new Intent();
					intent.setClass(this, IsForexMyRateInfoActivity.class);
					startActivity(intent);
					break;
				case 2:// 保证金交易
					ActivityTaskManager.getInstance().removeAllSecondActivity();
					Intent intent2 = new Intent();
					intent2.setClass(this, IsForexBailInfoActivity.class);
					startActivity(intent2);
					break;
				case 3:// 成交状况查询
					ActivityTaskManager.getInstance().removeAllSecondActivity();
					Intent intent1 = new Intent();
					intent1.setClass(this, IsForexQueryMenuActivity.class);
					startActivity(intent1);
					break;
				case 4:// 双向宝行情页面
					ActivityTaskManager.getInstance().removeAllSecondActivity();
					Intent intent4 = new Intent();
					intent4.setClass(this, IsForexTwoWayTreasureActivity.class);
					startActivity(intent4);
					break;
				default:
					break;
				}
			}
		}

	}

	/**
	 * 判断是否开通投资理财服务--二级菜单
	 */
	public void requestPsnInvestmentManageIsOpen() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_API);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "requestMenuIsOpenCallback");
	}

	/** 判断是否开通投资理财服务--二级菜单----回调 */
	public void requestMenuIsOpenCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		String isOpenOr = (String) biiResponseBody.getResult();
		// isOpen
		if ("false".equals(isOpenOr)) {
			isOpen = false;
		} else {
			isOpen = true;
		}
	}

	/**
	 * 双向宝账户信息
	 * 
	 * 判断是否设置外汇双向宝账户 --------二级菜单
	 */
	public void requestPsnVFGGetBindAccount() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGGETBINDACCOUNT_KEY);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(null);
		if (isCondition) {
			// 双向宝交易账户回调----做为条件
			HttpManager.requestBii(biiRequestBody, this, "requestConditionAccountCallback");
		} else {
			// 双向宝交易账户回调----不做为条件
			HttpManager.requestBii(biiRequestBody, this, "requestMenuAccountCallback");
		}

	}

	/**
	 * 查询双向宝账户信息信息----回调
	 */
	public void requestMenuAccountCallback(Object resultObj) {

	}

	/**
	 * 双向宝账户信息
	 * 
	 * 判断是否设置外汇双向宝账户 --------二级菜单----回调
	 */
	public void requestConditionAccountCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> accReaultMap = (Map<String, String>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(accReaultMap)) {
			isSettingAcc = false;
			isSign = false;
			return;
		} else {
			String accountNumber = accReaultMap.get(IsForex.ISFOREX_ACCOUNTNUMBER_REQ);
			if (StringUtil.isNull(accountNumber)) {
				isSettingAcc = false;
				isSign = false;
			} else {
				isSettingAcc = true;
				isSign = true;
				BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_ACCREAULTMAP, accReaultMap);
			}
		}

	}

	/** 用户没有开通双向宝交易账户或保证金交易账户 */
	public OnClickListener noIsAccListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
//			ActivityTaskManager.getInstance().removeAllSecondActivity();
//			Intent intent = new Intent(IsForexBaseActivity.this, SecondMainActivity.class);
//			startActivity(intent);
			goToMainActivity();
			finish();
		}
	};

	public void getPop() {
		View popupView = LayoutInflater.from(this).inflate(R.layout.isforex_task_notify, null);
		ImageView taskPopCloseButton = (ImageView) popupView.findViewById(R.id.top_right_close);
		// moneyButtonView:理财服务按钮
		moneyButtonView = popupView.findViewById(R.id.forex_money_button_show);
		moneOpenyButtonView = popupView.findViewById(R.id.forex_money_text_hide);
		signNoButtonView = popupView.findViewById(R.id.forex_sign_button_show);
		signOpenButtonView = popupView.findViewById(R.id.forex_sign_text_hide);
		accNoButtonView = popupView.findViewById(R.id.forex_acc_button_show);
		accOpenButtonView = popupView.findViewById(R.id.forex_acc_text_hide);
		popTopText = (TextView) popupView.findViewById(R.id.tv_acc_account_accountState);
		if (searchBaillAfterOpen == 2) {
			// 未开通投资理财服务
			popTopText.setText(getResources().getString(R.string.isForex_task_only_open_title));
			moneyButtonView.setVisibility(View.VISIBLE);
			moneOpenyButtonView.setVisibility(View.GONE);
			signNoButtonView.setVisibility(View.INVISIBLE);
			signOpenButtonView.setVisibility(View.GONE);
			accNoButtonView.setVisibility(View.INVISIBLE);
			accOpenButtonView.setVisibility(View.GONE);
		} else if (searchBaillAfterOpen == 1) {
			// 已开通投资理财服务
			popTopText.setText(getResources().getString(R.string.isForex_task_title));
			if (isOpen) {
				// 不显示开通投资理财
				moneyButtonView.setVisibility(View.GONE);
				moneOpenyButtonView.setVisibility(View.GONE);
				if (isSign) {
					// 签约成功
					signNoButtonView.setVisibility(View.GONE);
					signOpenButtonView.setVisibility(View.VISIBLE);
					// 判断是否设置账户
					if (isSettingAcc) {
						// 设置账户
						accNoButtonView.setVisibility(View.GONE);
						accOpenButtonView.setVisibility(View.VISIBLE);
					} else {
						// 未设置账户
						accNoButtonView.setVisibility(View.VISIBLE);
						accOpenButtonView.setVisibility(View.GONE);
					}
				} else {
					// 未签约成功,必须未设置账户
					signNoButtonView.setVisibility(View.VISIBLE);
					signOpenButtonView.setVisibility(View.GONE);
					accNoButtonView.setVisibility(View.VISIBLE);
					accOpenButtonView.setVisibility(View.GONE);
				}

			}
		}

		// 开通投资理财
		moneyButtonView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isOpen) {
					// 跳转到投资理财服务协议页面
					Intent gotoIntent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(), InvesAgreeActivity.class);
					startActivityForResult(gotoIntent, ConstantGloble.ACTIVITY_REQUEST_MANAGE_CODE);
				}

			}
		});
		// 签约账户
		signNoButtonView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isOpen) {
					if (!isSign) {
						// 跳转到签约协议页面
						Intent gotoIntent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(),
								IsForexProduceRadeActivity.class);
						startActivityForResult(gotoIntent, ConstantGloble.ACTIVITY_REQUEST_SETPRMSACC_CODE);// 签约协议页面
					}
				} else {
					// 没有开通投资理财，签约账户
					CustomDialog.toastInCenter(BaseDroidApp.getInstanse().getCurrentAct(), BaseDroidApp.getInstanse()
							.getCurrentAct().getString(R.string.bocinvt_task_toast_1));
				}
			}
		});
		// 登记账户
		accNoButtonView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isOpen && isSign) {
					if (!isSettingAcc) {
						// 跳转到登记账户页面
						Intent gotoIntent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(),
								IsForexSettingBindAccActivity.class);
						startActivityForResult(gotoIntent, ConstantGloble.ACTIVITY_REQUEST_INVTBINDING_CODE);// 登记账户
					}
				} else {
					// 必须先完成以上两个任务，才可以登记账户
					CustomDialog.toastInCenter(BaseDroidApp.getInstanse().getCurrentAct(), BaseDroidApp.getInstanse()
							.getCurrentAct().getString(R.string.isForex_task_toast_2));
				}
			}
		});
		// 右上角的关闭按钮
		taskPopCloseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 强制关闭任务提示框，返回到九宫格
				BaseDroidApp.getInstanse().dismissMessageDialogFore();
//				ActivityTaskManager.getInstance().removeAllActivity();
				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				Intent intent = new Intent();
//				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), MainActivity.class);
//				BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
				finish();
			}
		});
		BaseDroidApp.getInstanse().showForexMessageDialog(popupView);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {

		case RESULT_OK:// 成功
			switch (requestCode) {
			case ConstantGloble.ACTIVITY_REQUEST_MANAGE_CODE:// 开通投资理财3
				moneyButtonView.setVisibility(View.GONE);
				moneOpenyButtonView.setVisibility(View.GONE);
				isOpen = true;
				if (searchBaillAfterOpen == 1) {
					// 已经开通投资理财
					if (isSign) {
						// 已签约
						signNoButtonView.setVisibility(View.GONE);
						signOpenButtonView.setVisibility(View.VISIBLE);
						// 判断是否设置账户
						if (isSettingAcc) {
							// 设置账户
							accNoButtonView.setVisibility(View.GONE);
							accOpenButtonView.setVisibility(View.VISIBLE);
							BaseDroidApp.getInstanse().dismissMessageDialogFore();
							BaseHttpEngine.showProgressDialog();
							switch (taskTag) {
							case 1:// 我的双向宝
									// 查询双向宝持仓信息
								requestPsnVFGPositionInfo();
								break;
							case 2:// 保证金交易
									// 查询双向宝交易账户信息
								isCondition = false;
								requestPsnVFGGetBindAccount();
								break;
							case 3:// 交易状况查询
								isSearchFinish = true;
								requestCommConversationId();
								break;
							case 4:// 双向宝行情
								requestPsnVFGCustomerSetRate();
							case 5:// 签约
									// 已做完任务提示，查询保证金账户列表
								requestPsnVFGBailListQuery();
								break;
							case 6:// 双向宝行情
								BaseDroidApp.getInstanse().dismissMessageDialogFore();
								BaseHttpEngine.showProgressDialog();
								requestAllRate("");
								break;

							default:
								break;
							}
						} else {
							// 未设置账户
							accNoButtonView.setVisibility(View.VISIBLE);
							accOpenButtonView.setVisibility(View.GONE);
						}
					} else {
						// 未签约,未登记
						signNoButtonView.setVisibility(View.VISIBLE);
						signOpenButtonView.setVisibility(View.GONE);
						accNoButtonView.setVisibility(View.VISIBLE);
						accOpenButtonView.setVisibility(View.GONE);

					}
				} else if (searchBaillAfterOpen == 2) {
					BaseDroidApp.getInstanse().dismissMessageDialogFore();
					searchBaillAfterOpen = 1;
					// 查询签约产品，弹出任务提示框
					BaseHttpEngine.showProgressDialog();
					requestPsnVFGBailListQueryCondition();
				}

				break;
			case ConstantGloble.ACTIVITY_REQUEST_SETPRMSACC_CODE:// 签约协议页面4
				isOpen = true;
				isSign = true;
				searchBaillAfterOpen = 1;
				signNoButtonView.setVisibility(View.GONE);
				signOpenButtonView.setVisibility(View.VISIBLE);
				// 判断是否设置账户
				if (isSettingAcc) {
					// 设置账户
					accNoButtonView.setVisibility(View.GONE);
					accOpenButtonView.setVisibility(View.VISIBLE);
					BaseDroidApp.getInstanse().dismissMessageDialogFore();
					BaseHttpEngine.showProgressDialog();
					switch (taskTag) {
					case 1:// 我的双向宝
							// 查询双向宝持仓信息
						requestPsnVFGPositionInfo();
						break;
					case 2:// 保证金交易
							// 查询双向宝交易账户信息
						isCondition = false;
						requestPsnVFGGetBindAccount();
						break;
					case 3:// 交易状况查询
						isSearchFinish = true;
						requestCommConversationId();
						break;
					case 4:// 双向宝行情
						requestPsnVFGCustomerSetRate();
						break;
					case 5:// 签约
							// 已做完任务提示，查询保证金账户列表
						requestPsnVFGBailListQuery();
						break;

					default:
						break;
					}
				} else {
					// 未设置账户
					accNoButtonView.setVisibility(View.VISIBLE);
					accOpenButtonView.setVisibility(View.GONE);
				}

				break;
			case ConstantGloble.ACTIVITY_REQUEST_INVTBINDING_CODE:// 登记账户5
				searchBaillAfterOpen = 1;
				isOpen = true;
				isSign = true;
				isSettingAcc = true;
				BaseDroidApp.getInstanse().dismissMessageDialogFore();
				BaseHttpEngine.showProgressDialog();
				switch (taskTag) {
				case 1:// 我的双向宝
						// 查询双向宝持仓信息
					requestPsnVFGPositionInfo();
					break;
				case 2:// 保证金交易
						// 查询双向宝交易账户信息
					isCondition = false;
					requestPsnVFGGetBindAccount();
					break;
				case 3:// 交易状况查询
					isSearchFinish = true;
					requestCommConversationId();
					break;
				case 4:// 双向宝行情
					requestPsnVFGCustomerSetRate();
					break;
				case 5:// 签约
						// 已做完任务提示，查询保证金账户列表
					requestPsnVFGBailListQuery();
					break;

				default:
					break;
				}

				break;
			default:
				break;
			}

			break;
		case RESULT_CANCELED:// 失败

			break;
		default:
			break;
		}
	}

	/** 获得结算币种 */
	public void requestPsnVFGGetRegCurrency(String vfgType) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGGETREGCURRENCY_KRY);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> params = new HashMap<String, String>();
		params.put("vfgType", vfgType);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGGetRegCurrencyCallback");
	}

	/** 获得结算币种----回调 */
	public void requestPsnVFGGetRegCurrencyCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (vfgRegCurrencyList != null && vfgRegCurrencyList.size() > 0) {
			vfgRegCurrencyList.clear();
		}
		vfgRegCurrencyList = (List<String>) biiResponseBody.getResult();
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.ISFOREX_VFGREGCURRENCYLISTTLIST_KEY, vfgRegCurrencyList);
	}

	/** 查询全部汇率----货币对 */
	public void requestPsnVFGGetAllRate(String vfgType) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		Map<String, String> params = new HashMap<String, String>();
		params.put("vfgType", vfgType);
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGGETALLRATE_API);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGGetAllRateCallback");
	}

	/** 查询全部汇率----货币对-----回调 */
	public void requestPsnVFGGetAllRateCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		codeResultList = (List<Map<String, Object>>) biiResponseBody.getResult();
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_CODERESULTLIST_KEY, codeResultList);
	}

	/** 查询全部汇率----货币对---快速交易 *///PsnVFGGetAllRat  PsnVFGGetAllRate
	public void requestAllRate(String vfgType) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		Map<String, String> params = new HashMap<String, String>();
		params.put("vfgType", vfgType);
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGGETALLRATE_API);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestAllRateCallback");
	}

	/** 查询全部汇率----货币对----快速交易----回调 */
	public void requestAllRateCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		codeResultList = (List<Map<String, Object>>) biiResponseBody.getResult();
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_CODERESULTLIST_KEY, codeResultList);
	}

	/** 客户定制汇率查询 双向宝*/
	public void requestPsnVFGCustomerSetRate() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGCUSTOMERSETRATE_API);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGCustomerSetRateCallback");
	}

	/** 客户定制汇率查询---回调 */
	public void requestPsnVFGCustomerSetRateCallback(Object resultObj) {

	}

	/** 客户定制汇率查询   双向宝*/
	public void requestCustomerSetRate() {//requestPsnVFGCustomerSetRate
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGCUSTOMERSETRATE_API);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "requestCustomerSetRateCallback");
	}

	/** 客户定制汇率查询---回调 */
	public void requestCustomerSetRateCallback(Object resultObj) {

	}

	/**
	 * 反向持仓标识判断
	 * 
	 * @param currencyCode
	 *            结算币种
	 * @param direction
	 *            买卖方向
	 * @param currencyPairCode
	 *            货币对
	 */
	public void requestPsnVFGPositionFlag(String currencyCode, String direction, String currencyPairCode) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGPOSITIONFLAG_API);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> map = new HashMap<String, String>();
		map.put(IsForex.ISFOREX_CURRENCYCODES1_REQ, currencyCode);
		map.put(IsForex.ISFOREX_DIRECTIONS1_REQ, direction);
		map.put(IsForex.ISFOREX_CURRENCYPAIRCODES1_REQ, currencyPairCode);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGPositionFlagCallback");
	}

	/**
	 * 反向持仓标识判断
	 */
	public void requestPsnVFGPositionFlagCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> result = (Map<String, String>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		String showOpenPositionFlag = result.get(IsForex.ISFOREX_SHOWOPENPOSITIONFLAG_RES);
		if (StringUtil.isNull(showOpenPositionFlag)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(IsForex.ISFOREX_SHOWOPENPOSITIONFLAG_RES, showOpenPositionFlag);
	}
	
	/**
	 * 成交状况查询 ------详情页面
	 * @param vfgTransactionId 交易序号
	 * @param internalSeq 内部序号
	 */
	public void requestPsnVFGTradeDetailQuery(String vfgTransactionId, String internalSeq){
		// TODO Auto-generated method stub
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PsnVFGTradeDetailQuery_API);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(IsForex.ISFOREX_vfgTransactionId_REQ, vfgTransactionId);
		paramsmap.put(IsForex.ISFOREX_internalSeq_REQ, internalSeq);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGTradeDetailQueryCallback");
	}
	
	/** 成交状况查询 ----回调  详情页面*/
	public void requestPsnVFGTradeDetailQueryCallback(Object resultObj) {
//		BaseHttpEngine.dissMissProgressDialog();
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		// 得到response
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
//		if (StringUtil.isNullOrEmpty(result)) {
//			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_null));
//			return;
//		}
//		if (!result.containsKey(IsForex.ISFOREX_LIST_REQ)) {
//			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_null));
//			return;
//		}
//		if (StringUtil.isNullOrEmpty(result.get(IsForex.ISFOREX_LIST_REQ))) {
//			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_null));
//			return;
//		}
//		List<Map<String, Object>> lists = (List<Map<String, Object>>) result.get(IsForex.ISFOREX_LIST_REQ);
//		queryResultList.addAll(lists);
//		String internaNumber = (String) result.get(IsForex.ISFOREX_internalSeq_REQ);
//		internalSeq = Integer.valueOf(internaNumber);
	}
	

	/***
	 * 交易查询
	 * 
	 * @param currencyCode
	 *            :结算币种
	 * @param queryType
	 *            :查询类型
	 * @param startDate
	 *            :起始日期
	 * @param endDate
	 *            :结束日期
	 * @param pageSize
	 *            : 页面大小
	 * @param currentIndex
	 *            :当前页索引
	 * @param _refresh
	 */
	public void requestPsnVFGTradeInfoQuery(String currencyCode, String queryType, String startDate, String endDate,
			String pageSize, String currentIndex, String _refresh) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGTRADEINFOQUERY_KEY);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> map = new HashMap<String, String>();
		map.put(IsForex.ISFOREX_CURRENCYCODE_REQ, currencyCode);
		map.put(IsForex.ISFOREX_QUERYTYPE_REQ, queryType);
		map.put(IsForex.ISFOREX_STARTDATE_REQ, startDate);
		map.put(IsForex.ISFOREX_ENDDATE_REQ, endDate);
		map.put(IsForex.ISFOREX_PAGESIZE_REQ, pageSize);
		map.put(IsForex.ISFOREX_CURRENTINDEX_REQ, currentIndex);
		map.put(IsForex.ISFOREX_REFRESH_REQ, _refresh);
		biiRequestBody.setParams(map);
		if (queryTag == 1 || queryTag == 3 || queryTag == 5 || queryTag == 6||queryTag ==10) {
			HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGTradeInfoQueryCallback");
		} else if (queryTag == 2) {
			// 斩仓交易查询
			HttpManager.requestBii(biiRequestBody, this, "requestPsnVFZcTradeInfoQueryCallback");
		} else if (queryTag == 4) {
			// 对账单查询
			HttpManager.requestBii(biiRequestBody, this, "requestPsnVFAccTradeInfoQueryCallback");
		}

	}

	/** 成交状况查询 未平仓交易查询----回调 */
	public void requestPsnVFGTradeInfoQueryCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_null));
			return;
		}
		if (!result.containsKey(IsForex.ISFOREX_LIST_REQ)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_null));
			return;
		}
		if (StringUtil.isNullOrEmpty(result.get(IsForex.ISFOREX_LIST_REQ))) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_null));
			return;
		}
		List<Map<String, Object>> lists = (List<Map<String, Object>>) result.get(IsForex.ISFOREX_LIST_REQ);
		queryResultList.addAll(lists);
		String number = (String) result.get(IsForex.ISFOREX_RECORDERNUMBER_RES);
		recordNumber = Integer.valueOf(number);
	}

	/** 斩仓交易查询 ----回调 */
	public void requestPsnVFZcTradeInfoQueryCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_null));
			return;
		}
		if (!result.containsKey(IsForex.ISFOREX_LIST_REQ)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_null));
			return;
		}
		if (StringUtil.isNullOrEmpty(result.get(IsForex.ISFOREX_LIST_REQ))) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_null));
			return;
		}
		List<Map<String, Object>> lists = (List<Map<String, Object>>) result.get(IsForex.ISFOREX_LIST_REQ);
		queryResultList.addAll(lists);
		String number = (String) result.get(IsForex.ISFOREX_RECORDERNUMBER_RES);
		recordNumber = Integer.valueOf(number);
	}

	/** 对账单查询查询 ----回调 */
	public void requestPsnVFAccTradeInfoQueryCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_null));
			return;
		}
		if (!result.containsKey(IsForex.ISFOREX_LIST_REQ)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_null));
			return;
		}
		if (StringUtil.isNullOrEmpty(result.get(IsForex.ISFOREX_LIST_REQ))) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_transferquery_null));
			return;
		}
		List<Map<String, Object>> lists = (List<Map<String, Object>>) result.get(IsForex.ISFOREX_LIST_REQ);
		queryResultList.addAll(lists);
		String number = (String) result.get(IsForex.ISFOREX_RECORDERNUMBER_RES);
		recordNumber = Integer.valueOf(number);
	}

	/**
	 * 买卖预交易---查询汇率
	 * 
	 * @param currencyCode
	 *            :结算币种
	 * @param direction
	 *            :买卖方向
	 * @param currencyPairCode
	 *            :货币对
	 * @param tradeType
	 *            :交易类型
	 */
	public void getPsnVFGTradeConfirm(String currencyCode, String direction, String currencyPairCode, String tradeType) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGTRADECONFIRM_API);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> map = new HashMap<String, String>();
		map.put(IsForex.ISFOREX_CURRENCYCODES_REQ, currencyCode);
		map.put(IsForex.ISFOREX_DIRECTIONS_REQ, direction);
		map.put(IsForex.ISFOREX_CURRENCYPAIRCODES_REQ, currencyPairCode);
		map.put(IsForex.ISFOREX_TRADETYPES_REQ, tradeType);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "getPsnVFGTradeConfirmCallback");
	}

	/** 买卖预交易---回调 */
	public void getPsnVFGTradeConfirmCallback(Object resultObj) {

	}

	/**
	 * 双向宝指定平仓
	 */
	public void requestPSNVFGZHIDINGTRADE(String currencyCode,
			String closedVfgTransId,String closedInternanlSeq,
			String amount,String tradeType,String customRate,
			String currencyPairCode,String direction,String token,String dd){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGZHIDINGTRADE_API);
		biiRequestBody.setConversationId(dd);
		Map<String, String> map = new HashMap<String, String>();
		map.put(IsForex.ISFOREX_CURRENCYCODE1_REQ, currencyCode);
		map.put("closedVfgTransId", closedVfgTransId);
		map.put("closedInternalSeq", closedInternanlSeq);
		map.put(IsForex.ISFOREX_AMOUNT1_REQ, amount);
		map.put(IsForex.ISFOREX_TRADETYPE_REQ, tradeType);
		map.put(IsForex.ISFOREX_CUSTOMRATE, customRate);
		map.put(IsForex.ISFOREX_CURRENCYPAIRCODE_REQ, currencyPairCode);
		map.put(IsForex.ISFOREX_DIRECTION1_REQ, direction);
		map.put(IsForex.ISFOREX_TOCKEN_REQ, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPSNVFGZHIDINGTRADECallback");
	}
	
	
	public void requestPSNVFGZHIDINGTRADECallback(Object resultObj) {

	}
	
	
	/**
	 * 双向宝买卖交易
	 * 
	 * @param currencyPairCode
	 *            货币对
	 * @param currencyCode
	 *            结算币种
	 * @param direction
	 *            买卖方向
	 * @param openPositionFlag
	 *            建仓标识
	 * @param amount
	 *            交易金额
	 * @param tradeType
	 *            交易方式
	 * @param rate1
	 *            限价汇率
	 * @param rate2
	 *            委托汇率、止损汇率
	 * @param rate3
	 *            获利汇率
	 * @param profitStopType1
	 *            成交类型1
	 * @param profitStopType2
	 *            成交类型2
	 * @param profitStopType3
	 *            成交类型3
	 * @param pageDate
	 *            失效时间
	 * @param token
	 *            防重机制token
	 */
	public void requestPsnVFGTrade(String currencyPairCode, String currencyCode, String direction,
			String openPositionFlag, String amount, String tradeType, String rate1, String rate2, String rate3,
			String profitStopType1, String profitStopType2, String profitStopType3, String pageDate, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGTRADE_API);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> map = new HashMap<String, String>();
		map.put(IsForex.ISFOREX_CURRENCYPAIRCODE_REQ, currencyPairCode);
		map.put(IsForex.ISFOREX_CURRENCYCODE1_REQ, currencyCode);
		map.put(IsForex.ISFOREX_DIRECTION1_REQ, direction);
		map.put(IsForex.ISFOREX_OPENPOSITIONFLAG1_REQ, openPositionFlag);
		map.put(IsForex.ISFOREX_AMOUNT1_REQ, amount);
		map.put(IsForex.ISFOREX_TRADETYPE_REQ, tradeType);
		map.put(IsForex.ISFOREX_TOCKEN_REQ, token);
		// 市价即时 ：不需要汇率
		// 限价即时:rate1(输入)
		// 获利委托：rate1(输入)
		// 止损委托：rate1(输入)
		// 二选一委托： 获利汇率：rate1 止损汇率：rate2
		// 追加委托：如果***汇率rate1成交，则追加反向委托**汇率rate2
		// 连环委托：如果***汇率rate1成交，则追加反向委托获利汇率rate2 止损汇率rate3
		if (tradeType.equals(LocalData.isForexExchangeTypeCodeList.get(0))) {
			// 市价即时

		} else if (tradeType.equals(LocalData.isForexExchangeTypeCodeList.get(1))) {
			// 限价即时
			map.put(IsForex.ISFOREX_RATE1_REQ, rate1);
		} else if (tradeType.equals(LocalData.isForexExchangeTypeCodeList.get(2))) {
			// 获利委托
			map.put(IsForex.ISFOREX_RATE1_REQ, rate1);
			map.put(IsForex.ISFOREX_PAGEDATE_REQ, pageDate);
		} else if (tradeType.equals(LocalData.isForexExchangeTypeCodeList.get(3))) {
			// SO止损委托
			map.put(IsForex.ISFOREX_RATE1_REQ, rate1);
			map.put(IsForex.ISFOREX_PAGEDATE_REQ, pageDate);
		} else if (tradeType.equals(LocalData.isForexExchangeTypeCodeList.get(4))) {
			// OO二选一委托
			map.put(IsForex.ISFOREX_RATE1_REQ, rate1);
			map.put(IsForex.ISFOREX_RATE2_REQ, rate2);
			map.put(IsForex.ISFOREX_PAGEDATE_REQ, pageDate);
		} else if (tradeType.equals(LocalData.isForexExchangeTypeCodeList.get(5))) {
			// TODO------------- IO追加委托
			map.put(IsForex.ISFOREX_RATE1_REQ, rate1);
			map.put(IsForex.ISFOREX_RATE2_REQ, rate2);
			map.put(IsForex.ISFOREX_PROFITSTOPTYPE1_REQ, profitStopType1);
			map.put(IsForex.ISFOREX_PROFITSTOPTYPE2_REQ, profitStopType2);
		} else if (tradeType.equals(LocalData.isForexExchangeTypeCodeList.get(6))) {
			// TODO--------------TO连环委托
			map.put(IsForex.ISFOREX_RATE1_REQ, rate1);
			map.put(IsForex.ISFOREX_RATE2_REQ, rate2);
			map.put(IsForex.ISFOREX_RATE3_REQ, rate3);
			map.put(IsForex.ISFOREX_PROFITSTOPTYPE3_REQ, profitStopType3);
		}else if(tradeType.equals("FO")){
			//追击止损P603
			//openPositionFlag  建仓标识  当成交类型为追击止损挂单时需为N
			//7	rate1	限价汇率		汇率最多到小数点后四位（日元最多到小数点后两位）追击止损挂单时的客户指定的追击点差	
//		profitStopType1	成交类型1	追加委托时使用P获利，S止损     追击止损挂单时为S	
			map.put(IsForex.ISFOREX_OPENPOSITIONFLAG1_REQ, "N");
			map.put(IsForex.ISFOREX_RATE1_REQ, rate1);
			map.put(IsForex.ISFOREX_PROFITSTOPTYPE1_REQ, "S");
			map.put(IsForex.ISFOREX_PAGEDATE_REQ, pageDate);
		}
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGTradeCallback");
	}

	/** 双向宝买卖交易---回调 */
	public void requestPsnVFGTradeCallback(Object resultObj) {

	}

	/***
	 * 根据账户标志，查询账户详细信息
	 * 
	 * @param accountId
	 *            :账户标志
	 */
	public void requestPsnAccountQueryAccountDetail(String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNACCOUNTQUERYACCOUNTDETAIL_API);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> map = new Hashtable<String, String>();
		map.put(IsForex.ISFOREX_ACCOUNT_REQ, accountId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnAccountQueryAccountDetailCallback");
	}

	/**
	 * 根据账户标志，查询账户详细信息----回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnAccountQueryAccountDetailCallback(Object resultObj) {

	}

//	/** 保证金账户基本信息 */
//	public void requestPsnVFGGetBailAccountInfo() {
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGGETBAILACCOUNTINFO_API);
//		biiRequestBody.setConversationId(commConversationId);
//		biiRequestBody.setParams(null);
//		HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGGetBailAccountInfoCallback");
//	}

	/**双向宝保证金账户基本信息多笔查询**/
	public void requestPsnVFGGetBailAccountInfos() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGBAILACCOUNTINFOLISTQUERY_API);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGGetBailAccountInfoCallback");
	}
	
	
	/** 保证金账户基本信息------回调 */
	public void requestPsnVFGGetBailAccountInfoCallback(Object resultObj) {

	}

	/**
	 * 保证金存入/转出
	 * 
	 * @param currencyCode
	 *            :币种
	 * @param cashRemit
	 *            :钞汇
	 * @param fundTransferDir
	 *            :操作方式
	 * @param amount
	 *            :金额
	 * @param token
	 *            :防重标志
	 */
	public void requestPsnVFGBailTransfer(String currencyCode, String cashRemit, String fundTransferDir, String amount,
			String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGBAILTRANSFER_API);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> map = new HashMap<String, String>();
		map.put(IsForex.ISFOREX_CURRENCYCODE_REQ1, currencyCode);
		map.put(IsForex.ISFOREX_CASHREMIT_REQ, cashRemit);
		map.put(IsForex.ISFOREX_FUNDTRANSFERDIR_REQ, fundTransferDir);
		map.put(IsForex.ISFOREX_AMOUNT_REQ1, amount);
		map.put(IsForex.ISFOREX_TOCKEN_REQ1, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGBailTransferCallback");
	}

	/** 保证金存入/转出-----回调 */
	public void requestPsnVFGBailTransferCallback(Object resultObj) {

	}

	/** 过滤出符合条件的借记卡账户 */
	public void requestPsnVFGFilterDebitCard() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGFILTERDEBITCARD_API);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGFilterDebitCardCallback");
	}

	/** 过滤出符合条件的借记卡账户-----回调 */
	public void requestPsnVFGFilterDebitCardCallback(Object resultObj) {

	}

	/**
	 * 首次/重新设定双向宝账户
	 * 
	 * @param accountId
	 * @param token
	 */
	public void requestPsnVFGSetTradeAccount(String accountId, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGSETTRADEACCOUNT_API);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> params = new HashMap<String, String>();
		params.put(IsForex.ISFOREX_ACCOUNTID_REQ, accountId);
		params.put(IsForex.ISFOREX_TOKEN, token);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGSetTradeAccountCallback");
	}

	/** 首次/重新设定双向宝账户 */
	public void requestPsnVFGSetTradeAccountCallback(Object resultObj) {

	}

	public boolean httpRequestCallBackPre(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
			if (IsForex.ISFOREX_PSNVFGGETALLRATE_API.equals(biiResponseBody.getMethod())
					|| IsForex.ISFOREX_PSNVFGCUSTOMERSETRATE_API.equals(biiResponseBody.getMethod())) {
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
				return false;// 没有异常------查询异常
			} else if (IsForex.ISFOREX_PSNVFGBAILLISTQUERY_API.equals(biiResponseBody.getMethod())) {
				// 没有可签约保证金产品
				// 返回的是错误码
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
							} else if (ErrorCode.ISFOREX_NO_SIGN.equals(biiError.getCode())) {
								isSign = false;
								isSettingAcc = false;
								BaseHttpEngine.dissMissProgressDialog();
								getPop();
								return true;
							} else {
								// 查询版的用户
								BaseHttpEngine.dissMissProgressDialog();
								BaseDroidApp.getInstanse().createDialog("", biiError.getMessage(),
										new OnClickListener() {

											@Override
											public void onClick(View v) {
												BaseDroidApp.getInstanse().dismissMessageDialog();
//												ActivityTaskManager.getInstance().removeAllSecondActivity();
//												Intent intent = new Intent();
//												intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//														SecondMainActivity.class);
//												startActivity(intent);
												goToMainActivity();
												finish();

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
		}// 返回错误码
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
//				intent.setClass(IsForexBaseActivity.this, LoginActivity.class);
//				startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
//			}
//		});
//	}

	/** 未开通投资理财服务时查询是否设定双向宝交易账户 */
	public void requestNoOpenPsnVFGGetBindAccount() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGGETBINDACCOUNT_KEY);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "requestNoOpenPsnVFGGetBindAccountCallback");
	}

	/** 未开通投资理财服务时查询是否设定双向宝交易账户 */
	public void requestNoOpenPsnVFGGetBindAccountCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> accReaultMap = (Map<String, String>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(accReaultMap)) {
			isSettingAcc = false;
			isSign = false;
			return;
		} else {
			String accountNumber = accReaultMap.get(IsForex.ISFOREX_ACCOUNTNUMBER_REQ);
			if (StringUtil.isNull(accountNumber)) {
				isSettingAcc = false;
				isSign = false;
			} else {
				isSettingAcc = true;
				isSign = true;
			}
		}
		BaseHttpEngine.dissMissProgressDialog();
		getPop();
	}

	/** 查询保证金账户列表（签约产品列表）----任务提示框 */
	public void requestPsnVFGBailListQueryCondition() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGBAILLISTQUERY_API);
		biiRequestBody.setConversationId(commConversationId);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGBailListQueryConditionCallback");
	}

	/** 查询保证金账户列表（签约产品列表）----任务提示框--------回调 */
	public void requestPsnVFGBailListQueryConditionCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, String>> result = (List<Map<String, String>>) biiResponseBody.getResult();
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_RESULT_SIGN, result);
	}

	/** 查询保证金账户列表 */
	public void requestPsnVFGBailListQuery() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGBAILLISTQUERY_API);
		biiRequestBody.setConversationId(commConversationId);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGBailListQueryCallback");
	}

	/** 查询保证金账户列表--------回调 */
	public void requestPsnVFGBailListQueryCallback(Object resultObj) {

	}

	/** 双向宝持仓信息 */
	public void requestPsnVFGPositionInfo() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGPOSITIONINFO_KEY);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGPositionInfoCallbank");
	}

	/** 双向宝持仓信息----回调 */
	public void requestPsnVFGPositionInfoCallbank(Object resultObj) {

	}
	
	/**
	 * 打开账户选择页面
	 */
	protected void startLoginPriceActivity(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, IsForexTwoWayTreasureActivity.class);
		startActivity(intent);
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
	
	
	/**双向宝挂单点差范围查询
	 * wuhan
	 * @param currencyCode
	 * @param buyTradeType
	 */
		protected void queryisForexDianCha(String currencyPairCode) {
			BiiRequestBody biiRequestBody = new BiiRequestBody();
			biiRequestBody.setMethod(IsForex.ISFOREX_PSNMARGINPENDINGSETRANGEQUERY);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("currencyPairCode", currencyPairCode);////交易货币码
			
			biiRequestBody.setParams(map);
			HttpManager.requestBii(biiRequestBody, this, "queryisForexDianChaCallBack");
		}
		
		
		public void queryisForexDianChaCallBack(Object resultObj){
			
		}
		/**多笔行情查询*/
		protected  void requestQueryMultipleQuotation(String flag, String pSort){
			BiiRequestBody biiRequestBody = new BiiRequestBody();
			biiRequestBody.setMethod("queryMultipleQuotation");
			Map<String, Object> map = new HashMap<String, Object>();
			if(flag.equals("F")){//外汇
				map.put("cardType","F");
			}else if(flag.equals("G")) {//贵金属
				map.put("cardType","G");
			}
			map.put("cardClass", "M");
			map.put("pSort", pSort);
			biiRequestBody.setParams(map);
			HttpManager.requestOutlayBii(biiRequestBody, this, "requestQueryMultipleQuotationCallBack");
		}
		public void requestQueryMultipleQuotationCallBack(Object resultObj){

		}

	/**单笔行情查询*/
	protected  void requestQuerySingelQuotation(String currencyPairs, String cardType){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod("queryMultipleQuotation");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ccygrp", currencyPairs);//货币对代码
		map.put("cardType", cardType);//牌价类型
		map.put("cardClass", "M");//牌价种类
		biiRequestBody.setParams(map);
		HttpManager.requestOutlayBii(biiRequestBody, this, "requestQuerySingelQuotationCallBack");
	}

	public void requestQuerySingelQuotationCallBack(Object resultObj){

	}
	/**
	 * 趋势图查询queryAverageTendency
	 */
	protected void requestqueryAverageTendency(String flag, String ccygrp,String tendencyType) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod("queryMultipleQuotation");
		Map<String, Object> map = new HashMap<String, Object>();
		//ccygrp
		map.put("ccygrp",ccygrp);//货币对代码
		map.put("cardType",flag);
		map.put("cardClass", "R");
		map.put("tendencyType",tendencyType);//趋势类型
		biiRequestBody.setParams(map);
		HttpManager.requestOutlayBii(biiRequestBody, this, "requestqueryAverageTendencyCallBack");
	}
    public void requestqueryAverageTendencyCallBack(Object resultObj){

    }
    /**
     * queryKTendency K线图查询
     */
    protected void requestqueryKTendency(String flag,String ccygrp,String kType,String timeZone) {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod("queryMultipleQuotation");
        Map<String, Object> map = new HashMap<String, Object>();
        //ccygrp
        map.put("ccygrp",ccygrp);//货币对代码
        map.put("kType",kType);//趋势类型
        map.put("cardType",flag);
        map.put("cardClass", "R");
        map.put("timeZone",timeZone);//时间区间
        biiRequestBody.setParams(map);
        HttpManager.requestOutlayBii(biiRequestBody, this, "requestqueryKTendencyCallBack");
    }

    public void requestqueryKTendencyCallBack(Object resultObj){

    }

}
