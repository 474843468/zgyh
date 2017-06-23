package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCProductForHoldingInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.util.HoldingBOCProductInfoTransformUtil;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.util.HoldingBOCProductInfoUtil;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.util.Trans2ChineseNumber;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.IHttpResponseCallBack;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.userwidget.LabelTextView.TextColor;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 业绩基准型产品份额转换页面
 * 
 * @author HVZHUNG
 *
 */
public class QuantityTransitionActivity extends BocInvtBaseActivity {

	private static final String KEY_PRODUCT_INFO = "product_info";
	/** 产品信息 */
	private Map<String, Object> myproductMap;
	/** 产品代码 */
	private LabelTextView ltv_product_code;
	/** 产品名称 */
	private LabelTextView ltv_product_name;
	/** 产品币种 */
	private LabelTextView ltv_currency;
	/** 钞/汇 */
	private LabelTextView ltv_cash_remittance;
	/** 预计年收益率 */
	private LabelTextView ltv_estimate_apy;
	/** 剩余额度 */
	private LabelTextView ltv_residue_limit;
	/** 单日首次购买起点金额 */
	private LabelTextView ltv_starting_amount_for_first_day;
	/** 单日追加购买起点金额 */
	private LabelTextView ltv_starting_amount_for_superaddition_day;
	/** 购买金额基数 */
	private LabelTextView ltv_purchase_base;
	/** 是否允许撤单 */
	private LabelTextView ltv_can_revoke;
	/** 理财交易账户 */
	private LabelTextView ltv_account;
	/** 可用份额 */
	private LabelTextView ltv_enable_quantity;
	/** 持有份额 */
	private LabelTextView ltv_holding_quantity;
	private BOCProductForHoldingInfo info;
	/** 产品详情 */
	private Map<String, Object> responseDeal;
	/** 转换份额 */
	private EditText et_exchange_number;
	/** 大写显示 */
	private TextView tv_chinese_numeral;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bocinvt_quantity_transition_activity_p603);
		setTitle(R.string.bocinvt_holding_detail_lot_transform);
		// getBackgroundLayout().setRightButtonText(null);
		// 界面初始化
		init();
	}

	@SuppressWarnings("unchecked")
	private void init() {
		myproductMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap()
				.get(ConstantGloble.BOCINVT_QUANTITY_DETAIL_COMBINE_MAP);
		responseDeal = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_PRODUCT_DETAIL_MAP);
		info = HoldingBOCProductInfoTransformUtil
				.map2BocProductInfo(myproductMap);
		((BocInvestControl)BocInvestControl.getInstance()).curBOCProductForHoldingInfo = info;
		ltv_product_code = (LabelTextView) findViewById(R.id.ltv_product_code);
		ltv_product_name = (LabelTextView) findViewById(R.id.ltv_product_name);
		ltv_currency = (LabelTextView) findViewById(R.id.ltv_currency);
		ltv_cash_remittance = (LabelTextView) findViewById(R.id.ltv_cash_remittance);
		ltv_estimate_apy = (LabelTextView) findViewById(R.id.ltv_estimate_apy);
		ltv_residue_limit = (LabelTextView) findViewById(R.id.ltv_residue_limit);
		ltv_starting_amount_for_first_day = (LabelTextView) findViewById(R.id.ltv_starting_amount_for_first_day);
//		ltv_starting_amount_for_first_day.setValueTextColor(TextColor.Red);
		ltv_starting_amount_for_superaddition_day = (LabelTextView) findViewById(R.id.ltv_starting_amount_for_superaddition_day);
//		ltv_starting_amount_for_superaddition_day.setValueTextColor(TextColor.Red);
		ltv_purchase_base = (LabelTextView) findViewById(R.id.ltv_purchase_base);
		ltv_can_revoke = (LabelTextView) findViewById(R.id.ltv_can_revoke);
		ltv_account = (LabelTextView) findViewById(R.id.ltv_account);
		ltv_enable_quantity = (LabelTextView) findViewById(R.id.ltv_enable_quantity);
		ltv_enable_quantity.setValueTextColor(TextColor.Red);
		ltv_holding_quantity = (LabelTextView) findViewById(R.id.ltv_holding_quantity);
		ltv_holding_quantity.setValueTextColor(TextColor.Red);
		et_exchange_number = (EditText) findViewById(R.id.et_exchange_number);
		tv_chinese_numeral = (TextView) findViewById(R.id.tv_chinese_numeral);
		tv_chinese_numeral.setVisibility(View.GONE);
		Trans2ChineseNumber.relateNumInputAndChineseShower(et_exchange_number,
				tv_chinese_numeral);

		ltv_product_code.setValueText(info.prodCode);
		ltv_product_name.setValueText(info.prodName);
		String curcode = String.valueOf(myproductMap
				.get(BocInvt.BOCINVT_HOLDPRO_CURCODE_RES));
		/** 产品币种 */
		ltv_currency.setValueText(LocalData.Currency.get(curcode));
//		ltv_estimate_apy.setValueText(info.yearlyRR + "%");
		ltv_estimate_apy.setValueText(HoldingBOCProductInfoUtil.getFriendlyYearlyRRRange(info));
//		if("04".equals(info.termType)&&"-1".equals(info.productTerm)){
//			((LabelTextView) findViewById(R.id.ltv_product_deadline)).setValueText("无固定期限");
//		}else{
			((LabelTextView) findViewById(R.id.ltv_product_deadline)).setValueText("最低持有"+responseDeal.get("prodTimeLimit")+"天");
