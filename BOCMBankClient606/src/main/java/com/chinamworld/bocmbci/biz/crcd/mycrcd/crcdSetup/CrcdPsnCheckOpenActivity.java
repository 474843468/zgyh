package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 开通账单-----编辑账单
 * 
 * @author huangyuchao
 * 
 */
public class CrcdPsnCheckOpenActivity extends CrcdBaseActivity {

	private View view;

	String strBillSetup;
	int billSetupId;
	TextView finc_accNumber;
	TextView finc_accId;

	static String passType;

	Button sureButton;
	LinearLayout ll_paper, ll_email, ll_phone;

	EditText et_paper, et_email, et_phone;

	Spinner sp_papers;

	String[] addressArray;

	String[] addressNumArray;

	static String strEmail, phoneNum;

	String isEdit;

	protected String prefix;

	public static Map<String, Object> returnMap;

	
	private String accountNumber = null;
	private String accountId = null;

	private String email;
	/** 纸质账单地址 */
	private String paperAddress;
	private String mobile;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		strBillSetup = CrcdPsnQueryCheckDetail.strbillSetupId;
		billSetupId = this.getIntent().getIntExtra(ConstantGloble.CRCD_BILLSETUPID, -1);
		if(billSetupId==0){
			
			strBillSetup = this.getString(R.string.mycrcd_paper_billdan);
		}
		if(billSetupId==1){
		
			strBillSetup = this.getString(R.string.mycrcd_email_billdan);
		}
		if(billSetupId==2){
			
			strBillSetup = this.getString(R.string.mycrcd_phone_billdan);
		}
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		isEdit = this.getIntent().getStringExtra(ConstantGloble.CRCD_ISOPENOREDIT);
		mobile = getIntent().getStringExtra(Crcd.CRCD_MOBILE);
		email = getIntent().getStringExtra(Crcd.CRCD_EMAIL);
		if ("open".equals(isEdit)) {
			prefix = this.getString(R.string.mycrcd_open);
		} else if ("edit".equals(isEdit)) {
			prefix = this.getString(R.string.edit);
		}

		// 为界面标题赋值
		setTitle(prefix + strBillSetup);
		view = addView(R.layout.crcd_psn_check_open);
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

