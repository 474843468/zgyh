package com.chinamworld.bocmbci.utils;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;

import android.os.Environment;

import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.log.LogGloble;

public class SDcardLogUtil {
	private static final String TAG = "SDcardLogUtil";
	
	/**
	 * 将log信息写到文件
	 * 
	 * @param log
	 *            log信息
	 * @param fileName
	 *            文件名称
	 * @param folderName
	 *            文件夹名称
	 */
	public static void logWriteToFile(final String log, String fileName, String folderName) {
		if (!SystemConfig.WRITETOFILE) {
			return;
		}
		if (StringUtil.isNull(fileName) || StringUtil.isNull(folderName)) {
			LogGloble.e(TAG, "FileName or folderName is NULL!");
			return;
		}
		String sdcardpath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + folderName;
//		if (BaseDroidApp.getInstanse().isLogin()) {
//			String loginName = BaseDroidApp.getInstanse().getSharedPrefrences().getString("loginNameForLog", "");
//			sdcardpath = sdcardpath + File.separator + loginName;
//		}
		try {
			// 如果没有该路径则创建该路径
			File f = new File(sdcardpath);
			if (!f.exists()) {
				boolean isMkdir = f.mkdirs();
				if (!isMkdir) {
					LogGloble.i(TAG, "Path did not create success! == " + sdcardpath);
				} else {
					LogGloble.i(TAG, "Path creation success! == " + sdcardpath);
				}
			}
		} catch (final Exception e) {
			LogGloble.e(TAG, "Create path exception! == " + sdcardpath);
			LogGloble.e(TAG, "Exception -> " + e.getMessage(), e);
		}

//		String fileFullName = fileName + ".txt";
		long curDate = System.currentTimeMillis();// 获取当前时间
		SimpleDateFormat formatter1 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");
		SimpleDateFormat formatter2 = new SimpleDateFormat(
				"yyyyMMdd");
		String fileFullName = formatter2.format(curDate) + "Log" + ".txt";
		try {
			File file = new File(sdcardpath, fileFullName);
			if (!file.exists()) {
				boolean isCreate = file.createNewFile();
				if (!isCreate) {
					LogGloble.i(TAG, "File did not create success! == " + sdcardpath + fileFullName);
				} else {
					LogGloble.i(TAG, "File creation success! == " + sdcardpath + fileFullName);
				}
			}
			
			String log1 = formatter1.format(curDate) + " " + fileName + ":" + "\n\t" + log + "\n";  
			writeToFile(file, log1,true);
		} catch (final Exception e) {
			LogGloble.e(TAG, "Create file exception! == " + sdcardpath + fileFullName);
			LogGloble.e(TAG, "Exception -> " + e.getMessage(), e);
		}
	}
	
	/**
	 * 将log信息写到文件
	 * 
	 * @param log
	 *            log信息
	 * @param fileName
	 *            文件名称
	 * @param folderName
	 *            文件夹名称
	 * @param append
	 *            是否文件续写（如果为false，文件中保留的是最近一次写入的信息）
	 */
	public static void logWriteToFile(final String log, String fileName, String folderName, boolean append) {
		if (!SystemConfig.WRITETOFILE) {
			return;
		}
		if (StringUtil.isNull(fileName) || StringUtil.isNull(folderName)) {
			LogGloble.e(TAG, "FileName or folderName is NULL!");
			return;
		}
		String sdcardpath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + folderName;
		if (BaseDroidApp.getInstanse().isLogin()) {
			String loginName = BaseDroidApp.getInstanse().getSharedPrefrences().getString("loginNameForLog", "");
			sdcardpath = sdcardpath + File.separator + loginName;
		}
		try {
			// 如果没有该路径则创建该路径
			File f = new File(sdcardpath);
			if (!f.exists()) {
				boolean isMkdir = f.mkdirs();
				if (!isMkdir) {
					LogGloble.i(TAG, "Path did not create success! == " + sdcardpath);
				} else {
					LogGloble.i(TAG, "Path creation success! == " + sdcardpath);
				}
			}
		} catch (final Exception e) {
			LogGloble.e(TAG, "Create path exception! == " + sdcardpath);
			LogGloble.e(TAG, "Exception -> " + e.getMessage(), e);
		}
//		String fileFullName = fileName + ".txt";
		long curDate = System.currentTimeMillis();// 获取当前时间
		SimpleDateFormat formatter1 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");
		SimpleDateFormat formatter2 = new SimpleDateFormat(
				"yyyyMMdd");
		String fileFullName = formatter2.format(curDate) + "Log" + ".txt";
		try {
			File file = new File(sdcardpath, fileFullName);
			if (!file.exists()) {
				boolean isCreate = file.createNewFile();
				if (!isCreate) {
					LogGloble.i(TAG, "File did not create success! == " + sdcardpath + fileFullName);
				} else {
					LogGloble.i(TAG, "File creation success! == " + sdcardpath + fileFullName);
				}
			}
			String log1 = formatter1.format(curDate) + " " + fileName + ":\n\t" + log + "\n";
			writeToFile(file, log1, append);
		} catch (final Exception e) {
			LogGloble.e(TAG, "Create file exception! == " + sdcardpath + fileFullName);
			LogGloble.e(TAG, "Exception -> " + e.getMessage(), e);
		}
	}
	
