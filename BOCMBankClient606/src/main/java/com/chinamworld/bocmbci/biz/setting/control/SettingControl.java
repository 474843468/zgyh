package com.chinamworld.bocmbci.biz.setting.control;

import java.util.List;
import java.util.Map;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;


public class SettingControl {
	/**
	 * 账户管家所有账户信息
	 */
	public List<Map<String,Object>> accList;
	/**
	 * 默认账户信息
	 */
	public String defaultAccNum;
	private static SettingControl instance=null;
	
	public List<Map<String, Object>> factorList;
	/**报文数据返回结果数据*/
	public Map<String, Object> responseResult;
	/** 随机数 */
	public String randomNumber;
	
	
	public String editLimitConversationId;
	private SettingControl(){
	}
	public static SettingControl getInstance(){
		if(instance==null)
			instance=new SettingControl();
		return instance;
	}
	
	
	/**
	 * 修改交易限额
	 * @return
	 */
	public String[] getStepsForEditLimit() {
		String step1 = 1 + BaseDroidApp.getInstanse().getCurrentAct()
				.getResources()
				.getString(R.string.set_editlimit_step1);
		String step2 = 2 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.set_editlimit_step2);
		String step3 = 3 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.set_editlimit_step3);
		String[] steps = new String[3];
		steps[0] = step1;
		steps[1] = step2;
		steps[2] = step3;
		return steps;
	}
	
	
	public void cleanAll(){
		accList = null ;
		factorList= null;
	}
}
