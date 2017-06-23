package com.boc.bocma.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.boc.bocma.BuildConfig;


/**
 * 挡板类，保存了保存响应报文到文件中，文件名为请求报文的接口方法名。
 */
public class MAMessageWarehouse {
	private static final String TAG = "MessageWarehouse";
	
	private static final String WAREHOUSE_DIR = "boc_warehouse";
	
	/**
	 * Write response message to warehouse.
	 */
	public static boolean writeResponseContent(String userId, String method, InputStream inputStream) {
		File responseFile = getResponseFile(userId, method);
		if (responseFile == null) {
			return false;
		}
		if (responseFile != null && responseFile.exists()) {
			logd("replace existing response message file.");
		}
		
		File parent = responseFile.getParentFile();
		if (parent != null) {
			parent.mkdirs();
		}
		
		return saveToFile(inputStream, responseFile);
	}
	
	private static File getResponseFile(String userId, String method) {
		File userDir = getUserDir(userId);
		return new File(userDir, method);
	}
	
	/**
	 * Get response content from warehouse which stored in external storage. 
	 */
	public static InputStream getResponseContentFromExternalStorage(String userId, String method) 
	throws IOException {
	    Log.d(TAG, "userId: " + userId + ", method: " + method);
		File responseFile = getResponseFile(userId, method);
		if (responseFile == null || !responseFile.exists()) {
			throw new IOException("Can't find corresponding response message: "
					 + "userId: " + userId + ", method: " + method);
		}
		
		FileInputStream inputStream = new FileInputStream(responseFile);
		return inputStream;
	}
	
	public static InputStream getResponseContent(Context context, String userId, String method) 
	throws IOException {
		String responseFilePath = "";
		if (TextUtils.isEmpty(userId)) {
			responseFilePath = method;
		} else {
			responseFilePath = userId + File.pathSeparator + method;
		}
		InputStream inStream = null;
		try {
		    inStream = getResponseContentFromExternalStorage(userId, method);
		} catch (IOException e ) {
		    Log.d(TAG, "Can't find corresponding response message from external storage.");
		}
		if (inStream == null) {
		    inStream = context.getAssets().open(responseFilePath);
		}
		return inStream;
	}
	
	private static boolean saveToFile(InputStream inputStream, File file) {
		boolean result = false;
		final int IO_BUFFER_SIZE = 8 * 1024;
		BufferedOutputStream out = null;
        BufferedInputStream in = null;
        
        try {
        	in = new BufferedInputStream(inputStream, IO_BUFFER_SIZE);
        	out = new BufferedOutputStream(new FileOutputStream(file), IO_BUFFER_SIZE);

        	int b;
        	while ((b = in.read()) != -1) {
        		out.write(b);
        	}
        	out.flush();
        	result = true;
        } catch (IOException e) {
        	e.printStackTrace();
        } finally {
        	try {
        		if (out != null) {
        			out.close();
        		}
        		if (in != null) {
        			in.close();
        		}
        	} catch (IOException e) {
        		e.printStackTrace();
			}
        }
        return result;
	}
	
	private static void logd(String log) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, log);
		}
	}
	
	private static void loge(String log) {
		if (BuildConfig.DEBUG) {
			Log.e(TAG, log);
		}
	}
	
	private static File getMessageWarehouseDir() {
		if (!isExternalStorageAvailable()) {
			loge("External storage unavailable.");
			return null;
		}
		return new File(Environment.getExternalStorageDirectory(), WAREHOUSE_DIR);
	}
	
	private static boolean isExternalStorageAvailable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}
	
	private static File getUserDir(String userId) {
		File warehouse = getMessageWarehouseDir();
		if (TextUtils.isEmpty(userId)) {
			return warehouse;
		} else {
			return new File(warehouse, userId);
		}
	}
}
