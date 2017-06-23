package com.chinamworld.bocmbci.biz.bocinvt.myproduct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 修改分红方式页面
 * 
 * @author wangmengmeng
 * 
 */
public class ChangeBonusModeActivity extends BociBaseActivity implements
		OnClickListener {
	private static final String TAG = "ChangeBonusModeActivity";
	/** 修改分红方式页面 */
	private View view;
	/** 产品信息 */
	private Map<String, Object> myproductMap;
	/** 产品代码 */
	private TextView tv_prodCode_detail;
	/** 产品名称 */
	private TextView tv_prodName_detail;
	/** 分红方式 */
	private TextView tvOldeMode,tvNewMode;
	/** 现金分红 */
//	private RadioButton bocinvt_cashMode;
//	/** 再投资分红 */
//	private RadioButton bocinvt_againMode;
	private String bonusMode;
	// 按钮/////////////////////////
	private Button btn_ok;
	private Button btn_cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.bocinvt_changeMode_step1));
		// 添加布局
		view = addView(R.layout.bocinvt_changemode);
		// 界面初始化
		init();
	}

	private void init() {
//		myproductMap = (Map<String, Object>) BaseDroidApp.getInstanse()
//				.getBizDataMap().get(ConstantGloble.BOCINVT_MYPRODUCT_LIST);
		myproductMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_QUANTITY_DETAIL_COMBINE_MAP);
		tv_prodCode_detail = (TextView) view
				.findViewById(R.id.tv_prodCode_detail);
		tv_prodName_detail = (TextView) view
				.findViewById(R.id.tv_prodName_detail);
		tvOldeMode = (TextView) view.findViewById(R.id.olde_mode);
		tvNewMode = (TextView) view.findViewById(R.id.new_mode);

		btn_ok = (Button) view.findViewById(R.id.btn_ok);
		btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		btn_ok.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);

		// 赋值
		/** 产品代码 */
		tv_prodCode_detail.setText(String.valueOf(myproductMap
				.get(BocInvt.BOCINVT_HOLDPRO_PRODCODE_RES)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodCode_detail);
		/** 产品名称 */
		tv_prodName_detail.setText(String.valueOf(myproductMap
				.get(BocInvt.BOCINVT_HOLDPRO_PRODNAME_RES)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodName_detail);
		String bonusModetype = (String) myproductMap
				.get(BocInvt.BOCINVT_HOLDPRO_CURRENTBONUSMODE_RES);
		//分红方式
		if (!StringUtil.isNull(bonusModetype)) {
			tvOldeMode.setText(LocalData.bocicurrentmodeMap.get(bonusModetype));
			if (bonusModetype.equals("0")) {
				tvNewMode.setText(LocalData.bocicurrentmodeMap.get("1"));
			}else if(bonusModetype.equals("1")){
				tvNewMode.setText(LocalData.bocicurrentmodeMap.get("0"));
			}
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			// 确定点击事件 请求分红
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();
			break;
		case R.id.btn_cancel:
			// 取消点击事件
			finish();
			break;
		default:
			break;
		}
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
		// 请求修改分红方式
		requestChangeBonusMode(tokenId);
	}

	/** 请求修改分红方式 */
	public void requestChangeBonusMode(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADSETBONUSMODE_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		String bonusModetype = (String) myproductMap
				.get(BocInvt.BOCINVT_HOLDPRO_CURRENTBONUSMODE_RES);
		if (bonusModetype.equals("0")) {
			bonusMode = "1";
		}else if(bonusModetype.equals("1")){
			bonusMode = "0";
		}
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(BocInvt.BOCINVT_SETBONUS_PRODCODE_REQ,
				String.valueOf(myproductMap
						.get(BocInvt.BOCINVT_HOLDPRO_PRODCODE_RES)));
		paramsmap.put(BocInvt.BOCINVT_SETBONUS_PRODNAME_REQ,
				String.valueOf(myproductMap
						.get(BocInvt.BOCINVT_HOLDPRO_PRODNAME_RES)));
		paramsmap
				.put(BocInvt.BOCINVT_SETBONUS_CURRENCYCODE_REQ, String
						.valueOf(myproductMap
								.get(BocInvt.BOCINVT_HOLDPRO_CURCODE_RES)));
		paramsmap.put(BocInvt.BOCINVT_SETBONUS_MODE_REQ, bonusMode);
		paramsmap.put(BocInvt.BOCINVT_SETBONUS_CASHREMIT_REQ, String
				.valueOf(myproductMap
						.get(BocInvt.BOCINVT_HOLDPRO_CASHREMIT_RES)));
		paramsmap.put(BocInvt.BOCINVT_SETBONUS_TOKEN_REQ, tokenId);
		paramsmap.put("accountKey", (String)myproductMap.get("bancAccountKey"));//p604accountnumber改为accountkey
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestChangeBonusModeCallback");
	}

	/** 请求修改分红方式回调 */
	public void requestChangeBonusModeCallback(Object resultObj) {
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
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.BOCINVT_CHANGEBONUSMODE, resultMap);
		Intent intent = new Intent(this, ChangeBonusModeSuccessActivity.class);
		intent.putExtra(BocInvt.BOCINVT_SETBONUS_MODE_REQ, bonusMode);
		startActivity(intent);

	}

}
