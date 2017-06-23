package com.chinamworld.bocmbci.biz.investTask;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bocinvt.inflateView.InflaterViewDialog;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.InvtBindingChooseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.InvtEvaluationInputActivity;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.TestInvtEvaluationAnswerActivity;
import com.chinamworld.bocmbci.biz.invest.activity.InvesAgreeFirstActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.IHttpResponseCallBack;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 做三个任务 一、开通投资理财 二、账户登记 三、风险评估
 * 
 * @author yuht
 */
public class BOCDoThreeTask extends InvestBaseTask implements IHttpResponseCallBack<Map<String,Object>>{
	@Override
	public void commonHttpErrorCallBack(String s) {

	}

	/**
	 * 1: 仅做风险评估 2：开通投资理财，账户登记 3：开通投资理财，账户登记，风险评估
	 */
	private String type = null;

	private BOCDoThreeTask(BaseActivity context) {
		super(context);
	}

	public static BOCDoThreeTask getInstance(BaseActivity context) {
		return new BOCDoThreeTask(context);
	}

	private boolean isOpen = false;
	private boolean isInvtBinding = false;
	private boolean isevaluatedBefore = false;

	/**
	 * 是否是无效的风险评估
	 */
	private boolean isInvaildEvaluated = false;

	@Override
	protected View getTaskView() {
		InflaterViewDialog inflater = new InflaterViewDialog(context);
		if (type.equals("3")) {
			return (RelativeLayout) inflater.judgeViewDialog_choice(isOpen,
					investBindingInfo, isevaluatedBefore, manageOpenClick,
					invtBindingClick, invtEvaluationClick, exitClick);
		} else if (type.equals("2")) {
			View view = (RelativeLayout) inflater.judgeViewDialog_choice(
					isOpen, investBindingInfo, isevaluatedBefore, manageOpenClick,
					invtBindingClick, null, exitClick);
			((TextView) view.findViewById(R.id.tv_acc_account_accountState))
					.setText(context.getResources().getString(
							R.string.bocinvt_tv_76));
			return view;
		} else if (type.equals("1")) {
			View view = (RelativeLayout) inflater.judgeViewDialog_choice(
					isOpen, investBindingInfo, false, null, null,
					invtEvaluationClick, exitClick);
			((TextView) view.findViewById(R.id.tv_acc_account_accountState))
					.setText(context.getResources().getString(
							R.string.bocinvt_tv_77));
			return view;
		}
		return null;
	}

