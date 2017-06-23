package com.chinamworld.bocmbci.biz.plps.liveservice;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.peopleservice.ui.BTCUiActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
import com.chinamworld.bocmbci.boc.ModelBoc;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 常用缴费详情
 * @author zxj
 *
 */
public class CommServiceDetailActivity extends PlpsBaseActivity{
	//流程文件id
	private String flowFileId=null;
	//是否为常用缴费项目
	private String isOftenUse;
	//缴费项目是否有效
	private String isAvalid;
	/**民生解析需要字段*/
	//是否回显数据域
	private String isDisp;
	//步骤名称
	private String stepName;
	//回显域键值结合
	private Map<String, Object> dispMap;
	/**民生解析需要字段*/
	//是否可继续缴费
	private Button isContinueButton;
	//是否为常用缴费项目
	private Button isOftenButton;
	/**是否是加入常用缴费项目标识*/
	private Boolean isAddCommService = false;
	/**是否支持7*24小时服务*/
	private String isAllDayService;
	/**服务开始时间*/
	private String startTime;
	/**服务结束时间*/
	private String endTime;
	/**加入常用成功标识*/
	private Boolean isAddCommSuccess = false;
	// 常用缴费项目
	private List<Map<String, Object>> commonPaymentList = new ArrayList<Map<String, Object>>();
	/**服务小类名*/
	private String itemName;
	/**服务大类id*/
	private String menuName;
	/**flowName*/
	private String flowName;
	/**省名称*/
	private String prvcName;
	/**省简称*/
	private String prvcShortName;
	/**此文件ID对应的缴费项目是否在本终端可用*/
	private String valFlag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.plps_payment_detail);
		valFlag = getIntent().getStringExtra("valFlag");
//		requestPsnPlpsQueryCommonUsedPaymentList();
		inflateLayout(R.layout.plps_live_detail);
		setUpView();
	}
