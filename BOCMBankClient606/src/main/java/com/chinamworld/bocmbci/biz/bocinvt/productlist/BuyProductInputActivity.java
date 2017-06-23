package com.chinamworld.bocmbci.biz.bocinvt.productlist;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
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
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.DateUtils;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 购买产品输入信息页面
 * 
 * @author wangmengmeng
 * 
 */
@SuppressLint("SimpleDateFormat")
public class BuyProductInputActivity extends BociBaseActivity implements
		OnCheckedChangeListener {
	private static final String TAG = "BuyProductInputActivity";
	/** 购买产品输入信息页面 */
	private View view;
	// 非周期性产品初始////////////////////////
	/** 理财交易账户 */
	private TextView tv_prodNum;
	/** 理财产品名称 */
	private TextView tv_prodName;
	/** 产品币种 */
	private TextView tv_curCode;
	/** 认购起点金额 */
	private TextView tv_buyStartingAmount;
	/** 追加认申购起点金额 */
	private TextView tv_appendStartingAmount;
	/** 产品销售期 */
//	private TextView tv_sellingDate;
	/** 产品成立日 */
//	private TextView tv_aprodBegin;
	/** 产品到期日 */
//	private TextView tv_prodEnd;
	/** 产品详情列表 */
	private Map<String, Object> detailMap;
	private Map<String, Object> accound_map;
//	private Map<String, Object> chooseMap;
	/** 产品购买初始化列表 */
	private Map<String, Object> buyInitMap;
	/** 周期性产品签约初始化信息 */
//	private Map<String, Object> signInitMap;
	private String cashRemit;
	/** 非周期性下一步按钮 */
	private Button btn_next_submit;
	/** 非周期性上一步按钮 */
	private Button btn_pre_submit;
	/** 投资期数 */
	private View layout_tzqs;
	/** tokenid标志 */
	private String tokenId;
	/** 购买金额 */
	private EditText et_buyprice_submit;
	/** 柜员营销代码 */
	private EditText et_martCode_submit;
	/** 追加认申购起点金额 */
	private TextView tv_appending;

	// 周期性产品初始//////////////////////////////////////////
	/** 理财交易账户 */
//	private TextView tv_accountNumber_contract;
//	/** 理财产品名称 */
//	private TextView tv_prodName_contract;
//	/** 产品系列名称 */
//	private TextView tv_serialName_contract;
//	/** 产品币种 */
//	private TextView tv_curCode_contract;
//	/** 最大可购买期数 */
//	private TextView tv_remainCycleCount_contract;
//	/** 购买期数 */
//	private EditText et_totalPeriod_contract;
//	/** 基础金额模式 */
//	private RadioGroup rg_amounttype;
//	/** 基础金额模式 */
//	private String amountType;
//	/** 定额 */
//	private RadioButton rb_dinge;
//	/** 不定额 */
//	private RadioButton rb_budinge;
//	/** 基础金额 */
//	private EditText et_baseAmount_contract;

	/** 最低预留金额 */
//	private EditText et_minAmount_contract;
//	/** 最大扣款额金额 */
//	private EditText et_maxAmount_contract;
//	/** 基础金额布局 */
//	private LinearLayout ll_amountType_base;
//	/** 最低预留金额布局 */
//	private LinearLayout ll_amountType_min;
//	/** 最大扣款额金额布局 */
//	private LinearLayout ll_amountType_max;
//	/** 周期性下一步按钮 */
//	private Button btn_next;
//	/** 周期性上一步按钮 */
//	private Button btn_pre;
	// 公共可用/////////////////////////////////////
	/** 钞汇标志—人民币 */
	private TextView rmb_submit;
//	/** 钞汇选择 */
	private RadioGroup rg_cashRemit;
	/** 现钞 */
	private RadioButton rb_bill;
	/** 现汇 */
//	private RadioButton rb_remit;
	/** 购买预交易返回 */
	private Map<String, Object> resultMap;
	/** 基础金额模式 */
//	private TextView tv_amountTypeCode;
//	/** 是否是周期性连续 */
//	private String isPeriodContinue;
	/** 账户信息 */
//	private Map<String, Object> accountSignInitMap = new HashMap<String, Object>();
	/** 用户是否指定赎回日期 ,true/用户指定了赎回日期(需要上送参数)、false/用户不指定赎回日期(不上送参数)*/

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

	@SuppressWarnings("unchecked")
	private void init() {
//		chooseMap = BociDataCenter.getInstance().getChoosemap();
		accound_map=BocInvestControl.accound_map;
		detailMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_PRODUCTDETAIL_LIST);
		buyInitMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_BUYINIT_MAP);
//		if (detailMap.get(BocInvt.BOCI_DETAILPERIODICAL_RES).equals("1")) {
//			// 周期性产品
//			initContractView();
//		} else {
			// 如果是非周期性产品
//		P603购买输入  界面
			initBuyProductView();
//		}

	}

	// 初始化周期性产品界面
