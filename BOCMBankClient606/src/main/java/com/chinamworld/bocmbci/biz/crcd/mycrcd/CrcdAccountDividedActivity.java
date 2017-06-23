package com.chinamworld.bocmbci.biz.crcd.mycrcd;

import java.util.ArrayList;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.transquery.CrcdHasQueryDetailActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.transquery.CrcdHasQueryListActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 账单分期------输入信息页面
 * 
 * @author huangyuchao
 */
public class CrcdAccountDividedActivity extends CrcdBaseActivity {

	/** 交易分期 */
	private View view;
	Button trade_nextButton;

	TextView tv_card_type, tv_card_number, tv_card_step, mycrcd_accounted_type, mycrcd_selected_creditcard,
			mycrcd_accounted_money;

	Spinner forex_rate_currency_buylCode, forex_rate_currency_type;

	// 拟分期数的数组
	String[] numArray = new String[] { "3", "6", "9", "12", "18", "24" };
	// 拟分期数的收取方式
	// String[] typeArray = new String[] { "一次性收取" };

	protected static int position;
	/** 用户输入的分期金额 */
	protected static String money;

	EditText mycrcd_et_accounted_money;

	protected static String strCurrencyCode;

	double maxMoney, minMoney, currentMoney;

	String fromHasQuery;
	/** 账号 */
	public static String accountNum;
	/** 信用卡后四位 */
	public static String cardFour;
	/** 安全因子Id */
	protected String combinId = "";
	/** 用户选择的分期期数 */
	protected static String dividedNum;
	/** 手续费收取方式上传值 */
	protected static String dividedType = "0";
	/** 上限 */
	protected static String upMoney;
	/** 下限 */
	protected static String lowMoney;
	/** 手续费收取方式-----一次性收取 */
	protected static String strDividedType = "";
	/** 网银账户标志 */
	public static String accountId;
	/** 账单分期确认回调----返回结果 */
	public static Map<String, Object> result;
	private TextView upText = null;
	private TextView lowText = null;
	private TextView moneyText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_account_divide));
		view = addView(R.layout.crcd_trans_account_divided);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
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
	private void init() {

		fromHasQuery = this.getIntent().getStringExtra("fromHasQuery");

		if ("fromHasQuery".equals(fromHasQuery)) {
			// 账单分期
			accountId = CrcdHasQueryListActivity.accountId;
			// it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES,
			// MyCrcdDetailActivity.bankNumber);
			accountNum = CrcdHasQueryListActivity.accountNumber;

		} else {
			// 已出账单查询详情页面
			accountId = MyCrcdDetailActivity.accountId;
			// it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES,
			// MyCrcdDetailActivity.bankNumber);
			accountNum = MyCrcdDetailActivity.bankNumber;
		}
		LogGloble.d("info----", accountNum);
		cardFour = accountNum.substring(accountNum.length() - 4, accountNum.length());

		mycrcd_et_accounted_money = (EditText) view.findViewById(R.id.mycrcd_et_accounted_money);

		trade_nextButton = (Button) view.findViewById(R.id.trade_nextButton);

		strDividedType = this.getString(R.string.mycrcd_one_shouqu);

		tv_card_type = (TextView) view.findViewById(R.id.tv_card_type);
		tv_card_number = (TextView) view.findViewById(R.id.tv_card_number);
		tv_card_step = (TextView) view.findViewById(R.id.tv_card_step);

		upText = (TextView) findViewById(R.id.up_text);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, upText);
		lowText = (TextView) findViewById(R.id.low_text);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, lowText);
		moneyText = (TextView) findViewById(R.id.moneyText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, moneyText);
		tv_card_type.setText(String.valueOf(currentBankList.get(Crcd.CRCD_ACCOUNTNAME_RES)));
		// tv_card_number.setText(StringUtil.getForSixForString(String.valueOf(currentBankList.get(Crcd.CRCD_ACCOUNTNUMBER_RES))));

		tv_card_number.setText(StringUtil.getForSixForString(String.valueOf(accountNum)));
		tv_card_step.setText(String.valueOf(currentBankList.get(Crcd.CRCD_NICKNAME_RES)));
		// 分期币种
		mycrcd_accounted_type = (TextView) view.findViewById(R.id.mycrcd_accounted_type);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mycrcd_accounted_type);
		// 上限
		mycrcd_selected_creditcard = (TextView) view.findViewById(R.id.mycrcd_selected_creditcard);
		// 下限
		mycrcd_accounted_money = (TextView) view.findViewById(R.id.mycrcd_accounted_money);
		// 已出账单查询详情页面
		if (!StringUtil.isNullOrEmpty(CrcdTransDetailActivity.upMoney)) {
			upMoney = CrcdTransDetailActivity.upMoney;
			mycrcd_selected_creditcard.setText(StringUtil.parseStringPattern(CrcdTransDetailActivity.upMoney, 2));
		}
		// 已出账单查询详情页面
		if (!StringUtil.isNullOrEmpty(CrcdTransDetailActivity.lowMoney)) {
			lowMoney = CrcdTransDetailActivity.lowMoney;
			mycrcd_accounted_money.setText(StringUtil.parseStringPattern(CrcdTransDetailActivity.lowMoney, 2));
		}
		// 账单查询
		if (!StringUtil.isNullOrEmpty(CrcdHasQueryDetailActivity.upMoney)) {
			upMoney = CrcdHasQueryDetailActivity.upMoney;
			mycrcd_selected_creditcard.setText(StringUtil.parseStringPattern(upMoney, 2));
		}
		// 账单查询
		if (!StringUtil.isNullOrEmpty(CrcdHasQueryDetailActivity.lowMoney)) {
			lowMoney = CrcdHasQueryDetailActivity.lowMoney;
			mycrcd_accounted_money.setText(StringUtil.parseStringPattern(CrcdHasQueryDetailActivity.lowMoney, 2));
		}

		strCurrencyCode = strCurrency;
		mycrcd_accounted_type.setText(strCurrencyCode);
		// 分期期数
		forex_rate_currency_buylCode = (Spinner) view.findViewById(R.id.forex_rate_currency_buylCode);
		// 收费方式
		forex_rate_currency_type = (Spinner) view.findViewById(R.id.forex_rate_currency_type);
		ArrayAdapter<String> numAdapter = new ArrayAdapter<String>(this, R.layout.custom_spinner_item, numArray);
		numAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		forex_rate_currency_buylCode.setAdapter(numAdapter);
		forex_rate_currency_buylCode.setSelection(0);
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getResources().getString(R.string.mycrcd_bill_divide_setup),
						this.getResources().getString(R.string.mycrcd_divide_confirm),
						this.getResources().getString(R.string.mycrcd_divide_setup_success) });
		StepTitleUtils.getInstance().setTitleStep(1);

		forex_rate_currency_buylCode.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		forex_rate_currency_type.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		trade_nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dividedNum = forex_rate_currency_buylCode.getSelectedItem().toString();
				money = mycrcd_et_accounted_money.getText().toString();

				// 验证
				RegexpBean reb1 = new RegexpBean(CrcdAccountDividedActivity.this
						.getString(R.string.mycrcd_ni_account_fenqi_money), money, "dividedAmount");
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(reb1);
				if (RegexpUtils.regexpDate(lists)) {
					currentMoney = Double.valueOf(money);
					maxMoney = Double.valueOf(upMoney);
					minMoney = Double.valueOf(lowMoney);

					if (currentMoney < minMoney) {
						BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.mycrcd_not_xiaoyu));
						return;
					}
					if (currentMoney > maxMoney) {
						BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.mycrcd_not_dayu));
						return;
					}
					requestCommConversationId();
					BaseHttpEngine.showProgressDialog();
				}
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
				// 办理账单分期确认
				psnCrcdDividedPayBillConfirm();
			}
		});
	}

	/** 分期账单确认 */
	public void psnCrcdDividedPayBillConfirm() {
		// 通讯开始,展示通讯框
		// BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setMethod(Crcd.CRCD_PSNCrcdCIVIDEDPAYBILLSETCONFIRM_API);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		map.put(Crcd.CRCD_CURRENCYCODE, currency);
		map.put(Crcd.CRCD_AMOUNT, mycrcd_et_accounted_money.getText().toString());
		map.put(Crcd.CRCD_DIVPERIOD, forex_rate_currency_buylCode.getSelectedItem().toString());
		map.put(Crcd.CRCD_CHARGEMODE, dividedType);
		map.put(Crcd.CRCD_CRCDFINALFOUR, cardFour);
		map.put(Crcd.CRCD_LOWAMOUNT, lowMoney);
		map.put(Crcd.CRCD_COMBINID, BaseDroidApp.getInstanse().getSecurityChoosed());
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdDividedPayBillSetConfirmCallBack");
	}

	/** 账单分期确认---回调 */
	public void psnCrcdDividedPayBillSetConfirmCallBack(Object resultObj) {
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		result = (Map<String, Object>) biiResponseBody.getResult();

		Intent it = new Intent(CrcdAccountDividedActivity.this, CrcdAccountDividedConfirmActivity.class);
		startActivity(it);
	}

}
