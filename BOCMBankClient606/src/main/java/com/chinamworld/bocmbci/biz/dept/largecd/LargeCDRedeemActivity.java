package com.chinamworld.bocmbci.biz.dept.largecd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 支取大额存单确认页面
 * 
 * @author liuh
 * 
 */
public class LargeCDRedeemActivity extends DeptBaseActivity {
	private LinearLayout tabcontent;
	private Map<String, Object> signedAcc;
	/** 已购买大额存单明细 */
	private Map<String, Object> purchasedDetail;
	/** 存单编号 */
	private TextView cdNumberTv;
	/** 存单定期账号 */
	private TextView accTimeNumberTv;
	/** 签约账户 */
	private TextView accNumberTv;
	/** 支取金额 */
	private TextView saveTv;
	/** 利息 */
	private TextView interestTv;
	private Button confirmBtn;
	private String conversationId;
	/** 利息 */
	private String interest;
	
	// add by luqp 2016年3月4日 确认页面修改
	/** 产品编码 */
	private TextView productCodeTv;
	/** 新增日期 */
	private TextView startDateTv;
	/** 新增日期*/
	private String startDate;
	/** 到期日期 */
	private TextView endDateTv;
	/** 到期日期 */
	private String endDate;
	/** 利率 */
	private TextView rateTv;
	/** 标题*/
	private TextView titleText;
	/** 账户类型*/
	private TextView accType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();

		if (!getIntentData()) {
			setViews();
			setListeners();
		} else {
			finish();
		}
	}

	private void setListeners() {
		confirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				BaseHttpEngine.dissmissCloseOfProgressDialog();
				requestCommConversationId();
			}
		});
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		conversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		requestPSNGetTokenId(conversationId);
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		requestForRedeemSubmit();
	}

	private void requestForRedeemSubmit() {
		String balance = String.valueOf(purchasedDetail.get(Dept.CD_BALANCE));
		String tokenId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID));
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.PSN_LARGE_CD_REDEEM_SUBMIT);
		biiRequestBody.setConversationId(conversationId);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Dept.ACCOUNT_ID, String.valueOf(signedAcc.get(Dept.ACCOUNT_ID)));
		map.put(Dept.CD_NUMBER, String.valueOf(purchasedDetail.get(Dept.CD_NUMBER)));
		map.put(Dept.CD_ACC_NUMBER, String.valueOf(purchasedDetail.get(Dept.CD_ACC_NUMBER)));
		map.put(Dept.AMOUNT, StringUtil.parseStringPattern(balance, 2));
		map.put(Dept.CURRENCY, ConstantGloble.CURRENCY);
		map.put(Dept.CASHREMIT, ConstantGloble.CASHREMIT_00);
		map.put(Comm.TOKEN_REQ, tokenId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "redeemSubmitCallBack");
	}

	@SuppressWarnings("unchecked")
	public void redeemSubmitCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		Intent intent = new Intent();
		intent.setClass(LargeCDRedeemActivity.this, LargeCDRedeemResultActivity.class);
		intent.putExtra(Dept.TRANSACTION_ID, String.valueOf(result.get(Dept.TRANSACTION_ID)));
		intent.putExtra(Dept.STATUS, String.valueOf(result.get(Dept.STATUS)));
		intent.putExtra(Dept.INTEREST, String.valueOf(interest));
		startActivity(intent);
	}

	private void setViews() {
		cdNumberTv = (TextView) findViewById(R.id.tv_large_cd_number);
		// ===========================================================
		// add by luqp 2016年3月25日 显示标题 修改标题
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setVisibility(View.VISIBLE);
		titleText.setText("请确认以下信息");
		// ===========================================================
		accTimeNumberTv = (TextView) findViewById(R.id.tv_large_cd_acc_time_number);
		accNumberTv = (TextView) findViewById(R.id.tv_large_cd_acc_number);
		saveTv = (TextView) findViewById(R.id.tv_large_cd_save_money);
		interestTv = (TextView) findViewById(R.id.tv_large_cd_interest);
		confirmBtn = (Button) findViewById(R.id.btn_confirm);
		
		// add by luqp 2016年5月3日   账户后面添加账户类型    如:1005****8561 活期一本通 
		accType = (TextView) findViewById(R.id.tv_acc_type);
		String accTypeStr = StringUtil.valueOf1((String) signedAcc.get(Dept.LargeSign_accountType));
		String accountType =LocalData.AccountType.get(accTypeStr);
		accType.setText(accountType);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accType);
		
		//////////////////////////////////////////////////////////////////////////
		// add by luqp 2016年3月4日 页面修改
		//产品编码 
		productCodeTv = (TextView) findViewById(R.id.large_cd_product_code_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, productCodeTv);
		productCodeTv.setText(StringUtil.valueOf1((String) purchasedDetail.get(Dept.PRODUCT_CODE)));
		//新增日期
		startDateTv = (TextView) findViewById(R.id.large_cd_start_date);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, startDateTv);
		startDate = StringUtil.valueOf1((String) purchasedDetail.get(Dept.BUY_DATE));
		startDateTv.setText(startDate);	
		// 到期日期 
		endDateTv = (TextView) findViewById(R.id.large_cd_end_date_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, endDateTv);
		endDate = StringUtil.valueOf1((String) purchasedDetail.get(Dept.BUY_END_DATE));
		endDateTv.setText(endDate);	
		// 利率 
		rateTv = (TextView) findViewById(R.id.large_cd_rate_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, rateTv);
		String rate = StringUtil.valueOf1((String) purchasedDetail.get(Dept.RATE));
		rateTv.setText(rate + "%"); // add by luqp 2016年3月4日 追加%号
		///////////////////////////////////////////////////////////////////////////
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, cdNumberTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accTimeNumberTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accNumberTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, saveTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, interestTv);

		String cdBalance = StringUtil.valueOf1((String) purchasedDetail.get(Dept.CD_BALANCE));
		cdNumberTv.setText(StringUtil.valueOf1((String) purchasedDetail.get(Dept.CD_NUMBER)));
		accTimeNumberTv.setText(StringUtil.valueOf1((String) purchasedDetail.get(Dept.CD_ACC_NUMBER)));
		String accNumber = StringUtil.getForSixForString(StringUtil.valueOf1((String) signedAcc.get(Dept.ACCOUNT_NUMBER)));
		accNumberTv.setText(accNumber);
		saveTv.setText(StringUtil.parseStringPattern(cdBalance, 2));
		interestTv.setText(StringUtil.parseStringPattern(interest, 2));
	}

	private boolean getIntentData() {
		Intent intent = getIntent();
		interest = String.valueOf(intent.getDoubleExtra(Dept.INTEREST, 0.0));
		return getSignedAcc() & getPurchasedDetail();
	}

	/**
	 * 获取已购买大额存单明细
	 * 
	 * @return
	 */
	private boolean getPurchasedDetail() {
		purchasedDetail = DeptDataCenter.getInstance().getPurchasedDetail();
		return StringUtil.isNullOrEmpty(purchasedDetail);
	}

	/**
	 * 获取签约账户信息
	 * 
	 * @return
	 */
	private boolean getSignedAcc() {
		signedAcc = (Map<String, Object>) DeptDataCenter.getInstance().getSignedAcc();
		return StringUtil.isNullOrEmpty(signedAcc);
	}

	@SuppressLint("InflateParams")
	private void initViews() {
		setTitle(getString(R.string.large_cd_redeem_title));
		View view = mInflater.inflate(R.layout.large_cd_redeem, null);
		tabcontent = (LinearLayout) findViewById(R.id.sliding_body);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 0);
		}
	}
}
