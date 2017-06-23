package com.chinamworld.bocmbci.biz.plps.payment.project.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.abstracttools.AbstractLoginTool;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.peopleservice.ui.BTCUiActivity;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IHttpCallBack;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
import com.chinamworld.bocmbci.biz.plps.adapter.PaymentCommonServiceAdapter;
import com.chinamworld.bocmbci.biz.plps.order.OrderAdressCityActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpObserver;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
 * 常用服务页面
 * @author zxj
 */
public class PaymentCommonServerFragment extends Fragment implements
		HttpObserver {
	private boolean isShowDelete;// 根据这个变量来判断是否显示删除图标，true是显示，false是不显示
	private View view;
	// 常用缴费项目
	private List<Map<String, Object>> commonPaymentList = new ArrayList<Map<String, Object>>();
	//客户端支持省列表的常用缴费项目，不支持省的隐藏
	private List<Map<String, Object>> commonPaymentListt = new ArrayList<Map<String,Object>>();
	// 缴费项目名
	private ArrayList<HashMap<String, Object>> listItem;
	// 常用缴费项目
	private GridView commonGridView;
	// 缴费项目的adapter
	private PaymentCommonServiceAdapter paymentProjectAdapter;
	private Button finish;
	private String flowFileId;
	// 是否刷新
	private Boolean isNotify = false;
	// 服务大类彩
	private int[] imageServiceTypes = new int[] { R.drawable.servicetype_0,
			R.drawable.servicetype_1, R.drawable.servicetype_2,
			R.drawable.servicetype_3, R.drawable.servicetype_4,
			R.drawable.servicetype_5, R.drawable.servicetype_6,
			R.drawable.servicetype_7, R.drawable.servicetype_8,
			R.drawable.servicetype_9, R.drawable.servicetype_10,
			R.drawable.servicetype_11, R.drawable.servicetype_12 };
	// 服务小类彩
	private int[] smallServiceImage = new int[] { R.drawable.smallservices_0,
			R.drawable.smallservices_1, R.drawable.smallservices_2,
			R.drawable.smallservices_3, R.drawable.smallservices_4,
			R.drawable.smallservices_5, R.drawable.smallservices_6,
			R.drawable.smallservices_7, R.drawable.smallservices_8,
			R.drawable.smallservices_9, R.drawable.smallservices_10,
			R.drawable.smallservices_11, R.drawable.smallservices_12,
			R.drawable.smallservices_13, R.drawable.smallservices_14,
			R.drawable.smallservices_15, R.drawable.smallservices_16,
			R.drawable.smallservices_17, R.drawable.smallservices_18,
			R.drawable.smallservices_19, R.drawable.smallservices_20,
			R.drawable.smallservices_21, R.drawable.smallservices_22,
			R.drawable.smallservices_23, R.drawable.smallservices_24,
			R.drawable.smallservices_25, R.drawable.smallservices_26,
			R.drawable.smallservices_27, R.drawable.smallservices_28,
			R.drawable.smallservices_29, R.drawable.smallservices_30,
			R.drawable.smallservices_31, R.drawable.smallservices_32,
			R.drawable.smallservices_33, R.drawable.smallservices_34,
			R.drawable.smallservices_35, R.drawable.smallservices_36,
			R.drawable.smallservices_37, R.drawable.smallservices_38,
			R.drawable.smallservices_39, R.drawable.smallservices_40,
			R.drawable.smallservices_41, R.drawable.smallservices_42,
			R.drawable.smallservices_43, R.drawable.smallservices_44,
			R.drawable.smallservices_45, R.drawable.smallservices_46,
			R.drawable.smallservices_47, R.drawable.smallservices_48,
			R.drawable.smallservices_49, R.drawable.smallservices_50,
			R.drawable.smallservices_51,R.drawable.smallservices_52,
			R.drawable.smallservices_53,R.drawable.smallservices_54,
			R.drawable.smallservices_55,R.drawable.smallservices_56,
			R.drawable.smallservices_57,R.drawable.smallservices_58,
			R.drawable.smallservices_59,R.drawable.smallservices_60,
			R.drawable.smallservices_61,R.drawable.smallservices_62,
			R.drawable.smallservices_63,R.drawable.smallservices_64};
	List<Map<String, Object>> menuList;
	/**删除哪一个*/
	private int selectionPosition;
	//服务小类catId
	private String catId = null;
	/** 服务器时间 */
	private String dateTime;
	/**服务开始时间*/
	private String startTime;
	/**服务结束时间*/
	private String endTime;
	/**是否支持7*24小时服务*/
	private String allDayService;
	/**缴费项目是否有效*/
	private String isAvalid;
	/**缴费商户名称*/
	private String flowFileIdt;
	/**商户名称*/
	private String merchantName;
	
	private LayoutInflater layoutInflater;
	/**点击小类的省简称*/
	private String prvcShortName;
	/**其他服务在删除的时候是否需要添加*/
//	private Boolean isAddOtherService = true;
	/**滑动 图片是否有删除图标*/
	private Boolean isDelete = false;
	private TextView cityAdres;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		layoutInflater = inflater;
		view = inflater.inflate(R.layout.plps_common_payment, container, false);
		commonGridView = (GridView) view.findViewById(R.id.plps_grid_view);
		cityAdres = (TextView) PaymentCommonServerFragment.this.getActivity()
				.findViewById(R.id.cityAdress);
		cityAdres.setOnClickListener(cityAdresClick);
		listItem = new ArrayList<HashMap<String, Object>>();
		menuList = new ArrayList<Map<String,Object>>();
//		requestCommConversationId();
		requestPsnPlpsQueryCommonUsedPaymentList();
		return view;
	}
	OnClickListener cityAdresClick = new OnClickListener() {
		
		@Override
		public void onClick(View paramView) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(PaymentCommonServerFragment.this.getActivity(),
					OrderAdressCityActivity.class);
			intent.putExtra(Plps.PRVCDISPNAME, PlpsDataCenter.getInstance().getPrvcDispName());
			intent.putExtra(Plps.CITYDISPNAME, PlpsDataCenter.getInstance().getCityDispName());
//			startActivity(intent);
			startActivityForResult(intent, 1);
			
		}
	};
	/**
	 * 网络请求成功回调  */
	IHttpCallBack requestHttpCallBack;
	/**
	 * 请求ConversationId */
	public void requestCommConversationId(IHttpCallBack callBack){
		requestHttpCallBack = callBack;
		requestCommConversationId();
	}
	
	/**
	 * 请求conversationId 来登录之外的conversation出Id
	 */
	public void requestCommConversationId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.CONVERSATION_ID_API);
		HttpManager.requestBii(biiRequestBody, this, "requestCommConversationIdCallBack");
	}

	public void requestCommConversationIdCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String commConversationId = (String) biiResponseBody.getResult();
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.CONVERSATION_ID, commConversationId);
//		requestPsnPlpsQueryCommonUsedPaymentList();
		if(requestHttpCallBack != null){
			requestHttpCallBack.requestHttpSuccess(resultObj);
		}
	}

	/**
	 * 查询常用缴费项目
	 */
	private void requestPsnPlpsQueryCommonUsedPaymentList() {
		
		requestCommConversationId(new IHttpCallBack(){
			@Override
			public void requestHttpSuccess(Object result) {
				// TODO Auto-generated method stub
				if(isNotify){
					BaseHttpEngine.showProgressDialogCanGoBack();
				}
				BiiRequestBody biiRequestBody = new BiiRequestBody();
				biiRequestBody.setMethod(Plps.QUERYCOMMONUSEDPAYMENT);
				biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
				biiRequestBody.setParams(null);
				HttpManager.requestBii(biiRequestBody, PaymentCommonServerFragment.this,
						"requestPsnPlpsQueryCommonUsedPaymentListCallBack");
			}});
	
	}

	@SuppressWarnings("unchecked")
	public void requestPsnPlpsQueryCommonUsedPaymentListCallBack(
			Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultPayment = HttpTools.getResponseResult(resultObj);
		commonPaymentList.clear();
		commonPaymentList = (List<Map<String, Object>>) resultPayment
				.get(Plps.PAYMENTLIST);
		if (!StringUtil.isNullOrEmpty(commonPaymentList)) {
			// if(isFirst){
			PlpsDataCenter.getInstance().setCommonPaymentList(commonPaymentList);
			getData();
//			if (isNotify) {
//				paymentProjectAdapter.notifyDataSetChanged();
//				isNotify = false;
//				if(commonPaymentList.size()==8){
//					isAddOtherService = true;
//				}
//			} else {
				init();
//			}
			// }else {
			// paymentProjectAdapter.notifyDataSetChanged();
			// }
		}else {
			getLocalData();
//			if (isNotify) {
//				paymentProjectAdapter.notifyDataSetChanged();
//				isNotify = false;
//			} else {
				init();
//			}
		}
	}
	/**commonPaymentList为空*/
	private void getLocalData(){
		listItem.clear();
		commonPaymentListt.clear();
//		isAddOtherService = false;
		HashMap<String, Object> otherService = new HashMap<String, Object>();
		otherService.put("image", R.drawable.smallservices_3);
		otherService.put("text", "全部项目");
		otherService.put("Other", true); // 是否是其他服务，如果是，长按时不可以删除
		listItem.add(otherService);
	}

	private void getData() {
		listItem.clear();
		commonPaymentListt.clear();
//		menuList.clear();
		menuList = PlpsDataCenter.getInstance().getLiveMenus();
		for (int i = 0; i < commonPaymentList.size(); i++) {
			// 缴费项目id
			 String catId = (String)commonPaymentList.get(i).get(Plps.CATID);
			// //省简称城市显示名称
			// String prvcShortName =
			// (String)commonPaymentList.get(i).get(Plps.PRVCSHORTNAME);
			// 省显示名称
			String prvcDispName = (String) commonPaymentList.get(i).get(
					Plps.PRVCDISPNAME);
			// 小类名称
			String catName = (String) commonPaymentList.get(i)
					.get(Plps.CATNAME);
			// 城市显示名称
			String cityDispName = (String) commonPaymentList.get(i).get(
					Plps.CITYDISPNAME);
			// 商户名称
			String merchantName = (String) commonPaymentList.get(i).get(
					Plps.MERCHANTNAME);
			//菜单ID（服务大类）
			String mesnuId = (String)commonPaymentList.get(i).get(Plps.MUNUID);
			//常用缴费项目是否在此终端展现
			String displayFlag = (String)commonPaymentList.get(i).get("displayFlag");
			String prvcDispNamet = prvcDispName.trim();
			//!PlpsDataCenter.provList.contains(prvcDispNamet)||
			if(!StringUtil.isNullOrEmpty(displayFlag)){
				if(displayFlag.equals("0")){
					continue;
				}
			}
			commonPaymentListt.add(commonPaymentList.get(i));
			HashMap<String, Object> catMap = new HashMap<String, Object>();
			catMap.put(Plps.PRVCDISPNAME, prvcDispName);
			catMap.put(Plps.CATNAME, catName);
			catMap.put(Plps.CITYDISPNAME, cityDispName);
			catMap.put(Plps.MERCHANTNAME, merchantName);
			if (!StringUtil
					.isNullOrEmpty(PlpsDataCenter.smallServictTypes
							.get(catId))) {
				int imageId = Integer
						.valueOf(PlpsDataCenter.smallServictTypes
								.get(catId));
				catMap.put("image",
						smallServiceImage[imageId]);
			}else {
				for(int m=0; m<menuList.size(); m++){
					String munuIdCompare = (String)menuList.get(m).get(Plps.MENUSID);
					if(mesnuId.equals(munuIdCompare)){
//						String menuName = (String)menuList.get(m).get(Plps.MENUSNAME);
						if(!StringUtil.isNullOrEmpty(PlpsDataCenter.serviceTypes.get(mesnuId))){
							int imageServiceId = Integer.valueOf(PlpsDataCenter.serviceTypes.get(mesnuId));
							catMap.put("image",
									imageServiceTypes[imageServiceId]);
							break;
						}else {
							catMap.put("image", "-1");
							break;
						}
						
					}
				}
			}
//			if(commonPaymentList.size()==1){
//				catMap.put("text", catName);
//			}
//			catMap.put("text", catName);
			catMap.put("text", catName+"（"+prvcDispName+cityDispName+"）");
			if(PlpsDataCenter.municiplGovernment.contains(prvcDispName)){
				catMap.put("text", catName+"（"+cityDispName+"）");
			}
//			for (int j = 0; j < commonPaymentList.size(); j++) {
//				String catNameCompare = (String) commonPaymentList.get(j).get(
//						Plps.CATNAME);
//				if(i==j||!(catName.equals(catNameCompare))){
//					continue;
//				}
//				// 相同服务小类
//				if (catName.equals(catNameCompare)) {
//					String cityDispNameCom = (String) commonPaymentList.get(j)
//							.get(Plps.CITYDISPNAME);
//					if (!cityDispName.equals(cityDispNameCom)) {
//						// 不同地区
//						catMap.put("text", catName+"（"+prvcDispName+cityDispName+"）");
//						if(PlpsDataCenter.municiplGovernment.contains(prvcDispName)){
//							catMap.put("text", catName+"（"+cityDispName+"）");
//						}
//					} else {
//						// 相同地区
//						catMap.put("text", catName+"（"+merchantName+"）");
//						break;
//					}
//				}
//			}
				listItem.add(catMap);
		}
//		if(commonPaymentList.size() != 8){
//			isAddOtherService = false;
			HashMap<String, Object> otherService = new HashMap<String, Object>();
			otherService.put("image", R.drawable.smallservices_3);
			otherService.put("text", "全部项目");
			otherService.put("Other", true); // 是否是其他服务，如果是，长按时不可以删除
			listItem.add(otherService);
			
//		}
	}

	private void init() {
		paymentProjectAdapter = new PaymentCommonServiceAdapter(
				PaymentCommonServerFragment.this.getActivity(), listItem);
		commonGridView.setAdapter(paymentProjectAdapter);
		paymentProjectAdapter.notifyDataSetChanged();
		commonGridView.setOnItemClickListener(gridViewListener);
		commonGridView.setOnItemLongClickListener(gridViewLongClick);
		 PlpsBaseActivity.mFinishButton.setOnClickListener(mFinishClickListener);
	}

	/** 长按点击 */
	OnItemLongClickListener gridViewLongClick = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int position, long arg3) {
			commonGridView.setOnItemClickListener(null);
			PlpsDataCenter.getInstance().setIsDelete(null);
			isDelete = true;
			PlpsDataCenter.getInstance().setIsDelete(isDelete);
//			if(commonPaymentList.size() == 8){
//				if (isShowDelete) {
//					isShowDelete = false;
//				} else {
//					isShowDelete = true;
//				}
//				finish = (Button) PaymentCommonServerFragment.this
//						.getActivity().findViewById(R.id.finish);
//				finish.setVisibility(View.VISIBLE);
//				finish.setOnClickListener(mFinishClickListener);
//				PaymentCommonServerFragment.this.getActivity()
//						.findViewById(R.id.cityAdress).setVisibility(View.GONE);
//
//				paymentProjectAdapter.setIsShowDelete(isShowDelete);
//				paymentProjectAdapter
//						.setOnbanklistCancelRelationClickListener(listCancelRelationClickListener);
//				commonGridView.setOnItemLongClickListener(null);
//			}else {
				if (position < (listItem.size())) {
					if(commonPaymentListt.size() == 0 || position == (listItem.size()-1)){
						PlpsDataCenter.getInstance().setIsDelete(null);
						commonGridView.setOnItemClickListener(gridViewListener);
					}else {
						if (isShowDelete) {
							isShowDelete = false;
						} else {
							isShowDelete = true;
						}
						finish = (Button) PaymentCommonServerFragment.this
								.getActivity().findViewById(R.id.finish);
						finish.setVisibility(View.VISIBLE);
						finish.setOnClickListener(mFinishClickListener);
						PaymentCommonServerFragment.this.getActivity()
								.findViewById(R.id.cityAdress).setVisibility(View.GONE);

						paymentProjectAdapter.setIsShowDelete(isShowDelete);
						paymentProjectAdapter
								.setOnbanklistCancelRelationClickListener(listCancelRelationClickListener);
						commonGridView.setOnItemLongClickListener(null);
					}
				}
//			}
			
			return false;
		}

	};

	/** 删除事件 */
	OnItemClickListener listCancelRelationClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				final int position, long arg3) {
			if(commonPaymentList.size()!= 8){
				if (position < (listItem.size() - 1)) {
					BaseDroidApp.getInstanse().showErrorDialog("是否删除该常用服务",
							R.string.cancle, R.string.confirm,
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									BaseDroidApp.getInstanse().dismissErrorDialog();
									switch (v.getId()) {
									case R.id.exit_btn:// 取消

										break;
									case R.id.retry_btn:// 确定
										// 新增常用缴费项目
										// requestAddCommonUsedPaymentList();
										flowFileId = (String) commonPaymentListt
												.get(position).get(Plps.FLOWFILEID);
										selectionPosition = position;
										requestCommversationId();
										// 删除常用缴费项目
										// requestDeleteCommonUsedPaymentList();
										break;
									default:
										break;
									}
								}
							});
				}
			}else{
				BaseDroidApp.getInstanse().showErrorDialog("是否删除该常用服务",
						R.string.cancle, R.string.confirm,
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								BaseDroidApp.getInstanse().dismissErrorDialog();
								switch (v.getId()) {
								case R.id.exit_btn:// 取消

									break;
								case R.id.retry_btn:// 确定
									// 新增常用缴费项目
									// requestAddCommonUsedPaymentList();
									flowFileId = (String) commonPaymentListt
											.get(position).get(Plps.FLOWFILEID);
									selectionPosition = position;
									requestCommversationId();
									// 删除常用缴费项目
									// requestDeleteCommonUsedPaymentList();
									break;
								default:
									break;
								}
							}
						});
			}
			
		}
	};

	/**
	 * 请求conversationId
	 */
	private void requestCommversationId() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.CONVERSATION_ID_API);
		HttpManager.requestBii(biiRequestBody, this,
				"requestCommversationIdCallBack");
	}

	String commConversationId = null;

	public void requestCommversationIdCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		commConversationId = (String) biiResponseBody.getResult();
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.CONVERSATION_ID, commConversationId);
		requestTokenId(commConversationId);
	}

	/**
	 * 请求token
	 */
	private void requestTokenId(String commConversationId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId(commConversationId);
		HttpManager.requestBii(biiRequestBody, this, "requestTokenIdCallBack");
	}

	public void requestTokenIdCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String tokenId = (String)biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		requestDeleteCommonUsedPaymentList(tokenId);
	}

	/**
	 * 删除常用缴费项目
	 */
	private void requestDeleteCommonUsedPaymentList(String tokenId) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.DELETECOMMONUSED);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> flowFileIdList = new ArrayList<Map<String,Object>>();
		Map<String, Object> flowMap = new HashMap<String, Object>();
		flowMap.put(Plps.FLOWFILEID, flowFileId);
		flowFileIdList.add(flowMap);
		map.put(Plps.FLOWFILEIDLIST, flowFileIdList);
		map.put(Plps.TOKEN, tokenId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestDeleteCommonUsedPaymentListCallBack");
	}

	public void requestDeleteCommonUsedPaymentListCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
			toastShow(PaymentCommonServerFragment.this.getActivity(),
					getString(R.string.safety_delete_success));
			listItem.remove(selectionPosition);
