package com.chinamworld.bocmbci.biz.lsforex.query;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsforexDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 委托撤单确认页面 */
public class IsForexQuashConfirmActivity extends BaseActivity {
	public static final String TAG = "IsForexQuashConfirmActivity";
	/** 主视图布局 */
	private LinearLayout tabcontent;// 主Activity显示
	/** activity View */
	private View queryView = null;
	/** backButton:返回按钮 */
	private Button backButton = null;

	private TextView wtNumber = null;
	private TextView jsCode = null;
	private TextView codeText = null;
	private TextView sellTag = null;
	private TextView jcTag = null;
	private TextView jyMoney = null;
	private TextView wtType = null;
	private TextView jyQudao = null;
	private TextView wtTimes = null;
	private TextView sxTimes = null;
	private Button sureButton = null;
	private int position = -1;
	/** 结算币种代码 */
	private String settleCurrecny = null;
	/** 委托序号 */
	private String consignNumber = null;
	/** 货币对 */
	private String code1 = null;
	/** 货币对 */
	private String code2 = null;
	/** 买卖方向 */
	private String direction = null;
	/** 建仓标识 */
	private String openPositionFlag = null;
	
	/** 交易金额 */
	private String amount = null;
	/** 委托类型 */
	private String exchangeTranType = null;
	/** 委托时间 */
	private String paymentDate = null;
	/** 失效时间 */
	private String dueDate = null;
	/** 用于区分是那种交易查询 1-成交状况查询，2-斩仓交易查询，3-未平仓交易查询，4-对账单查询,5-当前委托，6-历史委托 */
	public int queryTag = 1;
	private String commConversationId = null;
	private Button quickTrade = null;
	private TextView cjTypeText = null;
	