//	public void initContractView() {
//		signInitMap = (Map<String, Object>) BaseDroidApp.getInstanse()
//				.getBizDataMap().get(ConstantGloble.BOCINVT_SIGNINIT_MAP);
//		// 添加布局
//		view = addView(R.layout.bocinvt_contractprod_submit_activity);
//		// 步骤条
//		StepTitleUtils.getInstance().initTitldStep(
//				this,
//				new String[] {
//						this.getResources().getString(
//								R.string.bocinvt_buy_step1),
//						this.getResources().getString(
//								R.string.bocinvt_buy_step2),
//						this.getResources().getString(
//								R.string.bocinvt_buy_step3) });
//		StepTitleUtils.getInstance().setTitleStep(2);
//		tv_amountTypeCode = (TextView) view
//				.findViewById(R.id.tv_amountTypeCode);
//
//		tv_accountNumber_contract = (TextView) view
//				.findViewById(R.id.tv_accountNumber_contract);
//		tv_prodName_contract = (TextView) view
//				.findViewById(R.id.tv_prodName_contract);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(
//				BaseDroidApp.getInstanse().getCurrentAct(),
//				tv_prodName_contract);
//		TextView tv_remainCycleCount_pre = (TextView) view
//				.findViewById(R.id.tv_remainCycleCount_pre);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
//				tv_remainCycleCount_pre);
//		tv_serialName_contract = (TextView) view
//				.findViewById(R.id.tv_serialName_contract);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(
//				BaseDroidApp.getInstanse().getCurrentAct(),
//				tv_serialName_contract);
//		tv_curCode_contract = (TextView) view
//				.findViewById(R.id.tv_curCode_contract);
//		tv_remainCycleCount_contract = (TextView) view
//				.findViewById(R.id.tv_remainCycleCount_contract);
//		amountType = LocalData.orderTimeMap.get(0);
//		// 赋值
//		accountSignInitMap = (Map<String, Object>) signInitMap
//				.get(BocInvt.BOCINVT_SIGNINIT_ACCOUNTINFO_RES);
//		String prodNum = String.valueOf(accountSignInitMap
//				.get(BocInvt.BOCINVT_BUYINIT_ACCOUNTNUMBER_RES));
//		tv_accountNumber_contract.setText(StringUtil
//				.getForSixForString(prodNum));
//		tv_prodName_contract.setText(String.valueOf(signInitMap
//				.get(BocInvt.BOCINVT_SIGNINIT_PRODUCTNAME_RES)));
//		tv_serialName_contract.setText(String.valueOf(signInitMap
//				.get(BocInvt.BOCINVT_SIGNINIT_SERIALNAME_RES)));
//		final String curcode = String.valueOf(signInitMap
//				.get(BocInvt.BOCINVT_SIGNINIT_CURCODE_RES));
//		tv_curCode_contract.setText(LocalData.Currency.get(curcode));
//		// 最大可购买期数
//		final String remaincycle = (String) signInitMap
//				.get(BocInvt.BOCINVT_SIGNINIT_REMAINCYCLECOUNT_RES);
//		tv_remainCycleCount_contract.setText(remaincycle);
//		ll_amountType_base = (LinearLayout) view
//				.findViewById(R.id.amountType_base);
//		ll_amountType_min = (LinearLayout) view
//				.findViewById(R.id.amountType_min);
//		ll_amountType_max = (LinearLayout) view
//				.findViewById(R.id.amountType_max);
//		// 输入项
//		et_totalPeriod_contract = (EditText) view
//				.findViewById(R.id.et_totalPeriod_contract);
//		rg_amounttype = (RadioGroup) view.findViewById(R.id.rg_amounttype);
//		rb_dinge = (RadioButton) view.findViewById(R.id.rb_dinge);
//		rb_budinge = (RadioButton) view.findViewById(R.id.rb_budinge);
//		et_baseAmount_contract = (EditText) view
//				.findViewById(R.id.et_baseAmount_contract);
//		et_minAmount_contract = (EditText) view
//				.findViewById(R.id.et_minAmount_contract);
//		et_maxAmount_contract = (EditText) view
//				.findViewById(R.id.et_maxAmount_contract);
//
//		rg_cashRemit = (RadioGroup) view
//				.findViewById(R.id.rg_cashRemit_contract);
//		rb_bill = (RadioButton) view.findViewById(R.id.bill_contract);
//		rb_remit = (RadioButton) view.findViewById(R.id.remit_contract);
//
//		// 判断币种
//		if (!StringUtil.isNull(String.valueOf(detailMap
//				.get(BocInvt.BOCI_DETAILCURCODE_RES)))) {
//			if ((String.valueOf(detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES)))
//					.equalsIgnoreCase(ConstantGloble.BOCINVT_CURRENCY_RMB)) {
//				// 是人民币
//				findViewById(R.id.rmb_submit).setVisibility(View.GONE);
//				cashRemit = LocalData.cashMapValueList.get(0);
//				rg_cashRemit.setVisibility(View.GONE);
//			} else {
//				// 其它的
//				findViewById(R.id.rmb_submit).setVisibility(View.VISIBLE);
//				rg_cashRemit.setVisibility(View.VISIBLE);
//				rg_cashRemit.setOnCheckedChangeListener(this);
//				rb_bill.setChecked(true);
//			}
//		}
//		checkCurrency(
//				String.valueOf(detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES)),
//				et_baseAmount_contract);
//		checkCurrency(
//				String.valueOf(detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES)),
//				et_maxAmount_contract);
//		checkCurrency(
//				String.valueOf(detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES)),
//				et_minAmount_contract);
//		isPeriodContinue = (String) signInitMap
//				.get(BocInvt.BOC_MODIFY_PERIODCONTINUE_RES);
//		// 判断是周期连续则基础金额模式不可选择，只可以是定额
//		if (!StringUtil.isNull(isPeriodContinue)) {
//			if (isPeriodContinue.equals(investTypeSubList.get(0))) {
//				// 不连续
//				tv_amountTypeCode.setVisibility(View.GONE);
//				rg_amounttype.setVisibility(View.VISIBLE);
//				rg_amounttype.setOnCheckedChangeListener(this);
//				rb_dinge.setChecked(true);
//			} else {
//				// 连续产品,只可以选择定额
//				tv_amountTypeCode.setVisibility(View.VISIBLE);
//				rg_amounttype.setVisibility(View.GONE);
//				// 定额
//				amountType = LocalData.orderTimeMap.get(0);
//				ll_amountType_base.setVisibility(View.VISIBLE);
//				ll_amountType_min.setVisibility(View.GONE);
//				ll_amountType_max.setVisibility(View.GONE);
//
//			}
//
//		}
//		btn_pre = (Button) view.findViewById(R.id.btn_conpre_submit);
//		btn_next = (Button) view.findViewById(R.id.btn_connext_submit);
//		btn_pre.setOnClickListener(truePreClick);
//		btn_next.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// 判断
//				if (StringUtil.isNull(et_totalPeriod_contract.getText()
//						.toString().trim())) {
//					BaseDroidApp
//							.getInstanse()
//							.showInfoMessageDialog(
//									BuyProductInputActivity.this
//											.getString(R.string.bocinvt_error_buytotalPeriod));
//					return;
//				}
//				// 以下为验证
//				// 购买期数
//				RegexpBean rebbuy = new RegexpBean(BuyProductInputActivity.this
//						.getString(R.string.totalPeriod_null),
//						et_totalPeriod_contract.getText().toString().trim(),
//						"buyPeriod");
//				// 基础金额
//				RegexpBean reb = checkJapReg(curcode,
//						BuyProductInputActivity.this
//								.getString(R.string.bocinvt_baseAmount_regex),
//						et_baseAmount_contract.getText().toString().trim());
//				// 最低预留金额
//				RegexpBean reb1 = checkJapReg(curcode,
//						BuyProductInputActivity.this
//								.getString(R.string.bocinvt_minAmount_regex),
//						et_minAmount_contract.getText().toString().trim());
//				// 最大扣款额金额
//				RegexpBean reb2 = checkJapReg(curcode,
//						BuyProductInputActivity.this
//								.getString(R.string.bocinvt_maxAmount_regex),
//						et_maxAmount_contract.getText().toString().trim());
//				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
//				lists.add(rebbuy);
//				if (rg_amounttype.isShown()) {
//					if (rb_dinge.isChecked()) {
//						lists.add(reb);
//					} else {
//						lists.add(reb1);
//						lists.add(reb2);
//					}
//				} else {
//					lists.add(reb);
//				}
//				if (RegexpUtils.regexpDate(lists)) {// 校验通过
//					// 最大可购买期数
//					double remain = Double.valueOf(remaincycle);
//					// 购买期数
//					double et_remain = Double.valueOf(et_totalPeriod_contract
//							.getText().toString().trim());
//					if (remain < et_remain) {
//						BaseDroidApp
//								.getInstanse()
//								.showInfoMessageDialog(
//										BuyProductInputActivity.this
//												.getString(R.string.bocinvt_error_remainbuy));
//						return;
//					}
//					requestPeriodPsnXpadQueryRiskMatch();
//				}
//			}
//		});
//
//	}

	// 初始化非周期性产品界面
