package com.chinamworld.bocmbci.biz.gatherinitiative.creatgather;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.DrawMoney;
import com.chinamworld.bocmbci.bii.constant.GatherInitiative;
import com.chinamworld.bocmbci.bii.constant.Inves;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.gatherinitiative.GatherBaseActivity;
import com.chinamworld.bocmbci.biz.gatherinitiative.GatherInitiativeData;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: GatherInputPreInfoActivity
 * @Description: 填写收款信息页面
 * @author JiangWei
 * @date 2013-8-21下午4:55:49
 */
public class GatherInputPreInfoActivity extends GatherBaseActivity {
	/** 收款人姓名 */
	private TextView textName;
	/** 收款币种 */
	private TextView textCurrency;
	/** 收款账户下拉框 */
	private Spinner accountNumSpinner;
	/** 收款金额 */
	private EditText editGatherAmount;
	/** 备注 */
	private EditText editRemark;
	/** 下一步按钮 */
	private Button btnNext;
	/** 收款人姓名str */
	private String nameStr;
	/** 收款人手机号str */
	private String payeePhoneStr;
	/** 收款币种str */
	private String currencyStr;
	/** 收款金额str */
	private String gatherAmountStr;
	/** 备注 str */
	private String remarkStr;
	/** 账户列表 */
	private List<Map<String, Object>> accountList;
	/** accountId集合 */
	private ArrayList<String> accountIdList;
	/** accountNumber集合 */
	private ArrayList<String> accountNoList;
	/** 账户id */
	private String accountId;
	/** 账号 */
	private String payeeActnoStr;
	/** 会话id */
	private String conversationId;
	/** 安全因子数组 */
	private List<Map<String, Object>> combineList;
	/** 是否需要otp */
	private boolean isNeedOtp;
	/** 是否需要smc */
	private boolean isNeedSmc;
	/** 付款人类型 */
	private String payerChanelStr;
	/** 付款人客户号 */
	private String payerCustIdStr = "";
	/** 付款人手机号 */
	private String payerPhoneStr;
	/** 付款人姓名 */
	private String payerNameStr;
	private boolean isNeedChooseAccount;
	/** 当没有转入账户时的提示语*/
	private String message = "您没有符合类型的收款账户";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(R.string.creat_new_gather);
		View view = LayoutInflater.from(this).inflate(R.layout.gather_input_pre_activity, null);
		tabcontent.addView(view);

