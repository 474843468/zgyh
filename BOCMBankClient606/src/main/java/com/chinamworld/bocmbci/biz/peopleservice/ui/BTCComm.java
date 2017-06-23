package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSONObject;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCommbean;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLable;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;

public class BTCComm extends BTCDrawLable {
	/** 上下文对象 */
	//private Context context;// 上下文对象
	private static final String TAG = BTCComm.class.getSimpleName();

	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCComm(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Comm;
		//this.context = context;
	}


	@Override
	public Object drawLable(Map<String, String> dbMap,
			View view) {
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		String name = btcElement.getElementName();
		String commname=params.get(BTCLable.NAME);
		String commtransaction=params.get(BTCLable.TRANSCATION);
		String bankid = params.get("bankid");
		Map<String, Object> postParams = new HashMap<String, Object>();
		Map<String, String> paramsMap = new HashMap<String, String>();
		Map<String, String> responseParams = new HashMap<String, String>();
		BTCCommbean request =new BTCCommbean(); 
		BTCElement childElement;
		BTCDrawLable btcDrawLable;
		for (int i = 0; i < childElements.size(); i++) {
			childElement = childElements.get(i);
//			btcDrawLable = BTCLableFactory.createLableInstance(null,context,
//					childElement.getElementName());
			btcDrawLable = childElement.getBTCDrawLable();
			if (BTCLable.REQUEST.equals(childElement.getElementName())) {
				
				//wuhan
				if(bankid!=null&&!"".equals(bankid)){
					//与后台请求。
					postParams.put("bankId", bankid);
					
				}
				Button btn = new Button(context);
				String requestStr= (String) btcDrawLable.drawLable(paramsMap, btn);
				 JSONObject jsss = new JSONObject();
//				 {CUST_ID=1000, 
//						 ACCT_NO=4563510100892709316, SCH_NO=0101,
//						 EBN_AGENT_CODE=05000006}
//				BTCCMWApplication.requestParamsMap.clear();
				for(Map.Entry<String, String> entry:paramsMap.entrySet()){
					String reqKey = entry.getKey();
					String reqValue = entry.getValue();
					if(reqValue==null){
						reqValue = "";
					}
					BTCCMWApplication.requestParamsMap.put(commname+"."+reqKey, reqValue);
				}
					if(!"".equals(commtransaction)&&commtransaction!=null){
						postParams.put("isSendCsp", 1+"");
						postParams.put("transCode", commtransaction);
						postParams.put("toCspParamsMap",  requestStr);//发送分行CSP
//						postParams.put("toCspParamsMap",  jsss.toJSON(paramsMap));//发送分行CSP
						postParams.put("flowFileId", BTCUiActivity.flowFileId);
						postParams.put("prvcShortName", PlpsDataCenter.getInstance().getPrvcShortName());//省简称
					}else{
						postParams.put("isSendCsp", 0+"");
						postParams.put("pageParamsMap",  requestStr);//不需向分行发送的数据
//						postParams.put("pageParamsMap",  jsss.toJSON(paramsMap));//不需向分行发送的数据
					}
					Random random = new Random();
					postParams.put("id", String.valueOf(random.nextInt(1000000000)));
					postParams.put("conversationId", BTCCMWApplication.conversationId);
					
			}
			
//			Map<String, String> dbMaps = new HashMap<String, String>();reponse
			if(BTCLable.RESPONSE.equals(childElement.getElementName())||"reponse".equals(childElement.getElementName())){
				
				responseParams =  (Map<String, String>) btcDrawLable.drawLable(responseParams, view);
				BTCCMWApplication.Response = responseParams;
			}
//			if (BTCLable.RESPONSE.equals(childElement.getElementName())) {
//				btcDrawLable.drawLable(childElement, responseParams, null);
//			}

		}
		request.setName(commname);
		request.setMethod("PsnPlpsSendAllFlowFileReq");
//		request.setUrl("http://192.168.1.2:9194/BIISimulate/_bfwajax.do");
		request.setParams(postParams);
		request.setResponse(responseParams);
//		body.setMethod("PsnPlpsSendAllFlowFileReq");
//		body.setParams(postParams);
		return request;
	}
	
//	void getRequest(final Map<String, String> requestTest){
//		new Thread(new Runnable() {
//			public void run() {
//				//test
//				String url = "http://192.168.1.2:9194/BIISimulate/_bfwajax.do";
//				Map<String, String> request = new HashMap<String, String>();
//				request.put("method", "PsnPlpsSendAllFlowFileReq");
//				request.put("params", requestTest.toString());
//				
//				try {
//					Map<String, Object> result  = BTCHttpManager.getInstance()
//							.openUrl(url, "POST", request);
//					BTCLogManager.logI("info", "TBCComm request 之后response=="+result.toString());
//					//{biiexception=false, result={"cspReturnMap":{"departmentList":[{"epcount":"10","name":"内科","ndname":"消化内科"},{"epcount":"8","name":"外科","ndname":"耳鼻喉科"},{"epcount":"5","name":"儿科","ndname":"小儿科"}],"hospitalId":"01","hospitalName":"北京协和医院","totalCount":"102.35"},"pageParamsMap":{"customerId":"343545465"},"securityToolsInfo":{"factorList":[{"field":{"name":"Smc","type":"password"}},{"field":{"name":"Otp","type":"password"}}],"smcTrigerInterval":"60"}}, header={"agent":"WEB20","device":"3","ext":"8","local":"7","page":"6","platform":"4","plugins":"5","version":"2"}}
//					Map<String, Object> resultmap=(Map<String, Object>) result.get("result");
//					BTCCMWApplication.getDepartmentsAll = resultmap;//返回所有的数据。
//					BTCCMWApplication.getDepartments = (Map<String, Object>) resultmap.get("cspReturnMap");
//					
//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//			}
//		}).start();
//	}
	
}