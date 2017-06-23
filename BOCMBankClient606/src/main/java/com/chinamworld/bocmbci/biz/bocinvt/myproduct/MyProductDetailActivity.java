package com.chinamworld.bocmbci.biz.bocinvt.myproduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.inverstagreemanager.InvestInvalidAgreeQueryActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCProductForHoldingInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.MyInvetProductActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.QuantityTransitionStatementActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.RedeemActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.ReferenceProfitActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.util.HoldingBOCProductInfoTransformUtil;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.util.HoldingBOCProductInfoUtil;
import com.chinamworld.bocmbci.biz.investTask.BOCDoThreeTask;
import com.chinamworld.bocmbci.biz.investTask.IAction;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.bocmbci.utils.DateUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 我的理财产品详情页面
 * 
 * @author wangmengmeng
 * 
 */
public class MyProductDetailActivity extends BociBaseActivity {
	private static final String TAG = "MyProductDetailActivity";
	/** 理财产品详情页面 */
	private View view;
	/** 产品代码 */
	private TextView tv_prodCode_detail;
	/** 产品名称 */
	private TextView tv_prodName_detail;
	/** 产品币种 */
	private TextView tv_curCode_detail;
	/** 产品成立日 p603修改为产品期限 */
	private TextView tv_prodBegin_detail;
	private LinearLayout prodBegin_detail_layout1;
	/** 产品到期日 */
	private TextView tv_prodEnd_detail;
	/** 产品到期日 layout*/
	private LinearLayout prodEnd_detail_layout;
	/** 持有份额 */
	private TextView tv_holdingQuantity_detail;
	/** 可用份额 */
	private TextView tv_availableQuantity_detail;
//	/** 赎回价格 */
//	private TextView tv_sellPrice_detail;
	/** 已实现收益 */
	// private TextView tv_payProfit_detail;
	/** 预计年收益率 */
	private TextView tv_yearlyRR_detail;
	/** 产品信息 */
	private Map<String, Object> myproductMap;
	/** 份额明细 */
	private Map<String, Object> quantityDetailMap;
	// 按钮//////////////////////
	/** 修改分红方式,p603此按钮改为追加购买按钮 */
	private Button btn_changeBonusMode;
	/** 赎回 p603此按钮改为更多按钮，点击弹出选择项 */
	private Button btn_redeem;
	private String acctId;

	// p603新增字段
	/** 预计年收益率 */
	private TextView ltv_yearly_yield_rate;
	private LinearLayout yearly_yield_rate_parentLayout;
	/** 历史净值 */
//	private LabelTextView ltv_price;
	private TextView ltv_price;
	/** 历史净值 */
	private LinearLayout price_layout;
	/** 净值更新日期 */
	private LabelTextView ltv_price_refresh_date;
	/** 参考收益 */
	private TextView tv_progression;
	
	private BOCProductForHoldingInfo info;
	/**产品详情 */
	private Map<String, Object> responseDeal;
	/** 钞/汇*/
	private TextView tv_cashRemit;
	/** 产品类型*/
	private TextView tv_productType;
	/** 账户信息 */
	private Map<String, Object> accountMap;
	/** 任务弹出框视图 */
	private RelativeLayout dialogView;
	/** 风险评测是否到期 */
	public boolean evalExpired = true;
	/** 是否关联银行账户 */
	public boolean iscombine = false;
	/** 理财交易账户*/
	private TextView tv_bocint_number;
	/** 产品到期日*/
	private TextView tv_prodEnd;
	/** 预计年收益率*/
	private TextView tv_bocinvt_yearlyRR;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.bocinvt_hold_manager));
		setText(this.getString(R.string.go_main));
		// 添加布局
		view = addView(R.layout.bocinvt_myproduct_detail);
		// 界面初始化
		init();
		setRightBtnClick(rightBtnClick);
		requestSystemDateTime();
	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 回主页面
			ActivityTaskManager.getInstance().removeAllSecondActivity();
		}
	};

	@SuppressWarnings("unchecked")
	private void init() {
				
		myproductMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_MYPRODUCT_LIST);
		quantityDetailMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_QUANTITY_DETAIL_LIST);
		responseDeal = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_PRODUCT_DETAIL_MAP);
		myproductMap = HoldingBOCProductInfoTransformUtil.Combine2map(myproductMap, quantityDetailMap);
		BaseDroidApp.getInstanse().getBizDataMap()
		.put(ConstantGloble.BOCINVT_QUANTITY_DETAIL_COMBINE_MAP,myproductMap);
		tv_prodCode_detail = (TextView) view
				.findViewById(R.id.tv_prodCode_detail);
		tv_prodName_detail = (TextView) view
				.findViewById(R.id.tv_prodName_detail);
		tv_curCode_detail = (TextView) view
				.findViewById(R.id.tv_curCode_detail);
		tv_prodBegin_detail = (TextView) view
				.findViewById(R.id.tv_prodBegin_detail);
		tv_prodEnd_detail = (TextView) view
				.findViewById(R.id.tv_prodEnd_detail);
		tv_holdingQuantity_detail = (TextView) view
				.findViewById(R.id.tv_holdingQuantity_detail);
		tv_availableQuantity_detail = (TextView) view
				.findViewById(R.id.tv_availableQuantity_detail);
