package com.chinamworld.bocmbci.biz.lsforex.manageacc;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.biz.lsforex.adapter.IsForexBailProduceAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.thread.PollingRequestThread;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 签约账户列表页面 */
public class IsForexBailProduceActivity extends IsForexBaseActivity {
	private static final String TAG = "IsForexBailProduceActivity";
	private ListView productListView = null;
	private View rateInfoView = null;
	private List<Map<String, String>> result = null;
	private IsForexBailProduceAdapter adapter = null;
	private Button backButton = null;
	private int selectPosition = -1;
	private Map<String, String> resultDatail = null;
	private Button rightSignButton = null;

	private String accountNumber = null;
	private String nickName = null;
	private String marginAccountNo = null;
	private String settleCurrency = null;
	private String liquidationRatio = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.isForex_manage_title));
		stopPollingFlag();
		LogGloble.d(TAG, "onCreate");
		taskTag = 5;
		setLeftSelectedPosition("isForexStorageCash_5");
		backButton = (Button) findViewById(R.id.ib_back);
		rightSignButton = (Button) findViewById(R.id.ib_top_right_btn);
		rightSignButton.setVisibility(View.VISIBLE);
		rightSignButton.setText(getResources().getString(R.string.isForex_manage_sign));
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
		// 判断用户是否开通投资理财服务
		requestPsnInvestmentManageIsOpen();

	}

	/** 开通投资理财服务---回调 */
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
		result = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
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
		if (isOpen && isSign && isSettingAcc) {
			BaseDroidApp.getInstanse().dismissMessageDialogFore();
			// 判断是否需要掉接口查询签约产品信息
			isDateEmpty();
			return;
		} else {
			BaseHttpEngine.dissMissProgressDialog();
			// 弹出任提示框
			getPop();
			return;
		}

	}

	/** 判断是否需要掉接口查询签约产品信息 */
	private void isDateEmpty() {
		result = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.ISFOREX_RESULT_SIGN);
		if (StringUtil.isNullOrEmpty(result)) {
			// 已做完任务提示，查询保证金账户列表
			requestPsnVFGBailListQuery();
		} else {
			BaseHttpEngine.dissMissProgressDialog();
			init();
		}

	}

	public void requestPsnVFGBailListQueryCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		result = (List<Map<String, String>>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(resultObj)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		init();
	}

	private void init() {
		rateInfoView = LayoutInflater.from(this).inflate(R.layout.isforex_manamge_product, null);
		tabcontent.addView(rateInfoView);
		productListView = (ListView) findViewById(R.id.product_list);
		adapter = new IsForexBailProduceAdapter(this, result);
		productListView.setAdapter(adapter);
		adapter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Map<String, String> map = result.get(position);
				String accountNumber = map.get(IsForex.ISFOREX_ACCOUNTNUMBER_RES);
				String settleCurrency = map.get(IsForex.ISFOREX_SETTLECURRENCY_RES1);
				selectPosition = position;
				BaseHttpEngine.showProgressDialog();
				requestPsnVFGBailDetailQuery(accountNumber, settleCurrency);
			}
		});
		/** 签约 */
		rightSignButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isFinishing()) {
					Intent intent = new Intent(IsForexBailProduceActivity.this, IsForexProduceRadeActivity.class);
					// 签约成功后登记交易账户
					BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.GENDER_MAN, 1);
					// 用于区分签约成功返回入口
					BaseDroidApp.getInstanse().getBizDataMap().put(IsForex.ISFOREX_FLAG_RES, 11);
					startActivity(intent);
				}
			}
		});
	}

	/** 查询保证金账户详情 */
	private void requestPsnVFGBailDetailQuery(String accountNumbe, String settleCurrency) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGBAILDETAILQUERY_API);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> map = new HashMap<String, String>();
		map.put(IsForex.ISFOREX_ACCOUNTNUMBER_RES, accountNumbe);
		map.put(IsForex.ISFOREX_SETTLECURRENCY_RES1, settleCurrency);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGBailDetailQueryCallback");
	}

	/** 查询保证金账户详情----回调 */
	public void requestPsnVFGBailDetailQueryCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		resultDatail = (Map<String, String>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(resultDatail)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_RESULTDATAIL, resultDatail);
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_RESULT, result);
		Intent intent = new Intent(this, IsForexProduceDialogActivity.class);
		intent.putExtra(ConstantGloble.POSITION, selectPosition);
		startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_REGISTFUNCACC_CODE);
	}

	private void showView() {
		View popView = LayoutInflater.from(this).inflate(R.layout.isforex_manage_produce_detail, null);
		TextView bailAccText = (TextView) popView.findViewById(R.id.isforex_bailAcc);
		TextView tradeAccText = (TextView) popView.findViewById(R.id.isforex_tradeAcc);
		TextView nickNameText = (TextView) popView.findViewById(R.id.isforex_tradeAcc_nickname);
		TextView jsCodeText = (TextView) popView.findViewById(R.id.isForex_vfgRegCurrency1);
		TextView signText = (TextView) popView.findViewById(R.id.isForex_manage_produce_times);
		TextView zcRateText = (TextView) popView.findViewById(R.id.isForex_manage_liquidationRatio);
		TextView bjRateText = (TextView) popView.findViewById(R.id.isForex_manage_warnRatio);
		TextView needText = (TextView) popView.findViewById(R.id.isForex_manage_needMarginRatio);
		TextView kcRateText = (TextView) popView.findViewById(R.id.isForex_manage_openRate);
		Map<String, String> map = result.get(selectPosition);
		accountNumber = map.get(IsForex.ISFOREX_ACCOUNTNUMBER_RES);
		String subAcctType = map.get(IsForex.ISFOREX_SUBACCTTYPE);
		nickName = map.get(IsForex.ISFOREX_NICKNAME_RES1);
		marginAccountNo = map.get(IsForex.ISFOREX_MARGINACCOUNTNO_RES1);
		settleCurrency = map.get(IsForex.ISFOREX_SETTLECURRENCY_RES1);
		String signDate = map.get(IsForex.ISFOREX_SIGNDATE_RES);
		String needMarginRatio = resultDatail.get(IsForex.ISFOREX_NEEDMARGINRATE_RES);
		String warnRatio = resultDatail.get(IsForex.ISFOREX_WARNRATIO_RES);
		liquidationRatio = resultDatail.get(IsForex.ISFOREX_LIQUIDATIONRATIO_RES);
		String openRate = resultDatail.get(IsForex.ISFOREX_OPENRATE_RES);
		String bailAcc = null;
		if (!StringUtil.isNull(marginAccountNo)) {
			bailAcc = StringUtil.getForSixForString(marginAccountNo);
		}
		String tradeAcc = null;
		if (!StringUtil.isNull(accountNumber)) {
			tradeAcc = StringUtil.getForSixForString(accountNumber);
		}
		String code = null;
		if (!StringUtil.isNull(settleCurrency) && LocalData.Currency.containsKey(settleCurrency)) {
			code = LocalData.Currency.get(settleCurrency);
		}
		String zcRate = null;
		if (StringUtil.isNull(liquidationRatio)) {
			zcRateText.setText("-");
		} else {
			zcRate = StringUtil.dealNumber(liquidationRatio);
			zcRateText.setText(zcRate);
		}
		String bjRate = null;
		if (StringUtil.isNull(warnRatio)) {
			bjRateText.setText("-");
		} else {
			bjRate = StringUtil.dealNumber(warnRatio);
			bjRateText.setText(bjRate);
		}
		String kcRate = StringUtil.dealNumber(openRate);
		if (StringUtil.isNull(openRate)) {
			kcRateText.setText("-");
		} else {
			kcRate = StringUtil.dealNumber(openRate);
			kcRateText.setText(kcRate);
		}

		String needRate = StringUtil.dealNumber(needMarginRatio);
		if (StringUtil.isNull(needMarginRatio)) {
			needText.setText("-");
		} else {
			needRate = StringUtil.dealNumber(needMarginRatio);
			needText.setText(needRate);
		}
		bailAccText.setText(bailAcc);
		tradeAccText.setText(tradeAcc);
		nickNameText.setText(nickName);
		String cash = null;
		// 0 – 人民币户 1 – 单钞户 3 – 钞汇户 即 1、人民币账户 2、XX现钞账户 3、XX钞汇账户
		if (!StringUtil.isNull(subAcctType)) {
			if (ConstantGloble.FOREX_FLAG0.equals(subAcctType)) {
				cash = getString(R.string.isForex_manage_cash_rmb);
				jsCodeText.setText(cash);
			} else if (ConstantGloble.FOREX_FLAG1.equals(subAcctType)) {
				cash = getString(R.string.isForex_manage_cash_cash1);
				jsCodeText.setText(code + cash);
			} else if (ConstantGloble.FOREX_FLAG3.equals(subAcctType)) {
				cash = getString(R.string.isForex_manage_cash_cash2);
				jsCodeText.setText(code + cash);
			} else {
				jsCodeText.setText(code);
			}
		}
		signText.setText(signDate);

		// 变更按钮
		Button changeButton = (Button) popView.findViewById(R.id.dept_checkout_btn);
		// 解约按钮
		Button signButton = (Button) popView.findViewById(R.id.dept_create_notice_btn);
		// 关闭按钮
		ImageButton closeButton = (ImageButton) popView.findViewById(R.id.dept_close_ib);

		signButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 账户解约
				Intent intent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(),
						IsForexCancelConfirmActivity.class);
				intent.putExtra(IsForex.ISFOREX_ACCOUNTNUMBER_RES, accountNumber);
				intent.putExtra(IsForex.ISFOREX_NICKNAME_RES1, nickName);
				intent.putExtra(IsForex.ISFOREX_MARGINACCOUNTNO_RES1, marginAccountNo);
				intent.putExtra(IsForex.ISFOREX_SETTLECURRENCY_RES1, settleCurrency);
				intent.putExtra(IsForex.ISFOREX_LIQUIDATIONRATIO_RES, liquidationRatio);
				startActivity(intent);
			}
		});
		changeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 账户变更
				BaseHttpEngine.showProgressDialog();
				// 过滤出符合条件的借记卡
				requestPsnVFGFilterDebitCard();
			}
		});
		closeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().dismissMessageDialogFore();
			}
		});
		BaseDroidApp.getInstanse().showForexMessageDialog(popView);
	}

	// /** 过滤出符合条件的借记卡----回调 */
	// @Override
	// public void requestPsnVFGFilterDebitCardCallback(Object resultObj) {
	// super.requestPsnVFGFilterDebitCardCallback(resultObj);
	// BiiResponse biiResponse = (BiiResponse) resultObj;
	// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
	// // 得到response
	// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
	// List<Map<String, String>> resultList = (List<Map<String, String>>)
	// biiResponseBody.getResult();
	// if (resultList == null || resultList.size() <= 0) {
	// BaseHttpEngine.dissMissProgressDialog();
	// BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
	// return;
	// }
	// if (StringUtil.isNull(accountNumber)) {
	// BaseHttpEngine.dissMissProgressDialog();
	// BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
	// return;
	// }
	// getRightTradeAcc(resultList);
	//
	// }
	//
	// /** 得到符合条件的借记卡 */
	// private void getRightTradeAcc(List<Map<String, String>> resultList) {
	// List<Map<String, String>> newTradeAccResultList = null;
	// newTradeAccResultList = new ArrayList<Map<String, String>>();
	// int len = resultList.size();
	// for (int i = 0; i < len; i++) {
	// Map<String, String> map = resultList.get(i);
	// String account = map.get(IsForex.ISFOREX_ACCOUNTNUMBER_RES);
	// if (!StringUtil.isNull(account)) {
	// if (!account.equals(accountNumber)) {
	// newTradeAccResultList.add(map);
	// }
	// }
	// }
	// // 过滤之后没有符合条件的借记卡
	// if (newTradeAccResultList == null || newTradeAccResultList.size() <= 0) {
	// BaseHttpEngine.dissMissProgressDialog();
	// BaseDroidApp.getInstanse().showInfoMessageDialog(
	// getResources().getString(R.string.isForex_times_change_noacc));
	// return;
	// }
	// BaseDroidApp.getInstanse().getBizDataMap().put(IsForex.ISFOREX_ACCOUNTNUMBER_RES,
	// newTradeAccResultList);
	// gotoActivity();
	// }
	//
	// /** 跳转到账户变更页面 */
	// private void gotoActivity() {
	// Intent intent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(),
	// IsForexChangeSubmitActivity.class);
	// intent.putExtra(IsForex.ISFOREX_ACCOUNTNUMBER_RES, accountNumber);
	// intent.putExtra(IsForex.ISFOREX_NICKNAME_RES1, nickName);
	// intent.putExtra(IsForex.ISFOREX_MARGINACCOUNTNO_RES1, marginAccountNo);
	// intent.putExtra(IsForex.ISFOREX_SETTLECURRENCY_RES1, settleCurrency);
	// intent.putExtra(IsForex.ISFOREX_LIQUIDATIONRATIO_RES, liquidationRatio);
	// BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.GENDER_MAN,
	// 1);
	// BaseHttpEngine.dissMissProgressDialog();
	// startActivity(intent);
	// }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
//			ActivityTaskManager.getInstance().removeAllActivity();
//			Intent intent = new Intent();
//			intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), MainActivity.class);
//			BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
			goToMainActivity();
		}
		return super.onKeyDown(keyCode, event);
	}

}
