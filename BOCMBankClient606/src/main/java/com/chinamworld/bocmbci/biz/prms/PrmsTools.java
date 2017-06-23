package com.chinamworld.bocmbci.biz.prms;

import android.app.Activity;
import android.content.Intent;

import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.lsforex.rate.IsForexTwoWayDetailActivity;
import com.chinamworld.bocmbci.biz.prms.price.PrmsNewPricesActivity;
import com.chinamworld.bocmbci.biz.prms.price.PrmsNewPricesDetailActivity;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/23.
 */
public class PrmsTools {
    /**扫描完成后 需要执行的动作*/
    public boolean isPrmsQRHandler(Activity activity, String qrInfo){
        String str1 = "boc://bocphone?type=";
        int type = Integer.parseInt(qrInfo.substring(str1.length(),str1.length()+1));
        if(!StringUtil.isNullOrEmpty(qrInfo)&&qrInfo.length()>str1.length()){
            if(type == 5){
                String str3 = "boc://bocphone?type=5&prmsRQCode=";
                String sourceCode = qrInfo.substring(str3.length(), str3.length()+3);
                String targetCode = qrInfo.substring(str3.length()+3,str3.length()+6);
                BaseDroidApp.getInstance().getBizDataMap().put("sourceCurrencyCodeStr",sourceCode);
                BaseDroidApp.getInstance().getBizDataMap().put("targetCurrencyStr",targetCode);
                Intent intent = new Intent();
                PrmsNewPricesActivity.prmsFlagGoWay = 3;
                intent.setClass(activity, PrmsNewPricesDetailActivity.class);
                intent.putExtra("sourceCurrencyCodeStr",sourceCode);
                intent.putExtra("targetCurrencyStr",targetCode);
                intent.putExtra("enters",3);//1是优选投资跳进去,2 内部跳进去,3是主页面扫二维码进去
                activity.startActivity(intent);
                return true;
            }
        }
        return false;
    }
}
