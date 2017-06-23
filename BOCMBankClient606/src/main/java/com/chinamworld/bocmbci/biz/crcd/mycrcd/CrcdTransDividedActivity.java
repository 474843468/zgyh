package com.chinamworld.bocmbci.biz.crcd.mycrcd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
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
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 消费分期-----填写信息页面
 * 
 * @author huangyuchao
 * 
 */
public class CrcdTransDividedActivity extends CrcdBaseActivity {
	private static final String TAG = "CrcdTransDividedActivity";
	/** 交易分期 */
	private View view;
	Button trade_nextButton;

	TextView tv_card_type, tv_card_number, tv_card_step, mycrcd_accounted_type, mycrcd_selected_creditcard,
			mycrcd_accounted_money;

	Spinner forex_rate_currency_buylCode, forex_rate_currency_type;

	// 拟分期数的数组
	String[] numArray = new String[] { "3", "6", "9", "12", "18", "24" };
	// 拟分期数的收取方式
	// String[] typeArray = new String[] {
	// getString(R.string.mycrcd_one_shouqu)};
	/** 消费分期查询返回结果 */
	protected static List<Map<String, Object>> transList;

	protected static int position;
	/** 币种代码 */
	protected static String currency;
	/** 分期金额 */
	protected static String money;
	/** 币种名称 */
	static String strCurrencyCode;
	/** 信用卡卡号后四位 */
	static String acctNumTail;

