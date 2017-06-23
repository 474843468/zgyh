package com.chinamworld.bocmbci.biz.plps.payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
import com.chinamworld.bocmbci.biz.plps.adapter.PaymentSpinnerAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PublicTools;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 签约主页
 * @author zxj
 *
 */
public class PaymentSignMainActivity extends PlpsBaseActivity implements OnItemSelectedListener{
	private final String TYPE_PROVICE = "1";
	private final String TYPE_CITY = "2";
	/** 服务品种  **/
	private Spinner mSpServiceName;
	/** 省 **/
//	private Spinner mSpProvice;
	private TextView mProviceText;
	/** 市  **/
	private Spinner mSpCity;
	/** 商户  **/
	private Spinner mSpAgent;
	private int isFirst;
//	private PaymentSpinnerAdapter provAdapter,cityAdapter,agentAdapter;
	private PaymentSpinnerAdapter serviceAdapter,cityAdapter,agentAdapter;
//	private String queryType;
	private String serviceName;
	private String serviceType;
	private String proId;
	private String cityId;
	//市名称
	private String cityName;
	private final List<String> defaultList = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add(Plps.SP_DEFUALTTXT);
		}
	};
//	private List<Map<String, Object>> provList;
//	private List<Map<String, Object>> cityList;
	private List<Map<String, Object>> agentList;
	//缴费签约服务子类列表
	private List<Map<String, Object>> serviceList;
	//服务品种变灰标识
	private Boolean isService = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.plps_payment_sign_main);
		setTitle(PlpsDataCenter.payment[1]);
		setUpViews();
	}
	
	private void setUpViews(){
		mProviceText = (TextView)findViewById(R.id.province);
		mProviceText.setText(PlpsDataCenter.getInstance().getPrvcDispName());
		mSpCity = (Spinner) findViewById(R.id.city);
		mSpServiceName = (Spinner) findViewById(R.id.servicename);
//		mSpProvice = (Spinner) findViewById(R.id.province);
		mSpAgent = (Spinner) findViewById(R.id.agent);
//		mSpProvice.setOnItemSelectedListener(this);
		mSpCity.setOnItemSelectedListener(this);
		mSpServiceName.setOnItemSelectedListener(this);
		setSpinnerBackground(true,mSpCity);
		setSpinnerBackground(false,mSpServiceName,mSpAgent);
//		provAdapter = new PaymentSpinnerAdapter(this, defaultList);
//		cityAdapter = new PaymentSpinnerAdapter(this, defaultList);
		serviceAdapter = new PaymentSpinnerAdapter(this, defaultList);
		agentAdapter = new PaymentSpinnerAdapter(this, defaultList);
//		mSpProvice.setAdapter(provAdapter);
//		mSpCity.setAdapter(cityAdapter);
		mSpServiceName.setAdapter(serviceAdapter);
		mSpAgent.setAdapter(agentAdapter);
//		PlpsUtils.initSpinnerView(this, mSpServiceName, PlpsUtils.initSpinnerData(
//				PlpsDataCenter.getInstance().getServiceType(), Plps.SERVICENAME,Plps.SP_DEFUALTTXT));
		PlpsUtils.initSpinnerView(this, mSpCity, PublicTools.getSpinnerDataWithDefaultValue(
				PlpsDataCenter.getInstance().getCityList(), Plps.CITYNAME,Plps.SP_DEFUALTTXT));
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if (isFirst < 2) {isFirst++;return;}
		if(parent == mSpCity){
			if (position == 0) {
				setSpinnerBackground(true,mSpCity);
				setSpinnerBackground(false,mSpServiceName,mSpAgent);
				isService = false;
				serviceAdapter.setList(defaultList);
				agentAdapter.setList(defaultList);return;
			}
			List<Map<String, Object>> cityList = PlpsDataCenter.getInstance().getCityList();
			cityId = (String)cityList.get(position-1).get(Plps.CITYCODE);
			cityName = (String)cityList.get(position-1).get(Plps.CITYNAME);
			//通过省来查询业务类别
			requestPsnProxyPaymentServiceTypeQueryByArea();
//			requestAgent();
		}
		
//		else if(parent == mSpProvice){
//			if (position == 0) {
//				setSpinnerBackground(true,mSpProvice);
//				setSpinnerBackground(false,mSpCity,mSpAgent); 
//				cityAdapter.setList(defaultList);
//				agentAdapter.setList(defaultList);return;
//			}
//			queryType = TYPE_CITY;
//			proId = (String)provList.get(position-1).get(Plps.PROVINCEID);
//			requestSignArea();
//		}
		else if (parent == mSpServiceName) {
			if (position == 0) {
				if(!isService){
				setSpinnerBackground(false,mSpServiceName,mSpAgent);
//				provAdapter.setList(defaultList);
				isService=true;
				serviceAdapter.setList(defaultList);
				agentAdapter.setList(defaultList);return;
				}else {
					setSpinnerBackground(true,mSpServiceName);
					setSpinnerBackground(false,mSpAgent);
//					provAdapter.setList(defaultList);
//					cityAdapter.setList(defaultList);
					agentAdapter.setList(defaultList);return;
				}
			}
			Map<String, Object> map = serviceList.get(position-1);
			serviceName = (String) map.get(Plps.SERVICENAME);
			serviceType = (String) map.get(Plps.SERVICETYPE);
//			queryType = TYPE_PROVICE;
//			requestSignArea();
//			requestProxyPaymentCityQuery();
			requestAgent();
			
		}
		
	}


	@Override
	public void onNothingSelected(AdapterView<?> parent) {}
	
	/**
	 * 下一步
	 * @param v
	 */
	public void btnNextOnclick(View v) {
		if ((mSpCity.getSelectedItem().toString()).equals(Plps.SP_DEFUALTTXT)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请选择城市");
			return;
		} else if ((mSpServiceName.getSelectedItem().toString())
				.equals(Plps.SP_DEFUALTTXT)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请选择服务品种");
			return;
		}
		// else
		// if((mSpProvice.getSelectedItem().toString()).equals(Plps.SP_DEFUALTTXT)){
		// BaseDroidApp.getInstanse().showInfoMessageDialog("请选择省份"); return;
		// }
		else if ((mSpAgent.getSelectedItem().toString())
				.equals(Plps.SP_DEFUALTTXT)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请选择商户名称");
			return;
		}
		BaseHttpEngine.showProgressDialog();
		requestAcctList();
	}
	
	/**
	 * 设置sipnner背景
	 * @param isdefault
	 * @param v
	 */
	private void setSpinnerBackground(boolean isdefault,View... v){
		if(v.length > 0){
			for(View sp : v){
				if(sp != null){
					if (isdefault){
						sp.setClickable(true);
						sp.setBackgroundResource(R.drawable.bg_spinner);
					}else{
						sp.setClickable(false);
						sp.setBackgroundResource(R.drawable.bg_spinner_default);
					}
				}
			}
		}
	}
	
