/**
 * 文件名	：BaseApplication.java
 * 创建日期	：2012-10-15
 * Copyright (c) 2003-2012 北京联龙博通

 * All rights reserved.
 */
package com.chinamworld.bocmbci.base.application;

import java.io.File;
import java.io.FileInputStream;
import java.util.Locale;
import java.util.Properties;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Environment;

import com.chinamworld.bocmbci.android.AndroidVersion;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.LengthUtils;

/**
 * 描述:BaseApplication
 * <p />
 * 
 * 基础Application
 * 
 * <p />
 * 
 * @version 1.00
 * @author wez
 * @date 2012-10-23 10:06:25
 * 
 */
public abstract class BaseApplication extends CommonApplication /*extends Application*/ {

	private static final String TAG = "BaseApplication";

	public static Context context;

	public static int APP_VERSION_CODE;

	public static String APP_VERSION_NAME;

	public static String APP_PACKAGE;

	public static File EXT_STORAGE;

	public static File APP_STORAGE;

	public static Properties BUILD_PROPS;

	public static String APP_NAME;

	public static Locale defLocale;

	public static Locale appLocale;
	
	public static int vipMessageCount,newMessageCount;

//	@Override
//	public void onCreate() {
//		super.onCreate();
//		LogGloble.i(TAG, "BaseApplicationon onCreate...");
//
//		// 初始化UncaughtException处理类
//	
//	}
//
//	@Override
	public void onLowMemory() {
//		super.onLowMemory();
	//BitmapManager.clear("on Low Memory: ");
	}
//
//	@Override
//	public void onTerminate() {
//		LogGloble.i(TAG, "BaseApplicationon onTerminate...");
//		super.onTerminate();
//	}

	/**
	 * 初始化基础信息
	 */
	protected void init() {
//		context = BaseDroidApp.getContext();

		final Configuration config = context.getResources().getConfiguration();
		appLocale = defLocale = config.locale;

		BUILD_PROPS = new Properties();
		try {
			BUILD_PROPS.load(new FileInputStream("/system/build.prop"));
		} catch (final Throwable th) {
		}

		final PackageManager pm = context.getPackageManager();
		try {
			final PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			APP_NAME = context.getString(pi.applicationInfo.labelRes);
			APP_VERSION_CODE = pi.versionCode;
			APP_VERSION_NAME = LengthUtils.safeString(pi.versionName, "DEV");
			APP_PACKAGE = pi.packageName;
			EXT_STORAGE = Environment.getExternalStorageDirectory();
			APP_STORAGE = getAppStorage(APP_PACKAGE);

			LogGloble.i(APP_NAME, APP_NAME + " (" + APP_PACKAGE + ")" + " " + APP_VERSION_NAME + "(" + pi.versionCode
					+ ")");
			LogGloble.i(APP_NAME, "Root             dir: " + Environment.getRootDirectory());
			LogGloble.i(APP_NAME, "Data             dir: " + Environment.getDataDirectory());
			LogGloble.i(APP_NAME, "External storage dir: " + EXT_STORAGE);
			LogGloble.i(APP_NAME, "App      storage dir: " + APP_STORAGE);
//			LogGloble.i(APP_NAME, "Files            dir: " + FileUtils.getAbsolutePath(context.getFilesDir()));
//			LogGloble.i(APP_NAME, "Cache            dir: " + FileUtils.getAbsolutePath(context.getCacheDir()));
			LogGloble.i(APP_NAME, "System locale       : " + defLocale);
			LogGloble.i(APP_NAME, "VERSION     : " + AndroidVersion.VERSION);
			LogGloble.i(APP_NAME, "BOARD       : " + Build.BOARD);
			LogGloble.i(APP_NAME, "BRAND       : " + Build.BRAND);
			LogGloble.i(APP_NAME, "CPU_ABI     : " + BUILD_PROPS.getProperty("ro.product.cpu.abi"));
			LogGloble.i(APP_NAME, "CPU_ABI2    : " + BUILD_PROPS.getProperty("ro.product.cpu.abi2"));
			LogGloble.i(APP_NAME, "DEVICE      : " + Build.DEVICE);
			LogGloble.i(APP_NAME, "DISPLAY     : " + Build.DISPLAY);
			LogGloble.i(APP_NAME, "FINGERPRINT : " + Build.FINGERPRINT);
			LogGloble.i(APP_NAME, "ID          : " + Build.ID);
			LogGloble.i(APP_NAME, "MANUFACTURER: " + BUILD_PROPS.getProperty("ro.product.manufacturer"));
			LogGloble.i(APP_NAME, "MODEL       : " + Build.MODEL);
			LogGloble.i(APP_NAME, "PRODUCT     : " + Build.PRODUCT);
		} catch (final NameNotFoundException e) {
			LogGloble.w(TAG, "init NameNotFoundException", e);
		}
	}

//	@Override
//	public void onConfigurationChanged(final Configuration newConfig) {
//		final Configuration oldConfig = getResources().getConfiguration();
//		final int diff = oldConfig.diff(newConfig);
//		final Configuration target = diff == 0 ? oldConfig : newConfig;
//
//		if (appLocale != null) {
//			setAppLocaleIntoConfiguration(target);
//		}
//		super.onConfigurationChanged(target);
//	}

	public static Context getContext() {
		return context;
	}

	

	protected File getAppStorage(final String appPackage) {
		File dir = EXT_STORAGE;
		if (dir != null) {
			final File appDir = new File(dir, "." + appPackage);
			if (appDir.isDirectory() || appDir.mkdir()) {
				dir = appDir;
			}
		} else {
			dir = context.getFilesDir();
		}
		dir.mkdirs();
		return dir.getAbsoluteFile();
	}

	public static void setAppLocale(final String lang) {
		final Configuration config = context.getResources().getConfiguration();
		appLocale = LengthUtils.isNotEmpty(lang) ? new Locale(lang) : defLocale;
		setAppLocaleIntoConfiguration(config);
	}

	protected static void setAppLocaleIntoConfiguration(final Configuration config) {
		if (!config.locale.equals(appLocale)) {
			Locale.setDefault(appLocale);
			config.locale = appLocale;
			context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
		}
		LogGloble.i(APP_NAME, "UI Locale: " + appLocale);
	}

}
