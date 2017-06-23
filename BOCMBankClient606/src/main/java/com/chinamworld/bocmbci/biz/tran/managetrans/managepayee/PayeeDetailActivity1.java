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
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.GetPhoneInfo;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 中行收款人 详情
 * 
 * @author WJP wangchao 定义dialog框没有按照规范要求 统一放在customDialog里面初始化 此处需要优化修改
 *         目前没有时间去优化 带后期修改
 */
public class PayeeDetailActivity1 extends ManageTransBaseActivity implements
		OnClickListener {

	private static final String TAG = PayeeDetailActivity1.class
			.getSimpleName();
	private Button editBtn, delBtn;
	/** 收款人姓名 */
	private TextView payeeNameTv = null;
	/** 收款人别名 */
	private TextView payeeNikeNameTv = null;
	/** 账号 */
	private TextView accNumTv = null;
	/** 账户类型 */
	private TextView accTypeTv = null;
	/** 所属地区 */
	private TextView areaTv = null;
	/** 收款人手机号 */
	private TextView mobileTv = null;
	/** 中行修改手机号 dialog */
	private CustomDialog bocEditMobileDialog;
	/** 中行修改别名 dialog */
	private CustomDialog bocEditNikeNameDialog;
	/** 修改收款人手机号的 dialog */
	private View mobileContentView;
	/** 修改收款人别名的 dialog */
	private View nikeNameContentView;

	Map<String, Object> bocPayeeResulMap = null;
	/** 手机号码 */
	private String phoneNum = null;
	/** 修改后手机号码 */
	private String newEditMobile = null;
	/** 修改后账户名 */
	private String nikeName = null;

	private String payeeId = "";
	private String payeeNum = "";
	private String payeeName = "";

	// 如果是跨行收款人信息的话
	// 账户类型 改为 账号所属银行 所属地区改为 开户行名称
	/** 账户类型和账户所属银行 */
	private TextView displayTypeAndBankTv;
	/** 所属地区和开户行名称 */
	private TextView displayAddrAndBankNameTv;
	/** 判断用户是否修改 */
	private boolean isModify = false;

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
					.get(Tran.MANAGE_PAYEELIST_ACCOUNTNAME_RES);
			payeeNameTv.setText(StringUtil.isNullChange(payeeName));
			nikeName = (String) bocPayeeResulMap
					.get(Tran.MANAGE_PAYEELIST_PAYEEALIAS_RES);
			payeeNikeNameTv.setText(StringUtil.isNullChange(nikeName));
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
					payeeNikeNameTv);
			payeeNum = (String) bocPayeeResulMap
					.get(Tran.MANAGE_PAYEELIST_ACCOUNTNUMBER_RES);
			accNumTv.setText(StringUtil.getForSixForString(payeeNum));
			phoneNum = (String) bocPayeeResulMap
					.get(Tran.MANAGE_PAYEELIST_MOBILE_RES);
			mobileTv.setText(StringUtil.isNullChange(phoneNum));

			String bocFlag = (String) bocPayeeResulMap
					.get(Tran.MANAGE_PAYEELIST_BOCFLAG_RES);
			if ((ConstantGloble.BOCBANK).equals(bocFlag)) {// 行内
				String accountType = (String) bocPayeeResulMap
						.get(Tran.MANAGE_PAYEELIST_TYPE_RES);
				accTypeTv.setText(LocalData.AccountType.get(accountType));
				String ibkNumber = (String) bocPayeeResulMap
						.get(Tran.MANAGE_PAYEELIST_ACCOUNTIBKNUM_RES);
				areaTv.setText(LocalData.Province.get(ibkNumber));
			} else if ((ConstantGloble.OTHERBANK).equals(bocFlag)) {// 跨行
				displayTypeAndBankTv
						.setText(getString(R.string.acc_in_bank_name));
				displayAddrAndBankNameTv
						.setText(getString(R.string.acc_bank_name));
				// 账户所属银行
				String bankName = (String) bocPayeeResulMap
						.get(Tran.MANAGE_PAYEELIST_BANKNAME_RES);
				accTypeTv.setText(bankName);
				// 开户行名称
				String address = (String) bocPayeeResulMap
						.get(Tran.TRANS_ADDRESS_RES);
				areaTv.setText(address);
			}

			payeeId = (String) bocPayeeResulMap
					.get(Tran.MANAGE_PAYEELIST_PAYEETID_RES);
			// payeeName = (String) bocPayeeResulMap
			// .get(Tran.MANAGE_PAYEELIST_PAYEEALIAS_RES);
		}

	}

	private void setupView() {

		editBtn = (Button) findViewById(R.id.btn_edit_payee_boc_detail);
		delBtn = (Button) findViewById(R.id.btn_delete_payee_boc_detail);

		payeeNameTv = (TextView) findViewById(R.id.tv_payeeName_payee_boc_detail);
		payeeNikeNameTv = (TextView) findViewById(R.id.tv_payee_nikename_payee_boc_detail);

		accNumTv = (TextView) findViewById(R.id.tv_accnum_payee_boc_detail);
		accTypeTv = (TextView) findViewById(R.id.tv_acctype_payee_boc_detail);
		areaTv = (TextView) findViewById(R.id.tv_area_payee_boc_detail);
		mobileTv = (TextView) findViewById(R.id.tv_mobile_payee_boc_detail);

		editBtn.setOnClickListener(this);
		delBtn.setOnClickListener(this);

		displayTypeAndBankTv = (TextView) findViewById(R.id.tran_acc_type_or_bank_tv);
		displayAddrAndBankNameTv = (TextView) findViewById(R.id.tran_address_or_bankname_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				PayeeDetailActivity1.this, payeeNameTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				PayeeDetailActivity1.this, payeeNikeNameTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				PayeeDetailActivity1.this, accNumTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				PayeeDetailActivity1.this, accTypeTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				PayeeDetailActivity1.this, areaTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				PayeeDetailActivity1.this, mobileTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				PayeeDetailActivity1.this, displayTypeAndBankTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				PayeeDetailActivity1.this, displayAddrAndBankNameTv);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_edit_payee_boc_detail:// 编辑
			BaseDroidApp.getInstanse().showPayeeEditDialog(
					new OnClickListener() {

						@Override
						public void onClick(View v) {

							switch ((Integer) v.getTag()) {
							case CustomDialog.TAG_RELA_ACC_TRAN:// 修改收款人手机号
								BaseDroidApp.getInstanse()
										.dismissMessageDialog();
								editPayeeMobile();
								break;
							case CustomDialog.TAG_COMMON_RECEIVER_TRAN:// 修改收款人别名
								BaseDroidApp.getInstanse()
										.dismissMessageDialog();
								editPayeeNikeName();
								break;
							case CustomDialog.TAG_CANCLE:// 取消
								BaseDroidApp.getInstanse()
										.dismissMessageDialog();
								break;
							default:
								break;
							}
						}
					});
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

	/**
	 * 修改收款人别名
	 */
	private void editPayeeNikeName() {
		showPayeeEditNikeNameDialog(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btn_confirm_nike_name_payee_edit_dialog:

					EditText nikeNameEt = (EditText) nikeNameContentView.findViewById(R.id.et_nike_name_payee_edit_dialog);
					EditTextUtils.setLengthMatcher(PayeeDetailActivity1.this,
							nikeNameEt, 50);
					nikeName = nikeNameEt.getText().toString().trim();
					//
					// ArrayList<RegexpBean> lists = new
					// ArrayList<RegexpBean>();
					//
					// RegexpBean nickNameReg = new
					// RegexpBean(getResources().getString(
					// R.string.regexp_nike_name), newEditNikeName, "nickname");
					//
					// lists.add(nickNameReg);
					// if (!RegexpUtils.regexpDate(lists)) {//
					// 账户别名最长20个英文字符或10个中文字符
					// return;
					// }else{
					// requestEditNikeNameConversationId();
					// }
					requestEditNikeNameConversationId();
					break;
				case R.id.btn_cancle_nike_name_payee_edit_dialog:
					dismissbocEditNikeNameDialog();
					break;
				default:
					break;
				}
			}
		}, payeeNikeNameTv.getText().toString().trim());
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
					RegexpBean reb = new RegexpBean(PayeeDetailActivity1.this
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

	// //////////////////////////////////////////////////////////////////////////
	// PsnTransManagePayeeModifyAlias修改收款人别名
	/**
	 * 修改收款人别名 请求conversationId
	 */
	public void requestEditNikeNameConversationId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.CONVERSATION_ID_API);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"editNikeNameConversationIdCallBack");
	}

	/**
	 * 修改收款人别名 请求conversation 回调
	 * 
	 * @param resultObj
	 */
	public void editNikeNameConversationIdCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		String commConversationId = (String) biiResponseBody.getResult();
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.CONVERSATION_ID, commConversationId);

		if (StringUtil.isNullOrEmpty(commConversationId)) {
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}

		requestEditNikeNameGetTokenId();
	}

	/**
	 * 修改收款人别名 获取tokenId
	 */
	public void requestEditNikeNameGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.GETTOKENID);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this,
				"editNikeNameGetTokenIdCallBack");
	}

	/**
	 * 修改收款人别名 获取tokenId的数据得到tokenId
	 * 
	 * @return tokenId
	 */
	public void editNikeNameGetTokenIdCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String tokenId = (String) biiResponseBody.getResult();
		requestEditNikeName(tokenId);
	}

	/**
	 * 修改收款人别名 req
	 */
	public void requestEditNikeName(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.EDITNIKENAME);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.MANAGE_NIKENAME_ACCOUNTID_REQ, payeeId);
		map.put(Tran.MANAGE_NIKENAME_ACCOUNTNICKNAME_REQ, nikeName);
		map.put(Tran.MANAGE_NIKENAME_ACCOUNTNICKNAME_OLD_REQ, payeeName);
		map.put(Tran.MANAGE_NIKENAME_TOKEN_REQ, tokenId);

		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "editNikeNameCallBack");
	}

	/**
	 * 修改收款人别名 res
	 */
	public void editNikeNameCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();

		// BiiResponse biiResponse = (BiiResponse) resultObj;
		// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		dismissbocEditNikeNameDialog();

		CustomDialog.toastShow(PayeeDetailActivity1.this,
				PayeeDetailActivity1.this
						.getString(R.string.nike_name_edit_success));

		payeeNikeNameTv.setText(StringUtil.isNullChange(nikeName));
		isModify = true;
	}

	// //////////////////////////////////////////////////////////////////////////
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

		CustomDialog.toastShow(PayeeDetailActivity1.this,
				PayeeDetailActivity1.this
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

	// ////////////////////////////////////////////////// 修改别名
	/**
	 * 修改收款人别名 dialog
	 * 
	 * @param onclickListener
	 * @param nikeName
	 *            旧别名
	 */
	public void showPayeeEditNikeNameDialog(OnClickListener onclickListener,
			String nikeName) {
		bocEditNikeNameDialog = new CustomDialog(this, R.style.Theme_Dialog);
		View v = initPayeeEditNikeNameView(onclickListener, nikeName);
		bocEditNikeNameDialog.setContentView(v);

		WindowManager.LayoutParams lp = bocEditNikeNameDialog.getWindow()
				.getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH * 6 / 7;
		lp.height = LayoutValue.SCREEN_WIDTH / 2;
		bocEditNikeNameDialog.getWindow().setAttributes(lp);
		bocEditNikeNameDialog.show();

	}

	/**
	 * 加载修改收款人别名dialog
	 * 
	 * @param onclickListener
	 * @param nikeName
	 *            旧别名
	 * @return
	 */
	public View initPayeeEditNikeNameView(View.OnClickListener onclickListener,
			String nikeName) {
		nikeNameContentView = LayoutInflater.from(this).inflate(
				R.layout.trans_custom_dialog_payee_edit_nike_name, null);

		Button confirmBtn = (Button) nikeNameContentView
				.findViewById(R.id.btn_confirm_nike_name_payee_edit_dialog);
		confirmBtn.setTag(TAG_CONFIRM);
		confirmBtn.setOnClickListener(onclickListener);
		Button cancleBtn = (Button) nikeNameContentView
				.findViewById(R.id.btn_cancle_nike_name_payee_edit_dialog);
		cancleBtn.setTag(TAG_CANCLE);
		cancleBtn.setOnClickListener(onclickListener);

		TextView mobileTv = (TextView) nikeNameContentView
				.findViewById(R.id.tv_nike_name_payee_edit_dialog);
		mobileTv.setText(nikeName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mobileTv);

		EditText nikeNameEt = (EditText) nikeNameContentView
				.findViewById(R.id.et_nike_name_payee_edit_dialog);
		nikeName = nikeName.equals("-") ? "" : nikeName;
		nikeNameEt.setText(nikeName);
		nikeNameEt.setSelection(nikeName.length());

		EditTextUtils.setLengthMatcher(PayeeDetailActivity1.this, nikeNameEt,
				50);
		nikeNameEt.setTag(TAG_TRAN_BOCMOBILE);
		return nikeNameContentView;
	}

	/**
	 * 取消收款人别名对话框
	 */
	private void dismissbocEditNikeNameDialog() {
		if (bocEditNikeNameDialog != null || bocEditNikeNameDialog.isShowing()) {
			bocEditNikeNameDialog.dismiss();
		}
	}

}