//	P603购买输入  界面
	public void initBuyProductView() {

		// 添加布局
		view = addView(R.layout.bocinvt_buyproduct_submit_activity);
		// 步骤条
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources().getString(
								R.string.bocinvt_buy_step1),
						this.getResources().getString(
								R.string.bocinvt_buy_step2),
						this.getResources().getString(
								R.string.bocinvt_buy_step3) });
		StepTitleUtils.getInstance().setTitleStep(2);
		layout_appdatered = findViewById(R.id.layout_appdatered);
		tv_sy = (TextView) findViewById(R.id.tv_sy);
		tv_limit = (TextView) findViewById(R.id.tv_limit);
		layout_pro_limit =  findViewById(R.id.layout_pro_limit);
		tv_product_code = (TextView) findViewById(R.id.tv_product_code);
		tv_base_money = (TextView) findViewById(R.id.tv_base_money);
		tv_can_cancel = (TextView) findViewById(R.id.tv_can_cancel);
		sp_buy_back_date = (TextView) findViewById(R.id.sp_buy_back_date);
		et_buy_lim = (EditText) findViewById(R.id.et_buy_lim);
		tv_prodNum = (TextView) view.findViewById(R.id.tv_prodNum_submit);
		tv_prodName = (TextView) view.findViewById(R.id.tv_prodName_submit);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				BaseDroidApp.getInstanse().getCurrentAct(), tv_prodName);
		tv_curCode = (TextView) view.findViewById(R.id.tv_curCode_submit);
		tv_buyStartingAmount = (TextView) view
				.findViewById(R.id.tv_buyStartingAmount_submit);
		tv_appendStartingAmount = (TextView) view
				.findViewById(R.id.tv_appendStartingAmount_submit);
//		tv_sellingDate = (TextView) view
//				.findViewById(R.id.tv_sellingDate_submit);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
//				tv_sellingDate);
//		tv_aprodBegin = (TextView) view.findViewById(R.id.tv_aprodBegin_submit);
//		tv_prodEnd = (TextView) view.findViewById(R.id.tv_prodEnd_submit);
		et_buyprice_submit = (EditText) view.findViewById(R.id.et_buyprice_submit);
		et_martCode_submit = (EditText) view.findViewById(R.id.et_martCode_submit);
		tv_appending = (TextView) view.findViewById(R.id.appendStrating);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,tv_appending);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,(TextView)findViewById(R.id.tv_1));
		EditTextUtils.setLengthMatcher(this, et_martCode_submit, 20);
		btn_next_submit = (Button) view.findViewById(R.id.btn_next_submit);
		btn_pre_submit = (Button) view.findViewById(R.id.btn_pre_submit);
		layout_tzqs = view.findViewById(R.id.layout_tzqs);
		layout_date = findViewById(R.id.layout_date);
		tv_tip_message = (TextView) findViewById(R.id.tv_tip_message);
		rg_date = (RadioGroup) findViewById(R.id.rg_date);
		tv_message = (TextView) findViewById(R.id.tv_message);
		rmb_submit = (TextView) findViewById(R.id.rmb_submit);
		layout_profit = findViewById(R.id.layout_profit);
//		sp_1 = (Spinner) findViewById(R.id.sp_1);
		// 赋值操作
		if (detailMap.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTTYPE_RES).toString().equals("2")) {//净值产品
			layout_profit.setVisibility(View.GONE);
		}else {
			layout_profit.setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.tv_profit)).setText(BocInvestControl.getYearlyRR(detailMap,BocInvt.BOCI_YEARLYRR_RES,BocInvestControl.RATEDETAIL));
		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView)findViewById(R.id.tv_sfzdrqsh));
		setMessage();
		isVisibilityView(layout_tzqs);
//		((TextView) findViewById(R.id.tv_profit)).setText((String)detailMap.get(BocInvt.BOCI_YEARLYRR_RES));
//		if (detailMap.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTKIND_RES).toString().equals("0")/*0:结构性理财产品1:类基金理财产品*/
//				&&!detailMap.get("isLockPeriod").toString().equals("0")/*业绩基准型产品*/) {//2016/3/22业务确认，不增加"单日"字样
//				((TextView)findViewById(R.id.tv_1)).setText("(单日)"+getResources().getString(R.string.bocinvt_subamount));
//				((TextView)findViewById(R.id.appendStrating)).setText("(单日)"+getResources().getString(R.string.bocinvt_tv_34));
//		}
		
		if (((String)detailMap.get(BocInvt.BOCI_PRODUCTTYPE_REQ)).equals(BocInvestControl.map_issueType.get(BocInvestControl.STR_JINGZHI))) {//交易手续费(净值型产品独有)
			findViewById(R.id.layout_poundage).setVisibility(View.VISIBLE);
			if (!StringUtil.isNullOrEmpty(detailMap.get(BocInvestControl.TRANSTYPECODE))) {
				switch (Integer.parseInt(detailMap.get(BocInvestControl.TRANSTYPECODE).toString())) {
				case 0:{//认购
					((TextView)findViewById(R.id.tv_s_r_sxf)).setText("认购手续费：");
					((TextView)findViewById(R.id.tv_poundage)).setText(getNull(detailMap.get(BocInvestControl.SUBSCRIBEFEE)));
				}break;
				case 1:{//申购
					((TextView)findViewById(R.id.tv_s_r_sxf)).setText("申购手续费：");
					((TextView)findViewById(R.id.tv_poundage)).setText(getNull(detailMap.get(BocInvestControl.PURCHFEE)));
				}break;
				default:
					break;
				}
			}
		}else {
			findViewById(R.id.layout_poundage).setVisibility(View.GONE);
		}
		EditTextUtils.relateNumInputToChineseShower(et_buyprice_submit,(TextView)findViewById(R.id.money_chinese));//设置  金额中文回显
