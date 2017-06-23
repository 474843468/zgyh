package com.chinamworld.bocmbci.biz.bocinvt.dealhistory;

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
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 历史产品详情页面
 * 
 * @author wangmengmeng
 * 
 */
public class HistoryProductDetailActivity extends BociBaseActivity {
	/** 历史产品详情页 */
	private View view;
	// ////////////////////////////////////////
	/** 产品详情 */
	private Map<String, Object> historyDetailMap;
	/** 交易日期 */
	private TextView tv_paymentDate_detail;
	/** 产品代码 */
	private TextView tv_prodCode_detail;
	/** 产品名称 */
	private TextView tv_prodName_detail;
	/** 交易类型 */
	private TextView tv_trfType_detail;
	/** 产品币种 */
	private TextView tv_curCode_detail;
	/** 交易份额 */
	private TextView tv_histrfAmount_detail;
	/** 成交价格 */
	private TextView tv_cjAmount_detail;
	/** 交易金额 */
	private TextView tv_payPrice_detail;
	/** 渠道 */
	private TextView tv_channel_detail;
	/** 状态 */
	private TextView tv_status_detail;
	/** 交易属性 */
	private TextView tv_tranAtrr_detail;
	/** 撤单 */
	private Button btn_canceled;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getResources().getString(R.string.bocinvt_historyquery_titile));
		setText(this.getString(R.string.go_main));
		// 添加布局
		view = addView(R.layout.bocinvt_myproduct_history_detail);
		init();
		setBackBtnClick(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		setRightBtnClick(rightBtnClick);
	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 回主页面
			ActivityTaskManager.getInstance().removeAllSecondActivity();
		}
	};

	/** 初始化界面 */
	@SuppressWarnings("unchecked")
	private void init() {
		historyDetailMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_HISDETAIL_MAP);

		tv_paymentDate_detail = (TextView) view
				.findViewById(R.id.tv_paymentDate_detail);
		tv_prodCode_detail = (TextView) view
				.findViewById(R.id.tv_prodCode_detail);
		tv_prodName_detail = (TextView) view
				.findViewById(R.id.tv_prodName_detail);
		tv_trfType_detail = (TextView) view
				.findViewById(R.id.tv_trfType_detail);
		tv_curCode_detail = (TextView) view
				.findViewById(R.id.tv_curCode_detail);
		tv_histrfAmount_detail = (TextView) view
				.findViewById(R.id.tv_histrfAmount_detail);
		tv_cjAmount_detail = (TextView) view
				.findViewById(R.id.tv_cjAmount_detail);
		tv_payPrice_detail = (TextView) view
				.findViewById(R.id.tv_payPrice_detail);
		tv_channel_detail = (TextView) view
				.findViewById(R.id.tv_channel_detail);
		tv_status_detail = (TextView) view.findViewById(R.id.tv_status_detail);
		tv_tranAtrr_detail = (TextView) view
				.findViewById(R.id.tv_tranAtrr_detail);
		// 赋值
		/** 交易日期 */
		tv_paymentDate_detail.setText(String.valueOf(historyDetailMap
				.get(BocInvt.BOCINCT_XPADTRAD_PAYMENTDATE_RES)));
		/** 产品代码 */
		tv_prodCode_detail.setText(String.valueOf(historyDetailMap
				.get(BocInvt.BOCINCT_XPADTRAD_PRODCODE_RES)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodCode_detail);
		/** 产品名称 */
		tv_prodName_detail.setText(String.valueOf(historyDetailMap
				.get(BocInvt.BOCINCT_XPADTRAD_PRODNAME_RES)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodName_detail);
		/** 交易类型 */
		tv_trfType_detail.setText(LocalData.bociTrfTypeMap.get(String
				.valueOf(historyDetailMap
						.get(BocInvt.BOCINCT_XPADTRAD_TRFTYPE_RES))));
		String currency = String.valueOf(historyDetailMap
				.get(BocInvt.BOCINCT_XPADTRAD_CURRENCYCODE_RES));
		if (!StringUtil.isNull(LocalData.Currency.get(currency))) {
			if (LocalData.Currency.get(currency).equals(ConstantGloble.ACC_RMB)) {
				/** 产品币种 */
				tv_curCode_detail.setText(LocalData.Currency.get(currency));
			} else {
				tv_curCode_detail
						.setText(LocalData.Currency.get(currency)
								+ LocalData.cashMapValue.get(String.valueOf(historyDetailMap
										.get(BocInvt.BOCINCT_XPADTRAD_CASHREMIT_RES))));
			}
		}
		/** 交易份额 */
		String histrfAmount = (String) historyDetailMap
				.get(BocInvt.BOCINCT_XPADTRAD_TRFAMOUNT_RES);
		tv_histrfAmount_detail.setText(StringUtil.parseStringPattern(
				histrfAmount, 2));
		/** 成交价格 */
		String cjAmount = (String) historyDetailMap
				.get(BocInvt.BOCINCT_XPADTRAD_TRFPRICE_RES);
		tv_cjAmount_detail.setText(StringUtil.parseStringCodePattern(currency,
				cjAmount, 2));
		/** 交易金额 */
		String payPrice = (String) historyDetailMap
				.get(BocInvt.BOCINCT_XPADTRAD_AMOUNT_RES);
		tv_payPrice_detail.setText(StringUtil.parseStringCodePattern(currency,
				payPrice, 2));
		/** 渠道 */
		tv_channel_detail.setText(LocalData.bociChannelMap.get(String
				.valueOf(historyDetailMap
						.get(BocInvt.BOCINCT_XPADTRAD_CHANNEL_RES))));
		/** 状态 */
		tv_status_detail.setText(LocalData.bociHisStatusMap.get(String
				.valueOf(historyDetailMap
						.get(BocInvt.BOCINCT_XPADTRAD_STATUS_RES))));
		/** 交易属性 */
		tv_tranAtrr_detail.setText(LocalData.bociTranAtrrMap
				.get((String) historyDetailMap
						.get(BocInvt.BOCINCT_XPADTRAD_TRANATRR_RES)));

		btn_canceled = (Button) view.findViewById(R.id.btn_canceled);
//		String status = (String) historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_STATUS_RES);
//		if (!StringUtil.isNull(status) && status.equals("0")) {
//			btn_canceled.setVisibility(View.VISIBLE);
//		}
		if (canBeCanceled()) {
			btn_canceled.setVisibility(View.VISIBLE);	
		}
		btn_canceled.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断是否可撤单
				if (canBeCanceled()) {
					// 可撤单
					Intent intent = new Intent(
							HistoryProductDetailActivity.this,
							ProductCanceledActivity.class);
					startActivityForResult(intent, ACTIVITY_BUY_CODE);
				}else{
					BaseDroidApp
					.getInstanse()
					.showInfoMessageDialog(
							HistoryProductDetailActivity.this
									.getString(R.string.bocinvt_error_canceled));
				}
			}
		});
	}
	
	/**
	 * 判断是否可撤单
	 * @return
	 */
	private boolean canBeCanceled(){
		String cancancel = (String) historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_CANBECANCELED_RES);
		if (StringUtil.isNullOrEmpty(cancancel)) {
			return false;
		}else if(cancancel.equals("true") || cancancel.equals("0")){
			return true;
		}else if(cancancel.equals("false") || cancancel.equals("1")){
			return false;
		}else {
			return false;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
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
	}
}
