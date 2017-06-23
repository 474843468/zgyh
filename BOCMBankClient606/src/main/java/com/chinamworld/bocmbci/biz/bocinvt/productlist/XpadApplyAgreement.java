package com.chinamworld.bocmbci.biz.bocinvt.productlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.util.Trans2ChineseNumber;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 投资协议申请页面(定时定额协议、余额理财协议)
 * 
 * @author wangmengmeng
 * 
 */
public class XpadApplyAgreement extends BociBaseActivity implements
		OnCheckedChangeListener {
	/** 协议申请页 */
	private View view;
	/** 理财交易账户 */
	private TextView tv_prodNum;
	/** 产品代码 */
	private TextView tv_prodCode;
	/** 产品名称 */
	private TextView tv_prodName;
	/** 认购起点金额 */
	private TextView tv_buyStartingAmount;
	/** 追加认申购起点金额——后 */
	private TextView tv_appendStartingAmount;
	/** 追加认申购起点金额——前 */
	private TextView tv_appending;
	/** 币种 */
	private TextView tv_curCode;
	/** 钞汇标志—人民币 */
	private TextView rmb_submit;
	/** 钞汇选择 */
	private RadioGroup rg_cashRemit;
	/** 现钞 */
	private RadioButton rb_bill;
	/** 现汇 */
	private RadioButton rb_remit;
	/** 投资方式 */
//	private Spinner sp_investType;
	/** 总期数方式 */
	private RadioGroup rg_period;
	/** 限期 */
	private RadioButton rb_period;
	/** 不限期 */
	private RadioButton rb_noperiod;
	// 限期时显示
	private LinearLayout ll_totalPeriod;
	/** 总期数 */
	private EditText et_totalPeriod;
	// 定时定额显示
	/** 定投类型 */
//	private Spinner sp_timeInvestType;
	private LinearLayout ll_timeInvestType;
	/** 定投金额 */
	private EditText et_redeemAmount;
	private LinearLayout ll_redeemAmount;
	private TextView tv_redeempre;
	/** 定投频率 */
	private EditText et_timeInvestRate;
	private LinearLayout ll_timeInvestRate;
	/** 频率单位 */
	private Spinner sp_timeInvestRate;
	/** 定投开始日 */
	private TextView tv_investTime;
	/** 定投开始日——前 */
	private TextView tv_investTime_pre;
	// 自动投资协议时显示
	/** 最低限额 */
	private EditText et_minAmount;
	private LinearLayout ll_minAmount;
	/** 最高限额 */
	private EditText et_maxAmount;
	private LinearLayout ll_maxAmount;
	/** 产品详情 */
	private Map<String, Object> detailMap;
	/** 下一步 */
	private Button btn_next;
	// 数据
	/** 钞汇 */
	private String cashremit;
	/** 投资方式 */
	private String investType="0";
	/** 定投类型 */
	private String timeInvestType="0";
	/** 定投频率 */
	private String timeInvestRate;
	/** 定投频率单位 */
	private String timeInvestRateFlag;
	/** 总期数方式 */
	private String period;
	/** 定投金额 */
	private String redeemAmount;
	/** 最低限额 */
	private String minAmount;
	/** 最高限额 */
	private String maxAmount;
	private Map<String, Object> acctMap;
	private Map<String, Object> map_listview_choose;
	private String str_agrtype;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.bocinvt_apply_agree_title));
		// 添加布局
		view = addView(R.layout.boc_apply_agree_input);
		// 界面初始化
		init();

	}

	/**
	 * 初始化界面
	 */
	private void init() {
		dateTime = this.getIntent().getStringExtra(ConstantGloble.DATE_TIEM);
		detailMap = BociDataCenter.getInstance().getDetailMap();
		acctMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_BUYINIT_MAP);
		map_listview_choose=BocInvestControl.map_listview_choose;
		str_agrtype = map_listview_choose.get(BocInvestControl.AGRTYPE).toString();
		/** 理财交易账户 */
		tv_prodNum = (TextView) view.findViewById(R.id.tv_number);
		/** 产品代码 */
		tv_prodCode = (TextView) view.findViewById(R.id.tv_prodCode);
		/** 产品名称 */
		tv_prodName = (TextView) view.findViewById(R.id.tv_prodName);
		/** 认购起点金额 */
		tv_buyStartingAmount = (TextView) view
				.findViewById(R.id.tv_buyStartingAmount);
		/** 追加认申购起点金额——后 */
		tv_appendStartingAmount = (TextView) view
				.findViewById(R.id.tv_appendStartingAmount);
		/** 追加认申购起点金额——前 */
		tv_appending = (TextView) view.findViewById(R.id.appendStrating);
		/** 币种 */
		tv_curCode = (TextView) view.findViewById(R.id.tv_curCode);
		/** 钞汇标志—人民币 */
		rmb_submit = (TextView) view.findViewById(R.id.rmb_submit);
		/** 钞汇选择 */
		rg_cashRemit = (RadioGroup) view.findViewById(R.id.rg_cashRemit);
		/** 现钞 */
		rb_bill = (RadioButton) view.findViewById(R.id.bill);
		/** 现汇 */
		rb_remit = (RadioButton) view.findViewById(R.id.remit);
		/** 投资方式 */
