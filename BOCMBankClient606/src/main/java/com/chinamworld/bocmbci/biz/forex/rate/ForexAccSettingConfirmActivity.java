package com.chinamworld.bocmbci.biz.forex.rate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.biz.forex.adapter.ForexAccSettingConfirmAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.Utils;

/**
 * 账户设定确认页面
 * 0608确认不在使用确认页面
 * @author Administrator
 * 
 */
public class ForexAccSettingConfirmActivity extends BaseActivity {
	private static final String TAG = "ForexAccSettingConfirmActivity";
	/** 主视图布局 */
	private LinearLayout tabcontent;// 主Activity显示

	/**
	 * ForexAccSettingConfirmActivity的主布局
	 */
	private View rateInfoView = null;
	/**
	 * quickTrade：关闭按钮
	 */
	private Button quickTrade = null;
	/**
	 * backButton:返回按钮
	 */
	private Button backButton = null;
	/**
	 * 账户号码
	 */
	private String accNum;
	/**
	 * 账户类型
	 */
	private String accstyle;
	/**
	 * 账户别名
	 */
	private String accalais;
	/**
	 * 账户ID
	 */
	private String accountId = null;

	/**
	 * 账户类型
	 */
	private TextView accTypeText = null;
	/**
	 * 账户别名
	 */
	private TextView accAlisText = null;
	/**
	 * 账户
	 */
	private TextView accNumberText = null;
	private View textLayout = null;
	private Button lastButton = null;
	private Button sureButton = null;
	private ListView listView = null;
	/** 获取到的conversationId 保存 */
	private String commConversationId;
	/** 获取到的tokenId 保存 */
	private String tokenId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		LogGloble.d(TAG, "onCreate");
		// 初始化弹窗按钮
		initPulldownBtn();
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		if (StringUtil.isNull(commConversationId)) {
			return;
		} 
		init();
		initData();
		initOnClick();
	}

	/**
	 * 初始化所有的控件
	 */
	private void init() {
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		View footView = findViewById(R.id.rl_menu);
		footView.setVisibility(View.GONE);
		Button leftButton = (Button) findViewById(R.id.btn_show);
		leftButton.setVisibility(View.GONE);
		rateInfoView = LayoutInflater.from(ForexAccSettingConfirmActivity.this).inflate(
				R.layout.forex_acc_setting_confirm, null);
		tabcontent.addView(rateInfoView);
		/** 为界面标题赋值 */
		setTitle(getResources().getString(R.string.forex_rate_setAcc));
		quickTrade = (Button) findViewById(R.id.ib_top_right_btn);
		quickTrade.setVisibility(View.VISIBLE);
		quickTrade.setText(getResources().getString(R.string.forex_rate_close));
		backButton = (Button) findViewById(R.id.ib_back);
		backButton.setVisibility(View.GONE);
		accTypeText = (TextView) findViewById(R.id.acc_type);
		accAlisText = (TextView) findViewById(R.id.acc_alias);
		accNumberText = (TextView) findViewById(R.id.acc_number);
		listView = (ListView) findViewById(R.id.acc_listView);
		lastButton = (Button) findViewById(R.id.acc_last);
		sureButton = (Button) findViewById(R.id.acc_sure);
		textLayout = findViewById(R.id.textLayout);
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getResources().getString(R.string.forex_acc_step1),
						this.getResources().getString(R.string.forex_acc_step2),
						this.getResources().getString(R.string.forex_acc_step3) });
		StepTitleUtils.getInstance().setTitleStep(2);
	}

	/***
	 * 初始化数据
	 */
	public void initData() {
		Intent intent = getIntent();
		String type = null;
		if (intent != null) {
			accstyle = intent.getStringExtra(Forex.FOREX_ACCOUNTTYPE_RES);
			accalais = (String) intent.getStringExtra(Forex.FOREX_NICKNAME_RES);
			accNum = (String) intent.getStringExtra(Forex.FOREX_ACCOUNTNUMBER_RES);
			accountId = intent.getStringExtra(Forex.FOREX_ACCOUNTID_REQ);
			LogGloble.d(TAG + "----accountId", accountId);
		}

		if (LocalData.AccountType.containsKey(accstyle)) {
			type = LocalData.AccountType.get(accstyle);

		}
		accTypeText.setText(type);
		accNum = StringUtil.getForSixForString(accNum);
		accNumberText.setText(accNum);
		accAlisText.setText(accalais);
		if (!StringUtil.isNullOrEmpty(accountId)) {
			// 根据accountId查询账户详细信息
			requestPsnAccountQueryAccountDetail(accountId);
		}

	}



	private void initOnClick() {
		quickTrade.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 返回到外汇行情页面
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		// 上一步
		lastButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 返回到账户设置页面
				finish();
			}
		});
		// 确定按钮
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestCommConversationId();BaseHttpEngine.showProgressDialog();
			}
		});
	}

	/** 根据账户ID查询账户详细信息 */
	private void requestPsnAccountQueryAccountDetail(String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Forex.FOREX_ACCOUNTQUERY_API);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(ConstantGloble.FOREX_ACCDETAIL_ACCOUNTID, accountId);
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestPsnAccountQueryAccountDetailCallback");
	}

	/** 根据账户ID查询账户详细信息---回调 */
	public void requestPsnAccountQueryAccountDetailCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultList = (Map<String, Object>) biiResponseBody.getResult();
		if (resultList==null||resultList.size()==0) {
			return;
		} else {
			List<Map<String, String>> accountDetaiList = (List<Map<String, String>>) resultList
					.get(Forex.FOREX_ACCOUNTDETAILIST_REQ);
			if (StringUtil.isNullOrEmpty(accountDetaiList)) {
				return;
			} else {
				if (accstyle.equals(ConstantGloble.ACC_ACTYPENOT)) {
					listView.setVisibility(View.GONE);
					textLayout.setVisibility(View.GONE);
				} else {
					textLayout.setVisibility(View.VISIBLE);
					listView.setVisibility(View.VISIBLE);
					listView.setAdapter(new ForexAccSettingConfirmAdapter(ForexAccSettingConfirmActivity.this,
							accountDetaiList));
					Utils.setListViewHeightBasedOnChildren(listView);
				}
			}
		}
	}
	
	/**
	 * 处理获取Conversation的数据得到Conversation
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 * @return conversationId
	 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		if (StringUtil.isNull(commConversationId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}else{
			aquirePSNGetTokenId();
		} 
	}

	/**
	 * 获取tokenId
	 */
	public void aquirePSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId(commConversationId);
		HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenIdCallback");
	}

	/**
	 * 处理获取tokenId的数据得到tokenId
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 * @return tokenId
	 */
	public void aquirePSNGetTokenIdCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			requestPsnForexActSubmit();
		}
	}

	/**
	 * 账户设定提交
	 */
	private void requestPsnForexActSubmit() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Forex.FOREX_PSNFOREXACTSUBMIT_API);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Comm.TOKEN_REQ, tokenId);
		map.put(Forex.FOREX_ACCOUNTID_REQ, accountId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnForexActSubmitCallback");
	}

	/**
	 * 账户设定提交---回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnForexActSubmitCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		Map<String, Object> map = (Map<String, Object>) biiResponseBody.getResult();
		String bandFlag = (String) map.get(Forex.FOREX_BANDFLAG_REQ);
		if (ConstantGloble.FOREX_ISSETACC_TRUE.equals(bandFlag)) {
			// 返回到账户设置成功页面
			Intent intent = new Intent(ForexAccSettingConfirmActivity.this, ForexAccSettingSuccessActivity.class);
			intent.putExtra(Forex.FOREX_ACCOUNTTYPE_RES, accstyle);
			intent.putExtra(Forex.FOREX_NICKNAME_RES, accalais);
			intent.putExtra(Forex.FOREX_ACCOUNTNUMBER_RES, accNum);
			startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
		} else {
			return;
		}
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

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return null;
	}
}