	/**p603 建仓标识   --》tradeBackground**/
	private String tradeBackground = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setContentView(R.layout.biz_activity_606_layout);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		View footView = findViewById(R.id.rl_menu);
		footView.setVisibility(View.GONE);
		Button leftButton = (Button) findViewById(R.id.btn_show);
		leftButton.setVisibility(View.GONE);
		quickTrade = (Button) findViewById(R.id.ib_top_right_btn);
		quickTrade.setVisibility(View.VISIBLE);
		quickTrade.setText(getResources().getString(R.string.forex_rate_close));
		// 初始化弹窗按钮
		initPulldownBtn();
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 20);
		}
		init();
		initData();
		initOnClick();
	}

	/** 初始化界面 */
	private void init() {
		queryView = LayoutInflater.from(IsForexQuashConfirmActivity.this).inflate(R.layout.isforex_wt_quash_confirm, null);
		tabcontent.addView(queryView);
		queryTag = (Integer) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.ISFOREX_QUERYTAG);
		/** 为界面标题赋值 */
		setTitle(getResources().getString(R.string.forex_quash_title));
		backButton = (Button) findViewById(R.id.ib_back);
		backButton.setVisibility(View.GONE);
		sureButton = (Button) findViewById(R.id.sureButton);
		wtNumber = (TextView) findViewById(R.id.forex_trade_number);
		jsCode = (TextView) findViewById(R.id.forex_trade_sell);
		codeText = (TextView) findViewById(R.id.forex_trade_sell_code);
		sellTag = (TextView) findViewById(R.id.forex_trade_buy);
		jcTag = (TextView) findViewById(R.id.forex_trade_code);
		jyMoney = (TextView) findViewById(R.id.forex_trade_type);
		wtType = (TextView) findViewById(R.id.forex_trade_wtType);
		jyQudao = (TextView) findViewById(R.id.forex_trade_jyQudao);
		wtTimes = (TextView) findViewById(R.id.forex_trade_wtTimes);
		sxTimes = (TextView) findViewById(R.id.forex_trade_sxTimes);
		cjTypeText = (TextView) findViewById(R.id.forex_trade_type1);
	}

	private void initData() {
		List<Map<String, Object>> queryResultList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(IsForex.ISFOREX_LIST_REQ);
		if (queryResultList.size() <= 0 || queryResultList == null) {
			return;
		}
		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		settleCurrecny = intent.getStringExtra(ConstantGloble.ISFOREX_VFGREGCODE);
		String settle = null;
		if (!StringUtil.isNull(settleCurrecny) && LocalData.Currency.containsKey(settleCurrecny)) {
			settle = LocalData.Currency.get(settleCurrecny);
		}
		position = intent.getIntExtra(ConstantGloble.ISFOREX_SELECTEDPOSITION, -1);
		if (position == -1) {
			return;
		}

		Map<String, Object> map = queryResultList.get(position);
		consignNumber = (String) map.get(IsForex.ISFOREX_CONSIGNNUMBER_REQ);

		Map<String, String> currency1 = (Map<String, String>) map.get(IsForex.ISFOREX_CURRENCY1_REQ);
		Map<String, String> currency2 = (Map<String, String>) map.get(IsForex.ISFOREX_CURRENCY2_REQ);
		String code = null;
		if (!StringUtil.isNullOrEmpty(currency1) && !StringUtil.isNullOrEmpty(currency2)) {
			code1 = currency1.get(IsForex.ISFOREX_CODE_REQ);
			code2 = currency2.get(IsForex.ISFOREX_CODE_REQ);
			if (!StringUtil.isNull(code1) && !StringUtil.isNull(code2)) {
				String codes1 = LocalData.Currency.get(code1);
				String codes2 = LocalData.Currency.get(code2);
				code = codes1 + "/" + codes2;
			}
		}
		direction = (String) map.get(IsForex.ISFOREX_DIRECTION_REQ);
		String directions = null;
		if (!StringUtil.isNull(direction)) {
			directions = LocalData.isForexdirectionMap.get(direction);
		}
		openPositionFlag = (String) map.get(IsForex.ISFOREX_OPENPOSITIONFLAG_REQ);

		amount = (String) map.get(IsForex.ISFOREX_AMOUNT_REQ);
		exchangeTranType = (String) map.get(IsForex.ISFOREX_EXCHANGETRANTYPE_REQ);
		String type = null;
		if (!StringUtil.isNull(exchangeTranType)) {
			type = LocalData.isForexExchangeTranType.get(exchangeTranType);
		}
		String channelType = (String) map.get(IsForex.ISFOREX_CHANNELTYPE_REQ);
		if (!StringUtil.isNull(channelType)) {
			channelType = LocalData.isForexchannelTypeMap.get(channelType);
		}

		paymentDate = (String) map.get(IsForex.ISFOREX_PAYMENTDATE_REQ);
		dueDate = (String) map.get(IsForex.ISFOREX_DUEDATE_REQ);

		wtNumber.setText(consignNumber);
		if (StringUtil.isNull(settle)) {
			jsCode.setText("-");
		} else {
			jsCode.setText(settle);
		}
		// 使用第一成交类型
		String tradeType = null;
		String firstType = (String) map.get(IsForex.ISFOREX_FIRSTTYPE_REQ);
		if (!StringUtil.isNull(firstType) && LocalData.isForexFirstTypeMap.containsKey(firstType)) {
			tradeType = LocalData.isForexFirstTypeMap.get(firstType);
		} else {
			tradeType = "-";
		}
		codeText.setText(code);
		sellTag.setText(directions);
		jcTag.setText(openPositionFlag);
		Map<String, Object> lradeDetailMap = IsforexDataCenter.getInstance().getLradeDetailMap();
		tradeBackground = (String) lradeDetailMap.get("tradeBackground");
		//p603
		tradeBackground = LocalData.mapTradeBack.get(tradeBackground);
		jcTag.setText(tradeBackground);
		String amounts = null;
		if (!StringUtil.isNull(amount) && !StringUtil.isNull(settleCurrecny)) {
			if (LocalData.codeNoNumber.contains(settleCurrecny)) {
				amounts = StringUtil.parseStringPattern(amount, 0);
			} else {
				amounts = StringUtil.parseStringPattern(amount, 2);
			}
		}
		jyMoney.setText(amounts);
		wtType.setText(type);
		jyQudao.setText(channelType);
		wtTimes.setText(paymentDate);
		if (StringUtil.isNull(dueDate)) {
			sxTimes.setText("-");
		} else {
			sxTimes.setText(dueDate);
		}
		cjTypeText.setText(tradeType);
	}

	private void initOnClick() {
		quickTrade.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		// 撤单按钮
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
		});
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		requestPSNGetTokenId(commConversationId);
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String pDate = QueryDateUtils.getcurrentDate(paymentDate);
		String dDate = QueryDateUtils.getcurrentDate(dueDate);
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		requestPsnVFGCancelOrder(consignNumber, settleCurrecny, code1, code2, direction, openPositionFlag, amount,
				exchangeTranType, pDate, dDate, token);
	}

	/**
	 * 委托撤单：
	 * 
	 * @param consignNumber
	 *            ：委托序号
	 * @param currencyCode
	 *            ：结算币种
	 * @param currencycode1
	 *            ：货币对
	 * @param currencycode2
	 *            ：货币对
	 * @param direction
	 *            ：：买卖方向
	 * @param openPositionFlag
	 *            ：建仓标识
	 * @param amount
	 *            : 交易金额
	 * @param exchangeTranType
	 *            :委托类型
	 * @param paymentDate
	 *            :委托时间
	 * @param dueDate
	 *            : 失效时间
	 * @param token
	 *            ： 防重标志
	 */
	private void requestPsnVFGCancelOrder(String consignNumber, String currencyCode, String currencycode1,
			String currencycode2, String direction, String openPositionFlag, String amount, String exchangeTranType,
			String paymentDate, String dueDate, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGCANCELORDER_API);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> map = new HashMap<String, String>();
		map.put(IsForex.ISFOREX_CONSIGNNUMBER_REQ2, consignNumber);
		map.put(IsForex.ISFOREX_CURRENCYCODE_REQ2, currencyCode);
		map.put(IsForex.ISFOREX_CURRENCYCODE1_REQ2, currencycode1);
		map.put(IsForex.ISFOREX_CURRENCYCODE2_REQ2, currencycode2);
		map.put(IsForex.ISFOREX_DIRECTION_REQ2, direction);
		map.put(IsForex.ISFOREX_OPENPOSITIONFLAG_REQ2, openPositionFlag);
		map.put(IsForex.ISFOREX_AMOUNT_REQ2, amount);
		map.put(IsForex.ISFOREX_EXCHANGETRANTYPE_REQ2, exchangeTranType);
		map.put(IsForex.ISFOREX_PAYMENTDATE_REQ2, paymentDate);
		map.put(IsForex.ISFOREX_DUEDATE_REQ2, dueDate);
		map.put(IsForex.ISFOREX_TOKEN, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGCancelOrderCallback");
	}

	public void requestPsnVFGCancelOrderCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> result = (Map<String, String>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		String exchangeTransDate = result.get(IsForex.ISFOREX_EXCHANGETRANSDATE_RES);
		String transactionId = result.get(IsForex.ISFOREX_TRANSACTIONID_RES1);
		if (StringUtil.isNull(exchangeTransDate) || StringUtil.isNull(transactionId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent(this, IsForexQuashSuccessActivity.class);
		intent.putExtra(ConstantGloble.ISFOREX_SELECTEDPOSITION, position);
		intent.putExtra(ConstantGloble.ISFOREX_VFGREGCODE, settleCurrecny);
		intent.putExtra(IsForex.ISFOREX_EXCHANGETRANSDATE_RES, exchangeTransDate);
		intent.putExtra(IsForex.ISFOREX_TRANSACTIONID_RES1, transactionId);
		startActivityForResult(intent, ConstantGloble.ISFOREX_WT_ACTIVITY);
		overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.no_animation, R.anim.slide_down_out);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ConstantGloble.ISFOREX_WT_ACTIVITY:
			switch (resultCode) {
			case RESULT_OK:
				setResult(RESULT_OK);
				finish();
				break;
			case RESULT_CANCELED:
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.TwoTask;
	}
}
