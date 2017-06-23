package com.boc.bocsoft.mobile.bocmobile.base.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;

/**
 * 崩溃日志记录工具
 */
public class BocCrashUtils implements UncaughtExceptionHandler{
	private static final  String TAG = "BocCrashUtils";

	private static BocCrashUtils crashUtils;
	private Context mContext;
	private BocCrashUtils(){
		tempDel();
	}
	
	public static BocCrashUtils getInstance(Context mContext){
		if(crashUtils == null){
			crashUtils = new BocCrashUtils();
		}
		
		crashUtils.mContext = mContext.getApplicationContext();
		return crashUtils;
	}

	private void tempDel(){

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String format = dateFormat.format(new Date());
		File dir = new File(Environment.getExternalStorageDirectory(),
				"boc/"+format+"/");

		File parentFile = dir.getParentFile();
		if(!parentFile.isDirectory())return;
		File[] files = parentFile.listFiles();
		if(files==null || files.length==0)return;
		for (File file:files){
			if(file.getPath().endsWith(".txt")){
				file.delete();
			}
		}
	}
	
	private UncaughtExceptionHandler otherUncaughtExceptionHandler;
	
	public void register(){
		
		Thread mainThread = Looper.getMainLooper().getThread();
		
		UncaughtExceptionHandler handler = mainThread.getUncaughtExceptionHandler();
		if(handler == null){
			handler = Thread.getDefaultUncaughtExceptionHandler();
		}
		
		
		this.otherUncaughtExceptionHandler = handler;
		
		mainThread.setUncaughtExceptionHandler(this);
		
		collectDeviceInfo(mContext);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {

		handlerException(thread, ex);
		
		if(otherUncaughtExceptionHandler != null){
			otherUncaughtExceptionHandler.uncaughtException(thread, ex);
		}
	}
	
	/**
	 * 自己的异常处理
	 * @param thread
	 * @param ex
	 */
	private void handlerException(Thread thread, Throwable ex){
		
		saveCrashInfo2File(ex);
	}
	
	
	private Map<String, String> infos = new HashMap<String, String>();
	// 用于格式化日期,作为日志文件名的一部分
	private DateFormat formatter = new SimpleDateFormat("HH-mm-ss",
			Locale.US);
	
	private String saveCrashInfo2File(Throwable ex) {

		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		LogUtils.e(TAG,"系统异常:"+result);
		sb.append(result);
		try {
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			String fileName = "crash-" + time + "-" + timestamp + ".txt";
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				File dir = new File(getCrashDir());
				if (!dir.exists()) {
					dir.mkdirs();
				}
				
				FileOutputStream fos = new FileOutputStream(new File(dir,
						fileName));
				fos.write(sb.toString().getBytes());
				fos.close();
			}
			return fileName;
		} catch (Exception e) {
		}
		return null;
	}
	
	public static String getCrashDir(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String format = dateFormat.format(new Date());
		File dir = new File(Environment.getExternalStorageDirectory(),
				"boc/crash/"+format+"/");
		return dir.getPath();
	}
	
	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	private void collectDeviceInfo(Context ctx) {
		/*try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
		}*/
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
			} catch (Exception e) {
			}
		}
	}
}
