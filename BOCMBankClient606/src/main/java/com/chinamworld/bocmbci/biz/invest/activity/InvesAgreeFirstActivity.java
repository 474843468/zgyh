package com.chinamworld.bocmbci.biz.invest.activity;

import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;

/**
 * ForexInvesAgreeActivity：开通投资理财服务页面，用户选择接受或不接受,输入动态口令可开通
 * 
 * @author xby
 * 
 */
public class InvesAgreeFirstActivity extends InvesAgreeActivity {
	@Override
	protected void removeAllActivity(){
		ActivityTaskManager.getInstance().removeAllActivity();	
	}
	@Override
	public ActivityTaskType getActivityTaskType() {
		return ActivityTaskType.OneTask;
	}
}