//		tv_sellPrice_detail = (TextView) view
//				.findViewById(R.id.tv_sellPrice_detail);
		// tv_payProfit_detail = (TextView) view
		// .findViewById(R.id.tv_payProfit_detail);
		// tv_yearlyRR_detail = (TextView) view
		// .findViewById(R.id.tv_yearlyRR_detail);

		/**** 以下代码P603新增 *****/

		tv_bocint_number = (TextView) view.findViewById(R.id.tv_bocint_number);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_bocint_number);
		tv_prodEnd = (TextView) view.findViewById(R.id.tv_prodEnd);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodEnd);
		tv_bocinvt_yearlyRR = (TextView) view.findViewById(R.id.tv_bocinvt_yearlyRR);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_bocinvt_yearlyRR);
		tv_productType = (TextView) view.findViewById(R.id.tv_product_type);
		tv_cashRemit =  (TextView) view.findViewById(R.id.tv_cashRemit);
		yearly_yield_rate_parentLayout =  (LinearLayout) view.findViewById(R.id.yearly_yield_rate_parent);
		prodBegin_detail_layout1 = (LinearLayout) view.findViewById(R.id.prodBegin_detail_layout);
		prodEnd_detail_layout = (LinearLayout) view.findViewById(R.id.prodEnd_detail_layout);
		info = HoldingBOCProductInfoTransformUtil
				.map2BocProductInfo(myproductMap);
		tv_productType.setText(BociDataCenter.productType.get(info.issueType));
		ltv_yearly_yield_rate = (TextView) view.findViewById(R.id.ltv_yearly_yield_rate);
//		ltv_price = (LabelTextView) findViewById(R.id.ltv_price);
		ltv_price = (TextView) view.findViewById(R.id.ltv_price);
		price_layout = (LinearLayout) view.findViewById(R.id.price_layout);
		ltv_price_refresh_date = (LabelTextView) findViewById(R.id.ltv_price_refresh_date);
		tv_progression = (TextView) view.findViewById(R.id.tv_progression);
		
		if(StringUtil.isNullOrEmpty(info.expProfit)||((!info.expProfit.contains("-"))
				&&(Double.parseDouble(StringUtil.append2Decimals(
				info.expProfit, 2)) == 0)&&!"2".equals(info.issueType))){
			tv_progression.setText("暂无收益");
			tv_progression.setTextColor(getResources().getColor(
					R.color.black));
		}else{
			
			if("1".equals(info.productKind)||"1".equals(info.progressionflag)||
				HoldingBOCProductInfoUtil.isStandardPro(info)||"AMRJYL01".equals(info.prodCode)){
			//参考收益
//			if("1".equals(info.productKind)&&(!info.expProfit.contains("-"))
//					&&(Double.parseDouble(StringUtil.append2Decimals(
//					info.expProfit, 2)) == 0)){
//				tv_progression.setText("暂无收益");
//				tv_progression.setTextColor(getResources().getColor(
//						R.color.black));
//			}else{
			tv_progression.setText(StringUtil.parseStringCodePattern(String.valueOf(myproductMap.get("curCode")),info.expProfit, 2));
			tv_progression.setTextColor(getResources().getColor(
					R.color.blue));
			tv_progression.getPaint()
					.setFlags(Paint.UNDERLINE_TEXT_FLAG);
			tv_progression.getPaint().setAntiAlias(true);
			tv_progression.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					startActivity(ReferenceProfitActivity.getIntent(MyProductDetailActivity.this, info));
				}
			});
//			}
		}else{
			tv_progression.setText(StringUtil.parseStringCodePattern(String.valueOf(myproductMap.get("curCode")),info.expProfit, 2));
			tv_progression.setTextColor(getResources().getColor(
					R.color.red));
		}
		}
		ltv_yearly_yield_rate.setText(HoldingBOCProductInfoUtil
				.getFriendlyYearlyRRRange(info));
		//现金管理类和固定期限类显示预计年收益率
		if(BOCProductForHoldingInfo.PROD_TYPE_CASH.equals(info.issueType)||
				BOCProductForHoldingInfo.PROD_TYPE_FIXATION.equals(info.issueType)){
			yearly_yield_rate_parentLayout.setVisibility(View.VISIBLE);
		}else{
			yearly_yield_rate_parentLayout.setVisibility(View.GONE);
		}
		if(yearly_yield_rate_parentLayout.getVisibility() == View.VISIBLE){
			
			String progressionflag = (String) myproductMap.get(BocInvt.PROGRESSIONFLAG);
			if (HoldingBOCProductInfoUtil.isStandardPro(info)) {
				ltv_yearly_yield_rate.setTextColor(this.getResources().getColor(R.color.blue));
				ltv_yearly_yield_rate.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
				ltv_yearly_yield_rate.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
//						requestSystemDateTime();
						BocInvestControl.toProductQueryAndBuyYearRateActivity(view.getContext(), responseDeal, dateTime, false, 0);
					}
				});
			} else if (progressionflag.equals("1")) {
				ltv_yearly_yield_rate.setTextColor(this.getResources().getColor(R.color.blue));
				ltv_yearly_yield_rate.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
				ltv_yearly_yield_rate.setOnClickListener(mClickListener);
			}
		}
