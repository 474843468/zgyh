package com.chinamworld.bocmbci.biz.plps;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IHttpCallBack;
import com.chinamworld.bocmbci.biz.plps.annuity.AnnuityAcctInfoListActivity;
import com.chinamworld.bocmbci.biz.plps.annuity.AnnuityListActivity;
import com.chinamworld.bocmbci.biz.plps.annuity.AnnuityPlanListQueryActivity;
import com.chinamworld.bocmbci.biz.plps.interprovincial.interprolongdis.InterproDecisNumbActivity;
import com.chinamworld.bocmbci.biz.plps.interprovincial.payquery.InterprovincPayqueryActivity;
import com.chinamworld.bocmbci.biz.plps.order.OrderAdressCityActivity;
import com.chinamworld.bocmbci.biz.plps.payment.PaymentSignListActivity;
import com.chinamworld.bocmbci.biz.plps.payment.PaymentSignMainActivity;
import com.chinamworld.bocmbci.biz.plps.prepaid.banquery.PrepaidCardQueryActivity;
import com.chinamworld.bocmbci.biz.plps.prepaid.recharge.PrepaidCardRechActivity;
import com.chinamworld.bocmbci.biz.plps.prepaid.resquery.PrepaidCardResultQueryActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 民生服务UI基类
 * 
 * @author panwe
 * 
 */
public class PlpsBaseActivity extends BaseActivity implements OnClickListener ,OnItemClickListener{
	private final int INDEX_LIVE = 0;
	private final int INDEX_ANNUITY = 1;
	private final int INDEX_PAYMENT = 2;
	private final int ZERO = 0;
	private final int ONE = 1;
	private final int TWO = 2;
	private final int THREE = 3;
	private LinearLayout mBodyLayout;
	private LinearLayout peoplerLayout;
//	public Button mRightButton;
	public Button mLeftButton;
	public String assetTotal;
	public String phoneNumber;
	public String recordNumber;
	//省市
	public static TextView cityAdress;
	//左侧菜单栏按钮
	public Button btnhide;
	public static Button mRightButton;
	public static Button mFinishButton;
	private Intent mIntent;
	// 城市名称
	private String city= null;
	//省的简称
	private String prvcShortName= null;
	//省名称
	private String prvcDispName=null;
	//城市显示名称
	private	String cityDispName= null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		initLeftSideList(this, LocalData.plpsLeftListData);
		setLeftButtonPopupGone();
		initPulldownBtn();
		initFootMenu();
		initView();
//		requestPlpsQueryDefaultArea();
	}
	
	public View addView(int resource) {
//		peoplerLayout.setVisibility(View.VISIBLE);
//		mBodyLayout.setVisibility(View.GONE);
		View view = LayoutInflater.from(this).inflate(resource, null);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);
		mBodyLayout.addView(view,layoutParams);
