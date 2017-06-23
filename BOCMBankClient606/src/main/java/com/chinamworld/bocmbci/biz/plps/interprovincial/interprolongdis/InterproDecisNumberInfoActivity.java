package com.chinamworld.bocmbci.biz.plps.interprovincial.interprolongdis;

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
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PublicTools;
import com.chinamworld.bocmbci.utils.StringUtil;

/*
 * 跨省异地交通违法罚款  信息页面
 * @author zxj
 */
public class InterproDecisNumberInfoActivity extends PlpsBaseActivity implements OnItemSelectedListener{
	/**决定书编号查询交通罚款返回信息*/
	private Map<String, Object> decisionBookNo;
	/**决定书编号*/
	private String decisionNo;
	private TextView decisionNoTextView;
	/**驾驶证号*/
	private String driverLicenseNo;
	private TextView driverLicenseNoTextView;
	/**当事人*/
	private String party;
	private TextView partyTextView;
	/**处理时间*/
	private String dealTime;
	private TextView dealTimeTextView;
	/**处理机关*/
	private String dealAuthority;
	private TextView dealAuthorityTextView;
	/**处理机关名称*/
	private String dealAuthorityName;
	private TextView dealAuthorityNameTextView;
	/**违法地点*/
	private String illegalPlace;
	private TextView illegelPlaceTextView;
	/**罚款金额*/
	private String fineAmount;
	private TextView fineAmountTextView;
	/**加处金额*/
	private String additionalAmount;
	private TextView additionalAmountTextView;
	/**总金额*/
	private String totalAmount;
	private TextView totalAmountTextView;
	/**违法地省行机构号*/
	private String inprvorgid;
	/**档案编号*/
	private String recordNo;
	/**发证机关*/
	private String issueAuthority;
	/**缴费帐号列表*/
	private List<Map<String, Object>> accountLists = new ArrayList<Map<String,Object>>();
	/**缴费帐号*/
	private Spinner paymentAmountSpinner;
	/**可用余额*/
	private TextView availableBalanceTextView;
	private String availableBalance;
	//报文中的可用余额值
	private String availableBalancermb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(R.string.plps_interprov_titlename);
		inflateLayout(R.layout.plps_interprov_dicisnuminfo);
		getData();
		init();
		initViewData();
	}
	/**获取数据*/
	private void getData(){
		decisionBookNo = (Map<String, Object>) PlpsDataCenter.getInstance().getDecisionBookNo();
		accountLists = (List<Map<String,Object>>)PlpsDataCenter.getInstance().getAcctList();
		availableBalance = (String)getIntent().getStringExtra(Plps.AVAILABLEBALANCE);
		decisionNo = (String)decisionBookNo.get(Plps.DECISIONNO);
		driverLicenseNo = (String)decisionBookNo.get(Plps.DRIVERLICENSENO);
		party = (String)decisionBookNo.get(Plps.PARTY);
		dealTime = (String)decisionBookNo.get(Plps.DEALTIME);
		dealAuthority = (String)decisionBookNo.get(Plps.DEALAUTHORITY);
		dealAuthorityName = (String)decisionBookNo.get(Plps.DEALAUTHORITYNAME);
		illegalPlace = (String)decisionBookNo.get(Plps.ILLEGAPLACE);
		fineAmount = (String)decisionBookNo.get(Plps.FINEAMOUNT);
		additionalAmount = (String)decisionBookNo.get(Plps.ADDITIONALAMOUNT);
		totalAmount = (String)decisionBookNo.get(Plps.TOTALAMOUNT);
		inprvorgid = (String)decisionBookNo.get(Plps.INPRVORGID);
		recordNo = (String)decisionBookNo.get(Plps.RECORDNO);
		issueAuthority = (String)decisionBookNo.get(Plps.ISSUEAUTHORITY);
	}
	/**初始话布局*/
	private void init(){
		decisionNoTextView = (TextView)findViewById(R.id.decision);
		driverLicenseNoTextView = (TextView)findViewById(R.id.driverno);
		partyTextView = (TextView)findViewById(R.id.party);
		dealTimeTextView = (TextView)findViewById(R.id.dealtime);
		dealAuthorityTextView = (TextView)findViewById(R.id.dealauthority);
		dealAuthorityNameTextView = (TextView)findViewById(R.id.dealauthorityname);
		illegelPlaceTextView = (TextView)findViewById(R.id.illegalplace);
		fineAmountTextView = (TextView)findViewById(R.id.fineamount);
		additionalAmountTextView = (TextView)findViewById(R.id.additional);
		totalAmountTextView = (TextView)findViewById(R.id.total_amount);
		paymentAmountSpinner = (Spinner)findViewById(R.id.signacct);
		availableBalanceTextView = (TextView)findViewById(R.id.available_amount);
	}
	/**布局中加载数据*/
	private void initViewData(){
		if(!StringUtil.isNullOrEmpty(decisionNo)){
			decisionNoTextView.setText(decisionNo);
		}
		if(!StringUtil.isNullOrEmpty(driverLicenseNo)){
			driverLicenseNoTextView.setText(driverLicenseNo);
		}
		if(!StringUtil.isNullOrEmpty(party)){
			partyTextView.setText(party);
		}
		if(!StringUtil.isNullOrEmpty(dealTime)){
			dealTimeTextView.setText(dealTime);
		}
		if(!StringUtil.isNullOrEmpty(dealAuthority)){
			dealAuthorityTextView.setText(dealAuthority);
		}
		if(!StringUtil.isNullOrEmpty(dealAuthorityName)){
			dealAuthorityNameTextView.setText(dealAuthorityName);
		}
		if(!StringUtil.isNullOrEmpty(illegalPlace)){
			illegelPlaceTextView.setText(illegalPlace);
		}
		if(!StringUtil.isNullOrEmpty(fineAmount)){
			String fineAmountt = StringUtil.parseStringPattern(fineAmount, 2);
			fineAmountTextView.setText(fineAmountt);
		}
		if(!StringUtil.isNullOrEmpty(additionalAmount)){
			String additionalAmountt = StringUtil.parseStringPattern(additionalAmount, 2);
			additionalAmountTextView.setText(additionalAmountt);
		}
		if(!StringUtil.isNullOrEmpty(totalAmount)){
			String totalAmountt = StringUtil.parseStringPattern(totalAmount, 2);
			totalAmountTextView.setText(totalAmountt);
		}
		if(!StringUtil.isNullOrEmpty(availableBalance)){
			availableBalance = StringUtil.parseStringPattern(availableBalance, 2);
			availableBalanceTextView.setText(availableBalance);
		}
		initAccSpinnerData();
	}
	/**缴费帐号数据加载*/
	private void initAccSpinnerData(){
		PlpsUtils.initSpinnerView(this, paymentAmountSpinner, PublicTools.getSpinnerDataWithDefaultValue(accountLists, Comm.ACCOUNTNUMBER, null));
		paymentAmountSpinner.setOnItemSelectedListener(this);
		paymentAmountSpinner.setSelection(0);
		PlpsUtils.setOnShowAllTextListener(this, decisionNoTextView, driverLicenseNoTextView, partyTextView, dealTimeTextView,
				dealAuthorityTextView, dealAuthorityNameTextView, illegelPlaceTextView, fineAmountTextView, additionalAmountTextView,
				totalAmountTextView);
	}
	
	/**下一步按钮*/
	public void nextBtnClick(View v){
		if(submitCheck()){
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
		}
	}
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestGetSecurityFactor(Plps.INTERPRODECISCODE);
	}
	/**请求预交易*/
	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BaseDroidApp.getInstanse().showSeurityChooseDialog(
			new OnClickListener() {
				@Override
				public void onClick(View v) {
					BaseHttpEngine.showProgressDialog();
					Map<String, Object> params = new HashMap<String, Object>();
					params.put(Plps.DECISIONNO, decisionNo);
					params.put(Plps.DRIVERLICENSENO, driverLicenseNo);
					params.put(Plps.PARTY, party);
					params.put(Plps.DEALTIME, dealTime);
					params.put(Plps.DEALAUTHORITY, dealAuthority);
					params.put(Plps.DEALAUTHORITYNAME, dealAuthorityName);
					params.put(Plps.ILLEGAPLACE, illegalPlace);
					params.put(Plps.FINEAMOUNT, fineAmount);
					params.put(Plps.ADDITIONALAMOUNT, additionalAmount);
					params.put(Plps.TOTALAMOUNT, totalAmount);
					params.put(Plps.INPRVORGID, inprvorgid);
					params.put(Plps.RECORDNO, recordNo);
					params.put(Plps.ISSUEAUTHORITY, issueAuthority);
					params.put(Comm.ACCOUNT_ID, PlpsDataCenter.getInstance().getAcctList().get(paymentAmountSpinner.getSelectedItemPosition()).get(Comm.ACCOUNT_ID));
					params.put(Plps.ACCOUNTNOM, PlpsDataCenter.getInstance().getAcctList().get(paymentAmountSpinner.getSelectedItemPosition()).get(Comm.ACCOUNTNUMBER));
					params.put(Plps.COMBINID, BaseDroidApp.getInstanse().getSecurityChoosed());
					requestHttp(Plps.PSNTRAFFICFINESPAYMENTCONFIRM, "requestPsnPrepaidCardReplenishmentPreCallBack", params, true);
				}
			});
	}
	/**请求预交易回调*/
	@SuppressWarnings("unchecked")
	public void requestPsnPrepaidCardReplenishmentPreCallBack(Object resultObj){
		PlpsDataCenter.getInstance().setInterMapresult(null);
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		if(StringUtil.isNullOrEmpty(resultMap)){
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}else {
			PlpsDataCenter.getInstance().setInterMapresult(resultMap);
			requestHttp(Login.COMM_RANDOM_NUMBER_API, "requestPSNGetRandomCallBack", null, true);
		}
	}

	/** 随机数请求回调 */
	public void requestPSNGetRandomCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		String randomNumber = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(randomNumber)) {
			return;
		} else {
			startActivityForResult(
					new Intent(InterproDecisNumberInfoActivity.this,
							InterproDecisNumberConfirmInfoActivity.class).putExtra(
							Comm.ACCOUNTNUMBER,
							(String) PlpsDataCenter
									.getInstance()
									.getAcctList()
									.get(paymentAmountSpinner
											.getSelectedItemPosition())
									.get(Comm.ACCOUNTNUMBER))
									.putExtra(Comm.ACCOUNT_ID, (String) PlpsDataCenter
									.getInstance()
									.getAcctList()
									.get(paymentAmountSpinner
											.getSelectedItemPosition())
									.get(Comm.ACCOUNT_ID))
									.putExtra(Plps.DECISIONNO, decisionNo)
									.putExtra(Plps.DRIVERLICENSENO, driverLicenseNo)
									.putExtra(Plps.PARTY, party)
									.putExtra(Plps.DEALTIME, dealTime)
									.putExtra(Plps.DEALAUTHORITY, dealAuthority)
									.putExtra(Plps.DEALAUTHORITYNAME, dealAuthorityName)
									.putExtra(Plps.ILLEGAPLACE, illegalPlace)
									.putExtra(Plps.FINEAMOUNT, fineAmount)
									.putExtra(Plps.ADDITIONALAMOUNT, additionalAmount)
									.putExtra(Plps.TOTALAMOUNT, totalAmount)
									.putExtra(Plps.RANDOMNUMBER, randomNumber)
									, 1011);
		}
	}
	
	/**校验是否选择缴费帐号*/
	private Boolean submitCheck(){
		Double avalAmounts = Double.parseDouble(availableBalancermb);
		Double total = Double.parseDouble(totalAmount);
		if(paymentAmountSpinner.getSelectedItemPosition() == -1){
			BaseDroidApp.getInstanse().showInfoMessageDialog("请选择银行账户");
			return false;
		}else if (avalAmounts<total) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("账户可用余额不足，请检查");
			return false;
		}else {
			return true;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			setResult(RESULT_OK);
			finish();
		}
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if(StringUtil.isNullOrEmpty(accountLists)){
			BaseDroidApp.getInstanse().showMessageDialog("无可用银行账户", new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					BaseDroidApp.getInstanse().dismissMessageDialog();
				}
			});
		}else {
			BaseHttpEngine.showProgressDialog();
			Map<String, Object> params = new HashMap<String, Object>();
			String accountId = (String)accountLists.get(position).get(Comm.ACCOUNT_ID);
			params.put(Comm.ACCOUNT_ID, accountId);
			httpTools.requestHttp(Plps.PSNACCOUNTQUERYACCOUNTDETAIL, "requestPsnAccountQueryAccountDetailCallBack", params, false);
		}
	}
	@SuppressWarnings("unchecked")
	public void requestPsnAccountQueryAccountDetailCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		List<Map<String, Object>> detailList = (List<Map<String,Object>>)resultMap.get(Plps.ACCOUNTDETAILIST);
		Boolean isRmb = false;
		if(StringUtil.isNullOrEmpty(detailList)){
			availableBalanceTextView.setText("0");
		}else {
			for(int i=0; i<detailList.size(); i++){
				String currencyCode = (String)detailList.get(i).get(Plps.CURRENCYCODE);
				if(currencyCode.equals("001")){
					isRmb = true;
					availableBalancermb = (String)detailList.get(i).get(Plps.AVAILABLEBALANCE);
					availableBalance = StringUtil.parseStringPattern(availableBalancermb, 2);
					availableBalanceTextView.setText(availableBalance);
				}else {
					continue;
				}
			}
		}
		if(!isRmb){
			availableBalanceTextView.setText("0");
		}
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
}
