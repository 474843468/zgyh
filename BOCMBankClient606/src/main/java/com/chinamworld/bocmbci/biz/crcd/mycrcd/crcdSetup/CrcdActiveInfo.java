package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.BusinessModelControl;
import com.chinamworld.bocmbci.biz.MainActivity;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.GetPhoneInfo;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 信用卡激活
 * 
 * @author huangyuchao
 * 
 */
public class CrcdActiveInfo extends CrcdBaseActivity {
	private static final String TAG = "CrcdActiveInfo";
	/** 信用卡激活 */
	private View view;

	Button nextBtn;

	EditText tv_cardNumber;
	String passWordIsSet;
	private View hasCrcdView = null;
	private View noCrcdView = null;
	private Button glButton = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_creditcard_active_title));
		if (view == null) {
			view = addView(R.layout.crcd_active_info);
		}
		hasCrcdView = findViewById(R.id.has_crcd_layout);
		noCrcdView = findViewById(R.id.no_crcd_layout);
		btn_right.setVisibility(View.GONE);
		back = (Button) findViewById(R.id.ib_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(CrcdActiveInfo.this, MainActivity.class);
//				startActivity(intent);
//				goToMainActivity();
				ActivityTaskManager.getInstance().removeAllActivity();
				finish();
			}
		});
		
		hasDate();
//		iscomformFootFastOrOther();

	}

	

	/** 用于是否从快捷键 */
	private void iscomformFootFastOrOther() {
		if (getIntent().getBooleanExtra(ConstantGloble.COMEFROMFOOTFAST, false)) {
			// 快捷键启动
			// 查询信用卡
			BaseHttpEngine.showProgressDialogCanGoBack();
			requestCommonQueryAllChinaBankAccount();
		} else {
			hasDate();
		}
	}
	
	/** 有信用卡数据 */
	private void hasDate() {
		
		hasCrcdView.setVisibility(View.VISIBLE);
		noCrcdView.setVisibility(View.GONE);
		init();
	}
	
	@Override
	public void requestCommonQueryAllChinaBankAccountCallBack(Object resultObj) {
		super.requestCommonQueryAllChinaBankAccountCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		List<Map<String, Object>> returnList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CRCD_CRCDACCOUNTRETURNLIST);

		if (StringUtil.isNullOrEmpty(returnList)) {

			hasCrcdView.setVisibility(View.GONE);
			noCrcdView.setVisibility(View.VISIBLE);
			setLeftButtonPopupGone();
			glButton = (Button) findViewById(R.id.btn_description);
			glButton.setVisibility(View.GONE); //屏蔽自助关联
			glButton.setOnClickListener(glOnClick);
		} else {
			hasDate();
		}
	}

	/** 关联信用卡 */
	private OnClickListener glOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
//			startActivityForResult((new Intent(CrcdActiveInfo.this, AccInputRelevanceAccountActivity.class)),
//					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
			BusinessModelControl.gotoAccRelevanceAccount(CrcdActiveInfo.this, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);
		}
	};
	public void init() {

		tv_cardNumber = (EditText) findViewById(R.id.tv_cardNumber);
		nextBtn = (Button) findViewById(R.id.nextButton);
		nextBtn.setOnClickListener(nextClick);
	}

	static String cardNumber;

	OnClickListener nextClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			cardNumber = tv_cardNumber.getText().toString();
			// 验证
			RegexpBean reb1 = new RegexpBean(getString(R.string.crcd_creditcard_number), cardNumber, Crcd.CREDITCARD);
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(reb1);
			if (RegexpUtils.regexpDate(lists)) {
				// 信用卡激活预交易
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
				
			}
		}
	};

	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);

		// 请求安全因子组合id
		requestGetSecurityFactor(psnActivesecurityId);

	};

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestGetSecurityFactorCallBack(resultObj);
		ArrayList<String> securityIdList = BaseDroidApp.getInstanse().getSecurityIdList();
		ArrayList<String> securityNameList = BaseDroidApp.getInstanse().getSecurityNameList();
		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
			@Override
			public void onClick(View v) {						
				psnCrcdActivatePre();
			}
		}, securityIdList, securityNameList);

//		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
//			@Override
//			public void onClick(View v) {						
//				psnCrcdActivatePre();
//			}
//		});
	}

	public void psnCrcdActivatePre() {
		// 通讯开始,展示通讯框
		 BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDACTIVATEPRE);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_TOACTIVEACT, cardNumber);
		params.put(Crcd.CRCD_COMBINID, BaseDroidApp.getInstanse().getSecurityChoosed());
		GetPhoneInfo.initActFirst(this);
		GetPhoneInfo.addPhoneInfo(params);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdActivatePreCallBack");
	}

	static Map<String, Object> resultMap;

	public void psnCrcdActivatePreCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		resultMap = (Map<String, Object>) biiResponseBody.getResult();
//		BaseHttpEngine.dissMissProgressDialog();
	// 查询是否设置查询密码
		requestPsnQueryPassWordIsSet();	
		
		
//		resultMap = (Map<String, Object>) biiResponseBody.getResult();
//		BaseHttpEngine.dissMissProgressDialog();
//		Intent it = new Intent(this, CrcdActiveConfirm.class);
//		// startActivity(it);
//		startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
	}

	public void requestPsnQueryPassWordIsSet() {
//		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setMethod(Crcd.CRCD_PSNQUERYPASSWORDISSET);		
//		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnQueryPassWordIsSetCallBack");	
	}

	public void requestPsnQueryPassWordIsSetCallBack(Object resultObj){
		// 通讯开始,展示通讯框
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		//0-未设置
//		1-设置

		passWordIsSet=(String)resultMap.get("passWordIsSet");
		if("0".equals(passWordIsSet)){
			Intent it = new Intent(this, CrcdSetConsumePWPreActivity.class);
			it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, cardNumber);
			it.putExtra("fromActive", true);	
			startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
			
		}else{
			Intent it = new Intent(this, CrcdActiveConfirm.class);
			startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);	
		}
		
		
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {		
		case 100:

			Intent it = new Intent(this, CrcdActiveConfirm.class);
			it.putExtra("passWordIsSet", passWordIsSet);//						
			it.putExtra(Crcd.CRCD_QUERYPW_RES, data.getStringExtra(Crcd.CRCD_QUERYPW_RES));//
			it.putExtra(Crcd.CRCD_QUERYPW_RC_RES, data.getStringExtra(Crcd.CRCD_QUERYPW_RC_RES));//
			it.putExtra(Crcd.CRCD_QUERYPWCONFIRM_RES, data.getStringExtra(Crcd.CRCD_QUERYPWCONFIRM_RES));//
			it.putExtra(Crcd.CRCD_QUERYPWCONFIRM_RC_RES,data.getStringExtra(Crcd.CRCD_QUERYPWCONFIRM_RC_RES));//
			it.putExtra("randomNumber",data.getStringExtra("randomNumber"));//
			
			startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
			break;
		case RESULT_OK:
			tv_cardNumber.setText("");
			break;

		default:
			break;
		}
	}
}
