package com.chinamworld.bocmbci.biz.bond.acct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Bond;
import com.chinamworld.bocmbci.biz.bond.BondBaseActivity;
import com.chinamworld.bocmbci.biz.bond.BondConstant;
import com.chinamworld.bocmbci.biz.bond.BondDataCenter;
import com.chinamworld.bocmbci.biz.bond.adapter.BankAcctAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 资金账户列表
 * 
 * @author panwe
 * 
 */
public class BankAcctListActivity extends BondBaseActivity implements
		OnClickListener {
	/** 主布局 */
	private View viewContent;
	/** 卡列表 */
	private ListView lvBank;
	/** 确定按钮 */
	private Button btnOk;
	/** 列表选中条目 */
	private int selectposition = -1;
	private BankAcctAdapter mAdapter;
	/** 标识开户/登记 */
	private boolean isOpen;
	/** 是否买入 */
	private boolean isBuy;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 添加布局
		viewContent = (View) LayoutInflater.from(this).inflate(
				R.layout.bond_bankacct_list, null);
		addView(viewContent);
		BondDataCenter.getInstance().addActivity(this);
		//隐藏左侧菜单
		setLeftButtonPopupGone();
		//隐藏底部tab
		setBottomTabGone();
		setText(this.getString(R.string.close));
		if (BondDataCenter.getInstance().isResetup()) {
			btnRight.setOnClickListener(backClick);
		}else{btnRight.setOnClickListener(coloseBtnClick);}
		init();
	}

	private void init() {
		isOpen = getIntent().getBooleanExtra(ISOPEN, false);
		isBuy = getIntent().getBooleanExtra(ISBUY, false);
		TextView textTip = (TextView) viewContent.findViewById(R.id.text_tip);
		btnOk = (Button) viewContent.findViewById(R.id.btnconfirm);
		btnOk.setOnClickListener(this);
		String title = null;
		String tip = null;
		if (isOpen) {
			title = this.getString(R.string.bond_acct_open_title);
			tip = this.getString(R.string.bond_acctopen_banklist_tip);
		} else {
			btnBack.setVisibility(View.GONE);
			btnOk.setText(this.getString(R.string.confirm));
			title = this.getString(R.string.bond_acct_setup_title);
			tip = this.getString(R.string.bond_acctsetup_banklist_tip);
		}
		setTitle(title);
		textTip.setText(tip);

		lvBank = (ListView) viewContent.findViewById(R.id.cardlist);
		lvBank.setOnItemClickListener(cardItemClick);
		mAdapter = new BankAcctAdapter(this, BondDataCenter.getInstance()
				.getBankAccList());
		lvBank.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (selectposition > -1) {
			// 获取会话id
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.bond_banklist_commontip));
		}
	}

	/** 列表点击事件 **/
	private OnItemClickListener cardItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if (selectposition == position) {
				return;
			} else {
				selectposition = position;
				mAdapter.setSelectedPosition(selectposition);
			}
		}
	};

	/** conversationid回调 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {

		super.requestCommConversationIdCallBack(resultObj);
			//开户
		if (isOpen) {
			requestOpenBondAcctCustomerInfo();
			//登记
		} else {
			// 获取token请求
			requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
					.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		}
	}

	/** 获取债券开户信息 */
	private void requestOpenBondAcctCustomerInfo() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bond.METHOD_BONDOPEN_CUSTOMERINFO);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put(Bond.BANKACCT_ID, BondDataCenter.getInstance()
				.getBankAccList().get(selectposition).get(Bond.ACCOUNTID));
		biiRequestBody.setParams(parms);
		HttpManager
				.requestBii(biiRequestBody, this, "bondAcctOpenInfoCallBack");
	}

	/** 开户信息返回 */
	public void bondAcctOpenInfoCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		if (result == null) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.bond_acctopen_error));
			return;
		}
		BondDataCenter.getInstance().setOpenBondCustomerInfoMap(result);
		// 发送安全因子请求
		requestGetSecurityFactor(Bond.SERVICECODE_SELL);
	}

	/*** 安全因子返回结果 ***/
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialog(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 请求开户确认
						requestBondOpenConfirm();
					}
				});
	}

	/** 开户确认 */
	private void requestBondOpenConfirm() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bond.METHOD_BONDOPEN_CONFIRM);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = BondDataCenter.getInstance()
				.getOpenBondCustomerInfoMap();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(Bond.BUY_CONFIRM_COMBIN, BaseDroidApp.getInstanse()
				.getSecurityChoosed());
		param.put(Bond.ACCOUNTID, BondDataCenter.getInstance().getBankAccList()
				.get(selectposition).get(Bond.ACCOUNTID));
		param.put(Bond.BONDACCT_TYPE, BondDataCenter.bondAcctType_re.get(2));
		param.put(Bond.CUSTOMERNAME, map.get(Bond.CUSTOMERNAME));
		param.put(Bond.IDENTITYPE, map.get(Bond.IDENTIFYTYPE));
		param.put(Bond.IDENTINUMBER, map.get(Bond.IDENTIFYNUMBER));
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this,
				"bondAcctOpenConfirmCallBack");
	}

	/** 开户确认返回 */
	public void bondAcctOpenConfirmCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BaseHttpEngine.dissMissProgressDialog();
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.bond_acctopen_error));
			return;
		}
		BondDataCenter.getInstance().setOpenBondConfirm(result);
		Intent it = new Intent(this, OpendAcctConfirmActivity.class);
		it.putExtra(BANKACCTID, (String) BondDataCenter.getInstance()
				.getBankAccList().get(selectposition).get(Bond.ACCOUNTID));
		it.putExtra(BANKACCTNUM, (String) BondDataCenter.getInstance()
				.getBankAccList().get(selectposition).get(Bond.ACCOUNTNUMBER));
		if (isBuy) {
			it.putExtra(ISBUY, isBuy);
			startActivityForResult(it, BondConstant.BOND_REQUEST_OPEN_ACCT_CODE);
		} else {
			startActivity(it);
		}
	}

	// token返回
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		requestBondAcctSetup(token);
	}

	/**
	 * 托管账户登记
	 * 
	 * @param token
	 */
	private void requestBondAcctSetup(String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bond.METHOD_ACCTSETUP);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Bond.BOND_BUYRESULT_TOKEN, token);
		map.put(Bond.BANKACCT_ID, BondDataCenter.getInstance().getBankAccList()
				.get(selectposition).get(Bond.ACCOUNTID));
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"bondAcctSetupResultCallBack");
	}

	/** 登记提交返回处理 **/
	public void bondAcctSetupResultCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BaseHttpEngine.dissMissProgressDialog();
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.bond_acctsetup_error));
			return;
		}
		Intent it = new Intent(this, AcctSetupResultActivity.class);
		it.putExtra(BANKACCTNUM, (String) BondDataCenter.getInstance().getBankAccList()
				.get(selectposition).get(Bond.ACCOUNTNUMBER));
		it.putExtra(BONDACCT, (String) result.get(Bond.RESETUP_BONDACCT));
		if (isBuy) {	
			it.putExtra(ISBUY,isBuy);
			startActivityForResult(it, BondConstant.BOND_REQUEST_REG_ACCT_CODE);
		} else {
			startActivity(it);
			finish();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case BondConstant.BOND_REQUEST_OPEN_ACCT_CODE:
		case BondConstant.BOND_REQUEST_REG_ACCT_CODE:
			switch (resultCode) {
			case RESULT_OK:
				setResult(RESULT_OK);
				finish();
				break;
			default:
				break;
			}
			break;

		default:
			break;
		}
	}
//	@Override
//	protected void onResume() {
//		super.onResume();
//		setLeftButtonPopupGone();
//	}
}