//		peoplerLayout.addView(view,layoutParams);
		return view;
	}
	/**
	 * 查询默认地区
	 */
	public void requestPlpsQueryDefaultArea(){
		BaseHttpEngine.showProgressDialogCanGoBack();
		// 查询默认地区前，先将保存的默认地区数据清空
		PlpsDataCenter.getInstance().setPrvcDispName(null);
		PlpsDataCenter.getInstance().setPrvcShortName(null);
		PlpsDataCenter.getInstance().setCityDispName(null);
		
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.QUERYDEFAULTAREA);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "requestPlpsQueryDefaultAreaCallBack");
	}
	
	@SuppressWarnings("unchecked")
	public void requestPlpsQueryDefaultAreaCallBack(Object resultObj){
//		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> defaultAreaList = HttpTools.getResponseResult(resultObj);
		if(StringUtil.isNullOrEmpty(defaultAreaList)){
			showMessageDialog();
			return;
		}			
		//省简称
		prvcShortName = (String)defaultAreaList.get(Plps.PRVCSHORTNAME);
		if(!StringUtil.isNullOrEmpty(prvcShortName)){
			PlpsDataCenter.getInstance().setPrvcShortName(prvcShortName);
		}
		//城市显示名称
		cityDispName = (String) defaultAreaList.get(Plps.CITYDISPNAME);
		if(!StringUtil.isNullOrEmpty(cityDispName)){
			PlpsDataCenter.getInstance().setCityDispName(cityDispName);
		}
		//省显示名称
		prvcDispName = (String)defaultAreaList.get(Plps.PRVCDISPNAME);
		if(!StringUtil.isNullOrEmpty(prvcDispName)){
			PlpsDataCenter.getInstance().setPrvcDispName(prvcDispName);
		}
//		if(StringUtil.isNullOrEmpty(prvcDispName)&&StringUtil.isNullOrEmpty(cityDispName)){
//			BaseDroidApp.getInstanse().showMessageDialog(getString(R.string.plps_query_default_area), new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					BaseDroidApp.getInstanse().dismissMessageDialog();
//				}
//			});
//		}
		// 城市多语言代码
		String cityDispNo = (String) defaultAreaList.get(Plps.CITYDISPNO);
		if (!StringUtil.isNullOrEmpty(cityDispNo)) {
			PlpsDataCenter.getInstance().setDisplayNo(cityDispNo);
		}
//		if(StringUtil.isNullOrEmpty(cityDispName)){
//			requestPlpsGetCityListByPrvcShortName(prvcShortName);
//		}else {
//			cityAdress.setText(prvcDispName + cityDispName);
//		}
		if (StringUtil.isNull(prvcDispName) || StringUtil.isNull(cityDispName)) {
			showMessageDialog();
//			cityAdress.setText(Plps.DEFAULT_AREA);
//			BaseDroidApp.getInstanse().showMessageDialog(getResources().getString(R.string.plps_choose_area_error),
//					new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							BaseDroidApp.getInstanse()
//									.dismissMessageDialog();
//						}
//					});
		} else {
			String prvcDispNameReplace = prvcDispName.trim();
			if(PlpsDataCenter.municiplGovernment.contains(prvcDispNameReplace)){
				cityAdress.setText(prvcDispName);
			}else {
				cityAdress.setText(prvcDispName + cityDispName);
			}
		}
//		requestPsnPlpsQueryAllPaymentList();
	}
	
	/**
	 * 没有查询到默认地址时，弹出提示信息 
	 * */
	private void showMessageDialog(){
		cityAdress.setText(Plps.DEFAULT_AREA);
		BaseDroidApp.getInstanse().showMessageDialog(getResources().getString(R.string.plps_choose_area_error),
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						BaseDroidApp.getInstanse()
								.dismissMessageDialog();
					}
				});
	}
	
	/**查询某地区所有缴费项目*/
	public void requestPsnPlpsQueryAllPaymentList() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.PSNPLPSALLPAYMENT);
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtil.isNullOrEmpty(PlpsDataCenter.getInstance()
				.getPrvcShortName())) {
			return;
		}
		params.put(Plps.PRVCSHORTNAME, PlpsDataCenter.getInstance()
				.getPrvcShortName());
//		if (StringUtil.isNullOrEmpty(PlpsDataCenter.getInstance()
//				.getCityDispName())) {
//			return;
//		}
//		params.put(Plps.CITY, PlpsDataCenter.getInstance().getCityDispName());
		params.put(Plps.CITYDISPNO, PlpsDataCenter.getInstance().getDisplayNo());
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnPlpsQueryAllPaymentListCallBack");
	}
	@SuppressWarnings("unchecked")
	public void requestPsnPlpsQueryAllPaymentListCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		List<Map<String, Object>> allPaymentList = (List<Map<String,Object>>)result.get(Plps.ALLPAYMENTLIST);
		PlpsDataCenter.getInstance().setAllPaymentList(allPaymentList);
	}
	
	/**
	 * 查询某省已开通民生缴费项目的城市列表*/
//	private void requestPlpsGetCityListByPrvcShortName(String prvcShortName){
//		BaseHttpEngine.showProgressDialog();
//		BiiRequestBody biiRequestBodey = new BiiRequestBody();
//		biiRequestBodey.setMethod(Plps.GETCITYLISTBYPRVC);
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put(Plps.PRVCSHORTNAME, prvcShortName);
//		biiRequestBodey.setParams(map);
//		HttpManager.requestBii(biiRequestBodey, this, "requestPlpsGetCityListByPrvcShortNameCallBack");
//	}

