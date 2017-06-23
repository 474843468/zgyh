package com.chinamworld.bocmbci.biz.branchorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Blpt;
import com.chinamworld.bocmbci.bii.constant.Order;
import com.chinamworld.bocmbci.biz.blpt.bdlocation.BDLocationCenter;
import com.chinamworld.bocmbci.biz.branchorder.adapter.OrderAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.Utils;
import com.chinamworld.bocmbci.widget.adapter.SpinnerAdapter;

/**
 * 网点查询主界面
 * 
 * @author fsm
 * 
 */
public class BranchOrderMainActivity extends BranchOrderBaseActivity implements
		OnItemSelectedListener {
	private String TAG = BranchOrderMainActivity.class.getName();
	public static BranchOrderMainActivity instance;
	private BDLocationCenter bdLocationCenter;
	/** 定位Handler **/
	private MyHandler mHandler;
	/** 定位线程 */
	private LoctionThread loction;
	public static final int OPEN_GPS = 0;
	private boolean locationSU;// 定位成功与否

	private String provinceIdStr, cityIdStr, areaIdStr;
	private String provinceStr, cityStr, areaStr;// 定位信息省、市、区域
	private Map<String, Object> privinceMap, cityMap, areaMap;
	private List<String> privinceList, cityList, areaList;
	private List<Map<String, Object>> orderList;
	private OrderAdapter mAdapter;
	/**
	 * 当前定位所在区域
	 */
	private TextView your_location;
	/** 省、市、区 */
	private Spinner province, city, area;
	private EditText edSearchArea;// 查询关键字
	private Button btnquery;// 查询按钮
	private ListView listview;// 结果列表
	private LinearLayout order_list_titile;//
	private int privinceFlag = 100;
	int cityFlag = 101;
	int areaFlag = 102;
	private SpinnerAdapter provinceAdapter, cityAdapter, areaAdapter;
	private int clickCount;// 省份选择点击次数
	private boolean first = true;
	
	/**程序注册的包名*/
    private static final String AppPath_ditu = "com.mapabc.bc";
    /**主页面的Activity的全路径*/
    private static final String ActAllPath_ditu = "com.mapabc.bc.activity.BCMapabcActivity";
    /**主页面的Activity 的名称*/
    private static final String ActName_ditu = "BCMapabcActivity";
    /**apkName*/
    private static final String ApkName_ditu ="BCMapabcMobile.mp3";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BranchOrderDataCenter.getInstance().clearAllData();
		inflateLayout(R.layout.order_main_2);
		setTitle(R.string.order_main_title);
		setupView();
		instance = this;
		locationCtty();
		setOnClickListener();
	}

	private void setupView() {
		mRightButton.setVisibility(View.VISIBLE);
		setRightText(getString(R.string.order_my_around));
		setRightBtnClick(this);
		setPadding(0, 0, 0, 0);
		your_location = (TextView) findViewById(R.id.your_location);
		province = (Spinner) findViewById(R.id.province);
		city = (Spinner) findViewById(R.id.city);
		area = (Spinner) findViewById(R.id.area);
		provinceAdapter = new SpinnerAdapter(this, new ArrayList<String>() {{add("");}}, "省/直辖市");
		cityAdapter = new SpinnerAdapter(this, new ArrayList<String>() {{add("");}}, "市");
		areaAdapter = new SpinnerAdapter(this, new ArrayList<String>() {{add("");}}, "区/县");
		province.setAdapter(provinceAdapter);
		city.setAdapter(cityAdapter);
		area.setAdapter(areaAdapter);

		edSearchArea = (EditText) findViewById(R.id.search_area);
		btnquery = (Button) findViewById(R.id.btnquery);
		listview = (ListView) findViewById(R.id.listview);
		initListener();
		order_list_titile = (LinearLayout) findViewById(R.id.order_list_titile);
		btnquery.setOnClickListener(this);
		EditTextUtils.setLengthMatcher(this, edSearchArea, 60);
	}

	private void initListener() {
		province.setOnTouchListener(this);
		city.setOnTouchListener(this);
		area.setOnTouchListener(this);
		province.setOnItemSelectedListener(this);
		city.setOnItemSelectedListener(this);
		area.setOnItemSelectedListener(this);
		city.setEnabled(false);
		area.setEnabled(false);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (!StringUtil.isNullOrEmpty(orderList)) {
					BranchOrderDataCenter.getInstance().setOrderListItem(
							orderList.get(arg2));
					Intent intent = new Intent(BranchOrderMainActivity.this,
							BranchOrderCustomerActivity.class);
//					intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
					BranchOrderDataCenter.isFromMain = true;
					startActivityForResult(intent, BranchOrderDataCenter.UN_VIP);
				}
			}
		});
	}

	/*** 定位城市 */
	private void locationCtty() {
		mHandler = new MyHandler();
		loction = new LoctionThread();
		loction.run();
		String message = BranchOrderMainActivity.this.getResources()
				.getString(R.string.blpt_location_ing);
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
			BaseHttpEngine.showProgressDialog();
			requestProvince(false);
		}
	};

	public class MyHandler extends Handler {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle b = msg.getData();
			getLocaInfo(b);
		}
	}

	private void getLocaInfo(Bundle b) {
		if ((b.getString(Blpt.KEY_CITY)).contains("市")) {
			cityStr = b.getString(Blpt.KEY_CITY).substring(0,
					b.getString(Blpt.KEY_CITY).length() - 1);
		} else {
			cityStr = b.getString(Blpt.KEY_CITY);
		}
		provinceStr = b.getString(Blpt.KEY_PROVICENAME);
		areaStr = b.getString(Blpt.KEY_AREA);
		BaseDroidApp.getInstanse().dismissErrorDialog();
		if (StringUtil.isNull(cityStr)) {
			locationSU = false;
		} else {
			locationSU = true;
			((LinearLayout) findViewById(R.id.locationLl))
					.setVisibility(View.VISIBLE);
			if("null".equals(areaStr)|| areaStr == null){
				your_location.setText(provinceStr + " " + cityStr + " " + "");
			}else{
				your_location.setText(provinceStr + " " + cityStr + " " + areaStr);
			}
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this, your_location);
		}
		BaseHttpEngine.showProgressDialog();
		requestProvince(locationSU);
		closeBDClient();
		LogGloble.d(TAG, provinceStr + cityStr + areaStr);
	}
	
	/*** 定位线程 */
	class LoctionThread extends Thread {
		@Override
		public void run() {
			super.run();
			if (bdLocationCenter == null) {
				bdLocationCenter = new BDLocationCenter(mHandler, instance);
			}
			bdLocationCenter.getLocationInfo();
		}
	}

	/**
	 * 请求省份
	 */
	private void requestProvince(boolean is) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Order.METHOD_PROVICE);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "provinceCallBack");
	}

	@SuppressWarnings("unchecked")
	public void provinceCallBack(Object resultObj) {
		List<Map<String, Object>> list = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(list)) return;
			privinceMap = BranchOrderUtils.getMapFromMapList(list,
				Order.PROVINCECODE, Order.PROVINCENAME);
		if (StringUtil.isNullOrEmpty(privinceMap))
			return ;
		privinceList = BranchOrderUtils.getMapValueList(privinceMap, false);
		if (first) {
			if (locationSU) {
				int index = BranchOrderUtils.getIndex(privinceList, provinceStr, true);
				provinceStr = (String) privinceList.get(index);
				provinceAdapter.setData(privinceList, true, "");
				if(index >= 0)
					province.setSelection(index);
				else{
					clearAddress();
					return;
				}
			}else{
				provinceStr = (String) privinceList.get(0);
			}
			provinceIdStr = BranchOrderUtils.getKeyByValue(privinceMap,
					provinceStr);
			requestCity(provinceIdStr); return;
		}
		 BaseHttpEngine.dissMissProgressDialog();
	}

	/**
	 * 请求网点
	 */
	private void requestBranchOrderList() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Order.METHOD_BRANCHORDER_LIST);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Order.PROVINCECODE, provinceIdStr);
		params.put(Order.CITYCODE, cityIdStr);
		params.put(Order.AREACODE, areaIdStr);
		params.put(Order.ORDERORGNAME, edSearchArea.getText().toString().trim());
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "branchOrderListCallBack");
	}

	@SuppressWarnings("unchecked")
	public void branchOrderListCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		orderList = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(orderList)) {
			return;
		}
		order_list_titile.setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.tip)).setVisibility(View.GONE);
		mAdapter = new OrderAdapter(this, orderList);
		listview.setAdapter(mAdapter);
	}
	
	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
			if (Order.METHOD_BRANCHORDER_LIST.equals(biiResponseBody.getMethod())) {
				if (biiResponse.isBiiexception()) {// 代表返回数据异常
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null) {
						if (biiError.getCode() != null) {
							if (LocalData.timeOutCode.contains(biiError
									.getCode())) {// 表示回话超时 要重新登录
								showTimeOutDialog(biiError.getMessage());
							} else {// 非会话超时错误拦截
								BaseDroidApp.getInstanse().createDialog("",
										biiError.getMessage(),
										new OnClickListener() {

											@Override
											public void onClick(View v) {
												BaseDroidApp.getInstanse()
														.dismissErrorDialog();
											}
										});
								if(mAdapter != null)
									mAdapter.clearDatas();
							}
						}
					}
					return true;
				}
				return false;// 没有异常
			} else {
				return super.httpRequestCallBackPre(resultObj);
			}
		}
		// 随机数获取异常
		return super.httpRequestCallBackPre(resultObj);

	}
	
