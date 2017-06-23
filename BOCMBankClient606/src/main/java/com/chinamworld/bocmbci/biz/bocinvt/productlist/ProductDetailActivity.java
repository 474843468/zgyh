package com.chinamworld.bocmbci.biz.bocinvt.productlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.SecondMainActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt.BocinvtUtils;
import com.chinamworld.bocmbci.biz.bocinvt.myproduct.ProgressInfoActivity;
import com.chinamworld.bocmbci.biz.bocinvt.myproduct.RedeemInputActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.productqueryandbuy.activity.ProductQueryAndBuyAgreementApplyActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.productqueryandbuy.activity.ProductQueryAndBuyAgreementApplyAgreementBaseMessageActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.productqueryandbuy.activity.ProductQueryAndBuyComboBuyStateActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.productqueryandbuy.activity.ProductQueryAndBuyProfitComputeActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.productqueryandbuy.activity.ProductQueryAndBuyYearRateActivity;
import com.chinamworld.bocmbci.biz.investTask.BOCDoThreeTask;
import com.chinamworld.bocmbci.biz.investTask.IAction;
import com.chinamworld.bocmbci.biz.plps.adapter.AnnuitySpinnerAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.PublicTools;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 产品详情页面
 * 
 * @author wangmengmeng
 * 
 */
public class ProductDetailActivity extends BociBaseActivity implements
		OnClickListener {
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
	private TextView tv_status_detail;
	/** 风险等级 */
	private TextView tv_prodRisklvl_detail;
	/** 周期属性 */
//	private TextView tv_periodical_detail;//P603删除字段
	/** 预计年收益率 */
	private TextView tv_yearlyRR_detail;
	/** 产品种类 */
	private TextView tv_brandName_detail;
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
	/** 交易渠道  **/
	private TextView tvTransferChannel;
	/** 非交易时间挂单  **/
//	private TextView tvOutTimeOrder;//P603删除字段
	/** 本金到期日 **/
	private TextView tvPaymentDate;
	/** 付息规则  **/
	private TextView tvCouponPay;
	/** 赎回起点份额  **/
	private TextView tvLowlimitAmount;
	/** 最低持有份额  **/
	private TextView tvLimitholdBalance;
	/** 赎回开放规则  **/
	private TextView tvSellType;
	/** 赎回本金到账规则 **/
	private TextView tvSellPaymentType;
	/** 赎回收益到账规则   **/
	private TextView tvProFit;
	/** 是否进行过风险评估 */
	private boolean isevaluatedBefore;
	// 按钮
	/** 购买 */
//	private Button btn_buy_buydetail;//P603改用按钮布局
	/** 签约 */
//	private Button btn_contract_detail;//P603改用按钮布局
	/** 理财产品说明书 */
//	private Button btn_description_buydetail;//P603改用按钮布局
	private Map<String, Object> chooseMap;
	/** 追加认申购起点金额 */
	private TextView tv_appending;
	/** 理财交易账户 **/
	private TextView tvAcct;
	private String accountId;
	private String status;
	/** 指令产品  0 是周期性  1否**/
	private String isPre;
	/** 指令交易产品交易类型  **/
	private String transType;
	private int index;
	private boolean isRed;
	private String availableAmt;//产品剩余额度
	/**产品类型*/
	private TextView tv_product_type;
	/**交易时间*/
	private TextView tv_product_buy_time;
	/**挂单时间*/
	private TextView tv_product_order_time;
	/**预计年收益率字段，业绩基准型产品独有*/
	private View layout_yearlyRR;
	/**单位净值-字段，类基金理财产品独有*/
	private View layout_jingzhi;
	/**1:产品购买、2:投资协议申请、3:组合购买*/
	private int buy_agreement_flag;
	/** 剩余额度 */
	private TextView tv_sy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.bocinvt_detail_title));
		setText(this.getString(R.string.acc_rightbtn_go_main));
		view = addView(R.layout.bocinvt_queryproduct_detail);
		accountId = getIntent().getStringExtra(Comm.ACCOUNT_ID);
