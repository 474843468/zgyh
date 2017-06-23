//package com.chinamworld.bocmbci.biz.plps.payment.project;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemSelectedListener;
//import android.widget.Spinner;
//
//import com.chinamworld.bocmbci.R;
//import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
//import com.chinamworld.bocmbci.base.application.BaseDroidApp;
//import com.chinamworld.bocmbci.bii.BiiRequestBody;
//import com.chinamworld.bocmbci.bii.constant.Plps;
//import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
//import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
//import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
//import com.chinamworld.bocmbci.biz.plps.adapter.PaymentSpinnerAdapter;
//import com.chinamworld.bocmbci.http.HttpManager;
//import com.chinamworld.bocmbci.http.HttpTools;
//import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
//import com.chinamworld.bocmbci.utils.PublicTools;
//import com.chinamworld.bocmbci.utils.StringUtil;
///**
// * 添加某地区的常用服务
// * @author zxj*/
//public class AddProvServiceActivity extends PlpsBaseActivity implements OnItemSelectedListener{
//	// 省/直辖市
//	private Spinner provCitySpinner;
//	// 市
//	private Spinner citySpinner;
////	private List<Map<String, Object>> provinceList;
//	private List<String> provinceList;
//	private int proSelectedPosition;
//	private int citySelectedPosition;
//	private String provName;
//	//省简称
//	private String provShortName;
//	private String cityName;
//	//市列表
//	private List<Map<String, Object>> cityList = new ArrayList<Map<String,Object>>();
//	private int isFirst;
//	private PaymentSpinnerAdapter cityAdapter;
//	private Boolean isServiceAdd=false;
//	private final List<String> defaultList = new ArrayList<String>() {
//		private static final long serialVersionUID = 1L;
//		{
//			add(Plps.SP_DEFUALTTXT);
//		}
//	};
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setTitle(R.string.plsp_common_name);
//		cityAdress.setVisibility(View.GONE);
//		mRightButton.setVisibility(View.VISIBLE);
//		inflateLayout(R.layout.plps_prov_service);
////		requestPlpsGetProvinceListt();
//		provinceList = (List<String>)PlpsDataCenter.provList;
//		init();
//	}
//
//	// 查询民生缴费项目的省列表
////	private void requestPlpsGetProvinceListt() {
////		BaseHttpEngine.showProgressDialog();
////		BiiRequestBody biiRequestBody = new BiiRequestBody();
////		biiRequestBody.setMethod(Plps.PLPSGETPROVINCELIST);
////		biiRequestBody.setParams(null);
////		HttpManager.requestBii(biiRequestBody, this,
////				"requestPlpsGetProvinceListtCallBack");
////	}
////
////	@SuppressWarnings("unchecked")
////	public void requestPlpsGetProvinceListtCallBack(Object resultObj) {
////		// TODO Auto-generated method stub
////		BaseHttpEngine.dissMissProgressDialog();
////		Map<String, Object> result = (Map<String, Object>) PlpsUtils
////				.httpResponseDeal(resultObj);
////		provinceList = (List<Map<String,Object>>)result.get(Plps.PLPSPROVINCELIST);
////		init();
////	}
//
//	private void init() {
//		provCitySpinner = (Spinner) findViewById(R.id.spinner_prov);
//		citySpinner = (Spinner) findViewById(R.id.spinner_city);
//		provCitySpinner.setOnItemSelectedListener(this);
//		setSpinnerBackground(true,provCitySpinner);
//		setSpinnerBackground(false,citySpinner);
//		citySpinner.setOnItemSelectedListener(this);
//		cityAdapter = new PaymentSpinnerAdapter(this, defaultList);
//		citySpinner.setAdapter(cityAdapter);
//		PlpsUtils.initSpinnerView(this, provCitySpinner, PlpsUtils
//				.initSpinnerProvinceData(provinceList, Plps.SP_DEFUALTTXT));
//		
//	}
//	/**
//	 * 设置sipnner背景
//	 * @param isdefault
//	 * @param v
//	 */
//	private void setSpinnerBackground(boolean isdefault,View... v){
//		if(v.length > 0){
//			for(View sp : v){
//				if(sp != null){
//					if (isdefault){
//						sp.setClickable(true);
//						sp.setBackgroundResource(R.drawable.bg_spinner);
//					}else{
//						sp.setClickable(false);
//						sp.setBackgroundResource(R.drawable.bg_spinner_default);
//					}
//				}
//			}
//		}
//	}
//	/**
//	 * 通过省查询市*/
//	private void requestGetCityListByPrvcShortName(){
//		BaseHttpEngine.showProgressDialog();
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(Plps.GETCITYLISTBYPRVC);
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put(Plps.PRVCSHORTNAME, provShortName);
//		biiRequestBody.setParams(params);
//		HttpManager.requestBii(biiRequestBody, this, "requestGetCityListByPrvcShortNameCallBack");
//	}
//	
//	@SuppressWarnings("unchecked")
//	public void requestGetCityListByPrvcShortNameCallBack(Object resultObj){
//		BaseHttpEngine.dissMissProgressDialog();
//		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
//		cityList = (List<Map<String, Object>>) resultMap.get(Plps.CITYLIST);
//		if(StringUtil.isNullOrEmpty(cityList)){
//			citySpinner.setSelection(0);
//			setSpinnerBackground(false, citySpinner);
//		}else {
//			citySpinner.setSelection(0);
//			cityAdapter.setList(PublicTools.getSpinnerDataWithDefaultValue(cityList, Plps.CITYNAME, Plps.SP_DEFUALTTXT));
////			PlpsUtils.initSpinnerView(this, citySpinner, PlpsUtils
////					.initSpinnerData(cityList, Plps.CITYDISPNAME,
////							Plps.SP_DEFUALTTXT));
//			setSpinnerBackground(true, citySpinner);
//		}
//		
//	}
//	
//	public void btnQueryOnclick(View v){
//		if ((provCitySpinner.getSelectedItem().toString()).equals(Plps.SP_DEFUALTTXT)) {
//			BaseDroidApp.getInstanse().showInfoMessageDialog("请选择省/直辖市");
//			return;
//		} else if ((citySpinner.getSelectedItem().toString())
//				.equals(Plps.SP_DEFUALTTXT)) {
//			BaseDroidApp.getInstanse().showInfoMessageDialog("请选择城市");
//			return;
//		}else {
////			PlpsDataCenter.getInstance().setPrvcDispName(provName);
////			PlpsDataCenter.getInstance().setCityDispName(citySpinner.getSelectedItem().toString());
//			for(int i=0; i<cityList.size(); i++){
//				String cityName = (String)cityList.get(i).get(Plps.CITYNAME);
//				if(cityName.equals(citySpinner.getSelectedItem().toString())){
//					String displayNo = (String)cityList.get(i).get(Plps.DISPLAYNO);
//					PlpsDataCenter.getInstance().setDisplayNo(displayNo);
//				}
//			}
//			requestPsnPlpsQueryAllPaymentLis();
//		}
//	}
//	/**查询某地区所有缴费项目*/
//	public void requestPsnPlpsQueryAllPaymentLis() {
//		BaseHttpEngine.showProgressDialog();
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(Plps.PSNPLPSALLPAYMENT);
//		Map<String, Object> params = new HashMap<String, Object>();
//		if (StringUtil.isNullOrEmpty(PlpsDataCenter.getInstance()
//				.getPrvcShortName())) {
//			return;
//		}
//		params.put(Plps.PRVCSHORTNAME, provShortName);
////		if (StringUtil.isNullOrEmpty(PlpsDataCenter.getInstance()
////				.getCityDispName())) {
////			return;
////		}
////		params.put(Plps.CITY, PlpsDataCenter.getInstance().getCityDispName());
//		params.put(Plps.CITYDISPNO, PlpsDataCenter.getInstance().getDisplayNo());
//		biiRequestBody.setParams(params);
//		HttpManager.requestBii(biiRequestBody, this,
//				"requestPsnPlpsQueryAllPaymentLisCallBack");
//	}
//	
//	@SuppressWarnings("unchecked")
//	public void requestPsnPlpsQueryAllPaymentLisCallBack(Object resultObj) {
//		// TODO Auto-generated method stub
//		BaseHttpEngine.dissMissProgressDialog();
//		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
//		List<Map<String, Object>> allPaymentList = (List<Map<String,Object>>)result.get(Plps.ALLPAYMENTLIST);
//		PlpsDataCenter.getInstance().setAllPaymentList(allPaymentList);
//		startActivityForResult(new Intent(AddProvServiceActivity.this, AddCommonServiceDialogActivity.class),1001);
//	}
//	
//
//	@Override
//	public void onItemSelected(AdapterView<?> parent, View view, int position,
//			long arg3) {
//		// TODO Auto-generated method stub
//		if (isFirst < 1) {isFirst++;return;}
//		if(parent== provCitySpinner){
//			if(position ==0){
//				setSpinnerBackground(true,provCitySpinner);
//				setSpinnerBackground(false,citySpinner);
//				cityAdapter.setList(defaultList);
//			}else {
//				proSelectedPosition = position;
////				provName = (String) provinceList.get(position).get(Plps.PRVCDISPNAME);
////				provShortName = (String)provinceList.get(position).get(Plps.PRVCSHORTNAME);
//				provName = (String)provinceList.get(position-1);
//				provShortName = (String)PlpsDataCenter.mapCode_prov.get(provName);
//				PlpsDataCenter.getInstance().setPrvcShortName(provShortName);
//				requestGetCityListByPrvcShortName();
//			}
//		}
//	}
//
//	@Override
//	public void onNothingSelected(AdapterView<?> arg0) {
//		// TODO Auto-generated method stub
//		
//	}
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		if(resultCode == RESULT_OK){
//			isServiceAdd = true;
//			provCitySpinner.setSelection(0);
//			citySpinner.setSelection(0);
//			setSpinnerBackground(true,provCitySpinner);
//			setSpinnerBackground(false,citySpinner);
//		}
//		if(resultCode == 1011){
//			setResult(1011);
//			finish();
//		}
//	}
//	@Override
//	public void onClick(View v) {
//		if (v.getId() == R.id.ib_back) {
//			if(isServiceAdd){
//				isServiceAdd = false;
//				setResult(RESULT_OK);
//				finish();
//			}
//			finish();
//		}
//		else if(v.getId() == R.id.ib_top_right_btn){
//			PlpsDataCenter.getInstance().clearAllData();
//			ActivityTaskManager.getInstance().removeAllActivity();
//		}
//	}
//	
//
//}
