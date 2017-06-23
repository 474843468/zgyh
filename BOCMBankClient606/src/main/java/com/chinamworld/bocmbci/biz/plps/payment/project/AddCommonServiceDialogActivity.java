package com.chinamworld.bocmbci.biz.plps.payment.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.adapter.AddCommonServiceAdapter;
import com.chinamworld.bocmbci.biz.plps.order.OrderAdressCityActivity;
import com.chinamworld.bocmbci.biz.plps.smallservice.LowServiceActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 添加服务小类页面
 * @author Administrator zxj*/
public class AddCommonServiceDialogActivity extends PlpsBaseActivity{
	//服务小类返回result
	private List<Map<String, Object>> allPaymentList = new ArrayList<Map<String,Object>>();
	//置灰小类返回顺序
	private List<Map<String, Object>> allPaymentListPosition = new ArrayList<Map<String,Object>>();
	//所有的服务类项目排序 可缴费-本地服务-不可缴费
	private ArrayList<HashMap<String, Object>> listItem;
	//服务小类名
	private String catName;
	private GridView paymentGridView;
	//缴费项目的adapter
	private AddCommonServiceAdapter addCommonServiceAdapter;
	//缴费项目唯一id
	private String catId=null;
	//市多语言代码
	private String cityDispNo = null;
	//省简称
	private String prvcShortName = null;
	/** 选中记录项 */
	public int selectposition = -1;
	//是否添加新服务
	private Boolean isServiceAdd=false;
	//添加新服务次数            最多添加8个，添加第9个时，前端控制报错
//	private int catNumber =0;
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
			R.drawable.smallservices_63,R.drawable.smallservices_64};
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
			R.drawable.smallservicesg_52, R.drawable.smallservicesg_53,
			R.drawable.smallservicesg_54, R.drawable.smallservicesg_55,
			R.drawable.smallservicesg_56, R.drawable.smallservicesg_57,
			R.drawable.smallservicesg_58, R.drawable.smallservicesg_59,
			R.drawable.smallservicesg_60, R.drawable.smallservicesg_61,
			R.drawable.smallservicesg_62, R.drawable.smallservicesg_63,
			R.drawable.smallservicesg_64 };
	//被选的服务小类名
	private String selectedCatName;
	//置灰小类
	private String grayCatName = null;
	//置灰小类id
	private String grayCatId = null;
    //大类id
    private String menuId = null;
    private String munuIdCompare = null;
    private String isAlailiablet= null;
	private boolean isRefresh = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("所有缴费项目");
		inflateLayout(R.layout.plps_add_common_service);
		mRightButton.setVisibility(View.GONE);
		cityAdress = (TextView) AddCommonServiceDialogActivity.this
				.findViewById(R.id.cityAdress);
		cityAdress.setVisibility(View.VISIBLE);
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
				cityAdress.setText(prvcName);
			}else {
				cityAdress.setText(prvcName + cityName);
			}
		}
		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		RelativeLayout lp_bank =(RelativeLayout)findViewById(R.id.lp_bank);
		android.widget.RelativeLayout.LayoutParams lp = new android.widget.RelativeLayout.LayoutParams(
				LayoutValue.SCREEN_WIDTH, LayoutValue.SCREEN_HEIGHT * 7 / 8);
		lp_bank.setLayoutParams(lp);
		listItem = new ArrayList<HashMap<String, Object>>();
		getData();
		init();
	}

	/** 获取该地区所有缴费项目 */
	private void getData() {
		if(!isRefresh){
			allPaymentList = PlpsDataCenter.getInstance().getAllPaymentList();
		}else {
			isRefresh = false;
			listItem.clear();
		}
		int imageId;
		for (int i = 0; i < allPaymentList.size(); i++) {
			String isAlailiable = (String) allPaymentList.get(i).get(
					Plps.ISAVAILIABLE);
			if (isAlailiable.equals("1")) {
				allPaymentListPosition.add(allPaymentList.get(i));
				catName = (String) allPaymentList.get(i).get(Plps.CATNAME);
				catId = (String)allPaymentList.get(i).get(Plps.CATID);
				HashMap<String, Object> catNameMap = new HashMap<String, Object>();
				if (!StringUtil.isNullOrEmpty(PlpsDataCenter.smallServictTypes
						.get(catId))) {
					imageId = Integer.valueOf(PlpsDataCenter.smallServictTypes
							.get(catId));
					catNameMap.put("image", smallServiceImage[imageId]);
					catNameMap.put("text", catName);
					listItem.add(catNameMap);
				} else {
					String menuId = (String) allPaymentList.get(i).get(Plps.MUNUID);
					List<Map<String, Object>> menuList = PlpsDataCenter
							.getInstance().getLiveMenus();
					for (int j = 0; j < menuList.size(); j++) {
						String munuIdCompare = (String) menuList.get(j).get(
								Plps.MENUSID);
						if (menuId.equals(munuIdCompare)) {
//							String menuName = (String) menuList.get(j).get(
//									Plps.MENUSNAME);
							if(!StringUtil.isNullOrEmpty(PlpsDataCenter.serviceTypes
											.get(menuId))){
								int imageServiceId = Integer
										.valueOf(PlpsDataCenter.serviceTypes
												.get(menuId));
								catNameMap.put("image",
										imageServiceTypes[imageServiceId]);
							}else {
								catNameMap.put("image", "-1");
							}
							catNameMap.put("text", catName);
							listItem.add(catNameMap);
						}
					}
				}
			}
		}
		// 置灰小类
		for (int i = 0; i < allPaymentList.size(); i++) {
			String isAlailiable = (String) allPaymentList.get(i).get(
					Plps.ISAVAILIABLE);
			if (isAlailiable.equals("0")) {
				allPaymentListPosition.add(allPaymentList.get(i));
				grayCatName = (String) allPaymentList.get(i).get(Plps.CATNAME);
				grayCatId = (String)allPaymentList.get(i).get(Plps.CATID);
				int imageGrayId;
				HashMap<String, Object> grayMap = new HashMap<String, Object>();
				if (!StringUtil
						.isNullOrEmpty(PlpsDataCenter.smallServiceTypesGray
								.get(grayCatId))) {
					imageGrayId = Integer
							.valueOf(PlpsDataCenter.smallServiceTypesGray
									.get(grayCatId));
					grayMap.put("image", smallServiceGrayImage[imageGrayId]);
					grayMap.put("text", grayCatName);
					listItem.add(grayMap);
				} else {
					String menuId = (String) allPaymentList.get(i).get(
							Plps.MUNUID);
					List<Map<String, Object>> menuList = PlpsDataCenter
							.getInstance().getLiveMenus();
					for (int j = 0; j < menuList.size(); j++) {
						String munuIdCompare = (String) menuList.get(j).get(
								Plps.MENUSID);
						if (menuId.equals(munuIdCompare)) {
//							String menuName = (String) menuList.get(j).get(
//									Plps.MENUSNAME);
							if(!StringUtil.isNullOrEmpty(PlpsDataCenter.serviceTypesGray
									.get(menuId))){
								int imageServiceId = Integer
										.valueOf(PlpsDataCenter.serviceTypesGray
												.get(menuId));
								grayMap.put("image",
										imageServiceGray[imageServiceId]);
							}else {
								grayMap.put("image", "-1");
							}
							grayMap.put("text", grayCatName);
							listItem.add(grayMap);
						}
					}
				}

			}
		}
	}
	/**加载服务小类*/
	private void init(){
		paymentGridView = (GridView)findViewById(R.id.plps_grid_view);
		addCommonServiceAdapter = new AddCommonServiceAdapter(this, listItem);
		paymentGridView.setAdapter(addCommonServiceAdapter);
		addCommonServiceAdapter.notifyDataSetChanged();
		paymentGridView.setOnItemClickListener(new gridViewListener());
		cityAdress.setOnClickListener(cityAdresClick);
	}
	OnClickListener cityAdresClick = new OnClickListener() {

		@Override
		public void onClick(View paramView) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(AddCommonServiceDialogActivity.this,
					OrderAdressCityActivity.class);
			intent.putExtra(Plps.PRVCDISPNAME, PlpsDataCenter.getInstance().getPrvcDispName());
			intent.putExtra(Plps.CITYDISPNAME, PlpsDataCenter.getInstance().getCityDispName());
//			startActivity(intent);
			startActivityForResult(intent, 1);

		}
	};
	public class gridViewListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long arg3) {
			// TODO Auto-generated method stub

			if (selectposition == position) {
				return;
			} else {
					catId = (String)allPaymentListPosition.get(position).get(Plps.CATID);
					selectedCatName = (String)allPaymentListPosition.get(position).get(Plps.CATNAME);
                    PlpsDataCenter.getInstance().setCatName(selectedCatName);
					cityDispNo = PlpsDataCenter.getInstance().getDisplayNo();
                    isAlailiablet = (String)allPaymentListPosition.get(position).get(Plps.ISAVAILIABLE);
                    menuId = (String)allPaymentListPosition.get(position).get(Plps.MUNUID);
                    List<Map<String, Object>> menuList = PlpsDataCenter
                        .getInstance().getLiveMenus();
                    for(int i=0; i<menuList.size(); i++){
                        munuIdCompare = (String) menuList.get(i).get(
                                Plps.MENUSID);
                        if(menuId.equals(munuIdCompare)){
                            PlpsDataCenter.getInstance().setMenuName((String)menuList.get(i).get(Plps.MENUSNAME));
                            break;
                        }
                    }
//					cityDispNo = (String)allPaymentList.get(position).get(Plps.CITYDISPNO);
//					prvcShortName = (String)allPaymentList.get(position).get(Plps.PRVCSHORTNAME);
					prvcShortName = PlpsDataCenter.getInstance().getPrvcShortName();
//					selectposition = position;
//					addCommonServiceAdapter.setSelectedPosition(position);
					// 查询支持小类省市区及商户
//					requestPsnPlpsQueryFlowFileAdscription();
					String isAvailabl = (String)allPaymentListPosition.get(position).get(Plps.ISAVAILIABLE);
					if(isAvailabl.equals("1")){
                        requestPsnPlpsQueryFlowFileAdscription();
					}else {
						BaseDroidApp.getInstanse().showErrorDialog("该缴费品种需要您办理该地区卡，是否继续添加？",
								R.string.cancle, R.string.confirm, new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										BaseDroidApp.getInstanse().dismissErrorDialog();
										switch (v.getId()) {
										case R.id.exit_btn:// 取消

											break;
										case R.id.retry_btn:// 确定
                                            requestPsnPlpsQueryFlowFileAdscription();
											break;
										default:
											break;
										}
									}
								});
					}

			}
		}
	}
