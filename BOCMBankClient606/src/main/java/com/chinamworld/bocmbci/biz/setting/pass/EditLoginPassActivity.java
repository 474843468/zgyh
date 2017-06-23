package com.chinamworld.bocmbci.biz.setting.pass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cfca.mobile.sip.SipBox;

import com.cfca.mobile.log.CodeException;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalDataCenter;
import com.chinamworld.bocmbci.constant.CheckRegExp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.RegCode;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.CommPublicTools;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 服务设定，修改密码页面
 * 
 * @author xyl
 * 
 */
public class EditLoginPassActivity extends PassBaseActivity {
	private static final String TAG = "EditLoginPassActivity";
	/***
	 * 旧密码编辑框
	 */
	private SipBox oldPassEdit;

	/**
	 * 新密码编辑框
	 */
	private SipBox newPassEdit;
	/**
	 * 新密码确认编辑框
	 */
	private SipBox newPassConfrimEdit;
	/**
	 * 确定按钮，点击，实现修改功能
	 */
	private Button confirm;
	/**
	 * 用户输入的旧密码
	 */
	private String oldPassStr;
	/**
	 * 用户输入的新密码
	 */
	private String newPassStr;
	/**
	 * 用户输入的新密码确认
	 */
	private String newPassConffirmStr;

	private int httpTag;
	private static final int RANDOM = 0;
	private static final int FACTOR = 1;

	private String random;
	private String oldPass_RC;
	private String newPass2_RC;
	private String newPass_RC;

	private String editPassConversationId;
	/** 是否是查询版用户 */
	private boolean isQureyAcc = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		right.setVisibility(View.GONE);
		setLeftSelectedPosition("settingManager_3");
		// httpTag = RANDOM;
		requestCommConversationId();
		BaseHttpEngine.showProgressDialogCanGoBack();
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		setLeftSelectedPosition("settingManager_3");
//	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		editPassConversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		// switch (httpTag) {
		// case RANDOM:
		requestForRandomNumber();
		// break;
		// case FACTOR:
		// requestGetSecurityFactor(ConstantGloble.SET_SERVICEID);
		// default:
		// break;
		// }

	}

	@Override
	public void queryRandomNumberCallBack(Object resultObj) {
		super.queryRandomNumberCallBack(resultObj);
		// sVRPasswordChoose();
		// isQureyAcc=
		// Setting.C_MASKFLAG_QUERY.equals(biiResponseBody.getResult());
		Map<String, Object> resultMap = (Map<String, Object>) BaseDroidApp
				.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);
		isQureyAcc = ConstantGloble.CRCD_TEN.equals(resultMap
				.get(Login.SEGMENT_ID));
		BaseHttpEngine.dissMissProgressDialog();
		if (!StringUtil.isNullOrEmpty(settingControl.randomNumber)) {
			random = settingControl.randomNumber;
			init();
		}
	}

	@Override
	public void sVRPasswordChooseCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.sVRPasswordChooseCallBack(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 加密控件设置随机数
		isQureyAcc = ConstantGloble.CRCD_TEN
				.equals(biiResponseBody.getResult());
		if (!StringUtil.isNullOrEmpty(settingControl.randomNumber)) {
			random = settingControl.randomNumber;
			init();
		}
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialog(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						BaseHttpEngine.showProgressDialog();
						settingLoginPassConfirm(oldPassStr, oldPass_RC,
								newPassStr, newPass_RC, newPassConffirmStr,
								newPass2_RC, BaseDroidApp.getInstanse()
										.getSecurityChoosed(),
								editPassConversationId);
					}
				});

	}

	@Override
	public void settingLoginPassConfirmCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// Map<String, Object> responseResult = (Map<String, Object>)
		// biiResponseBody.getResult();
		settingControl.responseResult = (Map<String, Object>) biiResponseBody
				.getResult();
		// settingControl.factorList = (List<Map<String, Object>>)
		// map.get(Inves.FACTORLIST);
		if (!StringUtil.isNullOrEmpty(settingControl.responseResult)) {
			Intent intent = new Intent();
			intent.setClass(EditLoginPassActivity.this,
					EditPassConfirmActivity.class);
			intent.putExtra(Setting.I_MASKFLAG, Setting.C_MASKFLAG_VIP);
			intent.putExtra(Setting.I_RANDOM, random);
			intent.putExtra(Setting.I_CONVERSATIONID, editPassConversationId);
			startActivity(intent);
		}
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String tokenId = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		settingLoginPassQueryResult(oldPassStr, oldPass_RC, newPassStr,
				newPass_RC, newPassConffirmStr, newPass2_RC,
				editPassConversationId, tokenId);
	}

	@Override
	public void settingLoginPassQueryResultCallBack(Object resultObj) {
		super.settingLoginPassQueryResultCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent(this, EditLoginPassActivity.class);
		CustomDialog.toastShow(this,
				getResources().getString(R.string.set_editpasssuccess_info));
		activityTaskManager.removeAllActivity();
		startActivity(intent);
	}

	/**
	 * 
	 * @Author xyl
	 */
	private void init() {
		View childView = mainInflater.inflate(R.layout.setting_editpass_main,
				null);
		tabcontent.addView(childView);
		oldPassEdit = (SipBox) childView.findViewById(R.id.set_oldloginpass);
		oldPassEdit.setCipherType(SystemConfig.CIPHERTYPE);
		newPassEdit = (SipBox) childView.findViewById(R.id.set_newloginpass);
		newPassEdit.setCipherType(SystemConfig.CIPHERTYPE);
		newPassConfrimEdit = (SipBox) childView
				.findViewById(R.id.set_newloginpassconfirm);
		newPassConfrimEdit.setCipherType(SystemConfig.CIPHERTYPE);

		oldPassEdit.setRandomKey_S(random);
		newPassEdit.setRandomKey_S(random);
		newPassConfrimEdit.setRandomKey_S(random);

		oldPassEdit.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE);
		newPassEdit.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE);
		newPassConfrimEdit
				.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE);

		oldPassEdit.setPasswordRegularExpression(CheckRegExp.newPass);

