package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCProductForHoldingInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.RedeemInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.RedeemVerifyInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment.RedeemAffirmNoValueFragment;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment.RedeemAffirmValueFragment;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.util.HoldingBOCProductInfoUtil;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 理财产品赎回确认页面
 * 
 * @author HVZHUNG
 *
 */
public class RedeemAffirmActivity extends BocInvtBaseActivity {

	private static final String TAG = RedeemAffirmActivity.class
			.getSimpleName();
	private static final String KEY_PRODUCT_INFO = "product_info";
	private static final String KEY_VERIFY_INFO = "verify_info";
	private RedeemVerifyInfo verifyInfo;
	private BOCProductForHoldingInfo productInfo;

	private Button bt_confirm;
	private String cashRemit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bocinvt_redeem_affirm_activity);
		bt_confirm = (Button) findViewById(R.id.bt_confirm);
		bt_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pSNGetTokenId();
			}
		});
		setTitle(R.string.bocinvt_holding_detail_redeem);
//		getBackgroundLayout().setRightButtonText(null);
		if (savedInstanceState == null) {
			productInfo = (BOCProductForHoldingInfo) getIntent()
					.getSerializableExtra(KEY_PRODUCT_INFO);
			verifyInfo = (RedeemVerifyInfo) getIntent()
					.getSerializableExtra(KEY_VERIFY_INFO);
			if (productInfo == null || verifyInfo == null) {
				LogGloble
						.e(TAG, "Extra product info or verify info is null!!!");
				return;
			}
			cashRemit = productInfo.cashRemit;
			Fragment f = null;
			if (HoldingBOCProductInfoUtil.isValueType(productInfo)) {
				f = RedeemAffirmValueFragment.newInstance(productInfo,
						verifyInfo);
			} else {
				f = RedeemAffirmNoValueFragment.newInstance(productInfo,
						verifyInfo);
			}
			// if (f == null) {
			// LogGloble.e(TAG, "没有合适的Fragment展示该产品的赎回确认页面");
			// return;
			// }
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, f).commit();
		}
	}

	/**
	 * 获取tokenId
	 */
	public void pSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		BiiHttpEngine.showProgressDialog();
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
		if (StringUtil.isNull(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		// 请求持有产品赎回结果
		requestXpadHoldProductAndRedeem(tokenId);
	}

	/** 请求持有产品赎回结果 */
	public void requestXpadHoldProductAndRedeem(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody
				.setMethod(BocInvt.BOCINVT_PSNXPADHOLDPRODUCTANDREDEEM_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(BocInvt.BOCINVT_REDEEM_TOKEN_REQ, tokenId);
		// paramsmap.put(BocInvt.DEALCODE,
		// (String)redeemVerifyMap.get(BocInvt.DEALCODE));
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestXpadHoldProductAndRedeemCallback");
	}

	/** 请求持有产品赎回结果回调 */
	public void requestXpadHoldProductAndRedeemCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		startActivity(RedeemSuccessActivity.getIntent(this, new RedeemInfo(
				resultMap),cashRemit));
	}

	public static Intent getIntent(Context context,
			BOCProductForHoldingInfo productInfo, RedeemVerifyInfo verifyInfo) {

		Intent intent = new Intent(context, RedeemAffirmActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(KEY_PRODUCT_INFO, productInfo);
		intent.putExtra(KEY_VERIFY_INFO, verifyInfo);
		return intent;
	}
}