//	@SuppressWarnings("unchecked")
//	public void requestProxyPaymentCityQueryCallBack(Object resultObj){
//		BaseHttpEngine.dissMissProgressDialog();
//		Map<String, Object> map = (Map<String, Object>)PlpsUtils.httpResponseDeal(resultObj);
//		if(StringUtil.isNullOrEmpty(map)) return;
//		cityList = (List<Map<String,Object>>)map.get(Plps.CITYLIST);
//		
//	}
	/**
	 * 查询地区
	 */
//	private void requestSignArea(){
//		BaseHttpEngine.showProgressDialog();
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(Plps.METHODSIGNAREA);
//		HashMap<String, Object> param = new HashMap<String, Object>();
//		param.put(Plps.SERVICETYPE, serviceType);
//		param.put(Plps.QUERYTYPE, queryType);
//		param.put(Plps.PROVINCEID, proId);
//		biiRequestBody.setParams(param);
//		HttpManager.requestBii(biiRequestBody, this, "signAreaCallBack");
//	}
	
//	@SuppressWarnings("unchecked")
//	public void signAreaCallBack(Object resultObj) {
//		BaseHttpEngine.dissMissProgressDialog();
//		Map<String, Object> result = (Map<String, Object>) PlpsUtils.httpResponseDeal(resultObj);
//		if (StringUtil.isNullOrEmpty(result)) return;
//		if (queryType == TYPE_PROVICE) {
//			provList = (List<Map<String, Object>>) result.get(Plps.PROVINCELIST);
//			provAdapter.setList(PlpsUtils.initSpinnerData(provList, Plps.PROVINCENAME, Plps.SP_DEFUALTTXT));
//			setSpinnerBackground(true, mSpProvice);
//		}
//		else
//			if(queryType == TYPE_CITY){
//			cityList = (List<Map<String, Object>>) result.get(Plps.CITYLIST);
//			cityAdapter.setList(PlpsUtils.initSpinnerData(cityList, Plps.CITYNAME, Plps.SP_DEFUALTTXT));
//			setSpinnerBackground(true, mSpCity);
//		}
//	}
	/**
	 * 通过省来查询业务类别*/
	private void requestPsnProxyPaymentServiceTypeQueryByArea(){
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.PLPSPAYMENTSERVICETYPE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Plps.PRVCSHORTNAME, PlpsDataCenter.getInstance().getPrvcShortName());
		map.put(Plps.CITYCODE, cityId);
		map.put(Plps.CITYNAME, cityName);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "PsnProxyPaymentServiceTypeQueryByAreaCallBack");
	}
	@SuppressWarnings("unchecked")
	public void PsnProxyPaymentServiceTypeQueryByAreaCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		if(StringUtil.isNullOrEmpty(map))return;
		serviceList = (List<Map<String,Object>>)map.get(Plps.PLPSSERVICELICT);
		serviceAdapter.setList(PublicTools.getSpinnerDataWithDefaultValue(serviceList, Plps.SERVICENAME, Plps.SP_DEFUALTTXT));
		mSpServiceName.setSelection(0);
		setSpinnerBackground(true, mSpServiceName);
