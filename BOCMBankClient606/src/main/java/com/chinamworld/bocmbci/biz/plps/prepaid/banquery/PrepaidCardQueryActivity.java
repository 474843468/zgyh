package com.chinamworld.bocmbci.biz.plps.prepaid.banquery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.cfca.mobile.log.CodeException;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cfca.mobile.sip.SipBox;
/*
 * 预付卡余额查询
 * zxj
 */
public class PrepaidCardQueryActivity extends PlpsBaseActivity implements OnItemSelectedListener{
	private int position;
	//预付卡类型
	private Spinner cardType;
	//预付卡号
	private EditText cardNumberText;
	private String cardNumber;
	//密码
//	private EditText passwordText;
	private SipBox passwordSipBox;
	private String passwordNumber;
	//密码控件上传参数
	private String passwordSip;
	private String passwordRandomNum;
	//查询
	private Button queryBtn;
	//重置
	private Button resetBtn;
	//卡类型
	private String merchNo;
	//预付卡单位名称
	private String merchName;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		position = getIntent().getIntExtra("p", -1);
		setTitle(PlpsDataCenter.prepaid[position]);
		init();
	}
	private void init(){
		inflateLayout(R.layout.plps_prepaid_banlance_query);
		cardType = (Spinner)findViewById(R.id.cardtype);
		PlpsUtils.initSpinnerView(this, cardType, PlpsDataCenter.plpsPrepaidCardType);
		cardType.setOnItemSelectedListener(this);
		cardNumberText = (EditText)findViewById(R.id.cardname);
//		passwordText = (EditText)findViewById(R.id.password);
		passwordSipBox = (SipBox)findViewById(R.id.password);
		// add by 2016年9月12日 luqp 修改
		SipBoxUtils.initSipBoxWithTwoType(passwordSipBox, ConstantGloble.MIN_LENGTH,
				ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, PlpsDataCenter.getInstance().getRandom(), this);
//		passwordSipBox.setCipherType(SystemConfig.CIPHERTYPE);
//		passwordSipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		passwordSipBox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		passwordSipBox.setId(10002);
//		passwordSipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		passwordSipBox.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		passwordSipBox.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//		passwordSipBox.setSipDelegator(this);
//		passwordSipBox.setRandomKey_S(PlpsDataCenter.getInstance().getRandom());
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if(position !=0){
//		Map<String, Object> map = PlpsDataCenter.getInstance().getResultCardType().get(position-1);
		merchNo = (String)PlpsDataCenter.plpsPrepaidCardNo.get(position-1);
		merchName = (String)PlpsDataCenter.plpsPrepaidCardType.get(position-1);
		}
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
	/**
	 * 重置*/
	public void resetOnclick(View v){
		passwordSipBox.setFocusable(false);
		cardType.setSelection(0);
		cardNumberText.setText("");
		cardNumberText.setFocusable(true);
		cardNumberText.setFocusableInTouchMode(true);
//		passwordText.setText("");
		passwordSipBox.setText("");
		passwordSipBox.setFocusable(true);
		passwordSipBox.setFocusableInTouchMode(true);
	}
	/**查询*/
	public void queryOnclick(View v){
		if ((cardType.getSelectedItem().toString()).equals(Plps.SP_DEFUALTTXT)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请选择预付卡类型"); return;
		}
		if(submitCheck()){
			BaseHttpEngine.showProgressDialog();
			requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
					.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		}
	}
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestPSNGetTokenIdCallBack(resultObj);
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		requestPsnPrepaidCardQueryBalance(token);
	}
	/**
	 * 查询预付卡余额*/
	private void requestPsnPrepaidCardQueryBalance(String token){
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.PREPARDQUERYBALANCE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Plps.TOKEN, token);
		params.put(Plps.MERCHNO, merchNo);
		params.put(Plps.PREPARDQUERYNUMBER, cardNumberText.getText().toString());
		params.put(Plps.PREPARDQUERYPASSWORD, passwordSip);
		params.put(Plps.PREPARDQUERYPASSWORDRANDOM, passwordRandomNum);
		SipBoxUtils.setSipBoxParams(params);
//		params.put(Plps.PREPARDQUERYPASSWORD, passwordText.getText().toString());
		SipBoxUtils.setSipBoxParams(params);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnPrepaidCardQueryBalanceCallBack");
	}
	public void requestPsnPrepaidCardQueryBalanceCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		//余额
		String cardBalance = (String)result.get(Plps.PREPARDQUERYCARDBALANCE);
		//币种
		String currency = (String)result.get(Plps.PREPARDQUERYCURRENCY);
		//姓名
		String name = (String)result.get(Plps.PREPARDQUERYNAME);
		startActivityForResult(new Intent(PrepaidCardQueryActivity.this, PrepaidCardQueryResultActivity.class)
				.putExtra(Plps.PREPARDQUERYCARDBALANCE, cardBalance)
				.putExtra(Plps.PREPARDQUERYCURRENCY, currency)
				.putExtra(Plps.PREPARDQUERYNAME, name)
				.putExtra(Plps.PREPARDQUERYNUMBER, cardNumberText.getText().toString())
				.putExtra(Plps.MERCHNAME, merchName),1001);
	}
	/**
	 * 提交校验*/
	private Boolean submitCheck(){
		cardNumber = cardNumberText.getText().toString().trim();
//		passwordNumber = passwordText.getText().toString();
		passwordNumber = passwordSipBox.getText().toString();
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		RegexpBean reCardNumber = new RegexpBean(Plps.PREPARDCARDNUMBER,cardNumber, "plpscardCheck");
		lists.add(reCardNumber);
		if(!StringUtil.isNullOrEmpty(passwordNumber)){
			RegexpBean rePasswordNumber = new RegexpBean(Plps.PASSWORD,passwordNumber, "plpspass");
			lists.add(rePasswordNumber);
		}
		if(!StringUtil.isNullOrEmpty(passwordNumber)){
			try {
				passwordSip = passwordSipBox.getValue().getEncryptPassword();
				passwordRandomNum = passwordSipBox.getValue().getEncryptRandomNum();
			} catch (CodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (RegexpUtils.regexpDate(lists)) {
			return true;
		}
		return false;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			setResult(RESULT_OK);
//			cardType.setSelection(0);
//			cardNumberText.setText("");
//			passwordText.setText("");
			finish();
		}
	}

}
