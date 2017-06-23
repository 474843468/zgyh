package com.chinamworld.bocmbci.biz.lsforex.acc;

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
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.biz.lsforex.adapter.IsForexBindAccAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.List;
import java.util.Map;

/** 选择双向宝交易账户页面 */
public class IsForexSettingBindAccActivity extends IsForexBaseActivity {
	private static final String TAG = "IsForexSettingBindAccActivity";
	/** 主视图布局 */
	private LinearLayout tabcontent;// 主Activity显示

	/** ForexAccSettingActivity的主布局*/
	private View rateInfoView = null;
	/** quickTrade：关闭按钮*/
	private Button quickTrade = null;
	/** 下一步按钮*/
	private Button nextButton = null;
	/** 网银账户标志*/
	String accountId = null;
	private ListView listView = null;
	private IsForexBindAccAdapter adapter = null;
	/** backButton:返回按钮*/
	private Button backButton = null;
	private String tokenId = null;
	private List<Map<String, String>> resultList = null;

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
		setLeftButtonPopupGone();
		quickTrade = (Button) findViewById(R.id.ib_top_right_btn);
		quickTrade.setVisibility(View.VISIBLE);
		quickTrade.setText(getResources().getString(R.string.forex_rate_close));
		backButton = (Button) findViewById(R.id.ib_back);
		backButton.setVisibility(View.GONE);
		/** 为界面标题赋值 */
		setTitle(getResources().getString(R.string.forex_rate_setAcc));
		BaseHttpEngine.showProgressDialogCanGoBack();
		// 过滤出符合条件的借记卡
		requestPsnVFGFilterDebitCard();
	}

	/** 过滤出符合条件的借记卡---回调 */
	@Override
	public void requestPsnVFGFilterDebitCardCallback(Object resultObj) {
		super.requestPsnVFGFilterDebitCardCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		resultList = (List<Map<String, String>>) biiResponseBody.getResult();
		if (resultList == null || resultList.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.isForex_times_change_noacc));
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		init();
		initOnClick();
	}

	/**
	 * 初始化所有的控件
	 */
	private void init() {
		rateInfoView = LayoutInflater.from(IsForexSettingBindAccActivity.this).inflate(R.layout.isforex_set_bind_acc,
				null);
		tabcontent.addView(rateInfoView);
		nextButton = (Button) findViewById(R.id.forex_nextButton);
		listView = (ListView) findViewById(R.id.forex_listView);
		// 将选中效果去掉
		IsForexBindAccAdapter.selectedPosition = -1;
		adapter = new IsForexBindAccAdapter(IsForexSettingBindAccActivity.this, resultList);
		listView.setAdapter(adapter);
	}

	private void initOnClick() {
		quickTrade.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				IsForexBindAccAdapter.selectedPosition = -1;
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Map<String, String> map = resultList.get(position);
				IsForexBindAccAdapter.selectedPosition = position;
				accountId = map.get(IsForex.ISFOREX_ACCOUNTID_REQ);
				// 更新数据显示
				adapter.notifyDataSetChanged();
			}
		});
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (IsForexBindAccAdapter.selectedPosition < 0) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getResources().getString(R.string.isForex_times_sign_acc_no));
					return;
				} else {
					BaseHttpEngine.showProgressDialog();
					requestCommConversationId();
				}
			}
		});
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		requestPSNGetTokenId(commConversationId);
	}

	/** 重新设定双向宝交易账户 */
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		tokenId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		// 重新设定双向宝交易账户
		requestPsnVFGSetTradeAccount(accountId, tokenId);
	}

	/** 重新设定双向宝交易账户-------回调 */
	@Override
	public void requestPsnVFGSetTradeAccountCallback(Object resultObj) {
		super.requestPsnVFGSetTradeAccountCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> result = (Map<String, String>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		String accountNumber = result.get(IsForex.ISFOREX_ACCOUNTNUMBER_REQ1);
		String accountType = result.get(IsForex.ISFOREX_ACCOUNTTYPE_REQ1);
		String nickName = result.get(IsForex.ISFOREX_NICKNAME_REQ1);
		if (StringUtil.isNull(accountNumber) || StringUtil.isNull(accountType) || StringUtil.isNull(nickName)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent(this, IsForexSettingBindAccSuccessActivity.class);
		intent.putExtra(IsForex.ISFOREX_ACCOUNTNUMBER_REQ1, accountNumber);
		intent.putExtra(IsForex.ISFOREX_ACCOUNTTYPE_REQ1, accountType);
		intent.putExtra(IsForex.ISFOREX_NICKNAME_REQ1, nickName);
		// startActivity(intent);
		startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_CHECKINFUNCACC_CODE);// 11
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
