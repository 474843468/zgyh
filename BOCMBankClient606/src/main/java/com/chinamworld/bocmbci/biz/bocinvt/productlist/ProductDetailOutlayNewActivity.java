package com.chinamworld.bocmbci.biz.bocinvt.productlist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt.myproduct.ProgressInfoOutlayActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.investTask.BOCDoThreeTask;
import com.chinamworld.bocmbci.biz.investTask.IAction;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.fidget.BTCConstant;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.LoginTask.LoginCallback;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class ProductDetailOutlayNewActivity extends BociBaseActivity implements
		OnClickListener {
	/** 登陆 */
	public static final int ACTIVITY_BUY_LOGIN = 12;
	/** 选择账号 */
	public static final int ACTIVITY_BUY_CHOOSEACC = 13;
	/** 查询信息页 */
	private View view;
	/** 产品详情列表 */
	private Map<String, Object> detailMap;
	/** 产品代码 */
	private TextView tv_prodCode_detail;
	/** 产品名称 */
	private TextView tv_prodName_detail;
	/** 产品币种 */
	private TextView tv_curCode_detail;
	/** 购买价格 */
	private TextView tv_buyPrice_detail;
	/** 产品期限 */
	private TextView tv_prodTimeLimit_detail;
	/** 适用对象 */
	private TextView tv_applyObj_detail;
	/** 产品销售状态 */
	// private TextView tv_status_detail;
	/** 风险等级 */
	private TextView tv_prodRisklvl_detail;
	/** 周期属性 */
	private TextView tv_periodical_detail;
	/** 预计年收益率 */
	private TextView tv_yearlyRR_detail;
	/** 产品种类 */
	// private TextView tv_brandName_detail;
	/** 认购起点金额 */
	private TextView tv_buyStartingAmount_detail;
	/** 追加申购起点金额 */
	private TextView tv_appendStartingAmount_detail;
	/** 产品销售期—开始 */
	private TextView tv_sellingDate_start;
	/** 产品销售期—结束 */
	private TextView tv_sellingDate_end;
	/** 产品成立日 */
	private TextView tv_prodBegin_detail;
	/** 产品到期日 */
	private TextView tv_prodEnd_detail;
	/** 交易渠道 **/
	// private TextView tvTransferChannel;
	/** 非交易时间挂单 **/
	// private TextView tvOutTimeOrder;
	/** 本金到期日 **/
	// private TextView tvPaymentDate;
	/** 付息规则 **/
	// private TextView tvCouponPay;
	/** 赎回起点份额 **/
	// private TextView tvLowlimitAmount;
	/** 最低持有份额 **/
	// private TextView tvLimitholdBalance;
	/** 赎回开放规则 **/
	// private TextView tvSellType;
	/** 赎回本金到账规则 **/
	// private TextView tvSellPaymentType;
	/** 赎回收益到账规则 **/
	// private TextView tvProFit;
	// 按钮
	/** 购买 */
	private Button btn_buy_buydetail;

	/** 理财产品说明书 */
	private Button btn_description_buydetail;
	private Map<String, Object> chooseMap;
	/** 追加认申购起点金额 */
	private TextView tv_appending;
//	private String accountId;
	private String status;
//	private boolean isRed;
	boolean btn_buy_buydetail_outlay;
	boolean btn_agreement_apply_flag;
//	boolean isbuy;// 购买 签约
	// boolean iscontract;// 投资协议申请
	// 购买弹出框
	/** 是否开通投资理财 */
//	private boolean isOpen;
//	/** 是否有风险评估经验 */
//	private boolean isevaluatedBefore;
//	/** 登记账户信息 */
//	private List<Map<String, Object>> investBindingInfo;
//	/** 任务弹出框视图 */
//	private RelativeLayout dialogView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.bocinvt_detail_title));
		view = addView(R.layout.bocinvt_queryproduct_detail_outlay_new);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0,
					0, 0);
		}
