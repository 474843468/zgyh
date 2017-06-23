package com.chinamworld.bocmbci.biz.finc;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.base.application.CommonApplication;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.biz.finc.finc_p606.FincProductDetailActivity;
import com.chinamworld.bocmbci.biz.finc.fundacc.FincFundAccMainActivity;
import com.chinamworld.bocmbci.biz.invest.activity.InvesAgreeActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.mode.IActionCall;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utiltools.HttpHandle;
import com.chinamworld.bocmbci.widget.CustomDialog;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class FincIntentNew extends HttpHandle {

	private String fincCodeString;
	private static Activity context;

	public FincIntentNew(Activity activity){
		super(activity);
		context = activity;
		CommonApplication.getInstance().setCurrentAct(activity); // 从软件中心模块跳过来，需要先保存当前的activity
	}
	protected FincControl fincControl = FincControl.getInstance();
	
//	private static FincIntentNew fincIntent = null;
//	public static FincIntentNew getIntent(){
//		if (fincIntent == null)
//			fincIntent = new FincIntentNew(context);
//		return fincIntent;
//	}
	private IActionCall successCall;

	public void fincIntent(Activity activity, String fincCode, IActionCall call){
		successCall = call;
		fincIntent(activity,fincCode);
	}

	public void fincIntent(Activity activity, String fincCode) {
//		successCall = null;
		fincCodeString = fincCode;
		// 判断finccode进入基金详情界面，登陆后需要判断开通投资理财
		context = activity;

		fincCodeString = fincCode;
		if (BaseDroidApp.getInstanse().isLogin()) {
			BaseHttpEngine.showProgressDialogCanGoBack();
			doCheckRequestPsnInvestmentManageIsOpen();
			// attentionFundQuery();
		} else {
//			BaseHttpEngine.showProgressDialog();
			BaseHttpEngine.showProgressDialogCanGoBack();
			PsnFundDetailQueryOutlay(fincCode);
		}

	}
	// 未登陆进入基金，调用接口 基金详情
	public void PsnFundDetailQueryOutlay(String fincCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.I_FUNDCODE, fincCode);
		httpTools.requestHttpOutlayWithNoDialog("PsnFundDetailQueryOutlay",
						"requestPsnFundDetailQueryOutlayCallBack2",
						map, false);
	}

	public void requestPsnFundDetailQueryOutlayCallBack2(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		fincControl.fundDetails = resultMap;
		//请求四方数据 修改为进入页面后再请求四方数据
		BaseHttpEngine.dissMissProgressDialog();
		if(successCall!=null){
			successCall.callBack();
		}
//		getQueryFundBasicDetail(fincCodeString);
	//	FincControl.getInstance().fundDetails = resultList.get(0);
//		context.startActivity(new Intent(context,
//				FincFundDetailActivityNew.class));
	}

