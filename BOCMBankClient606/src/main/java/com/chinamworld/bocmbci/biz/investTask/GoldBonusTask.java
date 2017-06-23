package com.chinamworld.bocmbci.biz.investTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.GoldBonus;
import com.chinamworld.bocmbci.biz.goldbonus.GoldbonusLocalData;
import com.chinamworld.bocmbci.biz.goldbonus.accountmanager.AccountManagerMainActivity;
import com.chinamworld.bocmbci.biz.goldbonus.accountmanager.GoldAcountChoseActivity;
import com.chinamworld.bocmbci.biz.goldbonus.accountmanager.GoldbounsReminderActivity;
import com.chinamworld.bocmbci.biz.invest.activity.InvesAgreeFirstActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.IHttpResponseCallBack;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 贵金属积利金 任务   1、开通投资理财，2签约关联账户
 * @author linyl
 *
 */
public class GoldBonusTask extends InvestBaseTask {

	private GoldBonusTask(BaseActivity context) {
		super(context);
	}
	
	public static GoldBonusTask getInstance(BaseActivity context){
		return new GoldBonusTask(context);
	}
	
	private boolean isOpen = false;
	private boolean isGoldBonusBinding = false;
	private boolean isGoldBonusBindinglink = false;
	private TextView is_requset;
	private View bindLayout;
	/** 长城借记卡 */
	protected static final String GREATWALL_CREDIT = "119";
	/** 活一本 */
	protected static final String HUOQIBENTONG = "188";

