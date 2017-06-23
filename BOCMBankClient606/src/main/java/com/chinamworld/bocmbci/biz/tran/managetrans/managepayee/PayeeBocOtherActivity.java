package com.chinamworld.bocmbci.biz.tran.managetrans.managepayee;

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
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.ManageTransBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.mytransfer.addpayee.ChooseBankActivity;
import com.chinamworld.bocmbci.biz.tran.mytransfer.addpayee.QueryExternalBankActivity1;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 跨行收款人修改页面
 * 
 * @author wangmengmeng
 * 
 */
public class PayeeBocOtherActivity extends ManageTransBaseActivity implements
		OnClickListener {

	private Button delBtn;
	/** 收款人姓名 */
	private EditText payeeNameEt = null;
	/** 收款人别名 */
	private TextView payeeNikeNameTv = null;
	/** 账号 */
	private EditText accNumEt = null;
	/** 所属银行 */
	private Spinner accInBankSp = null;
	/** 开户行 */
	private TextView inBankNameEt = null;
	/** 收款人手机号 */
	private EditText mobileEt = null;

	Map<String, Object> bocPayeeResulMap = null;
	/** 手机号码 */
	private String phoneNum = null;
	/** 修改后账户名 */
	private String nikeName = null;

	private String payeeId = "";
	private String payeeNum = "";
	private String payeeName = "";
	/** 查询开户行名称 */
	private Button kBankBtn = null;
	private String toOrgName = "";
	private String bankName = "";
	/** 账户所属银行 省行联行号 */
	private String orgNameCnapsCode = "";
	private boolean isfirstselect = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.manage_payee));
		View view = mInflater.inflate(
				R.layout.tran_manage_payee_bocother_detail_update_activity,
				null);
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
					.get(Tran.MANAGE_PAYEELIST_ACCOUNTNAME_RES);
			payeeNameEt.setText(StringUtil.isNullChange(payeeName));
			nikeName = (String) bocPayeeResulMap
					.get(Tran.MANAGE_PAYEELIST_PAYEEALIAS_RES);
			payeeNikeNameTv.setText(StringUtil.isNullChange(nikeName));
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
					payeeNikeNameTv);
			payeeNum = (String) bocPayeeResulMap
					.get(Tran.MANAGE_PAYEELIST_ACCOUNTNUMBER_RES);
			accNumEt.setText(payeeNum);
			phoneNum = (String) bocPayeeResulMap
					.get(Tran.MANAGE_PAYEELIST_MOBILE_RES);
			mobileEt.setText(phoneNum);
			// 账户所属银行
			bankName = (String) bocPayeeResulMap
					.get(Tran.MANAGE_PAYEELIST_BANKNAME_RES);
			// 开户行名称
			toOrgName = (String) bocPayeeResulMap.get(Tran.TRANS_ADDRESS_RES);
			inBankNameEt.setText(toOrgName);
			payeeId = (String) bocPayeeResulMap
					.get(Tran.MANAGE_PAYEELIST_PAYEETID_RES);
			ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
					R.layout.custom_spinner_item,
					ChooseBankActivity.bankNameList);
			adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			accInBankSp.setAdapter(adapter1);
			accInBankSp.setOnItemSelectedListener(onItemSelectedListener);
			for (int i = 0; i < ChooseBankActivity.bankNameList.size(); i++) {
				if (!StringUtil.isNullOrEmpty(bankName)
						&& bankName.equals(ChooseBankActivity.bankNameList
								.get(i))) {
					accInBankSp.setSelection(i);
					break;
				}
			}
		}

	}

	private OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int position, long id) {
			bankName = ChooseBankActivity.bankNameList.get(position);
			if (isfirstselect) {
				isfirstselect = false;
			} else {
				inBankNameEt.setVisibility(View.GONE);
				inBankNameEt.setText(null);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};

	private void setupView() {

		delBtn = (Button) findViewById(R.id.btn_delete_payee_boc_detail);

		payeeNameEt = (EditText) findViewById(R.id.tv_payeeName_payee_boc_detail);
		payeeNikeNameTv = (TextView) findViewById(R.id.tv_payee_nikename_payee_boc_detail);

		accNumEt = (EditText) findViewById(R.id.tv_accnum_payee_boc_detail);
		accInBankSp = (Spinner) findViewById(R.id.sp_accbank_payee_other_bank_write);
		inBankNameEt = (TextView) findViewById(R.id.et_acc_bankname_payee_other_bank_write);
		mobileEt = (EditText) findViewById(R.id.tv_mobile_payee_boc_detail);
		kBankBtn = (Button) findViewById(R.id.btn_query_kbank_othbank_write);
		inBankNameEt.setVisibility(View.VISIBLE);
		delBtn.setOnClickListener(this);
		kBankBtn.setOnClickListener(this);
		EditTextUtils.setLengthMatcher(this, payeeNameEt, 60);
		EditTextUtils.setBankCardNumAddSpace(accNumEt);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				PayeeBocOtherActivity.this, payeeNikeNameTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				PayeeBocOtherActivity.this, inBankNameEt);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_delete_payee_boc_detail:// 修改收款人
			// 校验
			payeeName = payeeNameEt.getText().toString().trim();
			payeeNum = accNumEt.getText().toString().trim();
			phoneNum = mobileEt.getText().toString().trim();
			toOrgName = inBankNameEt.getText().toString().trim();
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
			}
			if (StringUtil.isNullOrEmpty(toOrgName)) {
				errorInfo = getResources()
						.getString(R.string.inbank_name_error);
				BaseDroidApp.getInstanse().showInfoMessageDialog(errorInfo);
				return;
			}
			payeeNum = payeeNum.replace(" ", "");
			// 校验通过，进行修改流程
			requestCommConversationId();
			BiiHttpEngine.showProgressDialog();
			break;
		case R.id.btn_query_kbank_othbank_write:
			// 查询开户行
			requestQueryExternalBank(0, 10);
			break;
		default:
			break;
		}

	}

	/**
	 * 转账银行开户行查询
	 */
	public void requestQueryExternalBank(int currentIndex, int pageSize) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.QUERYEXTERNALBANKINFO);
		Map<String, String> map = new HashMap<String, String>();
		// 银行行号
		map.put(Tran.PAYEE_TOBANKCODE_REQ, LocalData.kBankListMap.get(bankName));
		// 银行名称
		map.put(Tran.PAYEE_BANKNAME_REQ, "");
		// 当前页
		map.put(Tran.PAYEE_CURRENTINDEX_REQ, currentIndex + "");
		// 每页显示条数
		map.put(Tran.PAYEE_PAGESIZE_REQ, pageSize + "");
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"queryExternalBankCallBack");
	}

	/**
	 * 转账银行开户行查询
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 * @return tokenId
	 */
	@SuppressWarnings("unchecked")
	public void queryExternalBankCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		String recordNumber = (String) result.get(Tran.RECORD_NUMBER);

		List<Map<String, String>> queryExternalBankList = (List<Map<String, String>>) result
				.get(ConstantGloble.LIST);
		TranDataCenter.getInstance().setExternalBankList(queryExternalBankList);
		// 跳转到下个列表页面
		Intent intent = new Intent(this, QueryExternalBankActivity1.class);
		intent.putExtra(Tran.RECORD_NUMBER, recordNumber);
		intent.putExtra(Tran.PAYEE_BANKNAME_REQ, bankName);
		startActivityForResult(intent, ConstantGloble.PAYEE_OTHERBANK);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			toOrgName = data
					.getStringExtra(Tran.TRANS_BOCNATIONAL_TOORGNAME_REQ);
			orgNameCnapsCode = data.getStringExtra(Tran.PAYEE_CNAPSCODE_REQ);
			inBankNameEt.setVisibility(View.VISIBLE);
			inBankNameEt.setText(toOrgName);
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
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
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		requestPsnModifyNationalPayee(token);
	}

	/**
	 * 修改跨行收款人
	 * 
	 * @param tokenId
	 */
	public void requestPsnModifyNationalPayee(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.PSNMODIFYNATIONALPAYEE_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.MODIFYNATION_PAYEETID_REQ, payeeId);
		map.put(Tran.MODIFYNATION_PAYEENAME_REQ, payeeName);
		map.put(Tran.MODIFYNATION_PAYEEMOBILE_REQ, phoneNum);
		map.put(Tran.MODIFYNATION_TOACCOUNT_REQ, payeeNum);
		map.put(Tran.MODIFYNATION_BANKNAME_REQ, bankName);
		map.put(Tran.MODIFYNATION_CNAPSCODE_REQ, orgNameCnapsCode);
		map.put(Tran.MODIFYNATION_TOORGNAME_REQ, toOrgName);
		map.put(Tran.MANAGE_NIKENAME_TOKEN_REQ, tokenId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnModifyNationalPayeeCallBack");
	}

	/**
	 * 修改跨行收款人res
	 */
	public void requestPsnModifyNationalPayeeCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		CustomDialog.toastShow(PayeeBocOtherActivity.this,
				PayeeBocOtherActivity.this
						.getString(R.string.nike_name_edit_success));
		ActivityTaskManager.getInstance().removeAllActivity();
		Intent intent = new Intent(this, PayeeListActivity.class);
		intent.putExtra(ISMODIFYPAYEE, "bocOther");
		startActivity(intent);
		finish();

	}
}