//	/**
//	 * 3.2 基金详情接口（基本信息）
//	 * @param fundId 基金Id或code
//	 */
//	private void getQueryFundBasicDetail(String fundId){
//		QueryFundBasicDetailRequestParams requestParams = new QueryFundBasicDetailRequestParams(fundId);
//		HttpHelp h= new HttpHelp();
//		h.postHttpFromSF(context,requestParams);
//		h.setHttpErrorCallBack(null);
//		h.setHttpResponseCallBack(new IHttpResponseCallBack() {
//			@Override
//			public boolean responseCallBack(String response, Object extendParams) {
//				QueryFundBasicDetailResponseData data = GsonTools.fromJson(response,QueryFundBasicDetailResponseData.class);
//				QueryFundBasicDetailResult body = data.getBody();
//				fincControl.fundDetails.put("siFangFundDetail",body);
//				if (BaseDroidApp.getInstanse().isLogin()) {
//					BaseHttpEngine.showProgressDialog();
//					attentionFundQuery();
//				} else {
//					BaseHttpEngine.dissMissProgressDialog();
//					if(successCall!=null){
//						successCall.callBack();
//					}
//					//ActivityIntentTools.intentToActivity(context,FincProductDetailActivity.class);
//				}
//				return false;
//			}
//		});
//	}


	/**
	 * 基金判断是否开通投资理财服务
	 */
	public void doCheckRequestPsnInvestmentManageIsOpen() {
		httpTools.requestHttpWithNoDialog(Comm.PSNINVESTMENTMANAGEISOPEN_API,
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
		Map<String, Object> map = new Hashtable<String, Object>();
		map.put(Finc.FINC_INVTTYPE_REQ, ConstantGloble.FINC_SERVICECODE);
		httpTools.requestHttpWithNoDialog(Finc.FINC_QUERYINVTBINDINGINFO_API,
				"doCheckRequestQueryInvtBindingInfoCallback", map, false);
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
			getFincFund(fincCodeString);
			 
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
				if (BaseDroidApp.getInstanse().getCurrentAct() instanceof FincProductDetailActivity)
					BaseDroidApp.getInstanse().getCurrentAct().finish();
			}
		});
		// taskPop.setFocusable(true);
		BaseDroidApp.getInstanse().showAccountMessageDialog(popupView);

	}

	/**登陆后基金详情查询
	 * 基金基本信息查询
	 * @param fundCode
	 */
	protected void getFincFund(String fundCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.I_FUNDCODE, fundCode);
		httpTools.requestHttpWithNoDialog(Finc.FINC_GETFUNDDETAIL, "getFincFundCallback", map, false);
	}

	/**
	 * 登陆后基金详情查询  回调处理
	 * @param resultObj
	 */
	public void getFincFundCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		fincControl.fundDetails = resultMap;
		//请求四方数据 修改为进入页面后请求