//		status = getIntent().getStringExtra(BocInvt.BOCI_XPADSTATUS_REQ);
		if (getIntent().hasExtra(BocInvt.ISPRE)) {
			isPre = getIntent().getStringExtra(BocInvt.ISPRE);
		}
		if (getIntent().hasExtra(BocInvt.TRANSTYPE)) {
			transType = getIntent().getStringExtra(BocInvt.TRANSTYPE);
		}
		if (getIntent().hasExtra(BocInvt.AVAILABLEAMT)) {
			availableAmt = getIntent().getStringExtra(BocInvt.AVAILABLEAMT);
		}
		setRightBtnClick(rightBtnClick);
		init();
		setBackBtnClick(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BocInvestControl.clearDate_productQueryAndBuy();
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void init() {
		chooseMap = BociDataCenter.getInstance().getChoosemap();
		detailMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_PRODUCTDETAIL_LIST);
		BociDataCenter.getInstance().setDetailMap(detailMap);
		btn_buy = (Button) findViewById(R.id.btn_buy);
		btn_more = (Button) findViewById(R.id.btn_more);
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
		tv_status_detail = (TextView) view.findViewById(R.id.tv_status_detail);
		tv_sy = (TextView) findViewById(R.id.tv_sy);
		tv_prodRisklvl_detail = (TextView) view
				.findViewById(R.id.tv_prodRisklvl_detail);
//		tv_periodical_detail = (TextView) view
//				.findViewById(R.id.tv_periodical_detail);
		tv_yearlyRR_detail = (TextView) view
				.findViewById(R.id.tv_yearlyRR_detail);
		tv_brandName_detail = (TextView) view
				.findViewById(R.id.tv_brandName_detail);
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
		tvTransferChannel = (TextView) view.findViewById(R.id.transfer_channel);
//		tvOutTimeOrder = (TextView) view.findViewById(R.id.outtimeorder);
		tvPaymentDate = (TextView) view.findViewById(R.id.paymentdate);
		tvCouponPay = (TextView) view.findViewById(R.id.couponpay);
		tvLowlimitAmount = (TextView) view.findViewById(R.id.lowlimitamount);
		tvLimitholdBalance = (TextView) view.findViewById(R.id.limitholdbalance);
		tvSellType = (TextView) view.findViewById(R.id.selltype);
		tvSellPaymentType = (TextView) view.findViewById(R.id.sellpaymenttype);
		tvProFit = (TextView) view.findViewById(R.id.profit);
		tv_product_type = (TextView) view.findViewById(R.id.tv_product_type);
		tv_product_buy_time = (TextView) view.findViewById(R.id.tv_product_buy_time);
		tv_product_order_time = (TextView) view.findViewById(R.id.tv_product_order_time);
		layout_yearlyRR = view.findViewById(R.id.layout_yearlyRR);
		layout_jingzhi = view.findViewById(R.id.layout_jingzhi);
		 paymenttype = (TextView) view.findViewById(R.id.paymenttype);
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_appending);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView)findViewById(R.id.outtimeorder_title));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView)findViewById(R.id.paymentdate_title));
		View layout_rgsxf = findViewById(R.id.layout_rgsxf);
		View layout_sgsxf = findViewById(R.id.layout_sgsxf);
		View layout_shsxf = findViewById(R.id.layout_shsxf);
		View layout_fdglf = findViewById(R.id.layout_fdglf);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView)findViewById(R.id.tv_l_shsxf));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView)findViewById(R.id.tv_l_fdglf));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView)findViewById(R.id.tv_l_rgsxf));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView)findViewById(R.id.tv_l_sgsxf));
		layout_femz = findViewById(R.id.layout_femz);
		TextView tv_tip = (TextView) findViewById(R.id.tv_tip);
		View layout_jingzhi_date = findViewById(R.id.layout_jingzhi_date);
		View layout_benjin_date = findViewById(R.id.layout_benjin_date);
		View layout_fugz = findViewById(R.id.layout_fugz);
		View layout_shsydzgz = findViewById(R.id.layout_shsydzgz);
		TextView tv_jingzhi_date = (TextView) findViewById(R.id.tv_jingzhi_date);
		// 赋值操作
		if (detailMap.get(BocInvestControl.PRODUCTKIND).toString().equals("0")) {//非净值型产品
			layout_rgsxf.setVisibility(View.GONE);
			layout_sgsxf.setVisibility(View.GONE);
			layout_shsxf.setVisibility(View.GONE);
			layout_fdglf.setVisibility(View.GONE);
			layout_shsydzgz.setVisibility(View.VISIBLE);
		}else {
			layout_rgsxf.setVisibility(View.VISIBLE);
			layout_sgsxf.setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.tv_rgsxf)).setText(getNull(detailMap.get("subscribeFee")));
			((TextView) findViewById(R.id.tv_sgsxf)).setText(getNull(detailMap.get("purchFee")));
			layout_shsxf.setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.tv_shsxf)).setText(getNull(detailMap.get("redeemFee")));
			layout_fdglf.setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.tv_fdglf)).setText("实际年化收益大于"
					+StringUtil.append2Decimals(detailMap.get("pfmcDrawStart").toString(), 2)
					+"%时，超出部分收益按照"
					+StringUtil.append2Decimals(detailMap.get("pfmcDrawScale").toString(), 2)
					+"%收取业绩报酬");
			layout_shsydzgz.setVisibility(View.GONE);
			paymenttype.setText("资金到账规则：");
		}
		setProgress();//收益累进产品，预计年收益率设置链接
		String curcode = (String) detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES);
		/** 产品币种 */
		tv_curCode_detail.setText(LocalData.Currency.get(curcode));
		if (detailMap.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTTYPE_RES).toString().equals("2")) {//1：现金管理类产品	2：净值开放类产品 3：固定期限产品
			tv_tip.setText(getResources().getString(R.string.bocinvt_buykoukuan_value_1));
			layout_femz.setVisibility(View.GONE);
			layout_yearlyRR.setVisibility(View.GONE);
			layout_jingzhi.setVisibility(View.VISIBLE);
			layout_jingzhi_date.setVisibility(View.VISIBLE);
			TextView tv_jingzhi_link = (TextView) findViewById(R.id.tv_jingzhi_link);
			if (detailMap.get("transTypeCode").toString().equals("1")) {//0：认购    1：申购
				tv_jingzhi_link.setTextColor(getResources().getColor(R.color.blue));
				tv_jingzhi_link.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
				tv_jingzhi_link.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//跳转到历史净值页
						BocInvestControl.toEchartWebViewActivity(ProductDetailActivity.this, 
								detailMap.get(BocInvt.BOCI_DETAILPRODCODE_RES).toString(),"1", 
								detailMap.get(BocInvestControl.PRICE)+"",
								detailMap.get("priceDate").toString(),
								false, 123);
					}
				});
			}else {
				tv_jingzhi_link.setTextColor(getResources().getColor(R.color.red));
			}
			tv_jingzhi_link.setText(StringUtil.append2Decimals(detailMap.get("price").toString(), 4));//单位净值
			tv_jingzhi_date.setText(detailMap.get("priceDate").toString());//净值日期
		}else {
			layout_femz.setVisibility(View.VISIBLE);
			layout_jingzhi.setVisibility(View.GONE);
			layout_jingzhi_date.setVisibility(View.GONE);
			layout_yearlyRR.setVisibility(View.VISIBLE);
			String buyPrice = (String) detailMap.get(BocInvt.BOCI_DETAILBUYPRICE_RES);
			tv_buyPrice_detail.setText(/*StringUtil.parseStringCodePattern(curcode,buyPrice, 2)*/
					StringUtil.parseStringPattern(buyPrice, 2));
			if (!String.valueOf(detailMap.get("isLockPeriod")).equals("0")) {//业绩基准型产品
				tv_tip.setText(getResources().getString(R.string.bocinvt_buykoukuan_value));
				tv_yearlyRR_detail.setTextColor(getResources().getColor(R.color.blue));
				tv_yearlyRR_detail.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
				tv_yearlyRR_detail.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						BaseHttpEngine.showProgressDialog();
						requestSystemDateTime();
					}
				});
			}else {
				tv_tip.setText(getResources().getString(R.string.bocinvt_buykoukuan_value_2));
			}
			tv_yearlyRR_detail.setText(BocInvestControl.getYearlyRR(detailMap,BocInvt.BOCI_YEARLYRR_RES,BocInvestControl.RATEDETAIL));
		}
		tv_product_type.setText(BocInvestControl.list_issueType.get(Integer.parseInt((String)detailMap
				.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTTYPE_RES))));
//		tv_product_buy_time.setText((String) detailMap.get(BocInvt.BOCI_TRANSTIME));
		tv_product_buy_time.setText((String) detailMap.get(BocInvestControl.STARTTIME)+"--"+(String) detailMap.get(BocInvestControl.ENDTIME));
//		tv_product_order_time.setText((String) detailMap.get(BocInvt.BOCINVT_BUYPRE_ORDERTIME_RES));
		if (((String)detailMap.get("outTimeOrder")).equals("1")) {//0：不允许1：允许
			tv_product_order_time.setText((String) detailMap.get(BocInvestControl.ORDERSTARTTIME)+"--"+(String) detailMap.get(BocInvestControl.ORDERENDTIME));
		}else {
			tv_product_order_time.setText("不允许挂单");
		}
		tv_prodCode_detail.setText((String) detailMap
				.get(BocInvt.BOCI_DETAILPRODCODE_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodCode_detail);
		tv_prodName_detail.setText((String) detailMap
				.get(BocInvt.BOCI_DETAILPRODNAME_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodName_detail);
//		String timeLimit = (String) detailMap
//				.get(BocInvt.BOCI_DETAILPRODTIMELIMIT_RES);
//		tv_prodTimeLimit_detail.setText(timeLimit + DAY);
		if ((detailMap.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTKIND_RES).toString().equals("0")//0:结构性理财产品
				&&!detailMap.get("isLockPeriod").toString().equals("0"))//0：非业绩基准产品
				||(detailMap.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTKIND_RES).toString().equals("0")
				&&detailMap.get("isLockPeriod").toString().equals("0")
				&&!detailMap.get("productTermType").toString().equals("3"))) {
			tv_prodTimeLimit_detail.setText(BocInvestControl.getProductLimit());
			tv_prodEnd_detail.setText((String) detailMap.get(BocInvt.BOCI_DETAILPRODEND_RES));
		}else {
			tv_prodTimeLimit_detail.setText("无固定期限");
			tv_prodEnd_detail.setText("长期");
		}
