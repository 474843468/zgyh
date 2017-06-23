package com.chinamworld.bocmbci.biz.tran.weixin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.pm.PackageManager.NameNotFoundException;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.fidget.BTCGlobal;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpObserver;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.http.engine.MbcgHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.DateUtils;
import com.chinamworld.bocmbci.utils.DeviceUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 微信抽奖
 * 
 * @author
 * 
 */
public class Weixin implements HttpObserver {
	public static Weixin wv;
	private View paretnview;
	private TextView weixinRaffleTv;
	private BaseActivity context;
	private WeixinSuccess weixincallBack;
	/** 转账抽奖时间 */
	private String weixin_raffle_startTime = "";
	private String weixin_raffle_endTime = "";
	private String weixin_crcd_startTime = "";
	private String weixin_crcd_endTime = "";
	
	private boolean weixinRaffleisUsed = false;
	private boolean weixinCrcdflagisUsed = false;
	
	/** 服务器时间 */
	protected String dateTime;
	/** 是否在抽奖活动的有效日期内 */
	private boolean weixinRaffleflag = false;
	private boolean weixinCrcdflag = false;
	protected HttpTools httpTools;

	/** 客户编号 */
	private static String CustNo = "custNo";
	/** 产品名称 */
	private static String ProductName = "productName";
	/** 版本号 */
	private static String VersionNo = "versionNo";
	/** 设备型号 */
	private static String Model = "model";
	/** 运行内存 */
	private static String RAM = "RAM";
	/** 存储空间 */
	private static String ROM = "ROM";
	/** 已使用存储空间 */
	private static String UsedROM = "usedROM";
	/** 分辨率 */
	private static String Resolution = "resolution";
	/** 系统版本 */
	private static String OsVersion = "osVersion";

	private String transactionId;
	/*卡类型*/
	private String type;
//	/*金额*/
	private double count;
	
	
	private int index = 0;
	public static Weixin getInstance() {
		if (wv == null) {
			wv = new Weixin();

		}
		return wv;
	}

	public void doweixin(View result, BaseActivity cont,
			WeixinSuccess callBack, String trantionId/*,OnClickListener right,OnClickListener confirm*/,String type,double count) {
		this.context = cont;
		this.paretnview = result;
		this.weixincallBack = callBack;
		this.transactionId = trantionId;
		this.type=type;
		this.count=count;
		
		weixin_raffle_startTime = "";
		weixin_raffle_endTime = "";
		weixin_crcd_startTime = "";
		weixin_crcd_endTime = "";
		weixinRaffleisUsed = false;
		weixinCrcdflagisUsed = false;
		weixinRaffleflag = false;
		weixinCrcdflag =false;
		
		
//		if(LocalData.cardList.contains(type)){
//			this.type="C";	
//		}else{
//			this.type="T";	
//		}
		BaseHttpEngine.showProgressDialog();

		/** 微信抽奖获取活动时间 */
		sendGetActivityAction();
		
		

	}

	/***
	 * 微信抽奖 获取抽奖活动时间
	 */
	public void sendGetActivityAction() {
		// 自定义头信息
		Map<String, Object> headerMap = new HashMap<String, Object>();
		headerMap.put(CustNo, "");// 核心系统客户号
		headerMap.put(ProductName, ConstantGloble.APP_USER_AGENT_PRODUCT_NAME);
		headerMap.put(VersionNo, SystemConfig.APP_VERSION);
		headerMap.put(Model, android.os.Build.MODEL);
		headerMap.put(RAM, "");
		headerMap.put(ROM, "");
		headerMap.put(UsedROM, DeviceUtils.getAvailableInternalMemorySize());
		headerMap.put(Resolution, "");
		headerMap.put(OsVersion, android.os.Build.VERSION.RELEASE);
		headerMap.put("firstUse", "");
		// headerMap.put(DeviceNo, "2");

		Map<String, Object> login = new HashMap<String, Object>();
		login.put("productName", ConstantGloble.APP_USER_AGENT_PRODUCT_NAME);

		Map<String, Object> par = new HashMap<String, Object>();
		par.put("method", "GetActivityAction");
		par.put("header", headerMap);
		par.put("params", login);
		MbcgHttpEngine mMbcgHttpEngine = new MbcgHttpEngine();
		mMbcgHttpEngine.setHasIMEI(false);
		HttpManager.requestString(SystemConfig.ActivityInfo_Url,
				ConstantGloble.GO_METHOD_POST, par, mMbcgHttpEngine, this,
				"weixinsendGetActivityActionCallback");
	}

