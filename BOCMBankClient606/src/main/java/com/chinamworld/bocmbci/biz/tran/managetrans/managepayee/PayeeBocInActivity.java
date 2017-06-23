package com.chinamworld.bocmbci.biz.tran.managetrans.managepayee;

import java.util.HashMap;
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
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.ManageTransBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
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
 * 行内收款人修改页面
 * 
 * @author wangmengmeng
 * 
 */
public class PayeeBocInActivity extends ManageTransBaseActivity implements
		OnClickListener {

	private Button delBtn;
	/** 收款人姓名 */
	private EditText payeeNameEt = null;
	/** 收款人别名 */
	private TextView payeeNikeNameTv = null;
	/** 账号 */
	private EditText accNumEt = null;
	/** 账户类型 */
	private TextView accTypeTv = null;
	/** 所属地区 */
	private TextView areaTv = null;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.manage_payee));
		View view = mInflater.inflate(
				R.layout.tran_manage_payee_boc_detail_update_activity, null);
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

			String accountType = (String) bocPayeeResulMap
					.get(Tran.MANAGE_PAYEELIST_TYPE_RES);
			accTypeTv.setText(LocalData.AccountType.get(accountType));
			String ibkNumber = (String) bocPayeeResulMap
					.get(Tran.MANAGE_PAYEELIST_ACCOUNTIBKNUM_RES);
			areaTv.setText(LocalData.Province.get(ibkNumber));

			payeeId = (String) bocPayeeResulMap
					.get(Tran.MANAGE_PAYEELIST_PAYEETID_RES);
		}

	}

	private void setupView() {

		delBtn = (Button) findViewById(R.id.btn_delete_payee_boc_detail);

		payeeNameEt = (EditText) findViewById(R.id.tv_payeeName_payee_boc_detail);
		payeeNikeNameTv = (TextView) findViewById(R.id.tv_payee_nikename_payee_boc_detail);

		accNumEt = (EditText) findViewById(R.id.tv_accnum_payee_boc_detail);
		accTypeTv = (TextView) findViewById(R.id.tv_acctype_payee_boc_detail);
		areaTv = (TextView) findViewById(R.id.tv_area_payee_boc_detail);
		mobileEt = (EditText) findViewById(R.id.tv_mobile_payee_boc_detail);

		delBtn.setOnClickListener(this);
		EditTextUtils.setLengthMatcher(this, payeeNameEt, 60);
		EditTextUtils.setBankCardNumAddSpace(accNumEt);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				PayeeBocInActivity.this, payeeNikeNameTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				PayeeBocInActivity.this, accTypeTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				PayeeBocInActivity.this, areaTv);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_delete_payee_boc_detail:// 修改收款人
			// 校验
			payeeName = payeeNameEt.getText().toString().trim();
			payeeNum = accNumEt.getText().toString().trim();
			phoneNum = mobileEt.getText().toString().trim();
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
			payeeNum = payeeNum.replace(" ", "");
			// 校验通过，进行修改流程
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
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		requestPsnModifyBocPayee(token);
	}

	/**
	 * 修改行内收款人
	 * 
	 * @param tokenId
	 */
	public void requestPsnModifyBocPayee(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.PSNMODIFYBOCPAYEE_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.MODIFYBOC_PAYEETID_REQ, payeeId);
		map.put(Tran.MODIFYBOC_PAYEENAME_REQ, payeeName);
		map.put(Tran.MODIFYBOC_PAYEEMOBILE_REQ, phoneNum);
		map.put(Tran.MODIFYBOC_TOACCOUNT_REQ, payeeNum);
		map.put(Tran.MANAGE_NIKENAME_TOKEN_REQ, tokenId);

		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnModifyBocPayeeCallBack");
	}

	/**
	 * 修改行内收款人res
	 */
	public void requestPsnModifyBocPayeeCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		CustomDialog.toastShow(PayeeBocInActivity.this, PayeeBocInActivity.this
				.getString(R.string.nike_name_edit_success));
		ActivityTaskManager.getInstance().removeAllActivity();
		Intent intent = new Intent(this, PayeeListActivity.class);
		intent.putExtra(ISMODIFYPAYEE, "bocIn");
		startActivity(intent);
		finish();

	}

}