	String transId;
	String mainAcctId;
	String sequence;
	/** 分期期数 */
	protected static String dividedNum;
	/** 手续费收取方式上传值 */
	protected static String dividedType = "0";
	/** 收费方式名称 */
	protected static String strDividedType = "";
	/** 办理消费分期确认---返回结果 */
	protected Map<String, Object> result;
	private TextView moneyText = null;
	/** 币种代码 */
	private String codeCode = null;
	/** 账号 */
	private String bankNumber = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "OnCreate");
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_sub_divide));
		// 右上角按钮赋值
		view = addView(R.layout.crcd_trans_divided);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		bankNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		init();

	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	/** 初始化界面 */
	@SuppressWarnings("unchecked")
	private void init() {
		// CrcdXiaofeiQueryActivity页面数据
		position = this.getIntent().getIntExtra("position", 0);
		currency = this.getIntent().getStringExtra("currency");
		strDividedType = this.getString(R.string.mycrcd_one_shouqu);

		trade_nextButton = (Button) view.findViewById(R.id.trade_nextButton);

		moneyText = (TextView) findViewById(R.id.money_text);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, moneyText);

		tv_card_type = (TextView) view.findViewById(R.id.tv_card_type);
		tv_card_number = (TextView) view.findViewById(R.id.tv_card_number);
		tv_card_step = (TextView) view.findViewById(R.id.tv_card_step);

		tv_card_type.setText(String.valueOf(currentBankList.get(Crcd.CRCD_ACCOUNTNAME_RES)));
		tv_card_number.setText(StringUtil.getForSixForString(String.valueOf(currentBankList
				.get(Crcd.CRCD_ACCOUNTNUMBER_RES))));
		tv_card_step.setText(String.valueOf(currentBankList.get(Crcd.CRCD_NICKNAME_RES)));

		mycrcd_accounted_type = (TextView) view.findViewById(R.id.mycrcd_accounted_type);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mycrcd_accounted_type);
		// 币种
		mycrcd_selected_creditcard = (TextView) view.findViewById(R.id.mycrcd_selected_creditcard);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mycrcd_selected_creditcard);
		mycrcd_accounted_money = (TextView) view.findViewById(R.id.mycrcd_accounted_money);
		// 消费分期查询结果
		transList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CRCE_XIAOFEI_RESULT);

		transId = String.valueOf(transList.get(position).get(Crcd.CRCD_TRANSID));
		mainAcctId = String.valueOf(transList.get(position).get(Crcd.CRCD_MAINACCTID));
		sequence = String.valueOf(transList.get(position).get(Crcd.CRCD_SEQUENCE));

		mycrcd_accounted_type.setText(String.valueOf(transList.get(position).get(Crcd.CRCD_TRANSDESC)));
		codeCode = (String) transList.get(position).get(Crcd.CRCD_CLEARINGCURRENCY);
		strCurrencyCode = LocalData.Currency.get(String
				.valueOf(transList.get(position).get(Crcd.CRCD_CLEARINGCURRENCY)));

		mycrcd_selected_creditcard.setText(strCurrencyCode);
		money = String.valueOf(transList.get(position).get(Crcd.CRCD_TRANAMOUNT_REQ));

		mycrcd_accounted_money.setText(StringUtil.parseStringCodePattern(codeCode, money, 2));

		String acc = String.valueOf(transList.get(position).get(Crcd.CRCD_ACCTNUMTAIL));
		if (StringUtil.isNull(acc)) {
			if (!StringUtil.isNull(bankNumber)) {
				acctNumTail = bankNumber.substring(bankNumber.length() - 4, bankNumber.length());
			}

		} else {
			acctNumTail = String.valueOf(transList.get(position).get(Crcd.CRCD_ACCTNUMTAIL));
		}

		forex_rate_currency_buylCode = (Spinner) view.findViewById(R.id.forex_rate_currency_buylCode);

		forex_rate_currency_type = (Spinner) view.findViewById(R.id.forex_rate_currency_type);
		ArrayAdapter<String> numAdapter = new ArrayAdapter<String>(this, R.layout.custom_spinner_item, numArray);
		numAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		forex_rate_currency_buylCode.setAdapter(numAdapter);
		forex_rate_currency_buylCode.setSelection(0);

		// ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,
		// R.layout.custom_spinner_item, typeArray);
		// typeAdapter
		// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// forex_rate_currency_type.setAdapter(typeAdapter);
		// forex_rate_currency_type.setSelection(0);

		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getResources().getString(R.string.mycrcd_divide_setup),
						this.getResources().getString(R.string.mycrcd_divide_confirm),
						this.getResources().getString(R.string.mycrcd_divide_setup_success) });
		StepTitleUtils.getInstance().setTitleStep(1);

		forex_rate_currency_buylCode.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		forex_rate_currency_type.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		trade_nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dividedNum = forex_rate_currency_buylCode.getSelectedItem().toString();
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
		});

	}

	public void requestCommConversationIdCallBack(Object o) {
		super.requestCommConversationIdCallBack(o);

		// 请求安全因子组合id
		requestGetSecurityFactor(psnDividedsecurityId);
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);

		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 办理消费分期确认
				psnCrcdDividedPayConsumeConfirm();
			}
		});
	}

	/** 办理消费分期确认 */
	public void psnCrcdDividedPayConsumeConfirm() {
		// 通讯开始,展示通讯框
		// BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDDIVIDEPAYCONSUMECONFIRM_API);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTID_RES, String.valueOf(currentBankList.get(Crcd.CRCD_ACCOUNTID_RES)));
		params.put(Crcd.CRCD_CURRENCYCODE, currency);
		params.put(Crcd.CRCD_AMOUNT, money);

		// 消费分期要传数字类型
		params.put(Crcd.CRCD_DIVPERIOD, Integer.valueOf(dividedNum));
		params.put(Crcd.CRCD_CHARGEMODE, dividedType);
		// 安全因子
		params.put(Crcd.CRCD_COMBINID, BaseDroidApp.getInstanse().getSecurityChoosed());
		// String cardNumber =
		// String.valueOf(currentBankList.get(Crcd.CRCD_ACCOUNTNUMBER_RES));
		params.put(Crcd.CRCD_CRCDFINALFOUR, acctNumTail);

		params.put(Crcd.CRCD_TRANSID, transId);
		params.put(Crcd.CRCD_MAINACCTID, mainAcctId);
		params.put(Crcd.CRCD_SEQUENCE, sequence);

		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdDividedPayConsumeConfirmCallBack");
	}

	/** 消费分期确认-----回调 */
	public void psnCrcdDividedPayConsumeConfirmCallBack(Object resultObj) {
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.CRCE_XIAOFEI_CONFIRM_RESULT, result);
		Intent it = new Intent(CrcdTransDividedActivity.this, CrcdTransDividedConfirmActivity.class);
		startActivity(it);
	}
}
