package com.chinamworld.bocmbci.biz.plps.payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 签约信息输入页
 * @author panwe
 *
 */
public class PaymentSignInputActivity extends PlpsBaseActivity{
	
	/** ScrollView 用来处理备注控件事件冲突 */
	private ScrollView mScrollView;
	/** 户名 **/
	private EditText mEditCustName;
	/** 手机号  **/
	private EditText mEditPhone;
	/** 缴费用户号  **/
	private EditText mEditPayUserNo;
	/** 签约账号  **/
	private Spinner mSpSignAcct;
	/** 别名  **/
	private EditText mEditNickName;
	/** 备注  **/
	private EditText mEditRemark;
	private CheckBox mCheckBox;
	private int flag;
	private String phoneNumber;
	private String agentName;
	private String agentCode;
	private String serviceName;
	private String payUserNo;
	private String custName;
	private String customerAlias;
	private String remarks;
	private String subAgentCode;
	private String cspCode;
	private String serviceType;
	private String accountType;
	private String acctNumber;
	private String nickName;
	private String accountIbkNum;
	private String acctId;
	/** 加密类型   */
	private String otp;
	private String smc;

	private Intent intentData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.plps_payment_sign_input);
		setTitle(PlpsDataCenter.payment[1]);
		getIntentData();
		initViews();
		initAcctSpinner();
		setViews();
	}
	
	private void getIntentData(){
		Intent it = getIntent();
		intentData = it;
		flag = it.getIntExtra("flag",0);
		phoneNumber = it.getStringExtra(Plps.PHONENUMBER);
		agentName = it.getStringExtra(Plps.AGENTNAME);
		agentCode = it.getStringExtra(Plps.AGENTCODE);
		serviceName = it.getStringExtra(Plps.SERVICENAME);
		payUserNo = it.getStringExtra(Plps.PAYUSERNO);
		custName = it.getStringExtra(Plps.CUSTNAME);
		subAgentCode = it.getStringExtra(Plps.SUBAGENTCODE);
		cspCode = it.getStringExtra(Plps.CSPCODE);
		serviceType = it.getStringExtra(Plps.SERVICETYPE);
	}
	
	private void initViews(){
		mScrollView = (ScrollView) findViewById(R.id.scrollView);
		mEditCustName = (EditText) findViewById(R.id.custname);
		EditTextUtils.setLengthMatcher(this, mEditCustName, 40);
		mEditPhone = (EditText) findViewById(R.id.phone);
		mEditPayUserNo = (EditText) findViewById(R.id.payuserno);
		EditTextUtils.setLengthMatcher(this, mEditPayUserNo, 20);
		mEditNickName = (EditText) findViewById(R.id.nickname);
		EditTextUtils.setLengthMatcher(this, mEditNickName, 20);
		mEditRemark = (EditText) findViewById(R.id.remark);
		EditTextUtils.setLengthMatcher(this, mEditRemark,
				100);
		mSpSignAcct = (Spinner) findViewById(R.id.signacct);
		mCheckBox = (CheckBox) findViewById(R.id.cbprotocol);
		TextView tvProtocol = (TextView) findViewById(R.id.tvprotocol);
		tvProtocol.setText(getClickableSpan(tvProtocol));
		tvProtocol.setMovementMethod(LinkMovementMethod.getInstance());
		
		//处理事件冲突
		mEditRemark.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mScrollView.requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
	}
	
	private void setViews(){
		 	//短信诚邀
		if (flag==1) {
			mEditCustName.setFocusable(false);
			mEditPayUserNo.setFocusable(false);
			mEditPhone.setFocusable(false);
			mEditCustName.setBackgroundResource(0);
			mEditPayUserNo.setBackgroundResource(0);
			mEditPhone.setBackgroundResource(0);
			mEditPayUserNo.setText(payUserNo);
			mEditCustName.setText(custName);
			mEditPhone.setText(phoneNumber);
			((TextView)findViewById(R.id.plps_payment_infoth)).setVisibility(View.GONE);
			//首次签约
		}else if(flag==2){
			((TextView)findViewById(R.id.plps_payment_infoth)).setText(intentData.getStringExtra(Plps.SIGNTIP));
			//再次签约
		}else if(flag==3){
			((TextView)findViewById(R.id.plps_payment_infoth)).setText(intentData.getStringExtra(Plps.SIGNTIP));
			mEditPhone.setFocusable(false);
			mEditPhone.setBackgroundResource(0);
			mEditPhone.setText(phoneNumber);
		}
	}
	
	private void initAcctSpinner(){
//		List<String> mList = PlpsUtils.initSpinnerData(PlpsDataCenter.getInstance().getAcctList(), Comm.ACCOUNTNUMBER,null);
		List<String> mList = PlpsUtils.initSpinnerDataThre(PlpsDataCenter.getInstance().getAcctList(), Comm.NICKNAME,Comm.ACCOUNTNUMBER,Comm.ACCOUNT_TYPE,Comm.ACCOUNTIBKNUM,"请选择");
		PlpsUtils.initSpinnerView(this, mSpSignAcct, mList);
	}
	
	/**
	 * 下一步
	 * @param v
	 */
	public void btnNextOnclick(View v){
		//户名
		custName = mEditCustName.getText().toString().trim();
		//缴费用户号
		payUserNo = mEditPayUserNo.getText().toString().trim();
		if(StringUtil.isNullOrEmpty(custName)){
			BaseDroidApp.getInstanse().showMessageDialog("请输入户名", new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					BaseDroidApp.getInstanse().dismissMessageDialog();
					
				}
			});
			return;
		}
		if(StringUtil.isNullOrEmpty(payUserNo)){
			BaseDroidApp.getInstanse().showMessageDialog("请输入缴费用户号", new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					BaseDroidApp.getInstanse().dismissMessageDialog();
					
				}
			});
			return;
		}
		if (NextCheck()) {
			int position = mSpSignAcct.getSelectedItemPosition();
			if(position==0){
				BaseDroidApp.getInstanse().showMessageDialog("请选择签约帐号", new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						BaseDroidApp.getInstanse().dismissMessageDialog();
					}
				});
				return;
			}else {
				accountType = (String)PlpsDataCenter.getInstance().getAcctList().get(position-1).get(Comm.ACCOUNT_TYPE);
				acctNumber = (String) PlpsDataCenter.getInstance().getAcctList().get(position-1).get(Comm.ACCOUNTNUMBER);
				nickName = (String)PlpsDataCenter.getInstance().getAcctList().get(position-1).get(Comm.NICKNAME);
				accountIbkNum = PlpsDataCenter.Province.get((String)PlpsDataCenter.getInstance().getAcctList().get(position-1).get(Comm.ACCOUNTIBKNUM));
				acctId = (String) PlpsDataCenter.getInstance().getAcctList().get(position-1).get(Comm.ACCOUNT_ID);
			}
			if (!mCheckBox.isChecked()) {
//				return false;
				BaseDroidApp.getInstanse().showMessageDialog("请勾选协议", new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						BaseDroidApp.getInstanse().dismissMessageDialog();		
						
					}
				});
				return;
			}
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
		}
	}
	/**
	 * 上一步*/
	public void btnLastOnclick(View v){
		setResult(RESULT_OK);
		finish();
	}
	
	/**
	 * 下一步校验
	 * @return
	 */
	private boolean NextCheck(){
		
		//手机号
		phoneNumber = mEditPhone.getText().toString().trim();
		//别名
		customerAlias = mEditNickName.getText().toString().trim();
		//备注
		remarks = mEditRemark.getText().toString().trim();
		
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		RegexpBean reCustName = new RegexpBean(getString(R.string.plps_cust_name), custName,"custname");
		lists.add(reCustName);
		RegexpBean rePhone = new RegexpBean(getString(R.string.trans_remit_phone_nolabel),phoneNumber, "mobile");
		lists.add(rePhone);
		
		RegexpBean repayUserNo = new RegexpBean(getString(R.string.plps_payuser_no), payUserNo, "payuserno");
		lists.add(repayUserNo);
		if(!StringUtil.isNullOrEmpty(customerAlias)){
			RegexpBean customerAliasRegex = new RegexpBean(getString(R.string.plps_custom_name), customerAlias, "payuserno");
			lists.add(customerAliasRegex);
		}
		if(!StringUtil.isNullOrEmpty(remarks)){
			RegexpBean remarksEdit = new RegexpBean(getString(R.string.plps_remarks_name), remarks, "remarksname");
			lists.add(remarksEdit);
		}
		if (RegexpUtils.regexpDate(lists)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 添加文字超链接
	 * @param tv
	 * @return
	 */
	private SpannableString getClickableSpan(final TextView tv) {
		String text = getString(R.string.plps_sign_protocol);
		final SpannableString sp = new SpannableString(text);
		sp.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				Intent intent = new Intent(PaymentSignInputActivity.this, PaymentSignProtocolActivity.class);
				String isCheck = String.valueOf(mCheckBox.isChecked());
				intent.putExtra("check", isCheck);
				PaymentSignInputActivity.this.startActivityForResult(intent, 1002);
			}
		}, 14, 18, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		sp.setSpan(new ForegroundColorSpan(Color.BLUE), 14, 18,
				Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		return sp;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Plps.PROTOCOL) {
			mCheckBox.setChecked(true); return;
		}else {
			mCheckBox.setChecked(false);
		}
		if (resultCode == RESULT_OK) {
			setResult(RESULT_OK);
			finish();
		}
		if(resultCode == 10){
			mCheckBox.setChecked(true);
		}
	}
	
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestGetSecurityFactor(Plps.SERVICECODE);
	}
	
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BaseDroidApp.getInstanse().showSeurityChooseDialog(
			new OnClickListener() {
				@Override
				public void onClick(View v) {
					BaseHttpEngine.showProgressDialog();
					requestSignConfirm();
				}
			});
	}
	
	/**
	 * 签约预交易
	 */
	private void requestSignConfirm() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.METHODSIGNCONFIRM);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(Plps.COMBINID, BaseDroidApp.getInstanse().getSecurityChoosed());
		param.put(Plps.PHONENUMBER, phoneNumber);
		param.put(Plps.AGENTNAME, agentName);
		param.put(Plps.SERVICENAME, serviceName);
		param.put(Plps.PAYUSERNO, payUserNo);
		param.put(Plps.CUSTNAME, custName);
		param.put(Plps.CUSTOMERALIAS, customerAlias);
		param.put(Plps.REMARKS, remarks);
		param.put(Comm.ACCOUNT_ID, acctId);
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "bondBuyConfirmCallBack");
	}

	@SuppressWarnings("unchecked")
	public void bondBuyConfirmCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();return;
		}
		PlpsDataCenter.getInstance().setResultObj(result);
