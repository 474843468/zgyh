package com.chinamworld.bocmbci.biz.setting;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.audio.AudioKeyInfoActivity;
import com.chinamworld.bocmbci.biz.audio.AudioKeyPwdActivity;
import com.chinamworld.bocmbci.biz.epay.EPayMainActivity;
import com.chinamworld.bocmbci.biz.epay.transquery.TransQueryActivity;
import com.chinamworld.bocmbci.biz.invest.activity.InvesHasOpenActivity;
import com.chinamworld.bocmbci.biz.setting.accmanager.AccountManagerActivity;
import com.chinamworld.bocmbci.biz.setting.control.SettingControl;
import com.chinamworld.bocmbci.biz.setting.exittime.ExitTimeSettingActivity;
import com.chinamworld.bocmbci.biz.setting.hardwarebinding.HardwareBindingActivity;
import com.chinamworld.bocmbci.biz.setting.limit.LimitSettingActivity;
import com.chinamworld.bocmbci.biz.setting.obligate.EditObligateMessageActivity;
import com.chinamworld.bocmbci.biz.setting.pass.EditLoginPassActivity;
import com.chinamworld.bocmbci.biz.setting.safetytools.SafetyToolsActivity;
import com.chinamworld.bocmbci.biz.setting.setacct.SettingAccountActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