//		sp_investType = (Spinner) view.findViewById(R.id.sp_investType);
		/** 总期数方式 */
		rg_period = (RadioGroup) view.findViewById(R.id.rg_period);
		/** 限期 */
		rb_period = (RadioButton) view.findViewById(R.id.period);
		/** 不限期 */
		rb_noperiod = (RadioButton) view.findViewById(R.id.noperiod);
		// 限期时显示
		ll_totalPeriod = (LinearLayout) view.findViewById(R.id.ll_totalPeriod);
		/** 总期数 */
		et_totalPeriod = (EditText) view.findViewById(R.id.et_totalPeriod);
		// 定时定额显示
		/** 定投类型 */
//		sp_timeInvestType = (Spinner) view.findViewById(R.id.sp_timeInvestType);
		ll_timeInvestType = (LinearLayout) view
				.findViewById(R.id.ll_timeInvestType);
		/** 定投金额 */
		et_redeemAmount = (EditText) view.findViewById(R.id.et_redeemAmount);
		ll_redeemAmount = (LinearLayout) view
				.findViewById(R.id.ll_redeemAmount);
		tv_redeempre = (TextView) view.findViewById(R.id.tv_redeempre);
		/** 定投频率 */
		et_timeInvestRate = (EditText) view
				.findViewById(R.id.et_timeInvestRate);
		ll_timeInvestRate = (LinearLayout) view
				.findViewById(R.id.ll_timeInvestRate);
		/** 频率单位 */
		sp_timeInvestRate = (Spinner) view.findViewById(R.id.sp_timeInvestRate);
		/** 定投开始日 */
		tv_investTime = (TextView) view.findViewById(R.id.tv_investTime);
		tv_investTime_pre = (TextView) view
				.findViewById(R.id.tv_investTime_pre);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_investTime_pre);
		tv_investTime.setText(QueryDateUtils.getOneDayLater(dateTime));
		// 自动投资协议时显示
		/** 最低限额 */
		et_minAmount = (EditText) view.findViewById(R.id.et_minAmount);
		ll_minAmount = (LinearLayout) view.findViewById(R.id.ll_minAmount);
		/** 最高限额 */
		et_maxAmount = (EditText) view.findViewById(R.id.et_maxAmount);
		ll_maxAmount = (LinearLayout) view.findViewById(R.id.ll_maxAmount);
		TextView tv_investType = (TextView) findViewById(R.id.tv_investType);
		tv_tip = (TextView) findViewById(R.id.tv_tip);
		RadioGroup rg_ch = (RadioGroup) findViewById(R.id.rg_ch);
		// 赋值
		tv_tip.setVisibility(View.GONE);
		rg_ch.setOnCheckedChangeListener(this);
		switch (Integer.parseInt(str_agrtype)) {
		case 2:{//2：定时定额投资
			tv_investType.setText("定时定额");
			investType="0";
			/** 定投类型 */
			ll_timeInvestType.setVisibility(View.VISIBLE);
			/** 定投金额 */
			ll_redeemAmount.setVisibility(View.VISIBLE);
			/** 定投频率 */
			ll_timeInvestRate.setVisibility(View.VISIBLE);
			/** 最低限额 */
			ll_minAmount.setVisibility(View.GONE);
			/** 最高限额 */
			ll_maxAmount.setVisibility(View.GONE);
			tv_investTime_pre.setText(XpadApplyAgreement.this
					.getString(R.string.bocinvt_investTime));
			et_totalPeriod.setText(ConstantGloble.BOCINVT_NULL_STRING);
			// 定时定额请输入以1到9开头的数字，最大长度2个字符
			et_totalPeriod
					.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
							2) });
			tv_redeempre.setText(BaseDroidApp.getInstanse()
					.getCurrentAct()
					.getString(R.string.bocinvt_redeemAmount));
