package com.chinamworld.bocmbci.biz.bocinvt_p603;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;

import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.NewBaseActivity;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;


/**
 * P603 投资理财模块，基类BaseActivity
 * @author yuht
 *
 */
public class BocInvtBaseActivity extends NewBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getBackgroundLayout().setLeftMenuData(LocalData.bocinvtManagerLeftList);	

	}
    @Override
    protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
    	super.selectedMenuItemHandler(context, menuItem);
    	ActivityTaskManager.getInstance().removeAllSecondActivity();
    	ImageAndText menu = menuItem;
    	HashMap<String,Object> data = new HashMap<String,Object>();
    	if(menuItem.MenuID.equals("bocinvtManager_5")){
    		data.put("flag", true);
    	}
    	ActivityIntentTools.intentToActivityWithData(context, menu.getClassName(),data);
    	return true;
    	
    	
//    	ActivityTaskManager.getInstance().removeAllSecondActivity();
//    	ImageAndText menu = LocalData.bocinvtManagerLeftList.get(selectedIndex);
//    	HashMap<String,Object> data = new HashMap<String,Object>();
//    	if(selectedIndex == 5){
//    		data.put("flag", true);
//    	}
//    	ActivityIntentTools.intentToActivityWithData(this, menu.getClassName(),data);
    }
	
	@Override
	public ActivityTaskType getActivityTaskType() {
		return ActivityTaskType.TwoTask;
	}

}
