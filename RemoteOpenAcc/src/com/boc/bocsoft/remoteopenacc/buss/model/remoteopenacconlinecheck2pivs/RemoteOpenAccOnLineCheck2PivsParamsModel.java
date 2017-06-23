package com.boc.bocsoft.remoteopenacc.buss.model.remoteopenacconlinecheck2pivs;

import com.boc.bocma.serviceinterface.op.interfacemodel.remoteopenacconlinecheck2pivs.MAORemoteOpenAccOnLineCheck2PivsParamsModel;
import com.boc.bocsoft.remoteopenacc.common.model.BaseParamsModel;

/**
 * 1.2.17 【SA9207】远程开户单笔联网核查接口tv0001
 * 
 * @author gwluo
 * 
 */
public class RemoteOpenAccOnLineCheck2PivsParamsModel implements BaseParamsModel {
	/**
	 * 网点机构号 String X(5) 必输
	 */
	public String siaOrgidt;
	/**
	 * 渠道标识 X(7) Y 渠道标识 Bancslink：0000001 网银：0000002 银医合作：0000003 自助发卡：0000004
	 * IC卡POS渠道：0000007 财富管理：0000005 POS渠道：0000008 VTM渠道： 0000009
	 */
	public String siaChannelFlag;
	/**
	 * 证件号 X(32) 必填 后补空格
	 */
	public String idNo;
	/**
	 * 证件类型 X (2) 必填 后补空格
	 */
	public String idType;
	/**
	 * 客户姓名 X(160) 必填 后补空格
	 */
	public String name;
	/**
	 * 版本号 X(32) 必填 后补空格
	 */
	public String picVer;
	/**
	 * 影像ID X (32) 必填 后补空格
	 */
	public String picId;

	@Override
	public MAORemoteOpenAccOnLineCheck2PivsParamsModel transformParamsModel() {
		MAORemoteOpenAccOnLineCheck2PivsParamsModel params = new MAORemoteOpenAccOnLineCheck2PivsParamsModel();
		params.siaOrgidt = siaOrgidt;
		params.siaChannelFlag = siaChannelFlag;
		params.idNo = idNo;
		params.idType = idType;
		params.name = name;
		params.picVer = picVer;
		params.picId = picId;
		return params;
	}

}