//		tv_applyObj_detail.setText(prodTimeLimitMap.get((String) detailMap
//				.get(BocInvt.BOCI_DETAILAPPLYOBJ_RES)));
		String applyObjStr = "适用于";
		if("0".equals((String) detailMap.get(BocInvt.BOCI_DETAILAPPLYOBJ_RES))){
			applyObjStr = applyObjStr + "具有投资经验";
		}
		if(!"0".equals((String) detailMap.get("custLevelSale"))){//适用的客户等级
			if("0".equals((String) detailMap.get(BocInvt.BOCI_DETAILAPPLYOBJ_RES))){
				applyObjStr = applyObjStr + "、";
			}
			applyObjStr = applyObjStr + BocInvestControl.custLevelSaleMap.get((String) detailMap
					.get("custLevelSale"));
		}
		if(!"0".equals((String) detailMap.get("prodRisklvl"))){//产品风险级别
			if("0".equals((String) detailMap.get(BocInvt.BOCI_DETAILAPPLYOBJ_RES))||
					!"0".equals((String) detailMap.get("custLevelSale"))){
				applyObjStr = applyObjStr + "、";
			}
			applyObjStr = applyObjStr + BocInvestControl.prodRisklvlMap.get((String) detailMap
					.get("prodRisklvl"));
		}
		if("1".equals((String) detailMap.get(BocInvt.BOCI_DETAILAPPLYOBJ_RES))
				&& "0".equals((String) detailMap.get("custLevelSale"))
				&& "0".equals((String) detailMap.get("prodRisklvl"))){
			tv_applyObj_detail.setText("适用于全部客户");
		}else{
			tv_applyObj_detail.setText(applyObjStr + "的客户");
		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_applyObj_detail);
		status = (String) detailMap.get(BocInvt.BOCI_DETAILSTATUS_RES);
		tv_status_detail.setText(LocalData.boci_StatusMap.get(status));
		String risk = (String) detailMap
				.get(BocInvt.BOCI_DETAILPRODRISKLVL_RES);
		tv_prodRisklvl_detail.setText(LocalData.boci_prodRisklvlMap.get(risk));

		tv_brandName_detail.setText((String) detailMap
				.get(BocInvt.BOCI_DETAILBRANDNAME_RES));
		String buyStartingAmount = (String) detailMap
				.get(BocInvt.SUBAMOUNT);
		tv_buyStartingAmount_detail.setText(StringUtil.parseStringCodePattern(
				curcode, buyStartingAmount, 2));
		if (getIntent().hasExtra(BocInvt.AVAILABLEAMT)) {
			if (StringUtil.stringToDouble(availableAmt) != 0.00) {
				findViewById(R.id.ll_btn).setVisibility(View.VISIBLE);
			}
			if (!StringUtil.isNull(availableAmt)) {
//				findViewById(R.id.layout_availamt).setVisibility(View.VISIBLE);
				if(StringUtil.stringToDouble(availableAmt) == 0.00){
					tv_sy.setText("售罄");//604增加
				}else if (StringUtil.stringToDouble(availableAmt) > 10000000.00) {
					tv_sy.setText("大于1000万");//P603删除该字段//604增加
				}else{
					tv_sy.setText(StringUtil.parseStringCodePattern(
							curcode, availableAmt, 2));//P603删除该字段//604增加
					}
			}else{
				tv_sy.setText("-");//P603删除该字段//604增加
			}
		}else{
			findViewById(R.id.ll_btn).setVisibility(View.VISIBLE);
		}
		String appendStartingAmount = (String) detailMap
				.get(BocInvt.ADDAMOUNT);
		tv_appendStartingAmount_detail.setText(StringUtil
				.parseStringCodePattern(curcode, appendStartingAmount, 2));
		tv_sellingDate_start.setText((String) detailMap
				.get(BocInvt.BOCI_DETAILSELLINGSTARTINGDATE_RES)
				+ ConstantGloble.BOCINVT_DATE_ADD);
		tv_sellingDate_end.setText((String) detailMap
				.get(BocInvt.BOCI_DETAILSELLINGENDINGDATE_RES));
		tv_prodBegin_detail.setText((String) detailMap
				.get(BocInvt.BOCI_DETAILPRODBEGIN_RES));
//		btn_buy_buydetail = (Button) view.findViewById(R.id.btn_buy_buydetail);
//		btn_contract_detail = (Button) view
//				.findViewById(R.id.btn_contract_detail);
//		btn_description_buydetail = (Button) view
//				.findViewById(R.id.btn_description_buydetail);
		((TextView) view.findViewById(R.id.prodRiskType)).setText(BociDataCenter.prodRiskType.get(detailMap.get(BocInvt.PRODRISKTYPE)));
		((TextView) view.findViewById(R.id.baseAmount)).setText((String)detailMap.get(BocInvt.BOCINVT_SIGNRESULT_BASEAMOUNT_REQ));
//		((TextView) view.findViewById(R.id.isCanCancle)).setText(BociDataCenter.isCanCancle.get(detailMap.get(BocInvt.ISCANCANCLE)));//认购撤单规则,P603删除
		
		tvTransferChannel.setText(getChannle(detailMap));
//		tvOutTimeOrder.setText(BociDataCenter.outTimeOrder.get(detailMap.get(BocInvt.OUTTIMEORDER)));
		
		if (((String)detailMap.get(BocInvestControl.PRODUCTKIND)).equals("0")) {
			layout_benjin_date.setVisibility(View.VISIBLE);
			layout_fugz.setVisibility(View.VISIBLE);
			switch (Integer.parseInt((String)detailMap.get("isLockPeriod"))) {
			//0：非业绩基准产品1：业绩基准-锁定期转低收益 2：业绩基准-锁定期后入账 3：业绩基准-锁定期周期滚续
			case 0:{
				set_tvPaymentDate();
				set_tvCouponPay();
			}break;
			case 1:{
				tvPaymentDate.setText("预计赎回后"+(String)detailMap.get("redPaymentDate")+"日内到账");
				tvCouponPay.setText("最低持有期内收益在持有期结束后"+(String)detailMap.get("datesPaymentOffset")+
						"日内到账，可赎回份额收益赎回后"+detailMap.get("profitDate")+"日内到账");
			}break;
			default:{
				tvPaymentDate.setText("预计到期后"+(String)detailMap.get("datesPaymentOffset")+"日内到账");
				tvCouponPay.setText("产品到期后"+(String)detailMap.get("datesPaymentOffset")+"日内到账");
			}break;
			}
		}else {
			layout_benjin_date.setVisibility(View.GONE);
			layout_fugz.setVisibility(View.GONE);
//			set_tvPaymentDate();
//			set_tvCouponPay();
		}
		setRedemptionText();
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
//				(TextView) view.findViewById(R.id.yearlyRR_detail));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) view.findViewById(R.id.rofit));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,paymenttype);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) view.findViewById(R.id.buyStartingAmount_detail));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) view.findViewById(R.id.outtext));
//		if (!StringUtil.isNull(transType) && transType.equals("06")) {//赎回
//			btn_contract_detail.setText(R.string.bocinvt_redeem);
//			btn_contract_detail.setVisibility(View.VISIBLE);
//			btn_buy_buydetail.setVisibility(View.GONE);
//			btn_description_buydetail.setVisibility(View.GONE);
//			if (!StringUtil.isNull(isPre) && isPre.equals("0")) {
//				tv_periodical_detail.setText(LocalData.periodicalList.get(1));
//			}else if(!StringUtil.isNull(isPre) && isPre.equals("1")){
//				tv_periodical_detail.setText(LocalData.periodicalList.get(0));
//			}
//		}else{//购买、签约
//			if (!isBuyCheck()) {
				// 为true是可签约
