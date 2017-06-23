package com.chinamworld.bocmbci.biz.finc.myfund;

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
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 我的基金 基金分红方式确认页面
 * 
 * @author 宁焰红
 * 
 */
public class MyFincBoundsTypeConfirmActivity extends FincBaseActivity {
	private final String TAG = "MyFincBoundsTypeConfirmActivity";
	/** 分红方式view */
	private View myFincView = null;
	/** 基金代码 */
	private TextView fincCodeText = null;
	/** 基金名称 */
	private TextView fincNameText = null;
	/** 当前分红方式 */
	private TextView fincTypeText = null;
	/** 基金代码 */
	private String foundCode = null;
	/** 基金名称 */
	private String foundName = null;
	/** 分红方式代码 0: 默认1: 现金2: 红利再投资 */
	private String foundTypeCode = null;
	/** 上一步 */
	private Button lastButton = null;
	/** 确定 */
	private Button sureButton = null;
	private String tokenId = null;

	private int flag;
	/** 白天的分红 */
	private final int DAYDEAL = 0;
	/** 非工作时间的分红 */
	private final int NIGHTDEAL = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		init();
		initData();
		initOnClick();
		initRightBtnForMain();
	}
	/***
	 * 初始化控件
	 */
	private void init() {
		myFincView = mainInflater.inflate(
				R.layout.finc_myfinc_bondstype_confirm, null);
		tabcontent.addView(myFincView);
		setTitle(getResources().getString(R.string.finc_myfinc_bound));
		fincCodeText = (TextView) findViewById(R.id.finc_fincCode);
		fincNameText = (TextView) findViewById(R.id.finc_fincName);
		fincTypeText = (TextView) findViewById(R.id.finc_boundsType);
		lastButton = (Button) findViewById(R.id.lastButton);
		sureButton = (Button) findViewById(R.id.sureButton);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fincNameText);
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources().getString(
								R.string.finc_myfinc_foundOne),
						this.getResources().getString(R.string.finc_top_two),
						this.getResources()
								.getString(R.string.finc_myfinc_acc3) });
		StepTitleUtils.getInstance().setTitleStep(2);
	}

	/** 初始化数据 */
	private void initData() {
		Intent intent = getIntent();
		if (intent == null) {
			return;
		} else {
			foundCode = intent.getStringExtra(Finc.FINC_FUNDCODE_REQ);
			foundName = intent.getStringExtra(Finc.FINC_FUNDNAME_REQ);
			foundTypeCode = intent
					.getStringExtra(ConstantGloble.FINC_FOUNDTYPECODE_KEY);
			fincCodeText.setText(foundCode);
			fincNameText.setText(foundName);
			fincTypeText.setText(LocalData.bonusTypeMap.get(foundTypeCode));
		}
	}

	/*** 初始化监听事件 */
	private void initOnClick() {
		lastButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				flag = DAYDEAL;
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();
			}
		});
	}

	/***
	 * 请求ConversationId--回调
	 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 请求TokenId
		requestPSNGetTokenId();
	}

	/** 重写请求TokenId---回调方法 */
	@Override
	public void requestPSNGetTokenIdCallback(Object resultObj) {
		super.requestPSNGetTokenIdCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (StringUtil.isNullOrEmpty(resultObj)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		tokenId = (String) biiResponseBody.getResult();
		switch (flag) {
		case DAYDEAL:
			// 基金修改分红方式
			requestPsnFundBonusResult(foundCode, foundTypeCode, tokenId);
			break;
		case NIGHTDEAL:
			requestPsnFundNightBonusResult(foundCode, foundTypeCode, tokenId);
			break;

		default:
			break;
		}

	}

	@Override
	public void requestPsnFundNightBonusResultCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.requestPsnFundNightBonusResultCallback(resultObj);
		Intent intent  = getIntent();
		intent.setClass(MyFincBoundsTypeConfirmActivity.this,
				MyFincBoundsTypeSuccessActivity.class);
		startActivityForResult(intent, 1);

	}
	  @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			switch (resultCode) {
			case RESULT_OK:
				setResult(RESULT_OK);
				finish();
				break;

			default:
				break;
			}
		}
	/** 基金修改分红方式接口----回调 */
	@Override
	public void requestPsnFundBonusResultCallback(Object resultObj) {
		super.requestPsnFundBonusResultCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> resultMap = (Map<String, String>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		} else {
			if (ConstantGloble.FINC_BONUSRESULT_OK.equals(resultMap
					.get(Finc.FINC_FUNDBUY_TRANSTATE))) {
				Intent intent = getIntent();
				intent.setClass(MyFincBoundsTypeConfirmActivity.this,
						MyFincBoundsTypeSuccessActivity.class);
				startActivityForResult(intent, 1);
			} else if (ConstantGloble.FINC_FUNDBUYSTATE_TIMEERROR
					.equals(resultMap.get(Finc.FINC_FUNDBUY_TRANSTATE))) {// 非工作时间
				BaseDroidApp.getInstanse().showErrorDialog(getString(R.string.finc_tradetime_error),
						R.string.cancle, R.string.confirm,
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								switch (Integer.parseInt(v.getTag() + "")) {
								case CustomDialog.TAG_SURE:
									BaseDroidApp.getInstanse()
											.dismissErrorDialog();
									flag = NIGHTDEAL;
									requestCommConversationId();
									BaseHttpEngine.showProgressDialog();
									break;
								case CustomDialog.TAG_CANCLE:
									BaseDroidApp.getInstanse()
											.dismissErrorDialog();
									break;
								}
							}
						});
				return;
			}
		}
	}
}
