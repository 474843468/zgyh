package com.boc.bocsoft.remoteopenacc.buss.model.remoteopenacconlinecheck2pivs;

import com.boc.bocma.serviceinterface.op.interfacemodel.remoteopenacconlinecheck2pivs.MAORemoteOpenAccOnLineCheck2PivsResponsModel;
import com.boc.bocsoft.remoteopenacc.common.model.BaseResultModel;

/**
 * 1.2.17 【SA9207】远程开户单笔联网核查接口tv0001
 * 
 * @author gwluo
 * 
 */
public class RemoteOpenAccOnLineCheck2PivsResponsModel implements
		BaseResultModel<RemoteOpenAccOnLineCheck2PivsResponsModel> {
	/**
	 * 更新成功标识 X(1) 必填 1. 更新成功 2. 更新失败
	 */
	public String success;

	@Override
	public RemoteOpenAccOnLineCheck2PivsResponsModel parseResultModel(Object resultModel) {
		MAORemoteOpenAccOnLineCheck2PivsResponsModel result = (MAORemoteOpenAccOnLineCheck2PivsResponsModel) resultModel;
		success = result.success;
		return this;
	}

}
