package com.chinamworld.bocmbci.biz.plps.payment.project.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IHttpCallBack;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.adapter.PaymentProjectAdapter;
import com.chinamworld.bocmbci.biz.plps.interprovincial.InterproLodisActivity;
import com.chinamworld.bocmbci.biz.plps.order.OrderAdressCityActivity;
import com.chinamworld.bocmbci.biz.plps.payment.project.dialogactivity.PaymentMainDialogActivity;
import com.chinamworld.bocmbci.biz.plps.payment.project.dialogactivity.PensionServiceDialogActivity;
import com.chinamworld.bocmbci.biz.plps.prepaid.PrepaidCardDialogActivity;
import com.chinamworld.bocmbci.biz.plps.smallservice.LowServiceActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 某省市所有缴费项目服务小类
 * @author zxj*/
public class PaymentAllProjectFragment extends PlpsBaseActivity{
	// 服务大类彩
	private int[] imageServiceTypes = new int[] { R.drawable.servicetype_0,
			R.drawable.servicetype_1, R.drawable.servicetype_2,
			R.drawable.servicetype_3, R.drawable.servicetype_4,
			R.drawable.servicetype_5, R.drawable.servicetype_6,
			R.drawable.servicetype_7, R.drawable.servicetype_8,
			R.drawable.servicetype_9, R.drawable.servicetype_10,
			R.drawable.servicetype_11, R.drawable.servicetype_12 };
	// 服务大类灰色
	private int[] imageServiceGray = new int[] { R.drawable.servicetypeg_0,
			R.drawable.servicetypeg_1, R.drawable.servicetypeg_2,
			R.drawable.servicetypeg_3, R.drawable.servicetypeg_4,
			R.drawable.servicetypeg_5, R.drawable.servicetypeg_6,
			R.drawable.servicetypeg_7, R.drawable.servicetypeg_8,
			R.drawable.servicetypeg_9, R.drawable.servicetypeg_10,
			R.drawable.servicetypeg_11, R.drawable.servicetypeg_12 };
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
			R.drawable.smallservices_63,R.drawable.smallservices_64,
			R.drawable.smallservices_65};
	// 服务小类灰
	private int[] smallServiceGrayImage = new int[] {
			R.drawable.smallservicesg_0, R.drawable.smallservicesg_1,
			R.drawable.smallservicesg_2, R.drawable.smallservicesg_3,
			R.drawable.smallservicesg_4, R.drawable.smallservicesg_5,
			R.drawable.smallservicesg_6, R.drawable.smallservicesg_7,
			R.drawable.smallservicesg_8, R.drawable.smallservicesg_9,
			R.drawable.smallservicesg_10, R.drawable.smallservicesg_11,
			R.drawable.smallservicesg_12, R.drawable.smallservicesg_13,
			R.drawable.smallservicesg_14, R.drawable.smallservicesg_15,
			R.drawable.smallservicesg_16, R.drawable.smallservicesg_17,
			R.drawable.smallservicesg_18, R.drawable.smallservicesg_19,
			R.drawable.smallservicesg_20, R.drawable.smallservicesg_21,
			R.drawable.smallservicesg_22, R.drawable.smallservicesg_23,
			R.drawable.smallservicesg_24, R.drawable.smallservicesg_25,
			R.drawable.smallservicesg_26, R.drawable.smallservicesg_27,
			R.drawable.smallservicesg_28, R.drawable.smallservicesg_29,
			R.drawable.smallservicesg_30, R.drawable.smallservicesg_31,
			R.drawable.smallservicesg_32, R.drawable.smallservicesg_33,
			R.drawable.smallservicesg_34, R.drawable.smallservicesg_35,
			R.drawable.smallservicesg_36, R.drawable.smallservicesg_37,
			R.drawable.smallservicesg_38, R.drawable.smallservicesg_39,
			R.drawable.smallservicesg_40, R.drawable.smallservicesg_41,
			R.drawable.smallservicesg_42, R.drawable.smallservicesg_43,
			R.drawable.smallservicesg_44, R.drawable.smallservicesg_45,
			R.drawable.smallservicesg_46, R.drawable.smallservicesg_47,
			R.drawable.smallservicesg_48, R.drawable.smallservicesg_49, 
			R.drawable.smallservicesg_50, R.drawable.smallservicesg_51,
			R.drawable.smallservicesg_52,R.drawable.smallservicesg_53,
			R.drawable.smallservicesg_54,R.drawable.smallservicesg_55,
			R.drawable.smallservicesg_56,R.drawable.smallservicesg_57,
			R.drawable.smallservicesg_58,R.drawable.smallservicesg_59,
			R.drawable.smallservicesg_60,R.drawable.smallservicesg_61,
			R.drawable.smallservicesg_62,R.drawable.smallservicesg_63,
			R.drawable.smallservicesg_64};
	private String[] textIds = new String[]{
		"养老金服务",
		"签约代缴服务",
		"预付卡充值",
		"跨省异地交通罚款"
	};
	//所有的服务类项目排序 可缴费-本地服务-不可缴费
	private ArrayList<HashMap<String, Object>> listItem;
	//缴费项目的gridview
	private GridView plpsGridview;
	//缴费项目的adapter
	private PaymentProjectAdapter paymentProjectAdapter;