//		accountId = getIntent().getStringExtra(Comm.ACCOUNT_ID);
		if (BaseDroidApp.getInstanse().isLogin()) {
			setText(this.getString(R.string.acc_rightbtn_go_main));
			setRightBtnClick(rightBtnClick);
		}
		init();
		setBackBtnClick(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (BaseDroidApp.getInstanse().isLogin()) {
					BociDataCenter.getInstance().clearBociData();
					Intent intent = new Intent();
					intent.setClass(ProductDetailOutlayNewActivity.this,
							QueryProductActivity.class);
					startActivity(intent);
//					ProductDetailOutlayNewActivity.this.finish();

				} else {
					setResult(RESULT_CANCELED);
					finish();
				}

			}
		});
	}
	protected void onResume() {
		super.onResume();
		if (BaseDroidApp.getInstanse().isLogin()) {
			setText(this.getString(R.string.acc_rightbtn_go_main));
			setRightBtnClick(rightBtnClick);
		}	
	}
		
	@SuppressWarnings("unchecked")
	private void init() {
		chooseMap = BociDataCenter.getInstance().getChoosemap();
		detailMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_PRODUCTDETAIL_LIST);
		BociDataCenter.getInstance().setDetailMap(detailMap);
		product_type = Integer.parseInt(detailMap.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTTYPE_RES).toString());
		tv_prodCode_detail = (TextView) view
				.findViewById(R.id.tv_prodCode_detail);
		tv_prodName_detail = (TextView) view
				.findViewById(R.id.tv_prodName_detail);
		tv_curCode_detail = (TextView) view
				.findViewById(R.id.tv_curCode_detail);
		tv_buyPrice_detail = (TextView) view
				.findViewById(R.id.tv_buyPrice_detail);
		tv_prodTimeLimit_detail = (TextView) view
				.findViewById(R.id.tv_prodTimeLimit_detail);
		tv_applyObj_detail = (TextView) view
				.findViewById(R.id.tv_applyObj_detail);
		tv_prodRisklvl_detail = (TextView) view
				.findViewById(R.id.tv_prodRisklvl_detail);
		tv_periodical_detail = (TextView) view
				.findViewById(R.id.tv_periodical_detail);
		tv_yearlyRR_detail = (TextView) view
				.findViewById(R.id.tv_yearlyRR_detail);
		tv_buyStartingAmount_detail = (TextView) view
				.findViewById(R.id.tv_buyStartingAmount_detail);
		tv_appendStartingAmount_detail = (TextView) view
				.findViewById(R.id.tv_appendStartingAmount_detail);
		tv_sellingDate_start = (TextView) view
				.findViewById(R.id.tv_sellingDate_start);
		tv_sellingDate_end = (TextView) view
				.findViewById(R.id.tv_sellingDate_end);
		tv_prodBegin_detail = (TextView) view
				.findViewById(R.id.tv_prodBegin_detail);
		tv_prodEnd_detail = (TextView) view
				.findViewById(R.id.tv_prodEnd_detail);
		tv_appending = (TextView) view.findViewById(R.id.appendStrating);
		btn_agreement_apply = (Button) findViewById(R.id.btn_agreement_apply);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_appending);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.outtimeorder_title));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.paymentdate_title));
		// 赋值操作
		if (detailMap.get(BocInvt.BOCINVT_CAPACITYQUERY_PRODUCTKIND_RES).toString().equals("0")) {//	0:结构性理财产品		1:类基金理财产品
			//结构性理财产品
			switch (Integer.parseInt(((String)detailMap.get("isLockPeriod")))) {
			case 0:{//非业绩基准型产品
				
			}break;
			default:{//业绩基准型产品
				//2016/3/22业务确认，不增加"单日"字样
//				((TextView)findViewById(R.id.tv_start_money_1)).setText("（单日）"+getResources().getString(R.string.bocinvt_subamount));
//				tv_appending.setText("（单日）"+getResources().getString(R.string.bocinvt_addamount));
			}break;
			}
			setProgress();
		}else {//类基金理财产品
			switch (product_type) {//净值型产品
			case 2:{
				((TextView)findViewById(R.id.yearlyRR_detail)).setText("单位净值");
				findViewById(R.id.layout_update).setVisibility(View.VISIBLE);
				tv_yearlyRR_detail.setTextColor(this.getResources().getColor(R.color.red));
				tv_yearlyRR_detail.setText(StringUtil.append2Decimals(detailMap.get("price").toString(), 4));//单位净值							
				((TextView)findViewById(R.id.tv_update)).setText(detailMap.get("priceDate").toString());
			}break;
			default:{
				setProgress();
			}break;
			}
		}
		tv_prodCode_detail.setText((String) detailMap
				.get(BocInvt.BOCI_DETAILPRODCODE_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodCode_detail);
		tv_prodName_detail.setText((String) detailMap.get(BocInvt.BOCI_DETAILPRODNAME_RES)
				+"("+String.valueOf(detailMap.get("prodCode"))+")");
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,tv_prodName_detail);
		String curcode = (String) detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES);
		/** 产品币种 */
		tv_curCode_detail.setText(LocalData.Currency.get(curcode));
		String buyPrice = (String) detailMap
				.get(BocInvt.BOCI_DETAILBUYPRICE_RES);
		tv_buyPrice_detail.setText(StringUtil.parseStringCodePattern(curcode,
				buyPrice, 2));
