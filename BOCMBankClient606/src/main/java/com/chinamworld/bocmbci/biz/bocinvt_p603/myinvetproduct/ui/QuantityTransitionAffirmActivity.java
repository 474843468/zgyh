package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.View;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCProductForHoldingInfo;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.IHttpResponseCallBack;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.userwidget.LabelTextView.TextColor;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 份额转换确认页面
 * 
 * @author HVZHUNG
 *
 */
public class QuantityTransitionAffirmActivity extends BocInvtBaseActivity {
	
	private BOCProductForHoldingInfo info;
	Map<String, Object> detailMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(R.string.bocinvt_holding_detail_lot_transform);
//		getBackgroundLayout().setRightButtonText(null);
		setContentView(R.layout.bocinvt_quantity_transition_affirm_activity_p603);
		
		info = ((BocInvestControl)BocInvestControl.getInstance()).curBOCProductForHoldingInfo;
		
		detailMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_PRODUCT_DETAIL_MAP);
		((LabelTextView) findViewById(R.id.ltv_product_code)).setValueText(info.prodCode);
		((LabelTextView) findViewById(R.id.ltv_product_name)).setValueText(info.prodName);	
		String curcode = (String)detailMap.get("curCode");
		/** 产品币种 */
		((LabelTextView) findViewById(R.id.ltv_currency)).setValueText(LocalData.Currency.get(curcode));
		((LabelTextView) findViewById(R.id.ltv_cash_remittance)).setValueText(LocalData.cashRemitMapValue.get(String.valueOf(info.cashRemit)));
		((LabelTextView) findViewById(R.id.ltv_estimate_apy)).setValueText(info.yearlyRR + "%");
//		if("04".equals(info.termType)&&"-1".equals(info.productTerm)){
//			((LabelTextView) findViewById(R.id.ltv_product_deadline)).setValueText("无固定期限");
//		}else{
//			((LabelTextView) findViewById(R.id.ltv_product_deadline)).setValueText("最低持有"+info.productTerm+"天");
//		}
		((LabelTextView) findViewById(R.id.ltv_product_deadline)).setValueText("最低持有"+detailMap.get("prodTimeLimit")+"天");
		((LabelTextView) findViewById(R.id.ltv_redeem_date)).setValueText((String)info.PsnXpadShareTransitionVerifyResponseMap.get("dueDate"));
		LabelTextView tranUnit = (LabelTextView) findViewById(R.id.ltv_transform);
//		tranUnit.setValueText(StringUtil.parseStringCodePattern(curcode,  info.transferUnit, 2));
		tranUnit.setValueText(StringUtil.parseStringPattern(info.transferUnit, 2));
		tranUnit.setValueTextColor(TextColor.Red);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_next:
			this.getHttpTools().requestHttpWithConversationId(Comm.PSN_GETTOKENID,null, (String)BaseDroidApp.getInstanse().getBizDataMap()
					.get(ConstantGloble.CONVERSATION_ID),new IHttpResponseCallBack<String>(){


				@Override
				public void httpResponseSuccess(String result,String method) {
					BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.TOKEN_ID,result);
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("token", result);
					map.put("accountKey", ((BocInvestControl)BocInvestControl.getInstance()).curBOCProductForHoldingInfo.bancAccountKey);
//					this.requestHttpWithConversationId("PsnXpadShareTransitionCommit", map, "requestPsnXpadShareTransitionCommit", (String)BaseDroidApp.getInstanse().getBizDataMap()
//							.get(ConstantGloble.CONVERSATION_ID));
//					
					QuantityTransitionAffirmActivity.this.getHttpTools().requestHttpWithConversationId("PsnXpadShareTransitionCommit",map, (String)BaseDroidApp.getInstanse().getBizDataMap()
							.get(ConstantGloble.CONVERSATION_ID),new IHttpResponseCallBack<Map<String,Object>>(){


						@Override
						public void httpResponseSuccess(Map<String,Object> result,String method) {
							BaseHttpEngine.dissMissProgressDialog();
							info.psnXpadShareTransitionCommitResponseMap  = result;
							ActivityIntentTools.intentToActivity(QuantityTransitionAffirmActivity.this,
									QuantityTransitionSuccessActivity.class);
						}
					});
					
				}
			});
			
//			this.requestHttpWithConversationId(Comm.PSN_GETTOKENID,null,"requestPSNGetTokenIdCallBack",(String)BaseDroidApp.getInstanse().getBizDataMap()
//					.get(ConstantGloble.CONVERSATION_ID));
			break;

		}
	}
	
//	@Override
//	public void requestPSNGetTokenIdCallBack(Object resultObj) {
//		super.requestPSNGetTokenIdCallBack(resultObj);
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("token", (String)BaseDroidApp.getInstanse().getBizDataMap()
//				.get(ConstantGloble.TOKEN_ID));
//		map.put("accountKey", ((BocInvestControl)BocInvestControl.getInstance()).curBOCProductForHoldingInfo.accountKey);
//		this.requestHttpWithConversationId("PsnXpadShareTransitionCommit", map, "requestPsnXpadShareTransitionCommit", (String)BaseDroidApp.getInstanse().getBizDataMap()
//				.get(ConstantGloble.CONVERSATION_ID));
//		
//	}

	
//	public void requestPsnXpadShareTransitionCommit(Object resultObj){
//		BaseHttpEngine.dissMissProgressDialog();
//		info.psnXpadShareTransitionCommitResponseMap  = getHttpTools().getResponseResult(resultObj);
//
//		Intent intent = new Intent(this,
//				QuantityTransitionSuccessActivity.class);
//		startActivity(intent);
//	}
}