//		}
			
		ltv_cash_remittance.setValueText( LocalData.cashRemitMapValue.get( info.cashRemit));
		ltv_account.setValueText(StringUtil
				.getForSixForString(info.bancAccount));
		ltv_enable_quantity.setValueText(HoldingBOCProductInfoUtil
				.getFriendlyAvailableQuantity(info));
		ltv_holding_quantity.setValueText(HoldingBOCProductInfoUtil
				.getFriendlyHoldingQuantity(info));
//		ltv_residue_limit.setValueText((String) responseDeal
//				.get(BocInvt.BOCINVT_AVAILAMT_RES));
//		if (Double.parseDouble(responseDeal.get(BocInvt.BOCINVT_AVAILAMT_RES).toString())>(double)10000000) {
//			ltv_residue_limit.setValueText("大于1000万");
//		}else {
		ltv_residue_limit.setValueTextColor(TextColor.Red);
			ltv_residue_limit.setValueText(StringUtil.parseStringCodePattern(curcode,responseDeal.get(BocInvt.BOCINVT_AVAILAMT_RES).toString(), 2));
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this, ltv_residue_limit);
//		}
		ltv_starting_amount_for_first_day.setValueText(StringUtil.parseStringCodePattern(curcode,(String) responseDeal
				.get(BocInvt.BOCINVT_SUBAMOUNT_RES), 2));
		ltv_starting_amount_for_superaddition_day.setValueText(StringUtil.parseStringCodePattern(curcode,(String) responseDeal
				.get(BocInvt.BOCINVT_ADDAMOUNT_RES), 2)
				);
		ltv_purchase_base.setValueText(StringUtil.parseStringCodePattern(curcode,
				(String) responseDeal.get(BocInvt.BOCINVT_BASEAMOUNT_RES),2));
		ltv_can_revoke
				.setValueText(LocalData.isCanCancleStr
						.get((String) responseDeal
								.get(BocInvt.BOCINVT_ISCANCANCLE_RES)));

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next:
			if (checkInput()) {
				info.transferUnit = et_exchange_number.getText().toString();
				this.getHttpTools().requestHttpWithConversationId(Comm.PSN_GETTOKENID,null, (String)BaseDroidApp.getInstanse().getBizDataMap()
						.get(ConstantGloble.CONVERSATION_ID),new IHttpResponseCallBack<String>(){


					@Override
					public void httpResponseSuccess(String result,String method) {
						BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.TOKEN_ID,result);
						Map<String,Object> map = new HashMap<String,Object>();
						map.put("token", result);
						map.put("accountKey", info.bancAccountKey);
						map.put("proId", info.prodCode);
						map.put("tranUnit", et_exchange_number.getText().toString());
						map.put("charCode", info.cashRemit);
						map.put("serialNo", info.tranSeq);
						QuantityTransitionActivity.this.getHttpTools().requestHttpWithConversationId("PsnXpadShareTransitionVerify",map, (String)BaseDroidApp.getInstanse().getBizDataMap()
								.get(ConstantGloble.CONVERSATION_ID),new IHttpResponseCallBack<Map<String,Object>>(){

							@Override
							public void httpResponseSuccess(Map<String,Object> result,String method) {
								BaseHttpEngine.dissMissProgressDialog();
								info.PsnXpadShareTransitionVerifyResponseMap  = result;
								ActivityIntentTools.intentToActivity(QuantityTransitionActivity.this,
										QuantityTransitionAffirmActivity.class);
							}
						});
						
					}
				});

//				this.getHttpTools().requestHttpWithConversationId("PsnXpadShareTransitionVerify",map, null,new IHttpResponseCallBack(){
//
//					@Override
//					public void httpResponseSuccess(Object result,String method) {
//						BaseHttpEngine.dissMissProgressDialog();
//						ActivityIntentTools.intentToActivity(QuantityTransitionActivity.this, QuantityTransitionAffirmActivity.class);
////						Intent intent = new Intent(QuantityTransitionActivity.this, QuantityTransitionAffirmActivity.class);
////						startActivity(intent);
//					}
//				});

			}
			break;
		}
	}

//	public void PsnXpadShareTransitionVerifyResponseCallBack(Object result) {
//		BaseHttpEngine.dissMissProgressDialog();
//		Intent intent = new Intent(this, QuantityTransitionAffirmActivity.class);
//		startActivity(intent);
//	}

	// public static Intent getIntent(Context context,
	// BOCProductForHoldingInfo productInfo) {
	// Intent intent = new Intent(context, QuantityTransitionActivity.class);
	// intent.putExtra(KEY_PRODUCT_INFO, productInfo);
	// return intent;
	// }
	/**
	 * 检测输入的合法性
	 * 
	 * @return
	 */
	public boolean checkInput() {
		/** 持有份额 */
		double canredeemQuantity = Double.valueOf(info.holdingQuantity);
		/** 转换份额 */
		String exchangeQuantity = et_exchange_number.getText().toString()
				.trim();
		// 以下为验证
		// 转换份额
		RegexpBean reb = new RegexpBean(getResources().getString(
				R.string.bocinvt_transition_transition_quantity_edit),
				exchangeQuantity, "redeemShare");
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
//		lists.add(BocInvestControl.getRegexpBean(info.curCode.numberCode, "转换份额", exchangeQuantity, null));
//		lists.add(BocInvestControl.getRegexpBean(info.curCode.letterCode, "转换份额", exchangeQuantity, null));
		lists.add(reb);
		if (!RegexpUtils.regexpDate(lists)) {// 校验没有通过
			return false;
		}

		return true;
	}
}