//		getCFCtype(result);
		requestForRandomNumber();
	}
	
	/**
	 * 解析加密类型数据
	 * @param resultData
	 */
	@SuppressWarnings("unchecked")
	private void getCFCtype(Map<String, Object> resultData) {
		List<Map<String, Object>> sipList = (List<Map<String, Object>>)resultData.get(Plps.CFCFACTORLIST);
		for (int i = 0; i < sipList.size(); i++) {
			Map<String, Object> map = (Map<String, Object>) sipList.get(i).get(Plps.CFCFIELD);
			String sipType = (String) map.get(Plps.CFCNAME);
			if (sipType.equals(Comm.Otp)) {
				otp = sipType;
			} else if (sipType.equals(Comm.Smc)) {
				smc = sipType;
			}
		}
		if (StringUtil.isNull(otp) && StringUtil.isNull(smc)) {
			BaseHttpEngine.dissMissProgressDialog(); return;
		}
		requestForRandomNumber();
	}
	
	/**
	 * 请求随机数
	 */
	private void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this,"queryRandomNumberCallBack");
	}
	
	public void queryRandomNumberCallBack(Object resultObj) {
		String randomNumber = HttpTools.getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNull(randomNumber)) return;
		startActivityForResult(new Intent(this, PaymentSignConfirmActivity.class)
//		.putExtra(Comm.Otp, otp)
//		.putExtra(Comm.Smc, smc)
		.putExtra(Plps.RANDOMNUMBER, randomNumber)
		.putExtra("flag", flag)
		.putExtra(Plps.PHONENUMBER, phoneNumber)
		.putExtra(Plps.AGENTNAME, agentName)
		.putExtra(Plps.AGENTCODE, agentCode)
		.putExtra(Plps.SUBAGENTCODE, subAgentCode)
		.putExtra(Plps.CSPCODE, cspCode)
		.putExtra(Plps.SERVICETYPE, serviceType)
		.putExtra(Comm.ACCOUNT_TYPE, accountType)
		.putExtra(Comm.ACCOUNTNUMBER, acctNumber)
		.putExtra(Comm.NICKNAME, nickName)
		.putExtra(Comm.ACCOUNTIBKNUM, accountIbkNum)
		.putExtra(Comm.ACCOUNT_ID, acctId)
		.putExtra(Plps.SERVICENAME, serviceName)
		.putExtra(Plps.PAYUSERNO, payUserNo)
		.putExtra(Plps.CUSTNAME, custName)
		.putExtra(Plps.REMARKS, remarks)
		.putExtra(Plps.CUSTOMERALIAS, customerAlias),1001);
	}

}
