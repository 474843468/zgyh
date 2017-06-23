package com.chinamworld.bocmbci.biz.finc.accmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.InvtEvaluationResultActivity;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.adapter.MyFincBalanceAccAdapter;
import com.chinamworld.bocmbci.biz.finc.myfund.FincChangCardActivity;
import com.chinamworld.bocmbci.biz.finc.myfund.MyFincBalanceResetAccSubmitActivity;
import com.chinamworld.bocmbci.biz.invest.constant.InvestConstant;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.ViewUtils;
import com.chinamworld.bocmbci.widget.CustomDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 基金账户管理 页面
 * @author xiaoyl
 *
 */
public  class FincAccManagerActivity extends FincBaseActivity {
	/** 资金账户,基金账户,风险级别 */
	private TextView accNumTv, fincAccTv, riskLevelTv;
	private ListView accBalanceListView;
	/** 重新评估,重新登记资金账户 */
	private Button resetRiskBtn, resetAccBtn;
	private List<Map<String, Object>> accountDetaiList;
	private MyFincBalanceAccAdapter adapter;
	//账户类型
	private String accountType;
	//account
	private String accountNumber;
	//accountId
	private String accountID;
	//信用卡集合
	private List<Map<String, Object>> mmListf ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		accountType=fincControl.accountType;
		accountNumber=fincControl.accNum;
		accountID=fincControl.accId;
		if(accountType.equals("103")||accountType.equals("104")||accountType.equals("107")){
			BaseHttpEngine.showProgressDialogCanGoBack();
			//x信用卡接口调用，在调用信用卡接口先调用PsnCrcdCurrencyQuery，这个接口返回两个值，如果第一个为空则用第二个
			//调用信用卡接口需要穿两个参数
//			requestForOutCrcdCurrency(accountNumber);
			/** 经确认为信用卡的时候调PsnFincQueryQccBalance接口 */
			queryQccBanlance(accountID);
		}else {
			BaseHttpEngine.showProgressDialogCanGoBack();
			//借记卡调用接口
			requestPsnAccountQueryAccountDetail(fincControl.accId);
		}
		
	}

	/**
	 * 基金持仓--查询风险评估等级---回调
	 * 
	 * @author 宁焰红 为RiskEvaluationText赋值
	 * @param resultObj
	 */
	public void requestPsnFundRiskEvaluationQueryResultCallback(Object resultObj) {
		super.requestPsnFundRiskEvaluationQueryResultCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		setRiskLevel();
	}

	/**
	 * 根据账户标志，查询账户详细信息----回调
	 * 
	 * @author 宁焰红
	 * @param resultObj
	 */
	@Override
	public void requestPsnAccountQueryAccountDetailCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.requestPsnAccountQueryAccountDetailCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		if (result == null) {
			return;
		} else {
			accountDetaiList = (List<Map<String, Object>>) result
					.get(Finc.FINC_ACCOUNTDETAILLIST_RES);
			if (accountDetaiList != null) {
				accountType=fincControl.accountType;
				adapter = new MyFincBalanceAccAdapter(this, accountDetaiList,accountType);
				accBalanceListView.setAdapter(adapter);
			}
		}

	}

	private void initView() {
		View childView = mainInflater.inflate(
				R.layout.finc_accmanager_activity, null);
		tabcontent.addView(childView);
		setTitle(R.string.fincn_accmanner_fundacc);
		accNumTv = (TextView) findViewById(R.id.finc_monyacc_tv);
		fincAccTv = (TextView) findViewById(R.id.finc_fundacc_tv);
		riskLevelTv = (TextView) findViewById(R.id.finc_risklevel_tv);
		resetRiskBtn = (Button) findViewById(R.id.finc_btn1);
		resetAccBtn = (Button) findViewById(R.id.finc_btn2);
		accBalanceListView = (ListView) findViewById(R.id.finc_ListView);
		right.setText(getString(R.string.boci_evaluation_title));
		ViewUtils.initBtnParamsTwoLeft(resetRiskBtn, this);
		ViewUtils.initBtnParamsTwoRight(resetAccBtn, this);
		setAccInfo();
		setRiskLevel();
	}

	/**
	 * 设定账户信息
	 */
	private void setAccInfo() {
		accNumTv.setText(StringUtil.getForSixForString(fincControl.accNum));
		fincAccTv.setText(fincControl.invAccId);
	}

	private void setRiskLevel() {
		riskLevelTv.setText(LocalData.fincRiskLevelCodeToStr.get(String.valueOf(fincControl.userRiskLevel)));
	}

	private void initListenner() {
		resetRiskBtn.setOnClickListener(this);
//		resetAccBtn.setOnClickListener(this);
		right.setOnClickListener(this);
		String[] function = getMoreBtnSelector();
		if(function.length == 1){
			resetAccBtn.setText(function[0]);
			resetAccBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					cancelAccAlertFrame();
				}
			});
		}else{
			PopupWindowUtils.getInstance().setshowMoreChooseUpListener(this, resetAccBtn, function,
					new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Integer tag = (Integer) v.getTag();
					switch (tag) {
					case 0://销户
						cancelAccAlertFrame();
						break;
					case 1://重新登记
						Intent intent = new Intent(FincAccManagerActivity.this, MyFincBalanceResetAccSubmitActivity.class);
						startActivityForResult(intent,
								ConstantGloble.ACTIVITY_REQUEST_SETFINCSACC_CODE);
						break;
	
					default:
						break;
					}
				}
			});
		}
	}
	
	private void cancelAccAlertFrame(){
		BaseDroidApp.getInstanse().showErrorDialog(
				FincAccManagerActivity.this.getString(R.string.fincn_discharge_info),
				R.string.cancle, R.string.confirm, new OnClickListener() {
					@Override
					public void onClick(View v) {
						switch (Integer.parseInt(v.getTag() + "")) {
						case CustomDialog.TAG_SURE:
							BaseDroidApp.getInstanse().dismissErrorDialog();
							requestCommConversationId();
							BaseHttpEngine.showProgressDialog();
							break;
						case CustomDialog.TAG_CANCLE:
							BaseDroidApp.getInstanse()
							.dismissErrorDialog();
							break;
						}
					}
				});
	}
	/**
	 * 
	 * @return	更多按钮显示的链接
	 */
	private final String[] getMoreBtnSelector(){
		String selectors[]= new String[1]; 
		selectors[0]=getString(R.string.fincn_discharge);//销户
//		selectors[1]=getString(R.string.bond_acct_reset);//重新登记
		return selectors;
	}

	private void init() {
		initView();
		initListenner();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent;
		switch (v.getId()) {
		case R.id.finc_btn1:// 变更资金账户
			intent = new Intent(this, FincChangCardActivity.class);
			startActivityForResult(intent,
					ConstantGloble.ACTIVITY_REQUEST_SETFINCSACC_CODE);
			break;
		case R.id.finc_btn2://更多
//			intent = new Intent(this, MyFincBalanceResetAccSubmitActivity.class);
//			startActivityForResult(intent,
//					ConstantGloble.ACTIVITY_REQUEST_SETFINCSACC_CODE);
			break;

		case R.id.ib_top_right_btn://风险评估
			intent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(),
					InvtEvaluationResultActivity.class);
			intent.putExtra(InvestConstant.RISKTYPE, InvestConstant.FUNDRISK);
			// intent.putExtra(InvestConstant.FROMMYSELF, true);
			intent.putExtra(ConstantGloble.BOCINVT_ISNEWEVA, true);
			BaseDroidApp.getInstanse().getCurrentAct()
					.startActivityForResult(intent, InvestConstant.FUNDRISK);
		
			break;
		default:
			break;
		}
	}
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId();
	}
	@Override
	public void requestPSNGetTokenIdCallback(Object resultObj) {
		super.requestPSNGetTokenIdCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(tokenId)) {
			return;
		}
		fundAccDischarge(tokenId);
	}
	@Override
	public void fundAccDischargeCallback(Object resultObj) {
		super.fundAccDischargeCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		CustomDialog.toastShow(this,
				this.getString(R.string.fincn_discharge_success));
		activityTaskManager.removeAllActivity();
	}
	/**
	 * 查询基金账户---回调 为fincAccinfoText赋值
	 * 
	 * @author 宁焰红
	 * @param resultObj
	 */
	public void requestQueryInvtBindingInfoCallback(Object resultObj) {
		super.requestQueryInvtBindingInfoCallback(resultObj);
		setAccInfo();
		accountType=fincControl.accountType;
		String accountID=fincControl.accId;
		if(accountType.equals("103")||accountType.equals("104")||accountType.equals("107")){
			BaseHttpEngine.showProgressDialogCanGoBack();
			queryQccBanlance(accountID);
		}else {
			BaseHttpEngine.showProgressDialogCanGoBack();
			//借记卡调用接口
			requestPsnAccountQueryAccountDetail(fincControl.accId);
		}
//		requestPsnAccountQueryAccountDetail(fincControl.accId);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ConstantGloble.ACTIVITY_REQUEST_SETFINCSACC_CODE:
			switch (resultCode) {
			case RESULT_OK:
				
				adapter.notifyDataSetChanged(new ArrayList<Map<String, Object>>() {
				},accountType);
				
				BaseHttpEngine.showProgressDialogCanGoBack();
				requestQueryInvtBindingInfo();
				break;

			default:
				break;
			}
			break;
		case InvestConstant.FUNDRISK:
			switch (resultCode) {
			case RESULT_OK:
				BaseHttpEngine.showProgressDialogCanGoBack();
				
				requestPsnFundRiskEvaluationQueryResult();
				break;

			default:
				break;
			}
			break;
		
		}
	}
	private void requestForOutCrcdCurrency(String accountNumber) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.CREDITCARDCURRENCYQUERY);

		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.ACCOUNTNUMBER_RES, accountNumber);

		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestForOutCrcdCurrencyCallBack");
	}
	@SuppressWarnings("unchecked")
	public void requestForOutCrcdCurrencyCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		String  currency1=((Map<Object, String>)result.get("currency1")).get("code");
		
		String currency;
		if(currency1==null){
			String currency2=((Map<Object, String>) result.get("currency2")).get("code");
			currency=currency2 ;
		}else {
			currency=currency1;
		}
		//信用卡详情接口
		requestForCrcdDetail(currency);
	}
	/**
	 * 查询信用卡详情 PsnCrcdQueryAccountDetail
	 * 
	 * @param currency
	 */
	private void requestForCrcdDetail(String currency) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		Map<String, String> paramsmap = new HashMap<String, String>();
		biiRequestBody.setMethod(Tran.CRCDQUERYACCOUNTDETAIL);
		paramsmap.put(Tran.QUERY_CONDITION_CRCD_ID, fincControl.accId);
		// 默认设置为人民币元
		paramsmap.put(Tran.QUERY_CONDITION_CRCD_CURRENCY,currency);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestForCrcdDetailCallBack");
	}
	/**
	 * 查询信用卡详情返回 PsnCrcdQueryAccountDetail
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestForCrcdDetailCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
		detailList = (List<Map<String, Object>>) resultMap.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
		
		if (detailList != null) {
			adapter = new MyFincBalanceAccAdapter(this, detailList,accountType);
			accBalanceListView.setAdapter(adapter);
			BaseHttpEngine.dissMissProgressDialog();
		}
		if (StringUtil.isNullOrEmpty(detailList)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void queryQccBanlanceCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object>	resultMap = (Map<String, Object>) biiResponseBody.getResult();
		 mmListf = new ArrayList<Map<String,Object>>();
		
		
		
		for (int i = 0; i < resultMap.size(); i++) {
			
			int q=resultMap.size();
			String qccbal="qccBalance" + ""+(char)('A' + i);
			Map<String, Map<String, Object>> midMap2= (Map<String, Map<String, Object>>) resultMap.get(qccbal);
			if(!StringUtil.isNullOrEmpty(midMap2)){
				Map<String, Object> midMap3=midMap2.get("currency");
				String codeString= (String)midMap3.get("code");
				Object cashremit=midMap2.get("cashremit");
				Object currentBalance=midMap2.get("currentBalance");
				
				Map<String, Object> mapfinalMap=new HashMap<String, Object>();
				mapfinalMap.put("code", codeString);
				mapfinalMap.put("cashRemit", cashremit);
				mapfinalMap.put("currentBalance", currentBalance);
				mmListf.add(mapfinalMap);
			}
		}
		LogGloble.e("asd", mmListf.size()+"");
		if (resultMap != null) {
			adapter = new MyFincBalanceAccAdapter(this, mmListf,accountType);
			accBalanceListView.setAdapter(adapter);
			BaseHttpEngine.dissMissProgressDialog();
		}
		if (StringUtil.isNullOrEmpty(resultMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		
	}
	

}