//		if (Double.parseDouble(detailMap.get(BocInvestControl.AVAILAMT).toString())>(double)10000000) {
//			tv_sy.setText("大于1000万");
//		}else {
			tv_sy.setText(StringUtil.parseStringCodePattern(detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES).toString(), 
					detailMap.get(BocInvestControl.AVAILAMT).toString(), 2));
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_sy);
//		}
		if (detailMap.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTTYPE_RES).toString().equals("2")) {//净值型产品
			layout_pro_limit.setVisibility(View.GONE);
		}else {
			layout_pro_limit.setVisibility(View.VISIBLE);
				switch (Integer.parseInt(detailMap.get("isLockPeriod").toString())) {
				case 0:{//0：非业绩基准产品
					if (detailMap.get("productTermType").toString().equals("3")) {//产品期限特性,3：无限开放式
						tv_limit.setText("无固定期限");
					}else {
						tv_limit.setText(String.valueOf(detailMap.get(BocInvt.BOCI_PRODTIMELIMIT_RES)) + "天");
					}
				}break;
				case 1:{//1：业绩基准-锁定期转低收益 
					tv_limit.setText("最低持有"+String.valueOf(detailMap.get(BocInvt.BOCI_PRODTIMELIMIT_RES)) + "天");
				}break;
				default:{//2：业绩基准-锁定期后入账,  3：业绩基准-锁定期周期滚续
					tv_limit.setText(String.valueOf(detailMap.get(BocInvt.BOCI_PRODTIMELIMIT_RES)) + "天");
				}break;
				}
		}
		tv_product_code.setText((String)detailMap.get(BocInvt.BOCINVT_BUYRES_PRODCODE_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,tv_product_code);
		if (detailMap.get("appdatered").toString().equals("1")) {//是否允许指定日期赎回,0：否1：是
			sp_buy_back_date.setText(getSystemDate());
			layout_date.setVisibility(View.VISIBLE);
			layout_appdatered.setVisibility(View.VISIBLE);
			tv_tip_message.setVisibility(View.VISIBLE);
		}else {
			layout_date.setVisibility(View.GONE);
			layout_appdatered.setVisibility(View.GONE);
			tv_tip_message.setVisibility(View.GONE);
		}
		rg_date.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_date_yes:{
					layout_appdatered.setVisibility(View.VISIBLE);
					tv_tip_message.setVisibility(View.VISIBLE);
				}break;
				case R.id.rb_date_no:{
					layout_appdatered.setVisibility(View.GONE);
					tv_tip_message.setVisibility(View.GONE);
				}break;
				default:
					break;
				}
			}
		});
		tv_base_money.setText(StringUtil.parseStringCodePattern(
				(String)detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES),(String)detailMap.get(BocInvt.BOCINVT_SIGNRESULT_BASEAMOUNT_REQ),2));
		tv_can_cancel.setText(BocInvestControl.map_isCanCancle.get((String)detailMap.get(BocInvt.ISCANCANCLE)));
		String prodNum="";
//		if (getIntent()!=null&&getIntent().getBooleanExtra("isFlag", false)) {//true/从其他持仓模块跳转过来的
//			prodNum=String.valueOf(buyInitMap.get("bancAccount"));
//		}else {
			prodNum = String.valueOf(buyInitMap.get(BocInvt.ACCOUNTNO));
//		}
		tv_prodNum.setText(StringUtil.getForSixForString(prodNum));
		tv_prodName.setText(String.valueOf(detailMap
				.get(BocInvt.BOCI_DETAILPRODNAME_RES)));
		String curcode = String.valueOf(detailMap
				.get(BocInvt.BOCI_DETAILCURCODE_RES));
		tv_curCode.setText(LocalData.Currency.get(curcode));

		String buyStarting = (String) detailMap
				.get(BocInvt.SUBAMOUNT);
		tv_buyStartingAmount.setText(StringUtil.parseStringCodePattern(curcode,
				buyStarting, 2));
		String appendStarting = (String) detailMap
				.get(BocInvt.ADDAMOUNT);
		tv_appendStartingAmount.setText(StringUtil.parseStringCodePattern(
				curcode, appendStarting, 2));
//		tv_sellingDate.setText(String.valueOf(detailMap
//				.get(BocInvt.BOCI_DETAILSELLINGSTARTINGDATE_RES))
//				+ ConstantGloble.BOCINVT_DATE_ADD
//				+ String.valueOf(detailMap
//						.get(BocInvt.BOCI_DETAILSELLINGENDINGDATE_RES)));
//		tv_aprodBegin.setText(String.valueOf(detailMap
//				.get(BocInvt.BOCI_DETAILPRODBEGIN_RES)));
//		tv_prodEnd.setText(String.valueOf(detailMap
//				.get(BocInvt.BOCI_DETAILPRODEND_RES)));
		rg_cashRemit = (RadioGroup) view.findViewById(R.id.rg_cashRemit);
		rb_bill = (RadioButton) view.findViewById(R.id.bill);
//		rb_remit = (RadioButton) view.findViewById(R.id.remit);
		// 判断币种
		if (!StringUtil.isNull(String.valueOf(detailMap
				.get(BocInvt.BOCI_DETAILCURCODE_RES)))) {
			if ((String.valueOf(detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES)))
					.equalsIgnoreCase(ConstantGloble.BOCINVT_CURRENCY_RMB)) {
				// 是人民币
//				findViewById(R.id.buy_cashremit_layout).setVisibility(View.GONE);
				rmb_submit.setVisibility(View.VISIBLE);
//				sp_1.setVisibility(View.GONE);
				rg_cashRemit.setVisibility(View.GONE);
//				cashRemit = LocalData.cashMapValueList.get(0);
			} else {
				// 其它的
//				findViewById(R.id.buy_cashremit_layout).setVisibility(View.VISIBLE);
				rmb_submit.setVisibility(View.GONE);
//				sp_1.setVisibility(View.VISIBLE);
//				setAdapterForSpinner(sp_1);
				rg_cashRemit.setVisibility(View.VISIBLE);
				rg_cashRemit.setOnCheckedChangeListener(this);
				rb_bill.setChecked(true);
			}
		}

		checkCurrency(curcode, et_buyprice_submit);
		btn_next_submit.setOnClickListener(trueNextClick);
		btn_pre_submit.setOnClickListener(truePreClick);
		sp_buy_back_date.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				TextView tv=(TextView) v;
				String tv_date = tv.getText().toString().trim();
				if (StringUtil.isNullOrEmpty(tv_date)) {
					tv_date=getSystemDate();
				}
				int startYear = Integer.parseInt(tv_date.substring(0, 4));
				int startMonthOfYear = Integer.parseInt(tv_date.substring(5, 7));
				int startDayOfMonth = Integer.parseInt(tv_date.substring(8, 10));
				DatePickerDialog datePickerDialog = new DatePickerDialog(BuyProductInputActivity.this, new OnDateSetListener(){
					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						StringBuilder date = new StringBuilder();
						date.append(String.valueOf(year));
						date.append("/");
						int month = monthOfYear + 1;
						date.append(((month < 10) ? ("0" + month)
								: (month + "")));
						date.append("/");
						date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth)
								: (dayOfMonth + "")));
