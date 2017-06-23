package com.chinamworld.bocmbci.biz.epay.myPayService.treaty.detail;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.epay.EPayBaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.constants.TreatyConstants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.myPayService.treaty.dialog.DeleteMerchantConfirmDialog;
import com.chinamworld.bocmbci.biz.epay.myPayService.treaty.modifyQuota.InputQuotaActivity;
import com.chinamworld.bocmbci.biz.epay.observer.PubHttpObserver;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class MerchantDetailActivity extends EPayBaseActivity {

	private View merchantDetail;

	private LinearLayout ll_c_client;

	private TextView tv_treaty_num;
	private TextView tv_cust_name;
	private TextView tv_identity_type;
	private TextView tv_identity_num;
	private TextView tv_merchant_name;
	private TextView tv_merchant_acc;
	private TextView tv_pay_account;
	private TextView tv_pay_account_type;
	private TextView tv_addup_quota;
	private TextView tv_treaty_status;
	private TextView tv_contract_date;
	private TextView tv_contract_channel;
	private TextView tv_contract_mobile_num;
	private TextView tv_contract_client;

	private Button bt_modify_quota;
	private Button bt_cancel_contract;

	private Map<Object, Object> merchant;
	private String treatyNum;
	private String custName;
	private String identityType;
	private String identityNum;
	private String merchantAcc;
	private String merchantName;
	private String payAccount;
	private String payAccountType;
	private String addUpQuota;
	/** 协议状态 */
	private String treatyStatus;
	private String contractDate;
	private String contractChannel;
	private String contractMobileNum;
	private String signDate;
	private String contractClient;

	private String serviceId;

	private Context treatyContext;
	private PubHttpObserver httpObserver;

	protected String combinId;

	private String bocNo;

	private String curTreatyStatus;

	// 动态口令和手机交易码
	private DeleteMerchantConfirmDialog deleteMerchantConfirmDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		treatyContext = TransContext.getTreatyTransContext();
		httpObserver = PubHttpObserver.getInstance(this, PubConstants.CONTEXT_TREATY);
		merchantDetail = LayoutInflater.from(this).inflate(R.layout.epay_treaty_detail, null);

		super.setType(0);
		super.setShowBackBtn(true);
		super.setTitleName(PubConstants.TITLE_TREATY);
		super.setContentView(merchantDetail);
		super.onCreate(savedInstanceState);

		// // 初始化导航条
		// EpayPubUtil.initStepBar(this, 2, new String[] { "修改限额", "确认信息",
		// "修改成功" });
		getTransData();
	}

	private void initCurPage() {
		ll_c_client = (LinearLayout) merchantDetail.findViewById(R.id.ll_c_client);
		tv_treaty_num = (TextView) merchantDetail.findViewById(R.id.tv_treaty_num);
		tv_treaty_num.setText(treatyNum);
		tv_cust_name = (TextView) merchantDetail.findViewById(R.id.tv_cust_name);
		tv_cust_name.setText(custName);
		tv_identity_type = (TextView) merchantDetail.findViewById(R.id.tv_identity_card);
		tv_identity_type.setText(LocalData.IDENTITYTYPE.get(identityType));
		tv_identity_num = (TextView) merchantDetail.findViewById(R.id.tv_identity_num);
		tv_identity_num.setText(identityNum);
		tv_merchant_name = (TextView) merchantDetail.findViewById(R.id.tv_merchant_name);
		tv_merchant_name.setText(merchantName);
		tv_merchant_acc = (TextView) merchantDetail.findViewById(R.id.tv_merchant_acc);
		tv_merchant_acc.setText(merchantAcc);
		tv_pay_account = (TextView) merchantDetail.findViewById(R.id.tv_pay_account);
		tv_pay_account.setText(StringUtil.getForSixForString(payAccount));
		tv_pay_account_type = (TextView) merchantDetail.findViewById(R.id.tv_pay_account_type);
		tv_pay_account_type.setText(LocalData.AccountType.get(payAccountType));
		tv_addup_quota = (TextView) merchantDetail.findViewById(R.id.tv_addup_quota);
		tv_addup_quota.setText(StringUtil.parseStringPattern(addUpQuota, 2));
		tv_contract_client = (TextView) merchantDetail.findViewById(R.id.tv_contract_client);
		// 如果解约隐藏底部按钮
		if (!"V".equals(getTreatyStatus())) {
			findViewById(R.id.ll_button).setVisibility(View.GONE);
		}

		if (!"2".equals(contractChannel) && !StringUtil.isNullOrEmpty(contractClient)) {
			ll_c_client.setVisibility(View.VISIBLE);
			tv_contract_client.setText(TreatyConstants.TREATY_AGENT.get(contractClient));
		} else {
			ll_c_client.setVisibility(View.GONE);
		}
		tv_treaty_status = (TextView) merchantDetail.findViewById(R.id.tv_treaty_status);
		tv_treaty_status.setText(TreatyConstants.TREATY_STATUS.get(treatyStatus));
		tv_contract_date = (TextView) merchantDetail.findViewById(R.id.tv_contract_date);
		tv_contract_date.setText(contractDate);
		tv_contract_channel = (TextView) merchantDetail.findViewById(R.id.tv_contract_channel);
		tv_contract_channel.setText(TreatyConstants.TREATY_CHANNEL.get(contractChannel));
		tv_contract_mobile_num = (TextView) merchantDetail.findViewById(R.id.tv_contract_mobile_num);
		tv_contract_mobile_num.setText(contractMobileNum);

		bt_cancel_contract = (Button) merchantDetail.findViewById(R.id.bt_cancel_contract);
		bt_cancel_contract.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!checkRelationStatus()) {
					return;
				}

				BiiHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
		});

		bt_modify_quota = (Button) merchantDetail.findViewById(R.id.bt_modify_quota);
		bt_modify_quota.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!checkRelationStatus()) {
					return;
				}
				Intent intent = new Intent(MerchantDetailActivity.this, InputQuotaActivity.class);
				MerchantDetailActivity.this.startActivityForResult(intent, 0);
				overridePendingTransition(R.anim.n_pop_enter_bottom_up, R.anim.no_animation);
			}
		});

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_treaty_num);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_cust_name);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_identity_type);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_identity_num);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_merchant_name);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_merchant_acc);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_pay_account);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_pay_account_type);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_addup_quota);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_treaty_status);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_contract_date);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_contract_channel);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_contract_mobile_num);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_contract_client);

	}

	private boolean checkRelationStatus() {
		if (!"V".equals(getTreatyStatus())) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("商户签约状态非“有效”，不能进行该操作");
			return false;
		}
		return true;
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		String conversationId = EpayUtil.getString(httpObserver.getResult(resultObj), "");
		treatyContext.setData(PubConstants.PUB_FIELD_CONVERSATION_ID, conversationId);
		httpObserver.setConversationId(conversationId);
		requestGetSecurityFactor("PB202C");

	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		final BaseDroidApp baseDroidApp = BaseDroidApp.getInstanse();
		baseDroidApp.showSeurityChooseDialog(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				combinId = baseDroidApp.getSecurityChoosed();
				BiiHttpEngine.showProgressDialog();
				httpObserver.req_getSystemTime("getSystemTimeCallback");
			}
		});
	}

	/** 获取系统时间回调方法 */
	public void getSystemTimeCallback(Object resultObj) {
		signDate = EpayUtil.getString(httpObserver.getResult(resultObj), "");
		signDate = EpayUtil.converSystemTime(signDate);
		HashMap<Object, Object> params = new HashMap<Object, Object>();
		params.put(PubConstants.PUB_FIELD_COMBIN_ID, combinId);
		params.put(TreatyConstants.METHOD_DELETE_RELATIONS_PRE_FIELD_MERCHANT_NO, bocNo);
		params.put(TreatyConstants.METHOD_DELETE_RELATIONS_PRE_FIELD_AGREEMENT_ID, treatyNum);
		params.put(TreatyConstants.METHOD_DELETE_RELATIONS_PRE_FIELD_HOLDER_MER_ID, merchantAcc);
		params.put(TreatyConstants.METHOD_DELETE_RELATIONS_PRE_FIELD_MERCHANT_NAME, merchantName);
		params.put(TreatyConstants.METHOD_DELETE_RELATIONS_PRE_FIELD_CARD_NO, payAccount);
		params.put(TreatyConstants.METHOD_DELETE_RELATIONS_PRE_FIELD_CARD_TYPE, payAccountType);
		params.put(TreatyConstants.METHOD_DELETE_RELATIONS_PRE_FIELD_DAILY_QUOTA, addUpQuota);
		params.put(TreatyConstants.METHOD_DELETE_RELATIONS_PRE_FIELD_SIGN_DATE, signDate);
		httpObserver.req_deleteMerchantRelationPre(params, "deleteMerchantRelationPreCallback");
	}

	public void deleteMerchantRelationPreCallback(Object resultObj) {
		Object result = httpObserver.getResult(resultObj);
//		Map<String, Object> resultMap = EpayUtil.getMap(result);
		Map<String, Object> resultMap = EpayUtil.getMapResponse(result);
//		List<Object> factorList = EpayUtil.getFactorList(resultMap);

		BaseHttpEngine.dissMissProgressDialog();
		showDeleteMerchantConfirmDialog(resultMap);
	}

	private void showDeleteMerchantConfirmDialog(final Map<String, Object> resultMap) {
		deleteMerchantConfirmDialog = new DeleteMerchantConfirmDialog(signDate, resultMap, merchant, this);
		deleteMerchantConfirmDialog.setErrorOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BaseDroidApp.getInstanse().dismissMessageDialog();
				showDeleteMerchantConfirmDialog(resultMap);
			}
		});
	}

	// 修改键盘弹出顶起问题
	@Override
	public void afterKeyboardHidden(SipBox arg0, int arg1) {
		if (deleteMerchantConfirmDialog != null)
			deleteMerchantConfirmDialog.getView().scrollTo(0, 0);
	}

	@Override
	public void beforeKeyboardShow(SipBox sipbox, int keyboardHeight) {
		// 判断弹出的安全键盘是否会遮掩输入框
		int[] location = new int[2];
		sipbox.getLocationOnScreen(location);
		int y = location[1];
		// 距底距离
		int bottom = getWindowHeight() - y - sipbox.getHeight();
		if (bottom < keyboardHeight) {// 说明遮掩
			if (deleteMerchantConfirmDialog != null)
				deleteMerchantConfirmDialog.getView().scrollTo(0, keyboardHeight - bottom);
		}
	}

	private void getTransData() {
		merchant = treatyContext.getMap(TreatyConstants.PUB_FEILD_SELECTED_MERCHANT);

		bocNo = EpayUtil.getString(merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_BOC_NO), "");
		treatyNum = EpayUtil.getString(merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_AGREEMENT_ID),
				"");
		custName = EpayUtil
				.getString(merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_HOLDER_NAME), "");
		identityType = EpayUtil.getString(
				merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_IDENTITY_TYPE), "");
		identityNum = EpayUtil.getString(
				merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_IDENTITY_NUMBER), "");
		merchantAcc = EpayUtil.getString(
				merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_HOLDER_MERID), "");
		merchantName = EpayUtil.getString(
				merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_MERCHANT_NAME), "");
		payAccount = EpayUtil.getString(merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_CARD_NO), "");
		payAccountType = EpayUtil.getString(
				merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_CARD_TYPE), "");
		addUpQuota = EpayUtil.getString(merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_DAILY_QUOTA),
				"");

		curTreatyStatus = treatyStatus = EpayUtil.getString(
				merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_STATUS), "");
		contractDate = EpayUtil.getString(merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_SIGN_DATE),
				"");
		contractChannel = EpayUtil.getString(
				merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_SIGN_CHANNEL), "");
		contractMobileNum = EpayUtil.getString(
				merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_MOBILE_NUMBER), "");
		contractClient = EpayUtil.getString(
				merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_VERIFY_MODTERMINAL_FLAG), "");
		initCurPage();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESET_DATA:
			treatyContext.setRightButtonClick(false);
			setResult(RESET_DATA);
			String newDateQuota = TransContext.getTreatyTransContext().getString("newDailyQuota", addUpQuota);
			if (StringUtil.isNullOrEmpty(newDateQuota))
				tv_addup_quota.setText("-");
			else
				tv_addup_quota.setText(StringUtil.parseStringPattern(newDateQuota, 2));
			break;
		}
	}

	@Override
	public void finish() {
		treatyContext.setRightButtonClick(false);
		if (!treatyStatus.equals(curTreatyStatus)) // 协议状态改变，需要刷新页面数据
			setResult(RESET_DATA);
		super.finish();
	}

	public void setTv_treaty_status(TextView tv_treaty_status) {
		this.tv_treaty_status = tv_treaty_status;
	}

	public TextView getTv_treaty_status() {
		return tv_treaty_status;
	}

	public void setTreatyStatus(String treatyStatus) {
		this.treatyStatus = treatyStatus;
	}

	public String getTreatyStatus() {
		return treatyStatus;
	}

	public void refreshTerminatePage() {
		// 如果解约隐藏底部按钮
		findViewById(R.id.ll_button).setVisibility(View.GONE);
		tv_treaty_status.setText(TreatyConstants.TREATY_STATUS.get("D"));
	}

}
