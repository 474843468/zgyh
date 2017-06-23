package com.chinamworld.bocmbci.biz.dept.largecd.sign;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 大额存单签约 协议页面 确认页面
 * @author luqp 2016年1月14日15:27:40
 */
public class LargeSignAgreementConfirmActivity extends DeptBaseActivity {
	private Context context = this;
	/** 加载布局 */
	private LayoutInflater inflater = null;
	private LinearLayout tabcontent;// 主Activity显示
	private View view = null;
	/** 选择的签约账户 */
	private Map<String, Object> selectedAccount;
	/** 卡类型 */
	private TextView accType = null;
	/** 卡别名 */
	private TextView accNum = null;
	/** 确认按钮 */
	private Button confirm = null;
	/** 账户类型 */
	private String accountType = null;
	/** 账户类型 */
	private String accountNumber = null;
	/** 账户ID */
	private String accountId = null;
	private String conversationId;
	private String token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.large_cd_sign_title)); // 为界面标题赋值
		inflater = LayoutInflater.from(this);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		LayoutInflater inflater = LayoutInflater.from(this); // 加载布局
		view = inflater.inflate(R.layout.large_sign_confirm, null);
		tabcontent.addView(view);
		setLeftSelectedPosition("deptStorageCash_4");
		selectedAccount = DeptDataCenter.getInstance().getLargeSignSelectListMap();

		init(); // 初始化所有控件
	}

	/** 初始化view和控件 */
	private void init() {
		accType = (TextView) view.findViewById(R.id.acc_account_type);
		accNum = (TextView) view.findViewById(R.id.acc_account_num);
		confirm = (Button) view.findViewById(R.id.btn_confirm);

		accountType = (String) selectedAccount.get(Dept.LargeSign_accountType);
		accountNumber = (String) selectedAccount.get(Dept.LargeSign_accountNumber);
		accType.setText(LocalData.AccountType.get(accountType));
		accNum.setText(StringUtil.getForSixForString(accountNumber));
		
		accountId = (String) selectedAccount.get(Dept.LargeSign_accountId);
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog(); // 提交签约
				requestCommConversationId();
			}
		});
	}
	
	public void requestCommConversationIdCallBack(Object resultObj){
		super.requestCommConversationIdCallBack(resultObj);
		conversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		requestPSNGetTokenId(conversationId);
	}
	
	
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		token = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		requestPsnLargeCDSignSubmit(accountId,token,conversationId);	
	}
	
	/** 大额存单签约交易 返回------
	 * @param resultObj
	 */
	public void requestPsnLargeCDSignSubmitCallBack(Object resultObj) {
		super.requestPsnLargeCDSignSubmitCallBack(resultObj);
		// 取消通讯框
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultList = (Map<String, Object>) biiResponseBody.getResult();
		DeptDataCenter.getInstance().setSignedAcc(selectedAccount); // 将签约的账户放入集合中
		Intent intent = new Intent();
		intent.setClass(context, LargeSignAgreementCompleteActivity.class);
		startActivity(intent);
	}
}