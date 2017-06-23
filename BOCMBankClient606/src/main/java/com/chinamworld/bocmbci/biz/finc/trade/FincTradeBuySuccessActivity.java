package com.chinamworld.bocmbci.biz.finc.trade;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 基金买入成功页面
 * 
 * @author xyl
 * 
 */
public class FincTradeBuySuccessActivity extends FincBaseActivity {
	private static final String TAG = "FincTradeBuySuccessActivity";
	private String buyAmountStr;
	private String fundCodeStr;
	private String fundNameStr;
	private String feeTypeStr;
	private String tradeCurrencyStr;
	private String fundSeqStr;
	private String transIdStr;
	private String fundStateStr;// 基金状态

	private TextView tradeIdTv;
	private TextView fundaccNumTv;
	private TextView accNumTv;
	private TextView fundSeqTv;
	private TextView fundNameTv;
	private TextView fundCodeTv;
	private TextView fundStateTv;
	private TextView feeTypeTv;
	private TextView fundCurrencyTv;
	private TextView buyAmountTv;
	/**执行方式*/
	private LinearLayout finc_executeType_layout;
	private TextView finc_executeType_tv;
	/**执行日期*/
	private LinearLayout finc_exeDate_layout;
	private TextView finc_exeDate_tv;
	private Button confirmBtn;

	private String assignedDateStr = null;
	private String cashFlagCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initData();
	}

	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	private void init() {
		View childview = mainInflater.inflate(R.layout.finc_trade_buy_success,
				null);
		tabcontent.addView(childview);
		setTitle(R.string.finc_title_buy);
		tradeIdTv = (TextView) childview.findViewById(R.id.finc_tradeId_tv);
		fundaccNumTv = (TextView) childview.findViewById(R.id.finc_accNum_tv);// 基金账户号码
		accNumTv = (TextView) childview.findViewById(R.id.finc_zjAccId_tv);// 资金账户号码
		fundSeqTv = (TextView) childview.findViewById(R.id.finc_fundseq_colon);// 基金交易流水号
		fundCodeTv = (TextView) childview
				.findViewById(R.id.finc_fundCode_textview);
		fundNameTv = (TextView) childview
				.findViewById(R.id.finc_fundName_textview);
		fundStateTv = (TextView) childview.findViewById(R.id.finc_fundstate_tv);
		feeTypeTv = (TextView) childview
				.findViewById(R.id.finc_feetype_textView);
		fundCurrencyTv = (TextView) childview
				.findViewById(R.id.finc_currency_tv);
		buyAmountTv = (TextView) childview.findViewById(R.id.finc_buyamount_tv);
		/**执行方式*/
		finc_executeType_layout = (LinearLayout) childview.findViewById(R.id.finc_executeType_layout);
		finc_executeType_tv = (TextView) childview.findViewById(R.id.finc_executeType_tv);
		/**执行日期*/
		finc_exeDate_layout = (LinearLayout) childview.findViewById(R.id.finc_exeDate_layout);
		finc_exeDate_tv = (TextView) childview.findViewById(R.id.finc_exeDate_tv);
		
		confirmBtn = (Button) childview.findViewById(R.id.finc_confirm);
		back.setVisibility(View.INVISIBLE);
		backImage.setVisibility(View.INVISIBLE);
		confirmBtn.setOnClickListener(this);
		initRightBtnForMain();
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fundNameTv);
	}

	private void initData() {
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		buyAmountStr = extras.getString(Finc.I_BUYAMOUNT);
		fundCodeStr = extras.getString(Finc.I_FUNDCODE);
		fundNameStr = extras.getString(Finc.I_FUNDNAME);
		feeTypeStr = extras.getString(Finc.I_FEETYPE);
		tradeCurrencyStr = extras.getString(Finc.I_CURRENCYCODE);
//		cashFlagCode  =extras.getString(Finc.I_CASHFLAG);
		cashFlagCode = (String) fincControl.fundDetails
				.get(Finc.FINC_CASHFLAG);
		fundSeqStr = extras.getString(Finc.I_FINCBUYSQL);
		transIdStr = extras.getString(Finc.I_TRANSACTIONID);
		fundStateStr = extras.getString(Finc.I_FUNDSTATE);
		assignedDateStr = extras.getString(Finc.I_ASSIGNEDDATE);
		if(assignedDateStr == null){
			finc_executeType_layout.setVisibility(View.GONE);
			finc_exeDate_layout.setVisibility(View.GONE);
		}else{
			/**执行方式*/
			finc_executeType_layout.setVisibility(View.VISIBLE);
			/**执行日期*/
			finc_exeDate_layout.setVisibility(View.VISIBLE);
			finc_exeDate_tv.setText(assignedDateStr);

		}

		tradeIdTv.setText(transIdStr);
		if(StringUtil.isNullOrEmpty(fundSeqStr)){
			fundSeqTv.setText("-");
		}else{
			fundSeqTv.setText(fundSeqStr);
		}
		fundCodeTv.setText(fundCodeStr);
		fundNameTv.setText(fundNameStr);
		fundStateTv.setText(LocalData.fincFundStateCodeToStr.get(fundStateStr));
		feeTypeTv.setText(LocalData.fundfeeTypeCodeToStr.get(feeTypeStr));
		fundCurrencyTv.setText(FincControl.fincCurrencyAndCashFlag(tradeCurrencyStr, cashFlagCode));//如果不是人民币   显示币种和炒汇
		buyAmountTv.setText(StringUtil.parseStringCodePattern(tradeCurrencyStr, 
				buyAmountStr,2));
		accNumTv.setText(StringUtil.getForSixForString(fincControl.accNum));
		fundaccNumTv.setText(fincControl.invAccId);
	
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_confirm:
			finish();
			break;
		default:
			break;
		}
	}
	@Override
	public void finish() {
		setResult(RESULT_OK);
		super.finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
