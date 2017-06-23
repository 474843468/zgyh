package com.chinamworld.bocmbci.biz.bocinvt.productlist;

import java.util.HashMap;
import java.util.List;
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
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.utils.DateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 中银理财 购买确认信息页
 * 
 * @author wangmengmeng
 * 
 */
public class BuyProductConfirmActivity extends BociBaseActivity implements
		OnClickListener {
	/** 购买产品输入信息页面 */
//	private View view;
	/** 产品详情列表 */
	private Map<String, Object> detailMap;
	// 非周期性产品////////////////////////////////////////////////////
	/** 非周期性产品确认信息列表 */
//	private Map<String, Object> noPeriodicalMap;
	/** 预交易 返回数据+输入页 信息拼接数据 */
	private Map<String, Object> viewDateMap;
	/** 理财交易账户 */
//	private TextView tv_number_validate;
//	/** 产品代码 */
//	private TextView tv_prodCode_validate;
//	/** 理财产品名称 */
//	private TextView tv_prodName_validate;
//	/** 产品币种 */
//	private TextView tv_curCode_validate;
//	/** 柜员营销代码 */
//	private TextView tv_martCode_validate;
//	/** 产品成立日 */
//	private TextView tv_prodBegin_validate;
//	/** 产品到期日 */
//	private TextView tv_prodEnd_validate;
//	/** 购买价格 */
//	private TextView tv_trfPrice_validate;
//	/** 购买金额 */
//	private TextView tv_buyPrice_validate;
//	/** 产品风险级别 */
//	private TextView tv_pro_risk_validate;
//	/** 客户风险级别 */
//	private TextView tv_cust_risk_validate;
//	/** 上一步 */
//	private Button btn_pre_validate;
//	/** 确定 */
//	private Button btn_next_validate;
	// 周期性产品//////////////////////////////////////////////
	/** 周期性产品签约初始化列表信息 */
	Map<String, Object> signInitMap;
	/** 理财交易账户 */
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
//	/** 上一步 */
//	private Button btn_pre_contract;
//	/** 确定 */
//	private Button btn_next_contract;
	/** 周期性产品确认信息 */
//	private ArrayList<String> extraList;
	/** 请求标识 1——非周期性产品 2——周期性产品 */
//	private int conid = 0;
	/** 等级是否匹配 */
	private Map<String, Object> riskMatchMap;
	/**产品性质：true/结构性理财、false类基金理财产品*/
	/** 提示信息 */
	private TextView tv_jz_confirm,tv_fjz_confirm;
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
		// 界面初始化
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
	/**净值型产品  布局*/
	private View layout_jingzhi;
	/**非净值型产品  布局*/
	private View layout_feijingzhi;
	private Button btn_sure;
	private LabelTextView tv_jinzhi_1;
	private LabelTextView tv_jinzhi_2;
	private LabelTextView tv_jinzhi_3;
	private LabelTextView tv_jinzhi_4;
	private LabelTextView tv_jinzhi_5;
	private LabelTextView tv_jinzhi_12;
	private LabelTextView tv_jinzhi_6;
	private LabelTextView tv_jinzhi_7;
	private LabelTextView tv_jinzhi_8;
//	private LabelTextView tv_jinzhi_9;
//	private LabelTextView tv_jinzhi_10;
//	private LabelTextView tv_jinzhi_11;
	private LabelTextView tv_feijinzhi_1;
	private LabelTextView tv_feijinzhi_2;
	private LabelTextView tv_feijinzhi_3;
	private LabelTextView tv_feijinzhi_4;
	private LabelTextView tv_feijinzhi_5;
	private LabelTextView tv_feijinzhi_7;
	private LabelTextView tv_feijinzhi_8;
	private LabelTextView tv_feijinzhi_9;
	private LabelTextView tv_feijinzhi_15;
	private LabelTextView tv_feijinzhi_10;
	private LabelTextView tv_feijinzhi_11;
	private LabelTextView tv_feijinzhi_12;
	private TextView tv_tip_fjz,tv_tip_fjz_1;
	private LabelTextView tv_feijinzhi_13;
	private LabelTextView tv_feijinzhi_14;

	@SuppressWarnings("unchecked")
	private void init() {
		riskMatchMap = BociDataCenter.getInstance().getRiskMatchMap();
		detailMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_PRODUCTDETAIL_LIST);
//		Map<String, Object> chooseMap = BociDataCenter.getInstance().getChoosemap();
		viewDateMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_CONFIRM_MAP);
//		if (detailMap.get(BocInvt.BOCI_DETAILPERIODICAL_RES).equals("1")) {
//			// 周期性产品
//			initContractView();
//		} else {
			// 如果是非周期性产品