//	private View view;
	//服务小类catId
	private String catId = null;
	//服务小类名称可缴费
	private String catName = null;
	//服务小类不可缴费
	private String grayCatName = null;
	//服务小类id不可缴费
	private String grayCatId = null;
	//缴费项目个数
	//private int catNameNumber = 0;
	//服务小类返回result
	private List<Map<String, Object>> allPaymentList = new ArrayList<Map<String,Object>>();
	//可缴费的服务小类进行排序
	private List<Map<String, Object>> allPaymentListPosition = new ArrayList<Map<String,Object>>();
	//市多语言代码
//	private String cityDispNo = null;
	//省简称
	private String prvcShortName = null;
	//是否刷新
	private Boolean isNotify=false;
	//地址
	private TextView cityAdres;
	//进入民生缴费获取省显示名称
	private String prvcDispName;
	/**点击缴费服务小类，请求商户标识*/
	private Boolean isFlowFile = false;
	/**第一次进入页面，初始化布局标识*/
	private Boolean isFirst = true;
	/**置灰标识*/
	private String isAlailiablet = null;
	
	
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		view = inflater.inflate(R.layout.plps_all_payment, container, false);
//		plpsGridview = (GridView)view.findViewById(R.id.plps_grid_view);
//		cityAdres = (TextView) PaymentAllProjectFragment.this
//				.findViewById(R.id.cityAdress);
//		cityAdres.setOnClickListener(cityAdresClick);
//		listItem = new ArrayList<HashMap<String,Object>>();
//		prvcDispName = (String)PlpsDataCenter.getInstance().getPrvcDispName();
//		if(!StringUtil.isNullOrEmpty(prvcDispName)){
//			String prvcDispNamet = prvcDispName.trim();
//			if(PlpsDataCenter.provList.contains(prvcDispNamet)){
//				requestPsnPlpsQueryAllPaymentLists();
//			}else {
////				cityAdres.setText(Plps.DEFAULT_AREA);
////				showMessageDialog();
//				getLocalData();
//			}
//		}
//		
////		getData();
////		//加载缴费服务类
//		init();
//		return view;
//	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		inflateLayout(R.layout.plps_all_payment);
		setTitle(getString(R.string.plps_all_payment));
		findViewById(R.id.ib_top_right_btn).setVisibility(View.GONE);
