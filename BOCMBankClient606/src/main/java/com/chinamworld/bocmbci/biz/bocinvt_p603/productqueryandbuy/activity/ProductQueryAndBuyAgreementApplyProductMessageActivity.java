package com.chinamworld.bocmbci.biz.bocinvt_p603.productqueryandbuy.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.util.Trans2ChineseNumber;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 中银理财-投资协议申请，投资产品信息页
 * @author Administrator
 *
 */
public class ProductQueryAndBuyAgreementApplyProductMessageActivity extends BocInvtBaseActivity implements OnCheckedChangeListener{

	private Map<String, Object> map_agreement_detail;
	private Map<String, Object> detailMap;
	private Map<String, Object> accound_map;
	/**true/不定额、false/定额*/
	private boolean isFlag;
	/**投资方式*/
	private int investType=-1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initBaseLayout();
		setContentView(R.layout.product_query_and_buy_agreement_product_messages);
		initDate();
		initUI();
	}
	/**
	 * 组件初始化及赋值
	 */
	private void initUI(){
		//初始化
		LabelTextView tv_1 = (LabelTextView) findViewById(R.id.tv_1);
		LabelTextView tv_2 = (LabelTextView) findViewById(R.id.tv_2);
		LabelTextView tv_3 = (LabelTextView) findViewById(R.id.tv_3);
		LabelTextView tv_4 = (LabelTextView) findViewById(R.id.tv_4);
		LabelTextView tv_5 = (LabelTextView) findViewById(R.id.tv_5);
		TextView tv_6 = (TextView) findViewById(R.id.tv_6);
		TextView tv_left_3 = (TextView) findViewById(R.id.tv_left_3);
		layout_1 = findViewById(R.id.layout_1);
		TextView textview = (TextView) findViewById(R.id.textview);//币种为人民币时显示"-"
		tv_tip = (TextView) findViewById(R.id.tv_tip);
		rg_ch = (RadioGroup) findViewById(R.id.rg_ch);
		rg_de = (RadioGroup) findViewById(R.id.rg_de);
		rb_c = (RadioButton) findViewById(R.id.rb_c);
		edt_1 = (EditText) findViewById(R.id.edt_1);
		edt_2 = (EditText) findViewById(R.id.edt_2);
		layout_fene = findViewById(R.id.layout_fene);
		layout_bu_ding_e = findViewById(R.id.layout_bu_ding_e);
		edt_5 = (EditText) findViewById(R.id.edt_5);
		edt_yu_e = (EditText) findViewById(R.id.edt_yu_e);
		edt_max_buy = (EditText) findViewById(R.id.edt_max_buy);
		Button btn_next = (Button) findViewById(R.id.btn_next);
		getInvestType();//获取投资方式
		//赋值
		switch (investType) {
		case -1:{//接口中investType取值为空
			
		}break;
		case 1:{//周期连续协议 
			EditTextUtils.relateNumInputToChineseShower(edt_1,(TextView)findViewById(R.id.tv_chinese));
			layout_fene.setVisibility(View.GONE);
			tv_tip.setVisibility(View.GONE);
			layout_bu_ding_e.setVisibility(View.GONE);
		}break;
		case 2:{//周期不连续协议 
			EditTextUtils.relateNumInputToChineseShower(edt_1,(TextView)findViewById(R.id.tv_chinese));
			layout_fene.setVisibility(View.GONE);
			tv_tip.setVisibility(View.GONE);
			layout_bu_ding_e.setVisibility(View.GONE);
		}break;
		case 3:{//多次购买协议 
			EditTextUtils.relateNumInputToChineseShower(edt_1,(TextView)findViewById(R.id.tv_chinese));
			layout_fene.setVisibility(View.GONE);
			layout_bu_ding_e.setVisibility(View.GONE);
		}break;
		case 4:{//多次赎回协议
			Trans2ChineseNumber.relateNumInputAndChineseShower(edt_5, (TextView)findViewById(R.id.tv_chinese));
			layout_1.setVisibility(View.GONE);
			tv_tip.setVisibility(View.GONE);
			layout_bu_ding_e.setVisibility(View.GONE);
			tv_left_3.setText("单次赎回份额：");
		}break;

		default:
			break;
		}
		
		EditTextUtils.relateNumInputToChineseShower(edt_yu_e,(TextView)findViewById(R.id.tv_chinese_yu_e));
		EditTextUtils.relateNumInputToChineseShower(edt_max_buy,(TextView)findViewById(R.id.tv_chinese_max_buy));
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView)findViewById(R.id.tv_left_1));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView)findViewById(R.id.tv_7));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView)findViewById(R.id.tv_left_3));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView)findViewById(R.id.tv_left_2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView)findViewById(R.id.tv_yu_e));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView)findViewById(R.id.tv_max_buy));
		
		tv_1.setValueText(String.valueOf(map_agreement_detail.get(BocInvt.BOCINVT_SIGNINIT_PRODUCTNAME_RES)));
		tv_2.setValueText(BocInvestControl.getYearlyRR(map_agreement_detail, BocInvestControl.RATE, BocInvt.BOCINVT_AGRINFOQUERY_RATEDETAIL_RES));
		str_curcode = String.valueOf(detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES));
		tv_3.setValueText(String.valueOf(BocInvestControl.map_productCurCode_toStr.get(str_curcode)));
		tv_4.setValueText(StringUtil.parseStringCodePattern(String.valueOf(detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES)), String.valueOf(map_agreement_detail.get(BocInvestControl.AGRPURSTART)), 2));
		tv_5.setValueText(StringUtil.getForSixForString(String.valueOf(accound_map.get(BocInvt.ACCOUNTNO))));
		if (String.valueOf(detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES)).equals(String.valueOf(BocInvestControl.map_productCurCode.get(BocInvestControl.list_productCurCode.get(1))))) {
			//币种为 人民币
			textview.setVisibility(View.VISIBLE);
			rg_ch.setVisibility(View.GONE);
		}else {
			//币种为  非人民币
			textview.setVisibility(View.GONE);
			rg_ch.setVisibility(View.VISIBLE);
		}
		if (String.valueOf(map_agreement_detail.get("isQuota")).equals("1")) {//	1/允许不定额、0/不允许
			//允许不定额
			tv_6.setVisibility(View.GONE);
			rg_de.setVisibility(View.VISIBLE);
			rg_de.setOnCheckedChangeListener(this);
		}else {
			tv_6.setVisibility(View.VISIBLE);
			rg_de.setVisibility(View.GONE);
		}
		