//			initBuyProductView();
		initUI();
//		}
	}

	/** 周期性产品初始化 */
//	public void initContractView() {
//		signInitMap = (Map<String, Object>) BaseDroidApp.getInstanse()
//				.getBizDataMap().get(ConstantGloble.BOCINVT_SIGNINIT_MAP);
//		Bundle bundle = this.getIntent().getExtras()
//				.getBundle(ConstantGloble.BOCINVT_BUNDLE);
//		extraList = bundle
//				.getStringArrayList(ConstantGloble.BOCINVT_CONTRACT_CONFIRM_EXTRA);
//		// 添加布局
//		view = addView(R.layout.bocinvt_contractprod_validate_activity);
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
//		StepTitleUtils.getInstance().setTitleStep(2);
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
//		// 赋值操作
//		tv_accNumber_contract.setText(StringUtil.getForSixForString(extraList
//				.get(0)));
//		tv_invtProdName_contract.setText(extraList.get(1));
//		tv_serialName_contract.setText(extraList.get(2));
//		if (!StringUtil.isNull(LocalData.Currency.get(extraList.get(3)))) {
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
//		TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
//		// （极低风险级别显示1）（低风险级别显示2）（中等风险或更高级别显示3）
//		String type = (String) riskMatchMap
//				.get(BocInvt.BOCINVT_MATCH_RISKMSG_RES);
//		if(!StringUtil.isNullOrEmpty(type)){
//		if (type.equals(investTypeSubList.get(0))) {
//			tv_confirm.setText(this
//					.getString(R.string.bocinvt_confirm_bottom_1));
//		} else if (type.equals(investTypeSubList.get(1))) {
//			tv_confirm.setText(this
//					.getString(R.string.bocinvt_confirm_bottom_2));
//		} else {
//			tv_confirm.setText(this
//					.getString(R.string.bocinvt_confirm_bottom_3));
//		}
//		}
//		btn_next_contract = (Button) view.findViewById(R.id.btn_connext);
//		btn_pre_contract = (Button) view.findViewById(R.id.btn_conpre);
//		btn_pre_contract.setOnClickListener(this);
//		btn_next_contract.setOnClickListener(this);
//	}

	/** 非周期性产品初始化 */