public class SettingBaseActivity extends BaseActivity implements
		OnClickListener {
	private static final String TAG = "SettingBaseActivity";
	protected ActivityTaskManager activityTaskManager = ActivityTaskManager
			.getInstance();
	
	/**
	 * 返回按钮
	 */
	protected Button back;
	/**
	 * 右边按钮
	 */
	protected Button right;
	/**
	 * 主页面布局
	 */
	protected LinearLayout tabcontent;
	/**
	 * 获取文件
	 */
	protected LayoutInflater mainInflater;

	protected SettingControl settingControl = SettingControl.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		baseinit();

	}

	@Override
	public void onBackPressed() {
		finish();
	}

	// /**
	// * 获取tocken
	// */
	protected void requestPSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPSNGetTokenIdCallback");
	}

	/**
	 * 获取tokenId----回调
	 * 
	 * @param resultObj
	 *            :返回结果
	 */
	public void requestPSNGetTokenIdCallback(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
	}

	// /**
	// * 获取安全因子
	// *@Author xyl
	// *@param serviceId 服务码
	// */
	// protected void requestSecurityFactor(String serviceId){
	// BiiRequestBody biiRequestBody = new BiiRequestBody();
	// biiRequestBody.setMethod(Setting.SET_GetSecurityFactor);
	// biiRequestBody.setConversationId((String)BaseDroidApp
	// .getInstanse()
	// .getBizDataMap()
	// .get(ConstantGloble.CONVERSATION_ID));
	// Map<String, String> map = new HashMap<String, String>();
	// map.put(Setting.SET_GETSECURITYFACTOR_SERVICEID, serviceId);
	// biiRequestBody.setParams(map);
	// HttpManager.requestBii(biiRequestBody, this,
	// "requestSecurityFactorCallback");
	// }
	// /**
	// * 获取安全因子 返回结果处理
	// *@Author xyl
	// *@param resultObj 返回结果
	// */
	// public void requestSecurityFactorCallback(Object resultObj){
	// BiiResponse biiResponse = (BiiResponse) resultObj;
	// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
	// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
	// }

	/**
	 * 请求发送手机验证码到手机
	 * 
	 * @Author xyl
	 */
	protected void sendMSCToMobile() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.SET_SENDMSC);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "sendMSCToMobileCallback");
	}

	protected void sendMSCToMobile(String conversaitonId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.SET_SENDMSC);
		biiRequestBody.setConversationId(conversaitonId);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "sendMSCToMobileCallback");
	}

	/**
	 * 请求发送手机验证码到手机 返回结果处理
	 * 
	 * @Author xyl
	 * @param resultObj
	 *            返回结果
	 */
	public void sendMSCToMobileCallback(Object resultObj) {

	}

	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.AQUIRE_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		// 获取 随机数
		HttpManager.requestBii(biiRequestBody, this,
				"queryRandomNumberCallBack");
	}

	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber(String conversationId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.AQUIRE_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId(conversationId);
		// 获取 随机数
		HttpManager.requestBii(biiRequestBody, this,
				"queryRandomNumberCallBack");
	}

	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 加密控件设置随机数
		settingControl.randomNumber = (String) biiResponseBody.getResult();
	}

	/**
	 * 菜单处理
	 * 
	 * @author xyl
	 */
	@Override
	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
		super.selectedMenuItemHandler(context, menuItem);
		String menuId = menuItem.MenuID;
		
		if(menuId.equals("settingManager_1")){// 账户管家
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof AccountManagerActivity)) {
				Intent intent0 = new Intent();
				intent0.setClass(this, AccountManagerActivity.class);
				context.startActivity(intent0);
				ActivityTaskManager.getInstance().removeAllActivity();
			}
		}
		else 	if(menuId.equals("settingManager_2")){// 交易查询
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LimitSettingActivity)) {
				Map<String, Object> returnMap = (Map<String, Object>) BaseDroidApp
						.getInstanse().getBizDataMap()
						.get(ConstantGloble.BIZ_LOGIN_DATA);
				String segmentId = (String) returnMap.get(Crcd.CRCD_SEGMENTID);
				if (StringUtil.isNull(segmentId)
						|| ConstantGloble.CRCD_TEN.equals(segmentId)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getString(R.string.crcd_no_crcd));
					
				} else {
					Intent intent1 = new Intent();
					intent1.setClass(this, LimitSettingActivity.class);
					context.startActivity(intent1);
					ActivityTaskManager.getInstance().removeAllActivity();
				}
			}
		}
		else 	if(menuId.equals("settingManager_3")){// 修改密码
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof EditLoginPassActivity)) {
				Intent intent1 = new Intent();
				intent1.setClass(this, EditLoginPassActivity.class);
				context.startActivity(intent1);
				ActivityTaskManager.getInstance().removeAllActivity();
			}
		}
		else 	if(menuId.equals("settingManager_4")){// 修改预留信息和头像
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof EditObligateMessageActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(this, EditObligateMessageActivity.class);
				context.startActivity(intent);
			}
		}
		else 	if(menuId.equals("settingManager_5")){// 修改退出时间设定
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof ExitTimeSettingActivity)) {
				Intent intent3 = new Intent();
				intent3.setClass(this, ExitTimeSettingActivity.class);
				context.startActivity(intent3);
				ActivityTaskManager.getInstance().removeAllActivity();
			}
		}
		else 	if(menuId.equals("settingManager_6")){// 电子支付2级菜单
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof EPayMainActivity)) {
				Intent intent3 = new Intent();
				intent3.setClass(this, EPayMainActivity.class);
				context.startActivity(intent3);
				ActivityTaskManager.getInstance().removeAllActivity();
			}
		}
		else 	if(menuId.equals("settingManager_7")){// 支付交易查询
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof TransQueryActivity)) {
				Intent intent3 = new Intent();
				intent3.setClass(this, TransQueryActivity.class);
				context.startActivity(intent3);
				ActivityTaskManager.getInstance().removeAllActivity();
			}
		}
		else 	if(menuId.equals("settingManager_8")){// 设定默认账户
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof SettingAccountActivity)) {
				Intent intent3 = new Intent();
				intent3.setClass(this, SettingAccountActivity.class);
				context.startActivity(intent3);
				ActivityTaskManager.getInstance().removeAllActivity();
			}
		}
		else 	if(menuId.equals("settingManager_9")){//投资理财
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof InvesHasOpenActivity)) {
				Intent intent3 = new Intent();
				intent3.setClass(this, InvesHasOpenActivity.class);
				context.startActivity(intent3);
				ActivityTaskManager.getInstance().removeAllActivity();
			}	
		}
		else 	if(menuId.equals("settingManager_10")){//中银E盾信息
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof AudioKeyInfoActivity)) {
				Intent intent9 = new Intent();
				intent9.setClass(this, AudioKeyInfoActivity.class);
				context.startActivity(intent9);
				ActivityTaskManager.getInstance().removeAllActivity();
			}
		}
		else 	if(menuId.equals("settingManager_11")){//修改中银E盾密码
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof AudioKeyPwdActivity)) {
				Intent intent10 = new Intent();
				intent10.setClass(this, AudioKeyPwdActivity.class);
				context.startActivity(intent10);
				ActivityTaskManager.getInstance().removeAllActivity();
			}
		}
		else 	if(menuId.equals("settingManager_12")){//管理绑定设备
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof HardwareBindingActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent11 = new Intent();
				intent11.setClass(this, HardwareBindingActivity.class);
				context.startActivity(intent11);
			}
		}
		else 	if(menuId.equals("settingManager_13")){//安全工具设置
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof SafetyToolsActivity)) {

				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent12 = new Intent();
				intent12.setClass(this, SafetyToolsActivity.class);
				context.startActivity(intent12);
			}
		}
		return true;
