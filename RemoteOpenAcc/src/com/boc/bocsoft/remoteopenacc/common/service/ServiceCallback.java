package com.boc.bocsoft.remoteopenacc.common.service;

import android.content.Context;

import com.boc.bocsoft.remoteopenacc.common.model.BaseResultModel;

public abstract class ServiceCallback {
	
	private Context context;
	private Class<? extends BaseResultModel<?>> targetModel;
	
	public abstract void onSuccess(Object result);

	public abstract void onFault(Exception error);
	
	public ServiceCallback(Context context) {
		this.context = context;
	}
	
	/**
	 * 通用错误处理 
	 *
	 * @param error
	 */
	public void performOnFault(Exception error) {
//		if (error.getType() == MARemoteException.EXCEPTIONTYPE_RESULT) {
//			Intent intent = new Intent(MEBBaseActivity.ACTION_SERVICE_TIMEOUT);
//			if (MEBAppSession.getInstance(context).mLoginBean != null) {
//				if (error.getError().getCode()
//						.equals(ErrorCode.SESSION_INVALID)
//						|| error.getError().getCode()
//								.equals(ErrorCode.SESSION_TIMEOUT)) {
//					intent.putExtra(MEBBaseActivity.ERROR_MESSAGE,
//							error.getMessage());
//					context.sendBroadcast(intent);
//					return;
//				}
//				if (error.getError().getCode().equals(ErrorCode.ROLE_INVALID)) {
//					intent.putExtra(MEBBaseActivity.ERROR_MESSAGE, error
//							.getError().getErrorMessage());
//					context.sendBroadcast(intent);
//					return;
//				}
//			}
//		}
		onFault(error);
	}
	
	public Class<? extends BaseResultModel<?>> getTargetModel() {
		return targetModel;
	}

	public void setTargetModel(Class<? extends BaseResultModel<?>> baseModel) {
		this.targetModel = baseModel;
	}
}
