package com.chinamworld.bocmbci.biz.plps.smallservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.peopleservice.ui.BTCUiActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsTools;
import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
import com.chinamworld.bocmbci.boc.ModelBoc;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PublicTools;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 选择商户页面
 * @author Administrator zxj*/
public class LowServiceActivity extends PlpsBaseActivity implements OnItemSelectedListener{
	
	private Spinner merchantName;
	//商户名称
	private String flowFileName;
	//是否支持7*24小时服务
	private String allDayService;
	private Boolean isAllDayService= false;
	//是否支持跨省缴费
	private String otherProvPay;
	private Boolean isOtherProvPay = false;
	//是否支持信用卡
	private String crcdPay;
	private Boolean isCrcdPay = false;
	//商户服务开始时间
	private String startTime;
	//商户服务结束时间
	private String endTime;
	//流程文件id
	private String flowFileId;
	/**商户选择标识*/
	private int selectedPosition;
	/**商户简介*/
	private TextView flowMerchants;
	/**商户简介地址*/
	private String infoUrl;
	/**常用缴费项目*/
	List<Map<String, Object>> commPaymentList = new ArrayList<Map<String,Object>>();
	/**重复添加FlowId标识*/
	private String flowFileIdSelection;
	Map<String, Object> flowIdMap = new HashMap<String, Object>();
	/**小类置灰标识*/
	private String isAlailiablet;
	/**从添加常用入口 标识*/
//	private Boolean Flag = false;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setTitle(R.string.plps_low_service_name);
		setTitle(PlpsDataCenter.getInstance().getCatName());
		commPaymentList = (List<Map<String,Object>>)PlpsDataCenter.getInstance().getCommonPaymentList();
		cityAdress.setVisibility(View.GONE);
		mRightButton.setVisibility(View.VISIBLE);
		isAlailiablet = getIntent().getStringExtra(Plps.ISAVAILIABLE);
//		Flag = getIntent().getBooleanExtra("Flag", false);
		inflateLayout(R.layout.plps_low_service);
		if(PlpsTools.isAddCommService){
			findViewById(R.id.btnconfirm).setVisibility(View.GONE);
			findViewById(R.id.addbtn).setVisibility(View.VISIBLE);
		}else {
			findViewById(R.id.btnconfirm).setVisibility(View.VISIBLE);
			findViewById(R.id.addbtn).setVisibility(View.GONE);
		}
		BaseHttpEngine.showProgressDialog();
		requestSystemDateTime();
	}
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		init();

	}

	private void init() {

		merchantName = (Spinner) findViewById(R.id.spinner_plan);
		TextView provinceTextView = (TextView)findViewById(R.id.province);
		TextView cityTextView = (TextView)findViewById(R.id.city);
		flowMerchants = (TextView)findViewById(R.id.flow);
		provinceTextView.setText(PlpsDataCenter.getInstance().getPrvcDispName());
		cityTextView.setText(PlpsDataCenter.getInstance().getCityDispName());
		PlpsUtils.initSpinnerView(this, merchantName, PublicTools.getSpinnerDataWithDefaultValue(PlpsDataCenter.getInstance()
						.getQueryFlowFile(), Plps.FLOWFILENAMET,
						Plps.SP_DEFUALTTXT));
		merchantName.setOnItemSelectedListener(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		selectedPosition = position;
		if(position !=0){
			List<Map<String, Object>> flowFileList = PlpsDataCenter.getInstance().getQueryFlowFile();
			flowFileName = (String) flowFileList.get(position-1).get(Plps.FLOWFILENAMET);
			PlpsDataCenter.getInstance().setFlowFileName(flowFileName);
			allDayService = (String)flowFileList.get(position-1).get(Plps.ALLDAYSERVICE);
			infoUrl = (String)flowFileList.get(position-1).get(Plps.INFOURL);
			if(!StringUtil.isNullOrEmpty(infoUrl)){
				flowMerchants.setVisibility(View.VISIBLE);
				flowMerchants.setOnClickListener(flowMerchantsClickListener);
			}else {
				flowMerchants.setVisibility(View.GONE);
			}
			if(allDayService.equals("1")){
				isAllDayService = true;
			}else{
				isAllDayService = false;
			}
			otherProvPay = (String)flowFileList.get(position-1).get(Plps.OTHERPROVPAY);
			if(otherProvPay.equals("1")){
				isOtherProvPay = true;
			}else {
				isOtherProvPay = false;
			}
			crcdPay = (String)flowFileList.get(position-1).get(Plps.CRCDPAY);
			if(crcdPay.equals("1")){
				isCrcdPay = true;
			}else {
				isCrcdPay = false;
			}
			startTime = (String)flowFileList.get(position-1).get(Plps.STARTTIME);
			endTime = (String)flowFileList.get(position-1).get(Plps.ENDTIME);
			flowFileId = (String)flowFileList.get(position-1).get(Plps.FLOWFILEID); 
		}else {
			flowMerchants.setVisibility(View.GONE);
		}
	}

	OnClickListener flowMerchantsClickListener = new OnClickListener() {

		@Override
		public void onClick(View paramView) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(LowServiceActivity.this, LowServiceWebviewActivity.class);
			intent.putExtra("infourl", infoUrl);
			startActivity(intent);
		}
	};

	@Override
	public void onNothingSelected(AdapterView<?> paramAdapterView) {
		// TODO Auto-generated method stub
		
	}
	public void btnNextOnclick(View v){
//		String dateTime = "2014/09/11 06:30:20";
		if(selectedPosition == 0){
			BaseDroidApp.getInstanse().showMessageDialog("请选择商户名称", new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					BaseDroidApp.getInstanse().dismissMessageDialog();
				}
			});
		}else if (isAlailiablet.equals("0")) {
			BaseDroidApp.getInstanse().showMessageDialog("该缴费品种需要您办理该地区卡。", new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					BaseDroidApp.getInstanse().dismissMessageDialog();
				}
			});
		}
		else {
			if(isAllDayService){
				BTCUiActivity.IntentToParseXML(LowServiceActivity.this, PlpsDataCenter.getInstance().getPrvcShortName(),flowFileId, PlpsDataCenter.getInstance().getCatName(), PlpsDataCenter.getInstance().getMenuName(), flowFileName,null,null,null, (String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
			}else {
				Boolean isService= true;
				if(StringUtil.isNullOrEmpty(startTime)||StringUtil.isNullOrEmpty(endTime)){
					isService = false;
				}else {
					isService = QueryDateUtils.compareStartDateToEndDate(startTime,endTime,dateTime);
				}
				if(!isService){
//					Intent intent = new Intent(this, BTCUiActivity.class);
////					intent.putExtra("flowFileId", flowFileId);
//					startActivity(intent);
					BTCUiActivity.IntentToParseXML(LowServiceActivity.this, PlpsDataCenter.getInstance().getPrvcShortName(),flowFileId, PlpsDataCenter.getInstance().getCatName(), PlpsDataCenter.getInstance().getMenuName(), flowFileName,null,null,null, (String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
				}else {
					String startTime1 = startTime.substring(0, 2);
					String startTime2 = startTime.substring(2, 4);
					String startTimes = startTime1+":"+startTime2;
					String endTime1 = endTime.substring(0, 2);
					String endTime2 = endTime.substring(2, 4);
					String endTimes = endTime1+":"+endTime2;
					BaseDroidApp
							.getInstanse()
							.showMessageDialog(
									LowServiceActivity.this
											.getString(R.string.plps_not_service_time)
											+ startTimes
											+ "--"
											+ endTimes
											+ LowServiceActivity.this
													.getString(R.string.plps_not_service_timet),
									new OnClickListener() {

										@Override
										public void onClick(View paramView) {
											// TODO Auto-generated method stub
											BaseDroidApp.getInstanse().dismissMessageDialog();
										}
									});
				}
			}
		}
		
	}
	
	
	public void addServiceClick(View v){
		if(selectedPosition == 0){
			BaseDroidApp.getInstanse().showMessageDialog("请选择商户名称", new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					BaseDroidApp.getInstanse().dismissMessageDialog();
				}
			});
		}else if (("0").equals(isAlailiablet)) {
			BaseDroidApp.getInstanse().showMessageDialog("该缴费品种需要您办理该地区卡，是否继续添加？", new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					BaseHttpEngine.showProgressDialog();
					requestPSNGetTokenId((String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
				}
			});
		} else {
		if(!StringUtil.isNullOrEmpty(commPaymentList)){
			for(int i=0; i<commPaymentList.size(); i++){
				if(flowFileId.equals(commPaymentList.get(i).get(Plps.FLOWFILEID))){
					BaseDroidApp.getInstanse().showMessageDialog(getString(R.string.plps_add_again), new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							BaseDroidApp.getInstanse().dismissMessageDialog();
						}
					});
					return;
				}
			}
		}
		BaseHttpEngine.showProgressDialog();
		requestPSNGetTokenId((String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		}
	}
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestPSNGetTokenIdCallBack(resultObj);
		requestAddCommonUsedPaymentList((String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID));
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
		flowIdMap.put(Plps.FLOWFILEID, flowFileId);
		commPaymentList.add(flowIdMap);
		BaseDroidApp.getInstanse().showMessageDialog("添加常用服务成功", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BaseDroidApp.getInstanse().dismissMessageDialog();
				setResult(1001);
//				if(Flag){
					ActivityTaskManager.getInstance().removeAllActivity();
					ModelBoc.onAddCommonUsePaymentSuccess();
//				}
				finish();
			}
		});
	}
}
