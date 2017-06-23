package com.chinamworld.bocmbci.biz.setting.obligate;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;

import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.setting.SettingBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.GetPhoneInfo;
import com.chinamworld.bocmbci.http.HttpManager;


public class ObligateBaseActivity extends SettingBaseActivity {
//	private static final String TAG = "ObligateBaseActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}


  
  /**
   * 修改欢迎信息请求
   *@Author xyl 
   *@param welcomeInfo  要修改成的欢迎信息
   *@param token  防重机制
   */
  protected void editWelcomeInfo(String welcomeInfo,String token){
	  	BiiRequestBody biiRequestBody = new BiiRequestBody();
			biiRequestBody.setMethod(Setting.SET_EDITWELCOMEINFO);
			//TODO commConverSationId
			biiRequestBody.setConversationId((String)BaseDroidApp
					.getInstanse()
					.getBizDataMap()
					.get(ConstantGloble.CONVERSATION_ID));
			Map<String,Object> map = new HashMap<String, Object>();
			map.put(Setting.SET_EDITWELCOMEINFO_WELCOMEINFO, welcomeInfo);
			map.put(Setting.TOKEN, token);
			//防欺诈信息
			GetPhoneInfo.addPhoneInfo(map);
			biiRequestBody.setParams(map);
			HttpManager.requestBii(biiRequestBody, this,
					"editWelcomeInfoCallback");
	  }
  /**
   * 修改欢迎信息
   *@Author xyl 
   *@param resultObj
   */
  public void editWelcomeInfoCallback(Object resultObj){
  }
}