//			if(isAddOtherService){
//				isAddOtherService = false;
//				HashMap<String, Object> otherService = new HashMap<String, Object>();
//				otherService.put("image", R.drawable.smallservices_3);
//				otherService.put("text", "其他服务");
//				listItem.add(otherService);
//			}
			String flowFileId = (String)commonPaymentListt.get(selectionPosition).get(Plps.FLOWFILEID);
			String flowFileIdt ;
			for(int i=0; i<commonPaymentList.size(); i++){
				flowFileIdt = (String)commonPaymentList.get(i).get(Plps.FLOWFILEID);
				if(flowFileId.equals(flowFileIdt)){
					commonPaymentList.remove(i);
					break;
				}
			}
			commonPaymentListt.remove(selectionPosition);
			PlpsDataCenter.getInstance().setCommonPaymentList(commonPaymentList);
			finish.setVisibility(View.GONE);
			PaymentCommonServerFragment.this.getActivity()
			.findViewById(R.id.cityAdress)
			.setVisibility(View.VISIBLE);
			isDelete = false;
			PlpsDataCenter.getInstance().setIsDelete(isDelete);
			getData();
			if (isShowDelete) {
				isShowDelete = false;
				paymentProjectAdapter.setIsShowDelete(isShowDelete);
				paymentProjectAdapter.notifyDataSetChanged();
				commonGridView.setOnItemClickListener(gridViewListener);
				commonGridView.setOnItemLongClickListener(gridViewLongClick);
			}
	}

	/**
	 * 删除成功toast
	 */
	private void toastShow(Context context, String text) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.toast, null);
		((TextView) layout.findViewById(R.id.textview)).setText(text);
		Toast toast = new Toast(context);
		toast.setView(layout);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(1000);
		toast.show();
	}

	// 完成按钮
	OnClickListener mFinishClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (isShowDelete) {
				isDelete = false;
				PlpsDataCenter.getInstance().setIsDelete(isDelete);
				isShowDelete = false;
				PaymentCommonServerFragment.this.getActivity()
						.findViewById(R.id.finish).setVisibility(View.GONE);
				PaymentCommonServerFragment.this.getActivity()
						.findViewById(R.id.cityAdress)
						.setVisibility(View.VISIBLE);
				paymentProjectAdapter.setIsShowDelete(isShowDelete);
				paymentProjectAdapter.notifyDataSetChanged();
				commonGridView.setOnItemClickListener(gridViewListener);
				commonGridView.setOnItemLongClickListener(gridViewLongClick);
			}

		}
	};
	
	
	// 服务类点击事件
	OnItemClickListener gridViewListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position,
				long arg3) {
//			if(commonPaymentList.size() !=8){
				if (position < (listItem.size() - 1)) {
//					String prvcDispNam = (String)commonPaymentListt.get(position).get(Plps.PRVCDISPNAME);
//					final String prvcDispNamt = prvcDispNam.trim();
						View contentView = layoutInflater.inflate(R.layout.plps_tip_info_dialog, null);
						((Button)contentView.findViewById(R.id.confirm_btn)).setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								BaseDroidApp.getInstanse().dismissMessageDialog();
//								if(!PlpsDataCenter.provList.contains(prvcDispNamt)){
//									BaseDroidApp.getInstanse().showMessageDialog("手机渠道暂不支持该地区的民生缴费功能，您可以使用账单缴付功能或者通过网银等渠道进行相关缴费", new OnClickListener() {
//										
//										@Override
//										public void onClick(View v) {
//											// TODO Auto-generated method stub
//											BaseDroidApp.getInstanse().dismissMessageDialog();
//										}
//									});
//									return;
//							}
								ClickItem(position);
							}
						});
						((Button)contentView.findViewById(R.id.cancel_btn)).setOnClickListener(new OnClickListener(){
							@Override
							public void onClick(View paramView) {
								BaseDroidApp.getInstanse().dismissMessageDialog();
							}});;
						
						Map<String,Object> map = (Map<String,Object>)listItem.get(position);
						((TextView)contentView.findViewById(R.id.plps_pro_name)).setText(StringUtil.isNullChange((String)map.get(Plps.PRVCDISPNAME)));	
						((TextView)contentView.findViewById(R.id.plps_city_name)).setText(StringUtil.isNullChange((String)map.get(Plps.CITYDISPNAME)));	
						((TextView)contentView.findViewById(R.id.plps_service_type)).setText(StringUtil.isNullChange((String)map.get(Plps.CATNAME)));	
						((TextView)contentView.findViewById(R.id.plps_merch_name)).setText(StringUtil.isNullChange((String)map.get(Plps.MERCHANTNAME)));	
						PlpsUtils.setOnShowAllTextListener(PaymentCommonServerFragment.this.getActivity(), (TextView)contentView.findViewById(R.id.plps_pro_name),
								(TextView)contentView.findViewById(R.id.plps_city_name),(TextView)contentView.findViewById(R.id.plps_service_type),
								(TextView)contentView.findViewById(R.id.plps_merch_name));
						BaseDroidApp.getInstanse().showMessageDialogByView(contentView);
						
				}else if (position == (listItem.size() - 1)) {
//					if(StringUtil.isNullOrEmpty(commonPaymentList)){
						startActivityForResult(new Intent(
								PaymentCommonServerFragment.this.getActivity(),
								PaymentAllProjectFragment.class), 1001);
//					}else {
//						if(commonPaymentList.size()<8){
//							startActivityForResult(new Intent(
//									PaymentCommonServerFragment.this.getActivity(),
//									PaymentAllProjectFragment.class), 1001);
//							}else {
//								BaseDroidApp.getInstanse().showMessageDialog(getString(R.string.plps_nine_error_message), new OnClickListener() {
//									
//									@Override
//									public void onClick(View v) {
//										// TODO Auto-generated method stub
//										BaseDroidApp.getInstanse().dismissMessageDialog();
//									}
//								});
//							}
//					}
					
				}
