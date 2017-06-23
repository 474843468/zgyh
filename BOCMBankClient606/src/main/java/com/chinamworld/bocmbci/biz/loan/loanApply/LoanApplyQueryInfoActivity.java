package com.chinamworld.bocmbci.biz.loan.loanApply;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 进度查询填写页面 */
public class LoanApplyQueryInfoActivity extends LoanBaseActivity {
	private static final String TAG = "LoanQuotaQueryDetailActivity";
	private View detailView = null;
	private int position = -1;
	private List<Map<String, String>> loanApplyList = null;

	private Button nextButton = null;
	// 定义输入框的名称
	private EditText infoName, infoIphone, infoEmail;
	// 用户输入的资料
	private String infoNames, infoIphones, infoEmails;
   // commConversationId
	private String commConversationId;
	//条目总数
	private String  totalCounts;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.loan_apply_three_new));
//		Button backButton = (Button) findViewById(R.id.ib_back);
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			Intent intent=new Intent(LoanApplyQueryInfoActivity.this, LoanApplyMenuActivity.class);
			startActivity(intent);
				finish();
			}
		});

		init();

	}

	private void init() {
		detailView = LayoutInflater.from(this).inflate(
				R.layout.loan_apply_query_info, null);
		tabcontentView.addView(detailView);
		TextView name_left=(TextView) findViewById(R.id.name_left);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, name_left);	
		infoName = (EditText) findViewById(R.id.et_loan_apply_name_value);
		//对申请人姓名 的输入进行限制
		EditTextUtils.setLengthMatcher(LoanApplyQueryInfoActivity.this,
				infoName, 60);
		infoIphone = (EditText) findViewById(R.id.et_loan_apply_iphone_value);
		infoEmail = (EditText) findViewById(R.id.et_loan_apply_email_value);
		nextButton = (Button) findViewById(R.id.loan_tradeButton);
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getValue();
				if(StringUtil.isNull(infoNames)){
					BaseDroidApp.getInstanse().showInfoMessageDialog("请输入申请人姓名/企业名称。");
					return;
				}
				if(StringUtil.isNull(infoIphones)&&StringUtil.isNull(infoEmails)){
					BaseDroidApp.getInstanse().showInfoMessageDialog("请输入联系电话或Emali地址。");
					return;
				}
				  //手机号码正则校验
				if(!StringUtil.isNull(infoIphones)){
//					RegexpBean regmobileNo = null;
//					regmobileNo = new RegexpBean("手机号码", infoIphones, "phone");
//					ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
//					lists.add(regmobileNo);
//					if (!RegexpUtils.regexpDate(lists)) {// 校验不通过
//						return;
//					}
		           if(infoIphones.matches(".*\\d+.*")){
						
					}else{
						BaseDroidApp.getInstanse().showInfoMessageDialog("输入正确的联系电话。");
						return;
						
					}
				}
				//邮箱正则校验
				if (!StringUtil.isNull(infoEmails)) {
					RegexpBean regEmail = null;
					regEmail = new RegexpBean("电子邮箱", infoEmails, "loanapplyemail");
					ArrayList<RegexpBean> list = new ArrayList<RegexpBean>();
					list.add(regEmail);
					if (!RegexpUtils.regexpDate(list)) {// 校验不通过
						return;
					}
				}
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
				
			}
		});
	}

	/** 获得用户输入的信息 */
	private void getValue() {
		infoNames = infoName.getText().toString();
		infoIphones = infoIphone.getText().toString();
		infoEmails = infoEmail.getText().toString().trim();
		
	}
	
	/**
	 * 请求conversationId 出来登录之外的conversationId
	 */
	public void requestCommConversationId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.CONVERSATION_ID_API);
		HttpManager.requestBii(biiRequestBody, this, "requestCommConversationIdCallBack");
	}

	/**
	 * 请求CommconversationId返回
	 * 
	 * @param resultObj
	 */
	public void requestCommConversationIdCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		 commConversationId = (String) biiResponseBody.getResult();
		requestOnLineLoanAppliedQry(infoNames, infoIphones, infoEmails,commConversationId);
	}

	/** 贷款申请---查询贷款记录列表 */
	public void requestOnLineLoanAppliedQry(String name, String appPhone,
			String AppEmail,String conversationId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_PSNONLINELOANAPPLIEDQRY_API);
		biiRequestBody.setConversationId(conversationId);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Loan.LOAN_APPLY_NAME_API, name);
		if(!StringUtil.isNullOrEmpty(appPhone)){
			map.put(Loan.LOAN_APPLY_APPPHONE_API, appPhone);
		}
		if(!StringUtil.isNullOrEmpty(AppEmail)){
			map.put(Loan.LOAN_APPLY_APPEMAIL_API, AppEmail);
		}
		map.put(Loan.LOAN_APPLY_PAGESIZE_API, "10");
		map.put(Loan.LOAN_APPLY_CURENTINDEX_API, "1");
		map.put(Loan.LOAN_APPLY_REFRESH_API, "true");
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestOnLineLoanAppliedQryCallback");
	}

	public void requestOnLineLoanAppliedQryCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap=  (Map<String, Object>) biiResponseBody.getResult();
		List<Map<String, Object>> resultList=new ArrayList<Map<String,Object>>();
	    totalCounts=	(String) resultMap.get(Loan.LOAN_APPLY_RECORDNUMBER_QRY);
		resultList=(List<Map<String, Object>>)resultMap.get("list");
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(
							R.string.loan_apply_LineLoanApplied_error));
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.LOAN_RESULT, resultList);
		BaseHttpEngine.dissMissProgressDialog();
		gotoActivity();
	}

	private void gotoActivity() {
		
		Intent intent = new Intent(this, LoanApplyQueryChooseActivity.class);
		intent.putExtra("commConversationId", commConversationId);
		intent.putExtra(ConstantGloble.APPLY_TOTALCOUNTS, totalCounts);
		intent.putExtra(ConstantGloble.APPLY_INFONAMES, infoNames);
		intent.putExtra(ConstantGloble.APPLY_INFOIPHONES, infoIphones);
		intent.putExtra(ConstantGloble.APPLY_INFOEMAILS, infoEmails);
		startActivity(intent);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			Intent intent=new Intent(LoanApplyQueryInfoActivity.this, LoanApplyMenuActivity.class);
			startActivity(intent);
			finish();  
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
