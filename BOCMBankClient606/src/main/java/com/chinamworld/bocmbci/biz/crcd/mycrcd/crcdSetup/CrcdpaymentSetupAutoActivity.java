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
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 主动还款确认
 * 
 * @author huangyuchao
 * 
 */
public class CrcdpaymentSetupAutoActivity extends CrcdBaseActivity {

	private View view;

	Button lastButton, sureButton;

	TextView mycrcd_renmi_account, mycrcd_foreign_huan_type;

	public static final String autoPayType = "-1";
	private String accountId = null;
	/** 还款标志 */
	private int benAutoPaymentTag = -1;
	private int foreanAutoPaymentTag = -1;
	private View benBiView = null;
	private View WaiBiView = null;
	/**是否显示人民币*/
    private Boolean isShowRmb=false;
    /**是否显示外币*/
    private Boolean isShowWb=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_creditcard_paymentstatusclose));
		view = addView(R.layout.crcd_payment_setup_auto_confirm);
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
//		benAutoPaymentTag = getIntent().getIntExtra(ConstantGloble.ISFOREX_CASH1, -1);
//		foreanAutoPaymentTag = getIntent().getIntExtra(ConstantGloble.ISFOREX_CASH2, -1);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		
		psnCrcdQueryCrcdPaymentWay();
		
	}

	public void psnCrcdQueryCrcdPaymentWay() {
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDQUERYCRCDPAYMENTWAY_API);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "getqueryPaymentCallBack");
	}

	protected  Map<String, Object> returnList = new HashMap<String, Object>();

	public void getqueryPaymentCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		if (null != list && list.size() > 0) {
			BiiResponseBody body = list.get(0);
			returnList = (Map<String, Object>) body.getResult();
		}

		init();

	}
	
	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	/** 初始化界面 */
	private void init() {
		Map<String, Object> setupMap = null;
		setupMap = returnList;
		
		
		String localPayment = String.valueOf(setupMap.get(Crcd.CRCD_LOCALCURRENCYPAYMENT));
		if (StringUtil.isNullOrEmpty(localPayment)) {
			benAutoPaymentTag = -1;
		} else {
			if ("0".equals(localPayment)) {
				
				benAutoPaymentTag = 0;
			} else if ("1".equals(localPayment)) {
				
				benAutoPaymentTag = 1;
			}
		}
		String foreignpayment = String.valueOf(setupMap.get(Crcd.CRCD_FOREIGNCURRENCYPAYMENT));
		if (StringUtil.isNullOrEmpty(foreignpayment)) {
			foreanAutoPaymentTag = -1;
		} else {
			if ("0".equals(foreignpayment)) {
				
				foreanAutoPaymentTag = 0;
			} else if ("1".equals(localPayment)) {
				
				foreanAutoPaymentTag = 1;
			}
		}
		benBiView = findViewById(R.id.benbi_layout);
		WaiBiView = findViewById(R.id.waibi_layout);
		mycrcd_renmi_account = (TextView) findViewById(R.id.mycrcd_renmi_account);
		mycrcd_foreign_huan_type = (TextView) findViewById(R.id.mycrcd_foreign_huan_type);
		if (-1 == foreanAutoPaymentTag) {
			WaiBiView.setVisibility(View.GONE);
			isShowWb=false;
		} else {
			WaiBiView.setVisibility(View.VISIBLE);
			isShowWb=true;
		}
		if (-1 == benAutoPaymentTag) {
			benBiView.setVisibility(View.GONE);
			isShowRmb=false;
		} else {
			benBiView.setVisibility(View.VISIBLE);
			isShowRmb=true;
		}
		mycrcd_renmi_account.setText(getString(R.string.mycrcd_myself_huanmoney));
		mycrcd_foreign_huan_type.setText(getString(R.string.mycrcd_myself_huanmoney));

		lastButton = (Button) findViewById(R.id.lastButton);
		lastButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		sureButton = (Button) findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();
			}
		});
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);

		pSNGetTokenId();
	}

	@Override
	public void aquirePSNGetTokenIdCallBack(Object resultObj) {
		super.aquirePSNGetTokenIdCallBack(resultObj);
		// 还款设定
		psnCrcdPaymentWaySetup();
	}

	public void psnCrcdPaymentWaySetup() {
		// 通讯开始,展示通讯框
		// BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDPAYMENTWAYSETUP);
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		params.put(Crcd.CRCD_REPAYTYPE, "0");
		params.put(Crcd.CRCD_AUTOREPAYMODE, autoPayType);
		params.put(Crcd.CRCD_REPAYCURSEL, autoPayType);
		params.put(Crcd.CRCD_TOKEN, tokenId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdPaymentWaySetupCallBack");
	}

	public void psnCrcdPaymentWaySetupCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();

		Intent it = new Intent(CrcdpaymentSetupAutoActivity.this, CrcdpaymentSetupAutoSuccessActivity.class);
		it.putExtra(Crcd.CRCD_LOCALCURRENCYPAYMENT, isShowRmb);
		it.putExtra(Crcd.CRCD_FOREIGNCURRENCYPAYMENT, isShowWb);
		// startActivity(it);
		startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;

		default:
			break;
		}
	}
}