	/***
	 * 微信抽奖 获取抽奖活动时间回调
	 */
	@SuppressWarnings("unchecked")
	public void weixinsendGetActivityActionCallback(Object resultObj) {

		if (resultObj != null && !"null".equals(resultObj)) {
			Map<String, Object> result = (Map<String, Object>) ((Map<String, Object>) resultObj)
					.get("result");
			List<Map<String, Object>> actionList = (List<Map<String, Object>>) result
					.get("activityList");
			/** 循环遍历获取当前活动信息 */
			
			for(Map<String, Object> item :actionList){
				if("T".equals(item.get("keyId"))){
					/*转账*/
					String isUsed = item.get("isUsed") + "";
					if ("1".equals(isUsed)) {
						/** 存储当前活动的开始时间和结束时间 */
						/**
						 * "startTime": "2015-06-25 11:14:07","endTime":
						 * "2015-06-26 11:14:05"
						 */
						weixinRaffleisUsed=true;
						String _startTime = item.get("startTime") + "";
						String _endTime = item.get("endTime") + "";
						weixin_raffle_startTime = _startTime.replace(" ", "")
								.replace("-", "").replace(":", "");
						weixin_raffle_endTime = _endTime.replace(" ", "")
								.replace("-", "").replace(":", "");
						
					}
				}
				if("C".equals(item.get("keyId"))){
					/*信用卡*/
					String isUsed = item.get("isUsed") + "";
					if ("1".equals(isUsed)) {
						
						/** 存储当前活动的开始时间和结束时间 */
						/**
						 * "startTime": "2015-06-25 11:14:07","endTime":
						 * "2015-06-26 11:14:05"
						 */
						weixinCrcdflagisUsed=true;
						String _startTime = item.get("startTime") + "";
						String _endTime = item.get("endTime") + "";
						weixin_crcd_startTime = _startTime.replace(" ", "")
								.replace("-", "").replace(":", "");
						weixin_crcd_endTime = _endTime.replace(" ", "")
								.replace("-", "").replace(":", "");
						
					}
				}
				
			}
//			Map<String, Object> item = actionList.get(0);
//			String isUsed = item.get("isUsed") + "";
//			if ("1".equals(isUsed)) {
//				/** 存储当前活动的开始时间和结束时间 */
//				/**
//				 * "startTime": "2015-06-25 11:14:07","endTime":
//				 * "2015-06-26 11:14:05"
//				 */
//				String _startTime = item.get("startTime") + "";
//				String _endTime = item.get("endTime") + "";
//				weixin_raffle_startTime = _startTime.replace(" ", "")
//						.replace("-", "").replace(":", "");
//				weixin_raffle_endTime = _endTime.replace(" ", "")
//						.replace("-", "").replace(":", "");
//			} else {
//				resultObj = "null";
//			}

//			if ("".equals(weixin_raffle_endTime)
//					|| weixin_raffle_endTime == null) {
//				resultObj = "null";
//			}
		}

//		if ("null".equals(resultObj)) {
//			BaseHttpEngine.dissMissProgressDialog();
//			weixincallBack.SuccessCallBack(false);
//
//		} else {
//			/** 微信抽奖获取系统时间 */
//			requestSystemDateTime();
//
//		}
		
		if(weixinRaffleisUsed
		  ||weixinCrcdflagisUsed
				){
			/** 微信抽奖获取系统时间 */
			requestSystemDateTime();
		}else{
			BaseHttpEngine.dissMissProgressDialog();
			weixincallBack.SuccessCallBack(false);	
		}
	}

	/**
	 * 请求系统时间
	 */
	public void requestSystemDateTime() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_SYSTEM_TIME);
		HttpManager.requestBii(biiRequestBody, this,
				"weixinrequestSystemDateTimeCallBack");
	}

	public void weixinrequestSystemDateTimeCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		dateTime = (String) resultMap.get(Comm.DATETME);
		String _dateTime = dateTime.replace(" ", "").replace("/", "")
				.replace(":", "");
		try {
			
			/** 判断系统当前时间是否包含在抽奖的时间内 */
			weixinCrcdflag = DateUtils.dateCompare(_dateTime,
					weixin_crcd_startTime, weixin_crcd_endTime);
			
		} catch (Exception e) {

			weixinCrcdflag = false;
		}
		try {
			
			/** 判断系统当前时间是否包含在抽奖的时间内 */
			weixinRaffleflag = DateUtils.dateCompare(_dateTime,
					weixin_raffle_startTime, weixin_raffle_endTime);
			
		} catch (Exception e) {

			weixinRaffleflag = false;
		}