//		String timeLimit = (String) detailMap
//				.get(BocInvt.BOCI_DETAILPRODTIMELIMIT_RES);
//		tv_prodTimeLimit_detail.setText(timeLimit + DAY);
		/** 产品期限 */
		if (String.valueOf(detailMap.get("productKind")).equals("0")) {//结构性理财产品
			switch (Integer.parseInt(detailMap.get("isLockPeriod").toString())) {
			case 0:{//0：非业绩基准产品
				if (detailMap.get("productTermType").toString().equals("3")) {//产品期限特性,3：无限开放式
					tv_prodTimeLimit_detail.setText("无固定期限");
				}else {
					tv_prodTimeLimit_detail.setText(String.valueOf(detailMap.get(BocInvt.BOCI_DETAILPRODTIMELIMIT_RES)) + "天");
				}
			}break;
			case 1:{//1：业绩基准-锁定期转低收益 
				tv_prodTimeLimit_detail.setText("最低持有"+String.valueOf(detailMap.get(BocInvt.BOCI_DETAILPRODTIMELIMIT_RES)) + "天");
			}break;
			case 2:{//2：业绩基准-锁定期后入账 
				tv_prodTimeLimit_detail.setText(String.valueOf(detailMap.get(BocInvt.BOCI_DETAILPRODTIMELIMIT_RES)) + "天");
			}break;
			case 3:{//3：业绩基准-锁定期周期滚续
				tv_prodTimeLimit_detail.setText(String.valueOf(detailMap.get(BocInvt.BOCI_DETAILPRODTIMELIMIT_RES)) + "天");
			}break;
			default:
				break;
			}
		}else {//类基金理财产品
			tv_prodTimeLimit_detail.setText(String.valueOf(detailMap.get(BocInvt.BOCI_DETAILPRODTIMELIMIT_RES))+"天");
		}
		
		tv_applyObj_detail.setText(prodTimeLimitMap.get((String) detailMap
				.get(BocInvt.BOCI_DETAILAPPLYOBJ_RES)));
		status = (String) detailMap.get(BocInvt.BOCI_DETAILSTATUS_RES);

		String buyStartingAmount = (String) detailMap.get(BocInvt.SUBAMOUNT);
		tv_buyStartingAmount_detail.setText(StringUtil.parseStringCodePattern(
				curcode, buyStartingAmount, 2));
		findViewById(R.id.ll_btn).setVisibility(View.VISIBLE);
		String appendStartingAmount = (String) detailMap.get(BocInvt.ADDAMOUNT);
		tv_appendStartingAmount_detail.setText(StringUtil
				.parseStringCodePattern(curcode, appendStartingAmount, 2));
		tv_sellingDate_start.setText((String) detailMap
				.get(BocInvt.BOCI_DETAILSELLINGSTARTINGDATE_RES)
				+ ConstantGloble.BOCINVT_DATE_ADD);
		tv_sellingDate_end.setText((String) detailMap
				.get(BocInvt.BOCI_DETAILSELLINGENDINGDATE_RES));
		tv_prodBegin_detail.setText((String) detailMap
				.get(BocInvt.BOCI_DETAILPRODBEGIN_RES));
		//产品到期日
		if (detailMap.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTKIND_RES).toString().equals("0")//0:结构性理财产品
				&&!detailMap.get("isLockPeriod").toString().equals("0")//0：非业绩基准产品
				||detailMap.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTKIND_RES).toString().equals("0")
				&&detailMap.get("isLockPeriod").toString().equals("0")
				&&!detailMap.get("productTermType").toString().equals("3")) {
			tv_prodEnd_detail.setText((String) detailMap.get(BocInvt.BOCI_DETAILPRODEND_RES));
		}else {
			tv_prodEnd_detail.setText("长期");
		}
//		tv_prodEnd_detail.setText((String) detailMap
//				.get(BocInvt.BOCI_DETAILPRODEND_RES));
		btn_buy_buydetail = (Button) view
				.findViewById(R.id.btn_buy_buydetail_outlay);
		btn_description_buydetail = (Button) view
				.findViewById(R.id.btn_description_buydetail_outlay);
		String risk = (String) detailMap
				.get(BocInvt.BOCI_DETAILPRODRISKLVL_RES);
		String pstr = LocalData.boci_prodRisklvl_OutlayMap.get(risk);
		int pstart = pstr.indexOf("风险");
		SpannableStringBuilder pstyle = new SpannableStringBuilder(pstr);
		pstyle.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.outlay_blue_bg)), 0, pstart,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		pstyle.setSpan(new ForegroundColorSpan(Color.WHITE), 0, pstart,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_prodRisklvl_detail.setText(pstyle);

		String str = BociDataCenter.prodRiskType.get(detailMap
				.get(BocInvt.PRODRISKTYPE));
		int bstart = str.indexOf("保本");
		SpannableStringBuilder style = new SpannableStringBuilder(str);
		style.setSpan(new BackgroundColorSpan(this.getResources().getColor(
				R.color.orange)), 0, bstart + 2,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		style.setSpan(new ForegroundColorSpan(Color.WHITE), 0, bstart + 2,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		((TextView) view.findViewById(R.id.prodRiskType)).setText(style);
		((TextView) view.findViewById(R.id.baseAmount))
				.setText((String) detailMap
						.get(BocInvt.BOCINVT_SIGNRESULT_BASEAMOUNT_REQ));

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) view.findViewById(R.id.yearlyRR_detail));

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) view.findViewById(R.id.buyStartingAmount_detail));
//		String periodical = (String) detailMap.get(BocInvt.BOCI_DETAILPERIODICAL_RES);
		// tv_periodical_detail.setText(LocalData.periodicalList.get(Integer
		// .valueOf(periodical)));