//	public void requestPlpsGetCityListByPrvcShortNameCallBack(Object resultObj) {
//		BaseHttpEngine.dissMissProgressDialog();
//		Map<String, Object> cityResult = (Map<String, Object>) PlpsUtils
//				.httpResponseDeal(resultObj);
//		if (StringUtil.isNullOrEmpty(cityResult))
//			return;
//		List<Map<String, Object>> cityList = (List<Map<String, Object>>) cityResult
//				.get(Plps.CITYLISTM);
//		for (int i = 0; i < cityList.size(); i++) {
//			// 城市显示名称
//			cityDispName = (String) cityList.get(i).get(
//					Plps.PRVCDISPNAME);
//			
//			city = (String) cityList.get(i).get(Plps.CITY);
//		}
//		requestPlpsSetDefaltArea(city);
//	}
	/**
	 *设置默认地区 */
	public void requestPlpsSetDefaltArea(String token, String provShortName, String city,String displayName){
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.PLPSSETDEFAUTLAREA);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put(Plps.PRVCSHORTNAME, provShortName);
		parms.put(Plps.TOKEN, token);
		parms.put(Plps.CITYDISPNO, displayName);
//		parms.put(Plps.CITYDISPNO, city);
		biiRequestBody.setParams(parms);
		HttpManager.requestBii(biiRequestBody, this, "requestPlpsSetDefaltAreaCallBack");
	}
	public void requestPlpsSetDefaltAreaCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
//		cityAdress.setText(prvcDispName + cityDispName);
	}
	

	/**
	 * 初始化控件
	 */
	private void initView() {
//		setContentView(R.layout.biz_activity_layout);
		peoplerLayout =(LinearLayout) findViewById(R.id.sliding_bodytwo);
		mBodyLayout = (LinearLayout) findViewById(R.id.sliding_body);
		mRightButton = (Button) findViewById(R.id.ib_top_right_btn);
		mFinishButton = (Button)findViewById(R.id.finish);
		cityAdress = (TextView) findViewById(R.id.cityAdress);
		mLeftButton = (Button) findViewById(R.id.ib_back);
		btnhide = (Button) findViewById(R.id.btn_show);
		cityAdress.setOnClickListener(this);
		 mRightButton.setOnClickListener(this);
		mLeftButton.setOnClickListener(this);
	}

	/**
	 * 初始化主布局
	 * @param resource
	 */
	public void inflateLayout(int resource) {
		peoplerLayout.setVisibility(View.GONE);
		mBodyLayout.setVisibility(View.VISIBLE);
		View v = View.inflate(this, resource, null);
		mBodyLayout.addView(v);
	}
	
	/**
	 * 初始化布局
	 * @param resource
	 * @return
	 */
	public View inflateView(int resource){
		return View.inflate(this, resource, null);
	}

	/**
	 * 隐藏左侧菜单
	 */
	public void setLeftBtnGone() {
		findViewById(R.id.btn_show).setVisibility(View.GONE);
	}

	/**
	 * 左侧菜单
	 */