	/**
	 * 写log到文件
	 * 
	 * @param fileName
	 *            文件
	 * @param log
	 *            log信息
	 */
	private static void writeToFile(File fileName, String log) {
		log += "\n";
		FileWriter out = null;
		try {
			out = new FileWriter(fileName, true);
			out.write(log);
			out.flush();
			out.close();
		} catch (Exception e) {
			LogGloble.e(TAG, "Write file exception!");
			LogGloble.e(TAG, "Exception -> " + e.getMessage(), e);
		}
	}
	
	/**
	 * 写log到文件
	 * 
	 * @param fileName
	 *            文件
	 * @param log
	 *            log信息
	 */
	private static void writeToFile(File fileName, String log, boolean append) {
		log += "\n";
		FileWriter out = null;
		try {
			out = new FileWriter(fileName, append);
			out.write(log);
			out.flush();
			out.close();
		} catch (Exception e) {
			LogGloble.e(TAG, "Write file exception!");
			LogGloble.e(TAG, "Exception -> " + e.getMessage(), e);
		}
	}
//
//	/**
//	 * 处理Exception(记录日志)
//	 * 
//	 * @param th
//	 */
//	public static void processException(final String log, String requestMethod) {
//		if (!SystemConfig.WRITETOFILE) {
//			return;
//		}
//		try {
//			String sdcardpath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "BIILog";
//			if (BaseDroidApp.getInstanse().isLogin()) {
//				String loginName = BaseDroidApp.getInstanse().getSharedPrefrences().getString("loginNameForLog", "");
//				sdcardpath = sdcardpath + File.separator + loginName;
//			}
//			File f = new File(sdcardpath);
//			if (!f.exists()) {
//				f.mkdir();
//			}
//			File file = new File(sdcardpath, requestMethod + ".txt");
//			if (!file.exists()) {
//				file.createNewFile();
//			}
//			writeToFile(log, file);
//		} catch (final Exception e) {
//			LogGloble.exceptionPrint(e);
//		}
//	}
//	
//	/**
//	 * 写日志
//	 * 
//	 * @param stacktrace
//	 * @param filename
//	 */
//	private static void writeToFile(final String stacktrace, final File filename) {
//		try {
//			final BufferedWriter bos = new BufferedWriter(new FileWriter(filename));
//
//			// bos.write("Application information:\n\n");
//			// bos.write("This file was generated by the "
//			// + BaseDroidApp.APP_PACKAGE + "."
//			// + BaseDroidApp.APP_VERSION_NAME + "\n");
//			// bos.write("\nDevice information:\n\n");
//			// bos.write("VERSION     : " + AndroidVersion.VERSION + "\n");
//			// bos.write("BOARD       : " + Build.BOARD + "\n");
//			// bos.write("BRAND       : " + Build.BRAND + "\n");
//			// bos.write("CPU_ABI     : "
//			// + BaseDroidApp.BUILD_PROPS
//			// .getProperty("ro.product.cpu.abi") + "\n");
//			// bos.write("CPU_ABI2    : "
//			// + BaseDroidApp.BUILD_PROPS
//			// .getProperty("ro.product.cpu.abi2") + "\n");
//			// bos.write("DEVICE      : " + Build.DEVICE + "\n");
//			// bos.write("DISPLAY     : " + Build.DISPLAY + "\n");
//			// bos.write("FINGERPRINT : " + Build.FINGERPRINT + "\n");
//			// bos.write("ID          : " + Build.ID + "\n");
//			// bos.write("MANUFACTURER: "
//			// + BaseDroidApp.BUILD_PROPS
//			// .getProperty("ro.product.manufacturer") + "\n");
//			// bos.write("MODEL       : " + Build.MODEL + "\n");
//			// bos.write("PRODUCT     : " + Build.PRODUCT + "\n");
//			// bos.write("\nError information:\n\n");
//			bos.write(stacktrace);
//			bos.flush();
//			bos.close();
//		} catch (final Exception e) {
//			LogGloble.exceptionPrint(e);
//			LogGloble.e(TAG, "写文件异常");
//		}
//	}
}
