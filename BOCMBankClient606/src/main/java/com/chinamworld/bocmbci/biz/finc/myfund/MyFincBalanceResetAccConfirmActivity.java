package com.chinamworld.bocmbci.biz.finc.myfund;


import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/***
 * 我的基金 重设基金账户 确认页面
 * 
 * @author Administrator
 * 
 */
public class MyFincBalanceResetAccConfirmActivity extends FincBaseActivity {
	private final String TAG = "MyFincBalanceResetAccConfirmActivity";
	/** 基金持仓主view */
	private View myFincView = null;
	/** 资金账户 */
	private TextView accNumberText = null;
	/** 基金账户 */
	private TextView numberText = null;
	private Button sureButton = null;
	/** 资金账户别名 */
	private String nickName = null;
	/** 资金账户 */
	private String accountNumber = null;
	/** 账户标志 */
	private String accountId = null;
	private String tokenId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settingbaseinit();
		init();
		initData();
		initOnClick();
	}

	/** 初始化控件 */
	private void init() {
		myFincView = mainInflater.inflate(R.layout.finc_myfinc_balance_reset_confirm, null);
		tabcontent.addView(myFincView);
		setTitle(getResources().getString(R.string.finc_myfinc_resetAcc_tite));
		accNumberText = (TextView) findViewById(R.id.finc_accId);
		numberText = (TextView) findViewById(R.id.finc_accNumber);
		sureButton = (Button) findViewById(R.id.sureButton);

		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getResources().getString(R.string.finc_myfinc_acc1),
						this.getResources().getString(R.string.finc_myfinc_acc2),
						this.getResources().getString(R.string.finc_myfinc_acc3) });
		StepTitleUtils.getInstance().setTitleStep(2);
	}

	/** 初始化数据 */
	private void initData() {
		Intent intent = getIntent();
		if (intent == null) {
			return;
		} else {
			nickName = intent.getStringExtra(Finc.FINC_NICKNAME_RES);
			accountNumber = intent.getStringExtra(Finc.FINC_ACCOUNTNUMBER_RES);
			accountId = intent.getStringExtra(Finc.FINC_ACCOUNTIDM_RES);
			String c=accountNumber;
			c = StringUtil.getForSixForString(c);
			accNumberText.setText(c);
			//TODO-------
			//numberText先设置为账户标志
			numberText.setText(fincControl.invAccId);
		}
	}

	/** 初始化监听事件 */
	private void initOnClick() {
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 获取Conversation
				
			}
		});
	}

	/** Conversation---回调 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 获取tockenId
		requestPSNGetTokenId();
	}

	@Override
	public void requestPSNGetTokenIdCallback(Object resultObj) {
		super.requestPSNGetTokenIdCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		tokenId = (String) biiResponseBody.getResult();
		if ("".equals(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			requestPsnFundRegistFundAccount(accountId, tokenId);
		}
	}

	/** 登记基金账户---回调 */
	@Override
	public void requestPsnFundRegistFundAccountCallback(Object resultObj) {
		super.requestPsnFundRegistFundAccountCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> result = (Map<String, String>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			return;
		} else {
//			String fincAccount = result.get(Finc.FINC_FINCACCOUNT_RES);
//			if (StringUtil.isNullOrEmpty(fincAccount)) {
//				return;
//			} else {
				//跳转到账户设置成功页面
				Intent intent = new Intent(MyFincBalanceResetAccConfirmActivity.this,
						MyFincBalanceResetAccSuccessActivity.class);
				intent.putExtra(Finc.FINC_NICKNAME_RES, nickName);
				intent.putExtra(Finc.FINC_ACCOUNTNUMBER_RES, accountNumber);
				intent.putExtra(Finc.FINC_FINCACCOUNT_RES,fincControl.invAccId);
				startActivity(intent);
//			}
		}

	}
}