//		if (!isBuyCheck()) {
//			// 为true是可签约，p603开始改为投资协议申请
//
////			btn_buy_buydetail.setText(getResources().getString(
////					R.string.product_qianyue
////					));
////			btn_buy_buydetail.setOnClickListener(this);
//			tv_periodical_detail.setText(LocalData.periodicalList.get(1));
//			btn_buy_buydetail.setVisibility(View.GONE);
//			btn_agreement_apply.setVisibility(View.VISIBLE);
//			btn_agreement_apply.setOnClickListener(btnMoreNullBocinvtClick);
//		} else {
//			// 非周期性产品可购买
//			tv_periodical_detail.setText(LocalData.periodicalList.get(0));
//			// TODO 401 判断是否允许定投与自动投资
//			if (!StringUtil.isNullOrEmpty(chooseMap)
//					&& LocalData.orderTimeMap.get(0).equals(
//							chooseMap.get(BocInvt.BOCI_AUTOPERMIT_RES))) {
////				btn_buy_buydetail.setText(getResources().getString(R.string.product_more));
////				btn_buy_buydetail.setOnClickListener(btnMoreNullBocinvtClick);
////				String[] service = {
////						getResources().getString(R.string.product_buy),
////						getResources().getString(R.string.product_contract) };
////				PopupWindowUtils.getInstance().setshowMoreChooseDownListener(
////						BaseDroidApp.getInstanse().getCurrentAct(),
////						btn_buy_buydetail, service, btnMoreNullBocinvtClick);
//				btn_buy_buydetail.setText(getResources().getString(R.string.product_buy));
//				btn_buy_buydetail.setVisibility(View.VISIBLE);
//				btn_agreement_apply.setVisibility(View.VISIBLE);
//				btn_agreement_apply.setText(getResources().getString(R.string.product_contract));
//				btn_buy_buydetail.setOnClickListener(btnMoreNullBocinvtClick);
//				btn_agreement_apply.setOnClickListener(btnMoreNullBocinvtClick);
//			} else {
//				btn_buy_buydetail.setText(getResources().getString(R.string.product_buy));
////				btn_buy_buydetail.setOnClickListener(this);
//				btn_buy_buydetail.setOnClickListener(btnMoreNullBocinvtClick);
//				btn_buy_buydetail.setVisibility(View.VISIBLE);
//				btn_agreement_apply.setVisibility(View.GONE);
//			}
//		}
		if (isBuyCheck()) {//周期性产品
			btn_buy_buydetail.setVisibility(View.GONE);
			btn_agreement_apply.setVisibility(View.VISIBLE);
			btn_agreement_apply.setText(getResources().getString(R.string.product_contract));
			btn_agreement_apply.setOnClickListener(btnMoreNullBocinvtClick);
		}else {
			btn_buy_buydetail.setText(getResources().getString(R.string.product_buy));
			btn_buy_buydetail.setVisibility(View.VISIBLE);
			btn_agreement_apply.setVisibility(View.GONE);
			btn_buy_buydetail.setOnClickListener(btnMoreNullBocinvtClick);
		}
		btn_description_buydetail.setOnClickListener(this);
	}

	private boolean isBuyCheck() {
		if (!StringUtil.isNullOrEmpty(chooseMap)) {
			if (chooseMap.get(BocInvt.BOCI_DETAILPERIODICAL_RES).equals("false")) {
				return false;
			}
		}

		return true;
		//P603开始，所有产品不使用签约，改成投资协议申请
//		return true;
	}

	/** 预计年收益率 */
	private void setProgress() {
		String progressionflag = (String) detailMap
				.get(BocInvt.PROGRESSIONFLAG);
		if (StringUtil.isNull(progressionflag))
			return;
		if (progressionflag.equals("0")) {//否
//			tv_yearlyRR_detail.setText(StringUtil.append2Decimals(
//					(String) detailMap.get(BocInvt.BOCI_DETAILYEARLYRR_RES), 2)
//					+ PER);
			tv_yearlyRR_detail.setText(BocInvestControl.getYearlyRR(detailMap,"yearlyRR","rateDetail"));
			tv_yearlyRR_detail.setTextColor(this.getResources().getColor(
					R.color.red));
		} else if (progressionflag.equals("1")) {

//			tv_yearlyRR_detail.setText(StringUtil.append2Decimals(
//					(String) detailMap.get(BocInvt.BOCI_DETAILYEARLYRR_RES), 2)
//					+ PER);
			tv_yearlyRR_detail.setPadding(5, 5, 5, 5);
			tv_yearlyRR_detail.setTextColor(this.getResources().getColor(R.color.blue));
			tv_yearlyRR_detail.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			tv_yearlyRR_detail.setText(BocInvestControl.getYearlyRR(detailMap,"yearlyRR","rateDetail"));
//			tv_yearlyRR_detail.setBackgroundResource(R.drawable.outlay_textbg);
//			tv_yearlyRR_detail.setText(getString(R.string.progression_outlay));
			tv_yearlyRR_detail.setOnClickListener(mClickListener);
		}
	}

	/** 收益累进 */
	private OnClickListener mClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			BaseHttpEngine.showProgressDialog();
			requestLoginPreOutlayConversationId();
		}
	};

	/**
	 * 功能外置 登录之前的conversationId 返回
	 */
	public void requestLoginPreOutlayConversationIdCallBack(Object resultObj) {
		super.requestLoginPreOutlayConversationIdCallBack(resultObj);
		requestProgressOutlay(
				(String) detailMap.get(BocInvt.BOCI_DETAILPRODCODE_RES), "0",
				true);
	}

	@Override
	public void progressQueryOutlayCallBack(Object resultObj) {
		super.progressQueryOutlayCallBack(resultObj);
		if (StringUtil.isNullOrEmpty(BociDataCenter.getInstance().getProgressionList())) {
			return;
		}
		startActivity(new Intent(ProductDetailOutlayNewActivity.this, ProgressInfoOutlayActivity.class)
				.putExtra(BocInvt.PROGRESS_RECORDNUM, progressRecordNumber)
				.putExtra(BocInvt.BOCI_DETAILPRODNAME_RES,
						(String) detailMap.get(BocInvt.BOCI_DETAILPRODNAME_RES))
				.putExtra(BocInvt.BOCI_PRODUCTCODE_REQ,
						(String) detailMap.get(BocInvt.BOCI_DETAILPRODCODE_RES)));
	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 主界面
//			Intent intent = new Intent(ProductDetailOutlayNewActivity.this,
//					SecondMainActivity.class);
//			startActivity(intent);
			ActivityTaskManager.getInstance().removeAllSecondActivity();
		}
	};

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btn_description_buydetail_outlay) {
//			Intent intent = new Intent(ProductDetailOutlayNewActivity.this,
//					ProductDescriptionActivity.class);
//			startActivity(intent);
//			overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
			
			  Uri uri = Uri.parse(BTCConstant.FIDGET_WEB_URL_CHANPINSHUOMINGSHU+(String) detailMap.get(BocInvt.BOCINVT_BUYRES_PRODCODE_RES));
	          Intent intent = new Intent(Intent.ACTION_VIEW, uri);
	          startActivity(intent);

		}