//		
//		switch (clickIndex) {
//		case 0:// 账户管家
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof AccountManagerActivity)) {
//				Intent intent0 = new Intent();
//				intent0.setClass(this, AccountManagerActivity.class);
//				startActivity(intent0);
//				ActivityTaskManager.getInstance().removeAllActivity();
//			}
//			break;
//		case 1:// 设定默认账户
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof SettingAccountActivity)) {
//				Intent intent3 = new Intent();
//				intent3.setClass(this, SettingAccountActivity.class);
//				startActivity(intent3);
//				ActivityTaskManager.getInstance().removeAllActivity();
//			}
//			break;
//			//投资理财
//		case 2:
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof InvesHasOpenActivity)) {
//				Intent intent3 = new Intent();
//				intent3.setClass(this, InvesHasOpenActivity.class);
//				startActivity(intent3);
//				ActivityTaskManager.getInstance().removeAllActivity();
//			}		
//			break;
//		case 3:// 交易查询
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LimitSettingActivity)) {
//				Map<String, Object> returnMap = (Map<String, Object>) BaseDroidApp
//						.getInstanse().getBizDataMap()
//						.get(ConstantGloble.BIZ_LOGIN_DATA);
//				String segmentId = (String) returnMap.get(Crcd.CRCD_SEGMENTID);
//				if (StringUtil.isNull(segmentId)
//						|| ConstantGloble.CRCD_TEN.equals(segmentId)) {
//					BaseDroidApp.getInstanse().showInfoMessageDialog(
//							getString(R.string.crcd_no_crcd));
//					return;
//				} else {
//					Intent intent1 = new Intent();
//					intent1.setClass(this, LimitSettingActivity.class);
//					startActivity(intent1);
//					ActivityTaskManager.getInstance().removeAllActivity();
//				}
//			}
//			break;
//		case 4:// 修改密码
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof EditLoginPassActivity)) {
//				Intent intent1 = new Intent();
//				intent1.setClass(this, EditLoginPassActivity.class);
//				startActivity(intent1);
//				ActivityTaskManager.getInstance().removeAllActivity();
//			}
//			break;
//		case 5:// 修改预留信息和头像
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof EditObligateMessageActivity)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(this, EditObligateMessageActivity.class);
//				startActivity(intent);
//			}
//			break;
//		case 6:// 修改退出时间设定
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof ExitTimeSettingActivity)) {
//				Intent intent3 = new Intent();
//				intent3.setClass(this, ExitTimeSettingActivity.class);
//				startActivity(intent3);
//				ActivityTaskManager.getInstance().removeAllActivity();
//			}
//			break;
//		// add by cyf
//		case 7:// 电子支付2级菜单
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof EPayMainActivity)) {
//				Intent intent3 = new Intent();
//				intent3.setClass(this, EPayMainActivity.class);
//				startActivity(intent3);
//				ActivityTaskManager.getInstance().removeAllActivity();
//			}
//			break;
//		case 8:// 支付交易查询
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof TransQueryActivity)) {
//				Intent intent3 = new Intent();
//				intent3.setClass(this, TransQueryActivity.class);
//				startActivity(intent3);
//				ActivityTaskManager.getInstance().removeAllActivity();
//			}
//			break;
//		case 9://中银E盾信息
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof AudioKeyInfoActivity)) {
//				Intent intent9 = new Intent();
//				intent9.setClass(this, AudioKeyInfoActivity.class);
//				startActivity(intent9);
//				ActivityTaskManager.getInstance().removeAllActivity();
//			}
//			break;
//		case 10://修改中银E盾密码
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof AudioKeyPwdActivity)) {
//				Intent intent10 = new Intent();
//				intent10.setClass(this, AudioKeyPwdActivity.class);
//				startActivity(intent10);
//				ActivityTaskManager.getInstance().removeAllActivity();
//			}
//			break;
//		case 11://管理绑定设备
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof HardwareBindingActivity)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent11 = new Intent();
//				intent11.setClass(this, HardwareBindingActivity.class);
//				startActivity(intent11);
//			}
//			break;
//		case 12://安全工具设置
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof SafetyToolsActivity)) {
//
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent12 = new Intent();
//				intent12.setClass(this, SafetyToolsActivity.class);
//				startActivity(intent12);
//
//				
//			}
//			break;
//		}
	}

	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	protected void baseinit() {
		initPulldownBtn();
		initFootMenu();
		initLeftSideList(this, LocalData.settingManagerlistData);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		mainInflater = LayoutInflater.from(this);
		back = (Button) findViewById(R.id.ib_back);
		right = (Button) findViewById(R.id.ib_top_right_btn);
		back.setOnClickListener(this);
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		// 把这个Activity放到Activity集中管理
//		ActivityTaskManager.getInstance().addActivit(this);
//
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			SettingBaseActivity.this.onBackPressed();
			break;

		default:
			break;
		}
	}

//	/** 主界面 */
	protected void initRightBtnForMain() {
		// right.setText(getString(R.string.forex_right));
		// right.setVisibility(View.VISIBLE);
		// right.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// activityTaskManager.removeAllActivity();
		// }
		// });
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}
}
