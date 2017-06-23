package com.chinamworld.bocmbci.biz.plps.interprovincial.interprolongdis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
/*
 * 跨省异地交通违法罚款 填写决定书编号页面
 * @author zxj
 */
public class InterproDecisNumbActivity extends PlpsBaseActivity{
	/**决定书编号输入框*/
	private EditText dicisNum;
	/**决定书编号*/
	private String decisionNo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(R.string.plps_interprov_titlename);
		inflateLayout(R.layout.plps_interprov_decisnum);
		dicisNum = (EditText)findViewById(R.id.dicisnum);
		EditTextUtils.setLengthMatcher(this, dicisNum, 15);
	}
	public void btnNextOnclick(View v){
		decisionNo = (String)dicisNum.getText().toString();
		if(submitCheck()){
			requestPsnQueryTrafficFinesByDecisionBookNo();
		}
	}
	/**决定书编号校验*/
	private Boolean submitCheck(){
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		if(StringUtil.isNullOrEmpty(dicisNum)){
			BaseDroidApp.getInstanse().showMessageDialog("请输入决定书编号", new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					BaseDroidApp.getInstanse().dismissMessageDialog();
				}
			});
		}else {
			RegexpBean passwordNumber = new RegexpBean(Plps.PASSWORDDECISION, decisionNo, "decisino");
			lists.add(passwordNumber);
			if(RegexpUtils.regexpData(lists)){
				return true;
			}
		}
		
		return false;
	}
	/**根据决定书编号查询交通罚款信息*/
	private void requestPsnQueryTrafficFinesByDecisionBookNo(){
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.PSNQUERYTRAFFICFINESBYDECISIONBOOKNO);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Plps.DECISIONNO, decisionNo);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnQueryTrafficFinesByDecisionBookNoCallBack");
	}

	@SuppressWarnings("unchecked")
	public void requestPsnQueryTrafficFinesByDecisionBookNoCallBack(
			Object resultObj) {
		PlpsDataCenter.getInstance().setDecisionBookNo(null);
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog("抱歉！暂未查询到该决定书编号信息。");
			return;
		} else {
			PlpsDataCenter.getInstance().setDecisionBookNo(result);
			requestPsnCommonQueryAllChinaBankAccount();
		}
	}
	/**缴费帐号请求-只支持借记卡*/
	private void requestPsnCommonQueryAllChinaBankAccount(){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_ALL_BANK_ACCOUNT);
		Map<String, Object> params = new HashMap<String, Object>();
		List<String> acctype = new ArrayList<String>();
		acctype.add("119");
		params.put(Comm.ACCOUNT_TYPE, acctype);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnCommonQueryAllChinaBankAccount");
	}

	@SuppressWarnings("unchecked")
	public void requestPsnCommonQueryAllChinaBankAccount(Object resultObj) {
		PlpsDataCenter.getInstance().setAcctList(null);
		List<Map<String, Object>> list = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(list)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			PlpsDataCenter.getInstance().setAcctList(list);
			Map<String, Object> params = new HashMap<String, Object>();
			String accountId = (String)list.get(0).get(Comm.ACCOUNT_ID);
			params.put(Comm.ACCOUNT_ID, accountId);
			httpTools.requestHttp(Plps.PSNACCOUNTQUERYACCOUNTDETAIL, "requestPsnAccountQueryAccountDetailCallBack", params,false);
				
				
		}
	}
	@SuppressWarnings("unchecked")
	public void requestPsnAccountQueryAccountDetailCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		List<Map<String, Object>> detailList = (List<Map<String,Object>>)resultMap.get(Plps.ACCOUNTDETAILIST);
		Boolean isRmb = false;
		if(StringUtil.isNullOrEmpty(detailList)){
			if(!isRmb){
				startActivityForResult(new Intent(InterproDecisNumbActivity.this,
						InterproDecisNumberInfoActivity.class)
				.putExtra(Plps.AVAILABLEBALANCE, 0), 101);
			}
		}else {
			for(int i=0; i<detailList.size(); i++){
				String currencyCode = (String)detailList.get(i).get(Plps.CURRENCYCODE);
				if(currencyCode.equals("001")){
					isRmb = true;
					String availableBalance = (String)detailList.get(i).get(Plps.AVAILABLEBALANCE);
					startActivityForResult(new Intent(InterproDecisNumbActivity.this,
							InterproDecisNumberInfoActivity.class)
					.putExtra(Plps.AVAILABLEBALANCE, availableBalance), 101);
				}else {
					continue;
				}
			}
			if(!isRmb){
				startActivityForResult(new Intent(InterproDecisNumbActivity.this,
						InterproDecisNumberInfoActivity.class)
				.putExtra(Plps.AVAILABLEBALANCE, 0), 101);
			}
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			dicisNum.setText("");
			finish();
		}
	}
}
