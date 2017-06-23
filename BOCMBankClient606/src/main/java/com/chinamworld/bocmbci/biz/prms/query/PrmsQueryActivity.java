package com.chinamworld.bocmbci.biz.prms.query;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.prms.PrmsBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 贵金属查询页面
 * 
 * @author xyl
 * 
 */
public class PrmsQueryActivity extends PrmsBaseActivity {
	private static final String TAG = "PrmsQueryActivity";
	/**
	 * 当前有效委托交易查询,历史委托交易查询,历史交易查询
	 */
	private View queryEntrustNowView, queryEntrustHistoryView, queryDealView;

	private int flag;
	private static final int DEAL = 1;
	private static final int ENTRUSTNOW = 2;
	private static final int ENTRUSTHISTORY = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		initView();
		initOnclickListenner();
	}

	/**
	 * 初始化界面
	 * 
	 * @Author xyl
	 */
	private void initView() {
		View childView = mainInflater.inflate(R.layout.prms_query_menu, null);
		tabcontent.addView(childView);
		queryEntrustNowView = findViewById(R.id.prms_querymenu_entrust_now_ll);
		queryEntrustHistoryView = findViewById(R.id.prms_querymenu_entrust_history_ll);
		queryDealView = findViewById(R.id.prms_querymenu_deal_ll);
		right.setVisibility(View.GONE);
		// 设置标题
		setTitle(R.string.prms_title_query);
	}

	/**
	 * 添加监听
	 */
	private void initOnclickListenner() {
		queryEntrustNowView.setOnClickListener(this);
		queryEntrustHistoryView.setOnClickListener(this);
		queryDealView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.prms_querymenu_entrust_now_ll:// 当前有效委托交易查询
			if (!BaseDroidApp.getInstanse().isLogin()) {// 没有登录就跳转到登录页面
//				Intent intent = new Intent(PrmsQueryActivity.this,
//						LoginActivity.class);
//				startActivityForResult(intent,
//						ConstantGloble.ACTIVITY_RESULT_CODE);
				BaseActivity.getLoginUtils(PrmsQueryActivity.this).exe(new LoginTask.LoginCallback() {

					@Override
					public void loginStatua(boolean isLogin) {

					}
				});
				return;
			}
			flag = ENTRUSTNOW;
			BaseHttpEngine.showProgressDialog();
			checkRequestPsnInvestmentManageIsOpen();
			break;
		case R.id.prms_querymenu_entrust_history_ll:// 历史委托交易查询
			if (!BaseDroidApp.getInstanse().isLogin()) {// 没有登录就跳转到登录页面
//				Intent intent = new Intent(PrmsQueryActivity.this,
//						LoginActivity.class);
//				startActivityForResult(intent,
//						ConstantGloble.ACTIVITY_RESULT_CODE);
				BaseActivity.getLoginUtils(PrmsQueryActivity.this).exe(new LoginTask.LoginCallback() {

					@Override
					public void loginStatua(boolean isLogin) {

					}
				});
				return;
			}
			flag = ENTRUSTHISTORY;
			BaseHttpEngine.showProgressDialog();
			checkRequestPsnInvestmentManageIsOpen();
			break;
		case R.id.prms_querymenu_deal_ll:// 成交状况查询
			if (!BaseDroidApp.getInstanse().isLogin()) {// 没有登录就跳转到登录页面
				BaseActivity.getLoginUtils(PrmsQueryActivity.this).exe(new LoginTask.LoginCallback() {

					@Override
					public void loginStatua(boolean isLogin) {

					}
				});
				return;
			}
			flag = DEAL;
			BaseHttpEngine.showProgressDialog();
			checkRequestPsnInvestmentManageIsOpen();
			break;
		default:
			break;
		}
	}

	@Override
	public void checkRequestPsnInvestmentManageIsOpenCallback(Object resultObj) {
		super.checkRequestPsnInvestmentManageIsOpenCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String isOpenOr = (String) biiResponseBody.getResult();
		prmsControl.ifInvestMent = StringUtil.parseStrToBoolean(isOpenOr);
		queryPrmsAcc();
	}

	@Override
	public void queryPrmsAccCallBack(Object resultObj) {
		super.queryPrmsAccCallBack(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getResult() == null) {
			prmsControl.ifhavPrmsAcc = false;
			prmsControl.accMessage = null;
			prmsControl.accId = null;
		} else {
			prmsControl.accMessage = (Map<String, String>) biiResponseBodys
					.get(0).getResult();
			prmsControl.accNum = String.valueOf(prmsControl.accMessage
					.get(Prms.QUERY_PRMSACC_ACCOUNT));
			prmsControl.accId = String.valueOf(prmsControl.accMessage
					.get(Prms.QUERY_PRMSACC_ACCOUNTID));
			prmsControl.ifhavPrmsAcc = true;
		}
		if (!prmsControl.ifhavPrmsAcc || !prmsControl.ifInvestMent) {
			BaseHttpEngine.dissMissProgressDialog();
			getPopup();
		} else {
			Intent intent;
			switch (flag) {
			case DEAL:
				BaseHttpEngine.dissMissProgressDialog();
				intent = new Intent(this, PrmsQueryDealActivity.class);
				startActivity(intent);
				break;
			case ENTRUSTHISTORY:
				BaseHttpEngine.dissMissProgressDialog();
				intent = new Intent(this, PrmsQueryEntrustHistoryActivity.class);
				startActivity(intent);
				break;
			case ENTRUSTNOW:
				requestCommConversationId();
				break;

			default:
				break;
			}
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		queryPrmsTradeEntrustNow(0, 10, true);
	}

	@Override
	public void queryPrmsTradeEntrustNowCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.queryPrmsTradeEntrustNowCallBack(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> map = (Map<String, Object>) biiResponseBody
				.getResult();
		prmsControl.queryEntrustNowList = (List<Map<String, Object>>) map
				.get(Prms.PRMS_QUERY_DEAL_LIST);
		if (!StringUtil.isNullOrEmpty(prmsControl.queryEntrustNowList)) {// 如果不为空
			String totalNum = (String) map
					.get(Prms.PRMS_QUERY_DEAL_RECORDNUMBER);
			Intent intent = new Intent();
			intent.putExtra(Prms.PRMS_QUERY_DEAL_RECORDNUMBER, totalNum);
			intent.setClass(this, PrmsQueryEntrustNowActivity.class);
			startActivity(intent);
		}
	}

}
