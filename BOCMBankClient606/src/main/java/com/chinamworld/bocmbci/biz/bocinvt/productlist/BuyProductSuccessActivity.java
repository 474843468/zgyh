package com.chinamworld.bocmbci.biz.bocinvt.productlist;

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
import com.chinamworld.bocmbci.biz.SecondMainActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.utils.DateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 中银理财 购买成功页面
 * 
 * @author wangmengmeng
 * 
 */
public class BuyProductSuccessActivity extends BociBaseActivity {
	/** 购买产品成功信息页面 */
//	private View view;
	// 非周期性产品///////////////////////////////
	/** 成功信息 */
	private Map<String, Object> successMap;
	/** 理财交易账户 */
//	private TextView tv_number;
//	/** 交易序号 */
//	private TextView tv_tranSeq;
//	/** 交易日期 */
//	private TextView tv_paymentDate;
//	/** 产品代码 */
//	private TextView tv_prodCode;
//	/** 理财产品名称 */
//	private TextView tv_prodName;
//	/** 产品币种——钞汇标志 */
//	private TextView tv_curCode;
//	/** 柜员营销代码 */
//	private TextView tv_martCode;
//	/** 产品成立日 */
//	private TextView tv_prodBegin;
//	/** 产品到期日 */
//	private TextView tv_prodEnd;
//	/** 购买价格 */
//	private TextView tv_trfPrice;
//	/** 购买份额 */
//	private TextView tv_trfAmount;
//	/** 购买金额 */
//	private TextView tv_buyPrice;
	/** 产品详情列表 */
	private Map<String, Object> detailMap;
	/** 购买预交易接口返回的信息 */
	private Map<String, Object> viewDateMap;
	/** 确定按钮 */
//	private Button sureBtn;
	// 周期性产品/////////////////////////////////
	/** 周期性产品成功信息 */
//	private ArrayList<String> extraList;
//	/** 周期性产品成功接口返回数据 */
//	private Map<String, Object> signResultMap;
//	/** 理财交易账户 */
//	private TextView tv_accNumber_contract;
//	/** 理财产品名称 */
//	private TextView tv_invtProdName_contract;
//	/** 产品系列名称 */
//	private TextView tv_serialName_contract;
//	/** 产品币种——钞汇标志 */
//	private TextView tv_curCode_contract;
//	/** 最大可购买期数 */
//	private TextView tv_remainCycleCount_contract;
//	/** 购买期数 */
//	private TextView tv_totalPeriod_contract;
//	/** 基础金额模式 */
//	private TextView tv_amountTypeCode_contract;
//	/** 基础金额 */
//	private TextView tv_baseAmount_contract;
//	/** 最低预留金额 */
//	private TextView tv_minAmount_contract;
//	/** 最大扣款额金额 */
//	private TextView tv_maxAmount_contract;
//	/** 基础金额布局 */
//	private LinearLayout ll_amountType_base;
//	/** 最低预留金额布局 */
//	private LinearLayout ll_amountType_min;
//	/** 最大扣款额金额布局 */
//	private LinearLayout ll_amountType_max;
//	/** 交易序号 */
//	private TextView tv_tranSeq_contract;
//	/** 协议序号 */
//	private TextView tv_contractSeq_contract;
//	/** 协议申请日期 */
//	private TextView tv_operateDate_contract;
//	/** 开始期数 */
//	private TextView tv_startPeriod_contract;
//	/** 结束期数 */
//	private TextView tv_endPeriod_contract;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		if("1".equals((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.IsFromAgreeApply))){
			setTitle(this.getString(R.string.bocinvt_apply_agree_title));
		}else{
			setTitle(this.getString(R.string.boci_buy_title));
		}
		setText(this.getString(R.string.go_main));
		back.setVisibility(View.GONE);
		// 界面初始化
		init();
		setRightBtnClick(rightBtnClick);
	}

	/** 右上角按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
//			startActivity(new Intent(BuyProductSuccessActivity.this,SecondMainActivity.class));
//			ActivityTaskManager.getInstance().removeAllSecondActivity();
			goToMainActivity();
		}
	};

	@SuppressWarnings("unchecked")
	private void init() {
		goneLeftButton();
		detailMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_PRODUCTDETAIL_LIST);
		viewDateMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_CONFIRM_MAP);
		successMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_BUYRESULT_MAP);
//		Map<String, Object> chooseMap = BociDataCenter.getInstance().getChoosemap();
//		if (detailMap.get(BocInvt.BOCI_DETAILPERIODICAL_RES).equals("1")) {
//			// 周期性产品
//			initContractView();
//		} else {
//			// 如果是非周期性产品
//			initBuyProductView();
//		}
		initUI();

	}

	/**
	 *初始化UI 
	 */
	private void initUI() {
		addView(R.layout.product_query_and_buy_buy_succeed);
		//初始化
		Button btn_sure = (Button) findViewById(R.id.btn_sure);
		LabelTextView tv_jingzhi_1 = (LabelTextView) findViewById(R.id.tv_jingzhi_1);
		LabelTextView tv_jingzhi_12 = (LabelTextView) findViewById(R.id.tv_jingzhi_12);
		LabelTextView tv_jingzhi_2 = (LabelTextView) findViewById(R.id.tv_jingzhi_2);
		LabelTextView tv_jingzhi_3 = (LabelTextView) findViewById(R.id.tv_jingzhi_3);
		LabelTextView tv_jingzhi_4 = (LabelTextView) findViewById(R.id.tv_jingzhi_4);
		LabelTextView tv_jingzhi_5 = (LabelTextView) findViewById(R.id.tv_jingzhi_5);
		LabelTextView tv_jingzhi_6 = (LabelTextView) findViewById(R.id.tv_jingzhi_6);
		LabelTextView tv_jingzhi_13 = (LabelTextView) findViewById(R.id.tv_jingzhi_13);
		LabelTextView tv_jingzhi_7 = (LabelTextView) findViewById(R.id.tv_jingzhi_7);
		LabelTextView tv_jingzhi_8 = (LabelTextView) findViewById(R.id.tv_jingzhi_8);
		LabelTextView tv_jingzhi_11 = (LabelTextView) findViewById(R.id.tv_jingzhi_11);
		LabelTextView tv_jingzhi_9 = (LabelTextView) findViewById(R.id.tv_jingzhi_9);
		LabelTextView tv_jingzhi_14 = (LabelTextView) findViewById(R.id.tv_jingzhi_14);
		LabelTextView tv_jingzhi_15 = (LabelTextView) findViewById(R.id.tv_jingzhi_15);
		LabelTextView tv_jingzhi_10 = (LabelTextView) findViewById(R.id.tv_jingzhi_10);
		LabelTextView tv_feijingzhi_1 = (LabelTextView) findViewById(R.id.tv_feijingzhi_1);
		TextView tv_tip_fjz = (TextView) findViewById(R.id.tv_tip_fjz);
		LabelTextView tv_feijingzhi_2 = (LabelTextView) findViewById(R.id.tv_feijingzhi_2);
		LabelTextView tv_feijingzhi_3 = (LabelTextView) findViewById(R.id.tv_feijingzhi_3);
		//赋值
		btn_sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
//				startActivity(new Intent(BuyProductSuccessActivity.this,QueryProductActivity.class));
			}
		});
		tv_jingzhi_1.setValueText(successMap.get(BocInvt.BOCINVT_BUYRES_TRANSACTIONID_RES).toString());
