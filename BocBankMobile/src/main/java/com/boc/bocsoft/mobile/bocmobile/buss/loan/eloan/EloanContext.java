package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.boc.bocsoft.mobile.bii.common.client.BIIClient;
import com.boc.bocsoft.mobile.bii.common.client.BiiConfigInterface;
import com.boc.bocsoft.mobile.common.client.model.RequestParams;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import com.chinamworld.boc.commonlib.BaseCommonTools;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * 中银E贷应用上下文
 * Created by lxw4566 on 2016/6/21.
 */
public enum EloanContext {

    instance;//单例对象
    HashMap<String, HashMap<String, String>> cookiesMap;
    BaseCommonTools tools;

    //程序前台运行计时器alarmPre 程序后台运行计时器alarmBack
    protected AlarmManager alarmPre,alarmBack;
    //对应前后台计时器
    protected PendingIntent pdPre,pdBack;
    /** 前后台的屏幕超时时间 */
    public int screenOutTime = -1;
    private Calendar calendar;
    private static final String LOGOUTACTION_BACK = "com.boc.bocsoft.mobile.bocmobile.buss.activity.action.back";
    private static final String LOGOUTACTION_PRE = "com.boc.bocsoft.mobile.bocmobile.buss.activity.action.pre";
    private LogoutReceiver receiver;

    private Application application;
    private Context context;
    public void init(Application application){
        this.tools = BaseCommonTools.getInstance();
        cookiesMap = tools.getCookieMap();
        HashMap<String, HashMap<String, String>> result =  converCookiesMap(cookiesMap);
        initBIIClient();

        BIIClient.instance.saveCookies(result);
        AndroidThreeTen.init(application);
        this.application = application;
        context = application.getApplicationContext();

//        // 注册超时广播
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(LOGOUTACTION_BACK);
//        filter.addAction(LOGOUTACTION_PRE);
//        receiver = new LogoutReceiver();
//        context.registerReceiver(receiver, filter);
//        //初始化后台计时闹钟
//        initTimeAlarmBack();
//        //初始化前台计时闹钟
//        initTimeAlarmPre();
//        // 前后台超时时间
//        screenOutTime = tools.getScreenTimeOut();
    }

    /**
     * 获取联龙接口对象
     * @return
     */
    public BaseCommonTools getCommonTools(){
        return tools;
    }

    /**
     * 初始化BII配置
     */
    private void initBIIClient(){
        BIIClient.config(new BiiConfigInterface(){

            @Override
            public String getBiiUrl() {
                return EloanConfig.BII_URL + EloanConfig.BII_CONTENT;
            }

            @Override
            public String getVaryficationCodeUrl() {
                return null;
            }

            @Override
            public String getMbcmUrl() {
                return "";
            }

            @Override
            public RequestParams getCommonParams() {
                return null;
            }

            @Override
            public boolean isDemo() {
                return false;
            }

            @Override
            public String getBMPSUrl() {
                return null;
            }
        });
    }

    /**
     * 初始化后台计时参数
     */
    @SuppressWarnings("static-access")
    private void initTimeAlarmBack() {

        alarmBack = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent();
        intent.setAction(LOGOUTACTION_BACK);
        pdBack = PendingIntent.getBroadcast(context, 1, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        calendar = Calendar.getInstance();
        reSetalarmBack();
    }

    /**
     * 初始化登录计时参数(前台运行)
     */
    @SuppressWarnings("static-access")
    private void initTimeAlarmPre() {
        // 注册广播
        alarmPre = (AlarmManager) context
                .getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent();
        intent.setAction(LOGOUTACTION_PRE);
        pdPre = PendingIntent.getBroadcast(context, 2, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        reSetalarmPre();
    }
    /**
     * 重设后台的闹钟 如果切换到后台调用此方法，重新计时
     */
    public void reSetalarmBack() {
        try {
            if (calendar != null) {
                calendar.clear();
                if (alarmPre != null) {
                    alarmPre.cancel(pdPre);
                }
                if (alarmBack != null) {
                    if (screenOutTime > 0) {
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.add(Calendar.SECOND, screenOutTime);
                        alarmPre.set(AlarmManager.RTC_WAKEUP,
                                calendar.getTimeInMillis(), pdBack);
                    } else {
                        alarmPre.cancel(pdPre);
                    }
                }
            } else {
                Log.e("boc", "reSetalarmBack calendar is null");
            }
        } catch (Exception e) {
            Log.e("boc", e.getMessage(), e);
        }

    }

    /**
     * 重设闹钟 如果屏幕有操作就要调用此方法，重新计时
     */
    public void reSetalarmPre() {
        try {
            if (calendar != null) {
                calendar.clear();
                if (alarmBack != null) {
                    alarmBack.cancel(pdBack);
                }
                if (alarmPre != null) {
                    if (screenOutTime > 0) {
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.add(Calendar.SECOND, screenOutTime);
                        alarmPre.set(AlarmManager.RTC_WAKEUP,
                                calendar.getTimeInMillis(), pdPre);
                    } else {
                        alarmPre.cancel(pdPre);
                    }
                }
            } else {
                Log.e("boc", "reSetalarmPre calendar is null");
            }
        } catch (Exception e) {
            Log.e("boc", e.getMessage(), e);
        }

    }

    /**
     * 停止(后台的闹钟)闹钟
     */
    public void stopAlarmBack() {
        try {
            if (alarmBack != null) {
                alarmBack.cancel(pdBack);
            }
        } catch (Exception e) {
            Log.e("boc", e.getMessage(), e);
        }
    }

    /**
     * 停止(前台的闹钟)闹钟
     */
    public void stopAlarmPre() {
        try {
            if (alarmPre != null) {
                alarmPre.cancel(pdPre);
            }
        } catch (Exception e) {
            Log.e("boc", e.getMessage(), e);
        }
    }


    /**
     * 停止计时器
     * 跳转到联龙使用
     */
    public void clearAlarm(){
        stopAlarmPre();
        stopAlarmBack();
    }
    /**
     * 屏幕超时广播，接收到广播，退出主界面，并改变弹出提示框的标识为true
     *
     * @author Administrator
     *
     */
    class LogoutReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

          //TODO 超时操作
            tools.noOperateTimeOutHandler(ActivityManager.getAppManager()
                    .currentActivity());

        }
    }

    /**
     *解除广播
     */
    public void unregisterReceiver(){

        context.unregisterReceiver(receiver);
    }

    private HashMap<String, HashMap<String, String>> converCookiesMap(HashMap<String, HashMap<String, String>> cookiesMap){
        HashMap<String, HashMap<String, String>> resultMap = new HashMap<String, HashMap<String, String>>();
        for (Map.Entry<String, HashMap<String, String>> cookieInfo : cookiesMap.entrySet()) {

            String key = "";
            if (EloanConfig.BII_URL.startsWith("http://")) {
                key = "http://" + cookieInfo.getKey();
            } else if (EloanConfig.BII_URL.startsWith("https://")) {
                key = "https://" + cookieInfo.getKey();
            } else {

            }
            resultMap.put(key, cookieInfo.getValue());
        }
        return resultMap;
    }
}