//	private void requestPsnPlpsQueryCommonUsedPaymentList() {
////		requestCommConversationId(new IHttpCallBack(){
////			@Override
////			public void requestHttpSuccess(Object result) {
//				// TODO Auto-generated method stub
//				BaseHttpEngine.showProgressDialog();
//				BiiRequestBody biiRequestBody = new BiiRequestBody();
//				biiRequestBody.setMethod(Plps.QUERYCOMMONUSEDPAYMENT);
//				biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
//				biiRequestBody.setParams(null);
//				HttpManager.requestBii(biiRequestBody, CommServiceDetailActivity.this,
//						"requestPsnPlpsQueryCommonUsedPaymentListCallBack");
////			}});
//	}
//	@SuppressWarnings("unchecked")
//	public void requestPsnPlpsQueryCommonUsedPaymentListCallBack(
//			Object resultObj) {
//		BaseHttpEngine.dissMissProgressDialog();
//		Map<String, Object> resultPayment = (Map<String, Object>) PlpsUtils
//				.httpResponseDeal(resultObj);
//		commonPaymentList.clear();
//		commonPaymentList = (List<Map<String, Object>>) resultPayment
//				.get(Plps.PAYMENTLIST);
////		if (StringUtil.isNullOrEmpty(commonPaymentList)||!StringUtil.isNullOrEmpty(commonPaymentList)) {
////			inflateLayout(R.layout.plps_live_detail);
////			setUpView();
////		}
//		if(StringUtil.isNullOrEmpty(commonPaymentList)||!StringUtil.isNullOrEmpty(commonPaymentList)){
//			if(isOftenUse.equals("1")){
//				BaseDroidApp.getInstanse().showMessageDialog("该缴费项目已经加入常用", new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						BaseDroidApp.getInstanse().dismissMessageDialog();
//					}
//				});
//			}else {
//				//校验是否可以加入常用
//				if(commonPaymentList.size() < 8||StringUtil.isNullOrEmpty(commonPaymentList)){
//					if (isAddCommSuccess) {
//						BaseDroidApp.getInstanse().showMessageDialog("该缴费项目已经加入常用", new OnClickListener() {
//							
//							@Override
//							public void onClick(View v) {
//								// TODO Auto-generated method stub
//								BaseDroidApp.getInstanse().dismissMessageDialog();
//							}
//						});
//					}else {
//						BaseDroidApp.getInstanse().showErrorDialog("是否把该缴费加入常用服务",
//								R.string.cancle, R.string.confirm, new OnClickListener() {
//
//									@Override
//									public void onClick(View v) {
//										// TODO Auto-generated method stub
//										BaseDroidApp.getInstanse().dismissErrorDialog();
//										switch (v.getId()) {
//										case R.id.exit_btn:// 取消
//
//											break;
//										case R.id.retry_btn:// 确定
//											isAddCommService = true;
//											//请求token
//											pSNGetTokenId();
//											// 新增常用缴费项目
////											requestAddCommonUsedPaymentList();
//											break;
//										default:
//											break;
//										}
//									}
//								});
//					}
//					
//				}else {
//					BaseDroidApp.getInstanse().showMessageDialog("对不起，常用服务项目最多只能设置8个", new OnClickListener() {
//						
//						@Override
//						public void onClick(View v) {
//							// TODO Auto-generated method stub
//							BaseDroidApp.getInstanse().dismissMessageDialog();
//						}
//					});
//				}
//			}
//		}
//	}
//	
	private void setUpView(){
		Map<String, Object> map = PlpsDataCenter.getInstance().getHistoryRecodDetail();
		flowFileId = (String)map.get(Plps.FLOWFILEID);
		isOftenUse = (String)map.get(Plps.ISOFTENUSE);
		isAvalid = (String)map.get(Plps.ISAVALID);
		isDisp = (String)map.get(Plps.ISDISP);
		stepName = (String)map.get(Plps.STEPNAME);
		dispMap = (Map<String, Object>)map.get(Plps.DISMAP);
		isAllDayService = (String)map.get(Plps.ALLDAYSERVICE);
		LinearLayout btn_layout = (LinearLayout)findViewById(R.id.btn_layout);
		if(valFlag.equals("0")){
			btn_layout.setVisibility(View.GONE);
		}
		((TextView) findViewById(R.id.paydate)).setText((String)map.get(Plps.TRANSTIME));
		((TextView) findViewById(R.id.province)).setText((String)map.get(Plps.PRVCNAME));
		((TextView) findViewById(R.id.city)).setText((String)map.get(Plps.CITYNAME));
		((TextView) findViewById(R.id.servicename)).setText((String)map.get(Plps.ITEMNAME));
		((TextView) findViewById(R.id.flowName)).setText((String)map.get(Plps.FLOWFILENAME));
		String summary = (String)map.get(Plps.SUMMARY);
		/**将缴费账号12-19位进行格式化*/
		String[] summarys = summary.split(",|\\，");
		for(int i=0; i<summarys.length; i++){
			String account = summarys[i];
			String[] accounts = account.split(":|\\：");
			if(accounts.length>1){
				String accountNum = accounts[1];
				if(accountNum.matches("^[\\d]{12,19}$")){
					String newAccountNum = accountNum;
					if (accountNum.length() >= 12 && accountNum.length()<=19) {
						newAccountNum = StringUtil.getForSixForString(accountNum);
					}
					summary = summary.replace(accountNum, newAccountNum);
				}	
			}
		}
		((TextView) findViewById(R.id.summary)).setText(summary);
		isContinueButton = (Button)findViewById(R.id.continuep);
		isContinueButton.setOnClickListener(this);
//		if(isAvalid.equals("1")){
//			isContinueButton.setOnClickListener(this);
//		}else {
//			isContinueButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_blue_big_press));
//			isContinueButton.setOnClickListener(null);
//		}
		
		isOftenButton = (Button)findViewById(R.id.addof);
		isOftenButton.setOnClickListener(this);
//		if(isOftenUse.equals("1")){
//			isOftenButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_blue_big_press));
//			isOftenButton.setOnClickListener(null);
//		}else {
//			isOftenButton.setOnClickListener(this);
//		}
		startTime = (String)map.get(Plps.STARTTIME);
		endTime = (String)map.get(Plps.ENDTIME);
		itemName = (String)map.get(Plps.ITEMNAME);
//		PlpsDataCenter.getInstance().setCatName(itemName);
		menuName = (String)map.get(Plps.MENUNAME);
//		PlpsDataCenter.getInstance().setMenuName(menuName);
		flowName = (String)map.get(Plps.FLOWNAME);
		prvcName = (String)map.get(Plps.PRVCNAME);
		prvcShortName = (String)map.get(Plps.PRVCSHORTNAME);
		PlpsUtils.setOnShowAllTextListener(this, (TextView) findViewById(R.id.paydate),
				(TextView) findViewById(R.id.province),
				(TextView) findViewById(R.id.city),
				(TextView) findViewById(R.id.servicename),
				(TextView) findViewById(R.id.flowName)
				);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		super.onClick(v);
		/**去除省名称可能两边存在的空格*/
		String prvcNamet = prvcName.trim();
		switch (v.getId()) {
		case R.id.continuep:
//			startActivity(new Intent(CommServiceDetailActivity.this, PaymentAllProject.class));	
			
			if(PlpsDataCenter.provList.contains(prvcNamet)){
				if(isAvalid.equals("1")){
					if(isAllDayService.equals("1")){
//						Intent intent = new Intent(
//								CommServiceDetailActivity.this,
//								BTCUiActivity.class);
////						intent.putExtra("flowFileId", flowFileId);
//						startActivity(intent);
						BTCUiActivity.IntentToParseXML(CommServiceDetailActivity.this, prvcShortName,flowFileId, itemName, menuName, flowName, isDisp, stepName, dispMap,(String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
					}else {
						String dateTime = PlpsDataCenter.getInstance().getSysDate();
						Boolean isService = QueryDateUtils.compareStartDateToEndDate(startTime,endTime,dateTime);
						if(!isService){
//							Intent intent = new Intent(
//									CommServiceDetailActivity.this,
//									BTCUiActivity.class);
////							intent.putExtra("flowFileId", flowFileId);
//							startActivity(intent);
							BTCUiActivity.IntentToParseXML(CommServiceDetailActivity.this, prvcShortName,flowFileId, itemName, menuName, flowName, isDisp, stepName, dispMap, (String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
						}else {
//							BaseDroidApp.getInstanse().showMessageDialog("对不起，该缴费项目不在服务时间", new OnClickListener() {
//								
//								@Override
//								public void onClick(View v) {
//									// TODO Auto-generated method stub
//									BaseDroidApp.getInstanse().dismissMessageDialog();
//								}
//							});
							String startTime1 = startTime.substring(0, 2);
							String startTime2 = startTime.substring(2, 4);
							String startTimes = startTime1 + ":" + startTime2;
							String endTime1 = endTime.substring(0, 2);
							String endTime2 = endTime.substring(2, 4);
							String endTimes = endTime1 + ":" + endTime2;
							BaseDroidApp
									.getInstanse()
									.showMessageDialog(
											CommServiceDetailActivity.this
													.getString(R.string.plps_not_service_time)
													+ startTimes
													+ "--"
													+ endTimes
													+ CommServiceDetailActivity.this
															.getString(R.string.plps_not_service_timet),
											new OnClickListener() {

												@Override
												public void onClick(View paramView) {
													// TODO Auto-generated method stub
													BaseDroidApp.getInstanse()
															.dismissMessageDialog();
												}
											});
						}
					}
				}else {
					BaseDroidApp.getInstanse().showMessageDialog("该缴费项目无效", new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							BaseDroidApp.getInstanse().dismissMessageDialog();
						}
					});
				}
			}else {
				BaseDroidApp.getInstanse().showMessageDialog("手机渠道暂不支持该地区的民生缴费功能，您可以使用账单缴付功能或者通过网银渠道进行相关缴费", new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						BaseDroidApp.getInstanse().dismissMessageDialog();
					}
				});
			}
			
			break;
		case R.id.addof:
//			requestPsnPlpsQueryCommonUsedPaymentList()
				if(isOftenUse.equals("1")){
					BaseDroidApp.getInstanse().showMessageDialog("该缴费项目已经加入常用", new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							BaseDroidApp.getInstanse().dismissMessageDialog();
						}
					});
				}else {
						if (isAddCommSuccess) {
							BaseDroidApp.getInstanse().showMessageDialog("该缴费项目已经加入常用", new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									BaseDroidApp.getInstanse().dismissMessageDialog();
								}
							});
						}else {
							BaseDroidApp.getInstanse().showErrorDialog("是否把该缴费加入常用服务",
									R.string.cancle, R.string.confirm, new OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											BaseDroidApp.getInstanse().dismissErrorDialog();
											switch (v.getId()) {
											case R.id.exit_btn:// 取消

												break;
											case R.id.retry_btn:// 确定
												isAddCommService = true;
												//请求token
												pSNGetTokenId();
												// 新增常用缴费项目
//												requestAddCommonUsedPaymentList();
												break;
											default:
												break;
											}
										}
									});
						}
						
					}
			break;
		case R.id.ib_back:
			if (isAddCommSuccess) {
				setResult(10111);
				finish();
			}else {
				setResult(RESULT_OK);
				finish();
			}
			
			break;
		case R.id.ib_top_right_btn:
			PlpsDataCenter.getInstance().clearAllData();
			ActivityTaskManager.getInstance().removeAllActivity();
			break;

		default:
			break;
		}
	}
	/**
	 * 删除记录
	 * @param v
	 */
	public void deleteOnclick(View v){
		showNotiDialog();
	}
	/**
	 * 添加常用缴费项目*/
	private void requestAddCommonUsedPaymentList(String tokenId){
//		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.ADDCOMMONUSEDPAYMENT);
		biiRequestBody.setConversationId((String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Plps.FLOWFILEID, flowFileId);
		params.put(Plps.TOKEN, tokenId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestAddCommonUsedPaymentListCallBack");
	}
	public void requestAddCommonUsedPaymentListCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		BaseDroidApp.getInstanse().showMessageDialog("添加常用服务成功", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BaseDroidApp.getInstanse().dismissMessageDialog();
				ActivityTaskManager.getInstance().removeAllActivity();
				ModelBoc.onAddCommonUsePaymentSuccess();
			}
		});
		isAddCommSuccess = true;