//		tv_jingzhi_12.setValueText(successMap.get("prodBegin").toString());
		tv_jingzhi_12.setValueText(successMap.get("paymentDate").toString());
		tv_jingzhi_2.setValueText(successMap.get(BocInvt.BOCINVT_BUYRES_PRODCODE_RES).toString());
		tv_jingzhi_3.setValueText(successMap.get(BocInvt.BOCINVT_BUYRES_PRODNAME_RES).toString());
		tv_jingzhi_4.setValueText(BocInvestControl.map_productCurCode_toStr.get(detailMap.get(BocInvt.BOCINVT_HOLDPRO_CURCODE_RES).toString()));
		tv_jingzhi_5.setValueText(getNullString(BocInvestControl.map_xpadCashRemit_code_key.get(successMap.get(BocInvt.BOCINVT_HOLDPRO_CASHREMIT_RES))));
		if (!detailMap.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTTYPE_RES).toString().equals("2")) {//非净值型产品
			tv_jingzhi_6.setVisibility(View.VISIBLE);
			switch (Integer.parseInt(detailMap.get("isLockPeriod").toString())) {
			case 0:{//0：非业绩基准产品
				if (detailMap.get("productTermType").toString().equals("3")) {//产品期限特性,3：无限开放式
					tv_jingzhi_6 .setValueText("无固定期限");
				}else {
					tv_jingzhi_6 .setValueText(String.valueOf(detailMap.get(BocInvt.BOCI_PRODTIMELIMIT_RES)) + "天");
				}
			}break;
			case 1:{//1：业绩基准-锁定期转低收益 
				tv_jingzhi_6 .setValueText("最低持有"+String.valueOf(detailMap.get(BocInvt.BOCI_PRODTIMELIMIT_RES)) + "天");
			}break;
			default:{//2：业绩基准-锁定期后入账   3：业绩基准-锁定期周期滚续
				tv_jingzhi_6 .setValueText(String.valueOf(detailMap.get(BocInvt.BOCI_PRODTIMELIMIT_RES)) + "天");
			}break;
			}
		}else {
			tv_jingzhi_6.setVisibility(View.GONE);
		}
		if (detailMap.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTTYPE_RES).toString().equals("3")) {//固定期限类产品
			tv_jingzhi_13.setVisibility(View.VISIBLE);
			if (detailMap.get("isLockPeriod").toString().equals("1")) {//业绩基准-转低收益产品显示“可赎回日期”
				tv_jingzhi_13.setLabelText("可赎回日期：");
				tv_jingzhi_13 .setValueText(viewDateMap.get("prodEnd").toString());
			}else {//其他固定期限产品显示“产品到期日”
				tv_jingzhi_13.setLabelText("产品到期日：");
				tv_jingzhi_13 .setValueText(viewDateMap.get("prodEnd").toString());
			}
		}else {
			tv_jingzhi_13.setVisibility(View.GONE);
		}
