package com.chinamworld.bocmbci.biz.bond.mybond;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Bond;
import com.chinamworld.bocmbci.biz.bond.BondBaseActivity;
import com.chinamworld.bocmbci.biz.bond.BondDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/***
 * 托管账户注销\查询密码重置 -确认页面
 * 
 * @author panwe
 * 
 */
public class BondAcctManagerConfirmActivity extends BondBaseActivity {

	/** 主布局 */
	private View mainView;
	/** 标识注销/密码重置 */
	private boolean isCanceAcct;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 添加布局
		mainView = (View) LayoutInflater.from(this).inflate(
				R.layout.bond_acctmanager, null);
		addView(mainView);
		//隐藏左侧菜单
		setLeftButtonPopupGone();
		//隐藏底部tab
		setBottomTabGone();
		btnBack.setVisibility(View.GONE);
		setText(this.getString(R.string.close));
		btnRight.setOnClickListener(backClick);
		init();
	}

	private void init() {
		isCanceAcct = getIntent().getBooleanExtra(ISCANCEACCT, false);
		TextView tvTip = (TextView) mainView.findViewById(R.id.tv_bill_tip);
		String title = null;
		String tip = null;
		if (isCanceAcct) {
			title = this.getString(R.string.bond_acct_cance_title);
			tip = this.getString(R.string.bond_acct_cance_tip);
		} else {
			title = this.getString(R.string.bond_acct_pasreset);
			tip = this.getString(R.string.bond_acct_pasreset_tip);
		}
		setTitle(title);
		tvTip.setText(tip);

		LinearLayout layoutBnak = (LinearLayout) mainView.findViewById(R.id.layout_bank);
		TextView tvBondAcct = (TextView) mainView.findViewById(R.id.tv_acc1);
		TextView tvBankAcct = (TextView) mainView.findViewById(R.id.tv_acc2);
		TextView tvIdentityType = (TextView) mainView
				.findViewById(R.id.tv_idtype);
		TextView tvIdentityNum = (TextView) mainView
				.findViewById(R.id.tv_idnum);

		Button btnConfirm = (Button) mainView.findViewById(R.id.btnConfirm);
		btnConfirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 获取会话id
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
		});

		Map<String, Object> customInfoMap = BondDataCenter.getInstance()
				.getCustomInfoMap();
		tvBondAcct
				.setText((String) customInfoMap.get(Bond.BOND_RESULT_BONDACC));
		if (isCanceAcct) {
			tvBankAcct.setText(StringUtil
					.getForSixForString((String) customInfoMap
							.get(Bond.CUSTOMER_BANKNUM)));
		} else {
			layoutBnak.setVisibility(View.GONE);
		}
		tvIdentityType.setText(BondDataCenter.identityType.get(customInfoMap
				.get(Bond.IDENTITYPE)));
		tvIdentityNum.setText((String) customInfoMap.get(Bond.IDENTINUM));
	}

	/** conversationid回调 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {

		super.requestCommConversationIdCallBack(resultObj);
		// 获取token请求
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
	}

	/** token返回 */
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		if (isCanceAcct) {
			//注销操作
			requestBondAcctCanceComit(token);
		}else{
			//重置查询密码
			requestPasResetComit(token);
		}
	}

	/**
	 * 发送注销请求
	 * 
	 * @param token
	 */
	private void requestBondAcctCanceComit(String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bond.METHOD_BONDACCT_CANCE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> customInfo = BondDataCenter.getInstance()
				.getCustomInfoMap();
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put(Bond.BOND_BUYRESULT_TOKEN, token);
		parms.put(Bond.IDENTITYPE, customInfo.get(Bond.IDENTITYPE));
		parms.put(Bond.IDENTINUMBER, customInfo.get(Bond.IDENTINUM));
		biiRequestBody.setParams(parms);
		HttpManager.requestBii(biiRequestBody, this, "bondAcctCanceCallBack");
	}

	/** 注销账户返回处理 */
	public void bondAcctCanceCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Intent it = new Intent(this, BondAcctManagerResultActivity.class);
		it.putExtra(ISCANCEACCT, true);
		startActivity(it);
		finish();
	}

	/**
	 * 发送密码重置请求
	 * 
	 * @param token
	 */
	private void requestPasResetComit(String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bond.METHOD_BONDACCT_PASRESET);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> customInfo = BondDataCenter.getInstance()
				.getCustomInfoMap();
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put(Bond.BOND_BUYRESULT_TOKEN, token);
		parms.put(Bond.IDENTITYPE, customInfo.get(Bond.IDENTITYPE));
		parms.put(Bond.IDENTINUMBER, customInfo.get(Bond.IDENTINUM));
		biiRequestBody.setParams(parms);
		HttpManager.requestBii(biiRequestBody, this, "pasResetCallBack");
	}

	/** 查询密码重置返回处理 */
	public void pasResetCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Intent it = new Intent(this, BondAcctManagerResultActivity.class);
		it.putExtra(ISCANCEACCT, false);
		startActivity(it);
		finish();
	}
}
