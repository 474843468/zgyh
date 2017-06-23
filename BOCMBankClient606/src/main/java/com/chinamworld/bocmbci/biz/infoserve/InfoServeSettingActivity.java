package com.chinamworld.bocmbci.biz.infoserve;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Push;
import com.chinamworld.bocmbci.biz.infoserve.push.PushInit;
import com.chinamworld.bocmbci.biz.push.PushManager;
import com.chinamworld.bocmbci.biz.push.PushSetting;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息服务设置
 * 
 * @author wanbing
 * 
 */
public class InfoServeSettingActivity extends InfoServeBaseActivity {

	/** 理财产品到期提醒签约标志 */
	protected static final int LICAI_SIGN_FLAG = 11;
	/** 理财产品到期提醒解约标志 */
	protected static final int LICAI_DELETE_FLAG = 12;

	private LinearLayout llytAgree;
	private LinearLayout llytSettingbuttons;
	private LinearLayout llytNewMessageAlarm;
	private LinearLayout llytDaedaozhangAlarm;
	private LinearLayout llytLicaidaoqiAlarm;
	private LinearLayout llytAssistFunction;
	private CheckBox checkboxAgree;
	private TextView txtNewMessageAlarm;
	private TextView txtDaedaozhangAlarm;
	private TextView txtLicaidaoqiAlarm;
	private TextView txtAgreeNotice;
	private ToggleButton toggle_btn_new_message_alarm;
	/** 理财产品到期查询 */
	private View toggle_btn_licaidaoqi_query;
	private Button toggle_btn_licaidaoqi_alarm;
	/** 理财产品是否签约 */
	private String licaiIsSigned;
	/** 理财产品提前天数 */
	private String licaiBeforeDate;
	/** 操作类型区别 */
	private int typeFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.infoserve_setting));
		//wuhan
		ib_setting_btn.setVisibility(View.GONE);
		
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				finish();
			}
		});
		btn_right.setVisibility(View.GONE);
		addView(R.layout.service_info_setting);
		initView();
	}

	private void initView() {

		llytAgree = (LinearLayout) findViewById(R.id.llyt_agree);
		llytSettingbuttons = (LinearLayout) findViewById(R.id.llyt_settingbuttons);
		llytNewMessageAlarm = (LinearLayout) findViewById(R.id.llyt_new_message_alarm);
		llytDaedaozhangAlarm = (LinearLayout) findViewById(R.id.llyt_daedaozhang_alarm);
		llytLicaidaoqiAlarm = (LinearLayout) findViewById(R.id.llyt_licaidaoqi_alarm);
		llytAssistFunction = (LinearLayout) findViewById(R.id.llyt_assist_function);
		checkboxAgree = (CheckBox) findViewById(R.id.checkbox_agree);
		txtNewMessageAlarm = (TextView) findViewById(R.id.txt_new_message_alarm);
		txtDaedaozhangAlarm = (TextView) findViewById(R.id.txt_daedaozhang_alarm);
		txtLicaidaoqiAlarm = (TextView) findViewById(R.id.txt_licaidaoqi_alarm);
		txtAgreeNotice = (TextView) findViewById(R.id.txt_agree_notice);
		toggle_btn_new_message_alarm = (ToggleButton) findViewById(R.id.toggle_btn_new_message_alarm);
		toggle_btn_licaidaoqi_alarm = (Button) findViewById(R.id.toggle_btn_licaidaoqi_alarm);
		toggle_btn_licaidaoqi_query = findViewById(R.id.toggle_btn_licaidaoqi_query);

		toggle_btn_licaidaoqi_query.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 理财查询
				requestPsnXpadDueDateRemindQuery();
			}
		});
		checkboxAgree.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				isShowSettingButtons(isChecked);
			}
		});
		llytAgree.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 消息服务协议
				Intent intent = new Intent();
				intent.setClass(InfoServeSettingActivity.this, InfoServeAgreeActivity.class);
				startActivityForResult(intent, 1);
				overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
			}
		});
		// 是否开启最新消息提醒
		showNewMessageAlarm();
		// 开启/关闭最新消息提醒
		// llytNewMessageAlarm.setOnClickListener(newMessageAlarmClick);
		toggle_btn_new_message_alarm.setOnClickListener(newMessageAlarmClick);
		// // 理财产品签约情况查询
		// requestPsnXpadDueDateRemindQuery();

		// llytLicaidaoqiAlarm.setOnClickListener(licaidaoqiAlarmClick);
		toggle_btn_licaidaoqi_alarm.setOnClickListener(licaidaoqiAlarmClick);
		// 大额到账提醒
		llytDaedaozhangAlarm.setOnClickListener(daedaozhangAlarmClick);
		// 辅助功能点击
		llytAssistFunction.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 辅助功能画面
				Intent intent = new Intent();
				intent.setClass(InfoServeSettingActivity.this, InfoServeAssistFunctionActivity.class);
				startActivity(intent);

			}
		});
	}

	/**
	 * 开启/关闭最新消息提醒
	 */
	private View.OnClickListener newMessageAlarmClick = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (PushSetting.getPushState()) {
				// 关闭
				// String message = getResources().getString(
				// R.string.infoserve_new_message_alarm_off_notice);
				// BaseDroidApp.getInstanse().showErrorDialog(message,
				// R.string.cancle, R.string.confirm,
				// new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// BaseDroidApp.getInstanse().dismissErrorDialog();
				// switch ((Integer) v.getTag()) {
				// case CustomDialog.TAG_SURE:// 确定
				// PushSetting.setPushState(false);
				// showNewMessageAlarm();
				// break;
				//
				// }
				//
				// }
				// });
				PushSetting.setPushState(false);
				PushManager.getInstance(InfoServeSettingActivity.this).closePushService();
				showNewMessageAlarm();
			} else {
				// 开启
				// String message = getResources().getString(
				// R.string.infoserve_new_message_alarm_on_notice);
				// BaseDroidApp.getInstanse().showErrorDialog(message,
				// R.string.cancle, R.string.confirm,
				// new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// BaseDroidApp.getInstanse().dismissErrorDialog();
				// switch ((Integer) v.getTag()) {
				// case CustomDialog.TAG_SURE:// 确定
				// PushSetting.setPushState(true);
				// showNewMessageAlarm();
				// break;
				//
				// }
				//
				// }
				// });
				PushSetting.setPushState(true);
				PushManager.getInstance(InfoServeSettingActivity.this).startPushService();
				showNewMessageAlarm();

			}
			PushInit.changePushManagerStatus(InfoServeSettingActivity.this);
		}
	};

	/**
	 * 理财取消按钮
	 */
	private View.OnClickListener licaiCanClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			toggle_btn_licaidaoqi_alarm.setVisibility(View.GONE);
			toggle_btn_licaidaoqi_query.setVisibility(View.VISIBLE);
		}
	};

	/**
	 * 理财产品到期提醒点击
	 */
	private View.OnClickListener licaidaoqiAlarmClick = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (Push.SIGNED.equals(licaiIsSigned)) {
				// 已签约，解约/修改
				licaidaoqiDeleteOrModify();
			} else if (Push.UNSIGNED.equals(licaiIsSigned)) {
				// 未签约，签约
				licaidaoqiSign();
			} else {
				// 未签约，签约
				licaidaoqiSign();
			}

		}
	};

	/**
	 * 大额到账提醒点击
	 */
	private View.OnClickListener daedaozhangAlarmClick = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// 跳转到大额到账提醒画面
			requestForCardList();
		}
	};

	/**
	 * 是否显示设置按钮
	 * 
	 * @param isShow
	 */
	private void isShowSettingButtons(boolean isShow) {
		if (isShow) {
			txtAgreeNotice.setVisibility(View.GONE);
			llytSettingbuttons.setVisibility(View.VISIBLE);
		} else {
			txtAgreeNotice.setVisibility(View.VISIBLE);
			llytSettingbuttons.setVisibility(View.GONE);
		}
	}

	/**
	 * 最新消息提醒开启/未开启显示
	 */
	private void showNewMessageAlarm() {
		if (PushSetting.getPushState()) {
			txtNewMessageAlarm.setText(R.string.infoserve_new_message_alarm_on);
			toggle_btn_new_message_alarm.setChecked(true);
		} else {
			txtNewMessageAlarm.setText(R.string.infoserve_new_message_alarm_off);
			toggle_btn_new_message_alarm.setChecked(false);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == 100) {
			checkboxAgree.setChecked(true);
		} else if (requestCode == 1 && resultCode == 101) {
			checkboxAgree.setChecked(false);
		}

	}

	/**
	 * 理财产品到期提醒签约
	 */
	private void licaidaoqiSign() {
		// // 弹出Dialog
		// String message = getResources().getString(
		// R.string.infoserve_licaidaiqi_sign_notice);
		// BaseDroidApp.getInstanse().showErrorDialog(message, R.string.cancle,
		// R.string.confirm, new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// BaseDroidApp.getInstanse().dismissErrorDialog();
		// switch ((Integer) v.getTag()) {
		// case CustomDialog.TAG_SURE:// 确定
		// typeFlag = LICAI_SIGN_FLAG;
		// // 请求token
		// requestCommConversationId();
		// BaseHttpEngine.showProgressDialog();
		// break;
		//
		// }
		//
		// }
		// });
		typeFlag = LICAI_SIGN_FLAG;
		// 请求token
		BaseHttpEngine.showProgressDialog(licaiCanClick);
		requestCommConversationId();
		
	}

	/**
	 * 理财产品到期提醒解约/修改
	 */
	private void licaidaoqiDeleteOrModify() {
		// // 弹出Dialog
		// String message = getResources().getString(
		// R.string.infoserve_licaidaiqi_delete_notice);
		// BaseDroidApp.getInstanse().showErrorDialog(message, R.string.cancle,
		// R.string.confirm, new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// BaseDroidApp.getInstanse().dismissErrorDialog();
		// switch ((Integer) v.getTag()) {
		// case CustomDialog.TAG_SURE:// 确定
		// typeFlag = LICAI_DELETE_FLAG;
		// // 请求token
		// requestCommConversationId();
		// BaseHttpEngine.showProgressDialog();
		// break;
		//
		// }
		//
		// }
		// });
		typeFlag = LICAI_DELETE_FLAG;
		// 请求token
		BaseHttpEngine.showProgressDialog(licaiCanClick);
		requestCommConversationId();
		

	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		switch (typeFlag) {
		case LICAI_SIGN_FLAG:
			requestPsnXpadDueDateRemindSign(licaiCanClick,token);
			break;
		case LICAI_DELETE_FLAG:
			requestPsnXpadDueDateRemindDelete(licaiCanClick,token);
			break;
		}

	}

	/**
	 * 理财产品查询结果回调
	 */
	@Override
	public void requestPsnXpadDueDateRemindQueryCallback(Object resultObj) {
		super.requestPsnXpadDueDateRemindQueryCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> map = (Map<String, Object>) biiResponseBody.getResult();

		// 判断理财产品提醒是否开启
		if (!StringUtil.isNullOrEmpty(map)) {
			licaiIsSigned = (String) map.get(Push.IS_SIGNED);
			licaiBeforeDate = (String) map.get(Push.BEFORE_DATE);
			if (!StringUtil.isNullOrEmpty(licaiIsSigned)) {
				if (Push.SIGNED.equals(licaiIsSigned)) {
					txtLicaidaoqiAlarm.setText(R.string.infoserve_licaidaoqi_sign);
					// toggle_btn_licaidaoqi_alarm.setChecked(true);
					toggle_btn_licaidaoqi_alarm.setBackgroundResource(R.drawable.img_switch_on);
				} else if (Push.UNSIGNED.equals(licaiIsSigned)) {
					txtLicaidaoqiAlarm.setText(R.string.infoserve_licaidaoqi_not_sign);
					// toggle_btn_licaidaoqi_alarm.setChecked(false);
					toggle_btn_licaidaoqi_alarm.setBackgroundResource(R.drawable.img_switch_off);
				} else {
					txtLicaidaoqiAlarm.setText(R.string.infoserve_licaidaoqi_not_sign);
					// toggle_btn_licaidaoqi_alarm.setChecked(false);
					toggle_btn_licaidaoqi_alarm.setBackgroundResource(R.drawable.img_switch_off);
				}
			} else {
				txtLicaidaoqiAlarm.setText(R.string.infoserve_licaidaoqi_not_sign);
				// toggle_btn_licaidaoqi_alarm.setChecked(false);
				toggle_btn_licaidaoqi_alarm.setBackgroundResource(R.drawable.img_switch_off);
			}

			toggle_btn_licaidaoqi_alarm.setVisibility(View.VISIBLE);
			toggle_btn_licaidaoqi_query.setVisibility(View.GONE);
		} else {
			txtLicaidaoqiAlarm.setText(R.string.infoserve_licaidaoqi_not_sign);
			// toggle_btn_licaidaoqi_alarm.setChecked(false);
			toggle_btn_licaidaoqi_alarm.setBackgroundResource(R.drawable.img_switch_off);
		}
	}

	/**
	 * @Title: requestPsnXpadDueDateRemindSign
	 * @Description:理财产品到期提醒签约
	 * @param token
	 * @return void
	 */
	private void requestPsnXpadDueDateRemindSign(View.OnClickListener listener,String token) {
		BaseHttpEngine.showProgressDialog(listener);
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Push.PSN_XPAD_DUE_DATE_REMIND_SIGN);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Push.BEFORE_DATE, null);// 提前天数
		paramsmap.put(Comm.TOKEN_REQ, token);
		paramsmap.put("conversationId",
				(String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnXpadDueDateRemindSignCallback");
	}

	/**
	 * @Title: requestPsnXpadDueDateRemindSignCallback
	 * @Description: 理财产品到期提醒签约的回调
	 * @param @param resultObj
	 * @return void
	 */
	public void requestPsnXpadDueDateRemindSignCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		licaiIsSigned = Push.SIGNED;
		txtLicaidaoqiAlarm.setText(R.string.infoserve_licaidaoqi_sign);
		// toggle_btn_licaidaoqi_alarm.setChecked(true);
		toggle_btn_licaidaoqi_alarm.setBackgroundResource(R.drawable.img_switch_on);
	}

	/**
	 * @Title: requestPsnXpadDueDateRemindDelete
	 * @Description:理财产品到期提醒解约
	 * @param token
	 * @return void
	 */
	private void requestPsnXpadDueDateRemindDelete(View.OnClickListener listener,String token) {
		BaseHttpEngine.showProgressDialog(listener);
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Push.PSN_XPAD_DUE_DATE_REMIND_DELETE);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Comm.TOKEN_REQ, token);
		paramsmap.put("conversationId",
				(String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnXpadDueDateRemindDeleteCallback");
	}

	/**
	 * @Title: requestPsnXpadDueDateRemindDeleteCallback
	 * @Description: 理财产品到期提醒解约的回调
	 * @param @param resultObj
	 * @return void
	 */
	public void requestPsnXpadDueDateRemindDeleteCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		licaiIsSigned = Push.UNSIGNED;
		txtLicaidaoqiAlarm.setText(R.string.infoserve_licaidaoqi_not_sign);
		// toggle_btn_licaidaoqi_alarm.setChecked(false);
		toggle_btn_licaidaoqi_alarm.setBackgroundResource(R.drawable.img_switch_off);
	}

	@Override
	public void requestForCardListCallBack(Object resultObj) {
		super.requestForCardListCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(InfoServeDataCenter.getInstance().getAccountList())) {
			BaseDroidApp.getInstanse().showMessageDialog(
					this.getResources().getString(R.string.infoserve_daedaozhang_no_account), new OnClickListener() {

						@Override
						public void onClick(View v) {
							BaseDroidApp.getInstanse().dismissErrorDialog();
						}
					});
		} else {
			// 跳转到大额到账提醒列表选择画面
			Intent intent = new Intent(this, NonFixedProductRemindAccountListActivity.class);
			startActivity(intent);
		}
	}

}
