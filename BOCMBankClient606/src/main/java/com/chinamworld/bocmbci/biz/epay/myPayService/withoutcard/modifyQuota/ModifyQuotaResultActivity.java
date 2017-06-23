package com.chinamworld.bocmbci.biz.epay.myPayService.withoutcard.modifyQuota;

import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.epay.EPayBaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.constants.WithoutCardContants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 电子支付开通-账户选择页
 * 
 */
public class ModifyQuotaResultActivity extends EPayBaseActivity {

	private String tag = "EPayModifyQuotaResultActivity";

	private View ePayModifyQuotaResult;

	private TextView tv_acc_number;
	// private TextView tv_acc_type;
	// private TextView tv_acc_nickname;
	private TextView tv_currency;
	private TextView tv_cust_max_quota_txt;
	private TextView tv_cust_max_quota;
	private TextView mMobile;
	private TextView mTipMsg;
	private LinearLayout mLayoutMobile;

	private Button bt_sure;

	private String accNumber;
	private String accType;
	private String accNickname;
	private String custMaxQuota;
	private String currency;
	private String cifMobile;

	private Context withoutCardTransContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		withoutCardTransContext = TransContext.getWithoutCardContext();
		ePayModifyQuotaResult = LayoutInflater.from(this).inflate(R.layout.epay_wc_modify_quota_result, null);