//		if (HoldingBOCProductInfoUtil.canShowReferenceYield(info)) {
//			// 可显示参考收益
//			ltv_yearly_yield_rate.setTextColor(getResources().getColor(
//					R.color.blue));
//			ltv_yearly_yield_rate.getPaint()
//					.setFlags(Paint.UNDERLINE_TEXT_FLAG);
//			ltv_yearly_yield_rate.getPaint().setAntiAlias(true);
//			ltv_yearly_yield_rate.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					startActivity(ReferenceProfitActivity.getIntent(
//							MyProductDetailActivity.this, info));
//
//				}
//			});
//		} else {
//			// 不可显示参考收益
//			ltv_yearly_yield_rate.setTextColor(getResources().getColor(
//					R.color.black));
//			ltv_yearly_yield_rate.getPaint().setFlags(Paint.SUBPIXEL_TEXT_FLAG);
//			ltv_yearly_yield_rate.getPaint().setAntiAlias(true);
//		}
		// 只有净值开放类显示“单位净值”“净值更新日期”
		if (BOCProductForHoldingInfo.PROD_TYPE_VALUE.equals(info.issueType)) {
//			ltv_price.setVisibility(View.VISIBLE);
			price_layout.setVisibility(View.VISIBLE);
			ltv_price.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO 跳转到历史净值页面
				BocInvestControl.toEchartWebViewActivity(MyProductDetailActivity.this, String.valueOf(myproductMap
						.get(BocInvt.BOCINVT_HOLDPRO_PRODCODE_RES)), "0",
						StringUtil.parseStringPattern(info.price,4),info.priceDate,false, 0);	
//				Intent intent=new Intent(MyProductDetailActivity.this, EchartWebViewActivity.class);
//				startActivity(intent);
//					Toast.makeText(MyProductDetailActivity.this, "历史净值页面尚未开发",
//							Toast.LENGTH_SHORT).show();

				}
			});
			ltv_price_refresh_date.setVisibility(View.VISIBLE);
		} else {
			price_layout.setVisibility(View.GONE);
			ltv_price_refresh_date.setVisibility(View.GONE);
		}

		if (price_layout.getVisibility() == View.VISIBLE)
			ltv_price.setText(StringUtil.parseStringPattern(info.price,4));
			ltv_price.setTextColor(getResources().getColor(R.color.blue));
			ltv_price.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			ltv_price.getPaint().setAntiAlias(true);
		if (ltv_price_refresh_date.getVisibility() == View.VISIBLE)
			ltv_price_refresh_date.setValueText(info.priceDate);

		/**** 以上代码P603新增 *****/

		// 赋值
		/** 产品代码 */
		tv_prodCode_detail.setText(String.valueOf(myproductMap
				.get(BocInvt.BOCINVT_HOLDPRO_PRODCODE_RES)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodCode_detail);
		/** 产品名称 */
		tv_prodName_detail.setText(String.valueOf(myproductMap
				.get(BocInvt.BOCINVT_HOLDPRO_PRODNAME_RES)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodName_detail);
		String curcode = String.valueOf(myproductMap
				.get(BocInvt.BOCINVT_HOLDPRO_CURCODE_RES));
		/** 产品币种 */
		tv_curCode_detail.setText(LocalData.Currency.get(curcode));
		if (!StringUtil.isNull(LocalData.Currency.get(curcode))) {
			if (LocalData.Currency.get(curcode).equals(ConstantGloble.ACC_RMB)) {
				/** 钞汇 */
				tv_cashRemit.setText("-");
			} else {
				tv_cashRemit.setText(BociDataCenter.cashRemitMapValue2.get(String.valueOf(myproductMap
						.get(BocInvt.BOCINVT_HOLDPRO_CASHREMIT_RES))));
//				tv_curCode_detail
//						.setText(LocalData.Currency.get(curcode)
//								+ BociDataCenter.cashRemitMapValue2.get(String.valueOf(myproductMap
//										.get(BocInvt.BOCINVT_HOLDPRO_CASHREMIT_RES))));
			}
		}
		
		/** 产品成立日 */
//		tv_prodBegin_detail.setText(String.valueOf(myproductMap
//				.get(BocInvt.BOCINVT_HOLDPRO_PRODBEGIN_RES)));
		//产品期限  固定期限类产品有效
//		tv_prodBegin_detail.setText(info.productTerm);
		if(BOCProductForHoldingInfo.PROD_TYPE_FIXATION.equals(info.issueType)){
			prodBegin_detail_layout1.setVisibility(View.VISIBLE);
			prodEnd_detail_layout.setVisibility(View.VISIBLE);
		}else{
			prodBegin_detail_layout1.setVisibility(View.GONE);
			prodEnd_detail_layout.setVisibility(View.GONE);
		}
		/** 产品到期日 */
		String prodEnd = (String) myproductMap
				.get(BocInvt.BOCINVT_HOLDPRO_PRODEND_RES);
		String termType = (String) myproductMap.get("termType");
		if(HoldingBOCProductInfoUtil.isStandardPro(info)){//业绩基准产品
			if(!StringUtil.isNullOrEmpty(quantityDetailMap)
					&&!StringUtil.isNullOrEmpty((String) quantityDetailMap.get("prodEnd"))
					&&("-1".equals((String) quantityDetailMap.get("prodEnd")))){
				tv_prodEnd_detail.setText(R.string.bocinvt_longTime);
				tv_prodBegin_detail.setText(R.string.bocinvt_unfixation);
			}else{
				if(BOCProductForHoldingInfo.STANDARD_YES_1.equals(info.standardPro)){//业绩基准转低收益产品
					tv_prodBegin_detail.setText("最低持有"+info.productTerm+"天");
				}else if(BOCProductForHoldingInfo.STANDARD_YES_3.equals(info.standardPro)){//周期滚续产品
					tv_prodBegin_detail.setText(info.productTerm+"天"+"("+(String) quantityDetailMap.get("currPeriod")
							+"/"+(String) quantityDetailMap.get("totalPeriod")+")");
				}else{
					tv_prodBegin_detail.setText(info.productTerm+"天");
				}
				tv_prodEnd_detail.setText(DateUtils.DateFormatter(String.valueOf(myproductMap
						.get(BocInvt.BOCINVT_HOLDPRO_PRODEND_RES))));
			}
			
		}else{
			if(!StringUtil.isNull(termType) && termType.equals("04")){//无限开放式
				tv_prodEnd_detail.setText(R.string.bocinvt_longTime);
				tv_prodBegin_detail.setText(R.string.bocinvt_unfixation);
			}else{
				tv_prodBegin_detail.setText(info.productTerm+"天");
				tv_prodEnd_detail.setText(DateUtils.DateFormatter(String.valueOf(myproductMap
								.get(BocInvt.BOCINVT_HOLDPRO_PRODEND_RES))));
			}
		}
//		if(BOCProductForHoldingInfo.STANDARD_YES_1.equals(info.standardPro)){
//			if(!StringUtil.isNullOrEmpty(quantityDetailMap)
//					&&!StringUtil.isNullOrEmpty((String) quantityDetailMap.get("prodEnd"))
//					&&("-1".equals((String) quantityDetailMap.get("prodEnd")))){
//				tv_prodEnd_detail.setText(R.string.bocinvt_longTime);
//				tv_prodBegin_detail.setText(R.string.bocinvt_unfixation);
//			}else{
//				tv_prodBegin_detail.setText("最低持有"+info.productTerm+"天");
//				tv_prodEnd_detail.setText(DateUtils.DateFormatter(String.valueOf(myproductMap
//								.get(BocInvt.BOCINVT_HOLDPRO_PRODEND_RES))));
//			}
//		}else{
//			if(!StringUtil.isNull(termType) && termType.equals("04")){
//				tv_prodEnd_detail.setText(R.string.bocinvt_longTime);
//				tv_prodBegin_detail.setText(R.string.bocinvt_unfixation);
//				if(!StringUtil.isNullOrEmpty(quantityDetailMap)
//					&&!StringUtil.isNullOrEmpty((String) quantityDetailMap.get("prodEnd"))
//					&&(!"-1".equals((String) quantityDetailMap.get("prodEnd")))){
//					tv_prodEnd_detail.setText(DateUtils.DateFormatter(String.valueOf(myproductMap
//						.get(BocInvt.BOCINVT_HOLDPRO_PRODEND_RES))));
//				}
//		}else{
//				tv_prodBegin_detail.setText(info.productTerm+"天");
//				tv_prodEnd_detail.setText(DateUtils.DateFormatter(String.valueOf(myproductMap
//								.get(BocInvt.BOCINVT_HOLDPRO_PRODEND_RES))));
//			}
//		}
//		if ((!StringUtil.isNullOrEmpty(quantityDetailMap)
//				&&!StringUtil.isNullOrEmpty((String) quantityDetailMap.get("prodEnd"))
//				&&("-1".equals((String) quantityDetailMap.get("prodEnd"))))//份额明细的无固定期限通过产品到期日是否为-1来判断
//				||(!HoldingBOCProductInfoUtil.isStandardPro(info)&&!StringUtil.isNull(termType) 
//						&& termType.equals("04"))) {//其他的通过是否为业绩基准型及产品期限特性（termtype）判定
//			tv_prodEnd_detail.setText(R.string.bocinvt_unfixation);
//			tv_prodBegin_detail.setText(R.string.bocinvt_unfixation);
//		} else {
//			tv_prodEnd_detail.setText(DateUtils.DateFormatter(String
//					.valueOf(myproductMap
//							.get(BocInvt.BOCINVT_HOLDPRO_PRODEND_RES))));
//			tv_prodBegin_detail.setText(DateUtils.DateFormatter(String
//					.valueOf(info.productTerm)));
//		}
		/** 持有份额 */
		String holdingQuantity = String.valueOf(myproductMap
				.get(BocInvt.BOCINVT_HOLDPRO_HOLDINGQUANTITY_RES));
//		tv_holdingQuantity_detail.setText(StringUtil.parseStringCodePattern(String.valueOf(myproductMap.get("curCode")),
//				holdingQuantity, 2));
		//XPADC去掉份额针对日元的格式化
		tv_holdingQuantity_detail.setText(StringUtil.parseStringPattern(holdingQuantity, 2));
		/** 可用份额 */
		String available = String.valueOf(myproductMap
				.get(BocInvt.BOCINVT_HOLDPRO_AVAILABLEQUANTITY_RES));
//		tv_availableQuantity_detail.setText(StringUtil.parseStringCodePattern(String.valueOf(myproductMap.get("curCode")),
//				available, 2));
		tv_availableQuantity_detail.setText(StringUtil.parseStringPattern(available, 2));
//		/** 赎回价格 */
//		String sellPrice = String.valueOf(myproductMap
//				.get(BocInvt.BOCINVT_HOLDPRO_SELLPRICE_RES));
//		tv_sellPrice_detail.setText(StringUtil.parseStringCodePattern(curcode,
//				sellPrice, 2));
		/** 已实现收益 */
		// tv_payProfit_detail.setText(String.valueOf(myproductMap
		// .get(BocInvt.BOCINVT_HOLDPRO_PAYPROFIT_RES)));
		((TextView) findViewById(R.id.bocint_number)).setText(StringUtil
				.getForSixForString((String) myproductMap
						.get(BocInvt.BANCACCOUNT)));
		btn_changeBonusMode = (Button) view
				.findViewById(R.id.btn_changeBonusMode);
		btn_redeem = (Button) view.findViewById(R.id.btn_redeem);
//		btn_changeBonusMode.setEnabled(info.canAddBuy);
//
//		btn_changeBonusMode.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// 跳转至追加购买页面
//				BocInvestControl.toBuyActivity(v.getContext(),
//						info.productKind, info.prodCode, new HashMap<String, Object>(),false, 0);
//
//			}
//		});
//		btn_redeem.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				showMoreAction(v);
//
//			}
//		});
		// setProgress();
		List<String> serlist = new ArrayList<String>();
		//是否可追加购买
		if((!"3".equals(info.issueType))&&info.canAddBuy){
			serlist.add(bocMyProductList.get(0));
		}
		if("3".equals(info.issueType)&&info.canAddBuy&&HoldingBOCProductInfoUtil.isStandardPro(info)){
			//是否可继续购买
			serlist.add(bocMyProductList.get(1));
		}
		//是否可赎回
		if(HoldingBOCProductInfoUtil.canRedeem(info)){
			serlist.add(bocMyProductList.get(2));
		}
		// 只有业绩基准型产品才显示份额转化按钮
		if(HoldingBOCProductInfoUtil.isStandardPro(info)&&"0".equals(myproductMap.get(BocInvt.BOCINVT_CANQUANTITYEXCHANGE_RES))){
			serlist.add(bocMyProductList.get(3));
		}
		//投资协议管理
		if(HoldingBOCProductInfoUtil.canAgreementMange(info)){
			serlist.add(bocMyProductList.get(4));
		}
		//设置分红方式
		if(HoldingBOCProductInfoUtil.canChangeBonusMode(info)){
			serlist.add(bocMyProductList.get(5));
		}
		if (serlist == null || serlist.size() == 0) {
			btn_changeBonusMode.setVisibility(View.GONE);
			btn_redeem.setVisibility(View.GONE);
		}else if(serlist.size() == 1){
			btn_changeBonusMode.setVisibility(View.VISIBLE);
			btn_redeem.setVisibility(View.GONE);
			btn_changeBonusMode.setText(serlist.get(0));
			btn_changeBonusMode.setOnClickListener(btnMoreNullBocinvtClick);
		}else if (serlist.size() == 2) {
			btn_changeBonusMode.setVisibility(View.VISIBLE);
			btn_redeem.setVisibility(View.VISIBLE);
			LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) btn_redeem
					.getLayoutParams();
			btn_changeBonusMode.setLayoutParams(param);
			btn_changeBonusMode.setText(serlist.get(0));
			btn_changeBonusMode.setOnClickListener(btnMoreNullBocinvtClick);
			btn_redeem.setText(serlist.get(1));
			btn_redeem.setOnClickListener(btnMoreNullBocinvtClick);
		}else {
			btn_changeBonusMode.setVisibility(View.VISIBLE);
			btn_redeem.setVisibility(View.VISIBLE);
			LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) btn_redeem
					.getLayoutParams();
			btn_changeBonusMode.setLayoutParams(param);
			btn_changeBonusMode.setText(serlist.get(0));
			btn_changeBonusMode.setOnClickListener(btnMoreNullBocinvtClick);
			btn_redeem.setText(BaseDroidApp.getInstanse().getCurrentAct()
					.getString(R.string.more));
			String[] service = new String[serlist.size() - 1];
			for (int k = 0; k < serlist.size() - 1; k++) {
				service[k] = serlist.get(k + 1);
			}
			PopupWindowUtils.getInstance().setshowMoreChooseUpListener(
					BaseDroidApp.getInstanse().getCurrentAct(), btn_redeem,
					service, btnMoreNullBocinvtClick);
		}
	}

	/** 更多点击事件 */
	final OnClickListener btnMoreNullBocinvtClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Button tv = (Button) v;
			String text = tv.getText().toString();
			moreClick(text, v);
		}
	};

	/** 判断点击事件 */
	public void moreClick(String text, View v) {
		if (text.equals(bocMyProductList.get(0))) {
//			requestInvtEva();
			// 跳转至追加购买页面
//			BocInvestControl.toBuyActivity(v.getContext(),
//					info.productKind, info.prodCode, myproductMap,dateTime,false, 0);
			
			BOCDoThreeTask task = BOCDoThreeTask.getInstance(this);
			registActivityEvent(task);
			task.doTask(new IAction() {

				@Override
				public void SuccessCallBack(Object param) {
//					Map<String, Object> parms_map=new HashMap<String, Object>();
//					parms_map.put("xpadAccountSatus", "1");
//					parms_map.put("queryType", "0");
//					getHttpTools().requestHttp("PsnXpadAccountQuery", "requestPsnXpadAccountQueryCallBack", parms_map, true);
					isCombineAccount();
				}
			},1);
//			Map<String, Object> parms_map=new HashMap<String, Object>();
//			parms_map.put("xpadAccountSatus", "1");
//			parms_map.put("queryType", "1");
//			getHttpTools().requestHttp("PsnXpadAccountQuery", "requestPsnXpadAccountQueryCallBack", parms_map, true);
		}
		if (text.equals(bocMyProductList.get(1))) {
//			requestInvtEva();
			
			BOCDoThreeTask task = BOCDoThreeTask.getInstance(this);
			registActivityEvent(task);
			task.doTask(new IAction() {

				@Override
				public void SuccessCallBack(Object param) {
//					Map<String, Object> parms_map=new HashMap<String, Object>();
//					parms_map.put("xpadAccountSatus", "1");
//					parms_map.put("queryType", "0");
//					getHttpTools().requestHttp("PsnXpadAccountQuery", "requestPsnXpadAccountQueryCallBack", parms_map, true);
					isCombineAccount();
				}
			},1);
			//继续购买
//			BocInvestControl.toBuyActivity(v.getContext(),
//					info.productKind, info.prodCode,myproductMap,dateTime, false, 0);
//			Map<String, Object> parms_map=new HashMap<String, Object>();
//			parms_map.put("xpadAccountSatus", "1");
//			parms_map.put("queryType", "1");
//			getHttpTools().requestHttp("PsnXpadAccountQuery", "requestPsnXpadAccountQueryCallBack", parms_map, true);
			
		}
		if (text.equals(bocMyProductList.get(2))) {
			// 赎回
			// p603 新的赎回流程
			startActivity(RedeemActivity.getIntent(
					MyProductDetailActivity.this, info));
		}
		if (text.equals(bocMyProductList.get(3))) {
			// 份额转换
//			startActivity(QuantityTransitionActivity.getIntent(
//					MyProductDetailActivity.this, info));
			Intent intent = new Intent(MyProductDetailActivity.this, QuantityTransitionStatementActivity.class);
			startActivity(intent);
		}
		if (text.equals(bocMyProductList.get(4))) {
			// 投资协议管理
			Intent intent = new Intent(MyProductDetailActivity.this,InvestInvalidAgreeQueryActivity.class);
			startActivity(intent);
		}
		if (text.equals(bocMyProductList.get(5))) {
			// 设置分红方式
			changeBonusModeClick.onClick(v);
		}
	}
	