//		if (v.getId() == R.id.btn_buy_buydetail_outlay) {
//
//			if (!StringUtil.isNull(status)
//					&& LocalData.boci_StatusMap.get(status).equalsIgnoreCase(
//							LocalData.bocinvtXpadStatus.get(1))) {
//
//				// 签约 购买
//				btn_buy_buydetail_outlay = true;
////				isbuy = true;
//				if (BaseDroidApp.getInstanse().isLogin()) {
//					if (isOpen && !StringUtil.isNullOrEmpty(investBindingInfo)&& isevaluatedBefore) {
//						List<Map<String, Object>> list = BociDataCenter.getInstance().getBocinvtAcctList();
//						if (list != null && list.size() == 1) {
//							BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.BOCINVT_BUYINIT_MAP,
//											BociDataCenter.getInstance().getBocinvtAcctList().get(0));
//
//							Intent intent = new Intent(ProductDetailOutlayNewActivity.this,BuyProductChooseActivity.class);
//							intent.putExtra(ConstantGloble.BOICNVT_ISDETAIL_BUY, false);
//							startActivityForResult(intent, ACTIVITY_BUY_CODE);
//
//						} else {
//							Intent intent = new Intent(ProductDetailOutlayNewActivity.this,BuyProductChooseCardOutlayActivity.class);
//							startActivityForResult(intent,ACTIVITY_BUY_CHOOSEACC);
//						}
//
//					} else {
//						BiiHttpEngine.showProgressDialog();
//						requestPsnInvestmentisOpenBefore();
//					}
//
//				} else {
//
//					Intent intent = new Intent(ProductDetailOutlayNewActivity.this,LoginActivity.class);
//					startActivityForResult(intent,ConstantGloble.ACTIVITY_REQUEST_LOGIN_CODE);
//				}
//
//			} else {
//				BaseDroidApp.getInstanse().showInfoMessageDialog(ProductDetailOutlayNewActivity.this.getString(R.string.bocinvt_error_buy));
//			}
//
//		}
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
		if (text.equals(getResources().getString(R.string.product_buy))) {
			// "购买"
			buyClick.onClick(v);
		}
		if (text.equals(getResources().getString(R.string.product_contract))) {
			// "投资协议申请"
			contractClick.onClick(v);
		}

	}

	// 购买
	final OnClickListener buyClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {

			if (!StringUtil.isNull(status)&& LocalData.boci_StatusMap.get(status).equalsIgnoreCase(LocalData.bocinvtXpadStatus.get(1))) {
				btn_buy_buydetail_outlay = true;
//				isbuy = true;
				if (BaseDroidApp.getInstanse().isLogin()) {
					requestSystemDateTime();
//					toBuyOrAgreement();
//					if (isOpen && !StringUtil.isNullOrEmpty(investBindingInfo)&& isevaluatedBefore) {
//						List<Map<String, Object>> list = BociDataCenter.getInstance().getBocinvtAcctList();
//						if (list != null && list.size() == 1) {
//							BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.BOCINVT_BUYINIT_MAP,
//											BociDataCenter.getInstance().getBocinvtAcctList().get(0));
//
//							Intent intent = new Intent(ProductDetailOutlayNewActivity.this,BuyProductChooseActivity.class);
//							intent.putExtra(ConstantGloble.BOICNVT_ISDETAIL_BUY, false);
//							startActivityForResult(intent, ACTIVITY_BUY_CODE);
//
//						} else {
//							Intent intent = new Intent(ProductDetailOutlayNewActivity.this,BuyProductChooseCardOutlayActivity.class);
//							startActivityForResult(intent,ACTIVITY_BUY_CHOOSEACC);
//						}
//
//					} else {
//						BiiHttpEngine.showProgressDialog();
//						requestPsnInvestmentisOpenBefore();
//					}

				} else {
//					Intent intent = new Intent(ProductDetailOutlayNewActivity.this,LoginActivity.class);
//					startActivityForResult(intent,ConstantGloble.ACTIVITY_REQUEST_LOGIN_CODE);
					BaseActivity.getLoginUtils(ProductDetailOutlayNewActivity.this).exe(new LoginCallback() {
						
						@Override
						public void loginStatua(boolean isLogin) {
							if(isLogin){
								loginSuccess();
							}
						}
					});
				}

			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						ProductDetailOutlayNewActivity.this
								.getString(R.string.bocinvt_error_buy));
			}

		}
	};
	// "投资协议申请"
	final OnClickListener contractClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (!StringUtil.isNull(status)
					&& LocalData.boci_StatusMap.get(status).equalsIgnoreCase(LocalData.bocinvtXpadStatus.get(1))) {
//				btn_buy_buydetail_outlay = true;
				btn_agreement_apply_flag=true;
				if (BaseDroidApp.getInstanse().isLogin()) {
					requestSystemDateTime();
//					toBuyOrAgreement();
					
					
//					//风险评估结果数据
//					if (isOpen && !StringUtil.isNullOrEmpty(investBindingInfo)&& isevaluatedBefore
//							&&!StringUtil.isNullOrEmpty(is_outDate)&&String.valueOf(is_outDate).equals("1")) {//风险评估未到期
//
//						List<Map<String, Object>> list = BociDataCenter.getInstance().getBocinvtAcctList();
//						if (list != null && list.size() == 1) {
//							BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.BOCINVT_BUYINIT_MAP,
//											BociDataCenter.getInstance().getBocinvtAcctList().get(0));
////							Intent intent = new Intent(ProductDetailOutlayNewActivity.this,AgreementChooseActivity.class);
////							startActivity(intent);
//							BocInvestControl.toPeriodAgreementApply(ProductDetailOutlayNewActivity.this//跳转到投资协议申请
//									, list.get(0), , null);//=============
//						} else {
//							Intent intent = new Intent(ProductDetailOutlayNewActivity.this,BuyProductChooseCardOutlayActivity.class);
//							startActivityForResult(intent,ACTIVITY_BUY_CHOOSEACC);
//						}
//
//					} else {
//						BiiHttpEngine.showProgressDialog();
//						requestPsnInvestmentisOpenBefore();
//					}

				} else {
//					Intent intent = new Intent(ProductDetailOutlayNewActivity.this,LoginActivity.class);
//					startActivityForResult(intent,ConstantGloble.ACTIVITY_REQUEST_LOGIN_CODE);
					BaseActivity.getLoginUtils(ProductDetailOutlayNewActivity.this).exe(new LoginCallback() {
						
						@Override
						public void loginStatua(boolean isLogin) {
							if(isLogin){
								loginSuccess();
							}
						}
					});
				}

			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						ProductDetailOutlayNewActivity.this
								.getString(R.string.bocinvt_error_buy));
			}
		}
	};
