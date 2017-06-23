package com.chinamworld.bocmbci.biz.safety.safetyhold;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.SBRemit;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyBaseActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.sbremit.mysbremit.adapter.SBRemitAccListAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;

public class ChooseAccountActivity extends SafetyBaseActivity {
	/** 账户列表 */
	private ListView cardList;
	/** 下一步按钮 */
	private Button nextBtn;
	/** 列表adapter */
	private SBRemitAccListAdapter accListAdapter;
//	/** 当前选中账户的位置 */
//	private int mCurrentPosition = -1;
	/** 账户列表 */
	List<Map<String, Object>> otherData;
	Map<String, Object> accDetail;
	Map<String, Object> safetyHoldProDetail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.safety_hold_pro_detail_btn_quit));
		addView(R.layout.acc_financeic_trans_choose);
		initViews();
		otherData = SafetyDataCenter.getInstance().getAcctList();
		safetyHoldProDetail = SafetyDataCenter.getInstance().getHoldProDetail();
		setListView(otherData);
	}
	
	private void  initViews(){
		((TextView)findViewById(R.id.tv_financeic_choose_title))
			.setText(getString(R.string.safety_return_noacc_alert));
		((TextView)findViewById(R.id.btnNext))
			.setText(getString(R.string.confirm));
		cardList = (ListView) this.findViewById(R.id.acc_accountlist);
		cardList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				accDetail = otherData.get(position);
				accListAdapter.setSelectedPosition(position);
			}
		});
		nextBtn = (Button) this.findViewById(R.id.btnNext);
		nextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(safetyHoldProDetail != null && accDetail != null){
					requestCommConversationId();
				}else if(accDetail == null)
					BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.safety_return_noacc_alert2));
			}
		});
	}
	
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID).toString());
	}
	
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		requestPsnInsuranceReturn();
	}
	
	/**
	 * 退保
	 * @author fsm
	 */
	public void requestPsnInsuranceReturn() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID).toString());
		biiRequestBody.setMethod(Safety.SafetyInsuranceReturn);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Safety.SAFETY_HOLD_TRANS_DATE, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_TRANS_DATE));
		paramsmap.put(Safety.SAFETY_HOLD_TRANS_ACCNO, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_TRANS_ACCNO));
		paramsmap.put(Safety.SAFETY_HOLD_POLICY_NO, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_POLICY_NO));
		paramsmap.put(Safety.ACC_ID, (String)accDetail.get(Acc.ACC_ACCOUNTID_RES));
		paramsmap.put(Safety.RECV_NAME, (String)accDetail.get(Safety.ACCOUNT_NAME));
		paramsmap.put(Safety.SAFETY_HOLD_INSU_ID, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_INSU_ID));
		paramsmap.put(Safety.SAFETY_HOLD_INSU_NAME, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_INSU_NAME));
		paramsmap.put(Safety.SAFETY_HOLD_RISK_CODE, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_RISK_CODE));
		paramsmap.put(Safety.SAFETY_HOLD_RISK_NAME, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_RISK_NAME));
		paramsmap.put(Safety.SAFETY_HOLD_APPNAME, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_APPNAME));
		paramsmap.put(Safety.SAFETY_HOLD_APPLIDNO, 
				(String)safetyHoldProDetail.get(Safety.SAFETY_HOLD_APPLIDNO));
		paramsmap.put(SBRemit.TOKEN, 
				BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID));
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnInsuranceReturnCallback");
	}
	
	/**
	 * 退保回调
	 * @author fsm
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnInsuranceReturnCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = (Map<String, Object>)this.getHttpTools().getResponseResult(resultObj);
		if(result != null){
			safetyHoldProDetail.put(Safety.BACK_PREM, result.get(Safety.BACK_PREM));
			safetyHoldProDetail.put(Safety.EFFECTIVE_DATA, result.get(Safety.EFFECTIVE_DATA));
			safetyHoldProDetail.putAll(accDetail);
			startActivityForResult(new Intent(this, SafetyInsuranceReturnActivity.class), 1);
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;
		default:
			break;
		}
	}
	
	/**
	 * @Title: setListView
	 * @Description: 填充视图的数据
	 * @param
	 * @return void
	 */
	private void setListView(List<Map<String, Object>> accountList) {
		if(accountList == null)
			return;
		if (accListAdapter == null) {
			accListAdapter = new SBRemitAccListAdapter(this, accountList);
			cardList.setAdapter(accListAdapter);
		} else {
			accListAdapter.setData(accountList);
		}
	}

}