//	@Override
//	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
//		super.selectedMenuItemHandler(context, menuItem);
//		ActivityTaskManager.getInstance().removeAllActivity();
//		mIntent = new Intent();
//		String menuId = menuItem.MenuID;
//		if(menuId.equals("plps_1")){
//			mIntent.setClass(this, PaymentAllProject.class);
//		}
//		else if(menuId.equals("plps_2")){
//			mIntent.setClass(this, CommServiceActivity.class);	
//		}
//		startActivity(mIntent);
//		
//		return true;
//		
////		ActivityTaskManager.getInstance().removeAllActivity();
////		mIntent = new Intent();
////		switch (clickIndex) {
////		case INDEX_LIVE:
//////			mIntent.setClass(this, LiveServiceMainActivity.class);
////			mIntent.setClass(this, PaymentAllProject.class);
////			break;
////
////		case INDEX_ANNUITY:
////			mIntent.setClass(this, CommServiceActivity.class);	
//////			mIntent.setClass(this, LiveServiceMainActivity.class);
////			break;
////		}
////		startActivity(mIntent);
//	}
	
	/**
	 * 养老金/签约代缴服务 三级菜单跳转
	 * @param position
	 * @param flag
	 * @param parm
	 */
	public void annuityIntentAction(int position,PlpsMenu flag,String... parm){
		Intent intent = new Intent();
		switch (position) {
		case ZERO:
			if (flag == PlpsMenu.ANNUITY) {
				intent.setClass(this, AnnuityAcctInfoListActivity.class)
				.putExtra(Plps.ASSETTOTAL, assetTotal);break;
			}
			if (flag == PlpsMenu.PAYMENT) {
				intent.setClass(this, PaymentSignListActivity.class)
				.putExtra(Plps.RECORDNUMBER, recordNumber)
				.putExtra(Plps.PHONENUMBER, phoneNumber);break;
			}

			if(flag == PlpsMenu.PREPAID){
				intent.setClass(this, PrepaidCardQueryActivity.class)
				.putExtra("p", position);break;
			}
			if(flag == PlpsMenu.INTERPROV){
				intent.setClass(this, InterproDecisNumbActivity.class);
				break;
			}

			break;
			
		case ONE:
			if (flag == PlpsMenu.ANNUITY) {
				intent.setClass(this, AnnuityListActivity.class); break;
			}
			if (flag == PlpsMenu.PAYMENT) {
				intent.setClass(this, PaymentSignMainActivity.class); break;
			}

			if(flag == PlpsMenu.PREPAID){
				intent.setClass(this, PrepaidCardRechActivity.class); break;
			}
			if(flag == PlpsMenu.INTERPROV){
				intent.setClass(this, InterprovincPayqueryActivity.class);break;
			}

			break;
		case TWO:
			if (flag == PlpsMenu.ANNUITY) {
//				intent.setClass(this, AnnuityPlanListActivity.class)
				intent.setClass(this, AnnuityPlanListQueryActivity.class)
				.putExtra("p", position); break;
			}
			if(flag == PlpsMenu.PREPAID){
				intent.setClass(this, PrepaidCardResultQueryActivity.class); break;
			}
			break;
		case THREE:
			if (flag == PlpsMenu.ANNUITY) {
//				intent.setClass(this, AnnuityPlanListActivity.class)
				intent.setClass(this, AnnuityPlanListQueryActivity.class)
				.putExtra("p", position); break;
			}
			break;
		}
		startActivity(intent);
	}
	
	/**
	 * 设置日期
	 */
	public OnClickListener plpsChooseDateClick = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			TextView tv = (TextView) v;
			String time = tv.getText().toString();
			int startYear = Integer.parseInt(time.substring(0, 4));
			int startMonth = Integer.parseInt(time.substring(5, 7));
			int startDay = Integer.parseInt(time.substring(8, 10));
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			DatePickerDialog dialog = new DatePickerDialog(
					PlpsBaseActivity.this, new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							StringBuilder date = new StringBuilder();
							date.append(String.valueOf(year));
							date.append("/");
							int month = monthOfYear + 1;
							date.append(((month < 10) ? ("0" + month)
									: (month + "")));
							date.append("/");
							date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth)
									: (dayOfMonth + "")));
							// 为日期赋值
							((TextView) v).setText(String.valueOf(date));
						}
					}, startYear, startMonth - 1, startDay);
			dialog.show();
		}
	};
	
