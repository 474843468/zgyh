package com.chinamworld.bocmbci.biz.lsforex;

import android.app.Activity;

import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.lsforex.rate.IsForexTwoWayDetailActivity;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zxj on 2016/11/23.
 */
public class IsforexTools {
    /**扫描完成后 需要执行的动作*/
    public boolean isForexQRHandler(Activity activity,String qrInfo){
        //解析qrInfo取出类型和基金代码 boc://bocphone?type=4&prmsRQCode=001003
        String str1 = "boc://bocphone?type=";
        int type = Integer.parseInt(qrInfo.substring(str1.length(),str1.length()+1));
        if(!StringUtil.isNullOrEmpty(qrInfo)&&qrInfo.length()>str1.length()){
            if(type == 4){
                String str3 = "boc://bocphone?type=4&lsforexRQCode=";
                String sourceCode = qrInfo.substring(str3.length(), str3.length()+3);
                String targetCode = qrInfo.substring(str3.length()+3,str3.length()+6);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("sourceCode", sourceCode);
                BaseDroidApp.getInstance().getBizDataMap().put("sourceCode", null);
                BaseDroidApp.getInstance().getBizDataMap().put("sourceCode", sourceCode);
                map.put("targetCode", targetCode);
                BaseDroidApp.getInstance().getBizDataMap().put("targetCode", null);
                BaseDroidApp.getInstance().getBizDataMap().put("targetCode", targetCode);
                map.put("isQrcode", true);
                ActivityIntentTools.intentToActivityWithData(activity, IsForexTwoWayDetailActivity.class, map);
                return true;
            }
        }
        return false;
    }
}
