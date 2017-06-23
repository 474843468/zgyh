package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 信用卡还款方式确认
 * 
 * @author huangyuchao
 * 
 */
public class CrcdPaymentwaySetupConfirm extends CrcdBaseActivity {

	private View view;

	TextView mycrcd_accounted_type;
	TextView mycrcd_selected_creditcard;
	TextView mycrcd_accounted_money;
	TextView mycrcd_renmi_account;
	TextView mycrcd_foreign_account;

	Button lastButton, sureButton;
	private String accountId = null;
	private String accountNumber = null;
	private String autoRepayMode = null;
	private int repayCurSel = -1;
	private int rmbPosition = -1;
	private int wbPosition = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_creditcard_money_setup_title));
		view = addView(R.layout.crcd_payment_setup_confirm);
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		autoRepayMode = getIntent().getStringExtra(Crcd.CRCD_AUTOREPAYMODE);
		repayCurSel = getIntent().getIntExtra(Crcd.CRCD_REPAYCURSEL, -1);
		rmbPosition = getIntent().getIntExtra(Crcd.CRCD_RMBREPAYACCID, -1);
		wbPosition = getIntent().getIntExtra(Crcd.CRCD_REPAYACCID, -1);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		init();
	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	LinearLayout ll_bennumber, ll_wainumber;

	/** 初始化界面 */
	private void init() {
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getResources().getString(R.string.mycrcd_setup_style),
						this.getResources().getString(R.string.mycrcd_setup_info),
						this.getResources().getString(R.string.mycrcd_setup_success) });
		StepTitleUtils.getInstance().setTitleStep(2);

		ll_bennumber = (LinearLayout) view.findViewById(R.id.ll_bennumber);
		ll_wainumber = (LinearLayout) view.findViewById(R.id.ll_wainumber);

		ll_bennumber.setVisibility(View.GONE);
		ll_wainumber.setVisibility(View.GONE);

		mycrcd_accounted_type = (TextView) view.findViewById(R.id.mycrcd_accounted_type);
		mycrcd_selected_creditcard = (TextView) view.findViewById(R.id.mycrcd_selected_creditcard);
		mycrcd_accounted_money = (TextView) view.findViewById(R.id.mycrcd_accounted_money);
		mycrcd_renmi_account = (TextView) view.findViewById(R.id.mycrcd_renmi_account);
		mycrcd_foreign_account = (TextView) view.findViewById(R.id.mycrcd_foreign_account);

		mycrcd_accounted_type.setText(getString(R.string.mycrcd_auto_huanmoney));
		mycrcd_selected_creditcard.setText(CrcdPaymentwaySetup.strRepayCurSel);
		mycrcd_accounted_money.setText(CrcdPaymentwaySetup.strautoRepayMode);

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mycrcd_selected_creditcard);

		String benNumber = CrcdPaymentwaySetup.getBenNumber();
		String waiNumber = CrcdPaymentwaySetup.getWaiNumber();

		if (CrcdPaymentwaySetup.ll_rmbShow.getVisibility() == View.VISIBLE) {
			ll_bennumber.setVisibility(View.VISIBLE);
			mycrcd_renmi_account.setText(benNumber);
		}
		if (CrcdPaymentwaySetup.ll_foreignShow.getVisibility() == View.VISIBLE) {
			ll_wainumber.setVisibility(View.VISIBLE);
			mycrcd_foreign_account.setText(waiNumber);
		}

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
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
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
		if (StringUtil.isNullOrEmpty(tokenId)) {
			return;
		}
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
		params.put(Crcd.CRCD_REPAYTYPE, "1");
		params.put(Crcd.CRCD_AUTOREPAYMODE, autoRepayMode);
		params.put(Crcd.CRCD_REPAYCURSEL, repayCurSel + "");
		if (repayCurSel == ALL_FOREIGN) {
			params.put(Crcd.CRCD_RMBREPAYACCID, MyCrcdSetupReadActivity.benIdArray.get(rmbPosition));
			params.put(Crcd.CRCD_FOREIGNREPAYACCTID, MyCrcdSetupReadActivity.waiIdArray.get(wbPosition));
		} else if (repayCurSel == ALL_REN) {
			params.put(Crcd.CRCD_REPAYACCID, MyCrcdSetupReadActivity.benIdArray.get(rmbPosition));
		} else if (repayCurSel == ALL_RENANDFOREIGN) {
			params.put(Crcd.CRCD_SIGNFOREIGNCURRENCYACCTID, MyCrcdSetupReadActivity.waiIdArray.get(wbPosition));
		}
		params.put(Crcd.CRCD_TOKEN, tokenId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdPaymentWaySetupCallBack");
	}

	public void psnCrcdPaymentWaySetupCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Intent it = new Intent(CrcdPaymentwaySetupConfirm.this, CrcdPaymentwaySetupSuccess.class);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
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