//	/**
//	 * 查询日期校验
//	 * @param startDate
//	 * @param endDate
//	 * @return
//	 */
	public boolean queryCheck(String startDate,String endDate){
		String dateTime = PlpsDataCenter.getInstance().getSysDate();
		// 起始日期不能早于系统当前日期一年前
		if (!QueryDateUtils.compareDateOneYear(startDate, dateTime)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_check_start_enddate));
			return false;
		}
		// 结束日期在服务器日期之前
		if (!QueryDateUtils.compareDate(endDate, dateTime)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_check_enddate));
			return false;
		}
		// 开始日期在结束日期之前
		if (!QueryDateUtils.compareDate(startDate, endDate)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_query_errordate));
			return false;
		}
		// 起始日期与结束日期最大间隔为三个自然月
		if (!QueryDateUtils.compareDateThree(startDate, endDate)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.acc_check_start_end_date));
			return false;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ib_back) {
			finish();
		}
		else if(v.getId() == R.id.ib_top_right_btn){
			PlpsDataCenter.getInstance().clearAllData();
			ActivityTaskManager.getInstance().removeAllActivity();
			goToMainActivity();
		}
//		else if (v.getId() == R.id.cityAdress) {
//			//查询民生缴费项目的省列表
//			requestPlpsGetProvinceLists();
//			
//
//		}
	}
	private void requestPlpsGetProvinceLists(){
		Intent intent = new Intent(PlpsBaseActivity.this,
				OrderAdressCityActivity.class);
		intent.putExtra(Plps.PRVCDISPNAME, PlpsDataCenter.getInstance().getPrvcDispName());
		intent.putExtra(Plps.CITYDISPNAME, PlpsDataCenter.getInstance().getCityDispName());
//		startActivity(intent);
		startActivityForResult(intent, 1);
	}
	

	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// TODO Auto-generated method stub
		if (requestCode == 1) {
			switch (resultCode) {
			case RESULT_OK:

				String prvcName = data.getStringExtra(Plps.PRVCDISPNAME);
				String cityName = data.getStringExtra(Plps.CITYDISPNAME);
				cityAdress.setText(prvcName + cityName);
				break;
			}
		}
	}*/

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
	}
	
	/**
	 * 养老金计划列表查询
	 */
	public void requestAnnuityPlanList(){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.METHODPLANLIST);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "annuityPlanListCallBack");
	}
	
	@SuppressWarnings("unchecked")
	public void annuityPlanListCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(result)) return;
		PlpsDataCenter.getInstance().setPlanList((List<Map<String, Object>>) result.get(Plps.LIST));
		assetTotal = (String) result.get(Plps.ASSETTOTAL);
	}
	
	/**
	 * 养老金账户信息列表查询
	 * @param currentIndex
	 * @param planNo
	 */
	public void requestAnnuityAcctInfoList(String currentIndex,String planNo){
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.METHODACCTINFOLIST);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Plps.CURRENTINDEX, currentIndex);
		params.put(Plps.PLANNOT, planNo);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "annuityAcctInfoListCallBack");
	}
	
	@SuppressWarnings("unchecked")
	public void annuityAcctInfoListCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(result)) return;
		PlpsDataCenter.getInstance().setAcctInfoList((List<Map<String, Object>>) result.get(Plps.LIST));
		recordNumber = (String) result.get(Plps.RECORDNUMBER);
	}
	
	/**
	 * 养老金查询
	 * @param index
	 * @param planNo
	 * @param startDate
	 * @param endDate
	 * @param queryType
	 */
	public void requestAnnuity(String index,String planNo,String startDate,String endDate,String queryType){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.METHODANNUITY);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Plps.PAGESIZE, ConstantGloble.FOREX_PAGESIZE);
		params.put(Plps.CURRENTINDEX, index);
		params.put(Plps.PLANNOT, planNo);
		params.put(Plps.STARTDATE, startDate);
		params.put(Plps.ENDDATE, endDate);
		params.put(Plps.QUERYTYPE, queryType);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "annuityCallBack");
	}
	
	@SuppressWarnings("unchecked")
	public void annuityCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		recordNumber = (String) result.get(Plps.RECORDNUMBER);
		List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(Plps.LIST);
		PlpsDataCenter.getInstance().setAnnuityList(list);
	}
	
	/**
	 * 客户签约信息查询
	 * @param currentIndex
	 */
	public void requestSignList(String currentIndex){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.METHODSIGNQUERY);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Plps.CURRENTINDEX, currentIndex);
		params.put(Plps.PAGESIZE, ConstantGloble.FOREX_PAGESIZE);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "signListCallBack");
	}
	
	@SuppressWarnings("unchecked")
	public void signListCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(result)) return;
		PlpsDataCenter.getInstance().setSignList((List<Map<String, Object>>) result.get(Plps.SIGNLIST));
		recordNumber = (String) result.get(Plps.RECORDNUMBER);
	}
	
	/**
	 * 请求账户列表
	 */
	public void requestAcctList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_ALL_BANK_ACCOUNT);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Comm.ACCOUNT_TYPE, PlpsDataCenter.accountTypeList);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "bankAccListCallBack");
	}

	@SuppressWarnings("unchecked")
	public void bankAccListCallBack(Object resultObj) {
		List<Map<String, Object>> list = HttpTools.getResponseResult(resultObj);
		PlpsDataCenter.getInstance().setAcctList(null);
		if(StringUtil.isNullOrEmpty(list)){
			 return;
		}else {
			PlpsDataCenter.getInstance().setAcctList(list);
		}

	}
	
	/**
	 * 请求客户手机号信息
	 */
	public void requestCustomerInfo(){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.METHODPAYMENTCUSTOMERINFO);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "customerInfoCallBack");
	}
	
	@SuppressWarnings("unchecked")
	public void customerInfoCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		phoneNumber = (String) result.get(Plps.PHONENUMBER);
