package com.boc.bocma.serviceinterface.op;

import com.boc.bocma.exception.MAOPException;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

public interface OnOPResultCallback {
    public void onSuccess(MAOPBaseResponseModel result);
    public void onFault(MAOPException e);
}