//						((TextView) v).setText(date.toString());
						setDateForTextView((TextView) v,date.toString());
					}
					
				},startYear, startMonthOfYear-1, startDayOfMonth);
				datePickerDialog.show();
			}
		});
	}
	/**
	 * 判断选中的日期是否符合要求，并反显
	 * @param tv 要反显日期的TextView
	 * @param date 当前用户选中要设置的日期，格式：2000/08/08
	 */
	private void setDateForTextView(TextView tv,String date) {
		String str_sellType=(String)detailMap.get("sellType");//赎回开放规则,00：不允许赎回01：开放期赎回02：付息日赎回03：起息后每日赎回04：周期开放赎回
		DateFormat dFormat=new SimpleDateFormat("yyyy/MM/dd");
		if (((String)detailMap.get("appdatered")).equals("1")&&!str_sellType.equals("00")) {//是否允许指定日期赎回,0：否1：是   &&  00：不允许赎回
			if (str_sellType.equals("01")||str_sellType.equals("02")||str_sellType.equals("03")) {
					try {
						Date date_start = dFormat.parse(detailMap.get("redEmptionStartDate").toString());
						Date date_end = dFormat.parse(detailMap.get("redEmptionEndDate").toString());
						Date date_current = dFormat.parse(date);
						if (date_current.before(date_end)&&date_current.after(date_start)) {
							tv.setText(date);
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
			}else if (str_sellType.equals("04")) {
				try {
					Date date_current = dFormat.parse(date);
					Date date_system_plus_1 = dFormat.parse(getSystemDate());
					Calendar cd = Calendar.getInstance();
					cd.setTime(date_system_plus_1);
					cd.add(Calendar.YEAR, 1);
					Date date_year_plus_1 = cd.getTime();
					if (!date_current.before(date_system_plus_1)&&date_current.before(date_year_plus_1)) {
						tv.setText(date);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
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
	
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	private void setAdapterForSpinner(Spinner spinner){
//		// 钞/汇
//				ArrayAdapter<ArrayList<String>> sp_adapter = new ArrayAdapter(this,
//						R.layout.custom_spinner_item, BocInvestControl.list_cashRemit);
//				sp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//				spinner.setAdapter(sp_adapter);
//				spinner.setSelection(0);
//	}
	
	/**
	 * 判断是否要显示投资期数
	 */
	private void isVisibilityView(View view){
		if (!detailMap.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTTYPE_RES).toString().equals("2")) {//非净值型产品
			switch (Integer.parseInt(detailMap.get("isLockPeriod").toString())) {
			case 0://非业绩基准产品
			{
				isVisibilityView1(view);
			}
			break;
			case 1://业绩基准-锁定期转低收益 
			break;
			case 2://业绩基准-锁定期后/入账
			break;
			case 3://业绩基准-锁定期周期滚续
			{
				view.setVisibility(View.VISIBLE);
			}
			break;
			default:
				break;
			}
		}else {
			isVisibilityView1(view);
		}
	}
	/**
	 * 判断是否要显示投资期数
	 */
	private void isVisibilityView1(View view){
		switch (Integer.parseInt(detailMap.get("progressionflag").toString())) {
		case 0://不是收益累进产品
		{
			switch (Integer.parseInt(detailMap.get("productTermType").toString())) {
			case 0:
				break;
			case 1:
				break;
			case 2://对应表内周期续约产品的收益试算 
			{
				view.setVisibility(View.VISIBLE);
			}
			break;
			case 3://对应日积月累产品的收益试算
			break;
			case 4:
				break;
			default://对应固定期限产品的收益试算
			break;
			}
		}
		break;
		case 1://是收益累进产品
		break;
		default:
			break;
		}
	}
	
	private String getSystemDate(){
		String sub = BocInvestControl.SYSTEM_DATE.substring(0, 10);
		Calendar cd = Calendar.getInstance();
		DateFormat df=new SimpleDateFormat("yyyy/MM/dd");
		String str_sellType=(String)detailMap.get("sellType");//赎回开放规则,00：不允许赎回01：开放期赎回02：付息日赎回03：起息后每日赎回04：周期开放赎回
		if (((String)detailMap.get("appdatered")).equals("1")&&!str_sellType.equals("00")) {//是否允许指定日期赎回,0：否1：是   &&  00：不允许赎回
			if (str_sellType.equals("01")||str_sellType.equals("02")||str_sellType.equals("03")) {
				try {
//					cd.setTime(df.parse(detailMap.get("redEmptionStartDate").toString()));
					cd.setTime(df.parse(sub));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				cd.add(Calendar.DAY_OF_MONTH, 1);
				return df.format(cd.getTime());
			}
		}
		
		try {
			cd.setTime(df.parse(sub));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cd.add(Calendar.DAY_OF_MONTH, 1);
		return df.format(cd.getTime());
	}

	/** 非周期性下一步按钮 */
	OnClickListener trueNextClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String curcode = String.valueOf(detailMap
					.get(BocInvt.BOCI_DETAILCURCODE_RES));
			// 购买金额
			RegexpBean reb1 = checkJapReg(curcode,
					BuyProductInputActivity.this
							.getString(R.string.bocinvt_buyAmount_regex),
					et_buyprice_submit.getText().toString().trim());
			// 柜员营销代码
			RegexpBean reb2 = new RegexpBean(
					BuyProductInputActivity.this
							.getString(R.string.bocinvt_marcode_regex),
					et_martCode_submit.getText().toString().trim(),
					"xpadMartCode");
			RegexpBean reg = null;
//			RegexpBean reg = new RegexpBean("投资期数", et_buy_lim.getText().toString().trim(), "buyTerm");
			if("1".equals((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.IsFromAgreeApply))){
				reg = new RegexpBean("投资期数", et_buy_lim.getText().toString().trim(), "buyTerm");
			}else{
				reg = new RegexpBean("投资期数", et_buy_lim.getText().toString().trim(), "sixNumber");
			}
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb1);
			lists.add(reg);

			if (StringUtil.isNull(et_martCode_submit.getText().toString().trim())) {

			} else {
				lists.add(reb2);
			}

			if (RegexpUtils.regexpDate(lists)) {// 校验通过
					requestCommConversationId();
					BaseHttpEngine.showProgressDialog();
			}
		}
	};
	/**
	 * 设置指定赎回日期提示信息
	 */
	private void setMessage(){
		String str_sellType=(String)detailMap.get("sellType");//赎回开放规则,00：不允许赎回01：开放期赎回02：付息日赎回03：起息后每日赎回04：周期开放赎回
		if (((String)detailMap.get("appdatered")).equals("1")&&!str_sellType.equals("00")) {//是否允许指定日期赎回,0：否1：是   &&  00：不允许赎回
			tv_message.setVisibility(View.VISIBLE);
			if (str_sellType.equals("01")||str_sellType.equals("02")||str_sellType.equals("03")) {
				tv_message.setText("赎回开放日期为"+DateUtils.formatStr((String)detailMap.get("redEmptionStartDate"))+"至"+DateUtils.formatStr((String)detailMap.get("redEmptionEndDate")));
			}else if (str_sellType.equals("04")) {
				String str_rStart=(String) detailMap.get("redEmperiodStart");
				String str_rEnd=(String) detailMap.get("redEmperiodEnd");
				String str_rer=(String)detailMap.get("redEmperiodfReq");
				if (str_rer.equalsIgnoreCase("d")) {
					tv_message.setText(str_rStart+"号至"+str_rEnd+"号可赎回");
				}else if (str_rer.equalsIgnoreCase("w")) {
					if (str_rEnd.equals("7")||str_rEnd.equals("七")) {
						tv_message.setText("每周的星期"+str_rStart+"至周日可赎回");
					}else {
						tv_message.setText("每周的星期"+str_rStart+"至星期"+str_rEnd+"可赎回");
					}
				}else if (str_rer.equalsIgnoreCase("m")) {
					tv_message.setText("每月的"+str_rStart+"号至"+str_rEnd+"号可赎回");
				}
			}
		}else {
			tv_message.setVisibility(View.GONE);
		}
	}

	/** 非周期性上一步按钮 */
	OnClickListener truePreClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setResult(RESULT_CANCELED);
			finish();
		}
	};
	/**购买金额基数-即 购买基数*/
	private TextView tv_base_money;
	/**是否允许撤单*/
	private TextView tv_can_cancel;
	/**投资期数*/
	private EditText et_buy_lim;
	/**赎回日期*/
	private TextView sp_buy_back_date;
	/**产品代码*/
	private TextView tv_product_code;
	/**产品期限*/
	private TextView tv_limit;
	private View layout_pro_limit;
	/**剩余额度*/
	private TextView tv_sy;
	private View layout_appdatered;
	private View layout_date;
	private TextView tv_tip_message;
	private RadioGroup rg_date;
	private TextView tv_message;
	private View layout_profit;
//	private Spinner sp_1;

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.bill:
		case R.id.bill_contract:
			// 现钞
			cashRemit = LocalData.cashMapValueList.get(1);
			break;
		case R.id.remit:
		case R.id.remit_contract:
			// 现汇
			cashRemit = LocalData.cashMapValueList.get(2);
			break;
//		case R.id.rb_dinge:
//			// 定额
//			amountType = LocalData.orderTimeMap.get(0);
//			ll_amountType_base.setVisibility(View.VISIBLE);
//			ll_amountType_min.setVisibility(View.GONE);
//			ll_amountType_max.setVisibility(View.GONE);
//			break;
//		case R.id.rb_budinge:
//			// 不定额
//			amountType = LocalData.orderTimeMap.get(1);
//			ll_amountType_base.setVisibility(View.GONE);
//			ll_amountType_min.setVisibility(View.VISIBLE);
//			ll_amountType_max.setVisibility(View.VISIBLE);
//			break;
		default:
			break;
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
//		if (getIntent()!=null&&getIntent().getBooleanExtra("isFlag", false)) {//从别的模块跳转过来(持仓模块)
//			Map<String, Object> parms_map=new HashMap<String, Object>();
//			parms_map.put("xpadAccountSatus", "1");
//			parms_map.put("queryType", "1");
//			getHttpTools().requestHttp(BocInvestControl.PSNXPADACCOUNTQUERY, "requestPsnXpadAccountQueryCallBack", parms_map, true);
//		}else {//本模块
		pSNGetTokenId();
//		}
	}
	/**
	 * 请求 查询客户理财账户信息 回调
	 */
//	@SuppressWarnings({ "static-access", "unchecked" })
//	public void requestPsnXpadAccountQueryCallBack(Object resultObj){
//		if (StringUtil.isNullOrEmpty(resultObj)) {
//			BaseHttpEngine.dissMissProgressDialog();
//			return;
//		}
//		Map<String, Object> result_map=getHttpTools().getResponseResult(resultObj);
//		List<Map<String, Object>> result_list = (List<Map<String, Object>>) result_map.get(BocInvt.BOCI_OURLAYLIST_RES);
//		if (StringUtil.isNullOrEmpty(result_list)) {
//			BaseHttpEngine.dissMissProgressDialog();
//			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.bocinvt_tv_75));
//			return;
//		}
//		for (int i = 0; i < result_list.size(); i++) {
//			if (buyInitMap.get("bancAccountKey").toString()
//					.equals(result_list.get(i).get(BocInvt.BOCINVT_CAPACITYQUERY_ACCOUNTKEY_RES).toString())) {
//				if (StringUtil.isNullOrEmpty(result_list.get(i).get(BocInvt.BOCI_ACCOUNTID_REQ))) {//未关联
//					BaseHttpEngine.dissMissProgressDialog();
//					BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.bocinvt_tv_75));
//					return;
//				}else {//已关联
//					pSNGetTokenId();
//					return;
//				}
//			}
//		}
//		BaseHttpEngine.dissMissProgressDialog();
//		BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.bocinvt_tv_75));
//	}

	/**
	 * 获取tokenId
	 */
	public void pSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
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
		tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		// 请求预交易
		requestPsnXpadProductBuyPre(tokenId);
	}

	/** 请求购买预交易 */
	@SuppressWarnings("unchecked")
	public void requestPsnXpadProductBuyPre(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADPRODUCTBUYPRE_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(BocInvt.BOCINVT_BUYPRE_PRODUCTNAME_REQ,detailMap.get(BocInvt.BOCI_DETAILPRODNAME_RES));
		paramsmap.put(BocInvt.BOCINVT_BUYPRE_CURCODE_REQ,detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES));
		paramsmap.put(BocInvt.BOCINVT_BUYPRE_PRODUCTCODE_REQ,detailMap.get(BocInvt.BOCI_DETAILPRODCODE_RES));
		if (rg_cashRemit.isShown()) {
			paramsmap.put(BocInvt.BOCINVT_BUYPRE_XPADCASHREMIT_REQ,
					rb_bill.isChecked()?BocInvestControl.map_xpadCashRemit_code_value.get(BocInvestControl.map_xpadCashRemit_code_key.get("01"))
							:BocInvestControl.map_xpadCashRemit_code_value.get(BocInvestControl.map_xpadCashRemit_code_key.get("02")));
		}else {
			paramsmap.put(BocInvt.BOCINVT_BUYPRE_XPADCASHREMIT_REQ, "00");
		}
		paramsmap.put(BocInvt.BOCINVT_BUYPRE_MARTCODE_REQ, et_martCode_submit.getText().toString().trim());
		paramsmap.put(BocInvt.BOCINVT_BUYPRE_BUYPRICE_REQ, et_buyprice_submit.getText().toString().trim());
		paramsmap.put(BocInvt.BOCINVT_BUYPRE_ISAUTOSER_REQ,ConstantGloble.BOCINVT_NULL_STRING);
		paramsmap.put(BocInvt.BOCINVT_BUYPRE_TOKEN_REQ, tokenId);
		paramsmap.put(Comm.ACCOUNT_ID, ((Map<String, Object>)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BOCINVT_BUYINIT_MAP)).get(Comm.ACCOUNT_ID));
		paramsmap.put(BocInvestControl.PRODUCTKIND, detailMap.get(BocInvestControl.PRODUCTKIND));
		if (layout_appdatered.isShown()) {
			paramsmap.put(BocInvestControl.REDDATE,sp_buy_back_date.getText().toString().trim());
		}else {
			paramsmap.put(BocInvestControl.REDDATE,"");
		}
		if (layout_tzqs.isShown()) {
			paramsmap.put(BocInvestControl.INVESTCYCLE,et_buy_lim.getText().toString().trim());
		}else {
			paramsmap.put(BocInvestControl.INVESTCYCLE,"");
		}
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnXpadProductBuyPreCallBack");
	}

	/** 请求购买预交易回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnXpadProductBuyPreCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		resultMap = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}
		if (rg_cashRemit.isShown()) {
			resultMap.put(BocInvt.BOCINVT_BUYPRE_XPADCASHREMIT_REQ,
					rb_bill.isChecked()?BocInvestControl.map_xpadCashRemit_code_value.get(BocInvestControl.map_xpadCashRemit_code_key.get("01"))
							:BocInvestControl.map_xpadCashRemit_code_value.get(BocInvestControl.map_xpadCashRemit_code_key.get("02")));
		}else {
			resultMap.put(BocInvt.BOCINVT_BUYPRE_XPADCASHREMIT_REQ, "00");
		}
		if (layout_tzqs.isShown()) {
			resultMap.put(BocInvestControl.BOCINVT_INVEST_CYCLE_INPUT, et_buy_lim.getText().toString().trim());
		}
		resultMap.put(ConstantGloble.BOCINVT_BUYPRICE_STRING,et_buyprice_submit.getText().toString().trim());
		resultMap.put(BocInvt.BOCINVT_BUYPRE_MARTCODE_REQ, et_martCode_submit.getText().toString().trim());
		resultMap.put(BocInvt.BOCINVT_BUYRES_CYCLECOUNT_RES, et_buy_lim.getText().toString().trim());
//		resultMap.put(BocInvt.BOCINVT_BUYPRE_PRODUCTNAME_REQ,
//				detailMap.get(BocInvt.BOCI_DETAILPRODNAME_RES));
//		resultMap.put(BocInvt.BOCINVT_BUYPRE_CURCODE_REQ,
//				detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES));
//		resultMap.put(BocInvt.BOCINVT_BUYPRE_PRODUCTCODE_REQ,
//				detailMap.get(BocInvt.BOCI_DETAILPRODCODE_RES));
		resultMap.put(BocInvt.BOCINVT_BUYINIT_ACCOUNTNUMBER_RES,buyInitMap.get(BocInvt.ACCOUNTNO));
		resultMap.put(BocInvestControl.KEY_ISCHECK_RABTN, ((RadioButton)findViewById(R.id.rb_date_yes)).isChecked());
		resultMap.put(BocInvestControl.REDDATE,sp_buy_back_date.getText().toString().trim());
//		resultMap.put(BocInvt.BOCI_DETAILPRODBEGIN_RES,
//				detailMap.get(BocInvt.BOCI_DETAILPRODBEGIN_RES));
//		resultMap.put(BocInvt.BOCI_DETAILPRODEND_RES,
//				detailMap.get(BocInvt.BOCI_DETAILPRODEND_RES));
//		resultMap.put(BocInvt.BOCI_DETAILBUYPRICE_RES,
//				detailMap.get(BocInvt.BOCI_DETAILBUYPRICE_RES));
//		resultMap.put(BocInvt.BOCI_DETAILPRODRISKLVL_RES,
//				detailMap.get(BocInvt.BOCI_DETAILPRODRISKLVL_RES));
		Map<String, String> evaluationMap = (Map<String, String>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BOICNVT_ISBEFORE_RESULT);
		if (StringUtil.isNull(evaluationMap.get(BocInvt.BOCIEVA_RISKLEVEL_RES))) {
			Map<String, String> evaluationResultMap = (Map<String, String>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BOCINVT_EVALUATION_RESULT);
			resultMap.put(BocInvt.BOCINVT_EVA_RISKLEVEL_RES, String
					.valueOf(evaluationResultMap
							.get(BocInvt.BOCINVT_EVA_RISKLEVEL_RES)));

		} else {
			resultMap.put(BocInvt.BOCINVT_EVA_RISKLEVEL_RES,
					evaluationMap.get(BocInvt.BOCIEVA_RISKLEVEL_RES));
		}
		// 拼合字段
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.BOCINVT_CONFIRM_MAP, resultMap);
		// 判断条件是否满足，如果满足——跳转到确认信息页面,否则弹出提示框
		if (Boolean.valueOf((String) resultMap
				.get(BocInvt.BOCINVT_BUYPRE_HASINVESTXP_RES))) {
			BiiHttpEngine.dissMissProgressDialog();
			// 客户无投资经验买了有投资经验
			BaseDroidApp
					.getInstanse()
					.showErrorDialog(
							BuyProductInputActivity.this
									.getString(R.string.bocinvt_error_nohasInvestXp),
							R.string.cancle, R.string.confirm,
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									switch (Integer.parseInt(v.getTag() + "")) {
									case CustomDialog.TAG_SURE:
										// 确定操作 可以进行购买
										BaseDroidApp.getInstanse()
												.dismissErrorDialog();
										// 该产品风险级别与您目前的风险承受能力不匹配，不能购买。请您选择匹配的理财产品。
										requestPsnXpadQueryRiskMatch();

										break;
									case CustomDialog.TAG_CANCLE:
										// 取消操作
										BaseDroidApp.getInstanse()
												.dismissErrorDialog();
										break;
									}
								}
							});
		} else {
			// 该产品风险级别与您目前的风险承受能力不匹配，不能购买。请您选择匹配的理财产品。
			requestPsnXpadQueryRiskMatch();
		}
	}

//	/** 请求查询周期性产品客户风险等级与产品风险等级是否匹配 */
//	public void requestPeriodPsnXpadQueryRiskMatch() {
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADQUERYRISKMATCH_API);
//		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
//				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
//		Map<String, Object> paramsmap = new HashMap<String, Object>();
//		paramsmap.put(BocInvt.BOCINVT_MATCH_SERIALCODE_REQ,
//				signInitMap.get(BocInvt.BOCINVT_SIGNINIT_SERIALCODE_RES));
//		paramsmap.put(Comm.ACCOUNTNUMBER,accountSignInitMap
//				.get(BocInvt.BOCINVT_BUYINIT_ACCOUNTNUMBER_RES));
//		biiRequestBody.setParams(paramsmap);
//		BiiHttpEngine.showProgressDialog();
//		HttpManager.requestBii(biiRequestBody, this,
//				"requestPeriodPsnXpadQueryRiskMatchCallBack");
//	}
//
//	/** 请求查询周期性产品客户风险等级与产品风险等级是否匹配回调 */
//	public void requestPeriodPsnXpadQueryRiskMatchCallBack(Object resultObj) {
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		BiiHttpEngine.dissMissProgressDialog();
//		Map<String, Object> riskMatchMap = (Map<String, Object>) biiResponseBody
//				.getResult();
//		if (StringUtil.isNullOrEmpty(riskMatchMap)) {
//			return;
//		}
//		BociDataCenter.getInstance().setRiskMatchMap(riskMatchMap);
//		String riskMatch = (String) riskMatchMap
//				.get(BocInvt.BOCINVT_MATCH_RISKMATCH_RES);
//		if (riskMatch.equals(MATCH)) {
//			goConfirmActivity();
//		} else if (riskMatch.equals(NOMATCHCAN)) {
//			// 1：不匹配但允许交易
//			// （即客户风险低于产品风险，但系统不强制风险匹配，可提示客户确认风险和交易）
//			BaseDroidApp
//					.getInstanse()
//					.showErrorDialog(
//							BuyProductInputActivity.this
//									.getString(R.string.bocinvt_error_noriskExceed),
//							R.string.cancle, R.string.confirm,
//							new OnClickListener() {
//
//								@Override
//								public void onClick(View v) {
//
//									switch (Integer.parseInt(v.getTag() + "")) {
//									case CustomDialog.TAG_SURE:
//										BaseDroidApp.getInstanse()
//												.dismissErrorDialog();
//										// 确定操作 可以进行签约
//										goConfirmActivity();
//										break;
//									case CustomDialog.TAG_CANCLE:
//										// 取消操作
//										BaseDroidApp.getInstanse()
//												.dismissErrorDialog();
//
//										break;
//									}
//
//								}
//							});
//		} else if (riskMatch.equals(NOMATCH)) {
//			// 2：不匹配且拒绝交易
//			// （即客户风险低于产品风险，且系统强制风险匹配，拒绝交易）
//			BaseDroidApp.getInstanse().showInfoMessageDialog(
//					BuyProductInputActivity.this
//							.getString(R.string.bocinvt_error_cannotBuy));
//			return;
//		}
//	}

