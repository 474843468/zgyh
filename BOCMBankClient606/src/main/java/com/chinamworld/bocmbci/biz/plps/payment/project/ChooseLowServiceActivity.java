//package com.chinamworld.bocmbci.biz.plps.payment.project;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemSelectedListener;
//import android.widget.Spinner;
//
//import com.chinamworld.bocmbci.R;
//import com.chinamworld.bocmbci.base.application.BaseDroidApp;
//import com.chinamworld.bocmbci.bii.BiiRequestBody;
//import com.chinamworld.bocmbci.bii.constant.Comm;
//import com.chinamworld.bocmbci.bii.constant.Plps;
//import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
//import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
//import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
//import com.chinamworld.bocmbci.constant.ConstantGloble;
//import com.chinamworld.bocmbci.http.HttpManager;
//import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
//import com.chinamworld.bocmbci.utils.PublicTools;
//import com.chinamworld.bocmbci.utils.StringUtil;
//import com.chinamworld.bocmbci.widget.CustomDialog;
///**
// * 添加商户页面
// * @author zxj*/
//public class ChooseLowServiceActivity extends PlpsBaseActivity implements OnItemSelectedListener{
//	private Spinner merchantName;
//	//流程文件id
//	private String flowFileId;
//	//选择记录项
//	private int selectedPosition=-1;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
////		String title = getIntent().getStringExtra(Plps.CATNAME);
//		setTitle("添加常用服务");
//		cityAdress.setVisibility(View.GONE);
//		mRightButton.setVisibility(View.VISIBLE);
//		inflateLayout(R.layout.plps_add_service);
//		init();
//	}
//
//	private void init() {
//		merchantName = (Spinner) findViewById(R.id.spinner_plan);
//		PlpsUtils.initSpinnerView(this, merchantName, PublicTools.getSpinnerDataWithDefaultValue(PlpsDataCenter.getInstance()
//						.getQueryFlowFile(), Plps.FLOWFILENAMET,
//						Plps.SP_DEFUALTTXT));
//		merchantName.setOnItemSelectedListener(this);
//	}
//
//	@Override
//	public void onItemSelected(AdapterView<?> parent, View view,
//			int position, long id) {
//		// TODO Auto-generated method stub
//		selectedPosition = position-1;
//		if(position !=0){
//			List<Map<String, Object>> flowFileList = PlpsDataCenter.getInstance().getQueryFlowFile();
//			flowFileId = (String)flowFileList.get(position-1).get(Plps.FLOWFILEID);
//		}
//	}
//
//	@Override
//	public void onNothingSelected(AdapterView<?> paramAdapterView) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	// 添加
//	public void addOnclick(View v) {
//		if (selectedPosition == -1) {
//			BaseDroidApp.getInstanse().showMessageDialog("请选择商户",
//					new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							// TODO Auto-generated method stub
//							BaseDroidApp.getInstanse().dismissMessageDialog();
//						}
//					});
//		} else {
//			List<Map<String, Object>> commonPaymentList= PlpsDataCenter.getInstance().getCommonPaymentList();
//			if(!StringUtil.isNullOrEmpty(commonPaymentList)){
//				for(int i=0; i<commonPaymentList.size();i++){
//					String flowFileIdt = (String)commonPaymentList.get(i).get(Plps.FLOWFILEID);
//					if(flowFileId.equals(flowFileIdt)){
//						BaseDroidApp.getInstanse().showMessageDialog("对不起，该缴费项目已添加过常用服务", new OnClickListener() {
//							
//							@Override
//							public void onClick(View paramView) {
//								// TODO Auto-generated method stub
//								BaseDroidApp.getInstanse().dismissMessageDialog();
//							}
//						});
//						return;
//					}
//					
//				}
//			}
//			BaseHttpEngine.showProgressDialog();
//			requestCommConversationId();
//			// 新增常用缴费项目
////			requestAddCommonUsedPaymentList();
//		}
//
//	}
//	@Override
//	public void requestCommConversationIdCallBack(Object resultObj) {
//		// TODO Auto-generated method stub
//		super.requestCommConversationIdCallBack(resultObj);
//		psnGetTokenId();
//	}
//	private void psnGetTokenId(){
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
//		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
//				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
//		HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenId");
//	}
//	public void aquirePSNGetTokenId(Object resultObj){
//		String tokenId = (String) this.getHttpTools().getResponseResult(resultObj);
//		if (StringUtil.isNullOrEmpty(tokenId)) {
//			BaseHttpEngine.dissMissProgressDialog(); return;
//		}
//		requestAddCommonUsedPaymentList(tokenId);
//	}
//	/**
//	 * 添加常用缴费项目*/
//	private void requestAddCommonUsedPaymentList(String tokenId){
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(Plps.ADDCOMMONUSEDPAYMENT);
//		biiRequestBody.setConversationId((String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put(Plps.FLOWFILEID, flowFileId);
//		params.put(Plps.TOKEN, tokenId);
//		biiRequestBody.setParams(params);
//		HttpManager.requestBii(biiRequestBody, this, "requestAddCommonUsedPaymentListCallBack");
//	}
//	public void requestAddCommonUsedPaymentListCallBack(Object resultObj){
//		BaseHttpEngine.dissMissProgressDialog();
//		CustomDialog.toastShow(this,getString(R.string.plps_add_service_success));
//		setResult(RESULT_OK);
//		finish();
//	}
//	
//	//取消
////	public void resetOnclick(View v){
////		finish();
////	}
//}
