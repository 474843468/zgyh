package com.chinamworld.bocmbci.biz.loan.loanApply;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Blpt;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.blpt.bdlocation.BDLocationCenter;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.biz.loan.LoanDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 贷款申请填写页面 */
public class LoanApplyChooseActivity extends LoanBaseActivity {
	private static final String TAG = "LoanQuotaQueryDetailActivity";
	private LoanApplyChooseActivity instance;
	private View detailView = null;
	private int positions = -1;
	private int provincePositions = -1;
	private int cityPositions = -1;
	private int typePositions = -1;

	/** 用于存储省份的List */
	List<String> provinceList = null;
	/** 用于存储省份编号的List */
	List<String> provinceCodeList = null;

	/** 用于存储城市的List */
	List<String> cityList = null;
	/** 用于存储城市编号的List */
	List<String> cityCodeList = null;

	/** 用于存储城市能办理业务的List */
	List<String> typeList = null;
	/** 用于存储城市能办理业务编号的List */
	List<String> typeCodeList = null;
	/** 被选中的城市编码 */
	private String cityCode;
	/** 被选中的业务编码 */
	private String typeCode;

	private Button nextButton = null;
	/** 当前省份 */
	private String province;
	/** 当前城市 */
	private String city;
	private String locaCity;
	/** 选择的贷款品种 */
	private String type;
	/** 定位Handler **/
	private MyHandler mHandler;
	/** 定位线程 */
	private LoctionThread loction;
	private int tag;
	private boolean locationSU, bs,locationWin,cityWin;
	private boolean isToprovince;
	private TextView text_spinner_select_province, text_spinner_select_city,
			text_spinner_loantype, tv_loan_new_pay_acc_label;
	private Spinner select_province, select_city, spinner_type;
	private BDLocationCenter bdLocationCenter;
	/** 省份列表List */
	List<Map<String, Object>> pResultList;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.loan_apply_loan_title));
		Button backButton = (Button) findViewById(R.id.ib_back);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		// 省份列表
		pResultList=LoanDataCenter.getInstance().getResApplyprovinceList();
		init();
		setList();
		ctrolViewVisible();
	}

	private void init() {
		detailView = LayoutInflater.from(this).inflate(
				R.layout.loan_apply_choose, null);
		tabcontentView.addView(detailView);
		text_spinner_select_province = (TextView) findViewById(R.id.text_spinner_select_province);
		text_spinner_select_city = (TextView) findViewById(R.id.text_spinner_select_city);
		text_spinner_select_city.setClickable(false);
		text_spinner_loantype = (TextView) findViewById(R.id.text_spinner_loantype);
		text_spinner_loantype.setClickable(false);
		tv_loan_new_pay_acc_label = (TextView) findViewById(R.id.tv_loan_new_pay_acc_label);
		select_province = (Spinner) findViewById(R.id.loan_apply_select_province);
		select_city = (Spinner) findViewById(R.id.loan_apply_select_city);
		spinner_type = (Spinner) findViewById(R.id.loan_apply_spinner_type);
		select_province.setOnItemSelectedListener(itemSelectedListener);
		select_city.setOnItemSelectedListener(itemSelectedListener);
		spinner_type.setOnItemSelectedListener(itemSelectedListener);
		nextButton = (Button) findViewById(R.id.loan_tradeButton);
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (provincePositions <= 0) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getResources().getString(
									R.string.loan_apply_city_error));
					return;
				}
				if (cityPositions <= 0) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getResources().getString(
									R.string.loan_apply_city_error));
					return;
				}
				if (typePositions <= 0) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getResources().getString(
									R.string.loan_apply_type_error));
					return;
				}
				requestPsnOnLineLoanFieldQry(typeCode);
			}
		});
	}

	private void ctrolViewVisible() {
		for (int i = 0; i < pResultList.size(); i++) {
			provinceList.add((String) pResultList.get(i).get(
					Loan.LOAN_APPLY_PROVINCE_QRY));
			provinceCodeList.add((String) pResultList.get(i).get(
					Loan.LOAN_APPLY_PROVINCECODE_QRY));

		}
		// 判断是否开通GPS定位
		if (getIsOpenGps()) {

			locationSU = true;
			locationCtty();
		} else {
			 BaseDroidApp.getInstanse().showInfoMessageDialog(
			 getResources().getString(R.string.loan_apply_location_fail));
//			BaseDroidApp.getInstanse().showInfoMessageDialog(
//					"定位失败，请您手动选择所在城市。");
			locationSU = false;
			if (provinceList.size() > 1 || provinceCodeList.size() > 1) {
				provinceList.clear();
				provinceList.add("请选择");
				provinceCodeList.clear();
			}
			for (int i = 0; i < pResultList.size(); i++) {
				provinceList.add((String) pResultList.get(i).get(
						Loan.LOAN_APPLY_PROVINCE_QRY));
				provinceCodeList.add((String) pResultList.get(i).get(
						Loan.LOAN_APPLY_PROVINCECODE_QRY));

			}

			initProvinceSpinner(select_province, provinceList, 0);

		}

	}

	/** 获得用户输入的信息 */
	private void getValue() {
	}

	/** 设置省份 城市 贷款样式 的list集合 */
	private void setList() {
		provinceList = new ArrayList<String>();
		provinceCodeList = new ArrayList<String>();
		provinceList.add("请选择");

		cityList = new ArrayList<String>();
		cityCodeList = new ArrayList<String>();
		cityList.add("请选择");

		typeList = new ArrayList<String>();
		typeCodeList = new ArrayList<String>();
		typeList.add("请选择");
	}

	private boolean getIsOpenGps() {
		boolean isOpen = false;
		LocationManager lm = (LocationManager) this
				.getSystemService(Service.LOCATION_SERVICE);
		isOpen = lm
				.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
		LogGloble.d(TAG, " GPS isOpen ==" + isOpen);
		return isOpen;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		closeBDClient();
	}

	/*** 定位城市 */
	private void locationCtty() {
		mHandler = new MyHandler();
		bdLocationCenter = new BDLocationCenter(mHandler,
				LoanApplyChooseActivity.this);
		loction = new LoctionThread();
		loction.start();
		String message = getString(R.string.loan_apply_location_fail);
		BaseDroidApp.getInstanse()
				.createLocationDialog(0, "", message, onclick);
	}

	/** 关闭定位 */
	private OnClickListener onclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (loction.isAlive()) {
				loction.interrupt();
			}
			closeBDClient();
			BaseDroidApp.getInstanse().dismissErrorDialog();
			locationSU = false;
			initProvinceSpinner(select_province, provinceList, 0);
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.loan_apply_location_fail));
			return;
		}
	};

	/*** 关闭百度定位服务 */
	private void closeBDClient() {
		if (bdLocationCenter == null)
			return;
		if (bdLocationCenter.mLocationClient != null) {
			bdLocationCenter.mLocationClient.unRegisterLocationListener(null);
			bdLocationCenter.mLocationClient.stop();
//			initProvinceSpinner(select_province, provinceList, 0);
			bs = true;
		}
	}

	/*** 定位线程 */
	class LoctionThread extends Thread {
		@Override
		public void run() {
			super.run();

			bdLocationCenter.getLocationInfo();
		}
	}

	public class MyHandler extends Handler {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle b = msg.getData();
			getLocaInfo(b);
		}
	}

	private void getLocaInfo(Bundle b) {
		if ((b.getString(Blpt.KEY_CITY)).contains("市")) {
			locaCity = b.getString(Blpt.KEY_CITY).substring(0,
					b.getString(Blpt.KEY_CITY).length() - 1);
		} else {
			locaCity = b.getString(Blpt.KEY_CITY);
		}
		// province =
		// BlptUtil.getInstance().provZhNameToShortName(b.getString(Blpt.KEY_PROVICENAME));
		if ((b.getString(Blpt.KEY_PROVICENAME)).contains("省") || 
				(b.getString(Blpt.KEY_PROVICENAME)).contains("市")) {
			province = b.getString(Blpt.KEY_PROVICENAME).substring(0, b.getString(Blpt.KEY_PROVICENAME).length()-1);			
		}else {
			province = b.getString(Blpt.KEY_PROVICENAME);
		}

		BaseDroidApp.getInstanse().dismissErrorDialog();
		if (StringUtil.isNull(locaCity)) {
			locationSU = false;
		} else {
			locationSU = true;
		}
		String pCode = "";
		String cCode = "";
		//判断省份是否为空
		if (!StringUtil.isNull(province)) {

			List<String> provinceList = new ArrayList<String>();
			provinceList.add("请选择");
			
			for (int i = 0; i < pResultList.size(); i++) {
				provinceList.add((String) pResultList.get(i).get(
						Loan.LOAN_APPLY_PROVINCE_QRY));
				if (pResultList.get(i).get(Loan.LOAN_APPLY_PROVINCE_QRY)
						.equals(province)) {
					locationWin=true;
					text_spinner_select_province.setText(province);
					pCode = (String) pResultList.get(i).get(
							Loan.LOAN_APPLY_PROVINCECODE_QRY);
				}
			}
//			if(locationWin==true){
				int position = provinceList.indexOf(province);
				initProvinceSpinner(select_province, provinceList, position);
//				select_province.setSelection(position);
//				requestPsnOnLineLoanCityQry(pCode);
//			}else{
//				 BaseDroidApp.getInstanse().showInfoMessageDialog(
//						 getResources().getString(R.string.loan_apply_location_fail));
//			}

		}
		
		if (bs == true) {
			BaseDroidApp.getInstanse()
					.showInfoMessageDialog(
							getResources().getString(
									R.string.loan_apply_location_fail));
		}
		closeBDClient();
	}

	/** 贷款申请--请求城市信息 列表 PsnOnLineLoanCityQry */
	public void requestPsnOnLineLoanCityQry(String provinceCode) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_PSNONLINELOANCITY_QRY);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Loan.LOAN_APPLY_PROVINCECODE_QRY, provinceCode);
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnOnLineLoanCityQryCallBack");
	}

	/**
	 * 贷款申请------ 请求城市信息 回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnOnLineLoanCityQryCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();

		List<Map<String, Object>> resultList = (List<Map<String, Object>>) resultMap
				.get("list");
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseHttpEngine.dissMissProgressDialog();
			 BaseDroidApp.getInstanse().showInfoMessageDialog(
					 getResources().getString(R.string.loan_apply_LineLoanProduct_error));
			return;
		}
		if (cityList.size() >= 1 || cityCodeList.size() >= 1) {
			cityList.clear();
			cityList.add("请选择");
			cityCodeList.clear();
		}
		// 如果返回的结果就一个，则直接显示该城市,并发送接口直接查询该城市能办理的业务
		if (resultList.size() == 1) {
			cityList.add((String) resultList.get(0).get(
					Loan.LOAN_APPLY_CITYNAME_QRY));
			String mCityCode = (String) resultList.get(0).get(
					Loan.LOAN_APPLY_CITYCODE_QRY);
			cityCodeList.add(mCityCode);
			String mCity = (String) resultList.get(0).get(
					Loan.LOAN_APPLY_CITYNAME_QRY);
			text_spinner_select_city.setText(mCity);
			initProvinceSpinner(select_city, cityList, 1);
//			requestPsnOnLineLoanProductQry(mCityCode);
		} else {
			for (int i = 0; i < resultList.size(); i++) {
				cityList.add((String) resultList.get(i).get(
						Loan.LOAN_APPLY_CITYNAME_QRY));
				cityCodeList.add((String) resultList.get(i).get(
						Loan.LOAN_APPLY_CITYCODE_QRY));

			}
			//判断城市是否为空
			String cCity = ""; 
			if (!StringUtil.isNull(locaCity)) {
				for (int i = 0; i < cityList.size(); i++) {
					if (cityList.get(i).contains(locaCity)) {
						cCity = cityList.get(i);
						cityWin=true;
						text_spinner_select_city.setText(cCity);
					}
				}
				int position = cityList.indexOf(cCity);
				initProvinceSpinner(select_city, cityList, position);
				select_city.setSelection(position);
//				requestPsnOnLineLoanProductQry(cCode);

			} else {
				initProvinceSpinner(select_city, cityList, 0);
			}
		}

	}

	/** 贷款申请--请求城市能办理贷款品种信息 列表 */
	public void requestPsnOnLineLoanProductQry(String cityCode) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_APPLY_PSNONLINELOANPRODUCT_QRY);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Loan.LOAN_APPLY_CITYCODE_QRY, cityCode);
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnOnLineLoanProductQryCallBack");
	}

	/**
	 * 贷款申请--请求城市能办理贷款品种信息回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnOnLineLoanProductQryCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();

		List<Map<String, Object>> resultList = (List<Map<String, Object>>) resultMap
				.get("list");
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseHttpEngine.dissMissProgressDialog();
			typeList.clear();
			typeList.add("请选择");
			typeCodeList.clear();
			initProvinceSpinner(spinner_type, typeList, 0);
			spinner_type.setClickable(false);
			BaseDroidApp.getInstanse()
			.showInfoMessageDialog(
					getResources().getString(
							R.string.loan_apply_LineLoanProduct_error));
			return;
		}
		if (typeList.size() > 1 || typeCodeList.size() > 1) {
			typeList.clear();
			typeList.add("请选择");
			typeCodeList.clear();
		}
		text_spinner_loantype.setClickable(false);
		if(resultList.size()==1){
			typeList.add((String) resultList.get(0).get(
					Loan.LOAN_APPLYPRODUCTNAME_QRY));
			typeCodeList.add((String) resultList.get(0).get(
					Loan.LOAN_APPLY_PRODUCTCODE_QRY));
			initProvinceSpinner(spinner_type, typeList, 1);
			
		}else{
		for (int i = 0; i < resultList.size(); i++) {
			typeList.add((String) resultList.get(i).get(
					Loan.LOAN_APPLYPRODUCTNAME_QRY));
			typeCodeList.add((String) resultList.get(i).get(
					Loan.LOAN_APPLY_PRODUCTCODE_QRY));

		}
		initProvinceSpinner(spinner_type, typeList, 0);
		}
	}


	/**
	 * 查询贷款产品申请栏位 4.4 004 PsnOnLineLoanFieldQry查询贷款产品申请栏位
	 * */
	public void requestPsnOnLineLoanFieldQry(String productCode) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_PSNONLINEFIELD_QRY);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Loan.LOAN_APPLY_PRODUCTCODE_QRY, productCode);
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnOnLineLoanFieldQryCallBack");
	}

	/**
	 * 4.4 004 PsnOnLineLoanFieldQry查询贷款产品申请栏位 贷款申请------ 请求城市信息 回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnOnLineLoanFieldQryCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();

		// List<Map<String, Object>> resultList = (List<Map<String,
		// Object>>)resultMap.get("list");
		if (StringUtil.isNullOrEmpty(resultMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}

		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.LOAN_RESULT, resultMap);
		gotoActivity();
	}

	/** 设置下拉列表 */
	private void initProvinceSpinner(Spinner select, List<String> provinceList,
			int pos) {
		ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(this,
				R.layout.dept_spinner, provinceList);

		provinceAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		select.setAdapter(provinceAdapter);
		select.setSelection(pos);
		// select_province = 0;
	}

	private OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> adapter, View view,
				int position, long id) {

			switch (adapter.getId()) {
			/** 省份 */
			case R.id.loan_apply_select_province:
				// positions=position;
				provincePositions = position;
				if (provincePositions > 0) {
					//每次都清空城市
					cityList.clear();
					cityList.add("请选择");
					String provinceCode = provinceCodeList
							.get(provincePositions - 1);
					text_spinner_select_province.setText(provinceList
							.get(provincePositions));
//					if (locationSU == false) {
						requestPsnOnLineLoanCityQry(provinceCode);
//					}
					select_city.setClickable(true);
					spinner_type.setClickable(true);
				}
				if (provincePositions == 0) {
					text_spinner_select_province.setText(provinceList
							.get(provincePositions));
					cityList.clear();
					cityList.add("请选择");
					select_city.setClickable(false);
					spinner_type.setClickable(false);
					text_spinner_select_city.setText(cityList
							.get(provincePositions));
					typeList.clear();
					typeList.add("请选择");
					text_spinner_loantype.setText(typeList
							.get(provincePositions));
				}
				break;
			/** 城市 */
			case R.id.loan_apply_select_city:
				// positions=position;
				
				cityPositions = position;
				if (cityPositions > 0) {
					cityCode = cityCodeList.get(cityPositions - 1);
					city = cityList.get(cityPositions);
					text_spinner_select_city.setText(city);
//					if (locationSU == false) {
						requestPsnOnLineLoanProductQry(cityCode);
//					}
					spinner_type.setClickable(true);
				}
				if (cityPositions == 0) {
					text_spinner_select_city.setText(cityList
							.get(cityPositions));
					typeList.clear();
					typeList.add("请选择");
					spinner_type.setClickable(false);
					text_spinner_loantype.setText(typeList.get(cityPositions));
				}
				break;
			/** 贷款品种 */
			case R.id.loan_apply_spinner_type:
				// positions=position;
				typePositions = position;
				if (typePositions > 0) {
					typeCode = typeCodeList.get(typePositions - 1);
					type = typeList.get(typePositions);
					text_spinner_loantype.setText(typeList.get(typePositions));
					// 4.4 004 PsnOnLineLoanFieldQry查询贷款产品申请栏位

				}
				if (typePositions == 0) {
					text_spinner_loantype.setText(typeList.get(typePositions));
				}

				break;
			default:
				break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> adapter) {
			// if (tag == 2) {
			// accSpinnerText.setVisibility(View.VISIBLE);
			// tv_loan_new_pay_acc.setVisibility(View.GONE);
			// }
		}
	};

	/** 通讯失败 **/
	public OnClickListener errorClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			BaseDroidApp.getInstanse().dismissErrorDialog();
			finish();
		}
	};

	private void gotoActivity() {

		Intent intent = new Intent(this,
				LoanApplyInfoClientMessageActivity.class);
		intent.putExtra(ConstantGloble.APPLY_CITY, city);
		intent.putExtra(ConstantGloble.APPLY_TYPE, type);
		intent.putExtra(ConstantGloble.APPLY_CITYCODE, cityCode);
		intent.putExtra(ConstantGloble.APPLY_TYPECODE, typeCode);
		startActivity(intent);
	}
}
