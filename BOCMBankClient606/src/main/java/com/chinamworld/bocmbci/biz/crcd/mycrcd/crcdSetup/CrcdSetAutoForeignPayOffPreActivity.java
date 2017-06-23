package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 我的信用卡 外币账单自动购汇  预交易界面
 * 
 * @author sunh
 * 
 */
public class CrcdSetAutoForeignPayOffPreActivity extends CrcdBaseActivity{

private View view;
	
	/** 账户ID accountId */
	private String accountId=null;
	/** 信用卡卡号 */
	private String accountNumber=null;
	/** 开通标示   “1000”： 开通“0000”： 关闭*/
	private String openFlag=null;
	
	TextView card_accountNumber;	
	TextView card_foreignpayoffstatus_txt;
	TextView  card_foreignpayoffstatus_openflag;
	Button sureButton;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	
		view = addView(R.layout.mycrcd_setautoforeignpayoff_layout);

		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		openFlag= getIntent().getStringExtra(Crcd.CRCD_GOUHUIOPENFLAG_RES);
		
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		BaseHttpEngine.showProgressDialog();
		// 请求conversationId
				requestCommConversationId();
		
		
		
	}
	/**
	 * 请求conversationId 回调
	 * 
	 * @param resultObj 返回结果
	 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		
		BaseHttpEngine.dissMissProgressDialog();
		init();
		
		
	}
	
	
	
	private void init() {
		
		card_accountNumber=(TextView)view.findViewById(R.id.card_accountNumber);
		card_accountNumber.setText(StringUtil.getForSixForString(accountNumber));
		card_foreignpayoffstatus_txt = (TextView) view
				.findViewById(R.id.card_foreignpayoffstatus_txt);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				CrcdSetAutoForeignPayOffPreActivity.this, card_foreignpayoffstatus_txt);
		
		card_foreignpayoffstatus_openflag=(TextView) view
		.findViewById(R.id.card_foreignpayoffstatus_openflag);
		if(openFlag.equals("1000")){
		card_foreignpayoffstatus_openflag.setText("开通");
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_setautoforeignpayoff_open));
		}else{
			card_foreignpayoffstatus_openflag.setText("关闭");	
			// 为界面标题赋值
			setTitle(this.getString(R.string.mycrcd_setautoforeignpayoff_close));
		}
		
		sureButton=(Button)view.findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				requestGetSecurityFactor(psnGouhuisecurityId);	
			}
			
			
		});
		
	}
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);

		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//信用卡信用卡外币账单自动购汇设置预交易
				requestPsnCrcdSetAutoForeignPayOffPre();
				
			}
		});
	}
	
	protected void requestPsnCrcdSetAutoForeignPayOffPre() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDSETAUTOFOREGINPAYOFFPRE);
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);// 获取conversationId
		Map<String, String> map = new HashMap<String, String>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, accountId);//账户ID
//		map.put(Crcd.CRCD_TOACTIVEACT, accountNumber);//卡号
		map.put(Crcd.CRCD_GOUHUIOPENFLAG_RES, openFlag);//开通标识		
		map.put(Crcd.CRCD_COMBINID, BaseDroidApp.getInstanse().getSecurityChoosed());//安全因子组合id
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "PsnCrcdSetAutoForeignPayOffPreCallBack");
	
	}

	 Map<String, Object> returnList;

	public void PsnCrcdSetAutoForeignPayOffPreCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);		
		returnList = (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnList)) {
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(Crcd.CRCD_PSNCRCDSETAUTOFOREGINPAYOFFPRE, returnList);
		Intent it = new Intent(CrcdSetAutoForeignPayOffPreActivity.this, CrcdSetAutoForeignPayOffConfirmActivity.class);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		it.putExtra(Crcd.CRCD_GOUHUIOPENFLAG_RES, openFlag);
		startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		BaseDroidApp.getInstanse().setCurrentAct(this);
		switch (resultCode) {
		case RESULT_OK:// 成功
			setResult(RESULT_OK);
			finish();
				break;
			default:
				break;


		case RESULT_CANCELED:// 失败
			break;
		}
	}
}