	/** 开通投资理财监听事件 */
	protected OnClickListener manageOpenClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// 跳转到投资理财服务协议页面
			ActivityIntentTools.intentToActivityForResult(context,
					InvesAgreeFirstActivity.class,
					ConstantGloble.ACTIVITY_RESULT_CODE, null);
			context.overridePendingTransition(R.anim.push_up_in,
					R.anim.no_animation);
		}
	};

	/** 登记账户监听事件 */
	OnClickListener invtBindingClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (isOpen == false) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("请先开通投资理财服务");
				return;
			}
			// 通讯开始,展示通讯框
			BiiHttpEngine.showProgressDialog();
			httpTools.requestHttp(BocInvt.PSNXPADRESET_API, null,
					new IHttpResponseCallBack<List<Map<String, Object>>>() {
						@Override
						public void httpResponseSuccess(
								List<Map<String, Object>> result,String method) {
							// 通讯结束,隐藏通讯框
							BiiHttpEngine.dissMissProgressDialog();
							List<Map<String, Object>> responseList = result;
							if (StringUtil.isNullOrEmpty(responseList)) {
								// 没有可登记账户
								BaseDroidApp
										.getInstanse()
										.showInfoMessageDialog(
												context.getString(R.string.bocinvt_binding_relevance));
								return;
							}
							// 有可登记账户
							BaseDroidApp
									.getInstanse()
									.getBizDataMap()
									.put(ConstantGloble.BOCINVT_XPADRESET_LIST,
											responseList);
							ActivityIntentTools.intentToActivityForResult(
									context, InvtBindingChooseActivity.class,
									100, null);
							context.overridePendingTransition(
									R.anim.push_up_in, R.anim.no_animation);

						}
					});
		}
	};

	/** 风险评估监听事件 */
	OnClickListener invtEvaluationClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if ("3".equals(type) && isOpen == false) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("请先开通投资理财服务");
				return;
			}
			
			Class<?> classType = TestInvtEvaluationAnswerActivity.class; // 默认为无效的风险评估，跳到对应的页面
			if (isInvaildEvaluated == false) {
				classType = InvtEvaluationInputActivity.class;
			}
			ActivityIntentTools.intentToActivityForResult(context, classType,
					200, null);
			context.overridePendingTransition(R.anim.push_up_in,
					R.anim.no_animation);
		}
	};

	protected OnClickListener exitClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (exitActionCallBack != null) {
				if (false == exitActionCallBack.callBack(v))
					return;
			}
			BaseDroidApp.getInstanse().dismissMessageDialog();
			context.finish();
		}
	};

	@Override
	protected boolean getDoTaskStatus() {
		if (type.equals("1")) {
			return isevaluatedBefore;
		} else if (type.equals("2")) {
			return isOpen && isInvtBinding;
		} else if (type.equals("3")) {
			return isOpen && isInvtBinding && isevaluatedBefore;
		}
		return true;
	}

	@Override
	protected void getTaskStatusOnLine(IAction actionCallBack) {
		OnLineRequestCallBack = actionCallBack;
		type = taskParam.toString();
		BiiHttpEngine.showProgressDialogCanGoBack();
		if (type.equals("1")) {
			httpTools.requestHttp(BocInvt.PSNINVTEVALUATIONINIT_API, null,this);
			BaseHttpEngine.showProgressDialogCanGoBack();
		} else if (type.equals("2")) {
			httpTools.requestHttp(Comm.PSNINVESTMENTMANAGEISOPEN_API,
					"requestPsnInvestmentisOpenBeforeCallback", null, false);
			BaseHttpEngine.showProgressDialogCanGoBack();
		} else if (type.equals("3")) {
			httpTools.requestHttp(Comm.PSNINVESTMENTMANAGEISOPEN_API,
					"requestPsnInvestmentisOpenBeforeCallback", null, false);
			BaseHttpEngine.showProgressDialogCanGoBack();
		}
	}

	/**
	 * 判断是否开通投资理财服务---回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnInvestmentisOpenBeforeCallback(Object resultObj) {
		String isOpenresult = String.valueOf(HttpTools
				.getResponseResult(resultObj));
		if (StringUtil.isNull(isOpenresult)) {
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}
		isOpen = Boolean.valueOf(isOpenresult);
		requestBociAcctList("1", "1");
	}

	/**
	 * 请求理财账户信息
	 * 
	 * @param acctSatus
	 * @param acctType
	 */
	private void requestBociAcctList(String acctSatus, String acctType) {
		Map<String, Object> map = new Hashtable<String, Object>();
		map.put(BocInvt.ACCOUNTSATUS, acctSatus);
		map.put(BocInvt.QUERYTYPE, acctType);
		httpTools.requestHttp("PsnXpadAccountQuery", map,
				new IHttpResponseCallBack<Map<String, Object>>() {

					@Override
					public void httpResponseSuccess(Map<String, Object> result,String method) {
						// TODO Auto-generated method stub
						Map<String, Object> map = result;
						investBindingInfo = (List<Map<String, Object>>) map
								.get(BocInvt.BOCI_LIST_RES);
						isInvtBinding = StringUtil
								.isNullOrEmpty(investBindingInfo) == false;

						BaseDroidApp
								.getInstanse()
								.getBizDataMap()
								.put(ConstantGloble.BOCINVT_XPADRESET_CHOOSE,
										investBindingInfo);
						if("2".equals(type)) {
							BiiHttpEngine.dissMissProgressDialog();
							OnLineRequestCallBack.SuccessCallBack(null);
						}
						else if("3".equals(type)){
							if(isOpen == true) { // 如果已开通投资理财，才可以调用查询风险评估接口
								httpTools.requestHttp(BocInvt.PSNINVTEVALUATIONINIT_API, null,BOCDoThreeTask.this);
							}
						}
					}
				});
		BaseHttpEngine.showProgressDialogCanGoBack();
	}

	/**
	 * 登记账户信息
	 */
	private List<Map<String, Object>> investBindingInfo;

	@Override
	protected boolean onActivityForResult(int requestCode, int resultCode,
			Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
				// 开通成功的响应
				isOpen = true;
				if("3".equals(type)){
					httpTools.requestHttp(BocInvt.PSNINVTEVALUATIONINIT_API, null,BOCDoThreeTask.this);
					return false;
				}
			} else if (requestCode == 100) { // 登记成功
				isInvtBinding = true;
			} else if (requestCode == 200) { // 风险评估完成
				isevaluatedBefore = true;
			} else
				return false;
			return true;
		}
		return false;
	}

	@Override
	public void httpResponseSuccess(Map<String, Object> result,String method) {
		if(BocInvt.PSNINVTEVALUATIONINIT_API.equals(method)){
			Map<String, Object> responseMap = result;
			BiiHttpEngine.dissMissProgressDialog();
			if (StringUtil.isNullOrEmpty(responseMap)) {
				return;
			}

			Map<String, String> inputMap = new HashMap<String, String>();
			inputMap.put(BocInvt.BOCINVT_EVA_GENDER_REQ,
					(String.valueOf(responseMap
							.get(BocInvt.BOCIEVA_GENDER_RES))));
			inputMap.put(BocInvt.BOCINVT_EVA_RISKBIRTHDAY_REQ,
					(String) responseMap
							.get(BocInvt.BOCIEVA_BIRTHDAY_RES));
			inputMap.put(BocInvt.BOCINVT_EVA_ADDRESS_REQ,
					(String) responseMap
							.get(BocInvt.BOCIEVA_ADDRESS_RES));
			inputMap.put(BocInvt.BOCINVT_EVA_PHONE_REQ,
					(String) responseMap
							.get(BocInvt.BOCIEVA_PHONE_RES));
			inputMap.put(BocInvt.BOCINVT_EVA_RISKMOBILE_REQ,
					(String) responseMap
							.get(BocInvt.BOCIEVA_MOBILE_RES));
			inputMap.put(BocInvt.BOCINVT_EVA_POSTCODE_REQ,
					(String) responseMap
							.get(BocInvt.BOCIEVA_POSTCODE_RES));
			inputMap.put(BocInvt.BOCINVT_EVA_RISKEMAIL_REQ,
					(String) responseMap
							.get(BocInvt.BOCIEVA_EMAIL_RES));
			inputMap.put(BocInvt.BOCINVT_EVA_REVENUE_REQ,
					(String) responseMap
							.get(BocInvt.BOCIEVA_REVENUE_RES));
			inputMap.put(
					BocInvt.BOCINVT_EVA_CUSTNATIONALITY_REQ,
					(String) responseMap
							.get(BocInvt.BOCIEVA_CUSTNATIONALITY_RES));
			inputMap.put(BocInvt.BOCINVT_EVA_EDUCATION_REQ,
					(String) responseMap
							.get(BocInvt.BOCIEVA_EDUCATION_RES));
			inputMap.put(
					BocInvt.BOCINVT_EVA_OCCUPATION_REQ,
					(String) responseMap
							.get(BocInvt.BOCIEVA_OCCUPATION_RES));
			inputMap.put(
					BocInvt.BOCINVT_EVA_HASINVESTEXPERIENCE_REQ,
					(String) responseMap
							.get(BocInvt.BOCIEVA_HASINVESTEXPERIENCE_RES));
			BaseDroidApp
					.getInstanse()
					.getBizDataMap()
					.put(ConstantGloble.BOCINVT_REQUESTMAP,
							inputMap);

			BaseDroidApp
					.getInstanse()
					.getBizDataMap()
					.put(ConstantGloble.BOICNVT_ISBEFORE_RESULT,
							responseMap);
			String status = (String) responseMap
					.get(BocInvt.BOCIEVA_STATUS_RES);
			if (!StringUtil.isNull(status)
					&& status
							.equals(ConstantGloble.BOCINVT_EVA_SUC_STATUS)) {
				isevaluatedBefore = true;
			} else {
				isevaluatedBefore = false;
			}
			isInvaildEvaluated = "0"
					.equals((String) responseMap
							.get("evalExpired"));
			OnLineRequestCallBack.SuccessCallBack(null);
			return;
		}
		
	}
}