//	/** 更多按钮的展开显示 */
//	private PopupWindow pw;
//
//	/**
//	 * 显示更多的操作
//	 */
//	private void showMoreAction(View v) {
//		if (pw == null) {
//			pw = initMoreActionPopuWindow(v);
//		}
//
//		pw.showAsDropDown(v);
//
//	}
//
//	/**
//	 * 初始化跟多按钮的展开popu
//	 * 
//	 * @param v
//	 * @return
//	 */
//	private PopupWindow initMoreActionPopuWindow(View v) {
//
//		pw = new PopupWindow(this);
//		View view = View.inflate(this,
//				R.layout.bocinvt_holding_detail_more_button_expansion, null);
//		OnClickListener listener = new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				pw.dismiss();
//				switch (v.getId()) {
//				case R.id.bt_continue_purchase:// 继续购买
//					BocInvestControl.toBuyActivity(v.getContext(),
//							info.productKind, info.prodCode,new HashMap<String, Object>(), false, 0);
//					break;
//				case R.id.bt_redeem:// 赎回
//					// redeemClick.onClick(v);
//					// p603 新的赎回流程
//					startActivity(RedeemActivity.getIntent(
//							MyProductDetailActivity.this, info));
//					break;
//				case R.id.bt_lot_transform:// 份额转换
//					startActivity(QuantityTransitionActivity.getIntent(
//							MyProductDetailActivity.this, info));
//					break;
//				case R.id.bt_agreement_manager:// 投资协议管理
//					Intent intent = new Intent(MyProductDetailActivity.this,InvestInvalidAgreeQueryActivity.class);
//					startActivity(intent);
//					break;
//				case R.id.bt_set_dividend:// 设置分红方式
//					changeBonusModeClick.onClick(v);
//					break;
//
//				default:
//					break;
//				}
//
//			}
//		};
//		View bt_continue_purchase = view
//				.findViewById(R.id.bt_continue_purchase);
//		bt_continue_purchase.setOnClickListener(listener);
//		View bt_redeem = view.findViewById(R.id.bt_redeem);
//		bt_redeem.setOnClickListener(listener);
//		View bt_lot_transform = view.findViewById(R.id.bt_lot_transform);
//		bt_lot_transform.setOnClickListener(listener);
//		// 只有业绩基准型产品才显示份额转化按钮
//		bt_lot_transform.setVisibility(HoldingBOCProductInfoUtil
//				.isStandardPro(info) ? View.VISIBLE : View.GONE);
//
//		View bt_agreement_manager = view
//				.findViewById(R.id.bt_agreement_manager);
//		// 不可投资协议管理
//		if (!HoldingBOCProductInfoUtil.canAgreementMange(info)) {
//			bt_agreement_manager.setVisibility(View.GONE);
//		}
//		bt_agreement_manager.setOnClickListener(listener);
//		View bt_set_dividend = view.findViewById(R.id.bt_set_dividend);
//		// 不可更改分红方式则隐藏该按钮
//		if (!HoldingBOCProductInfoUtil.canChangeBonusMode(info)) {
//			bt_set_dividend.setVisibility(View.GONE);
//		}
//
//		bt_set_dividend.setOnClickListener(listener);
//		view.measure(
//				MeasureSpec.makeMeasureSpec(v.getWidth(), MeasureSpec.AT_MOST),
//				0);
//
//		pw.setWidth(view.getMeasuredWidth());
//		pw.setHeight(view.getMeasuredHeight());
//		pw.setBackgroundDrawable(new BitmapDrawable());
//		pw.setContentView(view);
//		pw.setOutsideTouchable(false);
//		pw.setFocusable(true);
//		pw.setTouchable(true);
//
//		return pw;
//	}

	/** 修改分红方式 */
	OnClickListener changeBonusModeClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String canChangeBonusMode = (String) myproductMap
					.get(BocInvt.BOCINVT_HOLDPRO_CANCHANGEBONUSMODE_RES);
			if (!StringUtil.isNull(canChangeBonusMode)
					&& canChangeBonusMode.equals("0")) {
				// 允许修改分红方式
				Intent intent = new Intent(MyProductDetailActivity.this,
						ChangeBonusModeActivity.class);
				startActivity(intent);
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						MyProductDetailActivity.this
								.getString(R.string.bocinvt_error_changeMode));
			}
		}
	};
	/** 赎回 */
	OnClickListener redeemClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String canRedeem = (String) myproductMap
					.get(BocInvt.BOCINVT_HOLDPRO_CANREDEEM_RES);
			if (!StringUtil.isNull(canRedeem) && canRedeem.equals("0")) {
				Intent intent = new Intent(MyProductDetailActivity.this,
						RedeemInputActivity.class);
				startActivity(intent);
			} else {
				// 不允许赎回
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						MyProductDetailActivity.this
								.getString(R.string.bocinvt_error_canredeem));
			}
		}
	};



