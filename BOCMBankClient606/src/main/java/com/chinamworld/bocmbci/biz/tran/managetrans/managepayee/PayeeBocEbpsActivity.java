package com.chinamworld.bocmbci.biz.tran.managetrans.managepayee;

import java.util.HashMap;
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
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.ManageTransBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.mytransfer.addpayee.QueryExternalBankActivity1;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 实时收款人修改页面
 * 
 * @author wangmengmeng
 * 
 */
public class PayeeBocEbpsActivity extends ManageTransBaseActivity implements
		OnClickListener {
	private Button delBtn;
	/** 收款人姓名 */
	private EditText payeeNameTv = null;
	/** 账号 */
	private EditText accNumTv = null;
	/** 账户类型 */
	private TextView accTypeTv = null;
	/** 收款人手机号 */
	private EditText mobileTv = null;

	Map<String, Object> bocPayeeResulMap = null;
	/** 手机号码 */
	private String phoneNum = null;

	private String payeeId = "";
	private String payeeNum = "";
	private String payeeName = "";
	/** 实时——所属银行查询 */
	private Button btn_query_bank;
	private String toOrgName = "";
	/** 账户所属银行 省行联行号 */
	private String orgNameCnapsCode = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.manage_payee));
		View view = mInflater.inflate(
				R.layout.tran_manage_payee_ebps_update_activity, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);

		// modify by wjp 步骤栏没有显示 但是这个代码不要删除 会影响布局
		StepTitleUtils.getInstance().initTitldStep(this,
				this.getResources().getStringArray(R.array.tran_my_trans));
		StepTitleUtils.getInstance().setTitleStep(2);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		setupView();

		initData();
	}

	private void initData() {
		bocPayeeResulMap = TranDataCenter.getInstance().getCurPayeeMap();
		if (bocPayeeResulMap != null) {
			payeeName = (String) bocPayeeResulMap
					.get(Tran.EBPSQUERY_PAYEEACTNAME_REQ);
			payeeNameTv.setText(StringUtil.isNullChange(payeeName));
			payeeNum = (String) bocPayeeResulMap
					.get(Tran.EBPSQUERY_PAYEEACTNO_REQ);
			accNumTv.setText(payeeNum);
			phoneNum = (String) bocPayeeResulMap
					.get(Tran.EBPSQUERY_PAYEEMOBILE_REQ);
			mobileTv.setText(phoneNum);

			// 账户所属银行
			String bankName = (String) bocPayeeResulMap
					.get(Tran.EBPSQUERY_PAYEEBANKNAME_REQ);
			accTypeTv.setText(bankName);

			payeeId = (String) bocPayeeResulMap
					.get(Tran.EBPSQUERY_PAYEEACTID_REQ);
		}

	}

	private void setupView() {

		delBtn = (Button) findViewById(R.id.btn_delete_payee_boc_detail);
		btn_query_bank = (Button) findViewById(R.id.btn_query_bank);
		payeeNameTv = (EditText) findViewById(R.id.tv_payeeName_payee_boc_detail);

		accNumTv = (EditText) findViewById(R.id.tv_accnum_payee_boc_detail);
		accTypeTv = (TextView) findViewById(R.id.tv_acctype_payee_boc_detail);
		mobileTv = (EditText) findViewById(R.id.tv_mobile_payee_boc_detail);
		EditTextUtils.setLengthMatcher(this, payeeNameTv, 60);
		EditTextUtils.setBankCardNumAddSpace(accNumTv);
		delBtn.setOnClickListener(this);
		btn_query_bank.setOnClickListener(this);
		PopupWindowUtils.getInstance()
				.setOnShowAllTextListener(this, accTypeTv);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_query_bank:
			// 查询所属银行——实时
			psnEbpsQueryAccountOfBank(0, 10);
			break;
		case R.id.btn_delete_payee_boc_detail:
			payeeName = payeeNameTv.getText().toString().trim();
			payeeNum = accNumTv.getText().toString().trim();
			phoneNum = mobileTv.getText().toString().trim();
			toOrgName = accTypeTv.getText().toString().trim();
			String errorInfo = null;
			if (StringUtil.isNullOrEmpty(payeeName)) {
				errorInfo = getResources().getString(R.string.payee_name_error);
				BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);

				return;
			}
			if (StringUtil.isNullOrEmpty(payeeNum)) {
				errorInfo = getResources().getString(
						R.string.payee_accnum_error);
				BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);
				return;
			} else {
				// TODO 账户的正则 待确定
			}
			if (StringUtil.isNullOrEmpty(toOrgName)) {
				errorInfo = getResources().getString(
						R.string.shishibank_name_error);
				BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);
				return;
			}
			payeeNum = payeeNum.replace(" ", "");
			// 修改流程
			requestCommConversationId();
			BiiHttpEngine.showProgressDialog();
			break;
		default:
			break;
		}

	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String tokenId = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.EBPS_MODIFY_PAYEETID_REQ, payeeId);
		map.put(Tran.EBPS_MODIFY_TOACCOUNT_REQ, payeeNum);
		map.put(Tran.EBPS_MODIFY_BANKNAME_REQ, toOrgName);
		map.put(Tran.EBPS_MODIFY_PAYEENAME_REQ, payeeName);
		map.put(Tran.EBPS_MODIFY_PAYEEMOBILE_REQ, phoneNum);
		map.put(Tran.EBPS_MODIFY_TOKEN_REQ, tokenId);
		requestBIIForTran(Tran.PSNMODIFYEBPSREALTIMEPAYEE_API, map,
				"psnModifyEbpsRealTimePayeeCallBack");
	}

	/**
	 * 修改实时收款人res
	 */
	public void psnModifyEbpsRealTimePayeeCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		CustomDialog.toastShow(this,
				this.getString(R.string.nike_name_edit_success));
		ActivityTaskManager.getInstance().removeAllActivity();
		Intent intent=new Intent(this,PayeeListActivity.class);
		intent.putExtra(ISMODIFYPAYEE, "bocEbps");
		startActivity(intent);
		finish();

	}

	/**
	 * 实时转账——所属银行模糊查询
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	@SuppressWarnings("unchecked")
	public void psnEbpsQueryAccountOfBankCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		String recordNumber = (String) result
				.get(Tran.EBPSQUERY_RECORDNUMBER_RES);

		List<Map<String, String>> queryExternalBankList = (List<Map<String, String>>) result
				.get(Tran.EBPSQUERY_ACCOUNTOFBANKLIST_RES);
		TranDataCenter.getInstance().setShishiBankList(queryExternalBankList);
		// 跳转到下个列表页面
		Intent intent = new Intent(this, QueryExternalBankActivity1.class);
		intent.putExtra(Tran.RECORD_NUMBER, recordNumber);
		intent.putExtra(ISSHISHITYPE, true);
		startActivityForResult(intent, ConstantGloble.CHOOSE_SHISHIBANK);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			toOrgName = data
					.getStringExtra(Tran.TRANS_BOCNATIONAL_TOORGNAME_REQ);
			orgNameCnapsCode = data.getStringExtra(Tran.PAYEE_CNAPSCODE_REQ);
			accTypeTv.setVisibility(View.VISIBLE);
			accTypeTv.setText(toOrgName);

			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);

	}
}