//
//	public void btnNextOnclick(View v) {
//		if(isServiceAdd){
//			setResult(RESULT_OK);
//			finish();
//		}else {
//			finish();
//		}
//	}
//	@Override
//	public void requestCommConversationIdCallBack(Object resultObj) {
//		// TODO Auto-generated method stub
//		super.requestCommConversationIdCallBack(resultObj);
//
//	}
	/**
	 * 查询支持小类省市区及商户*/
	private void requestPsnPlpsQueryFlowFileAdscription(){
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.QUERYFLOWFILEA);
		biiRequestBody.setConversationId((String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Plps.ITEMID, catId);
		params.put(Plps.QUERYTYPET, "3");
		params.put(Plps.PRVCSHORTNAME, prvcShortName);
		params.put(Plps.CITYDISPNO, cityDispNo);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnPlpsQueryFlowFileAdscriptionCallBack");
	}
	@SuppressWarnings("unchecked")
	public void requestPsnPlpsQueryFlowFileAdscriptionCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		List<Map<String, Object>> queryFlowFile= (List<Map<String, Object>>) result.get(Plps.FLOWFILELIST);
		if(StringUtil.isNullOrEmpty(queryFlowFile)){return;}
		PlpsDataCenter.getInstance().setQueryFlowFile(queryFlowFile);
		startActivityForResult(new Intent(AddCommonServiceDialogActivity.this, LowServiceActivity.class)
//			.putExtra("Flag", true)
            .putExtra("isAvailable",isAlailiablet),1001);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