//	/** 预计年收益率 */
//	private void setProgress() {
//		String progressionflag = (String) myproductMap
//				.get(BocInvt.PROGRESSIONFLAG);
//		if (StringUtil.isNull(progressionflag))
//			return;
//		if (progressionflag.equals("0")) {
//			tv_yearlyRR_detail.setText(StringUtil.append2Decimals(
//					(String) myproductMap
//							.get(BocInvt.BOCINVT_HOLDPRO_YEARLYRR_RES), 2)
//					+ PER);
//		} else if (progressionflag.equals("1")) {
//			tv_yearlyRR_detail.setTextColor(this.getResources().getColor(
//					R.color.blue));
//			tv_yearlyRR_detail.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//			tv_yearlyRR_detail.setText(getString(R.string.progression));
//			tv_yearlyRR_detail.setOnClickListener(mClickListener);
//		}
//	}

	/** 收益累进 */
	private OnClickListener mClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
		}
	};

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		for (int i = 0; i < BociDataCenter.getInstance().getBocinvtAcctList()
				.size(); i++) {
			String acctNum = (String) BociDataCenter.getInstance()
					.getBocinvtAcctList().get(i).get(BocInvt.ACCOUNTNO);
			if (myproductMap.get(BocInvt.BANCACCOUNT).equals(acctNum)) {
//				acctId = (String) BociDataCenter.getInstance()
//						.getBocinvtAcctList().get(i).get(Comm.ACCOUNT_ID);
				acctId = (String)myproductMap.get("bancAccountKey");
			}
		}
		requestProgress(
				acctId,
				(String) myproductMap.get(BocInvt.BOCINVT_HOLDPRO_PRODCODE_RES),
				"0", true);
	}

	@Override
	public void progressQueryCallBack(Object resultObj) {
		super.progressQueryCallBack(resultObj);
		startActivity(new Intent(this, ProgressInfoActivity.class)
				.putExtra(Comm.ACCOUNT_ID, acctId)
				.putExtra(BocInvt.PROGRESS_RECORDNUM, progressRecordNumber)
				.putExtra(
						BocInvt.BOCI_DETAILPRODNAME_RES,
						(String) myproductMap
								.get(BocInvt.BOCINVT_HOLDPRO_PRODNAME_RES))
				.putExtra(
						BocInvt.BOCI_PRODUCTCODE_REQ,
						(String) myproductMap
								.get(BocInvt.BOCINVT_HOLDPRO_PRODCODE_RES)));
	}
	
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BociDataCenter.getInstance().setDateTime("dateTime", dateTime);
//		BocInvestControl.toProductQueryAndBuyYearRateActivity(view.getContext(), responseDeal, dateTime, false, 0);
//		requestPsnXpadProductDetailQuery();
	}
	
	/**
	 * 请求产品详情
	 */