	/** 初始化界面 */
	private void init() {
		addressArray = new String[] { this.getString(R.string.mycrcd_office_address),
				this.getString(R.string.mycrcd_home_address), this.getString(R.string.mycrcd_other_address) };

		addressNumArray = new String[] { "HOME", "BUSS", "OTHR" };

		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getResources().getString(R.string.mycrcd_write_info_message),
						this.getResources().getString(R.string.mycrcd_setup_info),
						this.getResources().getString(R.string.mycrcd_service_setup_success) });
		StepTitleUtils.getInstance().setTitleStep(1);

		finc_accNumber = (TextView) view.findViewById(R.id.finc_accNumber);
		finc_accId = (TextView) view.findViewById(R.id.finc_accId);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_accId);
		finc_accNumber.setText(StringUtil.getForSixForString(accountNumber));
		finc_accId.setText(strBillSetup);

		ll_paper = (LinearLayout) view.findViewById(R.id.ll_paper);
		ll_email = (LinearLayout) view.findViewById(R.id.ll_email);
		ll_phone = (LinearLayout) view.findViewById(R.id.ll_phone);

		et_paper = (EditText) view.findViewById(R.id.et_paper);
		et_email = (EditText) view.findViewById(R.id.et_email);
		et_phone = (EditText) view.findViewById(R.id.et_phone);

		sp_papers = (Spinner) view.findViewById(R.id.sp_papers);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, addressArray);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_papers.setAdapter(adapter);
		sp_papers.setSelection(0);

		sp_papers.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				et_paper.setText("");
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		sureButton = (Button) view.findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (billSetupId == 0) {
					// 请求安全因子组合id
					BaseHttpEngine.showProgressDialog();
					requestGetSecurityFactor(psnChecksecurityId);
				} else if (billSetupId == 1) {
					strEmail = et_email.getText().toString();
					// 验证
					RegexpBean reb1 = new RegexpBean(CrcdPsnCheckOpenActivity.this
							.getString(R.string.mycrcd_email_address), strEmail, "email");
					ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
					lists.add(reb1);
					if (RegexpUtils.regexpDate(lists)) {
						// 请求安全因子组合id
						BaseHttpEngine.showProgressDialog();
						requestGetSecurityFactor(psnChecksecurityId);
					}
				} else if (billSetupId == 2) {
					phoneNum = et_phone.getText().toString();
					// 验证
					RegexpBean reb1 = new RegexpBean(CrcdPsnCheckOpenActivity.this
							.getString(R.string.manage_payee_phone_num), phoneNum, "shoujiH_11_15");
					ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
					lists.add(reb1);
					if (RegexpUtils.regexpDate(lists)) {
						// 请求安全因子组合id
						BaseHttpEngine.showProgressDialog();
						requestGetSecurityFactor(psnChecksecurityId);
					}
				}

			}
		});

		if (billSetupId == 0) {
			ll_paper.setVisibility(View.VISIBLE);
			ll_email.setVisibility(View.GONE);
			ll_phone.setVisibility(View.GONE);
			et_paper.setVisibility(View.GONE);
			if ("edit".equals(isEdit)) {

				et_paper.setText(paperAddress);
			}

		} else if (billSetupId == 1) {
			ll_paper.setVisibility(View.GONE);
			ll_email.setVisibility(View.VISIBLE);
			ll_phone.setVisibility(View.GONE);

			if ("edit".equals(isEdit)) {
				et_email.setText(email);
			}
		} else if (billSetupId == 2) {
			ll_paper.setVisibility(View.GONE);
			ll_email.setVisibility(View.GONE);
			ll_phone.setVisibility(View.VISIBLE);

			if ("edit".equals(isEdit)) {
				et_phone.setText(mobile);
			}
		}

	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);

		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 账单开通或修改确认
				crcdServiceInfoConfirm();
			}
		});
	}

	public void crcdServiceInfoConfirm() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		if ("open".equals(isEdit)) {
			biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDPAPERCHECKOPENCONFIRM);
		} else if ("edit".equals(isEdit)) {
			biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDSERVICEMODCONFIRM);
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		map.put(Crcd.CRCD_BILLSERVICEID, billSetupId + "");
		if (billSetupId == 0) {
			// addressType---仅纸质对账单
			map.put(Crcd.CRCD_ADDRESSTYPE, String.valueOf(addressNumArray[sp_papers.getSelectedItemPosition()]));
			map.put(Crcd.CRCD_BILLADDRESS, CrcdPsnQueryCheckDetail.paperAddress);
		}
		if (billSetupId == 1) {
			map.put(Crcd.CRCD_BILLADDRESS, strEmail);
		}
		if (billSetupId == 2) {
			map.put(Crcd.CRCD_BILLADDRESS, phoneNum);
		}

		map.put(Crcd.CRCD_COMBINID, BaseDroidApp.getInstanse().getSecurityChoosed());
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "crcdServiceInfoConfirmCallBack");
	}

	public void crcdServiceInfoConfirmCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		returnMap = (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		// 仅对纸质对账单
		if (billSetupId == 0) {
			paperAddress = String.valueOf(returnMap.get(Crcd.CRCD_BILLADRESS));
			if (StringUtil.isNull(paperAddress)) {
				BaseHttpEngine.dissMissProgressDialog();
				return;
			}
		}

		BaseHttpEngine.dissMissProgressDialog();
		Intent it = new Intent(CrcdPsnCheckOpenActivity.this, CrcdPsnCheckOpenConfirmActivity.class);
		it.putExtra(ConstantGloble.CRCD_ISOPENOREDIT, isEdit);
		it.putExtra(ConstantGloble.CRCD_BILLSETUPID, billSetupId);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		it.putExtra(Crcd.CRCD_PAPERADDRESS, paperAddress);
		// startActivity(it);
		startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)

	{
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