//		setSpinner();
		btn_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (regexpEdittext()) {//输入值校验通过
					BaseHttpEngine.showProgressDialog();
					requestPsnXpadQueryRiskMatch();
//					requestCommConversationId();
				}
			}
		});
	}
	/**
	 * 校验输入值
	 * @return true/校验成功，false/校验失败
	 */
	private boolean regexpEdittext(){
		ArrayList<RegexpBean> list_regex=new ArrayList<RegexpBean>();
		if (!isFlag) {//定额
			
			switch (investType) {
			case -1:{//接口中investType取值为空
				
			}break;
			case 4:{//多次赎回协议
				list_regex.add(BocInvestControl.getRegexpBean(null, "单次赎回份额", edt_5.getText().toString().trim(), null));
				list_regex.add(BocInvestControl.getRegexpBean(null, "投资期数", edt_2.getText().toString().trim(), "sixNumber"));
			}break;

			default:{//1周期连续协议 、2周期不连续协议 、3多次购买协议 
				list_regex.add(BocInvestControl.getRegexpBean(str_curcode, "单期投资金额", edt_1.getText().toString().trim(), null));
				list_regex.add(BocInvestControl.getRegexpBean(null, "投资期数", edt_2.getText().toString().trim(), "sixNumber"));
			}break;
			}
		}else {//不定额
			
			list_regex.add(BocInvestControl.getRegexpBean(str_curcode, "账户保留余额", edt_yu_e.getText().toString().trim(), "amount1"));
			list_regex.add(BocInvestControl.getRegexpBean(str_curcode, "最大购买金额", edt_max_buy.getText().toString().trim(), null));
			list_regex.add(BocInvestControl.getRegexpBean(null, "投资期数", edt_2.getText().toString().trim(), "sixNumber"));
		}
		
		if (!list_regex.isEmpty()) {
			if (RegexpUtils.regexpDate(list_regex)) {//校验成功
				return true;
			}
		}
		
		
		return false;
	}
	/**
	 * 获取当前协议投资方式
	 */
	private void getInvestType(){
		String temp_str = map_agreement_detail.get(BocInvestControl.INVESTTYPE).toString();
		if (StringUtil.isNullOrEmpty(temp_str)) {return;}
		investType=Integer.parseInt(temp_str);
	}
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		//获取token
		getHttpTools().requestHttp(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API, "requestPSNGetTokenIdCallBack", new HashMap<String, Object>(), true);
	}
	/**
	 * 处理获取tokenId的数据得到tokenId
	 * @param resultObj 服务器返回数据
	 */
	
	@SuppressWarnings("static-access")
	public void requestPSNGetTokenIdCallBack(Object reslutObj){
		if (StringUtil.isNullOrEmpty(reslutObj)) {
			return;
		}
		String str_token = getHttpTools().getResponseResult(reslutObj).toString();
		requestPsnXpadAptitudeTreatyApplyVerify(str_token);
	}
	/**
	 * 请求 协议申请预交易
	 */
	private void requestPsnXpadAptitudeTreatyApplyVerify(String token){
		Map<String, Object> parms_map=new HashMap<String, Object>();
		parms_map.put(BocInvt.BOCI_ACCOUNTID_REQ, accound_map.get(BocInvt.BOCIBINDING_ACCOUNTIDF_RES).toString());
		parms_map.put(BocInvestControl.AGRCODE, map_agreement_detail.get(BocInvestControl.AGRCODE).toString());
		if (!isFlag) {
			//定额
			parms_map.put(BocInvestControl.AMOUNTTYPE, BocInvestControl.map_invest_money_Type_code_value.get(BocInvestControl.list_invest_money_Type.get(0)));
			parms_map.put(BocInvt.BOCINVT_BUYRES_AMOUNT_REQ, edt_1.getText().toString().trim());//单期购买金额/单次赎回金额
			//非必上送
			parms_map.put("minAmount", "0");//账户保留余额
			parms_map.put("maxAmount", "0");//最大购买金额
		}else {
			//不定额
			parms_map.put(BocInvestControl.AMOUNTTYPE, BocInvestControl.map_invest_money_Type_code_value.get(BocInvestControl.list_invest_money_Type.get(1)));
			parms_map.put("minAmount", edt_yu_e.getText().toString());//账户保留余额
			parms_map.put("maxAmount", edt_max_buy.getText().toString());//最大购买金额
			//非必上送
			parms_map.put(BocInvt.BOCINVT_BUYRES_AMOUNT_REQ, "");//单期购买金额/单次赎回金额
		}
		switch (investType) {
		case 4:{//多次赎回协议
			if("0".equals((String)map_agreement_detail.get("isNeedPur"))){
				parms_map.put("amount", edt_5.getText().toString());//赎回金额
			}else{
				parms_map.put("unit", edt_5.getText().toString());//赎回份额
			}
		}break;
		default:{
			parms_map.put("unit", "");//赎回份额}break;
		}
		}
		parms_map.put("isControl", "0");							//是否不限期,0/否、1/是,此处经业务确认写成固定值
		if (((String)parms_map.get("isControl")).equals("0")) {//isControl为“1：是”时，上送“-1”；isControl当为“0：否”上送具体签约期数
			parms_map.put("totalPeriod", edt_2.getText().toString());	//购买期数/赎回期数
		}else {
			parms_map.put("totalPeriod", "-1");	//购买期数/赎回期数
		}
		parms_map.put("charCode", getCharCode());					//钞汇类型
		parms_map.put(BocInvt.BOCINVT_SETBONUS_TOKEN_REQ, token);	//防重机制，通过PSNGetTokenId接口获取
		getHttpTools().requestHttp(BocInvestControl.PSNXPADAPTITUDETREATYAPPLYVERIFY, "requestPsnXpadAptitudeTreatyApplyVerifyCallBack", parms_map, true);
//		}
	}
	private String getCharCode(){
		if (rg_ch.isShown()) {
			return rb_c.isChecked()?BocInvestControl.map_xpadCashRemit_code_value.get(BocInvestControl.list_cashRemit.get(0))
					:BocInvestControl.map_xpadCashRemit_code_value.get(BocInvestControl.list_cashRemit.get(1));
		}
		return "00";
	}
	/**
	 * 请求 协议申请预交易  回调
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public void requestPsnXpadAptitudeTreatyApplyVerifyCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(resultObj)) {
			return;
		}
		Map<String , Object> responseDeal = (Map<String , Object>)getHttpTools().getResponseResult(resultObj);
		BocInvestControl.map_agreement_result_sure.clear();
		BocInvestControl.map_agreement_result_sure.putAll(responseDeal);
		startActivityForResult(new Intent(
				ProductQueryAndBuyAgreementApplyProductMessageActivity.this,ProductQueryAndBuyAgreementApplySureActivity.class)
				.putExtra(BocInvestControl.key_isFlag_agreement_apply, isFlag)
				.putExtra(BocInvestControl.key_investType_agreement_apply, investType),
				BocInvestControl.ACTIVITY_RESULT_CODE_AGREEMENT_APPLY);
	}
//	private void setSpinner(){
		// 钞/汇
//		ArrayAdapter<ArrayList<String>> adapter1 = new ArrayAdapter(this,
//				R.layout.custom_spinner_item, BocInvestControl.list_cashRemit);
//		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		sp_1.setAdapter(adapter1);
//		sp_1.setSelection(0);
		// 投资金额模式，定额/不定额
//		ArrayAdapter<ArrayList<String>> adapter2 = new ArrayAdapter(this,
//				R.layout.custom_spinner_item, BocInvestControl.list_invest_money_Type);
//		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		sp_2.setAdapter(adapter2);
//		sp_2.setSelection(0);
//		sp_2.setOnItemSelectedListener(new OnItemSelectedListener() {
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//				if (is_invest_money_Type(arg2)) {
//					layout_1.setVisibility(View.GONE);
//					layout_2.setVisibility(View.VISIBLE);
//					layout_3.setVisibility(View.VISIBLE);
//				}else {
//					layout_1.setVisibility(View.VISIBLE);
//					layout_2.setVisibility(View.GONE);
//					layout_3.setVisibility(View.GONE);
//				}
//			}
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				
//			}
//		});
//	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;
		default:
			break;
		}
	}
	/**
	 * 初始化数据
	 */
	private void initDate(){
//		chooseMap = BociDataCenter.getInstance().getChoosemap();
//		result_agreement=BocInvestControl.result_agreement;
//		map_listview_choose=BocInvestControl.map_listview_choose;
		detailMap=BociDataCenter.getInstance().getDetailMap();
		accound_map=BocInvestControl.accound_map;
		map_agreement_detail=BocInvestControl.map_agreement_detail;
	}
	/**
	 * 初始化基类布局
	 */
	private void initBaseLayout(){
		setLeftButtonPopupGone();
		getBackgroundLayout().setTitleText("投资协议申请");
		getBackgroundLayout().setLeftButtonText("返回");
		getBackgroundLayout().setLeftButtonClickListener(backClickListener);
	}
	/**
	 * 监听事件，返回
	 */
	private OnClickListener backClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};
	private View layout_1;
	private EditText edt_1;
	private EditText edt_2;
	private EditText edt_5;
	private EditText edt_yu_e;
	private EditText edt_max_buy;
	private RadioGroup rg_ch;
	private RadioGroup rg_de;
	private RadioButton rb_c;
	/**产品币种--币种代码*/
	private String str_curcode;
	/**账户保留余额、最大购买金额，不定额时显示*/
	private View layout_bu_ding_e;
	private TextView tv_tip;
	private View layout_fene;
	/**单选按钮选中事件*/
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_de:{//定额
			isFlag=false;
			showViewOnClickDingE(true);
		}break;
		case R.id.rb_bde:{//不定额
			isFlag=true;
			showViewOnClickDingE(false);
		}break;

		default:
			break;
		}
	}
	
	/**
	 * 定额、不定额单选按钮点击时要显示或隐藏的元素
	 * @param isDingE true/定额、false/不定额
	 */
	private void showViewOnClickDingE(boolean isDingE){
		if (isDingE) {
			layout_bu_ding_e.setVisibility(View.GONE);
			switch (investType) {
			case -1:{//接口中investType取值为空
				
			}break;
			case 1:{//周期连续协议 
				layout_1.setVisibility(View.VISIBLE);
			}break;
			case 2:{//周期不连续协议 
				layout_1.setVisibility(View.VISIBLE);
			}break;
			case 3:{//多次购买协议 
				layout_1.setVisibility(View.VISIBLE);
				tv_tip.setVisibility(View.VISIBLE);
			}break;
			case 4:{//多次赎回协议
				layout_fene.setVisibility(View.VISIBLE);
			}break;

			default:
				break;
			}
		}else {
			switch (investType) {
			case -1:{//接口中investType取值为空
				
			}break;
			case 1:{//周期连续协议 
				layout_1.setVisibility(View.GONE);
			}break;
			case 2:{//周期不连续协议 
				layout_1.setVisibility(View.GONE);
			}break;
			case 3:{//多次购买协议 
				layout_1.setVisibility(View.GONE);
				tv_tip.setVisibility(View.GONE);
			}break;
			case 4:{//多次赎回协议
				layout_fene.setVisibility(View.GONE);
			}break;

			default:
				break;
			}
			layout_bu_ding_e.setVisibility(View.VISIBLE);
		}
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
//		paramsmap.put(Comm.ACCOUNTNUMBER,String.valueOf(accound_map.get(BocInvt.ACCOUNTNO)));
		paramsmap.put(BocInvt.BOCINVT_MATCH_ACCOUNTKEY_REQ,accound_map.get(BocInvt.BOCINVT_CAPACITYQUERY_ACCOUNTKEY_RES));
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
//		BiiHttpEngine.dissMissProgressDialog();
		Map<String, Object> riskMatchMap = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(riskMatchMap)) {
			return;
		}
		BociDataCenter.getInstance().setRiskMatchMap(riskMatchMap);
		String riskMatch = (String) riskMatchMap
				.get(BocInvt.BOCINVT_MATCH_RISKMATCH_RES);
		if (riskMatch.equals("0")) {
			requestCommConversationId();
		} else if (riskMatch.equals("1")) {
			BiiHttpEngine.dissMissProgressDialog();
			// 1：不匹配但允许交易
			// （即客户风险低于产品风险，但系统不强制风险匹配，可提示客户确认风险和交易）
			BaseDroidApp
					.getInstanse()
					.showErrorDialog(
							ProductQueryAndBuyAgreementApplyProductMessageActivity.this
									.getString(R.string.bocinvt_error_noriskExceed),
							R.string.cancle, R.string.confirm,
							new OnClickListener() {

								@Override
								public void onClick(View v) {

									switch (Integer.parseInt(v.getTag() + "")) {
									case CustomDialog.TAG_SURE:
										BaseDroidApp.getInstanse()
												.dismissErrorDialog();
										requestCommConversationId();
										break;
									case CustomDialog.TAG_CANCLE:
										// 取消操作
										BaseDroidApp.getInstanse()
												.dismissErrorDialog();

										break;
									}

								}
							});
		} else if (riskMatch.equals("2")) {
			BiiHttpEngine.dissMissProgressDialog();
			// 2：不匹配且拒绝交易
			// （即客户风险低于产品风险，且系统强制风险匹配，拒绝交易）
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					ProductQueryAndBuyAgreementApplyProductMessageActivity.this
							.getString(R.string.bocinvt_error_cannotBuy));
			return;
		}

	}
}