//	public void initBuyProductView() {
//
//		noPeriodicalMap = (Map<String, Object>) BaseDroidApp.getInstanse()
//				.getBizDataMap().get(ConstantGloble.BOCINVT_CONFIRM_MAP);
//		// 添加布局
////		view = addView(R.layout.bocinvt_buyproduct_validate_activity);
//		view = addView(R.layout.product_query_and_buy_buy_sure);
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
//		StepTitleUtils.getInstance().setTitleStep(2);
//		// 初始化
//		/** 理财交易账户 */
//		tv_number_validate = (TextView) view
//				.findViewById(R.id.tv_number_validate);
//		/** 产品代码 */
//		tv_prodCode_validate = (TextView) view
//				.findViewById(R.id.tv_prodCode_validate);
//		/** 理财产品名称 */
//		tv_prodName_validate = (TextView) view
//				.findViewById(R.id.tv_prodName_validate);
//		/** 产品币种_钞汇标志 */
//		tv_curCode_validate = (TextView) view
//				.findViewById(R.id.tv_curCode_cashRemit_validate);
//		/** 柜员营销代码 */
//		tv_martCode_validate = (TextView) view
//				.findViewById(R.id.tv_martCode_validate);
//		/** 产品成立日 */
//		tv_prodBegin_validate = (TextView) view
//				.findViewById(R.id.tv_prodBegin_validate);
//		/** 产品到期日 */
//		tv_prodEnd_validate = (TextView) view
//				.findViewById(R.id.tv_prodEnd_validate);
//		/** 购买价格 */
//		tv_trfPrice_validate = (TextView) view
//				.findViewById(R.id.tv_trfPrice_validate);
//		/** 购买金额 */
//		tv_buyPrice_validate = (TextView) view
//				.findViewById(R.id.tv_buyPrice_validate);
//		/** 产品风险级别 */
//		tv_pro_risk_validate = (TextView) view
//				.findViewById(R.id.tv_pro_risk_validate);
//		/** 客户风险级别 */
//		tv_cust_risk_validate = (TextView) view
//				.findViewById(R.id.tv_cust_risk_validate);
//		// 赋值操作
//		String prodNum = String.valueOf(noPeriodicalMap
//				.get(BocInvt.BOCINVT_BUYINIT_ACCOUNTNUMBER_RES));
//		tv_number_validate.setText(StringUtil.getForSixForString(prodNum));
//		tv_prodCode_validate.setText(String.valueOf(noPeriodicalMap
//				.get(BocInvt.BOCINVT_BUYPRE_PRODUCTCODE_REQ)));
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(
//				BaseDroidApp.getInstanse().getCurrentAct(),
//				tv_prodCode_validate);
//		tv_prodName_validate.setText(String.valueOf(noPeriodicalMap
//				.get(BocInvt.BOCINVT_BUYPRE_PRODUCTNAME_REQ)));
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(
//				BaseDroidApp.getInstanse().getCurrentAct(),
//				tv_prodName_validate);
//		String curcode = String.valueOf(noPeriodicalMap
//				.get(BocInvt.BOCINVT_BUYPRE_CURCODE_REQ));
//		if (!StringUtil.isNull(LocalData.Currency.get(curcode))) {
//			if (LocalData.Currency.get(curcode).equals(ConstantGloble.ACC_RMB)) {
//				tv_curCode_validate.setText(LocalData.Currency.get(curcode));
//			} else {
//				tv_curCode_validate
//						.setText(LocalData.Currency.get(curcode)
//								+ LocalData.cashMapValue.get(String.valueOf(noPeriodicalMap
//										.get(BocInvt.BOCINVT_BUYPRE_XPADCASHREMIT_REQ))));
//			}
//		}
//
//		String martCode = (String) noPeriodicalMap
//				.get(BocInvt.BOCINVT_BUYPRE_MARTCODE_REQ);
//		tv_martCode_validate
//				.setText(StringUtil.isNull(martCode) ? ConstantGloble.BOCINVT_DATE_ADD
//						: martCode);
//		tv_prodBegin_validate.setText(String.valueOf(noPeriodicalMap
//				.get(BocInvt.BOCI_DETAILPRODBEGIN_RES)));
//		tv_prodEnd_validate.setText(String.valueOf(noPeriodicalMap
//				.get(BocInvt.BOCI_DETAILPRODEND_RES)));
//		String trfPrice = String.valueOf(noPeriodicalMap
//				.get(BocInvt.BOCI_DETAILBUYPRICE_RES));
//		tv_trfPrice_validate.setText(StringUtil.parseStringCodePattern(curcode,
//				trfPrice, 2));
//		String buyPrice = String.valueOf(noPeriodicalMap
//				.get(ConstantGloble.BOCINVT_BUYPRICE_STRING));
//		tv_buyPrice_validate.setText(StringUtil.parseStringCodePattern(curcode,
//				buyPrice, 2));
//		tv_pro_risk_validate.setText(LocalData.boci_prodRisklvlMap.get(String
//				.valueOf(noPeriodicalMap
//						.get(BocInvt.BOCI_DETAILPRODRISKLVL_RES))));
//		tv_cust_risk_validate
//				.setText(LocalData.riskLevelMap.get(String
//						.valueOf(noPeriodicalMap
//								.get(BocInvt.BOCINVT_EVA_RISKLEVEL_RES))));
//		TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
//		// （极低风险级别显示1）（低风险级别显示2）（中等风险或更高级别显示3）
//		String type = (String) riskMatchMap
//				.get(BocInvt.BOCINVT_MATCH_RISKMSG_RES);
//		if(!StringUtil.isNullOrEmpty(type)){
//			if (type.equals(investTypeSubList.get(0))) {
//				tv_confirm.setText(this
//						.getString(R.string.bocinvt_confirm_bottom_1));
//			} else if (type.equals(investTypeSubList.get(1))) {
//				tv_confirm.setText(this
//						.getString(R.string.bocinvt_confirm_bottom_2));
//			} else {
//				tv_confirm.setText(this
//						.getString(R.string.bocinvt_confirm_bottom_3));
//			}	
//		}
//		
//		// 按钮初始化
//		btn_pre_validate = (Button) view.findViewById(R.id.btn_pre_validate);
//		btn_next_validate = (Button) view.findViewById(R.id.btn_next_validate);
//		btn_pre_validate.setOnClickListener(this);
//		btn_next_validate.setOnClickListener(this);
//	}
	
	/**
	 * 初始化内容组件
	 */
	private void initUI(){
		addView(R.layout.product_query_and_buy_buy_sure);
		//初始化
		layout_jingzhi = findViewById(R.id.layout_jingzhi);
		layout_feijingzhi = findViewById(R.id.layout_feijingzhi);
		btn_sure = (Button) findViewById(R.id.btn_sure);
		tv_jinzhi_1 = (LabelTextView) findViewById(R.id.tv_jinzhi_1);
		tv_jinzhi_2 = (LabelTextView) findViewById(R.id.tv_jinzhi_2);
		tv_jinzhi_3 = (LabelTextView) findViewById(R.id.tv_jinzhi_3);
		tv_jinzhi_4 = (LabelTextView) findViewById(R.id.tv_jinzhi_4);
		tv_jinzhi_5 = (LabelTextView) findViewById(R.id.tv_jinzhi_5);
		tv_jinzhi_12 = (LabelTextView) findViewById(R.id.tv_jinzhi_12);
		tv_jinzhi_6 = (LabelTextView) findViewById(R.id.tv_jinzhi_6);
		tv_jinzhi_7 = (LabelTextView) findViewById(R.id.tv_jinzhi_7);
		tv_jinzhi_8 = (LabelTextView) findViewById(R.id.tv_jinzhi_8);
		tv_feijinzhi_1 = (LabelTextView) findViewById(R.id.tv_feijinzhi_1);
		tv_feijinzhi_2 = (LabelTextView) findViewById(R.id.tv_feijinzhi_2);
		tv_feijinzhi_3 = (LabelTextView) findViewById(R.id.tv_feijinzhi_3);
		tv_feijinzhi_4 = (LabelTextView) findViewById(R.id.tv_feijinzhi_4);
		tv_feijinzhi_5 = (LabelTextView) findViewById(R.id.tv_feijinzhi_5);
		tv_feijinzhi_7 = (LabelTextView) findViewById(R.id.tv_feijinzhi_7);
		tv_feijinzhi_8 = (LabelTextView) findViewById(R.id.tv_feijinzhi_8);
		tv_feijinzhi_9 = (LabelTextView) findViewById(R.id.tv_feijinzhi_9);
		tv_feijinzhi_15 = (LabelTextView) findViewById(R.id.tv_feijinzhi_15);
		tv_feijinzhi_10 = (LabelTextView) findViewById(R.id.tv_feijinzhi_10);
		tv_feijinzhi_11 = (LabelTextView) findViewById(R.id.tv_feijinzhi_11);
		tv_feijinzhi_12 = (LabelTextView) findViewById(R.id.tv_feijinzhi_12);
		tv_tip_fjz = (TextView) findViewById(R.id.tv_tip_fjz);
		tv_tip_fjz_1 = (TextView) findViewById(R.id.tv_tip_fjz_1);
		tv_feijinzhi_13 = (LabelTextView) findViewById(R.id.tv_feijinzhi_13);
		tv_feijinzhi_14 = (LabelTextView) findViewById(R.id.tv_feijinzhi_14);
	//赋值
		btn_sure.setOnClickListener(this);
		String type = (String) riskMatchMap.get(BocInvt.BOCINVT_MATCH_RISKMSG_RES);
		if (detailMap.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTTYPE_RES).toString().equals("2")) {
			//净值型产品
			layout_jingzhi.setVisibility(View.VISIBLE);
			layout_feijingzhi.setVisibility(View.GONE);
			setViewContent1();
			tv_jz_confirm = (TextView) findViewById(R.id.tv_jz_confirm);
			if (type.equals("0")) {
				tv_jz_confirm.setText(this.getString(R.string.bocinvt_confirm_bottom_1));	
			} else if (type.equals("1")) {
				tv_jz_confirm.setText(this.getString(R.string.bocinvt_confirm_bottom_2));
			} else {
				tv_jz_confirm.setText(this.getString(R.string.bocinvt_confirm_bottom_3));
			}
		}else {
			//非净值型产品
			layout_jingzhi.setVisibility(View.GONE);
			layout_feijingzhi.setVisibility(View.VISIBLE);
			setViewContent2();
			tv_fjz_confirm = (TextView) findViewById(R.id.tv_fjz_confirm);
			if (type.equals("0")) {
				tv_fjz_confirm.setText(this.getString(R.string.bocinvt_confirm_bottom_1));	
			} else if (type.equals("1")) {
				tv_fjz_confirm.setText(this.getString(R.string.bocinvt_confirm_bottom_2));
			} else {
				tv_fjz_confirm.setText(this.getString(R.string.bocinvt_confirm_bottom_3));
			}
		}
		
	}
	/**
	 * 净值型产品  UI 赋值
	 */
	private void setViewContent1(){
		tv_jinzhi_1.setValueText(detailMap.get(BocInvt.BOCI_PRODCODE_RES).toString());
		tv_jinzhi_2.setValueText(detailMap.get(BocInvt.BOCI_PRODNAME_RES).toString());
		tv_jinzhi_3.setValueText(BocInvestControl.map_productCurCode_toStr.get(detailMap.get(BocInvt.BOCI_CURCODE_RES).toString()));
		tv_jinzhi_4.setValueText(getString(BocInvestControl.map_xpadCashRemit_code_key.get(viewDateMap.get(BocInvt.BOCINVT_BUYPRE_XPADCASHREMIT_REQ))));
		tv_jinzhi_5.setValueText(StringUtil.parseStringCodePattern((String)detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES), viewDateMap.get(ConstantGloble.BOCINVT_BUYPRICE_STRING).toString(), 2));
		tv_jinzhi_12.setValueText(viewDateMap.get("purDate").toString());
		if (!StringUtil.isNullOrEmpty(detailMap.get(BocInvestControl.TRANSTYPECODE))) {
			switch (Integer.parseInt(detailMap.get(BocInvestControl.TRANSTYPECODE).toString())) {
			case 0:{//认购
				tv_jinzhi_6.setLabelText("认购手续费：");
			}break;
			case 1:{//申购
				tv_jinzhi_6.setLabelText("申购手续费：");
			}break;
			default:
				break;
			}
		}
		tv_jinzhi_6.setValueText(StringUtil.parseStringCodePattern(detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES).toString(), viewDateMap.get(BocInvestControl.TRANSFEE).toString(), 2));
		tv_jinzhi_7.setValueText(getString(viewDateMap.get(BocInvt.BOCINVT_BUYPRE_MARTCODE_REQ)));
		if (detailMap.get("appdatered").toString().equals("1")&&Boolean.parseBoolean(viewDateMap.get(BocInvestControl.KEY_ISCHECK_RABTN).toString())) {//是否允许指定日期赎回,0：否1：是
			tv_jinzhi_8.setVisibility(View.VISIBLE);
			tv_jinzhi_8 .setValueText(getString(viewDateMap.get(BocInvestControl.REDDATE)));
			if (Boolean.parseBoolean(viewDateMap.get(BocInvestControl.KEY_ISCHECK_RABTN).toString())) {//用户指定赎回日期
				tv_tip_fjz_1.setText("根据您的选择，系统将于"+DateUtils.formatStr((String)viewDateMap.get("redDate"))+"发起赎回；如遇节假日，您的赎回交易申请将顺延至下一交易日进行。");
			}else {
				tv_tip_fjz_1.setText("根据您的选择，系统将于"+DateUtils.formatStr((String)viewDateMap.get("redeemDate"))+"发起赎回；如遇节假日，您的赎回交易申请将顺延至下一交易日进行。");
			}
		}else {
			tv_jinzhi_8.setVisibility(View.GONE);
			tv_tip_fjz_1.setVisibility(View.GONE);
		}
	}
	private String getString(Object obj){
		return StringUtil.isNullOrEmpty(obj)? "-":obj.toString().trim();
	}
	/**
	 * 非净值型产品  UI 赋值
	 */
	private void setViewContent2(){
		tv_feijinzhi_1 .setValueText(detailMap.get(BocInvt.BOCI_PRODCODE_RES).toString());
		tv_feijinzhi_2 .setValueText(detailMap.get(BocInvt.BOCI_PRODNAME_RES).toString());
		tv_feijinzhi_3 .setValueText(BocInvestControl.map_productCurCode_toStr.get(detailMap.get(BocInvt.BOCI_CURCODE_RES).toString()));
		tv_feijinzhi_4 .setValueText(getString(BocInvestControl.map_xpadCashRemit_code_key.get(viewDateMap.get(BocInvt.BOCINVT_BUYPRE_XPADCASHREMIT_REQ))));
			switch (Integer.parseInt(detailMap.get("isLockPeriod").toString())) {
			case 0:{//0：非业绩基准产品
				if (detailMap.get("productTermType").toString().equals("3")) {//产品期限特性,3：无限开放式
					tv_feijinzhi_5 .setValueText("无固定期限");
				}else {
					tv_feijinzhi_5 .setValueText(String.valueOf(detailMap.get(BocInvt.BOCI_PRODTIMELIMIT_RES)) + "天");
				}
			}break;
			case 1:{//1：业绩基准-锁定期转低收益 
				tv_feijinzhi_5 .setValueText("最低持有"+String.valueOf(detailMap.get(BocInvt.BOCI_PRODTIMELIMIT_RES)) + "天");
			}break;
			default:{//2：业绩基准-锁定期后入账    3：业绩基准-锁定期周期滚续
				tv_feijinzhi_5 .setValueText(String.valueOf(detailMap.get(BocInvt.BOCI_PRODTIMELIMIT_RES)) + "天");
			}break;
			}
		if (detailMap.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTTYPE_RES).toString().equals("3")) {//固定期限类产品
			tv_feijinzhi_7.setVisibility(View.VISIBLE);
			if (detailMap.get("isLockPeriod").toString().equals("1")) {//业绩基准-转低收益产品显示“可赎回日期”
				tv_feijinzhi_7.setLabelText("可赎回日期：");
				tv_feijinzhi_7 .setValueText(getString(viewDateMap.get("prodEnd")));
			}else {//其他固定期限产品显示“产品到期日”
				tv_feijinzhi_7.setLabelText("产品到期日：");
				tv_feijinzhi_7 .setValueText(getString(viewDateMap.get("prodEnd")));
			}
		}else {
			tv_feijinzhi_7.setVisibility(View.GONE);
		}
		tv_feijinzhi_8 .setValueText(/*StringUtil.parseStringCodePattern((String)detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES),
				 * viewDateMap.get(BocInvt.BOCINVT_BUYPRE_TRFPRICE_RES).toString(), 2)*/
				StringUtil.append2Decimals(viewDateMap.get(BocInvt.BOCINVT_BUYPRE_TRFPRICE_RES).toString(), 2));
		tv_feijinzhi_9 .setValueText(StringUtil.parseStringCodePattern((String)detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES), viewDateMap.get(ConstantGloble.BOCINVT_BUYPRICE_STRING).toString(), 2));
		tv_feijinzhi_15.setValueText(viewDateMap.get("purDate").toString());
		if (!detailMap.get("isLockPeriod").equals("0")) {//业绩基准型产品
			tv_feijinzhi_10.setVisibility(View.VISIBLE);
			tv_feijinzhi_10 .setValueText(viewDateMap.get(BocInvestControl.EXYIELD).toString()+"%");
		}else {
			tv_feijinzhi_10.setVisibility(View.GONE);
		}
		tv_feijinzhi_11 .setValueText(getString(viewDateMap.get(BocInvt.BOCINVT_BUYPRE_MARTCODE_REQ)));
		if (detailMap.get("appdatered").toString().equals("1")&&Boolean.parseBoolean(viewDateMap.get(BocInvestControl.KEY_ISCHECK_RABTN).toString())) {//是否允许指定日期赎回,0：否1：是
			tv_feijinzhi_12.setVisibility(View.VISIBLE);
			tv_feijinzhi_12 .setValueText(getString(viewDateMap.get(BocInvestControl.REDDATE)));
			if (Boolean.parseBoolean(viewDateMap.get(BocInvestControl.KEY_ISCHECK_RABTN).toString())) {//用户指定赎回日期
				tv_tip_fjz.setText("根据您的选择，系统将于"+DateUtils.formatStr((String)viewDateMap.get("redDate"))+"发起赎回；如遇节假日，您的赎回交易申请将顺延至下一交易日进行。");
			}else {
				tv_tip_fjz.setText("根据您的选择，系统将于"+DateUtils.formatStr((String)viewDateMap.get("redeemDate"))+"发起赎回；如遇节假日，您的赎回交易申请将顺延至下一交易日进行。");
			}
		}else {
			tv_feijinzhi_12.setVisibility(View.GONE);
			tv_tip_fjz.setVisibility(View.GONE);
		}
		if (detailMap.get("isLockPeriod").toString().equals("3")) {//3：业绩基准-锁定期周期滚续
			tv_feijinzhi_13.setVisibility(View.VISIBLE);
			tv_feijinzhi_14.setVisibility(View.VISIBLE);
			tv_feijinzhi_13.setValueText(viewDateMap.get(BocInvt.BOCINVT_BUYRES_CYCLECOUNT_RES).toString());
			tv_feijinzhi_14 .setValueText(getString(viewDateMap.get(BocInvestControl.REDEEMDATE)));
		}else {
			tv_feijinzhi_13.setVisibility(View.GONE);
			tv_feijinzhi_14.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_pre_validate:
//		case R.id.btn_conpre:
//			// 非周期性产品、周期性产品上一步操作
//			setResult(RESULT_CANCELED);
//			finish();
//			break;
//		case R.id.btn_next_validate:
//			// 非周期性产品确定操作
//			conid = 1;
//			pSNGetTokenId();
//			break;
		case R.id.btn_sure:
			// 确定操作
//			conid = 1;
			pSNGetTokenId();
			break;
//		case R.id.btn_connext:
//			// 周期性产品确定操作 请求签约结果
//			conid = 2;
//			pSNGetTokenId();
//			break;
		default:
			break;
		}

	}

	/**
	 * 获取tokenId
	 */
	public void pSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenId");
	}

	/**
	 * 处理获取tokenId的数据得到tokenId
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void aquirePSNGetTokenId(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
//		if (conid == 1) {
			requestPsnXpadProductBuyResult(tokenId);
//		} else if (conid == 2) {
//			requestPsnXpadSignResult(tokenId);
//		}

	}

	/** 请求产品签约处理结果 */
