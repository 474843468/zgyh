package com.chinamworld.bocmbci.biz.gatherinitiative.creatgather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
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
import com.chinamworld.bocmbci.biz.gatherinitiative.GatherinitiativeConstant.GatherChannelType;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class CreatGatherInputInfoActivity extends GatherBaseActivity {

	/** 付款人类型 1：WEB渠道 2：手机渠道 */
	private static final String WEB_CHANNEL = "1";
	private static final String MOBILE_CHANNEL = "2";

	private static final int REQUEST_GATHER_CONFIRM_CODE = 110;
	/** 收款人姓名 */
	private TextView mPayeeNameView;
	/** 收款币种 */
	private TextView mPayeeCurrencyView;
	/** 收款人手机号 */
	private EditText mPayeeMobileView;
	/** 收款人账户 */
	private Spinner mPayeeAccountNumView;
	/** 收款金额 */
	private EditText mPayeeAmountView;
	/** 收款备注 */
	private EditText mPayeeRemarkView;
	/** 付款人编辑布局 */
	private View mPayerEditLayout;
	/** 付款人类型选择 */
	private RadioGroup mPayerRadioGroup;
	/** 付款人姓名 */
	private EditText mPayerNameView;
	/** 付款人手机号 */
	private EditText mPayerPhoneView;
	/** 付款人客户号 */
	private EditText mPayerLineBankView;
	/** 付款人布局 */
	private View mPayerLayout;
	/** 付款人类型 Text */
	private TextView mPayerTypeTextView;
	/** 付款人姓名 Text */
	private TextView mPayerNameTextView;
	/** 付款人手机号 Text */
	private TextView mPayerPhoneTextView;
	/** 付款人客户号 Text */
	private TextView mPayerLineTextView;

	/** 付款人客户号布局 */
	private LinearLayout mPayerLayoutLineBankView;
	/** 下一步按钮 */
	private Button mNextView;
	/** accountId集合 */
	private ArrayList<String> accountIdList;
	/** accountNumber集合 */
	private ArrayList<String> accountNoList;
	private String conversationId;
	private List<Map<String, Object>> combineList;
	private boolean isNeedSmc;
	private boolean isNeedOtp;
	/** 币种 */
	private String mCurrencyParam;
	private String mPayeeNameParam;
	private String mPayeeMobileParam;
	private String mPayeeAccountIdParam;
	private String mPayeeAccountNumParam;
	private String mPayeeAmountParam;
	private String mPayeeRemarkParam;

	private String mPayerNameParam;
	private String mPayerMobileParam;
	private String mPayerChanelParam = MOBILE_CHANNEL;
	private String mPayerLineBankNumParam;

	/** 是否可选择，处理从账户管理进入，帐号不可在选择 */
	private boolean mIsNeedChooseAccountParam;
	/** 付款人信息是否可以编辑，处理从常用收款人进入，付款人不可编辑 */
	private boolean mIsPayerEdit = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.creat_new_gather);
		View view = LayoutInflater.from(this).inflate(R.layout.gather_creat_input, null);
		tabcontent.addView(view);

		accountIdList = new ArrayList<String>();
		accountNoList = new ArrayList<String>();

		Intent intent = getIntent();
		mPayeeNameParam = (String) ((Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA)).get(Inves.CUSTOMERNAME);
		mPayeeMobileParam = (String) ((Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA)).get(Login.LOGIN_NAME);

		mIsNeedChooseAccountParam = intent.getBooleanExtra(IS_NEED_CHOOSE_ACCOUNT, true);
		mPayerChanelParam = getString(intent.getStringExtra(GatherInitiative.PAYER_CHANNEL), MOBILE_CHANNEL);
		mPayerMobileParam = getString(intent.getStringExtra(GatherInitiative.PAYER_MOBILE), "");
		mPayerNameParam = getString(intent.getStringExtra(GatherInitiative.PAYER_NAME), "");
		mPayerLineBankNumParam = getString(intent.getStringExtra(GatherInitiative.PAYER_CUST_ID), "");

		if (!StringUtil.isNull(mPayerNameParam) && !StringUtil.isNull(mPayerMobileParam)) {
			mIsPayerEdit = false;
		}

		initView();

		if (mIsNeedChooseAccountParam) {
			BaseHttpEngine.showProgressDialogCanGoBack();
			requestGatherAccountList();
		} else {
			setAccountSpinnerView();
			mPayeeAccountNumView.setClickable(false);
			mPayeeAccountNumView.setBackgroundResource(R.drawable.bg_spinner_default);
		}
	}

	// ----------------------------------------------------------------------------------------
	// request

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		conversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		// 请求安全因子
		requestGetSecurityFactor("PB037");
	}

	/**
	 * 请求"主动收款预交易"接口
	 */
	private void requestPsnTransActCollectionVerify() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(GatherInitiative.PSN_TRANS_ACT_COLLECTION_VERIFY);
		biiRequestBody.setConversationId(conversationId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(GatherInitiative.TO_ACCOUNTID, mPayeeAccountIdParam);
		map.put(GatherInitiative.PAYEE_NAME, mPayeeNameParam);
		map.put(GatherInitiative.CURRENCY, mCurrencyParam);
		map.put(GatherInitiative.NOTIFY_PAYEE_AMOUNT, mPayeeAmountParam);
		map.put(GatherInitiative.PAYER_CUST_ID, mPayerLineBankNumParam);
		map.put(GatherInitiative.REMARK, mPayeeRemarkParam);
		map.put(GatherInitiative.PAYER_MOBILE, mPayerMobileParam);
		map.put(GatherInitiative.PAYER_NAME, mPayerNameParam);
		map.put(GatherInitiative.PAYEE_MOBILE, mPayeeMobileParam);
		map.put(GatherInitiative.PAYER_CHANNEL, mPayerChanelParam);
		map.put(GatherInitiative.COMBIN_ID, BaseDroidApp.getInstanse().getSecurityChoosed());
		map.put(GatherInitiative.PAYEE_ACT_NO, mPayeeAccountNumParam);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnTransActCollectionVerifyCallback");
	}

	/**
	 * 请求随机数
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
		intent.putExtra(GatherInitiative.PAYEE_NAME, mPayeeNameParam);
		intent.putExtra(GatherInitiative.CURRENCY, mCurrencyParam);
		intent.putExtra(GatherInitiative.TO_ACCOUNTID, mPayeeAccountIdParam);
		intent.putExtra(Comm.ACCOUNTNUMBER, mPayeeAccountNumParam);
		intent.putExtra(GatherInitiative.PAYEE_MOBILE, mPayeeMobileParam);
		intent.putExtra(GatherInitiative.NOTIFY_PAYEE_AMOUNT, mPayeeAmountParam);
		intent.putExtra(GatherInitiative.PAYER_NAME, mPayerNameParam);
		intent.putExtra(GatherInitiative.PAYER_CHANNEL, mPayerChanelParam);
		intent.putExtra(GatherInitiative.PAYER_MOBILE, mPayerMobileParam);
		intent.putExtra(GatherInitiative.REMARK, mPayeeRemarkParam);
		intent.putExtra(GatherInitiative.PAYER_CUST_ID, mPayerLineBankNumParam);
		intent.putExtra(GatherBaseActivity.IS_NEED_CHOOSE_ACCOUNT, mIsNeedChooseAccountParam);
		intent.putExtra("noNeedSavePayer", getIntent().getBooleanExtra("noNeedSavePayer",false));
		startActivityForResult(intent, REQUEST_GATHER_CONFIRM_CODE);
	}

	// ----------------------------------------------------------------------------------
	// callback
	/**
	 * 查询账户列表
	 */
	@Override
	public void communicationCallBack(int flag) {
		super.communicationCallBack(flag);
		if (flag == QUERY_GATHER_ACCOUNT_CALLBACK) {
			// 没有符合类型的收款账户
			if (StringUtil.isNullOrEmpty(GatherInitiativeData.getInstance().getQueryAcountCallBackList())) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getText(R.string.no_suitable_customer_account).toString());
				return;
			}

			setAccountSpinnerView();
		}
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
	public static Map<String, Object> result;
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

	// -------------------------------------------------------------------------------------
	public String getString(Object obj, String defaultValue) {
		if (StringUtil.isNullOrEmpty(obj))
			return defaultValue;
		else
			return String.valueOf(obj).trim();
	}

	// mothod
	private void excuseNext() {
		mPayeeMobileParam = mPayeeMobileView.getText().toString();
		mPayeeAccountNumParam = accountNoList.get(mPayeeAccountNumView.getSelectedItemPosition());
		mPayeeAccountIdParam = accountIdList.get(mPayeeAccountNumView.getSelectedItemPosition());
		mPayeeAmountParam = mPayeeAmountView.getText().toString().trim();
		mPayeeRemarkParam = mPayeeRemarkView.getText().toString();

		if (mIsPayerEdit) {
			mPayerNameParam = mPayerNameView.getText().toString();
			mPayerMobileParam = mPayerPhoneView.getText().toString();
			mPayerLineBankNumParam = mPayerLineBankView.getText().toString().trim();
		}

		// XXX 重复提交 请求conversationId
		BaseHttpEngine.showProgressDialog();
		requestCommConversationId();
	}

	private boolean checkSubmit() {
		if (StringUtil.isNullOrEmpty(mPayeeMobileView.getText().toString().trim())) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.please_input_payee_mobile));
			return false;
		}
		if (!mPayeeMobileView.getText().toString().trim().matches("^\\d{11}$")) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.please_input_right_payee_mobile));
			return false;
		}
		
		if (StringUtil.isNullOrEmpty(mPayeeAccountNumParam)) {
			// 收款帐号为空
			BaseDroidApp.getInstanse().showInfoMessageDialog(getText(R.string.payee_select_payee_account).toString());
			return false;
		}

		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		RegexpBean regAmount = new RegexpBean(this.getString(R.string.gather_money_ammount), mPayeeAmountView.getText()
				.toString().trim(), "amount");
		lists.add(regAmount);

		if (mIsPayerEdit) {
			RegexpBean regName = new RegexpBean(this.getString(R.string.payer_name), mPayerNameView.getText()
					.toString(), "payeeName_notEmpty");
			RegexpBean regBankNum = new RegexpBean(this.getString(R.string.payer_phone), mPayerPhoneView.getText()
					.toString(), "longMobile");
			lists.add(regName);
			lists.add(regBankNum);
		}

		if (!RegexpUtils.regexpDate(lists)) {
			return false;
		}
		
		String str_phone="";
		if (mIsPayerEdit) {
			str_phone=mPayerPhoneView.getText().toString().trim();
		}else {
			str_phone=mPayerPhoneTextView.getText().toString().trim();
		}
		if (!str_phone.matches("^\\d{11}$")) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.please_input_right_payee_mobile));
			return false;
		}

		if (mIsPayerEdit) {
			if (WEB_CHANNEL.equals(mPayerChanelParam)
					&& TextUtils.isEmpty(mPayerLineBankView.getText().toString().trim())) {
				// WEB渠道.客户号为空
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getText(R.string.please_input_customer_number).toString());
				return false;
			}
		}

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_GATHER_CONFIRM_CODE && resultCode == RESULT_OK) {
			setResult(RESULT_OK);
			finish();
		}
	}

	private void setAccountSpinnerView() {
		ArrayList<String> acountNumberFixedList = new ArrayList<String>();
		List<Map<String, Object>> payAcountCallBackList = GatherInitiativeData.getInstance()
				.getQueryAcountCallBackList();
		if (payAcountCallBackList != null) {
			for (int i = 0; i < payAcountCallBackList.size(); i++) {
				String accountId = (String) payAcountCallBackList.get(i).get(Comm.ACCOUNT_ID);
				String accountNO = (String) payAcountCallBackList.get(i).get(Comm.ACCOUNTNUMBER);
				accountIdList.add(accountId);
				accountNoList.add(accountNO);
				acountNumberFixedList.add(StringUtil.getForSixForString(String.valueOf(accountNO)));
			}
		}

		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, R.layout.custom_spinner_item,
				acountNumberFixedList);
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mPayeeAccountNumView.setAdapter(mAdapter);
	}

	private void initView() {
		mPayeeNameView = (TextView) findViewById(R.id.tv_gather_money_name);
		mPayeeCurrencyView = (TextView) this.findViewById(R.id.tv_gather_money_type);
		mPayeeMobileView = (EditText) this.findViewById(R.id.edit_payee_mobile);
		mPayeeAccountNumView = (Spinner) this.findViewById(R.id.account_number_spinner);
		mPayeeAmountView = (EditText) this.findViewById(R.id.edit_gather_money_ammount);
		mPayeeRemarkView = (EditText) this.findViewById(R.id.edit_beizhu);

		mPayerEditLayout = findViewById(R.id.payer_edit_layout);
		mPayerRadioGroup = (RadioGroup) this.findViewById(R.id.payer_type_radiogroup);
		mPayerNameView = (EditText) this.findViewById(R.id.edit_payer_name);
		mPayerPhoneView = (EditText) this.findViewById(R.id.edit_payer_phone);
		mPayerLineBankView = (EditText) this.findViewById(R.id.edit_payer_customer_number);
		mNextView = (Button) this.findViewById(R.id.creat_gather_input_next_btn);
		mPayerLayoutLineBankView = (LinearLayout) this.findViewById(R.id.layout_line_bank_number);

		mPayerLayout = findViewById(R.id.payer_layout);
		mPayerTypeTextView = (TextView) findViewById(R.id.tv_payer_type);
		mPayerNameTextView = (TextView) findViewById(R.id.tv_payer_name);
		mPayerPhoneTextView = (TextView) findViewById(R.id.tv_payer_phone);
		mPayerLineTextView = (TextView) findViewById(R.id.tv_payer_line);
		// setAccountSpinnerView(new ArrayList<String>());

		PopupWindowUtils pop = PopupWindowUtils.getInstance();
		pop.setOnShowAllTextListener(this, (TextView) findViewById(R.id.edit_payee_mobile_table));
		pop.setOnShowAllTextListener(this, (TextView) findViewById(R.id.tv_payer_phone_table));
		pop.setOnShowAllTextListener(this, (TextView) findViewById(R.id.tv_payer_line_table));

		mCurrencyParam = "001";

		mPayeeNameView.setText(mPayeeNameParam);
		mPayeeMobileView.setText(mPayeeMobileParam);
		mPayeeCurrencyView.setText(R.string.rmb_currency);

		EditTextUtils.setLengthMatcher(this, mPayerNameView, 60);
		EditTextUtils.setLengthMatcher(this, mPayeeRemarkView, 20);// 主动收款附言改为20字符

		if (mIsPayerEdit) {
			mPayerEditLayout.setVisibility(View.VISIBLE);
			mPayerLayout.setVisibility(View.GONE);

			// 付款人可编辑
			mPayerNameView.setText(mPayerNameParam);
			mPayerPhoneView.setText(mPayerMobileParam);
			mPayerLineBankView.setText(mPayerLineBankNumParam);
			if (WEB_CHANNEL.equals(mPayerChanelParam)) {
				RadioButton rb = (RadioButton) findViewById(R.id.rb_user_from_line_bank);
				rb.setChecked(true);
			} else if (MOBILE_CHANNEL.equals(mPayerChanelParam)) {
				RadioButton rb = (RadioButton) findViewById(R.id.rb_user_from_phone_bank);
				rb.setChecked(true);
			}

			mPayerRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup arg0, int id) {
					switch (id) {
					case R.id.rb_user_from_line_bank:
						mPayerLayoutLineBankView.setVisibility(View.VISIBLE);
						mPayerChanelParam = WEB_CHANNEL;
						mPayerLineBankView.setText("");
						mPayerLineBankNumParam = "";
						break;

					case R.id.rb_user_from_phone_bank:
						mPayerLayoutLineBankView.setVisibility(View.GONE);
						mPayerChanelParam = MOBILE_CHANNEL;
						mPayerLineBankView.setText("");
						mPayerLineBankNumParam = "";
						break;
					}
				}
			});

		} else {
			// 付款人不可编辑
			mPayerEditLayout.setVisibility(View.GONE);
			mPayerLayout.setVisibility(View.VISIBLE);

			mPayerNameTextView.setText(mPayerNameParam);
			mPayerPhoneTextView.setText(mPayerMobileParam);
			mPayerLineTextView.setText(mPayerLineBankNumParam);
			if (StringUtil.isNullOrEmpty(mPayerLineBankNumParam)) {
				findViewById(R.id.tv_payer_line_table).setVisibility(View.GONE);
				findViewById(R.id.tv_payer_line).setVisibility(View.GONE);
				mPayerTypeTextView.setText(GatherChannelType.getTransferWayTypeStr(GatherChannelType.MOBILE_CHANNEL));
			} else {
				findViewById(R.id.tv_payer_line_table).setVisibility(View.VISIBLE);
				findViewById(R.id.tv_payer_line).setVisibility(View.VISIBLE);
				mPayerTypeTextView.setText(GatherChannelType.getTransferWayTypeStr(GatherChannelType.WEB_CHANNEL));
			}
		}

		mNextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (checkSubmit()) {
					excuseNext();
				}
			}
		});
		mPayeeAccountNumView.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				mPayeeAccountIdParam = accountIdList.get(position);
				mPayeeAccountNumParam = accountNoList.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				mPayeeAccountIdParam = accountIdList.get(0);
				mPayeeAccountNumParam = accountNoList.get(0);
			}
		});
	}

}
