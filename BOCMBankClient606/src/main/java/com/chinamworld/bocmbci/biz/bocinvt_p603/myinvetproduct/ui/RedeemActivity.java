package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCProductForHoldingInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.RedeemVerifyInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment.BaseRedeemFragment;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 中银理财产品赎回页面
 * 
 * @author HVZHUNG
 *
 */
public class RedeemActivity extends BocInvtBaseActivity implements
		OnClickListener {

	private static final String TAG = RedeemActivity.class.getSimpleName();
	private static final String KEY_INFO = "info";
	private ViewGroup content;
	/** 下一步按钮 */
	private Button bt_next;

	private String redeemQuantity;
	private String redeemDate;

	private BOCProductForHoldingInfo info;

	/** 份额明细 */
	private Map<String, Object> quantityDetailMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bocinvt_redeem_activity_p603);
		setTitle(R.string.bocinvt_holding_detail_redeem);
//		getBackgroundLayout().setRightButtonText(null);
		content = (ViewGroup) findViewById(R.id.content);
		quantityDetailMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_QUANTITY_DETAIL_LIST);
		bt_next = (Button) findViewById(R.id.btn_next);
		bt_next.setOnClickListener(this);
		if (savedInstanceState == null) {
			info = (BOCProductForHoldingInfo) getIntent().getSerializableExtra(
					KEY_INFO);
			if (info == null) {
				LogGloble.e(TAG, "BOCProductInfo is null");

				return;
			}

			BaseRedeemFragment newInstance = BaseRedeemFragment
					.newInstance(info);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content, newInstance).commit();

		}
	}

	@Override
	public void onClick(View v) {
		Fragment contentFragment = getSupportFragmentManager()
				.findFragmentById(R.id.content);
		if (contentFragment == null
				|| !(contentFragment instanceof BaseRedeemFragment)) {
			return;
		}
		BaseRedeemFragment instance = (BaseRedeemFragment) contentFragment;

		if (!instance.checkInput()) {
			return;
		}

		redeemDate = instance.getRedeemDate();
		redeemQuantity = instance.getRedeemQuantity();
		BiiHttpEngine.showProgressDialog();
		requestCommConversationId();
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);

		pSNGetTokenId();

	}

	/**
	 * 获取tokenId
	 */
	public void pSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));

		HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenId");
	}

	/**
	 * 处理获取tokenId的数据得到tokenId
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void aquirePSNGetTokenId(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		// 请求持有产品赎回预交易
		requestPsnXpadHoldProductRedeemVerify(tokenId);
	}

	/**
	 * 请求持有产品赎回预交易
	 * 
	 * @param redeemQuantity
	 *            赎回份额
	 * */
	public void requestPsnXpadHoldProductRedeemVerify(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody
				.setMethod(BocInvt.BOCINVT_PSNXPADHOLDPRODUCTREDEEMVERIFY_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		// paramsmap.put(BocInvt.BOCINVT_REDVER_CASHREMIT_REQ, String
		// .valueOf(myproductMap
		// .get(BocInvt.BOCINVT_HOLDPRO_CASHREMIT_RES)));
		paramsmap
				.put(BocInvt.BOCINVT_REDVER_REDEEMQUANTITY_REQ, redeemQuantity);
		paramsmap.put(BocInvt.BOCINVT_REDVER_PRODCODE_REQ, info.prodCode);
		paramsmap.put(BocInvt.BOCINVT_REDVER_TOKEN_REQ, tokenId);
		paramsmap.put(BocInvt.BOCINVT_SIGNRESULT_XPADCASHREMIT_REQ, info.cashRemit);
		paramsmap.put("accountKey",info.bancAccountKey);
		paramsmap.put("productKind",info.productKind);
			paramsmap.put("redeemDate", redeemDate);
		if(!StringUtil.isNullOrEmpty(quantityDetailMap)){
			paramsmap.put("tranSeq",(String)quantityDetailMap.get("tranSeq"));
		}
		if (!TextUtils.isEmpty(redeemQuantity)) {
			paramsmap.put("redeemQuantity", redeemQuantity);
		}
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnXpadHoldProductRedeemVerifyCallback");
	}

	/** 请求持有产品赎回预交易回调 */
	public void requestPsnXpadHoldProductRedeemVerifyCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		final Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}

		final RedeemVerifyInfo verifyInfo = new RedeemVerifyInfo(resultMap);
		final String tranflag = (String) resultMap
				.get(BocInvt.BOCINVT_REDVER_TRANFLAG_RES);
		if(info.canPartlyRedeem.equals("0")){//是否允许部分赎回0：是 1：否
			if (LocalData.tranFlagMap.get(tranflag).equals(
					ConstantGloble.BOCINVT_TRANFLAG)) {
				BaseDroidApp.getInstanse().showErrorDialog(
						this.getString(R.string.bocinvt_error_orderTime),
						R.string.cancle, R.string.confirm, new OnClickListener() {
							@Override
							public void onClick(View v) {
								switch (Integer.parseInt(v.getTag() + "")) {
								case CustomDialog.TAG_SURE:
									BaseDroidApp.getInstanse().dismissErrorDialog();
									BociDataCenter.getInstance().setRedeemVerify(
											resultMap);
									Intent intent = new Intent(RedeemActivity.this,
											RedeemAffirmActivity.class);
									startActivity(RedeemAffirmActivity.getIntent(
											RedeemActivity.this, info, verifyInfo));
									break;
								case CustomDialog.TAG_CANCLE:
									BaseDroidApp.getInstanse().dismissErrorDialog();
									break;
								}
							}
						});
			} else {
				BociDataCenter.getInstance().setRedeemVerify(resultMap);

				startActivity(RedeemAffirmActivity
						.getIntent(this, info, verifyInfo));
			}
		}else{
			BaseDroidApp.getInstanse().showErrorDialog(
					getString(R.string.bocinvt_error_redeem),
					R.string.cancle, R.string.confirm, new OnClickListener() {
						@Override
						public void onClick(View v) {
							switch (Integer.parseInt(v.getTag() + "")) {
							case CustomDialog.TAG_SURE:
								BaseDroidApp.getInstanse().dismissErrorDialog();
								if (LocalData.tranFlagMap.get(tranflag).equals(
										ConstantGloble.BOCINVT_TRANFLAG)) {
									BaseDroidApp.getInstanse().showErrorDialog(
											getString(R.string.bocinvt_error_orderTime),
											R.string.cancle, R.string.confirm, new OnClickListener() {
												@Override
												public void onClick(View v) {
													switch (Integer.parseInt(v.getTag() + "")) {
													case CustomDialog.TAG_SURE:
														BaseDroidApp.getInstanse().dismissErrorDialog();
														BociDataCenter.getInstance().setRedeemVerify(
																resultMap);
														Intent intent = new Intent(RedeemActivity.this,
																RedeemAffirmActivity.class);
														startActivity(RedeemAffirmActivity.getIntent(
																RedeemActivity.this, info, verifyInfo));
														break;
													case CustomDialog.TAG_CANCLE:
														BaseDroidApp.getInstanse().dismissErrorDialog();
														break;
													}
												}
											});
								} else {
									BociDataCenter.getInstance().setRedeemVerify(resultMap);

									startActivity(RedeemAffirmActivity
											.getIntent(RedeemActivity.this, info, verifyInfo));
								}
								break;
							case CustomDialog.TAG_CANCLE:
								BaseDroidApp.getInstanse().dismissErrorDialog();
								break;
							}
						}
					});
		}
	}

	public static Intent getIntent(Context context, BOCProductForHoldingInfo info) {
		Intent intent = new Intent(context, RedeemActivity.class);
		intent.putExtra(KEY_INFO, info);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return intent;
	}
}