//		tv_jingzhi_6.setValueText(detailMap.get(BocInvt.BOCI_PRODTIMELIMIT_REQ).toString());
		if (detailMap.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTTYPE_RES).toString().equals("2")) {//1：现金管理类产品	2：净值开放类产品 3：固定期限产品
			tv_jingzhi_7.setVisibility(View.GONE);
			tv_jingzhi_9.setVisibility(View.GONE);
			tv_jingzhi_14.setVisibility(View.VISIBLE);
			if (!StringUtil.isNullOrEmpty(detailMap.get(BocInvestControl.TRANSTYPECODE))) {
				switch (Integer.parseInt(detailMap.get(BocInvestControl.TRANSTYPECODE).toString())) {
				case 0:{//认购
					tv_jingzhi_14.setLabelText("认购手续费：");
				}break;
				case 1:{//申购
					tv_jingzhi_14.setLabelText("申购手续费：");
				}break;
				default:
					break;
				}
			}
			tv_jingzhi_14.setValueText(successMap.get("transFee")+"");
		}else {
			tv_jingzhi_7.setVisibility(View.VISIBLE);
			if (!detailMap.get("isLockPeriod").equals("0")) {//业绩基准型产品
				tv_jingzhi_9.setVisibility(View.VISIBLE);
				tv_jingzhi_9.setValueText(viewDateMap.get(BocInvestControl.EXYIELD).toString()+"%");
			}else {
				tv_jingzhi_9.setVisibility(View.GONE);
			}
			tv_jingzhi_14.setVisibility(View.GONE);
			tv_jingzhi_7.setValueText(/*StringUtil.parseStringCodePattern((String)detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES),
			successMap.get(BocInvt.BOCINVT_BUYPRE_TRFPRICE_RES).toString(),2)*/
					StringUtil.append2Decimals(successMap.get(BocInvt.BOCINVT_BUYPRE_TRFPRICE_RES).toString(),2));
		}
		tv_jingzhi_15.setValueText(StringUtil.isNullOrEmpty(successMap.get("tranflag"))?"-"
				:(successMap.get("tranflag").toString().equals("0")?"正常":"挂单"));//0:正常1:挂单
		tv_jingzhi_8.setValueText(StringUtil.parseStringCodePattern((String)detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES), successMap.get(BocInvt.BOCINVT_BUYRES_BUYPRICE_REQ).toString(), 2));
		tv_jingzhi_11.setValueText(viewDateMap.get("purDate").toString());
		tv_jingzhi_10.setValueText(getNullString(successMap.get(BocInvt.BOCINVT_CANCEL_MARTCODE_RES)));
		if (detailMap.get("appdatered").toString().equals("1")&&Boolean.parseBoolean(viewDateMap.get(BocInvestControl.KEY_ISCHECK_RABTN).toString())) {//是否允许指定日期赎回,0：否1：是
			tv_feijingzhi_1.setVisibility(View.VISIBLE);
			tv_feijingzhi_1 .setValueText(getString(viewDateMap.get(BocInvestControl.REDDATE)));
			if (Boolean.parseBoolean(viewDateMap.get(BocInvestControl.KEY_ISCHECK_RABTN).toString())) {//用户指定赎回日期
				tv_tip_fjz.setText("根据您的选择，系统将于"+DateUtils.formatStr((String)viewDateMap.get("redDate"))+"发起赎回；如遇节假日，您的赎回交易申请将顺延至下一交易日进行。");
			}else {
				tv_tip_fjz.setText("根据您的选择，系统将于"+DateUtils.formatStr((String)viewDateMap.get("redeemDate"))+"发起赎回；如遇节假日，您的赎回交易申请将顺延至下一交易日进行。");
			}
		}else {
			tv_feijingzhi_1.setVisibility(View.GONE);
			tv_tip_fjz.setVisibility(View.GONE);
		}
		if (!detailMap.get(BocInvt.BOCI_PRODUCTTYPE_REQ).toString().equals("2")&&detailMap.get("isLockPeriod").toString().equals("3")) {//非净值型产品&&3：业绩基准-锁定期周期滚续
			tv_feijingzhi_2.setVisibility(View.VISIBLE);
			tv_feijingzhi_3.setVisibility(View.VISIBLE);
//			tv_feijingzhi_2.setValueText(successMap.get(BocInvt.BOCINVT_BUYRES_CYCLECOUNT_RES).toString());
			tv_feijingzhi_2.setValueText(viewDateMap.get(BocInvestControl.BOCINVT_INVEST_CYCLE_INPUT).toString());
			tv_feijingzhi_3.setValueText(successMap.get(BocInvt.BOCINVT_HOLDPRO_PRODEND_RES).toString());
		}else {
			tv_feijingzhi_2.setVisibility(View.GONE);
			tv_feijingzhi_3.setVisibility(View.GONE);
		}
	}
	private String getString(Object obj){
		return StringUtil.isNullOrEmpty(obj)? "-":obj.toString().trim();
	}
	private String getNullString(Object obj){
		if (StringUtil.isNullOrEmpty(obj)) {
			return "-";
		}
		return obj.toString();
	}
	

	/** 周期性产品初始化 */