//			et_redeemAmount
//					.setHint(ConstantGloble.BOCINVT_NULL_STRING);
			et_redeemAmount
					.setText(ConstantGloble.BOCINVT_NULL_STRING);
		}break;
		case 3:{//3：周期滚续投资
		}break;
		case 4:{//4：余额理财投资
			tv_investType.setText("自动投资");
			investType="1";
			/** 定投类型 */
			ll_timeInvestType.setVisibility(View.GONE);
			/** 定投金额 */
			ll_redeemAmount.setVisibility(View.GONE);
			/** 定投频率 */
			ll_timeInvestRate.setVisibility(View.GONE);
			/** 最低限额 */
			ll_minAmount.setVisibility(View.VISIBLE);
			/** 最高限额 */
			ll_maxAmount.setVisibility(View.VISIBLE);
			tv_investTime_pre.setText(XpadApplyAgreement.this.getString(R.string.bocinvt_firstTime));
			et_totalPeriod.setText(ConstantGloble.BOCINVT_NULL_STRING);
			// 自动投资请输入1-366的整数，最大长度3个字符
			// p604修改为请输入以1到9开头的数字，最大长度2个字符
			et_totalPeriod.setFilters(new InputFilter[] { new InputFilter.LengthFilter(2) });
			timeInvestType="1";
			tv_redeempre.setText(XpadApplyAgreement.this.getString(R.string.bocinvt_redeemAmount_1));
//			et_redeemAmount.setHint(XpadApplyAgreement.this.getString(R.string.bocinvt_redeemAmount_hint));
			et_redeemAmount.setText(ConstantGloble.BOCINVT_NULL_STRING);
			EditTextUtils.relateNumInputToChineseShower(et_minAmount, (TextView)findViewById(R.id.money_chinese_min));
			EditTextUtils.relateNumInputToChineseShower(et_maxAmount, (TextView)findViewById(R.id.money_chinese_max));
		}break;
		default:
			break;
		}
		tv_prodNum.setText(StringUtil.getForSixForString((String) acctMap
				.get(BocInvt.ACCOUNTNO)));
		tv_prodCode.setText((String) detailMap
				.get(BocInvt.BOCI_DETAILPRODCODE_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodCode);
		tv_prodName.setText((String) detailMap
				.get(BocInvt.BOCI_DETAILPRODNAME_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodName);
		tv_buyStartingAmount.setText(StringUtil.parseStringCodePattern(
				(String) detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES),
				(String) detailMap
						.get(BocInvt.SUBAMOUNT), 2));
		tv_appendStartingAmount.setText(StringUtil.parseStringCodePattern(
				(String) detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES),
				(String) detailMap
						.get(BocInvt.ADDAMOUNT), 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_appending);
		tv_curCode.setText(LocalData.Currency.get((String) detailMap
				.get(BocInvt.BOCI_DETAILCURCODE_RES)));
		// 判断币种
		if (((String) detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES))
				.equals(ConstantGloble.BOCINVT_CURRENCY_RMB)) {
			// 是人民币
			rmb_submit.setVisibility(View.VISIBLE);
			rmb_submit.setText(ConstantGloble.BOCINVT_DATE_ADD);
			cashremit = agreeCashRemitList.get(0);
			rg_cashRemit.setVisibility(View.GONE);
		} else {
			// 其它的
			rmb_submit.setVisibility(View.GONE);
			rg_cashRemit.setVisibility(View.VISIBLE);
			rg_cashRemit.setOnCheckedChangeListener(this);
			rb_bill.setChecked(true);
		}
		spinnerInit();