//	public void requestPsnXpadSignResult(String tokenId) {
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADSIGNRESULT_API);
//		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
//				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
//		Map<String, Object> paramsmap = new HashMap<String, Object>();
//		Map<String, Object> accountSignInitMap = (Map<String, Object>) signInitMap
//				.get(BocInvt.BOCINVT_SIGNINIT_ACCOUNTINFO_RES);
//		paramsmap.put(BocInvt.BOCINVT_SIGNRESULT_ACCOUNTID_REQ, String
//				.valueOf(accountSignInitMap
//						.get(BocInvt.BOCINVT_SIGNINIT_ACCOUNTID_RES)));
//		paramsmap.put(BocInvt.BOCINVT_SIGNRESULT_SERIALNAME_REQ,
//				extraList.get(2));
//		paramsmap.put(BocInvt.BOCINVT_SIGNRESULT_SERIALCODE_REQ, String
//				.valueOf(signInitMap
//						.get(BocInvt.BOCINVT_SIGNINIT_SERIALCODE_RES)));
//		/** 钞汇标识 */
//		paramsmap.put(BocInvt.BOCINVT_SIGNRESULT_XPADCASHREMIT_REQ,
//				extraList.get(4));
//		/** 购买期数 */
//		paramsmap.put(BocInvt.BOCINVT_SIGNRESULT_TOTALPERIOD_REQ,
//				extraList.get(6));
//		/** 基础金额模式 */
//		paramsmap.put(BocInvt.BOCINVT_SIGNRESULT_AMOUNTTYPECODE_REQ,
//				extraList.get(7));
//		/** 币种 */
//		paramsmap.put(BocInvt.BOCINVT_SIGNRESULT_CURCODE_REQ, extraList.get(3));
//		if (extraList.get(7).equals(LocalData.orderTimeMap.get(0))) {
//			// 定额
//			/** 基础金额 */
//			paramsmap.put(BocInvt.BOCINVT_SIGNRESULT_BASEAMOUNT_REQ,
//					extraList.get(8));
//			/** 最低预留金额 */
//			paramsmap.put(BocInvt.BOCINVT_SIGNRESULT_MINAMOUNT_REQ, 0);
//			/** 最大扣款金额 */
//			paramsmap.put(BocInvt.BOCINVT_SIGNRESULT_MAXAMOUNT_REQ, 0);
//		} else if (extraList.get(7).equals(LocalData.orderTimeMap.get(1))) {
//			// 不定额
//			/** 基础金额 */
//			paramsmap.put(BocInvt.BOCINVT_SIGNRESULT_BASEAMOUNT_REQ, 0);
//			/** 最低预留金额 */
//			paramsmap.put(BocInvt.BOCINVT_SIGNRESULT_MINAMOUNT_REQ,
//					extraList.get(9));
//			/** 最大扣款金额 */
//			paramsmap.put(BocInvt.BOCINVT_SIGNRESULT_MAXAMOUNT_REQ,
//					extraList.get(10));
//		}
//		paramsmap.put(BocInvt.BOCINVT_SIGNRESULT_TOKENID_REQ, tokenId);
//		biiRequestBody.setParams(paramsmap);
//		HttpManager.requestBii(biiRequestBody, this,
//				"requestPsnXpadSignResultCallBack");
//	}

	/** 请求产品签约处理结果回调 */