//	public void initContractView() {
//		signResultMap = (Map<String, Object>) BaseDroidApp.getInstanse()
//				.getBizDataMap().get(ConstantGloble.BOCINVT_SIGN_RESULT_MAP);
//		Bundle bundle = this.getIntent().getExtras()
//				.getBundle(ConstantGloble.BOCINVT_BUNDLE);
//		extraList = bundle
//				.getStringArrayList(ConstantGloble.BOCINVT_CONTRACT_CONFIRM_EXTRA);
//		// 添加布局
//		view = addView(R.layout.bocinvt_contract_success_activity);
//		// 步骤条
//		StepTitleUtils.getInstance().initTitldStep(
//				this,
//				new String[] {
//						this.getResources().getString(
//								R.string.bocinvt_buy_step2),
//						this.getResources().getString(
//								R.string.bocinvt_buy_step3),
//						this.getResources().getString(
//								R.string.bocinvt_buy_step4) });
//		StepTitleUtils.getInstance().setTitleStep(3);
//		tv_accNumber_contract = (TextView) view
//				.findViewById(R.id.tv_accNumber_contract);
//		tv_invtProdName_contract = (TextView) view
//				.findViewById(R.id.tv_invtProdName_contract);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(
//				BaseDroidApp.getInstanse().getCurrentAct(),
//				tv_invtProdName_contract);
//		tv_serialName_contract = (TextView) view
//				.findViewById(R.id.tv_serialName_contract);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(
//				BaseDroidApp.getInstanse().getCurrentAct(),
//				tv_serialName_contract);
//		tv_curCode_contract = (TextView) view
//				.findViewById(R.id.tv_curCode_cashRemit_contract);
//		tv_remainCycleCount_contract = (TextView) view
//				.findViewById(R.id.tv_remainCycleCount_contract);
//		tv_totalPeriod_contract = (TextView) view
//				.findViewById(R.id.tv_totalPeriod_contract);
//		tv_amountTypeCode_contract = (TextView) view
//				.findViewById(R.id.tv_amountTypeCode_contract);
//		tv_baseAmount_contract = (TextView) view
//				.findViewById(R.id.tv_baseAmount_contract);
//		tv_minAmount_contract = (TextView) view
//				.findViewById(R.id.tv_minAmount_contract);
//		tv_maxAmount_contract = (TextView) view
//				.findViewById(R.id.tv_maxAmount_contract);
//		ll_amountType_base = (LinearLayout) view
//				.findViewById(R.id.ll_baseAmount);
//		ll_amountType_min = (LinearLayout) view.findViewById(R.id.ll_minAmount);
//		ll_amountType_max = (LinearLayout) view.findViewById(R.id.ll_maxAmount);
//		tv_tranSeq_contract = (TextView) view
//				.findViewById(R.id.tv_tranSeq_contract);
//		tv_contractSeq_contract = (TextView) view
//				.findViewById(R.id.tv_contractSeq_contract);
//		tv_operateDate_contract = (TextView) view
//				.findViewById(R.id.tv_operateDate_contract);
//		tv_startPeriod_contract = (TextView) view
//				.findViewById(R.id.tv_startPeriod_contract);
//		tv_endPeriod_contract = (TextView) view
//				.findViewById(R.id.tv_endPeriod_contract);
//		// 赋值操作
//		tv_tranSeq_contract.setText(String.valueOf(signResultMap
//				.get(BocInvt.BOCINVT_SIGNRESULT_TRANSEQ_RES)));
//		tv_contractSeq_contract.setText(String.valueOf(signResultMap
//				.get(BocInvt.BOCINVT_SIGNRESULT_CONTRACTSEQ_RES)));
//		tv_operateDate_contract.setText(String.valueOf(signResultMap
//				.get(BocInvt.BOCINVT_SIGNRESULT_OPERATEDATE_RES)));
//		tv_startPeriod_contract.setText(String.valueOf(signResultMap
//				.get(BocInvt.BOCINVT_SIGNRESULT_STARTPERIOD_RES)));
//		tv_endPeriod_contract.setText(String.valueOf(signResultMap
//				.get(BocInvt.BOCINVT_SIGNRESULT_ENDPERIOD_RES)));
//		tv_accNumber_contract.setText(StringUtil.getForSixForString(extraList
//				.get(0)));
//		tv_invtProdName_contract.setText(extraList.get(1));
//		tv_serialName_contract.setText(extraList.get(2));
//		if (!StringUtil.isNull(extraList.get(3))) {
//			if (LocalData.Currency.get(extraList.get(3)).equals(
//					ConstantGloble.ACC_RMB)) {
//				tv_curCode_contract.setText(LocalData.Currency.get(extraList
//						.get(3)));
//			} else {
//				tv_curCode_contract
//						.setText(LocalData.Currency.get(extraList.get(3))
//								+ LocalData.cashMapValue.get(extraList.get(4)));
//			}
//		}
//
//		tv_remainCycleCount_contract.setText(extraList.get(5));
//		tv_totalPeriod_contract.setText(extraList.get(6));
//		tv_amountTypeCode_contract.setText(LocalData.bociAmountTypeMap
//				.get(extraList.get(7)));
//		tv_baseAmount_contract.setText(StringUtil.parseStringCodePattern(
//				extraList.get(3), extraList.get(8), 2));
//		tv_minAmount_contract.setText(StringUtil.parseStringCodePattern(
//				extraList.get(3), extraList.get(9), 2));
//		tv_maxAmount_contract.setText(StringUtil.parseStringCodePattern(
//				extraList.get(3), extraList.get(10), 2));
//		if ((extraList.get(7)).equalsIgnoreCase(LocalData.orderTimeMap.get(0))) {
//			// 定额
//			ll_amountType_base.setVisibility(View.VISIBLE);
//			ll_amountType_min.setVisibility(View.GONE);
//			ll_amountType_max.setVisibility(View.GONE);
//		} else if ((extraList.get(7)).equalsIgnoreCase(LocalData.orderTimeMap
//				.get(1))) {
//			// 不定额
//			ll_amountType_base.setVisibility(View.GONE);
//			ll_amountType_min.setVisibility(View.VISIBLE);
//			ll_amountType_max.setVisibility(View.VISIBLE);
//		}
//		sureBtn = (Button) view.findViewById(R.id.btn_sure);
//		sureBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				setResult(RESULT_OK);
//				finish();
//			}
//		});
//	}

	/** 非周期性产品初始化 */
