package com.chinamworld.bocmbci.biz.tran.managetrans.managepayee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.ManageTransBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.http.GetPhoneInfo;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 实时收款人详情页面
 */
public class EbpsPayeeDetailActivity1 extends ManageTransBaseActivity implements
		OnClickListener {

	private static final String TAG = EbpsPayeeDetailActivity1.class
			.getSimpleName();
	private Button editBtn, delBtn;
	/** 收款人姓名 */
	private TextView payeeNameTv = null;
	/** 账号 */
	private TextView accNumTv = null;
	/** 账户类型 */
	private TextView accTypeTv = null;
	/** 收款人手机号 */
	private TextView mobileTv = null;

	Map<String, Object> bocPayeeResulMap = null;
	/** 手机号码 */
	private String phoneNum = null;

	private String payeeId = "";
	private String payeeNum = "";
	private String payeeName = "";

	// 如果是跨行收款人信息的话
	// 账户类型 改为 账号所属银行 所属地区改为 开户行名称
	/** 账户类型和账户所属银行 */
	private TextView displayTypeAndBankTv;
	/** 判断用户是否修改 */
	private boolean isModify = false;
	/** 中行修改手机号 dialog */
	private CustomDialog bocEditMobileDialog;
	/** 修改收款人手机号的 dialog */
	private View mobileContentView;
	/** 修改后手机号码 */
	private String newEditMobile = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.manage_payee));
		View view = mInflater.inflate(
				R.layout.tran_manage_payee_boc_detail_activity, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);

		// modify by wjp 步骤栏没有显示 但是这个代码不要删除 会影响布局
		StepTitleUtils.getInstance().initTitldStep(this,
				this.getResources().getStringArray(R.array.tran_my_trans));
		StepTitleUtils.getInstance().setTitleStep(2);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isModify)
					setResult(105);
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
			accNumTv.setText(StringUtil.getForSixForString(payeeNum));
			phoneNum = (String) bocPayeeResulMap
					.get(Tran.EBPSQUERY_PAYEEMOBILE_REQ);
			mobileTv.setText(StringUtil.isNullChange(phoneNum));

			displayTypeAndBankTv.setText(getString(R.string.acc_in_bank_name));
			// 账户所属银行
			String bankName = (String) bocPayeeResulMap
					.get(Tran.EBPSQUERY_PAYEEBANKNAME_REQ);
			accTypeTv.setText(bankName);

			payeeId = (String) bocPayeeResulMap
					.get(Tran.EBPSQUERY_PAYEEACTID_REQ);
		}

	}

	private void setupView() {

		editBtn = (Button) findViewById(R.id.btn_edit_payee_boc_detail);
		delBtn = (Button) findViewById(R.id.btn_delete_payee_boc_detail);

		payeeNameTv = (TextView) findViewById(R.id.tv_payeeName_payee_boc_detail);

		accNumTv = (TextView) findViewById(R.id.tv_accnum_payee_boc_detail);
		accTypeTv = (TextView) findViewById(R.id.tv_acctype_payee_boc_detail);
		mobileTv = (TextView) findViewById(R.id.tv_mobile_payee_boc_detail);
		LinearLayout ll_address = (LinearLayout) findViewById(R.id.ll_address);
		ll_address.setVisibility(View.GONE);
		LinearLayout ll_nickname = (LinearLayout) findViewById(R.id.ll_nickname);
		ll_nickname.setVisibility(View.GONE);
		editBtn.setText(this.getString(R.string.tran_edit_mobile));
		editBtn.setOnClickListener(this);
		delBtn.setOnClickListener(this);

		displayTypeAndBankTv = (TextView) findViewById(R.id.tran_acc_type_or_bank_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				EbpsPayeeDetailActivity1.this, payeeNameTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				EbpsPayeeDetailActivity1.this, accNumTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				EbpsPayeeDetailActivity1.this, accTypeTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				EbpsPayeeDetailActivity1.this, mobileTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				EbpsPayeeDetailActivity1.this, displayTypeAndBankTv);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_edit_payee_boc_detail:// 编辑
			//TODO 把注释打开即为进入修改收款人信息页面
			// Intent intent = new Intent(EbpsPayeeDetailActivity1.this,
			// PayeeBocEbpsActivity.class);
			// startActivity(intent);
			editPayeeMobile();
			break;
		case R.id.btn_delete_payee_boc_detail:// 删除收款人

			String message = getResources().getString(
					R.string.del_payee_confirm);
			BaseDroidApp.getInstanse().showErrorDialog(message,
					R.string.cancle, R.string.confirm, delPayeeListener);
			break;
		default:
			break;
		}

	}

	/**
	 * 修改中行收款人手机号
	 * 
	 */
	private void editPayeeMobile() {
		showPayeeEditMobileDialog(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btn_confirm_payee_edit_dialog:

					EditText mobileEt = (EditText) mobileContentView.findViewById(R.id.et_mobile_payee_edit_dialog);
					newEditMobile = mobileEt.getText().toString().trim();
					RegexpBean reb = new RegexpBean(
							EbpsPayeeDetailActivity1.this
									.getString(R.string.bocinvt_mobile_regex),
							newEditMobile, "shoujiH_01_15");

					ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
					lists.add(reb);
					if (RegexpUtils.regexpDate(lists)) {// 校验通过
						requestEditMobileConversationId();
					}

					break;
				case R.id.btn_cancle_payee_edit_dialog:
					dismissbocEditMobileDialog();
					break;
				default:
					break;
				}
			}
		}, mobileTv.getText().toString().trim());

	}

	// PsnTransManagePayeeModifyMobile修改收款人手机号
	/**
	 * 修改收款人手机号 请求conversationId
	 */
	public void requestEditMobileConversationId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.CONVERSATION_ID_API);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"editMobileConversationIdCallBack");
	}

	/**
	 * 修改收款人手机号 请求conversation 回调
	 * 
	 * @param resultObj
	 */
	public void editMobileConversationIdCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		String commConversationId = (String) biiResponseBody.getResult();
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.CONVERSATION_ID, commConversationId);

		if (StringUtil.isNullOrEmpty(commConversationId)) {
			return;
		}
		LogGloble.i(TAG, commConversationId + "++ commConversationId+");
		requestEditMobileGetTokenId(); // PsnGetSecurityFactor获得安全因子
	}

	/**
	 * 修改收款人手机号 获取tokenId
	 */
	public void requestEditMobileGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.GETTOKENID);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this,
				"editMobileGetTokenIdCallBack");
	}

	/**
	 * 修改收款人手机号 获取tokenId的数据得到tokenId
	 * 
	 * @return tokenId
	 */
	public void editMobileGetTokenIdCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		String tokenId = (String) biiResponseBody.getResult();

		LogGloble.i(TAG, "++tokenId++" + tokenId);
		requestEditMobile(tokenId);
	}

	/**
	 * 修改收款人手机号 req
	 */
	public void requestEditMobile(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.EDITMOBILE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));

		// Map<String,String>preDateAndExeTypeMap = (Map<String, String>)
		// BaseDroidApp.getInstanse().getBizDataMap().get("preDateAndExeTypeMap");
		// String prAndExeQueryFlag =
		// preDateAndExeTypeMap.get("preAndExeQueryType");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.MANAGE_BOCMOBILE_PAYEEID_REQ, payeeId);
		map.put(Tran.MANAGE_BOCMOBILE_OLDMOBILE_REQ, phoneNum);
		map.put(Tran.MANAGE_BOCMOBILE_MOBILE_REQ, newEditMobile);
		map.put(Tran.MANAGE_BOCMOBILE_TOKEN_REQ, tokenId);
		map.put(Tran.MANAGE_BOCMOBILE_TOUSERNAME_REQ, payeeName);
		map.put(Tran.MANAGE_BOCMOBILE_TOACCOUNTNO_REQ, payeeNum);

		GetPhoneInfo.addPhoneInfo(map);// 反欺诈 设备指纹
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "editMobileCallBack");
	}

	/**
	 * 修改收款人手机号 res
	 */
	public void editMobileCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();

		// BiiResponse biiResponse = (BiiResponse) resultObj;
		// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		dismissbocEditMobileDialog();

		CustomDialog.toastShow(EbpsPayeeDetailActivity1.this,
				EbpsPayeeDetailActivity1.this
						.getString(R.string.mobile_edit_success));

		mobileTv.setText(newEditMobile);

		isModify = true;
	}

	// ///////////////////////////////////////////////////////////// 修改手机号
	/**
	 * 修改收款人手机号 dialog
	 * 
	 * @param onclickListener
	 * @param phoneNum
	 *            旧手机号
	 */
	public void showPayeeEditMobileDialog(OnClickListener onclickListener,
			String phoneNum) {
		bocEditMobileDialog = new CustomDialog(this, R.style.Theme_Dialog);
		View v = initPayeeEditMobileView(onclickListener, phoneNum);
		bocEditMobileDialog.setContentView(v);

		WindowManager.LayoutParams lp = bocEditMobileDialog.getWindow()
				.getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH * 6 / 7;
		lp.height = LayoutValue.SCREEN_WIDTH / 2;
		bocEditMobileDialog.getWindow().setAttributes(lp);
		bocEditMobileDialog.show();

	}

	/**
	 * 加载修改收款人手机号 dialog
	 * 
	 * @param onclickListener
	 * @param phoneNum
	 *            旧手机号
	 * @return
	 */
	public View initPayeeEditMobileView(View.OnClickListener onclickListener,
			String phoneNum) {
		mobileContentView = LayoutInflater.from(this).inflate(
				R.layout.trans_custom_dialog_payee_edit_mobile, null);

		Button confirmBtn = (Button) mobileContentView
				.findViewById(R.id.btn_confirm_payee_edit_dialog);
		confirmBtn.setTag(TAG_CONFIRM);
		confirmBtn.setOnClickListener(onclickListener);
		Button cancleBtn = (Button) mobileContentView
				.findViewById(R.id.btn_cancle_payee_edit_dialog);
		cancleBtn.setTag(TAG_CANCLE);
		cancleBtn.setOnClickListener(onclickListener);

		TextView mobileTv = (TextView) mobileContentView
				.findViewById(R.id.tv_mobile_payee_edit_dialog);
		mobileTv.setText(phoneNum);

		EditText mobileEt = (EditText) mobileContentView
				.findViewById(R.id.et_mobile_payee_edit_dialog);
		phoneNum = phoneNum.equals("-") ? "" : phoneNum;
		mobileEt.setText(phoneNum);
		mobileEt.setSelection(phoneNum.length());

		mobileEt.setTag(TAG_TRAN_BOCMOBILE);
		return mobileContentView;
	}

	/**
	 * 取消手机号编辑框
	 */
	private void dismissbocEditMobileDialog() {
		if (bocEditMobileDialog != null || bocEditMobileDialog.isShowing()) {
			bocEditMobileDialog.dismiss();
		}
	}

	/**
	 * 删除收款人
	 */
	private OnClickListener delPayeeListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch ((Integer) v.getTag()) {
			case CustomDialog.TAG_SURE:
				BaseDroidApp.getInstanse().dismissErrorDialog();
				requestManagePayeeConversationId(payeeId);
				break;
			case CustomDialog.TAG_CANCLE:
				BaseDroidApp.getInstanse().dismissErrorDialog();
				break;
			default:
				break;
			}

		}
	};

}
