package com.chinamworld.bocmbci.biz.peopleservice.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.content.Context;
import android.view.View;

import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCommbean;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.global.BTCLable;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IAwaitExecute;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IExecuteFuction;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IFunction;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IHttpCallBack;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;



public class BTCAction extends BTCDrawLable implements IAwaitExecute {
	/** 上下文对象 */
//	private Context context;// 上下文对象
	private static final String TAG = BTCAction.class.getSimpleName();
	/** 界面栈对象 */
	private BTCActivityManager activityManager;// 界面栈对象
	/** 全局变量对象 */
	private BTCCMWApplication cmwApplication;
	BTCUiActivity uiActivity;
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public BTCAction(Context context,BTCElement element) {
		super(context,element);
		elementType=ElementType.Action;
		activityManager = BTCActivityManager.getInstance();
		cmwApplication = BTCUiActivity.getApp();
		this.uiActivity = BTCUiActivity.getInstance();
	}
	 List<BTCCommbean> commlist = null;
	
	private void ResponseDataHandle(Object result)
	{
		try {
			BTCCommbean reques = commlist.get(0);
			BiiResponse biiResponse = (BiiResponse) result;
			List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
			BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//			BTCCMWApplication.resultmap.clear();
			LogGloble.i("info", "BTCAction result====" +  biiResponseBody.getResult().toString());
			Map<String, Object> resultmap = (Map<String, Object>) biiResponseBody.getResult();
			
			if(resultmap.size()!=0){ //为了区分没有comm标签的时候，只有sercurity时，resultmap.size=0也算成功。
				Map<String, Object> requestMap =new HashMap<String, Object>();//请求回来的json
//				String requestKeys = "";
				 for(Map.Entry<String, Object> entrys : resultmap.entrySet()){
					   String keys = entrys.getKey().toString();
					   if("cspReturnMap".equals(keys)&&resultmap.get(keys)!=null){
						   requestMap=(Map<String, Object>) resultmap.get(keys);
						   
					   }else if("pageParamsMap".equals(keys)&&resultmap.get(keys)!=null){
						   requestMap=(Map<String, Object>) resultmap.get(keys);
						   
					   }
					 //请求回来的所有数据；有可能在use,或一下次请求的用到里面的数据；
//					   BTCCMWApplication.resultmap =requestMap;
				   }
				 List<String> requestsKeys=new ArrayList<String>();
				 if(requestMap!=null&&requestMap.size()!=0){
					 if(requestMap!=null&&requestMap.size()!=0){
//					    	{ERR_CODE=00, ERR_MSG=null, DATA_LIST=[{"agent_sub_no":"0000000","merchname":"呈贡"}]}
					    	for(Map.Entry<String, Object> entry:requestMap.entrySet()){
					    		String key = entry.getKey();
					    		requestsKeys.add(key);
					    	}
					    }
					 //用BTCCMWApplication.Response 中的key与reques.getResponse().entrySet() 中的key相同
					 //用BTCCMWApplication.Response 中的value 与requestsKeys中的key比较，相同的，则做为value放入BTCCMWApplication.responsemap
					 //{cylist:DATA_LIST}
					 if(reques.getResponse() !=null && reques.getResponse().size()!=0){
//						 BTCCMWApplication.responsemap.clear();
						  for (Map.Entry<String, String> entry : reques.getResponse().entrySet()) {
//								<param name="userName">USRNAM</param>/
							  String values = entry.getValue().toString();
							  String key = entry.getKey().toString();//cylist  userName
							  if(key !=null&&values!=null ){ //
								  String value = entry.getValue().toString();//DATA_LIST USRNAM
								   if(requestsKeys.contains(key)||requestsKeys.contains(value)){
									  //getDepartments. hospitalName组织的是xml中responsemap中的数据
									   if(requestMap.get(key)!=null ){
										   BTCCMWApplication.responsemap.put(reques.getName()+"."+key, requestMap.get(key));
//										   InfoInquery.DsfFiller  DSF_FILLER
										   BTCCMWApplication.responsemap.put(reques.getName()+"."+value, requestMap.get(key));
//										   BTCCMWApplication.responsemap.put(value, requestMap.get(key));
//										   BTCCMWApplication.responsemap.put(key, requestMap.get(key));
//										   if(key.equals("Amount")){
//											   BTCCMWApplication.responsemap.put("AMOUNT", requestMap.get(key));
//										   }
									   }else if(requestMap.get(value) !=null){
										   BTCCMWApplication.responsemap.put(reques.getName()+"."+value, requestMap.get(value));
										   BTCCMWApplication.responsemap.put(reques.getName()+"."+key, requestMap.get(value));
//										   BTCCMWApplication.responsemap.put(value, requestMap.get(value));
//										   BTCCMWApplication.responsemap.put(key, requestMap.get(value));
//										   InfoInquery.DSF_FILLER  DSF_FILLER
//										   if(value.equals("Amount")){
//											   BTCCMWApplication.responsemap.put("AMOUNT", requestMap.get(value));
//										   }
									   }
									   else{
//										   BTCCMWApplication.responsemap.put(key, " ");
//										   BTCCMWApplication.responsemap.put(value, " ");
										   BTCCMWApplication.responsemap.put(reques.getName()+"."+key, " ");
										   BTCCMWApplication.responsemap.put(reques.getName()+"."+value, " ");
									   }
									  
								   }
							  }
						  
						  } 
						  
						  //response里面没有的字段
						  for (int i = 0 ;i<requestsKeys.size();i++) {
							  String key = requestsKeys.get(i);
							 Map<String, String> responseMap = reques.getResponse();
							 if(!responseMap.containsValue(key)){
								 BTCCMWApplication.responsemap.put(reques.getName()+"."+key, requestMap.get(key));
							 }
						  }
					 }else{//没有response的情况下
						 
						 for (Map.Entry<String, Object> entry : requestMap.entrySet()) {
							   String key = entry.getKey().toString();//cylist  hospitalName
							   if(requestMap.get(key)!=null){
								   String value = entry.getValue().toString();//DATA_LIST
								   if(requestMap.get(key)!=null ){
									   BTCCMWApplication.responsemap.put(reques.getName()+"."+key, requestMap.get(key));
//									   InfoInquery.DsfFiller  DSF_FILLER
									   BTCCMWApplication.responsemap.put(reques.getName()+"."+value, requestMap.get(key));
//									   BTCCMWApplication.responsemap.put(key, requestMap.get(key));
//									   BTCCMWApplication.responsemap.put(value, requestMap.get(key));
								   }else if(requestMap.get(value) !=null){
//									   BTCCMWApplication.responsemap.put(value, requestMap.get(value));
//									   BTCCMWApplication.responsemap.put(key, requestMap.get(value));
									   BTCCMWApplication.responsemap.put(reques.getName()+"."+value, requestMap.get(value));
									   BTCCMWApplication.responsemap.put(reques.getName()+"."+key, requestMap.get(value));
//									   InfoInquery.DSF_FILLER  DSF_FILLER
								   }
								   else{
									   BTCCMWApplication.responsemap.put(reques.getName()+"."+key, " ");
									   BTCCMWApplication.responsemap.put(reques.getName()+"."+value, " ");
								   }
							   }
							  
							  
							 //getDepartments. hospitalName组织的是xml中responsemap中的数据
//							   if(requestMap.get(value)!=null){
//								   BTCCMWApplication.responsemap.put(reques.getName()+"."+key, requestMap.get(key));
////								   InfoInquery.DsfFiller  DSF_FILLER
//								   BTCCMWApplication.responsemap.put(reques.getName()+"."+value, requestMap.get(key));
////								   InfoInquery.DSF_FILLER  DSF_FILLER
//							   }else{
//								   BTCCMWApplication.responsemap.put(reques.getName()+"."+key, " ");
//								   BTCCMWApplication.responsemap.put(reques.getName()+"."+value, " ");
//							   }
						  } 
					 }
					
					  
					LogGloble.i(TAG," result="+result.toString());
				 }
				 
				 Map<String, Object> securityToolsInfo = new HashMap<String, Object>();
				 //有安全因的时情况有  securityToolsInfo active 
				 if(BTCCMWApplication.isContainSecurityActive&&resultmap.containsKey("securityToolsInfo")){
					securityToolsInfo = (Map<String, Object>) resultmap.get("securityToolsInfo"); 
					List<HashMap<String, Object>> factorList = new ArrayList<HashMap<String,Object>>();
					factorList = (List<HashMap<String, Object>>) securityToolsInfo.get("factorList");
					BTCCMWApplication.factorList = factorList;
					BTCCMWApplication._plainData = (String)securityToolsInfo.get("_plainData");
					BTCCMWApplication._certDN = (String)securityToolsInfo.get("_certDN");
				 }
				    
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	
	boolean isRequestServiceId = false;
	
	public void requestHttpFromAction(Map<String, String> dbMap,
			View view,final IExecuteFuction callback) {
		commlist = new ArrayList<BTCCommbean>();
		Map<String, String> params = btcElement.getParams();
		List<BTCElement> childElements = btcElement.getChildElements();
		String name = btcElement.getElementName();
		BTCElement childElement;
		BTCDrawLable btcDrawLable;
//		BTCCMWApplication.isContainSecurityInit = false;
//		BTCCMWApplication.isContainSecurityActive = false;
		BTCElement stepElement = findNearElemntBy(new IFunction(){

			@Override
			public <T> boolean func(T t) {
				return ((BTCElement)t).getBTCDrawLable() instanceof BTCStep;
			}});
		// 寻找所有的Common和Security标签
		List<BTCElement> commonList = FindElementBy(null, stepElement, new IFunction(){

			@Override
			public <T> boolean func(T t) {
				return ((BTCElement)t).getBTCDrawLable() instanceof BTCComm;
			}});
		BTCElement security = findElementToChildren(stepElement,new IFunction(){

			@Override
			public <T> boolean func(T t) {
				return ((BTCElement)t).getBTCDrawLable() instanceof BTCSecurity;
			}});

		//余海涛添加 。在一个交易做完以后，重复做交易时，BTCCMWApplication.security并未清空。
		//所以不能直接用BTCCMWApplication.security是否存在来判断是否有安全因子
		Map<String, String> securityMap = new HashMap<String,String>();
		
		if(security != null) {
			securityMap =  (Map<String, String>) security.getBTCDrawLable().drawLable(null, view);
			BTCCMWApplication.security = securityMap;
		}
		
		if(childElements.toString().contains(BTCLable.COMM)){//含有comm标签
//			Map<String, String> security = new HashMap<String, String>();
//			if(childElements.toString().contains(BTCLable.SECURITY)){
//				for (int i = 0; i < childElements.size(); i++){
//					childElement = childElements.get(i);
//					btcDrawLable = childElement.getBTCDrawLable();
//					if(btcDrawLable instanceof BTCSecurity){
//						if(BTCLable.SECURITY.equals(childElement.getElementName())){
//							security =  (Map<String, String>) btcDrawLable.drawLable(null, view);
//							securityMap = security;
//							BTCCMWApplication.security = security;
//						}
//					}
//				}
//			}
			
			for (int i = 0; i < childElements.size(); i++) {
				childElement = childElements.get(i);

				btcDrawLable = childElement.getBTCDrawLable();
				
				if(BTCLable.SECURITY.equals(childElement.getElementName())){
//					securityMap =  (Map<String, String>) btcDrawLable.drawLable(null, view);
//					BTCCMWApplication.security = securityMap;
					continue;
				}
				
				if (BTCLable.COMM.equals(childElement.getElementName())) {
					BTCCommbean request = (BTCCommbean) btcDrawLable.drawLable(null, view);
					Map<String, Object> postParams = new HashMap<String, Object>();
					postParams =request.getParams();
					if(commonList.indexOf(childElement) == 0)  // 只有第一个才需要组装数据
						combinSecurityData(postParams,securityMap);
					else 
						combinSecurityData(postParams,null);
					//用完之后，要清空，不然会重复提交
//					securityMap.clear();
					request.setParams(postParams);
					commlist.add(request);
				}

			}
		}
		else if(commonList.size() == 0 && childElements.toString().contains(BTCLable.SECURITY)){//只含有security标签
			for (int i = 0; i < childElements.size(); i++) {
				childElement = childElements.get(i);
				
				btcDrawLable = childElement.getBTCDrawLable();
				if(BTCLable.SECURITY.equals(childElement.getElementName())){
					securityMap =  (Map<String, String>) btcDrawLable.drawLable(null, view);
					BTCCMWApplication.security = securityMap;
				}
				
				BTCCommbean request = new BTCCommbean();
				Map<String, Object> postParams = new HashMap<String, Object>();
//				postParams =request.getParams();
				Random random = new Random();
				postParams.put("id", String.valueOf(random.nextInt(1000000000)));
				postParams.put("conversationId", BTCCMWApplication.conversationId);				
				postParams.put("flowFileId", BTCUiActivity.flowFileId);
				postParams.put("isSendCsp", 0+"");
				postParams.put("prvcShortName", PlpsDataCenter.getInstance().getPrvcShortName());//省简称
				combinSecurityData(postParams,securityMap);
				//用完之后，要清空，不然会重复提交
//				securityMap.clear();

				request.setParams(postParams);
				commlist.add(request);
			}
		}
		
		if(BTCCMWApplication.isContainSecurityInit){
			requestInitFactory(new IExecuteFuction(){
				@Override
				public void executeResultCallBack(Object param) {
					// TODO Auto-generated method stub
					sendHttpRequest(commlist,callback);
				}});
				return;
			
		}
		else if(BTCCMWApplication.isContainSecurityActive){
			requestActiveRandom(new IHttpCallBack(){
				@Override
				public void requestHttpSuccess(Object result) {
					sendHttpRequest(commlist,callback);
				}});
				return;
			
		}
		sendHttpRequest(commlist,callback);
	}
	
	/** 需要上送音频key数据的commomn PostParams */
	private Map<String, Object> usbKeyPostParams = null;
	private void combinSecurityData(Map<String,Object> postParams,Map<String,String> securityMap){
		BTCCMWApplication.isAudioKeySign = false;
		if(securityMap ==null||securityMap.size()<=0) {
			postParams.put("isSecurityTools", "0");
			return;
		}
		
		///还要组织安全因子。stamp,ssm
		if(BTCCMWApplication.security.containsKey("action")){
			postParams.put("security", BTCCMWApplication.security.get("action"));
			if("init".equals(BTCCMWApplication.security.get("action"))){
				BTCCMWApplication.isContainSecurityInit = true;
			}else{
				BTCCMWApplication.isContainSecurityInit = false;
			}
			 if("active".equals(BTCCMWApplication.security.get("action"))){
				BTCCMWApplication.isContainSecurityActive = true;

//				<stamp>AS_GN_AMOUNT_YE{display=I;k=cardNumber;type=s},AS_GN_AMOUNT_YE{display=V;k=chargeAmount;type=s}</stamp>
				if(BTCCMWApplication.security.containsKey("stamp")){
					String stamp = BTCCMWApplication.security.get("stamp");
					if(stamp.contains(",")){//多个
						String stapmSplit[] = stamp.split(",");
						for(int i =0;i<stapmSplit.length;i++){
							if(stapmSplit[i].indexOf("{")!=0){
								String stapmSp[] = stapmSplit[i].split("\\{");
								if(stapmSp[0].equals("cardNumber")){
									if(cmwApplication.getVar("cardNumber")!=null){
										postParams.put(stapmSp[0], cmwApplication.getVar("cardNumber"));
									}else{
										postParams.put(stapmSp[0], "");
									}
								}else if(stapmSp[0].equals("chargeAmount")){
									if(cmwApplication.getVar("chargeAmount")!=null){
										postParams.put(stapmSp[0], cmwApplication.getVar("chargeAmount"));
									}else{
										postParams.put(stapmSp[0], "");
									}
								}else if(!"".equals(stapmSp[0])){
									postParams.put(stapmSp[0], "");
								}
								
							}
						}
					}else{//1个
						String stapmSp[] = stamp.split("\\{");
						if(stapmSp.length!=1){
//							postParams.put(stapmSp[0], "");
							if(stapmSp[0].equals("cardNumber")){
								if(cmwApplication.getVar("cardNumber")!=null){
									postParams.put(stapmSp[0], cmwApplication.getVar("cardNumber"));
								}else{
									postParams.put(stapmSp[0], "");
								}
							}else if(stapmSp[0].equals("chargeAmount")){
								if(cmwApplication.getVar("chargeAmount")!=null){
									postParams.put(stapmSp[0], cmwApplication.getVar("chargeAmount"));
								}else{
									postParams.put(stapmSp[0], "");
								}
							}else if(!"".equals(stapmSp[0])){
								postParams.put(stapmSp[0], "");
							}
						}
					}
					
				}
			}else{
				BTCCMWApplication.isContainSecurityActive = false;
			}						
			 if("verify".equals(BTCCMWApplication.security.get("action"))){
				
				 //必须放在前面处理，否则将会被覆盖Smc,Otp 的赋值
				 if("96".equals(BaseDroidApp.getInstanse().getSecurityChoosed()) )// 硬件绑定时，需要上送设备指纹
					 {
						if(BTCCMWApplication.usbKeyText != null) {
							BTCCMWApplication.usbKeyText.InitUsbKeyResult(postParams);
						}
					}
				 
				if(cmwApplication.getVar("Smc")!=null){
					postParams.put("Smc", cmwApplication.getVar("Smc"));
					postParams.put("Smc_RC", cmwApplication.getVar("Smc_RC"));
				}
				if(cmwApplication.getVar("Otp")!=null){
					postParams.put("Otp", cmwApplication.getVar("Otp"));
					postParams.put("Otp_RC", cmwApplication.getVar("Otp_RC"));
				}
				if(cmwApplication.getVar("_signedData")!=null){
					//postParams.put("", "");
					usbKeyPostParams = postParams;
					BTCCMWApplication.isAudioKeySign = true;
				}
				
				
//				if(cmwApplication.getVar("deviceInfo")!=null){
//					postParams.put("", "");
//				}
				
				
				if(BTCCMWApplication.security.containsKey("stamp")){
					String stamp = BTCCMWApplication.security.get("stamp");
//					"diagnosisCardNo{display=I;k=cardNumber;type=s},registration{display=V;k=registerAmount;type=s}"
					if(stamp.contains(",")){//多个
						String stapmSplit[] = stamp.split(",");
						for(int i =0;i<stapmSplit.length;i++){
							if(stapmSplit[i].indexOf("{")!=0){
//								diagnosisCardNo{display=I;k=cardNumber;type=s}
								String stapmSp[] = stapmSplit[i].split("\\{");
								String stapss[] = stapmSp[1].split(";");
								String ks="";
								for(int k = 0; k<stapss.length;k++){
									if(stapss[k].contains("k")){
										String kss[]  = stapss[k].split("=");
										ks = kss[1];
									}
								}
								if(cmwApplication.getVar(ks)!=null){
									postParams.put(stapmSp[0], cmwApplication.getVar(ks));
								}else{
									postParams.put(stapmSp[0], "");
								}
//								if(stapmSp[0].equals("cardNumber")){
//									if(cmwApplication.getVar("cardNumber")!=null){
//										postParams.put(stapmSp[0], cmwApplication.getVar("cardNumber"));
//									}else{
//										postParams.put(stapmSp[0], "");
//									}
//								}else if(stapmSp[0].equals("chargeAmount")){
//									if(cmwApplication.getVar("chargeAmount")!=null){
//										postParams.put(stapmSp[0], cmwApplication.getVar("chargeAmount"));
//									}else{
//										postParams.put(stapmSp[0], "");
//									}
//								}else{
//									postParams.put(stapmSp[0], "");
//								}
								
							}
						}
					}else{//1个
						String stapmSp[] = stamp.split("\\{");
						if(stapmSp.length!=1){
							String stapss[] = stapmSp[1].split(";");
							String ks="";
							for(int k = 0; k<stapss.length;k++){
								if(stapss[k].contains("k")){
									String kss[]  = stapss[k].split("=");
									ks = kss[1];
								}
							}
							if(cmwApplication.getVar(ks)!=null){
								postParams.put(stapmSp[0], cmwApplication.getVar(ks));
							}else{
								postParams.put(stapmSp[0], "");
							}
//							if(stapmSp[0].equals("cardNumber")){
//								if(cmwApplication.getVar("cardNumber")!=null){
//									postParams.put(stapmSp[0], cmwApplication.getVar("cardNumber"));
//								}else{
//									postParams.put(stapmSp[0], "");
//								}
//							}else if(stapmSp[0].equals("chargeAmount")){
//								if(cmwApplication.getVar("chargeAmount")!=null){
//									postParams.put(stapmSp[0], cmwApplication.getVar("chargeAmount"));
//								}else{
//									postParams.put(stapmSp[0], "");
//								}
//							}else{
//								postParams.put(stapmSp[0], "");
//							}
						}
					}
					
					
				}
			}
		}
		
	////还要对value进行处理。卡号：cardNumber ；充值金额：chargeAmount
		if(BTCCMWApplication.security.containsKey("stamp")){
			String stamp = BTCCMWApplication.security.get("stamp");
			if(stamp.contains("cardNumber")){
				//用传过来的数据，取出相应的值进行替换cardNumber;
				
			}
			
			if(stamp.contains("chargeAmount")){
				
			}
			postParams.put("_combinId", BaseDroidApp.getInstanse().getSecurityChoosed());//安全因子
			postParams.put("stamp", stamp);
		}
		
		if(BTCCMWApplication.security.containsKey("ssm")){
			String ssm = BTCCMWApplication.security.get("ssm");
			postParams.put("ssm", ssm);
		}
		postParams.put("isSecurityTools", "1");
	
	}
	
	
	/**
	 * comm请求
	 * @param requestlist
	 * @param callback
	 */
	private void sendHttpRequest(final List<BTCCommbean> requestlist, final IExecuteFuction callback){
		if(BTCCMWApplication.isAudioKeySign) {
			if(BTCCMWApplication.usbKeyText != null)
			{
				BTCCMWApplication.usbKeyText.checkDataUsbKey(null, null, new IUsbKeyTextSuccess(){

					@Override
					public void SuccessCallBack(String result, int errorCode) {
						// TODO Auto-generated method stub
						BTCCMWApplication.isAudioKeySign = false;
						if(usbKeyPostParams != null)
							usbKeyPostParams.put("_signedData", result);
						
						
						sendHttpRequest(requestlist,callback);
					}
					
				});
				
				return;
			}
		}
//		BTCCMWApplication.resultmap.clear();
		if(requestlist.size() <= 0) {
			if(callback != null)
				callback.executeResultCallBack(true);
			return;
		}
		uiActivity.requestPeopleService(requestlist.get(0), new IHttpCallBack(){

			@Override
			public void requestHttpSuccess(Object result) {
				ResponseDataHandle(result);
				requestlist.remove(0);
				sendHttpRequest(requestlist,callback);
				
			}
			
		});
	}
	


	/**
	 * active 随机数请求
	 */
	private void requestActiveRandom(final IHttpCallBack callback){
		//请求安全因子，随机数。
		uiActivity.requestRandomCallBack(new IHttpCallBack() {
			
			@Override
			public void requestHttpSuccess(Object result) {
				handleRandomRequest(result);
				if(callback != null)
					callback.requestHttpSuccess(null);
			}
		});
	}
	
	/**
	 * 随机数请求处理
	 */
	private void handleRandomRequest(Object result){
		try {
			BiiResponse biiResponse = (BiiResponse) result;
			List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
			BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
			String randomNumber =(String) biiResponseBody.getResult();
			BTCCMWApplication.securityRandom = randomNumber;
			BaseHttpEngine.dissMissProgressDialog();
			LogGloble.i("info123","BTCCMWApplication.securityRandom =="+randomNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * init 中factory serviceid 请求
	 */
	private void requestInitFactory(final IExecuteFuction callback){
//      所有init走的请求流程。
		String serviceId = "PB151";
		uiActivity.requestGetSecurityFactorFromAction(new IHttpCallBack() {
			
			@Override
			public void requestHttpSuccess(Object result) {
				sendHttpRequest(commlist,callback);
			}
		}, serviceId);
		isRequestServiceId = true;
	}

	@Override
	public boolean awaitExecute(Map<String, String> dbMap, View view,
			IExecuteFuction executeCallBack) {
		requestHttpFromAction(dbMap,view,executeCallBack);
		return false;
	}
	
	
}