//		investType = investTypeSubList.get(0);
		rg_period.setOnCheckedChangeListener(this);
		rb_period.setChecked(true);
		initListener();
		EditTextUtils.relateNumInputToChineseShower(et_redeemAmount, (TextView)findViewById(R.id.money_chinese));
	}

	private void spinnerInit() {
		/** 投资方式 */
//		ArrayAdapter<ArrayList<String>> adapter1 = new ArrayAdapter(this,
//				R.layout.custom_spinner_item, investTypeList);
//		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		sp_investType.setAdapter(adapter1);
//		sp_investType.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//				investType = investTypeSubList.get(position);
//				if (position == 0) {
//					// 定时定额
//					/** 定投类型 */
//					ll_timeInvestType.setVisibility(View.VISIBLE);
//					/** 定投金额 */
//					ll_redeemAmount.setVisibility(View.VISIBLE);
//					/** 定投频率 */
//					ll_timeInvestRate.setVisibility(View.VISIBLE);
//					/** 最低限额 */
//					ll_minAmount.setVisibility(View.GONE);
//					/** 最高限额 */
//					ll_maxAmount.setVisibility(View.GONE);
//					tv_investTime_pre.setText(XpadApplyAgreement.this
//							.getString(R.string.bocinvt_investTime));
//					et_totalPeriod.setText(ConstantGloble.BOCINVT_NULL_STRING);
//					// 定时定额请输入以1到9开头的数字，最大长度2个字符
//					et_totalPeriod
//							.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
//									2) });
//				} else {
//					// 自动投资
//					/** 定投类型 */
//					ll_timeInvestType.setVisibility(View.GONE);
//					/** 定投金额 */
//					ll_redeemAmount.setVisibility(View.GONE);
//					/** 定投频率 */
//					ll_timeInvestRate.setVisibility(View.GONE);
//					/** 最低限额 */
//					ll_minAmount.setVisibility(View.VISIBLE);
//					/** 最高限额 */
//					ll_maxAmount.setVisibility(View.VISIBLE);
//					tv_investTime_pre.setText(XpadApplyAgreement.this
//							.getString(R.string.bocinvt_firstTime));
//					et_totalPeriod.setText(ConstantGloble.BOCINVT_NULL_STRING);
//					// 自动投资请输入1-366的整数，最大长度3个字符
//					et_totalPeriod
//							.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
//									3) });
//				}
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//				investType = investTypeSubList.get(0);
//				// 定时定额
//				/** 定投类型 */
//				ll_timeInvestType.setVisibility(View.VISIBLE);
//				/** 定投金额 */
//				ll_redeemAmount.setVisibility(View.VISIBLE);
//				/** 定投频率 */
//				ll_timeInvestRate.setVisibility(View.VISIBLE);
//				/** 最低限额 */
//				ll_minAmount.setVisibility(View.GONE);
//				/** 最高限额 */
//				ll_maxAmount.setVisibility(View.GONE);
//				tv_investTime_pre.setText(XpadApplyAgreement.this
//						.getString(R.string.bocinvt_investTime));
//				et_totalPeriod.setText(ConstantGloble.BOCINVT_NULL_STRING);
//				// 定时定额请输入以1到9开头的数字，最大长度2个字符
//				et_totalPeriod
//						.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
//								2) });
//			}
//		});
//		sp_investType.setSelection(0);
		/** 定投类型 */
//		ArrayAdapter<ArrayList<String>> adapter2 = new ArrayAdapter(this,
//				R.layout.custom_spinner_item, timeInvestTypeList);
//		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		sp_timeInvestType.setAdapter(adapter2);
//		sp_timeInvestType
//				.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//					@Override
//					public void onItemSelected(AdapterView<?> parent,
//							View view, int position, long id) {
//						timeInvestType = investTypeSubList.get(position);
//						if (position == 0) {
//							tv_redeempre.setText(BaseDroidApp.getInstanse()
//									.getCurrentAct()
//									.getString(R.string.bocinvt_redeemAmount));
//							et_redeemAmount
//									.setHint(ConstantGloble.BOCINVT_NULL_STRING);
//							et_redeemAmount
//									.setText(ConstantGloble.BOCINVT_NULL_STRING);
//						} else {
//							tv_redeempre.setText(XpadApplyAgreement.this
//									.getString(R.string.bocinvt_redeemAmount_1));
//							et_redeemAmount.setHint(XpadApplyAgreement.this
//									.getString(R.string.bocinvt_redeemAmount_hint));
//							et_redeemAmount
//									.setText(ConstantGloble.BOCINVT_NULL_STRING);
//						}
//					}
//
//					@Override
//					public void onNothingSelected(AdapterView<?> parent) {
//						timeInvestType = investTypeSubList.get(0);
//						tv_redeempre.setText(XpadApplyAgreement.this
//								.getString(R.string.bocinvt_redeemAmount));
//					}
//				});
//		sp_timeInvestType.setSelection(0);
		/** 定投频率 */
		ArrayAdapter<ArrayList<String>> adapter3 = new ArrayAdapter(this,
				R.layout.custom_spinner_item, timeInvestRateFlagList);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_timeInvestRate.setAdapter(adapter3);
		sp_timeInvestRate
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						timeInvestRateFlag = timeInvestRateFlagSubList
								.get(position);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						timeInvestRateFlag = timeInvestRateFlagSubList.get(0);
					}
				});
		sp_timeInvestRate.setSelection(0);
	}

	/**
	 * 初始化监听事件
	 */
	private void initListener() {
		btn_next = (Button) view.findViewById(R.id.btn_next);
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (rb_noperiod.isChecked()) {
					// 不限期
				} else {
					// 限期——验证 自动投资请输入1-366的整数，最大长度3个字符
					period = et_totalPeriod.getText().toString().trim();
				}
				String time = tv_investTime.getText().toString();
				// 总期数——定时定额总期数请输入以1到9开头的数字，最大长度2个字符
				RegexpBean reb_ding = new RegexpBean(XpadApplyAgreement.this
						.getString(R.string.bocinvt_totalPeriod_null), period,
						"totalPeriod");
				// 总期数——自动投资请输入1-366的整数，最大长度3个字符
				RegexpBean reb_zidong = new RegexpBean(XpadApplyAgreement.this
						.getString(R.string.bocinvt_totalPeriod_null), period,
						"totalPeriod2");
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();

				if (investType.equals(investTypeSubList.get(0))) {
					// 定时定额——总期数请输入以1到9开头的数字，最大长度2个字符
					if (rb_noperiod.isChecked()) {
						// 不限期
					} else {
						// 限期——验证
						lists.add(reb_ding);
					}
					String curcode = (String) detailMap
							.get(BocInvt.BOCI_DETAILCURCODE_RES);
					// 验证
					/** 定投金额 */
					redeemAmount = et_redeemAmount.getText().toString().trim();
					if (timeInvestType.equals(investTypeSubList.get(0))) {
						// 购买——定投金额
						RegexpBean reb_amount = checkJapReg(
								curcode,
								XpadApplyAgreement.this
										.getString(R.string.bocinvt_redeemAmount_null),
								redeemAmount);
						lists.add(reb_amount);
					} else {
						// 赎回——定赎份额
						RegexpBean reb_amount_1 = new RegexpBean(
								XpadApplyAgreement.this
										.getString(R.string.bocinvt_redeemAmount_1_null),
								redeemAmount, "minAmount2");
						lists.add(reb_amount_1);
					}
					/** 定投频率 */
					timeInvestRate = et_timeInvestRate.getText().toString()
							.trim();
					RegexpBean reb_rate = new RegexpBean(
							XpadApplyAgreement.this
									.getString(R.string.bocinvt_timeInvestRate_null),
							timeInvestRate, "totalPeriod");
					lists.add(reb_rate);

				} else {
					String curcode = (String) detailMap
							.get(BocInvt.BOCI_DETAILCURCODE_RES);
					minAmount = et_minAmount.getText().toString().trim();
					maxAmount = et_maxAmount.getText().toString().trim();
					// 自动投资
					if (rb_noperiod.isChecked()) {
						// 不限期
					} else {
						// 限期——验证 自动投资请输入1-366的整数，最大长度3个字符
//						lists.add(reb_zidong);
						//请输入以1到9开头的数字，最大长度2个字符604修改
						lists.add(reb_ding);
					}
					// 最低限额也可以为0
					RegexpBean reb_minamount = checkJapRegForMin(
							curcode,
							XpadApplyAgreement.this
									.getString(R.string.bocinvt_minAmount_null),
							minAmount);
					lists.add(reb_minamount);
					// 最高限额可以为0
					RegexpBean reb_maxamount = checkJapRegForMin(
							curcode,
							XpadApplyAgreement.this
									.getString(R.string.bocinvt_maxAmount_null),
							maxAmount);
					lists.add(reb_maxamount);
				}
				if (!RegexpUtils.regexpDate(lists)) {// 校验没有通过
					return;
				}
				if (investType.equals(investTypeSubList.get(0))) {
					if (!QueryDateUtils.compareDate(time, dateTime)) {
						// 结束日期在服务器日期之前
					} else {
						BaseDroidApp
								.getInstanse()
								.showInfoMessageDialog(
										XpadApplyAgreement.this
												.getString(R.string.bocinvt_check_date));
						return;
					}
				} else {
					// 最高必须大于等于最低
					if (Double.valueOf(minAmount) <= Double.valueOf(maxAmount)) {

					} else {
							// 最高限额不能低于最低限额
							BaseDroidApp
							.getInstanse()
							.showInfoMessageDialog(
									XpadApplyAgreement.this
									.getString(R.string.bocinvt_check_minmaxAmount));
							return;
					}
					if (!QueryDateUtils.compareDate(time, dateTime)) {
						// 结束日期在服务器日期之前
					} else {
						BaseDroidApp
								.getInstanse()
								.showInfoMessageDialog(
										XpadApplyAgreement.this
												.getString(R.string.bocinvt_check_firdate));
						return;
					}
				}
				// TODO 进入确认页面——保存条件
				Map<String, String> agreeInputMap = new HashMap<String, String>();
				agreeInputMap.put(BocInvt.BOCINVT_AGREE_CURCODE_REQ,
						(String) detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES));
				agreeInputMap.put(BocInvt.BOCINVT_AGREE_INVESTTIME_REQ, time);
				agreeInputMap.put(BocInvt.BOCINVT_AGREE_INVESTTYPE_REQ,
						investType);
				agreeInputMap.put(BocInvt.BOCINVT_AGREE_MAXAMOUNT_REQ,
						maxAmount);
				agreeInputMap.put(BocInvt.BOCINVT_AGREE_MINAMOUNT_REQ,
						minAmount);
				agreeInputMap
						.put(BocInvt.BOCINVT_AGREE_PRODNAME_REQ,
								(String) detailMap
										.get(BocInvt.BOCI_DETAILPRODNAME_RES));
				agreeInputMap
						.put(BocInvt.BOCINVT_AGREE_PRODUCTCODE_REQ,
								(String) detailMap
										.get(BocInvt.BOCI_DETAILPRODCODE_RES));
				agreeInputMap.put(BocInvt.BOCINVT_AGREE_REDEEMAMOUNT_REQ,
						redeemAmount);
				agreeInputMap.put(BocInvt.BOCINVT_AGREE_TIMEINVESTRATE_REQ,
						timeInvestRate);
				agreeInputMap.put(BocInvt.BOCINVT_AGREE_TIMEINVESTRATEFLAG_REQ,
						timeInvestRateFlag);
				agreeInputMap.put(BocInvt.BOCINVT_AGREE_TIMEINVESTTYPE_REQ,
						timeInvestType);
				agreeInputMap
						.put(BocInvt.BOCINVT_AGREE_TOTALPERIOD_REQ, period);
				agreeInputMap.put(BocInvt.BOCINVT_AGREE_XPADCASHREMIT_REQ,
						cashremit);
				BociDataCenter.getInstance().setAgreeInputMap(agreeInputMap);
				requestPsnXpadQueryRiskMatch();
			}
		});
		tv_investTime.setOnClickListener(chooseDateClick);
	}

	/** 请求查询客户风险等级与产品风险等级是否匹配 */
	public void requestPsnXpadQueryRiskMatch() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADQUERYRISKMATCH_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(BocInvt.BOCINVT_MATCH_PRODUCTCODE_REQ,
				detailMap.get(BocInvt.BOCI_DETAILPRODCODE_RES));
