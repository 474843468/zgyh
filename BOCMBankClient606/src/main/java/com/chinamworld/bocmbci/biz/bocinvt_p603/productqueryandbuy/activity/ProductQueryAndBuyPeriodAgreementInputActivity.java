package com.chinamworld.bocmbci.biz.bocinvt_p603.productqueryandbuy.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 周期滚续协议输入页
 * @author Administrator
 */
public class ProductQueryAndBuyPeriodAgreementInputActivity extends BocInvtBaseActivity {

	private Map<String, Object> acctMap;
	private Map<String, Object> accound_map;
	/** 产品详情 */
	private Map<String, Object> detailMap;
	/** 钞汇 */
	private String cashremit;
	private Map<String, Object> chooseMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_query_and_buy_period_agreement_input);
		setLeftButtonPopupGone();
		getBackgroundLayout().setTitleText("投资协议申请");
		getBackgroundLayout().setLeftButtonText("返回");
		getBackgroundLayout().setLeftButtonClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		initDate();
		init();
	}
	@SuppressWarnings("unchecked")
	private void initDate(){
		accound_map=BocInvestControl.accound_map;
		acctMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BOCINVT_BUYINIT_MAP);
		detailMap = BociDataCenter.getInstance().getDetailMap();
		chooseMap = BociDataCenter.getInstance().getChoosemap();
	}
	
	private void init(){
		LabelTextView tv_1 = (LabelTextView) findViewById(R.id.tv_1);
		LabelTextView tv_2 = (LabelTextView) findViewById(R.id.tv_2);
		LabelTextView tv_3 = (LabelTextView) findViewById(R.id.tv_3);
		LabelTextView tv_4 = (LabelTextView) findViewById(R.id.tv_4);
		LabelTextView tv_5 = (LabelTextView) findViewById(R.id.tv_5);
		TextView rmb_submit = (TextView) findViewById(R.id.rmb_submit);
		tv_dinge = (TextView) findViewById(R.id.tv_dinge);
		RadioGroup rg_cashRemit = (RadioGroup) findViewById(R.id.rg_cashRemit);//钞汇
		RadioGroup rg_d_e = (RadioGroup) findViewById(R.id.rg_d_e);//基础金额模式
		et_totalPeriod = (EditText) findViewById(R.id.et_totalPeriod);
		layout_baseAmount = findViewById(R.id.layout_baseAmount);
		layout_minAmount = findViewById(R.id.layout_minAmount);
		layout_maxAmount = findViewById(R.id.layout_maxAmount);
		ed_baseAmount = (EditText) findViewById(R.id.ed_baseAmount);
		ed_minAmount = (EditText) findViewById(R.id.ed_minAmount);
		ed_maxAmount = (EditText) findViewById(R.id.ed_maxAmount);
		rb_b_d_e = (RadioButton) findViewById(R.id.rb_b_d_e);
		//赋值
		tv_1.setValueText(StringUtil.getForSixForString((String) accound_map.get(BocInvt.ACCOUNTNO)));
		tv_2.setValueText(acctMap.get("productName").toString());
		tv_3.setValueText(acctMap.get("serialName").toString());
		tv_3.setValueText(acctMap.get("serialName").toString());
		tv_4.setValueText(LocalData.Currency.get((String) detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES)));
		tv_5.setValueText(StringUtil.isNullOrEmpty(chooseMap.get("remainCycleCount"))?"-":chooseMap.get("remainCycleCount").toString());
		// 判断币种
				if (((String) detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES)).equals(ConstantGloble.BOCINVT_CURRENCY_RMB)) {
					// 是人民币
					rmb_submit.setVisibility(View.VISIBLE);
					rmb_submit.setText(ConstantGloble.BOCINVT_DATE_ADD);
					cashremit = BocInvestControl.map_cashRemit_code_value.get("-");
					rg_cashRemit.setVisibility(View.GONE);
				} else {
					// 其它的
					rmb_submit.setVisibility(View.GONE);
					rg_cashRemit.setVisibility(View.VISIBLE);
					cashremit=BocInvestControl.map_cashRemit_code_value.get(BocInvestControl.list_cashRemit.get(0));
					rg_cashRemit.setOnCheckedChangeListener(radioGroupCheckedListener);
				}
				if (acctMap.get("periodContinue").toString().equals("1")) {//只支持定额
					tv_dinge.setVisibility(View.VISIBLE);
					rg_d_e.setVisibility(View.GONE);
				}else {//支持定额、非定额
					tv_dinge.setVisibility(View.GONE);
					rg_d_e.setVisibility(View.VISIBLE);
					rg_d_e.setOnCheckedChangeListener(radioGroupCheckedListener);
				}
				EditTextUtils.relateNumInputToChineseShower(ed_baseAmount, (TextView)findViewById(R.id.money_chinese));
				EditTextUtils.relateNumInputToChineseShower(ed_minAmount, (TextView)findViewById(R.id.money_chinese_min));
				EditTextUtils.relateNumInputToChineseShower(ed_maxAmount, (TextView)findViewById(R.id.money_chinese_max));
				((Button)findViewById(R.id.btn_next)).setOnClickListener(new OnClickListener() {//下一步按钮点击事件
					@Override
					public void onClick(View v) {
						saveDateAndToNext();
					}
				});
	}
	/**
	 * 校验保存数据并跳转到下一页面
	 */
	private void saveDateAndToNext(){
		ArrayList<RegexpBean> rg_list=new ArrayList<RegexpBean>();
		String str_et_totalPeriod = et_totalPeriod.getText().toString().trim();//购买期数
//		rg_list.add(BocInvestControl.getRegexpBean(null, "购买期数", str_et_totalPeriod, "sixNumberAndOne"));
		rg_list.add(BocInvestControl.getRegexpBean(null, "购买期数", str_et_totalPeriod, "totalPeriod"));//p604老协议（余额理财、定时定额、周期滚续），申请2位，新协议（智能协议、业绩基准周期滚续），申请6位
		if (acctMap.get("periodContinue").toString().equals("1")) {//只支持定额
			rg_list.add(BocInvestControl.getRegexpBean(detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES).toString(), "基础金额", ed_baseAmount.getText().toString().trim(), null));//基础金额
		}else {//支持定额、非定额
			if (!rb_b_d_e.isChecked()) {//定额
				rg_list.add(BocInvestControl.getRegexpBean(detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES).toString(), "基础金额", ed_baseAmount.getText().toString().trim(), null));//基础金额
			}else {//不定额
				rg_list.add(BocInvestControl.getRegexpBean(detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES).toString(), "最低预留金额", ed_minAmount.getText().toString().trim(), null));//最低预留金额
				rg_list.add(BocInvestControl.getRegexpBean(detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES).toString(), "最大扣款金额", ed_maxAmount.getText().toString().trim(), null));//最大扣款金额
			}
		}
		if (!RegexpUtils.regexpDate(rg_list)) {//校验不通过
			return;
		}
		if (Double.parseDouble(str_et_totalPeriod)>Double.parseDouble(chooseMap.get("remainCycleCount").toString())) {//输入的购买期数超过最大可购买期数
			BaseDroidApp.getInstanse().showInfoMessageDialog("购买期数不能大于可投资期数");
			return;
		}
		// TODO 进入确认页面——保存条件
		Map<String, String> agreeInputMap = new HashMap<String, String>();
		agreeInputMap.put(BocInvestControl.CURCODE, detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES).toString());//产品币种
		agreeInputMap.put(BocInvestControl.CASHREMIT, cashremit);//钞汇
		agreeInputMap.put(BocInvestControl.BUYCYCLECOUNT, et_totalPeriod.getText().toString().trim());//购买期数
		if (acctMap.get("periodContinue").toString().equals("1")) {//只支持定额
			agreeInputMap.put(BocInvestControl.BASEAMOUNT, ed_baseAmount.getText().toString().trim());//基础金额
			agreeInputMap.put(BocInvestControl.BASEAMOUNTMODE, 
					BocInvestControl.map_invest_money_Type_code_value.get(BocInvestControl.list_invest_money_Type.get(0)));//基础金额模式
		}else {//支持定额、非定额
			if (!rb_b_d_e.isChecked()) {//定额
				agreeInputMap.put(BocInvestControl.BASEAMOUNT, ed_baseAmount.getText().toString().trim());//基础金额
				agreeInputMap.put(BocInvestControl.BASEAMOUNTMODE, 
						BocInvestControl.map_invest_money_Type_code_value.get(BocInvestControl.list_invest_money_Type.get(0)));//基础金额模式
			}else {//不定额
				agreeInputMap.put(BocInvestControl.MINAMOUNT, ed_minAmount.getText().toString().trim());//最低预留金额
				agreeInputMap.put(BocInvestControl.MAXAMOUNT, ed_maxAmount.getText().toString().trim());//最大扣款金额
				agreeInputMap.put(BocInvestControl.BASEAMOUNTMODE, 
						BocInvestControl.map_invest_money_Type_code_value.get(BocInvestControl.list_invest_money_Type.get(1)));//基础金额模式
			}
		}
		BociDataCenter.getInstance().setAgreeInputMap(agreeInputMap);
		requestPsnXpadQueryRiskMatch();
	}
	/** 请求查询客户风险等级与产品风险等级是否匹配 */
	public void requestPsnXpadQueryRiskMatch() {
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put("serialCode", acctMap.get("serialCode").toString());//周期性产品系列编号
		paramsmap.put(BocInvt.BOCINVT_MATCH_PRODUCTCODE_REQ,detailMap.get(BocInvt.BOCI_DETAILPRODCODE_RES));
//		paramsmap.put(Comm.ACCOUNTNUMBER,accound_map.get(BocInvt.ACCOUNTNO));
		paramsmap.put(BocInvt.BOCINVT_MATCH_ACCOUNTKEY_REQ,accound_map.get(BocInvt.BOCINVT_CAPACITYQUERY_ACCOUNTKEY_RES));
		BiiHttpEngine.showProgressDialog();
		getHttpTools().requestHttp(BocInvt.BOCINVT_PSNXPADQUERYRISKMATCH_API, "requestPsnXpadQueryRiskMatchCallBack", paramsmap, true);
	}
	/** 请求查询客户风险等级与产品风险等级是否匹配回调 */
	@SuppressWarnings("static-access")
	public void requestPsnXpadQueryRiskMatchCallBack(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(resultObj)) {
			return;
		}
		Map<String, Object> riskMatchMap = getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(riskMatchMap)) {
			return;
		}
		BociDataCenter.getInstance().setRiskMatchMap(riskMatchMap);
		String riskMatch = (String) riskMatchMap.get(BocInvt.BOCINVT_MATCH_RISKMATCH_RES);
		if (riskMatch.equals(BocInvestControl.MATCH)) {
			// TODO 进入确定页面
			Intent intent = new Intent(ProductQueryAndBuyPeriodAgreementInputActivity.this,ProductQueryAndBuyPeriodAgreementSureActivity.class);
			startActivityForResult(intent, BocInvestControl.ACTIVITY_RESULT_CODE_AGREEMENT_APPLY);
//			startActivity(intent);
		} else if (riskMatch.equals(BocInvestControl.NOMATCHCAN)) {
			// 1：不匹配但允许交易
			// （即客户风险低于产品风险，但系统不强制风险匹配，可提示客户确认风险和交易）
			BaseDroidApp.getInstanse().showErrorDialog(
					ProductQueryAndBuyPeriodAgreementInputActivity.this.getString(R.string.bocinvt_error_noriskExceed),
							R.string.cancle, R.string.confirm,
							new OnClickListener() {

								@Override
								public void onClick(View v) {

									switch (Integer.parseInt(v.getTag() + "")) {
									case CustomDialog.TAG_SURE:
										BaseDroidApp.getInstanse().dismissErrorDialog();
										// 确定操作 进入确定页面
										Intent intent = new Intent(ProductQueryAndBuyPeriodAgreementInputActivity.this,
												ProductQueryAndBuyPeriodAgreementSureActivity.class);
										startActivityForResult(intent, BocInvestControl.ACTIVITY_RESULT_CODE_AGREEMENT_APPLY);
//										startActivity(intent);
										break;
									case CustomDialog.TAG_CANCLE:
										// 取消操作
										BaseDroidApp.getInstanse().dismissErrorDialog();
										break;
									}

								}
							});
		} else if (riskMatch.equals(BocInvestControl.NOMATCH)) {
			// 2：不匹配且拒绝交易
			// （即客户风险低于产品风险，且系统强制风险匹配，拒绝交易）
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					ProductQueryAndBuyPeriodAgreementInputActivity.this.getString(R.string.bocinvt_error_cannotBuy));
			return;
		}

	}
	private OnCheckedChangeListener radioGroupCheckedListener=new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.bill:{//现钞
				cashremit=BocInvestControl.map_cashRemit_code_value.get(BocInvestControl.list_cashRemit.get(0));
			}break;
			case R.id.remit:{//现汇
				cashremit=BocInvestControl.map_cashRemit_code_value.get(BocInvestControl.list_cashRemit.get(1));
			}break;
			case R.id.rb_d_e:{//定额
				layout_baseAmount.setVisibility(View.VISIBLE);
				layout_minAmount.setVisibility(View.GONE);
				layout_maxAmount.setVisibility(View.GONE);
			}break;
			case R.id.rb_b_d_e:{//不定额
				layout_baseAmount.setVisibility(View.GONE);
				layout_minAmount.setVisibility(View.VISIBLE);
				layout_maxAmount.setVisibility(View.VISIBLE);
			}break;

			default:
				break;
			}
		}
	};

	private View layout_baseAmount;
	private View layout_minAmount;
	private View layout_maxAmount;
	private EditText et_totalPeriod;
	private EditText ed_baseAmount;
	private EditText ed_minAmount;
	private EditText ed_maxAmount;
	private RadioButton rb_b_d_e;
	private TextView tv_dinge;
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