//		newPassEdit.setPasswordRegularExpression(CheckRegExp.newPass);
//		newPassConfrimEdit.setPasswordRegularExpression(CheckRegExp.newPass);
//		newPassEdit.setPasswordRegularExpression("(?=(\\\\S*[0-9]\\\\S*[a-zA-Z]|\\\\S*[a-zA-Z]\\\\S*[0-9]))^\\\\S{8,20}$");
//		newPassConfrimEdit.setPasswordRegularExpression("(?=(\\\\S*[0-9]\\\\S*[a-zA-Z]|\\\\S*[a-zA-Z]\\\\S*[0-9]))^\\\\S{8,20}$");
		oldPassEdit
				.setPasswordMinLength(ConstantGloble.EDILOGINPASS_MIN_LENGTH);
		oldPassEdit
				.setPasswordMaxLength(ConstantGloble.EDILOGINPASS_MAX_LENGTH);

		newPassEdit
				.setPasswordMinLength(ConstantGloble.EDILOGINPASS_MIN_LENGTH);
		newPassEdit
				.setPasswordMaxLength(ConstantGloble.EDILOGINPASS_MAX_LENGTH);

		newPassConfrimEdit
				.setPasswordMinLength(ConstantGloble.EDILOGINPASS_MIN_LENGTH);
		newPassConfrimEdit
				.setPasswordMaxLength(ConstantGloble.EDILOGINPASS_MAX_LENGTH);

		oldPassEdit.setSipDelegator(this);
		newPassEdit.setSipDelegator(this);
		newPassConfrimEdit.setSipDelegator(this);

		confirm = (Button) childView.findViewById(R.id.set_editpass_confirm);
		setTitle(getResources().getString(R.string.set_title_editpass));
		confirm.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.set_editpass_confirm:

			if (TextUtils.isEmpty(oldPassEdit.getText())) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("请输入原登录密码");
				return;
			}

			RegexpBean regexOldPass = new RegexpBean(getResources().getString(
					R.string.set_oldloginpass_no), oldPassEdit.getText()
					.toString(), RegCode.NEW_FORCE_MODIFY_PASSWORD);
			if (!regexpDate(regexOldPass)) {
				return;
			}

			if (TextUtils.isEmpty(newPassEdit.getText())) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("请输入新登陆密码");
				return;
			}
			if(newPassEdit.getText().length()>20||newPassEdit.getText().length()<8){
				BaseDroidApp.getInstanse().showInfoMessageDialog("密码长度必须为8-20位");
				return;
			}

			if(false == CommPublicTools.checkCFCAInfo(newPassEdit, PreciousmetalDataCenter.newRege,getString(R.string.set_oldPass_error_new)))
				return ;
			if (TextUtils.isEmpty(newPassConfrimEdit.getText())) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("请输入新密码确认");
				return;
			}
			if(newPassConfrimEdit.getText().length()>20||newPassConfrimEdit.getText().length()<8){
				BaseDroidApp.getInstanse().showInfoMessageDialog("密码长度必须为8-20位");
				return;
			}

			if(false == CommPublicTools.checkCFCAInfo(newPassConfrimEdit, PreciousmetalDataCenter.newRege,getString(R.string.set_oldPass_error_new)))
				return ;
//			RegexpBean regexNewPass1 = new RegexpBean(getResources().getString(
//			R.string.set_newloginpass_no), newPassEdit.getText()
//				.toString(),"newForceModifyPasswordNew","dfdsffsdfsdfsdfsdfsf");
//			if (!regexpDate(regexNewPass1)) {
//				return;
//			}

			if (TextUtils.isEmpty(newPassConfrimEdit.getText())) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("请输入新密码确认");
				return;
			}

			RegexpBean regexNewPass2 = new RegexpBean(getResources().getString(
					R.string.set_newloginpassconfrim_no), newPassConfrimEdit
					.getText().toString(), "newForceModifyPassword");
//			if (regexpDate(regexNewPass2)) {
				httpTag = FACTOR;
				try {
					oldPassStr = oldPassEdit.getValue().getEncryptPassword();
					oldPass_RC = oldPassEdit.getValue().getEncryptRandomNum();
				} catch (CodeException e) {
					LogGloble.e(TAG, "密码控件问题");
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getString(R.string.set_oldPass_error));
					return;
				}
				try {
					newPassStr = newPassEdit.getValue().getEncryptPassword();
					newPass_RC = newPassEdit.getValue().getEncryptRandomNum();
				} catch (CodeException e) {
					LogGloble.e(TAG, "密码控件问题");
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getString(R.string.set_oldPass_error_new));
					return;
				}
				try {
					newPassConffirmStr = newPassConfrimEdit.getValue()
							.getEncryptPassword();
					newPass2_RC = newPassConfrimEdit.getValue()
							.getEncryptRandomNum();
				} catch (CodeException e) {
					LogGloble.e(TAG, "密码控件问题");
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getString(R.string.set_oldPass_error_new));
					return;
				}
				BaseHttpEngine.showProgressDialog();
				if (isQureyAcc) {
					requestPSNGetTokenId(editPassConversationId);
				} else {
					requestGetSecurityFactor(ConstantGloble.SET_SERVICEID);
				}
//			}
			break;
		default:
			break;
		}
	}

	private boolean regexpDate(RegexpBean regex) {
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		lists.add(regex);
		return RegexpUtils.regexpDate(lists);
	}


}