//				btn_contract_detail.setVisibility(View.VISIBLE);
//				btn_buy_buydetail.setVisibility(View.GONE);
//				btn_description_buydetail.setVisibility(View.GONE);
//				tv_periodical_detail.setText(LocalData.periodicalList.get(1));
//			} else {
				// 非周期性产品可购买
//				btn_contract_detail.setVisibility(View.GONE);
//				btn_buy_buydetail.setVisibility(View.VISIBLE);
//				tv_periodical_detail.setText(LocalData.periodicalList.get(0));
				// TODO 401 判断是否允许定投与自动投资
//				if (!StringUtil.isNullOrEmpty(chooseMap) && LocalData.orderTimeMap.get(0).equals(
//						chooseMap.get(BocInvt.BOCI_AUTOPERMIT_RES))) {
					// 允许定投与自动投资
//					btn_description_buydetail.setVisibility(View.VISIBLE);
//					LayoutParams param = (LayoutParams) btn_description_buydetail
//							.getLayoutParams();
//					btn_buy_buydetail.setLayoutParams(param);
//				} else {
//					btn_description_buydetail.setVisibility(View.GONE);
//				}
//			}
//		}
		((RadioGroup)findViewById(R.id.radioGroup)).setOnCheckedChangeListener(chechOncliclk);
//		btn_description_buydetail.setOnClickListener(this);
//		btn_buy_buydetail.setOnClickListener(this);
//		btn_contract_detail.setOnClickListener(this);
		buyType(detailMap);
		setAcctView();
		initViewClick();
	}
	private String getNull(Object obj){
		if (StringUtil.isNullOrEmpty(obj)) {
			return "无";
		}
		char[] cr = obj.toString().toCharArray();
		for (int i = 0; i < cr.length; i++) {
			if (cr[i]=='|') {
				cr[i]='\n';
			}
		}
		return new String(cr);
	}
	private void set_tvPaymentDate(){
		Object tem_obj = detailMap.get(BocInvt.PAYMENTDATE);
		if (StringUtil.isNullOrEmpty(tem_obj)) {
			tvPaymentDate.setText("-");
		}else {
			tvPaymentDate.setText("预计"+tem_obj.toString()+"左右到账");
		}
	}
	private void set_tvCouponPay(){
		String couponpayFreq = (String)detailMap.get(BocInvt.COUPONPAYFREQ);
		if (!StringUtil.isNull(couponpayFreq) && couponpayFreq.equals("-1")) {
			tvCouponPay.setText("随本金一起到账");
		}else{
			tvCouponPay.setText("每"+BocinvtUtils.frequencyTransForm(couponpayFreq)+"付息一次，最近一次付息日预计是"+(String)detailMap.get(BocInvt.INTERESTDATE));
		}
	}
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		if (StringUtil.isNullOrEmpty(resultObj)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		super.requestSystemDateTimeCallBack(resultObj);
		BocInvestControl.SYSTEM_DATE=dateTime;
		BaseHttpEngine.dissMissProgressDialog();
		startActivity(new Intent(ProductDetailActivity.this,ProductQueryAndBuyYearRateActivity.class));
	}
	/**
	 * 初始化组件点事件
	 */
	private void initViewClick(){
//		if (chooseMap.get("isBuy").toString().equals("1")) {//isBuy=1
//			
//			btn_buy.setVisibility(View.VISIBLE);
//		}else {//isBuy=0
//			//不显示购买链接
//			btn_buy.setVisibility(View.GONE);
//		}
//		btn_buy.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(ProductDetailActivity.this,BuyProductChooseActivity.class);
//				intent.putExtra(ConstantGloble.BOICNVT_ISDETAIL_BUY, false);
//				startActivityForResult(intent, ACTIVITY_BUY_CODE);
//			}
//		});
		
		String [] str=null;
		List<String> str_list=new ArrayList<String>();
		if (chooseMap.get("isBuy").toString().equals("1")) {
			//显示购买链接
			str_list.add(getResources().getString(R.string.buy));
		}
		if (BocInvestControl.map_impawnPermit.get("是").equals(chooseMap.get(BocInvt.BOCI_IMPAWNPERMIT_RES).toString())) {
			//可组合购买
			str_list.add(getResources().getString(R.string.bocinvt_tv_32));
		}
		if (chooseMap.get("isAgreement").toString().equals("1")) {
			//允许投资协议申请
			str_list.add(getResources().getString(R.string.bocinvt_apply_agree_title));
		}
		if (chooseMap.get("isProfitest").toString().equals("1")) {
			//允许收益试算
			str_list.add(getResources().getString(R.string.bocinvt_product_profit_compute1));
		}
		switch (str_list.size()) {
		case 0:{//一个按钮也没有
			btn_buy.setVisibility(View.GONE);
			btn_more.setVisibility(View.GONE);
		}break;
		case 1:{//有1个按钮
			btn_buy.setVisibility(View.VISIBLE);
			btn_more.setVisibility(View.GONE);
			btn_buy.setText(str_list.get(0));
		}break;
		case 2:{//有2个按钮
			btn_buy.setVisibility(View.VISIBLE);
			btn_more.setVisibility(View.VISIBLE);
			btn_buy.setText(str_list.get(0));
			btn_more.setText(str_list.get(1));
			btn_more.setOnClickListener(moreButtonOnclick);
		}break;
		default:{//有2个以上按钮
			btn_buy.setText(str_list.get(0));
			btn_more.setText(getResources().getString(R.string.bocinvt_btn_more));
			btn_buy.setVisibility(View.VISIBLE);
			btn_more.setVisibility(View.VISIBLE);
			str_list.remove(0);
			str=new String[str_list.size()];
			for (int i = 0; i < str_list.size(); i++) {
				str[i]=str_list.get(i);
			}
			str_list=null;
			//构造更多按钮
			PopupWindowUtils.getInstance().setshowMoreChooseUpListener(ProductDetailActivity.this,
					btn_more,str,moreButtonOnclick);
		}break;
		}
		btn_buy.setOnClickListener(moreButtonOnclick);
	}
	
	/**
	 * "更多"按钮弹出框中 相应按钮点击事件
	 */
	private OnClickListener moreButtonOnclick=new OnClickListener() {
		@Override
		public void onClick(View v) {
			Button btn=(Button) v;
			String btn_text = btn.getText().toString();
			if (btn_text.equals(getResources().getString(R.string.buy))) {//购买
				buy_agreement_flag=1;
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
			if (btn_text.equals(getResources().getString(R.string.bocinvt_tv_32))) {//组合购买
				buy_agreement_flag=3;
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
			if (btn_text.equals(getResources().getString(R.string.bocinvt_apply_agree_title))) {//投资协议申请
				buy_agreement_flag=2;
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
			if (btn_text.equals(getResources().getString(R.string.bocinvt_product_profit_compute1))){//收益试算
				Intent intent = new Intent(ProductDetailActivity.this, ProductQueryAndBuyProfitComputeActivity.class);
				startActivity(intent);
			}
		}
	};
	
	/**
	 * 请求是否进行过风险评估
	 */
	public void requestInvtEvaluation() {
		BOCDoThreeTask task = BOCDoThreeTask.getInstance(ProductDetailActivity.this);
		registActivityEvent(task);
		task.doTask(new IAction() {
			@Override
			public void SuccessCallBack(Object param) {
				toBuyOrAgreementActivity();
			}
		},1);
//		Map<String, Object> parms_map=new HashMap<String, Object>();
//		getHttpTools().requestHttp(BocInvt.PSNINVTEVALUATIONINIT_API, "requestInvtEvaluationCallback", parms_map, true);
	}
	
	/**
	 * 请求是否进行过风险评估---回调
	 * 
	 * @param resultObj
	 */
//	@SuppressWarnings("static-access")
//	public void requestInvtEvaluationCallback(Object resultObj) {
//		BaseHttpEngine.dissMissProgressDialog();
//		if (StringUtil.isNullOrEmpty(resultObj)) {
//			buy_agreement_flag=0;
//			return;
//		}
//		result_map = getHttpTools().getResponseResult(resultObj);
//		String status = (String) result_map.get(BocInvt.BOCIEVA_STATUS_RES);
//		String evalExpired = result_map.get("evalExpired").toString();
//		if (!StringUtil.isNull(status)&& status.equals(ConstantGloble.BOCINVT_EVA_SUC_STATUS)&&evalExpired.equals("1")) {
//			isevaluatedBefore = true;
//		} else {
//			isevaluatedBefore = false;
//		}
//		if (isevaluatedBefore) {
//			toBuyOrAgreementActivity();
//		}else {
//			InflaterViewDialog inf_dialog = new InflaterViewDialog(ProductDetailActivity.this);
//			View viewDialog_choice = inf_dialog.judgeViewDialog_choice(true, null, isevaluatedBefore, null, null, invtEvaluationClick, exitDialogClick);
//			TextView tv = (TextView) viewDialog_choice.findViewById(R.id.tv_acc_account_accountState);
//			tv.setText("您需要完成下面一个任务才能进行理财交易");//待业务确认后再修改信息
//			BaseDroidApp.getInstanse().showAccountMessageDialog(viewDialog_choice);
//		}
//	}
	/**
	 * 风险评估过后跳转到购买页或投资协议申请页
	 */
	private void toBuyOrAgreementActivity(){
//		BocInvestControl.danger_map=result_map;
		switch (buy_agreement_flag) {
		case 1:{
			Intent intent = new Intent(ProductDetailActivity.this,BuyProductChooseActivity.class);
			intent.putExtra(ConstantGloble.BOICNVT_ISDETAIL_BUY, false);
			startActivityForResult(intent, ACTIVITY_BUY_CODE);
			buy_agreement_flag=0;
		}break;
		case 2:{
//			Intent intent = new Intent(ProductDetailActivity.this, ProductQueryAndBuyAgreementApplyActivity.class);
//			startActivityForResult(intent, BocInvestControl.ACTIVITY_RESULT_CODE_AGREEMENT_APPLY);
			buy_agreement_flag=0;
			requestPsnXpadProductInvestTreatyQuery();
		}break;
		case 3:{
			Intent intent = new Intent(ProductDetailActivity.this, ProductQueryAndBuyComboBuyStateActivity.class);
			startActivityForResult(intent, BocInvestControl.ACTIVITY_RESULT_CODE_COMBO_BUY);
			buy_agreement_flag=0;
		}break;
		
		default:
			break;
		}
	}
	/** 风险评估监听事件 */
	OnClickListener invtEvaluationClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(ProductDetailActivity.this,InvtEvaluationInputActivity.class);
			startActivityForResult(intent,ConstantGloble.ACTIVITY_REQUEST_INVTEVALUATION_CODE);
			overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
		}
	};
	protected OnClickListener exitDialogClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
//			Intent intent = new Intent(ProductDetailActivity.this,SecondMainActivity.class);
//			startActivity(intent);
//			ActivityTaskManager.getInstance().removeAllSecondActivity();
			goToMainActivity();
//		 	finish();
		}
	};
	
	/**
	 * 理财交易账户--推荐产品
	 */
	private void setAcctView(){
		if (!StringUtil.isNull(transType) && transType.equals("05")) {
			findViewById(R.id.acct_layout).setVisibility(View.VISIBLE);
			tvAcct = (TextView) findViewById(R.id.boci_combinacct);
			tvAcct.setOnClickListener(this);
		}
	}
	
