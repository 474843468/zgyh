package com.boc.bocsoft.remoteopenacc.common.service;

import com.boc.bocsoft.remoteopenacc.common.model.BaseResultModel;

import android.content.Context;
import android.os.Message;

public class SimpleServiceCallback extends ServiceCallback{

	private final OnTaskFinishListener listener;
	private final int requestCode;
	
	public SimpleServiceCallback(Context context, 
			int requestCode,
			Class<? extends BaseResultModel<?>> targetModel,
			OnTaskFinishListener listener) {
		super(context);
		this.requestCode = requestCode;
		setTargetModel(targetModel);
		this.listener = listener;
	}
	
	@Override
	public void onSuccess(Object result) {
		Message message = Message.obtain();
		message.what = requestCode;
		if (getTargetModel() != null) {//无返回结果的情况
			try {
				BaseResultModel<?> model = getTargetModel().newInstance();
				message.obj = model.parseResultModel(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(listener!=null){
			listener.onTaskSuccess(message);
		}
		
	}

	@Override
	public void onFault(Exception error) {
		Message message = Message.obtain();
		message.what = requestCode;
		message.obj = error;
		if(listener!=null){
			listener.onTaskFault(message);
		}
		
	}

}
