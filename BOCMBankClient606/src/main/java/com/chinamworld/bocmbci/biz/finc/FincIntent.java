package com.chinamworld.bocmbci.biz.finc;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.biz.finc.fundacc.FincFundAccMainActivity;
import com.chinamworld.bocmbci.biz.finc.fundprice.FincFundDetailActivityNew;
import com.chinamworld.bocmbci.biz.finc.fundprice.FundPricesActivityNew;
import com.chinamworld.bocmbci.biz.finc.orcm.OrcmProductListActivity;
import com.chinamworld.bocmbci.biz.finc.query.FincQueryDQDEActivity;
import com.chinamworld.bocmbci.biz.invest.activity.InvesAgreeActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

public class FincIntent {
	private Map<String, Object> ParamMap = new HashMap<String, Object>();
	private String FincDetailLayout_layout = "PsnFundDetailQueryOutlay ";
	private String FincDetailLayout = "PsnQueryFundDetail ";
	private String fincCodeString;
	private Activity context;
	protected HttpTools httpTools;
	protected FincControl fincControl = FincControl.getInstance();
	
	private static FincIntent fincIntent = null;
	public static FincIntent getIntent(){
		if (fincIntent == null)
			fincIntent = new FincIntent();
		return fincIntent;
	}
	public void fincIntent(Activity activity, String fincCode) {
		fincCodeString = fincCode;
		// 判断finccode进入基金详情界面，登陆后需要判断开通投资理财
		context = activity;
		httpTools = new HttpTools(activity, null);
		fincCodeString = fincCode;
		if (BaseDroidApp.getInstanse().isLogin()) {
			// //登陆后的详情界面FincFundDetailActivityNew
			BaseHttpEngine.showProgressDialogCanGoBack();
			doCheckRequestPsnInvestmentManageIsOpen();
			// attentionFundQuery();

		} else {// 未登录界面FundPricesActivityNew

			PsnFundDetailQueryOutlay(fincCode);
		}

	}
	// 未登陆进入基金，调用接口
	public void PsnFundDetailQueryOutlay(String fincCode) {
		// "params": {
		// "currentIndex": "0",
		// "fundInfo": "900001",
		// "fundKind": "00",
		// "fundState": "",
		// "fundType": "00",
		// "id": "568212572",
		// "pageSize": "10"
		// }
		ParamMap.put(Finc.PSNQUERYFUNDDETAIL_FUNDINFO, fincCode);
		ParamMap.put(Finc.COMBINQUERY_CURRENTINDEX, String.valueOf("0"));
		ParamMap.put(Finc.COMBINQUERY_PAGESIZE, String.valueOf("10"));
		ParamMap.put(Finc.PSNQUERYFUNDDETAIL_FUNDTYPE, "00");
		ParamMap.put(Finc.PSNQUERYFUNDDETAIL_FUNDKIND, "00");
		ParamMap.put(Finc.PSNQUERYFUNDDETAIL_FUNDSTATE, "");
		ParamMap.put(Finc.PSNQUERYFUNDDETAIL_SORTFLAG, "");
		ParamMap.put(Finc.PSNQUERYFUNDDETAIL_SORTFIELD, "");
		httpTools
				.requestHttp(FincDetailLayout_layout,
						"requestPsnFundDetailQueryOutlayCallBack",
						ParamMap, false);
	}

	public void requestPsnFundDetailQueryOutlayCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) resultMap
				.get(Finc.COMBINQUERY_LIST);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		FincControl.getInstance().fundDetails = resultList.get(0);
		context.startActivity(new Intent(context,
				FincFundDetailActivityNew.class));
	}

	/**
	 * 基金判断是否开通投资理财服务
	 */
	public void doCheckRequestPsnInvestmentManageIsOpen() {
		// BiiRequestBody biiRequestBody = new BiiRequestBody();
		// biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_API);
		// biiRequestBody.setConversationId(null);
		// biiRequestBody.setParams(null);
		httpTools.requestHttp(Comm.PSNINVESTMENTMANAGEISOPEN_API,
				"doCheckRequestPsnInvestmentManageIsOpenCallback", null, false);
	}

	/**
	 * 检查时调用的 是否开通中银理财服务 回调
	 * 
	 * @param resultObj
	 */
	public void doCheckRequestPsnInvestmentManageIsOpenCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String isOpenOr = (String) biiResponseBody.getResult();
		if (StringUtil.parseStrToBoolean(isOpenOr)) {
			FincControl.getInstance().ifInvestMent = true;
		} else {
			FincControl.getInstance().ifInvestMent = false;
		}
		doCheckRequestQueryInvtBindingInfo();

	}

	/**
	 * 查询基金账户check
	 */
	public void doCheckRequestQueryInvtBindingInfo() {
		// BiiRequestBody biiRequestBody = new BiiRequestBody();
		// biiRequestBody.setMethod(Finc.FINC_QUERYINVTBINDINGINFO_API);
		// biiRequestBody.setConversationId(null);
		Map<String, Object> map = new Hashtable<String, Object>();
		map.put(Finc.FINC_INVTTYPE_REQ, ConstantGloble.FINC_SERVICECODE);
		// biiRequestBody.setParams(map);
		httpTools.requestHttp(Finc.FINC_QUERYINVTBINDINGINFO_API,
				"doCheckRequestQueryInvtBindingInfoCallback", map, false);
		// httpTools.requestHttp(Comm.PSNINVESTMENTMANAGEISOPEN_API,
		// "doCheckRequestPsnInvestmentManageIsOpenCallback",null,false);
	}

	/**
	 * 这个东西怎么处理
	 * 
	 * @param resultObj
	 */
	public void doCheckRequestQueryInvtBindingInfoCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getResult() == null) {
			FincControl.getInstance().accId = null;
			FincControl.getInstance().ifhaveaccId = false;
			BaseHttpEngine.dissMissProgressDialog();
			getPopup();
			return;
		} else {
			Map<String, String> map = (Map<String, String>) biiResponseBodys
					.get(0).getResult();
			if (!StringUtil.isNullOrEmpty(map.get(Finc.FINC_INVESTACCOUNT_RES))) {
				FincControl.getInstance().accId = map.get(Finc.FINC_ACCOUNTID_RES);
				FincControl.getInstance().bankId = map.get(Finc.FINC_BANKID_RES);
				FincControl.getInstance().invAccId = map.get(Finc.FINC_INVESTACCOUNT_RES);
				FincControl.getInstance().accNum = map.get(Finc.FINC_ACCOUNT_RES);
				FincControl.getInstance().accDetailsMap = map;
				FincControl.getInstance().ifhaveaccId = true;
			}
		}
		if (!FincControl.getInstance().ifInvestMent) {
			BaseHttpEngine.dissMissProgressDialog();
			getPopup();
			return;
		}else {
		 // 查询账户详情接口
//			requestCommConversationId();
//			
//			"currentIndex": "0",
//			"fundInfo": "163808",
//			"fundKind": "00",
//			"fundState": "",
//			"fundType": "00",
//			"id": "731465477",
//			"pageSize": "10"
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(Finc.PSNQUERYFUNDDETAIL_FUNDINFO, fincCodeString);
			params.put(Finc.COMBINQUERY_CURRENTINDEX, String.valueOf("0"));
			params.put(Finc.COMBINQUERY_PAGESIZE, String.valueOf("10"));
			params.put(Finc.PSNQUERYFUNDDETAIL_FUNDTYPE, "00");
			params.put(Finc.PSNQUERYFUNDDETAIL_FUNDKIND, "00");
			params.put(Finc.PSNQUERYFUNDDETAIL_FUNDSTATE, "");
			params.put(Finc.PSNQUERYFUNDDETAIL_SORTFLAG, "");
			params.put(Finc.PSNQUERYFUNDDETAIL_SORTFIELD, "");
			httpTools
					.requestHttp(FincDetailLayout,
							"requestPsnFundDetailQueryOutlayCallBack",
							params, true);
			 
		 }

	}

	/**
	 * 任务提示框
	 */
	public void getPopup() {
		View popupView = LayoutInflater.from(context).inflate(
				R.layout.finc_task_notify, null);
		// 关闭按钮
		ImageView taskPopCloseButton = (ImageView) popupView
				.findViewById(R.id.top_right_close);
		// 设定账户
		LinearLayout accButtonView = (LinearLayout) popupView
				.findViewById(R.id.forex_acc_button_show);
		LinearLayout accTextView = (LinearLayout) popupView
				.findViewById(R.id.forex_acc_text_hide);
		// TextView setAccButton = (TextView) popupView
		// .findViewById(R.id.forex_acc_button);

		// 理财服务功能
		LinearLayout moneyButtonView = (LinearLayout) popupView
				.findViewById(R.id.forex_money_button_show);
		LinearLayout moneyTextView = (LinearLayout) popupView
				.findViewById(R.id.forex_money_text_hide);

		// 风险评估
		LinearLayout risktestBtnLayout = (LinearLayout) popupView
				.findViewById(R.id.finc_risk_button_show);
		risktestBtnLayout.setVisibility(View.GONE);
		LinearLayout risktestTextLayout = (LinearLayout) popupView
				.findViewById(R.id.finc_risk_text_hide);
		// TextView ristTestButton = (TextView) popupView
		// .findViewById(R.id.finc_risktest_button);

		// 先判断是否开通投资理财服务
		if (fincControl.ifInvestMent && fincControl.ifhaveaccId) {
			// finishPopupWindow();
			return;
		}
		if (fincControl.ifInvestMent) {// 已经开通投资理财
			moneyButtonView.setVisibility(View.GONE);
			moneyTextView.setVisibility(View.VISIBLE);
		} else {// 没有开通投资理财服务开通投资理财服务
			moneyButtonView.setVisibility(View.VISIBLE);
			moneyTextView.setVisibility(View.GONE);
			moneyButtonView.setOnClickListener(new OnClickListener() {
				// @Override
				public void onClick(View v) {
					// 跳转到投资理财服务协议页面
					Intent gotoIntent = new Intent(BaseDroidApp.getInstanse()
							.getCurrentAct(), InvesAgreeActivity.class);
					context.startActivityForResult(gotoIntent,
							ConstantGloble.ACTIVITY_REQUEST_MANAGE_CODE);

				}
			});
		}
		if (fincControl.ifhaveaccId) {// 有基金账户
			accButtonView.setVisibility(View.GONE);
			accTextView.setVisibility(View.VISIBLE);
		} else {// 没基金账户 设定 基金账户
			accButtonView.setVisibility(View.VISIBLE);
			accTextView.setVisibility(View.GONE);
			if (fincControl.ifInvestMent) {
				accButtonView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 到设定基金账户设定页面
						Intent gotoIntent = new Intent(BaseDroidApp
								.getInstanse().getCurrentAct(),
								FincFundAccMainActivity.class);
						context.startActivityForResult(
								gotoIntent,
								ConstantGloble.ACTIVITY_REQUEST_SETFINCSACC_CODE);
					}
				});
			} else {
				accButtonView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						CustomDialog.toastInCenter(
								BaseDroidApp.getInstanse().getCurrentAct(),
								BaseDroidApp
										.getInstanse()
										.getCurrentAct()
										.getString(
												R.string.bocinvt_task_toast_1));

					}
				});
			}
		}
		// 关闭按钮事件
		taskPopCloseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseDroidApp.getInstanse().dismissMessageDialog();
				// modi by fsm
				if (BaseDroidApp.getInstanse().getCurrentAct() instanceof FincQueryDQDEActivity)
					BaseDroidApp.getInstanse().getCurrentAct().finish();

				// 503add
				if (BaseDroidApp.getInstanse().getCurrentAct() instanceof FincFundDetailActivityNew)
					ActivityTaskManager.getInstance().removeAllActivity();
				// BaseDroidApp.getInstanse().getCurrentAct().finish();

				if (BaseDroidApp.getInstanse().getCurrentAct() instanceof FundPricesActivityNew) {
					// ActivityTaskManager.getInstance().removeAllSecondActivity();
					context.finish();
				}
				// TODO 基金推荐弹框，点击X之后，返回到首页
				if (BaseDroidApp.getInstanse().getCurrentAct() instanceof OrcmProductListActivity) {
					context.finish();
				}
			}
		});
		// taskPop.setFocusable(true);
		BaseDroidApp.getInstanse().showAccountMessageDialog(popupView);

	}
	// /**
	// * 请求conversationId 来登录之外的conversation出Id
	// */
	// public void requestCommConversationId() {
	// BiiRequestBody biiRequestBody = new BiiRequestBody();
	// biiRequestBody.setMethod(Comm.CONVERSATION_ID_API);
	// BaseHttpManager.Instance.requestBii(biiRequestBody, this,
	// "requestCommConversationIdCallBack");
	// }
	// /**
	// * 请求conversationId 来登录之外的conversation出Id
	// *
	// */
	// public void requestCommConversationId(IHttpCallBack callBack) {
	// httpCallBack = callBack;
	// BiiRequestBody biiRequestBody = new BiiRequestBody();
	// biiRequestBody.setMethod(Comm.CONVERSATION_ID_API);
	// BaseHttpManager.Instance.requestBii(biiRequestBody, this,
	// "requestCommConversationIdCallBack");
	// }
}