//		plpsGridview = (GridView)findViewById(R.id.plps_grid_view);
		cityAdres = (TextView) PaymentAllProjectFragment.this
				.findViewById(R.id.cityAdress);
		cityAdres.setOnClickListener(cityAdresClick);
		cityAdres.setVisibility(View.VISIBLE);
		String prvcName = (String)PlpsDataCenter.getInstance().getPrvcDispName();
		String cityName = (String)PlpsDataCenter.getInstance().getCityDispName();
		if(StringUtil.isNull(prvcName) || StringUtil.isNull(cityName)){
			cityAdress.setText(Plps.DEFAULT_AREA);
			BaseDroidApp.getInstanse().showMessageDialog(getResources().getString(R.string.plps_choose_area_error),
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							BaseDroidApp.getInstanse()
									.dismissMessageDialog();
						}
					});
		}else {
			if(PlpsDataCenter.municiplGovernment.contains(prvcName.trim())){
				cityAdres.setText(prvcName);
			}else {
				cityAdres.setText(prvcName + cityName);
			}
		}		
		listItem = new ArrayList<HashMap<String,Object>>();
		prvcDispName = (String)PlpsDataCenter.getInstance().getPrvcDispName();
		if(!StringUtil.isNullOrEmpty(prvcDispName)){
			String prvcDispNamet = prvcDispName.trim();
			if(PlpsDataCenter.provList.contains(prvcDispNamet)){
				requestPsnPlpsQueryAllPaymentLists();
			}else {
//				cityAdres.setText(Plps.DEFAULT_AREA);
//				showMessageDialog();
				getLocalData();
			}
		}
		
//		getData();
//		//加载缴费服务类
//		init();
	}
	/**手机客户端不支持省的提示信息*/