//			}else {
//				if (position < (listItem.size())) {
//					String prvcDispNam = (String)commonPaymentListt.get(position).get(Plps.PRVCDISPNAME);
//					final String prvcDispNamt = prvcDispNam.trim();
//						View contentView = layoutInflater.inflate(R.layout.plps_tip_info_dialog, null);
//						((Button)contentView.findViewById(R.id.confirm_btn)).setOnClickListener(new OnClickListener() {
//							@Override
//							public void onClick(View v) {
//								// TODO Auto-generated method stub
//								BaseDroidApp.getInstanse().dismissMessageDialog();
//								if(!PlpsDataCenter.provList.contains(prvcDispNamt)){
//									BaseDroidApp.getInstanse().showMessageDialog("手机渠道暂不支持该地区的民生缴费功能，您可以使用账单缴付功能或者通过网银等渠道进行相关缴费", new OnClickListener() {
//										
//										@Override
//										public void onClick(View v) {
//											// TODO Auto-generated method stub
//											BaseDroidApp.getInstanse().dismissMessageDialog();
//										}
//									});
//									return;
//							}
//								ClickItem(position);
//							}
//						});
//						((Button)contentView.findViewById(R.id.cancel_btn)).setOnClickListener(new OnClickListener(){
//							@Override
//							public void onClick(View paramView) {
//								BaseDroidApp.getInstanse().dismissMessageDialog();
//							}});;
//						
//						Map<String,Object> map = (Map<String,Object>)listItem.get(position);
//						((TextView)contentView.findViewById(R.id.plps_pro_name)).setText(StringUtil.isNullChange((String)map.get(Plps.PRVCDISPNAME)));	
//						((TextView)contentView.findViewById(R.id.plps_city_name)).setText(StringUtil.isNullChange((String)map.get(Plps.CITYDISPNAME)));	
//						((TextView)contentView.findViewById(R.id.plps_service_type)).setText(StringUtil.isNullChange((String)map.get(Plps.CATNAME)));	
//						((TextView)contentView.findViewById(R.id.plps_merch_name)).setText(StringUtil.isNullChange((String)map.get(Plps.MERCHANTNAME)));	
//						PlpsUtils.setOnShowAllTextListener(PaymentCommonServerFragment.this.getActivity(), (TextView)contentView.findViewById(R.id.plps_pro_name),
//								(TextView)contentView.findViewById(R.id.plps_city_name),(TextView)contentView.findViewById(R.id.plps_service_type),
//								(TextView)contentView.findViewById(R.id.plps_merch_name));
//						BaseDroidApp.getInstanse().showMessageDialogByView(contentView);
//						
//				}
//			}
			
			
		}

		
	};
	
	private void ClickItem(int position){
//		if(commonPaymentList.size() != 8){
			if (position < (listItem.size() - 1)) {
//				requestCommConversationId();
//				String otherProvPay = (String)commonPaymentList.get(position).get(Plps.OTHERPROVPAYT);
//				if(otherProvPay.equals("0")){
//					BaseDroidApp.getInstanse().showMessageDialog("该缴费品种需要您办理该地区卡", new OnClickListener() {
//						
//						@Override
//						public void onClick(View paramView) {
//							// TODO Auto-generated method stub
//							BaseDroidApp.getInstanse().dismissMessageDialog();
//						}
//					});
//				}else {
					catId = (String)commonPaymentListt.get(position).get(Plps.CATID);
					PlpsDataCenter.getInstance().setItemId(catId);
					String catName = (String)commonPaymentListt.get(position).get(Plps.CATNAME);
					PlpsDataCenter.getInstance().setCatName(catName);
					String menuId = (String)commonPaymentListt.get(position).get(Plps.MUNUID);
					List<Map<String, Object>> menuList = PlpsDataCenter.getInstance().getLiveMenus();
					for(int j=0; j<menuList.size(); j++){
						String munuIdCompare = (String)menuList.get(j).get(Plps.MENUSID);
						if(menuId.equals(munuIdCompare)){
							String menuName = (String)menuList.get(j).get(Plps.MENUSNAME);
							PlpsDataCenter.getInstance().setMenuName(menuName);
						}
					}
					//服务开始时间
					startTime = (String)commonPaymentListt.get(position).get(Plps.STARTTIME);
					//服务结束时间
					endTime = (String)commonPaymentListt.get(position).get(Plps.ENDTIME);
					//是否支持7*24小时服务
					allDayService=(String)commonPaymentListt.get(position).get(Plps.ALLDAYSERVICE);
					flowFileIdt = (String) commonPaymentListt
							.get(position).get(Plps.FLOWFILEID);
					//缴费项目是否有效
					isAvalid = (String)commonPaymentListt.get(position).get(Plps.ISAVALID);
					//商户名
					merchantName = (String)commonPaymentListt.get(position).get(Plps.MERCHANTNAME);
					PlpsDataCenter.getInstance().setFlowFileName(merchantName);
					//点击小类省简称
					prvcShortName = (String)commonPaymentListt.get(position).get(Plps.PRVCSHORTNAME);
					//请求当前时间
//					requestCommConversationId(new IHttpCallBack(){
	//
//						@Override
//						public void requestHttpSuccess(Object result) {
							// TODO Auto-generated method stub
							requestSystemDateTime();	
//						}});
				//	requestSystemDateTime();				
//				}
			} else if (position == (listItem.size() - 1)) {
//				if(StringUtil.isNullOrEmpty(commonPaymentList)){
					startActivityForResult(new Intent(
							PaymentCommonServerFragment.this.getActivity(),
							PaymentAllProjectFragment.class), 1001);
//				}
//				else {
//					if(commonPaymentList.size()<8){
//						startActivityForResult(new Intent(
//								PaymentCommonServerFragment.this.getActivity(),
//								AddProvServiceActivity.class), 1001);
//						}else {
//							BaseDroidApp.getInstanse().showMessageDialog(getString(R.string.plps_nine_error_message), new OnClickListener() {
//								
//								@Override
//								public void onClick(View v) {
//									// TODO Auto-generated method stub
//									BaseDroidApp.getInstanse().dismissMessageDialog();
//								}
//							});
//						}
//				}
				
			}
//		}
//		else {
//			if (position <= (listItem.size())) {
//				requestCommConversationId();
//				String otherProvPay = (String)commonPaymentList.get(position).get(Plps.OTHERPROVPAYT);
//				if(otherProvPay.equals("0")){
//					BaseDroidApp.getInstanse().showMessageDialog("该缴费品种需要您办理该地区卡", new OnClickListener() {
//						
//						@Override
//						public void onClick(View paramView) {
//							// TODO Auto-generated method stub
//							BaseDroidApp.getInstanse().dismissMessageDialog();
//						}
//					});
//				}else {
//					catId = (String)commonPaymentListt.get(position).get(Plps.CATID);
//					PlpsDataCenter.getInstance().setItemId(catId);
//					String catName = (String)commonPaymentListt.get(position).get(Plps.CATNAME);
//					PlpsDataCenter.getInstance().setCatName(catName);
//					String menuId = (String)commonPaymentListt.get(position).get(Plps.MUNUID);
//					List<Map<String, Object>> menuList = PlpsDataCenter.getInstance().getLiveMenus();
//					for(int j=0; j<menuList.size(); j++){
//						String munuIdCompare = (String)menuList.get(j).get(Plps.MENUSID);
//						if(menuId.equals(munuIdCompare)){
//							String menuName = (String)menuList.get(j).get(Plps.MENUSNAME);
//							PlpsDataCenter.getInstance().setMenuName(menuName);
//						}
//					}
//					//服务开始时间
//					startTime = (String)commonPaymentListt.get(position).get(Plps.STARTTIME);
//					//服务结束时间
//					endTime = (String)commonPaymentListt.get(position).get(Plps.ENDTIME);
//					//是否支持7*24小时服务
//					allDayService=(String)commonPaymentListt.get(position).get(Plps.ALLDAYSERVICE);
//					flowFileIdt = (String) commonPaymentListt
//							.get(position).get(Plps.FLOWFILEID);
//					//缴费项目是否有效
//					isAvalid = (String)commonPaymentListt.get(position).get(Plps.ISAVALID);
//					//商户名
//					merchantName = (String)commonPaymentListt.get(position).get(Plps.MERCHANTNAME);
//					PlpsDataCenter.getInstance().setFlowFileName(merchantName);
					//点击小类省简称
//					prvcShortName = (String)commonPaymentListt.get(position).get(Plps.PRVCSHORTNAME);
					//请求当前时间
//					requestCommConversationId(new IHttpCallBack(){
	//
//						@Override
//						public void requestHttpSuccess(Object result) {
							// TODO Auto-generated method stub
//							requestSystemDateTime();	
//						}});
				//	requestSystemDateTime();				
//				}
//			}
//		}
		
	}
	
	/**
	 * 请求系统时间
	 */
	public void requestSystemDateTime() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_SYSTEM_TIME);
		HttpManager.requestBii(biiRequestBody, this, "requestSystemDateTimeCallBack");
	}
	/**
	 * 请求系统时间返回
	 * 
	 * @param resultObj dateTime 格式例如：2015/03/31 17:36:41
	 */
	public void requestSystemDateTimeCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		dateTime = (String) resultMap.get(Comm.DATETME);
		if (isAvalid.equals("1")) {
			if (allDayService.equals("1")) {
//				Intent intent = new Intent(
//						PaymentCommonServerFragment.this.getActivity(),
//						BTCUiActivity.class);
////				intent.putExtra("flowFileId", flowFileIdt);
//				startActivity(intent);
				BTCUiActivity.IntentToParseXML(PaymentCommonServerFragment.this.getActivity(), prvcShortName,flowFileIdt, PlpsDataCenter.getInstance().getCatName(), PlpsDataCenter.getInstance().getMenuName(), merchantName,null,null,null, (String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
			} else {
				Boolean isService = QueryDateUtils.compareStartDateToEndDate(
						startTime, endTime, dateTime);
				if (!isService) {
//					Intent intent = new Intent(
//							PaymentCommonServerFragment.this.getActivity(),
//							BTCUiActivity.class);
////					intent.putExtra("flowFileId", flowFileIdt);
//					startActivity(intent);
					BTCUiActivity.IntentToParseXML(PaymentCommonServerFragment.this.getActivity(), prvcShortName,flowFileIdt, PlpsDataCenter.getInstance().getCatName(), PlpsDataCenter.getInstance().getMenuName(), merchantName, null,null,null,(String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
				} else {
					String startTime1 = startTime.substring(0, 2);
					String startTime2 = startTime.substring(2, 4);
					String startTimes = startTime1 + ":" + startTime2;
					String endTime1 = endTime.substring(0, 2);
					String endTime2 = endTime.substring(2, 4);
					String endTimes = endTime1 + ":" + endTime2;
					BaseDroidApp
							.getInstanse()
							.showMessageDialog(
									PaymentCommonServerFragment.this
											.getString(R.string.plps_not_service_time)
											+ startTimes
											+ "--"
											+ endTimes
											+ PaymentCommonServerFragment.this
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
		} else {
			BaseDroidApp.getInstanse().showMessageDialog("该缴费项目无效",
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							BaseDroidApp.getInstanse().dismissMessageDialog();
						}
					});
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		/*if (resultCode == PaymentCommonServerFragment.this.getActivity().RESULT_OK|| resultCode == 1011) {
			isNotify = true;
			requestPsnPlpsQueryCommonUsedPaymentList();
		}*/
		if (requestCode == 1) {
			if(resultCode == PaymentCommonServerFragment.this.getActivity().RESULT_OK ){
				String prvcName = data.getStringExtra(Plps.PRVCDISPNAME);
				String cityName = data.getStringExtra(Plps.CITYDISPNAME);
				if(PlpsDataCenter.municiplGovernment.contains(prvcName.trim())){
					cityAdres.setText(prvcName);
				}else {
					cityAdres.setText(prvcName + cityName);
				}
				PlpsDataCenter.getInstance().setPrvcDispName(null);
				PlpsDataCenter.getInstance().setCityDispName(null);
				PlpsDataCenter.getInstance().setCityDispName(cityName);
				PlpsDataCenter.getInstance().setPrvcDispName(prvcName);
			}
		}else if (requestCode == 1001) {
			String prvcName = PlpsDataCenter.getInstance().getPrvcDispName();
			String cityName =  PlpsDataCenter.getInstance().getCityDispName();
			if(PlpsDataCenter.municiplGovernment.contains(prvcName.trim())){
				cityAdres.setText(prvcName);
			}else {
				cityAdres.setText(prvcName + cityName);
			}
			isNotify = true;
			requestPsnPlpsQueryCommonUsedPaymentList();
		}
	}

	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		boolean stopFlag = false;
		if (resultObj instanceof BiiResponse) {
			// Bii请求前拦截
			stopFlag = doBiihttpRequestCallBackPre((BiiResponse) resultObj);
		} else if (resultObj instanceof String) {

		} else if (resultObj instanceof Map) {

		} else if (resultObj instanceof Bitmap) {

		} else if (resultObj instanceof File) {

		} else {
			// do nothing
		}

		return stopFlag;
	}
	/**
	 * Bii请求前拦截-可处理统一的错误弹出框 有返回数据（response）<br>
	 * 对于包含exception 的业务错误提示数据进行统一弹框提示
	 * 
	 * @param BiiResponse resultObj
	 * @return 是否终止业务流程
	 */
	public boolean doBiihttpRequestCallBackPre(BiiResponse response) {
		List<BiiResponseBody> biiResponseBodyList = response.getResponse();
		if (!StringUtil.isNullOrEmpty(biiResponseBodyList)) {
			for (BiiResponseBody body : biiResponseBodyList) {

				if (!ConstantGloble.STATUS_SUCCESS.equals(body.getStatus())) {
					// 消除通信框
					BaseHttpEngine.dissMissProgressDialog();
					if (Login.LOGOUT_API.equals(body.getMethod())) {// 退出的请求

						return false;
					}
					BiiError biiError = body.getError();
					// 判断是否存在error
					if (biiError != null) {
						//过滤错误
						LocalData.Code_Error_Message.errorToMessage(body);
						
						
						if (biiError.getCode() != null) {
							if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
																						// 要重新登录
								// TODO 错误码是否显示"("+biiError.getCode()+") "
								BaseDroidApp.getInstanse().showMessageDialog(biiError.getMessage(),
										new OnClickListener() {

											@Override
											public void onClick(View v) {
												BaseDroidApp.getInstanse().dismissErrorDialog();
												ActivityTaskManager.getInstance().removeAllActivity();
//												new LoginTask(PaymentCommonServerFragment.this.getActivity()).exe(new LoginTask.LoginCallback() {
//													@Override
//													public void loginStatua(boolean isLogin) {
//														if(isLogin){
//															ActivityTaskManager.getInstance().removeAllActivity();
//															ActivityTaskManager.getInstance().removeAllSecondActivity();
//														}
//													}
//												});
												AbstractLoginTool.Instance.Login(PaymentCommonServerFragment.this.getActivity(), new LoginTask.LoginCallback() {
													public void loginStatua(boolean isLogin) {
														if(isLogin){
															ActivityTaskManager.getInstance().removeAllActivity();
															ActivityTaskManager.getInstance().removeAllSecondActivity();
														}
													}
												});
//												Intent intent = new Intent();
//												intent.setClass(PaymentCommonServerFragment.this.getActivity(), LoginActivity.class);
//												startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
											}
										});

							} else {// 非会话超时错误拦截
								BaseDroidApp.getInstanse().createDialog(biiError.getCode(), biiError.getMessage(),
										new OnClickListener() {

											@Override
											public void onClick(View v) {
												BaseDroidApp.getInstanse().dismissErrorDialog();
												if (BaseHttpEngine.canGoBack) {
													PaymentCommonServerFragment.this.getActivity().finish();
													BaseHttpEngine.canGoBack = false;
												}
											}
										});
							}
							return true;
						}
						// 弹出公共的错误框
						BaseDroidApp.getInstanse().dismissErrorDialog();
						BaseDroidApp.getInstanse().createDialog("", biiError.getMessage(), new OnClickListener() {
							@Override
							public void onClick(View v) {
								BaseDroidApp.getInstanse().dismissErrorDialog();
								if (BaseHttpEngine.canGoBack) {
									PaymentCommonServerFragment.this.getActivity().finish();
									BaseHttpEngine.canGoBack = false;
								}
							}
						});
					} else {
						BaseDroidApp.getInstanse().dismissErrorDialog();
						// 避免没有错误信息返回时给个默认的提示
						BaseDroidApp.getInstanse().createDialog("", getResources().getString(R.string.request_error),
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										BaseDroidApp.getInstanse().dismissErrorDialog();
										if (BaseHttpEngine.canGoBack) {
											PaymentCommonServerFragment.this.getActivity().finish();
											BaseHttpEngine.canGoBack = false;
										}
									}
								});
					}

					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean httpRequestCallBackAfter(Object resultObj) {
		boolean stopFlag = false;

		if (resultObj instanceof BiiResponse) {
			// Bii请求后拦截
			stopFlag = doBiihttpRequestCallBackAfter((BiiResponse) resultObj);

		} else if (resultObj instanceof String) {

		} else if (resultObj instanceof Map) {

		} else if (resultObj instanceof Bitmap) {

		} else if (resultObj instanceof File) {

		} else {
			// do nothing
		}

		return stopFlag;
	}
	/**
	 * Bii请求后拦截
	 * 
	 * @param resultObj
	 */
	private boolean doBiihttpRequestCallBackAfter(BiiResponse resultObj) {
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
	public void commonHttpErrorCallBack(final String requestMethod) {
		// TODO Auto-generated method stub
		if (Login.LOGOUT_API.equals(requestMethod)) {// 退出的请求 不做任何处理
			return;
		}

		String message = getResources().getString(R.string.communication_fail);
		BaseDroidApp.getInstanse().showMessageDialog(message, new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseDroidApp.getInstanse().dismissErrorDialog();
				if (BaseHttpEngine.canGoBack || LocalData.queryCardMethod.contains(requestMethod)) {
					PaymentCommonServerFragment.this.getActivity().finish();
					BaseHttpEngine.canGoBack = false;
				}

			}
		});
	}

	@Override
	public void commonHttpResponseNullCallBack(String requestMethod) {
		// TODO Auto-generated method stub

	}

}