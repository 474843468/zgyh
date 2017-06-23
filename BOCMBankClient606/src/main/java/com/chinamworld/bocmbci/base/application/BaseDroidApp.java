/**
SystemConfig * 文件名	：BaseDroidApp.java
 * 创建日期	：2013-03-19
 * Copyright (c) 2003-2012 北京联龙博通

 * All rights reserved.
 */
package com.chinamworld.bocmbci.base.application;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.abstracttools.AbstractLoginTool;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.receiver.AudioReceiver;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.MainActivity;
import com.chinamworld.bocmbci.biz.audio.AudioKeyManager;
import com.chinamworld.bocmbci.biz.invest.adapter.SecurityAdapter;
import com.chinamworld.bocmbci.biz.loan.LoanData;
import com.chinamworld.bocmbci.biz.login.observer.LoginObserver;
import com.chinamworld.bocmbci.biz.plps.order.CharacterParser;
import com.chinamworld.bocmbci.biz.push.PushManager;
import com.chinamworld.bocmbci.biz.setting.hardwarebinding.HardwareBindingActivity;
import com.chinamworld.bocmbci.biz.setting.safetytools.SafetyToolsActivity;
import com.chinamworld.bocmbci.commonlibtools.CommonLibManager;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.database.DBHelper;
import com.chinamworld.bocmbci.exception.CrashHandler;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpObserver;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiPollingEngine;
import com.chinamworld.bocmbci.http.engine.CommonHttpEngine;
import com.chinamworld.bocmbci.http.engine.FileHttpEngine;
import com.chinamworld.bocmbci.http.engine.ImageFileHttpEngine;
import com.chinamworld.bocmbci.http.engine.PollingCommonHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.log.LogManager;
import com.chinamworld.bocmbci.utils.DeviceInfoTools;
import com.chinamworld.bocmbci.utils.DeviceUtils;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.SharedPreUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utiltools.UtilsToolsManager;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.bocmbci.widget.adapter.SecurityFactorAdapter;
import com.chinamworld.bocmbci.widget.entity.ImageTextAndAct;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:Application
 * <p />
 * 
 * @version 1.00
 * @author wez
 * @date 2013-03-19 10:06:25
 * 
 */
public class BaseDroidApp extends BaseApplication implements HttpObserver {

	public BaseDroidApp(){onCreate(context);
	}
	public BaseDroidApp(Context context){
		onCreate(context);
	}
	public static List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();

	private static final String TAG = "BaseDroidApp";

	protected static BaseDroidApp instanse = null;// 单例

//	protected static boolean isLogin = false; // 用户登录状态

//	private BaseActivity currentAct = null; // 当前act
	private String currentClass;

//	/** 用户网银用户号 */
//	private String operatorId;
//
//	public String getOperatorId() {
//		return operatorId;
//	}
//
//	public void setOperatorId(String operatorId) {
//		this.operatorId = operatorId;
//	}

	public String getHasBindingDevice() {
		return hasBindingDevice;
	}

	public void setHasBindingDevice(String hasBindingDevice) {
		this.hasBindingDevice = hasBindingDevice;
	}

	/** 用户是否绑定设备 */
	private String hasBindingDevice;

	/**
	 * 是否是Dialog的Activity
	 */
//	private boolean isDialogAct = false;
	/** 登录信息 */
	private String loginInfo;

	/** 用于Bii通信 */
	private BiiHttpEngine biiHttpEngine = null;// 发送Bii通信的请求

	/** 用于Bii轮训通信 */
	private BiiPollingEngine biiPollingHttpEngine = null;// 发送Bii通信的请求

	/** 用于普通通信 */
	private CommonHttpEngine commonHttpEngine = null;// 发送普通通信的请求

	/** 用于轮寻通信 */
	private PollingCommonHttpEngine pollingCommhttpEngine = null;// 发送轮寻的请求

	/** 下载图片转换为bitmap */
	private ImageFileHttpEngine imageFileHttpEngine = null;// 发送下载图片的请求

	/** 下载普通文件请求 */
	private FileHttpEngine fileHttpEngine = null;// 发送下载文件的请求

//	private HashMap<String, Object> appDataMap; // 应用级别的全局数据map

	private String imei = "";// 设备的imei

	public static boolean isExit = false;

	/** 观察用户是否登录 */
//	private LoginObserver loginObserver;

	/** 统计请求标识 */
	private boolean sflag = false;

	public boolean getSflag() {
		return sflag;
	}

	public void setSflag(boolean sflag) {
		this.sflag = sflag;
	}

	/** 市场细分 1：理财 10：查询 66：VIP */
	private String segmentInfo;

	public String getSegmentInfo() {
		return segmentInfo;
	}

	public void setSegmentInfo(String segmentInfo) {
		this.segmentInfo = segmentInfo;
	}

	/** 电子卡信息 */
	private Map<String, Object> ecardMap;
	
	public Map<String, Object> getEcardMap() {
		return ecardMap;
	}

	public void setEcardMap(Map<String, Object> ecardMap) {
		this.ecardMap = ecardMap;
	}

	private boolean isTransNoSafetyCombinMessage = false;

	/**
	 * 是否翻译SafetyCombin.noSafetyCombins错误码提示信息
	 * 
	 * @return
	 */
	public boolean getIsTransNoSafetyCombinMessage() {
		return isTransNoSafetyCombinMessage;
	}

	/**
	 * 设置翻译SafetyCombin.noSafetyCombins错误码提示信息
	 * 
	 * @param flag
	 */
	public void setIsTransNoSafetyCombinMessage(boolean flag) {
		isTransNoSafetyCombinMessage = flag;
	}

	/** 没有安全工具抛出的错误信息 */

	private String noSafetyCombinsMsg;

	public String getNoSafetyCombinsMsg() {
		return noSafetyCombinsMsg;
	}

	public void setNoSafetyCombinsMsg(String noSafetyCombinsMsg) {
		this.noSafetyCombinsMsg = noSafetyCombinsMsg;
	}

	/**
	 * 双击退出的消息处理
	 */
	public Handler mHandlerExit = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};

	/**
	 * 首页九宫格是否自动跳转（登录成功后跳转到九宫格首页需要判断）
	 */
//	private boolean isMainItemAutoClick = false;

//	public boolean isMainItemAutoClick() {
//		return isMainItemAutoClick;
//	}
//
//	public void setMainItemAutoClick(boolean isMainItemAutoClick) {
//		this.isMainItemAutoClick = isMainItemAutoClick;
//	}

	private Notification notification = null;

//	protected DBHelper dbHelper; // 数据库操作公共组件

	private SharedPreferences sharedPrefrences;

	/** 消息提示框 footChooseDialog 为底部的选择弹出框 */
	private CustomDialog messageDialog, securityDialog, footChooseDialog,
			messageDialogFore;

	private CustomDialog errorDialog, timeOutDialog;// 错误提示框

	private CustomDialog checkboxDialog;
	
	protected AlarmManager alarm, alarmPre;
	protected PendingIntent pd, pdPre;
	/** 前台的屏幕超时时间 */
//	public int screenOutTime = -1;
	private Calendar calendar;
	private LogoutReceiver receiver;
	private static final String LOGOUTACTION_BACK = "com.chinamworld.bocmbci.base.activity.action.back";
	private static final String LOGOUTACTION_PRE = "com.chinamworld.bocmbci.base.activity.action.pre";

	protected String dateTime;
	/** 安全因子组合的名字 */
//	protected ArrayList<String> securityNameList;
	/** 安全因子组合的id */
//	protected ArrayList<String> securityIdList;
	/** 用户所选安全因子 */
//	protected String securityChoosed;

//	private Activity actFirst, actSecond, actThree, actFor, actfive;

//	private ImageAndText fastItemCHoosed;
	/** 默认的安全因子Id */
//	private String defaultCombinId = "-1";
	/** 最新消息列表数据 */
	private List<Map<String, String>> new_infoList;
	/**
	 * 九宫格首页
	 */
	private MainActivity mainAct;

	// P501音频key
//	private AudioReceiver mAudioReceiver;
//	private AudioManager mAudioManager;
//	public AudioKeyManager mAudioKeyManager;
	/**
	 * 是否取消音频Key交易
	 */
//	private boolean isCancelAudioKey = false;

//	public AudioManager getAudioManager() {
//		return mAudioManager;
//	}
//
//	public boolean getIsCancelAudioKey() {
//		return isCancelAudioKey;
//	}
//
//	public void setIsCancelAudioKey(boolean isCancel) {
//		isCancelAudioKey = isCancel;
//	}
//
//	public AudioReceiver getAudioReceiver() {
//		return mAudioReceiver;
//	}
//
	public MainActivity getMainAct() {
		return mainAct;
	}

	public void setMainAct(MainActivity mainAct) {
		this.mainAct = mainAct;
	}
//
//	public List<Map<String, String>> getNew_infoList() {
//		return new_infoList;
//	}

	public void setNew_infoList(List<Map<String, String>> new_infoList) {
		this.new_infoList = new_infoList;
	}

	/**
	 * 底部添加快捷方式的弹出框
	 */
	private PopupWindow fastfootPopuwindow;

	public PopupWindow getFastfootPopuwindow() {
		return fastfootPopuwindow;
	}

	public void setFastfootPopuwindow(PopupWindow fastfootPopuwindow) {
		this.fastfootPopuwindow = fastfootPopuwindow;
	}

//	public boolean isDialogAct() {
//		return isDialogAct;
//	}
//
//	public void setDialogAct(boolean isDialogAct) {
//		this.isDialogAct = isDialogAct;
//	}

