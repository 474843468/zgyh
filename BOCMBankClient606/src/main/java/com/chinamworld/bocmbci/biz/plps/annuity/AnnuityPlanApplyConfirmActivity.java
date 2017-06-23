package com.chinamworld.bocmbci.biz.plps.annuity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 变更申请确认页面
 * @author panwe
 *
 */
public class AnnuityPlanApplyConfirmActivity extends PlpsBaseActivity{
	private EditText editPhone;
	private EditText editMobile;
	private EditText editAdress;
	private EditText editPostCode;
	private EditText editEmail;
	private String phone;
	private String mobile;
	private String adress;
	private String postCode;
	private String email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.plps_annuity_apply_confirm);
		setTitle(PlpsDataCenter.annuity[2]);
		setUpView();
	}
	
	private void setUpView(){
		Map<String, Object> map = PlpsDataCenter.getInstance().getAnnuityUserInfo();
		((TextView) findViewById(R.id.planno)).setText((String)map.get(Plps.PLANNO));
		((TextView) findViewById(R.id.username)).setText((String)map.get(Plps.NAME));
		((TextView) findViewById(R.id.idtype)).setText(LocalData.IDENTITYTYPE.get(map.get(Plps.CERTTYPE)));
		((TextView) findViewById(R.id.idnum)).setText((String)map.get(Plps.CERTNO));
		editPhone = (EditText) findViewById(R.id.phone);
		editMobile = (EditText) findViewById(R.id.mobile);
		editAdress = (EditText) findViewById(R.id.adress);
		editPostCode = (EditText) findViewById(R.id.postcode);
		editEmail = (EditText) findViewById(R.id.email);
		editPhone.setText((String)map.get(Plps.PHONE));
		editMobile.setText((String)map.get(Plps.MOBILE));
		editAdress.setText((String)map.get(Plps.ADDRESS));
		editPostCode.setText((String)map.get(Plps.POSTCODE));
		editEmail.setText((String)map.get(Plps.EMAIL));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,(TextView) findViewById(R.id.planno));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,(TextView) findViewById(R.id.idnum));
	}
	
	/**
	 * 确定
	 * @param v
	 */
	public void submitOnclick(View v){
		if (submitCheck()) {
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
		}
	}
	/*
	 * 上一步
	 */
	public void lastOnClick(View v){
		finish();
	}
	/*
	 * 重置
	 */
	public void resetOnClick(View v){
		editPhone.setText("");
		editMobile.setText("");
		editAdress.setText("");
		editPostCode.setText("");
		editEmail.setText("");
	}
	
	/***
	 * 提交校验
	 * @return
	 */
	private boolean submitCheck(){
		phone = editPhone.getText().toString().trim();
		mobile = editMobile.getText().toString().trim();
		adress = editAdress.getText().toString().trim();
		postCode = editPostCode.getText().toString().trim();
		email = editEmail.getText().toString().trim();
		
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		RegexpBean rePhone = new RegexpBean(Plps.TIP_PHONE,phone, Plps.REGEXPHONE);
		lists.add(rePhone);
		RegexpBean reMobile = new RegexpBean(Plps.TIP_MOBILE,mobile, Plps.REGEXMOBILE);
		lists.add(reMobile);
		RegexpBean reAdress = new RegexpBean(Plps.TIP_ADRESS,adress, Plps.REGEXADRESS);
		lists.add(reAdress);
		RegexpBean rePostCode = new RegexpBean(Plps.TIP_POSTCODE,postCode, Plps.REGEXPOST);
		lists.add(rePostCode);
		RegexpBean reEmail = new RegexpBean(Plps.TIP_EMAIL,email, Plps.REGEXEMAIL);
		lists.add(reEmail);
		if (RegexpUtils.regexpDate(lists)) {
			return true;
		}
		return false;
	}
	
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		pSNGetTokenId();
	}
	
	/** 请求token */
	public void pSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenId");
	}
	
	public void aquirePSNGetTokenId(Object resultObj) {
		String tokenId = (String) this.getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog(); return;
		}
		annuityApplySubmit(tokenId);
	}
	
	/**
	 * 变更申请提交
	 * @param tokenId
	 */
	private void annuityApplySubmit(String tokenId){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.METHODANNUITYAPPLYSUBMIT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = PlpsDataCenter.getInstance().getAnnuityUserInfo();
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put(Plps.TOKEN, tokenId);
		param.put(Plps.PLANNO, map.get(Plps.PLANNO));
		param.put(Plps.ANNUITYTELEPHONE, phone);
		param.put(Plps.ANNUITYMOBILE, mobile);
		param.put(Plps.ANNUITYADDRESS, adress);
		param.put(Plps.ANNUITYZIPCODE, postCode);
		param.put(Plps.ANNUITYMAIL, email);
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "applySubmitCallBack");
	}
	
	public void applySubmitCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		startActivityForResult(new Intent(this, AnnuityPlanApplyResultActivity.class)
		.putExtra(Plps.PHONE, phone)
		.putExtra(Plps.MOBILE, mobile)
		.putExtra(Plps.ADDRESS, adress)
		.putExtra(Plps.POSTCODE, postCode)
		.putExtra(Plps.EMAIL, email),1001);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			setResult(RESULT_OK);
			finish();
		}
	}

}