//	public void initBuyProductView() {
//		successMap = (Map<String, Object>) BaseDroidApp.getInstanse()
//				.getBizDataMap().get(ConstantGloble.BOCINVT_BUYRESULT_MAP);
//		// 添加布局
//		view = addView(R.layout.bocinvt_buyproduct_success_activity);
//		// 步骤条
//		StepTitleUtils.getInstance().initTitldStep(
//				this,
//				new String[] {
//						this.getResources().getString(
//								R.string.bocinvt_buy_step2),
//						this.getResources().getString(
//								R.string.bocinvt_buy_step3),
//						this.getResources().getString(
//								R.string.bocinvt_buy_step4) });
//		StepTitleUtils.getInstance().setTitleStep(3);
//		/** 理财交易账户 */
//		tv_number = (TextView) view.findViewById(R.id.tv_accNumber);
//		/** 交易序号 */
//		tv_tranSeq = (TextView) view.findViewById(R.id.tv_tranSeq);
//		/** 交易日期 */
//		tv_paymentDate = (TextView) view.findViewById(R.id.tv_paymentDate);
//		/** 产品代码 */
//		tv_prodCode = (TextView) view.findViewById(R.id.tv_prodCode);
//		/** 理财产品名称 */
//		tv_prodName = (TextView) view.findViewById(R.id.tv_prodName);
//		/** 产品币种 */
//		tv_curCode = (TextView) view.findViewById(R.id.tv_curCode_cashRemit);
//		/** 柜员营销代码 */
//		tv_martCode = (TextView) view.findViewById(R.id.tv_martCode);
//		/** 产品成立日 */
//		tv_prodBegin = (TextView) view.findViewById(R.id.tv_prodBegin);
//		/** 产品到期日 */
//		tv_prodEnd = (TextView) view.findViewById(R.id.tv_prodEnd);
//		/** 购买价格 */
//		tv_trfPrice = (TextView) view.findViewById(R.id.tv_trfPrice);
//		/** 购买份额 */
//		tv_trfAmount = (TextView) view.findViewById(R.id.tv_trfAmount);
//		/** 购买金额 */
//		tv_buyPrice = (TextView) view.findViewById(R.id.tv_buyPrice);
//		sureBtn = (Button) view.findViewById(R.id.btn_sure);
//		// 购买初始化返回结果——包含账号信息
//		Map<String, Object> buyInitMap = (Map<String, Object>) BaseDroidApp
//				.getInstanse().getBizDataMap()
//				.get(ConstantGloble.BOCINVT_BUYINIT_MAP);
//		// 赋值
//		String accNumber = String.valueOf(buyInitMap
//				.get(BocInvt.ACCOUNTNO));
//		tv_number.setText(StringUtil.getForSixForString(accNumber));
//		tv_tranSeq.setText((String) successMap
//				.get(BocInvt.BOCINVT_BUYRES_TRANSACTIONID_RES));
//		tv_paymentDate.setText((String) successMap
//				.get(BocInvt.BOCINVT_BUYRES_PAYMENTDATE_RES));
//		tv_prodCode.setText((String) successMap
//				.get(BocInvt.BOCINVT_BUYRES_PRODCODE_RES));
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(
//				BaseDroidApp.getInstanse().getCurrentAct(),
//				tv_prodCode);
//		tv_prodName.setText((String) successMap
//				.get(BocInvt.BOCINVT_BUYRES_PRODNAME_RES));
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(
//				BaseDroidApp.getInstanse().getCurrentAct(),
//				tv_prodName);
//		if (!StringUtil.isNull((String) detailMap
//				.get(BocInvt.BOCI_DETAILCURCODE_RES))) {
//			if (LocalData.Currency.get(
//					(String) detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES))
//					.equals(ConstantGloble.ACC_RMB)) {
//				tv_curCode.setText(LocalData.Currency.get((String) detailMap
//						.get(BocInvt.BOCI_DETAILCURCODE_RES)));
//			} else {
//				tv_curCode.setText(LocalData.Currency.get((String) detailMap
//						.get(BocInvt.BOCI_DETAILCURCODE_RES))
//						+ LocalData.cashMapValue.get((String) successMap
//								.get(BocInvt.BOCINVT_BUYRES_CASHREMIT_RES)));
//			}
//		}
//
//		tv_martCode
//				.setText(StringUtil.isNull((String) successMap
//						.get(BocInvt.BOCINVT_BUYRES_MARTCODE_RES)) ? ConstantGloble.BOCINVT_DATE_ADD
//						: (String) successMap
//								.get(BocInvt.BOCINVT_BUYRES_MARTCODE_RES));
//		tv_prodBegin.setText((String) successMap
//				.get(BocInvt.BOCINVT_BUYRES_PRODBEGIN_RES));
//		tv_prodEnd.setText((String) successMap
//				.get(BocInvt.BOCINVT_BUYRES_PRODEND_RES));
//		tv_trfPrice
//				.setText(StringUtil.parseStringCodePattern((String) detailMap
//						.get(BocInvt.BOCI_DETAILCURCODE_RES),
//						(String) successMap
//								.get(BocInvt.BOCINVT_BUYRES_TRFPRICE_RES), 2));
//		tv_trfAmount.setText(StringUtil.parseStringCodePattern(
//				(String) detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES),
//				(String) successMap.get(BocInvt.BOCINVT_BUYRES_TRFAMOUNT_RES),
//				2));
//		tv_buyPrice
//				.setText(StringUtil.parseStringCodePattern((String) detailMap
//						.get(BocInvt.BOCI_DETAILCURCODE_RES),
//						(String) successMap
//								.get(BocInvt.BOCINVT_BUYRES_BUYPRICE_RES), 2));
//		sureBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				setResult(RESULT_OK);
//				finish();
//			}
//		});
//	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 屏蔽返回键
			return false;
		}
		return super.onKeyDown(keyCode, event);

	}
}
