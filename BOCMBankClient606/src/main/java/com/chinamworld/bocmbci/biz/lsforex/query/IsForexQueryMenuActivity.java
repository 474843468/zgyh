package com.chinamworld.bocmbci.biz.lsforex.query;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.thread.PollingRequestThread;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.List;
import java.util.Map;

/** 交易查询 菜单页面 */
public class IsForexQueryMenuActivity extends IsForexBaseActivity implements OnClickListener {
	public static final String TAG = "IsForexQueryMenuActivity";
	/** 查询菜单View */
	private View queryMenuView = null;
	/** 成交状况查询View */
	private View historyView = null;
	/** 斩仓交易查询View */
	private View zCView = null;
	/** 未平仓交易查询View */
	private View wPCView = null;
	/** 对账单查询View */
	private View accView = null;
	private Button backButton = null;
	/** 当前有效委托 */
	private View currentQueryView = null;
	/** 历史委托 */
	private View historyQueryView = null;
	/** 是否显示委托状况查询 */
	private boolean isShowWeiTuo = true;
	private boolean isInit = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		stopPollingFlag();
		/** 为界面标题赋值 */
		setTitle(getResources().getString(R.string.isForex_query_menu_title));
		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setVisibility(View.GONE);
		setLeftSelectedPosition("isForexStorageCash_4");
		backButton = (Button) findViewById(R.id.ib_back);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), SecondMainActivity.class);
//				BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
				finish();
			}
		});
		taskTag = 3;
		BaseDroidApp.getInstanse().setCurrentAct(this);
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestCommConversationId();
	}

	/** 停止轮询 */
	private void stopPollingFlag() {
		if (HttpManager.mPollingRequestThread != null && PollingRequestThread.pollingFlag) {
			LogGloble.e(TAG, "onPause() mPollingRequestThread  stopPolling()  ");
			HttpManager.stopPolling();
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		if (StringUtil.isNull(commConversationId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			// 判断用户是否开通投资理财服务
			requestPsnInvestmentManageIsOpen();
		}
	}

	/** 开通投资理财服务---回调 */
	@Override
	public void requestMenuIsOpenCallback(Object resultObj) {
		super.requestMenuIsOpenCallback(resultObj);
		isInit = true;
		if (isOpen) {
			// 查询是否签约保证金产品
			searchBaillAfterOpen=1;
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
			// 弹出任务提示框
			showPop();
		} else {
			// 初始化----弹出任务提示框
			isSign = true;
			isCondition = true;
			// 是否设置双向宝账户
			requestPsnVFGGetBindAccount();

		}
	}

	/** 弹出任务提示框 */
	private void showPop() {
		isOpen = true;
		isSign = false;
		isSettingAcc = false;
		BaseHttpEngine.dissMissProgressDialog();
		// 弹出任提示框
		getPop();
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
		if (isOpen && isSign && isSettingAcc) {
			BaseDroidApp.getInstanse().dismissMessageDialogFore();
			isCondition = false;
			BaseHttpEngine.dissMissProgressDialog();
			// 显示页面
			init();
			return;
		} else {
			BaseHttpEngine.dissMissProgressDialog();
			// 弹出任提示框
			getPop();
			return;
		}

	}

	private void init() {
		queryMenuView = LayoutInflater.from(IsForexQueryMenuActivity.this).inflate(R.layout.isforex_query_menu, null);
		tabcontent.addView(queryMenuView);
		historyView = findViewById(R.id.menu1);
		zCView = findViewById(R.id.menu2);
		wPCView = findViewById(R.id.menu3);
		accView = findViewById(R.id.menu4);
		currentQueryView = findViewById(R.id.menu11);
		historyQueryView = findViewById(R.id.menu12);
		historyView.setOnClickListener(this);
		zCView.setOnClickListener(this);
		wPCView.setOnClickListener(this);
		accView.setOnClickListener(this);
		currentQueryView.setOnClickListener(this);
		historyQueryView.setOnClickListener(this);
		if (isShowWeiTuo) {
			currentQueryView.setVisibility(View.VISIBLE);
			historyQueryView.setVisibility(View.VISIBLE);
			currentQueryView.setBackgroundResource(R.drawable.selector_for_click_first_item);
			historyView.setBackgroundResource(R.drawable.selector_for_click_item);

		} else {
			currentQueryView.setVisibility(View.GONE);
			historyQueryView.setVisibility(View.GONE);
			historyView.setBackgroundResource(R.drawable.selector_for_click_first_item);

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu1:// 成交状况查询
			queryTag = 1;
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_QUERYTAG, queryTag);
			Intent intent1 = new Intent(IsForexQueryMenuActivity.this, IsForexHistoryQueryActivity.class);
			startActivity(intent1);
			break;
		case R.id.menu2:// 斩仓交易查询
			queryTag = 2;
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_QUERYTAG, queryTag);
			Intent intent2 = new Intent(IsForexQueryMenuActivity.this, IsForexHistoryQueryActivity.class);
			startActivity(intent2);
			break;
		case R.id.menu3:// 未平仓交易查询
			queryTag = 3;
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_QUERYTAG, queryTag);
			Intent intent3 = new Intent(IsForexQueryMenuActivity.this, IsForexHistoryQueryActivity.class);
			startActivity(intent3);
			break;
		case R.id.menu4:// 对账单查询
			queryTag = 4;
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_QUERYTAG, queryTag);
			Intent intent4 = new Intent(IsForexQueryMenuActivity.this, IsForexHistoryQueryActivity.class);
			startActivity(intent4);
			break;
		case R.id.menu11:// 当前有效委托
			queryTag = 5;
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_QUERYTAG, queryTag);
			Intent intent5 = new Intent(IsForexQueryMenuActivity.this, IsForexCurrenyWTActivity.class);
			startActivity(intent5);
			break;
		case R.id.menu12:// 历史委托查询
			queryTag = 6;
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_QUERYTAG, queryTag);
			Intent intent6 = new Intent(IsForexQueryMenuActivity.this, IsForexHistoryWTActivity.class);
			startActivity(intent6);
			break;
		default:
			break;
		}

	}

}
