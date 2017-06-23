package com.chinamworld.bocmbci.biz.investTask;

import java.util.List;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.invest.activity.InvesAgreeFirstActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 开通投资理财任务。 仅有投资理财任务需要开通
 * 
 * @author yuht time:2015年11月4日12:47:18
 * 
 */
public class BocInvestTask extends InvestBaseTask {
	@Override
	public void commonHttpErrorCallBack(String s) {

	}

	private BocInvestTask(BaseActivity context) {
		super(context);
	}

	public static BocInvestTask getInstance(BaseActivity context) {
		return new BocInvestTask(context);
	}

	private boolean isOpen = false;

	@Override
	protected View getTaskView() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.bocinvet_openmanangertask_layout, null);
		View openLayout = view
				.findViewById(R.id.bocinvt_money_button_show);
		openLayout.setOnClickListener(manageOpenClick);
		View layout = view
				.findViewById(R.id.bocinvt_money_text_hide);
		View close = view.findViewById(R.id.top_right_close);
		close.setOnClickListener(exitClick);
		if (isOpen == false) {
			openLayout.setVisibility(View.VISIBLE);
			layout.setVisibility(View.GONE);
		} else {
			openLayout.setVisibility(View.GONE);
			layout.setVisibility(View.VISIBLE);
		}
		return view;
	}
	
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

	protected OnClickListener exitClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
			context.finish();
		}
	};
	

	@Override
	protected boolean getDoTaskStatus() {
		return isOpen;
	}

	@Override
	protected void getTaskStatusOnLine(IAction actionCallBack) {
		BiiHttpEngine.showProgressDialogCanGoBack();
		OnLineRequestCallBack = actionCallBack;
		httpTools.requestHttp(Comm.PSNINVESTMENTMANAGEISOPEN_API,
				"requestPsnInvestmentisOpenBeforeCallback", null, false);
	}

	/**
	 * 判断是否开通投资理财服务---回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnInvestmentisOpenBeforeCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String isOpenresult = String.valueOf(biiResponseBody.getResult());
		if (StringUtil.isNull(isOpenresult)) {
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}
		isOpen = Boolean.valueOf(isOpenresult);
		BiiHttpEngine.dissMissProgressDialog();
		OnLineRequestCallBack.SuccessCallBack(null);
	}
	
	@Override
	protected boolean onActivityForResult(int requestCode, int resultCode,
			Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
				// 开通成功的响应
				isOpen = true;
				return true;
			}
			break;
		}
		return false;
	}
}
