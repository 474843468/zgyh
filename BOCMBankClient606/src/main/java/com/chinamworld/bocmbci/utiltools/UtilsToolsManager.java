package com.chinamworld.bocmbci.utiltools;

import android.content.Context;

import com.chinamworld.bocmbci.UtilsManager;
import com.chinamworld.bocmbci.base.activity.ActivitySwitcher;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.main.MainSetting;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;

public class UtilsToolsManager extends UtilsManager {

	
	public UtilsToolsManager(){
		instance = this;
		new RUtilsManager();
		new ShowDialogToolsManager();
		new PopupWindowUtils();
		new HttpManagerTools();
		new MainSetting();
		new ActivitySwitcher(null);
		new LoginTool();
	
		
	}
	@Override
	public Context getApplicationContext() {
		return BaseDroidApp.getContext();
	}

	@Override
	public boolean getWriteLogFlag() {
		return SystemConfig.DEBUG;
	}
	@Override
	public String getPushUrl() {
		return SystemConfig.BASE_PUSH_RUL;
	}
	
	


	
	
	/** WMS基金请求的地址 */
	@Override
	public  String getFincAdress(){
		return SystemConfig.FINCADDRESS;
	}
	
	/** BASE_HTTP_URL */
	@Override
	public String getBaseUrl(){
		return SystemConfig.BASE_HTTP_URL;
	}
	
	
	/** CipherType */
	@Override
	public  int getCipherType(){
		return SystemConfig.CIPHERTYPE;
	}
	
	/** AppVersion */
	@Override
	public  String getAppVersion(){
		return SystemConfig.APP_VERSION;
	}
	
	@Override
	public void setBTCCMWApplicationFlag(boolean flag) {
		// TODO Auto-generated method stub
		BTCCMWApplication.flag=flag;
	}
}
