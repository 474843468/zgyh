package com.chinamworld.bocmbci.biz.bocinvt.dialogActivity;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.RelativeLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt.inflateView.InflaterViewDialog;
import com.chinamworld.bocmbci.constant.LayoutValue;

/** 登记账户弹出框 */
public class BocBindinglDialogActivity extends BaseActivity {
	/** 账户列表信息页 */
	private RelativeLayout view;
	private RelativeLayout rl_bank;
	/** 登记账户余额 */
	private List<Map<String, Object>> balanceList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BaseDroidApp.getInstanse().setDialogAct(true);
		// 添加布局
		setContentView(R.layout.acc_for_dialog);
		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		balanceList=BociDataCenter.getInstance().getBalanceList();
		rl_bank = (RelativeLayout) findViewById(R.id.rl_bank);
		android.widget.RelativeLayout.LayoutParams lp = new android.widget.RelativeLayout.LayoutParams(
				LayoutValue.SCREEN_WIDTH, LayoutValue.SCREEN_HEIGHT * 7 / 8);
		rl_bank.setLayoutParams(lp);
		int position = getIntent().getIntExtra("p", 0);
		InflaterViewDialog inflaterdialog = new InflaterViewDialog(this);
		view = (RelativeLayout) inflaterdialog.newBindingViewDialog(
				BociDataCenter.getInstance().getBocinvtAcctList().get(position), balanceList, invtBindingClick,
			exitDialogClick);
		rl_bank.removeAllViews();
		rl_bank.addView(view);
	}

	/** 登记账户监听事件 */
	OnClickListener invtBindingClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 请求登记资金账户信息
//			requestXpadReset();
		}
	};

	/**
	 * 请求登记资金账户列表信息
	 */
//	public void requestXpadReset() {
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(BocInvt.PSNXPADRESET_API);
//		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
//				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
//		// 通讯开始,展示通讯框
//		BiiHttpEngine.showProgressDialog();
//		HttpManager
//				.requestBii(biiRequestBody, this, "requestXpadResetCallback");
//	}

	/**
	 * 请求登记资金账户列表信息---回调
	 * 
	 * @param resultObj
	 */
//	public void requestXpadResetCallback(Object resultObj) {
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		// 得到response
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		// 通讯结束,隐藏通讯框
//		BiiHttpEngine.dissMissProgressDialog();
//
//		if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
//			// 没有可登记账户
//			BaseDroidApp.getInstanse().showInfoMessageDialog(
//					BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.bocinvt_binding_relevance));
//		} else {
//			List<Map<String, Object>> responseList = (List<Map<String, Object>>) biiResponseBody
//					.getResult();
//			// 有可登记账户
//			BaseDroidApp.getInstanse().getBizDataMap()
//					.put(ConstantGloble.BOCINVT_XPADRESET_LIST, responseList);
//
//			Intent intent = new Intent(this, InvtBindingChooseActivity.class);
//			startActivityForResult(intent,
//					ConstantGloble.ACTIVITY_REQUEST_INVTBINDING_CODE);
//			overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
//		}
//
//	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		BaseDroidApp.getInstanse().setCurrentAct(this);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;
		case RESULT_CANCELED:
			break;
		}
	}
	/** 退出监听事件 */
	protected OnClickListener exitDialogClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			setResult(RESULT_FIRST_USER);
			finish();
		}
	};

	@Override
	public void finish() {
		super.finish();
		BaseDroidApp.getInstanse().setDialogAct(false);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		BaseDroidApp.getInstanse().setDialogAct(true);
//	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		return ActivityTaskType.OneTask;
	}
}