//	private void showMessageDialog(){
////		cityAdress.setText(Plps.DEFAULT_AREA);
//		BaseDroidApp.getInstanse().showMessageDialog("手机渠道暂不支持该地区的民生缴费功能，您可以使用账单缴付功能或者通过网银等渠道进行相关缴费",
//				new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						BaseDroidApp.getInstanse()
//								.dismissMessageDialog();
//					}
//				});
//	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		super.onClick(v);
		if (v.getId() == R.id.ib_back) {
			setResult(1001);
			finish();
		}
	}
	
	/**查询所有缴费项目*/
	private void requestPsnPlpsQueryAllPaymentLists(){
		if(isNotify){
			BaseHttpEngine.showProgressDialog();
		}
		// 如果没有查询到默认地区，则不调用接口
		if (StringUtil.isNullOrEmpty(PlpsDataCenter.getInstance()
				.getPrvcShortName())) {
			return;
		}
		
//		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.PSNPLPSALLPAYMENT);
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put(Plps.PRVCSHORTNAME, PlpsDataCenter.getInstance()
				.getPrvcShortName());

		params.put(Plps.CITYDISPNO, PlpsDataCenter.getInstance().getDisplayNo());
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnPlpsQueryAllPaymentListsCallBack");
	}
	@SuppressWarnings("unchecked")
	public void requestPsnPlpsQueryAllPaymentListsCallBack(Object resultObj){
		if(isNotify){
			isNotify = false;
			BaseHttpEngine.dissMissProgressDialog();
		}
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		allPaymentList.clear();
		allPaymentListPosition.clear();
		allPaymentList = (List<Map<String,Object>>)result.get(Plps.ALLPAYMENTLIST);
		if(isFirst){
			isFirst = false;
			inflateLayout(R.layout.plps_all_payment);
			plpsGridview = (GridView)findViewById(R.id.plps_grid_view);
			init();
		}
		getPaymentProjectData();
		paymentProjectAdapter.notifyDataSetChanged();
//		if(StringUtil.isNullOrEmpty(allPaymentList)){
//			getLocalData();
//			if(isNotify){
//				paymentProjectAdapter.notifyDataSetChanged();
//				isNotify = false;
//			}else {
//				init();
//			}
//		}else {
//			  	getPaymentProjectData();
//				if(isNotify){
//					paymentProjectAdapter.notifyDataSetChanged();
//					isNotify = false;
//				}else {
//					init();
//				}
//		}
	}
	/**如果某省的allpaymentList返回为空 则只获取本地的3个服务项目*/
	private void getLocalData() {
		listItem.clear();
		// 本地
		for (int i = 0; i < textIds.length; i++) {
			int imageLocalId = Integer.valueOf(PlpsDataCenter.smallServictTypes
					.get(textIds[i]));
			HashMap<String, Object> localMap = new HashMap<String, Object>();
			localMap.put("isAlailiable", -1-i);
			localMap.put("image", smallServiceImage[imageLocalId]);
			localMap.put("text", textIds[i]);
			listItem.add(localMap);
		}
		if(isNotify){
			isNotify = false;
			paymentProjectAdapter.notifyDataSetChanged();
		}
		
	}
	/** 获取所有缴费项目数据 */
	private void getPaymentProjectData() {
	//	catNameNumber=0;
		listItem.clear();

		if(StringUtil.isNullOrEmpty(allPaymentList)){  // allpaymentList返回为空 则只获取本地的3个服务项目
			getLocalData();
			return;
		}
		
		String isAlailiable = "";
		int imageId;
		HashMap<String, Object> catNameMap = null;
		//可缴费
		for (int i = 0; i < allPaymentList.size(); i++) {
			catId = (String)allPaymentList.get(i).get(Plps.CATID);
			isAlailiable = (String) allPaymentList.get(i).get(Plps.ISAVAILIABLE);
			if(isAlailiable.equals("1") == false)  // 不是可缴费项目
				continue;
			allPaymentListPosition.add(allPaymentList.get(i));
			
		//	catNameNumber++;
			catName = (String) allPaymentList.get(i).get(Plps.CATNAME);
			catNameMap = new HashMap<String, Object>();
			catNameMap.put("isAlailiable", 1);
			if(!StringUtil.isNullOrEmpty(PlpsDataCenter.smallServictTypes.get(catId))){
				imageId = Integer.valueOf(PlpsDataCenter.smallServictTypes.get(catId));
				catNameMap.put("image", smallServiceImage[imageId]);
				catNameMap.put("text", catName);
				listItem.add(catNameMap);
			}else {
				String menuId = (String)allPaymentList.get(i).get(Plps.MUNUID);
				List<Map<String, Object>> menuList = PlpsDataCenter.getInstance().getLiveMenus();
				for(int j=0; j<menuList.size(); j++){
					String munuIdCompare = (String)menuList.get(j).get(Plps.MENUSID);
					if(menuId.equals(munuIdCompare)){
//						String menuName = (String)menuList.get(j).get(Plps.MENUSNAME);
						int imageServiceId ;
						if(!StringUtil.isNullOrEmpty(PlpsDataCenter.serviceTypes.get(menuId))){
							imageServiceId = Integer.valueOf(PlpsDataCenter.serviceTypes.get(menuId));
							catNameMap.put("image", imageServiceTypes[imageServiceId]);;
						}else {
							catNameMap.put("image", "-1");
						}
						catNameMap.put("text", catName);
						listItem.add(catNameMap);
						break;
					}
				}
			}
		}
		
		//不可缴费
		for(int i=0; i<allPaymentList.size(); i++){
			 isAlailiable = (String) allPaymentList.get(i).get(
					Plps.ISAVAILIABLE);
			 if(isAlailiable.equals("0") == false)
				 continue;
			 allPaymentListPosition.add(allPaymentList.get(i));
			grayCatName = (String)allPaymentList.get(i).get(Plps.CATNAME);
			grayCatId = (String)allPaymentList.get(i).get(Plps.CATID);
			catNameMap = new HashMap<String, Object>();
			catNameMap.put("isAlailiable", 1);
			if(!StringUtil.isNullOrEmpty(PlpsDataCenter.smallServiceTypesGray.get(grayCatId))){
				imageId = Integer.valueOf(PlpsDataCenter.smallServiceTypesGray.get(grayCatId));
				catNameMap.put("image", smallServiceGrayImage[imageId]);
				catNameMap.put("text", grayCatName);
				listItem.add(catNameMap);
			}else {
				String menuId = (String)allPaymentList.get(i).get(Plps.MUNUID);
				List<Map<String, Object>> menuList = PlpsDataCenter.getInstance().getLiveMenus();
				for(int j=0; j<menuList.size(); j++){
					String munuIdCompare = (String)menuList.get(j).get(Plps.MENUSID);
					if(menuId.equals(munuIdCompare)){
//						String menuName = (String)menuList.get(j).get(Plps.MENUSNAME);
						if(!StringUtil.isNullOrEmpty(PlpsDataCenter.serviceTypes.get(menuId))){
							int imageServiceId;
							imageServiceId = Integer.valueOf(PlpsDataCenter.serviceTypes.get(menuId));
							catNameMap.put("image", imageServiceTypes[imageServiceId]);;
						}else {
							catNameMap.put("image", "-1");
						}
						catNameMap.put("text", catName);
						listItem.add(catNameMap);
					}
				}
			}
				
		}
		//本地
		for (int i = 0; i < textIds.length; i++) {
			int imageLocalId = Integer.valueOf(PlpsDataCenter.smallServictTypes.get(textIds[i]));
			catNameMap = new HashMap<String, Object>();
			catNameMap.put("isAlailiable", -1-i);
			catNameMap.put("image", smallServiceImage[imageLocalId]);
			catNameMap.put("text", textIds[i]);  
			listItem.add(catNameMap);
		}
//		for(int i=0; i<allPaymentList.size(); i++){
//			String isAvailablePosition = (String)allPaymentList.get(i).get(Plps.ISAVAILIABLE);
//			if(isAvailablePosition.equals("1")){
//				allPaymentListPosition.add(allPaymentList.get(i));
//			}
//		}
	}
	
	//加载服务类控件
	private void init(){
		paymentProjectAdapter = new PaymentProjectAdapter(PaymentAllProjectFragment.this, listItem);
		plpsGridview.setAdapter(paymentProjectAdapter);
		paymentProjectAdapter.notifyDataSetChanged();
		plpsGridview.setOnItemClickListener(gridViewListener);
//		cityAdres.setOnClickListener(cityAdresClick);
		
	}
	OnClickListener cityAdresClick = new OnClickListener() {
		
		@Override
		public void onClick(View paramView) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(PaymentAllProjectFragment.this,
					OrderAdressCityActivity.class);
			intent.putExtra(Plps.PRVCDISPNAME, PlpsDataCenter.getInstance().getPrvcDispName());
			intent.putExtra(Plps.CITYDISPNAME, PlpsDataCenter.getInstance().getCityDispName());
//			startActivity(intent);
			startActivityForResult(intent, 1);
			
		}
	};

	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1){
		if(resultCode == PaymentAllProjectFragment.this.RESULT_OK ){
			String prvcName = data.getStringExtra(Plps.PRVCDISPNAME);
			String cityName = data.getStringExtra(Plps.CITYDISPNAME);
			if(PlpsDataCenter.municiplGovernment.contains(prvcName)){
				cityAdres.setText(prvcName);
			}else {
				cityAdres.setText(prvcName + cityName);
			}
			isNotify = true;
			String prvcNamet = prvcName.trim();
			if(PlpsDataCenter.provList.contains(prvcNamet)){
				requestPsnPlpsQueryAllPaymentLists();
			}else {
				allPaymentList.clear();
				allPaymentListPosition.clear();
				getLocalData();
			}
			
		}
		}else if (requestCode == 1001) {
			if(resultCode == 1001){
				setResult(1001);
				finish();
			}
		}
	}
	//服务小类点击事件
	

	OnItemClickListener gridViewListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long arg3) {
			// TODO Auto-generated method stub
			Map<String,Object> item = listItem.get(position);
			int id = Integer.parseInt(item.get("isAlailiable").toString());		
			if (id == 1) {
				catId = (String) allPaymentListPosition.get(position).get(
						Plps.CATID);
				PlpsDataCenter.getInstance().setItemId(catId);
				String catName = (String) allPaymentListPosition.get(position)
						.get(Plps.CATNAME);
				PlpsDataCenter.getInstance().setCatName(catName);
				String menuId = (String) allPaymentListPosition.get(position)
						.get(Plps.MUNUID);
				isAlailiablet = (String)allPaymentListPosition.get(position).get(Plps.ISAVAILIABLE);
				List<Map<String, Object>> menuList = PlpsDataCenter
						.getInstance().getLiveMenus();
				for (int j = 0; j < menuList.size(); j++) {
					String munuIdCompare = (String) menuList.get(j).get(
							Plps.MENUSID);
					if (menuId.equals(munuIdCompare)) {
						String menuName = (String) menuList.get(j).get(
								Plps.MENUSNAME);
						PlpsDataCenter.getInstance().setMenuName(menuName);
					}
				}
				// cityDispNo =
				// (String)allPaymentList.get(position).get(Plps.CITYDISPNO)
				prvcShortName = PlpsDataCenter.getInstance().getPrvcShortName();
				// 查询支持小类省市区及商户
				requestPsnPlpsQueryFlowFileAdscriptionByPosition();
				//requestPsnPlpsQueryFlowFileAdscription();
				return;
			} else if(id == -1){
//				PaymentAllProjectFragment.this.getActivity().setTitle(R.string.plps_annuity_title);
				Intent intent = new Intent(PaymentAllProjectFragment.this, PensionServiceDialogActivity.class);
				startActivity(intent);
			}else if(id == -2){
//				PaymentAllProjectFragment.this.getActivity().setTitle(R.string.plps_contract_payment);
				Intent intent2 = new Intent(PaymentAllProjectFragment.this, PaymentMainDialogActivity.class);
				startActivity(intent2);
			}else if (id == -3) {
//				PaymentAllProjectFragment.this.getActivity().setTitle(R.string.plps_prepaid_card);
				startActivity(new Intent(PaymentAllProjectFragment.this, PrepaidCardDialogActivity.class));
			}else if (id == -4) {
				startActivity(new Intent(PaymentAllProjectFragment.this, InterproLodisActivity.class));
			}
//			else {
//				BaseDroidApp.getInstanse().showMessageDialog("该缴费品种需要您办理该地区卡", new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						BaseDroidApp.getInstanse().dismissMessageDialog();
//					}
//				});
//			}

		}
	};

	/**
	 *  查询支持小类省市区及商户 */
	private void requestPsnPlpsQueryFlowFileAdscriptionByPosition(){
		isFlowFile = true;
		BaseHttpEngine.showProgressDialog();
		requestCommConversationId(new IHttpCallBack(){
			@Override
			public void requestHttpSuccess(Object result) {
				// TODO Auto-generated method stub
				requestPsnPlpsQueryFlowFileAdscription();
				
			}});
	}
	
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
		if(!isFlowFile){
			isFlowFile = false;
			BaseHttpEngine.dissMissProgressDialog();
		}
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String commConversationId = (String) biiResponseBody.getResult();
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.CONVERSATION_ID, commConversationId);
//		requestPsnPlpsQueryCommonUsedPaymentList();
		if(requestHttpCallBack != null)
			requestHttpCallBack.requestHttpSuccess(resultObj);
	}
	
	/**
	 * 查询支持小类省市区及商户*/
	private void requestPsnPlpsQueryFlowFileAdscription(){
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.QUERYFLOWFILEA);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Plps.ITEMID, catId);
		params.put(Plps.QUERYTYPET, "3");
		params.put(Plps.PRVCSHORTNAME, prvcShortName);
		params.put(Plps.CITYDISPNO, PlpsDataCenter.getInstance().getDisplayNo());
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, PaymentAllProjectFragment.this, "requestPsnPlpsQueryFlowFileAdscriptionCallBack");
	}
	@SuppressWarnings("unchecked")
	public void requestPsnPlpsQueryFlowFileAdscriptionCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		List<Map<String, Object>> queryFlowFile = new ArrayList<Map<String,Object>>();
		if (!StringUtil.isNullOrEmpty(result)) {
			queryFlowFile = (List<Map<String, Object>>) result
					.get(Plps.FLOWFILELIST);
		}
		if(StringUtil.isNullOrEmpty(queryFlowFile)){return;}
		PlpsDataCenter.getInstance().setQueryFlowFile(queryFlowFile);
		startActivityForResult(new Intent(PaymentAllProjectFragment.this, LowServiceActivity.class)
			.putExtra(Plps.ISAVAILIABLE, isAlailiablet),1001);
	}

}