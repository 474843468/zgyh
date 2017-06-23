package com.chinamworld.bocmbci.biz.epay.myPayService.withoutcard.setPaymentAcc;

import java.util.List;
import java.util.Map;

import android.content.Intent;
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
import com.chinamworld.bocmbci.biz.epay.myPayService.withoutcard.WithoutCardActivity;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

public class SetPaymentAccResultActivity extends EPayBaseActivity {

	private View ePaySetPaymentAccResult;
	private Button bt_finish;

	// private TextView tv_acc_type;
	// private TextView tv_acc_nickname;
	private TextView tv_acc_number;
	private TextView tv_currency;
	private TextView tv_currentQuota_txt;
	private TextView tv_currentQuota;
	private TextView mMobile;
	private TextView mTipMsg;
	private LinearLayout mLayoutMobile;

	private String acc_type;
	private String acc_nickname;
	private String acc_number;
	private String currency;
	private String currentQuota;
	private String cifMobile;

	private Map<Object, Object> selectedAccount;

	private Context withoutCardTransContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		withoutCardTransContext = TransContext.getWithoutCardContext();
		ePaySetPaymentAccResult = LayoutInflater.from(this).inflate(R.layout.epay_wc_spa_result, null);
		
		super.setType(0);
		super.setShowBackBtn(false);
		if (serviceType == 1) {
			super.setTitleName(PubConstants.TITLE_SECOND_PAY);
		} else if (serviceType == 2) {
			super.setTitleName(PubConstants.TITLE_SECOND_RECEVICE);
		}
		super.setContentView(ePaySetPaymentAccResult);
		super.onCreate(savedInstanceState);
		getIntentData();
		
		// 初始化导航条
		// EpayPubUtil.initStepBar(this, 1, new String[]{"开通结果","",""});
		getTransData();
	}
	
	private void getIntentData(){
		cifMobile = getIntent().getStringExtra(WithoutCardContants.GIF_MOBILE);
	}
	
	/**
	 * 获取交易数据
	 */
	private void getTransData() {
		selectedAccount = withoutCardTransContext.getMap(PubConstants.CONTEXT_FIELD_SELECTED_ACC);
		acc_type = EpayUtil.getString(selectedAccount.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_TYPE), "");
		acc_nickname = EpayUtil.getString(selectedAccount.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_NICKNAME), "");
		acc_number = EpayUtil.getString(selectedAccount.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NUMBER), "");
		currency = EpayUtil.getString(selectedAccount.get(PubConstants.METHOD_QUERY_ACCOUNT_DETAIL_FIELD_CURRENCY_CODE), "");
		currentQuota = withoutCardTransContext.getString(WithoutCardContants.METHOD_OPEN_NC_PAY_PRE_FIELD_CURRENT_QUOTA, "");
		selectedAccount.put(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_QUOTA, currentQuota);
		withoutCardTransContext.setData(PubConstants.CONTEXT_FIELD_SELECTED_ACC, selectedAccount);
		// 清理交易数据
		withoutCardTransContext.clear(WithoutCardContants.METHOD_OPEN_NC_PAY_PRE_FIELD_CURRENT_QUOTA);
		withoutCardTransContext.clear(PubConstants.CONTEXT_FIELD_SELECTED_ACC);
		withoutCardTransContext.clear(PubConstants.PUB_FIELD_CONVERSATION_ID);
		
		// 初始化当前页
		initCurPage();
	}

	private void initCurPage() {
		
		
		
		TextView  tv_msg =(TextView)ePaySetPaymentAccResult.findViewById(R.id.tv_msg);
		if (serviceType == 1) {
			tv_msg.setText(getResources().getString(R.string.epay_wc_spa_result_tv_msg1));
		} else if (serviceType == 2) {
			tv_msg.setText(getResources().getString(R.string.epay_wc_spa_result_tv_msg2));
		}
		// tv_acc_type =
		// (TextView)ePaySetPaymentAccResult.findViewById(R.id.tv_acc_type);
		// tv_acc_nickname =
		// (TextView)ePaySetPaymentAccResult.findViewById(R.id.tv_acc_nickname);
		tv_acc_number = (TextView) ePaySetPaymentAccResult.findViewById(R.id.tv_acc_number);
		tv_currentQuota_txt= (TextView) ePaySetPaymentAccResult.findViewById(R.id.tv_currentQuota_txt);
		if (serviceType == 1) {
			tv_currentQuota_txt.setText(getResources().getString(R.string.epay_wc_spa_tv_day_max_quota_text));
		} else if (serviceType == 2) {
			tv_currentQuota_txt.setText(getResources().getString(R.string.epay_wc_spa_tv_day_max_collectquota_text));
		}
		tv_currentQuota = (TextView) ePaySetPaymentAccResult.findViewById(R.id.tv_currentQuota);
		tv_currency = (TextView) ePaySetPaymentAccResult.findViewById(R.id.tv_currency);
		mLayoutMobile = (LinearLayout) ePaySetPaymentAccResult.findViewById(R.id.layout_mobile);
		mMobile = (TextView) ePaySetPaymentAccResult.findViewById(R.id.mobile);
		mTipMsg = (TextView) ePaySetPaymentAccResult.findViewById(R.id.tv_prompt_msg);

		bt_finish = (Button) ePaySetPaymentAccResult.findViewById(R.id.bt_finish);
		bt_finish.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(SetPaymentAccResultActivity.this,WithoutCardActivity.class);
				startActivity(intent);
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
		// tv_acc_type.setText(LocalData.AccountType.get(acc_type));
		// tv_acc_nickname.setText(acc_nickname);
		tv_acc_number.setText(StringUtil.getForSixForString(acc_number));
		tv_currentQuota.setText(StringUtil.parseStringPattern(currentQuota, 2));
		tv_currency.setText(LocalData.Currency.get(currency));
		addDredgedAccList();
	}

	private void removeDredgedAccList(String accountId) {
		withoutCardTransContext.setData(PubConstants.CONTEXT_FIELD_UN_DREDGED_ACCOUNT_LIST, EpayUtil.filterAccList(withoutCardTransContext.getList(PubConstants.CONTEXT_FIELD_UN_DREDGED_ACCOUNT_LIST), accountId));
	}

	/**
	 * 将账户添加到已开通账户列表
	 */
	private void addDredgedAccList() {
		String accId = EpayUtil.getString(selectedAccount.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_ID), "");
		if (withoutCardTransContext.getList(PubConstants.CONTEXT_FIELD_DREDGED_LIST).isEmpty()) {
			withoutCardTransContext.getList(PubConstants.CONTEXT_FIELD_DREDGED_LIST).add(selectedAccount);
		} else {
			List<Object> newDredgedList = EpayUtil.filterAccList(withoutCardTransContext.getList(PubConstants.CONTEXT_FIELD_DREDGED_LIST), accId);
			newDredgedList.add(selectedAccount);
			withoutCardTransContext.setData(PubConstants.CONTEXT_FIELD_DREDGED_LIST, newDredgedList);
		}
		// 剔除已开通账户
		removeDredgedAccList(accId);
	}
	
	@Override
	public void finish() {
		setResult(RESULT_OK);
		super.finish();
	}
	
}