//	private void requestPsnXpadProductDetailQuery(){
//		HashMap<String, Object> map_parms = new HashMap<String, Object>();
//		map_parms.put(BocInvestControl.PRODUCTKIND, String.valueOf(myproductMap
//				.get("productKind")));
//		map_parms.put(BocInvestControl.PRODUCTCODE, String.valueOf(myproductMap
//				.get(BocInvt.BOCINVT_HOLDPRO_PRODCODE_RES)));
//		BaseHttpEngine.showProgressDialog();
//		getHttpTools().requestHttp(BocInvt.PRODUCTDETAILQUERY, "requestPsnXpadProductDetailQueryCallBack", map_parms, true);
//	}
//	/**
//	 * 请求产品详情 回调
//	 */
//	@SuppressWarnings("unchecked")
//	public void requestPsnXpadProductDetailQueryCallBack(Object resultObj){
//		if (StringUtil.isNullOrEmpty(resultObj)) {
//			BaseHttpEngine.dissMissProgressDialog();
//			return;
//		}
//		responseDeal = (Map<String, Object>)getHttpTools().httpResponseDeal(resultObj);
//		BaseHttpEngine.dissMissProgressDialog();
//		BocInvestControl.toProductQueryAndBuyYearRateActivity(view.getContext(), responseDeal, dateTime, false, 0);
//	}
	/**
	 * 是否已关联账户
	 */
	public void isCombineAccount(){
		List<Map<String, Object>> investBinding = BociDataCenter.getInstance().getBocinvtAcctList();
		if (StringUtil.isNullOrEmpty(investBinding)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.bocinvt_tv_75));
			return;
		}
		for (int i = 0; i < investBinding.size(); i++) {
			if (myproductMap.get("bancAccountKey").toString()
					.equals(investBinding.get(i).get(BocInvt.BOCINVT_CAPACITYQUERY_ACCOUNTKEY_RES).toString())) {
				if (StringUtil.isNullOrEmpty(investBinding.get(i).get(BocInvt.BOCI_ACCOUNTID_REQ))) {//未关联
					iscombine = false;
				}else {//已关联
					iscombine = true;
					accountMap = investBinding.get(i);

				}
			}
		}
		if(iscombine){//已关联
			BocInvestControl.toBuyActivity(this,
					info.productKind, info.prodCode,accountMap,dateTime, true, 0);
		}else{
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.bocinvt_tv_75));
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			if(requestCode == 0){
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				ActivityIntentTools.intentToActivity(MyProductDetailActivity.this, MyInvetProductActivity.class);
				this.finish();
			}
			break;
		default:
			break;
		}
	}
	
