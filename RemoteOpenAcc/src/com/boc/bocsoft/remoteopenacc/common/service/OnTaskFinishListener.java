package com.boc.bocsoft.remoteopenacc.common.service;

import android.os.Message;

/**
 * Service层完成网络请求后回调UI的接口
 *
 */
public interface OnTaskFinishListener {

	public void onTaskSuccess(Message result);
	public void onTaskFault(Message result);
}
