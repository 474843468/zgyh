package com.chinamworld.bocmbci.biz.sbremit.mysbremit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.SBRemit;
import com.chinamworld.bocmbci.biz.sbremit.SBRemitBaseActivity;
import com.chinamworld.bocmbci.biz.sbremit.SBRemitDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 购汇 输入信息页 */
public class BRemitInputInfoActivity extends SBRemitBaseActivity implements
		OnClickListener, OnItemSelectedListener {
	private ImageView source_tv;
	/** 控件 账号 */
	private TextView bremit_acc;
	/** 控件 当前参考牌价 */
//	private TextView cur_reference_price;
	/** 控件 当前参考牌价布局 */
//	private LinearLayout cur_reference_price_layout;
	/** 控件 购汇币种 */
	private Spinner bremit_money_type;

	/** 控件 年已用额度 */
	private TextView year_have_used_limit;
	/** 控件 年剩余额度 */
	private TextView year_remain_limit;
	/** 控件 最大购汇金额试算 */
	private TextView largest_exchange;
	/** 控件 资金来源 */
	private Spinner capital_use;
	/** 控件 购汇金额 */
	private EditText bremit_money;
	/** 资金来源Adapter */
	private ArrayAdapter arrayAdapter;
	/** 账户标识 */
	private String acountId;
	/** 账户号码 */
	private String acountNumber;
	/** 币种信息 购汇币种 */
	private String currency;
	/** 币种信息 钞汇 */
	private String cashRemit;
	/** 已用额度 */
	private String useAmount;
	/** 剩余额度 */
	private String remainAmount;
	/** 重点关注对象 */
	private String important_focus;
	/** 数据表单 */
	private String dataTable;
	/** 交易随机值 */
	private String transRandom;
	/** 查询结购汇当前参考牌价 */
	private Map<String, Object> exchangeRate;
	/** 控件 资金用途 */
	private String capitaUseStr;
	/** 控件 资金用途id */
	private String capitaUseId;
	/** 购汇金额 */
	private String bremitMoney;
	/** 控件 下一步 */
	private Button next;
	private boolean come_from_rate_info;
	/** 交易主题姓名 */
	private String custName;
	/** 证件号码 */
	private String identityNumber;
	/** 确认书签署状态 */
	private String signStatus;
	/** 确认书签署发布日期 */
	private String pubDate;
	/** 确认书签署到期日期 */
	private String endDate;
	/** 人民币余额 */
	private String availableBalanceRMB;
	/** 个人可结售汇金额折成美元 */
	private String annRmeAmtUSD;
	/** 最大金额试算的Layout */
	private LinearLayout largest_exchange_layout;
	/** 账户可用余额 */
	private String availableBalance;
	private static final int REQUEST_SOURCE_PROTOCOL_CODE = 10003;
	/** 现汇 */
	private RadioButton spot_exchange;
	/** 现钞 */
	private RadioButton cash_exchange;
	private LinearLayout message;
	private LinearLayout message_two;
	private RadioGroup rg_accopentype;
	private  String sbtypeString = "11";

	/** 币种类型 */
	// private String moneyType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.sbremit_trade_type_bremit));// 为界面标题赋值
		addView(R.layout.sbremit_br_input_info);// 添加布局
		// capitaUseStr = capitalUseList.get(0); // 修改 luqp 2015年11月16日19:32:24
		// capitaUseId = capitalUseValueKey.get(capitaUseStr); // 修改 luqp
		// 2015年11月16日19:32:24
		trymapMap = SBRemitDataCenter.getInstance().getTryMap();
		initViews();
		BaseHttpEngine.showProgressDialog();
		initParamsInfo();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			setResult(RESULT_CANCELED);
			finish();
			break;
		case R.id.ib_top_right_btn:// 返回主页
			ActivityTaskManager.getInstance().removeAllActivity();
			break;
		case R.id.next:
			if (currency.equals("-1")) { // 资金来源如果是"请选择"点击下一步提示用户选择资金来源.
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						BRemitInputInfoActivity.this.getResources().getString(
								R.string.remit_please_choose_curry));
				return;
			}
			//606新增，判断是否选中钞会类型
			if(!(spot_exchange.isChecked()||cash_exchange.isChecked())){
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						"请选择钞汇类型"
						);
				return;
			}


			if (validation()) {
				// add by luqp 如果用户没有选择购汇币种提示用户请选择! 2015年11月16日17:16:50

				// add by luqp 如果用户没有选择资金来源 提示用户请选择! 2015年11月16日17:16:50
				if (capitaUseId.equals("-1")) { // 资金来源如果是"请选择"点击下一步提示用户选择资金来源.
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							BRemitInputInfoActivity.this.getResources()
									.getString(R.string.remit_please_choose));
					return;
				}

				// 查询结购汇当前参考牌价 跳转至确认页面
				BaseHttpEngine.showProgressDialog();
				requestForExchangeRate();
			}
			break;
		case R.id.source_instruction: // 资金来源说明
			Intent intent = new Intent(this, BRemitSourceProtocolActivity.class);
			startActivityForResult(intent, REQUEST_SOURCE_PROTOCOL_CODE);
			// overridePendingTransition(R.anim.n_pop_enter_bottom_up,
			// R.anim.no_animation);
			break;
		default:
			break;
		}
	}

	private boolean validation() {
		bremitMoney = bremit_money.getText().toString();
		RegexpBean regAmount = null;
		if (!LocalData.codeNoNumber.contains(currency)) {// 判断是否是日元，日元用日元的校验规则
			regAmount = new RegexpBean(
					getString(R.string.bremit_money_no_colon), bremitMoney,
					"amount");
		} else {
			if (LocalData.codeNoNumber.get(0).equals(currency)) { // 日元(currency
																	// = "027")
				regAmount = new RegexpBean(
						getString(R.string.bremit_money_no_colon), bremitMoney,
						"spetialAmountJPY");
			} else if (LocalData.codeNoNumber.get(2).equals(currency)) {// 韩元(currency
																		// =
																		// "088")
				regAmount = new RegexpBean(
						getString(R.string.bremit_money_no_colon), bremitMoney,
						"spetialAmountKRW");
			} else {
				regAmount = new RegexpBean(
						getString(R.string.bremit_money_no_colon), bremitMoney,
						"spetialAmount");
			}
		}
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		lists.add(regAmount);
		if (!RegexpUtils.regexpDate(lists)) {// 校验不通过
			return false;
		}
		return true;
	}

	private void initViews() {
		back.setOnClickListener(this);
		btn_right.setOnClickListener(this);
		spot_exchange = (RadioButton) findViewById(R.id.spot_exchange);
		cash_exchange = (RadioButton) findViewById(R.id.cash_exchange);
		message=(LinearLayout) findViewById(R.id.message);
		message_two=(LinearLayout)findViewById(R.id.message2) ;
		bremit_acc = (TextView) findViewById(R.id.bremit_acc);
		bremit_money_type = (Spinner) findViewById(R.id.bremit_money_type);
		bremit_money_type.setOnItemSelectedListener(this);
		bremit_money_type.setSelection(0);
		rg_accopentype=(RadioGroup)findViewById(R.id.rg_accopentype);
		// ------给币种Spinners添加适配器 start
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.epay_spinner, moneyTypeList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bremit_money_type.setAdapter(adapter);
		// ------给币种Spinners添加适配器 end
		// bill_mark = (TextView) findViewById(R.id.bill_mark);

		year_have_used_limit = (TextView) findViewById(R.id.year_have_used_limit);
		year_remain_limit = (TextView) findViewById(R.id.year_remain_limit);
		
		largest_exchange_layout = (LinearLayout) findViewById(R.id.largest_exchange_layout);
		largest_exchange = (TextView) findViewById(R.id.largest_exchange);
//		cur_reference_price_layout=(LinearLayout) findViewById(R.id.cur_reference_price_layout);
//		cur_reference_price=(TextView) findViewById(R.id.cur_reference_price);
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				largest_exchange);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.year_have_used_limit_alert));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.year_remain_limit_alert));
		capital_use = (Spinner) findViewById(R.id.capital_use);
		capital_use.setOnItemSelectedListener(this);
		// ------给资金用途Spinners添加适配器 start
		arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,
				capitalUseList);
		arrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		capital_use.setAdapter(arrayAdapter);
		capital_use.setSelection(0); // add by luqp 默认选择第一个 2015年11月16日19:25:45

		// ------给资金用途Spinners添加适配器 end
		bremit_money = (EditText) findViewById(R.id.bremit_money);
		next = (Button) findViewById(R.id.next);
		next.setOnClickListener(this);
		source_tv = (ImageView) findViewById(R.id.source_instruction);
		source_tv.setOnClickListener(this);
		rg_accopentype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId)
				{
					case R.id.spot_exchange:
						cashRemit="02";
						//标识进来未选币种先选钞汇类型，不调用最大金额试算接口
						if(!currency.equals("-1")){
							message.setVisibility(View.VISIBLE);
							message_two.setVisibility(View.GONE);
							requestbiggesttry(sbtypeString, cashRemit, availableBalanceRMB,
									currency, annRmeAmtUSD);
						}else{
							//
							spot_exchange.setChecked(false);
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									BRemitInputInfoActivity.this.getResources().getString(
											R.string.remit_please_choose_curry));
							return;
						}
						break;
					case R.id.cash_exchange:
						cashRemit="01";
						//标识进来未选币种先选钞汇类型，不调用最大金额试算接口
						if(!currency.equals("-1")){
							message.setVisibility(View.GONE);
							message_two.setVisibility(View.VISIBLE);
							requestbiggesttry(sbtypeString, cashRemit, availableBalanceRMB,
									currency, annRmeAmtUSD);
						}else{
							//
							cash_exchange.setChecked(false);
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									BRemitInputInfoActivity.this.getResources().getString(
											R.string.remit_please_choose_curry));
							return;
						}


						break;
				}



			}
		});

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		switch (arg0.getId()) {
		case R.id.bremit_money_type:
			currency = moneyTypeValueKey.get(moneyTypeList.get(arg2));

			if (currency.equals("-1")) {
				cash_exchange.setChecked(false);//默认 选择
				spot_exchange.setChecked(false);
			} else {
				if (moneyTypetwo.contains(currency)) {
					spot_exchange.setTextColor(Color.BLACK);
					rg_accopentype.clearCheck();
//					cash_exchange.setEnabled(true);
					spot_exchange.setEnabled(true);
					if(cash_exchange.isChecked()){
						cashRemit="01";//现钞
					}else if(spot_exchange.isChecked()){
						cashRemit="02";//现汇
					}
					message.setVisibility(View.VISIBLE);
					message_two.setVisibility(View.GONE);
				} else if(moneyTypeone.contains(currency)){
//					cash_exchange.setChecked(true);//默认选择
					spot_exchange.setEnabled(false);//不能点
					rg_accopentype.check(R.id.cash_exchange);
					spot_exchange.setTextColor(getResources().getColor(R.color.gray));
					message.setVisibility(View.GONE);
					message_two.setVisibility(View.VISIBLE);
					
					cashRemit="01";//现钞
				}

				PopupWindowUtils
						.getInstance()
						.setOnShowAllTextListener(
								this,
								(TextView) findViewById(R.id.largest_exchange_calculate));
			
				if(spot_exchange.isChecked()||cash_exchange.isChecked()){
					requestbiggesttry(sbtypeString, cashRemit, availableBalanceRMB,
							currency, annRmeAmtUSD);
				}
//				requestForExchangeRate();
			}

			
			break;
		case R.id.capital_use:
			capitaUseStr = capitalUseList.get(arg2);
			capitaUseId = capitalUseValueKey.get(capitaUseStr);
			break;
		default:
			break;
		}
	}

	public void requestbiggesttryCallBack(Object resultObj) {
		super.requestbiggesttryCallBack(resultObj);
		largest_exchange_layout.setVisibility(View.VISIBLE);
		String availableBalanceCUR = (String) SBRemitDataCenter.getInstance()
				.getTryMap().get("availableBalanceCUR");
		String annRmeAmtCUR = (String) SBRemitDataCenter.getInstance()
				.getTryMap().get("annRmeAmtCUR");
		if (Double.parseDouble(availableBalanceCUR) < Double
				.parseDouble(annRmeAmtCUR)) {
			String availableBalanceRMB_new = StringUtil.parseStringCodePattern(
					currency, availableBalanceCUR,
					SBRemitDataCenter.getInstance().CASH_PATTERN_PARAM);
			largest_exchange.setText(availableBalanceRMB_new);

		} else {

			String annRmeAmtUSD_new = StringUtil.parseStringCodePattern(
					currency, annRmeAmtCUR,
					SBRemitDataCenter.getInstance().CASH_PATTERN_PARAM);

			largest_exchange.setText(annRmeAmtUSD_new);
		}
		if (StringUtil.isNullOrEmpty(trymapMap)) {
			return;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * 接收调用页的信息，信息完整则初始本页面
	 */
	private void initParamsInfo() {
		Intent intent = getIntent();
		Map<String, Object> accInfo = SBRemitDataCenter.getInstance()
				.getAccInfo();
		acountId = (String) accInfo.get(Comm.ACCOUNT_ID);
		acountNumber = (String) accInfo.get(Comm.ACCOUNTNUMBER);
		come_from_rate_info = intent.getBooleanExtra(
				SBRemit.COME_FROM_RATE_INFO, false);
		currency = intent.getStringExtra(SBRemit.CURRENCY);
//		cashRemit = intent.getStringExtra(SBRemit.CASH_REMIT);
		dataTable = intent.getStringExtra(SBRemit.DATA_TABLE);
		transRandom = intent.getStringExtra(SBRemit.TRANS_RANDOM);
		// useAmount = intent.getStringExtra(SBRemit.USED_AMOUNT);
		//
		useAmount = (String) SBRemitDataCenter.getInstance().getResultAmount()
				.get("annAmtUSD");
		useAmount = StringUtil.parseStringCodePattern(currency, useAmount,
				SBRemitDataCenter.getInstance().CASH_PATTERN_PARAM);
		// remainAmount = intent.getStringExtra(SBRemit.REMAIN_AMOUNT);
		remainAmount = (String) SBRemitDataCenter.getInstance()
				.getResultAmount().get("annRmeAmtUSD");
		remainAmount = StringUtil.parseStringCodePattern(currency,
				remainAmount,
				SBRemitDataCenter.getInstance().CASH_PATTERN_PARAM);
		identityNumber = intent.getStringExtra(SBRemit.IDENTITYNUMBER);
		signStatus = intent.getStringExtra(SBRemit.SIGNSTATUS);
		custName = intent.getStringExtra(SBRemit.CUSTNAME);
		pubDate = intent.getStringExtra(SBRemit.PUBDATE);
		endDate = intent.getStringExtra(SBRemit.END_DATE);
		important_focus = intent.getStringExtra(SBRemit.IMPORTANT_FOCUS);
		availableBalanceRMB = intent.getStringExtra(SBRemit.AVAILABLE_BALANCE);
		availableBalanceRMB = StringUtil
				.append2Decimals(availableBalanceRMB, 2);
		annRmeAmtUSD = intent.getStringExtra(SBRemit.ANNRMEAMTUSD);
		availableBalance = intent.getStringExtra(SBRemit.AVAILABLE_BALANCE);
		String acountType = (String) accInfo.get(Comm.ACCOUNT_TYPE);
		if (SBRemitDataCenter.dissMissAcc.contains(acountType)) {
			((TextView) findViewById(R.id.year_have_used_limit_alert))
					.setText(getString(R.string.validity_have_used_limit));
			((TextView) findViewById(R.id.year_remain_limit_alert))
					.setText(getString(R.string.validity_remain_limit));
		}
		initViewInfos();
	}

	/**
	 * 初始化本页信息单
	 */
	private void initViewInfos() {
		BaseHttpEngine.dissMissProgressDialog();
		bremit_acc.setText(StringUtil.getForSixForString(acountNumber));
		// bill_mark.setText(LocalData.CurrencyCashremit.get(CUR_REMIT));

		if (come_from_rate_info) {
			if (LocalData.moneyTypeValueKey.containsKey(currency)) {
				String currencyName = (String) LocalData.moneyTypeValueKey
						.get(currency);
				for (int i = 0; i < moneyTypeList.size(); i++) {
					if (moneyTypeList.get(i).equals(currencyName)) {
						bremit_money_type.setSelection(i);
					}
				}
			}
		}
		// String str = colorText(useAmount, "#ba001d", true)
		// + colorText(MEIYUAN, "", false);
		// year_have_used_limit.setText(Html.fromHtml(str));
		// str = colorText(remainAmount, "#ba001d", true)
		// + colorText(MEIYUAN, "", false);
		String str = "-";
		if (!StringUtil.isNull(useAmount)) {
			str = DENGZHI + useAmount + MEIYUAN;
		}
		year_have_used_limit.setText(str);
		str = "-";
		if (!StringUtil.isNull(remainAmount)) {
			str = DENGZHI + remainAmount + MEIYUAN;
		}
		str = DENGZHI + remainAmount + MEIYUAN;
		year_remain_limit.setText(str);

		// String availableBalanceCUR = (String) SBRemitDataCenter.getInstance()
		// .getTryMap().get("availableBalanceCUR");
		// ？
		// String annRmeAmtCUR = (String) SBRemitDataCenter.getInstance()
		// .getTryMap().get("annRmeAmtCUR");
		//
		// if (Double.parseDouble(availableBalanceCUR) < Double
		// .parseDouble(annRmeAmtCUR)) {
		// largest_exchange.setText(availableBalanceCUR);
		// } else {
		// largest_exchange.setText(annRmeAmtCUR);
		// }
		if (StringUtil.isNullOrEmpty(trymapMap)) {
			return;
		}
	}

	/**
	 * @Title: requestForExchangeRate
	 * @Description: 请求结购汇当前参考牌价
	 * @param
	 * @return void
	 */
	public void requestForExchangeRate() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(SBRemit.SBREMIT_CUR_RENFERENCE_PRICE);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(SBRemit.ACCOUNT_ID, acountId);
		paramsmap.put(SBRemit.CURRENCY, currency);
		paramsmap.put(SBRemit.FESS_FLAG, B_REMIT_NEW);
		paramsmap.put(SBRemit.CASH_REMIT, cashRemit);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForExchangeRateCallBack");
	}

	/**
	 * @Title: requestForExchangeRateCallBack
	 * @Description: 请求结购汇当前参考牌价回调
	 * @param @param resultObj
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void requestForExchangeRateCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		exchangeRate = (Map<String, Object>) biiResponseBody.getResult();
		communicationCallBack(NEXT_STEP_CALLBACK);
		
	}

	@Override
	public void communicationCallBack(int flag) {
		switch (flag) {
		case NEXT_STEP_CALLBACK:
			if (exchangeRate != null) {
				try {
					ensureNext(); // TODO
				} catch (Exception e) {
					LogGloble.exceptionPrint(e);
				}
			}
			break;
		default:
			break;
		}
	}

	private void ensureNext() throws Exception {
		Intent intent = getIntent();
		intent.putExtra(SBRemit.FUR_INFO, capitaUseStr);
		intent.putExtra(SBRemit.CASH_REMIT, cashRemit);
		intent.putExtra(SBRemit.CURRENCY, currency);
		intent.putExtra(SBRemit.USED_AMOUNT, useAmount);
		intent.putExtra(SBRemit.REMAIN_AMOUNT, remainAmount);
		intent.putExtra(SBRemit.REFERENCE_RATE,
				exchangeRate.get(SBRemit.REFERENCE_RATE).toString());
		intent.putExtra(SBRemit.AMOUNT, bremitMoney);
		intent.putExtra(SBRemit.DATA_TABLE, dataTable);
		intent.putExtra(SBRemit.TRANS_RANDOM, transRandom);
		intent.setClass(this, BRemitConfirmInfoActivity.class);
		startActivityForResult(intent, BREMIT_OPERATION);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			if (requestCode == BREMIT_OPERATION) {
				setResult(resultCode);
				finish();
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private Map<String, Object> trymapMap;

	/**
	 * 结汇购汇：购汇重点关注对象确认书
	 */
	public void BRemitimportantIssues() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(SBRemit.ImportantIssues);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));

		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(SBRemit.ACCOUNT_ID, acountId);
		paramsmap.put(SBRemit.TOKEN, BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID));// 防止重复提交令牌
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "importantIssuesCallback");
	}

	/** 资金用途Spinner */
	private void setSpinnnerAdapter(Spinner spinner, List<String> data) {
		ArrayAdapter<String> remitadapter = new ArrayAdapter<String>(this,
				R.layout.spinner_item, data);
		remitadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(remitadapter);
		spinner.setSelection(0);
	}

	/** 603新增购汇币种有两种钞汇类型的 */
	public ArrayList<String> moneyTypetwo = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("012");// , "英镑");
			add("GBP");// , "英镑");
			add("014");// , "美元");
			add("USD");// , "美元");
			add("013");// , "港币");
			add("HKD");// , "港币");
			add("015");// , "瑞士法郎");
			add("CHF");// , "瑞士法郎");
			add("018");// , "新加坡元");
			add("SGD");// , "新加坡元");
			add("027");// , "日元");
			add("JPY");// , "日元");
			add("028");// , "加拿大元");
			add("CAD");// , "加拿大元");
			add("029");// , "澳大利亚元");
			add("AUD");// , "澳大利亚元");
			add("038");// , "欧元");
			add("EUR");// , "欧元");
			add("023");// , "挪威克朗");
			add("NOK");// , "挪威克朗");
			add("021");// , "瑞典克朗");
			add("SEK");// , "瑞典克朗");
			add("022");// , "丹麦克朗");
			add("DKK");// , "丹麦克朗");
			add("087");// , "新西兰元");
			add("NZD");// , "新西兰元");
			add("081");// , "澳门元");
			add("MOP");// , "澳门元");
			add("088");// , "韩国元");
			add("KRW");// , "韩国元");
			add("196");// , "俄罗斯卢布");
			add("RUB");// , "俄罗斯卢布");
			add("070");// , "南非兰特");
			add("ZAR");// , "南非兰特");
			add("084");// , "泰国铢");
			add("THB");// , "泰国铢");
			add("082");// , "菲律宾比索");
			add("PHP");// , "菲律宾比索");
		}
	};

	/** 603新增购汇币种有一种钞汇类型的现钞 */
	public ArrayList<String> moneyTypeone = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{

			add("056");// , "印尼卢比");
			add("IDR");// , "印尼卢比");
			add("134");// , "巴西里亚尔");
			add("BRL");// , "巴西里亚尔");
			add("213");// , "新台币");
			add("TWD");// , "新台币");
			add("085");// , "印度卢比");
			add("INR");// , "印度卢比");
			add("096");// , "阿联酋迪拉姆");
			add("AED");// , "阿联酋迪拉姆");
		}
	};
}
