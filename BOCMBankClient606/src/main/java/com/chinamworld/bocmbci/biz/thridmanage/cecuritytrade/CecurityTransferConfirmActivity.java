package com.chinamworld.bocmbci.biz.thridmanage.cecuritytrade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Third;
import com.chinamworld.bocmbci.biz.thridmanage.BiiConstant.CurrencyType;
import com.chinamworld.bocmbci.biz.thridmanage.BiiConstant.TransferWayType;
import com.chinamworld.bocmbci.biz.thridmanage.ThirdManagerBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 银行资金转证券信息确认页
 * 
 * @author panwe
 * 
 */
public class CecurityTransferConfirmActivity extends ThirdManagerBaseActivity {

	/** 主布局 **/
	private View viewContent;
	/** 资金账户 */
	private TextView tvBankAcc;
	/** 证券账户 */
	private TextView tvCecurityAcc;
	/** 转账方式 */
	private TextView tvCecType;
	/** 币种 */
	private TextView tvBiZhong;
	/** 转账金额 */
	private TextView tvAmount;
	/** 资金账户信息 */
	private String cecNum;
	/** 银行账户信息 */
	private String banNum;
	/** 交易类型 */
	private String type;
	/** 交易金额 */
	private String amout;
	/** 币种 **/
	private String currency;
	/** 密码 */
	private String password;
	private String password_rc;
	/** 商圈名 */
	private String financeCompany;
	/** 商圈代码 */
	private String stockCode;
	/** 资金账号 **/
	private String capitalAcc;
	/** 账户id */
	private String accid;
	
	private String mTokenId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 添加布局
		viewContent = LayoutInflater.from(this).inflate(R.layout.third_cecuritytrade_confirm, null);
		addView(viewContent);
		setTitle(this.getString(R.string.third_cecuritytrade));
		init();
		getData();
	}

	private void init() {
		// 右上角按钮赋值
		// 右上角按钮赋值
		setRightBtnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goMainActivity();
			}
		});
		setTitleRightText(getString(R.string.go_main));
		tvBankAcc = (TextView) findViewById(R.id.tv_cec_bankacc);
		tvCecurityAcc = (TextView) findViewById(R.id.tv_cec_eceacc);
		tvCecType = (TextView) findViewById(R.id.tv_cec_type);
		tvBiZhong = (TextView) findViewById(R.id.tv_cec_bizhong);
		tvAmount = (TextView) findViewById(R.id.tv_cec_acout);

		Button btnConfirm = (Button) findViewById(R.id.btnconfirm);
		btnConfirm.setOnClickListener(onClick);
	}

	private void getData() {
		Bundle b = getIntent().getBundleExtra("B");
		accid = b.getString("ACCID");
		banNum = b.getString("BANKACCNUM");
		cecNum = b.getString("CECACCNUM");
		type = b.getString("TRADETYPE");
		amout = b.getString("AMOUT");
		currency = b.getString("CNCY");
		financeCompany = b.getString("financeCompany");
		stockCode = b.getString("stockCode");
		capitalAcc = b.getString("capitalAcc");
		if (b.containsKey("PAS")) {
			password = b.getString("PAS");
		}
		if (b.containsKey("PAS_RC")) {
			password_rc = b.getString("PAS_RC");
		}

		tvBankAcc.setText(StringUtil.getForSixForString(banNum));
		tvCecurityAcc.setText(cecNum);
//		if (currency.equals(Third.ENCY_RMB)) {
//			tvBiZhong.setText("人民币");
//		}
		tvBiZhong.setText(CurrencyType.getCurrencyTypeStr(currency));
//		if (type.equals(Third.TRADE_TYPE_BANKTOCEC)) {
//			tvCecType.setText(this.getString(R.string.third_btn_banktocecurity));
//		} else {
//			tvCecType.setText(this.getString(R.string.third_btn_cecuritytobank));
//		}
		tvCecType.setText(TransferWayType.getTransferWayTypeStr(type));

		tvAmount.setText(StringUtil.parseStringPattern(amout, 2));
	}

//	/** 右上角按钮点击事件   */
//	private OnClickListener rightBtnClick = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			Intent intent = new Intent(CecurityTransferConfirmActivity.this, CecurityTradeActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			CecurityTransferConfirmActivity.this.startActivity(intent);
//		}
//	};

	// token返回
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		if(mTokenId== null){
			mTokenId = token;
		}
		requestTransferComit(mTokenId);
	}

	// 转账提交
	private void requestTransferComit(String token) {
		// 展示通讯框
		List<BiiRequestBody> biiRequestBodyList = new ArrayList<BiiRequestBody>();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Third.METHOD_TRANSFER_COMIT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Comm.ACCOUNT_ID, accid);
		paramsmap.put(Third.TRANSFER_COMIT_TRANWAY, type);
		paramsmap.put(Third.CECURITY_AMOUT_RNCY, currency);
		paramsmap.put(Third.TRANSFER_COMIT_AMOUNT, amout);
		paramsmap.put(Third.CECURITY_AMOUT_COMANY, financeCompany);
		paramsmap.put(Third.CECURITY_AMOUT_STCODE, stockCode);
		paramsmap.put(Third.CECURITY_AMOUT_CAACC, capitalAcc);
		paramsmap.put(Third.TRANSFER_COMIT_TOKEN, token);
		if (type.equals(TransferWayType.STOCK_TO_BANK_CODE)) {
			paramsmap.put(Third.CECURITY_AMOUT_PS, password);
			paramsmap.put(Third.CECURITY_AMOUT_PS_RC, password_rc);
			SipBoxUtils.setSipBoxParams(paramsmap);
		}
		biiRequestBody.setParams(paramsmap);
		biiRequestBodyList.add(biiRequestBody);
		HttpManager.requestBii(biiRequestBodyList, this, "transferComitCallback");
	}

	/*** 转账提交返回处理 **/
	public void transferComitCallback(Object resultObj) {
		// 取消通讯框
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String result = (String) biiResponseBody.getResult();

		Intent it = new Intent(CecurityTransferConfirmActivity.this, CecurityTransferResultActivity.class);
		Bundle b = new Bundle();
		b.putString("ACCID", accid);
		b.putString("BANKACCNUM", banNum);
		b.putString("CECACCNUM", cecNum);
		b.putString("TRADETYPE", type);
		b.putString("AMOUT", amout);
		b.putString("CNCY", currency);
		b.putString("financeCompany", financeCompany);
		b.putString("stockCode", stockCode);
		b.putString("capitalAcc", capitalAcc);
//		if (type.equals(TransferWayType.STOCK_TO_BANK_CODE)) {
//			b.putString("PAS", password);
//			b.putString("PAS_RC", password_rc);
//		}
		b.putString("result", result);

		it.putExtras(b);
		startActivity(it);

	}

	/** 确定 */
	private OnClickListener onClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 发送获取token请求
			BaseHttpEngine.showProgressDialog();
			boolean isRequestCommConversationId = getIntent().getBooleanExtra("isRequestCommConversationId", true);
			if (isRequestCommConversationId) {
				requestCommConversationId();
			} else {
				requestPSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap()
						.get(ConstantGloble.CONVERSATION_ID));
			}
		}
	};

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
	};
}
