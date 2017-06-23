package com.chinamworld.bocmbci.biz.crcd.dialogActivity;

import java.util.HashMap;
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
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice.MyVirtualBCListActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice.MyVirtualGuanLianConfirmActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice.MyVirtualSetupActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice.VirtualBCListActivity;
import com.chinamworld.bocmbci.biz.crcd.view.InflaterViewDialog;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 我的虚拟卡------虚拟卡详情
 * 
 * @author huangyuchao
 * 
 */
public class CrcdVirtualDialogActivity extends BaseActivity {
	private static final String TAG = "CrcdVirtualDialogActivity";
	// 详情视图
	private RelativeLayout view;
	RelativeLayout rl_bank;

	Map<String, Object> virCardItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setContentView(R.layout.crcd_for_dialog);
		initData();
		init();
	}

	public void initData() {
		virCardItem = VirtualBCListActivity.virCardItem;
	}

	public void init() {

		BaseDroidApp.getInstanse().setDialogAct(true);

		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		rl_bank = (RelativeLayout) findViewById(R.id.rl_bank);
		android.widget.RelativeLayout.LayoutParams lp = new android.widget.RelativeLayout.LayoutParams(
				LayoutValue.SCREEN_WIDTH, LayoutValue.SCREEN_HEIGHT * 7 / 8);
		rl_bank.setLayoutParams(lp);

		InflaterViewDialog dialog = new InflaterViewDialog(this);

		view = (RelativeLayout) dialog.initVirtualBCListDialogView(virCardItem, exitVirtualDetailClick, gotoSutepClick,
				gotoSmsClick, popListener);

		rl_bank.removeAllViews();
		rl_bank.addView(view);
	}

	/** 虚拟卡设定 */
	OnClickListener gotoSutepClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();

			Intent it = new Intent(CrcdVirtualDialogActivity.this, MyVirtualSetupActivity.class);
			String single = String.valueOf(virCardItem.get(Crcd.CRCD_SINGLEEMT));
			String total = String.valueOf(virCardItem.get(Crcd.CRCD_TOTALEAMT));
			it.putExtra(Crcd.CRCD_SINGLEEMT, single);
			it.putExtra(Crcd.CRCD_TOTALEAMT, total);
			startActivity(it);
			finish();
		}
	};
	// 关闭
	OnClickListener exitVirtualDetailClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setResult(RESULT_OK);
			finish();
		}
	};

	OnClickListener popListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Integer tag = (Integer) v.getTag();
			switch (tag) {
			case 0:
				// 展示短信的dialog
				showSmsDialog();
				break;
			case 1:
				// 信用卡关联网银
				Intent it = new Intent(CrcdVirtualDialogActivity.this, MyVirtualGuanLianConfirmActivity.class);
				startActivity(it);
				finish();
				break;
			}

		}
	};
	/** 短信通知 */
	OnClickListener gotoSmsClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			showSmsDialog();
		}
	};

	public void showSmsDialog() {
		Map<String, Object> returnMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);

		String mobile = String.valueOf(returnMap.get(Crcd.CRCD_MOBILE));

		// 弹出对话框
		BaseDroidApp.getInstanse().createSmsDialog(
				getString(R.string.mycrcd_xunixinxi_mianfei_send_phone)+ mobile, new OnClickListener() {

					@Override
					public void onClick(View v) {
						switch (Integer.parseInt(v.getTag() + "")) {
						case CustomDialog.TAG_SURE:
							// 发送短信
							psnCrcdVirtualCardSendMessage();
							break;
						case CustomDialog.TAG_CANCLE:
							// 取消
							BaseDroidApp.getInstanse().dismissErrorDialog();
						}
					}
				});

	}

	public void psnCrcdVirtualCardSendMessage() {
		BiiHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDVIRTUALCARDSENDMESSAGE);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, MyVirtualBCListActivity.accountId);//
		map.put(Crcd.CRCD_VIRCARDNO, String.valueOf(virCardItem.get(Crcd.CRCD_VIRTUALCARDNO)));
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdVirtualCardSendMessageCallBack");
	}

	public void psnCrcdVirtualCardSendMessageCallBack(Object resultObj) {
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String result = (String) biiResponseBody.getResult();
		// BaseDroidApp.getInstanse().dismissMessageDialog();
		BaseDroidApp.getInstanse().dismissErrorDialog();
		CustomDialog.toastShow(this, this.getString(R.string.mycrcd_sms_has_send_success));
	}

//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		BaseDroidApp.getInstanse().setDialogAct(true);
//	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}

}