//		requestAgent();
	}
	
	/**
	 * 请求商户
	 */
	private void requestAgent(){
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.METHODAGENT);
		HashMap<String, Object> param = new HashMap<String, Object>();
//		param.put(Plps.QUERYTYPE, queryType);
		param.put(Plps.SERVICETYPE, serviceType);
		param.put(Plps.PRVCSHORTNAME, PlpsDataCenter.getInstance().getPrvcShortName());
//		param.put(Plps.PROVINCEID, proId);
		param.put(Plps.CITYCODE, cityId);
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "agentCallBack");
	}
	
	@SuppressWarnings("unchecked")
	public void agentCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		agentList = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(agentList)) return;
		agentAdapter.setList(PublicTools.getSpinnerDataWithDefaultValue(agentList, Plps.AGENTNAME, Plps.SP_DEFUALTTXT));
		mSpAgent.setSelection(0);
		setSpinnerBackground(true, mSpAgent);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void bankAccListCallBack(Object resultObj) {
		List<Map<String, Object>> list = HttpTools.getResponseResult(resultObj);
		if(StringUtil.isNullOrEmpty(list)){
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showMessageDialog("无可签约的签约帐号", new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					BaseDroidApp.getInstanse().dismissMessageDialog();
				}
			});
			 return;
		}else {
			PlpsDataCenter.getInstance().setAcctList(list);
			requestCustomerInfo();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void customerInfoCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		String phoneNumber = (String) result.get(Plps.PHONENUMBER);
		Map<String, Object> map = agentList.get(mSpAgent.getSelectedItemPosition()-1);
		Intent intent = new Intent(this, PaymentSignInputActivity.class)
		.putExtra(Plps.SUBAGENTCODE, (String)map.get(Plps.SUBAGENTCODE))
		.putExtra(Plps.AGENTNAME, (String)map.get(Plps.AGENTNAME))
		.putExtra(Plps.AGENTCODE, (String)map.get(Plps.AGENTCODE))
		.putExtra(Plps.CSPCODE, (String)map.get(Plps.CSPCODE))
		.putExtra(Plps.SERVICENAME, serviceName)
		.putExtra(Plps.PHONENUMBER, phoneNumber)
		.putExtra(Plps.SERVICETYPE, serviceType)
		.putExtra(Plps.SIGNTIP, (String)map.get(Plps.SIGNTIP));
		if (StringUtil.isNull(phoneNumber)){
			intent.putExtra("flag", 2);
			startActivityForResult(intent,1001); 
			return;
		}
		super.customerInfoCallBack(resultObj);
		intent.putExtra("flag", 3);
		startActivityForResult(intent,1001);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			setResult(RESULT_OK);
//			mSpCity.setSelection(0);
//			mSpServiceName.setSelection(0);
//			mSpAgent.setSelection(0);
			finish();
		}
	}
}