//	public void setCurrentClass(String currentClass) {
//		this.currentClass = currentClass;
//	}
//
//	public String getCurrentClass() {
//		return currentClass;
//	}
//
//	public String getSecurityChoosed() {
//		return securityChoosed;
//	}
//
//	public String getDefaultCombinId() {
//		return defaultCombinId;
//	}
//
//	public void setDefaultCombinId(String defaultCombinId) {
//		this.defaultCombinId = defaultCombinId;
//	}
//
//	public void setSecurityChoosed(String securityChoosed) {
//		this.securityChoosed = securityChoosed;
//	}
//
//	public ArrayList<String> getSecurityNameList() {
//		return securityNameList;
//	}
//
//	public void setSecurityNameList(ArrayList<String> securityNameList) {
//		this.securityNameList = securityNameList;
//	}
//
//	public ArrayList<String> getSecurityIdList() {
//		return securityIdList;
//	}
//
//	public void setSecurityIdList(ArrayList<String> securityIdList) {
//		this.securityIdList = securityIdList;
//	}

	// @Override
	public void onCreate(Context con) {
		// super.onCreate();
		context = con;
		instanse = this;
		instance=this;
		new UtilsToolsManager();
		CommonLibManager.InitCommonLib();
		this.init();

		CrashHandler.getInstance().init();
		// 前台的屏幕超时时间
		screenOutTime = Integer.valueOf(SharedPreUtils.getInstance().getString(
				ConstantGloble.SCREEN_TIME_OUT, "300"));  // 606 ,将默认时间改为5分钟。前后台时间都可以由后台设置

		initTimeAlarm();

		initTimeAlarmPre();
		// 初始化HttpEngines
		initHttpEngines();

		// // 预分配堆内存
		// VMRuntimeHack.preallocateHeap(PRE_ALL_LOCATE_HEAP_SIZE);

		// 初始化appDataMap
		initAppDataMap();

		// 初始化登录观察者
		loginObserver = new LoginObserver();

		// 初始化Notification
		notification = new Notification();

		// 初始化DBHelper
		dbHelper = new DBHelper(context);
		// 检测所有的数据实体模型是否已经建表，没有表则进行创建 add by wez 2012.10.24
		dbHelper.createTableIfNotExists();

		sharedPrefrences = context.getSharedPreferences(
				ConstantGloble.CONFIG_FILE, context.MODE_PRIVATE);
		// 初始化LogManager
		LogManager.init(context);

		// 屏幕宽高
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		if (dm.widthPixels <= dm.heightPixels) {
			LayoutValue.SCREEN_WIDTH = dm.widthPixels;
			LayoutValue.SCREEN_HEIGHT = dm.heightPixels;
		} else {
			LayoutValue.SCREEN_WIDTH = dm.heightPixels;
			LayoutValue.SCREEN_HEIGHT = dm.widthPixels;
		}
		// 密度
		LayoutValue.SCREEN_DENSITY = dm.density;

		try {
			BaseHttpEngine.initProxy(context);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogGloble.exceptionPrint(e);
		}

		// P402 启动消息推送
		PushManager.getInstance(context).restartPushService();

		// P501 音频KEY
		mAudioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		// AudioKeyManager.getInstance();
		AudioKeyManager.getInstance().initKeyDriver(context, null, null);
		// initAudioReceiver();

		Thread pinYinThread = new Thread(new Runnable() {

			@Override
			public void run() {
				CharacterParser.initChinesePinYin(context);
			}
		});
		pinYinThread.start();
	
	}

	/**
	 * 注册广播接收器
	 */
	public void initAudioReceiver() {
		mAudioReceiver = new AudioReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_HEADSET_PLUG);
		context.registerReceiver(mAudioReceiver, filter);
	}

	/**
	 * 初始化登录计时参数
	 */
	@SuppressWarnings("static-access")
	private void initTimeAlarm() {
		LogGloble.d("BaseDroidApp","注册登录超时广播");
		receiver = new LogoutReceiver();
		// 注册广播
		IntentFilter filter = new IntentFilter();
		filter.addAction(LOGOUTACTION_BACK);
		filter.addAction(LOGOUTACTION_PRE);
		context.registerReceiver(receiver, filter);
		alarm = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
		Intent intent = new Intent();
		intent.setAction(LOGOUTACTION_BACK);
		pd = PendingIntent.getBroadcast(context, 131, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		calendar = Calendar.getInstance();
		reSetalarm();
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
		pdPre = PendingIntent.getBroadcast(context, 130, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		reSetalarmPre();
	}

	// @Override
	public void onTerminate() {
		dbHelper.close();
		// super.onTerminate();
	}

	/**
	 * 初始化appDataMap
	 */
	private void initAppDataMap() {
		appDataMap = new HashMap<String, Object>();

		// 全局轮询数据Map
		appDataMap.put(ConstantGloble.APP_POLLING_DATA_MAP,
				new HashMap<String, Object>());

		// 全局业务数据Map
		appDataMap.put(ConstantGloble.APP_BIZ_DATA_MAP,
				new HashMap<String, Object>());
	}

	/**
	 * 初始化HttpEngines
	 */
	private void initHttpEngines() {
		biiHttpEngine = new BiiHttpEngine();

		pollingCommhttpEngine = new PollingCommonHttpEngine();

		commonHttpEngine = new CommonHttpEngine();

		imageFileHttpEngine = new ImageFileHttpEngine();

		fileHttpEngine = new FileHttpEngine(null);
		biiPollingHttpEngine = new BiiPollingEngine();

	}

	public BiiPollingEngine getBiiPollingHttpEngine() {
		return biiPollingHttpEngine;
	}

	public void setBiiPollingHttpEngine(BiiPollingEngine biiPollingHttpEngine) {
		this.biiPollingHttpEngine = biiPollingHttpEngine;
	}

//	/**
//	 * 判断当前用户是否已经登录
//	 * 
//	 * @return
//	 */
//	public boolean isLogin() {
//		return isLogin;
//	}
//
//	/**
//	 * 设置用户登录状态
//	 * 
//	 * @param isLogin
//	 */
//	public static void setLogin(boolean isLogin) {
////		BaseDroidApp.isLogin = isLogin;
//		CommonApplication.isLogin=isLogin;
//		
//	}

	/**
	 * @param cookieKey
	 *            ,避免cookies覆盖
	 * 
	 * @return cookie
	 */
	public String getCookie(String cookieKey) {
		// 从配置文件中读取cookie
		String cookie = new SharedPreUtils(
				ConstantGloble.CLIENT_COOKIES_FILE_NAME, context)
				.getStringCookie(ConstantGloble.CLIENT_COOKIES_PREFIX
						+ cookieKey, null);
		if (StringUtil.isNullOrEmpty(cookie)) {
			LogGloble.w(TAG, "cookie is null!");
		}
		return cookie;
	}

	/**
	 * 
	 * @param cookie
	 *            服务器Set-Cookie字段
	 * @param cookieKey
	 *            区分bii和其他系统的host,避免cookies覆盖
	 */
	public void setCookie(String cookie, String cookieKey) {
		LogGloble.w(TAG, cookieKey + ": cookie changed from "
				+ getCookie(cookieKey) + " to " + cookie);
		String msg = "cookie changed from " + getCookie(cookieKey) + " to "
				+ cookie;
		// showToast(msg);
		new SharedPreUtils(ConstantGloble.CLIENT_COOKIES_FILE_NAME, context)
				.addOrModifyCookie(ConstantGloble.CLIENT_COOKIES_PREFIX
						+ cookieKey, cookie);
	}

	public static HashMap<String, HashMap<String, String>> cookieMap = new HashMap<String, HashMap<String, String>>();

	
	
	public Map<String, String> getCookieMap(String host) {

		if (cookieMap.containsKey(host) == false) {
			HashMap<String, String> map = new HashMap<String, String>();
			cookieMap.put(host, map);
			return map;
		}
		return cookieMap.get(host);
	}

	public void ClearCookie() {
		if(BaseHttpEngine.sClient != null)
			BaseHttpEngine.sClient.getCookieStore().clear();
		cookieMap.clear();
	}

	public void SetCookie(String cookie, String host) {
		String cookieStr = cookie;
		int nIndex = cookieStr.indexOf("=");
		String key = cookieStr.substring(0, nIndex);
		String path = "";
		nIndex = cookieStr.indexOf("Path=/");
		if (nIndex >= 0) {
			String tmp = cookieStr.substring(nIndex + 6);
			nIndex = tmp.indexOf(";");
			if (nIndex >= 0)
				path = tmp.substring(0, nIndex);
		}

		HashMap<String, String> map = null;
		if (cookieMap.containsKey(host) == false) {
			map = new HashMap<String, String>();
			cookieMap.put(host, map);
		} else {
			map = cookieMap.get(host);
		}
		map.put(key + "+" + path, cookie);
	}

	public String getAllCookie(String host) {
		Map<String, String> cookieMap = BaseDroidApp.getInstanse()
				.getCookieMap(host);
		String cookie = "";
		for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
			String key = entry.getKey();
			cookie += "," + cookieMap.get(key);
		}
		if (cookie.length() > 1)
			cookie = cookie.substring(1);
		return cookie;
	}

	// public void showToast(final String msg) {
	//
	// new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	// Looper.prepare();
	// Toast.makeText(BaseDroidApp.getInstanse().getApplicationContext(), msg,
	// Toast.LENGTH_LONG)
	// .show();
	// Looper.loop();
	// }
	// }).start();
	//
	// }
	/**
	 * 
	 * 删除所有cookie
	 */
	public void deleteCookie() {
		LogGloble.w(TAG, "delete cookie ");
		// TODO 需要更改
		ClearCookie();
		new SharedPreUtils(ConstantGloble.CLIENT_COOKIES_FILE_NAME, context)
				.deleteAll(ConstantGloble.CLIENT_COOKIES_FILE_NAME);
	}

	/**
	 * 客户端退出（清除cookie和用户登录状态）
	 * 
	 * 删除cookie
	 * 
	 */
	public void clientLogOut() {
		/*
		 * 501音频key(中银E盾) 解除广播注册
		 */
		AudioKeyManager.getInstance().init();
		if (mAudioReceiver != null) {
			try {
				context.unregisterReceiver(mAudioReceiver);
			} catch (Exception e) {
				LogGloble.w(TAG, "AudioReceiver has not been registered!");
			}
		}
		// 删除cookie
		deleteCookie();
		// 设置用户登录状态
		setLogin(false);

	}

	/**
	 * 获取imei 并保存
	 * 
	 * @return imei
	 */
	public String getImei() {
		if (StringUtil.isNullOrEmpty(imei)) {
			imei = DeviceUtils.getIMEI(context);
		}
		return imei;

	}

	/**
	 * http请求callback之前的前拦截器(无http error code异常)</p>
	 * 
	 * @param resultObj
	 * 
	 * @return 是否终止当前的回调进度 </p> true:终止进度 </p> false：继续进度 </p>
	 * 
	 */
	public boolean httpRequestCallBackPre(Object resultObj) {

		if (resultObj instanceof BiiResponse) {
			// Bii请求后拦截

		} else if (resultObj instanceof String) {

		} else if (resultObj instanceof Map) {

		} else if (resultObj instanceof Bitmap) {

		} else if (resultObj instanceof File) {

		} else {
			// do nothing
		}

		return false;
	}

	/**
	 * http请求callback之前的前拦截器(无http error code异常)</p>
	 * 
	 * 主要用于拦截服务端返回的自定义异常error_message</p>
	 * 
	 * @param resultObj
	 * 
	 * @return 是否终止当前的回调进度 </p> true:终止进度 </p> false：继续进度
	 * 
	 *         add by wez 2012.10.26
	 */
	public boolean httpRequestCallBackAfter(Object resultObj) {
		if (resultObj instanceof BiiResponse) {
			// Bii请求后拦截

		} else if (resultObj instanceof String) {

		} else if (resultObj instanceof Map) {

		} else if (resultObj instanceof Bitmap) {

		} else if (resultObj instanceof File) {

		} else {
			// do nothing
		}

		return false;
	}

	/**
	 * http code error callback 前拦截器</p>
	 * 
	 * @param code
	 * 
	 * @return 是否终止当前的回调进度 </p> true:终止进度 </p> false：继续进度 </p>
	 * 
	 */
	public boolean httpCodeErrorCallBackPre(String code) {
		return false;
	}

	/**
	 * http code error callback 后拦截器</p>
	 * 
	 * 主要用于拦截服务端返回的自定义异常error_message</p>
	 * 
	 * @param code
	 * 
	 * @return 是否终止当前的回调进度 true:终止进度 false：继续进度
	 * 
	 */
	public boolean httpCodeErrorCallBackAfter(String code) {
		return false;
	}

	@Override
	public void commonHttpErrorCallBack(String code) {
	}

	/**
	 * 创建消息提示框
	 * 
	 * @param message
	 *            提示信息
	 * @param btn1Text
	 *            按钮名字
	 * @param btn2Text
	 *            按钮名字
	 * @param onclicklistener
	 *            按钮监听
	 */
	public void showErrorDialog(String message, int btn1Text, int btn2Text,
			OnClickListener onclicklistener) {
		// 防止同时弹出多个错误提示框，引用覆盖的问题。 没弹出一个，dismiss掉该引用之前指向的对象
		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		errorDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = errorDialog.initMentionDialogView(null, message, btn1Text,
				btn2Text, onclicklistener);
		showDialog(v);
	}

	/**
	 * 2个红色按钮 创建消息提示框
	 * 
	 * @param message
	 *            提示信息
	 * @param btn1Text
	 *            按钮名字
	 * @param btn2Text
	 *            按钮名字
	 * @param onclicklistener
	 *            按钮监听
	 */
	public void showError2RedDialog(String message, int btn1Text, int btn2Text,
			OnClickListener onclicklistener) {
		// 防止同时弹出多个错误提示框，引用覆盖的问题。 没弹出一个，dismiss掉该引用之前指向的对象
		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		errorDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = errorDialog.init2RedMentionDialogView(null, message, btn1Text,
				btn2Text, onclicklistener);
		showDialog(v);
	}
	/**
	 * 积利金信息提示框
	 * 
	 * @param message
	 *            提示信息
	 * @param onclick
	 *            点击事件监听
	 */
	public void showGoldBounsDialog(String message, OnClickListener onclick) {
		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		errorDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = LayoutInflater.from(currentAct).inflate(
				R.layout.comm_message_dialog_goldbouns, null);
		TextView tvMentionMsg = (TextView) v.findViewById(R.id.tv_metion_msg);
		tvMentionMsg.setText(message);
		Button cancleBtn = (Button) v.findViewById(R.id.retry_btn);
		cancleBtn.setOnClickListener(onclick);
		cancleBtn.setText("确定");
		showDialog(v);
	}

	/**
	 * 中银证券信息提示框
	 * 
	 * @param message
	 *            提示信息
	 * @param onclick
	 *            点击事件监听
	 */
	public void showZhongyinDialog(String message, OnClickListener onclick) {
		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		errorDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = LayoutInflater.from(currentAct).inflate(
				R.layout.comm_message_dialog_zhongyin, null);
		Button retryBtn = (Button) v.findViewById(R.id.exit_btn);
		retryBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				errorDialog.dismiss();

			}
		});
		retryBtn.setText("取消");
		TextView tvMentionMsg = (TextView) v.findViewById(R.id.tv_metion_msg);
		tvMentionMsg.setText(message);
		Button cancleBtn = (Button) v.findViewById(R.id.retry_btn);
		cancleBtn.setOnClickListener(onclick);
		cancleBtn.setText("接受");
		showDialog(v);
	}

	/**
	 * 创建消息提示dialog
	 * 
	 * @param errorCode
	 *            错误码
	 * @param messageResId
	 *            错误信息 资源ID
	 */
	public void createDialog(final String errorCode, int messageResId) {
		String str = context.getResources().getString(messageResId);
		createDialog(errorCode, str);
	}

	/**
	 * 创建消息提示dialog
	 * 
	 * @param errorCode
	 *            错误码
	 * @param messageResId
	 *            错误信息 资源ID
	 * 
	 * @param onclick
	 *            监听器
	 */
	public void createDialog(final String errorCode, int messageResId,
			OnClickListener onclick) {
		String str = context.getResources().getString(messageResId);
		createDialog(errorCode, str, onclick);
	}

	/**
	 * 创建消息提示dialog
	 * 
	 * @param errorCode
	 *            错误码
	 * @param message
	 *            错误信息
	 */
	// add by wjp 2012.10.28
	public void createDialog(final String errorCode, String message) {
		createDialog(errorCode, message, globalOnclickListener);// mod by wez
		// 2012.11.02 去掉重复代码
	}

	/**
	 * 创建消息提示dialog
	 * 
	 * @param errorCode
	 *            错误码
	 * @param message
	 *            错误信息
	 * @param onclick
	 *            监听器
	 */
	public void createDialog(String errorCode, String message,
			OnClickListener onclick) {
		String msgContent = null;
		// 2014.12.11 做了判断
		if ("BANCS.1756".equals(errorCode)) {
			msgContent = LoanData.loanTypeDataLimitError.get(errorCode);
		} else {			
//			if(StringUtil.isNullOrEmpty(errorCode)==false&&errorCode.indexOf(".")>=0){
//				if(LocalData.ErrorCodeList.contains(errorCode.substring(0,errorCode.indexOf(".")))){
//					msgContent = errorCode+"："+message;
//				}else{
//					msgContent = message;
//				}
//			}else{
//				if(LocalData.ErrorCodeList.contains(errorCode)){
//					msgContent = errorCode+"："+message;
//				}else{
//					msgContent = message;
//				}
//			}
			msgContent = message;

		}
		// added by nl. 防止同时弹出多个错误提示框，引用覆盖的问题。 每弹出一个，dismiss掉该引用之前指向的对象
		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		errorDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		errorDialog.setContentView(errorDialog.initMsgDialogView(errorCode,
				msgContent, onclick));

		WindowManager.LayoutParams lp = errorDialog.getWindow().getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH * 6 / 7;
		lp.height = LayoutValue.SCREEN_HEIGHT * 6 / 7;
		errorDialog.getWindow().setAttributes(lp);
		errorDialog.setCancelable(false);
		errorDialog.show();
		errorDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	/**
	 * 创建消息提示dialog
	 *
	 * @param message
	 *            错误信息
	 * @param onclick
	 *            监听器
	 */
	public void createSmsDialog(String message, OnClickListener onclick) {
		// added by nl. 防止同时弹出多个错误提示框，引用覆盖的问题。 每弹出一个，dismiss掉该引用之前指向的对象
		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		errorDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		// errorDialog.setContentView(errorDialog.initMsgDialogView("", message,
		// onclick));
		errorDialog.setContentView(errorDialog.initMentionDialogView("",
				message, R.string.cancle, R.string.send, onclick));

		WindowManager.LayoutParams lp = errorDialog.getWindow().getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH * 6 / 7;
		lp.height = LayoutValue.SCREEN_WIDTH / 2;
		errorDialog.getWindow().setAttributes(lp);
		errorDialog.show();
		errorDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	/**
	 * 关闭自定义消息展示框 这个框 是自定义的消息展示框
	 */
	public void dismissMessageDialog() {
		if (messageDialog != null && messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		
		if(checkboxDialog!= null && checkboxDialog.isShowing()){
			checkboxDialog.dismiss();
		}
	}

	/**
	 * 关闭自定义消息展示框 这个框 是自定义的消息展示框 仅限于关闭showForexMessageDialog
	 */
	public void dismissMessageDialogFore() {
		if (messageDialogFore != null && messageDialogFore.isShowing()) {
			messageDialogFore.dismiss();
		}

	}

	/**
	 * 关闭提示信息框 这个框 只是系统样式那种 确定 取消 一行提示样式
	 */
	public void dismissErrorDialog() {
		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		// if (messageDialog != null && messageDialog.isShowing()) {
		// messageDialog.dismiss();
		// }
	}

	/**
	 * 公共错误弹出框
	 * 
	 * @param code
	 * @param message
	 */
	public void showErrorDialog(String code, String message) {
		// 防止同时弹出多个错误提示框，引用覆盖的问题。 没弹出一个，dismiss掉该引用之前指向的对象
		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		errorDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = errorDialog.initMentionDialogView(code + "", message,
				R.string.exit, R.string.confirm, globalOnclickListener);
		showDialog(v);
	}

	/**
	 * 显示信息提示框
	 * 
	 * @param code
	 *            错误码
	 * @param message
	 *            提示信息
	 * @param onclick
	 *            点击事件监听
	 */
	public void showErrorDialog(String code, String message,
			OnClickListener onclick) {
		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		errorDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = errorDialog.initMentionDialogView(code + "", message,
				R.string.button_negative, R.string.button_positive, onclick);
		showDialog(v);
	}

	/**
	 * 贷款账户详情信息展示
	 * 
	 * @param v
	 *            贷款账户详情视图
	 */
	public void showLoanMessageDialog(View v) {
		if (messageDialog != null && messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		messageDialog.setCancelable(false);
		showDialogFill(v, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	/**
	 * 贷款申请地址选择信息展示
	 * 
	 * @param v
	 *            贷款申请视图
	 */
	public void showLoanApplySelectDialog(View v) {
		if (messageDialog != null && messageDialog.isShowing()) {
			showDialogRefresh(v, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		} else {
			LogGloble.i("info", "aaaaaa");
			messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
			messageDialog.setCancelable(false);
			showDialogFill(v, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}
	}

	public void showDialogRefresh(View view, int width, int height) {
		messageDialog.setContentView(view);
		WindowManager.LayoutParams lp = messageDialog.getWindow()
				.getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH;
		lp.height = LayoutValue.SCREEN_HEIGHT * 7 / 8;
		messageDialog.getWindow().setAttributes(lp);
		messageDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	/**
	 * 账户详情展示
	 * 
	 * @param v
	 *            账户详情视图
	 */
	public void showAccountMessageDialog(View v) {
		if (messageDialog != null && messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		messageDialog.setCancelable(false);
		showDialogFill(v, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	/**
	 * 外汇任务提示框
	 * 该提示框需要手动关闭，调用BaseDroidApp.getInstanse().dismissMessageDialogFore();
	 * 
	 * @param v
	 * 
	 */
	public void showForexMessageDialog(View v) {
		if (messageDialogFore != null && messageDialogFore.isShowing()) {
			messageDialogFore.dismiss();
		}
		messageDialogFore = new CustomDialog(currentAct, R.style.Theme_Dialog);
		messageDialogFore.setCancelable(false);
		showDialogForeFill(v, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
	}

	/**
	 * 用来展示自定义框（不包括 errorDialog）
	 * 
	 * @param view
	 *            所要展示的视图
	 * @param width
	 *            要展示视图的宽
	 * @param height
	 *            要展示视图的高
	 */
	public void showDialogForeFill(View view, int width, int height) {

		messageDialogFore.setContentView(view);
		WindowManager.LayoutParams lp = messageDialogFore.getWindow()
				.getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH * 19 / 20;
		lp.height = LayoutValue.SCREEN_HEIGHT * 4 / 5;
		messageDialogFore.getWindow().setAttributes(lp);
		// messageDialog.getWindow().setWindowAnimations(R.style.shotcutDialogAnimation);
		messageDialogFore.show();
		messageDialogFore.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	/**
	 * 电子现金账户弹出框
	 */
	public void showFinanceIcAccountMessageDialog(View v) {
		if (messageDialog != null && messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		messageDialog.setCancelable(false);
		showDialogFill(v, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	/**
	 * 展示自定义View
	 * 
	 * @param v
	 */
	public void showViewWrapDialog(View v) {
		if (messageDialog != null && messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		messageDialog.setCancelable(false);
		showDialogWrap(v);
	}

	/**
	 * 用来展示 系统提示框大小的消息提示框
	 * 
	 * @param view
	 */
	public void showDialog(View view) {
		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		errorDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		errorDialog.setContentView(view);
		WindowManager.LayoutParams lp = errorDialog.getWindow().getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH * 6 / 7;
		lp.height = LayoutValue.SCREEN_HEIGHT * 6 / 7;
		lp.gravity = Gravity.CENTER;
		errorDialog.getWindow().setAttributes(lp);
		errorDialog.setCancelable(false);
		errorDialog.show();
		errorDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	/**
	 * 用来展示 超时的消息提示框
	 * 
	 * @param view
	 */
	public void showDialogTimeOut(View view) {
		if (timeOutDialog != null && timeOutDialog.isShowing()) {
			timeOutDialog.dismiss();
		}
		timeOutDialog.setContentView(view);
		WindowManager.LayoutParams lp = timeOutDialog.getWindow().getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH * 6 / 7;
		lp.height = LayoutValue.SCREEN_WIDTH / 2;
		timeOutDialog.getWindow().setAttributes(lp);
		timeOutDialog.setCancelable(false);
		timeOutDialog.show();
		timeOutDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	/**
	 * 用来展示 系统提示框大小的消息提示框
	 * 
	 * @param view
	 */
	public void showAddressDialog(View view) {
		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		errorDialog = new CustomDialog(getCurrentAct());
		errorDialog.setContentView(view);
		errorDialog.setCancelable(false);
		WindowManager.LayoutParams lp = errorDialog.getWindow().getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH * 6 / 7;
		lp.height = LayoutValue.SCREEN_WIDTH / 2;
		errorDialog.getWindow().setAttributes(lp);
		errorDialog.show();
		errorDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	/**
	 * 用来展示自定义框（不包括 errorDialog）
	 * 
	 * @param view
	 *            所要展示的视图
	 * @param width
	 *            要展示视图的宽
	 * @param height
	 *            要展示视图的高
	 */
	public void showDialogFill(View view, int width, int height) {
		if (messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog.setContentView(view);
		WindowManager.LayoutParams lp = messageDialog.getWindow()
				.getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH;
		lp.height = LayoutValue.SCREEN_HEIGHT * 7 / 8;
		messageDialog.getWindow().setAttributes(lp);
		// messageDialog.getWindow().setWindowAnimations(R.style.shotcutDialogAnimation);
		messageDialog.show();
		messageDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	/**
	 * 用于展示自定义信息框
	 * 
	 * @param view
	 */
	private void showDialogWrap(View view) {
		if (messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog.setContentView(view);
		WindowManager.LayoutParams lp = messageDialog.getWindow()
				.getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH * 19 / 20;

		lp.height = LayoutValue.SCREEN_BOTTOMHIGHT * 2 / 3;
		// lp.height = LayoutParams.WRAP_CONTENT;

		// lp.height = LayoutValue.SCREEN_BOTTOMHIGHT * 2 / 3;
		lp.height = LayoutParams.WRAP_CONTENT;

		messageDialog.getWindow().setAttributes(lp);
		messageDialog.show();
		messageDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	/**
	 * 信息消息框
	 * 
	 * @param message
	 *            要展示的消息
	 */
	public void showInfoMessageDialog(CharSequence message) {
		showInfoMessageDialog(message, Gravity.CENTER);
	}

	/**
	 * 
	 * @Description: 信息消息框
	 * @param message
	 *            要展示的消息
	 * @param gravity
	 *            textView gravity属性
	 * @author: luql
	 * @date: 2015年2月11日 上午11:45:39
	 */
	public void showInfoMessageDialog(CharSequence message, int gravity) {
		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		errorDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = errorDialog.initInfoDialogView(null, message, gravity,
				globalOnclickListener);
		errorDialog.setCancelable(false);
		showDialog(v);
	}
	
	/**
	 * 
	 * @Description: 信息消息框
	 * @param message1
	 *            要展示的消息
	 * @param gravity
	 *            textView gravity属性
	 * @author: luql
	 * @date: 2015年2月11日 上午11:45:39
	 */
	public void showInfoMessageDialog2(CharSequence message1, CharSequence message2, int gravity) {
		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		errorDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = errorDialog.initInfoDialogView2(null, message1, message2, gravity,
				globalOnclickListener);
		errorDialog.setCancelable(false);
		showDialog(v);
	}

	/**
	 * checkbox的
	 * wuhan
	 */
	public void showInfoMessageCheckedBoxDialog(String message,View.OnClickListener onclickListener,OnCheckedChangeListener checkedListener){
		if(checkboxDialog!=null && checkboxDialog.isShowing()){
			checkboxDialog.dismiss();
		}
		checkboxDialog = new CustomDialog(currentAct);
		View v = checkboxDialog.checkboxDialogView(message, onclickListener, checkedListener);
		checkboxDialog.setCancelable(false);
//		checkboxDialog.setCancelable(true);
		
		showCheckedBoxDialog(v);
	}
	
	public void showCheckedBoxDialog(View view) {
		if (checkboxDialog != null && checkboxDialog.isShowing()) {
			checkboxDialog.dismiss();
		}
		checkboxDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		checkboxDialog.setContentView(view);
		WindowManager.LayoutParams lp = checkboxDialog.getWindow().getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH * 6 / 7;
		lp.height = LayoutValue.SCREEN_HEIGHT * 6 / 7;
		lp.gravity = Gravity.CENTER;
		checkboxDialog.getWindow().setAttributes(lp);
		checkboxDialog.setCancelable(false);
		checkboxDialog.show();
		checkboxDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}
	
	
	/**
	 * 信息消息框展示屏幕超时
	 */
	public void showTimeOutDialog(OnClickListener clickListenner) {

		if (timeOutDialog != null && timeOutDialog.isShowing()) {
			timeOutDialog.dismiss();
		}
		timeOutDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = timeOutDialog.initInfoDialogView(null,
				getScreenTimeoutString(), clickListenner);
		timeOutDialog.setCancelable(false);
		showDialogTimeOut(v);
	}

	/**
	 * 关联账户转账类型 消息框
	 * 
	 * @param onclickListener
	 */
	public void showChooseTransTypeDialog(OnClickListener onclickListener) {
		if (messageDialog != null && messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = messageDialog
				.initRelativeAccTransTypeDialogView(onclickListener);
		if (messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog.setContentView(v);
		WindowManager.LayoutParams lp = messageDialog.getWindow()
				.getAttributes();
		lp.x = LayoutValue.SCREEN_WIDTH / 2;
		lp.y = LayoutValue.SCREEN_HEIGHT * 2 / 5;
		lp.width = LayoutValue.SCREEN_WIDTH;
		lp.height = LayoutValue.SCREEN_HEIGHT * 2 / 5;
		messageDialog.getWindow().setAttributes(lp);
		messageDialog.getWindow().setWindowAnimations(
				R.style.shotcutDialogAnimation);
		messageDialog.show();
		messageDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	/**
	 * 信用卡设定消息框
	 */
	public void showCrcdSetupTypeDialog(OnClickListener onclickListener) {
		if (messageDialog != null && messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = messageDialog.initCrcdSetupTypeDialogView(onclickListener);
		if (messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog.setContentView(v);
		WindowManager.LayoutParams lp = messageDialog.getWindow()
				.getAttributes();
		lp.x = LayoutValue.SCREEN_WIDTH / 2;
		lp.y = LayoutValue.SCREEN_HEIGHT / 4;
		lp.width = LayoutValue.SCREEN_WIDTH;
		lp.height = LayoutValue.SCREEN_HEIGHT / 2;
		messageDialog.getWindow().setAttributes(lp);
		messageDialog.getWindow().setWindowAnimations(
				R.style.shotcutDialogAnimation);
		messageDialog.show();
		messageDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	/**
	 * 转账汇款 修改收款人手机号和别名 选择dialog
	 * 
	 * @param onclickListener
	 */
	public void showPayeeEditDialog(OnClickListener onclickListener) {
		if (messageDialog != null && messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = messageDialog.initPayeeEditDialogView(onclickListener);

		if (messageDialog.isShowing()) {
			messageDialog.dismiss();
		}

		messageDialog.setContentView(v);
		WindowManager.LayoutParams lp = messageDialog.getWindow()
				.getAttributes();
		lp.x = LayoutValue.SCREEN_WIDTH / 2;
		lp.y = LayoutValue.SCREEN_HEIGHT * 2 / 5;
		lp.width = LayoutValue.SCREEN_WIDTH;
		lp.height = LayoutValue.SCREEN_HEIGHT * 2 / 5;
		messageDialog.getWindow().setAttributes(lp);
		messageDialog.getWindow().setWindowAnimations(
				R.style.shotcutDialogAnimation);
		messageDialog.show();
		messageDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	/**
	 * 修改收款人手机号和 dialog
	 * 
	 * @param message
	 *            要展示的消息
	 */
	// public void showPayeeEditMobileDialog(OnClickListener
	// onclickListener,String phoneNum) {
	// errorDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
	// View v = errorDialog.initPayeeEditMobileView( onclickListener ,phoneNum);
	// showDialog(v);
	//
	//
	// // messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
	// // View v =
	// messageDialog.initPayeeEditMobileView(onclickListener,phoneNum);
	// // messageDialog.setContentView(v);
	// // WindowManager.LayoutParams lp =
	// messageDialog.getWindow().getAttributes();
	// // messageDialog.show();
	//
	//
	// }
	/**
	 * 我要存定期 账户详情弹出框
	 * @param onCreateNoticeListener
	 *            通知按钮监听
	 * @param onCheckoutListener
	 *            支取按钮监听
	 */
	public void showMyRegSaveAccDetailMessageDialog(int position,
			View.OnClickListener onCreateNoticeListener,
			View.OnClickListener onCheckoutListener,
			View.OnClickListener onContinueSaveListener,
			View.OnClickListener onModifyNickNameListener) {
		if (messageDialog != null && messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		messageDialog.setCancelable(false);
		View v = messageDialog.initMySaveDetailDialogView(position,
				onCreateNoticeListener, onCheckoutListener,
				onContinueSaveListener, onModifyNickNameListener);
		showDialogFill(v, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	// /////////////////////// 账户管理 我的定期存款 列表详情页点击事件 start//////////////////

	/**
	 * 我要存定期 账户详情弹出框 账户管理 存折册号和存单序号反显数据
	 * @param onCreateNoticeListener
	 *            通知按钮监听
	 * @param onCheckoutListener
	 *            支取按钮监听
	 */
	public void showMyRegSaveAccDialog(int selectedPosition,
			Map<String, Object> positionMap,
			View.OnClickListener onCreateNoticeListener,
			View.OnClickListener onCheckoutListener,
			View.OnClickListener onContinueSaveListener,
			View.OnClickListener onModifyNickNameListener) {
		if (messageDialog != null && messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		messageDialog.setCancelable(false);
		View v = messageDialog.initMySaveView(selectedPosition, positionMap,
				onCreateNoticeListener, onCheckoutListener,
				onContinueSaveListener, onModifyNickNameListener);
		showDialogFill(v, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	/**
	 * 我要存定期 账户详情弹出框 修改 无效账户 无支取按钮
	 * 
	 * @param selectedPosition
	 *            用户选择账户
	 * @param onCreateNoticeListener
	 *            通知按钮监听
	 */
	public void showMyRegSaveAccDetailDialog(int selectedPosition,
			Map<String, Object> positionMap,
			View.OnClickListener onCreateNoticeListener,
			View.OnClickListener onContinueSaveListener,
			View.OnClickListener onModifyNickNameListener) {
		if (messageDialog != null && messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		messageDialog.setCancelable(false);
		View v = messageDialog.initMySaveDialogView(selectedPosition,
				positionMap, onCreateNoticeListener, onContinueSaveListener,
				onModifyNickNameListener);
		showDialogFill(v, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	// /////////////////////// 账户管理 我的定期存款 列表详情页点击事件 end//////////////////
	/**
	 * 大额到账提醒设置账户详情弹出框
	 * 
	 * @param position
	 *            下标
	 * @param onSignListener
	 *            签约点击
	 * @param onDeleteListener
	 *            解约点击
	 * @param onModifyListener
	 *            修改点击
	 */
	public void showNonFixedProductRemindAccDetailDialog(int position,
			final View.OnClickListener onSignListener,
			final View.OnClickListener onDeleteListener,
			final View.OnClickListener onModifyListener) {
		if (messageDialog != null && messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		messageDialog.setCancelable(false);
		View v = messageDialog.initNonFixedProductRemindAccDetailDialogView(
				position, onSignListener, onDeleteListener, onModifyListener);
		showDialogFill(v, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	/*
	 * 功能描述：转账管理账户列表弹出框
	 * 
	 * @param list 数据源
	 * 
	 * @param type 转入、转出类型
	 * 
	 * @param listViewClick listview的监听器
	 * 
	 * @param click button click事件 *
	 */
	// public void showTransferAccountsView(List<Map<String, Object>> list,
	// int type, AdapterView.OnItemClickListener listViewClick,
	// View.OnClickListener click) {
	// if (messageDialog != null && messageDialog.isShowing()) {
	// messageDialog.dismiss();
	// }
	// messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
	// messageDialog.setCancelable(false);
	//
	// View content = messageDialog.initTransferAccountsDialogView(list, type,
	// listViewClick, click);
	// messageDialog.setContentView(content);
	//
	// WindowManager.LayoutParams lp = messageDialog.getWindow()
	// .getAttributes();
	//
	// lp.width = ViewGroup.LayoutParams.FILL_PARENT;
	// lp.height = ViewGroup.LayoutParams.FILL_PARENT;
	//
	// messageDialog.getWindow().setAttributes(lp);
	//
	// messageDialog.getWindow().setWindowAnimations(
	// R.style.shotcutDialogAnimation);
	//
	// messageDialog.show();
	// };

	/**
	 * 转账 显示转入账户列表
	 * 
	 * @param listView
	 * @param backListener
	 * @param addAccInNoClicklistener
	 * @param textWatcher
	 */
	public void showTransAccInListDialog(boolean isshowtoprightBtn,
			ListView listView, View.OnClickListener backListener,
			View.OnClickListener addAccInNoClicklistener,
			TextWatcher textWatcher) {
		if (messageDialog != null && messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		messageDialog.setCancelable(false);

		View content = messageDialog.initTranAccInListView(isshowtoprightBtn,
				listView, backListener, addAccInNoClicklistener, textWatcher);
		messageDialog.setContentView(content);

		WindowManager.LayoutParams lp = messageDialog.getWindow()
				.getAttributes();

		lp.width = ViewGroup.LayoutParams.FILL_PARENT;
		lp.height = ViewGroup.LayoutParams.FILL_PARENT;
		messageDialog.getWindow().setAttributes(lp);
		messageDialog.getWindow().setWindowAnimations(
				R.style.shotcutDialogAnimation);
		messageDialog.show();
		messageDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	};

	/**
	 * 存款管理转入转出账户弹出框
	 * 
	 * @param listView
	 */
	public void showDeptTranoutinDialog(int flag, ListView listView,
			View.OnClickListener backListener,
			View.OnClickListener rightTopListener) {
		if (messageDialog != null && messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		listView.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		listView.setBackgroundColor(Color.WHITE);
		messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		messageDialog.setCancelable(false);

		View content = messageDialog.initDeptTranOutInView(flag, listView,
				backListener, rightTopListener);
		messageDialog.setContentView(content);
		WindowManager.LayoutParams lp = messageDialog.getWindow()
				.getAttributes();
		lp.width = ViewGroup.LayoutParams.FILL_PARENT;
		lp.height = ViewGroup.LayoutParams.FILL_PARENT;
		messageDialog.getWindow().setAttributes(lp);
		messageDialog.getWindow().setWindowAnimations(
				R.style.shotcutDialogAnimation);
		messageDialog.show();
		messageDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	/**
	 * 存款管理转入转出账户弹出框
	 * 
	 * @param listView
	 */
	public void showMobileTranoutDialog(ListView listView,
			View.OnClickListener backListener) {
		if (messageDialog != null && messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		messageDialog.setCancelable(false);

		View content = messageDialog.initMobileTranOutView(listView,
				backListener);
		messageDialog.setContentView(content);
		WindowManager.LayoutParams lp = messageDialog.getWindow()
				.getAttributes();
		lp.width = ViewGroup.LayoutParams.FILL_PARENT;
		lp.height = ViewGroup.LayoutParams.FILL_PARENT;
		messageDialog.getWindow().setAttributes(lp);
		messageDialog.getWindow().setWindowAnimations(
				R.style.shotcutDialogAnimation);
		messageDialog.show();
		messageDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	/**
	 * 点击定期一本通时候 更换账号 弹出详情框
	 * 
	 * @param position
	 * @param onCreateNoticeListener
	 * @param onCheckoutListener
	 */
	public void showMyRegSaveWholesaveAccDetailMessageDialog(int position,
			View.OnClickListener onCreateNoticeListener,
			View.OnClickListener onCheckoutListener) {
		if (messageDialog != null && messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		messageDialog.setCancelable(false);
		View v = messageDialog.initMySaveWholesaveDetailDialogView(position,
				onCreateNoticeListener, onCheckoutListener);
		showDialogFill(v, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	/**
	 * 显示设定基金关注页面
	 * 
	 * @Author xyl s
	 * @param btnMessage
	 *            btn 上显示的信息
	 * @param textviewMessage
	 *            提示信息
	 * @param onclickListener
	 *            btn 的onClick
	 */
	public void showSettingAttationFundDialog(String btnMessage,
			String textviewMessage, View.OnClickListener onclickListener) {
		if (messageDialog != null && messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = messageDialog.initFincSetAttentionFundDialogView(btnMessage,
				textviewMessage, onclickListener);
		if (messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog.setContentView(v);
		WindowManager.LayoutParams lp = messageDialog.getWindow()
				.getAttributes();
		lp.width = LayoutParams.FILL_PARENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		messageDialog.getWindow().setAttributes(lp);
		messageDialog.show();
		messageDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	/**
	 * 显示选择安全因子的选择框
	 * 
	 * @param onclickListener
	 */
	public void showSeurityChooseDialog(
			final View.OnClickListener onclickListener) {
		if (securityDialog != null && securityDialog.isShowing()) {
			securityDialog.dismiss();
		}
		securityDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = LayoutInflater.from(currentAct).inflate(
				R.layout.security_list, null);
		final ListView listview = (ListView) v.findViewById(R.id.listview);
		final SecurityAdapter adapter;

		/** 操作员信息 */
		Map<String, Object> resultMap = (Map<String, Object>) BaseDroidApp
				.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);
		/** 查询版客户类型 :1 查询版老客户;2 查询版新客户（非电子卡签约）;3 查询版新客户（电子卡签约） */
		String qryCustType = (String) resultMap.get("qryCustType");
		if ("2".equals(qryCustType) || "3".equals(qryCustType)) {
			if (securityNameList.size() == 1) {// 仅有一条数据 不需要用户选择直接跳过
				if (securityIdList.contains("96")) { // 单手机交易码认证 判断是否绑定当前设备
					// 判断是否绑定当前设备 是否激活
					bindAndOpenView = v;
					bindAndOpenOnclickListener = onclickListener;
					securityChoosed = securityIdList.get(0);
					requestForLoginInfo();
				} else {
					securityChoosed = securityIdList.get(0);
					onclickListener.onClick(v);
				}

			} else {

				adapter = new SecurityAdapter(currentAct, securityNameList,
						securityIdList, defaultCombinId);
				listview.setAdapter(adapter);
				listview.setOnItemClickListener(new OnItemClickListener() {
	
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						// 保存安全因子的值
						securityChoosed = securityIdList.get(position);
						adapter.changeDate();
						if ("0".equals(securityChoosed)
								|| "96".equals(securityChoosed)) {
							// 判断是否绑定当前设备 ，进入绑定激活单手机交易码流程
							bindAndOpenView = arg1;
							bindAndOpenOnclickListener = onclickListener;
							requestForLoginInfo();
						} else {
							// 如果选择的是音频Key，则需要在此处连接音频key
							// 。音频Key插入广播中已经连接过音频Key，因此不需要再次重新连接
							onclickListener.onClick(arg1);
						}
						if (securityDialog.isShowing())
							securityDialog.dismiss();
					}
				});
				if (securityDialog.isShowing())
					securityDialog.dismiss();
				securityDialog.setContentView(v);
				WindowManager.LayoutParams lp = securityDialog.getWindow()
						.getAttributes();
				lp.width = LayoutValue.SCREEN_WIDTH * 6 / 7;
				lp.height = LayoutValue.SCREEN_HEIGHT / 2;
				securityDialog.getWindow().setAttributes(lp);
				BaseHttpEngine.dissMissProgressDialog();
				securityDialog.show();
				securityDialog.getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			}
		} else {

			if (securityNameList == null || securityNameList.size() == 0) {
				securityChoosed = "96";
				/** 返回没有 默认加上单手机交易码 */
				bindAndOpenView = v;
				bindAndOpenOnclickListener = onclickListener;
				requestForLoginInfo();
			} else {
				if (!securityIdList.contains("96")) {
					securityNameList.add("手机交易码");
					securityIdList.add("96");
				}
				if (securityNameList.size() == 1) {// 仅有一条数据 不需要用户选择直接跳过
					if (securityIdList.contains("96")) { // 单手机交易码认证 判断是否绑定当前设备
						// 判断是否绑定当前设备 是否激活
						bindAndOpenView = v;
						bindAndOpenOnclickListener = onclickListener;
						securityChoosed = securityIdList.get(0);
						requestForLoginInfo();
					} else {
						securityChoosed = securityIdList.get(0);
						onclickListener.onClick(v);
					}

				} else {
					adapter = new SecurityAdapter(currentAct, securityNameList,
							securityIdList, defaultCombinId);
					listview.setAdapter(adapter);
					listview.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int position, long arg3) {
							// 保存安全因子的值
							securityChoosed = securityIdList.get(position);
							adapter.changeDate();
							if ("0".equals(securityChoosed)
									|| "96".equals(securityChoosed)) {
								// 判断是否绑定当前设备 ，进入绑定激活单手机交易码流程
								bindAndOpenView = arg1;
								bindAndOpenOnclickListener = onclickListener;
								requestForLoginInfo();
							} else {
								// 如果选择的是音频Key，则需要在此处连接音频key
								// 。音频Key插入广播中已经连接过音频Key，因此不需要再次重新连接
								onclickListener.onClick(arg1);
							}
							if (securityDialog.isShowing()) {
								securityDialog.dismiss();
							}
						}
					});
					if (securityDialog.isShowing()) {
						securityDialog.dismiss();
					}
					securityDialog.setContentView(v);
					WindowManager.LayoutParams lp = securityDialog.getWindow()
							.getAttributes();
					lp.width = LayoutValue.SCREEN_WIDTH * 6 / 7;
					lp.height = LayoutValue.SCREEN_HEIGHT / 2;
					securityDialog.getWindow().setAttributes(lp);
					BaseHttpEngine.dissMissProgressDialog();
					securityDialog.show();
					securityDialog.getWindow().setSoftInputMode(
							WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

				}
			}
		}

	}

	/** 显示安全工具选择框 */
	public void showSeurityChooseDialog(
			final View.OnClickListener onclickListener,
			final ArrayList<String> securityIdList,
			ArrayList<String> securityNameList) {
		if (securityDialog != null && securityDialog.isShowing()) {
			securityDialog.dismiss();
		}
		securityDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = LayoutInflater.from(currentAct).inflate(
				R.layout.security_list, null);
		final ListView listview = (ListView) v.findViewById(R.id.listview);
		final SecurityAdapter adapter;
		if (securityNameList.size() == 1) {// 仅有一条数据 不需要用户选择直接跳过
			if (securityIdList.contains("96")) { // 单手机交易码认证 判断是否绑定当前设备
				// 判断是否绑定当前设备 是否激活
				bindAndOpenView = v;
				bindAndOpenOnclickListener = onclickListener;
				securityChoosed = securityIdList.get(0);
				requestForLoginInfo();
			} else {
				securityChoosed = securityIdList.get(0);
				onclickListener.onClick(v);
			}

		} else {
			adapter = new SecurityAdapter(currentAct, securityNameList,
					securityIdList, defaultCombinId);
			listview.setAdapter(adapter);
			listview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// 保存安全因子的值
					securityChoosed = securityIdList.get(position);
					adapter.changeDate();
					if ("0".equals(securityChoosed)
							|| "96".equals(securityChoosed)) {
						// 判断是否绑定当前设备 ，进入绑定激活单手机交易码流程
						bindAndOpenView = arg1;
						bindAndOpenOnclickListener = onclickListener;
						requestForLoginInfo();
					} else {
						// 如果选择的是音频Key，则需要在此处连接音频key
						// 。音频Key插入广播中已经连接过音频Key，因此不需要再次重新连接
						onclickListener.onClick(arg1);
					}
					if (securityDialog.isShowing()) {
						securityDialog.dismiss();
					}
				}
			});
			if (securityDialog.isShowing()) {
				securityDialog.dismiss();
			}
			securityDialog.setContentView(v);
			WindowManager.LayoutParams lp = securityDialog.getWindow()
					.getAttributes();
			lp.width = LayoutValue.SCREEN_WIDTH * 6 / 7;
			lp.height = LayoutValue.SCREEN_HEIGHT / 2;
			securityDialog.getWindow().setAttributes(lp);
			BaseHttpEngine.dissMissProgressDialog();
			securityDialog.show();
			securityDialog.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		}

	}

	/**
	 * 显示选择安全因子的选择框(不包含激活流程)
	 * 
	 * @param onclickListener
	 */
	public void showSeurityChooseDialogWithoutActive(
			final View.OnClickListener onclickListener) {
		String mess = StringUtil.isNullOrEmpty(getNoSafetyCombinsMsg()) ? "尊敬的客户，系统检测到当前手机设备已被其他手机银行用户绑定。请选择其它安全工具（如数字安全证书、动态口令+手机交易码）进行交易。您可以手动卸载并重新安装手机银行客户端后再进行设备绑定！"
				: getNoSafetyCombinsMsg();

		showSeurityChooseDialogWithoutActiveHandler(onclickListener, mess);
		// if (securityDialog != null && securityDialog.isShowing()) {
		// securityDialog.dismiss();
		// }
		// securityDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		// View v = LayoutInflater.from(currentAct).inflate(
		// R.layout.security_list, null);
		// final ListView listview = (ListView) v.findViewById(R.id.listview);
		// final SecurityAdapter adapter;
		//
		// if (securityNameList != null) {
		// if (securityNameList.size() > 1) {
		// adapter = new SecurityAdapter(currentAct, securityNameList,
		// defaultCombinId);
		// listview.setAdapter(adapter);
		// listview.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1,
		// int position, long arg3) {
		// adapter.changeDate(position);
		// // 保存安全因子的值
		// securityChoosed = securityIdList
		// .get(((SecurityAdapter) listview.getAdapter())
		// .getIndex());
		// // 如果选择的是音频Key，则需要在此处连接音频key
		// // 。音频Key插入广播中已经连接过音频Key，因此不需要再次重新连接
		//
		// onclickListener.onClick(arg1);
		// if (securityDialog.isShowing()) {
		// securityDialog.dismiss();
		// }
		// }
		// });
		// if (securityDialog.isShowing()) {
		// securityDialog.dismiss();
		// }
		// securityDialog.setContentView(v);
		// WindowManager.LayoutParams lp = securityDialog.getWindow()
		// .getAttributes();
		// lp.width = LayoutValue.SCREEN_WIDTH * 6 / 7;
		// lp.height = LayoutValue.SCREEN_HEIGHT / 2;
		// securityDialog.getWindow().setAttributes(lp);
		// BaseHttpEngine.dissMissProgressDialog();
		// securityDialog.show();
		// securityDialog.getWindow().setSoftInputMode(
		// WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		// } else if (securityNameList.size() == 1) {// 仅有一条数据 不需要用户选择直接跳过
		// securityChoosed = securityIdList.get(0);
		// onclickListener.onClick(v);
		// } else {
		//
		// BaseHttpEngine.dissMissProgressDialog();
		// String mess = StringUtil.isNullOrEmpty(getNoSafetyCombinsMsg()) ?
		// "尊敬的客户，系统检测到当前手机设备已被其他手机银行用户绑定。请选择其它安全工具（如数字安全证书、动态口令+手机交易码）进行交易。您可以手动卸载并重新安装手机银行客户端后再进行设备绑定！":
		// getNoSafetyCombinsMsg();
		// BaseDroidApp.getInstanse().showMessageDialog(mess/*"尊敬的客户，系统检测到当前手机设备已被其他手机银行用户绑定。请选择其它安全工具（如数字安全证书、动态口令+手机交易码）进行交易。您可以手动卸载并重新安装手机银行客户端后再进行设备绑定！"*/,
		// new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// BaseDroidApp.getInstanse()
		// .dismissMessageDialog();
		// }
		// });
		// }
		// }

	}

	/**
	 * 显示选择安全因子的选择框(不包含激活流程) message : 无安全因子时，报错信息
	 * */
	public void showSeurityChooseDialogWithoutActive(
			final View.OnClickListener onclickListener, String message) {
		showSeurityChooseDialogWithoutActiveHandler(onclickListener, message);
	}

	private void showSeurityChooseDialogWithoutActiveHandler(
			final View.OnClickListener onclickListener, String message) {
		if (securityDialog != null && securityDialog.isShowing()) {
			securityDialog.dismiss();
		}
		securityDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = LayoutInflater.from(currentAct).inflate(
				R.layout.security_list, null);
		final ListView listview = (ListView) v.findViewById(R.id.listview);
		final SecurityAdapter adapter;

		if (securityNameList != null) {
			if (securityNameList.size() > 1) {
				adapter = new SecurityAdapter(currentAct, securityNameList,
						securityIdList, defaultCombinId);
				listview.setAdapter(adapter);
				listview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						// 保存安全因子的值
						securityChoosed = securityIdList.get(position);
						// 如果选择的是音频Key，则需要在此处连接音频key
						// 。音频Key插入广播中已经连接过音频Key，因此不需要再次重新连接
						adapter.changeDate();
						onclickListener.onClick(arg1);
						if (securityDialog.isShowing()) {
							securityDialog.dismiss();
						}
					}
				});
				if (securityDialog.isShowing()) {
					securityDialog.dismiss();
				}
				securityDialog.setContentView(v);
				WindowManager.LayoutParams lp = securityDialog.getWindow()
						.getAttributes();
				lp.width = LayoutValue.SCREEN_WIDTH * 6 / 7;
				lp.height = LayoutValue.SCREEN_HEIGHT / 2;
				securityDialog.getWindow().setAttributes(lp);
				BaseHttpEngine.dissMissProgressDialog();
				securityDialog.show();
				securityDialog.getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			} else if (securityNameList.size() == 1) {// 仅有一条数据 不需要用户选择直接跳过
				securityChoosed = securityIdList.get(0);
				onclickListener.onClick(v);
			} else {

				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showMessageDialog(message,
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								BaseDroidApp.getInstanse()
										.dismissMessageDialog();
							}
						});
			}
		}

	}

	/**
	 * 显示添加快捷方式框
	 * 
	 * @param icons
	 * @param onItemClickListener
	 *            子项监听事件
	 */
	public void showShotcutDialog(ArrayList<ImageTextAndAct> icons,
			OnItemClickListener onItemClickListener) {
		if (messageDialog != null && messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = messageDialog.initShotcutDialog(icons, globalOnclickListener,
				onItemClickListener);
		if (messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog.setContentView(v);
		WindowManager.LayoutParams lp = messageDialog.getWindow()
				.getAttributes();
		lp.x = LayoutValue.SCREEN_WIDTH / 2;
		lp.y = LayoutValue.SCREEN_HEIGHT / 8;
		lp.width = LayoutValue.SCREEN_WIDTH;
		lp.height = LayoutValue.SCREEN_HEIGHT * 3 / 4;
		messageDialog.getWindow().setAttributes(lp);
		messageDialog.getWindow().setWindowAnimations(
				R.style.shotcutDialogAnimation);
		messageDialog.show();
		messageDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	/** 显示弹出框 */
	public void showFiincMessageDialog(View v) {
		if (messageDialog != null && messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		messageDialog.setCancelable(false);
		WindowManager.LayoutParams lp = messageDialog.getWindow()
				.getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH * 19 / 20;
		lp.height = LayoutValue.SCREEN_HEIGHT * 3 / 4;
		messageDialog.getWindow().setAttributes(lp);
		showDialogFill(v, lp.width, lp.height);
	}

	/**
	 * 默认监听器
	 */
	OnClickListener globalOnclickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch ((Integer) v.getTag()) {
			case CustomDialog.TAG_CONFIRM:
				if (errorDialog.isShowing()) {
					errorDialog.dismiss();
				}

				if (timeOutDialog != null && timeOutDialog.isShowing()) {
					timeOutDialog.dismiss();
				}
				break;
			case CustomDialog.TAG_SURE:
				if (errorDialog.isShowing()) {
					errorDialog.dismiss();
				}
				break;
			case CustomDialog.TAG_CANCLE:
				if (errorDialog.isShowing()) {
					errorDialog.dismiss();
				}
				break;
			case CustomDialog.TAG_CLOSE:
				dismissMessageDialog();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 从下往上弹出的选择框 仿Iphone 公用
	 * 
	 * @param showView
	 *            要展示的View
	 */
	public void showFootChooseDialog(View showView) {
		if (footChooseDialog != null && footChooseDialog.isShowing()) {
			footChooseDialog.dismiss();
		}
		footChooseDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		if (footChooseDialog.isShowing()) {
			footChooseDialog.dismiss();
		}
		footChooseDialog.setContentView(showView);
		WindowManager.LayoutParams lp = footChooseDialog.getWindow()
				.getAttributes();
		lp.x = LayoutValue.SCREEN_WIDTH / 2;
		lp.y = LayoutValue.SCREEN_HEIGHT * 2 / 5;
		lp.width = LayoutValue.SCREEN_WIDTH;
		lp.height = LayoutValue.SCREEN_HEIGHT * 2 / 5;
		footChooseDialog.getWindow().setAttributes(lp);
		footChooseDialog.getWindow().setWindowAnimations(
				R.style.shotcutDialogAnimation);
		footChooseDialog.show();
		footChooseDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	/**
	 * 关闭底部的选择框按钮
	 */
	public void dissmissFootChooseDialog() {
		if (footChooseDialog != null && footChooseDialog.isShowing()) {
			footChooseDialog.dismiss();
			footChooseDialog = null;
		}
	}

	/**
	 * 个人设定 账户管家 dialog
	 * 
	 * @Author xyl
	 * @param onBtnListenner
	 *            监听
	 * @param isCanSetDefaultAcc
	 *            是否可设定默认账户
	 * @param isCanConsernConnnection
	 *            是否可 取消关联
	 */
	public void setAccManagerMenuDialog(OnClickListener onBtnListenner,
			boolean isCanSetDefaultAcc, boolean isCanConsernConnnection) {
		if (messageDialog != null && messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = messageDialog.initSettingAccManagerDialogView(onBtnListenner,
				isCanSetDefaultAcc, isCanConsernConnnection);
		if (messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog.setContentView(v);
		/*
		 * WindowManager.LayoutParams lp = messageDialog.getWindow()
		 * .getAttributes(); lp.width = LayoutParams.MATCH_PARENT; lp.height =
		 * LayoutParams.WRAP_CONTENT;
		 * messageDialog.getWindow().setAttributes(lp);
		 * messageDialog.getWindow().setWindowAnimations(
		 * R.style.shotcutDialogAnimation); messageDialog.show();
		 */

		WindowManager.LayoutParams lp = messageDialog.getWindow()
				.getAttributes();
		lp.x = LayoutValue.SCREEN_WIDTH / 2;
		lp.y = LayoutValue.SCREEN_HEIGHT * 2 / 5;
		lp.width = LayoutValue.SCREEN_WIDTH;
		lp.height = LayoutValue.SCREEN_HEIGHT * 2 / 5;
		messageDialog.getWindow().setAttributes(lp);
		messageDialog.getWindow().setWindowAnimations(
				R.style.shotcutDialogAnimation);
		messageDialog.show();
		messageDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	/**
	 * 我的外汇----账户重设
	 * 
	 * @accNum：账号
	 * @alias：账号别名
	 * @typetype:账户类型
	 * @return
	 */

	public void showCustomerResetAccDialog(String accNum, String alias,
			String type, OnClickListener onClickListener) {
		if (messageDialog != null && messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = messageDialog.initForexCustomerAccResetDailgView(accNum,
				alias, type, onClickListener);
		if (messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog.setContentView(v);
		WindowManager.LayoutParams lp = messageDialog.getWindow()
				.getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH * 19 / 20;
		lp.height = LayoutParams.WRAP_CONTENT;
		messageDialog.getWindow().setAttributes(lp);
		// messageDialog.getWindow().setWindowAnimations(
		// R.style.shotcutDialogAnimation);
		messageDialog.show();
		messageDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	/**
	 * 电子支付 操作菜单
	 * 
	 * @param closeServiceListener
	 * @param modifyQuotaListener
	 */
	public void showEpayModifyMenuDialog(OnClickListener closeServiceListener,
			OnClickListener modifyQuotaListener) {
		if (messageDialog != null && messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);

		View v = messageDialog.initEpayModifyDialog(closeServiceListener,
				modifyQuotaListener, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						messageDialog.dismiss();
					}
				});

		if (messageDialog.isShowing()) {
			messageDialog.dismiss();
		}

		messageDialog.setContentView(v);
		WindowManager.LayoutParams lp = messageDialog.getWindow()
				.getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH;
		lp.height = LayoutValue.SCREEN_HEIGHT * 2 / 5;
		messageDialog.getWindow().setAttributes(lp);
		messageDialog.getWindow().setWindowAnimations(
				R.style.shotcutDialogAnimation);
		messageDialog.show();
		messageDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	/**
	 * 信息消息框
	 * 
	 * @param message
	 *            要展示的消息
	 * @param onClickListener
	 *            :确定按钮的监听事件
	 */
	public void showMessageDialog(String message,
			OnClickListener onClickListener) {
		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		errorDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = errorDialog.initInfoDialogView(null, message, onClickListener);
		showDialog(v);
	}

	/**
	 * 弹出自定义模板的弹出框 View：自定义的模板
	 * */
	public void showMessageDialogByView(View view) {
		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		errorDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		// View v = errorDialog.initInfoDialogViewById(null,id,
		// message,Gravity.CENTER, onClickListener);
		showDialog(view);
	}

	/**
	 * 信息消息框
	 * 
	 * * @param message 要展示的消息
	 * 
	 * @param btnText
	 *            按钮的名称
	 * @param onClickListener
	 *            :按钮的监听事件
	 */
	public void showMsgDialogOneBtn(String message, String btnText,
			OnClickListener onClickListener) {
		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		errorDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = errorDialog.initInfoDialogViewOneBtn(message, btnText,
				onClickListener);
		showDialog(v);
	}

	/**
	 * 信息消息框
	 * 
	 * @param title
	 *            要展示的标题
	 * @param message
	 *            要展示的消息
	 * @param onClickListener
	 *            :确定按钮的监听事件
	 */
	public void showMessageDialog(String title, String message,
			OnClickListener onClickListener) {
		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		errorDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = errorDialog.initInfoDialogView(null, title, message,
				onClickListener);
		showDialog(v);
	}

	/**
	 * 音频key 修改密码预警提示框
	 * 
	 * @param message
	 */
	public void showMessageDialogWithoutBtn(String message) {
		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		errorDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = errorDialog.initInfoDialogView(null, message, null);
		v.findViewById(R.id.confirm_btn).setVisibility(View.GONE);
		showDialog(v);
	}

	/**
	 * 音频Key 交易确认弹出框
	 */
	public void showMessageAudiokey(OnClickListener onAudioClickListener) {
		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		errorDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = errorDialog.initInfoAudioDialogView(onAudioClickListener);
		showDialog(v);
	}

//	public BaseActivity getCurrentAct() {
//		return currentAct;
//	}
//
//	public void setCurrentAct(BaseActivity currentAct) {
//		this.currentAct = currentAct;
//	}

	public static BaseDroidApp getInstanse() {
		return instanse;
	}

	public BiiHttpEngine getBiiHttpEngine() {
		return biiHttpEngine;
	}

	public void setBiiHttpEngine(BiiHttpEngine biiHttpEngine) {
		this.biiHttpEngine = biiHttpEngine;
	}

	public CommonHttpEngine getCommonHttpEngine() {
		return commonHttpEngine;
	}

	public void setCommonHttpEngine(CommonHttpEngine commonHttpEngine) {
		this.commonHttpEngine = commonHttpEngine;
	}

	public PollingCommonHttpEngine getPollingCommhttpEngine() {
		return pollingCommhttpEngine;
	}

	public void setPollingCommhttpEngine(
			PollingCommonHttpEngine pollingCommhttpEngine) {
		this.pollingCommhttpEngine = pollingCommhttpEngine;
	}

	public ImageFileHttpEngine getImageFileHttpEngine() {
		return imageFileHttpEngine;
	}

	public void setImageFileHttpEngine(ImageFileHttpEngine imageFileHttpEngine) {
		this.imageFileHttpEngine = imageFileHttpEngine;
	}

	public FileHttpEngine getFileHttpEngine() {
		return fileHttpEngine;
	}

	public void setFileHttpEngine(FileHttpEngine fileHttpEngine) {
		this.fileHttpEngine = fileHttpEngine;
	}

	public HashMap<String, Object> getPollingDataMap() {
		return appDataMap;
	}

	public HashMap<String, Object> getBizDataMap() {
		return appDataMap;
	}

	// public HashMap<String, Object> getPollingDataMap() {
	// return (HashMap<String, Object>) appDataMap
	// .get(ConstantGloble.APP_POLLING_DATA_MAP);
	// }
	//
	// public HashMap<String, Object> getBizDataMap() {
	// return (HashMap<String, Object>) appDataMap
	// .get(ConstantGloble.APP_BIZ_DATA_MAP);
	// }

	public Notification getNotification() {
		return notification;
	}

	public DBHelper getDbHelper() {
		return dbHelper;
	}

	public SharedPreferences getSharedPrefrences() {
		return sharedPrefrences;
	}

	/**
	 * @return the messageDialog
	 */
	public CustomDialog getMessageDialog() {
		return messageDialog;
	}

	/**
	 * @param messageDialog
	 *            the messageDialog to set
	 */
	public void setMessageDialog(CustomDialog messageDialog) {
		this.messageDialog = messageDialog;
	}

	/**
	 * 重设后台的闹钟 如果切换到后台调用此方法，重新计时
	 */
	public void reSetalarm() {
		calendar.clear();
		calendar.setTimeInMillis(System.currentTimeMillis());
		// TODO 测试修改
		calendar.add(Calendar.SECOND, screenOutTime);
		if (alarm != null) {
			alarm.cancel(pd);
			alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pd);
		}

	}

	/**
	 * 快速交易，选择买入还是卖出
	 * 
	 * @Author xyl
	 */
	public void showSelectBuyOrSaleDialog(String buy, String sale,
			OnClickListener onClickListener) {
		if (messageDialog != null && messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = messageDialog.initPrmsSelectBuyOrSaleDialogView(buy, sale,
				onClickListener);

		if (messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog.setContentView(v);
		WindowManager.LayoutParams lp = messageDialog.getWindow()
				.getAttributes();
		lp.x = LayoutValue.SCREEN_WIDTH / 2;
		lp.y = LayoutValue.SCREEN_HEIGHT * 2 / 5;
		lp.width = LayoutValue.SCREEN_WIDTH;
		lp.height = LayoutValue.SCREEN_HEIGHT * 2 / 5;
		messageDialog.getWindow().setAttributes(lp);
		messageDialog.getWindow().setWindowAnimations(
				R.style.shotcutDialogAnimation);
		messageDialog.show();
		messageDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	/**
	 * 重设闹钟 如果屏幕有操作就要调用此方法，重新计时
	 */
	public void reSetalarmPre() {
		try {
			if (calendar != null) {
				calendar.clear();
				if (alarm != null) {
					alarm.cancel(pd);
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
				LogGloble.e(TAG, "reSetalarmPre calendar is null");
			}
		} catch (Exception e) {
			LogGloble.e(TAG, e.getMessage(), e);
		}

	}

	/**
	 * 停止(后台的闹钟)闹钟
	 */
	public void stopAlarm() {
		try {
			if (alarm != null) {
				alarm.cancel(pd);
			}
		} catch (Exception e) {
			LogGloble.e(TAG, e.getMessage(), e);
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
			LogGloble.e(TAG, e.getMessage(), e);
		}
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
			// 超时时增加退出接口的调用 add by xby 20140312
			Activity a = getCurrentAct();
			if(a instanceof BaseActivity == false)
				return;

			final BaseActivity activity = (BaseActivity) getCurrentAct();
			if(activity != null)
				activity.requestForLogout();
			isMainItemAutoClick = false;
			if (isLogin()) {
				if (LOGOUTACTION_BACK.equals(intent.getAction())) {// 后台超时
					SharedPreUtils.getInstance().addOrModifyInt(
							ConstantGloble.TIME_OUT, 1);
				} else if (LOGOUTACTION_PRE.equals(intent.getAction())) {// 前台超时
					SharedPreUtils.getInstance().addOrModifyInt(
							ConstantGloble.TIME_OUT, 2);
					// 强制退出
//					ActivityTaskManager.getInstance().removeAllActivity();
					BaseDroidApp.getInstanse().clientLogOut();
//					Intent intent1 = new Intent();
//					intent1.setClass(
//							BaseDroidApp.getInstanse().getCurrentAct(),
//							LoginActivity.class);
//					intent1.putExtra(ConstantGloble.TIME_OUT_CONFIRM, true);
//					intent1.putExtra(ConstantGloble.BACK_TO_MAIN, true);
//					BaseDroidApp.getInstanse().getCurrentAct()
//							.startActivity(intent1);
//					BaseActivity.getLoginUtils(activity).exe(new LoginTask.LoginCallback() {
//						public void loginStatua(boolean isLogin) {
//						}
//					});
					BaseDroidApp.getInstanse().showTimeOutDialog(new OnClickListener() {
						@Override
						public void onClick(View v) {
							timeOutDialog.dismiss();
							ActivityTaskManager.getInstance().removeAllActivity();
							AbstractLoginTool.Instance.Login(activity, new LoginTask.LoginCallback() {
								public void loginStatua(boolean isLogin) {
								}
							});
						}
					});

				}
			}

		}
	}

	
	
	/**
	 * 定位提示框
	 * 
	 * @param message
	 * @param onclick
	 */
	public void createLocationDialog(int tag, String btnText, String message,
			OnClickListener onclick) {
		// added by nl. 防止同时弹出多个错误提示框，引用覆盖的问题。 每弹出一个，dismiss掉该引用之前指向的对象
		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		errorDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		errorDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (event.getKeyCode() == event.KEYCODE_BACK) {
					ActivityTaskManager.getInstance().removeAllActivity();
				}
				return false;
			}
		});
		errorDialog.setContentView(errorDialog.createLocationProgressDialog(
				tag, btnText, message, onclick));
		WindowManager.LayoutParams lp = errorDialog.getWindow().getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH * 6 / 7;
		lp.height = LayoutValue.SCREEN_WIDTH * 4 / 9;
		errorDialog.getWindow().setAttributes(lp);
		errorDialog.setCancelable(false);
		errorDialog.show();
		errorDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	/**
	 * 打开GPS提示框
	 * 
	 * @param message
	 * @param btn1Text
	 * @param btn2Text
	 * @param onclicklistener
	 */
	public void showOpenGPSDialog(String message, int btn1Text, int btn2Text,
			OnClickListener onclicklistener) {
		// 防止同时弹出多个错误提示框，引用覆盖的问题。 没弹出一个，dismiss掉该引用之前指向的对象
		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		errorDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		errorDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (event.getKeyCode() == event.KEYCODE_BACK) {
					ActivityTaskManager.getInstance().removeAllActivity();
				}
				return false;
			}
		});
		View v = errorDialog.initMentionDialogView(null, message, btn1Text,
				btn2Text, onclicklistener);
		errorDialog.setContentView(v);
		WindowManager.LayoutParams lp = errorDialog.getWindow().getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH * 6 / 7;
		lp.height = LayoutValue.SCREEN_HEIGHT * 6 / 7;
		lp.gravity = Gravity.CENTER;
		errorDialog.getWindow().setAttributes(lp);
		errorDialog.setCancelable(true);
		errorDialog.show();
		errorDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	/**
	 * 关闭所有的对话框
	 */
	public void closeAllDialog() {
		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		if (messageDialog != null && messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		if (securityDialog != null && securityDialog.isShowing()) {
			securityDialog.dismiss();
		}
	}

	/**
	 * 显示选择安全因子的选择框
	 */
	public void showSeurityFactorDialog(
			final OnItemClickListener onItemClickListener,
			SecurityFactorAdapter sfAdatper) {
		if (securityDialog != null && securityDialog.isShowing()) {
			securityDialog.dismiss();
		}
		securityDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);

		View v = LayoutInflater.from(currentAct).inflate(
				R.layout.security_list, null);
		final ListView listview = (ListView) v.findViewById(R.id.listview);
		listview.setAdapter(sfAdatper);
		// yuht.重置密码部分调用了此方法，选择安全工具后，没有关闭弹出框，因此做此修改
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (securityDialog.isShowing())
					securityDialog.dismiss();
				if (onItemClickListener != null)
					onItemClickListener.onItemClick(parent, listview, position,
							id);
			}

		});
		securityDialog.setContentView(v);

		WindowManager.LayoutParams lp = securityDialog.getWindow()
				.getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH * 6 / 7;
		lp.height = LayoutValue.SCREEN_HEIGHT / 2;
		securityDialog.getWindow().setAttributes(lp);
		BaseHttpEngine.dissMissProgressDialog();
		securityDialog.show();
		securityDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	/**
	 * 密码输入框
	 * 
	 * @param view
	 */
	public void showSipDialog(View view) {
		// added by nl. 防止同时弹出多个错误提示框，引用覆盖的问题。 每弹出一个，dismiss掉该引用之前指向的对象
		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		errorDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		errorDialog.setContentView(view);
		WindowManager.LayoutParams lp = errorDialog.getWindow().getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH * 6 / 7;
		lp.height = LayoutValue.SCREEN_WIDTH / 2;
		errorDialog.getWindow().setAttributes(lp);
		errorDialog.show();
		errorDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	/**
	 * 转出账户 弹出框
	 * 
	 * @param listView
	 */
	public void showTranoutDialog(ListView listView,
			View.OnClickListener backListener,
			View.OnClickListener rightTopListener) {
		showTranoutDialog(listView,
				context.getString(R.string.tran_acc_out_top_title),
				backListener, rightTopListener);
	}

	/**
	 * 转出账户 弹出框
	 * 
	 * @param listView
	 *            展示的listView
	 * @param title
	 *            自定义标题
	 * @param backListener
	 *            左按钮监听
	 * @param rightTopListener
	 *            右按钮监听
	 */
	public void showTranoutDialog(ListView listView, String title,
			View.OnClickListener backListener,
			View.OnClickListener rightTopListener) {
		if (messageDialog != null && messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		messageDialog.setCancelable(false);

		View content = messageDialog.initTranOutView(listView, title,
				backListener, rightTopListener);
		messageDialog.setContentView(content);
		WindowManager.LayoutParams lp = messageDialog.getWindow()
				.getAttributes();
		lp.width = ViewGroup.LayoutParams.FILL_PARENT;
		lp.height = ViewGroup.LayoutParams.FILL_PARENT;
		messageDialog.getWindow().setAttributes(lp);
		messageDialog.getWindow().setWindowAnimations(
				R.style.shotcutDialogAnimation);
		messageDialog.show();
		messageDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	/**
	 * 获取屏幕超时的提示信息
	 * 
	 * @return
	 */
	public String getScreenTimeoutString() {
		int screenTimeOutMinit = screenOutTime / 60;
		return context.getResources().getString(R.string.screentimeoutpre)
				+ screenTimeOutMinit
				+ context.getResources().getString(R.string.screentimeoutback);
	}

	/**
	 * 创建spinner弹窗内容
	 * 
	 * @param v
	 * @param title
	 */
	public void showSpinnerDialog(ListView v, String title) {
		if (messageDialog != null && messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		messageDialog.setCancelable(true);
		View content = messageDialog.initAnnuityDialogView(v, title);
		messageDialog.setContentView(content);
		WindowManager.LayoutParams lp = messageDialog.getWindow()
				.getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH * 6 / 7;
		lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		messageDialog.getWindow().setAttributes(lp);
		messageDialog.show();
		messageDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	public void showAudioKeyModifyPwdDialog(View view) {

		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		errorDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		errorDialog.setContentView(view);
		WindowManager.LayoutParams lp = errorDialog.getWindow().getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH * 6 / 7;
		lp.height = LayoutValue.SCREEN_HEIGHT * 6 / 7;
		lp.gravity = Gravity.CENTER;
		errorDialog.getWindow().setAttributes(lp);
		errorDialog.setCancelable(true);
		errorDialog.show();
		errorDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

//	public Activity getActFirst() {
//		return actFirst;
//	}
//
//	public void setActFirst(Activity actFirst) {
//		this.actFirst = actFirst;
//	}
//
//	public Activity getActSecond() {
//		return actSecond;
//	}
//
//	public void setActSecond(Activity actSecond) {
//		this.actSecond = actSecond;
//	}
//
//	public Activity getActThree() {
//		return actThree;
//	}
//
//	public void setActThree(Activity actThree) {
//		this.actThree = actThree;
//	}
//
//	public Activity getActFor() {
//		return actFor;
//	}
//
//	public void setActFor(Activity actFor) {
//		this.actFor = actFor;
//	}
//
//	public Activity getActfive() {
//		return actfive;
//	}
//
//	public void setActfive(Activity actfive) {
//		this.actfive = actfive;
//	}
//
//	public ImageAndText getFastItemCHoosed() {
//		return fastItemCHoosed;
//	}
//
//	public void setFastItemCHoosed(ImageAndText fastItemCHoosed) {
//		this.fastItemCHoosed = fastItemCHoosed;
//	}

	@Override
	public void commonHttpResponseNullCallBack(String requestMethod) {
		// TODO Auto-generated method stub

	}

	public String getLoginInfo() {
		return loginInfo;
	}

	public void setLoginInfo(String loginInfo) {
		this.loginInfo = loginInfo;
	}

	// @Override
	// public void unregisterReceiver(BroadcastReceiver receiver) {
	// // TODO Auto-generated method stub
	// super.unregisterReceiver(receiver);
	// }

	public void showcancheckErrorDialog(SpannableString sp,
			View.OnClickListener onclickListener) {

		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		errorDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = errorDialog.initMentionDialogView(sp, onclickListener);
		showDialog(v);
	}

	/** */
	public void showLargeDialog(SpannableString sp,
			View.OnClickListener onclickListener) {

		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		errorDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = errorDialog.initLargeDialogView(sp, onclickListener);
		showDialog(v);
	}

	public void showFundEmptyDialog(SpannableString sp,
			View.OnClickListener onclickListener) {

		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		errorDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = errorDialog.fundEmptyDialogView(sp, onclickListener);
		showDialog(v);
	}

	/**
	 * P501音频key(中银E盾) 音频接口是否插入设备
	 * 
	 * @return
	 */
	public boolean isWiredHeadsetOn() {
		return mAudioManager != null && mAudioManager.isWiredHeadsetOn();
	}

	public LoginObserver getLoginObserver() {
		return loginObserver;
	}

	public void setLoginObserver(LoginObserver loginObserver) {
		this.loginObserver = loginObserver;
	}

	// ///////////////////////////////////判断是否绑定当前设备and判断开通单手机手机码///////////////////////////////////////////////
	/**
	 * 请求登录信息
	 */
	private void requestForLoginInfo() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.LOGIN_INFO_API);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForLoginInfoCallBack");
	}

	/**
	 * 请求登录信息的回调
	 */
	public void requestForLoginInfoCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		operatorId = resultMap.get("operatorId") + "";
		hasBindingDevice = resultMap.get("hasBindingDevice") + "";
		final String localBindInfo = SharedPreUtils.getInstance().getString(
				ConstantGloble.SHARED_PREF_LOCAL_BIND_INFO, "");
     	String localBindInfo_mac = SharedPreUtils.getInstance().getString(ConstantGloble.SHARED_PREF_LOCAL_BIND_INFO_MAC, "");// MAC
    	String cfcamacString = DeviceInfoTools.getLocalCAOperatorId(currentAct, BaseDroidApp.getInstanse().getOperatorId(),2);// mac
		final String cfcaString = DeviceInfoTools.getLocalCAOperatorId(currentAct,operatorId,1); // CFCA获取

		if ("1".equals(hasBindingDevice)) {
			if (!"".equals(localBindInfo) && cfcaString.equals(localBindInfo)) {
				requestForPsnSvrQueryMessage();
				return;
			} else if(cfcamacString.equals(localBindInfo_mac)){
				requestForPsnSvrQueryMessage();
				return;
			}
			
			else if ("".equals(localBindInfo)) {
				// TODO 解绑非本机
				showErrorDialog(
						context.getString(R.string.is_binding_other_devices),
						R.string.cancle, R.string.confirm,
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								switch ((Integer) v.getTag()) {
								case CustomDialog.TAG_CANCLE:
									if (errorDialog != null
											&& errorDialog.isShowing()) {
										errorDialog.dismiss();
									}
									break;
								case CustomDialog.TAG_SURE:
									Intent intent = new Intent(currentAct,
											HardwareBindingActivity.class);
									intent.putExtra("is_open_activated",
											"unbind_other_machine");
									currentAct.startActivity(intent);
									currentAct.finish();
									break;
								}
							}
						});
			} else {
				// TODO 提示用户无法绑定
				showMessageDialog(
						context.getResources()
								.getString(
										R.string.hardware_trading_unbind_can_not_unbind_info),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								BaseDroidApp.getInstanse()
										.dismissMessageDialog();
							}
						});
			}
		} else if ("0".equals(hasBindingDevice)) {
			if ("".equals(localBindInfo) || cfcaString.equals(localBindInfo)) {
				// 弹框提示用户绑定
				showErrorDialog(
						context.getString(R.string.not_binding_current_devices),
						R.string.cancle, R.string.confirm,
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								switch ((Integer) v.getTag()) {
								case CustomDialog.TAG_CANCLE:
									if (errorDialog != null
											&& errorDialog.isShowing()) {
										errorDialog.dismiss();
									}
									break;
								case CustomDialog.TAG_SURE:
									Intent intent = new Intent(currentAct,
											HardwareBindingActivity.class);
									intent.putExtra("is_open_activated",
											"bind_own_machine");
									currentAct.startActivity(intent);
									currentAct.finish();
									break;
								}
							}
						});
			} else {
				// TODO 提示用户无法绑定
				showMessageDialog(
						context.getResources()
								.getString(
										R.string.hardware_trading_unbind_can_not_unbind_info),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								BaseDroidApp.getInstanse()
										.dismissMessageDialog();
							}
						});
			}
		}

		BaseHttpEngine.dissMissProgressDialog();
	}

	


	/**
	 * 请求 查询客户是否开通单短信（理财版、vip版用户）
	 */
	public void requestForPsnSvrQueryMessage() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.METHOD_PSNSVRQUERYMESSAGE);
		biiRequestBody.setConversationId(null);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this,
				"queryPsnSvrQueryMessageCallBack");
	}

	private View bindAndOpenView;
	private View.OnClickListener bindAndOpenOnclickListener;

	/**
	 * 请求 查询客户是否开通单短信（理财版、vip版用户）回调
	 */
	public void queryPsnSvrQueryMessageCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		// 开通标识false:未开通 true:已开通
		String flag = result.get("flag") + "";
		// false:未开通 true:已开通
		if ("true".equals(flag)) {
			bindAndOpenOnclickListener.onClick(bindAndOpenView);
		} else {
			// 弹框提示用户绑定
			showErrorDialog(
					context.getResources().getString(
							R.string.not_open_transaction_code),
					R.string.cancle, R.string.confirm, new OnClickListener() {

						@Override
						public void onClick(View v) {
							switch ((Integer) v.getTag()) {
							case CustomDialog.TAG_CANCLE:
								if (errorDialog != null
										&& errorDialog.isShowing()) {
									errorDialog.dismiss();
								}
								break;
							case CustomDialog.TAG_SURE:
								Intent intent = new Intent(currentAct,
										SafetyToolsActivity.class);
								intent.putExtra("is_open_activated",
										"is_open_activated0");
								currentAct.startActivity(intent);
								currentAct.finish();
								break;
							}
						}
					});
		}
	}

	// --------------------x
	// - 提示内容 -
	// - -
	// --------------------
	/**
	 * 只有一个叉按钮提示框
	 * 
	 * @param prompt
	 *            提示信息
	 * @param onclickListener
	 */
	public void ShowPromptDialog(String prompt,
			View.OnClickListener onclickListener) {
		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
		}
		errorDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		View v = errorDialog.largeDialogView(prompt, onclickListener);
		showDialog(v);
	}

	/**
	 * 底部显示Dialog (上面:Spinner 下面取消按钮)
	 * @param currencyPair
	 *            货币对数据
	 * @param onCurrencyPairsItemClickListener
	 *            litview item点击事件监听
	 */
	public void showSelectCurrencyPairDialog(Context context, List<Map<String,Object>> currencyPair,
											 AdapterView.OnItemClickListener onCurrencyPairsItemClickListener) {
		if (messageDialog != null && messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog = new CustomDialog(currentAct, R.style.Theme_Dialog);
		messageDialog.setCancelable(false);
		View v = messageDialog.currencyPairDialogView(context , currencyPair,
				onCurrencyPairsItemClickListener);
		showBottomDialog(v, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	/**
	 * 用来展示底部显示Dialog（不包括 errorDialog）
	 * @param view
	 *            所要展示的视图
	 * @param width
	 *            要展示视图的宽
	 * @param height
	 *            要展示视图的高
	 */
	public void showBottomDialog(View view, int width, int height) {
		if (messageDialog.isShowing()) {
			messageDialog.dismiss();
		}
		messageDialog.setContentView(view);
		WindowManager.LayoutParams lp = messageDialog.getWindow().getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH;
		lp.height = LayoutValue.SCREEN_HEIGHT * 4 / 10;
		lp.gravity = Gravity.BOTTOM; // 紧贴底部
		messageDialog.getWindow().setAttributes(lp);
		// messageDialog.getWindow().setWindowAnimations(R.style.shotcutDialogAnimation);
		messageDialog.show();
		messageDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

}