//		if(resultCode == RESULT_OK){
//			isServiceAdd = true;
//			setResult(1011);
//			finish();
//		}
		if(requestCode == 1){
			if(resultCode == AddCommonServiceDialogActivity.this.RESULT_OK ){
				String prvcName = data.getStringExtra(Plps.PRVCDISPNAME);
				String cityName = data.getStringExtra(Plps.CITYDISPNAME);
				cityAdress = (TextView) AddCommonServiceDialogActivity.this
						.findViewById(R.id.cityAdress);
				if(PlpsDataCenter.municiplGovernment.contains(prvcName)){
					cityAdress.setText(prvcName);
				}else {
					cityAdress.setText(prvcName + cityName);
				}
				String prvcNamet = prvcName.trim();
				if(PlpsDataCenter.provList.contains(prvcNamet)){
					requestPsnPlpsQueryAllPaymentLists();
				}
			}
		}
	}
	/**查询所有缴费项目*/
	private void requestPsnPlpsQueryAllPaymentLists(){
		BaseHttpEngine.showProgressDialog();
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
	public void requestPsnPlpsQueryAllPaymentListsCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		isRefresh = true;
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		allPaymentList.clear();
		allPaymentListPosition.clear();
		allPaymentList = (List<Map<String,Object>>)result.get(Plps.ALLPAYMENTLIST);
		getData();
		addCommonServiceAdapter.notifyDataSetChanged();
	}
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ib_back) {
			if(isServiceAdd){
				isServiceAdd = false;
				setResult(RESULT_OK);
				finish();
			}
			finish();
		}
		else if(v.getId() == R.id.ib_top_right_btn){
			PlpsDataCenter.getInstance().clearAllData();
			ActivityTaskManager.getInstance().removeAllActivity();
		}

	}
}