//		paramsmap.put(Comm.ACCOUNTNUMBER,acctMap
//				.get(BocInvt.ACCOUNTNO));
		paramsmap.put("accountKey",acctMap
				.get(BocInvt.BOCINVT_CAPACITYQUERY_ACCOUNTKEY_RES));//604修改为accountkey
		biiRequestBody.setParams(paramsmap);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnXpadQueryRiskMatchCallBack");
	}

	/** 请求查询客户风险等级与产品风险等级是否匹配回调 */
	public void requestPsnXpadQueryRiskMatchCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		Map<String, Object> riskMatchMap = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(riskMatchMap)) {
			return;
		}
		BociDataCenter.getInstance().setRiskMatchMap(riskMatchMap);
		String riskMatch = (String) riskMatchMap
				.get(BocInvt.BOCINVT_MATCH_RISKMATCH_RES);
		if (riskMatch.equals(MATCH)) {
			// TODO 进入确定页面
			Intent intent = new Intent(XpadApplyAgreement.this,
					XpadApplyAgreementConfirm.class);
			startActivityForResult(intent, BocInvestControl.ACTIVITY_RESULT_CODE_AGREEMENT_APPLY);
//			startActivity(intent);
		} else if (riskMatch.equals(NOMATCHCAN)) {
			// 1：不匹配但允许交易
			// （即客户风险低于产品风险，但系统不强制风险匹配，可提示客户确认风险和交易）
			BaseDroidApp
					.getInstanse()
					.showErrorDialog(
							XpadApplyAgreement.this
									.getString(R.string.bocinvt_error_noriskExceed),
							R.string.cancle, R.string.confirm,
							new OnClickListener() {

								@Override
								public void onClick(View v) {

									switch (Integer.parseInt(v.getTag() + "")) {
									case CustomDialog.TAG_SURE:
										BaseDroidApp.getInstanse()
												.dismissErrorDialog();
										// 确定操作 进入确定页面
										Intent intent = new Intent(
												XpadApplyAgreement.this,
												XpadApplyAgreementConfirm.class);
										startActivityForResult(intent, BocInvestControl.ACTIVITY_RESULT_CODE_AGREEMENT_APPLY);
//										startActivity(intent);
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
					XpadApplyAgreement.this
							.getString(R.string.bocinvt_error_cannotBuy));
			return;
		}

	}

	/** 设置查询日期 */
	OnClickListener chooseDateClick = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			TextView tv = (TextView) v;
			String time = tv.getText().toString();
			int startYear = Integer.parseInt(time.substring(0, 4));
			int startMonth = Integer.parseInt(time.substring(5, 7));
			int startDay = Integer.parseInt(time.substring(8, 10));
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			DatePickerDialog dialog = new DatePickerDialog(
					XpadApplyAgreement.this, new OnDateSetListener() {

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
							// 为日期赋值
							((TextView) v).setText(String.valueOf(date));
						}
					}, startYear, startMonth - 1, startDay);
			dialog.show();
		}
	};
	private TextView tv_tip;

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.bill:
			// 现钞
			cashremit = agreeCashRemitList.get(1);
			break;
		case R.id.remit:
			// 现汇
			cashremit = agreeCashRemitList.get(2);
			break;
		case R.id.period:
			// 限期
			ll_totalPeriod.setVisibility(View.VISIBLE);
			et_totalPeriod.setText(ConstantGloble.BOCINVT_NULL_STRING);
			if (investType.equals(investTypeSubList.get(0))) {
				// 定时定额请输入以1到9开头的数字，最大长度2个字符
				et_totalPeriod
						.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
								2) });
			} else {
				// 自动投资请输入1-366的整数，最大长度3个字符
				// p604修改为请输入以1到9开头的数字，最大长度2个字符
				et_totalPeriod
						.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
								2) });
			}
			break;
		case R.id.noperiod:
			// 不限期
			ll_totalPeriod.setVisibility(View.GONE);
			period = NOPERIODSTR;
			break;
		case R.id.rb_c:
			timeInvestType="0";
			tv_tip.setVisibility(View.GONE);
			tv_redeempre.setText(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.bocinvt_redeemAmount));
//			et_redeemAmount.setHint(ConstantGloble.BOCINVT_NULL_STRING);
			et_redeemAmount.setText(ConstantGloble.BOCINVT_NULL_STRING);
			EditTextUtils.relateNumInputToChineseShower(et_redeemAmount, (TextView)findViewById(R.id.money_chinese));
			break;
		case R.id.rb_h:
			timeInvestType="1";
			tv_tip.setVisibility(View.VISIBLE);
			tv_redeempre.setText(XpadApplyAgreement.this.getString(R.string.bocinvt_redeemAmount_1));
//			et_redeemAmount.setHint(XpadApplyAgreement.this.getString(R.string.bocinvt_redeemAmount_hint));
			et_redeemAmount.setText(ConstantGloble.BOCINVT_NULL_STRING);
			Trans2ChineseNumber.relateNumInputAndChineseShower(et_redeemAmount, (TextView)findViewById(R.id.money_chinese));
			break;
		default:
			break;
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
