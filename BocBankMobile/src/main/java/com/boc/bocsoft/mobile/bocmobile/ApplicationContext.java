package com.boc.bocsoft.mobile.bocmobile;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;
import com.boc.bocsoft.mobile.bii.common.client.BiiConfigInterface;
import com.boc.bocsoft.mobile.bii.common.global.BIIGlobalConst;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BaseMobileActivity;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussActivity;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussApplication;
import com.boc.bocsoft.mobile.bocmobile.base.activity.ModuleFactory;
import com.boc.bocsoft.mobile.bocmobile.base.db.BocMobileDB;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.FactorBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.User;
import com.boc.bocsoft.mobile.bocmobile.base.utils.BocCrashUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ErrorDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.AccountTypeUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.common.eventbus.BocEventBus;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Menu;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.MenuParse;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleFactoryImpl;
import com.boc.bocsoft.mobile.bocmobile.buss.login.ui.LoginBaseActivity;
import com.boc.bocsoft.mobile.bocmobile.buss.system.main.eventbus.event.GoHomeEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.system.main.eventbus.event.LogoutEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.system.main.eventbus.event.SessionOutEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.system.main.ui.MainActivity;
import com.boc.bocsoft.mobile.bocmobile.buss.system.main.ui.MainFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.BankEntity;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransPayeeListqueryForDimViewModel;
import com.boc.bocsoft.mobile.bocmobile.yun.other.YunUtils;
import com.boc.bocsoft.mobile.bocyun.HeaderParamCenter;
import com.boc.bocsoft.mobile.bocyun.common.client.YunClient;
import com.boc.bocsoft.mobile.bocyun.common.client.YunConfigInterface;
import com.boc.bocsoft.mobile.bocyun.common.model.YunHeader;
import com.boc.bocsoft.mobile.common.client.model.RequestParams;
import com.boc.bocsoft.mobile.cr.common.client.CRClient;
import com.boc.bocsoft.mobile.cr.common.client.CRConfigInterface;
import com.boc.bocsoft.mobile.cr.common.global.CRGlobalConst;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import com.boc.bocsoft.mobile.framework.utils.regex.RegexUtils;
import com.boc.bocsoft.mobile.mbcg.MBCGClient;
import com.boc.bocsoft.mobile.mbcg.activityinfo.ActivityInfoResult;
import com.boc.bocsoft.mobile.wfss.common.client.WFSSClient;
import com.boc.bocsoft.mobile.wfss.common.client.WFSSConfigInterface;
import com.chinamworld.boc.commonlib.BaseCommonTools;
import com.jakewharton.threetenabp.AndroidThreeTen;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.threeten.bp.Duration;
import org.threeten.bp.LocalDateTime;

/**
 * 国内手机银行APP
 * Created by lxw on 2016/5/21.
 */
public class ApplicationContext extends BussApplication {

    public static boolean isLogin() {
        return login;
    }

    public static void setLogin(boolean login) {
        ApplicationContext.login = login;
    }

    // 用户是否登录
    private static boolean login = false;

    private static final String LOG_TAG = ApplicationContext.class.getSimpleName();

    private static Context mContext;
    private static ApplicationContext instance;


    private volatile String active = "";
    private volatile String state = "41943040";


    private ModuleFactory moduleFactory;

    private Menu menu;

    // 当前App使用的用户
    private volatile User user;

    public static BocMobileDB mobileDB;

    private List<BankEntity> hotBankEntityList = new ArrayList<BankEntity>();
    private List<BankEntity> bankEntityList = new ArrayList<BankEntity>();
    private List<PsnTransPayeeListqueryForDimViewModel.PayeeEntity> mPayeeEntityList;

    public static Context getAppContext() {
        return mContext;
    }

    //登录成功服务器时间
    public static LocalDateTime systemDataTime;
    //后管平台活动信息数据
    public static LocalDateTime deviceDataTime;
    //中行所有账户
    public volatile List<AccountBean> chinaBankAccountList;

    //获取活动信息
    public static List<ActivityInfoResult.ActivityBean> activityInfoList;

    //程序前台运行计时器alarmPre 程序后台运行计时器alarmBack
    protected AlarmManager alarmPre, alarmBack;
    //对应前后台计时器
    protected PendingIntent pdPre, pdBack;
    /**
     * 前后台的屏幕超时时间
     */
//    public int screenOutTime = -1;
    private Calendar calendar;
    private static final String LOGOUTACTION_BACK = "com.boc.bocsoft.mobile.bocmobile.buss.activity.action.back";
    private static final String LOGOUTACTION_PRE = "com.boc.bocsoft.mobile.bocmobile.buss.activity.action.pre";
    private LogoutReceiver receiver;
    private BaseCommonTools tools;

