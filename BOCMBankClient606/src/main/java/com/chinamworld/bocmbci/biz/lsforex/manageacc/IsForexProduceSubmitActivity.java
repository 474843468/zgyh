package com.chinamworld.bocmbci.biz.lsforex.manageacc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Third;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 签约选择资金账户、保证金账户页面 */
public class IsForexProduceSubmitActivity extends IsForexBaseActivity {
	public static final String TAG = "IsForexProduceSubmitActivity";
	private Button backButton = null;
	private View rateInfoView = null;
	/** 资金账户 */
	private Spinner tradeAccSpinner = null;
	private TextView baillAccSpinner = null;
	private TextView typeText = null;
	private TextView nickText = null;
	private TextView cNameText = null;
	private TextView eNameText = null;
	private TextView jsCodeText = null;
	private TextView zcRateText = null;
	private TextView warnRateText = null;
	private TextView needRateText = null;
	private TextView openRateText = null;
	private TextView left_needText = null;
	private Button nextButton = null;
	/** 符合条件的借记卡信息 */
	private List<Map<String, String>> resultList = null;
	/** 资金账户list */
	private List<String> tradeList = null;
	/** 可签约的保证金产品list */
	private List<Map<String, String>> signBailList = null;
	private int tradePosition = -1;
	private int bailPosition = -1;
	private boolean initTrade = true;
	private boolean initBail = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.isForex_manage_right));
		LogGloble.d(TAG, "onCreate");
		backButton = (Button) findViewById(R.id.ib_back);
		rateInfoView = LayoutInflater.from(this).inflate(R.layout.isforex_manage_sign_submit, null);
		tabcontent.addView(rateInfoView);
		signBailList = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap().get(IsForex.ISFOREX_LIST);
		resultList = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap().get(IsForex.ISFOREX_LIST_REQ);
		tradeList = new ArrayList<String>();
		bailPosition = getIntent().getIntExtra(ConstantGloble.ACC_POSITION, -1);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		getSpinnerDate();
		init();
		initOnClick();
	}

	/** 得到Spinner的date */
	private void getSpinnerDate() {
		if (StringUtil.isNullOrEmpty(signBailList) || StringUtil.isNullOrEmpty(resultList)) {
			return;
		}
		// 得到资金账户的list
		int len = resultList.size();
		for (int i = 0; i < len; i++) {
			Map<String, String> map = resultList.get(i);
			String accountNumber = map.get(IsForex.ISFOREX_ACCOUNTNUMBER_REQ1);
			String account = StringUtil.getForSixForString(accountNumber);
			tradeList.add(account);
		}
	}

	private void init() {
		tradeAccSpinner = (Spinner) findViewById(R.id.isForex_tradeAcc);
		typeText = (TextView) findViewById(R.id.forex_customer_accType);
		nickText = (TextView) findViewById(R.id.forex_customer_accAlias);
		baillAccSpinner = (TextView) findViewById(R.id.isForex_BaillAcc);
		cNameText = (TextView) findViewById(R.id.isForex_manage_bailCNamen);
		eNameText = (TextView) findViewById(R.id.isForex_manage_bailEName);
		jsCodeText = (TextView) findViewById(R.id.isForex_vfgRegCurrency1);
		zcRateText = (TextView) findViewById(R.id.isForex_manage_liquidationRatio);
		warnRateText = (TextView) findViewById(R.id.isForex_manage_warnRatio);
		needRateText = (TextView) findViewById(R.id.isForex_manage_needMarginRatio);
		openRateText = (TextView) findViewById(R.id.isForex_manage_openRate);
		left_needText = (TextView) findViewById(R.id.left_text);
		nextButton = (Button) findViewById(R.id.trade_nextButton);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, left_needText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, nickText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, cNameText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, eNameText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, zcRateText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, warnRateText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, needRateText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, openRateText);

		// 保证金产品编号 保证金中文名称 保证金英文名称 弹出框
		TextView tv_chinese = (TextView) findViewById(R.id.tv_chinese);
		TextView tv_english = (TextView) findViewById(R.id.tv_english);
		TextView tv_number = (TextView) findViewById(R.id.tv_number);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_chinese);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_english);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_number);

		ArrayAdapter<String> tradeAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, tradeList);
		tradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		tradeAccSpinner.setAdapter(tradeAdapter);
		tradeAccSpinner.setSelection(0, true);
		tradePosition = 0;
		// 为资金账户赋值
		getTradePositonValue(0);
		// 为保证金账户赋值
		getBailPositionValue(bailPosition);

	}

	/** 资金账户的数据 */
	private void getTradePositonValue(int position) {
		Map<String, String> map = resultList.get(position);
		String accountType = map.get(IsForex.ISFOREX_ACCOUNTTYPE_REQ1);
		String type = null;
		if (!StringUtil.isNull(accountType) && LocalData.AccountType.containsKey(accountType)) {
			type = LocalData.AccountType.get(accountType);
		}
		String nickName = map.get(IsForex.ISFOREX_NICKNAME_REQ1);
		typeText.setText(type);
		nickText.setText(nickName);
	}

	/** 保证金账户数据 */
	private void getBailPositionValue(int position) {
		Map<String, String> map = signBailList.get(position);
		String bailNo = map.get(IsForex.ISFOREX_BAILNO_RES);
		String bailCName = map.get(IsForex.ISFOREX_BAILCNAME_RES);
		String bailEName = map.get(IsForex.ISFOREX_BAILENAME_RES);
		String settleCurrency = map.get(IsForex.ISFOREX_SETTLECURRENCY_RES1);
		String liquidationRatio = map.get(IsForex.ISFOREX_LIQUIDATIONRATIO_RES);
		String warnRatio = map.get(IsForex.ISFOREX_WARNRATIO_RES);
		String needMarginRatio = map.get(IsForex.ISFOREX_NEEDMARGINRATE_RES);
		String openRate = map.get(IsForex.ISFOREX_OPENRATE_RES);
		baillAccSpinner.setText(bailNo);
		cNameText.setText(bailCName);
		eNameText.setText(bailEName);
		String jsCode = null;
		if (!StringUtil.isNull(settleCurrency) && LocalData.Currency.containsKey(settleCurrency)) {
			jsCode = LocalData.Currency.get(settleCurrency);
			jsCodeText.setText(jsCode);
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
			warnRateText.setText("-");
		} else {
			bjRate = StringUtil.dealNumber(warnRatio);
			warnRateText.setText(bjRate);
		}
		String kcRate = StringUtil.dealNumber(openRate);
		if (StringUtil.isNull(openRate)) {
			openRateText.setText("-");
		} else {
			kcRate = StringUtil.dealNumber(openRate);
			openRateText.setText(kcRate);
		}

		String needRate = StringUtil.dealNumber(needMarginRatio);
		if (StringUtil.isNull(needMarginRatio)) {
			needRateText.setText("-");
		} else {
			needRate = StringUtil.dealNumber(needMarginRatio);
			needRateText.setText(needRate);
		}
	}

	private void initOnClick() {
		tradeAccSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// if (!initTrade) {
				// 清空原来的数据
				clearTradeDate();
				// 为资金账户赋值
				getTradePositonValue(position);
				tradePosition = position;
				// } else {
				// initTrade = false;
				// }

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				// 查询资金账号是否已签约保证金
				Map<String, String> map = resultList.get(tradePosition);
				String accountId = map.get(IsForex.ISFOREX_ACCOUNT_REQ);
				Map<String, String> map1 = signBailList.get(bailPosition);
				String settleCurrency = map1.get(IsForex.ISFOREX_SETTLECURRENCY_RES1);
				PsnVFGCheckAccountRegistered(accountId, settleCurrency);
			}
		});
	}

	/** 清空资金账户数据 */
	private void clearTradeDate() {
		typeText.setText("");
		nickText.setText("");
	}

	/** 查询资金账号是否已签约保证金 */
	private void PsnVFGCheckAccountRegistered(String accountId, String settleCurrency) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGCHECKACCOUNTREGISTERED_API);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> map = new HashMap<String, String>();
		map.put(IsForex.ISFOREX_ACCOUNT_REQ, accountId);
		map.put(IsForex.ISFOREX_SETTLECURRENCY1_RES, settleCurrency);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "PsnVFGCheckAccountRegisteredCallback");
	}

	/** 查询资金账号是否已签约保证金-----回调 */
	public void PsnVFGCheckAccountRegisteredCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		String registered = (String) result.get(IsForex.ISFOREX_REGISTERED_RES);
		if (StringUtil.isNull(registered)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		if (ConstantGloble.TRUE.equals(registered)) {
			// true:已签约
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.isForex_times_sign_err));
			return;
		} else {
			requestCommConversationId();
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		// 请求安全因子组合id
		requestGetSecurityFactor(ConstantGloble.ISFOREX_SERVICEID);

	}

	// 请求安全因子组合id
	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);

		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 双向宝签约预交易
				PsnVFGSignPre(BaseDroidApp.getInstanse().getSecurityChoosed());
			}
		});
	}

	/**
	 * 双向宝签约预交易
	 * 
	 * @param accountId
	 *            :账户标识
	 * @param accountNumber
	 *            :借记卡卡号
	 * @param bailName
	 *            :保证金产品名称
	 * @param bailNo
	 *            :保证金产品序号
	 */
	private void PsnVFGSignPre(String combin) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGSIGNPRE_API);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> map = resultList.get(tradePosition);
		String accountId = map.get(IsForex.ISFOREX_ACCOUNT_REQ);
		String accountNumber = map.get(IsForex.ISFOREX_ACCOUNTNUMBER_REQ1);
		Map<String, String> bailMap = signBailList.get(bailPosition);
		String bailEName = bailMap.get(IsForex.ISFOREX_BAILCNAME_RES);
		String bailNo = bailMap.get(IsForex.ISFOREX_BAILNO_RES);
		Map<String, String> maps = new HashMap<String, String>();
		maps.put(IsForex.ISFOREX_ACCOUNT_REQ, accountId);
		maps.put(IsForex.ISFOREX_ACCOUNTNUMBER_REQ1, accountNumber);
		maps.put(IsForex.ISFOREX_BAILNAME_RES, bailEName);
		maps.put(IsForex.ISFOREX_BAILNO_RES, bailNo);
		maps.put(Third.OPENDACC_CONFIRM_COMBINID, combin);
		biiRequestBody.setParams(maps);
		HttpManager.requestBii(biiRequestBody, this, "PsnVFGSignPreCallback");
	}

	/** 双向宝签约预交易----回调 */
	public void PsnVFGSignPreCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> preResult = (Map<String, Object>) biiResponseBody.getResult();
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_PRERESULT_KEY, preResult);
		// 请求密码控件随机数
		requestForRandomNumber();
	}

	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "queryRandomNumberCallBack");
	}

	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String randomNumber = (String) biiResponseBody.getResult();
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_RANDOMNUMBER_KEY, randomNumber);
		gotoActivity();
	}

	private void gotoActivity() {
		Intent intent = new Intent(IsForexProduceSubmitActivity.this, IsForexProduceConfirmActivity.class);
		intent.putExtra(ConstantGloble.POSITION, tradePosition);// 资金账户position
		intent.putExtra(ConstantGloble.ACC_POSITION, bailPosition);// 保证金账户position
		BaseHttpEngine.dissMissProgressDialog();
		// startActivity(intent);
		startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_REGISTFUNCACC_CODE);// 10
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;
		default:
			break;
		}
	}
}
