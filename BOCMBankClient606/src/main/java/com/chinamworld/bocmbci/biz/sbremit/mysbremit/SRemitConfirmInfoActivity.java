package com.chinamworld.bocmbci.biz.sbremit.mysbremit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.chinamworld.bocmbci.utils.StringUtil;

public class SRemitConfirmInfoActivity extends SBRemitBaseActivity implements
		OnClickListener {
	/** 身份证类型 */
	private String identityType;
	/** 账户标识 */
	private String acountId;
	/** 账户号码 */
	private String acountNumber;
	/** 币种信息 币种 */
	private String currency;
	/** 币种信息 钞汇 */
	private String cashRemit;
	/** 币种信息 已用余额 */
	private String usedAmount;
	/** 币种信息 可用余额 */
	private String remainAmount;
	/** 当前参考牌价 */
	private String exchangeRate;
	/** 控件 账号 */
	private TextView sremit_acc;
	/** 控件 币种 */
	private TextView money_type;
	/** 控件 年已用额度 */
	private TextView year_have_used_limit;
	/** 控件 年剩余额度 */
	private TextView year_remain_limit;
	/** 控件 资金来源 */
	private TextView capital_source;
	/** 控件 当前参考牌价 */
	private TextView cur_reference_price;
	/** 控件 资金用途id */
	private String capitaUseId;
	/** 控件 资金用途 */
	private String capitaUse;
	/** 控件 结汇金额 */
	private TextView sremit_money;
	/** 结汇金额 */
	private String sremitMoney;
	/** 数据表单 */
	private String dataTable;
	/** 随机值 */
	private String transRandom;
	/** 控件 下一步 */
	private Button sbremit_confirm_info_ok;
	/** 结汇信息 */
	private Map<String, Object> sRemitInfo;
	String str = "-";
	String str1 = "-";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.sbremit_trade_type_sremit));// 为界面标题赋值
		addView(R.layout.sbremit_sr_confirm_info);// 添加布局
		BaseHttpEngine.showProgressDialog();
		initViews();
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
		case R.id.sbremit_confirm_info_ok:
			BaseHttpEngine.showProgressDialog();
			requestPSNGetTokenId(BaseDroidApp.getInstanse().getBizDataMap()
					.get(ConstantGloble.CONVERSATION_ID).toString());
			// requestForSellRemit();// 结汇
			break;
		default:
			break;
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID).toString());
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		requestForSellRemit();// 结汇

	}

	/**
	 * 实例化控件
	 */
	private void initViews() {
		back.setOnClickListener(this);
		btn_right.setOnClickListener(this);
		sremit_acc = (TextView) findViewById(R.id.sremit_acc);
		money_type = (TextView) findViewById(R.id.money_type);
		year_have_used_limit = (TextView) findViewById(R.id.year_have_used_limit);
		year_remain_limit = (TextView) findViewById(R.id.year_remain_limit);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.year_have_used_limit_alert));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.year_remain_limit_alert));
		capital_source = (TextView) findViewById(R.id.capital_source);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				capital_source);
		cur_reference_price = (TextView) findViewById(R.id.cur_reference_price);
		// 省略时添加浮动窗口
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				((TextView) findViewById(R.id.cur_reference_price_alert)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				cur_reference_price);
		sremit_money = (TextView) findViewById(R.id.sremit_money);
		sbremit_confirm_info_ok = (Button) findViewById(R.id.sbremit_confirm_info_ok);
		sbremit_confirm_info_ok.setOnClickListener(this);
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
		currency = intent.getStringExtra(SBRemit.CURRENCY);
		cashRemit = intent.getStringExtra(SBRemit.CASH_REMIT);
		capitaUse = intent.getStringExtra(SBRemit.FUR_INFO);
		usedAmount = intent.getStringExtra(SBRemit.USED_AMOUNT);
		remainAmount = intent.getStringExtra(SBRemit.REMAIN_AMOUNT);
		sremitMoney = intent.getStringExtra(SBRemit.AMOUNT);
		dataTable = intent.getStringExtra(SBRemit.DATA_TABLE);
		transRandom = intent.getStringExtra(SBRemit.TRANS_RANDOM);
		exchangeRate = intent.getStringExtra(SBRemit.REFERENCE_RATE);
		identityType = intent.getStringExtra(SBRemit.IDENTITYTYPE);
		// exchangeRate = StringUtil.parseStringPattern(
		// exchangeRate,
		// SBRemitDataCenter.getInstance().CASH_PATTERN_PARAM);
		if (StringUtil.isNull(acountId) || StringUtil.isNull(acountNumber)
				|| StringUtil.isNull(currency) || StringUtil.isNull(cashRemit)
				|| StringUtil.isNull(capitaUse)
				|| StringUtil.isNull(usedAmount)
				|| StringUtil.isNull(remainAmount)
				|| StringUtil.isNull(exchangeRate)
				|| StringUtil.isNull(sremitMoney))
			return;
		String acountType = (String) accInfo.get(Comm.ACCOUNT_TYPE);
		if (SBRemitDataCenter.dissMissAcc.contains(acountType)) {
			((TextView) findViewById(R.id.year_have_used_limit_alert))
					.setText(getString(R.string.validity_have_used_limit));
			((TextView) findViewById(R.id.year_remain_limit_alert))
					.setText(getString(R.string.validity_remain_limit));
		}
		initViewInfos();// 初始化信息单
		BaseHttpEngine.dissMissProgressDialog();
	}

	/**
	 * 初始化本页信息单
	 */
	private void initViewInfos() {
		sremit_acc.setText(StringUtil.getForSixForString(acountNumber));
		money_type.setText(LocalData.Currency.get(currency)
				+ "  "+LocalData.CurrencyCashremit.get(cashRemit));
		capital_source.setText(capitaUse);
		// String str = colorText(usedAmount, "#ba001d", true) +
		// colorText(MEIYUAN, "", false);
		// year_have_used_limit.setText(Html.fromHtml(str));
		// str = colorText(remainAmount, "#ba001d", true) + colorText(MEIYUAN,
		// "", false);
		// year_remain_limit.setText(Html.fromHtml(str));

		if (!StringUtil.isNull(usedAmount)) {
			str = DENGZHI + usedAmount + MEIYUAN;
		}
		year_have_used_limit.setText(str);

		if (!StringUtil.isNull(remainAmount)) {
			str1 = DENGZHI + remainAmount + MEIYUAN;
		}
		year_remain_limit.setText(str1);

		// 正则表达式去掉小数点后无用的0
		if (exchangeRate.indexOf(".") > 0) {

			exchangeRate = exchangeRate.replaceAll("0+?$", "");
			exchangeRate = exchangeRate.replaceAll("[.]$", "");

		}
		if (StringUtil.splitStringwith2pointnew(exchangeRate) < 4) {
			cur_reference_price.setText(exchangeRate);
		} else {
			String str = StringUtil.splitStringwith2point(exchangeRate, 4);
			try {
				double d = Double.parseDouble(str);
				str = String.valueOf(d);
				cur_reference_price.setText(str);
			} catch (Exception e) {
				cur_reference_price.setText(str);
			}
		}
		sremit_money
				.setText(StringUtil.parseStringCodePattern(currency,
						sremitMoney,
						SBRemitDataCenter.getInstance().CASH_PATTERN_PARAM));
	}

	/**
	 * @Title: requestForSellRemit
	 * @Description: 请求结汇
	 * @param
	 * @return void
	 */
	public void requestForSellRemit() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody
				.setConversationId(BaseDroidApp.getInstanse().getBizDataMap()
						.get(ConstantGloble.CONVERSATION_ID).toString());
		biiRequestBody.setMethod(SBRemit.SBREMIT_SREMIT_NEW);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(SBRemit.ACCOUNT_ID, acountId);
		paramsmap.put(SBRemit.CURRENCY, currency);
		paramsmap.put(SBRemit.CASH_REMIT, cashRemit);
		paramsmap.put(SBRemit.AMOUNT, sremitMoney);
		if (identityType.equals("01")) {
			paramsmap.put(SBRemit.FOUD_SOURCE,
					capitalUseValueKeyIn.get(capitaUse));
		} else {
			paramsmap.put(SBRemit.FOUD_SOURCE,
					capitalUseValueKeyOut.get(capitaUse));
		}

		// paramsmap.put(SBRemit.TRANS_RANDOM, transRandom);// 随机值
		// paramsmap.put(SBRemit.DATA_TABLE, dataTable);// 数据表单
		// paramsmap.put(SBRemit.USED_AMOUNT, usedAmount);
		paramsmap.put(SBRemit.TOKEN, BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID));// 防止重复提交令牌
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForSellRemitCallBack");
	}

	/**
	 * @Title: requestForSellRemitCallBack
	 * @Description: 请求结汇回调
	 * @param @param resultObj
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void requestForSellRemitCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		sRemitInfo = (Map<String, Object>) biiResponseBody.getResult();
		communicationCallBack(NEXT_STEP_CALLBACK);
	}

	@Override
	public void requestGetSecurityFactor(String serviceId) {
		super.requestGetSecurityFactor(serviceId);
	}

	@Override
	public void communicationCallBack(int flag) {
		switch (flag) {
		case NEXT_STEP_CALLBACK:
			if (sRemitInfo != null) {
				try {
					String sbType = "01";
					requestForCommonData(sbType, acountId);

				} catch (Exception e) {
					LogGloble.exceptionPrint(e);
				}
			}
			break;
		default:
			break;
		}
	}

	public void requestForCommonDataCallBack(Object resultObj) {
		super.requestForCommonDataCallBack(resultObj);
		try {
			ensureNext();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogGloble.exceptionPrint(e);
		}
	}

	/**
	 * 结汇成功至结汇成功页
	 */
	private void ensureNext() throws Exception {
		Intent intent = new Intent();
		intent.putExtra(SBRemit.TRANSACTION_ID,
				sRemitInfo.get(SBRemit.TRANSACTION_ID).toString());
		intent.putExtra(SBRemit.EXCHANGE_RATE,
				isObjNull(sRemitInfo.get(SBRemit.EXCHANGE_RATE), exchangeRate));
		intent.putExtra(SBRemit.CASH_REMIT,
				sRemitInfo.get(SBRemit.CASH_REMIT) == null ? "" : sRemitInfo
						.get(SBRemit.CASH_REMIT).toString());
		// 判断境内境外
		if (identityType.equals("01")) {
			intent.putExtra(SBRemit.FUND_USE_INFO,
					capitalUseValueKeyIn.get(capitaUse).toString());
		} else {
			intent.putExtra(SBRemit.FUND_USE_INFO,
					capitalUseValueKeyOut.get(capitaUse).toString());
		}

		intent.putExtra(SBRemit.RETURN_CNY_AMT,
				sRemitInfo.get(SBRemit.RETURN_CNY_AMT) == null ? ""
						: sRemitInfo.get(SBRemit.RETURN_CNY_AMT).toString());
		intent.putExtra(SBRemit.TRADE_STATUS_2,
				sRemitInfo.get(SBRemit.TRADE_STATUS_2) == null ? ""
						: sRemitInfo.get(SBRemit.TRADE_STATUS_2).toString());
		intent.putExtra(SBRemit.CURRENCY,
				isObjNull(sRemitInfo.get(SBRemit.CURRENCY)));
		intent.putExtra(SBRemit.USED_AMOUNT, isObjNull(usedAmount));
		intent.putExtra(SBRemit.REMAIN_AMOUNT, isObjNull(remainAmount));
		intent.putExtra(SBRemit.AMOUNT, isObjNull(sremitMoney));
		intent.setClass(this, SRemitSuccessActivity.class);
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
	}

	/**
	 * 在本界面调用查询个人结售汇额度接口异常拦截 本方法有待改进
	 */
	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		if (resultObj instanceof BiiResponse) {
			BiiResponse biiResponse = (BiiResponse) resultObj;
			List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
			BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
			if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
				//
				//
				if (SBRemit.SBREMIT_MONEY_FORM_NEW.equals(biiResponseBody
						.getMethod())) {// 行内手续费试算
					if (biiResponse.isBiiexception()) {// 代表返回数据异常
						BiiHttpEngine.dissMissProgressDialog();
						BiiError biiError = biiResponseBody.getError();
						// 判断是否存在error
						if (biiError != null) {
							if (biiError.getCode() != null) {
								Map<String, Object> resultAmount = SBRemitDataCenter
										.getInstance().getResultAmount();
								if (!StringUtil.isNullOrEmpty(resultAmount)) {
									resultAmount.clear();
								}
								if (LocalData.timeOutCode.contains(biiError
										.getCode())) {// 表示回话超时
									// 要重新登录
									showTimeOutDialog(biiError.getMessage());
								} else {// 非会话超时错误拦截
									try {
										ensureNext();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						}
						return true;
					}
					return false;// 没有异常
				} else {
					return super.httpRequestCallBackPre(resultObj);
				}
			}
			// 随机数获取异常
		}
		return super.httpRequestCallBackPre(resultObj);
	}

//	public void showTimeOutDialog(BiiError biiError) {
//		BaseDroidApp.getInstanse().showMessageDialog(biiError.getMessage(),
//				new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						BaseDroidApp.getInstanse().dismissErrorDialog();
//						ActivityTaskManager.getInstance().removeAllActivity();
//						Intent intent = new Intent();
//						intent.setClass(SRemitConfirmInfoActivity.this,
//								LoginActivity.class);
//						startActivityForResult(intent,
//								ConstantGloble.ACTIVITY_RESULT_CODE);
//					}
//				});
//	}
}
