package com.chinamworld.bocmbci.biz.finc.query;

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
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.invest.constant.InvestConstant;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.ErrorCode;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 基金交易查询
 * 
 * @author xyl
 * 
 */
public class FundqueryMenuActivity extends FincBaseActivity {
	private static final String TAG = "FundPricesMenuActivity";

	/** 在途交易查询,历史交易查询 */
	private View effectiveDealQueryView, historyDealQueryView;
	/** 标记点击的交易类型 */
	private int flag;
	private final int EFFECTIVE = 1;// 在途
	private final int HISTORY = 2;// 历史

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		fincControl.cleanAllData();
		setLeftSelectedPosition("finc_5");
	}

	private void initView() {
		View childView = mainInflater.inflate(R.layout.finc_query_menu, null);
		tabcontent.addView(childView);
		effectiveDealQueryView = childView
				.findViewById(R.id.finc_effectiveDealQuery_view);
		historyDealQueryView = childView
				.findViewById(R.id.finc_historyDealQuery_view);
		setTitle(R.string.finc_query_main);
		right.setVisibility(View.GONE);
	}

	private void initListenner() {
		effectiveDealQueryView.setOnClickListener(this);
		historyDealQueryView.setOnClickListener(this);
	}

	private void init() {
		initView();
		initListenner();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ConstantGloble.ACTIVITY_REQUEST_MANAGE_CODE:// 开通理财服务
			switch (resultCode) {
			case RESULT_OK:
				fincControl.ifInvestMent = true;
				if (!fincControl.ifhaveaccId) {// 如果还没有基金账户 getPopup();
					getPopup();
				}
				break;

			default:
				fincControl.ifInvestMent = false;
				getPopup();
				break;
			}
			break;
		case ConstantGloble.ACTIVITY_REQUEST_SETFINCSACC_CODE:// 开通基金账户
			switch (resultCode) {
			case RESULT_OK:
				fincControl.ifhaveaccId = true;
				switch (flag) {
				case EFFECTIVE: // 在途交易
					BaseHttpEngine.showProgressDialog();
					requestCommConversationId();
					break;
				case HISTORY: // 历史交易
					startActivity(new Intent(this,
							FincQueryHistoryActivity.class));
					break;
				}
				break;

			default:
				fincControl.ifhaveaccId = false;
				getPopup();
				break;
			}
			break;
		case InvestConstant.FUNDRISK:// 基金风险评估
			switch (resultCode) {
			case RESULT_OK:
				fincControl.ifdorisk = true;
				switch (flag) {
				case EFFECTIVE: // 在途交易
					BaseHttpEngine.showProgressDialog();
					requestCommConversationId();
					break;
				case HISTORY: // 历史交易
					startActivity(new Intent(this,
							FincQueryHistoryActivity.class));
					break;
				}
				break;
			default:
				fincControl.ifdorisk = false;
				getPopup();
				break;
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		fundQueryEnTrust(0, 10, true);
	}

	@Override
	public void fundQueryEnTrustCallback(Object resultObj) {
		super.fundQueryEnTrustCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty((List<Map<String, Object>>) resultMap
				.get(Finc.FINC_QUERYEXTRADAY_RESULTLIST))) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.finc_query_noresult_error));
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(Finc.D_ENTRUST_RESULTMAP, resultMap);
		Intent intent = new Intent();
		intent.setClass(this,FundQueryEffectiveActivity.class);
		startActivity(intent);
	}

	@Override
	public void doCheckRequestQueryInvtBindingInfoCallback(
			Object resultObj) {
		super.doCheckRequestQueryInvtBindingInfoCallback(resultObj);
		if(!fincControl.ifhaveaccId || !fincControl.ifInvestMent) return;
		switch (flag) {
		case EFFECTIVE: // 在途交易查询
			requestCommConversationId();
			break;
		case HISTORY: // 历史交易查询
			BaseHttpEngine.dissMissProgressDialog();
			startActivity(new Intent(this, FincQueryHistoryActivity.class));
			break;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_effectiveDealQuery_view:// 在途交易查询
			if (!BaseDroidApp.getInstanse().isLogin()) {// 没有登录就跳转到登录页面
//				Intent intent = new Intent(FundqueryMenuActivity.this,
//						LoginActivity.class);
//				startActivityForResult(intent,
//						ConstantGloble.ACTIVITY_RESULT_CODE);
				BaseActivity.getLoginUtils(FundqueryMenuActivity.this).exe(new LoginTask.LoginCallback() {

					@Override
					public void loginStatua(boolean isLogin) {

					}
				});
				return;
			}
			flag = EFFECTIVE;
			BaseHttpEngine.showProgressDialog();
			doCheckRequestPsnInvestmentManageIsOpen();
			break;
		case R.id.finc_historyDealQuery_view:// 历史交易查询
			if (!BaseDroidApp.getInstanse().isLogin()) {// 没有登录就跳转到登录页面
//				Intent intent = new Intent(FundqueryMenuActivity.this,
//						LoginActivity.class);
//				startActivityForResult(intent,
//						ConstantGloble.ACTIVITY_RESULT_CODE);
				BaseActivity.getLoginUtils(FundqueryMenuActivity.this).exe(new LoginTask.LoginCallback() {

					@Override
					public void loginStatua(boolean isLogin) {

					}
				});
				return;
			}
			flag = HISTORY;
			BaseHttpEngine.showProgressDialog();
			doCheckRequestPsnInvestmentManageIsOpen();
			break;
		}
	}

	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		super.httpRequestCallBackPre(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
			if (biiResponseBody.getError().getCode()
					.equals(ErrorCode.FINC_ACCCHECIN_ERROR)) {
				fincControl.ifhaveaccId = false;
				fincControl.ifdorisk = false;
				BaseHttpEngine.dissMissProgressDialog();
				getPopup();
				return true;

			}
		}
		return super.httpRequestCallBackPre(resultObj);

	}

}
