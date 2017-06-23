package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import java.util.HashMap;
import java.util.List;
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
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 对账单关闭
 * 
 * @author huangyuchao
 * 
 */
public class CrcdPsnCheckCloseActivity extends CrcdBaseActivity {

	private View view;

	static String zhuiText;
	private int billSetupId;
	private String strBillSetup;
	private String isEdit;
	private String accountNumber = null;
	private String accountId = null;
	private String email;
	/** 纸质账单地址 */
	private String paperAddress;
	private String mobile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		billSetupId = this.getIntent().getIntExtra(
				ConstantGloble.CRCD_BILLSETUPID, -1);
		if (billSetupId == 0) {

			strBillSetup = this.getString(R.string.mycrcd_paper_billdan);
		}
		if (billSetupId == 1) {

			strBillSetup = this.getString(R.string.mycrcd_email_billdan);
		}
		if (billSetupId == 2) {

			strBillSetup = this.getString(R.string.mycrcd_phone_billdan);
		}
		isEdit = this.getIntent().getStringExtra(
				ConstantGloble.CRCD_ISOPENOREDIT);
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		paperAddress = getIntent().getStringExtra(Crcd.CRCD_PAPERADDRESS);
		mobile = getIntent().getStringExtra(Crcd.CRCD_MOBILE);
		email = getIntent().getStringExtra(Crcd.CRCD_EMAIL);
		// zhuiText = CrcdPsnQueryCheckDetail.strbillSetupId;

		// 为界面标题赋值
		setTitle(this.getString(R.string.close) + strBillSetup);
		view = addView(R.layout.crcd_query_check_close);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		init();
	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	TextView finc_accId, mycrd_service_type;

	Button sureButton;

	LinearLayout ll_paper, ll_email, ll_phone;

	TextView et_paper, et_email, et_phone;

	/** 初始化界面 */
	private void init() {

		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources().getString(
								R.string.mycrcd_write_step_info),
						this.getResources().getString(
								R.string.mycrcd_setup_info),
						this.getResources().getString(
								R.string.mycrcd_service_setup_success) });
		StepTitleUtils.getInstance().setTitleStep(1);

		ll_paper = (LinearLayout) view.findViewById(R.id.ll_paper);
		ll_email = (LinearLayout) view.findViewById(R.id.ll_email);
		ll_phone = (LinearLayout) view.findViewById(R.id.ll_phone);

		et_paper = (TextView) view.findViewById(R.id.et_paper);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, et_paper);
		et_email = (TextView) view.findViewById(R.id.et_email);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, et_email);
		et_phone = (TextView) view.findViewById(R.id.et_phone);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, et_phone);
		finc_accId = (TextView) findViewById(R.id.finc_accId);
		mycrd_service_type = (TextView) findViewById(R.id.mycrd_service_type);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				mycrd_service_type);
		finc_accId.setText(StringUtil.getForSixForString(accountNumber));
		mycrd_service_type.setText(strBillSetup);

		if (0 == billSetupId) {
			ll_paper.setVisibility(View.VISIBLE);
			ll_email.setVisibility(View.GONE);
			ll_phone.setVisibility(View.GONE);
			if (StringUtil.isNull(paperAddress)) {
				et_paper.setText("-");
			} else {
				et_paper.setText(paperAddress);
			}

		} else if (1 == billSetupId) {
			ll_paper.setVisibility(View.GONE);
			ll_email.setVisibility(View.VISIBLE);
			ll_phone.setVisibility(View.GONE);
			if (StringUtil.isNull(email)) {
				et_email.setText("-");
			} else {
				et_email.setText(email);
			}
		} else if (2 == billSetupId) {
			ll_paper.setVisibility(View.GONE);
			ll_email.setVisibility(View.GONE);
			ll_phone.setVisibility(View.VISIBLE);
			if (StringUtil.isNull(mobile)) {
				et_phone.setText("-");
			} else {
				et_phone.setText(mobile);
			}
		}

		sureButton = (Button) findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 请求安全因子组合id
				BaseHttpEngine.showProgressDialog();
				requestGetSecurityFactor(psnChecksecurityId);
			}
		});

	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialog(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						psnCrcdInfoServiceCloseConfirm();
					}
				});
	}

	public void psnCrcdInfoServiceCloseConfirm() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDINFOSERVICECLOSECONFIRM);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		map.put(Crcd.CRCD_BILLSERVICEID, billSetupId + "");
		map.put(Crcd.CRCD_BILLADDRESS, paperAddress);
		map.put(Crcd.CRCD_COMBINID, BaseDroidApp.getInstanse()
				.getSecurityChoosed());
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"psnCrcdInfoServiceCloseConfirmCallBack");
	}

	public static Map<String, Object> returnMap;

	public void psnCrcdInfoServiceCloseConfirmCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		returnMap = (Map<String, Object>) body.getResult();

		Intent it = new Intent(CrcdPsnCheckCloseActivity.this,
				CrcdPsnCheckCloseConfirmActivity.class);
		it.putExtra(ConstantGloble.CRCD_ISOPENOREDIT, isEdit);
		it.putExtra(ConstantGloble.CRCD_BILLSETUPID, billSetupId);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		it.putExtra(Crcd.CRCD_PAPERADDRESS, paperAddress);
		it.putExtra(Crcd.CRCD_MOBILE, mobile);
		it.putExtra(Crcd.CRCD_EMAIL, email);
		// startActivity(it);
		startActivityForResult(it,
				ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
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
