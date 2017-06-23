package com.boc.bocsoft.remoteopenacc.common.service;

import com.boc.bocma.exception.MAOPException;
import com.boc.bocma.serviceinterface.op.OnOPResultCallback;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

public class HandlerAdapte implements OnOPResultCallback{
	
	private ServiceCallback callback;
	public HandlerAdapte(ServiceCallback callback) {
		this.callback = callback;
	}
	@Override
	public void onSuccess(MAOPBaseResponseModel resModel) {
		callback.onSuccess(resModel);
		
	}
	@Override
	public void onFault(MAOPException ex) {
		callback.performOnFault(ex);
		
	}


}
