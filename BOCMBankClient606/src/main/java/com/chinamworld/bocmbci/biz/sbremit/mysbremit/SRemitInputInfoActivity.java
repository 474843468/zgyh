package com.chinamworld.bocmbci.biz.sbremit.mysbremit;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
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
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 结汇 输入信息页 */
public class SRemitInputInfoActivity extends SBRemitBaseActivity implements
		OnClickListener, OnItemSelectedListener {

	/** 账户标识 */
	private String acountId;
	/** 账户号码 */
	private String acountNumber;
	/** 币种信息 币种 */
	private String currency;
	/** 币种信息 钞汇 */
	private String cashRemit;
	/** 已用额度 */
	private String useAmount;
	/** 剩余额度 */
	private String remainAmount;
	/** 数据表单 */
	private String dataTable;
	/** 交易随机值 */
	private String transRandom;
	/** 查询结购汇当前参考牌价 */
	private Map<String, Object> exchangeRate;
	/** 控件 账号 */
	private TextView sremit_acc;
	/** 控件 币种 */
	private TextView money_type;
	/** 控件 可用余额 */
	private TextView acc_of_balance;
	/** 控件 当前参考牌价 */
//	private TextView cur_reference_price;
	/** 控件 年已用额度 */
	private TextView year_have_used_limit;
	/** 控件 最大结汇金额试算 */
	private TextView largest_exchange;
	/** 控件 年剩余额度 */
	private TextView year_remain_limit;
	/** 控件 资金来源 */
	private Spinner capital_source;
	/** 资金来源Adapter */
	private ArrayAdapter arrayAdapter;
	/** 控件 资金用途 */
	private String capitaUseStr;
	/** 控件 资金用途id */
	private String capitaUseId;
	/** 控件 结汇金额 */
	private EditText sremit_money;
	/** 结汇金额 */
	private String sremitMoney;
	/** 控件 下一步 */
	private Button sbremit_sr_input_info_next;
	private ImageView source_tv;
	/** 可用余额 */
	private String availableBalance;
	/** 牌价 */
	private String exchange_rate;
	/** 确认书签署状态 */
	private String signStatus;
	/** 交易主体姓名 */
	private String custName;
	/** 是否重点关注对象 */
	private String important_focus;
	/** 交易主体类型代码 */
	private String custTypeCode;
	/** 发布日期 */
	private String pubDate;
	/** 截至日期 */
	private String endDate;
	/** 身份证号 */
	private String identityNumber;
	/** 身份证类型 */
	private String identityType;
	String keyongyue;
	/** 结汇/购汇类型 */
	private String sbType;
	/** 格式化后人民币余额 */
	private String availableBalanceRMB_new;
	/** 币种类型 */
	private String moneytype_new;
	/** 个人可结售汇金额折成美元 */
	private String annRmeAmtUSD;
	private static final int REQUEST_SOURCE_PROTOCOL_CODE = 10002;
	private String annRmeAmtCUR;
	/** 最大金额试算布局 */
	private LinearLayout largest_exchange_layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.sbremit_trade_type_sremit));// 为界面标题赋值
		addView(R.layout.sbremit_sr_input_info);// 添加布局

		initViews();
		BaseHttpEngine.showProgressDialog();

		initParamsInfo();

		// 美元不进行最大金额试算，非美元进行最大金额试算
		if (false == isRequestbiggesttry(sbType, cashRemit,
				availableBalanceRMB_new, moneytype_new, annRmeAmtUSD)) {
			initViewInfos();
		}else {
			String sbType_new="01";
			requestbiggesttry(sbType_new, cashRemit, availableBalanceRMB_new,
					moneytype_new, annRmeAmtUSD);
		}
		
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
		case R.id.sbremit_sr_input_info_next:
			if (validation()) {
				// add by luqp 如果用户没有选择资金来源 提示用户请选择! 2015年11月16日17:16:50
				if (capitaUseId.equals("-1")) { // 资金来源如果是"请选择"点击下一步提示用户选择资金来源.
					BaseDroidApp
							.getInstanse()
							.showInfoMessageDialog(
									SRemitInputInfoActivity.this
											.getResources()
											.getString(
													R.string.remit_please_choose_SR));
					return;
				}
				if (Double.parseDouble(sremitMoney) > Double
						.parseDouble(getIntent().getStringExtra(
								SBRemit.AVAILABLE_BALANCE))) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							SRemitInputInfoActivity.this.getResources()
									.getString(R.string.remit_acc_judgement));
					return;
				}
				// 查询结购汇当前参考牌价 跳转至确认页面
				BaseHttpEngine.showProgressDialog();
				requestForExchangeRate();
			}
			break;
		case R.id.source_instruction: // 资金来源说明
			Intent intent = new Intent(this,
					SBRemitSourceProtocolActivity.class);
			startActivityForResult(intent, REQUEST_SOURCE_PROTOCOL_CODE);
			// overridePendingTransition(R.anim.n_pop_enter_bottom_up,
			// R.anim.no_animation);
			break;
		default:
			break;
		}
	}

	/**
	 * 校验输入的购汇金额
	 * 
	 * @return true:校验通过 false:校验未通过
	 */
	private boolean validation() {
		sremitMoney = sremit_money.getText().toString();
		RegexpBean regAmount = null;
		if (!LocalData.codeNoNumber.contains(currency)) {// 判断是否是日元，日元用日元的校验规则
			regAmount = new RegexpBean(
					getString(R.string.sremit_money_no_colon), sremitMoney,
					"amount");
		} else {
			if (LocalData.codeNoNumber.get(0).equals(currency)) { // 日元(currency
																	// = "027")
				regAmount = new RegexpBean(
						getString(R.string.bremit_money_no_colon), sremitMoney,
						"spetialAmountJPY");
			} else if (LocalData.codeNoNumber.get(2).equals(currency)) {// 韩元(currency
																		// =
																		// "088")
				regAmount = new RegexpBean(
						getString(R.string.bremit_money_no_colon), sremitMoney,
						"spetialAmountKRW");
			} else {
				regAmount = new RegexpBean(
						getString(R.string.bremit_money_no_colon), sremitMoney,
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
		sremit_acc = (TextView) findViewById(R.id.sremit_acc);
		money_type = (TextView) findViewById(R.id.money_type);
//		cur_reference_price=(TextView) findViewById(R.id.cur_reference_price);
		acc_of_balance=(TextView) findViewById(R.id.acc_of_balance);
		year_have_used_limit = (TextView) findViewById(R.id.year_have_used_limit);
		largest_exchange = (TextView) findViewById(R.id.largest_exchange);
		largest_exchange_layout=(LinearLayout) findViewById(R.id.largest_exchange_layout);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				largest_exchange);
		year_remain_limit = (TextView) findViewById(R.id.year_remain_limit);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.year_have_used_limit_alert));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.year_remain_limit_alert));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.largest_exchange_calculate));

		capital_source = (Spinner) findViewById(R.id.capital_source);
		capital_source.setOnItemSelectedListener(this);
		identityType = getIntent().getStringExtra(SBRemit.IDENTITYTYPE);
		// ------给Spinners添加适配器 start
		if (identityType.equals("01")) {
			arrayAdapter = new ArrayAdapter<String>(this,
					R.layout.spinner_item, capitalUseListIn);
		} else {
			arrayAdapter = new ArrayAdapter<String>(this,
					R.layout.spinner_item, capitalUseListOut);
		}

		arrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		capital_source.setAdapter(arrayAdapter);
		// ------给Spinners添加适配器 end
		sremit_money = (EditText) findViewById(R.id.sremit_money);
		sbremit_sr_input_info_next = (Button) findViewById(R.id.sbremit_sr_input_info_next);
		sbremit_sr_input_info_next.setOnClickListener(this);

		source_tv = (ImageView) findViewById(R.id.source_instruction);
		source_tv.setOnClickListener(this);
