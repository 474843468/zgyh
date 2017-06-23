package com.chinamworld.bocmbci.biz.finc.query;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.invest.constant.InvestConstant;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.ErrorCode;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 基金定投管理三级菜单
 * 
 * @author fsm
 * 
 */
public class FundDQDEMenuActivity extends FincBaseActivity {

	/** 有效定投申请,已失效定投申请 */
	private View finc_scheduled_available, finc_scheduled_unavailable;
	/** 标记点击的交易类型 */
	private int flag;
	private final int AVAILABLE = 1;// 有效
	private final int UNAVAILABLE = 2;// 已失效

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		fincControl.cleanAllData();
		setLeftSelectedPosition("finc_4");
	}

	private void initView() {
		View childView = mainInflater.inflate(R.layout.finc_dqde_menu, null);
		tabcontent.addView(childView);
		finc_scheduled_available = childView
				.findViewById(R.id.finc_scheduled_available);
		finc_scheduled_unavailable = childView
				.findViewById(R.id.finc_scheduled_unavailable);
		setTitle(R.string.finc_query_dtgl);
		right.setVisibility(View.GONE);
	}

	private void initListenner() {
		finc_scheduled_available.setOnClickListener(this);
		finc_scheduled_unavailable.setOnClickListener(this);
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
				case AVAILABLE: // 有效定投申请
					BaseHttpEngine.showProgressDialog();
					requestCommConversationId();
					break;
				case UNAVAILABLE: // 已失效定投申请
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
				case AVAILABLE: // 有效定投申请
					BaseHttpEngine.showProgressDialog();
					requestCommConversationId();
					break;
				case UNAVAILABLE: // 已失效定投申请
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
		switch (flag) {
		case AVAILABLE: // 有效定投申请
			fundQueryDQDE("0", "10", true, null, null);
//			BaseHttpEngine.dissMissProgressDialog();
//			Intent intent = new Intent();
//			intent.setClass(this,FincQueryDQDEActivity.class);
//			startActivityForResult(intent, DQDE_AVAILABLE);
			break;
		case UNAVAILABLE: // 已失效定投申请
			requestScheduledFundUnavailableQuery(0, true);
			break;
		}
	}
	
	@Override
	public void fundQueryDQDECallback(Object resultObj) {
		super.fundQueryDQDECallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if(StringUtil.isNullOrEmpty(fincControl.fundAvailableList)){
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.finc_query_noresult_error));
			return;
		}
		Intent intent = new Intent();
		intent.setClass(this,FincQueryDQDEActivity.class);
		startActivityForResult(intent, DQDE_AVAILABLE);
	}

	@Override
	public void scheduledFundUnavailableQueryCallback(Object resultObj) {
		super.scheduledFundUnavailableQueryCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if(StringUtil.isNullOrEmpty(fincControl.fundUnavailableList))
			return;
		Intent intent = new Intent();
		intent.setClass(this,FincQueryDQDEUnavailableActivity.class);
		startActivityForResult(intent, DQDE_UNAVAILABLE);
	}

	@Override
	public void doCheckRequestQueryInvtBindingInfoCallback(
			Object resultObj) {
		super.doCheckRequestQueryInvtBindingInfoCallback(resultObj);
		if(!fincControl.ifhaveaccId || !fincControl.ifInvestMent) return;
		requestCommConversationId();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_scheduled_available:// 有效定投申请
			if (!BaseDroidApp.getInstanse().isLogin()) {// 没有登录就跳转到登录页面
//				Intent intent = new Intent(FundDQDEMenuActivity.this,
//						LoginActivity.class);
//				startActivityForResult(intent,
//						ConstantGloble.ACTIVITY_RESULT_CODE);
				BaseActivity.getLoginUtils(FundDQDEMenuActivity.this).exe(new LoginTask.LoginCallback() {

					@Override
					public void loginStatua(boolean isLogin) {

					}
				});
				return;
			}
			flag = AVAILABLE;
			BaseHttpEngine.showProgressDialog();
			doCheckRequestPsnInvestmentManageIsOpen();
			break;
		case R.id.finc_scheduled_unavailable:// 已失效定投申请
			if (!BaseDroidApp.getInstanse().isLogin()) {// 没有登录就跳转到登录页面
//				Intent intent = new Intent(FundDQDEMenuActivity.this,
//						LoginActivity.class);
//				startActivityForResult(intent,
//						ConstantGloble.ACTIVITY_RESULT_CODE);
				BaseActivity.getLoginUtils(FundDQDEMenuActivity.this).exe(new LoginTask.LoginCallback() {

					@Override
					public void loginStatua(boolean isLogin) {

					}
				});
				return;
			}
			flag = UNAVAILABLE;
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