		accountList = GatherInitiativeData.getInstance().getQueryAcountCallBackList();
		payerChanelStr = this.getIntent().getStringExtra(GatherInitiative.PAYER_CHANNEL);
		payerPhoneStr = this.getIntent().getStringExtra(GatherInitiative.PAYER_MOBILE);
		payerNameStr = this.getIntent().getStringExtra(GatherInitiative.PAYER_NAME);
		isNeedChooseAccount = this.getIntent().getBooleanExtra(GatherBaseActivity.IS_NEED_CHOOSE_ACCOUNT, true);
//		if ("1".equals(payerCustIdStr)) {
			payerCustIdStr = this.getIntent().getStringExtra(GatherInitiative.PAYER_CUST_ID);
//		}
		init();
	}

	/**
	 * @Title: init
	 * @Description: 初始化控件和数据
	 * @param
	 * @return void
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	private void init() {
		textName = (TextView) this.findViewById(R.id.tv_gather_money_name);
		textCurrency = (TextView) this.findViewById(R.id.tv_gather_money_type);
		accountNumSpinner = (Spinner) this.findViewById(R.id.account_number_spinner);
		editGatherAmount = (EditText) this.findViewById(R.id.edit_gather_money_ammount);
		editRemark = (EditText) this.findViewById(R.id.edit_beizhu);
		EditTextUtils.setLengthMatcher(this, editRemark, 20);// 主动收款附言改为20字符
		btnNext = (Button) this.findViewById(R.id.next_btn);

		nameStr = (String) ((Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA)).get(Inves.CUSTOMERNAME);
		payeePhoneStr = (String) ((Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA)).get(GatherInitiative.MOBILE);
		textName.setText(nameStr);
		currencyStr = "001";
		textCurrency.setText(R.string.rmb_currency);
		accountIdList = new ArrayList<String>();
		accountNoList = new ArrayList<String>();
		ArrayList<String> acountNumberFixedList = new ArrayList<String>();
		for (int i = 0; i < accountList.size(); i++) {
			String accountId = (String) accountList.get(i).get(Comm.ACCOUNT_ID);
			String accountNO = (String) accountList.get(i).get(Comm.ACCOUNTNUMBER);
			accountIdList.add(accountId);
			accountNoList.add(accountNO);
			acountNumberFixedList.add(StringUtil.getForSixForString(String
				.valueOf(accountNO)));
		}
		
		ArrayAdapter<ArrayList<String>> adapter1 = new ArrayAdapter(this, R.layout.custom_spinner_item,
				acountNumberFixedList);
		if(acountNumberFixedList.isEmpty()){
			BaseDroidApp.getInstanse().showMessageDialog(message, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					GatherInputPreInfoActivity.this.finish();
				}
			});
		}
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		accountNumSpinner.setAdapter(adapter1);
		accountNumSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				accountId = accountIdList.get(position);
				payeeActnoStr = accountNoList.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				accountId = accountIdList.get(0);
				payeeActnoStr = accountNoList.get(0);
			}
		});
		
		if(!isNeedChooseAccount){
			accountNumSpinner.setClickable(false);
			accountNumSpinner.setBackgroundResource(R.drawable.bg_spinner_default);
		}

		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				excuseNext();
			}
		});
	}

	/**
	 * @Title: excuseNext
	 * @Description: 执行下一步操作
	 * @param
	 * @return void
	 * @throws
	 */
	private void excuseNext() {
		gatherAmountStr = editGatherAmount.getText().toString().trim();
		remarkStr = editRemark.getText().toString().trim();
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		RegexpBean regAmount = new RegexpBean(this.getString(R.string.gather_money_ammount), gatherAmountStr, "amount");
		lists.add(regAmount);
		if (RegexpUtils.regexpDate(lists)) {
			// 请求conversationId
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		conversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		// 请求安全因子
		requestGetSecurityFactor("PB037");
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 请求预交易
				requestPsnTransActCollectionVerify();
			}
		});
	}

	/**
	 * @Title: requestPsnTransActCollectionVerify
	 * @Description: 请求"主动收款预交易"接口
	 * @param
	 * @return void
	 * @throws
	 */
	private void requestPsnTransActCollectionVerify() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(GatherInitiative.PSN_TRANS_ACT_COLLECTION_VERIFY);
		biiRequestBody.setConversationId(conversationId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(GatherInitiative.TO_ACCOUNTID, accountId);
		map.put(GatherInitiative.PAYEE_NAME, nameStr);
		map.put(GatherInitiative.CURRENCY, currencyStr);
		map.put(GatherInitiative.NOTIFY_PAYEE_AMOUNT, gatherAmountStr);
		map.put(GatherInitiative.PAYER_CUST_ID, payerCustIdStr);
		map.put(GatherInitiative.REMARK, remarkStr);
		map.put(GatherInitiative.PAYER_MOBILE, payerPhoneStr);
		map.put(GatherInitiative.PAYER_NAME, payerNameStr);
		map.put(GatherInitiative.PAYEE_MOBILE, payeePhoneStr);
		map.put(GatherInitiative.PAYER_CHANNEL, payerChanelStr);
		map.put(GatherInitiative.COMBIN_ID, BaseDroidApp.getInstanse().getSecurityChoosed());
		map.put(GatherInitiative.PAYEE_ACT_NO, payeeActnoStr);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnTransActCollectionVerifyCallback");
	}

	/**
	 * @Title: requestPsnTransActCollectionVerifyCallback
	 * @Description: 请求"主动收款预交易"接口的回调
	 * @param
	 * @return void
	 * @throws
	 */
	public static Map<String, Object> result;
	@SuppressWarnings("unchecked")
	public void requestPsnTransActCollectionVerifyCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		result = (Map<String, Object>) biiResponseBody.getResult();
		combineList = (List<Map<String, Object>>) result.get(DrawMoney.FACTORLIST);
		if (StringUtil.isNullOrEmpty(combineList)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		// 获取factorList 判断交易需要的安全因子
//		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
//		for (int i = 0; i < combineList.size(); i++) {
//			Map<String, Object> defaultCombine = (Map<String, Object>) combineList.get(i).get("field");
//			listMap.add(defaultCombine);
//		}
//		if (combineList.size() == 1) {
//			String str = (String) listMap.get(0).get(Login.NAME);
//			if (str.equals(DrawMoney.SMC)) {
//				isNeedSmc = true;
//				isNeedOtp = false;
//			}
//		} else {
//			isNeedSmc = true;
//			isNeedOtp = false;
//		}
		requestForRandomNumber();
	}

	/**
	 * @Title: requestForRandomNumber
	 * @Description: 请求随机数
	 * @param
	 * @return void
	 */
	private void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId(conversationId);
		// 获取 随机数
		HttpManager.requestBii(biiRequestBody, this, "queryRandomNumberCallBack");
	}

	/**
	 * @Title: queryRandomNumberCallBack
	 * @Description: 请求随机数的回调
	 * @param @param resultObj
	 * @return void
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String randomNumber = (String) biiResponseBody.getResult();
		GatherInitiativeData.getInstance().setRandomNumber(randomNumber);

		Intent intent = new Intent(this, GatherConfirmActivity.class);
		intent.putExtra(GatherInitiative.PAYEE_NAME, nameStr);
		intent.putExtra(GatherInitiative.CURRENCY, currencyStr);
		intent.putExtra(GatherInitiative.TO_ACCOUNTID, accountId);
		intent.putExtra(Comm.ACCOUNTNUMBER, payeeActnoStr);
		intent.putExtra(GatherInitiative.PAYEE_MOBILE, payeePhoneStr);
		intent.putExtra(GatherInitiative.NOTIFY_PAYEE_AMOUNT, gatherAmountStr);
		intent.putExtra(GatherInitiative.PAYER_NAME, payerNameStr);
		intent.putExtra(GatherInitiative.PAYER_CHANNEL, payerChanelStr);
		intent.putExtra(GatherInitiative.PAYER_MOBILE, payerPhoneStr);
		intent.putExtra(GatherInitiative.REMARK, remarkStr);
		intent.putExtra(GatherInitiative.PAYER_CUST_ID, payerCustIdStr);
		intent.putExtra(GatherBaseActivity.IS_NEED_CHOOSE_ACCOUNT, isNeedChooseAccount);
		startActivityForResult(intent, 101);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 101 && resultCode == RESULT_OK) {
			setResult(RESULT_OK);
			finish();
		}
	}
	
}