//		if (StringUtil.isNull(phoneNumber)) {
//			return;
//		}
	}
	
	/**
	 * 业务类型请求
	 */
//	public void requestServiceType(){
//		BaseHttpEngine.showProgressDialog();
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(Plps.METHODSERVICETYPE);
//		biiRequestBody.setParams(null);
//		HttpManager.requestBii(biiRequestBody, this, "serviceTypeCallBack");
//	}
//	
//	@SuppressWarnings("unchecked")
//	public void serviceTypeCallBack(Object resultObj) {
//		BaseHttpEngine.dissMissProgressDialog();
//		List<Map<String, Object>> result = (List<Map<String, Object>>) PlpsUtils.httpResponseDeal(resultObj);
//		if (StringUtil.isNullOrEmpty(result)) return;
//		PlpsDataCenter.getInstance().setServiceType(result);
//
//	}
	/*
	 * 缴费签约通过省查询城市
	 */
	public void requestProxyPaymentCityQuery(){
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.PLPSPROXYCITYQUERY);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Plps.PRVCSHORTNAME, PlpsDataCenter.getInstance().getPrvcShortName());
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestProxyPaymentCityQueryCallBack");
	}
	@SuppressWarnings("unchecked")
	public void requestProxyPaymentCityQueryCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		if(StringUtil.isNullOrEmpty(map)) return;
		List<Map<String, Object>> cityList = (List<Map<String,Object>>)map.get(Plps.CITYLIST);
		PlpsDataCenter.getInstance().setCityList(cityList);
	}

	/**
	 * 为请求网络提供统一的请求方法
	 * 
	 * @param requestMethod
	 *            要请求的接口
	 * @param responseMethod
	 *            请求成功后的回调方法
	 * @param params
	 *            参数列表，子类准备此参数
	 * @param needConversationId
	 *            是否需要ConversationId
	 */
	public void requestHttp(String requestMethod, String responseMethod, Map<String, Object> params, boolean needConversationId){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(requestMethod);
		biiRequestBody.setParams(params);
		// 如果需要ConversationId
		if (needConversationId)
			biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, responseMethod);
	}
	
	
	
	/**
	 * 网络请求成功回调  */
	IHttpCallBack requestHttpCallBack;
	/**
	 * 请求ConversationId */
	public void requestCommConversationId(IHttpCallBack callBack){
		requestHttpCallBack = callBack;
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.CONVERSATION_ID_API);
		HttpManager.requestBii(biiRequestBody, this, "requestCommConversationIdCallBack");

	}
	
	
	/**
	 * 请求CommconversationId返回
	 * 
	 * @param resultObj
	 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String commConversationId = (String) biiResponseBody.getResult();
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.CONVERSATION_ID, commConversationId);

		if(requestHttpCallBack != null)
			requestHttpCallBack.requestHttpSuccess(resultObj);
    }

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}
}
