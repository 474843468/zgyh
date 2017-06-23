package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.CommissionDealForGeneralInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.CommissionDealForGroupInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.ICommissionDealInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment.CommissionDealCancelForGeneralFragment;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment.CommissionDealCancelForGroupFragment;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;

/**
 * 委托交易撤单
 * 
 * @author HVZHUNG
 * 
 */
public class CommissionDealRevokeActivity extends BocInvtBaseActivity {

	private ICommissionDealInfo info;

	private String conversationId;
	
	private String ibknum;
	private String typeOfAccount;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("委托交易撤单");
		setContentView(R.layout.bocinvt_commission_deal_revoke_activity_p603);

		info = (ICommissionDealInfo) getIntent().getSerializableExtra("info");
		ibknum=getIntent().getStringExtra("ibknum");
		typeOfAccount=getIntent().getStringExtra("typeOfAccount");
		if (info instanceof CommissionDealForGeneralInfo) {
			requestCeneralCommissionDealCancelVerify((CommissionDealForGeneralInfo) info);
		} else if (info instanceof CommissionDealForGroupInfo) {
			requestGroupCommissionDealCancelVerify((CommissionDealForGroupInfo) info);
		}
		
	}

	/**
	 * 
	 * 组合委托交易确认请求
	 * 
	 * @param info
	 */
	private void requestGroupCommissionDealCancelVerify(
			CommissionDealForGroupInfo info) {

		getSupportFragmentManager()
				.beginTransaction()
				.replace(
						R.id.container,
						CommissionDealCancelForGroupFragment.newInstance(0,
								info)).commit();
	}

	/**
	 * 常规委托交易撤单确认请求 PsnXpadRemoveGuarantyProductResult
	 * 
	 * @param info
	 */
	private void requestCeneralCommissionDealCancelVerify(
			CommissionDealForGeneralInfo info) {

		getSupportFragmentManager()
				.beginTransaction()
				.replace(
						R.id.container,
						CommissionDealCancelForGeneralFragment.newInstance(0,
								(CommissionDealForGeneralInfo) info)).commit();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_next:
			if (info == null) {
				return;
			}
			BaseHttpEngine.showProgressDialog();
			if (info instanceof CommissionDealForGeneralInfo) {
//				常规委托交易撤单PsnXpadDelegateCancel
				requestCommConversationId();
				
			} else if (info instanceof CommissionDealForGroupInfo) {
//				组合购买解除：PsnXpadRemoveGuarantyProductResult
				requestCommConversationId();
//				requestPsnXpadRemoveGuarantyProductResult((CommissionDealForGroupInfo) info);
			}
			
		
			
			

			break;

		default:
			break;
		}
	}

	
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		conversationId=(String)BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		requestPSNGetTokenId(conversationId);
	}
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String token=(String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		if(info instanceof CommissionDealForGeneralInfo){
			requestPsnXpadDelegateCancel(token, (CommissionDealForGeneralInfo) info);
		}else if(info instanceof CommissionDealForGroupInfo){
			requestPsnXpadRemoveGuarantyProductResult(token,(CommissionDealForGroupInfo) info);
		}
		
	}
	private void requestPsnXpadRemoveGuarantyProductResult(String token,CommissionDealForGroupInfo info) {

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod("PsnXpadRemoveGuarantyProductResult");
		biiRequestBody.setConversationId(conversationId);
		Map<String, Object> parms=new HashMap<String, Object>();
		parms.put("tranSeq", info.tranSeq);//组合交易流水号
		parms.put("currency", info.currency);//交易币种
		parms.put("buyAmt", info.buyAmt);//购买金额
		parms.put("amount", info.amount);//成交金额
		parms.put("xpadCode", info.prodCode);//产品代码
		parms.put("xpadName", info.prodName);//产品名称
		parms.put("trsDate", info.returnDate);//交易日期
		parms.put("cashRemit", info.cashRemit);//钞汇标识		
		parms.put("trfPrice", info.trfPrice);//购买价格
		parms.put("status", info.status);//状态
		parms.put("channel", info.channel);//渠道
		parms.put("ibknum", ibknum);//省行联行号
		parms.put("typeOfAccount", typeOfAccount);//账户类型
		parms.put("token", token);
		parms.put("accountKey", info.accountKey);
		biiRequestBody.setParams(parms);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnXpadRemoveGuarantyProductResultCallBack");
		
	
		
	}
	public void requestPsnXpadRemoveGuarantyProductResultCallBack(Object resultObj){
		Map<String,Object> map = getHttpTools().getResponseResult(resultObj);
//		Map<String, Object> map2 = (Map<String, Object>) map.get("guarantyBuyInfo");
		((CommissionDealForGroupInfo)info).transactionId = (String)map.get("transactionId");
//		BigDecimal buyAmtTemp;
//		if (StringUtil.isNullOrEmpty(map2.get("buyAmt"))) {
//			buyAmtTemp = new BigDecimal(0);
//		} else {
//			buyAmtTemp = new BigDecimal(map2.get("buyAmt")+"");
//		}
//		((CommissionDealForGroupInfo)info).buyAmt = buyAmtTemp;
//		BigDecimal amountTemp ;
//		if (StringUtil.isNullOrEmpty(map2.get("amount"))) {
//			amountTemp = new BigDecimal(0);
//		} else {
//			amountTemp = new BigDecimal(map2.get("amount")+"");
//		}
//		((CommissionDealForGroupInfo)info).amount = amountTemp;
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = null;
		intent = CommissionDealRevokeSucceedActivity.getIntent(this, info);
		startActivity(intent);
	}
	public void requestPsnXpadDelegateCancel(String token,CommissionDealForGeneralInfo info){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod("PsnXpadDelegateCancel");
		biiRequestBody.setConversationId(conversationId);
		Map<String, Object> parms=new HashMap<String, Object>();
		parms.put("accountKey",info.accountKey);//账户缓存标识
		parms.put("transSeq",info.tranSeq );//交易流水号（后台）
		parms.put("entrustType",info.entrustType + "");//委托业务类型
		parms.put("token", token);//防重标识	
					/*非必上送字段*/
		parms.put("prodCode", info.prodCode);//产品代码	
		parms.put("prodName", info.prodName);//产品名称
		parms.put("currencyCode", info.currencyCode);//交易币种
		parms.put("cashRemit", info.cashRemit);//钞汇标识
		parms.put("amount", info.amount + "");//交易金额
		biiRequestBody.setParams(parms);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnXpadDelegateCancelCallBack");
		
	}
	public void requestPsnXpadDelegateCancelCallBack(Object resultObj){
		Map<String,Object> map = getHttpTools().getResponseResult(resultObj);
		((CommissionDealForGeneralInfo)info).transactionId = (String)map.get("transactionId");
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = null;
		intent = CommissionDealRevokeSucceedActivity.getIntent(this, info);
		startActivity(intent);
	}
	
	public static Intent getIntent(Context context, ICommissionDealInfo info) {
		Intent intent = new Intent(context, CommissionDealRevokeActivity.class);
		intent.putExtra("info", info);
		return intent;
	}
}
