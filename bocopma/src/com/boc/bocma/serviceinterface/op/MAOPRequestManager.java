package com.boc.bocma.serviceinterface.op;
import java.util.ArrayList;
import java.util.List;

import com.boc.bocma.network.communication.http.MAOPHttpUtils;

import android.os.AsyncTask;
public class MAOPRequestManager {
	private static List<AsyncTask> tasks = new ArrayList<AsyncTask>();
	
	public static synchronized void addTask(AsyncTask mTask){
		tasks.add(mTask);
	}
	
	public static synchronized void removeTask(AsyncTask mTask){
		tasks.remove(mTask);
	}
	
	/**
	 * 中断所有请求
	 */
	public static void cancelAllRequest(){
		for (AsyncTask mTask : tasks) {
			mTask.cancel(true);
		}
		tasks.clear();
		MAOPHttpUtils.cancelAllRequest();
	}
	
	public static void resetHttpClient(){
		for (AsyncTask mTask : tasks) {
			mTask.cancel(true);
		}
		tasks.clear();
		MAOPHttpUtils.resetHttpClient();
	}
}
