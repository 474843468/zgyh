package com.chinamworld.bocmbci.biz.lsforex.query;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.biz.lsforex.IsforexDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/**  当前有效委托查询与历史委托交易查询  详情页面*/
public class IsForexWTDetaiActivity extends IsForexBaseActivity {
	public static final String TAG = "IsForexWTDetaiActivity";
	/** activity View */
	private View queryView = null;
	/** backButton:返回按钮*/
	private Button backButton = null;

	private TextView wtNumber = null;
	private TextView jsCode = null;
	private TextView codeText = null;
	private TextView sellTag = null;
	private TextView jcTag = null;
	private TextView jyMoney = null;
	private TextView wtRate = null;
	private TextView wtType = null;
	private TextView jyStatus = null;
	private TextView jyQudao = null;
	private TextView wtTimes = null;
	private TextView sxTimes = null;
	private Button sureButton = null;
	private View buttonView = null;
	private int position = -1;
	private String settleCurrecny = null;
	private TextView cjTypeText = null;

	//wuhan  603
	private View zhijizhisun_layout;
	private TextView forex_zhuijidiancha;
	private String diancha;
	/**p603 建仓标识   --》tradeBackground**/
	private String tradeBackground = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		init();
		initData();
		initClick();
	}

	/** 初始化界面 */
	private void init() {
		queryView = LayoutInflater.from(IsForexWTDetaiActivity.this).inflate(R.layout.isforex_query_history_detail,
				null);
		tabcontent.addView(queryView);
		queryTag = (Integer) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.ISFOREX_QUERYTAG);
		/** 为界面标题赋值 */
		if (queryTag == 5) {
			setTitle(getResources().getString(R.string.query_title_five));
		} else if (queryTag == 6) {
			setTitle(getResources().getString(R.string.query_title_six));
		}
		
		//wuhan P603 追击止损委托
		zhijizhisun_layout = findViewById(R.id.zhijizhisun_layout);
		forex_zhuijidiancha = (TextView) findViewById(R.id.forex_zhuijidiancha);
		zhijizhisun_layout.setVisibility(View.GONE);
		
		buttonView = findViewById(R.id.ll_btn);
		backButton = (Button) findViewById(R.id.ib_back);
		sureButton = (Button) findViewById(R.id.forex_query_deal_detailes_ok);
		wtNumber = (TextView) findViewById(R.id.forex_trade_number);
		jsCode = (TextView) findViewById(R.id.forex_trade_sell);
		codeText = (TextView) findViewById(R.id.forex_trade_sell_code);
		sellTag = (TextView) findViewById(R.id.forex_trade_buy);
		jcTag = (TextView) findViewById(R.id.forex_trade_code);
		jyMoney = (TextView) findViewById(R.id.forex_trade_type);
		wtRate = (TextView) findViewById(R.id.forex_trade_times);
		wtType = (TextView) findViewById(R.id.forex_trade_wtType);
		jyStatus = (TextView) findViewById(R.id.forex_trade_jtStatus);
		jyQudao = (TextView) findViewById(R.id.forex_trade_jyQudao);
		wtTimes = (TextView) findViewById(R.id.forex_trade_wtTimes);
		sxTimes = (TextView) findViewById(R.id.forex_trade_sxTimes);
		cjTypeText = (TextView) findViewById(R.id.forex_trade_type1);
	}

	private void initData() {
		queryResultList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
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
		String consignNumber = (String) map.get(IsForex.ISFOREX_CONSIGNNUMBER_REQ);

		Map<String, String> currency1 = (Map<String, String>) map.get(IsForex.ISFOREX_CURRENCY1_REQ);
		Map<String, String> currency2 = (Map<String, String>) map.get(IsForex.ISFOREX_CURRENCY2_REQ);
		String code = null;
		if (!StringUtil.isNullOrEmpty(currency1) && !StringUtil.isNullOrEmpty(currency2)) {
			String code1 = currency1.get(IsForex.ISFOREX_CODE_REQ);
			String code2 = currency2.get(IsForex.ISFOREX_CODE_REQ);
			if (!StringUtil.isNull(code1) && !StringUtil.isNull(code2)) {
				String codes1 = LocalData.Currency.get(code1);
				String codes2 = LocalData.Currency.get(code2);
				code = codes1 + "/" + codes2;
			}
		}
		String direction = (String) map.get(IsForex.ISFOREX_DIRECTION_REQ);
		if (!StringUtil.isNull(direction)) {
			direction = LocalData.isForexdirectionMap.get(direction);
		}
		String openPositionFlag = (String) map.get(IsForex.ISFOREX_OPENPOSITIONFLAG_REQ);
		//p063
		Map<String, Object> lradeDetailMap = IsforexDataCenter.getInstance().getLradeDetailMap();
		String tradeBackground = (String)lradeDetailMap.get("tradeBackground");
		String amount = (String) map.get(IsForex.ISFOREX_AMOUNT_REQ);
		String customerRate = null;
		String firstCustomerRate = (String) map.get(IsForex.ISFOREX_FIRSTRATE_REQ);
		String secondCustomerRate = (String) map.get(IsForex.ISFOREX_SECONDRATE_REQ);
		String thirdCustomerRate = (String) map.get(IsForex.ISFOREX_THIRDRATE_REQ);
		if (!StringUtil.isNull(firstCustomerRate)) {
			customerRate = firstCustomerRate;
		} else {
			if (!StringUtil.isNull(secondCustomerRate)) {
				customerRate = secondCustomerRate;
			} else {
				if (!StringUtil.isNull(thirdCustomerRate)) {
					customerRate = thirdCustomerRate;
				}
			}
		}

		String exchangeTranType = (String) map.get(IsForex.ISFOREX_EXCHANGETRANTYPE_REQ);
		if (!StringUtil.isNull(exchangeTranType)) {
			exchangeTranType = LocalData.isForexExchangeTranType.get(exchangeTranType);
		}

		String orderStatus = (String) map.get(IsForex.ISFOREX_ORDERSTATUS_REQ);
		if (!StringUtil.isNull(orderStatus)) {
			// 判断撤单按钮是否显示buttonView
			if (ConstantGloble.ISFOREX_CD_N.equals(orderStatus)) {
				buttonView.setVisibility(View.VISIBLE);
			} else {
				buttonView.setVisibility(View.GONE);
			}
			orderStatus = LocalData.isForexOrderStatusMap.get(orderStatus);
		}

		String channelType = (String) map.get(IsForex.ISFOREX_CHANNELTYPE_REQ);
		if (!StringUtil.isNull(channelType)) {
			channelType = LocalData.isForexchannelTypeMap.get(channelType);
		}

		String paymentDate = (String) map.get(IsForex.ISFOREX_PAYMENTDATE_REQ);
		String dueDate = (String) map.get(IsForex.ISFOREX_DUEDATE_REQ);
		// 使用第一成交类型
		String tradeType = null;
		String firstType = (String) map.get(IsForex.ISFOREX_FIRSTTYPE_REQ);
		if (!StringUtil.isNull(firstType) && LocalData.isForexFirstTypeMap.containsKey(firstType)) {
			tradeType = LocalData.isForexFirstTypeMap.get(firstType);
		} else {
			tradeType = "-";
		}
		wtNumber.setText(consignNumber);
		if (StringUtil.isNull(settle)) {
			jsCode.setText("-");
		} else {
			jsCode.setText(settle);
		}

		codeText.setText(code);
		sellTag.setText(direction);
//		p603
//		jcTag.setText(openPositionFlag);
		tradeBackground = LocalData.mapTradeBack.get(tradeBackground);
		jcTag.setText(tradeBackground);
		if (!StringUtil.isNull(amount) && !StringUtil.isNull(settleCurrecny)) {
			if (LocalData.codeNoNumber.contains(settleCurrecny)) {
				amount = StringUtil.parseStringPattern(amount, twoNumber);
			} else {
				amount = StringUtil.parseStringPattern(amount, fourNumber);
			}
		}
		//追击止损委托 wuhan
		if(exchangeTranType.equals("FO")||exchangeTranType.equals("追击止损委托")){
			diancha = lradeDetailMap.get("foSet").toString();
			zhijizhisun_layout.setVisibility(View.VISIBLE);
			forex_zhuijidiancha.setText(diancha);
		}else{
			zhijizhisun_layout.setVisibility(View.GONE);
		}
		
		jyMoney.setText(amount);
		wtRate.setText(firstCustomerRate);
		wtType.setText(exchangeTranType);
		jyStatus.setText(orderStatus);
		jyQudao.setText(channelType);
		wtTimes.setText(paymentDate);
		if (StringUtil.isNull(dueDate)) {
			sxTimes.setText("-");
		} else {
			sxTimes.setText(dueDate);
		}
		cjTypeText.setText(tradeType);
	}

	private void initClick() {
		// 返回
		backButton.setOnClickListener(new OnClickListener() {

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
				Intent intent = new Intent(IsForexWTDetaiActivity.this, IsForexQuashConfirmActivity.class);
				intent.putExtra(ConstantGloble.ISFOREX_SELECTEDPOSITION, position);
				intent.putExtra(ConstantGloble.ISFOREX_VFGREGCODE, settleCurrecny);
				BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_WT_TAG, queryTag);
				startActivityForResult(intent, ConstantGloble.ISFOREX_WT_ACTIVITY);
				overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
			}
		});
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
}
