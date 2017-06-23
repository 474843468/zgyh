package com.boc.bocsoft.mobile.bocmobile.buss.login.ui;

import android.app.Activity;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;
import com.boc.bocsoft.mobile.bii.common.client.BIIClientConfig;
import com.boc.bocsoft.mobile.bocmobile.buss.common.LoginCallback;
import com.boc.bocsoft.mobile.common.client.CookieStore;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.chinamworld.boc.commonlib.BaseCommonTools;
import com.chinamworld.boc.commonlib.ModuleManager;
import com.chinamworld.boc.commonlib.SaveDataManager;
import com.chinamworld.boc.commonlib.model.IActionCallBack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import okhttp3.Cookie;

/**
 * 登录上下文
 * Created by lxw on 2016/8/28 0028.
 */
public enum LoginContext {

    instance;//单例对象
    LoginCallback callback;

    public void setCallback(LoginCallback callback){

        this.callback =callback;
    }

    public LoginCallback getCallback(){

        return callback;
    }

    /**
     * 设置当前activity
     * @param var1
     */
    public void setCurrentActivity(Activity var1){
        if(BaseCommonTools.getInstance() == null)
            return;
        BaseCommonTools.getInstance().setCurrentActivity(var1);
    }
    /**
     * 保存登录用户市场细分
     * @param segmentId
     */
    public void saveSegmentId(String segmentId){
        if(SaveDataManager.getInstance() == null)return;
        /** 保存登录信息中的segmentId 字段，来自LoginWP7接口*/
        SaveDataManager.getInstance().saveSegmentId(segmentId);
    }

    /**
     * 保存操作员信息，含有用户等级
     * @param info
     */
    public void saveLoginCommonInfo(Map<String, Object> info){
        if(SaveDataManager.getInstance() == null)return;
        /** 保存操作员信息,来自PsnCommonQueryOprLoginInfo接口的数据 */
        SaveDataManager.getInstance().saveLoginCommonInfo(info);
    }

    /**
     * 保存cookie到联龙
     */
    public void saveCookiesToLianLong(){
        if(BaseCommonTools.getInstance() == null)return;

        CookieStore cookieStore = BIIClient.instance.getCookies();

        String biiUrl = BIIClientConfig.getBiiUrl();
        String bpmsUrl = BIIClientConfig.getBPMSUrl();


        try {
            URL biiURL = new URL(biiUrl);
            URL bpmsURL = new URL(bpmsUrl);
            List<Cookie> cookies = cookieStore.getCookies();
            for (Cookie cookie : cookies) {

                int port = -1;
                if (biiURL.getHost().equals(cookie.domain())){
                    port = biiURL.getPort();
                } else if (bpmsURL.getHost().equals(cookie.domain())){
                    port = bpmsURL.getPort();
                }

                String result = "";
                if (port > 0) {
                    result = port + "";
                }

                if (StringUtils.isEmptyOrNull(result) ) {
                    BaseCommonTools.getInstance().SetCookie(cookie.toString(), cookie.domain());
                } else {
                    BaseCommonTools.getInstance().SetCookie(cookie.toString(), cookie.domain() + ":" + result);
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存电子卡签约用户信息
     * @param accountBean
     * @param isECard
     */
    public void saveECardFlag(Map<String, Object> accountBean,boolean isECard){
        if(SaveDataManager.getInstance() == null)return;

        SaveDataManager.getInstance().saveECardFlag(accountBean,isECard);
    }

    /**
     * 是否开通了音频key
     * @param isEKeyOpen
     */
    public void saveEKeyOpenStatus(boolean isEKeyOpen){
        if(SaveDataManager.getInstance() == null)return;

        SaveDataManager.getInstance().saveEKeyOpenStatus(isEKeyOpen);
    }

    /**
     * 跳转到忘记密码页面
     * @param activity
     */
    public void gotoForgetPasswordModule(Activity activity){
        if( ModuleManager.instance == null)return;

        ModuleManager.instance.gotoForgetPasswordModule(activity);
    }

    /**
     * 跳转到自助注册页面
     * @param activity
     */
    public void gotoRegistModule(Activity activity){
        if( ModuleManager.instance == null)return;

        ModuleManager.instance.gotoRegistModule(activity);
    }

    /**
     * 统计接口，上送设备信息
     * @param activity
     * @param cifNumber
     */
    public void sendFunctionUsedAction(Activity activity, String cifNumber){
        if( ModuleManager.instance == null)return;

        ModuleManager.instance.sendFunctionUsedAction(activity, cifNumber);
    }


    /**
     * 消息推送绑定硬件信息
     */
    public void bindingDeviceForPushService(Activity activity, IActionCallBack callBack){
        if( ModuleManager.instance == null)return;

        ModuleManager.instance.BindingDeviceForPushService(activity,callBack);
    }

    /**
     * 消息推送绑定硬件信息
     */
    public void bindingDeviceForPushService(Activity activity){
        if( ModuleManager.instance == null)return;

        SaveDataManager.getInstance().BindingDeviceForPushServiceSuccess(activity);
    }

    /**
     * 持卡登录跳转
     * @param activity
     * @param accountSeq
     * @param isDebit
     */
    public void gotoBocnetLoginModule(Activity activity, Map<String, Object> loginFor,String accountSeq, boolean isDebit){
        if( ModuleManager.instance == null)return;

        ModuleManager.instance.gotoBocnetLoginModule(activity,loginFor,accountSeq,isDebit);
    }

}