//	/**
//	 * 
//	 * @param resultObj
//	 */
//	public void requestPsnXpadAccountQueryCallBack(Object resultObj){
//		if (StringUtil.isNullOrEmpty(resultObj)) {
//			BaseHttpEngine.dissMissProgressDialog();
//			return;
//		}
//		Map<String, Object> result_map=getHttpTools().getResponseResult(resultObj);
//		List<Map<String, Object>> result_list = (List<Map<String, Object>>) result_map.get("list");
//		if (StringUtil.isNullOrEmpty(result_list)) {
//			BaseHttpEngine.dissMissProgressDialog();
//			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.bocinvt_tv_75));
//			return;
//		}
//		for (int i = 0; i < result_list.size(); i++) {
//			if (myproductMap.get("bancAccountKey").toString()
//					.equals(result_list.get(i).get(BocInvt.BOCINVT_CAPACITYQUERY_ACCOUNTKEY_RES).toString())) {
//				if (StringUtil.isNullOrEmpty(result_list.get(i).get(BocInvt.BOCI_ACCOUNTID_REQ))) {//未关联
////					BaseHttpEngine.dissMissProgressDialog();
////					BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.bocinvt_tv_75));
////					return;
//					iscombine = false;
//				}else {//已关联
//					iscombine = true;
//					accountMap = result_list.get(i);
////					BocInvestControl.toBuyActivity(this,
////							info.productKind, info.prodCode,accountMap,dateTime, false, 0);
//				}
//			}
//		}
//		if(iscombine){//已关联
//			BocInvestControl.toBuyActivity(this,
//					info.productKind, info.prodCode,accountMap,dateTime, false, 0);
//		}else{
//			BaseHttpEngine.dissMissProgressDialog();
//			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.bocinvt_tv_75));
//			return;
//		}
//		BaseHttpEngine.dissMissProgressDialog();
////		BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.bocinvt_tv_75));
//	}
//	/**
//	 * 请求是否进行过风险评估---回调
//	 * 
//	 * @param resultObj
//	 */
//	public void requestInvtEvaCallback(Object resultObj) {
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		// 得到response
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		Map<String, Object> responseMap = (Map<String, Object>) biiResponseBody
//				.getResult();
//		if (StringUtil.isNullOrEmpty(responseMap)) {
//			BiiHttpEngine.dissMissProgressDialog();
//			return;
//		}
//		BaseDroidApp.getInstanse().getBizDataMap()
//				.put(ConstantGloble.BOICNVT_ISBEFORE_RESULT, responseMap);
//		String status = (String) responseMap.get(BocInvt.BOCIEVA_STATUS_RES);
//		String evalExpiredRes = (String) responseMap.get("evalExpired");//风险评测否到期 0：是 1：否 未做过风险评估 /不存在此客户返回空格
//
//		if (!StringUtil.isNull(status)
//				&& status.equals(ConstantGloble.BOCINVT_EVA_SUC_STATUS)) {
//			isevaluated = true;
//			if(!StringUtil.isNull(evalExpiredRes)&&"1".equals(evalExpiredRes)){
//				evalExpired = false;
//			}else{
//				evalExpired = true;
//			}
//			
//		} else {
//			isevaluated = false;
//			
//		}
//		//做过风险评估且风险评估到期
//		if(!evalExpired){
//			return;
//		}else{
//			BiiHttpEngine.dissMissProgressDialog();
//			InflaterViewDialog inflater = new InflaterViewDialog(this);
//			investBinding = BociDataCenter.getInstance().getUnSetAcctList();
//			dialogView=(RelativeLayout)inflater.judgeViewDialog_choice(true,
//					investBinding, false, null,
//					null, invtEvaluationClick, exitDialogClick);
//			TextView tv = (TextView) dialogView.findViewById(R.id.tv_acc_account_accountState);
//			tv.setText(this.getString(R.string.bocinvt_invtEvaluation_title));
//			BaseDroidApp.getInstanse().showAccountMessageDialog(dialogView);
//		}
//		
//	}
	
//	/** 风险评估监听事件 */
//	protected OnClickListener invtEvaluationClick = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			Intent intent = new Intent(MyProductDetailActivity.this,
//					InvtEvaluationInputActivity.class);
//			startActivityForResult(intent,
//					ConstantGloble.ACTIVITY_REQUEST_INVTEVALUATION_CODE);
//			overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
//		}
//	};
}