//	private boolean isBuyCheck(){
//		if (!StringUtil.isNull(transType) && transType.equals("05")) {
//			if (!StringUtil.isNull(isPre) && isPre.equals("1")) {
//				return true;
//			}
//		}else {
//			if (!StringUtil.isNullOrEmpty(chooseMap)) {
//				if (chooseMap.get(BocInvt.BOCI_DETAILPERIODICAL_RES).equals("false")) {
//					return true;
//				}
//			}
//		}
//		return false;
//	}
	
	private OnCheckedChangeListener chechOncliclk = new OnCheckedChangeListener(){
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.btn1:
				findViewById(R.id.base_layout).setVisibility(View.VISIBLE);
//				findViewById(R.id.detaillong).setVisibility(View.VISIBLE);
				findViewById(R.id.red_layout).setVisibility(View.GONE);
				findViewById(R.id.buy_layout).setVisibility(View.GONE);
				break;
			case R.id.btn2:
				changText();
				findViewById(R.id.base_layout).setVisibility(View.GONE);
				findViewById(R.id.red_layout).setVisibility(View.GONE);
//				findViewById(R.id.detaillong).setVisibility(View.GONE);
				findViewById(R.id.buy_layout).setVisibility(View.VISIBLE);
				break;
			case R.id.btn3:
				findViewById(R.id.base_layout).setVisibility(View.GONE);
				findViewById(R.id.red_layout).setVisibility(View.VISIBLE);
				findViewById(R.id.buy_layout).setVisibility(View.GONE);
//				findViewById(R.id.detaillong).setVisibility(View.GONE);
				break;
			}
		}
	};
	
	/**
	 * 业绩基准型产品增加 (单日)
	 */
	private void changText(){
		//2016/3/22业务确认，不增加"单日"字样
//		if (((String)detailMap.get(BocInvestControl.PRODUCTKIND)).equals("0")&&!((String)detailMap.get("isLockPeriod")).equals("0")) {
//			//0:结构性理财产品1:类基金理财产品     且      isLockPeriod:0：非业绩基准产品     1：业绩基准-锁定期转低收益 2：业绩基准-锁定期后入账 3：业绩基准-锁定期周期滚续
//			((TextView)findViewById(R.id.buyStartingAmount_detail)).setText("(单日)"+getResources().getString(R.string.bocinvt_subamount));
//			((TextView)findViewById(R.id.appendStrating)).setText("(单日)"+getResources().getString(R.string.bocinvt_addamount));
//		}else {
			((TextView)findViewById(R.id.buyStartingAmount_detail)).setText(getResources().getString(R.string.bocinvt_subamount));
			((TextView)findViewById(R.id.appendStrating)).setText(getResources().getString(R.string.bocinvt_addamount));
//		}
	}
	
	/**
	 * 申购开放规则
	 * @param map
	 */
	private void buyType(Map<String, Object> map){
		view.findViewById(R.id.layout_buyType).setVisibility(View.VISIBLE);
		TextView tvBuyType = (TextView)view.findViewById(R.id.buyType);
		String bidHoliday = (String) map.get(BocInvt.BIDHOLIDAY);
		String buyType = (String) map.get(BocInvt.BUYTYPE);
		if (!StringUtil.isNull(bidHoliday) && !StringUtil.isNull(buyType)) {
			if (buyType.equals("00")) {
				tvBuyType.setText("不开放");
			}else if(buyType.equals("01")){
				if (bidHoliday.equals("0")) {
					tvBuyType.setText(map.get(BocInvt.BIDSTARTDATE)+"至"+map.get(BocInvt.BIDENDDATE)+"工作日开放申购");
				}else if(bidHoliday.equals("1")){
					tvBuyType.setText(map.get(BocInvt.BIDSTARTDATE)+"至"+map.get(BocInvt.BIDENDDATE)+"每日开放申购");
				}
			}else if(buyType.equals("02")){
				String bidPeriodMode = ((String) map.get(BocInvt.BIDPERIODMODE)).toUpperCase();
				String bidPeriodStartDate = (String) map.get(BocInvt.BIDPERIODSTARTDATE);
				String bidPeriodEndDate = (String) map.get(BocInvt.BIDPERIODENDDATE);
				if (bidPeriodMode.endsWith("M")) {
					tvBuyType.setText("每月的"+bidPeriodStartDate+"号至"+bidPeriodEndDate+"号开放申购");
				}else if(bidPeriodMode.endsWith("W")){
					tvBuyType.setText("每周的星期"+bidPeriodStartDate+"至星期"+bidPeriodEndDate+"开放申购");
				}else if(bidPeriodMode.endsWith("S")){
					tvBuyType.setText("每季的第一个月的"+bidPeriodStartDate+"号至"+bidPeriodEndDate+"号开放申购");
				}
			}else if(buyType.equals("03")){
				if (bidHoliday.equals("0")) {
					tvBuyType.setText("工作日开放申购");
				}else if(bidHoliday.equals("1")){
					tvBuyType.setText("每日开放申购");
				}
			}
		}
	}
	
	/**
	 * 交易渠道
	 * @param map
	 */
	private String getChannle(Map<String, Object> map){
		String result = ConstantGloble.FINC_COMBINQURY_NONE;
		StringBuffer b = new StringBuffer();
		String isBancs = (String) map.get(BocInvt.ISBANCS);
		if (!StringUtil.isNull(isBancs) && isBancs.equals(BocInvt.CHANNLE_VISIBLE)) {
			b.append(BociDataCenter.channle.get(0));
			b.append(BociDataCenter.channle.get(9));
		}
		String isSMS = (String) map.get(BocInvt.ISSMS);
		if (!StringUtil.isNull(isSMS) && isSMS.equals(BocInvt.CHANNLE_VISIBLE)) {
			b.append(BociDataCenter.channle.get(1));
			b.append(BociDataCenter.channle.get(9));
		}
		String sellOnline = (String) map.get(BocInvt.SELLONLINE);
		if (!StringUtil.isNull(sellOnline) && sellOnline.equals(BocInvt.CHANNLE_VISIBLE)) {
			b.append(BociDataCenter.channle.get(2));
			b.append(BociDataCenter.channle.get(9));
		}
		String sellMobile = (String) map.get(BocInvt.SELLMOBILE);
		if (!StringUtil.isNull(sellMobile) && sellMobile.equals(BocInvt.CHANNLE_VISIBLE)) {
			b.append(BociDataCenter.channle.get(3));
			b.append(BociDataCenter.channle.get(9));
		}
		String sellHomeBanc = (String) map.get(BocInvt.SELLHOMEBANC);
		if (!StringUtil.isNull(sellHomeBanc) && sellHomeBanc.equals(BocInvt.CHANNLE_VISIBLE)) {
			b.append(BociDataCenter.channle.get(4));
			b.append(BociDataCenter.channle.get(9));
		}
		String sellAutoBanc = (String) map.get(BocInvt.SELLAUTOBANC);
		if (!StringUtil.isNull(sellAutoBanc) && sellAutoBanc.equals(BocInvt.CHANNLE_VISIBLE)) {
			b.append(BociDataCenter.channle.get(5));
			b.append(BociDataCenter.channle.get(9));
		}
		String sellTelphone = (String) map.get(BocInvt.SELLTELPHONE);
		if (!StringUtil.isNull(sellTelphone) && sellTelphone.equals(BocInvt.CHANNLE_VISIBLE)) {
			b.append(BociDataCenter.channle.get(6));
			b.append(BociDataCenter.channle.get(9));
		}
		String sellTelByPeple = (String) map.get(BocInvt.SELLTELBYPEPLE);
		if (!StringUtil.isNull(sellTelByPeple) && sellTelByPeple.equals(BocInvt.CHANNLE_VISIBLE)) {
			b.append(BociDataCenter.channle.get(7));
			b.append(BociDataCenter.channle.get(9));
		}
		String sellWeChat = (String) map.get(BocInvt.SELLWECHAT);
		if (!StringUtil.isNull(sellWeChat) && sellWeChat.equals(BocInvt.CHANNLE_VISIBLE)) {
			b.append(BociDataCenter.channle.get(8));
			b.append(BociDataCenter.channle.get(9));
		}
		if (StringUtil.isNull(b.toString())) {
			return result;
		}
		result = b.toString().substring(0, b.toString().length()-1);
		return result;
	}
	
	/**
	 * 赋值赎回
	 */
	private void setRedemptionText(){
		if(((String)detailMap.get(BocInvestControl.PRODUCTKIND)).equals("0")&&(detailMap.get("isLockPeriod").equals("2")||detailMap.get("isLockPeriod").equals("3"))){
			findViewById(R.id.layout_unred).setVisibility(View.VISIBLE);
		}else{
			if (!detailMap.get(BocInvt.SELLTYPE).equals("00")) {
				findViewById(R.id.redlayout).setVisibility(View.VISIBLE);
				tvLowlimitAmount.setText(StringUtil.parseStringPattern((String)detailMap.get(BocInvt.LOWLIMITAMOUNT), 2));
				tvLimitholdBalance.setText(StringUtil.parseStringPattern((String)detailMap.get(BocInvt.LIMITHOLDBALANCE), 2));
				sellType();
				sellPaymentType();
				proFit();
			}else{
				findViewById(R.id.layout_unred).setVisibility(View.VISIBLE);
			}
		}
	}
	
	/**
	 * 赎回开放规则
	 */
	private void sellType(){
		//是否允许节假日赎回
		String redEmptionHoliday = (String) detailMap.get(BocInvt.REDEMPTIONHOLIDAY);
		//赎回开放规则
		String sellType = (String) detailMap.get(BocInvt.SELLTYPE);
		if (!StringUtil.isNull(sellType) && !StringUtil.isNull(redEmptionHoliday)){
			if (sellType.equals("00")) {
				tvSellType.setText("不开放主动赎回");
			}else if(sellType.equals("01") && redEmptionHoliday.equals("0")){
				tvSellType.setText(detailMap.get(BocInvt.REDEMPTIONSTARTDATE)+"至"+
			detailMap.get(BocInvt.REDEMPTIONENDDATE)+"工作日开放赎回");
			}else if(sellType.equals("01") && redEmptionHoliday.equals("1")){
				tvSellType.setText(detailMap.get(BocInvt.REDEMPTIONSTARTDATE)+"至"+
						detailMap.get(BocInvt.REDEMPTIONENDDATE)+"每日开放赎回");
			}else if(sellType.equals("02")){
				tvSellType.setText("每"+BocinvtUtils.frequencyTransForm((String)detailMap.get(BocInvt.COUPONPAYFREQ))+"开放赎回，最近一次是"+detailMap.get(BocInvt.INTERESTDATE));
			}else if(sellType.equals("03") && redEmptionHoliday.equals("0")){
				tvSellType.setText("工作日开放赎回");
			}else if(sellType.equals("03") && redEmptionHoliday.equals("1")){
				tvSellType.setText("每日开放赎回");
			}else if(sellType.equals("04")){
				String redEmperiodfReq = ((String) detailMap.get(BocInvt.REDEMPERIODFREQ)).toUpperCase();
				String redEmperiodStart = (String) detailMap.get(BocInvt.REDEMPERIODSTART);
				String redEmperiodEnd = (String) detailMap.get(BocInvt.REDEMPERIODEND);
				if (redEmperiodfReq.endsWith("W")) {
					tvSellType.setText("每周的星期"+redEmperiodStart+"至星期"+redEmperiodEnd+"开放赎回");
				}else if(redEmperiodfReq.endsWith("M")){
					tvSellType.setText("每月的"+redEmperiodStart+"号至"+redEmperiodEnd+"号开放赎回");
				}else if(redEmperiodfReq.endsWith("S")){
					tvSellType.setText("每季的第一个月的"+redEmperiodStart+"号至"+redEmperiodEnd+"号开放赎回");
				}
			}
		}
	}
	
	/**
	 * 本金到账规则
	 */
	private void sellPaymentType(){
		String redPaymentMode = (String) detailMap.get(BocInvt.REDPAYMENTMODE);
		if (StringUtil.isNull(redPaymentMode)) return;
		if (redPaymentMode.equals("0")) {
			tvSellPaymentType.setText("T日赎回，本金实时到账");
		}else if(redPaymentMode.equals("1")){
			String dateModeType = (String) detailMap.get(BocInvt.DATEMODETYPE);
			if (StringUtil.isNull(dateModeType)) return;
			String redPaymentDate = (String) detailMap.get(BocInvt.REDPAYMENTDATE);
			if (dateModeType.equals("0")) {
				tvSellPaymentType.setText("T日赎回，本金T＋"+redPaymentDate+"日到账");
			}else if(dateModeType.equals("1")){
				tvSellPaymentType.setText("T日赎回，本金T＋"+redPaymentDate+"日到账（若遇节假日顺延至下一工作日）");
			}else if(dateModeType.equals("2")){
				tvSellPaymentType.setText("T日赎回，本金T＋"+redPaymentDate+"日到账（若遇节假日顺延至下一工作日，如顺延日期跨月，则在当月最后一个工作日到账）");
			}else if(dateModeType.equals("3")){
				tvSellPaymentType.setText("T日赎回，本金T＋"+redPaymentDate+"日到账（若遇节假日自动向前调整）");
			}else if(dateModeType.equals("4")){
				tvSellPaymentType.setText("T日赎回，本金T＋"+redPaymentDate+"日到账（若遇节假日自动向前调整，如顺延日期跨月，则在当月第一个工作日到账）");
			}	
		}else if(redPaymentMode.equals("2")){
			tvSellPaymentType.setText("预计"+detailMap.get(BocInvt.BOCINVT_BUYRES_PAYMENTDATE_RES)+"左右到账");
		}
	}
	
	/**
	 * 赎回收益到账规则
	 */
	private void proFit(){
		String profitMode = (String) detailMap.get(BocInvt.PROFITMODE);
		if (StringUtil.isNull(profitMode)) return;
		if (profitMode.equals("1")) {
			String dateModeType = (String) detailMap.get(BocInvt.DATEMODETYPE);
			if (StringUtil.isNull(dateModeType)) return;
			String profitDate = (String) detailMap.get(BocInvt.PROFITDATE);
			if (dateModeType.equals("0")) {
				tvProFit.setText("T日赎回，收益T＋"+profitDate+"日到账");
			}else if(dateModeType.equals("1")){
				tvProFit.setText("T日赎回，收益T＋"+profitDate+"日到账（若遇节假日顺延至下一工作日）");
			}else if(dateModeType.equals("2")){
				tvProFit.setText("T日赎回，收益T＋"+profitDate+"日到账（若遇节假日顺延至下一工作日，如顺延日期跨月，则在当月最后一个工作日到账）");
			}else if(dateModeType.equals("3")){
				tvProFit.setText("T日赎回，收益T＋"+profitDate+"日到账（若遇节假日自动向前调整）");
			}else if(dateModeType.equals("4")){
				tvProFit.setText("T日赎回，收益T＋"+profitDate+"日到账（若遇节假日自动向前调整，如顺延日期跨月，则在当月第一个工作日到账）");
			}	
		}else if(profitMode.equals("2")){
			tvProFit.setText("预计"+detailMap.get(BocInvt.REDPAYDATE)+"左右到账");
		}	
	}


	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 主界面