//		weixinRaffleflag = true;
		if (("C".equals(type)&&weixinCrcdflag)// 信用卡
			||("T".equals(type)&&weixinRaffleflag&&count>=Tran.WEIXIN_RAFFLE_AMOUNT)// 转账
			||("CT".equals(type)&&(weixinCrcdflag||(weixinRaffleflag&&count>=Tran.WEIXIN_RAFFLE_AMOUNT))) // 信用卡及转账
				) {
			// 消息推送取票接口 TODO
//			sendPsnGetTicketForMessage(transactionId);
			BaseHttpEngine.dissMissProgressDialog();
			weixincallBack.SuccessCallBack(true);			
			weixinRaffleTv = (TextView) paretnview
					.findViewById(R.id.tran_weixin_raffle_tv);
			weixinRaffleTv.setVisibility(View.VISIBLE);
			weixinRaffleTv.postInvalidate();
			weixinRaffleTv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					BaseDroidApp.getInstanse().showErrorDialog(
							getWXRaffleCue(0, transactionId), R.string.close,
							R.string.confirm, new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									switch ((Integer) v.getTag()) {
									case CustomDialog.TAG_CANCLE:
										BaseDroidApp.getInstanse()
												.dismissMessageDialog();
										break;
									case CustomDialog.TAG_SURE:
										BaseDroidApp.getInstanse()
												.dismissMessageDialog();
										skipWeixin();
										break;
									}

								}
							});
				}
			});
		} else {
			BaseHttpEngine.dissMissProgressDialog();
			weixincallBack.SuccessCallBack(false);
		}

	}

	/***
	 * 消息推送取票接口
	 * 
	 * @param tranTime
	 *            交易时间
	 * @param strTransaction
	 *            交易号
	 */
//	public void sendPsnGetTicketForMessage(String strTransaction) {
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(Tran.PSNGETTICKETFORMESSAGE);
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("ticketId", strTransaction);
//		biiRequestBody.setParams(params);
//		HttpManager.requestBii(biiRequestBody, this,
//				"requestPsnGetTicketForMessageCallBack");
//	}

	/***
	 * 消息推送取票接口 回调
	 * 
	 * @param resultObj
	 */
//	public void requestPsnGetTicketForMessageCallBack(Object resultObj) {
//
//		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
//		String ticket = result.get("ticket") + "";
//		String _dateTime = dateTime.replace(" ", "").replace("/", "")
//				.replace(":", "");
//		// TODO 注释微信银行接口
//		sendInsertTranSeq(transactionId, _dateTime, ticket);
//	}

	/***
	 * PsnGetTicketForMessage
	 * 
	 * @param seqNo
	 *            流水号
	 * @param tranTime
	 *            交易时间
	 * @param ticket
	 *            票内容
	 */
//	public void sendInsertTranSeq(String seqNo, String tranTime, String ticket) {
//		// BaseHttpEngine.showProgressDialog();
//		// 参数
//		Map<String, Object> paramsMap = new HashMap<String, Object>();
//		/** PsnCommonQueryOprLoginInfo 查询操作员信息接口返回数据 */
//		HashMap<String, Object> loginInfo = (HashMap<String, Object>) BaseDroidApp
//				.getInstanse().getBizDataMap()
//				.get(ConstantGloble.BIZ_LOGIN_DATA);
//		paramsMap.put(Tran.WEIXIN_CUSTID, loginInfo.get("cifNumber") + "");
//		paramsMap.put(Tran.WEIXIN_NAME, loginInfo.get("customerName") + "");
//		paramsMap.put(Tran.WEIXIN_PHONENO, loginInfo.get("mobile") + "");
//		paramsMap.put(Tran.WEIXIN_SEQNO, seqNo);
//		paramsMap.put(Tran.WEIXIN_TRANTIME, tranTime);
//		paramsMap.put(Tran.WEIXIN_TICKET, ticket);
//		;
//
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(Tran.INSERTTRANSEQ);
//		biiRequestBody.setParams(paramsMap);
//		HttpManager.requestBii(SystemConfig.WEIBANKPUBLICMODULE_PATH,
//				biiRequestBody, this, "sendInsertTranSeqCallback");
//
//	}

	/***
	 * PsnGetTicketForMessage 回调
	 * 
	 * @param resultObj
	 */