//	public void goConfirmActivity() {
//		Intent intent = new Intent(BuyProductInputActivity.this,
//				BuyProductConfirmActivity.class);
//		Bundle bundle = new Bundle();
//		String curcode = (String) signInitMap
//				.get(BocInvt.BOCINVT_SIGNINIT_CURCODE_RES);
//		ArrayList<String> list = new ArrayList<String>();
//		list.add(String.valueOf(accountSignInitMap
//				.get(BocInvt.BOCINVT_BUYINIT_ACCOUNTNUMBER_RES)));
//		list.add(String.valueOf(signInitMap
//				.get(BocInvt.BOCINVT_SIGNINIT_PRODUCTNAME_RES)));
//		list.add(String.valueOf(signInitMap
//				.get(BocInvt.BOCINVT_SIGNINIT_SERIALNAME_RES)));
//		list.add(curcode);
//		list.add(cashRemit);
//		list.add(String.valueOf(signInitMap
//				.get(BocInvt.BOCINVT_SIGNINIT_REMAINCYCLECOUNT_RES)));
//		list.add(et_totalPeriod_contract.getText().toString().trim());
//		list.add(amountType);
//		list.add(et_baseAmount_contract.getText().toString().trim());
//		list.add(et_minAmount_contract.getText().toString().trim());
//		list.add(et_maxAmount_contract.getText().toString().trim());
//		bundle.putStringArrayList(
//				ConstantGloble.BOCINVT_CONTRACT_CONFIRM_EXTRA, list);
//		intent.putExtra(ConstantGloble.BOCINVT_BUNDLE, bundle);
//		startActivityForResult(intent, ACTIVITY_BUY_CODE);
//	}

	/** 请求查询客户风险等级与产品风险等级是否匹配 */
	public void requestPsnXpadQueryRiskMatch() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADQUERYRISKMATCH_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(BocInvt.BOCINVT_MATCH_PRODUCTCODE_REQ,detailMap.get(BocInvt.BOCI_DETAILPRODCODE_RES));
