package com.chinamworld.bocmbci.biz.bocinvt_p603.productqueryandbuy.activity;


import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 周期滚续协议申请，确认页
 * @author Administrator
 *
 */
public class ProductQueryAndBuyPeriodAgreementSureActivity extends BocInvtBaseActivity{

	private Map<String, Object> acctMap;
	private Map<String, String> agreeInputMap;
	private Map<String, Object> chooseMap;
	/** 等级是否匹配 */
	private Map<String, Object> riskMatchMap;
	private Map<String, Object> accound_map;
	/** 产品详情 */
	private Map<String, Object> detailMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_query_and_buy_period_agreement_sure);
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
		agreeInputMap=BociDataCenter.getInstance().getAgreeInputMap();
		detailMap = BociDataCenter.getInstance().getDetailMap();
		chooseMap = BociDataCenter.getInstance().getChoosemap();
		riskMatchMap=BociDataCenter.getInstance().getRiskMatchMap();
	}
	
	private void init(){
		TextView tv_tip_bottom = (TextView) findViewById(R.id.tv_tip_bottom);
		LabelTextView tv_1 = (LabelTextView) findViewById(R.id.tv_1);
		LabelTextView tv_2 = (LabelTextView) findViewById(R.id.tv_2);
		LabelTextView tv_3 = (LabelTextView) findViewById(R.id.tv_3);
		LabelTextView tv_4 = (LabelTextView) findViewById(R.id.tv_4);
		LabelTextView tv_5 = (LabelTextView) findViewById(R.id.tv_5);
		LabelTextView tv_6 = (LabelTextView) findViewById(R.id.tv_6);
		LabelTextView tv_7 = (LabelTextView) findViewById(R.id.tv_7);
		LabelTextView tv_8 = (LabelTextView) findViewById(R.id.tv_8);
		LabelTextView tv_9 = (LabelTextView) findViewById(R.id.tv_9);
		LabelTextView tv_10 = (LabelTextView) findViewById(R.id.tv_10);
		LabelTextView tv_11 = (LabelTextView) findViewById(R.id.tv_11);
		LabelTextView tv_12 = (LabelTextView) findViewById(R.id.tv_12);
		LabelTextView tv_13 = (LabelTextView) findViewById(R.id.tv_13);
		((Button)findViewById(R.id.btn_next)).setOnClickListener(btn_next_clickListener);
		//赋值
//		String type = (String) riskMatchMap.get(BocInvt.BOCINVT_MATCH_PRORISK_RES);
		String type = (String) riskMatchMap.get(BocInvt.BOCINVT_MATCH_RISKMSG_RES);
		if (type.equals("0")) {
			tv_tip_bottom.setText(this.getString(R.string.bocinvt_confirm_bottom_1));
		} else if (type.equals("1")) {
			tv_tip_bottom.setText(this.getString(R.string.bocinvt_confirm_bottom_2));
		} else {
			tv_tip_bottom.setText(this.getString(R.string.bocinvt_confirm_bottom_3));
		}
		tv_1.setValueText(StringUtil.getForSixForString((String) accound_map.get(BocInvt.ACCOUNTNO)));
		tv_2.setValueText(acctMap.get("productName").toString());
		tv_3.setValueText(acctMap.get("serialName").toString());
		tv_4.setValueText(BocInvestControl.map_productCurCode_toStr.get(agreeInputMap.get(BocInvestControl.CURCODE)));
		tv_5.setValueText(BocInvestControl.map_cashRemit_code_key.get(agreeInputMap.get(BocInvestControl.CASHREMIT)));
		tv_6.setValueText(StringUtil.isNullOrEmpty(chooseMap.get("remainCycleCount"))?"-":chooseMap.get("remainCycleCount").toString());
		tv_7.setValueText(agreeInputMap.get(BocInvestControl.BUYCYCLECOUNT));
		tv_8.setValueText(BocInvestControl.map_invest_money_Type_code_key.get(agreeInputMap.get(BocInvestControl.BASEAMOUNTMODE)));
		if (agreeInputMap.get(BocInvestControl.BASEAMOUNTMODE).equals("1")) {//不定额
			tv_9.setVisibility(View.GONE);
			tv_10.setVisibility(View.VISIBLE);
			tv_11.setVisibility(View.VISIBLE);
			tv_10.setValueText(StringUtil.parseStringCodePattern(detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES).toString(), agreeInputMap.get(BocInvestControl.MINAMOUNT), 2));
			tv_11.setValueText(StringUtil.parseStringCodePattern(detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES).toString(), agreeInputMap.get(BocInvestControl.MAXAMOUNT), 2));
		}else {//定额
			tv_9.setVisibility(View.VISIBLE);
			tv_10.setVisibility(View.GONE);
			tv_11.setVisibility(View.GONE);
			tv_9.setValueText(StringUtil.parseStringCodePattern(detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES).toString(), agreeInputMap.get(BocInvestControl.BASEAMOUNT), 2));
		}
		tv_12.setValueText(LocalData.boci_prodRisklvlMap.get(String.valueOf(riskMatchMap.get(BocInvt.BOCINVT_MATCH_PRORISK_RES))));
		tv_13.setValueText(LocalData.riskLevelMap.get(String.valueOf(riskMatchMap.get(BocInvt.BOCINVT_MATCH_CUSTRISK_RES))));
	}
	private OnClickListener btn_next_clickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
		}
	};
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		if (StringUtil.isNullOrEmpty(resultObj)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId();
	}
	/**
	 * 获取tokenId
	 */
	public void requestPSNGetTokenId() {
		Map<String, Object> parms_map=new HashMap<String, Object>();
		getHttpTools().requestHttp(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API, "requestPSNGetTokenIdCallBack", parms_map, true);
	}
	@SuppressWarnings("static-access")
	public void requestPSNGetTokenIdCallBack(Object resultObj){
		if (StringUtil.isNullOrEmpty(resultObj)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		String str_token = getHttpTools().getResponseResult(resultObj).toString();
		requestPsnXpadSignResult(str_token);
	}
	/**
	 * 请求  周期性产品续约协议签约/签约结束
	 */
	private void requestPsnXpadSignResult(String token){
		Map<String, Object> parms_map=new HashMap<String, Object>();
		parms_map.put("accountId", accound_map.get("accountId").toString());
		parms_map.put("serialName", acctMap.get("serialName").toString());//产品系列名称
		parms_map.put("serialCode", acctMap.get("serialCode"));//产品系列编号
		parms_map.put("xpadCashRemit", agreeInputMap.get(BocInvestControl.CASHREMIT));//钞汇标识
		parms_map.put("totalPeriod", agreeInputMap.get(BocInvestControl.BUYCYCLECOUNT));//购买期数
		parms_map.put("amountTypeCode", agreeInputMap.get(BocInvestControl.BASEAMOUNTMODE));//基础金额模式
		parms_map.put("curCode", agreeInputMap.get(BocInvestControl.CURCODE));//币种
		if (agreeInputMap.get(BocInvestControl.BASEAMOUNTMODE).equals("0")) {//定额
			parms_map.put("baseAmount", agreeInputMap.get(BocInvestControl.BASEAMOUNT));//基础金额
		}else {//不定额
			parms_map.put("minAmount", agreeInputMap.get(BocInvestControl.MINAMOUNT));//最低预留金额
			parms_map.put("maxAmount", agreeInputMap.get(BocInvestControl.MAXAMOUNT));//最大扣款金额
		}
//		parms_map.put("dealCode", value);//指令交易后台交易ID,指令交易上送不可为空
		parms_map.put(BocInvt.BOCINVT_XPADRESULT_TOKEN_REQ, token);
		getHttpTools().requestHttp(BocInvestControl.PSNXPADSIGNRESULT, "requestPsnXpadSignResultCallBack", parms_map, true);
	}
	@SuppressWarnings("static-access")
	public void requestPsnXpadSignResultCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(resultObj)) {
			return;
		}
		//存储成功数据并跳转到成功页
		BaseDroidApp.getInstanse().getBizDataMap().put(BocInvestControl.PERIODAGREEMENTRESULTMAP, getHttpTools().getResponseResult(resultObj));
		Intent intent = new Intent(ProductQueryAndBuyPeriodAgreementSureActivity.this,ProductQueryAndBuyPeriodAgreementSuccessActivity.class);
		startActivityForResult(intent, BocInvestControl.ACTIVITY_RESULT_CODE_AGREEMENT_APPLY);
//		startActivity(intent);
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
