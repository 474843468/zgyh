
package com.chinamworld.bocmbci.biz.forex.rate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.biz.BusinessModelControl;
import com.chinamworld.bocmbci.biz.forex.adapter.ForexAccSettingAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 设定外汇交易账户，选择账户
 * 
 * @author 宁焰红
 * 
 */
public class ForexAccSettingActivity extends BaseActivity {
	private static final String TAG = "ForexAccSettingActivity";
	/** 主视图布局 */
	private LinearLayout tabcontent;// 主Activity显示

	/**
	 * ForexAccSettingActivity的主布局
	 */
	private View rateInfoView = null;
	/**
	 * 关联新账户的主布局
	 */
	private View accRateInfoView = null;
	/**
	 * quickTrade：关闭按钮
	 */
	private Button quickTrade = null;
	/**
	 * 下一步按钮
	 */
	private Button nextButton = null;
	/**
	 * 网银账户标志
	 */
	String accountId = null;
	private ListView listView = null;
	private ForexAccSettingAdapter adapter = null;
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
	 * backButton:返回按钮
	 */
	private Button backButton = null;
	/** 存储所有符合条件的账户 */
	List<Map<String, String>> allAccList = null;
	/**
	 * 关联新账户
	 */
	private Button accButton = null;
	private String commConversationId = null;
	private String tokenId = null;
	/** 存储账户数据，判断是否有可用账户 */
	private List<Map<String, String>> resultList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_606_layout);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		LogGloble.d(TAG, "onCreate");
		View footView = findViewById(R.id.rl_menu);
		footView.setVisibility(View.GONE);
		Button leftButton = (Button) findViewById(R.id.btn_show);
		leftButton.setVisibility(View.GONE);
		// 初始化弹窗按钮
		initPulldownBtn();
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 20);
		}
		quickTrade = (Button) findViewById(R.id.ib_top_right_btn);
		quickTrade.setVisibility(View.VISIBLE);
		quickTrade.setText(getResources().getString(R.string.forex_rate_close));
		backButton = (Button) findViewById(R.id.ib_back);
		backButton.setVisibility(View.GONE);
		/** 为界面标题赋值 */
		setTitle(getResources().getString(R.string.forex_rate_setAcc));
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		if (StringUtil.isNull(commConversationId)) {
			return;
		}
		allAccList = new ArrayList<Map<String, String>>();
		init();
		initOnClick();
		// 查询所有的账户
		// requestInitPsnForexActAvai();
	}

	/** 初始化没有账户的布局 */
	private void initNoAcc() {
		if (accRateInfoView == null) {
			accRateInfoView = LayoutInflater.from(ForexAccSettingActivity.this).inflate(R.layout.forex_rate_no_acc,
					null);
		}
		tabcontent.addView(accRateInfoView);
		accButton = (Button) findViewById(R.id.forex_acc_addnewacc);
		accButton.setVisibility(View.GONE); //屏蔽自助关联
	}

	/** 初始化关联新账户布局的监听事件 */
	private void initAccOnClick() {
		// 关联新账户页面关闭按钮
		quickTrade.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		// 关联新账户
		accButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(ForexAccSettingActivity.this, AccInputRelevanceAccountActivity.class);
//				startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
				BusinessModelControl.gotoAccRelevanceAccount(ForexAccSettingActivity.this,  ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);
			}
		});
	}

	/**
	 * 初始化所有的控件
	 */
	private void init() {
		rateInfoView = LayoutInflater.from(ForexAccSettingActivity.this).inflate(R.layout.forex_acc_seting, null);
		tabcontent.addView(rateInfoView);
		nextButton = (Button) findViewById(R.id.forex_nextButton);
		listView = (ListView) findViewById(R.id.forex_listView);
		// StepTitleUtils.getInstance().initTitldStep(
		// this,
		// new String[] {
		// this.getResources().getString(R.string.forex_acc_step1),
		// this.getResources().getString(R.string.forex_acc_step2),
		// this.getResources().getString(R.string.forex_acc_step3) });
		// StepTitleUtils.getInstance().setTitleStep(1);
		// 将选中效果去掉
		ForexAccSettingAdapter.selectedPosition = -1;
		resultList = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.FOREX_ACTAVI_RESULT_KEY);
		adapter = new ForexAccSettingAdapter(ForexAccSettingActivity.this, resultList);
		listView.setAdapter(adapter);

	}

	private void initOnClick() {
		// 关闭按钮事件
		quickTrade.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ForexAccSettingAdapter.selectedPosition = -1;
				setResult(RESULT_CANCELED);
				finish();

			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Map<String, String> map = resultList.get(position);
				ForexAccSettingAdapter.selectedPosition = position;
				accstyle = map.get(Forex.FOREX_ACCOUNTTYPE_RES);
				accalais = map.get(Forex.FOREX_NICKNAME_RES);
				accNum = map.get(Forex.FOREX_ACCOUNTNUMBER_RES);
				accountId = map.get(Forex.FOREX_ACCOUNTID_REQ);
				String accountType = map.get(Forex.Forex_ACCOUNYTYPE_RES);
				BaseDroidApp.getInstanse().getBizDataMap().put(Forex.Forex_ACCOUNYTYPE_RES, accountType);
				BaseDroidApp.getInstanse().getBizDataMap().put(Forex.Forex_RATE_ACCOUNT_RES, accNum);
				BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_NICKNAME_RES, accalais);
				// 更新数据显示
				adapter.notifyDataSetChanged();

			}
		});
		// 下一步按钮事件
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ForexAccSettingAdapter.selectedPosition < 0) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							ForexAccSettingActivity.this.getString(R.string.forex_currency_acc));
					return;
				} else {
					// 不在使用确认页面，直接跳转到成功页面
					// // 跳转到账户确认页面
					// Intent intent = new Intent(ForexAccSettingActivity.this,
					// ForexAccSettingConfirmActivity.class);
					// // 将数据传送到设置账户确认页面
					// intent.putExtra(ConstantGloble.FOREX_ACCOUNTTYPE,
					// accstyle);
					// intent.putExtra(ConstantGloble.FOREX_NICKNAME, accalais);
					// intent.putExtra(ConstantGloble.FOREX_ACCOUNTNUMBER,
					// accNum);
					// intent.putExtra(Forex.FOREX_ACCOUNTID_REQ, accountId);
					// startActivityForResult(intent,
					// ConstantGloble.ACTIVITY_RESULT_CODE);
					// 跳转到成功页面
					requestCommConversationId();
					BaseHttpEngine.showProgressDialog();
				}

			}
		});
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
		} else {
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
		Map<String, String> chinaBankAccount = (Map<String, String>) map.get(Forex.FOREX_CHINABANKACCOUNT_REQ);
		if (ConstantGloble.FOREX_ISSETACC_TRUE.equals(bandFlag)) {
			if (StringUtil.isNullOrEmpty(chinaBankAccount)) {
				return;
			}
			String accountType = chinaBankAccount.get(Forex.FOREX_ACCOUNTTYPE_REQ);
			String nickName = chinaBankAccount.get(Forex.FOREX_NICKNAME_REQ);
			String accountNumber = chinaBankAccount.get(Forex.FOREX_ACCOUNTNUMBER_REQ);
			if (StringUtil.isNull(accountType) || StringUtil.isNull(nickName) || StringUtil.isNull(accountNumber)) {
				return;
			}
			// 返回到账户设置成功页面
			Intent intent = new Intent(ForexAccSettingActivity.this, ForexAccSettingSuccessActivity.class);
			intent.putExtra(Forex.FOREX_ACCOUNTTYPE_RES, accountType);
			intent.putExtra(Forex.FOREX_NICKNAME_RES, nickName);
			intent.putExtra(Forex.FOREX_ACCOUNTNUMBER_RES, accountNumber);
			startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
		} else {
			return;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE:// 关联新账户
			switch (resultCode) {
			case RESULT_OK:
				// 查询外汇交易账户
				requestPsnForexActAvai();
				break;
			case RESULT_CANCELED:
				// 开通失败的响应
				break;
			}
			break;

		default:// 设定账户
			switch (resultCode) {
			case RESULT_OK:
				BaseDroidApp.getInstanse().setCurrentAct(this);
				setResult(RESULT_OK);
				finish();
				break;

			default:
				break;
			}
			break;
		}

	}

	/** 查询所有的外汇交易账户 */
	private void requestInitPsnForexActAvai() {
		BiiHttpEngine.showProgressDialogCanGoBack();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Forex.FOREX_PSNFOREXACTAVAI_API);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "requestInitPsnForexActAvaiCallback");
	}

	/** 查询所有的外汇交易账户----回调 */
	public void requestInitPsnForexActAvaiCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		resultList = (List<Map<String, String>>) biiResponseBody.getResult();
		if (resultList == null || resultList.size() <= 0) {
			// 没有可用账户，关联新账户
			initNoAcc();
			initAccOnClick();
		} else {
			init();
			initOnClick();
		}

	}

	/**
	 * 外汇交易账户列表-01
	 */
	private void requestPsnForexActAvai() {
		BiiHttpEngine.showProgressDialogCanGoBack();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Forex.FOREX_PSNFOREXACTAVAI_API);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnForexActAvaiCallback");
	}

	/**
	 * 外汇交易账户列表----回调
	 * 
	 * @param resultObj
	 *            :返回结果
	 */
	public void requestPsnForexActAvaiCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		resultList = (List<Map<String, String>>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(resultList) || resultList == null || resultList.size() <= 0) {
//			Intent intent = new Intent(ForexAccSettingActivity.this, AccInputRelevanceAccountActivity.class);
//			startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
//			BusinessModelControl.gotoAccRelevanceAccount(ForexAccSettingActivity.this, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);
			//屏蔽自助关联
			finish();
			return;
		} else {
			// 加载布局
			tabcontent.removeAllViews();
			init();
			initOnClick();
		}
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.TwoTask;
	}
}