//		isOftenButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_blue_big_press));
//		isOftenButton.setOnClickListener(null);
	}
	
	private void showNotiDialog(){
		BaseDroidApp.getInstanse().showErrorDialog(getString(R.string.plps_service_delete_tip),
				R.string.cancle, R.string.confirm,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						switch (Integer.parseInt(v.getTag() + "")) {
						case CustomDialog.TAG_SURE:
							BaseDroidApp.getInstanse().dismissErrorDialog();
							BaseHttpEngine.showProgressDialog();
							//请求token
							pSNGetTokenId();
							break;
						case CustomDialog.TAG_CANCLE:
							BaseDroidApp.getInstanse().dismissErrorDialog();
							break;
						}
					}
				});
	}
	/**
	 * 请求token
	 */
	public void pSNGetTokenId() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenId");
	}
	
	public void aquirePSNGetTokenId(Object resultObj) {
		String tokenId = (String) this.getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog(); return;
		}
		if(isAddCommService){
			isAddCommService = false;
			// 新增常用缴费项目
			requestAddCommonUsedPaymentList(tokenId);
		}else {
			requestDeleteRecord(tokenId);
		}
	}
	
	/**
	 * 请求删除记录
	 * @param tokenId
	 */
	private void requestDeleteRecord(String tokenId){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.METHODDELETEHISTORYRECORD);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put(Plps.TOKEN, tokenId);
		param.put(Plps.RECORDID, PlpsDataCenter.getInstance().getHistoryRecodDetail().get(Plps.RECORDID));
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "deleteRecordCallBack");
	}
	
	public void deleteRecordCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
//		CustomDialog.toastShow(this, "加入成功！");
//		setResult(111);
//		finish();
		BaseDroidApp.getInstanse().showMessageDialog("删除记录成功",
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						setResult(111);
						BaseDroidApp.getInstanse().dismissMessageDialog();	
						finish();
					}
				});
	}
}
