package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCProductForHoldingInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BaseReferProfitInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment.ReferenceProfitFragmentFactory;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 产品的参考收益显示页面
 * 
 * @author HVZHUNG
 *
 */
public class ReferenceProfitActivity extends BocInvtBaseActivity {

	private static final String TAG = ReferenceProfitActivity.class
			.getSimpleName();
	private static final String EXTRA_KEY_INFO = "info";

	private BOCProductForHoldingInfo info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bocinvt_reference_profit_view);
		setTitle(R.string.bocinvt_reference_yield);
		//getBackgroundLayout().setRightButtonText(null);
		info = (BOCProductForHoldingInfo) getIntent().getSerializableExtra(
				EXTRA_KEY_INFO);
		if (info == null) {
			LogGloble.e(TAG, "BOCProductForHoldingInfo is null !!! ");
			return;
		}

		requestReferenceProfitInfo(info);
		BiiHttpEngine.showProgressDialogCanGoBack();
	}

	/**
	 * 显示具体的参考收益fragment
	 * 
	 * @param baseReferProfitInfo
	 */
	private void showReFerenceYieldFragment(
			BaseReferProfitInfo baseReferProfitInfo) {

		Fragment instance = ReferenceProfitFragmentFactory.newInstance(info,
				baseReferProfitInfo);
		if (instance == null) {
			LogGloble.e(TAG, "暂不支持该类型产品的显示");
			return;
		}
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.content, instance,
						instance.getClass().getSimpleName()).commit();
	}

	/**
	 * 请求收益汇总信息
	 * 
	 * @param productInfo
	 */
	public void requestReferenceProfitInfo(BOCProductForHoldingInfo productInfo) {
		BiiHttpEngine.showProgressDialog();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("accountKey", info.bancAccountKey);
		params.put("productCode", info.prodCode);
		params.put("kind", info.productKind);
		params.put("charCode", info.cashRemit);
		if(!"0".equals(info.standardPro)){
			params.put("tranSeq", info.tranSeq);
		}
		getHttpTools().requestHttp("PsnXpadReferProfitQuery",
				"requestReferenceProfitInfoCallback", params);
	}

	public void requestReferenceProfitInfoCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		LogGloble.e(TAG, "requestObj = " + resultObj);
		Map<String, Object> responseMap = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(responseMap)) {
			return;
		}
		showReFerenceYieldFragment(BaseReferProfitInfo.newInstance(responseMap));
	}

	public static Intent getIntent(Context context,
			BOCProductForHoldingInfo info) {
		Intent intent = new Intent(context, ReferenceProfitActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(EXTRA_KEY_INFO, info);
		return intent;
	}
}
