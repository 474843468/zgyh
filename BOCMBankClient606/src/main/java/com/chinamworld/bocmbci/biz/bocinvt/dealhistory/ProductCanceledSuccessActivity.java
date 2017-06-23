package com.chinamworld.bocmbci.biz.bocinvt.dealhistory;

import java.util.Map;

import android.os.Bundle;
import android.view.KeyEvent;
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
 * 历史查询产品撤单成功页
 * 
 * @author wangmengmeng
 * 
 */
public class ProductCanceledSuccessActivity extends BociBaseActivity {
	/** 历史产品撤单确认页 */
	private View view;
	/** 产品详情 */
	private Map<String, Object> historyDetailMap;
	/** 撤单确认结果 */
	private Map<String, Object> xpadCanceledMap;
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
	// 按钮//////////////////////////////////
	/** 确定 */
	private Button btn_canceled;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getResources().getString(R.string.bocinvt_cancel_titile));
		// 添加布局
		view = addView(R.layout.bocinvt_myproduct_history_canceled_success);
		init();

	}

	/** 初始化界面 */
	private void init() {
		back.setVisibility(View.GONE);
		historyDetailMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_HISDETAIL_MAP);
		xpadCanceledMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_XPAD_CANCELED_MAP);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
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
		btn_canceled = (Button) view.findViewById(R.id.btn_canceled);
		btn_canceled.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});
		// 赋值
		/** 交易序号 */
		tv_paymentDate_detail.setText(String.valueOf(xpadCanceledMap
				.get(BocInvt.BOCINVT_CANCEL_TRANSEQ_RES)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_paymentDate_detail);
		/** 产品代码 */
		tv_prodCode_detail.setText(String.valueOf(xpadCanceledMap
				.get(BocInvt.BOCINVT_CANCEL_PRODCODE_RES)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodCode_detail);
		/** 产品名称 */
		tv_prodName_detail.setText(String.valueOf(xpadCanceledMap
				.get(BocInvt.BOCINVT_CANCEL_PRODNAME_RES)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodName_detail);
		/** 交易类型 */
		tv_trfType_detail.setText(LocalData.bociTrfTypeMap.get(String
				.valueOf(xpadCanceledMap
						.get(BocInvt.BOCINVT_CANCEL_TRFTYPE_RES))));
		/** 产品币种 */
		String currency = String.valueOf(historyDetailMap
				.get(BocInvt.BOCINCT_XPADTRAD_CURRENCYCODE_RES));
		if (!StringUtil.isNull(LocalData.Currency.get(currency))) {
			if (LocalData.Currency.get(currency).equals(ConstantGloble.ACC_RMB)) {
				/** 产品币种 */
				tv_curCode_detail.setText(LocalData.Currency.get(currency));
			} else {
				String cash = String.valueOf(historyDetailMap
						.get(BocInvt.BOCINCT_XPADTRAD_CASHREMIT_RES));
				cash = StringUtil.isNull(cash) ? ConstantGloble.BOCINVT_DATE_ADD
						: cash;
				tv_curCode_detail.setText(LocalData.Currency.get(currency)
						+ LocalData.CurrencyCashremit.get(cash));
			}
		}
		/** 交易份额 */
		String histrfAmount = (String) xpadCanceledMap
				.get(BocInvt.BOCINVT_CANCEL_TRFAMOUNT_RES);
		tv_histrfAmount_detail.setText(StringUtil.parseStringPattern(
				histrfAmount, 2));
		/** 成交价格 */
		String cjAmount = (String) xpadCanceledMap
				.get(BocInvt.BOCINVT_CANCEL_TRFPRICE_RES);
		tv_cjAmount_detail.setText(StringUtil.parseStringCodePattern(currency,
				cjAmount, 2));
		/** 交易金额 */
		String payPrice = (String) historyDetailMap
				.get(BocInvt.BOCINCT_XPADTRAD_AMOUNT_RES);
		tv_payPrice_detail.setText(StringUtil.parseStringCodePattern(currency,
				payPrice, 2));
	}

	/** 右上角按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ActivityTaskManager.getInstance().removeAllSecondActivity();
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 屏蔽返回键
			return false;
		}
		return super.onKeyDown(keyCode, event);

	}
}