//	/**
//	 * 请求登录后的产品详情
//	 * @param accoundMap 账户信息
//	 * @param productCode 产品代码
//	 */
//	private void requestPsnXpadProductDetailQuery(Map<String, Object> accoundMap,String productCode){
//		
//		Map<String, Object> parms_map=new HashMap<String, Object>();
//		parms_map.put("productKind", String.valueOf(detailMap.get(key)));//=============
//		parms_map.put("productCode", productCode);
//		BaseHttpEngine.showProgressDialog();
//		getHttpTools().requestHttp(BocInvt.PRODUCTDETAILQUERY,"requestPsnXpadProductDetailQueryCallBack", parms_map, false);
//	}
//	/**
//	 * 请求登录后的产品详情  回调
//	 * @param resultObj
//	 */
//	public void requestPsnXpadProductDetailQueryCallBack(Object resultObj){
//		
//	}

	/**
	 * 请求查询登记账户---回调
	 * 
	 * @param resultObj
	 */
//	@SuppressWarnings("unchecked")
//	@Override
//	public void bocinvtAcctCallback(Object resultObj) {
//		super.bocinvtAcctCallback(resultObj);
//		isOpen = isOpenBefore;
//		isevaluatedBefore = isevaluated;
//		investBindingInfo = investBinding;
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		// 得到response
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//
//		if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
//
//		} else {
//			// 得到result
//			Map<String, Object> map = HttpTools.getResponseResult(resultObj);
//			investBindingInfo = (List<Map<String, Object>>) map.get(BocInvt.BOCI_LIST_RES);
//
//		}
//		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.BOCINVT_XPADRESET_CHOOSE, investBindingInfo);
//		if (isOpen && !StringUtil.isNullOrEmpty(investBindingInfo)&& isevaluatedBefore) {
//			BaseHttpEngine.dissMissProgressDialog();
//			if (btn_buy_buydetail_outlay) {
//
//				List<Map<String, Object>> list = BociDataCenter.getInstance().getBocinvtAcctList();
//				if (list != null && list.size() == 1) {
//					if (StringUtil.isNullOrEmpty(list.get(0).get("accountId"))) {//未关联进网银
//						BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.bocinvt_tv_75));//账户未关联提示信息
//					}else {//已关联进网银
//						BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.BOCINVT_BUYINIT_MAP,
//								BociDataCenter.getInstance().getBocinvtAcctList().get(0));
//						if (isbuy) {
//							Intent intent = new Intent(ProductDetailOutlayNewActivity.this,BuyProductChooseActivity.class);
//							intent.putExtra(ConstantGloble.BOICNVT_ISDETAIL_BUY,false);
//							startActivityForResult(intent, ACTIVITY_BUY_CODE);
//						} else {
//						Intent intent = new Intent(this,AgreementChooseActivity.class);
//						startActivity(intent);
//						}
//					}
//					
//				} else {
//					Intent intent = new Intent(ProductDetailOutlayNewActivity.this,BuyProductChooseCardOutlayActivity.class);
//					startActivityForResult(intent, ACTIVITY_BUY_CHOOSEACC);
//				}
//			}
//
//		} else {
//
//			BaseHttpEngine.dissMissProgressDialog();
//			InflaterViewDialog inflater = new InflaterViewDialog(ProductDetailOutlayNewActivity.this);
//			dialogView = (RelativeLayout) inflater.judgeViewDialog(isOpen,
//					investBindingInfo, isevaluatedBefore, manageOpenClick,
//					invtBindingClick, invtEvaluationClick, exitDialogClick);
//			TextView tv = (TextView) dialogView.findViewById(R.id.tv_acc_account_accountState);
//			tv.setText(this.getString(R.string.bocinvt_query_title));
//			BaseDroidApp.getInstanse().showAccountMessageDialog(dialogView);
//
//		}
//	}

	protected OnClickListener exitDialogClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
		}
	};
	/**产品类型*/
	private int product_type;
	private Button btn_agreement_apply;

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:

			if (requestCode == ACTIVITY_BUY_CODE) {
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				BociDataCenter.getInstance().clearBociData();
				Intent intent = new Intent();
				intent.setClass(ProductDetailOutlayNewActivity.this,
						QueryProductActivity.class);
				startActivity(intent);
				ProductDetailOutlayNewActivity.this.finish();
				return;
			}
			if (requestCode == ConstantGloble.ACTIVITY_REQUEST_LOGIN_CODE) {
				if (btn_buy_buydetail_outlay||btn_agreement_apply_flag) {
//					BiiHttpEngine.showProgressDialog();
//					requestPsnInvestmentisOpenBefore();
					requestSystemDateTime();
//					toBuyOrAgreement();
				} else {
					ActivityTaskManager.getInstance().removeAllSecondActivity();
					BociDataCenter.getInstance().clearBociData();
					Intent intent = new Intent();
					intent.setClass(ProductDetailOutlayNewActivity.this,QueryProductActivity.class);
					startActivity(intent);
					ProductDetailOutlayNewActivity.this.finish();
				}
				// setText(this.getString(R.string.acc_rightbtn_go_main));
				// setRightBtnClick(rightBtnClick);

				return;
			}
			if (requestCode == ACTIVITY_BUY_CHOOSEACC) {

//				if (isbuy) {
					Map<String, Object> map_accound = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BOCINVT_BUYINIT_MAP);
					if (!StringUtil.isNullOrEmpty(map_accound.get(BocInvt.BOCIBINDING_ACCOUNTIDF_RES))) {//账户已关联进网银
						if (btn_agreement_apply_flag) {//投资协议申请
							btn_agreement_apply_flag=false;
							requestPsnXpadProductInvestTreatyQuery(map_accound);
							BocInvestControl.toPeriodAgreementApply(ProductDetailOutlayNewActivity.this//跳转到投资协议申请
									, map_accound, detailMap, null);
						}
						if (btn_buy_buydetail_outlay) {//购买
							btn_buy_buydetail_outlay=false;
							BocInvestControl.toBuyActivity(ProductDetailOutlayNewActivity.this, String.valueOf(detailMap.get("productKind"))
									,String.valueOf(detailMap.get("prodCode")) , map_accound, dateTime, true, ACTIVITY_BUY_CODE);
//							Intent intent = new Intent(ProductDetailOutlayNewActivity.this,BuyProductChooseActivity.class);
//							intent.putExtra(ConstantGloble.BOICNVT_ISDETAIL_BUY, false);
//							startActivityForResult(intent, ACTIVITY_BUY_CODE);
						}
					}else {//账户未关联进网银
						BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.bocinvt_tv_75));//账户未关联提示信息
					}
					