//	public void sendInsertTranSeqCallback(Object resultObj) {
//		BaseHttpEngine.dissMissProgressDialog();
//		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
//		// “success”为成功;“fail”为失败
//		String status = result.get("status") + "";
////		status = "success";
//		if ("success".equals(status)) {
//			// View view =
//			// LayoutInflater.from(context).inflate(R.layout.weixin_layout,
//			// null);
//			// ((ViewGroup) paretnview).addView(view);
//			weixincallBack.SuccessCallBack(true);
//			weixinRaffleflag = true;
//			/** 进入微信入口 */
//			weixinRaffleTv = (TextView) paretnview
//					.findViewById(R.id.tran_weixin_raffle_tv);
//			weixinRaffleTv.setVisibility(View.VISIBLE);
//			weixinRaffleTv.postInvalidate();
//			weixinRaffleTv.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					BaseDroidApp.getInstanse().showErrorDialog(
//							getWXRaffleCue(0, transactionId), R.string.close,
//							R.string.confirm, new View.OnClickListener() {
//
//								@Override
//								public void onClick(View v) {
//									switch ((Integer) v.getTag()) {
//									case CustomDialog.TAG_CANCLE:
//										BaseDroidApp.getInstanse()
//												.dismissMessageDialog();
//										break;
//									case CustomDialog.TAG_SURE:
//										BaseDroidApp.getInstanse()
//												.dismissMessageDialog();
//										skipWeixin();
//										break;
//									}
//
//								}
//							});
//				}
//			});
//		}else{
//			weixincallBack.SuccessCallBack(true);
//			
//		}
//		
//
//	}

	/**
	 * 
	 * @param flag
	 *            0 抽奖图标提示语 ；1完成/主界面 提示语
	 * @param transactionId
	 *            交易序号
	 * @return 提示语
	 */
	public String getWXRaffleCue(int flag, String transactionId) {
		
		if(StringUtil.isNullOrEmpty(transactionId)){
			transactionId="";
		}
		StringBuffer sb = new StringBuffer();
		if (flag == 0) {
			sb.append("\t\t\t亲爱的用户：您的转账交易金额已符合抽奖条件，请按照以下提示操作。\n\t\t\t1、记录并妥善保存您当前转账的交易序号");
			sb.append("\t\t\t\n" + transactionId + "\n");
			sb.append("作为抽奖凭证，点击“确定”即可进入微信。在公众号中搜索“中国银行微银行”进行关注后，点击页面下方“微生活”频道选择“幸运抽奖”进入活动页面，按提示输入交易序号后进行抽奖。\n\t\t\t2、欲了解更多活动信息，请关注我行微信公众号“中国银行微银行”。 ");
		} else {
			sb.append("亲爱的用户：您的本次转账交易金额已符合抽奖条件，可关闭本页面后，再点击“幸运大抽奖”图标进行操作。");
			// sb.append("\t\t\t\n"+transactionId+"\n");
			// sb.append("并阅读以下提示：\n\t\t\t在参与活动前，请确保您当前使用的手机已下载并开通微信客户端，如果您尚未关注我行微信公众号“中国银行微银行”，请按以下步骤操作（如您已关注我行微信，请忽略步骤二）：\n\t\t\t步骤一，使用您当前进行转账交易的手机进入微信客户端；\n\t\t\t步骤二，在公众号中搜索中国银行微银行进行关注；\n\t\t\t步骤三，点击页面下方“微生活”频道选择“幸运抽奖”进入活动页面，按提示输入交易序号后进行抽奖");
		}
		return sb.toString();
	}

	public void skipWeixin() {
		try {
			/* 判断微信是否安装 */
			if ((context.getPackageManager().getPackageInfo("com.tencent.mm",
					64)) == null) {
				Toast.makeText(context, "微信未安装！", Toast.LENGTH_SHORT).show();
				return;
			}

		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
			Toast.makeText(context, "微信未安装！", Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			context.startActivity(context.getPackageManager()
					.getLaunchIntentForPackage("com.tencent.mm"));// 打开微信，com.tencent.mm是微信的包名
			LogGloble.i("MainActivity", "start weixin");
		} catch (Exception e) {
			Toast.makeText(context, "请检查是否安装微信！", Toast.LENGTH_SHORT).show();

		}
	}

	

	
	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		if (resultObj instanceof BiiResponse) {
			BiiResponse biiResponse = (BiiResponse) resultObj;
			List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
			BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
			String method = biiResponseBody.getMethod();
			/** 抽奖取票、微信银行录入流水号等接口 */
			if (Tran.PSNGETTICKETFORMESSAGE.equals(method)
					|| Tran.INSERTTRANSEQ.equals(method)
					|| "GetActivityAction".equals(method)) {
				if (biiResponse.isBiiexception()) {// 代表返回数据异常
					BiiHttpEngine.dissMissProgressDialog();
					weixinRaffleflag = false;
					weixincallBack.SuccessCallBack(false);
				}
				return false;
			}
			
		}
		return false;
	}

	@Override
	public boolean httpRequestCallBackAfter(Object resultObj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean httpCodeErrorCallBackPre(String code) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean httpCodeErrorCallBackAfter(String code) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void commonHttpErrorCallBack(String requestMethod) {
		 if(Tran.PSNGETTICKETFORMESSAGE.equals(requestMethod)
					|| Tran.INSERTTRANSEQ.equals(requestMethod)
					|| "GetActivityAction".equals(requestMethod)){
			 BaseHttpEngine.dissMissProgressDialog();
			 weixinRaffleflag = false;
			 weixincallBack.SuccessCallBack(false);
			}else{
				context.commonHttpErrorCallBack(requestMethod);
			}

	}

	@Override
	public void commonHttpResponseNullCallBack(String requestMethod) {
		// TODO Auto-generated method stub

	}

}