		super.setType(0);
		super.setShowBackBtn(false);
		if (serviceType == 1) {
			super.setTitleName(PubConstants.TITLE_SECOND_PAY);
		} else if (serviceType == 2) {
			super.setTitleName(PubConstants.TITLE_SECOND_RECEVICE);
		}
//		super.setTitleName(PubConstants.TITLE_WITHOUT_CARD);
		super.setContentView(ePayModifyQuotaResult);
		super.onCreate(savedInstanceState);
		super.hideFoot();
//		super.hideRightButton();
		setLeftButtonPopupGone();
		getIntentData();
		// 初始化导航条
		// EpayPubUtil.initStepBar(this, 3, new String[]{"修改限额","确认信息","修改成功"});
		getTransData();
	}
	
	private void getIntentData(){
		cifMobile = getIntent().getStringExtra(WithoutCardContants.GIF_MOBILE);
	}

	/**
	 * 获取交易数据
	 */
	@SuppressWarnings("unchecked")
	private void getTransData() {
		BiiHttpEngine.showProgressDialog();
		Map<Object, Object> selectedAccount = withoutCardTransContext.getMap(PubConstants.CONTEXT_FIELD_SELECTED_ACC);
		accType = EpayUtil.getString(selectedAccount.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_TYPE), "");
		accNickname = EpayUtil.getString(selectedAccount.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_NICKNAME), "");
		accNumber = EpayUtil.getString(selectedAccount.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NUMBER), "");
		currency = EpayUtil.getString(selectedAccount.get(PubConstants.METHOD_QUERY_ACCOUNT_DETAIL_FIELD_CURRENCY_CODE), "");
		custMaxQuota = withoutCardTransContext.getString(WithoutCardContants.METHOD_OPEN_NC_PAY_PRE_FIELD_CURRENT_QUOTA, "");
		if(serviceType==1){
			selectedAccount.put(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_QUOTA, custMaxQuota);
		}else if(serviceType==2){
			selectedAccount.put(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_COLLECTQUOTA, custMaxQuota);
		}
		
		String selectedAccountId = EpayUtil.getString(selectedAccount.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_ID), "");
		for (int i = 0; i < withoutCardTransContext.getList(PubConstants.CONTEXT_FIELD_DREDGED_LIST).size(); i++) {
			Map<Object, Object> account = EpayUtil.getMap(withoutCardTransContext.getList(PubConstants.CONTEXT_FIELD_DREDGED_LIST).get(i));
			String accId = EpayUtil.getString(account.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_ID), "");
			if (selectedAccountId.equals(accId)) {
				account.put(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_QUOTA, custMaxQuota);
				((Map<Object, Object>) withoutCardTransContext.getList(PubConstants.CONTEXT_FIELD_DREDGED_LIST).get(i)).put(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_QUOTA, custMaxQuota);
				break;
			}
		}

		withoutCardTransContext.clear(WithoutCardContants.METHOD_OPEN_NC_PAY_PRE_FIELD_CURRENT_QUOTA);
		withoutCardTransContext.clear(PubConstants.PUB_FIELD_CONVERSATION_ID);
		withoutCardTransContext.clear(PubConstants.CONTEXT_FIELD_SELECTED_ACC);
		withoutCardTransContext.clear(PubConstants.PUB_FIELD_FACTORLIST);

		// 初始化当前页
		initCurPage();
	}

	private void initCurPage() {
		
		TextView  tv_msg =(TextView)ePayModifyQuotaResult.findViewById(R.id.tv_msg);
		if (serviceType == 1) {
			tv_msg.setText(getResources().getString(R.string.epay_wc_modify_result_tv_msg1));
		} else if (serviceType == 2) {
			tv_msg.setText(getResources().getString(R.string.epay_wc_modify_result_tv_msg2));
		}
		
		// tv_acc_type = (TextView)
		// ePayModifyQuotaResult.findViewById(R.id.tv_acc_type);
		tv_acc_number = (TextView) ePayModifyQuotaResult.findViewById(R.id.tv_acc_number);
		// tv_acc_nickname = (TextView)
		// ePayModifyQuotaResult.findViewById(R.id.tv_acc_nickname);
		tv_currency = (TextView) ePayModifyQuotaResult.findViewById(R.id.tv_currency);
		tv_cust_max_quota_txt= (TextView) ePayModifyQuotaResult.findViewById(R.id.tv_cust_max_quota_txt);
		if (serviceType == 1) {
			tv_cust_max_quota_txt.setText(getResources().getString(R.string.epay_wc_spa_tv_day_max_quota_text));
		} else if (serviceType == 2) {
			tv_cust_max_quota_txt.setText(getResources().getString(R.string.epay_wc_spa_tv_day_max_collectquota_text));
		}
		tv_cust_max_quota = (TextView) ePayModifyQuotaResult.findViewById(R.id.tv_cust_max_quota);
		mLayoutMobile = (LinearLayout) ePayModifyQuotaResult.findViewById(R.id.layout_mobile);
		mMobile = (TextView) ePayModifyQuotaResult.findViewById(R.id.mobile);
		mTipMsg = (TextView) ePayModifyQuotaResult.findViewById(R.id.tv_prompt_msg);

		bt_sure = (Button) findViewById(R.id.bt_ensure);
		bt_sure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});

		initDisplay();
		controlCifMobile();
	}
	
	/** 显示核心客户手机号  pwe*/
	private void controlCifMobile(){
		if (!StringUtil.isNull(cifMobile)) {
			mMobile.setText(StringUtil.getThreeFourThreeString(cifMobile));
			if (serviceType == 1) {
				mTipMsg.setText(R.string.epay_cifmobile_tip);
			} else if (serviceType == 2) {
				mTipMsg.setText(R.string.epay_cifmobile_tip2);
			}
			
			mLayoutMobile.setVisibility(View.VISIBLE); return;
		}
	
		if (serviceType == 1) {
			mTipMsg.setText(R.string.epay_nocifmobile_tip);
		} else if (serviceType == 2) {
			mTipMsg.setText(R.string.epay_nocifmobile_tip2);
		}
	}

	/**
	 * 设置显示内容
	 */
	private void initDisplay() {
		// tv_acc_type.setText(LocalData.AccountType.get(accType));
		tv_acc_number.setText(StringUtil.getForSixForString(accNumber));
		// tv_acc_nickname.setText(accNickname);
		tv_currency.setText(LocalData.Currency.get(currency));
		tv_cust_max_quota.setText(StringUtil.parseStringPattern(custMaxQuota, 2));
		BiiHttpEngine.dissMissProgressDialog();
	}

	@Override
	public void finish() {
		setResult(RESULT_OK);
		super.finish();
		overridePendingTransition(R.anim.no_animation, R.anim.n_pop_exit_bottom_down);
	}

}