//		getQueryFundBasicDetail(fincCodeString);
//		BaseHttpEngine.showProgressDialog();
		attentionFundQuery();
	}

	/** 查询关注的基金 */
	public void attentionFundQuery() {
		httpTools.requestHttpWithNoDialog(Finc.FINC_ATTENTIONQUERYLIST, "attentionFundQueryCallback", null, false);
	}

	/**
	 * 查询关注的基金 返回处理
	 *
	 * @param resultObj
	 */
	public void attentionFundQueryCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (!StringUtil.isNullOrEmpty(resultMap)) {
			if (!StringUtil.isNullOrEmpty(resultMap
					.get(Finc.FINC_ATTENTIONQUERYLIST_ATTENTIONLIST))) {
				FincControl.getInstance().attentionFundList = (List<Map<String, Object>>) resultMap
						.get(Finc.FINC_ATTENTIONQUERYLIST_ATTENTIONLIST);
				for (Map<String, Object> map : FincControl.getInstance().attentionFundList) {
					// 已关注的基金
					String fundCodeAttentioned = (String) map.get(Finc.FINC_FUNDCODE);
					// 当前选择的基金
					String fundCodeCurrent = (String) fincControl.fundDetails
							.get(Finc.FINC_FUNDCODE);
					if (fundCodeAttentioned.equals(fundCodeCurrent)) {// 如果已关注
						FincControl.getInstance().setAttentionFlag(true);
					}
				}
			}
		}
		if (StringUtil.isNullOrEmpty(fincControl.attentionFundList)) {
			FincControl.getInstance().setAttentionCount(0);
		} else {
			FincControl.getInstance().setAttentionCount(
					fincControl.attentionFundList.size());
		}
		requestPsnFundQueryFundBalance(fincCodeString);
	}

	/**
	 * @author 基金持仓--查询基金持仓信息
	 */
	public void requestPsnFundQueryFundBalance(String fundCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.I_FUNDCODE, fundCode);
		httpTools.requestHttpWithNoDialog(Finc.FINC_FUNDQUERYFUNDBALANCE_API, "requestPsnFundQueryFundBalanceCallback", map, false);
		httpTools.registAllErrorCode(Finc.FINC_FUNDQUERYFUNDBALANCE_API);
	}

	/**
	 * @author  基金持仓--查询基金持仓信息--回调
	 */
	public void requestPsnFundQueryFundBalanceCallback(Object resultObj) {
		if(!fincControl.ifhaveaccId || !fincControl.ifInvestMent) return;
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		/**
		 * PsnFundQueryFund查询基金持仓信息
		 */
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		if (result == null || StringUtil.isNullOrEmpty(((List<Map<String,Object>>) result
				.get(Finc.FINC_FUNDBALANCE_REQ))) == true) {
			fincControl.fundBalancList = null;
		} else {
			fincControl.fundBalancList = (List<Map<String, Object>>) result
					.get(Finc.FINC_FUNDBALANCE_REQ);
		}
		if(!StringUtil.isNullOrEmpty(fincControl.fundBalancList)){//不为空时进行浮动盈亏试算
			Map<String, Object> fundBalancList_new = (Map<String, Object>) fincControl.fundBalancList.get(0)
					.get(Finc.FINC_FUNDINFO_REQ);
			String isFdyk=(String) fundBalancList_new.get("isFdyk");
			if(isFdyk.equals("Y")){//
				requestSystemDateTime();
			}else{
				BaseHttpEngine.dissMissProgressDialog();
				if(successCall!=null){
					successCall.callBack();
				}
			}
		}else{
			BaseHttpEngine.dissMissProgressDialog();
			if(successCall!=null){
				successCall.callBack();
			}
		}
		//ActivityIntentTools.intentToActivity(context,FincProductDetailActivity.class);
	}

	/**
	 * 请求系统时间
	 */
	public void requestSystemDateTime() {
		httpTools.requestHttp("PsnCommonQuerySystemDateTime", "requestSystemDateTimeCallBack", null, false);
	}

	/**
	 * 系统时间回调
	 * @param resultObj
	 */
	public void requestSystemDateTimeCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse)resultObj;
		List biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = (BiiResponseBody)biiResponseBodys.get(0);
		Map resultMap = (Map)biiResponseBody.getResult();
		String dateTime = (String)resultMap.get("dateTme");
		String startDateStr = QueryDateUtils.getsysDateOneYear(dateTime);//获得一年前日期
		String endDateStr = QueryDateUtils.getFincLastDay(dateTime);//系统日期前一天
		getFDYKList(fincCodeString, startDateStr, endDateStr);
	}

	/**
	 * 浮动盈亏 测算 20 基金35
	 */
	public void getFDYKList(String fundCode, String startDate, String endDate) {
		BaseHttpEngine.showProgressDialog();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_FUNDCODE, fundCode);
		map.put(Finc.FINC_STARTDATE, startDate);
		map.put(Finc.FINC_ENDDATE, endDate);
		httpTools.requestHttp(Finc.FINC_FLOATPROFITANDLOSS, "getFDYKListCallback", map, true);
		httpTools.registAllErrorCode(Finc.FINC_FLOATPROFITANDLOSS);
	}

	/**
	 * 试算回调
	 * @param resultObj
     */
	public void getFDYKListCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) biiResponseBody.getResult();
		String middleFloat = "";
		if(!StringUtil.isNullOrEmpty(resultList)){
			middleFloat = (String)resultList.get(0).get(Finc.FINC_FLOATPROFITANDLOSS_MIDDLEFLOAT);
			fincControl.fundDetails.put("middleFloat",middleFloat);
		}
		BaseHttpEngine.dissMissProgressDialog();
		if(successCall!=null){
			successCall.callBack();
		}
	}

	/**
	 * 根据扫描出的二维码信息跳转到对应页面
	 * @param activity
	 * @param qrInfo 二维码文本信息
     * @return
     */
	public boolean fundQRHandler(Activity activity,String qrInfo){
		//解析qrInfo取出类型和基金代码 boc://bocphone?type=2&fundCode=380001
		String str1 = "boc://bocphone?type=";
		String str2 = "boc://bocphone?type=2&fundCode=";
		if(!StringUtil.isNullOrEmpty(qrInfo)&&qrInfo.length()>str1.length()){
			int type = Integer.parseInt(qrInfo.substring(str1.length(),str1.length()+1));
			if(type == 2){
				if(qrInfo.length()>str2.length()){
					String fundCode = qrInfo.substring(str2.length(),qrInfo.length());
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("fromOtherFundCode", fundCode);
					map.put("isFromOther", "1");
					ActivityIntentTools.intentToActivityWithData(activity, FincProductDetailActivity.class, map);
					return true;
				}
			}
		}
		return false;
	}
}
