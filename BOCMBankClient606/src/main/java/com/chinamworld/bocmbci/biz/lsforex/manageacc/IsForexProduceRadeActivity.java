package com.chinamworld.bocmbci.biz.lsforex.manageacc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 签约协议页面 */
public class IsForexProduceRadeActivity extends IsForexBaseActivity {
	private final String TAG = "IsForexProduceRadeActivity";
	private Button backButton = null;
	private View rateInfoView = null;
	private Button receptButton = null;
	private Button refuseButton = null;
	private TextView naneText = null;
	private int pageSize = 10;
	private int currentIndex = 0;
	private boolean refresh = true;
	private int recordNumber = 0;
	private List<Map<String, String>> baillResultList = null;
	private List<Map<String, String>> signBailList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.isForex_manage_right));
		LogGloble.d(TAG, "onCreate");
		backButton = (Button) findViewById(R.id.ib_back);
		backButton.setVisibility(View.INVISIBLE);
		rateInfoView = LayoutInflater.from(this).inflate(R.layout.isforex_manage_sign_rade, null);
		tabcontent.addView(rateInfoView);
		receptButton = (Button) findViewById(R.id.sureButton);
		refuseButton = (Button) findViewById(R.id.lastButton);
		baillResultList = new ArrayList<Map<String, String>>();
		// 获取登录信息
		Map<String, Object> returnMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);
		String loginName = String.valueOf(returnMap.get(Crcd.CRCD_CUSTOMERNAME));
		naneText = (TextView) findViewById(R.id.tv_jianfang);
		String prix = getResources().getString(R.string.mycrcd_jiafang);
		naneText.setText(prix + loginName);
		initOnClick();
	}

	private void initOnClick() {
		// 不接受
		refuseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		// 接受
		receptButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
		});
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 查询可签约保证金产品
		PsnVFGBailProductQuery(currentIndex, pageSize, refresh);
	}

	/** 查询可签约保证金产品 */
	private void PsnVFGBailProductQuery(int currentIndex, int pageSize, boolean _refresh) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGBAILPRODUCTQUERY_API);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> map = new HashMap<String, String>();
		map.put(IsForex.ISFOREX_CURRENTINDEX_REQ, String.valueOf(currentIndex));
		map.put(IsForex.ISFOREX_PAGESIZE_REQ, String.valueOf(pageSize));
		map.put(IsForex.ISFOREX_REFRESH_REQ, String.valueOf(_refresh));
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "PsnVFGBailProductQueryCallback");
	}

	/** 查询可签约保证金产品-----回调 */
	public void PsnVFGBailProductQueryCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.isForex_times_manage_noproduce));
			return;
		}
		String number = (String) result.get(IsForex.ISFOREX_RECORDERNUMBER_RES);
		if (StringUtil.isNull(number)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.isForex_times_manage_noproduce));
			return;
		}
		recordNumber = Integer.valueOf(number);
		if (!result.containsKey(IsForex.ISFOREX_LIST) || StringUtil.isNullOrEmpty(result.get(IsForex.ISFOREX_LIST))) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.isForex_times_manage_noproduce));
			return;
		}
		List<Map<String, String>> list = (List<Map<String, String>>) result.get(IsForex.ISFOREX_LIST);
		if (list == null || list.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.isForex_times_manage_noproduce));
			return;
		}
		if (baillResultList != null && !baillResultList.isEmpty()) {
			baillResultList.clear();
		}		
		baillResultList.addAll(list);
		Intent intent = new Intent(IsForexProduceRadeActivity.this, IsForexProduceChoiseActivity.class);
		BaseDroidApp.getInstanse().getBizDataMap().put(IsForex.ISFOREX_LIST, baillResultList);
		intent.putExtra(IsForex.ISFOREX_RECORDERNUMBER_RES, recordNumber);
		BaseHttpEngine.dissMissProgressDialog();
		// startActivity(intent);
		startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_REGISTFUNCACC_CODE);// 10

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;
		default:
			break;
		}
	}
}