//	public void requestPsnXpadSignResultCallBack(Object resultObj) {
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		BiiHttpEngine.dissMissProgressDialog();
//		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
//				.getResult();
//		if (StringUtil.isNullOrEmpty(resultMap)) {
//			return;
//		}
//		BaseDroidApp.getInstanse().getBizDataMap()
//				.put(ConstantGloble.BOCINVT_SIGN_RESULT_MAP, resultMap);
//		Intent intent = new Intent(this, BuyProductSuccessActivity.class);
//		Bundle bundle = new Bundle();
//		bundle.putStringArrayList(
//				ConstantGloble.BOCINVT_CONTRACT_CONFIRM_EXTRA, extraList);
//		intent.putExtra(ConstantGloble.BOCINVT_BUNDLE, bundle);
//		startActivityForResult(intent, ACTIVITY_BUY_CODE);
//	}

	/** 请求产品购买处理结果 */
	@SuppressWarnings("unchecked")
	public void requestPsnXpadProductBuyResult(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADPRODUCTBUYRESULT_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();

		/** 产品代码 */
//		paramsmap.put(BocInvt.BOCINVT_BUYRES_PRODUCTCODE_REQ, String
//				.valueOf(noPeriodicalMap
//						.get(BocInvt.BOCINVT_BUYPRE_PRODUCTCODE_REQ)));
		paramsmap.put(BocInvt.BOCINVT_BUYRES_PRODUCTCODE_REQ, detailMap.get(BocInvt.BOCI_PRODCODE_RES).toString());
		/** 钞汇标识 */
//		paramsmap.put(BocInvt.BOCINVT_BUYRES_XPADCASHREMIT_REQ, String
//				.valueOf(noPeriodicalMap
//						.get(BocInvt.BOCINVT_BUYPRE_XPADCASHREMIT_REQ)));
		paramsmap.put(BocInvt.BOCINVT_BUYRES_XPADCASHREMIT_REQ, viewDateMap.get(BocInvt.BOCINVT_BUYPRE_XPADCASHREMIT_REQ));
		/** 购买金额 */
//		paramsmap.put(BocInvt.BOCINVT_BUYRES_BUYPRICE_REQ, String
//				.valueOf(noPeriodicalMap
//						.get(ConstantGloble.BOCINVT_BUYPRICE_STRING)));
		paramsmap.put(BocInvt.BOCINVT_BUYRES_BUYPRICE_REQ, viewDateMap.get(ConstantGloble.BOCINVT_BUYPRICE_STRING).toString());
		/** 是否自动续存 */
		paramsmap.put(BocInvt.BOCINVT_BUYRES_ISAUTOSER_REQ,
				ConstantGloble.BOCINVT_NULL_STRING);
		/** 产品名称 */
//		paramsmap.put(BocInvt.BOCINVT_BUYRES_PRODUCTNAME_REQ, String
//				.valueOf(noPeriodicalMap
//						.get(BocInvt.BOCINVT_BUYPRE_PRODUCTNAME_REQ)));
		paramsmap.put(BocInvt.BOCINVT_BUYRES_PRODUCTNAME_REQ, detailMap.get(BocInvt.BOCI_PRODNAME_RES).toString());
		/** 防重标识 */
		paramsmap.put(BocInvt.BOCINVT_BUYRES_TOKEN_REQ, tokenId);
		/** 产品币种 */
//		paramsmap.put(BocInvt.BOCINVT_BUYRES_CURRENCY_REQ, String
//				.valueOf(noPeriodicalMap
//						.get(BocInvt.BOCINVT_BUYPRE_CURCODE_REQ)));
		paramsmap.put(BocInvt.BOCINVT_BUYRES_CURRENCY_REQ, detailMap.get(BocInvt.BOCI_CURCODE_RES).toString());
		/** 购买价格 */
//		paramsmap.put(BocInvt.BOCINVT_BUYRES_AMOUNT_REQ, String
//				.valueOf(noPeriodicalMap.get(BocInvt.BOCI_DETAILBUYPRICE_RES)));
		paramsmap.put(BocInvt.BOCINVT_BUYRES_AMOUNT_REQ, viewDateMap.get(BocInvt.BOCINVT_BUYPRE_TRFPRICE_RES).toString());
		paramsmap.put(Comm.ACCOUNT_ID, ((Map<String, Object>)BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_BUYINIT_MAP)).get(Comm.ACCOUNT_ID));
		if (detailMap.get(BocInvestControl.PRODUCTKIND).toString().equals("1")
				||detailMap.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTTYPE_RES).toString().equals("2")) {
			paramsmap.put(BocInvestControl.PRODUCTKIND, "1");
		}else {
			paramsmap.put(BocInvestControl.PRODUCTKIND, "0");
		}
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnXpadProductBuyResultCallBack");
	}

	/** 请求产品购买处理结果回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnXpadProductBuyResultCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
//		resultMap.put(BocInvt.BOCINVT_BUYINIT_ACCOUNTNUMBER_RES, String
//				.valueOf(noPeriodicalMap
//						.get(BocInvt.BOCINVT_BUYINIT_ACCOUNTNUMBER_RES)));
		resultMap.put(BocInvt.BOCINVT_BUYINIT_ACCOUNTNUMBER_RES, viewDateMap.get(BocInvt.BOCINVT_BUYINIT_ACCOUNTNUMBER_RES));
		resultMap.put(BocInvestControl.REDDATE, viewDateMap.get(BocInvestControl.REDDATE));
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.BOCINVT_BUYRESULT_MAP, resultMap);
		// 进入交易成功页面
		Intent intent = new Intent(this, BuyProductSuccessActivity.class);
		startActivityForResult(intent, ACTIVITY_BUY_CODE);
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
