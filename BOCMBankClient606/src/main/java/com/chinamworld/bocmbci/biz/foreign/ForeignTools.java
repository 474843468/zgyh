package com.chinamworld.bocmbci.biz.foreign;

import android.app.Activity;
import android.content.Intent;

import com.chinamworld.bocmbci.base.application.CommonApplication;
import com.chinamworld.bocmbci.biz.foreign.details.ForeignTradeDetailsActivity;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.bocmbci.utiltools.HttpHandle;

import java.util.HashMap;
import java.util.Map;

/**
 * ForeignTools 外汇模块入口管理
 * @author luqp 2016/11/8
 */
public class ForeignTools extends HttpHandle {
    private Activity mActivity;
    /** 二维码全部信息*/
    private String QRCodeAllTheInformation;
    public ForeignTools(Activity activity) {
        super(activity);
        mActivity = activity;
        CommonApplication.getInstance().setCurrentAct(activity); // 从软件中心模块跳过来，需要先保存当前的activity
    }

    /**
     * 外汇详情页面 入口
     * @param activity
     * @param QRCode 二维码信息
     * return false:不做处理 true:二维码解析成功
     */
    public Boolean gotoDetailsOfForeignExchange(Activity activity, String QRCode){
        this.mActivity = activity;
        String QRTepy = QRCode.substring(QRCode.indexOf("?")+1,QRCode.indexOf("&"));
        if (!QRTepy.contains("3")){ // type不包含3 返回false
            return false;
        } else {
            //解析QRCode boc://bocphone?type=3&forexRQCode=001002
            String sourceCode = QRCode.substring(QRCode.length() - 6, QRCode.length() - 3);
            String targetCode = QRCode.substring(QRCode.length() - 3, QRCode.length());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("sourceCode", sourceCode);
            map.put("targetCode", targetCode);
            map.put("isQrcode", true);
            ActivityIntentTools.intentToActivityWithData(activity, ForeignTradeDetailsActivity.class, map);
            return true;
        }
    }
}
