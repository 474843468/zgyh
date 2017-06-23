package com.chinamworld.bocmbci.commonlibtools;

import android.app.Activity;

import com.boc.bocsoft.mobile.bocmobile.base.model.Module;
import com.chinamworld.boc.commonlib.AbstractBocModule;
import com.chinamworld.bocmbci.boc.ModelBoc;
import com.chinamworld.bocmbci.interfacemodule.IActionTwo;


import java.util.Map;

/**
 * Created by Administrator on 2016/11/24.
 */
public class BocModule extends AbstractBocModule {

    public BocModule(){
        instance = this;
    }
    @Override
    public void gotoRegistDHAccountActivity(Activity activity, Map<String, String> map, final com.chinamworld.boc.commonlib.interfacemodule.IActionTwo iActionTwo) {
        ModelBoc.gotoBocPageActivity(activity,null,new IActionTwo(){
            @Override
            public void callBack(Object param1, Object param2) {
                if(iActionTwo != null) {
                    iActionTwo.callBack(param1,param2);
                }
            }
        });
    }
}
