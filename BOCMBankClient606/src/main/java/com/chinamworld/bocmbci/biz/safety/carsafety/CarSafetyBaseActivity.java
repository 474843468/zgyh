package com.chinamworld.bocmbci.biz.safety.carsafety;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyBaseActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyConstant;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 车险基类，负责暂存保单流程与方法
 * 
 * @author Zhi
 */
public class CarSafetyBaseActivity extends SafetyBaseActivity {
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 保单名 */
	protected String policyName;
	/** 保单别名  */
	protected EditText mSavepolicyName;
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	/** 显示暂存保单对话框 */
	public void showSaveDialog(){
		if (SafetyDataCenter.getInstance().isSaveToThere || SafetyDataCenter.getInstance().isSaved) {
			// 如果是从暂存保单模块跳转过来或者非首次保存保单，直接进行保存保单
			BaseHttpEngine.showProgressDialog();
			requestPSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		} else {
			// 否则弹出录入暂存单名称对话框
			View dialogView = View.inflate(this, R.layout.safety_save_dialog, null);
			mSavepolicyName = (EditText)dialogView.findViewById(R.id.save_alias);
			EditTextUtils.setLengthMatcher(this, mSavepolicyName, 50);
			mSavepolicyName.setText("车险"+SafetyDataCenter.getInstance().getSysTimeFull());
			BaseDroidApp.getInstanse().showDialog(dialogView);
		}
	}
	
	/** 暂存保单弹出对话框中按钮的点击事件 */
	public void dialogClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cance:
			BaseDroidApp.getInstanse().dismissErrorDialog();
			break;

		case R.id.btn_confirm:
			policyName = mSavepolicyName.getText().toString().trim();
			
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			RegexpBean policyNameRegexp = new RegexpBean(SafetyConstant.TEMPNAME, policyName, SafetyConstant.TEMP_NAME);
			lists.add(policyNameRegexp);
			if (!RegexpUtils.regexpDate(lists)) {
				return;
			}
			
//			if (StringUtil.isNull(policyName)) {
//				CustomDialog.toastInCenter(this, SafetyConstant.ALIAS);
//				return;
//			}
			BaseHttpEngine.showProgressDialog();
			SafetyDataCenter.getInstance().getMapSaveParams().put(Safety.POLICYNAME, policyName);
			requestPSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
			break;
		}
	}

	public boolean regexpDate(String date, boolean isBJ) {
		String sysDate = SafetyDataCenter.getInstance().getSysTime();
		if ((Integer.valueOf(date.substring(0, 4)) > Integer.valueOf(sysDate.substring(0, 4)))) {
			showInfoMessage(isBJ);
			return false;
		} else if (date.substring(0, 4).equals(sysDate.substring(0, 4))
				&& (Integer.valueOf(date.substring(5, 7)) > Integer.valueOf(sysDate.substring(5, 7)))) {
			showInfoMessage(isBJ);
			return false;
		} else if (date.substring(0, 4).equals(sysDate.substring(0, 4))
				&& date.substring(5, 7).equals(sysDate.substring(5, 7))
				&& (Integer.valueOf(date.substring(8, 10)) > Integer.valueOf(sysDate.substring(8, 10)))) {
			showInfoMessage(isBJ);
			return false;
		}
		return true;
	}
	
	private void showInfoMessage(boolean isBJ) {
		if (isBJ) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("购车发票日期不能晚于系统日期");
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog("注册日期不能晚于系统日期");
		}
	}
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO ---------------------------------------网络请求与回调方法--------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("unchecked")
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		SafetyDataCenter.getInstance().getMapSaveParams().put(ConstantGloble.PUBLIC_TOKEN, BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID));
		HashMap<String, Object> params = (HashMap<String, Object>) ((HashMap<String, Object>) SafetyDataCenter.getInstance().getMapSaveParams()).clone();
		httpTools.requestHttp(Safety.METHOD_PSNAUTOINSURPOLICYSAVE, "requestPsnAutoInsurPolicySaveCallBack", params, true);
	}
	
	/** 暂存保单保存/更新回调 */
	public void requestPsnAutoInsurPolicySaveCallBack(Object resultObj) {
		BaseDroidApp.getInstanse().dismissErrorDialog();
		BaseHttpEngine.dissMissProgressDialog();
		CustomDialog.toastShow(this,getString(R.string.safety_save_tip));
		SafetyDataCenter.getInstance().isSaved = true;
		SafetyDataCenter.getInstance().getMapSaveParams().put(Safety.TYPEFLAG, "1");
	}
}