//		requestForExchangeRate();
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if (identityType.equals("01")) {
			capitaUseStr = capitalUseListIn.get(arg2);
			capitaUseId = capitalUseValueKeyIn.get(capitaUseStr);
		} else {
			capitaUseStr = capitalUseListOut.get(arg2);
			capitaUseId = capitalUseValueKeyOut.get(capitaUseStr);
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
		identityNumber = intent.getStringExtra(SBRemit.IDENTITYNUMBER);
		LogGloble.e("asd", "accInfo" + accInfo);
		acountId = (String) accInfo.get(Comm.ACCOUNT_ID);
		acountNumber = (String) accInfo.get(Comm.ACCOUNTNUMBER);
		currency = intent.getStringExtra(SBRemit.CURRENCY);
		cashRemit = intent.getStringExtra(SBRemit.CASH_REMIT);
		dataTable = intent.getStringExtra(SBRemit.DATA_TABLE);
		// transRandom = intent.getStringExtra(SBRemit.TRANS_RANDOM);
		signStatus = intent.getStringExtra(SBRemit.SIGNSTATUS);
		custName = intent.getStringExtra(SBRemit.CUSTNAME);
		pubDate = intent.getStringExtra(SBRemit.PUBDATE);
		endDate = intent.getStringExtra(SBRemit.END_DATE);
		important_focus = intent.getStringExtra(SBRemit.IMPORTANT_FOCUS);
		custTypeCode = intent.getStringExtra(SBRemit.CUSTTYPRCODE);

		sbType = intent.getStringExtra(SBRemit.SB_TYPE);
		availableBalanceRMB_new = intent
				.getStringExtra(SBRemit.AVAILABLE_BALANCE_new);
		moneytype_new = intent.getStringExtra(SBRemit.MONERY_TYPE_new);
		annRmeAmtUSD = intent.getStringExtra(SBRemit.ANNRMEAMTUSD);

		// 等值美元，可以有小数点
		// useAmount = intent.getStringExtra(SBRemit.USED_AMOUNT);
		useAmount = (String) SBRemitDataCenter.getInstance().getResultAmount()
				.get("annAmtUSD");
		useAmount = StringUtil.parseStringPattern(useAmount,
				SBRemitDataCenter.getInstance().CASH_PATTERN_PARAM);
		// 等值美元，可以有小数点
		// remainAmount = intent.getStringExtra(SBRemit.REMAIN_AMOUNT);
		remainAmount = (String) SBRemitDataCenter.getInstance()
				.getResultAmount().get("annRmeAmtUSD");
		remainAmount = StringUtil.parseStringPattern(remainAmount,
				SBRemitDataCenter.getInstance().CASH_PATTERN_PARAM);

		availableBalance = intent.getStringExtra(SBRemit.AVAILABLE_BALANCE);

		availableBalance = StringUtil.parseStringCodePattern(currency,
				availableBalance,
				SBRemitDataCenter.getInstance().CASH_PATTERN_PARAM);

		exchange_rate = StringUtil.parseStringCodePattern(currency,
				exchange_rate,
				SBRemitDataCenter.getInstance().CASH_PATTERN_PARAM);

		if (StringUtil.isNull(acountId) || StringUtil.isNull(acountNumber)
				|| StringUtil.isNull(currency) || StringUtil.isNull(cashRemit)
				|| StringUtil.isNull(useAmount)
				|| StringUtil.isNull(remainAmount))
			return;
		String acountType = (String) accInfo.get(Comm.ACCOUNT_TYPE);
		// if (SBRemitDataCenter.dissMissAcc.contains(acountType)) {
		// ((TextView) findViewById(R.id.year_have_used_limit_alert))
		// .setText(getString(R.string.validity_have_used_limit));
		// ((TextView) findViewById(R.id.year_remain_limit_alert))
		// .setText(getString(R.string.validity_remain_limit));
		// }
		// if ((Integer.valueOf(important_focus) == 02)
		// && ((Integer.valueOf(signStatus) == 0))
		// && ((Integer.valueOf(custTypeCode) == 01))) {
		// BaseDroidApp.getInstanse().showSbremitImportFocus(custName,
		// identityNumber, pubDate, endDate, cancelClickListener,
		// ensureClickListener);
		// }

	}

	/**
	 * 初始化本页信息单,除已用额度、剩余额度
	 */
	private void initViewInfos() {
		BaseHttpEngine.dissMissProgressDialog();

		// if(identityType.equals("01")){
		// capitaUseStr = capitalUseListIn.get(0);
		// capitaUseId = capitalUseValueKeyIn.get(capitaUseStr);
		// }else{
		// capitaUseStr = capitalUseListOut.get(0);
		// capitaUseId = capitalUseValueKeyOut.get(capitaUseStr);
		// }

		sremit_acc.setText(StringUtil.getForSixForString(acountNumber));
		acc_of_balance.setText(StringUtil.parseStringPattern2(availableBalance,2));
		money_type.setText(LocalData.Currency.get(currency)
				+ LocalData.CurrencyCashremit.get(cashRemit));
		// String str = colorText(useAmount, "#ba001d", true) +
		// colorText(MEIYUAN, "", false);
		// year_have_used_limit.setText(Html.fromHtml(str));
		// str = colorText(remainAmount, "#ba001d", true) + colorText(MEIYUAN,
		// "", false);
		// year_remain_limit.setText(Html.fromHtml(str));
		String str = "-";
		if (!StringUtil.isNull(useAmount)) {
			str = DENGZHI + useAmount + MEIYUAN;
		}
		year_have_used_limit.setText(str);
		str = "-";
		if (!StringUtil.isNull(remainAmount)) {
			str = DENGZHI + remainAmount + MEIYUAN;
		}
		year_remain_limit.setText(str);
		
		// 日元的时候 不能输入小数
		// if(LocalData.codeNoNumber.contains(currency))
		// sremit_money.setInputType(InputType.TYPE_CLASS_NUMBER);
		// if (SBRemitDataCenter.getInstance().getTryMap().size() == 0) {
		// LogGloble.e("fsg", "+++"
		// + SBRemitDataCenter.getInstance().getTryMap().toString());
		// return;
		// }

		if (LocalData.Currency.get(currency).equals("美元")||(Double.parseDouble(annRmeAmtUSD)==0)) {
			LogGloble.e("asd", "1111" + availableBalance);
			// LogGloble.e("asd", "1234" +
			// Double.parseDouble(""+availableBalance));
			
			if (Double.parseDouble((String) SBRemitDataCenter.getInstance()
					.getResultAmount().get("annRmeAmtUSD")) < Double
					.parseDouble((getIntent()
							.getStringExtra(SBRemit.AVAILABLE_BALANCE)))) {
				largest_exchange.setText(remainAmount);
			} else {

				largest_exchange.setText(availableBalance);
			}

		} else {
			// String availableBalanceCUR = (String) SBRemitDataCenter
			// .getInstance().getTryMap().get("availableBalanceCUR");
			Map<String, Object> tryMap = SBRemitDataCenter.getInstance()
					.getTryMap();
			LogGloble.e("asd", "try" + tryMap);
			Map<String, Object> resultAmount12 = SBRemitDataCenter
					.getInstance().getResultAmount();
			annRmeAmtCUR = (String) SBRemitDataCenter.getInstance().getTryMap()
					.get("annRmeAmtCUR");
			String annRmeAmtCUR2 = StringUtil.parseStringCodePattern(currency,
					annRmeAmtCUR,
					SBRemitDataCenter.getInstance().CASH_PATTERN_PARAM);
			String availableBalancenew = getIntent().getStringExtra(
					SBRemit.AVAILABLE_BALANCE);
			String availableBalancenew2 = StringUtil.parseStringCodePattern(
					currency, availableBalancenew,
					SBRemitDataCenter.getInstance().CASH_PATTERN_PARAM);
			if (Double.parseDouble(availableBalancenew) < Double
					.parseDouble(annRmeAmtCUR)) {
				largest_exchange.setText(availableBalancenew2);
			} else {
				largest_exchange.setText(annRmeAmtCUR2);
			}
		}
//		//当年度剩余额度为0的时候，最大试算金额根据币种选择
//				if(Double.parseDouble(remainAmount)==0){
//					if(LocalData.Currency.get(currency).equals("日元")||LocalData.Currency.get(currency).equals("韩元")){
//						largest_exchange.setText("0");
//					}else{
//						largest_exchange.setText("0.00");
//					}
//				}
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
		paramsmap.put(SBRemit.FESS_FLAG, S_REMIT_NEW);
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
					ensureNext();
				} catch (Exception e) {
					LogGloble.exceptionPrint(e);
				}
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 确认跳转下一步
	 * 
	 * @throws Exception
	 */
	private void ensureNext() throws Exception {
		Intent intent = getIntent();
		intent.putExtra(SBRemit.FUR_INFO, capitaUseStr);
		intent.putExtra(SBRemit.USED_AMOUNT, useAmount);
		intent.putExtra(SBRemit.REMAIN_AMOUNT, remainAmount);
		intent.putExtra(SBRemit.REFERENCE_RATE,
				exchangeRate.get(SBRemit.REFERENCE_RATE).toString());
		intent.putExtra(SBRemit.AMOUNT, sremitMoney);
		intent.putExtra(SBRemit.DATA_TABLE, dataTable);
		intent.putExtra(SBRemit.TRANS_RANDOM, transRandom);
		intent.putExtra(SBRemit.IDENTITYTYPE, identityType);
		intent.setClass(this, SRemitConfirmInfoActivity.class);
		startActivityForResult(intent, SREMIT_OPERATION);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			if (requestCode == SREMIT_OPERATION) {
				setResult(resultCode);
				finish();
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 建立取消按钮 监听
	 */
	private OnClickListener cancelClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();// 关闭详情框
			finish();
		}
	};
	/**
	 * 建立确定按钮 监听
	 */
	private OnClickListener ensureClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();// 关闭详情框
			SRemitimportantIssues();
		}
	};

	/**
	 * 结汇购汇：结汇重点关注对象确认书
	 */
	public void SRemitimportantIssues() {
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
	/**
	 * @Title: requestForCardListCallBack
	 * @Description: 请求最大金额试算返回
	 * @param @param resultObj
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void requestbiggesttryCallBack(Object resultObj) {
		super.requestbiggesttryCallBack(resultObj);
		initViewInfos();
	}
	//调用最大金额试算接口的时候，报错也好、要继续进行
	@Override
	public boolean doBiihttpRequestCallBackPre(BiiResponse response) {
		List<BiiResponseBody> biiResponseBodys = response.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {
			if (SBRemit.BIGGEST_TRY.equals(biiResponseBody.getMethod())) {
				BiiHttpEngine.dissMissProgressDialog();
				BiiError biiError = biiResponseBody.getError();
				// 判断是否存在error
				if (biiError != null) {
					largest_exchange_layout.setVisibility(View.GONE);
					if (biiError.getCode() != null) {
						if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
							return super.doBiihttpRequestCallBackPre(response);
						}
					}else {
						largest_exchange_layout.setVisibility(View.VISIBLE);
					}
				}
		
				initViewInfos();
				BaseDroidApp.getInstanse().createDialog("",
						biiError.getMessage(), new OnClickListener() {
							@Override
							public void onClick(View v) {
								BaseDroidApp.getInstanse()
										.dismissErrorDialog();
								
							}
						});
				return true;
			}
		}

		return super.doBiihttpRequestCallBackPre(response);
	}

}