//	public void showTimeOutDialog(BiiError biiError) {
//		BaseDroidApp.getInstanse().showMessageDialog(biiError.getMessage(),
//				new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						BaseDroidApp.getInstanse().dismissErrorDialog();
//						ActivityTaskManager.getInstance().removeAllActivity();
//						Intent intent = new Intent();
//						intent.setClass(BranchOrderMainActivity.this,
//								LoginActivity.class);
//						startActivityForResult(intent,
//								ConstantGloble.ACTIVITY_RESULT_CODE);
//					}
//				});
//	}

	/**
	 * 请求城市
	 * 
	 * @param proviceCode
	 */
	private void requestCity(String proviceCode) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Order.METHOD_CITY);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Order.PROVINCECODE, proviceCode);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "cityCallBack");
	}

	@SuppressWarnings("unchecked")
	public void cityCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		List<Map<String, Object>> list = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(list))
			return;
		cityMap = BranchOrderUtils.getMapFromMapList(list, Order.CITYCODE,
				Order.CITYNAME);
		if (!StringUtil.isNullOrEmpty(cityMap))
			cityList = BranchOrderUtils.getMapValueList(cityMap, false);
		if (locationSU && first) {
			int index = BranchOrderUtils.getIndex(cityList, cityStr, true);
			cityStr = (String) cityList.get(index);
			cityIdStr = BranchOrderUtils.getKeyByValue(cityMap, cityStr);
			cityAdapter.setData(cityList, true, "");
			if(index >= 0){
				city.setSelection(index);
				requestArea(provinceIdStr, cityIdStr);
			}else{
				clearAddress();
				return;
			}
		}
	}

	/**
	 * 请求区
	 * 
	 * @param proviceCode
	 * @param cityCode
	 */
	private void requestArea(String proviceCode, String cityCode) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Order.METHOD_AREA);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Order.PROVINCECODE, proviceCode);
		params.put(Order.CITYCODE, cityCode);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "areaCallBack");
	}

	@SuppressWarnings("unchecked")
	public void areaCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		List<Map<String, Object>> list = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(list))
			return;
		areaMap = BranchOrderUtils.getMapFromMapList(list, Order.AREACODE,
				Order.AREANAME);
		if (!StringUtil.isNullOrEmpty(areaMap))
			areaList = BranchOrderUtils.getMapValueList(areaMap, false);
			areaList = BranchOrderUtils.sortByPinYin(areaList);
		if (locationSU && first) {
			first = false;
			int index = BranchOrderUtils.getIndex(areaList, areaStr, true);
			areaStr = (String) areaList.get(index);
			areaIdStr = BranchOrderUtils.getKeyByValue(areaMap, areaStr);
			areaAdapter.setData(areaList, true, "");
			if(index >= 0){
				area.setSelection(index);
				requestBranchOrderList();
			}else{
				clearAddress();
				return;
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1001:
			break;
		default:
			switch (resultCode) {
			case RESULT_OK:
				BranchOrderDataCenter.getInstance().clearAllData();
				finish();
				break;
			default:
				break;
			}
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		super.onTouch(v, event);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			switch (v.getId()) {
			case R.id.province:
				if(!StringUtil.isNullOrEmpty(privinceList)){
					provinceAdapter.setData(privinceList, true,"");
				}else if(StringUtil.isNullOrEmpty(privinceList) || 
						StringUtil.isNull(privinceList.get(0))){
					requestProvince(locationSU);
				}
				city.setEnabled(true);
				break;
			case R.id.city:
				if(!StringUtil.isNullOrEmpty(cityList)){
					cityAdapter.setData(cityList, true,"");
					city.setAdapter(cityAdapter);
				}else if(StringUtil.isNullOrEmpty(cityList) || 
						StringUtil.isNull(cityList.get(0))){
					requestCity(provinceIdStr);
				}
				area.setEnabled(true);
				break;
			case R.id.area:
				if(!StringUtil.isNullOrEmpty(areaList)){
					areaAdapter.setData(areaList, true,"");
					area.setAdapter(areaAdapter);
				}
				break;
			default:
				break;
			}
		}
		return false;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		switch (arg0.getId()) {
		case R.id.province:
			if (provinceAdapter == null || privinceList == null)
				return;
			provinceStr = (String) privinceList.get(arg2);
			provinceIdStr = BranchOrderUtils.getKeyByValue(privinceMap,
					provinceStr);
			BaseHttpEngine.showProgressDialog();
			area.setEnabled(false);
			cityAdapter.setData(new ArrayList<String>() {{add("");}}, false, "市");
			areaAdapter.setData(new ArrayList<String>() {{add("");}}, false, "区/县");
			requestCity(provinceIdStr);
			break;
		case R.id.city:
			if (cityAdapter == null || cityList == null)
				return;
			cityStr = (String) cityList.get(arg2);
			cityIdStr = BranchOrderUtils.getKeyByValue(cityMap, cityStr);
			BaseHttpEngine.showProgressDialog();
			area.setEnabled(true);
			areaAdapter.setData(new ArrayList<String>() {{add("");}}, false, "区/县");
			requestArea(provinceIdStr, cityIdStr);
			break;
		case R.id.area:
			if (areaAdapter == null || areaList == null)
				return;
			areaStr = (String) areaList.get(arg2);
			areaIdStr = BranchOrderUtils.getKeyByValue(areaMap, areaStr);
			if (locationSU)
				requestBranchOrderList();
			break;
		default:
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btnquery:
			if (StringUtil.isNull(provinceIdStr)
					|| !provinceAdapter.isSelected()) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("请选择省/直辖市");
				return;
			} else if (StringUtil.isNull(cityIdStr)
					|| !cityAdapter.isSelected()) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("请选择市");
				return;
			} else if (StringUtil.isNull(areaIdStr)
					|| !areaAdapter.isSelected()) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("请选择区/县");
				return;
			}
			requestBranchOrderList();
			break;
		default:
			break;
		}
	}
	/**2014-10-28
	 * 从新设定右键(我的周边的点击事件)
	 */
	private void setOnClickListener(){
		mRightButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Utils.aboutMapQuery(BranchOrderMainActivity.this, AppPath_ditu, ActName_ditu,
						ApkName_ditu, ActAllPath_ditu);
			}
		});
	}
	
	private void clearAddress(){
		if(provinceAdapter != null && cityAdapter != null && areaAdapter != null){
			provinceAdapter.setData(new ArrayList<String>() {{add("");}}, false, "省/直辖市");
			cityAdapter.setData(new ArrayList<String>() {{add("");}}, false, "市");
			areaAdapter.setData(new ArrayList<String>() {{add("");}}, false, "区/县");
			provinceIdStr = ""; cityIdStr = ""; areaIdStr = "";
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		closeBDClient();
	}

	/*** 关闭百度定位服务 */
	private void closeBDClient() {
		if (bdLocationCenter == null)
			return;
		if (bdLocationCenter.mLocationClient != null) {
			bdLocationCenter.mLocationClient.unRegisterLocationListener(null);
			bdLocationCenter.mLocationClient.stop();
		}
	}

}