//			Intent intent = new Intent(ProductDetailActivity.this,
//					SecondMainActivity.class);
//			startActivity(intent);
			ActivityTaskManager.getInstance().removeAllSecondActivity();
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.btn_description_buydetail:
//			// // 产品说明书
//			// Intent intent = new Intent(ProductDetailActivity.this,
//			// ProductDescriptionActivity.class);
//			// startActivity(intent);
//			// overridePendingTransition(R.anim.push_up_in,
//			// R.anim.no_animation);
//			// 投资协议申请
//			Intent intent = new Intent(this, AgreementChooseActivity.class);
//			startActivity(intent);
//
//			break;
//		case R.id.btn_buy_buydetail:
//			// 购买
//			// 判断是否是在售产品
////			String periodical = (String) chooseMap
////					.get(BocInvt.BOCI_DETAILPERIODICAL_RES);
////			boolean isperiodical = Boolean.valueOf(periodical);
//			if (!StringUtil.isNull(status) && LocalData.boci_StatusMap.get(status)
//					.equalsIgnoreCase(LocalData.bocinvtXpadStatus.get(1))
//					&& isBuyCheck()) {
//				if (ocrmNextCheck()) {
//					// 传递产品信息
//					Intent intent1 = new Intent(ProductDetailActivity.this,
//							BuyProductChooseActivity.class);
//					intent1.putExtra(ConstantGloble.BOICNVT_ISDETAIL_BUY, false);
//					startActivityForResult(intent1, ACTIVITY_BUY_CODE);
//				}
//			} else {
//				BaseDroidApp.getInstanse().showInfoMessageDialog(
//						ProductDetailActivity.this
//								.getString(R.string.bocinvt_error_buy));
//			}
//
//			break;
//		case R.id.btn_contract_detail:
//			if (!StringUtil.isNull(transType) && transType.equals("06")) {//赎回
//				isRed = true;
//				BaseHttpEngine.showProgressDialog();
//				requestCommConversationId(); return;
//			}
////			String periodical_con = (String) chooseMap
////					.get(BocInvt.BOCI_DETAILPERIODICAL_RES);
////			boolean isperiodical_con = Boolean.valueOf(periodical_con);
//			// 签约
//			if (!StringUtil.isNull(status) && LocalData.boci_StatusMap.get(status)
//					.equalsIgnoreCase(LocalData.bocinvtXpadStatus.get(1))) {
//				if(ocrmNextCheck()){
//				// 传递产品信息
//					Intent intent2 = new Intent(ProductDetailActivity.this,
//							BuyProductChooseActivity.class);
//					intent2.putExtra(ConstantGloble.BOICNVT_ISDETAIL_BUY, false);
//					startActivityForResult(intent2, ACTIVITY_BUY_CODE);
//				}
//			} else {
//				BaseDroidApp.getInstanse().showInfoMessageDialog(
//						ProductDetailActivity.this
//								.getString(R.string.bocinvt_error_contract));
//			}
//			break;
			//理财交易账户
		case R.id.boci_combinacct:
			createDialog(PublicTools.getSpinnerData(BociDataCenter.getInstance().getBocinvtAcctList(), BocInvt.ACCOUNTNO), "理财交易账户",index);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 创建spinner弹窗框
	 * @param list
	 * @param title
	 * @param position
	 */
	private void createDialog(List<String> list,String title,int position){
		ListView mListView = new ListView(this);
		mListView.setFadingEdgeLength(0);
		mListView.setOnItemClickListener(acctItemOnClick);
		mListView.setScrollingCacheEnabled(false);
		AnnuitySpinnerAdapter mAdapter = new AnnuitySpinnerAdapter(this, list,position);
		mListView.setAdapter(mAdapter);
		BaseDroidApp.getInstanse().showSpinnerDialog(mListView, title);
	}
	
	private OnItemClickListener acctItemOnClick = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			index = position;
			BaseDroidApp.getInstanse().dismissMessageDialog();
			tvAcct.setText(PublicTools.getSpinnerData(BociDataCenter.getInstance().getBocinvtAcctList(), BocInvt.ACCOUNTNO).get(position));
		}
	};
	
