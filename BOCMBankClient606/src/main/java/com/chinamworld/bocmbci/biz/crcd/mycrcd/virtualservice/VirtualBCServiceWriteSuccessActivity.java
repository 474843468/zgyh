package com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdAccBaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.BomConstants;
import com.chinamworld.bocmbci.biz.epay.myPayService.bomobile.servOpen.ServOpenAgreementActivity;
import com.chinamworld.bocmbci.biz.epay.myPayService.bomobile.setPaymentAcc.SetPaymentAccConfirmActivity;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 虚拟银行卡填写成功信息
 * 
 * @author huangyuchao
 * 
 */
public class VirtualBCServiceWriteSuccessActivity extends CrcdAccBaseActivity {

	private View view = null;

	Button lastButton, sureButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_virtual_shenqing));
		if (view == null) {
			view = addView(R.layout.crcd_mycard_service_write_success);
		}
		back.setVisibility(View.GONE);
		init();
	}

	static String accountId;
	static String accountNumber;
	static String currencyCode;
	private int accNum = 0;

	TextView finc_accNumber, finc_fenqidate, finc_miaoshus;
	TextView finc_validatime, finc_remiannomoney, finc_nextdate;

	TextView finc_startdate;

	EditText haha;

	/** 系统时间 */
	private String currenttime;

	static Map<String, Object> vcardinfo;

	public static Map<String, Object> getVcardinfo() {
		return vcardinfo;
	}

	/** 初始化界面 */
	private void init() {

		// StepTitleUtils.getInstance().initTitldStep(
		// this,
		// new String[] {
		// this.getResources().getString(
		// R.string.mycrcd_virtual_write_info),
		// this.getResources().getString(
		// R.string.mycrcd_setup_info),
		// this.getResources().getString(
		// R.string.mycrcd_setup_success) });
		// StepTitleUtils.getInstance().setTitleStep(3);

		finc_accNumber = (TextView) findViewById(R.id.finc_accNumber);
		finc_fenqidate = (TextView) findViewById(R.id.finc_fenqidate);
		finc_miaoshus = (TextView) findViewById(R.id.finc_miaoshus);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_miaoshus);
		finc_accNumber = (TextView) view.findViewById(R.id.finc_accNumber);
		finc_fenqidate = (TextView) view.findViewById(R.id.finc_fenqidate);
		finc_miaoshus = (TextView) view.findViewById(R.id.finc_miaoshus);
		finc_validatime = (TextView) view.findViewById(R.id.finc_validatime);
		finc_remiannomoney = (TextView) view.findViewById(R.id.finc_remiannomoney);
		finc_nextdate = (TextView) view.findViewById(R.id.finc_nextdate);

		vcardinfo = VirtualBCServiceWriteConfirmActivity.vcardinfo;

		String virtualNo = String.valueOf(vcardinfo.get(Crcd.CRCD_VIRTUALCARDNO));

		String startDate = String.valueOf(vcardinfo.get(Crcd.CRCD_STARTDATE));

		if (startDate.contains("-")) {
			startDate = startDate.replaceAll("-", "/");
		}

		String endDate = String.valueOf(vcardinfo.get(Crcd.CRCD_ENDDATE));

		if (endDate.contains("-")) {
			endDate = endDate.replaceAll("-", "/");
		}

		finc_accNumber.setText(StringUtil.getForSixForString(VirtualBCServiceListActivity.accountNumber));
		// 虚拟卡号
		finc_fenqidate.setText(virtualNo);
		finc_miaoshus.setText(VirtualBCServiceListActivity.accountName);

		finc_startdate = (TextView) findViewById(R.id.finc_startdate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_startdate);
		finc_startdate.setText(startDate);

		finc_validatime.setText(endDate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_validatime);
		String singleMoney = String.valueOf(vcardinfo.get(Crcd.CRCD_SINGLEEMT));
		String totalMoney = String.valueOf(vcardinfo.get(Crcd.CRCD_TOTALEAMT));

		finc_remiannomoney.setText(StringUtil.parseStringPattern(totalMoney, 2));
		finc_nextdate.setText(StringUtil.parseStringPattern(singleMoney, 2));

		lastButton = (Button) findViewById(R.id.lastButton);
		lastButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 开通手机银行支付
				psnEpayIsOpenOnlinePayment();
			}
		});

		sureButton = (Button) findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent it = new Intent(VirtualBCServiceWriteSuccessActivity.this, VirtualBCServiceMenuActivity.class);
				it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(it);
				// ActivityTaskManager.getInstance().removeAllActivity();
			}
		});

	}

	public void psnEpayIsOpenOnlinePayment() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BomConstants.METHOD_IS_OPENONLINE_PAYMENT);
		HttpManager.requestBii(biiRequestBody, this, "psnEpayIsOpenOnlinePaymentCallBack");
	}

	public void psnEpayIsOpenOnlinePaymentCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Object result = biiResponseBody.getResult();
		boolean isOpenBomPayService = EpayUtil.getBoolean(result);

		Intent it = new Intent();
		if (isOpenBomPayService) {
			it.setClass(VirtualBCServiceWriteSuccessActivity.this, SetPaymentAccConfirmActivity.class);
			it.putExtra("excuteType", 2);
			Bundle b = new Bundle();
			b.putString("selectedAcc", "test");
			it.putExtra("qccRedirct", b);
		} else {
			it.setClass(VirtualBCServiceWriteSuccessActivity.this, ServOpenAgreementActivity.class);
			it.putExtra("excuteType", 1);
			Bundle b = new Bundle();
			b.putString("selectedAcc", "test");
			it.putExtra("qccRedirct", b);
		}
		startActivity(it);
	}


	@Override
	protected void onDestroy() {
		BaseHttpEngine.dissMissProgressDialog();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
	}
}
