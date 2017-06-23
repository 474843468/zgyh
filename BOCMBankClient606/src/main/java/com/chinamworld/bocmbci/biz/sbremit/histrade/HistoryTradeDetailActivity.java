package com.chinamworld.bocmbci.biz.sbremit.histrade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.SBRemit;
import com.chinamworld.bocmbci.biz.sbremit.SBRemitBaseActivity;
import com.chinamworld.bocmbci.biz.sbremit.SBRemitDataCenter;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class HistoryTradeDetailActivity extends SBRemitBaseActivity implements
		OnClickListener {

	/** 交易序号 */
	private String tradeSerialNumber;
	/** 交易结果返回码 */
	private String tranRetCode;

	/** 手工处理状态码 */
	private String handleStatus;
	/** 交易日期 */
	private String tradeDate;
	/** 交易账号 */
	private String tradeAcc;
	/** 交易类型 */
	private String tradeType;
	/** 资金用途 */
	private String capitalUseCode;
	/** 钞汇标志 */
	private String cashRemit;
	/** 币种 */
	private String currencyCode;
	/** 兑换牌价 */
	private String exchangeRate;
	/** 人民币金额 */
	private String rmbAmount;
	/** 交易金额 */
	private String tradeAmount;
	/** 交易状态 */
	private String tradeStatus;
	/** 交易渠道 */
	private String tradeChannel;
	/** 控件 交易序号 */
	private TextView trade_serial_number;
	/** 交易日期 */
	private TextView trade_date;
	/** 控件 交易账号 */
	private TextView trade_acc;
	/** 控件 交易类型 */
	private TextView trade_type;
	/** 控件 资金用途 */
	private TextView capital_use;
	/** 控件 币种and钞汇 */
	private TextView cash_remit;
	/** 控件 当前参考牌价 */
	private TextView exchange_rate;
	/** 控件 人民币金额 */
	private TextView rmb_amount;
	/** 控件 外币金额 */
	private TextView trade_money;
	/** 控件 外币币种 */
	private TextView foreign_currency;
	/** 控件 汇钞类型 */
	private TextView money_type;
	/** 控件 交易状态 */
	private TextView trade_status;
	/** 控件 失败原因 */
	private LinearLayout fail_reason;
	/** 控件 shibai原因 */
	private TextView foreign_currency_reason;
	/** 交易失败原因 */
	private Map<String, Object> failureReason;
	/** 控件 手工处理状态 */
	private LinearLayout handmade;
	/** 控件 手工改处理状态 */
	private TextView handmade_message;
	/** 手工处理状态原因 */
	private String handmadeString;
	/** 资金属性代码 */
	private String fundTypeCode;
	/** 交易时间 */
	private String paymentTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.history_trade_query));// 为界面标题赋值
		addView(R.layout.sbremit_history_trade_detail);// 添加布局
		BaseHttpEngine.showProgressDialog();
		initViews();
		initParamsInfo();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			finish();
			break;
		case R.id.ib_top_right_btn:// 返回主页
			ActivityTaskManager.getInstance().removeAllActivity();
			break;
		default:
			break;
		}
	}

	private void initViews() {
		back.setOnClickListener(this);
		btn_right.setOnClickListener(this);
		trade_serial_number = (TextView) findViewById(R.id.trade_serial_number);
		trade_date = (TextView) findViewById(R.id.trade_date);
		trade_acc = (TextView) findViewById(R.id.trade_acc);
		trade_type = (TextView) findViewById(R.id.trade_type);
		capital_use = (TextView) findViewById(R.id.capital_use);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				capital_use);
		// cash_remit = (TextView)findViewById(R.id.money_type);
		exchange_rate = (TextView) findViewById(R.id.exchange_rate);
		rmb_amount = (TextView) findViewById(R.id.rmb_money);
		trade_money = (TextView) findViewById(R.id.trade_money);
		trade_status = (TextView) findViewById(R.id.trade_status);
		foreign_currency = (TextView) findViewById(R.id.foreign_currency);
		money_type = (TextView) findViewById(R.id.money_type);
		fail_reason = (LinearLayout) findViewById(R.id.fail_reason);
		foreign_currency_reason = (TextView) findViewById(R.id.foreign_currency_reason);
		handmade = (LinearLayout) findViewById(R.id.handmade);
		handmade_message = (TextView) findViewById(R.id.handmade_message);
	}

	/**
	 * 接收调用页的信息，信息完整则初始本页面
	 */
	private void initParamsInfo() {
		if (SBRemitDataCenter.getInstance().getInfoDetail() != null) {
			// 详情的信息
			Map<String, Object> detail = SBRemitDataCenter.getInstance()
					.getInfoDetail();
			// 当为失败的时候，为列表信息
			Map<String, Object> detailList = SBRemitDataCenter.getInstance()
					.getTradeListTotal();
			tradeSerialNumber = (String) detail.get(SBRemit.BANKSELFNUM);
			tradeDate = (String) detail.get(SBRemit.TRANS_DATE);
			paymentTime = (String) detail.get(SBRemit.PAYMENTTIME);
			tradeAcc = (String) detail.get(SBRemit.ACCOUNT_NUMBER);
			tradeType = (String) detail.get(SBRemit.TRANS_TYPE);
			LogGloble.e("asd", "111" + tradeType);
			capitalUseCode = (String) detail.get(SBRemit.FUR_INFO);
			cashRemit = (String) detail.get(SBRemit.CASH_REMIT);
			currencyCode = (String) detail.get(SBRemit.CURRENCY_CODE);
			exchangeRate = (String) detail.get(SBRemit.EXCHANGE_RATE);
			rmbAmount = (String) detail.get(SBRemit.RETURN_CNY_AMT);
			tradeAmount = (String) detail.get(SBRemit.AMOUNT);
			tradeStatus = (String) detail.get(SBRemit.TRADE_STATUS);
			tradeChannel = (String) detail.get(SBRemit.CHANNEL);
			tranRetCode = (String) detailList.get(SBRemit.TRANRETCODE);
			handleStatus = (String) detailList.get(SBRemit.HANDLESSTATUS);
			fundTypeCode = (String) detailList.get(SBRemit.FUNDTYPECODE);
			if (detailList.get(SBRemit.STATUS).equals("01")) {

				capitalUseCode = (String) detailList.get(SBRemit.FUR_INFO);
				cashRemit = (String) detailList.get(SBRemit.CASH_REMIT);
				currencyCode = (String) detailList.get(SBRemit.CURRENCY_CODE);
				exchangeRate = (String) detailList.get(SBRemit.EXCHANGE_RATE);
				rmbAmount = (String) detailList.get(SBRemit.RETURN_CNY_AMT);
				tradeAmount = (String) detailList.get(SBRemit.AMOUNT);
				tradeStatus = (String) detailList.get(SBRemit.TRADE_STATUS);
				tradeChannel = (String) detailList.get(SBRemit.CHANNEL);

				fail_reason.setVisibility(View.VISIBLE);

				requestForPsnFessQueryForErrormesg(tranRetCode);

			} else if (detail.get(SBRemit.STATUS).equals("02")) {
				handmade.setVisibility(View.VISIBLE);
				handmade_message.setText(LocalData.handMade.get(handleStatus));
			}
		}
		/*
		 * 结汇详情页面时显示资金来源
		 */
		if (isStrEquals(tradeType, S_REMIT)) {
			((TextView) findViewById(R.id.capital_use_alert))
					.setText(getString(R.string.capital_source));
		}
		try {
			initViewInfos();
		} catch (Exception e) {
			LogGloble.exceptionPrint(e);
		}
	}

	/**
	 * 初始化本页信息单
	 */
	@SuppressWarnings("static-access")
	private void initViewInfos() throws Exception {
		BaseHttpEngine.dissMissProgressDialog();

		trade_serial_number.setText(tradeSerialNumber);
		rmb_amount.setText(StringUtil.parseStringPattern(rmbAmount,
				SBRemitDataCenter.getInstance().CASH_PATTERN_PARAM));
		trade_money
				.setText(StringUtil.parseStringCodePattern(currencyCode,
						tradeAmount,
						SBRemitDataCenter.getInstance().CASH_PATTERN_PARAM));
		foreign_currency.setText(LocalData.Currency.get(currencyCode) + "  "
				+ LocalData.CurrencyCashremit.get(cashRemit));
		// money_type.setText(LocalData.CurrencyCashremit.get(cashRemit));
		trade_status.setText(statusnew.get(tradeStatus));
		((TextView) findViewById(R.id.trade_channel))
				.setText(strIsNull(channelnew.get(tradeChannel)));// add 添加渠道字段
		// trade_date.setText(QueryDateUtils.getcurrentDate(tradeDate));
		if (paymentTime == null) {
			trade_date.setText(tradeDate);
		} else {
			trade_date.setText(tradeDate + "  " + paymentTime);
		}
		trade_acc.setText(StringUtil.getForSixForString(tradeAcc));
		trade_type.setText(getKeyByValue(sbRemitValueKey_New, tradeType));
		// trade_type.setText(sbRemitValueKey_New.get(tradeType));
		if (tradeStatus.equals("00")) {
			if (tradeType.equals(S_REMIT))
				capital_use.setText(capitalResource.get(capitalUseCode));

			else
				capital_use.setText(capitalUse.get(capitalUseCode));
		} else {
			if (tradeType.equals(S_REMIT))
				capital_use.setText(capitalResource.get(fundTypeCode));
			else
				capital_use.setText(capitalUse.get(fundTypeCode));
		}

		// cash_remit.setText(LocalData.Currency.get(currencyCode) +
		// LocalData.CurrencyCashremit.get(cashRemit));
		// exchange_rate.setText(strIsNull(exchangeRate));

		// 正则表达式去掉小数点后无用的0
		if (exchangeRate.indexOf(".") < 0) {

			exchange_rate.setText(exchangeRate);
		} else {
			exchangeRate = exchangeRate.replaceAll("0+?$", "");// 去掉多余的0
			exchangeRate = exchangeRate.replaceAll("[.]$", "");// 如最后一位是.则去掉
			if (StringUtil.splitStringwith2pointnew(exchangeRate) < 4) {
				if (exchangeRate != null) {
					exchange_rate.setText(exchangeRate);
				} else {
					exchange_rate.setText("-");
				}

			} else {
				String str = StringUtil.splitStringwith2point(exchangeRate, 4);
				try {
					double d = Double.parseDouble(str);
					str = String.valueOf(d);
					if (exchangeRate != null) {
						exchange_rate.setText(str);
					} else {
						exchange_rate.setText("-");
					}

				} catch (Exception e) {
					exchange_rate.setText(str);
				}
			}
		}

	}

	/**
	 * @Title: requestForPsnFessQueryForErrormesg
	 * @Description: 历史交易记录查询失败原因
	 * @param
	 * @return void
	 */
	public void requestForPsnFessQueryForErrormesg(String tranRetCode) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(SBRemit.QUERYFORERRORMESG);
		// biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
		// .getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(SBRemit.TRANRETCODE, tranRetCode);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForPsnFessQueryForErrormesgCallBack");
	}

	/**
	 * @Title: requestForAccRemainCallBack
	 * @Description: 历史交易记录查询失败原因余额回调
	 * @param @param resultObj
	 * @return void
	 */

	@SuppressWarnings("unchecked")
	public void requestForPsnFessQueryForErrormesgCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		failureReason = (Map<String, Object>) biiResponseBody.getResult();
		foreign_currency_reason.setText((String) failureReason
				.get(SBRemit.TRANRETMESG));
		// 字太多显示不了，加上popupWindow
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				foreign_currency_reason);
		try {
			initViewInfos();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		fail_reason.setVisibility(View.GONE);
		handmade.setVisibility(View.GONE);
		super.onDestroy();
	}

}