    private boolean hasLianLong = false;//是否接入连龙代码

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = getApplicationContext();
        AndroidThreeTen.init(this);


        initRequestHeader();
        initRequestClient();

        // 初始化用户
        initUser();
        initMobileDb();
        initMenu();
        RegexUtils.configRegexFileId(R.raw.regex);
//        BaseCommonTools.getInstance().InitApplication(this);
        initErrorHandler();
      attachLianLongContext();

        //TODO 注册崩溃日志收集,开发测试阶段记录日志,生产版本去除
        BocCrashUtils.getInstance(this).register();
        registerActivityLifecycle();

    }

    public List<PsnTransPayeeListqueryForDimViewModel.PayeeEntity> getPayeeEntityList() {
        return mPayeeEntityList;
    }

    public void setPayeeEntityList(List<PsnTransPayeeListqueryForDimViewModel.PayeeEntity> mPayeeEntityList) {
        this.mPayeeEntityList = mPayeeEntityList;
    }

    //// agent: X-ANDR  终端应用标识，基于浏览器的RIA也是一种终端应用；APAD终端对应WEB渠道：APAD
    private final static String APP_USER_AGENT = "X-ANDR";
    // versio:终端应用版本，建议格式：“x.y”，其中x为大版本，y为小版本
    private static String APP_VERSION;
    // device:设备信息，用来区别终端特性，建议格式：“厂商名,产品名,型号”;如果是基于浏览器的RIA应用，则填写浏览器信息，建议格式：“厂商名,产品名,版本号”
    private final static String DEVICE = Build.MANUFACTURER + "," + Build.MODEL + "," + Build.PRODUCT;
    // platform:操作系统信息，建议格式：“厂商名,产品名,版本号”
    private final static String PLATFORM = Build.BRAND + "," + "Android" + "," + Build.VERSION.RELEASE;
    private static String PLUGINS = "";// plugins:客户端支持的安全控件或插件，逗号分隔的列表，列表项由网银定义
    private static String LOCAL = "ZH_cn";// local:国家及语言，标准格式

    private static String EXT = "";// ext: 扩展信息，可由终端应用提供商自由定义

    private static String CIPHERTYPE = "0";// 安全控件，国密（0）和非国密（1）

    private static int TIMEOUT = 1000 * 60 * 2;//120000

    private void initRequestHeader() {

        APP_VERSION = getVersion();

        BIIGlobalConst.AGENT = APP_USER_AGENT;
        BIIGlobalConst.VERSION = APP_VERSION;
        BIIGlobalConst.DEVICE = DEVICE;
        BIIGlobalConst.PLATFORM = PLATFORM;
        BIIGlobalConst.PLUGINS = PLUGINS;
        BIIGlobalConst.PAGE = "";// page: 页面标识，用来确定当前请求所在的页面及位置，格式由终端应用提供商定义
        BIIGlobalConst.LOCAL = LOCAL;
        BIIGlobalConst.EXT = EXT;
        BIIGlobalConst.CIPHERTYPE = CIPHERTYPE;
        BIIGlobalConst.TIMEOUT = TIMEOUT;

        CRGlobalConst.AGENT = APP_USER_AGENT;
        CRGlobalConst.VERSION = APP_VERSION;
        CRGlobalConst.DEVICE = DEVICE;
        CRGlobalConst.PLATFORM = PLATFORM;
        CRGlobalConst.PLUGINS = PLUGINS;
        CRGlobalConst.PAGE = "";// page: 页面标识，用来确定当前请求所在的页面及位置，格式由终端应用提供商定义
        CRGlobalConst.LOCAL = LOCAL;
        CRGlobalConst.EXT = EXT;
        CRGlobalConst.CIPHERTYPE = CIPHERTYPE;
        CRGlobalConst.TIMEOUT = TIMEOUT;

        //TODO 远程开户？？ OPURL  MAGlobalConfig
    }

    /**
     * 返回应用实例
     *
     * @return
     */
    public static ApplicationContext getInstance() {
        return instance;
    }

    public void setHasLianLong(boolean isHasLianLong) {
        this.hasLianLong = isHasLianLong;
    }

    public boolean hasLianLong() {
        return hasLianLong;
    }

    private boolean hasInitTimer = false;

    public void initTimer() {
        if (hasInitTimer) return;
        this.tools = BaseCommonTools.getInstance();
        // 注册超时广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(LOGOUTACTION_BACK);
        filter.addAction(LOGOUTACTION_PRE);
        receiver = new LogoutReceiver();
        mContext.registerReceiver(receiver, filter);
        //初始化后台计时闹钟
        initTimeAlarmBack();
        //初始化前台计时闹钟
        initTimeAlarmPre();
        // 前后台超时时间
     /*  if(tools != null){
         screenOutTime = tools.getScreenTimeOut();
       }else{
         screenOutTime = 60 * 3;
       }*/

        hasInitTimer = true;
    }

    private int getScreenOutTime() {
        if (tools != null) {
            return tools.getScreenTimeOut();
        } else {
            return 60 * 5;
        }

    }


    /**
     * 退出登录 - 清理数据
     */
    public void logout() {
        setLogin(false);
        setUser(new User());
        BIIClient.instance.clearCookies();
        if(BaseCommonTools.getInstance() != null){
            BaseCommonTools.getInstance().SetCookie(null,null);
        }
        //TODO 是否需要清除webview的cookie
        BocEventBus.getInstance().post(new LogoutEvent());
    }

    private void initRequestClient() {
        // 初始化BII配置
        initBIIClient();
        //初始化CR设置
        initCRClient();
        initYunClient();
        initMBCGClient();
        initWFSSClient();
    }

    /**
     * 初始化BII配置
     */
    private void initBIIClient() {
        BIIClient.config(new BiiConfigInterface() {

            @Override
            public String getBiiUrl() {
                return ApplicationConfig.getBiiURL();
            }

            @Override
            public String getVaryficationCodeUrl() {
                return null;
            }

            @Override
            public String getMbcmUrl() {
                return ApplicationConfig.URL_VERSION;
            }

            @Override
            public RequestParams getCommonParams() {
                return null;
            }

            @Override
            public boolean isDemo() {
                return ApplicationConfig.BII_IS_DEMO;
            }

            @Override
            public String getBMPSUrl() {
                return ApplicationConfig.BMPS_URL + ApplicationConfig.BMPS_CONTENT;
            }
        });
    }

    private void initCRClient() {
        CRClient.config(new CRConfigInterface() {

                            @Override
                            public String getUrl() {
                                return ApplicationConfig.CR_URL;//TODO
                            }

                            @Override
                            public RequestParams getCommonParams() {
                                return null;
                            }

                            @Override
                            public boolean isDemo() {
                                return ApplicationConfig.CR_IS_DEMO;
                            }
                        }
        );
    }

    private void initYunClient() {
        YunClient.config(new YunConfigInterface() {
            @Override
            public String getUrl() {
                return ApplicationConfig.YUN_URL;
            }

            @Override
            public RequestParams getCommonParams() {
                return null;
            }
        });

        YunHeader header = HeaderParamCenter.getInstance();

        header.setMsgType("0");
        header.setTrDt("");
        header.setTrTime("");
        header.setReqTraceNo("10010");
        header.setValidFlag("");
        header.setResTraceNo("");

        header.setChannelId("01");
        header.setProduct("BOCMBC_V01.1A");
        header.setVersion(getVersion());
        header.setDevice(DEVICE);
        header.setPlatform(PLATFORM);
        header.setDeviceNo(YunUtils.getDeivceId());
    }

    /**
     * 初始化WFSSClient
     */
    private void initWFSSClient() {
        WFSSClient.config(new WFSSConfigInterface() {

                              @Override
                              public String getUrl() {
                                  return ApplicationConfig.WFSS_URL + ApplicationConfig.WFSS_CONTENT;//TODO
                              }

                              @Override
                              public RequestParams getCommonParams() {
                                  return null;
                              }


                              @Override
                              public boolean isDemo() {
                                  return false;
                              }
                          }
        );
    }

    private void initMBCGClient() {
        MBCGClient.config(ApplicationConfig.MBCG_URL);
    }

    /**
     * 初始化数据库
     */
    private void initMobileDb() {
        mobileDB = new BocMobileDB(mContext);
/*
        //临时见表,防止崩溃
        new BaseDao() {
            @Override
            public synchronized void execSQL(String sql) {
                db.execSQL(BocMobileDB.CREATE_TABLE_TR_LIFE);
            }
        }.execSQL("");*/
    }

    /**
     *
     */
    private void initMenu() {
        InputStream input;
        try {
            input = instance.getAssets().open("boc_menu.xml");
            menu = MenuParse.parseXML(input);
        } catch (Exception e) {
        }
    }

    /**
     * 初始化用户
     */
    private void initUser() {
        user = new User();
    }

    /**
     * 获取当前系统用户
     *
     * @return 系统用户
     */
    public User getUser() {
        return this.user;
    }

    /**
     * 设置用户信息
     *
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    /**
     * 获取当前系统日期时间
     * ApplicationContext.getInstance().getCurrentSystemDate()
     *
     * @return LocalDateTime
     * 格式:YYYY/MM/dd hh:mm:ss
     */
    public LocalDateTime getCurrentSystemDate() {
        LocalDateTime currentSystemDateTime;
        LocalDateTime currentDeviceDateTime = LocalDateTime.now();
        if (null == systemDataTime) {
            return currentDeviceDateTime;
        }
        Duration durationDateTime = Duration.between(currentDeviceDateTime, deviceDataTime);
        currentSystemDateTime = systemDataTime.plus(durationDateTime);
        return currentSystemDateTime;
    }


    /**
     * 根据账户类型获取中行所有账户
     * ApplicationContext.getInstance().getChinaBankAccountList(List<String>accountTypeList)
     *
     * @return
     */
    public List<AccountBean> getChinaBankAccountList(List<String> accountTypeList) {
        List<AccountBean> filterAccountList = new ArrayList<AccountBean>();
        if (null != accountTypeList) {
            if (null != chinaBankAccountList) {
                for (AccountBean accountItem : chinaBankAccountList) {
                    if (accountTypeList.contains(accountItem.getAccountType())) {
                        filterAccountList.add(accountItem);
                    }
                }
            }
        } else {
            filterAccountList = chinaBankAccountList;
        }
        return filterAccountList;
    }

    /**
     * 更新中行所有账户数据
     * 如果账户信息更改，重新请求所有中行账户接口，并调用此方法更新账户数据
     * ApplicationContext.getInstance().setChinaBankAccountList(List<AccountBean> chinaBankAccountList)
     *
     * @return
     */
    public void setChinaBankAccountList(List<AccountBean> chinaBankAccountList) {
        this.chinaBankAccountList = chinaBankAccountList;

        List<AccountBean> accountBeans = new ArrayList<>();

        //获取借记卡账户
        accountBeans.addAll(getChinaBankAccountList(AccountTypeUtil.getBroType()));
        //获取信用卡账户
        accountBeans.addAll(getChinaBankAccountList(AccountTypeUtil.getCreditType()));
        //获取活期账户
        accountBeans.addAll(getChinaBankAccountList(AccountTypeUtil.getCurrentTypeWithOutBro()));
        //获取定期账户
        accountBeans.addAll(getChinaBankAccountList(AccountTypeUtil.getRegularType()));
        //获取电子现金账户
        accountBeans.addAll(getChinaBankAccountList(AccountTypeUtil.getFinanceType()));
        //获取虚拟卡账户
        accountBeans.addAll(getChinaBankAccountList(AccountTypeUtil.getVirtualType()));
        //获取网上理财专属账户
        accountBeans.addAll(getChinaBankAccountList(AccountTypeUtil.getMoneyType()));

        this.chinaBankAccountList = accountBeans;
    }


    /**
     * 初始化错误处理
     */
    public void initErrorHandler(){
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                ActivityManager.getAppManager().AppExit(ApplicationContext.this);

            }
        });
    }

    @Override
    public ModuleFactory getModuleFactory() {
        if (moduleFactory == null) {
            moduleFactory = new ModuleFactoryImpl();
        }
        return moduleFactory;
    }

    /**
     * 返回菜单对象
     *
     * @return
     */
    public Menu getMenu() {
        return menu;
    }

    public List<BankEntity> getHotBankEntityList() {
        return hotBankEntityList;
    }

    public void setHotBankEntityList(List<BankEntity> hotBankEntityList) {
        this.hotBankEntityList.clear();
        this.hotBankEntityList.addAll(hotBankEntityList);
    }

    public List<BankEntity> getBankEntityList() {
        return bankEntityList;
    }

    public void setBankEntityList(List<BankEntity> bankEntityList) {
        this.bankEntityList.clear();
        this.bankEntityList.addAll(bankEntityList);
    }

    public String getVersion() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "3.0.0";

    }

    /**
     * 初始化后台计时参数
     */
    @SuppressWarnings("static-access")
    private void initTimeAlarmBack() {

        alarmBack = (AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE);
        Intent intent = new Intent();
        intent.setAction(LOGOUTACTION_BACK);
        pdBack = PendingIntent.getBroadcast(mContext, 1, intent,
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
        alarmPre = (AlarmManager) mContext
                .getSystemService(mContext.ALARM_SERVICE);
        Intent intent = new Intent();
        intent.setAction(LOGOUTACTION_PRE);
        pdPre = PendingIntent.getBroadcast(mContext, 2, intent,
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
                    if (getScreenOutTime() > 0) {
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.add(Calendar.SECOND, getScreenOutTime());
                        alarmBack.set(AlarmManager.RTC_WAKEUP,
                                calendar.getTimeInMillis(), pdBack);
                    } else {
                        alarmBack.cancel(pdPre);
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
                    if (getScreenOutTime() > 0) {
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.add(Calendar.SECOND, getScreenOutTime());
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
    public void clearAlarm() {
        stopAlarmPre();
        stopAlarmBack();
    }

    /**
     * 屏幕超时广播，接收到广播，退出主界面，并改变弹出提示框的标识为true
     *
     * @author Administrator
     */
    class LogoutReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
        	//判断是否已经登录
        	if(isLogin()){
        		 Activity currentActivy = (Activity) ActivityManager.getAppManager().currentActivity();
            if (currentActivy instanceof BussActivity) {
                ((BussActivity) currentActivy).closeProgressDialog();
            }
            final ErrorDialog timeOutDialog = new ErrorDialog(currentActivy);
            timeOutDialog.setCancelable(false);

            timeOutDialog.setOnBottomViewClickListener(new ErrorDialog.OnBottomViewClickListener() {

                @Override
                public void onBottomViewClick() {
                    timeOutDialog.dismiss();
                    BocEventBus.getInstance().post(new SessionOutEvent());
                    //onSessionTimeout();
                }
            });
            timeOutDialog.setErrorData("您在设定的退出时间(" + (getScreenOutTime() / 60) + "分钟)都没有操作手机" +
                    "银行，已经被系统强制退出，如需操作请重新登录"
            );
            timeOutDialog.setBtnText("确定");
            timeOutDialog.show();
        		}


        }
    }

    /**
     * session超期时处理
     */
    protected void onSessionTimeout() {
        logout();
        Intent intent = new Intent();
        intent.setClass(mContext, LoginBaseActivity.class);
        //ModuleActivityDispatcher.popToHomePage();
        Activity activity = ActivityManager.getAppManager().currentActivity();
        try {

            if (activity != null) {
                startActivity(intent);
            } else {
                startActivity(intent);
            }
        } catch (Exception e) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    /**
     * 解除广播
     */
    public void unregisterReceiver() {

        mContext.unregisterReceiver(receiver);
    }


    /**
     * 获取活动信息数据
     *
     * @return
     */
    public List<ActivityInfoResult.ActivityBean> getActivityInfoList() {
        return activityInfoList;
    }

    /**
     * 设置活动信息数据
     *
     * @param activityInfoList
     */
    public void setActivityInfoList(List<ActivityInfoResult.ActivityBean> activityInfoList) {
        ApplicationContext.activityInfoList = activityInfoList;
    }

    private void attachLianLongContext(){
        try {
            Class<?> aClass = Class.forName("com.chinamworld.bocmbci.base.application.BaseDroidApp");
          Constructor<?> constructor = aClass.getConstructor(Context.class);
          if(constructor == null)return;
          constructor.newInstance(this.getApplicationContext());
        } catch (Exception e) {
        }
    }


    /**
     * activity 管理生命周期
     */
    private void registerActivityLifecycle(){

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                // 关闭后台的闹钟
                ApplicationContext.getInstance().stopAlarmBack();

                if(isOurActivity(activity)){
                    // 开启前台的闹钟
                    ApplicationContext.getInstance().reSetalarmPre();
                }

            }

            @Override
            public void onActivityPaused(Activity activity) {

                if(isOurActivity(activity)){
                    //                开启后台的闹钟
                    ApplicationContext.getInstance().reSetalarmBack();
                }
                // 关闭前台的闹钟
                ApplicationContext.getInstance().stopAlarmPre();

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    private boolean isOurActivity(Activity activity){
        //CannotReceivePage
        if(activity instanceof BaseMobileActivity){
            return true;
        }
        return  false;

    }

}