//				}
//				else {
//					Intent intent = new Intent(this,AgreementChooseActivity.class);
//					startActivity(intent);
//				}

				return;
			}
//			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
//				// 开通成功的响应
//				isOpen = true;
//
//			} else if (requestCode == ConstantGloble.ACTIVITY_REQUEST_INVTBINDING_CODE) {
//				// 登记账户成功的响应
//				investBindingInfo = BociDataCenter.getInstance()
//						.getUnSetAcctList();
//
//			} else if (requestCode == ConstantGloble.ACTIVITY_REQUEST_INVTEVALUATION_CODE) {
//				// 风险评估成功的响应
//				if (BociDataCenter.getInstance().getI() == 1) {
//
//				} else {
//					isevaluatedBefore = true;
//				}
//
//			}
//			if (isOpen && !StringUtil.isNullOrEmpty(investBindingInfo)
//					&& isevaluatedBefore) {
//				BaseHttpEngine.showProgressDialog();
//				requestBociAcctList("1", "1");
//			} else {
//				InflaterViewDialog inflater = new InflaterViewDialog(
//						ProductDetailOutlayNewActivity.this);
//				dialogView = (RelativeLayout) inflater.judgeViewDialog(isOpen,
//						investBindingInfo, isevaluatedBefore, manageOpenClick,
//						invtBindingClick, invtEvaluationClick, exitDialogClick);
//				TextView tv = (TextView) dialogView
//						.findViewById(R.id.tv_acc_account_accountState);
//				tv.setText(this.getString(R.string.bocinvt_query_title));
//				BaseDroidApp.getInstanse().showAccountMessageDialog(dialogView);
//			}

			break;
		case RESULT_CANCELED:
//			if (BaseDroidApp.getInstanse().isLogin()) {
//				if (isOpen && !StringUtil.isNullOrEmpty(investBindingInfo)
//						&& isevaluatedBefore) {
//				} else {
//					InflaterViewDialog inflater = new InflaterViewDialog(
//							ProductDetailOutlayNewActivity.this);
//					dialogView = (RelativeLayout) inflater.judgeViewDialog(
//							isOpen, investBindingInfo, isevaluatedBefore,
//							manageOpenClick, invtBindingClick,
//							invtEvaluationClick, exitDialogClick);
//					TextView tv = (TextView) dialogView
//							.findViewById(R.id.tv_acc_account_accountState);
//					tv.setText(this.getString(R.string.bocinvt_query_title));
//					BaseDroidApp.getInstanse().showAccountMessageDialog(
//							dialogView);
//
//				}
//			}
//			btn_buy_buydetail_outlay = false;
//			btn_agreement_apply_flag=false;
//			isbuy = false;
			break;
		default:
			break;
		}
	}
	
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		toBuyOrAgreement();
	}
	
	/**
	 * 请求判断  是否开通投资理财、风险评估、账户登记    并在完成后  	跳转到购买或投资协议申请
	 */
	private void toBuyOrAgreement(){
		//请求开通投资理财、风险评估、账户登记任务是否完成
		BOCDoThreeTask task = BOCDoThreeTask.getInstance(ProductDetailOutlayNewActivity.this);
		registActivityEvent(task);
		task.doTask(new IAction() {
			@SuppressWarnings("unchecked")
			@Override
			public void SuccessCallBack(Object param) {
				BaseDroidApp.getInstanse().dismissMessageDialog();
				List<Map<String, Object>> list = (List<Map<String, Object>>) BaseDroidApp
						.getInstanse().getBizDataMap().get(ConstantGloble.BOCINVT_XPADRESET_CHOOSE);//账户列表
				BociDataCenter.getInstance().setBocinvtAcctList(list);
				if (list != null && list.size() == 1) {
					if (!StringUtil.isNullOrEmpty(list.get(0).get(BocInvt.BOCIBINDING_ACCOUNTIDF_RES))) {//账户已关联进网银
						if (btn_agreement_apply_flag) {//投资协议申请
							btn_agreement_apply_flag=false;
							requestPsnXpadProductInvestTreatyQuery(list.get(0));
							BocInvestControl.toPeriodAgreementApply(ProductDetailOutlayNewActivity.this//跳转到投资协议申请
									, list.get(0), detailMap, null);
						}
						if (btn_buy_buydetail_outlay) {//购买
							btn_buy_buydetail_outlay=false;
							BocInvestControl.toBuyActivity(ProductDetailOutlayNewActivity.this, String.valueOf(detailMap.get("productKind"))
									,String.valueOf(detailMap.get("prodCode")) , list.get(0), dateTime, true, ACTIVITY_BUY_CODE);
//							Intent intent = new Intent(ProductDetailOutlayNewActivity.this,BuyProductChooseActivity.class);
//							intent.putExtra(ConstantGloble.BOICNVT_ISDETAIL_BUY, false);
//							startActivityForResult(intent, ACTIVITY_BUY_CODE);
						}
					}else {//账户未关联进网银
						BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.bocinvt_tv_75));//账户未关联提示信息
					}
				}else {//进入选择账户页
					Intent intent = new Intent(ProductDetailOutlayNewActivity.this,BuyProductChooseCardOutlayActivity.class);
					startActivityForResult(intent,ACTIVITY_BUY_CHOOSEACC);
				}
			}
		}, 3);
	}

	/*
	 * 返回键 未登陆 直接finish 登陆 跳转到之原始查询页面
	 */
	public void onBackPressed() {
		if (BaseDroidApp.getInstanse().isLogin()) {
			BociDataCenter.getInstance().clearBociData();
			Intent intent = new Intent();
			intent.setClass(ProductDetailOutlayNewActivity.this,
					QueryProductActivity.class);
			startActivity(intent);
			ProductDetailOutlayNewActivity.this.finish();

		} else {
			setResult(RESULT_CANCELED);
			finish();
		}

	}
	
	/**
	 * 请求 投资协议列表查询
	 * @param _refresh 是否重新加载
	 */
	public void requestPsnXpadProductInvestTreatyQuery(Map<String, Object> map){
		Map<String, Object> parms_map=new HashMap<String, Object>();
		parms_map.put(BocInvt.BOCI_ACCOUNTID_REQ,map.get(BocInvt.BOCIBINDING_ACCOUNTIDF_RES).toString());
		parms_map.put(BocInvestControl.PROID, detailMap.get(BocInvt.BOCI_DETAILPRODCODE_RES).toString());
		parms_map.put(BocInvt.BOCINCT_XPADTRAD_PAGESIZE_REQ, BocInvestControl.PAGESIZE);
		parms_map.put(BocInvestControl.AGRTYPE, "0");
		parms_map.put(BocInvestControl.INSTTYPE, "0");
		parms_map.put(BocInvt.BOCINCT_XPADTRAD_CURRENTINDEX_REQ, "0");
		parms_map.put(BocInvt.BOCINCT_XPADTRAD_REFRESH_REQ, "true");
		BaseHttpEngine.showProgressDialog();
		getHttpTools().requestHttp(BocInvestControl.PSNXPADPRODUCTINVESTTREATYQUERY, "requestPsnXpadProductInvestTreatyQueryCallBack", parms_map, true);
	}
	/**
	 * 请求 投资协议列表查询 回调
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public void requestPsnXpadProductInvestTreatyQueryCallBack(Object resultObj){
		if (StringUtil.isNullOrEmpty(resultObj)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		BocInvestControl.map_listview_choose.clear();
		Map<String, Object> result_map = (Map<String, Object>)getHttpTools().getResponseResult(resultObj);
		List<Map<String, Object>> list_agreement_all = (List<Map<String, Object>>) result_map.get(BocInvestControl.key_list);
		if (!StringUtil.isNullOrEmpty(list_agreement_all)&&
				Integer.parseInt(result_map.get("recordNumber").toString()) == 1) {
			BaseHttpEngine.dissMissProgressDialog();
			BocInvestControl.map_listview_choose.putAll(list_agreement_all.get(0));
			
			return;
		}else{
			
		}

		BaseHttpEngine.dissMissProgressDialog();
	}
	
	public void loginSuccess(){
		if (btn_buy_buydetail_outlay||btn_agreement_apply_flag) {
			requestSystemDateTime();
		} else {
			ActivityTaskManager.getInstance().removeAllSecondActivity();
			BociDataCenter.getInstance().clearBociData();
			Intent intent = new Intent();
			intent.setClass(ProductDetailOutlayNewActivity.this,QueryProductActivity.class);
			startActivity(intent);
			ProductDetailOutlayNewActivity.this.finish();
		}
		return;
	}
}