//	private boolean ocrmNextCheck(){
//		if (!StringUtil.isNull(transType) && transType.equals("05")) {
//			if (tvAcct.getText().toString().equals("请选择账户")) {
//				BaseDroidApp.getInstanse().showInfoMessageDialog("请选择账户");
//				return false;
//			}
//			BaseDroidApp.getInstanse().getBizDataMap()
//			.put(ConstantGloble.BOCINVT_BUYINIT_MAP, BociDataCenter.getInstance().getBocinvtAcctList()
//					.get(index)); return true;
//		}
//		return true;
//	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
//			if (requestCode==ConstantGloble.ACTIVITY_REQUEST_INVTEVALUATION_CODE) {
//				// 风险评估成功的响应
//				if (BociDataCenter.getInstance().getI() == 1) {
//
//				} else {
//					isevaluatedBefore = true;
//					toBuyOrAgreementActivity();
//				}
//			}else {
				BocInvestControl.clearDate_productQueryAndBuy();
				setResult(RESULT_OK);
				finish();
//			}
			break;
		case RESULT_CANCELED:
			break;
		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			BocInvestControl.clearDate_productQueryAndBuy();
			setResult(RESULT_CANCELED);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/** 预计年收益率 */
	private void setProgress(){
		String progressionflag = (String) detailMap.get(BocInvt.PROGRESSIONFLAG);
		if (StringUtil.isNull(progressionflag)) return;
//		if (progressionflag.equals("0")) {
//			tv_yearlyRR_detail.setText(StringUtil.append2Decimals((String)detailMap
//					.get(BocInvt.BOCI_DETAILYEARLYRR_RES), 2) + PER);
//		}else if(progressionflag.equals("1")){
//			tv_yearlyRR_detail.setTextColor(this.getResources().getColor(R.color.blue));
//			tv_yearlyRR_detail.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//			tv_yearlyRR_detail.setText(getString(R.string.progression));
//			tv_yearlyRR_detail.setOnClickListener(mClickListener);
//		}
		if (progressionflag.equals("1")) {//是收益累进产品
			tv_yearlyRR_detail.setTextColor(this.getResources().getColor(R.color.blue));
			tv_yearlyRR_detail.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			tv_yearlyRR_detail.setOnClickListener(mClickListener);
		}
	}
	
	/** 收益累进  */
	private OnClickListener mClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			isRed = false;
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
		}
	};
	private TextView paymenttype;
	private Button btn_buy;
	private Button btn_more;
	private View layout_femz;
	private Map<String, Object> result_map;
	
	@SuppressWarnings("unchecked")
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		switch (buy_agreement_flag) {
		case 1:{
			requestInvtEvaluation();
		}break;
		case 2:{
			requestInvtEvaluation();
		}break;
		case 3:{
			requestInvtEvaluation();
		}break;

		default:{
			if (isRed) {
				requestProductBalance(); return;
			}
			requestProgress(/*从P603开始不用accountId*/
					((Map<String, Object>)BaseDroidApp.getInstanse().getBizDataMap()
							.get(ConstantGloble.BOCINVT_BUYINIT_MAP))
							.get(BocInvt.BOCINVT_CAPACITYQUERY_ACCOUNTKEY_RES).toString(),
							(String) detailMap.get(BocInvt.BOCI_DETAILPRODCODE_RES),"0",true);
		}break;
		}
	}

	@Override
	public void progressQueryCallBack(Object resultObj) {
		super.progressQueryCallBack(resultObj);
		startActivity(new Intent(this, ProgressInfoActivity.class)
		.putExtra(Comm.ACCOUNT_ID, accountId)
		.putExtra(BocInvt.PROGRESS_RECORDNUM, progressRecordNumber)
		.putExtra(BocInvt.BOCI_DETAILPRODNAME_RES, (String)detailMap.get(BocInvt.BOCI_DETAILPRODNAME_RES))
		.putExtra(BocInvt.BOCI_PRODUCTCODE_REQ, (String)detailMap.get(BocInvt.BOCI_DETAILPRODCODE_RES)));
	}
	
	/**
	 * 我的理财产品
	 */
	private void requestProductBalance() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.PRODUCTBALANCEQUERY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(BocInvt.BOCI_CURRENTINDEX_REQ, ConstantGloble.LOAN_CURRENTINDEX_VALUE);
		params.put(BocInvt.BOCI_PAGESIZE_REQ,BocInvt.PAGESIZE);
		params.put(BocInvt.BOCI_REFRESH_REQ,ConstantGloble.LOAN_REFRESH_FALSE);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this,
				"productBalanceCallback");
	}
	
	@SuppressWarnings("unchecked")
	public void productBalanceCallback(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		List<Map<String, Object>> list = (List<Map<String, Object>>)result.get(BocInvt.BOCI_LIST_RES);
		if (!StringUtil.isNullOrEmpty(list)) {
			for (Map<String, Object> map: list) {
				if (map.get(BocInvt.BOCINVT_HOLDPRO_PRODCODE_RES).equals(detailMap
						.get(BocInvt.BOCI_DETAILPRODCODE_RES))) {
					BaseDroidApp.getInstanse().getBizDataMap()
					.put(ConstantGloble.BOCINVT_MYPRODUCT_LIST,map);
					BaseHttpEngine.dissMissProgressDialog();
					startActivity(new Intent(this, RedeemInputActivity.class)); return;
				}
			}
		}
		BaseHttpEngine.dissMissProgressDialog();
		BaseDroidApp.getInstanse().showInfoMessageDialog(
				getString(R.string.bocinvt_error_canredeem));

	}
	/**
	 * 请求 投资协议列表查询
	 * @param _refresh 是否重新加载
	 */
	public void requestPsnXpadProductInvestTreatyQuery(){
		Map<String, Object> parms_map=new HashMap<String, Object>();
		parms_map.put(BocInvt.BOCI_ACCOUNTID_REQ, BocInvestControl.accound_map.get(BocInvt.BOCIBINDING_ACCOUNTIDF_RES).toString());
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
			Intent intent = new Intent(ProductDetailActivity.this,ProductQueryAndBuyAgreementApplyAgreementBaseMessageActivity.class);
			startActivityForResult(intent, BocInvestControl.ACTIVITY_RESULT_CODE_AGREEMENT_APPLY);
			return;
		}else{
			Intent intent = new Intent(ProductDetailActivity.this, ProductQueryAndBuyAgreementApplyActivity.class);
			startActivityForResult(intent, BocInvestControl.ACTIVITY_RESULT_CODE_AGREEMENT_APPLY);
		}

		BaseHttpEngine.dissMissProgressDialog();
	}
}