	@Override
	protected View getTaskView() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.goldbonustask_main, null);
		View openLayout = view
				.findViewById(R.id.bocinvt_money_button_show);
		bindLayout = view
				.findViewById(R.id.bocinvt_money_text_hide);
		TextView isopentv = (TextView) view.findViewById(R.id.isopen_text);
		ImageView openima = (ImageView) view.findViewById(R.id.isopen_ima);
		ImageView bindima = (ImageView) view.findViewById(R.id.isbind_ima);
		is_requset=(TextView) view.findViewById(R.id.is_requset);
		openLayout.setOnClickListener(manageOpenClick);
		bindLayout.setOnClickListener(manageBindClick);
		View close = view.findViewById(R.id.top_right_close);
		close.setOnClickListener(exitClick);
		if (isOpen == false&& isGoldBonusBinding == false) {
//			openLayout.setVisibility(View.VISIBLE);
//			bindLayout.setVisibility(View.VISIBLE);
			isopentv.setText(R.string.boc_noopen);
			openima.setVisibility(View.VISIBLE);
			openLayout.setClickable(true);
			is_requset.setText(R.string.boc_nobinding);
			bindima.setVisibility(View.VISIBLE);
			bindLayout.setClickable(true);
			is_requset.setVisibility(View.INVISIBLE);
			bindLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					BaseDroidApp
					.getInstanse()
					.showInfoMessageDialog(context.getString(R.string.goldbouns_first_opne));
				}
			});
		} else if(isOpen == true && isGoldBonusBinding == false ){
			isopentv.setText(R.string.boc_open);
			openima.setVisibility(View.INVISIBLE);
			openLayout.setClickable(false);
			is_requset.setText(R.string.boc_nobinding);
			bindima.setVisibility(View.VISIBLE);
			bindLayout.setClickable(true);
//			bindLayout.setVisibility(View.VISIBLE);
		}else if(isOpen == false && isGoldBonusBinding == true ){
			isopentv.setText(R.string.boc_noopen);
			openima.setVisibility(View.VISIBLE);
			openLayout.setClickable(true);
			is_requset.setText(R.string.boc_binding);
			bindima.setVisibility(View.INVISIBLE);
			bindLayout.setClickable(false);
			is_requset.setVisibility(View.INVISIBLE);
			bindLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					BaseDroidApp
					.getInstanse()
					.showInfoMessageDialog(context.getString(R.string.goldbouns_first_opne));
				}
			});
		}else if(isOpen==true&&isGoldBonusBindinglink==true){
			ActivityIntentTools.intentToActivity(context, GoldbounsReminderActivity.class);
		}
		return view;
	}
	
	protected OnClickListener exitClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
			context.finish();
		}
	};
	
	/** 开通投资理财监听事件 */
	protected OnClickListener manageOpenClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 跳转到投资理财服务协议页面
			ActivityIntentTools.intentToActivityForResult(context, InvesAgreeFirstActivity.class, ConstantGloble.ACTIVITY_RESULT_CODE,null);
			context.overridePendingTransition(R.anim.push_up_in,
					R.anim.no_animation);
		}
	};

	/** 登记账户监听事件 */
	OnClickListener manageBindClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (isOpen == false) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("请先开通投资理财服务");
				return;
			}
			//在这判断是否有账户，有的话继续，没有的话报提示信息。再次调用查询所有账户的接口
			Map<String, Object> paramMap = new HashMap<String, Object>();
			httpTools.requestHttp(GoldBonus.PSNCOMMONQUERYFILTEREDACCOUNTS,
					"requestPsnCommonQueryAllChinaBankAccountCallBack", paramMap,
					false);
		
		}
	};

	
	@Override
	protected boolean getDoTaskStatus() {
		return isOpen && isGoldBonusBinding;
	}

	@Override
	protected void getTaskStatusOnLine(IAction actionCallBack) {
		OnLineRequestCallBack = actionCallBack;
		httpTools.requestHttp(Comm.PSNINVESTMENTMANAGEISOPEN_API,
				"requestPsnInvestmentisOpenBeforeCallback", null, false);
		BaseHttpEngine.showProgressDialogCanGoBack();
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
		BiiHttpEngine.dissMissProgressDialog();
		isOpen = Boolean.valueOf(isOpenresult);
		if(isOpen){
			requestPsnGoldBonusSignInfoQuery();
		}else {
			OnLineRequestCallBack.SuccessCallBack(null);
			
		}
		
	}
	

	private void requestPsnGoldBonusSignInfoQuery() {
		httpTools.requestHttp("PsnGoldBonusSignInfoQuery", null,
				new IHttpResponseCallBack<Map<String, Object>>() {

					@Override
					public void httpResponseSuccess(Map<String, Object> result,String method) {
						GoldbonusLocalData.getInstance().psnGoldBonusSignInfoQuery = result;
						GoldbonusLocalData.getInstance().accountNumber=(String) result.get(GoldBonus.ACCOUNTNUM);
						/**签约标示关联判断**/
						String linkAccFlag = (String) result
								.get(GoldBonus.LINKACCTFLAG);
						if("1".equals(linkAccFlag)){
						isGoldBonusBinding = true;
						}
						else if("0".equals(linkAccFlag)){
//							ActivityIntentTools.intentToActivity(context, GoldAcountChoseActivity.class);
						}else if("2".equals(linkAccFlag)){
							isGoldBonusBindinglink=true;
							if(isOpen){
								ActivityIntentTools.intentToActivity(context, GoldbounsReminderActivity.class);
								context.finish();
							}
						}
						BiiHttpEngine.dissMissProgressDialog();
						if(!(isOpen&&isGoldBonusBindinglink)){
							OnLineRequestCallBack.SuccessCallBack(null);
						}
						
						
					}
				});
		BaseHttpEngine.showProgressDialogCanGoBack();
		
	}
	
	@Override
	protected boolean onActivityForResult(int requestCode, int resultCode,
			Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
				// 开通成功的响应
				isOpen = true;
			}else if(requestCode == 100){
				isGoldBonusBinding = true;
			}
			return true;
		}
		return false;
	}
	/** 获取借记卡列表----回调 */
	public void requestPsnCommonQueryAllChinaBankAccountCallBack(
			Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> result = (List<Map<String, Object>>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			//没有可变更账户弹出错误信息
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog("您没有可进行贵金属积利交易的借记卡或活期一本通账户。请您携带未关联账户、有效身份证件及已关联电子银行的任一账户，前往我行营业网点进行关联");
			return;
		}
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		if (result != null && result.size() > 0) {
			for (int i = 0; i < result.size(); i++) {
				if (GREATWALL_CREDIT.equals(result.get(i).get(
						Crcd.CRCD_ACCOUNTTYPE_RES))
						|| HUOQIBENTONG.equals(result.get(i).get(
								Crcd.CRCD_ACCOUNTTYPE_RES))) {
					resultList.add(result.get(i));
				}
			}
		}
		if (resultList != null && resultList.size() > 0) {
			GoldbonusLocalData.getInstance().requestPsnCommonQueryAllChinaBankAccountList = resultList;
			// 通讯开始,展示通讯框
			BiiHttpEngine.showProgressDialog();
			httpTools.requestHttp(GoldBonus.PSNGOLDBONUSSIGNINFOQUERY, null,
					new IHttpResponseCallBack<Map<String, Object>>() {
						@Override
						public void httpResponseSuccess(
								Map<String, Object> result,String method) {
							// 通讯结束,隐藏通讯框
							BiiHttpEngine.dissMissProgressDialog();
							Map<String, Object> responseList = result;
							String statueString = (String) responseList.get(GoldBonus.LINKACCTFLAG);
							if (statueString.equals("0")) {
								// 状态为“0”未签约 跳转到账户选择界面
								Map<String,Object> data = new HashMap<String, Object>();
								data.put("isFirst", "2");
								ActivityIntentTools.intentToActivityForResult(
										context, GoldAcountChoseActivity.class,
										100, data);
								context.overridePendingTransition(
										R.anim.push_up_in, R.anim.no_animation);

							} else if (statueString.equals("2")) {
								// 状态为“2”已签约未关联 跳转到统一界面
								Map<String,Object> data = new HashMap<String, Object>();
								ActivityIntentTools.intentToActivityForResult(
										context, GoldbounsReminderActivity.class,
										101, null);
								context.overridePendingTransition(
										R.anim.push_up_in, R.anim.no_animation);

							} else {
								//状态为1时，调到账户选择界面，并显示已关联
								is_requset.setText("已关联");
								ActivityIntentTools.intentToActivityForResult(
										context, AccountManagerMainActivity.class,
										102, null);
								context.overridePendingTransition(
										R.anim.push_up_in, R.anim.no_animation);
							}
							

						}
					});
		} 
//		else {
//			//没有可变更账户弹出错误信息
//			BaseHttpEngine.dissMissProgressDialog();
//			BaseDroidApp.getInstanse().showInfoMessageDialog("您没有可进行贵金属积利交易的借记卡或活期一本通账户。请您携带未关联账户、有效身份证件及已关联电子银行的任一账户，前往我行营业网点进行关联");
//			return;
//
//		}

	}

	@Override
	public void commonHttpErrorCallBack(String s) {

	}
}