//		paramsmap.put(BocInvt.BOCINVT_MATCH_ACCOUNTKEY_REQ,(String)accound_map.get(BocInvt.BOCINVT_CAPACITYQUERY_ACCOUNTKEY_RES));
		paramsmap.put(BocInvt.BOCINVT_MATCH_ACCOUNTKEY_REQ,(String)buyInitMap.get(BocInvt.BOCINVT_CAPACITYQUERY_ACCOUNTKEY_RES));
//		paramsmap.put(Comm.ACCOUNTNUMBER,buyInitMap.get(BocInvt.ACCOUNTNO));
		biiRequestBody.setParams(paramsmap);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnXpadQueryRiskMatchCallBack");
	}

	/** 请求查询客户风险等级与产品风险等级是否匹配回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnXpadQueryRiskMatchCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		Map<String, Object> riskMatchMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(riskMatchMap)) {
			return;
		}
		BociDataCenter.getInstance().setRiskMatchMap(riskMatchMap);
		String riskMatch = (String) riskMatchMap
				.get(BocInvt.BOCINVT_MATCH_RISKMATCH_RES);
		if (riskMatch.equals(MATCH)) {
			orderTime();
		} else if (riskMatch.equals(NOMATCHCAN)) {
			// 1：不匹配但允许交易
			// （即客户风险低于产品风险，但系统不强制风险匹配，可提示客户确认风险和交易）
			BaseDroidApp
					.getInstanse()
					.showErrorDialog(
							BuyProductInputActivity.this
									.getString(R.string.bocinvt_error_noriskExceed),
							R.string.cancle, R.string.confirm,
							new OnClickListener() {

								@Override
								public void onClick(View v) {

									switch (Integer.parseInt(v.getTag() + "")) {
									case CustomDialog.TAG_SURE:
										BaseDroidApp.getInstanse()
												.dismissErrorDialog();
										// 确定操作 可以进行购买
										orderTime();
										break;
									case CustomDialog.TAG_CANCLE:
										// 取消操作
										BaseDroidApp.getInstanse()
												.dismissErrorDialog();

										break;
									}

								}
							});
		} else if (riskMatch.equals(NOMATCH)) {
			// 2：不匹配且拒绝交易
			// （即客户风险低于产品风险，且系统强制风险匹配，拒绝交易）
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					BuyProductInputActivity.this
							.getString(R.string.bocinvt_error_cannotBuy));
			return;
		}

	}

	/** 判断是否挂单 */
	@SuppressWarnings("unchecked")
	public void orderTime() {
		Map<String, Object> resultMap = (Map<String, Object>) BaseDroidApp
				.getInstanse().getBizDataMap()
				.get(ConstantGloble.BOCINVT_CONFIRM_MAP);
		if (LocalData.orderTimeMap.get(0).equals(
				(String) resultMap.get(BocInvt.BOCINVT_BUYPRE_ORDERTIME_RES))) {
			// 处于挂单时间
			BaseDroidApp
					.getInstanse()
					.showErrorDialog(
							BuyProductInputActivity.this
									.getString(R.string.bocinvt_error_orderTime),
							R.string.cancle, R.string.confirm,
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									switch (Integer.parseInt(v.getTag() + "")) {
									case CustomDialog.TAG_SURE:
										// 确定操作
										// 可以进行购买
										BaseDroidApp.getInstanse()
												.dismissErrorDialog();
										Intent intent = new Intent(
												BuyProductInputActivity.this,
												BuyProductConfirmActivity.class);
										startActivityForResult(intent,
												ACTIVITY_BUY_CODE);
										break;
									case CustomDialog.TAG_CANCLE:
										// 取消操作
										BaseDroidApp.getInstanse()
												.dismissErrorDialog();

										break;
									}
								}
							});
		} else {
			// 进行跳转进入确认信息页面
			Intent intent = new Intent(BuyProductInputActivity.this,
					BuyProductConfirmActivity.class);
			startActivityForResult(intent, ACTIVITY_BUY_CODE);
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
