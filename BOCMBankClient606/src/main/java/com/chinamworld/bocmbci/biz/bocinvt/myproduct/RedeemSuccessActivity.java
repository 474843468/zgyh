package com.chinamworld.bocmbci.biz.bocinvt.myproduct;

import java.util.Map;

import android.content.Intent;
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
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 产品赎回成功页面
 * 
 * @author wangmengmeng
 * 
 */
public class RedeemSuccessActivity extends BociBaseActivity {
	/** 赎回成功页面 */
	private View view;
	/** 赎回结果信息 */
	private Map<String, Object> redeemMap;
	/** 交易序号 */
	private TextView tv_tranSeq_detail;
	/** 交易日期 */
	private TextView tv_paymentDate;
	/** 产品代码 */
	private TextView tv_prodCode_detail;
	/** 产品名称 */
	private TextView tv_prodName_detail;
	/** 币种 */
	private TextView tv_currency_detail;
	/** 赎回金额 */
	private TextView tv_trfPrice_detail;
	/** 赎回份额 */
	private TextView tv_redeemQuantity_detail;
	/** 确定 */
	private Button btn_ok;
	/** 产品信息 */
	private Map<String, Object> myproductMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.boci_redeem_title));
		// 添加布局
		view = addView(R.layout.bocinvt_redeem_success);
		// 界面初始化
		init();
		setRightBtnClick(rightBtnClick);
	}

	private void init() {
		redeemMap = BociDataCenter.getInstance().getRedeemSuccessMap();
		back.setVisibility(View.GONE);
		// 步骤条
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources().getString(
								R.string.bocinvt_redeem_step1),
						this.getResources().getString(
								R.string.bocinvt_redeem_step2),
						this.getResources().getString(
								R.string.bocinvt_redeem_step3) });
		StepTitleUtils.getInstance().setTitleStep(3);
		myproductMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_MYPRODUCT_LIST);
		tv_tranSeq_detail = (TextView) view
				.findViewById(R.id.tv_tranSeq_detail);
		tv_paymentDate = (TextView) view.findViewById(R.id.tv_paymentDate);
		tv_prodCode_detail = (TextView) view
				.findViewById(R.id.tv_prodCode_detail);
		tv_prodName_detail = (TextView) view
				.findViewById(R.id.tv_prodName_detail);
		tv_currency_detail = (TextView) view
				.findViewById(R.id.tv_currency_detail);
		tv_trfPrice_detail = (TextView) view
				.findViewById(R.id.tv_trfPrice_detail);
		tv_redeemQuantity_detail = (TextView) view
				.findViewById(R.id.tv_redeemQuantity_detail);
		btn_ok = (Button) view.findViewById(R.id.btn_ok);
		// 赋值
		/** 交易序号 */
		tv_tranSeq_detail.setText(String.valueOf(redeemMap
				.get(BocInvt.BOCINVT_REDEEM_TRANSACTIONID_RES)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_tranSeq_detail);
		/** 交易日期 */
		tv_paymentDate.setText(String.valueOf(redeemMap
				.get(BocInvt.BOCINVT_REDEEM_PAYMENTDATE_RES)));
		/** 产品代码 */
		tv_prodCode_detail.setText(String.valueOf(redeemMap
				.get(BocInvt.BOCINVT_REDEEM_PRODCODE_RES)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodCode_detail);
		/** 产品名称 */
		tv_prodName_detail.setText(String.valueOf(redeemMap
				.get(BocInvt.BOCINVT_REDEEM_PRODNAME_RES)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				BaseDroidApp.getInstanse().getCurrentAct(), tv_prodName_detail);
		/** 币种 */
		String currency = (String) redeemMap
				.get(BocInvt.BOCINVT_REDEEM_CURRENCYCODE_RES);
		/** 产品币种 */
		if (!StringUtil.isNull(LocalData.Currency.get(currency))) {
			if (LocalData.Currency.get(currency).equals(ConstantGloble.ACC_RMB)) {
				/** 产品币种 */
				tv_currency_detail.setText(LocalData.Currency.get(currency));
			} else {
				tv_currency_detail
						.setText(LocalData.Currency.get(currency)
								+ BociDataCenter.cashMapValue.get(String.valueOf(myproductMap
										.get(BocInvt.BOCINVT_HOLDPRO_CASHREMIT_RES))));
			}
		}
		/** 赎回金额 */
		String trfPrice = (String) redeemMap
				.get(BocInvt.BOCINVT_REDEEM_REDEEMAMOUNT_RES);
		tv_trfPrice_detail.setText(StringUtil.parseStringCodePattern(currency,
				trfPrice, 2));
		/** 赎回份额 */
		String redeemQuantity = (String) redeemMap
				.get(BocInvt.BOCINVT_REDEEM_TRFAMOUNT_RES);
		tv_redeemQuantity_detail.setText(StringUtil.parseStringPattern(
				redeemQuantity, 2));
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(RedeemSuccessActivity.this,
						MyProductListActivity.class);
				startActivity(intent);
				ActivityTaskManager.getInstance().removeAllSecondActivity();
			}
		});
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
