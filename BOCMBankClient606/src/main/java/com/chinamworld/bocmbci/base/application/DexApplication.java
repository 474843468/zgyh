package com.chinamworld.bocmbci.base.application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.support.multidex.MultiDexApplication;
import android.support.multidex.MultiDex;

import com.chinamworld.bocmbci.constant.SystemConfig;

import dalvik.system.DexClassLoader;

public class DexApplication extends MultiDexApplication /*Application*/ {
	public static Context cont;
	public static DexApplication instanse = null;

	public void onCreate() {
		super.onCreate();
		cont = getApplicationContext();
//		if(SystemConfig.IS_SPLIDE_DEX){
//			dexTool();
//		}
		
//		BaseDroidApp instanse = new BaseDroidApp();
//		instanse.onCreate(cont);
	}
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
	@Override
	public void onTerminate() {	
		BaseDroidApp.getInstanse().onTerminate();
		super.onTerminate();
		
	}
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		BaseDroidApp.getInstanse().onLowMemory();
	}
	@SuppressLint("NewApi")
	private void dexTool() {

		File dexDir = new File(getFilesDir(), "dlibs");
		dexDir.mkdir();
		File dexFile = new File(dexDir, "libs.apk");
		File dexOpt = getCacheDir();
		try {
			InputStream ins = getAssets().open("libs.apk");

			if (dexFile.length() != ins.available()) {

				FileOutputStream fos = new FileOutputStream(dexFile);
				byte[] buf = new byte[4096];
				int l;
				while ((l = ins.read(buf)) != -1) {
					fos.write(buf, 0, l);
				}
				fos.close();
			}
			ins.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		ClassLoader cl = getClassLoader();
		ApplicationInfo ai = getApplicationInfo();
		String nativeLibraryDir = null;
		if (Build.VERSION.SDK_INT > 8) {

			nativeLibraryDir = ai.nativeLibraryDir;
		} else {
			nativeLibraryDir = "/data/data/" + ai.packageName + "/lib/";
		}

		DexClassLoader dcl = new DexClassLoader(dexFile.getAbsolutePath(),
				dexOpt.getAbsolutePath(), nativeLibraryDir, cl.getParent());
		
		  try {
		        Object dexElements = combineArray(getDexElements(getPathList(cl)), getDexElements(getPathList(dcl)));
		        Object pathList = getPathList(cl);
		        setField(pathList, pathList.getClass(), "dexElements", dexElements);
		        return ;
		      } catch (Throwable e) {
		        e.printStackTrace();
		        return ;
		      }
		  
		  
//		try {
//			Field f = ClassLoader.class.getDeclaredField("parent");
//			f.setAccessible(true);
//			f.set(cl, dcl);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
	}

	private static Object getPathList(Object baseDexClassLoader)
			throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
		return getField(baseDexClassLoader, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
	}

	private static Object getField(Object obj, Class<?> cl, String field)
			throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field localField = cl.getDeclaredField(field);
		localField.setAccessible(true);
		return localField.get(obj);
	}

	private static Object getDexElements(Object paramObject)
			throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		return getField(paramObject, paramObject.getClass(), "dexElements");
	}
	private static void setField(Object obj, Class<?> cl, String field,
			Object value) throws NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {

		Field localField = cl.getDeclaredField(field);
		localField.setAccessible(true);
		localField.set(obj, value);
	}
	private static Object combineArray(Object arrayLhs, Object arrayRhs) {
		Class<?> localClass = arrayLhs.getClass().getComponentType();
		int i = Array.getLength(arrayLhs);
		int j = i + Array.getLength(arrayRhs);
		Object result = Array.newInstance(localClass, j);
		for (int k = 0; k < j; ++k) {
			if (k < i) {
				Array.set(result, k, Array.get(arrayLhs, k));
			} else {
				Array.set(result, k, Array.get(arrayRhs, k - i));
			}
		}
		return result;
	}
}
