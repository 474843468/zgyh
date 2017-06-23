package com.chinamworld.bocmbci.biz.plps.payment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 信息修改输入页面
 * @author panwe
 *
 */
public class PaymentSignMsgChangeInputActivity extends PlpsBaseActivity{
	private EditText mEditNickName;
	private Spinner mSpinnerAcct;
	private int position;
	/** 原账号  **/
	private String accountNumber;
	/** 新账号  **/
	private String selectAcctNum;
	/**卡名*/
	private String nickName;
	private String customerAlias;
	/**别名*/
	private String editNickName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.plps_payment_msgchange_input);
//		setTitle(PlpsDataCenter.payment[0]);
		setTitle(R.string.plps_payment_info_modify);
		position = getIntent().getIntExtra("p", -1);
		setUpViews();
		initAcctSpinner();
	}
	
	private void setUpViews(){
		Map<String, Object> map = PlpsDataCenter.getInstance().getSignList().get(position);
		customerAlias = (String)map.get(Plps.CUSTOMERALIAS);
		((TextView) findViewById(R.id.agent)).setText((String)map.get(Plps.AGENTNAME));
		((TextView) findViewById(R.id.customerName)).setText((String)map.get(Plps.CUSTNAME));
		((TextView) findViewById(R.id.payuserno)).setText((String)map.get(Plps.PAYUSERNO));
		((TextView) findViewById(R.id.remarks)).setText((String)map.get(Plps.REMARKS));
		((TextView) findViewById(R.id.statue)).setText(PlpsDataCenter.signType.get(map.get(Plps.SIGNTYPE)));
		mEditNickName = (EditText) findViewById(R.id.nickname);
		mSpinnerAcct = (Spinner) findViewById(R.id.signacct);
		mEditNickName.setText(customerAlias);
		accountNumber = (String) map.get(Plps.ACCOUNTNUMBER);
		PlpsUtils.setOnShowAllTextListener(this, (TextView) findViewById(R.id.agent)
				,(TextView) findViewById(R.id.payuserno),(TextView) findViewById(R.id.remarks));
	}
	
	/**
	 * 初始化账户列表
	 */
	private void initAcctSpinner(){
		List<String> mList = PlpsUtils.initSpinnerDataThre(PlpsDataCenter.getInstance().getAcctList(), null,Comm.ACCOUNTNUMBER,Comm.ACCOUNT_TYPE,null,null);
		PlpsUtils.initSpinnerView(this, mSpinnerAcct, mList);
		for (int i = 0; i < PlpsDataCenter.getInstance().getAcctList().size(); i++) {
			if (accountNumber.equals(PlpsDataCenter.getInstance().getAcctList().get(i).get(Comm.ACCOUNTNUMBER))) {
				mSpinnerAcct.setSelection(i);
			}
		}
	}
	
	/**
	 * 确定操作
	 * @param v
	 */
	public void btnConfirmOnclick(View v){	
		editNickName = (String)mEditNickName.getText().toString();
//		if(StringUtil.isNullOrEmpty(editNickName)){
//			BaseDroidApp.getInstanse().showMessageDialog("请输入别名", new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					BaseDroidApp.getInstanse().dismissMessageDialog();
//				}
//			});
//		}else {
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
//		}
		
	}
	/**别名校验*/
//	private boolean nextCheck(){
//		editNickName = (String)mEditNickName.getText().toString();
//		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
//		RegexpBean rePhone = new RegexpBean(getString(R.string.plps_nick_name),editNickName, "editnickname");
//		lists.add(rePhone);
//		if (RegexpUtils.regexpData(lists)) {
//			
//			return true;
//		}
//		return false;
//	}
	
	/**
	 * 取消操作
	 * @param v
	 */
	/*public void btnCancleOnclick(View v){
		finish();
	}
	*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			setResult(RESULT_OK);
			finish();
		}
	}
	
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		pSNGetTokenId();
	}
	
	/**
	 * 请求token
	 */
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
		requestSignInfoChange(tokenId);
	}
	
	/**
	 * 请求信息修改
	 * @param tokenId
	 */
	private void requestSignInfoChange(String tokenId){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.METHODSIGNINFOCHANGE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = PlpsDataCenter.getInstance().getSignList().get(position);
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put(Plps.TOKEN, tokenId);
		param.put(Plps.AGENTCODE, map.get(Plps.AGENTCODE));
		param.put(Plps.AGENTNAME, map.get(Plps.AGENTNAME));
		param.put(Plps.SUBAGENTCODE, map.get(Plps.SUBAGENTCODE));
		param.put(Plps.CSPCODE, map.get(Plps.CSPCODE));
		param.put(Plps.SERVICETYPE, map.get(Plps.SERVICETYPE));
		param.put(Plps.SERVICENAME, map.get(Plps.SERVICENAME));
		param.put(Plps.PAYUSERNO, map.get(Plps.PAYUSERNO));
		param.put(Plps.CUSTNAME, map.get(Plps.CUSTNAME));
		param.put(Plps.REMARKS, map.get(Plps.REMARKS));
		param.put(Plps.CUSTOMERALIAS, editNickName);
		setParams(param);
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "signInfoChangeCallBack");
	}
	
	private void setParams(HashMap<String, Object> map){
		int index = mSpinnerAcct.getSelectedItemPosition();
		Map<String, Object> acctMap = PlpsDataCenter.getInstance().getAcctList().get(index);
		selectAcctNum = (String) acctMap.get(Comm.ACCOUNTNUMBER);
		nickName = (String)acctMap.get(Comm.NICKNAME);
		if (accountNumber.equals(selectAcctNum)) {
			map.put(Plps.UPDATEACCTFLAG, "1");
		}else{
			map.put(Plps.UPDATEACCTFLAG, "0");
		}
		map.put(Plps.ACCOUNTNUMBER, selectAcctNum);
		map.put(Comm.ACCOUNT_ID, acctMap.get(Comm.ACCOUNT_ID));
		
		String accountTypet = (String)acctMap.get(Comm.ACCOUNT_TYPE);
		if(accountTypet.equals("101")||accountTypet.equals("188")){
			map.put(Plps.ACCOUNTTYPE, "1");
		}else if (accountTypet.equals("119")) {
			map.put(Plps.ACCOUNTTYPE, "0");
		}
	}
	
	public void signInfoChangeCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		String result = (String) this.getHttpTools().getResponseResult(resultObj);
		if(StringUtil.isNull(result)){
			startActivityForResult(new Intent(this, PaymentSignMsgChangeResultActivity.class)
			.putExtra(Plps.CUSTOMERALIAS, mEditNickName.getText().toString())
			.putExtra(Plps.ACCOUNTNUMBER, selectAcctNum)
			.putExtra(Comm.NICKNAME, nickName)
			.putExtra("p", position),1001);
		}
	}

}
