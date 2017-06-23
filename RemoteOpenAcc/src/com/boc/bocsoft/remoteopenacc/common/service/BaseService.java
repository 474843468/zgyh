package com.boc.bocsoft.remoteopenacc.common.service;

import android.content.Context;
import android.os.Message;

public class BaseService {
	
	protected Context mContext;
	private OnTaskFinishListener mListener;
	
	public BaseService(Context context, OnTaskFinishListener listener) {
		this.mContext = context;
		this.mListener = listener;
		
	}
	
	/**
	 * 异常处理
	 * 
	 * @param error
	 * @param requestCode
	 */
	protected void onFault(Exception error, int requestCode) {
		Message message = Message.obtain();
		message.what = requestCode;
		message.obj = error;
		if (mListener != null) {
			mListener.onTaskFault(message);
		}
	}
		
}
